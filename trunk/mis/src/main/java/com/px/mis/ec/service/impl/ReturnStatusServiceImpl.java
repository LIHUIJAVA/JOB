package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ReturnStatusDao;
import com.px.mis.ec.entity.ReturnStatus;
import com.px.mis.ec.service.ReturnStatusService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ReturnStatusServiceImpl implements ReturnStatusService {

	private Logger logger = LoggerFactory.getLogger(ReturnStatusServiceImpl.class);

	@Autowired
	private ReturnStatusDao returnStatusDao;

	@Override
	public String add(ReturnStatus returnStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (returnStatusDao.select(returnStatus.getReturnStatusId()) != null) {
				message = "���" + returnStatus.getReturnStatusId() + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				returnStatusDao.insert(returnStatus);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/returnStatus/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/returnStatus/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String edit(ReturnStatus returnStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (returnStatusDao.select(returnStatus.getReturnStatusId()) == null) {
				message = "���" + returnStatus.getReturnStatusId() + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				returnStatusDao.update(returnStatus);
				message = "�޸ĳɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/returnStatus/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/returnStatus/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String delete(String returnStatusId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (returnStatusDao.select(returnStatusId) == null) {
				message = "���" + returnStatusId + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				returnStatusDao.delete(returnStatusId);
				message = "ɾ���ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/returnStatus/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/returnStatus/delete �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String returnStatusId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ReturnStatus returnStatus = returnStatusDao.select(returnStatusId);
			if (returnStatus == null) {
				message = "���" + returnStatusId + "�����ڣ����������룡";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/returnStatus/query", isSuccess, message, null);
			} else {
				message = "��ѯ�ɹ�";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/returnStatus/query", isSuccess, message, returnStatus);
			}
		} catch (IOException e) {
			logger.error("URL��ec/returnStatus/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<ReturnStatus> mList = returnStatusDao.selectList(map);
			int count = returnStatusDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/returnStatus/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, mList);
		} catch (IOException e) {
			logger.error("URL��ec/returnStatus/queryList �쳣˵����", e);
		}
		return resp;
	}

}
