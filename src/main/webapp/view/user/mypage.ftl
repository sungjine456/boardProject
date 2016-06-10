<!DOCTYPE html>
<html>
	<head>
		<script>
			<#assign message = message!"">
			<#if message != "">
				alert("${message}");
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
							<label>아이디 : </label>
						</th>
						<td>
							${id}
						</td>
					</tr>
					<tr>
						<th>
							<label>이메일 : </label>
						</th>
						<td>
							${email}
						</td>
					</tr>
					<tr>
						<th>
							<label>이름 : </label>
						</th>
						<td>
							${name}
						</td>
					</tr>
					<tr>
						<th>
							<label>비밀번호 : </label>
						</th>
						<td>
							<button data-target="#change" data-toggle="modal" type="button" class="btn btn-primary">비밀번호 수정</button>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<button data-target="#leave" data-toggle="modal" class="btn btn-danger" type="button" style="float:right">회원탈퇴</button>
						</td>
					</tr>
				</table>
			</form>
			<div class="modal fade" id="change">
				<div class="modal-dialog modal-sm">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">×</button>
							<h4 class="modal-title">비밀번호 수정</h4>
						</div>
	 					<div class="modal-body">
	 						<form id="changeForm" class="form-signin" action="/changePassword" method="post">
	 							<table style="width:98%">
	 								<colgroup>
										<col width="50%"/>
										<col width="50%"/>
									</colgroup>
		 							<tr>
		 								<th>
											<label for="password">이전 비밀번호 : </label>
										</th>
										<td>
											<input type="password" id="password" class="form-control" name="password" maxlength="15"/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="changePassword">비밀번호 수정 : </label>
										</th>
										<td>
											<input type="password" id="changePassword" class="form-control" name="changePassword" maxlength="15"/>
										</td>
									</tr>
									<tr>
										<th>
											<label for="changePasswordConfirm">비밀번호 수정 확인 : </label>
										</th>
										<td>
											<input type="password" id="changePasswordConfirm" class="form-control" maxlength="15"/>
										</td>
									</tr>
									<tr>
										<td colspan="2">
											<button id="passwordChangeBtn" type="button" class="btn btn-primary" style="float:right">비밀번호 수정</button>
										<td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
			</div>
			<div class="modal fade" id="leave">
				<div class="modal-dialog modal-sm">
					<div class="modal-content">
						 <div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">×</button>
							<h4 class="modal-title">회원탈퇴</h4>
						</div>
	 					<div class="modal-body">
	 						<form id="leaveForm" class="form-signin" action="/leave" method="post">
	 							<table>
	 								<tr>
										<th>
											<label>비밀번호 : </label> 
										</th>
										<td>
											<input id="leavePassword" class="form-control" name="password" type="password"/>
										</td>
									</tr>
									<tr>
										<td colspan="2"> 
											<button id="leaveBtn" type="button" class="btn btn-danger" style="float:right">회원탈퇴</button>
										</td>
									</tr>
								</table>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
		<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/user/mypage.js"></script>
	</body>
</html>