<div class="container" style="height:100%;">
	<h1> 게시판  </h1>
	<table class="table" style="margin-top:10px;">
		<colgroup>
			<col width="10%"/>
			<col width="61%"/>
			<col width="8%"/>
			<col width="6%"/>
			<col width="15%"/>
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
					조회수
				</th>
				<th>
					작성일
				</th>
			</tr>
		</thead>
		<tbody>
		<#list boardList.content as board>
			<tr>
				<td>
					${board.idx}
				</td>
				<td>
					<a href="/boardDetail?num=${board.idx}">${board.title}</a>
				</td>
				<td>
					${board.user.name}
				</td>
				<td>
					${board.hitCount}
				</td>
				<td>
					${board.regDate.toString("yyyy.MM.dd HH:mm")}
				</td>
			</tr>
		<#else>
			<tr>
				<td colspan="5">
					등록된 글이 없습니다.
				</td>
			</tr>
		</#list>
			<tr>
				<td colspan="5">
					<button type="button" id="write" class="btn btn-primary" style="margin-left:80%;">글쓰기</button>
				</td>
			</tr>
			<tr>
				<td colspan="5">
					<div style="text-align:center;">
		<#if startNum &gt; 2>
			<a class="btn btn-default btn-sm" href="?num=${startNum-1}">이전</a>
		</#if>
		<#list startNum..lastNum as i>
			<a class="btn btn-default btn-sm" href="?num=${i}">${i}</a>
		</#list>
		<#if lastNum != lastPage>
			<a class="btn btn-default btn-sm" href="?num=${lastNum+1}">다음</a>
		</#if>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<script type="text/javascript">
</script>
<script type="text/javascript" src="js/board/main.js"></script>
