package com.px.mis.account.entity.PursComnInvForU8;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "datarow")
public class U8PursComnInvResponse {
	private String dscode;
	private int type;
	private String infor;
	
	
	public U8PursComnInvResponse() {
		super();
	}

	public U8PursComnInvResponse(String dscode, int type, String infor) {
		super();
		this.dscode = dscode;
		this.type = type;
		this.infor = infor;
	}

	public String getDscode() {
		return dscode;
	}

	public void setDscode(String dscode) {
		this.dscode = dscode;
	}



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getInfor() {
		return infor;
	}

	public void setInfor(String infor) {
		this.infor = infor;
	}

}
