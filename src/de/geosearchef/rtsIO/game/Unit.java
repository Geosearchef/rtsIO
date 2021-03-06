package de.geosearchef.rtsIO.game;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.buildings.Building;
import de.geosearchef.rtsIO.game.gems.Gem;
import de.geosearchef.rtsIO.js.Data;
import de.geosearchef.rtsIO.js.UnitData;
import de.geosearchef.rtsIO.json.units.UpdateUnitMessage;
import de.geosearchef.rtsIO.util.Pair;
import de.geosearchef.rtsIO.util.Vector;

import javax.xml.bind.UnmarshallerHandler;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@lombok.Data
public class Unit extends Targetable {

	private static final int MAXIMUM_UPDATE_DELAY = 400;
	private static final float UNIT_COLLISION_IGNORE_DIST = 0.0000f;//see JS aswell TODO:

	private Player player;
	private int unitID;
	private int unitType;
	private Vector pos;
	private Vector vel;
	private Vector dest;//Not nullable, only matters when vel != 0
	private BuildingTask buildingTask = null;
	private Targetable target = null;
	private long nextAttack = 0;

	//Server sided
	private Optional<Long> needsUpdateSince = Optional.empty(); //e.g. after a collision


	public Unit(Player player, int unitType, Vector pos, float hp) {
		super(Data.getUnitData(unitType).getMaxHp());

		this.player = player;
		this.unitID = IDFactory.generateUnitID();
		this.unitType = unitType;
		this.pos = new Vector(pos);
		this.vel = new Vector();
		this.dest = new Vector();
	}

	public void update(float d) {

		if(vel.lengthSquared() > 0) {
			this.vel = dest.sub(pos).normalise().scale(this.getMoveSpeed());
			if(this.vel.lengthSquared() == 0f) {
				this.broadcastUpdate();
			}
		}

		Vector travel = this.vel.scale(d);
		float travelDistanceSquared = travel.lengthSquared();

		if(travelDistanceSquared > 0f) {
			if(travelDistanceSquared >= dest.sub(pos).lengthSquared()) {
				this.vel = new Vector();
				this.pos = new Vector(this.dest);

				if(buildingTask != null) {
					//Check resources
					boolean enoughResources = this.player.removeResources(Data.getBuildingData(buildingTask.buildingType).getCost());

					//Create new building

					if(enoughResources) {
						Game.addBuilding(new Building(this.getPlayer(), this.buildingTask.buildingType, new Vector(this.dest)));
					}
					buildingTask = null;
				}

				this.broadcastUpdate();
			} else {
				this.pos = this.pos.add(travel);
			}
		}

		//Check for collision with gem
		synchronized (Game.gems) {
			Game.gems.stream()
					.filter(g -> ! g.isSpawner())
					.filter(g -> this.getCenter().sub(g.getCenter()).length() <= (this.getSize() + Gem.SMALL_GEM_SIZE) / 2f)//this radius is based on the entities sides, not diagonals
					.collect(Collectors.toCollection(HashSet::new))//avoid concurrent modification
					.forEach(gem -> {
						Game.consumeGem(gem, this);
					});
		}

		//Check for collision with unit, is already synchronized to Game.units
		Game.units.stream().filter(u -> u != Unit.this)
				.map(u -> new Pair<Unit, Float>(u, u.getCenter().sub(Unit.this.getCenter()).length()))
				.forEach(p -> {
					float neededDist = p.getFirst().getRadius() + Unit.this.getRadius();
					if(p.getSecond() < neededDist - UNIT_COLLISION_IGNORE_DIST) {
						Vector force = this.getCenter().sub(p.getFirst().getCenter()).normaliseOrElse(new Vector((float)Math.random() - 0.5f, (float)Math.random() - 0.5f).normaliseOr1()).scale(2f * (1.0f - p.getSecond() / neededDist) * d);
						this.pos = this.pos.add(force);
						p.getFirst().pos = p.getFirst().pos.add(force.scale(-1f));

						this.requireUpdate();
						p.getFirst().requireUpdate();
					}
				});

		//Check for collision with building
		synchronized (Game.buildings) {
			Game.buildings.stream()
					.map(b -> new Pair<Building, Float>(b, b.getCenter().sub(Unit.this.getCenter()).length()))
					.forEach(p -> {
						float neededDist = p.getFirst().getSize() / 2f + Unit.this.getRadius();
						if(p.getSecond() < neededDist - UNIT_COLLISION_IGNORE_DIST) {
							Vector force = this.getCenter().sub(p.getFirst().getCenter()).normaliseOrElse(new Vector((float)Math.random() - 0.5f, (float)Math.random() - 0.5f).normaliseOr1()).scale(2f * (1.0f - p.getSecond() / neededDist) * d);
							this.pos = this.pos.add(force);

							this.requireUpdate();
						}
					});
		}

		Collection<Targetable> targetables = Game.getTargetables();
		if(target != null && (! targetables.contains(target) || target.getPos().sub(this.pos).lengthSquared() > this.getUnitData().getAttackRange() * this.getUnitData().getAttackRange())) {
			target = null;
		}

		if(target == null && this.getUnitData().getProjectile() != -1) {
			checkForTarget(targetables);
		}


		if(target != null && System.currentTimeMillis() >= this.nextAttack) {
			//attack
 			Game.addProjectile(new Projectile(this.player, this.getCenter(), new Vector(), this.target));
			this.nextAttack = System.currentTimeMillis() + this.getUnitData().getAttackCooldown();//DOES NOT compensate lag
		}

		needsUpdateSince.ifPresent(t -> {
			if(System.currentTimeMillis() - t > MAXIMUM_UPDATE_DELAY) {
				broadcastUpdate();
			}
		});
	}

