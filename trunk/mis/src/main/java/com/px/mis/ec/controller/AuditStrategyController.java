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

import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

@RequestMapping(value="ec/auditStrategy",method = RequestMethod.POST)
@Controller
public class AuditStrategyController {
	private Logger logger = LoggerFactory.getLogger(AuditStrategyController.class);
	@Autowired
	private AuditStrategyService auditStrategyService;

	@RequestMapping(value="addStrategy",method = RequestMethod.POST)
	@ResponseBody
	private String addStrategy(@RequestBody String jsonBody) {
		logger.info("url:ec/auditStrategy/addStrategy");
		logger.info("请求参数："+jsonBody);
		String resp=auditStrategyService.insert(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/auditStrategy/addStrategy");
		logger.info("请求参数："+jsonBody);
		String resp=auditStrategyService.delete(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	private String update(@RequestBody String jsonBody) {
		logger.info("url:ec/auditStrategy/update");
		logger.info("请求参数："+jsonBody);
		String resp=auditStrategyService.update(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="selectList",method = RequestMethod.POST)
	@ResponseBody
	private String selectList(@RequestBody String jsonBody) {
		logger.info("url:ec/auditStrategy/selectList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=auditStrategyService.selectList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return resp;
	}
	@RequestMapping(value="findById",method = RequestMethod.POST)
	@ResponseBody
	private String findById(@RequestBody String jsonBody) {
		logger.info("url:ec/auditStrategy/findById");
		logger.info("请求参数："+jsonBody);
		String resp=auditStrategyService.findById(jsonBody);
		logger.info("返回参数："+resp);
		return resp;
	}
	
	
	
}
