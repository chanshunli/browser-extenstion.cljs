function login(usr, pwd){
    console.log("save password to localStorage");
    localStorage["api_usr"] = usr;
    localStorage["api_pwd"] = pwd;
};
document.getElementById('login-bt').onclick = function () {
    login(document.getElementById('name').value, document.getElementById('confirm').value);
};

