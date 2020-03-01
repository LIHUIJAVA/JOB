package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.BrokerageDao;
import com.px.mis.ec.entity.Brokerage;
import com.px.mis.ec.service.BrokerageService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class BrokerageServiceImpl implements BrokerageService {

	private Logger logger = LoggerFactory.getLogger(BrokerageServiceImpl.class);

	@Autowired
	private BrokerageDao brokerageDao;

	@Override
	public String add(Brokerage brokerage) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (brokerageDao.select(brokerage.getBrokId()) != null) {
				message = "���" + brokerage.getBrokId() + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				brokerageDao.insert(brokerage);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/brokerage/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/brokerage/add �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String edit(Brokerage brokerage) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (brokerageDao.select(brokerage.getBrokId()) == null) {
				message = "���" + brokerage.getBrokId() + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				brokerageDao.update(brokerage);
				message = "�޸ĳɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/brokerage/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/brokerage/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String delete(String brokId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (brokerageDao.select(brokId) == null) {
				message = "���" + brokId + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				brokerageDao.delete(brokId);
				message = "ɾ���ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/brokerage/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/brokerage/delete �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String brokId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";

		try {
			Brokerage brokerage = brokerageDao.select(brokId);
			if (brokerage == null) {
				message = "���" + brokId + "�����ڣ����������룡";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/brokerage/query", isSuccess, message, null);
			} else {
				message = "��ѯ�ɹ�";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/brokerage/query", isSuccess, message, brokerage);
			}
		} catch (IOException e) {
			logger.error("URL��ec/brokerage/query �쳣˵����", e);
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
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<Brokerage> brokerageList = brokerageDao.selectList(map);
			int count = brokerageDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/brokerage/queryList", isSuccess, message, count, pageNo, pageSize, 0, 0,
					brokerageList);
		} catch (IOException e) {
			logger.error("URL:ec/brokerage/queryList �쳣˵����", e);
		}
		return resp;
	}

}
