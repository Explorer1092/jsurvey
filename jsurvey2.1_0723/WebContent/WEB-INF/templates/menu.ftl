<div id="nav">
	<ul class="nav-menu">
		<#list top_menus as top_menu>
		<#if top_menu.isAutoHide() == false>
		<li channel="${top_menu.channel}" class="${top_menu.clazz}" style="${top_menu.style}">
			<div onclick="${top_menu.onClick}" ${(top_menu.tree?length == 0)?string('','menu="'+top_menu.tree+'"')} ${(top_menu.page?length == 0)?string('','page="'+top_menu.page+'"')} class="nav-title nav-link">${top_menu.name}</div>
			<#if (top_menu.subMenuItems?size > 0)>
			<ul class="nav-submenu" style="left: auto; right: -1px;">
			<#list top_menu.subMenuItems as sub_menu>
				<li class="${sub_menu.clazz}" style="${sub_menu.style}">
					<div onclick="${sub_menu.onClick}" ${(sub_menu.tree?length == 0)?string('','menu="'+sub_menu.tree+'"')} ${(sub_menu.page?length == 0)?string('','page="'+sub_menu.page+'"')} class="nav-link"><span>${sub_menu.name}</span></div>
				</li>
			</#list>
			</ul>
			</#if>
		</li>
		<#if top_menu.separator>
		<li class="separator"></li>
		</#if>
		</#if>
		</#list>
	</ul>
</div>