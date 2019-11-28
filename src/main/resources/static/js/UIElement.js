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
function UIElement(descr) {
    for (let property in descr) {
        this[property] = descr[property];
    }

    this.sprite = util.getSprite(this.sprite) || g_sprites.pirate;
}

UIElement.prototype.render = function (ctx) {

    let origScaleX = this.sprite.scaleX;

    this.sprite.scaleX = this.scaleX;

    let origScaleY = this.sprite.scaleY;

    this.sprite.scaleY = this.scaleY;

    this.sprite.drawCentredAt(
	    ctx, this.x, this.y, this.rotation
    );

    this.sprite.scaleX = origScaleX;
    this.sprite.scaleY = origScaleY;
};
