
function renderGui() {

    renderResourceAmount();
}


function renderResourceAmount() {
    ctx.fillStyle = "lightgray";
    ctx.fillRect(canvas.width - 50, 0, 50, 30);
    ctx.font = "14px Arial";
    ctx.textAlign = "center";
    ctx.fillStyle = "black";
    ctx.fillText(ownResourceAmount, canvas.width - 25, 20);
}