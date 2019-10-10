// GENERIC RENDERING

let g_frameCounter = 1;


function render(ctx) {

    ctx.imageSmoothingEnabled = false;

    // Clear the canvas
    //
    util.clearCanvas(ctx);
    
    // The core rendering of the actual game / simulation
    //
    renderSimulation(ctx);

    
    ++g_frameCounter;
}
