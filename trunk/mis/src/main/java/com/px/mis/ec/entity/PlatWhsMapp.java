package com.px.mis.ec.entity;

/**
  *  ƽ̨�ֿ�ӳ��
 *
 */
public class PlatWhsMapp {
	
	private Integer id;
	private String type;//ƽ̨����
	private String online;//���ϱ���
	private String offline;//���±���
	private String onlineName;//��������
	private String offlineName;//��������
	
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
