package com.hanweb.dczj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.entity.DataCall;
import com.hanweb.dczj.entity.DataCallTemplate;
import com.hanweb.dczj.entity.TitleInfo;

public class DczjStatisticalListTaskService {
	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private DataCallTempService tempalteService;
	@Autowired
	private DataCallService dataCallService;
	@Autowired
	private WebSiteService webSiteService;
	@Autowired
	private DataCallPublishService publishService;

	/**
	 * 定时生成缓存
	 * @return
	 */
	public boolean dczjListCache(){
		Cache cache = CacheManager.getInstance();
		List<DCZJ_WebSite> websiteList = webSiteService.findAllWebId();
		if(websiteList != null && websiteList.size()>0){
			for(DCZJ_WebSite website : websiteList){
				int webId = website.getIid();
				int surveycount = titleInfoService.findNumByWebid(webId);
				if(surveycount > 0){
					List<TitleInfo> surveyFormListByWebId = titleInfoService.findTitleByWebId(webId,null);
					cache.put("surveyFormListByWebId_"+webId, surveyFormListByWebId);
				}
				
			}
		}
		return true;
	}
	
	/**
	 * 定时更新前台嵌入页面
	 */
	public void getDczjListHtml() {
	    List<DataCall> jactList = dataCallService.findAllJact();
	    if(jactList != null && jactList.size()> 0){
	    	for(DataCall jactEn : jactList){
	    		if(jactEn.getUpdatehtml() == 1){
	    			String jactid = jactEn.getIid()+"";
	    			String loadPath = BaseInfo.getRealPath() + "/jsurvey/list/index_"+ jactid + ".html";
	    			String jacttemp = "";
	    			DataCallTemplate templateEn = tempalteService.findTemplate(jactid,"");
	    			
	    			if(templateEn == null){
	    				templateEn = new DataCallTemplate();
	    				String path = BaseInfo.getRealPath()+ "/resources/dczj/datacall/";
	    				if(StringUtil.equals("0", jactEn.getDatacall_type()+"")){
	    					path += "datacall.html";
	    				}else if(StringUtil.equals("1", jactEn.getDatacall_type()+"")){
	    					path += "datacalllist.html";
	    				}
	    				jacttemp = FileUtil.readFileToString(path);
	    			}else{
	    				jacttemp = templateEn.getTemplate();
	    			}
	    			String content = publishService.getJactPage(jacttemp,jactEn);
	    			FileUtil.writeStringToFile(loadPath, content);
	    		}
	    	}
	    }
		
	}
	
}
