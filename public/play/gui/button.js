
function Button(x, y, width, height) {
    this.rect = new Rect({x: x, y: y}, {x: x + width, y: y + height});
    this.isInside = function(p) {return p.containedInRect(this.rect)};
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
}