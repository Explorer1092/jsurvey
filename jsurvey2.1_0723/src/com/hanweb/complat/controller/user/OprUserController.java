package com.hanweb.complat.controller.user;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.hanweb.common.BaseInfo;
import com.hanweb.common.annotation.Permission;
import com.hanweb.common.permission.Allowed;
import com.hanweb.common.task.TaskManager;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.interceptor.CsrfDefInterceptor;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.EmailService;
import com.hanweb.complat.service.GroupManagerService;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.RoleService;
import com.hanweb.complat.service.UserService;
import com.hanweb.complat.task.RestDynamicCodeTask;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebManagerService;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;

/**
 * 用户操作页控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "user", allowedGroup = Allowed.YES)
@RequestMapping("manager/user")
public class OprUserController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupManagerService groupManagerService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WebSiteService webSiteService;
	
	@Autowired
	private WebManagerService webManagerService;

	/**
	 * 显示新增用户页面
	 * 
	 * @return
	 */
	@Permission(function = "add_show")
	@RequestMapping(value = "add_show")
	public ModelAndView showAdd(Integer groupId, HttpServletResponse response, HttpSession session) {

		ModelAndView modelAndView = new ModelAndView("complat/user/user_opr");
		UserFormBean userFormBean = new UserFormBean();

		userFormBean.getUser().setGroupId(groupId);

		String groupName = groupService.findNameByIid(groupId);
		userFormBean.setGroupName(groupName);

		// 机构树(所属机构和管理机构选择)
		String groupTreeJson = this.findGroupTreeJson();

		// 可选角色，即所有选项
		LinkedHashMap<Integer, String> allRoleOptions = this.findAllRoleOptions();

		// 新增用户初始角色，包括默认角色和机构拥有的角色
		List<Role> groupRoleList = roleService.findGroupRoles(groupId);
		List<Role> defaultRoleList = roleService.findDefaultRoles();

		LinkedHashSet<Integer> selectedRole = new LinkedHashSet<Integer>();
		HashSet<Integer> noRemoveRole = new HashSet<Integer>();
		for (Role role : groupRoleList) {
			selectedRole.add(role.getIid());
			noRemoveRole.add(role.getIid());
		}
		for (Role role : defaultRoleList) {
			selectedRole.add(role.getIid());
		}
		selectedRole.add(1);
		modelAndView.addObject("url", "add_submit.do");
		modelAndView.addObject("formBean", userFormBean);
		modelAndView.addObject("groupMenu", groupTreeJson);
		modelAndView.addObject("allRoleOptions", JsonUtil.objectToString(allRoleOptions));
		modelAndView.addObject("selectedRoleIds", JsonUtil.objectToString(selectedRole));
		modelAndView.addObject("noremoveRoleIds", JsonUtil.objectToString(noRemoveRole));
		modelAndView.addObject("checkPasswordLevel", Settings.getSettings().getCheckLevel());

		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, null);
		return modelAndView;
	}

	/**
	 * 机构树(所属机构和管理机构选择)
	 * 
	 * @return 输的JSON字符串
	 */
	private String findGroupTreeJson() {
		Integer adminRangeId = -1;
		String adminRangeName;
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();

		Tree tree = Tree.getInstance();
		if (currentUser.isSysAdmin()) {
			List<Group> groupList = groupService.findChildGroupByIid(0);
			for (Group group : groupList) {
				tree.addNode(TreeNode.getInstance(group.getIid() + "", 0 + "", group.getName())
						.setIsParent(group.getIsParent()));
			}
		} else {
			adminRangeId = currentUser.getRangeId();
			if (adminRangeId == null) {
				/* 缺省 管理当前机构 */
				adminRangeId = currentUser.getGroupId();
			}
			adminRangeName = groupService.findNameByIid(adminRangeId);
			tree.addNode(TreeNode.getInstance(adminRangeId + "", "menu", adminRangeName, true));
		}

		return tree.parse();
	}

	/**
	 * 获得可选角色，即所有选项
	 * 
	 * @return
	 */
	private LinkedHashMap<Integer, String> findAllRoleOptions() {
		List<Role> roleList = null;
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		if (currentUser.isSysAdmin()) {
			roleList = roleService.findAllRoles();
		} else {
			roleList = roleService.findUserRoles(currentUser.getIid());
		}
		LinkedHashMap<Integer, String> allRoleOptions = new LinkedHashMap<Integer, String>();
		for (Role role : roleList) {
			allRoleOptions.put(role.getIid(), role.getName());
		}
		return allRoleOptions;
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 *            用户实体
	 * @return
	 */
	@Permission(function = "add_submit")
	@RequestMapping(value = "add_submit")
	@ResponseBody
	public JsonResult saveAdd(UserFormBean userFormBean, HttpSession session, HttpServletResponse response) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean isSuccess = false;
		try {
			userFormBean.getUser().setLoginName(SecurityUtil.RSADecode(userFormBean.getUser().getLoginName()));
			userFormBean.getUser().setPwd(SecurityUtil.RSADecode(userFormBean.getUser().getPwd()));
			isSuccess = userService.add(userFormBean);
			if (isSuccess) {
				// 判断动态码
				if (Settings.getSettings().isDynamicCodeLogin()) {
					emailService.modifyDynamicCodeAndSendEmail(userFormBean.getUser().getIid());
				}
				String userIp = IpUtil.getIp();
				String [] ips = userIp.split(",");
				if (ips != null && ips.length > 0){ //防止双IP
				    userIp = StringUtil.getString(ips[0]);
				}
				CurrentUser currentUser = UserSessionInfo.getCurrentUser();
				LogRecorder.record(LogEntity.getInstance().setModelName("用户管理").setFunctionName("新增")
						.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("新增用户，用户ID："+userFormBean.getUser().getIid()+"，姓名："+userFormBean.getUser().getName()));
				
				jsonResult.set(ResultState.ADD_SUCCESS);
			} else {
				jsonResult.set(ResultState.ADD_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		} finally {
			// 加入标志给dologin判断是否是csrf
			CsrfDefInterceptor.addCsrfToken(response, session, null);
		}
		return jsonResult;
	}

	/**
	 * 显示编辑用户页面
	 * 
	 * @param iid
	 *            用户ID
	 * @return
	 */
	@Permission(function = "modify_show")
	@RequestMapping(value = "modify_show")
	public ModelAndView showModify(Integer iid, HttpServletResponse response, HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("complat/user/user_opr");

		UserFormBean userFormBean = new UserFormBean();

		User user = userService.findByIid(iid);

		userFormBean.setUser(user);

		Integer groupId = user.getGroupId();
		String groupName = groupService.findNameByIid(user.getGroupId());
		userFormBean.setGroupName(groupName);

		// 机构树(所属机构和管理机构选择)
		String groupTreeJson = this.findGroupTreeJson();

		// 可选角色，即所有选项
		LinkedHashMap<Integer, String> allRoleOptions = this.findAllRoleOptions();

		// 可管理机构的ID
		Integer rangeId = groupManagerService.findRangeIdByUserId(user.getIid());
		String rangeName = groupService.findNameByIid(rangeId);
		String rangWebIds = webManagerService.findRangeWebIdsByUserId(user.getIid());
		String rangWebNames = webSiteService.findRangeWebNamesByWebIds(rangWebIds);
		userFormBean.setRangeId(rangeId);
		userFormBean.setRangeName(rangeName);
		userFormBean.setRangeWebIds(rangWebIds);
		userFormBean.setRangeWebNames(rangWebNames);

		// 不可移除角色
		HashSet<Integer> noRemoveRole = new HashSet<Integer>();
		// 机构拥有的角色
		List<Role> groupRoleList = roleService.findGroupRoles(groupId);
		for (Role role : groupRoleList) {
			noRemoveRole.add(role.getIid());
		}

		// 已选角色
		List<Role> selectedRole = roleService.findUserRoles(user.getIid());
		int[] selectedRoleIds = new int[selectedRole.size()];
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		for (int i = 0; i < selectedRole.size(); i++) {
			Role role = selectedRole.get(i);
			int roleId = role.getIid();
			selectedRoleIds[i] = roleId;

			// 机构管理员不可移除系统管理员角色
			if (currentUser.isGroupAdmin() && !currentUser.isSysAdmin() && roleId == 1) {
				noRemoveRole.add(1);
			}
		}

		modelAndView.addObject("url", "modify_submit.do");
		modelAndView.addObject("formBean", userFormBean);
		modelAndView.addObject("groupMenu", groupTreeJson);
		modelAndView.addObject("rangeMenu", groupTreeJson);
		modelAndView.addObject("allRoleOptions", JsonUtil.objectToString(allRoleOptions));
		modelAndView.addObject("selectedRoleIds", JsonUtil.objectToString(selectedRoleIds));
		modelAndView.addObject("noremoveRoleIds", JsonUtil.objectToString(noRemoveRole));
		modelAndView.addObject("checkPasswordLevel", Settings.getSettings().getCheckLevel());

		// 加入标志给dologin判断是否是csrf
		CsrfDefInterceptor.addCsrfToken(response, session, null);
		return modelAndView;
	}

	/**
	 * 编辑用户
	 * 
	 * @param user
	 *            用户实体
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModify(UserFormBean userFormBean, HttpSession session, HttpServletResponse response) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			if (StringUtil.isNotEmpty(userFormBean.getUser().getPwd())) {
				userFormBean.getUser().setPwd(SecurityUtil.RSADecode(userFormBean.getUser().getPwd()));
			}
			isSuccess = userService.modify(userFormBean);
			if (isSuccess) {
				jsonResult.set(ResultState.MODIFY_SUCCESS);
			} else {
				jsonResult.set(ResultState.MODIFY_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		} finally {
			// 加入标志给dologin判断是否是csrf
			CsrfDefInterceptor.addCsrfToken(response, session, null);
		}
		return jsonResult;
	}

	/**
	 * 删除用户
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3,4
	 * @return
	 */
	@Permission(function = "remove")
	@RequestMapping(value = "remove")
	@ResponseBody
	public JsonResult remove(String ids) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = userService.removeByIds(ids);
			if (isSuccess) {
				jsonResult.set(ResultState.REMOVE_SUCCESS);
			} else {
				jsonResult.set(ResultState.REMOVE_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 切换用户状态
	 * 
	 * @param iid
	 *            用户ID
	 * @param enable
	 *            是否有效<br>
	 *            1 有效<br>
	 *            0 失效
	 * @return
	 */
	@Permission(function = "enable_modify")
	@RequestMapping(value = "enable_modify")
	@ResponseBody
	public JsonResult modifyEnable(Integer iid, Integer enable) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean isSuccess = userService.modifyEnable(iid, enable);
		jsonResult.setSuccess(isSuccess);
		if (isSuccess) {
			jsonResult.setMessage("opr.success");
		} else {
			jsonResult.setMessage("opr.fail");
		}

		return jsonResult;
	}

	/**
	 * 用户导出
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3,4
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	@Permission(function = "export")
	@RequestMapping(value = "export")
	public FileResource export(String ids, Integer groupId) {
		File file = null;
		FileResource fileResouce = null;
		try {
			String filePath = userService.exportUser(ids, groupId);
			file = new File(filePath);
			fileResouce = ControllerUtil.getFileResource(file, "用户列表.xls");
		} catch (Exception e) {
			logger.error("export group Error ", e);
			return null;
		} finally {
			if (file != null && file.exists()) {
				file.delete();
			}
		}
		return fileResouce;
	}

	/**
	 * 显示用户导入页面
	 * 
	 * @return
	 */
	@Permission(function = "import_show")
	@RequestMapping(value = "import_show")
	@ResponseBody
	public ModelAndView showImport() {
		ModelAndView modelAndView = new ModelAndView("complat/user/user_import");

		modelAndView.addObject("exporturl", "user.xls");
		modelAndView.addObject("url", "import_submit.do");
		return modelAndView;
	}

	/**
	 * 用户导入
	 * 
	 * @param file
	 *            上传的文件
	 * @return
	 */
	@Permission(function = "import_submit")
	@RequestMapping(value = "import_submit")
	@ResponseBody
	public String submitImport(MultipartFile file) {
		String message = "";
		Script script = Script.getInstanceWithJsLib();
		if (!MultipartFileInfo.isValid(file)) {
			message = SpringUtil.getMessage("import.nofile");
		} else {
			try {
				MultipartFileInfo info = MultipartFileInfo.getInstance(file);
				if (ArrayUtils.contains(FileUtil.EXCEL_FILE, info.getFileType())) {
					File filePath = new File(Settings.getSettings().getFileTmp() + StringUtil.getUUIDString() + "."
							+ info.getFileType());
					ControllerUtil.writeMultipartFileToFile(filePath, file);
					message = userService.importUser(filePath);
				} else {
					throw new OperationException("文件类型不正确");
				}
			} catch (OperationException e) {
				message = e.getMessage();
			}
		}
		if (StringUtil.isEmpty(message)) {
	//		script.refreshNode("0");
			script.addScript("parent.refreshParentWindow();parent.closeDialog();");
		} else {
			// script.addAlert(message);
			script.addAlert(message);
	//		script.refreshNode("0");
			script.addScript("parent.refreshParentWindow();");
		}
		return script.getScript();
	}

	/**
	 * 用户xls文件下载
	 * 
	 * @return
	 */
	@RequestMapping(value = "downloadfile")
	@ResponseBody
	public FileResource downloadFile() {
		File file = new File(BaseInfo.getRealPath() + "/WEB-INF/pages/complat/user/user.xls");
		FileResource fileResource = ControllerUtil.getFileResource(file, "user.xls");

		return fileResource;
	}

	/**
	 * 重置动态码密钥
	 * 
	 * @param iid
	 *            用户ID
	 * @param enable
	 *            是否有效<br>
	 *            1 有效<br>
	 *            0 失效
	 * @return
	 */
	@Permission(function = "restDynamicCode")
	@RequestMapping(value = "rest_dynamic_code")
	@ResponseBody
	public JsonResult restDynamicCode(String ids) {
		JsonResult jsonResult = JsonResult.getInstance();
		if (TaskManager.isExist(RestDynamicCodeTask.TASKID)) {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("线程正在运行请稍后再试");
		} else {
			RestDynamicCodeTask restDynamicCodeTask = new RestDynamicCodeTask();
			restDynamicCodeTask.addParam("ids", ids);
			TaskManager.addTask(restDynamicCodeTask);
			jsonResult.setSuccess(true);
			jsonResult.setMessage("操作已经加入后台线程");
		}
		return jsonResult;
	}
	
	@RequestMapping(value = "webRights")
	@ResponseBody
	public ModelAndView webRights(String userId){
		ModelAndView modelAndView = new ModelAndView("complat/user/webRights_tree");
		Tree tree = Tree.getInstance("treeNodeId", "nodeName");
		tree.addNode(TreeNode.getInstance("0", "", "网站管理",true,true).setNocheck(true));
		List<DCZJ_WebSite> websites = webSiteService.findChildWebByIid(0);
		boolean isExist = false;
		for(DCZJ_WebSite website : websites){
			String iid = StringUtil.getString(website.getIid());
			String name = website.getName();
			String pId = StringUtil.getString(website.getPid());
			isExist = webManagerService.checkWebid(userId,iid);
			if(isExist){
				tree.addNode(TreeNode.getInstance(iid, pId, name,website.getIsParent(),false).setChecked(true));
			}else{
				tree.addNode(TreeNode.getInstance(iid, pId, name,website.getIsParent(),false).setChecked(false));
			}
		}
		modelAndView.addObject("url", "menutree_submit.do");
		modelAndView.addObject("tree", tree.parse());
		return modelAndView;
	}
	
	/**
	 * 菜单树设置提交保存
	 * @param c_id
	 * @param menuIds
	 * @return
	 */
	@RequestMapping("menutree_submit")
	@ResponseBody
	public JsonResult submitMenuTree(String menuIds){
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
}
