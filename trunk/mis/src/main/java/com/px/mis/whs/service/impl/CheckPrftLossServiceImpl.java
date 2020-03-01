package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.px.mis.whs.entity.CannibSnglMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.CannibSnglMapper;
import com.px.mis.whs.dao.CheckPrftLossMapper;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossMap;
import com.px.mis.whs.entity.CheckPrftLossSubTab;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.OthOutIntoWhs;
import com.px.mis.whs.entity.OthOutIntoWhsSubTab;
import com.px.mis.whs.service.CheckPrftLossService;

@Service
@Transactional
public class CheckPrftLossServiceImpl extends poiTool implements CheckPrftLossService {

    @Autowired
    CheckPrftLossMapper checkPrftLossMapper;
    // ������
    @Autowired
    GetOrderNo getOrderNo;

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    @Autowired
    CannibSnglMapper cannibSnglMapper;
    @Autowired
    InvtyGdsBitListMapper bitListMapper;
    /**
     * �ֿ�
     */
    @Autowired
    WhsDocMapper whsDocMapper;

    @Autowired
    OthOutIntoWhsServiceImpl intoWhsServiceImpl;

    // ���������
    @Override
    public String insertCheckSnglLoss(String userId, CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList ,String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            String number = getOrderNo.getSeqNo("SY", userId,loginTime);// ��ȡ������

            if (checkPrftLossMapper.selectCheckSnglLoss(number) != null) {
                message = "���" + number + "�Ѵ��ڣ����������룡";
                isSuccess = false;
            } else {
                if (checkPrftLossMapper.selectCheckSnglLossSubTab(number).size() > 0) {
                    checkPrftLossMapper.deleteCheckSnglSubLossTab(number);
                }
                cSngl.setCheckFormNum(number);// ���ݺ�
                checkPrftLossMapper.insertCheckSnglLoss(cSngl);
                checkPrftLossMapper.insertCheckSnglLossSubTab(cSubTabList);

                message = "�����ɹ���";
                isSuccess = true;
            }

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/insertCheckSnglLoss", isSuccess, message, null);
        } catch (Exception e) {

        }
        return resp;
    }

    // �޸������
    @Override
    public String updateCheckSnglLoss(CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(cSngl.getCheckFormNum());
        if (checkPrftLoss.getIsNtChk() == null) {
            throw new RuntimeException("�������״̬�쳣");
        } else if (checkPrftLoss.getIsNtChk() == 1) {
            throw new RuntimeException("��������˲������޸�");
        }

//		checkPrftLossMapper.deleteCheckSnglSubLossTab(cSngl.getCheckFormNum());
        checkPrftLossMapper.updateCheckSnglLoss(cSngl);
        for (CheckPrftLossSubTab cPrftLossSubTab : cSubTabList) {
            // ��ȡδ˰����
            BigDecimal unTaxUprc = cPrftLossSubTab.getUnTaxUprc();
            // ��ȡ˰�� ҳ��˰��δ������������Ҫ��˰��/100
            BigDecimal taxRate = cPrftLossSubTab.getTaxRate().divide(new BigDecimal(100));
            // ��������
            BigDecimal prftLossQtysg = cPrftLossSubTab.getPrftLossQtys();
            // δ˰������
            cPrftLossSubTab.setUnTaxPrftLossAmts(unTaxUprc.multiply(prftLossQtysg));
            //����˰��
            cPrftLossSubTab.setPrftLossTaxAmts(prftLossQtysg.multiply(unTaxUprc).multiply(taxRate));
            // ��˰������
            cPrftLossSubTab.setCntnTaxPrftLossAmts(cPrftLossSubTab.getUnTaxPrftLossAmts().add(cPrftLossSubTab.getPrftLossTaxAmts()));//��˰������
        }
        checkPrftLossMapper.updateCheckPrftLossSubTab(cSubTabList);

//		.insertCheckSnglLossSubTab(cSubTabList);
        message = "���³ɹ���";

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCheckSnglLoss", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����ɾ��
    @Override
    public String deleteAllCheckSnglLoss(String checkFormNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> list = getList(checkFormNum);
        List<CheckPrftLoss> prftLosses = checkPrftLossMapper.selectCSnglLossList(list);
        List<String> lists = new ArrayList<String>();
        List<String> lists2 = new ArrayList<String>();

        for (CheckPrftLoss sngl : prftLosses) {
            if (sngl.getIsNtChk() == 0) {
                lists.add(sngl.getCheckFormNum());
            } else {
                lists2.add(sngl.getCheckFormNum());
            }
        }
        if (lists.size() > 0) {
            checkPrftLossMapper.insertCheckSnglLossDl(lists);
            checkPrftLossMapper.insertCheckSnglLossSubTabDl(lists);

            checkPrftLossMapper.deleteAllCheckSnglLoss(lists);
            isSuccess = true;
            message = "ɾ���ɹ���" + lists.toString();
            if (lists2.size() > 0) {
                message = message + "\r�������" + lists2;
            }
        } else {
            isSuccess = false;
            message = "���" + lists2 + "����";
        }

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/deleteAllCheckSnglLoss", isSuccess, message, null);
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

    // ��ѯ
    @Override
    public String query(String checkFormNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<CheckPrftLossSubTab> cSubTabList = new ArrayList<>();
        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(checkFormNum);
        if (checkPrftLoss != null) {
            cSubTabList = checkPrftLossMapper.selectCheckSnglLossSubTab(checkFormNum);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + checkFormNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/check_sngl_loss/query", isSuccess, message, checkPrftLoss,
                    cSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String queryList(Map map) {
        String resp = "";

//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("checkFormNum", getList((String) map.get("checkFormNum")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("srcFormNum", getList((String) map.get("srcFormNum")));
        map.put("setupPers", getList((String) map.get("setupPers")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<CheckPrftLossMap> aList = checkPrftLossMapper.selectList(map);
        int count = checkPrftLossMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/check_sngl_loss/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ���
    @Override
    public String updateCSnglLossChk(String userId, String jsonBody, String userName, String formDate) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        String loginTime=formDate;
        ObjectNode jdRespJson = null;
        try {
            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }
        String danHao = jdRespJson.get("formNum").asText();
        String chkr = userName;// �����
        String number = null;// �����=======
        String numberc = null;// ������=======

        List<CheckPrftLoss> cPrft = new ArrayList<>();// �̵���������
        List<CheckPrftLossSubTab> cPrftSubTab = new ArrayList<>();// �̵������ӱ�
        List<InvtyTab> invtyTabs = new ArrayList<>();// ����list
        OthOutIntoWhs othOutWhs = new OthOutIntoWhs();// ����������
        OthOutIntoWhs othIntoWhs = new OthOutIntoWhs();// ���������
        List<OthOutIntoWhsSubTab> othOutWhsSubTabs = new ArrayList<>();// ����������
        List<OthOutIntoWhsSubTab> othIntoWhsSubTabs = new ArrayList<>();// ���������
        List<MovBitTab> movBitTabInto = new ArrayList<>();// ��λ��
        List<MovBitTab> movBitTabOut = new ArrayList<>();// ��λ��
        Map<String, OthOutIntoWhsSubTab> IntoMap = new HashMap<String, OthOutIntoWhsSubTab>();// ��λ��
        Map<String, OthOutIntoWhsSubTab> OutMap = new HashMap<String, OthOutIntoWhsSubTab>();// ��λ��

        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectIsChr(danHao);
        if (checkPrftLoss == null) {
            throw new RuntimeException("����:" + danHao + "������");
        }
        checkPrftLoss.setChkr(chkr);
        checkPrftLoss.setChkTm(loginTime);
        // �̵���������
        cPrft.add(checkPrftLoss);

        if (checkPrftLoss.getIsNtChk() == 1) {
            throw new RuntimeException("���ţ�" + danHao + "����ˣ�����Ҫ�ٴ����");

        } else {
            int a = checkPrftLossMapper.updateCSnglLossChk(cPrft);
            if (a >= 1) {
                // ����
                boolean isNtPrgrGds = isNtPrgrGdsBitMgmt(checkPrftLoss.getWhsEncd());
                cPrftSubTab = checkPrftLossMapper.selectCheckSnglLossSubTab(danHao);

                for (CheckPrftLossSubTab dbzi : cPrftSubTab) {
                    InvtyTab invtyTab = new InvtyTab();
                    invtyTab.setWhsEncd(checkPrftLoss.getWhsEncd());// �ֿ����
                    // ����
                    invtyTab.setInvtyEncd(dbzi.getInvtyEncd());
                    invtyTab.setBatNum(dbzi.getBatNum());
                    invtyTab.setAvalQty(dbzi.getPrftLossQtys().abs());// ������
//					invtyTab.setNowStok(BigDecimal.ZERO);// �ִ���
                    invtyTab.setPrdcDt(dbzi.getPrdcDt());
                    invtyTab.setBaoZhiQi(dbzi.getBaoZhiQi());
                    invtyTab.setInvldtnDt(dbzi.getInvldtnDt());
                    invtyTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());// ��˰����
                    invtyTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());// δ˰����
                    invtyTab.setTaxRate(dbzi.getTaxRate().abs());// ˰��
                    invtyTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// ��˰��������
                    invtyTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// δ˰��������
                    invtyTabs.add(invtyTab);
                    // ӯ ���
                    String dbziKey = dbzi.getInvtyEncd() + "__" + dbzi.getBatNum();

                    if (dbzi.getPrftLossQtys().doubleValue() > 0) {
                        // �����=======
                        if (number == null) {
                            try {
                                number = getOrderNo.getSeqNo("OTCR", userId,loginTime);
                            } catch (Exception e) {
                                throw new RuntimeException("��ȡ������ʧ�� ");
                            } // ��ȡ������
                            othIntoWhs.setFormNum(number);// ������
                            othIntoWhs.setFormDt(formDate);
                            othIntoWhs.setOutIntoWhsTypId("8");// ��ӯ�������
                            othIntoWhs.setInStatus("������");// ���״̬
                            othIntoWhs.setSetupPers(userName);// ������
                            othIntoWhs.setWhsEncd(checkPrftLoss.getWhsEncd());// �ֿ����
                            othIntoWhs.setSrcFormNum(checkPrftLoss.getCheckFormNum());// ��Դ���ݺ�
                            othIntoWhs.setFormTypEncd("014");
                            othIntoWhs.setMemo(checkPrftLoss.getMemo());// ��ע
                            othIntoWhs.setIsNtChk(0);// ���״̬
                        }
                        // �����
                        if (IntoMap.containsKey(dbziKey)) {
                            OthOutIntoWhsSubTab othIntoWhsSubTab = IntoMap.get(dbziKey);
                            othIntoWhsSubTab.setQty(othIntoWhsSubTab.getQty().add(dbzi.getPrftLossQtys().abs()));// ��������dbzi.getPrftLossQtys().abs()
                            othIntoWhsSubTab.setCntnTaxAmt(
                                    othIntoWhsSubTab.getCntnTaxAmt().add(dbzi.getCntnTaxPrftLossAmts().abs()));// ��˰���
                            // ��˰ӯ�����
                            othIntoWhsSubTab.setUnTaxAmt(othIntoWhsSubTab.getUnTaxAmt().add(dbzi.getUnTaxPrftLossAmts().abs()));// δ˰���
                            // δ˰ӯ�����
                            othIntoWhsSubTab.setTaxAmt(othIntoWhsSubTab.getTaxAmt().add(dbzi.getPrftLossTaxAmts().abs()));//  ӯ��˰���
                            IntoMap.put(dbziKey, othIntoWhsSubTab);
                        } else {
                            OthOutIntoWhsSubTab othIntoWhsSubTab = new OthOutIntoWhsSubTab();
                            othIntoWhsSubTab.setFormNum(number);
                            othIntoWhsSubTab.setInvtyEncd(dbzi.getInvtyEncd());// �������
                            othIntoWhsSubTab.setQty(dbzi.getPrftLossQtys().abs());// ��������
                            othIntoWhsSubTab.setTaxRate(dbzi.getTaxRate().abs());// ˰��
                            othIntoWhsSubTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());// ��˰����
                            othIntoWhsSubTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());// δ˰����[����(�������δ��뵥��)]
                            othIntoWhsSubTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// ��˰��� ��˰ӯ�����
                            othIntoWhsSubTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// δ˰��� δ˰ӯ�����
                            othIntoWhsSubTab.setTaxAmt(dbzi.getPrftLossTaxAmts().abs());//˰��  ����˰��
                            othIntoWhsSubTab.setBatNum(dbzi.getBatNum());// ����
                            othIntoWhsSubTab.setPrdcDt(dbzi.getPrdcDt());// ��������
                            othIntoWhsSubTab.setBaoZhiQi(dbzi.getBaoZhiQi());// ������
                            othIntoWhsSubTab.setInvldtnDt(dbzi.getInvldtnDt());// ʧЧ����
                            othIntoWhsSubTab.setBxRule(dbzi.getBxRule());
                            othIntoWhsSubTab.setGdsBitEncd(dbziKey);
                            othIntoWhsSubTab.setProjEncd(dbzi.getProjEncd());

                            IntoMap.put(dbziKey, othIntoWhsSubTab);
                        }
                        // �Ƿ��λ����
                        if (isNtPrgrGds) {
                            MovBitTab bitTab = new MovBitTab();
                            bitTab.setWhsEncd(checkPrftLoss.getWhsEncd());// �ֿ����
                            bitTab.setInvtyEncd(dbzi.getInvtyEncd());
                            bitTab.setBatNum(dbzi.getBatNum());
                            bitTab.setQty(dbzi.getPrftLossQtys().abs());// ������
                            bitTab.setPrdcDt(dbzi.getPrdcDt());
                            bitTab.setGdsBitEncd(dbzi.getGdsBitEncd());

                            bitTab.setRegnEncd(
                                    whsDocMapper.selectWhsGdsReal(checkPrftLoss.getWhsEncd(), dbzi.getGdsBitEncd()));

                            bitTab.setGdsBitEncd1(dbziKey);

                            movBitTabInto.add(bitTab);
                        }

                    }

                    // �� ����
                    if (dbzi.getPrftLossQtys().doubleValue() < 0) {
                        // �޸Ŀ����������

                        // ������=======
                        if (numberc == null) {
                            try {
                                numberc = getOrderNo.getSeqNo("OTCR", userId,loginTime);
                            } catch (Exception e) {

                                throw new RuntimeException("��ȡ������ʧ�� ");
                            } // ��ȡ������
                            othOutWhs.setFormNum(numberc);// ������
                            othOutWhs.setFormDt(formDate);
                            othOutWhs.setOutIntoWhsTypId("7");// �̿���������
                            othOutWhs.setOutStatus("������");// ����״̬
                            othOutWhs.setSetupPers(userName);// ������
                            othOutWhs.setWhsEncd(checkPrftLoss.getWhsEncd());// ת���ֿ����
                            othOutWhs.setSrcFormNum(checkPrftLoss.getCheckFormNum());// ��Դ���ݺ�
                            othOutWhs.setMemo(checkPrftLoss.getMemo());
                            othOutWhs.setFormTypEncd("015");
                            othOutWhs.setIsNtChk(0);
                        }

                        // ������
                        if (OutMap.containsKey(dbziKey)) {
                            OthOutIntoWhsSubTab othOutWhsSubTab = OutMap.get(dbziKey);
                            othOutWhsSubTab.setQty(othOutWhsSubTab.getQty().add(dbzi.getPrftLossQtys().abs()));// ��������dbzi.getPrftLossQtys().abs()
                            othOutWhsSubTab.setCntnTaxAmt(othOutWhsSubTab.getCntnTaxAmt().add(dbzi.getCntnTaxPrftLossAmts().abs()));// ��˰���
                            // ��˰ӯ�����
                            othOutWhsSubTab.setUnTaxAmt(othOutWhsSubTab.getUnTaxAmt().add(dbzi.getUnTaxPrftLossAmts().abs()));// δ˰���
                            // δ˰ӯ�����
                            othOutWhsSubTab.setTaxAmt(othOutWhsSubTab.getTaxAmt().add(dbzi.getPrftLossTaxAmts().abs()));//  ӯ��˰���
                            OutMap.put(dbziKey, othOutWhsSubTab);

                        } else {
                            OthOutIntoWhsSubTab othOutWhsSubTab = new OthOutIntoWhsSubTab();
                            othOutWhsSubTab.setFormNum(numberc);
                            othOutWhsSubTab.setInvtyEncd(dbzi.getInvtyEncd());
                            othOutWhsSubTab.setQty(dbzi.getPrftLossQtys().abs());
                            othOutWhsSubTab.setTaxRate(dbzi.getTaxRate());
                            othOutWhsSubTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());
                            othOutWhsSubTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());
                            othOutWhsSubTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// ��˰��� cntnTaxPrftLossAmt
                            othOutWhsSubTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// δ˰ӯ����� getUnTaxPrftLossAmt
                            othOutWhsSubTab.setTaxAmt(dbzi.getPrftLossTaxAmts().abs());//˰��  ����˰��
                            othOutWhsSubTab.setBatNum(dbzi.getBatNum());
                            othOutWhsSubTab.setPrdcDt(dbzi.getPrdcDt());
                            othOutWhsSubTab.setBaoZhiQi(dbzi.getBaoZhiQi());
                            othOutWhsSubTab.setInvldtnDt(dbzi.getInvldtnDt());
                            othOutWhsSubTab.setBxRule(dbzi.getBxRule());
                            othOutWhsSubTab.setGdsBitEncd(dbziKey);
                            othOutWhsSubTab.setProjEncd(dbzi.getProjEncd());

                            OutMap.put(dbziKey, othOutWhsSubTab);
                        }
                        if (isNtPrgrGds) {
                            MovBitTab bitTab = new MovBitTab();
                            bitTab.setWhsEncd(checkPrftLoss.getWhsEncd());// �ֿ����
                            bitTab.setInvtyEncd(dbzi.getInvtyEncd());
                            bitTab.setBatNum(dbzi.getBatNum());
                            bitTab.setQty(dbzi.getPrftLossQtys().abs());// ������
                            bitTab.setPrdcDt(dbzi.getPrdcDt());
                            bitTab.setGdsBitEncd(dbzi.getGdsBitEncd());
                            bitTab.setRegnEncd(
                                    whsDocMapper.selectWhsGdsReal(checkPrftLoss.getWhsEncd(), dbzi.getGdsBitEncd()));

                            bitTab.setGdsBitEncd1(dbziKey);

                            movBitTabOut.add(bitTab);
                        }

                    }

                }
                othIntoWhsSubTabs = new ArrayList<OthOutIntoWhsSubTab>(IntoMap.values());
                othOutWhsSubTabs = new ArrayList<OthOutIntoWhsSubTab>(OutMap.values());
                for (OthOutIntoWhsSubTab tab : othIntoWhsSubTabs) {
                    if (!(tab.getBxRule() == null || tab.getBxRule().compareTo(BigDecimal.ZERO) == 0)) {
                        tab.setBxQty(tab.getQty().divide(tab.getBxRule(), 8, BigDecimal.ROUND_HALF_UP));
                    }
                }
                for (OthOutIntoWhsSubTab tab : othOutWhsSubTabs) {
                    if (!(tab.getBxRule() == null || tab.getBxRule().compareTo(BigDecimal.ZERO) == 0)) {
                        tab.setBxQty(tab.getQty().divide(tab.getBxRule(), 8, BigDecimal.ROUND_HALF_UP));
                    }
                }

                if (othIntoWhsSubTabs.size() > 0) {
                    othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);// ���������
                    othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);// ���������

                    if (movBitTabInto.size() > 0) {
                        List<String> strings = new ArrayList();
                        strings.add(othIntoWhs.getFormNum());
                        bitListMapper.deleteInvtyGdsBitList(strings);
                        for (OthOutIntoWhsSubTab intoWhsSubTab : othIntoWhsSubTabs) {
                            for (MovBitTab bitTab : movBitTabInto) {
                                bitTab.setOrderNum(othIntoWhs.getFormNum());
                                if (intoWhsSubTab.getGdsBitEncd().equals(bitTab.getGdsBitEncd1())) {
                                    bitTab.setSerialNum(intoWhsSubTab.getOrdrNum().toString());
                                }
                            }
                        }
                        bitListMapper.insertInvtyGdsBitLists(movBitTabInto);
                    }
//					JSONObject object = new JSONObject();
//					object.put("formNum", othIntoWhs.getFormNum());
//					object.put("chkr", userId);

                    ObjectNode object;
                    try {
                        object = JacksonUtil.getObjectNode("");
                        object.put("formNum", othIntoWhs.getFormNum());
                        object.put("chkr", userName);
                        intoWhsServiceImpl.updateOutInWhsChk(userName, object.toString(),loginTime);
                    } catch (IOException e) {

                    }
                    message = "����" + danHao + "������˳ɹ�";

                }
                if (othOutWhsSubTabs.size() > 0) {
                    othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);// ����������
                    othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);// ����������
                    if (movBitTabOut.size() > 0) {
                        List<String> strings = new ArrayList();
                        strings.add(othOutWhs.getFormNum());
                        bitListMapper.deleteInvtyGdsBitList(strings);

                        for (OthOutIntoWhsSubTab intoWhsSubTab : othOutWhsSubTabs) {
                            for (MovBitTab bitTab : movBitTabOut) {
                                bitTab.setOrderNum(othOutWhs.getFormNum());
                                if (intoWhsSubTab.getGdsBitEncd().equals(bitTab.getGdsBitEncd1())) {
                                    bitTab.setSerialNum(intoWhsSubTab.getOrdrNum().toString());
                                }
                            }
                        }
                        bitListMapper.insertInvtyGdsBitLists(movBitTabOut);
                    }

