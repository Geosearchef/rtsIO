window.onload = init;//set init() to be executed on page loading


var connected = false;//did the login succeed?
var username;//own username
var playerID;//own playerID

var socket;

function onMessage(event) {
    var msg = JSON.parse(event.data);
    alert(event.data);

    switch (msg.type) {
        case "loginFailed":
            window.location.href = "/";
            connected = false;
            break;
        case "loginSuccess":
            username = msg.username;
            playerID = msg.id;
            connected = true;
            alert("Received username " + username);
            break;
        //TODO: Redirect back to main page
    }
}

function send(message) {
    socket.send(JSON.stringify(message));
}

function onClose() {
    alert("Connection to server lost!");
}

function init() {

    socket = new WebSocket("ws://" + window.location.hostname + ":" + location.port + "/play/socket");
    socket.onopen = function () {
        var loginMessage = {type: "login", username: getParameterByName("username"), token : getParameterByName("token")};
        send(loginMessage);
    }
    socket.onmessage = onMessage;
    socket.onclose = onClose;

    //TODO: send token
}





//UTIL ---------------------------


//TODO: rewrite
//returns a URL parameter
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}