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
    // 订单号
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
     * 仓库
     */
    @Autowired
    WhsDocMapper whsDocMapper;

    @Autowired
    OthOutIntoWhsServiceImpl intoWhsServiceImpl;

    // 新增损益表
    @Override
    public String insertCheckSnglLoss(String userId, CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList ,String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            String number = getOrderNo.getSeqNo("SY", userId,loginTime);// 获取订单号

            if (checkPrftLossMapper.selectCheckSnglLoss(number) != null) {
                message = "编号" + number + "已存在，请重新输入！";
                isSuccess = false;
            } else {
                if (checkPrftLossMapper.selectCheckSnglLossSubTab(number).size() > 0) {
                    checkPrftLossMapper.deleteCheckSnglSubLossTab(number);
                }
                cSngl.setCheckFormNum(number);// 单据号
                checkPrftLossMapper.insertCheckSnglLoss(cSngl);
                checkPrftLossMapper.insertCheckSnglLossSubTab(cSubTabList);

                message = "新增成功！";
                isSuccess = true;
            }

            resp = BaseJson.returnRespObj("whs/check_sngl_loss/insertCheckSnglLoss", isSuccess, message, null);
        } catch (Exception e) {

        }
        return resp;
    }

    // 修改损益表
    @Override
    public String updateCheckSnglLoss(CheckPrftLoss cSngl, List<CheckPrftLossSubTab> cSubTabList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(cSngl.getCheckFormNum());
        if (checkPrftLoss.getIsNtChk() == null) {
            throw new RuntimeException("单号审核状态异常");
        } else if (checkPrftLoss.getIsNtChk() == 1) {
            throw new RuntimeException("单号已审核不允许修改");
        }

//		checkPrftLossMapper.deleteCheckSnglSubLossTab(cSngl.getCheckFormNum());
        checkPrftLossMapper.updateCheckSnglLoss(cSngl);
        for (CheckPrftLossSubTab cPrftLossSubTab : cSubTabList) {
            // 获取未税单价
            BigDecimal unTaxUprc = cPrftLossSubTab.getUnTaxUprc();
            // 获取税率 页面税率未整数，我们需要将税率/100
            BigDecimal taxRate = cPrftLossSubTab.getTaxRate().divide(new BigDecimal(100));
            // 损益数量
            BigDecimal prftLossQtysg = cPrftLossSubTab.getPrftLossQtys();
            // 未税损益金额
            cPrftLossSubTab.setUnTaxPrftLossAmts(unTaxUprc.multiply(prftLossQtysg));
            //损益税额
            cPrftLossSubTab.setPrftLossTaxAmts(prftLossQtysg.multiply(unTaxUprc).multiply(taxRate));
            // 含税损益金额
            cPrftLossSubTab.setCntnTaxPrftLossAmts(cPrftLossSubTab.getUnTaxPrftLossAmts().add(cPrftLossSubTab.getPrftLossTaxAmts()));//含税损益金额
        }
        checkPrftLossMapper.updateCheckPrftLossSubTab(cSubTabList);

//		.insertCheckSnglLossSubTab(cSubTabList);
        message = "更新成功！";

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/updateCheckSnglLoss", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 批量删除
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
            message = "删除成功！" + lists.toString();
            if (lists2.size() > 0) {
                message = message + "\r编号已审：" + lists2;
            }
        } else {
            isSuccess = false;
            message = "编号" + lists2 + "已审";
        }

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl_loss/deleteAllCheckSnglLoss", isSuccess, message, null);
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
        }
        return list;
    }

    // 查询
    @Override
    public String query(String checkFormNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<CheckPrftLossSubTab> cSubTabList = new ArrayList<>();
        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectCheckSnglLoss(checkFormNum);
        if (checkPrftLoss != null) {
            cSubTabList = checkPrftLossMapper.selectCheckSnglLossSubTab(checkFormNum);
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + checkFormNum + "不存在！";
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
            resp = BaseJson.returnRespList("whs/check_sngl_loss/queryList", true, "查询成功！", count, pageNo, pageSize,
                    listNum, pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 审核
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
        String chkr = userName;// 审核人
        String number = null;// 入库主=======
        String numberc = null;// 出库主=======

        List<CheckPrftLoss> cPrft = new ArrayList<>();// 盘点损益主表
        List<CheckPrftLossSubTab> cPrftSubTab = new ArrayList<>();// 盘点损益子表
        List<InvtyTab> invtyTabs = new ArrayList<>();// 库存表list
        OthOutIntoWhs othOutWhs = new OthOutIntoWhs();// 其他出库主
        OthOutIntoWhs othIntoWhs = new OthOutIntoWhs();// 其他入库主
        List<OthOutIntoWhsSubTab> othOutWhsSubTabs = new ArrayList<>();// 其他出库子
        List<OthOutIntoWhsSubTab> othIntoWhsSubTabs = new ArrayList<>();// 其他入库子
        List<MovBitTab> movBitTabInto = new ArrayList<>();// 货位入
        List<MovBitTab> movBitTabOut = new ArrayList<>();// 货位出
        Map<String, OthOutIntoWhsSubTab> IntoMap = new HashMap<String, OthOutIntoWhsSubTab>();// 货位入
        Map<String, OthOutIntoWhsSubTab> OutMap = new HashMap<String, OthOutIntoWhsSubTab>();// 货位出

        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectIsChr(danHao);
        if (checkPrftLoss == null) {
            throw new RuntimeException("单据:" + danHao + "不存在");
        }
        checkPrftLoss.setChkr(chkr);
        checkPrftLoss.setChkTm(loginTime);
        // 盘点损益主表
        cPrft.add(checkPrftLoss);

        if (checkPrftLoss.getIsNtChk() == 1) {
            throw new RuntimeException("单号：" + danHao + "已审核，不需要再次审核");

        } else {
            int a = checkPrftLossMapper.updateCSnglLossChk(cPrft);
            if (a >= 1) {
                // 库存表
                boolean isNtPrgrGds = isNtPrgrGdsBitMgmt(checkPrftLoss.getWhsEncd());
                cPrftSubTab = checkPrftLossMapper.selectCheckSnglLossSubTab(danHao);

                for (CheckPrftLossSubTab dbzi : cPrftSubTab) {
                    InvtyTab invtyTab = new InvtyTab();
                    invtyTab.setWhsEncd(checkPrftLoss.getWhsEncd());// 仓库编码
                    // 库存表
                    invtyTab.setInvtyEncd(dbzi.getInvtyEncd());
                    invtyTab.setBatNum(dbzi.getBatNum());
                    invtyTab.setAvalQty(dbzi.getPrftLossQtys().abs());// 可用量
//					invtyTab.setNowStok(BigDecimal.ZERO);// 现存量
                    invtyTab.setPrdcDt(dbzi.getPrdcDt());
                    invtyTab.setBaoZhiQi(dbzi.getBaoZhiQi());
                    invtyTab.setInvldtnDt(dbzi.getInvldtnDt());
                    invtyTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());// 含税单价
                    invtyTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());// 未税单价
                    invtyTab.setTaxRate(dbzi.getTaxRate().abs());// 税率
                    invtyTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// 含税损益数量
                    invtyTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// 未税损益数量
                    invtyTabs.add(invtyTab);
                    // 盈 入库
                    String dbziKey = dbzi.getInvtyEncd() + "__" + dbzi.getBatNum();

                    if (dbzi.getPrftLossQtys().doubleValue() > 0) {
                        // 入库主=======
                        if (number == null) {
                            try {
                                number = getOrderNo.getSeqNo("OTCR", userId,loginTime);
                            } catch (Exception e) {
                                throw new RuntimeException("获取订单号失败 ");
                            } // 获取订单号
                            othIntoWhs.setFormNum(number);// 订单号
                            othIntoWhs.setFormDt(formDate);
                            othIntoWhs.setOutIntoWhsTypId("8");// 盘盈入库类型
                            othIntoWhs.setInStatus("处理中");// 入库状态
                            othIntoWhs.setSetupPers(userName);// 创建人
                            othIntoWhs.setWhsEncd(checkPrftLoss.getWhsEncd());// 仓库编码
                            othIntoWhs.setSrcFormNum(checkPrftLoss.getCheckFormNum());// 来源单据号
                            othIntoWhs.setFormTypEncd("014");
                            othIntoWhs.setMemo(checkPrftLoss.getMemo());// 备注
                            othIntoWhs.setIsNtChk(0);// 审核状态
                        }
                        // 入库子
                        if (IntoMap.containsKey(dbziKey)) {
                            OthOutIntoWhsSubTab othIntoWhsSubTab = IntoMap.get(dbziKey);
                            othIntoWhsSubTab.setQty(othIntoWhsSubTab.getQty().add(dbzi.getPrftLossQtys().abs()));// 损益数量dbzi.getPrftLossQtys().abs()
                            othIntoWhsSubTab.setCntnTaxAmt(
                                    othIntoWhsSubTab.getCntnTaxAmt().add(dbzi.getCntnTaxPrftLossAmts().abs()));// 含税金额
                            // 含税盈亏金额
                            othIntoWhsSubTab.setUnTaxAmt(othIntoWhsSubTab.getUnTaxAmt().add(dbzi.getUnTaxPrftLossAmts().abs()));// 未税金额
                            // 未税盈亏金额
                            othIntoWhsSubTab.setTaxAmt(othIntoWhsSubTab.getTaxAmt().add(dbzi.getPrftLossTaxAmts().abs()));//  盈亏税金额
                            IntoMap.put(dbziKey, othIntoWhsSubTab);
                        } else {
                            OthOutIntoWhsSubTab othIntoWhsSubTab = new OthOutIntoWhsSubTab();
                            othIntoWhsSubTab.setFormNum(number);
                            othIntoWhsSubTab.setInvtyEncd(dbzi.getInvtyEncd());// 存货编码
                            othIntoWhsSubTab.setQty(dbzi.getPrftLossQtys().abs());// 损益数量
                            othIntoWhsSubTab.setTaxRate(dbzi.getTaxRate().abs());// 税率
                            othIntoWhsSubTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());// 含税单价
                            othIntoWhsSubTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());// 未税单价[单价(根据批次带入单价)]
                            othIntoWhsSubTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// 含税金额 含税盈亏金额
                            othIntoWhsSubTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// 未税金额 未税盈亏金额
                            othIntoWhsSubTab.setTaxAmt(dbzi.getPrftLossTaxAmts().abs());//税额  损益税额
                            othIntoWhsSubTab.setBatNum(dbzi.getBatNum());// 批号
                            othIntoWhsSubTab.setPrdcDt(dbzi.getPrdcDt());// 生产日期
                            othIntoWhsSubTab.setBaoZhiQi(dbzi.getBaoZhiQi());// 保质期
                            othIntoWhsSubTab.setInvldtnDt(dbzi.getInvldtnDt());// 失效日期
                            othIntoWhsSubTab.setBxRule(dbzi.getBxRule());
                            othIntoWhsSubTab.setGdsBitEncd(dbziKey);
                            othIntoWhsSubTab.setProjEncd(dbzi.getProjEncd());

                            IntoMap.put(dbziKey, othIntoWhsSubTab);
                        }
                        // 是否货位管理
                        if (isNtPrgrGds) {
                            MovBitTab bitTab = new MovBitTab();
                            bitTab.setWhsEncd(checkPrftLoss.getWhsEncd());// 仓库编码
                            bitTab.setInvtyEncd(dbzi.getInvtyEncd());
                            bitTab.setBatNum(dbzi.getBatNum());
                            bitTab.setQty(dbzi.getPrftLossQtys().abs());// 可用量
                            bitTab.setPrdcDt(dbzi.getPrdcDt());
                            bitTab.setGdsBitEncd(dbzi.getGdsBitEncd());

                            bitTab.setRegnEncd(
                                    whsDocMapper.selectWhsGdsReal(checkPrftLoss.getWhsEncd(), dbzi.getGdsBitEncd()));

                            bitTab.setGdsBitEncd1(dbziKey);

                            movBitTabInto.add(bitTab);
                        }

                    }

                    // 亏 出库
                    if (dbzi.getPrftLossQtys().doubleValue() < 0) {
                        // 修改库存表出库数量

                        // 出库主=======
                        if (numberc == null) {
                            try {
                                numberc = getOrderNo.getSeqNo("OTCR", userId,loginTime);
                            } catch (Exception e) {

                                throw new RuntimeException("获取订单号失败 ");
                            } // 获取订单号
                            othOutWhs.setFormNum(numberc);// 订单号
                            othOutWhs.setFormDt(formDate);
                            othOutWhs.setOutIntoWhsTypId("7");// 盘亏出库类型
                            othOutWhs.setOutStatus("处理中");// 出库状态
                            othOutWhs.setSetupPers(userName);// 创建人
                            othOutWhs.setWhsEncd(checkPrftLoss.getWhsEncd());// 转出仓库编码
                            othOutWhs.setSrcFormNum(checkPrftLoss.getCheckFormNum());// 来源单据号
                            othOutWhs.setMemo(checkPrftLoss.getMemo());
                            othOutWhs.setFormTypEncd("015");
                            othOutWhs.setIsNtChk(0);
                        }

                        // 出库子
                        if (OutMap.containsKey(dbziKey)) {
                            OthOutIntoWhsSubTab othOutWhsSubTab = OutMap.get(dbziKey);
                            othOutWhsSubTab.setQty(othOutWhsSubTab.getQty().add(dbzi.getPrftLossQtys().abs()));// 损益数量dbzi.getPrftLossQtys().abs()
                            othOutWhsSubTab.setCntnTaxAmt(othOutWhsSubTab.getCntnTaxAmt().add(dbzi.getCntnTaxPrftLossAmts().abs()));// 含税金额
                            // 含税盈亏金额
                            othOutWhsSubTab.setUnTaxAmt(othOutWhsSubTab.getUnTaxAmt().add(dbzi.getUnTaxPrftLossAmts().abs()));// 未税金额
                            // 未税盈亏金额
                            othOutWhsSubTab.setTaxAmt(othOutWhsSubTab.getTaxAmt().add(dbzi.getPrftLossTaxAmts().abs()));//  盈亏税金额
                            OutMap.put(dbziKey, othOutWhsSubTab);

                        } else {
                            OthOutIntoWhsSubTab othOutWhsSubTab = new OthOutIntoWhsSubTab();
                            othOutWhsSubTab.setFormNum(numberc);
                            othOutWhsSubTab.setInvtyEncd(dbzi.getInvtyEncd());
                            othOutWhsSubTab.setQty(dbzi.getPrftLossQtys().abs());
                            othOutWhsSubTab.setTaxRate(dbzi.getTaxRate());
                            othOutWhsSubTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());
                            othOutWhsSubTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());
                            othOutWhsSubTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// 含税金额 cntnTaxPrftLossAmt
                            othOutWhsSubTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// 未税盈亏金额 getUnTaxPrftLossAmt
                            othOutWhsSubTab.setTaxAmt(dbzi.getPrftLossTaxAmts().abs());//税额  损益税额
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
                            bitTab.setWhsEncd(checkPrftLoss.getWhsEncd());// 仓库编码
                            bitTab.setInvtyEncd(dbzi.getInvtyEncd());
                            bitTab.setBatNum(dbzi.getBatNum());
                            bitTab.setQty(dbzi.getPrftLossQtys().abs());// 可用量
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
                    othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);// 其他入库主
                    othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);// 其他入库子

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
                    message = "单据" + danHao + "单据审核成功";

                }
                if (othOutWhsSubTabs.size() > 0) {
                    othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);// 其他出库主
                    othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);// 其他出库子
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
                    message = "单据" + danHao + "单据审核成功";
                }
                if (othOutWhsSubTabs.size() == 0 && othIntoWhsSubTabs.size() == 0) {
                    message = "单据" + danHao + "审核成功！但没有损益产生";
                }

            } else {
                throw new RuntimeException("单据" + danHao + "审核失败！");
            }

        }

        return message;
    }

    // 弃审
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

        List<CheckPrftLoss> cPrft = new ArrayList<>();// 盘点损益主表
        List<CheckPrftLossSubTab> cPrftSubTab = new ArrayList<>();// 盘点损益子表
        List<InvtyTab> invtyTabs = new ArrayList<>();// 库存表list

        cPrft = new ArrayList<>();// 盘点损益主表
        cPrftSubTab = new ArrayList<>();// 盘点损益子表
        invtyTabs = new ArrayList<>();// 库存表list

        // 盘点损益主表
        CheckPrftLoss checkPrftLoss = checkPrftLossMapper.selectIsChr(danHao);
        if (checkPrftLoss == null) {
            throw new RuntimeException("单据:" + danHao + "不存在 ");

        }
        if (checkPrftLoss.getIsNtChk() == 0) {
            throw new RuntimeException("单据:" + danHao + "已弃审,不需要重复弃审 ");

        }

        // 查询是否有其他出入库
        List<OthOutIntoWhs> count = cannibSnglMapper.selectOthIsDelete(danHao);

        if (count.size() > 0) {
            throw new RuntimeException("单号：" + danHao + "对应的其他出入库未删除，请先删除");
        }
        // 已删除其他出入库的数据，进行弃审操作
        cPrft.add(checkPrftLoss);

        // 跟新弃审状态
        int a = checkPrftLossMapper.updateCSnglLossNoChk(cPrft);

        if (a >= 1) {
            message = "单据:" + danHao + "弃审成功";
//			cPrftSubTab = checkPrftLossMapper.selectCheckSnglLossSubTab(danHao);
//			for (CheckPrftLossSubTab dbzi : cPrftSubTab) {
//				// 库存表
//				InvtyTab invtyTab = new InvtyTab();
//				invtyTab.setWhsEncd(checkPrftLoss.getWhsEncd());
//				// 库存表
//				invtyTab.setInvtyEncd(dbzi.getInvtyEncd());
//				invtyTab.setBatNum(dbzi.getBatNum());
//				invtyTab.setAvalQty(dbzi.getPrftLossQtys().abs());// 可用量
//				invtyTab.setNowStok(dbzi.getPrftLossQtys().abs());// 现存量
//				invtyTab.setPrdcDt(dbzi.getPrdcDt());// 生产日期
//				invtyTab.setBaoZhiQi(dbzi.getBaoZhiQi());
//				invtyTab.setInvldtnDt(dbzi.getInvldtnDt());// 失效日期
////				BigDecimal cntnTaxUprc = new BigDecimal(dbzi.get("cntnTaxUprc").asText());
//				invtyTab.setCntnTaxUprc(dbzi.getCntnTaxUprc().abs());
////				BigDecimal unTaxUprc = new BigDecimal(dbzi.get("unTaxUprc").asText());
//				invtyTab.setUnTaxUprc(dbzi.getUnTaxUprc().abs());
////				BigDecimal taxRate = new BigDecimal(dbzi.get("taxRate").asText());
//				invtyTab.setTaxRate(dbzi.getTaxRate().abs());
////				BigDecimal cntnTaxAmt = new BigDecimal(dbzi.get("cntnTaxPrftLossAmts").asText());
//				invtyTab.setCntnTaxAmt(dbzi.getCntnTaxPrftLossAmts().abs());// cntnTaxPrftLossAmts;含税损益数量
////				BigDecimal unTaxAmt = new BigDecimal(dbzi.get(   "unTaxPrftLossAmts").asText());
//				invtyTab.setUnTaxAmt(dbzi.getUnTaxPrftLossAmts().abs());// 未税损益数量unTaxPrftLossAmts
//				// 区域编码 货位编码 (通过仓库编码、存货编码、批号查询查询出货位编码 通过货位编码查询仓库编码)
//				invtyTabs.add(invtyTab);
//
//				double prftLossQtys = dbzi.getPrftLossQtys().doubleValue();
//				// 盈 入库
//				if (prftLossQtys > 0) {
//
//					// 修改库存表出库数量
//					for (InvtyTab iTab : invtyTabs) {
//					}
//					/// 查询库存表
//					InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
//					for (CheckPrftLossSubTab cTab : cPrftSubTab) {
//					}
//					if ((inTab.getAvalQty().compareTo(dbzi.getPrftLossQtys()) == 1
//							|| inTab.getAvalQty().compareTo(dbzi.getPrftLossQtys()) == 0)
//							&& (inTab.getNowStok().compareTo(dbzi.getPrftLossQtys()) == 1
//									|| inTab.getNowStok().compareTo(dbzi.getPrftLossQtys()) == 0)) {
//
//						invtyNumMapper.updateAInvtyTab(invtyTabs);// 修改库存表 可用量减少
//						invtyNumMapper.updateInvtyTabAmt(invtyTabs);// 修改库存表 现存量减少
//						// 修改库存金额没写 SQL
//						invtyTabs.clear();
//						message += checkPrftLoss.getCheckFormNum() + " 单据弃审成功！";
//
//					} else
//
//					if (inTab.getAvalQty().compareTo(dbzi.getPrftLossQtys()) == -1
//							|| inTab.getNowStok().compareTo(dbzi.getPrftLossQtys()) == -1) {
//						message = inTab.getInvtyEncd() + "库存中数量不足 ";
//						try {
//							throw new RuntimeException(danHao+"单号"+inTab.getBatNum()+"批号"+inTab.getInvtyEncd() + "  库存中数量不足 损益数" + dbzi.getPrftLossQtys());
//						} catch (Exception e) {
//							
//							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动回滚失误
//							return message;
//						}
//
//					}
//
//				}
//
//				// 亏 出库
//				if (prftLossQtys < 0) {
//					// System.out.println("亏 出库");
//					// 修改库存表入库数量
//					if (invtyNumMapper.selectInvtyTabByTerm(invtyTab) != null) {
//						// 如果其他入库 不等于空 修改库存表数量
//						invtyNumMapper.updateAInvtyTabJia(invtyTabs);// 修改库存表 可用量增加
//						invtyNumMapper.updateInvtyTabAmtJia(invtyTabs);// 修改库存表 现存量增加
//						invtyTabs.clear();
//						// 修改库存金额没写 SQL
//					} else {
//						// 新增库存表 入库
//						invtyNumMapper.insertInvtyTabList(invtyTabs);
//						invtyTabs.clear();
//					}
//				}
//
//			}

        } else {
            throw new RuntimeException("单据：" + danHao + "单据弃审失败！");

        }

        return message;
    }

    // 打印
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
            resp = BaseJson.returnRespObjListAnno("whs/check_sngl_loss/queryListDaYin", true, "查询成功！", null, aList);
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getCheckFormNum() + " 请检查");

            }
            try {
                checkPrftLossMapper.exInsertCSngl(entry.getValue());

                checkPrftLossMapper.insertCheckSnglLossSubTab(entry.getValue().getcPrftLossList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "调拨单新增成功！";
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
                String orderNo = GetCellData(r, "盘点单号");
                // 创建实体类
                // System.out.println(orderNo);
                CheckPrftLoss checkSngl = new CheckPrftLoss();
                if (temp.containsKey(orderNo)) {
                    checkSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//				// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上

                checkSngl.setSrcFormNum(orderNo);// 来源单据号
                checkSngl.setCheckFormNum(orderNo);// 盘点单号
                checkSngl.setFormTypEncd("029");

                checkSngl.setCheckDt((GetCellData(r, "盘点日期") == null || GetCellData(r, "盘点日期").equals("")) ? null
                        : GetCellData(r, "盘点日期").replaceAll("[^0-9:-]", " "));// 盘点日期
                checkSngl.setBookDt((GetCellData(r, "盘点日期") == null || GetCellData(r, "盘点日期").equals("")) ? null
                        : GetCellData(r, "盘点日期").replaceAll("[^0-9:-]", " "));// 账面日期q
                checkSngl.setWhsEncd(GetCellData(r, "盘点仓库编码"));// 仓库编码
                checkSngl.setCheckBat(GetCellData(r, "盘点批号"));// 盘点批号
                checkSngl.setSetupPers(GetCellData(r, "制单人")); // 创建人q
                checkSngl.setSetupTm((GetCellData(r, "制单时间") == null || GetCellData(r, "制单时间").equals("")) ? null
                        : GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " ")); // 创建时间q
                checkSngl.setMdfr(GetCellData(r, "修改人")); // 修改人q
                checkSngl.setModiTm((GetCellData(r, "修改时间") == null || GetCellData(r, "修改时间").equals("")) ? null
                        : GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ")); // 修改时间q
                checkSngl.setChkr(GetCellData(r, "审核人")); // 审核人q
                checkSngl.setChkTm((GetCellData(r, "审核时间") == null || GetCellData(r, "审核时间").equals("")) ? null
                        : GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " ")); // 审核时间q
                checkSngl.setMemo(GetCellData(r, "备注"));// 备注

                checkSngl.setBookEntryPers(null);// 记账人

                checkSngl.setBookEntryTm(null);// 记账时间
                checkSngl.setIsNtChk(0);// 是否审核
                checkSngl.setIsNtBookEntry(0);// 是否记账
                checkSngl.setIsNtCmplt(0);// 是否完成
                checkSngl.setIsNtClos(0);// 是否关闭
                checkSngl.setPrintCnt(
                        new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
                                : GetCellData(r, "打印次数")).intValue());// 打印次数q
                checkSngl.setIsNtWms(0);// 是否向WMS上传q
//				// System.out.println("isNtWms"+isNtWms);
                List<CheckPrftLossSubTab> checkSnglSubList = checkSngl.getcPrftLossList();// 订单子表
                if (checkSnglSubList == null) {
                    checkSnglSubList = new ArrayList<>();
                }

                CheckPrftLossSubTab checkSnglSubTab = new CheckPrftLossSubTab();

                checkSnglSubTab.setCheckFormNum(orderNo);// 盘点单号
                checkSnglSubTab.setInvtyEncd(GetCellData(r, "存货编码")); // 存货编码
                checkSnglSubTab.setBatNum(GetCellData(r, "批号")); // 批号

                checkSnglSubTab.setInvtyBigClsEncd(null); // 存货大类编码
                checkSnglSubTab.setPrdcDt((GetCellData(r, "生产日期") == null || GetCellData(r, "生产日期").equals("")) ? null
                        : GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " ")); // 生产日期1
                String BaoZhiQi = GetCellData(r, "保质期");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                checkSnglSubTab.setBaoZhiQi(BaoZhiQi); // 保质期1

                checkSnglSubTab
                        .setInvldtnDt((GetCellData(r, "失效日期") == null || GetCellData(r, "失效日期").equals("")) ? null
                                : GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " ")); // 失效日期1
                checkSnglSubTab.setBookQty(
                        new BigDecimal(GetCellData(r, "账面数量") == null || GetCellData(r, "账面数量").equals("") ? "0"
                                : GetCellData(r, "账面数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 账面数量1
                checkSnglSubTab.setAdjOutWhsQty(
                        new BigDecimal(GetCellData(r, "调整出库数量") == null || GetCellData(r, "调整出库数量").equals("") ? "0"
                                : GetCellData(r, "调整出库数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 调整出库数量1
                checkSnglSubTab.setAdjIntoWhsQty(
                        new BigDecimal(GetCellData(r, "调整入库数量") == null || GetCellData(r, "调整入库数量").equals("") ? "0"
                                : GetCellData(r, "调整入库数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 调整入库数量1
                // 账面数量+调整入库数量- 调整出库数量
                checkSnglSubTab.setBookAdjustQty(checkSnglSubTab.getBookQty()
                        .add(checkSnglSubTab.getAdjIntoWhsQty().subtract(checkSnglSubTab.getAdjOutWhsQty()))); // 账面调节数量?
//				new BigDecimal(GetCellData(r, "账面调节数量") == null || GetCellData(r, "账面调节数量").equals("") ? "0"
//				: GetCellData(r, "账面调节数量")).setScale(8, BigDecimal.ROUND_HALF_UP)

                checkSnglSubTab.setCheckQty(
                        new BigDecimal(GetCellData(r, "盘点数量") == null || GetCellData(r, "盘点数量").equals("") ? "0"
                                : GetCellData(r, "盘点数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盘点数量1
                checkSnglSubTab.setPrftLossQty(
                        new BigDecimal(GetCellData(r, "盈亏数量") == null || GetCellData(r, "盈亏数量").equals("") ? "0"
                                : GetCellData(r, "盈亏数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盈亏数量1
                checkSnglSubTab.setPrftLossQtys(
                        new BigDecimal(GetCellData(r, "盈亏数量") == null || GetCellData(r, "盈亏数量").equals("") ? "0"
                                : GetCellData(r, "盈亏数量")).setScale(8, BigDecimal.ROUND_HALF_UP));
                checkSnglSubTab.setPrftLossRatio(
                        new BigDecimal(GetCellData(r, "盈亏比例%") == null || GetCellData(r, "盈亏比例%").equals("") ? "0"
                                : GetCellData(r, "盈亏比例%")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盈亏比例(%)1
                checkSnglSubTab.setReasn(GetCellData(r, "原因")); // 原因1
                checkSnglSubTab.setTaxRate(BigDecimal.ZERO);

                checkSnglSubTab.setCntnTaxUprc(BigDecimal.ZERO);// 含税单价

                checkSnglSubTab.setUnTaxUprc(new BigDecimal(
                        GetCellData(r, "单价") == null || GetCellData(r, "单价").equals("") ? "0" : GetCellData(r, "单价"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP));// 未税单价

                checkSnglSubTab.setCntnTaxBookAmt(BigDecimal.ZERO);// 含税账面金额

                checkSnglSubTab.setUnTaxBookAmt(
                        new BigDecimal(GetCellData(r, "账面金额") == null || GetCellData(r, "账面金额").equals("") ? "0"
                                : GetCellData(r, "账面金额")).setScale(8, BigDecimal.ROUND_HALF_UP));// 未税账面金额

                checkSnglSubTab.setCntnTaxCheckAmt(BigDecimal.ZERO);// 含税盘点金额

                checkSnglSubTab.setUnTaxCheckAmt(
                        new BigDecimal(GetCellData(r, "盘点金额") == null || GetCellData(r, "盘点金额").equals("") ? "0"
                                : GetCellData(r, "盘点金额")).setScale(8, BigDecimal.ROUND_HALF_UP));// 未

                checkSnglSubTab.setCntnTaxPrftLossAmt(BigDecimal.ZERO);// 含税盈亏金额

                checkSnglSubTab.setUnTaxPrftLossAmt(
                        new BigDecimal(GetCellData(r, "盈亏金额") == null || GetCellData(r, "盈亏金额").equals("") ? "0"
                                : GetCellData(r, "盈亏金额")).setScale(8, BigDecimal.ROUND_HALF_UP));// 未税盈亏金额

                checkSnglSubTab.setCntnTaxPrftLossAmts(BigDecimal.ZERO);// 含税损益金额

                checkSnglSubTab.setUnTaxPrftLossAmts(
                        new BigDecimal(GetCellData(r, "盈亏金额") == null || GetCellData(r, "盈亏金额").equals("") ? "0"
                                : GetCellData(r, "盈亏金额")).setScale(8, BigDecimal.ROUND_HALF_UP));// 未税损益金额

                checkSnglSubTab.setGdsBitEncd(null); // 货位编码
                checkSnglSubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                checkSnglSubList.add(checkSnglSubTab);

                checkSngl.setcPrftLossList(checkSnglSubList);
                temp.put(orderNo, checkSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());

        }
        return temp;
    }
}
