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
    // 订单号
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
    // 货位
    @Autowired
    InvtyGdsBitListMapper bitListMapper;

    // 新增的组装拆卸
    @Override
    public String insertAmbDisambSngl(String userId, AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList, String loginTime) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        String number = null;
        InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSngl.getMomKitEncd()))
                .orElseThrow(() -> new RuntimeException(aSngl.getMomKitEncd()+"该存货不存在"));
        Integer pto = Optional.ofNullable(invty.getPto()).orElseThrow(() -> new RuntimeException("pto属于为空"));
        Integer isQuaGuaPer = Optional.ofNullable(invty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
        if(isQuaGuaPer == 1){
            if(StringUtils.isBlank(aSngl.getBaoZhiQi())|| StringUtils.isBlank(aSngl.getMprdcDt())
                    || StringUtils.isBlank(aSngl.getInvldtnDt()) ){
                throw new RuntimeException(aSngl.getMomKitEncd()+"保质期管理存货日期设置错误");
            }
        }else{
            aSngl.setBaoZhiQi(null);
            aSngl.setMprdcDt(null);
            aSngl.setInvldtnDt(null);
        }

        if (pto == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "该存货属于pto", null);
            } catch (IOException e) {


            }
            return resp;
        }
        try {
            number = getOrderNo.getSeqNo("ZC", userId, loginTime);
        } catch (Exception e) {

            throw new RuntimeException("生成订单编号! 失败");
        } // 获取订单
        Optional<String> opt = Optional.ofNullable(number);
//	    BaseJson.returnRespObj("whs/amb_disamb_sngl/insertASngl", isSuccess, message, aSngl);

        if (ambDisambSnglMapper.selectAmbDisambSngl(number) != null) {
            message = "编号" + number + "已存在，请重新输入！";
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
        aSngl.setFormNum(number);// 主表获取订单号
        for (AmbDisambSnglubTab aSnglubTab : aList) {
            aSnglubTab.setFormNum(number);// 主表单据号
            aSnglubTab.setMomQty(aSngl.getMomQty());
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSnglubTab.getSubKitEncd()))
                    .orElseThrow(() -> new RuntimeException(aSnglubTab.getSubKitEncd()+"该存货不存在"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(aSnglubTab.getBaoZhiQi())|| StringUtils.isBlank(aSnglubTab.getSprdcDt())
                        || StringUtils.isBlank(aSnglubTab.getSinvldtnDt()) ){
                    throw new RuntimeException(aSnglubTab.getSubKitEncd()+"保质期管理存货日期设置错误");
                }
            }else{
                aSnglubTab.setBaoZhiQi(null);
                aSnglubTab.setSprdcDt(null);
                aSnglubTab.setSinvldtnDt(null);
            }
        }
        /**
         * BigDecimal unTaxUprc1; // 无税单价 BigDecimal qty1; // 数量 BigDecimal taxRate1; //
         * 税率 String prdcDt1; // 生产日期 String baoZhiQi1; // 保质期
         * aSngl.setFormNum(number);// 获取订单号 // 获取未税单价 unTaxUprc1 =
         * aSngl.getMunTaxUprc(); // 获取未税数量 qty1 = aSngl.getMomQty(); // 获取税率
         * 页面税率未整数，我们需要将税率/100 aSngl.setTaxRate(aSngl.getTaxRate().divide(new
         * BigDecimal(100))); taxRate1 = aSngl.getTaxRate(); // 获取生产日期 prdcDt1 =
         * aSngl.getMprdcDt(); // 获取保质期 baoZhiQi1 = aSngl.getBaoZhiQi(); // 计算失效日期
         * aSngl.setInvldtnDt(CalcAmt.getDate(prdcDt1, Integer.parseInt(baoZhiQi1))); //
         * 计算未税金额 金额=未税数量*未税单价 aSngl.setMunTaxAmt(CalcAmt.noTaxAmt(unTaxUprc1, qty1));
         * // 计算含税单价 含税单价=无税单价*税率+无税单价
         * aSngl.setMcntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc1, qty1, taxRate1)); //
         * 计算含税金额 含税金额=无税金额*税率+无税金额=税额+无税金额
         * aSngl.setMcntnTaxAmt(CalcAmt.prcTaxSum(unTaxUprc1, qty1, taxRate1));
         *
         * BigDecimal unTaxUprc; // 无税单价 BigDecimal qty; // 数量 BigDecimal taxRate; // 税率
         * String prdcDt; // 生产日期 String baoZhiQi; // 保质期 for (AmbDisambSnglubTab
         * aSnglubTab : aList) { aSnglubTab.setFormNum(aSngl.getFormNum());// 主表单据号 //
         * 子件数量=母件数量*母子比例 BigDecimal mQty = aSnglubTab.getMomQty(); BigDecimal ratio =
         * aSnglubTab.getMomSubRatio(); aSnglubTab.setSubQty(mQty.multiply(ratio)); //
         * 获取未税数量 qty = aSnglubTab.getSubQty(); // 获取未税单价 unTaxUprc =
         * aSnglubTab.getSunTaxUprc(); // 获取税率 页面税率未整数，我们需要将税率/100
         * aSnglubTab.setTaxRate(aSnglubTab.getTaxRate().divide(new BigDecimal(100)));
         * taxRate = aSnglubTab.getTaxRate(); // 获取生产日期 prdcDt =
         * aSnglubTab.getSprdcDt(); // 获取保质期 baoZhiQi = aSnglubTab.getBaoZhiQi(); //
         * 计算失效日期 aSnglubTab.setSinvldtnDt(CalcAmt.getDate(prdcDt,
         * Integer.parseInt(baoZhiQi))); // 计算未税金额 金额=未税数量*未税单价
         * aSnglubTab.setSunTaxAmt(CalcAmt.noTaxAmt(unTaxUprc, qty)); // 计算含税单价
         * 含税单价=无税单价*税率+无税单价 aSnglubTab.setScntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc,
         * qty, taxRate)); // 计算含税金额 含税金额=无税金额*税率+无税金额=税额+无税金额
         * aSnglubTab.setScntnTaxAmt(CalcAmt.prcTaxSum(unTaxUprc, qty, taxRate)); }
         */

        aSngl.setFormTypEncd(aSngl.getFormTyp().equals("组装") ? "012" : "013");

        ambDisambSnglMapper.insertAmbDisambSngl(aSngl);
        ambDisambSnglMapper.insertAmbDisambSnglubTab(aList);
        for (AmbDisambSnglubTab aSnglubTab : aList) {
            aSnglubTab.setMomQty(aSngl.getMomQty());
        }
        message = "新增成功！";
        isSuccess = true;
        aSngl.setAmbSnglubList(aList);

        try {
            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/insertASngl", isSuccess, message, aSngl);
        } catch (IOException e) {


        }

        return resp;
    }

    // 修改组装拆卸
    @Override
    public String updateAmbDisambSngl(AmbDisambSngl aSngl, List<AmbDisambSnglubTab> aList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        InvtyDoc invty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSngl.getMomKitEncd()))
                .orElseThrow(() -> new RuntimeException("该存货不存在"));
        Integer pto = Optional.ofNullable(invty.getPto()).orElseThrow(() -> new RuntimeException("pto属于为空"));
