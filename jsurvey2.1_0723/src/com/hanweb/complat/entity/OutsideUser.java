package com.hanweb.complat.entity;

import java.util.Date;

import com.hanweb.common.annotation.Column;
import com.hanweb.common.annotation.ColumnType;
import com.hanweb.common.annotation.Id;
import com.hanweb.common.annotation.Table;
import com.hanweb.complat.constant.Tables;

/**
 * 外网用户实体
 * 
 * @author ZhangC
 * 
 */
@Table(name = Tables.OUTSIDEUSER)
public class OutsideUser implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2506351625961775795L;

	/**
	 * 主键id
	 */
	@Id
	@Column(type = ColumnType.INT)
	private Integer iid;

	/**
	 * uuid
	 */
	@Column(type = ColumnType.VARCHAR, length = 32, update = false)
	private String uuid;

	/**
	 * 登录id
	 */
	@Column(type = ColumnType.VARCHAR, update = false)
	private String loginName;

	/**
	 * 登录密码
	 */
	@Column(type = ColumnType.VARCHAR, update = false)
	private String pwd;

	/**
	 * 姓名
	 */
	@Column(type = ColumnType.VARCHAR)
	private String name;

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
	@Column(type = ColumnType.INT, length = 1)
	private Integer enable = 1;

	/**
	 * 全拼
	 */
	@Column(type = ColumnType.VARCHAR)
	private String pinYin;

	/**
	 * 证件类型 1 身份证 <br>
	 * 2 教师证<br>
	 * 3 军官证<br>
	 * 4 其他
	 */
	@Column(type = ColumnType.INT, length = 1)
	private Integer papersType = 1;

	/**
	 * 证件编号
	 */
	@Column(type = ColumnType.VARCHAR)
	private String papersNumber;

	/**
	 * 备注
	 */
	@Column(type = ColumnType.VARCHAR)
	private String description;

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
	 * 联系方式
	 */
	@Column(type = ColumnType.VARCHAR)
	private String contact;

	/**
	 * 联系住址
	 */
	@Column(type = ColumnType.VARCHAR)
	private String address;

	/**
	 * 邮编
	 */
	@Column(type = ColumnType.VARCHAR)
	private String post;

	/**
	 * 工作单位
	 */
	@Column(type = ColumnType.VARCHAR)
	private String workUnit;

	/**
	 * 职务
	 */
	@Column(type = ColumnType.VARCHAR)
	private String headship;

	/**
	 * 生日
	 */
	@Column(type = ColumnType.DATETIME)
	private Date birthDate;

	/**
	 * 上次登录时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date loginTime;

	/**
	 * 上次登录ip
	 */
	@Column(type = ColumnType.VARCHAR)
	private String loginIp;

	/**
	 * 注册ip
	 */
	@Column(type = ColumnType.VARCHAR)
	private String regip;

	/**
	 * 注册时间
	 */
	@Column(type = ColumnType.DATETIME)
	private Date regtime;

	/**
	 * 注册地点
	 */
	@Column(type = ColumnType.VARCHAR)
	private String regsite;

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

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public Integer getPapersType() {
		return papersType;
	}

	public void setPapersType(Integer papersType) {
		this.papersType = papersType;
	}

	public String getPapersNumber() {
		return papersNumber;
	}

	public void setPapersNumber(String papersNumber) {
		this.papersNumber = papersNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getHeadship() {
		return headship;
	}

	public void setHeadship(String headship) {
		this.headship = headship;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	public Date getRegtime() {
		return regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public String getRegsite() {
		return regsite;
	}

	public void setRegsite(String regsite) {
		this.regsite = regsite;
	}

}
