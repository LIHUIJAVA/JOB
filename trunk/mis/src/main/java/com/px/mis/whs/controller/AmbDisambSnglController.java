package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.px.mis.system.service.impl.MisUserServiceImpl;
import com.px.mis.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
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
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.AmbDisambSnglubTab;
import com.px.mis.whs.service.AmbDisambSnglService;

//组装拆卸单据
@Controller
@RequestMapping("/whs/amb_disamb_sngl")
public class AmbDisambSnglController {

    private static final Logger logger = LoggerFactory.getLogger(AmbDisambSnglController.class);

    @Autowired
    AmbDisambSnglService ambDisambSnglService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    //	记账
    @Autowired
    FormBookService formBookService;
    @Autowired
    MisUserServiceImpl misUserService;

    // 新增的组装拆卸
    @RequestMapping(value = "insertASngl", method = RequestMethod.POST)
    @ResponseBody
    public Object insertASngl(@RequestBody String jsonBody) {
        logger.info("url:whs/amb_disamb_sngl/insertASngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));
            // 主表
            AmbDisambSngl aSngl = BaseJson.getPOJO(jsonBody, AmbDisambSngl.class);
            aSngl.setSetupPers(userName);
//            aSngl.setFormDt(loginTime);

            aSngl.setMprdcDt(StringUtils.trimToNull(aSngl.getMprdcDt()));// 生产日期
            aSngl.setInvldtnDt(StringUtils.trimToNull(aSngl.getInvldtnDt()));// 失效日期
            // 子表
            List<AmbDisambSnglubTab> aList = BaseJson.getPOJOList(jsonBody, AmbDisambSnglubTab.class);
            if(!misUserService.isWhsPer(jsonBody,aSngl.getWhsEncd())){
                throw new RuntimeException("用户没有仓库权限");
            }

            for (AmbDisambSnglubTab tab : aList) {
                tab.setSinvldtnDt(StringUtils.trimToNull(tab.getSinvldtnDt()));
                tab.setSprdcDt(StringUtils.trimToNull(tab.getSprdcDt()));
                if(!aSngl.getWhsEncd().equals(tab.getWhsEncd())){
                    throw new RuntimeException("子件仓库需要与母件相同");
                }
            }

            resp = ambDisambSnglService.insertAmbDisambSngl(userId, aSngl, aList,loginTime);

            misLogDAO.insertSelective(new MisLog("新增组装拆卸", "仓库", null, jsonBody, request, "成功", aSngl.getFormNum()));

        } catch (Exception e) {
            // 调试用
            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            misLogDAO.insertSelective(new MisLog("新增组装拆卸", "仓库", null, jsonBody, request, e));

            try {
                  resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASngl", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改组装拆卸
    @RequestMapping(value = "updateASngl", method = RequestMethod.POST)
    @ResponseBody
    public Object updateASngl(@RequestBody String jsonBody) {
        logger.info("url:whs/amb_disamb_sngl/updateASngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            // 主表
            AmbDisambSngl aSngl = BaseJson.getPOJO(jsonBody, AmbDisambSngl.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            aSngl.setMdfr(userName);
            aSngl.setMprdcDt(StringUtils.trimToNull(aSngl.getMprdcDt()));// 生产日期
            aSngl.setInvldtnDt(StringUtils.trimToNull(aSngl.getInvldtnDt()));// 失效日期

            // 子表
            List<AmbDisambSnglubTab> aList = BaseJson.getPOJOList(jsonBody, AmbDisambSnglubTab.class);
            if(!misUserService.isWhsPer(jsonBody,aSngl.getWhsEncd())){
                throw new RuntimeException("用户没有仓库权限");
            }
            // 子表
            for (AmbDisambSnglubTab snglubTab : aList) {
                snglubTab.setFormNum(aSngl.getFormNum());
                snglubTab.setMomQty(aSngl.getMomQty());
                snglubTab.setSinvldtnDt(StringUtils.trimToNull(snglubTab.getSinvldtnDt()));
                snglubTab.setSprdcDt(StringUtils.trimToNull(snglubTab.getSprdcDt()));
                if(!aSngl.getWhsEncd().equals(snglubTab.getWhsEncd())){
                    throw new RuntimeException("子件仓库需要与母件相同");
                }
            }

            resp = ambDisambSnglService.updateAmbDisambSngl(aSngl, aList);
            misLogDAO.insertSelective(new MisLog("修改组装拆卸", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            misLogDAO.insertSelective(new MisLog("修改组装拆卸", "仓库", null, jsonBody, request, e));

            try {
                return resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASngl", false, e.getMessage(), null);
            } catch (IOException e1) {

            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除组装拆卸 批删
    @RequestMapping(value = "deleteAllAmbDisambSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllAmbDisambSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/amb_disamb_sngl/deleteAllAmbDisambSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            String formNum = BaseJson.getReqBody(jsonBody).get("formNum").asText();

            resp = ambDisambSnglService.deleteAllAmbDisambSngl(formNum);
            misLogDAO.insertSelective(new MisLog("删除组装拆卸", "仓库", null, jsonBody, request, "成功", formNum));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("修改组装拆卸", "仓库", null, jsonBody, request, e));

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/deleteAllAmbDisambSngl", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 产品结构
    // 查询
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) {
        logger.info("url:whs/amb_disamb_sngl/query");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String formNum = reqBody.get("formNum").asText();
            resp = ambDisambSnglService.query(formNum);
        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件

            try {
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/query", false, "查询失败" + e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) {
        logger.info("url:whs/amb_disamb_sngl/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/queryList", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);

                resp = ambDisambSnglService.queryList(map);

            }


        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件

            try {
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/queryList", false, "查询失败" + e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 审核
    @RequestMapping(value = "updateASnglChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateASnglChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/amb_disamb_sngl/updateASnglChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Boolean isSuccess = true;

        try {

            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASnglChk", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));
            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                try {
                    resp += ambDisambSnglService.updateASnglChk(userId, dbzhu, userName,
                            CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) + "\n";
                    misLogDAO.insertSelective(new MisLog("审核组装拆卸", "仓库", null, jsonBody, request, "成功", dbzhu));

                } catch (Exception e) {

                    misLogDAO.insertSelective(new MisLog("审核组装拆卸", "仓库", null, jsonBody, request, e));

                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }
        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件
            misLogDAO.insertSelective(new MisLog("审核组装拆卸", "仓库", null, jsonBody, request, "失败", "json解析异常"));

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASnglChk", false, "json解析异常", null);

        }
        resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASnglChk", isSuccess, resp, null);
        // 审核
        // resp=ambDisambSnglService.updateASnglChk(userId, jsonBody);

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 弃审
    @RequestMapping(value = "updateASnglNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateASnglNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/amb_disamb_sngl/updateASnglNoChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Boolean isSuccess = true;

        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASnglNoChk", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                try {
                    resp += ambDisambSnglService.updateASnglNoChk(userId, dbzhu, userName) + "\n";
                    misLogDAO.insertSelective(new MisLog("弃审组装拆卸", "仓库", null, jsonBody, request, "成功", dbzhu));

                } catch (Exception e) {

                    misLogDAO.insertSelective(new MisLog("弃审组装拆卸", "仓库", null, jsonBody, request, e));

                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }

            }

            // 弃审
            // resp=ambDisambSnglService.updateASnglNoChk(userId, jsonBody);

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASnglNoChk", isSuccess, resp, null);
        } catch (Exception e1) {
            logger.error("返回参数：" + resp, e1);// 写入本地日志文件
            misLogDAO.insertSelective(new MisLog("弃审组装拆卸", "仓库", null, jsonBody, request, "失败", "json解析异常"));

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASnglNoChk", false, "json解析异常" + e1.getMessage(),
                    null);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) {
        logger.info("url:whs/amb_disamb_sngl/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null   && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null  && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/queryListDaYin", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);

            resp = ambDisambSnglService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("导出的组装拆卸", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            logger.error("返回参数：" + resp, e);// 写入本地日志文件

            try {
                misLogDAO.insertSelective(new MisLog("导出的组装拆卸", "仓库", null, jsonBody, request, e));

                return resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/queryListDaYin", false, e.getMessage(), null);
            } catch (IOException e1) {


            }
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入组装拆卸单单据
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public Object uploadPursOrderFile(HttpServletRequest request) {
        try {

//			misLogDAO.insertSelective(new MisLog("导入组装拆卸", "仓库", null, "", request));

            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request

                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDb", false, "请选择文件。", null);
                }
                return ambDisambSnglService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }

    // 导入组装拆卸单单据
    @RequestMapping("uploadFileAddDbU8")
    @ResponseBody
    public Object uploadFileAddDbU8(HttpServletRequest request) {
        try {

//			misLogDAO.insertSelective(new MisLog("导入组装拆卸", "仓库", null, "", request));

            // 创建一个通用的多部分解析器
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // 判断 request 是否有文件上传,即多部分请求
            if (multipartResolver.isMultipart(request)) {
                // 转换成多部分request

                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// request强制转换注意
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDbU8", false, "请选择文件。", null);
                }
                return ambDisambSnglService.uploadFileAddDbU8(file);
            } else {
                return BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDbU8", false, "请选择文件。", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDbU8", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }
}
