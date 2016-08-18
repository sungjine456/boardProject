var detail = {
	commentForm : $("#commentForm"),
	boardUpdateBtn : $("#boardUpdateBtn"),
	commentBtn : $("#commentForm #commentBtn"),
	num : $("#commentForm #num"),
	likeCount : $("#likeCount"),
	writeComment : $("#commentForm #writeComment"),
	commentUpdateBtn : $("#commentForm .commentUpdateBtn"),
	commentReplyBtn : $("#commentForm .commentReplyBtn"),
	commentLengthCount : $("#commentLengthCount"),
	boardUpdateEvent : function(){
		$(location).attr("href", "/boardUpdateView?num=" + this.num.val());
	},
	commentEvent : function(){
		if(this.writeComment.val() == ""){
			alert("뎃글을 입력해주세요.");
			return;
		}
		this.commentForm.submit();
	},
	commentUpdateEvent : function(event){
		var target = $(event.target);
		var idx = target.attr("idx");
		var span = $("#commentSpan"+idx);
		$.ajax({
			url : "/updateCommentView",
			type : "POST",
			data : {"comment" : target.attr("value"), "num" : detail.num.val(), "idx" : idx},
			success : function(data){
				span.html(data);
			},
			error : function(x, e){
				if(x.status == 404){
					$(location).attr("href", "/error/404error.ftl");
				}
			}
		});
	},
	commentReplyEvent : function(event){
		var target = $(event.target);
		var idx = target.attr("idx");
		var span = $("#replySpan"+idx);
		$.ajax({
			url : "/replyView",
			type : "POST",
			data : {"num" : detail.num.val(), "idx" : idx},
			success : function(data){
				span.html(data);
			},
			error : function(x, e){
				if(x.status == 404){
					$(location).attr("href", "/error/404error.ftl");
				}
			}
		});
	},
	likeCountEvent : function(){
		var userIdx = this.likeCount.attr("userIdx");
		$.ajax({
			url : "/boardLikeCount",
			type : "POST",
			data : {"boardIdx" : detail.num.val(), "userIdx" : userIdx},
			dataType : "JSON",
			success : function(data){
				detail.likeCount.attr("userIdx", userIdx);
				detail.likeCount.text(data.like+" ("+data.likeCount+")");
			},
			error : function(x, e){
				if(x.status == 404){
					$(location).attr("href", "/error/404error.ftl");
				}
			}
		});
	},
	remainingEvent : function(){
        var maximumCount = this.commentLengthCount.attr("maxCount") * 1;
        
        var now = maximumCount - this.writeComment.val().length;
        if (now < 0) {
            var str = this.writeComment.val();
            alert('글자 입력수를 초과하였습니다.');
            this.writeComment.val(str.substr(0, maximumCount));
            now = 0;
        }
        this.commentLengthCount.text(now);
	},
	init : function(){
		var self = this;
		self.boardUpdateBtn.click(function(){self.boardUpdateEvent();});
		self.commentBtn.click(function(){self.commentEvent();});
		self.commentUpdateBtn.click(function(){self.commentUpdateEvent(event);});
		self.commentReplyBtn.click(function(){self.commentReplyEvent(event);});
		self.likeCount.click(function(){self.likeCountEvent()});
		self.writeComment.keyup(function(){self.remainingEvent()});
	}
}

detail.init();