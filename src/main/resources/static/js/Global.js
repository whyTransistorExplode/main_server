


function authorizedAjaxCall(data, method, url, onsuccess, onerror) {
    return tokenAjaxCall(data, method, url, window.localStorage.getItem('token'), 'json', onsuccess, onerror);
}

function authorizedAjaxSyncCall(data, method, url) {
    return tokenAjaxSyncCall(data, method, url, window.localStorage.getItem('token'), 'json');
}

function authorizedAPICall(data, method, url, datatype, onsuccess) {
    return tokenAjaxCall_v2(data, method, url, window.localStorage.getItem('token'), datatype, onsuccess)
}

function tokenAjaxCall(body, method, url, token, datatype, onsuccess, onerror) {
    $.ajax(url, {
        type: "GET"
        , data: body
        , headers: {
            "Authorization": token
        }
        , contentType: 'application/x-www-form-urlencoded'
        , success: function (result, status, xhr) {
            onsuccess(result, status, xhr)
        }
        , onerror: function (result, status, xhr) {
            onerror(result, status, xhr)
        }
    })

}

function tokenAjaxSyncCall(body, method, url, token, datatype) {
    let res = {};
    $.ajax(url, {
        type: method
        , data: body
        , headers: {
            "Authorization": token
        }
        , dataType: datatype
        , success: function (result, status, xhr) {
            res = result
        }
        , async: false
    });
    return res;
}

function tokenAjaxCall_v2(body, method, url, token, contentType, onsuccess) {
    $.ajax(url, {
        type: method
        , body: {body}
        , headers: {
            "Authorization": token
        }
        , contentType: contentType
        , success: function (result, status, xhr) {
            onsuccess(result, status, xhr)
        }
    })

}