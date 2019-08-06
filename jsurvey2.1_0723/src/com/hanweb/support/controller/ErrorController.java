package com.hanweb.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 错误控制器
 * 
 * @author 李杰
 *
 */
@Controller
@RequestMapping("error")
public class ErrorController {

	@RequestMapping("404")
	public ModelAndView fileNotFile() {
		ModelAndView modelAndView = new ModelAndView("support/error/404");
		return modelAndView;
	}

	@RequestMapping("500")
	public ModelAndView systemError() {
		ModelAndView modelAndView = new ModelAndView("support/error/error");
		return modelAndView;
	}

	@RequestMapping("300")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView("support/error/logintimeout");
		return modelAndView;
	}

	@RequestMapping("600")
	public ModelAndView reg() {
		ModelAndView modelAndView = new ModelAndView("support/error/600");
		return modelAndView;
	}

	@RequestMapping("601")
	public ModelAndView right() {
		ModelAndView modelAndView = new ModelAndView("support/error/601");
		return modelAndView;
	}

	@RequestMapping("602")
	public ModelAndView params() {
		ModelAndView modelAndView = new ModelAndView("support/error/602");
		return modelAndView;
	}

	@RequestMapping("603")
	public ModelAndView httpparams() {
		ModelAndView modelAndView = new ModelAndView("support/error/603");
		return modelAndView;
	}
}
