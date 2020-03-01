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

import com.px.mis.ec.entity.RefundOrderStatus;
import com.px.mis.ec.service.RefundOrderStatusService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//退款单状态
@RequestMapping(value="ec/refundOrderStatus",method = RequestMethod.POST)
@Controller
public class RefundOrderStatusController {
	
	private Logger logger = LoggerFactory.getLogger(RefundOrderStatusController.class);
	
	@Autowired
	private RefundOrderStatusService refundOrderStatusService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrderStatus/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			RefundOrderStatus refundOrderStatus = BaseJson.getPOJO(jsonBody, RefundOrderStatus.class);
			resp=refundOrderStatusService.add(refundOrderStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrderStatus/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			RefundOrderStatus refundOrderStatus = BaseJson.getPOJO(jsonBody, RefundOrderStatus.class);
			resp=refundOrderStatusService.edit(refundOrderStatus);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrderStatus/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=refundOrderStatusService.delete(BaseJson.getReqBody(jsonBody).get("refStatusId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrderStatus/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=refundOrderStatusService.query(BaseJson.getReqBody(jsonBody).get("refStatusId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrderStatus/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=refundOrderStatusService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}

}
