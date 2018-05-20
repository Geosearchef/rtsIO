const BUILDING_BUTTON_SIZE = 65;


var buttons;

var buildingButtons;

function generateGui() {
    buttons = [];


    buildingButtons = [];

    //create buildings buttons
    var buildingButtonsStartX = (canvas.width - buildingData.length * BUILDING_BUTTON_SIZE) / 2.0;
    for(var i = 0;i < buildingData.length;i++) {
        var b = new Button(buildingButtonsStartX + BUILDING_BUTTON_SIZE * i, canvas.height - BUILDING_BUTTON_SIZE, BUILDING_BUTTON_SIZE, BUILDING_BUTTON_SIZE);
        buttons.push(b);
        buildingButtons.push(b);
    }
}




function renderGui() {

    renderResourceAmount();

    renderBuildingButtons();

    renderBuildingList();
    renderUnitList();

    renderNetworkStats();
}


function renderResourceAmount() {
    ctx.fillStyle = "lightgray";
    ctx.fillRect(canvas.width - 50, 0, 50, 30);
    ctx.font = "14px Arial";
    ctx.textAlign = "center";
    ctx.fillStyle = "black";
    ctx.fillText(ownResourceAmount, canvas.width - 25, 20);
}

function renderBuildingButtons() {
    for(var i = 0;i < buildingData.length;i++) {
        ctx.drawImage(buildingIcons[i], buildingButtons[i].x, buildingButtons[i].y, buildingButtons[i].width, buildingButtons[i].height);
    }
}


function renderNetworkStats() {
    ctx.font = "14px Consolas";
    ctx.textAlign = "left";
    ctx.fillStyle = "black";
    ctx.fillText("LAT: " + Math.round(Latency.mediumLatency), canvas.width - 120, 20);
    ctx.fillText("JIT: " + Math.round(Latency.jitter), canvas.width - 120, 35);
}