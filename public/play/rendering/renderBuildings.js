var buildingIcons = [];
loadBuildingIcons();


function renderBuildings() {

    buildings.forEach(function (building) {
        ctx.drawImage(buildingIcons[building.buildingType], building.pos.x * CELL_SCALE, building.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);
    });

    renderBuildingMode();
}

function renderBuildingMode() {
    if(buildingModeTypeID != -1) {
        var buildingModePos = screenToMapSpace(currentMousePos);
        ctx.drawImage(buildingIcons[buildingModeTypeID], (buildingModePos.x - 0.5) * CELL_SCALE, (buildingModePos.y - 0.5) * CELL_SCALE, CELL_SCALE, CELL_SCALE);//TODO: dynamic width of buildings in js
    }
}


function loadBuildingIcons() {
    buildingData.forEach(function(buildingEntry) {
        buildingIcons.push(loadImage("img/buildings/" + buildingEntry.files[0]));
    });
}