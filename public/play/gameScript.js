window.onload = init;//set init() to be executed on page loading

const CELL_SCALE = 50.0;

const MAP_SIZE = {x:100, y:100};


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var center = {x: MAP_SIZE.x / 2 + 0.5,y: MAP_SIZE.y / 2 + 0.5};
center.screen = function() {return {x: center.x * CELL_SCALE, y: center.y * CELL_SCALE}};
//TODO: update

/*
 * VIEW -----------------------------------------------------------------------------------
 */

var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var ctxScreen = canvas.getContext("2d");

var test = new Image();
test.src = "img/test.svg";

function render(d) {
    handleResize();
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    //Render grid
    ctx.strokeStyle = "#868686";
    ctx.beginPath();

    for(var x = (center.screen().x - canvas.width / 2)- ((center.screen().x - canvas.width / 2) % CELL_SCALE) + CELL_SCALE;x < center.screen().x + canvas.width / 2;x += CELL_SCALE) {
        ctx.moveTo(x + 0.5, 0);
        ctx.lineTo(x + 0.5, canvas.height);console.log(x);
    }
    for(var y = 0;y < canvas.height;y += CELL_SCALE) {
        ctx.moveTo(0, y + 0.5);
        ctx.lineTo(canvas.width, y + 0.5);
    }
    ctx.stroke();

    ctx.drawImage(test, 100, 100);
}

var oldWidth = 0;
var oldHeight = 0;
function handleResize() {
    var newWidth = window.innerWidth;
    var newHeight = window.innerHeight;

    if(oldWidth == newWidth && oldHeight == newHeight)
        return;

    canvas.width = newWidth;
    canvas.height = newHeight;

    ctx.restore();
    ctx.translate((center.screen().x + (canvas.width / 2)), (center.screen().y + (canvas.height / 2)));

    oldWidth = newWidth;
    oldHeight = newHeight;
}

/*
 * END VIEW -------------------------------------------------------------------------------
 */




/*
 * CONTROLLER -----------------------------------------------------------------------------
 */

//Set interrupt for gameLoop
var lastFrame = Date.now();
setInterval(gameLoop, 16.6666667);

function gameLoop() {
    var delta = Date.now() - lastFrame;
    lastFrame += delta;
    update(delta);
    render(delta);
}


function update(d) {

}


function loggedIn() {

}

/*
 * END CONTROLLER -------------------------------------------------------------------------
 */




/*
 * WEB SOCKET -------------------------------------------------------------------------
 */
var socket;


function onSocketMessage(event) {
    var msg = JSON.parse(event.data);
    //alert(event.data);

    switch (msg.type) {
        case "loginSuccess":
            username = msg.username;
            playerID = msg.id;
            connected = true;
            loggedIn();

            break;
    }
}

function send(message) {
    socket.send(JSON.stringify(message));
}

function onSocketClose() {
    window.location.href = "/?connectionLost=true";
}

function webSocketInit() {
    socket = new WebSocket("ws://" + window.location.hostname + ":" + location.port + "/play/socket");
    socket.onopen = function () {
        //Attempt login on server using username and token
        var loginMessage = {type: "login", username: Util.getParameterByName("username"), token : Util.getParameterByName("token")};
        send(loginMessage);
    }
    socket.onmessage = onSocketMessage;
    socket.onclose = onSocketClose;
}

/*
 * WEB SOCKET END -------------------------------------------------------------------------
 */





function init() {

    webSocketInit();
}


