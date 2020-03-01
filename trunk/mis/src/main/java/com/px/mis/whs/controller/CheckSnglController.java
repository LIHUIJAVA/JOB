package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.px.mis.system.service.impl.MisUserServiceImpl;
import com.px.mis.util.CommonUtil;
import org.apache.commons.beanutils.BeanUtils;
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
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglSubTab;
import com.px.mis.whs.service.CheckSnglService;

//盘点单的单据
@Controller
@RequestMapping("/whs/check_sngl")
public class CheckSnglController {

    private static final Logger logger = LoggerFactory.getLogger(CheckSnglController.class);

    @Autowired
    CheckSnglService checkSnglService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    WhsDocMapper whsDocMapper;
    @Autowired
    MisUserServiceImpl misUserService;

    // 新增盘点单
    @RequestMapping(value = "insertCheckSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object insertCheckSngl(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);
        logger.info("url:whs/check_sngl/insertCheckSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            // 主表
            CheckSngl cSngl = BaseJson.getPOJO(jsonBody, CheckSngl.class);
            cSngl.setSetupPers(userName);
            if(!misUserService.isWhsPer(jsonBody,cSngl.getWhsEncd())){
                throw new RuntimeException("用户没有仓库权限");
            }
            // 子表
            List<CheckSnglSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckSnglSubTab.class);
            for (CheckSnglSubTab cSubTab : cList) {
                cSubTab.setCheckFormNum(cSngl.getCheckFormNum());
                cSubTab.setPrdcDt(StringUtils.trimToNull(cSubTab.getPrdcDt()));// 生产日期
                cSubTab.setInvldtnDt(StringUtils.trimToNull(cSubTab.getInvldtnDt()));// 失效日期

            }
            resp = checkSnglService.insertCheckSngl(userId, cSngl, cList,loginTime);
            misLogDAO.insertSelective(new MisLog("新增盘点单", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("新增盘点单", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglList", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改盘点单
    @RequestMapping(value = "updateCheckSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCheckSngl(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCheckSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            // 主表
            CheckSngl cSngl = BaseJson.getPOJO(jsonBody, CheckSngl.class);
            // 子表
            if(!misUserService.isWhsPer(jsonBody,cSngl.getWhsEncd())){
                throw new RuntimeException("用户没有仓库权限");
            }
            List<CheckSnglSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckSnglSubTab.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            cSngl.setMdfr(userName);

            for (CheckSnglSubTab cSubTab : cList) {
                cSubTab.setCheckFormNum(cSngl.getCheckFormNum());// 将主表主键提取插入子表
                cSubTab.setPrdcDt(StringUtils.trimToNull(cSubTab.getPrdcDt()));// 生产日期
                cSubTab.setInvldtnDt(StringUtils.trimToNull(cSubTab.getInvldtnDt()));// 失效日期
            }

            resp = checkSnglService.updateCheckSngl(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("修改盘点单", "仓库", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("修改盘点单", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCheckSngl", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除
    @RequestMapping(value = "deleteAllCheckSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllCheckSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/deleteAllCheckSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkSnglService.deleteAllCheckSngl(checkFormNum);
            misLogDAO.insertSelective(new MisLog("删除盘点单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除盘点单", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl/deleteAllCheckSngl", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查
    // 查询
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/query");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkSnglService.query(checkFormNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/query", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
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
                resp = BaseJson.returnRespObj("whs/check_sngl/queryList", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);

                resp = checkSnglService.queryList(map);

            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 通过仓库、盘点批号、存货大类编码、账面为零时是否盘点
    @RequestMapping(value = "selectCheckSnglList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectCheckSnglList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/selectCheckSnglList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
            if(!misUserService.isWhsPer(jsonBody,(String)map.get("whsEncd"))){
                throw new RuntimeException("用户没有仓库权限");
            }
            resp = checkSnglService.selectCheckSnglList(map);
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // PDA 查询 返回参数
    @RequestMapping(value = "checkSnglList", method = RequestMethod.POST)
    @ResponseBody
    public Object checkSnglList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/checkSnglList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = checkSnglService.checkSnglList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/check_sngl/checkSnglList", false, "用户无业务仓库", null);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/checkSnglList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // pda 根据子表序号
    @RequestMapping(value = "updateCheck", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCheck(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCheck");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            // 主表
            CheckSngl cSngl = BaseJson.getPOJO(jsonBody, CheckSngl.class);

            // 子表
            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<CheckSnglSubTab> cList = new ArrayList();
            for (Map map : cTabMap) {
                CheckSnglSubTab cSubTab = new CheckSnglSubTab();
                cSubTab.setCheckFormNum(cSngl.getCheckFormNum());// 将主表主键提取插入子表
                BeanUtils.populate(cSubTab, map);
                cList.add(cSubTab);
            }

            resp = checkSnglService.updateCheckTab(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("盘点pda 根据子表序号回传", "仓库", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("盘点pda 根据子表序号回传", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCheck", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 审核
    @RequestMapping(value = "updateCSnglChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCSnglChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));

            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // 审核
                try {

                    resp += checkSnglService.updateCSnglChk(userId, dbzhu, userName,
                            CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) + "\n";
                    misLogDAO.insertSelective(new MisLog("审核盘点单", "仓库", null, jsonBody, request, "成功", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("审核盘点单", "仓库", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglChk", isSuccess, resp, null);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglChk", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 弃审
    @RequestMapping(value = "updateCSnglNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCSnglNoChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        Boolean isSuccess = true;

        try {

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("登录失效，请重新登录"));
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // 弃审
                try {
                    resp += checkSnglService.updateCSnglNoChk(userId, dbzhu) + "\n";
                    misLogDAO.insertSelective(new MisLog("弃审盘点单", "仓库", null, jsonBody, request, "成功", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("弃审盘点单", "仓库", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglNoChk", isSuccess, resp, null);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglNoChk", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 下载之后锁定状态
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    private String updateStatus(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateStatus");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<CheckSngl> cList = new ArrayList();
            for (Map map : cTabMap) {
                CheckSngl cSngl = new CheckSngl();
                BeanUtils.populate(cSngl, map);
                cList.add(cSngl);
            }
            // 下载之后锁定状态
            resp = checkSnglService.updateStatus(cList);
            misLogDAO.insertSelective(new MisLog("下载之后锁定状态盘点单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("下载之后锁定状态盘点单", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/updateStatus", false, e.getMessage(), null);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
            if (map.get("checkDt2") !=null && !map.get("checkDt2").equals("")) {

                map.put("checkDt2", map.get("checkDt2").toString() + " 23:59:59");
            }
            if (map.get("checkDt1") !=null && !map.get("checkDt1").equals("")) {
                map.put("checkDt1", map.get("checkDt1").toString() + " 00:00:00");
            }
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/check_sngl/queryList", false, "用户没有仓库权限" , null);
            }else{
                map.put("whsId", whsId);

            resp = checkSnglService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("导出盘点单", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出盘点单", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入盘点单单据
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public Object uploadPursOrderFile(HttpServletRequest request) {
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
                    return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", false, "请选择文件", null);
                }
                return checkSnglService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }

    // 通过仓库、盘点批号、存货大类编码、账面为零时是否盘点
    @RequestMapping(value = "selectCheckSnglGdsBitList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectCheckSnglGdsBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/selectCheckSnglGdsBitList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);

            resp = checkSnglService.selectCheckSnglGdsBitList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglGdsBitList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    private String realWhsString(String whsEncd) {
        // TODO Auto-generated method stub
        List<String> aList = whsDocMapper.selectRealWhsList(getList(whsEncd));
        String s = "";
        for (int i = 0; i < aList.size(); i++) {
            if (i == aList.size() - 1) {
                s += aList.get(i);
            } else {
                s += aList.get(i) + ",";
            }
        }
        if (s.equals("")) {
            s = null;
        }
        return s;
    }

    /**
     * id放入list
     *
     * @param id id(多个已逗号分隔)
     * @return List集合
     */
    public List<String> getList(String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }

    // 导入盘点单单据
    @RequestMapping("uploadFileAddDbU8")
    @ResponseBody
    public Object uploadFileAddDbU8(HttpServletRequest request) {
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
                    return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", false, "请选择文件", null);
                }
                return checkSnglService.uploadFileAddDbU8(file);
            } else {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", false, "请选择文件。", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }
}
