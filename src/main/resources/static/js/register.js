// 采用JQuery完成AJAX，实现用户名检测
function ine(data) {
    if (data === 1) {
        document.getElementById("uMsg").innerText = "Username already exists";
    } else if (data === 2) {
        document.getElementById("uMsg").innerText = "Can not be empty";
    } else if (data === 3) {
        document.getElementById("uMsg").innerText = "Username can be used";
    } else if (data === 0) {
        document.getElementById("uMsg").innerText = "AJAX return null";
    } else {
        document.getElementById("uMsg").innerText = "Unknown error";
    }
}

function checkUser() {
    let name = $('#username').val();
    $.ajax({
        type: "post",
        url: '/account/usernameExists',
        data: "username=" + name,
        success: function (data) {
            ine(data);
        }
    });
}

// 密码检测
function checkPwd() {
    let pwd = document.getElementById("password").value;
    let rePsw = document.getElementById("repeatedPassword").value;
    if (pwd !== rePsw) {
        document.getElementById("pMsg").innerText = "Inconsistent password!";
    } else {
        document.getElementById("pMsg").innerText = "";
    }
}

$(document).ready(function () {
    $("#username").blur(function () {
        checkUser();
    });
})
