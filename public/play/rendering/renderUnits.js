

var testImage = new Image();
testImage.src = "img/test.svg";

var unitIcons = [];
loadUnitIcons();

var selectionIcon = loadImage("img/selectionIcon.svg");






function renderUnits() {

    renderUnitMovementIndicators();

    //render units
    units.forEach(function (unit) {
        ctx.drawImage(unitIcons[unit.unitType].body, unit.pos.x * CELL_SCALE, unit.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);
        ctx.drawImage(unitIcons[unit.unitType].turret, unit.pos.x * CELL_SCALE, unit.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);

        if(unit.selected) {
            ctx.drawImage(selectionIcon, unit.pos.x * CELL_SCALE, unit.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);
        }
    });
}

function renderUnitMovementIndicators() {
    ctx.beginPath();
    ctx.setLineDash([10,10]);
    ctx.globalAlpha = 0.8;
    units.forEach(function (unit) {
        if(unit.vel != null && unit.vel.lengthSquared() != 0 && unit.dest != null) {
            ctx.moveTo((unit.pos.x + 0.5) * CELL_SCALE, (unit.pos.y + 0.5) * CELL_SCALE);
            ctx.lineTo((unit.dest.x + 0.5) * CELL_SCALE, (unit.dest.y + 0.5) * CELL_SCALE);
        }
    });
    ctx.stroke();
    ctx.globalAlpha = 1.0;
    ctx.setLineDash([]);
}



function loadUnitIcons() {
    unitData.forEach(function(unitEntry) {
        unitIcons.push({body: loadImage("img/units/" + unitEntry.files[0]), turret: loadImage("img/units/" + unitEntry.files[1])});
    });
}