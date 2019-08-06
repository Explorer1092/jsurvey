package com.hanweb.complat.constant;

/**
 * 通用平台对应的表名
 * 
 * @author ZhangC
 * 
 */
public class Tables {
	/**
	 * 表前缀
	 */
	public static final String PREFIX = "complat_";

	/**
	 * 用户表
	 */
	public static final String USER = PREFIX + "user";

	/**
	 * 机构表
	 */
	public static final String GROUP = PREFIX + "group";

	/**
	 * 角色表
	 */
	public static final String ROLE = PREFIX + "role";

	/**
	 * 角色关系表
	 */
	public static final String ROLERELATION = PREFIX + "rolerelation";

	/**
	 * 角色权限表
	 */
	public static final String ROLERIGHT = PREFIX + "roleright";

	/**
	 * 权限表
	 */
	public static final String RIGHT = PREFIX + "right";

	/**
	 * 封停表
	 */
	public static final String BANLIST = PREFIX + "banlist";

	/**
	 * 临时文件表
	 */
	public static final String TEMPFILE = PREFIX + "tempfile";

	/**
	 * 外网用户表
	 */
	public static final String OUTSIDEUSER = PREFIX + "outsideuser";

	/**
	 * 机构管理范围表
	 */
	public static final String GROUPMANAGER = PREFIX + "groupmanager";
	
	/**
	 * 登录日志表
	 */
	public static final String ACCESSLOG = PREFIX + "accesslog";
}
