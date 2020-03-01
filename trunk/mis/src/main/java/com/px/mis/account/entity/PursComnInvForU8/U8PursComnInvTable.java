package com.px.mis.account.entity.PursComnInvForU8;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
@JacksonXmlRootElement(localName = "DataTable")
public class U8PursComnInvTable {
	/*
	 * ����ߵ�ʵ��,�������������� �ṹ������: DataTable DataRow DataRow DataRow
	 */
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "DataRow")
	private List<U8PursComnInv> rowList;

	public List<U8PursComnInv> getRowList() {
		return rowList;
	}

	public void setRowList(List<U8PursComnInv> rowList) {
		this.rowList = rowList;
	}

	@Override
	public String toString() {
		return "U8PursComnInvTable [rowList=" + rowList + "]";
	}

	

}
