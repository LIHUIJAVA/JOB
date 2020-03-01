package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.CurmthAvgUprcTab;

public interface CurmthAvgUprcTabDao {
	
	int insertCurmthAvgUprcTab(CurmthAvgUprcTab curmthAvgUprcTab);
	
	int updateCurmthAvgUprcTabByOrdrNum(CurmthAvgUprcTab curmthAvgUprcTab);
	
	int deleteCurmthAvgUprcTabByOrdrNum(Integer ordrNum);
	
	CurmthAvgUprcTab selectCurmthAvgUprcTabByOrdrNum(Integer ordrNum);

    List<CurmthAvgUprcTab> selectCurmthAvgUprcTabList(Map map);
    
    int selectCurmthAvgUprcTabCount();
    
    List<CurmthAvgUprcTab> getCurmthAvgUprcList(Map<String,String> parameters);
    
}
