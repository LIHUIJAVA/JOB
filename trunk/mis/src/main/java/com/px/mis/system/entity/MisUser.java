package com.px.mis.system.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MisUser {
	
	private String accNum; // �˺�
	
	private String password; // ����
	
	private String accSet; // ����
	
	private String userName; // �û�����
	
	private String depId; // ���ű��
	
	private String depName; // ��������
	
	private String roleId; // ��ɫ���
	
	private String roleName; // ��ɫ����
	
	private String phoneNo; // ��ϵ�绰
	
	private String email; // ��ҵ����

	private String createDate; // �û���������
	
	private String whsId; // �ֿ���
	
	private String whsName;//�ֿ�����
	
	
	public String getAccNum() {
		return accNum;
	}

	public void setAccNum(String accNum) {
		this.accNum = accNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccSet() {
		return accSet;
	}

	public void setAccSet(String accSet) {
		this.accSet = accSet;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getWhsId() {
		return whsId;
	}

	public void setWhsId(String whsId) {
		this.whsId = whsId == null ? "" : whsId;
	}

	public String getWhsName() {
		return whsName;
	}

	public void setWhsName(String whsName) {
		this.whsName = whsName;
	}

	
	
}