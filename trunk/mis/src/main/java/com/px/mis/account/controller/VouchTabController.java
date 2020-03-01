package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.VouchTab;
import com.px.mis.account.entity.VouchTabSub;
import com.px.mis.account.service.VouchTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//凭证表接口
@RequestMapping(value="account/vouchTab",method=RequestMethod.POST)
@Controller
public class VouchTabController {
	private Logger logger = LoggerFactory.getLogger(VouchTabController.class);
	@Autowired
	private VouchTabService vouchTabService;
	/*添加*/
	@RequestMapping("insertVouchTab")
	public @ResponseBody Object insertVouchTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/insertVouchTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchTab vouchTab=null;
		try {
			vouchTab = BaseJson.getPOJO(jsonData,VouchTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/insertVouchTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchTab/insertVouchTab", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/insertVouchTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = vouchTabService.insertVouchTab(vouchTab);
			if(on==null) {
				resp =BaseJson.returnRespObj("/account/vouchTab/insertVouchTab", false, "添加异常了", null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchTab/insertVouchTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/insertVouchTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*删除*/
	@RequestMapping("deleteVouchTab")
	public @ResponseBody Object deleteVouchTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/deleteVouchTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/deleteVouchTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchTab/deleteVouchTab", false, "请求参数解析错误，请检查请求参数是否书写正确，删除出错了", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/deleteVouchTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = vouchTabService.deleteVouchTabByOrdrNum(ordrNum);
			if(on != null) {
				resp =BaseJson.returnRespObj("/account/vouchTab/deleteVouchTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchTab/deleteVouchTab", false, "删除出错了", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/deleteVouchTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*更新*/
	@RequestMapping("updateVouchTab")
	public @ResponseBody Object updateVouchTab(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/updateVouchTab");
		logger.info("请求参数："+jsonData);
		ObjectNode on=null;
		String resp="";
		VouchTab vouchTab=null;
		try {
			vouchTab = BaseJson.getPOJO(jsonData,VouchTab.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/updateVouchTab 异常说明：",e1);
			try {
				resp =BaseJson.returnRespObj("/account/vouchTab/updateVouchTab", false, "请求参数解析错误，请检查请求参数是否书写正确，更新失败", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/updateVouchTab 异常说明：",e);
			}
			return resp;
		}
		try {
			on = vouchTabService.updateVouchTabByOrdrNum(vouchTab);
			if(on != null) {
				resp =BaseJson.returnRespObj("/account/vouchTab/updateVouchTab", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
			}else {
				resp =BaseJson.returnRespObj("/account/vouchTab/updateVouchTab", false, "更新失败", null);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/updateVouchTab 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询所有*/
	@RequestMapping(value="selectVouchTab")
	public @ResponseBody Object selectVouchTab(@RequestBody String jsonBody) {
		logger.info("url:/account/vouchTab/selectVouchTab");
		logger.info("请求参数："+jsonBody);

		String resp="";
		try {
			Map map= JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String start = (String)map.get("start");
			String end = (String)map.get("end");
			
			if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
				if (start.length() < 12 ) {
					start = start + " 00:00:00";
				}
				if (end.length() < 12) {
					end = end + " 23:59:59";
				}
				map.put("start", start);
				map.put("end", end);
			}
			resp=vouchTabService.selectVouchTabList(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/selectVouchTab 异常说明：",e);
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/*查询单个*/
	@RequestMapping(value="selectVouchTabById")
	public @ResponseBody Object selectVouchTabById(@RequestBody String jsonData) {
		logger.info("url:/account/vouchTab/selectVouchTabById");
		logger.info("请求参数："+jsonData);

		String resp=""; 
		Integer ordrNum=null;
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			ordrNum = reqBody.get("ordrNum").asInt();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error("url:/account/vouchTab/selectVouchTabById 异常说明：",e1);
			try {
				resp=BaseJson.returnRespObj("/account/vouchTab/selectVouchTabById",false,"请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("url:/account/vouchTab/selectVouchTabById 异常说明：",e);
			}
			return resp;
		}
		try {
			VouchTab vouchTab = vouchTabService.selectVouchTabByOrdrNum(ordrNum);
			if(vouchTab!=null) {
				 resp=BaseJson.returnRespObj("/account/vouchTab/selectVouchTabById",true,"处理成功！", vouchTab);
			}else {
				 resp=BaseJson.returnRespObj("/account/vouchTab/selectVouchTabById",false,"没有查到数据！", vouchTab);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/selectVouchTabById 异常说明：",e);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	/**
	 * 生成凭证
	 */
	@RequestMapping(value = "/voucher/generate")
	private @ResponseBody String VoucherGenerate(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/generate");
		logger.info("请求参数："+jsonBody);
		
		String resp="";
		
		try {
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //当前用户
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //当前用户
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //登录时间
			ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
			ArrayNode platOrdersArray = (ArrayNode) reqBody.get("list");
			List<VouchTabSub> paltOrdersList = new ArrayList<>();
			for(Iterator<JsonNode> platOrdersArrayIterator = platOrdersArray.iterator(); platOrdersArrayIterator.hasNext();) {
				JsonNode platOrdersNode = platOrdersArrayIterator.next();
				VouchTabSub platOrders=JacksonUtil.getPOJO((ObjectNode)platOrdersNode, VouchTabSub.class);
				paltOrdersList.add(platOrders);
			}
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String vouchCateWor = (String)map.get("vouchCateWor"); //凭证类别字
			String comnVouchComnt = (String)map.get("comnVouchComnt"); //摘要
			if(StringUtils.isNotEmpty(vouchCateWor) || paltOrdersList.size() > 0) {
				map.put("vouchCateWor", vouchCateWor);
				map.put("comnVouchComnt", comnVouchComnt);
				map.put("paltOrdersList", paltOrdersList);
				map.put("userName", userName);
				map.put("loginTime", loginTime);
				map.put("accNum", accNum);
				resp = vouchTabService.voucherGenerate(map);
			} else {
				resp = BaseJson.returnRespObj("account/vouchTab/voucher/generate", false, "接口解析异常", null);
			}
			
			
		} catch (Exception e) {
			try {
				resp = BaseJson.returnRespObj("account/vouchTab/voucher/generate", false, "接口解析异常", null);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("url:account/vouchTab/voucher/generate 异常说明：",e);
		}

		return resp;
	}
	
	/**
	 * 查询凭证-联查
	 */
	@RequestMapping(value = "/voucher/formlist")
	private @ResponseBody String VoucherList(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/formlist");
		logger.info("请求参数："+jsonBody);
		
		String resp="";
		
		try {
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			
			resp = vouchTabService.selectvoucherList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/voucher/formlist 异常说明：",e);
		}

		return resp;
	}
	/**
	 * 导出凭证
	 */
	@RequestMapping(value = "/voucher/export")
	private @ResponseBody String VoucherExport(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/export");
		logger.info("请求参数："+jsonBody);
		
		String resp="";
		
		try {
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //登录时间
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //登录人
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //当前用户
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			String start = (String)map.get("start");
			String end = (String)map.get("end");
			
			if(StringUtils.isNotEmpty(start) && StringUtils.isNotEmpty(end)) {
				if (start.length() < 12 ) {
					start = start + " 00:00:00";
				}
				if (end.length() < 12) {
					end = end + " 23:59:59";
				}
				map.put("start", start);
				map.put("end", end);
			}
			
			map.put("user", userName);
			map.put("loginTime", loginTime);
			resp = vouchTabService.exportvoucherList(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/voucher/export 异常说明：",e);
		}

		return resp;
	}
	/**
	 * 导入凭证
	 */
	@RequestMapping(value = "/voucher/import")
	private @ResponseBody String VoucherImport(HttpServletRequest request,String jsonBody) {

		logger.info("url:account/vouchTab/voucher/import");
		String resp="";
		
		try {
				
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //登录人
			//创建一个通用的多部分解析器
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //判断 request 是否有文件上传,即多部分请求
		    if(multipartResolver.isMultipart(request)) {
		        //转换成多部分request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return  ("请选择文件!");
			    }
			    return vouchTabService.importVoucherList(file,accNum);
		    }else {
		    	return  BaseJson.returnRespObj("url:/account/vouchTab/voucher/import", false, "请选择文件！", null);
		    }
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/vouchTab/voucher/import 异常说明：",e);
		}

		return resp;
	}
	/**
	 * 删除查询
	 */
	@RequestMapping(value = "/voucher/del")
	private @ResponseBody String VoucherDel(@RequestBody String jsonBody) {

		logger.info("url:account/vouchTab/voucher/del");
		logger.info("请求参数："+jsonBody);
		
		String resp="";
		
		try {
			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText(); //当前用户
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText(); //当前用户
			String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText(); //登录时间
			
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());	
			map.put("userName", userName);
			map.put("loginTime", loginTime);
			map.put("accNum", accNum);
			resp = vouchTabService.voucherDel(map);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("url:/account/invty/voucher/list 异常说明：",e);
		}

		return resp;
	}
}
