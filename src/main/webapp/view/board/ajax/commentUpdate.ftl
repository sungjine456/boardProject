<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
<form id="commentUpdateForm" action="/updateComment" method="post">
	<input type="hidden" name="upnum" value="${num!0}">
	<input type="hidden" name="upidx" value="${idx!0}">
	<a id="commentUpdateBtn" style="float:right; padding-right: 5%;">수정</a>
	<br/>
	<input type="text" id="updateComment" class="form-control" name="comment" value="${comment!""}"/>
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