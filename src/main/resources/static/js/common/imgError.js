$().ready(function() {
	$('img').each(function(n) {
		$(this).error(function() {
			$(this).attr('src', "img/user/none.png");
		});
	});
});