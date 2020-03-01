package com.px.mis.ec.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.SpecialSettingsDao;
import com.px.mis.ec.entity.SpecialSettings;
import com.px.mis.ec.service.SpecialSettingsService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class SpecialSettingsServiceImpl implements SpecialSettingsService {

	private Logger logger = LoggerFactory.getLogger(SpecialSettingsServiceImpl.class);

	@Autowired
	private SpecialSettingsDao specialSettingsDao;

	@Override
	public String edit(SpecialSettings specialSettings) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			specialSettingsDao.update(specialSettings);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("ec/specialSetting/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/specialSetting/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String settingId) {
		String message = "查询成功！";
		Boolean isSuccess = true;
		String resp = "";
		try {
			SpecialSettings specialSettings = specialSettingsDao.select(settingId);
			resp = BaseJson.returnRespObj("ec/specialSetting/query", isSuccess, message, specialSettings);
		} catch (IOException e) {
			logger.error("URL：ec/specialSetting/query 异常说明：", e);
		}
		return resp;
	}

}
