package com.hanweb.complat.controller.onlineuser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.view.grid.Button;
import com.hanweb.common.view.grid.GridRow;
import com.hanweb.common.view.grid.GridView;
import com.hanweb.common.view.grid.Head;
import com.hanweb.complat.listener.OnlineUserListener;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.RoleService;
import com.hanweb.support.controller.CurrentUser;

/**
 * 在线用户列表控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@RequestMapping("manager/onlineuser")
public class ListOnlineUserController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	GroupService groupService;

	@RequestMapping("list")
	public synchronized GridView list(GridView gridView) {
		gridView.setViewName("complat/onlineuser/onlineuser_list");
		createButton(gridView);
		createHead(gridView);
		createBody(gridView, gridView.getSearchText());
		gridView.setShowAdvSearch(false);
		gridView.setShowSimpleSearch(false);
		gridView.setPosition("在线用户管理");
		gridView.setSearchPlaceholder("请输入用户登录名");
		return gridView;
	}

	/**
	 * 创建按钮
	 * 
	 * @param gridView
	 */
	private void createButton(GridView gridView) {
		gridView.addButton(Button.getReload());
	}

	/**
	 * 创建表头
	 * 
	 * @param gridView
	 */
	private void createHead(GridView gridView) {
		gridView.addHead(Head.getInstance().setField("loginname").setTitle("登录名").setAlign("left")
				.setWidth(100).setResizable(true));
		gridView.addHead(Head.getInstance().setField("name").setTitle("姓名").setAlign("left")
				.setWidth(100).setResizable(true).setFixed(true));
		gridView.addHead(Head.getInstance().setField("groupname").setTitle("所属机构").setAlign("left")
				.setWidth(200).setFixed(true));
		gridView.addHead(Head.getInstance().setField("accessip").setTitle("访问IP").setAlign("left")
				.setWidth(200).setFixed(true));
		gridView.addHead(Head.getInstance().setField("accesstime").setTitle("登录时间")
				.setAlign("center").setWidth(80).setFixed(true));
		if (RoleService.isSysAdmin()) {
			gridView.addHead(Head.getInstance().setField("kick").setTitle("操作")
					.setAlign("center").setWidth(80).setFixed(true));
		}

	}

	/**
	 * 创建列表
	 * 
	 * @param gridView
	 */
	private void createBody(GridView gridView, String keyword) {
		if (OnlineUserListener.ONLINE_ENTITIES.size() == 0) {
			return;
		} else {
			// 获得总数量
			gridView.setTotal(OnlineUserListener.ONLINE_ENTITIES.size()); 
		}
		keyword = keyword == null || "".equals(keyword) ? gridView.getSearchText() : keyword;
		// 在线用户列表
		List<CurrentUser> datas = getDatas(keyword, gridView.getPageNumber(),
				gridView.getPageSizeWithDefault()); 
		// 获得当前用户的sessionid
		String curSessionId = SpringUtil.getRequest().getSession().getId(); 

		if (datas == null || datas.size() == 0 || StringUtil.isEmpty(curSessionId)) {
			return;
		}

		GridRow row = null;

		String sessionid = null;
		String loginName = null;
		String name = null;
		Integer groupId = null;
		String groupName = null;
		String accessIp = null;
		String accessTime = null;
		HashMap<Integer, String> groupMap = new HashMap<Integer, String>(5);

		for (Iterator<CurrentUser> iterator = datas.iterator(); iterator.hasNext();) {
			CurrentUser onlineUser = null;
			try {
				onlineUser = iterator.next();
			} catch (Exception e) {
				logger.warn("在线用户列表部分数据冲突，请再次刷新");
				break;
			}
			row = new GridRow();

			groupId = onlineUser.getGroupId();
			groupName = groupMap.get(groupId);
			groupName = groupName == null ? groupService.findNameByIid(groupId) : groupName;

			sessionid = onlineUser.getSessionId();
			loginName = StringUtil.getString(onlineUser.getLoginName());
			name = StringUtil.getString(onlineUser.getName());
			groupName = StringUtil.getString(groupName);
			accessIp = StringUtil.getString(onlineUser.getIp());
			accessTime = FastDateFormat.getInstance("HH:mm").format(
					onlineUser.getAccessTime());

			row.addCell("loginname", loginName);
			row.addCell("name", name);
			row.addCell("groupname", groupName);
			row.addCell("accessip", accessIp);
			row.addCell("accesstime", accessTime);
			// 只有系统管理员才可以踢人，不能踢自己
			if (RoleService.isSysAdmin() && !curSessionId.equals(sessionid)) {
				row.addCell("kick", "<a href=\"javascript:;\" class=\"link\">踢出</a>", Script.createScript("kickUser", sessionid), false);
			} else {
				row.addCell("kick", "");
			}
			gridView.addRow(row);
		}
	}

	private List<CurrentUser> getDatas(String keyword, int pageNo, int pageSize) {
		List<CurrentUser> onlineUserList = null;
		int fromIndex = (pageNo - 1) * pageSize;
		int toIndex = pageNo * pageSize;
		int size = OnlineUserListener.ONLINE_ENTITIES.size();
		if (size == 0) {
			return null;
		} else {
			if (size < toIndex) {
				toIndex = size;
			}
			onlineUserList = OnlineUserListener.ONLINE_ENTITIES.subList(fromIndex, toIndex);
		}
		return onlineUserList;
	}
}
