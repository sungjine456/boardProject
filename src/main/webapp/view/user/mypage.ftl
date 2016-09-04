<style type="text/css">
	.container {
		width : 30%;
		height : 50%;
		padding-top : 5%;
	}
</style>
<div class="container">
	<h1> 회원 페이지 </h1>
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
		</tr>
			<td>
				<label>이미지 : </label>
			</td>
			<td>
				<img src="${img}" width="200" height="250"/>
			</td>
		<tr>
			<td>
				<button data-target="#update" data-toggle="modal" type="button" class="btn btn-primary">회원 수정</button>
			</td>
			<td>
				<button data-target="#leave" data-toggle="modal" type="button" class="btn btn-danger" style="float:right">회원탈퇴</button>
			</td>
		</tr>
	</table>
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
	<div class="modal fade" id="update">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				 <div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">×</button>
					<h4 class="modal-title">회원수정</h4>
				</div>
				<div class="modal-body">
					<form id="updateForm" class="form-signin" action="/updateView" method="post">
						<table>
							<tr>
								<th>
									<label>비밀번호 : </label> 
								</th>
								<td>
									<input id="updatePassword" class="form-control" name="password" type="password"/>
								</td>
							</tr>
							<tr>
								<td colspan="2"> 
									<button id="updateBtn" type="button" class="btn btn-primary" style="float:right">비밀번호 확인</button>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="js/user/mypage.js"></script>
