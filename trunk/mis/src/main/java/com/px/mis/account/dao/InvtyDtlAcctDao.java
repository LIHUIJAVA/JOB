package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.InvtyDtlAcct;

public interface InvtyDtlAcctDao {
	
	int insertInvtyDtlAcct(InvtyDtlAcct invtyDtlAcct);
	
	int updateInvtyDtlAcctByOrdrNum(InvtyDtlAcct invtyDtlAcct);
	
	int deleteInvtyDtlAcctByOrdrNum(Integer ordrNum);
	
	InvtyDtlAcct selectInvtyDtlAcctByOrdrNum(Integer ordrNum);

    List<InvtyDtlAcct> selectInvtyDtlAcctList(Map map);
    
    int selectInvtyDtlAcctCount();
    //批量新增
	int inserDtlAccttList(List<InvtyDtlAcct> invtyDtlAcctList);
}
