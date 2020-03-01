package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.MemberRecord;

public interface MemberRecordService {

	public String add(MemberRecord memberRecord);
	
	public String edit(MemberRecord memberRecord);
	
	public String delete(String memId);
	
	public String query(String memId);
	
	public String queryList(Map map);
}
