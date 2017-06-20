var Util = {};



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