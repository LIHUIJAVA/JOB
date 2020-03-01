package com.px.mis.purc.entity.PayApplFormForU8;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataRow")
public class U8PayApplForm {

	private String dscode;// 电商单据号

	private String csscode;// u8结算方式编码

	private String ddate;// 单据日期

	private String cdepcode;// u8部门编码

	private String cvencode;// u8供应商编码

	private BigDecimal ipaymoney;// 付款金额合计

	private String caccount;// 银行账号

	private String cbank;// 银行名称

	private BigDecimal itaxrate;// 表头税率

	private String cpersoncode;// 业务员编码

	private String remark;// 备注

	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "DataDetails")
	private List<U8PayApplFormSub> subList;

	public String getDscode() {
		return dscode;
	}

	public void setDscode(String dscode) {
		this.dscode = dscode;
	}

	public String getCsscode() {
		return csscode;
	}

	public void setCsscode(String csscode) {
		this.csscode = csscode;
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

	public String getCvencode() {
		return cvencode;
	}

	public void setCvencode(String cvencode) {
		this.cvencode = cvencode;
	}

	public BigDecimal getIpaymoney() {
		return ipaymoney;
	}

	public void setIpaymoney(BigDecimal ipaymoney) {
		this.ipaymoney = ipaymoney;
	}

	public String getCaccount() {
		return caccount;
	}

	public void setCaccount(String caccount) {
		this.caccount = caccount;
	}

	public String getCbank() {
		return cbank;
	}

	public void setCbank(String cbank) {
		this.cbank = cbank;
	}

	public BigDecimal getItaxrate() {
		return itaxrate;
	}

	public void setItaxrate(BigDecimal itaxrate) {
		this.itaxrate = itaxrate;
	}

	public String getCpersoncode() {
		return cpersoncode;
	}

	public void setCpersoncode(String cpersoncode) {
		this.cpersoncode = cpersoncode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<U8PayApplFormSub> getSubList() {
		return subList;
	}

	public void setSubList(List<U8PayApplFormSub> subList) {
		this.subList = subList;
	}

	
	

}
