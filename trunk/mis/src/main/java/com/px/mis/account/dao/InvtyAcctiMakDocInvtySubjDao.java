package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyAcctiMakDocInvtySubj;

public interface InvtyAcctiMakDocInvtySubjDao {
	
	int insertInvtyAcctiMakDocInvtySubj(InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj);
	
	int updateInvtyAcctiMakDocInvtySubjByOrdrNum(InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj);
	
	int deleteInvtyAcctiMakDocInvtySubjByOrdrNum(Integer ordrNum);
	
	InvtyAcctiMakDocInvtySubj selectInvtyAcctiMakDocInvtySubjByOrdrNum(Integer ordrNum);

    List<InvtyAcctiMakDocInvtySubj> selectInvtyAcctiMakDocInvtySubjList(Map map);
    
    int selectInvtyAcctiMakDocInvtySubjCount();
}
