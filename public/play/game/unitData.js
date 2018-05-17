var unitData = [

    {"name": "Scout", "files": ["scoutBody.svg", "scoutTurret.svg"], "maxHp": 100, "movementSpeed": 2.0, "projectile": 0, "attackCooldown": 1500, "attackRange": 7},
    {"name": "Tank", "files": ["tankBody.svg", "tankTurret.svg"], "maxHp": 200, "movementSpeed": 1.2, "projectile": -1},
    {"name": "Artillery", "files": ["artyBody.svg", "artyTurret.svg"], "maxHp": 120, "movementSpeed": 0.8, "projectile": -1},

];

function getUnitSize(typeID) {
    return 1.0;
}
