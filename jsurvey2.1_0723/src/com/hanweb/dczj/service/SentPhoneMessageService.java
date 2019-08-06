package com.hanweb.dczj.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.entity.Mobilelog;
import com.hanweb.sso.ldap.util.MD5;

public class SentPhoneMessageService {
	@Autowired
	private SurveyMobilelogService surveyMobilelogService;
	/**
	*计算两个日期之间的差值 (调查）
	*/
	public long[] dateDiff(String mobile, String formid) {
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		String[][] strData = surveyMobilelogService.getDate(formid,mobile);
		if(strData != null && strData.length > 0){
			/*if( strData.length>=5 ){
				return new long[]{-1,0};
			}*/
			int count = 1;
			String datelist = null;
			String datelist2 = null;
			Date da = null;
			String begin = null;
			String now = null;
			Date be = null;
			String end = null;
			Date en = null;
			for(int i=0;i<strData.length;i++){
				now = DateUtil.dateToString(new Date(), DateUtil.YYYY_MM_DD);
				now = now.substring(0,10);
				 datelist = strData[i][0];
				 datelist2 = datelist.substring(0,10);
				 
				 if(now.equals(datelist2)){
					 da = DateUtil.stringtoDate(datelist,DateUtil.YYYY_MM_DD_HH_MM_SS);
						begin = datelist.substring(0,10);
						begin = begin+" 00:00:00";
						be = DateUtil.stringtoDate(begin,DateUtil.YYYY_MM_DD_HH_MM_SS);
						end = datelist.substring(0,10);
						end = end+" 23:59:59";
						en = DateUtil.stringtoDate(end,DateUtil.YYYY_MM_DD_HH_MM_SS);
						
						if(da.after(be)&&da.before(en)){
							long a = DateUtil.dayDiff(new Date(),DateUtil.stringtoDate(datelist,DateUtil.YYYY_MM_DD_HH_MM_SS));
							if(a >= 0){
								count++;
							}
						}
				 }
				
				
			}
			if(count > 5){
				return new long[]{-1,0};
			}
			String date = strData[0][0];
			if("".equals(date)){
				return null;
			}
			String nowData = DateUtil.getCurrDateTime();
			SimpleDateFormat sim = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
			try{
				Date d1 = sim.parse(date); 
				d1 = new Date(d1.getTime() + 2*60*1000);//获取验证间隔时间
				Date d2 = sim.parse(nowData);
				if(d1.before(d2)){
					return null;	
				}
				long min = (d1.getTime() - d2.getTime()) %nd%nh/nm;
				long sec = (d1.getTime() - d2.getTime()) %nd%nh%nm/ns;
				return new long[]{min,sec};
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	*计算两个日期之间的差值 （征集）
	*/
	public long[] dateDiff2(String mobile, String topicid) {
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		String[][] strData = surveyMobilelogService.getDate2(topicid,mobile);
		if(strData != null && strData.length > 0){
			/*if( strData.length>=5 ){
				return new long[]{-1,0};
			}*/
			int count = 1;
			String datelist = null;
			String datelist2 = null;
			Date da = null;
			String begin = null;
			String now = null;
			Date be = null;
			String end = null;
			Date en = null;
			for(int i=0;i<strData.length;i++){
				now = DateUtil.dateToString(new Date(), DateUtil.YYYY_MM_DD);
				now = now.substring(0,10);
				 datelist = strData[i][0];
				 datelist2 = datelist.substring(0,10);
				 
				 if(now.equals(datelist2)){
					 da = DateUtil.stringtoDate(datelist,DateUtil.YYYY_MM_DD_HH_MM_SS);
						begin = datelist.substring(0,10);
						begin = begin+" 00:00:00";
						be = DateUtil.stringtoDate(begin,DateUtil.YYYY_MM_DD_HH_MM_SS);
						end = datelist.substring(0,10);
						end = end+" 23:59:59";
						en = DateUtil.stringtoDate(end,DateUtil.YYYY_MM_DD_HH_MM_SS);
						
						if(da.after(be)&&da.before(en)){
							long a = DateUtil.dayDiff(new Date(),DateUtil.stringtoDate(datelist,DateUtil.YYYY_MM_DD_HH_MM_SS));
							if(a >= 0){
								count++;
							}
						}
				 }
				
				
			}
			if(count > 5){
				return new long[]{-1,0};
			}
			String date = strData[0][0];
			if("".equals(date)){
				return null;
			}
			String nowData = DateUtil.getCurrDateTime();
			SimpleDateFormat sim = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
			try{
				Date d1 = sim.parse(date); 
				d1 = new Date(d1.getTime() + 2*60*1000);//获取验证间隔时间
				Date d2 = sim.parse(nowData);
				if(d1.before(d2)){
					return null;	
				}
				long min = (d1.getTime() - d2.getTime()) %nd%nh/nm;
				long sec = (d1.getTime() - d2.getTime()) %nd%nh%nm/ns;
				return new long[]{min,sec};
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	/***
	 * 调用易和短信接口发送短信验证码 （调查征集）
	 * @param mobile
	 * @param msg
	 * @param formid
	 * @param ip
	 * @param code
	 * @return
	 */
	public String sendMessageCode(String mobile, String msg, String formid,String topicid,String voteid ,String ip,int code){
//		   System.out.println("formid:"+formid+":topicid:"+topicid);
//		   System.out.println("getEnablememcache:"+Settings.getSettings().getEnablememcache());
//		   System.out.println("getMemcacheIp:"+Settings.getSettings().getMemcacheIp());
//		   System.out.println("getMemcacheport:"+Settings.getSettings().getMemcacheport());
		   Date current = new Date();
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		   String time = formatter.format(current);
		   String sign = MD5.encodeByJavaSecurity("h6HYUjhNnHiVcU7Y"+time);
		   String results = "";
		   String messageurl = Settings.getSettings().getMessageurl();
//		   System.out.println("messageurl:"+messageurl);
		   String messageparam = Settings.getSettings().getMessageparam();
//		   System.out.println("messageparam:"+messageparam);
		   String params ="";
		   URL syncurl = null;
		   try {
			   String message = URLEncoder.encode(msg, "UTF-8");
			   if(StringUtil.isNotEmpty(messageparam)){
				   messageparam = messageparam.replace("=:message", "="+message).replace("=:time", "="+time)
						   						.replace("=:sign", "="+sign).replace("=:mobile", "="+mobile);
				   params = messageparam;
			   }
//			   System.out.println("params:"+params);
//		   		String params = "method=send&&message="+message+"&servercode=h6HYUjhN&time="+time+"&sign="+sign+"&srcnum=3117&desttype=0&dest="+mobile+"&messageid=";
//		   		URL syncurl = new URL("http://59.202.28.89/smsapi/servlet/smsapi");
			   if(StringUtil.isNotEmpty(messageurl)){
				   syncurl = new URL(messageurl);
				   HttpURLConnection conn;
				   conn = (HttpURLConnection) syncurl.openConnection();
				   conn.setDoOutput(true);
				   conn.setRequestMethod("POST");
				   byte[] ob = params.getBytes();
					conn.getOutputStream().write(ob);
					conn.setConnectTimeout(10000);
					InputStream in = conn.getInputStream();
					BufferedReader b = new BufferedReader(new InputStreamReader(in));
					String line;
					StringBuffer strb = new StringBuffer();
					while ((line = b.readLine()) != null) {
						strb.append(line);
					}
					results = strb.toString();
					//设置5秒超时
					b.close();
					in.close();
			   }
			}catch (Exception e) {
				//e.printStackTrace();
			}finally{
				Mobilelog mobileLog = new Mobilelog();
				if(StringUtil.isNotEmpty(formid)){
					mobileLog.setI_formid(NumberUtil.getInt(formid));
				}
				 if(StringUtil.isNotEmpty(topicid)){
					 mobileLog.setI_topicid(NumberUtil.getInt(topicid));
				 }
				 if(StringUtil.isNotEmpty(voteid)){
					 mobileLog.setI_voteid(NumberUtil.getInt(voteid));
				 }
				mobileLog.setC_createdate(DateUtil.getCurrDateTime());
				mobileLog.setVc_mobile(mobile);
				mobileLog.setI_type(1);
				mobileLog.setVc_ip(ip);
				mobileLog.setVc_code(code + "");
				mobileLog.setVc_result(results);
				surveyMobilelogService.insert(mobileLog);
			}
			return results;
	}

	public long[] dateVoteDiff(String mobile, String voteid) {
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		String[][] strData = surveyMobilelogService.getDate3(voteid,mobile);
		if(strData != null && strData.length > 0){
			/*if( strData.length>=5 ){
				return new long[]{-1,0};
			}*/
			int count = 1;
			String datelist = null;
			String datelist2 = null;
			Date da = null;
			String begin = null;
			String now = null;
			Date be = null;
			String end = null;
			Date en = null;
			for(int i=0;i<strData.length;i++){
				now = DateUtil.dateToString(new Date(), DateUtil.YYYY_MM_DD);
				now = now.substring(0,10);
				 datelist = strData[i][0];
				 datelist2 = datelist.substring(0,10);
				 
				 if(now.equals(datelist2)){
					 da = DateUtil.stringtoDate(datelist,DateUtil.YYYY_MM_DD_HH_MM_SS);
						begin = datelist.substring(0,10);
						begin = begin+" 00:00:00";
						be = DateUtil.stringtoDate(begin,DateUtil.YYYY_MM_DD_HH_MM_SS);
						end = datelist.substring(0,10);
						end = end+" 23:59:59";
						en = DateUtil.stringtoDate(end,DateUtil.YYYY_MM_DD_HH_MM_SS);
						
						if(da.after(be)&&da.before(en)){
							long a = DateUtil.dayDiff(new Date(),DateUtil.stringtoDate(datelist,DateUtil.YYYY_MM_DD_HH_MM_SS));
							if(a >= 0){
								count++;
							}
						}
				 }
				
				
			}
			if(count > 5){
				return new long[]{-1,0};
			}
			String date = strData[0][0];
			if("".equals(date)){
				return null;
			}
			String nowData = DateUtil.getCurrDateTime();
			SimpleDateFormat sim = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
			try{
				Date d1 = sim.parse(date); 
				d1 = new Date(d1.getTime() + 2*60*1000);//获取验证间隔时间
				Date d2 = sim.parse(nowData);
				if(d1.before(d2)){
					return null;	
				}
				long min = (d1.getTime() - d2.getTime()) %nd%nh/nm;
				long sec = (d1.getTime() - d2.getTime()) %nd%nh%nm/ns;
				return new long[]{min,sec};
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
