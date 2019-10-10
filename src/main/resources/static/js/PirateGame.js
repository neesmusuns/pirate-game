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

// =================
// UPDATE SIMULATION
// =================

// GAME-SPECIFIC DIAGNOSTICS

var KEY_0 = keyCode('0');

var KEY_1 = keyCode('1');

var KEY_K = keyCode('K');

function processDiagnostics() {
    if(eatKey(KEY_K)){

    }

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
        pirate: '../images/pirate0.png'
    };

    imagesPreload(requiredImages, g_images, preloadDone);
}

let g_sprites = {};

function preloadDone() {

    g_sprites.pirate = new Sprite(g_images.pirate);

    main.init();
}

// Kick it off
requestPreloads();