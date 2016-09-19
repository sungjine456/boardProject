<!DOCTYPE html>
<html>
	<head>
		<title>email access</title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
		<style type="text/css" media="screen">
			body {
				background-color: #f1f1f1;
				margin: 0;
			}
			.container { margin: 50px auto 40px auto; width: 600px; text-align: center;}
			p { color: rgba(0, 0, 0, 0.5); margin: 20px 0; line-height: 1.6; }
		</style>
	</head>
	<body>
		<div class="container">
			<p><strong>이메일 인증이 필요합니다.</strong></p>
			<form id="form" action="/emailAccessRe" method="post">
				<input type="hidden" name="email" value="${email!""}"/>
				<button type="button" id="accessBtn" class="btn btn-primary">재전송</button>
			</form>
		</div>
	</body>
	<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
	<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
	<script type="text/javascript" src="js/user/emailAccessAgo.js"></script>
</html>
