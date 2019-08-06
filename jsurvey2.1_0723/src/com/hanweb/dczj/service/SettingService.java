package com.hanweb.dczj.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.dao.SettingDao;
import com.hanweb.dczj.dao.ThanksSettingDAO;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.ThanksSetting;

public class SettingService {

	@Autowired
	SettingDao settingDao;
	@Autowired
	ThanksSettingDAO thanksSettingDAO;
	@Autowired
	ThanksSettingService thanksSettingService;
	
	/**
	 * 更新抽奖状态
	 * @param dczjid
	 * @param isPrize
	 * @return
	 */
	public Integer modifyIsPrize(String dczjid, Integer isprize) {
		return settingDao.modifyIsPrize(dczjid, isprize);
	}
	
	public Dczj_Setting getEntityBydczjid(String dczjid){
		return settingDao.getEntityBydczjid(dczjid);
	}
	
	public Integer modify(String name, String value, Integer dczjid, Integer type) {
		return settingDao.modifyColumnById(name, value, dczjid, type);
	}
	
	public Integer insertEntity(String dczjid) {
		Dczj_Setting setting = new Dczj_Setting();
		setting.setDczjid(NumberUtil.getInt(dczjid));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		setting.setEndtime(calendar.getTime());
		return settingDao.insert(setting);
	}
	
	/**
	 * 获取PC端感谢页面DIV
	 * @param dczjid
	 * @return
	 */
	public String initPCThanksDiv(String dczjid) {
		StringBuffer div = new StringBuffer();
		Dczj_Setting setting = settingDao.getEntityBydczjid(dczjid);
		if(setting == null) {
			insertEntity(dczjid);
			setting = settingDao.getEntityBydczjid(dczjid);
		}
		if(setting.getIsjump()==0) {
			//感谢信息
			ThanksSetting thanksSetting = thanksSettingDAO.getSettingBydczjid(NumberUtil.getInt(dczjid));
			if(thanksSetting == null) {
				thanksSettingService.init(dczjid);
				thanksSetting = thanksSettingDAO.getSettingBydczjid(NumberUtil.getInt(dczjid));
			}
			String jumpbtn = thanksSetting.getJumpbtn();
			int isjump ;
			if(thanksSetting.getIsjump() == null) {
				isjump = 0;
			}else {
				isjump = thanksSetting.getIsjump();
			}
			if(StringUtil.isEmpty(jumpbtn)) {
				jumpbtn = "";
			}
			div.append("<style>" + 
					"#bgcs {" +  
					"	width: 100%;" + 
					"	height: 100%;" + 
					"	top: 0px;" + 
					"	left: 0px;" + 
					"	position: fixed;" + 
					"	filter: Alpha(opacity = 50);" + 
					"	opacity: 0.2;" + 
					"	background: #000000;" + 
					"	display: none;" + 
					"}" + 
					"" + 
					"#popboxcs {" + 
					"	position:fixed;" + 
					"	width:355px;" + 
					"	height:330px;" + 
					"	left:48%;" + 
					"	top:50%;" + 
					"	margin:-150px 0 0 -150px;" + 
					"	display:none;" + 
					"	padding:0px;" + 
					"	background:#fff;" + 
					"	border-radius:5px;" + 
					"	z-index:100;" + 
					"}" + 
					"</style>" + 
					"<div id=\"bgcs\"></div>");
			div.append("<div id=\"popboxcs\">");
			div.append("<span style=\"position:absolute;top:6px;right:6px;display:inline-block;width:28px;height:28px;cursor:pointer;\">");
			div.append("<img src=\"../../resources/dczj/images/jumpdiv/close.png\" width=\"27\" height=\"26\" onclick=\"closeResultWind();\"/>");
			div.append("</span>");
			div.append("<div style=\"padding:30px 36% ;\">");
			div.append("<img src=\"../../resources/dczj/images/jumpdiv/gou.png\" width=\"94\" height=\"94\" />");
			div.append("</div>");
			div.append("<div id=\"bgscore\" style=\"padding:0px 19% 10px;font-size:16px;font-weight:bold;text-align:center;\">");
			div.append(thanksSetting.getThankscontent());
			div.append("</div>");
			div.append("<div id=\"bgdeatail\" style=\"padding:0px 19% 10px;font-size:14px;text-align:center;\"></div>");
			div.append("<div style=\"padding:5px 27% 28px;cursor:pointer;color:rgb(14,153,210);\">");
			div.append("<div id=\"pop_time\" style=\"display:none;\">即将跳转倒计时<span id = \"endsecond\" style =\"color:red;\">10</span>秒</div>");
			if(isjump == 0) {
				if(jumpbtn!=null) {
					if(jumpbtn.indexOf("0")>=0) {
						//0:查看结果
						div.append("<div id=\"pop_result\" style=\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;color:white;"
						+ "background-color:rgb(14,153,210)\" onclick=\"showResult('../../jsurvey/questionnaire/result_"+dczjid+".html',"+dczjid+");\">前往查看调查结果</div>");
					}
					if(jumpbtn.indexOf("1")>=0) {
						//1.参与抽奖
						div.append("<div id=\"pop_draw\" style=\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;color:white;"
								+ "background-color:rgb(14,153,210);margin-top:5px;\" onclick=\"showDraw("+dczjid+");\">前往抽奖</div>");
					}
//					if(jumpbtn.indexOf("2")>=0) {
//						
//					}
				}
			}else if(isjump == 1) {
				//2.前往其他页面
				div.append("<div id=\"pop_draw\" style=\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;color:white;"
						+ "background-color:rgb(14,153,210);margin-top:5px;text-align:center;\" onclick=\"location.href=\'"+thanksSetting.getJumpurl()+"\'\">"+thanksSetting.getBtnname()+"</div>");
			}
			div.append("</div></div>");
		}else {
			//指定页面
			div.append("<style>" + 
					"#bgcs {" + 
					"	width: 100%;" + 
					"	height: 100%;" + 
					"	top: 0px;" + 
					"	left: 0px;" + 
					"	position: fixed;" + 
					"	filter: Alpha(opacity = 50);" + 
					"	opacity: 0.2;" + 
					"	background: #000000;" + 
					"	display: none;" + 
					"}" + 
					"" + 
					"#popboxcs {" + 
					"	position:fixed;" + 
					"	width:355px;" + 
					"	height:300px;" + 
					"	left:48%;" + 
					"	top:50%;" + 
					"	margin:-150px 0 0 -150px;" + 
					"	display:none;" + 
					"	padding:0px;" + 
					"	background:#fff;" + 
					"	border-radius:5px;" + 
					"	z-index:100;" + 
					"}" + 
					"</style>" + 
					"<div id=\"bgcs\"></div>");
			div.append("<div id=\"popboxcs\">");
			div.append("<span style=\"position:absolute;top:6px;right:6px;display:inline-block;width:28px;height:28px;cursor:pointer;\">");
			div.append("<img src=\"../../resources/dczj/images/jumpdiv/close.png\" width=\"27\" height=\"26\" onclick=\"closeResultWind();\"/>");
			div.append("</span>");
			div.append("<div style=\"padding:30px 36% ;\">");
			div.append("<img src=\"../../resources/dczj/images/jumpdiv/gou.png\" width=\"94\" height=\"94\" />");
			div.append("</div>");
			div.append("<div style=\"padding:0px 19% 20px;font-size:18px;font-weight:bold;text-align:center;\">");
			div.append("提交成功，感谢您的参与！");
			div.append("</div>");
			div.append("<div style=\"padding:5px 27% 28px;cursor:pointer;color:rgb(14,153,210);\">");
			div.append("<div id=\"pop_time\" style=\"display:none;\">即将跳转倒计时<span id = \"endsecond\" style =\"color:red;\">10</span>秒</div>");
			div.append("<div id=\"\" style=\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;color:white;"
					+ "background-color:rgb(14,153,210);margin-top:5px;\" onclick=\"location.href =\'"+setting.getJumpurl()+"\'\">跳转页面</div>");
			div.append("</div></div>");
		}
		return div.toString();
	}
	
