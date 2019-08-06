package com.hanweb.complat.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.OnlyQuery;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 角色实体
 * 
 * @author ZhangC
 * 
 */
@Table(name = Tables.ROLE)
public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6791358822013618822L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 角色名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String name;

	/**
	 * 角色备注
	 */
	@Column(type = ColumnType.VARCHAR)
	private String spec;

	/**
	 * 是否为缺省角色<br/>
	 * 1 - 缺省角色 <br/>
	 * 0 - 非缺省角色
	 */
	@Column(type = ColumnType.INT, length = 1, update = false)
	private Integer isDefault;

	/**
	 * 角色类型<br/>
	 * 0 - 系统管理员<br/>
	 * 1 - 机构管理员<br/>
	 * 6 - 自定义角色<br/>
	 * 其余2,3,4,5为系统保留类型
	 */

	@Column(type = ColumnType.INT, length = 1)
	@OnlyQuery
	private Integer type;

	/**
	 * 拼音的首字母
	 */
	@Column(type = ColumnType.VARCHAR)
	private String pinYin;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
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

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

}
