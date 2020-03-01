package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyAcctiSysTab;

public interface InvtyAcctiSysTabDao {
	
	int insertInvtyAcctiSysTab(InvtyAcctiSysTab InvtyAcctiSysTab);
	
	int updateInvtyAcctiSysTabByOrdrNum(InvtyAcctiSysTab InvtyAcctiSysTab);
	
	int deleteInvtyAcctiSysTabByOrdrNum(Integer ordrNum);
	
	InvtyAcctiSysTab selectInvtyAcctiSysTabByOrdrNum(Integer ordrNum);

    List<InvtyAcctiSysTab> selectInvtyAcctiSysTabList(Map map);
    
    int selectInvtyAcctiSysTabCount();
}
