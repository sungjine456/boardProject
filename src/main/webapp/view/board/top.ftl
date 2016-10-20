<style>
	.white{
		color: white;
	}
</style>
<div>
	<font color="white"><a href="/board">Person Board Project</a></font>
	<div style="float:right;">
		<#if user.admin == "Y">
			<a href="/adminView" class="white">관리자 페이지</a>
		</#if>
		<a href="/mypage" class="white"><img src="${user.img}" width="40" height="40"/>${user.name}</a><span class="white">님 환영합니다.</span>
		<a href="/logout" class="white">로그아웃</a>
	</div>
</div>
