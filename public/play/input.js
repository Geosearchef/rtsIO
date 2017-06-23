

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
    if(Mouse.x < MAP_MOVE_BORDER_SIZE) centerMoveDir.x -= 1;
    if(Mouse.y < MAP_MOVE_BORDER_SIZE) centerMoveDir.y -= 1;
    if(Mouse.x > canvas.width - MAP_MOVE_BORDER_SIZE) centerMoveDir.x += 1;
    if(Mouse.y > canvas.height - MAP_MOVE_BORDER_SIZE) centerMoveDir.y += 1;

    center.setAdd(centerMoveDir.scale(d * MAP_MOVE_SPEED));
}

window.addEventListener('mousedown', function(event) {
    if(event.button == 2) {
        moveUnits(screenToMapSpace(new Vector(event.clientX, event.clientY)).add(new Vector(0.5, 0.5)));//TODO: unit size instead of 0.5
    }
});