package com.hanweb.dczj.controller.templatestyle;

import java.io.File;

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
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.entity.CommonTemplateStyle;
import com.hanweb.dczj.entity.Template;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.CommonTemplateStyleService;
import com.hanweb.dczj.service.TemplateService;
import com.hanweb.dczj.service.TitleInfoService;

@Controller
@RequestMapping("manager/tempalte")
public class OprCommonTemplateController {
	
	@Autowired
	private TemplateService templateService;
	@Autowired
	private CommonTemplateStyleService commonTemplateStyleService;
	@Autowired
	private TitleInfoService titleInfoService;
	
	
	@RequestMapping("showCommonTemplate")
	public ModelAndView showCommonTemplate(int dczjid,int type,String templateid) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/customtemplate");
		templateid = StringUtil.getSafeString(templateid);
		String pcContent = "";
		String phoneContent = "";
		String pcDefaultPage = "";
		String phoneDefaultPage = "";
		
		pcDefaultPage = BaseInfo.getRealPath() + "/resources/dczj/template/default.html";
		pcDefaultPage = FileUtil.readFileToString(pcDefaultPage);
		phoneDefaultPage = BaseInfo.getRealPath() + "/resources/dczj/template/phonedefault.html";
		phoneDefaultPage = FileUtil.readFileToString(phoneDefaultPage);
		if(StringUtil.isNotEmpty(templateid)) {
			if(StringUtil.equals(templateid, "dczj1")) {
				pcContent = pcDefaultPage;
				phoneContent =phoneDefaultPage;
				modelAndView.addObject("templateid", "dczj1");
			}else if(StringUtil.equals(templateid, "dczj2")) {
				Template pctemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"", 0);
				Template phonetemplate = templateService.findTemplatByDczjidAndTypeAndPagetype(dczjid+"",2);
				if(pctemplate != null) {
					pcContent = pctemplate.getContent();
				}else {
					pcContent = pcDefaultPage;
				}
				if(phonetemplate != null) {
					phoneContent = phonetemplate.getContent(); 
				}else {
					phoneContent = phoneDefaultPage;
				}
				
			}else {
				int iid = NumberUtil.getInt(templateid);
				CommonTemplateStyle commonTemplateStyle = commonTemplateStyleService.findListByIid(iid);
				pcContent = commonTemplateStyle.getListstyle();
				phoneContent = commonTemplateStyle.getPhoneliststyle();
				modelAndView.addObject("templateid", iid);
			}
		}
		
		modelAndView.addObject("pcPageCode", pcContent);
		modelAndView.addObject("phonePageCode", phoneContent);
		modelAndView.addObject("pcDefaultPage", pcDefaultPage);
		modelAndView.addObject("phoneDefaultPage", phoneDefaultPage);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("type", type);
		modelAndView.addObject("uuid", StringUtil.getUUIDString());
		modelAndView.addObject("url", "customsubmit.do");
		return modelAndView;
	}
	
	@RequestMapping("commontemplatesubmit")
	@ResponseBody
	public String commonTemplateSubmit(int dczjid,int type,String pcPageCode,String phonePageCode,MultipartFile file ) {
		Script script = Script.getInstanceWithJsLib();
		if (file.isEmpty()) {
			script.addAlert(SpringUtil.getMessage("import.nofile"));
			return script.getScript();
		}
		
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
	    	String pcpath = BaseInfo.getRealPath() + pctemplate.getPath() + pctemplate.getName();
	    	String phonepath = BaseInfo.getRealPath() + phonetemplate.getPath() + phonetemplate.getName();
	    	FileUtil.writeStringToFile(pcpath, pctemplate.getContent());
	    	FileUtil.writeStringToFile(phonepath, phonetemplate.getContent());
	    	CommonTemplateStyle commonTemplateStyle = new CommonTemplateStyle();
	    	TitleInfo titleInfo = titleInfoService.getEntity(dczjid);
            if(titleInfo != null ) {
            	commonTemplateStyle.setWebid(titleInfo.getWebid());
	    	}else {
		    	script.addAlert("操作失败，请联系管理员");
				return script.getScript();
	    	}
            MultipartFileInfo info = MultipartFileInfo.getInstance(file);
	    	commonTemplateStyle.setDczjid(dczjid);
	    	commonTemplateStyle.setListstyle(pctemplate.getContent());
	    	commonTemplateStyle.setPhoneliststyle(phonetemplate.getContent());
	    	commonTemplateStyle.setState(0);
	    	commonTemplateStyle.setType(type);
	    	commonTemplateStyle.setStyleimgname(info.getFileFullName());
	    	int iid = commonTemplateStyleService.insert(commonTemplateStyle);
	    	if(iid > 0) {
    		    titleInfoService.setUpdateHtml(StringUtil.getString(dczjid),0);
    		    try {
    				String imgpath = BaseInfo.getRealPath()+"/template/commontemplate_"+iid+"/image/";
    				FileUtil.createDir(imgpath);
    				deleAll(imgpath);
    				File desFile = new File(imgpath + info.getFileFullName());
    				if(!desFile.exists()){
    					// 开始拷贝
    					boolean copyContent = ControllerUtil.writeMultipartFileToFile(desFile, file);
    					if(!copyContent){
    						script.addAlert("拷贝文件出错!");
    						return script.getScript();
    					}
    				}
    			} catch (Exception e) {
    			}
//    		    String path =  BaseInfo.getRealPath() + "/template/jsurvey_"+dczjid+"/image/"+dczjid+".png";
//    			String commonpath = BaseInfo.getRealPath()+"/template/commontemplate_"+iid+"/image/";
//    			FileUtil.createDir(commonpath);
//    			deleAll(commonpath);
//    			File desFile = new File(path);
//    			File commontempfile = new File(commonpath+iid+".png");
//    			if(desFile.exists()) {
//    				FileUtil.copyFile(desFile, commontempfile);
//    			}else {
//    				String newpath = BaseInfo.getRealPath()+"/resources/dczj/images/template/1.png";
//    				File newFile = new File(newpath);
//    				FileUtil.copyFile(newFile, commontempfile);
//    			}
    		    script.addScript("parent.closeDialog(true);");	
    		    return script.getScript();
	    	}else {
	    		script.addAlert("操作失败，请联系管理员");
				return script.getScript();
	    	}
	    }else {
	    	script.addAlert("操作失败，请联系管理员");
			return script.getScript();
	    }
	}

	@ResponseBody
	@RequestMapping("deleteTemplate")
	public JsonResult deleteTemplate(String templateid) {
		JsonResult jsonResult = JsonResult.getInstance();
		StringUtil.getSafeString(templateid);
        Boolean deleteresult = false;
		if(StringUtil.isNotEmpty(templateid)) {
			int iid = NumberUtil.getInt(templateid);
			CommonTemplateStyle commonTemplateStyle = commonTemplateStyleService.findListByIid(iid);
			commonTemplateStyle.setState(1);
			deleteresult = commonTemplateStyleService.update(commonTemplateStyle);
		}
		return deleteresult ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult.set(ResultState.REMOVE_FAIL);
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
	
	@RequestMapping("showcommontemplateimport")
	public ModelAndView showcommontemplateimport(String dczjid,String type,String pcPageCode,String phonePageCode,MultipartFile file ) {
		ModelAndView modelAndView = new ModelAndView("dczj/template/commontemplateimport");
		
		modelAndView.addObject("pcPageCode", pcPageCode);
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("type", type);
		modelAndView.addObject("phonePageCode", phonePageCode);
		modelAndView.addObject("url", "commontemplatesubmit.do");
		return modelAndView;
	}
}
