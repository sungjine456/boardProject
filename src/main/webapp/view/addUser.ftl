<!DOCTYPE html>
<html>
	<head>
		<title> ȸ������ </title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
	</head>
	<body>
		<div class="container">
			<h1> ȸ�� ���� </h1>
			<form action="/" method="post">
				<table class="table table-hover">
					<colgroup>
						<col width="10%"/>
						<col width="90%"/>
					</colgroup>
					<tr>
						<th>
							<label for="u_i">���̵� : </label>
						</th>
						<td>
							<input type="text" id="id" class="form-control" name="id"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="p_w">��й�ȣ : </label>
						</th>
						<td>
							<input type="password" id="password" class="form-control" name="password"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="p_w_confirm">��й�ȣ Ȯ�� : </label>
						</th>
						<td>
							<input type="password" id="passwordConfirm" class="form-control"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="n_a">�̸� : </label>
						</th>
						<td>
							<input type="text" id="name" class="form-control" name="name"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="e_m">�̸��� : </label>
						</th>
						<td>
							<input type="text" id="email" class="form-control" name="email"/>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="padding-left: 80%">
							<button type="submit" id="join_btn" class="btn btn-primary">ȸ������</button>
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