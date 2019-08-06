package com.hanweb.dczj.controller.setting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.RoleService;
import com.hanweb.complat.service.UserService;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.LimitLoginUser;
import com.hanweb.dczj.entity.LimitOpenUser;
import com.hanweb.dczj.service.LimitLoginUserService;
import com.hanweb.dczj.service.LimitOpenUserService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/setting")
public class SettingController {
	
	@Autowired
	SettingService settingService;
	@Autowired
	GroupService groupService;
	@Autowired
	LimitLoginUserService limitLoginUserService;
	@Autowired
	LimitOpenUserService limitOpenUserService;
	@Autowired
	TitleInfoService titleInfoService;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	
	
	
	@RequestMapping("page_show")
	public ModelAndView showPage(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/setting/setting");
		Dczj_Setting dczj_Setting = settingService.getEntityBydczjid(dczjid);
		if(dczj_Setting==null){
			settingService.insertEntity(dczjid);
			dczj_Setting = settingService.getEntityBydczjid(dczjid);
		}
		modelAndView.addObject("setting", dczj_Setting);
		modelAndView.addObject("dczjid", dczjid);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("modifysetting")
	public JsonResult modifySetting(String name,String value,Integer dczjid,Integer type) {
		JsonResult jsonResult = JsonResult.getInstance();
		int flag = settingService.modify(name, value, dczjid, type);
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		String userIp = IpUtil.getIp();
		LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
				.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("将jsurveyid为"+dczjid+"的设参表属性"+name+"修改为"+value));
		if(flag>0) {
			jsonResult.setSuccess(true);
			Dczj_Setting setting = settingService.getEntityBydczjid(dczjid+"");
			Cache cache = CacheManager.getInstance("jsurvey");
			cache.put("configEnformid"+dczjid, setting);
		}else {
			jsonResult.setSuccess(false).setMessage("保存失败，请联系系统管理员。");
		}
		return jsonResult;
	}
	
