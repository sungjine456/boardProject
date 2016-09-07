<div style="float:right; padding: 5px 3% 0px 0px;">
	<input class="commentReplyBtn a-btn" data-idx="${commentIdx!0}" type="button" value="답글"/>
	<span>&nbsp;/&nbsp;</span>
	<a href="/boardDetail?boardNum=${boardNum!0}">취소</a>
</div>
<form id="commentReplyForm${commentIdx!0}" action="/writeReply" method="post">
	<input type="hidden" name="boardNum" value="${boardNum!0}">
	<input type="hidden" name="idx" value="${commentIdx!0}">
	<br/>
	<input type="text" class="replyComment form-control" name="comment" data-idx="${commentIdx!0}"/>
	<DIV style="float:right">남은 글자수: <SPAN id="replyCommentLengthCount${commentIdx!0}" data-maxCount="200">200</SPAN></DIV>
</form>
<script type="text/javascript" src="js/board/comment/commentReply.js"></script>