package com.px.mis.ec.entity;
//��Ʒ��ʽ��
public class PresentMode {
	private Integer no;//���
	private String presentModeCode;//��Ʒ��ʽ����
	private String presentModeName;//��Ʒ��ʽ����
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public String getPresentModeCode() {
		return presentModeCode;
	}
	public void setPresentModeCode(String presentModeCode) {
		this.presentModeCode = presentModeCode;
	}
	public String getPresentModeName() {
		return presentModeName;
	}
	public void setPresentModeName(String presentModeName) {
		this.presentModeName = presentModeName;
	}
	@Override
	public String toString() {
		return "PresentMode [no=" + no + ", presentModeCode=" + presentModeCode + ", presentModeName=" + presentModeName
				+ "]";
	}
	
}
