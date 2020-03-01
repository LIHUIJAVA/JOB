package com.px.mis.whs.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.ListView;

import com.px.mis.system.service.impl.MisUserServiceImpl;
import com.px.mis.util.CommonUtil;
import com.px.mis.whs.dao.IntoWhsMapper;
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
import com.px.mis.account.service.FormBookService;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.system.service.MisUserService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;
import com.px.mis.whs.service.OthOutIntoWhsService;

//���������ĵ���      PDA�ĳ����
@Controller
@RequestMapping("/whs/out_into_whs")
public class OthOutIntoWhsController {

    private static final Logger logger = LoggerFactory.getLogger(OthOutIntoWhsController.class);
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    OthOutIntoWhsService othOutIntoWhsService;
    @Autowired
    IntoWhsMapper intoWhsMapper;
    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;
    @Autowired
    InvtyNumMapper invtyNumMapper;
    @Autowired
    WhsDocMapper whsDocMapper;
    //	����
    @Autowired
    FormBookService formBookService;
    @Autowired
    MisUserServiceImpl misUserService;

    // �������������
    @RequestMapping(value = "insertOthOutIntoWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object insertOthOutIntoWhs(@RequestBody String jsonBody) throws IOException {
        // System.out.println(jsonBody);
        logger.info("url:whs/out_into_whs/insertOthOutIntoWhs");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            String loginTime = BaseJson.getReqHead(jsonBody).get("loginTime").asText();

            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/insertOthOutIntoWhs", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            // ����
            OthOutIntoWhs oSngl = BaseJson.getPOJO(jsonBody, OthOutIntoWhs.class);
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            if (!misUserService.isWhsPer(jsonBody, oSngl.getWhsEncd())) {
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            oSngl.setSetupPers(userName);
            // �ӱ�
            List<OthOutIntoWhsSubTab> aList = BaseJson.getPOJOList(jsonBody, OthOutIntoWhsSubTab.class);

            for (OthOutIntoWhsSubTab subTab : aList) {
                subTab.setFormNum(oSngl.getFormNum());// ������������ȡ�����ӱ�
                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
            }

            resp = othOutIntoWhsService.insertOthOutIntoWhs(userId, oSngl, aList, loginTime);
            misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/out_into_whs/insertOthOutIntoWhs", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);

        return resp;
    }

    // �޸����������
    @RequestMapping(value = "updateOthOutIntoWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object updateOthOutIntoWhs(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/out_into_whs/updateOthOutIntoWhs");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/updateOthOutIntoWhs", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            // ����
            OthOutIntoWhs oSngl = BaseJson.getPOJO(jsonBody, OthOutIntoWhs.class);
            oSngl.setMdfr(userName);
            oSngl.setModiTm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if (!misUserService.isWhsPer(jsonBody, oSngl.getWhsEncd())) {
                throw new RuntimeException("�û�û�вֿ�Ȩ��");
            }
            // �ӱ�
            List<OthOutIntoWhsSubTab> aList = BaseJson.getPOJOList(jsonBody, OthOutIntoWhsSubTab.class);

            for (OthOutIntoWhsSubTab subTab : aList) {
                subTab.setFormNum(oSngl.getFormNum());// ������������ȡ�����ӱ�
                subTab.setPrdcDt(StringUtils.trimToNull(subTab.getPrdcDt()));
                subTab.setInvldtnDt(StringUtils.trimToNull(subTab.getInvldtnDt()));
            }

            resp = othOutIntoWhsService.updateOthOutIntoWhs(oSngl, aList);
            misLogDAO.insertSelective(new MisLog("�޸����������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�޸����������", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOthOutIntoWhs", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����ɾ��
    @RequestMapping(value = "deleteAllOthOutIntoWhs", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteAllOthOutIntoWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/deleteAllOthOutIntoWhs");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
//			if(formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
//			resp = BaseJson.returnRespObj("whs/out_into_whs/deleteAllOthOutIntoWhs", false, "�����ѷ��ˣ�", null);
//			logger.info("���ز�����" + resp);
//			return resp;
//		}
            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String formNum = reqBody.get("formNum").asText();
            // System.out.println(formNum);
            resp = othOutIntoWhsService.deleteAllOthOutIntoWhs(formNum);
            misLogDAO.insertSelective(new MisLog("ɾ�����������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("ɾ�����������", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/out_into_whs/deleteAllOthOutIntoWhs", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��Ʒ�ṹ
    // ��ѯ����ҳ��
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    private String query(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/query");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String formNum = reqBody.get("formNum").asText();
            resp = othOutIntoWhsService.query(formNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/out_into_whs/query", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �򵥲� ��Ʒ�ṹ
    // ��ѯ����ҳ��
    @RequestMapping(value = "queryMovBitTab", method = RequestMethod.POST)
    @ResponseBody
    private String queryMovBitTab(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/queryMovBitTab");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String formNum = reqBody.get("formNum").asText();
            String invtyEncd = reqBody.get("invtyEncd").asText();
            String batNum = reqBody.get("batNum").asText();

            resp = othOutIntoWhsService.queryMovBitTab(formNum, invtyEncd, batNum);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    @RequestMapping(value = "queryList", method = RequestMethod.POST)
    @ResponseBody
    private String queryList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/queryList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") != null && !map.get("formDt2").equals("")) {

                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") != null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }

            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryList", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = othOutIntoWhsService.queryList(map);
            }
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/out_into_whs/queryList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ���
    @RequestMapping(value = "updateOutInWhsChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateOutInWhsChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/out_into_whs/updateOutInWhsChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsChk", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            String loginTime = CommonUtil.getLoginTime(BaseJson.getReqHead(jsonBody).get("loginTime").asText());
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
                try {
                    resp += othOutIntoWhsService.updateOutInWhsChk(userName, dbzhu, loginTime) + "\n";
                    misLogDAO.insertSelective(new MisLog("������������", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("������������", "�ֿ�", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }

            }
            // ���
            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsChk", isSuccess, resp, null);

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("������������", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsChk", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ����
    @RequestMapping(value = "updateOutInWhsNoChk", method = RequestMethod.POST)
    @ResponseBody
    private String updateOutInWhsNoChk(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/out_into_whs/updateOutInWhsNoChk");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(BaseJson.getReqHead(jsonBody).get("loginTime").asText())) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsNoChk", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            Optional.ofNullable(userId).orElseThrow(() -> new Exception("��¼ʧЧ�������µ�¼"));
            ObjectNode jdRespJson;
            JsonNode resultJson;
            ArrayNode listJson = null;
            Boolean isSuccess = true;

            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
            resultJson = jdRespJson.get("reqBody");
            listJson = (ArrayNode) resultJson.get("list");

            // ����
            Set<String> set = new HashSet<String>();
            for (Iterator<JsonNode> dbzhuIterator = listJson.iterator(); dbzhuIterator.hasNext(); ) {
                String dbzhu = dbzhuIterator.next().toString();
                set.add(dbzhu);
            }
            for (String dbzhu : set) {
                try {
                    resp += othOutIntoWhsService.updateOutInWhsNoChk(userName, dbzhu) + "\n";
                    misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request, "�ɹ�", dbzhu));

                } catch (Exception e) {
                    misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request, e));


                    resp += e.getMessage() + "\n";
                    isSuccess = false;

                }
            }

            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsNoChk", isSuccess, resp, null);

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsNoChk", false, e.getMessage(), null);

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

    // PDA ���������
    // ����ѯ
    @RequestMapping(value = "selectOINChkr", method = RequestMethod.POST)
    @ResponseBody
    public Object selectOINChkr(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/selectOINChkr");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
// 			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = othOutIntoWhsService.selectOINChkr(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/out_into_whs/selectOINChkr", false, "�û���ҵ��ֿ�", null);
            }
            // System.out.println("othOutIntoWhsService.selectOINChkr(whsEncd)");
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/out_into_whs/selectOINChkr", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �����ѯ
    @RequestMapping(value = "selectOutChkr", method = RequestMethod.POST)
    @ResponseBody
    public Object selectOutChkr(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/selectOutChkr");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = othOutIntoWhsService.selectOutChkr(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/out_into_whs/selectOutChkr", false, "�û���ҵ��ֿ�", null);
            }
            // System.out.println("OthOutIntoWhsSubTab");
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/out_into_whs/selectOutChkr", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��������� ���
    @RequestMapping(value = "uInvMov", method = RequestMethod.POST)
    @ResponseBody
    public Object uInvMov(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/uInvMov");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/uInvMov", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            List<OthOutIntoWhsSubTab> othWhslist = new ArrayList<OthOutIntoWhsSubTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");

//            JSONArray array = JSON.parseArray(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("list"));
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");

//                OthOutIntoWhsSubTab intoWhsSubTab = JSON.parseObject(str.toString(), OthOutIntoWhsSubTab.class);
                OthOutIntoWhsSubTab intoWhsSubTab = JacksonUtil.getPOJO(str, OthOutIntoWhsSubTab.class);
                othWhslist.add(intoWhsSubTab);
//                MovBitTab oSubTab = JSON.parseObject(str.toString(), MovBitTab.class);
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("formNum").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());
                String[] strs = oSubTab.getGdsBitEncd().split(",");
                String[] qtys = qty.split(",");
                for (int j = 0; j < strs.length; j++) {
                    MovBitTab oSubTabs = new MovBitTab();
                    org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);

                    String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), strs[j]);
                    if (regnEncd == null) {
                        logger.info("�����λ�������Ƿ����");
                        throw new RuntimeException("����ֿ��λ��Ӧ��ϵ�Ƿ����");
                    }
                    oSubTabs.setRegnEncd(regnEncd);
                    oSubTabs.setQty(new BigDecimal(qtys[j]));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            OthOutIntoWhs outInto = new OthOutIntoWhs();

            outInto.setFormNum(BaseJson.getReqBody(jsonBody).get("formNum").asText());
            outInto.setChkr(userName);
            resp = othOutIntoWhsService.uInvMov(null, othWhslist, list, outInto);

            if (true) {
                logger.info("���ز�����" + resp);
                return resp;
            }

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            boolean isSuccess = true;
            List<OthOutIntoWhs> oList = new ArrayList<>();// �����������
            List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
            List<InvtyTab> invtyList = new ArrayList<>();// ����
            List<MovBitTab> mList = new ArrayList<>();// ��λ��

            String danHao = reqBody.get("formNum").asText().toString();
            // �����������
            OthOutIntoWhs outIntoWhs = othOutIntoWhsMapper.selectIsChk(danHao);
            // ��ѯ�ӱ�
            oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
            // ����
            // System.out.println("danHao" + danHao);

            outIntoWhs.setInStatus("�������");// ���״̬(������--�������)
            outIntoWhs.setIsNtChk(1);// �Ƿ����(0δ��� 1�����)
            outIntoWhs.setOperator(reqBody.get("operator").asText().toString());// ������
            outIntoWhs.setOperatorId(reqBody.get("operatorId").asText().toString());// �����˱���

            // System.out.println("oSubTabList\t" + oSubTabList.size());
            for (OthOutIntoWhsSubTab tab : oSubTabList) {

                // ����
                // System.out.println(tab.getInvtyEncd() + "\ttab.getInvtyEncd()");
                InvtyTab invtyTab = new InvtyTab();
                invtyTab.setWhsEncd(outIntoWhs.getWhsEncd());// �ֿ����
                invtyTab.setInvtyEncd(tab.getInvtyEncd());// �������
                invtyTab.setBatNum(tab.getBatNum());// ����
                invtyTab.setNowStok(tab.getQty());// �ִ���
                invtyTab.setAvalQty(tab.getQty());// ������
                invtyTab.setPrdcDt(tab.getPrdcDt());// ��������
                invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// ������
                invtyTab.setInvldtnDt(tab.getInvldtnDt());// ʧЧ����
//			BigDecimal cntnTaxUprc=new BigDecimal(tab.get("cntnTaxUprc").asText());
                invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// ��˰����
//			BigDecimal unTaxUprc=new BigDecimal(tab.get("unTaxUprc").asText());
                invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// δ˰����
//			BigDecimal taxRate=new BigDecimal(tab.get("taxRate").asText());
                invtyTab.setTaxRate(tab.getTaxRate());// ˰��
//			BigDecimal cntnTaxAmt=new BigDecimal(tab.get("cntnTaxAmt").asText());
                invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// ��˰���
//			BigDecimal unTaxAmt=new BigDecimal(tab.get("unTaxAmt").asText());
                invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// δ˰���

                invtyList.add(invtyTab);
            }
            // System.out.println("invtyList" + invtyList.size());
            // ���ӱ�
            List<Map> oTabMap = BaseJson.getList(jsonBody);

            // System.out.println("oTabMap.size()\t" + oTabMap.size());
            List<OthOutIntoWhsSubTab> oSubTabLists = new ArrayList<>();// ���Ĺ�������
            for (Map map : oTabMap) {

                // System.out.println(map);

                String ordrNum = map.get("ordrNum").toString();
                String qty = map.get("qty").toString();
                String whsEncd = map.get("whsEncd").toString();
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
                String intlBat = map.get("intlBat").toString();// ��������
                String gdsBitEncd = map.get("gdsBitEncd").toString();

                // ����������
                String[] Qty = qty.split(",");// 10,20,30
                for (InvtyTab tab : invtyList) {

                    // System.out.println(tab.getInvtyEncd() + "\t@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    if (tab.getWhsEncd().equals(whsEncd) && tab.getInvtyEncd().equals(invtyEncd)
                            && tab.getBatNum().equals(batNum)) {
                        double dQty = 0;
                        for (int m = 0; m < Qty.length; m++) {
                            dQty += new BigDecimal(Qty[m]).doubleValue();
                            // System.out.println("Qty[m]" + Qty[m]);
                        }
                        // System.out.println("tab.getNowStok().compareTo(new BigDecimal(dQty) )\t"
//                                + tab.getNowStok().compareTo(new BigDecimal(dQty)));
                        if (tab.getNowStok().compareTo(new BigDecimal(dQty)) != 0) {
                            // System.out.println("�������ԣ������ⵥ");
                        }
                    }

                }

                // System.out.println("��������������������������������������������������������������������������������������������������������������������������");

                String[] gBitEncd = gdsBitEncd.split(",");
                MovBitTab movBitTab;
                for (int m = 0; m < gBitEncd.length; m++) {
                    // map.put("qty", Qty[m]);
                    movBitTab = new MovBitTab();
                    movBitTab.setInvtyEncd(invtyEncd);
                    // �������
                    movBitTab.setWhsEncd(whsEncd);
                    // �ֿ����
                    movBitTab.setBatNum(batNum);// ���κ�
                    movBitTab.setGdsBitEncd(gBitEncd[m]);// ��λ����
//				String qString = map.get("qty").toString();
                    movBitTab.setQty(new BigDecimal(Qty[m]));// ����
                    mList.add(movBitTab);
                }
                // System.out.println("1111111111111111111111111111111111111");

                OthOutIntoWhsSubTab othOutIntoWhsSubTab = new OthOutIntoWhsSubTab();

                othOutIntoWhsSubTab.setOrdrNum(Long.parseLong(ordrNum));// ���
                othOutIntoWhsSubTab.setIntlBat(intlBat);// ��������
                oSubTabLists.add(othOutIntoWhsSubTab);
            }

            /**
             *
             *
             *
             * ///////////////////////////////////////////////////////////////////////////////////////////////////////
             * // ���� OthOutIntoWhs oSngl = BaseJson.getPOJO(jsonBody, OthOutIntoWhs.class);
             *
             *
             * // ������� �޸����״̬��������--������ɡ� OthOutIntoWhs outIntoWhs = new OthOutIntoWhs();
             * outIntoWhs.setFormNum(oSngl.getFormNum());// ���ݺ�
             * outIntoWhs.setInStatus(oSngl.getInStatus());// ���״̬(������--�������)
             * outIntoWhs.setIsNtChk(oSngl.getIsNtChk());// �Ƿ����(0δ��� 1�����)
             * outIntoWhs.setOperator(oSngl.getOperator());// ������
             * outIntoWhs.setOperatorId(oSngl.getOperatorId());// �����˱���
             *
             * // �ӱ� List<Map> oTabMap = BaseJson.getList(jsonBody);
             *
             * List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ������ӱ�
             * List<InvtyTab> iList = new ArrayList<>();// ���� List<MovBitTab> mList = new
             * ArrayList<>();// ��λ��
             *
             * for (Map map : oTabMap) { String qty = map.get("qty").toString(); String
             * whsEncd = map.get("whsEncd").toString(); String invtyEncd =
             * map.get("invtyEncd").toString(); String batNum =
             * map.get("batNum").toString(); String ordrNum =
             * map.get("ordrNum").toString();// �ӱ���� String intlBat =
             * map.get("intlBat").toString();// �������� String gdsBitEncd =
             * map.get("gdsBitEncd").toString();
             *
             * // ���������� String[] Qty = qty.split(",");// 10,20,30 double dQty = 0; for (int
             * m = 0; m < Qty.length; m++) { dQty += new BigDecimal(Qty[m]).doubleValue(); }
             *
             * // ������� ȡ����������� OthOutIntoWhsSubTab othOutIntoWhsSubTab = new
             * OthOutIntoWhsSubTab(); othOutIntoWhsSubTab.setQty(new BigDecimal(dQty));//
             * ������ȡ�������+ԭ������=�ֿ�������
             *
             * othOutIntoWhsSubTab.setOrdrNum(Long.parseLong(ordrNum));// ���
             * othOutIntoWhsSubTab.setIntlBat(intlBat);// ��������
             * oSubTabList.add(othOutIntoWhsSubTab);
             *
             * // ���� ���� InvtyTab invtyTab = new InvtyTab(); invtyTab.setWhsEncd(whsEncd);//
             * �ֿ����(�ӱ�������) invtyTab.setInvtyEncd(invtyEncd);// �������
             * invtyTab.setBatNum(batNum);// ���� invtyTab.setNowStok(new BigDecimal(dQty));
             * invtyTab.setAvalQty(new BigDecimal(dQty));
             *
             * invtyTab.setPrdcDt(map.get("prdcDt").toString());
             * invtyTab.setBaoZhiQi(map.get("baoZhiQi").toString());
             * invtyTab.setInvldtnDt(map.get("invldtnDt").toString()); BigDecimal
             * cntnTaxUprc = new BigDecimal(map.get("cntnTaxUprc").toString());
             * invtyTab.setCntnTaxUprc(cntnTaxUprc); BigDecimal unTaxUprc = new
             * BigDecimal(map.get("unTaxUprc").toString());
             * invtyTab.setUnTaxUprc(unTaxUprc); BigDecimal taxRate = new
             * BigDecimal(map.get("taxRate").toString()); invtyTab.setTaxRate(taxRate);
             * BigDecimal cntnTaxAmt = new BigDecimal(map.get("cntnTaxAmt").toString());
             * invtyTab.setCntnTaxAmt(cntnTaxAmt); BigDecimal unTaxAmt = new
             * BigDecimal(map.get("unTaxAmt").toString()); invtyTab.setUnTaxAmt(unTaxAmt);
             * // ������� ��λ���� iList.add(invtyTab);
             *
             * // ��λ�� (����ʱ���������������������ڻ�λ�����ӣ����û����������) String[] gBitEncd =
             * gdsBitEncd.split(","); MovBitTab movBitTab; for (int m = 0; m <
             * gBitEncd.length; m++) { map.put("qty", Qty[m]); movBitTab = new MovBitTab();
             * movBitTab.setInvtyEncd(invtyEncd); // ������� movBitTab.setWhsEncd(whsEncd); //
             * �ֿ���� movBitTab.setBatNum(batNum);// ���κ�
             * movBitTab.setGdsBitEncd(gBitEncd[m]);// ��λ���� String qString =
             * map.get("qty").toString(); movBitTab.setQty(new BigDecimal(qString));// ����
             * mList.add(movBitTab); }
             *
             * }
             */
            // System.out.println(
//                    "invtyList" + invtyList.size() + "\toSubTabList" + oSubTabLists.size() + "\tmList" + mList.size());
            if (isSuccess) {
                resp = othOutIntoWhsService.uInvMov(invtyList, oSubTabLists, mList, outIntoWhs);
                misLogDAO.insertSelective(new MisLog("PDA��������� ���", "�ֿ�", null, jsonBody, request));

            } else {
                resp = BaseJson.returnRespObj("whs/out_into_whs/uInvMov", true, resp, null);

            }

        } catch (Exception e2) {


            misLogDAO.insertSelective(new MisLog("PDA��������� ���", "�ֿ�", null, jsonBody, request, e2));

            resp = BaseJson.returnRespObj("whs/out_into_whs/uInvMov", false, e2.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��������� ����
    @RequestMapping(value = "uOutvMov", method = RequestMethod.POST)
    @ResponseBody
    public String selectCheckSnglStauts(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/uOutvMov");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            List<MovBitTab> list = new ArrayList<MovBitTab>();
//            JSONArray array = JSON.parseArray(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("list"));
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");

            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");

//                MovBitTab oSubTab = JSON.parseObject(str.toString(), MovBitTab.class);
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
//                oSubTab.setOrderNum(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("formNum"));
//                oSubTab.setSerialNum(str.getString("ordrNum"));

                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("formNum").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());


                String[] strs = oSubTab.getGdsBitEncd().split(",");
                String[] qtys = qty.split(",");
                for (int j = 0; j < strs.length; j++) {
                    MovBitTab oSubTabs = new MovBitTab();
                    org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);

                    String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), strs[j]);
                    if (regnEncd == null) {
                        logger.info("�����λ�������Ƿ����");
                        throw new RuntimeException("����ֿ��λ��Ӧ��ϵ�Ƿ����");
                    }
                    oSubTabs.setRegnEncd(regnEncd);

                    oSubTabs.setQty(new BigDecimal(qtys[j]));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            OthOutIntoWhs outInto = new OthOutIntoWhs();
//            outInto.setFormNum(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("formNum"));
            outInto.setFormNum(BaseJson.getReqBody(jsonBody).get("formNum").asText());
            outInto.setChkr(userName);
            resp = othOutIntoWhsService.uOutvMov(null, null, list, outInto);

            if (true) {
                logger.info("���ز�����" + resp);
                return resp;
            }

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);

            List<OthOutIntoWhs> oList = new ArrayList<>();// �����������
            List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
            List<InvtyTab> invtyList = new ArrayList<>();// ����
            List<MovBitTab> mList = new ArrayList<>();// ��λ��

            String danHao = reqBody.get("formNum").asText().toString();
            // �����������
            OthOutIntoWhs outIntoWhs = othOutIntoWhsMapper.selectIsChk(danHao);
            // ��ѯ�ӱ�
            oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
            // ����
            // System.out.println("danHao" + danHao);

            outIntoWhs.setInStatus("�������");// ���״̬(������--�������)
            outIntoWhs.setIsNtChk(1);// �Ƿ����(0δ��� 1�����)
            outIntoWhs.setOperator(reqBody.get("operator").asText().toString());// ������
            outIntoWhs.setOperatorId(reqBody.get("operatorId").asText().toString());// �����˱���

            // System.out.println("oSubTabList\t" + oSubTabList.size());
            for (OthOutIntoWhsSubTab tab : oSubTabList) {

                // ����
                // System.out.println(tab.getInvtyEncd() + "\ttab.getInvtyEncd()");
                InvtyTab invtyTab = new InvtyTab();
                invtyTab.setWhsEncd(outIntoWhs.getWhsEncd());// �ֿ����
                invtyTab.setInvtyEncd(tab.getInvtyEncd());// �������
                invtyTab.setBatNum(tab.getBatNum());// ����
                invtyTab.setNowStok(tab.getQty());// �ִ���
                invtyTab.setAvalQty(tab.getQty());// ������
                invtyTab.setPrdcDt(tab.getPrdcDt());// ��������
                invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// ������
                invtyTab.setInvldtnDt(tab.getInvldtnDt());// ʧЧ����
//			BigDecimal cntnTaxUprc=new BigDecimal(tab.get("cntnTaxUprc").asText());
                invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// ��˰����
//			BigDecimal unTaxUprc=new BigDecimal(tab.get("unTaxUprc").asText());
                invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// δ˰����
//			BigDecimal taxRate=new BigDecimal(tab.get("taxRate").asText());
                invtyTab.setTaxRate(tab.getTaxRate());// ˰��
//			BigDecimal cntnTaxAmt=new BigDecimal(tab.get("cntnTaxAmt").asText());
                invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// ��˰���
//			BigDecimal unTaxAmt=new BigDecimal(tab.get("unTaxAmt").asText());
                invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// δ˰���

                invtyList.add(invtyTab);
            }
            // System.out.println("invtyList" + invtyList.size());
            // ���ӱ�
            List<Map> oTabMap = BaseJson.getList(jsonBody);

            // System.out.println("oTabMap.size()\t" + oTabMap.size());
            for (Map map : oTabMap) {

                // System.out.println(map);

                String qty = map.get("qty").toString();
                String whsEncd = map.get("whsEncd").toString();
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();

                String gdsBitEncd = map.get("gdsBitEncd").toString();

                // ����������
                String[] Qty = qty.split(",");// 10,20,30
                for (InvtyTab tab : invtyList) {

                    // System.out.println(tab.getInvtyEncd() + "\t\"\\@@@@@@@@@@@@@@@@@@@@@@@@@2");
                    if (tab.getWhsEncd().equals(whsEncd) && tab.getInvtyEncd().equals(invtyEncd)
                            && tab.getBatNum().equals(batNum)) {
                        double dQty = 0;
                        for (int m = 0; m < Qty.length; m++) {
                            dQty += new BigDecimal(Qty[m]).doubleValue();
                            // System.out.println("Qty[m]" + Qty[m]);
                        }
                        // System.out.println("tab.getNowStok().compareTo(new BigDecimal(dQty) )\t"
//                                + tab.getNowStok().compareTo(new BigDecimal(dQty)));
                        if (tab.getNowStok().compareTo(new BigDecimal(dQty)) != 0) {
                            // System.out.println("�������ԣ�����ⵥ");
                        }
                    }

                }

                // System.out.println("��������������������������������������������������������������������������������������������������������������������������");

                String[] gBitEncd = gdsBitEncd.split(",");
                MovBitTab movBitTab;
                for (int m = 0; m < gBitEncd.length; m++) {
                    // map.put("qty", Qty[m]);
                    movBitTab = new MovBitTab();
                    movBitTab.setInvtyEncd(invtyEncd);
                    // �������
                    movBitTab.setWhsEncd(whsEncd);
                    // �ֿ����
                    movBitTab.setBatNum(batNum);// ���κ�
                    movBitTab.setGdsBitEncd(gBitEncd[m]);// ��λ����
//				String qString = map.get("qty").toString();
                    movBitTab.setQty(new BigDecimal(Qty[m]));// ����
                    mList.add(movBitTab);
                }
                // System.out.println("1111111111111111111111111111111111111");

            }

            /**
             * // ���� OthOutIntoWhs oSngl = BaseJson.getPOJO(jsonBody, OthOutIntoWhs.class);
             *
             * // ������� �޸����״̬��������--������ɡ� OthOutIntoWhs outIntoWhs = new OthOutIntoWhs();
             * outIntoWhs.setFormNum(oSngl.getFormNum());// ���ݺ�
             * outIntoWhs.setOutStatus(oSngl.getOutStatus()); // ����״̬(������--�������)
             * outIntoWhs.setIsNtChk(oSngl.getIsNtChk());// �Ƿ����(0δ��� 1�����)
             * outIntoWhs.setOperator(oSngl.getOperator());// ������
             * outIntoWhs.setOperatorId(oSngl.getOperatorId());// �����˱���
             *
             * // �ӱ� List<Map> oTabMap = BaseJson.getList(jsonBody);
             *
             * List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ������ӱ�
             * List<InvtyTab> iList = new ArrayList<>();// ���� List<MovBitTab> mList = new
             * ArrayList<>();// ��λ��
             *
             * for (Map map : oTabMap) { String qty = map.get("qty").toString(); String
             * whsEncd = map.get("whsEncd").toString(); String invtyEncd =
             * map.get("invtyEncd").toString(); String batNum =
             * map.get("batNum").toString(); String gdsBitEncd =
             * map.get("gdsBitEncd").toString();
             *
             * // ���������� String[] Qty = qty.split(",");// 10,20,30 double dQty = 0; for (int
             * m = 0; m < Qty.length; m++) { dQty += new BigDecimal(Qty[m]).doubleValue(); }
             *
             * // ������� ȡ����������� OthOutIntoWhsSubTab othOutIntoWhsSubTab = new
             * OthOutIntoWhsSubTab(); othOutIntoWhsSubTab.setQty(new BigDecimal(dQty));// ����
             * oSubTabList.add(othOutIntoWhsSubTab);
             *
             * // ���� ���� InvtyTab invtyTab = new InvtyTab(); invtyTab.setWhsEncd(whsEncd);//
             * �ֿ����(�ӱ�������) invtyTab.setInvtyEncd(invtyEncd);// �������
             * invtyTab.setBatNum(batNum);// ���� invtyTab.setNowStok(new BigDecimal(dQty));
             * invtyTab.setAvalQty(new BigDecimal(dQty)); iList.add(invtyTab);
             *
             * // ��λ��
             *
             * String[] gBitEncd = gdsBitEncd.split(","); MovBitTab movBitTab; for (int m =
             * 0; m < gBitEncd.length; m++) {
             *
             * map.put("qty", Qty[m]); // System.out.println(map.get("qty").toString());
             * movBitTab = new MovBitTab(); movBitTab.setInvtyEncd(invtyEncd); ;// �������
             * movBitTab.setWhsEncd(whsEncd); ;// �ֿ���� movBitTab.setBatNum(batNum);// ���κ�
             * movBitTab.setGdsBitEncd(gBitEncd[m]);// ��λ���� String qString =
             * map.get("qty").toString(); movBitTab.setQty(new BigDecimal(qString));// ����
             * mList.add(movBitTab);
             *
             * } }
             */
            resp = othOutIntoWhsService.uOutvMov(invtyList, oSubTabList, mList, outIntoWhs);
            misLogDAO.insertSelective(new MisLog("PDA��������� ����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("PDA��������� ����", "�ֿ�", null, jsonBody, request, e));


            resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);

        return resp;
    }

    @RequestMapping(value = "updateMovBit", method = RequestMethod.POST)
    @ResponseBody
    public Object updateMovBit(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/out_into_whs/updateMovBit");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            List<Map> mTabMap = BaseJson.getList(jsonBody);
            List<MovBitTab> mList = new ArrayList();
            for (Map map : mTabMap) {
                MovBitTab movBitTab = new MovBitTab();
                BeanUtils.populate(movBitTab, map);
                mList.add(movBitTab);
            }

            resp = othOutIntoWhsService.updateMovbitTab(mList);
            misLogDAO.insertSelective(new MisLog("���Ļ�λ���������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("���Ļ�λ���������", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ӡ
    @RequestMapping(value = "queryListDaYin", method = RequestMethod.POST)
    @ResponseBody
    private String queryListDaYin(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/queryListDaYin");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            if (map.get("formDt2") != null && !map.get("formDt2").equals("")) {
                map.put("formDt2", map.get("formDt2").toString() + " 23:59:59");
            }
            if (map.get("formDt1") != null && !map.get("formDt1").equals("")) {
                map.put("formDt1", map.get("formDt1").toString() + " 00:00:00");
            }
//            misUserService.selectUserWhs(jsonBody, map);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if (StringUtils.isBlank(whsId)) {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryListDaYin", false, "�û�û�вֿ�Ȩ��", null);
            } else {
                map.put("whsId", whsId);

                resp = othOutIntoWhsService.queryListDaYin(map);
            }
            misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�������������", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/out_into_whs/queryListDaYin", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��������
    @RequestMapping(value = "wholeSingleLianZha", method = RequestMethod.POST)
    @ResponseBody
    public String wholeSingleLianZha(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/out_into_whs/wholeSingleLianZha");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            String formNum = BaseJson.getReqBody(jsonBody).get("formNum").asText();
            String formTypEncd = BaseJson.getReqBody(jsonBody).get("formTypEncd").asText();
//            JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("formTypEncd");

            resp = othOutIntoWhsService.wholeSingleLianZha(formNum, formTypEncd);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/out_into_whs/wholeSingleLianZha", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ������������
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
                    return BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDb", false, "��ѡ���ļ�", null);
                }
                return othOutIntoWhsService.uploadFileAddDb(file);
            } else {
                return BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDb", false, "��ѡ���ļ�", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDb", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }

    // ���ݻ�λ
    @RequestMapping("insertInvtyGdsBitList")
    @ResponseBody
    public Object insertInvtyGdsBitList(@RequestBody String jsonBody) {
        logger.info("url:whs/out_into_whs/insertInvtyGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            List<MovBitTab> list = new ArrayList<MovBitTab>();

            String orderNum = BaseJson.getReqBody(jsonBody).get("orderNum").asText();
            String serialNum = BaseJson.getReqBody(jsonBody).get("serialNum").asText();
            String whsEncd = BaseJson.getReqBody(jsonBody).get("whsEncd").asText();
            String invtyEncd = BaseJson.getReqBody(jsonBody).get("invtyEncd").asText();
            String batNum = BaseJson.getReqBody(jsonBody).get("batNum").asText();
            String prdcDt = BaseJson.getReqBody(jsonBody).get("prdcDt").asText();
            prdcDt = (prdcDt == null || prdcDt.length() == 0) ? null : prdcDt;


//            JSONArray array = JSON.parseArray(BaseJson.getReqBody(jsonBody).get("list"));
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(orderNum);
                oSubTab.setSerialNum(serialNum);
                oSubTab.setWhsEncd(whsEncd);
                oSubTab.setInvtyEncd(invtyEncd);
                oSubTab.setBatNum(batNum);
                oSubTab.setPrdcDt(prdcDt);

                list.add(oSubTab);
            }
            resp = othOutIntoWhsService.insertInvtyGdsBitList(list,orderNum,serialNum);
            misLogDAO.insertSelective(new MisLog("�������ݻ�λ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("�������ݻ�λ", "�ֿ�", null, jsonBody, request, e));


            try {
                return BaseJson.returnRespObj("whs/out_into_whs/insertInvtyGdsBitList", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ɾ��
    @RequestMapping("deleteInvtyGdsBitList")
    @ResponseBody
    public Object deleteInvtyGdsBitList(@RequestBody String jsonBody) {
        logger.info("url:whs/out_into_whs/deleteInvtyGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

//            JSONObject dengji = JSON.parseObject(jsonBody).getJSONObject("reqBody");
            ObjectNode dengji = BaseJson.getReqBody(jsonBody);
            if (dengji.has("orderNum")) {
                resp = othOutIntoWhsService.deleteInvtyGdsBitList(dengji.get("num").asText(), "orderNum",
                        dengji.get("orderNum").asText());

            } else if (dengji.has("serialNum")) {
                resp = othOutIntoWhsService.deleteInvtyGdsBitList(dengji.get("num").asText(), "serialNum",
                        dengji.get("serialNum").asText());

            } else if (dengji.has("id")) {
                resp = othOutIntoWhsService.deleteInvtyGdsBitList(dengji.get("num").asText(), "id",
                        dengji.get("id").asText());

            } else {
                return BaseJson.returnRespObj("whs/out_into_whs/deleteInvtyGdsBitList", false, "�����������", null);
            }
            misLogDAO.insertSelective(new MisLog("ɾ�����ݻ�λ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("ɾ�����ݻ�λ", "�ֿ�", null, jsonBody, request, e));

            try {
                return BaseJson.returnRespObj("whs/out_into_whs/deleteInvtyGdsBitList", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ��ѯ
    @RequestMapping("queryInvtyGdsBitList")
    @ResponseBody
    public Object queryInvtyGdsBitList(@RequestBody String jsonBody) {
        logger.info("url:whs/out_into_whs/queryInvtyGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            String json = BaseJson.getReqBody(jsonBody).toString();

            resp = othOutIntoWhsService.queryInvtyGdsBitList(json);

        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/out_into_whs/queryInvtyGdsBitList", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // ���������λ
    @RequestMapping("allInvtyGdsBitList")
    @ResponseBody
    public Object allInvtyGdsBitList(@RequestBody String jsonBody) {
        logger.info("url:whs/out_into_whs/allInvtyGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            String formNum = null;

            formNum = BaseJson.getReqBody(jsonBody).get("formNum").asText();
            List<String> lists = getList(formNum);

            for (String list : lists) {
                try {
                    resp+= othOutIntoWhsService.allInvtyGdsBitList(list)+"\n";
                } catch (Exception e) {
                    resp += e.getMessage()+"\n";
                }
            }
            misLogDAO.insertSelective(new MisLog("���������λ", "�ֿ�", null, jsonBody, request));
            resp = BaseJson.returnRespObj("whs/out_into_whs/allInvtyGdsBitList", false, resp, null);
        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("���������λ", "�ֿ�", null, jsonBody, request, e));
            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/allInvtyGdsBitList", false, resp, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        logger.info("���ز�����" + resp);
        return resp;

    }

    // �޸�
    @RequestMapping("uploadInvtyGdsBitList")
    @ResponseBody
    public Object uploadInvtyGdsBitList(@RequestBody String jsonBody) {
        logger.info("url:whs/out_into_whs/uploadInvtyGdsBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            String orderNum = BaseJson.getReqBody(jsonBody).get("orderNum").asText();
            String serialNum = BaseJson.getReqBody(jsonBody).get("serialNum").asText();
            String whsEncd = BaseJson.getReqBody(jsonBody).get("whsEncd").asText();
            String invtyEncd = BaseJson.getReqBody(jsonBody).get("invtyEncd").asText();
            String batNum = BaseJson.getReqBody(jsonBody).get("batNum").asText();
            String prdcDt = BaseJson.getReqBody(jsonBody).get("prdcDt").asText();
            prdcDt = (prdcDt == null || prdcDt.length() == 0) ? null : prdcDt;


            List<MovBitTab> xinlist = new ArrayList<MovBitTab>();// ����
            List<MovBitTab> gailist = new ArrayList<MovBitTab>();// �޸�


//            JSONArray array = JSON.parseArray(BaseJson.getReqBody(jsonBody).get("list"));
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
//                MovBitTab oSubTab = JSON.parseObject(str, MovBitTab.class);
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);

                oSubTab.setOrderNum(orderNum);
                oSubTab.setSerialNum(serialNum);
                oSubTab.setWhsEncd(whsEncd);
                oSubTab.setInvtyEncd(invtyEncd);
                oSubTab.setBatNum(batNum);
                oSubTab.setPrdcDt(prdcDt);

//				orderNum = oSubTab.getOrderNum();
                if (oSubTab.getId() == null) {
                    xinlist.add(oSubTab);
                } else {
                    gailist.add(oSubTab);
                }

            }
            if (xinlist.size() == 0 && gailist.size() == 0) {
                return BaseJson.returnRespObj("whs/out_into_whs/uploadInvtyGdsBitList", false, "��ѡ����ȷ����", null);
            }
            resp = othOutIntoWhsService.uploadInvtyGdsBitList(xinlist, gailist, orderNum, serialNum);
            misLogDAO.insertSelective(new MisLog("�޸ĵ��ݻ�λ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("�޸ĵ��ݻ�λ", "�ֿ�", null, jsonBody, request, e));

            try {
                return BaseJson.returnRespObj("whs/out_into_whs/uploadInvtyGdsBitList", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

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
        Set<String> list = new HashSet<>();
        String[] str = id.split(",");
        for (int i = 0; i < str.length; i++) {
            list.add(str[i]);
        }
        return new ArrayList<>(list);
    }

    // ������������
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
                    return BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDbU8", false, "��ѡ���ļ�", null);
                }
                return othOutIntoWhsService.uploadFileAddDbU8(file);
            } else {
                return BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDbU8", false, "��ѡ���ļ�", null);

            }
        } catch (Exception e) {


            try {
                return BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDbU8", false, e.getMessage(), null);
            } catch (IOException e1) {


            }

        }
        return null;

    }
}
