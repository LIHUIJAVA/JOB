package com.px.mis.ec.service;

import org.apache.ibatis.annotations.Param;

public interface RefundOrderService {

	public String add(String jsonBody);
	/**
	 * 联查
	 * @param refId
	 * @return
	 */
	public String unionQuery(String refId);
	
	public String edit(String jsonBody);
	
	public String delete(String jsonBody);
	
	public String query(String jsonBody);
	
	public String queryList(String jsonBody);
	//退款参照
	public String refundReference(@Param("ecOrderId")String ecOrderId,@Param("orderId")String orderId);
	//退款单审核
	public String audit(String jsonBody);
	//退款单弃审
	public String noAudit(String jsonBody);
	//下载退货单
	public String download(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId,String ecOrderId);
	
	public String exportList(String jsonBody);
}
