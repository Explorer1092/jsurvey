package com.hanweb.dczj.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.ExcelUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.dczj.dao.QuesInfoDAO;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.TitleInfo;

public class QuesInfoService {

	@Autowired
	private QuesInfoDAO quesInfoDAO;
	@Autowired
	private AnswInfoService answInfoService;
	@Autowired
	private TitleInfoService titleInfoService;
	/**
	 * 通过dczjid和type查找集合
	 * @param dczjid
	 * @param type
	 * @return
	 */
	public List<QuesInfo> findQuesListByDczjidAndType(String dczjid ,String type){
		return quesInfoDAO.findQuesListByDczjidAndType(dczjid ,type);
	}
	
	/**
	 * 题目新增
	 * @param dczjid
	 * @param type
	 * @return
	 */
	public int addEn(int dczjid, int type) {
		QuesInfo quesen = new QuesInfo();
		quesen.setDczjid(dczjid);
		if(type == 0){
			quesen.setQuesname("单选题");
		}else if(type == 1){
			quesen.setQuesname("多选题");
		}else if(type == 2){
			quesen.setQuesname("多行文本题");
		}else if(type == 3){
			quesen.setQuesname("文字说明");
		}else if(type == 4){
			quesen.setQuesname("分页");
		}else if(type == 5){
			quesen.setQuesname("单行文本题");
		}else if(type == 6) {
			quesen.setQuesname("测评单选题");
		}else if(type == 7) {
			quesen.setQuesname("测评多选题");
		}else if(type == 8) {
			quesen.setQuesname("下拉题");
		}else if(type == 9) {
			quesen.setQuesname("联动题");
		}
		quesen.setOrderid(0);
		quesen.setNote("");
		quesen.setType(type);
		quesen.setCol(1);
		quesen.setIsmustfill(0);
		quesen.setMinselect(1);
		quesen.setMaxselect(3);
		quesen.setTextinputwidth(860);
		quesen.setTextinputheight(184);
		if(type == 3){
			quesen.setContent("文字说明");
		}else{
			quesen.setContent("");
		}
		quesen.setState(0);
		quesen.setDczjtype(0);
		quesen.setRelyanswid(0);
		quesen.setShowpublish(1);
		quesen.setValidaterules(0);
		return quesInfoDAO.insert(quesen);
	}

	/**
	 * 问题新增
	 * @param en
	 * @return
	 */
	public int addQues(QuesInfo en) {
		return quesInfoDAO.insert(en);
	}
	
	/**
	 * 通过dczjid查找集合
	 * @param dczjid
	 * @return
	 */
	public List<QuesInfo> findQuesListByDczjid(String dczjid) {
		return quesInfoDAO.findQuesListByDczjid(dczjid);
	}
	
	/**
	 * 通过dczjid查找集合
	 * @param dczjid
	 * @return
	 */
	public List<QuesInfo> findQuesListForExcel(String dczjid) {
		return quesInfoDAO.findQuesListForExcel(dczjid);
	}

	/**
	 * 通过主键查找实体
	 * @param quesid
	 * @return
	 */
	public QuesInfo findQuesEntityByIid(Integer quesid) {
		return quesInfoDAO.queryForEntityById(quesid);
	}

	/**
	 * 删除
	 * @param quesid
	 * @return
	 */
	public boolean delete(String quesid) {
		boolean deleteResult = false;
		//通过问题id查询问题表中是否有数据
		List<AnswInfo> answInfoList = answInfoService.getAnswListByQuesId(NumberUtil.getInt(quesid));
		if(answInfoList != null && answInfoList.size() > 0){
			deleteResult = answInfoService.deleteByQuesid(quesid);
			if(!deleteResult){
				return deleteResult;
			}
		}
		deleteResult = quesInfoDAO.deleteById(quesid, "1");//删除问题
		
		return deleteResult;
	}

	public QuesInfo findQuesByDczjid(Integer dczjid) {
		return quesInfoDAO.findQuesByDczjid(dczjid);
	}

	public List<QuesInfo> findQuesListByIid(int iid) {
		return quesInfoDAO.findQuesListByIid(iid);
	}

	/**
	 * 问题修改
	 * @param en
	 * @return
	 */
	public boolean modify(QuesInfo surveyQues) {
		return quesInfoDAO.update(surveyQues);
	}

