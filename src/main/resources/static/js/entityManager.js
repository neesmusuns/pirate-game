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
_removedIDs : [],
_playerID : 0,
_playerPos : {x : 0, y : 0},
_stats : {health : 3, drink: 3},

// "PUBLIC" METHODS

deferredSetup : function () {
},


render: function(ctx) {
    //Map to temporary array
    let map = this._gameObjects.map(function (el, index) {
        return { index : index, value : el.zIndex };
    });

    //Sort by z index
    map.sort(function (a, b) {
        return a.value - b.value;
    });

    //Rebuild sorted array
    let objectsSorted = map.map(function (el) {
        return entityManager._gameObjects[el.index];
    });

    //Render objects
    for (let i = 0; i < objectsSorted.length; i++) {
        objectsSorted[i].render(ctx)
    }

    renderUI(this._stats.health, this._stats.drink);
},

updateGameState: function(response) {
    if(response == null)
        return;

    let gameState = JSON.parse(response);

    if(gameState.playerID != null){
        this._playerID = gameState.playerID
    }

    if(gameState.stats != null) {
        this._stats.health = gameState.stats.health;
        this._stats.drink = gameState.stats.drink;
    }

    gameState.gameObjects.forEach(go => {
        if(go.id === this._playerID){
            this._playerPos.x = go.x;
            this._playerPos.y = go.y;
        }
        if(this._gameObjects.filter(e => e.id === go.id ).length > 0){
            let foundIndex = this._gameObjects.findIndex(e => e.id === go.id);
            this._gameObjects[foundIndex].targetX = go.x;
            this._gameObjects[foundIndex].targetY = go.y;
            this._gameObjects[foundIndex].scaleX = go.scaleX;
            this._gameObjects[foundIndex].scaleY = go.scaleY;
            this._gameObjects[foundIndex].zIndex = go.zIndex;
            if(this._playerID == go.id)
                this._gameObjects[foundIndex].tooltip = go.tooltip;
        } else {
            let obj = new GameObject({
                id : go.id,
                targetX : go.x,
                targetY : go.y,
                sprite : go.sprite,
                scaleX: go.scaleX,
                scaleY: go.scaleY,
                isStatic : go.isStatic,
                zIndex : go.zIndex,
                tooltip: ""
            });
            this._gameObjects.push(obj);
        }
    });

    gameState.removedGameObjectIDs.forEach(ID => {
        if(!this._removedIDs[ID]) {
            let index = this._gameObjects.findIndex(e => e.id === ID);
            this._removedIDs[ID] = true;
            this._gameObjects.splice(index, 1);
        }
    });

    let xLerp = util.lerp(0,gameState.posShift.x - this.posShift.x, 0.03);
    let yLerp = util.lerp(0,gameState.posShift.y - this.posShift.y, 0.03);
    g_ctx.translate(xLerp, yLerp);

    this.posShift.x += xLerp;

    this.posShift.y += yLerp;

}

};

// Some deferred setup which needs the object to have been created first
entityManager.deferredSetup();
