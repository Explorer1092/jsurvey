package com.hanweb.dczj.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.ExcelUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.dczj.dao.SensitiveDAO;
import com.hanweb.dczj.entity.Sensitive;

public class SensitiveService {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	SensitiveDAO sensitiveDAO;
	
	/**
	 * 增加铭感词
	 * @param en
	 * @return
	 */
	public boolean add(Sensitive en) {
		return sensitiveDAO.insert(en) > 0;
	}

	/**
	 * 通过id获得一个铭感词
	 * 
	 * @param iid
	 * @return
	 */
	public Sensitive findById(Integer iid) {
		if (iid == null) {
			return null;
		}
		return sensitiveDAO.queryForEntityById(iid);
	}

	/**
	 * 修改
	 * 
	 * @param iid
	 * @return
	 */
	public boolean modify(Sensitive en) {
		return sensitiveDAO.update(en);
	}

	/**
	 * 删除操作
	 * @param integerList
	 * @return
	 */
	public boolean delete(List<Integer> integerList) {
		boolean deleteResult = false;
		deleteResult = sensitiveDAO.deleteByIds(integerList);
		return deleteResult;
	}

	/**
	 * 查询库中所有铭感词
	 * @param pagesize 
	 * @param  
	 * @return
	 */
	public List<Sensitive> findsensitiveList(int p, int pagesize) {
		return sensitiveDAO.findsensitiveList(p,pagesize);
	}

	/**
	 * 查询库中所有铭感词的数量
	 * @return
	 */
	public int getcount() {
		return sensitiveDAO.getcount();
	}

	/**
	 * 问题导入提交
	 * @param filePath
	 * @return
	 */
	public String importSensitive(File file) throws OperationException{
		if (file == null) {
			throw new OperationException("无法找到上传的文件！");
		}
		List<Map<String, String>> rows = ExcelUtil.readExcel(file);
		if (CollectionUtils.isEmpty(rows)) {
			throw new OperationException(SpringUtil.getMessage("import.filetype.error"));
		}
		
		List<Sensitive> sensitiveList = this.findSensitiveListByRows(rows);/* 读出数据 */
		String retMessage = "";
		
		try {
			retMessage = this.importSensitives(sensitiveList);/* 循环插入机构信息 */
			if (!retMessage.equals("")) {
				retMessage = "<div style='height:150px;overflow:auto'>导入完毕，存在以下问题：<br/>"
						+ retMessage + "</div>";
			}
			return retMessage;
		} catch (Exception e) {
			logger.error("import group error", e);
			return "导入失败";
		} finally {
			try {
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
				logger.error("delete file error", e);
			}
		}
	}

	/**
	 * 循环读取表中题目数据
	 * @param rows
	 * @return
	 */
	private List<Sensitive> findSensitiveListByRows(
			List<Map<String, String>> rows) {
		List<Sensitive> sensitiveList = new ArrayList<Sensitive>(); // 机构集合
		Map<String, String> cell = null;
		
		Sensitive sensitive = null;
		
		/* excel记录转换成用户集合 */
		Iterator<Map<String, String>> iterator = rows.iterator();
		while (iterator.hasNext()) {
			cell = iterator.next();
			sensitive = new Sensitive();
			sensitive.setVc_sensitiveword(StringUtil.trim(cell.get("敏感词名称")));
			
			sensitiveList.add(sensitive);
		}
		return sensitiveList;
	}
	
	/**
	 * 循环插入数据
	 * @param sensitiveList
	 * @return
	 */
	private String importSensitives(List<Sensitive> sensitiveList) throws OperationException {
		if (sensitiveList == null) {
			return "";
		}
		Sensitive sensitive = null;  
		String message = "";
		StringBuilder result = new StringBuilder();
		
		for(int i = 0;i < sensitiveList.size();i++){
			sensitive = sensitiveList.get(i);
			if(StringUtil.isEmpty(sensitive.getVc_sensitiveword())){
				message = "该敏感词不能为空";
				this.getReturnMessage(result, i, message);
				continue;
			}
			
			sensitiveDAO.insert(sensitive);
		}
		return result.toString();
	}

	/**
	 * 组织导入的错误提示
	 * 
	 * @param result
	 *            错误提示信息集
	 * @param i
	 *            信息在List中的序号
	 * @param message
	 *            错误信息
	 */
	private void getReturnMessage(StringBuilder result, int i, String message) {
		result.append("<li>");
		result.append("[" + SpringUtil.getMessage("import.error", i + 2) + "]" + message);
		result.append("</li>");
	}

	public boolean checkduplicate (Sensitive en){
		if(en == null){
			return false;
		}
		int count = sensitiveDAO.findCountByName(en.getVc_sensitiveword());
		return count>0 ? false : true;
	}
}
