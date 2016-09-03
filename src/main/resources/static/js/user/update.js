var update = {
	email : $("#form #email"),
	name : $("#form #name"),
	ufile : $("#ufile"),
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
			alert("비밀번호를 입력해주세요.");
			return false;
		}
		if(this.changePassword.val() == ""){
			alert("비밀번호 수정을 입력해주세요.");
			return false;
		}
		if(this.changePasswordConfirm.val() == ""){
			alert("비밀번호 수정 확인을 입력해주세요.");
			return false;
		}
		if(this.changePassword.val().length < 6 && this.changePasswordConfirm.val().length < 6){
			alert("비밀번호 수정은 6자 이상 입력해주세요.");
			return false;
		}
		if(this.changePassword.val() !== this.changePasswordConfirm.val()){
			alert("비밀번호 수정과 비밀번호 수정 확인이 동일하게 입력해주세요.");
			return false;
		}
		if(this.password.val() === this.changePassword){
			alert("비밀번호와 비밀번호 수정이 다르게 입력해주세요.");
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
		var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		if(this.email.val().length < 6 || !regex.test(this.email.val())) {
			alert("올바른 형식의 이메일을 입력해주세요");
			this.email.focus();
			return false;
		}
		this.updateForm.submit();
	},
	imagePreviewEvent : function(){
		var fileName = this.ufile.val();
		var ext = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(!fileName == "" && !(ext == "gif" || ext == "jpg" || ext == "jpeg" || ext == "png")){
			update.ufile.val("");
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
		self.ufile.change(function(){self.imagePreviewEvent();});
	}
}

update.init();