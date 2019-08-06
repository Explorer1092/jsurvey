package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJCOMMONTEMPSTYLE)
public class CommonTemplateStyle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4474846709610263075L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 网站ID
	 */
	@Column(type = ColumnType.INT)
	private Integer webid;
	
	/**
	 * 常用模板样式名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String templatename;
	
	/**
	 * 类型  1 调查  2 征集  3 投票
	 */
	@Column(type = ColumnType.INT)
	private Integer type;
	
	/**
	 * 调查id
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * 是否删除 0在用1删除
	 */
	@Column(type = ColumnType.INT)
	private Integer state;
	
	/**
	 * 上传样式图片新名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String styleimgname;
	
	/**
	 * 列表页样式代码
	 */
	@Column(type = ColumnType.TEXT)
	private String liststyle = "";
	
	/**
	 * 结果页样式代码
	 */
	@Column(type = ColumnType.TEXT)
	private String resultstyle = "";
	
	/**
	 * 移动端列表页样式代码
	 */
	@Column(type = ColumnType.TEXT)
	private String phoneliststyle = "";
	
	/**
	 * 移动端结果页样式代码
	 */
	@Column(type = ColumnType.TEXT)
	private String phoneresultstyle = "";

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

	public String getTemplatename() {
		return templatename;
	}

	public void setTemplatename(String templatename) {
		this.templatename = templatename;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStyleimgname() {
		return styleimgname;
	}

	public void setStyleimgname(String styleimgname) {
		this.styleimgname = styleimgname;
	}

	public String getListstyle() {
		return liststyle;
	}

	public void setListstyle(String liststyle) {
		this.liststyle = liststyle;
	}

	public String getResultstyle() {
		return resultstyle;
	}

	public void setResultstyle(String resultstyle) {
		this.resultstyle = resultstyle;
	}

	public String getPhoneliststyle() {
		return phoneliststyle;
	}

	public void setPhoneliststyle(String phoneliststyle) {
		this.phoneliststyle = phoneliststyle;
	}

	public String getPhoneresultstyle() {
		return phoneresultstyle;
	}

	public void setPhoneresultstyle(String phoneresultstyle) {
		this.phoneresultstyle = phoneresultstyle;
	}
	
}
