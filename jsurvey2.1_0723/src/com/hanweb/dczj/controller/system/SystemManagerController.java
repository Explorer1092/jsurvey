package com.hanweb.dczj.controller.system;

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
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/system")
public class SystemManagerController {
	
	@Autowired
	WebSiteService webSiteService;
	
	@Autowired
	GroupService groupService;

	@RequestMapping("list")
	public ModelAndView systemManager() {
		ModelAndView modelAndView = new ModelAndView("dczj/system/systemManager");
		CurrentUser user = UserSessionInfo.getCurrentUser();
		Integer rangeId = user.getRangeId();
		String rangeName = user.getRangeName();
		rangeName = "";
		String nodeName = StringUtil.isEmpty(rangeName) ? "网站管理" : rangeName;
		if (rangeId == null || rangeId < 0) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("webId", "webName");

		tree.addNode(TreeNode.getInstance("0", "", nodeName, "/manager/website/list.do"));

		List<DCZJ_WebSite> websiteList = webSiteService.findChildWebByIid(rangeId);

		for (DCZJ_WebSite webSite : websiteList) {
			tree.addNode(TreeNode.getInstance(webSite.getIid() + "", "0", webSite.getName(),
					"/manager/website/list.do", webSite.getIsParent(), false));
		}
		
		modelAndView.addObject("tree", tree.parse());
		
		// 组织树
		Tree tree_datacall = Tree.getInstance("webId", "webName");

		tree_datacall.addNode(TreeNode.getInstance("0", "", "站点选择", "/manager/website/list.do"));

		List<DCZJ_WebSite> websiteList_datacall = webSiteService.findChildWebByIid(rangeId);

		for (DCZJ_WebSite webSite : websiteList_datacall) {
			tree_datacall.addNode(TreeNode.getInstance(webSite.getIid() + "", "0", webSite.getName(),
					"/manager/datacall/list.do", webSite.getIsParent(), false));
		}

		modelAndView.addObject("user", user);
		modelAndView.addObject("webname", "网站管理");
		modelAndView.addObject("tree_datacall", tree_datacall.parse());
		modelAndView.addObject("webname_datacall", "站点选择");
		return modelAndView;
	}
	
	@RequestMapping("grouplist")
	public ModelAndView listGroup() {
		ModelAndView modelAndView = new ModelAndView("dczj/system/grouplist");
		
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeName = StringUtil.isEmpty(rangeName) ? "机构管理" : rangeName;
		if (rangeId == null || rangeId < 0) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeName));

		List<Group> groupList = groupService.findChildGroupByIid(rangeId);

		for (Group group : groupList) {
			tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
					 group.getIsParent(), false));
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
					 group.getIsParent(), false));
		}

		return tree.parse();
	}
	
	/**
	 * 加载用户管理中 初始机构树
	 * 
	 * @return
	 */
	@RequestMapping("userlist_show")
	public ModelAndView showUserMenu() {
		ModelAndView modelAndView = new ModelAndView("dczj/system/userlist");

		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeName = StringUtil.isEmpty(rangeName) ? "后台用户" : rangeName;
		if (rangeId == null) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");
		tree.addNode(TreeNode.getInstance(rangeId + "", "", nodeName));

		List<Group> groupList = groupService.findChildGroupByIid(NumberUtil.getInt(rangeId));

		for (Group group : groupList) {
			tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
					 group.getIsParent(), false));
		}
		if (currentUser.isSysAdmin()) {
			tree.addNode(TreeNode.getInstance("outside", "user", "前台用户"));
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
							 group.getIsParent(), false)
					.addParam("groupId", group.getIid()).addParam("groupName", group.getName()));
		}

		return tree.parse();
	}
	
	/**
	 * 异步加载站点树
	 * 
	 * @param groupId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("sync_loadtree")
	@ResponseBody
	public String sync_loadtree(int webid, String isDisabled) {
		// 组织树
		Tree tree = Tree.getInstance();

		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(webid);

		for (DCZJ_WebSite web : webList) {
			tree.addNode(TreeNode.getInstance(web.getIid() + "", webid+"", web.getName(),"/manager/website/list.do", web.getIsParent(), false)
					.addParam("webId", web.getIid()).addParam("webName", web.getName()));
		}

		return tree.parse();
	}
	
	/**
	 * 异步加载站点树
	 * 
	 * @param groupId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("sync_loadtree_datacall")
	@ResponseBody
	public String sync_loadtree_datacall(int webid, String isDisabled) {
		// 组织树
		Tree tree = Tree.getInstance();

		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(webid);

		for (DCZJ_WebSite web : webList) {
			tree.addNode(TreeNode.getInstance(web.getIid() + "", webid+"", web.getName(),"/manager/datacall/list.do", web.getIsParent(), false)
					.addParam("webId", web.getIid()).addParam("webName", web.getName()));
		}

		return tree.parse();
	}
}
