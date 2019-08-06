package com.hanweb.dczj.controller.website;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.NumberUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.exception.OperationException;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;


/**
 * 网站操作控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@RequestMapping("manager/website")
public class OprWebController {

	@Autowired
	private WebSiteService webSiteService;
	
	/**
	 * 显示新增网站页面
	 * 
	 * @param pid
	 *            父网站ID
	 * @return
	 */
	@Permission(function = "add_show")
	@RequestMapping(value = "add_show")
	public ModelAndView showAdd(Integer pid) {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		int parentWebId = NumberUtil.getInt(pid);
		String parentWebName = "";
		
		DCZJ_WebSite web = new DCZJ_WebSite();
		if (parentWebId > 0) {
			DCZJ_WebSite parentweb = webSiteService.findByIid(parentWebId);
			parentWebName = parentweb.getName();
		}
		
		web.setPid(parentWebId);
		web.setPname(parentWebName);

		ModelAndView modelAndView = new ModelAndView("dczj/website/web_opr");
		modelAndView.addObject("url", "add_submit.do");
		modelAndView.addObject("web", web);
		modelAndView.addObject("noremoveRoleIds", "null");
		modelAndView.addObject("rangeId", currentUser.getRangeId());
		modelAndView.addObject("rangeName", currentUser.getRangeName());
		return modelAndView;
	}
	
	/**
	 * 显示编辑网站页面
	 * 
	 * @param iid
	 *            网站ID
	 * @return
	 */
	@Permission(function = "modify_show")
	@RequestMapping(value = "modify_show")
	public ModelAndView showModify(int iid) {
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		DCZJ_WebSite web = webSiteService.findByIid(iid);

		ModelAndView modelAndView = new ModelAndView("dczj/website/web_opr");
		modelAndView.addObject("url", "modify_submit.do");
		modelAndView.addObject("web", web);
		modelAndView.addObject("noremoveRoleIds", "null");
		modelAndView.addObject("rangeId", currentUser.getRangeId());
		modelAndView.addObject("rangeName", currentUser.getRangeName());
		return modelAndView;
	}

	/**
	 * 新增网站
	 * 
	 * @param web
	 *            网站实体
	 * @return
	 */
	@Permission(function = "add_submit")
	@RequestMapping(value = "add_submit")
	@ResponseBody
	public JsonResult submitAdd(WebSiteFormBean web) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = webSiteService.add(web);
			if (isSuccess) {
				jsonResult.set(ResultState.MODIFY_SUCCESS);
				
				String userIp = IpUtil.getIp();
				String [] ips = userIp.split(",");
				if (ips != null && ips.length > 0){ //防止双IP
				    userIp = StringUtil.getString(ips[0]);
				}
				CurrentUser currentUser = UserSessionInfo.getCurrentUser();
				LogRecorder.record(LogEntity.getInstance().setModelName("网站管理").setFunctionName("新增")
						.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("新增网站，网站ID："+web.getIid()+"，网站名："+web.getName()));
				
				jsonResult.set(ResultState.ADD_SUCCESS);
				jsonResult.addParam("refresh", NumberUtil.getInt(web.getPid()) + "");
			} else {
				jsonResult.set(ResultState.ADD_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}
	
	/**
	 * 编辑网站
	 * 
	 * @param group
	 *            网站实体
	 * @return
	 */
	@Permission(function = "modify_submit")
	@RequestMapping(value = "modify_submit")
	@ResponseBody
	public JsonResult submitModify(WebSiteFormBean web) {
		boolean isSuccess = false;
		JsonResult jsonResult = JsonResult.getInstance();
		try {
			isSuccess = webSiteService.modify(web);
			if (isSuccess) {
				if (NumberUtil.getInt(web.getPrevPid()) != NumberUtil.getInt(web.getPid())) {
					jsonResult.addParam("remove", NumberUtil.getInt(web.getIid()) + "");
				}
				String userIp = IpUtil.getIp();
				String [] ips = userIp.split(",");
				if (ips != null && ips.length > 0){ //防止双IP
				    userIp = StringUtil.getString(ips[0]);
				}
				CurrentUser currentUser = UserSessionInfo.getCurrentUser();
				LogRecorder.record(LogEntity.getInstance().setModelName("网站管理").setFunctionName("修改")
						.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("修改网站，网站ID："+web.getIid()+"，网站名："+web.getName()));
				
				jsonResult.addParam("refresh", NumberUtil.getInt(web.getPid()) + "");
				jsonResult.set(ResultState.MODIFY_SUCCESS);
			} else {
				jsonResult.set(ResultState.MODIFY_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}
	
	
	/**
	 * 删除网站
	 * 
	 * @param ids
	 *            网站ID串 如:1,2,3,4
	 * @param pid
	 *            父网站ID
	 * @return
	 */
	@Permission(function = "remove")
	@RequestMapping(value = "remove")
	@ResponseBody
	public JsonResult remove(String ids, String pid) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean isSuccess = false;
		try {
			if(!isSuccess){
				if(ids!=null&&ids.length()>0){
					String[] idsDelete = ids.split(",");
					for(int ide=0;ide<idsDelete.length;ide++){
						int webid = NumberUtil.getInt(idsDelete[ide]);
						isSuccess = webSiteService.checkSubJsurvey(webid);
						DCZJ_WebSite en = webSiteService.findByIid(webid);
						if(!isSuccess) {
							jsonResult.setSuccess(false);
							jsonResult.setMessage("网站"+en.getName()+"下存在问卷，请先删除!");
							return jsonResult;
						}
						String userIp = IpUtil.getIp();
						String [] ips = userIp.split(",");
						if (ips != null && ips.length > 0){ //防止双IP
						    userIp = StringUtil.getString(ips[0]);
						}
						CurrentUser currentUser = UserSessionInfo.getCurrentUser();
						String userName = currentUser.getName();
						LogRecorder.record(LogEntity.getInstance().setModelName("网站管理").setFunctionName("删除")
								.setIpAddr(userIp).setLogUser(userName+"("+currentUser.getLoginName()+")").setDescription("删除网站，网站ID："+en.getIid()+"，网站名："+en.getName()));
					}
				}
			}
			isSuccess = webSiteService.removeByIds(ids);
			if (isSuccess) {
				jsonResult.set(ResultState.REMOVE_SUCCESS);
				jsonResult.addParam("remove", ids);
			} else {
				jsonResult.set(ResultState.REMOVE_FAIL);
			}
		} catch (OperationException e) {
			jsonResult.setMessage(e.getMessage());
		}
		return jsonResult;
	}
	
	
}
