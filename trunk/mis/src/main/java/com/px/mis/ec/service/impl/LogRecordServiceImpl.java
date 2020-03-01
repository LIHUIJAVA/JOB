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
				message = "���" + logRecord.getLogId() + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				logRecordDao.insert(logRecord);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/logRecord/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/add �쳣˵����", e);
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
				message = "���" + logRecord.getLogId() + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				logRecordDao.update(logRecord);
				message = "�޸ĳɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/logRecord/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/edit �쳣˵����", e);
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
				message = "���" + goodId + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				logRecordDao.delete(goodId);
				message = "ɾ���ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/logRecord/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/delete �쳣˵����", e);
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
				message = "���" + goodId + "�����ڣ����������룡";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/logRecord/query", isSuccess, message, null);
			} else {
				message = "��ѯ�ɹ�";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/logRecord/query", isSuccess, message, logRecord);
			}
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/query �쳣˵����", e);
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
			message = "��ѯ�ɹ�";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/logRecord/queryList", isSuccess, message, count, pageNo, pageSize,
					0, 0, lList);
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/queryList �쳣˵����", e);
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
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/logRecord/logRecordList", isSuccess, message, 1, 1, 1,
					0, 0, logRecords);
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/logRecordList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String logTypeList() {
		// TODO Auto-generated method stub
		String resp="";
		List<LogType> logTypes = logRecordDao.logTypeList();
		try {
			resp = BaseJson.returnRespList("ec/logRecord/queryList", true, "��ѯ�ɹ�", 1, 1, 1,
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
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespObjList("ec/logRecord/exportList", isSuccess, message,null, lList);
		} catch (IOException e) {
			logger.error("URL��ec/logRecord/exportList �쳣˵����", e);
		}
		return resp;
	}

}
