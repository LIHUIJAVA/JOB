package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.MemberRecordDao;
import com.px.mis.ec.entity.MemberRecord;
import com.px.mis.ec.service.MemberRecordService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class MemberRecordServiceImpl implements MemberRecordService {

	private Logger logger = LoggerFactory.getLogger(MemberRecordServiceImpl.class);

	@Autowired
	private MemberRecordDao memberRecordDao;

	@Override
	public String add(MemberRecord memberRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (memberRecordDao.select(memberRecord.getMemId()) != null) {
				message = "编号" + memberRecord.getMemId() + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				memberRecordDao.insert(memberRecord);
				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/memberRecord/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/memberRecord/add 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String edit(MemberRecord memberRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (memberRecordDao.select(memberRecord.getMemId()) == null) {
				message = "编号" + memberRecord.getMemId() + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				memberRecordDao.update(memberRecord);
				message = "修改成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/memberRecord/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/memberRecord/edit 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String delete(String goodId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (memberRecordDao.select(goodId) == null) {
				message = "编号" + goodId + "不存在，请重新输入！";
				isSuccess = false;
			} else {
				memberRecordDao.delete(goodId);
				message = "删除成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/memberRecord/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/memberRecord/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String memId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		MemberRecord memberRecord = memberRecordDao.select(memId);

		try {
			if (memberRecord == null) {
				message = "编号" + memId + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/memberRecord/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/memberRecord/query", isSuccess, message, memberRecord);
			}
		} catch (IOException e) {
			logger.error("URL：ec/memberRecord/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<MemberRecord> mList = memberRecordDao.selectList(map);
		int count = memberRecordDao.selectCount(map);
		message = "查询成功";
		isSuccess = true;
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = mList.size();
		int pages = count / pageSize + 1;
		try {
			resp = BaseJson.returnRespList("ec/memberRecord/queryList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, mList);
		} catch (IOException e) {
			logger.error("URL：ec/memberRecord/queryList 异常说明：", e);
		}
		return resp;
	}

}
