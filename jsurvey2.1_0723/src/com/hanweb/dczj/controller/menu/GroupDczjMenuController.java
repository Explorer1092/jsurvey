package com.hanweb.dczj.controller.menu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.UserService;
import com.hanweb.dczj.entity.LimitLoginUser;
import com.hanweb.dczj.entity.LimitOpenUser;
import com.hanweb.dczj.service.LimitLoginUserService;
import com.hanweb.dczj.service.LimitOpenUserService;

@Controller
@RequestMapping("manager/dczj/menu")
public class GroupDczjMenuController {
	
	@Autowired
	GroupService groupService;

	@Autowired
	UserService userService;
	
	@Autowired
	LimitLoginUserService limitLoginUserService;
	@Autowired
	LimitOpenUserService limitOpenUserService;
	
	@RequestMapping("menulimituserwithurlforgroup_search")
	@ResponseBody
	public String searchAsyncMenuWithUrl(int groupId, String isDisabled, String dczjid) {
		
		LimitLoginUser limitLoginUser = limitLoginUserService.findEntityBydczjid(dczjid,0);
		
		String limituds = "";
		if(limitLoginUser!=null && StringUtil.isNotEmpty(limitLoginUser.getLimitids())) {
			limituds = limitLoginUser.getLimitids();
			limitLoginUser.setGroups(JsonUtil.objectToString(groupService.findByIds(limituds)));
		}else {
			limitLoginUser = new LimitLoginUser();
			limitLoginUser.setGroups(JsonUtil.objectToString(new ArrayList<Group>()));
		}
		
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		List<Group> groupList = groupService.findChildGroupByIid(groupId);

		for (Group group : groupList) {//3
			if(StringUtil.isNotEmpty(limituds)) {
				int count = 0; 
				String[] ids = limituds.split(",");//2
				for(int i = 0; i < ids.length; i++) {
					if(NumberUtil.getInt(ids[i]) == NumberUtil.getInt(group.getIid())) {
						tree.addNode(TreeNode.getInstance("" + NumberUtil.getInt(group.getIid()), "" + groupId, group.getName(),
								"/manager/group/list.do", group.getIsParent(), false).setChecked(true));
						count++;
						break;
					}
					count = 0;
				}
				if(count == 0) {
					tree.addNode(TreeNode.getInstance("" + NumberUtil.getInt(group.getIid()), "" + groupId, group.getName(),
							"/manager/group/list.do", group.getIsParent(), false));
				}
			}else {
				tree.addNode(TreeNode.getInstance("" + NumberUtil.getInt(group.getIid()), "" + groupId, group.getName(),
						"/manager/group/list.do", group.getIsParent(), false));
			}
			
		}

		return tree.parse();
	}
	
	
	@RequestMapping("menulimitopenuserwithurlforgroup_search")
	@ResponseBody
	public String searchOpenAsyncMenuWithUrl(int groupId, String isDisabled, String dczjid) {
		
		LimitOpenUser limitOpenUser = limitOpenUserService.findEntityBydczjid(dczjid);
		String limituds = "";
		if(limitOpenUser!=null && StringUtil.isNotEmpty(limitOpenUser.getLimitids())) {
			limituds = limitOpenUser.getLimitids();
			limitOpenUser.setGroups(JsonUtil.objectToString(groupService.findByIds(limituds)));
		}else {
			limitOpenUser = new LimitOpenUser();
			limitOpenUser.setGroups(JsonUtil.objectToString(new ArrayList<Group>()));
		}
		
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		List<Group> groupList = groupService.findChildGroupByIid(groupId);

		for (Group group : groupList) {//3
			if(StringUtil.isNotEmpty(limituds)) {
				int count = 0; 
				String[] ids = limituds.split(",");//2
				for(int i = 0; i < ids.length; i++) {
					if(NumberUtil.getInt(ids[i]) == NumberUtil.getInt(group.getIid())) {
						tree.addNode(TreeNode.getInstance("" + NumberUtil.getInt(group.getIid()), "" + groupId, group.getName(),
								"/manager/group/list.do", group.getIsParent(), false).setChecked(true));
						count++;
						break;
					}
					count = 0;
				}
				if(count == 0) {
					tree.addNode(TreeNode.getInstance("" + NumberUtil.getInt(group.getIid()), "" + groupId, group.getName(),
							"/manager/group/list.do", group.getIsParent(), false));
				}
			}else {
				tree.addNode(TreeNode.getInstance("" + NumberUtil.getInt(group.getIid()), "" + groupId, group.getName(),
						"/manager/group/list.do", group.getIsParent(), false));
			}
			
		}

		return tree.parse();
	}
}
