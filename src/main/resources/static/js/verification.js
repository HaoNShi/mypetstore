let code;

function createCode() {
    code = "";
    const codeLength = 4;
    const codeChars = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']; //所有候选组成验证码的字符，当然也可以用中文的
    let checkCode = document.getElementById("checkCode");
    for (let i = 0; i < codeLength; i++) {
        const charNum = Math.floor(Math.random() * 52);
        code += codeChars[charNum];
    }
    if (checkCode) {
        checkCode.className = "code";
        checkCode.innerText = code;
    }
}

function validateCode() {
    const inputCode = document.getElementById("inputCode").value;
    if (inputCode.length <= 0) {
        document.getElementById("verMsg").innerText = "Please enter the verification code!";
        return false;
    } else if (inputCode.toUpperCase() !== code.toUpperCase()) {
        document.getElementById("verMsg").innerText = "Verification code incorrect!";
        return false;
    } else {
        document.getElementById("verMsg").innerText = "";
        return true;
    }
}
