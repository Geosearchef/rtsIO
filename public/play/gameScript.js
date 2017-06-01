window.onload = init;//set init() to be executed on page loading


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var socket;


/*
 * VIEW -----------------------------------------------------------------------------------
 */

var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");

function render(d) {
    handleResize();
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "#000000";
    ctx.fillRect(0, 0, 10000, 10000);
}

var oldWidth = 0;
var oldHeight = 0;
function handleResize() {
    var newWidth = window.innerWidth;
    var newHeight = window.innerHeight;

    if(oldWidth == newWidth && oldHeight == newHeight)
        return;
    console.log(newWidth + " x " + newHeight);
    canvas.width = newWidth;
    canvas.height = newHeight;

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


