<!DOCTYPE html>
<html>
	<head>
		<script>
			<#assign message = Request.message!"">
			<#if message != "">
				alert("${Request.message}");
			</#if>
		</script>
		<title>로그인</title>
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
			<h1> 로그인 </h1>
			<form id="form" class="form-signin" action="/" method="post">
				<table class="table table-hover">
					<colgroup>
						<col width="30%"/>
						<col width="70%"/>
					</colgroup>
					<tr>
						<th>
							<label for="id">아이디 : </label>
						</th>
						<td>
							<input type="text" id="id" class="form-control" name="id"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="password">비밀번호 : </label>
						</th>
						<td>
							<input type="password" id="password" class="form-control" name="password" maxlength="15"/>
						</td>
					</tr>
					<tr>
						<td>
							<button type="button" id="joinBtn" class="btn btn-primary">회원가입</button>
						</td>
						<td style="padding-left: 50%">
							<button type="submit" id="loginBtn" class="btn btn-primary">로그인</button>
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