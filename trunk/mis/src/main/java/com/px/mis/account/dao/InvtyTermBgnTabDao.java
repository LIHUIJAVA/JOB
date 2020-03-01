package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyTermBgnTab;

public interface InvtyTermBgnTabDao {
	
	int insertInvtyTermBgnTab(InvtyTermBgnTab invtyTermBgnTab);
	
	int updateInvtyTermBgnTabByOrdrNum(InvtyTermBgnTab invtyTermBgnTab);
	
	int deleteInvtyTermBgnTabByOrdrNum(Integer ordrNum);
	
	InvtyTermBgnTab selectInvtyTermBgnTabByOrdrNum(Integer ordrNum);

    List<InvtyTermBgnTab> selectInvtyTermBgnTabList(Map map);
    
    int selectInvtyTermBgnTabCount();
}
