package com.px.mis.ec.service;

import java.util.Map;

public interface AssociatedSearchService {
	public String quickSearchByOrderId(String orderId);//������ѯ��
	
	public String reverseOperationByOrderId(String orderId);//���������
	
	public String orderAuditByOrderId(String orderId,String accNum,String loginDate);//������ˣ�
	
	public String orderAbandonAuditByOrderId(String orderId,String accNum);//��������
	
	public String selectList(Map map);
	
}
