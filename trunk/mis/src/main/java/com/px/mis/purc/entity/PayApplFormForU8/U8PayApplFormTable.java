package com.px.mis.purc.entity.PayApplFormForU8;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "DataTable")
public class U8PayApplFormTable {

	@JacksonXmlProperty(localName = "DataRow")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<U8PayApplForm> dataRowList;

	public ArrayList<U8PayApplForm> getDataRowList() {
		return dataRowList;
	}

	public void setDataRowList(ArrayList<U8PayApplForm> dataRowList) {
		this.dataRowList = dataRowList;
	}

	

}
