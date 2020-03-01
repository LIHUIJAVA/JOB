package com.px.mis.ec.service;

public interface RefundOrderAuditService {
	public String refundOrderAudit(String refId,String userId,String loginDate);//退款单的审核；
	public String refundOrderAbandonAudit(String refId,String userId);//退款单的弃审；
}
