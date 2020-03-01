package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.whs.entity.ExpressCorpMap;
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
import com.px.mis.whs.dao.CheckPrftLossMapper;
import com.px.mis.whs.dao.CheckSnglMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.CheckPrftLoss;
import com.px.mis.whs.entity.CheckPrftLossSubTab;
import com.px.mis.whs.entity.CheckSngl;
import com.px.mis.whs.entity.CheckSnglMap;
import com.px.mis.whs.entity.CheckSnglSubTab;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.service.CheckSnglService;

@Service
@Transactional
public class CheckSnglServiceImpl extends poiTool implements CheckSnglService {

    @Autowired
    CheckSnglMapper checkSnglMapper;
    // ������
    @Autowired
    GetOrderNo getOrderNo;

    @Autowired
    CheckPrftLossMapper checkPrftLossMapper;

    @Autowired
    InvtyNumMapper invtyNumMapper;
    @Autowired
    IntoWhsDao intoWhsDao;

    @Autowired
    InvtyTabDao invtyTabDao;
    @Autowired
    InvtyDocDao invtyDocDao;
    /**
     * �ֿ�
     */
    @Autowired
    WhsDocMapper whsDocMapper;

    // �����̵㵥
    @Override
    public String insertCheckSngl(String userId, CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList, String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            String number = getOrderNo.getSeqNo("PD", userId, loginTime);// ��ȡ������

            if (checkSnglMapper.selectCheckSngl(number) != null) {
                message = "���" + number + "�Ѵ��ڣ����������룡";
                isSuccess = false;
            } else {
                if (checkSnglMapper.selectCheckSnglSubTab(number).size() > 0) {
                    checkSnglMapper.deleteCheckSnglSubTab(number);
                }

                cSngl.setFormTypEncd("028");
                cSngl.setCheckFormNum(number);// ���ݺ�
                checkSnglMapper.insertCheckSngl(cSngl);
                for (CheckSnglSubTab cSubTab : cSubTabList) {
                    cSubTab.setCheckFormNum(cSngl.getCheckFormNum());// �����ݺ�
                    cSubTab.setInvldtnDt((cSubTab.getInvldtnDt() == null || cSubTab.getInvldtnDt().equals("")) ? null
                            : cSubTab.getInvldtnDt());
                    cSubTab.setPrdcDt((cSubTab.getPrdcDt() == null || cSubTab.getPrdcDt().equals("")) ? null
                            : cSubTab.getPrdcDt());

                    InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                            .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd() + "�ô��������"));
                    Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
                    if (sisQuaGuaPer == 1) {
                        if (StringUtils.isBlank(cSubTab.getBaoZhiQi()) || StringUtils.isBlank(cSubTab.getPrdcDt())
                                || StringUtils.isBlank(cSubTab.getInvldtnDt())) {
                            throw new RuntimeException(cSubTab.getInvtyEncd() + "�����ڹ������������ô���");
                        }
                    } else {
                        cSubTab.setBaoZhiQi(null);
                        cSubTab.setPrdcDt(null);
                        cSubTab.setInvldtnDt(null);
                    }
                }
                checkSnglMapper.insertCheckSnglSubTab(cSubTabList);
//				for (CheckSnglSubTab cSubTab : cSubTabList) {
//					// System.out.println(cSubTab.getOrdrNum());
//
//					for (CheckGdsBit asdas : cSubTab.getCheckGdsBit()) {
//						// System.out.println(asdas);
//
//					}
//				}

            }

