<div class="container" style="height:100%;">
	<h1>유저 목록</h1>
	<h3><a href="/admin/boards">글 목록</a></h3>
	<BR>
	<form id="translatePasswordForm" action="/admin/translatePassword" method="post">
		<input type="hidden" id="translatePasswordEmail" name="email"/>
	</form>
	<form id="emailAccessReForm" action="/admin/emailAccessRe" method="post">
		<input type="hidden" id="emailAccessReEmail" name="email"/>
	</form>
	<table class="table" style="margin-top:10px;">
		<colgroup>
			<col width="8%"/>
			<col width="8%"/>
			<col width="30%"/>
			<col width="15%"/>
			<col width="15%"/>
			<col width="8%"/>
			<col width="8%"/>
			<col width="8%"/>
		</colgroup>
		<thead>
			<tr>
				<th class="text-center">
					<a href="?sort=id">아이디</a>
				</th>
				<th class="text-center">
					<a href="?sort=name">이름</a>
				</th>
				<th class="text-center">
					<a href="?sort=email">이메일</a>
				</th>
				<th class="text-center">
					<a href="?sort=regDate">가입일</a>
				</th>
				<th class="text-center">
					<a href="?sort=updateDate">수정일</a>
				</th>
				<th class="text-center">
					<a href="?sort=access">동의 여부</a>
				</th>
				<th class="text-center">
					비밀번호
				</th>
				<th class="text-center">
					동의 여부 재전송
				</th>
			</tr>
		</thead>
		<tbody>
			<#list users.content as user>
			<tr>
				<td class="text-center">
					${user.id}
				</td>
				<td class="text-center">
					${user.name}
				</td>
				<td class="text-center">
					${user.email}
				</td>
				<td class="text-center">
					${user.regDate}
				</td>
				<td class="text-center">
					${user.updateDate}
				</td>
				<td class="text-center">
					${user.access}
				</td>
				<td class="text-center">
					<button type="button" class="btn btn-default btn-xs translatePasswordBtn" data-email="${user.email}">재 발급</button>
				</td>
				<td class="text-center">
					<button type="button" class="btn btn-default btn-xs emailAccessReBtn" data-email="${user.email}">재 전송</button>
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
<script type="text/javascript" src="/js/admin/usersView.js"></script>