package com.px.mis.ec.entity;

/**
  *  平台仓库映射
 *
 */
public class PlatWhsMapp {
	
	private Integer id;
	private String type;//平台类型
	private String online;//线上编码
	private String offline;//线下编码
	private String onlineName;//线上名称
	private String offlineName;//线下名称
	
	public String getOnlineName() {
		return onlineName;
	}
	public void setOnlineName(String onlineName) {
		this.onlineName = onlineName;
	}
	public String getOfflineName() {
		return offlineName;
	}
	public void setOfflineName(String offlineName) {
		this.offlineName = offlineName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getOffline() {
		return offline;
	}
	public void setOffline(String offline) {
		this.offline = offline;
	}
	
}
