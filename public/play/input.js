
var buildingModeTypeId = -1;


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

    currentMousePos = new Vector(event.clientX, event.clientY);

    for(var i = 0;i < buttons.length;i++) {
        var button = buttons[i];
        if(button.isInside(currentMousePos)) {

            var buildingButtonId = buildingButtons.indexOf(button);
            if(buildingButtonId != -1 && event.button === 0) {
                buildingModeTypeId = buildingButtonId;
            }

            return;//CANCEL processing
        }
    }



    //Start selection
    if(event.button === 0) {

        if(buildingModeTypeId != -1) {
            createBuilding(screenToMapSpace(currentMousePos), buildingModeTypeId);
            buildingModeTypeId = -1;
        } else {
            selectionStartPos = screenToMapSpace(new Vector(event.clientX, event.clientY));
        }
    }

    //Grab map / Move unit
    if(event.button === 2) {
        mouseRightDown = true;

        //Stop building process
        buildingModeTypeId = -1;
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

    currentMousePos = new Vector(event.clientX, event.clientY);

    for(var i = 0;i < buttons.length;i++) {
        var button = buttons[i];
        if(button.isInside(currentMousePos)) {


            return;//CANCEL processing
        }
    }


    var currentMousePosMapSpace = screenToMapSpace(currentMousePos);

    if(event.button === 0) {

        //Apply selection
        if(! selectionStartPos.equals(currentMousePosMapSpace)) {
            units.forEach(function (unit) {
                //TODO: swap containedInRect with intersects between selection rect and unit rect
                unit.selected = unit.playerID == ownPlayerID && unit.pos.containedInRect(new Rect(selectionStartPos, currentMousePosMapSpace));
            });
        } else {
            units.forEach(function (unit) {
                unit.selected = unit.playerID == ownPlayerID && currentMousePosMapSpace.containedInRect(new Rect(unit.pos, unit.pos.add(new Vector(unit.getSize(), unit.getSize()))));
            });
        }

        selectionStartPos = null;
    }

    if(event.button === 2) {
        //Movement order
        if(! isMapMoving) {
            moveUnits(currentMousePosMapSpace.add(new Vector(-0.5, -0.5)));//TODO: unit size instead of 0.5
        }
        isMapMoving = false;
        mouseRightDown = false;
    }
})