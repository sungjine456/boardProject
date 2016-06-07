<!DOCTYPE html>
<html>
	<head>
		<script>
			<#assign message = message!""> 
			<#if message != "">
				alert("${message}");
			</#if>
		</script>
		<title> 회원가입 </title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
	</head>
	<body>
		<div class="container">
			<h1> 회원 가입 </h1>
			<form id="form" action="/join" method="post">
				<table class="table table-hover">
					<colgroup>
						<col width="10%"/>
						<col width="70%"/>
						<col width="20%"/>
					</colgroup>
					<tr>
						<th>
							<label for="id">아이디 : </label>
						</th>
						<td>
							<input type="text" id="id" class="form-control" name="id"/>
						</td>
						<td>
							<span id="idSpan"></span>
						</td>
					</tr>
					<tr>
						<th>
							<label for="password">비밀번호 : </label>
						</th>
						<td>
							<input type="password" id="password" class="form-control" name="password" maxlength="15"/>
						</td>
						<td>
							<font>6자 이상 15자 이하로 입력하셔야 합니다.</font>
						</td>
					</tr>
					<tr>
						<th>
							<label for="passwordConfirm">비밀번호 확인 : </label>
						</th>
						<td>
							<input type="password" id="passwordConfirm" class="form-control" maxlength="15"/>
						</td>
						<td>
							<span id="passwordSpan"></span>
						</td>
					</tr>
					<tr>
						<th>
							<label for="name">이름 : </label>
						</th>
						<td>
							<input type="text" id="name" class="form-control" name="name"/>
						</td>
						<td>
						</td>
					</tr>
					<tr>
						<th>
							<label for="email">이메일 : </label>
						</th>
						<td>
							<input type="text" id="email" class="form-control" name="email"/>
						</td>
						<td>
							<span id="emailSpan"></span>
						</td>
					</tr>
					<tr>
						<td colspan="3" style="padding-left: 80%">
							<button type="button" id="joinBtn" class="btn btn-primary">회원가입</button>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
		<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/user/join.js"></script>
	</body>
</html>