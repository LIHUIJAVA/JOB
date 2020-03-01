package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.AmbDisambSnglMapper;
import com.px.mis.whs.dao.CannibSnglMapper;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.entity.AmbDisambSngl;
import com.px.mis.whs.entity.AmbDisambSnglMap;
import com.px.mis.whs.entity.AmbDisambSnglubTab;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;
import com.px.mis.whs.service.AmbDisambSnglService;

@Service
@Transactional
public class AmbDisambSnglServiceImpl extends poiTool implements AmbDisambSnglService {

    @Autowired
    AmbDisambSnglMapper ambDisambSnglMapper;
    // ������
    @Autowired
    GetOrderNo getOrderNo;
    @Autowired
    InvtyNumMapper invtyNumMapper;

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;

    @Autowired
    CannibSnglMapper cannibSnglMapper;
    @Autowired
    InvtyDocDao invtyDocDao;
    @Autowired
    IntoWhsDao intoWhsDao;
    @Autowired
    InvtyTabDao invtyTabDao;
    // ��λ
    @Autowired
    InvtyGdsBitListMapper bitListMapper;

    // ��������װ��ж
    @Override
    public String insertAmbDisambSngl(String userId, AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList, String loginTime) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        String number = null;
        InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSngl.getMomKitEncd()))
                .orElseThrow(() -> new RuntimeException(aSngl.getMomKitEncd()+"�ô��������"));
        Integer pto = Optional.ofNullable(invty.getPto()).orElseThrow(() -> new RuntimeException("pto����Ϊ��"));
        Integer isQuaGuaPer = Optional.ofNullable(invty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
        if(isQuaGuaPer == 1){
            if(StringUtils.isBlank(aSngl.getBaoZhiQi())|| StringUtils.isBlank(aSngl.getMprdcDt())
                    || StringUtils.isBlank(aSngl.getInvldtnDt()) ){
                throw new RuntimeException(aSngl.getMomKitEncd()+"�����ڹ������������ô���");
            }
        }else{
            aSngl.setBaoZhiQi(null);
            aSngl.setMprdcDt(null);
            aSngl.setInvldtnDt(null);
        }

        if (pto == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "�ô������pto", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            number = getOrderNo.getSeqNo("ZC", userId, loginTime);
        } catch (Exception e) {

            throw new RuntimeException("���ɶ������! ʧ��");
        } // ��ȡ����
        Optional<String> opt = Optional.ofNullable(number);
//	    BaseJson.returnRespObj("whs/amb_disamb_sngl/insertASngl", isSuccess, message, aSngl);

        if (ambDisambSnglMapper.selectAmbDisambSngl(number) != null) {
            message = "���" + number + "�Ѵ��ڣ����������룡";
            isSuccess = false;
            try {
                resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/insertASngl", isSuccess, message, null);
            } catch (IOException e) {


            }
            return resp;
        }

        if (ambDisambSnglMapper.selectAmbDisambSnglubTabList(number).size() > 0) {
            ambDisambSnglMapper.deleteAmbDisambSnglubTab(number);
        }
        aSngl.setFormNum(number);// �����ȡ������
        for (AmbDisambSnglubTab aSnglubTab : aList) {
            aSnglubTab.setFormNum(number);// �����ݺ�
            aSnglubTab.setMomQty(aSngl.getMomQty());
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSnglubTab.getSubKitEncd()))
                    .orElseThrow(() -> new RuntimeException(aSnglubTab.getSubKitEncd()+"�ô��������"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(aSnglubTab.getBaoZhiQi())|| StringUtils.isBlank(aSnglubTab.getSprdcDt())
                        || StringUtils.isBlank(aSnglubTab.getSinvldtnDt()) ){
                    throw new RuntimeException(aSnglubTab.getSubKitEncd()+"�����ڹ������������ô���");
                }
            }else{
                aSnglubTab.setBaoZhiQi(null);
                aSnglubTab.setSprdcDt(null);
                aSnglubTab.setSinvldtnDt(null);
            }
        }
        /**
         * BigDecimal unTaxUprc1; // ��˰���� BigDecimal qty1; // ���� BigDecimal taxRate1; //
         * ˰�� String prdcDt1; // �������� String baoZhiQi1; // ������
         * aSngl.setFormNum(number);// ��ȡ������ // ��ȡδ˰���� unTaxUprc1 =
         * aSngl.getMunTaxUprc(); // ��ȡδ˰���� qty1 = aSngl.getMomQty(); // ��ȡ˰��
         * ҳ��˰��δ������������Ҫ��˰��/100 aSngl.setTaxRate(aSngl.getTaxRate().divide(new
         * BigDecimal(100))); taxRate1 = aSngl.getTaxRate(); // ��ȡ�������� prdcDt1 =
         * aSngl.getMprdcDt(); // ��ȡ������ baoZhiQi1 = aSngl.getBaoZhiQi(); // ����ʧЧ����
         * aSngl.setInvldtnDt(CalcAmt.getDate(prdcDt1, Integer.parseInt(baoZhiQi1))); //
         * ����δ˰��� ���=δ˰����*δ˰���� aSngl.setMunTaxAmt(CalcAmt.noTaxAmt(unTaxUprc1, qty1));
         * // ���㺬˰���� ��˰����=��˰����*˰��+��˰����
         * aSngl.setMcntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc1, qty1, taxRate1)); //
         * ���㺬˰��� ��˰���=��˰���*˰��+��˰���=˰��+��˰���
         * aSngl.setMcntnTaxAmt(CalcAmt.prcTaxSum(unTaxUprc1, qty1, taxRate1));
         *
         * BigDecimal unTaxUprc; // ��˰���� BigDecimal qty; // ���� BigDecimal taxRate; // ˰��
         * String prdcDt; // �������� String baoZhiQi; // ������ for (AmbDisambSnglubTab
         * aSnglubTab : aList) { aSnglubTab.setFormNum(aSngl.getFormNum());// �����ݺ� //
         * �Ӽ�����=ĸ������*ĸ�ӱ��� BigDecimal mQty = aSnglubTab.getMomQty(); BigDecimal ratio =
         * aSnglubTab.getMomSubRatio(); aSnglubTab.setSubQty(mQty.multiply(ratio)); //
         * ��ȡδ˰���� qty = aSnglubTab.getSubQty(); // ��ȡδ˰���� unTaxUprc =
         * aSnglubTab.getSunTaxUprc(); // ��ȡ˰�� ҳ��˰��δ������������Ҫ��˰��/100
         * aSnglubTab.setTaxRate(aSnglubTab.getTaxRate().divide(new BigDecimal(100)));
         * taxRate = aSnglubTab.getTaxRate(); // ��ȡ�������� prdcDt =
         * aSnglubTab.getSprdcDt(); // ��ȡ������ baoZhiQi = aSnglubTab.getBaoZhiQi(); //
         * ����ʧЧ���� aSnglubTab.setSinvldtnDt(CalcAmt.getDate(prdcDt,
         * Integer.parseInt(baoZhiQi))); // ����δ˰��� ���=δ˰����*δ˰����
         * aSnglubTab.setSunTaxAmt(CalcAmt.noTaxAmt(unTaxUprc, qty)); // ���㺬˰����
         * ��˰����=��˰����*˰��+��˰���� aSnglubTab.setScntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc,
         * qty, taxRate)); // ���㺬˰��� ��˰���=��˰���*˰��+��˰���=˰��+��˰���
         * aSnglubTab.setScntnTaxAmt(CalcAmt.prcTaxSum(unTaxUprc, qty, taxRate)); }
         */

        aSngl.setFormTypEncd(aSngl.getFormTyp().equals("��װ") ? "012" : "013");

        ambDisambSnglMapper.insertAmbDisambSngl(aSngl);
        ambDisambSnglMapper.insertAmbDisambSnglubTab(aList);
        for (AmbDisambSnglubTab aSnglubTab : aList) {
            aSnglubTab.setMomQty(aSngl.getMomQty());
        }
        message = "�����ɹ���";
        isSuccess = true;
        aSngl.setAmbSnglubList(aList);

        try {
            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/insertASngl", isSuccess, message, aSngl);
        } catch (IOException e) {


        }

        return resp;
    }

    // �޸���װ��ж
    @Override
    public String updateAmbDisambSngl(AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSngl.getMomKitEncd()))
                .orElseThrow(() -> new RuntimeException("�ô��������"));
        Integer pto = Optional.ofNullable(invty.getPto()).orElseThrow(() -> new RuntimeException("pto����Ϊ��"));
