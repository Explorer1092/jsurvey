package com.hanweb.complat.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.PinyinUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.dao.OutsideUserDAO;
import com.hanweb.complat.entity.OutsideUser;
import com.hanweb.complat.exception.OperationException;

/**
 * jis 用户 webservice 的业务
 * 
 * @author 李杰
 * 
 */
public class OutSideUserWsService {

	@Autowired
	private OutsideUserDAO outsideUserDAO;

	public boolean addUser(OutsideUser outsideUser) throws OperationException {
		int num = outsideUserDAO.findIidByLoginName(null, outsideUser.getLoginName());
		if (num > 0) {
			throw new OperationException("登录名已存在,请重新设置！");
		}
		outsideUser.setRegtime(new Date());
		outsideUser.setPinYin(PinyinUtil.getHeadByString(outsideUser.getName()));
		int iid = outsideUserDAO.insert(outsideUser);

		return iid > 0 ? true : false;
	}

	public boolean modify(OutsideUser outsideUser) throws OperationException {
		outsideUser.setPinYin(PinyinUtil.getHeadByString(outsideUser.getName()));
		boolean isSuccess = outsideUserDAO.update(outsideUser);
		if (isSuccess && StringUtil.isNotEmpty(outsideUser.getPwd())) {
			isSuccess = outsideUserDAO.updatePwd(outsideUser.getIid(), outsideUser.getPwd());
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
	public boolean removeUser(String loginName) throws OperationException {
		if (StringUtil.isEmpty(loginName)) {
			throw new OperationException("loginId 为空");
		}
		return outsideUserDAO.deleteByLoginName(loginName);
	}

	/**
	 * 启用用户
	 * 
	 * @param loginName
	 * @return
	 */
	public boolean modifyEnableUser(String loginName) {
		return outsideUserDAO.updateEnable(loginName, 1);
	}

	/**
	 * 停用用户
	 * 
	 * @param loginName
	 * @return
	 */
	public boolean modifyDisableUser(String loginName) {
		return outsideUserDAO.updateEnable(loginName, 0);
	}

	/**
	 * 获得一个用户
	 * 
	 * @param loginId
	 * @return
	 */
	public OutsideUser findUser(String loginName) {
		return outsideUserDAO.findByLoginName(loginName);
	}
}
