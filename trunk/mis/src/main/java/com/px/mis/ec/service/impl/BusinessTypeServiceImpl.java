package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.BusinessTypeDao;
import com.px.mis.ec.entity.BusinessType;
import com.px.mis.ec.service.BusinessTypeService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

	private Logger logger = LoggerFactory.getLogger(BusinessTypeServiceImpl.class);
	
	@Autowired
	private BusinessTypeDao businessTypeDao;

	@Override
	public String add(BusinessType businessType) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (businessTypeDao.select(businessType.getBusTypeId()) != null) {
			message = "编号" + businessType.getBusTypeId() + "已存在，请重新输入！";
			isSuccess = false;
		} else {
			businessTypeDao.insert(businessType);
			message = "新增成功！";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/businessType/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:ec/businessType/add 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String edit(BusinessType businessType) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (businessTypeDao.select(businessType.getBusTypeId()) == null) {
			message = "编号" + businessType.getBusTypeId() + "不存在，请重新输入！";
			isSuccess = false;
		} else {
			businessTypeDao.update(businessType);
			message = "修改成功！";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/businessType/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:ec/businessType/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String delete(String brokId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if (businessTypeDao.select(brokId) == null) {
			message = "编号" + brokId + "不存在，请重新输入！";
			isSuccess = false;
		} else {
			businessTypeDao.delete(brokId);
			message = "删除成功！";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/brokerage/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL:ec/businessType/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String brokId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		BusinessType businessType = businessTypeDao.select(brokId);

		try {
			if (businessType == null) {
				message = "编号" + brokId + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/brokerage/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/brokerage/query", isSuccess, message, businessType);
			}
		} catch (IOException e) {
			logger.error("URL:ec/businessType/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<BusinessType> businessList = businessTypeDao.selectList(map);
		int count = businessTypeDao.selectCount(map);
		message = "查询成功";
		isSuccess = true;
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = businessList.size();
		int pages = count / pageSize + 1;
		try {
			resp = BaseJson.returnRespList("ec/brokerage/queryList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, businessList);
		} catch (IOException e) {
			logger.error("URL:ec/businessType/queryList 异常说明：", e);
		}
		return resp;
	}

}
