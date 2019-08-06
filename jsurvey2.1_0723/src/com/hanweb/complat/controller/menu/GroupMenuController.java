package com.hanweb.complat.controller.menu;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
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
import com.hanweb.complat.service.UserService;
import com.hanweb.support.controller.CurrentUser;

/**
 * 机构相关的 机构树控制器<br>
 * (点击时触发的URL跳转为机构相关)
 * 
 * @author ZhangC
 * 
 */
@Controller
@RequestMapping("manager/menu")
public class GroupMenuController {

	@Autowired
	GroupService groupService;

	@Autowired
	UserService userService;

	/**
	 * 加载机构管理中 初始机构树
	 * 
	 * @return
	 */
	@RequestMapping("groupmenu_show")
	public ModelAndView showGroupMenu() {
		ModelAndView modelAndView = new ModelAndView("/complat/group/group_menu");

		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeName = StringUtil.isEmpty(rangeName) ? "机构管理" : rangeName;
		if (rangeId == null || rangeId < 0) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeName, "/manager/group/list.do"));

		List<Group> groupList = groupService.findChildGroupByIid(rangeId);

		for (Group group : groupList) {
			tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
					"/manager/group/list.do", group.getIsParent(), false));
		}

		modelAndView.addObject("tree", tree.parse());

		return modelAndView;
	}

	/**
	 * 异步加载机构树
	 * 
	 * @param groupId
	 *            机构ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuwithurlforgroup_search")
	@ResponseBody
	public String searchAsyncMenuWithUrl(int groupId, String isDisabled) {

		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		List<Group> groupList = groupService.findChildGroupByIid(groupId);

		for (Group group : groupList) {
			tree.addNode(TreeNode.getInstance("" + group.getIid(), "" + groupId, group.getName(),
					"/manager/group/list.do", group.getIsParent(), false));
		}

		return tree.parse();
	}

	/**
	 * 机构编辑时 加载父机构树
	 * 
	 * @param groupId
	 *            机构ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @param currentId
	 *            所在操作页面的机构ID
	 * @return
	 */
	@RequestMapping("menuforgroup_search")
	@ResponseBody
	public String searchAsyncMenuForGroup(Integer groupId, String isDisabled, String currentId) {
		groupId = NumberUtil.getInt(groupId);
		int temp = groupId;

		// 组织树
		Tree tree = Tree.getInstance();
		TreeNode treeNode = null;

		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		if (groupId == 0 && currentUser.isGroupAdmin()) {
			groupId = currentUser.getRangeId();
		}

		List<Group> groupList = groupService.findChildGroupByIid(groupId);
		for (Group group : groupList) {
			if (BooleanUtils.toBoolean(isDisabled)
					|| group.getIid() == NumberUtil.getInt(currentId)) {
				// 机构不能选择自身及其下属机构为父机构
				treeNode = TreeNode.getInstance(group.getIid() + "", groupId + "", group.getName())
						.setIsParent(group.getIsParent()).setIsDisabled(true); 
			} else {
				treeNode = TreeNode.getInstance(group.getIid() + "", groupId + "", group.getName())
						.setIsParent(group.getIsParent());
			}
			tree.addNode(treeNode);
		}

		if (temp == 0 && currentUser.isGroupAdmin()) {
			boolean isParent = treeNode != null;
			treeNode = TreeNode.getInstance(groupId + "", "", currentUser.getRangeName())
					.setIsParent(isParent);
			tree.addNode(treeNode);
		}

		return tree.parse();
	}

	/**
	 * 用户编辑时 加载机构树
	 * 
	 * @param groupId
	 *            机构ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuforuser_search")
	@ResponseBody
	public String searchAsyncMenuForUser(String groupId, String isDisabled) {
		// 组织树
		Tree tree = Tree.getInstance();
		List<Group> groupList = groupService.findChildGroupByIid(NumberUtil.getInt(groupId));
		for (Group group : groupList) {
			tree.addNode(TreeNode.getInstance(group.getIid() + "", NumberUtil.getInt(groupId) + "",
					group.getName()).setIsParent(group.getIsParent()));
		}

		return tree.parse();
	}

}
