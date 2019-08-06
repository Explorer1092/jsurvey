package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * 手机记录表实体
 * 
 * @author zhounj
 * 
 */
@Table(name = Tables.MOBILELOG)
public class Mobilelog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer i_id;

	/**
	 * 电话
	 */
	@Column(type = ColumnType.VARCHAR)
	private String vc_mobile;

	/**
	 * 网上调查id如是意见征集则值为空
	 */
	@Column(type = ColumnType.INT)
	private Integer i_formid;
	
	/**
	 * 创建日期
	 */
	@Column(type = ColumnType.VARCHAR)
	private String c_createdate;

	/**
	 * 数据来源
	 * 1:pc 2:手机端 3:微信
	 */
	@Column(type = ColumnType.INT)
	private Integer i_type;

	/**
	 * ip地址
	 */
	@Column(type = ColumnType.VARCHAR)
	private String vc_ip;
	/**
	 * 短信接口返回值
	 */
	@Column(type = ColumnType.VARCHAR)
	private String vc_result;
	
	/**
	 * 验证码
	 */
	@Column(type = ColumnType.VARCHAR)
	private String vc_code;
	/**
	 * 意见征集id如是网上调查则值为空
	 */
	@Column(type = ColumnType.INT)
	private Integer i_topicid;
	
	/**
	 * 网上投票id如是网上调查则值为空
	 */
	@Column(type = ColumnType.INT)
	private Integer i_voteid;
	
	public Integer getI_id() {
		return i_id;
	}
	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}
	public String getVc_mobile() {
		return vc_mobile;
	}
	public void setVc_mobile(String vc_mobile) {
		this.vc_mobile = vc_mobile;
	}
	public Integer getI_formid() {
		return i_formid;
	}
	public void setI_formid(Integer i_formid) {
		this.i_formid = i_formid;
	}
	public String getC_createdate() {
		return c_createdate;
	}
	public void setC_createdate(String c_createdate) {
		this.c_createdate = c_createdate;
	}
	public Integer getI_type() {
		return i_type;
	}
	public void setI_type(Integer i_type) {
		this.i_type = i_type;
	}
	public String getVc_ip() {
		return vc_ip;
	}
	public void setVc_ip(String vc_ip) {
		this.vc_ip = vc_ip;
	}
	public String getVc_result() {
		return vc_result;
	}
	public void setVc_result(String vc_result) {
		this.vc_result = vc_result;
	}
	public String getVc_code() {
		return vc_code;
	}
	public void setVc_code(String vc_code) {
		this.vc_code = vc_code;
	}
	public Integer getI_topicid() {
		return i_topicid;
	}
	public void setI_topicid(Integer i_topicid) {
		this.i_topicid = i_topicid;
	}
	public Integer getI_voteid() {
		return i_voteid;
	}
	public void setI_voteid(Integer i_voteid) {
		this.i_voteid = i_voteid;
	}

	
}
