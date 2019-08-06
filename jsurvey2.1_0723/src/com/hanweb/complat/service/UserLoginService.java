package com.hanweb.complat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hanweb.common.permission.PermissionUtil;
import com.hanweb.common.util.CheckPwdStrong;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.security.SecurityUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.entity.Right;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.LoginException;
import com.hanweb.support.controller.CurrentUser;

/**
 * 专门处理用户登陆的service
 * 
 * @author 李杰
 *
 */
@Service
public class UserLoginService {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private RightService rightService;

	@Autowired
	private GroupManagerService groupManagerService;

	/**
	 * 用户登录
	 * 
	 * @param loginName
	 * 
	 * @param pwd
	 * 
	 * @param ip
	 * 
	 * @throws ComplatException
	 * @return CurrentUser
	 */
	public CurrentUser checkUserLogin(String loginName, String pwd, String ip) throws LoginException {
		// 通过登录名获取一个用户的密码、主键id、是否可用
		User user = userService.findByLoginName(loginName);
		CurrentUser currentUser = null;
		// 判断是否有此用户
		if (user != null) {
			if (user.getEnable() == 0) {
				throw new LoginException("login.isnotallowed");
			}

			String storePwd = user.getPwd();

			if (Md5Util.isValidatePwd(pwd, storePwd)) {
				currentUser = wrapLoginUser(user, pwd, ip);
			}
		}
		return currentUser;
	}

	/**
	 * 用户登录
	 * 
	 * @param loginName
	 *            用户名
	 * @param dynamicCode
	 *            动态码
	 * @param ip
	 *            ip地址
	 * @return
	 * @throws LoginException
	 */
	public CurrentUser checkUserLogin(String loginName, String pwd, String dynamicCode, String ip)
			throws LoginException {
		// 通过登录名获取一个用户的密码、主键id、是否可用
		User user = userService.findByLoginName(loginName);
		CurrentUser currentUser = null;
		// 判断是否有此用户
		if (user != null) {
			if (user.getEnable() == 0) {
				throw new LoginException("login.isnotallowed");
			}

			if (Settings.getSettings().isDynamicCodeLogin()) {
				String dynamicCodeKey = user.getDynamicCodeKey();
				if (StringUtil.isEmpty(dynamicCodeKey)) {
					throw new LoginException("user.dynamiccode.error");
				}
				if (!SecurityUtil.isValidateAuthCode(NumberUtil.getInt(dynamicCode), dynamicCodeKey)) {
					throw new LoginException("login.dynamiccode.error");
				}
			}

			String storePwd = user.getPwd();
			if (Md5Util.isValidatePwd(pwd, storePwd)) {
				currentUser = wrapLoginUser(user, pwd, ip);
			}
		}
		return currentUser;
	}

	/**
	 * 为票据方式的单点登录而生
	 * 
	 * @param loginName
	 * @param pwd
	 * @param ip
	 * @return
	 * @throws LoginException
	 */
	public CurrentUser checkUserLogin(String loginName, String ip) throws LoginException {
		// 通过登录名获取一个用户的密码、主键id、是否可用
		User user = userService.findByLoginName(loginName);
		CurrentUser currentUser = null;
		// 判断是否有此用户
		if (user != null) {
			if (user.getEnable() == 0) {
				throw new LoginException("login.isnotallowed");
			}
			currentUser = wrapLoginUser(user, null, ip);
		}

		return currentUser;
	}

	/**
	 * 包装登陆的user
	 * 
	 * @param user
	 * @param pwd
	 * @param ip
	 * @return
	 */
	private CurrentUser wrapLoginUser(User user, String pwd, String ip) {
		int iid = user.getIid();
		CurrentUser currentUser = new CurrentUser();
		currentUser.setAddress(user.getAddress());
		currentUser.setAge(user.getAge());
		currentUser.setEmail(user.getEmail());
		currentUser.setEnable(user.getEnable());
		currentUser.setGroupId(user.getGroupId());
		currentUser.setHeadship(user.getHeadship());
		currentUser.setIid(user.getIid());
		currentUser.setLoginName(user.getLoginName());
		currentUser.setMobile(user.getMobile());
		currentUser.setName(user.getName());
		currentUser.setPhone(user.getPhone());
		currentUser.setPwd(user.getPwd());
		currentUser.setUuid(user.getUuid());
		if (StringUtil.isEmpty(pwd)) {
			currentUser.setPwdLevel(3);
		} else {
			currentUser.setPwdLevel(CheckPwdStrong.check(pwd));
		}
		currentUser.setSex(user.getSex());
		currentUser.setUuid(user.getUuid());
		currentUser.setContact(user.getContact());
		currentUser.setIp(ip);
		currentUser.setAccessTime(new Date());

		Integer rangeId = null;
		String rangeName = null;
		if (roleService.isSysAdminUser(iid)) {
			rangeId = 0;
			currentUser.setSysAdmin(true);
		} else if (roleService.isGroupAdminUser(iid)) {
			rangeId = groupManagerService.findRangeIdByUserId(iid);
			// 机构管理员 没设置管理范围时， 缺省取其所在机构
			if (rangeId == null) { 
				rangeId = user.getGroupId();
			}
			rangeName = groupService.findNameByIid(rangeId);
			currentUser.setGroupAdmin(true);
		}
		currentUser.setRangeId(rangeId);
		currentUser.setRangeName(rangeName);

		// 取得用户角色
		List<Role> roleList = new ArrayList<Role>();
		roleList = roleService.findUserRoles(iid);
		currentUser.setRoleList(roleList);

		// 取得菜单功能模块权限
		List<Right> rightList = new ArrayList<Right>(); 
		rightList = rightService.findUserRights(iid);
		currentUser.setRightList(rightList);

		PermissionUtil.organizePermissionKey(currentUser);
		return currentUser;
	}

	public CurrentUser getUser(User user, String password, String ip) {
		CurrentUser currentUser = null;
		currentUser = wrapLoginUser(user, password, ip);
		return currentUser;
	}
}