	/**
	 * 获取手机端感谢页面DIV
	 * @param dczjid
	 * @return
	 */
	public String initPhoneThanksDiv(String dczjid) {
		StringBuffer div = new StringBuffer();
		Dczj_Setting setting = settingDao.getEntityBydczjid(dczjid);
		if(setting == null) {
			insertEntity(dczjid);
			setting = settingDao.getEntityBydczjid(dczjid);
		}
		if(setting.getIsjump()==0) {
			//感谢信息
			ThanksSetting thanksSetting = thanksSettingDAO.getSettingBydczjid(NumberUtil.getInt(dczjid));
			if(thanksSetting == null) {
				thanksSettingService.init(dczjid);
				thanksSetting = thanksSettingDAO.getSettingBydczjid(NumberUtil.getInt(dczjid));
			}
			String jumpbtn = thanksSetting.getJumpbtn();
			if(StringUtil.isEmpty(jumpbtn)) {
				jumpbtn = "";
			}
			div.append("<style>" + 
					"#bgcs {" + 
					"	width: 100%;" + 
					"	height: 100%;" + 
					"	top: 0px;" + 
					"	left: 0px;" + 
					"	position: fixed;" + 
					"	filter: Alpha(opacity = 50);" + 
					"	opacity: 0.2;" + 
					"	background: #000000;" + 
					"	display: none;" + 
					"}" + 
					"" + 
					"#popboxcs {" + 
					"	position:fixed;" + 
					"	width:300px; " + 
					"	height:300px;" + 
					"	left:50%;" + 
					"	top:50%;" + 
					"	margin:-150px 0 0 -150px;" + 
					"	display:none;" + 
					"	padding:0px;" + 
					"	background:#fff;" + 
					"	border-radius:5px;" + 
					"	z-index:100;" + 
					"}" + 
					"</style>" + 
					"<div id=\"bgcs\"></div>");
			div.append("<div id=\"popboxcs\">" + 
					"   <span style=\"position:absolute;top:6px;right:6px;display:inline-block;width:28px;height:28px;cursor:pointer;\"><img src=\"../../resources/dczj/images/jumpdiv/close.png\" width=\"27\" height=\"26\" onclick=\"closeResultWind();\"/></span>" + 
					"   <div style=\"padding:30px 36% ;\"><img src=\"../../resources/dczj/images/jumpdiv/gou.png\" width=\"94\" height=\"94\" /></div>" + 
					"    <div style=\"padding:0px 14% 15px;font-size:18px;font-weight:bold;text-align:center;\">提交成功，谢谢您的参与！</div>" + 
					"   <div id=\"bgdeatail\" style=\"padding:0px 19% 10px;font-size:14px;text-align:center;\"></div>" +
					"    <div style=\"padding:10px 20% 10px;color:rgb(14,153,210);\" >");
			div.append("<div id=\"pop_time\" style=\"display:none;font-size:18px;text-align:center;\">即将跳转倒计时<span id = \"endsecond\" style =\"color:red;\">10</span>秒</div>");
			if(jumpbtn!=null) {
				if(jumpbtn.indexOf("0")>=0) {
					div.append("<div id=\"pop_result\" style =\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;cursor:pointer;font-size:18px;color:white;background-color:rgb(14,153,210)\" "
							+ "onclick=\"showPhoneResult('../../jsurvey/questionnaire/phoneresult_"+dczjid+".html',"+dczjid+");\">前往查看调查结果</div>");
				}
				if(jumpbtn.indexOf("1")>=0) {
					div.append("<div id=\"pop_draw\" style =\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;"
							+ "cursor:pointer;font-size:18px;color:white;background-color:rgb(14,153,210);margin-top:5px;\" "
							+ "onclick=\"showDraw("+dczjid+");\">前往抽奖</div>");
				}
				if(jumpbtn.indexOf("2")>=0) {
					div.append("<div id=\"pop_draw\" style =\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;"
							+ "cursor:pointer;font-size:18px;color:white;background-color:rgb(14,153,210);margin-top:5px;\" "
							+ "onclick=\"location.href=\'"+thanksSetting.getJumpurl()+"\'\">"+thanksSetting.getBtnname()+"</div>");
				}
			}
			div.append("</div></div>");
		}else {
			div.append("<style>" + 
					"#bgcs {" + 
					"	width: 100%;" + 
					"	height: 100%;" + 
					"	top: 0px;" + 
					"	left: 0px;" + 
					"	position: fixed;" + 
					"	filter: Alpha(opacity = 50);" + 
					"	opacity: 0.2;" + 
					"	background: #000000;" + 
					"	display: none;" + 
					"}" + 
					"" + 
					"#popboxcs {" + 
					"	position:fixed;" + 
					"	width:300px; " + 
					"	height:300px;" + 
					"	left:50%;" + 
					"	top:50%;" + 
					"	margin:-150px 0 0 -150px;" + 
					"	display:none;" + 
					"	padding:0px;" + 
					"	background:#fff;" + 
					"	border-radius:5px;" + 
					"	z-index:100;" + 
					"}" + 
					"</style>" + 
					"<div id=\"bgcs\"></div>");
			div.append("<div id=\"popboxcs\">" + 
					"   <span style=\"position:absolute;top:6px;right:6px;display:inline-block;width:28px;height:28px;cursor:pointer;\"><img src=\"../../resources/dczj/images/jumpdiv/close.png\" width=\"27\" height=\"26\" onclick=\"closeResultWind();\"/></span>" + 
					"   <div style=\"padding:30px 36% ;\"><img src=\"../../resources/dczj/images/jumpdiv/gou.png\" width=\"94\" height=\"94\" /></div>" + 
					"    <div style=\"padding:0px 14% 15px;font-size:18px;font-weight:bold;text-align:center;\">提交成功，谢谢您的参与！</div>" + 
					"    <div style=\"padding:10px 20% 10px;color:rgb(14,153,210);\" >");
			div.append("<div id=\"pop_time\" style=\"display:none;font-size:18px;text-align:center;\">即将跳转倒计时<span id = \"endsecond\" style =\"color:red;\">10</span>秒</div>");
			div.append("<div id=\"\" style =\"border:solid 1px rgb(14,153,210);border-radius:5px;text-align:center;"
					+ "cursor:pointer;font-size:18px;color:white;background-color:rgb(14,153,210);margin-top:5px;\" "
					+ "onclick=\"location.href=\'"+setting.getJumpurl()+"\'\">跳转至指定页面</div>");
		}
		return div.toString();
	}
}
