var socket;


function onSocketMessage(event) {
    var msg = JSON.parse(event.data);

    switch (msg.type) {
        case "loginSuccess":
            ownUsername = msg.username;
            ownPlayerID = msg.id;
            connected = true;
            loggedIn();
            break;

        case "gameInfo":
            MAP_SIZE.x = msg.mapSize.x;
            MAP_SIZE.y = msg.mapSize.y;

        case "playerConnect":
            onPlayerConnect(msg.id, msg.username);
            break;

        case "playerDisconnect":
            onPlayerDisconnect(msg.id);
            break;

        case "newUnit":
            onNewUnitMessage(msg);
            break;

        case "deleteUnit":
            onDeleteUnit(msg.unitID);
            break;

        case "updateUnit":
            onUpdateUnit(msg);
            break;

        case "resourceAmountUpdate":
            ownResourceAmount = msg.resourceAmount;
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

