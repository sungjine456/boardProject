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
		this.leaveBtn.click(function(){mypage.leaveEvent();});
		this.updateBtn.click(function(){mypage.updateEvent();});
	}
}

mypage.init();