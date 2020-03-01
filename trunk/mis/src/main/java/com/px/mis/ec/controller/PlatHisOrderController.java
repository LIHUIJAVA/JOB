package com.px.mis.ec.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//历史订单
@RequestMapping(value = "ec/platHisOrder", method = RequestMethod.POST)
@Controller
public class PlatHisOrderController {

	private Logger logger = LoggerFactory.getLogger(PlatOrderController.class);

	@Autowired
	private PlatOrderService platOrderService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/platOrder/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			PlatOrder platOrder = BaseJson.getPOJO(jsonBody, PlatOrder.class);
			List<Map> mList = BaseJson.getList(jsonBody);
			List<PlatOrders> paltOrdersList = new ArrayList<>();
			for (Map map : mList) {
				PlatOrders platOrders = new PlatOrders();
				BeanUtils.populate(platOrders, map);
				paltOrdersList.add(platOrders);
			}
			resp = platOrderService.add(userId, platOrder, paltOrdersList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/platOrder/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			PlatOrder platOrder = BaseJson.getPOJO(jsonBody, PlatOrder.class);
			List<Map> mList = BaseJson.getList(jsonBody);
			List<PlatOrders> paltOrdersList = new ArrayList<>();
			for (Map map : mList) {
				PlatOrders platOrders = new PlatOrders();
				BeanUtils.populate(platOrders, map);
				paltOrdersList.add(platOrders);
			}
			//resp = platOrderService.edit(platOrder, paltOrdersList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/platOrder/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		/*try {
			resp = platOrderService.delete(BaseJson.getReqBody(jsonBody).get("orderId").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/platOrder/query");
		logger.info("请求http：" + jsonBody);
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = platOrderService.query(BaseJson.getReqBody(jsonBody).get("orderId").asText());
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
		logger.info("url:ec/platOrder/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = platOrderService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

/*	@RequestMapping(value = "download", method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/platOrder/download");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		resp = platOrderService.download(jsonBody);
		logger.info("返回参数：" + resp);
		return resp;
	}*/

}
