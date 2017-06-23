
function Unit(playerID, unitID, unitType, pos, dest, vel, hp) {
    this.playerID = playerID;
    this.unitID = unitID;
    this.unitType = unitType;
    this.pos = pos;
    this.vel = vel;
    this.dest = dest;
    this.hp = hp;

    this.selected = true;
}

function Player(playerID, username) {
    this.playerID = playerID;
    this.username = username;
}