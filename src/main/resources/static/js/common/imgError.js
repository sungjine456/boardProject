$().ready(function() {
	$('img').each(function() {
		$(this).on("error", function () {
			$(this).attr('src', "img/user/none.png");
		});
	});
});