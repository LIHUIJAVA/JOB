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

import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.service.InvtyTabService;

//�����ݡ���������Ԥ�����ִ�����ѯ��
@SuppressWarnings("all")
@Controller
@RequestMapping("whs/invty_tab")
public class InvtyTabController {

    private static final Logger logger = LoggerFactory.getLogger(InvtyTabController.class);
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    InvtyTabService invtyTabService;
    @Autowired
    MisUserServiceImpl misUserService;

    // ������Ԥ��
    @RequestMapping(value = "queryBaoZhiQiList", method = RequestMethod.POST)
    @ResponseBody
    private String queryBaoZhiQiList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/queryBaoZhiQiList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/invty_tab/queryBaoZhiQiList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.queryBaoZhiQiList(map);
            }
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("������Ԥ��������", "�ֿ�", null, jsonBody, request));
            }

        } catch (Exception e) {
            if (prin) {
                misLogDAO.insertSelective(new MisLog("������Ԥ��������", "�ֿ�", null, jsonBody, request, e));
            }


            resp = BaseJson.returnRespObj("whs/invty_tab/queryBaoZhiQiList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �������ˮ
    @RequestMapping(value = "outIngWaterList", method = RequestMethod.POST)
    @ResponseBody
    private String outIngWaterList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/outIngWaterList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("chkTm2") !=null && !map.get("chkTm2").equals("")) {
                map.put("chkTm2", map.get("chkTm2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("chkTm1") !=null && !map.get("chkTm1").equals("")) {
                map.put("chkTm1", map.get("chkTm1").toString() + " 00:00:00");
            }
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/invty_tab/outIngWaterList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.outIngWaterList(map);
            }
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("�������ˮ������", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {
            if (prin) {
                misLogDAO.insertSelective(new MisLog("�������ˮ������", "�ֿ�", null, jsonBody, request, e));
            }

            resp = BaseJson.returnRespObj("whs/invty_tab/outIngWaterList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���̨��
    @RequestMapping(value = "InvtyStandList", method = RequestMethod.POST)
    @ResponseBody
    private String InvtyStandList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/InvtyStandList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("chkTm2") !=null && !map.get("chkTm2").equals("")) {
                map.put("chkTm2", map.get("chkTm2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("chkTm1") !=null && !map.get("chkTm1").equals("")) {
                map.put("chkTm1", map.get("chkTm1").toString() + " 00:00:00");
            }

            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/check_sngl_loss/queryList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.InvtyStandList(map);
            }
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("���̨�˱�����", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {
            if (prin) {
                misLogDAO.insertSelective(new MisLog("���̨�˱�����", "�ֿ�", null, jsonBody, request, e));
            }

            resp = BaseJson.returnRespObj("whs/invty_tab/InvtyStandList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "selectInvty", method = RequestMethod.POST)
    @ResponseBody
    private String selectInvty(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/selectInvty");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = invtyTabService.selectInvty(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/invty_tab/selectInvty", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // --------hj---�����ѯ------------------------------------------------------------
    @RequestMapping(value = "queryInvtyTabList", method = RequestMethod.POST)
    @ResponseBody
    private String queryInvtyTabList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/queryInvtyTabList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/invty_tab/queryInvtyTabList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.queryInvtyTabList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/invty_tab/queryInvtyTabList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �ִ�����ѯ
    @RequestMapping(value = "selectExtantQtyList", method = RequestMethod.POST)
    @ResponseBody
    private String selectExtantQtyList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/selectExtantQtyList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectExtantQtyList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);


                resp = invtyTabService.selectAvalQty(map);
            }
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("�ִ�����ѯ������", "�ֿ�", null, jsonBody, request));
            }

        } catch (Exception e) {
            if (prin) {
                misLogDAO.insertSelective(new MisLog("�ִ�����ѯ������", "�ֿ�", null, jsonBody, request, e));
            }

            resp = BaseJson.returnRespObj("whs/invty_tab/selectExtantQtyList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �շ������
    @RequestMapping(value = "selectTSummaryList", method = RequestMethod.POST)
    @ResponseBody
    private String selectTSummaryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/selectTSummaryList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("chkTm2") !=null && !map.get("chkTm2").equals("")) {
                map.put("chkTm2", map.get("chkTm2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("chkTm1") !=null && !map.get("chkTm1").equals("")) {
                map.put("chkTm1", map.get("chkTm1").toString() + " 00:00:00");
            }
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectTSummaryList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);


                resp = invtyTabService.selectTSummary(map);
            }
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("�շ�����ܱ�����", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/invty_tab/selectTSummaryList", false, e.getMessage(), null);
            if (prin) {
                misLogDAO.insertSelective(new MisLog("�շ�����ܱ�����", "�ֿ�", null, jsonBody, request, e));
            }

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���������
    @RequestMapping(value = "outIntoWhsTyp", method = RequestMethod.POST)
    @ResponseBody
    private String outIntoWhsTyp(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/outIntoWhsTyp");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            resp = invtyTabService.outIntoWhsTyp(map);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/invty_tab/outIntoWhsTyp", false, "��ѯʧ�ܣ�", null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ʧЧ����ά��
    @RequestMapping(value = "selectInvldtnDtList", method = RequestMethod.POST)
    @ResponseBody
    private String queryInvldtnDtList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/selectInvldtnDtList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/invty_tab/queryInvtyTabList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.queryInvldtnDtList(map);
            }

            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("ʧЧ����ά��������", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/invty_tab/selectInvldtnDtList", false, e.getMessage(), null);
            if (prin) {
                misLogDAO.insertSelective(new MisLog("ʧЧ����ά������", "�ֿ�", null, jsonBody, request, e));
            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���λ��ܱ�
    @RequestMapping(value = "queryBatNumSummaryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryBatNumSummaryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/queryBatNumSummaryList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("chkTm2") !=null && !map.get("chkTm2").equals("")) {
                map.put("chkTm2", map.get("chkTm2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("chkTm1") !=null && !map.get("chkTm1").equals("")) {
                map.put("chkTm1", map.get("chkTm1").toString() + " 00:00:00");
            }
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/invty_tab/queryInvtyTabList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.selectTSummary(map);
            }
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("���λ��ܱ���", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/invty_tab/queryBatNumSummaryList", false, e.getMessage(), null);
            if (prin) {
                misLogDAO.insertSelective(new MisLog("���λ��ܱ���", "�ֿ�", null, jsonBody, request, e));
            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����̨��
    @RequestMapping(value = "selectBatNumStandList", method = RequestMethod.POST)
    @ResponseBody
    private String selectBatNumStandList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/selectBatNumStandList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") !=null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("chkTm2") !=null && !map.get("chkTm2").equals("")) {
                map.put("chkTm2", map.get("chkTm2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") !=null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
            if (map.get("chkTm1") !=null && !map.get("chkTm1").equals("")) {
                map.put("chkTm1", map.get("chkTm1").toString() + " 00:00:00");
            }
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/invty_tab/queryInvtyTabList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = invtyTabService.InvtyStandList(map);
            }

            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                resp = invtyTabService.InvtyStandList(map);

                misLogDAO.insertSelective(new MisLog("����̨�˱���", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/invty_tab/selectBatNumStandList", false, e.getMessage(), null);
            if (prin) {
                misLogDAO.insertSelective(new MisLog("����̨�˱���", "�ֿ�", null, jsonBody, request, e));
            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // δ��˵���
    @RequestMapping(value = "selectNtChkNoList", method = RequestMethod.POST)
    @ResponseBody
    private String selectNtChkNoList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/invty_tab/selectNtChkNoList");
        logger.info("���������" + jsonBody);
        String resp = "";
        boolean prin = false;

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            if (map.get("formNum") !=null && !map.get("formNum").equals("")) {
                String[] strings = map.get("formNum").toString().split(",");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < strings.length; i++) {
                    list.add(strings[i]);
                }
                map.put("formNum", list);
            } else {
                map.remove("formNum");

            }
            if (map.get("formTypEncd")!=null && !map.get("formTypEncd").equals("")) {
                String[] strings = map.get("formTypEncd").toString().split(",");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < strings.length; i++) {
                    list.add(strings[i]);
                }
                map.put("formTypEncd", list);
            } else {
                map.remove("formTypEncd");

            }
            resp = invtyTabService.selectNtChkNoList(map);
            if (prin = !(map.containsKey("pageNo") && map.containsKey("pageSize"))) {
                misLogDAO.insertSelective(new MisLog("δ��˵��ݵ���", "�ֿ�", null, jsonBody, request));
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/invty_tab/selectNtChkNoList", false, e.getMessage(), null);
            if (prin) {
                misLogDAO.insertSelective(new MisLog("δ��˵��ݵ���", "�ֿ�", null, jsonBody, request, e));
            }
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����
    @RequestMapping("uploadFileAddDb")
    @ResponseBody
    public Object uploadPursOrderFile(HttpServletRequest request) throws IOException {
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
                    return BaseJson.returnRespObj("whs/invty_tab/uploadFileAddDb", false, "��ѡ���ļ���", null);
                }
                return invtyTabService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/invty_tab/uploadFileAddDb", false, "��ѡ���ļ���", null);

            }
        } catch (Exception e) {


            return BaseJson.returnRespObj("whs/invty_tab/uploadFileAddDb", false, e.getMessage(), null);

        }

    }

}
