var cookieUserName = $.cookie("userName");
var cookieUserPwd = $.cookie("userPwd");

if (cookieUserName != undefined && cookieUserName != null) {

    $('#exampleInputEmail').val(cookieUserName)
}
if (cookieUserName != undefined && cookieUserName != null) {
    $('#exampleInputPassword').val(cookieUserPwd);
}


function login() {
    /**
     * 1. 获取用户名和密码
     * 2. 前台校验参数是否为空
     * 3. 发送ajax请求
     * 4. 处理返回信息
     * */

    var userName = $('#exampleInputEmail').val();
    var userPwd = $('#exampleInputPassword').val();

    if (isEmpty(userName)) {
        alert("用户名为空!");
        return;
    }
    if (isEmpty(userPwd)) {
        alert("密码为空!");
        return;
    }

    $.ajax({
        url: 'user/login',//后台请求地址
        type: 'post',
        data: {
            userName: userName,
            userPwd: userPwd
        },//请求参数
        success: function (data) {
            // 请求成功后的处理

            if (data.code == 200) {
                alert(data.msg);
                /***
                 * 1.存储cookie信息
                 * 2.页面跳转到主页
                 * */
                $.cookie("userIdStr", data.result.userIdStr);
                $.cookie("userName", data.result.userName);
                $.cookie("trueName", data.result.trueName);

                if ($('#customCheck')[0].checked) {

                    $.cookie("userPwd", userPwd);
                }
                window.location.href = 'index';
            } else {
                alert(data.msg);
            }
        }
    });
}