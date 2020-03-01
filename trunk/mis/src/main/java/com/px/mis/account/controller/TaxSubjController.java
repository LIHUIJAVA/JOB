package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.TaxSubj;
import com.px.mis.account.service.TaxSubjService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//˰���Ŀ�ӿ�
@Controller
@RequestMapping(value = "account/taxSubj", method = RequestMethod.POST)
public class TaxSubjController {
    private Logger logger = LoggerFactory.getLogger(TaxSubjController.class);
    @Autowired
    private TaxSubjService tss;

    /* ��� */
    @RequestMapping("insertTaxSubj")
    public @ResponseBody Object insertTaxSubj(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/insertTaxSubj");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        TaxSubj taxSubj = null;
        try {
            taxSubj = BaseJson.getPOJO(jsonData, TaxSubj.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/insertTaxSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/insertTaxSubj", false, "���������������������������Ƿ���д��ȷ������쳣��",
                        null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/insertTaxSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = tss.insertTaxSubj(taxSubj);
            if (on == null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/insertTaxSubj", false, "����쳣��", null);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/insertTaxSubj", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/insertTaxSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ɾ�� */
    @RequestMapping("deleteTaxSubj")
    public @ResponseBody Object deleteTaxSubj(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/deleteTaxSubj");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        String autoId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            autoId = reqBody.get("autoId").asText();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/deleteTaxSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/deleteTaxSubj", false,
                        "���������������������������Ƿ���д��ȷ��delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/deleteTaxSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = tss.deleteTaxSubjById(autoId);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/deleteTaxSubj", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/deleteTaxSubj", false, "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/deleteTaxSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ���� */
    @RequestMapping("updateTaxSubj")
    public @ResponseBody Object updateTaxSubj(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/updateTaxSubj");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<TaxSubj> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                TaxSubj taxSubj = new TaxSubj();
                BeanUtils.populate(taxSubj, map);
                cList.add(taxSubj);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/updateTaxSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/updateTaxSubj", false,
                        "���������������������������Ƿ���д��ȷ��update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/updateTaxSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = tss.updateTaxSubjById(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/updateTaxSubj", on.get("isSuccess").asBoolean(),
                        on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/updateTaxSubj", false, "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/updateTaxSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectTaxSubj")
    public @ResponseBody Object selectTaxSubj(@RequestBody String jsonBody) {
        logger.info("url:/account/taxSubj/selectTaxSubj");
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
                resp = BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", false,
                        "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�",
                        null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/selectTaxSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = tss.selectTaxSubjList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectTaxSubjById")
    public @ResponseBody Object selectTaxSubjById(@RequestBody String jsonData) {
        logger.info("url:/account/taxSubj/selectTaxSubjById");
        logger.info("���������" + jsonData);
        String resp = "";
        Integer autoId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            autoId = reqBody.get("autoId").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjById �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/taxSubj/selectTaxSubjById", false,
                        "���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/selectTaxSubjById �쳣˵����", e);
            }
            return resp;
        }
        try {
            TaxSubj taxSubj = tss.selectTaxSubjById(autoId);
            if (taxSubj != null) {
                resp = BaseJson.returnRespObj("/account/taxSubj/selectTaxSubjById", true, "����ɹ���", taxSubj);
            } else {
                resp = BaseJson.returnRespObj("/account/taxSubj/selectTaxSubjById", false, "û�в鵽���ݣ�", taxSubj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjById �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���д�ӡ */
    @RequestMapping(value = "selectTaxSubjPrint")
    public @ResponseBody Object selectTaxSubjPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/taxSubj/selectTaxSubjPrint");
        logger.info("���������" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjPrint �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespList("/account/taxSubj/selectTaxSubjPrint", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/taxSubj/selectTaxSubjPrint �쳣˵����", e);
            }
            return resp;
        }
        try {

            resp = tss.selectTaxSubjPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/taxSubj/selectTaxSubjPrint �쳣˵����", e);
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
                    return BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", false, "��ѡ���ļ���", null);
                }
                return tss.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/taxSubj/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
