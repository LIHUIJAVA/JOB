package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.entity.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.CannibSnglMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.service.CannibSnglService;

/**
 * @author zhangxiaoyu
 * @version ����ʱ�䣺2018��11��15�� ����2:21:54
 * @ClassName ������
 * @Description ������
 */
@Service
@Transactional
public class CannibSnglServiceImpl extends poiTool implements CannibSnglService {

    @Autowired
    CannibSnglMapper cannibSnglMapper;
    // ������
    @Autowired
    GetOrderNo getOrderNo;

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;

    @Autowired
    InvtyTabDao invtyTabDao;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    @Autowired
    IntoWhsDao intoWhsDao;
    @Autowired
    InvtyDocDao invtyDocDao;
    // ��λ
    @Autowired
    InvtyGdsBitListMapper bitListMapper;

    // ��ӵ�����
    @Override
    public String insertCSngl(String userId, CannibSngl cSngl, List<CannibSnglSubTab> cList, String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        String number = null;
        try {
            number = getOrderNo.getSeqNo("DB", userId, loginTime);// ��ȡ������
        } catch (Exception e) {

            throw new RuntimeException("��ȡ�����ų���");
        }

        if (cannibSnglMapper.selectCSngl(number) != null) {
            message = "���" + number + "�Ѵ��ڣ����������룡";
            isSuccess = false;

            throw new RuntimeException(message);

        } else {
            if (cannibSnglMapper.selectCSnglSubTabList(number).size() > 0) {
                cannibSnglMapper.deleteCSnglSubTab(number);
            }
            cSngl.setFormTypEncd("011");
            cSngl.setFormNum(number);// ������
            cSngl.setCannibDt(cSngl.getFormDt());

            cannibSnglMapper.insertCSngl(cSngl);// ��������
            for (CannibSnglSubTab cSubTab : cList) {
                cSubTab.setFormNum(cSngl.getFormNum());
                InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                        .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd()+"�ô��������"));
                Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
                if(sisQuaGuaPer == 1){
                    if(StringUtils.isBlank(cSubTab.getBaoZhiQi())|| StringUtils.isBlank(cSubTab.getPrdcDt())
                            || StringUtils.isBlank(cSubTab.getInvldtnDt()) ){
                        throw new RuntimeException(cSubTab.getInvtyEncd()+"�����ڹ������������ô���");
                    }
                }else{
                    cSubTab.setBaoZhiQi(null);
                    cSubTab.setPrdcDt(null);
                    cSubTab.setInvldtnDt(null);
                }
            }
            cannibSnglMapper.insertCSnglSubTab(cList);// �����ӱ�

