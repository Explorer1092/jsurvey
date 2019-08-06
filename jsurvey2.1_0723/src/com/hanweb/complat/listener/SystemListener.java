package com.hanweb.complat.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.task.TaskManager;
import com.hanweb.complat.task.OperationLogTask;
import com.hanweb.complat.task.ResetPwdTask;
import com.hanweb.complat.task.TempFileTask;
import com.hanweb.jis.expansion.webservice.Constants;

/**
 * 平台监听
 * 
 * @author 李杰
 * 
 */
public class SystemListener extends ContextLoaderListener implements ServletContextListener {
	/**
     * 
     */
	protected final Log logger = LogFactory.getLog(SystemListener.class);

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// 初始化jis
		Constants.setSysPath(BaseInfo.getRealPath());
		// 判断是否已经注册并初始化过
		if (BaseInfo.isPrepared()) {
			initApplication();
		}
	}

	/**
	 * initApplication:(具体应用需要初始化的内容在此实现).
	 */
	public void initApplication() {
		TaskManager.addTask(new TempFileTask(), new OperationLogTask(), new ResetPwdTask());
	}
}
