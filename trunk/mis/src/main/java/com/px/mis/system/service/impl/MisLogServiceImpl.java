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
				resp = BaseJson.returnRespObj("system/misLog/delete", isSuccess, "ɾ��ʧ��", null);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/delete", isSuccess, "ɾ���ɹ�", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL��system/misLog/delete �쳣˵����", e);
			throw new RuntimeException(e);
		}
		return resp;
	}

	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
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
				resp = BaseJson.returnRespList("system/misLog/queryList", true, "��ѯ�ɹ�������", count, pageNo, pageSize,
						listNum, pages, logList);
			} else {
				resp = BaseJson.returnRespList("system/misLog/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
						listNum, pages, logList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL��system/misLog/queryList �쳣˵����", e);
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
				resp = BaseJson.returnRespObjListAnno("system/misLog/queryPrint", true, "��ѯ�ɹ�������",null, logList);
			} else {
				resp = BaseJson.returnRespObjListAnno("system/misLog/queryPrint", true, "��ѯ�ɹ���",null, logList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL��system/misLog/queryPrint �쳣˵����", e);
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
				resp = BaseJson.returnRespObj("system/misLog/add", isSuccess, "����ʧ��", record);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/add", isSuccess, "�����ɹ�", record);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL��system/misLog/add �쳣˵����", e);
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
				resp = BaseJson.returnRespObj("system/misLog/edit", isSuccess, "�޸�ʧ��", null);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/edit", isSuccess, "�޸ĳɹ�", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL��system/misLog/edit �쳣˵����", e);
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
				resp = BaseJson.returnRespObj("system/misLog/editList", isSuccess, "�޸�ʧ��", null);

			} else {
				resp = BaseJson.returnRespObj("system/misLog/editList", isSuccess, "�޸ĳɹ�", null);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL��system/misLog/editList �쳣˵����", e);
			throw new RuntimeException(e);

		}
		return resp;
	}

}
