package com.px.mis.whs.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.fasterxml.jackson.databind.JsonNode;
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


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.service.IntoWhsService;

//�Уģ��Ĳɹ��롡�����۳�
@Controller
@RequestMapping("/whs/pda_into_whs")
public class IntoWhsController {

    private Logger logger = LoggerFactory.getLogger(IntoWhsController.class);

    @Autowired
    IntoWhsService intoWhsService;
    // ��־
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    @Autowired
    InvtyNumMapper invtyNumMapper;
    @Autowired
    WhsDocMapper whsDocMapper;
    //	����
    @Autowired
    FormBookService formBookService;

    // ------------------�ɹ���--------------------
    // ��ѯ���е����� �ɹ���Ĳ�ѯ
    @RequestMapping(value = "selectToGdsSnglList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectToGdsSnglList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectToGdsSnglList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectToGdsSnglList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectToGdsSnglList", false, "�û���ҵ��ֿ�", null);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectToGdsSnglList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �����������յ���Ϣ ���սӿ�(�ش�)
    @RequestMapping(value = "addToGdsSngl", method = RequestMethod.POST)
    @ResponseBody
    private String addPursOrdr(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/addToGdsSngl");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode aString = BaseJson.getReqHead(jsonBody);
            String userId = aString.get("accNum").asText();

            ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);
            List<Map> mList = BaseJson.getList(jsonBody);
            List<ToGdsSnglSub> toGdsSnglSubList = new ArrayList<>();
            for (Map map : mList) {
                ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
                toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
                BeanUtils.populate(toGdsSnglSub, map);
                toGdsSnglSubList.add(toGdsSnglSub);
            }

            // ��ӵ�����
            resp = intoWhsService.insertToGdsSngl(userId, toGdsSngl, toGdsSnglSubList);
            misLogDAO.insertSelective(new MisLog("PDA�����������յ���Ϣ", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("PDA�����������յ���Ϣ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/addToGdsSngl", true, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �����ɹ���ⵥ��Ϣ �ɹ���ӿ�(�ش�)
    @RequestMapping(value = "addIntoWhs", method = RequestMethod.POST)
    @ResponseBody
    private String addIntoWhs(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pda_into_whs/addIntoWhs");
        logger.info("���������" + jsonBody);
        String resp = "";

        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/addIntoWhs", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            ObjectNode aString = BaseJson.getReqHead(jsonBody);

            String userId = aString.get("accNum").asText();
            String userName = aString.get("userName").asText();

            List<ToGdsSnglSub> tLists = new ArrayList<>();// �������յ��ӱ�
            Map<String, List<MovBitTab>> movMap = new HashMap<String, List<MovBitTab>>();
            List<MovBitTab> list = new ArrayList<MovBitTab>();

//            JSONArray array = JSON.parseArray(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("list"));
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");

//                ToGdsSnglSub gdsSnglSub = JSON.parseObject(str.toString(), ToGdsSnglSub.class);

                ToGdsSnglSub gdsSnglSub = JacksonUtil.getPOJO(str, ToGdsSnglSub.class);

                gdsSnglSub.setCrspdBarCd(str.get("ordrNum").asText());// ��Ӧ������ ����Ϊ���
                gdsSnglSub.setQty(BigDecimal.ZERO);// ʵ�ʵ�������


                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());

                if (oSubTab.getGdsBitEncd() != null && !oSubTab.getGdsBitEncd().equals("")) {
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
                        gdsSnglSub.setQty(gdsSnglSub.getQty().add(oSubTabs.getQty()));// ʵ�ʵ�������
                        oSubTabs.setGdsBitEncd(strs[j]);
                        list.add(oSubTabs);
                    }

                }
                tLists.add(gdsSnglSub);

            }
            ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);

//			for (MovBitTab movBitTab : list) {
//				String key = movBitTab.getWhsEncd();
//				List<MovBitTab> bitTabs;
//				if (movMap.containsKey(key)) {
//					bitTabs = movMap.get(key);
//				} else {
//					bitTabs = new ArrayList<MovBitTab>();
//				}
//				bitTabs.add(movBitTab);
//				movMap.put(key, bitTabs);
//			}
//
//			List<List<MovBitTab>> lists = new ArrayList<List<MovBitTab>>(movMap.values());

            resp = intoWhsService.addIntoWhs(userId, userName, null, null, null, list, toGdsSngl, tLists);
            if (true) {
                logger.info("���ز�����" + resp);

                return resp;
            }

            // ��ⵥ�͵������յ�����
            IntoWhs intoWhs = BaseJson.getPOJO(jsonBody, IntoWhs.class);
//			ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);
            List<Map> mList = BaseJson.getList(jsonBody);

