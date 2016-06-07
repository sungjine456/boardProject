var join = {
	form : $("#form"),
	pw : $("#form #password"),
	pwConfirm : $("#form #passwordConfirm"),
	email : $("#form #email"),
	id : $("#form #id"),
	name : $("#form #name"),
	joinBtn : $("#joinBtn"),
	joinEvent : function(){
		if(this.id.val() === ""){
			alert("아이디를 입력해주세요");
			return false;
		}
		if(this.pw.val() === ""){
			alert("비밀번호를 입력해주세요");
			return false;
		}
		if(this.pw.val() !== this.pwConfirm.val()){
			alert("비밀번호확인을 다시 입력해주세요");
			return false;
		}
		if(this.pw.val().length < 6 && this.pwConfirm.val().length < 6){
			alert("비밀번호는 6자 이상 입력해주세요.");
			return false;
		}
		if(this.name.val() === "") {
			alert("이름을 입력해주세요");
			return false;
		}
		if(this.email.val() === "") {
			alert("이메일을 입력해주세요");
			return false;
		}
		var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		if(this.email.val().length < 6 || !regex.test(this.email.val())) {
			alert("이메일을 확인해주세요");
			return false;
		}
		form.submit();
	},
	passwordCheckEvent : function(){
		if(join.pw.val() !== join.pwConfirm.val()){
			$("#passwordSpan").html("<font style='color:red'>비밀번호가 일치하지 않습니다.</font>");
		} else if(join.pw.val().length < 6 && join.pwConfirm.val().length < 6){
			$("#passwordSpan").html("<font style='color:red'>비밀번호는 6자 이상 입력해주세요.</font>");
		} else {
			$("#passwordSpan").html("<font style='color:blue'>비밀번호가 일치합니다.</font>");
		}
	},
	passwordConfirmCheckEvent : function(){
		if(join.pw.val() !== join.pwConfirm.val()){
			$("#passwordSpan").html("<font style='color:red'>비밀번호가 일치하지 않습니다.</font>");
		} else if(join.pw.val().length < 6 && join.pwConfirm.val().length < 6){
			$("#passwordSpan").html("<font style='color:red'>비밀번호는 6자 이상 입력해주세요.</font>");
		} else {
			$("#passwordSpan").html("<font style='color:blue'>비밀번호가 일치합니다.</font>");
		}
	},
	idCheckEvent : function(){
		$.ajax({
			url : "/idCheck",
			type : "POST",
			data : {"id" : join.id.val()},
			success : function(data){
				$("#idSpan").html(data);
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
			url : "/emailCheck",
			type : "POST",
			data : {"email" : join.email.val()},
			success : function(data){
				$("#emailSpan").html(data);
			},
			error : function(x, e){
				if(x.status == 404){
					$(location).attr("href", "/error/404error.ftl");
				}
			}
		});
	},
	init : function(){
		this.joinBtn.click(function(){join.joinEvent();});
		this.pw.keyup(function(){join.passwordCheckEvent();});
		this.pwConfirm.keyup(function(){passwordConfirmCheckEvent();});
		this.id.keyup(function(){join.idCheckEvent();});
		this.email.keyup(function(){join.emailCheckEvent();});
	}
}

join.init();