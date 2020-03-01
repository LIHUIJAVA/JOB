package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
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

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.EntrsAgnDelvService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*委托代销发货单功能描述*/
@RequestMapping(value="purc/EntrsAgnDelv")
@Controller
public class EntrsAgnDelvController {
	
	private Logger logger = LoggerFactory.getLogger(EntrsAgnDelvController.class);
	
	@Autowired
	private EntrsAgnDelvService eads;
    //记账
	@Autowired
	FormBookService formBookService;
	
	//新增委托代销发货单信息
	@RequestMapping(value="addEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String addEntrsAgnDelv(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/EntrsAgnDelv/addEntrsAgnDelv");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			EntrsAgnDelv entrsAgnDelv = BaseJson.getPOJO(jsonBody, EntrsAgnDelv.class);
			entrsAgnDelv.setSetupPers(userName);
			List<EntrsAgnDelvSub> entrsAgnDelvSubList=new ArrayList<>();
			entrsAgnDelvSubList=BaseJson.getPOJOList(jsonBody, EntrsAgnDelvSub.class);
			resp=eads.addEntrsAgnDelv(userId,entrsAgnDelv, entrsAgnDelvSubList,loginTime);
		} catch (IOException e) {
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/EntrsAgnDelv/addEntrsAgnDelv", false, e.getMessage(), null);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//修改委托代销发货单信息
	@RequestMapping(value="editEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String editEntrsAgnDelv(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/EntrsAgnDelv/editEntrsAgnDelv");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			EntrsAgnDelv entrsAgnDelv = BaseJson.getPOJO(jsonBody, EntrsAgnDelv.class);
			entrsAgnDelv.setMdfr(userName);
			List<EntrsAgnDelvSub> entrsAgnDelvSubList=new ArrayList<>();
			entrsAgnDelvSubList=BaseJson.getPOJOList(jsonBody, EntrsAgnDelvSub.class);
			resp=eads.editEntrsAgnDelv(entrsAgnDelv, entrsAgnDelvSubList);
		} catch (IOException e) {
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/EntrsAgnDelv/editEntrsAgnDelv", false, e.getMessage(), null);
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//删除采购订单信息
	@RequestMapping(value="deleteEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String deleteEntrsAgnDelv(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/deleteEntrsAgnDelv");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=eads.deleteEntrsAgnDelv(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//查询委托代销发货单详细信息
	@RequestMapping(value="queryEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnDelv(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelv");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=eads.queryEntrsAgnDelv(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//查询所有委托代销发货单信息
	@RequestMapping(value="queryEntrsAgnDelvList",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnDelvList(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelvList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String delvSnglDt1 = (String) map.get("delvSnglDt1");
			String delvSnglDt2 = (String) map.get("delvSnglDt2");
			if (delvSnglDt1!=null && delvSnglDt1!="") {
				delvSnglDt1 = delvSnglDt1 + " 00:00:00";
			}
			if (delvSnglDt2!=null && delvSnglDt2!="") {
				delvSnglDt2 = delvSnglDt2 + " 23:59:59";
			}
			map.put("delvSnglDt1", delvSnglDt1);
			map.put("delvSnglDt2", delvSnglDt2);
			resp=eads.queryEntrsAgnDelvList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	

	//批量委托代销发货单信息删除
	@RequestMapping(value="deleteEntrsAgnDelvList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteEntrsAgnDelvList(@RequestBody String jsonBody) throws IOException {
			
		logger.info("url:purc/EntrsAgnDelv/deleteEntrsAgnDelvList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		resp=eads.deleteEntrsAgnDelvList(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
	    logger.info("返回参数："+resp);
		return resp;
	}
	
	//批量更新销售单审核状态
	@RequestMapping(value="updateEntrsAgnDelvIsNtChkList",method = RequestMethod.POST)
	@ResponseBody
	public Object updateEntrsAgnDelvIsNtChkList(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		Map<String,Object> result = new HashMap<>();
		boolean isSuccess = false;
		List<EntrsAgnDelv> entrsAgnDelvList=new ArrayList();
		try
		{
			String userId=BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String userName=BaseJson.getReqHead(jsonBody).get("userName").asText();
			String loginTime=BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList", false, "当月已封账！", null);
				logger.info("返回参数：" + resp);
				return resp;
			}
		    entrsAgnDelvList = BaseJson.getPOJOList(jsonBody, EntrsAgnDelv.class);
		    for(EntrsAgnDelv entrsAgnDelv :entrsAgnDelvList) {
				try
				{
					entrsAgnDelv.setChkr(userName);
					result=eads.updateEntrsAgnDelvIsNtChkList(userId,entrsAgnDelv,loginTime);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}	
		    resp=BaseJson.returnRespObj("/purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList", isSuccess, resp, null);
		    logger.info("返回参数："+resp);
		}
		catch (Exception ex) {
			logger.info("返回参数："+ex.getMessage());
			resp=BaseJson.returnRespObj("/purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList", false, ex.getMessage(), null);
		}
		return resp;
	}
	
	//打印及输入输出查询全部客户档案
	@RequestMapping("printingEntrsAgnDelvList")
	@ResponseBody
	public Object printingEntrsAgnDelvList(@RequestBody String jsonBody){
		
		logger.info("url:purc/EntrsAgnDelv/printingEntrsAgnDelvList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String delvSnglDt1 = (String) map.get("delvSnglDt1");
			String delvSnglDt2 = (String) map.get("delvSnglDt2");
			if (delvSnglDt1!=null && delvSnglDt1!="") {
				delvSnglDt1 = delvSnglDt1 + " 00:00:00";
			}
			if (delvSnglDt2!=null && delvSnglDt2!="") {
				delvSnglDt2 = delvSnglDt2 + " 23:59:59";
			}
			map.put("delvSnglDt1", delvSnglDt1);
			map.put("delvSnglDt2", delvSnglDt2);
			resp=eads.printingEntrsAgnDelvList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//导入委托代销发货单
	@RequestMapping("uploadEntrsAgnDelvFile")
	@ResponseBody
	public Object uploadEntrsAgnDelvFile(HttpServletRequest request) throws IOException{
		try {
			//创建一个通用的多部分解析器
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //判断 request 是否有文件上传,即多部分请求
		    if(multipartResolver.isMultipart(request)) {
		        //转换成多部分request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return ("请选择文件。");
			    }
			    return eads.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/EntrsAgnDelv/uploadEntrsAgnDelvFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/EntrsAgnDelv/uploadEntrsAgnDelvFile", false, e.getMessage(), null);
		}
	}
	
	//参照时查询所有委托代销发货单主表信息
	@RequestMapping(value="queryEntrsAgnDelvLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnDelvLists(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelvLists");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String delvSnglDt1 = (String) map.get("delvSnglDt1");
			String delvSnglDt2 = (String) map.get("delvSnglDt2");
			if (delvSnglDt1!=null && delvSnglDt1!="") {
				delvSnglDt1 = delvSnglDt1 + " 00:00:00";
			}
			if (delvSnglDt2!=null && delvSnglDt2!="") {
				delvSnglDt2 = delvSnglDt2 + " 23:59:59";
			}
			map.put("delvSnglDt1", delvSnglDt1);
			map.put("delvSnglDt2", delvSnglDt2);
			resp=eads.queryEntrsAgnDelvLists(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//到货拒收单参照到货单时点击主表信息查询多个未拒收子表明细
	@RequestMapping("selectEntDeSubUnBllgRtnGoodsQty")
	@ResponseBody
	public Object selectEntDeSubUnBllgRtnGoodsQty(@RequestBody String jsonBody){
		logger.info("url:purc/EntrsAgnDelv/selectEntDeSubUnBllgRtnGoodsQty");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=eads.selectEntDeSubUnBllgRtnGoodsQty(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	//分页+排序查询所有委托代销发货单信息
			@RequestMapping(value="queryEntrsAgnDelvListOrderBy",method = RequestMethod.POST)
			@ResponseBody
			private String queryEntrsAgnDelvListOrderBy(@RequestBody String jsonBody) {
				logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelvListOrderBy");
				logger.info("请求参数："+jsonBody);
				String resp="";
				try {
					Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
					int pageNo=(int)map.get("pageNo");
					int pageSize=(int)map.get("pageSize");
					map.put("index", (pageNo-1)*pageSize);
					map.put("num", pageSize);
					String delvSnglDt1 = (String) map.get("delvSnglDt1");
					String delvSnglDt2 = (String) map.get("delvSnglDt2");
					if (delvSnglDt1!=null && delvSnglDt1!="") {
						delvSnglDt1 = delvSnglDt1 + " 00:00:00";
					}
					if (delvSnglDt2!=null && delvSnglDt2!="") {
						delvSnglDt2 = delvSnglDt2 + " 23:59:59";
					}
					map.put("delvSnglDt1", delvSnglDt1);
					map.put("delvSnglDt2", delvSnglDt2);
					resp=eads.queryEntrsAgnDelvListOrderBy(map);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} 
				logger.info("返回参数："+resp);
				return resp;
			}
	
}
