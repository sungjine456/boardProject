<!DOCTYPE html>
<html>
	<head>
		<title> 회원가입 </title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
	</head>
	<body>
		<div class="container">
			<h1> 회원 가입 </h1>
			<form action="/" method="post">
				<table class="table table-hover">
					<colgroup>
						<col width="10%"/>
						<col width="90%"/>
					</colgroup>
					<tr>
						<th>
							<label for="u_i">아이디 : </label>
						</th>
						<td>
							<input type="text" id="id" class="form-control" name="id"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="p_w">비밀번호 : </label>
						</th>
						<td>
							<input type="password" id="password" class="form-control" name="password"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="p_w_confirm">비밀번호 확인 : </label>
						</th>
						<td>
							<input type="password" id="passwordConfirm" class="form-control"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="n_a">이름 : </label>
						</th>
						<td>
							<input type="text" id="name" class="form-control" name="name"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="e_m">이메일 : </label>
						</th>
						<td>
							<input type="text" id="email" class="form-control" name="email"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="padding-left: 80%">
							<button type="submit" id="join_btn" class="btn btn-primary">회원가입</button>
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