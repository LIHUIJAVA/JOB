package com.px.mis.account.entity.SellComnInvForU8;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataTable")
public class U8SellComnInvTable {

	@JacksonXmlProperty(localName = "DataRow")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<U8SellComnInv> dataRowList;

	public ArrayList<U8SellComnInv> getDataRowList() {
		return dataRowList;
	}

	public void setDataRowList(ArrayList<U8SellComnInv> dataRowList) {
		this.dataRowList = dataRowList;
	}

}
