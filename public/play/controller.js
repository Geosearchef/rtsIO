window.onload = init;//set init() to be executed on page loading

const CELL_SCALE = 50.0;

const MAP_SIZE = {x:100, y:100};


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var players = new Map();
var units = new Map();




function update(d) {
    units.forEach(function(unit) {
        unit.pos.setAdd(unit.vel.scale(d));
    });
}


function loggedIn() {
    //TODO
}



//maybe outsource to parser???
function onNewUnitMessage(msg) {
    var unit = new Unit(msg.playerID, msg.unitID, msg.unitType, cloneVector(msg.pos), cloneVector(msg.vel), msg.hp);
    units.set(msg.unitID, unit);
}

function onDeleteUnit(id) {

}

function onPlayerConnect(id, username) {
    players.set(id, new Player(id, username));
}

function onPlayerDisconnect(id) {
    players.delete(id);
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