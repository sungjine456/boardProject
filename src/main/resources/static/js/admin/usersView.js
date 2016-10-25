var usersView = {
	translatePasswordBtn : $(".translatePasswordBtn"),
	form : $("#form"),
	email : $("#email"),
	tramslatePasswordEvent : function(event){
		var target = $(event.target);
		this.email.val(target.data("email"));
        form.submit();
    },
	init : function(){
		var self = this;
		self.translatePasswordBtn.click(function(){self.tramslatePasswordEvent(event)});
	}	
}

usersView.init();