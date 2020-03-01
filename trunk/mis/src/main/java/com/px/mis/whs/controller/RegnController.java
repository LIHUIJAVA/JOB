package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.List;
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


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.Regn;
import com.px.mis.whs.service.RegnService;

//区域档案的单据
@Controller
@RequestMapping("/whs/regn")
public class RegnController {

    private static final Logger logger = LoggerFactory.getLogger(RegnController.class);

    @Autowired
    RegnService regnService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // 新增区域档案
    @RequestMapping(value = "insertRegn", method = RequestMethod.POST)
    @ResponseBody
    public Object insertRegn(@RequestBody String jsonBody) {
        logger.info("url:whs/regn/insertRegn");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            Regn regn = BaseJson.getPOJO(jsonBody, Regn.class);
            regn.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = regnService.insertRegn(regn);
            resp = BaseJson.returnRespObj("whs/regn/insertRegn", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增区域档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("新增区域档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增区域档案", "仓库", null, jsonBody, request, e));

            try {
                resp = BaseJson.returnRespObj("whs/regn/insertRegn", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改区域档案
    @RequestMapping(value = "updateRegn", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRegn(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/regn/updateRegn");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Regn regn = BaseJson.getPOJO(jsonBody, Regn.class);
            regn.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = regnService.updateRegn(regn);

            resp = BaseJson.returnRespObj("whs/regn/updateRegn", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("修改区域档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("修改区域档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改区域档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/regn/updateRegn", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除区域档案 --
    @RequestMapping(value = "deleteRegn", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteRegn(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/regn/deleteRegn");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String regnEncd = reqBody.get("regnEncd").asText();

            objectNode = regnService.deleteRegn(regnEncd);
            resp = BaseJson.returnRespObj("whs/regn/deleteRegn", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("删除区域档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("删除区域档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除区域档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/regn/deleteRegn", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除
    @RequestMapping(value = "deleteRegnList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWDocList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/regn/deleteRegnList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String regnEncd = reqBody.get("regnEncd").asText();

            resp = regnService.deleteRegnList(regnEncd);
            misLogDAO.insertSelective(new MisLog("批量删除区域档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("批量删除区域档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/regn/deleteRegnList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 区域档案 --
    @RequestMapping(value = "selectRegn", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRegn(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/regn/selectRegn");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String regnEncd = reqBody.get("regnEncd").asText();

            Regn regn = regnService.selectRegn(regnEncd);
            if (regn != null) {
                resp = BaseJson.returnRespObj("whs/regn/selectRegn", true, "查询成功！", regn);
            } else {
                resp = BaseJson.returnRespObj("whs/regn/selectRegn", false, "查询失败！", regn);
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/regn/selectRegn", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 查询 区域档案 list
    @RequestMapping(value = "selectRegnList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRegnList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/regn/selectRegnList");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String regnEncd = reqBody.get("regnEncd").asText();

            List<Regn> rList = regnService.selectRegnList(regnEncd);
            if (rList != null) {
                resp = BaseJson.returnRespObjList("whs/regn/selectRegnList", true, "查询成功！", null, rList);
            } else {
                resp = BaseJson.returnRespObjList("whs/regn/selectRegnList", false, "查询失败！", null, rList);
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/regn/selectRegnList", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/regn/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = regnService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/regn/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/regn/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = regnService.queryListDaYin(map);
            misLogDAO.insertSelective(new MisLog("导出区域档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出区域档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/regn/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入区域档案
    @RequestMapping("uploadRegnFile")
    @ResponseBody
    public Object uploadRegnFile(HttpServletRequest request) {
        try {
            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return ("请选择文件。");
                }
                return regnService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/regn/uploadRegnFile", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

}
