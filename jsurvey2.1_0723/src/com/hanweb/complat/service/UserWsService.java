package com.hanweb.complat.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.PinyinUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.dao.UserDAO;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.exception.OperationException;

/**
 * jis 用户 webservice 的业务
 * 
 * @author 李杰
 * 
 */
public class UserWsService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDAO userDAO;

	/**
	 * 新增用户
	 * 
	 * @param user
	 * @return
	 * @throws OperationException
	 */
	public boolean addUser(User user) throws OperationException {
		int num = userDAO.findIidByLoginName(user.getIid(), user.getLoginName());
		if (num > 0) {
			throw new OperationException("登录名已存在,请重新设置！");
		}
		user.setCreatetime(new Date(0));
		user.setPinYin(PinyinUtil.getHeadByString(user.getName()));

		int iid = userDAO.insert(user);

		return iid > 0 ? true : false;
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 * @return
	 * @throws OperationException
	 */
	public boolean modifyUser(User user) throws OperationException {
		boolean isSuccess = false;
		int num = userDAO.findIidByLoginName(user.getIid(), user.getLoginName());
		if (num > 0) {
			throw new OperationException("登录名已存在,请重新设置！");
		}
		// user.setPwd(Md5Util.md5encode(user.getPwd()));
		user.setPinYin(PinyinUtil.getHeadByString(user.getName()));
		isSuccess = userDAO.update(user);
		if (isSuccess && StringUtil.isNotEmpty(user.getPwd())) {
			isSuccess = userDAO.updatePassword(user.getIid(), user.getPwd());
		}
		return isSuccess;
	}

	/**
	 * 删除用户
	 * 
	 * @param loginId
	 * @return
	 * @throws OperationException
	 */
	public boolean removeUser(String loginId) throws OperationException {
		boolean isSuccess = false;
		User user = userService.findByLoginName(loginId);
		if (user != null) {
			isSuccess = userService.removeByIds(user.getIid().intValue() + "");
		}
		return isSuccess;
	}

	/**
	 * 启用用户
	 * 
	 * @param loginId
	 * @return
	 */
	public boolean modifyEnableUser(String loginId) {
		return userDAO.updateEnable(loginId, 1);
	}

	/**
	 * 停用用户
	 * 
	 * @param loginId
	 * @return
	 */
	public boolean modifyDisableUser(String loginId) {
		return userDAO.updateEnable(loginId, 0);
	}

	/**
	 * 获得一个用户
	 * 
	 * @param loginId
	 * @return
	 */
	public User findUser(String loginId) {
		return userService.findByLoginName(loginId);
	}
}
