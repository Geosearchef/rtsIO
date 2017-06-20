

const MAP_MOVE_BORDER_SIZE = 30;
const MAP_MOVE_SPEED = 5;

var center = new Vector(MAP_SIZE.x / 2 + 0.5, MAP_SIZE.y / 2 + 0.5);
center.screen = function() {return new Vector(center.x * CELL_SCALE, center.y * CELL_SCALE)};
//TODO: update




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
        ctx.moveTo(x + 0.5, center.screen().y - canvas.height / 2);
        ctx.lineTo(x + 0.5, center.screen().y + canvas.height / 2);
    }
    for(var y = (center.screen().y - canvas.height / 2)- ((center.screen().y - canvas.height / 2) % CELL_SCALE) + CELL_SCALE;y < center.screen().y + canvas.height / 2;y += CELL_SCALE) {
        ctx.moveTo(-10000, y + 0.5);
        ctx.lineTo(10000, y + 0.5);
    }
    ctx.stroke();

    ctx.drawImage(test, Math.floor(screenToMapSpace(Mouse).x) * CELL_SCALE, Math.floor(screenToMapSpace(Mouse).y) * CELL_SCALE);


    ctx.restore();
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



function screenToMapSpace(screen) {
    return new Vector((center.x - canvas.width / 2 / CELL_SCALE) + screen.x / CELL_SCALE, (center.y - canvas.height / 2 / CELL_SCALE) + screen.y / CELL_SCALE);
}

