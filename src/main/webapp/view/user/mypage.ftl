<head>
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
					<td>
						<button type="button" id="updateBtn" class="btn btn-primary">회원 수정</button>
					</td>
					<td>
						<button data-target="#leave" data-toggle="modal" class="btn btn-danger" type="button" style="float:right">회원탈퇴</button>
					</td>
				</tr>
			</table>
		</form>
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