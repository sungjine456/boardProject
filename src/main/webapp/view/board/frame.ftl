<!DOCTYPE html>
<html>
	<head>
		<script>
			<#assign include = Request.include!"main/board.ftl">
		</script>
		<title>Person Board Projec</title>
		<link rel="stylesheet" href="css/boot/bootstrap.min.css">
		<link rel="stylesheet" href="css/boot/bootstrap-theme.min.css">
		<link rel="stylesheet" href="css/common/common.css">
	</head>
	<body>
		<div id="wrap">
			<div id="header" style="background-color: black">
				<#include "top.ftl">
			</div>
			<div id="middle">
				<#include include>
			</div>
		<div>
		<script type="text/javascript" src="js/common/jquery-1.12.3.min.js"></script>
		<script type="text/javascript" src="js/boot/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/board/main.js"></script>
	</body>
</html>