	/**
	 * 查找排在这道题目之前的调查问题集合
	 * @param formId
	 * @param quesId
	 * @return
	 */
	public List<QuesInfo> findQuesBeforeThisQuesId(String dczjid, String quesid) {
		List<QuesInfo> quesList = quesInfoDAO.findQuesByType(dczjid);
		List<QuesInfo> newQuesList = new ArrayList<QuesInfo>();
		for(QuesInfo quesEn : quesList){
			if(quesEn.getIid() == NumberUtil.getInt(quesid)){
				break;
			}
			List<AnswInfo> answList = answInfoService.getAnswListByQuesId(quesEn.getIid());
			ArrayList<AnswInfo> answMoreList = new ArrayList<AnswInfo>();
			answMoreList = (ArrayList<AnswInfo>) answList;
			quesEn.setAnswMoreList(answMoreList);
			newQuesList.add(quesEn);
		}
		return newQuesList;
	}

	/**
	 * 导出功能
	 * @param ids
	 * @return
	 */
	public String exportDczjQues(int dczjid) {
		String filePath = "";
		List<List<String>> rows = new ArrayList<List<String>>();
		List<String> headList = new ArrayList<String>();/* 表头 */
		List<String> valueList = null; /*数据列 */
		List<QuesInfo> quesInfoList = null;
		TitleInfo title = titleInfoService.getEntity(dczjid);
		
		headList.add("题目名称");
		headList.add("题目备注");
		headList.add("题目类型");
		headList.add("答案列数");
		headList.add("是否必填");
		headList.add("最多选择");
		headList.add("最少选择");
		headList.add("文本框宽度");
		headList.add("文本框高度");
		headList.add("内容");
		headList.add("是否公开");
		headList.add("仅投票用");
		headList.add("原问题id");
		headList.add("选项集合");
		if(title != null) {
		  if(title.getType() == 3) {
			 headList.add("得分");  
		  }
		}
		rows.add(headList);//Survey_Ques
		
		quesInfoList = this.findQuesListByDczjid(dczjid+"");
		if(quesInfoList != null && quesInfoList.size() > 0){
			for(QuesInfo quesInfo : quesInfoList){
				valueList = new ArrayList<String>();
				String quesname = "";
				if(quesInfo != null) {
				    quesname = quesInfo.getQuesname();
				    if(quesname.indexOf("<img") != -1) {
						quesname = StringUtil.removeHTML(quesname)+"[图片]";
					}
					quesname = StringUtil.removeHTML(quesname).replace("&nbsp;"," ");
				}
				
				if(quesInfo != null) {
					valueList.add(quesname);
					valueList.add(quesInfo.getNote());
					valueList.add(quesInfo.getType()+"");
					valueList.add(quesInfo.getCol()+"");
					valueList.add(quesInfo.getIsmustfill()+"");
					valueList.add(quesInfo.getMaxselect()+"");
					valueList.add(quesInfo.getMinselect()+"");
					valueList.add(quesInfo.getTextinputwidth()+"");
					valueList.add(quesInfo.getTextinputheight()+"");
					valueList.add(quesInfo.getContent());
					valueList.add(quesInfo.getShowpublish()+"");
					valueList.add(quesInfo.getDczjtype()+"");
					valueList.add(quesInfo.getIid()+"");	
				}				
				
				List<AnswInfo> list = new ArrayList<AnswInfo>();		
				if(quesInfo != null) {
					list = answInfoService.getAnswListByQuesId(quesInfo.getIid());	
				}
				
				String surveyAnswsList = "";
				if(list != null && list.size() > 0) {
					for(AnswInfo answInfo : list) {
						String ansname = answInfo.getAnswname();
						if(ansname.indexOf("<img") != -1) {
							ansname = StringUtil.removeHTML(ansname)+"[图片]";
						}
						ansname = StringUtil.removeHTML(ansname).replace("&nbsp;"," ");
						if(quesInfo.getType()==6 || quesInfo.getType()==7) {
							if(StringUtil.equals(answInfo.getIsright(), "1")) {
								ansname = ansname + "（正确答案）";
							}	
						}
						surveyAnswsList += ansname+",";
					}
				}
				if(surveyAnswsList !="" && surveyAnswsList!=null) {
					surveyAnswsList = surveyAnswsList.substring(0, surveyAnswsList.length()-1);
				}				
				valueList.add(surveyAnswsList);
				if(title != null) {
				 if(title.getType() == 3) {
					 if(quesInfo != null) {
						 if(quesInfo.getType() == 6 || quesInfo.getType() == 7) {
							 valueList.add(StringUtil.getString(quesInfo.getQuesscore())); 
						 } 
					 }
					 
					 
				  }
				}
				rows.add(valueList);
			}
		}
		
		String fileName = StringUtil.getUUIDString() + ".xls";
		filePath = Settings.getSettings().getFileTmp() + fileName;
		ExcelUtil.writeExcel(filePath, rows);
		return filePath;
	}

