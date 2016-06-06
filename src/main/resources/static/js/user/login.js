var login = {
	id : $("#form #id"),
	password : $("#form #password"),
	checkBox : $("#idSaveCheck"),
	joinBtn : $("#joinBtn"),
	translatePasswordBtn : $("#translatePasswordBtn"),
	loginBtn : $("#loginBtn"),
};

login.joinBtn.click(function(){
	$(location).attr("href", "/join");
});

login.translatePasswordBtn.click(function(){
	$(location).attr("href", "/translatePassword");
});

login.loginBtn.click(function(){
	if(login.id.val() === ""){
		alert("아이디를 입력해주세요.");
		return false;
	}
	
	if(login.password.val() === ""){
		alert("패스워드를 입력해주세요.");
		return false;
	}
	
	if(login.password.val().length < 6){
		alert("비밀번호는 6자 이상  입력하셔야 합니다.");
		return false;
	}
});

$(document).ready(function(){
    login.checkBox.change(function(){
        if(!login.checkBox.is(":checked")){
            deleteCookie("saveId");
        }
    });
});
 
function deleteCookie(cookieName){
    var date = new Date();
    date.setDate(date.getDate() - 1);
    document.cookie = cookieName + "= ; expires=" + date.toGMTString();
}