package com.hanweb.dczj.controller.datacall;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.common.util.mvc.ResultState;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.entity.DataCall;
import com.hanweb.dczj.entity.DataCallTemplate;
import com.hanweb.dczj.service.DataCallService;
import com.hanweb.dczj.service.DataCallTempService;
import com.hanweb.dczj.service.WebManagerService;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.log.LogRecorder;
import com.hanweb.log.entity.LogEntity;
import com.hanweb.support.controller.CurrentUser;


@Controller
@RequestMapping("manager/datacall")
public class OprDataCallController {
	
	@Autowired
	private WebSiteService webSiteService;
	@Autowired
	private DataCallService dataCallService;
	@Autowired
	private WebManagerService webManagerService;
	@Autowired
	private DataCallTempService dataCallTempService;
	
	@RequestMapping("add_show")
	public ModelAndView showAdd(Integer webId) {
		ModelAndView modelAndView = new ModelAndView("/dczj/datacall/datacall_opr");
		DataCall en = new DataCall();
		en.setWebid(webId);
		en.setState(0);
		en.setUpdatehtml(0);
		en.setDatacall_type(0);
		en.setDatacall_sorttype(0);
		en.setDatacall_sort(0);
		en.setDatacall_types("0");
		modelAndView.addObject("en", en);
		modelAndView.addObject("url", "add_submit.do");
		return modelAndView;
	}
	
	@RequestMapping("add_submit")
	@ResponseBody
	public JsonResult submitAdd(DataCall en) {
		
		JsonResult jsonResult = JsonResult.getInstance();
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		if(StringUtil.isEmpty(en.getDatacall_types())){
			en.setDatacall_types("0,1,2");
		}
		en.setCreatename(currentUser.getName());
		en.setCreatedate(new Date());
		int iid = dataCallService.add(en);
		if(iid > 0){
			jsonResult.set(ResultState.ADD_SUCCESS);
			String userIp = IpUtil.getIp();
			String [] ips = userIp.split(",");
			if (ips != null && ips.length > 0){ //防止双IP
			    userIp = StringUtil.getString(ips[0]);
			}
			LogRecorder.record(LogEntity.getInstance().setWebName(en.getWebid()+"").setModelName("数据调用").setFunctionName("新增")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("新增数据调用，标题："+en.getDatacall_name()));
		}else{
			jsonResult.set(ResultState.ADD_FAIL);
		}
		return jsonResult;
	}
	
	/**
	 * 生成网站树
	 * @param jact_webids
	 * @return
	 */
	@RequestMapping(value = "selectWeb")
	@ResponseBody
	public ModelAndView grouplimit(String jact_webids){
		ModelAndView modelAndView = new ModelAndView("dczj/datacall/datacall_selectweb_tree");
		jact_webids = StringUtil.getSafeString(jact_webids);
		Tree tree = Tree.getInstance("treeNodeId", "nodeName");
		
		List<DCZJ_WebSite> websiteList = null;
		jact_webids = ","+jact_webids+",";
		boolean isExist = false;
		boolean ishave = false;// 判断树是否生成
		
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		List<Role> currentRoles = currentUser.getRoleList();
		int jactnum = 0;
		for (Role role : currentRoles) {
			switch (role.getIid()) {
			case 1:
				websiteList =webSiteService.findChildWebByIid(0);
				tree.addNode(TreeNode.getInstance("0", "", "网站管理",true,true).setNocheck(true));
				for(DCZJ_WebSite website : websiteList){
					String iid = StringUtil.getString(website.getIid());
					String name = website.getName();
					String pId = StringUtil.getString(website.getPid());
					String ids = ","+iid+",";
					isExist = jact_webids.contains(ids);
					if(isExist){
						tree.addNode(TreeNode.getInstance(iid, pId, name,website.getIsParent(),false).setChecked(true));
					}else{
						tree.addNode(TreeNode.getInstance(iid, pId, name,website.getIsParent(),false).setChecked(false));
					}
				}
				jactnum++;
				break;
			case 3:
				if(jactnum == 0){
					List<Integer> webIdsList = webManagerService.findWebIdsByUserId(currentUser.getIid());
					tree.addNode(TreeNode.getInstance("0", "", "网站管理",true,true).setNocheck(true));
					for (Integer webid : webIdsList) {
						String webName = webSiteService.findWebNameByWebId(webid);
						String ids = ","+webid+",";
						isExist = jact_webids.contains(ids);
						if(isExist){
							tree.addNode(TreeNode.getInstance(webid + "", 0+"", webName,false,false).setChecked(true));
						}else{
							tree.addNode(TreeNode.getInstance(webid + "", 0+"", webName,false,false).setChecked(false));
						}
					}
				}
				ishave = true;// 判断树是否已经生成
				break;
			}
			if (ishave) {// 避免生成2次树
				break;
			}
		}
		
		modelAndView.addObject("jact_webids", jact_webids);
		modelAndView.addObject("url", "webistetree_submit.do");
		modelAndView.addObject("tree", tree.parse());
		return modelAndView;
	}

