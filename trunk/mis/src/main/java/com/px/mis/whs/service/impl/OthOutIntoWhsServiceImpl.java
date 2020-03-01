package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.AmbDisambSnglMapper;
import com.px.mis.whs.dao.CannibSnglMapper;
import com.px.mis.whs.dao.CheckPrftLossMapper;
import com.px.mis.whs.dao.CheckSnglMapper;
import com.px.mis.whs.dao.IntoWhsMapper;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.CannibSngl;
import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsMap;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;
import com.px.mis.whs.service.OthOutIntoWhsService;

@Service
@Transactional
public class OthOutIntoWhsServiceImpl extends poiTool implements OthOutIntoWhsService {

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;
    // ������
    @Autowired
    GetOrderNo getOrderNo;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    @Autowired
    IntoWhsMapper intoWhsMapper;
    // ��λ
    @Autowired
    InvtyGdsBitListMapper bitListMapper;
    /**
     * �ֿ�
     */
    @Autowired
    WhsDocMapper whsDocMapper;
    // ����
    @Autowired
    CannibSnglMapper cannibSnglMapper;
    // ��װ
    @Autowired
    AmbDisambSnglMapper ambDisambSnglMapper;
    // �����
    @Autowired
    CheckPrftLossMapper checkPrftLossMapper;
    // �̵�
    @Autowired
    CheckSnglMapper checkSnglMapper;
    @Autowired
    InvtyDocDao invtyDocDao;
    // �������������
    @Override
    public String insertOthOutIntoWhs(String userId, OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList, String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        if (outIntoWhs.getOutIntoWhsTypId().equals("11")) {// ��������
            outIntoWhs.setOutIntoWhsTypId("11");
            outIntoWhs.setOutStatus("������");
            outIntoWhs.setFormTypEncd("015");

        } else if (outIntoWhs.getOutIntoWhsTypId().equals("12")) {// �������
            outIntoWhs.setOutIntoWhsTypId("12");
            outIntoWhs.setInStatus("������");
            outIntoWhs.setFormTypEncd("014");

        }

        String number = getOrderNo.getSeqNo("OTCR", userId, loginTime);// ��ȡ������
        if (othOutIntoWhsMapper.selectOthOutIntoWhs(number) != null) {
            message = "���" + number + "�Ѵ��ڣ����������룡";
            isSuccess = false;
        } else {
            if (othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(number).size() > 0) {
                othOutIntoWhsMapper.deleteOthOutIntoWhsSubTab(number);
            }

            outIntoWhs.setFormNum(number);// ��������
            othOutIntoWhsMapper.insertOthOutIntoWhs(outIntoWhs);
            for (OthOutIntoWhsSubTab oIntoWhsSubTab : oList) {
                oIntoWhsSubTab.setFormNum(outIntoWhs.getFormNum());// �������ű�ʶ
                InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(oIntoWhsSubTab.getInvtyEncd()))
                        .orElseThrow(() -> new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"�ô��������"));
                Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
                if(sisQuaGuaPer == 1){
                    if(StringUtils.isBlank(oIntoWhsSubTab.getBaoZhiQi())|| StringUtils.isBlank(oIntoWhsSubTab.getPrdcDt())
                            || StringUtils.isBlank(oIntoWhsSubTab.getInvldtnDt()) ){
                        throw new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"�����ڹ������������ô���");
                    }
                }else{
                    oIntoWhsSubTab.setBaoZhiQi(null);
                    oIntoWhsSubTab.setPrdcDt(null);
                    oIntoWhsSubTab.setInvldtnDt(null);
                }
            }
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(oList);
            message = "�����ɹ�";

        }
        /**
         * // ��������Ᵽ�� ������ٿ����� ������ӿ����� // �жϿ������Ƿ���� Boolean enough = true; for (InvtyTab
         * iTab : iList) { InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(iTab);
         *
         * for (OthOutIntoWhsSubTab oSubTab : oList) {
         *
         * if (inTab.getAvalQty().compareTo(oSubTab.getQty()) == 1 ||
         * inTab.getAvalQty().compareTo(oSubTab.getQty()) == 0) {
         *
         * // ������������� 11:�������� 12:������� if
         * (Integer.parseInt(outIntoWhs.getOutIntoWhsTypId()) == 11) {
         * invtyNumMapper.updateAInvtyTab(iList);// ���������� ���� } else if
         * (Integer.parseInt(outIntoWhs.getOutIntoWhsTypId()) == 12) { // �жϿ������Ƿ���� for
         * (InvtyTab iTab1 : iList) { InvtyTab inTab1 =
         * invtyNumMapper.selectInvtyTabByTerm(iTab1); if (inTab1 != null) {
         * invtyNumMapper.updateAInvtyTabJia(iList);// ���������� ��� } else { // ��������
         * // System.out.println("�������� ����������½� �н��");
         * invtyNumMapper.insertInvtyTabList(iList); }
         *
         * } }
         *
         * message = "�����ɹ���"; isSuccess = true;
         *
         * }
         *
         * if (inTab.getAvalQty().compareTo(oSubTab.getQty()) == -1) { isSuccess = true;
         * message = "�������������"; enough = false; }
         *
         * } }
         *
         * if (enough) { outIntoWhs.setFormNum(number);// �������� BigDecimal unTaxUprc; //
         * ��˰���� BigDecimal qty; // ���� BigDecimal taxRate; // ˰�� String prdcDt; // ��������
         * String baoZhiQi; // ������ for (OthOutIntoWhsSubTab oIntoWhsSubTab : oList) {
         * oIntoWhsSubTab.setFormNum(outIntoWhs.getFormNum());// �������ű�ʶ // ��ȡδ˰����
         * unTaxUprc = oIntoWhsSubTab.getUnTaxUprc(); // ��ȡδ˰���� qty =
         * oIntoWhsSubTab.getQty(); // ��ȡ˰�� ҳ��˰��δ������������Ҫ��˰��/100
         * oIntoWhsSubTab.setTaxRate(oIntoWhsSubTab.getTaxRate().divide(new
         * BigDecimal(100))); taxRate = oIntoWhsSubTab.getTaxRate(); // ��ȡ�������� prdcDt =
         * oIntoWhsSubTab.getPrdcDt(); // ��ȡ������ baoZhiQi = oIntoWhsSubTab.getBaoZhiQi();
         * // ����ʧЧ���� oIntoWhsSubTab.setInvldtnDt(CalcAmt.getDate(prdcDt,
         * Integer.parseInt(baoZhiQi))); // ����δ˰��� ���=δ˰����*δ˰����
         * oIntoWhsSubTab.setUnTaxAmt(CalcAmt.noTaxAmt(unTaxUprc, qty)); // ���㺬˰����
         * ��˰����=��˰����*˰��+��˰����
         * oIntoWhsSubTab.setCntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc, qty, taxRate));
         * // ���㺬˰��� ��˰���=��˰���*˰��+��˰���=˰��+��˰���
         * oIntoWhsSubTab.setCntnTaxAmt(CalcAmt.prcTaxSum(unTaxUprc, qty, taxRate)); }
         *
         * othOutIntoWhsMapper.insertOthOutIntoWhs(outIntoWhs);
         * othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(oList); }
         *
         * }
         */
        try {

            resp = BaseJson.returnRespObj("whs/out_into_whs/insertOthOutIntoWhs", isSuccess, message, outIntoWhs);
        } catch (Exception e) {

        }
        return resp;
    }

    // �޸����������
    @Override
    public String updateOthOutIntoWhs(OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> list = new ArrayList<>();
        list.add(outIntoWhs.getFormNum());

        OthOutIntoWhs oIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(outIntoWhs.getFormNum());
        if (oIntoWhs.getIsNtChk() == null) {
            throw new RuntimeException("�������״̬�쳣");
        } else if (oIntoWhs.getIsNtChk() == 1) {
            throw new RuntimeException("��������˲������޸�");
        }
        othOutIntoWhsMapper.deleteOthOutIntoWhsSubTab(outIntoWhs.getFormNum());
        // ɾ���ӱ���� ������

        if (outIntoWhs.getOutIntoWhsTypId().equals("11")) {// ��������
            outIntoWhs.setOutIntoWhsTypId("11");
            outIntoWhs.setOutStatus("������");
            outIntoWhs.setInStatus(null);

        } else if (outIntoWhs.getOutIntoWhsTypId().equals("12")) {// �������
            outIntoWhs.setOutIntoWhsTypId("12");
            outIntoWhs.setInStatus("������");
            outIntoWhs.setOutStatus(null);

        }

        othOutIntoWhsMapper.updateOthOutIntoWhs(outIntoWhs);

        for (OthOutIntoWhsSubTab oIntoWhsSubTab : oList) {
            oIntoWhsSubTab.setFormNum(outIntoWhs.getFormNum());// �������ű�ʶ
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(oIntoWhsSubTab.getInvtyEncd()))
                    .orElseThrow(() -> new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"�ô��������"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(oIntoWhsSubTab.getBaoZhiQi())|| StringUtils.isBlank(oIntoWhsSubTab.getPrdcDt())
                        || StringUtils.isBlank(oIntoWhsSubTab.getInvldtnDt()) ){
                    throw new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"�����ڹ������������ô���");
                }
            }else{
                oIntoWhsSubTab.setBaoZhiQi(null);
                oIntoWhsSubTab.setPrdcDt(null);
                oIntoWhsSubTab.setInvldtnDt(null);
            }
        }
        othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(oList);
        bitListMapper.deleteInvtyGdsBitList(Arrays.asList(outIntoWhs.getFormNum()));

        message = "���³ɹ���";

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOthOutIntoWhs", isSuccess, message, outIntoWhs);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����ɾ��
    @Override
    public String deleteAllOthOutIntoWhs(String formNum) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> list = getList(formNum);
        // System.out.println(list);

        List<OthOutIntoWhs> whsList = othOutIntoWhsMapper.selectOthOutIntoWhsList(list);
        List<String> lists = new ArrayList<String>();
        //        1		�������
