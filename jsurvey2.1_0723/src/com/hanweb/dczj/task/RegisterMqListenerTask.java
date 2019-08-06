package com.hanweb.dczj.task;

import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.complat.constant.Settings;
import com.hanweb.dczj.service.RegisterMqListenerService;

public class RegisterMqListenerTask extends BaseTask{

	@Override
	protected void config() {
		setTaskId("mqlistener_create");
		setTaskName("初始化mq监听");
		setTaskSchedule(TaskScheduleBuilder.getOnceSchedule());
	}

	@Override
	protected void doWork(JobDataMap arg0) {
		if(Settings.getSettings().getEnabledistributed()==1){
			SpringUtil.getBean(RegisterMqListenerService.class).registerDczjMq();
		}
	}

}
