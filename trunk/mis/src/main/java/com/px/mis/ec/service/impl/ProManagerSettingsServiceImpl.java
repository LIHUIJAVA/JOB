package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ProManagerSettingsDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.ProManagerSettings;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.ProManagerSettingsService;
import com.px.mis.ec.service.StoreSettingsService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ProManagerSettingsServiceImpl implements ProManagerSettingsService {

	private Logger logger = LoggerFactory.getLogger(ProManagerSettingsServiceImpl.class);

	@Autowired
	private ProManagerSettingsDao proManagerSettingsDao;

	@Override
	public String edit(ProManagerSettings proManagerSettings) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			proManagerSettingsDao.update(proManagerSettings);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("ec/proManagerSettings/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/proManagerSettings/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String settingId) {
		String resp = "";
		try {
			ProManagerSettings proManagerSettings = proManagerSettingsDao.select(settingId);
			resp = BaseJson.returnRespObj("ec/proManagerSettings/queryList", true, "查询成功！", proManagerSettings);
		} catch (IOException e) {
			logger.error("URL：ec/proManagerSettings/queryList 异常说明：", e);
		}
		return resp;
	}

}
