<!DOCTYPE html>
<html>
	<head>
		<title> ȸ������ </title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
	</head>
	<body>
		<form id="form" action="/" method="post">
			<table style="border:0px">
				<tr>
					<td>
						<label>���̵� : </label>
					</td>
					<td>
						<input type="text" id="u_i" name="user_id"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>��й�ȣ : </label>
					</td>
					<td>
						<input type="text" id="p_w" name="password"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>��й�ȣ Ȯ�� : </label>
					</td>
					<td>
						<input type="text" id="p_w_confirm"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>�̸� : </label>
					</td>
					<td>
						<input type="text" id="n_a" name="name"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>�̸��� : </label>
					</td>
					<td>
						<input type="text" id="e_m" name="email"/>
					</td>
				</tr>
				<tr>
					<td>
						<button type="submit">ȸ������</button>
					</td>
				</tr>
			</table>
		</form>
	</body>
	<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
	<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/user/join.js"></script>
</html>