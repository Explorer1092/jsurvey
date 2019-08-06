package com.hanweb.setup.controller;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.reg.LicenceCheck;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.LogUtil;
import com.hanweb.common.util.Md5Util;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.jis.expansion.entity.JisAppEntity;
import com.hanweb.jis.expansion.webservice.AppConfig;

/**
 * setup
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping("setup/main")
public class BaseSetupController {
	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping("index")
	public ModelAndView main() {
		ModelAndView modelAndView = new ModelAndView("setup/main");
		modelAndView.addObject("baseSetup", "basesetup.do");
		modelAndView.addObject("dbSetup", "db/dbsetup.do");
		modelAndView.addObject("center_url", "basesetup.do");
		modelAndView.addObject("logout_url", "../logout.do");
		modelAndView.addObject("transferSetup", "transfer/transfersetup.do");
		return modelAndView;
	}

	@RequestMapping("basesetup")
	public ModelAndView baseSetup() {
		Properties setupProp = getSetupProp();
		Properties versionProp = getVersionProp();
		ModelAndView modelAndView = new ModelAndView("setup/basesetup");
		modelAndView.addObject("url", "save.do");
		modelAndView.addObject("appPath", BaseInfo.getRealPath());
		modelAndView.addObject("kick", setupProp.getString("kick"));
		modelAndView.addObject("sso", setupProp.getString("sso"));
		modelAndView.addObject("sync", setupProp.getString("sync"));
		modelAndView.addObject("code", setupProp.getString("code"));
		modelAndView.addObject("debug", setupProp.getString("debug"));
		modelAndView.addObject("domain", setupProp.getString("domain"));
		String appName = versionProp.getString("appname");
		modelAndView.addObject("appname", appName);
		modelAndView.addObject("version", versionProp.getString("version"));
		int state = LicenceCheck.regState(BaseInfo.getRealPath(), appName + ".licence", appName);
		String message = LicenceCheck.regMessage(BaseInfo.getRealPath(), appName + ".licence", appName);
		modelAndView.addObject("regState", state);
		modelAndView.addObject("regMessage", message);
		JisAppEntity appEntity = new JisAppEntity();
		appEntity.setAppmark(AppConfig.CONFIG.getAppmark());
		appEntity.setEnckey(AppConfig.CONFIG.getEnckey());
		appEntity.setEncrypttype(AppConfig.CONFIG.getEncrypttype());
		appEntity.setJisurl(AppConfig.CONFIG.getJisurl());
		modelAndView.addObject("jisConfig", appEntity);
		modelAndView.addObject("machineCode",
				LicenceCheck.getMachineCode(BaseInfo.getRealPath(), appName + ".licence", appName));
		modelAndView.addObject("checkPasswordLevel", Settings.getSettings().getCheckLevel());
		return modelAndView;
	}

	@RequestMapping("save")
	@ResponseBody
	public String save(BaseSetupForm setupForm) {
		MultipartFile licence = setupForm.getLicence();
		if (setupForm.getLicence() != null) {
			MultipartFileInfo fileInfo = MultipartFileInfo.getInstance(licence);
			String fileType = "licence";
			if (StringUtil.equals(fileInfo.getFileType(), fileType)) {
				String licencePath = BaseInfo.getRealPath() + "/" + BaseInfo.getAppName() + ".licence";
				File file = new File(licencePath);
				try {
					FileUtil.writeInputStreamToFile(file, licence.getInputStream());
				} catch (Exception e) {
					logger.error("save error", e);
				}
			}
		}
		Properties properties = getSetupProp();
		properties.setProperty("kick", setupForm.getKick());
		properties.setProperty("sso", setupForm.getSso());
		properties.setProperty("sync", setupForm.getSync());
		properties.setProperty("code", setupForm.getCode());
		properties.setProperty("debug", setupForm.getDebug());
		// properties.setProperty("appname", setupForm.getAppName());
		if (StringUtil.isNotEmpty(setupForm.getPassword())) {
			properties.setProperty("adminpw", Md5Util.encodePwd(setupForm.getPassword()));
		}
		String domain = setupForm.getDomain();
		if (StringUtil.isNotEmpty(domain)) {
			domain = StringUtil.convertPath(domain.trim().toLowerCase(), false);
			String start = "http";
			String end = "/";
			if (domain.startsWith(start) && !domain.endsWith(end)) {
				properties.setProperty("domain", domain);
			}
		}
		properties.save();
		saveLogLevel(setupForm.getDebug());

		// 保存jis设置
		AppConfig.writeJisApp(setupForm.getJisConfig());

		BaseInfo.loadSystemConfig();
		Script script = Script.getInstanceWithJsLib();
		script.addAlert("保存成功").reloadParent();
		return script.getScript();
	}

	private Properties getSetupProp() {
		// 考虑db的配置和设置项分开
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/setup.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}

	private Properties getVersionProp() {
		String dbProp = BaseInfo.getRealPath() + "/WEB-INF/config/version.properties";
		Properties properties = new Properties(dbProp);
		return properties;
	}

	private void saveLogLevel(int logLevle) {
		LogUtil.changeLogLevel(logLevle);
	}
}
