package com.hanweb.complat.controller.role;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hanweb.common.annotation.Permission;
import com.hanweb.common.basedao.LikeType;
import com.hanweb.common.service.GridViewService;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.grid.Button;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.GridViewDelegate;
import com.hanweb.common.view.grid.GridViewSql;
import com.hanweb.common.view.grid.Head;
import com.hanweb.complat.constant.Tables;

/**
 * 角色成员列表控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "list")
@RequestMapping("manager/role/members")
public class ListRoleMembersController implements GridViewDelegate {

	private static final String PARENT_ID = "0";
	@Autowired
	private GridViewService gridViewService;

	@Permission(function = "list")
	@RequestMapping("list")
	public GridView list(GridView gridView, Integer roleId, Integer memberType, String memberName) {
		if (memberType == null) {
			memberType = 0;
		}

		gridView.setShowPageList(false);
		gridView.setDelegate(this);
		gridView.setViewName("complat/role/members_list");
		gridView.addQueryParam("roleId", roleId);
		createButton(gridView);
		createHead(gridView);
		if (StringUtil.isEmpty(memberName)) {
			memberName = gridView.getSearchText();
		}
		createBody(gridView, roleId, memberType, memberName);
		gridView.setSearchPlaceholder("请输入成员名称");
		gridView.addObject("roleId", roleId);
		gridView.addObject("memberType", memberType);
		gridView.addObject("memberName", memberName);
		gridView.addObject("orgType", "u,g");
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
		gridView.addButton(Button.getClean());
	}

	/**
	 * 创建表头
	 * 
	 * @param gridView
	 */
	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setCheckbox(true).setField("iid"));
		gridView.addHead(Head.getInstance().setField("name").setTitle("成员名称").setAlign("left")
				.setResizable(true));
	}

	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView, Integer roleId, Integer memberType, String memberName) {
		GridViewSql gridViewSql = GridViewSql.getInstance(gridView);

		gridViewSql
				.addSelectField("r.userid userid")
				.addSelectField("u.name username")
				.addSelectField("r.groupid groupid")
				.addSelectField("g.name groupname")
				.setTable(
						Tables.ROLERELATION + " r LEFT JOIN " + Tables.USER
								+ " u ON r.userid = u.iid LEFT JOIN " + Tables.GROUP
								+ " g ON r.groupid = g.iid");

		StringBuilder where = new StringBuilder();

		if (StringUtil.isNotEmpty(memberName)) {
			where.append("(g.name LIKE :groupname OR u.name LIKE :username) AND ");
			gridViewSql.addParam("groupname", memberName, LikeType.LR);
			gridViewSql.addParam("username", memberName, LikeType.LR);
		}

		switch (memberType) {
		case 1:
			where.append("r.groupid IS NOT NULL AND ");
			break;
		case 2:
			where.append("r.userid IS NOT NULL AND ");
			break;
		default:
			break;
		}

		where.append("r.roleid = :roleid");
		gridViewSql.addParam("roleid", roleId);

		gridViewSql.setWhere(where.toString());

		gridViewSql.addOrderBy("r.iid", "DESC");
		gridViewService.find(gridViewSql);
	}

	@Override
	public void createRow(GridRow gridRow, Map<String, Object> rowData, Integer index) {
		String userId = StringUtil.getString(rowData.get("userid"));
		String userName = StringUtil.getString(rowData.get("username"));
		String groupId = StringUtil.getString(rowData.get("groupid"));
		String groupName = StringUtil.getString(rowData.get("groupname"));

		if (StringUtil.isEmpty(userId)) {
			gridRow.addCell("iid", "g_" + groupId);

			if (PARENT_ID.equals(groupId)) {
				gridRow.addCell("name",
						"<span class=\"group\"><i class=\"iconfont\">&#xf101d;</i>全部机构</span>", false);
			} else {
				gridRow.addCell("name", "<span class=\"group\"><i class=\"iconfont\">&#xf101d;</i>"
						+ groupName + "</span>", false);
			}
		} else {
			gridRow.addCell("iid", "u_" + userId);
			gridRow.addCell("name", "<span class=\"user\"><i class=\"iconfont\">&#xf1006;</i>" + userName
					+ "</span>", false);
		}
	}
}