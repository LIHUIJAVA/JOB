package com.px.mis.whs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.px.mis.system.service.impl.MisUserServiceImpl;
import com.px.mis.util.CommonUtil;
import org.apache.commons.beanutils.BeanUtils;
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
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglSubTab;
import com.px.mis.whs.service.CheckSnglService;

//�̵㵥�ĵ���
@Controller
@RequestMapping("/whs/check_sngl")
public class CheckSnglController {

    private static final Logger logger = LoggerFactory.getLogger(CheckSnglController.class);

    @Autowired
    CheckSnglService checkSnglService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    WhsDocMapper whsDocMapper;
    @Autowired
    MisUserServiceImpl misUserService;

    // �����̵㵥
    @RequestMapping(value = "insertCheckSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object insertCheckSngl(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);
        logger.info("url:whs/check_sngl/insertCheckSngl");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            String loginTime= BaseJson.getReqHead(jsonBody).get("loginTime").asText();
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            // ����
            CheckSngl cSngl = BaseJson.getPOJO(jsonBody, CheckSngl.class);
            cSngl.setSetupPers(userName);
            if(!misUserService.isWhsPer(jsonBody,cSngl.getWhsEncd())){
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            // �ӱ�
            List<CheckSnglSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckSnglSubTab.class);
            for (CheckSnglSubTab cSubTab : cList) {
                cSubTab.setCheckFormNum(cSngl.getCheckFormNum());
                cSubTab.setPrdcDt(StringUtils.trimToNull(cSubTab.getPrdcDt()));// ��������
                cSubTab.setInvldtnDt(StringUtils.trimToNull(cSubTab.getInvldtnDt()));// ʧЧ����

            }
            resp = checkSnglService.insertCheckSngl(userId, cSngl, cList,loginTime);
            misLogDAO.insertSelective(new MisLog("�����̵㵥", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("�����̵㵥", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �޸��̵㵥
    @RequestMapping(value = "updateCheckSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCheckSngl(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCheckSngl");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // ����
            CheckSngl cSngl = BaseJson.getPOJO(jsonBody, CheckSngl.class);
            // �ӱ�
            if(!misUserService.isWhsPer(jsonBody,cSngl.getWhsEncd())){
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            List<CheckSnglSubTab> cList = BaseJson.getPOJOList(jsonBody, CheckSnglSubTab.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            cSngl.setMdfr(userName);

            for (CheckSnglSubTab cSubTab : cList) {
                cSubTab.setCheckFormNum(cSngl.getCheckFormNum());// ������������ȡ�����ӱ�
                cSubTab.setPrdcDt(StringUtils.trimToNull(cSubTab.getPrdcDt()));// ��������
                cSubTab.setInvldtnDt(StringUtils.trimToNull(cSubTab.getInvldtnDt()));// ʧЧ����
            }

            resp = checkSnglService.updateCheckSngl(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("�޸��̵㵥", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("�޸��̵㵥", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCheckSngl", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteAllCheckSngl", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllCheckSngl(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/deleteAllCheckSngl");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkSnglService.deleteAllCheckSngl(checkFormNum);
            misLogDAO.insertSelective(new MisLog("ɾ���̵㵥", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ���̵㵥", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl/deleteAllCheckSngl", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲�
    // ��ѯ
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/query");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String checkFormNum = reqBody.get("checkFormNum").asText();
            resp = checkSnglService.query(checkFormNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/query", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ҳ��
    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
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
                resp = BaseJson.returnRespObj("whs/check_sngl/queryList", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);

                resp = checkSnglService.queryList(map);

            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ͨ���ֿ⡢�̵����š����������롢����Ϊ��ʱ�Ƿ��̵�
    @RequestMapping(value = "selectCheckSnglList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectCheckSnglList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/selectCheckSnglList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
            if(!misUserService.isWhsPer(jsonBody,(String)map.get("whsEncd"))){
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            resp = checkSnglService.selectCheckSnglList(map);
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // PDA ��ѯ ���ز���
    @RequestMapping(value = "checkSnglList", method = RequestMethod.POST)
    @ResponseBody
    public Object checkSnglList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/checkSnglList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = checkSnglService.checkSnglList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/check_sngl/checkSnglList", false, "�û���ҵ��ֿ�", null);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/checkSnglList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // pda �����ӱ����
    @RequestMapping(value = "updateCheck", method = RequestMethod.POST)
    @ResponseBody
    public Object updateCheck(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCheck");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            // ����
            CheckSngl cSngl = BaseJson.getPOJO(jsonBody, CheckSngl.class);

            // �ӱ�
            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<CheckSnglSubTab> cList = new ArrayList();
            for (Map map : cTabMap) {
                CheckSnglSubTab cSubTab = new CheckSnglSubTab();
                cSubTab.setCheckFormNum(cSngl.getCheckFormNum());// ������������ȡ�����ӱ�
                BeanUtils.populate(cSubTab, map);
                cList.add(cSubTab);
            }

            resp = checkSnglService.updateCheckTab(cSngl, cList);
            misLogDAO.insertSelective(new MisLog("�̵�pda �����ӱ���Żش�", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {


            misLogDAO.insertSelective(new MisLog("�̵�pda �����ӱ���Żش�", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCheck", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���
    @RequestMapping(value = "updateCSnglChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCSnglChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));

            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // ���
                try {

                    resp += checkSnglService.updateCSnglChk(userId, dbzhu, userName,
                            CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) + "\n";
                    misLogDAO.insertSelective(new MisLog("����̵㵥", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("����̵㵥", "�ֿ�", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglChk", isSuccess, resp, null);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglChk", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����
    @RequestMapping(value = "updateCSnglNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateCSnglNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateCSnglNoChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        Boolean isSuccess = true;

        try {

            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                // ����
                try {
                    resp += checkSnglService.updateCSnglNoChk(userId, dbzhu) + "\n";
                    misLogDAO.insertSelective(new MisLog("�����̵㵥", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("�����̵㵥", "�ֿ�", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglNoChk", isSuccess, resp, null);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/updateCSnglNoChk", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����֮������״̬
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    private String updateStatus(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/check_sngl/updateStatus");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            List<Map> cTabMap = BaseJson.getList(jsonBody);
            List<CheckSngl> cList = new ArrayList();
            for (Map map : cTabMap) {
                CheckSngl cSngl = new CheckSngl();
                BeanUtils.populate(cSngl, map);
                cList.add(cSngl);
            }
            // ����֮������״̬
            resp = checkSnglService.updateStatus(cList);
            misLogDAO.insertSelective(new MisLog("����֮������״̬�̵㵥", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("����֮������״̬�̵㵥", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/check_sngl/updateStatus", false, e.getMessage(), null);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
            if (map.get("checkDt2") !=null && !map.get("checkDt2").equals("")) {

                map.put("checkDt2", map.get("checkDt2").toString() + " 23:59:59");
            }
            if (map.get("checkDt1") !=null && !map.get("checkDt1").equals("")) {
                map.put("checkDt1", map.get("checkDt1").toString() + " 00:00:00");
            }
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/check_sngl/queryList", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);

            resp = checkSnglService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("�����̵㵥", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�����̵㵥", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/check_sngl/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �����̵㵥����
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
                    return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", false, "��ѡ���ļ�", null);
                }
                return checkSnglService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }

    // ͨ���ֿ⡢�̵����š����������롢����Ϊ��ʱ�Ƿ��̵�
    @RequestMapping(value = "selectCheckSnglGdsBitList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectCheckSnglGdsBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/check_sngl/selectCheckSnglGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);

            resp = checkSnglService.selectCheckSnglGdsBitList(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglGdsBitList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    private String realWhsString(String whsEncd) {
        // TODO Auto-generated method stub
        List<String> aList = whsDocMapper.selectRealWhsList(getList(whsEncd));
        String s = "";
        for (int i = 0; i < aList.size(); i++) {
            if (i == aList.size() - 1) {
                s += aList.get(i);
            } else {
                s += aList.get(i) + ",";
            }
        }
        if (s.equals("")) {
            s = null;
        }
        return s;
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

    // �����̵㵥����
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
                    return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", false, "��ѡ���ļ�", null);
                }
                return checkSnglService.uploadFileAddDbU8(file);
            } else {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }
}
