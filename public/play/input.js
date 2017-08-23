
var Key = {
    pressed: new Array(256).fill(false),
    LEFT: 37, UP: 38, RIGHT: 39, DOWN: 40,
    isDown: function(keyCode) {return Key.pressed[keyCode];},
    onKeyDown: function(event) {Key.pressed[event.keyCode] = true;},
    onKeyUp: function(event) {Key.pressed[event.keyCode] = false;}
};
window.addEventListener('keydown', Key.onKeyDown);
window.addEventListener('keyup', Key.onKeyUp);

var Mouse = {x: canvas.width / 2, y : canvas.height / 2};
window.addEventListener('mousemove', function(event) {Mouse.x = event.clientX; Mouse.y = event.clientY});
window.addEventListener('mouseout', function(event) {Mouse.x = canvas.width / 2; Mouse.y = canvas.height / 2});


function input(d) {

    //Map movement
    var centerMoveDir = new Vector(0, 0);
    if(Key.isDown(Key.LEFT)) centerMoveDir.x -= 1;
    if(Key.isDown(Key.UP)) centerMoveDir.y -= 1;
    if(Key.isDown(Key.RIGHT)) centerMoveDir.x += 1;
    if(Key.isDown(Key.DOWN)) centerMoveDir.y += 1;
    if(Key.isDown('A'.charCodeAt(0))) centerMoveDir.x -= 1;
    if(Key.isDown('W'.charCodeAt(0))) centerMoveDir.y -= 1;
    if(Key.isDown('D'.charCodeAt(0))) centerMoveDir.x += 1;
    if(Key.isDown('S'.charCodeAt(0))) centerMoveDir.y += 1;
    if(MAP_MOVE_BORDER && Mouse.x < MAP_MOVE_BORDER_SIZE) centerMoveDir.x -= 1;
    if(MAP_MOVE_BORDER && Mouse.y < MAP_MOVE_BORDER_SIZE) centerMoveDir.y -= 1;
    if(MAP_MOVE_BORDER && Mouse.x > canvas.width - MAP_MOVE_BORDER_SIZE) centerMoveDir.x += 1;
    if(MAP_MOVE_BORDER && Mouse.y > canvas.height - MAP_MOVE_BORDER_SIZE) centerMoveDir.y += 1;

    center.setAdd(centerMoveDir.scale(d * MAP_MOVE_SPEED));
}




var currentMousePos = new Vector(0, 0);

//Selection
var selectionStartPos = null;//map space

//Map movement
var mouseRightDown = false;
var isMapMoving = false;

window.addEventListener('mousedown', function(event) {

    //Start selection
    if(event.button === 0) {
        selectionStartPos = screenToMapSpace(new Vector(event.clientX, event.clientY));
    }

    //Grab map / Move unit
    if(event.button === 2) {
        mouseRightDown = true;
    }
});

window.addEventListener('mousemove', function(event) {
    var newPos = new Vector(event.clientX, event.clientY);

    if(mouseRightDown) {
        isMapMoving = true;
        var diff = screenToMapSpace(currentMousePos).sub(screenToMapSpace(newPos));

        center.setAdd(diff.scale(MAP_GRAB_FACTOR));
    }

    currentMousePos = newPos;
})

window.addEventListener('mouseup', function(event) {

    if(event.button === 0) {
        //Apply selection
        units.forEach(function (unit) {
            unit.selected = unit.playerID == ownPlayerID && unit.pos.containedInRect(new Rect(selectionStartPos, screenToMapSpace(new Vector(event.clientX, event.clientY))));
        });

        selectionStartPos = null;
    }

    if(event.button === 2) {
        //Movement order
        if(! isMapMoving) {
            moveUnits(screenToMapSpace(currentMousePos).add(new Vector(-0.5, -0.5)));//TODO: unit size instead of 0.5
        }
        isMapMoving = false;
        mouseRightDown = false;
    }
})