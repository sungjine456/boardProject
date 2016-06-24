var write = {
	writeForm : $("#writeForm"),
	writeBtn : $("#writeForm #writeBtn"),
	title : $("#writeForm #title"),
	content : $("#writeForm #content"),
	writeEvent : function(){
		if(this.title.val() == ""){
			alert("제목을 입력해주세요.");
			return;
		}
		if(this.content.val() == ""){
			alert("내용을 입력해주세요.");
			return;
		}
		this.writeForm.submit();
	},
	init : function(){
		this.writeBtn.click(function(){write.writeEvent();});
	}
}

write.init();