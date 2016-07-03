var main = {
	write : $("#write"),
	locationBoardWrite : function(){
		$(location).attr("href", "/boardWrite");
	},
	init : function() {
		var self = this;
		self.write.click(function(){self.locationBoardWrite();});
	}
};

main.init();