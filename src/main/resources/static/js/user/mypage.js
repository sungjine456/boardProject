var mypage = {
	pw : $("#form #password"),
	changePw : $("#form #changePassword"),
	changePwConfirm : $("#form #changePasswordConfirm"),
	passwordChangeBtn : $("#passwordChangeBtn")
}

mypage.passwordChangeBtn.click(function(){
	if(mypage.pw.val() == ""){
		alert("비밀번호 칸을 입력해주세요.");
		return false;
	}
	if(mypage.changePw.val() == ""){
		alert("비밀번호 수정칸을 입력해주세요.");
		return false;
	}
	if(mypage.changePwConfirm.val() == ""){
		alert("비밀번호 수정 확인칸을 입력해주세요.");
		return false;
	}
	if(mypage.changePw.val().length < 6 && mypage.changePwConfirm.val().length < 6){
		alert("비밀번호 수정은 6자 이상 입력해주세요.");
		return false;
	}
	if(mypage.changePw.val() !== mypage.changePwConfirm.val()){
		alert("비밀번호 수정 확인칸을 다시 입력해주세요");
		return false;
	}
});