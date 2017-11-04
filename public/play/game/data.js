
function Player(playerID, username) {
    this.playerID = playerID;
    this.username = username;
}

function Unit(playerID, unitID, unitType, pos, vel, dest, hp) {
    this.playerID = playerID;
    this.unitID = unitID;
    this.unitType = unitType;
    this.pos = pos;
    this.vel = vel;
    this.dest = dest;
    this.hp = hp;

    this.getSize = function() {return 1.0;};
    this.getRadius = function () {return SQRT2 / 2.0 * this.getSize();};
    this.getCenter = function () {return this.pos.add(new Vector(this.getSize() / 2.0, this.getSize() / 2.0));};

    if(ownPlayerID === playerID)
        this.selected = true;
}

function Gem(id, pos, spawner) {
    this.id = id;
    this.pos = pos;
    this.spawner = spawner;
}

function Building(playerID, buildingID, buildingType, pos, hp, inBuildingProcess) {
    this.playerID = playerID;
    this.buildingID = buildingID;
    this.buildingType = buildingType;
    this.pos = pos;
    this.hp = hp;
    this.inBuildingProcess = inBuildingProcess;

    this.getSize = function() {return 1.0;};
}