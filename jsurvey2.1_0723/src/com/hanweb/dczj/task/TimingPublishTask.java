package com.hanweb.dczj.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.PublishTaskService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;

public class TimingPublishTask extends BaseTask{

	@Override
	protected void config() {
		setTaskId("timingPublishtask");
		setTaskName("定时发布线程");
		//每天0点执行
		TaskScheduleBuilder taskScheduleBuilder = TaskScheduleBuilder.getInstance();
		taskScheduleBuilder.setHour("0");
		setTaskSchedule(taskScheduleBuilder.getSchedule());	
	}

	@Override
	protected void doWork(JobDataMap arg0) {
		TitleInfoService titleInfoService = SpringUtil.getBean(TitleInfoService.class);
		SettingService settingService = SpringUtil.getBean(SettingService.class);
		PublishTaskService publishTaskService = SpringUtil.getBean(PublishTaskService.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Dczj_Setting setting = new Dczj_Setting();
		//查找所有未开始的调查征集
		List<TitleInfo> titleInfos = titleInfoService.getTitleListByStatus("1");
		for(TitleInfo titleInfo : titleInfos) {
			//查找指定调查征集设置
			setting = settingService.getEntityBydczjid(titleInfo.getIid()+"");
			if(setting != null && setting.getIsstart()==1) {
				//开启定时发布的调查征集
				if(StringUtil.equals(sdf.format(setting.getStarttime()), sdf.format(new Date()))) {
					//状态设置为正在进行
					titleInfoService.updateState(titleInfo.getIid()+"", 0);
					//发布
					publishTaskService.dopublish(titleInfo.getIid()+"");
				}
			}
		}
	}

}
