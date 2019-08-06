package com.hanweb.setup.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.datasource.DataSourceConfig;
import com.hanweb.common.datasource.DataSourceSwitch;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.dao.TitleInfoDAO;
import com.hanweb.dczj.service.DataMigrationService;

@Controller
@RequestMapping("setup/main/transfer")
public class TransferSetupController {
	
	@Autowired
	private TitleInfoDAO infoDAO;
	@Autowired
	private DataMigrationService dataMigrationService;
	
	
	@RequestMapping("transfersetup")
	public ModelAndView modify() {
		Properties prop = getSetupProp();
		boolean isInit = BooleanUtils.toBoolean(prop.getInt("init"), 1, 0);
		boolean isSetDb = StringUtils.isNotBlank(prop.getString("url")) || StringUtils.isNotBlank(prop.getString("jndi"));
		boolean canInit = false;
		if (isSetDb && !isInit) {
			canInit = true;
		}
		ModelAndView modelAndView = new ModelAndView("setup/transfersetup");
		modelAndView.addObject("dbtype", prop.getString("dbtype"));
		modelAndView.addObject("canInit", canInit);
		modelAndView.addObject("prepared", BaseInfo.isPrepared());
		modelAndView.addObject("url", "save.do");
		modelAndView.addObject("ip", prop.getString("ip"));
		modelAndView.addObject("port", prop.getString("port"));
		modelAndView.addObject("dbName", prop.getString("dbname"));
		modelAndView.addObject("dbUser", prop.getString("username"));
		return modelAndView;
	}

	@RequestMapping("save")
	@ResponseBody
	public JsonResult save(DataSourceConfig dataSourceConfig, HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = JsonResult.getInstance();
		saveProp(dataSourceConfig);
		jsonResult.setSuccess(true);
		jsonResult.setMessage("保存成功，请重启应用再进行迁移");
		return jsonResult;
	}
	
	@RequestMapping("transfer")
	@ResponseBody
	public JsonResult transfer(DataSourceConfig dataSourceConfig, HttpServletRequest request, HttpServletResponse response) {
		JsonResult jsonResult = JsonResult.getInstance();
		String out_webid = StringUtil.getSafeString(request.getParameter("out_webid"));
		String in_webid = StringUtil.getSafeString(request.getParameter("in_webid"));
		String transfer_content = StringUtil.getSafeString(request.getParameter("transfer_content"));
		if(out_webid==null || in_webid==null || transfer_content==null) {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("输入不能为空");
			return jsonResult;
		}
		try {
			System.out.println("验证数据库连接");
			DataSourceSwitch.change("2");
			System.out.println("切换数据源");
			String sql = "SELECT count(1) FROM survey_form";
			Query query = infoDAO.createQuery(sql);
			if(infoDAO.queryForInteger(query)<=0) {
				jsonResult.setSuccess(false);
				jsonResult.setMessage("迁出站点表不存在或表中没有信息");
				return jsonResult;
			}
		}catch(Exception e) {
			e.printStackTrace();
			jsonResult.setSuccess(false);
			jsonResult.setMessage("数据库链接有误");
			return jsonResult;
		}
		
		try {
			if(StringUtil.equals("1", transfer_content)) {
				dataMigrationService.surveyDateMingration(out_webid, in_webid);
			}else if(StringUtil.equals("2", transfer_content)){
				dataMigrationService.ideaDateMingration(out_webid, in_webid);
			}else if(StringUtil.equals("3", transfer_content)){
				dataMigrationService.evaluationDateMingration(out_webid, in_webid);
			}else {
				jsonResult.setSuccess(false);
				jsonResult.setMessage("输入不能为空");
				return jsonResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonResult.setSuccess(false);
			jsonResult.setMessage("迁移失败，请联系系统管理员");
			return jsonResult;
		}
		
		
		jsonResult.setSuccess(true);
		jsonResult.setMessage("数据迁移成功");
		return jsonResult;
	}
	
	
	private void saveProp(DataSourceConfig dataSourceConfig) {
		Properties prop = getSetupProp();
		prop.setProperty("username", dataSourceConfig.getDbUser());
		String password = dataSourceConfig.getDbPassword();
		if (!"".equals(password)) {
			prop.setProperty("password", dataSourceConfig.getDbPassword());
		}
		//prop.setProperty("validationQuery", dataSourceConfig.getValidationQuery());
		prop.setProperty("dbtype", dataSourceConfig.getDbType());
		prop.setProperty("ip", dataSourceConfig.getIp());
		prop.setProperty("port", dataSourceConfig.getPort());
		prop.setProperty("dbname", dataSourceConfig.getDbName());
		prop.save();
	}

	private Properties getSetupProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/extend_db/transfer.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}
}
