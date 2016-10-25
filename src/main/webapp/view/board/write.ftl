<link rel="stylesheet" href="css/common/editer.css">
<div class="container">
	<h1>글쓰기</h1>
	<form id="writeForm" action="/boardWrite" method="post" enctype="multipart/form-data">
		<textarea type="text" id="content" name="content" style="display:none"></textarea>
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
					<input id="editFontBold" class="edit-btn" type="button" style="font-weight:bold;" value="B"/>
					<input id="editFontUnderLine" class="edit-btn" type="button" style="text-decoration:underline" value="U"/>
					<input id="editFontItalic" class="edit-btn" type="button" style="font-style: italic;" value="I"/>
					<button id="editLeftSort" class="edit-btn" type="button"><img src="image/editer/left.png" style="padding-bottom: 2px;"/></button>
					<button id="editCenterSort" class="edit-btn" type="button"><img src="image/editer/center.png" style="padding-bottom: 2px;"/></button>
					<button id="editRightSort" class="edit-btn" type="button"><img src="image/editer/right.png" style="padding-bottom: 2px;"/></button>
					<input id="editImage" name="editImage" class="edit-img" type="file"/>
					<button class="edit-btn" type="button"><img src="image/editer/image.png" style="padding-bottom: 2px;"/></button>
					<iframe id="editFrame" name="frame" width="100%" height="400px"></iframe>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="padding-left: 90%">
					<button type="button" id="writeBtn" class="btn btn-primary active">글쓰기</button>
				</td>
			</tr>
		</table>
	</form>
</div>
<script type="text/javascript" src="/js/board/write.js"></script>
<script type="text/javascript" src="/js/common/editer.js"></script>