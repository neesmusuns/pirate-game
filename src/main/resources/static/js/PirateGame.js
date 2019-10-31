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

    keys.forEach(function (value, keyCode) {
        if(value)
            keyPresses += (String(keyCode)) + " ";
    });

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
        sea1: '../images/tiles/sea1.png',
        sea2: '../images/tiles/sea2.png',
        boat: '../images/boat0.png',
        beach1:'../images/tiles/beach1.png',
        black_shirt:'../images/black_shirt.png',
        parrot:'../images/parrot1.png',
        sword1:'../images/sword1.png'
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
    var gameArea = document.getElementById('gameArea');
    var widthToHeight = 4 / 3;
    var margin = 50;
    var newWidth = window.innerWidth - 50;
    var newHeight = window.innerHeight - 50;
    var newWidthToHeight = newWidth / newHeight;

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

    var gameCanvas = document.getElementById('myCanvas');
    gameCanvas.style.width = newWidth - margin;
    gameCanvas.style.height = newHeight - margin;
    gameCanvas.height = 600;
    gameCanvas.width = 800;
}

window.addEventListener('resize', resizeGame, false);
window.addEventListener('orientationchange', resizeGame, false);

// Kick it off
resizeGame()
requestPreloads();

