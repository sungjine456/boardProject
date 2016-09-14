<div class="container">
	<span style="font-size:30px;">${board.title}</span>
	<#if board.user.idx == user.idx>
		<span style="font-size:10px; padding-right:10px;">${like} (${likeCount})</span>
		<div style="float:right">
			<button type="button" id="boardUpdateBtn" class="btn btn-primary">내용 수정</button>
		</div>
	<#else>
		<span id="likeSpan">
			<a id="likeCount" style="font-size:10px; padding-right:10px;" data-useridx="${idx}">${like} (${likeCount})</a>
		</span>
	</#if>
	<table class="table table-hover">
		<colgroup>
			<col width="10%"/>
			<col width="90%"/>
		</colgroup>
		<tr>
			<th>
				<label style="font-size:25px;">내용 : </label>
			</th>
			<td>
				<span style="font-size:25px;">${board.content}</span>
			</td>
		</tr>
	</table>
	<hr style="border:1px dashed #ddd"><br>
	<form id="commentForm" action="/writeComment" method="post">
		<input type="hidden" id="boardNum" name="boardNum" value="${board.idx}"/>
		<table class="table" style="background-color: #f4f4f4;">
			<colgroup>
				<col width="10%"/>
				<col width="90%"/>
			</colgroup>
			<tr>
				<td style="padding-left: 5%; width: 90%">
					<input type="text" id="writeComment" class="form-control" name="comment"/>
					<DIV style="float:right">남은 글자수: <SPAN id="commentLengthCount" data-maxCount="200">200</SPAN></DIV>
				</td>
				<td>
					<button type="button" id="commentBtn" class="btn btn-primary">댓글</button>
				</td>
			</tr>
<#list comments.content as comment>
			<tr>
				<td colspan="2">
					<div style="padding: 0 3% 0 5%;">
<#if comment.depth &gt; 0>
	<#list 1..comment.depth as i>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<#if i == comment.depth>
			└ RE :
		</#if>
	</#list>
</#if>
						<span style="font-weight:bold; font-size:20px">${comment.writer.name}</span> <span style="font-size:2px">${comment.regDate.toString("yyyy.MM.dd HH:mm")}</span>
						<span id="commentSpan${comment.idx}" style="padding-left:10px;">
							<div style="float:right; padding-right: 3%;">
			<#if comment.writer.idx == user.idx>
								<input class="commentUpdateBtn a-btn" type="button" value="수정" data-comment="${comment.comment}" data-idx="${comment.idx}"/>
								<span>&nbsp;/&nbsp;</span>
			</#if>
								<input class="commentReplyBtn a-btn" data-idx="${comment.idx}" type="button" value="답글">
							</div>
							<br>
							<div>
						<#if comment.depth != 0>
							<#list 1..comment.depth as i>
								&nbsp;&nbsp;&nbsp;&nbsp;
							</#list>
						</#if>
								${comment.comment}
							</div>
						</span>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="height:0px; padding: 0px;">
					<div style="padding: 0 3% 0 5%;">
						<span id="replySpan${comment.idx}">
					</div>
				</td>
			</tr>
<#else>
			<tr>
				<td colspan="2">
					<div style="padding-left:5%;">
						댓글을 입력해주세요.
					</div>
				</td>
			</tr>
</#list>
			<tr>
				<td colspan="5">
					<div style="text-align:center;">
		<#if startPage &gt; 2>
			<a class="btn btn-default btn-sm" href="?boardNum=${board.idx}&pageNum=${startPage-1}">이전</a>
		</#if>
		<#list startPage..lastPage as i>
			<a class="btn btn-default btn-sm" href="?boardNum=${board.idx}&pageNum=${i}">${i}</a>
		</#list>
		<#if lastPage &lt; maxPage>
			<a class="btn btn-default btn-sm" href="?boardNum=${board.idx}&pageNum=${lastPage+1}">다음</a>
		</#if>
					</div>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="js/board/detail.js"></script>
