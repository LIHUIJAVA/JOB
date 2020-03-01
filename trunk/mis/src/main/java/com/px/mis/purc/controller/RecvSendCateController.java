package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.entity.RecvSendCate;
import com.px.mis.purc.service.RecvSendCateService;
import com.px.mis.util.BaseJson;
/*收发类型档案功能描述*/
@RequestMapping(value="purc/RecvSendCate")
@Controller
public class RecvSendCateController {
	
	@Autowired
	private RecvSendCateService rscs;
	
	/*新增收发类型档案*/
	@RequestMapping("insertRecvSendCate")
	public @ResponseBody Object insertRecvSendCate(@RequestBody String jsonData) {
	
		ObjectNode on=null;
		String resp="";
		try {
			RecvSendCate recvSendCate=BaseJson.getPOJO(jsonData,RecvSendCate.class);
			on=rscs.insertRecvSendCate(recvSendCate);
			resp =BaseJson.returnRespObj("purc/RecvSendCate/insertRecvSendCate", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*修改收发类型档案*/
	@RequestMapping("updateRecvSendCateByRecvSendCateId")
	public @ResponseBody Object updateRecvSendCateByRecvSendCateId(@RequestBody String jsonData) {
	
		ObjectNode on=null;
		String resp="";
		try {
			RecvSendCate recvSendCate=BaseJson.getPOJO(jsonData,RecvSendCate.class);
			on=rscs.updateRecvSendCateByRecvSendCateId(recvSendCate);
			resp =BaseJson.returnRespObj("purc/RecvSendCate/updateRecvSendCateByRecvSendCateId", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除收发类型档案*/
	@RequestMapping("deleteRecvSendCateByRecvSendCateId")
	public @ResponseBody Object deleteRecvSendCateByRecvSendCateId(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String recvSendCateId = reqBody.get("recvSendCateId").asText();
			on=rscs.deleteRecvSendCateByRecvSendCateId(recvSendCateId);
			resp =BaseJson.returnRespObj("purc/RecvSendCate/deleteRecvSendCateByRecvSendCateId", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照编号查询收发类型档案*/
	@RequestMapping("selectRecvSendCateByRecvSendCateId")
	@ResponseBody
	public Object selectRecvSendCateByRecvSendCateId(@RequestBody String jsonData) throws IOException {
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String recvSendCateId = reqBody.get("recvSendCateId").asText();
		RecvSendCate recvSendCate=rscs.selectRecvSendCateByRecvSendCateId(recvSendCateId);
		if(recvSendCate!=null) {
			 resp=BaseJson.returnRespObj("purc/RecvSendCate/selectRecvSendCateByRecvSendCateId",true,"处理成功！", recvSendCate);
		}else {
			 resp=BaseJson.returnRespObj("purc/RecvSendCate/selectRecvSendCateByRecvSendCateId",false,"处理失败！", recvSendCate);
		}
		return resp;
	}
	
	/*查询所有收发类型档案*/
	@RequestMapping("selectRecvSendCate")
	@ResponseBody
	public Object selectRecvSendCate(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		List<Map> recvSendCateList = rscs.selectRecvSendCate();
		if(recvSendCateList!=null) {
			 resp=BaseJson.returnRespObjList("purc/RecvSendCate/selectRecvSendCate",true,"处理成功！", null, recvSendCateList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/RecvSendCate/selectRecvSendCate",false,"处理失败！", null, recvSendCateList);
		}
		
		return resp;
	}
	
	//导入到货单
	@RequestMapping("uploadRecvSendCateFile")
	@ResponseBody
	public Object uploadRecvSendCateFile(HttpServletRequest request) throws IOException{
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
			    return rscs.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/RecvSendCate/uploadRecvSendCateFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/RecvSendCate/uploadRecvSendCateFile", false, e.getMessage(), null);
		}
	}

}
