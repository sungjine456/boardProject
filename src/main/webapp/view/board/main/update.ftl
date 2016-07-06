<div class="container">
	<h1>글 수정</h1>
	<form id="updateForm" action="/boardUpdate" method="post">
		<input type="hidden" name="num" value="${num}"/>
		<table class="table table-hover">
			<colgroup>
				<col width="10%"/>
				<col width="90%"/>
			</colgroup>
			<tr>
				<th>
					<label for="title">제목 : </label>
				</th>
				<td>
					<input type="text" id="title" class="form-control" name="title" value="${board.title}" autofocus/>
				</td>
			</tr>
			<tr>
				<th>
					<label for="content">내용 : </label>
				</th>
				<td>
					<textarea id="content" class="form-control" name="content" rows="3">${board.content}</textarea>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left: 90%">
					<button type="button" id="updateBtn" class="btn btn-primary">글 수정</button>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="js/board/update.js"></script>
