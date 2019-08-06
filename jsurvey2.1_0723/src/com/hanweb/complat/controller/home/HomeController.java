package com.hanweb.complat.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页控制器
 * 
 * @author 李杰
 * 
 */
@Controller
@RequestMapping(value = "manager/home")
public class HomeController {

	/**
	 * 显示首页
	 * 
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView showMainPage() {
		ModelAndView modelAndView = new ModelAndView("complat/index");
		return modelAndView;
	}
}