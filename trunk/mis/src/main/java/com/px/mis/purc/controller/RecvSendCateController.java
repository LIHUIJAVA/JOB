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
/*�շ����͵�����������*/
@RequestMapping(value="purc/RecvSendCate")
@Controller
public class RecvSendCateController {
	
	@Autowired
	private RecvSendCateService rscs;
	
	/*�����շ����͵���*/
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
	
	/*�޸��շ����͵���*/
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
	
	/*ɾ���շ����͵���*/
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
	
	/*���ձ�Ų�ѯ�շ����͵���*/
	@RequestMapping("selectRecvSendCateByRecvSendCateId")
	@ResponseBody
	public Object selectRecvSendCateByRecvSendCateId(@RequestBody String jsonData) throws IOException {
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		String recvSendCateId = reqBody.get("recvSendCateId").asText();
		RecvSendCate recvSendCate=rscs.selectRecvSendCateByRecvSendCateId(recvSendCateId);
		if(recvSendCate!=null) {
			 resp=BaseJson.returnRespObj("purc/RecvSendCate/selectRecvSendCateByRecvSendCateId",true,"����ɹ���", recvSendCate);
		}else {
			 resp=BaseJson.returnRespObj("purc/RecvSendCate/selectRecvSendCateByRecvSendCateId",false,"����ʧ�ܣ�", recvSendCate);
		}
		return resp;
	}
	
	/*��ѯ�����շ����͵���*/
	@RequestMapping("selectRecvSendCate")
	@ResponseBody
	public Object selectRecvSendCate(@RequestBody String jsonData) throws IOException {
		
		String resp="";
		ObjectNode reqBody = BaseJson.getReqBody(jsonData);
		List<Map> recvSendCateList = rscs.selectRecvSendCate();
		if(recvSendCateList!=null) {
			 resp=BaseJson.returnRespObjList("purc/RecvSendCate/selectRecvSendCate",true,"����ɹ���", null, recvSendCateList);
		}else {
			 resp=BaseJson.returnRespObjList("purc/RecvSendCate/selectRecvSendCate",false,"����ʧ�ܣ�", null, recvSendCateList);
		}
		
		return resp;
	}
	
	//���뵽����
	@RequestMapping("uploadRecvSendCateFile")
	@ResponseBody
	public Object uploadRecvSendCateFile(HttpServletRequest request) throws IOException{
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
			    return rscs.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/RecvSendCate/uploadRecvSendCateFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/RecvSendCate/uploadRecvSendCateFile", false, e.getMessage(), null);
		}
	}

}
