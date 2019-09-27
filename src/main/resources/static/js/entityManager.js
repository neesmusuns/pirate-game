/*

entityManager.js

A module which handles arbitrary entity-management for "Asteroids"

*/


"use strict";

/*jslint nomen: true, white: true, plusplus: true*/


var entityManager = {

// "PRIVATE" DATA

_gameObjects   : [],
// "PRIVATE" METHODS

deferredSetup : function () {
    this._categories = [this._rocks, this._bullets, this._ships];
},


render: function(ctx) {
    //For each category render each object
    this._categories.forEach(category => category.forEach(entity => entity.render(ctx)));
}

};

// Some deferred setup which needs the object to have been created first
entityManager.deferredSetup();
