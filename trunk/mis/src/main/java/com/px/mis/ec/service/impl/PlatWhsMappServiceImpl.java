package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.PlatWhsMappDao;
import com.px.mis.ec.entity.PlatWhsMapp;
import com.px.mis.ec.service.PlatWhsMappService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@Transactional
@Service
public class PlatWhsMappServiceImpl implements PlatWhsMappService {

	private Logger logger = LoggerFactory.getLogger(PlatWhsMappServiceImpl.class);

	@Autowired
	private PlatWhsMappDao platWhsMappDao;

	@Override
	public String select(Map map) {
		String offline = "";
		try {
			offline = platWhsMappDao.select(map);
		} catch (Exception e) {
			logger.error("查询异常", e);
		}
		return offline;
	}

	@Override
	public String insert(String jsonbody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonbody).toString());
			/*String online = BaseJson.getReqBody(jsonbody).get("online").asText();
			String onlineName = BaseJson.getReqBody(jsonbody).get("onlineName").asText();
			String offline = BaseJson.getReqHead(jsonbody).get("offline").asText();
			String type =  BaseJson.getReqHead(jsonbody).get("type").asText();*/
			if(platWhsMappDao.select(map)==null) {
				PlatWhsMapp platWhsMapp = new PlatWhsMapp();
				platWhsMapp.setOnline(map.get("online").toString());
				platWhsMapp.setOffline(map.get("offline").toString());
				platWhsMapp.setOnlineName(map.get("onlineName").toString());
				platWhsMapp.setType(map.get("type").toString());
				platWhsMappDao.insert(platWhsMapp);
				message="新增成功";
			}else {
				isSuccess=false;
				message="要添加的平台仓已创建对应关系";
			}
			resp = BaseJson.returnRespObj("ec/platWhsMapp/insert", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String update(String jsonbody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			//Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonbody).toString());
			PlatWhsMapp platWhsMapp = BaseJson.getPOJO(jsonbody, PlatWhsMapp.class);
			platWhsMappDao.update(platWhsMapp);
			message="修改成功";
			resp = BaseJson.returnRespObj("ec/platWhsMapp/update", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String delete(String jsonbody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			//Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonbody).toString());
			PlatWhsMapp platWhsMapp = BaseJson.getPOJO(jsonbody, PlatWhsMapp.class);
			platWhsMappDao.delete(platWhsMapp);
			message="删除成功";
			resp = BaseJson.returnRespObj("ec/platWhsMapp/delete", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectList(String jsonbody) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonbody).toString());
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<PlatWhsMapp> platWhsMapps = platWhsMappDao.selectList(map);
			int count = platWhsMappDao.selectCount(map);
			message="查询成功";
			resp = BaseJson.returnRespList("ec/platWhsMapp/selectList", isSuccess, message, count, pageNo, pageSize, 0,
					0, platWhsMapps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

}
