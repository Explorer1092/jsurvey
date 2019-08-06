package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.LimitLoginUserDAO;
import com.hanweb.dczj.entity.LimitLoginUser;

public class LimitLoginUserService {
	
	@Autowired
	LimitLoginUserDAO limitLoginUserDAO;
	
	public boolean insertEntity(LimitLoginUser limitLoginUser) {
		int flag = 0;
		if(limitLoginUserDAO.findCountBydczjid(limitLoginUser.getDczjid())>0) {
			flag = limitLoginUserDAO.updateLimitidsBydczjd(limitLoginUser.getDczjid(), limitLoginUser.getLimitids(),limitLoginUser.getLimittype());
		}else {
			flag = limitLoginUserDAO.insert(limitLoginUser);
		}
		if(flag>0) {
			return true;
		}else {
			return false;
		}
	}
	
	public LimitLoginUser findEntityBydczjid(String dczjid,Integer limittype) {
		return limitLoginUserDAO.findEntityBydczjid(NumberUtil.getInt(dczjid),limittype);
	}

}
