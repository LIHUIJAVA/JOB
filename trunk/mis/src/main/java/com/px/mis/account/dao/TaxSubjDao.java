package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.TaxSubj;

public interface TaxSubjDao {
	
	int insertTaxSubj(TaxSubj TaxSubj);
	
	int updateTaxSubjById(TaxSubj TaxSubj);
	
	int deleteTaxSubjById(List<String> autoId);
	
	TaxSubj selectTaxSubjById(Integer autoId);

    List<TaxSubj> selectTaxSubjList(Map map);
    
    int selectTaxSubjCount();
}
