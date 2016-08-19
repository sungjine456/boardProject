<div style="float:right; padding-right: 5%;">
	<a href="/boardDetail?num=${num!0}">취소</a>
</div>
<form id="commentReplyForm${idx!0}" action="/writeReply" method="post">
	<input type="hidden" name="num" value="${num!0}">
	<input type="hidden" name="idx" value="${idx!0}">
	<br/>
	<table style="background-color: #f4f4f4; width:98%;">
		<tr>
			<td style="padding-left: 7%; width: 90%; padding-right: 1%;">
				<input type="text" id="replyComment${idx!0}" class="form-control" name="comment"/>
				<DIV style="float:right">남은 글자수: <SPAN id="replyCommentLengthCount${idx!0}" maxCount="200">200</SPAN></DIV>
			</td>
			<td>
				<a class="commentReplyBtn" class="btn btn-primary" idx="${idx!0}">댓글</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	var commentReplyBase = {
		replyComment : $("#replyComment${idx}"),
		replyCommentLengthCount : $("#replyCommentLengthCount${idx}"),
		remainingEvent : function(){
			var maximumCount = this.replyCommentLengthCount.attr("maxCount") * 1;
			
		    var now = maximumCount - this.replyComment.val().length;
		    if (now < 0) {
		    	alert("now : " + now);
		        var str = this.replyComment.val();
		        alert('글자 입력수를 초과하였습니다.');
		        this.replyComment.val(str.substr(0, maximumCount));
		        now = 0;
		    }
		    this.replyCommentLengthCount.text(now);
		},
		init : function(){
			var self = this;
			self.replyComment.keyup(function(){self.remainingEvent()});
		}
	}
	commentReplyBase.init();
</script>
<script type="text/javascript" src="js/board/comment/commentReply.js"></script>