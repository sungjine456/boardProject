<div style="float:right; padding-right: 5%;">
	<a class="commentUpdateBtn" idx="${idx!0}">수정</a>
	<span>&nbsp;/&nbsp;</span>
	<a href="/boardDetail?num=${num!0}">취소</a>
</div>
<form id="commentUpdateForm${idx}" action="/updateComment" method="post">
	<input type="hidden" name="num" value="${num!0}">
	<input type="hidden" name="idx" value="${idx!0}">
	<br/>
	<input type="text" id="updateComment${idx}" class="form-control" name="comment" value="${comment!""}" autofocus/>
	<DIV style="float:right">남은 글자수: <SPAN id="updateCommentLengthCount${idx}" maxCount="200">200</SPAN></DIV>
</form>
<script type="text/javascript">
	var commentUpdateBase = {
		updateComment : $("#updateComment${idx}"),
		updateCommentLengthCount : $("#updateCommentLengthCount${idx}"),
		remainingEvent : function(){
			var maximumCount = this.updateCommentLengthCount.attr("maxCount") * 1;
			
		    var now = maximumCount - this.updateComment.val().length;
		    if (now < 0) {
		    	alert("now : " + now);
		        var str = this.updateComment.val();
		        alert('글자 입력수를 초과하였습니다.');
		        this.updateComment.val(str.substr(0, maximumCount));
		        now = 0;
		    }
		    this.updateCommentLengthCount.text(now);
		},
		init : function(){
			var self = this;
			self.updateComment.keyup(function(){self.remainingEvent()});
		}
	}
	commentUpdateBase.init();
	$(document).ready(function(){
		commentUpdateBase.remainingEvent();
	});
</script>
<script type="text/javascript" src="js/board/comment/commentUpdate.js"></script>