	//TODO: decrease scan rate
	public void checkForTarget(Collection<Targetable> targetables) {
		//TODO: rasterize????
		Optional<Targetable> newTarget = targetables.stream()
				.filter(t -> t.getPlayer() != this.getPlayer())
				.map(t -> new Pair<Targetable, Float>(t, this.getCenter().sub(t.getCenter()).lengthSquared()))
				.filter(t -> t.getSecond() <= this.getUnitData().getAttackRange() * this.getUnitData().getAttackRange())
				.min((l, r) -> (int) (l.getSecond() - r.getSecond())).map(t -> t.getFirst());

		newTarget.ifPresent(t -> {
			this.target = t;
		});
	}


	public void move(Vector dest) {
		this.dest = dest;
		this.buildingTask = null;

		if(dest.sub(pos).lengthSquared() == 0)
			return;

		this.vel = dest.sub(pos).normalise().scale(this.getMoveSpeed());
		this.broadcastUpdate();
	}

	public void build(Vector dest, int buildingType) {
		move(dest);
		this.buildingTask = new BuildingTask(buildingType);
	}

	public void requireUpdate() {
		if(! needsUpdateSince.isPresent()) {
			needsUpdateSince = Optional.of(System.currentTimeMillis());
		}
	}

	public void broadcastUpdate() {
		PlayerManager.broadcastPlayers(new UpdateUnitMessage(this.getUnitID(), this.getPos(), this.getVel(), this.getDest(), this.getHp()));
		needsUpdateSince = Optional.empty();
	}

	public void damage(float amount, Player source) {
		this.setHp(this.hp - amount);
		this.broadcastUpdate();
		if(this.hp <= 0) {
			Game.removeUnit(this);
		}
	}

	@Override
	public String getTargetType() {
		return "unit";
	}

	@Override
	public int getTargetID() {
		return this.getUnitID();
	}

	@Override
	public int hashCode() {
		return unitID;
	}

	public float getMoveSpeed() {return Data.getUnitData(unitType).getMovementSpeed();}

	private static final float SQRT2 = (float)Math.sqrt(2);
	public float getRadius() {return SQRT2 / 2f * this.getSize();}

	public Vector getCenter() {
		return pos.add(new Vector(this.getSize() / 2.0f, this.getSize() / 2.0f));
	}

	public UnitData getUnitData() {
		return Data.getUnitData(this.unitType);
	}

	@lombok.Data
	public static class BuildingTask {
		private final int buildingType;
	}
}
