package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyPayblSubj;

public interface InvtyPayblSubjDao {
	
	int insertInvtyPayblSubj(InvtyPayblSubj InvtyPayblSubj);
	
	int updateInvtyPayblSubjById(InvtyPayblSubj InvtyPayblSubj);
	
	Integer deleteInvtyPayblSubjById(Integer incrsId);
	
	InvtyPayblSubj selectInvtyPayblSubjById(Integer incrsId);

    List<InvtyPayblSubj> selectInvtyPayblSubjList(Map map);
    
    int selectInvtyPayblSubjCount();
    
    String selectProvrClsEncd(String provrClsEncd);
    
    int deleteInvtyPayblSubjList(Map incrsId);
}
