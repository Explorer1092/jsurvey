package com.hanweb.dczj.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Index;
import com.hanweb.common.annotation.Indexes;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * 提交主表
 * 
 * @author 
 * 
 */
@Table(name = Tables.TOTALRECO)
@Indexes({
	@Index(fieldNames={"dczjid"},indexName="IDX_STR_FORMID"),
	@Index(fieldNames={"submitstate"},indexName="IDX_STR_SUBMITSTATE")
})
public class TotalReco implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 手机号码
	 */
	@Column(type = ColumnType.VARCHAR)
	private String mobile;

	/**
	 * 提交时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date createdate;

	/**
	 * 数据来源
	 * 1:pc 2:手机端 3:微信
	 */
	@Column(type = ColumnType.INT)
	private Integer type;

	/**
	 * ip地址
	 */
	@Column(type = ColumnType.VARCHAR)
	private String ip;

	/**
	 * 网上调查id
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;

	/**
	 * 提交唯一码
	 */
	@Column(type = ColumnType.VARCHAR)
	private String unid;
	
	/**
	 * 手机验证码
	 */
	@Column(type = ColumnType.VARCHAR)
	private String code;

	/**
	 * 提交标识  0 未提交 1已经提交
	 */
	@Column(type = ColumnType.INT)
	private Integer submitstate;
	
	/**
	 * 提交获得的总分值
	 */
	@Column(type = ColumnType.INT )
	private Integer sumscore;
	
	/**
	 * 手机验证码
	 */
	@Column(type = ColumnType.VARCHAR)
	private String ipaddress;
	
	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Integer getSubmitstate() {
		return submitstate;
	}

	public void setSubmitstate(Integer submitstate) {
		this.submitstate = submitstate;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSumscore() {
		return sumscore;
	}

	public void setSumscore(Integer sumscore) {
		this.sumscore = sumscore;
	}
	
}
