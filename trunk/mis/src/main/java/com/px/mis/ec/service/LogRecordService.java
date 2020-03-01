package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.LogRecord;

public interface LogRecordService {

	public String add(LogRecord logRecord);
	
	public String edit(LogRecord logRecord);
	
	public String delete(int logId);
	
	public String query(int logId);
	
	public String queryList(Map map);
	
	public String logRecordList(String ecOrderId);
	
	public String logTypeList();
	
	public String exportList(Map map);
}
