package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ReturnStatusDao;
import com.px.mis.ec.entity.ReturnStatus;
import com.px.mis.ec.service.ReturnStatusService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ReturnStatusServiceImpl implements ReturnStatusService {

	private Logger logger = LoggerFactory.getLogger(ReturnStatusServiceImpl.class);

	@Autowired
	private ReturnStatusDao returnStatusDao;

	@Override
	public String add(ReturnStatus returnStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (returnStatusDao.select(returnStatus.getReturnStatusId()) != null) {
				message = "编号" + returnStatus.getReturnStatusId() + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				returnStatusDao.insert(returnStatus);
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/returnStatus/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/returnStatus/add 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String edit(ReturnStatus returnStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (returnStatusDao.select(returnStatus.getReturnStatusId()) == null) {
				message = "编号" + returnStatus.getReturnStatusId() + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				returnStatusDao.update(returnStatus);
				message = "修改成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/returnStatus/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/returnStatus/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String delete(String returnStatusId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (returnStatusDao.select(returnStatusId) == null) {
				message = "编号" + returnStatusId + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				returnStatusDao.delete(returnStatusId);
				message = "删除成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/returnStatus/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/returnStatus/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String returnStatusId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ReturnStatus returnStatus = returnStatusDao.select(returnStatusId);
			if (returnStatus == null) {
				message = "编号" + returnStatusId + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/returnStatus/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/returnStatus/query", isSuccess, message, returnStatus);
			}
		} catch (IOException e) {
			logger.error("URL：ec/returnStatus/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<ReturnStatus> mList = returnStatusDao.selectList(map);
			int count = returnStatusDao.selectCount(map);
			message = "查询成功";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/returnStatus/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, mList);
		} catch (IOException e) {
			logger.error("URL：ec/returnStatus/queryList 异常说明：", e);
		}
		return resp;
	}

}
