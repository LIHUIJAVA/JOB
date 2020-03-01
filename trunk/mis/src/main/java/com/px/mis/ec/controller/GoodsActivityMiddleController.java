package com.px.mis.ec.controller;

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
import com.px.mis.ec.entity.GoodsActivityMiddle;
import com.px.mis.ec.service.GoodsActivityMiddleService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//商品活动中间表接口
@Controller
@RequestMapping(value="ec/goodsActivityMiddle",method=RequestMethod.POST)
public class GoodsActivityMiddleController {
	private Logger logger = LoggerFactory.getLogger(GoodsActivityMiddleController.class);
	@Autowired
	private GoodsActivityMiddleService goodsActivityMiddleService;
	/*添加*/
	@RequestMapping("insertGoodsActivityMiddle")
	public @ResponseBody Object insertGoodsActivityMiddle(@RequestBody String jsonData) {
		logger.info("url:/ec/goodsActivityMiddle/insertGoodsActivityMiddle");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		GoodsActivityMiddle goodsActivityMiddle=null;
		try {
			goodsActivityMiddle = BaseJson.getPOJO(jsonData,GoodsActivityMiddle.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/insertGoodsActivityMiddle 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/insertGoodsActivityMiddle", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/goodsActivityMiddle/insertGoodsActivityMiddle 异常说明：",e);
			}
			return resp;
		}
		try {
			on = goodsActivityMiddleService.insertGoodsActivityMiddle(goodsActivityMiddle);
			if(on==null) {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/insertGoodsActivityMiddle", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/insertGoodsActivityMiddle", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/insertGoodsActivityMiddle 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteGoodsActivityMiddle")
	public @ResponseBody Object deleteGoodsActivityMiddle(@RequestBody String jsonData) {
		logger.info("url:/ec/goodsActivityMiddle/deleteGoodsActivityMiddle");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/deleteGoodsActivityMiddle 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/deleteGoodsActivityMiddle", false, "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/goodsActivityMiddle/deleteGoodsActivityMiddle 异常说明：",e);
			}
			return resp;
		}
		try {
			on = goodsActivityMiddleService.deleteGoodsActivityMiddleById(no);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/deleteGoodsActivityMiddle", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/deleteGoodsActivityMiddle", false, "delete error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/deleteGoodsActivityMiddle 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateGoodsActivityMiddle")
	public @ResponseBody Object updateGoodsActivityMiddle(@RequestBody String jsonData) {
		logger.info("url:/ec/goodsActivityMiddle/updateGoodsActivityMiddle");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		GoodsActivityMiddle goodsActivityMiddle=null;
		try {
			goodsActivityMiddle = BaseJson.getPOJO(jsonData,GoodsActivityMiddle.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/updateGoodsActivityMiddle 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/updateGoodsActivityMiddle", false, "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/goodsActivityMiddle/updateGoodsActivityMiddle 异常说明：",e);
			}
			return resp;
		}
		try {
			on = goodsActivityMiddleService.updateGoodsActivityMiddleById(goodsActivityMiddle);
			if(on !=null) {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/updateGoodsActivityMiddle", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/ec/goodsActivityMiddle/updateGoodsActivityMiddle", false, "update error!", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/updateGoodsActivityMiddle 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectGoodsActivityMiddle")
	public @ResponseBody Object selectGoodsActivityMiddle(@RequestBody String jsonBody) {
		logger.info("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddle");
		logger.info("请求参数："+jsonBody);
		String resp="";
		Map map=null;
		Integer pageNo=null;
		Integer pageSize=null;
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			pageNo = (int)map.get("pageNo");
			pageSize = (int)map.get("pageSize");
			if(pageNo==0 || pageSize==0) {
				resp=BaseJson.returnRespList("/ec/goodsActivityMiddle/selectGoodsActivityMiddle", false, "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！",null);
				return resp;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddle 异常说明：",e1);
			try {
				resp=BaseJson.returnRespList("/ec/goodsActivityMiddle/selectGoodsActivityMiddle", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！",null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddle 异常说明：",e);
			}
			return resp;
		}
		try {
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=goodsActivityMiddleService.selectGoodsActivityMiddleList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddle 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectGoodsActivityMiddleById")
	public @ResponseBody Object selectGoodsActivityMiddleById(@RequestBody String jsonData) {
		logger.info("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddleById");
		logger.info("请求参数："+jsonData);
		String resp=""; 
		Integer no=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			no = reqBody.get("no").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddleById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/ec/goodsActivityMiddle/selectGoodsActivityMiddleById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddleById 异常说明：",e);
			}
			return resp;
		}
		try {
			GoodsActivityMiddle goodsActivityMiddle = goodsActivityMiddleService.selectGoodsActivityMiddleById(no);
			if(goodsActivityMiddle!=null) {
				 resp=BaseJson.returnRespObj("/ec/goodsActivityMiddle/selectGoodsActivityMiddleById",true,"处理成功！", goodsActivityMiddle);
			}else {
				 resp=BaseJson.returnRespObj("/ec/goodsActivityMiddle/selectGoodsActivityMiddleById",false,"没有查到数据！", goodsActivityMiddle);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/ec/goodsActivityMiddle/selectGoodsActivityMiddleById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
}
