package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.px.mis.util.BaseJson;
import com.px.mis.util.poiTool;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.InvtyTabMapper;
import com.px.mis.whs.entity.BaoZhiQi;
import com.px.mis.whs.entity.InvldtnDt;
import com.px.mis.whs.entity.InvtyStandReport;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.NowStokReport;
import com.px.mis.whs.entity.NtChkNo;
import com.px.mis.whs.entity.OutIngWaterReport;
import com.px.mis.whs.entity.TransceiverSummaryReport;
import com.px.mis.whs.service.InvtyTabService;

@Service
@Transactional
@SuppressWarnings("all")
public class InvtyTabServiceImpl extends poiTool implements InvtyTabService {

    @Autowired
    InvtyTabMapper invtyTabMapper;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    // <!-- ���ݲֿ� ������� ������ ����>0 ������-->

    /**
     * �������ֵ ��С������0 һ��û�� ���� ��治��
     */
    @Override
    public List<InvtyTab> selectInvtyTabByBatNum(InvtyTab invtyTab) {
        List<InvtyTab> invtyTabs = invtyNumMapper.selectInvtyTabByBatNum(invtyTab);
        BigDecimal avalQty = invtyTab.getAvalQty();
        List<InvtyTab> invtyTabsf = new ArrayList<>();
        for (InvtyTab tab : invtyTabs) {
            if (tab.getAvalQty().compareTo(avalQty) >= 0) {

                invtyTabsf.add(tab);
                return invtyTabsf;
            } else {
                invtyTabsf.add(tab);
                avalQty = avalQty.subtract(tab.getAvalQty());
            }

        }
        invtyTabsf.clear();
        return invtyTabsf;

    }

    // ʧЧ����ά��
    @Override
    public String queryInvldtnDtList(Map map) {
        String resp = "";
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            List<InvldtnDt> proList = invtyTabMapper.selectInvldtnDtList(map);
            int count = invtyTabMapper.selectInvldtnDtCount(map);
            int listNum = proList.size();
            int pages = count / pageSize + 1;
            try {
                resp = BaseJson.returnRespList("whs/invty_tab/selectInvldtnDtList", true, "��ѯ�ɹ���", count, pageNo,
                        pageSize, listNum, pages, proList);
            } catch (Exception e) {

            }
        } else {
            List<InvldtnDt> proList = invtyTabMapper.selectInvldtnDtList(map);
            proList.add(new InvldtnDt());

//			InvldtnDt pro = invtyTabMapper.selectInvldtnDtSUM(map);
//			if (pro != null) {
//			}
            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/selectInvldtnDtList", true, "��ѯ�ɹ���", null,
                        proList);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    // ������Ԥ��
    @Override
    public String queryBaoZhiQiList(Map map) {
        String resp = "";
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            List<BaoZhiQi> proList = invtyTabMapper.selectBaoZhiQiList(map);
            int count = invtyTabMapper.selectBaoZhiQiCount(map);
            int listNum = proList.size();
            int pages = count / pageSize + 1;
            try {
                resp = BaseJson.returnRespList("whs/invty_tab/queryBaoZhiQiList", true, "��ѯ�ɹ���", count, pageNo,
                        pageSize, listNum, pages, proList);
            } catch (IOException e) {

            }
        } else {
            List<BaoZhiQi> proList = invtyTabMapper.selectBaoZhiQiList(map);
            proList.add(new BaoZhiQi());
//			BaoZhiQi pro = invtyTabMapper.selectBaoZhiQiSUM(map);
//			if (pro != null) {
//				proList.add(pro);
//			}
            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/queryBaoZhiQiList", true, "��ѯ�ɹ���", null, proList);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    // �������ˮ
    @Override
    public String outIngWaterList(Map map) {
        String resp = "";
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("formNum", getList((String) map.get("formNum")));
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            List<OutIngWaterReport> proList = invtyTabMapper.selectOutIngWaterList(map);

//			String into = ".*���";
//			String out = ".*����";
//			for (OutIngWaterReport content : proList) {
//				if (content.getOutIntoWhsTypNm() == null) {
//					continue;
//				}
//				// boolean isMatch = ;
//				if (Pattern.matches(out, content.getOutIntoWhsTypNm())) {
//					content.setOutNoTaxUprc(content.getNoTaxUprc());
//					content.setOutQty(content.getQty());
//
//				} else if (Pattern.matches(into, content.getOutIntoWhsTypNm())) {
//					content.setIntoNoTaxUprc(content.getNoTaxUprc());
//					content.setIntoQty(content.getQty());
//				}
//
//			}
            int count = invtyTabMapper.selectOutIngWaterCount(map);

            int listNum = proList.size();
            int pages = count / pageSize + 1;
            try {
                resp = BaseJson.returnRespList("whs/invty_tab/outIngWaterList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                        listNum, pages, proList);
            } catch (IOException e) {

            }

        } else {
            List<OutIngWaterReport> proList = invtyTabMapper.selectOutIngWaterList(map);
            proList.add(new OutIngWaterReport());

//			OutIngWaterReport pro = invtyTabMapper.selectOutIngWaterSUM(map);
//			if (pro != null) {
//				proList.add(pro);
//			}
//			String into = ".*���";
//			String out = ".*����";
//			for (OutIngWaterReport content : proList) {
//
//				// boolean isMatch = ;
//				if (Pattern.matches(out, content.getOutIntoWhsTypNm())) {
//					content.setOutNoTaxUprc(content.getNoTaxUprc());
//					content.setOutQty(content.getQty());
//
//				} else if (Pattern.matches(into, content.getOutIntoWhsTypNm())) {
//					content.setIntoNoTaxUprc(content.getNoTaxUprc());
//					content.setIntoQty(content.getQty());
//				}
//
//			}

            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/outIngWaterList", true, "��ѯ�ɹ���", null, proList);
            } catch (IOException e) {

            }

        }

        return resp;
    }

