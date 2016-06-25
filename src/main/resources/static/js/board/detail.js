var detail = {
	updateBtn : $("#updateBtn"),
	num : $("#num"),
	updateEvent : function(){
		$(location).attr("href", "/boardUpdateView?num=" + this.num.val());
	},
	init : function(){
		this.updateBtn.click(function(){detail.updateEvent();});
	}
}

detail.init();