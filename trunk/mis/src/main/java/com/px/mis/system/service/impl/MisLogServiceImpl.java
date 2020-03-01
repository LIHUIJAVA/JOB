package com.px.mis.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisLogService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class MisLogServiceImpl implements MisLogService {

	private Logger logger = LoggerFactory.getLogger(MisLogServiceImpl.class);

	@Autowired
	private MisLogDAO misLogDAO;

	@Override
	public String delete(String id) {
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> list = getList(id);
			int i = misLogDAO.deleteList(list);
			if (i == 0) {
				resp = BaseJson.returnRespObj("system/misLog/delete", isSuccess, "删除失败", null);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/delete", isSuccess, "删除成功", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL：system/misLog/delete 异常说明：", e);
			throw new RuntimeException(e);
		}
		return resp;
	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			list.add(str[i]);
		}
		return list;
	}

	@Override
	public String queryList(Map<?, ?> map) {
		String resp = "";
		try {
			List<MisLog> logList = misLogDAO.selectList(map);
			int i = logList.size();
			int count = misLogDAO.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = logList.size();
			int pages = count / pageSize;
			if (count % pageSize > 0) {
				pages += 1;
			}
			if (i == 0) {
				resp = BaseJson.returnRespList("system/misLog/queryList", true, "查询成功无数据", count, pageNo, pageSize,
						listNum, pages, logList);
			} else {
				resp = BaseJson.returnRespList("system/misLog/queryList", true, "查询成功！", count, pageNo, pageSize,
						listNum, pages, logList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL：system/misLog/queryList 异常说明：", e);
			throw new RuntimeException(e);

		}
		return resp;
	}

	@Override
	public String queryPrint(Map<?, ?> map) {
		String resp = "";
		try {
			List<MisLog> logList = misLogDAO.selectList(map);
			int i = logList.size();
			if (i == 0) {
				resp = BaseJson.returnRespObjListAnno("system/misLog/queryPrint", true, "查询成功无数据",null, logList);
			} else {
				resp = BaseJson.returnRespObjListAnno("system/misLog/queryPrint", true, "查询成功！",null, logList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL：system/misLog/queryPrint 异常说明：", e);
			throw new RuntimeException(e);

		}
		return resp;
	}

	@Override
	public String add(MisLog record) {
		Boolean isSuccess = true;
		String resp = "";
		try {
			int i = misLogDAO.insertSelective(record);
			if (i == 0) {
				resp = BaseJson.returnRespObj("system/misLog/add", isSuccess, "新增失败", record);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/add", isSuccess, "新增成功", record);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL：system/misLog/add 异常说明：", e);
			throw new RuntimeException(e);

		}
		return resp;

	}

	@Override
	public String edit(MisLog record) {
		Boolean isSuccess = true;
		String resp = "";
		try {
			int i = misLogDAO.updateByPrimaryKeySelective(record);
			if (i == 0) {
				resp = BaseJson.returnRespObj("system/misLog/edit", isSuccess, "修改失败", null);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/edit", isSuccess, "修改成功", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL：system/misLog/edit 异常说明：", e);
			throw new RuntimeException(e);

		}
		return resp;
	}

	@Override
	public String editList(List<MisLog> record) {
		Boolean isSuccess = true;
		String resp = "";
		try {
			int i = misLogDAO.updateByList(record);
			if (i == 0) {
				resp = BaseJson.returnRespObj("system/misLog/editList", isSuccess, "修改失败", null);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/editList", isSuccess, "修改成功", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL：system/misLog/editList 异常说明：", e);
			throw new RuntimeException(e);

		}
		return resp;
	}

}
