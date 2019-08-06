package com.hanweb.complat.entity;

import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Index;
import com.hanweb.common.annotation.Indexes;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 登录日志
 * @author 陈健
 *
 */
@Table(name = Tables.ACCESSLOG)
@Indexes({
	@Index(fieldNames={"loginname"}, indexName="accesslog_loginname")
})
public class AccessLog {
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	@Column(type = ColumnType.VARCHAR, length = 255)
	private String loginName;
	
	@Column(type = ColumnType.DATETIME, notNull = true)
	private Date accesstime;

	@Column(type = ColumnType.VARCHAR, length = 255)
	private String ip;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getAccesstime() {
		return accesstime;
	}

	public void setAccesstime(Date accesstime) {
		this.accesstime = accesstime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
}
