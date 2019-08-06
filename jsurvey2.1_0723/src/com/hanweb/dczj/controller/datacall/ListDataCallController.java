package com.hanweb.dczj.controller.datacall;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.service.GridViewService;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.GridViewDelegate;
import com.hanweb.common.view.grid.GridViewSql;
import com.hanweb.common.view.grid.Head;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/datacall")
public class ListDataCallController implements GridViewDelegate{

	@Autowired
	private GridViewService service;

	@Autowired
	private WebSiteService webSiteService;

	@RequestMapping("list")
	public GridView list(GridView gridView, Integer webId) {
		if (webId == null) {
			webId = 0;
		}
		DCZJ_WebSite webSite = webSiteService.findByIid(webId);
		if (webSite != null) {
			String webname = webSite.getName();
			gridView.addQueryParam("webname", webname);
		}
		gridView.setDelegate(this);
		gridView.setShowAdvSearch(false);
		gridView.setSearchPlaceholder("请输入标题的关键字");
		createButton(gridView);
		createHead(gridView);
		createBody(gridView, webId);
		gridView.addQueryParam("webId", webId);
		gridView.addQueryParam("p", gridView.getPageNumber());
		String btn = "<table><tr>";
		//if (webId > 0) {
			btn += "<td><img id=\"jactadd\" src=\"../../resources/complat/images/add.png\"></td>";
		//}
		btn += "<td><img id=\"jactdelete\" src=\"../../resources/complat/images/delete.png\"></td>";
		btn += "</tr></table>";
		gridView.addObject("btn", btn);
		gridView.setViewName("dczj/datacall/datacall_list");
		return gridView;
	}

	private void createButton(GridView gridView) {
		// TODO Auto-generated method stub
		
	}

	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setField("iid").setCheckbox(true));
		gridView.addHead(Head.getInstance().setField("datacall_name").setTitle("数据调用名称").setWidth(60).setAlign("left")
				.setTip(true));
		gridView.addHead(Head.getInstance().setField("createdate").setTitle("创建时间").setWidth(35));
		gridView.addHead(Head.getInstance().setField("createname").setTitle("创建人").setWidth(35));
		gridView.addHead(Head.getInstance().setField("tempstyle").setTitle("模板样式").setWidth(35));
		gridView.addHead(Head.getInstance().setField("publish").setTitle("发布").setWidth(35));
	}

	private void createBody(GridView gridView, Integer webId) {
		GridViewSql sql = GridViewSql.getInstance(gridView);
		sql.addSelectField("iid").addSelectField("datacall_name").addSelectField("createdate")
		.addSelectField("createname").addSelectField("datacall_type").addSelectField("updatehtml").setTable("jsurvey_datacall");
		StringBuilder where = new StringBuilder();
		where.append(" state = 0 "); //状态为未删除数据 
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		if(StringUtil.isNotEmpty(webId + "")){
			if(!currentUser.isSysAdmin()){
				where.append(" AND webid = :webid ");
				sql.addParam("webid", webId);
			}else{
				if(webId > 0){
					where.append(" AND webid = :webid ");
					sql.addParam("webid", webId);
				}
			}
		}
		String text = StringUtil.trim(gridView.getSearchText());
		if (StringUtil.isNotEmpty(text)) {
			where.append(" AND datacall_name LIKE :text");
			sql.addParam("text", text, LikeType.LR);
		}
		sql.setWhere(StringUtil.getString(where));
		sql.addOrderBy("iid", "DESC");
		service.find(sql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer arg2) {
		String i_id = StringUtil.getString(rowData.get("iid"));
		String vc_name = StringUtil.getString(rowData.get("datacall_name"));
		String createdate = StringUtil.getString(rowData.get("createdate"));
		String createname = StringUtil.getString(rowData.get("createname"));
		String type = StringUtil.getString(rowData.get("datacall_type"));
		String b_updatehtml = StringUtil.getString(rowData.get("updatehtml"));
		
		gridRow.addCell("iid", i_id);
		gridRow.addCell("datacall_name", vc_name,Script.createScript("edit", i_id));
		gridRow.addCell("createdate", createdate);
		gridRow.addCell("createname", createname);
		gridRow.addCell("tempstyle", "<img src=\"../../resources/complat/images/model.png\"/>",
				Script.createScript("settempstyle",i_id,type), false);
		if("1".equals(b_updatehtml)){
			gridRow.addCell("publish", "<img src=\"../../resources/complat/images/fabuhou.png\"/>",
					Script.createScript("showfrontpage",i_id), false);
		}else{
			gridRow.addCell("publish", "<img src=\"../../resources/complat/images/fabuqian.png\">",
					Script.createScript("publish", i_id), false);
		}
	}
}
