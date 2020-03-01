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
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.service.ExpressCorpService;

//快递公司的单据
@Controller
@RequestMapping("/whs/express_crop")
public class ExpressCorpController {

    private static final Logger logger = LoggerFactory.getLogger(ExpressCorpController.class);

    @Autowired
    ExpressCorpService expressCorpService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // 新增快递公司
    @RequestMapping(value = "insertExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object insertExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/insertExpressCorp");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ExpressCorp eCorp = BaseJson.getPOJO(jsonBody, ExpressCorp.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            eCorp.setSetupPers(userName);
            objectNode = expressCorpService.insertExpressCorp(eCorp);

            resp = BaseJson.returnRespObj("whs/express_crop/insertExpressCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增快递公司档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("新增快递公司档案", "仓库", null, jsonBody, request, "失败",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增快递公司档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/express_crop/insertExpressCorp", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改快递公司
    @RequestMapping(value = "updateExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object updateExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/updateExpressCorp");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ExpressCorp eCorp = BaseJson.getPOJO(jsonBody, ExpressCorp.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            eCorp.setMdfr(userName);
            objectNode = expressCorpService.updateExpressCorp(eCorp);

            resp = BaseJson.returnRespObj("whs/express_crop/updateExpressCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("修改快递公司档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("修改快递公司档案", "仓库", null, jsonBody, request, "失败",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改快递公司档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/express_crop/updateExpressCorp", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除快递公司--
    @RequestMapping(value = "deleteExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/deleteExpressCorp");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            objectNode = expressCorpService.deleteExpressCorp(expressEncd);

            resp = BaseJson.returnRespObj("whs/express_crop/deleteExpressCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("删除快递公司档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("删除快递公司档案", "仓库", null, jsonBody, request, "失败",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除快递公司档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/express_crop/deleteExpressCorp", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除
    @RequestMapping(value = "deleteECorpList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteECorpList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/deleteECorpList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            resp = expressCorpService.deleteECorpList(expressEncd);
            misLogDAO.insertSelective(new MisLog("批量删除快递公司档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("批量删除快递公司档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/express_crop/deleteECorpList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 快递公司--
    @RequestMapping(value = "selectExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object selectExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/selectExpressCorp");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            ExpressCorp wCorp = expressCorpService.selectExpressCorp(expressEncd);
            if (wCorp != null) {
                resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorp", true, "处理成功！", wCorp);
            } else {
                resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorp", false, "处理失败！", wCorp);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorp", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 查询 快递公司 list
    @RequestMapping(value = "selectExpressCorpList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectExpressCorpList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/selectExpressCorpList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            List<ExpressCorp> eList = expressCorpService.selectExpressCorpList(expressEncd);
            if (eList != null) {
                resp = BaseJson.returnRespObjList("whs/express_crop/selectExpressCorpList", true, "处理成功！", null, eList);
            } else {
                resp = BaseJson.returnRespObjList("whs/express_crop/selectExpressCorpList", false, "处理失败！", null,
                        eList);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorpList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = expressCorpService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/express_crop/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = expressCorpService.queryListDaYin(map);
            misLogDAO.insertSelective(new MisLog("导出快递公司档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("导出快递公司档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/express_crop/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入区域档案
    @RequestMapping("uploadExpressCorpFile")
    @ResponseBody
    public Object uploadExpressCorpFile(HttpServletRequest request) {
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
                return expressCorpService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/express_crop/uploadExpressCorpFile", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

    // 查快递express_code_and_name
    @RequestMapping(value = "queryExpressCodeAndNameList", method = RequestMethod.POST)
    @ResponseBody
    private String queryExpressCodeAndNameList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/queryExpressCodeAndNameList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            resp = expressCorpService.queryExpressCodeAndNameList();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/express_crop/queryExpressCodeAndNameList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }
}
