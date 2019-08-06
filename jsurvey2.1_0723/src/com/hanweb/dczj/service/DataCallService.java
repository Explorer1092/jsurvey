package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.DataCallDAO;
import com.hanweb.dczj.entity.DataCall;

public class DataCallService {

	@Autowired
	DataCallDAO dataCallDAO;
	
	/**
	 * 新增实体
	 * @param en
	 * @return
	 */
	public int add(DataCall en) {
		return dataCallDAO.insert(en);
	}

	/**
	 * 删除实体
	 * @param ids
	 * @return
	 */
	public boolean delete(List<Integer> ids) {
		return dataCallDAO.deleteByid(ids, "1");
	}

	public DataCall findJactByIid(int iid) {
		return dataCallDAO.queryForEntityById(iid);
	}

	public boolean modifyEntity(DataCall en) {
		return dataCallDAO.UpdateById(en);
	}
	
	public boolean modify(DataCall en) {
		return dataCallDAO.update(en);
	}

	public boolean setUpdateHtml(Integer iid, int updateHtml) {
		return dataCallDAO.setUpdateHtml(iid,updateHtml);
	}

	public List<DataCall> findAllJact() {
		return dataCallDAO.findAllJact();
	}
}
