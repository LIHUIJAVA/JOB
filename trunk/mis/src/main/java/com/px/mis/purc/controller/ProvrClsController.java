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
import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.service.ProvrClsService;
import com.px.mis.util.BaseJson;
/*供应商分类功能描述*/
@RequestMapping(value="purc/ProvrCls")
@Controller
public class ProvrClsController {
	
	@Autowired
	private ProvrClsService pcs;
	
	/*新增供应商分类*/
	@RequestMapping("insertProvrCls")
	public @ResponseBody Object insertProvrCls(@RequestBody String jsonData) {
	
		ObjectNode on=null;
		String resp="";
		try {
			ProvrCls provrCls=BaseJson.getPOJO(jsonData,ProvrCls.class);
			on=pcs.insertProvrCls(provrCls);
			resp =BaseJson.returnRespObj("purc/ProvrCls/insertProvrCls", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*修改供应商档案*/
	@RequestMapping("updateProvrClsByProvrClsId")
	public @ResponseBody Object updateProvrClsByProvrClsId(@RequestBody String jsonData) {
	
		ObjectNode on=null;
		String resp="";
		try {
			ProvrCls provrCls=BaseJson.getPOJO(jsonData,ProvrCls.class);
			on=pcs.updateProvrClsByProvrClsId(provrCls);
			resp =BaseJson.returnRespObj("purc/ProvrCls/updateProvrClsByProvrClsId", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除供应商档案*/
	@RequestMapping("deleteProvrClsByProvrClsId")
	public @ResponseBody Object deleteProvrClsByProvrClsId(@RequestBody String jsonData) {
		
		ObjectNode on=null;
		String resp="";
		try {
			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
			String provrClsId = reqBody.get("provrClsId").asText();
			on=pcs.deleteProvrClsByProvrClsId(provrClsId);
			resp =BaseJson.returnRespObj("purc/ProvrCls/deleteProvrClsByProvrClsId", on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照供应商分类编号查询所有供应商分类*/
	@RequestMapping("selectProvrClsByProvrClsId")
	@ResponseBody
	public Object selectProvrClsByProvrClsId(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String provrClsId = reqBody.get("provrClsId").asText();
		ProvrCls provrCls=pcs.selectProvrClsByProvrClsId(provrClsId);
		if(provrCls!=null) {
			 resp=BaseJson.returnRespObj("purc/ProvrCls/selectProvrClsByProvrClsId",true,"处理成功！", provrCls);
		}else {
			 resp=BaseJson.returnRespObj("purc/ProvrCls/selectProvrClsByProvrClsId",false,"处理失败！", provrCls);
		}
		return resp;
	}
	
	/*查询所有供应商分类*/
	@RequestMapping("selectProvrCls")
	@ResponseBody
	public Object selectProvrCls(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		List<Map> provrClsList = pcs.selectProvrCls();
		if(provrClsList!=null) {
			 resp=BaseJson.returnRespObjList("purc/ProvrCls/selectProvrCls",true,"处理成功！", null, provrClsList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/ProvrCls/selectProvrCls",false,"处理失败！", null, provrClsList);
		}
		
		return resp;
	}
	
	//导入供应商分类
	@RequestMapping("uploadProvrClsFile")
	@ResponseBody
	public Object uploadProvrClsFile(HttpServletRequest request){
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
			    return pcs.uploadFileAddDb(file);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			try {
				return BaseJson.returnRespObj("purc/ProvrCls/uploadProvrClsFile", false, "导入文件格式有误，无法导入！", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
        return null;
	}
	

}
