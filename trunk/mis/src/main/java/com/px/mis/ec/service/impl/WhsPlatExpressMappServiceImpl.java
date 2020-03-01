package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.WhsPlatExpressMappDao;
import com.px.mis.ec.entity.WhsPlatExpressMapp;
import com.px.mis.ec.service.WhsPlatExpressMappService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Transactional
@Service
public class WhsPlatExpressMappServiceImpl implements WhsPlatExpressMappService {

	private Logger logger = LoggerFactory.getLogger(WhsPlatExpressMappServiceImpl.class);
	
	@Autowired
	private WhsPlatExpressMappDao whsPlatExpressMappDao;

	@Override
	public String insert(String jsonBody) {
		// TODO Auto-generated method stub
		
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			WhsPlatExpressMapp whsPlatExpressMapp = BaseJson.getPOJO(jsonBody, WhsPlatExpressMapp.class);
			if(whsPlatExpressMappDao.checkExsist(whsPlatExpressMapp)>0) {
				isSuccess=false;
				message="����ʧ�ܣ��ֿ⣺"+whsPlatExpressMapp.getWhsId()+"ƽ̨��"+whsPlatExpressMapp.getPlatId()+"�����ö�Ӧ��ݹ�˾";
			}else {
				whsPlatExpressMappDao.insert(whsPlatExpressMapp);
				message="�����ɹ�";
			}
			resp = BaseJson.returnRespObj("ec/whsPlatExpressMapp/insert", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String update(String jsonBody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			WhsPlatExpressMapp whsPlatExpressMapp = BaseJson.getPOJO(jsonBody, WhsPlatExpressMapp.class);
			if(whsPlatExpressMappDao.checkExsist(whsPlatExpressMapp)>0) {
				isSuccess=false;
				message="�޸�ʧ�ܣ��ֿ⣺"+whsPlatExpressMapp.getWhsName()+"ƽ̨��"+whsPlatExpressMapp.getPlatName()+"�����ö�Ӧ��ݹ�˾";
			}else {
				whsPlatExpressMappDao.update(whsPlatExpressMapp);
				message="�޸ĳɹ�";
			}
			resp = BaseJson.returnRespObj("ec/whsPlatExpressMapp/update", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/whsPlatExpressMapp/update", false, "ɾ��ʧ�ܣ���ѡ����", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	@Override
	public String delete(String jsonBody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			WhsPlatExpressMapp whsPlatExpressMapp = BaseJson.getPOJO(jsonBody, WhsPlatExpressMapp.class);
			whsPlatExpressMappDao.delete(whsPlatExpressMapp);
			resp = BaseJson.returnRespObj("ec/whsPlatExpressMapp/delete", isSuccess, "ɾ���ɹ�", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/whsPlatExpressMapp/delete", false, "ɾ��ʧ�ܣ���ѡ����", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	@Override
	public String selectList(String jsonBody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<WhsPlatExpressMapp> WhsPlatExpressMapps = whsPlatExpressMappDao.selectList(map);
			int count = whsPlatExpressMappDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/whsPlatExpressMapp/selectList", isSuccess, message, count, pageNo, pageSize, 0,
					0, WhsPlatExpressMapps);
		} catch (Exception e) {
			logger.error("URL��ec/whsPlatExpressMapp/selectList �쳣˵����", e);
		}
		return resp;
	}
	
	
	
	
}
