package com.hanweb.dczj.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提交验证用
 * 
 * @author admin
 *
 */
public class FrontVerificationService {

	/**
	 * 是否全是中文
	 * 
	 * @param name
	 * @return
	 */
	public boolean checkname(String content) {
		if (content == null)
			return false;
		int n = 0;
		for (int i = 0; i < content.length(); i++) {
			n = (int) content.charAt(i);
			if (!(19968 <= n && n < 40869)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 是否为数字
	 * 
	 * @param content
	 * @return
	 */
	public boolean checkNumber(String content) {
		if (content == null)
			return false;
		for (int i = content.length(); --i >= 0;) {
			int chr = content.charAt(i);
			if (chr < 48 || chr > 57)
				return false;
		}
		return true;
	}

	/**
	 * 邮箱是否合法
	 * 
	 * @param content
	 * @return
	 */
	public boolean checkEmail(String content) {
		if (content == null)
			return false;
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern p;
		Matcher m;
		p = Pattern.compile(regEx1);
		m = p.matcher(content);
		if (m.matches())
			return true;
		else
			return false;
	}

	/**
	 * 手机号是否合法
	 * 
	 * @param content
	 * @return
	 */
	public boolean checkPhone(String content) {
		String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
		if (content.length() != 11) {
			return false;
		} else {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(content);
			boolean isMatch = m.matches();
			return isMatch;
		}
	}

}