    public List<InvtyStandReport> InvtyStand(Map map) {
        String Dt1 = (String) map.get("formDt1") == null ? "" : (String) map.get("formDt1");
        String Dt2 = (String) map.get("formDt2") == null ? "" : (String) map.get("formDt2");
//ʱ��ת��
        try {
            String now;
            Date date = null;
            date = new SimpleDateFormat("yyyy-MM-dd").parse(Dt1);
            now = new SimpleDateFormat("yyyy-MM").format(date);

            map.put("Dt1", now + "-01 00:00:00");
            // System.out.println(now + "\t" + Dt1);
        } catch (Exception e) {


            List<InvtyStandReport> invtyStandReports = new ArrayList<InvtyStandReport>();
            InvtyStandReport InvtyStandReport = new InvtyStandReport();
            InvtyStandReport.setWhsNm("ʱ���������");
            invtyStandReports.add(InvtyStandReport);
            return invtyStandReports;

        }
//�ڳ���
        InvtyTab invtyTab = invtyTabMapper.selectInvtyTabSum(map);
        BigDecimal nowStok = null;
        if (invtyTab == null) {
            nowStok = new BigDecimal(0);
        } else {
            Optional<BigDecimal> a = Optional.ofNullable(invtyTab.getNowStok());
            nowStok = a.orElse(new BigDecimal(0));
        }

        // �ڳ�
        List<InvtyStandReport> proList = invtyTabMapper.selectOutIngWater(map);

        String into = ".*���";
        String out = ".*����";
//		InvtyStandReport ingWaterReport = new InvtyStandReport();
//		ingWaterReport.setBalance(nowStok);
//
//		ingWaterReport.setFormType("�ڳ�");
        // proList.add(0, ingWaterReport);

        for (InvtyStandReport content : proList) {
            Optional<BigDecimal> a1 = Optional.ofNullable(content.getQty());

            // boolean isMatch = ;
            if (Pattern.matches(out, content.getFormType() == null ? "" : content.getFormType())) {
                content.setOutQty(a1.orElse(new BigDecimal(0)));
                nowStok = nowStok.subtract(a1.orElse(new BigDecimal(0)));
                content.setBalance(nowStok);

            } else if (Pattern.matches(into, content.getFormType() == null ? "" : content.getFormType())) {
                content.setIntoQty(a1.orElse(new BigDecimal(0)));
                nowStok = nowStok.add(a1.orElse(new BigDecimal(0)));
                content.setBalance(nowStok);
            }

        }
        map.remove("Dt1");
        map.remove("formDt1");
        map.remove("formDt2");
        map.put("formDt12", Dt1);
        map.put("formDt22", Dt2);

//�ڳ�������
        List<InvtyStandReport> proLists = invtyTabMapper.selectOutIngWater(map);

        InvtyStandReport ingWaterReports = new InvtyStandReport();
        ingWaterReports.setBalance(nowStok);

        ingWaterReports.setFormType("�ڳ�");

        proLists.add(0, ingWaterReports);
        // System.out.println(proLists.size());
        for (InvtyStandReport content : proLists) {
            Optional<BigDecimal> a1 = Optional.ofNullable(content.getQty());

            // boolean isMatch = ;
            if (Pattern.matches(out, content.getFormType() == null ? "" : content.getFormType())) {

                content.setOutQty(a1.orElse(new BigDecimal(0)));
                nowStok = nowStok.subtract(a1.orElse(new BigDecimal(0)));
                content.setBalance(nowStok);

            } else if (Pattern.matches(into, content.getFormType() == null ? "" : content.getFormType())) {
                content.setIntoQty(a1.orElse(new BigDecimal(0)));
                nowStok = nowStok.add(a1.orElse(new BigDecimal(0)));
                content.setBalance(nowStok);
            }

        }

        return proLists;
    }

