const LARGE_GEM_SIZE = 3;//WARNING: change aswell in Java
const SMALL_GEM_SIZE = 1;


var smallGemImage = loadImage("img/smallGem.svg");
var largeGemImage = loadImage("img/largeGem.svg");

function renderGems() {

    gems.forEach(function(gem) {
        if(gem.spawner)
            ctx.drawImage(largeGemImage, gem.pos.x * CELL_SCALE, gem.pos.y * CELL_SCALE, LARGE_GEM_SIZE * CELL_SCALE, LARGE_GEM_SIZE * CELL_SCALE);
        else
            ctx.drawImage(smallGemImage, gem.pos.x * CELL_SCALE, gem.pos.y * CELL_SCALE, SMALL_GEM_SIZE * CELL_SCALE, SMALL_GEM_SIZE * CELL_SCALE);
    });

}