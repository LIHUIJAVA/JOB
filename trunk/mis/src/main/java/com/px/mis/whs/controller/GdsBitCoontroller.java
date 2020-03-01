package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.service.GdsBitService;

//��λ����
@Controller
@RequestMapping("/whs/gds_bit")
public class GdsBitCoontroller {

    private static final Logger logger = LoggerFactory.getLogger(GdsBitCoontroller.class);

    @Autowired
    GdsBitService gdsBitService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // ������λ����
    @RequestMapping(value = "insertGdsBit")
    @ResponseBody
    public Object insertGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/insertGdsBit");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            GdsBit gBit = BaseJson.getPOJO(jsonBody, GdsBit.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            gBit.setSetupPers(userName);
            objectNode = gdsBitService.insertGdsBit(gBit);

            resp = BaseJson.returnRespObj("whs/gds_bit/insertGdsBit", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("������λ����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("������λ����", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("������λ����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit/insertGdsBit", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸Ļ�λ����
    @RequestMapping(value = "updateGdsBit")
    @ResponseBody
    public Object updateGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/updateGdsBit");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            GdsBit gBit = BaseJson.getPOJO(jsonBody, GdsBit.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            gBit.setMdfr(userName);
            objectNode = gdsBitService.updateGdsBit(gBit);

            resp = BaseJson.returnRespObj("whs/gds_bit/updateGdsBit", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�޸Ļ�λ����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("�޸Ļ�λ����", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸Ļ�λ����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit/updateGdsBit", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ����λ���� --
    @RequestMapping(value = "deleteGdsBit")
    @ResponseBody
    public Object deleteGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/deleteGdsBit");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            objectNode = gdsBitService.deleteGdsBit(gdsBitEncd);
            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBit", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("ɾ����λ����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("ɾ����λ����", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ����λ����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBit", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteGdsBitList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteGdsBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/deleteGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            resp = gdsBitService.deleteGdsBitList(gdsBitEncd);
            misLogDAO.insertSelective(new MisLog("������λ����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("������λ����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gds_bit/deleteGdsBitList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��λ����--
    @RequestMapping(value = "selectGdsBit")
    @ResponseBody
    public Object selectGdsBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/selectGdsBit");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            GdsBit gdsBit = gdsBitService.selectGdsBit(gdsBitEncd);
            if (gdsBit != null) {
                resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBit", true, "����ɹ���", gdsBit);
            } else {
                resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBit", false, "����ʧ�ܣ�", gdsBit);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBit", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��λ���� list
    @RequestMapping(value = "selectGdsBitList")
    @ResponseBody
    public Object selectGdsBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/selectGdsBitList");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdsBitEncd = reqBody.get("gdsBitEncd").asText();

            List<GdsBit> gList = gdsBitService.selectGdsBitList(gdsBitEncd);
            if (gList != null) {
                resp = BaseJson.returnRespObjList("whs/gds_bit/selectGdsBitList", true, "����ɹ���", null, gList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gds_bit/selectGdsBitList", false, "����ʧ�ܣ�", null, gList);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit/selectGdsBitList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ��ҳ��
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = gdsBitService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ���л�λ����
    @RequestMapping(value = "selectgTypList", method = RequestMethod.POST)
    @ResponseBody
    private String selectgTypList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/selectgTypList");
        String resp = "";
        try {

            resp = gdsBitService.selectgTypList();
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gds_bit/selectgTypList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = gdsBitService.queryListDaYin(map);
            misLogDAO.insertSelective(new MisLog("������λ����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("������λ����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gds_bit/queryListDaYin", false, e.getMessage(), null);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �����λ����
    @RequestMapping("uploadGdsBitFile")
    @ResponseBody
    public Object uploadGdsBitFile(HttpServletRequest request) {
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
                return gdsBitService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/gds_bit/uploadGdsBitFile", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

}
