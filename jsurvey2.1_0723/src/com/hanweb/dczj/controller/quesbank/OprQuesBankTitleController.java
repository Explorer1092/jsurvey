package com.hanweb.dczj.controller.quesbank;

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
import com.hanweb.common.util.mvc.Script;
import com.hanweb.common.view.tree.Tree;
import com.hanweb.common.view.tree.TreeNode;
import com.hanweb.complat.entity.Role;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.dczj.entity.DCZJ_WebSite;
import com.hanweb.dczj.entity.QuesBankTitleInfo;
import com.hanweb.dczj.service.QuesBankTitleInfoService;
import com.hanweb.dczj.service.WebManagerService;
import com.hanweb.dczj.service.WebSiteService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/dczj")
public class OprQuesBankTitleController {

	@Autowired
	private QuesBankTitleInfoService quesBankTitleInfoService;
	@Autowired
	private WebSiteService webSiteService;
	@Autowired
	WebManagerService webManagerService;
	
	/**
	 * 题库新增页面
	 * @param webId
	 * @return
	 */
	@RequestMapping("addquesbank_show")
	public ModelAndView showAdd(Integer webid) {
		ModelAndView model = new ModelAndView("dczj/quesbank/quesbanktitle_opr");
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		DCZJ_WebSite website = webSiteService.findByIid(webid);
		String webName = website.getName();
		QuesBankTitleInfo infoEn = new QuesBankTitleInfo();
		infoEn.setUsername(currentUser.getName());
		String username = currentUser.getName();
		String creator = username;
		model.addObject("webid", webid);
		model.addObject("username", username);
		model.addObject("creator", creator);
		model.addObject("webName", webName);
		model.addObject("infoEn", infoEn);
		model.addObject("dczjid", 0);
		return model;
	}
	
	
	/**
	 * 题库新增提交操作跳转
	 * @param webId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addquesbank_submit")
	public JsonResult submitAdd(Integer webid,String quesbankname,String username,String creator) {
		JsonResult jsonResult = JsonResult.getInstance();
		Script script = Script.getInstanceWithJsLib();
		String message = "";
		quesbankname = StringUtil.getSafeString(quesbankname);
		username = StringUtil.getSafeString(username);
		creator = StringUtil.getSafeString(creator);
		List<QuesBankTitleInfo> quesbanktitleInfos = quesBankTitleInfoService.findTitleByWebId(webid);
		if(quesbanktitleInfos!=null && quesbanktitleInfos.size()>0) {
			for (QuesBankTitleInfo quesbanktitleInfo : quesbanktitleInfos) {
				if(quesbankname.equals(quesbanktitleInfo.getQuesbankname())) {
					jsonResult.setSuccess(false);
					jsonResult.setMessage("题库名称不能相同");
					return jsonResult;
				}	
			}
		}
		QuesBankTitleInfo infoEn = new QuesBankTitleInfo();
		infoEn.setWebid(webid);
		infoEn.setUsername(username);
		infoEn.setCreator(creator);
		infoEn.setCreatetime(new Date());
		infoEn.setQuesbankname(quesbankname);
		infoEn.setIsdelete(0);
		infoEn.setOrderid(0);
		int iid = quesBankTitleInfoService.add(infoEn);
		if(iid >0 ) {
//tip:解决一下导航栏会消失的情况
//		script.addScript("parent.refreshParentWindow();parent.closeDialog();");
//		script.addScript("parent.location.href = 'https://www.baidu.com'");
//script.addScript("alert('123');");
		jsonResult.set(ResultState.ADD_SUCCESS);
		}else {
			message = "添加失败!";
			script.addAlert(message);
		}
		if(webid==null||webid==0) {
			message = "站点不能为空!";
			script.addAlert(message);
		}
		return jsonResult;

	}
	
	//进入题库分类卡片列表,记得绑定
	@RequestMapping("show_quesbankcate")
	public ModelAndView showQuesBankCate() {
		System.out.println("-----show_quesbankcate----");
		ModelAndView mav = new ModelAndView("dczj/quesbank/quesbankcate");
		System.out.println("-----打开show_quesbankcate----");
		//导航栏，站点选择等
		CurrentUser user = UserSessionInfo.getCurrentUser();
		String rangeName = user.getRangeName();
		List<Role> currentRoles = user.getRoleList();
		rangeName = "";
		boolean ishave = false;// 判断树是否生成
		int surveynum = 0;
		String webName = "";		
		int isSysManager = 0;
		// 组织树
		Tree tree = Tree.getInstance("webid", "webName");
		
		List<DCZJ_WebSite> webList = null;
		for (Role role : currentRoles) {
			switch (role.getIid()) {
			case 1: //系统管理员
				System.out.println("---case1111---");
				String nodeName = StringUtil.isEmpty(rangeName) ? "站点选择" : rangeName;
				webList = webSiteService.findChildWebByIid(0);
				
				tree.addNode(TreeNode.getInstance(0 + "", "", nodeName));
				for (DCZJ_WebSite web : webList) {
					tree.addNode(TreeNode.getInstance(web.getIid() + "", 0 + "", web.getName(),"/manager/dczj/quesbankcate_opr.do", web.getIsParent(), false));
				}
				surveynum++;
				isSysManager=1;
				break;
			case 2://网站管理员
				if(surveynum == 0){
					System.out.println("----case2----");
					List<Integer> webIdsList = webManagerService.findWebIdsByUserId(user.getIid());
					tree.addNode(TreeNode.getInstance(0 + "", "", "站点选择",""));
					for (Integer webid : webIdsList) {
						webName = webSiteService.findWebNameByWebId(webid);
						tree.addNode(TreeNode.getInstance(webid + "", 0 + "", webName,"/manager/dczj/quesbankcate_opr.do", false, false));
					}
				}
				ishave = true;// 判断树是否已经生成
				break;
				
			}
			if (ishave) {// 避免生成2次树
				break;
			}
		}

		mav.addObject("tree", tree.parse());
		mav.addObject("webname", "站点选择");
		mav.addObject("user", user);
		mav.addObject("isSysManager", isSysManager);
		return mav;		
	}
	
	
	/**
	 *  删除一条记录
	 * @param answId
	 * @return
	 */
	@RequestMapping("remove_quesbank")
	@ResponseBody
	public JsonResult remove(int dczjid) {
		JsonResult jsonResult = JsonResult.getInstance();
		boolean bl = quesBankTitleInfoService.delete(dczjid);
		return bl ? jsonResult.set(ResultState.REMOVE_SUCCESS) : jsonResult.set(ResultState.REMOVE_FAIL);
	}

}
