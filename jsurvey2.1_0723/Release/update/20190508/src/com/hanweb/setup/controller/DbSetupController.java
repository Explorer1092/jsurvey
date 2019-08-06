package com.hanweb.setup.controller;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.basedao.EntityManager;
import com.hanweb.common.basedao.SqlType;
import com.hanweb.common.datasource.DataSourceConfig;
import com.hanweb.common.datasource.DataSourceManager;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.setup.service.DataInitService;

/**
 * 数据库设置
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping("setup/main/db")
public class DbSetupController {
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private DataInitService dataInitService;

	@RequestMapping("dbsetup")
	public ModelAndView modify() {
		Properties prop = getSetupProp();
		boolean isInit = BooleanUtils.toBoolean(prop.getInt("init"), 1, 0);
		boolean isSetDb = StringUtils.isNotBlank(prop.getString("url"))
				|| StringUtils.isNotBlank(prop.getString("jndi"));
		boolean canInit = false;
		if (isSetDb && !isInit) {
			canInit = true;
		}
		ModelAndView modelAndView = new ModelAndView("setup/dbsetup");
		modelAndView.addObject("dbtype", prop.getString("dbtype"));
		modelAndView.addObject("canInit", canInit);
		modelAndView.addObject("prepared", BaseInfo.isPrepared());
		modelAndView.addObject("url", "save.do");
		modelAndView.addObject("ip", prop.getString("ip"));
		modelAndView.addObject("port", prop.getString("port"));
		modelAndView.addObject("dbName", prop.getString("dbname"));
		modelAndView.addObject("dbUser", DataSourceConfig.decode(prop.getString("username")));
		return modelAndView;
	}

	@RequestMapping("save")
	@ResponseBody
	public JsonResult save(DataSourceConfig dataSourceConfig, HttpServletResponse response) {
		JsonResult jsonResult = JsonResult.getInstance();
		dataSourceConfig.config();
		saveProp(dataSourceConfig);
		BaseInfo.loadSystemConfig();
		DataSourceManager.loadDefaultDataSource();
		EntityManager.init();
		jsonResult.setSuccess(true);
		if (BaseInfo.isPrepared()) {
			jsonResult.setMessage("设置成功");
		} else {
			jsonResult.setMessage("设置成功，请初始化数据");
		}
		return jsonResult;
	}

	@RequestMapping("initdb")
	@ResponseBody
	public JsonResult initDB() {
		JsonResult jsonResult = JsonResult.getInstance();
		String message = "初始化数据库成功，请重启应用";
		try {
			dataInitService.addData();
			dataInitService.initDateBase();
			Properties prop = getSetupProp();
			prop.setProperty("init", 1);
			prop.save();
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			logger.error("initDB error", e);
			message = "初始化数据库失败，请查看日志";
		}
		jsonResult.setMessage(message);
		BaseInfo.loadSystemConfig();
		return jsonResult;
	}

	private void saveProp(DataSourceConfig dataSourceConfig) {
		Properties prop = getSetupProp();
		prop.setProperty("driver", dataSourceConfig.getDbDriver());
		prop.setProperty("url", dataSourceConfig.getUrl());
		prop.setProperty("username", dataSourceConfig.getDbUserEncode());
		String password = dataSourceConfig.getDbPassword();
		if (!"".equals(password)) {
			prop.setProperty("password", dataSourceConfig.getDbPasswordEncode());
		}
		prop.setProperty("validationQuery", dataSourceConfig.getValidationQuery());
		prop.setProperty("dbtype", dataSourceConfig.getDbType());
		prop.setProperty("ip", dataSourceConfig.getIp());
		prop.setProperty("port", dataSourceConfig.getPort());
		prop.setProperty("dbname", dataSourceConfig.getDbName());
		prop.save();
	}

	private Properties getSetupProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}

	@RequestMapping("showSql")
	@ResponseBody
	public String showSql() {
		Collection<Map<SqlType, String>> s = EntityManager.getEntitiesSql();
		StringBuffer sb = new StringBuffer();
		for (Map<SqlType, String> map : s) {
			sb.append(map.get(SqlType.TABLE) + "<br>").append("<br>");
		}
		return sb.toString();
	}
}
