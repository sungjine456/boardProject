<div class="container" style="height:100%;">
	<h1> 게시판  </h1>
	<table class="table" style="margin-top:10px;">
		<colgroup>
			<col width="10%"/>
			<col width="70%"/>
			<col width="10%"/>
			<col width="10%"/>
		</colgroup>
		<thead>
			<tr>
				<th>
					글 번호
				</th>
				<th>
					제목
				</th>
				<th>
					작성자
				</th>
				<th>
					작성일
				</th>
			</tr>
		</thead>
		<tbody>
		<#list boardList as board>
			<tr>
				<td>
					${board_index + 1}
				</td>
				<td>
					${board.title}
				</td>
				<td>
					${board.content}
				</td>
				<td>
					${board.user.name}
				</td>
			</tr>
		<#else>
			<tr>
				<td colspan="4">
					등록된 글이 없습니다.
				</td>
			</tr>
		</#list>
			<tr>
				<td colspan="4">
					<button type="button" id="write" class="btn btn-primary" style="margin-left:80%;">글쓰기</button>
				</td>
			</tr>
		</tbody>
	</table>
</div>