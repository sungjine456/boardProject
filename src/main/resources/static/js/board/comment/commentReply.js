var commentReply = {
	replyBtn : $(".commentReplyBtn"),
	replyComment : $(".replyComment"),
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
	remainingEvent : function(event){
		var target = $(event.target);
		var idx = target.attr("idx");
		var replyCommentLengthCount = $("#replyCommentLengthCount" + idx);
		var maximumCount = Number(replyCommentLengthCount.attr("maxCount"));
		
	    var now = maximumCount - target.val().length;
	    if (now < 0) {
	    	alert("now : " + now);
	        var str = target.val();
	        alert('글자 입력수를 초과하였습니다.');
	        target.val(str.substr(0, maximumCount));
	        now = 0;
	    }
	    replyCommentLengthCount.text(now);
	},
	init : function(){
		var self = this;
		self.replyBtn.click(function(){self.replyEvent(event)});
		self.replyComment.keyup(function(){self.remainingEvent(event)});
	}
}

commentReply.init();