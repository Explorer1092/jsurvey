package com.hanweb.dczj.controller.count;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.ExcelUtil;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ZipUtil;
import com.hanweb.common.util.mvc.ControllerUtil;
import com.hanweb.common.util.mvc.FileResource;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.RadioReco;
import com.hanweb.dczj.entity.TotalReco;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;
import com.hanweb.dczj.service.TotalRecoService;

@Controller
@RequestMapping("manager/count")
public class OprJoinDetailController {

	@Autowired
	TotalRecoService totalRecoService;
	@Autowired
	QuesInfoService quesInfoService;
	@Autowired
	AnswInfoService answInfoService;
	@Autowired
	CheckedBoxRecoService checkedBoxRecoService;
	@Autowired
	RadioRecoService radioRecoService;
	@Autowired
	ContentRecoService contentRecoService;
	
	@RequestMapping(value = "showdetail")
	public ModelAndView showdetail(String id) {
		ModelAndView modelAndView = new ModelAndView("dczj/count/detail");
		TotalReco totalReco = totalRecoService.findById(id);
//		List<CheckedBoxReco> checkedBoxRecoList=checkedBoxRecoService.findByunid(totalReco.getUnid());
		/*List<RadioReco> radioRecoList=radioRecoService.findByuuid(totalReco.getUnid());*/
		AnswInfo answer=new AnswInfo();
//		List<AnswInfo> answInfos1=new ArrayList<>();
		List<AnswInfo> answInfos=null;
		int sumscore=0;
		float totalscore=0;
		int que_num=0;
		int score=totalReco.getSumscore();
		int right_num=0;
		int right_num1=0;
		int right_num2=0;
		List<QuesInfo> quesInfos = quesInfoService.findQuesListByDczjid(totalReco.getDczjid()+"");
		for(QuesInfo quesInfo : quesInfos) {
			String answname = "";
			totalscore = 0;
			answInfos=answInfoService.getAnswListByQuesId(quesInfo.getIid());
			quesInfo.setAnswMoreList(answInfos);
			/*right_num +=checkedBoxRecoService.findNumByunid(totalReco.getUnid())+radioRecoService.findNumByuuid(totalReco.getUnid());*/
			if(quesInfo.getType()==6||quesInfo.getType()==7) {
				sumscore += quesInfo.getQuesscore();
				que_num++;
			}
			if(quesInfo.getType()==0) {	
//				List<RadioReco> radioRecoList=radioRecoService.findByuuid(totalReco.getUnid(),quesInfo.getIid());
//				for (RadioReco radioReco : radioRecoList) {
//					answer=answInfoService.getEntity(radioReco.getAnswid());
//					if(answer.getIsright().equals("1")) {
//						right_num1++;
//						totalscore=quesInfo.getQuesscore();
//						
//					}
//				}
				quesInfo.setAnswcount(NumberUtil.getInt(totalscore));
				quesInfo.setAnswvalue(radioRecoService.getValueByDczjidAndQueid(totalReco.getDczjid()+"", quesInfo.getIid()+"", totalReco.getUnid()));
			}else if(quesInfo.getType()==6) {	
				List<RadioReco> radioRecoList=radioRecoService.findByuuid(totalReco.getUnid(),quesInfo.getIid());
				for (RadioReco radioReco : radioRecoList) {
					answer=answInfoService.getEntity(radioReco.getAnswid());
					if(answer.getIsright().equals("1")) {
						/*score1 +=quesInfo.getQuesscore();*/
						right_num1++;
						totalscore=quesInfo.getQuesscore();
						answname += answer.getAnswname() +",";
					}
				}
				if(StringUtil.isNotEmpty(answname)) {
					answname = answname.substring(0,answname.length()-1);
				}
				quesInfo.setAnswcount(NumberUtil.getInt(totalscore));
				quesInfo.setAnswvalue(answname);
			}else if(quesInfo.getType()==1) {
//				for (AnswInfo answerinfo : answInfos) {
//					if(checkedBoxRecoService.findCountByunid(totalReco.getUnid(),answerinfo.getIid())>=1) {
//						right_num2++;
//						totalscore=quesInfo.getQuesscore();
//					}
//					
//				}
				quesInfo.setAnswcount(NumberUtil.getInt(totalscore));
				quesInfo.setAnswvalue(checkedBoxRecoService.getValueByDczjidAndQuesid(totalReco.getDczjid()+"", quesInfo.getIid()+"", totalReco.getUnid()));
			}else if(quesInfo.getType()==7) {
			    String useranswids = "";
			    String rightanswids = "";
			    for (AnswInfo answerinfo : answInfos) {
				    if(checkedBoxRecoService.findCountByunid(totalReco.getUnid(),answerinfo.getIid())>=1) {
					    useranswids += answerinfo.getIid() + ",";
					    answname += answerinfo.getAnswname() +",";
				    }
				    if(StringUtil.equals(answerinfo.getIsright(), "1")) {
					    rightanswids += answerinfo.getIid() + ",";
				    }
			    }
			    if(StringUtil.equals(rightanswids, useranswids)) {
				    right_num2 ++;
				    totalscore=quesInfo.getQuesscore();
			    }
			    if(StringUtil.isNotEmpty(answname)) {
				    answname = answname.substring(0,answname.length()-1);
			    }
			    quesInfo.setAnswcount(NumberUtil.getInt(totalscore));
			    quesInfo.setAnswvalue(answname);
		    }else {
				quesInfo.setAnswvalue(contentRecoService.getValueByDczjidAndQuesid(totalReco.getDczjid()+"", quesInfo.getIid()+"", totalReco.getUnid()));
			}
			totalReco.setSumscore(score);
			right_num=right_num1+right_num2;
		}
		modelAndView.addObject("quesInfos", quesInfos);
		modelAndView.addObject("que_num", que_num);
		modelAndView.addObject("right_num", right_num);
		modelAndView.addObject("answer",answer);
		modelAndView.addObject("answInfos",answInfos);
		modelAndView.addObject("score",score);
		modelAndView.addObject("sumscore",sumscore);
		modelAndView.addObject("totalscore",totalscore);
		modelAndView.addObject("totalReco",totalReco);
		return modelAndView;
	}
	
	
	public String remove(String id) {
		int n = totalRecoService.removeById(id);
		if(n > 0) {
			return "{\"isSuccess\":1}";
		}else {
			return "{\"isSuccess\":0}";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "removeall")
	public JsonResult removeAll(String dczjid) {
		JsonResult result = JsonResult.getInstance();
		int n = totalRecoService.removeAll(dczjid);
		if(n > 0) {
			return result.setSuccess(true);
		}else {
			return result.setSuccess(false).setMessage("删除失败，请联系系统管理员");
		}
	}
	
	@RequestMapping(value = "exportdetailnew")
	public ModelAndView exportArticleDay(String dczjid,String starttime,String endtime){
		ModelAndView modelAndView = new ModelAndView();
		String fileName = exportdetail(dczjid, starttime, endtime);
		RedirectView redirectView = new RedirectView(BaseInfo.getContextPath()+ "/manager/count/downloadDetailFile.do?fileName="+fileName);
		modelAndView.setView(redirectView);
		return modelAndView;
	}
	
	@RequestMapping("downloadDetailFile")
	public FileResource downloadFile(String fileName) {
		String filePath = BaseInfo.getRealPath() + "/tempfile/" +fileName;
		File file = new File(filePath);
		return ControllerUtil.getFileResource(file, fileName);
	}
	
	public String exportdetail(String dczjid,String starttime,String endtime) {
		String filePath = "";
		List<List<String>> rows = new ArrayList<List<String>>();
		List<String> headList = new ArrayList<String>();/* 表头 */
		List<String> valueList = null; /* 数据列 */
		String type = "";
		
		List<TotalReco> totalRecos = totalRecoService.findSubmitRecoListsByFormid(dczjid, starttime, endtime);
		Map<String, String> valueMap = totalRecoService.getExcelValueMap(dczjid);
		
		/* 表头数据*/
		headList.add("参与人ip");
		headList.add("提交来源");
		headList.add("提交时间");
		List<QuesInfo> quesInfos = quesInfoService.findQuesListForExcel(dczjid);
		for(QuesInfo quesInfo : quesInfos) {
			headList.add(quesInfo.getQuesname());
		}
		rows.add(headList);
		/*数据列*/
		for(TotalReco totalReco : totalRecos) {
//			System.out.println("abc == "  + abc++);
			valueList = new ArrayList<String>();
			valueList.add(totalReco.getIp());
			if(totalReco.getType()==1) {
				type = "pc端";
			}else if(totalReco.getType()==2) {
				type = "手机端";
			}else {
				type = "微信";
			}
			valueList.add(type);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createDate = sdf.format(totalReco.getCreatedate());
			valueList.add(createDate);
			for(QuesInfo quesInfo : quesInfos) {
				valueList.add(valueMap.get(quesInfo.getIid()+"_"+totalReco.getUnid()));
			}
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
}
