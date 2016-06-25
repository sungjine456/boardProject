var detail = {
	boardUpdateBtn : $("#boardUpdateBtn"),
	num : $("#num"),
	boardUpdateEvent : function(){
		$(location).attr("href", "/boardUpdateView?num=" + this.num.val());
	},
	init : function(){
		this.boardUpdateBtn.click(function(){detail.boardUpdateEvent();});
	}
}

detail.init();