//		Integer bom = Optional.ofNullable(invty.getAllowBomMain()).orElseThrow(() -> new RuntimeException("bom属于为空"));
        Integer isQuaGuaPer = Optional.ofNullable(invty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
        if(isQuaGuaPer == 1){
            if(StringUtils.isBlank(aSngl.getBaoZhiQi())|| StringUtils.isBlank(aSngl.getMprdcDt())
                    || StringUtils.isBlank(aSngl.getInvldtnDt()) ){
                throw new RuntimeException(aSngl.getMomKitEncd()+"保质期管理存货日期设置错误");
            }
        }else{
            aSngl.setBaoZhiQi(null);
            aSngl.setMprdcDt(null);
            aSngl.setInvldtnDt(null);
        }
        if (pto == 1) {
            try {
                resp = BaseJson.returnRespObj("whs/prod_stru/insertProdStru", false, "该存货属于pto", null);
            } catch (IOException e) {


            }
            return resp;
        }
        for (AmbDisambSnglubTab aSnglubTab : aList) {
            aSnglubTab.setMomQty(aSngl.getMomQty());
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(aSnglubTab.getSubKitEncd()))
                    .orElseThrow(() -> new RuntimeException(aSnglubTab.getSubKitEncd()+"该存货不存在"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(aSnglubTab.getBaoZhiQi())|| StringUtils.isBlank(aSnglubTab.getSprdcDt())
                        || StringUtils.isBlank(aSnglubTab.getSinvldtnDt()) ){
                    throw new RuntimeException(aSnglubTab.getSubKitEncd()+"保质期管理存货日期设置错误");
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
                throw new RuntimeException("单号审核状态异常");
            } else if (amb.getIsNtChk() == 1) {
                throw new RuntimeException("单号已审核不允许修改");
            }
            int i = ambDisambSnglMapper.deleteAmbDisambSnglubTab(aSngl.getFormNum());

            ambDisambSnglMapper.updateAmbDisambSngl(aSngl);
            ambDisambSnglMapper.insertAmbDisambSnglubTab(aList);
            message = "更新成功！";

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/updateASngl", isSuccess, message, null);
        } catch (Exception e) {

            throw new RuntimeException("修改组装拆失败" + e.getMessage());

        }
        return resp;
    }

    // 批量删除
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
                    message = "删除成功！" + lists.toString();
                    isSuccess = true;
                } else if (lists2.size() > 0) {
                    isSuccess = false;
                    message = message + "\r编号已审：" + lists2;
                }

            } else {
                isSuccess = false;
                message = "编号" + formNum + "不存在！";
            }

            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/deleteAllAmbDisambSngl", isSuccess, message, null);
        } catch (Exception e) {

            throw new RuntimeException("删除 失败");
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

    // 查询、分页查
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
                message = "查询成功！";
            } else {
                isSuccess = false;
                message = "编号" + formNum + "不存在！";
            }

            resp = BaseJson.returnRespObjList("whs/amb_disamb_sngl/query", isSuccess, message, aDisambSngl,
                    aSubTabList);
        } catch (Exception e) {

            throw new RuntimeException("查询 失败");

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
            resp = BaseJson.returnRespList("whs/amb_disamb_sngl/queryList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {


        }
        return resp;
    }

    // 审核
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
            throw new RuntimeException("Json 解析异常");
        }
        try {
            String danHao = dbzhu.get("formNum").asText();
            List<AmbDisambSnglubTab> aSnglubTabList = new ArrayList<>();// 组装拆卸子
            OthOutIntoWhs othOutWhs = new OthOutIntoWhs();// 其他出库主
            OthOutIntoWhs othIntoWhs = new OthOutIntoWhs();// 其他入库主
            List<OthOutIntoWhsSubTab> othOutWhsSubTabs = new ArrayList<>();// 其他出库子
            List<OthOutIntoWhsSubTab> othIntoWhsSubTabs = new ArrayList<>();// 其他入库子

            // 仓库中
            AmbDisambSngl ambDisambSngl = ambDisambSnglMapper.selectISChr(danHao);
            if (ambDisambSngl == null) {
                throw new RuntimeException("单号：" + danHao + " 不存在 ");
            }
            if (ambDisambSngl.getIsNtChk() == 1) {
                throw new RuntimeException("单据：" + ambDisambSngl.getFormNum() + " 已审核,不需要再次审核 ");
            }
            ambDisambSngl.setChkr(name);
            ambDisambSngl.setChkTm(loginTime);
            // 跟新审核状态

            int a = ambDisambSnglMapper.updateASnglChk(Arrays.asList(ambDisambSngl));
            if (a < 1) {
                isSuccess = false;
                throw new RuntimeException("单据：" + danHao + "审核异常");
            }
            message = ambDisambSngl.getFormNum() + "单据审核成功";

            if (ambDisambSngl.getFormTyp().equals("组装")) {

                // 入库子===新增其他出入库====
                String numberz = null;
                try {
                    numberz = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {
                    throw new RuntimeException("单号获取失败");
                } // 获取订单号
                othOutWhs.setFormNum(numberz);// 订单号
                othOutWhs.setFormDt(formDate);
                othOutWhs.setOutIntoWhsTypId("4");// 组装入库类型
                othOutWhs.setOutStatus("处理中");// 入库状态
                othOutWhs.setSetupPers(name);// 创建人
                othOutWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// 仓库编码
                othOutWhs.setSrcFormNum(danHao);// 来源单据号
                othOutWhs.setMemo(ambDisambSngl.getMemo());
                othOutWhs.setFormTypEncd("015");
                int oth = othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);

                // 组装拆卸单子表信息 仓库
                aSnglubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(danHao);
                if (aSnglubTabList.size() == 0) {
                    throw new RuntimeException("单据：" + danHao + "没有表体信息，审核失败");
                }
                isSuccess = true;
                for (AmbDisambSnglubTab tab : aSnglubTabList) {
                    InvtyTab inTabZi = new InvtyTab();
                    inTabZi.setWhsEncd(tab.getWhsEncd());
                    inTabZi.setInvtyEncd(tab.getSubKitEncd());
                    inTabZi.setBatNum(tab.getSbatNum());
                    inTabZi.setAvalQty(tab.getSubQty());
                    InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(inTabZi);
                    Optional.ofNullable(inTab).orElseThrow(() -> new RuntimeException("单据：" + danHao + "中,仓库：" + tab.getWhsEncd() + ",存货："
                            + tab.getSubKitEncd() + ",批号：" + tab.getSbatNum() + "对应的库存不存在, 审核失败！"));

                    if (inTab.getAvalQty().compareTo(inTabZi.getAvalQty()) == 1 || inTab.getAvalQty().compareTo(inTabZi.getAvalQty()) == 0) {

                        invtyNumMapper.updateAInvtyTab(Arrays.asList(inTabZi));// 修改库存表可用量 可用量减少
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
                        throw new RuntimeException("单据：" + danHao + "中,仓库：" + tab.getWhsEncd() + ",存货："
                                + tab.getSubKitEncd() + ",批号：" + tab.getSbatNum() + "对应的库存数量不足, 审核失败！");
                    }
                }
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);// 新增其他zhu子表
                // 入库主===新增其他出入库====
                String number = null;
                try {
                    number = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("单号获取失败");
                } // 获取订单号
                othIntoWhs.setFormNum(number);// 订单号
                othIntoWhs.setFormDt(formDate);
                othIntoWhs.setOutIntoWhsTypId("3");// 组装出库类型
                othIntoWhs.setInStatus("处理中");// 出库状态
                othIntoWhs.setSetupPers(name);// 创建人
                othIntoWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// 仓库编码
                othIntoWhs.setSrcFormNum(danHao);// 来源单据号
                othIntoWhs.setMemo(ambDisambSngl.getMemo());// 备注
                othIntoWhs.setFormTypEncd("014");

                othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);// 新增其他入库主

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
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);// 新增其他入子表

            } else if (ambDisambSngl.getFormTyp().equals("拆卸")) {

                othOutWhs = new OthOutIntoWhs();// 出库主
                othIntoWhs = new OthOutIntoWhs();// 入库主
                othOutWhsSubTabs = new ArrayList<>();// 出库子
                othIntoWhsSubTabs = new ArrayList<>();// 入库子

                // 新增其他出入库====拆 订单号 主 的主表
                String number = null;
                try {
                    number = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("单号获取失败");
                } // 获取订单号

                othOutWhs.setFormNum(number);// 订单号
                othOutWhs.setFormDt(formDate);
                othOutWhs.setOutIntoWhsTypId("6");// 组装入库类型
                othOutWhs.setOutStatus("处理中");// 入库状态
                othOutWhs.setSetupPers(name);// 创建人
                othOutWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// 仓库编码
                othOutWhs.setSrcFormNum(danHao);// 来源单据号
                othOutWhs.setFormTypEncd("015");

                // 新增其他出库主 主
                othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);

                // 新增其他出入库====拆 订单号 子 的主表
                String numberz = null;
                try {
                    numberz = getOrderNo.getSeqNo("OTCR", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("单号获取失败");
                } // 获取订单号

                othIntoWhs.setFormNum(numberz);// 订单号

                othIntoWhs.setFormDt(formDate);
                othIntoWhs.setOutIntoWhsTypId("5");// 组装入库类型
                othIntoWhs.setInStatus("处理中");// 入库状态
                othIntoWhs.setSetupPers(name);// 创建人
                othIntoWhs.setWhsEncd(ambDisambSngl.getWhsEncd());// 仓库编码
                othIntoWhs.setSrcFormNum(danHao);// 来源单据号
                othIntoWhs.setFormTypEncd("014");

                // 新增其他入库子 主
                othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);

                InvtyTab inTabZi = new InvtyTab();
                inTabZi.setWhsEncd(ambDisambSngl.getWhsEncd());
                inTabZi.setInvtyEncd(ambDisambSngl.getMomKitEncd());
                inTabZi.setBatNum(ambDisambSngl.getMbatNum());
                inTabZi.setAvalQty(ambDisambSngl.getMomQty());
                InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(inTabZi);

                Optional.ofNullable(inTab).orElseThrow(() -> new RuntimeException("单据：" + danHao + "中,仓库：" + ambDisambSngl.getWhsEncd()
                        + ",存货：" + ambDisambSngl.getMomKitEncd() + ",批号：" + ambDisambSngl.getMbatNum() + "对应的库存不存在, 审核失败！"));
                if (inTab.getAvalQty().compareTo(ambDisambSngl.getMomQty()) == 1 || inTab.getAvalQty().compareTo(ambDisambSngl.getMomQty()) == 0) {
                    invtyNumMapper.updateAInvtyTab(Arrays.asList(inTabZi));// 修改库存表可用量 可用量减少 主
                    isSuccess = true;
                } else if (inTab.getAvalQty().compareTo(ambDisambSngl.getMomQty()) == -1) {
                    throw new RuntimeException("单据：" + danHao + "中,仓库：" + ambDisambSngl.getWhsEncd() + ",存货："
                            + ambDisambSngl.getMomKitEncd() + ",批号：" + ambDisambSngl.getMbatNum() + "对应的库存数量不足, 审核失败！");
                }
                // 主表的 子表
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

                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);// 新增其他出子表
                aSnglubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(danHao);
                if (aSnglubTabList.size() == 0) {
                    throw new RuntimeException("单据：" + danHao + "没有表体信息，审核失败");

                }
                for (AmbDisambSnglubTab dbzi : aSnglubTabList) {

//					// 生成其他出库子表
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

                // 新增其他出库子表
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);

            }else {
                throw new RuntimeException("单据：" + danHao + "类型异常");
            	}

        } catch (Exception e) {


            throw new RuntimeException(e.getMessage());
        }

        return message;
    }

    // 弃审
    @Override
    public String updateASnglNoChk(String userId, String jsonBody, String name) {
        String message = "";

        ObjectNode dbzhu = null;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }

        List<AmbDisambSngl> aList = new ArrayList<>();// 组装拆卸主
        List<AmbDisambSnglubTab> aSnglubTabList = new ArrayList<>();// 组装拆卸子
        aList = new ArrayList<>();// 组装拆卸主
        aSnglubTabList = new ArrayList<>();// 组装拆卸子

        String danHao = dbzhu.get("formNum").asText();
        AmbDisambSngl ambDisambSngl = ambDisambSnglMapper.selectISChr(danHao);
        if (ambDisambSngl == null) {
            throw new RuntimeException("单号：" + danHao + " 不存在 ");
        }
        if (ambDisambSngl.getIsNtChk() == 0) {
            throw new RuntimeException("单据:" + danHao + "已弃审,不需要重复弃审 ");
        }
        ambDisambSngl.setChkr(name);
        OthOutIntoWhs   intoWhs = null;
        OthOutIntoWhs   outWhs = null;
        // 查询是否有其他出入库
        String srcFromNum = ambDisambSngl.getFormNum();
        if("组装".equals(ambDisambSngl.getFormTyp())) {
        	intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "3");
        	outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "4");
        }else if("拆卸".equals(ambDisambSngl.getFormTyp())) {
          intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "5");
          outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(srcFromNum, "6");
        }else {
            throw new RuntimeException("单据：" + danHao + "类型异常");
        	}
        if (intoWhs == null || outWhs == null ) {
            throw new RuntimeException("单号：" + danHao + "对应的其他出入库存在异常");
            }
        
        if (intoWhs.getIsNtChk() == 1 || outWhs.getIsNtChk() == 1) {
            throw new RuntimeException("单号：" + danHao + "对应的其他出入库已审核");
        } else {
            //删除其他出入库的数据，进行弃审操作
            List<String> lists = Arrays.asList(intoWhs.getFormNum(), outWhs.getFormNum());
            othOutIntoWhsMapper.insertOthOutIntoWhsDl(lists);
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTabDl(lists);
            othOutIntoWhsMapper.deleteAllOthOutIntoWhs(lists);
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

                int a = ambDisambSnglMapper.updateASnglNoChk(Arrays.asList(ambDisambSngl));
                if (a == 1) {
                    message = ambDisambSngl.getFormNum() + "单据弃审成功";

                    aSnglubTabList = ambDisambSnglMapper.selectAmbDisambSnglubTabList(srcFromNum);
                    if (aSnglubTabList.size() == 0) {
                        throw new RuntimeException("单号：" + danHao + "没有表体信息");
                    }
                    // 判断是组装单还是拆卸单进行代码逻辑判断
                    if (ambDisambSngl.getFormTyp().equals("组装")) {

                        for (AmbDisambSnglubTab dbzi : aSnglubTabList) {
                            InvtyTab inTabZi = new InvtyTab();
                            inTabZi.setWhsEncd(dbzi.getWhsEncd());// 仓库编码
                            inTabZi.setInvtyEncd(dbzi.getSubKitEncd());// 存货编码
                            inTabZi.setBatNum(dbzi.getSbatNum());// 批号
//							inTabZi.setNowStok(BigDecimal.ZERO);// 现存量
                            inTabZi.setAvalQty(dbzi.getSubQty());// 可用量
                            inTabZi.setPrdcDt(dbzi.getSprdcDt());// 生产日期
                            inTabZi.setBaoZhiQi(dbzi.getBaoZhiQi());// 保质期
                            inTabZi.setInvldtnDt(dbzi.getSinvldtnDt());// 失效日期
                            inTabZi.setTaxRate(dbzi.getTaxRate());// 税率

                            // 修改库存表入库数量
                            if (invtyNumMapper.selectInvtyTabByTerm(inTabZi) != null) {
                                // 如果其他入库 不等于空 修改库存表数量
                                invtyNumMapper.updateAInvtyTabJia(Arrays.asList(inTabZi));// 修改库存表可用量 可用量增加
                            } else {
                                // 新增库存表 入库
                                invtyNumMapper.insertInvtyTabList(Arrays.asList(inTabZi));
                            }

                        }

                    } else if (ambDisambSngl.getFormTyp().equals("拆卸")) {// true 审核成功

                        // 库存表
                        InvtyTab invtyTabZhu = new InvtyTab();
                        invtyTabZhu.setWhsEncd(ambDisambSngl.getWhsEncd());
                        invtyTabZhu.setInvtyEncd(ambDisambSngl.getMomKitEncd());// 存货编码
                        invtyTabZhu.setBatNum(ambDisambSngl.getMbatNum());// 批号
//						invtyTabZhu.setNowStok(BigDecimal.ZERO);// 现存量
                        invtyTabZhu.setTaxRate(ambDisambSngl.getTaxRate());// 税率
                        invtyTabZhu.setAvalQty(ambDisambSngl.getMomQty());// 可用量

                        invtyTabZhu.setPrdcDt(ambDisambSngl.getMprdcDt());
                        invtyTabZhu.setBaoZhiQi(ambDisambSngl.getBaoZhiQi());
                        invtyTabZhu.setInvldtnDt(ambDisambSngl.getInvldtnDt());
                        invtyTabZhu.setTaxRate(ambDisambSngl.getTaxRate());// 税率

                        if (invtyNumMapper.selectInvtyTabByTerm(invtyTabZhu) != null) {
                            // 如果其他入库 不等于空 修改库存表数量
                            invtyNumMapper.updateAInvtyTabJia(Arrays.asList(invtyTabZhu));// 修改库存表可用量 可用量增加
                        } else {
                            // 新增库存表 入库
                            invtyNumMapper.insertInvtyTabList(Arrays.asList(invtyTabZhu));
                        }
                    }

                } else {
                    throw new RuntimeException("单据：" + danHao + "单据弃审失败！");

                }

        return message;
    }

    // 打印
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

            resp = BaseJson.returnRespObjListAnno("whs/amb_disamb_sngl/queryListDaYin", true, "查询成功！", null, aList);
