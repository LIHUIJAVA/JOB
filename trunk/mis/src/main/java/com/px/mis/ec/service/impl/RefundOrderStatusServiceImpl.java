package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.RefundOrderStatusDao;
import com.px.mis.ec.entity.RefundOrderStatus;
import com.px.mis.ec.service.RefundOrderStatusService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class RefundOrderStatusServiceImpl implements RefundOrderStatusService {

	private Logger logger = LoggerFactory.getLogger(RefundOrderStatusServiceImpl.class);

	@Autowired
	private RefundOrderStatusDao refundOrderStatusDao;

	@Override
	public String add(RefundOrderStatus refundOrderStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		String refStatusId = refundOrderStatus.getRefStatusId();
		try {
			if (refStatusId == null || "".equals(refStatusId)) {
				message = "编号不能为空，请重新输入！";
				isSuccess = false;
			} else {
				if (refundOrderStatusDao.select(refStatusId) != null) {
					message = "编号" + refStatusId + "已存在，请重新输入！";
					isSuccess = false;
				} else {
					refundOrderStatusDao.insert(refundOrderStatus);
					message = "新增成功！";
					isSuccess = true;
				}
			}
			resp = BaseJson.returnRespObj("ec/refundOrderStatus/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/refundOrderStatus/add 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String edit(RefundOrderStatus refundOrderStatus) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (refundOrderStatusDao.select(refundOrderStatus.getRefStatusId()) == null) {
				message = "编号" + refundOrderStatus.getRefStatusId() + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				refundOrderStatusDao.update(refundOrderStatus);
				message = "修改成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/refundOrderStatus/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/refundOrderStatus/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String delete(String refStatusId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (refundOrderStatusDao.select(refStatusId) == null) {
				message = "编号" + refStatusId + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				refundOrderStatusDao.delete(refStatusId);
				message = "删除成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/refundOrderStatus/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/refundOrderStatus/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String refStatusId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			RefundOrderStatus refundOrderStatus = refundOrderStatusDao.select(refStatusId);
			if (refundOrderStatus == null) {
				message = "编号" + refStatusId + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/refundOrderStatus/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/refundOrderStatus/query", isSuccess, message, refundOrderStatus);
			}
		} catch (IOException e) {
			logger.error("URL：ec/refundOrderStatus/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<RefundOrderStatus> mList = refundOrderStatusDao.selectList(map);
			int count = refundOrderStatusDao.selectCount(map);
			message = "查询成功";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/refundOrderStatus/queryList", isSuccess, message, count, pageNo,
					pageSize, 0, 0, mList);
		} catch (IOException e) {
			logger.error("URL：ec/refundOrderStatus/queryList 异常说明：", e);
		}
		return resp;
	}

}
