<div style="float:right; padding-right: 5%;">
	<a id="commentUpdateBtn">수정</a>
	<span>&nbsp;/&nbsp;</span>
	<a href="/boardDetail?num=${num!0}">취소</a>
</div>
<form id="commentUpdateForm" action="/updateComment" method="post">
	<input type="hidden" name="upnum" value="${num!0}">
	<input type="hidden" name="upidx" value="${idx!0}">
	<br/>
	<input type="text" id="updateComment" class="form-control" name="comment" value="${comment!""}" autofocus/>
</form>
<script type="text/javascript">
	var form = $("#commentUpdateForm");
	var btn = $("#commentUpdateBtn");
	var comment = $("#updateComment");
	btn.click(function(){
		if(comment.val() == ""){
			alert("뎃글을 입력해주세요.");
			return;
		}
		form.submit();
	});
</script>