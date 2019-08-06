package com.hanweb.complat.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.PinyinUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.dao.OutsideUserDAO;
import com.hanweb.complat.entity.OutsideUser;
import com.hanweb.complat.exception.OperationException;

/**
 * 外网用户Service
 * 
 * @author ZhangC
 * 
 */
public class OutsideUserService {

	@Autowired
	private OutsideUserDAO outsideUserDAO;

	/**
	 * 新增前台用户
	 * 
	 * @param outsideUser
	 *            前台用户实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean add(OutsideUser outsideUser) throws OperationException {
		if (outsideUser == null) {
			return false;
		}

		int num = outsideUserDAO.findIidByLoginName(null, outsideUser.getLoginName());
		if (num > 0) {
			throw new OperationException("存在相同登录名的用户！");
		}

		outsideUser.setUuid(StringUtil.getUUIDString());
		outsideUser.setPwd(Md5Util.encodePwd(outsideUser.getPwd()));
		outsideUser.setRegtime(new Date());
		outsideUser.setPinYin(PinyinUtil.getHeadByString(outsideUser.getName()));
		int iid = outsideUserDAO.insert(outsideUser);

		return iid > 0 ? true : false;
	}

	/**
	 * 更新前台用户
	 * 
	 * @param outsideUser
	 *            前台用户实体
	 * @return true - 成功<br/>
	 *         false - 失败
	 * @throws OperationException
	 *             界面异常
	 */
	public boolean modify(OutsideUser outsideUser) throws OperationException {
		if (outsideUser == null || outsideUser.getIid() <= 0) {
			return false;
		}
		int num = outsideUserDAO.findIidByLoginName(outsideUser.getIid(), outsideUser.getLoginName());
		if (num > 0) {
			throw new OperationException("存在相同登录名的用户！");
		}
		boolean isSuccess;
		outsideUser.setPinYin(PinyinUtil.getHeadByString(outsideUser.getName()));
		isSuccess = outsideUserDAO.update(outsideUser);
		if (isSuccess && StringUtils.isNotBlank(outsideUser.getPwd())) {
			outsideUser.setPwd(Md5Util.encodePwd(outsideUser.getPwd()));
			isSuccess = outsideUserDAO.updatePwd(outsideUser.getIid(), outsideUser.getPwd());
		}

		return isSuccess;
	}
	
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
			success = outsideUserDAO.updatePwd(userId, Md5Util.encodePwd(password));
		}
		return success;
	}
	
	/**
	 * 删除前台用户
	 * 
	 * @param ids
	 *            前台用户ID串 如:1,2,3
	 * @return true - 成功<br/>
	 *         false - 失败
	 */
	public boolean removeByIds(String ids) {
		List<Integer> idsList = StringUtil.toIntegerList(ids, ",");
		if (CollectionUtils.isEmpty(idsList)) {
			return false;
		}
		boolean isSuccess = outsideUserDAO.deleteByIds(idsList);

		return isSuccess;
	}

	/**
	 * 通用角色id获得角色信息
	 * 
	 * @param iid
	 *            角色id
	 * @return
	 */
	public OutsideUser findByIid(int iid) {
		return outsideUserDAO.findByIid(iid);
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
		return outsideUserDAO.updateEnable(iid, enable);
	}

}
