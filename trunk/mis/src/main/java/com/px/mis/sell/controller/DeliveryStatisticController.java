package com.px.mis.sell.controller;
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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.sell.service.DeliveryStatisticService;
import com.px.mis.util.BaseJson;

@RequestMapping(value = "sell/Report", method = RequestMethod.POST)
@Controller
public class DeliveryStatisticController {

	private Logger logger = LoggerFactory.getLogger(DeliveryStatisticController.class);

	@Autowired
	private DeliveryStatisticService DeliveryStatisticService;
	
	//查询
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "SelectDeliveryStatistic", method = RequestMethod.POST)
	@ResponseBody
	private String SelectDeliveryStatistic(@RequestBody String jsonBody) throws IOException {
		logger.info("url:sell/Report/SelectDeliveryStatistic");
		logger.info("请求参数：" + jsonBody);
		String resp = "";

		try {

			ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
			Map<String, Object> map = BaseJson.getMap(reqBody);

			if (map.containsKey("formDt2") && !map.get("formDt2").equals("")) {

				map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("formDt1") && !map.get("formDt1").equals("")) {
				map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
			}

			
			resp = DeliveryStatisticService.SelectDeliveryStatistic(map);
		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("url:sell/Report/SelectDeliveryStatistic", false, e.getMessage(), null);

		}

		logger.info("返回参数：" + resp);
		return resp;

	}
	//打印
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "printDeliveryStatistic", method = RequestMethod.POST)
	@ResponseBody
	private String printDeliveryStatistic(@RequestBody String jsonBody) throws IOException {
		logger.info("url:sell/Report/printDeliveryStatistic");
		logger.info("请求参数：" + jsonBody);
		String resp = "";

		try {

			ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
			Map<String, Object> map = BaseJson.getMap(reqBody);

			if (map.containsKey("formDt2") && !map.get("formDt2").equals("")) {

				map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("formDt1") && !map.get("formDt1").equals("")) {
				map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
			}

			resp = DeliveryStatisticService.printDeliveryStatistic(map);
		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("url:sell/Report/SelectDeliveryStatistic", false, e.getMessage(), null);

		}

		logger.info("返回参数：" + resp);
		return resp;

	}

}
