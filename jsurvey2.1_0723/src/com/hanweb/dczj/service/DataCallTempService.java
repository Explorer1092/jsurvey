package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.DataCallTempDAO;
import com.hanweb.dczj.entity.DataCallTemplate;

public class DataCallTempService {

	@Autowired
	DataCallTempDAO dataCallTempDAO;
	
	/**
	 * 通过jactid和类型查找模板
	 * @param jactid
	 * @param type
	 * @return
	 */
	public DataCallTemplate findTemplate(String datacallid, String type) {
		return dataCallTempDAO.findTemplate(datacallid,type);
	}

	/**
	 * 修改模板
	 * @param en
	 * @return
	 */
	public boolean modify(DataCallTemplate en) {
		return dataCallTempDAO.update(en);
	}

	/**
	 * 新增模板
	 * @param en
	 * @return
	 */
	public boolean add(DataCallTemplate en) {
		return dataCallTempDAO.insert(en) > 0;
	}
}
