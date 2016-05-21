$("#join_btn").click(function(){
	var email = $("#form #e_m");
	var pw = $("#form #p_w");
	var pw_confirm = $("#form #p_w_confirm");
	
	if(pw.val() === ""){
		alert("비밀번호를 입력해주세요");
		return false;
	}
	
	if(pw.val() !== pw_confirm.val()){
		alert("비밀번호를 확인해주세요");
		return false;
	}
	
	var regex=/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	if(ele.value.length<6 || !regex.test(email.val())) {
		alert("이메일을 확인해주세요");
		return false;
	}
})