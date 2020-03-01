package com.px.mis.ec.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.service.RefundOrderAuditService;
import com.px.mis.util.BaseJson;

//�˿����˺�����
@Controller
@RequestMapping(value="ec/refundOrderAudit",method = RequestMethod.POST)
public class RefundOrderAuditController{
	
	private Logger logger = LoggerFactory.getLogger(OrderAdjustController.class);
	
	@Autowired
	private RefundOrderAuditService refundOrderAuditService;
	//�˿�����
	@RequestMapping(value="refundOrderAudit",method = RequestMethod.POST)
	@ResponseBody
	private String refundOrderAudit(@RequestBody String jsonData) {
		logger.info("url:ec/refundOrderAudit/refundOrderAudit");
		logger.info("���������"+jsonData);
		String resp="";
		ObjectNode reqBody=null;
		String refId=null;
		String userId=null;
		String loginDate=null;
		try {
			reqBody = BaseJson.getReqBody(jsonData);
			refId= reqBody.get("refId").asText();
			userId= reqBody.get("userId").asText();
			loginDate= reqBody.get("loginDate").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/refundOrderAudit/refundOrderAudit �쳣˵����",e1);
			try {
				resp = BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit", false,"���������������������������Ƿ���ȷ������ʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/refundOrderAudit/refundOrderAudit �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp = refundOrderAuditService.refundOrderAudit(refId,userId,loginDate);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/refundOrderAudit/refundOrderAudit �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�˿����
	@RequestMapping(value="refundOrderAbandonAudit",method = RequestMethod.POST)
	@ResponseBody
	private String refundOrderAbandonAudit(@RequestBody String jsonData) {
		logger.info("url:ec/refundOrderAudit/refundOrderAbandonAudit");
		logger.info("���������"+jsonData);
		String resp="";
		ObjectNode reqBody=null;
		String refId=null;
		String userId=null;
		try {
			reqBody = BaseJson.getReqBody(jsonData);
			refId= reqBody.get("refId").asText();
			userId= reqBody.get("userId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/refundOrderAudit/refundOrderAbandonAudit �쳣˵����",e1);
			try {
				resp = BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAbandonAudit", false,"���������������������������Ƿ���ȷ������ʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/refundOrderAudit/refundOrderAbandonAudit �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp = refundOrderAuditService.refundOrderAbandonAudit(refId,userId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/refundOrderAudit/refundOrderAbandonAudit �쳣˵����",e);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
}
