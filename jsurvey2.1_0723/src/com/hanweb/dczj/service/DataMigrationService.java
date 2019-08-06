package com.hanweb.dczj.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.basedao.Query;
import com.hanweb.common.datasource.DataSourceSwitch;
import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.dczj.dao.AnswInfoDAO;
import com.hanweb.dczj.dao.CheckedBoxRecoDAO;
import com.hanweb.dczj.dao.ContentRecoDAO;
import com.hanweb.dczj.dao.CountDAO;
import com.hanweb.dczj.dao.DisplayConfigDAO;
import com.hanweb.dczj.dao.QuesInfoDAO;
import com.hanweb.dczj.dao.RadioRecoDAO;
import com.hanweb.dczj.dao.SettingDao;
import com.hanweb.dczj.dao.TitleInfoDAO;
import com.hanweb.dczj.dao.TotalRecoDAO;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.ContentReco;
import com.hanweb.dczj.entity.Count;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.RadioReco;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.entity.TotalReco;

public class DataMigrationService {

	@Autowired
	private SettingDao settingDao;
	@Autowired
	private TotalRecoDAO totalRecoDAO;
	@Autowired
	private ContentRecoDAO contentRecoDAO;
	@Autowired
	private TitleInfoDAO titleInfoDAO;
	@Autowired
	private QuesInfoDAO quesInfoDAO;
	@Autowired
	private AnswInfoDAO answInfoDAO;
    @Autowired
    private RadioRecoDAO radioRecoDAO;
    @Autowired
    private CheckedBoxRecoDAO checkedBoxRecoDAO;
    @Autowired
    private TitleInfoService titleInfoService;
    @Autowired
	private SettingService settingService;
    @Autowired
	private CountDAO countDAO;
    @Autowired
	private DisplayConfigDAO displayConfigDAO;
    
