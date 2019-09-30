/*

entityManager.js

A module which handles arbitrary entity-management for "Asteroids"

*/


"use strict";

/*jslint nomen: true, white: true, plusplus: true*/


let entityManager = {

// "PRIVATE" DATA
_gameObjects   : [],

// "PUBLIC" METHODS

deferredSetup : function () {
},


render: function(ctx) {
    //For each category render each object
    this._gameObjects.forEach(obj => {
        obj.render(ctx);
    })
},

updateGameState: function(response) {
    let gameState = JSON.parse(response);

    gameState.gameObjects.forEach(go => {
        if(this._gameObjects.filter(e => e.id == go.id ).length > 0){
            let foundIndex = this._gameObjects.findIndex(e => e.id === go.id);
            this._gameObjects[foundIndex] = new GameObject({
                id: go.id,
                x: go.x,
                y: go.y,
                sprite: go.sprite
            });
        } else {
            let obj = new GameObject({
                id : go.id,
                x : go.x,
                y : go.y,
                sprite : go.sprite
            });
            this._gameObjects.push(obj);
        }
    })


}

};

// Some deferred setup which needs the object to have been created first
entityManager.deferredSetup();
