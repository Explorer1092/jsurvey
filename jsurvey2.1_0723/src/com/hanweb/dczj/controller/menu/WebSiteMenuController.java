package com.hanweb.dczj.controller.menu;

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
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.support.controller.CurrentUser;

/**
 * 网站相关的 网站树控制器<br>
 * (点击时触发的URL跳转为网站相关)
 * 
 * @author xll
 * 
 */

@Controller
@RequestMapping("manager/menu")
public class WebSiteMenuController {
	
	@Autowired
	WebSiteService webSiteService;
	
	/**
	 * 加载网站管理中 初始网站树(网站管理员有权限修改网站管理，此树需修改)
	 * 
	 * @return
	 */
	@RequestMapping("websitemenu_show")
	public ModelAndView showGroupMenu() {
		ModelAndView modelAndView = new ModelAndView("/dczj/website/website_menu");

		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		rangeId = 0;
		String rangeName = currentUser.getRangeName();
		rangeName = "";
		String nodeName = StringUtil.isEmpty(rangeName) ? "网站管理" : rangeName;
		if (rangeId == null || rangeId < 0) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("webId", "webName");

		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeName, "/manager/website/list.do"));

		List<DCZJ_WebSite> websiteList = webSiteService.findChildWebByIid(rangeId);

		for (DCZJ_WebSite webSite : websiteList) {
			tree.addNode(TreeNode.getInstance(webSite.getIid() + "", rangeId + "", webSite.getName(),
					"/manager/website/list.do", webSite.getIsParent(), false));
		}

		modelAndView.addObject("tree", tree.parse());

		return modelAndView;
	}

	/**
	 * 异步加载机构树
	 * 
	 * @param groupId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuwithurlforweb_search")
	@ResponseBody
	public String searchAsyncMenuWithUrl(int webId, String isDisabled) {

		// 组织树
		Tree tree = Tree.getInstance("webId", "webName");

		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(webId);

		for (DCZJ_WebSite webSite : webList) {
			tree.addNode(TreeNode.getInstance("" + webSite.getIid(), "" + webId, webSite.getName(),
					"/manager/website/list.do", webSite.getIsParent(), false));
		}

		return tree.parse();
	}

	/**
	 * 异步加载机构树
	 * 
	 * @param groupId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuforwebsearch")
	@ResponseBody
	public String searchAsyncMenu(int webId, String isDisabled) {

		// 组织树
		Tree tree = Tree.getInstance("webId", "webName");

		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(webId);

		for (DCZJ_WebSite webSite : webList) {
			tree.addNode(TreeNode.getInstance("" + webSite.getIid(), "" + webId, webSite.getName(),
					"", webSite.getIsParent(), false));
		}

		return tree.parse();
	}

	
	/**
	 * 网站编辑时 加载父网站树
	 * 
	 * @param webId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @param currentId
	 *            所在操作页面的网站ID
	 * @return
	 */
	@RequestMapping("menuforweb_search")
	@ResponseBody
	public String searchAsyncMenuForGroup(Integer webId, String isDisabled, String currentId) {
		webId = NumberUtil.getInt(webId);
		int temp = webId;
		// 组织树
		Tree tree = Tree.getInstance();
		TreeNode treeNode = null;

		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		if (webId == 0 && currentUser.isGroupAdmin()) {
			webId = currentUser.getRangeId();
		}
		if (webId == 0 && !currentUser.isGroupAdmin() &&!currentUser.isSysAdmin()) {
			webId = currentUser.getGroupId();
		}
		List<DCZJ_WebSite> webSiteList = webSiteService.findChildWebByIid(webId);
		for (DCZJ_WebSite webSite : webSiteList) {
			if (BooleanUtils.toBoolean(isDisabled)
					|| webSite.getIid() == NumberUtil.getInt(currentId)) {
				treeNode = TreeNode.getInstance(webSite.getIid() + "", webId + "", webSite.getName())
						.setIsParent(webSite.getIsParent()).setIsDisabled(true); // 机构不能选择自身及其下属机构为父机构
			} else {
				treeNode = TreeNode.getInstance(webSite.getIid() + "", webId + "", webSite.getName())
						.setIsParent(webSite.getIsParent());
			}
			tree.addNode(treeNode);
		}

		if (temp == 0 && currentUser.isGroupAdmin()) {
			boolean isParent = treeNode != null;
			treeNode = TreeNode.getInstance(webId + "", "", currentUser.getRangeName())
					.setIsParent(isParent);
			tree.addNode(treeNode);
		}

		return tree.parse();
	}
	
	/**
	 * 用户编辑时 加载网站树
	 * 
	 * @param webId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuforuseredit_search")
	@ResponseBody
	public String searchAsyncMenuForUser(String webId, String isDisabled) {
		// 组织树
		Tree tree = Tree.getInstance();
		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(NumberUtil.getInt(webId));
		for (DCZJ_WebSite web : webList) {
			tree.addNode(TreeNode.getInstance(web.getIid() + "", NumberUtil.getInt(web) + "",
					web.getName()).setIsParent(web.getIsParent()));
		}

		return tree.parse();
	}
}
