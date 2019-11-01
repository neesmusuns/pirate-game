// util.js
//
// A module of utility functions, with no private elements to hide.
// An easy case; just return an object containing the public stuff.

"use strict";


var util = {

// MISC
// ====

square: function(x) {
    return x*x;
},

lerp: function (start, end, amt){
    return (1-amt)*start+amt*end
},

// CANVAS OPS
// ==========

clearCanvas: function (ctx) {
    var prevfillStyle = ctx.fillStyle;
    ctx.fillStyle = "black";
    ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);
    ctx.fillStyle = prevfillStyle;
},

// SPRITE FETCHING
// ===============

getSprite: function(name){
    switch (name) {
        case 'pirate':
            return g_sprites.pirate;
        case 'sea1':
            return g_sprites.sea1;
        case 'sea2':
            return g_sprites.sea2;
        case 'beach1':
            return g_sprites.beach1;
        case 'black_shirt':
            return g_sprites.black_shirt;
        case 'boat':
            return g_sprites.boat;
        case 'parrot':
            return g_sprites.parrot;
        case 'sword1':
            return g_sprites.sword1;

        default:
            break;
    }
}

};
