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
import com.px.mis.whs.entity.GdFlowCorp;
import com.px.mis.whs.service.GdFlowCorpService;

//������˾�ĵ���
@Controller
@RequestMapping("/whs/gd_flow_crop")
public class GdFlowCorpController {

    private static final Logger logger = LoggerFactory.getLogger(GdFlowCorpController.class);

    @Autowired
    GdFlowCorpService gdFlowCorpService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // ����������˾
    @RequestMapping(value = "insertGdFlowCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object insertGdFlowCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/insertGdFlowCorp");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            GdFlowCorp gCorp = BaseJson.getPOJO(jsonBody, GdFlowCorp.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            gCorp.setSetupPers(userName);
            objectNode = gdFlowCorpService.insertGdFlowCorp(gCorp);

            resp = BaseJson.returnRespObj("whs/gd_flow_crop/insertGdFlowCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("����������˾����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("����������˾����", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����������˾����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gd_flow_crop/insertGdFlowCorp", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸�������˾
    @RequestMapping(value = "updateGdFlowCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object updateGdFlowCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/updateGdFlowCorp");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            GdFlowCorp gCorp = BaseJson.getPOJO(jsonBody, GdFlowCorp.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            gCorp.setMdfr(userName);
            objectNode = gdFlowCorpService.updateGdFlowCorp(gCorp);

            resp = BaseJson.returnRespObj("whs/gd_flow_crop/updateGdFlowCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�޸�������˾����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("�޸�������˾����", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸�������˾����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gd_flow_crop/updateGdFlowCorp", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ��������˾--
    @RequestMapping(value = "deleteGdFlowCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteGdFlowCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/deleteGdFlowCorp");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdFlowEncd = reqBody.get("gdFlowEncd").asText();
            objectNode = gdFlowCorpService.deleteGdFlowCorp(gdFlowEncd);
            resp = BaseJson.returnRespObj("whs/gd_flow_crop/deleteGdFlowCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("ɾ��������˾����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("ɾ��������˾����", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ��������˾����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gd_flow_crop/updateGdFlowCorp", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteGFlowCorpList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteGFlowCorpList(@RequestBody String jsonBody) throws IOException {

        // System.out.println(jsonBody);

        logger.info("url:whs/gd_flow_crop/deleteGFlowCorpList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdFlowEncd = reqBody.get("gdFlowEncd").asText();
            resp = gdFlowCorpService.deleteGFlowCorpList(gdFlowEncd);
            misLogDAO.insertSelective(new MisLog("����ɾ��������˾����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����ɾ��������˾����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gd_flow_crop/deleteGFlowCorpList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ������˾--
    @RequestMapping(value = "selectGdFlowCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object selectGdFlowCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/selectGdFlowCorp");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdFlowEncd = reqBody.get("gdFlowEncd").asText();
            GdFlowCorp gCorp = gdFlowCorpService.selectGdFlowCorp(gdFlowEncd);
            if (gCorp != null) {
                resp = BaseJson.returnRespObj("whs/gd_flow_crop/selectGdFlowCorp", true, "����ɹ���", gCorp);
            } else {
                resp = BaseJson.returnRespObj("whs/gd_flow_crop/selectGdFlowCorp", false, "����ʧ�ܣ�", gCorp);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gd_flow_crop/selectGdFlowCorp", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ������˾ list
    @RequestMapping(value = "selectGdFlowCorpList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectGdFlowCorpList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/selectGdFlowCorpList");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String gdFlowEncd = reqBody.get("gdFlowEncd").asText();
            List<GdFlowCorp> gCorpList = gdFlowCorpService.selectGdFlowCorpList(gdFlowEncd);
            if (gCorpList != null) {
                resp = BaseJson.returnRespObjList("whs/gd_flow_crop/selectGdFlowCorpList", true, "����ɹ���", null,
                        gCorpList);
            } else {
                resp = BaseJson.returnRespObjList("whs/gd_flow_crop/selectGdFlowCorpList", false, "����ʧ�ܣ�", null,
                        gCorpList);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gd_flow_crop/selectGdFlowCorpList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ҳ��
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = gdFlowCorpService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gd_flow_crop/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gd_flow_crop/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = gdFlowCorpService.queryListDaYin(map);
            misLogDAO.insertSelective(new MisLog("����������˾����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����������˾����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/gd_flow_crop/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �������򵵰�
    @RequestMapping("uploadGdFlowCropFile")
    @ResponseBody
    public Object uploadGdFlowCropFile(HttpServletRequest request) {
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
                return gdFlowCorpService.uploadFileAddDb(file);
            }
        } catch (Exception e) {

            try {
                return BaseJson.returnRespObj("whs/gd_flow_crop/uploadGdFlowCropFile", false, "�����ļ���ʽ�����޷����룡", null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

}
