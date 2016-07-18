<div class="container">
	<h1>${board.title}</h1>
	<table class="table table-hover">
		<colgroup>
			<col width="10%"/>
			<col width="90%"/>
		</colgroup>
		<tr>
			<th>
				<label for="content">내용 : </label>
			</th>
			<td>
				${board.content}
			</td>
		</tr>
	<#if board.user.idx == idx>
		<button type="button" id="boardUpdateBtn" class="btn btn-primary" style="float:right">내용 수정</button>
	</#if>
	</table>
	<hr style="border:1px dashed #ddd"><br>
	<form id="commentForm" action="/writeComment" method="post">
		<input type="hidden" id="num" name="num" value="${num}"/>
		<table class="table" style="background-color: #f4f4f4;">
			<colgroup>
				<col width="10%"/>
				<col width="90%"/>
			</colgroup>
			<tr>
				<td style="padding-left: 5%; width: 90%">
					<input type="text" id="writeComment" class="form-control" name="comment"/>
				</td>
				<td>
					<button type="button" id="commentBtn" class="btn btn-primary">댓글</button>
				</td>
			</tr>
<#list comments as comment>
			<tr>
				<td colspan="2">
					<input type="hidden" name="commentIdx" value="${comment.idx}"/>
					<div style="padding-left:5%;">
<#if comment.circle != 0>
	<#list 1..comment.depth as i>
		&nbsp;&nbsp;
		<#if i == comment.depth>
			└ RE :
		</#if>
	</#list>
</#if>
						<span style="font-weight:bold; font-size:20px">${comment.writer.name}</span> <span style="font-size:2px">${comment.regDate.toString("yyyy.MM.dd HH:mm")}</span>
						<span id="commentSpan${comment.idx}" style="padding-left:10px;">
						<div style="float:right; padding-right: 5%;">
		<#if comment.writer.idx == idx>
							<a class="commentUpdateBtn" value="${comment.comment}" idx="${comment.idx}">수정</a>
							<span>&nbsp;/&nbsp;</span>
		</#if>
							<a class="commentReplyBtn" idx="${comment.idx}">답글</a>
						</div>
							<br>
							${comment.comment}
						</span>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<span id="replySpan${comment.idx}">
				</td>
			</tr>
<#else>
			<tr>
				<td colspan="2">
					<div style="padding-left:5%;">
						뎃글을 입력해주세요.
					</div>
				</td>
			</tr>
</#list>
		</table>
	</form>
</div>
<script type="text/javascript" src="js/board/detail.js"></script>
