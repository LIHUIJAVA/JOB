package com.px.mis.ec.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.service.EcExpressService;
import com.px.mis.ec.service.LogisticsTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value = "ec/ecExpress", method = RequestMethod.POST)
@Controller
public class EcExpressController {
	private Logger logger = LoggerFactory.getLogger(EcExpressController.class);
	@Autowired
	private EcExpressService ecExpressService;
	
	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/ecExpress/download");
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String platId=map.get("platId").toString();
			resp=ecExpressService.download(platId);
			logger.info("");
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/ecExpress/download �쳣˵����",e1);
			try {
				resp = BaseJson.returnRespObj("ec/ecExpress/download", false, "���س��ִ���,������", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/ecExpress/download ��װresp�쳣���쳣˵����",e);
			}
		}
		return resp;
	}

	@RequestMapping(value = "selectList", method = RequestMethod.POST)
	@ResponseBody
	private String selectList(@RequestBody String jsonBody) {
		logger.info("url:ec/ecExpress/selectList");
		logger.info("���������" + jsonBody);
		String resp = "";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int) map.get("pageNo");
			pageSize = (int) map.get("pageSize");
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/ec/ecExpress/selectList", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/ecExpress/selectList �쳣˵����",e1);
			try {
				resp = TransformJson.returnRespList("ec/ecExpress/selectList", false, "���������������:"+e1.getMessage()+";������������Ƿ���ȷ����ҳ��ѯʧ�ܡ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/ecExpress/selectList �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			//resp = logisticsTabService.selectList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/ecExpress/selectList �쳣˵����",e);
		}
		logger.info("���ز�����" + resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/ecExpress/queryList");
		logger.info("���������"+jsonBody);
		String resp="";
		resp = ecExpressService.selectList(jsonBody); 
		logger.info("���ز�����"+resp);
		return resp;
	}
}
