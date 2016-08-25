var editer = {
	frame : document.getElementById("editFrame"),
	editFontBold : $("#editFontBold"),
	editLeftBold : $("#editLeftBold"),
	editCenterBold : $("#editCenterBold"),
	editRightBold : $("#editRightBold"),
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
		self.editLeftBold.click(function(){self.editEvent('justifyleft')});
		self.editCenterBold.click(function(){self.editEvent('justifycenter')});
		self.editRightBold.click(function(){self.editEvent('justifyright')});
		self.editImage.change(function(){self.editImageEvent('insertimage')});
	}
}

window.onload = editer.func();

editer.init();