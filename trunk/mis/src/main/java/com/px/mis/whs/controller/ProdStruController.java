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

//��Ʒ�ṹ�ĵ���
@Controller
@RequestMapping("/whs/prod_stru")
public class ProdStruController {

    private static final Logger logger = LoggerFactory.getLogger(ProdStruController.class);

    @Autowired
    ProdStruService prodStruService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // �����Ĳ�Ʒ�ṹ
    @RequestMapping(value = "insertProdStru", method = RequestMethod.POST)
    @ResponseBody
    public Object insertProdStru(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/insertProdStru");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // ����

            ProdStru pStru = BaseJson.getPOJO(jsonBody, ProdStru.class);

            pStru.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());
            // �ӱ�

            List<ProdStruSubTab> aList = BaseJson.getPOJOList(jsonBody, ProdStruSubTab.class);

            for (ProdStruSubTab subTab : aList) {
                subTab.setMomEncd(pStru.getMomEncd());
            }

            resp = prodStruService.insertProdStru(pStru, aList);
            misLogDAO.insertSelective(new MisLog("�����Ĳ�Ʒ�ṹ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����Ĳ�Ʒ�ṹ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸Ĳ�Ʒ�ṹ
    @RequestMapping(value = "updateProdStru", method = RequestMethod.POST)
    @ResponseBody
    public Object updateProdStru(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/updateProdStru");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // ����
            ProdStru pStru = BaseJson.getPOJO(jsonBody, ProdStru.class);
            // �ӱ�
            pStru.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            List<ProdStruSubTab> aList = BaseJson.getPOJOList(jsonBody, ProdStruSubTab.class);
            for (ProdStruSubTab subTab : aList) {
                subTab.setMomEncd(pStru.getMomEncd());
            }

            resp = prodStruService.updateProdStru(pStru, aList);
            misLogDAO.insertSelective(new MisLog("�޸Ĳ�Ʒ�ṹ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("�޸Ĳ�Ʒ�ṹ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/updateProdStru", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ����Ʒ�ṹ
    // ����ɾ��
    @RequestMapping(value = "deleteAllProdStru", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllProdStru(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);

        logger.info("url:whs/prod_stru/deleteAllProdStru");
        logger.info("���������" + jsonBody);
        String resp = "";
//		"1,2,3,4,56"
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String ordrNum = reqBody.get("ordrNum").asText();
            resp = prodStruService.deleteAllProdStru(ordrNum);
            misLogDAO.insertSelective(new MisLog("ɾ����Ʒ�ṹ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ����Ʒ�ṹ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/deleteAllProdStru", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��Ʒ�ṹ
    // ��ѯ
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/query");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            resp = prodStruService.query(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/prod_stru/query", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ҳ��
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/queryList");
        logger.info("���������" + jsonBody);
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
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListPrint", method = RequestMethod.POST)
    @ResponseBody
    private String queryListPrint(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/queryListPrint");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = prodStruService.queryListPrint(map);
            misLogDAO.insertSelective(new MisLog("������Ʒ�ṹ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("������Ʒ�ṹ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/queryListPrint", false, e.getMessage(), null);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ͨ��ĸ����ѯ��Ϣ
    @RequestMapping(value = "selectProdStruByMom", method = RequestMethod.POST)
    @ResponseBody
    private String selectProdStruByMom(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/selectProdStruByMom");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);
            String ordrNum = reqBody.get("momEncd").asText();
            resp = prodStruService.selectProdStruByMom(ordrNum);

        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/prod_stru/selectProdStruByMom", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ͨ��ĸ����װ
    @RequestMapping(value = "queryAmbDisambSngl", method = RequestMethod.POST)
    @ResponseBody
    private String queryAmbDisambSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/prod_stru/queryAmbDisambSngl");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = prodStruService.queryAmbDisambSngl(map);

        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/prod_stru/queryAmbDisambSngl", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���
    @RequestMapping(value = "updatePStruChk", method = RequestMethod.POST)
    @ResponseBody
    private String updatePStruChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/updatePStruChk");
        logger.info("���������" + jsonBody);
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
            // ���
            resp = prodStruService.updatePStruChk(cList);
            misLogDAO.insertSelective(new MisLog("��˲�Ʒ�ṹ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("��˲�Ʒ�ṹ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/prod_stru/updatePStruChk", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����
    @RequestMapping(value = "updatePStruNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updatePStruNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/prod_stru/updatePStruNoChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<ProdStru> cList = new ArrayList();
            for (Map map : cTabMap) {
                ProdStru pStru = new ProdStru();
                BeanUtils.populate(pStru, map);
                cList.add(pStru);
            }
            // ����
            resp = prodStruService.updatePStruNoChk(cList);
            misLogDAO.insertSelective(new MisLog("�����Ʒ�ṹ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("�����Ʒ�ṹ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/prod_stru/updatePStruNoChk", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �����Ʒ�ṹ����
    @RequestMapping("uploadRegnFile")
    @ResponseBody
    public Object uploadRegnFile(HttpServletRequest request) {
        try {
            // ����һ��ͨ�õĶಿ�ֽ�����
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                    request.getSession().getServletContext());
            // �ж� request �Ƿ����ļ��ϴ�,���ಿ������
            if (multipartResolver.isMultipart(request)) {
                // ת���ɶಿ��request
                MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;// requestǿ��ת��ע��
                MultipartFile file = mRequest.getFile("file");
                if (file == null) {
                    return ("��ѡ���ļ���");
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
