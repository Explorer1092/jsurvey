package com.hanweb.dczj.controller.ques;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.hanweb.common.util.file.IFileUtil;
import com.hanweb.common.util.file.LocalFileUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.MultipartFileInfo;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.util.mvc.Script;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.StyleService;

@Controller
@RequestMapping("manager/dczj")
public class OprAnswController {

	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private StyleService styleService;
	
	@Autowired
	@Qualifier("FileUtil")
	private IFileUtil fileUtil;
	@Autowired
	@Qualifier("LocalFileUtil")
	private LocalFileUtil localFileUtil;
	
	/**
	 * 答案新增
	 * @param dczjid
	 * @param quesid
	 * @return
	 */
	@RequestMapping("addansw")
	@ResponseBody
	public JsonResult addAnsw(int dczjid,int quesid) {
		JsonResult jsonResult = JsonResult.getInstance();
		AnswInfo en = new AnswInfo();
		en.setIid(-1);
		en.setOrderid(0);
		en.setDczjid(dczjid);
		en.setQuesid(quesid);
		en.setBasepoint(0);
		en.setState(0);
		en.setAnswnote("");
		en.setAllowfillinair(0);
		int maxnum = answInfoService.findCount(quesid);
		maxnum = maxnum +1;
		en.setAnswname("选项"+maxnum);
		int newAnswId = answInfoService.add(en);
		boolean b1 = newAnswId>0? true:false;
		if(b1){
			jsonResult.addParam("newAnswId", newAnswId);
		}
		return b1 ? jsonResult.set(ResultState.ADD_SUCCESS) : jsonResult.set(ResultState.ADD_FAIL);
	}
	
	/**
	 * 组织答案新增页面
	 * @param quesid
	 * @param answid
	 * @param type
	 * @return
	 */
	@RequestMapping("addanswhtml")
	@ResponseBody
	public JsonResult addAnswHtml(int quesid,int answid,int type) {
		JsonResult jsonResult = JsonResult.getInstance();
		String quespage = BaseInfo.getRealPath()+ "/resources/dczj/ques/surveyansw.xml";
		String content = styleService.getStyleCodeByPath(quespage);
		AnswInfo en = answInfoService.getEntity(answid);
		if(type == 0){
			content = styleService.parseSingleChoice(content);
			String strContent = "";
			String answnote = "";
			if(StringUtil.isNotEmpty(en.getAnswnote())) {
				answnote = en.getAnswnote();
			}
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->")+20;
			int afterIndex = content.indexOf("<!--/for-->");
			strContent= content.substring(beforeIndex,afterIndex);
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--trAnswId-->", en.getIid()+"").replaceAll("<!--tdAnswName-->", en.getAnswname()).replaceAll("<!--tdAnswNote-->", answnote)
			                       .replaceAll("<!--basepoint-->", en.getBasepoint()+"").replaceAll("<!--answ_imgname-->", "").replaceAll("<!--allowFillInAir-->", "0").replaceAll("<!--answ_display-->", "none");
			jsonResult.setMessage(strContent);
			jsonResult.setSuccess(true);
		}else if(type == 1){
			content = styleService.parseMultipleChoice(content);
			String strContent = "";
			String answnote = "";
			if(StringUtil.isNotEmpty(en.getAnswnote())) {
				answnote = en.getAnswnote();
			}
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->")+20;
			int afterIndex = content.indexOf("<!--/for-->");
			strContent= content.substring(beforeIndex,afterIndex);
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--trAnswId-->", en.getIid()+"").replaceAll("<!--tdAnswName-->", en.getAnswname()).replaceAll("<!--tdAnswNote-->", answnote)
			                       .replaceAll("<!--basepoint-->", en.getBasepoint()+"").replaceAll("<!--answ_imgname-->", "").replaceAll("<!--allowFillInAir-->", "0").replaceAll("<!--answ_display-->", "none");
			jsonResult.setMessage(strContent);
			jsonResult.setSuccess(true);
		}else if(type == 6){
			content = styleService.parseEvaluationSingleChoice(content);
			String strContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->")+20;
			int afterIndex = content.indexOf("<!--/for-->");
			strContent= content.substring(beforeIndex,afterIndex);
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--trAnswId-->", en.getIid()+"").replaceAll("<!--tdAnswName-->", en.getAnswname()).replaceAll("<!--tdIsRight-->", en.getIsright())
			                       .replaceAll("<!--basepoint-->", en.getBasepoint()+"").replaceAll("<!--answ_imgname-->", "").replaceAll("<!--allowFillInAir-->", "0").replaceAll("<!--answ_display-->", "none");
			jsonResult.setMessage(strContent);
			jsonResult.setSuccess(true);
		}else if(type == 7){
			content = styleService.parseEvaluationMultipleChoice(content);
			String strContent = "";
			int beforeIndex = content.indexOf("<!--for name=\"答案\"-->")+20;
			int afterIndex = content.indexOf("<!--/for-->");
			strContent= content.substring(beforeIndex,afterIndex);
			strContent = strContent.replaceAll("<!--divquesId-->", quesid+"").replaceAll("<!--trAnswId-->", en.getIid()+"").replaceAll("<!--tdAnswName-->", en.getAnswname()).replaceAll("<!--tdIsRight-->", en.getIsright())
			                       .replaceAll("<!--basepoint-->", en.getBasepoint()+"").replaceAll("<!--answ_imgname-->", "").replaceAll("<!--allowFillInAir-->", "0").replaceAll("<!--answ_display-->", "none");
			jsonResult.setMessage(strContent);
			jsonResult.setSuccess(true);
		}
		return jsonResult;
	}
	
