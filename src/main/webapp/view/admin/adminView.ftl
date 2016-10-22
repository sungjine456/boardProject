<div class="container" style="height:100%;">
	<table class="table" style="margin-top:10px;">
		<colgroup>
			<col width="10%"/>
			<col width="30%"/>
			<col width="25%"/>
			<col width="25%"/>
			<col width="10%"/>
		</colgroup>
		<thead>
			<tr>
				<th>
					이름
				</th>
				<th>
					이메일
				</th>
				<th>
					가입일
				</th>
				<th>
					수정일
				</th>
				<th>
					동의 여부
				</th>
			</tr>
		</thead>
		<tbody>
			<#list users as user>
			<tr>
				<td>
					${user.name}
				</td>
				<td>
					${user.email}
				</td>
				<td>
					${user.regDate}
				</td>
				<td>
					${user.upDate}
				</td>
				<td>
					${user.access}
				</td>
			</tr>
			</#list>
		</tbody>
	</table>
</div>