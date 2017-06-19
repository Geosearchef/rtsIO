window.onload = init;//set init() to be executed on page loading

const CELL_SCALE = 50.0;

const MAP_SIZE = {x:100, y:100};


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID


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


function update(d) {

}


function loggedIn() {

}




function init() {

    webSocketInit();
}