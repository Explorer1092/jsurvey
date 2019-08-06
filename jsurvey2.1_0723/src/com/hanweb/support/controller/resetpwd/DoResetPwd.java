package com.hanweb.support.controller.resetpwd;

import com.hanweb.common.util.StringUtil;

import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

/**
 * 重置密码
 * 
 * @author 李杰
 *
 */
public class DoResetPwd {

	@NotBlank(message = "安全凭证不能为空")
	@NotNull(message = "安全凭证不能为空")
	private String token;

	@NotBlank(message = "密码不能为空")
	@NotNull(message = "密码不能为空")
	private String newPwd;

	@NotBlank(message = "重复密码不能为空")
	@NotNull(message = "重复密码不能为空")
	private String reNewPwd;

	@NotBlank(message = "验证码不能为空")
	@NotNull(message = "验证码不能为空")
	private String verifycode;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNewPwd() {
		return newPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = StringUtil.trim(newPwd);
	}

	public String getReNewPwd() {
		return reNewPwd;
	}

	public void setReNewPwd(String reNewPwd) {
		this.reNewPwd = StringUtil.trim(reNewPwd);
	}

	public String getVerifycode() {
		return verifycode;
	}

	public void setVerifycode(String verifycode) {
		this.verifycode = StringUtil.trim(verifycode);
	}
}
