package com.px.mis.account.dao;

import java.util.List;

import com.px.mis.account.entity.PursStlSubTab;

public interface PursStlSubTabDao{
	
	int insertPursStlSubTab(List<PursStlSubTab> pursStlSubTabList);
	
	int deletePursStlSubTabByStlSnglId(String stlSnglId);
	
/*	int updatePursStlSubTab(PursStlSubTab pursStlSubTab);
	
	int deletePursStlSubTabByStlSnglNum(String stlSnglNum);
	
	PursStlSubTab selectPursStlSubTabByOrdrNum(Integer ordrNum);

    List<PursStlSubTab> selectPursStlSubTabList(Map map);
    
    int selectPursStlSubTabCount();*/
}
