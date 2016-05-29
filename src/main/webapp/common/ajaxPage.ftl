<#if Request.bool>
	<font style='color:blue'>${Request.message}</font>
<#else>
	<font style='color:red'>${Request.message}</font>
</#if>
