
var projectileIcons = [];
loadProjectileIcons();

const projectileSize = 0.3;

function renderProjectiles() {
    projectiles.forEach(function(p) {
        ctx.drawImage(projectileIcons[p.projectileType], (p.pos.x - projectileSize / 2.0) * CELL_SCALE, (p.pos.y - projectileSize / 2.0) * CELL_SCALE, CELL_SCALE * projectileSize, CELL_SCALE * projectileSize);
    });
}



function loadProjectileIcons() {
    projectileData.forEach(function(projectileEntry) {
       projectileIcons.push(loadImage("img/projectiles/" + projectileEntry.files[0]));
    });
}