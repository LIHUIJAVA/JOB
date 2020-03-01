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
/*��Ӧ�̷��๦������*/
@RequestMapping(value="purc/ProvrCls")
@Controller
public class ProvrClsController {
	
	@Autowired
	private ProvrClsService pcs;
	
	/*������Ӧ�̷���*/
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
	
	/*�޸Ĺ�Ӧ�̵���*/
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
	
	/*ɾ����Ӧ�̵���*/
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
	
	/*���չ�Ӧ�̷����Ų�ѯ���й�Ӧ�̷���*/
	@RequestMapping("selectProvrClsByProvrClsId")
	@ResponseBody
	public Object selectProvrClsByProvrClsId(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String provrClsId = reqBody.get("provrClsId").asText();
		ProvrCls provrCls=pcs.selectProvrClsByProvrClsId(provrClsId);
		if(provrCls!=null) {
			 resp=BaseJson.returnRespObj("purc/ProvrCls/selectProvrClsByProvrClsId",true,"����ɹ���", provrCls);
		}else {
			 resp=BaseJson.returnRespObj("purc/ProvrCls/selectProvrClsByProvrClsId",false,"����ʧ�ܣ�", provrCls);
		}
		return resp;
	}
	
	/*��ѯ���й�Ӧ�̷���*/
	@RequestMapping("selectProvrCls")
	@ResponseBody
	public Object selectProvrCls(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		List<Map> provrClsList = pcs.selectProvrCls();
		if(provrClsList!=null) {
			 resp=BaseJson.returnRespObjList("purc/ProvrCls/selectProvrCls",true,"����ɹ���", null, provrClsList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/ProvrCls/selectProvrCls",false,"����ʧ�ܣ�", null, provrClsList);
		}
		
		return resp;
	}
	
	//���빩Ӧ�̷���
	@RequestMapping("uploadProvrClsFile")
	@ResponseBody
	public Object uploadProvrClsFile(HttpServletRequest request){
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
			    return pcs.uploadFileAddDb(file);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			try {
				return BaseJson.returnRespObj("purc/ProvrCls/uploadProvrClsFile", false, "�����ļ���ʽ�����޷����룡", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
        return null;
	}
	

}