//					for (CheckGdsBit asdas : cSubTab.getCheckGdsBit()) {
//						// System.out.println(asdas);
//
//					}
            message = "�����ɹ���";
            isSuccess = true;
            resp = BaseJson.returnRespObj("whs/check_sngl/insertCheckSngl", isSuccess, message, cSngl);
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
        return resp;
    }

    // �޸��̵㵥
    @Override
    public String updateCheckSngl(CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(cSngl.getCheckFormNum());
        if (checkSngl == null) {
            throw new RuntimeException("���Ų�����");
        } else if (checkSngl.getIsNtChk() == null) {
            throw new RuntimeException("�������״̬�쳣");
        } else if (checkSngl.getIsNtChk() == 1) {
            throw new RuntimeException("��������˲������޸�");
        }
        for (CheckSnglSubTab cSubTab : cSubTabList) {
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                    .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd() + "�ô��������"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("�Ƿ����ڹ�������Ϊ��"));
            if (sisQuaGuaPer == 1) {
                if (StringUtils.isBlank(cSubTab.getBaoZhiQi()) || StringUtils.isBlank(cSubTab.getPrdcDt())
                        || StringUtils.isBlank(cSubTab.getInvldtnDt())) {
                    throw new RuntimeException(cSubTab.getInvtyEncd() + "�����ڹ������������ô���");
                }
            } else {
                cSubTab.setBaoZhiQi(null);
                cSubTab.setPrdcDt(null);
                cSubTab.setInvldtnDt(null);
            }
        }
        checkSnglMapper.deleteCheckSnglSubTab(cSngl.getCheckFormNum());
        checkSnglMapper.updateCheckSngl(cSngl);
        checkSnglMapper.insertCheckSnglSubTab(cSubTabList);
        message = "���³ɹ���";

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/updateCheckSngl", isSuccess, message, cSngl);
        } catch (IOException e) {

        }
        return resp;
    }

    // ����ɾ��
    @Override
    public String deleteAllCheckSngl(String checkFormNum) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<String> list = getList(checkFormNum);
        List<CheckSngl> checkSngls = checkSnglMapper.selectCheckSnglsList(list);
        List<String> lists = new ArrayList<String>();
        List<String> lists2 = new ArrayList<String>();
        List<String> lists3 = new ArrayList<String>();

        if (checkSngls.size() > 0) {
            for (CheckSngl sngl : checkSngls) {
                if (sngl.getIsNtChk() == 0) {
                    lists.add(sngl.getCheckFormNum());
                } else if (sngl.getIsNtChk() == 1) {
                    lists2.add(sngl.getCheckFormNum());
                } else {
                    lists3.add(sngl.getCheckFormNum());
                }
            }

            if (lists.size() > 0) {
                checkSnglMapper.insertCheckSnglDl(lists);
                checkSnglMapper.insertCheckSnglSubTabDl(lists);

                checkSnglMapper.deleteAllCheckSngl(lists);
                message = "ɾ���ɹ���" + lists.toString();
                isSuccess = true;
            } else if (lists2.size() > 0) {
                isSuccess = false;
                message = message + "\r�������" + lists2;
            }

            if (lists3.size() > 0) {
                message = message + "\r��Ų����ڣ�" + lists3;
            }
        } else {
            isSuccess = false;
            message = "���" + checkFormNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/deleteAllCheckSngl", isSuccess, message, null);
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
    public String query(String checkFormNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<CheckSnglSubTab> cSubTabList = new ArrayList<>();
        CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(checkFormNum);
        if (checkSngl != null) {
            cSubTabList = checkSnglMapper.selectCheckSnglSubTab(checkFormNum);
            message = "��ѯ�ɹ���";
        } else {
            isSuccess = false;
            message = "���" + checkFormNum + "�����ڣ�";
        }

        try {
            resp = BaseJson.returnRespObjList("whs/check_sngl/query", isSuccess, message, checkSngl, cSubTabList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String queryList(Map map) {
        String resp = "";

//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("setupPers", getList((String) map.get("setupPers")));
        map.put("checkFormNum", getList((String) map.get("checkFormNum")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<CheckSnglMap> aList = checkSnglMapper.selectList(map);
        int count = checkSnglMapper.selectCount(map);

        int pageNo = Integer.parseInt(map.get("pageNo").toString());
        int pageSize = Integer.parseInt(map.get("pageSize").toString());
        int listNum = aList.size();
        int pages = count / pageSize;
        if (count % pageSize > 0) {
            pages += 1;
        }
        try {
            resp = BaseJson.returnRespList("whs/check_sngl/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // ͨ���ֿ⡢�̵����š����������롢����Ϊ��ʱ�Ƿ��̵� �б�
    @Override
    public String selectCheckSnglList(Map map) throws IOException {
        String resp = "";

//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
//        map.put("regnEncd", getList((String) map.get("regnEncd")));// �������
//        map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));// ��λ����
        map.put("batNum", getList((String) map.get("batNum")));
        String Qty = map.get("Qty").toString();
        if (isNtPrgrGdsBitMgmt(map.get("whsEncd").toString())) {
            // ��λ
            List<CheckSngl> cList = checkSnglMapper.selectCheckSnglListZero(map);
            if (cList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", true, "����ɹ���", null, cList);
            } else {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", false, "����ʧ�ܣ����������", null,
                        cList);
            }
        } else {
            // �ֿ�
            List<CheckSngl> cList = checkSnglMapper.selectCheckSnglList(map);
            if (cList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", true, "����ɹ���", null, cList);
            } else {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", false, "����ʧ�ܣ����������", null,
                        cList);
            }
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

    // PDA ��ѯ���ز���
    @Override
    public String checkSnglList(String whsEncd) throws IOException {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<CheckSngl> cList = checkSnglMapper.checkSnglList(list);
        if (cList.size() > 0) {
            resp = BaseJson.returnRespObjList("whs/check_sngl/checkSnglList", true, "����ɹ���", null, cList);
        } else {
            resp = BaseJson.returnRespObjList("whs/check_sngl/checkSnglList", false, "����ʧ�ܣ�", null, cList);
        }

        return resp;
    }

    // pda �����ӱ����
    @Override
    public String updateCheckTab(CheckSngl checkSngl, List<CheckSnglSubTab> cSubTabList) throws IOException {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        if (checkSngl.getCheckFormNum() == null) {
            isSuccess = false;
            message = "�̵㵥�ݺŲ����ڣ�";
        } else {
            // �޸��̵�״̬(�̵��С��̵����)
            checkSngl.setCheckStatus("�̵����");
            checkSnglMapper.updateCheckStatus(checkSngl);
            // �޸�ʵ����
            checkSnglMapper.updateCheckTab(cSubTabList);
            message = "����ɹ���";

        }

        resp = BaseJson.returnRespObj("whs/check_sngl/updateCheckTab", isSuccess, message, null);

        return resp;
    }

    // ���
    @Override
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate) {
        String message = "";
        String loginTime = formDate;
        ObjectNode dbzhu = null;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);

            String danHao = dbzhu.get("formNum").asText();
            String chkr = userName;// �����

            List<CheckSngl> cSnglList = new ArrayList<>();// �̵���
            CheckPrftLoss checkPrftLoss = new CheckPrftLoss();// �̵�������
            List<CheckSnglSubTab> cSubTabsList;// �̵���
            List<CheckPrftLossSubTab> cSubLossList = new ArrayList<>();// �̵�������
            List<InvtyTab> invtyTabs = new ArrayList<>();// ����list

            CheckSngl cSngl = checkSnglMapper.selectCheckSngl(danHao);
            if (cSngl == null) {
                throw new RuntimeException("����:" + danHao + "������ ");

            }
            cSngl.setChkr(chkr);
            cSngl.setChkTm(loginTime);
            cSnglList.add(cSngl);
            if (cSngl.getIsNtChk() == 1) {
                throw new RuntimeException("���ţ�" + danHao + "����ˣ�����Ҫ�ٴ����");

            }
            int count = checkSnglMapper.updateCSnglChk(cSnglList);
            if (count <= 0) {
                throw new RuntimeException("���ţ�" + danHao + "���ʧ�ܣ�");
            }
            cSubTabsList = checkSnglMapper.selectCheckQty(danHao);
            if (cSubTabsList.size() == 0) {
                throw new RuntimeException("���ţ�" + danHao + "û�б�����Ϣ");
            }
            for (CheckSnglSubTab cSubTab : cSubTabsList) {
                // �ж�ʵ�������Ƿ���д����
                if (cSubTab.getCheckQty() != null) {
                    if (cSubTab.getPrftLossQty().compareTo(BigDecimal.ZERO) != 0) {
                        CheckPrftLossSubTab cPrftLossSubTab = new CheckPrftLossSubTab();
//						cPrftLossSubTab.setCheckFormNum(number);// �̵����浥��
                        cPrftLossSubTab.setInvtyEncd(cSubTab.getInvtyEncd());// �������
                        cPrftLossSubTab.setInvtyBigClsEncd(cSubTab.getInvtyBigClsEncd());// ����������
                        cPrftLossSubTab.setBatNum(cSubTab.getBatNum());// ����
                        cPrftLossSubTab.setPrdcDt(cSubTab.getPrdcDt());// ��������
                        cPrftLossSubTab.setBaoZhiQi(cSubTab.getBaoZhiQi());// ������
                        cPrftLossSubTab.setInvldtnDt(cSubTab.getInvldtnDt());// ʧЧ����
                        cPrftLossSubTab.setBookQty(cSubTab.getBookQty());// ��������
                        cPrftLossSubTab.setCheckQty(cSubTab.getCheckQty());// �̵�����
                        // ������ ӯ����û������
                        cPrftLossSubTab.setPrftLossQty(cSubTab.getPrftLossQty());// ӯ������
                        cPrftLossSubTab.setPrftLossQtys(cSubTab.getPrftLossQty());// ��������
                        cPrftLossSubTab.setBookAdjustQty(cSubTab.getBookAdjustQty());// �����������
                        cPrftLossSubTab.setAdjIntoWhsQty(cSubTab.getAdjIntoWhsQty());// �����������
                        cPrftLossSubTab.setPrftLossRatio(cSubTab.getPrftLossRatio());// ӯ�����ڱ���%
                        cPrftLossSubTab.setAdjOutWhsQty(cSubTab.getAdjOutWhsQty());// ������������
                        cPrftLossSubTab.setReasn(cSubTab.getReasn());// ԭ��
                        cPrftLossSubTab.setGdsBitEncd(cSubTab.getGdsBitEncd());// ��λ
                        cPrftLossSubTab.setProjEncd(cSubTab.getProjEncd());

                        InvtyTab invtyTab = new InvtyTab();
                        invtyTab.setWhsEncd(cSngl.getWhsEncd());
                        // ȡ�����е�˰�ʺ���˰����
                        invtyTab.setInvtyEncd(cSubTab.getInvtyEncd());// �����
                        invtyTab.setBatNum(cSubTab.getBatNum());// ����
                        InvtyTab aTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
                        if (aTab == null) {
                            throw new RuntimeException("���ݣ�" + danHao + "��,�ֿ⣺" + invtyTab.getWhsEncd() + ",�����"
                                    + invtyTab.getInvtyEncd() + ",���ţ�" + invtyTab.getBatNum() + "��Ӧ�Ŀ�治����,���ʧ�ܣ�");
                        }
//						Map<String, Object> map = new HashMap<>();
//						map.put("invtyEncd", cPrftLossSubTab.getInvtyEncd());
//						map.put("whsEncd", checkPrftLoss.getWhsEncd());
//						map.put("batNum", cPrftLossSubTab.getBatNum());
//
//						BigDecimal unTaxUprc = cPrftLossSubTab.getUnTaxUprc();
//
//							// ��ȡδ˰����
//							unTaxUprc = intoWhsDao.selectUnTaxUprc(map);
//							if (unTaxUprc == null) {// �޲ɹ��ɱ��������޿ⵥ��
//								// �������
//								InvtyDoc doc = invtyDocDao.selectInvtyDocByInvtyDocEncd(cPrftLossSubTab.getInvtyEncd());
//								if (doc != null && doc.getRefCost() != null) {
//									unTaxUprc = doc.getRefCost();
//								} else {
//
//								}
//
//							}

//						cPrftLossSubTab.setUnTaxUprc(unTaxUprc== null ? new BigDecimal(0) : unTaxUprc);// δ˰����
                        cPrftLossSubTab.setUnTaxUprc(aTab.getUnTaxUprc());// δ˰����

//						cPrftLossSubTab.setTaxRate(aTab.getTaxRate() == null ? new BigDecimal(0) : aTab.getTaxRate());// ˰��
                        BigDecimal unTaxUprc; // ��˰����
                        BigDecimal checkQtyg; // �̵�����
                        BigDecimal bookQtyg; //  ��������
                        BigDecimal prftLossQtyg; // ӯ������ ��������
                        BigDecimal taxRate; // ˰��
                        // ��ȡδ˰����
                        unTaxUprc = cPrftLossSubTab.getUnTaxUprc();
                        if (unTaxUprc == null) {
                            throw new RuntimeException(cSngl.getWhsEncd() + "�ֿ�" + cSubTab.getInvtyEncd() + "���" + cSubTab.getBatNum() + "������˰����Ϊ��");
                        }
                        InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd());
                        if (invtyDoc == null) {
                            throw new RuntimeException("��������в����ڣ�" + invtyTab.getInvtyEncd());
                        }
                        //��涼ȡ����˰��
                        if (invtyDoc.getIptaxRate() == null) {
                            throw new RuntimeException(cSubTab.getInvtyEncd() + "���������˰��Ϊ��");
                        }
                        cPrftLossSubTab.setTaxRate(invtyDoc.getIptaxRate());

                        // ��ȡδ˰����
                        checkQtyg = cPrftLossSubTab.getCheckQty();
                        bookQtyg = cPrftLossSubTab.getBookQty();
//						bookQtyg=cPrftLossSubTab.getBookAdjustQty();
                        prftLossQtyg = cPrftLossSubTab.getPrftLossQty();
                        // ��ȡ˰�� ҳ��˰��δ������������Ҫ��˰��/100
//                        if (cPrftLossSubTab.getTaxRate() == null) {
//                            cPrftLossSubTab.setTaxRate(BigDecimal.ONE);
//                        }
                        taxRate = cPrftLossSubTab.getTaxRate().divide(new BigDecimal(100));
//						// ���㺬˰���� ��˰����=��˰����*˰��+��˰����
//						cPrftLossSubTab.setCntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc, checkQtyg, taxRate));
//						// δ˰������ δ˰��� =δ˰����*δ˰����
//						cPrftLossSubTab.setUnTaxBookAmt(CalcAmt.noTaxAmt(unTaxUprc, bookQtyg));
//						// ��˰������ ��˰���=��˰���*˰��+��˰���=˰��+��˰���
//						cPrftLossSubTab.setCntnTaxBookAmt(CalcAmt.prcTaxSum(unTaxUprc, bookQtyg, taxRate));
//						// δ˰�̵���
//						cPrftLossSubTab.setUnTaxCheckAmt(CalcAmt.noTaxAmt(unTaxUprc, checkQtyg));
//						// ��˰�̵���
//						cPrftLossSubTab.setCntnTaxCheckAmt(CalcAmt.prcTaxSum(unTaxUprc, checkQtyg, taxRate));
//						// δ˰ӯ�����
//						cPrftLossSubTab.setUnTaxPrftLossAmt(CalcAmt.noTaxAmt(unTaxUprc, prftLossQtyg));
//						// ��˰ӯ�����
//						cPrftLossSubTab.setCntnTaxPrftLossAmt(CalcAmt.prcTaxSum(unTaxUprc, prftLossQtyg, taxRate));
//						// δ˰������
//						cPrftLossSubTab.setUnTaxPrftLossAmts(CalcAmt.noTaxAmt(unTaxUprc, prftLossQtysg));
//						// ��˰������
//						cPrftLossSubTab.setCntnTaxPrftLossAmts(CalcAmt.prcTaxSum(unTaxUprc, prftLossQtysg, taxRate));
                        // ���㺬˰���� ��˰���ۣ����ɱ༭��=��˰����*��1+˰�ʣ�
//                        cPrftLossSubTab.setCntnTaxUprc((BigDecimal.ONE.add(taxRate)).multiply(unTaxUprc));
//                        // δ˰������ ��˰�����ɱ༭��=��˰����*����
//                        cPrftLossSubTab.setUnTaxBookAmt(unTaxUprc.multiply(bookQtyg));
//                        // ��˰������ ��˰�ϼƣ����ɱ༭��=��˰����*����
//                        cPrftLossSubTab.setCntnTaxBookAmt(cPrftLossSubTab.getCntnTaxUprc().multiply(bookQtyg));
//                        // δ˰�̵���
//                        cPrftLossSubTab.setUnTaxCheckAmt(unTaxUprc.multiply(checkQtyg));
//                        // ��˰�̵���
//                        cPrftLossSubTab.setCntnTaxCheckAmt(cPrftLossSubTab.getCntnTaxUprc().multiply(checkQtyg));
//                        // δ˰ӯ�����
//                        cPrftLossSubTab.setUnTaxPrftLossAmt(unTaxUprc.multiply(prftLossQtyg));
//                        // ��˰ӯ�����
//                        cPrftLossSubTab.setCntnTaxPrftLossAmt(cPrftLossSubTab.getCntnTaxUprc().multiply(prftLossQtyg));
//                        // δ˰������
//                        cPrftLossSubTab.setUnTaxPrftLossAmts(unTaxUprc.multiply(prftLossQtysg));
//                        // ��˰������
//                        cPrftLossSubTab.setCntnTaxPrftLossAmts(cPrftLossSubTab.getCntnTaxUprc().multiply(prftLossQtysg));

//                        cPrftLossSubTab.setCntnTaxUprc(unTaxUprc.multiply(taxRate).add(unTaxUprc));//��˰����

//                        cPrftLossSubTab.setUnTaxUprc();//δ˰����

                        cPrftLossSubTab.setUnTaxCheckAmt(checkQtyg.multiply(unTaxUprc));//δ˰�̵���
                        cPrftLossSubTab.setUnTaxBookAmt(bookQtyg.multiply(unTaxUprc));//δ˰������
                        cPrftLossSubTab.setUnTaxPrftLossAmt(prftLossQtyg.multiply(unTaxUprc));//δ˰ӯ�����
                        cPrftLossSubTab.setUnTaxPrftLossAmts(prftLossQtyg.multiply(unTaxUprc));//δ˰������

                        cPrftLossSubTab.setCheckTaxAmt(checkQtyg.multiply(unTaxUprc).multiply(taxRate));//�̵�˰��
                        cPrftLossSubTab.setBookTaxAmt(bookQtyg.multiply(unTaxUprc).multiply(taxRate));//����˰��
                        cPrftLossSubTab.setPrftLossTaxAmt(prftLossQtyg.multiply(unTaxUprc).multiply(taxRate));//ӯ��˰��
                        cPrftLossSubTab.setPrftLossTaxAmts(prftLossQtyg.multiply(unTaxUprc).multiply(taxRate));//����˰��


                        cPrftLossSubTab.setCntnTaxCheckAmt(cPrftLossSubTab.getUnTaxCheckAmt().add(cPrftLossSubTab.getCheckTaxAmt()));//��˰�̵���
                        cPrftLossSubTab.setCntnTaxBookAmt(cPrftLossSubTab.getUnTaxBookAmt().add(cPrftLossSubTab.getBookTaxAmt()));//��˰������
                        cPrftLossSubTab.setCntnTaxPrftLossAmt(cPrftLossSubTab.getUnTaxPrftLossAmt().add(cPrftLossSubTab.getPrftLossTaxAmt()));//��˰ӯ�����
                        cPrftLossSubTab.setCntnTaxPrftLossAmts(cPrftLossSubTab.getUnTaxPrftLossAmts().add(cPrftLossSubTab.getPrftLossTaxAmts()));//��˰������
                        cPrftLossSubTab.setCntnTaxUprc(cPrftLossSubTab.getCntnTaxCheckAmt().divide(checkQtyg, 8, BigDecimal.ROUND_HALF_UP));//��˰����

                        cSubLossList.add(cPrftLossSubTab);

                        message = "����:" + danHao + "��˳ɹ���";

//					invtyTabs.add(invtyTab);

                    }
                } else {

                    throw new RuntimeException("����:" + danHao + "ʵ�������п�ֵ,��ȫ����д��");

                }
            }
            if (cSubLossList.size() > 0) {
                // �����̵������
                String number = null;
                try {
                    number = getOrderNo.getSeqNo("SY", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("���Ż�ȡ�쳣");
                } // ��ȡ������
                checkPrftLoss.setCheckFormNum(number);// ������
                checkPrftLoss.setSrcFormNum(danHao);// ��Դ���ݺ�
                checkPrftLoss.setCheckDt(formDate);// �̵�����
                checkPrftLoss.setBookDt(cSngl.getBookDt());// ��������
                checkPrftLoss.setWhsEncd(cSngl.getWhsEncd());// �ֿ����
                checkPrftLoss.setCheckBat(cSngl.getCheckBat());// �̵�����
                checkPrftLoss.setMemo(cSngl.getMemo());// ��ע
                checkPrftLoss.setSetupPers(userName);// ������
                checkPrftLoss.setFormTypEncd("029");
                checkPrftLossMapper.insertCheckSnglLoss(checkPrftLoss);// �����̵��������
                for (CheckPrftLossSubTab tab : cSubLossList) {
                    tab.setCheckFormNum(number);// �̵����浥��
                }
                checkPrftLossMapper.insertCheckSnglLossSubTab(cSubLossList);// �����̵�������
            } else {
                message = "����:" + danHao + "ʵ������������һ���������";
            }

            return message;
        } catch (Exception e1) {

            throw new RuntimeException(e1.getMessage());
        }
    }

    // ����
    @Override
    public String updateCSnglNoChk(String userId, String jsonBody) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        ObjectNode dbzhu = null;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }

        List<CheckSngl> cSnglList = new ArrayList<>();// �̵���
        cSnglList = new ArrayList<>();// �̵���

        String danHao = dbzhu.get("formNum").asText();

        CheckSngl checkSngl = new CheckSngl();
        List<CheckPrftLoss> count = checkSnglMapper.selectLossIsDel(danHao);
        if (count.size() > 0) {
            throw new RuntimeException("����:" + danHao + "���̵������б���δɾ��,����ɾ��");

        }
        // ��ɾ���̵������е����ݣ������������

        CheckSngl cSngl = checkSnglMapper.selectCheckSngl(danHao);

        cSnglList.add(cSngl);
        if (cSngl == null) {
            throw new RuntimeException("����:" + danHao + "������ ");
        }
        if (cSngl.getIsNtChk() == 0) {
            throw new RuntimeException("����:" + danHao + "������,����Ҫ�ظ����� ");

        } else {

            int count1 = checkSnglMapper.updateCSnglNoChk(cSnglList);
            if (count1 >= 1) {
                message = "����:" + danHao + "����ɹ���";
            } else {
                throw new RuntimeException("����:" + danHao + "����ʧ�ܣ�");
            }
        }

        return message;
    }

    // ����֮������״̬ ����PC�ν��и��� ��֤����ͬ��
    @Override
    public String updateStatus(List<CheckSngl> cList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = checkSnglMapper.updateStatus(cList);
        if (i >= 1) {
            message = "���������ɹ���";
            isSuccess = true;
        } else {
            message = "��������ʧ�ܣ�";
            isSuccess = false;
        }

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/updateStatus", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // ��ӡ
    @Override
    public String queryListDaYin(Map map) {
        String resp = "";
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("setupPers", getList((String) map.get("setupPers")));
        map.put("checkFormNum", getList((String) map.get("checkFormNum")));
        map.put("formTypEncd", getList((String) map.get("formTypEncd")));
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        List<CheckSnglMap> aList = checkSnglMapper.selectList(map);
//		List<CheckSngl> aList = checkSnglMapper.selectListDaYin(map);
        aList.add(new CheckSnglMap());
        try {
            resp = BaseJson.returnRespObjListAnno("whs/check_sngl/queryListDaYin", true, "��ѯ�ɹ���", null, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String uploadFileAddDb(MultipartFile file) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, CheckSngl> pusOrderMap = uploadScoreInfo(file);
        // System.out.println(pusOrderMap.size());

        for (Map.Entry<String, CheckSngl> entry : pusOrderMap.entrySet()) {

            CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(entry.getValue().getCheckFormNum());
            if (checkSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getCheckFormNum() + " ����");
            }
            try {
                checkSnglMapper.exInsertCheckSngl(entry.getValue());
                checkSnglMapper.exInsertCheckSnglSubTab(entry.getValue().getcSubTabList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "�̵㵥�����ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // ����excle
    private Map<String, CheckSngl> uploadScoreInfo(MultipartFile file) {
        Map<String, CheckSngl> temp = new HashMap<>();
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
                CheckSngl checkSngl = new CheckSngl();
                if (temp.containsKey(orderNo)) {
                    checkSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//				// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                checkSngl.setCheckFormNum(orderNo);// �̵㵥��
                checkSngl.setFormTypEncd("028");

                checkSngl.setCheckDt(
                        GetCellData(r, "�̵�����") == null ? "" : GetCellData(r, "�̵�����"));// �̵�����
                checkSngl.setBookDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������"));// ��������q
                checkSngl.setWhsEncd(GetCellData(r, "�ֿ����"));// �ֿ����
                checkSngl.setCheckBat(GetCellData(r, "�̵�����"));// �̵�����
                checkSngl.setSetupPers(GetCellData(r, "������")); // ������q
                checkSngl.setSetupTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��")); // ����ʱ��q
                checkSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���q
                checkSngl.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��")); // �޸�ʱ��q
                checkSngl.setChkr(GetCellData(r, "�����")); // �����q
                checkSngl.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��")); // ���ʱ��q
                checkSngl.setMemo(GetCellData(r, "��ע"));// ��ע

                checkSngl.setBookEntryPers(GetCellData(r, "������"));// ������

                checkSngl.setBookEntryTm(
                        GetCellData(r, "����ʱ��") == null ? "" : GetCellData(r, "����ʱ��"));// ����ʱ��
//                checkSngl.setOperator(GetCellData(r, "����Ա"));// ����Ա
//                checkSngl.setOperatorId(GetCellData(r, "����Ա����"));// ����Ա����
                checkSngl.setCheckStatus(GetCellData(r, "�̵�״̬"));// �̵�״̬

                checkSngl
                        .setIsNtChk(new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
//				// System.out.println("checkSngl"+isNtChk);
                checkSngl.setIsNtBookEntry(
                        new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
                                : GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
//				// System.out.println("isNtBookEntry"+isNtBookEntry);
//                checkSngl.setIsNtCmplt(
//                        new Double(GetCellData(r, "�Ƿ����") == null || GetCellData(r, "�Ƿ����").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ����")).intValue());// �Ƿ����
//				// System.out.println("isNtCmplt"+isNtCmplt);
//                checkSngl.setIsNtClos(
//                        new Double(GetCellData(r, "�Ƿ�ر�") == null || GetCellData(r, "�Ƿ�ر�").equals("") ? "0"
//                                : GetCellData(r, "�Ƿ�ر�")).intValue());// �Ƿ�ر�
//				// System.out.println("isNtClos"+isNtClos);
//                checkSngl.setPrintCnt(
//                        new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
//                                : GetCellData(r, "��ӡ����")).intValue());// ��ӡ����q
//				checkSngl.setIsNtWms(new Double(GetCellData(r, "�Ƿ���WMS�ϴ�")).intValue());// �Ƿ���WMS�ϴ�q
//				// System.out.println("isNtWms"+isNtWms);
                List<CheckSnglSubTab> checkSnglSubList = checkSngl.getcSubTabList();// �����ӱ�
                if (checkSnglSubList == null) {
                    checkSnglSubList = new ArrayList<>();
                }

                CheckSnglSubTab checkSnglSubTab = new CheckSnglSubTab();

                checkSnglSubTab.setCheckFormNum(orderNo);// �̵㵥��
                checkSnglSubTab.setInvtyEncd(GetCellData(r, "�������")); // �������1
                checkSnglSubTab.setBatNum(GetCellData(r, "����")); // ����1

                checkSnglSubTab.setInvtyBigClsEncd(GetCellData(r, "����������")); // ����������
                checkSnglSubTab.setBarCd(GetCellData(r, "������")); // ������1
                checkSnglSubTab.setPrdcDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������")); // ��������1

//				Integer baoZhiQi = (new Double(r.getCell(29).getNumericCellValue())).intValue();
//				// System.out.println("baoZhiQi" + baoZhiQi);
                checkSnglSubTab.setBaoZhiQi(GetCellData(r, "������")); // ������1
//				// System.out.println("getCell" + r.getCell(29)); // ������1
//				// System.out.println("getNumericCellValue" + r.getCell(29).getNumericCellValue());
//				// System.out.println("getCellType" + r.getCell(29).getCellType()); // ������1

                checkSnglSubTab.setInvldtnDt(
                        GetCellData(r, "ʧЧ����") == null ? "" : GetCellData(r, "ʧЧ����")); // ʧЧ����1
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
                checkSnglSubTab.setBookAdjustQty(
                        new BigDecimal(GetCellData(r, "�����������") == null || GetCellData(r, "�����������").equals("") ? "0"
                                : GetCellData(r, "�����������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // �����������?
                checkSnglSubTab.setCheckQty(
                        new BigDecimal(GetCellData(r, "�̵�����") == null || GetCellData(r, "�̵�����").equals("") ? "0"
                                : GetCellData(r, "�̵�����")).setScale(8, BigDecimal.ROUND_HALF_UP)); // �̵�����1
                checkSnglSubTab.setPrftLossQty(
                        new BigDecimal(GetCellData(r, "ӯ������") == null || GetCellData(r, "ӯ������").equals("") ? "0"
                                : GetCellData(r, "ӯ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ӯ������1
                checkSnglSubTab.setPrftLossRatio(
                        new BigDecimal(GetCellData(r, "ӯ������") == null || GetCellData(r, "ӯ������").equals("") ? "0"
                                : GetCellData(r, "ӯ������")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ӯ������(%)1
                checkSnglSubTab.setReasn(GetCellData(r, "ԭ��")); // ԭ��1

                checkSnglSubTab
                        .setGdsBitEncd((GetCellData(r, "��λ����") == null || GetCellData(r, "��λ����").equals("")) ? null
                                : GetCellData(r, "��λ����")); // ��λ����
                checkSnglSubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                checkSnglSubList.add(checkSnglSubTab);

                checkSngl.setcSubTabList(checkSnglSubList);
                temp.put(orderNo, checkSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());

        }
        return temp;
    }

    @Override
    public String selectCheckSnglGdsBitList(Map map) {
        String resp = "";

        // ������Ϊ��
        List<InvtyTab> cList = checkSnglMapper.selectCheckSnglGdsBitList(map);
        if (cList.size() > 0) {
            try {
                resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglGdsBitList", true, "�ɹ�", cList);
            } catch (IOException e) {


            }

        } else {
            try {
                resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglGdsBitList", false, "ʧ��", null);
            } catch (IOException e) {


            }

        }

        return resp;
    }

    @Override
    public String uploadFileAddDbU8(MultipartFile file) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        Map<String, CheckSngl> pusOrderMap = uploadScoreInfoU8(file);
        // System.out.println(pusOrderMap.size());

        for (Map.Entry<String, CheckSngl> entry : pusOrderMap.entrySet()) {

            CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(entry.getValue().getCheckFormNum());
            if (checkSngl != null) {
                throw new RuntimeException("�����ظ����� " + entry.getValue().getCheckFormNum() + " ����");
            }
            try {
                checkSnglMapper.exInsertCheckSngl(entry.getValue());
                checkSnglMapper.exInsertCheckSnglSubTab(entry.getValue().getcSubTabList());

            } catch (Exception e) {


                throw new RuntimeException("����sql����");
            }
        }

        isSuccess = true;
        message = "�̵㵥�����ɹ���";
        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDbU8", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    private Map<String, CheckSngl> uploadScoreInfoU8(MultipartFile file) {
        Map<String, CheckSngl> temp = new HashMap<>();
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
                CheckSngl checkSngl = new CheckSngl();
                if (temp.containsKey(orderNo)) {
                    checkSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//				// System.out.println(r.getCell(0));
                // ȡ����ǰ�е�1����Ԫ�����ݣ�����װ��pursOrdrʵ��ĸ���������
                checkSngl.setCheckFormNum(orderNo);// �̵㵥��
                checkSngl.setFormTypEncd("028");

                checkSngl.setCheckDt(
                        GetCellData(r, "�̵�����") == null ? "" : GetCellData(r, "�̵�����").replaceAll("[^0-9:-]", " "));// �̵�����
                checkSngl.setBookDt(
                        GetCellData(r, "�̵�����") == null ? "" : GetCellData(r, "�̵�����").replaceAll("[^0-9:-]", " "));// ��������q
                checkSngl.setWhsEncd(GetCellData(r, "�̵�ֿ����"));// �ֿ����
                checkSngl.setCheckBat(GetCellData(r, "�̵�����"));// �̵�����
                checkSngl.setSetupPers(GetCellData(r, "�Ƶ���")); // ������q
                checkSngl.setSetupTm(
                        GetCellData(r, "�Ƶ�ʱ��") == null ? "" : GetCellData(r, "�Ƶ�ʱ��").replaceAll("[^0-9:-]", " ")); // ����ʱ��q
                checkSngl.setMdfr(GetCellData(r, "�޸���")); // �޸���q
                checkSngl.setModiTm(
                        GetCellData(r, "�޸�ʱ��") == null ? "" : GetCellData(r, "�޸�ʱ��").replaceAll("[^0-9:-]", " ")); // �޸�ʱ��q
                checkSngl.setChkr(GetCellData(r, "�����")); // �����q
                checkSngl.setChkTm(
                        GetCellData(r, "���ʱ��") == null ? "" : GetCellData(r, "���ʱ��").replaceAll("[^0-9:-]", " ")); // ���ʱ��q
                checkSngl.setMemo(GetCellData(r, "��ע"));// ��ע

                checkSngl.setBookEntryPers(null);// ������

                checkSngl.setBookEntryTm(null);// ����ʱ��
                checkSngl.setOperator(null);// ����Ա
                checkSngl.setOperatorId(null);// ����Ա����
                checkSngl.setCheckStatus("�̵���");// �̵�״̬
                checkSngl.setIsNtChk(0);// �Ƿ����
                checkSngl.setIsNtBookEntry(0);// �Ƿ����
                checkSngl.setIsNtCmplt(0);// �Ƿ����
                checkSngl.setIsNtClos(0);// �Ƿ�ر�
                checkSngl.setPrintCnt(
                        new Double(GetCellData(r, "��ӡ����") == null || GetCellData(r, "��ӡ����").equals("") ? "0"
                                : GetCellData(r, "��ӡ����")).intValue());// ��ӡ����q
                checkSngl.setIsNtWms(0);// �Ƿ���WMS�ϴ�q
//				// System.out.println("isNtWms"+isNtWms);
                List<CheckSnglSubTab> checkSnglSubList = checkSngl.getcSubTabList();// �����ӱ�
                if (checkSnglSubList == null) {
                    checkSnglSubList = new ArrayList<>();
                }

                CheckSnglSubTab checkSnglSubTab = new CheckSnglSubTab();

                checkSnglSubTab.setCheckFormNum(orderNo);// �̵㵥��
                checkSnglSubTab.setInvtyEncd(GetCellData(r, "�������")); // �������
                checkSnglSubTab.setBatNum(GetCellData(r, "����")); // ����

                checkSnglSubTab.setInvtyBigClsEncd(null); // ����������
                checkSnglSubTab.setBarCd(GetCellData(r, "������")); // ������1
                checkSnglSubTab.setPrdcDt(
                        GetCellData(r, "��������") == null ? "" : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ")); // ��������1
                String BaoZhiQi = GetCellData(r, "������");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                checkSnglSubTab.setBaoZhiQi(BaoZhiQi); // ������1

                checkSnglSubTab.setInvldtnDt(
                        GetCellData(r, "ʧЧ����") == null ? "" : GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " ")); // ʧЧ����1
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
                checkSnglSubTab.setPrftLossRatio(
                        new BigDecimal(GetCellData(r, "ӯ������%") == null || GetCellData(r, "ӯ������%").equals("") ? "0"
                                : GetCellData(r, "ӯ������%")).setScale(8, BigDecimal.ROUND_HALF_UP)); // ӯ������(%)1
                checkSnglSubTab.setReasn(GetCellData(r, "ԭ��")); // ԭ��1

                checkSnglSubTab.setGdsBitEncd(null); // ��λ����
                checkSnglSubTab.setProjEncd(GetCellData(r, "��Ŀ����")); //��Ŀ����

                checkSnglSubList.add(checkSnglSubTab);

                checkSngl.setcSubTabList(checkSnglSubList);
                temp.put(orderNo, checkSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());

        }
        return temp;
    }

}