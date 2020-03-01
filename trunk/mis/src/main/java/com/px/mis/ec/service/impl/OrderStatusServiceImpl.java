package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.OrderStatusDao;
import com.px.mis.ec.entity.OrderStatus;
import com.px.mis.ec.service.OrderStatusService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class OrderStatusServiceImpl implements OrderStatusService {

	private Logger logger = LoggerFactory.getLogger(OrderStatusServiceImpl.class);

	@Autowired
	private OrderStatusDao orderStatusDao;

	@Override
	public String add(OrderStatus orderStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (orderStatusDao.select(orderStatus.getOrderStatusId()) != null) {
				message = "���" + orderStatus.getOrderStatusId() + "�Ѵ��ڣ����������룡";
				isSuccess = false;
			} else {
				orderStatusDao.insert(orderStatus);
				message = "�����ɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/orderStatus/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/orderStatus/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String edit(OrderStatus orderStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (orderStatusDao.select(orderStatus.getOrderStatusId()) == null) {
				message = "���" + orderStatus.getOrderStatusId() + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				orderStatusDao.update(orderStatus);
				message = "�޸ĳɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/orderStatus/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/orderStatus/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String delete(String goodId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (orderStatusDao.select(goodId) == null) {
				message = "���" + goodId + "�����ڣ����������룡";
				isSuccess = false;
			} else {
				orderStatusDao.delete(goodId);
				message = "ɾ���ɹ���";
				isSuccess = true;
			}

			resp = BaseJson.returnRespObj("ec/orderStatus/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/orderStatus/delete �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String query(String memId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		OrderStatus orderStatus = orderStatusDao.select(memId);

		try {
			if (orderStatus == null) {
				message = "���" + memId + "�����ڣ����������룡";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/orderStatus/query", isSuccess, message, null);
			} else {
				message = "��ѯ�ɹ�";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/orderStatus/query", isSuccess, message, orderStatus);
			}
		} catch (IOException e) {
			logger.error("URL��ec/orderStatus/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<OrderStatus> oList = orderStatusDao.selectList(map);
			int count = orderStatusDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/orderStatus/queryList", isSuccess, message, count, pageNo, pageSize,
					0, 0, oList);
		} catch (IOException e) {
			logger.error("URL��ec/orderStatus/queryList �쳣˵����", e);
		}
		return resp;
	}

}
