package com.px.mis.ec.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrderThird;
import com.px.mis.ec.service.PlatOrderThirdService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//΢�̳Ƕ���
@RequestMapping(value = "ec/platOrder/other", method = RequestMethod.POST)
@Controller
public class PlatOrderThirdController {
	
	private Logger logger = LoggerFactory.getLogger(PlatOrderController.class);
	
	@Autowired
	private PlatOrderThirdService platOrderOtherService;
	
	/*
	 * ��������������
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	@ResponseBody
	private String export(@RequestBody String jsonBody) {
		String url = "ec/platOrder/other/export";
		logger.info("url:"+url);
		logger.info("���������" + jsonBody);
		String resp = "";
		try {
			//��ȡ�ӿ�ǩ��
			String signKey = BaseJson.getReqHead(jsonBody).get("sign").asText(); //ǩ��
			String platId = BaseJson.getReqHead(jsonBody).get("platCode").asText(); //ƽ̨����
			String storeId = BaseJson.getReqHead(jsonBody).get("storeCode").asText();//���̱���
			
			//ǩ����֤
			if(signAuth(signKey,storeId)) {
				String userId = "";		
				userId = platId+storeId;
				Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
				ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
				ArrayNode platOrdersArray = (ArrayNode) reqBody.get("orderInfoList");//���붩�����ݼ���
				List<PlatOrderThird> paltOrderThirdList = new ArrayList<>();
				for(Iterator<JsonNode> platOrdersArrayIterator = platOrdersArray.iterator(); platOrdersArrayIterator.hasNext();) {
					JsonNode platOrdersNode = platOrdersArrayIterator.next();
					PlatOrderThird platOrderThird = JacksonUtil.getPOJO((ObjectNode)platOrdersNode, PlatOrderThird.class);
					paltOrderThirdList.add(platOrderThird);
				}

				if(paltOrderThirdList.size() == 0) {
					resp = BaseJson.returnRespObj("mis/ec/platOrder/other/export", false, "�������ݲ���Ϊ��", null);
					
				} else {
					resp = platOrderOtherService.exportPlatOrderThird(userId,platId,storeId,paltOrderThirdList);
					
					logger.info("���ز�����" + resp);
				}	
				
			} else {
				resp = BaseJson.returnRespObj("mis/ec/platOrder/other/export", false, "�ӿ���֤�쳣", null);
				logger.error(url);
				logger.error("�ӿ���֤ʱ����" + resp);
			}
							
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("mis/ec/platOrder/other/export", false, "�����������ʱ�쳣", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				logger.error(url);
				logger.error("ƴ�ӷ���ֵʱ����" + resp);
			}
		}
		return resp;
	}
	
	@RequestMapping(value = "pdd", method = RequestMethod.POST)
	@ResponseBody
	private String testPdd(@RequestBody String jsonBody) {
		String url = "ec/platOrder/other/pdd";
		logger.info("url:"+url);
		logger.info("���������" + jsonBody);
		String resp = "";
		resp = platOrderOtherService.testPddOrder();
		return resp;
	}
	/*
	 * ����������ȡ��
	 */
	@RequestMapping(value = "recall", method = RequestMethod.POST)
	@ResponseBody
	private String recall(@RequestBody String jsonBody) {
		String url = "ec/platOrder/other/recall";
		logger.info("url:ec/platOrder/other/recall");
		logger.info("���������" + jsonBody);
		String resp = "";
		
		try {
			//��ȡ�ӿ�ǩ��
			String signKey = BaseJson.getReqHead(jsonBody).get("sign").asText(); //ǩ��
			String platId = BaseJson.getReqHead(jsonBody).get("platCode").asText(); //ƽ̨����
			String storeId = BaseJson.getReqHead(jsonBody).get("storeCode").asText();//���̱���
			//ǩ����֤
			if(signAuth(signKey,storeId)) {
				String userId = "";		
				userId = platId+storeId;
				String orderId = BaseJson.getReqBody(jsonBody).get("orderCode").asText(); //����id
				resp = platOrderOtherService.recallPlatOrderThird(orderId,storeId,userId);
				logger.info("���ز�����" + resp);
								
			} else {
				resp = BaseJson.returnRespObj("ec/platOrder/other/recall", false, "�ӿ���֤�쳣", null);
				logger.error(url);
				logger.error("�ӿ���֤ʱ����" + resp);
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/platOrder/other/recall", false, "�����������ʱ�쳣", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				logger.error(url);
				logger.error("ƴ�ӷ���ֵʱ����" + resp);
			}
		}	
		return resp;
	}
	
	
	private Boolean signAuth(String signKey,String storeId) {
		Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  //������
        String dateNowStr = sdf.format(d); 
        
        Date now_20 = new Date(d.getTime() - 120000); //20����ǰ��ʱ��
        String dateNowB_20 = sdf.format(now_20); 
       
        //ǩ�����ɹ���
        //signValue = md5�����ַ���(mdsc-��ǰ����-export-rasa3321)
        String signValue_20 = DigestUtils.md5Hex(storeId+"-"+dateNowB_20+"-export-rasa3321");
		String signValue = DigestUtils.md5Hex(storeId+"-"+dateNowStr+"-export-rasa3321");
		
		if(StringUtils.equals(signKey, signValue) || StringUtils.equals(signKey, signValue_20)) {
			return true;
		} else{
			return false;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
