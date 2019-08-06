package com.hanweb.dczj.controller.setting;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.service.GridViewService;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.GridViewDelegate;
import com.hanweb.common.view.grid.GridViewSql;
import com.hanweb.common.view.grid.Head;

/**
 * 中奖信息列表页
 * @author admin
 *
 */
@Controller
@RequestMapping("manager/drawwinners")
public class ListDrawWinnersController implements GridViewDelegate{
	
	@Autowired
	private GridViewService service;
	
	@RequestMapping("list")
	public GridView list(GridView gridView, Integer dczjid) {
		gridView.setDelegate(this);
		gridView.setShowAdvSearch(false);
		gridView.setSearchPlaceholder("请输入奖品的关键字");
		gridView.addQueryParam("dczjid", dczjid);
		createHead(gridView);
		createBody(gridView,dczjid);
		gridView.setViewName("dczj/setting/drawwinners_list");
		return gridView;
	}
	
	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setField("loginname").setTitle("登录名"));
		gridView.addHead(Head.getInstance().setField("prizename").setTitle("奖品"));
		gridView.addHead(Head.getInstance().setField("wintime").setTitle("中奖时间"));
	}

	private void createBody(GridView gridView, Integer dczjid) {
		GridViewSql sql = GridViewSql.getInstance(gridView);
		sql.addSelectField("iid").addSelectField("loginname").addSelectField("winnername")
		.addSelectField("prizename").addSelectField("wintime").setTable("jsurvey_winnersinfo");
		StringBuilder where = new StringBuilder();
		where.append(" 1 = 1 ");
		if(StringUtil.isNotEmpty(dczjid + "")){
				where.append(" AND dczjid = :dczjid ");
				sql.addParam("dczjid", dczjid);
		}
		String text = StringUtil.trim(gridView.getSearchText());
		if (StringUtil.isNotEmpty(text)) {
			where.append(" AND prizename LIKE :text");
			sql.addParam("text", text, LikeType.LR);
		}
		sql.setWhere(StringUtil.getString(where));
		sql.addOrderBy("iid", "DESC");
		service.find(sql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer arg2) {
		String loginname = StringUtil.getString(rowData.get("loginname"));
		String prizename = StringUtil.getString(rowData.get("prizename"));
		String wintime = StringUtil.getString(rowData.get("wintime"));

		gridRow.addCell("loginname", loginname);
		gridRow.addCell("prizename", prizename);
		gridRow.addCell("wintime", wintime);
	}

}
