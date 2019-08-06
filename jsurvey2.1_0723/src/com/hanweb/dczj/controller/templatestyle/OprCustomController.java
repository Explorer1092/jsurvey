package com.hanweb.dczj.controller.templatestyle;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ZipUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.dczj.entity.CommonTemplateStyle;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.Template;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.CommonTemplateStyleService;
import com.hanweb.dczj.service.DisplayConfigService;
import com.hanweb.dczj.service.PhonePublishService;
import com.hanweb.dczj.service.PublishService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.StyleService;
import com.hanweb.dczj.service.TemplateService;
import com.hanweb.dczj.service.TitleInfoService;

@Controller
@RequestMapping("manager/tempalte")
public class OprCustomController {

	@Autowired
	private TemplateService templateService;
	@Autowired
	private StyleService styleService;
	@Autowired
	private PublishService publishService;
	@Autowired
	private DisplayConfigService displayConfigService;
	@Autowired
	private PhonePublishService phonePublishService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private CommonTemplateStyleService commonTemplateStyleService;
	
	@RequestMapping("showcustom")
	public ModelAndView showcustom(int dczjid,int type) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/customtemplate");
		Template pctemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"",0);
		Template phonetemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 2);
		String pcPageCode = "";
		String phonePageCode = "";
		String pcDefaultPage = "";
		String phoneDefaultPage = "";
		String index = "0";
		pcDefaultPage = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
		pcDefaultPage = FileUtil.readFileToString(pcDefaultPage);
		
		phoneDefaultPage = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
		phoneDefaultPage = FileUtil.readFileToString(phoneDefaultPage);
		
		if(pctemplate != null) {
			pcPageCode = pctemplate.getContent();
		}
		if(StringUtil.isEmpty(pcPageCode)) {
			pcPageCode = pcDefaultPage;
		}
		
		if(phonetemplate != null) {
			phonePageCode = phonetemplate.getContent();
		}
		if(StringUtil.isEmpty(phonePageCode)) {
			phonePageCode = phoneDefaultPage;
		}
		
		TitleInfo titleInfo = new TitleInfo();
		Dczj_Setting settingEn = null;
		Style styleEn = null;
		DisplayConfig displayConfig = null;
		if(StringUtil.isNotEmpty(dczjid+"")){
			settingEn = settingService.getEntityBydczjid(dczjid+"");  //formid查询config实体
			titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			styleEn = styleService.getEntityByDczjid(dczjid+"");
			displayConfig = displayConfigService.findEntityByDczjid(NumberUtil.getInt(dczjid));
		}
		
		if(styleEn == null){
			styleEn = new Style();
		}
		if(settingEn == null) {
			settingEn = new Dczj_Setting();
		}
		if(displayConfig == null) {
			displayConfig = new DisplayConfig();
		}
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		if(titleInfo != null){
			confMap.put("surveytitle",titleInfo.getTitlename());  //调查标题
			confMap.put("createtime",titleInfo.getCreatetime());
			confMap.put("starttime",settingEn.getStarttime());
			confMap.put("endtime",settingEn.getEndtime());
			confMap.put("formId",""+dczjid);
		}
		String strContent = publishService.startingPage(settingEn,styleEn,dczjid+"",confMap,displayConfig,index);
		String phoneStrContent = phonePublishService.startingPage(settingEn,styleEn,dczjid+"",confMap,displayConfig);
		
		modelAndView.addObject("strContent", strContent);
		modelAndView.addObject("phoneStrContent", phoneStrContent);
		modelAndView.addObject("pcPageCode", pcPageCode);
		modelAndView.addObject("phonePageCode", phonePageCode);
		modelAndView.addObject("pcDefaultPage", pcDefaultPage);
		modelAndView.addObject("phoneDefaultPage", phoneDefaultPage);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("type", type);
		modelAndView.addObject("uuid", StringUtil.getUUIDString());
		modelAndView.addObject("url", "customsubmit.do");
		return modelAndView;
	}
	
	@RequestMapping("customsubmit")
	@ResponseBody
	public JsonResult applicationtemplate(int dczjid,int type,String pcPageCode,String phonePageCode ) {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(true);
		
		Template pctemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"",0);
		Template phonetemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"",2);
		if(pctemplate == null) {
			String pcPath = "/template/jsurvey_"+dczjid+"/pctemplate/";
			pctemplate = new Template();
			pctemplate.setDczjid(dczjid);
			pctemplate.setType(type);
			pctemplate.setPagetype(0);
			pctemplate.setName("default.html");
			pctemplate.setPath(pcPath);
			templateService.insert(pctemplate);
		}
		
		if(phonetemplate == null) {
			String phonePath = "/template/jsurvey_"+dczjid+"/phonetemplate/";
			phonetemplate = new Template();
			phonetemplate.setDczjid(dczjid);
			phonetemplate.setType(type);
			phonetemplate.setPagetype(2);
			phonetemplate.setName("phonedefault.html");
			phonetemplate.setPath(phonePath);
			templateService.insert(phonetemplate);
		}
		
		if(StringUtil.isEmpty(pcPageCode)) {
			pcPageCode = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			pcPageCode = FileUtil.readFileToString(pcPageCode);
		}
		if(StringUtil.isEmpty(phonePageCode)) {
			phonePageCode = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			phonePageCode = FileUtil.readFileToString(phonePageCode);
		}
		pctemplate.setContent(pcPageCode);
		phonetemplate.setContent(phonePageCode);
		
	    boolean pcbl = false;
	    boolean phonebl = false;
	    pcbl = templateService.update(pctemplate);
	    phonebl = templateService.update(phonetemplate);
	    if(pcbl && phonebl) {
	    	titleInfoService.setUpdateHtml(StringUtil.getString(dczjid),0);
	    	String pcpath = null;
	    	String phonepath = null;
	    	if(pctemplate.getPath().length()>0&&pctemplate.getName().length()>0) {
	    		pcpath = BaseInfo.getRealPath() + pctemplate.getPath() + pctemplate.getName();
	    	}else {
	    		pcpath = BaseInfo.getRealPath() + "/template/jsurvey_"+dczjid+"/pctemplate/" + "default.html";
			}
	    	if (phonetemplate.getPath()!= null&&phonetemplate.getName()!=null) {
	    		phonepath = BaseInfo.getRealPath() + phonetemplate.getPath() + phonetemplate.getName();
			}else {
				phonepath = BaseInfo.getRealPath() + "/template/jsurvey_"+dczjid+"/phonetemplate/" + "phonedefault.html";
			}
	    	FileUtil.writeStringToFile(pcpath, pctemplate.getContent());
	    	FileUtil.writeStringToFile(phonepath, phonetemplate.getContent());
	    	return jsonResult;
	    }else {
	    	jsonResult.setSuccess(false);
	    	jsonResult.setMessage("操作失败，请联系管理员");
	    	return jsonResult;
	    }
	}
	
	@RequestMapping("showtemplateimport")
	public ModelAndView showtemplateinport(int dczjid,int type) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/templateimport");
		
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("type", type);
		modelAndView.addObject("url", "templateimportsubmit.do");
		return modelAndView;
	}
	
	@RequestMapping("templateimportsubmit")
	@ResponseBody
	public String templateimportsubmit(int dczjid,int type,MultipartFile imgfile,MultipartFile pcfile,MultipartFile phonefile) {
		Script script = Script.getInstanceWithJsLib();
		String path = "";
		String newFileName = "";
		String suffix = "";
		String message = "";
		if(imgfile != null && !imgfile.isEmpty()) {
			MultipartFileInfo imginfo = MultipartFileInfo.getInstance(imgfile);
			newFileName = imginfo.getFileFullName();
			suffix = newFileName.substring(newFileName.indexOf(".") + 1).toLowerCase();
			if (!"jpg".equals(suffix) && !"png".equals(suffix) && !"jpeg".equals(suffix)) {
				message = "只能上传.jpg/.png/.jpeg后缀的图片!";
				script.addAlert(message);
				script.addScript("location.href=\"tempalte_show.do?dczjid="+dczjid+"\"");
				return script.getScript();
			}
			path = BaseInfo.getRealPath()+"/template/jsurvey_"+dczjid+"/image/";
			FileUtil.createDir(path);
			//删除文件夹path下的所有文档
			deleAll(path);
			newFileName = dczjid+".png";
			File desFile = new File(path + newFileName);
			if(!desFile.exists()){
				ControllerUtil.writeMultipartFileToFile(desFile, imgfile);
			}
		}
        if(StringUtils.isNotBlank(pcfile.getOriginalFilename())) {
        	MultipartFileInfo pcinfo = MultipartFileInfo.getInstance(pcfile);
        	newFileName = pcinfo.getFileFullName();
			suffix = newFileName.substring(newFileName.indexOf(".") + 1).toLowerCase();
			if (!"html".equals(suffix) && !"htm".equals(suffix) && !"zip".equals(suffix)) {
				message = "只能上传.html/.htm/.zip后缀的模板!";
				script.addAlert(message);
				script.addScript("location.href=\"tempalte_show.do?dczjid="+dczjid+"\"");
				return script.getScript();
			}
			path = BaseInfo.getRealPath()+"/template/jsurvey_"+dczjid+"/pctemplate/";
			FileUtil.createDir(path);
			//删除文件夹path下的所有文档
			deleAll(path);
			File desFile = new File(path + newFileName);
			if(!desFile.exists()){
				ControllerUtil.writeMultipartFileToFile(desFile, pcfile);
				Template pcTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"",0);
				if(pcTemplate != null) {
					if ("html".equals(newFileName.substring(newFileName.indexOf(".") + 1))
							|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						pcTemplate.setPath("/template/jsurvey_"+dczjid+"/pctemplate/");
						pcTemplate.setName(newFileName);
					}
					if("zip".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						File dire = new File(path);
						if (!dire.exists()) {
							dire.mkdirs();
						}
						ZipUtil.unzip(desFile, new File(path));
						newFileName = newFileName.substring(0,newFileName.indexOf("."));
						File []newfilename = dire.listFiles();
						if(newfilename != null && newfilename.length >0) {
							for(File files:newfilename){
								newFileName = files.getName();
								if("html".equals(newFileName.substring(newFileName.indexOf(".") + 1)) 
										|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))){
									break;
								}
							}
						}
						pcTemplate.setPath("/template/jsurvey_"+dczjid+"/pctemplate/");
						pcTemplate.setName(newFileName);
					}
					
					File newFile = new File(path + newFileName);
					pcTemplate.setContent(FileUtil.readFileToString(newFile));
					templateService.update(pcTemplate);
				}else {
					pcTemplate = new Template();
					pcTemplate.setDczjid(dczjid);
					pcTemplate.setPagetype(0);
					pcTemplate.setType(type);
					if ("html".equals(newFileName.substring(newFileName.indexOf(".") + 1))
							|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						pcTemplate.setPath("/template/jsurvey_"+dczjid+"/pctemplate/");
						pcTemplate.setName(newFileName);
					}
					if("zip".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						File dire = new File(path);
						if (!dire.exists()) {
							dire.mkdirs();
						}
						ZipUtil.unzip(desFile, new File(path));
						newFileName = newFileName.substring(0,newFileName.indexOf("."));
						File []newfilename = dire.listFiles();
						if(newfilename != null && newfilename.length>0) {
							for(File files:newfilename){
								newFileName = files.getName();
								if("html".equals(newFileName.substring(newFileName.indexOf(".") + 1)) 
										|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))){
									break;
								}
							}
						}
						pcTemplate.setPath("/template/jsurvey_"+dczjid+"/pctemplate/");
						pcTemplate.setName(newFileName);
					}
					
					File newFile = new File(path + newFileName);
					pcTemplate.setContent(FileUtil.readFileToString(newFile));
					templateService.insert(pcTemplate);
				}
			}
		}
        if(StringUtils.isNotBlank(phonefile.getOriginalFilename())) {
        	MultipartFileInfo phoneinfo = MultipartFileInfo.getInstance(phonefile);
        	newFileName = phoneinfo.getFileName();
        	suffix = newFileName.substring(newFileName.indexOf(".") + 1).toLowerCase();
			if (!"html".equals(suffix) && !"htm".equals(suffix) && !"zip".equals(suffix)) {
				message = "只能上传.html/.htm/.zip后缀的模板!";
				script.addAlert(message);
				script.addScript("location.href=\"tempalte_show.do?dczjid="+dczjid+"\"");
				return script.getScript();
			}
			path = BaseInfo.getRealPath()+"/template/jsurvey_"+dczjid+"/phonetemplate/";
			FileUtil.createDir(path);
			//删除文件夹path下的所有文档
			deleAll(path);
			File desFile = new File(path + newFileName);
			if(!desFile.exists()){
				ControllerUtil.writeMultipartFileToFile(desFile, phonefile);
				Template phoneTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"",2);
				if(phoneTemplate != null) {
					if ("html".equals(newFileName.substring(newFileName.indexOf(".") + 1))
							|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						phoneTemplate.setPath(BaseInfo.getContextPath()+"/template/jsurvey_"+dczjid+"/phonetemplate/");
						phoneTemplate.setName(newFileName);
					}
					if("zip".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						File dire = new File(path);
						if (!dire.exists()) {
							dire.mkdirs();
						}
						ZipUtil.unzip(desFile, new File(path));
						newFileName = newFileName.substring(0,newFileName.indexOf("."));
						File []newfilename = dire.listFiles();
						if(newfilename != null && newfilename.length >0 ) {
							for(File files:newfilename){
								newFileName = files.getName();
								if("html".equals(newFileName.substring(newFileName.indexOf(".") + 1)) 
										|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))){
									break;
								}
							}
						}
						phoneTemplate.setPath("/template/jsurvey_"+dczjid+"/phonetemplate/");
						phoneTemplate.setName(newFileName);
					}
					
					File newFile = new File(path + newFileName);
					phoneTemplate.setContent(FileUtil.readFileToString(newFile));
					templateService.update(phoneTemplate);
				}else {
					phoneTemplate = new Template();
					phoneTemplate.setDczjid(dczjid);
					phoneTemplate.setPagetype(2);
					phoneTemplate.setType(type);
					if ("html".equals(newFileName.substring(newFileName.indexOf(".") + 1))
							|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						phoneTemplate.setPath("/template/jsurvey_"+dczjid+"/phonetemplate/");
						phoneTemplate.setName(newFileName);
					}
					if("zip".equals(newFileName.substring(newFileName.indexOf(".") + 1))) {
						File dire = new File(path);
						if (!dire.exists()) {
							dire.mkdirs();
						}
						ZipUtil.unzip(desFile, new File(path));
						newFileName = newFileName.substring(0,newFileName.indexOf("."));
						File []newfilename = dire.listFiles();
						if(newfilename != null && newfilename.length > 0) {
							for(File files:newfilename){
								newFileName = files.getName();
								if("html".equals(newFileName.substring(newFileName.indexOf(".") + 1)) 
										|| "htm".equals(newFileName.substring(newFileName.indexOf(".") + 1))){
									break;
								}
							}
						}
						phoneTemplate.setPath("/template/jsurvey_"+dczjid+"/phonetemplate/");
						phoneTemplate.setName(newFileName);
					}
					
					File newFile = new File(path + newFileName);
					phoneTemplate.setContent(FileUtil.readFileToString(newFile));
					templateService.insert(phoneTemplate);
				}
			}
        }
        script.addScript("location.href=\"showcustom.do?dczjid="+dczjid+"&type="+type+"\"");
		return script.getScript();
	}
	
	/**
	 * 删除所有文件
	 * @param path
	 * @return
	 */
	public static boolean deleAll(String path){
		boolean isSuccess = false;
		File file = new File(path);
		 if (!file.exists()) {
	         return isSuccess;
	       }
		 if (!file.isDirectory()) {
	         return isSuccess;
	       }
		 String[] tempList = file.list();
		 File temp = null;
		 if(tempList != null && tempList.length >0 ) {
			 for (int i = 0; i < tempList.length; i++) {
				 if (path.endsWith("/")) {
					 temp = new File(path + tempList[i]);
				 }else {
		              temp = new File(path + "/" + tempList[i]);
		         }
				 if (temp.isFile()) {
					 temp.delete();
				 }
				 if (temp.isDirectory()) {
					 deleAll(path + "/" + tempList[i]); //先删除文件夹里面的文件
					 delFolder(path + "/" + tempList[i]);//再删除空文件夹
					 isSuccess = true;
				 }
			}
		}
		return isSuccess;
	}
	
	public static void delFolder(String folderPath) {
	     try {
	    	deleAll(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //删除空文件夹
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}
	
	@RequestMapping("iframecustomtemplate")
	public ModelAndView showIframeTemplate(String dczjid,int type,String templateid) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/templateiframe");
		String strContent = "";
		String temp = "";
		String style = null;
		String pctemp = "";
		String phonetemp = "";
		String index = "0";
		TitleInfo titleInfo = new TitleInfo();
		Dczj_Setting settingEn = null;
		Style styleEn = null;
		DisplayConfig displayConfig = null;
		if(StringUtil.isNotEmpty(dczjid)){
			settingEn = settingService.getEntityBydczjid(dczjid);  //formid查询config实体
			titleInfo = titleInfoService.getEntity(NumberUtil.getInt(dczjid)); //通过formid查询调查实体
			styleEn = styleService.getEntityByDczjid(dczjid);
			displayConfig = displayConfigService.findEntityByDczjid(NumberUtil.getInt(dczjid));
		}
		
		if(styleEn == null){
			styleEn = new Style();
		}
		if(settingEn == null) {
			settingEn = new Dczj_Setting();
		}
		if(displayConfig == null) {
			displayConfig = new DisplayConfig();
		}
		HashMap<String, Object> confMap = new HashMap<String, Object>();
		if(titleInfo != null){
			confMap.put("surveytitle",titleInfo.getTitlename());  //调查标题
			confMap.put("createtime",titleInfo.getCreatetime());
			confMap.put("starttime",settingEn.getStarttime());
			confMap.put("endtime",settingEn.getEndtime());
			confMap.put("formId",""+dczjid);
		}
		if(StringUtil.isNotEmpty(templateid) && !StringUtil.equals(templateid, "dczj1")) {
			CommonTemplateStyle commonTemplateStyle = commonTemplateStyleService.findListByIid(NumberUtil.getInt(templateid));
			if(commonTemplateStyle != null) {
				pctemp = commonTemplateStyle.getListstyle();
				phonetemp = commonTemplateStyle.getPhoneliststyle();
			}
		}
		if(type == 0) {
			if(styleEn != null ){
				style = styleEn.getPcstyle();
			}
			if(style == null || style.length() == 0){ //如果没设置样式时，，设置默认样式
				String dauatlpage = BaseInfo.getRealPath()+ "/resources/dczj/template/htmlform.xml";
				style = styleService.getStyleCodeByPath(dauatlpage);
			}
			style = style.substring(1,style.length()-1);
			String cssstyle = displayConfig.getCssstyle();
			String thanksmessage = settingService.initPCThanksDiv(dczjid);
			style = cssstyle + style + thanksmessage;
			
			if(StringUtil.isNotEmpty(pctemp)) {
				temp = pctemp;
			}else {
				Template pcTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 0);
				if(pcTemplate != null ) {
					temp = pcTemplate.getContent();
				}else {
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
					temp = FileUtil.readFileToString(temp);
				}
				if(StringUtil.isNotEmpty(templateid) && StringUtil.equals(templateid, "dczj1")) {
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
					temp = FileUtil.readFileToString(temp);
				}
			}
			
			strContent = publishService.parseDczjFront(dczjid,confMap,settingEn,style,displayConfig,index);
			strContent = publishService.parseTemplate(strContent, temp);
		}else {
			if (styleEn != null) {
				style = styleEn.getPhonestyle();
			}
			if (style == null || style.length() == 0) {
				// 手机端样式设置为默认样式
				String dauatlpage = BaseInfo.getRealPath() + "/resources/dczj/template/phoneform.xml";
				style = styleService.getStyleCodeByPath(dauatlpage);
			}
			style = style.substring(1, style.length() - 1);
			String cssstyle = displayConfig.getCssstyle();
			style =cssstyle+style;
			
			if(StringUtil.isNotEmpty(phonetemp)) {
				temp = phonetemp;
			}else {
				Template phoneTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 2);
				if(phoneTemplate != null) {
					temp = phoneTemplate.getContent();
				}else {
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
					temp = FileUtil.readFileToString(temp);
				}
				if(StringUtil.isNotEmpty(templateid) && StringUtil.equals(templateid, "dczj1")) {
					temp = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
					temp = FileUtil.readFileToString(temp);
				}
			}
			
			strContent = phonePublishService.parsePhoneFront(dczjid,confMap,settingEn,style,displayConfig);
			strContent = phonePublishService.parseTemplate(strContent, temp);
		}
		modelAndView.addObject("strContent", strContent);
		return modelAndView;
	}
}
