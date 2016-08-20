var commentUpdate = {
	updateBtn : $(".commentUpdateBtn"),
	updateComment : $(".updateComment"),
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
	remainingEvent : function(event){
		var target = $(event.target);
		var idx = target.attr("idx");
		var updateCommentLengthCount = $("#updateCommentLengthCount" + idx);
		var maximumCount = Number(updateCommentLengthCount.attr("maxCount"));
		
	    var now = maximumCount - target.val().length;
	    if (now < 0) {
	    	alert("now : " + now);
	        var str = target.val();
	        alert('글자 입력수를 초과하였습니다.');
	        target.val(str.substr(0, maximumCount));
	        now = 0;
	    }
	    updateCommentLengthCount.text(now);
	},
	init : function(){
		var self = this;
		self.updateBtn.click(function(){self.updateEvent(event)});
		self.updateComment.keyup(function(){self.remainingEvent(event)});
	}
}

commentUpdate.init();