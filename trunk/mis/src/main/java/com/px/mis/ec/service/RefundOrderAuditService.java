package com.px.mis.ec.service;

public interface RefundOrderAuditService {
	public String refundOrderAudit(String refId,String userId,String loginDate);//�˿����ˣ�
	public String refundOrderAbandonAudit(String refId,String userId);//�˿������
}
