package com.px.mis.ec.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ShareShipSettingsDao;
import com.px.mis.ec.entity.ShareShipSettings;
import com.px.mis.ec.service.ShareShipSettingsService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ShareShipSettingsServiceImpl implements ShareShipSettingsService {

	private Logger logger = LoggerFactory.getLogger(ProActivityServiceImpl.class);

	@Autowired
	private ShareShipSettingsDao shareShipSettingsDao;

	@Override
	public String edit(ShareShipSettings shareShipSettings) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			shareShipSettingsDao.update(shareShipSettings);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("ec/shareShipSettings/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/shareShipSettings/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String settingId) {
		String message = "查询成功！";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ShareShipSettings shareShipSettings = shareShipSettingsDao.select(settingId);
			resp = BaseJson.returnRespObj("ec/shareShipSettings/query", isSuccess, message, shareShipSettings);
		} catch (IOException e) {
			logger.error("URL：ec/shareShipSettings/query 异常说明：", e);
		}
		return resp;
	}

}
