var mypage = {
	leaveForm : $("#leaveForm"),
	updateForm : $("#updateForm"),
	leaveBtn : $("#leaveForm #leaveBtn"),
	updateBtn : $("#updateForm #updateBtn"),
	leavePassword : $("#leaveForm #leavePassword"),
	updatePassword : $("#updateForm #updatePassword"),
	leaveEvent : function(){
		if(this.leavePassword.val() == ""){
			alert("비밀번호를 입력해주세요.");
			return false;
		}
		this.leaveForm.submit();
	},
	updateEvent : function(){
		if(this.updatePassword.val() == ""){
			alert("비밀번호를 입력해주세요.");
			return false;
		}
		this.updateForm.submit();
	},
	init : function(){
		var self = this;
		self.leaveBtn.click(function(){self.leaveEvent();});
		self.updateBtn.click(function(){self.updateEvent();});
	}
}

mypage.init();