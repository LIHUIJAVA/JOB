package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.ReturnStatus;

public interface ReturnStatusDao  {

	public void insert(ReturnStatus returnStatus);
	
	public void update(ReturnStatus returnStatus);
	
	public void delete(String returnStatusId);
	
	public ReturnStatus select(String returnStatusId);
	
	public List<ReturnStatus> selectList(Map map);
	
	public int selectCount(Map map);
	
}
