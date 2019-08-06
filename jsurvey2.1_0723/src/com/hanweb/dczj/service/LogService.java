package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.dao.LogDAO;
import com.hanweb.dczj.entity.Dczj_Log;

public class LogService {

	@Autowired
	private LogDAO logdao;
	/**
	 * 增加操作记录
	 * @param log
	 * @return
	 */
	public boolean add(Dczj_Log log){
		if(log == null){
			return false;
		}
		User currentUser = UserSessionInfo.getCurrentUser();
		if(currentUser != null){
			log.setOpruser(currentUser.getName()+"("+currentUser.getLoginName()+")");
		}
		String userIp = IpUtil.getIp();
		String [] ips = userIp.split(",");
		if (ips != null && ips.length > 0){ //防止双IP
		    userIp = StringUtil.getString(ips[0]);
		}
		log.setOpruserip(userIp);
		String currentTime = DateUtil.getCurrDate(DateUtil.YYYY_MM_DD_HH_MM);
		log.setOprcreatedate(currentTime);
		return logdao.insert(log) > 0 ? true : false;
	}
	/**
	 * 删除记录
	 * @param integerList
	 * @return
	 */
	public boolean delete(List<Integer> integerList) {
		return logdao.deleteByiids(integerList);
	}
}
