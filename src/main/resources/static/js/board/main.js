var main = {
	write : $("#write"),
	locationBoardWrite : function(){
		$(location).attr("href", "/boardWrite");
	},
	init : function() {
		this.write.click(function(){main.locationBoardWrite();});
	}
};

main.init();