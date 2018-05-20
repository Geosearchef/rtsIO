const ENTITY_LIST_ICON_SIZE = 40;
const ENTITY_LIST_COUNT_WIDTH = 16;
const ENTITY_LIST_COUNT_FONT_SIZE = 12;
const BUILDING_ENTITY_LIST_SPACING = 10;


function renderUnitList() {
    let unitsPerType = new Map();
    Array.from(units.values())
        .filter(unit => unit.playerID === ownPlayerID)
        .forEach(unit => {
            unitsPerType.set(unit.unitType, unitsPerType.has(unit.unitType) ? (unitsPerType.get(unit.unitType) + 1) : 1);
    });

    let index = 0;
    let unitListX = ENTITY_LIST_ICON_SIZE + BUILDING_ENTITY_LIST_SPACING;
    Array.from(unitsPerType.entries())
        .sort((e1, e2) => e1[0] - e2[0])
        .forEach(e => {
            var y = index * ENTITY_LIST_ICON_SIZE;
            ctx.drawImage(unitIcons[e[0]].body, unitListX, y, ENTITY_LIST_ICON_SIZE, ENTITY_LIST_ICON_SIZE);
            ctx.drawImage(unitIcons[e[0]].turret, unitListX, y, ENTITY_LIST_ICON_SIZE, ENTITY_LIST_ICON_SIZE);

            ctx.fillStyle = "lightgray";
            ctx.fillRect(unitListX + ENTITY_LIST_ICON_SIZE - ENTITY_LIST_COUNT_WIDTH, y + ENTITY_LIST_ICON_SIZE - (ENTITY_LIST_COUNT_FONT_SIZE), ENTITY_LIST_COUNT_WIDTH, ENTITY_LIST_COUNT_FONT_SIZE);
            ctx.font = ENTITY_LIST_COUNT_FONT_SIZE + "px Arial";
            ctx.textAlign = "right";
            ctx.fillStyle = "black";
            ctx.fillText(e[1], unitListX + ENTITY_LIST_ICON_SIZE - 1, y + ENTITY_LIST_ICON_SIZE - 2);

            index++;
        });
}

function renderBuildingList() {
    let buildingsPerType = new Map();
    Array.from(buildings.values())
        .filter(building => building.playerID === ownPlayerID)
        .forEach(building => {
            buildingsPerType.set(building.buildingType, buildingsPerType.has(building.buildingType) ? (buildingsPerType.get(building.buildingType) + 1) : 1);
    });

    let index = 0;
    let buildingListX = 0;
    Array.from(buildingsPerType.entries())
        .sort((e1, e2) => e1[0] - e2[0])
        .forEach(e => {
            var y = index * ENTITY_LIST_ICON_SIZE;
            ctx.drawImage(buildingIcons[e[0]], buildingListX, y, ENTITY_LIST_ICON_SIZE, ENTITY_LIST_ICON_SIZE);

            ctx.fillStyle = "lightgray";
            ctx.fillRect(buildingListX + ENTITY_LIST_ICON_SIZE - ENTITY_LIST_COUNT_WIDTH, y + ENTITY_LIST_ICON_SIZE - (ENTITY_LIST_COUNT_FONT_SIZE), ENTITY_LIST_COUNT_WIDTH, ENTITY_LIST_COUNT_FONT_SIZE);
            ctx.font = ENTITY_LIST_COUNT_FONT_SIZE + "px Arial";
            ctx.textAlign = "right";
            ctx.fillStyle = "black";
            ctx.fillText(e[1], buildingListX + ENTITY_LIST_ICON_SIZE - 1, y + ENTITY_LIST_ICON_SIZE - 2);

            index++;
        });
}