//			resp = BaseJson.returnRespObjList("whs/amb_disamb_sngl/queryListDaYin", true, "查询成功！", null, aList);
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");
            }
            try {
                ambDisambSnglMapper.exInsertAmbDisambSngl(entry.getValue());
                ambDisambSnglMapper.exInsertAmbDisambSnglubTab(entry.getValue().getAmbSnglubList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");

            }
        }

        isSuccess = true;
        message = "组装拆卸单新增成功！";
        try {
            resp = BaseJson.returnRespObj("whs/amb_disamb_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // 导入excle
    private Map<String, AmbDisambSngl> uploadScoreInfo(MultipartFile file) {
        Map<String, AmbDisambSngl> temp = new HashMap<>();
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

                String orderNo = GetCellData(r, "单据号");
                // 创建实体类
                // System.out.println(orderNo);
                AmbDisambSngl ambDisambSngl = new AmbDisambSngl();
                if (temp.containsKey(orderNo)) {
                    ambDisambSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                List<AmbDisambSnglubTab> ambDisambSnglubList = ambDisambSngl.getAmbSnglubList();// 订单子表
                if (ambDisambSnglubList == null) {
                    ambDisambSnglubList = new ArrayList<>();
                }

                ambDisambSngl.setMomKitEncd(GetCellData(r, "母件编码")); // varchar(200 '母配套件编码',1
                ambDisambSngl.setFormNum(orderNo); // varchar(200 '单据号', 1
                ambDisambSngl.setFormDt(GetCellData(r, "单据日期")); // datetime '单据日期',1
                ambDisambSngl.setFormTyp(GetCellData(r, "单据类型(组装、拆卸)")); // varchar(200 '单据类型(组装、拆卸', 1
                ambDisambSngl.setFormTypEncd(GetCellData(r, "单据类型编码"));// 单据类型编码

                ambDisambSngl.setFee(new BigDecimal(
                        GetCellData(r, "费用") == null || GetCellData(r, "费用").equals("") ? "0" : GetCellData(r, "费用"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,0 '费用',1
                ambDisambSngl.setFeeComnt(GetCellData(r, "费用说明")); // varchar(2000 '费用说明',
                ambDisambSngl.setMemo(GetCellData(r, "备注")); // varchar(2000 '备注',1
                ambDisambSngl.setTyp(GetCellData(r, "类型(套件、散件)")); // varchar(200 '类型(套件、散件',
                ambDisambSngl.setWhsEncd(GetCellData(r, "母件仓库编码")); // varchar(200 '仓库编码', 1
                ambDisambSngl.setMomQty(
                        new BigDecimal(GetCellData(r, "母件数量") == null || GetCellData(r, "母件数量").equals("") ? "0"
                                : GetCellData(r, "母件数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '母件数量', 1
                ambDisambSngl.setTaxRate(new BigDecimal(
                        GetCellData(r, "母件税率") == null || GetCellData(r, "母件税率").equals("") ? "0" : GetCellData(r, "母件税率"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 '税率',
                ambDisambSngl.setMcntnTaxUprc(
                        new BigDecimal(GetCellData(r, "母件含税单价") == null || GetCellData(r, "母件含税单价").equals("") ? "0"
                                : GetCellData(r, "母件含税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '含税单价', 1
                ambDisambSngl.setMunTaxUprc(
                        new BigDecimal(GetCellData(r, "母件未税单价") == null || GetCellData(r, "母件未税单价").equals("") ? "0"
                                : GetCellData(r, "母件未税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '未税单价',
                ambDisambSngl.setMcntnTaxAmt(
                        new BigDecimal(GetCellData(r, "母件含税金额") == null || GetCellData(r, "母件含税金额").equals("") ? "0"
                                : GetCellData(r, "母件含税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '含税金额',
                ambDisambSngl.setMunTaxAmt(
                        new BigDecimal(GetCellData(r, "母件未税金额") == null || GetCellData(r, "母件未税金额").equals("") ? "0"
                                : GetCellData(r, "母件未税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8
                // '未税金额', 1
                ambDisambSngl.setMbatNum(GetCellData(r, "母件批号")); // varchar(200 '批号', 1
                ambDisambSngl.setMprdcDt(
                        GetCellData(r, "母件生产日期") == null ? "" : GetCellData(r, "母件生产日期")); // datetime
                // '生产日期',
                ambDisambSngl.setBaoZhiQi(GetCellData(r, "母件保质期")); // varchar(200 '保质期',
                ambDisambSngl.setInvldtnDt(
                        GetCellData(r, "母件失效日期") == null ? "" : GetCellData(r, "母件失效日期")); // datetime
                // '失效日期',
//					ambDisambSngl.setIsNtWms((new Double(GetCellData(r, "是否向WMS上传"))).intValue()); // int(11 '是否向WMS上传',
                ambDisambSngl.setIsNtChk(
                        (new Double(GetCellData(r, "是否审核") == null || GetCellData(r, "是否审核").equals("") ? "0"
                                : GetCellData(r, "是否审核"))).intValue()); // int(11 '是否审核',
                ambDisambSngl.setIsNtBookEntry(
                        (new Double(GetCellData(r, "是否记账") == null || GetCellData(r, "是否记账").equals("") ? "0"
                                : GetCellData(r, "是否记账"))).intValue()); // int(11 '是否记账',
//                ambDisambSngl.setIsNtCmplt(
//                        (new Double(GetCellData(r, "是否完成") == null || GetCellData(r, "是否完成").equals("") ? "0"
//                                : GetCellData(r, "是否完成"))).intValue()); // int(11 '是否完成',
//                ambDisambSngl.setIsNtClos(
//                        (new Double(GetCellData(r, "是否关闭") == null || GetCellData(r, "是否关闭").equals("") ? "0"
//                                : GetCellData(r, "是否关闭"))).intValue()); // int(11 '是否关闭',
//                ambDisambSngl.setPrintCnt(
//                        (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
//                                : GetCellData(r, "打印次数"))).intValue()); // int(11 '打印次数',
                ambDisambSngl.setSetupPers(GetCellData(r, "创建人")); // varchar(200 '创建人',
                ambDisambSngl.setSetupTm(
                        GetCellData(r, "创建时间") == null ? "" : GetCellData(r, "创建时间")); // datetime
                // '创建时间',
                ambDisambSngl.setMdfr(GetCellData(r, "修改人")); // varchar(200 '修改人',
                ambDisambSngl.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间")); // datetime
                // '修改时间',
                ambDisambSngl.setChkr(GetCellData(r, "审核人")); // varchar(200 '审核人',
                ambDisambSngl.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间")); // datetime
                // '审核时间',
                ambDisambSngl.setBookEntryPers(GetCellData(r, "记账人")); // varchar(200 '记账人',
                ambDisambSngl.setBookEntryTm(
                        GetCellData(r, "记账时间") == null ? "" : GetCellData(r, "记账时间")); // datetime
                ambDisambSngl.setBxQty(new BigDecimal(
                        GetCellData(r, "母件箱数") == null || GetCellData(r, "母件箱数").equals("") ? "0" : GetCellData(r, "母件箱数"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 箱数 a decimal(208 // //
                // '记账时间',

                AmbDisambSnglubTab AmbDisambSnglubTab = new AmbDisambSnglubTab();

//					AmbDisambSnglubTab.setOrdrNum(ordrNum); // bigint(20 '序号',
                AmbDisambSnglubTab.setFormNum(orderNo); // varchar(200 // '主表单据号',
                AmbDisambSnglubTab.setSubKitEncd(GetCellData(r, "子件编码")); // varchar(200 // '子配套件编码',
                AmbDisambSnglubTab.setWhsEncd(GetCellData(r, "子件仓库编码")); // varchar(200 // '仓库编码',
                AmbDisambSnglubTab.setSubQty(
                        new BigDecimal(GetCellData(r, "子件数量") == null || GetCellData(r, "子件数量").equals("") ? "0"
                                : GetCellData(r, "子件数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '子件数量',
                AmbDisambSnglubTab.setMomQty(
                        new BigDecimal(GetCellData(r, "母件数量") == null || GetCellData(r, "母件数量").equals("") ? "0"
                                : GetCellData(r, "母件数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '母件数量',
                AmbDisambSnglubTab.setMomSubRatio(
                        new BigDecimal(GetCellData(r, "母子比例") == null || GetCellData(r, "母子比例").equals("") ? "0"
                                : GetCellData(r, "母子比例")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '母子比例',
                AmbDisambSnglubTab.setTaxRate(new BigDecimal(
                        GetCellData(r, "子件税率") == null || GetCellData(r, "子件税率").equals("") ? "0" : GetCellData(r, "子件税率"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 // '税率',
                AmbDisambSnglubTab.setScntnTaxUprc(
                        new BigDecimal(GetCellData(r, "子件含税单价") == null || GetCellData(r, "子件含税单价").equals("") ? "0"
                                : GetCellData(r, "子件含税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '含税单价',
                AmbDisambSnglubTab.setSunTaxUprc(
                        new BigDecimal(GetCellData(r, "子件未税单价") == null || GetCellData(r, "子件未税单价").equals("") ? "0"
                                : GetCellData(r, "子件未税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '未税单价',
                AmbDisambSnglubTab.setScntnTaxAmt(
                        new BigDecimal(GetCellData(r, "子件含税金额") == null || GetCellData(r, "子件含税金额").equals("") ? "0"
                                : GetCellData(r, "子件含税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '含税金额',
                AmbDisambSnglubTab.setSunTaxAmt(
                        new BigDecimal(GetCellData(r, "子件未税金额") == null || GetCellData(r, "子件未税金额").equals("") ? "0"
                                : GetCellData(r, "子件未税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // decimal(20,8 //
                // '未税金额',
                AmbDisambSnglubTab.setSbatNum(GetCellData(r, "子件批号")); // varchar(200 // '批号',
                AmbDisambSnglubTab.setSprdcDt(
                        GetCellData(r, "子件生产日期") == null ? "" : GetCellData(r, "子件生产日期")); // datetime
                // //
                // '生产日期',
                AmbDisambSnglubTab.setBaoZhiQi(GetCellData(r, "子件保质期")); // varchar(200 // '保质期',
                AmbDisambSnglubTab.setSinvldtnDt(
                        GetCellData(r, "子件失效日期") == null ? "" : GetCellData(r, "子件失效日期")); // datetime
                // '失效日期',
                AmbDisambSnglubTab.setSbxQty(new BigDecimal(
                        GetCellData(r, "子件箱数") == null || GetCellData(r, "子件箱数").equals("") ? "0" : GetCellData(r, "子件箱数"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 箱数 a decimal(208 // //
                AmbDisambSnglubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                ambDisambSnglubList.add(AmbDisambSnglubTab);

                ambDisambSngl.setAmbSnglubList(ambDisambSnglubList);

                temp.put(orderNo, ambDisambSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");
            }
            try {
                ambDisambSnglMapper.exInsertAmbDisambSngl(entry.getValue());
                ambDisambSnglMapper.exInsertAmbDisambSnglubTab(entry.getValue().getAmbSnglubList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");

            }
        }

        isSuccess = true;
        message = "组装拆卸单新增成功！";
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

                String orderNo = GetCellData(r, "单据号");

                String formTyp = null;
                String formTypEncd = null;
                if (GetCellData(r, "单据类型名").equals("组装单")) {

                    orderNo = "zc" + orderNo;
                    formTyp = "组装";
                    formTypEncd = "012";
                } else if (GetCellData(r, "单据类型名").equals("拆卸单")) {

                    orderNo = "cz" + orderNo;
                    formTyp = "拆卸";
                    formTypEncd = "013";

                }

                // 创建实体类
                // System.out.println("\t" + orderNo);

                AmbDisambSngl ambDisambSngl = new AmbDisambSngl();
                if (temp.containsKey(orderNo)) {
                    ambDisambSngl = temp.get(orderNo);
                }
                String BaoZhiQi = GetCellData(r, "保质期");

                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                String mprdcDt = GetCellData(r, "生产日期") == null ? ""
                        : GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " ");
                if (mprdcDt == null || mprdcDt.equals("")) {
                    mprdcDt = null;
                }
                String invldtnDt = null;
                if (mprdcDt != null) {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate ldt = LocalDate.parse(mprdcDt, df);
                    invldtnDt = ldt.plusDays(Long.valueOf(BaoZhiQi == null ? "0" : BaoZhiQi)).toString();
                }

                if (GetCellData(r, "类型").equals("套件")) {
                    ambDisambSngl.setMomKitEncd(GetCellData(r, "存货编码")); // 母配套件编码
                    ambDisambSngl.setFormNum(orderNo); // 单据号
                    ambDisambSngl.setFormDt(
                            GetCellData(r, "日期") == null ? "" : GetCellData(r, "日期").replaceAll("[^0-9:-]", " ")); // 单据日期
                    ambDisambSngl.setFormTyp(formTyp); // 单据类型组装、拆卸
                    ambDisambSngl.setFormTypEncd(formTypEncd);// 单据类型编码

                    ambDisambSngl
                            .setFee(new BigDecimal(GetCellData(r, "费用") == null || GetCellData(r, "费用").equals("") ? "0"
                                    : GetCellData(r, "费用")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 费用
                    ambDisambSngl.setFeeComnt(null); // 费用说明
                    ambDisambSngl.setMemo(GetCellData(r, "备注")); // 备注
                    ambDisambSngl.setTyp("套件"); // 类型 套件 散件
                    ambDisambSngl.setWhsEncd(GetCellData(r, "仓库编码")); // 仓库编码
                    ambDisambSngl.setMomQty(
                            new BigDecimal(GetCellData(r, "数量") == null || GetCellData(r, "数量").equals("") ? "0"
                                    : GetCellData(r, "数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 母件数量
                    ambDisambSngl.setTaxRate(BigDecimal.ZERO); // 税率
                    ambDisambSngl.setMcntnTaxUprc(BigDecimal.ZERO); // 含税单价
                    ambDisambSngl.setMunTaxUprc(
                            new BigDecimal(GetCellData(r, "单价") == null || GetCellData(r, "单价").equals("") ? "0"
                                    : GetCellData(r, "单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税单价
                    ambDisambSngl.setMcntnTaxAmt(BigDecimal.ZERO); // 含税金额
                    ambDisambSngl.setMunTaxAmt(
                            new BigDecimal(GetCellData(r, "金额") == null || GetCellData(r, "金额").equals("") ? "0"
                                    : GetCellData(r, "金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税金额
                    ambDisambSngl.setMbatNum(GetCellData(r, "批号")); // varchar(200 '批号', 1

                    ambDisambSngl.setMprdcDt(mprdcDt); // 生产日期

                    ambDisambSngl.setBaoZhiQi(BaoZhiQi); // 保质期

                    ambDisambSngl.setInvldtnDt(invldtnDt); // 失效日期
                    ambDisambSngl.setIsNtWms(0); // 是否向WMS上传
                    ambDisambSngl.setIsNtChk(0); // int(11 '是否审核',
                    ambDisambSngl.setIsNtBookEntry(0); // int(11 '是否记账',
                    ambDisambSngl.setIsNtCmplt(0); // int(11 '是否完成',
                    ambDisambSngl.setIsNtClos(0); // int(11 '是否关闭',
                    ambDisambSngl.setPrintCnt(
                            (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
                                    : GetCellData(r, "打印次数"))).intValue()); // int(11 '打印次数',
                    ambDisambSngl.setSetupPers(GetCellData(r, "制单人")); // varchar(200 '创建人',
                    ambDisambSngl.setSetupTm(
                            GetCellData(r, "制单时间") == null ? "" : GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " ")); // datetime
                    // '创建时间',
                    ambDisambSngl.setMdfr(GetCellData(r, "修改人")); // varchar(200 '修改人',
                    ambDisambSngl.setModiTm(
                            GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ")); // datetime
                    // '修改时间',
                    ambDisambSngl.setChkr(GetCellData(r, "审核人")); // varchar(200 '审核人',
                    ambDisambSngl.setChkTm(
                            GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " ")); // datetime
                    // '审核时间',
                    ambDisambSngl.setBookEntryPers(null); // 记账人
                    ambDisambSngl.setBookEntryTm(null); // 记账人时间

                    BigDecimal bxRule = GetCellData(r, "箱规") == null || GetCellData(r, "箱规").equals("") ? null
                            : new BigDecimal(GetCellData(r, "箱规"));
                    if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                        ambDisambSngl.setBxQty(ambDisambSngl.getMomQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // 箱数
                    } else {
                        ambDisambSngl.setBxQty(BigDecimal.ZERO);// 箱数
                    }

                    temp.put(orderNo, ambDisambSngl);

                } else if (GetCellData(r, "类型").equals("散件")) {
                    // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                    List<AmbDisambSnglubTab> ambDisambSnglubList = ambDisambSngl.getAmbSnglubList();// 订单子表
                    if (ambDisambSnglubList == null) {
                        ambDisambSnglubList = new ArrayList<>();
                    }
                    AmbDisambSnglubTab AmbDisambSnglubTab = new AmbDisambSnglubTab();

//					AmbDisambSnglubTab.setOrdrNum(ordrNum); // bigint(20 '序号',
                    AmbDisambSnglubTab.setFormNum(orderNo); // varchar(200 // 主表单据号
                    AmbDisambSnglubTab.setSubKitEncd(GetCellData(r, "存货编码")); // 子配套件编码
                    AmbDisambSnglubTab.setWhsEncd(GetCellData(r, "仓库编码")); // 仓库编码
                    AmbDisambSnglubTab.setSubQty(
                            new BigDecimal(GetCellData(r, "数量") == null || GetCellData(r, "数量").equals("") ? "0"
                                    : GetCellData(r, "数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 子件数量
                    AmbDisambSnglubTab.setMomQty(ambDisambSngl.getMomQty()); // 母件数量
                    AmbDisambSnglubTab.setMomSubRatio(
                            new BigDecimal(GetCellData(r, "基本用量") == null || GetCellData(r, "基本用量").equals("") ? "0"
                                    : GetCellData(r, "基本用量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 母子比例
                    AmbDisambSnglubTab.setTaxRate(BigDecimal.ZERO); // 税率
                    AmbDisambSnglubTab.setScntnTaxUprc(BigDecimal.ZERO); // 含税单价
                    AmbDisambSnglubTab.setSunTaxUprc(
                            new BigDecimal(GetCellData(r, "单价") == null || GetCellData(r, "单价").equals("") ? "0"
                                    : GetCellData(r, "单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税单价
                    AmbDisambSnglubTab.setScntnTaxAmt(BigDecimal.ZERO); // 含税金额
                    AmbDisambSnglubTab.setSunTaxAmt(
                            new BigDecimal(GetCellData(r, "金额") == null || GetCellData(r, "金额").equals("") ? "0"
                                    : GetCellData(r, "金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税金额
                    AmbDisambSnglubTab.setSbatNum(GetCellData(r, "批号")); // 批号
                    AmbDisambSnglubTab.setSprdcDt(mprdcDt); // 生产日期
                    AmbDisambSnglubTab.setBaoZhiQi(BaoZhiQi); // 保质期
                    AmbDisambSnglubTab.setSinvldtnDt(invldtnDt); // 失效日期

                    BigDecimal bxRule = GetCellData(r, "箱规") == null || GetCellData(r, "箱规").equals("") ? null
                            : new BigDecimal(GetCellData(r, "箱规"));
                    if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                        AmbDisambSnglubTab
                                .setSbxQty(AmbDisambSnglubTab.getSubQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // 箱数
                    } else {
                        AmbDisambSnglubTab.setSbxQty(BigDecimal.ZERO);// 箱数
                    }
                    AmbDisambSnglubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                    ambDisambSnglubList.add(AmbDisambSnglubTab);
                    ambDisambSngl.setAmbSnglubList(ambDisambSnglubList);
                    temp.put(orderNo, ambDisambSngl);

                }

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());
        }
        return temp;
    }
}
