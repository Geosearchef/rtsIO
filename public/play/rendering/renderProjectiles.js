
var projectileIcons = [];
loadProjectileIcons();

const projectileSize = 0.3;

function renderProjectiles() {
    projectiles.forEach(function(p) {
        // p.pos.x = 50;
        // p.pos.y = 50;
        // ctx.drawImage(projectileIcons[p.projectileType], p.pos.x * CELL_SCALE, p.pos.y * CELL_SCALE, CELL_SCALE, CELL_SCALE);
        ctx.drawImage(projectileIcons[p.projectileType], (p.pos.x - projectileSize / 2.0) * CELL_SCALE, (p.pos.y - projectileSize / 2.0) * CELL_SCALE, CELL_SCALE * projectileSize, CELL_SCALE * projectileSize);
    });
}



function loadProjectileIcons() {
    projectileData.forEach(function(projectileEntry) {
       projectileIcons.push(loadImage("img/projectiles/" + projectileEntry.files[0]));
    });
}