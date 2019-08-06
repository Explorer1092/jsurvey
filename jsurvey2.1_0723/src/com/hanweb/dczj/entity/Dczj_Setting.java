package com.hanweb.dczj.entity;

import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * 调查征集设参实体
 */
@Table(name = Tables.CONFIG)
public class Dczj_Setting {
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * dczjid
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * 类型
	 */
	@Column(type = ColumnType.INT,defaultVal = "1")
	private Integer type = 0;
	
	/**
	 * 是否定时发布问卷（0否，1是）
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer isstart = 0;
	
	/**
	 * 开始时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date starttime = new Date();
	
	/**
	 * 是否设定结束时间（0否，1是）
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer isend = 1;
	
	/**
	 * 结束时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date endtime = new Date();
	
	/**
	 * 是否限定登录用户参与
	 */
	@Column(type = ColumnType.INT)
	private Integer islimituser = 0;
	
	/**
	 * 验证码（0未开启，1开启）
	 */
	@Column(type = ColumnType.INT,defaultVal = "1")
	private Integer iscode = 1;
	
	/**
	 * 0为图形 1为手机
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer codes = 0;
	
	/**
	 * 是否开启提交限制（0关闭，1开启）
	 */
	@Column(type = ColumnType.INT)
	private Integer submitlimit = 1;
	
	/**
	 * 限制类型（0 ip,1 手机号）
	 */
	@Column(type = ColumnType.INT)
	private Integer limittype = 0;
	
	/**
	 * 限制时间（0只能，1每小时，2每天）
	 */
	@Column(type = ColumnType.INT)
	private Integer limittime = 0;
	
	/**
	 * 限制次数
	 */
	@Column(type = ColumnType.INT)
	private Integer limitnumber = 1;
	
	/**
	 * 是否公开问卷内容
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer isopen = 0;
	
	/**
	 * 跳转方式（0感谢信息，1制定页面）
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer isjump = 0;
	
	/**
	 * 跳转页面地址
	 */
	@Column(type = ColumnType.VARCHAR)
	private String jumpurl;
	
	/**
	 * 是否提供参与奖品
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer isprize = 0;
	
	/**
	 * 抽奖参与次数(0仅一次，1跟随参与提交次数)
	 */
	@Column(type = ColumnType.INT,defaultVal = "0")
	private Integer prizetime = 0;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsstart() {
		return isstart;
	}

	public void setIsstart(Integer isstart) {
		this.isstart = isstart;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Integer getIsend() {
		return isend;
	}

	public void setIsend(Integer isend) {
		this.isend = isend;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public Integer getCodes() {
		return codes;
	}

	public void setCodes(Integer codes) {
		this.codes = codes;
	}

	public Integer getIscode() {
		return iscode;
	}

	public void setIscode(Integer iscode) {
		this.iscode = iscode;
	}

	public Integer getSubmitlimit() {
		return submitlimit;
	}

	public void setSubmitlimit(Integer submitlimit) {
		this.submitlimit = submitlimit;
	}

	public Integer getLimittype() {
		return limittype;
	}

	public void setLimittype(Integer limittype) {
		this.limittype = limittype;
	}

	public Integer getLimittime() {
		return limittime;
	}

	public void setLimittime(Integer limittime) {
		this.limittime = limittime;
	}

	public Integer getLimitnumber() {
		return limitnumber;
	}

	public void setLimitnumber(Integer limitnumber) {
		this.limitnumber = limitnumber;
	}

	public Integer getIsjump() {
		return isjump;
	}

	public void setIsjump(Integer isjump) {
		this.isjump = isjump;
	}

	public String getJumpurl() {
		return jumpurl;
	}

	public void setJumpurl(String jumpurl) {
		this.jumpurl = jumpurl;
	}

	public Integer getIslimituser() {
		return islimituser;
	}

	public void setIslimituser(Integer islimituser) {
		this.islimituser = islimituser;
	}

	public Integer getIsopen() {
		return isopen;
	}

	public void setIsopen(Integer isopen) {
		this.isopen = isopen;
	}

	public Integer getIsprize() {
		return isprize;
	}

	public void setIsprize(Integer isprize) {
		this.isprize = isprize;
	}

	public Integer getPrizetime() {
		return prizetime;
	}

	public void setPrizetime(Integer prizetime) {
		this.prizetime = prizetime;
	}

	
}
