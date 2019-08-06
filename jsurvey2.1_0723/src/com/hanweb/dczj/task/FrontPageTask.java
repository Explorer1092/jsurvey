package com.hanweb.dczj.task;

import java.util.List;

import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.dczj.entity.Dczj_Setting;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.CheckedBoxRecoService;
import com.hanweb.dczj.service.PublishTaskService;
import com.hanweb.dczj.service.RadioRecoService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TitleInfoService;

public class FrontPageTask extends BaseTask{

	@Override
	protected void config() {
		setTaskId("pagecachetask");
		setTaskName("前台调查结果页生成缓存线程");
		setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(1));
	}

	@Override
	protected void doWork(JobDataMap arg0) {
		TitleInfoService titleInfoService = SpringUtil.getBean(TitleInfoService.class);
		SettingService settingService = SpringUtil.getBean(SettingService.class);
		PublishTaskService publishTaskService = SpringUtil.getBean(PublishTaskService.class);
		RadioRecoService radioRecoService = SpringUtil.getBean(RadioRecoService.class);
		CheckedBoxRecoService checkedBoxRecoService = SpringUtil.getBean(CheckedBoxRecoService.class);
		List<TitleInfo> titleInfoList = titleInfoService.getTitleListByStatus("0");
		if(titleInfoList != null && titleInfoList.size()>0) {
			for(TitleInfo titleInfo : titleInfoList) {
				Dczj_Setting setting = settingService.getEntityBydczjid(titleInfo.getIid()+"");
				if(setting != null && setting.getIsopen() == 1 && titleInfo.getIspublish() == 1) {
					publishTaskService.doResultPublish(titleInfo.getIid()+"");
					radioRecoService.countByDczjid(titleInfo.getIid()+"");
					checkedBoxRecoService.countByDczjid(titleInfo.getIid()+"");
				}
			}
		}
		
		
	}

}
