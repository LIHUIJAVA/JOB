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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.PresentMode;
import com.px.mis.ec.service.PresentModeService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//��Ʒģʽ�ӿ�
@Controller
@RequestMapping(value="ec/presentMode",method=RequestMethod.POST)
public class PresentModeController {
	private Logger logger = LoggerFactory.getLogger(PresentModeController.class);
	@Autowired
	private PresentModeService presentModeService;
	/*���*/
	@RequestMapping("insertPresentMode")
	public @ResponseBody Object insertPresentMode(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/insertPresentMode");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		PresentMode presentMode=null;
		try {
			presentMode = BaseJson.getPOJO(jsonData,PresentMode.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/insertPresentMode �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/presentMode/insertPresentMode", false, "���������������������������Ƿ���д��ȷ������쳣��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/insertPresentMode �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = presentModeService.insertPresentMode(presentMode);
			if(on==null) {
				resp =BaseJson.returnRespObj("/ec/presentMode/insertPresentMode", false, "����쳣��", null);
			}else {
				resp =BaseJson.returnRespObj("/ec/presentMode/insertPresentMode", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/insertPresentMode �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deletePresentMode")
	public @ResponseBody Object deletePresentMode(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/deletePresentMode");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/deletePresentMode �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/presentMode/deletePresentMode", false, "���������������������������Ƿ���д��ȷ��delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/deletePresentMode �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = presentModeService.deletePresentModeById(no);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/presentMode/deletePresentMode", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/presentMode/deletePresentMode", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/deletePresentMode �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updatePresentMode")
	public @ResponseBody Object updatePresentMode(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/updatePresentMode");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		PresentMode presentMode=null;
		try {
			presentMode = BaseJson.getPOJO(jsonData,PresentMode.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/updatePresentMode �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/presentMode/updatePresentMode", false, "���������������������������Ƿ���д��ȷ��update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/updatePresentMode �쳣˵����",e);
			}
			return resp;
		}
		try {
			on = presentModeService.updatePresentModeById(presentMode);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/presentMode/updatePresentMode", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/presentMode/updatePresentMode", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/updatePresentMode �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectPresentMode")
	public @ResponseBody Object selectPresentMode(@RequestBody String jsonBody) {
		logger.info("url:/ec/presentMode/selectPresentMode");
		logger.info("���������"+jsonBody);
		String resp="";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int)map.get("pageNo");
			pageSize = (int)map.get("pageSize");
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/ec/presentMode/selectPresentMode", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentMode �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespList("/ec/presentMode/selectPresentMode", false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/selectPresentMode �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=presentModeService.selectPresentModeList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentMode �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectPresentModeById")
	public @ResponseBody Object selectPresentModeById(@RequestBody String jsonData) {
		logger.info("url:/ec/presentMode/selectPresentModeById");
		logger.info("���������"+jsonData);
		String resp=""; 
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentModeById �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/ec/presentMode/selectPresentModeById",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/presentMode/selectPresentModeById �쳣˵����",e);
			}
			return resp;
		}
		try {
			PresentMode presentMode = presentModeService.selectPresentModeById(no);
			if(presentMode!=null) {
				 resp=BaseJson.returnRespObj("/ec/presentMode/selectPresentModeById",true,"����ɹ���",presentMode);
			}else {
				 resp=BaseJson.returnRespObj("/ec/presentMode/selectPresentModeById",false,"û�в鵽���ݣ�",presentMode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/presentMode/selectPresentModeById �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
