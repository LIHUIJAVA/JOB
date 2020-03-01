package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.ProCondition;


public interface ProConditionDao {
	
	int insertProCondition(ProCondition proCondition);
	
	int updateProConditionById(ProCondition proCondition);
	
	int deleteProConditionById(Integer no);
	
	ProCondition selectProConditionById(Integer no);
	
	ProCondition selectProConditionByProConditionEncd(Integer proConditionEncd);

    List<ProCondition> selectProConditionList(Map map);
    
    int selectProConditionCount();
}
