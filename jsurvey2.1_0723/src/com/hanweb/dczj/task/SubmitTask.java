package com.hanweb.dczj.task;

import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.dczj.service.FrontSubmitService;

public class SubmitTask extends BaseTask{
	@Override
	protected void config() {
		setTaskId("submitcachetask");
		setTaskName("前台页面缓存提交线程");
		//每**秒执行一次
		setTaskSchedule(TaskScheduleBuilder.getEverySecondSchedule(60));
	}

	@Override
	protected void doWork(JobDataMap arg0) {
		SpringUtil.getBean(FrontSubmitService.class).doSubmitTask();
	}

}
