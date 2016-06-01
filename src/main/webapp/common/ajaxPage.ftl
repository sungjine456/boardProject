<#if Request.bool>
	<font style='color:blue'>${Session.message}</font>
<#else>
	<font style='color:red'>${Session.message}</font>
</#if>
