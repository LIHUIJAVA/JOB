package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.StoreSettingsService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class StoreSettingsServiceImpl implements StoreSettingsService {

	private Logger logger = LoggerFactory.getLogger(StoreSettingsServiceImpl.class);

	@Autowired
	private StoreSettingsDao storeSettingDao;

	@Override
	public String edit(StoreSettings storeSetting) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if(storeSetting.getTokenDate().length()<10) {
				storeSetting.setTokenDate(null);
			}else if (storeSetting.getTokenDate().length()==10) {
				storeSetting.setTokenDate(storeSetting.getTokenDate() + " 00:00:00");
			}
			storeSettingDao.update(storeSetting);
			message = "���³ɹ���";
			resp = BaseJson.returnRespObj("ec/storeSetting/edit", isSuccess, message, null);
		} catch (Exception e) {
			message = "����ʧ�ܣ������������ݸ�ʽ";
			try {
				resp = BaseJson.returnRespObj("ec/storeSetting/edit", false, message, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.error("URL��ec/storeSetting/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String resp = "";
		try {
			List<StoreSettings> ssList = storeSettingDao.selectList(map);
			int count = storeSettingDao.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/storeSetting/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					ssList);
		} catch (IOException e) {
			logger.error("URL��ec/storeSetting/queryList �쳣˵����", e);
		}
		return resp;
	}

}
