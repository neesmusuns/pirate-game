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

    request();
}

function parseLogInResponse(response){
    isLoggedIn = response == 'true';
    console.log(isLoggedIn);
}
