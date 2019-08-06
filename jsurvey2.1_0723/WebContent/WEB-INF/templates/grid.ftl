<#if grid_buttons || grid_simplesearch || grid_advsearch>
<div id="${grid_id}_toolbar">
	<div class="datagrid-toolbar-btn-wrap">
		<#list grid_buttons as button>
			<#if button.subButtons>
				<a class="datagrid-toolbar-btn" ${button.domAttrsStr} onclick="toolbarAction('${button.function?default("")}')">
				 ${(button.icon?length == 0)?string('','<i class="iconfont">'+button.icon+'</i>')}${button.value?default("")}<i class="iconfont" style="margin-left:3px;font-size:8px;">&#xf001e;</i>
				<ul class="datagrid-toolbar-subbtns ani-transform-y">
					<#list button.subButtons as subbutton>
							<li class="datagrid-toolbar-subbtn" ${button.domAttrsStr} onclick="toolbarAction('${subbutton.function?default("")}');">${(subbutton.icon?length == 0)?string('','<i class="iconfont">'+subbutton.icon+'</i>')}${subbutton.value?default("")}</li>
					</#list>
				</ul>
				</a>
			<#else>
				<a class="datagrid-toolbar-btn" title="${button.tip?default("")}" ${button.domAttrsStr} onclick="toolbarAction('${button.function?default("")}')">${(button.icon?length == 0)?string('','<i class="iconfont">'+button.icon+'</i>')}${button.value?default("")}</a>
			</#if>
		</#list>
	</div>
	<div class="datagrid-toolbar-search-wrap">
		<#if grid_simplesearch>
		<div class="datagrid-toolbar-simple-search">
		<form>
		<span class="simple_search_custom_wrap">
		</span>
		<span class="simple_search_default_wrap">
			<input type="text" id="${grid_id}_simple_search" class="input-text" style="width:150px;" name="searchText" value="${grid_searchText}" placeholder="${grid_searchPlaceholder}"/>
			<input type="button" class="btn btn-primary" style="margin-left:5px;" value="检索"/>
		</span>
		</form>
		</div>
		</#if>
	    <#if grid_advsearch>
		<input type="button" class="btn btn-advsearch" style="margin-left:5px;" value="高级检索"/>
		</#if>
	</div>
</div>
</#if>
<script type="text/javascript">
	gridOptions['${grid_id}'] = {
			pageSize: ${pageSize},
			idFieldName: '${grid_checkbox_field}',
			recordTotal: ${total},
			pageSize: ${pageSize},
			pageList: ${pageList},
			showPageList : ${showPageList},
			pageNumber: ${pageNumber},
			searchType:${grid_searchType},
			sortName:'${sortName}',
			sortOrder:'${sortOrder}',
			dataSize:${grid_datas?size}
	}
</script>
<table id="${grid_id}" class="easyui-datagrid" scrollbarSize="1" sortName="${sortName}" loadMsg="" sortOrder="${sortOrder}" data-options="toolbar:'#${grid_id}_toolbar',${grid_options}" style="display:none">
    <thead>
        <tr>
        <#list grid_heads as head>
        	<th data-options="
        					field:'${head.field?default("")}',
        					checkbox:${head.checkbox?string('true','false')},
        					width:${head.width},
        					align:'${head.align}',
        					sortable:${head.sortable?string('true','false')},
        					hidden:${head.hidden?string('true','false')},
        					resizable:${head.resizable?string('true','false')},
        					fixed:${head.fixed?string('true','false')},
        					editor:'${head.editor}'
        	">
        		${head.title?default("")}
        	</th>
		</#list>
        </tr>
    </thead>
    <tbody>
    <#if (grid_datas?size > 0)>
    <#list grid_datas as row>
    	<tr>
		<#list grid_heads as head>
			<td>
			<#list row.gridCells as cell>
				<#if head.field == cell.field>
					<#if head.checkbox>
			        	<input type="checkbox" ${cell.disabled?string('disabled','')} value="${cell.safeValue}"/>
			        <#elseif (cell.onClick?length > 0)>
		        		<div style="cursor: pointer;" class="datagrid-cell-ellipsis" onclick="${cell.onClick}" ${head.tip?string('title="${cell.tip?html}"','')}>
		        		<a>
		        		${cell.safeValue}
		        		</a></div>
			        <#elseif (head.hidden)>
			        	<div class="datagrid-cell-ellipsis" ${head.tip?string('title="${cell.tip?html}"','')}>
		        			${cell.safeValue}
		        		</div>
			        <#else>
			        	<div class="datagrid-cell-ellipsis" ${head.tip?string('title="${cell.tip?html}"','')}>
		        			${cell.safeValue}
		        		</div>
			        </#if>
		        </#if>
		    </#list>
		    </td>
		</#list>
        </tr>
    </#list>
    </#if>
    </tbody>
</table>