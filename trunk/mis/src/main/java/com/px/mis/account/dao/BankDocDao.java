package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.BankDoc;

public interface BankDocDao {
	
	Integer insertBankDoc(BankDoc BankDoc);
	
	Integer updateBankDocByOrdrNum(BankDoc BankDoc);
	
	Integer deleteBankDocByOrdrNum(List<String> bankEncd);
	
	BankDoc selectBankDocByOrdrNum(String bankEncd);

    List<BankDoc> selectBankDocList(Map map);
    
    Integer selectBankDocCount();
}
