package com.hanweb.complat.task;

import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.support.controller.resetpwd.ResetPwdCache;

/**
 * 检测密码重置有效期
 * 
 * @author 李杰
 *
 */
public class ResetPwdTask extends BaseTask {

	@Override
	protected void config() {
		setTaskId("resetpwd_checker");
		setTaskName("检测密码重置有效期");
		setTaskSchedule(TaskScheduleBuilder.getEveryMinuteSchedule(1));
	}

	@Override
	protected void doWork(JobDataMap dataMap) {
		ResetPwdCache.removeExpiratoin();
	}
}
