<div style="float:right; padding-right: 5%;">
	<a href="/boardDetail?num=${num!0}">취소</a>
</div>
<form id="commentReplyForm" action="/writeReply" method="post">
	<input type="hidden" name="bnum" value="${num!0}">
	<input type="hidden" name="idx" value="${idx!0}">
	<br/>
	<table style="background-color: #f4f4f4; width:98%;">
		<tr>
			<td style="padding-left: 7%; width: 90%; padding-right: 1%;">
				<input type="text" id="reply" class="form-control" name="comment"/>
			</td>
			<td>
				<a id="commentReplyBtn" class="btn btn-primary">댓글</a>
			</td>
		</tr>
	</table>
</form>
<script type="text/javascript">
	var form = $("#commentReplyForm");
	var btn = $("#commentReplyBtn");
	var reply = $("#reply");
	btn.click(function(){
		if(reply.val() == ""){
			alert("뎃글을 입력해주세요.");
			return;
		}
		form.submit();
	});
</script>