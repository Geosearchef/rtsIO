window.onload = init;



function init() {
    alert("executed");
    var socket = new WebSocket("ws://" + window.location.hostname + ":" + location.port + "/play/socket");

    socket.onopen = function () {
        alert("opened");
    }

    socket.onmessage = function (evt) {
        var message = evt.data;
        alert(message);
    }

    socket.onclose = function () {
        alert("closed");
    }
}
