window.onload = init;//set init() to be executed on page loading


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var socket;

/*
 * VIEW -----------------------------------------------------------------------------------
 */

function render(d) {

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
var test = 0;
function update(d) {
    test += d;
    document.getElementById("username").innerHTML = test;
}


function loggedIn() {
    document.getElementById("test").innerHTML = "Username: " + username;
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


