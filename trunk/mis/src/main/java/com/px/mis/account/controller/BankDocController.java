package com.px.mis.account.controller;

import java.io.IOException;
import java.util.ArrayList;
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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.service.BankDocService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//银行档案接口
@RequestMapping(value = "account/bankDoc", method = RequestMethod.POST)
@Controller
public class BankDocController {
    private Logger logger = LoggerFactory.getLogger(BankDocController.class); 
    @Autowired
    private BankDocService bds;

    /* 添加 */
    @RequestMapping("insertBankDoc")
    public @ResponseBody Object insertBankDoc(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/insertBankDoc");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        BankDoc bankDoc = null;
        try {
            bankDoc = BaseJson.getPOJO(jsonData, BankDoc.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/insertBankDoc 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/insertBankDoc", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了",
                        null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/insertBankDoc 异常说明：", e);
            }
            return resp;
        }
        try {
            on = bds.insertBankDoc(bankDoc);
            if (on == null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/insertBankDoc", false, "添加异常了", null);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/insertBankDoc", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/insertBankDoc 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除 */
    @RequestMapping("deleteBankDoc")
    public @ResponseBody Object deleteBankDoc(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/deleteBankDoc");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        String bankEncd = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            bankEncd = reqBody.get("bankEncd").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/deleteBankDoc 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/deleteBankDoc", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/deleteBankDoc 异常说明：", e);
            }
            return resp;
        }
        try {
            on = bds.deleteBankDocByOrdrNum(bankEncd);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/deleteBankDoc", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/deleteBankDoc", false, "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/deleteBankDoc 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 更新 */
    @RequestMapping("updateBankDoc")
    public @ResponseBody Object updateBankDoc(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/updateBankDoc");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<BankDoc> bankDocList = new ArrayList<BankDoc>();
        try {
        	ObjectNode jsonObject = JacksonUtil.getObjectNode(jsonData);
            JsonNode jsonObjectList = jsonObject.get("reqBody");
            JsonNode jsonArray = jsonObjectList.get("list");
            bankDocList = BaseJson.getPOJOList(jsonObjectList.toString(),BankDoc.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/updateBankDoc 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/updateBankDoc", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/updateBankDoc 异常说明：", e);
            }
            return resp;
        }
        try {
            on = bds.updateBankDocByordrNum(bankDocList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/updateBankDoc", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/updateBankDoc", false, "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/updateBankDoc 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有 */
    @RequestMapping(value = "selectBankDoc")
    public @ResponseBody Object selectBankDoc(@RequestBody String jsonBody) {
        logger.info("url:/account/bankDoc/selectBankDoc");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        Integer pageNo = null;
        Integer pageSize = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            pageNo = (int) map.get("pageNo");
            pageSize = (int) map.get("pageSize");
            if (pageNo == 0 || pageSize == 0) {
                resp = BaseJson.returnRespList("/account/bankDoc/selectBankDoc", false,
                        "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDoc 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/bankDoc/selectBankDoc", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/selectBankDoc 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = bds.selectBankDocList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDoc 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个 */
    @RequestMapping(value = "selectBankDocById")
    public @ResponseBody Object selectBankDocById(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/selectBankDocById");
        logger.info("请求参数：" + jsonData);
        String resp = "";
        String bankEncd = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            bankEncd = reqBody.get("bankEncd").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/selectBankDocById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/selectBankDocById 异常说明：", e);
            }
            return resp;
        }
        try {
            BankDoc bankDoc = bds.selectBankDocByordrNum(bankEncd);
            if (bankDoc != null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/selectBankDocById", true, "处理成功！", bankDoc);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/selectBankDocById", false, "没有查到数据！", bankDoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有打印 */
    @RequestMapping(value = "selectBankDocPrint")
    public @ResponseBody Object selectBankDocPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/bankDoc/selectBankDocPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocPrint 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/bankDoc/selectBankDocPrint", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，分页查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/selectBankDocPrint 异常说明：", e);
            }
            return resp;
        }
        try {

            resp = bds.selectBankDocPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocPrint 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入单据
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public String uploadPursOrderFile(HttpServletRequest request) throws IOException {
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
                    return BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", false, "请选择文件。", null);
                }
                return bds.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
