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
        case 'map':
            return g_sprites.map;
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
        case 'shop':
            return g_sprites.shop;
        case 'healthfull':
            return g_sprites.healthfull;
        case 'healthempty':
            return g_sprites.healthempty;
        case 'bottlefull':
            return g_sprites.bottlefull;
        case 'bottleempty':
            return g_sprites.bottleempty;
        case 'pier':
            return g_sprites.pier;
        case 'underwater1':
            return g_sprites.underwater1;
        case 'underwater2':
            return g_sprites.underwater2;
        case 'background':
            return g_sprites.background;
        case 'treasure':
            return g_sprites.treasure;
        case 'bubble':
            return g_sprites.bubble;

        default:
            break;
    }
}

};
