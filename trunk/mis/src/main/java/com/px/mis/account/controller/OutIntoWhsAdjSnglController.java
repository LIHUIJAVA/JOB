package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.OutIntoWhsAdjSngl;
import com.px.mis.account.service.OutIntoWhsAdjSnglService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
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
import java.util.Map;

//出入库调整单接口
@RequestMapping(value = "account/outIntoWhsAdjSngl", method = RequestMethod.POST)
@Controller
public class OutIntoWhsAdjSnglController {
    private Logger logger = LoggerFactory.getLogger(OutIntoWhsAdjSnglController.class);
    @Autowired
    private OutIntoWhsAdjSnglService outIntoWhsAdjSnglService;

    /* 添加出入库调整单 */
    @RequestMapping("insertOutIntoWhsAdjSngl")
    public @ResponseBody Object insertOutIntoWhsAdjSngl(@RequestBody String jsonData) {
        logger.info("url:/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        ObjectNode aString;
        String resp = "";
        String userId = "";
        String loginTime = "";
        OutIntoWhsAdjSngl outIntoWhsAdjSngl = null;
       
        try {
            aString = BaseJson.getReqHead(jsonData);
            userId = aString.get("accNum").asText();
            loginTime=aString.get("loginTime").asText();
            outIntoWhsAdjSngl = BaseJson.getPOJO(jsonData, OutIntoWhsAdjSngl.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，insert error!", outIntoWhsAdjSngl);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl 异常说明：", e);
            }
            return resp;
        }
        try {
            on = outIntoWhsAdjSnglService.insertOutIntoWhsAdjSngl(userId, outIntoWhsAdjSngl,loginTime);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), outIntoWhsAdjSngl);
            } else {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl", false,
                        "insert error!", outIntoWhsAdjSngl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/insertOutIntoWhsAdjSngl 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除出入库调整单 */
    @RequestMapping("deleteOutIntoWhsAdjSngl")
    public @ResponseBody Object deleteOutIntoWhsAdjSngl(@RequestBody String jsonData) {
        logger.info("url:/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        String formNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            formNum = reqBody.get("formNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl 异常说明：", e);
            }
            return resp;
        }
        try {
            on = outIntoWhsAdjSnglService.deleteOutIntoWhsAdjSnglByFormNum(formNum);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl", false,
                        "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/deleteOutIntoWhsAdjSngl 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 更新出入库调整单 */
    @RequestMapping("updateOutIntoWhsAdjSngl")
    public @ResponseBody Object updateOutIntoWhsAdjSngl(@RequestBody String jsonData) {
        logger.info("url:/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        OutIntoWhsAdjSngl OutIntoWhsAdjSngl = null;
        try {
            OutIntoWhsAdjSngl = BaseJson.getPOJO(jsonData, OutIntoWhsAdjSngl.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl 异常说明：", e);
            }
            return resp;
        }
        try {
            on = outIntoWhsAdjSnglService.updateOutIntoWhsAdjSnglByFormNum(OutIntoWhsAdjSngl);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl", false,
                        "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/updateOutIntoWhsAdjSngl 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有出入库调整单 */
    @RequestMapping(value = "selectOutIntoWhsAdjSngl")
    public @ResponseBody Object selectOutIntoWhsAdjSngl(@RequestBody String jsonBody) {
        logger.info("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        Map map = null;
        Integer pageNo = null;
        Integer pageSize = null;
        Object formTm1=null;
		Object formTm2=null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            pageNo = (int) map.get("pageNo");
            pageSize = (int) map.get("pageSize");
            formTm1 = (String) map.get("formTm1");
            formTm2 = (String) map.get("formTm2");
			if (formTm1!=null || formTm1!="") {
				formTm1 = formTm1 + " 00:00:00";
			}
			if (formTm2!=null || formTm2!="") {
				formTm2 = formTm2 + " 23:59:59";
			}
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl 异常说明：", e1);
            try {
                resp = BaseJson.returnRespList("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            map.put("formTm1", formTm1);
            map.put("formTm2", formTm2);
            resp = outIntoWhsAdjSnglService.selectOutIntoWhsAdjSnglList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSngl 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个出入库调整单 */
    @RequestMapping(value = "selectOutIntoWhsAdjSnglById")
    public @ResponseBody Object selectOutIntoWhsAdjSnglById(@RequestBody String jsonData) {
        logger.info("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById");
        logger.info("请求参数：" + jsonData);
        String resp = "";
        String formNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            formNum = reqBody.get("formNum").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById 异常说明：", e);
            }
            return resp;
        }
        try {
            OutIntoWhsAdjSngl outIntoWhsAdjSngl = outIntoWhsAdjSnglService.selectOutIntoWhsAdjSnglByFormNum(formNum);
            if (outIntoWhsAdjSngl != null) {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById", true, "处理成功！",
                        outIntoWhsAdjSngl);
            } else {
                resp = BaseJson.returnRespObj("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById", false,
                        "没有查到数据！", outIntoWhsAdjSngl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 打印、输出所有出入库调整单 */
    @RequestMapping(value = "selectOutIntoWhsAdjSnglPrint")
    public @ResponseBody Object selectOutIntoWhsAdjSnglPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String formTm1 = (String) map.get("formTm1");
			String formTm2 = (String) map.get("formTm2");
			if (formTm1!=null || formTm1!="") {
				formTm1 = formTm1 + " 00:00:00";
			}
			if (formTm2!=null || formTm2!="") {
				formTm2 = formTm2 + " 23:59:59";
			}
			map.put("formTm1", formTm1);
			map.put("formTm2", formTm2);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint 异常说明：", e1);
            try {
                resp = BaseJson.returnRespList("/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = outIntoWhsAdjSnglService.selectOutIntoWhsAdjSnglPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/outIntoWhsAdjSngl/selectOutIntoWhsAdjSnglPrint 异常说明：", e);
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
                    return BaseJson.returnRespObj("account/outIntoWhsAdjSngl/uploadFileAddDb", false, "请选择文件。", null);
                }
                return outIntoWhsAdjSnglService.uploadFileAddDb(file);
               
            } else {
                return BaseJson.returnRespObj("account/outIntoWhsAdjSngl/uploadFileAddDb", false, "请选择文件11。", null);

            }
        } catch (Exception e) {
           
            e.printStackTrace();
          
            String body =  e.getMessage();
            if(e instanceof RuntimeException){
                body = "导入失败了";
            }
            return BaseJson.returnRespObj("account/outIntoWhsAdjSngl/uploadFileAddDb", false, body, null);

        }
    }
}
