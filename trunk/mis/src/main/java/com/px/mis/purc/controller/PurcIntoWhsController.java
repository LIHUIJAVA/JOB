package com.px.mis.purc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.service.IntoWhsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*�ɹ���ⵥ��������*/
@RequestMapping(value="purc/IntoWhs")
@Controller
public class PurcIntoWhsController {
	
	private Logger logger = LoggerFactory.getLogger(PurcIntoWhsController.class);
	
	@Autowired
	private IntoWhsService iws;
    //����
	@Autowired
	FormBookService formBookService;
	
	//�����ɹ���ⵥ��Ϣ
	@RequestMapping(value="addIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String addIntoWhs(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/IntoWhs/addIntoWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try{
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			IntoWhs intoWhs = BaseJson.getPOJO(jsonBody, IntoWhs.class);
			intoWhs.setSetupPers(userName);
			List<IntoWhsSub> intoWhsSubList=new ArrayList<>();
			intoWhsSubList=BaseJson.getPOJOList(jsonBody, IntoWhsSub.class);
			resp=iws.addIntoWhs(userId,intoWhs, intoWhsSubList,loginTime);
		} catch (Exception e) {
			resp=BaseJson.returnRespObj("purc/IntoWhs/addIntoWhs", false, e.getMessage(), null);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�޸Ĳɹ���ⵥ��Ϣ
	@RequestMapping(value="editIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String editIntoWhs(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/IntoWhs/editIntoWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			IntoWhs intoWhs = BaseJson.getPOJO(jsonBody, IntoWhs.class);
			intoWhs.setMdfr(userName);
			List<IntoWhsSub> intoWhsSubList=new ArrayList<>();
			intoWhsSubList=BaseJson.getPOJOList(jsonBody, IntoWhsSub.class);
			resp=iws.editIntoWhs(intoWhs, intoWhsSubList);
		} catch (Exception e) {
			resp=BaseJson.returnRespObj("purc/IntoWhs/editIntoWhs", false, e.getMessage(), null);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//ɾ���ɹ�������Ϣ
	@RequestMapping(value="deleteIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String deleteIntoWhs(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/deleteIntoWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=iws.deleteIntoWhs(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ѯ�ɹ���ⵥ����
	@RequestMapping(value="queryIntoWhs",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhs(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhs");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp = iws.queryIntoWhs(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ҳ��ѯȫ���ɹ���ⵥ��Ϣ
	@RequestMapping(value="queryIntoWhsList",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsList(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			
			//�ֿ�Ȩ�޿���
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/IntoWhs/queryIntoWhsList", false, "�û�û�иòֿ�Ȩ��" , null);
			}else {
			    map.put("whsId", whsId);
			    resp = iws.queryIntoWhsList(map);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�����ɹ���ⵥ��Ϣɾ��
	@RequestMapping(value="deleteIntoWhsList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteIntoWhsList(@RequestBody String jsonBody) throws IOException {
			
		logger.info("url:purc/IntoWhs/deleteIntoWhsList");
		logger.info("���������"+jsonBody);
		String resp="";
		resp=iws.deleteIntoWhsList(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
	    logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������²ɹ���ⵥ���״̬
	@RequestMapping(value="updateIntoWhsIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateIntoWhsIsNtChk(@RequestBody String jsonBody) throws Exception {	
		
		logger.info("url:purc/IntoWhs/updateIntoWhsIsNtChk");
		logger.info("���������"+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp = "";
		boolean isSuccess = false;
		List<IntoWhs> intoWhsList=new ArrayList<>();
		try
		{
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/IntoWhs/updateIntoWhsIsNtChk", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
				return resp;
			}
			intoWhsList = BaseJson.getPOJOList(jsonBody,IntoWhs.class);
		    for(IntoWhs intoWhs :intoWhsList) {
				try
				{
					intoWhs.setChkr(userName);
					result=iws.updateIntoWhsIsNtChk(intoWhs);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}
			resp=BaseJson.returnRespObj("purc/IntoWhs/updateIntoWhsIsNtChk", isSuccess, resp, null);
		    logger.info("���ز�����"+resp);
		}catch (Exception ex) {
			logger.info("���ز�����"+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/IntoWhs/updateIntoWhsIsNtChk", false, ex.getMessage(), null);
		}
		return resp;
	}

	//��ӡ�����������ѯȫ���ɹ���ⵥ
	@RequestMapping("printingIntoWhsList")
	@ResponseBody
	public Object printingIntoWhsList(@RequestBody String jsonBody){
		
		logger.info("url:purc/IntoWhs/printingIntoWhsList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			//�ֿ�Ȩ�޿���
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/IntoWhs/printingIntoWhsList", false, "�û�û�иòֿ�Ȩ��" , null);
			}else {
			    map.put("whsId", whsId);
			    resp=iws.printingIntoWhsList(map);
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�����ϸ��
	@RequestMapping(value="queryIntoWhsByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsByInvtyEncd");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
				int pageNo=(int)map.get("pageNo");
				int pageSize=(int)map.get("pageSize");
				map.put("index", (pageNo-1)*pageSize);
				map.put("num", pageSize);
			}
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			resp=iws.queryIntoWhsByInvtyEncd(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�����ϸ��-����
		@RequestMapping(value="queryIntoWhsByInvtyEncdPrint",method = RequestMethod.POST)
		@ResponseBody
		private String queryIntoWhsByInvtyEncdPrint(@RequestBody String jsonBody) {
			logger.info("url:purc/IntoWhs/queryIntoWhsByInvtyEncdPrint");
			logger.info("���������"+jsonBody);
			String resp="";
			try {
				Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
				if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
					int pageNo=(int)map.get("pageNo");
					int pageSize=(int)map.get("pageSize");
					map.put("index", (pageNo-1)*pageSize);
					map.put("num", pageSize);
				}
				String intoWhsDt1 = (String) map.get("intoWhsDt1");
				String intoWhsDt2 = (String) map.get("intoWhsDt2");
				if (intoWhsDt1!=null && intoWhsDt1!="") {
					intoWhsDt1 = intoWhsDt1 + " 00:00:00";
				}
				if (intoWhsDt2!=null && intoWhsDt2!="") {
					intoWhsDt2 = intoWhsDt2 + " 23:59:59";
				}
				map.put("intoWhsDt1", intoWhsDt1);
				map.put("intoWhsDt2", intoWhsDt2);
				resp=iws.queryIntoWhsByInvtyEncdPrint(map);
			} catch (IOException e) {
				//e.printStackTrace();
			} 
			logger.info("���ز�����"+resp);
			return resp;
		}
	
	//�ɹ����ջ�ͳ�Ʊ�
	@RequestMapping(value="selectIntoWhsAndPursOrdr",method = RequestMethod.POST)
	@ResponseBody
	private String selectIntoWhsAndPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/selectIntoWhsAndPursOrdr");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			resp=iws.selectIntoWhsAndPursOrdr(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//����ɹ���ⵥ
	@RequestMapping("uploadIntoWhsFile")
	@ResponseBody
	public Object uploadIntoWhsFile(HttpServletRequest request) throws IOException{
		try {
			//����һ��ͨ�õĶಿ�ֽ�����
		    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
		    if(multipartResolver.isMultipart(request)) {
		        //ת���ɶಿ��request
			    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
		      	MultipartFile file = mRequest.getFile("file");
			    if(file == null) {
				   return  ("��ѡ���ļ�!");
			    }
			    return iws.uploadFileAddDb(file,0);
		    }else {
		    	return  BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFile", false, e.getMessage(), null);
		}
	}
	//����ɹ���ⵥ
		@RequestMapping("uploadIntoWhsFileU8")
		@ResponseBody
		public Object uploadIntoWhsFileU8(HttpServletRequest request) throws IOException{
			try {
				//����һ��ͨ�õĶಿ�ֽ�����
			    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
			    //�ж� request �Ƿ����ļ��ϴ�,���ಿ������
			    if(multipartResolver.isMultipart(request)) {
			        //ת���ɶಿ��request
				    MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;//requestǿ��ת��ע��
			      	MultipartFile file = mRequest.getFile("file");
				    if(file == null) {
					   return  ("��ѡ���ļ�!");
				    }
				    return iws.uploadFileAddDb(file,1);
			    }else {
			    	return  BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFileU8", false, "��ѡ���ļ���", null);
			    }
			} catch (Exception e) {
				// TODO: handle exception
				//e.printStackTrace();
				return BaseJson.returnRespObj("purc/IntoWhs/uploadIntoWhsFileU8", false, e.getMessage(), null);
			}
		}
		
	//�ɹ���Ʊ���ղɹ���ⵥʱ���ض���ɹ�����ӱ���Ϣ
	@RequestMapping("queryIntoWhsByIntoWhsIds")
	@ResponseBody
	public Object queryIntoWhsByIntoWhsIds(@RequestBody String jsonBody){
		logger.info("url:purc/IntoWhs/queryIntoWhsByIntoWhsIds");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=iws.queryIntoWhsByIntoWhsIds(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�ɹ��˻����ղɹ���ⵥʱ���ض���ɹ�����ӱ���Ϣ
	@RequestMapping("selectIntoWhsSubByUnReturnQty")
	@ResponseBody
	public Object selectIntoWhsSubByUnReturnQty(@RequestBody String jsonBody){
		logger.info("url:purc/IntoWhs/selectIntoWhsSubByUnReturnQty");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=iws.selectIntoWhsSubByUnReturnQty(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//����ʱ��ѯȫ���ɹ���ⵥ������Ϣ
	@RequestMapping(value="queryIntoWhsLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsLists(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsLists");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			resp = iws.queryIntoWhsLists(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	//��ҳ+����
	@RequestMapping(value="queryIntoWhsListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String queryIntoWhsListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/IntoWhs/queryIntoWhsListOrderBy");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String intoWhsDt1 = (String) map.get("intoWhsDt1");
			String intoWhsDt2 = (String) map.get("intoWhsDt2");
			if (intoWhsDt1!=null && intoWhsDt1!="") {
				intoWhsDt1 = intoWhsDt1 + " 00:00:00";
			}
			if (intoWhsDt2!=null && intoWhsDt2!="") {
				intoWhsDt2 = intoWhsDt2 + " 23:59:59";
			}
			map.put("intoWhsDt1", intoWhsDt1);
			map.put("intoWhsDt2", intoWhsDt2);
			//�ֿ�Ȩ�޿���
			String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
			if(StringUtils.isBlank(whsId)){
			    resp = BaseJson.returnRespObj("purc/IntoWhs/queryIntoWhsListOrderBy", false, "�û�û�иòֿ�Ȩ��" , null);
			}else {
			    map.put("whsId", whsId);
			    resp = iws.queryIntoWhsListOrderBy(map);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
}
