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
        pirate: '../images/pirate0.png'
    };

    imagesPreload(requiredImages, g_images, preloadDone);
}

let g_sprites = {};

function preloadDone() {

    g_sprites.pirate = new Sprite(g_images.pirate);

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

// Kick it off
requestPreloads();