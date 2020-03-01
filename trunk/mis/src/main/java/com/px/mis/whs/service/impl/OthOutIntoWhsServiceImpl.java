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
    // 订单号
    @Autowired
    GetOrderNo getOrderNo;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    @Autowired
    IntoWhsMapper intoWhsMapper;
    // 货位
    @Autowired
    InvtyGdsBitListMapper bitListMapper;
    /**
     * 仓库
     */
    @Autowired
    WhsDocMapper whsDocMapper;
    // 调拨
    @Autowired
    CannibSnglMapper cannibSnglMapper;
    // 组装
    @Autowired
    AmbDisambSnglMapper ambDisambSnglMapper;
    // 损益表
    @Autowired
    CheckPrftLossMapper checkPrftLossMapper;
    // 盘点
    @Autowired
    CheckSnglMapper checkSnglMapper;
    @Autowired
    InvtyDocDao invtyDocDao;
    // 新增其他出入库
    @Override
    public String insertOthOutIntoWhs(String userId, OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList, String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        if (outIntoWhs.getOutIntoWhsTypId().equals("11")) {// 其他出库
            outIntoWhs.setOutIntoWhsTypId("11");
            outIntoWhs.setOutStatus("处理中");
            outIntoWhs.setFormTypEncd("015");

        } else if (outIntoWhs.getOutIntoWhsTypId().equals("12")) {// 其他入库
            outIntoWhs.setOutIntoWhsTypId("12");
            outIntoWhs.setInStatus("处理中");
            outIntoWhs.setFormTypEncd("014");

        }

        String number = getOrderNo.getSeqNo("OTCR", userId, loginTime);// 获取订单号
        if (othOutIntoWhsMapper.selectOthOutIntoWhs(number) != null) {
            message = "编号" + number + "已存在，请重新输入！";
            isSuccess = false;
        } else {
            if (othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(number).size() > 0) {
                othOutIntoWhsMapper.deleteOthOutIntoWhsSubTab(number);
            }

            outIntoWhs.setFormNum(number);// 主表订单号
            othOutIntoWhsMapper.insertOthOutIntoWhs(outIntoWhs);
            for (OthOutIntoWhsSubTab oIntoWhsSubTab : oList) {
                oIntoWhsSubTab.setFormNum(outIntoWhs.getFormNum());// 主表订单号标识
                InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(oIntoWhsSubTab.getInvtyEncd()))
                        .orElseThrow(() -> new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"该存货不存在"));
                Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
                if(sisQuaGuaPer == 1){
                    if(StringUtils.isBlank(oIntoWhsSubTab.getBaoZhiQi())|| StringUtils.isBlank(oIntoWhsSubTab.getPrdcDt())
                            || StringUtils.isBlank(oIntoWhsSubTab.getInvldtnDt()) ){
                        throw new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"保质期管理存货日期设置错误");
                    }
                }else{
                    oIntoWhsSubTab.setBaoZhiQi(null);
                    oIntoWhsSubTab.setPrdcDt(null);
                    oIntoWhsSubTab.setInvldtnDt(null);
                }
            }
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(oList);
            message = "新增成功";

        }
        /**
         * // 其他出入库保存 出库减少可用量 入库增加可用量 // 判断可用量是否充足 Boolean enough = true; for (InvtyTab
         * iTab : iList) { InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(iTab);
         *
         * for (OthOutIntoWhsSubTab oSubTab : oList) {
         *
         * if (inTab.getAvalQty().compareTo(oSubTab.getQty()) == 1 ||
         * inTab.getAvalQty().compareTo(oSubTab.getQty()) == 0) {
         *
         * // 其他出入库类型 11:其它出库 12:其他入库 if
         * (Integer.parseInt(outIntoWhs.getOutIntoWhsTypId()) == 11) {
         * invtyNumMapper.updateAInvtyTab(iList);// 可用量减少 出库 } else if
         * (Integer.parseInt(outIntoWhs.getOutIntoWhsTypId()) == 12) { // 判断库存表中是否存在 for
         * (InvtyTab iTab1 : iList) { InvtyTab inTab1 =
         * invtyNumMapper.selectInvtyTabByTerm(iTab1); if (inTab1 != null) {
         * invtyNumMapper.updateAInvtyTabJia(iList);// 可用量增加 入库 } else { // 新增库存表
         * // System.out.println("新增库存表 其他出入库新建 有金额");
         * invtyNumMapper.insertInvtyTabList(iList); }
         *
         * } }
         *
         * message = "新增成功！"; isSuccess = true;
         *
         * }
         *
         * if (inTab.getAvalQty().compareTo(oSubTab.getQty()) == -1) { isSuccess = true;
         * message = "库存中数量不足"; enough = false; }
         *
         * } }
         *
         * if (enough) { outIntoWhs.setFormNum(number);// 主表订单号 BigDecimal unTaxUprc; //
         * 无税单价 BigDecimal qty; // 数量 BigDecimal taxRate; // 税率 String prdcDt; // 生产日期
         * String baoZhiQi; // 保质期 for (OthOutIntoWhsSubTab oIntoWhsSubTab : oList) {
         * oIntoWhsSubTab.setFormNum(outIntoWhs.getFormNum());// 主表订单号标识 // 获取未税单价
         * unTaxUprc = oIntoWhsSubTab.getUnTaxUprc(); // 获取未税数量 qty =
         * oIntoWhsSubTab.getQty(); // 获取税率 页面税率未整数，我们需要将税率/100
         * oIntoWhsSubTab.setTaxRate(oIntoWhsSubTab.getTaxRate().divide(new
         * BigDecimal(100))); taxRate = oIntoWhsSubTab.getTaxRate(); // 获取生产日期 prdcDt =
         * oIntoWhsSubTab.getPrdcDt(); // 获取保质期 baoZhiQi = oIntoWhsSubTab.getBaoZhiQi();
         * // 计算失效日期 oIntoWhsSubTab.setInvldtnDt(CalcAmt.getDate(prdcDt,
         * Integer.parseInt(baoZhiQi))); // 计算未税金额 金额=未税数量*未税单价
         * oIntoWhsSubTab.setUnTaxAmt(CalcAmt.noTaxAmt(unTaxUprc, qty)); // 计算含税单价
         * 含税单价=无税单价*税率+无税单价
         * oIntoWhsSubTab.setCntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc, qty, taxRate));
         * // 计算含税金额 含税金额=无税金额*税率+无税金额=税额+无税金额
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

    // 修改其他出入库
    @Override
    public String updateOthOutIntoWhs(OthOutIntoWhs outIntoWhs, List<OthOutIntoWhsSubTab> oList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> list = new ArrayList<>();
        list.add(outIntoWhs.getFormNum());

        OthOutIntoWhs oIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(outIntoWhs.getFormNum());
        if (oIntoWhs.getIsNtChk() == null) {
            throw new RuntimeException("单号审核状态异常");
        } else if (oIntoWhs.getIsNtChk() == 1) {
            throw new RuntimeException("单号已审核不允许修改");
        }
        othOutIntoWhsMapper.deleteOthOutIntoWhsSubTab(outIntoWhs.getFormNum());
        // 删除子表完成 该主表

        if (outIntoWhs.getOutIntoWhsTypId().equals("11")) {// 其他出库
            outIntoWhs.setOutIntoWhsTypId("11");
            outIntoWhs.setOutStatus("处理中");
            outIntoWhs.setInStatus(null);

        } else if (outIntoWhs.getOutIntoWhsTypId().equals("12")) {// 其他入库
            outIntoWhs.setOutIntoWhsTypId("12");
            outIntoWhs.setInStatus("处理中");
            outIntoWhs.setOutStatus(null);

        }

        othOutIntoWhsMapper.updateOthOutIntoWhs(outIntoWhs);

        for (OthOutIntoWhsSubTab oIntoWhsSubTab : oList) {
            oIntoWhsSubTab.setFormNum(outIntoWhs.getFormNum());// 主表订单号标识
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(oIntoWhsSubTab.getInvtyEncd()))
                    .orElseThrow(() -> new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"该存货不存在"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(oIntoWhsSubTab.getBaoZhiQi())|| StringUtils.isBlank(oIntoWhsSubTab.getPrdcDt())
                        || StringUtils.isBlank(oIntoWhsSubTab.getInvldtnDt()) ){
                    throw new RuntimeException(oIntoWhsSubTab.getInvtyEncd()+"保质期管理存货日期设置错误");
                }
            }else{
                oIntoWhsSubTab.setBaoZhiQi(null);
                oIntoWhsSubTab.setPrdcDt(null);
                oIntoWhsSubTab.setInvldtnDt(null);
            }
        }
        othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(oList);
        bitListMapper.deleteInvtyGdsBitList(Arrays.asList(outIntoWhs.getFormNum()));

        message = "更新成功！";

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/updateOthOutIntoWhs", isSuccess, message, outIntoWhs);
        } catch (IOException e) {

        }
        return resp;
    }

    // 批量删除
    @Override
    public String deleteAllOthOutIntoWhs(String formNum) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> list = getList(formNum);
        // System.out.println(list);

        List<OthOutIntoWhs> whsList = othOutIntoWhsMapper.selectOthOutIntoWhsList(list);
        List<String> lists = new ArrayList<String>();
        //        1		调拨入库
//        2		调拨出库
//        3		组装入库
//        4		组装出库
//        5		拆卸入库
//        6		拆卸出库
//        7		盘亏出库
//        8		盘盈入库
//        9		采购入库
//        10	销售出库
//        11	其他出库
//        12	其他入库
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
            message = "删除成功！" + lists.toString();
        } else {
            isSuccess = false;
            message = "编号" + stringSet + "已审或不允许直接删除";
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/deleteAllOthOutIntoWhs", isSuccess, message, null);
        } catch (IOException e) {

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
            // System.out.println(str[i]);
        }
        return list;
    }

    // 查询、分页查
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
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + formNum + "不存在！";
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
            resp = BaseJson.returnRespList("whs/out_into_whs/queryList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // PDA
    // 查询 入库
    @Override
    public String selectOINChkr(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<OthOutIntoWhs> outIntoWhs = othOutIntoWhsMapper.selectOINChkr(list);
        // System.out.println("outIntoWhs\t" + outIntoWhs.size());
        // **************推荐入库货位*****************

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
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOINChkr", true, "查询成功！", null, outIntoWhs);
            } else {
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOINChkr", false, "查询失败！", null, outIntoWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // 查询 出库
    @Override
    public String selectOutChkr(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<OthOutIntoWhs> outIntoWhs = othOutIntoWhsMapper.selectOutChkr(list);

        // **************推荐出库货位*****************

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
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOutChkr", true, "查询成功！", null, outIntoWhs);
            } else {
                resp = BaseJson.returnRespObjList("whs/out_into_whs/selectOutChkr", false, "查询失败！", null, outIntoWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // 入库
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
            othOutIntoWhsMapper.updateBat(othOutIntoWhsSubTab);// 修改国际批号
        }

        message = "成功";

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

        // 修改主表状态
        // System.out.println("修改状态");
        int count = othOutIntoWhsMapper.updateOthOutIntoWhs(othOutIntoWhs);// 修改状态
        // System.out.println("修改国际批号");
        for (OthOutIntoWhsSubTab intoWhsSubTab : othOutIntoWhsSubTab) {
            List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// 其他出入库 子
            oSubTabList.add(intoWhsSubTab);
            // System.out.println(intoWhsSubTab.toString());
            othOutIntoWhsMapper.updateBat(oSubTabList);// 修改国际批号
        }

        if (count >= 1) {

            for (InvtyTab iTab : invtyTab) {

                List<InvtyTab> invtyTabs = new ArrayList<>();
                invtyTabs.add(iTab);
                // 修改库存表入库数量
                // System.out.println("for(InvtyTab iTab:invtyTab) {");
                if (invtyNumMapper.selectInvtyTabByTerm(iTab) != null) {
                    // 修改库存数量
                    // System.out.println("修改库存数量 ");
                    invtyNumMapper.updateInvtyTabJia(invtyTabs);// 修改库存表数量(增加)
                    // 修改库存金额没写 SQL
                } else {
                    // System.out.println("新增库存表  入库");
                    // 新增库存表 入库
                    invtyNumMapper.insertInvtyTabList(invtyTabs);
                }
            }

            for (MovBitTab mTab : movBitTab) {
                // System.out.println("for(MovBitTab mTab : movBitTab) {");
                List<MovBitTab> lmbt = new ArrayList<>();
                // System.out.println("new.size()" + lmbt.size());
                lmbt.add(mTab);
                // System.out.println("add.size()" + lmbt.size());
                // (入库的时候如果存货本身有增在所在货位上增加，如果没有则进行添加)
                if (invtyNumMapper.selectAllMbit(lmbt).size() > 0) {

                    invtyNumMapper.updateMovbitTabJia(lmbt);// 修改移位表(增加)

                } else {
                    List<GdsBit> gList = invtyNumMapper.selectRegn(mTab.getGdsBitEncd());
                    if (gList.size() == 0) {
                        throw new RuntimeException("没有区域");
                    }
                    // System.out.println(gList.get(0).getRegnEncd());
                    mTab.setRegnEncd(gList.get(0).getRegnEncd());// 区域
                    // System.out.println("else.size()" + lmbt.size());
                    lmbt.clear();
                    // System.out.println("clear.size()" + lmbt.size());
                    lmbt.add(mTab);
                    // System.out.println("add.size()" + lmbt.size());
                    invtyNumMapper.insertMovBitTab(lmbt);// 增加移位表
                }

            }

            /**
             *
             * //(入库的时候如果存货本身有增在所在货位上增加，如果没有则进行添加)
             * if(invtyNumMapper.selectAllMbit(movBitTab)!=null) {
             *
             * invtyNumMapper.updateMovbitTabJia(movBitTab);//修改移位表(增加) }else { //新增移位表 寻找区域
             * for(MovBitTab mTab:movBitTab) { List<GdsBit>
             * gList=invtyNumMapper.selectRegn(mTab.getGdsBitEncd()); for(GdsBit
             * gdsBit:gList) { mTab.setRegnEncd(gdsBit.getRegnEncd());//区域
             * movBitTab.add(mTab); } } invtyNumMapper.insertMovBitTab(movBitTab);//增加移位表 }
             *
             */
            isSuccess = true;
            message = "入库成功！";
        } else {
            isSuccess = false;
            message = "入库失败！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uInvMov", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 出库
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
        message = "成功";
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

        // 修改主表状态
        int count = othOutIntoWhsMapper.updateOthOutIntoWhs(othOutIntoWhs);// 修改状态
        if (count >= 1) {

            for (InvtyTab iTab : invtyTab) {

                List<InvtyTab> invtyTabs = new ArrayList<>();
                invtyTabs.add(iTab);
                // 修改库存表入库数量
                // System.out.println("for(InvtyTab iTab:invtyTab) {");
                InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(iTab);
//				if(InvtyTab sdas=invtyNumMapper.selectInvtyTabByTerm(iTab)!=null) {

                if (inTab.getNowStok().compareTo(iTab.getNowStok()) == 1
                        || inTab.getNowStok().compareTo(iTab.getNowStok()) == 0) {

                    // 修改库存数量
                    // System.out.println("修改库存数量  出库 ");
                    message = "出库成功！";
                    isSuccess = true;
                    invtyNumMapper.updateInvtyTab(invtyTabs);// 修改库存表数量
                    // 修改库存金额没写 SQL
                } else {
                    message = "库存中数量不足";
                    isSuccess = false;
                    try {
                        throw new RuntimeException("库存表  出库");
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
                            message = "出库成功！";
                            isSuccess = true;
                            // 修改移位数量(减少)
                            invtyNumMapper.updateMovbitTab(lmbt);// 修改移位表
                        } else {
                            isSuccess = false;
                            message = "出库失败！";
                            try {
                                throw new RuntimeException("库存表  出库");
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
                    message = "出库失败！";
                }

            }
            /**
             * for(OthOutIntoWhsSubTab oSubTab:othOutIntoWhsSubTab) { for(InvtyTab
             * iTab:invtyTab) { InvtyTab inTab=invtyNumMapper.selectInvtyTabByTerm(iTab);
             *
             * if(inTab.getNowStok().compareTo(oSubTab.getQty())==1||inTab.getNowStok().compareTo(oSubTab.getQty())==0)
             * {
             *
             * //修改库存数量 invtyNumMapper.updateInvtyTab(invtyTab);//修改库存表数量
             *
             * //修改移位数量(减少) invtyNumMapper.updateMovbitTab(movBitTab);//修改移位表
             *
             * isSuccess=true; message="出库成功！"; }
             *
             * if(inTab.getNowStok().compareTo(oSubTab.getQty())==-1){ isSuccess=false;
             * message="库存中数量不足"; } } }
             */

            isSuccess = true;
            message = "出库成功！";
        } else {
            isSuccess = false;
            message = "出库失败！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uOutvMov", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /**
     * 货位判断
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
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + othOutInto.getWhsEncd() + ",存货："
                        + tab.getInvtyEncd() + ",批号：" + tab.getBatNum() + "批次无货位数据");
            } else if (bitTab2.getQty().compareTo(tab.getQty()) != 0) {
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + othOutInto.getWhsEncd() + ",存货："
                        + tab.getInvtyEncd() + ",批号：" + tab.getBatNum() + "批次货位数量与单据数量不符！");
            }
        }
    }

    /**
     * 出库货位
     */
    private void outGdsBitLis(OthOutIntoWhs othOutInto, List<OthOutIntoWhsSubTab> oSubTabList, String danHao) {
        // 是否货位管理
        if (!isNtPrgrGdsBitMgmt(othOutInto.getWhsEncd())) {
            return;
        }
        // 出库状态
        List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(danHao);
        if (tabs.size() == 0) {
            throw new RuntimeException("单据：" + danHao + "没有货位信息");
        }

        isGdsBit(othOutInto, oSubTabList, danHao);
        // 货位
        for (MovBitTab tab : tabs) {
            MovBitTab whsTab = invtyNumMapper.selectMbit(tab);
            if (whsTab == null) {
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + othOutInto.getWhsEncd() + ",存货："
                        + tab.getInvtyEncd() + ",批号：" + tab.getBatNum() + "批次货位上不存在");
            } else if (whsTab.getQty().compareTo(tab.getQty()) == 1 || whsTab.getQty().compareTo(tab.getQty()) == 0) {
                tab.setMovBitEncd(whsTab.getMovBitEncd());
                invtyNumMapper.updateJianMbit(tab);// 出库
            } else {
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + othOutInto.getWhsEncd() + ",存货："
                        + tab.getInvtyEncd() + ",批号：" + tab.getBatNum() + "批次货位上数量不足");
            }
        }
//		删除货位单据信息防止弃审
//		bitListMapper.deleteInvtyGdsBitList(Arrays.asList(danHao));

    }

    /**
     * 入库货位
     */
    private void intoGdsBitLis(OthOutIntoWhs othOutInto, List<OthOutIntoWhsSubTab> oSubTabList, String danHao) {
        // 是否货位管理
        if (!isNtPrgrGdsBitMgmt(othOutInto.getWhsEncd())) {
            return;
        }

        // 入库状态
        List<MovBitTab> tabs = bitListMapper.selectInvtyGdsBitListf(danHao);
        if (tabs.size() == 0) {
            throw new RuntimeException("单据：" + danHao + "没有货位信息");
        }

        isGdsBit(othOutInto, oSubTabList, danHao);
        /* 货位信息 */
        for (MovBitTab tab : tabs) {
            MovBitTab whsTab = invtyNumMapper.selectMbit(tab);
            if (whsTab == null) {
                invtyNumMapper.insertMovBitTabJia(tab);// 新增
            } else {
                tab.setMovBitEncd(whsTab.getMovBitEncd());
                invtyNumMapper.updateJiaMbit(tab);// 入库
            }
        }
//		删除货位单据信息防止弃审
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

    // 没有来源单据入
    String laiYuanWuIn(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// 其他出入库 子
        List<InvtyTab> invtyList = new ArrayList<>();// 库存表

//		if (a >= 1) {
        // 查询子表
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("单号：" + danHao + "没有表体信息");

        }
        // 入库状态
        intoGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // 库存表
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// 仓库编码
            // 库存表
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// 存货编码
            invtyTab.setBatNum(tab.getBatNum());// 批号
            invtyTab.setNowStok(tab.getQty());// 现存量
            invtyTab.setAvalQty(tab.getQty());// 可用量
            invtyTab.setPrdcDt(tab.getPrdcDt());// 生产日期
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// 保质期
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// 失效日期
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// 含税单价
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// 未税单价
            invtyTab.setTaxRate(tab.getTaxRate());// 税率
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// 含税金额
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// 未税金额

            invtyList.add(invtyTab);

            InvtyTab inTab1 = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab1 != null) {
                invtyNumMapper.updateInvtyTabAmtJia(invtyList);// 现存量增加 入库 修改库存表的数量 金额等信息 -->
                invtyNumMapper.updateAInvtyTabJia(invtyList);// 可用量增加
                invtyList.clear();
            } else {
                // 新增库存表
                invtyNumMapper.insertInvtyTabList(invtyList);
                invtyList.clear();
            }
        }
        return "单据" + danHao + zhuagntai + "成功！";
//		

    }

    // 没有来源单据出
    String laiYuanWuOut(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// 其他出入库 子
        List<InvtyTab> invtyList = new ArrayList<>();// 库存表
        String message = null;
//		if (a >= 1) {
        // 查询子表
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("单号：" + danHao + "没有表体信息");

        }
        // 货位
        outGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // 库存表
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// 仓库编码
            // 库存表
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// 存货编码
            invtyTab.setBatNum(tab.getBatNum());// 批号
            invtyTab.setNowStok(tab.getQty());// 现存量

            invtyTab.setAvalQty(tab.getQty());// 可用量
            invtyTab.setPrdcDt(tab.getPrdcDt());// 生产日期
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// 保质期
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// 失效日期
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// 含税单价
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// 未税单价
            invtyTab.setTaxRate(tab.getTaxRate());// 税率
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// 含税金额
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// 未税金额

            invtyList.add(invtyTab);

            // 库存数量
            InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab == null) {
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd() + ",存货："
                        + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存不存在," + zhuagntai + "失败！");
            }
            // 出库
            if (((inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok()).compareTo(tab.getQty()) == 1
                    || (inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok())
                    .compareTo(tab.getQty()) == 0)
                    && ((inTab.getAvalQty() == null ? new BigDecimal("0") : inTab.getAvalQty())
                    .compareTo(tab.getQty()) == 1
                    || (inTab.getAvalQty() == null ? new BigDecimal("0") : inTab.getAvalQty())
                    .compareTo(tab.getQty()) == 0)) {
                invtyNumMapper.updateInvtyTabAmt(invtyList);// 现存量减少 出库 修改库存表的数量 金额等信息
                invtyNumMapper.updateAInvtyTab(invtyList);// 可用量减少

                invtyList.clear();
            } else {
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd() + ",存货："
                        + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存数量不足," + zhuagntai + "失败！");

            }

        }
        message = "单据:" + danHao + zhuagntai + "成功！";
        return message;

    }

    String outWhs(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// 其他出入库 子
        List<InvtyTab> invtyList = new ArrayList<>();// 库存表
        String message = null;
//		if (a >= 1) {

        // 查询子表
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("单号：" + danHao + "没有表体信息");

        }

        // 货位
        outGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // 库存表
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// 仓库编码
            // 库存表
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// 存货编码
            invtyTab.setBatNum(tab.getBatNum());// 批号
            invtyTab.setNowStok(tab.getQty());// 现存量
            invtyTab.setAvalQty(tab.getQty());// 可用量
            invtyTab.setPrdcDt(tab.getPrdcDt());// 生产日期
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// 保质期
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// 失效日期
//				BigDecimal cntnTaxUprc=new BigDecimal(tab.get("cntnTaxUprc").asText());
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// 含税单价
//				BigDecimal unTaxUprc=new BigDecimal(tab.get("unTaxUprc").asText());
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// 未税单价
//				BigDecimal taxRate=new BigDecimal(tab.get("taxRate").asText());
            invtyTab.setTaxRate(tab.getTaxRate());// 税率
