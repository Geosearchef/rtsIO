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

