package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.ReturnStatus;

public interface ReturnStatusService {

	public String add(ReturnStatus returnStatus);
	
	public String edit(ReturnStatus returnStatus);
	
	public String delete(String returnStatusId);
	
	public String query(String returnStatusId);
	
	public String queryList(Map map);
	
}
