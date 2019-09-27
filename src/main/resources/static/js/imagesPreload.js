// Multi-Image Preloader

"use strict";

/*jslint browser: true, devel: true, white: true */

var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");

/*
0        1         2         3         4         5         6         7         8
12345678901234567890123456789012345678901234567890123456789012345678901234567890
*/

Image.prototype.asyncLoad = function(src, asyncCallback) {
    this.onload = asyncCallback;
    this.onerror = asyncCallback;
    
    console.log("requesting image src of ", src);
    this.src = src;
};


// imagePreload
//
// IN  : `requiredImages` - an object of <name:uri> pairs for each image
// OUT : `loadedImages` - object to which our <name:Image> pairs will be added
// IN  : `completionCallback` - will be executed when everything is done
//
function imagesPreload(requiredImages,
                       loadedImages,
                       completionCallback) {

    var numImagesRequired,
        numImagesHandled = 0,
        currentName,
        currentImage,
        preloadHandler;

    numImagesRequired = Object.keys(requiredImages).length;

    preloadHandler = function () {

        console.log("preloadHandler called with this=", this);
        loadedImages[this.name] = this;

        if (0 === this.width) {
            console.log("loading failed for", this.name);
        }

        this.onload = null;
        this.onerror = null;

        numImagesHandled += 1;

        if (numImagesHandled === numImagesRequired) {
            console.log("all preload images handled");
            console.log("loadedImages=", loadedImages);
            console.log("");
            console.log("performing completion callback");

            completionCallback();

            console.log("completion callback done");
            console.log("");
        }
    };

    for (currentName in requiredImages) {
        if (requiredImages.hasOwnProperty(currentName)) {
            
            console.log("preloading image", currentName);
            currentImage = new Image();
            currentImage.name = currentName;

            currentImage.asyncLoad(requiredImages[currentName], preloadHandler);
        }
    }
}
