package com.px.mis.account.controller;

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
import com.px.mis.account.entity.PursStlSnglMasTab;
import com.px.mis.account.service.PursStlSnglMasTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
//�ɹ�����ӿ�
@RequestMapping(value="account/PursStlSnglMasTab",method=RequestMethod.POST)
@Controller
public class PursStlSnglMasTabController {
	private Logger logger = LoggerFactory.getLogger(PursStlSnglMasTabController.class);
	@Autowired
	private PursStlSnglMasTabService pursStlSnglMasTabService;
	
	/*���*/
	@RequestMapping("addPursStlSnglMasTab")
	private @ResponseBody String addPursStlSnglMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/addPursStlSnglMasTab");
		logger.info("���������"+jsonData);
		String resp="";
		PursStlSnglMasTab pursStlSnglMasTab=null;
		ObjectNode aString;
		String userId="";
		String loginTime = "";
		try {
			aString = BaseJson.getReqHead(jsonData);
			userId=aString.get("accNum").asText();
			loginTime = aString.get("loginTime").asText();
			pursStlSnglMasTab = BaseJson.getPOJO(jsonData,PursStlSnglMasTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/addPursStlSnglMasTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/PursStlSnglMasTab/addPursStlSnglMasTab", false, "���������������������������Ƿ���д��ȷ��", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/addPursStlSnglMasTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.addPursStlSnglMasTab(userId, pursStlSnglMasTab,loginTime);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/insertPursStlSnglMasTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*ɾ��*/
	@RequestMapping("deletePursStlSnglMasTabList")
	public @ResponseBody String deletePursStlSnglMasTabList(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		String stlSnglId=null;
		try {
			stlSnglId = BaseJson.getReqBody(jsonData).get("stlSnglId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/PursStlSnglMasTab/deletePursStlSnglMasTabList", false, "���������������������������Ƿ���д��ȷ!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.deletePursStlSnglMasTabList(stlSnglId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/deletePursStlSnglMasTabList �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*����*/
	@RequestMapping("updatePursStlSnglMasTab")
	public @ResponseBody String updatePursStlSnglMasTab(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab");
		logger.info("���������"+jsonData);
		ObjectNode on=null;
		String resp="";
		PursStlSnglMasTab pursStlSnglMasTab=null;
		try {
			pursStlSnglMasTab = BaseJson.getPOJO(jsonData,PursStlSnglMasTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab �쳣˵����",e1);
			try {
				resp =BaseJson.returnRespObj("/account/PursStlSnglMasTab/updatePursStlSnglMasTab", false, "���������������������������Ƿ���д��ȷ!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.updatePursStlSnglMasTab(pursStlSnglMasTab);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/updatePursStlSnglMasTab �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*��ѯ����*/
	@RequestMapping(value="selectPursStlSnglMasTabList")
	public @ResponseBody String selectPursStlSnglMasTabList(@RequestBody String jsonBody) {
		logger.info("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList");
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
				resp=BaseJson.returnRespList("/account/PursStlSnglMasTab/selectPursStlSnglMasTabList", false, "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespList("/account/PursStlSnglMasTab/selectPursStlSnglMasTabList", false, "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList �쳣˵����",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=pursStlSnglMasTabService.selectPursStlSnglMasTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabList �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	/*��ѯ����*/
	@RequestMapping(value="selectPursStlSnglMasTabByStlSnglId")
	public @ResponseBody Object selectPursStlSnglMasTabByStlSnglId(@RequestBody String jsonData) {
		logger.info("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId");
		logger.info("���������"+jsonData);
		String resp=""; 
		String stlSnglId=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			stlSnglId = reqBody.get("stlSnglId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId �쳣˵����",e1);
			try {
				resp=BaseJson.returnRespObj("/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId",false,"���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp=pursStlSnglMasTabService.selectPursStlSnglMasTabByStlSnglId(stlSnglId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/PursStlSnglMasTab/selectPursStlSnglMasTabByStlSnglId �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
