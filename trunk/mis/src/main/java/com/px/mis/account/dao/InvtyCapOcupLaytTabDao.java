package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyCapOcupLaytTab;

public interface InvtyCapOcupLaytTabDao {
	
	int insertInvtyCapOcupLaytTab(InvtyCapOcupLaytTab invtyCapOcupLaytTab);
	
	int updateInvtyCapOcupLaytTabByOrdrNum(InvtyCapOcupLaytTab invtyCapOcupLaytTab);
	
	int deleteInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum);
	
	InvtyCapOcupLaytTab selectInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum);

    List<InvtyCapOcupLaytTab> selectInvtyCapOcupLaytTabList(Map map);
    
    int selectInvtyCapOcupLaytTabCount();
}