            List<IntoWhsSub> intoWhsSubList = new ArrayList<>();// ��ⵥ�ӱ�
            List<InvtyTab> iList = new ArrayList<>();// ����
            List<MovBitTab> mBList = new ArrayList<>();// ��λ��
            List<ToGdsSnglSub> tList = new ArrayList<>();// �������յ��ӱ�

            for (Map map : mList) {
                // System.out.println(map.toString());

                String qty = map.get("qty").toString();
                String whsEncd = map.get("whsEncd").toString();
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
                String gdsBitEncd = map.get("gdsBitEncd").toString();
                String prdcDt = map.get("prdcDt").toString();

                // ��λ�� (����ʱ���������������������ڻ�λ�����ӣ����û����������)
                String[] gBitEncd = gdsBitEncd.split(",");
                String[] Qty = qty.split(",");// 10,20,30
                MovBitTab movBitTab;
                for (int m = 0; m < gBitEncd.length; m++) {
                    // map.put("qty", Qty[m]);
                    movBitTab = new MovBitTab();
                    movBitTab.setInvtyEncd(invtyEncd);// �������
                    movBitTab.setWhsEncd(whsEncd);// �ֿ����
                    movBitTab.setBatNum(batNum);// ���κ�
                    movBitTab.setGdsBitEncd(gBitEncd[m]);// ��λ����
                    movBitTab.setPrdcDt(prdcDt);
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                    movBitTab.setIntoDt(dateStr);
                    String qString = Qty[m].toString();

                    // System.out.println("Qty[m].toString()" + Qty[m].toString());
                    // System.out.println("for" + map.get("qty"));

                    movBitTab.setQty(new BigDecimal(qString));// ����
                    mBList.add(movBitTab);
                }

            }

            IntoWhsSub intoWhsSub;
            for (Map map : mList) {
                // System.out.println(map);

                map.remove("gdsBitEncd");

                String qty = map.get("qty").toString();
                String[] Qty = qty.split(",");// 10,20,30
                double dQty = 0;
                for (int m = 0; m < Qty.length; m++) {
                    dQty += new BigDecimal(Qty[m]).doubleValue();
                    // System.out.println(dQty);
                }
                map.put("qty", dQty);
                // ��ⵥ�ӱ�
                intoWhsSub = new IntoWhsSub();
//				intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
//				intoWhsSub.setQty(new BigDecimal(dQty));
                BeanUtils.populate(intoWhsSub, map);
                intoWhsSubList.add(intoWhsSub);

                // �������յ�
                ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
//				toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
//				toGdsSnglSub.setQty(new BigDecimal(dQty));
                BeanUtils.populate(toGdsSnglSub, map);
                tList.add(toGdsSnglSub);

            }

            // ������ⵥ
//			intoWhsService.addIntoWhs(userId, userName, intoWhs, intoWhsSubList, iList, mBList, toGdsSngl, tList);
            misLogDAO.insertSelective(new MisLog("PDA�����ɹ���ⵥ��Ϣ", "�ֿ�", null, jsonBody, request));

            try {
                resp = BaseJson.returnRespObj("/whs/pda_into_whs/addIntoWhs", true, "�����ɹ���", null);
            } catch (IOException e) {

            }

            logger.info("���ز�����" + resp);

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("PDA�����ɹ���ⵥ��Ϣ", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/addIntoWhs", false, e.getMessage(), null);

        }

