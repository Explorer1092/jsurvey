package com.hanweb.complat.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.Constants;
import com.hanweb.common.util.ExcelUtil;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.PinyinUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.controller.user.UserFormBean;
import com.hanweb.complat.dao.GroupManagerDAO;
import com.hanweb.complat.dao.RoleRelationDAO;
import com.hanweb.complat.dao.UserDAO;
import com.hanweb.complat.entity.Group;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.dczj.dao.WebManagerDAO;
import com.hanweb.dczj.service.WebManagerService;

/**
 * 用户Service
 * 
 * @author ZhangC
 * 
 */
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private RoleRelationDAO roleRelationDAO;

	@Autowired
	private GroupManagerService groupManagerService;

	@Autowired
	private GroupManagerDAO groupManagerDAO;
	
	@Autowired
	private WebManagerService webManagerService;
	
	@Autowired
	private WebManagerDAO webManagerDAO;

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 *            user 必须包含iid 和 pwd
	 * @return
	 */
	public boolean modifyPassword(Integer userId, String password) {
		boolean success = false;
		if (userId != null && StringUtil.isNotEmpty(password)) {
			success = userDAO.updatePassword(userId, Md5Util.encodePwd(password));
		}
		return success;
	}

	/**
	 * 强制修改用户名、密码
	 * 
	 * @param userId
	 *            用户id
	 * @param loginId
	 *            用户名
	 * @param password
	 *            密码
	 * @return
	 */
	public boolean modifyLoginIdAndPassword(Integer userId, String loginId, String password) {
		boolean success = false;
		if (userId != null && StringUtil.isNotEmpty(loginId) && StringUtil.isNotEmpty(password)) {
			success = userDAO.updateLoginIdAndPassword(userId, loginId, Md5Util.encodePwd(password));
		}
		return success;
	}

	/**
	 * 更新用户基本信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean modifySelf(User user) {
		boolean success = false;
		try {
			user.setPinYin(PinyinUtil.getHeadByString(user.getName()));

			success = userDAO.update(user);
			if (success && StringUtil.isNotEmpty(user.getPwd())) {
				success = modifyPassword(user.getIid(), user.getPwd());
			}
		} catch (Exception e) {
			LogWriter.error("modifySelf error", e);
		}
		return success;
	}

	/**
	 * 通过用户ID获得用户实体
	 * 
	 * @param iid
	 *            用户ID
	 * @return
	 */
	public User findByIid(int iid) {
		if (iid == 0) {
			return null;
		}
		User user = userDAO.findByIid(iid);
		if (user == null) {
			return null;
		}
		// user.setPwd(Md5Util.md5decode(user.getPwd()));
		return user;
	}

	/**
	 * 通过登录名获得用户实体
	 * 
	 * @param loginName
	 *            用户登录名
	 * @return
	 */
	public User findByLoginName(String loginName) {
		if (StringUtil.isEmpty(loginName)) {
			return null;
		}
		User user = userDAO.findByLoginName(loginName);
		if (user == null) {
			return null;
		}
		// user.setPwd(Md5Util.md5decode(user.getPwd()));
		return user;
	}

	/**
	 * 获得用户所在机构ID
	 * 
	 * @param iid
	 *            用户ID
	 * @return
	 */
	public Integer findGroupIdByIid(int iid) {
		if (iid == 0) {
			return null;
		}

		Integer groupId = userDAO.findGroupIdByIid(iid);

		return groupId;
	}

	/**
	 * 获得指定机构ID串下的所有用户集合
	 * 
	 * @param groupIds
	 *            机构ID串 如:1,2,3
	 * @return
	 */
	public List<User> findByGroupIds(String groupIds) {
		if (StringUtil.isEmpty(groupIds)) {
			return null;
		}
		List<Integer> groupidList = StringUtil.toIntegerList(groupIds, ",");

		List<User> userList = userDAO.findByGroupIds(groupidList);
		return userList;
	}

	/**
	 * 新增用户
	 * 
	 * @param user
	 *            用户实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean add(UserFormBean userFormBean) throws OperationException {
		if (userFormBean == null) {
			return false;
		}
		User user = userFormBean.getUser();
		boolean isSuccess = false;
		Integer rangeId = userFormBean.getRangeId();
		List<Integer> roleIdList = StringUtil.toIntegerList(userFormBean.getRoleIds(), ",");
		List<Integer> webIdList = null;
		if(StringUtil.isNotEmpty(userFormBean.getRangeWebIds())){
			webIdList = StringUtil.toIntegerList(userFormBean.getRangeWebIds(), ",");
		}
		int num = userDAO.findIidByLoginName(null, user.getLoginName());
		if (num > 0) {
			throw new OperationException("登录名已存在,请重新设置！");
		}
		user.setPwd(Md5Util.encodePwd(user.getPwd()));
		user.setCreatetime(new Date());
		user.setPinYin(PinyinUtil.getHeadByString(user.getName()));
		user.setUuid(StringUtil.getUUIDString());
		user.setDynamicCodeKey(SecurityUtil.createAuthKey());

		int iid = userDAO.insert(user);

		// 设置用户管理范围
		if (iid > 0 && NumberUtil.getInt(rangeId) > 0) {
			isSuccess = groupManagerService.add(iid, rangeId);
			if (!isSuccess) {
				throw new OperationException("新增用户机构关系失败！");
			}
		}
		
		// 设置用户网站管理范围
		if (iid > 0 &&CollectionUtils.isNotEmpty(webIdList)) {
			isSuccess = webManagerService.add(iid, webIdList);
			if (!isSuccess) {
				throw new OperationException("新增用户网站关系失败！");
			}
		}

		// 新增用户对应的角色
		if (iid > 0 && CollectionUtils.isNotEmpty(roleIdList)) {
			isSuccess = roleService.modifyUserMembers(roleIdList, user.getGroupId(), iid);
			if (!isSuccess) {
				throw new OperationException("新增用户角色关系失败！");
			}
		}
		
		return iid > 0 ? true : false;
	}

	/**
	 * 更新用户
	 * 
	 * @param user
	 *            用户实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean modify(UserFormBean userFormBean) throws OperationException {
		if (userFormBean == null) {
			return false;
		}
		User user = userFormBean.getUser();
		Integer iid = user.getIid();
		Integer rangeId = userFormBean.getRangeId();

		List<Integer> userIdList = new ArrayList<Integer>();
		List<Integer> roleIdList = StringUtil.toIntegerList(userFormBean.getRoleIds(), ",");
		List<Integer> webIdList = StringUtil.toIntegerList(userFormBean.getRangeWebIds(), ",");
		userIdList.add(iid);

		user.setPinYin(PinyinUtil.getHeadByString(user.getName()));

		boolean isSuccess = userDAO.update(user);

		if (isSuccess && StringUtils.isNotBlank(user.getPwd())) {
			user.setPwd(Md5Util.encodePwd(user.getPwd()));
			isSuccess = userDAO.updatePassword(iid, user.getPwd());
		}

		isSuccess = groupManagerDAO.deleteByUserIds(userIdList);
		if (!isSuccess) {
			throw new OperationException("删除用户机构关系失败！");
		}
		// 设置用户管理范围
		if (NumberUtil.getInt(rangeId) > 0) {
			isSuccess = groupManagerService.add(iid, rangeId);
			if (!isSuccess) {
				throw new OperationException("更新用户机构关系失败！");
			}
		}

		isSuccess = webManagerDAO.deleteByUserIds(userIdList);
		if (!isSuccess) {
			throw new OperationException("删除用户网站关系失败！");
		}
		// 设置用户管理范围
		if (CollectionUtils.isNotEmpty(webIdList)) {
			isSuccess = webManagerService.add(iid, webIdList);
			if (!isSuccess) {
				throw new OperationException("更新用户网站关系失败！");
			}
		}
				
		isSuccess = roleRelationDAO.deleteUsers(null, userIdList);
		if (!isSuccess) {
			throw new OperationException("删除用户角色关系失败！");
		}

		// 新增用户对应的角色
		if (CollectionUtils.isNotEmpty(roleIdList)) {

			isSuccess = roleService.modifyUserMembers(roleIdList, user.getGroupId(), iid);
			if (!isSuccess) {
				throw new OperationException("新增用户角色关系失败！");
			}
		}
		return isSuccess;
	}

	/**
	 * 更新用户基本信息，不涉及角色权限修改<br/>
	 * 主要供接口使用
	 * 
	 * @param user
	 *            用户实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean modifyUserInfo(User user) {
		if (user == null || user.getIid() <= 0) {
			return false;
		}
		Integer iid = user.getIid();
		user.setPinYin(PinyinUtil.getHeadByString(user.getName()));

		boolean isSuccess = userDAO.update(user);
		if (isSuccess && StringUtils.isNotBlank(user.getPwd())) {
			user.setPwd(Md5Util.encodePwd(user.getPwd()));
			isSuccess = userDAO.updatePassword(iid, user.getPwd());
		}
		return isSuccess;
	}

	/**
	 * 删除用户
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean removeByIds(String ids) throws OperationException {
		List<Integer> idList = StringUtil.toIntegerList(ids, ",");
		if (CollectionUtils.isEmpty(idList)) {
			return false;
		}
		boolean isSuccess = userDAO.deleteByIds(idList);
		if (!isSuccess) {
			throw new OperationException("删除用户失败！");
		}

		isSuccess = groupManagerDAO.deleteByUserIds(idList);
		if (!isSuccess) {
			throw new OperationException("删除用户机构关系失败！");
		}

		isSuccess = roleRelationDAO.deleteUsers(null, idList);
		if (!isSuccess) {
			throw new OperationException("删除用户角色关系失败！");
		}
		return isSuccess;
	}

	/**
	 * 更新用户的有效性
	 * 
	 * @param iid
	 *            用户ID
	 * @param enable
	 *            是否有效<br/>
	 *            1 - 有效<br/>
	 *            0 - 无效
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean modifyEnable(int iid, int enable) {
		if (iid == 0) {
			return false;
		}
		return userDAO.updateEnable(iid, enable);
	}

	/**
	 * 以Excel形式导出用户信息
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3<br/>
	 *            ids不为空时导出所选用户<br/>
	 *            ids为空时导出groupId及其子孙机构下的所有用户
	 * @param groupId
	 *            机构ID
	 * @return 导出文件的绝对路径
	 */
	public String exportUser(String ids, Integer groupId) {

		List<List<String>> rows = new ArrayList<List<String>>();
		/* 存放用户 */
		List<User> userList = null;
		/* 表头 */
		List<String> headList = new ArrayList<String>();
		/* 用户数据列 */
		List<String> valueList = null;
		String filePath = "";

		headList.add("姓名");
		headList.add("登录名");
		headList.add("所属机构");
		headList.add("机构标识");
		headList.add("上级机构");
		headList.add("职务");
		headList.add("固定电话");
		headList.add("移动电话");
		headList.add("电子邮箱");
		headList.add("联系地址");
		headList.add("联系方式");
		headList.add("唯一标识");
		rows.add(headList);

		// 导出已选用户
		if (StringUtil.isNotEmpty(ids)) {
			List<Integer> idsList = StringUtil.toIntegerList(ids, ",");
			userList = userDAO.findByIds(idsList);
		} else {
			/* 获得当前机构下的所有子孙用户 */
			userList = this.findAllByGroupId(groupId);
		}

		Map<Integer, Group> tempGroupMap = new HashMap<Integer, Group>(Constants.MAP_INIT_SIZE);

		/* 写入用户信息 */
		for (User user : userList) {
			String groupName = null;
			String groupCode = null;
			String parentGroupName = null;
			if (NumberUtil.getInt(user.getGroupId()) > 0) {
				Group tempGroup = tempGroupMap.get(user.getGroupId());
				if (tempGroup == null) {
					tempGroup = groupService.findByIid(user.getGroupId());
					tempGroupMap.put(tempGroup.getIid(), tempGroup);
				}
				groupName = tempGroup.getName();
				groupCode = tempGroup.getCodeId();
				parentGroupName = tempGroup.getPname();
			}
			valueList = new ArrayList<String>();
			valueList.add(user.getName());
			valueList.add(user.getLoginName());
			valueList.add(groupName);
			valueList.add(groupCode);
			valueList.add(parentGroupName);
			valueList.add(user.getHeadship());
			valueList.add(user.getPhone());
			valueList.add(user.getMobile());
			valueList.add(user.getEmail());
			valueList.add(user.getAddress());
			valueList.add(user.getContact());
			valueList.add(user.getUuid());
			rows.add(valueList);
		}

		try {
			/* 写入文件 */
			String fileName = StringUtil.getUUIDString() + ".xls";
			filePath = Settings.getSettings().getFileTmp() + fileName;
			ExcelUtil.writeExcel(filePath, rows);
			return filePath;
		} catch (Exception e) {
			LogWriter.error("exportUsers Error ", e);
			return "";
		}
	}

	/**
	 * 获得机构及其子孙机构下的所有用户集合
	 * 
	 * @param groupId
	 *            机构ID
	 * @return
	 */
	public List<User> findAllByGroupId(Integer groupId) {
		groupId = NumberUtil.getInt(groupId);

		List<User> allUserList = new ArrayList<User>();
		Map<Integer, List<User>> userMap = new LinkedHashMap<Integer, List<User>>();

		List<Integer> groupIdsList = new ArrayList<Integer>();

		// 获得机构ID列表
		groupIdsList = groupService.findIdsByPId(groupId, groupIdsList);

		// 每个机构建立一个用户集
		for (Integer groupid : groupIdsList) {
			userMap.put(groupid, new ArrayList<User>());
		}

		List<User> userList = null;
		// 导出所有
		if (groupId == 0) {
			List<User> allUsers = userDAO.findAllUsers();
			// 将每个用户放入机构对应的用户集中
			for (User user : allUsers) {
				userList = userMap.get(user.getGroupId());
				userList.add(user);
				userMap.put(user.getGroupId(), userList);
			}

			Iterator<Integer> iterator = userMap.keySet().iterator();
			while (iterator.hasNext()) {
				userList = userMap.get(iterator.next());
				if (userList != null) {
					allUserList.addAll(userList);
				}
			}
		} else {
			// 获得当前机构下所有用户
			userList = this.findByGroupIds(groupId + "");
			if (CollectionUtils.isNotEmpty(userList)) {
				allUserList.addAll(userList);
			}

			List<List<Integer>> splitIdsList = this.splitGroupIds(groupIdsList);
			// 获得每组机构下所有的用户
			for (List<Integer> eachIdsList : splitIdsList) {
				if (CollectionUtils.isEmpty(eachIdsList)) {
					continue;
				}
				userList = userDAO.findByGroupIds(eachIdsList);
				if (CollectionUtils.isNotEmpty(userList)) {
					allUserList.addAll(userList);
				}
			}
		}
		return allUserList;
	}

	/**
	 * 切割机构ID串. 以20个ID为一组的List<Integer>
	 * 
	 * @param groupIdsList
	 *            机构ID串的集合
	 * @return 机构ID串的集合
	 */
	private List<List<Integer>> splitGroupIds(List<Integer> groupIdsList) {
		List<List<Integer>> subIdsList = new ArrayList<List<Integer>>();
		// 每组ID的集合
		List<Integer> eachIdsList = new ArrayList<Integer>();

		int num = 20;

		int groupSize = groupIdsList.size() / num;
		int elseSize = groupIdsList.size() % num;

		for (int i = 0; groupSize != 0 && i < groupSize; i++) {
			eachIdsList = groupIdsList.subList(i * num, (i + 1) * num);
			subIdsList.add(eachIdsList);
		}
		/* 剩余的为一组 */
		if (elseSize != 0) {
			eachIdsList = groupIdsList.subList(groupSize * 10, groupIdsList.size());
			subIdsList.add(eachIdsList);
		}
		return subIdsList;
	}

	/**
	 * 从Excel导入用户信息
	 * 
	 * @param filePath
	 *            导入Excel表单的绝对路径
	 * @return 提示信息
	 * @throws OperationException
	 *             错误信息
	 */
	public String importUser(File file) throws OperationException {
		if (file == null) {
			throw new OperationException("找不到导入文件！");
		}
		List<Map<String, String>> rows = ExcelUtil.readExcel(file);
		if (CollectionUtils.isEmpty(rows)) {
			throw new OperationException(SpringUtil.getMessage("import.filetype.error"));
		}

		/* 获得用户集合 */
		List<Map<String, String>> userList = this.getUserListByRows(rows);
		String retMessage = "";

		try {
			/* 循环插入用户信息 */
			retMessage = this.importUsers(userList);

			if (StringUtil.isNotEmpty(retMessage)) {
				retMessage = "<div style=\"height:100%;overflow:auto\">导入完毕，存在以下问题：<br/>" + retMessage + "</div>";
			}
			return retMessage;
		} catch (Exception e) {
			LogWriter.error("import users error", e);
			return "导入失败";
		} finally {
			try {
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
				LogWriter.error("delete file error", e);
			}
		}
	}

	/**
	 * 获得用户实体List
	 * 
	 * @param rows
	 *            用户集合 每个Map为一个用户实体<br/>
	 *            key为表单头 如:"姓名"<br/>
	 *            vaule为表单值 如:"张三"
	 * @return
	 */
	private List<Map<String, String>> getUserListByRows(List<Map<String, String>> rows) {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Map<String, String> cell = null;

		/* excel记录转换成用户集合 */
		Iterator<Map<String, String>> iterator = rows.iterator();
		while (iterator.hasNext()) {
			cell = iterator.next();

			Map<String, String> userMap = new HashMap<String, String>(Constants.MAP_INIT_SIZE);
			userMap.put("name", cell.get("姓名"));
			userMap.put("loginName", cell.get("登录名"));
			userMap.put("pwd", cell.get("密码"));
			userMap.put("groupName", cell.get("所属机构"));
			userMap.put("groupCode", cell.get("机构标识"));
			userMap.put("parentGroupName", cell.get("上级机构"));
			userMap.put("headship", cell.get("职务"));
			userMap.put("phone", cell.get("固定电话"));
			userMap.put("mobile", cell.get("移动电话"));
			userMap.put("email", cell.get("电子邮箱"));
			userMap.put("address", cell.get("联系地址"));
			userMap.put("contact", cell.get("联系方式"));
			userMap.put("uuid", cell.get("唯一标识"));
			userList.add(userMap);
		}
		return userList;
	}

	/**
	 * 导入用户
	 * 
	 * @param userList
	 *            用户实体集合
	 * @return 界面提示信息
	 */
	public String importUsers(List<Map<String, String>> userList) {
		if (userList == null) {
			return "";
		}
		String retString = doImport(userList);
		if (StringUtil.isNotEmpty(retString)) {
			retString = "<ul>" + retString.toString() + "</ul>";
		}
		return retString;
	}

	private String doImport(List<Map<String, String>> userList) {
		String message = "";
		StringBuilder result = new StringBuilder();
		Map<String, String> existMap = new HashMap<String, String>(Constants.MAP_INIT_SIZE);
		for (int i = 0; i < userList.size(); i++) {
			Map<String, String> userMap = userList.get(i);
			if (userMap == null) {
				continue;
			}
			Integer groupId = 0;
			boolean isSccuess = false;
			String name = userMap.get("name");
			String loginName = userMap.get("loginName");
			String groupName = userMap.get("groupName");
			String groupCode = userMap.get("groupCode");
			String pwd = userMap.get("pwd");
			String parentGroupName = userMap.get("parentGroupName");
			/* 记录用户是否被导入过 */
			String exist = "";
			if (userMap == null || StringUtil.isEmpty(name) || StringUtil.isEmpty(loginName)) {
				message = "该行姓名或登录名为空！";
				this.getReturnMessage(result, i, message);
				continue;
			}
			/* 检查导入信息是否符合要求 */
			message = this.checkImportUser(name, loginName, groupName, groupCode);
			if (message != null && message.length() > 0) {
				this.getReturnMessage(result, i, message);
				continue;
			}
			try {
				/* 取得对应的机构ID */
				groupId = this.findValidGroupId(groupCode, groupName, parentGroupName);
			} catch (Exception e) {
				this.getReturnMessage(result, i, e.getMessage());
				continue;
			}
			exist = existMap.get(loginName);
			if (exist != null && exist.equals("loginName")) {
				message = "登录名'" + loginName + "'重复";
				this.getReturnMessage(result, i, message);
				continue;
			}
			User tempUser = this.findByLoginName(loginName);
			if (tempUser == null) {
				try {
					isSccuess = importUser(groupId, name, loginName, pwd, userMap);
				} catch (Exception e) {
					this.getReturnMessage(result, i, e.getMessage());
				}
			} else {
				message = "登录名为 '" + loginName + "' 的用户已存在";
				this.getReturnMessage(result, i, message);
				continue;
				// user.setIid(tempUser.getIid());
				// isSccuess = this.modifyUserInfo(user);
			}
			existMap.put(loginName, "loginName");
			if (!isSccuess) {
				message = "导入用户'" + loginName + "'出现异常";
				this.getReturnMessage(result, i, message);
				continue;
			}
		}
		return result.toString();
	}

	private boolean importUser(Integer groupId, String name, String loginName, String pwd, Map<String, String> userMap)
			throws Exception {
		// 新增用户初始角色，包括默认角色和机构拥有的角色
		List<Role> groupRoleList = roleService.findGroupRoles(groupId);
		List<Role> defaultRoleList = roleService.findDefaultRoles();
		LinkedHashSet<Integer> selectedRole = new LinkedHashSet<Integer>();
		for (Role role : groupRoleList) {
			selectedRole.add(role.getIid());
		}
		for (Role role : defaultRoleList) {
			selectedRole.add(role.getIid());
		}
		String roleIds = StringUtil.join(selectedRole, ",");
		User user = new User();
		user.setName(name);
		user.setLoginName(loginName);
		user.setGroupId(groupId);
		user.setHeadship(userMap.get("headship"));
		user.setPhone(userMap.get("phone"));
		user.setMobile(userMap.get("mobile"));
		user.setEmail(userMap.get("email"));
		user.setAddress(userMap.get("address"));
		user.setContact(userMap.get("contact"));
		user.setUuid(userMap.get("uuid"));
		user.setEnable(1);
		if (StringUtil.isEmpty(pwd)) {
			// 导入用户的默认密码设为888888
			pwd = "888888";
		}
		user.setPwd(pwd);
		UserFormBean userFormBean = new UserFormBean();
		userFormBean.setUser(user);
		userFormBean.setRoleIds(roleIds);
		return this.add(userFormBean);
	}

	/**
	 * 检查导入的用户是否符合标准
	 * 
	 * @param name
	 * @param loginName
	 * @param groupName
	 * @param groupCode
	 * @return
	 */
	private String checkImportUser(String name, String loginName, String groupName, String groupCode) {
		String message = "";

		/* 检查 是否符合要求 */
		int maxNameLength = 80;
		if (StringUtil.isEmpty(name)) {
			message += ":姓名不能为空";
		} else if (name.length() > maxNameLength) {
			message += ":姓名过长";
		}
		if (StringUtil.isEmpty(loginName)) {
			message += ":登录不能为空";
		} else if (loginName.length() > maxNameLength) {
			message += ":登录名过长";
		}
		if (StringUtil.isEmpty(groupName) && StringUtil.isEmpty(groupCode)) {
			message += ":机构名称、机构标识不能同时为空";
		} else {
			int maxGroupCodeLength = 16;
			if (groupCode.length() > maxGroupCodeLength) {
				message += ":机构标识不能超过16位";
			}
			if (name.length() > maxNameLength) {
				message += ":机构名称超长";
			}
		}
		return message;
	}

	/**
	 * 组织导入的错误提示
	 * 
	 * @param result
	 *            错误提示信息集
	 * @param i
	 *            信息在List中的序号
	 * @param message
	 *            错误信息
	 */
	private void getReturnMessage(StringBuilder result, int i, String message) {
		result.append("<li>");
		result.append("[" + SpringUtil.getMessage("import.error", i + 2) + "] " + message);
		result.append("</li>");
	}

	/**
	 * 获得导入用户的有效机构ID
	 * 
	 * @param user
	 *            用户实体
	 * @return
	 * @throws OperationException
	 *             错误信息
	 */
	private Integer findValidGroupId(String groupCode, String groupName, String parentGroupName)
			throws OperationException {
		Group group = new Group();
		List<Group> groupList = null;
		String message = "";
		// 待返回的用户机构ID
		Integer groupId;

		/* 如果存在机构标识 */
		if (!"".equals(groupCode)) {
			group = groupService.findByCodeId(groupCode);
			if (group != null) {
				groupId = group.getIid();
				return groupId;
			}
		}
		/* 所属机构不为空且父机构不为空 */
		if (!"".equals(groupName) && !"".equals(parentGroupName)) {
			groupList = groupService.findByNameAndPName(groupName, parentGroupName);
			if (groupList != null && groupList.size() == 1) {
				group = groupList.get(0);
				groupId = group.getIid();
				return groupId;
			}
		}
		/* 所属机构不为空 */
		if (!"".equals(groupName)) {
			groupList = groupService.findByName(groupName);
			if (CollectionUtils.isEmpty(groupList)) {
				throw new OperationException("无法找到所属机构！");
			} else if (groupList.size() == 1) {
				group = groupList.get(0);
				groupId = group.getIid();
				return groupId;
			} else {
				throw new OperationException("找到多个同名机构'" + groupName + "'， 无法准确定位");
			}
		}

		message = "无法找到所属机构";
		throw new OperationException(message);
	}

	/**
	 * 按姓名、登录名或姓名首字母缩写查询
	 * 
	 * @param keyword
	 *            关键字
	 * @return
	 */
	public List<User> findByNameOrPinYin(String keyword) {
		List<User> userList = userDAO.findByNameOrPinYin(keyword);
		return userList;
	}

	/**
	 * 获得角色相关的用户列表
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	public List<User> findUsersByRoleId(Integer roleId) {
		if (NumberUtil.getInt(roleId) == 0) {
			return null;
		}

		List<User> userList = userDAO.findUsersByRoleId(roleId);
		return userList;
	}

	public void addCommonReigon(String loginName, String region) {
		User user = findByLoginName(loginName);
		String oldCommonReigon = user.getCommonRegion();
		String newCommonReigon = null;
		if (StringUtil.isNotEmpty(oldCommonReigon)) {
			String breakWord = "\n";
			String breakWord1 = "\r";
			if (StringUtils.endsWith(oldCommonReigon, breakWord) || StringUtils.endsWith(oldCommonReigon, breakWord1)) {
				newCommonReigon = oldCommonReigon + region;
			} else {
				newCommonReigon = oldCommonReigon + breakWord1 + region;
			}
		} else {
			newCommonReigon = region;
		}
		user.setCommonRegion(newCommonReigon);
		userDAO.update(user);
	}
	
	
	public List<User> findAllUsers() { 		  
		  return userDAO.findAllUsers(); 
	}
	 
	public List<User> findByIds(String ids) {
		List<Integer> idsLsit = StringUtil.toIntegerList(ids, ",");
		if (CollectionUtils.isEmpty(idsLsit)) {
			return null;
		}
		List<User> userList = new ArrayList<User>();
		for (Integer integer : idsLsit) {
			User user =userDAO.findByIid(integer);
			userList.add(user);
		}		
		return userList;
	}
}
