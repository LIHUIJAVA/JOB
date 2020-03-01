package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogType;

public interface LogRecordDao  {

	/**
	 * ��־����
	 * @param logRecord
	 * type��
	 * 1  ����
		2���
		3����
		4��
		5�޸�
		6�ϲ�
		7�ر�
		8����
		9�Զ�����
		10�ֹ�����
		11�Զ�ƥ��
		12�˿�����
		13ɾ��
		14ȡ�����浥
		15��ӡ��ݵ�
		16���붩��
		17ȡ���浥
		18ǿ�Ʒ���
		19ȡ������
		20������ɾ��
	 */
	public void insert(LogRecord logRecord);
	
	public void update(LogRecord logRecord);
	
	public void delete(int logId);
	
	public LogRecord select(int logId);
	
	public List<LogRecord> selectList(Map map);
	
	public List<Map> exportList(Map map);
	
	public int selectCount(Map map);
	
	public List<LogRecord> logRecordList(String ecOrderId);
	
	public List<LogType> logTypeList();
}
