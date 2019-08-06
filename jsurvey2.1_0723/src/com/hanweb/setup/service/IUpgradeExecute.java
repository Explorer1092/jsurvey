package com.hanweb.setup.service;

import com.hanweb.setup.dao.DataInitDAO;

/**
 * 升级接口
 * @author 李杰
 *
 */
public interface IUpgradeExecute {
	
	/**
	 * 升级接口
	 * @param dataInitDAO
	 * @return
	 */
	public Object execute(DataInitDAO dataInitDAO);
}
