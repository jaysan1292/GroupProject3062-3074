// slightly modified from http://stackoverflow.com/questions/1776915/how-to-center-absolute-element-in-div
$.fn.center = function (div) {
    return this.each(function () {
        div = typeof div != 'undefined' ? $(div) : window;
        var xoff = 0, yoff = 0;
        if (div !== window) {
            xoff = div.position().left;
            yoff = div.position().top;
        }

        var el = $(this);
        var h = el.height();
        var w = el.width();
        var w_box = $(div).width();
        var h_box = $(div).height();
        var w_total = ((w_box - w) / 2) + xoff;
        var h_total = ((h_box - h) / 2) + yoff;
        var css = {
            position: 'absolute',
            left:     w_total + "px",
            top:      h_total + "px"
        };
        el.css(css)
    });
};

// slightly modified from http://stackoverflow.com/questions/1038746/equivalent-of-string-format-in-jquery
String.prototype.format = String.prototype.f = function (args) {
    var newStr = this;
    for (var key in args) {
        newStr = newStr.replace(new RegExp("{" + key + "}", "g"), args[key]);
    }
    return newStr;
};

// Recursively remove the "password" and "description" properties from objects
function cleanOutput(obj) {
    var func = function (res, key) {
        if (key == 'description' || key == 'password') {
            delete res[key];
        }
    };
    objectProperties(obj, func);
}

function objectProperties(obj, func) {
    if (obj) {
        for (var key in obj) {
            if (typeof obj[key] == "object") {
                objectProperties(obj[key], func)
            } else if (typeof obj[key] != "function") {
                func(obj, key)
            }
        }
    }
}
