package com.px.mis.ec.service;

import org.apache.ibatis.annotations.Param;

public interface RefundOrderService {

	public String add(String jsonBody);
	/**
	 * ����
	 * @param refId
	 * @return
	 */
	public String unionQuery(String refId);
	
	public String edit(String jsonBody);
	
	public String delete(String jsonBody);
	
	public String query(String jsonBody);
	
	public String queryList(String jsonBody);
	//�˿����
	public String refundReference(@Param("ecOrderId")String ecOrderId,@Param("orderId")String orderId);
	//�˿���
	public String audit(String jsonBody);
	//�˿����
	public String noAudit(String jsonBody);
	//�����˻���
	public String download(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId,String ecOrderId);
	
	public String exportList(String jsonBody);
}
