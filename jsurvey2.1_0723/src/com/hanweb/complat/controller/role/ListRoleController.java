package com.hanweb.complat.controller.role;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hanweb.common.annotation.Permission;
import com.hanweb.common.basedao.LikeType;
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

/**
 * 角色列表控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "role")
@RequestMapping("manager/role")
public class ListRoleController implements GridViewDelegate {

	@Autowired
	private GridViewService gridViewService;

	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView) {
		gridView.setDelegate(this);
		gridView.setViewName("complat/role/role_list");
		createButton(gridView);
		createHead(gridView);
		createBody(gridView);
		gridView.setShowAdvSearch(false);
		gridView.setPosition("角色管理");
		gridView.setSearchPlaceholder("请输入角色名称");
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
		gridView.addHead(Head.getInstance().setField("name").setTitle("角色名称").setAlign("left")
				.setWidth(200).setResizable(true));
		gridView.addHead(Head.getInstance().setField("isdefault").setTitle("设为缺省角色")
				.setAlign("center").setWidth(100));
		gridView.addHead(Head.getInstance().setField("type").setTitle("成员设置").setAlign("center")
				.setWidth(80));
		gridView.addHead(Head.getInstance().setField("right").setTitle("权限设置").setAlign("center")
				.setWidth(80));
	}

	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("iid").addSelectField("name").addSelectField("isdefault")
				.addSelectField("type").setTable(Tables.ROLE);
		if (StringUtil.isNotEmpty(gridView.getSearchText())) {
			gridViewSql.setWhere("name LIKE :name");
			gridViewSql.addParam("name", gridView.getSearchText(), LikeType.LR);
		}
		gridViewSql.addOrderBy("iid", "ASC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
		String iid = StringUtil.getString(rowData.get("iid"));
		String name = StringUtil.getString(rowData.get("name"));
		String isDefault = StringUtil.getString(rowData.get("isdefault"));
		int type = NumberUtil.getInt(rowData.get("type"));
		int itype = 6;
		if (type < itype) {
			gridRow.addCell("iid", iid).setDisabled(true);
		} else {
			gridRow.addCell("iid", iid);
		}
		gridRow.addCell("name", name, Script.createScript("edit", iid, name));
		if (type == itype) {
			gridRow.addCell("isdefault", "<input type='checkbox' onclick='(modifyIsDefault(" + iid
					+ ", this))' " + ("1".equals(isDefault) ? "checked='checked'" : "") + " />",
					false);
		} else {
			// 管理员不能设置为缺省角色+
			gridRow.addCell("isdefault", "", false);
		}
		gridRow.addCell("type",
				"<i class=\"iconfont link\">&#xf1012;</i>",
				Script.createScript("setMembers", iid), false);
		// 系统管理员不能设置权限
		if (type >= itype) { 
			gridRow.addCell(
					"right",
					"<i class=\"iconfont link\">&#xf101a;</i>",
					Script.createScript("setRight", iid), false);
		}
	}
}
