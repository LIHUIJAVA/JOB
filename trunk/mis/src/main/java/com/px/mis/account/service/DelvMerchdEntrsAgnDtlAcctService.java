package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.DelvMerchdEntrsAgnDtlAcct;

public interface DelvMerchdEntrsAgnDtlAcctService {

	ObjectNode insertDelvMerchdEntrsAgnDtlAcct(DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct);
	
	ObjectNode updateDelvMerchdEntrsAgnDtlAcctByOrdrNum(DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct);
	
	ObjectNode deleteDelvMerchdEntrsAgnDtlAcctByOrdrNum(Integer ordrNum);
	
	DelvMerchdEntrsAgnDtlAcct selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(Integer ordrNum);
	
    String selectDelvMerchdEntrsAgnDtlAcctList(Map map);
}
