package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtySubjSetTab;
import com.px.mis.account.service.InvtySubjSetTabService;
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

//存货科目接口
@RequestMapping(value = "account/invtySubjSetTab", method = RequestMethod.POST)
@Controller
public class InvtySubjSetTabController {
    private Logger logger = LoggerFactory.getLogger(InvtySubjSetTabController.class);
    @Autowired
    private InvtySubjSetTabService isst;

    /* 添加存货科目 */
    @RequestMapping("insertInvtySubjSetTab")
    public @ResponseBody Object insertInvtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/insertInvtySubjSetTab");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        InvtySubjSetTab invtySubjSetTab = null;
        try {
            invtySubjSetTab = BaseJson.getPOJO(jsonData, InvtySubjSetTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/insertInvtySubjSetTab 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/insertInvtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，insert error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/insertInvtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            on = isst.insertInvtySubjSetTab(invtySubjSetTab);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/insertInvtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/insertInvtySubjSetTab", false, "insert error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/insertInvtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除存货科目 */
    @RequestMapping("deleteInvtySubjSetTab")
    public @ResponseBody Object deleteInvtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/deleteInvtySubjSetTab");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/deleteInvtySubjSetTab 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/deleteInvtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/deleteInvtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            on = isst.deleteInvtySubjSetTabByOrdrNum(ordrNum);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/deleteInvtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/deleteInvtySubjSetTab", false, "delete error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/deleteInvtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 更新存货科目 */
    @RequestMapping("updateInvtySubjSetTab")
    public @ResponseBody Object updateInvtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/updateInvtySubjSetTab");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<InvtySubjSetTab> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                InvtySubjSetTab invtySubjSetTab = new InvtySubjSetTab();
                BeanUtils.populate(invtySubjSetTab, map);
                cList.add(invtySubjSetTab);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/updateInvtySubjSetTab 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/updateInvtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/updateInvtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            on = isst.updateInvtySubjSetTabById(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/updateInvtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/updateInvtySubjSetTab", false, "update error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/updateInvtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有存货科目 */
    @RequestMapping(value = "selectInvtySubjSetTab")
    public @ResponseBody Object selectInvtySubjSetTab(@RequestBody String jsonBody) {
        logger.info("url:/account/invtySubjSetTab/selectInvtySubjSetTab");
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
                resp = BaseJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTab", false,
                        "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTab 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = isst.selectInvtySubjSetTabList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个存货科目 */
    @RequestMapping(value = "selectInvtySubjSetTabById")
    public @ResponseBody Object selectInvtySubjSetTabById(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/selectInvtySubjSetTabById");
        logger.info("请求参数：" + jsonData);

        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/selectInvtySubjSetTabById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabById 异常说明：", e);
            }
            return resp;
        }
        try {
            InvtySubjSetTab selectByOrdrNum = isst.selectInvtySubjSetTabByOrdrNum(ordrNum);
            if (selectByOrdrNum != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/selectInvtySubjSetTabById", true, "处理成功！",
                        selectByOrdrNum);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/selectInvtySubjSetTabById", false, "没有查到数据！",
                        selectByOrdrNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除存货科目
    @RequestMapping(value = "deleteInvtySubjSetTabList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteInvtySubjSetTabList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:account/invtySubjSetTab/deleteInvtySubjSetTabList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        resp = isst.deleteInvtySubjSetTabList(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有存货科目打印 */
    @RequestMapping(value = "selectInvtySubjSetTabPrint")
    public @ResponseBody Object selectInvtySubjSetTabPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        Map map = null;

        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTabPrint", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint 异常说明：", e);
            }
            return resp;
        }
        try {

            resp = isst.selectInvtySubjSetTabPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint 异常说明：", e);
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
                    return BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", false, "请选择文件。", null);
                }
                return isst.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
