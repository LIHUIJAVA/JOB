package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.entity.OrderStatus;
import com.px.mis.ec.service.OrderStatusService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//订单状态
@RequestMapping(value="ec/orderStatus",method = RequestMethod.POST)
@Controller
public class OrderStatusController {
	
	private Logger logger = LoggerFactory.getLogger(OrderStatusController.class);
	
	@Autowired
	private OrderStatusService orderStatusService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/orderStatus/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			OrderStatus orderStatus = BaseJson.getPOJO(jsonBody, OrderStatus.class);
			resp=orderStatusService.add(orderStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/orderStatus/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			OrderStatus orderStatus = BaseJson.getPOJO(jsonBody, OrderStatus.class);
			resp=orderStatusService.edit(orderStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/orderStatus/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=orderStatusService.delete(BaseJson.getReqBody(jsonBody).get("orderStatusId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/orderStatus/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=orderStatusService.query(BaseJson.getReqBody(jsonBody).get("orderStatusId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/orderStatus/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=orderStatusService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}

}
