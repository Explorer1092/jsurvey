package com.hanweb.complat.constant;

import java.io.File;

import com.hanweb.common.BaseInfo;
import com.hanweb.common.util.FileUtil;
import com.hanweb.common.util.Properties;
import com.hanweb.common.util.StringUtil;
import com.hanweb.common.util.ip.IpUtil;

/**
 * 系统参数
 * 
 * @author 李杰
 * 
 */
public class Settings {

	private static Settings settings = null;

	static {
		init();
	}

	public static void init() {
		settings = new Settings();
		Properties properties = new Properties(BaseInfo.getRealPath() + "/WEB-INF/config/complat.properties");
		settings.setAttachmentDir(properties.getString("attachmentDir"));
		settings.setBanTimes(properties.getInt("banTimes", 15));
		settings.setCanFeedback(properties.getBoolean("canFeedback", false));
		settings.setCheckLevel(properties.getInt("checkLevel", 0));
		settings.setEnableVerifyCode(properties.getBoolean("enableVerifyCode", false));
		settings.setFileTmp(properties.getString("fileTmp"));
		settings.setImageDir(properties.getString("imageDir"));
		settings.setInterceptMode(properties.getInt("interceptMode", 0));
		settings.setIpHead(properties.getString("ipHead", "x-forwarded-for"));
		settings.setLoginError(properties.getInt("loginError", 0));
		settings.setSessionTime(properties.getInt("sessionTime", 0));
		settings.setQrcodeLogin(properties.getBoolean("qrcodeLogin", false));
		settings.setDynamicCodeLogin(properties.getBoolean("dynamicCodeLogin", false));
		// 下面是dczj新增的
		settings.setStyletag(properties.getString("styletag"));
		settings.setMessageurl(properties.getString("messageurl"));
		settings.setMessageparam(properties.getString("messageparam"));
		settings.setEnableoss(properties.getInt("enableoss", 0));
		settings.setEnabledistributed(properties.getInt("enabledistributed",0));
		settings.setServertype(properties.getInt("servertype"));
		settings.setClosesystem(properties.getInt("closesystem",0));
	}

	public static void save(Settings settings) {
		Properties properties = new Properties(BaseInfo.getRealPath() + "/WEB-INF/config/complat.properties");
		properties.setProperty("attachmentDir", settings.getAttachmentDir());
		properties.setProperty("banTimes", settings.getBanTimes());
		properties.setProperty("canFeedback", settings.isCanFeedback());
		properties.setProperty("checkLevel", settings.getCheckLevel());
		properties.setProperty("enableVerifyCode", settings.isEnableVerifyCode());
		properties.setProperty("fileTmp", settings.getFileTmp());
		properties.setProperty("imageDir", settings.getImageDir());
		properties.setProperty("interceptMode", settings.getInterceptMode());
		properties.setProperty("ipHead", settings.getIpHead());
		properties.setProperty("loginError", settings.getLoginError());
		properties.setProperty("sessionTime", settings.getSessionTime());
		properties.setProperty("qrcodeLogin", settings.isQrcodeLogin());
		properties.setProperty("dynamicCodeLogin", settings.isDynamicCodeLogin());
		
		// 下面是dczj新增的
		properties.setProperty("styletag", settings.getStyletag());
		properties.setProperty("messageurl", settings.getMessageurl());
		properties.setProperty("messageparam", settings.getMessageparam());
		properties.setProperty("enableoss", settings.getEnableoss());
		properties.setProperty("enabledistributed", settings.getEnabledistributed());
		properties.setProperty("servertype", settings.getServertype());
		properties.setProperty("closesystem", settings.getClosesystem());
		properties.save();
		init();
	}

	/**
	 * 密码强度等级
	 */
	private int checkLevel = 2;

	/**
	 * 真实ip的head
	 */
	private String ipHead = "";

	/**
	 * 验证码
	 */
	private boolean enableVerifyCode = true;

	/**
	 * 权限拦截模式
	 */
	private int interceptMode = 1;

	/**
	 * 是否开启意见反馈
	 */
	private boolean canFeedback = false;

	/**
	 * 登录错误次数
	 */
	private int loginError = 3;

	/**
	 * 是否支持二维码登陆
	 */
	private boolean qrcodeLogin = false;

	/**
	 * 是否使用动态码登陆
	 */
	private boolean  dynamicCodeLogin = false;

	/**
	 * 登录错误的封停时间
	 */
	private int banTimes = 15;

	/**
	 * 登录超时时间
	 */
	private int sessionTime = 30;

	/**
	 * 文件暂存目录
	 */
	private String fileTmp;

	/**
	 * 高级编辑器上传的图片路径
	 */
	private String imageDir;

	/**
	 * 高级编辑器上传的附件路径
	 */
	private String attachmentDir;

	public int getCheckLevel() {
		return checkLevel;
	}

