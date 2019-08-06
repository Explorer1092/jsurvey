package com.hanweb.support.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebManagerService;
import com.hanweb.dczj.service.WebSiteService;

@Controller
@RequestMapping(value = "manager")
public class MainFrameController {
	
	@Autowired
	WebSiteService webSiteService;
	@Autowired
	WebManagerService webManagerService;
	
	@RequestMapping("index")
	public ModelAndView showMainPage() {
		ModelAndView modelAndView = new ModelAndView("dczj/mainpage/homepage");	
		CurrentUser user = UserSessionInfo.getCurrentUser();
		String rangeName = user.getRangeName();
		List<Role> currentRoles = user.getRoleList();
		rangeName = "";
		boolean ishave = false;// 判断树是否生成
		int surveynum = 0;
		String webName = "";		
		int isSysManager = 0;
		// 组织树
		Tree tree = Tree.getInstance("webid", "webName");
		
		List<DCZJ_WebSite> webList = null;
		for (Role role : currentRoles) {
			switch (role.getIid()) {
			case 1: //系统管理员
				String nodeName = StringUtil.isEmpty(rangeName) ? "站点选择" : rangeName;
				webList = webSiteService.findChildWebByIid(0);
				
				tree.addNode(TreeNode.getInstance(0 + "", "", nodeName));
				for (DCZJ_WebSite web : webList) {
					tree.addNode(TreeNode.getInstance(web.getIid() + "", 0 + "", web.getName(),"/manager/dczj/titlelist.do", web.getIsParent(), false));
				}
				surveynum++;
				isSysManager=1;
				break;
			case 2://网站管理员
				if(surveynum == 0){
					List<Integer> webIdsList = webManagerService.findWebIdsByUserId(user.getIid());
					tree.addNode(TreeNode.getInstance(0 + "", "", "站点选择",""));
					for (Integer webid : webIdsList) {
						webName = webSiteService.findWebNameByWebId(webid);
						tree.addNode(TreeNode.getInstance(webid + "", 0 + "", webName,"/manager/dczj/titlelist.do", false, false));
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
		modelAndView.addObject("webname", "站点选择");
		modelAndView.addObject("user", user);
		modelAndView.addObject("isSysManager", isSysManager);
		return modelAndView;
	}
}
