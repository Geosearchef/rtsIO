

function Vector(x, y) {
    this.x = x;
    this.y = y;
    this.add = function(v) {return new Vector(v.x + this.x, v.y + this.y)};
    this.setAdd = function(v) {return this.set(this.add(v));};
    this.sub = function(v) {return new Vector(-v.x + this.x, -v.y + this.y)};
    this.setSub = function(v) {return this.set(this.sub(v));};
    this.scale = function(s) {return new Vector(s * this.x, s * this.y)};
    this.setScale = function(s) {return this.set(this.scale(s));};
    this.negate = function() {return new Vector(-this.x, -this.y);}
    this.setNegate = function() {return this.set(this.negate());};
    this.lengthSquared = function() {return this.x * this.x + this.y * this.y;};
    this.length = function() {return Math.sqrt(this.lengthSquared())};
    this.normalise = function() {return this.scale(1 / this.length())};
    this.setNormalise = function() {return this.set(this.normalise());};
    this.set = function(v) {this.x = v.x; this.y = v.y;return this;};
    this.equals = function (o) {return 'x' in o && 'y' in o && this.x === o.x && this.y === o.y;};
    this.containedInRect = function(rect) {return this.x >= rect.topLeftCorner.x && this.y >= rect.topLeftCorner.y && this.x <= rect.topLeftCorner.x + rect.size.x && this.y <= rect.topLeftCorner.y + rect.size.y;}
}

//TODO: move into Vector as clone()
function cloneVector(v) {return new Vector(v.x, v.y);}

function Rect(corner1, corner2) {
    this.topLeftCorner = new Vector(Math.min(corner1.x, corner2.x), Math.min(corner1.y, corner2.y));
    this.size = new Vector(Math.abs(corner1.x - corner2.x), Math.abs(corner1.y - corner2.y));

    this.setScale = function(factor) {this.topLeftCorner.setScale(factor); this.size.setScale(factor);};
}