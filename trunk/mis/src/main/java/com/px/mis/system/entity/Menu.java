package com.px.mis.system.entity;

public class Menu {
	
	private String id;		//�˵����
	private String name;	//�˵�����
	private int level;		//�˵��ȼ�
	private String ico;		//�˵�ͼ��
	private String url;		//�˵�url
	private String pid;		//�ϼ��˵����
	private String targetType;//target����
	
	
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	
	
	

}