	@RequestMapping("limit_user")
	public ModelAndView limitUser(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/setting/limituser");
		
		LimitLoginUser limitLoginUser = limitLoginUserService.findEntityBydczjid(dczjid,0);
		String limituds = "";
		if(limitLoginUser!=null && StringUtil.isNotEmpty(limitLoginUser.getLimitids())) {
			limituds = limitLoginUser.getLimitids();
			limitLoginUser.setGroups(JsonUtil.objectToString(groupService.findByIds(limituds)));
		}else {
			limitLoginUser = new LimitLoginUser();
			limitLoginUser.setGroups(JsonUtil.objectToString(new ArrayList<Group>()));
		}
		
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeName = StringUtil.isEmpty(rangeName) ? "机构选择" : rangeName;

		if (rangeId == null || rangeId < 0) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeName).setNocheck(true));
		List<Group> groupList = groupService.findChildGroupByIid(rangeId);
		for (Group group : groupList) {//3
			if(StringUtil.isNotEmpty(limituds)) {
				int count = 0; 
				String[] ids = limituds.split(",");//2
				for(int i = 0; i < ids.length; i++) {
					if(NumberUtil.getInt(ids[i]) == NumberUtil.getInt(group.getIid())) {
						tree.addNode(TreeNode.getInstance(NumberUtil.getInt(group.getIid()) + "", rangeId + "", group.getName(),
								 group.getIsParent(), false).setHalfCheck(false).setChecked(true));
						count++;
						break;
					}
					count = 0;
				}
				if(count == 0) {
					tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
						 group.getIsParent(), false).setHalfCheck(false));
				}
			}else {
				tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
						 group.getIsParent(), false).setHalfCheck(false));
			}
			
		}
		
		modelAndView.addObject("tree", tree.parse());
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("limitLoginUser", limitLoginUser);
		return modelAndView;
	}
	
	@RequestMapping("findAllUser")
	@ResponseBody
	public JsonResult findAllUser(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		LimitLoginUser limitLoginUser = limitLoginUserService.findEntityBydczjid(dczjid,1);
		String limituds = "";
		if(limitLoginUser!=null && StringUtil.isNotEmpty(limitLoginUser.getLimitids())) {
			limituds = limitLoginUser.getLimitids();
			limitLoginUser.setUsers(JsonUtil.objectToString(userService.findByIds(limituds)));
		}else {
			limitLoginUser = new LimitLoginUser();
			limitLoginUser.setUsers(JsonUtil.objectToString(new ArrayList<User>()));
		}
		result.setSuccess(true).addParam("limitLoginUser", limitLoginUser.getUsers());
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeNameuser = StringUtil.isEmpty(rangeName) ? "用户选择" : rangeName;
		Tree tree = Tree.getInstance("groupId", "groupName");	
		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeNameuser).setNocheck(true));
		List<User> userList = userService.findAllUsers();
		for (User user : userList) {//3
			if(StringUtil.isNotEmpty(limituds)) {
				int count = 0; 
				String[] ids = limituds.split(",");//2
				for(int i = 0; i < ids.length; i++) {
					if(NumberUtil.getInt(ids[i]) == NumberUtil.getInt(user.getIid())) {
						tree.addNode(TreeNode.getInstance(NumberUtil.getInt(user.getIid()) + "", rangeId + "", user.getName(),
								false, false).setHalfCheck(false).setChecked(true));
						count++;
						break;
					}
					count = 0;
				}
				if(count == 0) {
					tree.addNode(TreeNode.getInstance(user.getIid() + "", rangeId + "", user.getName(),
							false, false).setHalfCheck(false));
				}
			}else {
				tree.addNode(TreeNode.getInstance(user.getIid() + "", rangeId + "", user.getName(),
						false, false).setHalfCheck(false));
			}
			
		}
		result.setSuccess(true).addParam("tree", tree.parse());
		return result;
	}
	
	
	@RequestMapping("findAllrole")
	@ResponseBody
	public JsonResult findAllrole(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		LimitLoginUser limitLoginUser = limitLoginUserService.findEntityBydczjid(dczjid,2);
		String limituds = "";
		if(limitLoginUser!=null && StringUtil.isNotEmpty(limitLoginUser.getLimitids())) {
			limituds = limitLoginUser.getLimitids();
			limitLoginUser.setRoles(JsonUtil.objectToString(roleService.findByIds(limituds)));
		}else {
			limitLoginUser = new LimitLoginUser();
			limitLoginUser.setRoles(JsonUtil.objectToString(new ArrayList<Role>()));
		}
		result.setSuccess(true).addParam("limitLoginUser", limitLoginUser.getRoles());
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeNameuser = StringUtil.isEmpty(rangeName) ? "角色选择" : rangeName;
		Tree tree = Tree.getInstance("groupId", "groupName");	
		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeNameuser).setNocheck(true));
		List<Role> roleList = roleService.findAllRoles();
		for (Role role : roleList) {//3
			if(StringUtil.isNotEmpty(limituds)) {
				int count = 0; 
				String[] ids = limituds.split(",");//2
				for(int i = 0; i < ids.length; i++) {
					if(NumberUtil.getInt(ids[i]) == NumberUtil.getInt(role.getIid())) {
						tree.addNode(TreeNode.getInstance(NumberUtil.getInt(role.getIid()) + "", rangeId + "", role.getName(),
								false, false).setHalfCheck(false).setChecked(true));
						count++;
						break;
					}
					count = 0;
				}
				if(count == 0) {
					tree.addNode(TreeNode.getInstance(role.getIid() + "", rangeId + "", role.getName(),
							false, false).setHalfCheck(false));
				}
			}else {
				tree.addNode(TreeNode.getInstance(role.getIid() + "", rangeId + "", role.getName(),
						false, false).setHalfCheck(false));
			}
			
		}
		result.setSuccess(true).addParam("tree", tree.parse());
		return result;
	}
	@RequestMapping("limitopen_user")
	public ModelAndView limitOpenUser(String dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/setting/limitopenuser");
		
		LimitOpenUser limitOpenUser = limitOpenUserService.findEntityBydczjid(dczjid);
		String limituds = "";
		if(limitOpenUser!=null && StringUtil.isNotEmpty(limitOpenUser.getLimitids())) {
			limituds = limitOpenUser.getLimitids();
			limitOpenUser.setGroups(JsonUtil.objectToString(groupService.findByIds(limituds)));
		}else {
			limitOpenUser = new LimitOpenUser();
			limitOpenUser.setGroups(JsonUtil.objectToString(new ArrayList<Group>()));
		}
		
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		String rangeName = currentUser.getRangeName();
		String nodeName = StringUtil.isEmpty(rangeName) ? "机构选择" : rangeName;
		if (rangeId == null || rangeId < 0) {
			return modelAndView;
		}
		// 组织树
		Tree tree = Tree.getInstance("groupId", "groupName");

		tree.addNode(TreeNode.getInstance(rangeId + "", "0", nodeName).setNocheck(true));

		List<Group> groupList = groupService.findChildGroupByIid(rangeId);

//		for (Group group : groupList) {
//			tree.addNode(TreeNode.getInstance(group.getIid() + "", rangeId + "", group.getName(),
//					 group.getIsParent(), false).setHalfCheck(false));
//		}
		
		for (Group group : groupList) {//3
			if(StringUtil.isNotEmpty(limituds)) {
				int count = 0; 
				String[] ids = limituds.split(",");//2
				for(int i = 0; i < ids.length; i++) {
					if(NumberUtil.getInt(ids[i]) == NumberUtil.getInt(group.getIid())) {
						tree.addNode(TreeNode.getInstance(NumberUtil.getInt(group.getIid()) + "", rangeId + "", group.getName(),
								 group.getIsParent(), false).setHalfCheck(false).setChecked(true));
						count++;
						break;
					}
					count = 0;
				}
				if(count == 0) {
					tree.addNode(TreeNode.getInstance(NumberUtil.getInt(group.getIid()) + "", rangeId + "", group.getName(),
							 group.getIsParent(), false).setHalfCheck(false));
				}
			}else {
				tree.addNode(TreeNode.getInstance(NumberUtil.getInt(group.getIid()) + "", rangeId + "", group.getName(),
						 group.getIsParent(), false).setHalfCheck(false));
			}
			
		}

		modelAndView.addObject("tree", tree.parse());
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("limitOpenUser", limitOpenUser);
		return modelAndView;
	}

	@RequestMapping("checkall")
	@ResponseBody
	public JsonResult checkAll() {
		JsonResult jsonResult = JsonResult.getInstance();
		
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Integer rangeId = currentUser.getRangeId();
		if (rangeId == null || rangeId < 0) {
			return jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		try {
			List<Group> groupList = groupService.findChildGroupByIid(rangeId);
			List<Group> songroups = new ArrayList<Group>();
			for(Group group : groupList) {
				songroups.addAll(groupService.findAllSeedsById(group.getIid()));
			}
			groupList.addAll(songroups);
			jsonResult.setSuccess(true).addParam("nodes", groupList);
			return jsonResult;
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@RequestMapping("modify_ispublish")
	@ResponseBody
	public JsonResult modifyIspublish(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		if(titleInfoService.setUpdateHtml(dczjid, 0)) {
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			String userIp = IpUtil.getIp();
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("将jsurveyid为"+dczjid+"的发布状态修改为未发布"));
			result.setSuccess(true);
		}else {
			result.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		
		return result;
	}
	
	@RequestMapping("modify_state")
	@ResponseBody
	public JsonResult modifyIspublish(String dczjid,String starttime,String endtime) {
		JsonResult result = JsonResult.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int state = 0;
		try {
			Dczj_Setting dczj_Setting = settingService.getEntityBydczjid(dczjid);
			if(dczj_Setting==null){
				settingService.insertEntity(dczjid);
				dczj_Setting = settingService.getEntityBydczjid(dczjid);
			}
			if(DateUtil.dayDiff(sdf.parse(DateUtil.getCurrDateTime()), sdf.parse(starttime))>0) {
				if(dczj_Setting.getIsstart() == 0) {
					state=0;
				}else {
					state=1;
				}
			}else if(DateUtil.dayDiff(sdf.parse(endtime), sdf.parse(DateUtil.getCurrDateTime()))>0) {
				if(dczj_Setting.getIsend() == 0) {
					state=0;
				}else {
					state=2;
				}
			}else {
				state=0;
			}
			titleInfoService.updateState(dczjid, state);
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			String userIp = IpUtil.getIp();
			LogRecorder.record(LogEntity.getInstance().setModelName("在线调查").setFunctionName("设参")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("将jsurveyid为"+dczjid+"的state修改为"+state));
		} catch (ParseException e) {
			e.printStackTrace();
			result.setSuccess(false).setMessage("日期转换错误，请联系系统管理员");
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		result.setSuccess(true);
		
		return result;
	}

}