	/**
	 * 问题导入提交
	 * @param file
	 * @param dczjid
	 * @return
	 */
	public String importQues(File file, int dczjid) {
		if (file == null || !file.exists()) {
			throw new OperationException("无法找到上传的文件！");
		}
		List<Map<String, String>> rows = ExcelUtil.readExcel(file);
		if (CollectionUtils.isEmpty(rows)) {
			throw new OperationException(SpringUtil.getMessage("import.filetype.error"));
		}
		
		TitleInfo title = titleInfoService.getEntity(dczjid);
		Iterator<Map<String, String>> iterator = rows.iterator();
		
		if(title != null) {
			if(title.getType() != 3 && iterator.next().containsKey("得分")) {
				return "导入失败";
			}
	    }
		
		/*while (iterator.hasNext()) {
			if(iterator.next().get("得分")!=null ) {
				
			}
			
		}
		*/
		
		String retMessage = "";
		List<QuesInfo> quesInfoList = this.findQuesListByRows(rows);
		List<AnswInfo> answInfoList = this.findAnswListByRows(rows);
		
		
		try {
			retMessage = this.importSurveyQuesList(quesInfoList,dczjid,answInfoList);
			if (!retMessage.equals("")) {
				retMessage = "<div style='height:150px;overflow:auto'>导入完毕，存在以下问题：<br/>"+ retMessage + "</div>";
			}
			return retMessage;
		} catch (Exception e) {
			e.printStackTrace();
			return "导入失败";
		} finally {
			if (file.exists()) {
				file.delete();
			}
		}
	}


	/**
	 * 循环读取表中题目数据
	 * @param rows
	 * @return
	 */
	private List<QuesInfo> findQuesListByRows(List<Map<String, String>> rows) {
		List<QuesInfo> surveyQuesList = new ArrayList<QuesInfo>();
		Map<String, String> cell = null;
		QuesInfo quesInfo = null;
		/* excel记录转换成用户集合 */
		Iterator<Map<String, String>> iterator = rows.iterator();
		int b = 1;
		while (iterator.hasNext()) {
			cell = iterator.next();
			quesInfo = new QuesInfo();
			quesInfo.setOrderid(0);
			quesInfo.setQuesname(StringUtil.trim(cell.get("题目名称")));
			quesInfo.setNote(StringUtil.trim(cell.get("题目备注")));
			quesInfo.setType(NumberUtil.getInt(StringUtil.trim(cell.get("题目类型"))));
			quesInfo.setCol(NumberUtil.getInt(StringUtil.trim(cell.get("答案列数"))));
			quesInfo.setIsmustfill(NumberUtil.getInt(StringUtil.trim(cell.get("是否必填"))));
			quesInfo.setMaxselect(NumberUtil.getInt(StringUtil.trim(cell.get("最多选择"))));
			quesInfo.setMinselect(NumberUtil.getInt(StringUtil.trim(cell.get("最少选择"))));
			quesInfo.setTextinputwidth(NumberUtil.getInt(StringUtil.trim(cell.get("文本框宽度"))));
			quesInfo.setTextinputheight(NumberUtil.getInt(StringUtil.trim(cell.get("文本框高度"))));
			quesInfo.setContent(StringUtil.trim(cell.get("内容")));
			quesInfo.setShowpublish(NumberUtil.getInt(StringUtil.trim(cell.get("是否公开"))));
			quesInfo.setDczjtype(NumberUtil.getInt(StringUtil.trim(cell.get("仅投票用"))));
			
			String oldquesid = StringUtil.trim(cell.get("原问题id"));
			b++;
//			当Excel 中原问题ID为空时 自动给其赋值
			if(StringUtil.isEmpty(oldquesid)){
				oldquesid = b+"";
			}
			quesInfo.setOldquesid(NumberUtil.getInt(oldquesid));
			if(cell.get("得分") != null && StringUtil.isNotEmpty(cell.get("得分"))) {
				quesInfo.setQuesscore(NumberUtil.getInt(StringUtil.trim(cell.get("得分"))));	
			}
			
			surveyQuesList.add(quesInfo);
		}
		
		return surveyQuesList;
	} 

