
function Unit(playerID, unitID, unitType, pos, vel, hp) {
    this.playerID = playerID;
    this.unitID = unitID;
    this.unitType = unitType;
    this.pos = pos;
    this.vel = vel;
    this.hp = hp;
}

function Player(playerID, username) {
    this.playerID = playerID;
    this.username = username;
}