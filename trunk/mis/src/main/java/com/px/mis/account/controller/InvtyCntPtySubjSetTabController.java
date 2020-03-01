package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyCntPtySubjSetTab;
import com.px.mis.account.service.InvtyCntPtySubjSetTabService;
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

//对方科目接口
@RequestMapping(value = "account/invtyCntPtySubjSetTab", method = RequestMethod.POST)
@Controller
public class InvtyCntPtySubjSetTabController {
    private Logger logger = LoggerFactory.getLogger(InvtyCntPtySubjSetTabController.class);
    @Autowired
    private InvtyCntPtySubjSetTabService icpsst;

    /* 添加 */
    @RequestMapping("insertInvtyCntPtySubjSetTab")
    public @ResponseBody Object insertInvtyCntPtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        InvtyCntPtySubjSetTab invtyCntPtySubjSetTab = null;
        try {
            invtyCntPtySubjSetTab = BaseJson.getPOJO(jsonData, InvtyCntPtySubjSetTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，insert error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            on = icpsst.insertInvtyCntPtySubjSetTab(invtyCntPtySubjSetTab);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab", false,
                        "insert error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 删除 */
    @RequestMapping("deleteInvtyCntPtySubjSetTab")
    public @ResponseBody Object deleteInvtyCntPtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            on = icpsst.deleteInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab", false,
                        "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 更新 */
    @RequestMapping("updateInvtyCntPtySubjSetTab")
    public @ResponseBody Object updateInvtyCntPtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab");
        logger.info("请求参数：" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<InvtyCntPtySubjSetTab> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                InvtyCntPtySubjSetTab invtyCntPtySubjSetTab = new InvtyCntPtySubjSetTab();
                BeanUtils.populate(invtyCntPtySubjSetTab, map);
                cList.add(invtyCntPtySubjSetTab);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            on = icpsst.updateInvtyCntPtySubjSetTabByOrdrNum(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab", false,
                        "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有 */
    @RequestMapping(value = "selectInvtyCntPtySubjSetTab")
    public @ResponseBody Object selectInvtyCntPtySubjSetTab(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab");
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
                resp = BaseJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab", false,
                        "请求参数错误，pageNo和pageSize都不容许等于零，查询失败！", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab 异常说明：", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = icpsst.selectInvtyCntPtySubjSetTabList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询单个 */
    @RequestMapping(value = "selectInvtyCntPtySubjSetTabById")
    public @ResponseBody Object selectInvtyCntPtySubjSetTabById(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById");
        logger.info("请求参数：" + jsonData);
        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById 异常说明：", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById", false,
                        "请求参数解析错误，请检查请求参数是否书写正确，没有查到数据！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById 异常说明：", e);
            }
            return resp;
        }
        try {
            InvtyCntPtySubjSetTab selectByOrdrNum = icpsst.selectInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
            if (selectByOrdrNum != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById", true,
                        "处理成功！", selectByOrdrNum);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById", false,
                        "处理失败！", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById 异常说明：", e);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除
    @RequestMapping(value = "deleteInvtyCntPtySubjSetTabList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteInvtyCntPtySubjSetTabList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTabList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        resp = icpsst.deleteInvtyCntPtySubjSetTabList(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
        logger.info("返回参数：" + resp);
        return resp;
    }

    /* 查询所有打印 */
    @RequestMapping(value = "selectInvtyCntPtySubjSetTabPrint")
    public @ResponseBody Object selectInvtyCntPtySubjSetTabPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint 异常说明：", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint",
                        false, "请求参数解析错误，请检查请求参数是否书写正确，查询失败！", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint 异常说明：", e);
            }
            return resp;
        }
        try {
            resp = icpsst.selectInvtyCntPtySubjSetTabListPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint 异常说明：", e);
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
                    return BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", false, "请选择文件。",
                            null);
                }
                return icpsst.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