	/**
	 * 循环读取表中选项数据
	 * @param rows
	 * @return
	 */
	private List<AnswInfo> findAnswListByRows(List<Map<String, String>> rows) {
		List<AnswInfo> surveyAnswsList = new ArrayList<AnswInfo>();
		Map<String, String> cell = null;
		AnswInfo answInfo = null;
		
		int a = 1;
		/* excel记录转换成用户集合 */
		Iterator<Map<String, String>> iterator = rows.iterator();
		while (iterator.hasNext()) {
			cell = iterator.next();
			String answers = StringUtil.trim(cell.get("选项集合"));
			int quesid = NumberUtil.getInt(cell.get("原问题id"));
			
//			当Excel 中原问题ID 为空时 或者为0时 自动为其赋值 
			a++;
			if(StringUtil.isEmpty(quesid+"") || quesid == 0){
				quesid = a;
			}
			if(answers!=null&&answers.length()>0){
				String [] answerLists = answers.split(",");
				for( int ai = 0 ; ai < answerLists.length; ai++){
					answInfo = new AnswInfo();
					String answer = answerLists[ai];
					if(StringUtil.isNotEmpty(answer)) {
					   if(answer.indexOf("正确答案") != -1) {
						  answInfo.setIsright("1");
						  answer = answer.replace("（正确答案）", "");
					   }	 
					}

					answInfo.setAnswname(answer);
					answInfo.setQuesid(quesid);
					answInfo.setState(0);
					answInfo.setAllowfillinair(0);
					answInfo.setAnswimgname("");
					answInfo.setAnswnote("");
					answInfo.setBasepoint(0);
					surveyAnswsList.add(answInfo);
				}
			}
			
		}
		return surveyAnswsList;
	}
	
	/**
	 * 循环插入数据
	 * @param quesInfoList
	 * @param dczjid
	 * @param answInfoList
	 * @return
	 */
	private String importSurveyQuesList(List<QuesInfo> quesInfoList, int dczjid, List<AnswInfo> answInfoList) {
		if(quesInfoList == null){
			return "";
		}
		
		QuesInfo quesInfo = null;
		String message = ""; // 错误提示信息
		int iid = 0;
		StringBuilder result = new StringBuilder();
		for(int i = 0;i < quesInfoList.size();i++){
			quesInfo = quesInfoList.get(i);
			quesInfo.setDczjid(dczjid);;
			quesInfo.setState(0);;
			if(StringUtil.isEmpty(quesInfo.getQuesname())){
				message = "该调查题目不能为空";
				this.getReturnMessage(result, i, message);
				continue;
			}
			if(StringUtil.isEmpty(quesInfo.getNote())){
				quesInfo.setNote("");
			}
			if(StringUtil.isEmpty(quesInfo.getType()+"")){
				message = "该行题目类型不能为空";
				this.getReturnMessage(result, i, message);
				continue;
			}
			if(StringUtil.isEmpty(quesInfo.getCol()+"")){
				quesInfo.setCol(1);
			}
			if(StringUtil.isEmpty(quesInfo.getIsmustfill()+"")){
				quesInfo.setIsmustfill(0);
			}
			if(StringUtil.isEmpty(quesInfo.getMaxselect()+"")){
				quesInfo.setMaxselect(3);;
			}
			if(StringUtil.isEmpty(quesInfo.getMinselect()+"")){
				quesInfo.setMinselect(1);
			}
			if(StringUtil.isEmpty(quesInfo.getTextinputwidth()+"")){
				quesInfo.setTextinputwidth(860);;
			}
			if(StringUtil.isEmpty(quesInfo.getTextinputheight()+"")){
				quesInfo.setTextinputheight(184);;
			}
			if(StringUtil.isEmpty(quesInfo.getShowpublish()+"")){
				quesInfo.setShowpublish(1);;
			}
			if(StringUtil.isEmpty(quesInfo.getDczjtype()+"")){
				quesInfo.setDczjtype(0);;
			}
			iid = quesInfoDAO.insert(quesInfo);
			if(iid <= 0){
				message = "导入调查"+quesInfo.getQuesname()+"出现异常";
				this.getReturnMessage(result, i, message);
				continue;
			}
			
			for( int aa =0; aa<answInfoList.size();aa++){
				int qid = answInfoList.get(aa).getQuesid();
				if(StringUtil.equals(quesInfo.getOldquesid()+"", qid+"")){
					answInfoList.get(aa).setQuesid(iid);
					answInfoList.get(aa).setDczjid(dczjid);
					answInfoService.add(answInfoList.get(aa));
				}
			}
			
			
		}
		return result.toString();
	}

