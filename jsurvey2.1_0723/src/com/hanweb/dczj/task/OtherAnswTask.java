package com.hanweb.dczj.task;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobDataMap;

import com.hanweb.common.cache.Cache;
import com.hanweb.common.cache.CacheManager;
import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.dczj.entity.AnswInfo;
import com.hanweb.dczj.entity.CheckedBoxReco;
import com.hanweb.dczj.entity.ContentReco;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.DisplayConfig;
import com.hanweb.dczj.entity.QuesInfo;
import com.hanweb.dczj.entity.RadioReco;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.AnswInfoService;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.ContentRecoService;
import com.hanweb.dczj.service.DisplayConfigService;
import com.hanweb.dczj.service.QuesInfoService;
import com.hanweb.dczj.service.RadioRecoService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;

public class OtherAnswTask extends BaseTask{

	@Override
	protected void config() {
		setTaskId("jsurvey_openAnswer");
		setTaskName("调查征集更新公开信息缓存");
		setCanRemove(false);
		setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(60));
	}

	@Override
	protected void doWork(JobDataMap arg0) {
		Cache cache = CacheManager.getInstance("jsurvey_openAnswer");
		TitleInfoService titleInfoService = SpringUtil.getBean(TitleInfoService.class);
		RadioRecoService radioRecoService = SpringUtil.getBean(RadioRecoService.class);
		CheckedBoxRecoService checkedBoxRecoService = SpringUtil.getBean(CheckedBoxRecoService.class);
		ContentRecoService contentRecoService = SpringUtil.getBean(ContentRecoService.class);
		QuesInfoService quesInfoService = SpringUtil.getBean(QuesInfoService.class);
		AnswInfoService answInfoService = SpringUtil.getBean(AnswInfoService.class);
		SettingService settingService = SpringUtil.getBean(SettingService.class);
		DisplayConfigService displayConfigService = SpringUtil.getBean(DisplayConfigService.class);
		List<AnswInfo> answInfos = new ArrayList<AnswInfo>();
		
		//所有进行中的调查
		List<TitleInfo> titleInfos = titleInfoService.getTitleListByStatus("0");
		if(titleInfos != null && titleInfos.size()>0) {
			for(TitleInfo titleInfo : titleInfos) {
				Dczj_Setting dczj_Setting = settingService.getEntityBydczjid(titleInfo.getIid()+"");
				if(dczj_Setting == null || dczj_Setting.getIsopen()==0) {
					continue;
				}
				DisplayConfig displayConfig = displayConfigService.findEntityByDczjid(titleInfo.getIid());
				if(displayConfig==null ||  displayConfig.getIsopencontent()==0) {
					continue;
				}
				List<QuesInfo> quesInfos = quesInfoService.findDQuesByDczjId(titleInfo.getIid()+"");
				if(quesInfos != null && quesInfos.size()>0) {
					for(QuesInfo quesInfo : quesInfos) {
						if(quesInfo.getType()==0) {
							List<RadioReco> radioRecos = new ArrayList<RadioReco>();
							answInfos = answInfoService.getAnswListByQuesId(quesInfo.getIid());
							if(answInfos != null && answInfos.size()>0) {
								for(AnswInfo answInfo : answInfos) {
									if(answInfo.getAllowfillinair()==1) {
										int count = radioRecoService.getRecoConut(quesInfo.getIid()+"", answInfo.getIid()+"");
										int page = count/1000+1;
										for(int i=0;i<page;i++) {
											radioRecos.addAll(radioRecoService.findOtherAnswerList(page, 1000, quesInfo.getIid()+"", answInfo.getIid()+""));
										}
										cache.put("jsurvey_"+quesInfo.getIid()+"_"+answInfo.getIid(), radioRecos);
									}
								}
							}
						}else if(quesInfo.getType()==1) {
							List<CheckedBoxReco> checkedBoxRecos = new ArrayList<CheckedBoxReco>();
							answInfos = answInfoService.getAnswListByQuesId(quesInfo.getIid());
							if(answInfos != null && answInfos.size()>0) {
								for(AnswInfo answInfo : answInfos) {
									if(answInfo.getAllowfillinair()==1) {
										int count = checkedBoxRecoService.getRecoConut(quesInfo.getIid()+"", answInfo.getIid()+"");
										int page = count/1000+1;
										for(int i=0;i<page;i++) {
											checkedBoxRecos.addAll(checkedBoxRecoService.findOtherAnswerList(page, 1000, quesInfo.getIid()+"", answInfo.getIid()+""));
										}
										cache.put("jsurvey_"+quesInfo.getIid()+"_"+answInfo.getIid(), checkedBoxRecos);
									}
								}
							}
						}else if(quesInfo.getType()==2) {
							List<ContentReco> contentRecos = new ArrayList<ContentReco>();
							answInfos = answInfoService.getAnswListByQuesId(quesInfo.getIid());
							if(answInfos != null && answInfos.size()>0) {
								for(AnswInfo answInfo : answInfos) {
									if(answInfo.getAllowfillinair()==1) {
										int count = contentRecoService.getRecoConut(quesInfo.getIid()+"", answInfo.getIid()+"");
										int page = count/1000+1;
										for(int i=0;i<page;i++) {
											contentRecos.addAll(contentRecoService.findOtherAnswerList(page, 1000, quesInfo.getIid()+"",1));
										}
										cache.put("jsurvey_"+quesInfo.getIid()+"_"+answInfo.getIid(), contentRecos);
									}
								}
							}
						}else if(quesInfo.getType()==5) {
							List<ContentReco> contentRecos = new ArrayList<ContentReco>();
							answInfos = answInfoService.getAnswListByQuesId(quesInfo.getIid());
							if(answInfos != null && answInfos.size()>0) {
								for(AnswInfo answInfo : answInfos) {
									if(answInfo.getAllowfillinair()==1) {
										int count = contentRecoService.getRecoConut(quesInfo.getIid()+"", answInfo.getIid()+"");
										int page = count/1000+1;
										for(int i=0;i<page;i++) {
											contentRecos.addAll(contentRecoService.findOtherAnswerList(page, 1000, quesInfo.getIid()+""));
										}
										cache.put("jsurvey_"+quesInfo.getIid()+"_"+answInfo.getIid(), contentRecos);
									}
								}
							}
						}
					}
				}
			}
		}
		
	}

}
