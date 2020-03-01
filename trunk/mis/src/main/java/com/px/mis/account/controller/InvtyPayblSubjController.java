package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyPayblSubj;
import com.px.mis.account.service.InvtyPayblSubjService;
import com.px.mis.account.utils.TransformJson;
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

//应付科目接口
@RequestMapping(value = "account/invtyPayblSubj", method = RequestMethod.POST)
@Controller
public class InvtyPayblSubjController {
    private Logger logger = LoggerFactory.getLogger(InvtyPayblSubjController.class);
    @Autowired
    private InvtyPayblSubjService ipss;

    /* 添加 */
    @RequestMapping("insertInvtyPayblSubj")
    public @ResponseBody Object insertInvtyPayblSubj(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/insertInvtyPayblSubj");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        InvtyPayblSubj invtyPayblSubj = null;
        try {
            invtyPayblSubj = BaseJson.getPOJO(jsonData, InvtyPayblSubj.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/insertInvtyPayblSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/insertInvtyPayblSubj", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，添加异常了", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/insertInvtyPayblSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            on = ipss.insertInvtyPayblSubj(invtyPayblSubj);
            if (on == null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/insertInvtyPayblSubj", false, "添加异常了", null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/insertInvtyPayblSubj",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/insertInvtyPayblSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除 */
    @RequestMapping("deleteInvtyPayblSubj")
    public @ResponseBody Object deleteInvtyPayblSubj(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/deleteInvtyPayblSubj");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        Integer incrsId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            incrsId = reqBody.get("incrsId").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/deleteInvtyPayblSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/deleteInvtyPayblSubj", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/deleteInvtyPayblSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            on = ipss.deleteInvtyPayblSubjById(incrsId);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/deleteInvtyPayblSubj",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/deleteInvtyPayblSubj", false, "delete error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/deleteInvtyPayblSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 更新 */
    @RequestMapping("updateInvtyPayblSubj")
    public @ResponseBody Object updateInvtyPayblSubj(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/updateInvtyPayblSubj");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<InvtyPayblSubj> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                InvtyPayblSubj invtyPayblSubj = new InvtyPayblSubj();
                BeanUtils.populate(invtyPayblSubj, map);
                cList.add(invtyPayblSubj);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/updateInvtyPayblSubj 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/updateInvtyPayblSubj", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/updateInvtyPayblSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            on = ipss.updateInvtyPayblSubjById(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/updateInvtyPayblSubj",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/updateInvtyPayblSubj", false, "update error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/updateInvtyPayblSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有 */
    @RequestMapping(value = "selectInvtyPayblSubj")
    public @ResponseBody Object selectInvtyPayblSubj(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyPayblSubj/selectInvtyPayblSubj");
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
                resp = BaseJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubj", false,
                        "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubj", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = ipss.selectInvtyPayblSubjList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个 */
    @RequestMapping(value = "selectInvtyPayblSubjById")
    public @ResponseBody Object selectInvtyPayblSubjById(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/selectInvtyPayblSubjById");
        logger.info("请求参数：" + jsonData);
        String resp = "";
        Integer incrsId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            incrsId = reqBody.get("incrsId").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/selectInvtyPayblSubjById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjById 异常说明：", e);
            }
            return resp;
        }
        try {
            InvtyPayblSubj invtyPayblSubj = ipss.selectInvtyPayblSubjById(incrsId);
            if (invtyPayblSubj != null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/selectInvtyPayblSubjById", true, "处理成功！",
                        invtyPayblSubj);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/selectInvtyPayblSubjById", false, "没有查到数据！",
                        invtyPayblSubj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除应付科目
    @RequestMapping(value = "deleteInvtyPayblSubjList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteInvtyPayblSubjList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:account/invtyPayblSubj/deleteInvtyPayblSubjList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        resp = ipss.deleteInvtyPayblSubjList(BaseJson.getReqBody(jsonBody).get("incrsId").asText());
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有打印 */
    @RequestMapping(value = "selectInvtyPayblSubjPrint")
    public @ResponseBody Object selectInvtyPayblSubjPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyPayblSubj/selectInvtyPayblSubjPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjPrint 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubjPrint", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjPrint 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = ipss.selectInvtyPayblSubjListPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj 异常说明：", e);
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
                    return BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", false, "请选择文件。", null);
                }
                return ipss.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
