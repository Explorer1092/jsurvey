package com.hanweb.dczj.service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.util.json.Type;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.ContentReco;
import com.hanweb.dczj.entity.RadioReco;
import com.hanweb.dczj.entity.TotalReco;

public class FrontSubmitService {
	
	@Autowired
	CheckedBoxRecoService checkedBoxRecoService;
	@Autowired
	ContentRecoService contentRecoService;
	@Autowired
	RadioRecoService radioRecoService;
	@Autowired
	TotalRecoService totalRecoService;

	/**
	 * 调查提交同步
	 * @param cache
	 * @param totalReco
	 */
	public synchronized void submitSync(Cache cache, TotalReco totalReco) {
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		int serveytype = Settings.getSettings().getServertype();
		Queue<TotalReco> cacheTotalRecoList = null;
		if(Settings.getSettings().getEnabledistributed() == 1){
			cacheTotalRecoList  = cache.get("total_"+minute+"_"+serveytype, new Type<Queue<TotalReco>>() {});
		}else{
			cacheTotalRecoList  = cache.get("total_"+minute, new Type<Queue<TotalReco>>() {});
		}
		
		if(cacheTotalRecoList == null){
			cacheTotalRecoList = new LinkedList<TotalReco>();
		}
		cacheTotalRecoList.offer(totalReco); //加入队列中
		
		if(Settings.getSettings().getEnabledistributed() == 1){
			cache.put("total_"+minute+"_"+serveytype, cacheTotalRecoList);
		}else{
			cache.put("total_"+minute, cacheTotalRecoList);
		}
	}
	
	/**
	 * 前台提交线程
	 * @return
	 */
	public boolean doSubmitTask() {
		Cache cache = CacheManager.getInstance("jsurvey");
		Queue<TotalReco> recoList = new LinkedList<TotalReco>();
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		int nowtime = minute;
		if(minute == 0){
			minute = 59;
		}else{
			minute = minute-1;
		}
		int serveytype = Settings.getSettings().getServertype();
		recoList = this.getSurveyRecoList(cache,nowtime,minute,serveytype,recoList);

		if(recoList==null){
			return false;
		}
		boolean issuccess = false;
		int listlen = recoList.size();
		for(int i=0;i<listlen;i++){
			//从队列中依次获取数据
			TotalReco  reco = recoList.poll();
			if( reco == null){
				continue;
			}
			
			//通过唯一码判断是否已经提交
			boolean isHaveSubmit = totalRecoService.findRecoIsHaveSubmitByUnid(reco.getUnid());
			if(isHaveSubmit){
				continue;
			}
			//插入总表
			issuccess = totalRecoService.insertEntity(reco);
			//从缓存中获取相对应的数据
			String unid = reco.getUnid();
			List<RadioReco> radioList = null;
			radioList = cache.get("radio"+unid, new Type<List<RadioReco>>() {});
			//循环插入单选
			if(radioList != null && radioList.size() > 0){
				issuccess = radioRecoService.insertFor(radioList);
				if(issuccess){
					cache.remove("radio"+unid);
				}
			}
			
			List<CheckedBoxReco> checkedBoxRecoList = null;
			checkedBoxRecoList = cache.get("checkedBox"+unid, new Type<List<CheckedBoxReco>>() {});
			//循环插入多选
			if(checkedBoxRecoList != null && checkedBoxRecoList.size() > 0 ){
				issuccess = checkedBoxRecoService.insertFor(checkedBoxRecoList);
				if(issuccess){
					cache.remove("checkedBox"+unid); //清除多选
				}
			}
			
			List<ContentReco> contentRecoList = null;
			contentRecoList = cache.get("content"+unid, new Type<List<ContentReco>>() {});
			//循环插入填空
			if(contentRecoList != null && contentRecoList.size() > 0){
				issuccess = contentRecoService.insertFor(contentRecoList);
				if(issuccess){
					cache.remove("content"+unid);  //清除填空
				}
			}
		}
		return issuccess;
	}
	
	private Queue<TotalReco> getSurveyRecoList(Cache cache, int nowtime, int minute, int serveytype, Queue<TotalReco> recoList) {
		int time = 0;
		if(minute == 0){
			time = 119 ;
		}else{
			time = minute-1+60;
		}
		if(nowtime == 59){
			nowtime = 0;
		}else{
			nowtime = nowtime+1;
		}
		if(minute == time){
			return recoList;
		}else{
			if(Settings.getSettings().getEnabledistributed() == 1){

				for(int i=time;i>=nowtime;i--){
					if(i >= 60){
						recoList  = cache.get("total_"+(i-60)+"_"+serveytype, new Type<Queue<TotalReco>>() {});
					}else{
						recoList  = cache.get("total_"+i+"_"+serveytype, new Type<Queue<TotalReco>>() {});
					}
					
					if(recoList != null){
						if(i >= 60){
							cache.remove("total_"+(i-60)+"_"+serveytype);
						}else{
							cache.remove("total_"+i+"_"+serveytype);
						}
						return recoList;
					}
				}
			}else{
				for(int i=time;i>=nowtime;i--){
					if(i >= 60){
						recoList  = cache.get("total_"+(i-60), new Type<Queue<TotalReco>>() {});
					}else{
						recoList  = cache.get("total_"+i, new Type<Queue<TotalReco>>() {});
					}
					
					if(recoList != null){
						if(i >= 60){
							cache.remove("total_"+(i-60));
						}else{
							cache.remove("total_"+i);
						}
						
						return recoList;
					}
				}
				
			}
			
		}
		return recoList;
	}
	
	/**
	 * 每天定时清理缓存数据线程
	 * @return
	 */
	public boolean clearCacheTask() {
		boolean success = false;
		List<TotalReco> recoList = totalRecoService.findSubmitRecoLists("0");
		//循环清理数据
		Cache cache = CacheManager.getInstance();
		if(recoList != null && recoList.size() > 0){
			String mobile = "";
			Integer formid = 0;
			String uIp = "";
			int ishave = 0;
			int isHaveSurvey = 0;
			for(TotalReco reco:recoList){
				 mobile = reco.getMobile();
				 formid =  reco.getDczjid();
				 uIp = reco.getIp().replace(".", "_").replace(":", "_");
				 if(cache.get("everydayforip"+formid + uIp, new Type<Integer>() {}) != null){
					 ishave = cache.get("everydayforip"+formid + uIp, new Type<Integer>() {});
					 if(ishave > 0){
						 cache.remove("everydayforip"+formid + uIp);
					 }
				 }
				 if(cache.get("everyday"+formid + mobile, new Type<Integer>() {}) != null){
					 isHaveSurvey = cache.get("everyday"+formid + mobile, new Type<Integer>() {});
					 if(isHaveSurvey > 0){
						 cache.remove("everyday"+formid + mobile);
					 }
				 }
			}
		}
		success = true;
		return success;
	}
	
}
