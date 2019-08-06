package com.hanweb.complat.controller.group;

import java.util.ArrayList;
import java.util.List;
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
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.util.SqlUtil;
import com.hanweb.support.controller.CurrentUser;

/**
 * 机构列表页控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "group", allowedGroup = Allowed.YES)
@RequestMapping("manager/group")
public class ListGroupController implements GridViewDelegate {

	@Autowired
	private GridViewService gridViewService;

	@Autowired
	private GroupService groupService;

	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView, Integer groupId, String groupName) {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		groupId = NumberUtil.getInt(groupId);
		if (groupId == 0 && currentUser.isGroupAdmin()) {
			groupId = currentUser.getRangeId();
			groupName = currentUser.getRangeName();
		}

		String searchText = gridView.getSearchText();
		searchText = StringUtil.trim(searchText);
		gridView.setSearchText(searchText);
		gridView.setShowAdvSearch(false);

		gridView.setDelegate(this);
		gridView.setViewName("complat/group/group_list");
		createButton(gridView, groupId);
		createHead(gridView);
		createBody(gridView, groupId, searchText, currentUser);
		gridView.addQueryParam("groupId", groupId + "");
		gridView.addQueryParam("groupName", groupName);
		gridView.setSearchPlaceholder("请输入机构名称或标识");
		return gridView;
	}

	/**
	 * 创建按钮
	 * 
	 * @param gridView
	 */
	private void createButton(GridView gridView, int groupId) {
		if (groupId >= 0) {
			gridView.addButton(Button.getAdd());
		}
		gridView.addButton(Button.getRemove());
		gridView.addButton(Button.getImport());
		gridView.addButton(Button.getExport());
	}

	/**
	 * 创建表头
	 * 
	 * @param gridView
	 */
	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setCheckbox(true).setField("iid"));
		gridView.addHead(Head.getInstance().setField("name").setTitle("机构名称").setAlign("left")
				.setWidth(200).setResizable(true));
		gridView.addHead(Head.getInstance().setField("pid").setTitle("上级机构id").setHidden(true));
		gridView.addHead(Head.getInstance().setField("pname").setTitle("上级机构").setAlign("left")
				.setWidth(100));
		gridView.addHead(Head.getInstance().setField("codeid").setTitle("机构标识").setAlign("left")
				.setWidth(80));
	}
	
	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView, int pid, String searchText, CurrentUser currentUser) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("iid").addSelectField("name").addSelectField("pid")
				.addSelectField("(SELECT name FROM " + Tables.GROUP + " WHERE iid = a.pid) pname")
				.addSelectField("codeid").setTable(Tables.GROUP + " a");
		StringBuilder where = new StringBuilder();

		if (pid < 0) {
			// 查询所有有权限的机构
			if (currentUser.isGroupAdmin()) {
				Integer rangeId = currentUser.getRangeId();
				List<Integer> rangeIdsList = new ArrayList<Integer>();
				rangeIdsList = groupService.findIdsByPId(rangeId, rangeIdsList);
				where.append("iid IN(" + StringUtil.join(rangeIdsList, ",") + ")");
			}
		} else {
			where.append("pid = :pid");
			gridViewSql.addParam("pid", pid);
		}

		if (StringUtil.isNotEmpty(searchText)) {
			where.append(" AND (name LIKE :name OR codeid = :codeId)");
			gridViewSql.addParam("name", searchText, LikeType.LR);
			gridViewSql.addParam("codeId", searchText);
		}
		gridViewSql.setWhere(SqlUtil.trimWhere(where));
		gridViewSql.addOrderBy("orderid", "ASC");
		gridViewSql.addOrderBy("iid", "ASC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
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