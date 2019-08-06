package com.hanweb.dczj.controller.quesbank;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hanweb.common.util.DateUtil;
import com.hanweb.common.util.StringUtil;
import com.hanweb.complat.listener.UserSessionInfo;
import com.hanweb.complat.service.TempFileService;
import com.hanweb.dczj.entity.QuesBankTitleInfo;
import com.hanweb.dczj.entity.TitleInfo;
import com.hanweb.dczj.service.QuesBankTitleInfoService;
import com.hanweb.dczj.service.SettingService;
import com.hanweb.dczj.service.TotalRecoService;
import com.hanweb.support.controller.CurrentUser;

@Controller
@RequestMapping("manager/dczj")
public class QuesBankController {
	
	@Autowired
	private QuesBankTitleInfoService quesBankTitleInfoService;
	@Autowired
	private TempFileService tempFileService;
	@Autowired
	private TotalRecoService totalRecoService;
	@Autowired
	SettingService settingService;
	
		
	/**
	 * 题库头部
	 * @param webid
	 * @return
	 */
	@RequestMapping("quesbanktitle")
	public ModelAndView showTitleList(Integer webid) {
		ModelAndView modelAndView = new ModelAndView("dczj/quesbank/quesbanktitle");
		modelAndView.addObject("webid", webid);
		return modelAndView;
	}
	
	@RequestMapping("questitlejson")
	public ModelAndView logjson(int webid,String quesbankname,int page, int limit) {
		System.out.println("----questitlejson---");
		ModelAndView modelAndView = new ModelAndView("/dczj/quesbank/quesbanktitle_json");
		CurrentUser currentUser = UserSessionInfo.getCurrentUser();
		String username = currentUser.getName();
		quesbankname = StringUtil.getSafeString(quesbankname);
		String json = "";
		String data = "";
		String operation ="";
		int count = 0;
		//List<TitleInfo> infoList = null;
		List<QuesBankTitleInfo> infoList = null;
		if(currentUser.isSysAdmin() && webid == 0) {
			infoList = quesBankTitleInfoService.findInfoList(quesbankname,page,limit);
			count = quesBankTitleInfoService.findNum(quesbankname);
		}else {
			infoList = quesBankTitleInfoService.findInfoListByWebid(webid,quesbankname,page,limit);
			count = quesBankTitleInfoService.findNumByWebid(quesbankname,webid);
		}
		
		if(infoList != null && infoList.size()>0 ) {
			int titlenum = 0;
			for(QuesBankTitleInfo infoEn : infoList) {
				String name = "<span class='table-pic'>"+infoEn.getQuesbankname()+"</span>";
				String creator = infoEn.getCreator();//创建人姓名，这里可以理解为登录的用户名名称
				String createtime = DateUtil.dateToString(infoEn.getCreatetime(), "yyyy-MM-dd HH:mm:ss");
				//这边还要再加个判断，每个人只能对自己创建的题库进行修改和删除，无法修改站点其他人编辑的题库。
				//所以同属一个站点的其他人编辑的题库再列表上显示时，操作区域的“编辑”和“删除”置灰。
				//System.out.println("creator---"+creator);
				//System.out.println("username--"+username);
				try {
					if(creator.equals(username)) {
						//System.out.println("---走上面---");
						 operation = "<a class='select-pic' style='color:blue' onclick='select("+infoEn.getIid()+")'>"+"查看"+"</a>"
										+" | "
										+"<a class='select-pic' style='color:blue' onclick='update("+infoEn.getIid()+")'>"+"编辑"+"</a>"
										+" | "
										+"<a class='select-pic' style='color:blue' onclick='remove_quesbank("+infoEn.getIid()+")'>"+"删除"+"</a>";
					}else {//置为灰色且不可点击
						//System.out.println("---走下面---");
						 operation = "<a class='select-pic' style='color:blue' onclick='select("+infoEn.getIid()+")'>"+"查看"+"</a>"
								 		+" | "
								 		+"<a class='select-pic' style='color:grey'>"+"编辑"+"</a>"
								 		+" | "
								 		+"<a class='select-pic'style='color:grey'>"+"删除"+"</a>";
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				titlenum = totalRecoService.findRecoIsHaveSubmitByDczjid(infoEn.getIid()+"");
				data +="{\"quesbankname\":\""+name+"\","
						+ "\"creator\":\""+creator+"\","
						+ "\"createtime\":\""+createtime+"\","
						+ "\"number\":\""+titlenum+"\","
						+ "\"operation\":\""+operation+"\"},";
			}
		}
		if(StringUtil.isNotEmpty(data)) {
			data = data.substring(0,data.length()-1);
		}
		json = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":["+data+"]}";
		modelAndView.addObject("json", json);
		return modelAndView;
	}
	
	@RequestMapping("quesbankcate_opr")
	public ModelAndView showQuesBankCate(Integer webid) {
		ModelAndView modelAndView = new ModelAndView("dczj/quesbank/quesbankcate_opr");
		modelAndView.addObject("webid", webid);
		return modelAndView;
	}
	
}
