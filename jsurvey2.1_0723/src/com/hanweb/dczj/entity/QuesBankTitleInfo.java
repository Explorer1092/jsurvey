package com.hanweb.dczj.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * DCZJ题库标题表实体
 *
 */
@Table(name = Tables.DCZJQUEABANKTITLEINFO)
public class QuesBankTitleInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5073805898269161707L;
	
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 站点id
	 */
	@Column(type = ColumnType.INT)
	private Integer webid;
	
	/**
	 * 网站id
	 */
	@Column(type = ColumnType.INT)
	private Integer orderid;
    
	/**
	 * 用户名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String username;
	
	
	
	/**
	 * 用户名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String quesbankname;
	
	
	@Column(type = ColumnType.VARCHAR)
	private String creator;

	/**
	 * 创建时间
	 */
	@Column(type = ColumnType.DATETIME , update = false)
	private Date createtime;
	
	/**
	 *是否删除，0未删除，1已删除
	 */
	@Column(type = ColumnType.INT)
	private Integer isdelete;
	
	public Integer getIid() {
		return iid;
	}


	public Integer getIsdelete() {
		return isdelete;
	}


	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
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


	public Integer getOrderid() {
		return orderid;
	}


	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public String getQuesbankname() {
		return quesbankname;
	}


	public void setQuesbankname(String quesbankname) {
		this.quesbankname = quesbankname;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
