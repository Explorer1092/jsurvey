package com.hanweb.complat.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.dao.BanListDAO;
import com.hanweb.complat.entity.BanList;

/**
 * 封停
 * 
 * @author 李杰
 * 
 */
public class BanListService {

	@Autowired
	private BanListDAO banListDAO;

	/**
	 * 更新封停状态
	 * 
	 * @param banList
	 */
	public void modify(BanList banList) {
		banList.setLoginDate(new Date());
		if (banList.getIid() != null) {
			banListDAO.update(banList);
		} else {
			banListDAO.insert(banList);
		}
	}

	/**
	 * 删除已封停的用户
	 * 
	 * @param id
	 *            用户ID
	 */
	public void removeById(Integer id) {
		banListDAO.deleteById(id);
	}

	/**
	 * 删除已封停的用户
	 * 
	 * @param ids
	 *            用户ID串 如:1,2,3,4
	 */
	public void removeByIds(String ids) {
		List<Integer> intIds = StringUtil.toIntegerList(ids, ",");
		banListDAO.deleteByIds(intIds);
	}

	/**
	 * 检查是否登陆次数过多
	 * 
	 * @param loginName
	 *            用户登录名
	 * @param ipAddr
	 *            用户登录IP
	 * @param userType
	 *            用户类型
	 * @return
	 */
	public BanList checkLoginTimes(String loginName, String ipAddr) {
		BanList banList = banListDAO.findByLoginNameAndIp(loginName, ipAddr);
		if (banList != null) {
			Date lastDate = banList.getLoginDate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(lastDate);
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)
					+ Settings.getSettings().getBanTimes());
			// String lastDate =
			// StringConvert.getFormatDateFromDate(banList.getLoginDate(),
			// "yyyy-MM-dd");
			Date now = new Date();
			boolean isBan = true;
			if (calendar.getTime().compareTo(now) <= 0) {
				isBan = false;
			}
			if (isBan
					&& banList.getLoginTimes().intValue() >= Settings.getSettings().getLoginError()) {
				banList.setCanLogin(false);
			} else if (isBan) {
				banList.setCanLogin(true);
			} else {
				banList.setCanLogin(true);
				banList.setLoginTimes(0);
			}
		} else {
			banList = new BanList();
			banList.setLoginName(loginName);
			banList.setIpAddr(ipAddr);
			banList.setCanLogin(true);
			banList.setLoginTimes(0);
		}
		return banList;
	}
}
