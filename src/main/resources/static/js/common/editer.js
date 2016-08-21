var editer = {
	frame : document.getElementById("editFrame"),
	editFontBold : $("#editFontBold"),
	func : function(){
		this.frame.contentWindow.document.designMode = "on";
	},
	editFontBoldEvent : function(command){
		this.frame.contentWindow.document.execCommand(command, false, null);
	},
	init : function(){
		var self = this;
		self.editFontBold.click(function(){self.editFontBoldEvent('bold')});
	}
}

window.onload = editer.func();

editer.init();