package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.MemberRecord;
import com.px.mis.ec.entity.RefundOrderStatus;

public interface RefundOrderStatusDao  {

	public void insert(RefundOrderStatus refundOrderStatus);
	
	public void update(RefundOrderStatus refundOrderStatus);
	
	public void delete(String refStatusId);
	
	public RefundOrderStatus select(String refStatusId);
	
	public List<RefundOrderStatus> selectList(Map map);
	
	public int selectCount(Map map);
	
}
