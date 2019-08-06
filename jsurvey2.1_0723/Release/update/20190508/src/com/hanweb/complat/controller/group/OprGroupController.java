package com.hanweb.complat.controller.group;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.JsonUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.GroupService;
import com.hanweb.complat.service.RoleService;
import com.hanweb.support.controller.CurrentUser;

/**
 * 机构操作控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "group", allowedGroup = Allowed.YES)
@RequestMapping("manager/group")
public class OprGroupController {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private GroupService groupService;

	@Autowired
	private RoleService roleService;
	
	/**
	 * 显示新增机构页面
	 * 
	 * @param pid
	 *            父机构ID
	 * @return
	 */
	@Permission(function = "add_show")
	@RequestMapping(value = "add_show")
	public ModelAndView showAdd(Integer pid) {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		int parentGroupId = NumberUtil.getInt(pid);
		String parentGroupName = "";
		
		Group group = new Group();
		if (parentGroupId > 0) {
			Group parentGroup = groupService.findByIid(parentGroupId);
			parentGroupName = parentGroup.getName();
		}
		
		group.setPid(parentGroupId);
		group.setPname(parentGroupName);
		
		// 机构树(所属机构和管理机构选择)
//		String groupTreeJson = this.findGroupTreeJson(currentUser);
		
		// 角色选项
		List<Role> roleList = null;
		if (currentUser.isSysAdmin())
			roleList = roleService.findAllRoles();
		else {
			roleList = roleService.findUserRoles(currentUser.getIid());
		}
		
		LinkedHashMap<Integer, String> allRoleOptions = new LinkedHashMap<Integer, String>();
		for (Role role : roleList) {
			allRoleOptions.put(role.getIid(), role.getName());
		}
		
		// 新增用户初始角色，包括默认角色
		List<Role> defaultRoleList = roleService.findDefaultRoles();
		
		LinkedHashSet<Integer> selectedRole = new LinkedHashSet<Integer>();
		for (Role role : defaultRoleList) {
			selectedRole.add(role.getIid());
		}
		
		ModelAndView modelAndView = new ModelAndView("complat/group/group_opr");
		modelAndView.addObject("url", "add_submit.do");
		modelAndView.addObject("group", group);
//		modelAndView.addObject("groupMenu", groupTreeJson);
		modelAndView.addObject("allRoleOptions", JsonUtil.objectToString(allRoleOptions));
		modelAndView.addObject("selectedRoleIds", JsonUtil.objectToString(selectedRole));
		modelAndView.addObject("noremoveRoleIds", "null");
		modelAndView.addObject("rangeId", currentUser.getRangeId());
		modelAndView.addObject("rangeName", currentUser.getRangeName());
		return modelAndView;
	}
	
	/**
	 * 显示编辑机构页面
	 * @param iid	机构id
	 * @return
	 */
	@Permission(function = "modify_show")
	@RequestMapping(value = "modify_show")
	public ModelAndView showModify(int iid) {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		Group group = groupService.findByIid(iid);

		// 机构树(所属机构和管理机构选择)
//		String groupTreeJson = this.findGroupTreeJson(currentUser);

		// 角色选项
		List<Role> roleList = null;
		if (currentUser.isSysAdmin())
			roleList = roleService.findAllRoles();
		else {
			roleList = roleService.findUserRoles(currentUser.getIid());
		}
		LinkedHashMap<Integer, String> allRoleOptions = new LinkedHashMap<Integer, String>();
		for (Role role : roleList) {
			allRoleOptions.put(role.getIid(), role.getName());
		}
		
		List<Role> groupRoles = roleService.findGroupRoles(iid);
		LinkedHashSet<Integer> selectedRole = new LinkedHashSet<Integer>();
		for (Role role : groupRoles) {
			selectedRole.add(role.getIid());
		}
		
		ModelAndView modelAndView = new ModelAndView("complat/group/group_opr");
		modelAndView.addObject("url", "modify_submit.do");
		modelAndView.addObject("group", group);
//		modelAndView.addObject("groupMenu", groupTreeJson);
		modelAndView.addObject("allRoleOptions", JsonUtil.objectToString(allRoleOptions));
		modelAndView.addObject("selectedRoleIds", JsonUtil.objectToString(selectedRole));
		modelAndView.addObject("noremoveRoleIds", "null");
		modelAndView.addObject("rangeId", currentUser.getRangeId());
		modelAndView.addObject("rangeName", currentUser.getRangeName());
		return modelAndView;
	}

	/**
	 * 新增机构
	 * 
	 * @param group
	 *            机构实体
	 * @return
	 */
	@Permission(function = "add_submit")
	@RequestMapping(value = "add_submit")
	@ResponseBody
	public JsonResult submitAdd(GroupFormBean group) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = groupService.add(group);
			if (isSuccess) {
				jsonResult.set(ResultState.ADD_SUCCESS);
				jsonResult.addParam("refresh", NumberUtil.getInt(group.getPid()) + "");
			} else {
				jsonResult.set(ResultState.ADD_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 编辑机构
	 * 
	 * @param group
	 *            机构实体
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModify(GroupFormBean group) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = groupService.modify(group);
			if (isSuccess) {
				if (NumberUtil.getInt(group.getPrevPid()) != NumberUtil.getInt(group.getPid())) {
					jsonResult.addParam("remove", NumberUtil.getInt(group.getIid()) + "");
				}
				jsonResult.addParam("refresh", NumberUtil.getInt(group.getPid()) + "");
				jsonResult.set(ResultState.MODIFY_SUCCESS);
			} else {
				jsonResult.set(ResultState.MODIFY_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 删除机构
	 * 
	 * @param ids
	 *            机构ID串 如:1,2,3,4
	 * @param pid
	 *            父机构ID
	 * @return
	 */
	@Permission(function = "remove")
	@RequestMapping(value = "remove")
	@ResponseBody
	public JsonResult remove(String ids, String pid) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean isSuccess = false;
		try {
			isSuccess = groupService.removeByIds(ids);
			if (isSuccess) {
				jsonResult.set(ResultState.REMOVE_SUCCESS);
				jsonResult.addParam("remove", ids);
			} else {
				jsonResult.set(ResultState.REMOVE_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}

	/**
	 * 机构导出
	 * 
	 * @param ids
	 *            机构ID串 如:1,2,3,4
	 * @param pid
	 *            父机构ID
	 * @return
	 */
	@Permission(function = "export")
	@RequestMapping(value = "export")
	public FileResource export(String ids, String pid) {

		File file = null;
		FileResource fileResouce = null;
		try {
			String filePath = groupService.exportGroup(ids, NumberUtil.getInt(pid));
			file = new File(filePath);
			fileResouce = ControllerUtil.getFileResource(file, "机构列表.xls");
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
	 * 显示机构导入页面
	 * 
	 * @return
	 */
	@Permission(function = "import_show")
	@RequestMapping(value = "import_show")
	@ResponseBody
	public ModelAndView showImport() {
		ModelAndView modelAndView = new ModelAndView("complat/group/group_import");

		modelAndView.addObject("url", "import_submit.do");
		return modelAndView;
	}

	/**
	 * 机构导入
	 * 
	 * @param file
	 *            上传的文件
	 * @return
	 */
	@Permission(function = "import_submit")
	@RequestMapping(value = "import_submit")
	@ResponseBody
	public String submitImport(MultipartFile file) {
		Script script = Script.getInstanceWithJsLib();
		String message = "";
		if (!MultipartFileInfo.isValid(file)) {
			message = SpringUtil.getMessage("import.nofile");
		} else {
			try {
				MultipartFileInfo info = MultipartFileInfo.getInstance(file);
				if (ArrayUtils.contains(FileUtil.EXCEL_FILE, info.getFileType())) {
					File filePath = new File(Settings.getSettings().getFileTmp()
							+ StringUtil.getUUIDString() + "." + info.getFileType());
					ControllerUtil.writeMultipartFileToFile(filePath, file);
					message = groupService.importGroup(filePath);
				} else {
					throw new OperationException("文件类型不正确");
				}
			} catch (OperationException e) {
				message = e.getMessage();
			}
		}
		if (StringUtil.isEmpty(message)) {
			/* script.refreshNode("0"); */
			script.addScript("parent.refreshParentWindow();parent.closeDialog();");
		} else {
			script.addAlert(message);
			script.refreshNode("0");
			script.addScript("parent.refreshParentWindow();");
		}
		return script.getScript();
	}

	/**
	 * 机构xls文件下载
	 * 
	 * @return
	 */
	@RequestMapping(value = "downloadfile")
	@ResponseBody
	public FileResource downloadFile() {
		File file = new File(BaseInfo.getRealPath() + "/WEB-INF/pages/complat/group/group.xls");
		FileResource fileResource = ControllerUtil.getFileResource(file, "group.xls");

		return fileResource;
	}
}