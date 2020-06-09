//退出
function logout() {

    $.removeCookie("userIdStr");
    $.removeCookie("userName");
    $.removeCookie("trueName");
    window.location.href = 'login';


}

