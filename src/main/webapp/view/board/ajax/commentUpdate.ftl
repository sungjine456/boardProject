<div style="float:right; padding-right: 5%;">
	<a class="commentUpdateBtn" idx="${idx!0}">수정</a>
	<span>&nbsp;/&nbsp;</span>
	<a href="/boardDetail?num=${num!0}">취소</a>
</div>
<form id="commentUpdateForm${idx}" action="/updateComment" method="post">
	<input type="hidden" name="num" value="${num!0}">
	<input type="hidden" name="idx" value="${idx!0}">
	<br/>
	<input type="text" class="updateComment form-control" name="comment" value="${comment!""}" idx="${idx!0}" autofocus/>
	<DIV style="float:right">남은 글자수: <SPAN id="updateCommentLengthCount${idx}" maxCount="200">${200 - comment?length}</SPAN></DIV>
</form>
<script type="text/javascript" src="js/board/comment/commentUpdate.js"></script>