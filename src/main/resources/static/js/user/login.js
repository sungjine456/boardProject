var login = {
	id : $("#form #id"),
	password : $("#form #password")
};

$("#joinBtn").click(function(){
	$(location).attr("href", "/addUser");
});

$("#loginBtn").click(function(){
	if(login.id.val() === ""){
		alert("아이디를 입력해주세요.");
		return false;
	}
	
	if(login.password.val() === ""){
		alert("패스워드를 입력해주세요.");
		return false;
	}
	
	if(login.pw.val().length < 6){
		alert("비밀번호는 6자 이상  입력하셔야 합니다.");
		return false;
	}
});