<div style="float:right; padding-right: 3%;">
	<input class="commentUpdateBtn a-btn" data-idx="${idx!0}" type="button" value="수정"/>
	<span>&nbsp;/&nbsp;</span>
	<a href="/boardDetail?boardNum=${boardNum!0}">취소</a>
</div>
<form id="commentUpdateForm${idx}" action="/updateComment" method="post">
	<input type="hidden" name="boardNum" value="${boardNum!0}">
	<input type="hidden" name="idx" value="${idx!0}">
	<br/>
	<input type="text" class="updateComment form-control" name="comment" value="${comment!""}" data-idx="${idx!0}" autofocus/>
	<DIV style="float:right">남은 글자수: <SPAN id="updateCommentLengthCount${idx}" data-maxCount="200">${200 - comment?length}</SPAN></DIV>
</form>
<script type="text/javascript" src="js/board/comment/commentUpdate.js"></script>