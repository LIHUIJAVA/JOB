package com.px.mis.account.entity.VoucherForU8;



import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ufinterface")
public class U8VoucherTable {

	@JacksonXmlProperty(localName = "DataRow")
	@JacksonXmlElementWrapper(useWrapping = false)
	private ArrayList<U8Voucher> dataRowList;


}
