package com.px.mis.whs.controller;

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


import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.LabelSteup;
import com.px.mis.whs.entity.LabelTemplateRelation;
import com.px.mis.whs.entity.LabelTemplates;
import com.px.mis.whs.service.LabelService;

//快递单据
@Controller
@RequestMapping("/whs/label")
public class LabelController {

    private static final Logger logger = LoggerFactory.getLogger(LabelController.class);
    @Autowired
    LabelService labelServiceImpl;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // 新增打印模板
    @RequestMapping(value = "insertLabelTemplates", method = RequestMethod.POST)
    @ResponseBody
    public String insertLabelTemplates(@RequestBody String jsonBody) {
        logger.info("url:whs/label/insertLabelTemplates");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            LabelTemplates labelTemplates = BaseJson.getPOJO(jsonBody, LabelTemplates.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            labelTemplates.setOpname(userName);
            labelTemplates.setOpid(userId);
            resp = labelServiceImpl.insertLabelTemplates(labelTemplates);
            misLogDAO.insertSelective(new MisLog("新增打印模板", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            // 调试用
            misLogDAO.insertSelective(new MisLog("新增打印模板", "仓库", null, jsonBody, request, e));

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelTemplates", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改打印模板
    @RequestMapping(value = "updateLabelTemplates", method = RequestMethod.POST)
    @ResponseBody
    public String updateLabelTemplates(@RequestBody String jsonBody) {
        logger.info("url:whs/label/updateLabelTemplates");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            LabelTemplates labelTemplates = BaseJson.getPOJO(jsonBody, LabelTemplates.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            labelTemplates.setOpname(userName);
            labelTemplates.setOpid(userId);
            resp = labelServiceImpl.updateLabelTemplates(labelTemplates);
            misLogDAO.insertSelective(new MisLog("修改打印模板", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改打印模板", "仓库", null, jsonBody, request, e));


            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplates", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除打印模板 批删
    @RequestMapping(value = "deleteLabelTemplates", method = RequestMethod.POST)
    @ResponseBody
    public String deleteLabelTemplates(@RequestBody String jsonBody) {
        logger.info("url:whs/label/deleteLabelTemplates");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            String idx = BaseJson.getReqBody(jsonBody).get("idx").asText();

            resp = labelServiceImpl.deleteLabelTemplates(idx);
            misLogDAO.insertSelective(new MisLog("删除打印模板", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除打印模板", "仓库", null, jsonBody, request, e));


            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/deleteLabelTemplates", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查 打印模板
    @RequestMapping(value = "queryListLabelTemplates", method = RequestMethod.POST)
    @ResponseBody
    public String queryListLabelTemplates(@RequestBody String jsonBody) {
        logger.info("url:whs/label/queryListLabelTemplates");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            LabelTemplates labelTemplates = BaseJson.getPOJO(jsonBody, LabelTemplates.class);

            int pageNo = BaseJson.getReqBody(jsonBody).get("pageNo").asInt();
            int pageSize = BaseJson.getReqBody(jsonBody).get("pageSize").asInt();

            resp = labelServiceImpl.queryListLabelTemplates(labelTemplates, pageNo, pageSize);

        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/queryListLabelTemplates", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 新增模板对应表
    @RequestMapping(value = "insertlabeLtemplaterelation", method = RequestMethod.POST)
    @ResponseBody
    public String insertlabeLtemplaterelation(@RequestBody String jsonBody) {
        logger.info("url:whs/label/insertlabeLtemplaterelation");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            LabelTemplateRelation labelTemplateRelation = BaseJson.getPOJO(jsonBody, LabelTemplateRelation.class);

            resp = labelServiceImpl.insertLabelTemplateRelation(labelTemplateRelation);
            misLogDAO.insertSelective(new MisLog("新增模板对应表", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增模板对应表", "仓库", null, jsonBody, request, e));


            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/insertlabeLtemplaterelation", true, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改模板对应表
    @RequestMapping(value = "updateLabelTemplateRelation", method = RequestMethod.POST)
    @ResponseBody
    public String updateLabelTemplateRelation(@RequestBody String jsonBody) {
        logger.info("url:whs/label/updateLabelTemplateRelation");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            LabelTemplateRelation labelTemplateRelation = BaseJson.getPOJO(jsonBody, LabelTemplateRelation.class);
            resp = labelServiceImpl.updateLabelTemplateRelation(labelTemplateRelation);
            misLogDAO.insertSelective(new MisLog("修改模板对应表", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改模板对应表", "仓库", null, jsonBody, request, e));

            logger.error("返回参数：" + resp, e);// 写入本地日志文件

            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelTemplateRelation", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除模板对应表
    @RequestMapping(value = "deleteLabelTemplateRelation", method = RequestMethod.POST)
    @ResponseBody
    public String deleteLabelTemplateRelation(@RequestBody String jsonBody) {
        logger.info("url:whs/label/deleteLabelTemplateRelation");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            String idx = BaseJson.getReqBody(jsonBody).get("idx").asText();

            resp = labelServiceImpl.deleteLabelTemplateRelation(idx);
            misLogDAO.insertSelective(new MisLog("删除模板对应表", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除模板对应表", "仓库", null, jsonBody, request, e));

            logger.error("返回参数：" + resp, e);// 写入本地日志文件

            try {
                resp = BaseJson.returnRespObj("whs/label/deleteLabelTemplateRelation", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查 模板对应表
    @RequestMapping(value = "queryListLabelTemplateRelation", method = RequestMethod.POST)
    @ResponseBody
    public String queryListLabelTemplateRelation(@RequestBody String jsonBody) {
        logger.info("url:whs/label/queryListLabelTemplateRelation");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            LabelTemplateRelation labelTemplateRelation = BaseJson.getPOJO(jsonBody, LabelTemplateRelation.class);
            int pageNo = BaseJson.getReqBody(jsonBody).get("pageNo").asInt();
            int pageSize = BaseJson.getReqBody(jsonBody).get("pageSize").asInt();
            resp = labelServiceImpl.queryListLabelTemplateRelation(labelTemplateRelation, pageNo, pageSize);
        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/queryListLabelTemplateRelation", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 新增标签设置
    @RequestMapping(value = "insertLabelSteup", method = RequestMethod.POST)
    @ResponseBody
    public String insertLabelSteup(@RequestBody String jsonBody) {
        logger.info("url:whs/label/insertLabelSteup");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            LabelSteup labelSteup = BaseJson.getPOJO(jsonBody, LabelSteup.class);
            resp = labelServiceImpl.insertLabelSteup(labelSteup);
            misLogDAO.insertSelective(new MisLog("新增标签设置", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增标签设置", "仓库", null, jsonBody, request, e));

            // 调试用
            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/insertLabelSteup", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改标签设置
    @RequestMapping(value = "updateLabelSteup", method = RequestMethod.POST)
    @ResponseBody
    public String updateLabelSteup(@RequestBody String jsonBody) {
        logger.info("url:whs/label/updateLabelSteup");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            LabelSteup labelSteup = BaseJson.getPOJO(jsonBody, LabelSteup.class);
            resp = labelServiceImpl.updateLabelSteup(labelSteup);
            misLogDAO.insertSelective(new MisLog("修改标签设置", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改标签设置", "仓库", null, jsonBody, request, e));


            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/updateLabelSteup", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除标签设置 批删
    @RequestMapping(value = "deleteLabelSteup", method = RequestMethod.POST)
    @ResponseBody
    public String deleteLabelSteup(@RequestBody String jsonBody) {
        logger.info("url:whs/label/deleteLabelSteup");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            String idx = BaseJson.getReqBody(jsonBody).get("idx").asText();
            resp = labelServiceImpl.deleteLabelSteup(idx);
            misLogDAO.insertSelective(new MisLog("删除标签设置", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除标签设置", "仓库", null, jsonBody, request, e));

            logger.error("返回参数：" + resp, e);// 写入本地日志文件

            try {
                resp = BaseJson.returnRespObj("whs/label/deleteLabelSteup", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查 标签设置
    @RequestMapping(value = "queryListLabelSteup", method = RequestMethod.POST)
    @ResponseBody
    public String queryListLabelSteup(@RequestBody String jsonBody) {
        logger.info("url:whs/label/queryListLabelSteup");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

//			Map<String, Object> map = ObjToJsonUtil.getReqBodyMap(jsonBody);
            resp = labelServiceImpl.queryListLabelSteup(map);

        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            try {
                resp = BaseJson.returnRespObj("whs/label/queryListLabelSteup", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

//	// 查询标签字段
//	@RequestMapping(value = "query", method = RequestMethod.POST)
//	@ResponseBody
//	private String query(@RequestBody String jsonBody) {
//		logger.info("url:whs/label/query");
//		logger.info("请求参数：" + jsonBody);
//		String resp = "";
//
//		try {
//			String idx = JSON.parseObject(jsonBody).getJSONObject("reqHead").getString("idx");
//
//			resp = labelServiceImpl.query();
//		} catch (Exception e) {
//			
//			logger.error("返回参数：" + resp, e);// 写入本地日志文件
//
//		}
//		logger.info("返回参数：" + resp);
//		return resp;
//	}
//	
}