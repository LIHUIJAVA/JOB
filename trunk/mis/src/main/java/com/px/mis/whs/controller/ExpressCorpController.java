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
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.service.ExpressCorpService;

//��ݹ�˾�ĵ���
@Controller
@RequestMapping("/whs/express_crop")
public class ExpressCorpController {

    private static final Logger logger = LoggerFactory.getLogger(ExpressCorpController.class);

    @Autowired
    ExpressCorpService expressCorpService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    // ������ݹ�˾
    @RequestMapping(value = "insertExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object insertExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/insertExpressCorp");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ExpressCorp eCorp = BaseJson.getPOJO(jsonBody, ExpressCorp.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            eCorp.setSetupPers(userName);
            objectNode = expressCorpService.insertExpressCorp(eCorp);

            resp = BaseJson.returnRespObj("whs/express_crop/insertExpressCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("������ݹ�˾����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("������ݹ�˾����", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("������ݹ�˾����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/express_crop/insertExpressCorp", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸Ŀ�ݹ�˾
    @RequestMapping(value = "updateExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object updateExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/updateExpressCorp");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ExpressCorp eCorp = BaseJson.getPOJO(jsonBody, ExpressCorp.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            eCorp.setMdfr(userName);
            objectNode = expressCorpService.updateExpressCorp(eCorp);

            resp = BaseJson.returnRespObj("whs/express_crop/updateExpressCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�޸Ŀ�ݹ�˾����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("�޸Ŀ�ݹ�˾����", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸Ŀ�ݹ�˾����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/express_crop/updateExpressCorp", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ����ݹ�˾--
    @RequestMapping(value = "deleteExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/deleteExpressCorp");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            objectNode = expressCorpService.deleteExpressCorp(expressEncd);

            resp = BaseJson.returnRespObj("whs/express_crop/deleteExpressCorp", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("ɾ����ݹ�˾����", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("ɾ����ݹ�˾����", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));

            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ����ݹ�˾����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/express_crop/deleteExpressCorp", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteECorpList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteECorpList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/deleteECorpList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            resp = expressCorpService.deleteECorpList(expressEncd);
            misLogDAO.insertSelective(new MisLog("����ɾ����ݹ�˾����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("����ɾ����ݹ�˾����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/express_crop/deleteECorpList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��ݹ�˾--
    @RequestMapping(value = "selectExpressCorp", method = RequestMethod.POST)
    @ResponseBody
    public Object selectExpressCorp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/selectExpressCorp");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            ExpressCorp wCorp = expressCorpService.selectExpressCorp(expressEncd);
            if (wCorp != null) {
                resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorp", true, "����ɹ���", wCorp);
            } else {
                resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorp", false, "����ʧ�ܣ�", wCorp);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorp", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ ��ݹ�˾ list
    @RequestMapping(value = "selectExpressCorpList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectExpressCorpList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/selectExpressCorpList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String expressEncd = reqBody.get("expressEncd").asText();
            List<ExpressCorp> eList = expressCorpService.selectExpressCorpList(expressEncd);
            if (eList != null) {
                resp = BaseJson.returnRespObjList("whs/express_crop/selectExpressCorpList", true, "����ɹ���", null, eList);
            } else {
                resp = BaseJson.returnRespObjList("whs/express_crop/selectExpressCorpList", false, "����ʧ�ܣ�", null,
                        eList);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/express_crop/selectExpressCorpList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ҳ��
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = expressCorpService.queryList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/express_crop/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = expressCorpService.queryListDaYin(map);
            misLogDAO.insertSelective(new MisLog("������ݹ�˾����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("������ݹ�˾����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/express_crop/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �������򵵰�
    @RequestMapping("uploadExpressCorpFile")
    @ResponseBody
    public Object uploadExpressCorpFile(HttpServletRequest request) {
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
                return expressCorpService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/express_crop/uploadExpressCorpFile", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;
    }

    // ����express_code_and_name
    @RequestMapping(value = "queryExpressCodeAndNameList", method = RequestMethod.POST)
    @ResponseBody
    private String queryExpressCodeAndNameList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/express_crop/queryExpressCodeAndNameList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            resp = expressCorpService.queryExpressCodeAndNameList();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/express_crop/queryExpressCodeAndNameList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }
}
