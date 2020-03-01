package com.px.mis.purc.entity.PayApplFormForU8;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataRow")
public class U8PayApplForm {

	private String dscode;// ���̵��ݺ�

	private String csscode;// u8���㷽ʽ����

	private String ddate;// ��������

	private String cdepcode;// u8���ű���

	private String cvencode;// u8��Ӧ�̱���

	private BigDecimal ipaymoney;// ������ϼ�

	private String caccount;// �����˺�

	private String cbank;// ��������

	private BigDecimal itaxrate;// ��ͷ˰��

	private String cpersoncode;// ҵ��Ա����

	private String remark;// ��ע

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
