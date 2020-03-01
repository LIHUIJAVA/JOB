package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.quartz.ListenerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogType;
import com.px.mis.ec.service.LogRecordService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class LogRecordServiceImpl implements LogRecordService {

	private Logger logger = LoggerFactory.getLogger(LogRecordServiceImpl.class);

	@Autowired
	private LogRecordDao logRecordDao;

	@Override
	public String add(LogRecord logRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (logRecordDao.select(logRecord.getLogId()) != null) {
				message = "编号" + logRecord.getLogId() + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				logRecordDao.insert(logRecord);
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/logRecord/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/add 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String edit(LogRecord logRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (logRecordDao.select(logRecord.getLogId()) == null) {
				message = "编号" + logRecord.getLogId() + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				logRecordDao.update(logRecord);
				message = "修改成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/logRecord/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String delete(int goodId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (logRecordDao.select(goodId) == null) {
				message = "编号" + goodId + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				logRecordDao.delete(goodId);
				message = "删除成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/logRecord/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(int goodId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		try {
			LogRecord logRecord = logRecordDao.select(goodId);
			if (logRecord == null) {
				message = "编号" + goodId + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/logRecord/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/logRecord/query", isSuccess, message, logRecord);
			}
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<LogRecord> lList = logRecordDao.selectList(map);
			int count = logRecordDao.selectCount(map);
			message = "查询成功";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/logRecord/queryList", isSuccess, message, count, pageNo, pageSize,
					0, 0, lList);
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/queryList 异常说明：", e);
		}
		return resp;
	}
	
	@Override
	public String logRecordList(String ecOrderId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<LogRecord> logRecords = logRecordDao.logRecordList(ecOrderId);
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/logRecord/logRecordList", isSuccess, message, 1, 1, 1,
					0, 0, logRecords);
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/logRecordList 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String logTypeList() {
		// TODO Auto-generated method stub
		String resp="";
		List<LogType> logTypes = logRecordDao.logTypeList();
		try {
			resp = BaseJson.returnRespList("ec/logRecord/queryList", true, "查询成功", 1, 1, 1,
					0, 0, logTypes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String exportList(Map map) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<Map> lList = logRecordDao.exportList(map);
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespObjList("ec/logRecord/exportList", isSuccess, message,null, lList);
		} catch (IOException e) {
			logger.error("URL：ec/logRecord/exportList 异常说明：", e);
		}
		return resp;
	}

}
