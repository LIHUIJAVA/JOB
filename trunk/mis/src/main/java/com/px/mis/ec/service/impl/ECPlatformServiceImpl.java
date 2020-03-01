package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ECPlatformDao;
import com.px.mis.ec.entity.ECPlatform;
import com.px.mis.ec.service.ECPlatformService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ECPlatformServiceImpl implements ECPlatformService {

	private Logger logger = LoggerFactory.getLogger(ECPlatformServiceImpl.class);

	@Autowired
	private ECPlatformDao ecPlatformDao;

	@Override
	public String add(ECPlatform ecPlatform) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (ecPlatformDao.select(ecPlatform.getEcId()) != null) {
			message = "���" + ecPlatform.getEcId() + "�Ѵ��ڣ����������룡";
			isSuccess = false;
		} else {
			ecPlatformDao.insert(ecPlatform);
			message = "�����ɹ���";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/ecPlatform/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:ec/ecPlatform/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String edit(ECPlatform ecPlatform) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (ecPlatformDao.select(ecPlatform.getEcId()) == null) {
			message = "���" + ecPlatform.getEcId() + "�����ڣ����������룡";
			isSuccess = false;
		} else {
			ecPlatformDao.update(ecPlatform);
			message = "�޸ĳɹ���";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/ecPlatform/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:ec/ecPlatform/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String delete(String brokId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (ecPlatformDao.select(brokId) == null) {
			message = "���" + brokId + "�����ڣ����������룡";
			isSuccess = false;
		} else {
			ecPlatformDao.delete(brokId);
			message = "ɾ���ɹ���";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/ecPlatform/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:ec/ecPlatform/delete �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String brokId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		ECPlatform ecPlatform = ecPlatformDao.select(brokId);

		try {
			if (ecPlatform == null) {
				message = "���" + brokId + "�����ڣ����������룡";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/ecPlatform/query", isSuccess, message, null);
			} else {
				message = "��ѯ�ɹ�";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/ecPlatform/query", isSuccess, message, ecPlatform);
			}
		} catch (IOException e) {
			logger.error("URL:ec/ecPlatform/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			List<ECPlatform> eList = ecPlatformDao.selectList(map);
			int count = ecPlatformDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/ecPlatform/queryList", isSuccess, message, count, pageNo, pageSize, 0, 0,
					eList);
		} catch (IOException e) {
			logger.error("URL:ec/ecPlatform/queryList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryAll(String jsonBody) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<Map<String, String>> eList = ecPlatformDao.selectAll();
		message = "��ѯ�ɹ�";
		isSuccess = true;
		try {
			resp = BaseJson.returnRespList("ec/ecPlatform/queryAll", isSuccess, message, eList);
		} catch (IOException e) {
			logger.error("URL:ec/ecPlatform/queryAll �쳣˵����", e);
		}
		return resp;
	}

}
