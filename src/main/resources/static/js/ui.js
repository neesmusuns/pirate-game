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

    for(let i = 0; i < 3; i++){
        let bottleSprite = i < drink ? "bottlefull" : "bottleempty";

        let bottle = new UIElement({
            x : -entityManager.posShift.x + 40 + 30*i,
            y : -entityManager.posShift.y + 80,
            scaleX : 2,
            scaleY : 2,
            sprite : bottleSprite
        });

        bottle.render(ctx)
    }

    if(breath < 10){
        for(let i = 0; i < Math.round(breath); i++){
            let bubble = new UIElement({
                x : -entityManager.posShift.x + 40 + 20*i,
                y : -entityManager.posShift.y + 100,
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
function renderShopUI(items) {
    items.forEach(item=>{
        let t = `<div class="item">
                    <button class="btn-img" onclick="buy('${item.sprite}')">
                        <img src="../images/${item.sprite}.png">
                        <p> ${item.name}: ${item.price} </p>
                    </button>
                  </div>`;
        $("#items").append(t);
        }
    );
    let money = entityManager._stats.money;
    $("#gameArea").append(`<button class="btn-rem" id="money">GOLD: ${money}</button>`);
    $("#gameArea").append(`<button class="btn-rem" id="exit" type=\"submit\" onclick=\"exitShop()\">EXIT SHOP</button>`);
    $("#gameArea").append(`<button class="btn-rem" id="sell" type=\"submit\" onclick=\"sell()\">SELL</button>`);
}

function clearShopUI(){
    entityManager.isInShop = false;
    entityManager.hasGeneratedShopUI = false;
    $("#items").empty();
    var elmnt = document.getElementById("money");
    elmnt.remove();
    elmnt = document.getElementById("exit");
    elmnt.remove();
    elmnt = document.getElementById("sell");
    elmnt.remove();
}

function exitShop(){
    let exitObject = {HasExited: true};
    let request = function () {
        $.ajax({
            type: "POST",
            cache: false,
            url: "",
            data: exitObject, // parameters
            success:
                function (response) {

                },
            error: function () {
                console.log("failed to send request");
            }
        });
    };
    request();
}