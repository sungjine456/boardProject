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
		<button type="button" id="updateBtn" class="btn btn-primary" style="float:right">내용 수정</button>
	</table>
	<br><br>
	<form id="commentForm" action="/boardWrite" method="post">
		<table class="table" style="background-color: #f4f4f4;">
			<colgroup>
				<col width="10%"/>
				<col width="90%"/>
			</colgroup>
			<tr>
				<td style="padding-left: 5%; width: 90%">
					<input type="text" class="form-control"/>
				</td>
				<td>
					<button type="button" id="comment" class="btn btn-primary">뎃글</button>
				</td>
			</tr>
		</table>
	</form>
</div>
