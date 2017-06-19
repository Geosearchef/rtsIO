var Util = {};


/*
 * Library stuff
 */
var Vector = {};
Vector.new = function(x, y) {return {x: x, y: y};};
Vector.add = function(v1, v2) {return {x: v1.x + v2.x, y: v1.y + v2.y};};
Vector.sub = function(v1, v2) {return {x: v1.x - v2.x, y: v1.y - v2.y};};
Vector.scale = function(v1, s) {return {x: v1.x * s, y: v1.y * s};};
Vector.lengthSquared = function(v1) {return v1.x * v1.x + v1.y * v1.y;};
Vector.length = function(v1) {return Math.sqrt(Vector.lengthSquared(v1));};
Vector.normalise = function(v1) {return Vector.scale(v1, 1 / Vector.length(v1))};
Vector.set = function(dst, src) {dst.x = src.x; dst.y = src.y; return dst};




//TODO: rewrite
//returns a URL parameter
Util.getParameterByName = function (name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    //TODO: if parameter not present, uncaught
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}