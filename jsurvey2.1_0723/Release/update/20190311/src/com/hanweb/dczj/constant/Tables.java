package com.hanweb.dczj.constant;


/**
 * @系统对应表名
 * @author xll
 *
 */
public class Tables {

	/**
	 *  调查征集标题表
	 */
	public static final String DCZJTITLEINFO = "jsurvey_titleinfo";
	
	/**
	 *  调查征集题目表
	 */
	public static final String DCZJQUESINFO = "jsurvey_quesinfo";
	
	/**
	 *  调查征集答案表
	 */
	public static final String DCZJANSWINFO = "jsurvey_answinfo";
	
	/**
	 * 调查征集模板表
	 */
	public static final String DCZJTEMPLATE = "jsurvey_template";
	
	/**
	 * 调查征集样式表
	 */
	public static final String DCZJSTYLE = "jsurvey_style";
	
	/**
	 * 调查征集常用模板样式表
	 */
	public static final String DCZJCOMMONTEMPSTYLE = "jsurvey_commontempstyle";
	
	/**
	 * 调查征集选项设置表
	 */
	public static final String JSURVEYDISPLAYCONFIG = "jsurvey_displayconfig";
	
	/**
	 * 调查征集网站表
	 */
    public static final String WEBSITE = "jsurvey_website";
    
    /**
	 * 调查征集网站管理范围表
	 */
    public static final String WEBMANAGER = "jsurvey_webmanager";
    
    /**
	 * 敏感词题库表
	 */
    public static final String SENSITIVE = "jsurvey_sensitive";
    
    /**
	 * 征集调查后台操作日志表
	 */
	public static final String LOG = "jsurvey_log";
	
	/**
	 * 调查征集空表(该表没有任何信息辅助日志表)
	 */
   public static final String DCZJNULL = "jsurvey_null";
   
   /**
	 * 调查征集设参表
	 */
	public static final String CONFIG = "jsurvey_config";
	
	/**
	 * 调查征集奖项设置表
	 */
	public static final String PRIZESETTING = "jsurvey_prizesetting";
	
	/**
	 * 调查征集感谢内容设置表
	 */
	public static final String THANKSSETTING = "jsurvey_thankssetting";
	
	/**
	 * 调查征集感谢内容设置表
	 */
	public static final String DRAWWINNERSINFO = "jsurvey_winnersinfo";
	
	/**
	 * 调查征集感谢内容设置表
	 */
	public static final String MESSAGECONTENT = "jsurvey_messagecontent";
	
	/**
	 * 提交主表
	 */
	public static final String TOTALRECO = "jsurvey_totalreco";
	
	/**
	 * 多选提交主表
	 */
	public static final String CHECKEDBOXRECO = "jsurvey_checkedboxreco";
	
	/**
	 * 填空提交主表
	 */
	public static final String CONTENTRECO = "jsurvey_contentreco";
	
	/**
	 * 单选提交主表
	 */
	public static final String RADIORECO = "jsurvey_radioreco";
	
	/**
	 * 调查征集统计表
	 */
	public static final String COUNT = "jsurvey_count";
	
	/**
	 * 调查征集数据调用表
	 */
    public static final String DCZJDATACALL = "jsurvey_datacall";
    
    /**
	 * 调查征集数据模板样式表
	 */
    public static final String DCZJDATATEMP = "jsurvey_dc_temp";
    
    /**
	 * 调查征集限制用户登录表
	 */
    public static final String JUSURVEYLIMITLOGIN = "jsurvey_loginuser";
    
    /**
	 * 调查征集公开用户登录表
	 */
    public static final String JUSURVEYLIMITOPEN = "jsurvey_openuser";
	
    /**
	 * 手机记录
	 */
	public static final String MOBILELOG = "jsurvey_mobilelog";
	
	/**
	 * 访问统计表
	 */
	public static final String VISITCOUNT = "jsurvey_visitcount";
}
