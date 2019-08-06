package com.hanweb.complat.controller.user;

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
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.constant.Tables;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.support.controller.CurrentUser;

/**
 * 用户列表页控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "user", allowedGroup = Allowed.YES)
@RequestMapping("manager/user")
public class ListUserController implements GridViewDelegate {

	@Autowired
	private GridViewService gridViewService;

	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView, Integer groupId, String groupName, String name, String loginName) {
		groupId = NumberUtil.getInt(groupId);
		if (groupId == 0) {
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			groupId = currentUser.getRangeId();
		}

		groupName = StringUtil.trim(groupName);
		if (StringUtil.isEmpty(groupName)) {
			groupName = "全部用户";
		}

		name = StringUtil.trim(name);
		loginName = StringUtil.trim(loginName);
		String searchText = gridView.getSearchText();
		searchText = StringUtil.trim(searchText);

		gridView.setDelegate(this);
		gridView.setViewName("complat/user/user_list");
		gridView.addObject("sessionUser", UserSessionInfo.getCurrentUser());
		gridView.setSearchText(searchText);
		gridView.addQueryParam("groupName", groupName);
		gridView.addQueryParam("groupId", groupId + "");
		gridView.setSearchPlaceholder("请输入用户姓名或登录名");
		gridView.addObject("name", name);
		gridView.addObject("loginName", loginName);

		createButton(gridView);
		createHead(gridView);
		createBody(gridView, groupId, name, loginName, searchText);
		return gridView;
	}

	/**
	 * 创建按钮
	 * 
	 * @param gridView
	 */
	private void createButton(GridView gridView) {
		gridView.addButton(Button.getAdd());
		gridView.addButton(Button.getRemove());
		gridView.addButton(Button.getImport());
		gridView.addButton(Button.getExport());
		// 判断动态码
		if (Settings.getSettings().isDynamicCodeLogin()) {
			gridView.addButton(Button.getInstance("f5063", "restDynamicCode", "重置动态码密钥"));
		}
	}

	/**
	 * 创建表头
	 * 
	 * @param gridView
	 */
	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setCheckbox(true).setField("iid"));
		gridView.addHead(Head.getInstance().setField("name").setTitle("姓名").setAlign("left").setAlign("left")
				.setWidth(200).setResizable(true));
		gridView.addHead(Head.getInstance().setField("loginname").setTitle("登录名").setAlign("left").setWidth(100));
		gridView.addHead(Head.getInstance().setField("groupid").setTitle("机构id").setHidden(true));
		gridView.addHead(Head.getInstance().setField("groupname").setTitle("所属机构").setAlign("left").setWidth(80));
		gridView.addHead(Head.getInstance().setField("headship").setTitle("职务").setAlign("left").setWidth(80));
		gridView.addHead(Head.getInstance().setField("enable").setTitle("账号开启").setWidth(80));
	}

	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView, Integer groupId, String name, String loginName, String searchText) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("iid").addSelectField("name").addSelectField("loginname")
				.addSelectField("(SELECT name FROM " + Tables.GROUP + " b WHERE b.iid = groupid) groupname")
				.addSelectField("headship").addSelectField("enable").setTable(Tables.USER);
		// 仅过滤系统管理员
		String where = "";
		if (NumberUtil.getInt(groupId) > 0) {
			where = "groupid = :groupId";
			gridViewSql.addParam("groupId", groupId);
		} else {
			where = "uuid <> :uuid";
			gridViewSql.addParam("uuid", "bdf699cf97284db0a9524332f0648f22");
		}
		if (StringUtil.isNotEmpty(searchText)) {
			where += " AND (name LIKE :searchText OR loginname LIKE :searchText)";
			gridViewSql.addParam("searchText", searchText, LikeType.LR);
		} else {
			if (StringUtil.isNotEmpty(name)) {
				where += " AND name LIKE :name";
				gridViewSql.addParam("name", name, LikeType.LR);
			}
			if (StringUtil.isNotEmpty(loginName)) {
				where += " AND loginname LIKE :loginName";
				gridViewSql.addParam("loginName", loginName, LikeType.LR);
			}
		}

		gridViewSql.setWhere(where);
		gridViewSql.addOrderBy("createtime", "DESC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
		Integer iid = NumberUtil.getInt(StringUtil.getString(rowData.get("iid")));
		String name = StringUtil.getString(rowData.get("name"));
		String loginName = StringUtil.getString(rowData.get("loginname"));
		String groupId = StringUtil.getString(rowData.get("groupid"));
		String groupName = StringUtil.getString(rowData.get("groupname"));
		String headship = StringUtil.getString(rowData.get("headship"));
		int enable = NumberUtil.getInt(rowData.get("enable"));

		gridRow.addCell("iid", iid);
		gridRow.addCell("name", name, Script.createScript("edit", iid, name));
		gridRow.addCell("loginname", loginName);
		gridRow.addCell("groupid", groupId);
		gridRow.addCell("groupname", groupName);
		gridRow.addCell("headship", headship);
		CurrentUser currentUser = gridRow.getGridObject("sessionUser");
		if (iid != NumberUtil.getInt(currentUser.getIid())) {
			gridRow.addCell("enable",
					"<input name=\"enable\" type=\"hidden\" value=\"" + enable + "\" data=\"{iid:" + iid + "}\"/>",
					false);
		}
	}
}
