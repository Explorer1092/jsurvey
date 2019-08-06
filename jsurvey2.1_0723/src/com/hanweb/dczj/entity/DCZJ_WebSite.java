package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.OnlyQuery;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.WEBSITE)
public class DCZJ_WebSite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7850696638870604768L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 网站ID （暂时不用）
	 */
	@Column(type = ColumnType.INT)
	private Integer webid;

	/**
	 * 网站名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String name;

	/**
	 * 网站描述
	 */
	@Column(type = ColumnType.VARCHAR)
	private String spec;

	/**
	 * 父id
	 */
	@Column(type = ColumnType.INT)
	private Integer pid;

	/**
	 * 父网站名称
	 */
	@OnlyQuery
	private String pname;

	/**
	 * 网站编码
	 */
	@Column(type = ColumnType.VARCHAR, update = false)
	private String codeId;

	/**
	 * 排序号
	 */
	@Column(type = ColumnType.INT)
	private Integer orderid = 0;

	/**
	 * 拼音的首字母
	 */
	@Column(type = ColumnType.VARCHAR)
	private String pinYin;

	/**
	 * 附加属性 不计入数据库
	 */

	/**
	 * 父机构编码
	 */
	private String parCodeid = "";

	/**
	 * 是否有下属网站
	 */
	private Boolean isParent = false;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getWebid() {
		return webid;
	}

	public void setWebid(Integer webid) {
		this.webid = webid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getParCodeid() {
		return parCodeid;
	}

	public void setParCodeid(String parCodeid) {
		this.parCodeid = parCodeid;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

}
