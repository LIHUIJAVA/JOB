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

import com.px.mis.ec.service.RefundOrderService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//退款单
@RequestMapping(value = "ec/refundOrder", method = RequestMethod.POST)
@Controller
public class RefundOrderController {

	private Logger logger = LoggerFactory.getLogger(RefundOrderController.class);

	@Autowired
	private RefundOrderService refundOrderService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.add(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.edit(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.delete(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/query");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = refundOrderService.query(BaseJson.getReqBody(jsonBody).get("refId").asText());
		} catch (IOException e) {
			logger.error("URL:ec/refundOrder/query 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "unionQuery", method = RequestMethod.POST)
	@ResponseBody
	private String unionQuery(@RequestBody String jsonBody) {
		String url = "ec/refundOrder/unionQuery";
		logger.info("url:"+url);
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = refundOrderService.unionQuery(BaseJson.getReqBody(jsonBody).get("refId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	
	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.queryList(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	@ResponseBody
	private String audit(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/audit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.audit(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "noAudit", method = RequestMethod.POST)
	@ResponseBody
	private String noAudit(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/noAudit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.noAudit(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "refundReference", method = RequestMethod.POST)
	@ResponseBody
	private String refundReference(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/refundReference");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = refundOrderService.refundReference(BaseJson.getReqBody(jsonBody).get("ecOrderId").asText(),BaseJson.getReqBody(jsonBody).get("orderId").asText());
		} catch (IOException e) {
			logger.error("URL:ec/refundOrder/query 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/download");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String startDate = map.get("startDate").toString();
			String endDate = map.get("endDate").toString();
			String ecOrderId = map.get("ecOrderId").toString();
			int pageNo=1;
			int pageSize=20;
			String storeId = map.get("storeId").toString();
			resp = refundOrderService.download(userId, startDate, endDate, pageNo, pageSize, storeId,ecOrderId);
		} catch (IOException e) {
			logger.error("URL:ec/refundOrder/query 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	@RequestMapping(value = "exportList", method = RequestMethod.POST)
	@ResponseBody
	private String exportList(@RequestBody String jsonBody) {
		logger.info("url:ec/refundOrder/exportList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = refundOrderService.exportList(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}
	

}
