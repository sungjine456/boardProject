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
			<form id="form" action="/" method="post">
				<table class="table table-hover">
					<colgroup>
						<col width="10%"/>
						<col width="90%"/>
					</colgroup>
					<tr>
						<td>
							<label for="u_i">아이디 : </label>
						</td>
						<td>
							<input type="text" id="u_i" class="form-control" name="user_id"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="p_w">비밀번호 : </label>
						</td>
						<td>
							<input type="password" id="p_w" class="form-control" name="password"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="p_w_confirm">비밀번호 확인 : </label>
						</td>
						<td>
							<input type="password" id="p_w_confirm" class="form-control"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="n_a">이름 : </label>
						</td>
						<td>
							<input type="text" id="n_a" class="form-control" name="name"/>
						</td>
					</tr>
					<tr>
						<td>
							<label for="e_m">이메일 : </label>
						</td>
						<td>
							<input type="text" id="e_m" class="form-control" name="email"/>
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
	</body>
	<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
	<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/user/join.js"></script>
</html>