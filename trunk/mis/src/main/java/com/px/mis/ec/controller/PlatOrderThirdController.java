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

//微商城订单
@RequestMapping(value = "ec/platOrder/other", method = RequestMethod.POST)
@Controller
public class PlatOrderThirdController {
	
	private Logger logger = LoggerFactory.getLogger(PlatOrderController.class);
	
	@Autowired
	private PlatOrderThirdService platOrderOtherService;
	
	/*
	 * 第三方订单导入
	 */
	@RequestMapping(value = "export", method = RequestMethod.POST)
	@ResponseBody
	private String export(@RequestBody String jsonBody) {
		String url = "ec/platOrder/other/export";
		logger.info("url:"+url);
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			//获取接口签名
			String signKey = BaseJson.getReqHead(jsonBody).get("sign").asText(); //签名
			String platId = BaseJson.getReqHead(jsonBody).get("platCode").asText(); //平台编码
			String storeId = BaseJson.getReqHead(jsonBody).get("storeCode").asText();//店铺编码
			
			//签名认证
			if(signAuth(signKey,storeId)) {
				String userId = "";		
				userId = platId+storeId;
				Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
				ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
				ArrayNode platOrdersArray = (ArrayNode) reqBody.get("orderInfoList");//导入订单数据集合
				List<PlatOrderThird> paltOrderThirdList = new ArrayList<>();
				for(Iterator<JsonNode> platOrdersArrayIterator = platOrdersArray.iterator(); platOrdersArrayIterator.hasNext();) {
					JsonNode platOrdersNode = platOrdersArrayIterator.next();
					PlatOrderThird platOrderThird = JacksonUtil.getPOJO((ObjectNode)platOrdersNode, PlatOrderThird.class);
					paltOrderThirdList.add(platOrderThird);
				}

				if(paltOrderThirdList.size() == 0) {
					resp = BaseJson.returnRespObj("mis/ec/platOrder/other/export", false, "导入数据不能为空", null);
					
				} else {
					resp = platOrderOtherService.exportPlatOrderThird(userId,platId,storeId,paltOrderThirdList);
					
					logger.info("返回参数：" + resp);
				}	
				
			} else {
				resp = BaseJson.returnRespObj("mis/ec/platOrder/other/export", false, "接口认证异常", null);
				logger.error(url);
				logger.error("接口认证时错误：" + resp);
			}
							
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("mis/ec/platOrder/other/export", false, "解析请求参数时异常", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				logger.error(url);
				logger.error("拼接返回值时错误：" + resp);
			}
		}
		return resp;
	}
	
	@RequestMapping(value = "pdd", method = RequestMethod.POST)
	@ResponseBody
	private String testPdd(@RequestBody String jsonBody) {
		String url = "ec/platOrder/other/pdd";
		logger.info("url:"+url);
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = platOrderOtherService.testPddOrder();
		return resp;
	}
	/*
	 * 第三方订单取消
	 */
	@RequestMapping(value = "recall", method = RequestMethod.POST)
	@ResponseBody
	private String recall(@RequestBody String jsonBody) {
		String url = "ec/platOrder/other/recall";
		logger.info("url:ec/platOrder/other/recall");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		
		try {
			//获取接口签名
			String signKey = BaseJson.getReqHead(jsonBody).get("sign").asText(); //签名
			String platId = BaseJson.getReqHead(jsonBody).get("platCode").asText(); //平台编码
			String storeId = BaseJson.getReqHead(jsonBody).get("storeCode").asText();//店铺编码
			//签名认证
			if(signAuth(signKey,storeId)) {
				String userId = "";		
				userId = platId+storeId;
				String orderId = BaseJson.getReqBody(jsonBody).get("orderCode").asText(); //订单id
				resp = platOrderOtherService.recallPlatOrderThird(orderId,storeId,userId);
				logger.info("返回参数：" + resp);
								
			} else {
				resp = BaseJson.returnRespObj("ec/platOrder/other/recall", false, "接口认证异常", null);
				logger.error(url);
				logger.error("接口认证时错误：" + resp);
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("ec/platOrder/other/recall", false, "解析请求参数时异常", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				logger.error(url);
				logger.error("拼接返回值时错误：" + resp);
			}
		}	
		return resp;
	}
	
	
	private Boolean signAuth(String signKey,String storeId) {
		Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  //年月日
        String dateNowStr = sdf.format(d); 
        
        Date now_20 = new Date(d.getTime() - 120000); //20分钟前的时间
        String dateNowB_20 = sdf.format(now_20); 
       
        //签名生成规则
        //signValue = md5加密字符串(mdsc-当前日期-export-rasa3321)
        String signValue_20 = DigestUtils.md5Hex(storeId+"-"+dateNowB_20+"-export-rasa3321");
		String signValue = DigestUtils.md5Hex(storeId+"-"+dateNowStr+"-export-rasa3321");
		
		if(StringUtils.equals(signKey, signValue) || StringUtils.equals(signKey, signValue_20)) {
			return true;
		} else{
			return false;
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
