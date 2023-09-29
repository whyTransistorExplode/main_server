// homepage js
function redirectLogin(){
    location.replace("/web/pages/op/login");
}


function start(){

    authorizedAjaxCall(null, 'get', '/api/v1/user/get-user', userExist)
}
start();

function userExist(result, status, xhr){
    console.log(result);
    document.getElementById('dropdown-authorized').hidden = false;
    document.getElementById('login-button').hidden = true;
    document.getElementById('profile-dropdown').innerText = result.name;
}

function callAdmin(){
    console.log("js call started")
    authorizedAjaxCall(null,"get","/api/v1/user/add-user",successCall)
}

function successCall(result, status, xhr){
    console.log(result)
}