var update = {
	email : $("#form #email"),
	name : $("#form #name"),
	file : $("#ufile"),
	preview : $("#preview"),
	password : $("#changeForm #password"),
	changePassword : $("#changeForm #changePassword"),
	changePasswordConfirm : $("#changeForm #changePasswordConfirm"),
	passwordChangeBtn : $("#changeForm #passwordChangeBtn"),
	updateBtn : $("#form #updateBtn"),
	updateForm : $("#form"), 
	changeForm : $("#changeForm"),
	passwordChangeEvent : function(){
		if(this.password.val() == ""){
			alert("비밀번호 칸을 입력해주세요.");
			return false;
		}
		if(this.changePassword.val() == ""){
			alert("비밀번호 수정칸을 입력해주세요.");
			return false;
		}
		if(this.changePasswordConfirm.val() == ""){
			alert("비밀번호 수정 확인칸을 입력해주세요.");
			return false;
		}
		if(this.changePassword.val().length < 6 && this.changePasswordConfirm.val().length < 6){
			alert("비밀번호 수정은 6자 이상 입력해주세요.");
			return false;
		}
		if(this.changePassword.val() !== this.changePasswordConfirm.val()){
			alert("비밀번호 수정 확인칸을 다시 입력해주세요");
			return false;
		}
		this.changeForm.submit();
	},
	updateEvent : function(){
		if(this.name.val() === "") {
			alert("이름을 입력해주세요");
			this.name.focus();
			return false;
		}
		if(this.email.val() === "") {
			alert("이메일을 입력해주세요");
			this.email.focus();
			return false;
		}
		this.updateForm.submit();
	},
	imagePreviewEvent : function(e){
		var fileName = this.file.val();
		var ext = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(!fileName == "" && !(ext == "gif" || ext == "jpg" || ext == "jpeg" || ext == "png")){
			update.file.val("");
			preview.innerHTML = '';
			alert("이미지파일 (.jpg, .jpeg, .png, .gif ) 만 업로드 가능합니다.");
			return;
		}
		var reader = new FileReader();
		reader.onload = function (event) {
			var img = new Image();
			img.src = event.target.result;
			if(img.width > 200){
				img.width = 200;
			}
			if(img.height > 200){
				img.height = 200;
			}
			preview.innerHTML = '';
			preview.appendChild(img);
		};
		reader.readAsDataURL(ufile.files[0]);
	},
	init : function(){
		var self = this;
		self.passwordChangeBtn.click(function(){self.passwordChangeEvent();});
		self.updateBtn.click(function(){self.updateEvent();});
		self.file.change(function(){self.imagePreviewEvent();});
	}
}

update.init();