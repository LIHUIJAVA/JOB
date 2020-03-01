package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.px.mis.ec.entity.CancelOrder;

public interface CancelOrderDao  {

	public void insert(List<CancelOrder> cancelOrderList);
	
	public void update(CancelOrder cancelOrder);
	
	public List<CancelOrder> selectList(Map map);
	
	public List<CancelOrder> selectByIds(List<String> idList);
	
	public int selectCount(Map map);

	int audit(@Param("idList") List<String> idList,@Param("userId")String userId,@Param("userName")String userName,@Param("auditTime")String auditTime);
	
	public CancelOrder selectById(String id);
	
	public void delete(CancelOrder cancelOrder);
	
}
