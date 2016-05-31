<!DOCTYPE html>
<html>
	<head>
		<script>
			<#assign message = Request.message!"">
			<#if message != "">
				alert("${Request.message}");
			</#if>
		</script>
		<title>회원페이지</title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
		<style type="text/css">
			.container {
				width : 30%;
				height : 50%;
				padding-top : 5%;
			}
		</style>
	</head>
	<body>
		<div class="container">
			<h1> 회원페이지 </h1>
			<form id="form" class="form-signin" action="/" method="post">
				<table class="table table-hover">
					<colgroup>
						<col width="30%"/>
						<col width="40%"/>
					</colgroup>
					<tr>
						<th>
							<label for="id">아이디 : </label>
						</th>
						<td>
							${Request.id}
						</td>
					</tr>
					<tr>
						<th>
							<label for="password">이메일 : </label>
						</th>
						<td>
							${Request.email}
						</td>
					</tr>
					<tr>
						<th>
							<label for="password">이름 : </label>
						</th>
						<td>
							${Request.name}
						</td>
					</tr>
					<tr>
						<th>
							<label for="password">비밀번호 : </label>
						</th>
						<td>
							<button data-target="#layerpop" data-toggle="modal" type="button" class="btn btn-default">비밀번호 수정</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
		<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/user/login.js"></script>
	</body>
</html>