	/**
	 * 组织导入的错误提示
	 * 
	 * @param result
	 *            错误提示信息集
	 * @param i
	 *            信息在List中的序号
	 * @param message
	 *            错误信息
	 */
	private void getReturnMessage(StringBuilder result, int i, String message) {
		result.append("<li>");
		result.append("[" + SpringUtil.getMessage("import.error", i + 2) + "]" + message);
		result.append("</li>");
	}

	/**
	 * 根据dczjid 和 quesid 查询分页的页码
	 * @param dczjid
	 * @param quesid
	 * @return
	 */
	public int findPageNum(String dczjid, String quesid) {
		List<QuesInfo> quesList = quesInfoDAO.findQuesPage(dczjid);
		int num = 0;
		if(quesList != null && quesList.size() > 0 ){
			for(QuesInfo quesEn : quesList){
				num ++;
				String quesId = quesEn.getIid()+"";
				if(StringUtil.equals(quesId, quesid)){
					break;
				}
			}
		}
		return num;
	}

	public List<QuesInfo> findTxetQuesBeforeThisQuesid(String dczjid, String quesId) {
		List<QuesInfo> quesList = this.findTextQuesAndPageQues(dczjid);
		List<QuesInfo> newQuesList = new ArrayList<QuesInfo>();
		if(quesList != null && quesList.size() > 0 ){
			for(QuesInfo quesEn : quesList){
				int quesid = quesEn.getIid();
				if(StringUtil.equals(quesId, quesid+"")){
					break;
				}
				newQuesList.add(quesEn);
			}
		}
		return newQuesList;
	}

	private List<QuesInfo> findTextQuesAndPageQues(String dczjid) {
		return quesInfoDAO.findTextQuesAndPageQues(dczjid);
	}

	public List<QuesInfo> findDQuesByDczjId(String dczjid) {
		return quesInfoDAO.findDQuesByDczjId(dczjid);
	}

	/**
	 * 查询分页总量
	 * @param dczjid
	 * @return
	 */
	public int findpageNumCount(String dczjid) {
		int num = 0;
		List<QuesInfo> quesList = quesInfoDAO.findQuesPage(dczjid);
		if(quesList != null && quesList.size() > 0 ){
			num = quesList.size();
		}
		return num;
	}

	public List<Integer> findQuesIdByDczjId(int dczjid) {
		ArrayList<Integer> questIdList = new ArrayList<Integer>();
		List<QuesInfo> quesInfoList = this.findQuesListByDczjid(dczjid+"");
		if(quesInfoList != null && quesInfoList.size()>0) {
			for(QuesInfo en : quesInfoList){
				questIdList.add(en.getIid());
			}
		}
		return questIdList;
	}

	public String[][] findMinOrder(List<Integer> quesIdList) {
		return quesInfoDAO.findMinOrder(quesIdList);
	}

	public boolean modifyOrder(Integer iid, int newOrderid) {
		return quesInfoDAO.modifyOrder(iid,newOrderid);
	}

	public int findNum(Integer dczjid) {
		return quesInfoDAO.selectnum(dczjid);
	}
	public int findScore(Integer dczjid) {
		return quesInfoDAO.selectscore(dczjid);
	}

	public List<QuesInfo> findRadioQuesByDczjId(String dczjid) {
		return quesInfoDAO.findRadioQuesByDczjId(dczjid);
	}
	
	public List<QuesInfo> findRadioQuesByDczjId1(String dczjid) {
		return quesInfoDAO.findRadioQuesByDczjId1(dczjid);
	}

	public int findSumScore(String dczjid) {
		List<QuesInfo> quesList = this.findQuesListByDczjid(dczjid);
		int sumscore = 0;
		if(quesList != null && quesList.size() > 0) {
			for(QuesInfo quesInfo : quesList) {
				if(quesInfo.getType() ==6 || quesInfo.getType() ==7) {
					sumscore += quesInfo.getQuesscore();
				}
			}
		}
		return sumscore;
	}

}
