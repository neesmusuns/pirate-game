/*

entityManager.js

A module which handles rendering of entities

*/


"use strict";

/*jslint nomen: true, white: true, plusplus: true*/


let entityManager = {

// "PRIVATE" DATA
_gameObjects   : [],
posShift : {x : 0, y : 0},

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

    gameState.removedGameObjectIDs.forEach(ID => {
        let index = this._gameObjects.findIndex(e => e.id === ID);
        this._gameObjects.splice(index, 1);
    });

    gameState.gameObjects.forEach(go => {
        if(this._gameObjects.filter(e => e.id === go.id ).length > 0){
            let foundIndex = this._gameObjects.findIndex(e => e.id === go.id);
            this._gameObjects[foundIndex].targetX = go.x;
            this._gameObjects[foundIndex].targetY = go.y;
            this._gameObjects[foundIndex].scaleX = go.scaleX;
            this._gameObjects[foundIndex].scaleY = go.scaleY;
        } else {
            let obj = new GameObject({
                id : go.id,
                targetX : go.x,
                targetY : go.y,
                sprite : go.sprite,
                scaleX: go.scaleX,
                scaleY: go.scaleY
            });
            this._gameObjects.push(obj);
        }
    });

    let xLerp = util.lerp(0,gameState.posShift.x - this.posShift.x, 0.02);
    let yLerp = util.lerp(0,gameState.posShift.y - this.posShift.y, 0.02);
    g_ctx.translate(xLerp, yLerp);

    this.posShift.x += xLerp;

    this.posShift.y += yLerp;

}

};

// Some deferred setup which needs the object to have been created first
entityManager.deferredSetup();
