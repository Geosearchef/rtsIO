var buildingIcons = [];
loadBuildingIcons();


function renderBuildings() {

    buildings.forEach(function (building) {
        ctx.drawImage(buildingIcons[building.buildingType], building.pos.x * CELL_SCALE, building.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);

        //render health bar
        if(building.hp < buildingData[building.buildingType].maxHp) {
            ctx.fillStyle = "red";
            ctx.fillRect(building.pos.x * CELL_SCALE, (building.pos.y - 0.20) * CELL_SCALE, CELL_SCALE, 0.1 * CELL_SCALE);
            ctx.fillStyle = "green";
            ctx.fillRect(building.pos.x * CELL_SCALE, (building.pos.y - 0.20) * CELL_SCALE, building.getSize() * CELL_SCALE * (building.hp / buildingData[building.buildingType].maxHp), 0.1 * CELL_SCALE);
            ctx.fillStyle = "black";
        }
    });

    renderBuildingMode();
}

function renderBuildingMode() {
    if(buildingModeTypeID != -1) {
        var buildingModePos = screenToMapSpace(currentMousePos);
        if (ownResourceAmount < buildingData[buildingModeTypeID].cost
            || intersectsAnyBuilding(buildingModePos.sub(new Vector(0.5, 0.5)), buildingModeTypeID)) {
            ctx.filter = "blur(3px)";
        }
        ctx.drawImage(buildingIcons[buildingModeTypeID], (buildingModePos.x - 0.5) * CELL_SCALE, (buildingModePos.y - 0.5) * CELL_SCALE, CELL_SCALE, CELL_SCALE);//TODO: dynamic width of buildings in js
        ctx.filter = "blur(0px)";
    }
}


function loadBuildingIcons() {
    buildingData.forEach(function(buildingEntry) {
        buildingIcons.push(loadImage("img/buildings/" + buildingEntry.files[0]));
    });
}