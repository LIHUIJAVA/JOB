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

    // <!-- 根据仓库 货物编码 查批次 数量>0 的数据-->

    /**
     * 如果返回值 大小不大于0 一是没有 二是 库存不够
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

    // 失效日期维护
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
                resp = BaseJson.returnRespList("whs/invty_tab/selectInvldtnDtList", true, "查询成功！", count, pageNo,
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
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/selectInvldtnDtList", true, "查询成功！", null,
                        proList);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    // 保质期预警
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
                resp = BaseJson.returnRespList("whs/invty_tab/queryBaoZhiQiList", true, "查询成功！", count, pageNo,
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
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/queryBaoZhiQiList", true, "查询成功！", null, proList);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    // 出入库流水
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

//			String into = ".*入库";
//			String out = ".*出库";
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
                resp = BaseJson.returnRespList("whs/invty_tab/outIngWaterList", true, "查询成功！", count, pageNo, pageSize,
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
//			String into = ".*入库";
//			String out = ".*出库";
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
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/outIngWaterList", true, "查询成功！", null, proList);
            } catch (IOException e) {

            }

        }

        return resp;
    }

    public List<InvtyStandReport> InvtyStand(Map map) {
        String Dt1 = (String) map.get("formDt1") == null ? "" : (String) map.get("formDt1");
        String Dt2 = (String) map.get("formDt2") == null ? "" : (String) map.get("formDt2");
//时间转换
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
            InvtyStandReport.setWhsNm("时间必须输入");
            invtyStandReports.add(InvtyStandReport);
            return invtyStandReports;

        }
//期初表
        InvtyTab invtyTab = invtyTabMapper.selectInvtyTabSum(map);
        BigDecimal nowStok = null;
        if (invtyTab == null) {
            nowStok = new BigDecimal(0);
        } else {
            Optional<BigDecimal> a = Optional.ofNullable(invtyTab.getNowStok());
            nowStok = a.orElse(new BigDecimal(0));
        }

        // 期初
        List<InvtyStandReport> proList = invtyTabMapper.selectOutIngWater(map);

        String into = ".*入库";
        String out = ".*出库";
//		InvtyStandReport ingWaterReport = new InvtyStandReport();
//		ingWaterReport.setBalance(nowStok);
//
//		ingWaterReport.setFormType("期初");
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

//期初到现在
        List<InvtyStandReport> proLists = invtyTabMapper.selectOutIngWater(map);

        InvtyStandReport ingWaterReports = new InvtyStandReport();
        ingWaterReports.setBalance(nowStok);

        ingWaterReports.setFormType("期初");

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

    // 库存台账
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
                resp = BaseJson.returnRespList("whs/invty_tab/InvtyStandList", true, "查询成功！", count, pageNo, pageSize,
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
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/InvtyStandList", true, "查询成功！", null, list);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    // 查询所有存货信息 分组返回前端
    @Override
    public String selectInvty(Map map) {
        String resp = "";
        List<Map> invtyList = invtyTabMapper.selectInvty(map);

        try {
            resp = BaseJson.returnRespList("whs/invty_tab/selectInvty", true, "查询成功！", invtyList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 现存量查询
    @Override
    public String selectAvalQty(Map map) {
        // TODO Auto-generated method stub
        String resp = "";
        /**
         * // 到货toSdsSnglQty // 采购入库intoWhsQty;// 量 // 销售单sellSnglQty;// 量 //
         * 调拨出cannibSngllQty;// 量 // 销售出库sellOutWhslQty;// 量 List<InvtyTab> ToGdsSngl =
         * invtyTabMapper.selectToGdsSngl(map);// 到货 List<InvtyTab> SellSngl =
         * invtyTabMapper.selectSellSngl(map);// 销售单 List<InvtyTab> SellOutWhs =
         * invtyTabMapper.selectSellOutWhs(map);// 销售出库 List<InvtyTab> CannibSngl =
         * invtyTabMapper.selectCannibSngl(map);// 调拨 List<InvtyTab> IntoWhsSub =
         * invtyTabMapper.selectIntoWhsSub(map);// 采购入
         *
         * for (InvtyTab list : proList) {
         *
         * // 库存 InvtyTab lin = invtyNumMapper.selectInvtyTabByTerm(list); NowStokReport
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
         * //private String invtyNm; // 存货名称 private String whsNm; // 仓库名称
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
                resp = BaseJson.returnRespList("whs/InvtyTab/selectExtantQtyList", true, "查询成功！", count, pageNo,
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
                resp = BaseJson.returnRespObjListAnno("whs/InvtyTab/selectExtantQtyList", true, "查询成功！", null, proList);
            } catch (IOException e) {

            }

        }
        return resp;
    }

    // --------hj---库存表查询------------------------------------------------------------
    // 分页条件查询库存表信息
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
                resp = BaseJson.returnRespList("whs/invty_tab/queryInvtyTabList", true, "查询成功！", count, pageNo,
                        pageSize, listNum, pages, poList);
            } catch (IOException e) {

            }
        } else {

            List<InvtyTab> poList = invtyTabMapper.selectInvtyTabList(map);
            poList.add(new InvtyTab());
            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/queryInvtyTabList", true, "查询成功！", null, poList);
            } catch (IOException e) {

            }

        }
        return resp;
    }

    // 收发存汇总
    @Override
    public String selectTSummary(Map map) {
        String resp = "";
        String Dt1 = (String) map.get("formDt1") == null ? "" : (String) map.get("formDt1");

        // 时间转换
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
                return resp = BaseJson.returnRespObj("whs/InvtyTab/queryInvtyTabList", false, "请输入时间！", null);
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

            // 查出所有出入库
            List<TransceiverSummaryReport> poList = invtyTabMapper.selectTSummaryList(map);

            int count = invtyTabMapper.selectTSummaryCount(map);

            int listNum = poList.size();
            int pages = count / pageSize + 1;

            try {
                resp = BaseJson.returnRespList("whs/InvtyTab/queryInvtyTabList", true, "查询成功！", count, pageNo, pageSize,
                        listNum, pages, poList);
            } catch (IOException e) {

            }
        } else {

            // 查出所有出入库
            List<TransceiverSummaryReport> poList = invtyTabMapper.selectTSummaryList(map);
            poList.add(new TransceiverSummaryReport());
            // 查出所有出入库
//			TransceiverSummaryReport po = invtyTabMapper.selectTSummary(map);
//			poList.add(po);
            try {
                resp = BaseJson.returnRespObjListAnno("whs/InvtyTab/queryInvtyTabList", true, "查询成功！", null, poList);
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
            resp = BaseJson.returnRespList("whs/amb_disamb_sngl/insertASngl", true, "查询成功", list);
        } catch (IOException e) {


        }

        return resp;
    }

    // 未审核单据
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
                resp = BaseJson.returnRespList("whs/invty_tab/selectNtChkNoList", true, "查询成功！", count, pageNo,
                        pageSize, listNum, pages, proList);
            } catch (IOException e) {

            }
        } else {
            List<NtChkNo> proList = invtyTabMapper.selectNtChkNoList(map);
            proList.add(new NtChkNo());
            try {
                resp = BaseJson.returnRespObjListAnno("whs/invty_tab/selectNtChkNoList", true, "查询成功！", null, proList);
            } catch (IOException e) {

            }
        }
        return resp;
    }

    /**
     * id放入list
     *
     * @param id id(多个已逗号分隔)
     * @return List集合
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


            throw new RuntimeException("插入sql问题");

        }

        isSuccess = true;
        message = "库存导入成功";
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
            // 根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new HSSFWorkbook(fileIn);

            // 获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            // 获得当前sheet的开始行
            int firstRowNum = sht0.getFirstRowNum();
            // 获取文件的最后一行
            int lastRowNum = sht0.getLastRowNum();
            // 设置中文字段和下标映射
            SetColIndex(sht0.getRow(firstRowNum));
            // 对Sheet中的每一行进行迭代
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                j++;
                Row r = sht0.getRow(i);
                // 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (r.getRowNum() < 1) {
                    continue;
                }

                InvtyTab invtyTab = new InvtyTab();

                String BaoZhiQi = GetCellData(r, "保质期");

                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                String prdcDt = GetCellData(r, "生产日期") == null ? ""
                        : GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " ");
                if (prdcDt == null || prdcDt.equals("")) {
                    prdcDt = null;
                }
                String invldtnDt = GetCellData(r, "失效日期") == null ? ""
                        : GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " ");
                if (invldtnDt == null || invldtnDt.equals("")) {
                    invldtnDt = null;
                }

                invtyTab.setWhsEncd(GetCellData(r, "仓库编码"));
                invtyTab.setInvtyEncd(GetCellData(r, "存货编码"));
                invtyTab.setBatNum(GetCellData(r, "存货批号"));
                invtyTab.setPrdcDt(prdcDt);
                invtyTab.setBaoZhiQi(BaoZhiQi);
                invtyTab.setInvldtnDt(invldtnDt);
                invtyTab.setNowStok(
                        new BigDecimal(GetCellData(r, "结存数量") == null || GetCellData(r, "结存数量").equals("") ? "0"
                                : GetCellData(r, "结存数量")).setScale(8, BigDecimal.ROUND_HALF_UP));
                invtyTab.setAvalQty(
                        new BigDecimal(GetCellData(r, "结存数量") == null || GetCellData(r, "结存数量").equals("") ? "0"
                                : GetCellData(r, "结存数量")).setScale(8, BigDecimal.ROUND_HALF_UP));
                invtyTab.setUnTaxUprc(
                        new BigDecimal(GetCellData(r, "结存单价") == null || GetCellData(r, "结存单价").equals("") ? "0"
                                : GetCellData(r, "结存单价")).setScale(8, BigDecimal.ROUND_HALF_UP));
                invtyTab.setUnTaxAmt(
                        new BigDecimal(GetCellData(r, "结存金额") == null || GetCellData(r, "结存金额").equals("") ? "0"
                                : GetCellData(r, "结存金额")).setScale(8, BigDecimal.ROUND_HALF_UP));
                temp.add(invtyTab);
            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());
        }
        return temp;
    }
}
