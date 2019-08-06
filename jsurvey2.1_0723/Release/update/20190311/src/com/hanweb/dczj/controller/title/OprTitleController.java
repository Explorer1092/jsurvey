package com.hanweb.dczj.controller.title;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.TitleInfoService;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/dczj")
public class OprTitleController {

	@Autowired
	private TitleInfoService titleInfoService;
	@Autowired
	private WebSiteService webSiteService;
	
	/**
	 * 在线调查新增
	 * @param webId
	 * @return
	 */
	@RequestMapping("add_show")
	public ModelAndView showAdd(Integer webid,Integer type) {
		ModelAndView model = new ModelAndView("dczj/title/title_opr");
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		DCZJ_WebSite website = webSiteService.findByIid(webid);
		String webName = website.getName();
		TitleInfo infoEn = new TitleInfo();
		infoEn.setUsername(currentUser.getName());
		infoEn.setType(type);
		model.addObject("webid", webid);
		model.addObject("webName", webName);
		model.addObject("infoEn", infoEn);
		model.addObject("dczjid", 0);
		return model;
	}
	
	
	/**
	 * 在线调查新增
	 * @param webId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("add_submit")
	public JsonResult submitAdd(Integer webid,String titlename,int type,String username) {
		JsonResult jsonResult = JsonResult.getInstance();
		titlename = StringUtil.getSafeString(titlename);
		username = StringUtil.getSafeString(username);
		if(StringUtil.isEmpty(titlename)) {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("标题不能为空");
			return jsonResult;
		}
		TitleInfo infoEn = new TitleInfo();
		infoEn.setWebid(webid);
		infoEn.setUsername(username);
		infoEn.setCreatetime(new Date());
		infoEn.setTitlename(titlename);
		infoEn.setState(0);
		infoEn.setIspublish(0);
		infoEn.setIsdelete(0);
		infoEn.setType(type);
		infoEn.setOrderid(0);
		int iid = titleInfoService.add(infoEn);
		if(iid >0 ) {
			jsonResult.addParam("type", type);
			jsonResult.set(ResultState.ADD_SUCCESS);
		}else {
			jsonResult.set(ResultState.ADD_FAIL);
		}
		return jsonResult;
	}
	
	/**
	 * 在线调查新增
	 * @param webId
	 * @return
	 */
	@RequestMapping("titlemodifyshow")
	public ModelAndView showTitleModify(Integer dczjid) {
		ModelAndView model = new ModelAndView("dczj/title/title_opr");
		TitleInfo infoEn = titleInfoService.getEntity(dczjid);
		int webid = infoEn.getWebid();
	    DCZJ_WebSite website = webSiteService.findByIid(webid);
	    String webName = website.getName();
		String typename = "";
		if(infoEn.getType() == 0) {
			typename = "调查";
		}else if(infoEn.getType() == 1) {
			typename = "征集";
		}else if(infoEn.getType() == 2) {
			typename = "投票";
		}
		model.addObject("webid", webid);
		model.addObject("webName", webName);
		model.addObject("dczjid", dczjid);
		model.addObject("infoEn", infoEn);
		model.addObject("typename", typename);
		return model;
	}
	
	
	@ResponseBody
	@RequestMapping("titlemodifysubmit")
	public JsonResult submitmodify(String titlename,Integer dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		titlename= StringUtil.getSafeString(titlename);
		if(StringUtil.isEmpty(titlename)) {
			jsonResult.setSuccess(false);
			jsonResult.setMessage("标题不能为空");
			return jsonResult;
		}
		TitleInfo infoEn = titleInfoService.getEntity(dczjid);
		infoEn.setTitlename(titlename);
		Boolean bl = titleInfoService.modify(infoEn);
		if(bl) {
			titleInfoService.setUpdateHtml(dczjid+"", 0);
		}
		return bl ? jsonResult.set(ResultState.MODIFY_SUCCESS) : jsonResult.set(ResultState.MODIFY_FAIL);
	}
	
	/**
	 * 排序
	 * @param webid
	 * @param type
	 * @param dczjId
	 * @param sorttype
	 * @return
	 */
	@RequestMapping("sort")
	@ResponseBody
	public JsonResult sort(int webid,int type,int dczjId,int sorttype) {
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(false);
		List<Integer> dczjIdList = titleInfoService.findDczjIdByWebIdAndType(webid,type);
		
		if(dczjIdList != null && dczjIdList.size()>0){
			for(int i = 0; i<= dczjIdList.size() - 1; i++){
				if(dczjIdList.get(i) == dczjId){
					if(sorttype == 1){
						if(i > 0){
							int sortDczjId = dczjIdList.get(i-1);
							dczjIdList.set(i, sortDczjId);
							dczjIdList.set(i-1, dczjId);
							break;
						}else{
							return jsonResult.setMessage("已经排在第一位！");
						}
					}else if(sorttype == 2){
						if(i < dczjIdList.size() - 1){
							int sortDczjId = dczjIdList.get(i+1);
							dczjIdList.set(i, sortDczjId);
							dczjIdList.set(i+1, dczjId);
							break;
						}else{
							return jsonResult.setMessage("已经排在最后一位！");
						}
					}
				}
			}
		}
//		String[][] strData = titleInfoService.findMinOrder(dczjIdList,type);
		int nOrderID = 0;
//		if (strData != null && strData.length > 0)
//			nOrderID = NumberUtil.getInt(strData[0][0]);
		
		int nLen = dczjIdList.size();
		for (int j = 0; j < nLen; j++) {
			boolean bl = titleInfoService.modifyOrder(dczjIdList.get(j),nOrderID);
			nOrderID++;
			if(!bl){
				return jsonResult.setMessage("操作失败，请联系管理员！");
			}
		}
		return jsonResult.setSuccess(true);
	}
	
	/**
	 *  删除表
	 * @param answId
	 * @return
	 */
	@RequestMapping("remove")
	@ResponseBody
	public JsonResult remove(int dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean bl = titleInfoService.delete(dczjid);
		if(bl){
			titleInfoService.setUpdateHtml(dczjid+"", 0);
		}
		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult.set(ResultState.REMOVE_FAIL);
	}
}