	/**
	 * 菜单树设置提交保存
	 * @return
	 */
	@RequestMapping("webistetree_submit")
	@ResponseBody
	public JsonResult submitMenuTree(){
		JsonResult jsonResult = JsonResult.getInstance();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	/**
	 * 异步加载机构树
	 * 
	 * @param groupId
	 *            网站ID
	 * @param isDisabled
	 *            是否可选<br>
	 *            true 可选<br>
	 *            false 不可选
	 * @return
	 */
	@RequestMapping("menuforgroupsearch")
	@ResponseBody
	public String searchAsyncMenu(int webId, String isDisabled,String jact_webids) {

		// 组织树
		Tree tree = Tree.getInstance("treeNodeId", "nodeName");
		jact_webids = StringUtil.getSafeString(jact_webids);
		List<DCZJ_WebSite> websiteList = webSiteService.findChildWebByIid(webId);

		boolean isExist = false;
		jact_webids = StringUtil.getSafeString(jact_webids);
		for (DCZJ_WebSite website : websiteList) {
			String iid = ","+StringUtil.getString(website.getIid())+",";
			if(StringUtil.isNotEmpty(jact_webids)){
				isExist = jact_webids.contains(iid);
			}
			if(isExist){
				tree.addNode(TreeNode.getInstance("" + website.getIid(), "" + website, website.getName(),
						website.getIsParent(), false).setChecked(true));
			}else{
				tree.addNode(TreeNode.getInstance("" + website.getIid(), "" + website, website.getName(),
						website.getIsParent(), false).setChecked(false));
			}
			
		}
		return tree.parse();
	}
	
	/**
	 * 在线调查修改
	 * @param i_id
	 * @return
	 */
	@RequestMapping("modify_show")
	public ModelAndView showModify(int i_id) {
		ModelAndView model = new ModelAndView("dczj/datacall/datacall_opr");
		DataCall en = dataCallService.findJactByIid(i_id);
		DataCallTemplate template = dataCallTempService.findTemplate(i_id+"","");
		if(template != null){
			model.addObject("isTemp", 1);
		}
		model.addObject("en", en);
		model.addObject("url", "modify_submit.do");
		return model;
	}
	
	@RequestMapping("modify_submit")
	@ResponseBody
	public JsonResult modifysubmit(DataCall en) {
		JsonResult jsonResult = JsonResult.getInstance();
		if(StringUtil.isEmpty(en.getDatacall_types())){
			en.setDatacall_types("0,1,2");
		}
		boolean bl = dataCallService.modifyEntity(en);
		if(bl){
			dataCallService.setUpdateHtml(en.getIid(),0);
			CurrentUser currentUser = UserSessionInfo.getCurrentUser();
			jsonResult.set(ResultState.MODIFY_SUCCESS);
			String userIp = IpUtil.getIp();
			String [] ips = userIp.split(",");
			if (ips != null && ips.length > 0){ //防止双IP
			    userIp = StringUtil.getString(ips[0]);
			}
			LogRecorder.record(LogEntity.getInstance().setWebName(en.getWebid()+"").setModelName("数据调用").setFunctionName("编辑")
					.setIpAddr(userIp).setLogUser(currentUser.getName()+"("+currentUser.getLoginName()+")").setDescription("编辑数据调用，标题："+en.getDatacall_name()));
		}else{
			jsonResult.set(ResultState.MODIFY_FAIL);
		}
		return jsonResult;
	}
	/**
	 * 删除在线调查列表
	 * @param ids
	 * @return
	 */
	@RequestMapping("remove")
	@ResponseBody
	public JsonResult remove(String ids) {
		JsonResult jsonResult = JsonResult.getInstance();
		ids = StringUtil.getSafeString(ids);
		List<Integer> idList = StringUtil.toIntegerList(ids);
		boolean bl = dataCallService.delete(idList);// 删除调查
		if (bl) {
			for (Integer idcount : idList) {
				String loadPath = BaseInfo.getRealPath() + "/jsurvey/list/index_"+ idcount + ".html";
				FileUtil.deleteFile(loadPath);	
			}	
		}
		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult.set(ResultState.REMOVE_FAIL);
	}
}
