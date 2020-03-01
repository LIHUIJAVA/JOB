package com.px.mis.purc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
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
import com.px.mis.purc.entity.CustCls;
import com.px.mis.purc.entity.EntrsAgnAdj;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.ToGdsSnglService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
/*��������������*/
@RequestMapping(value="purc/ToGdsSngl")
@Controller
public class ToGdsSnglController {
	
	private Logger logger = LoggerFactory.getLogger(ToGdsSnglController.class);
	
	@Autowired
	private ToGdsSnglService tgss;
    //����
	@Autowired
	FormBookService formBookService;
	
	//������������Ϣ
	@RequestMapping(value="addToGdsSngl",method = RequestMethod.POST)
	@ResponseBody
	private String addPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/ToGdsSngl/addToGdsSngl");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userId=aString.get("accNum").asText();
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			
			ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody,ToGdsSngl.class);
			toGdsSngl.setSetupPers(userName);
			List<ToGdsSnglSub> toGdsSnglSubList=new ArrayList<>();
			toGdsSnglSubList=BaseJson.getPOJOList(jsonBody, ToGdsSnglSub.class);
			resp=tgss.addToGdsSngl(userId,toGdsSngl, toGdsSnglSubList,loginTime);
		} catch (Exception e) {
			//e.printStackTrace();
			resp=BaseJson.returnRespObj("purc/ToGdsSngl/addToGdsSngl", false, e.getMessage(), null);
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�޸ĵ�������Ϣ
	@RequestMapping(value="editToGdsSngl",method = RequestMethod.POST)
	@ResponseBody
	private String editPursOrdr(@RequestBody String jsonBody) throws IOException {
		logger.info("url:purc/ToGdsSngl/editToGdsSngl");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);
			toGdsSngl.setMdfr(userName);
			List<ToGdsSnglSub> toGdsSnglSubList=BaseJson.getPOJOList(jsonBody, ToGdsSnglSub.class);
			resp=tgss.editToGdsSngl(toGdsSngl, toGdsSnglSubList);
		} catch (Exception e) {
			resp=BaseJson.returnRespObj("purc/ToGdsSngl/editToGdsSngl", false, e.getMessage(), null);
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//ɾ����������Ϣ
	@RequestMapping(value="deleteToGdsSngl",method = RequestMethod.POST)
	@ResponseBody
	private String deletePursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/deleteToGdsSngl");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=tgss.deleteToGdsSngl(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//��ѯ��������Ϣ
	@RequestMapping(value="queryToGdsSngl",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdr(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/queryToGdsSngl");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=tgss.queryToGdsSngl(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	@RequestMapping(value="queryToGdsSnglList",method = RequestMethod.POST)
	@ResponseBody
	private String queryPursOrdrList(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/queryToGdsSnglList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String toGdsSnglDt1 = (String) map.get("toGdsSnglDt1");
			String toGdsSnglDt2 = (String) map.get("toGdsSnglDt2");
			if (toGdsSnglDt1!=null && toGdsSnglDt1!="") {
				toGdsSnglDt1 = toGdsSnglDt1 + " 00:00:00";
			}
			if (toGdsSnglDt2!=null && toGdsSnglDt2!="") {
				toGdsSnglDt2 = toGdsSnglDt2 + " 23:59:59";
			}
			map.put("toGdsSnglDt1", toGdsSnglDt1);
			map.put("toGdsSnglDt2", toGdsSnglDt2);
			resp=tgss.queryToGdsSnglList(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//������������Ϣɾ��
	@RequestMapping(value="deleteToGdsSnglList",method = RequestMethod.POST)
	@ResponseBody
	public Object deleteToGdsSnglList(@RequestBody String jsonBody) throws IOException {
			
		logger.info("url:purc/ToGdsSngl/deleteToGdsSnglList");
		logger.info("���������"+jsonBody);
		String resp="";
		resp=tgss.deleteToGdsSnglList(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
	    logger.info("���ز�����"+resp);
		return resp;
	}

	//�������²ɹ����������״̬
	@RequestMapping(value="updateToGdsSnglIsNtChk",method = RequestMethod.POST)
	@ResponseBody
	public Object updateToGdsSnglIsNtChk(@RequestBody String jsonBody) throws Exception {
		logger.info("url:purc/ToGdsSngl/updateToGdsSnglIsNtChk");
		logger.info("���������"+jsonBody);
		Map<String,Object> result = new HashMap<>();
		String resp="";
		boolean isSuccess = false;
		List<ToGdsSngl> toGdsSnglList=new ArrayList();
		try {
			
			ObjectNode aString=BaseJson.getReqHead(jsonBody);
			String userName=aString.get("userName").asText();
			String loginTime=aString.get("loginTime").asText();
			if (formBookService.isMthSeal(loginTime)) {
				resp = BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglIsNtChk", false, "�����ѷ��ˣ�", null);
				logger.info("���ز�����" + resp);
				return resp;
			}
			toGdsSnglList = BaseJson.getPOJOList(jsonBody,ToGdsSngl.class);
			for (ToGdsSngl toGdsSngl : toGdsSnglList) {
				try
				{
					toGdsSngl.setChkr(userName);
					result=tgss.updateToGdsSnglIsNtChkList(toGdsSngl);
					isSuccess = (boolean) result.get("isSuccess");
					resp+=result.get("message");
				}
				catch(Exception e)
				{
					isSuccess = false;
					resp+=e.getMessage();
				}
			}	
			resp=BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglIsNtChk", isSuccess, resp, null);
		    logger.info("���ز�����"+resp);
	    }catch (Exception ex) {
			ex.printStackTrace();
			logger.info("���ز�����"+ex.getMessage());
			resp=BaseJson.returnRespObj("purc/ToGdsSngl/updateToGdsSnglIsNtChk", false, ex.getMessage(), null);
		}
	    
		return resp;
	}
	
	//��ӡ�����������ѯȫ��������
	@RequestMapping("printingToGdsSnglList")
	@ResponseBody
	public Object printingToGdsSnglList(@RequestBody String jsonBody){
		
		logger.info("url:purc/ToGdsSngl/printingToGdsSnglList");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			String toGdsSnglDt1 = (String) map.get("toGdsSnglDt1");
			String toGdsSnglDt2 = (String) map.get("toGdsSnglDt2");
			if (toGdsSnglDt1!=null && toGdsSnglDt1!="") {
				toGdsSnglDt1 = toGdsSnglDt1 + " 00:00:00";
			}
			if (toGdsSnglDt2!=null && toGdsSnglDt2!="") {
				toGdsSnglDt2 = toGdsSnglDt2 + " 23:59:59";
			}
			map.put("toGdsSnglDt1", toGdsSnglDt1);
			map.put("toGdsSnglDt2", toGdsSnglDt2);
			resp=tgss.printingToGdsSnglList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//������ϸ��
	@RequestMapping(value="queryToGdsSnglByInvtyEncd",method = RequestMethod.POST)
	@ResponseBody
	private String queryToGdsSnglByInvtyEncd(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/queryToGdsSnglByInvtyEncd");
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
			String toGdsSnglDt1 = (String) map.get("toGdsSnglDt1");
			String toGdsSnglDt2 = (String) map.get("toGdsSnglDt2");
			if (toGdsSnglDt1!=null && toGdsSnglDt1!="") {
				toGdsSnglDt1 = toGdsSnglDt1 + " 00:00:00";
			}
			if (toGdsSnglDt2!=null && toGdsSnglDt2!="") {
				toGdsSnglDt2 = toGdsSnglDt2 + " 23:59:59";
			}
			map.put("toGdsSnglDt1", toGdsSnglDt1);
			map.put("toGdsSnglDt2", toGdsSnglDt2);
//			resp=tgss.queryToGdsSnglList(map);
			resp=tgss.queryToGdsSnglByInvtyEncd(map);
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//���뵽����
	@RequestMapping("uploadToGdsSnglFile")
	@ResponseBody
	public Object uploadToGdsSnglFile(HttpServletRequest request) throws IOException{
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
			    return tgss.uploadFileAddDb(file,0);
		    }else {
		    	return BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFile", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFile", false, e.getMessage(), null);
		}
	}
	//���뵽����
	@RequestMapping("uploadToGdsSnglFileU8")
	@ResponseBody
	public Object uploadToGdsSnglFileU8(HttpServletRequest request) throws IOException{
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
			    return tgss.uploadFileAddDb(file,1);
		    }else {
		    	return BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFileU8", false, "��ѡ���ļ���", null);
		    }
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			return BaseJson.returnRespObj("purc/ToGdsSngl/uploadToGdsSnglFileU8", false, e.getMessage(), null);
		}
	}
	//�ɹ���ⵥ���յ�����ʱ���������Ϣ��ѯ���δ����ӱ���ϸ
	@RequestMapping("selectUnIntoWhsQtyByByToGdsSnglId")
	@ResponseBody
	public Object selectUnIntoWhsQtyByByToGdsSnglId(@RequestBody String jsonBody){
		logger.info("url:purc/ToGdsSngl/selectUnIntoWhsQtyByByToGdsSnglId");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=tgss.selectUnIntoWhsQtyByByToGdsSnglId(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//�������յ����յ�����ʱ���������Ϣ��ѯ���δ�����ӱ���ϸ
	@RequestMapping("selectUnReturnQtyByToGdsSnglId")
	@ResponseBody
	public Object selectUnReturnQtyByToGdsSnglId(@RequestBody String jsonBody){
		logger.info("url:purc/ToGdsSngl/selectUnReturnQtyByToGdsSnglId");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			resp=tgss.selectUnReturnQtyByToGdsSnglId(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	//����ʱ��ѯ������Ϣ
	@RequestMapping(value="queryToGdsSnglLists",method = RequestMethod.POST)
	@ResponseBody
	private String queryToGdsSnglLists(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/queryToGdsSnglLists");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String toGdsSnglDt1 = (String) map.get("toGdsSnglDt1");
			String toGdsSnglDt2 = (String) map.get("toGdsSnglDt2");
			if (toGdsSnglDt1!=null && toGdsSnglDt1!="") {
				toGdsSnglDt1 = toGdsSnglDt1 + " 00:00:00";
			}
			if (toGdsSnglDt2!=null && toGdsSnglDt2!="") {
				toGdsSnglDt2 = toGdsSnglDt2 + " 23:59:59";
			}
			map.put("toGdsSnglDt1", toGdsSnglDt1);
			map.put("toGdsSnglDt2", toGdsSnglDt2);
			resp=tgss.queryToGdsSnglLists(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	@RequestMapping(value="queryToGdsSnglListOrderBy",method = RequestMethod.POST)
	@ResponseBody
	private String queryToGdsSnglListOrderBy(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/queryToGdsSnglListOrderBy");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			Map map=JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo=(int)map.get("pageNo");
			int pageSize=(int)map.get("pageSize");
			map.put("index", (pageNo-1)*pageSize);
			map.put("num", pageSize);
			String toGdsSnglDt1 = (String) map.get("toGdsSnglDt1");
			String toGdsSnglDt2 = (String) map.get("toGdsSnglDt2");
			if (toGdsSnglDt1!=null && toGdsSnglDt1!="") {
				toGdsSnglDt1 = toGdsSnglDt1 + " 00:00:00";
			}
			if (toGdsSnglDt2!=null && toGdsSnglDt2!="") {
				toGdsSnglDt2 = toGdsSnglDt2 + " 23:59:59";
			}
			map.put("toGdsSnglDt1", toGdsSnglDt1);
			map.put("toGdsSnglDt2", toGdsSnglDt2);
			resp=tgss.queryToGdsSnglListOrderBy(map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*�رչ���*/
	@RequestMapping("updateToGdsSnglDealStatOK")
	public @ResponseBody Object updateToGdsSnglDealStatOK(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/updateToGdsSnglDealStatOK");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			String toGdsSnglId = BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText();
			resp=tgss.updateToGdsSnglDealStat(toGdsSnglId, 1);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
	/*�򿪹���*/
	@RequestMapping("updateToGdsSnglDealStatNO")
	public @ResponseBody Object updateToGdsSnglDealStatNO(@RequestBody String jsonBody) {
		logger.info("url:purc/ToGdsSngl/updateToGdsSnglDealStatNO");
		logger.info("���������"+jsonBody);
		String resp="";
		try {
			String toGdsSnglId = BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText();
			resp=tgss.updateToGdsSnglDealStat(toGdsSnglId, 0);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		logger.info("���ز�����"+resp);
		return resp;
	}
	
}
