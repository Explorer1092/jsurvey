package com.hanweb.dczj.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DRAWWINNERSINFO)
public class Draw_WinnersInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6817225677058718035L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 调查ID
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * 奖品ID
	 */
	@Column(type = ColumnType.INT)
	private Integer prizeid;
	
	/**
	 * 奖品名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String prizename;
	/**
	 * 中奖人ID
	 */
	@Column(type = ColumnType.INT)
	private Integer winnerid;
	
	/**
	 * 中奖人登录名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String loginname;
	
	/**
	 * 中奖人姓名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String winnername;
	
	/**
	 * 中奖时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date wintime;

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

	public Integer getWinnerid() {
		return winnerid;
	}

	public void setWinnerid(Integer winnerid) {
		this.winnerid = winnerid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getWinnername() {
		return winnername;
	}

	public void setWinnername(String winnername) {
		this.winnername = winnername;
	}

	public Date getWintime() {
		return wintime;
	}

	public void setWintime(Date wintime) {
		this.wintime = wintime;
	}

}
