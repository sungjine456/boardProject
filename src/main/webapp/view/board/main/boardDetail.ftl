<div class="container">
	<h1>제목</h1>
	<form id="form" action="/boardWrite" method="post">
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
					${board.title}
				</td>
			</tr>
			<tr>
				<th>
					<label for="content">내용 : </label>
				</th>
				<td>
					${board.content}
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left: 90%">
					<button type="button" id="write" class="btn btn-primary">뎃글</button>
				</td>
			</tr>
		</table>
	</form>
</div>