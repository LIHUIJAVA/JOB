package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.PursInvSubTab;

public interface PursInvSubTabDao{
	
	//新增采购专用发票子表
	int insertPursInvSubTab(PursInvSubTab pursInvSubTab);
	
	//修改采购专用发票子表
	int updatePursInvSubTabByOrdrNum(PursInvSubTab pursInvSubTab);
	
	//删除采购专用发票子表
	int deletePursInvSubTabByOrdrNum(String pursInvNum);
	
	//按照序号查询采购专用发票子表信息
	PursInvSubTab selectPursInvSubTabByOrdrNum(Integer ordrNum);
	
    List<PursInvSubTab> selectPursInvSubTabList(Map map);
    
    int selectPursInvSubTabCount();
    
    //新增采购专用发票子表
  	int insertPursInvSubTabUpload(List<PursInvSubTab> pursInvSubTab);
    
}
