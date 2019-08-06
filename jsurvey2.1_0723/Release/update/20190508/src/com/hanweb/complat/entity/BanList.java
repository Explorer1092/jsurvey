package com.hanweb.complat.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Index;
import com.hanweb.common.annotation.Indexes;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 封停实体
 * 
 * @author 李杰
 * 
 */
@Table(name = Tables.BANLIST)
@Indexes({
	@Index(fieldNames={"ipaddr", "loginname"}, indexName="idx_ip_login")
})
public class BanList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 登陆ip
	 */
	@Column(type = ColumnType.VARCHAR)
	private String ipAddr;

	/**
	 * 登陆时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date loginDate;

	/**
	 * 登陆次数
	 */
	@Column(type = ColumnType.INT)
	private Integer loginTimes;

	/**
	 * 登陆用户名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String loginName;

	/**
	 * 是否可以登陆 不入数据库
	 */
	private boolean canLogin = true;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public Integer getLoginTimes() {
		return loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	public boolean isCanLogin() {
		return canLogin;
	}

	public void setCanLogin(boolean canLogin) {
		this.canLogin = canLogin;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
