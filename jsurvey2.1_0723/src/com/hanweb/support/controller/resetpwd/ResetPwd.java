package com.hanweb.support.controller.resetpwd;

import com.hanweb.common.util.StringUtil;

import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

/**
 * 重置密码实体
 * 
 * @author 李杰
 *
 */
public class ResetPwd {

	@NotBlank(message = "用户名不能为空")
	@NotNull(message = "用户名不能为空")
	private String name;

	@NotBlank(message = "email不能为空")
	@NotNull(message = "email不能为空")
	@Email(message = "email格式不正确")
	private String email;

	@NotBlank(message = "验证码不能为空")
	@NotNull(message = "验证码不能为空")
	private String verifycode;

	private String token;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = StringUtil.getSafeString(name);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = verifycode;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