            isSuccess = true;
            message = "�����ɹ���";
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/insertCSngl", isSuccess, message, cSngl);
        } catch (IOException e) {

        }

        return resp;
    }

    // �޸ĵ�����
    @Override
    public String updateCSngl(CannibSngl cSngl, List<CannibSnglSubTab> cList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(cSngl.getFormNum());
        if (cannibSngl.getIsNtChk() == null) {
            throw new RuntimeException("�������״̬�쳣");
        } else if (cannibSngl.getIsNtChk() == 1) {
            throw new RuntimeException("��������˲������޸�");
        }

        int i = cannibSnglMapper.deleteCSnglSubTab(cSngl.getFormNum());

        cSngl.setFormDt(cSngl.getCannibDt());

        for (CannibSnglSubTab cSubTab : cList) {
            cSubTab.setFormNum(cSngl.getFormNum());
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                    .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd()+"�ô��������"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(cSubTab.getBaoZhiQi())|| StringUtils.isBlank(cSubTab.getPrdcDt())
                        || StringUtils.isBlank(cSubTab.getInvldtnDt()) ){
                    throw new RuntimeException(cSubTab.getInvtyEncd()+"�����ڹ������������ô���");
                }
            }else{
                cSubTab.setBaoZhiQi(null);
                cSubTab.setPrdcDt(null);
                cSubTab.setInvldtnDt(null);
            }
        }

        cannibSnglMapper.updateCSngl(cSngl);
        cannibSnglMapper.insertCSnglSubTab(cList);
        message = "���³ɹ���";

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngl", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����ɾ��
    @Override
    public String deleteAllCSngl(String formNum) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(formNum);
        List<CannibSngl> cannibSngls = cannibSnglMapper.selectCSnglList(list);
        List<String> lists = new ArrayList<String>();
        List<String> lists2 = new ArrayList<String>();
        if (cannibSngls.size() > 0) {
            for (CannibSngl sngl : cannibSngls) {
                if (sngl.getIsNtChk() == 0) {
                    lists.add(sngl.getFormNum());
                } else {
                    lists2.add(sngl.getFormNum());
                }
            }
            if (lists.size() > 0) {
                cannibSnglMapper.insertCSnglDl(lists);
                cannibSnglMapper.insertCSnglSubTabDl(lists);

                cannibSnglMapper.deleteAllCSngl(lists);
                isSuccess = true;
                message = "ɾ���ɹ���" + lists.toString();
                if (lists2.size() > 0) {
                    message = message + "\r�������" + lists2;
                }
            } else {
                message = "�������" + lists2;

            }
        } else {
            isSuccess = false;
            message = "���" + formNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/deleteAllCSngl", isSuccess, message, null);
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
        }
        return list;
    }

    // ��ѯ����ҳ��
    @Override
    public String query(String formNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<CannibSnglSubTab> cSubTabList = null;
        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(formNum);
        if (cannibSngl != null) {
            cSubTabList = cannibSnglMapper.selectCSnglSubTabList(formNum);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + formNum + "�����ڣ�";
        }

//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		for (CannibSnglSubTab cannibSnglSubTab : cSubTabList) {
//			Map<String, Object> map = ObjToJsonUtil.beanToMap(cannibSnglSubTab);
//			map.put("invtyEncd", map.get("invtyId"));
//			list.add(map);
//		}
        try {
            resp = BaseJson.returnRespObjList("whs/cannib_sngl/query", isSuccess, message, cannibSngl, cSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String queryList(Map map) {
        String resp = "";

        map.put("tranOutDeptEncd", getList((String) map.get("tranOutDeptEncd")));
        map.put("tranInDeptEncd", getList((String) map.get("tranInDeptEncd")));
        map.put("formNum", getList((String) map.get("formNum")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("tranInWhsEncd", getList((String) map.get("tranInWhsEncd")));
        map.put("tranOutWhsEncd", getList((String) map.get("tranOutWhsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<CannibSnglMap> aList = cannibSnglMapper.selectList(map);
        int count = cannibSnglMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/cannib_sngl/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, aList);

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA ��ѯ
    @Override
    public String selectCSnglChkr() {
        String resp = "";
        List<CannibSngl> csChkrList = cannibSnglMapper.selectCSnglChkr();
        try {
            resp = BaseJson.returnRespObjList("whs/cannib_sngl/selectCSnglChkr", true, "��ѯ�ɹ���", null, csChkrList);
        } catch (IOException e) {

        }
        return resp;
    }

    // �޸��ӱ�
    @Override
    public String updateCSnglSubTab(CannibSnglSubTab cSubTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = cannibSnglMapper.updateCSnglSubTab(cSubTab);
        if (i >= 1) {
            message = "�޸ĳɹ���";
            isSuccess = true;
        } else {
            message = "�޸�ʧ�ܣ�";
            isSuccess = true;
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSnglSubTab", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // �޸�����
    @Override
    public String updateCSngl(CannibSngl cSngl) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = cannibSnglMapper.updateCSngl(cSngl);
        if (i >= 1) {
            message = "�޸ĳɹ���";
            isSuccess = true;
        } else {
            message = "�޸�ʧ�ܣ�";
            isSuccess = true;
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngl", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ���������
    @Override
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate) {

        String message = "";
        String loginTime = formDate;
        ObjectNode dbzhu = null;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {


        }
        String danHao = dbzhu.get("formNum").asText();

        List<CannibSngl> cannibSngls = new ArrayList<>();// ��������list
        List<CannibSnglSubTab> cSnglSubTabs = new ArrayList<>();// �����ӱ�list
        List<InvtyTab> invtyTabs = new ArrayList<>();// ����list
        OthOutIntoWhs othOutWhs = new OthOutIntoWhs();// ������������
        OthOutIntoWhs othIntoWhs = new OthOutIntoWhs();// �����������
        List<OthOutIntoWhsSubTab> othOutWhsSubTabs = new ArrayList<>();// ����������
        List<OthOutIntoWhsSubTab> othIntoWhsSubTabs = new ArrayList<>();// ���������

        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(danHao);

        if (cannibSngl == null) {
            throw new RuntimeException("����:" + danHao + "������ ");
        }
        if (cannibSngl.getIsNtChk() == 1) {
            throw new RuntimeException("���ţ�" + danHao + "����ˣ�����Ҫ�ٴ����");
        }

        cannibSngl.setChkr(userName);
        cannibSngl.setChkTm(loginTime);
        cannibSngls.add(cannibSngl);

        int a = cannibSnglMapper.updateCSnglChk(cannibSngls);// �޸����״̬

        if (a >= 1) {
            cSnglSubTabs = cannibSnglMapper.selectCSnglSubTabList(danHao);
            if (cSnglSubTabs.size() == 0) {
                throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");
            }
            // ������=======
            String number = null;
            try {
                number = getOrderNo.getSeqNo("OTCR", userId, loginTime);
            } catch (Exception e) {

                throw new RuntimeException("��ȡ����ʧ��");
            } // ��ȡ������
            othOutWhs.setFormNum(number);// ������
            othOutWhs.setFormDt(formDate);
            othOutWhs.setOutIntoWhsTypId("2");// ������������
            othOutWhs.setOutStatus("������");// ����״̬
            othOutWhs.setSetupPers(userName);// ������
            othOutWhs.setFormTypEncd("015");
            othOutWhs.setWhsEncd(cannibSngl.getTranOutWhsEncd());// ת���ֿ����
            othOutWhs.setSrcFormNum(cannibSngl.getFormNum());// ��Դ���ݺ�
            othOutWhs.setMemo(cannibSngl.getMemo());// ��ע

            // �����=======
            String number2 = null;
            try {
                number2 = getOrderNo.getSeqNo("OTCR", userId, loginTime);
            } catch (Exception e) {

                throw new RuntimeException("��ȡ����ʧ��");

            } // ��ȡ������
            othIntoWhs.setFormNum(number2);// ������
            othIntoWhs.setFormDt(formDate);
            othIntoWhs.setOutIntoWhsTypId("1");// �����������
            othIntoWhs.setInStatus("������");// ���״̬
            othIntoWhs.setSetupPers(userName);// ������
            othIntoWhs.setFormTypEncd("014");
            othIntoWhs.setWhsEncd(cannibSngl.getTranInWhsEncd());// ת��ֿ����
            othIntoWhs.setSrcFormNum(cannibSngl.getFormNum());// ��Դ���ݺ�
            othIntoWhs.setMemo(cannibSngl.getMemo());// ��ע

            for (CannibSnglSubTab tabs : cSnglSubTabs) {
                // ����
                InvtyTab invtyTab = new InvtyTab();
                invtyTab.setWhsEncd(cannibSngl.getTranOutWhsEncd());// �ֿ����
                invtyTab.setInvtyEncd(tabs.getInvtyId());// �������
                invtyTab.setBatNum(tabs.getBatNum());// ����
                invtyTab.setAvalQty(tabs.getCannibQty());// ������

                OthOutIntoWhsSubTab othOutWhsSubTab = new OthOutIntoWhsSubTab();
                othOutWhsSubTab.setFormNum(number);
                othOutWhsSubTab.setInvtyEncd(tabs.getInvtyId());
                othOutWhsSubTab.setQty(tabs.getCannibQty());
                othOutWhsSubTab.setBxQty(tabs.getBxQty());
                othOutWhsSubTab.setTaxRate(tabs.getTaxRate());
                othOutWhsSubTab.setCntnTaxUprc(tabs.getCntnTaxUprc());
                othOutWhsSubTab.setUnTaxUprc(tabs.getUnTaxUprc());
                othOutWhsSubTab.setCntnTaxAmt(tabs.getCntnTaxAmt());
                othOutWhsSubTab.setUnTaxAmt(tabs.getUnTaxAmt());
                othOutWhsSubTab.setBatNum(tabs.getBatNum());
                othOutWhsSubTab.setPrdcDt(tabs.getPrdcDt());
                othOutWhsSubTab.setBaoZhiQi(tabs.getBaoZhiQi());
                othOutWhsSubTab.setInvldtnDt(tabs.getInvldtnDt());
                othOutWhsSubTab.setProjEncd(tabs.getProjEncd());
                othOutWhsSubTab.setTaxAmt(tabs.getTaxAmt());

                // �����
                OthOutIntoWhsSubTab othIntoWhsSubTab = new OthOutIntoWhsSubTab();
                othIntoWhsSubTab.setFormNum(number2);
                othIntoWhsSubTab.setInvtyEncd(tabs.getInvtyId());
                othIntoWhsSubTab.setQty(tabs.getCannibQty());
                othIntoWhsSubTab.setBxQty(tabs.getBxQty());
                othIntoWhsSubTab.setTaxRate(tabs.getTaxRate());
                othIntoWhsSubTab.setCntnTaxUprc(tabs.getCntnTaxUprc());
                othIntoWhsSubTab.setUnTaxUprc(tabs.getUnTaxUprc());
                othIntoWhsSubTab.setCntnTaxAmt(tabs.getCntnTaxAmt());
                othIntoWhsSubTab.setUnTaxAmt(tabs.getUnTaxAmt());
                othIntoWhsSubTab.setBatNum(tabs.getBatNum());
                othIntoWhsSubTab.setPrdcDt(tabs.getPrdcDt());
                othIntoWhsSubTab.setBaoZhiQi(tabs.getBaoZhiQi());
                othIntoWhsSubTab.setInvldtnDt(tabs.getInvldtnDt());
                othIntoWhsSubTab.setProjEncd(tabs.getProjEncd());
                othIntoWhsSubTab.setTaxAmt(tabs.getTaxAmt());

                othOutWhsSubTabs.add(othOutWhsSubTab);
                othIntoWhsSubTabs.add(othIntoWhsSubTab);
                InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);

                Optional.ofNullable(inTab).orElseThrow(() -> new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd()
                        + ",�����" + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ�治����,���ʧ�ܣ�"));

                if (inTab.getAvalQty().compareTo(tabs.getCannibQty()) > -1) {
                    invtyNumMapper.updateAInvtyTab(Arrays.asList(invtyTab));// �޸Ŀ��������
                    message = "����:" + danHao + "��˳ɹ���";
                } else {
                    throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd() + ",�����"
                            + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ����������,���ʧ�ܣ�");
                }

            }
            othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);// �����������
            othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);

            othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);// �������������
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);

        } else {
            throw new RuntimeException("���ţ�" + danHao + "���ʧ�ܣ�");
        }

        return message;
    }

    // ������ ����
    @Override
    public String updateCSnglNoChk(String userId, String jsonBody, String userName) {
        String message = "";

        ObjectNode dbzhu = null;

        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }
        String danHao = dbzhu.get("formNum").asText();
        String chkr = userName;// �����
        List<CannibSngl> cannibSngls = new ArrayList<>();// ��������list
        List<CannibSnglSubTab> cannibSnglSubTab = new ArrayList<>();// ������

        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(danHao);
        if (cannibSngl == null) {
            throw new RuntimeException("����:" + danHao + "������ ");
        }
        // ������
        cannibSngl.setChkr(chkr);// �����
        cannibSngls.add(cannibSngl);


        if (cannibSngl.getIsNtChk() == 0) {
            return message = "����:" + danHao + "������,����Ҫ�ظ����� ";
        } else {
            // ��ѯ�Ƿ������������
            OthOutIntoWhs intoWhs1 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(danHao, "1");
            OthOutIntoWhs intoWhs2 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(danHao, "2");
            if (intoWhs1 == null || intoWhs2 == null) {
                throw new RuntimeException("���ţ�" + danHao + "��Ӧ���������������쳣");
            }
            // ɾ���������������ݣ������������
            if (intoWhs1.getIsNtChk() == 1 || intoWhs2.getIsNtChk() == 1) {
                throw new RuntimeException("���ţ�" + danHao + "��Ӧ����������������");
            } else {
                List<String> lists = Arrays.asList(intoWhs1.getFormNum(), intoWhs2.getFormNum());
                othOutIntoWhsMapper.insertOthOutIntoWhsDl(lists);
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTabDl(lists);
                othOutIntoWhsMapper.deleteAllOthOutIntoWhs(lists);
                bitListMapper.deleteInvtyGdsBitList(lists);
            }

            int a = cannibSnglMapper.updateCSnglNoChk(cannibSngls);// �޸�����״̬
            if (a >= 1) {

                cannibSnglSubTab = cannibSnglMapper.selectCSnglSubTabList(danHao);// ������
                if (cannibSnglSubTab.size() == 0) {
                    throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");
                }
                for (CannibSnglSubTab dbzi : cannibSnglSubTab) {
                    // ����
                    InvtyTab invtyTab = new InvtyTab();
                    invtyTab.setWhsEncd(cannibSngl.getTranOutWhsEncd());// �ֿ����
                    invtyTab.setInvtyEncd(dbzi.getInvtyId());// �������
                    invtyTab.setBatNum(dbzi.getBatNum());// ����
                    invtyTab.setAvalQty(dbzi.getCannibQty());// ������
// 					invtyTab.setNowStok(BigDecimal.ZERO);// �ִ���
                    InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);// ת��ֿ�

                    if (invtyNumMapper.selectInvtyTabByTerm(invtyTab) != null) {
                        invtyNumMapper.updateAInvtyTabJia(Arrays.asList(invtyTab));// �޸Ŀ�������� ������ ����
                    } else {
                        invtyTab.setPrdcDt(dbzi.getPrdcDt());
                        invtyTab.setBaoZhiQi(dbzi.getBaoZhiQi());
                        invtyTab.setInvldtnDt(dbzi.getInvldtnDt());
//							��� ע�Ͳ���
//							invtyTab.setCntnTaxUprc(BigDecimal.ZERO);
//							invtyTab.setUnTaxUprc(BigDecimal.ZERO);
//							invtyTab.setTaxRate(BigDecimal.ZERO);
//							invtyTab.setCntnTaxAmt(BigDecimal.ZERO);
//							invtyTab.setUnTaxAmt(BigDecimal.ZERO);
                        // �������� ���
                        invtyNumMapper.insertInvtyTabList(Arrays.asList(invtyTab));
                    }
                }
                message = "���ݣ�" + danHao + "��������ɹ���";
            } else {
                throw new RuntimeException("���ݣ�" + danHao + "��������ʧ�ܣ�");
            }

        }

        return message;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";

        map.put("tranOutDeptEncd", getList((String) map.get("tranOutDeptEncd")));
        map.put("tranInDeptEncd", getList((String) map.get("tranInDeptEncd")));
        map.put("formNum", getList((String) map.get("formNum")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("tranInWhsEncd", getList((String) map.get("tranInWhsEncd")));
        map.put("tranOutWhsEncd", getList((String) map.get("tranOutWhsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<CannibSnglMap> aList = cannibSnglMapper.selectList(map);
//		List<CannibSngl> aList = cannibSnglMapper.selectListDaYin(map);
        aList.add(new CannibSnglMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/cannib_sngl/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
//			resp = BaseJson.returnRespObjList("whs/cannib_sngl/queryListDaYin", true, "��ѯ�ɹ���", null, aList);

        } catch (IOException e) {

        }
        return resp;
    }

    // ��ѯ������ִ��� ������
    @Override
    public String selectInvty(Map map) {
        String resp = "";
        List<InvtyTab> invtyTabList = cannibSnglMapper.selectInvty(map);
        try {
            resp = BaseJson.returnRespObjList("whs/cannib_sngl/selectInvty", true, "��ѯ�ɹ���", null, invtyTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, CannibSngl> pusOrderMap = uploadScoreInfo(file);

        for (Map.Entry<String, CannibSngl> entry : pusOrderMap.entrySet()) {

            CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(entry.getValue().getFormNum());

            if (cannibSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");

            }
            try {
                cannibSnglMapper.exInsertCSngl(entry.getValue());

                cannibSnglMapper.exInsertCSnglSubTab(entry.getValue().getCheckSnglList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "�����������ɹ���";
        try {
            resp = BaseJson.returnResp("whs/cannib_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // ����excle
    private Map<String, CannibSngl> uploadScoreInfo(MultipartFile file) {
        Map<String, CannibSngl> temp = new HashMap<>();
        int j = 0;
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
                CannibSngl cannibSngl = new CannibSngl();
                if (temp.containsKey(orderNo)) {
                    cannibSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                cannibSngl.setFormNum(orderNo);// ���ݺ�
                cannibSngl.setFormDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������")); // ��������,q
                // datetime
                cannibSngl.setCannibDt(cannibSngl.getFormDt());
//                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������")); // ��������,a
                // datetime
                cannibSngl.setTranOutWhsEncd(GetCellData(r, "ת���ֿ����")); // ת���ֿ����,q varchar(200
                cannibSngl.setTranInWhsEncd(GetCellData(r, "ת��ֿ����")); // ת��ֿ����,q varchar(200
//                cannibSngl.setTranOutDeptEncd(GetCellData(r, "ת�����ű���")); // ת�����ű���,q varchar(200
//                cannibSngl.setTranInDeptEncd(GetCellData(r, "ת�벿�ű���")); // ת�벿�ű���,q varchar(200
//                cannibSngl.setCannibStatus(GetCellData(r, "����״̬")); // ����״̬, a varchar(200
                cannibSngl.setMemo(GetCellData(r, "��ע")); // ��ע,q varchar(2000
                cannibSngl.setFormTypEncd("011");// �������ͱ���

//				cannibSngl.setIsNtWms(new Double(GetCellData(r, "�Ƿ���WMS�ϴ�")).intValue()); // �Ƿ���WMS�ϴ�,a int(11
                cannibSngl.setIsNtChk(
                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����"))).intValue()); // �Ƿ����(0.δ���1.�����,a int(11
//                cannibSngl.setIsNtCmplt(
//                        (new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ����"))).intValue()); // �Ƿ����,a int(11
//                cannibSngl.setIsNtClos(
//                        (new Double(GetCellData(r, "�Ƿ�ر�") == null || GetCellData(r, "�Ƿ�ر�").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ�ر�"))).intValue()); // �Ƿ�ر�,a int(11
//                cannibSngl.setPrintCnt(
//                        (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
//                                : GetCellData(r, "��ӡ����"))).intValue()); // ��ӡ����,q int(11
                cannibSngl.setSetupPers(GetCellData(r, "������")); // ������,q varchar(200
                cannibSngl.setSetupTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // ����ʱ��,q
                // datetime
                cannibSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���,q varchar(200
                cannibSngl.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��")); // �޸�ʱ��,q
                // datetime
                cannibSngl.setChkr(GetCellData(r, "�����")); // �����,q varchar(200
                cannibSngl.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��")); // ���ʱ��,q
                // datetime

                List<CannibSnglSubTab> cannibSnglSubList = cannibSngl.getCheckSnglList();// �����ӱ�
                if (cannibSnglSubList == null) {
                    cannibSnglSubList = new ArrayList<>();
                }

                CannibSnglSubTab cannibSnglSubTab = new CannibSnglSubTab();

                cannibSnglSubTab.setFormNum(orderNo); // ���ݺ� varchar(200
                cannibSnglSubTab.setInvtyId(GetCellData(r, "�������")); // ������ 1 varchar(200

                cannibSnglSubTab.setCannibQty(
                        new BigDecimal(GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("") ? "0"
                                : GetCellData(r, "��������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // �������� 1 decimal(208

                cannibSnglSubTab.setRecvQty(cannibSnglSubTab.getCannibQty());
//                        new BigDecimal(GetCellData(r, "ʵ������") == null || GetCellData(r, "ʵ������").equals("") ? "0"
//                                : GetCellData(r, "ʵ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ʵ������ a decimal(208
                cannibSnglSubTab.setBatNum(GetCellData(r, "����")); // ���� 1 varchar(200
                cannibSnglSubTab.setInvldtnDt(
                        GetCellData(r, "ʧЧ����") == null ? "" : GetCellData(r, "ʧЧ����")); // ʧЧ����
                // 1
                // datetime
                cannibSnglSubTab.setBaoZhiQi(GetCellData(r, "������")); // ������ 1 varchar(200
                cannibSnglSubTab.setPrdcDt(
                        GetCellData(r, "�������� ") == null ? "" : GetCellData(r, "��������")); // ��������
                // 1
                // datetime
                cannibSnglSubTab.setTaxRate(new BigDecimal(
                        GetCellData(r, "˰��") == null || GetCellData(r, "˰��").equals("") ? "0" : GetCellData(r, "˰��"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ˰�� a decimal(208
                cannibSnglSubTab.setCntnTaxUprc(
                        new BigDecimal(GetCellData(r, "��˰����") == null || GetCellData(r, "��˰����").equals("") ? "0"
                                : GetCellData(r, "��˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ��˰���� 1 decimal(208
                cannibSnglSubTab.setUnTaxUprc(
                        new BigDecimal(GetCellData(r, "δ˰����") == null || GetCellData(r, "δ˰����").equals("") ? "0"
                                : GetCellData(r, "δ˰����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰���� 1 decimal(208
                cannibSnglSubTab.setCntnTaxAmt(
                        new BigDecimal(GetCellData(r, "��˰���") == null || GetCellData(r, "��˰���").equals("") ? "0"
                                : GetCellData(r, "��˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ��˰��� a decimal(208
                cannibSnglSubTab.setUnTaxAmt(
                        new BigDecimal(GetCellData(r, "δ˰���") == null || GetCellData(r, "δ˰���").equals("") ? "0"
                                : GetCellData(r, "δ˰���")).setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰��� a decimal(208
                cannibSnglSubTab.setBxQty(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ���� a decimal(208
                cannibSnglSubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                cannibSnglSubList.add(cannibSnglSubTab);

                cannibSngl.setCheckSnglList(cannibSnglSubList);
                temp.put(orderNo, cannibSngl);

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
        Map<String, CannibSngl> pusOrderMap = uploadScoreInfoU8(file);

        for (Map.Entry<String, CannibSngl> entry : pusOrderMap.entrySet()) {

            CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(entry.getValue().getFormNum());

            if (cannibSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getFormNum() + " ����");

            }
            try {
                cannibSnglMapper.exInsertCSngl(entry.getValue());

                cannibSnglMapper.exInsertCSnglSubTab(entry.getValue().getCheckSnglList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "�����������ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    private Map<String, CannibSngl> uploadScoreInfoU8(MultipartFile file) {
        Map<String, CannibSngl> temp = new HashMap<>();
        int j = 0;
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
                CannibSngl cannibSngl = new CannibSngl();
                if (temp.containsKey(orderNo)) {
                    cannibSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                cannibSngl.setFormNum(orderNo);// ���ݺ�
                cannibSngl.setFormDt(
                        GetCellData(r, "����") == null ? "" : GetCellData(r, "����").replaceAll("[^0-9:-]", " ")); // ��������,q
                // datetime
                cannibSngl.setCannibDt(
                        GetCellData(r, "����") == null ? "" : GetCellData(r, "����").replaceAll("[^0-9:-]", " ")); // ��������,a
                // datetime
                cannibSngl.setTranOutWhsEncd(GetCellData(r, "ת���ֿ����")); // ת���ֿ����
                cannibSngl.setTranInWhsEncd(GetCellData(r, "ת��ֿ����")); // ת��ֿ����
                cannibSngl.setTranOutDeptEncd(GetCellData(r, "ת�����ű���")); // ת�����ű���
                cannibSngl.setTranInDeptEncd(GetCellData(r, "ת�벿�ű���")); // ת�벿�ű���
                cannibSngl.setCannibStatus("������"); // ����״̬, a varchar(200
                cannibSngl.setMemo(GetCellData(r, "��ע")); // ��ע,q varchar(2000
                cannibSngl.setFormTypEncd("011");// �������ͱ���

                cannibSngl.setIsNtWms(0); // �Ƿ���WMS�ϴ�,a int(11
                cannibSngl.setIsNtChk(0); // �Ƿ����(0.δ���1.�����,a int(11
                cannibSngl.setIsNtCmplt(0); // �Ƿ����,a int(11
                cannibSngl.setIsNtClos(0); // �Ƿ�ر�,a int(11
                cannibSngl.setPrintCnt(
                        (new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
                                : GetCellData(r, "��ӡ����"))).intValue()); // ��ӡ����
                cannibSngl.setSetupPers(GetCellData(r, "�Ƶ���")); // ������
                cannibSngl.setSetupTm(
                        GetCellData(r, "�Ƶ�ʱ��") == null ? "" : GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " ")); // ����ʱ��,q
                // datetime
                cannibSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���
                cannibSngl.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ")); // �޸�ʱ��
                cannibSngl.setChkr(GetCellData(r, "�����")); // �����
                cannibSngl.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " ")); // ���ʱ��
                List<CannibSnglSubTab> cannibSnglSubList = cannibSngl.getCheckSnglList();// �����ӱ�
                if (cannibSnglSubList == null) {
                    cannibSnglSubList = new ArrayList<>();
                }

                CannibSnglSubTab cannibSnglSubTab = new CannibSnglSubTab();

                cannibSnglSubTab.setFormNum(orderNo); // ���ݺ� varchar(200
                cannibSnglSubTab.setInvtyId(GetCellData(r, "�������")); // ������ 1 varchar(200

                cannibSnglSubTab.setCannibQty(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // �������� 1 decimal(208

                cannibSnglSubTab.setRecvQty(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // ʵ������
                cannibSnglSubTab.setBatNum(GetCellData(r, "����")); // ����
                cannibSnglSubTab.setInvldtnDt(
                        GetCellData(r, "ʧЧ����") == null ? "" : GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " ")); // ʧЧ����
                // 1
                String BaoZhiQi = GetCellData(r, "������");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                } // datetime
                cannibSnglSubTab.setBaoZhiQi(BaoZhiQi); // ������ 1 varchar(200
                cannibSnglSubTab.setPrdcDt(
                        GetCellData(r, "�������� ") == null ? "" : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ")); // ��������
                // 1
                // datetime
                cannibSnglSubTab.setTaxRate(new BigDecimal(0).setScale(8, BigDecimal.ROUND_HALF_UP)); // ˰�� a
                // decimal(208
                cannibSnglSubTab.setCntnTaxUprc(BigDecimal.ZERO); // ��˰���� 1 decimal(208
                cannibSnglSubTab.setUnTaxUprc(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰����
                cannibSnglSubTab.setCntnTaxAmt(BigDecimal.ZERO); // ��˰���
                cannibSnglSubTab.setUnTaxAmt(new BigDecimal(
                        GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? "0" : GetCellData(r, "���"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // δ˰��� a decimal(208

                BigDecimal bxRule = GetCellData(r, "���") == null || GetCellData(r, "���").equals("") ? null
                        : new BigDecimal(GetCellData(r, "���"));
                if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                    cannibSnglSubTab
                            .setBxQty(cannibSnglSubTab.getCannibQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // ����
                } else {
                    cannibSnglSubTab.setBxQty(BigDecimal.ZERO);// ����
                }
                cannibSnglSubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                cannibSnglSubList.add(cannibSnglSubTab);

                cannibSngl.setCheckSnglList(cannibSnglSubList);
                temp.put(orderNo, cannibSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());

        }
        return temp;
    }

}
