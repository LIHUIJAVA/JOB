package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.service.InvtyDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*存货档案功能描述*/
@RequestMapping(value="purc/InvtyDoc")
@Controller
public class InvtyDocController {
	
	@Autowired
	private InvtyDocService ids;
	
	private Logger logger = LoggerFactory.getLogger(InvtyDocController.class);
	
	@RequestMapping("insertInvtyDoc")
	public @ResponseBody String insertInvtyDoc(@RequestBody String jsonData) {
		String resp="";
		logger.info("url:purc/InvtyDoc/insertInvtyDoc");
		logger.info("请求参数："+jsonData);
		try {
			String userName=BaseJson.getReqHead(jsonData).get("userName").asText();
			InvtyDoc invtyDoc=BaseJson.getPOJO(jsonData,InvtyDoc.class);
			invtyDoc.setSetupPers(userName);
			resp=ids.insertInvtyDoc(invtyDoc);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	@RequestMapping("updateInvtyDocByInvtyDocEncd")
	public @ResponseBody Object updateInvtyDocByInvtyDocEncd(@RequestBody String jsonData) {
		ObjectNode on=null;
		String resp="";
		try {
			String userName=BaseJson.getReqHead(jsonData).get("userName").asText();
			InvtyDoc invtyDoc=BaseJson.getPOJO(jsonData,InvtyDoc.class);
			invtyDoc.setMdfr(userName);
			on=ids.updateInvtyDocByInvtyDocEncd(invtyDoc);
			resp =BaseJson.returnRespObj("purc/InvtyDoc/updateInvtyDocByInvtyDocEncd", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	/*删除存货档案*/
	@RequestMapping("deleteInvtyDocList")
	public @ResponseBody String deleteInvtyDocList(@RequestBody String jsonBody) {
		String resp="";
		try {
			resp=ids.deleteInvtyDocList(BaseJson.getReqBody(jsonBody).get("invtyEncd").asText());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照id 查询存货档案*/
	@RequestMapping("selectInvtyDocByInvtyDocEncd")
	@ResponseBody
	public Object selectselectInvtyClsByInvtyClsEncd(@RequestBody String jsonData) throws IOException {
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String invtyEncd = reqBody.get("invtyEncd").asText();
		InvtyDoc invtyDoc=ids.selectInvtyDocByInvtyDocEncd(invtyEncd);
		if(invtyDoc!=null) {
			 resp=BaseJson.returnRespObj("purc/InvtyDoc/selectInvtyDocByInvtyDocEncd",true,"处理成功！", invtyDoc);
		}else {
			 resp=BaseJson.returnRespObj("purc/InvtyDoc/selectInvtyDocByInvtyDocEncd",false,"该存货不存在！", invtyDoc);
		}
		return resp;
	}
	
	//查询全部存货档案
	@RequestMapping("selectInvtyDocList")
	@ResponseBody
	public Object selectInvtyDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/InvtyDoc/selectInvtyDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=ids.selectInvtyDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//打印及输入输出查询全部存货档案
	@RequestMapping("printingInvtyDocList")
	@ResponseBody
	public Object printingInvtyDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/InvtyDoc/printingInvtyDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=ids.printingInvtyDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//导入存货档案
	@RequestMapping("uploadInvtyDocFile")
	@ResponseBody
	public Object uploadInvtyDocFile(HttpServletRequest request) throws IOException{
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
			    return ids.uploadFileAddDb(file,0);
		    }else {
		    	return BaseJson.returnRespObj("purc/InvtyDoc/uploadInvtyDocFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/InvtyDoc/uploadInvtyDocFile", false, e.getMessage(), null);
		}
	}
	//导入存货档案
	@RequestMapping("uploadInvtyDocFileU8")
	@ResponseBody
	public Object uploadInvtyDocFileU8(HttpServletRequest request) throws IOException{
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
			    return ids.uploadFileAddDb(file,1);
		    }else {
		    	return BaseJson.returnRespObj("purc/InvtyDoc/uploadInvtyDocFileU8", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/InvtyDoc/uploadInvtyDocFileU8", false, e.getMessage(), null);
		}
	}

	//打印及输入输出查询全部存货档案
	@RequestMapping("selectInvtyEncdLike")
	@ResponseBody
	public Object selectInvtyEncdLike(@RequestBody String jsonBody){
		
		logger.info("url:purc/InvtyDoc/selectInvtyEncdLike");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=ids.selectInvtyEncdLike(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
}
