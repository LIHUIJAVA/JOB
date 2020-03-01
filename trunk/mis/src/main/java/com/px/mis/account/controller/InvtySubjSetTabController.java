package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtySubjSetTab;
import com.px.mis.account.service.InvtySubjSetTabService;
import com.px.mis.account.utils.TransformJson;
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

//�����Ŀ�ӿ�
@RequestMapping(value = "account/invtySubjSetTab", method = RequestMethod.POST)
@Controller
public class InvtySubjSetTabController {
    private Logger logger = LoggerFactory.getLogger(InvtySubjSetTabController.class);
    @Autowired
    private InvtySubjSetTabService isst;

    /* ��Ӵ����Ŀ */
    @RequestMapping("insertInvtySubjSetTab")
    public @ResponseBody Object insertInvtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/insertInvtySubjSetTab");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        InvtySubjSetTab invtySubjSetTab = null;
        try {
            invtySubjSetTab = BaseJson.getPOJO(jsonData, InvtySubjSetTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/insertInvtySubjSetTab �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/insertInvtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ��insert error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/insertInvtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = isst.insertInvtySubjSetTab(invtySubjSetTab);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/insertInvtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/insertInvtySubjSetTab", false, "insert error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/insertInvtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ɾ�������Ŀ */
    @RequestMapping("deleteInvtySubjSetTab")
    public @ResponseBody Object deleteInvtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/deleteInvtySubjSetTab");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/deleteInvtySubjSetTab �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/deleteInvtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ��delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/deleteInvtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = isst.deleteInvtySubjSetTabByOrdrNum(ordrNum);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/deleteInvtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/deleteInvtySubjSetTab", false, "delete error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/deleteInvtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ���´����Ŀ */
    @RequestMapping("updateInvtySubjSetTab")
    public @ResponseBody Object updateInvtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/updateInvtySubjSetTab");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<InvtySubjSetTab> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                InvtySubjSetTab invtySubjSetTab = new InvtySubjSetTab();
                BeanUtils.populate(invtySubjSetTab, map);
                cList.add(invtySubjSetTab);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/updateInvtySubjSetTab �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/updateInvtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ��update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/updateInvtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = isst.updateInvtySubjSetTabById(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/updateInvtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/updateInvtySubjSetTab", false, "update error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/updateInvtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���д����Ŀ */
    @RequestMapping(value = "selectInvtySubjSetTab")
    public @ResponseBody Object selectInvtySubjSetTab(@RequestBody String jsonBody) {
        logger.info("url:/account/invtySubjSetTab/selectInvtySubjSetTab");
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
                resp = BaseJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTab", false,
                        "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTab �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = isst.selectInvtySubjSetTabList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���������Ŀ */
    @RequestMapping(value = "selectInvtySubjSetTabById")
    public @ResponseBody Object selectInvtySubjSetTabById(@RequestBody String jsonData) {
        logger.info("url:/account/invtySubjSetTab/selectInvtySubjSetTabById");
        logger.info("���������" + jsonData);

        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabById �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/selectInvtySubjSetTabById", false,
                        "���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabById �쳣˵����", e);
            }
            return resp;
        }
        try {
            InvtySubjSetTab selectByOrdrNum = isst.selectInvtySubjSetTabByOrdrNum(ordrNum);
            if (selectByOrdrNum != null) {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/selectInvtySubjSetTabById", true, "����ɹ���",
                        selectByOrdrNum);
            } else {
                resp = BaseJson.returnRespObj("/account/invtySubjSetTab/selectInvtySubjSetTabById", false, "û�в鵽���ݣ�",
                        selectByOrdrNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabById �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ�������Ŀ
    @RequestMapping(value = "deleteInvtySubjSetTabList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteInvtySubjSetTabList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:account/invtySubjSetTab/deleteInvtySubjSetTabList");
        logger.info("���������" + jsonBody);
        String resp = "";
        resp = isst.deleteInvtySubjSetTabList(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���д����Ŀ��ӡ */
    @RequestMapping(value = "selectInvtySubjSetTabPrint")
    public @ResponseBody Object selectInvtySubjSetTabPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint");
        logger.info("���������" + jsonBody);

        String resp = "";
        Map map = null;

        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtySubjSetTab/selectInvtySubjSetTabPrint", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint �쳣˵����", e);
            }
            return resp;
        }
        try {

            resp = isst.selectInvtySubjSetTabPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtySubjSetTab/selectInvtySubjSetTabPrint �쳣˵����", e);
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
                    return BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", false, "��ѡ���ļ���", null);
                }
                return isst.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/invtySubjSetTab/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
