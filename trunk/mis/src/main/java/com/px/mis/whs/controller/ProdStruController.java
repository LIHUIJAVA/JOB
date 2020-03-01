package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.px.mis.util.CommonUtil;
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


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.px.mis.whs.service.ProdStruService;

//产品结构的单据
@Controller
@RequestMapping("/whs/prod_stru")
public class ProdStruController {

    private static final Logger logger = LoggerFactory.getLogger(ProdStruController.class);

    @Autowired
    ProdStruService prodStruService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // 新增的产品结构
    @RequestMapping(value = "insertProdStru", method = RequestMethod.POST)
    @ResponseBody
    public Object insertProdStru(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/insertProdStru");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            // 主表

            ProdStru pStru = BaseJson.getPOJO(jsonBody, ProdStru.class);

            pStru.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());
            // 子表

            List<ProdStruSubTab> aList = BaseJson.getPOJOList(jsonBody, ProdStruSubTab.class);

            for (ProdStruSubTab subTab : aList) {
                subTab.setMomEncd(pStru.getMomEncd());
            }

            resp = prodStruService.insertProdStru(pStru, aList);
            misLogDAO.insertSelective(new MisLog("新增的产品结构", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("新增的产品结构", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 修改产品结构
    @RequestMapping(value = "updateProdStru", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProdStru(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/updateProdStru");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            // 主表
            ProdStru pStru = BaseJson.getPOJO(jsonBody, ProdStru.class);
            // 子表
            pStru.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            List<ProdStruSubTab> aList = BaseJson.getPOJOList(jsonBody, ProdStruSubTab.class);
            for (ProdStruSubTab subTab : aList) {
                subTab.setMomEncd(pStru.getMomEncd());
            }

            resp = prodStruService.updateProdStru(pStru, aList);
            misLogDAO.insertSelective(new MisLog("修改产品结构", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("修改产品结构", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/updateProdStru", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 删除产品结构
    // 批量删除
    @RequestMapping(value = "deleteAllProdStru", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllProdStru(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);

        logger.info("url:whs/prod_stru/deleteAllProdStru");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
//		"1,2,3,4,56"
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String ordrNum = reqBody.get("ordrNum").asText();
            resp = prodStruService.deleteAllProdStru(ordrNum);
            misLogDAO.insertSelective(new MisLog("删除产品结构", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("删除产品结构", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/deleteAllProdStru", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 简单查 产品结构
    // 查询
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/query");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            resp = prodStruService.query(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/prod_stru/query", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 分页查
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/queryList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = prodStruService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/prod_stru/query", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 打印
    @RequestMapping(value = "queryListPrint", method = RequestMethod.POST)
    @ResponseBody
    private String queryListPrint(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/queryListPrint");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = prodStruService.queryListPrint(map);
            misLogDAO.insertSelective(new MisLog("导出产品结构", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("导出产品结构", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/queryListPrint", false, e.getMessage(), null);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 通过母件查询信息
    @RequestMapping(value = "selectProdStruByMom", method = RequestMethod.POST)
    @ResponseBody
    private String selectProdStruByMom(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/selectProdStruByMom");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);
            String ordrNum = reqBody.get("momEncd").asText();
            resp = prodStruService.selectProdStruByMom(ordrNum);

        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/prod_stru/selectProdStruByMom", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 通过母件组装
    @RequestMapping(value = "queryAmbDisambSngl", method = RequestMethod.POST)
    @ResponseBody
    private String queryAmbDisambSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/queryAmbDisambSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = prodStruService.queryAmbDisambSngl(map);

        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/prod_stru/queryAmbDisambSngl", false, e.getMessage(), null);

        }

        logger.info("返回参数：" + resp);
        return resp;
    }

    // 审核
    @RequestMapping(value = "updatePStruChk", method = RequestMethod.POST)
    @ResponseBody
    private String updatePStruChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/updatePStruChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<ProdStru> cList = new ArrayList();
            for (Map map : cTabMap) {
                ProdStru pStru = new ProdStru();
                BeanUtils.populate(pStru, map);
                pStru.setChkr(BaseJson.getReqHead(jsonBody).get("userName").asText());
                pStru.setChkTm(CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText()));
                cList.add(pStru);
            }
            // 审核
            resp = prodStruService.updatePStruChk(cList);
            misLogDAO.insertSelective(new MisLog("审核产品结构", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("审核产品结构", "仓库", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/updatePStruChk", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 弃审
    @RequestMapping(value = "updatePStruNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updatePStruNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/updatePStruNoChk");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<ProdStru> cList = new ArrayList();
            for (Map map : cTabMap) {
                ProdStru pStru = new ProdStru();
                BeanUtils.populate(pStru, map);
                cList.add(pStru);
            }
            // 弃审
            resp = prodStruService.updatePStruNoChk(cList);
            misLogDAO.insertSelective(new MisLog("弃审产品结构", "仓库", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("弃审产品结构", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/prod_stru/updatePStruNoChk", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 导入产品结构档案
    @RequestMapping("uploadRegnFile")
    @ResponseBody
    public Object uploadRegnFile(HttpServletRequest request) {
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
                return prodStruService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/prod_stru/uploadRegnFile", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

}
