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

import com.px.mis.ec.entity.Brokerage;
import com.px.mis.ec.service.BrokerageService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//佣金扣点表
@RequestMapping(value="ec/brokerage",method = RequestMethod.POST)
@Controller
public class BrokerageController {
	
	private Logger logger = LoggerFactory.getLogger(BrokerageController.class);
	
	@Autowired
	private BrokerageService brokerageService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/brokerage/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Brokerage brokerage = BaseJson.getPOJO(jsonBody, Brokerage.class);
			resp=brokerageService.add(brokerage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/brokerage/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Brokerage brokerage = BaseJson.getPOJO(jsonBody, Brokerage.class);
			resp=brokerageService.edit(brokerage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/brokerage/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=brokerageService.delete(BaseJson.getReqBody(jsonBody).get("brokId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/brokerage/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=brokerageService.query(BaseJson.getReqBody(jsonBody).get("brokId").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/brokerage/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=brokerageService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="test",method = RequestMethod.POST)
	@ResponseBody
	private String test(@RequestBody String jsonBody) {
		System.out.println(jsonBody);
		return "{\r\n" + 
				"	\"v\":\"2.0\",\r\n" + 
				"	\"method\":\"jingdong.pop.order.search\",\r\n" + 
				"	\"app_key\":\"827ADD63009AFF59FF31ED1F73D5459B\",\r\n" + 
				"	\"access_token\":\"a44b6a56-5a85-4369-ae92-1b49899d9402\",\r\n" + 
				"	\"timestamp\":\"2018-12-26 16:54:58\",\r\n" + 
				"	\"sign\":\"9D95C7B56245FEEC7E51F0864C39D2E4\",\r\n" + 
				"	\"360buy_param_json\":{\r\n" + 
				"		\"start_date\":\"\",\r\n" + 
				"		\"end_date\":\"\",\r\n" + 
				"		\"order_state\":\"WAIT_SELLER_STOCK_OUT \",\r\n" + 
				"		\"optional_fields\":\"orderId,venderId \",\r\n" + 
				"		\"page\":\"1\",\r\n" + 
				"		\"page_size\":\"20\",\r\n" + 
				"		\"sortType\":\"\",\r\n" + 
				"		\"dateType\":\"\"\r\n" + 
				"	}\r\n" + 
				"}";
	}

}
