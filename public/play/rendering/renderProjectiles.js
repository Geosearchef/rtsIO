
var projectileIcons = [];
loadProjectileIcons();

function renderProjectiles() {
    projectiles.forEach(function(p) {
        ctx.drawImage(projectileIcons[p.unitType], p.pos.x * CELL_SCALE, p.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);
    });
}



function loadProjectileIcons() {
    projectileData.forEach(function(projectileEntry) {
       projectileIcons.push(loadImage("img/projectiles/" + projectileEntry.files[0]));
    });
}