package com.px.mis.account.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.entity.InvtyPayblSubj;
import com.px.mis.account.service.InvtyPayblSubjService;
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

//Ӧ����Ŀ�ӿ�
@RequestMapping(value = "account/invtyPayblSubj", method = RequestMethod.POST)
@Controller
public class InvtyPayblSubjController {
    private Logger logger = LoggerFactory.getLogger(InvtyPayblSubjController.class);
    @Autowired
    private InvtyPayblSubjService ipss;

    /* ��� */
    @RequestMapping("insertInvtyPayblSubj")
    public @ResponseBody Object insertInvtyPayblSubj(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/insertInvtyPayblSubj");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        InvtyPayblSubj invtyPayblSubj = null;
        try {
            invtyPayblSubj = BaseJson.getPOJO(jsonData, InvtyPayblSubj.class);
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/insertInvtyPayblSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/insertInvtyPayblSubj", false,
                        "���������������������������Ƿ���д��ȷ������쳣��", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/insertInvtyPayblSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = ipss.insertInvtyPayblSubj(invtyPayblSubj);
            if (on == null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/insertInvtyPayblSubj", false, "����쳣��", null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/insertInvtyPayblSubj",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/insertInvtyPayblSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ɾ�� */
    @RequestMapping("deleteInvtyPayblSubj")
    public @ResponseBody Object deleteInvtyPayblSubj(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/deleteInvtyPayblSubj");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        Integer incrsId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            incrsId = reqBody.get("incrsId").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/deleteInvtyPayblSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/deleteInvtyPayblSubj", false,
                        "���������������������������Ƿ���д��ȷ��delete error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/deleteInvtyPayblSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = ipss.deleteInvtyPayblSubjById(incrsId);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/deleteInvtyPayblSubj",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/deleteInvtyPayblSubj", false, "delete error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/deleteInvtyPayblSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ���� */
    @RequestMapping("updateInvtyPayblSubj")
    public @ResponseBody Object updateInvtyPayblSubj(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/updateInvtyPayblSubj");
        logger.info("���������" + jsonData);
        ObjectNode on = null;
        String resp = "";
        List<InvtyPayblSubj> cList = new ArrayList<>();
        try {
            List<Map> mList = BaseJson.getList(jsonData);
            for (Map map : mList) {
                InvtyPayblSubj invtyPayblSubj = new InvtyPayblSubj();
                BeanUtils.populate(invtyPayblSubj, map);
                cList.add(invtyPayblSubj);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/updateInvtyPayblSubj �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/updateInvtyPayblSubj", false,
                        "���������������������������Ƿ���д��ȷ��update error!", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/updateInvtyPayblSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            on = ipss.updateInvtyPayblSubjById(cList);
            if (on != null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/updateInvtyPayblSubj",
                        on.get("isSuccess").asBoolean(), on.get("message").asText(), null);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/updateInvtyPayblSubj", false, "update error!",
                        null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/updateInvtyPayblSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectInvtyPayblSubj")
    public @ResponseBody Object selectInvtyPayblSubj(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyPayblSubj/selectInvtyPayblSubj");
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
                resp = BaseJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubj", false,
                        "�����������pageNo��pageSize������������㣬��ѯʧ�ܣ�", null);
                return resp;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubj", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj �쳣˵����", e);
            }
            return resp;
        }
        try {
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = ipss.selectInvtyPayblSubjList(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���� */
    @RequestMapping(value = "selectInvtyPayblSubjById")
    public @ResponseBody Object selectInvtyPayblSubjById(@RequestBody String jsonData) {
        logger.info("url:/account/invtyPayblSubj/selectInvtyPayblSubjById");
        logger.info("���������" + jsonData);
        String resp = "";
        Integer incrsId = null;
        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonData);
            incrsId = reqBody.get("incrsId").asInt();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjById �쳣˵����", e1);
            try {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/selectInvtyPayblSubjById", false,
                        "���������������������������Ƿ���д��ȷ��û�в鵽���ݣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjById �쳣˵����", e);
            }
            return resp;
        }
        try {
            InvtyPayblSubj invtyPayblSubj = ipss.selectInvtyPayblSubjById(incrsId);
            if (invtyPayblSubj != null) {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/selectInvtyPayblSubjById", true, "����ɹ���",
                        invtyPayblSubj);
            } else {
                resp = BaseJson.returnRespObj("/account/invtyPayblSubj/selectInvtyPayblSubjById", false, "û�в鵽���ݣ�",
                        invtyPayblSubj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjById �쳣˵����", e);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��Ӧ����Ŀ
    @RequestMapping(value = "deleteInvtyPayblSubjList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteInvtyPayblSubjList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:account/invtyPayblSubj/deleteInvtyPayblSubjList");
        logger.info("���������" + jsonBody);
        String resp = "";
        resp = ipss.deleteInvtyPayblSubjList(BaseJson.getReqBody(jsonBody).get("incrsId").asText());
        logger.info("���ز�����" + resp);
        return resp;
    }

    /* ��ѯ���д�ӡ */
    @RequestMapping(value = "selectInvtyPayblSubjPrint")
    public @ResponseBody Object selectInvtyPayblSubjPrint(@RequestBody String jsonBody) {
        logger.info("url:/account/invtyPayblSubj/selectInvtyPayblSubjPrint");
        logger.info("���������" + jsonBody);
        String resp = "";
        Map map = null;
        try {
            map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjPrint �쳣˵����", e1);
            try {
                resp = TransformJson.returnRespList("/account/invtyPayblSubj/selectInvtyPayblSubjPrint", false,
                        "���������������������������Ƿ���д��ȷ����ѯʧ�ܣ�", null);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubjPrint �쳣˵����", e);
            }
            return resp;
        }
        try {
            resp = ipss.selectInvtyPayblSubjListPrint(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("url:/account/invtyPayblSubj/selectInvtyPayblSubj �쳣˵����", e);
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
                    return BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", false, "��ѡ���ļ���", null);
                }
                return ipss.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return BaseJson.returnRespObj("account/invtyPayblSubj/uploadFileAddDb", false, e.getMessage(), null);

        }

    }
}
