package com.hanweb.complat.controller.outsideuser;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.service.GridViewService;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.view.grid.Button;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.GridViewDelegate;
import com.hanweb.common.view.grid.GridViewSql;
import com.hanweb.common.view.grid.Head;
import com.hanweb.complat.constant.Tables;

/**
 * 外网用户列表控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "outsideuser")
@RequestMapping("manager/outsideuser")
public class ListOutsideUserController implements GridViewDelegate {

	@Autowired
	private GridViewService gridViewService;

	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView, String name, String loginName) {
		gridView.setDelegate(this);
		gridView.setViewName("complat/outsideuser/outsideuser_list");
		createButton(gridView);
		createHead(gridView);
		createBody(gridView, name, loginName);
		gridView.setPosition("前台用户管理");
		gridView.setSearchPlaceholder("请输入用户姓名或登录名");

		gridView.addObject("name", name);
		gridView.addObject("loginName", loginName);
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
	}

	/**
	 * 创建表头
	 * 
	 * @param gridView
	 */
	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setCheckbox(true).setField("iid"));
		gridView.addHead(Head.getInstance().setField("name").setTitle("姓名").setAlign("left")
				.setWidth(200).setResizable(true));
		gridView.addHead(Head.getInstance().setField("loginname").setTitle("登录名").setAlign("left")
				.setWidth(100));
		gridView.addHead(Head.getInstance().setField("enable").setTitle("账号开启").setAlign("center")
				.setWidth(80));
	}

	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView, String name, String loginName) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("iid").addSelectField("name").addSelectField("loginname")
				.addSelectField("headship").addSelectField("enable").setTable(Tables.OUTSIDEUSER);
		String where = "1=1";
		String userName = gridView.getSearchText();
		if (StringUtil.isNotEmpty(userName)) {
			where += " AND (name LIKE :userName OR loginname LIKE :userName)";
			gridViewSql.addParam("userName", userName, LikeType.LR);
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
		gridViewSql.addOrderBy("iid", "DESC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
		String iid = StringUtil.getString(rowData.get("iid"));
		String name = StringUtil.getString(rowData.get("name"));
		String loginName = StringUtil.getString(rowData.get("loginname"));
		String enable = StringUtil.getString(rowData.get("enable"));

		gridRow.addCell("iid", iid);
		gridRow.addCell("name", name, Script.createScript("edit", iid, name));
		gridRow.addCell("loginname", loginName);
		gridRow.addCell("enable", "<input name=\"enable\" type=\"hidden\" value=\"" + enable
				+ "\" data=\"{iid:" + iid + "}\"/>", false);

	}

}
