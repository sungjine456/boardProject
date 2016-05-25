var user = {
	pw : $("#form #password"),
	pwConfirm : $("#form #passwordConfirm"),
	email : $("#form #email"),
	id : $("#form #id"),
	name : $("#form #name")
}

$("#joinBtn").click(function(){
	if(user.id.val() === ""){
		alert("아이디를 입력해주세요");
		return false;
	}
		
	if(user.pw.val() === ""){
		alert("비밀번호를 입력해주세요");
		return false;
	}
	
	if(user.pw.val() !== user.pwConfirm.val()){
		alert("비밀번호확인을 다시 입력해주세요");
		return false;
	}
	
	if(user.pw.val().length < 6 && user.pwConfirm.val().length < 6){
		alert("비밀번호는 6자 이상 15자 이하로 입력하셔야 합니다.");
		return false;
	}
	
	if(user.name.val() === "") {
		alert("이름을 입력해주세요");
		return false;
	}
	
	if(user.email.val() === "") {
		alert("이메일을 입력해주세요");
		return false;
	}
	
	var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	if(user.email.val().length < 6 || !regex.test(user.email.val())) {
		alert("이메일을 확인해주세요");
		return false;
	}
});

user.pw.keyup(function(){
	if(user.pw.val() !== user.pwConfirm.val()){
		$("#passwordSpan").html("<font style='color:red'>비밀번호가 일치하지 않습니다.</font>");
	} else {
		$("#passwordSpan").html("<font style='color:blue'>비밀번호가 일치합니다.</font>");
	}
});

user.pwConfirm.keyup(function(){
	if(user.pw.val() !== user.pwConfirm.val()){
		$("#passwordSpan").html("<font style='color:red'>비밀번호가 일치하지 않습니다.</font>");
	} else {
		$("#passwordSpan").html("<font style='color:blue'>비밀번호가 일치합니다.</font>");
	}
});

user.id.keyup(function(){
	$.ajax({
		url : "/idCheck",
		type : "POST",
		data : {"id" : user.id.val()},
		success : function(data){
			$("#idSpan").html("<font>"+data+"</font>");
		}
	});
});

user.email.keyup(function(){
	$.ajax({
		url : "/emailCheck",
		type : "POST",
		data : {"email" : user.email.val()},
		success : function(data){
			$("#emailSpan").html("<font>"+data+"</font>");
		}
	});
});