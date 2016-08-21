<div class="container">
	<h1>글쓰기</h1>
	<form id="writeForm" action="/boardWrite" method="post">
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
					<input type="text" id="title" class="form-control" name="title" autofocus/>
				</td>
			</tr>
			<tr>
				<th>
					<label for="content">내용 : </label>
				</th>
				<td>
					<input id="editFontBold" type="button" value="B"/>
					<iframe id="editFrame" width="100%" height="400px"></iframe>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left: 90%">
					<button type="button" id="writeBtn" class="btn btn-primary">글쓰기</button>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="js/board/write.js"></script>
<script type="text/javascript" src="js/common/editer.js"></script>