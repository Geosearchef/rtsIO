
function Unit(playerID, unitID, unitType, pos, vel, dest, hp) {
    this.playerID = playerID;
    this.unitID = unitID;
    this.unitType = unitType;
    this.pos = pos;
    this.vel = vel;
    this.dest = dest;
    this.hp = hp;

    this.getSize = function() {return 1.0;};

    if(ownPlayerID === playerID)
        this.selected = true;
}

function Player(playerID, username) {
    this.playerID = playerID;
    this.username = username;
}

function Gem(id, pos, spawner) {
    this.id = id;
    this.pos = pos;
    this.spawner = spawner;
}