var join = {
	form : $("#form"),
	pw : $("#form #password"),
	pwConfirm : $("#form #passwordConfirm"),
	email : $("#form #email"),
	id : $("#form #id"),
	name : $("#form #name"),
	joinBtn : $("#form #joinBtn"),
	idBool : $("#form #idBool"),
	emailBool : $("#form #emailBool"),
	passwordSpan : $("#form #passwordSpan"),
	idSpan : $("#form #idSpan"),
	emailSpan : $("#form #emailSpan"),
	file : $("#file"),
	preview : $("#preview"),
	joinEvent : function(){
		if(this.id.val() === ""){
			alert("아이디를 입력해주세요");
			this.id.focus();
			return false;
		}
		if(this.pw.val() === ""){
			alert("비밀번호를 입력해주세요");
			this.email.focus();
			return false;
		}
		if(this.pw.val() !== this.pwConfirm.val()){
			alert("비밀번호확인을 다시 입력해주세요");
			this.pw.focus();
			return false;
		}
		if(this.pw.val().length < 6 && this.pwConfirm.val().length < 6){
			alert("비밀번호는 6자 이상 입력해주세요.");
			this.pw.focus();
			return false;
		}
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
			alert("올바른 형식의 이메일을 확인해주세요");
			this.email.focus();
			return false;
		}
		if(this.idBool.val() === "false"){
			alert("아이디를 확인해주세요");
			this.id.focus();
			return false;
		}
		if(this.emailBool.val() === "false"){
			alert("이메일을 확인해주세요");
			this.email.focus();
			return false;
		}
		form.submit();
	},
	passwordCheckEvent : function(){
		if(this.pw.val() !== this.pwConfirm.val()){
			this.passwordSpan.html("<font style='color:red'>비밀번호가 일치하지 않습니다.</font>");
		} else if(this.pw.val().length < 6 && this.pwConfirm.val().length < 6){
			this.passwordSpan.html("<font style='color:red'>비밀번호는 6자 이상 입력해주세요.</font>");
		} else if(this.pw.val() === this.pwConfirm.val()){
			this.passwordSpan.html("<font style='color:blue'>비밀번호가 일치합니다.</font>");
		}
	},
	idCheckEvent : function(){
		$.ajax({
			url : "/join/idCheck",
			type : "POST",
			data : {"id" : this.id.val()},
			dataType : "JSON",
			success : function(data){
				if(data.bool === "true"){
					join.idBool.val("true");
					join.idSpan.html("<font style='color:blue'>"+data.str+"</font>");
				} else {
					join.idBool.val("false");
					join.idSpan.html("<font style='color:red'>"+data.str+"</font>");
				}
			},
			error : function(x, e){
				if(x.status == 404){
					$(location).attr("href", "/error/404error.ftl");
				}
			}
		});
	},
	emailCheckEvent : function(){
		$.ajax({
			url : "/join/emailCheck",
			type : "POST",
			data : {"email" : this.email.val()},
			dataType : "JSON",
			success : function(data){
				if(data.bool === "true"){
					join.emailBool.val("true");
					join.emailSpan.html("<font style='color:blue'>"+data.str+"</font>");
				} else {
					join.emailBool.val("false");
					join.emailSpan.html("<font style='color:red'>"+data.str+"</font>");
				}
			},
			error : function(x, e){
				if(x.status == 404){
					$(location).attr("href", "/error/404error.ftl");
				}
			}
		});
	},
	imagePreviewEvent : function(){
		var fileName = this.file.val();
		var ext = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(!fileName == "" && !(ext == "gif" || ext == "jpg" || ext == "jpeg" || ext == "png")){
			join.file.val("");
			preview.innerHTML = '';
			alert("이미지파일 (.jpg, .jpeg, .png, .gif ) 만 업로드 가능합니다.");
			return;
		}
		var reader = new FileReader();
		reader.onload = function (event) {
			var img = new Image();
			img.src = event.target.result;
			if(img.width > 360){
				img.width = 360;
			}
			if(img.height > 170){
				img.height = 170;
			}
			preview.innerHTML = '';
			preview.appendChild(img);
		};
		reader.readAsDataURL(file.files[0]);
	},
	init : function(){
		var self = this;
		self.joinBtn.click(function(){self.joinEvent();});
		self.pw.keyup(function(){self.passwordCheckEvent();});
		self.pwConfirm.keyup(function(){self.passwordCheckEvent();});
		self.id.keyup(function(){self.idCheckEvent();});
		self.email.keyup(function(){self.emailCheckEvent();});
		self.file.change(function(){self.imagePreviewEvent();});
	}
}

join.init();