//        2		��������
//        3		��װ���
//        4		��װ����
//        5		��ж���
//        6		��ж����
//        7		�̿�����
//        8		��ӯ���
//        9		�ɹ����
//        10	���۳���
//        11	��������
//        12	�������
        final List<String> listLambda = Arrays.asList("1", "2", "3", "4", "5", "6");

        lists = whsList.stream().filter(whs -> !listLambda.contains(whs.getOutIntoWhsTypId())).filter(whs -> whs.getIsNtChk() == 0).map(OthOutIntoWhs::getFormNum)
                .collect(Collectors.toList());
        Set<String> stringSet = whsList.stream().filter(whs -> listLambda.contains(whs.getOutIntoWhsTypId())).map(OthOutIntoWhs::getFormNum)
                .collect(Collectors.toSet());
        stringSet.addAll(whsList.stream().filter(whs -> whs.getIsNtChk() == 1).map(OthOutIntoWhs::getFormNum)
                .collect(Collectors.toSet()));

        if (lists.size() > 0) {
            othOutIntoWhsMapper.insertOthOutIntoWhsDl(lists);
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTabDl(lists);

            othOutIntoWhsMapper.deleteAllOthOutIntoWhs(lists);
            bitListMapper.deleteInvtyGdsBitList(lists);
            isSuccess = true;
            message = "ɾ���ɹ���" + lists.toString();
        } else {
            isSuccess = false;
            message = "���" + stringSet + "���������ֱ��ɾ��";
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/deleteAllOthOutIntoWhs", isSuccess, message, null);
        } catch (IOException e) {

        }
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
            // System.out.println(str[i]);
        }
        return list;
    }

    // ��ѯ����ҳ��
    @Override
    public String query(String formNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();
//        List<Map> maps = new ArrayList<Map>();
        OthOutIntoWhs oIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(formNum);
        if (oIntoWhs != null) {
            oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(formNum);
//            for (OthOutIntoWhsSubTab tab : oSubTabList) {
//                Map<String, Object> map = new HashMap<String, Object>();
//
//                BeanMap beanMap = BeanMap.create(tab);
//                for (Object key : beanMap.keySet()) {
//                    map.put(key + "", beanMap.get(key));
//                }
//                if (tab.getUnTaxUprc() == null) {
//                    tab.setUnTaxUprc(BigDecimal.ZERO);
//                }
//                if (tab.getTaxRate() == null) {
//                    tab.setTaxRate(BigDecimal.ZERO);
//                }
//                if (tab.getQty() == null) {
//                    tab.setQty(BigDecimal.ZERO);
//                }
//                if (tab.getBxRule() != null && tab.getBxRule().compareTo(BigDecimal.ZERO) != 0) {
//                    map.put("bxQty", tab.getQty().divide(tab.getBxRule(), 8, BigDecimal.ROUND_HALF_UP));
//                }
//                map.put("taxAmt", tab.getUnTaxUprc().multiply(tab.getTaxRate().divide(new BigDecimal(100)))
//                        .multiply(tab.getQty()));
//                maps.add(map);
//            }
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + formNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/out_into_whs/query", isSuccess, message, oIntoWhs, oSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String queryList(Map map) {
        String resp = "";

//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("formNum", getList((String) map.get("formNum")));
        map.put("outIntoWhsTypId", getList((String) map.get("outIntoWhsTypId")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("srcFormNum", getList((String) map.get("srcFormNum")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("recvSendCateId", getList((String) map.get("recvSendCateId")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<OthOutIntoWhsMap> aList = othOutIntoWhsMapper.selectList(map);
        int count = othOutIntoWhsMapper.selectCount(map);
        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/out_into_whs/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // PDA
    // ��ѯ ���
    @Override
    public String selectOINChkr(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<OthOutIntoWhs> outIntoWhs = othOutIntoWhsMapper.selectOINChkr(list);
        // System.out.println("outIntoWhs\t" + outIntoWhs.size());
        // **************�Ƽ�����λ*****************

        for (OthOutIntoWhs oIntoWhs : outIntoWhs) {
            List<OthOutIntoWhsSubTab> oSubTabsList = oIntoWhs.getOutIntoSubList();
            for (OthOutIntoWhsSubTab oSubTab : oSubTabsList) {

                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(oIntoWhs.getWhsEncd());
                movBitTab.setInvtyEncd(oSubTab.getInvtyEncd());
                movBitTab.setBatNum(oSubTab.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s = s.substring(s.length() - 2);
                }
                oSubTab.setGdsBitEncd(s);
            }
        }
        // *****************************************

        try {
            if (outIntoWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOINChkr", true, "��ѯ�ɹ���", null, outIntoWhs);
            } else {
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOINChkr", false, "��ѯʧ�ܣ�", null, outIntoWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // ��ѯ ����
    @Override
    public String selectOutChkr(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<OthOutIntoWhs> outIntoWhs = othOutIntoWhsMapper.selectOutChkr(list);

        // **************�Ƽ������λ*****************

        for (OthOutIntoWhs oIntoWhs : outIntoWhs) {
            // System.out.println("OthOutIntoWhsSubTab");
            List<OthOutIntoWhsSubTab> oSubTabsList = oIntoWhs.getOutIntoSubList();
            // System.out.println("OthOutIntoWhsSubTab1");
            for (OthOutIntoWhsSubTab oSubTab : oSubTabsList) {

                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(oIntoWhs.getWhsEncd());
                movBitTab.setInvtyEncd(oSubTab.getInvtyEncd());
                movBitTab.setBatNum(oSubTab.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s = s.substring(s.length() - 2);
                }
                oSubTab.setGdsBitEncd(s);
            }
        }
        // *****************************************

        try {
            if (outIntoWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOutChkr", true, "��ѯ�ɹ���", null, outIntoWhs);
            } else {
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOutChkr", false, "��ѯʧ�ܣ�", null, outIntoWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // ���
    @Override
    public String uInvMov(List<InvtyTab> invtyTab, List<OthOutIntoWhsSubTab> othOutIntoWhsSubTab,
                          List<MovBitTab> movBitTab, OthOutIntoWhs othOutIntoWhs) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(othOutIntoWhs.getFormNum());
        if (bitTabs.size() > 0) {
            List<String> lists = new ArrayList<String>();
            lists.add(othOutIntoWhs.getFormNum());
            bitListMapper.deleteInvtyGdsBitList(lists);
        }
        bitListMapper.insertInvtyGdsBitLists(movBitTab);

        if (othOutIntoWhsSubTab.size() > 0) {
            othOutIntoWhsMapper.updateBat(othOutIntoWhsSubTab);// �޸Ĺ�������
        }

        message = "�ɹ�";

//        JSONObject object = new JSONObject();
//        object.put("formNum", othOutIntoWhs.getFormNum());
//        object.put("chkr", othOutIntoWhs.getChkr());
        ObjectNode object;
        try {
            object = JacksonUtil.getObjectNode("");

            object.put("formNum", othOutIntoWhs.getFormNum());
            object.put("chkr", othOutIntoWhs.getChkr());
            updateOutInWhsChk(othOutIntoWhs.getChkr(), object.toString(), LocalDateTime.now().toString());
        } catch (IOException e) {

        }

        if (true) {
            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/uInvMov", isSuccess, message, null);
                return resp;

            } catch (IOException e) {

            }
        }

        // �޸�����״̬
        // System.out.println("�޸�״̬");
        int count = othOutIntoWhsMapper.updateOthOutIntoWhs(othOutIntoWhs);// �޸�״̬
        // System.out.println("�޸Ĺ�������");
        for (OthOutIntoWhsSubTab intoWhsSubTab : othOutIntoWhsSubTab) {
            List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
            oSubTabList.add(intoWhsSubTab);
            // System.out.println(intoWhsSubTab.toString());
            othOutIntoWhsMapper.updateBat(oSubTabList);// �޸Ĺ�������
        }

        if (count >= 1) {

            for (InvtyTab iTab : invtyTab) {

                List<InvtyTab> invtyTabs = new ArrayList<>();
                invtyTabs.add(iTab);
                // �޸Ŀ����������
                // System.out.println("for(InvtyTab iTab:invtyTab) {");
                if (invtyNumMapper.selectInvtyTabByTerm(iTab) != null) {
                    // �޸Ŀ������
                    // System.out.println("�޸Ŀ������ ");
                    invtyNumMapper.updateInvtyTabJia(invtyTabs);// �޸Ŀ�������(����)
                    // �޸Ŀ����ûд SQL
                } else {
                    // System.out.println("��������  ���");
                    // �������� ���
                    invtyNumMapper.insertInvtyTabList(invtyTabs);
                }
            }

            for (MovBitTab mTab : movBitTab) {
                // System.out.println("for(MovBitTab mTab : movBitTab) {");
                List<MovBitTab> lmbt = new ArrayList<>();
                // System.out.println("new.size()" + lmbt.size());
                lmbt.add(mTab);
                // System.out.println("add.size()" + lmbt.size());
                // (����ʱ���������������������ڻ�λ�����ӣ����û����������)
                if (invtyNumMapper.selectAllMbit(lmbt).size() > 0) {

                    invtyNumMapper.updateMovbitTabJia(lmbt);// �޸���λ��(����)

                } else {
                    List<GdsBit> gList = invtyNumMapper.selectRegn(mTab.getGdsBitEncd());
                    if (gList.size() == 0) {
                        throw new RuntimeException("û������");
                    }
                    // System.out.println(gList.get(0).getRegnEncd());
                    mTab.setRegnEncd(gList.get(0).getRegnEncd());// ����
                    // System.out.println("else.size()" + lmbt.size());
                    lmbt.clear();
                    // System.out.println("clear.size()" + lmbt.size());
                    lmbt.add(mTab);
                    // System.out.println("add.size()" + lmbt.size());
                    invtyNumMapper.insertMovBitTab(lmbt);// ������λ��
                }

            }

            /**
             *
             * //(����ʱ���������������������ڻ�λ�����ӣ����û����������)
             * if(invtyNumMapper.selectAllMbit(movBitTab)!=null) {
             *
             * invtyNumMapper.updateMovbitTabJia(movBitTab);//�޸���λ��(����) }else { //������λ�� Ѱ������
             * for(MovBitTab mTab:movBitTab) { List<GdsBit>
             * gList=invtyNumMapper.selectRegn(mTab.getGdsBitEncd()); for(GdsBit
             * gdsBit:gList) { mTab.setRegnEncd(gdsBit.getRegnEncd());//����
             * movBitTab.add(mTab); } } invtyNumMapper.insertMovBitTab(movBitTab);//������λ�� }
             *
             */
            isSuccess = true;
            message = "���ɹ���";
        } else {
            isSuccess = false;
            message = "���ʧ�ܣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uInvMov", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����
    @Override
    public String uOutvMov(List<InvtyTab> invtyTab, List<OthOutIntoWhsSubTab> othOutIntoWhsSubTab,
                           List<MovBitTab> movBitTab, OthOutIntoWhs othOutIntoWhs) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(othOutIntoWhs.getFormNum());
        if (bitTabs.size() > 0) {
            List<String> lists = new ArrayList<String>();
            lists.add(othOutIntoWhs.getFormNum());
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

        bitListMapper.insertInvtyGdsBitLists(movBitTab);
        message = "�ɹ�";
        ObjectNode object;
        try {
            object = JacksonUtil.getObjectNode("");


//		JSONObject object = new JSONObject();
            object.put("formNum", othOutIntoWhs.getFormNum());
            object.put("chkr", othOutIntoWhs.getChkr());
            updateOutInWhsChk(othOutIntoWhs.getChkr(), object.toString(), LocalDateTime.now().toString());
        } catch (IOException e) {

        }
        if (true) {
            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", isSuccess, message, null);
                return resp;

            } catch (IOException e) {

            }
        }

        // �޸�����״̬
        int count = othOutIntoWhsMapper.updateOthOutIntoWhs(othOutIntoWhs);// �޸�״̬
        if (count >= 1) {

            for (InvtyTab iTab : invtyTab) {

                List<InvtyTab> invtyTabs = new ArrayList<>();
                invtyTabs.add(iTab);
                // �޸Ŀ����������
                // System.out.println("for(InvtyTab iTab:invtyTab) {");
                InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(iTab);
//				if(InvtyTab sdas=invtyNumMapper.selectInvtyTabByTerm(iTab)!=null) {

                if (inTab.getNowStok().compareTo(iTab.getNowStok()) == 1
                        || inTab.getNowStok().compareTo(iTab.getNowStok()) == 0) {

                    // �޸Ŀ������
                    // System.out.println("�޸Ŀ������  ���� ");
                    message = "����ɹ���";
                    isSuccess = true;
                    invtyNumMapper.updateInvtyTab(invtyTabs);// �޸Ŀ�������
                    // �޸Ŀ����ûд SQL
                } else {
                    message = "�������������";
                    isSuccess = false;
                    try {
                        throw new RuntimeException("����  ����");
                    } catch (Exception e) {

                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        try {
                            return resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", true, message, null);
                        } catch (IOException e1) {

                        }
                    }

                }
            }

            for (MovBitTab mTab : movBitTab) {
                // System.out.println("for(MovBitTab mTab : movBitTab) {");
                List<MovBitTab> lmbt = new ArrayList<>();
                // System.out.println("new.size()" + lmbt.size());
                lmbt.add(mTab);
                // System.out.println("add.size()" + lmbt.size());
                if (invtyNumMapper.selectAllMbit(lmbt).size() > 0) {
                    List<MovBitTab> list = invtyNumMapper.selectAllMbit(lmbt);
                    for (MovBitTab bitTab : list) {
                        if (bitTab.getQty().compareTo(mTab.getQty()) == 1
                                || bitTab.getQty().compareTo(mTab.getQty()) == 0) {
                            message = "����ɹ���";
                            isSuccess = true;
                            // �޸���λ����(����)
                            invtyNumMapper.updateMovbitTab(lmbt);// �޸���λ��
                        } else {
                            isSuccess = false;
                            message = "����ʧ�ܣ�";
                            try {
                                throw new RuntimeException("����  ����");
                            } catch (Exception e) {

                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                try {
                                    return resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", isSuccess,
                                            message, null);
                                } catch (IOException e1) {

                                }

                            }
                        }

                    }

                } else {
                    message = "����ʧ�ܣ�";
                }

            }
            /**
             * for(OthOutIntoWhsSubTab oSubTab:othOutIntoWhsSubTab) { for(InvtyTab
             * iTab:invtyTab) { InvtyTab inTab=invtyNumMapper.selectInvtyTabByTerm(iTab);
             *
             * if(inTab.getNowStok().compareTo(oSubTab.getQty())==1||inTab.getNowStok().compareTo(oSubTab.getQty())==0)
             * {
             *
             * //�޸Ŀ������ invtyNumMapper.updateInvtyTab(invtyTab);//�޸Ŀ�������
             *
             * //�޸���λ����(����) invtyNumMapper.updateMovbitTab(movBitTab);//�޸���λ��
             *
             * isSuccess=true; message="����ɹ���"; }
             *
             * if(inTab.getNowStok().compareTo(oSubTab.getQty())==-1){ isSuccess=false;
             * message="�������������"; } } }
             */

            isSuccess = true;
            message = "����ɹ���";
        } else {
            isSuccess = false;
            message = "����ʧ�ܣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /**
     * ��λ�ж�
     */
    private void isGdsBit(OthOutIntoWhs othOutInto, List<OthOutIntoWhsSubTab> oSubTabList, String danHao) {
        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            MovBitTab bitTab = new MovBitTab();
            bitTab.setWhsEncd(othOutInto.getWhsEncd());
            bitTab.setInvtyEncd(tab.getInvtyEncd());
            bitTab.setBatNum(tab.getBatNum());
            bitTab.setOrderNum(danHao);
            bitTab.setSerialNum(tab.getOrdrNum().toString());

            MovBitTab bitTab2 = bitListMapper.selectInvtyGdsBitSum(bitTab);
            if (bitTab2 == null) {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + othOutInto.getWhsEncd() + ",�����"
                        + tab.getInvtyEncd() + ",���ţ�" + tab.getBatNum() + "�����޻�λ����");
            } else if (bitTab2.getQty().compareTo(tab.getQty()) != 0) {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + othOutInto.getWhsEncd() + ",�����"
                        + tab.getInvtyEncd() + ",���ţ�" + tab.getBatNum() + "���λ�λ�����뵥������������");
            }
        }
    }

    /**
     * �����λ
     */
    private void outGdsBitLis(OthOutIntoWhs othOutInto, List<OthOutIntoWhsSubTab> oSubTabList, String danHao) {
        // �Ƿ��λ����
        if (!isNtPrgrGdsBitMgmt(othOutInto.getWhsEncd())) {
            return;
        }
        // ����״̬
        List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(danHao);
        if (tabs.size() == 0) {
            throw new RuntimeException("���ݣ�" + danHao + "û�л�λ��Ϣ");
        }

        isGdsBit(othOutInto, oSubTabList, danHao);
        // ��λ
        for (MovBitTab tab : tabs) {
            MovBitTab whsTab = invtyNumMapper.selectMbit(tab);
            if (whsTab == null) {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + othOutInto.getWhsEncd() + ",�����"
                        + tab.getInvtyEncd() + ",���ţ�" + tab.getBatNum() + "���λ�λ�ϲ�����");
            } else if (whsTab.getQty().compareTo(tab.getQty()) == 1 || whsTab.getQty().compareTo(tab.getQty()) == 0) {
                tab.setMovBitEncd(whsTab.getMovBitEncd());
                invtyNumMapper.updateJianMbit(tab);// ����
            } else {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + othOutInto.getWhsEncd() + ",�����"
                        + tab.getInvtyEncd() + ",���ţ�" + tab.getBatNum() + "���λ�λ����������");
            }
        }
//		ɾ����λ������Ϣ��ֹ����
//		bitListMapper.deleteInvtyGdsBitList(Arrays.asList(danHao));

    }

    /**
     * ����λ
     */
    private void intoGdsBitLis(OthOutIntoWhs othOutInto, List<OthOutIntoWhsSubTab> oSubTabList, String danHao) {
        // �Ƿ��λ����
        if (!isNtPrgrGdsBitMgmt(othOutInto.getWhsEncd())) {
            return;
        }

        // ���״̬
        List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(danHao);
        if (tabs.size() == 0) {
            throw new RuntimeException("���ݣ�" + danHao + "û�л�λ��Ϣ");
        }

        isGdsBit(othOutInto, oSubTabList, danHao);
        /* ��λ��Ϣ */
        for (MovBitTab tab : tabs) {
            MovBitTab whsTab = invtyNumMapper.selectMbit(tab);
            if (whsTab == null) {
                invtyNumMapper.insertMovBitTabJia(tab);// ����
            } else {
                tab.setMovBitEncd(whsTab.getMovBitEncd());
                invtyNumMapper.updateJiaMbit(tab);// ���
            }
        }
//		ɾ����λ������Ϣ��ֹ����
//		bitListMapper.deleteInvtyGdsBitList(Arrays.asList(danHao));

    }

    private boolean isNtPrgrGdsBitMgmt(String whsEncd) {
        Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsEncd);
        if (whs != null && whs.compareTo(1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    // û����Դ������
    String laiYuanWuIn(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
        List<InvtyTab> invtyList = new ArrayList<>();// ����

//		if (a >= 1) {
        // ��ѯ�ӱ�
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");

        }
        // ���״̬
        intoGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // ����
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// �ֿ����
            // ����
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// �������
            invtyTab.setBatNum(tab.getBatNum());// ����
            invtyTab.setNowStok(tab.getQty());// �ִ���
            invtyTab.setAvalQty(tab.getQty());// ������
            invtyTab.setPrdcDt(tab.getPrdcDt());// ��������
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// ������
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// ʧЧ����
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// ��˰����
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// δ˰����
            invtyTab.setTaxRate(tab.getTaxRate());// ˰��
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// ��˰���
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// δ˰���

            invtyList.add(invtyTab);

            InvtyTab inTab1 = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab1 != null) {
                invtyNumMapper.updateInvtyTabAmtJia(invtyList);// �ִ������� ��� �޸Ŀ�������� ������Ϣ -->
                invtyNumMapper.updateAInvtyTabJia(invtyList);// ����������
                invtyList.clear();
            } else {
                // ��������
                invtyNumMapper.insertInvtyTabList(invtyList);
                invtyList.clear();
            }
        }
        return "����" + danHao + zhuagntai + "�ɹ���";
//		

    }

    // û����Դ���ݳ�
    String laiYuanWuOut(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
        List<InvtyTab> invtyList = new ArrayList<>();// ����
        String message = null;
//		if (a >= 1) {
        // ��ѯ�ӱ�
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");

        }
        // ��λ
        outGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // ����
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// �ֿ����
            // ����
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// �������
            invtyTab.setBatNum(tab.getBatNum());// ����
            invtyTab.setNowStok(tab.getQty());// �ִ���

            invtyTab.setAvalQty(tab.getQty());// ������
            invtyTab.setPrdcDt(tab.getPrdcDt());// ��������
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// ������
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// ʧЧ����
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// ��˰����
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// δ˰����
            invtyTab.setTaxRate(tab.getTaxRate());// ˰��
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// ��˰���
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// δ˰���

            invtyList.add(invtyTab);

            // �������
            InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab == null) {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd() + ",�����"
                        + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ�治����," + zhuagntai + "ʧ�ܣ�");
            }
            // ����
            if (((inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok()).compareTo(tab.getQty()) == 1
                    || (inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok())
                    .compareTo(tab.getQty()) == 0)
                    && ((inTab.getAvalQty() == null ? new BigDecimal("0") : inTab.getAvalQty())
                    .compareTo(tab.getQty()) == 1
                    || (inTab.getAvalQty() == null ? new BigDecimal("0") : inTab.getAvalQty())
                    .compareTo(tab.getQty()) == 0)) {
                invtyNumMapper.updateInvtyTabAmt(invtyList);// �ִ������� ���� �޸Ŀ�������� ������Ϣ
                invtyNumMapper.updateAInvtyTab(invtyList);// ����������

                invtyList.clear();
            } else {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd() + ",�����"
                        + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ����������," + zhuagntai + "ʧ�ܣ�");

            }

        }
        message = "����:" + danHao + zhuagntai + "�ɹ���";
        return message;

    }

    String outWhs(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
        List<InvtyTab> invtyList = new ArrayList<>();// ����
        String message = null;
//		if (a >= 1) {

        // ��ѯ�ӱ�
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");

        }

        // ��λ
        outGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // ����
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// �ֿ����
            // ����
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// �������
            invtyTab.setBatNum(tab.getBatNum());// ����
            invtyTab.setNowStok(tab.getQty());// �ִ���
            invtyTab.setAvalQty(tab.getQty());// ������
            invtyTab.setPrdcDt(tab.getPrdcDt());// ��������
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// ������
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// ʧЧ����
//				BigDecimal cntnTaxUprc=new BigDecimal(tab.get("cntnTaxUprc").asText());
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// ��˰����
//				BigDecimal unTaxUprc=new BigDecimal(tab.get("unTaxUprc").asText());
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// δ˰����
//				BigDecimal taxRate=new BigDecimal(tab.get("taxRate").asText());
            invtyTab.setTaxRate(tab.getTaxRate());// ˰��
//				BigDecimal cntnTaxAmt=new BigDecimal(tab.get("cntnTaxAmt").asText());
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// ��˰���
//				BigDecimal unTaxAmt=new BigDecimal(tab.get("unTaxAmt").asText());
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// δ˰���
            invtyList.add(invtyTab);

            // �������
            InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab == null) {
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd() + ",�����"
                        + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ�治����," + zhuagntai + "ʧ�ܣ�");
            }
            // ����

            if ((inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok()).compareTo(tab.getQty()) == 1
                    || (inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok())
                    .compareTo(tab.getQty()) == 0) {
                message = " ����" + danHao + zhuagntai + "�ɹ���";
                invtyNumMapper.updateInvtyTabAmt(invtyList);// �ִ������� ���� �޸Ŀ�������� ������Ϣ
                invtyList.clear();
            } else {
//					message = inTab.getInvtyEncd() + "�������������" + inTab.getBatNum() + "����" + danHao + " �������ʧ�ܣ�";
                throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd() + ",�����"
                        + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ����������," + zhuagntai + "ʧ�ܣ�");

            }

        }

        return message;

    }

    String inWhs(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// ��������� ��
        List<InvtyTab> invtyList = new ArrayList<>();// ����
        String message = null;

//		if (a >= 1) {
        // ��ѯ�ӱ�
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");

        }
        // ��λ
        intoGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // ����
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// �ֿ����
            // ����
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// �������
            invtyTab.setBatNum(tab.getBatNum());// ����
            invtyTab.setNowStok(tab.getQty());// �ִ���

            invtyTab.setAvalQty(tab.getQty());// ������
            invtyTab.setPrdcDt(tab.getPrdcDt());// ��������
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// ������
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// ʧЧ����
//				BigDecimal cntnTaxUprc=new BigDecimal(tab.get("cntnTaxUprc").asText());
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// ��˰����
//				BigDecimal unTaxUprc=new BigDecimal(tab.get("unTaxUprc").asText());
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// δ˰����
//				BigDecimal taxRate=new BigDecimal(tab.get("taxRate").asText());
            invtyTab.setTaxRate(tab.getTaxRate());// ˰��
//				BigDecimal cntnTaxAmt=new BigDecimal(tab.get("cntnTaxAmt").asText());
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// ��˰���
//				BigDecimal unTaxAmt=new BigDecimal(tab.get("unTaxAmt").asText());
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// δ˰���
            invtyList.add(invtyTab);

            InvtyTab inTab1 = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab1 != null) {
                invtyNumMapper.updateInvtyTabAmtJia(invtyList);// �ִ������� ��� �޸Ŀ�������� ������Ϣ -->
                invtyList.clear();
            } else {
                // ��������
                invtyNumMapper.insertInvtyTabList(invtyList);
                invtyList.clear();
            }
        }

        message = "����" + danHao + zhuagntai + "�ɹ�";

        return message;
    }

    // ���
    @Override
    public String updateOutInWhsChk(String userName, String jsonBody, String loginTime) {
        String message = "";
        ObjectNode jdRespJson = null;
        String zhuagntai = "���";
        try {
            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
        } catch (Exception e1) {
            throw new RuntimeException("json����ʧ��");
        }
        final String danHaos = jdRespJson.get("formNum").asText();

        String chkr = userName;// �����
        try {
            OthOutIntoWhs othOutInto = othOutIntoWhsMapper.selectIsChk(danHaos);
            Optional.ofNullable(othOutInto).orElseThrow(() -> new RuntimeException("����:" + danHaos + "������"));
            if (othOutInto.getIsNtChk() == 1) {
                throw new RuntimeException("���ţ�" + danHaos + "����ˣ�����Ҫ�ٴ����");
            }
            othOutInto.setChkr(chkr);
            othOutInto.setChkTm(loginTime);
            int a = othOutIntoWhsMapper.updateOutInWhsChk(Arrays.asList(othOutInto));// �޸����״̬
            if (a == 0) {
                throw new RuntimeException("���ţ�" + danHaos + "���ʧ�ܣ�");
            }
            if (othOutInto.getOutIntoWhsTypId().equals("2")) {
                // ��������
                message += outWhs(othOutInto, zhuagntai, danHaos);

            } else if (othOutInto.getOutIntoWhsTypId().equals("1")) {
                // ������� �жϳ���
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "2");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("�����������쳣"));
                if (intoWhs.getIsNtChk() == 0) {
                    throw new RuntimeException("�������ⵥδ��ˣ�������˵������ⵥ:" + intoWhs.getFormNum());
                }
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("7")) {
                // �̿�����

                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("8")) {
                // ��ӯ���
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            }
            // û����Դ
            // ��
            else if (othOutInto.getOutIntoWhsTypId().equals("11")) {

                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            }
            // ��
            else if (othOutInto.getOutIntoWhsTypId().equals("12")) {

                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("3")) {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "4");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("��װ�������쳣"));
                if (intoWhs.getIsNtChk() == 0) {
                    throw new RuntimeException("��װ���ⵥδ��ˣ����������װ���ⵥ:" + intoWhs.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// �޸����״̬
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " �������ʧ�ܣ�");
//                }
//                message += outWhs(intoWhs, "���", intoWhs.getFormNum());
                message += laiYuanWuIn(othOutInto, zhuagntai, othOutInto.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("4")) {
//                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "3");
//                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("��װ�������쳣"));
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " �������ʧ�ܣ�");
//                }
                message += outWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += inWhs(intoWhs, "���", intoWhs.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("5")) {// 5��ж���
                OthOutIntoWhs intoWhs11 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "6");
                Optional.ofNullable(intoWhs11).orElseThrow(() -> new RuntimeException("��ж�������쳣"));
                if (intoWhs11.getIsNtChk() == 0) {
                    throw new RuntimeException("��ж���ⵥδ��ˣ�������˲�ж���ⵥ:" + intoWhs11.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs11);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " �������ʧ�ܣ�");
//                }
                message += laiYuanWuIn(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += outWhs(intoWhs11, "���", intoWhs11.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("6")) {
//                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "5");
//                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("��ж�������쳣"));
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " �������ʧ�ܣ�");
//                }
//                message += inWhs(intoWhs, "���", intoWhs.getFormNum());
                message += outWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
            } else {
                throw new RuntimeException("������û�����ó��������:" + othOutInto.getOutIntoWhsTypId());
            }
            // 3 ��װ���
            // 4 ��װ����
            // 5 ��ж���
            // 6 ��ж����

            return message;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    // ����
    @Override
    public String updateOutInWhsNoChk(String userId, String jsonBody) {
        String message = "";
        ObjectNode jdRespJson = null;
        String zhuagntai = "����";
        try {
            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
        } catch (Exception e1) {

            e1.getMessage();
            throw new RuntimeException("json����ʧ��");
        }
        String danHaos = jdRespJson.get("formNum").asText();

//		String chkr = (String)jdRespJson.get("chkr");// �����

        OthOutIntoWhs othOutInto = othOutIntoWhsMapper.selectIsChk(danHaos);
        Optional.ofNullable(othOutInto).orElseThrow(() -> new RuntimeException("����:" + danHaos + "������"));
        if (othOutInto.getIsNtChk() == 0) {
            message = "����:" + danHaos + "������,����Ҫ�ظ����� ";
            return message;
        }
        if (othOutInto.getIsNtChk() == 1 && othOutInto.getIsNtBookEntry() == 1) {
            message = "����:" + danHaos + " �Ѽ��� ,��������";
            return message;
        }
        List<OthOutIntoWhs> oList = new ArrayList<>();// �����������
        othOutInto.setChkr(null);
        oList.add(othOutInto);
        try {

            int a = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// �޸�����״̬
            if (a == 0) {
                throw new RuntimeException("���ݣ�" + danHaos + " ��������ʧ�ܣ�");
            }
            if (othOutInto.getOutIntoWhsTypId().equals("2")) {
                // ��������
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "1");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("�����������쳣"));
                if (intoWhs.getIsNtChk() == 1) {
                    throw new RuntimeException("��������������ɵ�������ⵥ:" + intoWhs.getFormNum());
                }
                message += inWhs(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("1")) {
                // ������� �жϳ���
                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("7")) {
                // �̿�����
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("8")) {
                // ��ӯ���
                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            }
            // û����Դ
            // ��
            else if (othOutInto.getOutIntoWhsTypId().equals("11")) {
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            }
            // ��
            else if (othOutInto.getOutIntoWhsTypId().equals("12")) {

                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("3")) {
//                // 3 ��װ���
//                // 4 ��װ����
//                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "4");
//                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("��װ�������쳣"));
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " ��������ʧ�ܣ�");
//                }
//                message += inWhs(intoWhs, "����", intoWhs.getFormNum());
                message += laiYuanWuOut(othOutInto, zhuagntai, othOutInto.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("4")) {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "3");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("��װ�������쳣"));
                if (intoWhs.getIsNtChk() == 1) {
                    throw new RuntimeException("����������װ���ɵ�������ⵥ:" + intoWhs.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " ��������ʧ�ܣ�");
//                }
                message += inWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += outWhs(intoWhs, "����", intoWhs.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("5")) {
                // 5 ��ж���
                // 6 ��ж����
//                OthOutIntoWhs intoWhs11 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "6");
//                Optional.ofNullable(intoWhs11).orElseThrow(() -> new RuntimeException("��ж�������쳣"));
//                oList.clear();
//                oList.add(intoWhs11);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " ��������ʧ�ܣ�");
//                }
                message += laiYuanWuOut(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += inWhs(intoWhs11, "����", intoWhs11.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("6")) {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "5");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("��ж�������쳣"));
                if (intoWhs.getIsNtChk() == 1) {
                    throw new RuntimeException("���������ж���ɵ�������ⵥ:" + intoWhs.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// �޸����״̬
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " ��������ʧ�ܣ�");
//                }
                message += inWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += outWhs(intoWhs, "����", intoWhs.getFormNum());

            } else {
                throw new RuntimeException("������û�����ö�Ӧ�ĳ��������:" + othOutInto.getOutIntoWhsTypId());
            }

            return message;
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }
    }

    /**
     * //���
     *
     * @Override public String updateOutInWhsChk(String userId,String jsonBody) {
     * String message=""; Boolean isSuccess=true; String resp="";
     * <p>
     * ObjectNode jdRespJson; JsonNode resultJson; ArrayNode listJson =
     * null; try { jdRespJson = JacksonUtil.getObjectNode(jsonBody);
     * resultJson = jdRespJson.get("reqBody"); listJson = (ArrayNode)
     * resultJson.get("list"); } catch (IOException e1) {
     * }
     * <p>
     * List<OthOutIntoWhs> oList=new ArrayList<>();//�����������
     * List<OthOutIntoWhsSubTab> oSubTabList=new ArrayList<>();//��������� ��
     * List<InvtyTab> invtyList=new ArrayList<>();//����
     * <p>
     * for(Iterator<JsonNode> dbzhuIterator = listJson.iterator();
     * dbzhuIterator.hasNext();) { JsonNode dbzhu = dbzhuIterator.next();
     * <p>
     * oList=new ArrayList<>();//����������� oSubTabList=new
     * ArrayList<>();//��������� �� invtyList=new ArrayList<>();//����
     * <p>
     * //����������� OthOutIntoWhs othOutIntoWhs=new OthOutIntoWhs();
     * othOutIntoWhs.setFormNum(dbzhu.get("formNum").asText());
     * othOutIntoWhs.setOutStatus(dbzhu.get("outStatus").asText());
     * othOutIntoWhs.setInStatus(dbzhu.get("inStatus").asText());
     * othOutIntoWhs.setChkr(dbzhu.get("chkr").asText());
     * oList.add(othOutIntoWhs); //���� InvtyTab invtyTab=new InvtyTab();
     * invtyTab.setWhsEncd(dbzhu.get("whsEncd").asText());
     * <p>
     * OthOutIntoWhs
     * oIntoWhs=othOutIntoWhsMapper.selectIsChk(othOutIntoWhs.getFormNum());
     * if(oIntoWhs.getIsNtChk()==1) {
     * message+=othOutIntoWhs.getFormNum()+" �����״̬�����ٴ���� "; }else { int
     * a=othOutIntoWhsMapper.updateOutInWhsChk(oList);//�޸����״̬ if(a>=1) {
     * ArrayNode canListJson=(ArrayNode) dbzhu.get("canList");
     * for(Iterator<JsonNode> dbziIterator = canListJson.iterator();
     * dbziIterator.hasNext();) { JsonNode dbzi = dbziIterator.next();
     * <p>
     * BigDecimal qty=new BigDecimal(dbzi.get("qty").asText());
     * OthOutIntoWhsSubTab oIntoWhsSubTab=new OthOutIntoWhsSubTab();
     * oIntoWhsSubTab.setQty(qty); oSubTabList.add(oIntoWhsSubTab);
     * <p>
     * //���� invtyTab.setInvtyEncd(dbzi.get("invtyEncd").asText());//�������
     * invtyTab.setBatNum(dbzi.get("batNum").asText());//����
     * invtyTab.setNowStok(qty);//�ִ���
     * <p>
     * invtyTab.setAvalQty(qty);//������
     * invtyTab.setPrdcDt(dbzi.get("prdcDt").asText());
     * invtyTab.setBaoZhiQi(dbzi.get("baoZhiQi").asText());
     * invtyTab.setInvldtnDt(dbzi.get("invldtnDt").asText()); BigDecimal
     * cntnTaxUprc=new BigDecimal(dbzi.get("cntnTaxUprc").asText());
     * invtyTab.setCntnTaxUprc(cntnTaxUprc); BigDecimal unTaxUprc=new
     * BigDecimal(dbzi.get("unTaxUprc").asText());
     * invtyTab.setUnTaxUprc(unTaxUprc); BigDecimal taxRate=new
     * BigDecimal(dbzi.get("taxRate").asText());
     * invtyTab.setTaxRate(taxRate); BigDecimal cntnTaxAmt=new
     * BigDecimal(dbzi.get("cntnTaxAmt").asText());
     * invtyTab.setCntnTaxAmt(cntnTaxAmt); BigDecimal unTaxAmt=new
     * BigDecimal(dbzi.get("unTaxAmt").asText());
     * invtyTab.setUnTaxAmt(unTaxAmt); //������� ��λ����
     * (ͨ���ֿ���롢������롢���Ų�ѯ��ѯ����λ���� ͨ����λ�����ѯ�ֿ����) } invtyList.add(invtyTab);
     * <p>
     * //�޸Ŀ���������� for(InvtyTab iTab:invtyList) { InvtyTab
     * inTab=invtyNumMapper.selectInvtyTabByTerm(iTab);
     * for(OthOutIntoWhsSubTab oSubTab:oSubTabList) {
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==1||inTab.getNowStok().compareTo(oSubTab.getQty())==0)
     * {
     * <p>
     * //������������� 11:�������� 12:�������
     * if(othOutIntoWhs.getOutStatus()!=null||othOutIntoWhs.getOutStatus()!="")
     * { invtyNumMapper.updateInvtyTabAmt(invtyList);//�ִ������� ����
     * <p>
     * }else
     * if(othOutIntoWhs.getInStatus()!=null||othOutIntoWhs.getInStatus()!="")
     * { //�жϿ������Ƿ���� for(InvtyTab iTab1:invtyList) { InvtyTab
     * inTab1=invtyNumMapper.selectInvtyTabByTerm(iTab1); if(inTab1!=null)
     * { invtyNumMapper.updateInvtyTabAmtJia(invtyList);//�ִ������� ��� }else {
     * //�������� invtyNumMapper.insertInvtyTabList(invtyList); }
     * <p>
     * } } isSuccess=true; message+=othOutIntoWhs.getFormNum()+" ������˳ɹ���";
     * }
     * <p>
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==-1){
     * isSuccess=false; message="�������������"; } } }
     * <p>
     * }else { isSuccess=false; message+=othOutIntoWhs.getFormNum()+"
     * �������ʧ�ܣ�"; }
     * <p>
     * }
     * <p>
     * }
     * <p>
     * try {
     * resp=BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsChk",
     * isSuccess, message, null); } catch (IOException e) {
     * } return resp; }
     * <p>
     * <p>
     * //����
     * @Override public String updateOutInWhsNoChk(String userId,String jsonBody) {
     * String message=""; Boolean isSuccess=true; String resp="";
     * <p>
     * ObjectNode jdRespJson; JsonNode resultJson; ArrayNode listJson =
     * null; try { jdRespJson = JacksonUtil.getObjectNode(jsonBody);
     * resultJson = jdRespJson.get("reqBody"); listJson = (ArrayNode)
     * resultJson.get("list"); } catch (IOException e1) {
     * }
     * <p>
     * List<OthOutIntoWhs> oList=new ArrayList<>();//�����������
     * List<OthOutIntoWhsSubTab> oSubTabList=new ArrayList<>();//��������� ��
     * List<InvtyTab> invtyList=new ArrayList<>();//����
     * <p>
     * for(Iterator<JsonNode> dbzhuIterator = listJson.iterator();
     * dbzhuIterator.hasNext();) { JsonNode dbzhu = dbzhuIterator.next();
     * <p>
     * oList=new ArrayList<>();//����������� oSubTabList=new
     * ArrayList<>();//��������� �� invtyList=new ArrayList<>();//����
     * <p>
     * //����������� OthOutIntoWhs othOutIntoWhs=new OthOutIntoWhs();
     * othOutIntoWhs.setFormNum(dbzhu.get("formNum").asText());
     * othOutIntoWhs.setOutStatus(dbzhu.get("outStatus").asText());
     * othOutIntoWhs.setInStatus(dbzhu.get("inStatus").asText());
     * othOutIntoWhs.setChkr(dbzhu.get("chkr").asText());
     * oList.add(othOutIntoWhs); //���� InvtyTab invtyTab=new InvtyTab();
     * invtyTab.setWhsEncd(dbzhu.get("whsEncd").asText());
     * <p>
     * OthOutIntoWhs
     * oIntoWhs=othOutIntoWhsMapper.selectIsChk(othOutIntoWhs.getFormNum());
     * if(oIntoWhs.getIsNtChk()==0) {
     * message+=othOutIntoWhs.getFormNum()+" ������״̬�����ٴ����� "; }else { int
     * a=othOutIntoWhsMapper.updateOutInWhsNoChk(oList);//�޸����״̬ if(a>=1)
     * { ArrayNode canListJson=(ArrayNode) dbzhu.get("canList");
     * for(Iterator<JsonNode> dbziIterator = canListJson.iterator();
     * dbziIterator.hasNext();) { JsonNode dbzi = dbziIterator.next();
     * <p>
     * BigDecimal qty=new BigDecimal(dbzi.get("qty").asText());
     * OthOutIntoWhsSubTab oIntoWhsSubTab=new OthOutIntoWhsSubTab();
     * oIntoWhsSubTab.setQty(qty); oSubTabList.add(oIntoWhsSubTab);
     * <p>
     * //���� invtyTab.setInvtyEncd(dbzi.get("invtyEncd").asText());//�������
     * invtyTab.setBatNum(dbzi.get("batNum").asText());//����
     * invtyTab.setNowStok(qty);//�ִ���
     * <p>
     * BigDecimal taxRate=new BigDecimal(dbzi.get("taxRate").asText());
     * invtyTab.setTaxRate(taxRate); BigDecimal unTaxAmt=new
     * BigDecimal(dbzi.get("unTaxAmt").asText());
     * invtyTab.setUnTaxAmt(unTaxAmt); } invtyList.add(invtyTab);
     * <p>
     * //�޸Ŀ���������� for(InvtyTab iTab:invtyList) { InvtyTab
     * inTab=invtyNumMapper.selectInvtyTabByTerm(iTab);
     * for(OthOutIntoWhsSubTab oSubTab:oSubTabList) {
     * <p>
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==1||inTab.getNowStok().compareTo(oSubTab.getQty())==0)
     * {
     * <p>
     * if(othOutIntoWhs.getOutStatus()!=null||othOutIntoWhs.getOutStatus()!="")
     * { invtyNumMapper.updateInvtyTabAmtJia(invtyList);//�ִ������� ���� }else
     * if(othOutIntoWhs.getInStatus()!=null||othOutIntoWhs.getInStatus()!="")
     * { invtyNumMapper.updateInvtyTabAmt(invtyList);//�ִ������� ��� }
     * <p>
     * isSuccess=true; message+=othOutIntoWhs.getFormNum()+" ��������ɹ���"; }
     * <p>
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==-1){
     * isSuccess=false; message="�������������"; } } }
     * <p>
     * }else { isSuccess=false; message+=othOutIntoWhs.getFormNum()+"
     * ��������ʧ�ܣ�"; }
     * <p>
     * }
     * <p>
     * }
     * <p>
     * <p>
     * try {
     * resp=BaseJson.returnRespObj("whs/out_into_whs/updateOutInWhsNoChk",
     * isSuccess, message, null); } catch (IOException e) {
     * } return resp; }
     **/
    @Override
    public String updateMovbitTab(List<MovBitTab> movBitTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = invtyNumMapper.updateMovbitTab(movBitTab);
        if (i >= 1) {
            message = "��λ�޸ĳɹ���";
            isSuccess = true;
        } else {
            message = "��λ�޸�ʧ�ܣ�";
            isSuccess = false;
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/updateMovbitTab", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(@SuppressWarnings("rawtypes") Map map) {
        String resp = "";
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("formNum", getList((String) map.get("formNum")));
        map.put("outIntoWhsTypId", getList((String) map.get("outIntoWhsTypId")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("srcFormNum", getList((String) map.get("srcFormNum")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("recvSendCateId", getList((String) map.get("recvSendCateId")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<OthOutIntoWhsMap> aList = othOutIntoWhsMapper.selectList(map);
//		List<OthOutIntoWhs> aList = othOutIntoWhsMapper.selectListDaYin(map);
        aList.add(new OthOutIntoWhsMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/out_into_whs/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, OthOutIntoWhs> pusOrderMap = uploadScoreInfo(file);
        // System.out.println(pusOrderMap.size());

        for (Map.Entry<String, OthOutIntoWhs> entry : pusOrderMap.entrySet()) {

            OthOutIntoWhs oIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(entry.getValue().getFormNum());
            if (oIntoWhs != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");

            }
            try {
                othOutIntoWhsMapper.exInsertOthOutIntoWhs(entry.getValue());
                othOutIntoWhsMapper.exInsertOthOutIntoWhsSubTab(entry.getValue().getOutIntoSubList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "��������ⵥ�����ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // ����excle
    private Map<String, OthOutIntoWhs> uploadScoreInfo(MultipartFile file) {
        Map<String, OthOutIntoWhs> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            // ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
            Workbook wb0 = new HSSFWorkbook(fileIn);

            // ��ȡExcel�ĵ��еĵ�һ����
            Sheet sht0 = wb0.getSheetAt(0);
            // ��õ�ǰsheet�Ŀ�ʼ��
            int firstRowNum = sht0.getFirstRowNum();
            // ��ȡ�ļ������һ��
            int lastRowNum = sht0.getLastRowNum();
            // System.out.println(lastRowNum);
            // ���������ֶκ��±�ӳ��
            SetColIndex(sht0.getRow(firstRowNum));
            // ��Sheet�е�ÿһ�н��е���
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                // �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }

//				String outInto=r.getCell(1).getStringCellValue();
//				 String tern = ".*���.*";
//				 String pat = ".*����.*";
                String orderNo = GetCellData(r, "���ݺ�");
//			      boolean into = Pattern.matches(tern, outInto);
//			      boolean out = Pattern.matches(pat, outInto);
//			      if(into) {
//				 orderNo = r.getCell(6).getStringCellValue()+"into";
//
//			      }else if(out) {
//				 orderNo = r.getCell(6).getStringCellValue()+"out";
//
//			      }

                // ����ʵ����
                // System.out.println(orderNo);
                OthOutIntoWhs othOutIntoWhs = new OthOutIntoWhs();
                if (temp.containsKey(orderNo)) {
                    othOutIntoWhs = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                // r.getCell(6).getStringCellValue()
                othOutIntoWhs.setFormNum(orderNo); // ���� q varchar(200
                othOutIntoWhs.setFormDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������")); // ��������
                othOutIntoWhs.setWhsEncd(GetCellData(r, "�ֿ����")); // �ֿ���� q varchar(200
                othOutIntoWhs.setSrcFormNum(GetCellData(r, "��Դ���ݺ�")); // ��Դ���ݺ� q varchar(200

//				String outIntoWhsTypId;
//				String outStatus = null;
//				String inStatus=null;
//				switch (outInto) {
//				case "�������":outIntoWhsTypId="1";outStatus=null;inStatus="������";
//					break;
//				case "��������":outIntoWhsTypId="11";outStatus="������";inStatus=null;
//					break;
//				case "�������":outIntoWhsTypId="12";outStatus=null;inStatus="������";
//					break;
//				case "��������":outIntoWhsTypId="2";outStatus="������";inStatus=null;
//					break;
//				case "��װ���":outIntoWhsTypId="3";outStatus=null;inStatus="������";
//					break;
//				case "��װ����":outIntoWhsTypId="4";outStatus="������";inStatus=null;
//					break;
//				case "��ж���":outIntoWhsTypId="5";outStatus=null;inStatus="������";
//					break;
//				case "��ж����":outIntoWhsTypId="6";outStatus="������";inStatus=null;
//					break;
//				case "�̿�����":outIntoWhsTypId="7";outStatus="������";inStatus=null;
//					break;
//				case "��ӯ���":outIntoWhsTypId="8";outStatus=null;inStatus="������";
//					break;
//				default:outIntoWhsTypId="0";
//				 String tern = ".*���.*";
//				 String pat = ".*����.*";
//
//			      boolean into = Pattern.matches(tern, outInto);
//			      boolean out = Pattern.matches(pat, outInto);
//			      if(into) {
//			    	  inStatus="������";
//			      }
//			      if(out) {
//			    	  outStatus="������";
//			      }
//					break;
//				}
//				

                othOutIntoWhs.setOutIntoWhsTypId(
                        (new Double(GetCellData(r, "��������ͱ���") == null || GetCellData(r, "��������ͱ���").equals("") ? null
                                : GetCellData(r, "��������ͱ���"))).intValue() + ""); // ����������� q ����ҵ�������ж� varchar(200
                othOutIntoWhs.setFormTypEncd(othOutIntoWhs.getOutIntoWhsTypId().equals("11") ? "015" : "014");// 014
                // ������ⵥ
                // 015
                // �������ⵥ

//                othOutIntoWhs.setOutStatus(GetCellData(r, "����״̬")); // ����״̬ �������� varchar(200
//                othOutIntoWhs.setInStatus(GetCellData(r, "���״̬")); // ���״̬ �������� �ж� varchar(200
                othOutIntoWhs.setMemo(GetCellData(r, "��ע")); // ��עq varchar(2000
//				othOutIntoWhs.setIsNtWms((new Double(GetCellData(r, "�Ƿ���WMS�ϴ�"))).intValue()); // �Ƿ���WMS�ϴ� û int(11
                othOutIntoWhs.setIsNtChk(
                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����"))).intValue()); // �Ƿ���� int(11
                othOutIntoWhs.setIsNtBookEntry(
                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����"))).intValue()); // �Ƿ���� int(11
//                othOutIntoWhs.setIsNtCmplt(
//                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ����"))).intValue()); // �Ƿ���� int(11
//                othOutIntoWhs.setIsNtClos(
//                        (new Double(GetCellData(r, "�Ƿ�ر�") == null || GetCellData(r, "�Ƿ�ر�").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ�ر�"))).intValue()); // �Ƿ�ر� int(11
//                othOutIntoWhs.setPrintCnt(
//                        (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
//                                : GetCellData(r, "��ӡ����"))).intValue()); // ��ӡ���� q int(11
                othOutIntoWhs.setSetupPers(GetCellData(r, "������")); // ������ q varchar(200
                othOutIntoWhs.setSetupTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // ����ʱ��
                // q
                // datetime
                othOutIntoWhs.setMdfr(GetCellData(r, "�޸���")); // �޸��� q varchar(200
                othOutIntoWhs.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��")); // �޸�ʱ��
                // q
                // datetime
                othOutIntoWhs.setChkr(GetCellData(r, "�����")); // ����� q varchar(200
                othOutIntoWhs.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��")); // ���ʱ��
                // q
                // datetime
                othOutIntoWhs.setBookEntryPers(GetCellData(r, "������")); // ������ q varchar(200
                othOutIntoWhs.setBookEntryTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // ����ʱ��
                // datetime
//                othOutIntoWhs.setOperator(GetCellData(r, "������")); // ������ q varchar(200
//                othOutIntoWhs.setOperatorId(GetCellData(r, "�����˱���")); // �����˱��� q varchar(200

                othOutIntoWhs.setIsNtMakeVouch(
                        (new Double(GetCellData(r, "�Ƿ�����ƾ֤") == null || GetCellData(r, "�Ƿ�����ƾ֤").equals("") ? "0"
                                : GetCellData(r, "�Ƿ�����ƾ֤"))).intValue()); // �Ƿ�����ƾ֤ int(11
                othOutIntoWhs.setMakVouchPers(GetCellData(r, "��ƾ֤��")); // ��ƾ֤��
                othOutIntoWhs.setMakVouchTm(
                        GetCellData(r, "��ƾ֤ʱ��") == null ? "" : GetCellData(r, "��ƾ֤ʱ��")); // ��ƾ֤ʱ��

                othOutIntoWhs.setRecvSendCateId(GetCellData(r, "�շ�������"));//�շ����id

                List<OthOutIntoWhsSubTab> OthOutIntoWhsSub = othOutIntoWhs.getOutIntoSubList();// �����ӱ�
                if (OthOutIntoWhsSub == null) {
                    OthOutIntoWhsSub = new ArrayList<>();
                }

                OthOutIntoWhsSubTab othOutIntoWhsSubList = new OthOutIntoWhsSubTab();

//				othOutIntoWhsSubList.setOrdrNum(ordrNum); // ��� bigint(20
                othOutIntoWhsSubList.setFormNum(orderNo); // ���� varchar(200
                othOutIntoWhsSubList.setInvtyEncd(GetCellData(r, "�������")); // ������� 1 varchar(200
                othOutIntoWhsSubList.setQty(new BigDecimal(GetCellData(r, "����") == null ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ���� 1 decimal(20,8
                othOutIntoWhsSubList.setTaxRate(new BigDecimal(
                        GetCellData(r, "˰��") == null || GetCellData(r, "˰��").equals("") ? "0" : GetCellData(r, "˰��"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ˰�� decimal(20,8
                othOutIntoWhsSubList.setCntnTaxUprc(
                        new BigDecimal(GetCellData(r, "��˰����") == null || GetCellData(r, "��˰����").equals("") ? "0"
                                : GetCellData(r, "��˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ��˰���� decimal(20,8
                othOutIntoWhsSubList.setUnTaxUprc(
                        new BigDecimal(GetCellData(r, "δ˰����") == null || GetCellData(r, "δ˰����").equals("") ? "0"
                                : GetCellData(r, "δ˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰����[����(�������δ��뵥��]
                // decimal(20,8
                othOutIntoWhsSubList.setBookEntryUprc(
                        new BigDecimal(GetCellData(r, "���˵���") == null || GetCellData(r, "���˵���").equals("") ? "0"
                                : GetCellData(r, "���˵���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ���˵��� decimal(20,8
                othOutIntoWhsSubList.setCntnTaxAmt(
                        new BigDecimal(GetCellData(r, "��˰���") == null || GetCellData(r, "��˰���").equals("") ? "0"
                                : GetCellData(r, "��˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ��˰��� decimal(20,8
                othOutIntoWhsSubList.setUnTaxAmt(
                        new BigDecimal(GetCellData(r, "δ˰���") == null || GetCellData(r, "δ˰���").equals("") ? "0"
                                : GetCellData(r, "δ˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰��� decimal(20,8
                othOutIntoWhsSubList.setIntlBat(GetCellData(r, "��������")); // �������� 1 varchar(200
                othOutIntoWhsSubList.setBatNum(GetCellData(r, "����")); // ���� 1 varchar(200
                othOutIntoWhsSubList.setPrdcDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������")); // ��������
                // 1
                othOutIntoWhsSubList.setBxQty(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ���� a decimal(208
                // datetime
                othOutIntoWhsSubList.setBaoZhiQi(GetCellData(r, "������")); // ������ 1 varchar(200
                othOutIntoWhsSubList.setInvldtnDt(
                        GetCellData(r, "ʧЧ����") == null ? "" : GetCellData(r, "ʧЧ����")); // ʧЧ����
                // 1
                // datetime
                othOutIntoWhsSubList.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����
                othOutIntoWhsSubList.setMemos(GetCellData(r, "�ӱ�ע")); //�ӱ�ע

                OthOutIntoWhsSub.add(othOutIntoWhsSubList);

                othOutIntoWhs.setOutIntoSubList(OthOutIntoWhsSub);
                temp.put(orderNo, othOutIntoWhs);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());

        }
        return temp;
    }

    @Override
    public String queryMovBitTab(String formNum, String invtyEncd, String batNum) {
        // TODO Auto-generated method stub
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<MovBitTab> oSubTabList = new ArrayList<>();
        OthOutIntoWhs oIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(formNum);
        if (oIntoWhs != null) {
            MovBitTab record = new MovBitTab();
            record.setBatNum(batNum);
            record.setInvtyEncd(invtyEncd);
            record.setOrderNum(formNum);

            oSubTabList = bitListMapper.selectInvtyGdsBitList(record);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + formNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/out_into_whs/queryMovBitTab", isSuccess, message, oIntoWhs,
                    oSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String insertInvtyGdsBitList(List<MovBitTab> oList, String orderNum, String serialNum) {
        // TODO �Զ����ɵķ������
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<MovBitTab> movBitTabList = othOutIntoWhsMapper.selectallInvtyGdsBitListInto(orderNum);
            if (movBitTabList.size() == 0) {
                throw new RuntimeException("����" + orderNum + "�����ڻ�����˲���Ҫ�����λ");
            }
            movBitTabList.stream().filter(mov -> serialNum.equals(mov.getSerialNum())).findFirst()
                    .orElseThrow(() -> new RuntimeException("����" + orderNum + "��" + serialNum + "��Ŵ�����ˢ��ҳ��"));
            bitListMapper.insertInvtyGdsBitLists(oList);
            message = "�ɹ�";
            resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, message, null);
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, e.getMessage(), null);
            } catch (IOException e1) {
                // TODO �Զ����ɵ� catch ��

            }

        }
        return resp;
    }

    @Override
    public String deleteInvtyGdsBitList(String num, String dengji, String list) {
        // TODO �Զ����ɵķ������
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> lists = getList(list);
        // System.out.println(lists.size());
        if (lists == null || lists.size() == 0) {
            try {
                isSuccess = false;
                return BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, "��������,ֵΪ��", null);
            } catch (IOException e) {
            }
        }
        try {
//			list  $() getList

//            OthOutIntoWhs othOutIntoWhs = othOutIntoWhsMapper.selectIsChk(num);
//            if (othOutIntoWhs == null) {
//                throw new RuntimeException(num + " ������");
//            }
//            if (othOutIntoWhs.getIsNtChk() == 1) {
//                throw new RuntimeException(num + "����˲���ɾ��");
//            }

            switch (dengji) {
                case "orderNum":
                    bitListMapper.deleteInvtyGdsBitList(lists);

                    break;
                case "serialNum":
                    bitListMapper.deleteInvtyGdsBitSerial(lists);

                    break;
                case "id":
                    bitListMapper.deleteInvtyGdsBitId(lists);
                    break;

                default:
                    return BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, "��������", null);

            }
            message = "ɾ���ɹ�";
            resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, message, null);
        } catch (Exception e) {

            try {
                isSuccess = false;
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, e.getMessage(), null);
            } catch (IOException e1) {

            }

        }

        return resp;
    }

    // ��ѯ
    @Override
    public String queryInvtyGdsBitList(String json) {
        // TODO �Զ����ɵķ������
        String resp = "";
        try {
            MovBitTab bitTab = JacksonUtil.getPOJO(JacksonUtil.getObjectNode(json), MovBitTab.class);
//			MovBitTab bitTab = new MovBitTab();
//			bitTab.setOrderNum(node.has("orderNum") ? node.get("orderNum").asText() : null);
//			bitTab.setSerialNum(node.has("serialNum") ? node.get("serialNum").asText() : null);
            List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitList(bitTab);
            if (bitTabs.size() > 0) {
                resp = BaseJson.returnRespList("whs/out_into_whs/queryInvtyGdsBitList", true, "��ѯ�ɹ�", bitTabs);
            } else {
                resp = BaseJson.returnRespList("whs/out_into_whs/queryInvtyGdsBitList", false, "û�л�λ��Ϣ", bitTabs);
            }
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", false, e.getMessage(), null);
            } catch (IOException e1) {
                // TODO �Զ����ɵ� catch ��

            }

        }

        return resp;
    }

    @Override
    public String uploadInvtyGdsBitList(List<MovBitTab> xinlist, List<MovBitTab> gailist, String orderNum,
                                        String serialNum) {
        // TODO �Զ����ɵķ������
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
//		OthOutIntoWhs othOutIntoWhs = othOutIntoWhsMapper.selectIsChk(orderNum);
//		if (othOutIntoWhs == null) {
//			throw new RuntimeException(orderNum + " ������");
//		}
//		if (othOutIntoWhs.getIsNtChk() == 1) {
//			throw new RuntimeException(orderNum + "����˲����޸�");
//		}
//		List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListNum(list, orderNum);
//		for (MovBitTab movBitTab : bitTabs) {
//			listStrings.add(movBitTab.getId().toString());
//		}
//		for (MovBitTab movBitTab : gailist) {
//			listStrings.remove(movBitTab.getId().toString());
//		}
        List<MovBitTab> movBitTabList = othOutIntoWhsMapper.selectallInvtyGdsBitListInto(orderNum);
        if (movBitTabList.size() == 0) {
            throw new RuntimeException("����" + orderNum + "�����ڻ�����˲���Ҫ�����λ");
        }
        movBitTabList.stream().filter(mov -> serialNum.equals(mov.getSerialNum())).findFirst()
                .orElseThrow(() -> new RuntimeException("����" + orderNum + "��" + serialNum + "��Ŵ�����ˢ��ҳ��"));
        try {

            if (gailist.size() > 0) {
                bitListMapper.updateInvtyGdsBitLists(gailist);
            }
            if (xinlist.size() > 0) {
                bitListMapper.insertInvtyGdsBitLists(xinlist);
            }

            message = "�޸ĳɹ�";
            xinlist.addAll(gailist);
            resp = BaseJson.returnRespObjList("whs/out_into_whs/queryInvtyGdsBitList", isSuccess, message, null,
                    xinlist);
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, e.getMessage(), null);
            } catch (IOException e1) {

            }

        }

        return resp;
    }

    // ��������
    @Override
    public String wholeSingleLianZha(String formNum, String formTypEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

//		11		��������
//		12		�������
        if (formTypEncd.equals("011")) {
//        011	������
//			1		�������
//			2		��������
            CannibSngl cannibSngls = cannibSnglMapper.selectCSngl(formNum);
            if (cannibSngls == null) {
                isSuccess = true;
                message = message + formNum + "�����ڵ������򵥾ݱ�ɾ��\n";
            } else if (cannibSngls.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "������δ���\n";
            } else {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "1");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "2");
                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "������ⵥ��ɾ��\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// �������ͱ���
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// ������������
                    intohashMap.put("FormNum", intoWhs.getFormNum());// ���ݺ�
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// ���״̬
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// ������
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// ����ʱ��
                    list.add(intohashMap);
                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "�������ⵥ��ɾ��\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// �������ͱ���
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// ������������
                    outhashMap.put("FormNum", outWhs.getFormNum());// ���ݺ�
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// ���״̬
                    outhashMap.put("setupPers", outWhs.getSetupPers());// ������
                    outhashMap.put("setupTm", outWhs.getSetupTm());// ����ʱ��
                    list.add(outhashMap);

                }
            }

        } else if (formTypEncd.equals("012")) {
//          012	��װ��	 
//			3		��װ���
//			4		��װ����
            AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(formNum);
            if (aDisambSngl == null) {
                isSuccess = true;
                message = message + formNum + "��������װ���򵥾ݱ�ɾ��\n";
            } else if (aDisambSngl.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "��װ��δ���\n";
            } else {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "3");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "4");
                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "������ⵥ��ɾ��\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// �������ͱ���
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// ������������
                    intohashMap.put("FormNum", intoWhs.getFormNum());// ���ݺ�
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// ���״̬
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// ������
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// ����ʱ��
                    list.add(intohashMap);

                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "�������ⵥ��ɾ��\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// �������ͱ���
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// ������������
                    outhashMap.put("FormNum", outWhs.getFormNum());// ���ݺ�
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// ���״̬
                    outhashMap.put("setupPers", outWhs.getSetupPers());// ������
                    outhashMap.put("setupTm", outWhs.getSetupTm());// ����ʱ��
                    list.add(outhashMap);

                }
            }
        } else if (formTypEncd.equals("013")) {
//          013	��ж��
//			5		��ж���
//			6		��ж����
            AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(formNum);
            if (aDisambSngl == null) {
                isSuccess = true;
                message = message + formNum + "�����ڲ�ж���򵥾ݱ�ɾ��\n";
            } else if (aDisambSngl.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "��ж��δ���\n";
            } else {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "5");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "6");
                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "������ⵥ��ɾ��\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// �������ͱ���
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// ������������
                    intohashMap.put("FormNum", intoWhs.getFormNum());// ���ݺ�
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// ���״̬
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// ������
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// ����ʱ��
                    list.add(intohashMap);

                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "�������ⵥ��ɾ��\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// �������ͱ���
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// ������������
                    outhashMap.put("FormNum", outWhs.getFormNum());// ���ݺ�
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// ���״̬
                    outhashMap.put("setupPers", outWhs.getSetupPers());// ������
                    outhashMap.put("setupTm", outWhs.getSetupTm());// ����ʱ��
                    list.add(outhashMap);
                }
            }

        } else if (formTypEncd.equals("015") || formTypEncd.equals("014")) {
//			 015 �������ⵥ	 014 ������ⵥ
            OthOutIntoWhs outIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(formNum);
            Optional.ofNullable(outIntoWhs).orElseThrow(() -> new RuntimeException(formNum + "�������뵥������"));
            switch (outIntoWhs.getOutIntoWhsTypId()) {
                // ����
                case "11":
                case "12":
                    isSuccess = true;
                    message = "��������ⵥû����Դ����";
                    list = null;
                    break;
                // ����
                case "1"://
                case "2":
                    CannibSngl cannibSngls = cannibSnglMapper.selectCSngl(outIntoWhs.getSrcFormNum());
                    Optional.ofNullable(cannibSngls).orElseThrow(() -> new RuntimeException("��Դ�����ѱ�ɾ��"));
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("FormTypName", cannibSngls.getFormTypName());// �������ͱ���
                    hashMap.put("FormTypEncd", cannibSngls.getFormTypEncd());// ������������
                    hashMap.put("FormNum", cannibSngls.getFormNum());// ���ݺ�
                    hashMap.put("isNtChk", cannibSngls.getIsNtChk());// ���״̬
                    hashMap.put("setupPers", cannibSngls.getSetupPers());// ������
                    hashMap.put("setupTm", cannibSngls.getSetupTm());// ����ʱ��

                    list.add(hashMap);
                    break;
                // ��װ ��ж
                case "3":
                case "4":
                case "5":
                case "6":
                    AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(outIntoWhs.getSrcFormNum());
                    Optional.ofNullable(aDisambSngl).orElseThrow(() -> new RuntimeException("��Դ�����ѱ�ɾ��"));
                    HashMap<String, Object> AhashMap = new HashMap<String, Object>();
                    AhashMap.put("FormTypName", aDisambSngl.getFormTypName());// �������ͱ���
                    AhashMap.put("FormTypEncd", aDisambSngl.getFormTypEncd());// ������������
                    AhashMap.put("FormNum", aDisambSngl.getFormNum());// ���ݺ�
                    AhashMap.put("isNtChk", aDisambSngl.getIsNtChk());// ���״̬
                    AhashMap.put("setupPers", aDisambSngl.getSetupPers());// ������
                    AhashMap.put("setupTm", aDisambSngl.getSetupTm());// ����ʱ��

                    list.add(AhashMap);
                    break;
                // �̵�
                case "7":
                case "8":
                    CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(outIntoWhs.getSrcFormNum());
                    Optional.ofNullable(checkPrftLoss).orElseThrow(() -> new RuntimeException("��Դ�����ѱ�ɾ��"));
                    HashMap<String, Object> BhashMap = new HashMap<String, Object>();
                    BhashMap.put("FormTypName", checkPrftLoss.getFormTypName());// �������ͱ���
                    BhashMap.put("FormTypEncd", checkPrftLoss.getFormTypEncd());// ������������
                    BhashMap.put("FormNum", checkPrftLoss.getCheckFormNum());// ���ݺ�
                    BhashMap.put("isNtChk", checkPrftLoss.getIsNtChk());// ���״̬
                    BhashMap.put("setupPers", checkPrftLoss.getSetupPers());// ������
                    BhashMap.put("setupTm", checkPrftLoss.getSetupTm());// ����ʱ��

                    list.add(BhashMap);
                    CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(checkPrftLoss.getSrcFormNum());

                    Optional.ofNullable(checkSngl).orElseThrow(() -> new RuntimeException("��Դ�����ѱ�ɾ��"));
                    HashMap<String, Object> ChashMap = new HashMap<String, Object>();
                    ChashMap.put("FormTypName", checkSngl.getFormTypName());// �������ͱ���
                    ChashMap.put("FormTypEncd", checkSngl.getFormTypEncd());// ������������
                    ChashMap.put("FormNum", checkSngl.getCheckFormNum());// ���ݺ�
                    ChashMap.put("isNtChk", checkSngl.getIsNtChk());// ���״̬
                    ChashMap.put("setupPers", checkSngl.getSetupPers());// ������
                    ChashMap.put("setupTm", checkSngl.getSetupTm());// ����ʱ��

                    list.add(ChashMap);
                    break;
                default:
                    isSuccess = false;
                    message = outIntoWhs.getOutIntoWhsTypId() + "�õ������Ͳ�����";
                    break;
            }
        } else if (formTypEncd.equals("028")) {
            // 028 �̵㵥
            CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(formNum);
            if (checkSngl == null) {
                isSuccess = true;
                message = message + formNum + "�������̵㵥�򵥾ݱ�ɾ��\n";
            } else if (checkSngl.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "�̵㵥δ���\n";
            } else {
                CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectSrcCheckSnglLoss(formNum);
                if (checkPrftLoss == null) {
                    isSuccess = true;
                    message = message + formNum + "�̵㵥������\n";
                } else {
                    HashMap<String, Object> BhashMap = new HashMap<String, Object>();
                    BhashMap.put("FormTypName", checkPrftLoss.getFormTypName());// �������ͱ���
                    BhashMap.put("FormTypEncd", checkPrftLoss.getFormTypEncd());// ������������
                    BhashMap.put("FormNum", checkPrftLoss.getCheckFormNum());// ���ݺ�
                    BhashMap.put("isNtChk", checkPrftLoss.getIsNtChk());// ���״̬
                    BhashMap.put("setupPers", checkPrftLoss.getSetupPers());// ������
                    BhashMap.put("setupTm", checkPrftLoss.getSetupTm());// ����ʱ��
                    list.add(BhashMap);
                    if (checkPrftLoss.getIsNtChk() == 0) {
                        isSuccess = true;
                        message = message + formNum + "�̵㵥�������δ���\n";
                    } else {
                        OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(checkPrftLoss.getCheckFormNum(), "8");
                        OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(checkPrftLoss.getCheckFormNum(), "7");

                        if (intoWhs == null) {
                            isSuccess = true;
                            message = message + formNum + "��������ⵥ\n";
                        } else {
                            HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                            intohashMap.put("FormTypName", intoWhs.getFormTypName());// �������ͱ���
                            intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// ������������
                            intohashMap.put("FormNum", intoWhs.getFormNum());// ���ݺ�
                            intohashMap.put("isNtChk", intoWhs.getIsNtChk());// ���״̬
                            intohashMap.put("setupPers", intoWhs.getSetupPers());// ������
                            intohashMap.put("setupTm", intoWhs.getSetupTm());// ����ʱ��
                            list.add(intohashMap);
                        }
                        if (outWhs == null) {
                            isSuccess = true;
                            message = message + formNum + "���������ⵥ\n";
                        } else {
                            HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                            outhashMap.put("FormTypName", outWhs.getFormTypName());// �������ͱ���
                            outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// ������������
                            outhashMap.put("FormNum", outWhs.getFormNum());// ���ݺ�
                            outhashMap.put("isNtChk", outWhs.getIsNtChk());// ���״̬
                            outhashMap.put("setupPers", outWhs.getSetupPers());// ������
                            outhashMap.put("setupTm", outWhs.getSetupTm());// ����ʱ��
                            list.add(outhashMap);
                        }

                    }
                }
            }

        } else if (formTypEncd.equals("029")) {
//			029	�̵����浥
//			7		�̿�����
//			8		��ӯ���
            CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(formNum);
            if (checkPrftLoss == null) {
                isSuccess = true;
                message = message + formNum + "�������̵����絥�򵥾ݱ�ɾ��\n";
//            } else if (checkPrftLoss.getIsNtChk() == 0) {
//                isSuccess = true;
//                message = message + formNum + "�̵㵥����δ���\n";
            } else {
                CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(checkPrftLoss.getSrcFormNum());

                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "8");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "7");
                if (checkSngl == null) {
                    isSuccess = true;
                    message = message + formNum + "���̵㵥\n";
                } else {
                    HashMap<String, Object> checkhashMap = new HashMap<String, Object>();
                    checkhashMap.put("FormTypName", checkSngl.getFormTypName());// �������ͱ���
                    checkhashMap.put("FormTypEncd", checkSngl.getFormTypEncd());// ������������
                    checkhashMap.put("FormNum", checkSngl.getCheckFormNum());// ���ݺ�
                    checkhashMap.put("isNtChk", checkSngl.getIsNtChk());// ���״̬
                    checkhashMap.put("setupPers", checkSngl.getSetupPers());// ������
                    checkhashMap.put("setupTm", checkSngl.getSetupTm());// ����ʱ��
                    list.add(checkhashMap);
                }

                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "��������ⵥ\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// �������ͱ���
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// ������������
                    intohashMap.put("FormNum", intoWhs.getFormNum());// ���ݺ�
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// ���״̬
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// ������
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// ����ʱ��
                    list.add(intohashMap);
                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "���������ⵥ\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// �������ͱ���
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// ������������
                    outhashMap.put("FormNum", outWhs.getFormNum());// ���ݺ�
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// ���״̬
                    outhashMap.put("setupPers", outWhs.getSetupPers());// ������
                    outhashMap.put("setupTm", outWhs.getSetupTm());// ����ʱ��
                    list.add(outhashMap);
                }
            }
        } else {
            isSuccess = false;
            message = message + formNum + "��Ӧ�������Ͳ�����\n";
        }
        try {
            resp = BaseJson.returnRespList("whs/out_into_whs/wholeSingleLianZha", isSuccess, message, list);
        } catch (IOException e) {


        }

        return resp;
    }

    @Override
    public String allInvtyGdsBitList(String formNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        Boolean flas = true;

        List<MovBitTab> movBitTabList = othOutIntoWhsMapper.selectallInvtyGdsBitListInto(formNum);
        if (movBitTabList.size() == 0) {
            throw new RuntimeException("����" + formNum + "�����ڻ�����˲���Ҫ�����λ");
        }
        List<MovBitTab> bitTabs = new ArrayList<>();
        MovBitTab bitTab;
        for (MovBitTab movBitTab : movBitTabList) {
//                bitListMapper
            //���Ϊ1 ����Ϊ0
            if (movBitTab.getId() == 1 && movBitTab.getQty().compareTo(BigDecimal.ZERO) < 0) {
                flas = false;
                //�������۳��� �븺��
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                for (MovBitTab mTab : mBitTab) {
                    bitTab = new MovBitTab();
                    bitTab.setWhsEncd(movBitTab.getWhsEncd());
                    bitTab.setInvtyEncd(movBitTab.getInvtyEncd());
                    bitTab.setBatNum(movBitTab.getBatNum());
                    bitTab.setRegnEncd(mTab.getRegnEncd());
                    bitTab.setGdsBitEncd(mTab.getGdsBitEncd());
                    bitTab.setOrderNum(movBitTab.getOrderNum());
                    bitTab.setSerialNum(movBitTab.getSerialNum());
                    bitTab.setPrdcDt(movBitTab.getPrdcDt());
                    if (mTab.getQty().compareTo(movBitTab.getQty()) == 1) {
                        // ��λ��-������
//                            BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // ������-��λ��
                        BigDecimal diff = movBitTab.getQty().subtract(mTab.getQty());
                        bitTab.setQty(mTab.getQty());
                        movBitTab.setQty(diff);
                        bitTabs.add(bitTab);
                    } else {
                        bitTab.setQty(mTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    }
                }
            } else if (movBitTab.getId() == 0 && movBitTab.getQty().compareTo(BigDecimal.ZERO) < 0) {
                flas = false;
                //�ɹ��˻� ������
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                movBitTab.setQty(movBitTab.getQty().abs());
                for (MovBitTab mTab : mBitTab) {
                    bitTab = new MovBitTab();
                    bitTab.setWhsEncd(mTab.getWhsEncd());
                    bitTab.setInvtyEncd(mTab.getInvtyEncd());
                    bitTab.setBatNum(mTab.getBatNum());
                    bitTab.setRegnEncd(mTab.getRegnEncd());
                    bitTab.setGdsBitEncd(mTab.getGdsBitEncd());
                    bitTab.setOrderNum(movBitTab.getOrderNum());
                    bitTab.setSerialNum(movBitTab.getSerialNum());
                    bitTab.setPrdcDt(movBitTab.getPrdcDt());
                    if (mTab.getQty().compareTo(movBitTab.getQty()) == 1) {
                        // ��λ��-������
                        BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // ������-��λ��
                        BigDecimal diff = movBitTab.getQty().subtract(mTab.getQty());
                        bitTab.setQty(mTab.getQty());
                        movBitTab.setQty(diff);
                        bitTabs.add(bitTab);
                    } else {
                        bitTab.setQty(mTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    }
                }
            } else if (movBitTab.getId() == 1) {
                //�������
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                for (MovBitTab mTab : mBitTab) {
                    bitTab = new MovBitTab();
                    bitTab.setWhsEncd(movBitTab.getWhsEncd());
                    bitTab.setInvtyEncd(movBitTab.getInvtyEncd());
                    bitTab.setBatNum(movBitTab.getBatNum());
                    bitTab.setRegnEncd(mTab.getRegnEncd());
                    bitTab.setGdsBitEncd(mTab.getGdsBitEncd());
                    bitTab.setOrderNum(movBitTab.getOrderNum());
                    bitTab.setSerialNum(movBitTab.getSerialNum());
                    bitTab.setPrdcDt(movBitTab.getPrdcDt());
                    if (mTab.getQty().compareTo(movBitTab.getQty()) == 1) {
                        // ��λ��-������
//                            BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // ������-��λ��
                        BigDecimal diff = movBitTab.getQty().subtract(mTab.getQty());
                        bitTab.setQty(mTab.getQty());
                        movBitTab.setQty(diff);
                        bitTabs.add(bitTab);
                    } else {
                        bitTab.setQty(mTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    }
                }

            } else if (movBitTab.getId() == 0) {
                //��������
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                for (MovBitTab mTab : mBitTab) {
                    bitTab = new MovBitTab();
                    bitTab.setWhsEncd(mTab.getWhsEncd());
                    bitTab.setInvtyEncd(mTab.getInvtyEncd());
                    bitTab.setBatNum(mTab.getBatNum());
                    bitTab.setRegnEncd(mTab.getRegnEncd());
                    bitTab.setGdsBitEncd(mTab.getGdsBitEncd());
                    bitTab.setOrderNum(movBitTab.getOrderNum());
                    bitTab.setSerialNum(movBitTab.getSerialNum());
                    bitTab.setPrdcDt(movBitTab.getPrdcDt());
                    if (mTab.getQty().compareTo(movBitTab.getQty()) == 1) {
                        // ��λ��-������
//                            BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // ������-��λ��
                        BigDecimal diff = movBitTab.getQty().subtract(mTab.getQty());
                        bitTab.setQty(mTab.getQty());
                        movBitTab.setQty(diff);
                        bitTabs.add(bitTab);
                    } else {
                        bitTab.setQty(mTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    }
                }
            } else {
                message = "�����쳣";
            }
            //����ת��
            if (!flas) {
                bitTabs.stream().forEach((tab) -> tab.setQty(BigDecimal.ZERO.subtract(tab.getQty())));
            }
        }
        bitListMapper.deleteInvtyGdsBitList(Arrays.asList(formNum));
        if (bitTabs.size() > 0) {
            bitListMapper.insertInvtyGdsBitLists(bitTabs);
            message = "����" + formNum + "�Զ������λ�ɹ�";
        } else {
            message = "����" + formNum + "�Զ������λʧ��";
        }

        return message;
    }

    @Override
    public String uploadFileAddDbU8(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, OthOutIntoWhs> pusOrderMap = uploadScoreInfoU8(file);

        for (Map.Entry<String, OthOutIntoWhs> entry : pusOrderMap.entrySet()) {

            OthOutIntoWhs oIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(entry.getValue().getFormNum());
            if (oIntoWhs != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");

            }
            try {
                othOutIntoWhsMapper.exInsertOthOutIntoWhs(entry.getValue());
                othOutIntoWhsMapper.exInsertOthOutIntoWhsSubTab(entry.getValue().getOutIntoSubList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "��������ⵥ�����ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDbU8", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // ����excle
    private Map<String, OthOutIntoWhs> uploadScoreInfoU8(MultipartFile file) {
        Map<String, OthOutIntoWhs> temp = new HashMap<>();
        int j = 1;
        try {
            InputStream fileIn = file.getInputStream();
            // ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
            Workbook wb0 = new HSSFWorkbook(fileIn);

            // ��ȡExcel�ĵ��еĵ�һ����
            Sheet sht0 = wb0.getSheetAt(0);
            // ��õ�ǰsheet�Ŀ�ʼ��
            int firstRowNum = sht0.getFirstRowNum();
            // ��ȡ�ļ������һ��
            int lastRowNum = sht0.getLastRowNum();
            // System.out.println(lastRowNum);
            // ���������ֶκ��±�ӳ��
            SetColIndex(sht0.getRow(firstRowNum));
            // ��Sheet�е�ÿһ�н��е���
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                // �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }
                // ����ʵ����
                OthOutIntoWhs othOutIntoWhs = new OthOutIntoWhs();

                String orderNo = null;
                String formDt = null;
                String outIntoWhsTypId = null;
                String formTypEncd = null;
                String outStatus = null;
                String inStatus = null;
//				11		��������
//				12		�������
//				014	������ⵥ
//				015	�������ⵥ

//				1		�������
//				10		���۳���
//				11		��������
//				12		�������
//				2		��������
//				3		��װ���
//				4		��װ����
//				5		��ж���
//				6		��ж����
//				7		�̿�����
//				8		��ӯ���
//				9		�ɹ����
                othOutIntoWhs.setSrcFormNum(GetCellData(r, "ҵ���") == null || GetCellData(r, "ҵ���").equals("") ? null
                        : GetCellData(r, "ҵ���"));
                if (GetCellData(r, "ҵ������").equals("�������") || GetCellData(r, "ҵ������").equals("�������")
                        || GetCellData(r, "ҵ������").equals("��ӯ���") || GetCellData(r, "ҵ������").equals("��װ���")
                        || GetCellData(r, "ҵ������").equals("��ж���")) {
                    orderNo = GetCellData(r, "��ⵥ��");
                    orderNo = "into" + orderNo;
                    othOutIntoWhs.setRecvSendCateId(GetCellData(r, "���������"));//�շ����id

                    formDt = GetCellData(r, "�������") == null ? "" : GetCellData(r, "�������").replaceAll("[^0-9:-]", " ");
                    if (GetCellData(r, "ҵ������").equals("�������")) {
                        outIntoWhsTypId = "12";
                    } else if (GetCellData(r, "ҵ������").equals("�������")) {
                        outIntoWhsTypId = "1";

                    } else if (GetCellData(r, "ҵ������").equals("��ӯ���")) {
                        outIntoWhsTypId = "8";

                    } else if (GetCellData(r, "ҵ������").equals("��װ���")) {
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "ҵ���") == null || GetCellData(r, "ҵ���").equals("") ? null
                                : "zc" + GetCellData(r, "ҵ���"));
                        outIntoWhsTypId = "3";

                    } else if (GetCellData(r, "ҵ������").equals("��ж���")) {
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "ҵ���") == null || GetCellData(r, "ҵ���").equals("") ? null
                                : "cz" + GetCellData(r, "ҵ���"));
                        outIntoWhsTypId = "5";

                    }

                    formTypEncd = "014";
                    inStatus = "������";

                } else if (GetCellData(r, "ҵ������").equals("��������") || GetCellData(r, "ҵ������").equals("��������")
                        || GetCellData(r, "ҵ������").equals("�̿�����") || GetCellData(r, "ҵ������").equals("��װ����")
                        || GetCellData(r, "ҵ������").equals("��ж����")) {
                    orderNo = GetCellData(r, "���ⵥ��");
                    orderNo = "out" + orderNo;
                    othOutIntoWhs.setRecvSendCateId(GetCellData(r, "����������"));//�շ����id

                    if (GetCellData(r, "ҵ������").equals("��������")) {
                        outIntoWhsTypId = "11";
                    } else if (GetCellData(r, "ҵ������").equals("��������")) {
                        outIntoWhsTypId = "2";

                    } else if (GetCellData(r, "ҵ������").equals("�̿�����")) {
                        outIntoWhsTypId = "7";

                    } else if (GetCellData(r, "ҵ������").equals("��װ����")) {
                        outIntoWhsTypId = "4";
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "ҵ���") == null || GetCellData(r, "ҵ���").equals("") ? null
                                : "zc" + GetCellData(r, "ҵ���"));

                    } else if (GetCellData(r, "ҵ������").equals("��ж����")) {
                        outIntoWhsTypId = "6";
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "ҵ���") == null || GetCellData(r, "ҵ���").equals("") ? null
                                : "cz" + GetCellData(r, "ҵ���"));

                    }
                    formDt = GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ");
                    formTypEncd = "015";
                    outStatus = "������";
                }

                // System.out.println(orderNo);

                if (temp.containsKey(orderNo)) {
                    othOutIntoWhs = temp.get(orderNo);
                }

                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                // r.getCell(6).getStringCellValue()
                othOutIntoWhs.setFormNum(orderNo); // ���� q varchar(200
                othOutIntoWhs.setFormDt(formDt); // ��������
                othOutIntoWhs.setWhsEncd(GetCellData(r, "�ֿ����")); // �ֿ���� q varchar(200
//                othOutIntoWhs.setSrcFormNum(null); // ��Դ���ݺ� q varchar(200

                othOutIntoWhs.setOutIntoWhsTypId(outIntoWhsTypId); // ����������� q ����ҵ�������ж� varchar(200
                othOutIntoWhs.setFormTypEncd(formTypEncd);// 014 ������ⵥ 015 �������ⵥ

                othOutIntoWhs.setOutStatus(outStatus); // ����״̬ ��������
                othOutIntoWhs.setInStatus(inStatus); // ���״̬ ��������
                othOutIntoWhs.setMemo(GetCellData(r, "��ע")); // ��ע
                othOutIntoWhs.setIsNtWms(0); // �Ƿ���WMS�ϴ�

                othOutIntoWhs.setIsNtChk(0); // �Ƿ���� int(11
                othOutIntoWhs.setIsNtBookEntry(0); // �Ƿ���� int(11
                othOutIntoWhs.setIsNtCmplt(0); // �Ƿ���� int(11
                othOutIntoWhs.setIsNtClos(0); // �Ƿ�ر� int(11
                othOutIntoWhs.setPrintCnt(
                        (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
                                : GetCellData(r, "��ӡ����"))).intValue()); // ��ӡ���� q int(11
                othOutIntoWhs.setSetupPers(GetCellData(r, "�Ƶ���")); // ������
                othOutIntoWhs.setSetupTm(
                        GetCellData(r, "�Ƶ�ʱ��") == null ? "" : GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " ")); // ����ʱ��
                // q
                // datetime
                othOutIntoWhs.setMdfr(GetCellData(r, "�޸���")); // �޸��� q varchar(200
                othOutIntoWhs.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ")); // �޸�ʱ��
                // q
                // datetime
                othOutIntoWhs.setChkr(GetCellData(r, "�����")); // ����� q varchar(200
                othOutIntoWhs.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " ")); // ���ʱ��
                // q
                // datetime
                othOutIntoWhs.setBookEntryPers(GetCellData(r, "������")); // ������ q varchar(200
                othOutIntoWhs.setBookEntryTm(null); // ����ʱ��
                // datetime
                othOutIntoWhs.setOperator(null); // ������ q varchar(200
                othOutIntoWhs.setOperatorId(null); // �����˱��� q varchar(200

                othOutIntoWhs.setIsNtMakeVouch(0); // �Ƿ�����ƾ֤ int(11
                othOutIntoWhs.setMakVouchPers(null); // ��ƾ֤��
                othOutIntoWhs.setMakVouchTm(null); // ��ƾ֤ʱ��

                List<OthOutIntoWhsSubTab> OthOutIntoWhsSub = othOutIntoWhs.getOutIntoSubList();// �����ӱ�
                if (OthOutIntoWhsSub == null) {
                    OthOutIntoWhsSub = new ArrayList<>();
                }

                OthOutIntoWhsSubTab othOutIntoWhsSubList = new OthOutIntoWhsSubTab();

//				othOutIntoWhsSubList.setOrdrNum(null); // ��� bigint(20
                othOutIntoWhsSubList.setFormNum(orderNo); // ���� varchar(200
                othOutIntoWhsSubList.setInvtyEncd(GetCellData(r, "�������")); // ������� 1 varchar(200
                othOutIntoWhsSubList.setQty(new BigDecimal(GetCellData(r, "����") == null ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ����
                othOutIntoWhsSubList.setTaxRate(BigDecimal.ZERO); // ˰��
                othOutIntoWhsSubList.setCntnTaxUprc(BigDecimal.ZERO); // ��˰����
                othOutIntoWhsSubList.setUnTaxUprc(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰����
                othOutIntoWhsSubList.setBookEntryUprc(BigDecimal.ZERO); // ���˵���
                othOutIntoWhsSubList.setCntnTaxAmt(BigDecimal.ZERO); // ��˰���
                othOutIntoWhsSubList.setUnTaxAmt(new BigDecimal(
                        GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? "0" : GetCellData(r, "���"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰���
                othOutIntoWhsSubList.setIntlBat(GetCellData(r, "��������")); // ��������
                othOutIntoWhsSubList.setBatNum(GetCellData(r, "����")); // ���� 1 varchar(200
                othOutIntoWhsSubList.setPrdcDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ")); // ��������

                BigDecimal bxRule = GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? null
                        : new BigDecimal(GetCellData(r, "���"));
                if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                    othOutIntoWhsSubList
                            .setBxQty(othOutIntoWhsSubList.getQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // ����
                } else {
                    othOutIntoWhsSubList.setBxQty(BigDecimal.ZERO);
                }

                String BaoZhiQi = GetCellData(r, "������");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }

                othOutIntoWhsSubList.setBaoZhiQi(BaoZhiQi); // ������ 1 varchar(200
                othOutIntoWhsSubList.setInvldtnDt(
                        GetCellData(r, "ʧЧ����") == null ? "" : GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " ")); // ʧЧ����
                // 1
                // datetime
                othOutIntoWhsSubList.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                OthOutIntoWhsSub.add(othOutIntoWhsSubList);

                othOutIntoWhs.setOutIntoSubList(OthOutIntoWhsSub);
                temp.put(orderNo, othOutIntoWhs);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());

        }
        return temp;
    }
}
