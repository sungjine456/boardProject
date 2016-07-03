var mypage = {
	updateBtn : $("#form #updateBtn"),
	leaveForm : $("#form #leaveForm"),
	leavePassword : $("#leaveForm #leavePassword"),
	leaveBtn : $("#leaveForm #leaveBtn"),
	leaveEvent : function(){
		if(this.leavePassword.val() == ""){
			alert("비밀번호를 입력해주세요.");
			return false;
		}
		this.leaveForm.submit();
	},
	updateEvent : function(){
		$(location).attr("href", "/update");
	},
	init : function(){
		var self = this;
		self.leaveBtn.click(function(){self.leaveEvent();});
		self.updateBtn.click(function(){self.updateEvent();});
	}
}

mypage.init();