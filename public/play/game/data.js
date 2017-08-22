
function Unit(playerID, unitID, unitType, pos, vel, dest, hp) {
    this.playerID = playerID;
    this.unitID = unitID;
    this.unitType = unitType;
    this.pos = pos;
    this.vel = vel;
    this.dest = dest;
    this.hp = hp;

    if(ownPlayerID === playerID)
        this.selected = true;
}

function Player(playerID, username) {
    this.playerID = playerID;
    this.username = username;
}