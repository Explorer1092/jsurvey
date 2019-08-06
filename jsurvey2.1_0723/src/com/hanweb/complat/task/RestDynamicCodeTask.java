package com.hanweb.complat.task;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobDataMap;

import com.hanweb.common.task.BaseTask;
import com.hanweb.common.task.TaskScheduleBuilder;
import com.hanweb.common.util.SpringUtil;
import com.hanweb.complat.service.EmailService;

/**
 * 重置用户动态验证码密钥
 * 
 * @author 李杰
 *
 */
public class RestDynamicCodeTask extends BaseTask {

	public static final String TASKID = "resetpwd_DynamicCodeTask";

	@Override
	protected void config() {
		setTaskId(TASKID);
		setTaskName("重置用户动态验证码密钥");
		setTaskSchedule(TaskScheduleBuilder.getOnceSchedule());
	}

	@Override
	protected void doWork(JobDataMap dataMap) {
		String ids = dataMap.getString("ids");
		EmailService emailService = SpringUtil.getBean(EmailService.class);
		List<Integer> idList = emailService.findUserIdHasEmail(ids);
		if (CollectionUtils.isNotEmpty(idList)) {
			for (Integer id : idList) {
				emailService.modifyDynamicCodeAndSendEmail(id);
			}
		}
	}
}
