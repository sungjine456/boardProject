<div class="container" style="height:100%;">
	<h1>글 목록</h1>
	<h3><a href="/admin/users">유저 목록</a></h3>
	<BR>
	<table class="table" style="margin-top:10px;">
		<colgroup>
			<col width="10%"/>
			<col width="40%"/>
			<col width="10%"/>
			<col width="10%"/>
			<col width="15%"/>
			<col width="15%"/>
		</colgroup>
		<thead>
			<tr>
				<th class="text-center">
					<a href="?sort=idx">글번호</a>
				</th>
				<th class="text-center">
					<a href="?sort=title">제목</a>
				</th>
				<th class="text-center">
					<a href="?sort=user">작성자</a>
				</th>
				<th class="text-center">
					<a href="?sort=hitCount">조회수</a>
				</th>
				<th class="text-center">
					<a href="?sort=regDate">가입일</a>
				</th>
				<th class="text-center">
					<a href="?sort=updateDate">수정일</a>
				</th>
			</tr>
		</thead>
		<tbody>
			<#list boards.content as board>
			<tr>
				<td class="text-center">
					${board.idx}
				</td>
				<td class="text-center">
					${board.title}
				</td>
				<td class="text-center">
					${board.user.name}
				</td>
				<td class="text-center">
					${board.hitCount}
				</td>
				<td class="text-center">
					${board.regDate.toString("yyyy.MM.dd HH:mm")}
				</td>
				<td class="text-center">
					${board.updateDate.toString("yyyy.MM.dd HH:mm")}
				</td>
			</tr>
			</#list>
			<tr>
				<td colspan="8">
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
<script type="text/javascript" src="/js/admin/boardsView.js"></script>