//		Integer bom = Optional.ofNullable(invty.getAllowBomMain()).orElseThrow(() -> new RuntimeException("bom����Ϊ��"));
        Integer isQuaGuaPer = Optional.ofNullable(invty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
        if(isQuaGuaPer == 1){
            if(StringUtils.isBlank(aSngl.getBaoZhiQi())|| StringUtils.isBlank(aSngl.getMprdcDt())
                    || StringUtils.isBlank(aSngl.getInvldtnDt()) ){
                throw new RuntimeException(aSngl.getMomKitEncd()+"�����ڹ������������ô���");
            }
        }else{
            aSngl.setBaoZhiQi(null);
            aSngl.setMprdcDt(null);
            aSngl.setInvldtnDt(null);
        }
        if (pto == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "�ô������pto", null);
            } catch (IOException e) {


            }
            return resp;
        }
        for (AmbDisambSnglubTab aSnglubTab : aList) {
            aSnglubTab.setMomQty(aSngl.getMomQty());
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSnglubTab.getSubKitEncd()))
                    .orElseThrow(() -> new RuntimeException(aSnglubTab.getSubKitEncd()+"�ô��������"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(aSnglubTab.getBaoZhiQi())|| StringUtils.isBlank(aSnglubTab.getSprdcDt())
                        || StringUtils.isBlank(aSnglubTab.getSinvldtnDt()) ){
                    throw new RuntimeException(aSnglubTab.getSubKitEncd()+"�����ڹ������������ô���");
                }
            }else{
                aSnglubTab.setBaoZhiQi(null);
                aSnglubTab.setSprdcDt(null);
                aSnglubTab.setSinvldtnDt(null);
            }
        }
        try {
            AmbDisambSngl amb = ambDisambSnglMapper.selectAmbDisambSngl(aSngl.getFormNum());
            if (amb.getIsNtChk() == null) {
                throw new RuntimeException("�������״̬�쳣");
            } else if (amb.getIsNtChk() == 1) {
                throw new RuntimeException("��������˲������޸�");
            }
            int i = ambDisambSnglMapper.deleteAmbDisambSnglubTab(aSngl.getFormNum());

            ambDisambSnglMapper.updateAmbDisambSngl(aSngl);
            ambDisambSnglMapper.insertAmbDisambSnglubTab(aList);
            message = "���³ɹ���";

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASngl", isSuccess, message, null);
        } catch (Exception e) {

            throw new RuntimeException("�޸���װ��ʧ��" + e.getMessage());

        }
        return resp;
    }

    // ����ɾ��
    @Override
    public String deleteAllAmbDisambSngl(String formNum) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<String> list = getList(formNum);
            List<AmbDisambSngl> ambDisambSngls = ambDisambSnglMapper.selectAmbDisambSnglList(list);
            List<String> lists = new ArrayList<String>();
            List<String> lists2 = new ArrayList<String>();
            if (ambDisambSngls.size() > 0) {
                for (AmbDisambSngl sngl : ambDisambSngls) {
                    if (sngl.getIsNtChk() == 0) {
                        lists.add(sngl.getFormNum());
                    } else {
                        lists2.add(sngl.getFormNum());
                    }
                }
                if (lists.size() > 0) {
                    ambDisambSnglMapper.insertAmbDisambSnglDl(lists);
                    ambDisambSnglMapper.insertAmbDisambSnglubTabDl(lists);

                    ambDisambSnglMapper.deleteAllAmbDisambSngl(lists);
                    message = "ɾ���ɹ���" + lists.toString();
                    isSuccess = true;
                } else if (lists2.size() > 0) {
                    isSuccess = false;
                    message = message + "\r�������" + lists2;
                }

            } else {
                isSuccess = false;
                message = "���" + formNum + "�����ڣ�";
            }

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/deleteAllAmbDisambSngl", isSuccess, message, null);
        } catch (Exception e) {

            throw new RuntimeException("ɾ�� ʧ��");
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
        }

        return list;
    }

    // ��ѯ����ҳ��
    @Override
    public String query(String formNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<AmbDisambSnglubTab> aSubTabList = new ArrayList<>();
            AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(formNum);
            if (aDisambSngl != null) {
                aSubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(formNum);
                message = "��ѯ�ɹ���";
            } else {
                isSuccess = false;
                message = "���" + formNum + "�����ڣ�";
            }

            resp = BaseJson.returnRespObjList("whs/amb_disamb_sngl/query", isSuccess, message, aDisambSngl,
                    aSubTabList);
        } catch (Exception e) {

            throw new RuntimeException("��ѯ ʧ��");

        }
        return resp;
    }

    @Override
    public String queryList(Map map) {
        String resp = "";
        List<String> formNum = getList((String) map.get("formNum"));
        List<String> formTypEncd = getList((String) map.get("formTypEncd"));
        List<String> whsEncd = getList((String) map.get("whsEncd"));
        List<String> typ = getList((String) map.get("typ"));
        List<String> formTyp = getList((String) map.get("formTyp"));
//        List<String> invtyClsEncd = getList((String) map.get("invtyClsEncd"));
        List<String> invtyEncd = getList((String) map.get("invtyEncd"));
        List<String> batNum = getList((String) map.get("batNum"));
        List<String> whsId = getList((String) map.get("whsId"));
        List<String> sbatNum = getList((String) map.get("sbatNum"));

//        map.put("invtyClsEncd", invtyClsEncd);
        map.put("invtyEncd", invtyEncd);
        map.put("batNum", batNum);
        map.put("formNum", formNum);
        map.put("formTypEncd", formTypEncd);
        map.put("whsEncd", whsEncd);
        map.put("typ", typ);
        map.put("formTyp", formTyp);
        map.put("whsId", whsId);
        map.put("sbatNum", sbatNum);


        List<AmbDisambSnglMap> aList = ambDisambSnglMapper.selectList(map);
//		int count = aList.size();
        int count = ambDisambSnglMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/amb_disamb_sngl/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {


        }
        return resp;
    }

    // ���
    @Override
    public String updateASnglChk(String userId, String jsonBody, String name, String formDate) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        ObjectNode dbzhu = null;
        String loginTime = formDate;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (Exception e1) {
            throw new RuntimeException("Json �����쳣");
        }
        try {
            String danHao = dbzhu.get("formNum").asText();
            List<AmbDisambSnglubTab> aSnglubTabList = new ArrayList<>();// ��װ��ж��
            OthOutIntoWhs othOutWhs = new OthOutIntoWhs();// ����������
            OthOutIntoWhs othIntoWhs = new OthOutIntoWhs();// ���������
            List<OthOutIntoWhsSubTab> othOutWhsSubTabs = new ArrayList<>();// ����������
            List<OthOutIntoWhsSubTab> othIntoWhsSubTabs = new ArrayList<>();// ���������

            // �ֿ���
            AmbDisambSngl ambDisambSngl = ambDisambSnglMapper.selectISChr(danHao);
            if (ambDisambSngl == null) {
                throw new RuntimeException("���ţ�" + danHao + " ������ ");
            }
            if (ambDisambSngl.getIsNtChk() == 1) {
                throw new RuntimeException("���ݣ�" + ambDisambSngl.getFormNum() + " �����,����Ҫ�ٴ���� ");
            }
            ambDisambSngl.setChkr(name);
            ambDisambSngl.setChkTm(loginTime);
            // �������״̬

            int a = ambDisambSnglMapper.updateASnglChk(Arrays.asList(ambDisambSngl));
            if (a < 1) {
                isSuccess = false;
                throw new RuntimeException("���ݣ�" + danHao + "����쳣");
            }
            message = ambDisambSngl.getFormNum() + "������˳ɹ�";

            if (ambDisambSngl.getFormTyp().equals("��װ")) {

                // �����===�������������====
                String numberz = null;
                try {
                    numberz = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {
                    throw new RuntimeException("���Ż�ȡʧ��");
                } // ��ȡ������
                othOutWhs.setFormNum(numberz);// ������
                othOutWhs.setFormDt(formDate);
                othOutWhs.setOutIntoWhsTypId("4");// ��װ�������
                othOutWhs.setOutStatus("������");// ���״̬
                othOutWhs.setSetupPers(name);// ������
                othOutWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// �ֿ����
                othOutWhs.setSrcFormNum(danHao);// ��Դ���ݺ�
                othOutWhs.setMemo(ambDisambSngl.getMemo());
                othOutWhs.setFormTypEncd("015");
                int oth = othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);

                // ��װ��ж���ӱ���Ϣ �ֿ�
                aSnglubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(danHao);
                if (aSnglubTabList.size() == 0) {
                    throw new RuntimeException("���ݣ�" + danHao + "û�б�����Ϣ�����ʧ��");
                }
                isSuccess = true;
                for (AmbDisambSnglubTab tab : aSnglubTabList) {
                    InvtyTab inTabZi = new InvtyTab();
                    inTabZi.setWhsEncd(tab.getWhsEncd());
                    inTabZi.setInvtyEncd(tab.getSubKitEncd());
                    inTabZi.setBatNum(tab.getSbatNum());
                    inTabZi.setAvalQty(tab.getSubQty());
                    InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(inTabZi);
                    Optional.ofNullable(inTab).orElseThrow(() -> new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + tab.getWhsEncd() + ",�����"
                            + tab.getSubKitEncd() + ",���ţ�" + tab.getSbatNum() + "��Ӧ�Ŀ�治����, ���ʧ�ܣ�"));

                    if (inTab.getAvalQty().compareTo(inTabZi.getAvalQty()) == 1 || inTab.getAvalQty().compareTo(inTabZi.getAvalQty()) == 0) {

                        invtyNumMapper.updateAInvtyTab(Arrays.asList(inTabZi));// �޸Ŀ�������� ����������
                        OthOutIntoWhsSubTab othIntoWhsSubTab = new OthOutIntoWhsSubTab();
                        othIntoWhsSubTab.setFormNum(numberz);
                        othIntoWhsSubTab.setInvtyEncd(tab.getSubKitEncd());
                        othIntoWhsSubTab.setQty(tab.getSubQty());
                        othIntoWhsSubTab.setBxQty(tab.getSbxQty());
                        othIntoWhsSubTab.setTaxRate(tab.getTaxRate());
                        othIntoWhsSubTab.setCntnTaxUprc(tab.getScntnTaxUprc());
                        othIntoWhsSubTab.setUnTaxUprc(tab.getSunTaxUprc());
                        othIntoWhsSubTab.setCntnTaxAmt(tab.getScntnTaxAmt());
                        othIntoWhsSubTab.setUnTaxAmt(tab.getSunTaxAmt());
                        othIntoWhsSubTab.setBatNum(tab.getSbatNum());
                        othIntoWhsSubTab.setPrdcDt(tab.getSprdcDt());
                        othIntoWhsSubTab.setBaoZhiQi(tab.getBaoZhiQi());
                        othIntoWhsSubTab.setInvldtnDt(tab.getSinvldtnDt());
                        othIntoWhsSubTab.setProjEncd(tab.getProjEncd());
                        othIntoWhsSubTab.setTaxAmt(tab.getStaxAmt());

                        othOutWhsSubTabs.add(othIntoWhsSubTab);

                    } else {
                        throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + tab.getWhsEncd() + ",�����"
                                + tab.getSubKitEncd() + ",���ţ�" + tab.getSbatNum() + "��Ӧ�Ŀ����������, ���ʧ�ܣ�");
                    }
                }
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);// ��������zhu�ӱ�
                // �����===�������������====
                String number = null;
                try {
                    number = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("���Ż�ȡʧ��");
                } // ��ȡ������
                othIntoWhs.setFormNum(number);// ������
                othIntoWhs.setFormDt(formDate);
                othIntoWhs.setOutIntoWhsTypId("3");// ��װ��������
                othIntoWhs.setInStatus("������");// ����״̬
                othIntoWhs.setSetupPers(name);// ������
                othIntoWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// �ֿ����
                othIntoWhs.setSrcFormNum(danHao);// ��Դ���ݺ�
                othIntoWhs.setMemo(ambDisambSngl.getMemo());// ��ע
                othIntoWhs.setFormTypEncd("014");

                othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);// �������������

                OthOutIntoWhsSubTab othIntoWhsSubTab = new OthOutIntoWhsSubTab();

                othIntoWhsSubTab.setFormNum(number);
                othIntoWhsSubTab.setInvtyEncd(ambDisambSngl.getMomKitEncd());
                othIntoWhsSubTab.setQty(ambDisambSngl.getMomQty());
                othIntoWhsSubTab.setBxQty(ambDisambSngl.getBxQty());
                othIntoWhsSubTab.setTaxRate(ambDisambSngl.getTaxRate());
                othIntoWhsSubTab.setCntnTaxUprc(ambDisambSngl.getMcntnTaxUprc());
                othIntoWhsSubTab.setUnTaxUprc(ambDisambSngl.getMunTaxUprc());
                othIntoWhsSubTab.setCntnTaxAmt(ambDisambSngl.getMcntnTaxAmt());
                othIntoWhsSubTab.setUnTaxAmt(ambDisambSngl.getMunTaxAmt());
                othIntoWhsSubTab.setBatNum(ambDisambSngl.getMbatNum());
                othIntoWhsSubTab.setPrdcDt(ambDisambSngl.getMprdcDt());
                othIntoWhsSubTab.setBaoZhiQi(ambDisambSngl.getBaoZhiQi());
                othIntoWhsSubTab.setInvldtnDt(ambDisambSngl.getInvldtnDt());
				othIntoWhsSubTab.setProjEncd(ambDisambSngl.getMprojEncd());
                othIntoWhsSubTab.setTaxAmt(ambDisambSngl.getMtaxAmt());

                othIntoWhsSubTabs.add(othIntoWhsSubTab);
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);// �����������ӱ�

            } else if (ambDisambSngl.getFormTyp().equals("��ж")) {

                othOutWhs = new OthOutIntoWhs();// ������
                othIntoWhs = new OthOutIntoWhs();// �����
                othOutWhsSubTabs = new ArrayList<>();// ������
                othIntoWhsSubTabs = new ArrayList<>();// �����

                // �������������====�� ������ �� ������
                String number = null;
                try {
                    number = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("���Ż�ȡʧ��");
                } // ��ȡ������

                othOutWhs.setFormNum(number);// ������
                othOutWhs.setFormDt(formDate);
                othOutWhs.setOutIntoWhsTypId("6");// ��װ�������
                othOutWhs.setOutStatus("������");// ���״̬
                othOutWhs.setSetupPers(name);// ������
                othOutWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// �ֿ����
                othOutWhs.setSrcFormNum(danHao);// ��Դ���ݺ�
                othOutWhs.setFormTypEncd("015");

                // �������������� ��
                othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);

                // �������������====�� ������ �� ������
                String numberz = null;
                try {
                    numberz = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("���Ż�ȡʧ��");
                } // ��ȡ������

                othIntoWhs.setFormNum(numberz);// ������

                othIntoWhs.setFormDt(formDate);
                othIntoWhs.setOutIntoWhsTypId("5");// ��װ�������
                othIntoWhs.setInStatus("������");// ���״̬
                othIntoWhs.setSetupPers(name);// ������
                othIntoWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// �ֿ����
                othIntoWhs.setSrcFormNum(danHao);// ��Դ���ݺ�
                othIntoWhs.setFormTypEncd("014");

                // ������������� ��
                othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);

                InvtyTab inTabZi = new InvtyTab();
                inTabZi.setWhsEncd(ambDisambSngl.getWhsEncd());
                inTabZi.setInvtyEncd(ambDisambSngl.getMomKitEncd());
                inTabZi.setBatNum(ambDisambSngl.getMbatNum());
                inTabZi.setAvalQty(ambDisambSngl.getMomQty());
                InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(inTabZi);

                Optional.ofNullable(inTab).orElseThrow(() -> new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + ambDisambSngl.getWhsEncd()
                        + ",�����" + ambDisambSngl.getMomKitEncd() + ",���ţ�" + ambDisambSngl.getMbatNum() + "��Ӧ�Ŀ�治����, ���ʧ�ܣ�"));
                if (inTab.getAvalQty().compareTo(ambDisambSngl.getMomQty()) == 1 || inTab.getAvalQty().compareTo(ambDisambSngl.getMomQty()) == 0) {
                    invtyNumMapper.updateAInvtyTab(Arrays.asList(inTabZi));// �޸Ŀ�������� ���������� ��
                    isSuccess = true;
                } else if (inTab.getAvalQty().compareTo(ambDisambSngl.getMomQty()) == -1) {
                    throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + ambDisambSngl.getWhsEncd() + ",�����"
                            + ambDisambSngl.getMomKitEncd() + ",���ţ�" + ambDisambSngl.getMbatNum() + "��Ӧ�Ŀ����������, ���ʧ�ܣ�");
                }
                // ����� �ӱ�
                OthOutIntoWhsSubTab intoWhsSubTab = new OthOutIntoWhsSubTab();
                intoWhsSubTab.setFormNum(number);
                intoWhsSubTab.setInvtyEncd(ambDisambSngl.getMomKitEncd());
                intoWhsSubTab.setQty(ambDisambSngl.getMomQty());
                intoWhsSubTab.setTaxRate(ambDisambSngl.getTaxRate());
                intoWhsSubTab.setCntnTaxUprc(ambDisambSngl.getMcntnTaxUprc());
                intoWhsSubTab.setUnTaxUprc(ambDisambSngl.getMunTaxUprc());
                intoWhsSubTab.setCntnTaxAmt(ambDisambSngl.getMcntnTaxAmt());
                intoWhsSubTab.setUnTaxAmt(ambDisambSngl.getMunTaxAmt());
                intoWhsSubTab.setBxQty(ambDisambSngl.getBxQty());

                intoWhsSubTab.setBatNum(ambDisambSngl.getMbatNum());
                intoWhsSubTab.setPrdcDt(ambDisambSngl.getMprdcDt());
                intoWhsSubTab.setBaoZhiQi(ambDisambSngl.getBaoZhiQi());
                intoWhsSubTab.setInvldtnDt(ambDisambSngl.getInvldtnDt());
				intoWhsSubTab.setProjEncd(ambDisambSngl.getMprojEncd());
                intoWhsSubTab.setTaxAmt(ambDisambSngl.getMtaxAmt());

                othOutWhsSubTabs.add(intoWhsSubTab);

                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);// �����������ӱ�
                aSnglubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(danHao);
                if (aSnglubTabList.size() == 0) {
                    throw new RuntimeException("���ݣ�" + danHao + "û�б�����Ϣ�����ʧ��");

                }
                for (AmbDisambSnglubTab dbzi : aSnglubTabList) {

//					// �������������ӱ�
                    OthOutIntoWhsSubTab othOutWhsSubTab = new OthOutIntoWhsSubTab();

                    othOutWhsSubTab.setFormNum(numberz);
                    othOutWhsSubTab.setInvtyEncd(dbzi.getSubKitEncd());
                    othOutWhsSubTab.setQty(dbzi.getSubQty());
                    othOutWhsSubTab.setBxQty(dbzi.getSbxQty());
                    othOutWhsSubTab.setTaxRate(dbzi.getTaxRate());
                    othOutWhsSubTab.setCntnTaxUprc(dbzi.getScntnTaxUprc());
                    othOutWhsSubTab.setUnTaxUprc(dbzi.getSunTaxUprc());
                    othOutWhsSubTab.setCntnTaxAmt(dbzi.getScntnTaxAmt());
                    othOutWhsSubTab.setUnTaxAmt(dbzi.getSunTaxAmt());
                    othOutWhsSubTab.setBatNum(dbzi.getSbatNum());
                    othOutWhsSubTab.setPrdcDt(dbzi.getSprdcDt());
                    othOutWhsSubTab.setBaoZhiQi(dbzi.getBaoZhiQi());
                    othOutWhsSubTab.setInvldtnDt(dbzi.getSinvldtnDt());
                    othOutWhsSubTab.setProjEncd(dbzi.getProjEncd());
                    intoWhsSubTab.setTaxAmt(dbzi.getStaxAmt());

                    othIntoWhsSubTabs.add(othOutWhsSubTab);
                }

                // �������������ӱ�
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);

            }else {
                throw new RuntimeException("���ݣ�" + danHao + "�����쳣");
            	}

        } catch (Exception e) {


            throw new RuntimeException(e.getMessage());
        }

        return message;
    }

    // ����
    @Override
    public String updateASnglNoChk(String userId, String jsonBody, String name) {
        String message = "";

        ObjectNode dbzhu = null;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }

        List<AmbDisambSngl> aList = new ArrayList<>();// ��װ��ж��
        List<AmbDisambSnglubTab> aSnglubTabList = new ArrayList<>();// ��װ��ж��
        aList = new ArrayList<>();// ��װ��ж��
        aSnglubTabList = new ArrayList<>();// ��װ��ж��

        String danHao = dbzhu.get("formNum").asText();
        AmbDisambSngl ambDisambSngl = ambDisambSnglMapper.selectISChr(danHao);
        if (ambDisambSngl == null) {
            throw new RuntimeException("���ţ�" + danHao + " ������ ");
        }
        if (ambDisambSngl.getIsNtChk() == 0) {
            throw new RuntimeException("����:" + danHao + "������,����Ҫ�ظ����� ");
        }
        ambDisambSngl.setChkr(name);
        OthOutIntoWhs   intoWhs = null;
        OthOutIntoWhs   outWhs = null;
        // ��ѯ�Ƿ������������
        String srcFromNum = ambDisambSngl.getFormNum();
        if("��װ".equals(ambDisambSngl.getFormTyp())) {
        	intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "3");
        	outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "4");
        }else if("��ж".equals(ambDisambSngl.getFormTyp())) {
          intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "5");
          outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "6");
        }else {
            throw new RuntimeException("���ݣ�" + danHao + "�����쳣");
        	}
        if (intoWhs == null || outWhs == null ) {
            throw new RuntimeException("���ţ�" + danHao + "��Ӧ���������������쳣");
            }
        
        if (intoWhs.getIsNtChk() == 1 || outWhs.getIsNtChk() == 1) {
            throw new RuntimeException("���ţ�" + danHao + "��Ӧ����������������");
        } else {
            //ɾ���������������ݣ������������
            List<String> lists = Arrays.asList(intoWhs.getFormNum(), outWhs.getFormNum());
            othOutIntoWhsMapper.insertOthOutIntoWhsDl(lists);
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTabDl(lists);
            othOutIntoWhsMapper.deleteAllOthOutIntoWhs(lists);
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

                int a = ambDisambSnglMapper.updateASnglNoChk(Arrays.asList(ambDisambSngl));
                if (a == 1) {
                    message = ambDisambSngl.getFormNum() + "��������ɹ�";

                    aSnglubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(srcFromNum);
                    if (aSnglubTabList.size() == 0) {
                        throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");
                    }
                    // �ж�����װ�����ǲ�ж�����д����߼��ж�
                    if (ambDisambSngl.getFormTyp().equals("��װ")) {

                        for (AmbDisambSnglubTab dbzi : aSnglubTabList) {
                            InvtyTab inTabZi = new InvtyTab();
                            inTabZi.setWhsEncd(dbzi.getWhsEncd());// �ֿ����
                            inTabZi.setInvtyEncd(dbzi.getSubKitEncd());// �������
                            inTabZi.setBatNum(dbzi.getSbatNum());// ����
//							inTabZi.setNowStok(BigDecimal.ZERO);// �ִ���
                            inTabZi.setAvalQty(dbzi.getSubQty());// ������
                            inTabZi.setPrdcDt(dbzi.getSprdcDt());// ��������
                            inTabZi.setBaoZhiQi(dbzi.getBaoZhiQi());// ������
                            inTabZi.setInvldtnDt(dbzi.getSinvldtnDt());// ʧЧ����
                            inTabZi.setTaxRate(dbzi.getTaxRate());// ˰��

                            // �޸Ŀ����������
                            if (invtyNumMapper.selectInvtyTabByTerm(inTabZi) != null) {
                                // ���������� �����ڿ� �޸Ŀ�������
                                invtyNumMapper.updateAInvtyTabJia(Arrays.asList(inTabZi));// �޸Ŀ�������� ����������
                            } else {
                                // �������� ���
                                invtyNumMapper.insertInvtyTabList(Arrays.asList(inTabZi));
                            }

                        }

                    } else if (ambDisambSngl.getFormTyp().equals("��ж")) {// true ��˳ɹ�

                        // ����
                        InvtyTab invtyTabZhu = new InvtyTab();
                        invtyTabZhu.setWhsEncd(ambDisambSngl.getWhsEncd());
                        invtyTabZhu.setInvtyEncd(ambDisambSngl.getMomKitEncd());// �������
                        invtyTabZhu.setBatNum(ambDisambSngl.getMbatNum());// ����
//						invtyTabZhu.setNowStok(BigDecimal.ZERO);// �ִ���
                        invtyTabZhu.setTaxRate(ambDisambSngl.getTaxRate());// ˰��
                        invtyTabZhu.setAvalQty(ambDisambSngl.getMomQty());// ������

                        invtyTabZhu.setPrdcDt(ambDisambSngl.getMprdcDt());
                        invtyTabZhu.setBaoZhiQi(ambDisambSngl.getBaoZhiQi());
                        invtyTabZhu.setInvldtnDt(ambDisambSngl.getInvldtnDt());
                        invtyTabZhu.setTaxRate(ambDisambSngl.getTaxRate());// ˰��

                        if (invtyNumMapper.selectInvtyTabByTerm(invtyTabZhu) != null) {
                            // ���������� �����ڿ� �޸Ŀ�������
                            invtyNumMapper.updateAInvtyTabJia(Arrays.asList(invtyTabZhu));// �޸Ŀ�������� ����������
                        } else {
                            // �������� ���
                            invtyNumMapper.insertInvtyTabList(Arrays.asList(invtyTabZhu));
                        }
                    }

                } else {
                    throw new RuntimeException("���ݣ�" + danHao + "��������ʧ�ܣ�");

                }

        return message;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
        List<String> formNum = getList((String) map.get("formNum"));
        List<String> formTypEncd = getList((String) map.get("formTypEncd"));
        List<String> whsEncd = getList((String) map.get("whsEncd"));
        List<String> typ = getList((String) map.get("typ"));
        List<String> formTyp = getList((String) map.get("formTyp"));
        List<String> invtyEncd = getList((String) map.get("invtyEncd"));
        List<String> batNum = getList((String) map.get("batNum"));
//        List<String> invtyClsEncd = getList((String) map.get("invtyClsEncd"));
        List<String> whsId = getList((String) map.get("whsId"));
        List<String> sbatNum = getList((String) map.get("sbatNum"));

        map.put("invtyEncd", invtyEncd);
        map.put("batNum", batNum);
//        map.put("invtyClsEncd", invtyClsEncd);
        map.put("sbatNum", sbatNum);
        map.put("formNum", formNum);
        map.put("formTypEncd", formTypEncd);
        map.put("whsEncd", whsEncd);
        map.put("typ", typ);
        map.put("formTyp", formTyp);
        map.put("whsId", whsId);

//		List<AmbDisambSngl> aList = ambDisambSnglMapper.selectListDaYin(map);
        List<AmbDisambSnglMap> aList = ambDisambSnglMapper.selectList(map);
        aList.add(new AmbDisambSnglMap());
        try {

            resp = BaseJson.returnRespObjListAnno("whs/amb_disamb_sngl/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
//			resp = BaseJson.returnRespObjList("whs/amb_disamb_sngl/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, AmbDisambSngl> pusOrderMap = uploadScoreInfo(file);
        // System.out.println(pusOrderMap.size());

        for (Map.Entry<String, AmbDisambSngl> entry : pusOrderMap.entrySet()) {
            // System.out.println(entry.getValue().getFormTyp());

            AmbDisambSngl ambDisambSngl = ambDisambSnglMapper.selectISChr(entry.getValue().getFormNum());

            if (ambDisambSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");
            }
            try {
                ambDisambSnglMapper.exInsertAmbDisambSngl(entry.getValue());
                ambDisambSnglMapper.exInsertAmbDisambSnglubTab(entry.getValue().getAmbSnglubList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");

            }
        }

        isSuccess = true;
        message = "��װ��ж�������ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // ����excle
    private Map<String, AmbDisambSngl> uploadScoreInfo(MultipartFile file) {
        Map<String, AmbDisambSngl> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "���ݺ�");
                // ����ʵ����
                // System.out.println(orderNo);
                AmbDisambSngl ambDisambSngl = new AmbDisambSngl();
                if (temp.containsKey(orderNo)) {
                    ambDisambSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                List<AmbDisambSnglubTab> ambDisambSnglubList = ambDisambSngl.getAmbSnglubList();// �����ӱ�
                if (ambDisambSnglubList == null) {
                    ambDisambSnglubList = new ArrayList<>();
                }

                ambDisambSngl.setMomKitEncd(GetCellData(r, "ĸ������")); // varchar(200 'ĸ���׼�����',1
                ambDisambSngl.setFormNum(orderNo); // varchar(200 '���ݺ�', 1
                ambDisambSngl.setFormDt(GetCellData(r, "��������")); // datetime '��������',1
                ambDisambSngl.setFormTyp(GetCellData(r, "��������(��װ����ж)")); // varchar(200 '��������(��װ����ж', 1
                ambDisambSngl.setFormTypEncd(GetCellData(r, "�������ͱ���"));// �������ͱ���

                ambDisambSngl.setFee(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,0 '����',1
                ambDisambSngl.setFeeComnt(GetCellData(r, "����˵��")); // varchar(2000 '����˵��',
                ambDisambSngl.setMemo(GetCellData(r, "��ע")); // varchar(2000 '��ע',1
                ambDisambSngl.setTyp(GetCellData(r, "����(�׼���ɢ��)")); // varchar(200 '����(�׼���ɢ��',
                ambDisambSngl.setWhsEncd(GetCellData(r, "ĸ���ֿ����")); // varchar(200 '�ֿ����', 1
                ambDisambSngl.setMomQty(
                        new BigDecimal(GetCellData(r, "ĸ������") == null || GetCellData(r, "ĸ������").equals("") ? "0"
                                : GetCellData(r, "ĸ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // 'ĸ������', 1
                ambDisambSngl.setTaxRate(new BigDecimal(
                        GetCellData(r, "ĸ��˰��") == null || GetCellData(r, "ĸ��˰��").equals("") ? "0" : GetCellData(r, "ĸ��˰��"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 '˰��',
                ambDisambSngl.setMcntnTaxUprc(
                        new BigDecimal(GetCellData(r, "ĸ����˰����") == null || GetCellData(r, "ĸ����˰����").equals("") ? "0"
                                : GetCellData(r, "ĸ����˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '��˰����', 1
                ambDisambSngl.setMunTaxUprc(
                        new BigDecimal(GetCellData(r, "ĸ��δ˰����") == null || GetCellData(r, "ĸ��δ˰����").equals("") ? "0"
                                : GetCellData(r, "ĸ��δ˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // 'δ˰����',
                ambDisambSngl.setMcntnTaxAmt(
                        new BigDecimal(GetCellData(r, "ĸ����˰���") == null || GetCellData(r, "ĸ����˰���").equals("") ? "0"
                                : GetCellData(r, "ĸ����˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '��˰���',
                ambDisambSngl.setMunTaxAmt(
                        new BigDecimal(GetCellData(r, "ĸ��δ˰���") == null || GetCellData(r, "ĸ��δ˰���").equals("") ? "0"
                                : GetCellData(r, "ĸ��δ˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // 'δ˰���', 1
                ambDisambSngl.setMbatNum(GetCellData(r, "ĸ������")); // varchar(200 '����', 1
                ambDisambSngl.setMprdcDt(
                        GetCellData(r, "ĸ����������") == null ? "" : GetCellData(r, "ĸ����������")); // datetime
                // '��������',
                ambDisambSngl.setBaoZhiQi(GetCellData(r, "ĸ��������")); // varchar(200 '������',
                ambDisambSngl.setInvldtnDt(
                        GetCellData(r, "ĸ��ʧЧ����") == null ? "" : GetCellData(r, "ĸ��ʧЧ����")); // datetime
                // 'ʧЧ����',
//					ambDisambSngl.setIsNtWms((new Double(GetCellData(r, "�Ƿ���WMS�ϴ�"))).intValue()); // int(11 '�Ƿ���WMS�ϴ�',
                ambDisambSngl.setIsNtChk(
                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����"))).intValue()); // int(11 '�Ƿ����',
                ambDisambSngl.setIsNtBookEntry(
                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����"))).intValue()); // int(11 '�Ƿ����',
//                ambDisambSngl.setIsNtCmplt(
//                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ����"))).intValue()); // int(11 '�Ƿ����',
//                ambDisambSngl.setIsNtClos(
//                        (new Double(GetCellData(r, "�Ƿ�ر�") == null || GetCellData(r, "�Ƿ�ر�").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ�ر�"))).intValue()); // int(11 '�Ƿ�ر�',
//                ambDisambSngl.setPrintCnt(
//                        (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
//                                : GetCellData(r, "��ӡ����"))).intValue()); // int(11 '��ӡ����',
                ambDisambSngl.setSetupPers(GetCellData(r, "������")); // varchar(200 '������',
                ambDisambSngl.setSetupTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // datetime
                // '����ʱ��',
                ambDisambSngl.setMdfr(GetCellData(r, "�޸���")); // varchar(200 '�޸���',
                ambDisambSngl.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��")); // datetime
                // '�޸�ʱ��',
                ambDisambSngl.setChkr(GetCellData(r, "�����")); // varchar(200 '�����',
                ambDisambSngl.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��")); // datetime
                // '���ʱ��',
                ambDisambSngl.setBookEntryPers(GetCellData(r, "������")); // varchar(200 '������',
                ambDisambSngl.setBookEntryTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // datetime
                ambDisambSngl.setBxQty(new BigDecimal(
                        GetCellData(r, "ĸ������") == null || GetCellData(r, "ĸ������").equals("") ? "0" : GetCellData(r, "ĸ������"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ���� a decimal(208 // //
                // '����ʱ��',

                AmbDisambSnglubTab AmbDisambSnglubTab = new AmbDisambSnglubTab();

//					AmbDisambSnglubTab.setOrdrNum(ordrNum); // bigint(20 '���',
                AmbDisambSnglubTab.setFormNum(orderNo); // varchar(200 // '�����ݺ�',
                AmbDisambSnglubTab.setSubKitEncd(GetCellData(r, "�Ӽ�����")); // varchar(200 // '�����׼�����',
                AmbDisambSnglubTab.setWhsEncd(GetCellData(r, "�Ӽ��ֿ����")); // varchar(200 // '�ֿ����',
                AmbDisambSnglubTab.setSubQty(
                        new BigDecimal(GetCellData(r, "�Ӽ�����") == null || GetCellData(r, "�Ӽ�����").equals("") ? "0"
                                : GetCellData(r, "�Ӽ�����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '�Ӽ�����',
                AmbDisambSnglubTab.setMomQty(
                        new BigDecimal(GetCellData(r, "ĸ������") == null || GetCellData(r, "ĸ������").equals("") ? "0"
                                : GetCellData(r, "ĸ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // 'ĸ������',
                AmbDisambSnglubTab.setMomSubRatio(
                        new BigDecimal(GetCellData(r, "ĸ�ӱ���") == null || GetCellData(r, "ĸ�ӱ���").equals("") ? "0"
                                : GetCellData(r, "ĸ�ӱ���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // 'ĸ�ӱ���',
                AmbDisambSnglubTab.setTaxRate(new BigDecimal(
                        GetCellData(r, "�Ӽ�˰��") == null || GetCellData(r, "�Ӽ�˰��").equals("") ? "0" : GetCellData(r, "�Ӽ�˰��"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 // '˰��',
                AmbDisambSnglubTab.setScntnTaxUprc(
                        new BigDecimal(GetCellData(r, "�Ӽ���˰����") == null || GetCellData(r, "�Ӽ���˰����").equals("") ? "0"
                                : GetCellData(r, "�Ӽ���˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '��˰����',
                AmbDisambSnglubTab.setSunTaxUprc(
                        new BigDecimal(GetCellData(r, "�Ӽ�δ˰����") == null || GetCellData(r, "�Ӽ�δ˰����").equals("") ? "0"
                                : GetCellData(r, "�Ӽ�δ˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // 'δ˰����',
                AmbDisambSnglubTab.setScntnTaxAmt(
                        new BigDecimal(GetCellData(r, "�Ӽ���˰���") == null || GetCellData(r, "�Ӽ���˰���").equals("") ? "0"
                                : GetCellData(r, "�Ӽ���˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '��˰���',
                AmbDisambSnglubTab.setSunTaxAmt(
                        new BigDecimal(GetCellData(r, "�Ӽ�δ˰���") == null || GetCellData(r, "�Ӽ�δ˰���").equals("") ? "0"
                                : GetCellData(r, "�Ӽ�δ˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // 'δ˰���',
                AmbDisambSnglubTab.setSbatNum(GetCellData(r, "�Ӽ�����")); // varchar(200 // '����',
                AmbDisambSnglubTab.setSprdcDt(
                        GetCellData(r, "�Ӽ���������") == null ? "" : GetCellData(r, "�Ӽ���������")); // datetime
                // //
                // '��������',
                AmbDisambSnglubTab.setBaoZhiQi(GetCellData(r, "�Ӽ�������")); // varchar(200 // '������',
                AmbDisambSnglubTab.setSinvldtnDt(
                        GetCellData(r, "�Ӽ�ʧЧ����") == null ? "" : GetCellData(r, "�Ӽ�ʧЧ����")); // datetime
                // 'ʧЧ����',
                AmbDisambSnglubTab.setSbxQty(new BigDecimal(
                        GetCellData(r, "�Ӽ�����") == null || GetCellData(r, "�Ӽ�����").equals("") ? "0" : GetCellData(r, "�Ӽ�����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ���� a decimal(208 // //
                AmbDisambSnglubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                ambDisambSnglubList.add(AmbDisambSnglubTab);

                ambDisambSngl.setAmbSnglubList(ambDisambSnglubList);

                temp.put(orderNo, ambDisambSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());
        }
        return temp;
    }

    @Override
    public String uploadFileAddDbU8(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, AmbDisambSngl> pusOrderMap = uploadScoreInfoU8(file);
        // System.out.println(pusOrderMap.size());

        for (Map.Entry<String, AmbDisambSngl> entry : pusOrderMap.entrySet()) {
            // System.out.println(entry.getValue().getFormTyp());

            AmbDisambSngl ambDisambSngl = ambDisambSnglMapper.selectISChr(entry.getValue().getFormNum());

            if (ambDisambSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");
            }
            try {
                ambDisambSnglMapper.exInsertAmbDisambSngl(entry.getValue());
                ambDisambSnglMapper.exInsertAmbDisambSnglubTab(entry.getValue().getAmbSnglubList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");

            }
        }

        isSuccess = true;
        message = "��װ��ж�������ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    private Map<String, AmbDisambSngl> uploadScoreInfoU8(MultipartFile file) {
        Map<String, AmbDisambSngl> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "���ݺ�");

                String formTyp = null;
                String formTypEncd = null;
                if (GetCellData(r, "����������").equals("��װ��")) {

                    orderNo = "zc" + orderNo;
                    formTyp = "��װ";
                    formTypEncd = "012";
                } else if (GetCellData(r, "����������").equals("��ж��")) {

                    orderNo = "cz" + orderNo;
                    formTyp = "��ж";
                    formTypEncd = "013";

                }

                // ����ʵ����
                // System.out.println("\t" + orderNo);

                AmbDisambSngl ambDisambSngl = new AmbDisambSngl();
                if (temp.containsKey(orderNo)) {
                    ambDisambSngl = temp.get(orderNo);
                }
                String BaoZhiQi = GetCellData(r, "������");

                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                String mprdcDt = GetCellData(r, "��������") == null ? ""
                        : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ");
                if (mprdcDt == null || mprdcDt.equals("")) {
                    mprdcDt = null;
                }
                String invldtnDt = null;
                if (mprdcDt != null) {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate ldt = LocalDate.parse(mprdcDt, df);
                    invldtnDt = ldt.plusDays(Long.valueOf(BaoZhiQi == null ? "0" : BaoZhiQi)).toString();
                }

                if (GetCellData(r, "����").equals("�׼�")) {
                    ambDisambSngl.setMomKitEncd(GetCellData(r, "�������")); // ĸ���׼�����
                    ambDisambSngl.setFormNum(orderNo); // ���ݺ�
                    ambDisambSngl.setFormDt(
                            GetCellData(r, "����") == null ? "" : GetCellData(r, "����").replaceAll("[^0-9:-]", " ")); // ��������
                    ambDisambSngl.setFormTyp(formTyp); // ����������װ����ж
                    ambDisambSngl.setFormTypEncd(formTypEncd);// �������ͱ���

                    ambDisambSngl
                            .setFee(new BigDecimal(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
                                    : GetCellData(r, "����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ����
                    ambDisambSngl.setFeeComnt(null); // ����˵��
                    ambDisambSngl.setMemo(GetCellData(r, "��ע")); // ��ע
                    ambDisambSngl.setTyp("�׼�"); // ���� �׼� ɢ��
                    ambDisambSngl.setWhsEncd(GetCellData(r, "�ֿ����")); // �ֿ����
                    ambDisambSngl.setMomQty(
                            new BigDecimal(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
                                    : GetCellData(r, "����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ĸ������
                    ambDisambSngl.setTaxRate(BigDecimal.ZERO); // ˰��
                    ambDisambSngl.setMcntnTaxUprc(BigDecimal.ZERO); // ��˰����
                    ambDisambSngl.setMunTaxUprc(
                            new BigDecimal(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
                                    : GetCellData(r, "����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰����
                    ambDisambSngl.setMcntnTaxAmt(BigDecimal.ZERO); // ��˰���
                    ambDisambSngl.setMunTaxAmt(
                            new BigDecimal(GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? "0"
                                    : GetCellData(r, "���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰���
                    ambDisambSngl.setMbatNum(GetCellData(r, "����")); // varchar(200 '����', 1

                    ambDisambSngl.setMprdcDt(mprdcDt); // ��������

                    ambDisambSngl.setBaoZhiQi(BaoZhiQi); // ������

                    ambDisambSngl.setInvldtnDt(invldtnDt); // ʧЧ����
                    ambDisambSngl.setIsNtWms(0); // �Ƿ���WMS�ϴ�
                    ambDisambSngl.setIsNtChk(0); // int(11 '�Ƿ����',
                    ambDisambSngl.setIsNtBookEntry(0); // int(11 '�Ƿ����',
                    ambDisambSngl.setIsNtCmplt(0); // int(11 '�Ƿ����',
                    ambDisambSngl.setIsNtClos(0); // int(11 '�Ƿ�ر�',
                    ambDisambSngl.setPrintCnt(
                            (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
                                    : GetCellData(r, "��ӡ����"))).intValue()); // int(11 '��ӡ����',
                    ambDisambSngl.setSetupPers(GetCellData(r, "�Ƶ���")); // varchar(200 '������',
                    ambDisambSngl.setSetupTm(
                            GetCellData(r, "�Ƶ�ʱ��") == null ? "" : GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " ")); // datetime
                    // '����ʱ��',
                    ambDisambSngl.setMdfr(GetCellData(r, "�޸���")); // varchar(200 '�޸���',
                    ambDisambSngl.setModiTm(
                            GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ")); // datetime
                    // '�޸�ʱ��',
                    ambDisambSngl.setChkr(GetCellData(r, "�����")); // varchar(200 '�����',
                    ambDisambSngl.setChkTm(
                            GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " ")); // datetime
                    // '���ʱ��',
                    ambDisambSngl.setBookEntryPers(null); // ������
                    ambDisambSngl.setBookEntryTm(null); // ������ʱ��

                    BigDecimal bxRule = GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? null
                            : new BigDecimal(GetCellData(r, "���"));
                    if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                        ambDisambSngl.setBxQty(ambDisambSngl.getMomQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // ����
                    } else {
                        ambDisambSngl.setBxQty(BigDecimal.ZERO);// ����
                    }

                    temp.put(orderNo, ambDisambSngl);

                } else if (GetCellData(r, "����").equals("ɢ��")) {
                    // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                    List<AmbDisambSnglubTab> ambDisambSnglubList = ambDisambSngl.getAmbSnglubList();// �����ӱ�
                    if (ambDisambSnglubList == null) {
                        ambDisambSnglubList = new ArrayList<>();
                    }
                    AmbDisambSnglubTab AmbDisambSnglubTab = new AmbDisambSnglubTab();

//					AmbDisambSnglubTab.setOrdrNum(ordrNum); // bigint(20 '���',
                    AmbDisambSnglubTab.setFormNum(orderNo); // varchar(200 // �����ݺ�
                    AmbDisambSnglubTab.setSubKitEncd(GetCellData(r, "�������")); // �����׼�����
                    AmbDisambSnglubTab.setWhsEncd(GetCellData(r, "�ֿ����")); // �ֿ����
                    AmbDisambSnglubTab.setSubQty(
                            new BigDecimal(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
                                    : GetCellData(r, "����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // �Ӽ�����
                    AmbDisambSnglubTab.setMomQty(ambDisambSngl.getMomQty()); // ĸ������
                    AmbDisambSnglubTab.setMomSubRatio(
                            new BigDecimal(GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("") ? "0"
                                    : GetCellData(r, "��������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ĸ�ӱ���
                    AmbDisambSnglubTab.setTaxRate(BigDecimal.ZERO); // ˰��
                    AmbDisambSnglubTab.setScntnTaxUprc(BigDecimal.ZERO); // ��˰����
                    AmbDisambSnglubTab.setSunTaxUprc(
                            new BigDecimal(GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0"
                                    : GetCellData(r, "����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰����
                    AmbDisambSnglubTab.setScntnTaxAmt(BigDecimal.ZERO); // ��˰���
                    AmbDisambSnglubTab.setSunTaxAmt(
                            new BigDecimal(GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? "0"
                                    : GetCellData(r, "���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰���
                    AmbDisambSnglubTab.setSbatNum(GetCellData(r, "����")); // ����
                    AmbDisambSnglubTab.setSprdcDt(mprdcDt); // ��������
                    AmbDisambSnglubTab.setBaoZhiQi(BaoZhiQi); // ������
                    AmbDisambSnglubTab.setSinvldtnDt(invldtnDt); // ʧЧ����

                    BigDecimal bxRule = GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? null
                            : new BigDecimal(GetCellData(r, "���"));
                    if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                        AmbDisambSnglubTab
                                .setSbxQty(AmbDisambSnglubTab.getSubQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // ����
                    } else {
                        AmbDisambSnglubTab.setSbxQty(BigDecimal.ZERO);// ����
                    }
                    AmbDisambSnglubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                    ambDisambSnglubList.add(AmbDisambSnglubTab);
                    ambDisambSngl.setAmbSnglubList(ambDisambSnglubList);
                    temp.put(orderNo, ambDisambSngl);

                }

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());
        }
        return temp;
    }
}
