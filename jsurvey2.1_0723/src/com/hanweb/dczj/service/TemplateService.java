package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.dczj.dao.TemplateDAO;
import com.hanweb.dczj.entity.Template;

public class TemplateService {

	@Autowired
	private TemplateDAO templateDAO;
	
	public List<Template> findTemplatByDczjidAndType(String dczjid, int type) {
		return templateDAO.findTemplatByDczjidAndType(dczjid,type);
	}

	public Template findTemplatByDczjidAndTypeAndPagetype(String dczjid, int pagetype) {
		return templateDAO.findTemplatByDczjidAndTypeAndPagetype(dczjid,pagetype);
	}
	
	public Integer insert(Template template) {
		return templateDAO.insert(template);
	}

	public boolean update(Template template) {
		return templateDAO.update(template);
	}

}
