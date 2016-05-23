$("#join_btn").click(function(){
	var email = $("#form #email");
	var pw = $("#form #password");
	var pwConfirm = $("#form #passwordConfirm");
	
	if(pw.val() === ""){
		alert("비밀번호를 입력해주세요");
		return false;
	}
	
	if(pw.val() !== pwConfirm.val()){
		alert("비밀번호를 확인해주세요");
		return false;
	}
	
	var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	if(email.val().length < 6 || !regex.test(email.val())) {
		alert("이메일을 확인해주세요");
		return false;
	}
})