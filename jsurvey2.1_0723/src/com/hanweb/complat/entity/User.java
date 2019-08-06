package com.hanweb.complat.entity;

import java.io.Serializable;
import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 用户实体
 * 
 * @author ZhangC
 * 
 */
@Table(name = Tables.USER)
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3689648473909509892L;

	/**
	 * 主键
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * uuid
	 */
	@Column(type = ColumnType.VARCHAR, length = 32, update = false, unique = true)
	private String uuid;

	/**
	 * 登陆名（唯一）
	 */
	@Column(type = ColumnType.VARCHAR, length = 100, update = false, unique = true)
	private String loginName;

	/**
	 * 密码
	 */
	@Column(type = ColumnType.VARCHAR, length = 100, update = false)
	private String pwd;

	/**
	 * 姓名
	 */
	@Column(type = ColumnType.VARCHAR, length = 100)
	private String name;

	/**
	 * 机构ID
	 */
	@Column(type = ColumnType.INT)
	private Integer groupId = 0;

	/**
	 * 年龄
	 */
	@Column(type = ColumnType.INT, length = 3)
	private Integer age = 0;

	/**
	 * 性别
	 */
	@Column(type = ColumnType.INT, length = 1)
	private Integer sex = 0;

	/**
	 * 是否有效
	 */
	@Column(type = ColumnType.INT, length = 1, update = false)
	private Integer enable = 1;

	/**
	 * 创建时间
	 */
	@Column(type = ColumnType.DATETIME, update = false)
	private Date createtime;

	/**
	 * 拼音的首字母
	 */
	@Column(type = ColumnType.VARCHAR)
	private String pinYin;

	/**
	 * 移动电话
	 */
	@Column(type = ColumnType.VARCHAR)
	private String mobile;

	/**
	 * 固定电话
	 */
	@Column(type = ColumnType.VARCHAR)
	private String phone;

	/**
	 * email
	 */
	@Column(type = ColumnType.VARCHAR)
	private String email;

	/**
	 * 地址
	 */
	@Column(type = ColumnType.VARCHAR)
	private String address;

	/**
	 * 联系方式
	 */
	@Column(type = ColumnType.VARCHAR)
	private String contact;

	/**
	 * 职务
	 */
	@Column(type = ColumnType.VARCHAR)
	private String headship;

	/**
	 * 常用登录地址
	 */
	@Column(type = ColumnType.VARCHAR)
	private String commonRegion;

	/**
	 * 动态码密钥
	 */
	@Column(type = ColumnType.VARCHAR, update = false)
	private String dynamicCodeKey;

	public Integer getIid() {
		return iid;
	}

	public void setIid(Integer iid) {
		this.iid = iid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHeadship() {
		return headship;
	}

	public void setHeadship(String headship) {
		this.headship = headship;
	}

	public String getCommonRegion() {
		return commonRegion;
	}

	public void setCommonRegion(String commonRegion) {
		this.commonRegion = commonRegion;
	}

	public String getDynamicCodeKey() {
		return dynamicCodeKey;
	}

	public void setDynamicCodeKey(String dynamicCodeKey) {
		this.dynamicCodeKey = dynamicCodeKey;
	}
}
