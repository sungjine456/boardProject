var commentReply = {
	replyBtn : $(".commentReplyBtn"),
	replyComment : $(".replyComment"),
	replyEvent : function(event){
		var target = $(event.target);
		var idx = target.data('idx');
		var replyComment = $("#replyComment"+idx);
		var replyForm = $("#commentReplyForm"+idx);
		if(replyComment.val() == ""){
			alert("댓글을 입력해주세요.");
			return;
		}
		replyForm.submit();
	},
	remainingEvent : function(event){
		var target = $(event.target);
		var idx = target.data('idx');
		var replyCommentLengthCount = $("#replyCommentLengthCount" + idx);
		var maximumCount = Number(replyCommentLengthCount.data("maxcount"));
		
	    var now = maximumCount - target.val().length;
	    if (now < 0) {
	    	changeCountNum(target, maximumCount);
	        alert('글자 입력수를 초과하였습니다.');
	        now = 0;
	    }
	    replyCommentLengthCount.text(now);
	},
	changeCountNum : function(target, maximumCount){
		var str = target.val();
		target.val(str.substr(0, maximumCount));
	},
	init : function(){
		var self = this;
		self.replyBtn.click(function(){self.replyEvent(event)});
		self.replyComment.keyup(function(){self.remainingEvent(event)});
	}
}

commentReply.init();