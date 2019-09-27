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


// ====================
// CREATE INITIAL SHIPS
// ====================

entityManager.generateShip({
    cx : 140,
    cy : 200
});

entityManager.generateShip({
    cx : 200,
    cy : 200,

   // numSubSteps : 2
});

entityManager.generateShip({
    cx : 260,
    cy : 200,

    //numSubSteps : 4
});

// =================
// UPDATE SIMULATION
// =================

// GAME-SPECIFIC UPDATE LOGIC

function updateSimulation(du) {
    
    processDiagnostics();
    
    entityManager.update(du);

    // Prevent perpetual firing!
    eatKey(Ship.prototype.KEY_FIRE);
}

// GAME-SPECIFIC DIAGNOSTICS

var g_allowMixedActions = true;
var g_useGravity = false;
var g_useAveVel = true;

var KEY_GRAVITY = keyCode('G');
var KEY_AVE_VEL = keyCode('V');

var KEY_HALT  = keyCode('H');
var KEY_RESET = keyCode('R');

var KEY_0 = keyCode('0');

var KEY_1 = keyCode('1');

var KEY_K = keyCode('K');

function processDiagnostics() {

    if (eatKey(KEY_GRAVITY)) g_useGravity = !g_useGravity;

    if (eatKey(KEY_AVE_VEL)) g_useAveVel = !g_useAveVel;

    if (eatKey(KEY_HALT)) entityManager.haltShips();

    if (eatKey(KEY_RESET)) entityManager.resetShips();

    if (eatKey(KEY_0)) entityManager.toggleRocks();

    if (eatKey(KEY_1)) entityManager.generateShip({
	cx : g_mouseX,
	cy : g_mouseY});

    if (eatKey(KEY_K)) entityManager.killNearestShip(
        g_mouseX, g_mouseY);
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

var g_images = {};

function requestPreloads() {

    var requiredImages = {
	ship   : "https://notendur.hi.is/~pk/308G/images/ship.png",
	rock   : "https://notendur.hi.is/~pk/308G/images/rock.png"
    };

    imagesPreload(requiredImages, g_images, preloadDone);
}

var g_sprites = {};

function preloadDone() {

    g_sprites.ship = new Sprite(g_images.ship);
    g_sprites.rock = new Sprite(g_images.rock);

    g_sprites.bullet = new Sprite(g_images.ship);
    g_sprites.bullet.scale = 0.25;

    main.init();
}

// Kick it off
requestPreloads();