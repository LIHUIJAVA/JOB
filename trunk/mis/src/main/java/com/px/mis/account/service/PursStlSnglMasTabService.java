package com.px.mis.account.service;

import java.util.Map;

import com.px.mis.account.entity.PursStlSnglMasTab;

public interface PursStlSnglMasTabService {

	String addPursStlSnglMasTab(String userId,PursStlSnglMasTab pursStlSnglMasTab,String loginTime);
	
	String updatePursStlSnglMasTab(PursStlSnglMasTab pursStlSnglMasTab);
	
//	String deletePursStlSnglMasTabByStlSnglId(String stlSnglId);
	
	String deletePursStlSnglMasTabList(String stlSnglId);
	
	String selectPursStlSnglMasTabByStlSnglId(String stlSnglId);
	
    String selectPursStlSnglMasTabList(Map map);
}
