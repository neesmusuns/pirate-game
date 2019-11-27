function buy(item){
    let exitObject = {IsBuying: true, Item: item};
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

function sell(){
    let exitObject = {IsSelling: true};
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