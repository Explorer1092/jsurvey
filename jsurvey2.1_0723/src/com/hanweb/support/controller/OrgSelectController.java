package com.hanweb.support.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.Constants;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.RoleService;
import com.hanweb.complat.service.UserService;

/**
 * 对象选择器
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping(value = "manager")
public class OrgSelectController {
	@Autowired
	GroupService groupService;

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@RequestMapping("orgselect")
	public ModelAndView showMainPage(String orgType) {
		ModelAndView modelAndView = new ModelAndView("support/orgselect/orgselect");
		modelAndView.addObject("orgType", orgType);

		return modelAndView;
	}
	
	/**
	 * 有序的
	 * @param orgType
	 * @return
	 */
	@RequestMapping("orgselect_order")
	public ModelAndView showMainPageWidthOrder(String orgType) {
		ModelAndView modelAndView = new ModelAndView("support/orgselect/orgselect_order");
		modelAndView.addObject("orgType", orgType);

		return modelAndView;
	}

	/**
	 * 选择机构，ajax加载机构数据
	 * 
	 * @param id
	 *            选中机构ID
	 * @return
	 */
	@RequestMapping("group_load")
	@ResponseBody
	public String loadGroupData(String id) {
		List<HashMap<String, Object>> groupNodes = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> groupNode = null;
		Integer groupId = NumberUtil.getInt(id);
		List<Group> groupList = groupService.findChildGroupByIid(groupId);

		String text;
		for (Group group : groupList) {
			groupNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			groupNode.put("id", group.getIid());
			text = "<span class='optionname'>" + group.getName() + "</span>";
			if (group.getCodeId() != null && group.getCodeId().length() > 0) {
				text += " &lt;<span class='ic'>" + group.getCodeId() + "</span>&gt;";
			}
			groupNode.put("text", text);
			groupNode.put("state", group.getIsParent() ? "closed" : "");
			groupNodes.add(groupNode);
		}

		if (groupId == 0) {
			groupNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			groupNode.put("id", groupId);
			groupNode.put("text", "<span class='optionname'>全部机构</span>");
			groupNode.put("children", groupNodes);

			groupNodes = new ArrayList<HashMap<String, Object>>();
			groupNodes.add(groupNode);
		}

		return JsonUtil.objectToString(groupNodes);
	}

	/**
	 * 选择机构及用户，ajax加载机构及用户数据
	 * 
	 * @param id
	 *            机构选中机构ID
	 * @return
	 */
	@RequestMapping("user_load")
	@ResponseBody
	public String loadUserData(String id) {
		Map<String, Object> allNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);

		List<HashMap<String, Object>> groupNodes = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> userNodes = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> groupNode = null;
		HashMap<String, Object> userNode = null;

		Integer pid = groupService.findPidById(NumberUtil.getInt(id));
		List<Group> groupList = groupService.findChildGroupByIid(NumberUtil.getInt(id));

		for (Group group : groupList) {
			groupNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			groupNode.put("id", group.getIid());
			groupNode.put("text", group.getName());
			groupNode.put("ic", group.getCodeId());
			groupNodes.add(groupNode);
		}

		if (NumberUtil.getInt(id) != 0) {
			List<User> userList = userService.findByGroupIds(id);
			for (User user : userList) {
				userNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
				userNode.put("id", user.getIid());
				userNode.put("text", user.getName());
				userNode.put("ic", user.getLoginName());
				userNodes.add(userNode);
			}
		}

		allNode.put("id", id);
		allNode.put("pid", pid);
		allNode.put("groups", groupNodes);
		allNode.put("users", userNodes);

		return JsonUtil.objectToString(allNode);
	}

	/**
	 * 选择角色，ajax加载角色数据
	 * 
	 * @return
	 */
	@RequestMapping("role_load")
	@ResponseBody
	public String loadRoleData() {
		Map<String, Object> allNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
		List<HashMap<String, Object>> roleNodes = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> roleNode = null;

		List<Role> roleList = roleService.findAllRoles();

		for (Role role : roleList) {
			roleNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			roleNode.put("id", role.getIid());
			roleNode.put("text", role.getName());
			roleNodes.add(roleNode);
		}
		allNode.put("roles", roleNodes);

		return JsonUtil.objectToString(allNode);
	}

	/**
	 * 选择机构，检索机构数据
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping("group_search")
	@ResponseBody
	public String searchGroupDate(String keyword) {
		if (StringUtil.isEmpty(keyword)) {
			return "";
		}

		Map<String, Object> allNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
		List<HashMap<String, Object>> groupNodes = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> groupNode = null;

		List<Group> groupList = groupService.findByNameOrPinYin(keyword);
		for (Group group : groupList) {
			groupNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			groupNode.put("id", group.getIid());
			groupNode.put("text", group.getName());
			groupNode.put("ic", group.getCodeId());
			groupNodes.add(groupNode);
		}
		allNode.put("groups", groupNodes);

		return JsonUtil.objectToString(allNode);
	}

	/**
	 * 选择机构及用户，检索机构及用户数据
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping("user_search")
	@ResponseBody
	public String searchUserDate(String keyword) {
		if (StringUtil.isEmpty(keyword)) {
			return "";
		}
		Map<String, Object> allNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);

		List<HashMap<String, Object>> groupNodes = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> userNodes = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> groupNode = null;
		HashMap<String, Object> userNode = null;

		List<Group> groupList = groupService.findByNameOrPinYin(keyword);
		for (Group group : groupList) {
			groupNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			groupNode.put("id", group.getIid());
			groupNode.put("text", group.getName());
			groupNode.put("ic", group.getCodeId());
			groupNodes.add(groupNode);
		}

		List<User> userList = userService.findByNameOrPinYin(keyword);
		for (User user : userList) {
			userNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			userNode.put("id", user.getIid());
			userNode.put("text", user.getName());
			userNode.put("ic", user.getLoginName());
			userNodes.add(userNode);
		}

		allNode.put("groups", groupNodes);
		allNode.put("users", userNodes);

		return JsonUtil.objectToString(allNode);
	}

	/**
	 * 选择角色，检索角色数据
	 * 
	 * @param keyword
	 * @return
	 */
	@RequestMapping("role_search")
	@ResponseBody
	public String searchRoleDate(String keyword) {
		if (StringUtil.isEmpty(keyword)) {
			return "";
		}

		Map<String, Object> allNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
		List<HashMap<String, Object>> roleNodes = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> roleNode = null;
		List<Role> roleList = roleService.findByNameOrPinYin(keyword);

		for (Role role : roleList) {
			roleNode = new HashMap<String, Object>(Constants.MAP_INIT_SIZE);
			roleNode.put("id", role.getIid());
			roleNode.put("text", role.getName());
			roleNodes.add(roleNode);
		}

		allNode.put("roles", roleNodes);

		return JsonUtil.objectToString(allNode);
	}
}
