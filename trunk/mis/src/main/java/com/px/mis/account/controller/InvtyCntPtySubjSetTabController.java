package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyCntPtySubjSetTab;
import com.px.mis.account.service.InvtyCntPtySubjSetTabService;
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

//�Է���Ŀ�ӿ�
@RequestMapping(value = "account/invtyCntPtySubjSetTab", method = RequestMethod.POST)
@Controller
public class InvtyCntPtySubjSetTabController {
    private Logger logger = LoggerFactory.getLogger(InvtyCntPtySubjSetTabController.class);
    @Autowired
    private InvtyCntPtySubjSetTabService icpsst;

    /* ��� */
    @RequestMapping("insertInvtyCntPtySubjSetTab")
    public @ResponseBody Object insertInvtyCntPtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        InvtyCntPtySubjSetTab invtyCntPtySubjSetTab = null;
        try {
            invtyCntPtySubjSetTab = BaseJson.getPOJO(jsonData, InvtyCntPtySubjSetTab.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ��insert error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = icpsst.insertInvtyCntPtySubjSetTab(invtyCntPtySubjSetTab);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab", false,
                        "insert error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/insertInvtyCntPtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ɾ�� */
    @RequestMapping("deleteInvtyCntPtySubjSetTab")
    public @ResponseBody Object deleteInvtyCntPtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ��delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = icpsst.deleteInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab", false,
                        "delete error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ���� */
    @RequestMapping("updateInvtyCntPtySubjSetTab")
    public @ResponseBody Object updateInvtyCntPtySubjSetTab(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<InvtyCntPtySubjSetTab> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                InvtyCntPtySubjSetTab invtyCntPtySubjSetTab = new InvtyCntPtySubjSetTab();
                BeanUtils.populate(invtyCntPtySubjSetTab, map);
                cList.add(invtyCntPtySubjSetTab);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ��update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = icpsst.updateInvtyCntPtySubjSetTabByOrdrNum(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab", false,
                        "update error!", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/updateInvtyCntPtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectInvtyCntPtySubjSetTab")
    public @ResponseBody Object selectInvtyCntPtySubjSetTab(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab");
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
                resp = BaseJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab", false,
                        "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = icpsst.selectInvtyCntPtySubjSetTabList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTab �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectInvtyCntPtySubjSetTabById")
    public @ResponseBody Object selectInvtyCntPtySubjSetTabById(@RequestBody String jsonData) {
        logger.info("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById");
        logger.info("���������" + jsonData);
        String resp = "";
        Integer ordrNum = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            ordrNum = reqBody.get("ordrNum").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById", false,
                        "���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById �쳣˵����", e);
            }
            return resp;
        }
        try {
            InvtyCntPtySubjSetTab selectByOrdrNum = icpsst.selectInvtyCntPtySubjSetTabByOrdrNum(ordrNum);
            if (selectByOrdrNum != null) {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById", true,
                        "����ɹ���", selectByOrdrNum);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById", false,
                        "����ʧ�ܣ�", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabById �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteInvtyCntPtySubjSetTabList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteInvtyCntPtySubjSetTabList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:account/invtyCntPtySubjSetTab/deleteInvtyCntPtySubjSetTabList");
        logger.info("���������" + jsonBody);
        String resp = "";
        resp = icpsst.deleteInvtyCntPtySubjSetTabList(BaseJson.getReqBody(jsonBody).get("ordrNum").asText());
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���д�ӡ */
    @RequestMapping(value = "selectInvtyCntPtySubjSetTabPrint")
    public @ResponseBody Object selectInvtyCntPtySubjSetTabPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint");
        logger.info("���������" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint",
                        false, "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = icpsst.selectInvtyCntPtySubjSetTabListPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyCntPtySubjSetTab/selectInvtyCntPtySubjSetTabPrint �쳣˵����", e);
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
                    return BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", false, "��ѡ���ļ���",
                            null);
                }
                return icpsst.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/invtyCntPtySubjSetTab/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
