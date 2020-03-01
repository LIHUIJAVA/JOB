package com.px.mis.account.entity.SellComnInvForU8;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataRow")
public class U8SellComnInv {
	private String dscode;// 电商单据号
	
	private String vouttype;// 发票类型 26专票 27普票
	
	private String cstcode;// u8销售类型编码
	
	private String ddate;// 单据日期
	
	private String cdepcode;// u8部门编码
	
	private String ccuscode;// u8客戶编码
	
	private String ccusbank;//客户银行 专票必填
	
	private String ccusaccount;//客户账号 专票必填
	
	private BigDecimal itaxrate;// 表头税率
	
	private String cpersoncode;// 业务员编码
	
	private String remark;// 备注
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "DataDetails")
	private List<U8SellComnInvSub> subList;
	public String getDscode() {
		return dscode;
	}
	public void setDscode(String dscode) {
		this.dscode = dscode;
	}
	public String getVouttype() {
		return vouttype;
	}
	public void setVouttype(String vouttype) {
		this.vouttype = vouttype;
	}
	public String getCstcode() {
		return cstcode;
	}
	public void setCstcode(String cstcode) {
		this.cstcode = cstcode;
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
	public String getCcuscode() {
		return ccuscode;
	}
	public void setCcuscode(String ccuscode) {
		this.ccuscode = ccuscode;
	}
	public String getCcusbank() {
		return ccusbank;
	}
	public void setCcusbank(String ccusbank) {
		this.ccusbank = ccusbank;
	}
	public String getCcusaccount() {
		return ccusaccount;
	}
	public void setCcusaccount(String ccusaccount) {
		this.ccusaccount = ccusaccount;
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
	public List<U8SellComnInvSub> getSubList() {
		return subList;
	}
	public void setSubList(List<U8SellComnInvSub> subList) {
		this.subList = subList;
	}
	
	
	

}
