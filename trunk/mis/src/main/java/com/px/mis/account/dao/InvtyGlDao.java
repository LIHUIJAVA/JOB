package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyGl;

public interface InvtyGlDao {
	
	int insertInvtyGl(InvtyGl invtyGl);
	
	int updateInvtyGlByOrdrNum(InvtyGl invtyGl);
	
	int deleteInvtyGlByOrdrNum(Integer ordrNum);
	
	InvtyGl selectInvtyGlByOrdrNum(Integer ordrNum);

    List<InvtyGl> selectInvtyGlList(Map map);
    
    int selectInvtyGlCount();
}
