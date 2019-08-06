package com.hanweb.dczj.controller.website;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.permission.Allowed;
import com.hanweb.common.service.GridViewService;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.view.grid.Button;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.GridViewDelegate;
import com.hanweb.common.view.grid.GridViewSql;
import com.hanweb.common.view.grid.Head;
//import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.constant.Tables;
import com.hanweb.support.controller.CurrentUser;

@Controller
@Permission(module = "website", allowedGroup = Allowed.YES)
@RequestMapping("manager/website")
public class ListWebController implements GridViewDelegate {

	@Autowired
	private GridViewService gridViewService;
	
	
	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView, Integer webId, String webName) {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		webId = NumberUtil.getInt(webId);

		String searchText = gridView.getSearchText();
		searchText = StringUtil.trim(searchText);
		gridView.setSearchText(searchText);
		gridView.setShowAdvSearch(false);

		gridView.setDelegate(this);
		gridView.setViewName("dczj/website/web_list");
		createButton(gridView,webId);
		createHead(gridView);
		createBody(gridView, webId, searchText, currentUser);
		
		String btn = "<table><tr>" ;
		if(webId >= 0){
			btn += "<td><img id=\"webadd\" src=\"../../resources/complat/images/add.png\"></td>";
		};
		btn += 	 
		"<td><img id=\"webdelete\" src=\"../../resources/complat/images/delete.png\"></td>" +
		"</tr></table>";
		gridView.addObject("btn",btn);
		
		gridView.addQueryParam("webId", webId + "");
		gridView.addQueryParam("webName", webName);
		gridView.setSearchPlaceholder("请输入网站名称或标识");
		return gridView;
	}
	
	private void createButton(GridView gridView, Integer webId) {
		if(webId >= 0){
			gridView.addButton(Button.getAdd());
		}
		gridView.addButton(Button.getRemove());
	}

	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setCheckbox(true).setField("iid"));
		gridView.addHead(Head.getInstance().setField("name").setTitle("网站名称").setAlign("left")
				.setWidth(200).setResizable(true));
		gridView.addHead(Head.getInstance().setField("pid").setTitle("上级网站id").setHidden(true));
		gridView.addHead(Head.getInstance().setField("pname").setTitle("上级网站").setAlign("left")
				.setWidth(100));
		gridView.addHead(Head.getInstance().setField("codeid").setTitle("网站标识").setAlign("left")
				.setWidth(80));
	}

	private void createBody(GridView gridView, Integer pid, String searchText, CurrentUser currentUser) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("iid").addSelectField("name").addSelectField("pid")
				.addSelectField("(SELECT name FROM " + Tables.WEBSITE + " WHERE iid = a.pid) pname")
				.addSelectField("codeid").setTable(Tables.WEBSITE + " a");
		StringBuilder where = new StringBuilder();

		if (pid < 0) {
			// 查询所有有权限的网站
//			if (currentUser.isWebAdmin()) {
//				Integer userId = currentUser.getIid();
//				List<Integer> rangeIdsList =webManagerService.findWebIdsByUserId(userId);
//				where.append("iid IN(" + StringUtil.join(rangeIdsList, ",") + ")");
//			}else{
				where.append("1 = 1");
//			}
		} else {
			where.append("pid = :pid");
			gridViewSql.addParam("pid", pid);
		}

		if (StringUtil.isNotEmpty(searchText)) {
			where.append(" AND (name LIKE :name OR codeid = :codeId)");
			gridViewSql.addParam("name", searchText, LikeType.LR);
			gridViewSql.addParam("codeId", searchText);
		}
		
		gridViewSql.setWhere(where.toString());
		gridViewSql.addOrderBy("orderid", "ASC");
		gridViewSql.addOrderBy("iid", "ASC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer arg2) {
		String iid = StringUtil.getString(rowData.get("iid"));
		String name = StringUtil.getString(rowData.get("name"));
		String pid = StringUtil.getString(rowData.get("pid"));
		String pname = StringUtil.getString(rowData.get("pname"));
		String codeId = StringUtil.getString(rowData.get("codeid"));

		gridRow.addCell("iid", iid);
		gridRow.addCell("name", name, Script.createScript("edit", iid, name));
		gridRow.addCell("pid", pid);
		gridRow.addCell("pname", pname);
		gridRow.addCell("codeid", codeId);
	}

}
