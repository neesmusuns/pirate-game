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
_stats : {health : 3, drink: 3, breath : 10, hasTreasure : false},
_background : null,
changedWorld : true,
isInShop : false,

// "PUBLIC" METHODS

deferredSetup : function () {

},


render: function(ctx) {
    if(this._background != null)
        this._background.render(ctx);
    else{
        this._background = new GameObject({
            id : -1,
            targetX : 0,
            targetY : 0,
            sprite : 'background',
            scaleX: 2,
            scaleY: 2,
            isStatic : true,
            zIndex : -1,
            tooltip: "",
            isRendered: true
        })
    }

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

    renderUI(this._stats.health, this._stats.drink, this._stats.breath, this._stats.hasTreasure);
},

updateGameState: function(response) {
    if(response == null)
        return;

    let gameState = JSON.parse(response);

    if(gameState.shopItems != null){
        this.isInShop = true;
    } else{
        this.isInShop = false;
    }

    if(gameState.playerID != null){
        this._playerID = gameState.playerID
    }

    if(gameState.stats != null) {
        this._stats.health = gameState.stats.health;
        this._stats.drink = gameState.stats.drink;
        this._stats.breath = gameState.stats.breath;
        this._stats.hasTreasure = gameState.stats.hasTreasure;
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
            this._gameObjects[foundIndex].isRendered = go.isRendered;
            if(this._playerID === parseInt(go.id))
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
                tooltip: "",
                isRendered: go.isRendered
            });
            this._gameObjects.push(obj);
        }
    });

    gameState.tempRemovedGameObjectIDs.forEach(ID => {
        let index = this._gameObjects.findIndex(e => e.id === ID);
        this._gameObjects.splice(index, 1);
    });

    gameState.removedGameObjectIDs.forEach(ID => {
        if(!this._removedIDs[ID]) {
            let index = this._gameObjects.findIndex(e => e.id === ID);
            this._removedIDs[ID] = true;
            this._gameObjects.splice(index, 1);
        }
    });

    if(this._background != null) {
        this._background.targetX = -this.posShift.x;
        this._background.targetY = -this.posShift.y;
    }

    if(gameState.changedWorld != null){
        this.changedWorld = gameState.changedWorld;
    }

    if(!this.changedWorld) {
        let xLerp = util.lerp(0, gameState.posShift.x - this.posShift.x, 0.03);
        let yLerp = util.lerp(0, gameState.posShift.y - this.posShift.y, 0.03);
        g_ctx.translate(xLerp, yLerp);

        this.posShift.x += xLerp;

        this.posShift.y += yLerp;
    } else{
        g_ctx.translate(gameState.posShift.x - this.posShift.x, gameState.posShift.y - this.posShift.y);
        this.posShift.x += gameState.posShift.x - this.posShift.x;
        this.posShift.y += gameState.posShift.y - this.posShift.y;
    }

}

};

// Some deferred setup which needs the object to have been created first
entityManager.deferredSetup();
