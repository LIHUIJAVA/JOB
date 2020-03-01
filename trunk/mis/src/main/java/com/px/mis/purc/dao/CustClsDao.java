package com.px.mis.purc.dao;

import java.util.List;

import com.px.mis.purc.entity.CustCls;

public interface CustClsDao {
	
	//�����ͻ�����
	int insertCustCls(CustCls custCls);
	
	//�޸Ŀͻ�����
	int updateCustClsByClsId(CustCls custCls);
	
	//ɾ���ͻ�����
	int deleteCustClsByClsId(String clsId);
	
	//��ѯ�ͻ�������ϸ
	CustCls selectCustClsByClsId(String clsId);
	
	//��ѯ���пͻ�������Ϣ
	List<CustCls> selectCustCls();
	
}