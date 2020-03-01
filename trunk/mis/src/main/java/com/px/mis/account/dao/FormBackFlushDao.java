package com.px.mis.account.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.entity.FormBackFlush;

public interface FormBackFlushDao {
	
	List<FormBackFlush> selectIntoWhsByMap(Map<String,Object> map);
	//��������
	void insertRedFormBackFlushList(List<FormBackFlush> formBackList);
	//����ɾ���س嵥�Լ����ӱ�
	void delectFormBackFlushAndSub(List<FormBackFlush> backRedList);
	//��ѯ�س嵥�б�
	List<FormBackFlush> selectBackFlushByMap(Map map);
	
	int countSelectBackFlushByMap(Map map);
}
