package de.geosearchef.rtsIO.game.buildings;

import de.geosearchef.rtsIO.IDFactory;
import de.geosearchef.rtsIO.game.Game;
import de.geosearchef.rtsIO.game.Player;
import de.geosearchef.rtsIO.game.PlayerManager;
import de.geosearchef.rtsIO.game.Targetable;
import de.geosearchef.rtsIO.js.BuildingData;
import de.geosearchef.rtsIO.js.Data;
import de.geosearchef.rtsIO.json.buildings.UpdateBuildingMessage;
import de.geosearchef.rtsIO.util.Vector;

@lombok.Data
public class Building extends Targetable {

	private static float BUILDING_PROCESS_HP_PER_SECOND = 10.0f;//also in js

	private Player player;
	private int buildingID;
	private int buildingType;
	private Vector pos;
	private boolean inBuildingProcess;

	public Building(Player player, int buildingType, Vector pos) {
		super(Data.getBuildingData(buildingType).getMaxHp());

		this.player = player;
		this.buildingType = buildingType;
		this.pos = pos;

		this.buildingID = IDFactory.generateBuildingID();
		this.inBuildingProcess = true;

		this.setHp(1.0f);
	}


	public void update(float d) {
		if(inBuildingProcess) {
			this.setHp(this.getHp() + d * BUILDING_PROCESS_HP_PER_SECOND);
			if(this.getHp() > Data.getBuildingData(buildingType).getMaxHp()) {
				this.setHp(Data.getBuildingData(buildingType).getMaxHp());
				this.inBuildingProcess = false;
				broadcastUpdate();
			}
		}
	}

	public void broadcastUpdate() {
		PlayerManager.broadcastPlayers(new UpdateBuildingMessage(this.getBuildingID(), this.getHp(), this.isInBuildingProcess()));
	}

	public void damage(float amount) {
		this.setHp(this.hp - amount);
		if(this.hp <= 0f) {
			synchronized (Game.buildings) {
				Game.buildings.remove(this);
			}
		}
		this.broadcastUpdate();
	}

	public int getTargetID() {
		return this.getBuildingID();
	}

	public String getTargetType() {
		return "building";
	}

	public float getCost() {
		return Data.getBuildingData(this.buildingType).getCost();
	}
	public float getSize() {return 1.0f;}
	public Vector getCenter() {return this.pos.add(new Vector(getSize() / 2f, getSize() / 2f));}
}
