package com.px.mis.whs.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.MovBitList;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.service.GdsBitDistionService;

//��λ�����
@Controller
@RequestMapping("/whs/gds_bit_distion")
public class GdsBitDistionController {

    private static final Logger logger = LoggerFactory.getLogger(GdsBitDistionController.class);

    @Autowired
    GdsBitDistionService gdsBitDistionService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;
    @Autowired
    WhsDocMapper whsDocMapper;

    // ��ѯ�ֿ⡢���򡢻�λ�������Ϣ
    // ��ѯ���вֿ�
    @RequestMapping(value = "selectWDoc", method = RequestMethod.POST)
    @ResponseBody
    private String selectWDoc(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectWDoc");
        String resp = "";
        try {

            resp = gdsBitDistionService.selectWDoc();
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectWDoc", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ��������
    @RequestMapping(value = "selectAllRegn", method = RequestMethod.POST)
    @ResponseBody
    private String selectRegn(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectAllRegn");
        logger.info("���������" + jsonBody);
        String resp = "";

        String whsEncd;
        try {

            whsEncd = BaseJson.getReqBody(jsonBody).get("whsEncd").asText();
            resp = gdsBitDistionService.selectRegn(whsEncd);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectAllRegn", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ���л�λ
    @RequestMapping(value = "selectAllBit", method = RequestMethod.POST)
    @ResponseBody
    private String selectBit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectAllBit");
        logger.info("���������" + jsonBody);
        String resp = "";

        String regnEncd;
        try {

            regnEncd = BaseJson.getReqBody(jsonBody).get("regnEncd").asText();
            resp = gdsBitDistionService.selectBit(regnEncd);
        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectAllBit", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ��λ�´��
    @RequestMapping(value = "selectInvty", method = RequestMethod.POST)
    @ResponseBody
    private String selectInvty(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectInvty");
        logger.info("���������" + jsonBody);
        String resp = "";

        String gdsBitEncd;
        try {

            gdsBitEncd = BaseJson.getReqBody(jsonBody).get("gdsBitEncd").asText();
            resp = gdsBitDistionService.selectInvty(gdsBitEncd);
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectInvty", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��ѯ�������
    @RequestMapping(value = "selectInvtyWhs", method = RequestMethod.POST)
    @ResponseBody
    private String selectInvtyWhs(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectInvtyWhs");
        logger.info("���������" + jsonBody);
        String resp = "";

        String gdsBitEncd;
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectInvtyWhs", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);
                int pageNo = (int) map.get("pageNo");
                int pageSize = (int) map.get("pageSize");
                map.put("index", (pageNo - 1) * pageSize);
                map.put("num", pageSize);
            resp = gdsBitDistionService.selectInvtyWhs(map);
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectInvtyWhs", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // pc�˽�����λ
    @RequestMapping(value = "updateMovbit", method = RequestMethod.POST)
    @ResponseBody
    private String updateMovbit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/updateMovbit");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            List<Map> mList = BaseJson.getList(jsonBody);
            List<MovBitList> mBitLists = new ArrayList<>();
            for (Map map : mList) {
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
                String prdcDt = map.get("prdcDt").toString();
                String whsEncd = map.get("whsEncd").toString();
                String regnEncd = map.get("regnEncd").toString();// ԭʼ
                String qty1 = map.get("qty1").toString();
                String gdsBitEncd1 = map.get("gdsBitEncd1").toString();
                String regnEncd2 = map.get("regnEncd2").toString();// Ŀ��
                String qty2 = map.get("qty2").toString();
                String gdsBitEncd2 = map.get("gdsBitEncd2").toString();

                MovBitList mBitList = new MovBitList();
                mBitList.setInvtyEncd(invtyEncd);
                mBitList.setWhsEncd(whsEncd);
                mBitList.setBatNum(batNum);
                mBitList.setPrdcDt(prdcDt);
                mBitList.setRegnEncd(regnEncd);// ԭʼ
                mBitList.setOalBit(gdsBitEncd1);
                BigDecimal oalBitNum = new BigDecimal(qty1);
                mBitList.setOalBitNum(oalBitNum);
                mBitList.setTargetRegnEncd(regnEncd2);// Ŀ��
                mBitList.setTargetBit(gdsBitEncd2);
                BigDecimal targetBitNum = new BigDecimal(qty2);
                mBitList.setTargetBitNum(targetBitNum);
                mBitLists.add(mBitList);

            }

            List<MovBitTab> mBitTabList = new ArrayList<>();
            for (Map map : mList) {
//				map.remove("batNum");
                map.remove("prdcDt");
                MovBitTab mBitTab = new MovBitTab();
                try {
                    BeanUtils.populate(mBitTab, map);
                } catch (IllegalAccessException e) {

                } catch (InvocationTargetException e) {

                }
                mBitTabList.add(mBitTab);
            }

            resp = gdsBitDistionService.updateMovbit(mBitLists, mBitTabList);
            misLogDAO.insertSelective(new MisLog("��λ��pc�˽�����λ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("��λ��pc�˽�����λ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMovbit", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��λ
    // PDA��ѯ��ʾ
    @RequestMapping(value = "selectMTabList", method = RequestMethod.POST)
    @ResponseBody
    private String selectMTab(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectMTabList");
        logger.info("���������" + jsonBody);
        String resp = "";

        ObjectNode reqBody;
        try {

            reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd=realWhsString(whsEncd);
            if (whsEncd != null) {
                resp = gdsBitDistionService.selectMTabList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectMTabList", false, "�û���ҵ��ֿ�", null);
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectMTabList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // PDA�ش�����
    @RequestMapping(value = "updateMBitList", method = RequestMethod.POST)
    @ResponseBody
    private String updateMBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/updateMBitList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
//			String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
//			String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();

            MovBitList movBitList = BaseJson.getPOJO(jsonBody, MovBitList.class);
//			movBitList.setOperator(userName);
//			movBitList.setOperatorId(userId);

            resp = gdsBitDistionService.updateMBitList(movBitList);
            misLogDAO.insertSelective(new MisLog("��λ��PDA�ش�����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("��λ��PDA�ش�����", "�ֿ�", null, jsonBody, request, e));
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMBitList", false, e.getMessage(), null);
            logger.info("���ز�����" + resp);
            return resp;
        }
        resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMBitList", true, resp, null);
        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��λƥ���Ƽ�
    @RequestMapping(value = "selectMBitList", method = RequestMethod.POST)
    @ResponseBody
    private String selectMBitList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectMBitList");
        logger.info("���������" + jsonBody);
        String resp = "";
//		BeanUtils.copyProperties(dest, orig);
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            resp = gdsBitDistionService.selectMBitList(map);

        } catch (Exception e) {

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectMBitList", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // pc�˽�����λ����
    @RequestMapping(value = "insertMovbitPC", method = RequestMethod.POST)
    @ResponseBody
    private String insertMovbitPC(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/insertMovbitPC");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            List<Map> mList = BaseJson.getList(jsonBody);
            List<MovBitList> mBitLists = new ArrayList<>();

            for (Map map : mList) {
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
//					String prdcDt = map.get("prdcDt").toString();
                String whsEncd = map.get("whsEncd").toString();
                String regnEncd = map.get("regnEncd").toString();// ԭʼ
                String qty = map.get("qty").toString();
                String gdsBitEncd1 = map.get("oalBit").toString();
                String TargetRegnEncd = map.get("TargetRegnEncd").toString();// Ŀ��
//					String qty2 = map.get("qty2").toString();
                String gdsBitEncd2 = map.get("targetBit").toString();
                String prdcDt = map.get("prdcDt").toString();
                if (prdcDt != null && prdcDt.equals("")) {
                    prdcDt = null;
                }

                MovBitList mBitList = new MovBitList();
                mBitList.setInvtyEncd(invtyEncd);
                mBitList.setWhsEncd(whsEncd);
                mBitList.setBatNum(batNum);

                mBitList.setRegnEncd(regnEncd);// ����
                mBitList.setTargetRegnEncd(TargetRegnEncd);// Ŀ��

                mBitList.setPrdcDt(prdcDt);
                mBitList.setOalBit(gdsBitEncd1);
                mBitList.setOalBitNum(new BigDecimal(qty));
                mBitList.setTargetBit(gdsBitEncd2);
                mBitLists.add(mBitList);

            }
            resp = gdsBitDistionService.insertMovbitPC(mBitLists);
            misLogDAO.insertSelective(new MisLog("��λ��pc�˽�����λ����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("��λ��pc�˽�����λ����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/insertMovbitPC", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ɾ��
    @RequestMapping(value = "deleteMovbit", method = RequestMethod.POST)
    @ResponseBody
    private String deleteMovbit(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/deleteMovbit");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {

            String ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();

            // ��λɾ��
            resp = gdsBitDistionService.deleteMovbit(ordrNum);
            misLogDAO.insertSelective(new MisLog("��λ��ɾ��", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("��λ��ɾ��", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/deleteMovbit", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // ��λ
    // ��ѯ��ʾ
    @RequestMapping(value = "selectMTabLists", method = RequestMethod.POST)
    @ResponseBody
    private String selectMTabLists(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectMTabList");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            String reqBody = BaseJson.getReqBody(jsonBody).toString();
            Map map = JacksonUtil.getMap(reqBody);
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectMTabLists", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);

                resp = gdsBitDistionService.selectMTabLists(map);
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectMTabLists", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

    // �ش�����
    @RequestMapping(value = "updateMBitListPC", method = RequestMethod.POST)
    @ResponseBody
    private String updateMBitListPC(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/updateMBitListPC");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String userName = BaseJson.getReqHead(jsonBody).get("userName").asText();
            String ordrNum = BaseJson.getReqBody(jsonBody).get("ordrNum").asText();
            List<String> list = getList(ordrNum);
            for (String string : list) {
                MovBitList movBitList = new MovBitList();
                movBitList.setOperator(userName);
                movBitList.setOperatorId(userId);
                movBitList.setOrdrNum(Long.valueOf(string));
                try {
                    resp += gdsBitDistionService.updateMBitList(movBitList);

                } catch (Exception e) {

                    resp += e.getMessage();

                }

            }
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMBitListPC", false, resp, null);

            misLogDAO.insertSelective(new MisLog("��λ��PC�ش�����", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("��λ��PC�ش�����", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/gds_bit_distion/updateMBitListPC", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }

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

    // ��ѯ�������
    @RequestMapping(value = "selectWhsgds", method = RequestMethod.POST)
    @ResponseBody
    private String selectWhsgds(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/gds_bit_distion/selectWhsgds");
        logger.info("���������" + jsonBody);
        String resp = "";

        String gdsBitEncd;
        try {

            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
            map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));
            map.put("whsEncd", getList((String) map.get("whsEncd")));
            map.put("regnEncd", getList((String) map.get("regnEncd")));
            map.put("realWhs", getList((String) map.get("realWhs")));
            String whsId = BaseJson.getReqHead(jsonBody).get("whsId").asText();
            if(StringUtils.isBlank(whsId)){
                resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectMTabLists", false, "�û�û�вֿ�Ȩ��" , null);
            }else{
                map.put("whsId", whsId);

            resp = gdsBitDistionService.selectWhsgds(map);
            }
        } catch (Exception e) {
            resp = BaseJson.returnRespObj("whs/gds_bit_distion/selectWhsgds", false, e.getMessage(), null);

        }

        logger.info("���ز�����" + resp);
        return resp;
    }
}
