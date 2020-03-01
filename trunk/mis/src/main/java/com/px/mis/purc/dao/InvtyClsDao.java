package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.InvtyCls;

public interface InvtyClsDao {

	int insertInvtyCls(InvtyCls invtyCls);
	
	int updateInvtyClsByInvtyClsEncd(InvtyCls invtyCls);
	
	int deleteInvtyClsByInvtyClsEncd(String invtyClsEncd);
	
	InvtyCls selectInvtyClsByInvtyClsEncd(String invtyClsEncd);

	List<InvtyCls> selectInvtyCls();
	//查询上级存货分类
	InvtyCls selectInvtyClsSuper(String invtyClsEncd);
}