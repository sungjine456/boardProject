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
				<th class="text-center">
					글 번호
				</th>
				<th>
					제목
				</th>
				<th class="text-center">
					작성자
				</th>
				<th class="text-center">
					조회수
				</th>
				<th class="text-center">
					작성일
				</th>
			</tr>
		</thead>
		<tbody>
		<#list boardList.content as board>
			<tr>
				<td class="text-center">
					${board.idx}
				</td>
				<td>
					<a href="/boardDetail?boardNum=${board.idx}">${board.title}</a>
				</td>
				<td class="text-center">
					${board.user.name}
				</td>
				<td class="text-center">
					${board.hitCount}
				</td>
				<td class="text-center">
					${board.regDate}
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
		<#if startPage &gt; 2>
			<a class="btn btn-default btn-sm" href="?pageNum=${startPage-1}">이전</a>
		</#if>
		<#list startPage..lastPage as i>
			<a class="btn btn-default btn-sm" href="?pageNum=${i}">${i}</a>
		</#list>
		<#if lastPage &lt; maxPage>
			<a class="btn btn-default btn-sm" href="?pageNum=${lastPage+1}">다음</a>
		</#if>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
<script type="text/javascript" src="/js/board/main.js"></script>
