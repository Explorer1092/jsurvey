package com.hanweb.complat.task;

import org.quartz.JobDataMap;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.common.util.log.LogWriter;
import com.hanweb.complat.service.TempFileService;

/**
 * 临时文件清除
 * 
 * @author 李杰
 *
 */
public class TempFileTask extends BaseTask {
	@Override
	protected void config() {
		setTaskId("clean_temp_file");
		setTaskName("临时文件清除");
		setStartNow(true);
		TaskScheduleBuilder taskScheduleBuilder = TaskScheduleBuilder.getInstance();
		taskScheduleBuilder.setHour("1");
		setTaskSchedule(taskScheduleBuilder.getSchedule());
	}

	@Override
	protected void doWork(JobDataMap dataMap) {
		LogWriter.debug("清除暂存附件开始...");
		try {
			if (BaseInfo.isPrepared()) {
				SpringUtil.getBean("complat_TempFileService", TempFileService.class).removeYesterday();
			}
		} catch (Exception e) {
			LogWriter.warn("清除暂存附件异常", e);
		}
	}
}
