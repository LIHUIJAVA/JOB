package com.px.mis.ec.controller;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.entity.ProActivity;
import com.px.mis.ec.entity.ProActivitys;
import com.px.mis.ec.service.ProActivityService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//促销活动
@RequestMapping(value = "ec/proActivity", method = RequestMethod.POST)
@Controller
public class ProActivityController {

	private Logger logger = LoggerFactory.getLogger(ProActivityController.class);

	@Autowired
	private ProActivityService proActService;

	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/add");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {

			ProActivity proAct = BaseJson.getPOJO(jsonBody, ProActivity.class);
			proAct.setCreator(BaseJson.getReqHead(jsonBody).get("userName").asText()); // 制单人
			proAct.setAuditor(null); // 审核人
			proAct.setAuditDate(null); // 审核日期
			proAct.setAuditResult("0"); // 审核结果
			ArrayNode proActsArray = (ArrayNode) BaseJson.getReqBody(jsonBody).get("list");
			List<ProActivitys> proActsList = new ArrayList<>();
			for (Iterator<JsonNode> proActsArrayIterator = proActsArray.iterator(); proActsArrayIterator.hasNext();) {
				JsonNode proActsNode = proActsArrayIterator.next();
				ProActivitys proActs = JacksonUtil.getPOJO((ObjectNode) proActsNode, ProActivitys.class);
				proActs.setProActId(proAct.getProActId());
				proActs.setProActName(proAct.getProActName());
				proActsList.add(proActs);
			}
			resp = proActService.add(proAct, proActsList);
		} catch (Exception e) {
			logger.error("url:ec/proActivity/add 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/edit");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			ProActivity proAct = BaseJson.getPOJO(jsonBody, ProActivity.class);
			proAct.setCreator(BaseJson.getReqHead(jsonBody).get("userName").asText()); // 制单人
			proAct.setAuditor(null); // 审核人
			proAct.setAuditDate(null); // 审核日期
			proAct.setAuditResult("0"); // 审核结果
			ArrayNode proActsArray = (ArrayNode) BaseJson.getReqBody(jsonBody).get("list");
			List<ProActivitys> proActsList = new ArrayList<>();
			for (Iterator<JsonNode> proActsArrayIterator = proActsArray.iterator(); proActsArrayIterator.hasNext();) {
				JsonNode proActsNode = proActsArrayIterator.next();
				ProActivitys proActs = JacksonUtil.getPOJO((ObjectNode) proActsNode, ProActivitys.class);
				proActs.setProActId(proAct.getProActId());
				proActs.setProActName(proAct.getProActName());
				proActsList.add(proActs);
			}
			resp = proActService.edit(proAct, proActsList);
		} catch (Exception e) {
			logger.error("url:ec/proActivity/edit 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/delete");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = proActService.delete(BaseJson.getReqBody(jsonBody).get("proActId").asText());
		} catch (Exception e) {
			logger.error("url:ec/proActivity/delete 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/query");
		logger.info("请求http：" + jsonBody);
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp = proActService.query(BaseJson.getReqBody(jsonBody).get("proActId").asText());
		} catch (Exception e) {
			logger.error("url:ec/proActivity/query 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/queryList");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = proActService.queryList(map);
		} catch (Exception e) {
			logger.error("url:ec/proActivity/queryList 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 审核
	@RequestMapping(value = "updateAuditResult", method = RequestMethod.POST)
	@ResponseBody
	private String updateAuditResult(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/updateAuditResult");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			List<Map> oTabMap = BaseJson.getList(jsonBody);
			List<ProActivity> oList = new ArrayList<ProActivity>();
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

			for (Map map : oTabMap) {
				ProActivity oSubTab = new ProActivity();
				try {
					map.put("auditor", userName);
					map.put("auditResult", "1");
					map.put("auditDate", LocalDateTime.now().toString());

					BeanUtils.populate(oSubTab, map);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				oList.add(oSubTab);
			}
			resp = proActService.updateAudit(oList);
		} catch (Exception e) {
			logger.error("url:ec/proActivity/updateAuditResult 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

	// 弃审
	@RequestMapping(value = "updateAuditResultNo", method = RequestMethod.POST)
	@ResponseBody
	private String updateAuditResultNo(@RequestBody String jsonBody) {
		logger.info("url:ec/proActivity/updateAuditResultNo");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			List<Map> oTabMap = BaseJson.getList(jsonBody);
			List<ProActivity> oList = new ArrayList<ProActivity>();
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

			for (Map map : oTabMap) {
				ProActivity oSubTab = new ProActivity();
				try {
					map.put("auditor", userName);
					map.put("auditResult", "0");
					map.put("auditDate", null);
					map.remove("auditor");
					BeanUtils.populate(oSubTab, map);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				oList.add(oSubTab);
			}
			resp = proActService.updateAuditNo(oList);
		} catch (Exception e) {
			logger.error("url:ec/proActivity/updateAuditResultNo 错误信息：", e);
		}
		logger.info("返回参数：" + resp);
		return resp;
	}

}
