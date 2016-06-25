var update = {
	updateForm : $("#updateForm"),
	updateBtn : $("#updateForm #updateBtn"),
	title : $("#updateForm #title"),
	content : $("#updateForm #content"),
	updateEvent : function(){
		if(this.title.val() == ""){
			alert("제목을 입력해주세요.");
			return;
		}
		if(this.content.val() == ""){
			alert("내용을 입력해주세요.");
			return;
		}
		this.updateForm.submit();
	},
	init : function(){
		this.updateBtn.click(function(){update.updateEvent();});
	}
}

update.init();