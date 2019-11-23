function renderUI(health, drink){
    for(let i = 0; i < 3; i++){
        let heartSprite = i < health ? "healthfull" : "healthempty";

        let heart = new UIElement({
            x : -entityManager.posShift.x + 40 + 30*i,
            y : -entityManager.posShift.y + 40,
            scaleX : 2,
            scaleY : 2,
            sprite : heartSprite
        });

        heart.render(ctx)
    }
}