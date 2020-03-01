package com.px.mis.purc.controller;


import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.px.mis.purc.entity.ProjCls;
import com.px.mis.purc.service.ProjClsService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//项目分类档案
@RequestMapping(value="ec/projCls")
@Controller
public class ProjClsController {
	
	private Logger logger = LoggerFactory.getLogger(ProjClsController.class);
	
	@Autowired
	private ProjClsService projClsService;
	
	@RequestMapping("insertProjCls")
	public @ResponseBody Object insertProjCls(@RequestBody String jsonData) {
		String resp="";
		try {
			ProjCls projCls=BaseJson.getPOJO(jsonData,ProjCls.class);
			resp=projClsService.insertProjCls(projCls);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
		
	}
	
	/*修改客户分类*/
	@RequestMapping("updateProjCls")
	public @ResponseBody Object updateProjClsByOrdrNum(@RequestBody String jsonData) {
		
		String resp="";
		try {
			ProjCls projCls=BaseJson.getPOJO(jsonData,ProjCls.class);
			resp=projClsService.updateProjCls(projCls);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return resp;
	}
	
	/*删除客户分类*/
//	@RequestMapping("deleteProjClsByOrdrNum")
//	public @ResponseBody Object deleteProjClsByOrdrNum(@RequestBody String jsonData) {
//		
//		String resp="";
//		try {
//			ObjectNode reqBody = BaseJson.getReqBody(jsonData);
//			Integer ordrNum = Integer.parseInt(reqBody.get("ordrNum").asText());
//			resp=projClsService.deleteProjClsByOrdrNum(ordrNum);
//		} catch (Exception e) {
//			//e.printStackTrace();
//		}
//		return resp;
//	}
	
	/*按照自增序号查询*/
	@RequestMapping("selectProjClsByOrdrNum")
	@ResponseBody
	public Object selectProjClsByOrdrNum(@RequestBody String jsonData){
		logger.info("url:ec/projCls/selectProjClsByOrdrNum");
		logger.info("请求参数："+jsonData);
		String resp="";
		try {
			resp=projClsService.selectProjClsByOrdrNum(Integer.parseInt(BaseJson.getReqBody(jsonData).get("ordrNum").asText()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	//查询全部项目分类
	@RequestMapping(value = "queryList", method = RequestMethod.POST)
	@ResponseBody
	public String queryList(@RequestBody String jsonBody) {
		logger.info("url:ec/projCls/queryList");
		logger.info("请求参数：" + jsonBody);
		Map map = null;
		String resp = "";
		try {
			map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			resp = projClsService.queryList(map);
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数" + resp);
		return resp;
	}
	/*按照项目编码查询项目分类*/
	@RequestMapping("selectProjClsByProjEncd")
	@ResponseBody
	public String selectProjClsByProjEncd(@RequestBody String jsonData){
		logger.info("url:ec/projCls/selectProjClsByProjEncd");
		logger.info("请求参数："+jsonData);
		String resp="";
		try {
			resp=projClsService.selectProjClsByProjEncd(BaseJson.getReqBody(jsonData).get("projEncd").asText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	/*批量删除项目分类*/
	@RequestMapping(value = "delProjCls", method = RequestMethod.POST)
	@ResponseBody
	public String delProjCls(@RequestBody String jsonBody) {
		logger.info("url:ec/projCls/delProjCls");
		logger.info("请求参数：" + jsonBody);
		String resp = "";
		try {
			resp=projClsService.delProjCls(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
		} catch (IOException e) {
			//e.printStackTrace();
		}
		logger.info("返回参数" + resp);
		return resp;
	}

}
