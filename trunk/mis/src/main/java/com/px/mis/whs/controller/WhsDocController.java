package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;
import com.px.mis.whs.service.WhsDocService;

//仓库档案的单据
@Controller
@RequestMapping("/whs/whs_doc")
public class WhsDocController {

    private static final Logger logger = LoggerFactory.getLogger(WhsDocController.class);

    @Autowired
    WhsDocService whsDocService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MisUserServiceImpl misUserService;

    // 添加仓库档案
    @RequestMapping(value = "insertWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object insertWhsDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertWhsDoc");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            WhsDoc wDoc = BaseJson.getPOJO(jsonBody, WhsDoc.class);
            wDoc.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.insertWhsDoc(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertWhsDoc", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增仓库档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("新增仓库档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增仓库档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertWhsDoc", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改仓库档案
    @RequestMapping(value = "updateWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object updateWhsDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/updateWhsDoc");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            WhsDoc wDoc = BaseJson.getPOJO(jsonBody, WhsDoc.class);
            wDoc.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.updateWhsDoc(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/updateWhsDoc", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("修改仓库档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("修改仓库档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改仓库档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/updateWhsDoc", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除仓库档案 --
    @RequestMapping(value = "deleteWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWhsDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteWhsDoc");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            objectNode = whsDocService.deleteWhsDoc(whsEncd);
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsDoc", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("删除仓库档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("删除仓库档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除仓库档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsDoc", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除仓库档案
    // 批量删除
    @RequestMapping(value = "deleteWDocList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWDocList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteWDocList");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            resp = whsDocService.deleteWDocList(whsEncd);
            misLogDAO.insertSelective(new MisLog("批量删除仓库档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("批量删除仓库档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWDocList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 仓库档案 --
    @RequestMapping(value = "selectWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsDoc(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/whs_doc/selectWhsDoc");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            if(!misUserService.isWhsPer(jsonBody,whsEncd)){
                throw new RuntimeException("用户没有仓库权限");
            }
            WhsDoc whsDoc = whsDocService.selectWhsDoc(whsEncd);
            if (whsDoc != null) {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDoc", true, "处理成功！", whsDoc);
            } else {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDoc", false, "处理失败！", whsDoc);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDoc", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 仓库档案List
    @RequestMapping(value = "selectWhsDocList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsDocList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsDocList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();

            reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            if(!misUserService.isWhsPer(jsonBody,whsEncd)){
                throw new RuntimeException("用户没有仓库权限");
            }
            resp = whsDocService.selectWhsDocList(whsEncd, userId);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/whs_doc/queryList", false, "用户没有仓库权限" , null);
            }else {
                map.put("whsId", whsId);

//                misUserService.selectUserWhs(jsonBody, map);
                resp = whsDocService.queryList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/queryList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/whs_doc/queryListDaYin", false, "用户没有仓库权限" , null);
            }else {
                map.put("whsId", whsId);

            resp = whsDocService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("导出仓库档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出仓库档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/queryListDaYin", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 省 市 县 不要了
    @RequestMapping(value = "selectCity", method = RequestMethod.POST)
    @ResponseBody
    private String selectCity(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectCity");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            City city = null;

            city = BaseJson.getPOJO(jsonBody, City.class);

            resp = whsDocService.selectCity(city);
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/whs_doc/selectCity", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 省
    @RequestMapping(value = "selectProvinces", method = RequestMethod.POST)
    @ResponseBody
    private String selectProvinces(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectProvinces");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            resp = whsDocService.selectProvinces();

        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/whs_doc/selectProvinces", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 市
    @RequestMapping(value = "selectCities", method = RequestMethod.POST)
    @ResponseBody
    private String selectCities(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectCities");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody = null;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);

            String superiorCode = reqBody.get("codeId").asText();

            resp = whsDocService.selectCities(superiorCode);
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/whs_doc/selectCities", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 县
    @RequestMapping(value = "selectCounties", method = RequestMethod.POST)
    @ResponseBody
    private String selectCounties(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectCounties");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody = null;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);

            String superiorCode = reqBody.get("codeId").asText();

            resp = whsDocService.selectCounties(superiorCode);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectCities", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 计价方式
    @RequestMapping(value = "selectValtnMode", method = RequestMethod.POST)
    @ResponseBody
    private String selectValtnMode(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectValtnMode");
        String resp = "";
        try {

            resp = whsDocService.selectValtnMode();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectValtnMode", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 仓库属性
    @RequestMapping(value = "selectWhsAttr", method = RequestMethod.POST)
    @ResponseBody
    private String selectWhsAttr(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsAttr");
        String resp = "";
        try {

            resp = whsDocService.selectWhsAttr();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsAttr", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 可用量控制方式
    @RequestMapping(value = "selectAMode", method = RequestMethod.POST)
    @ResponseBody
    private String selectAMode(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectAMode");
        String resp = "";
        try {

            resp = whsDocService.selectAMode();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectAMode", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;

    }

    // 导入盘点单单据
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public Object uploadPursOrderFile(HttpServletRequest request) {
        String resp = "";
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
                    return BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", false, "请选择文件。", null);
                }

                return whsDocService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", false, "请选择文件。", null);

            }
        } catch (Exception e) {


            try {
                resp = BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return resp;
    }

    // 添加用户仓库
    @RequestMapping(value = "insertUserWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object insertUserWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertUserWhs");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            List<UserWhs> userWhs = new ArrayList<>();
            List<String> list = getList((String) map.get("realWhs"));
            for (String string : list) {
                UserWhs e = new UserWhs();
                e.setAccNum((String) map.get("accNum"));
                e.setRealWhs(string);
                userWhs.add(e);
            }

            objectNode = whsDocService.insertUserWhs(userWhs);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertUserWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增用户仓库对应关系", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("新增用户仓库对应关系", "仓库", null, jsonBody, request, "失败",
                        objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增用户仓库对应关系", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertUserWhs", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
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

    // 修改用户仓库
    @RequestMapping(value = "updateUserWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object updateUserWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/updateUserWhs");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            UserWhs wDoc = BaseJson.getPOJO(jsonBody, UserWhs.class);
            objectNode = whsDocService.updateUserWhs(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/updateUserWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("修改用户仓库对应关系", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("修改用户仓库对应关系", "仓库", null, jsonBody, request, "失败",
                        objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改用户仓库对应关系", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/updateUserWhs", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除仓库档案
    // 批量删除
    @RequestMapping(value = "deleteUserWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteUserWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteUserWhsList");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String id = reqBody.get("id").asText();
            resp = whsDocService.deleteUserWhsList(id);
            misLogDAO.insertSelective(new MisLog("批量删除用户仓库对应关系", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("批量删除用户仓库对应关系", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteUserWhsList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查用户仓库对应关系List
    @RequestMapping(value = "selectUserWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectUserWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectUserWhsList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = whsDocService.selectUserWhsList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectUserWhsList", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 添加仓库关系
    @RequestMapping(value = "insertWhsGds", method = RequestMethod.POST)
    @ResponseBody
    public Object insertLogicRealWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertWhsGds");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            List<WhsGds> userWhs = new ArrayList<>();
            WhsGds e = new WhsGds();
            e.setGdsBitEncd((String) map.get("gdsBitEncd"));
            e.setRealWhs((String) map.get("realWhs"));
            e.setRegnEncd((String) map.get("regnEncd"));
            userWhs.add(e);

            objectNode = whsDocService.insertWhsGds(userWhs);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertWhsGds", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增仓库关系", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("新增仓库关系", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增仓库关系", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertLogicRealWhs", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除仓库关系
    // 批量删除
    @RequestMapping(value = "deleteWhsGds", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWhsGds(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteWhsGds");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String id = reqBody.get("id").asText();
            List<String> list = getList(id);
            resp = whsDocService.deleteWhsGds(list);

            misLogDAO.insertSelective(new MisLog("批量删除仓库关系", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("批量删除仓库关系", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsGds", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查仓库关系List
    @RequestMapping(value = "selectWhsGds", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsGds(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsGds");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));
            map.put("realWhs", getList((String) map.get("realWhs")));
            map.put("regnEncd", getList((String) map.get("regnEncd")));

            resp = whsDocService.selectWhsGdsList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsGds", false, e.getMessage(), null);
        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // Pda简单查仓库关系List
    @RequestMapping(value = "selectWhsGdsRealList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsGdsRealList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsGdsRealList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
//			map.put("realWhs", getList((String) map.get("realWhs")));
//			map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));
//			map.put("regnEncd", getList((String) map.get("regnEncd")));
            map.put("whsEncd", getList((String) map.get("whsEncd")));
            resp = whsDocService.selectWhsGdsRealList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsGds", false, e.getMessage(), null);
        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 添加仓库档案
    @RequestMapping(value = "insertRealWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object insertRealWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertRealWhs");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            RealWhs wDoc = BaseJson.getPOJO(jsonBody, RealWhs.class);
            wDoc.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.insertRealWhs(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增总仓库", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("新增总仓库", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增总仓库", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhs", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改仓库档案
    @RequestMapping(value = "updateRealWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRealWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/updateRealWhs");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            RealWhs wDoc = BaseJson.getPOJO(jsonBody, RealWhs.class);
            wDoc.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.updateRealWhs(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/updateRealWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("修改总仓库档案", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("修改总仓库档案", "仓库", null, jsonBody, request, "失败", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("修改总仓库档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/updateRealWhs", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除仓库档案
    // 批量删除
    @RequestMapping(value = "deleteRealWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteRealWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteRealWhsList");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("realWhs").asText();
            resp = whsDocService.deleteRealWhsList(whsEncd);
            misLogDAO.insertSelective(new MisLog("批量删除仓库档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("批量删除仓库档案", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteRealWhsList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 仓库档案 --
    @RequestMapping(value = "selectRealWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRealWhs(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/whs_doc/selectRealWhs");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String realWhs = reqBody.get("realWhs").asText();
            RealWhs whsDoc = whsDocService.selectRealWhs(realWhs);
            if (whsDoc != null) {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhs", true, "处理成功！", whsDoc);
            } else {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhs", false, "处理失败！", whsDoc);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhs", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查
    @RequestMapping(value = "queryRealWhsList", method = RequestMethod.POST)
    @ResponseBody
    private String queryRealWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryRealWhsList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = whsDocService.queryRealWhsList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/queryRealWhsList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryRealWhsListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryRealWhsListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryRealWhsListDaYin");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = whsDocService.queryRealWhsListDaYin(map);
            misLogDAO.insertSelective(new MisLog("导出总仓库档案", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出总仓库档案", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/queryRealWhsListDaYin", false, e.getMessage(), null);


        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 仓库和总仓

    // 添加RealWhsMap
    @RequestMapping(value = "insertRealWhsMap", method = RequestMethod.POST)
    @ResponseBody
    public Object insertRealWhsMap(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertRealWhsMap");
        logger.info("请求参数：" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            RealWhsMap realWhsMap = new RealWhsMap();
            realWhsMap.setWhsEncd((String) map.get("whsEncd"));
            realWhsMap.setRealWhs((String) map.get("realWhs"));

            objectNode = whsDocService.insertRealWhsMap(realWhsMap);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhsMap", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("新增仓库和总仓关系", "仓库", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("新增仓库和总仓关系", "仓库", null, jsonBody, request, "失败",
                        objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增仓库和总仓关系", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhsMap", false, e.getMessage(), null);


        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 批量删除仓库关系
    // 批量删除
    @RequestMapping(value = "deleteRealWhsMap", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteRealWhsMap(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteRealWhsMap");
        logger.info("请求参数：" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);

            List<String> list = getList(reqBody.get("id").asText());

            resp = whsDocService.deleteRealWhsMap(list);

            misLogDAO.insertSelective(new MisLog("批量删除仓库和总仓关系", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("批量删除仓库和总仓关系", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteRealWhsMap", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查仓库关系List
    @RequestMapping(value = "selectRealWhsMap", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRealWhsMap(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectRealWhsMap");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            map.put("whsEncd", getList((String) map.get("whsEncd")));
            map.put("realWhs", getList((String) map.get("realWhs")));

            resp = whsDocService.selectRealWhsMap(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhsMap", false, e.getMessage(), null);
        }

        logger.info("返回参数：" + resp);
        return resp;
    }

}
