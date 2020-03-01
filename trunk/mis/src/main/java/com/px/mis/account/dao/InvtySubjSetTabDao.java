package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtySubjSetTab;

public interface InvtySubjSetTabDao {
	
	int insertInvtySubjSetTab(InvtySubjSetTab invtySubjSetTab);
	
	int updateInvtySubjSetTabByOrdrNum(InvtySubjSetTab invtySubjSetTab);
	
	int deleteInvtySubjSetTabByOrdrNum(Integer ordrNum);
	
	InvtySubjSetTab selectInvtySubjSetTabByOrdrNum(Integer ordrNum);

    List<InvtySubjSetTab> selectInvtySubjSetTabList(Map map);
    
    int selectInvtySubjSetTabCount();
    
    String selectInvtyBigClsEncd(String invtyBigClsEncd);
    
    int deleteInvtySubjSetTabList(Map ordrNum);
    
	
}

