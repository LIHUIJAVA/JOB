package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursStlSnglMasTab;

public interface PursStlSnglMasTabDao {
	
	//新增采购结算
	int insertPursStlSnglMasTab(PursStlSnglMasTab pursStlSnglMasTab);
	
	//修改采购结算
	int updatePursStlSnglMasTab(PursStlSnglMasTab pursStlSnglMasTab);
	
	//单个删除
	int deletePursStlSnglMasTabByStlSnglId(String stlSnglId);
	
	//批量删除
	int deletePursStlSnglMasTabList(List<String> stlSnglId);
	
	//按照采购结算编码详细信息
	PursStlSnglMasTab selectPursStlSnglMasTabByStlSnglId(String stlSnglId);
	
	//不带分页查询所有采购结算
	List<PursStlSnglMasTab> printingPursStlSnglMasTabList(Map map);

	//条件查询所有采购结算
    List<PursStlSnglMasTab> selectPursStlSnglMasTabList(Map map);
    
    int selectPursStlSnglMasTabCount();
    
    //查询审核状态
    int selectPursStlSnglMasTabIsNtChk(String stlSnglId);
}
