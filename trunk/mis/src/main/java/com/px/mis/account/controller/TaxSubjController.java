package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.TaxSubj;
import com.px.mis.account.service.TaxSubjService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//税金科目接口
@Controller
@RequestMapping(value = "account/taxSubj", method = RequestMethod.POST)
public class TaxSubjController {
    private Logger logger = LoggerFactory.getLogger(TaxSubjController.class);
    @Autowired
    private TaxSubjService tss;

    /* 添加 */
    @RequestMapping("insertTaxSubj")
    public @ResponseBody Object insertTaxSubj(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/insertTaxSubj");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        TaxSubj taxSubj = null;
        try {
            taxSubj = BaseJson.getPOJO(jsonData, TaxSubj.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/insertTaxSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/insertTaxSubj", false, "请求参数解析错误，请检查请求参数是否书写正确，添加异常了",
                        null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/insertTaxSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            on = tss.insertTaxSubj(taxSubj);
            if (on == null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/insertTaxSubj", false, "添加异常了", null);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/insertTaxSubj", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/insertTaxSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除 */
    @RequestMapping("deleteTaxSubj")
    public @ResponseBody Object deleteTaxSubj(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/deleteTaxSubj");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        String autoId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            autoId = reqBody.get("autoId").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/deleteTaxSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/deleteTaxSubj", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/deleteTaxSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            on = tss.deleteTaxSubjById(autoId);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/deleteTaxSubj", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/deleteTaxSubj", false, "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/deleteTaxSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 更新 */
    @RequestMapping("updateTaxSubj")
    public @ResponseBody Object updateTaxSubj(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/updateTaxSubj");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<TaxSubj> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                TaxSubj taxSubj = new TaxSubj();
                BeanUtils.populate(taxSubj, map);
                cList.add(taxSubj);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/updateTaxSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/updateTaxSubj", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/updateTaxSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            on = tss.updateTaxSubjById(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/updateTaxSubj", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/updateTaxSubj", false, "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/updateTaxSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有 */
    @RequestMapping(value = "selectTaxSubj")
    public @ResponseBody Object selectTaxSubj(@RequestBody String jsonBody) {
        logger.info("url:/account/taxSubj/selectTaxSubj");
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
                resp = BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", false,
                        "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！",
                        null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/selectTaxSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = tss.selectTaxSubjList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个 */
    @RequestMapping(value = "selectTaxSubjById")
    public @ResponseBody Object selectTaxSubjById(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/selectTaxSubjById");
        logger.info("请求参数：" + jsonData);
        String resp = "";
        Integer autoId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            autoId = reqBody.get("autoId").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/selectTaxSubjById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/selectTaxSubjById 异常说明：", e);
            }
            return resp;
        }
        try {
            TaxSubj taxSubj = tss.selectTaxSubjById(autoId);
            if (taxSubj != null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/selectTaxSubjById", true, "处理成功！", taxSubj);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/selectTaxSubjById", false, "没有查到数据！", taxSubj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有打印 */
    @RequestMapping(value = "selectTaxSubjPrint")
    public @ResponseBody Object selectTaxSubjPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/taxSubj/selectTaxSubjPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjPrint 异常说明：", e1);
            try {
                resp = BaseJson.returnRespList("/account/taxSubj/selectTaxSubjPrint", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/selectTaxSubjPrint 异常说明：", e);
            }
            return resp;
        }
        try {

            resp = tss.selectTaxSubjPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjPrint 异常说明：", e);
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
                    return BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", false, "请选择文件。", null);
                }
                return tss.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
