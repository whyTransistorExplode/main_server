function formSubmit(){

    console.log('at least this happen')
    const username = document.getElementById("input-username").value;
    const password = document.getElementById("input-password").value;
    const reqData = {username, password};
    console.log(reqData);
    const result = $.ajax(
      '/api/v1/auth/authenticate',
        {
            contentType: 'application/json'
            ,type: 'POST'
            ,data: JSON.stringify(reqData)
            , dataType: 'JSON'
            , success: function(result, status, xhr) {
                setTokenInLocalStorage(result);

                location.replace("/web/pages/op/home")
            }
        }
    );


}

function setTokenInLocalStorage(response){
    window.localStorage.setItem('token', 'Bearer ' + response.token)
}