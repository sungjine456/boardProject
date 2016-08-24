var editer = {
	frame : document.getElementById("editFrame"),
	editFontBold : $("#editFontBold"),
	editLeftBold : $("#editLeftBold"),
	editCenterBold : $("#editCenterBold"),
	editRightBold : $("#editRightBold"),
	func : function(){
		this.frame.contentWindow.document.designMode = "on";
	},
	editEvent : function(command){
		this.frame.focus();
		this.frame.contentWindow.document.execCommand(command);
	},
	init : function(){
		var self = this;
		self.editFontBold.click(function(){self.editEvent('bold')});
		self.editLeftBold.click(function(){self.editEvent('justifyleft')});
		self.editCenterBold.click(function(){self.editEvent('justifycenter')});
		self.editRightBold.click(function(){self.editEvent('justifyright')});
	}
}

window.onload = editer.func();

editer.init();