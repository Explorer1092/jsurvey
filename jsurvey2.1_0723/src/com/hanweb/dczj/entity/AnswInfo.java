package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJANSWINFO)
public class AnswInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1335143559173515634L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 排序id
	 */
	@Column(type = ColumnType.INT)
	private Integer orderid;
	
	/**
	 * dczjid
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * quesid
	 */
	@Column(type = ColumnType.INT)
	private Integer quesid;
	
	/**
	 * 答案名称
	 */
	@Column(type = ColumnType.TEXT)
	private String answname;
	
	/**
	 * 答案备注
	 */
	@Column(type = ColumnType.VARCHAR)
	private String answnote;
	
	/**
	 * 基础票数
	 */
	@Column(type = ColumnType.INT)
	private Integer basepoint;
	
	/**
	 * 是否删除 0在用1删除
	 */
	@Column(type = ColumnType.INT)
	private Integer state;
	
	/**
	 * pic名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String answimgname;
	
	/**
	 * 是否允许填空（0不允许，1允许）
	 */
	@Column(type = ColumnType.INT)
	private Integer allowfillinair;
	

	/**
	 * 是否正确
	 */
	@Column(type = ColumnType.VARCHAR)
	private String isright;
	
	private Integer count;
	
	private double percent;
	
	@Column(type = ColumnType.INT)
	private Integer defaultvalue;

	public Integer getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(Integer defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}

	public Integer getQuesid() {
		return quesid;
	}

	public void setQuesid(Integer quesid) {
		this.quesid = quesid;
	}

	public String getAnswname() {
		return answname;
	}

	public void setAnswname(String answname) {
		this.answname = answname;
	}

	public String getAnswnote() {
		return answnote;
	}

	public void setAnswnote(String answnote) {
		this.answnote = answnote;
	}

	public Integer getBasepoint() {
		return basepoint;
	}

	public void setBasepoint(Integer basepoint) {
		this.basepoint = basepoint;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getAnswimgname() {
		return answimgname;
	}

	public void setAnswimgname(String answimgname) {
		this.answimgname = answimgname;
	}

	public Integer getAllowfillinair() {
		return allowfillinair;
	}

	public void setAllowfillinair(Integer allowfillinair) {
		this.allowfillinair = allowfillinair;
	}

	public String getIsright() {
		return isright;
	}

	public void setIsright(String isright) {
		this.isright = isright;
	}

	
	
}
