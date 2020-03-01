package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.CurmthIntoWhsCostTab;

public interface CurmthIntoWhsCostTabDao {
	
	int insertCurmthIntoWhsCostTab(CurmthIntoWhsCostTab curmthIntoWhsCostTab);
	
	int updateCurmthIntoWhsCostTabById(CurmthIntoWhsCostTab curmthIntoWhsCostTab);
	
	int deleteCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum);
	
	CurmthIntoWhsCostTab selectCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum);

    List<CurmthIntoWhsCostTab> selectCurmthIntoWhsCostTabList(Map map);
    
    int selectCurmthIntoWhsCostTabCount();
}
