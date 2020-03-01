package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.MemberRecord;

public interface MemberRecordDao  {

	public void insert(MemberRecord memberRecord);
	
	public void update(MemberRecord memberRecord);
	
	public void delete(String memId);
	
	public MemberRecord select(String memId);
	
	public List<MemberRecord> selectList(Map map);
	
	public int selectCount(Map map);
	
}
