package com.px.mis.ec.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.service.GoodRecordService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//商品档案
@RequestMapping(value="ec/goodRecord",method = RequestMethod.POST)
@Controller
public class GoodRecordController {
	
	private Logger logger = LoggerFactory.getLogger(GoodRecordController.class);
	
	@Autowired
	private GoodRecordService goodRecordService;
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	private String add(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/add");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			GoodRecord goodRecord = BaseJson.getPOJO(jsonBody, GoodRecord.class);
			resp=goodRecordService.add(goodRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/edit");
		logger.info("请求参数："+jsonBody);
		String resp="";
		System.err.println(jsonBody);
		try {
			GoodRecord goodRecord = BaseJson.getPOJO(jsonBody, GoodRecord.class);
			resp=goodRecordService.edit(goodRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/delete");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=goodRecordService.delete(BaseJson.getReqBody(jsonBody).get("id").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/query");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			resp=goodRecordService.query(BaseJson.getReqBody(jsonBody).get("id").asInt());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/queryList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=goodRecordService.queryList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
/**
 * 导出
 * @param jsonBody
 * @return
 */
	@RequestMapping(value="exportList",method = RequestMethod.POST)
	@ResponseBody
	private String exportList(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/exportList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=goodRecordService.exportList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	
	@RequestMapping(value="download",method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/download");
		logger.info("请求参数："+jsonBody);
		String resp = "";
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String storeId = BaseJson.getReqBody(jsonBody).get("storeId").asText();
			resp = goodRecordService.download(accNum,storeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("返回参数：" + resp);
		return resp;
	}
	
	//导入店铺商品档案
	@RequestMapping("uploadGoodRecord")
	@ResponseBody
	public Object uploadGoodRecord(HttpServletRequest request){
		//创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        //System.out.println("开始导入数据。。。");
        try {
			if(multipartResolver.isMultipart(request)) {
			    //转换成多部分request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//request强制转换注意
			  	MultipartFile file = mRequest.getFile("file");
			  	String userID = mRequest.getParameter("accNum");
			    if(file == null) {
			    	return BaseJson.returnRespObj("ec/goodRecord/uploadGoodRecord", false,"请选择文件", null);
			    }else {
			    	 return goodRecordService.uploadFile(file,userID);
			    }
			}else {
				return BaseJson.returnRespObj("ec/goodRecord/uploadGoodRecord", false,"请选择文件", null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
        
	}
}
