var write = {
	writeForm : $("#writeForm"),
	writeBtn : $("#writeForm #writeBtn"),
	title : $("#writeForm #title"),
	content : $("#writeForm #content"),
	writeEvent : function(){
		var editFrame = frame.document.body.innerHTML;
		if(this.title.val() == ""){
			alert("제목을 입력해주세요.");
			this.title.focus();
			return;
		}
		if(commonJs.spaceRemove(this.title.val()) == ""){
			alert("제목을 다시 입력해주세요.");
			this.title.val("");
			this.title.focus();
			return;
		}
		if(editFrame == ""){
			alert("내용을 입력해주세요.");
			return;
		}
		this.content.val(editFrame.replace(/&nbsp;/gi, ""));
		if(this.content.val() == ""){
			alert("내용을 입력해주세요.");
			return;
		}
		if(editer.editerSpaceRemove(this.content.val()) == ""){
			alert("내용을 다시 입력해주세요.");
			this.content.focus();
			return;
		}
		this.writeForm.submit();
	},
	init : function(){
		var self = this;
		self.writeBtn.click(function(){self.writeEvent();});
	}
}

write.init();