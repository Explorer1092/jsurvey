package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.CommonTemplateStyleDAO;
import com.hanweb.dczj.entity.CommonTemplateStyle;

public class CommonTemplateStyleService {

	@Autowired
	private CommonTemplateStyleDAO commonTemplateStyleDAO;
	
	public Integer insert(CommonTemplateStyle commonTemplateStyle) {
		return commonTemplateStyleDAO.insert(commonTemplateStyle);
	}

	public List<CommonTemplateStyle> findListByWebid(int webid, int type) {
		return commonTemplateStyleDAO.findListByWebid(webid,type);
	}

	public CommonTemplateStyle findListByIid(int iid) {
		return commonTemplateStyleDAO.queryForEntityById(iid);
	}

	public Boolean update(CommonTemplateStyle commonTemplateStyle) {
		return commonTemplateStyleDAO.update(commonTemplateStyle);
	}

}
