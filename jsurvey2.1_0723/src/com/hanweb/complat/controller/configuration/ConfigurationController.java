package com.hanweb.complat.controller.configuration;

import java.io.File;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.constant.StaticValues;
import com.hanweb.complat.exception.OperationException;

/**
 * 系统参数设置控制器
 * 
 * @author 李杰
 * 
 */
@Controller
@Permission(module = "configuration")
@RequestMapping("manager/configuration")
public class ConfigurationController {

	/**
	 * 打开系统参数设置页面
	 * 
	 * @return
	 */
	@RequestMapping("modify_show")
	public ModelAndView showModify() {
		ModelAndView modelAndView = new ModelAndView("/complat/configuration/configuration_opr");
		modelAndView.addObject("setting", Settings.getSettings());
		modelAndView.addObject("url", "update_submit.do");
		return modelAndView;
	}

	/**
	 * 保存系统参数
	 * 
	 * @param settings
	 *            系统参数实体
	 * @return
	 */
	@Permission(function = "update_submit")
	@RequestMapping("update_submit")
	@ResponseBody
	public String submitUpdate(Settings settings, MultipartFile file) {
		File desFile = new File(StaticValues.QRCODE_REAL_PATH);
		Script script = Script.getInstanceWithJsLib();
		String message = "";
		if (MultipartFileInfo.isValid(file)) {
			try {
				MultipartFileInfo info = MultipartFileInfo.getInstance(file);
				String typeName = "png";
				if (StringUtil.equals(info.getFileType(), typeName)) {
					ControllerUtil.writeMultipartFileToFile(desFile, file);
				} else {
					throw new OperationException("文件类型不正确，支持png格式");
				}
			} catch (OperationException e) {
				message = e.getMessage();
			}
		}
		Settings.save(settings);
		if (StringUtil.isNotEmpty(message)) {
			script.addAlert(message);
		} else {
			script.addScript("parent.location.reload()");
		}
		return script.getScript();
	}
}
