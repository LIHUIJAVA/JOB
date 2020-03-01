package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.px.mis.system.service.impl.MisUserServiceImpl;
import com.px.mis.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
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
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.City;
import com.px.mis.whs.entity.RealWhs;
import com.px.mis.whs.entity.RealWhsMap;
import com.px.mis.whs.entity.UserWhs;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.entity.WhsGds;
import com.px.mis.whs.service.WhsDocService;

//�ֿ⵵���ĵ���
@Controller
@RequestMapping("/whs/whs_doc")
public class WhsDocController {

    private static final Logger logger = LoggerFactory.getLogger(WhsDocController.class);

    @Autowired
    WhsDocService whsDocService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    MisUserServiceImpl misUserService;

    // ��Ӳֿ⵵��
    @RequestMapping(value = "insertWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object insertWhsDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertWhsDoc");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            WhsDoc wDoc = BaseJson.getPOJO(jsonBody, WhsDoc.class);
            wDoc.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.insertWhsDoc(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertWhsDoc", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�����ֿ⵵��", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("�����ֿ⵵��", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����ֿ⵵��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertWhsDoc", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸Ĳֿ⵵��
    @RequestMapping(value = "updateWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object updateWhsDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/updateWhsDoc");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            WhsDoc wDoc = BaseJson.getPOJO(jsonBody, WhsDoc.class);
            wDoc.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.updateWhsDoc(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/updateWhsDoc", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�޸Ĳֿ⵵��", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("�޸Ĳֿ⵵��", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸Ĳֿ⵵��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/updateWhsDoc", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ���ֿ⵵�� --
    @RequestMapping(value = "deleteWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWhsDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteWhsDoc");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            objectNode = whsDocService.deleteWhsDoc(whsEncd);
            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsDoc", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsDoc", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ���ֿ⵵��
    // ����ɾ��
    @RequestMapping(value = "deleteWDocList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWDocList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteWDocList");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            resp = whsDocService.deleteWDocList(whsEncd);
            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWDocList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� �ֿ⵵�� --
    @RequestMapping(value = "selectWhsDoc", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsDoc(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/whs_doc/selectWhsDoc");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            if(!misUserService.isWhsPer(jsonBody,whsEncd)){
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            WhsDoc whsDoc = whsDocService.selectWhsDoc(whsEncd);
            if (whsDoc != null) {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDoc", true, "����ɹ���", whsDoc);
            } else {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDoc", false, "����ʧ�ܣ�", whsDoc);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDoc", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� �ֿ⵵��List
    @RequestMapping(value = "selectWhsDocList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsDocList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsDocList");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();

            reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
            if(!misUserService.isWhsPer(jsonBody,whsEncd)){
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            resp = whsDocService.selectWhsDocList(whsEncd, userId);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsDocList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ҳ��
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/whs_doc/queryList", false, "�û�û�вֿ�Ȩ��" , null);
            }else {
                map.put("whsId", whsId);

//                misUserService.selectUserWhs(jsonBody, map);
                resp = whsDocService.queryList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/whs_doc/queryListDaYin", false, "�û�û�вֿ�Ȩ��" , null);
            }else {
                map.put("whsId", whsId);

            resp = whsDocService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("�����ֿ⵵��", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����ֿ⵵��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/queryListDaYin", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ʡ �� �� ��Ҫ��
    @RequestMapping(value = "selectCity", method = RequestMethod.POST)
    @ResponseBody
    private String selectCity(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectCity");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            City city = null;

            city = BaseJson.getPOJO(jsonBody, City.class);

            resp = whsDocService.selectCity(city);
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/whs_doc/selectCity", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ʡ
    @RequestMapping(value = "selectProvinces", method = RequestMethod.POST)
    @ResponseBody
    private String selectProvinces(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectProvinces");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            resp = whsDocService.selectProvinces();

        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/whs_doc/selectProvinces", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ��
    @RequestMapping(value = "selectCities", method = RequestMethod.POST)
    @ResponseBody
    private String selectCities(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectCities");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody = null;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);

            String superiorCode = reqBody.get("codeId").asText();

            resp = whsDocService.selectCities(superiorCode);
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/whs_doc/selectCities", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ��
    @RequestMapping(value = "selectCounties", method = RequestMethod.POST)
    @ResponseBody
    private String selectCounties(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectCounties");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody = null;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);

            String superiorCode = reqBody.get("codeId").asText();

            resp = whsDocService.selectCounties(superiorCode);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectCities", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // �Ƽ۷�ʽ
    @RequestMapping(value = "selectValtnMode", method = RequestMethod.POST)
    @ResponseBody
    private String selectValtnMode(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectValtnMode");
        String resp = "";
        try {

            resp = whsDocService.selectValtnMode();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectValtnMode", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // �ֿ�����
    @RequestMapping(value = "selectWhsAttr", method = RequestMethod.POST)
    @ResponseBody
    private String selectWhsAttr(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsAttr");
        String resp = "";
        try {

            resp = whsDocService.selectWhsAttr();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsAttr", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ���������Ʒ�ʽ
    @RequestMapping(value = "selectAMode", method = RequestMethod.POST)
    @ResponseBody
    private String selectAMode(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectAMode");
        String resp = "";
        try {

            resp = whsDocService.selectAMode();
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectAMode", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // �����̵㵥����
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public Object uploadPursOrderFile(HttpServletRequest request) {
        String resp = "";
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
                    return BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", false, "��ѡ���ļ���", null);
                }

                return whsDocService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {


            try {
                resp = BaseJson.returnRespObj("whs/whs_doc/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return resp;
    }

    // ����û��ֿ�
    @RequestMapping(value = "insertUserWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object insertUserWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertUserWhs");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            List<UserWhs> userWhs = new ArrayList<>();
            List<String> list = getList((String) map.get("realWhs"));
            for (String string : list) {
                UserWhs e = new UserWhs();
                e.setAccNum((String) map.get("accNum"));
                e.setRealWhs(string);
                userWhs.add(e);
            }

            objectNode = whsDocService.insertUserWhs(userWhs);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertUserWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�����û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("�����û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertUserWhs", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    /**
     * id����list
     *
     * @param id id(����Ѷ��ŷָ�)
     * @return List����
     */
    public List<String> getList(String id) {
        if (id == null || id.equals("")) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return list;
    }

    // �޸��û��ֿ�
    @RequestMapping(value = "updateUserWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object updateUserWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/updateUserWhs");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            UserWhs wDoc = BaseJson.getPOJO(jsonBody, UserWhs.class);
            objectNode = whsDocService.updateUserWhs(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/updateUserWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�޸��û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("�޸��û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸��û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/updateUserWhs", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ���ֿ⵵��
    // ����ɾ��
    @RequestMapping(value = "deleteUserWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteUserWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteUserWhsList");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String id = reqBody.get("id").asText();
            resp = whsDocService.deleteUserWhsList(id);
            misLogDAO.insertSelective(new MisLog("����ɾ���û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����ɾ���û��ֿ��Ӧ��ϵ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteUserWhsList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲��û��ֿ��Ӧ��ϵList
    @RequestMapping(value = "selectUserWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectUserWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectUserWhsList");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = whsDocService.selectUserWhsList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectUserWhsList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��Ӳֿ��ϵ
    @RequestMapping(value = "insertWhsGds", method = RequestMethod.POST)
    @ResponseBody
    public Object insertLogicRealWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertWhsGds");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            List<WhsGds> userWhs = new ArrayList<>();
            WhsGds e = new WhsGds();
            e.setGdsBitEncd((String) map.get("gdsBitEncd"));
            e.setRealWhs((String) map.get("realWhs"));
            e.setRegnEncd((String) map.get("regnEncd"));
            userWhs.add(e);

            objectNode = whsDocService.insertWhsGds(userWhs);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertWhsGds", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�����ֿ��ϵ", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("�����ֿ��ϵ", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����ֿ��ϵ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertLogicRealWhs", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ���ֿ��ϵ
    // ����ɾ��
    @RequestMapping(value = "deleteWhsGds", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteWhsGds(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteWhsGds");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String id = reqBody.get("id").asText();
            List<String> list = getList(id);
            resp = whsDocService.deleteWhsGds(list);

            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ��ϵ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ��ϵ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteWhsGds", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲�ֿ��ϵList
    @RequestMapping(value = "selectWhsGds", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsGds(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsGds");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));
            map.put("realWhs", getList((String) map.get("realWhs")));
            map.put("regnEncd", getList((String) map.get("regnEncd")));

            resp = whsDocService.selectWhsGdsList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsGds", false, e.getMessage(), null);
        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // Pda�򵥲�ֿ��ϵList
    @RequestMapping(value = "selectWhsGdsRealList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectWhsGdsRealList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectWhsGdsRealList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
//			map.put("realWhs", getList((String) map.get("realWhs")));
//			map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));
//			map.put("regnEncd", getList((String) map.get("regnEncd")));
            map.put("whsEncd", getList((String) map.get("whsEncd")));
            resp = whsDocService.selectWhsGdsRealList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectWhsGds", false, e.getMessage(), null);
        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��Ӳֿ⵵��
    @RequestMapping(value = "insertRealWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object insertRealWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertRealWhs");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            RealWhs wDoc = BaseJson.getPOJO(jsonBody, RealWhs.class);
            wDoc.setSetupPers(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.insertRealWhs(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�����ֿܲ�", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("�����ֿܲ�", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����ֿܲ�", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhs", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸Ĳֿ⵵��
    @RequestMapping(value = "updateRealWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object updateRealWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/updateRealWhs");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {

            RealWhs wDoc = BaseJson.getPOJO(jsonBody, RealWhs.class);
            wDoc.setMdfr(BaseJson.getReqHead(jsonBody).get("userName").asText());

            objectNode = whsDocService.updateRealWhs(wDoc);

            resp = BaseJson.returnRespObj("whs/whs_doc/updateRealWhs", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);
            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�޸��ֿܲ⵵��", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(
                        new MisLog("�޸��ֿܲ⵵��", "�ֿ�", null, jsonBody, request, "ʧ��", objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸��ֿܲ⵵��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/updateRealWhs", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ���ֿ⵵��
    // ����ɾ��
    @RequestMapping(value = "deleteRealWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteRealWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteRealWhsList");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("realWhs").asText();
            resp = whsDocService.deleteRealWhsList(whsEncd);
            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ⵵��", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteRealWhsList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� �ֿ⵵�� --
    @RequestMapping(value = "selectRealWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRealWhs(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/whs_doc/selectRealWhs");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String realWhs = reqBody.get("realWhs").asText();
            RealWhs whsDoc = whsDocService.selectRealWhs(realWhs);
            if (whsDoc != null) {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhs", true, "����ɹ���", whsDoc);
            } else {
                resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhs", false, "����ʧ�ܣ�", whsDoc);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhs", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ҳ��
    @RequestMapping(value = "queryRealWhsList", method = RequestMethod.POST)
    @ResponseBody
    private String queryRealWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryRealWhsList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            resp = whsDocService.queryRealWhsList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/queryRealWhsList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryRealWhsListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryRealWhsListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/queryRealWhsListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = whsDocService.queryRealWhsListDaYin(map);
            misLogDAO.insertSelective(new MisLog("�����ֿܲ⵵��", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����ֿܲ⵵��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/queryRealWhsListDaYin", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �ֿ���ܲ�

    // ���RealWhsMap
    @RequestMapping(value = "insertRealWhsMap", method = RequestMethod.POST)
    @ResponseBody
    public Object insertRealWhsMap(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/insertRealWhsMap");
        logger.info("���������" + jsonBody);

        ObjectNode objectNode = null;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            RealWhsMap realWhsMap = new RealWhsMap();
            realWhsMap.setWhsEncd((String) map.get("whsEncd"));
            realWhsMap.setRealWhs((String) map.get("realWhs"));

            objectNode = whsDocService.insertRealWhsMap(realWhsMap);

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhsMap", objectNode.get("isSuccess").asBoolean(),
                    objectNode.get("message").asText(), null);

            if (objectNode.get("isSuccess").asBoolean()) {
                misLogDAO.insertSelective(new MisLog("�����ֿ���ֹܲ�ϵ", "�ֿ�", null, jsonBody, request));

            } else {
                misLogDAO.insertSelective(new MisLog("�����ֿ���ֹܲ�ϵ", "�ֿ�", null, jsonBody, request, "ʧ��",
                        objectNode.get("message").asText()));
            }
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����ֿ���ֹܲ�ϵ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/whs_doc/insertRealWhsMap", false, e.getMessage(), null);


        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ���ֿ��ϵ
    // ����ɾ��
    @RequestMapping(value = "deleteRealWhsMap", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteRealWhsMap(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/deleteRealWhsMap");
        logger.info("���������" + jsonBody);

        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);

            List<String> list = getList(reqBody.get("id").asText());

            resp = whsDocService.deleteRealWhsMap(list);

            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ���ֹܲ�ϵ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����ɾ���ֿ���ֹܲ�ϵ", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/whs_doc/deleteRealWhsMap", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲�ֿ��ϵList
    @RequestMapping(value = "selectRealWhsMap", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRealWhsMap(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/whs_doc/selectRealWhsMap");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            map.put("whsEncd", getList((String) map.get("whsEncd")));
            map.put("realWhs", getList((String) map.get("realWhs")));

            resp = whsDocService.selectRealWhsMap(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/whs_doc/selectRealWhsMap", false, e.getMessage(), null);
        }

        logger.info("���ز�����" + resp);
        return resp;
    }

}
