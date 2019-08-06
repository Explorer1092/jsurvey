package com.hanweb.complat.constant;

import com.hanweb.common.BaseInfo;

/**
 * 常量
 * 
 * @author 李杰
 *
 */
public class StaticValues {

	/**
	 * 用户session key
	 */
	public static final String USERINFO = "userinfo";

	/**
	 * 后台登陆成功后的 cookie key，记住用户名
	 */
	public static final String LOGIN_COOKIE = "loginuserid";

	/**
	 * 登陆小程序二维码
	 */
	public static final String QRCODE_REAL_PATH = BaseInfo.getRealPath() + "/upload/images/_qrcode.png";

	/**
	 * 登陆小程序二维码
	 */
	public static final String QRCODE_URL = BaseInfo.getContextPath() + "/upload/images/_qrcode.png";
}
