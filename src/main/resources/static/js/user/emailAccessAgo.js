var emailAccessAgo = {
	form : $("#form"),
	accessBtn : $("#accessBtn"),
	accessEvent : function(){
		this.form.submit();
	},
	init : function(){
		var self = this;
		self.accessBtn.click(function(){self.accessEvent();});
	}
}

emailAccessAgo.init();