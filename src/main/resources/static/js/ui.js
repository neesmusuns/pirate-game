function renderUI(health, drink, breath, hasTreasure){
    for(let i = 0; i < 3; i++){
        let heartSprite = i < Math.round(health) ? "healthfull" : "healthempty";

        let heart = new UIElement({
            x : -entityManager.posShift.x + 40 + 30*i,
            y : -entityManager.posShift.y + 40,
            scaleX : 2,
            scaleY : 2,
            sprite : heartSprite
        });

        heart.render(ctx)
    }

    if(breath < 10){
        for(let i = 0; i < Math.round(breath); i++){
            let bubble = new UIElement({
                x : -entityManager.posShift.x + 40 + 20*i,
                y : -entityManager.posShift.y + 80,
                scaleX : 2,
                scaleY : 2,
                sprite : 'bubble'
            });

            bubble.render(ctx)
        }
    }

    if(hasTreasure){
        let treasure = new UIElement({
            x : -entityManager.posShift.x + 40,
            y : -entityManager.posShift.y + 120,
            scaleX : 1,
            scaleY : 1,
            sprite : 'treasure'
        });

        treasure.render(ctx)
    }
}