	/**
	 * 在线调查迁移
	 * @param out_webid
	 * @param in_webid
	 */
	public boolean surveyDateMingration(String out_webid, String in_webid) {
		if(StringUtil.isEmpty(out_webid) || StringUtil.isEmpty(in_webid)) {
			return false;
		}
		String[][] outJurvey = this.achieveOutJsurveyByWebid(out_webid);
		if(outJurvey != null && outJurvey.length > 0) {
			for(int i=0;i<outJurvey.length;i++) {
				int jsurveyid = this.achieveFormEntity(in_webid,outJurvey,i);
				if(jsurveyid > 0) {
					this.addNewConfig(jsurveyid);
					String startTime = outJurvey[i][4];
					String endTime = outJurvey[i][5];
					this.achieveSetting(jsurveyid,startTime,endTime);
					int outJsurveyid = NumberUtil.getInt(outJurvey[i][7]);
					String[][] outJurveyQues = this.achieveOutJurveyQues(outJsurveyid);
					if(outJurveyQues != null && outJurveyQues.length > 0) {
						for(int j=0;j<outJurveyQues.length;j++) {
							int outJurveyQuesid = NumberUtil.getInt(outJurveyQues[j][0]);
							int type = NumberUtil.getInt(outJurveyQues[j][2]);
							int jsurveyQuesid = this.achieveQuesEntity(jsurveyid,outJurveyQues,j);
							if(outJurveyQuesid > 0 && jsurveyQuesid > 0) {
								if(type == 1 || type == 0) {
									String[][] outJurveyAnsw = this.achieveJcmsSurveyAnswByQuesid(outJsurveyid,outJurveyQuesid);
									if(outJurveyAnsw != null && outJurveyAnsw.length > 0) {
										for(int k=0;k<outJurveyAnsw.length;k++) {
											int outJsurveyAnswid = NumberUtil.getInt(outJurveyAnsw[k][0]);
											int jsurveyAnswid = this.achieveAnswEntity(jsurveyid,jsurveyQuesid,outJurveyAnsw,k);
											if(jsurveyAnswid > 0 && outJsurveyAnswid > 0) {
												if(type == 0) {
													int radioNumber = this.achieveRadioNumber(outJsurveyid,outJurveyQuesid,outJsurveyAnswid);
													if(radioNumber > 0) {
														int pageSize = 1000;
														int b = radioNumber / pageSize;
														int pageNum = b + 1;   //分页
														for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
															String[][] radioTotal = this.achieveRadioTotal(outJsurveyid,outJurveyQuesid,outJsurveyAnswid,pageNo);
															if(radioTotal != null && radioTotal.length > 0) {
																for(int aa = 0; aa < radioTotal.length ; aa++) {
																	this.achieveRadio(jsurveyid,jsurveyQuesid,jsurveyAnswid,radioTotal,aa);
																}
															}
														}
													}
												}
                                                if(type == 1) {
													int checkBoxNumber = this.achieveCheckBoxNumber(outJsurveyid,outJurveyQuesid,outJsurveyAnswid);
													if(checkBoxNumber > 0) {
														int pageSize = 1000;
														int b = checkBoxNumber / pageSize;
														int pageNum = b + 1;   //分页
														for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
															String[][] checkBoxTotal = this.achieveCheckBoxTotal(outJsurveyid,outJurveyQuesid,outJsurveyAnswid,pageNo);
															if(checkBoxTotal != null && checkBoxTotal.length > 0) {
																for(int aa = 0; aa < checkBoxTotal.length ; aa++) {
																	this.achieveCheckBox(jsurveyid,jsurveyQuesid,jsurveyAnswid,checkBoxTotal,aa);
																}
															}
														}
													}
												
												}
											}
										}
									}
								}else if(type == 2) {
									int contentNumber = this.achieveContentNumber(outJsurveyid,outJurveyQuesid);
									if(contentNumber > 0) {
										int pageSize = 1000;
										int b = contentNumber / pageSize;
										int pageNum = b + 1;   //分页
										for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
											String[][] contentTotal = this.achieveContentTotal(outJsurveyid,outJurveyQuesid,pageNo);
											if(contentTotal != null && contentTotal.length > 0) {
												for(int aa = 0; aa < contentTotal.length ; aa++) {
													this.achieveContent(jsurveyid,jsurveyQuesid,contentTotal,aa);
												}
											}
										}
									}
								}
							}
						}
					}
					
					int totalNumber = this.achieveTotalNumber(outJsurveyid);
					if(totalNumber > 0) {
						int pageSize = 1000;
						int b = totalNumber / pageSize;
						int pageNum = b + 1;   //分页
						for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
							String[][] total = this.achieveTotal(outJsurveyid,pageNo);
							if(total != null && total.length > 0) {
								for(int aa = 0; aa < total.length ; aa++) {
									this.achieveTotal(jsurveyid,total,aa);
								}
							}
						}
					}
					this.addRedioCount(StringUtil.getString(jsurveyid));
					this.addCheckboxCount(StringUtil.getString(jsurveyid));
				}
			}
		}
		return true;
	}


	public DisplayConfig addNewConfig(int jsurveyid) {
		DisplayConfig displayConfig = new DisplayConfig();
		String cssstyle = "";
		String stylepath = BaseInfo.getRealPath()+"/resources/dczj/template/jsurvey.css";
		File file = new File(stylepath);
		cssstyle = FileUtil.readFileToString(file);
		if(StringUtil.isNotEmpty(cssstyle)) {
			cssstyle = "<style type=\"text/css\">"+cssstyle+"</style >";
		}
		displayConfig.setDczjid(jsurveyid);
		displayConfig.setChooseframe_style(0);
		displayConfig.setContentsize("20");
		displayConfig.setCssstyle(cssstyle);
		displayConfig.setIsopencontent(0);
		displayConfig.setIsprogress(0);
		displayConfig.setIstitlenumber(0);
		displayConfig.setIsshowscore(0);
		int iid = displayConfigDAO.insert(displayConfig);
		displayConfig.setIid(iid);
		return displayConfig;
	}
	
	public void addRedioCount(String dczjid) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = "";
		if(month<10) {
			if(day<10) {
				date = year+"-0"+month+"-0"+day;
			}else {
				date = year+"-0"+month+"-"+day;
			}
		}else {
			if(day<10) {
				date = year+"-"+month+"-0"+day;
			}else {
				date = year+"-"+month+"-"+day;
			}
		}
		List<Count> counts = radioRecoDAO.getCountByDczjid1(dczjid, date);		
		if(counts != null && counts.size()>0) {
			for(Count count : counts) {				
		     	countDAO.insert(count);
			}
		}
	}
	
	public void addCheckboxCount(String dczjid) {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = "";
		if(month<10) {
			if(day<10) {
				date = year+"-0"+month+"-0"+day;
			}else {
				date = year+"-0"+month+"-"+day;
			}
		}else {
			if(day<10) {
				date = year+"-"+month+"-0"+day;
			}else {
				date = year+"-"+month+"-"+day;
			}
		}
		List<Count> counts = checkedBoxRecoDAO.getCountByDczjid1(dczjid,date);
		if(counts != null && counts.size()>0) {
			for(Count count : counts) {				
			   countDAO.insert(count);
			}
		}
	}
	
	
	
	

	private void achieveTotal(int jsurveyid, String[][] total, int aa) {
		DataSourceSwitch.changeDefault();
		TotalReco totalReco = new TotalReco();
		totalReco.setDczjid(jsurveyid);
		totalReco.setMobile(total[aa][0]);
		totalReco.setCreatedate(DateUtil.stringtoDate(total[aa][1], "yyyy-MM-dd HH:mm:ss"));
		totalReco.setType(NumberUtil.getInt(total[aa][2]));
		totalReco.setIp(total[aa][3]);
		totalReco.setUnid(total[aa][4]);
		totalReco.setCode(total[aa][5]);
		totalReco.setSubmitstate(0);
		if(total[aa][3] != null && StringUtil.isNotEmpty(total[aa][3])) {
		  String address = IpUtil.getCountryByIP(SpringUtil.getRequest(), total[aa][3]);
		  totalReco.setIpaddress(address);
		}
		totalRecoDAO.insert(totalReco);
	}
	
	private void achieveTotal1(int jsurveyid, String[][] total, int aa) {
		DataSourceSwitch.changeDefault();
		TotalReco totalReco = new TotalReco();
		totalReco.setDczjid(jsurveyid);
		totalReco.setMobile(total[aa][0]);
		totalReco.setCreatedate(DateUtil.stringtoDate(total[aa][1], "yyyy-MM-dd HH:mm:ss"));
		totalReco.setType(NumberUtil.getInt(total[aa][2]));
		totalReco.setIp(total[aa][3]);
		totalReco.setUnid(total[aa][4]);
		totalReco.setCode(total[aa][5]);
		totalReco.setSubmitstate(0);
		totalReco.setSumscore(NumberUtil.getInt(total[aa][6]));		
		if(total[aa][3] != null && StringUtil.isNotEmpty(total[aa][3])) {
		  String address = IpUtil.getCountryByIP(SpringUtil.getRequest(), total[aa][3]);
		  totalReco.setIpaddress(address);
		}
		totalRecoDAO.insert(totalReco);
	}

	private String[][] achieveTotal(int outJsurveyid, int pageNo) {
		DataSourceSwitch.change("2");
		String sql = "SELECT vc_mobile,c_createdate,i_type,vc_ip,c_unid,vc_code FROM survey_totalreco WHERE i_formid =:outJsurveyid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.setPageNo(pageNo);
		query.setPageSize(1000);
		return answInfoDAO.queryForArrays(query);
	}
	private String[][] achieveTotal1(int outJsurveyid, int pageNo) {
		DataSourceSwitch.change("2");
		String sql = "SELECT vc_mobile,c_createdate,i_type,vc_ip,c_unid,vc_code,totalscore FROM survey_totalreco WHERE i_formid =:outJsurveyid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.setPageNo(pageNo);
		query.setPageSize(1000);
		return answInfoDAO.queryForArrays(query);
	}
	private int achieveTotalNumber(int outJsurveyid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT count(1) FROM survey_totalreco WHERE i_formid =:outJsurveyid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		return answInfoDAO.queryForInteger(query);
	}

	private void achieveContent(int jsurveyid, int jsurveyQuesid, String[][] contentTotal, int aa) {
		DataSourceSwitch.changeDefault();
		ContentReco contentReco = new ContentReco();
		contentReco.setDczjid(jsurveyid);
		contentReco.setQuesid(jsurveyQuesid);
		contentReco.setAnswid(0);
		contentReco.setAnswcontent(contentTotal[aa][0]);
		contentReco.setIp(contentTotal[aa][1]);
		contentReco.setSubmittime(DateUtil.stringtoDate(contentTotal[aa][2], "yyyy-MM-dd HH:mm:ss"));
		contentReco.setAudi(contentTotal[aa][3]);
		contentReco.setUnid(contentTotal[aa][4]);
		contentReco.setReplyid(contentTotal[aa][5]);
		contentRecoDAO.insert(contentReco);
	}

	private String[][] achieveContentTotal(int outJsurveyid, int outJurveyQuesid, int pageNo) {
		DataSourceSwitch.change("2");
		String sql = "SELECT vc_answcontent,c_ip,c_submittime,i_audi,c_unid,c_replyid FROM survey_contentreco WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND i_answid =:outJsurveyAnswid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		query.addParameter("outJsurveyAnswid", 0);
		query.setPageNo(pageNo);
		query.setPageSize(1000);
		return answInfoDAO.queryForArrays(query);
	}

	private int achieveContentNumber(int outJsurveyid, int outJurveyQuesid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT count(1) FROM survey_contentreco WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND i_answid =0";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		return answInfoDAO.queryForInteger(query);
	}
	
	private void achieveCheckBox(int jsurveyid, int jsurveyQuesid, int jsurveyAnswid, String[][] checkBoxTotal,
			int aa) {
		DataSourceSwitch.changeDefault();
		CheckedBoxReco checkedBoxReco = new CheckedBoxReco();
		checkedBoxReco.setDczjid(jsurveyid);
		checkedBoxReco.setQuesid(jsurveyQuesid);
		checkedBoxReco.setAnswid(jsurveyAnswid);
		checkedBoxReco.setAnswcontent(checkBoxTotal[aa][0]);
		checkedBoxReco.setIp(checkBoxTotal[aa][1]);
		checkedBoxReco.setSubmittime(DateUtil.stringtoDate(checkBoxTotal[aa][2], "yyyy-MM-dd HH:mm:ss"));
		checkedBoxReco.setAudi(checkBoxTotal[aa][3]);
		checkedBoxReco.setUnid(checkBoxTotal[aa][4]);
		checkedBoxReco.setReplyid(checkBoxTotal[aa][5]);
		checkedBoxRecoDAO.insert(checkedBoxReco);
	}
	
	private String[][] achieveCheckBoxTotal(int outJsurveyid, int outJurveyQuesid, int outJsurveyAnswid, int pageNo) {
		DataSourceSwitch.change("2");
		String sql = "SELECT vc_answcontent,c_ip,c_submittime,i_audi,c_unid,c_replyid FROM survey_checkedboxreco WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND i_answid =:outJsurveyAnswid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		query.addParameter("outJsurveyAnswid", outJsurveyAnswid);
		query.setPageNo(pageNo);
		query.setPageSize(1000);
		return answInfoDAO.queryForArrays(query);
	}

	private int achieveCheckBoxNumber(int outJsurveyid, int outJurveyQuesid, int outJsurveyAnswid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT count(1) FROM survey_checkedboxreco WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND i_answid =:outJsurveyAnswid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		query.addParameter("outJsurveyAnswid", outJsurveyAnswid);
		return answInfoDAO.queryForInteger(query);
	}

	private void achieveRadio(int jsurveyid, int jsurveyQuesid, int jsurveyAnswid, String[][] radioTotal, int aa) {
		DataSourceSwitch.changeDefault();
		RadioReco radioReco = new RadioReco();
		radioReco.setDczjid(jsurveyid);
		radioReco.setQuesid(jsurveyQuesid);
		radioReco.setAnswid(jsurveyAnswid);
		radioReco.setAnswcontent(radioTotal[aa][0]);
		radioReco.setIp(radioTotal[aa][1]);
		radioReco.setSubmittime(DateUtil.stringtoDate(radioTotal[aa][2], "yyyy-MM-dd HH:mm:ss"));
		radioReco.setAudi(radioTotal[aa][3]);
		radioReco.setUnid(radioTotal[aa][4]);
		radioReco.setReplyid(radioTotal[aa][5]);
		radioRecoDAO.insert(radioReco);
	}

	private String[][] achieveRadioTotal(int outJsurveyid, int outJurveyQuesid, int outJsurveyAnswid, int pageNo) {
		DataSourceSwitch.change("2");
		String sql = "SELECT vc_answcontent,c_ip,c_submittime,i_audi,c_unid,c_replyid FROM survey_radioreco WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND i_answid =:outJsurveyAnswid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		query.addParameter("outJsurveyAnswid", outJsurveyAnswid);
		query.setPageNo(pageNo);
		query.setPageSize(1000);
		return answInfoDAO.queryForArrays(query);
	}

	private int achieveRadioNumber(int outJsurveyid, int outJurveyQuesid, int outJsurveyAnswid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT count(1) FROM survey_radioreco WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND i_answid =:outJsurveyAnswid";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		query.addParameter("outJsurveyAnswid", outJsurveyAnswid);
		return answInfoDAO.queryForInteger(query);
	}

	private int achieveAnswEntity(int jsurveyid, int jsurveyQuesid, String[][] outJurveyAnsw, int k) {
		DataSourceSwitch.changeDefault();
		AnswInfo answInfo = new AnswInfo();
//		answInfo.setOrderid(NumberUtil.getInt(outJurveyAnsw[k][1]));
		if(outJurveyAnsw[k][1]!=null && StringUtil.isNotEmpty(outJurveyAnsw[k][1])) {
			answInfo.setOrderid(NumberUtil.getInt(outJurveyAnsw[k][1]));	
		}else {
		    answInfo.setOrderid(0);	
		}
		answInfo.setDczjid(jsurveyid);
		answInfo.setQuesid(jsurveyQuesid);
		answInfo.setAnswname(outJurveyAnsw[k][4]);
		answInfo.setBasepoint(NumberUtil.getInt(outJurveyAnsw[k][5]));
		answInfo.setAllowfillinair(NumberUtil.getInt(outJurveyAnsw[k][6]));
		answInfo.setState(0);
		return answInfoDAO.insert(answInfo);
	}
	
	private int achieveAnswEntity1(int jsurveyid, int jsurveyQuesid, String[][] outJurveyAnsw, int k) {
		DataSourceSwitch.changeDefault();
		AnswInfo answInfo = new AnswInfo();
		if(outJurveyAnsw[k][1]!=null && StringUtil.isNotEmpty(outJurveyAnsw[k][1])) {
			answInfo.setOrderid(NumberUtil.getInt(outJurveyAnsw[k][1]));	
		}else {
		    answInfo.setOrderid(0);	
		}
				
		answInfo.setDczjid(jsurveyid);
		answInfo.setQuesid(jsurveyQuesid);
		answInfo.setAnswname(outJurveyAnsw[k][4]);
		answInfo.setBasepoint(NumberUtil.getInt(outJurveyAnsw[k][5]));
		answInfo.setAllowfillinair(NumberUtil.getInt(outJurveyAnsw[k][6]));
		answInfo.setState(0);
		answInfo.setIsright(outJurveyAnsw[k][7]);
		return answInfoDAO.insert(answInfo);
	}

	private String[][] achieveJcmsSurveyAnswByQuesid(int outJsurveyid, int outJurveyQuesid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT i_id,i_orderid,i_formid,i_quesid,vc_name,i_basepoint,allowfillinair FROM survey_answ WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND vc_state = 0";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		return answInfoDAO.queryForArrays(query);
	}
	private String[][] achieveJcmsSurveyAnswByQuesid1(int outJsurveyid, int outJurveyQuesid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT i_id,i_orderid,i_formid,i_quesid,vc_name,i_basepoint,allowfillinair,isright FROM survey_answ WHERE i_formid =:outJsurveyid AND i_quesid =:outJurveyQuesid AND vc_state = 0";
		Query query = answInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		query.addParameter("outJurveyQuesid", outJurveyQuesid);
		return answInfoDAO.queryForArrays(query);
	}
	private int achieveQuesEntity(int jsurveyid, String[][] outJurveyQues, int j) {
		DataSourceSwitch.changeDefault();
		QuesInfo quesInfo = new QuesInfo();
		quesInfo.setQuesname(outJurveyQues[j][1]);
		quesInfo.setDczjid(jsurveyid);
		quesInfo.setType(NumberUtil.getInt(outJurveyQues[j][2]));
		quesInfo.setCol(NumberUtil.getInt(outJurveyQues[j][3]));
		quesInfo.setIsmustfill(NumberUtil.getInt(outJurveyQues[j][4]));
		quesInfo.setMinselect(NumberUtil.getInt(outJurveyQues[j][5]));
		quesInfo.setMaxselect(NumberUtil.getInt(outJurveyQues[j][6]));
		quesInfo.setTextinputwidth(NumberUtil.getInt(outJurveyQues[j][7]));
		quesInfo.setTextinputheight(NumberUtil.getInt(outJurveyQues[j][8]));
		quesInfo.setContent(outJurveyQues[j][9]);
//		quesInfo.setOrderid(NumberUtil.getInt(outJurveyQues[j][10]));
		if(outJurveyQues[j][10]!=null && StringUtil.isNotEmpty(outJurveyQues[j][10])) {
			quesInfo.setOrderid(NumberUtil.getInt(outJurveyQues[j][10]));	
		}else {
			quesInfo.setOrderid(0);	
		}
		quesInfo.setState(0);
		quesInfo.setDczjtype(0);
		quesInfo.setShowpublish(0);
		quesInfo.setValidaterules(0);
		return quesInfoDAO.insert(quesInfo);
	}
	private int achieveQuesEntity1(int jsurveyid, String[][] outJurveyQues, int j) {
		DataSourceSwitch.changeDefault();
		QuesInfo quesInfo = new QuesInfo();
		quesInfo.setQuesname(outJurveyQues[j][1]);
		quesInfo.setDczjid(jsurveyid);
		quesInfo.setType(NumberUtil.getInt(outJurveyQues[j][2]));
		quesInfo.setCol(NumberUtil.getInt(outJurveyQues[j][3]));
		quesInfo.setIsmustfill(NumberUtil.getInt(outJurveyQues[j][4]));
		quesInfo.setMinselect(NumberUtil.getInt(outJurveyQues[j][5]));
		quesInfo.setMaxselect(NumberUtil.getInt(outJurveyQues[j][6]));
		quesInfo.setTextinputwidth(NumberUtil.getInt(outJurveyQues[j][7]));
		quesInfo.setTextinputheight(NumberUtil.getInt(outJurveyQues[j][8]));
		quesInfo.setContent(outJurveyQues[j][9]);
		/*if(outJurveyQues[j][10]==null || outJurveyQues[j][10] =="" ) {
			quesInfo.setOrderid(0);	
		}else {
			quesInfo.setOrderid(NumberUtil.getInt(outJurveyQues[j][10]));	
		}*/
		if(outJurveyQues[j][10]!=null && StringUtil.isNotEmpty(outJurveyQues[j][10])) {
			quesInfo.setOrderid(NumberUtil.getInt(outJurveyQues[j][10]));	
		}else {
			quesInfo.setOrderid(0);	
		}
	
		quesInfo.setState(0);
		quesInfo.setDczjtype(0);
		quesInfo.setShowpublish(1);
		quesInfo.setValidaterules(0);
		quesInfo.setQuesscore(NumberUtil.getFloat(outJurveyQues[j][11]));
		return quesInfoDAO.insert(quesInfo);
	}
	private String[][] achieveOutJurveyQues(int outJsurveyid) {
		DataSourceSwitch.change("2");
		String sql = "SElECT i_id,vc_name,i_type,i_col,b_mustfill,i_min,i_max,i_textinputwidth,i_textinputheight,vc_content,i_orderid FROM survey_ques WHERE i_formid =:outJsurveyid AND vc_state = 0";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		return titleInfoDAO.queryForArrays(query);
	}

	private String[][] achieveOutJurveyQues1(int outJsurveyid) {
		DataSourceSwitch.change("2");
		String sql = "SElECT i_id,vc_name,i_type,i_col,b_mustfill,i_min,i_max,i_textinputwidth,i_textinputheight,vc_content,i_orderid,quesscore FROM survey_ques WHERE i_formid =:outJsurveyid AND vc_state = 0";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("outJsurveyid", outJsurveyid);
		return titleInfoDAO.queryForArrays(query);
	}
	
	private Integer achieveSetting(int jsurveyid, String startTime, String endTime) {
		DataSourceSwitch.changeDefault();
		Dczj_Setting setting = new Dczj_Setting();
		setting.setDczjid(jsurveyid);
		setting.setIsstart(1);
		setting.setStarttime(DateUtil.stringtoDate(startTime, "yyyy-MM-dd HH:mm:ss"));
		setting.setIsend(1);
		setting.setEndtime(DateUtil.stringtoDate(endTime, "yyyy-MM-dd HH:mm:ss"));
		return settingDao.insert(setting);
	}

	private int achieveFormEntity(String in_webid, String[][] outJurvey, int i) {
		DataSourceSwitch.changeDefault();
		TitleInfo titleInfo = new TitleInfo();
		titleInfo.setWebid(NumberUtil.getInt(in_webid));
		titleInfo.setUsername(outJurvey[i][0]);
		titleInfo.setCreatetime(DateUtil.stringtoDate(outJurvey[i][1], "yyyy-MM-dd HH:mm:ss"));
		titleInfo.setTitlename(outJurvey[i][2]);
		titleInfo.setState(NumberUtil.getInt(outJurvey[i][6]));
		titleInfo.setIspublish(0);
		titleInfo.setIsdelete(0);
		titleInfo.setType(0);
		titleInfo.setOrderid(0);
		return titleInfoDAO.insert(titleInfo);
	}
	
	private int achieveFormEntity1(String in_webid, String[][] outJurvey, int i) {
		DataSourceSwitch.changeDefault();
		TitleInfo titleInfo = new TitleInfo();
		titleInfo.setWebid(NumberUtil.getInt(in_webid));
		titleInfo.setUsername(outJurvey[i][0]);
		titleInfo.setCreatetime(DateUtil.stringtoDate(outJurvey[i][1], "yyyy-MM-dd HH:mm:ss"));
		titleInfo.setTitlename(outJurvey[i][2]);
		titleInfo.setState(NumberUtil.getInt(outJurvey[i][6]));
		titleInfo.setIspublish(0);
		titleInfo.setIsdelete(0);
		titleInfo.setType(3);
		titleInfo.setOrderid(0);
		return titleInfoDAO.insert(titleInfo);
	}

	private String[][] achieveOutJsurveyByWebid(String out_webid) {
		DataSourceSwitch.change("2");
		String sql = "SElECT vc_username,c_createtime,vc_name,b_showresult,c_starttime,c_endtime,i_state,i_id FROM survey_form WHERE i_cateid =:out_webid AND vc_state = 0 AND surveytype = 0";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("out_webid", out_webid);
		return titleInfoDAO.queryForArrays(query);
	}
	
	private String[][] achieveOutJsurveyByWebid1(String out_webid) {
		DataSourceSwitch.change("2");
		String sql = "SElECT vc_username,c_createtime,vc_name,b_showresult,c_starttime,c_endtime,i_state,i_id FROM survey_form WHERE i_cateid =:out_webid AND vc_state = 0 AND surveytype = 1";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("out_webid", out_webid);
		return titleInfoDAO.queryForArrays(query);
	}

	/**
	 * 意见征集迁移
	 * @param out_webid
	 * @param in_webid
	 */
	public boolean ideaDateMingration(String out_webid, String in_webid) {
		if(StringUtil.isEmpty(out_webid) || StringUtil.isEmpty(in_webid)) {
			return false;
		}
		System.out.println("===================征集开始======================");
		String[][] oldIdea = this.achieveJcmsIdeaByWebid(out_webid);
		if(oldIdea != null && oldIdea.length > 0) {
			for(int i=0;i<oldIdea.length;i++) {
				String jcmsTopid = oldIdea[i][0];
				System.out.println("===================jcms征集ID："+jcmsTopid+"======================");
				int jsurveytopicid = this.achieveTopicEntity(in_webid,oldIdea,i);
				String starttime = oldIdea[i][6];
				String endtime = oldIdea[i][7];
				System.out.println("===================jsurvey征集ID："+jsurveytopicid+"======================");
				if(jsurveytopicid <= 0) {
					System.out.println("失败的信息： " +oldIdea[i][0]);
				}else {
					this.addNewConfig(jsurveytopicid);
					int jsurveyquesid = this.achieveIdeaQues(jsurveytopicid);
					this.achieveIdeaSetting(jsurveytopicid,starttime,endtime);
					this.ideaRevertDateMingration(jcmsTopid,jsurveytopicid+"",jsurveyquesid);
				}
			}
		}
		System.out.println("===================征集结束======================");
		return true;
	}

	private Integer achieveIdeaQues(int jsurveytopicid) {
		DataSourceSwitch.changeDefault();
		QuesInfo quesInfo = new QuesInfo();
		quesInfo.setDczjid(jsurveytopicid);
		quesInfo.setType(2);
		quesInfo.setQuesname("请填写建议");
		quesInfo.setCol(1);
		quesInfo.setIsmustfill(1);
		quesInfo.setMinselect(1);
		quesInfo.setMaxselect(3);
		quesInfo.setTextinputwidth(860);
		quesInfo.setTextinputwidth(184);
		quesInfo.setState(0);
		quesInfo.setDczjtype(0);
		quesInfo.setShowpublish(1);
		quesInfo.setValidaterules(0);
		return quesInfoDAO.insert(quesInfo);
	}

	private boolean ideaRevertDateMingration(String jcmsTopid, String jsurveyTopid, int jsurveyquesid) {
		if(StringUtil.isEmpty(jcmsTopid) || StringUtil.isEmpty(jsurveyTopid)) {
			return false;
		}
		int num = this.achieveJcmsIdeaRevertNum(jcmsTopid);
		System.out.println("===================征集数量:"+num+"======================");
		if(num > 0) {  
			int pageNum = 0;
			int pageSize = 1000;
			int b = num / pageSize;
			pageNum = b + 1;   //分页
			for(int ii=1;ii<=pageNum;ii++) {
				String[][] jcmsIdeaRevert = this.achieveJcmsIdeaRevertByTopid(jcmsTopid,ii,pageSize);
				if(jcmsIdeaRevert != null && jcmsIdeaRevert.length > 0) {
					DataSourceSwitch.changeDefault();
					for(int jj = 0;jj< jcmsIdeaRevert.length;jj++) {
						String createdate = jcmsIdeaRevert[jj][3] + " 00:00:00";
						String ip = jcmsIdeaRevert[jj][4];
						String content = jcmsIdeaRevert[jj][5];
						String unid = jcmsIdeaRevert[jj][7];
						String c_result = jcmsIdeaRevert[jj][6];
						String reply = jcmsIdeaRevert[jj][8];
						Date date = DateUtil.stringtoDate(createdate, "yyyy-MM-dd HH:mm:ss");
						this.achieveTotalReco(jsurveyTopid,date,ip,unid,1);
						this.achieveContentReco(jsurveyTopid,jsurveyquesid,date,content,unid,c_result,ip,reply);
					}
				}
			}
		}
		return true;
	}

	private void achieveContentReco(String jsurveyTopid, int jsurveyquesid, Date date, String content,
			String unid, String c_result, String ip,String reply) {
		ContentReco contentReco = new ContentReco();
		contentReco.setDczjid(NumberUtil.getInt(jsurveyTopid));
		contentReco.setQuesid(jsurveyquesid);
		contentReco.setAnswid(0);
		contentReco.setAnswcontent(content);
		contentReco.setIp(ip);
		contentReco.setSubmittime(date);
		if("3".equals(c_result)) {
		  contentReco.setAudi("1");
		}else if("1".equals(c_result)) {
		  contentReco.setAudi("2");
		}else {
		  contentReco.setAudi(c_result);
		}
//		contentReco.setAudi(c_result);
		contentReco.setUnid(unid);
		contentReco.setReplyid(StringUtil.getUUIDString());
		contentReco.setReplycontent(reply);
		contentRecoDAO.insert(contentReco);
	}

	private void achieveTotalReco(String jsurveyTopid, Date date, String ip, String unid, int type) {
		TotalReco totalReco = new TotalReco();
		totalReco.setDczjid(NumberUtil.getInt(jsurveyTopid));
		totalReco.setIp(ip);
		totalReco.setType(type);
		totalReco.setUnid(unid);
		totalReco.setSubmitstate(0);
		totalReco.setCreatedate(date);
		if(ip != null && StringUtil.isNotEmpty(ip)) {
		  String address = IpUtil.getCountryByIP(SpringUtil.getRequest(),ip);
		  totalReco.setIpaddress(address);
		}
		totalRecoDAO.insert(totalReco);
	}

	private String[][] achieveJcmsIdeaRevertByTopid(String jcmsTopid, int pageNum, int pageSize) {
		DataSourceSwitch.change("2");
		String sql = "SElECT i_id,i_topicid,vc_sendname,dt_senddate,vc_ip,vc_content,c_result,c_uuid,vc_reply FROM idea_revert WHERE i_topicid =:jcmsTopid";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("jcmsTopid", jcmsTopid);
		query.setPageNo(pageNum);
		query.setPageSize(pageSize);
		return titleInfoDAO.queryForArrays(query);
	}

	private int achieveJcmsIdeaRevertNum(String jcmsTopid) {
		DataSourceSwitch.change("2");
		String sql = "SElECT count(1) FROM idea_revert WHERE i_topicid =:jcmsTopid";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("jcmsTopid", jcmsTopid);
		return titleInfoDAO.queryForInteger(query);
	}

	private Integer achieveIdeaSetting(int jsurveytopicid, String starttime, String endtime) {
		DataSourceSwitch.changeDefault();
		Dczj_Setting setting = new Dczj_Setting();
		setting.setDczjid(jsurveytopicid);
		setting.setIsstart(1);
		setting.setStarttime(DateUtil.stringtoDate(starttime+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		setting.setIsend(1);
		setting.setEndtime(DateUtil.stringtoDate(endtime+" 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		return settingDao.insert(setting);
	}

	private String[][] achieveJcmsIdeaByWebid(String out_webid) {
		DataSourceSwitch.change("2");
		String sql = "SELECT i_id,i_cateid,vc_title,vc_content,vc_username,dt_createdate,dt_begindate,dt_enddate,i_state,vc_department FROM idea_topic WHERE i_cateid =:jcmsWebid AND vc_state = 0";
		Query query = titleInfoDAO.createQuery(sql);
		query.addParameter("jcmsWebid", out_webid);
		return titleInfoDAO.queryForArrays(query);
	}

	private int achieveTopicEntity(String jsurveyWebid, String[][] jcmsIdea, int i) {
		DataSourceSwitch.changeDefault();
		TitleInfo infoEn = new TitleInfo();
		infoEn.setWebid(NumberUtil.getInt(jsurveyWebid));
		infoEn.setUsername(jcmsIdea[i][4]);
		String createtime = jcmsIdea[i][5] + " 00:00:00";
		Date date = DateUtil.stringtoDate(createtime, "yyyy-MM-dd HH:mm:ss");
		infoEn.setCreatetime(date);
		infoEn.setTitlename(jcmsIdea[i][2]);
		infoEn.setState(NumberUtil.getInt(jcmsIdea[i][8]));
		infoEn.setIspublish(0);
		infoEn.setIsdelete(0);
		infoEn.setType(1);
		infoEn.setOrderid(0);
		return titleInfoDAO.insert(infoEn);
	}
	
	public boolean refreshData(String Webid) {
		if(StringUtil.isEmpty(Webid)) {
			return false;
		}
		List<TitleInfo> titleInfoList = titleInfoService.findTitleByWebId(Webid);
		if(titleInfoList != null && titleInfoList.size() > 0) {
			for(TitleInfo infoEn : titleInfoList) {
				int jsurveyid = infoEn.getIid();
				Date creatdate = infoEn.getCreatetime();
				Date enddate = null;
				Dczj_Setting setting = settingService.getEntityBydczjid(StringUtil.getString(jsurveyid));
				if(setting != null && setting.getIsend() == 1) {
					enddate = setting.getEndtime();
				}else {
					enddate = DateUtil.stringtoDate(DateUtil.getCurrDateTime(), "yyyy-MM-dd HH:mm:ss");
				}
				long daydiff = DateUtil.dayDiff(creatdate, enddate);
				if(daydiff >= 0) {
					for(int i = 0;i<=daydiff;i++) {
						Date date = DateUtil.nextDay(creatdate, i);
						String time = DateUtil.dateToString(date, "yyyy-MM-dd");
						
						List<Count> radiocounts = radioRecoDAO.getCountByDczjid(StringUtil.getString(jsurveyid), time);
						if(radiocounts != null && radiocounts.size()>0) {
							for(Count count : radiocounts) {
								if(countDAO.getCountnNum(count.getCountdate(), count.getDczjid(), count.getQuesid(), count.getAnswid()) > 0) {
									countDAO.updateCount(count);
								}else {
									countDAO.insert(count);
								}
							}
						}
						
						List<Count> checkedcounts = checkedBoxRecoDAO.getCountByDczjid(StringUtil.getString(jsurveyid), time);
						if(checkedcounts != null && checkedcounts.size()>0) {
							for(Count count : checkedcounts) {
								if(countDAO.getCountnNum(count.getCountdate(), count.getDczjid(), count.getQuesid(), count.getAnswid()) > 0) {
									countDAO.updateCount(count);
								}else {
									countDAO.insert(count);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 测评迁移
	 * @param out_webid
	 * @param in_webid
	 * @return
	 */
	public boolean evaluationDateMingration(String out_webid, String in_webid) {
		if(StringUtil.isEmpty(out_webid) || StringUtil.isEmpty(in_webid)) {
			return false;
		}
		String[][] outJurvey = this.achieveOutJsurveyByWebid1(out_webid);
		if(outJurvey != null && outJurvey.length > 0) {
			for(int i=0;i<outJurvey.length;i++) {
				int jsurveyid = this.achieveFormEntity1(in_webid,outJurvey,i);
				if(jsurveyid > 0) {
					this.addNewConfig(jsurveyid);
					String startTime = outJurvey[i][4];
					String endTime = outJurvey[i][5];
					this.achieveSetting(jsurveyid,startTime,endTime);
					int outJsurveyid = NumberUtil.getInt(outJurvey[i][7]);
					String[][] outJurveyQues = this.achieveOutJurveyQues1(outJsurveyid);					
					if(outJurveyQues != null && outJurveyQues.length > 0) {
						for(int j=0;j<outJurveyQues.length;j++) {
							int outJurveyQuesid = NumberUtil.getInt(outJurveyQues[j][0]);
							int type = NumberUtil.getInt(outJurveyQues[j][2]);
							int jsurveyQuesid = this.achieveQuesEntity1(jsurveyid,outJurveyQues,j);							
							if(outJurveyQuesid > 0 && jsurveyQuesid > 0) {
								if(type == 1 || type == 0 || type == 6 || type == 7) {
									String[][] outJurveyAnsw = this.achieveJcmsSurveyAnswByQuesid1(outJsurveyid,outJurveyQuesid);									
									if(outJurveyAnsw != null && outJurveyAnsw.length > 0) {
										for(int k=0;k<outJurveyAnsw.length;k++) {
											int outJsurveyAnswid = NumberUtil.getInt(outJurveyAnsw[k][0]);
											int jsurveyAnswid = this.achieveAnswEntity1(jsurveyid,jsurveyQuesid,outJurveyAnsw,k);											
											if(jsurveyAnswid > 0 && outJsurveyAnswid > 0) {
												if(type == 0 || type==6) {
													int radioNumber = this.achieveRadioNumber(outJsurveyid,outJurveyQuesid,outJsurveyAnswid);												
													if(radioNumber > 0) {
														int pageSize = 1000;
														int b = radioNumber / pageSize;
														int pageNum = b + 1;   //分页
														for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
															String[][] radioTotal = this.achieveRadioTotal(outJsurveyid,outJurveyQuesid,outJsurveyAnswid,pageNo);
															if(radioTotal != null && radioTotal.length > 0) {
																for(int aa = 0; aa < radioTotal.length ; aa++) {
																	this.achieveRadio(jsurveyid,jsurveyQuesid,jsurveyAnswid,radioTotal,aa);														
																}
															}
														}
													}
												}
                                                if(type == 1 || type==7) {
													int checkBoxNumber = this.achieveCheckBoxNumber(outJsurveyid,outJurveyQuesid,outJsurveyAnswid);
													if(checkBoxNumber > 0) {
														int pageSize = 1000;
														int b = checkBoxNumber / pageSize;
														int pageNum = b + 1;   //分页
														for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
															String[][] checkBoxTotal = this.achieveCheckBoxTotal(outJsurveyid,outJurveyQuesid,outJsurveyAnswid,pageNo);
															if(checkBoxTotal != null && checkBoxTotal.length > 0) {
																for(int aa = 0; aa < checkBoxTotal.length ; aa++) {
																	this.achieveCheckBox(jsurveyid,jsurveyQuesid,jsurveyAnswid,checkBoxTotal,aa);
																}
															}
														}
													}
												
												}
											}
										}
									}
								}else if(type == 2) {
									int contentNumber = this.achieveContentNumber(outJsurveyid,outJurveyQuesid);
									if(contentNumber > 0) {
										int pageSize = 1000;
										int b = contentNumber / pageSize;
										int pageNum = b + 1;   //分页
										for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
											String[][] contentTotal = this.achieveContentTotal(outJsurveyid,outJurveyQuesid,pageNo);
											if(contentTotal != null && contentTotal.length > 0) {
												for(int aa = 0; aa < contentTotal.length ; aa++) {
													this.achieveContent(jsurveyid,jsurveyQuesid,contentTotal,aa);
												}
											}
										}
									}
								}
							}
						}
					}
					
					int totalNumber = this.achieveTotalNumber(outJsurveyid);
					if(totalNumber > 0) {
						int pageSize = 1000;
						int b = totalNumber / pageSize;
						int pageNum = b + 1;   //分页
						for(int pageNo = 1; pageNo <= pageNum; pageNo++) {
							String[][] total = this.achieveTotal1(outJsurveyid,pageNo);
							
							if(total != null && total.length > 0) {
								for(int aa = 0; aa < total.length ; aa++) {
									this.achieveTotal1(jsurveyid,total,aa);
									
								}
							}
						}
					}
					
					this.addRedioCount(StringUtil.getString(jsurveyid));
					this.addCheckboxCount(StringUtil.getString(jsurveyid));
				}
			}
		}
		return true;
	}

}
