package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.PRIZESETTING)
public class PrizeSetting implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5187739789357169753L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 所属调查ID
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * 奖品Id
	 */
	@Column(type = ColumnType.INT)
	private Integer prizeid;
	
	/**
	 * 奖品名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String prizename;
	
	/**
	 * 奖品数量
	 */
	@Column(type = ColumnType.INT)
	private Integer prizenumber;
	
	/**
	 * 奖品剩余数量
	 */
	@Column(type = ColumnType.INT)
	private Integer prizeremainder;
	
	/**
	 * 奖品概率
	 */
	@Column(type = ColumnType.FLOAT, decimalLenght = 2)
	private float prizeprobability;

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

	public Integer getPrizeid() {
		return prizeid;
	}

	public void setPrizeid(Integer prizeid) {
		this.prizeid = prizeid;
	}

	public String getPrizename() {
		return prizename;
	}

	public void setPrizename(String prizename) {
		this.prizename = prizename;
	}

	public Integer getPrizenumber() {
		return prizenumber;
	}

	public void setPrizenumber(Integer prizenumber) {
		this.prizenumber = prizenumber;
	}

	public Integer getPrizeremainder() {
		return prizeremainder;
	}

	public void setPrizeremainder(Integer prizeremainder) {
		this.prizeremainder = prizeremainder;
	}

	public float getPrizeprobability() {
		return prizeprobability;
	}

	public void setPrizeprobability(float prizeprobability) {
		this.prizeprobability = prizeprobability;
	}

}
