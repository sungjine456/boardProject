var login = {
	form : $("#form"),
	translateForm : $("#translateForm"),
	id : $("#form #id"),
	password : $("#form #password"),
	email : $("#translateForm #email"),
	checkBox : $("#form #idSaveCheck"),
	joinBtn : $("#form #joinBtn"),
	translatePasswordBtn : $("#translateForm #translatePasswordBtn"),
	loginBtn : $("#form #loginBtn"),
	enterEvent : function(){
		if(event.keyCode === 13){
			this.loginEvent();
		}
	},
	loginEvent : function(){
		if(this.id.val() === ""){
			alert("아이디를 입력해주세요.");
			return false;
		}
		if(this.password.val() === ""){
			alert("패스워드를 입력해주세요.");
			return false;
		}
		if(this.password.val().length < 6){
			alert("비밀번호는 6자 이상  입력하셔야 합니다.");
			return false;
		}
		this.form.submit();
	},
	joinEvent : function(){
		$(location).attr("href", "/join");
	},
	tramslatePasswordEvent : function(){
		if(this.email.val() === ""){
			alert("이메일을 입력해주세요");
			return false;
		}
		translateForm.submit();
	},
	checkBoxEvent : function(){
        if(!this.checkBox.is(":checked")){
        	this.deleteCookie("saveId");
        }
	},
	deleteCookie : function(cookieName){
		var date = new Date();
	    date.setDate(date.getDate() - 1);
	    document.cookie = cookieName + "= ; expires=" + date.toGMTString();
	},
	init : function(){
		this.loginBtn.click(function(){login.loginEvent();});
		this.joinBtn.click(function(){login.joinEvent();});
		this.translatePasswordBtn.click(function(){login.tramslatePasswordEvent();});
		this.checkBox.change(function(){login.checkBoxEvent();});
		this.id.keyup(function(){login.enterEvent();});
		this.password.keyup(function(){login.enterEvent();});
	}
};

login.init();