//				BigDecimal cntnTaxAmt=new BigDecimal(tab.get("cntnTaxAmt").asText());
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// 含税金额
//				BigDecimal unTaxAmt=new BigDecimal(tab.get("unTaxAmt").asText());
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// 未税金额
            invtyList.add(invtyTab);

            // 库存数量
            InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab == null) {
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd() + ",存货："
                        + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存不存在," + zhuagntai + "失败！");
            }
            // 出库

            if ((inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok()).compareTo(tab.getQty()) == 1
                    || (inTab.getNowStok() == null ? new BigDecimal("0") : inTab.getNowStok())
                    .compareTo(tab.getQty()) == 0) {
                message = " 单据" + danHao + zhuagntai + "成功！";
                invtyNumMapper.updateInvtyTabAmt(invtyList);// 现存量减少 出库 修改库存表的数量 金额等信息
                invtyList.clear();
            } else {
//					message = inTab.getInvtyEncd() + "库存中数量不足" + inTab.getBatNum() + "批号" + danHao + " 单据审核失败！";
                throw new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd() + ",存货："
                        + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存数量不足," + zhuagntai + "失败！");

            }

        }

        return message;

    }

    String inWhs(OthOutIntoWhs othOutInto, String zhuagntai, String danHao) {
        List<OthOutIntoWhsSubTab> oSubTabList = new ArrayList<>();// 其他出入库 子
        List<InvtyTab> invtyList = new ArrayList<>();// 库存表
        String message = null;

//		if (a >= 1) {
        // 查询子表
        oSubTabList = othOutIntoWhsMapper.selectOthOutIntoWhsSubTabList(danHao);
        if (oSubTabList.size() == 0) {
            throw new RuntimeException("单号：" + danHao + "没有表体信息");

        }
        // 货位
        intoGdsBitLis(othOutInto, oSubTabList, danHao);

        for (OthOutIntoWhsSubTab tab : oSubTabList) {
            // 库存表
            InvtyTab invtyTab = new InvtyTab();

            invtyTab.setWhsEncd(othOutInto.getWhsEncd());// 仓库编码
            // 库存表
            invtyTab.setInvtyEncd(tab.getInvtyEncd());// 存货编码
            invtyTab.setBatNum(tab.getBatNum());// 批号
            invtyTab.setNowStok(tab.getQty());// 现存量

            invtyTab.setAvalQty(tab.getQty());// 可用量
            invtyTab.setPrdcDt(tab.getPrdcDt());// 生产日期
            invtyTab.setBaoZhiQi(tab.getBaoZhiQi());// 保质期
            invtyTab.setInvldtnDt(tab.getInvldtnDt());// 失效日期
//				BigDecimal cntnTaxUprc=new BigDecimal(tab.get("cntnTaxUprc").asText());
            invtyTab.setCntnTaxUprc(tab.getCntnTaxUprc());// 含税单价
//				BigDecimal unTaxUprc=new BigDecimal(tab.get("unTaxUprc").asText());
            invtyTab.setUnTaxUprc(tab.getUnTaxUprc());// 未税单价
//				BigDecimal taxRate=new BigDecimal(tab.get("taxRate").asText());
            invtyTab.setTaxRate(tab.getTaxRate());// 税率
//				BigDecimal cntnTaxAmt=new BigDecimal(tab.get("cntnTaxAmt").asText());
            invtyTab.setCntnTaxAmt(tab.getCntnTaxAmt());// 含税金额
//				BigDecimal unTaxAmt=new BigDecimal(tab.get("unTaxAmt").asText());
            invtyTab.setUnTaxAmt(tab.getUnTaxAmt());// 未税金额
            invtyList.add(invtyTab);

            InvtyTab inTab1 = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
            if (inTab1 != null) {
                invtyNumMapper.updateInvtyTabAmtJia(invtyList);// 现存量增加 入库 修改库存表的数量 金额等信息 -->
                invtyList.clear();
            } else {
                // 新增库存表
                invtyNumMapper.insertInvtyTabList(invtyList);
                invtyList.clear();
            }
        }

        message = "单据" + danHao + zhuagntai + "成功";

        return message;
    }

    // 审核
    @Override
    public String updateOutInWhsChk(String userName, String jsonBody, String loginTime) {
        String message = "";
        ObjectNode jdRespJson = null;
        String zhuagntai = "审核";
        try {
            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
        } catch (Exception e1) {
            throw new RuntimeException("json解析失败");
        }
        final String danHaos = jdRespJson.get("formNum").asText();

        String chkr = userName;// 审核人
        try {
            OthOutIntoWhs othOutInto = othOutIntoWhsMapper.selectIsChk(danHaos);
            Optional.ofNullable(othOutInto).orElseThrow(() -> new RuntimeException("单据:" + danHaos + "不存在"));
            if (othOutInto.getIsNtChk() == 1) {
                throw new RuntimeException("单号：" + danHaos + "已审核，不需要再次审核");
            }
            othOutInto.setChkr(chkr);
            othOutInto.setChkTm(loginTime);
            int a = othOutIntoWhsMapper.updateOutInWhsChk(Arrays.asList(othOutInto));// 修改审核状态
            if (a == 0) {
                throw new RuntimeException("单号：" + danHaos + "审核失败！");
            }
            if (othOutInto.getOutIntoWhsTypId().equals("2")) {
                // 调拨出库
                message += outWhs(othOutInto, zhuagntai, danHaos);

            } else if (othOutInto.getOutIntoWhsTypId().equals("1")) {
                // 调拨入库 判断出库
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "2");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("调拨单存在异常"));
                if (intoWhs.getIsNtChk() == 0) {
                    throw new RuntimeException("调拨出库单未审核，请先审核调拨出库单:" + intoWhs.getFormNum());
                }
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("7")) {
                // 盘亏出库

                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("8")) {
                // 盘盈入库
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            }
            // 没有来源
            // 出
            else if (othOutInto.getOutIntoWhsTypId().equals("11")) {

                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            }
            // 入
            else if (othOutInto.getOutIntoWhsTypId().equals("12")) {

                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("3")) {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "4");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("组装单存在异常"));
                if (intoWhs.getIsNtChk() == 0) {
                    throw new RuntimeException("组装出库单未审核，请先审核组装出库单:" + intoWhs.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// 修改审核状态
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据审核失败！");
//                }
//                message += outWhs(intoWhs, "审核", intoWhs.getFormNum());
                message += laiYuanWuIn(othOutInto, zhuagntai, othOutInto.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("4")) {
//                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "3");
//                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("组装单存在异常"));
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据审核失败！");
//                }
                message += outWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += inWhs(intoWhs, "审核", intoWhs.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("5")) {// 5拆卸入库
                OthOutIntoWhs intoWhs11 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "6");
                Optional.ofNullable(intoWhs11).orElseThrow(() -> new RuntimeException("拆卸单存在异常"));
                if (intoWhs11.getIsNtChk() == 0) {
                    throw new RuntimeException("拆卸出库单未审核，请先审核拆卸出库单:" + intoWhs11.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs11);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据审核失败！");
//                }
                message += laiYuanWuIn(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += outWhs(intoWhs11, "审核", intoWhs11.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("6")) {
//                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "5");
//                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("拆卸单存在异常"));
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据审核失败！");
//                }
//                message += inWhs(intoWhs, "审核", intoWhs.getFormNum());
                message += outWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
            } else {
                throw new RuntimeException("单据中没有设置出入库类型:" + othOutInto.getOutIntoWhsTypId());
            }
            // 3 组装入库
            // 4 组装出库
            // 5 拆卸入库
            // 6 拆卸出库

            return message;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    // 弃审
    @Override
    public String updateOutInWhsNoChk(String userId, String jsonBody) {
        String message = "";
        ObjectNode jdRespJson = null;
        String zhuagntai = "弃审";
        try {
            jdRespJson = JacksonUtil.getObjectNode(jsonBody);
        } catch (Exception e1) {

            e1.getMessage();
            throw new RuntimeException("json解析失败");
        }
        String danHaos = jdRespJson.get("formNum").asText();

//		String chkr = (String)jdRespJson.get("chkr");// 审核人

        OthOutIntoWhs othOutInto = othOutIntoWhsMapper.selectIsChk(danHaos);
        Optional.ofNullable(othOutInto).orElseThrow(() -> new RuntimeException("单据:" + danHaos + "不存在"));
        if (othOutInto.getIsNtChk() == 0) {
            message = "单据:" + danHaos + "已弃审,不需要重复弃审 ";
            return message;
        }
        if (othOutInto.getIsNtChk() == 1 && othOutInto.getIsNtBookEntry() == 1) {
            message = "单据:" + danHaos + " 已记账 ,不能弃审";
            return message;
        }
        List<OthOutIntoWhs> oList = new ArrayList<>();// 其他出入库主
        othOutInto.setChkr(null);
        oList.add(othOutInto);
        try {

            int a = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// 修改弃审状态
            if (a == 0) {
                throw new RuntimeException("单据：" + danHaos + " 单据弃审失败！");
            }
            if (othOutInto.getOutIntoWhsTypId().equals("2")) {
                // 调拨出库
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "1");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("调拨单存在异常"));
                if (intoWhs.getIsNtChk() == 1) {
                    throw new RuntimeException("请先弃审调拨生成的其他入库单:" + intoWhs.getFormNum());
                }
                message += inWhs(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("1")) {
                // 调拨入库 判断出库
                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("7")) {
                // 盘亏出库
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("8")) {
                // 盘盈入库
                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            }
            // 没有来源
            // 出
            else if (othOutInto.getOutIntoWhsTypId().equals("11")) {
                message += laiYuanWuIn(othOutInto, zhuagntai, danHaos);
            }
            // 入
            else if (othOutInto.getOutIntoWhsTypId().equals("12")) {

                message += laiYuanWuOut(othOutInto, zhuagntai, danHaos);
            } else if (othOutInto.getOutIntoWhsTypId().equals("3")) {
//                // 3 组装入库
//                // 4 组装出库
//                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "4");
//                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("组装单存在异常"));
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据弃审失败！");
//                }
//                message += inWhs(intoWhs, "弃审", intoWhs.getFormNum());
                message += laiYuanWuOut(othOutInto, zhuagntai, othOutInto.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("4")) {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "3");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("组装单存在异常"));
                if (intoWhs.getIsNtChk() == 1) {
                    throw new RuntimeException("请先弃审组装生成的其他入库单:" + intoWhs.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据弃审失败！");
//                }
                message += inWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += outWhs(intoWhs, "弃审", intoWhs.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("5")) {
                // 5 拆卸入库
                // 6 拆卸出库
//                OthOutIntoWhs intoWhs11 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "6");
//                Optional.ofNullable(intoWhs11).orElseThrow(() -> new RuntimeException("拆卸单存在异常"));
//                oList.clear();
//                oList.add(intoWhs11);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据弃审失败！");
//                }
                message += laiYuanWuOut(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += inWhs(intoWhs11, "弃审", intoWhs11.getFormNum());
            } else if (othOutInto.getOutIntoWhsTypId().equals("6")) {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(othOutInto.getSrcFormNum(), "5");
                Optional.ofNullable(intoWhs).orElseThrow(() -> new RuntimeException("拆卸单存在异常"));
                if (intoWhs.getIsNtChk() == 1) {
                    throw new RuntimeException("请先弃审拆卸生成的其他入库单:" + intoWhs.getFormNum());
                }
//                oList.clear();
//                oList.add(intoWhs);
//                int b = othOutIntoWhsMapper.updateOutInWhsNoChk(oList);// 修改审核状态
//                oList.clear();
//                if (b == 0) {
//                    throw new RuntimeException(danHaos + " 单据弃审失败！");
//                }
                message += inWhs(othOutInto, zhuagntai, othOutInto.getFormNum());
//                message += outWhs(intoWhs, "弃审", intoWhs.getFormNum());

            } else {
                throw new RuntimeException("单据中没有设置对应的出入库类型:" + othOutInto.getOutIntoWhsTypId());
            }

            return message;
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }
    }

    /**
     * //审核
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
     * List<OthOutIntoWhs> oList=new ArrayList<>();//其他出入库主
     * List<OthOutIntoWhsSubTab> oSubTabList=new ArrayList<>();//其他出入库 子
     * List<InvtyTab> invtyList=new ArrayList<>();//库存表
     * <p>
     * for(Iterator<JsonNode> dbzhuIterator = listJson.iterator();
     * dbzhuIterator.hasNext();) { JsonNode dbzhu = dbzhuIterator.next();
     * <p>
     * oList=new ArrayList<>();//其他出入库主 oSubTabList=new
     * ArrayList<>();//其他出入库 子 invtyList=new ArrayList<>();//库存表
     * <p>
     * //其他出入库主 OthOutIntoWhs othOutIntoWhs=new OthOutIntoWhs();
     * othOutIntoWhs.setFormNum(dbzhu.get("formNum").asText());
     * othOutIntoWhs.setOutStatus(dbzhu.get("outStatus").asText());
     * othOutIntoWhs.setInStatus(dbzhu.get("inStatus").asText());
     * othOutIntoWhs.setChkr(dbzhu.get("chkr").asText());
     * oList.add(othOutIntoWhs); //库存表 InvtyTab invtyTab=new InvtyTab();
     * invtyTab.setWhsEncd(dbzhu.get("whsEncd").asText());
     * <p>
     * OthOutIntoWhs
     * oIntoWhs=othOutIntoWhsMapper.selectIsChk(othOutIntoWhs.getFormNum());
     * if(oIntoWhs.getIsNtChk()==1) {
     * message+=othOutIntoWhs.getFormNum()+" 已审核状态不能再次审核 "; }else { int
     * a=othOutIntoWhsMapper.updateOutInWhsChk(oList);//修改审核状态 if(a>=1) {
     * ArrayNode canListJson=(ArrayNode) dbzhu.get("canList");
     * for(Iterator<JsonNode> dbziIterator = canListJson.iterator();
     * dbziIterator.hasNext();) { JsonNode dbzi = dbziIterator.next();
     * <p>
     * BigDecimal qty=new BigDecimal(dbzi.get("qty").asText());
     * OthOutIntoWhsSubTab oIntoWhsSubTab=new OthOutIntoWhsSubTab();
     * oIntoWhsSubTab.setQty(qty); oSubTabList.add(oIntoWhsSubTab);
     * <p>
     * //库存表 invtyTab.setInvtyEncd(dbzi.get("invtyEncd").asText());//存货编码
     * invtyTab.setBatNum(dbzi.get("batNum").asText());//批号
     * invtyTab.setNowStok(qty);//现存量
     * <p>
     * invtyTab.setAvalQty(qty);//可用量
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
     * invtyTab.setUnTaxAmt(unTaxAmt); //区域编码 货位编码
     * (通过仓库编码、存货编码、批号查询查询出货位编码 通过货位编码查询仓库编码) } invtyList.add(invtyTab);
     * <p>
     * //修改库存表出库数量 for(InvtyTab iTab:invtyList) { InvtyTab
     * inTab=invtyNumMapper.selectInvtyTabByTerm(iTab);
     * for(OthOutIntoWhsSubTab oSubTab:oSubTabList) {
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==1||inTab.getNowStok().compareTo(oSubTab.getQty())==0)
     * {
     * <p>
     * //其他出入库类型 11:其它出库 12:其他入库
     * if(othOutIntoWhs.getOutStatus()!=null||othOutIntoWhs.getOutStatus()!="")
     * { invtyNumMapper.updateInvtyTabAmt(invtyList);//现存量减少 出库
     * <p>
     * }else
     * if(othOutIntoWhs.getInStatus()!=null||othOutIntoWhs.getInStatus()!="")
     * { //判断库存表中是否存在 for(InvtyTab iTab1:invtyList) { InvtyTab
     * inTab1=invtyNumMapper.selectInvtyTabByTerm(iTab1); if(inTab1!=null)
     * { invtyNumMapper.updateInvtyTabAmtJia(invtyList);//现存量增加 入库 }else {
     * //新增库存表 invtyNumMapper.insertInvtyTabList(invtyList); }
     * <p>
     * } } isSuccess=true; message+=othOutIntoWhs.getFormNum()+" 单据审核成功！";
     * }
     * <p>
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==-1){
     * isSuccess=false; message="库存中数量不足"; } } }
     * <p>
     * }else { isSuccess=false; message+=othOutIntoWhs.getFormNum()+"
     * 单据审核失败！"; }
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
     * //弃审
     * @Override public String updateOutInWhsNoChk(String userId,String jsonBody) {
     * String message=""; Boolean isSuccess=true; String resp="";
     * <p>
     * ObjectNode jdRespJson; JsonNode resultJson; ArrayNode listJson =
     * null; try { jdRespJson = JacksonUtil.getObjectNode(jsonBody);
     * resultJson = jdRespJson.get("reqBody"); listJson = (ArrayNode)
     * resultJson.get("list"); } catch (IOException e1) {
     * }
     * <p>
     * List<OthOutIntoWhs> oList=new ArrayList<>();//其他出入库主
     * List<OthOutIntoWhsSubTab> oSubTabList=new ArrayList<>();//其他出入库 子
     * List<InvtyTab> invtyList=new ArrayList<>();//库存表
     * <p>
     * for(Iterator<JsonNode> dbzhuIterator = listJson.iterator();
     * dbzhuIterator.hasNext();) { JsonNode dbzhu = dbzhuIterator.next();
     * <p>
     * oList=new ArrayList<>();//其他出入库主 oSubTabList=new
     * ArrayList<>();//其他出入库 子 invtyList=new ArrayList<>();//库存表
     * <p>
     * //其他出入库主 OthOutIntoWhs othOutIntoWhs=new OthOutIntoWhs();
     * othOutIntoWhs.setFormNum(dbzhu.get("formNum").asText());
     * othOutIntoWhs.setOutStatus(dbzhu.get("outStatus").asText());
     * othOutIntoWhs.setInStatus(dbzhu.get("inStatus").asText());
     * othOutIntoWhs.setChkr(dbzhu.get("chkr").asText());
     * oList.add(othOutIntoWhs); //库存表 InvtyTab invtyTab=new InvtyTab();
     * invtyTab.setWhsEncd(dbzhu.get("whsEncd").asText());
     * <p>
     * OthOutIntoWhs
     * oIntoWhs=othOutIntoWhsMapper.selectIsChk(othOutIntoWhs.getFormNum());
     * if(oIntoWhs.getIsNtChk()==0) {
     * message+=othOutIntoWhs.getFormNum()+" 已弃审状态不能再次弃审 "; }else { int
     * a=othOutIntoWhsMapper.updateOutInWhsNoChk(oList);//修改审核状态 if(a>=1)
     * { ArrayNode canListJson=(ArrayNode) dbzhu.get("canList");
     * for(Iterator<JsonNode> dbziIterator = canListJson.iterator();
     * dbziIterator.hasNext();) { JsonNode dbzi = dbziIterator.next();
     * <p>
     * BigDecimal qty=new BigDecimal(dbzi.get("qty").asText());
     * OthOutIntoWhsSubTab oIntoWhsSubTab=new OthOutIntoWhsSubTab();
     * oIntoWhsSubTab.setQty(qty); oSubTabList.add(oIntoWhsSubTab);
     * <p>
     * //库存表 invtyTab.setInvtyEncd(dbzi.get("invtyEncd").asText());//存货编码
     * invtyTab.setBatNum(dbzi.get("batNum").asText());//批号
     * invtyTab.setNowStok(qty);//现存量
     * <p>
     * BigDecimal taxRate=new BigDecimal(dbzi.get("taxRate").asText());
     * invtyTab.setTaxRate(taxRate); BigDecimal unTaxAmt=new
     * BigDecimal(dbzi.get("unTaxAmt").asText());
     * invtyTab.setUnTaxAmt(unTaxAmt); } invtyList.add(invtyTab);
     * <p>
     * //修改库存表出库数量 for(InvtyTab iTab:invtyList) { InvtyTab
     * inTab=invtyNumMapper.selectInvtyTabByTerm(iTab);
     * for(OthOutIntoWhsSubTab oSubTab:oSubTabList) {
     * <p>
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==1||inTab.getNowStok().compareTo(oSubTab.getQty())==0)
     * {
     * <p>
     * if(othOutIntoWhs.getOutStatus()!=null||othOutIntoWhs.getOutStatus()!="")
     * { invtyNumMapper.updateInvtyTabAmtJia(invtyList);//现存量增加 出库 }else
     * if(othOutIntoWhs.getInStatus()!=null||othOutIntoWhs.getInStatus()!="")
     * { invtyNumMapper.updateInvtyTabAmt(invtyList);//现存量减少 入库 }
     * <p>
     * isSuccess=true; message+=othOutIntoWhs.getFormNum()+" 单据弃审成功！"; }
     * <p>
     * if(inTab.getNowStok().compareTo(oSubTab.getQty())==-1){
     * isSuccess=false; message="库存中数量不足"; } } }
     * <p>
     * }else { isSuccess=false; message+=othOutIntoWhs.getFormNum()+"
     * 单据弃审失败！"; }
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
            message = "移位修改成功！";
            isSuccess = true;
        } else {
            message = "移位修改失败！";
            isSuccess = false;
        }

        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/updateMovbitTab", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 打印
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
            resp = BaseJson.returnRespObjListAnno("whs/out_into_whs/queryListDaYin", true, "查询成功！", null, aList);
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");

            }
            try {
                othOutIntoWhsMapper.exInsertOthOutIntoWhs(entry.getValue());
                othOutIntoWhsMapper.exInsertOthOutIntoWhsSubTab(entry.getValue().getOutIntoSubList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "其他出入库单新增成功！";
        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // 导入excle
    private Map<String, OthOutIntoWhs> uploadScoreInfo(MultipartFile file) {
        Map<String, OthOutIntoWhs> temp = new HashMap<>();
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
            // System.out.println(lastRowNum);
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

//				String outInto=r.getCell(1).getStringCellValue();
//				 String tern = ".*入库.*";
//				 String pat = ".*出库.*";
                String orderNo = GetCellData(r, "单据号");
//			      boolean into = Pattern.matches(tern, outInto);
//			      boolean out = Pattern.matches(pat, outInto);
//			      if(into) {
//				 orderNo = r.getCell(6).getStringCellValue()+"into";
//
//			      }else if(out) {
//				 orderNo = r.getCell(6).getStringCellValue()+"out";
//
//			      }

                // 创建实体类
                // System.out.println(orderNo);
                OthOutIntoWhs othOutIntoWhs = new OthOutIntoWhs();
                if (temp.containsKey(orderNo)) {
                    othOutIntoWhs = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                // r.getCell(6).getStringCellValue()
                othOutIntoWhs.setFormNum(orderNo); // 单号 q varchar(200
                othOutIntoWhs.setFormDt(
                        GetCellData(r, "单据日期") == null ? "" : GetCellData(r, "单据日期")); // 单据日期
                othOutIntoWhs.setWhsEncd(GetCellData(r, "仓库编码")); // 仓库编码 q varchar(200
                othOutIntoWhs.setSrcFormNum(GetCellData(r, "来源单据号")); // 来源单据号 q varchar(200

//				String outIntoWhsTypId;
//				String outStatus = null;
//				String inStatus=null;
//				switch (outInto) {
//				case "调拨入库":outIntoWhsTypId="1";outStatus=null;inStatus="处理中";
//					break;
//				case "其他出库":outIntoWhsTypId="11";outStatus="处理中";inStatus=null;
//					break;
//				case "其他入库":outIntoWhsTypId="12";outStatus=null;inStatus="处理中";
//					break;
//				case "调拨出库":outIntoWhsTypId="2";outStatus="处理中";inStatus=null;
//					break;
//				case "组装入库":outIntoWhsTypId="3";outStatus=null;inStatus="处理中";
//					break;
//				case "组装出库":outIntoWhsTypId="4";outStatus="处理中";inStatus=null;
//					break;
//				case "拆卸入库":outIntoWhsTypId="5";outStatus=null;inStatus="处理中";
//					break;
//				case "拆卸出库":outIntoWhsTypId="6";outStatus="处理中";inStatus=null;
//					break;
//				case "盘亏出库":outIntoWhsTypId="7";outStatus="处理中";inStatus=null;
//					break;
//				case "盘盈入库":outIntoWhsTypId="8";outStatus=null;inStatus="处理中";
//					break;
//				default:outIntoWhsTypId="0";
//				 String tern = ".*入库.*";
//				 String pat = ".*出库.*";
//
//			      boolean into = Pattern.matches(tern, outInto);
//			      boolean out = Pattern.matches(pat, outInto);
//			      if(into) {
//			    	  inStatus="处理中";
//			      }
//			      if(out) {
//			    	  outStatus="处理中";
//			      }
//					break;
//				}
//				

                othOutIntoWhs.setOutIntoWhsTypId(
                        (new Double(GetCellData(r, "出入库类型编码") == null || GetCellData(r, "出入库类型编码").equals("") ? null
                                : GetCellData(r, "出入库类型编码"))).intValue() + ""); // 出入库类别编码 q 根据业务类型判断 varchar(200
                othOutIntoWhs.setFormTypEncd(othOutIntoWhs.getOutIntoWhsTypId().equals("11") ? "015" : "014");// 014
                // 其他入库单
                // 015
                // 其他出库单

//                othOutIntoWhs.setOutStatus(GetCellData(r, "出库状态")); // 出库状态 单据类型 varchar(200
//                othOutIntoWhs.setInStatus(GetCellData(r, "入库状态")); // 入库状态 单据类型 判端 varchar(200
                othOutIntoWhs.setMemo(GetCellData(r, "备注")); // 备注q varchar(2000
//				othOutIntoWhs.setIsNtWms((new Double(GetCellData(r, "是否向WMS上传"))).intValue()); // 是否向WMS上传 没 int(11
                othOutIntoWhs.setIsNtChk(
                        (new Double(GetCellData(r, "是否审核") == null || GetCellData(r, "是否审核").equals("") ? "0"
                                : GetCellData(r, "是否审核"))).intValue()); // 是否审核 int(11
                othOutIntoWhs.setIsNtBookEntry(
                        (new Double(GetCellData(r, "是否记账") == null || GetCellData(r, "是否记账").equals("") ? "0"
                                : GetCellData(r, "是否记账"))).intValue()); // 是否记账 int(11
//                othOutIntoWhs.setIsNtCmplt(
//                        (new Double(GetCellData(r, "是否完成") == null || GetCellData(r, "是否完成").equals("") ? "0"
//                                : GetCellData(r, "是否完成"))).intValue()); // 是否完成 int(11
//                othOutIntoWhs.setIsNtClos(
//                        (new Double(GetCellData(r, "是否关闭") == null || GetCellData(r, "是否关闭").equals("") ? "0"
//                                : GetCellData(r, "是否关闭"))).intValue()); // 是否关闭 int(11
//                othOutIntoWhs.setPrintCnt(
//                        (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
//                                : GetCellData(r, "打印次数"))).intValue()); // 打印次数 q int(11
                othOutIntoWhs.setSetupPers(GetCellData(r, "创建人")); // 创建人 q varchar(200
                othOutIntoWhs.setSetupTm(
                        GetCellData(r, "创建时间") == null ? "" : GetCellData(r, "创建时间")); // 创建时间
                // q
                // datetime
                othOutIntoWhs.setMdfr(GetCellData(r, "修改人")); // 修改人 q varchar(200
                othOutIntoWhs.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间")); // 修改时间
                // q
                // datetime
                othOutIntoWhs.setChkr(GetCellData(r, "审核人")); // 审核人 q varchar(200
                othOutIntoWhs.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间")); // 审核时间
                // q
                // datetime
                othOutIntoWhs.setBookEntryPers(GetCellData(r, "记账人")); // 记账人 q varchar(200
                othOutIntoWhs.setBookEntryTm(
                        GetCellData(r, "记账时间") == null ? "" : GetCellData(r, "记账时间")); // 记账时间
                // datetime
//                othOutIntoWhs.setOperator(GetCellData(r, "操作人")); // 操作人 q varchar(200
//                othOutIntoWhs.setOperatorId(GetCellData(r, "操作人编码")); // 操作人编码 q varchar(200

                othOutIntoWhs.setIsNtMakeVouch(
                        (new Double(GetCellData(r, "是否生成凭证") == null || GetCellData(r, "是否生成凭证").equals("") ? "0"
                                : GetCellData(r, "是否生成凭证"))).intValue()); // 是否生成凭证 int(11
                othOutIntoWhs.setMakVouchPers(GetCellData(r, "制凭证人")); // 制凭证人
                othOutIntoWhs.setMakVouchTm(
                        GetCellData(r, "制凭证时间") == null ? "" : GetCellData(r, "制凭证时间")); // 制凭证时间

                othOutIntoWhs.setRecvSendCateId(GetCellData(r, "收发类别编码"));//收发类别id

                List<OthOutIntoWhsSubTab> OthOutIntoWhsSub = othOutIntoWhs.getOutIntoSubList();// 订单子表
                if (OthOutIntoWhsSub == null) {
                    OthOutIntoWhsSub = new ArrayList<>();
                }

                OthOutIntoWhsSubTab othOutIntoWhsSubList = new OthOutIntoWhsSubTab();

//				othOutIntoWhsSubList.setOrdrNum(ordrNum); // 序号 bigint(20
                othOutIntoWhsSubList.setFormNum(orderNo); // 单号 varchar(200
                othOutIntoWhsSubList.setInvtyEncd(GetCellData(r, "存货编码")); // 存货编码 1 varchar(200
                othOutIntoWhsSubList.setQty(new BigDecimal(GetCellData(r, "数量") == null ? "0" : GetCellData(r, "数量"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 数量 1 decimal(20,8
                othOutIntoWhsSubList.setTaxRate(new BigDecimal(
                        GetCellData(r, "税率") == null || GetCellData(r, "税率").equals("") ? "0" : GetCellData(r, "税率"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 税率 decimal(20,8
                othOutIntoWhsSubList.setCntnTaxUprc(
                        new BigDecimal(GetCellData(r, "含税单价") == null || GetCellData(r, "含税单价").equals("") ? "0"
                                : GetCellData(r, "含税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 含税单价 decimal(20,8
                othOutIntoWhsSubList.setUnTaxUprc(
                        new BigDecimal(GetCellData(r, "未税单价") == null || GetCellData(r, "未税单价").equals("") ? "0"
                                : GetCellData(r, "未税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税单价[单价(根据批次带入单价]
                // decimal(20,8
                othOutIntoWhsSubList.setBookEntryUprc(
                        new BigDecimal(GetCellData(r, "记账单价") == null || GetCellData(r, "记账单价").equals("") ? "0"
                                : GetCellData(r, "记账单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 记账单价 decimal(20,8
                othOutIntoWhsSubList.setCntnTaxAmt(
                        new BigDecimal(GetCellData(r, "含税金额") == null || GetCellData(r, "含税金额").equals("") ? "0"
                                : GetCellData(r, "含税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 含税金额 decimal(20,8
                othOutIntoWhsSubList.setUnTaxAmt(
                        new BigDecimal(GetCellData(r, "未税金额") == null || GetCellData(r, "未税金额").equals("") ? "0"
                                : GetCellData(r, "未税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税金额 decimal(20,8
                othOutIntoWhsSubList.setIntlBat(GetCellData(r, "国际批号")); // 国际批号 1 varchar(200
                othOutIntoWhsSubList.setBatNum(GetCellData(r, "批号")); // 批号 1 varchar(200
                othOutIntoWhsSubList.setPrdcDt(
                        GetCellData(r, "生产日期") == null ? "" : GetCellData(r, "生产日期")); // 生产日期
                // 1
                othOutIntoWhsSubList.setBxQty(new BigDecimal(
                        GetCellData(r, "箱数") == null || GetCellData(r, "箱数").equals("") ? "0" : GetCellData(r, "箱数"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 箱数 a decimal(208
                // datetime
                othOutIntoWhsSubList.setBaoZhiQi(GetCellData(r, "保质期")); // 保质期 1 varchar(200
                othOutIntoWhsSubList.setInvldtnDt(
                        GetCellData(r, "失效日期") == null ? "" : GetCellData(r, "失效日期")); // 失效日期
                // 1
                // datetime
                othOutIntoWhsSubList.setProjEncd(GetCellData(r, "项目编码")); //项目编码
                othOutIntoWhsSubList.setMemos(GetCellData(r, "子表备注")); //子备注

                OthOutIntoWhsSub.add(othOutIntoWhsSubList);

                othOutIntoWhs.setOutIntoSubList(OthOutIntoWhsSub);
                temp.put(orderNo, othOutIntoWhs);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());

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
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + formNum + "不存在！";
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
        // TODO 自动生成的方法存根
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            List<MovBitTab> movBitTabList = othOutIntoWhsMapper.selectallInvtyGdsBitListInto(orderNum);
            if (movBitTabList.size() == 0) {
                throw new RuntimeException("单据" + orderNum + "不存在或已审核不需要分配货位");
            }
            movBitTabList.stream().filter(mov -> serialNum.equals(mov.getSerialNum())).findFirst()
                    .orElseThrow(() -> new RuntimeException("单据" + orderNum + "的" + serialNum + "序号错误请刷新页面"));
            bitListMapper.insertInvtyGdsBitLists(oList);
            message = "成功";
            resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, message, null);
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, e.getMessage(), null);
            } catch (IOException e1) {
                // TODO 自动生成的 catch 块

            }

        }
        return resp;
    }

    @Override
    public String deleteInvtyGdsBitList(String num, String dengji, String list) {
        // TODO 自动生成的方法存根
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> lists = getList(list);
        // System.out.println(lists.size());
        if (lists == null || lists.size() == 0) {
            try {
                isSuccess = false;
                return BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, "参数有误,值为空", null);
            } catch (IOException e) {
            }
        }
        try {
//			list  $() getList

//            OthOutIntoWhs othOutIntoWhs = othOutIntoWhsMapper.selectIsChk(num);
//            if (othOutIntoWhs == null) {
//                throw new RuntimeException(num + " 不存在");
//            }
//            if (othOutIntoWhs.getIsNtChk() == 1) {
//                throw new RuntimeException(num + "已审核不许删除");
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
                    return BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", isSuccess, "参数有误", null);

            }
            message = "删除成功";
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

    // 查询
    @Override
    public String queryInvtyGdsBitList(String json) {
        // TODO 自动生成的方法存根
        String resp = "";
        try {
            MovBitTab bitTab = JacksonUtil.getPOJO(JacksonUtil.getObjectNode(json), MovBitTab.class);
//			MovBitTab bitTab = new MovBitTab();
//			bitTab.setOrderNum(node.has("orderNum") ? node.get("orderNum").asText() : null);
//			bitTab.setSerialNum(node.has("serialNum") ? node.get("serialNum").asText() : null);
            List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitList(bitTab);
            if (bitTabs.size() > 0) {
                resp = BaseJson.returnRespList("whs/out_into_whs/queryInvtyGdsBitList", true, "查询成功", bitTabs);
            } else {
                resp = BaseJson.returnRespList("whs/out_into_whs/queryInvtyGdsBitList", false, "没有货位信息", bitTabs);
            }
        } catch (Exception e) {

            try {
                resp = BaseJson.returnRespObj("whs/out_into_whs/queryMovBitTab", false, e.getMessage(), null);
            } catch (IOException e1) {
                // TODO 自动生成的 catch 块

            }

        }

        return resp;
    }

    @Override
    public String uploadInvtyGdsBitList(List<MovBitTab> xinlist, List<MovBitTab> gailist, String orderNum,
                                        String serialNum) {
        // TODO 自动生成的方法存根
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
//		OthOutIntoWhs othOutIntoWhs = othOutIntoWhsMapper.selectIsChk(orderNum);
//		if (othOutIntoWhs == null) {
//			throw new RuntimeException(orderNum + " 不存在");
//		}
//		if (othOutIntoWhs.getIsNtChk() == 1) {
//			throw new RuntimeException(orderNum + "已审核不许修改");
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
            throw new RuntimeException("单据" + orderNum + "不存在或已审核不需要分配货位");
        }
        movBitTabList.stream().filter(mov -> serialNum.equals(mov.getSerialNum())).findFirst()
                .orElseThrow(() -> new RuntimeException("单据" + orderNum + "的" + serialNum + "序号错误请刷新页面"));
        try {

            if (gailist.size() > 0) {
                bitListMapper.updateInvtyGdsBitLists(gailist);
            }
            if (xinlist.size() > 0) {
                bitListMapper.insertInvtyGdsBitLists(xinlist);
            }

            message = "修改成功";
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

    // 整单联查
    @Override
    public String wholeSingleLianZha(String formNum, String formTypEncd) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

//		11		其他出库
//		12		其他入库
        if (formTypEncd.equals("011")) {
//        011	调拨单
//			1		调拨入库
//			2		调拨出库
            CannibSngl cannibSngls = cannibSnglMapper.selectCSngl(formNum);
            if (cannibSngls == null) {
                isSuccess = true;
                message = message + formNum + "不属于调拨单或单据被删除\n";
            } else if (cannibSngls.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "调拨单未审核\n";
            } else {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "1");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "2");
                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "其他入库单已删除\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// 单据类型编码
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// 单据类型名称
                    intohashMap.put("FormNum", intoWhs.getFormNum());// 单据号
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// 审核状态
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// 创建人
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// 创建时间
                    list.add(intohashMap);
                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "其他出库单已删除\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// 单据类型编码
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// 单据类型名称
                    outhashMap.put("FormNum", outWhs.getFormNum());// 单据号
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// 审核状态
                    outhashMap.put("setupPers", outWhs.getSetupPers());// 创建人
                    outhashMap.put("setupTm", outWhs.getSetupTm());// 创建时间
                    list.add(outhashMap);

                }
            }

        } else if (formTypEncd.equals("012")) {
//          012	组装单	 
//			3		组装入库
//			4		组装出库
            AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(formNum);
            if (aDisambSngl == null) {
                isSuccess = true;
                message = message + formNum + "不属于组装单或单据被删除\n";
            } else if (aDisambSngl.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "组装单未审核\n";
            } else {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "3");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "4");
                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "其他入库单已删除\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// 单据类型编码
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// 单据类型名称
                    intohashMap.put("FormNum", intoWhs.getFormNum());// 单据号
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// 审核状态
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// 创建人
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// 创建时间
                    list.add(intohashMap);

                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "其他出库单已删除\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// 单据类型编码
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// 单据类型名称
                    outhashMap.put("FormNum", outWhs.getFormNum());// 单据号
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// 审核状态
                    outhashMap.put("setupPers", outWhs.getSetupPers());// 创建人
                    outhashMap.put("setupTm", outWhs.getSetupTm());// 创建时间
                    list.add(outhashMap);

                }
            }
        } else if (formTypEncd.equals("013")) {
//          013	拆卸单
//			5		拆卸入库
//			6		拆卸出库
            AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(formNum);
            if (aDisambSngl == null) {
                isSuccess = true;
                message = message + formNum + "不属于拆卸单或单据被删除\n";
            } else if (aDisambSngl.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "拆卸单未审核\n";
            } else {
                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "5");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "6");
                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "其他入库单已删除\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// 单据类型编码
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// 单据类型名称
                    intohashMap.put("FormNum", intoWhs.getFormNum());// 单据号
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// 审核状态
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// 创建人
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// 创建时间
                    list.add(intohashMap);

                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "其他出库单已删除\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// 单据类型编码
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// 单据类型名称
                    outhashMap.put("FormNum", outWhs.getFormNum());// 单据号
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// 审核状态
                    outhashMap.put("setupPers", outWhs.getSetupPers());// 创建人
                    outhashMap.put("setupTm", outWhs.getSetupTm());// 创建时间
                    list.add(outhashMap);
                }
            }

        } else if (formTypEncd.equals("015") || formTypEncd.equals("014")) {
//			 015 其他出库单	 014 其他入库单
            OthOutIntoWhs outIntoWhs = othOutIntoWhsMapper.selectOthOutIntoWhs(formNum);
            Optional.ofNullable(outIntoWhs).orElseThrow(() -> new RuntimeException(formNum + "其他出入单不存在"));
            switch (outIntoWhs.getOutIntoWhsTypId()) {
                // 其他
                case "11":
                case "12":
                    isSuccess = true;
                    message = "其他出入库单没有来源单据";
                    list = null;
                    break;
                // 调拨
                case "1"://
                case "2":
                    CannibSngl cannibSngls = cannibSnglMapper.selectCSngl(outIntoWhs.getSrcFormNum());
                    Optional.ofNullable(cannibSngls).orElseThrow(() -> new RuntimeException("来源单据已被删除"));
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("FormTypName", cannibSngls.getFormTypName());// 单据类型编码
                    hashMap.put("FormTypEncd", cannibSngls.getFormTypEncd());// 单据类型名称
                    hashMap.put("FormNum", cannibSngls.getFormNum());// 单据号
                    hashMap.put("isNtChk", cannibSngls.getIsNtChk());// 审核状态
                    hashMap.put("setupPers", cannibSngls.getSetupPers());// 创建人
                    hashMap.put("setupTm", cannibSngls.getSetupTm());// 创建时间

                    list.add(hashMap);
                    break;
                // 组装 拆卸
                case "3":
                case "4":
                case "5":
                case "6":
                    AmbDisambSngl aDisambSngl = ambDisambSnglMapper.selectAmbDisambSngl(outIntoWhs.getSrcFormNum());
                    Optional.ofNullable(aDisambSngl).orElseThrow(() -> new RuntimeException("来源单据已被删除"));
                    HashMap<String, Object> AhashMap = new HashMap<String, Object>();
                    AhashMap.put("FormTypName", aDisambSngl.getFormTypName());// 单据类型编码
                    AhashMap.put("FormTypEncd", aDisambSngl.getFormTypEncd());// 单据类型名称
                    AhashMap.put("FormNum", aDisambSngl.getFormNum());// 单据号
                    AhashMap.put("isNtChk", aDisambSngl.getIsNtChk());// 审核状态
                    AhashMap.put("setupPers", aDisambSngl.getSetupPers());// 创建人
                    AhashMap.put("setupTm", aDisambSngl.getSetupTm());// 创建时间

                    list.add(AhashMap);
                    break;
                // 盘点
                case "7":
                case "8":
                    CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(outIntoWhs.getSrcFormNum());
                    Optional.ofNullable(checkPrftLoss).orElseThrow(() -> new RuntimeException("来源单据已被删除"));
                    HashMap<String, Object> BhashMap = new HashMap<String, Object>();
                    BhashMap.put("FormTypName", checkPrftLoss.getFormTypName());// 单据类型编码
                    BhashMap.put("FormTypEncd", checkPrftLoss.getFormTypEncd());// 单据类型名称
                    BhashMap.put("FormNum", checkPrftLoss.getCheckFormNum());// 单据号
                    BhashMap.put("isNtChk", checkPrftLoss.getIsNtChk());// 审核状态
                    BhashMap.put("setupPers", checkPrftLoss.getSetupPers());// 创建人
                    BhashMap.put("setupTm", checkPrftLoss.getSetupTm());// 创建时间

                    list.add(BhashMap);
                    CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(checkPrftLoss.getSrcFormNum());

                    Optional.ofNullable(checkSngl).orElseThrow(() -> new RuntimeException("来源单据已被删除"));
                    HashMap<String, Object> ChashMap = new HashMap<String, Object>();
                    ChashMap.put("FormTypName", checkSngl.getFormTypName());// 单据类型编码
                    ChashMap.put("FormTypEncd", checkSngl.getFormTypEncd());// 单据类型名称
                    ChashMap.put("FormNum", checkSngl.getCheckFormNum());// 单据号
                    ChashMap.put("isNtChk", checkSngl.getIsNtChk());// 审核状态
                    ChashMap.put("setupPers", checkSngl.getSetupPers());// 创建人
                    ChashMap.put("setupTm", checkSngl.getSetupTm());// 创建时间

                    list.add(ChashMap);
                    break;
                default:
                    isSuccess = false;
                    message = outIntoWhs.getOutIntoWhsTypId() + "该单据类型不存在";
                    break;
            }
        } else if (formTypEncd.equals("028")) {
            // 028 盘点单
            CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(formNum);
            if (checkSngl == null) {
                isSuccess = true;
                message = message + formNum + "不属于盘点单或单据被删除\n";
            } else if (checkSngl.getIsNtChk() == 0) {
                isSuccess = true;
                message = message + formNum + "盘点单未审核\n";
            } else {
                CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectSrcCheckSnglLoss(formNum);
                if (checkPrftLoss == null) {
                    isSuccess = true;
                    message = message + formNum + "盘点单无损溢\n";
                } else {
                    HashMap<String, Object> BhashMap = new HashMap<String, Object>();
                    BhashMap.put("FormTypName", checkPrftLoss.getFormTypName());// 单据类型编码
                    BhashMap.put("FormTypEncd", checkPrftLoss.getFormTypEncd());// 单据类型名称
                    BhashMap.put("FormNum", checkPrftLoss.getCheckFormNum());// 单据号
                    BhashMap.put("isNtChk", checkPrftLoss.getIsNtChk());// 审核状态
                    BhashMap.put("setupPers", checkPrftLoss.getSetupPers());// 创建人
                    BhashMap.put("setupTm", checkPrftLoss.getSetupTm());// 创建时间
                    list.add(BhashMap);
                    if (checkPrftLoss.getIsNtChk() == 0) {
                        isSuccess = true;
                        message = message + formNum + "盘点单的损溢表未审核\n";
                    } else {
                        OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(checkPrftLoss.getCheckFormNum(), "8");
                        OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(checkPrftLoss.getCheckFormNum(), "7");

                        if (intoWhs == null) {
                            isSuccess = true;
                            message = message + formNum + "无其他入库单\n";
                        } else {
                            HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                            intohashMap.put("FormTypName", intoWhs.getFormTypName());// 单据类型编码
                            intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// 单据类型名称
                            intohashMap.put("FormNum", intoWhs.getFormNum());// 单据号
                            intohashMap.put("isNtChk", intoWhs.getIsNtChk());// 审核状态
                            intohashMap.put("setupPers", intoWhs.getSetupPers());// 创建人
                            intohashMap.put("setupTm", intoWhs.getSetupTm());// 创建时间
                            list.add(intohashMap);
                        }
                        if (outWhs == null) {
                            isSuccess = true;
                            message = message + formNum + "无其他出库单\n";
                        } else {
                            HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                            outhashMap.put("FormTypName", outWhs.getFormTypName());// 单据类型编码
                            outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// 单据类型名称
                            outhashMap.put("FormNum", outWhs.getFormNum());// 单据号
                            outhashMap.put("isNtChk", outWhs.getIsNtChk());// 审核状态
                            outhashMap.put("setupPers", outWhs.getSetupPers());// 创建人
                            outhashMap.put("setupTm", outWhs.getSetupTm());// 创建时间
                            list.add(outhashMap);
                        }

                    }
                }
            }

        } else if (formTypEncd.equals("029")) {
//			029	盘点损益单
//			7		盘亏出库
//			8		盘盈入库
            CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(formNum);
            if (checkPrftLoss == null) {
                isSuccess = true;
                message = message + formNum + "不属于盘点损溢单或单据被删除\n";
//            } else if (checkPrftLoss.getIsNtChk() == 0) {
//                isSuccess = true;
//                message = message + formNum + "盘点单损溢未审核\n";
            } else {
                CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(checkPrftLoss.getSrcFormNum());

                OthOutIntoWhs intoWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "8");
                OthOutIntoWhs outWhs = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(formNum, "7");
                if (checkSngl == null) {
                    isSuccess = true;
                    message = message + formNum + "无盘点单\n";
                } else {
                    HashMap<String, Object> checkhashMap = new HashMap<String, Object>();
                    checkhashMap.put("FormTypName", checkSngl.getFormTypName());// 单据类型编码
                    checkhashMap.put("FormTypEncd", checkSngl.getFormTypEncd());// 单据类型名称
                    checkhashMap.put("FormNum", checkSngl.getCheckFormNum());// 单据号
                    checkhashMap.put("isNtChk", checkSngl.getIsNtChk());// 审核状态
                    checkhashMap.put("setupPers", checkSngl.getSetupPers());// 创建人
                    checkhashMap.put("setupTm", checkSngl.getSetupTm());// 创建时间
                    list.add(checkhashMap);
                }

                if (intoWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "无其他入库单\n";
                } else {
                    HashMap<String, Object> intohashMap = new HashMap<String, Object>();
                    intohashMap.put("FormTypName", intoWhs.getFormTypName());// 单据类型编码
                    intohashMap.put("FormTypEncd", intoWhs.getFormTypEncd());// 单据类型名称
                    intohashMap.put("FormNum", intoWhs.getFormNum());// 单据号
                    intohashMap.put("isNtChk", intoWhs.getIsNtChk());// 审核状态
                    intohashMap.put("setupPers", intoWhs.getSetupPers());// 创建人
                    intohashMap.put("setupTm", intoWhs.getSetupTm());// 创建时间
                    list.add(intohashMap);
                }
                if (outWhs == null) {
                    isSuccess = true;
                    message = message + formNum + "无其他出库单\n";
                } else {
                    HashMap<String, Object> outhashMap = new HashMap<String, Object>();
                    outhashMap.put("FormTypName", outWhs.getFormTypName());// 单据类型编码
                    outhashMap.put("FormTypEncd", outWhs.getFormTypEncd());// 单据类型名称
                    outhashMap.put("FormNum", outWhs.getFormNum());// 单据号
                    outhashMap.put("isNtChk", outWhs.getIsNtChk());// 审核状态
                    outhashMap.put("setupPers", outWhs.getSetupPers());// 创建人
                    outhashMap.put("setupTm", outWhs.getSetupTm());// 创建时间
                    list.add(outhashMap);
                }
            }
        } else {
            isSuccess = false;
            message = message + formNum + "对应单据类型不存在\n";
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
            throw new RuntimeException("单据" + formNum + "不存在或已审核不需要分配货位");
        }
        List<MovBitTab> bitTabs = new ArrayList<>();
        MovBitTab bitTab;
        for (MovBitTab movBitTab : movBitTabList) {
//                bitListMapper
            //入库为1 出库为0
            if (movBitTab.getId() == 1 && movBitTab.getQty().compareTo(BigDecimal.ZERO) < 0) {
                flas = false;
                //红字销售出库 入负数
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
                        // 货位和-单据数
//                            BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // 单据数-货位和
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
                //采购退货 出负数
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
                        // 货位和-单据数
                        BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // 单据数-货位和
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
                //正常入库
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
                        // 货位和-单据数
//                            BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // 单据数-货位和
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
                //正常出库
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
                        // 货位和-单据数
//                            BigDecimal diff = mTab.getQty().subtract(movBitTab.getQty());
                        bitTab.setQty(movBitTab.getQty());
                        bitTabs.add(bitTab);
                        break;
                    } else if (mTab.getQty().compareTo(movBitTab.getQty()) == -1) {
                        // 单据数-货位和
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
                message = "其他异常";
            }
            //负数转换
            if (!flas) {
                bitTabs.stream().forEach((tab) -> tab.setQty(BigDecimal.ZERO.subtract(tab.getQty())));
            }
        }
        bitListMapper.deleteInvtyGdsBitList(Arrays.asList(formNum));
        if (bitTabs.size() > 0) {
            bitListMapper.insertInvtyGdsBitLists(bitTabs);
            message = "单据" + formNum + "自动分配货位成功";
        } else {
            message = "单据" + formNum + "自动分配货位失败";
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");

            }
            try {
                othOutIntoWhsMapper.exInsertOthOutIntoWhs(entry.getValue());
                othOutIntoWhsMapper.exInsertOthOutIntoWhsSubTab(entry.getValue().getOutIntoSubList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "其他出入库单新增成功！";
        try {
            resp = BaseJson.returnRespObj("whs/out_into_whs/uploadFileAddDbU8", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // 导入excle
    private Map<String, OthOutIntoWhs> uploadScoreInfoU8(MultipartFile file) {
        Map<String, OthOutIntoWhs> temp = new HashMap<>();
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
            // System.out.println(lastRowNum);
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
                // 创建实体类
                OthOutIntoWhs othOutIntoWhs = new OthOutIntoWhs();

                String orderNo = null;
                String formDt = null;
                String outIntoWhsTypId = null;
                String formTypEncd = null;
                String outStatus = null;
                String inStatus = null;
//				11		其他出库
//				12		其他入库
//				014	其他入库单
//				015	其他出库单

//				1		调拨入库
//				10		销售出库
//				11		其他出库
//				12		其他入库
//				2		调拨出库
//				3		组装入库
//				4		组装出库
//				5		拆卸入库
//				6		拆卸出库
//				7		盘亏出库
//				8		盘盈入库
//				9		采购入库
                othOutIntoWhs.setSrcFormNum(GetCellData(r, "业务号") == null || GetCellData(r, "业务号").equals("") ? null
                        : GetCellData(r, "业务号"));
                if (GetCellData(r, "业务类型").equals("其他入库") || GetCellData(r, "业务类型").equals("调拨入库")
                        || GetCellData(r, "业务类型").equals("盘盈入库") || GetCellData(r, "业务类型").equals("组装入库")
                        || GetCellData(r, "业务类型").equals("拆卸入库")) {
                    orderNo = GetCellData(r, "入库单号");
                    orderNo = "into" + orderNo;
                    othOutIntoWhs.setRecvSendCateId(GetCellData(r, "入库类别编码"));//收发类别id

                    formDt = GetCellData(r, "入库日期") == null ? "" : GetCellData(r, "入库日期").replaceAll("[^0-9:-]", " ");
                    if (GetCellData(r, "业务类型").equals("其他入库")) {
                        outIntoWhsTypId = "12";
                    } else if (GetCellData(r, "业务类型").equals("调拨入库")) {
                        outIntoWhsTypId = "1";

                    } else if (GetCellData(r, "业务类型").equals("盘盈入库")) {
                        outIntoWhsTypId = "8";

                    } else if (GetCellData(r, "业务类型").equals("组装入库")) {
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "业务号") == null || GetCellData(r, "业务号").equals("") ? null
                                : "zc" + GetCellData(r, "业务号"));
                        outIntoWhsTypId = "3";

                    } else if (GetCellData(r, "业务类型").equals("拆卸入库")) {
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "业务号") == null || GetCellData(r, "业务号").equals("") ? null
                                : "cz" + GetCellData(r, "业务号"));
                        outIntoWhsTypId = "5";

                    }

                    formTypEncd = "014";
                    inStatus = "处理中";

                } else if (GetCellData(r, "业务类型").equals("其他出库") || GetCellData(r, "业务类型").equals("调拨出库")
                        || GetCellData(r, "业务类型").equals("盘亏出库") || GetCellData(r, "业务类型").equals("组装出库")
                        || GetCellData(r, "业务类型").equals("拆卸出库")) {
                    orderNo = GetCellData(r, "出库单号");
                    orderNo = "out" + orderNo;
                    othOutIntoWhs.setRecvSendCateId(GetCellData(r, "出库类别编码"));//收发类别id

                    if (GetCellData(r, "业务类型").equals("其他出库")) {
                        outIntoWhsTypId = "11";
                    } else if (GetCellData(r, "业务类型").equals("调拨出库")) {
                        outIntoWhsTypId = "2";

                    } else if (GetCellData(r, "业务类型").equals("盘亏出库")) {
                        outIntoWhsTypId = "7";

                    } else if (GetCellData(r, "业务类型").equals("组装出库")) {
                        outIntoWhsTypId = "4";
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "业务号") == null || GetCellData(r, "业务号").equals("") ? null
                                : "zc" + GetCellData(r, "业务号"));

                    } else if (GetCellData(r, "业务类型").equals("拆卸出库")) {
                        outIntoWhsTypId = "6";
                        othOutIntoWhs.setSrcFormNum(GetCellData(r, "业务号") == null || GetCellData(r, "业务号").equals("") ? null
                                : "cz" + GetCellData(r, "业务号"));

                    }
                    formDt = GetCellData(r, "出库日期") == null ? "" : GetCellData(r, "出库日期").replaceAll("[^0-9:-]", " ");
                    formTypEncd = "015";
                    outStatus = "处理中";
                }

                // System.out.println(orderNo);

                if (temp.containsKey(orderNo)) {
                    othOutIntoWhs = temp.get(orderNo);
                }

                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                // r.getCell(6).getStringCellValue()
                othOutIntoWhs.setFormNum(orderNo); // 单号 q varchar(200
                othOutIntoWhs.setFormDt(formDt); // 单据日期
                othOutIntoWhs.setWhsEncd(GetCellData(r, "仓库编码")); // 仓库编码 q varchar(200
//                othOutIntoWhs.setSrcFormNum(null); // 来源单据号 q varchar(200

                othOutIntoWhs.setOutIntoWhsTypId(outIntoWhsTypId); // 出入库类别编码 q 根据业务类型判断 varchar(200
                othOutIntoWhs.setFormTypEncd(formTypEncd);// 014 其他入库单 015 其他出库单

                othOutIntoWhs.setOutStatus(outStatus); // 出库状态 单据类型
                othOutIntoWhs.setInStatus(inStatus); // 入库状态 单据类型
                othOutIntoWhs.setMemo(GetCellData(r, "备注")); // 备注
                othOutIntoWhs.setIsNtWms(0); // 是否向WMS上传

                othOutIntoWhs.setIsNtChk(0); // 是否审核 int(11
                othOutIntoWhs.setIsNtBookEntry(0); // 是否记账 int(11
                othOutIntoWhs.setIsNtCmplt(0); // 是否完成 int(11
                othOutIntoWhs.setIsNtClos(0); // 是否关闭 int(11
                othOutIntoWhs.setPrintCnt(
                        (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
                                : GetCellData(r, "打印次数"))).intValue()); // 打印次数 q int(11
                othOutIntoWhs.setSetupPers(GetCellData(r, "制单人")); // 创建人
                othOutIntoWhs.setSetupTm(
                        GetCellData(r, "制单时间") == null ? "" : GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " ")); // 创建时间
                // q
                // datetime
                othOutIntoWhs.setMdfr(GetCellData(r, "修改人")); // 修改人 q varchar(200
                othOutIntoWhs.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ")); // 修改时间
                // q
                // datetime
                othOutIntoWhs.setChkr(GetCellData(r, "审核人")); // 审核人 q varchar(200
                othOutIntoWhs.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " ")); // 审核时间
                // q
                // datetime
                othOutIntoWhs.setBookEntryPers(GetCellData(r, "记账人")); // 记账人 q varchar(200
                othOutIntoWhs.setBookEntryTm(null); // 记账时间
                // datetime
                othOutIntoWhs.setOperator(null); // 操作人 q varchar(200
                othOutIntoWhs.setOperatorId(null); // 操作人编码 q varchar(200

                othOutIntoWhs.setIsNtMakeVouch(0); // 是否生成凭证 int(11
                othOutIntoWhs.setMakVouchPers(null); // 制凭证人
                othOutIntoWhs.setMakVouchTm(null); // 制凭证时间

                List<OthOutIntoWhsSubTab> OthOutIntoWhsSub = othOutIntoWhs.getOutIntoSubList();// 订单子表
                if (OthOutIntoWhsSub == null) {
                    OthOutIntoWhsSub = new ArrayList<>();
                }

                OthOutIntoWhsSubTab othOutIntoWhsSubList = new OthOutIntoWhsSubTab();

//				othOutIntoWhsSubList.setOrdrNum(null); // 序号 bigint(20
                othOutIntoWhsSubList.setFormNum(orderNo); // 单号 varchar(200
                othOutIntoWhsSubList.setInvtyEncd(GetCellData(r, "存货编码")); // 存货编码 1 varchar(200
                othOutIntoWhsSubList.setQty(new BigDecimal(GetCellData(r, "数量") == null ? "0" : GetCellData(r, "数量"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 数量
                othOutIntoWhsSubList.setTaxRate(BigDecimal.ZERO); // 税率
                othOutIntoWhsSubList.setCntnTaxUprc(BigDecimal.ZERO); // 含税单价
                othOutIntoWhsSubList.setUnTaxUprc(new BigDecimal(
                        GetCellData(r, "单价") == null || GetCellData(r, "单价").equals("") ? "0" : GetCellData(r, "单价"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税单价
                othOutIntoWhsSubList.setBookEntryUprc(BigDecimal.ZERO); // 记账单价
                othOutIntoWhsSubList.setCntnTaxAmt(BigDecimal.ZERO); // 含税金额
                othOutIntoWhsSubList.setUnTaxAmt(new BigDecimal(
                        GetCellData(r, "金额") == null || GetCellData(r, "金额").equals("") ? "0" : GetCellData(r, "金额"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税金额
                othOutIntoWhsSubList.setIntlBat(GetCellData(r, "国际批号")); // 国际批号
                othOutIntoWhsSubList.setBatNum(GetCellData(r, "批号")); // 批号 1 varchar(200
                othOutIntoWhsSubList.setPrdcDt(
                        GetCellData(r, "生产日期") == null ? "" : GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " ")); // 生产日期

                BigDecimal bxRule = GetCellData(r, "箱规") == null || GetCellData(r, "箱规").equals("") ? null
                        : new BigDecimal(GetCellData(r, "箱规"));
                if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                    othOutIntoWhsSubList
                            .setBxQty(othOutIntoWhsSubList.getQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // 箱数
                } else {
                    othOutIntoWhsSubList.setBxQty(BigDecimal.ZERO);
                }

                String BaoZhiQi = GetCellData(r, "保质期");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }

                othOutIntoWhsSubList.setBaoZhiQi(BaoZhiQi); // 保质期 1 varchar(200
                othOutIntoWhsSubList.setInvldtnDt(
                        GetCellData(r, "失效日期") == null ? "" : GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " ")); // 失效日期
                // 1
                // datetime
                othOutIntoWhsSubList.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                OthOutIntoWhsSub.add(othOutIntoWhsSubList);

                othOutIntoWhs.setOutIntoSubList(OthOutIntoWhsSub);
                temp.put(orderNo, othOutIntoWhs);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());

        }
        return temp;
    }
}