//					JSONObject object = new JSONObject();
                    ObjectNode object;
                    try {
                        object = JacksonUtil.getObjectNode("");
                        object.put("formNum", othOutWhs.getFormNum());
                        object.put("chkr", userName);
                        intoWhsServiceImpl.updateOutInWhsChk(userName, object.toString(),loginTime);
                    } catch (IOException e) {

                    }
                    message = "����" + danHao + "������˳ɹ�";
                }
                if (othOutWhsSubTabs.size() == 0 && othIntoWhsSubTabs.size() == 0) {
                    message = "����" + danHao + "��˳ɹ�����û���������";
                }

            } else {
                throw new RuntimeException("����" + danHao + "���ʧ�ܣ�");
            }

        }

        return message;
    }

    // ����
    @Override
    public String updateCSnglLossNoChk(String userId, String jsonBody) {
        String message = "";
        Boolean isSuccess = true;

        ObjectNode jdRespJson = null;
        try {
            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }
        String danHao = jdRespJson.get("formNum").asText();

        List<CheckPrftLoss> cPrft = new ArrayList<>();// �̵���������
        List<CheckPrftLossSubTab> cPrftSubTab = new ArrayList<>();// �̵������ӱ�
        List<InvtyTab> invtyTabs = new ArrayList<>();// ����list

        cPrft = new ArrayList<>();// �̵���������
        cPrftSubTab = new ArrayList<>();// �̵������ӱ�
        invtyTabs = new ArrayList<>();// ����list

        // �̵���������
        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectIsChr(danHao);
        if (checkPrftLoss == null) {
            throw new RuntimeException("����:" + danHao + "������ ");

        }
        if (checkPrftLoss.getIsNtChk() == 0) {
            throw new RuntimeException("����:" + danHao + "������,����Ҫ�ظ����� ");

        }

        // ��ѯ�Ƿ������������
        List<OthOutIntoWhs> count = cannibSnglMapper.selectOthIsDelete(danHao);

        if (count.size() > 0) {
            throw new RuntimeException("���ţ�" + danHao + "��Ӧ�����������δɾ��������ɾ��");
        }
        // ��ɾ���������������ݣ������������
        cPrft.add(checkPrftLoss);

        // ��������״̬
        int a = checkPrftLossMapper.updateCSnglLossNoChk(cPrft);

        if (a >= 1) {
            message = "����:" + danHao + "����ɹ�";
//			cPrftSubTab = checkPrftLossMapper.selectCheckSnglLossSubTab(danHao);
//			for (CheckPrftLossSubTab dbzi : cPrftSubTab) {
//				// ����
//				InvtyTab invtyTab = new InvtyTab();
//				invtyTab.setWhsEncd(checkPrftLoss.getWhsEncd());
//				// ����
//				invtyTab.setInvtyEncd(dbzi.getInvtyEncd());
//				invtyTab.setBatNum(dbzi.getBatNum());
//				invtyTab.setAvalQty(dbzi.getPrftLossQtys().abs());// ������
//				invtyTab.setNowStok(dbzi.getPrftLossQtys().abs());// �ִ���
//				invtyTab.setPrdcDt(dbzi.getPrdcDt());// ��������
//				invtyTab.setBaoZhiQi(dbzi.getBaoZhiQi());
//				invtyTab.setInvldtnDt(dbzi.getInvldtnDt());// ʧЧ����
////				BigDecimal cntnTaxUprc = new BigDecimal(dbzi.get("cntnTaxUprc").asText());
//				invtyTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());
////				BigDecimal unTaxUprc = new BigDecimal(dbzi.get("unTaxUprc").asText());
//				invtyTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());
////				BigDecimal taxRate = new BigDecimal(dbzi.get("taxRate").asText());
//				invtyTab.setTaxRate(dbzi.getTaxRate().abs());
////				BigDecimal cntnTaxAmt = new BigDecimal(dbzi.get("cntnTaxPrftLossAmts").asText());
//				invtyTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// cntnTaxPrftLossAmts;��˰��������
////				BigDecimal unTaxAmt = new BigDecimal(dbzi.get(   "unTaxPrftLossAmts").asText());
//				invtyTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// δ˰��������unTaxPrftLossAmts
//				// ������� ��λ���� (ͨ���ֿ���롢������롢���Ų�ѯ��ѯ����λ���� ͨ����λ�����ѯ�ֿ����)
//				invtyTabs.add(invtyTab);
//
//				double prftLossQtys = dbzi.getPrftLossQtys().doubleValue();
//				// ӯ ���
//				if (prftLossQtys > 0) {
//
//					// �޸Ŀ����������
//					for (InvtyTab iTab : invtyTabs) {
//					}
//					/// ��ѯ����
//					InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
//					for (CheckPrftLossSubTab cTab : cPrftSubTab) {
//					}
//					if ((inTab.getAvalQty().compareTo(dbzi.getPrftLossQtys()) == 1
//							|| inTab.getAvalQty().compareTo(dbzi.getPrftLossQtys()) == 0)
//							&& (inTab.getNowStok().compareTo(dbzi.getPrftLossQtys()) == 1
//									|| inTab.getNowStok().compareTo(dbzi.getPrftLossQtys()) == 0)) {
//
//						invtyNumMapper.updateAInvtyTab(invtyTabs);// �޸Ŀ��� ����������
//						invtyNumMapper.updateInvtyTabAmt(invtyTabs);// �޸Ŀ��� �ִ�������
//						// �޸Ŀ����ûд SQL
//						invtyTabs.clear();
//						message += checkPrftLoss.getCheckFormNum() + " ��������ɹ���";
//
//					} else
//
//					if (inTab.getAvalQty().compareTo(dbzi.getPrftLossQtys()) == -1
//							|| inTab.getNowStok().compareTo(dbzi.getPrftLossQtys()) == -1) {
//						message = inTab.getInvtyEncd() + "������������� ";
//						try {
//							throw new RuntimeException(danHao+"����"+inTab.getBatNum()+"����"+inTab.getInvtyEncd() + "  ������������� ������" + dbzi.getPrftLossQtys());
//						} catch (Exception e) {
//							
//							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // �ֶ��ع�ʧ��
//							return message;
//						}
//
//					}
//
//				}
//
//				// �� ����
//				if (prftLossQtys < 0) {
//					// System.out.println("�� ����");
//					// �޸Ŀ����������
//					if (invtyNumMapper.selectInvtyTabByTerm(invtyTab) != null) {
//						// ���������� �����ڿ� �޸Ŀ�������
//						invtyNumMapper.updateAInvtyTabJia(invtyTabs);// �޸Ŀ��� ����������
//						invtyNumMapper.updateInvtyTabAmtJia(invtyTabs);// �޸Ŀ��� �ִ�������
//						invtyTabs.clear();
//						// �޸Ŀ����ûд SQL
//					} else {
//						// �������� ���
//						invtyNumMapper.insertInvtyTabList(invtyTabs);
//						invtyTabs.clear();
//					}
//				}
//
//			}

        } else {
            throw new RuntimeException("���ݣ�" + danHao + "��������ʧ�ܣ�");

        }

        return message;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("checkFormNum", getList((String) map.get("checkFormNum")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("srcFormNum", getList((String) map.get("srcFormNum")));
        map.put("setupPers", getList((String) map.get("setupPers")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<CheckPrftLossMap> aList = checkPrftLossMapper.selectList(map);
//		List<CheckPrftLoss> aList = checkPrftLossMapper.selectListDaYin(map);
        aList.add(new CheckPrftLossMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/check_sngl_loss/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    private boolean isNtPrgrGdsBitMgmt(String whsEncd) {
        Integer whs = whsDocMapper.selectisNtPrgrGdsBitMgmtWhs(whsEncd);
        if (whs != null && whs.compareTo(1) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, CheckPrftLoss> pusOrderMap = uploadScoreInfoU8(file);

        for (Map.Entry<String, CheckPrftLoss> entry : pusOrderMap.entrySet()) {

            CheckPrftLoss cannibSngl = checkPrftLossMapper.selectCheckSnglLoss(entry.getValue().getCheckFormNum());

            if (cannibSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getCheckFormNum() + " ����");

            }
            try {
                checkPrftLossMapper.exInsertCSngl(entry.getValue());

                checkPrftLossMapper.insertCheckSnglLossSubTab(entry.getValue().getcPrftLossList());

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

    private Map<String, CheckPrftLoss> uploadScoreInfoU8(MultipartFile file) {
        Map<String, CheckPrftLoss> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "�̵㵥��");
                // ����ʵ����
                // System.out.println(orderNo);
                CheckPrftLoss checkSngl = new CheckPrftLoss();
                if (temp.containsKey(orderNo)) {
                    checkSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//				// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������

                checkSngl.setSrcFormNum(orderNo);// ��Դ���ݺ�
                checkSngl.setCheckFormNum(orderNo);// �̵㵥��
                checkSngl.setFormTypEncd("029");

                checkSngl.setCheckDt((GetCellData(r, "�̵�����") == null || GetCellData(r, "�̵�����").equals("")) ? null
                        : GetCellData(r, "�̵�����").replaceAll("[^0-9:-]", " "));// �̵�����
                checkSngl.setBookDt((GetCellData(r, "�̵�����") == null || GetCellData(r, "�̵�����").equals("")) ? null
                        : GetCellData(r, "�̵�����").replaceAll("[^0-9:-]", " "));// ��������q
                checkSngl.setWhsEncd(GetCellData(r, "�̵�ֿ����"));// �ֿ����
                checkSngl.setCheckBat(GetCellData(r, "�̵�����"));// �̵�����
                checkSngl.setSetupPers(GetCellData(r, "�Ƶ���")); // ������q
                checkSngl.setSetupTm((GetCellData(r, "�Ƶ�ʱ��") == null || GetCellData(r, "�Ƶ�ʱ��").equals("")) ? null
                        : GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " ")); // ����ʱ��q
                checkSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���q
                checkSngl.setModiTm((GetCellData(r, "�޸�ʱ��") == null || GetCellData(r, "�޸�ʱ��").equals("")) ? null
                        : GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ")); // �޸�ʱ��q
                checkSngl.setChkr(GetCellData(r, "�����")); // �����q
                checkSngl.setChkTm((GetCellData(r, "���ʱ��") == null || GetCellData(r, "���ʱ��").equals("")) ? null
                        : GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " ")); // ���ʱ��q
                checkSngl.setMemo(GetCellData(r, "��ע"));// ��ע

                checkSngl.setBookEntryPers(null);// ������

                checkSngl.setBookEntryTm(null);// ����ʱ��
                checkSngl.setIsNtChk(0);// �Ƿ����
                checkSngl.setIsNtBookEntry(0);// �Ƿ����
                checkSngl.setIsNtCmplt(0);// �Ƿ����
                checkSngl.setIsNtClos(0);// �Ƿ�ر�
                checkSngl.setPrintCnt(
                        new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
                                : GetCellData(r, "��ӡ����")).intValue());// ��ӡ����q
                checkSngl.setIsNtWms(0);// �Ƿ���WMS�ϴ�q
//				// System.out.println("isNtWms"+isNtWms);
                List<CheckPrftLossSubTab> checkSnglSubList = checkSngl.getcPrftLossList();// �����ӱ�
                if (checkSnglSubList == null) {
                    checkSnglSubList = new ArrayList<>();
                }

                CheckPrftLossSubTab checkSnglSubTab = new CheckPrftLossSubTab();

                checkSnglSubTab.setCheckFormNum(orderNo);// �̵㵥��
                checkSnglSubTab.setInvtyEncd(GetCellData(r, "�������")); // �������
                checkSnglSubTab.setBatNum(GetCellData(r, "����")); // ����

                checkSnglSubTab.setInvtyBigClsEncd(null); // ����������
                checkSnglSubTab.setPrdcDt((GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("")) ? null
                        : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ")); // ��������1
                String BaoZhiQi = GetCellData(r, "������");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                checkSnglSubTab.setBaoZhiQi(BaoZhiQi); // ������1

                checkSnglSubTab
                        .setInvldtnDt((GetCellData(r, "ʧЧ����") == null || GetCellData(r, "ʧЧ����").equals("")) ? null
                                : GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " ")); // ʧЧ����1
                checkSnglSubTab.setBookQty(
                        new BigDecimal(GetCellData(r, "��������") == null || GetCellData(r, "��������").equals("") ? "0"
                                : GetCellData(r, "��������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ��������1
                checkSnglSubTab.setAdjOutWhsQty(
                        new BigDecimal(GetCellData(r, "������������") == null || GetCellData(r, "������������").equals("") ? "0"
                                : GetCellData(r, "������������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ������������1
                checkSnglSubTab.setAdjIntoWhsQty(
                        new BigDecimal(GetCellData(r, "�����������") == null || GetCellData(r, "�����������").equals("") ? "0"
                                : GetCellData(r, "�����������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // �����������1
                // ��������+�����������- ������������
                checkSnglSubTab.setBookAdjustQty(checkSnglSubTab.getBookQty()
                        .add(checkSnglSubTab.getAdjIntoWhsQty().subtract(checkSnglSubTab.getAdjOutWhsQty()))); // �����������?
//				new BigDecimal(GetCellData(r, "�����������") == null || GetCellData(r, "�����������").equals("") ? "0"
//				: GetCellData(r, "�����������")).setScale(8, BigDecimal.ROUND_HALF_UP)

                checkSnglSubTab.setCheckQty(
                        new BigDecimal(GetCellData(r, "�̵�����") == null || GetCellData(r, "�̵�����").equals("") ? "0"
                                : GetCellData(r, "�̵�����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // �̵�����1
                checkSnglSubTab.setPrftLossQty(
                        new BigDecimal(GetCellData(r, "ӯ������") == null || GetCellData(r, "ӯ������").equals("") ? "0"
                                : GetCellData(r, "ӯ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ӯ������1
                checkSnglSubTab.setPrftLossQtys(
                        new BigDecimal(GetCellData(r, "ӯ������") == null || GetCellData(r, "ӯ������").equals("") ? "0"
                                : GetCellData(r, "ӯ������")).setScale(8, BigDecimal.ROUND_HALF_UP));
                checkSnglSubTab.setPrftLossRatio(
                        new BigDecimal(GetCellData(r, "ӯ������%") == null || GetCellData(r, "ӯ������%").equals("") ? "0"
                                : GetCellData(r, "ӯ������%")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ӯ������(%)1
                checkSnglSubTab.setReasn(GetCellData(r, "ԭ��")); // ԭ��1
                checkSnglSubTab.setTaxRate(BigDecimal.ZERO);

                checkSnglSubTab.setCntnTaxUprc(BigDecimal.ZERO);// ��˰����

                checkSnglSubTab.setUnTaxUprc(new BigDecimal(
                        GetCellData(r, "����") == null || GetCellData(r, "����").equals("") ? "0" : GetCellData(r, "����"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP));// δ˰����

                checkSnglSubTab.setCntnTaxBookAmt(BigDecimal.ZERO);// ��˰������

                checkSnglSubTab.setUnTaxBookAmt(
                        new BigDecimal(GetCellData(r, "������") == null || GetCellData(r, "������").equals("") ? "0"
                                : GetCellData(r, "������")).setScale(8, BigDecimal.ROUND_HALF_UP));// δ˰������

                checkSnglSubTab.setCntnTaxCheckAmt(BigDecimal.ZERO);// ��˰�̵���

                checkSnglSubTab.setUnTaxCheckAmt(
                        new BigDecimal(GetCellData(r, "�̵���") == null || GetCellData(r, "�̵���").equals("") ? "0"
                                : GetCellData(r, "�̵���")).setScale(8, BigDecimal.ROUND_HALF_UP));// δ

                checkSnglSubTab.setCntnTaxPrftLossAmt(BigDecimal.ZERO);// ��˰ӯ�����

                checkSnglSubTab.setUnTaxPrftLossAmt(
                        new BigDecimal(GetCellData(r, "ӯ�����") == null || GetCellData(r, "ӯ�����").equals("") ? "0"
                                : GetCellData(r, "ӯ�����")).setScale(8, BigDecimal.ROUND_HALF_UP));// δ˰ӯ�����

                checkSnglSubTab.setCntnTaxPrftLossAmts(BigDecimal.ZERO);// ��˰������

                checkSnglSubTab.setUnTaxPrftLossAmts(
                        new BigDecimal(GetCellData(r, "ӯ�����") == null || GetCellData(r, "ӯ�����").equals("") ? "0"
                                : GetCellData(r, "ӯ�����")).setScale(8, BigDecimal.ROUND_HALF_UP));// δ˰������

                checkSnglSubTab.setGdsBitEncd(null); // ��λ����
                checkSnglSubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                checkSnglSubList.add(checkSnglSubTab);

                checkSngl.setcPrftLossList(checkSnglSubList);
                temp.put(orderNo, checkSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());

        }
        return temp;
    }
}
