package com.hanweb.dczj.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.task.TaskManager;
import com.hanweb.dczj.task.ClearCacheTask;
import com.hanweb.dczj.task.CountTask;
import com.hanweb.dczj.task.DczjStatisticalListTask;
import com.hanweb.dczj.task.FrontPageTask;
import com.hanweb.dczj.task.OtherAnswTask;
import com.hanweb.dczj.task.RegisterMqListenerTask;
import com.hanweb.dczj.task.SubmitTask;
import com.hanweb.dczj.task.TimingOverTask;
import com.hanweb.dczj.task.TimingPublishTask;
import com.hanweb.jis.expansion.webservice.Constants;

public class DczjListener extends ContextLoaderListener implements ServletContextListener{
	/**
     * 
     */
    protected final Log logger = LogFactory.getLog(DczjListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 判断是否已经注册并初始化过
        if(BaseInfo.isPrepared()){
          	Constants.setSysPath(BaseInfo.getRealPath());
            dczjTask();
        }
    }

	private void dczjTask() {
		TaskManager.addTask(new CountTask(), new ClearCacheTask(), new SubmitTask(), new TimingPublishTask(),
				new TimingOverTask(),new OtherAnswTask(),new FrontPageTask(),new RegisterMqListenerTask(),
				new DczjStatisticalListTask());
	}
}
