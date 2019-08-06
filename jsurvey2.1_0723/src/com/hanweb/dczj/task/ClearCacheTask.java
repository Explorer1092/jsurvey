package com.hanweb.dczj.task;

import org.quartz.JobDataMap;
import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.dczj.service.FrontSubmitService;

public class ClearCacheTask extends BaseTask{
	@Override
	protected void config() {
		setTaskId("clearcachetask");
		setTaskName("每天清理缓存线程");
		TaskScheduleBuilder taskScheduleBuilder = TaskScheduleBuilder.getInstance();
		taskScheduleBuilder.setHour("23");  //每天零点清除缓存
		setTaskSchedule(taskScheduleBuilder.getSchedule());
	}

	@Override
	protected void doWork(JobDataMap arg0) {
		SpringUtil.getBean(FrontSubmitService.class).clearCacheTask();
	}

}
