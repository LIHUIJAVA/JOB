package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.AcctItmDoc;
import com.px.mis.account.entity.InvtyCntPtySubjSetTab;

public interface InvtyCntPtySubjSetTabDao {
	
	int insertInvtyCntPtySubjSetTab(InvtyCntPtySubjSetTab invtyCntPtySubjSetTab);
	
	int updateInvtyCntPtySubjSetTabByOrdrNum(InvtyCntPtySubjSetTab invtyCntPtySubjSetTab);
	
	int deleteInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum);
	
	InvtyCntPtySubjSetTab selectInvtyCntPtySubjSetTabByOrdrNum(Integer ordrNum);

    List<InvtyCntPtySubjSetTab> selectInvtyCntPtySubjSetTabList(Map map);
    
    int selectInvtyCntPtySubjSetTabCount();
    
    int deleteInvtyCntPtySubjSetTabList(Map ordrNum);
    
}
