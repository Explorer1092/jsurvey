package com.hanweb.dczj.controller.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.GroupService;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebManagerService;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/jactmenu")
public class DataCallMenu {

	@Autowired
	GroupService groupService;
	
	@Autowired
	WebSiteService webSiteService;
	
	@Autowired
	WebManagerService webManagerService;
	
	@RequestMapping("menu")
	public ModelAndView showMenu(Integer webId){
		ModelAndView modelAndView = new ModelAndView("dczj/jact/jact_menu");
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		List<Role> currentRoles = currentUser.getRoleList();
		String rangeName = currentUser.getRangeName();
		String webName = "";
		boolean ishave = false;// 判断树是否生成
		// 组织树
		Tree tree = Tree.getInstance("webId", "webName");
		List<DCZJ_WebSite> webList = null;
		int surveynum = 0;
		for (Role role : currentRoles) {
			switch (role.getIid()) {
			case 1: //系统管理员
				String nodeName = StringUtil.isEmpty(rangeName) ? "数据调用" : rangeName;
				webList = webSiteService.findChildWebByIid(0);
				
				tree.addNode(TreeNode.getInstance(0 + "", "", nodeName, "/manager/jact/list.do"));
				for (DCZJ_WebSite web : webList) {
					tree.addNode(TreeNode.getInstance(web.getIid() + "", 0 + "", web.getName(),
							"/manager/jact/list.do", web.getIsParent(), false));
				}
				surveynum++;
				break;
			case 3://网站管理员
				if(surveynum == 0){
					List<Integer> webIdsList = webManagerService.findWebIdsByUserId(currentUser.getIid());
					tree.addNode(TreeNode.getInstance(0 + "", "", "数据调用",
							"/manager/jact/list.do"));
					for (Integer webid : webIdsList) {
						webName = webSiteService.findWebNameByWebId(webid);
						tree.addNode(TreeNode.getInstance(webid + "", 0 + "", webName,
								"/manager/jact/list.do", false, false));
					}
				}
				ishave = true;// 判断树是否已经生成
				break;
				
			}
			if (ishave) {// 避免生成2次树
				break;
			}
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
	@RequestMapping("menuwithurlforuser_search")
	@ResponseBody
	public String searchAsyncMenuWithUrl(int webId, String isDisabled) {
		// 组织树
		Tree tree = Tree.getInstance();

		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(webId);

		for (DCZJ_WebSite web : webList) {
			tree.addNode(TreeNode
					.getInstance(web.getIid() + "", webId+"", web.getName(),"/manager/jact/list.do", web.getIsParent(), false)
					.addParam("webId", web.getIid()).addParam("webName", web.getName()));
		}

		return tree.parse();
	}
}
