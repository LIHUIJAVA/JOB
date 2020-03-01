package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.EntrsAgnDelv;
import com.px.mis.purc.entity.EntrsAgnDelvSub;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.EntrsAgnDelvService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*ί�д�����������������*/
@RequestMapping(value="purc/EntrsAgnDelv")
@Controller
public class EntrsAgnDelvController {
	
	private Logger logger = LoggerFactory.getLogger(EntrsAgnDelvController.class);
	
	@Autowired
	private EntrsAgnDelvService eads;
    //����
	@Autowired
	FormBookService formBookService;
	
	//����ί�д�����������Ϣ
	@RequestMapping(value="addEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String addEntrsAgnDelv(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/EntrsAgnDelv/addEntrsAgnDelv");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			EntrsAgnDelv entrsAgnDelv = BaseJson.getPOJO(jsonBody, EntrsAgnDelv.class);
			entrsAgnDelv.setSetupPers(userName);
			List<EntrsAgnDelvSub> entrsAgnDelvSubList=new ArrayList<>();
			entrsAgnDelvSubList=BaseJson.getPOJOList(jsonBody, EntrsAgnDelvSub.class);
			resp=eads.addEntrsAgnDelv(userId,entrsAgnDelv, entrsAgnDelvSubList,loginTime);
		} catch (IOException e) {
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/EntrsAgnDelv/addEntrsAgnDelv", false, e.getMessage(), null);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�޸�ί�д�����������Ϣ
	@RequestMapping(value="editEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String editEntrsAgnDelv(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/EntrsAgnDelv/editEntrsAgnDelv");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			EntrsAgnDelv entrsAgnDelv = BaseJson.getPOJO(jsonBody, EntrsAgnDelv.class);
			entrsAgnDelv.setMdfr(userName);
			List<EntrsAgnDelvSub> entrsAgnDelvSubList=new ArrayList<>();
			entrsAgnDelvSubList=BaseJson.getPOJOList(jsonBody, EntrsAgnDelvSub.class);
			resp=eads.editEntrsAgnDelv(entrsAgnDelv, entrsAgnDelvSubList);
		} catch (IOException e) {
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/EntrsAgnDelv/editEntrsAgnDelv", false, e.getMessage(), null);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//ɾ���ɹ�������Ϣ
	@RequestMapping(value="deleteEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String deleteEntrsAgnDelv(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/deleteEntrsAgnDelv");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=eads.deleteEntrsAgnDelv(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ѯί�д�����������ϸ��Ϣ
	@RequestMapping(value="queryEntrsAgnDelv",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnDelv(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelv");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=eads.queryEntrsAgnDelv(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ѯ����ί�д�����������Ϣ
	@RequestMapping(value="queryEntrsAgnDelvList",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnDelvList(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelvList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String delvSnglDt1 = (String) map.get("delvSnglDt1");
			String delvSnglDt2 = (String) map.get("delvSnglDt2");
			if (delvSnglDt1!=null && delvSnglDt1!="") {
				delvSnglDt1 = delvSnglDt1 + " 00:00:00";
			}
			if (delvSnglDt2!=null && delvSnglDt2!="") {
				delvSnglDt2 = delvSnglDt2 + " 23:59:59";
			}
			map.put("delvSnglDt1", delvSnglDt1);
			map.put("delvSnglDt2", delvSnglDt2);
			resp=eads.queryEntrsAgnDelvList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	

	//����ί�д�����������Ϣɾ��
	@RequestMapping(value="deleteEntrsAgnDelvList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteEntrsAgnDelvList(@RequestBody String jsonBody) throws IOException {
			
		logger.info("url:purc/EntrsAgnDelv/deleteEntrsAgnDelvList");
		logger.info("���������"+jsonBody);
		String resp="";
		resp=eads.deleteEntrsAgnDelvList(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
	    logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�����������۵����״̬
	@RequestMapping(value="updateEntrsAgnDelvIsNtChkList",method = RequestMethod.POST)
	@ResponseBody
	public Object updateEntrsAgnDelvIsNtChkList(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList");
		logger.info("���������"+jsonBody);
		String resp="";
		Map<String,Object> result = new HashMap<>();
		boolean isSuccess = false;
		List<EntrsAgnDelv> entrsAgnDelvList=new ArrayList();
		try
		{
			String userId=BaseJson.getReqHead(jsonBody).get("accNum").asText();
			String userName=BaseJson.getReqHead(jsonBody).get("userName").asText();
			String loginTime=BaseJson.getReqHead(jsonBody).get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
				return resp;
			}
		    entrsAgnDelvList = BaseJson.getPOJOList(jsonBody, EntrsAgnDelv.class);
		    for(EntrsAgnDelv entrsAgnDelv :entrsAgnDelvList) {
				try
				{
					entrsAgnDelv.setChkr(userName);
					result=eads.updateEntrsAgnDelvIsNtChkList(userId,entrsAgnDelv,loginTime);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}	
		    resp=BaseJson.returnRespObj("/purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList", isSuccess, resp, null);
		    logger.info("���ز�����"+resp);
		}
		catch (Exception ex) {
			logger.info("���ز�����"+ex.getMessage());
			resp=BaseJson.returnRespObj("/purc/EntrsAgnDelv/updateEntrsAgnDelvIsNtChkList", false, ex.getMessage(), null);
		}
		return resp;
	}
	
	//��ӡ�����������ѯȫ���ͻ�����
	@RequestMapping("printingEntrsAgnDelvList")
	@ResponseBody
	public Object printingEntrsAgnDelvList(@RequestBody String jsonBody){
		
		logger.info("url:purc/EntrsAgnDelv/printingEntrsAgnDelvList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String delvSnglDt1 = (String) map.get("delvSnglDt1");
			String delvSnglDt2 = (String) map.get("delvSnglDt2");
			if (delvSnglDt1!=null && delvSnglDt1!="") {
				delvSnglDt1 = delvSnglDt1 + " 00:00:00";
			}
			if (delvSnglDt2!=null && delvSnglDt2!="") {
				delvSnglDt2 = delvSnglDt2 + " 23:59:59";
			}
			map.put("delvSnglDt1", delvSnglDt1);
			map.put("delvSnglDt2", delvSnglDt2);
			resp=eads.printingEntrsAgnDelvList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//����ί�д���������
	@RequestMapping("uploadEntrsAgnDelvFile")
	@ResponseBody
	public Object uploadEntrsAgnDelvFile(HttpServletRequest request) throws IOException{
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
			    return eads.uploadFileAddDb(file);
		    }else {
		    	return BaseJson.returnRespObj("purc/EntrsAgnDelv/uploadEntrsAgnDelvFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/EntrsAgnDelv/uploadEntrsAgnDelvFile", false, e.getMessage(), null);
		}
	}
	
	//����ʱ��ѯ����ί�д���������������Ϣ
	@RequestMapping(value="queryEntrsAgnDelvLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryEntrsAgnDelvLists(@RequestBody String jsonBody) {
		logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelvLists");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String delvSnglDt1 = (String) map.get("delvSnglDt1");
			String delvSnglDt2 = (String) map.get("delvSnglDt2");
			if (delvSnglDt1!=null && delvSnglDt1!="") {
				delvSnglDt1 = delvSnglDt1 + " 00:00:00";
			}
			if (delvSnglDt2!=null && delvSnglDt2!="") {
				delvSnglDt2 = delvSnglDt2 + " 23:59:59";
			}
			map.put("delvSnglDt1", delvSnglDt1);
			map.put("delvSnglDt2", delvSnglDt2);
			resp=eads.queryEntrsAgnDelvLists(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������յ����յ�����ʱ���������Ϣ��ѯ���δ�����ӱ���ϸ
	@RequestMapping("selectEntDeSubUnBllgRtnGoodsQty")
	@ResponseBody
	public Object selectEntDeSubUnBllgRtnGoodsQty(@RequestBody String jsonBody){
		logger.info("url:purc/EntrsAgnDelv/selectEntDeSubUnBllgRtnGoodsQty");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=eads.selectEntDeSubUnBllgRtnGoodsQty(BaseJson.getReqBody(jsonBody).get("delvSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	//��ҳ+�����ѯ����ί�д�����������Ϣ
			@RequestMapping(value="queryEntrsAgnDelvListOrderBy",method = RequestMethod.POST)
			@ResponseBody
			private String queryEntrsAgnDelvListOrderBy(@RequestBody String jsonBody) {
				logger.info("url:purc/EntrsAgnDelv/queryEntrsAgnDelvListOrderBy");
				logger.info("���������"+jsonBody);
				String resp="";
				try {
					Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
					int pageNo=(int)map.get("pageNo");
					int pageSize=(int)map.get("pageSize");
					map.put("index", (pageNo-1)*pageSize);
					map.put("num", pageSize);
					String delvSnglDt1 = (String) map.get("delvSnglDt1");
					String delvSnglDt2 = (String) map.get("delvSnglDt2");
					if (delvSnglDt1!=null && delvSnglDt1!="") {
						delvSnglDt1 = delvSnglDt1 + " 00:00:00";
					}
					if (delvSnglDt2!=null && delvSnglDt2!="") {
						delvSnglDt2 = delvSnglDt2 + " 23:59:59";
					}
					map.put("delvSnglDt1", delvSnglDt1);
					map.put("delvSnglDt2", delvSnglDt2);
					resp=eads.queryEntrsAgnDelvListOrderBy(map);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} 
				logger.info("���ز�����"+resp);
				return resp;
			}
	
}
