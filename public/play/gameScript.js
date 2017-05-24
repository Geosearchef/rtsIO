window.onload = init;//set init() to be executed on page loading


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var socket;

function onMessage(event) {
    var msg = JSON.parse(event.data);
    alert(event.data);

    switch (msg.type) {
        case "loginSuccess":
            username = msg.username;
            playerID = msg.id;
            connected = true;
            alert("Received username " + username);
            break;
    }
}

function send(message) {
    socket.send(JSON.stringify(message));
}

function onClose() {
    window.location.href = "/?connectionLost=true"
}

function init() {

    socket = new WebSocket("ws://" + window.location.hostname + ":" + location.port + "/play/socket");
    socket.onopen = function () {
        //Attempt login on server using username and token
        var loginMessage = {type: "login", username: getParameterByName("username"), token : getParameterByName("token")};
        send(loginMessage);
    }
    socket.onmessage = onMessage;
    socket.onclose = onClose;
}


