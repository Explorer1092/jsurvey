package com.hanweb.dczj.controller.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebSiteService;

@Controller
@RequestMapping("manager/dczjmenu")
public class DczjMenu {

	@Autowired
	WebSiteService webSiteService;
	
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
	public String searchAsyncMenuWithUrl(int webid, String isDisabled) {
//		isDisabled = StringUtil.getSafeString(isDisabled);
		// 组织树
		Tree tree = Tree.getInstance();

		List<DCZJ_WebSite> webList = webSiteService.findChildWebByIid(webid);

		for (DCZJ_WebSite web : webList) {
			tree.addNode(TreeNode.getInstance(web.getIid() + "", webid+"", web.getName(),"/manager/dczj/titlelist.do", web.getIsParent(), false)
					.addParam("webid", web.getIid()).addParam("webName", web.getName()));
		}

		return tree.parse();
	}
}
