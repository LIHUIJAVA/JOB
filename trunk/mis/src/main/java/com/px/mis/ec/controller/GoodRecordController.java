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

//��Ʒ����
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
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			GoodRecord goodRecord = BaseJson.getPOJO(jsonBody, GoodRecord.class);
			resp=goodRecordService.add(goodRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	private String edit(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/edit");
		logger.info("���������"+jsonBody);
		String resp="";
		System.err.println(jsonBody);
		try {
			GoodRecord goodRecord = BaseJson.getPOJO(jsonBody, GoodRecord.class);
			resp=goodRecordService.edit(goodRecord);
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	private String delete(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/delete");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=goodRecordService.delete(BaseJson.getReqBody(jsonBody).get("id").asText());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="query",method = RequestMethod.POST)
	@ResponseBody
	private String query(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/query");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=goodRecordService.query(BaseJson.getReqBody(jsonBody).get("id").asInt());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="queryList",method = RequestMethod.POST)
	@ResponseBody
	private String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/queryList");
		logger.info("���������"+jsonBody);
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
		logger.info("���ز�����"+resp);
		return resp;
	}
/**
 * ����
 * @param jsonBody
 * @return
 */
	@RequestMapping(value="exportList",method = RequestMethod.POST)
	@ResponseBody
	private String exportList(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/exportList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			resp=goodRecordService.exportList(map);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	
	@RequestMapping(value="download",method = RequestMethod.POST)
	@ResponseBody
	private String download(@RequestBody String jsonBody) {
		logger.info("url:ec/goodRecord/download");
		logger.info("���������"+jsonBody);
		String resp = "";
		try {
			String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String storeId = BaseJson.getReqBody(jsonBody).get("storeId").asText();
			resp = goodRecordService.download(accNum,storeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("���ز�����" + resp);
		return resp;
	}
	
	//���������Ʒ����
	@RequestMapping("uploadGoodRecord")
	@ResponseBody
	public Object uploadGoodRecord(HttpServletRequest request){
		//����һ��ͨ�õĶಿ�ֽ�����
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
        //System.out.println("��ʼ�������ݡ�����");
        try {
			if(multipartResolver.isMultipart(request)) {
			    //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
			  	MultipartFile file = mRequest.getFile("file");
			  	String userID = mRequest.getParameter("accNum");
			    if(file == null) {
			    	return BaseJson.returnRespObj("ec/goodRecord/uploadGoodRecord", false,"��ѡ���ļ�", null);
			    }else {
			    	 return goodRecordService.uploadFile(file,userID);
			    }
			}else {
				return BaseJson.returnRespObj("ec/goodRecord/uploadGoodRecord", false,"��ѡ���ļ�", null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
        
	}
}
