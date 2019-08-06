package com.hanweb.dczj.entity;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * 调查征集操作日志
 * @author JamieAllen
 *
 */
@Table(name = Tables.LOG)
public class Dczj_Log {
	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer i_id;
	
	/**
	 * 操作用户
	 */
	@Column(type = ColumnType.VARCHAR)
	private String opruser;
	/**
	 * 操作名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String oprname;
	
	/**
	 * 操作状态
	 */
	@Column(type = ColumnType.INT)
	private int oprstate = 0; 
	
	/**
	 * 操作人ip
	 */
	@Column(type = ColumnType.VARCHAR)
	private String opruserip;
	
	/**
	 * 操作日期
	 */
	@Column(type = ColumnType.VARCHAR)
	private String oprcreatedate;
	
	/**
	 * 调查id不属于调查投票时为空
	 */
	@Column(type = ColumnType.INT)
	private Integer i_formid;

	/**
	 * 意见征集id不属于意见征集时为空
	 */
	@Column(type = ColumnType.INT)
	private Integer i_topicid;

	/**
	 * 网上调查id不属于网上调查时为空
	 */
	@Column(type = ColumnType.INT)
	private Integer i_voteid;

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getOpruser() {
		return opruser;
	}

	public void setOpruser(String opruser) {
		this.opruser = opruser;
	}

	public String getOprname() {
		return oprname;
	}

	public void setOprname(String oprname) {
		this.oprname = oprname;
	}

	public int getOprstate() {
		return oprstate;
	}

	public void setOprstate(int oprstate) {
		this.oprstate = oprstate;
	}

	public String getOpruserip() {
		return opruserip;
	}

	public void setOpruserip(String opruserip) {
		this.opruserip = opruserip;
	}

	public String getOprcreatedate() {
		return oprcreatedate;
	}

	public void setOprcreatedate(String oprcreatedate) {
		this.oprcreatedate = oprcreatedate;
	}

	public Integer getI_formid() {
		return i_formid;
	}

	public void setI_formid(Integer i_formid) {
		this.i_formid = i_formid;
	}

	public Integer getI_topicid() {
		return i_topicid;
	}

	public void setI_topicid(Integer i_topicid) {
		this.i_topicid = i_topicid;
	}

	public Integer getI_voteid() {
		return i_voteid;
	}

	public void setI_voteid(Integer i_voteid) {
		this.i_voteid = i_voteid;
	}

}
