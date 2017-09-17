var socket;


function onSocketMessage(event) {
    var msg = JSON.parse(event.data);
    console.log(msg);
    switch (msg.type) {
        case "loginSuccess":
            ownUsername = msg.username;
            ownPlayerID = msg.id;
            connected = true;
            loggedIn();
            break;

        case "gameInfo":
            MAP_SIZE.set(new Vector(msg.mapSize.x, msg.mapSize.y));
            center.set(new Vector(MAP_SIZE.x / 2 + 0.5, MAP_SIZE.y / 2 + 0.5));

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

        case "newGem":
            onNewGem(msg.id, msg.pos, msg.spawner);
            break;

        case "deleteGem":
            onDeleteGem(msg.id);
            break;

        case "newBuilding":
            onNewBuilding(msg.playerID, msg.buildingID, msg.buildingType, cloneVector(msg.pos), msg.hp, msg.inBuildingProcess);
            break;

        case "updateBuilding":
            onUpdateBuilding(msg);
            break;

        case "deleteBuilding":
            onDeleteBuilding(msg.buildingID);
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

