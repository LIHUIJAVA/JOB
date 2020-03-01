package com.px.mis.ec.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.ProCondition;

public interface ProConditionService {

	ObjectNode insertProCondition(ProCondition proCondition);
	
	ObjectNode updateProConditionById(ProCondition proCondition);
	
	ObjectNode deleteProConditionById(Integer no);
	
	ProCondition selectProConditionById(Integer no);
	
    String selectProConditionList(Map map);
}
