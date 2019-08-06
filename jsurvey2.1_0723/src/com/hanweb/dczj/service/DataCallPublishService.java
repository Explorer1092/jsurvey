package com.hanweb.dczj.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.json.Type;
import com.hanweb.dczj.entity.DataCall;
import com.hanweb.dczj.entity.DataCallTemplate;
import com.hanweb.dczj.entity.DczjList;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.TitleInfo;

public class DataCallPublishService {

	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private DataCallTempService tempalteService;
	@Autowired
	private DataCallService dataCallService;
	@Autowired
	private SettingService settingService;
	
	Cache cache = CacheManager.getInstance("surveyFormListByWebId");
	
	/**
	 * 组织静态HTML
	 * @param jacttemp
	 * @param jactEn
	 * @return
	 */
	public String getJactPage(String jacttemp, DataCall En) {
		String first_content = "";
		String second_content = "";
		String third_content = "";
		String content = "";
		
		int firstindex = jacttemp.indexOf("<!--dczj_start-->");
		int secondindex = jacttemp.indexOf("<!--dczj_end-->");
		first_content = jacttemp.substring(0,firstindex);
		second_content = jacttemp.substring(firstindex+17, secondindex);
		third_content = jacttemp.substring(secondindex+15);
		
		List<DczjList> dczjList = this.getDczjList(En);
		//解析模板标签
		second_content = this.parseJactList(second_content,En,dczjList);
		third_content = this.parseJactPage(third_content,En,dczjList);
		content = first_content + second_content + third_content;
		return content;
	}

	/**
	 * 获取所有满足条件的调查征集列表集合
	 * @param jactEn
	 * @return
	 */
	private List<DczjList> getDczjList(DataCall dataCall) {
		String webids = dataCall.getDatacall_webids();
		int filter_condition = dataCall.getFiltercondition();
		
		List<DczjList> dczjList = new ArrayList<DczjList>();
		List<TitleInfo> titleInfoList = null;
		
		String hrefpath = "";
		String name = "";
		String starttime = "";
		String endtime = "";
		String createtime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i_state = 0;
		Properties pro = new Properties(BaseInfo.getRealPath()+ "/WEB-INF/config/setup.properties");
		String path = pro.getString("domain");

    	for(String webId : StringUtil.toStringList(webids)){
   		    cache.put("surveyFormListByWebId_"+webId,null);
    		titleInfoList = titleInfoService.findTitleByWebId1(NumberUtil.getInt(webId), dataCall.getDatacall_types());
    		if(titleInfoList != null){
				for(TitleInfo titleInfo : titleInfoList){
					Dczj_Setting setting = settingService.getEntityBydczjid(titleInfo.getIid()+"");
					Date startDate = null;
					if(setting.getIsstart()==1 && setting.getStarttime()!=null) {
						startDate = setting.getStarttime();
					}
					Date endDate = null;
					if(setting.getIsend()==1 && setting.getEndtime()!=null) {
						endDate = setting.getEndtime();
					}
					i_state = titleInfo.getState();
					if(filter_condition != 0 && i_state != filter_condition){
						continue;
					}
					DczjList en = new DczjList();
					hrefpath = path + "/jsurvey/questionnaire/jsurvey_"+titleInfo.getIid()+".html";
					name = titleInfo.getTitlename();
					if(startDate!=null) {
						starttime = sdf.format(startDate);
					}else {
						starttime = "1900-01-01 00:00:00";
					}
					if(endDate!=null) {
						endtime = sdf.format(endDate);
					}else {
						endtime = "2100-01-01 00:00:00";
					}
					createtime = sdf.format(titleInfo.getCreatetime());
					en.setName(name);
					en.setStarttime(starttime);
					en.setEndtime(endtime);
					en.setState(i_state+"");
					en.setHrefpath(hrefpath);
					en.setCreatetime(createtime);
					dczjList.add(en);
				}
			}
    	}
		return dczjList;
	}

