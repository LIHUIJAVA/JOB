package com.px.mis.purc.controller;

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

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.EntrsAgnAdj;
import com.px.mis.purc.entity.EntrsAgnAdjSub;
import com.px.mis.purc.service.EntrsAgnAdjService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*委托代销调整单功能描述*/
@RequestMapping(value="purc/EntrsAgnAdj")
@Controller
public class EntrsAgnAdjController {
	
	private Logger logger = LoggerFactory.getLogger(EntrsAgnAdjController.class);
	
	@Autowired
	private EntrsAgnAdjService eaas;
	
	//新增委托代销调整单信息
	@RequestMapping(value="addEntrsAgnAdj",method = RequestMethod.POST)
	@ResponseBody
	private String addEntrsAgnAdj(@RequestBody String jsonBody)throws Exception {
		logger.info("url:purc/EntrsAgnAdj/addEntrsAgnAdj");
		logger.info("请求参数："+jsonBody);
		String resp="";
		
		List<EntrsAgnAdj> entrsAgnAdjList =new ArrayList<EntrsAgnAdj>();
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String loginTime=aString.get("loginTime").asText();
			
//			JSONObject jsonObject = JSON.parseObject(jsonBody);
//			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
//		    JSONArray jsonArray = jsonObjectList.getJSONArray("list");
//		    entrsAgnAdjList= jsonArray.toJavaList(EntrsAgnAdj.class);
		    entrsAgnAdjList=BaseJson.getPOJOList(jsonBody, EntrsAgnAdj.class);
			resp=eaas.addEntrsAgnAdj(userId,entrsAgnAdjList,loginTime);
		} catch (Exception ex) {
			ex.printStackTrace();
			resp=BaseJson.returnRespObj("purc/EntrsAgnAdj/addEntrsAgnAdj", false,  ex.getMessage(), null);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//修改委托代销调整单信息
	@RequestMapping(value="editEntrsAgnAdj",method = RequestMethod.POST)
	@ResponseBody
	private String editEntrsAgnAdj(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnAdj/editEntrsAgnAdj");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			EntrsAgnAdj entrsAgnAdj = BaseJson.getPOJO(jsonBody, EntrsAgnAdj.class);
			List<Map> mList=BaseJson.getList(jsonBody);
			List<EntrsAgnAdjSub> entrsAgnAdjSubList=new ArrayList<>();
			for (Map map : mList) {
				EntrsAgnAdjSub entrsAgnAdjSub = new EntrsAgnAdjSub();
				entrsAgnAdjSub.setAdjSnglId(entrsAgnAdj.getAdjSnglId());
				BeanUtils.populate(entrsAgnAdjSub, map);
				entrsAgnAdjSubList.add(entrsAgnAdjSub);
			}
			resp=eaas.editEntrsAgnAdj(entrsAgnAdj, entrsAgnAdjSubList);
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//删除委托代销调整单信息
	@RequestMapping(value="deleteEntrsAgnAdj",method = RequestMethod.POST)
	@ResponseBody
	private String deleteEntrsAgnAdj(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnAdj/deleteEntrsAgnAdj");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=eaas.deleteEntrsAgnAdj(BaseJson.getReqBody(jsonBody).get("adjSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//查询委托代销调整单信息
	@RequestMapping(value="queryEntrsAgnAdj",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnAdj(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnAdj/queryEntrsAgnAdj");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=eaas.queryEntrsAgnAdj(BaseJson.getReqBody(jsonBody).get("adjSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryEntrsAgnAdjList",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnAdjList(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnAdj/queryEntrsAgnAdjList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String adjSnglDt1 = (String) map.get("adjSnglDt1");
			String adjSnglDt2 = (String) map.get("adjSnglDt2");
			if (adjSnglDt1!=null && adjSnglDt1!="") {
				adjSnglDt1 = adjSnglDt1 + " 00:00:00";
			}
			if (adjSnglDt2!=null && adjSnglDt2!="") {
				adjSnglDt2 = adjSnglDt2 + " 23:59:59";
			}
			map.put("adjSnglDt1", adjSnglDt1);
			map.put("adjSnglDt2", adjSnglDt2);
			resp=eaas.queryEntrsAgnAdjList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}

	//批量委托代销调整单信息删除
	@RequestMapping(value="deleteEntrsAgnAdjList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteEntrsAgnAdjList(@RequestBody String jsonBody) throws IOException {
			
		logger.info("url:purc/EntrsAgnAdj/deleteEntrsAgnAdjList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=eaas.deleteEntrsAgnAdjList(BaseJson.getReqBody(jsonBody).get("adjSnglId").asText());
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//打印及输入输出查询全部客户档案
	@RequestMapping("printingEntrsAgnAdjList")
	@ResponseBody
	public Object printingEntrsAgnAdjList(@RequestBody String jsonBody){
		
		logger.info("url:purc/EntrsAgnAdj/printingEntrsAgnAdjList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String adjSnglDt1 = (String) map.get("adjSnglDt1");
			String adjSnglDt2 = (String) map.get("adjSnglDt2");
			if (adjSnglDt1!=null && adjSnglDt1!="") {
				adjSnglDt1 = adjSnglDt1 + " 00:00:00";
			}
			if (adjSnglDt2!=null && adjSnglDt2!="") {
				adjSnglDt2 = adjSnglDt2 + " 23:59:59";
			}
			map.put("adjSnglDt1", adjSnglDt1);
			map.put("adjSnglDt2", adjSnglDt2);
			resp=eaas.printingEntrsAgnAdjList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//委托代销调整单页面查询接口
	@RequestMapping("queryEntrsAgnAdjOrEntrsAgn")
	@ResponseBody
	public Object queryEntrsAgnAdjOrEntrsAgn(@RequestBody String jsonBody){
		
		logger.info("url:purc/EntrsAgnAdj/queryEntrsAgnAdjOrEntrsAgn");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			/*JSONObject jsonObject = JSON.parseObject(jsonBody);
			JSONObject jsonObjectList = jsonObject.getJSONObject("reqBody");
			Map map=JacksonUtil.getMap(jsonObjectList.toString());*/
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=eaas.queryEntrsAgnAdjOrEntrsAgn(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
}
