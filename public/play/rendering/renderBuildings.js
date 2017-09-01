var buildingIcons = [];
loadBuildingIcons();


function loadBuildingIcons() {
    buildingData.forEach(function(buildingEntry) {
        buildingIcons.push(loadImage("img/buildings/" + buildingEntry.files[0]));
    });
}