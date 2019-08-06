package com.hanweb.support.controller.resetpwd;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.VerifyCode;
import com.hanweb.common.validation.ValidationException;
import com.hanweb.common.validation.ValidationUtil;
import com.hanweb.complat.entity.User;
import com.hanweb.complat.service.UserService;
import com.hanweb.support.service.ResetPwdService;
/**
 * 重置密码
 * @author 李杰
 *
 */
@Controller("resetpwd")
@RequestMapping("resetpwd")
public class ResetpwdController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ResetPwdService resetPwdService;
	
	/**
	 * 打开重置密码页面
	 * @return
	 */
	@RequestMapping("show")
	public ModelAndView show(){
		ModelAndView modelAndView = new ModelAndView("support/resetpwd/resetpwd_opr");
		modelAndView.addObject("url", "submit.do");
		return modelAndView;
	}
	
	/**
	 * 提交
	 * @return
	 */
	@RequestMapping("submit")
	public ModelAndView sbumit(ResetPwd resetPwd, HttpSession session, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("resetpwd", resetPwd);
		try {
			ValidationUtil.validation(resetPwd);
			String sessionVcode = (String) session.getAttribute("resetpwd_code");
			session.removeAttribute("resetpwd_code");
			if(!StringUtil.equals(resetPwd.getVerifycode().toLowerCase(), sessionVcode)){
				throw new ValidationException("验证码错误");
			}
			User user = userService.findByLoginName(resetPwd.getName());
			if(user == null){
				throw new ValidationException("用户名不存在");
			}
			String email = user.getEmail();
			if(StringUtil.isEmpty(email)){
				throw new ValidationException("此用户未设置email，请联系管理员修改密码");
			}
			if(!StringUtil.equals(email, resetPwd.getEmail())){
				throw new ValidationException("email不匹配");
			}
			String token = StringUtil.getUUIDString();
			resetPwd.setToken(token);
			resetPwdService.sendResetPwdEmail(resetPwd);
			modelAndView.setView(new RedirectView("result.do"));
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", -10);
		} catch (Exception e) {
			modelAndView.setViewName("forward:show.do");
			modelAndView.addObject("message", e.getMessage());
		}
		return modelAndView;
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("result")
	public ModelAndView result(){
		ModelAndView modelAndView = new ModelAndView("support/resetpwd/resetpwd_result");
		modelAndView.addObject("success", true);
		return modelAndView;
	}
	
	/**
	 * 开始重置密码
	 * @return
	 */
	@RequestMapping("change")
	public ModelAndView change(DoResetPwd doResetPwd){
		ModelAndView modelAndView = new ModelAndView("support/resetpwd/resetpwd_changepwd");
		modelAndView.addObject("url", "change_submit.do");
		ResetPwdCache resetPwdCache = ResetPwdCache.get(doResetPwd.getToken());
		modelAndView.addObject("resetPwdCache", resetPwdCache);
		modelAndView.addObject("doResetPwd", doResetPwd);
		return modelAndView;
	}
	
	/**
	 * 提交重置
	 * @return
	 */
	@RequestMapping("change_submit")
	public ModelAndView submitChange(DoResetPwd doResetPwd, HttpSession session, HttpServletResponse response){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("doResetPwd", doResetPwd);
		try {
			ValidationUtil.validation(doResetPwd);
			String sessionVcode = (String) session.getAttribute("resetpwd_code");
			if(!StringUtil.equals(doResetPwd.getVerifycode().toLowerCase(), sessionVcode)){
				throw new ValidationException("验证码错误");
			}
			ResetPwdCache resetPwdCache = ResetPwdCache.get(doResetPwd.getToken());
			if(resetPwdCache == null){
				throw new ValidationException("此次密码重置的安全凭证已过期失效，无法设置帐号信息");
			}
			if(!StringUtil.equals(doResetPwd.getNewPwd(), doResetPwd.getReNewPwd())){
				throw new ValidationException("两次输入的密码不匹配");
			}
			User user = userService.findByLoginName(resetPwdCache.getName());
			if(user == null){
				throw new ValidationException("用户不存在");
			}
			if(userService.modifyPassword(user.getIid(), doResetPwd.getNewPwd())){
				ResetPwdCache.remove(doResetPwd.getToken());
			}else{
				throw new ValidationException("修改密码失败，请联系管理员");
			}
			modelAndView.setView(new RedirectView("change_result.do"));
			response.setHeader("Pragma","No-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires", -10);
		}catch(Exception e){
			modelAndView.setViewName("forward:change.do");
			modelAndView.addObject("message", e.getMessage());
		}
		return modelAndView;
	}
	
	/**
	 * 修改密码成功
	 * @return
	 */
	@RequestMapping("change_result")
	public ModelAndView changeResult(){
		ModelAndView modelAndView = new ModelAndView("support/resetpwd/resetpwd_changepwd_result");
		modelAndView.addObject("success", true);
		return modelAndView;
	}
	
	@RequestMapping("verifycode")
	@ResponseBody
	public void verifycode(HttpServletResponse response){
		VerifyCode.generate(response, "resetpwd_code");
	}
}
