<!DOCTYPE html>
<html>
	<head>
		<title> 회원가입 </title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
	</head>
	<body>
		<form id="form" action="/" method="post">
			<table style="border:0px">
				<tr>
					<td>
						<label>아이디 : </label>
					</td>
					<td>
						<input type="text" id="u_i" name="user_id"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>비밀번호 : </label>
					</td>
					<td>
						<input type="text" id="p_w" name="password"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>비밀번호 확인 : </label>
					</td>
					<td>
						<input type="text" id="p_w_confirm"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>이름 : </label>
					</td>
					<td>
						<input type="text" id="n_a" name="name"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>이메일 : </label>
					</td>
					<td>
						<input type="text" id="e_m" name="email"/>
					</td>
				</tr>
				<tr>
					<td>
						<button type="submit">회원가입</button>
					</td>
				</tr>
			</table>
		</form>
	</body>
	<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
	<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/user/join.js"></script>
</html>