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
/*��Ӧ�̵�����������*/
@RequestMapping(value="purc/ProvrDoc")
@Controller
public class ProvrDocController {
	
	@Autowired
	private ProvrDocService pds;
	
	private Logger logger = LoggerFactory.getLogger(ProvrDocController.class);
	
	/*������Ӧ�̵���*/
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
	
	/*�޸Ĺ�Ӧ�̵���*/
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
	
	/*ɾ����Ӧ�̵���*/
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
	
	/*���չ�Ӧ�̱�Ų�ѯ��Ӧ�̵���*/
	@RequestMapping("selectProvrDocByProvrId")
	@ResponseBody
	public Object ProvrDocByProvrId(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String provrId = reqBody.get("provrId").asText();
		ProvrCls provrCls=pds.selectProvrDocByProvrId(provrId);
		if(provrCls!=null) {
			 resp=BaseJson.returnRespObj("purc/ProvrDoc/selectProvrDocByProvrId",true,"����ɹ���", provrCls);
		}else {
			 resp=BaseJson.returnRespObj("purc/ProvrDoc/selectProvrDocByProvrId",false,"����ʧ�ܣ�", provrCls);
		}
		return resp;
	}
	
	//��ѯ���й�Ӧ�̵�����Ϣ
	@RequestMapping("selectProvrDocList")
	@ResponseBody
	public Object selectProvrDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/ProvrDoc/selectProvrDocList");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
		
	}
	
	//��ӡ�����������ѯȫ���������
	@RequestMapping("printingProvrDocList")
	@ResponseBody
	public Object printingProvrDocList(@RequestBody String jsonBody){
		
		logger.info("url:purc/ProvrDoc/printingProvrDocList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=pds.printingProvrDocList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//���빩Ӧ�̵���
	@RequestMapping("uploadProvrDocFile")
	@ResponseBody
	public Object uploadProvrDocFile(HttpServletRequest request) throws IOException{
		try {
			//����һ��ͨ�õĶಿ�ֽ�����
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
		    if(multipartResolver.isMultipart(request)) {
		        //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return ("��ѡ���ļ���");
			    }
			    return pds.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/ProvrDoc/uploadProvrDocFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/ProvrDoc/uploadProvrDocFile", false, e.getMessage(), null);
		}
	}
	
}
