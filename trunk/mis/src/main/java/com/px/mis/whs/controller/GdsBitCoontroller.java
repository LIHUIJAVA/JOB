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
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.service.GdsBitService;

//货位管理
@Controller
@RequestMapping("/whs/gds_bit")
public class GdsBitCoontroller {

    private static final Logger logger = LoggerFactory.getLogger(GdsBitCoontroller.class);

    @Autowired
    GdsBitService gdsBitService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // 新增货位档案
    @RequestMapping(value = "insertGdsBit")
    @ResponseBody
    public Object insertGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/insertGdsBit");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            GdsBit gBit = BaseJson.getPOJO(jsonBody, GdsBit.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            gBit.setSetupPers(userName);
            objectNode = gdsBitService.insertGdsBit(gBit);

            resp = BaseJson.returnRespObj("whs/gds_bit/insertGdsBit", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增货位档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("新增货位档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增货位档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit/insertGdsBit", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改货位档案
    @RequestMapping(value = "updateGdsBit")
    @ResponseBody
    public Object updateGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/updateGdsBit");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            GdsBit gBit = BaseJson.getPOJO(jsonBody, GdsBit.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            gBit.setMdfr(userName);
            objectNode = gdsBitService.updateGdsBit(gBit);

            resp = BaseJson.returnRespObj("whs/gds_bit/updateGdsBit", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("修改货位档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("修改货位档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改货位档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit/updateGdsBit", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除货位档案 --
    @RequestMapping(value = "deleteGdsBit")
    @ResponseBody
    public Object deleteGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/deleteGdsBit");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            objectNode = gdsBitService.deleteGdsBit(gdsBitEncd);
            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBit", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("删除货位档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("删除货位档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除货位档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBit", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除
    @RequestMapping(value = "deleteGdsBitList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteGdsBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/deleteGdsBitList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            resp = gdsBitService.deleteGdsBitList(gdsBitEncd);
            misLogDAO.insertSelective(new MisLog("批量货位档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("批量货位档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBitList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 货位档案--
    @RequestMapping(value = "selectGdsBit")
    @ResponseBody
    public Object selectGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/selectGdsBit");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            GdsBit gdsBit = gdsBitService.selectGdsBit(gdsBitEncd);
            if (gdsBit != null) {
                resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBit", true, "处理成功！", gdsBit);
            } else {
                resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBit", false, "处理失败！", gdsBit);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBit", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 货位档案 list
    @RequestMapping(value = "selectGdsBitList")
    @ResponseBody
    public Object selectGdsBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/selectGdsBitList");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            List<GdsBit> gList = gdsBitService.selectGdsBitList(gdsBitEncd);
            if (gList != null) {
                resp = BaseJson.returnRespObjList("whs/gds_bit/selectGdsBitList", true, "处理成功！", null, gList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit/selectGdsBitList", false, "处理失败！", null, gList);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBitList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = gdsBitService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 查询所有货位类型
    @RequestMapping(value = "selectgTypList", method = RequestMethod.POST)
    @ResponseBody
    private String selectgTypList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/selectgTypList");
        String resp = "";
        try {

            resp = gdsBitService.selectgTypList();
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gds_bit/selectgTypList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = gdsBitService.queryListDaYin(map);
            misLogDAO.insertSelective(new MisLog("导出货位档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出货位档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gds_bit/queryListDaYin", false, e.getMessage(), null);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入货位档案
    @RequestMapping("uploadGdsBitFile")
    @ResponseBody
    public Object uploadGdsBitFile(HttpServletRequest request) {
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
                return gdsBitService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/gds_bit/uploadGdsBitFile", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

}
