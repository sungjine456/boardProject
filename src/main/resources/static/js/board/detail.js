var detail = {
	commentForm : $("#commentForm"),
	boardUpdateBtn : $("#boardUpdateBtn"),
	commentBtn : $("#commentForm #commentBtn"),
	num : $("#commentForm #num"),
	writeComment : $("#commentForm #writeComment"),
	commentUpdateBtn : $("#commentForm .commentUpdateBtn"),
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
	init : function(){
		this.boardUpdateBtn.click(function(){detail.boardUpdateEvent();});
		this.commentBtn.click(function(){detail.commentEvent();});
		this.commentUpdateBtn.click(function(){detail.commentUpdateEvent(event);});
	}
}

detail.init();