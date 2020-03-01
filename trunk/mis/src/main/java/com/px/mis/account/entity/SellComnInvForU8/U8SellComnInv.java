package com.px.mis.account.entity.SellComnInvForU8;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataRow")
public class U8SellComnInv {
	private String dscode;// ���̵��ݺ�
	
	private String vouttype;// ��Ʊ���� 26רƱ 27��Ʊ
	
	private String cstcode;// u8�������ͱ���
	
	private String ddate;// ��������
	
	private String cdepcode;// u8���ű���
	
	private String ccuscode;// u8�͑�����
	
	private String ccusbank;//�ͻ����� רƱ����
	
	private String ccusaccount;//�ͻ��˺� רƱ����
	
	private BigDecimal itaxrate;// ��ͷ˰��
	
	private String cpersoncode;// ҵ��Ա����
	
	private String remark;// ��ע
	
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
