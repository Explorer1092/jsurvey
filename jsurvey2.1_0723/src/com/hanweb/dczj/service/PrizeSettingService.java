package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.PrizeSettingDAO;
import com.hanweb.dczj.entity.PrizeSetting;

public class PrizeSettingService {

	@Autowired
	PrizeSettingDAO prizeSettingDAO;
	
	public boolean updateEntity(PrizeSetting setting) {
		return prizeSettingDAO.update(setting);
	}
	
	/**
	 * 根据iid查找实体
	 * @param iid
	 * @return
	 */
	public PrizeSetting getEntityByIid(Integer iid) {
		return prizeSettingDAO.queryForEntityById(iid);
	}
	
	/**
	 * 通过dczjid获取实体
	 * @param dczjid
	 * @return
	 */
	public List<PrizeSetting> getSettingBydczjid(String dczjid) {
		Integer id = NumberUtil.getInt(dczjid);
		return prizeSettingDAO.getSettingBydczjid(id);
	}
	
	/**
	 * 通过dczjid更新实体
	 * @param thanksSetting
	 * @return
	 */
	public boolean modify(PrizeSetting prizeSetting) {
		try {
			int a = prizeSettingDAO.updateById(prizeSetting);
			if(a > 0) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 初始创建
	 * @return
	 */
	public boolean init(String dczjid) {
		try {
			int dczj_id = NumberUtil.getInt(dczjid);
			//初始创建6条记录
			for(int i = 1;i<=6;i++) {
				PrizeSetting prizeSetting = new PrizeSetting();
				prizeSetting.setDczjid(dczj_id);
				prizeSetting.setPrizeid(i);
				prizeSetting.setPrizename("");
				prizeSetting.setPrizenumber(0);
				prizeSetting.setPrizeprobability((float) 0.01);
				prizeSettingDAO.insert(prizeSetting);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