    // ���̨��
    @Override
    public String InvtyStandList(Map map) {
        String resp = "";
//		InvtyStandReport	InvtyStandReport
        map.put("whsId", getList((String) map.get("whsId")));
        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            List<InvtyStandReport> list = InvtyStand(map);
            // List<Map> iStandList = invtyTabMapper.selectInvtyStandList(map);
            // int count = invtyTabMapper.selectInvtyStandCount(map);
            int count = invtyTabMapper.selectInvtyStandCount(map);
            count = count + 1;
            int listNum = list.size();
            int pages = count / pageSize;
            if (count % pageSize > 0) {
                pages += 1;
            }
            try {
                resp = BaseJson.returnRespList("whs/invty_tab/InvtyStandList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                        listNum, pages, list);
            } catch (IOException e) {

            }

        } else {
            List<InvtyStandReport> list = InvtyStand(map);
            list.add(new InvtyStandReport());
//			InvtyStandReport report = invtyTabMapper.selectOutIngSUM(map);
//			if (report != null) {
//				list.add(report);
//			}

            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/InvtyStandList", true, "��ѯ�ɹ���", null, list);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    // ��ѯ���д����Ϣ ���鷵��ǰ��
    @Override
    public String selectInvty(Map map) {
        String resp = "";
        List<Map> invtyList = invtyTabMapper.selectInvty(map);

        try {
            resp = BaseJson.returnRespList("whs/invty_tab/selectInvty", true, "��ѯ�ɹ���", invtyList);
        } catch (IOException e) {

        }
        return resp;
    }

    // �ִ�����ѯ
    @Override
    public String selectAvalQty(Map map) {
        // TODO Auto-generated method stub
        String resp = "";
        /**
         * // ����toSdsSnglQty // �ɹ����intoWhsQty;// �� // ���۵�sellSnglQty;// �� //
         * ������cannibSngllQty;// �� // ���۳���sellOutWhslQty;// �� List<InvtyTab> ToGdsSngl =
         * invtyTabMapper.selectToGdsSngl(map);// ���� List<InvtyTab> SellSngl =
         * invtyTabMapper.selectSellSngl(map);// ���۵� List<InvtyTab> SellOutWhs =
         * invtyTabMapper.selectSellOutWhs(map);// ���۳��� List<InvtyTab> CannibSngl =
         * invtyTabMapper.selectCannibSngl(map);// ���� List<InvtyTab> IntoWhsSub =
         * invtyTabMapper.selectIntoWhsSub(map);// �ɹ���
         *
         * for (InvtyTab list : proList) {
         *
         * // ��� InvtyTab lin = invtyNumMapper.selectInvtyTabByTerm(list); NowStokReport
         * nowStokReport = new NowStokReport(); if (lin == null) {
         * nowStokReport.setWhsEncd(list.getWhsEncd());
         * nowStokReport.setInvtyEncd(list.getInvtyEncd());
         * nowStokReport.setBatNum(list.getBatNum());
         *
         * NowStokReport nowStokReportl = invtyTabMapper.selectWhsSub(nowStokReport); if
         * (nowStokReportl != null) { nowStokReport.setWhsNm(nowStokReportl.getWhsNm()
         * == null ? "" : nowStokReportl.getWhsNm());
         * nowStokReport.setInvtyNm(nowStokReportl.getInvtyNm() == null ? "" :
         * nowStokReportl.getInvtyNm()); } } else {
         * nowStokReport.setWhsEncd(lin.getWhsEncd());
         * nowStokReport.setInvtyEncd(lin.getInvtyEncd());
         * nowStokReport.setBatNum(lin.getBatNum());
         * nowStokReport.setNowStok(lin.getNowStok());
         * nowStokReport.setAvalQty(lin.getAvalQty());
         *
         * NowStokReport nowStokReportl = invtyTabMapper.selectWhsSub(nowStokReport); if
         * (nowStokReportl != null) { nowStokReport.setWhsNm(nowStokReportl.getWhsNm()
         * == null ? "" : nowStokReportl.getWhsNm());
         * nowStokReport.setInvtyNm(nowStokReportl.getInvtyNm() == null ? "" :
         * nowStokReportl.getInvtyNm()); } } ;
         *
         *
         * //private String invtyNm; // ������� private String whsNm; // �ֿ�����
         *
         *
         * for (InvtyTab to : ToGdsSngl) { if (to != null) { if
         * (nowStokReport.getWhsEncd().equals(to.getWhsEncd()) &&
         * nowStokReport.getInvtyEncd().equals(to.getInvtyEncd()) &&
         * nowStokReport.getBatNum().equals(to.getBatNum())) {
         * nowStokReport.setToSdsSnglQty(to.getAvalQty()); // break; } }
         *
         * } for (InvtyTab sell : SellSngl) { if (sell != null) { if
         * (nowStokReport.getWhsEncd().equals(sell.getWhsEncd()) &&
         * nowStokReport.getInvtyEncd().equals(sell.getInvtyEncd()) &&
         * nowStokReport.getBatNum().equals(sell.getBatNum())) {
         * nowStokReport.setSellSnglQty(sell.getAvalQty()); // break; } }
         *
         * } for (InvtyTab to : SellOutWhs) { if (to != null) { if
         * (nowStokReport.getWhsEncd().equals(to.getWhsEncd()) &&
         * nowStokReport.getInvtyEncd().equals(to.getInvtyEncd()) &&
         * nowStokReport.getBatNum().equals(to.getBatNum())) {
         * nowStokReport.setSellOutWhsQty(to.getAvalQty()); // break; } }
         *
         * } for (InvtyTab to : CannibSngl) { if (to != null) { if
         * (nowStokReport.getWhsEncd().equals(to.getWhsEncd()) &&
         * nowStokReport.getInvtyEncd().equals(to.getInvtyEncd()) &&
         * nowStokReport.getBatNum().equals(to.getBatNum())) {
         * nowStokReport.setCannibSnglQty(to.getAvalQty()); // break; } }
         *
         * } for (InvtyTab to : IntoWhsSub) { // System.out.println(to == null); if (to !=
         * null) {
         *
         * if (nowStokReport.getWhsEncd().equals(to.getWhsEncd()) &&
         * nowStokReport.getInvtyEncd().equals(to.getInvtyEncd()) &&
         * nowStokReport.getBatNum().equals(to.getBatNum())) {
         * nowStokReport.setIntoWhsQty(to.getAvalQty()); // break; } } }
         * nowStokReportsList.add(nowStokReport); }
         */
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            List<NowStokReport> proList = invtyTabMapper.selectExtantQtyList(map);
            int count = invtyTabMapper.selectExtantQtyCount(map);
//            int count=1;
//			NowStokReport pro = invtyTabMapper.selectExtantQtySUM(map);
//			if (pro != null) {
//				proList.add(pro);
//				count =pro.getNowStok().intValue();
//                pro.setNowStok(null);
//            }
            int listNum = proList.size();
            int pages = count / pageSize + 1;
            try {
                resp = BaseJson.returnRespList("whs/InvtyTab/selectExtantQtyList", true, "��ѯ�ɹ���", count, pageNo,
                        pageSize, listNum, pages, proList);
            } catch (IOException e) {

            }
        } else {

            List<NowStokReport> proList = invtyTabMapper.selectExtantQtyList(map);
            proList.add(new NowStokReport());
//			NowStokReport pro = invtyTabMapper.selectExtantQtySUM(map);
//			if (pro != null) {
//				proList.add(pro);
//			}

            try {
                resp = BaseJson.returnRespObjListAnno("whs/InvtyTab/selectExtantQtyList", true, "��ѯ�ɹ���", null, proList);
            } catch (IOException e) {

            }

        }
        return resp;
    }

    // --------hj---�����ѯ------------------------------------------------------------
    // ��ҳ������ѯ������Ϣ
    @Override
    public String queryInvtyTabList(Map map) {
        String resp = "";
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);
            List<InvtyTab> poList = invtyTabMapper.selectInvtyTabList(map);
            int count = invtyTabMapper.selectInvtyTabCount(map);
            int listNum = poList.size();
            int pages = count / pageSize + 1;

            try {
                resp = BaseJson.returnRespList("whs/invty_tab/queryInvtyTabList", true, "��ѯ�ɹ���", count, pageNo,
                        pageSize, listNum, pages, poList);
            } catch (IOException e) {

            }
        } else {

            List<InvtyTab> poList = invtyTabMapper.selectInvtyTabList(map);
            poList.add(new InvtyTab());
            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/queryInvtyTabList", true, "��ѯ�ɹ���", null, poList);
            } catch (IOException e) {

            }

        }
        return resp;
    }

    // �շ������
    @Override
    public String selectTSummary(Map map) {
        String resp = "";
        String Dt1 = (String) map.get("formDt1") == null ? "" : (String) map.get("formDt1");

        // ʱ��ת��
        try {
            String now;
            Date date = null;
            date = new SimpleDateFormat("yyyy-MM-dd").parse(Dt1);
            now = new SimpleDateFormat("yyyy-MM").format(date);

//			date = new SimpleDateFormat("yyyy-MM-dd").parse(Dt2);
//			now1 = new SimpleDateFormat("yyyy-MM").format(date);
            map.put("formDt", now + "-01 00:00:00");

            // System.out.println(now + "-01 00:00:00");
        } catch (Exception e) {

            try {
                return resp = BaseJson.returnRespObj("whs/InvtyTab/queryInvtyTabList", false, "������ʱ�䣡", null);
            } catch (IOException e1) {


            }

        }
        map.put("whsEncd", getList((String) map.get("whsEncd")));
        map.put("batNum", getList((String) map.get("batNum")));
//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
        map.put("whsId", getList((String) map.get("whsId")));

        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {

            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("ind", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            // ������г����
            List<TransceiverSummaryReport> poList = invtyTabMapper.selectTSummaryList(map);

            int count = invtyTabMapper.selectTSummaryCount(map);

            int listNum = poList.size();
            int pages = count / pageSize + 1;

            try {
                resp = BaseJson.returnRespList("whs/InvtyTab/queryInvtyTabList", true, "��ѯ�ɹ���", count, pageNo, pageSize,
                        listNum, pages, poList);
            } catch (IOException e) {

            }
        } else {

            // ������г����
            List<TransceiverSummaryReport> poList = invtyTabMapper.selectTSummaryList(map);
            poList.add(new TransceiverSummaryReport());
            // ������г����
//			TransceiverSummaryReport po = invtyTabMapper.selectTSummary(map);
//			poList.add(po);
            try {
                resp = BaseJson.returnRespObjListAnno("whs/InvtyTab/queryInvtyTabList", true, "��ѯ�ɹ���", null, poList);
            } catch (IOException e) {

            }

        }

        return resp;
    }

    @Override
    public String outIntoWhsTyp(Map map) {
        String resp = "";
        map.put("outIntoWhsTypId", getList((String) map.get("outIntoWhsTypId")));
        List<Map> list = invtyTabMapper.outIntoWhsTyp(map);

        try {
            resp = BaseJson.returnRespList("whs/amb_disamb_sngl/insertASngl", true, "��ѯ�ɹ�", list);
        } catch (IOException e) {


        }

        return resp;
    }

    // δ��˵���
    @Override
    public String selectNtChkNoList(Map map) {
        String resp = "";
        if (map.containsKey("pageNo") && map.containsKey("pageSize")) {
            int pageNo = (int) map.get("pageNo");
            int pageSize = (int) map.get("pageSize");
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            List<NtChkNo> proList = invtyTabMapper.selectNtChkNoList(map);
            int count = invtyTabMapper.selectNtChkNoCount(map);
            int listNum = proList.size();
            int pages = count / pageSize + 1;
            try {
                resp = BaseJson.returnRespList("whs/invty_tab/selectNtChkNoList", true, "��ѯ�ɹ���", count, pageNo,
                        pageSize, listNum, pages, proList);
            } catch (IOException e) {

            }
        } else {
            List<NtChkNo> proList = invtyTabMapper.selectNtChkNoList(map);
            proList.add(new NtChkNo());
            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/selectNtChkNoList", true, "��ѯ�ɹ���", null, proList);
            } catch (IOException e) {

            }
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

    @Override
    public String uploadFileAddDb(MultipartFile file) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<InvtyTab> pusOrderMap = uploadScoreInfo(file);

        try {
            invtyTabMapper.exInvtyTab(pusOrderMap);

        } catch (Exception e) {


            throw new RuntimeException("����sql����");

        }

        isSuccess = true;
        message = "��浼��ɹ�";
        try {
            resp = BaseJson.returnResp("whs/invty_tab/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    private List<InvtyTab> uploadScoreInfo(MultipartFile file) {
        List<InvtyTab> temp = new LinkedList<InvtyTab>();
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

                InvtyTab invtyTab = new InvtyTab();

                String BaoZhiQi = GetCellData(r, "������");

                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                String prdcDt = GetCellData(r, "��������") == null ? ""
                        : GetCellData(r, "��������").replaceAll("[^0-9:-]", " ");
                if (prdcDt == null || prdcDt.equals("")) {
                    prdcDt = null;
                }
                String invldtnDt = GetCellData(r, "ʧЧ����") == null ? ""
                        : GetCellData(r, "ʧЧ����").replaceAll("[^0-9:-]", " ");
                if (invldtnDt == null || invldtnDt.equals("")) {
                    invldtnDt = null;
                }

                invtyTab.setWhsEncd(GetCellData(r, "�ֿ����"));
                invtyTab.setInvtyEncd(GetCellData(r, "�������"));
                invtyTab.setBatNum(GetCellData(r, "�������"));
                invtyTab.setPrdcDt(prdcDt);
                invtyTab.setBaoZhiQi(BaoZhiQi);
                invtyTab.setInvldtnDt(invldtnDt);
                invtyTab.setNowStok(
                        new BigDecimal(GetCellData(r, "�������") == null || GetCellData(r, "�������").equals("") ? "0"
                                : GetCellData(r, "�������")).setScale(8, BigDecimal.ROUND_HALF_UP));
                invtyTab.setAvalQty(
                        new BigDecimal(GetCellData(r, "�������") == null || GetCellData(r, "�������").equals("") ? "0"
                                : GetCellData(r, "�������")).setScale(8, BigDecimal.ROUND_HALF_UP));
                invtyTab.setUnTaxUprc(
                        new BigDecimal(GetCellData(r, "��浥��") == null || GetCellData(r, "��浥��").equals("") ? "0"
                                : GetCellData(r, "��浥��")).setScale(8, BigDecimal.ROUND_HALF_UP));
                invtyTab.setUnTaxAmt(
                        new BigDecimal(GetCellData(r, "�����") == null || GetCellData(r, "�����").equals("") ? "0"
                                : GetCellData(r, "�����")).setScale(8, BigDecimal.ROUND_HALF_UP));
                temp.add(invtyTab);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("������ʽ�����" + j + "��" + e.getMessage());
        }
        return temp;
    }
}
