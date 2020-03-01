package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossSubTab;
import com.px.mis.whs.service.CheckPrftLossService;

//�̵������ĵ���
@Controller
@RequestMapping("/whs/check_sngl_loss")
public class CheckPrftLossController {

    private static final Logger logger = LoggerFactory.getLogger(CheckPrftLossController.class);

    @Autowired
    CheckPrftLossService checkPrftLossService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    //	����
    @Autowired
    FormBookService formBookService;
    @Autowired
    MisUserService misUserService;

    // ���������
    @RequestMapping(value = "insertCheckSnglLoss", method = RequestMethod.POST)
    @ResponseBody
    public Object insertCheckSnglLoss(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);
        logger.info("url:whs/check_sngl_loss/insertCheckSnglLoss");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();

            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));

            // ����
            CheckPrftLoss cSngl = BaseJson.getPOJO(jsonBody, CheckPrftLoss.class);
            cSngl.setSetupPers(userName);
            // �ӱ�
            List<CheckPrftLossSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckPrftLossSubTab.class);

            for (CheckPrftLossSubTab subTab : cList) {

                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
                subTab.setCheckFormNum(cSngl.getCheckFormNum());// ������������ȡ�����ӱ�

            }

            resp = checkPrftLossService.insertCheckSnglLoss(userId, cSngl, cList,loginTime);
            misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request));

            logger.info("���ز�����" + resp);

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request, e));

            return resp = BaseJson.returnRespObj("whs/check_sngl_loss/insertCheckSnglLoss", false, e.getMessage(),
                    null);

        }
        return resp;
    }

    // �޸������
    @RequestMapping(value = "updateCheckSnglLoss", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCheckSnglLoss(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl_loss/updateCheckSnglLoss");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // ����
            CheckPrftLoss cSngl = BaseJson.getPOJO(jsonBody, CheckPrftLoss.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            cSngl.setMdfr(userName);
            // �ӱ�
            List<CheckPrftLossSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckPrftLossSubTab.class);
            for (CheckPrftLossSubTab subTab : cList) {
                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
                subTab.setCheckFormNum(cSngl.getCheckFormNum());// ������������ȡ�����ӱ�
            }

            resp = checkPrftLossService.updateCheckSnglLoss(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("�޸������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸������", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCheckSnglLoss", false, e.getMessage(), null);


        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteAllCheckSnglLoss", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllCheckSnglLoss(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/deleteAllCheckSnglLoss");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkPrftLossService.deleteAllCheckSnglLoss(checkFormNum);
            misLogDAO.insertSelective(new MisLog("ɾ�������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("ɾ�������", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/deleteAllCheckSnglLoss", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��Ʒ�ṹ
    // ��ѯ����ҳ��
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/query");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkPrftLossService.query(checkFormNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/query", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("checkDt2") !=null && !map.get("checkDt2").equals("")) {

                map.put("checkDt2", map.get("checkDt2").toString() + " 23:59:59");
            }
            if (map.get("checkDt1") !=null && !map.get("checkDt1").equals("")) {
                map.put("checkDt1", map.get("checkDt1").toString() + " 00:00:00");
            }
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryList", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);


            resp = checkPrftLossService.queryList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���
    @RequestMapping(value = "updateCSnglLossChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglLossChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl_loss/updateCSnglLossChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossChk", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // ���
                try {
                    resp += checkPrftLossService.updateCSnglLossChk(userId, dbzhu, userName,
                            CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) + "\n";
                    misLogDAO.insertSelective(new MisLog("��������", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {

                    misLogDAO.insertSelective(new MisLog("��������", "�ֿ�", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglChk", isSuccess, resp, null);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglChk", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����
    @RequestMapping(value = "updateCSnglLossNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl_loss/updateCSnglLossNoChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossNoChk", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // ����
                try {
                    resp += checkPrftLossService.updateCSnglLossNoChk(userId, dbzhu) + "\n";
                    misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }

            }
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossNoChk", isSuccess, resp, null);
        } catch (Exception e1) {

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCSnglLossNoChk", false, e1.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl_loss/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("checkDt2") !=null && !map.get("checkDt2").equals("")) {

                map.put("checkDt2", map.get("checkDt2").toString() + " 23:59:59");
            }
            if (map.get("checkDt1") !=null && !map.get("checkDt1").equals("")) {
                map.put("checkDt1", map.get("checkDt1").toString() + " 00:00:00");
            }
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryListDaYin", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);

            resp = checkPrftLossService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("���������", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryListDaYin", false, e.getMessage(), null);

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
                return checkPrftLossService.uploadFileAddDb(file);
            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/check_sngl_loss/uploadExpressCorpFile", false, "�����ļ���ʽ�����޷����룡",
                        null);
            } catch (IOException e1) {


            }

        }
        return null;
    }
}
