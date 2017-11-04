window.onload = init;//set init() to be executed on page loading

const CELL_SCALE = 50.0;
const BUILDING_PROCESS_HP_PER_SECOND = 10.0; //also in java

var MAP_SIZE = new Vector(1, 1);//SET ON GAME INFO


var connected = false;//did the login succeed?
var ownUsername;//own username
var ownPlayerID;//own playerID

var ownResourceAmount = 0;

var players = new Map();
var units = new Map();
var gems = new Map();
var buildings = new Map();




function update(d) {
    units.forEach(function(unit) {
        var travel = unit.vel.scale(d);
        if(travel.lengthSquared() >= unit.pos.sub(unit.dest).lengthSquared()) {
            unit.vel = new Vector(0, 0);
            unit.pos = cloneVector(unit.dest);
        } else {
            unit.pos.setAdd(travel);
        }
    });

    buildings.forEach(function(building) {
        if(building.inBuildingProcess) {
            building.hp += d * BUILDING_PROCESS_HP_PER_SECOND;
            if(building.hp > building.maxHp) {
                building.hp = building.maxHp;
            }
        }
    });
}


function loggedIn() {
    //TODO
}



//maybe outsource to parser???
function onNewUnitMessage(msg) {
    var unit = new Unit(msg.playerID, msg.unitID, msg.unitType, cloneVector(msg.pos), cloneVector(msg.vel), cloneVector(msg.dest), msg.hp);
    units.set(msg.unitID, unit);
}


function onDeleteUnit(id) {
    units.delete(id);
}

function onUpdateUnit(msg) {
    var unit = units.get(msg.unitID);
    unit.pos = cloneVector(msg.pos);
    unit.vel = cloneVector(msg.vel);
    unit.dest = cloneVector(msg.dest);
    unit.hp = msg.hp;
}

function onNewGem(id, pos, spawner) {
    var gem = new Gem(id, pos, spawner);
    gems.set(id, gem);
}

function onDeleteGem(id) {
    gems.delete(id);
}

function onNewBuilding(playerID, buildingID, buildingType, pos, hp, inBuildingProcess) {//TODO: refactor, only pass msg
    var building = new Building(playerID, buildingID, buildingType, pos, hp, inBuildingProcess);
    buildings.set(buildingID, building);
}

function onUpdateBuilding(msg) {
    var building = buildings.get(msg.buildingID);
    building.hp = msg.hp;
    building.inBuildingProcess = msg.inBuildingProcess;
}

function onDeleteBuilding(buildingID) {
    buildings.delete(buildingID);
}


function onPlayerConnect(id, username) {
    players.set(id, new Player(id, username));
}

function onPlayerDisconnect(id) {
    players.delete(id);
}


//TODO: Move this to websocket???
function moveUnits(dest) {
    var unitIDs = getSelectedUnitIDs();
    if(unitIDs.length > 0) {
        send({type: "moveUnits", unitIDs: unitIDs, dest: dest});
    }
}

function createBuilding(pos, typeID) {
    var unitIDs = getSelectedUnitIDs();
    send({type: "createBuilding", typeID: typeID, pos: pos, unitIDs: unitIDs});
}



function getSelectedUnitIDs() {
    var unitIDs = [];
    units.forEach(function(unit) {
        if(unit.selected) {
            unitIDs.push(unit.unitID);
        }
    });
    return unitIDs;
}





//Set interrupt for gameLoop
var lastFrame = Date.now();
setInterval(gameLoop, 16.6666667);

function gameLoop() {
    var delta = Date.now() - lastFrame;
    lastFrame += delta;
    input(delta / 1000);
    update(delta / 1000);
    render(delta / 1000);
}

function init() {

    webSocketInit();
}