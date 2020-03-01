package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.CurmthIntoWhsCostTab;

public interface CurmthIntoWhsCostTabService {

	ObjectNode insertCurmthIntoWhsCostTab(CurmthIntoWhsCostTab curmthIntoWhsCostTab);
	
	ObjectNode updateCurmthIntoWhsCostTabByOrdrNum(CurmthIntoWhsCostTab curmthIntoWhsCostTab);
	
	ObjectNode deleteCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum);
	
	CurmthIntoWhsCostTab selectCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum);
	
    String selectCurmthIntoWhsCostTabList(Map map);
}
