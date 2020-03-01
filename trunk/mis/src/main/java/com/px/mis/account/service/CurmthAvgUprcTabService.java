package com.px.mis.account.service;

import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import com.px.mis.account.entity.CurmthAvgUprcTab;

public interface CurmthAvgUprcTabService {

	ObjectNode insertCurmthAvgUprcTab(CurmthAvgUprcTab curmthAvgUprcTab);
	
	ObjectNode updateCurmthAvgUprcTabByOrdrNum(CurmthAvgUprcTab curmthAvgUprcTab);
	
	ObjectNode deleteCurmthAvgUprcTabByOrdrNum(Integer ordrNum);
	
	CurmthAvgUprcTab selectCurmthAvgUprcTabByOrdrNum(Integer ordrNum);
	
    String selectCurmthAvgUprcTabList(Map map);
    
    List<CurmthAvgUprcTab> getCurmthAvgUprcList(Map<String,String> parameters);
}
