package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.List;
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
import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.service.OrderAdjustService;
import com.px.mis.util.BaseJson;
//�����ĵ�����
@RequestMapping(value="ec/orderAdjust",method = RequestMethod.POST)
@Controller
public class OrderAdjustController {
	
	private Logger logger = LoggerFactory.getLogger(OrderAdjustController.class);
	
	@Autowired
	private OrderAdjustService orderAdjustService;
	//��Ʒ����֮ǰ��չʾ
	@RequestMapping(value="showAdjustGoods",method = RequestMethod.POST)
	@ResponseBody
	private String showAdjustGoods(@RequestBody String jsonData) {
		logger.info("url:ec/orderAdjust/showAdjustGoods");
		logger.info("���������"+jsonData);
		String resp="";
		ObjectNode reqBody=null;
		String orderId=null;
		try {
			reqBody = BaseJson.getReqBody(jsonData);
			orderId= reqBody.get("orderId").asText();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/orderAdjust/showAdjustGoods �쳣˵����",e1);
			try {
				resp = TransformJson.returnRespList("ec/orderAdjust/showAdjustGoods", false,"���������������������������Ƿ���ȷ������ʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/orderAdjust/showAdjustGoods �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp = orderAdjustService.showAdjustGoods(orderId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/orderAdjust/showAdjustGoods �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	//��Ʒ����
	@RequestMapping(value="editAdjustGoods",method = RequestMethod.POST)
	@ResponseBody
	private String editAdjustGoods(@RequestBody String jsonData) {
		logger.info("url:ec/orderAdjust/editAdjustGoods");
		logger.info("���������"+jsonData);
		String resp="";
		List<Map> list=null;
		try {
			list = BaseJson.getList(jsonData);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:ec/orderAdjust/editAdjustGoods �쳣˵����",e1);
			try {
				resp = BaseJson.returnRespObj("ec/orderAdjust/editAdjustGoods", false, "���������������������������Ƿ���ȷ����Ʒ����ʧ�ܣ�", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:ec/orderAdjust/editAdjustGoods �쳣˵����",e);
			}
			return resp;
		}
		try {
			resp = orderAdjustService.editAdjustGoods(list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:ec/orderAdjust/editAdjustGoods �쳣˵����",e);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//���������Ƕ����ò�ֺͺϲ������������ʱ���á�
	@RequestMapping(value="showSplitOrder",method = RequestMethod.POST)
	@ResponseBody
	private String showSplitOrder(@RequestBody String jsonData) {
		logger.info("url:ec/orderAdjust/showSplitOrder");
		logger.info("���������"+jsonData);
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String orderId = reqBody.get("orderId").asText();
			resp = orderAdjustService.showSplitOrder(orderId);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="editSplitOrder",method = RequestMethod.POST)
	@ResponseBody
	private String editSplitOrder(@RequestBody String jsonData) {
		logger.info("url:ec/orderAdjust/editSplitOrder");
		logger.info("���������"+jsonData);
		String resp="";
		try {
			PlatOrder platOrder=BaseJson.getPOJO(jsonData,PlatOrder.class);
			resp = orderAdjustService.editSplitOrder(platOrder);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="showMergeOrder",method = RequestMethod.POST)
	@ResponseBody
	private String showMergeOrder(@RequestBody String jsonData) {
		logger.info("url:ec/orderAdjust/showSplitOrder");
		logger.info("���������"+jsonData);
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String orderIds = reqBody.get("orderIds").asText();
			resp = orderAdjustService.showMergeOrder(orderIds);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="editMergeOrder",method = RequestMethod.POST)
	@ResponseBody
	private String editMergeOrder(@RequestBody String jsonData) {
		logger.info("url:ec/orderAdjust/editMergeOrder");
		logger.info("���������"+jsonData);
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String orderId = reqBody.get("orderId").asText();
			System.out.println(orderId);
			List<Map> list = BaseJson.getList(jsonData);
			for(Map map:list) {
				System.out.println(map);
			}
			resp = orderAdjustService.editMergeOrder(list,orderId);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
}
