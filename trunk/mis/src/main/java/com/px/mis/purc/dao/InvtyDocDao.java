package com.px.mis.purc.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.px.mis.purc.entity.InvtyDoc;

public interface InvtyDocDao {
	
	int insertInvtyDoc(InvtyDoc invtyDoc);
	
	int updateInvtyDocByInvtyDocEncd(InvtyDoc invtyDoc);
	
	int deleteInvtyDocByInvtyDocEncd(String invtyEncd);
	
	InvtyDoc selectInvtyDocByInvtyDocEncd(String invtyEncd);
	
	List<InvtyDoc> selectInvtyDocList(Map map);
	
	int selectInvtyDocCount(Map map);
	
	List<InvtyDoc> printingInvtyDocList(Map map);
	
	String selectInvtyEncd(String invtyEncd);
	
	int deleteInvtyDocList(List<String> invtyEncd);
	
	BigDecimal selectRefCost(String invtyEncd);
	
	InvtyDoc selectAllByInvtyEncd(String invtyEncd);
	
	List<InvtyDoc> selectInvtyEncdLike(Map map);
   /* int insertSelective(InvtyDoc record);

    int updateByPrimaryKeySelective(InvtyDoc record);*/

}