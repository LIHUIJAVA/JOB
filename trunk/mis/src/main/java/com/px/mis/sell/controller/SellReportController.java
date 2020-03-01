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
import com.px.mis.sell.service.SellReportService;
import com.px.mis.util.BaseJson;


@RequestMapping(value = "sell/Report", method = RequestMethod.POST)
@Controller
public class SellReportController {
	private Logger logger = LoggerFactory.getLogger(SellReportController.class);

	@Autowired
	SellReportService sellReportService;

	// 销售现存量报表
	// @SuppressWarnings("all")
	@RequestMapping(value = "sellNowStokReport", method = RequestMethod.POST)
	@ResponseBody
	private String sellNowStokReport(@RequestBody String jsonBody) throws IOException {
		logger.info("url:sell/Report/sellNowStokReport");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {

					ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
					Map<String, Object> map = BaseJson.getMap(reqBody);
			
			 
//            if (map.containsKey("formDt2") && !map.get("formDt2").equals("")) {
//
//				map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
//			}
//			if (map.containsKey("formDt1") && !map.get("formDt1").equals("")) {
//				map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
//			}

			resp = sellReportService.sellNowStokReport(map);
		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("sell/Report/sellNowStokReport", false, e.getMessage(), null);

		}

		logger.info("返回参数：" + resp);
		return resp;
	}

	// 发货统计报表
	// @SuppressWarnings("all")
	@RequestMapping(value = "sellStatisticalReport", method = RequestMethod.POST)
	@ResponseBody
	private String sellStatisticalReport(@RequestBody String jsonBody) throws IOException {
		logger.info("url:sell/Report/sellStatisticalReport");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			// JSONObject
			ObjectNode jsonObject = BaseJson.getReqBody(jsonBody);
			Map<String, Object> map = BaseJson.getMap(jsonObject);
			if (map.containsKey("fromDt2") && !map.get("fromDt2").equals("")) {

				map.put("fromDt2", map.get("fromDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("fromDt1") && !map.get("fromDt1").equals("")) {
				map.put("fromDt1", map.get("fromDt1").toString() + " 00:00:00");
			}

			resp = sellReportService.sellSellStatisticalReport(map);
		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("sell/Report/sellStatisticalReport", false, e.getMessage(), null);

		}

		logger.info("返回参数：" + resp);
		return resp;
	}

	// 销售综合统计表
	// @SuppressWarnings("all")
	@RequestMapping(value = "sellSalesStatisticsReport", method = RequestMethod.POST)
	@ResponseBody
	private String sellSalesStatisticsReport(@RequestBody String jsonBody) {
		logger.info("url:sell/Report/sellSalesStatisticsReport");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			// JSONObject
			ObjectNode jsonObject = BaseJson.getReqBody(jsonBody);
			Map<String, Object> map = BaseJson.getMap(jsonObject);
			if (map.containsKey("fromDt2") && !map.get("fromDt2").equals("")) {

				map.put("fromDt2", map.get("fromDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("fromDt1") && !map.get("fromDt1").equals("")) {
				map.put("fromDt1", map.get("fromDt1").toString() + " 00:00:00");
			}

			resp = sellReportService.sellSalesStatisticsReport(map);

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			e.toString();
			try {
				resp = BaseJson.returnRespObj("sell/Report/sellSalesStatisticsReport", false, "查询条件有错", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		logger.info("返回参数：" + resp);
		return resp;
	}

	// ProcurementNowStokReport
	// 采购现存量查询
	// @SuppressWarnings("all")
	@RequestMapping(value = "sellProcurementNowStokReport", method = RequestMethod.POST)
	@ResponseBody
	private String sellProcurementNowStokReport(@RequestBody String jsonBody) {
		logger.info("url:sell/Report/sellProcurementNowStokReport");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			// JSONObject
			ObjectNode jsonObject = BaseJson.getReqBody(jsonBody);
			Map<String, Object> map = BaseJson.getMap(jsonObject);
//			if (map.containsKey("formDt2") && !map.get("formDt2").equals("")) {
//
//				map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
//			}
//			if (map.containsKey("formDt1") && !map.get("formDt1").equals("")) {
//				map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
//			}

			resp = sellReportService.sellProcurementNowStokReport(map);

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			e.toString();
			try {
				resp = BaseJson.returnRespObj("sell/Report/sellProcurementNowStokReport", false, "查询条件有错", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		logger.info("返回参数：" + resp);
		return resp;
	}

//	JSONObject jsonObject = JSON.parseObject(jsonBody);
//	JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//    entrsAgnAdjList= jsonArray.toJavaList(EntrsAgnAdj.class);

	// 发货汇总报表
	// @SuppressWarnings("all")
	@RequestMapping(value = "sellStatisticalCount", method = RequestMethod.POST)
	@ResponseBody
	private String sellStatisticalCount(@RequestBody String jsonBody) throws IOException {
		logger.info("url:sell/Report/sellStatisticalCount");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			// JSONObject
			ObjectNode jsonObject = BaseJson.getReqBody(jsonBody);
			Map<String, Object> map = BaseJson.getMap(jsonObject);
			if (map.containsKey("formDt2") && !map.get("formDt2").equals("")) {

				map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("formDt1") && !map.get("formDt1").equals("")) {
				map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
			}
			
			resp = sellReportService.sellStatisticalCount(map);
		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("sell/Report/sellStatisticalCount", false, e.getMessage(), null);

		}
		logger.info("返回参数：" + resp);
		return resp;
	}

}
