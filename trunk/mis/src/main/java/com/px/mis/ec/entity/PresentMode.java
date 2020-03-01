package com.px.mis.ec.entity;
//赠品方式表
public class PresentMode {
	private Integer no;//序号
	private String presentModeCode;//赠品方式编码
	private String presentModeName;//赠品方式名称
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
