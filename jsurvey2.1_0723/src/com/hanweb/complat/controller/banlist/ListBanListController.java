package com.hanweb.complat.controller.banlist;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.service.GridViewService;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.grid.Button;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.GridViewDelegate;
import com.hanweb.common.view.grid.GridViewSql;
import com.hanweb.common.view.grid.Head;
import com.hanweb.complat.constant.Tables;

/**
 * 封停列表控制器
 * 
 * @author 李杰
 * 
 */
@Controller
@Permission(module = "banlist")
@RequestMapping("manager/banlist")
public class ListBanListController implements GridViewDelegate {
	@Autowired
	private GridViewService gridViewService;

	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView) {
		// 必须设置delegate
		gridView.setDelegate(this);

		gridView.setViewName("/complat/banlist/banlist_list");

		gridView.setPosition("封停列表");

		createToolBar(gridView);

		createHead(gridView);

		createBody(gridView);

		createQuery(gridView);

		return gridView;
	}

	/**
	 * 创建列表工具栏
	 * 
	 * @param gridView
	 */
	private void createToolBar(GridView gridView) {
		gridView.addButton(Button.getRemove());
		gridView.setSearchPlaceholder("请输入登录名");
		gridView.setShowAdvSearch(false);
	}

	/**
	 * 设置列表页携带参数
	 * 
	 * @param gridView
	 *            gridView
	 */
	private void createQuery(GridView gridView) {
	}

	/**
	 * 创建表头
	 * 
	 * @param gridView
	 */
	private void createHead(GridView gridView) {
		// 设置列
		gridView.addHead(Head.getInstance().setField("iid").setCheckbox(true));

		gridView.addHead(Head.getInstance().setField("loginname").setTitle("登录名").setAlign("left")
				.setWidth(300).setResizable(true));

		gridView.addHead(Head.getInstance().setField("logindate").setTitle("最后登录时间").setWidth(150));

		gridView.addHead(Head.getInstance().setField("ipaddr").setTitle("ip地址").setWidth(100));

		gridView.addHead(Head.getInstance().setField("logintimes").setTitle("错误次数").setWidth(100));
	}

	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("iid").addSelectField("loginname").addSelectField("logindate")
				.addSelectField("ipaddr").addSelectField("logintimes").setTable(Tables.BANLIST);
		if (StringUtil.isNotEmpty(gridView.getSearchText())) {
			gridViewSql.setWhere("loginname LIKE :loginname");
			gridViewSql.addParam("loginname", gridView.getSearchText(), LikeType.LR);
		}
		gridViewSql.addOrderBy("iid", "DESC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
		gridRow.addCell("iid", rowData.get("iid"));
		gridRow.addCell("loginname", rowData.get("loginname"));
		gridRow.addCell("logindate", DateUtil.dateToString((Date) rowData.get("logindate"),
				DateUtil.YYYY_MM_DD_HH_MM_SS));
		gridRow.addCell("ipaddr", rowData.get("ipaddr"));
		gridRow.addCell("logintimes", rowData.get("logintimes"));
	}
}
