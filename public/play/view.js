

const VISION_SIZE_CELLS = 30;

const MAP_MOVE_BORDER = false;
const MAP_MOVE_BORDER_SIZE = 30;
const MAP_MOVE_SPEED = 5;

const MAP_GRAB_FACTOR = 1.0;

var center = new Vector(MAP_SIZE.x / 2 + 0.5, MAP_SIZE.y / 2 + 0.5);
center.screen = function() {return new Vector(center.x * CELL_SCALE, center.y * CELL_SCALE);};//this is only virtual screen
//actual rendered pixels are scaled using scaleFactor
//TODO: SCREEN() IS NOT SCREEN SPACE; IT'S VIRTUAL SCREEN SPACE, (not scaled, not relative to map center)




var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");//relative to center
var ctxScreen = canvas.getContext("2d");//relative to screen


function render(d) {
    handleResize();
    ctxScreen.clearRect(0, 0, canvas.width, canvas.height);
    ctx.save();
    // ctx.translate(-50, -50);
    ctx.scale(scaleFactor, scaleFactor);
    ctx.translate(-(center.screen().x - (canvas.width / 2 / scaleFactor)), -(center.screen().y - (canvas.height / 2 / scaleFactor)));


    renderGui();

    renderUnits();


    ctx.restore();
}


var scaleFactor = 1;
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
    scaleFactor = Math.max(newWidth, newHeight) / (CELL_SCALE * VISION_SIZE_CELLS);

    Mouse.x = newWidth / 2;
    Mouse.y = newHeight / 2;
}



function screenToMapSpace(screen) {
    return new Vector((center.x - canvas.width / 2 / scaleFactor / CELL_SCALE) + screen.x / scaleFactor / CELL_SCALE, (center.y - canvas.height / 2 / scaleFactor / CELL_SCALE) + screen.y / scaleFactor / CELL_SCALE);
}

function loadImage(src) {
    var image = new Image();
    image.src = src;
    return image;
}