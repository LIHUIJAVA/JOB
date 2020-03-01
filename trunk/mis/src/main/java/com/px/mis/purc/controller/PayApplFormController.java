package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.PayApplForm;
import com.px.mis.purc.entity.PayApplFormSub;
import com.px.mis.purc.service.PayApplFormService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

/** 付款申请单 */
@RequestMapping(value = "purc/PayApplForm")
@Controller
public class PayApplFormController {

	private Logger logger = LoggerFactory.getLogger(PayApplFormController.class);

	@Autowired
	private PayApplFormService applFormService;
    //记账
	@Autowired
	FormBookService formBookService;

	/* 新增 */
	@RequestMapping("addPayApplForm")
	@ResponseBody
	public Object insertPayApplForm(@RequestBody String jsonBody) {
		logger.info("url:purc/PayApplForm/addPayApplForm");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		ObjectNode aString;
		try {
			aString = BaseJson.getReqHead(jsonBody);

			String userId = aString.get("accNum").asText();
			String userName = aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();

			PayApplForm payApplForm = BaseJson.getPOJO(jsonBody, PayApplForm.class);

			payApplForm.setSetupPers(userName);
			List<PayApplFormSub> payApplFormSubList = new ArrayList<>();
			payApplFormSubList=BaseJson.getPOJOList(jsonBody, PayApplFormSub.class);
			resp = applFormService.addPayApplForm(userId, payApplForm, payApplFormSubList,loginTime);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("purc/PayApplForm/addPayApplForm", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		logger.info("返回参数：" + resp);
		return resp;

	}

	/* 修改 */
	@RequestMapping("editPayApplForm")
	public @ResponseBody String editPayApplForm(@RequestBody String jsonData) {
		logger.info("url:purc/PayApplForm/editPayApplForm");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		ObjectNode aString;
		try {
			aString = BaseJson.getReqHead(jsonData);
			PayApplForm payApplForm = BaseJson.getPOJO(jsonData, PayApplForm.class);
			String userName = aString.get("userName").asText();
			payApplForm.setMdfr(userName);

			List<PayApplFormSub> payApplFormSubList = new ArrayList<>();
			payApplFormSubList=BaseJson.getPOJOList(jsonData, PayApplFormSub.class);
			resp = applFormService.editPayApplForm(payApplForm, payApplFormSubList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("purc/PayApplForm/editPayApplForm", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		logger.info("返回参数：" + resp);
		return resp;

	}

	/* 删除 */
	@RequestMapping("deleteEntrsAgnAdj")
	public @ResponseBody Object deleteEntrsAgnAdj(@RequestBody String jsonData) {
		logger.info("url:purc/PayApplForm/deleteEntrsAgnAdj");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String clsId = reqBody.get("payApplId").asText();
			resp = applFormService.deleteEntrsAgnAdj(clsId);
		} catch (Exception e) {
			//e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("purc/PayApplForm/deleteEntrsAgnAdj", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	/* 单条查询 */
	@RequestMapping("queryPayApplForm")
	@ResponseBody
	public String queryPayApplForm(@RequestBody String jsonData) {
		logger.info("url:purc/PayApplForm/queryPayApplForm");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			resp = applFormService.queryPayApplForm(BaseJson.getReqBody(jsonData).get("payApplId").asText());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			try {
				resp = BaseJson.returnRespObj("purc/PayApplForm/queryPayApplForm", false, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	/* 分页查询所有 */
	@RequestMapping("queryPayApplFormList")
	@ResponseBody
	public Object queryPayApplFormList(@RequestBody String jsonData) throws IOException {
		logger.info("url:purc/PayApplForm/queryPayApplFormList");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonData).toString());
			if (map.containsKey("payApplDt2") && !map.get("payApplDt2").equals("")) {
				map.put("payApplDt2", map.get("payApplDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("payApplDt1") && !map.get("payApplDt1").equals("")) {
				map.put("payApplDt1", map.get("payApplDt1").toString() + " 00:00:00");
			}
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = applFormService.queryPayApplFormList(map);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			resp = BaseJson.returnRespObj("purc/PayApplForm/queryList", false, "查询失败" + e.getMessage(), null);

		}
		return resp;
	}
	
	//批量更新付款申请单审核状态
	@RequestMapping(value="updatePayApplFormIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateToGdsSnglIsNtChk(@RequestBody String jsonBody) throws Exception {
		logger.info("url:purc/PayApplForm/updatePayApplFormIsNtChk");
		logger.info("请求参数："+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp="";
		boolean isSuccess = false;
		List<PayApplForm> payApplFormList=new ArrayList();
		try {
			
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/PayApplForm/updatePayApplFormIsNtChk", false, "当月已封账！", null);
				logger.info("返回参数：" + resp);
				return resp;
			}
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//		    payApplFormList= jsonArray.toJavaList(PayApplForm.class);
			payApplFormList = BaseJson.getPOJOList(jsonBody, PayApplForm.class);
			for (PayApplForm payApplForm : payApplFormList) {
				try
				{
					payApplForm.setChkr(userName);
					result=applFormService.updatePayApplFormIsNtChk(payApplForm);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}	
			resp=BaseJson.returnRespObj("purc/PayApplForm/updatePayApplFormIsNtChk", isSuccess, resp, null);
		    logger.info("返回参数："+resp);
	    }catch (Exception ex) {
			ex.printStackTrace();
			logger.info("返回参数："+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/PayApplForm/updatePayApplFormIsNtChk", false, ex.getMessage(), null);
		}
	    
		return resp;
	}
	
	@RequestMapping("queryPayApplFormListOrderBy")
	@ResponseBody
	public Object queryPayApplFormListOrderBy(@RequestBody String jsonData) throws IOException {
		logger.info("url:purc/PayApplForm/queryPayApplFormListOrderBy");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonData).toString());
			if (map.containsKey("payApplDt2") && !map.get("payApplDt2").equals("")) {
				map.put("payApplDt2", map.get("payApplDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("payApplDt1") && !map.get("payApplDt1").equals("")) {
				map.put("payApplDt1", map.get("payApplDt1").toString() + " 00:00:00");
			}
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);

			resp = applFormService.queryPayApplFormListOrderBy(map);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			resp = BaseJson.returnRespObj("purc/PayApplForm/queryPayApplFormListOrderBy", false, "查询失败" + e.getMessage(), null);

		}
		return resp;
	}
	
	@RequestMapping("printPayApplFormList")
	@ResponseBody
	public Object printPayApplFormList(@RequestBody String jsonData) throws IOException {
		logger.info("url:purc/PayApplForm/printPayApplFormList");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonData).toString());
			if (map.containsKey("payApplDt2") && !map.get("payApplDt2").equals("")) {
				map.put("payApplDt2", map.get("payApplDt2").toString() + " 23:59:59");
			}
			if (map.containsKey("payApplDt1") && !map.get("payApplDt1").equals("")) {
				map.put("payApplDt1", map.get("payApplDt1").toString() + " 00:00:00");
			}
		

			resp = applFormService.printPayApplFormList(map);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			resp = BaseJson.returnRespObj("purc/PayApplForm/printPayApplFormList", false, "查询失败" + e.getMessage(), null);

		}
		return resp;
	}
	
	@RequestMapping("pushToU8")
	@ResponseBody
	@CrossOrigin
	public Object pushToU8(@RequestBody String jsonData) throws IOException {
		logger.info("url:purc/PayApplForm/pushToU8");
		logger.info("请求参数：" + jsonData);
		String resp = "";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String ids = reqBody.get("ids").asText();
			resp = applFormService.pushToU8(ids);

		} catch (Exception e) {
			
			//e.printStackTrace();
			resp = BaseJson.returnRespObj("purc/PayApplForm/pushToU8", false, "查询失败" + e.getMessage(), null);

		}
		return resp;
	}

}
