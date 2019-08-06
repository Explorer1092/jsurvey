package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.JSURVEYDISPLAYCONFIG)
public class DisplayConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1024424439742564364L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;	

	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	@Column(type = ColumnType.INT)
	private Integer isprogress;
	
	@Column(type = ColumnType.INT)
	private Integer istitlenumber;
	
	@Column(type = ColumnType.INT)
	private Integer chooseframe_style;
	
	@Column(type = ColumnType.INT)
	private Integer isopencontent;
	
	@Column(type = ColumnType.VARCHAR)
	private String contentsize;
	
	@Column(type = ColumnType.TEXT)
	private String cssstyle;

	@Column(type = ColumnType.INT)
	private Integer isshowscore;
	
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

	public Integer getIsprogress() {
		return isprogress;
	}

	public void setIsprogress(Integer isprogress) {
		this.isprogress = isprogress;
	}

	public Integer getIstitlenumber() {
		return istitlenumber;
	}

	public void setIstitlenumber(Integer istitlenumber) {
		this.istitlenumber = istitlenumber;
	}

	public Integer getChooseframe_style() {
		return chooseframe_style;
	}

	public void setChooseframe_style(Integer chooseframe_style) {
		this.chooseframe_style = chooseframe_style;
	}

	public Integer getIsopencontent() {
		return isopencontent;
	}

	public void setIsopencontent(Integer isopencontent) {
		this.isopencontent = isopencontent;
	}

	public String getContentsize() {
		return contentsize;
	}

	public void setContentsize(String contentsize) {
		this.contentsize = contentsize;
	}

	public String getCssstyle() {
		return cssstyle;
	}

	public void setCssstyle(String cssstyle) {
		this.cssstyle = cssstyle;
	}

	public Integer getIsshowscore() {
		return isshowscore;
	}

	public void setIsshowscore(Integer isshowscore) {
		this.isshowscore = isshowscore;
	}
	
}
