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
import com.px.mis.purc.entity.ProvrCls;
import com.px.mis.purc.entity.ProvrDoc;
import com.px.mis.purc.service.ProvrDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*供应商档案功能描述*/
@RequestMapping(value="purc/ProvrDoc")
@Controller
public class ProvrDocController {
	
	@Autowired
	private ProvrDocService pds;
	
	private Logger logger = LoggerFactory.getLogger(ProvrDocController.class);
	
	/*新增供应商档案*/
	@RequestMapping("insertProvrDoc")
	public @ResponseBody String insertProvrDoc(@RequestBody String jsonData) {
		String resp="";
		try {
			String userName=BaseJson.getReqHead(jsonData).get("userName").asText();
			ProvrDoc provrDoc=BaseJson.getPOJO(jsonData,ProvrDoc.class);
			provrDoc.setSetupPers(userName);
			resp=pds.insertProvrDoc(provrDoc);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*修改供应商档案*/
	@RequestMapping("updateProvrDocByProvrId")
	public @ResponseBody Object updateProvrDocByProvrId(@RequestBody String jsonData) {
		String resp="";
		try {
			String userName=BaseJson.getReqHead(jsonData).get("userName").asText();
			ProvrDoc provrDoc=BaseJson.getPOJO(jsonData,ProvrDoc.class);
			provrDoc.setSetupPers(userName);
			resp=pds.updateProvrDocByProvrId(provrDoc);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除供应商档案*/
	@RequestMapping("deleteProvrDocList")
	public @ResponseBody String deleteProvrDocList(@RequestBody String jsonBody) {
		String resp="";
		try {
			resp=pds.deleteProvrDocList(BaseJson.getReqBody(jsonBody).get("provrId").asText());
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*按照供应商编号查询供应商档案*/
	@RequestMapping("selectProvrDocByProvrId")
	@ResponseBody
	public Object ProvrDocByProvrId(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String provrId = reqBody.get("provrId").asText();
		ProvrCls provrCls=pds.selectProvrDocByProvrId(provrId);
		if(provrCls!=null) {
			 resp=BaseJson.returnRespObj("purc/ProvrDoc/selectProvrDocByProvrId",true,"处理成功！", provrCls);
		}else {
			 resp=BaseJson.returnRespObj("purc/ProvrDoc/selectProvrDocByProvrId",false,"处理失败！", provrCls);
		}
		return resp;
	}
	
	//查询所有供应商档案信息
	@RequestMapping("selectProvrDocList")
	@ResponseBody
	public Object selectProvrDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/ProvrDoc/selectProvrDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			resp=pds.selectProvrDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
		
	}
	
	//打印及输入输出查询全部存货档案
	@RequestMapping("printingProvrDocList")
	@ResponseBody
	public Object printingProvrDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/ProvrDoc/printingProvrDocList");
		logger.info("请求参数："+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=pds.printingProvrDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("返回参数："+resp);
		return resp;
	}
	
	//导入供应商档案
	@RequestMapping("uploadProvrDocFile")
	@ResponseBody
	public Object uploadProvrDocFile(HttpServletRequest request) throws IOException{
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
			    return pds.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/ProvrDoc/uploadProvrDocFile", false, "请选择文件！", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/ProvrDoc/uploadProvrDocFile", false, e.getMessage(), null);
		}
	}
	
}
