var editer = {
	frame : document.getElementById("editFrame"),
	editFontBold : $("#editFontBold"),
	editFontUnderLine : $("#editFontUnderLine"),
	editFontItalic : $("#editFontItalic"),
	editLeftSort : $("#editLeftSort"),
	editCenterSort : $("#editCenterSort"),
	editRightSort : $("#editRightSort"),
	editImage : $("#editImage"),
	func : function(){
		this.frame.contentWindow.document.designMode = "on";
	},
	editEvent : function(command){
		this.frame.focus();
		this.frame.contentWindow.document.execCommand(command);
	},
	editImageEvent : function(command){
		this.frame.focus();
		var fileName = this.editImage.val();
		var src;
		var ext = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();
		if(!fileName == "" && !(ext == "gif" || ext == "jpg" || ext == "jpeg" || ext == "png")){
			join.file.val("");
			preview.innerHTML = '';
			alert("이미지파일 (.jpg, .jpeg, .png, .gif ) 만 업로드 가능합니다.");
			return;
		}
		var reader = new FileReader();
		reader.addEventListener('load', function() {
			editer.frame.contentWindow.document.execCommand(command, false, reader.result);
		});
		reader.readAsDataURL(editImage.files[0]);
	},
	init : function(){
		var self = this;
		self.editFontBold.click(function(){self.editEvent('bold')});
		self.editFontUnderLine.click(function(){self.editEvent('underline')});
		self.editFontItalic.click(function(){self.editEvent('italic')});
		self.editLeftSort.click(function(){self.editEvent('justifyleft')});
		self.editCenterSort.click(function(){self.editEvent('justifycenter')});
		self.editRightSort.click(function(){self.editEvent('justifyright')});
		self.editImage.change(function(){self.editImageEvent('insertimage')});
	}
}

window.onload = editer.func();

editer.init();