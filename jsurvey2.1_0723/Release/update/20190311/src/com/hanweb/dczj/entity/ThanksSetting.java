package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.THANKSSETTING)
public class ThanksSetting implements Serializable{

	private static final long serialVersionUID = -8069169821669241897L;

	@Id
	@Column(type=ColumnType.INT)
	private Integer iid;
	
	@Column(type=ColumnType.INT)
	private Integer dczjid;
	
	@Column(type=ColumnType.VARCHAR)
	private String thankscontent = "";//感谢内容
	
	@Column(type=ColumnType.VARCHAR)
	private String jumpbtn;//前往按钮，0查看结果 1参与抽奖 2前往其他页面
	
	@Column(type=ColumnType.VARCHAR)
	private String btnname;//按钮名称
	
	@Column(type=ColumnType.VARCHAR)
	private String jumpurl;//跳转url

	@Column(type=ColumnType.INT)
	private Integer isdetail; //是否显示详情
	
	@Column(type=ColumnType.INT)
	private Integer isjump; //是否显示详情
	
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

	public String getThankscontent() {
		return thankscontent;
	}

	public void setThankscontent(String thankscontent) {
		this.thankscontent = thankscontent;
	}

	public String getJumpbtn() {
		return jumpbtn;
	}

	public void setJumpbtn(String jumpbtn) {
		this.jumpbtn = jumpbtn;
	}

	public String getBtnname() {
		return btnname;
	}

	public void setBtnname(String btnname) {
		this.btnname = btnname;
	}

	public String getJumpurl() {
		return jumpurl;
	}

	public void setJumpurl(String jumpurl) {
		this.jumpurl = jumpurl;
	}

	public Integer getIsdetail() {
		return isdetail;
	}

	public void setIsdetail(Integer isdetail) {
		this.isdetail = isdetail;
	}

	public Integer getIsjump() {
		return isjump;
	}

	public void setIsjump(Integer isjump) {
		this.isjump = isjump;
	}
	
}
