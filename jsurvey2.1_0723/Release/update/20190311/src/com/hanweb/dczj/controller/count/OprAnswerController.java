package com.hanweb.dczj.controller.count;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.ExcelUtil;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ZipUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.ContentReco;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.RadioReco;
import com.hanweb.dczj.entity.Sensitive;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.CountService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;
import com.hanweb.dczj.service.SensitiveService;

@Controller
@RequestMapping("manager/count")
public class OprAnswerController {
	
	@Autowired
	private RadioRecoService radioRecoService;
	@Autowired
	private CheckedBoxRecoService checkedBoxRecoService;
	@Autowired
	private ContentRecoService contentRecoService;
	@Autowired
	private QuesInfoService quesInfoService;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private CountService countService;
	@Autowired
	private SensitiveService sensitiveService;
	
	
	@ResponseBody
	@RequestMapping("showotheranser")
	public JsonResult showOtherAnser() {
		String dczjid = SpringUtil.getRequest().getParameter("dczjid");
		String answid = SpringUtil.getRequest().getParameter("answid");
		String quesid = SpringUtil.getRequest().getParameter("quesid");
		String type = SpringUtil.getRequest().getParameter("type");
		String starttime = SpringUtil.getRequest().getParameter("starttime");
		String endtime = SpringUtil.getRequest().getParameter("endtime");
		String page = SpringUtil.getRequest().getParameter("page");
		String limit = SpringUtil.getRequest().getParameter("limit");
		
		JsonResult jsonResult = JsonResult.getInstance();
		String divDom = "";
		Integer count = 0;
		try {
			if(StringUtil.equals("0", type)) {
				divDom = radioRows(page, limit, dczjid, quesid, answid, starttime, endtime);
				count = radioRecoService.radioAnsListCount(dczjid, quesid, answid, starttime, endtime);
			}else if(StringUtil.equals("1", type)) {
				divDom = checkBoxRows(page, limit, dczjid, quesid, answid, starttime, endtime);
				count = checkedBoxRecoService.checkBoxAnsListCount(dczjid, quesid, answid, starttime, endtime);
			}else {
				divDom = contentRows(page, limit, dczjid, quesid, answid, starttime, endtime);
				count = contentRecoService.findEntityCountByTime(dczjid, quesid, answid, starttime, endtime);
			}
			jsonResult.setSuccess(true).addParam("count", count).addParam("divDom",divDom);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}

	@ResponseBody
	@RequestMapping("updateexamine")
	public JsonResult updateExamine(String ids) {
		JsonResult jsonResult = JsonResult.getInstance();
		String[] idArray = ids.split(",");
		String contentid = "";
		try {
			for(int i=0;i<idArray.length;i++) {
				contentid = idArray[i];
				contentRecoService.updateAllAudiById(contentid);
			}
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@ResponseBody
	@RequestMapping("removeanswer")
	public JsonResult remove(String ids) {
		JsonResult jsonResult = JsonResult.getInstance();
		String[] idArray = ids.split(",");
		String contentid = "";
		try {
			for(int i=0;i<idArray.length;i++) {
				contentid = idArray[i];
				contentRecoService.deleteContentByid(contentid);
			}
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@ResponseBody
	@RequestMapping("examineanswer")
	public JsonResult examineanswer(String id, String audi) {
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			contentRecoService.updateAudiById(id, audi);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@RequestMapping("showexaminepage")
	public ModelAndView showExaminePage(Integer contentid) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/examineansw");
		ContentReco contentReco = contentRecoService.findEntityById(contentid+"");
		
		String content = contentReco.getAnswcontent();
		List<Sensitive> sensitiveList = null;
		if(StringUtil.isNotEmpty(content)){
			int pageNum = 0;
			int tcount = 0;
			int pagesize = 1000;
			tcount = sensitiveService.getcount();
			int a = tcount % pagesize;
			int b = tcount / pagesize;
			if (a == 0) {
				pageNum = b;
			} else {
				pageNum = b + 1;
			}
			for(int p = 0;p<pageNum; p++ ){
				sensitiveList = sensitiveService.findsensitiveList(p,pagesize);
				if(sensitiveList != null){
					Iterator<Sensitive> it = sensitiveList.iterator();
					while(it.hasNext()){
						Sensitive sensitiveEn = (Sensitive)it.next();
						String vc_sensitiveword = sensitiveEn.getVc_sensitiveword();
						int num = content.indexOf(vc_sensitiveword);
						if(num >= 0){
							String newword = "<span style=\"background-color: rgb(255, 0, 0);\">"+vc_sensitiveword+"</span>";
							num = content.indexOf(newword);
							if(num == -1){
								content = content.replaceAll(vc_sensitiveword, newword);
							}
						}
					}
				}
			}
		}
		contentReco.setAnswcontent(content);
		modelAndView.addObject("contentReco",contentReco);
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("savereplycontent")
	public JsonResult savereplycontent(String contentid, String replycontent) {
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			contentRecoService.saveReply(contentid, replycontent);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@ResponseBody
	@RequestMapping("unaudi")
	public JsonResult unaudi(String contentid) {
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			contentRecoService.updateAudiById(contentid, "0");
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@ResponseBody
	@RequestMapping("saveandaudi")
	public JsonResult saveandaudi(String contentid, String replycontent) {
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			contentRecoService.saveAndAudi(contentid, replycontent);
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@ResponseBody
	@RequestMapping("dispass")
	public JsonResult dispass(String contentid) {
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			contentRecoService.updateAudiById(contentid, "2");
			jsonResult.setSuccess(true);
		} catch (Exception e) {
			jsonResult.setSuccess(false).setMessage("操作失败，请联系系统管理员");
		}
		return jsonResult;
	}
	
	@RequestMapping("exportansandrep")
	public ModelAndView exportansandrep(String dczjid,String answid,String quesid,String starttime,String endtime) {
//		File file = null;
//		FileResource fileResouce = null;
//		try {
//			String filePath = exportdetailStr(dczjid, answid, quesid, starttime, endtime);
//			file = new File(filePath);
//			fileResouce = ControllerUtil.getFileResource(file, "答案详情列表.xls");
//		} catch (Exception e) {
//			return null;
//		} finally {
//			if (file !=null && file.exists()) {
//				file.delete();
//			}
//		}
		ModelAndView modelAndView = new ModelAndView();
		String fileName = exportdetailStr(dczjid, answid, quesid, starttime, endtime);
		RedirectView redirectView = new RedirectView(BaseInfo.getContextPath()+ "/manager/count/downloadMaxFile.do?fileName="+fileName);
		modelAndView.setView(redirectView);
		return modelAndView;
	}
	
	@RequestMapping("downloadMaxFile")
	public FileResource downloadFile(String fileName) {
		String filePath = BaseInfo.getRealPath() + "/tempfile/" +fileName;
		File file = new File(filePath);
		return ControllerUtil.getFileResource(file, fileName);
	}
	
	@RequestMapping("export_reportlist")
	public FileResource exportReportList(String dczjid,String starttime,String endtime) {
		File file = null;
		FileResource fileResouce = null;
		try {
			String filePath = exportReportStr(dczjid, starttime, endtime);
			file = new File(filePath);
			fileResouce = ControllerUtil.getFileResource(file, "结果报表列表.xls");
		} catch (Exception e) {
			return null;
		} finally {
			if (file !=null && file.exists()) {
				file.delete();
			}
		}
		return fileResouce;
	}
	
	public String exportReportStr(String dczjid, String starttime, String endtime) {
		int count = 0;
		int sum = 0;
		double percent = 0.0;
		String filePath = "";
		List<List<String>> rows = new ArrayList<List<String>>();
		//List<String> headList = new ArrayList<String>();/* 表头 */
		List<String> valueList = null; /* 数据列 */
		
		List<QuesInfo> quesInfos = quesInfoService.findQuesListForExcel(dczjid);
		for(QuesInfo quesInfo : quesInfos) {
			if(quesInfo.getType()==2 || quesInfo.getType()==5) {
				/*问题*/
				valueList = new ArrayList<String>();
				valueList.add(quesInfo.getQuesname());
				rows.add(valueList);
				/*答案主体*/
				valueList = new ArrayList<String>();
				valueList.add("本题回收"+contentRecoService.findContentSumByTime(quesInfo.getDczjid(), quesInfo.getIid(), starttime, endtime)+"条，更多详情请导出明细数据查看");
				rows.add(valueList);
			}else if(quesInfo.getType()==0 || quesInfo.getType()==1) {
				sum = 0;
				/*问题*/
				valueList = new ArrayList<String>();
				valueList.add(quesInfo.getQuesname());
				rows.add(valueList);
				/*答案标题*/
				valueList = new ArrayList<String>();
				valueList.add("选项");
				valueList.add("小计");
				valueList.add("百分比");
				rows.add(valueList);
				/*答案主体*/
				List<AnswInfo> answInfos = answInfoService.getAnswListByQuesId(quesInfo.getIid());
				if(answInfos != null && answInfos.size() > 0) {
					for(AnswInfo answInfo : answInfos) {
						count = countService.getCountResult(starttime, endtime, answInfo.getDczjid(), answInfo.getQuesid(), answInfo.getIid());
						answInfo.setCount(count);
						sum += count;
					}
					BigDecimal sumdecimal = new BigDecimal(sum);
					BigDecimal countdecimal = null;
					for(AnswInfo answInfo : answInfos) {
						if(answInfo.getCount()==0) {
							percent = 0.0;
						}else {
							countdecimal = new BigDecimal(answInfo.getCount());
							percent = countdecimal.multiply(new BigDecimal(100)).divide(sumdecimal, 2, RoundingMode.HALF_UP).doubleValue();
						}
						valueList = new ArrayList<String>();
						valueList.add(answInfo.getAnswname());
						valueList.add(answInfo.getCount()+"");
						valueList.add(percent+"%");
						rows.add(valueList);
					}
				}
			}else {
				continue;
			}
			
			/*末尾空格*/
			valueList = new ArrayList<String>();
			valueList.add("");
			rows.add(valueList);
		}
		
		/* 写入文件 */
		String fileName = StringUtil.getUUIDString() + ".xls";
		filePath = BaseInfo.getRealPath() + "/tempfile/" + fileName;
		ExcelUtil.writeExcel(filePath, rows);
		return filePath;
	}
	
	public String exportdetailStr(String dczjid,String answid,String quesid,String starttime,String endtime) {
		String filePath = "";
		List<List<String>> rows = new ArrayList<List<String>>();
		List<String> headList = new ArrayList<String>();/* 表头 */
		List<String> valueList = null; /* 数据列 */
		String type = "";
		int page = 0;
		List<ContentReco> contentRecos = new ArrayList<ContentReco>();
		int count = contentRecoService.findEntityCountByTime(dczjid, quesid, answid, starttime, endtime);
		page = count/1000 + 1;
		for(int i=1;i<=page;i++) {
			contentRecos.addAll(contentRecoService.findEntityListByTime(i+"", 1000+"", dczjid, quesid, answid, starttime, endtime));
		}
		
		/* 表头数据*/
		headList.add("答案详情");
		headList.add("审核状态");
		headList.add("回复");
		rows.add(headList);
		
		/*数据列*/
		for(ContentReco contentReco : contentRecos) {
			valueList = new ArrayList<String>();
			valueList.add(contentReco.getAnswcontent());
			if(StringUtil.equals("0", contentReco.getAudi())) {
				type = "未审核";
			}else if(StringUtil.equals("1", contentReco.getAudi())) {
				type = "已通过";
			}else {
				type = "未通过";
			}
			valueList.add(type);
			valueList.add(contentReco.getReplycontent());
			rows.add(valueList);
		}
		
		String zipname = StringUtil.getUUIDString();
		List<List<String>> newrows = null;
		if(rows != null && rows.size() > 0) {
			int pagesize = 50000;
			int rowsize = rows.size();
			int pageno = rowsize/pagesize;
			for(int i = 0;i<=pageno;i++) {
				newrows = new ArrayList<List<String>>();
				int startPageSize = i*pagesize;
				int endPageSize = (i+1)*pagesize-1;
				if(rowsize < endPageSize) {
					endPageSize = rowsize;
				}
				for(int ii = startPageSize;ii<endPageSize;ii++) {
					newrows.add(rows.get(ii));
				}
				FileUtil.createDir(BaseInfo.getRealPath() + "/tempfile/"+zipname);
				String fileName = StringUtil.getUUIDString() + ".xls";
				filePath = BaseInfo.getRealPath() + "/tempfile/"+zipname+"/" + fileName;
				ExcelUtil.writeExcel(filePath, newrows);
			}
		}
		
		/* 写入文件 */
		String zipfileName = zipname + ".zip";
		String zipfilePath = BaseInfo.getRealPath() + "/tempfile/"+zipname;
		File outfile = new File(BaseInfo.getRealPath() + "/tempfile/" + zipfileName);
		File file = new File(zipfilePath);
		ZipUtil.zip(file, outfile);
		return zipfileName;
	}
	
	public String rowString(String value, String timeStr) {
		String row = "<div style=\"width: 100%;height: 70px;border-bottom: 1px solid #f0f0f0;\">" + 
				"		<div style=\"padding-left: 20px;padding-top: 5px;\">"+value+"</div>" + 
				"		<div style=\"float: right;margin-right: 15px;padding-top: 25px;\">"+timeStr+"</div>" + 
				"	</div>";
		return row;
	}
	
	public String radioRows(String page, String limit, String dczjid, String quesid, String answid, String starttime, String endtime) {
		StringBuffer radioStr = new StringBuffer();
		StringBuffer value = new StringBuffer();
		StringBuffer timeStr = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		List<RadioReco> radioRecos = radioRecoService.radioAnsList(page, limit, dczjid, quesid, answid, starttime, endtime);
		if(radioRecos!=null && radioRecos.size()>0) {
			for(RadioReco radioReco : radioRecos) {
				value = new StringBuffer(radioReco.getAnswcontent());
				timeStr = new StringBuffer("提交时间:"+sdf.format(radioReco.getSubmittime()));
				radioStr.append(rowString(value.toString(), timeStr.toString()));
			}
		}
		return radioStr.toString();
	}
	
	public String checkBoxRows(String page, String limit, String dczjid, String quesid, String answid, String starttime, String endtime) {
		StringBuffer radioStr = new StringBuffer();
		StringBuffer value = new StringBuffer();
		StringBuffer timeStr = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		List<CheckedBoxReco> checkedBoxRecos = checkedBoxRecoService.checkBoxAnsList(page, limit, dczjid, quesid, answid, starttime, endtime);
		if(checkedBoxRecos!=null && checkedBoxRecos.size()>0) {
			for(CheckedBoxReco checkedBoxReco : checkedBoxRecos) {
				value = new StringBuffer(checkedBoxReco.getAnswcontent());
				timeStr = new StringBuffer("提交时间:"+sdf.format(checkedBoxReco.getSubmittime()));
				radioStr.append(rowString(value.toString(), timeStr.toString()));
			}
		}
		return radioStr.toString();
	}
	
	public String contentRows(String page, String limit, String dczjid, String quesid, String answid, String starttime, String endtime) {
		StringBuffer radioStr = new StringBuffer();
		StringBuffer value = new StringBuffer();
		StringBuffer timeStr = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		List<ContentReco> contentRecos = contentRecoService.findEntityListByTime(page, limit, dczjid, quesid, answid, starttime, endtime);
		if(contentRecos!=null && contentRecos.size()>0) {
			for(ContentReco contentReco : contentRecos) {
				value = new StringBuffer(contentReco.getAnswcontent());
				timeStr = new StringBuffer("提交时间:"+sdf.format(contentReco.getSubmittime()));
				radioStr.append(rowString(value.toString(), timeStr.toString()));
			}
		}
		return radioStr.toString();
	}
}
