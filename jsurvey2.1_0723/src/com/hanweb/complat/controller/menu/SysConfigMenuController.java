package com.hanweb.complat.controller.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;

/**
 * 系统设置
 * 
 * @author 李杰
 * 
 */
@Controller
@Permission()
@RequestMapping("manager/menu")
public class SysConfigMenuController {

	/**
	 * 系统设置
	 * 
	 * @return
	 */
	@RequestMapping("config_show")
	public ModelAndView showGroupMenu() {
		ModelAndView modelAndView = new ModelAndView("/complat/configuration/config_menu");
		// 组织树
		Tree tree = Tree.getInstance();
		tree.addNode(TreeNode.getInstance("sysconfig", null, "系统设置"));
		tree.addNode(TreeNode.getInstance("config", "sysconfig", "参数设置", "/manager/configuration/modify_show.do"));
		tree.addNode(TreeNode.getInstance("email", "sysconfig", "邮件设置", "/manager/configuration/email/modify_show.do"));
		tree.addNode(TreeNode.getInstance("banlist", "sysconfig", "封停管理", "/manager/banlist/list.do"));
		tree.addNode(TreeNode.getInstance("dbpool", "sysconfig", "数据库连接", "/manager/system/dbpool/info.do"));
		tree.addNode(TreeNode.getInstance("sysinfo", "sysconfig", "线程", "/manager/system/info.do"));
		tree.addNode(TreeNode.getInstance("log", "sysconfig", "日志设置", "/manager/configuration/log/info.do"));
		modelAndView.addObject("tree", tree.parse());
		return modelAndView;
	}
}
