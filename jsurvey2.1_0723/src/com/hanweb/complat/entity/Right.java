package com.hanweb.complat.entity;

import java.io.Serializable;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 权限实体
 * 
 * @author ZhangC
 * 
 */
@Table(name = Tables.RIGHT)
public class Right implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8384115949800751843L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * 唯一标识（此标识只是用来确定层次关系）
	 */
	@Column(type = ColumnType.VARCHAR, update = false)
	private String codeId;

	/**
	 * 父标识（此标识只是用来确定层次关系）
	 */
	@Column(type = ColumnType.VARCHAR, update = false)
	private String parentCodeId;

	/**
	 * 模块标识
	 */
	@Column(type = ColumnType.VARCHAR)
	private String moduleId;

	/**
	 * 模块名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String moduleName;

	/**
	 * 功能标识
	 */
	@Column(type = ColumnType.VARCHAR)
	private String functionId;

	/**
	 * 功能名称
	 */
	@Column(type = ColumnType.VARCHAR)
	private String functionName;

	/**
	 * 动态id
	 */
	@Column(type = ColumnType.VARCHAR)
	private String dynamicId;

	/**
	 * 类型（此参数为自由参数）
	 */
	@Column(type = ColumnType.VARCHAR)
	private String custom = "";

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFunctionId() {
		return functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getDynamicId() {
		return dynamicId;
	}

	public void setDynamicId(String dynamicId) {
		this.dynamicId = dynamicId;
	}

	public String getCodeId() {
		return codeId;
	}

	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}

	public String getParentCodeId() {
		return parentCodeId;
	}

	public void setParentCodeId(String parentCodeId) {
		this.parentCodeId = parentCodeId;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}
}
