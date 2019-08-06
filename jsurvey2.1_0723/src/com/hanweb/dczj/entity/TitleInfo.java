package com.hanweb.dczj.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * DCZJ标题表实体
 * @author xll
 *
 */
@Table(name = Tables.DCZJTITLEINFO)
public class TitleInfo implements Serializable{

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
	 * 创建时间
	 */
	@Column(type = ColumnType.DATETIME , update = false)
	private Date createtime;
	
	/**
	 * 用户名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String titlename;
	
	/**
	 * 信息表状态 0:进行中 1：未开始 2：已结束
	 */
	@Column(type = ColumnType.INT)
	private Integer state;
	
	/**
	 *是否发布，0未发布，1已发布
	 */
	@Column(type = ColumnType.INT)
	private Integer ispublish;
	
	/**
	 *是否删除，0未删除，1已删除
	 */
	@Column(type = ColumnType.INT)
	private Integer isdelete;
	
	/**
	 *所属服务器编号
	 */
	@Column(type = ColumnType.INT)
	private Integer servertype;
	
	/**
	 *类型
	 */
	@Column(type = ColumnType.INT)
	private Integer type;

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

	public String getTitlename() {
		return titlename;
	}

	public void setTitlename(String titlename) {
		this.titlename = titlename;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIspublish() {
		return ispublish;
	}

	public void setIspublish(Integer ispublish) {
		this.ispublish = ispublish;
	}

	public Integer getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
	}

	public Integer getServertype() {
		return servertype;
	}

	public void setServertype(Integer servertype) {
		this.servertype = servertype;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
