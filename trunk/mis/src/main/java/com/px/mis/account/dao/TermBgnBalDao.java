package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.TermBgnBal;

public interface TermBgnBalDao {
	
	int insertTermBgnBal(TermBgnBal termBgnBal);
	
	int insertTermBgnBalUpload(TermBgnBal termBgnBal);
	
	int updateTermBgnBalByOrdrNum(TermBgnBal termBgnBal);
	
	List<TermBgnBal> selectTermBgnBalList(Map map);
    
    int selectTermBgnBalCount(Map map);
    
	int deleteTermBgnBalByOrdrNum(List<Integer> ordrNum);
	
	TermBgnBal selectTermBgnBalByOrdrNum(Integer ordrNum);

	List<TermBgnBal> selectTermBgnBal(Map<String, Object> map);
	//批量更新
	int updateTermIsBookOk(List<TermBgnBal> termList);
	
	List<TermBgnBal> selectBgnBalList(Map<String, Object> map);
}
