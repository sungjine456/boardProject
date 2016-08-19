var commentUpdate = {
	updateBtn : $(".commentUpdateBtn"),
	updateEvent : function(event){
		var target = $(event.target);
		var idx = target.attr("idx");
		var form = $("#commentUpdateForm"+idx);
		var updateComment = $("#updateComment"+idx);
		if(updateComment.val() == ""){
			alert("뎃글을 입력해주세요.");
			return;
		}
		form.submit();
	},
	init : function(){
		var self = this;
		self.updateBtn.click(function(){self.updateEvent(event)});
	}
}

commentUpdate.init();