	/**
	 * 删除答案
	 * @param answid
	 * @return
	 */
	@RequestMapping("removeansw")
	@ResponseBody
	public JsonResult removeAnsw(String answid) {
		JsonResult jsonResult = JsonResult.getInstance();
		answid = StringUtil.getSafeString(answid);
		boolean bl = answInfoService.delete(answid);
		
		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult.set(ResultState.REMOVE_FAIL);
	}
	
	/**
	 * 排序
	 * @param quesId
	 * @param answId
	 * @param type
	 * @return
	 */
	@RequestMapping("sortansw")
	@ResponseBody
	public JsonResult sortansw(int quesid,int answid,int type) {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(false);
		List<Integer> answIdList = answInfoService.findAnswidByQuesid(quesid);
		if(answIdList != null && answIdList.size()>0){
			for(int i = 0; i<= answIdList.size() - 1; i++){
				if(answIdList.get(i) == answid){
					if(type == 1){
						if(i > 0){
							int sortAnswId = answIdList.get(i-1);
							answIdList.set(i, sortAnswId);
							answIdList.set(i-1, answid);
							jsonResult.setSuccess(true);
							jsonResult.addParam("sortAnswId", sortAnswId);
							break;
						}else{
							return jsonResult.setMessage("当前选项已排在第一位！");
						}
					}else if(type == 2){
						if(i < answIdList.size() - 1){
							int sortAnswId = answIdList.get(i+1);
							answIdList.set(i, sortAnswId);
							answIdList.set(i+1, answid);
							jsonResult.setSuccess(true);
							jsonResult.addParam("sortAnswId", sortAnswId);
							break;
						}else{
							return jsonResult.setMessage("当前选项已排在最后一位！");
						}
					}
				}
			}
		}
		String[][] strData = answInfoService.findMinOrder(answIdList);
		int nOrderID = 0;
		if (strData != null && strData.length > 0)
			nOrderID = NumberUtil.getInt(strData[0][0]);
		
		int nLen = answIdList.size();
		for (int j = 0; j < nLen; j++) {
			boolean bl = answInfoService.modifyOrder(answIdList.get(j),nOrderID + nLen -1 -j);
			if(!bl){
				return jsonResult.setMessage("操作失败，请联系管理员！");
			}
		}
		return jsonResult.setSuccess(true);
	}

	@RequestMapping("selectAnswId")
	public ModelAndView selectAnswId(String dczjid,String quesid) {
		ModelAndView model = new ModelAndView("dczj/ques/ques_selectanswid");
		dczjid = StringUtil.getSafeString(dczjid);
		quesid = StringUtil.getSafeString(quesid);
		
		QuesInfo quesEn = quesInfoService.findQuesEntityByIid(NumberUtil.getInt(quesid));
		List<QuesInfo> quesList = quesInfoService.findQuesBeforeThisQuesId(dczjid,quesid);
		String quesName = quesEn.getQuesname();
		if(quesName.length()>20){
			quesName = quesName.substring(0,20)+"...";
		}
		int d_answid =  quesEn.getRelyanswid();
		String d_answname = "";
		if(d_answid>0){
			AnswInfo answEn = answInfoService.getEntity(NumberUtil.getInt(d_answid));
			d_answname = answEn.getAnswname();
		}
		model.addObject("quesId", quesid);
		model.addObject("quesName", quesName);
		model.addObject("quesList", quesList);
		model.addObject("d_answname", d_answname);
		model.addObject("d_answid", d_answid);
		model.addObject("url", "selectAnswId_submit.do");
		return model;
	}
	
