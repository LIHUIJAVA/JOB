package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.DelvMerchdEntrsAgnDtlAcct;

public interface DelvMerchdEntrsAgnDtlAcctDao {
	
	int insertDelvMerchdEntrsAgnDtlAcct(DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct);
	
	int updateDelvMerchdEntrsAgnDtlAcctByOrdrNum(DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct);
	
	int deleteDelvMerchdEntrsAgnDtlAcctByOrdrNum(Integer ordrNum);
	
	DelvMerchdEntrsAgnDtlAcct selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(Integer ordrNum);

    List<DelvMerchdEntrsAgnDtlAcct> selectDelvMerchdEntrsAgnDtlAcctList(Map map);
    
    int selectDelvMerchdEntrsAgnDtlAcctCount();
}
