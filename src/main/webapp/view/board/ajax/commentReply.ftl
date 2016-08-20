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
				<input type="text" class="replyComment form-control" name="comment" idx="${idx}"/>
				<DIV style="float:right">남은 글자수: <SPAN id="replyCommentLengthCount${idx!0}" maxCount="200">200</SPAN></DIV>
			</td>
			<td>
				<a class="commentReplyBtn" class="btn btn-primary" idx="${idx!0}">댓글</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript" src="js/board/comment/commentReply.js"></script>