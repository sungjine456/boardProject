<form id="commentReplyForm${commentIdx!0}" action="/writeReply" method="post">
	<input type="hidden" name="boardNum" value="${boardNum!0}">
	<input type="hidden" name="idx" value="${commentIdx!0}">
	<br/>
	<table style="background-color: #f4f4f4; width:98%;">
		<tr>
			<td style="padding-left: 7%; width: 90%; padding-right: 1%;">
				<input type="text" class="replyComment form-control" name="comment" idx="${commentIdx!0}"/>
				<DIV style="float:right">남은 글자수: <SPAN id="replyCommentLengthCount${commentIdx!0}" maxCount="200">200</SPAN></DIV>
			</td>
			<td>
				<a class="commentReplyBtn" idx="${commentIdx!0}">답글</a>
				<a href="/boardDetail?boardNum=${boardNum!0}">취소</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript" src="js/board/comment/commentReply.js"></script>