var login = {
	id : $("#form #id"),
	password : $("#form #password")
};

$("#joinBtn").click(function(){
	$(location).attr("href", "/addUser");
});

$("#translatePasswordBtn").click(function(){
	alert("asdads");
	$(location).attr("href", "/translatePassword");
});

$("#loginBtn").click(function(){
	if(login.id.val() === ""){
		alert("아이디를 입력해주세요.");
		return false;
	}
	
	if(login.password.val() === ""){
		alert("패스워드를 입력해주세요.");
		return false;
	}
	
	if(login.pw.val().length < 6){
		alert("비밀번호는 6자 이상  입력하셔야 합니다.");
		return false;
	}
});

$(document).ready(function(){
    var userSaveId = getCookie("userSaveId");
    $("input[name='id']").val(userSaveId); 
     
    if($("input[name='id']").val() != ""){
        $("#idSaveCheck").attr("checked", true);
    }
     
    $("#idSaveCheck").change(function(){
        if($("#idSaveCheck").is(":checked")){
            var userSaveId = $("input[name='id']").val();
            setCookie("userSaveId", userSaveId, 7);
        } else {
            deleteCookie("userSaveId");
        }
    });
     
    $("input[name='id']").keyup(function(){
        if($("#idSaveCheck").is(":checked")){
            var userSaveId = $("input[name='id']").val();
            setCookie("userSaveId", userSaveId, 7);
        }
    });
});
 
function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}
 
function deleteCookie(cookieName){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}
 
function getCookie(cookieName) {
    cookieName = cookieName + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(cookieName);
    var cookieValue = '';
    if(start != -1){
        start += cookieName.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1)end = cookieData.length;
        cookieValue = cookieData.substring(start, end);
    }
    return unescape(cookieValue);
}