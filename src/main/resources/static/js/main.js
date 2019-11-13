// ========
// MAINLOOP
// ========
/*

The mainloop is one big object with a fairly small public interface
, and a bunch of private internal helper methods.


*/

"use strict";

/* jshint browser: true, devel: true, globalstrict: true */

/*
0        1         2         3         4         5         6         7         8
12345678901234567890123456789012345678901234567890123456789012345678901234567890
*/


var main = {
    
    // "Frame Time" is a (potentially high-precision) frame-clock for animations
    _frameTime_ms : null,
    _frameTimeDelta_ms : null,
    _hasRequestedNextFrame : false
};

// Perform one iteration of the mainloop
main.iter = function (frameTime) {

    // Use the given frameTime to update all of our game-clocks
    this._updateClocks(frameTime);

    // Perform the iteration core to do all the "real" work
    if(isLoggedIn)
        this._iterCore(this._frameTimeDelta_ms);
    
    // Request the next iteration if needed
    main._hasRequestedNextFrame = false;
    this._requestNextIteration();
};

main._updateClocks = function (frameTime) {
    
    // First-time initialisation
    if (this._frameTime_ms === null) this._frameTime_ms = frameTime;
    
    // Track frameTime and its delta
    this._frameTimeDelta_ms = frameTime - this._frameTime_ms;
    this._frameTime_ms = frameTime;
};

main._iterCore = function () {
    render(g_ctx);

    let keyObject = {IsLoggedIn : true,
                     Keys : collectInput()};

    let request = function() {
        $.ajax({
            type: "POST",
            cache: false,
            url: "",
            data: keyObject, // parameters
            success:
                function (response) {
                    entityManager.updateGameState(response);
                },
            error:function () {
                console.log("failed to send request");
            }
        });
    };

    request();

};

// Annoying shim for Firefox and Safari
window.requestAnimationFrame = 
    window.requestAnimationFrame ||        // Chrome
    window.mozRequestAnimationFrame ||     // Firefox
    window.webkitRequestAnimationFrame;    // Safari

// This needs to be a "global" function, for the "window" APIs to callback to
function mainIterFrame(frameTime) {
    main.iter(frameTime);
}

main._requestNextIteration = function () {
    if(!main._hasRequestedNextFrame) {
        setTimeout(function () {
            window.requestAnimationFrame(mainIterFrame);
        }, 16);
        main._hasRequestedNextFrame = true;
    }
};

main.init = function () {
    g_ctx.fillStyle = "white";

    this._requestNextIteration();
};