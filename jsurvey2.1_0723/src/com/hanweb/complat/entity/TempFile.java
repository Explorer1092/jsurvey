package com.hanweb.complat.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.GeneratorType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 临时文件管理
 * 
 * @author 李杰
 * 
 */
@Table(name = Tables.TEMPFILE)
public class TempFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标示
	 */
	@Id(generatorType = GeneratorType.UUID)
	@Column(type = ColumnType.VARCHAR, length = 32)
	private String uuid;

	/**
	 * 原文件名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String oldName;

	/**
	 * 新文件名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String newName;

	/**
	 * 文件类型
	 */
	@Column(type = ColumnType.VARCHAR)
	private String fileType;

	/**
	 * 上传文件的用户id
	 */
	@Column(type = ColumnType.VARCHAR)
	private String loginName;

	/**
	 * 上传时间
	 */
	@Column(type = ColumnType.DATE)
	private Date uploadDate;

	/**
	 * 文件大小
	 */
	@Column(type = ColumnType.INT)
	private Long fileSize;

	/**
	 * 暂存目录
	 */
	@Column(type = ColumnType.VARCHAR)
	private String tmpPath;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getTmpPath() {
		return tmpPath;
	}

	public void setTmpPath(String tmpPath) {
		this.tmpPath = tmpPath;
	}
}
