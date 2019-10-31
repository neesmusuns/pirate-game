let isLoggedIn = false;

function logIn(){
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    let request = function() {
        let requestData = {IsLoggedIn : false,
                           Username : username,
                            Password : password};

        $.ajax({
            type: "POST",
            cache: false,
            url: "",
            data: requestData, // parameters
            success:
                function (response) {
                    parseLogInResponse(response);
                },
            error:function () {
                console.log("failed to send request");
            }
        });
    };

    if(!isLoggedIn)
        request();
}

function parseLogInResponse(response){
    let parsedResponse = JSON.parse(response);
    isLoggedIn = parsedResponse.IsLoggedIn == 'true';
    if(isLoggedIn){
        let logInDiv = document.getElementById("log-in-form");
        logInDiv.parentNode.removeChild(logInDiv);
        entityManager.updateGameState(response);
    }

}
