<div class="container">
	<input type="hidden" id="num" name="num" value="${num}"/>
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
	<br><br>
	<form id="commentForm" action="/boardWriteComment" method="post">
		<table class="table" style="background-color: #f4f4f4;">
			<colgroup>
				<col width="10%"/>
				<col width="90%"/>
			</colgroup>
<#list comments as comment>
			<tr>
				<td colspan="2">
					<div>
						<span style="font-weight:bold; font-size:20px">${comment.writer.name}</span> <span style="font-size:2px">${comment.regDate?string["yyyy.MM.dd HH:mm"]}</span><br>
						<div style="padding-left:10px;">
							${comment.comment}
						</div>
					</div>
				</td>
			</tr>
<#else>
			<tr>
				<td colspan="2">
					뎃글을 입력해주세요.
				</td>
			</tr>
</#list>
			<tr>
				<td style="padding-left: 5%; width: 90%">
					<input type="text" id="comment" class="form-control" name="comment"/>
				</td>
				<td>
					<button type="button" id="commentBtn" class="btn btn-primary">뎃글</button>
				</td>
			</tr>
		</table>
	</form>
</div>