	/**
	 * 解析标签
	 * @param second_content
	 * @param jactEn
	 * @param dczjList
	 * @return
	 */
	private String parseJactList(String second_content, DataCall dataCall, List<DczjList> dczjList) {
		Integer jactnum = dataCall.getDatacall_number();
		String content="";
        
        if(dczjList != null && dczjList.size()>0){
			ListSort(dczjList,dataCall.getDatacall_sorttype(),dataCall.getDatacall_sort());
			String i_state = "";
			
			int num = 0;
			if(jactnum <= dczjList.size()){
				num = jactnum;
			}else{
				num = dczjList.size();
			}
			for(int i=0;i<num;i++){
				DczjList dczjListEn = dczjList.get(i);
				if (StringUtil.equals(dczjListEn.getState(), "0")) {
					i_state = "<span class='dczj_notbeginning'>进行中</span>";
				} else if (StringUtil.equals(dczjListEn.getState(), "1")) {
					i_state = "<span class='dczj_beginning'>已结束</span>";
				} else if (StringUtil.equals(dczjListEn.getState(), "2")) {
					i_state = "<span class='dczj_end'>未开始</span>";
				}
				
				if(dataCall.getDatacall_type()==0) {
					if(dataCall.getDatacall_sorttype()==1) {
						content += second_content.replaceAll("<!--dczj_time-->", dczjListEn.getStarttime()).replaceAll("<!--dczj_state}-->", i_state).replaceAll("<!--dczj_title-->", dczjListEn.getName())
		                         .replaceAll("<!--dczj_href-->", dczjListEn.getHrefpath()).replaceAll("<!--dczj_starttime-->", dczjListEn.getStarttime()).replaceAll("<!--dczj_endtime-->", dczjListEn.getEndtime());
					}else if(dataCall.getDatacall_sorttype()==2) {
						content += second_content.replaceAll("<!--dczj_time-->", dczjListEn.getEndtime()).replaceAll("<!--dczj_state}-->", i_state).replaceAll("<!--dczj_title-->", dczjListEn.getName())
		                         .replaceAll("<!--dczj_href-->", dczjListEn.getHrefpath()).replaceAll("<!--dczj_starttime-->", dczjListEn.getStarttime()).replaceAll("<!--dczj_endtime-->", dczjListEn.getEndtime());
					}else {
						content += second_content.replaceAll("<!--dczj_time-->", dczjListEn.getCreatetime()).replaceAll("<!--dczj_state}-->", i_state).replaceAll("<!--dczj_title-->", dczjListEn.getName())
		                         .replaceAll("<!--dczj_href-->", dczjListEn.getHrefpath()).replaceAll("<!--dczj_starttime-->", dczjListEn.getStarttime()).replaceAll("<!--dczj_endtime-->", dczjListEn.getEndtime());
					}
				}else {
					content += second_content.replaceAll("<!--dczj_time-->", dczjListEn.getCreatetime()).replaceAll("<!--dczj_state}-->", i_state).replaceAll("<!--dczj_title-->", dczjListEn.getName())
	                         .replaceAll("<!--dczj_href-->", dczjListEn.getHrefpath()).replaceAll("<!--dczj_starttime-->", dczjListEn.getStarttime()).replaceAll("<!--dczj_endtime-->", dczjListEn.getEndtime());
				}
					
				
			}
			
		}
		return content;
	}

	/**
	 * 解析标签
	 * @param second_content
	 * @param jactEn
	 * @param dczjList
	 * @return
	 */
	private String parseJactPage(String third_content, DataCall dataCall,List<DczjList> dczjList) {
		if(dczjList != null && dczjList.size()>0){
			if(dataCall.getDatacall_type() == 1){
				String pagecontent ="<input id=\"pageno_lui\" type=\"hidden\" value=\"1\" ><input id=\"jactid\" type=\"hidden\" value="+dataCall.getIid()+" >" 
				                   +"<input id=\"pageNum\" type=\"hidden\" value="+dataCall.getDatacall_number()+" ><input id=\"pageNumCount\" type=\"hidden\" value="+dczjList.size()+" >" 
				                   +"<div id=\"jactpage\" style=\"margin-left:10px;\"></div><script type='text/javascript' src='../../resources/dczj/datacall/datacall.js'></script>";
				third_content = third_content.replaceAll("<!--dczj_paging-->", pagecontent);
			}
		}
		return third_content;
	}
	
	/**
	 * 对list列表排序
	 * @param list
	 * @param jact_sorttype
	 * @param jact_sort
	 */
	public static void ListSort(List<DczjList> list, final Integer jact_sorttype,final Integer jact_sort) {  
        Collections.sort(list, new Comparator<DczjList>() {  
            @Override  
            public int compare(DczjList o1, DczjList o2) {  
   //             SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {  
                    Date dt1 = null;  
                    Date dt2 = null;
                    if(jact_sorttype == 0){
                    	dt1 = format.parse(o1.getCreatetime());
                    	dt2 = format.parse(o2.getCreatetime());  
                    }else if(jact_sorttype == 1){
                    	dt1 = format.parse(o1.getStarttime());
                    	dt2 = format.parse(o2.getStarttime());  
                    }else if(jact_sorttype == 2){
                    	dt1 = format.parse(o1.getEndtime());
                    	dt2 = format.parse(o2.getEndtime());  
                    }
                    if(dt1 != null && dt2 != null) {
                    	if (dt1.getTime() > dt2.getTime()) {  
                        	if(jact_sort == 0){
                        		 return -1;  
                        	}else if(jact_sort == 1){
                        		 return 1;  
                        	}
                        } else if (dt1.getTime() < dt2.getTime()) {  
                        	if(jact_sort == 0){
                       		 return 1;  
                         	}else if(jact_sort == 1){
                       		 return -1;  
                       	    }
                        } else {  
                            return 0;  
                        }  
                    }
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
                return 0;  
            }  
        });  
    }

