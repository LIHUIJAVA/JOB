package com.px.mis.account.entity.VoucherForU8;



import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "header")
public class U8Voucher {
	private String dscode;// 电商单据号
	
	private String fyType;//费用类型
	
	private String ddate;// 2015-02-10 凭证日期
	
	private String cdepcode;// u8部门编码
	
	private String ino_id;//凭证号
	
	private String csign;//凭证类别
	
	private String remark;//备注
	

	
	@JacksonXmlElementWrapper(useWrapping = true,localName = "body")
	@JacksonXmlProperty(localName = "entry")
	private List<U8VoucherSub> subList;



	public String getDscode() {
		return dscode;
	}



	public void setDscode(String dscode) {
		this.dscode = dscode;
	}



	public String getFyType() {
		return fyType;
	}



	public void setFyType(String fyType) {
		this.fyType = fyType;
	}



	public String getDdate() {
		return ddate;
	}



	public void setDdate(String ddate) {
		this.ddate = ddate;
	}



	public String getCdepcode() {
		return cdepcode;
	}



	public void setCdepcode(String cdepcode) {
		this.cdepcode = cdepcode;
	}



	public String getIno_id() {
		return ino_id;
	}



	public void setIno_id(String ino_id) {
		this.ino_id = ino_id;
	}



	public String getCsign() {
		return csign;
	}



	public void setCsign(String csign) {
		this.csign = csign;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public List<U8VoucherSub> getSubList() {
		return subList;
	}



	public void setSubList(List<U8VoucherSub> subList) {
		this.subList = subList;
	}




	
	
	
	
	

}
