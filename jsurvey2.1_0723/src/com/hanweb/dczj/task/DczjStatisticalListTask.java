package com.hanweb.dczj.task;

import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.dczj.service.DczjStatisticalListTaskService;

public class DczjStatisticalListTask extends BaseTask{

	@Override
	protected void config() {
		setTaskId("dczjlisttask");
		setTaskName("前台调查征集列表页缓存线程");
		setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(30));
		
	}

	@Override
	protected void doWork(JobDataMap var1) {
		DczjStatisticalListTaskService dczjStatisticalListTaskService = SpringUtil.getBean(DczjStatisticalListTaskService.class);
		dczjStatisticalListTaskService.dczjListCache();
		dczjStatisticalListTaskService.getDczjListHtml();
		
	}

}
