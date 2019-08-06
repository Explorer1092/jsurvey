package com.hanweb.dczj.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.json.Type;
import com.hanweb.dczj.dao.VisitCountDAO;
import com.hanweb.dczj.entity.VisitCount;

public class VisitCountService {

	@Autowired
	private VisitCountDAO visitCountDAO;
	
	/**
	 * 查找VC
	 * @param jsurveyid
	 * @return
	 */
	public int findVistCount(String jsurveyid) {
		int count = 0;
		VisitCount vcEn = visitCountDAO.findVistCount(jsurveyid);
		if(vcEn != null) {
			count = vcEn.getVisitcount();
		}
	    return count;
	}

	/**
	 * 修改或新增VC
	 * @param visitcount
	 * @return
	 */
	public boolean modifyVc(String jsurveyid) {
		Cache cache = CacheManager.getInstance("jsurvey_visitcount");
		String vc = cache.get("jsurveyvc_"+jsurveyid, new Type<String>(){});
		int visitcount = 0;
		if(StringUtil.isNotEmpty(vc)) {
			visitcount = NumberUtil.getInt(vc);
		}
		if(visitcount == 0) {
			return true;
		}
		VisitCount vcEn = visitCountDAO.findVistCount(jsurveyid);
		boolean bl = false;
		if(vcEn != null) {
			vcEn.setVisitcount(visitcount);
			bl = visitCountDAO.update(vcEn);
		}else {
			vcEn = new VisitCount();
			vcEn.setDczjid(NumberUtil.getInt(jsurveyid));
			vcEn.setVisitcount(visitcount);
			bl = visitCountDAO.insert(vcEn) >0 ? true : false;
		}
		return bl;
	}

}