	public void setCheckLevel(int checkLevel) {
		this.checkLevel = checkLevel;
	}

	public boolean isEnableVerifyCode() {
		return enableVerifyCode;
	}

	public void setEnableVerifyCode(boolean enableVerifyCode) {
		this.enableVerifyCode = enableVerifyCode;
	}

	public int getInterceptMode() {
		return interceptMode;
	}

	public void setInterceptMode(int interceptMode) {
		this.interceptMode = interceptMode;
	}

	public boolean isCanFeedback() {
		return canFeedback;
	}

	public void setCanFeedback(boolean canFeedback) {
		this.canFeedback = canFeedback;
	}

	public int getLoginError() {
		return loginError;
	}

	public void setLoginError(int loginError) {
		this.loginError = loginError;
	}

	public int getBanTimes() {
		return banTimes;
	}

	public void setBanTimes(int banTimes) {
		this.banTimes = banTimes;
	}

	public int getSessionTime() {
		return sessionTime;
	}

	public void setSessionTime(int sessionTime) {
		this.sessionTime = sessionTime;
	}

	public String getFileTmp() {
		if (StringUtil.isEmpty(fileTmp)) {
			fileTmp = BaseInfo.getRealPath() + "/tempfile/";
		}
		File file = new File(fileTmp);
		FileUtil.isDirExsit(file, true);
		return StringUtil.convertPath(fileTmp, true);
	}

	public void setFileTmp(String fileTmp) {
		this.fileTmp = fileTmp;
	}

	/**
	 * 获得系统Setting对象
	 * 
	 * @return
	 */
	public static Settings getSettings() {
		return settings;
	}

	/**
	 * 获得高级编辑器上传的图片路径
	 * 
	 * @return
	 */
	public String getImageDir() {
		if (StringUtil.isEmpty(imageDir)) {
			this.imageDir = BaseInfo.getRealPath() + "/upload/images/";
		}
		File file = new File(this.imageDir);
		FileUtil.isDirExsit(file, true);
		return StringUtil.convertPath(this.imageDir, true);
	}

	/**
	 * 获得高级编辑器上传的附件路径
	 * 
	 * @return
	 */
	public String getAttachmentDir() {
		if (StringUtil.isEmpty(attachmentDir)) {
			this.attachmentDir = BaseInfo.getRealPath() + "/upload/attachment/";
		}
		File file = new File(this.attachmentDir);
		FileUtil.isDirExsit(file, true);
		return StringUtil.convertPath(this.attachmentDir, true);
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}

	public void setAttachmentDir(String attachmentDir) {
		this.attachmentDir = attachmentDir;
	}

	public String getIpHead() {
		return ipHead;
	}

	public void setIpHead(String ipHead) {
		this.ipHead = ipHead;
		IpUtil.setIpHead(ipHead);
	}

	public boolean isQrcodeLogin() {
		return qrcodeLogin;
	}

	public void setQrcodeLogin(boolean qrcodeLogin) {
		this.qrcodeLogin = qrcodeLogin;
	}

	public boolean isDynamicCodeLogin() {
		return dynamicCodeLogin;
	}

	public void setDynamicCodeLogin(boolean dynamicCodeLogin) {
		this.dynamicCodeLogin = dynamicCodeLogin;
	}
	
	/**
	 * 短信接口url
	 */
	private String messageurl;
	
	/**
	 * 短信接口配置参数
	 */
	private String messageparam;
	
	/**
	 * 是否运用oss
	 */
	private int enableoss;
	
	/**
	 * 样式标签设置
	 */
	private String styletag;
	/**
	 * 是否运用分布式
	 */
	private int enabledistributed;
	
	/**
	 * 所属服务器类型设置
	 */
	private int servertype;
	
	/**
	 * 是否关系统
	 */
	private int closesystem;

	public String getMessageurl() {
		return messageurl;
	}

	public void setMessageurl(String messageurl) {
		this.messageurl = messageurl;
	}

	public String getMessageparam() {
		return messageparam;
	}

	public void setMessageparam(String messageparam) {
		this.messageparam = messageparam;
	}

	public int getEnableoss() {
		return enableoss;
	}

	public void setEnableoss(int enableoss) {
		this.enableoss = enableoss;
	}

	public String getStyletag() {
		return styletag;
	}

	public void setStyletag(String styletag) {
		this.styletag = styletag;
	}

	public int getEnabledistributed() {
		return enabledistributed;
	}

	public void setEnabledistributed(int enabledistributed) {
		this.enabledistributed = enabledistributed;
	}

	public int getServertype() {
		return servertype;
	}

	public void setServertype(int servertype) {
		this.servertype = servertype;
	}

	public int getClosesystem() {
		return closesystem;
	}

	public void setClosesystem(int closesystem) {
		this.closesystem = closesystem;
	}
	
}
