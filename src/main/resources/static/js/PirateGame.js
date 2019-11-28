// ========
// PIRATE GAME
// ========
/*

This is the main script for the game which delegates
all the actions to the relevant scripts

*/

"use strict";

/* jshint browser: true, devel: true, globalstrict: true */

var g_canvas = document.getElementById("myCanvas");
var g_ctx = g_canvas.getContext("2d");
g_ctx.scale(0.25, 0.25);

// =================
// UPDATE SIMULATION
// =================

// INPUT-HANDLING
// Here we structure input as a string the following way:
// "KEYCODE1_IsDown KEYCODE2_IsDown ... KEYCODEX_IsDown"

function collectInput(){
    let keyPresses = "";

    if(!entityManager.isInShop) {
        keys.forEach(function (value, keyCode) {
            if (keyCode == 69 || keyCode == 70) {
                if (eatKey(keyCode))
                    keyPresses += (String(keyCode)) + " ";
            } else if (value)
                keyPresses += (String(keyCode)) + " ";
        });
    }

    return keyPresses;
}

// =================
// RENDER SIMULATION
// =================

// GAME-SPECIFIC RENDERING

function renderSimulation(ctx) {

    entityManager.render(ctx);

}


// =============
// PRELOAD STUFF
// =============

let g_images = {};

function requestPreloads() {

    let requiredImages = {
        pirate: '../images/pirate0.png',
        map: '../images/map.png',
        sea1: '../images/tiles/sea1.png',
        sea2: '../images/tiles/sea2.png',
        boat: '../images/boat0.png',
        beach1:'../images/tiles/beach1.png',
        black_shirt:'../images/black_shirt.png',
        parrot:'../images/parrot1.png',
        sword1:'../images/sword1.png',
        shop:'../images/shop.png',
        healthfull:'../images/healthfull.png',
        healthempty:'../images/healthempty.png',
        pier:'../images/tiles/pier.png',
        underwater1: '../images/tiles/underwater1.png',
        underwater2: '../images/tiles/underwater2.png',
        background: '../images/tiles/background.png',
        treasure: '../images/treasure.png',
        bubble: '../images/bubble.png',
        bottleempty: '../images/bottleempty.png',
        bottlefull: '../images/bottlefull.png',
        marker: '../images/marker.png'
    };

    imagesPreload(requiredImages, g_images, preloadDone);
}

let g_sprites = {};

function preloadDone() {

    g_sprites.pirate = new Sprite(g_images.pirate);
    g_sprites.sea1 = new Sprite(g_images.sea1);
    g_sprites.sea2 = new Sprite(g_images.sea2);
    g_sprites.boat = new Sprite(g_images.boat);
    g_sprites.beach1 = new Sprite(g_images.beach1);
    g_sprites.black_shirt = new Sprite(g_images.black_shirt);
    g_sprites.parrot =  new Sprite(g_images.parrot);
    g_sprites.sword1 = new Sprite(g_images.sword1);
    g_sprites.shop = new Sprite(g_images.shop);
    g_sprites.healthfull = new Sprite(g_images.healthfull);
    g_sprites.healthempty = new Sprite(g_images.healthempty);
    g_sprites.pier = new Sprite(g_images.pier);
    g_sprites.underwater1 = new Sprite(g_images.underwater1);
    g_sprites.underwater2 = new Sprite(g_images.underwater2);
    g_sprites.background = new Sprite(g_images.background);
    g_sprites.treasure = new Sprite(g_images.treasure);
    g_sprites.bubble = new Sprite(g_images.bubble);
    g_sprites.bottleempty = new Sprite(g_images.bottleempty);
    g_sprites.bottlefull = new Sprite(g_images.bottlefull);
    g_sprites.marker = new Sprite(g_images.marker);
    g_sprites.map = new Sprite(g_images.map);

    main.init();
}

window.addEventListener('beforeunload', function(e) {
    let request = function() {
        let requestData = {IsQuitting : true, IsLoggedIn : isLoggedIn};

        $.ajax({
            type: "POST",
            cache: false,
            url: "",
            data: requestData, // parameters
            success:
                function (response) {

                },
            error:function () {
                console.log("failed to send request");
            }
        });
    };

    request();
});

function resizeGame() {
    let gameArea = document.getElementById('gameArea');
    let widthToHeight = 4 / 3;
    let margin = 50;
    let newWidth = window.innerWidth - 50;
    let newHeight = window.innerHeight - 50;
    let newWidthToHeight = newWidth / newHeight;

    if (newWidthToHeight > widthToHeight) {
        newWidth = newHeight * widthToHeight;
        gameArea.style.height = newHeight + 'px';
        gameArea.style.width = newWidth + 'px';
    } else {
        newHeight = newWidth / widthToHeight;
        gameArea.style.width = newWidth + 'px';
        gameArea.style.height = newHeight + 'px';
    }

    gameArea.style.marginTop = (-newHeight / 2) + 'px';
    gameArea.style.marginLeft = (-newWidth / 2) + 'px';

    let gameCanvas = document.getElementById('myCanvas');
    gameCanvas.style.width = newWidth - margin;
    gameCanvas.style.height = newHeight - margin;
    gameCanvas.height = 600;
    gameCanvas.width = 800;
}

window.addEventListener('resize', resizeGame, false);
window.addEventListener('orientationchange', resizeGame, false);

// Kick it off
resizeGame();
requestPreloads();

