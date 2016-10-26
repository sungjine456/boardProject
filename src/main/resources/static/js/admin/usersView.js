var usersView = {
	translatePasswordBtn : $(".translatePasswordBtn"),
	emailAccessReBtn : $(".emailAccessReBtn"),
	translatePasswordForm : $("#translatePasswordForm"),
	emailAccessReForm : $("#emailAccessReForm"),
	translatePasswordEmail : $("#translatePasswordEmail"),
	emailAccessReEmail : $("#emailAccessReEmail"),
	tramslatePasswordEvent : function(event){
		var target = $(event.target);
		this.translatePasswordEmail.val(target.data("email"));
		this.translatePasswordForm.submit();
    },
    emailAccessReEvent : function(event){
    	var target = $(event.target);
		this.emailAccessReEmail.val(target.data("email"));
        this.emailAccessReForm.submit();
    },
	init : function(){
		var self = this;
		self.translatePasswordBtn.click(function(){self.tramslatePasswordEvent(event)});
		self.emailAccessReBtn.click(function(){self.emailAccessReEvent(event)});
	}	
}

usersView.init();