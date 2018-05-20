const ENTITY_LIST_ICON_SIZE = 40;
const ENTITY_LIST_COUNT_WIDTH = 16;
const ENTITY_LIST_COUNT_FONT_SIZE = 12;
const BUILDING_ENTITY_LIST_SPACING = 10;


function renderUnitList() {
    let unitsPerType = new Map();
    let selectedUnitsPerType = new Map();

    //Collect
    Array.from(units.values())
        .filter(unit => unit.playerID === ownPlayerID)
        .forEach(unit => {
            unitsPerType.set(unit.unitType, unitsPerType.has(unit.unitType) ? (unitsPerType.get(unit.unitType) + 1) : 1);
            if(unit.selected) {
                selectedUnitsPerType.set(unit.unitType, selectedUnitsPerType.has(unit.unitType) ? (selectedUnitsPerType.get(unit.unitType) + 1) : 1);
            }
    });

    //Render
    let index = 0;
    let unitListX = ENTITY_LIST_ICON_SIZE + BUILDING_ENTITY_LIST_SPACING;
    for(let unitType = 0;unitType < unitData.length;unitType++) {
        if(! unitsPerType.has(unitType)) {
           continue;
        }

        let unitCount = unitsPerType.get(unitType);
        let selectedUnitCount = selectedUnitsPerType.has(unitType) ? selectedUnitsPerType.get(unitType) : 0;

        let y = (index++) * ENTITY_LIST_ICON_SIZE;
        ctx.drawImage(unitIcons[unitType].body, unitListX, y, ENTITY_LIST_ICON_SIZE, ENTITY_LIST_ICON_SIZE);
        ctx.drawImage(unitIcons[unitType].turret, unitListX, y, ENTITY_LIST_ICON_SIZE, ENTITY_LIST_ICON_SIZE);

        ctx.font = ENTITY_LIST_COUNT_FONT_SIZE + "px Arial";

        //Total unit count
        ctx.fillStyle = "lightgray";
        ctx.fillRect(unitListX + ENTITY_LIST_ICON_SIZE - ENTITY_LIST_COUNT_WIDTH, y + ENTITY_LIST_ICON_SIZE - (ENTITY_LIST_COUNT_FONT_SIZE), ENTITY_LIST_COUNT_WIDTH, ENTITY_LIST_COUNT_FONT_SIZE);
        ctx.fillStyle = "black";
        ctx.textAlign = "right";
        ctx.fillText(unitCount, unitListX + ENTITY_LIST_ICON_SIZE - 1, y + ENTITY_LIST_ICON_SIZE - 2);

        //Selected unit count
        if(selectedUnitCount > 0) {
            ctx.fillStyle = "#79ca56";
            ctx.fillRect(unitListX, y + ENTITY_LIST_ICON_SIZE - (ENTITY_LIST_COUNT_FONT_SIZE), ENTITY_LIST_COUNT_WIDTH, ENTITY_LIST_COUNT_FONT_SIZE);
            ctx.fillStyle = "black";
            ctx.textAlign = "left";
            ctx.fillText(selectedUnitCount, unitListX, y + ENTITY_LIST_ICON_SIZE - 2);
        }
    }
}

function renderBuildingList() {
    let buildingsPerType = new Map();

    //Collect
    Array.from(buildings.values())
        .filter(building => building.playerID === ownPlayerID)
        .forEach(building => {
            buildingsPerType.set(building.buildingType, buildingsPerType.has(building.buildingType) ? (buildingsPerType.get(building.buildingType) + 1) : 1);
    });

    //Render
    let index = 0;
    let buildingListX = 0;
    for(let buildingType = 0;buildingType < buildingData.length;buildingType++) {
        if(! buildingsPerType.has(buildingType)) {
            continue;
        }

        let y = (index++) * ENTITY_LIST_ICON_SIZE;
        ctx.drawImage(buildingIcons[buildingType], buildingListX, y, ENTITY_LIST_ICON_SIZE, ENTITY_LIST_ICON_SIZE);

        ctx.fillStyle = "lightgray";
        ctx.fillRect(buildingListX + ENTITY_LIST_ICON_SIZE - ENTITY_LIST_COUNT_WIDTH, y + ENTITY_LIST_ICON_SIZE - (ENTITY_LIST_COUNT_FONT_SIZE), ENTITY_LIST_COUNT_WIDTH, ENTITY_LIST_COUNT_FONT_SIZE);
        ctx.font = ENTITY_LIST_COUNT_FONT_SIZE + "px Arial";
        ctx.textAlign = "right";
        ctx.fillStyle = "black";
        ctx.fillText(buildingsPerType.get(buildingType), buildingListX + ENTITY_LIST_ICON_SIZE - 1, y + ENTITY_LIST_ICON_SIZE - 2);
    }
}