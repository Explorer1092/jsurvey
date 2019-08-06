package com.hanweb.complat.controller.onlineuser;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hanweb.common.annotation.Permission;
import com.hanweb.common.util.mvc.JsonResult;
import com.hanweb.complat.listener.OnlineUserListener;

/**
 * 在线用户操作控制器
 * 
 * @author ZhangC
 * 
 */
@Controller
@Permission(module = "onlineuser")
@RequestMapping("manager/onlineuser")
public class OprOnlineUserController {

	/**
	 * 踢除用户
	 * 
	 * @param sessionId
	 * @return
	 */
	@Permission(function = "user_kick")
	@RequestMapping("user_kick")
	@ResponseBody
	public JsonResult kickUser(String sessionId) {

		JsonResult jsonResult = JsonResult.getInstance();
		try {
//			for (CurrentUser onlineUser : onlineUserList) {
//				if (sessionId.equals(onlineUser.getSessionId())) {
//					onlineUser.getSession().invalidate(); // 踢除用户
//					break;
//				}
//			}
			HttpSession session = OnlineUserListener.USERSESSIONS.get(sessionId);
			if(session != null){
				session.invalidate();
			}
			jsonResult.setSuccess(true);
			jsonResult.setMessage("onlineuser.kick.success");
		} catch (Exception e) {
			jsonResult.setMessage("onlineuser.kick.fail");
		}
		return jsonResult;
	}
}
