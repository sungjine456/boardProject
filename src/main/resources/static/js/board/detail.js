var detail = {
	commentForm : $("#commentForm"),
	boardUpdateBtn : $("#boardUpdateBtn"),
	commentBtn : $("#commentForm #commentBtn"),
	num : $("#commentForm #num"),
	comment : $("#commentForm #comment"),
	boardUpdateEvent : function(){
		$(location).attr("href", "/boardUpdateView?num=" + this.num.val());
	},
	commentEvent : function(){
		if(this.comment.val() == ""){
			alert("뎃글을 입력해주세요.");
			return;
		}
		this.commentForm.submit();
	},
	init : function(){
		this.boardUpdateBtn.click(function(){detail.boardUpdateEvent();});
		this.commentBtn.click(function(){detail.commentEvent();});
	}
}

detail.init();