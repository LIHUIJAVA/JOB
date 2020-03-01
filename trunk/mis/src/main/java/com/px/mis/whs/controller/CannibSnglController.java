package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.entity.CannibSngl;
import com.px.mis.whs.entity.CannibSnglSubTab;
import com.px.mis.whs.service.CannibSnglService;

/**
 * @author zhangxiaoyu
 * @version ����ʱ�䣺2018��11��15�� ����2:39:10
 * @ClassName ������ �������ĵ���
 * @Description ������
 */
@Controller
@RequestMapping("/whs/cannib_sngl")
public class CannibSnglController {

    private static final Logger logger = LoggerFactory.getLogger(CannibSnglController.class);

    @Autowired
    CannibSnglService cannibSnglService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    // http����
    @Autowired
    HttpServletRequest request;
    //	����
    @Autowired
    FormBookService formBookService;
    @Autowired
    MisUserServiceImpl misUserService;

    // ����������
    @RequestMapping(value = "insertCSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object insertCSngl(@RequestBody String jsonBody) {
        // System.out.println(jsonBody);
        logger.info("url:whs/cannib_sngl/insertCSngl");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            // ����
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            CannibSngl cSngl = BaseJson.getPOJO(jsonBody, CannibSngl.class);
            cSngl.setSetupPers(userName);
            // �ӱ�
            List<CannibSnglSubTab> cList = BaseJson.getPOJOList(jsonBody, CannibSnglSubTab.class);
            if(!misUserService.isWhsPer(jsonBody,cSngl.getTranOutWhsEncd())){
                throw new RuntimeException("�û�û��ת���ֿ�Ȩ��");
            }
            if(!misUserService.isWhsPer(jsonBody,cSngl.getTranInWhsEncd())){
                throw new RuntimeException("�û�û��ת��ֿ�Ȩ��");
            }
            for (CannibSnglSubTab subTab : cList) {

//                subTab.setBaoZhiQi(subTab.getBaoZhiQiDt());
                subTab.setInvtyId(subTab.getInvtyEncd());
                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
            }

            resp = cannibSnglService.insertCSngl(userId, cSngl, cList,loginTime);
            misLogDAO.insertSelective(new MisLog("����������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            logger.error("url:whs/cannib_sngl/insertCSngl �쳣˵��", e);
            try {
                misLogDAO.insertSelective(new MisLog("����������", "�ֿ�", null, jsonBody, request, e));

                return resp = BaseJson.returnRespObj("whs/cannib_sngl/insertCSngl", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸ĵ�����
    @RequestMapping(value = "updateCSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCSngl(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/updateCSngl");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            // ����
            CannibSngl cSngl = BaseJson.getPOJO(jsonBody, CannibSngl.class);
            cSngl.setCannibDt(cSngl.getFormDt());
            cSngl.setMdfr(userName);
            if(!misUserService.isWhsPer(jsonBody,cSngl.getTranOutWhsEncd())){
                throw new RuntimeException("�û�û��ת���ֿ�Ȩ��");
            }
            if(!misUserService.isWhsPer(jsonBody,cSngl.getTranInWhsEncd())){
                throw new RuntimeException("�û�û��ת��ֿ�Ȩ��");
            }
            // �ӱ�
            List<CannibSnglSubTab> cList = BaseJson.getPOJOList(jsonBody, CannibSnglSubTab.class);

            for (CannibSnglSubTab subTab : cList) {

//                subTab.setBaoZhiQi(subTab.getBaoZhiQiDt());
                subTab.setInvtyId(subTab.getInvtyEncd());
                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
                subTab.setFormNum(cSngl.getFormNum());// ������������ȡ�����ӱ�
            }

            resp = cannibSnglService.updateCSngl(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("�޸ĵ�����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {


            logger.error("url:whs/cannib_sngl/updateCSngl �쳣˵��", e);
            misLogDAO.insertSelective(new MisLog("�޸ĵ�����", "�ֿ�", null, jsonBody, request, e));

            try {
                return resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngl", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ�������� ��ɾ
    @RequestMapping(value = "deleteAllCSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllCSngl(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/deleteAllCSngl");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String formNum = reqBody.get("formNum").asText();
            resp = cannibSnglService.deleteAllCSngl(formNum);
            misLogDAO.insertSelective(new MisLog("ɾ��������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/deleteAllCSngl �쳣˵��", e);

            try {
                misLogDAO.insertSelective(new MisLog("ɾ��������", "�ֿ�", null, jsonBody, request, e));
                return resp = BaseJson.returnRespObj("whs/cannib_sngl/deleteAllCSngl", false, resp + e.getMessage(),
                        null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ������
    // ��ѯ����ҳ��
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/query");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String formNum = reqBody.get("formNum").asText();
            resp = cannibSnglService.query(formNum);
        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/query �쳣˵��", e);

            try {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/query", false, resp + e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {

                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {

                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/cannib_sngl/queryList", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);

            resp = cannibSnglService.queryList(map);
            }
        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/queryList �쳣˵��", e);

            try {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/queryList", false, resp + e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // PDA ��ѯ
    @RequestMapping(value = "selectCSnglChkr", method = RequestMethod.POST)
    @ResponseBody
    private String selectCSnglChkr() {
        logger.info("url:whs/cannib_sngl/selectCSnglChkr");
        String resp = "";
        try {

            resp = cannibSnglService.selectCSnglChkr();
        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/selectCSnglChkr �쳣˵��", e);

            try {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/selectCSnglChkr", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸��ӱ�
    @RequestMapping(value = "updateCSnglSubTab", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglSubTab(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/updateCSnglSubTab");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // �ӱ�
            CannibSnglSubTab cSnglTab = BaseJson.getPOJO(jsonBody, CannibSnglSubTab.class);
            resp = cannibSnglService.updateCSnglSubTab(cSnglTab);
            misLogDAO.insertSelective(new MisLog("�޸��ӱ������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/updateCSnglSubTab �쳣˵��", e);
            misLogDAO.insertSelective(new MisLog("�޸��ӱ������", "�ֿ�", null, jsonBody, request, e));

            try {
                return resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSnglSubTab", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸�����
    @RequestMapping(value = "updateCSngls", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSngls(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/updateCSngls");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // ����
            CannibSngl cSngl = BaseJson.getPOJO(jsonBody, CannibSngl.class);
            resp = cannibSnglService.updateCSngl(cSngl);
            misLogDAO.insertSelective(new MisLog("�޸����������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/updateCSngls �쳣˵��", e);

            try {
                misLogDAO.insertSelective(new MisLog("�޸����������", "�ֿ�", null, jsonBody, request, e));

                return resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngls", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ������ ���
    @RequestMapping(value = "updateCSnglChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/cannib_sngl/updateCSnglChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        Boolean isSuccess = true;

        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngls", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // System.out.println(dbzhu);
                try {

                    resp += cannibSnglService.updateCSnglChk(userId, dbzhu, userName,
                            CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) + "\n";
                    misLogDAO.insertSelective(new MisLog("��˵�����", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {


                    misLogDAO.insertSelective(new MisLog("��˵�����", "�ֿ�", null, jsonBody, request, e));

                    resp += e.getMessage() + "\n";
                    isSuccess = false;
                }
            }
        } catch (Exception e1) {

            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngls", isSuccess, resp + e1.getMessage(), null);
            logger.error("url:whs/cannib_sngl/updateCSnglChk �쳣˵��", e1);

        }
        resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngls", isSuccess, resp, null);

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ������ ����
    @RequestMapping(value = "updateCSnglNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/cannib_sngl/updateCSnglNoChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        Boolean isSuccess = true;

        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSnglNoChk", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // System.out.println(dbzhu);
                // ����
                try {

                    resp += cannibSnglService.updateCSnglNoChk(userId, dbzhu, userName) + "\n";
                    misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {


                    misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request, e));

                    resp += e.getMessage() + "\n";
                    isSuccess = false;
                }
            }

            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSnglNoChk", isSuccess, resp, null);
        } catch (Exception e1) {

            logger.error("url:whs/cannib_sngl/updateCSnglNoChk �쳣˵��", e1);

            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSnglNoChk", isSuccess, resp + e1.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {

                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {

                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/cannib_sngl/queryListDaYin", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);


            resp = cannibSnglService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("����������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("����������", "�ֿ�", null, jsonBody, request, e));

            logger.error("url:whs/cannib_sngl/queryListDaYin �쳣˵��", e);

            try {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/queryListDaYin", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ������ִ��� ������
    @RequestMapping(value = "selectInvty", method = RequestMethod.POST)
    @ResponseBody
    private String selectInvty(@RequestBody String jsonBody) {
        logger.info("url:whs/cannib_sngl/selectInvty");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            resp = cannibSnglService.selectInvty(map);
        } catch (Exception e) {

            logger.error("url:whs/cannib_sngl/selectInvty �쳣˵��", e);

            try {
                resp = BaseJson.returnRespObj("whs/cannib_sngl/selectInvty", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���뵥��
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public Object uploadPursOrderFile(HttpServletRequest request) {
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
                    return BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDb", false, "��ѡ���ļ�", null);
                }
                return cannibSnglService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    } // ���뵥��

    @RequestMapping("uploadFileAddDbU8")
    @ResponseBody
    public Object uploadFileAddDbU8(HttpServletRequest request) {
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
                    return BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDbU8", false, "��ѡ���ļ�", null);
                }
                return cannibSnglService.uploadFileAddDbU8(file);
            } else {
                return BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDbU8", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDbU8", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }
}
