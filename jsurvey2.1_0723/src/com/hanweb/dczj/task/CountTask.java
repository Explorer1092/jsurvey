package com.hanweb.dczj.task;

import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskManager;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.task.TaskState;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.dczj.service.CountService;

public class CountTask extends BaseTask{

	@Override
	protected void config() {
		setTaskId("dczj_count");
		setTaskName("调查征集统计线程");
		setCanRemove(false);
		setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(30));	
	}
	
	@Override
	protected void doWork(JobDataMap dataMap) {
		TaskState countTaskState = TaskManager.getTaskState("dczj_count");
		if((countTaskState!=TaskState.RUNNING || countTaskState!=TaskState.WATTING || countTaskState!=TaskState.PAUSED)){
			CountService countService = SpringUtil.getBean(CountService.class);
			countService.count();
		}
	}
	
}
