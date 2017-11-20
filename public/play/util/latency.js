
const LATENCY_TEST_DELAY = 250;

var Latency = {};

Latency.history = [];
Latency.mediumLatency = 0;
Latency.jitter = 0;
Latency.lastPingSent = 0;

Latency.update = function() {
    var currTime = Date.now();
    if(currTime - Latency.lastPingSent > LATENCY_TEST_DELAY) {
        send({type: "ping", t: Date.now()});
        Latency.lastPingSent = currTime;
    }
};

Latency.onPongReceived = function(m) {
    var latency = Date.now() - m.t;
    if(Latency.history.length >= 7) {
        Latency.history.shift();
    }
    Latency.history.push(latency);

    Latency.mediumLatency = Latency.history.reduce(function(a, b) {return a + b;}) / Latency.history.length;
    Latency.jitter = Latency.history.reduce(function(a, b) { return Math.max(Math.abs(Latency.mediumLatency - b), a); }, 0);



    console.log("Latency: " + Latency.mediumLatency);
    console.log("Jitter: " + Latency.jitter);
};