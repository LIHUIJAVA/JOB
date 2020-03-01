package com.px.mis.ec.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.util.JacksonUtil;

public class JDApiHelper {

	public static String orderSearch(Map map) throws IOException {
		ObjectNode objectNode=JacksonUtil.getObjectNode("");
		
		objectNode.put("start_date", map.get("startDate").toString());
		objectNode.put("end_date", map.get("endDate").toString());
		objectNode.put("order_state", map.get("orderState").toString());
		objectNode.put("optional_fields", "itemInfoList,paymentConfirmTime,orderTotalPrice,orderPayment,orderRemark,venderRemark,pin,consigneeInfo,invoiceInfo,invoiceEasyInfo,balanceUsed,sellerDiscount");
		objectNode.put("page", map.get("pageNo").toString());
		objectNode.put("page_size", map.get("pageSize").toString());
		objectNode.put("sortType", map.get("sortType").toString());
		objectNode.put("dateType", map.get("dateType").toString());
		
		return objectNode.toString();
	}
	
	public static String goodsSearch(ObjectNode reqBody) throws IOException {
		ObjectNode objectNode=JacksonUtil.getObjectNode("");
		
		objectNode.put("start_date", "");
		
		return objectNode.toString();
	}
	
}
