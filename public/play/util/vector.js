

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
    this.lengthSqaured = function() {return this.x * this.x + this.y * this.y;};
    this.length = function() {return Math.sqrt(this.lengthSqaured())};
    this.normalise = function() {return this.scale(1 / this.length())};
    this.setNormalise = function() {return this.set(this.normalise());};
    this.set = function(v) {this.x = v.x; this.y = v.y;return this;};
}

function cloneVector(v) {return new Vector(v.x, v.y);}
