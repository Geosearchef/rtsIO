window.onload = init;//set init() to be executed on page loading

const CELL_SCALE = 50.0;

const MAP_SIZE = {x:100, y:100};

const MAP_MOVE_BORDER_SIZE = 30;
const MAP_MOVE_SPEED = 5;

var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var center = Vector.new(MAP_SIZE.x / 2 + 0.5, MAP_SIZE.y / 2 + 0.5);
center.screen = function() {return Vector.new(center.x * CELL_SCALE, center.y * CELL_SCALE)};
//TODO: update

/*
 * VIEW -----------------------------------------------------------------------------------
 */

var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");//relative to center
var ctxScreen = canvas.getContext("2d");//relative to screen

var test = new Image();
test.src = "img/test.svg";

function render(d) {
    handleResize();
    ctxScreen.clearRect(0, 0, canvas.width, canvas.height);
    ctx.save();
    // ctx.translate(-50, -50);
    ctx.translate(-(center.screen().x - (canvas.width / 2)), -(center.screen().y - (canvas.height / 2)));

    //Render grid
    ctx.strokeStyle = "#868686";
    ctx.beginPath();

    for(var x = (center.screen().x - canvas.width / 2)- ((center.screen().x - canvas.width / 2) % CELL_SCALE) + CELL_SCALE;x < center.screen().x + canvas.width / 2;x += CELL_SCALE) {
        console.log();
        ctx.moveTo(x + 0.5, center.screen().y - canvas.height / 2);
        ctx.lineTo(x + 0.5, center.screen().y + canvas.height / 2);
    }
    for(var y = (center.screen().y - canvas.height / 2)- ((center.screen().y - canvas.height / 2) % CELL_SCALE) + CELL_SCALE;y < center.screen().y + canvas.height / 2;y += CELL_SCALE) {
        ctx.moveTo(-10000, y + 0.5);
        ctx.lineTo(10000, y + 0.5);
    }
    ctx.stroke();

    ctx.drawImage(test, 50 * CELL_SCALE, 50 * CELL_SCALE);


    ctx.restore();
}

//INPUT
var Key = {
    pressed: new Array(256).fill(false),
    LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40,
    isDown: function(keyCode) {return Key.pressed[keyCode];},
    onKeyDown: function(event) {Key.pressed[event.keyCode] = true;},
    onKeyUp: function(event) {Key.pressed[event.keyCode] = false;}
};
window.addEventListener('keydown', Key.onKeyDown);
window.addEventListener('keyup', Key.onKeyUp);
var Mouse = {x: canvas.width / 2, y : canvas.height / 2};
window.addEventListener('mousemove', function(event) {Mouse.x = event.clientX; Mouse.y = event.clientY});
window.addEventListener('mouseout', function(event) {Mouse.x = canvas.width / 2; Mouse.y = canvas.height / 2});

function input(d) {
    var centerMoveDir = Vector.new(0, 0);
    if(Key.isDown(Key.LEFT)) centerMoveDir.x -= 1;
    if(Key.isDown(Key.UP)) centerMoveDir.y -= 1;
    if(Key.isDown(Key.RIGHT)) centerMoveDir.x += 1;
    if(Key.isDown(Key.DOWN)) centerMoveDir.y += 1;
    if(Mouse.x < MAP_MOVE_BORDER_SIZE) centerMoveDir.x -= 1;
    if(Mouse.y < MAP_MOVE_BORDER_SIZE) centerMoveDir.y -= 1;
    if(Mouse.x > canvas.width - MAP_MOVE_BORDER_SIZE) centerMoveDir.x += 1;
    if(Mouse.y > canvas.height - MAP_MOVE_BORDER_SIZE) centerMoveDir.y += 1;

    Vector.set(center, Vector.add(center, Vector.scale(centerMoveDir, d * MAP_MOVE_SPEED)));
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
    oldWidth = newWidth;
    oldHeight = newHeight;

    Mouse.x = newWidth / 2;
    Mouse.y = newHeight / 2;
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
    input(delta / 1000);
    update(delta / 1000);
    render(delta / 1000);
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


