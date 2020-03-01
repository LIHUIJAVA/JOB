package com.px.mis.account.entity.PursComnInvForU8;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class U8PursComnInv {
	//这相当于主表对象
	private String dscode;// 电商单据号
	private String vouttype;// 发票类型 01专票 02普票
	private String cptcode;// u8采购类型编码
	private String ddate;// 单据日期
	private String cdepcode;// u8部门编码
	private String cvencode;// u8供应商编码
	private BigDecimal itaxrate;// 表头税率
	private String cpersoncode;// 业务员编码
	private String remark;// 备注
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "DataDetails")
	private ArrayList<U8PursComnInvSub> subList;


	

	

	public ArrayList<U8PursComnInvSub> getSubList() {
		return subList;
	}

	public void setSubList(ArrayList<U8PursComnInvSub> subList) {
		this.subList = subList;
	}

	public String getDscode() {
		return dscode;
	}

	public String getCvencode() {
		return cvencode;
	}

	public void setCvencode(String cvencode) {
		this.cvencode = cvencode;
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

	

	public String getCptcode() {
		return cptcode;
	}

	public void setCptcode(String cptcode) {
		this.cptcode = cptcode;
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

}
