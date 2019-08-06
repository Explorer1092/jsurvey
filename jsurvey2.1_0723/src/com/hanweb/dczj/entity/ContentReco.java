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
 * 网上调查提交表实体
 * 
 * @author lijing
 * 
 */
@Table(name = Tables.CONTENTRECO)
@Indexes({
	@Index(fieldNames={"unid"},indexName="IDX_SCR_UNID"),
	@Index(fieldNames={"quesid","unid"},indexName="IDX_SCR_QUESID_UNID"),
	@Index(fieldNames={"dczjid","quesid","answid"},indexName="IDX_SCR_FORMID_QUESID_ANSWID")
})
public class ContentReco implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 调查id
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;

	/**
	 * 题目id
	 */
	@Column(type = ColumnType.INT)
	private Integer quesid;

	/**
	 * 答案id
	 */
	@Column(type = ColumnType.INT)
	private Integer answid;

	/**
	 * 其他答案
	 */
	@Column(type = ColumnType.TEXT)
	private String answcontent;
	
	/**
	 * 提交的ip
	 */
	@Column(type = ColumnType.VARCHAR)
	private String ip;

	/**
	 * 提交时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date submittime;

	/**
	 * 审核状态
	 */
	@Column(type = ColumnType.INT)
	private String audi;
	/**
	 * 外键，用于关联主表
	 */
	@Column(type = ColumnType.VARCHAR)
	private String unid;
	/**
	 * 唯一码
	 */
	@Column(type = ColumnType.VARCHAR)
	private String replyid;
	
	/**
	 * 回复内容
	 */
	@Column(type = ColumnType.VARCHAR)
	private String replycontent;
	
	public String getReplycontent() {
		return replycontent;
	}
	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}
	public Integer getDczjid() {
		return dczjid;
	}
	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}
	public Integer getIid() {
		return iid;
	}
	public void setIid(Integer iid) {
		this.iid = iid;
	}
	public Integer getQuesid() {
		return quesid;
	}
	public void setQuesid(Integer quesid) {
		this.quesid = quesid;
	}
	public Integer getAnswid() {
		return answid;
	}
	public void setAnswid(Integer answid) {
		this.answid = answid;
	}
	public String getAnswcontent() {
		return answcontent;
	}
	public void setAnswcontent(String answcontent) {
		this.answcontent = answcontent;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Date getSubmittime() {
		return submittime;
	}
	public void setSubmittime(Date submittime) {
		this.submittime = submittime;
	}
	public String getAudi() {
		return audi;
	}
	public void setAudi(String audi) {
		this.audi = audi;
	}
	public String getUnid() {
		return unid;
	}
	public void setUnid(String unid) {
		this.unid = unid;
	}
	public String getReplyid() {
		return replyid;
	}
	public void setReplyid(String replyid) {
		this.replyid = replyid;
	}
	
	
}