	/**
	 * 组织循环内的列表
	 * @param jactid
	 * @param pageno
	 * @param pageNum
	 * @return
	 */
	public String getPageHtml(String jactid, int pageno, int pageNum) {
		String jacttemp = "";
		DataCallTemplate templateEn = tempalteService.findTemplate(jactid,"1");
		DataCall dataCall = dataCallService.findJactByIid(NumberUtil.getInt(jactid));
		
		if(templateEn == null){
			String path = BaseInfo.getRealPath()+ "/resources/dczj/datacall/datacalllist.html";
			jacttemp = FileUtil.readFileToString(path);
		}else{
			jacttemp = templateEn.getTemplate();
		}
		int firstindex = jacttemp.indexOf("<!--dczj_start-->");
		int secondindex = jacttemp.indexOf("<!--dczj_end-->");
		String second_content = jacttemp.substring(firstindex+17, secondindex);
		second_content = this.parseFrontJactPage(second_content,dataCall,pageno,pageNum);
		return second_content;
	}

	
	private String parseFrontJactPage(String second_content, DataCall dataCall, int pageno, int pageNum) {

		String webids = dataCall.getDatacall_webids();
		int filter_condition = dataCall.getFiltercondition();
		
		List<DczjList> dczjList = new ArrayList<DczjList>();
		List<TitleInfo> titleInfoList = null;
		
		String hrefpath = "";
		String name = "";
		String starttime = "";
		String endtime = "";
		String createtime = "";
		int state = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Properties pro = new Properties(BaseInfo.getRealPath()+ "/WEB-INF/config/setup.properties");
		String path = pro.getString("domain");

		for(String webId : StringUtil.toStringList(webids)){
			titleInfoList = cache.get("surveyFormListByWebId_" + webId, new Type<List<TitleInfo>>() {});
			if(titleInfoList == null){
				titleInfoList = titleInfoService.findTitleByWebId1(NumberUtil.getInt(webId), dataCall.getDatacall_types());
				if(titleInfoList != null){
					cache.put("surveyFormListByWebId_"+webId, titleInfoList);
				}
			}
    		
    		if(titleInfoList != null){
				for(TitleInfo titleInfo : titleInfoList){
					Dczj_Setting setting = settingService.getEntityBydczjid(titleInfo.getIid()+"");
					Date startDate = null;
					if(setting.getIsstart()==1 && setting.getStarttime()!=null) {
						startDate = setting.getStarttime();
					}
					Date endDate = null;
					if(setting.getIsend()==1 && setting.getEndtime()!=null) {
						endDate = setting.getEndtime();
					}
					state = titleInfo.getState();
					if(filter_condition != 0 && state != filter_condition){
						continue;
					}
					DczjList en = new DczjList();
					hrefpath = path + "/jsurvey/questionnaire/jsurvey_"+titleInfo.getIid()+".html";
					name = titleInfo.getTitlename();
					if(startDate!=null) {
						starttime = sdf.format(startDate);
					}else {
		//				starttime = "未指定时间";
						starttime = "1900-01-01 00:00:00";
					}
					if(endDate!=null) {
						endtime = sdf.format(endDate);
					}else {
				//		endtime = "未指定时间";
						endtime = "2100-01-01 00:00:00";
					}
					createtime = sdf.format(titleInfo.getCreatetime());
					en.setName(name);
					en.setStarttime(starttime);
					en.setEndtime(endtime);
					en.setState(state+"");
					en.setHrefpath(hrefpath);
					en.setCreatetime(createtime);
					dczjList.add(en);
				}
			}
    	}
    
        String content = "";
        if(dczjList != null && dczjList.size()>0){
			ListSort(dczjList,dataCall.getDatacall_sorttype(),dataCall.getDatacall_sort());
			String i_state = "";
			
			int startnum = 0;
			int endnum = 0;
			startnum = (pageno-1)*pageNum;
			endnum = pageno*pageNum;
			if(endnum >= dczjList.size()){
				endnum = dczjList.size();
			}
			for(int i=startnum;i<endnum;i++){
				DczjList dczjListEn = dczjList.get(i);
				if (StringUtil.equals(dczjListEn.getState(), "0")) {
					i_state = "<span class='dczj_notbeginning'>进行中</span>";
				} else if (StringUtil.equals(dczjListEn.getState(), "1")) {
					i_state = "<span class='dczj_beginning'>已结束</span>";
				} else if (StringUtil.equals(dczjListEn.getState(), "2")) {
					i_state = "<span class='dczj_end'>未开始</span>";
				}
				content += second_content.replaceAll("<!--dczj_time-->", dczjListEn.getCreatetime()).replaceAll("<!--dczj_state}-->", i_state).replaceAll("<!--dczj_title-->", dczjListEn.getName())
				                         .replaceAll("<!--dczj_href-->", dczjListEn.getHrefpath()).replaceAll("<!--dczj_starttime-->", dczjListEn.getStarttime()).replaceAll("<!--dczj_endtime-->", dczjListEn.getEndtime());
			}
			
		}
		return content;
	}  

}
