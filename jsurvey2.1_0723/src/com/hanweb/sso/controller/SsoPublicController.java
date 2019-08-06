package com.hanweb.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hanweb.common.util.security.SecurityUtil;

/**
 * 供第三方来做sso的事情
 * @author 李杰
 *
 */
@Controller()
@RequestMapping("pub/sso")
public class SsoPublicController {
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("pubk")
	@ResponseBody
	public String getPublicKey(){
		return SecurityUtil.getPublicKey();
	}
}
