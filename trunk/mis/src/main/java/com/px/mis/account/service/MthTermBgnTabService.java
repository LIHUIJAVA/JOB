package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.MthTermBgnTab;

public interface MthTermBgnTabService {

	ObjectNode insertMthTermBgnTab(MthTermBgnTab mthTermBgnTab);
	
	ObjectNode updateMthTermBgnTabByOrdrNum(MthTermBgnTab mthTermBgnTab);
	
	ObjectNode deleteMthTermBgnTabByOrdrNum(Integer ordrNum);
	
	MthTermBgnTab selectMthTermBgnTabByOrdrNum(Integer ordrNum);
	
    String selectMthTermBgnTabList(Map map);
}
