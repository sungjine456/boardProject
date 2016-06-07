var mypage = {
	changeForm : $("#changeForm"),
	leaveForm : $("#leaveForm"),
	password : $("#changeForm #password"),
	changePw : $("#changeForm #changePassword"),
	changePwConfirm : $("#changeForm #changePasswordConfirm"),
	leavePassword : $("#leaveForm #leavePassword"),
	passwordChangeBtn : $("#changeForm #passwordChangeBtn"),
	leaveBtn : $("#leaveForm #levaeBtn"),
	passwordChangeEvent : function(){
		if(this.password.val() == ""){
			alert("비밀번호 칸을 입력해주세요.");
			return false;
		}
		if(this.changePw.val() == ""){
			alert("비밀번호 수정칸을 입력해주세요.");
			return false;
		}
		if(this.changePwConfirm.val() == ""){
			alert("비밀번호 수정 확인칸을 입력해주세요.");
			return false;
		}
		if(this.changePw.val().length < 6 && this.changePwConfirm.val().length < 6){
			alert("비밀번호 수정은 6자 이상 입력해주세요.");
			return false;
		}
		if(this.changePw.val() !== this.changePwConfirm.val()){
			alert("비밀번호 수정 확인칸을 다시 입력해주세요");
			return false;
		}
		this.changeForm.submit();
	},
	leaveEvent : function(){
		if(this.leavePassword.val() == ""){
			alert("비밀번호를 입력해주세요.");
			return false;
		}
		this.leaveForm.submit();
	},
	init : function(){
		this.passwordChangeBtn.click(function(){mypage.passwordChangeEvent();});
		this.leaveBtn.click(function(){mypage.leaveEvent();});
	}
}

mypage.init();