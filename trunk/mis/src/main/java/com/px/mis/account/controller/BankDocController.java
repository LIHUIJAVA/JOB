package com.px.mis.account.controller;

import java.io.IOException;
import java.util.ArrayList;
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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.BankDoc;
import com.px.mis.account.service.BankDocService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//���е����ӿ�
@RequestMapping(value = "account/bankDoc", method = RequestMethod.POST)
@Controller
public class BankDocController {
    private Logger logger = LoggerFactory.getLogger(BankDocController.class); 
    @Autowired
    private BankDocService bds;

    /* ��� */
    @RequestMapping("insertBankDoc")
    public @ResponseBody Object insertBankDoc(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/insertBankDoc");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        BankDoc bankDoc = null;
        try {
            bankDoc = BaseJson.getPOJO(jsonData, BankDoc.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/insertBankDoc �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/insertBankDoc", false, "���������������������������Ƿ���д��ȷ������쳣��",
                        null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/insertBankDoc �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = bds.insertBankDoc(bankDoc);
            if (on == null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/insertBankDoc", false, "����쳣��", null);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/insertBankDoc", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/insertBankDoc �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ɾ�� */
    @RequestMapping("deleteBankDoc")
    public @ResponseBody Object deleteBankDoc(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/deleteBankDoc");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        String bankEncd = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            bankEncd = reqBody.get("bankEncd").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/deleteBankDoc �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/deleteBankDoc", false,
                        "���������������������������Ƿ���д��ȷ��delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/deleteBankDoc �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = bds.deleteBankDocByOrdrNum(bankEncd);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/deleteBankDoc", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/deleteBankDoc", false, "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/deleteBankDoc �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ���� */
    @RequestMapping("updateBankDoc")
    public @ResponseBody Object updateBankDoc(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/updateBankDoc");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<BankDoc> bankDocList = new ArrayList<BankDoc>();
        try {
        	ObjectNode jsonObject = JacksonUtil.getObjectNode(jsonData);
            JsonNode jsonObjectList = jsonObject.get("reqBody");
            JsonNode jsonArray = jsonObjectList.get("list");
            bankDocList = BaseJson.getPOJOList(jsonObjectList.toString(),BankDoc.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/updateBankDoc �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/updateBankDoc", false,
                        "���������������������������Ƿ���д��ȷ��update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/updateBankDoc �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = bds.updateBankDocByordrNum(bankDocList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/updateBankDoc", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/updateBankDoc", false, "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/updateBankDoc �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectBankDoc")
    public @ResponseBody Object selectBankDoc(@RequestBody String jsonBody) {
        logger.info("url:/account/bankDoc/selectBankDoc");
        logger.info("���������" + jsonBody);
        String resp = "";
        Map map = null;
        Integer pageNo = null;
        Integer pageSize = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            pageNo = (int) map.get("pageNo");
            pageSize = (int) map.get("pageSize");
            if (pageNo == 0 || pageSize == 0) {
                resp = BaseJson.returnRespList("/account/bankDoc/selectBankDoc", false,
                        "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDoc �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/bankDoc/selectBankDoc", false,
                        "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/selectBankDoc �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = bds.selectBankDocList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDoc �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectBankDocById")
    public @ResponseBody Object selectBankDocById(@RequestBody String jsonData) {
        logger.info("url:/account/bankDoc/selectBankDocById");
        logger.info("���������" + jsonData);
        String resp = "";
        String bankEncd = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            bankEncd = reqBody.get("bankEncd").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocById �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/bankDoc/selectBankDocById", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/selectBankDocById �쳣˵����", e);
            }
            return resp;
        }
        try {
            BankDoc bankDoc = bds.selectBankDocByordrNum(bankEncd);
            if (bankDoc != null) {
                resp = BaseJson.returnRespObj("/account/bankDoc/selectBankDocById", true, "����ɹ���", bankDoc);
            } else {
                resp = BaseJson.returnRespObj("/account/bankDoc/selectBankDocById", false, "û�в鵽���ݣ�", bankDoc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocById �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���д�ӡ */
    @RequestMapping(value = "selectBankDocPrint")
    public @ResponseBody Object selectBankDocPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/bankDoc/selectBankDocPrint");
        logger.info("���������" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocPrint �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/bankDoc/selectBankDocPrint", false,
                        "���������������������������Ƿ���д��ȷ����ҳ��ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/bankDoc/selectBankDocPrint �쳣˵����", e);
            }
            return resp;
        }
        try {

            resp = bds.selectBankDocPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/bankDoc/selectBankDocPrint �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���뵥��
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public String uploadPursOrderFile(HttpServletRequest request) throws IOException {
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
                    return BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", false, "��ѡ���ļ���", null);
                }
                return bds.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/bankDoc/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
