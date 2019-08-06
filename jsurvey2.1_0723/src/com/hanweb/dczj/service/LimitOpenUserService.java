package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.LimitOpenUserDAO;
import com.hanweb.dczj.entity.LimitOpenUser;

public class LimitOpenUserService {

	@Autowired
	LimitOpenUserDAO limitOpenUserDAO;
	
	public boolean insertEntity(LimitOpenUser limitOpenUser) {
		int flag = 0;
		if(limitOpenUserDAO.findCountBydczjid(limitOpenUser.getDczjid())>0) {
			flag = limitOpenUserDAO.updateLimitidsBydczjd(limitOpenUser);
		}else {
			flag = limitOpenUserDAO.insert(limitOpenUser);
		}
		if(flag>0) {
			return true;
		}else {
			return false;
		}
	}
	
	public LimitOpenUser findEntityBydczjid(String dczjid) {
		return limitOpenUserDAO.findEntityBydczjid(NumberUtil.getInt(dczjid));
	}
	
}
