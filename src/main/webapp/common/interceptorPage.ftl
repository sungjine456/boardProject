<!DOCTYPE html>
<html>
	<body>
		<form id="form" action="/" method="get">
			<input type="hidden" name="message" value="로그인 후 이용해주세요."/>
		</form>
		<script>
			window.onload = function(){
				document.getElementById("form").submit();
			}
		</script>
	</body>
</html>