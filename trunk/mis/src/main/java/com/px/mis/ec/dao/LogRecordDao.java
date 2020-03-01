package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogType;

public interface LogRecordDao  {

	/**
	 * 日志插入
	 * @param logRecord
	 * type：
	 * 1  新增
		2审核
		3弃审
		4拆单
		5修改
		6合并
		7关闭
		8发货
		9自动下载
		10手工下载
		11自动匹配
		12退款下载
		13删除
		14取电子面单
		15打印快递单
		16导入订单
		17取消面单
		18强制发货
		19取消发货
		20物流表删除
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
