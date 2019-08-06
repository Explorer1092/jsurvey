package com.hanweb.dczj.controller.templatestyle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ZipUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.entity.CommonTemplateStyle;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.Style;
import com.hanweb.dczj.entity.Template;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.CommonTemplateStyleService;
import com.hanweb.dczj.service.DisplayConfigService;
import com.hanweb.dczj.service.PublishService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.StyleService;
import com.hanweb.dczj.service.TemplateService;
import com.hanweb.dczj.service.TitleInfoService;

@Controller
@RequestMapping("manager/tempalte")
public class OprTemplateController {

	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private CommonTemplateStyleService commonTemplateStyleService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private StyleService styleService;
	@Autowired
	private DisplayConfigService displayConfigService;
	@Autowired
	private PublishService publishService;
	
	@RequestMapping("tempalte_show")
	public ModelAndView showTemplateList(int dczjid) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/template_index");
		TitleInfo titleInfo = titleInfoService.getEntity(dczjid);
		int webid = titleInfo.getWebid();
		int type = titleInfo.getType();
		
		List<Template> templateList = templateService.findTemplatByDczjidAndType(dczjid+"", type);
		if(templateList != null && templateList.size() >0) {
			String path =  BaseInfo.getRealPath() + "/template/jsurvey_"+dczjid+"/image/"+dczjid+".png";
			File desFile = new File(path);
			if(desFile.exists()) {
				modelAndView.addObject("imagepath", BaseInfo.getContextPath()+"/template/jsurvey_"+dczjid+"/image/"+dczjid+".png");
			}else {
				modelAndView.addObject("imagepath", BaseInfo.getContextPath()+"/resources/dczj/images/template/1.png");
			}
			modelAndView.addObject("templateList", templateList);
		}else {
			modelAndView.addObject("templateList", null);
		}
		List<CommonTemplateStyle> commonTemplateStyleList = commonTemplateStyleService.findListByWebid(webid,type);
		if(commonTemplateStyleList != null && commonTemplateStyleList.size() >0) {
			modelAndView.addObject("commonTemplateStyleList", commonTemplateStyleList);
		}else {
			modelAndView.addObject("commonTemplateStyleList", null);
		}
		modelAndView.addObject("type", type);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("webid", webid);
		return modelAndView;
	}
	
	@RequestMapping("applicationtemplate")
	@ResponseBody
	public JsonResult applicationtemplate(String commoncomplateid,int dczjid,int type) {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(true);
		commoncomplateid = StringUtil.getSafeString(commoncomplateid);
		if(StringUtil.isEmpty(commoncomplateid)) {
			return jsonResult.setSuccess(false).setMessage("操作失败，请联系管理员");
		}
		String pcContent = "";
		String phoneContent = "";
		String path = "";
		String name = "";
		List<Template> templateList = templateService.findTemplatByDczjidAndType(dczjid+"", type);
		if(StringUtil.equals(commoncomplateid, "dczj1")) {
			pcContent = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			pcContent = FileUtil.readFileToString(pcContent);
			phoneContent = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			phoneContent = FileUtil.readFileToString(phoneContent);
		}else if(StringUtil.equals(commoncomplateid, "dczj2")) {
			if(templateList != null && templateList.size()>0) {
				for(int j=0;j<templateList.size();j++) {
					Template template = templateList.get(j);
					if(j == 0) {
						pcContent = template.getContent();
					}else if(j == 1) {
						phoneContent = template.getContent();
					}
				}
			}
		}else {
			int iid = NumberUtil.getInt(commoncomplateid);
			CommonTemplateStyle commonTemplateStyle = commonTemplateStyleService.findListByIid(iid);
			if(commonTemplateStyle != null) {
				pcContent = commonTemplateStyle.getListstyle();
				phoneContent = commonTemplateStyle.getPhoneliststyle();
			}
		}
		if(templateList != null && templateList.size()>0) {
			for(int j=0;j<templateList.size();j++) {
				Template template = templateList.get(j);
				if(j == 0) {
					template.setPagetype(0);
					template.setContent(pcContent);
				}else if(j == 1) {
					template.setPagetype(2);
					template.setContent(phoneContent);
				}
				template.setPath(path);
				template.setName(name);
				templateService.update(template);
				
			}
		}else {
			for(int i=0;i<2;i++) {
				Template template = new Template();
				template.setDczjid(dczjid);
				template.setType(type);
				if(i == 0) {
					template.setPagetype(0);
					template.setContent(pcContent);
				}else if(i == 1) {
					template.setPagetype(2);
					template.setContent(phoneContent);
				}
				template.setPath(path);
				template.setName(name);
				templateService.insert(template);
			}
		}
		titleInfoService.setUpdateHtml(StringUtil.getString(dczjid),0);
		return jsonResult;
	}
	
	@RequestMapping(value = "downloadtemplate")
	@ResponseBody
	public FileResource downloadtemplate(String commoncomplateid,int dczjid) {
		commoncomplateid = StringUtil.getSafeString(commoncomplateid);
		FileResource fileResouce = null;
		String pcContent = "";
		String phoneContent = "";
		ArrayList<File> fileList = new ArrayList<File>();
		File pcfile = null;
		File phonefile = null;
		if(StringUtil.equals(commoncomplateid, "dczj1")) {
			pcContent = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			pcfile = new File(pcContent);
			phoneContent = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			phonefile = new File(phoneContent);
			fileList.add(pcfile);
			fileList.add(phonefile);
		}else if(StringUtil.equals(commoncomplateid, "dczj2")) {
			Template pcTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 0);
			Template phoneTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 2);
			if(pcTemplate != null) {
				pcContent = BaseInfo.getRealPath()+pcTemplate.getPath();
			}else {
				pcContent = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			}
			if(phoneTemplate != null) {
				phoneContent = BaseInfo.getRealPath()+phoneTemplate.getPath();
			}else {
				phoneContent = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
			}
			pcfile = new File(pcContent);
			phonefile = new File(phoneContent);
			fileList.add(pcfile);
			fileList.add(phonefile);
		}else {
			int iid = NumberUtil.getInt(commoncomplateid);
			CommonTemplateStyle commonTemplateStyle = commonTemplateStyleService.findListByIid(iid);
			if(commonTemplateStyle != null) {
				pcContent = commonTemplateStyle.getListstyle();
				phoneContent = commonTemplateStyle.getPhoneliststyle();
				String pcpath = Settings.getSettings().getFileTmp()+"/template/jsurvey_"+dczjid+"/pctemplate/pcdefault.html";
				String phonepath = Settings.getSettings().getFileTmp()+"/template/jsurvey_"+dczjid+"/phonetemplate/phonedefault.html";
				FileUtil.writeStringToFile(pcpath, pcContent);
				FileUtil.writeStringToFile(phonepath, phoneContent);
				pcfile = new File(pcpath);
				phonefile = new File(phonepath);
				fileList.add(pcfile);
				fileList.add(phonefile);
			}
		}
		
		String filepath = Settings.getSettings().getFileTmp()+"/template/jsurvey_"+dczjid+"/";
		try {
			String plugfilePath = filepath+"templatedownload.zip";
			FileUtil.createDir(filepath);
			File plugfile = new File(plugfilePath);
			ZipUtil.zip(fileList, plugfile);
			fileResouce = ControllerUtil.getFileResource(plugfile, "模板下载.zip");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			deleAll(filepath);
		}
		return fileResouce;
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
		if(tempList != null && tempList.length >0) {
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
	
	
	@RequestMapping("iframetemplate")
	public ModelAndView showIframeTemplate(String commoncomplateid,String dczjid,int type) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/templateiframe");
		String pcContent = "";
		String temp = "";
		String style = null;
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
		
		if(StringUtil.equals(commoncomplateid, "dczj1")) {
			temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
			temp = FileUtil.readFileToString(temp);
		}else if(StringUtil.equals(commoncomplateid, "dczj2")) {
			Template pcTemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 0);
			if(pcTemplate != null) {
				temp = pcTemplate.getContent();
			}else {
				temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
				temp = FileUtil.readFileToString(temp);
			}
		}else {
			int iid = NumberUtil.getInt(commoncomplateid);
			CommonTemplateStyle commonTemplateStyle = commonTemplateStyleService.findListByIid(iid);
			if(commonTemplateStyle != null) {
				temp = commonTemplateStyle.getListstyle();
			}else {
				temp = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
				temp = FileUtil.readFileToString(temp);
			}
		}
		pcContent = publishService.parseDczjFront(dczjid,confMap,settingEn,style,displayConfig,index);
		pcContent = publishService.parseTemplate(pcContent, temp);
		modelAndView.addObject("strContent", pcContent);
		return modelAndView;
	}
}
