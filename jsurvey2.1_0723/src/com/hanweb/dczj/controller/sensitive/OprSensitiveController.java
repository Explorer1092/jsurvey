package com.hanweb.dczj.controller.sensitive;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.Sensitive;
import com.hanweb.dczj.service.SensitiveService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;


@Controller
@RequestMapping("manager/sensitive")
public class OprSensitiveController {

	@Autowired
	SensitiveService sensitiveService;
	
	@RequestMapping("add_show")
	public ModelAndView showAdd() {
		ModelAndView modelAndView = new ModelAndView("/dczj/sensitive/sensitive_opr");
		Sensitive en = new Sensitive();
		modelAndView.addObject("en", en);
		modelAndView.addObject("url", "add_submit.do");
		return modelAndView;
	}
	
	@RequestMapping("add_submit")
	@ResponseBody
	public JsonResult submitAdd(Sensitive en) {
		JsonResult jsonResult = JsonResult.getInstance();
		if(en != null){
			boolean result = false;
			result = sensitiveService.checkduplicate(en);
			if(result){
				result = sensitiveService.add(en);
				if(result){
					String userIp = IpUtil.getIp();
					String [] ips = userIp.split(",");
					if (ips != null && ips.length > 0){ //防止双IP
					    userIp = StringUtil.getString(ips[0]);
					}
					CurrentUser currentUser = UserSessionInfo.getCurrentUser();
					LogRecorder.record(LogEntity.getInstance().setModelName("敏感词管理").setFunctionName("新增")
							.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("新增敏感词："+en.getVc_sensitiveword()));
					
					jsonResult.set(ResultState.ADD_SUCCESS);
				}else{
					jsonResult.setMessage("新增失败，请联系管理员！");
					jsonResult.setSuccess(false);
				} 
			}else{
				jsonResult.setMessage("敏感词已存在！");
				jsonResult.setSuccess(false);
			}
		}else{
			jsonResult.setMessage("敏感词实体为空，请联系管理员！");
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}
	
	@RequestMapping("modify_show")
	public ModelAndView showModify(Integer iid) {
		ModelAndView modelAndView = new ModelAndView("/dczj/sensitive/sensitive_opr");
		modelAndView.addObject("url", "modify_submit.do");
		Sensitive en = sensitiveService.findById(iid);
		modelAndView.addObject("en", en);
		return modelAndView;
	}

	@RequestMapping("modify_submit")
	@ResponseBody
	public JsonResult submitModify(Sensitive en) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean result = false;
		result = sensitiveService.checkduplicate(en);
		if(result){
			result = sensitiveService.modify(en);
			if(result){
				String userIp = IpUtil.getIp();
				String [] ips = userIp.split(",");
				if (ips != null && ips.length > 0){ //防止双IP
				    userIp = StringUtil.getString(ips[0]);
				}
				CurrentUser currentUser = UserSessionInfo.getCurrentUser();
				LogRecorder.record(LogEntity.getInstance().setModelName("敏感词管理").setFunctionName("修改")
						.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("修改敏感词："+en.getVc_sensitiveword()));
				
				jsonResult.set(ResultState.MODIFY_SUCCESS);
			}else{
				jsonResult.setMessage("修改失败，请联系管理员！");
				jsonResult.setSuccess(false);
			}
		}else{
			jsonResult.setMessage("敏感词已存在！");
			jsonResult.setSuccess(false);
		}
		return jsonResult;
	}
	
	@RequestMapping("remove")
	@ResponseBody
	public JsonResult remove(String ids) {
		JsonResult jsonResult = JsonResult.getInstance();
		if(ids!=null&&ids.length()>0){
			String[] idsDelete = ids.split(",");
			for(int ide=0;ide<idsDelete.length;ide++){
				String userIp = IpUtil.getIp();
				String [] ips = userIp.split(",");
				if (ips != null && ips.length > 0){ //防止双IP
				    userIp = StringUtil.getString(ips[0]);
				}
				CurrentUser currentUser = UserSessionInfo.getCurrentUser();
				String userName = currentUser.getName();
				Sensitive en = sensitiveService.findById(NumberUtil.getInt(idsDelete[ide]));
				LogRecorder.record(LogEntity.getInstance().setModelName("敏感词管理").setFunctionName("删除")
						.setIpAddr(userIp).setLogUser(userName+"("+currentUser.getLoginName()+")").setDescription("删除敏感词："+en.getVc_sensitiveword()));
			}
		}		
		boolean bl = sensitiveService.delete(StringUtil.toIntegerList(ids));
		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult
				.set(ResultState.REMOVE_FAIL);
	}
	
	/**
	 * 显示敏感词导入页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "import_show")
	@ResponseBody
	public ModelAndView showImport() {
		ModelAndView modelAndView = new ModelAndView("dczj/sensitive/sensitive_import");
		modelAndView.addObject("url", "import_submit.do");
		return modelAndView;
	}
	
	/**
	 * 导入提交
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "import_submit")
	@ResponseBody
	public String submitImport(MultipartFile file) {
		Script script = Script.getInstanceWithJsLib();
		String message = "";
		if (file.isEmpty()) {
			message = SpringUtil.getMessage("import.nofile");
		} else {
			try {
				MultipartFileInfo info = MultipartFileInfo.getInstance(file);
				if (ArrayUtils.contains(FileUtil.EXCEL_FILE, info.getFileType())) {
					File filePath = new File(Settings.getSettings().getFileTmp()
							+ StringUtil.getUUIDString() + "." + info.getFileType());
					ControllerUtil.writeMultipartFileToFile(filePath, file);
					message = sensitiveService.importSensitive(filePath);
					
					String userIp = IpUtil.getIp();
					String [] ips = userIp.split(",");
					if (ips != null && ips.length > 0){ //防止双IP
					    userIp = StringUtil.getString(ips[0]);
					}
					CurrentUser currentUser = UserSessionInfo.getCurrentUser();
					LogRecorder.record(LogEntity.getInstance().setModelName("敏感词管理").setFunctionName("导入")
							.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("导入敏感词"));
				} else {
					throw new OperationException("文件类型不正确");
				}
			} catch (OperationException e) {
				message = e.getMessage();
			}
		}
		if (StringUtil.isNotEmpty(message)) {
			script.addAlert(message);
			script.addScript("parent.refreshParentWindow();");
		} else {
			script.addScript("parent.refreshParentWindow();parent.closeDialog();");
		}
		return script.getScript();
	}
	
	/**
	 * 机构xls文件下载
	 * 
	 * @return
	 */
	@RequestMapping(value = "downloadfile")
	@ResponseBody
	public FileResource downloadFile() {
		File file = new File(BaseInfo.getRealPath() + "/WEB-INF/pages/dczj/sensitive/sensitive.xls");
		FileResource fileResource = ControllerUtil.getFileResource(file, "sensitive.xls");
		return fileResource;
	}
}
