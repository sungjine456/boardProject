var update = {
	email : $("#form #email"),
	name : $("#form #name"),
	password : $("#changeForm #password"),
	changePassword : $("#changeForm #changePassword"),
	changePasswordConfirm : $("#changeForm #changePasswordConfirm"),
	passwordChangeBtn : $("#changeForm #passwordChangeBtn"),
	updateBtn : $("#form #updateBtn"),
	updateForm : $("#form"), 
	changeForm : $("#changeForm"),
	passwordChangeEvent : function(){
		if(this.password.val() == ""){
			alert("비밀번호 칸을 입력해주세요.");
			return false;
		}
		if(this.changePassword.val() == ""){
			alert("비밀번호 수정칸을 입력해주세요.");
			return false;
		}
		if(this.changePasswordConfirm.val() == ""){
			alert("비밀번호 수정 확인칸을 입력해주세요.");
			return false;
		}
		if(this.changePassword.val().length < 6 && this.changePasswordConfirm.val().length < 6){
			alert("비밀번호 수정은 6자 이상 입력해주세요.");
			return false;
		}
		if(this.changePassword.val() !== this.changePasswordConfirm.val()){
			alert("비밀번호 수정 확인칸을 다시 입력해주세요");
			return false;
		}
		this.changeForm.submit();
	},
	updateEvent : function(){
		if(this.name.val() === "") {
			alert("이름을 입력해주세요");
			this.name.focus();
			return false;
		}
		if(this.email.val() === "") {
			alert("이메일을 입력해주세요");
			this.email.focus();
			return false;
		}
		this.updateForm.submit();
	},
	init : function(){
		this.passwordChangeBtn.click(function(){update.passwordChangeEvent();});
		this.updateBtn.click(function(){update.updateEvent();});
	}
}

update.init();