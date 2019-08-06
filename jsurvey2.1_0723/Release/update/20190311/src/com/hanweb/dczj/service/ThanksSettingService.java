package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.dczj.dao.ThanksSettingDAO;
import com.hanweb.dczj.entity.ThanksSetting;

public class ThanksSettingService {

	@Autowired
	ThanksSettingDAO thanksSettingDAO;
	
	/**
	 * 通过dczjid获取实体
	 * @param dczjid
	 * @return
	 */
	public ThanksSetting getSettingBydczjid(String dczjid) {
		Integer id = NumberUtil.getInt(dczjid);
		return thanksSettingDAO.getSettingBydczjid(id);
	}
	
	/**
	 * 通过dczjid更新实体
	 * @param thanksSetting
	 * @return
	 */
	public boolean updateEntity(ThanksSetting thanksSetting) {
		try {
			int a = 0;
			if(thanksSettingDAO.getSettingBydczjid(thanksSetting.getDczjid())==null) {
				a = thanksSettingDAO.insert(thanksSetting);
			}else {
				a = thanksSettingDAO.updateBydczjid(thanksSetting);
			}
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
			ThanksSetting thanksSetting = new ThanksSetting();
			thanksSetting.setDczjid(NumberUtil.getInt(dczjid));
			thanksSetting.setThankscontent("提交成功，感谢您的参与！");
			thanksSetting.setIsjump(0);
			int a = thanksSettingDAO.insert(thanksSetting);
			if(a > 0) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
