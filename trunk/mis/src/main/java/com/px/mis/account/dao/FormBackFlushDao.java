package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.entity.FormBackFlush;

public interface FormBackFlushDao {
	
	List<FormBackFlush> selectIntoWhsByMap(Map<String,Object> map);
	//批量新增
	void insertRedFormBackFlushList(List<FormBackFlush> formBackList);
	//批量删除回冲单以及其子表
	void delectFormBackFlushAndSub(List<FormBackFlush> backRedList);
	//查询回冲单列表
	List<FormBackFlush> selectBackFlushByMap(Map map);
	
	int countSelectBackFlushByMap(Map map);
}
