// ==========
// SHIP STUFF
// ==========

"use strict";

/* jshint browser: true, devel: true, globalstrict: true */

/*
0        1         2         3         4         5         6         7         8
12345678901234567890123456789012345678901234567890123456789012345678901234567890
*/


// A generic contructor which accepts an arbitrary descriptor object
function GameObject(descr) {
    for (let property in descr) {
        this[property] = descr[property];
    }
}

GameObject.prototype.render = function (ctx) {

    g_sprites.pirate.drawCentredAt(
	    ctx, this.x, this.y
    );
};
