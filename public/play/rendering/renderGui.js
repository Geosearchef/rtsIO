
function renderGui() {

    renderGrid();

    renderSelectionArea()

}

function renderGrid() {
    ctx.strokeStyle = "#868686";
    ctx.beginPath();

    for(var x = (center.screen().x - canvas.width / 2 / scaleFactor)- ((center.screen().x - canvas.width / 2 / scaleFactor) % CELL_SCALE) + CELL_SCALE;x < center.screen().x + canvas.width / 2 / scaleFactor;x += CELL_SCALE) {
        ctx.moveTo(x + 0.5, -10000/*center.screen().y - canvas.height / 2*/);
        ctx.lineTo(x + 0.5, 10000/*center.screen().y + canvas.height / 2 / scaleFactor / scaleFactor*/);
    }
    for(var y = (center.screen().y - canvas.height / 2 / scaleFactor)- ((center.screen().y - canvas.height / 2 / scaleFactor) % CELL_SCALE) + CELL_SCALE;y < center.screen().y + canvas.height / 2 / scaleFactor;y += CELL_SCALE) {
        ctx.moveTo(-10000, y + 0.5);
        ctx.lineTo(10000, y + 0.5);
    }
    ctx.stroke();
}


function renderSelectionArea() {
    ctx.fillStyle = "black";
    ctx.globalAlpha = 0.2;
    if(selectionStartPos != null) {
        var rect = new Rect(selectionStartPos, screenToMapSpace(currentMousePos));
        rect.setScale(CELL_SCALE);
        ctx.fillRect(rect.topLeftCorner.x, rect.topLeftCorner.y, rect.size.x, rect.size.y);
    }
    ctx.globalAlpha = 1.0;
}