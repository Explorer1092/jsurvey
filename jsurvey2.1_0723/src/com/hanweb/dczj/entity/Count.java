package com.hanweb.dczj.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Index;
import com.hanweb.common.annotation.Indexes;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

/**
 * 统计实体
 * @author JamieAllen
 *
 */
@Table(name = Tables.COUNT)
@Indexes({
	@Index(fieldNames={"dczjid","quesid","answid"},indexName="IDX_SF_FORMID_QUESID_ANSWID"),
	@Index(fieldNames={"quesid","answid"},indexName="IDX_SF_QUESID_ANSWID"),
	@Index(fieldNames={"answid"},indexName="IDX_SF_ANSWID")
})
public class Count implements Serializable{

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
	
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	@Column(type = ColumnType.INT)
	private Integer quesid;
	
	@Column(type = ColumnType.INT)
	private Integer answid;
	
	@Column(type = ColumnType.INT)
	private Integer selectcount = 0;
	
	@Column(type = ColumnType.VARCHAR)
	private String countdate;/*形式为yyyy-MM-dd*/

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
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

	public Integer getSelectcount() {
		return selectcount;
	}

	public void setSelectcount(Integer selectcount) {
		this.selectcount = selectcount;
	}

	public String getCountdate() {
		return countdate;
	}

	public void setCountdate(String countdate) {
		this.countdate = countdate;
	}

	
}