        return resp;

    }

    // -----------���۳�-------------------------------
    // ��ѯ���г��ⵥ
    @RequestMapping(value = "selectSellOutWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectSellOutWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectSellOutWhsList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectSellOutWhsList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectSellOutWhsList", false, "�û���ҵ��ֿ�", null);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectSellOutWhsList", false, e.getMessage(), null);
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

    // ���۳��⣨�ش���
    @RequestMapping(value = "updatesOutWhs", method = RequestMethod.POST)
    @ResponseBody
    public String selectCheckSnglStauts(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/updatesOutWhs");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");

                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);

                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
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
            // ����

            SellOutWhs sOutWhs = new SellOutWhs();
            sOutWhs.setChkr(BaseJson.getReqHead(jsonBody).get("userName").asText());
            sOutWhs.setOutWhsId(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
            sOutWhs.setIsNtChk(1);

            resp = intoWhsService.updatesOutWhs(sOutWhs, null, null, list);

            if (true) {
                logger.info("���ز�����" + resp);
                return resp;
            }

            // �ӱ�
            List<Map> oTabMap = BaseJson.getList(jsonBody);
            List<SellOutWhsSub> sList = new ArrayList<>();// ������ӱ�
            List<InvtyTab> iList = new ArrayList<>();// ����
            List<MovBitTab> mList = new ArrayList<>();// ��λ��

            for (Map map : oTabMap) {
                String qty = map.get("qty").toString();
                String whsEncd = map.get("whsEncd").toString();
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
                String gdsBitEncd = map.get("gdsBitEncd").toString();

                String[] Qty = qty.split(",");// 10,20,30
                double dQty = 0;
                for (int m = 0; m < Qty.length; m++) {
                    dQty += new BigDecimal(Qty[m]).doubleValue();
                }
                // ������� ȡ�����������
                SellOutWhsSub sub = new SellOutWhsSub();// ������ӱ�
                sub.setQty(new BigDecimal(dQty));// ����
                // System.out.println(dQty);
                sList.add(sub);

                // ���� ����
                InvtyTab invtyTab = new InvtyTab();
                invtyTab.setWhsEncd(whsEncd);// �ֿ����(�ӱ�������)
                invtyTab.setInvtyEncd(invtyEncd);// �������
                invtyTab.setBatNum(batNum);// ����
                invtyTab.setNowStok(new BigDecimal(dQty));
                iList.add(invtyTab);

                // ��λ��
                String[] gBitEncd = gdsBitEncd.split(",");
                MovBitTab movBitTab = null;
                for (int m = 0; m < gBitEncd.length; m++) {
                    map.put("qty", Qty[m]);
                    movBitTab = new MovBitTab();
                    movBitTab.setInvtyEncd(invtyEncd);
                    ;// �������
                    movBitTab.setWhsEncd(whsEncd);
                    ;// �ֿ����
                    movBitTab.setBatNum(batNum);// ���κ�
                    movBitTab.setGdsBitEncd(gBitEncd[m]);// ��λ����
                    String qString = map.get("qty").toString();
                    movBitTab.setQty(new BigDecimal(qString));// ����
                    mList.add(movBitTab);
                }

            }
            resp = intoWhsService.updatesOutWhs(sOutWhs, sList, iList, mList);
            misLogDAO.insertSelective(new MisLog("PDA���۳��⣨�ش���", "�ֿ�", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("PDA���۳��⣨�ش���", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);

        return resp;
    }

    // ����ԭ��
    @RequestMapping(value = "selectReason", method = RequestMethod.POST)
    @ResponseBody
    public String selectReason(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectReason");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            resp = intoWhsService.selectReason();
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectReason", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // -----------�������۳�-------------------------------
    // ��ѯ���к��ֳ��ⵥ
    @RequestMapping(value = "selectRedSellOutWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRedSellOutWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectRedSellOutWhsList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectRedSellOutWhsList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectRedSellOutWhsList", false, "�û���ҵ��ֿ�", null);
            }

        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectRedSellOutWhsList", false, e.getMessage(), null);
        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �������۳��⣨�ش���
    @RequestMapping(value = "updatesRedOutWhsPda", method = RequestMethod.POST)
    @ResponseBody
    public String updatesRedOutWhsPda(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/updatesRedOutWhsPda");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
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

                    oSubTabs.setQty(BigDecimal.ZERO.subtract(new BigDecimal(qtys[j])));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            // ����

            SellOutWhs sOutWhs = new SellOutWhs();
            sOutWhs.setChkr(BaseJson.getReqHead(jsonBody).get("userName").asText());
            sOutWhs.setOutWhsId(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
            sOutWhs.setIsNtChk(1);

            resp = intoWhsService.updatesRedOutWhs(sOutWhs, list);

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("PDA�������۳��⣨�ش���", "�ֿ�", null, jsonBody, request, e));
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);

        return resp;
    }

    // �ɹ��˻��Ĳ�ѯ
    @RequestMapping(value = "selectIntoWhsList", method = RequestMethod.POST)
    @ResponseBody
    public String selectIntoWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectIntoWhsList");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectIntoWhsList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectIntoWhsList", false, "�û���ҵ��ֿ�", null);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectIntoWhsList", false, e.getMessage(), null);

        }
        logger.info("���ز�����" + resp);
        return resp;
    }

    // �ɹ��˻����ش���
    @RequestMapping(value = "updatesRedIntoWhsPda", method = RequestMethod.POST)
    @ResponseBody
    public String updatesRedIntoWhsPda(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/updatesRedIntoWhsPda");
        logger.info("���������" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedIntoWhsPda", false, "�����ѷ��ˣ�", null);
                logger.info("���ز�����" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
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

                    oSubTabs.setQty(BigDecimal.ZERO.subtract(new BigDecimal(qtys[j])));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            // ����

            IntoWhs intoWhs = new IntoWhs();
            intoWhs.setChkr(BaseJson.getReqBody(jsonBody).get("userName").asText());
            intoWhs.setIntoWhsSnglId(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
            intoWhs.setIsNtChk(1);

            resp = intoWhsService.updatesRedIntoWhs(intoWhs, list);

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("�ɹ��˻����ش���", "�ֿ�", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedIntoWhsPda", false, e.getMessage(), null);

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
}