	@RequestMapping("selectAnswIdSubmit")
	@ResponseBody
	public JsonResult selectAnswList(String quesid,String answid) {
		JsonResult jsonResult = JsonResult.getInstance();
		quesid = StringUtil.getSafeString(quesid);
		answid = StringUtil.getSafeString(answid);
		boolean bl = false;
		QuesInfo quesEn = quesInfoService.findQuesEntityByIid(NumberUtil.getInt(quesid));
		if(quesEn != null){
			quesEn.setRelyanswid(NumberUtil.getInt(answid));
			bl = quesInfoService.modify(quesEn);
		}
		return bl ? jsonResult.set(ResultState.MODIFY_SUCCESS) : jsonResult.set(ResultState.MODIFY_FAIL);
	}
	
	@RequestMapping("clearAnswId")
	@ResponseBody
	public JsonResult clearAnswId(String quesId) {
		JsonResult jsonResult = JsonResult.getInstance();
		quesId = StringUtil.getSafeString(quesId);
		boolean bl = false;
		QuesInfo quesEn = quesInfoService.findQuesEntityByIid(NumberUtil.getInt(quesId));
		if(quesEn != null){
			quesEn.setRelyanswid(0);
			bl = quesInfoService.modify(quesEn);
		}
		return bl ? jsonResult.set(ResultState.MODIFY_SUCCESS) : jsonResult.set(ResultState.MODIFY_FAIL);
	}
	
	/**
	 * 备选答案图片导入页面跳转
	 * @return
	 */
	@RequestMapping("answimg_import")
	public ModelAndView showTemplateImport(String dczjid,String quesid,String answid) {
		ModelAndView  modelAndView = new ModelAndView("dczj/ques/answ_import");
		modelAndView.addObject("dczjid", dczjid);
		modelAndView.addObject("quesid", quesid);
		if("-1".equals(answid)){
			answid="0";
		}
		modelAndView.addObject("answid", answid);
		modelAndView.addObject("url","submitanswimg_import.do");
		return modelAndView;
	}
	
	/**
	 * 备选答案图片导入保存提交
	 * @param file
	 * @return
	 */
	@RequestMapping("submitanswimg_import")
	@ResponseBody
	public String submitFileUpload(MultipartFile file,String dczjid,String quesid,String answid) {
		Script script = Script.getInstanceWithJsLib();
		String message = "";
		String newFileName = "";
		String path = "";
		if (file.isEmpty()) {
			message = SpringUtil.getMessage("import.nofile");
		} else {
			try {
			// 可以用MultipartFileInfo获取上传文件信息
			MultipartFileInfo info = MultipartFileInfo.getInstance(file);
			if(StringUtil.isNotEmpty(answid)){
				path = BaseInfo.getRealPath() + "/"+ "answimg" + "/dczjid_"+dczjid+"/quesid_"+quesid+"/answid_"+answid+"/";
			}else{
				path = BaseInfo.getRealPath() + "/"+ "answimg" + "/dczjid_"+dczjid+"/quesid_"+quesid+"/answid_0/";
			}
		    newFileName = info.getFileFullName();
		    if (!"jpg".equals((newFileName.substring(newFileName.indexOf(".") + 1)).toLowerCase())
					&& !"png".equals((newFileName.substring(newFileName.indexOf(".") + 1)).toLowerCase())
					&& !"jpeg".equals((newFileName.substring(newFileName.indexOf(".") + 1)).toLowerCase())) {
				message = "只能上传.jpg/.png/.jpeg后缀的图片!";
				script.addAlert(message);
				return script.getScript();
			}

			FileUtil.createDir(path);
			//删除文件夹path下的所有文档
			deleAll(path);
			File desFile = new File(path + newFileName);
			if(!desFile.exists()){
				// 开始拷贝
				boolean copyContent = ControllerUtil.writeMultipartFileToFile(desFile, file);
				if(!copyContent){
					message = "拷贝文件出错!";
					script.addAlert(message);
					return script.getScript();
				}else{
					if(Settings.getSettings().getEnableoss() == 1){
						String imagepath ="answimg" + "/dczjid_"+ dczjid+"/quesid_"+quesid+"/answid_"+answid+"/";
						String ossFilePath = fileUtil.getAbsolutePath(imagepath);
                        
                        File tempfile = new File(Settings.getSettings().getFileTmp() + newFileName);
						localFileUtil.copyFile(desFile, tempfile);
						
						fileUtil.deleteDirectory(ossFilePath);
						fileUtil.createDir(ossFilePath);
						fileUtil.moveFile(tempfile, ossFilePath +newFileName);
					}
				}
			}else{
				message = "上传图片名称已存在，请重新命名!";
				script.addAlert(message);
				return script.getScript();
			}
		} catch (OperationException e) {
				message = e.getMessage();
			}
			
			if (StringUtil.isEmpty(message)) {
				script.addScript("parent.closeDialog();parent.getParentWindow().crossname('"+newFileName+"','"+answid+"');parent.getParentWindow().addanswdisplay('"+answid+"');");
			}
		}
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
		 if(tempList == null) {
			 return isSuccess;
		 }
		 File temp = null;
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
}
