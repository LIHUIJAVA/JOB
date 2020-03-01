package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossSubTab;
import com.px.mis.whs.service.CheckPrftLossService;

//盘点损益表的单据
@Controller
@RequestMapping("/whs/check_sngl_loss")
public class CheckPrftLossController {

    private static final Logger logger = LoggerFactory.getLogger(CheckPrftLossController.class);

    @Autowired
    CheckPrftLossService checkPrftLossService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    //	记账
    @Autowired
    FormBookService formBookService;
    @Autowired
    MisUserService misUserService;

    // 新增损益表
    @RequestMapping(value = "insertCheckSnglLoss", method = RequestMethod.POST)
    @ResponseBody
    public Object insertCheckSnglLoss(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);
        logger.info("url:whs/check_sngl_loss/insertCheckSnglLoss");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();

            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));

            // 主表
            CheckPrftLoss cSngl = BaseJson.getPOJO(jsonBody, CheckPrftLoss.class);
            cSngl.setSetupPers(userName);
            // 子表
            List<CheckPrftLossSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckPrftLossSubTab.class);

            for (CheckPrftLossSubTab subTab : cList) {

                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
                subTab.setCheckFormNum(cSngl.getCheckFormNum());// 将主表主键提取插入子表

            }

            resp = checkPrftLossService.insertCheckSnglLoss(userId, cSngl, cList,loginTime);
            misLogDAO.insertSelective(new MisLog("新增损益表", "仓库", null, jsonBody, request));

            logger.info("返回参数：" + resp);

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("新增损益表", "仓库", null, jsonBody, request, e));

            return resp = BaseJson.returnRespObj("whs/check_sngl_loss/insertCheckSnglLoss", false, e.getMessage(),
                    null);

        }
        return resp;
    }

    // 修改损益表
    @RequestMapping(value = "updateCheckSnglLoss", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCheckSnglLoss(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl_loss/updateCheckSnglLoss");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            // 主表
            CheckPrftLoss cSngl = BaseJson.getPOJO(jsonBody, CheckPrftLoss.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            cSngl.setMdfr(userName);
            // 子表
            List<CheckPrftLossSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckPrftLossSubTab.class);
            for (CheckPrftLossSubTab subTab : cList) {
                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
                subTab.setCheckFormNum(cSngl.getCheckFormNum());// 将主表主键提取插入子表
            }

            resp = checkPrftLossService.updateCheckSnglLoss(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("修改损益表", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改损益表", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCheckSnglLoss", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除
    @RequestMapping(value = "deleteAllCheckSnglLoss", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllCheckSnglLoss(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/deleteAllCheckSnglLoss");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkPrftLossService.deleteAllCheckSnglLoss(checkFormNum);
            misLogDAO.insertSelective(new MisLog("删除损益表", "仓库", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("删除损益表", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/deleteAllCheckSnglLoss", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 产品结构
    // 查询、分页查
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/query");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkPrftLossService.query(checkFormNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/query", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("checkDt2") !=null && !map.get("checkDt2").equals("")) {

                map.put("checkDt2", map.get("checkDt2").toString() + " 23:59:59");
            }
            if (map.get("checkDt1") !=null && !map.get("checkDt1").equals("")) {
                map.put("checkDt1", map.get("checkDt1").toString() + " 00:00:00");
            }
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryList", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);


            resp = checkPrftLossService.queryList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 审核
    @RequestMapping(value = "updateCSnglLossChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglLossChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl_loss/updateCSnglLossChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossChk", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));
            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

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
                // 审核
                try {
                    resp += checkPrftLossService.updateCSnglLossChk(userId, dbzhu, userName,
                            CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) + "\n";
                    misLogDAO.insertSelective(new MisLog("审核损益表", "仓库", null, jsonBody, request, "成功", dbzhu));

                } catch (Exception e) {

                    misLogDAO.insertSelective(new MisLog("审核损益表", "仓库", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglChk", isSuccess, resp, null);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglChk", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 弃审
    @RequestMapping(value = "updateCSnglLossNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl_loss/updateCSnglLossNoChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossNoChk", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));
            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // 弃审
                try {
                    resp += checkPrftLossService.updateCSnglLossNoChk(userId, dbzhu) + "\n";
                    misLogDAO.insertSelective(new MisLog("弃审损益表", "仓库", null, jsonBody, request, "成功", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("弃审损益表", "仓库", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }

            }
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossNoChk", isSuccess, resp, null);
        } catch (Exception e1) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossNoChk", false, e1.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("checkDt2") !=null && !map.get("checkDt2").equals("")) {

                map.put("checkDt2", map.get("checkDt2").toString() + " 23:59:59");
            }
            if (map.get("checkDt1") !=null && !map.get("checkDt1").equals("")) {
                map.put("checkDt1", map.get("checkDt1").toString() + " 00:00:00");
            }
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryListDaYin", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);

            resp = checkPrftLossService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("导出损益表", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出损益表", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryListDaYin", false, e.getMessage(), null);

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
                return checkPrftLossService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/check_sngl_loss/uploadExpressCorpFile", false, "导入文件格式有误，无法导入！",
                        null);
            } catch (IOException e1) {


            }

        }
        return null;
    }
}
