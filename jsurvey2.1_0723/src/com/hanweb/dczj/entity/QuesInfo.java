package com.hanweb.dczj.entity;

import java.io.Serializable;
import java.util.List;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.dczj.constant.Tables;

@Table(name = Tables.DCZJQUESINFO)
public class QuesInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861409962456446522L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;
	
	/**
	 * 排序id
	 */
	@Column(type = ColumnType.INT)
	private Integer orderid;
	
	/**
	 * 题目名称
	 */
	@Column(type = ColumnType.TEXT)
	private String quesname;
	
	/**
	 * 题目备注
	 */
	@Column(type = ColumnType.VARCHAR)
	private String note;
	
	/**
	 * 所属调查ID
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjid;
	
	/**
	 * 题目类型 0单选 1多选 2多行文本 3文字说明 4分页 5单行文本
	 */
	@Column(type = ColumnType.INT)
	private Integer type;
	
	/**
	 * 答案列数
	 */
	@Column(type = ColumnType.INT)
	private Integer col;
	
	/**
	 * 是否必填
	 */
	@Column(type = ColumnType.INT)
	private Integer ismustfill;
	
	/**
	 * 最少选择
	 */
	@Column(type = ColumnType.INT)
	private Integer minselect;
	
	/**
	 * 最多选择
	 */
	@Column(type = ColumnType.INT)
	private Integer maxselect;
	
	/**
	 * 自定义：文本框宽度
	 */
	@Column(type = ColumnType.INT)
	private Integer textinputwidth;
	
	/**
	 * 自定义：文本框高度
	 */
	@Column(type = ColumnType.INT)
	private Integer textinputheight;
	
	/**
	 * 文本内容
	 */
	@Column(type = ColumnType.TEXT)
	private String content;
	
	/**
	 * 是否删除 0在用1删除
	 */
	@Column(type = ColumnType.INT)
	private Integer state;
	
	/**
	 *  调查类型  1仅投票
	 */
	@Column(type = ColumnType.INT)
	private Integer dczjtype;
	
	/**
	 * 关联选项的ID
	 */
	@Column(type = ColumnType.INT)
	private Integer relyanswid;
	
	/**
	 * 默认公开
	 */
	@Column(type = ColumnType.INT)
	private Integer showpublish;
	
	/**
	 * 校验规则
	 */
	@Column(type = ColumnType.INT)
	private Integer validaterules;
	
	/**
	 * 题目分值默认为5
	 */
	@Column(type = ColumnType.FLOAT, decimalLenght = 2)
	private float quesscore = 5;
	
	private List<AnswInfo> answMoreList;
	private Integer oldquesid;
	
	private String answvalue;
	
	private Integer answcount;
	
	public Integer getAnswcount() {
		return answcount;
	}

	public void setAnswcount(Integer answcount) {
		this.answcount = answcount;
	}

	public String getAnswvalue() {
		return answvalue;
	}

	public void setAnswvalue(String answvalue) {
		this.answvalue = answvalue;
	}

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getQuesname() {
		return quesname;
	}

	public void setQuesname(String quesname) {
		this.quesname = quesname;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getDczjid() {
		return dczjid;
	}

	public void setDczjid(Integer dczjid) {
		this.dczjid = dczjid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	public Integer getIsmustfill() {
		return ismustfill;
	}

	public void setIsmustfill(Integer ismustfill) {
		this.ismustfill = ismustfill;
	}

	public Integer getMinselect() {
		return minselect;
	}

	public void setMinselect(Integer minselect) {
		this.minselect = minselect;
	}

	public Integer getMaxselect() {
		return maxselect;
	}

	public void setMaxselect(Integer maxselect) {
		this.maxselect = maxselect;
	}

	public Integer getTextinputwidth() {
		return textinputwidth;
	}

	public void setTextinputwidth(Integer textinputwidth) {
		this.textinputwidth = textinputwidth;
	}

	public Integer getTextinputheight() {
		return textinputheight;
	}

	public void setTextinputheight(Integer textinputheight) {
		this.textinputheight = textinputheight;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getDczjtype() {
		return dczjtype;
	}

	public void setDczjtype(Integer dczjtype) {
		this.dczjtype = dczjtype;
	}

	public Integer getRelyanswid() {
		return relyanswid;
	}

	public void setRelyanswid(Integer relyanswid) {
		this.relyanswid = relyanswid;
	}

	public Integer getShowpublish() {
		return showpublish;
	}

	public void setShowpublish(Integer showpublish) {
		this.showpublish = showpublish;
	}

	public Integer getValidaterules() {
		return validaterules;
	}

	public void setValidaterules(Integer validaterules) {
		this.validaterules = validaterules;
	}

	public List<AnswInfo> getAnswMoreList() {
		return answMoreList;
	}

	public void setAnswMoreList(List<AnswInfo> answMoreList) {
		this.answMoreList = answMoreList;
	}

	public Integer getOldquesid() {
		return oldquesid;
	}

	public void setOldquesid(Integer oldquesid) {
		this.oldquesid = oldquesid;
	}

	public float getQuesscore() {
		return quesscore;
	}

	public void setQuesscore(float quesscore) {
		this.quesscore = quesscore;
	}

	

	
}
