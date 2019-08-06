package com.hanweb.complat.controller.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.GroupService;
import com.hanweb.support.controller.CurrentUser;

/**
 * 用户相关的 机构树控制器<br>
 * (点击时触发的URL跳转为用户相关)
 * 
 * @author ZhangC
 * 
 */
@Controller
@RequestMapping("manager/menu")
public class UserMenuController {

	@Autowired
	GroupService groupService;

	/**
	 * 加载用户管理中 初始机构树
	 * 
	 * @return
	 */
	@RequestMapping("usermenu_show")
	public ModelAndView showUserMenu() {
		ModelAndView modelAndView = new ModelAndView("/complat/user/user_menu");

		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeName = StringUtil.isEmpty(rangeName) ? "后台用户" : rangeName;
		if (rangeId == null) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");
		tree.addNode(TreeNode.getInstance(rangeId + "", "", nodeName, "/manager/user/list.do"));

		List<Group> groupList = groupService.findChildGroupByIid(NumberUtil.getInt(rangeId));

		for (Group group : groupList) {
			tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
					"/manager/user/list.do", group.getIsParent(), false));
		}
		if (currentUser.isSysAdmin()) {
			tree.addNode(TreeNode.getInstance("outside", "user", "前台用户",
					"/manager/outsideuser/list.do"));
		}

		modelAndView.addObject("tree", tree.parse());

		return modelAndView;
	}

	/**
	 * 异步加载所需的机构树
	 * 
	 * @param groupId
	 *            机构ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuwithurlforuser_search")
	@ResponseBody
	public String searchAsyncMenuWithUrl(String groupId, String isDisabled) {

		// 组织树
		Tree tree = Tree.getInstance();

		List<Group> groupList = groupService.findChildGroupByIid(NumberUtil.getInt(groupId));

		for (Group group : groupList) {
			tree.addNode(TreeNode
					.getInstance(group.getIid() + "", groupId, group.getName(),
							"/manager/user/list.do", group.getIsParent(), false)
					.addParam("groupId", group.getIid()).addParam("groupName", group.getName()));
		}

		return tree.parse();
	}

}
