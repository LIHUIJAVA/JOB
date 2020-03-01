package com.px.mis.ec.entity;


public class EcAccountMapp {
	private int id;
	private String platId;//平台id
	private String typeName;//类别名称
	private int is_check;//是否勾兑项
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlatId() {
		return platId;
	}
	public void setPlatId(String platId) {
		this.platId = platId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getIs_check() {
		return is_check;
	}
	public void setIs_check(int is_check) {
		this.is_check = is_check;
	}
	
	
}
