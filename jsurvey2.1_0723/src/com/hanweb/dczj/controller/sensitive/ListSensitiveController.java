package com.hanweb.dczj.controller.sensitive;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

/**
 * 敏感词管理列表页
 * 
 * @author xll
 * 
 */
@Controller
@RequestMapping("manager/sensitive")
public class ListSensitiveController implements GridViewDelegate{

	@Autowired
	private GridViewService gridViewService;
	
	@RequestMapping("list")
	public GridView list(GridView gridView) {
		gridView.setDelegate(this);
		gridView.setShowAdvSearch(false);
		gridView.setSearchPlaceholder("请输入标题的关键字");
		createButton(gridView);
		createHead(gridView);
		createBody(gridView);
		String btn = "<table><tr><td><img id=\"sensitiveadd\" src=\"../../resources/complat/images/add.png\"></td>" +
		"<td><img id=\"sensitivedelete\" src=\"../../resources/complat/images/delete.png\"></td>" +
		"<td><img id=\"sensitiveimport\" src=\"../../resources/complat/images/import.png\"></td>" +
		"</tr></table>";
		gridView.addObject("btn",btn);
		gridView.setViewName("dczj/sensitive/sensitive_list");
		return gridView;
	}
	
	private void createButton(GridView gridView) {
		gridView.addButton(Button.getAdd());
		gridView.addButton(Button.getRemove());
		gridView.addButton(Button.getImport());
	}

	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setField("i_id").setCheckbox(true));
		gridView.addHead(Head.getInstance().setField("vc_sensitiveword").setTitle("敏感词").setAlign("left"));
	}

	private void createBody(GridView gridView) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);
		gridViewSql.addSelectField("i_id").addSelectField("vc_sensitiveword").setTable("jsurvey_sensitive");
		String where = null;
		String searchText = gridView.getSearchText();
		if (StringUtil.isNotEmpty(searchText)) {
			where = " vc_sensitiveword LIKE :vc_sensitiveword order by vc_sensitiveword asc";
			gridViewSql.addParam("vc_sensitiveword", searchText, LikeType.LR);
		}
		gridViewSql.setWhere(where);
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
		String i_id = StringUtil.getString(rowData.get("i_id"));
		String vc_sensitiveword = StringUtil.getString(rowData.get("vc_sensitiveword"));
		
		gridRow.addCell("i_id", i_id);
		gridRow.addCell("vc_sensitiveword", vc_sensitiveword,Script.createScript("edit", i_id));
	}
}
