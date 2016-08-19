var commentReply = {
	replyBtn : $(".commentReplyBtn"),
	replyEvent : function(event){
		var target = $(event.target);
		var idx = target.attr("idx");
		var replyComment = $("#replyComment"+idx);
		var replyForm = $("#commentReplyForm"+idx);
		if(replyComment.val() == ""){
			alert("뎃글을 입력해주세요.");
			return;
		}
		replyForm.submit();
	},
	init : function(){
		var self = this;
		self.replyBtn.click(function(){self.replyEvent(event)});
	}
}

commentReply.init();