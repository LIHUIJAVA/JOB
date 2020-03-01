package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.DownloadSettingsDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.DownloadSettings;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.StoreRecordService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@Transactional
@Service
public class StoreRecordServiceImpl implements StoreRecordService {

	private Logger logger = LoggerFactory.getLogger(StoreRecordServiceImpl.class);

	@Autowired
	private StoreRecordDao storeRecordDao;

	@Autowired
	private StoreSettingsDao storeSettingsDao;

	@Autowired
	private DownloadSettingsDao downloadSettingsDao;
	@Override
	public String add(StoreRecord storeRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (storeRecordDao.select(storeRecord.getStoreId()) != null) {
				message = "编号" + storeRecord.getStoreId() + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				storeRecordDao.insert(storeRecord);
				
				StoreSettings storeSettings = new StoreSettings();
				storeSettings.setStoreId(storeRecord.getStoreId());
				storeSettings.setStoreName(storeRecord.getStoreName());
				storeSettingsDao.insert(storeSettings);
				
				DownloadSettings downloadSettings = new DownloadSettings();
				downloadSettings.setShopId(storeRecord.getStoreId());
				downloadSettings.setShopName(storeRecord.getStoreName());
				downloadSettings.setRecentHours("12");//设置默认12小时
				downloadSettingsDao.insert(downloadSettings);
				
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/storeRecord/add", isSuccess, message, storeRecord);
		} catch (IOException e) {
			logger.error("URL：ec/storeRecord/add 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String edit(StoreRecord storeRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (storeRecordDao.select(storeRecord.getStoreId()) == null) {
				message = "店铺编号" + storeRecord.getStoreId() + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				storeRecordDao.update(storeRecord);
				StoreSettings storeSetting = storeSettingsDao.select(storeRecord.getStoreId());
				storeSetting.setStoreName(storeRecord.getStoreName());
				storeSettingsDao.update(storeSetting);
				message = "修改成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/storeRecord/edit", isSuccess, message, storeRecord);
		} catch (IOException e) {
			logger.error("URL：ec/storeRecord/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String delete(String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (storeRecordDao.select(storeId) == null) {
				message = "编号" + storeId + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				storeRecordDao.delete(storeId);
				storeSettingsDao.delete(storeId);//店铺删除的同时删除店铺对应设置
				downloadSettingsDao.delete(storeId);
				message = "删除成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/storeRecord/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/storeRecord/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		StoreRecord storeRecord = storeRecordDao.select(storeId);

		try {
			if (storeRecord == null) {
				message = "编号" + storeId + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/storeRecord/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/storeRecord/query", isSuccess, message, storeRecord);
			}
		} catch (IOException e) {
			logger.error("URL：ec/storeRecord/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<StoreRecord> mList = storeRecordDao.selectList(map);
			int count = storeRecordDao.selectCount(map);
			message = "查询成功";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/storeRecord/queryList", isSuccess, message, count, pageNo, pageSize,
					0, 0, mList);
		} catch (IOException e) {
			logger.error("URL：ec/storeRecord/queryList 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryAll(String jsonBody) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<StoreRecord> storeRecordList = storeRecordDao.selectAll();
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/storeRecord/queryAll", isSuccess, message, storeRecordList);
		} catch (IOException e) {
			logger.error("URL：ec/storeRecord/queryAll 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String download(ObjectNode jsonBody) {
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<StoreRecord> storeRecordList = storeRecordDao.selectAll();
			for (StoreRecord storeRecord : storeRecordList) {
				StoreSettings storeSettings = storeSettingsDao.select(storeRecord.getStoreId());
				if (storeRecord.getEcId().equals("JD")) {
					jdDownload(storeRecord, storeSettings);
				} else if (storeRecord.getEcId().equals("TM")) {

				}
			}
			resp = BaseJson.returnRespObj("ec/storeRecord/download", isSuccess, "下载成功！", null);
		} catch (NoSuchAlgorithmException | IOException e) {
			logger.error("URL：ec/storeRecord/download 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}

	private void jdDownload(StoreRecord storeRecord, StoreSettings storeSettings)
			throws NoSuchAlgorithmException, IOException {
		String jdRespStr = ECHelper.getJD("jingdong.vender.shop.query", storeSettings, "{}");
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		if (jdRespJson.get("jingdong_vender_shop_query_responce").get("code").asText().equals("0")) {
			JsonNode shopJson = jdRespJson.get("jingdong_vender_shop_query_responce").get("shop_jos_result");
			storeRecord.setEcStoreId(shopJson.get("shop_id").asText());
			storeRecord.setStoreName(shopJson.get("shop_name").asText());
			storeRecordDao.update(storeRecord);
		}
	}

	@Override
	public String exportList(Map map) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public String importStoreRecord(MultipartFile file, String userId) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
