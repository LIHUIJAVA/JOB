package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.DownloadSettingsDao;
import com.px.mis.ec.entity.DownloadSettings;
import com.px.mis.ec.service.DownloadSettingsService;
import com.px.mis.ec.util.QuartzManager;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class DownloadSettingsServiceImpl implements DownloadSettingsService {

	private Logger logger = LoggerFactory.getLogger(DownloadSettingsServiceImpl.class);

	@Autowired
	private DownloadSettingsDao downloadSettingsDao;

	@Override
	public String add(DownloadSettings downloadSettings) {
		String message = "";
		Boolean isSuccess = false;
		String resp = "";
		try {
			downloadSettingsDao.insert(downloadSettings);
			message = "��ӳɹ���";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/downloadSet/add", isSuccess, message, null);
		} catch (Exception e) {
			logger.error("URL��ec/downloadSet/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String edit(DownloadSettings downloadSettings) {
		String message = "";
		Boolean isSuccess = false;
		String resp = "";
		try {
			downloadSettingsDao.update(downloadSettings);
			message = "�޸ĳɹ���";
			isSuccess = true;
		/*	//���ʱ��
			int intervalTime = Integer.parseInt(downloadSettings.getIntervalTime());
			//������ʱ����
			QuartzManager.modifyJobTime(String.valueOf(downloadSettings.getId()), intervalTime);
			long currentTime = System.currentTimeMillis();
			currentTime += intervalTime * 60 * 60 * 1000;
			Date date = new Date(currentTime);
			downloadSettings.setNextTime(date);*/
			//�޸��´�ִ��ʱ��
			//downloadSettingsDao.update(downloadSettings);
			resp = BaseJson.returnRespObj("ec/downloadSet/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/downloadSet/edit �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String selectList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			List<DownloadSettings> eList = downloadSettingsDao.selectList(map);
			int count = downloadSettingsDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/downloadSet/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, eList);
		} catch (IOException e) {
			logger.error("URL:ec/downloadSet/queryList �쳣˵����", e);
		}
		return resp;
	}

}
