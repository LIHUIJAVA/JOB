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
import com.px.mis.purc.entity.InvtyCls;
import com.px.mis.purc.service.InvtyClsService;
import com.px.mis.util.BaseJson;
/*存货分类功能描述*/
@RequestMapping(value="purc/InvtyCls")
@Controller
public class InvtyClsController {
	
	@Autowired
	private InvtyClsService ics;
	
	/*新增存货分类*/
	@RequestMapping("insertInvtyCls")
	public @ResponseBody Object insertInvtyCls(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			InvtyCls invtyCls=BaseJson.getPOJO(jsonData,InvtyCls.class);
			on=ics.insertInvtyCls(invtyCls);
			resp =BaseJson.returnRespObj("purc/InvtyCls/insertInvtyCls", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*修改存货分类*/
	@RequestMapping("updateInvtyClsByInvtyClsEncd")
	public @ResponseBody Object updateInvtyClsByInvtyClsEncd(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			InvtyCls invtyCls=BaseJson.getPOJO(jsonData,InvtyCls.class);
			on=ics.updateInvtyClsByInvtyClsEncd(invtyCls);
			resp =BaseJson.returnRespObj("purc/InvtyCls/updateInvtyClsByInvtyClsEncd", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除存货分类*/
	@RequestMapping("deleteInvtyClsByInvtyClsEncd")
	public @ResponseBody Object deleteInvtyClsByInvtyClsEncd(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String invtyClsEncd = reqBody.get("invtyClsEncd").asText();
			on=ics.deleteInvtyClsByInvtyClsEncd(invtyClsEncd);
			resp =BaseJson.returnRespObj("purc/InvtyCls/deleteInvtyClsByInvtyClsEncd", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
		
	}
	
	/*简单查询存货分类*/
	@RequestMapping("selectInvtyClsByInvtyClsEncd")
	@ResponseBody
	public Object selectInvtyClsByInvtyClsEncd(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String invtyClsEncd = reqBody.get("invtyClsEncd").asText();
		InvtyCls invtyCls=ics.selectInvtyClsByInvtyClsEncd(invtyClsEncd);
		if(invtyCls!=null) {
			 resp=BaseJson.returnRespObj("purc/InvtyCls/selectInvtyClsByInvtyClsEncd",true,"处理成功！", invtyCls);
		}else {
			 resp=BaseJson.returnRespObj("purc/InvtyCls/selectInvtyClsByInvtyClsEncd",false,"处理失败！", invtyCls);
		}
		return resp;
	}
	
	/*查询所有存货分类*/
	@RequestMapping("selectInvtyCls")
	@ResponseBody
	public Object selectInvtyCls(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		List<Map> invtyClsList = ics.selectInvtyCls();
		if(invtyClsList!=null) {
			 resp=BaseJson.returnRespObjList("purc/InvtyCls/selectInvtyCls",true,"处理成功！", null, invtyClsList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/InvtyCls/selectInvtyCls",false,"处理失败！", null, invtyClsList);
		}
		return resp;
	}
	
	//导入存货分类
	@RequestMapping("uploadInvtyClsFile")
	@ResponseBody
	public Object uploadInvtyClsFile(HttpServletRequest request){
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
			    
			    return ics.uploadFileAddDb(file);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			try {
				return BaseJson.returnRespObj("purc/InvtyCls/uploadInvtyClsFile", false, "导入文件格式有误，无法导入！", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
        return null;
	}

}
