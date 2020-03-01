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
    // 订单号
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
     * 仓库
     */
    @Autowired
    WhsDocMapper whsDocMapper;

    // 新增盘点单
    @Override
    public String insertCheckSngl(String userId, CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList, String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            String number = getOrderNo.getSeqNo("PD", userId, loginTime);// 获取订单号

            if (checkSnglMapper.selectCheckSngl(number) != null) {
                message = "编号" + number + "已存在，请重新输入！";
                isSuccess = false;
            } else {
                if (checkSnglMapper.selectCheckSnglSubTab(number).size() > 0) {
                    checkSnglMapper.deleteCheckSnglSubTab(number);
                }

                cSngl.setFormTypEncd("028");
                cSngl.setCheckFormNum(number);// 单据号
                checkSnglMapper.insertCheckSngl(cSngl);
                for (CheckSnglSubTab cSubTab : cSubTabList) {
                    cSubTab.setCheckFormNum(cSngl.getCheckFormNum());// 主单据号
                    cSubTab.setInvldtnDt((cSubTab.getInvldtnDt() == null || cSubTab.getInvldtnDt().equals("")) ? null
                            : cSubTab.getInvldtnDt());
                    cSubTab.setPrdcDt((cSubTab.getPrdcDt() == null || cSubTab.getPrdcDt().equals("")) ? null
                            : cSubTab.getPrdcDt());

                    InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                            .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd() + "该存货不存在"));
                    Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
                    if (sisQuaGuaPer == 1) {
                        if (StringUtils.isBlank(cSubTab.getBaoZhiQi()) || StringUtils.isBlank(cSubTab.getPrdcDt())
                                || StringUtils.isBlank(cSubTab.getInvldtnDt())) {
                            throw new RuntimeException(cSubTab.getInvtyEncd() + "保质期管理存货日期设置错误");
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
            message = "新增成功！";
            isSuccess = true;
            resp = BaseJson.returnRespObj("whs/check_sngl/insertCheckSngl", isSuccess, message, cSngl);
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
        return resp;
    }

    // 修改盘点单
    @Override
    public String updateCheckSngl(CheckSngl cSngl, List<CheckSnglSubTab> cSubTabList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(cSngl.getCheckFormNum());
        if (checkSngl == null) {
            throw new RuntimeException("单号不存在");
        } else if (checkSngl.getIsNtChk() == null) {
            throw new RuntimeException("单号审核状态异常");
        } else if (checkSngl.getIsNtChk() == 1) {
            throw new RuntimeException("单号已审核不允许修改");
        }
        for (CheckSnglSubTab cSubTab : cSubTabList) {
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                    .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd() + "该存货不存在"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
            if (sisQuaGuaPer == 1) {
                if (StringUtils.isBlank(cSubTab.getBaoZhiQi()) || StringUtils.isBlank(cSubTab.getPrdcDt())
                        || StringUtils.isBlank(cSubTab.getInvldtnDt())) {
                    throw new RuntimeException(cSubTab.getInvtyEncd() + "保质期管理存货日期设置错误");
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
        message = "更新成功！";

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/updateCheckSngl", isSuccess, message, cSngl);
        } catch (IOException e) {

        }
        return resp;
    }

    // 批量删除
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
                message = "删除成功！" + lists.toString();
                isSuccess = true;
            } else if (lists2.size() > 0) {
                isSuccess = false;
                message = message + "\r编号已审：" + lists2;
            }

            if (lists3.size() > 0) {
                message = message + "\r编号不存在：" + lists3;
            }
        } else {
            isSuccess = false;
            message = "编号" + checkFormNum + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/deleteAllCheckSngl", isSuccess, message, null);
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

    // 查询、分页查
    @Override
    public String query(String checkFormNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<CheckSnglSubTab> cSubTabList = new ArrayList<>();
        CheckSngl checkSngl = checkSnglMapper.selectCheckSngl(checkFormNum);
        if (checkSngl != null) {
            cSubTabList = checkSnglMapper.selectCheckSnglSubTab(checkFormNum);
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + checkFormNum + "不存在！";
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
            resp = BaseJson.returnRespList("whs/check_sngl/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, aList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 通过仓库、盘点批号、存货大类编码、账面为零时是否盘点 列表
    @Override
    public String selectCheckSnglList(Map map) throws IOException {
        String resp = "";

//        map.put("invtyClsEncd", getList((String) map.get("invtyClsEncd")));
        map.put("invtyEncd", getList((String) map.get("invtyEncd")));
//        map.put("regnEncd", getList((String) map.get("regnEncd")));// 区域编码
//        map.put("gdsBitEncd", getList((String) map.get("gdsBitEncd")));// 货位编码
        map.put("batNum", getList((String) map.get("batNum")));
        String Qty = map.get("Qty").toString();
        if (isNtPrgrGdsBitMgmt(map.get("whsEncd").toString())) {
            // 货位
            List<CheckSngl> cList = checkSnglMapper.selectCheckSnglListZero(map);
            if (cList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", true, "处理成功！", null, cList);
            } else {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", false, "处理失败！存货不存在", null,
                        cList);
            }
        } else {
            // 仓库
            List<CheckSngl> cList = checkSnglMapper.selectCheckSnglList(map);
            if (cList.size() > 0) {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", true, "处理成功！", null, cList);
            } else {
                resp = BaseJson.returnRespObjList("whs/check_sngl/selectCheckSnglList", false, "处理失败！存货不存在", null,
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

    // PDA 查询返回参数
    @Override
    public String checkSnglList(String whsEncd) throws IOException {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<CheckSngl> cList = checkSnglMapper.checkSnglList(list);
        if (cList.size() > 0) {
            resp = BaseJson.returnRespObjList("whs/check_sngl/checkSnglList", true, "处理成功！", null, cList);
        } else {
            resp = BaseJson.returnRespObjList("whs/check_sngl/checkSnglList", false, "处理失败！", null, cList);
        }

        return resp;
    }

    // pda 根据子表序号
    @Override
    public String updateCheckTab(CheckSngl checkSngl, List<CheckSnglSubTab> cSubTabList) throws IOException {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        if (checkSngl.getCheckFormNum() == null) {
            isSuccess = false;
            message = "盘点单据号不存在！";
        } else {
            // 修改盘点状态(盘点中、盘点完成)
            checkSngl.setCheckStatus("盘点完成");
            checkSnglMapper.updateCheckStatus(checkSngl);
            // 修改实盘数
            checkSnglMapper.updateCheckTab(cSubTabList);
            message = "处理成功！";

        }

        resp = BaseJson.returnRespObj("whs/check_sngl/updateCheckTab", isSuccess, message, null);

        return resp;
    }

    // 审核
    @Override
    public String updateCSnglChk(String userId, String jsonBody, String userName, String formDate) {
        String message = "";
        String loginTime = formDate;
        ObjectNode dbzhu = null;
        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);

            String danHao = dbzhu.get("formNum").asText();
            String chkr = userName;// 审核人

            List<CheckSngl> cSnglList = new ArrayList<>();// 盘点主
            CheckPrftLoss checkPrftLoss = new CheckPrftLoss();// 盘点损益主
            List<CheckSnglSubTab> cSubTabsList;// 盘点子
            List<CheckPrftLossSubTab> cSubLossList = new ArrayList<>();// 盘点损益子
            List<InvtyTab> invtyTabs = new ArrayList<>();// 库存表list

            CheckSngl cSngl = checkSnglMapper.selectCheckSngl(danHao);
            if (cSngl == null) {
                throw new RuntimeException("单据:" + danHao + "不存在 ");

            }
            cSngl.setChkr(chkr);
            cSngl.setChkTm(loginTime);
            cSnglList.add(cSngl);
            if (cSngl.getIsNtChk() == 1) {
                throw new RuntimeException("单号：" + danHao + "已审核，不需要再次审核");

            }
            int count = checkSnglMapper.updateCSnglChk(cSnglList);
            if (count <= 0) {
                throw new RuntimeException("单号：" + danHao + "审核失败！");
            }
            cSubTabsList = checkSnglMapper.selectCheckQty(danHao);
            if (cSubTabsList.size() == 0) {
                throw new RuntimeException("单号：" + danHao + "没有表体信息");
            }
            for (CheckSnglSubTab cSubTab : cSubTabsList) {
                // 判断实盘数量是否都填写完整
                if (cSubTab.getCheckQty() != null) {
                    if (cSubTab.getPrftLossQty().compareTo(BigDecimal.ZERO) != 0) {
                        CheckPrftLossSubTab cPrftLossSubTab = new CheckPrftLossSubTab();
//						cPrftLossSubTab.setCheckFormNum(number);// 盘点损益单号
                        cPrftLossSubTab.setInvtyEncd(cSubTab.getInvtyEncd());// 存货编码
                        cPrftLossSubTab.setInvtyBigClsEncd(cSubTab.getInvtyBigClsEncd());// 存货大类编码
                        cPrftLossSubTab.setBatNum(cSubTab.getBatNum());// 批号
                        cPrftLossSubTab.setPrdcDt(cSubTab.getPrdcDt());// 生产日期
                        cPrftLossSubTab.setBaoZhiQi(cSubTab.getBaoZhiQi());// 保质期
                        cPrftLossSubTab.setInvldtnDt(cSubTab.getInvldtnDt());// 失效日期
                        cPrftLossSubTab.setBookQty(cSubTab.getBookQty());// 账面数量
                        cPrftLossSubTab.setCheckQty(cSubTab.getCheckQty());// 盘点数量
                        // 有问题 盈亏数没有区分
                        cPrftLossSubTab.setPrftLossQty(cSubTab.getPrftLossQty());// 盈亏数量
                        cPrftLossSubTab.setPrftLossQtys(cSubTab.getPrftLossQty());// 损益数量
                        cPrftLossSubTab.setBookAdjustQty(cSubTab.getBookAdjustQty());// 账面调节数量
                        cPrftLossSubTab.setAdjIntoWhsQty(cSubTab.getAdjIntoWhsQty());// 调整入库数量
                        cPrftLossSubTab.setPrftLossRatio(cSubTab.getPrftLossRatio());// 盈亏调节比例%
                        cPrftLossSubTab.setAdjOutWhsQty(cSubTab.getAdjOutWhsQty());// 调整出库数量
                        cPrftLossSubTab.setReasn(cSubTab.getReasn());// 原因
                        cPrftLossSubTab.setGdsBitEncd(cSubTab.getGdsBitEncd());// 货位
                        cPrftLossSubTab.setProjEncd(cSubTab.getProjEncd());

                        InvtyTab invtyTab = new InvtyTab();
                        invtyTab.setWhsEncd(cSngl.getWhsEncd());
                        // 取库存表中的税率和无税单价
                        invtyTab.setInvtyEncd(cSubTab.getInvtyEncd());// 存货编
                        invtyTab.setBatNum(cSubTab.getBatNum());// 批号
                        InvtyTab aTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);
                        if (aTab == null) {
                            throw new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd() + ",存货："
                                    + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存不存在,审核失败！");
                        }
//						Map<String, Object> map = new HashMap<>();
//						map.put("invtyEncd", cPrftLossSubTab.getInvtyEncd());
//						map.put("whsEncd", checkPrftLoss.getWhsEncd());
//						map.put("batNum", cPrftLossSubTab.getBatNum());
//
//						BigDecimal unTaxUprc = cPrftLossSubTab.getUnTaxUprc();
//
//							// 获取未税单价
//							unTaxUprc = intoWhsDao.selectUnTaxUprc(map);
//							if (unTaxUprc == null) {// 无采购成本无生成无库单价
//								// 存货档案
//								InvtyDoc doc = invtyDocDao.selectInvtyDocByInvtyDocEncd(cPrftLossSubTab.getInvtyEncd());
//								if (doc != null && doc.getRefCost() != null) {
//									unTaxUprc = doc.getRefCost();
//								} else {
//
//								}
//
//							}

//						cPrftLossSubTab.setUnTaxUprc(unTaxUprc== null ? new BigDecimal(0) : unTaxUprc);// 未税单价
                        cPrftLossSubTab.setUnTaxUprc(aTab.getUnTaxUprc());// 未税单价

//						cPrftLossSubTab.setTaxRate(aTab.getTaxRate() == null ? new BigDecimal(0) : aTab.getTaxRate());// 税率
                        BigDecimal unTaxUprc; // 无税单价
                        BigDecimal checkQtyg; // 盘点数量
                        BigDecimal bookQtyg; //  账面数量
                        BigDecimal prftLossQtyg; // 盈亏数量 损益数量
                        BigDecimal taxRate; // 税率
                        // 获取未税单价
                        unTaxUprc = cPrftLossSubTab.getUnTaxUprc();
                        if (unTaxUprc == null) {
                            throw new RuntimeException(cSngl.getWhsEncd() + "仓库" + cSubTab.getInvtyEncd() + "存货" + cSubTab.getBatNum() + "批次无税单价为空");
                        }
                        InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd());
                        if (invtyDoc == null) {
                            throw new RuntimeException("存货档案中不存在：" + invtyTab.getInvtyEncd());
                        }
                        //库存都取进项税率
                        if (invtyDoc.getIptaxRate() == null) {
                            throw new RuntimeException(cSubTab.getInvtyEncd() + "存货档案中税率为空");
                        }
                        cPrftLossSubTab.setTaxRate(invtyDoc.getIptaxRate());

                        // 获取未税数量
                        checkQtyg = cPrftLossSubTab.getCheckQty();
                        bookQtyg = cPrftLossSubTab.getBookQty();
//						bookQtyg=cPrftLossSubTab.getBookAdjustQty();
                        prftLossQtyg = cPrftLossSubTab.getPrftLossQty();
                        // 获取税率 页面税率未整数，我们需要将税率/100
//                        if (cPrftLossSubTab.getTaxRate() == null) {
//                            cPrftLossSubTab.setTaxRate(BigDecimal.ONE);
//                        }
                        taxRate = cPrftLossSubTab.getTaxRate().divide(new BigDecimal(100));
//						// 计算含税单价 含税单价=无税单价*税率+无税单价
//						cPrftLossSubTab.setCntnTaxUprc(CalcAmt.cntnTaxUprc(unTaxUprc, checkQtyg, taxRate));
//						// 未税账面金额 未税金额 =未税数量*未税单价
//						cPrftLossSubTab.setUnTaxBookAmt(CalcAmt.noTaxAmt(unTaxUprc, bookQtyg));
//						// 含税账面金额 含税金额=无税金额*税率+无税金额=税额+无税金额
//						cPrftLossSubTab.setCntnTaxBookAmt(CalcAmt.prcTaxSum(unTaxUprc, bookQtyg, taxRate));
//						// 未税盘点金额
//						cPrftLossSubTab.setUnTaxCheckAmt(CalcAmt.noTaxAmt(unTaxUprc, checkQtyg));
//						// 含税盘点金额
//						cPrftLossSubTab.setCntnTaxCheckAmt(CalcAmt.prcTaxSum(unTaxUprc, checkQtyg, taxRate));
//						// 未税盈亏金额
//						cPrftLossSubTab.setUnTaxPrftLossAmt(CalcAmt.noTaxAmt(unTaxUprc, prftLossQtyg));
//						// 含税盈亏金额
//						cPrftLossSubTab.setCntnTaxPrftLossAmt(CalcAmt.prcTaxSum(unTaxUprc, prftLossQtyg, taxRate));
//						// 未税损益金额
//						cPrftLossSubTab.setUnTaxPrftLossAmts(CalcAmt.noTaxAmt(unTaxUprc, prftLossQtysg));
//						// 含税损益金额
//						cPrftLossSubTab.setCntnTaxPrftLossAmts(CalcAmt.prcTaxSum(unTaxUprc, prftLossQtysg, taxRate));
                        // 计算含税单价 含税单价（不可编辑）=无税单价*（1+税率）
//                        cPrftLossSubTab.setCntnTaxUprc((BigDecimal.ONE.add(taxRate)).multiply(unTaxUprc));
//                        // 未税账面金额 无税金额（不可编辑）=无税单价*数量
//                        cPrftLossSubTab.setUnTaxBookAmt(unTaxUprc.multiply(bookQtyg));
//                        // 含税账面金额 价税合计（不可编辑）=含税单价*数量
//                        cPrftLossSubTab.setCntnTaxBookAmt(cPrftLossSubTab.getCntnTaxUprc().multiply(bookQtyg));
//                        // 未税盘点金额
//                        cPrftLossSubTab.setUnTaxCheckAmt(unTaxUprc.multiply(checkQtyg));
//                        // 含税盘点金额
//                        cPrftLossSubTab.setCntnTaxCheckAmt(cPrftLossSubTab.getCntnTaxUprc().multiply(checkQtyg));
//                        // 未税盈亏金额
//                        cPrftLossSubTab.setUnTaxPrftLossAmt(unTaxUprc.multiply(prftLossQtyg));
//                        // 含税盈亏金额
//                        cPrftLossSubTab.setCntnTaxPrftLossAmt(cPrftLossSubTab.getCntnTaxUprc().multiply(prftLossQtyg));
//                        // 未税损益金额
//                        cPrftLossSubTab.setUnTaxPrftLossAmts(unTaxUprc.multiply(prftLossQtysg));
//                        // 含税损益金额
//                        cPrftLossSubTab.setCntnTaxPrftLossAmts(cPrftLossSubTab.getCntnTaxUprc().multiply(prftLossQtysg));

//                        cPrftLossSubTab.setCntnTaxUprc(unTaxUprc.multiply(taxRate).add(unTaxUprc));//含税单价

//                        cPrftLossSubTab.setUnTaxUprc();//未税单价

                        cPrftLossSubTab.setUnTaxCheckAmt(checkQtyg.multiply(unTaxUprc));//未税盘点金额
                        cPrftLossSubTab.setUnTaxBookAmt(bookQtyg.multiply(unTaxUprc));//未税账面金额
                        cPrftLossSubTab.setUnTaxPrftLossAmt(prftLossQtyg.multiply(unTaxUprc));//未税盈亏金额
                        cPrftLossSubTab.setUnTaxPrftLossAmts(prftLossQtyg.multiply(unTaxUprc));//未税损益金额

                        cPrftLossSubTab.setCheckTaxAmt(checkQtyg.multiply(unTaxUprc).multiply(taxRate));//盘点税额
                        cPrftLossSubTab.setBookTaxAmt(bookQtyg.multiply(unTaxUprc).multiply(taxRate));//账面税额
                        cPrftLossSubTab.setPrftLossTaxAmt(prftLossQtyg.multiply(unTaxUprc).multiply(taxRate));//盈亏税额
                        cPrftLossSubTab.setPrftLossTaxAmts(prftLossQtyg.multiply(unTaxUprc).multiply(taxRate));//损益税额


                        cPrftLossSubTab.setCntnTaxCheckAmt(cPrftLossSubTab.getUnTaxCheckAmt().add(cPrftLossSubTab.getCheckTaxAmt()));//含税盘点金额
                        cPrftLossSubTab.setCntnTaxBookAmt(cPrftLossSubTab.getUnTaxBookAmt().add(cPrftLossSubTab.getBookTaxAmt()));//含税账面金额
                        cPrftLossSubTab.setCntnTaxPrftLossAmt(cPrftLossSubTab.getUnTaxPrftLossAmt().add(cPrftLossSubTab.getPrftLossTaxAmt()));//含税盈亏金额
                        cPrftLossSubTab.setCntnTaxPrftLossAmts(cPrftLossSubTab.getUnTaxPrftLossAmts().add(cPrftLossSubTab.getPrftLossTaxAmts()));//含税损益金额
                        cPrftLossSubTab.setCntnTaxUprc(cPrftLossSubTab.getCntnTaxCheckAmt().divide(checkQtyg, 8, BigDecimal.ROUND_HALF_UP));//含税单价

                        cSubLossList.add(cPrftLossSubTab);

                        message = "单据:" + danHao + "审核成功！";

//					invtyTabs.add(invtyTab);

                    }
                } else {

                    throw new RuntimeException("单据:" + danHao + "实盘数量有空值,请全部填写！");

                }
            }
            if (cSubLossList.size() > 0) {
                // 生成盘点损益表
                String number = null;
                try {
                    number = getOrderNo.getSeqNo("SY", userId, loginTime);
                } catch (Exception e) {

                    throw new RuntimeException("单号获取异常");
                } // 获取订单号
                checkPrftLoss.setCheckFormNum(number);// 订单号
                checkPrftLoss.setSrcFormNum(danHao);// 来源单据号
                checkPrftLoss.setCheckDt(formDate);// 盘点日期
                checkPrftLoss.setBookDt(cSngl.getBookDt());// 账面日期
                checkPrftLoss.setWhsEncd(cSngl.getWhsEncd());// 仓库编码
                checkPrftLoss.setCheckBat(cSngl.getCheckBat());// 盘点批号
                checkPrftLoss.setMemo(cSngl.getMemo());// 备注
                checkPrftLoss.setSetupPers(userName);// 创建人
                checkPrftLoss.setFormTypEncd("029");
                checkPrftLossMapper.insertCheckSnglLoss(checkPrftLoss);// 创建盘点损益表主
                for (CheckPrftLossSubTab tab : cSubLossList) {
                    tab.setCheckFormNum(number);// 盘点损益单号
                }
                checkPrftLossMapper.insertCheckSnglLossSubTab(cSubLossList);// 创建盘点损益子
            } else {
                message = "单据:" + danHao + "实盘数与账面数一致无损益表";
            }

            return message;
        } catch (Exception e1) {

            throw new RuntimeException(e1.getMessage());
        }
    }

    // 弃审
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

        List<CheckSngl> cSnglList = new ArrayList<>();// 盘点主
        cSnglList = new ArrayList<>();// 盘点主

        String danHao = dbzhu.get("formNum").asText();

        CheckSngl checkSngl = new CheckSngl();
        List<CheckPrftLoss> count = checkSnglMapper.selectLossIsDel(danHao);
        if (count.size() > 0) {
            throw new RuntimeException("单据:" + danHao + "在盘点损益列表中未删除,请先删除");

        }
        // 已删除盘点损益中的数据，进行弃审操作

        CheckSngl cSngl = checkSnglMapper.selectCheckSngl(danHao);

        cSnglList.add(cSngl);
        if (cSngl == null) {
            throw new RuntimeException("单据:" + danHao + "不存在 ");
        }
        if (cSngl.getIsNtChk() == 0) {
            throw new RuntimeException("单据:" + danHao + "已弃审,不需要重复弃审 ");

        } else {

            int count1 = checkSnglMapper.updateCSnglNoChk(cSnglList);
            if (count1 >= 1) {
                message = "单据:" + danHao + "弃审成功！";
            } else {
                throw new RuntimeException("单据:" + danHao + "弃审失败！");
            }
        }

        return message;
    }

    // 下载之后锁定状态 不让PC段进行更改 保证数据同步
    @Override
    public String updateStatus(List<CheckSngl> cList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = checkSnglMapper.updateStatus(cList);
        if (i >= 1) {
            message = "下载锁定成功！";
            isSuccess = true;
        } else {
            message = "下载锁定失败！";
            isSuccess = false;
        }

        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/updateStatus", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 打印
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
            resp = BaseJson.returnRespObjListAnno("whs/check_sngl/queryListDaYin", true, "查询成功！", null, aList);
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getCheckFormNum() + " 请检查");
            }
            try {
                checkSnglMapper.exInsertCheckSngl(entry.getValue());
                checkSnglMapper.exInsertCheckSnglSubTab(entry.getValue().getcSubTabList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "盘点单新增成功！";
        try {
            resp = BaseJson.returnRespObj("whs/check_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // 导入excle
    private Map<String, CheckSngl> uploadScoreInfo(MultipartFile file) {
        Map<String, CheckSngl> temp = new HashMap<>();
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
                CheckSngl checkSngl = new CheckSngl();
                if (temp.containsKey(orderNo)) {
                    checkSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//				// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                checkSngl.setCheckFormNum(orderNo);// 盘点单号
                checkSngl.setFormTypEncd("028");

                checkSngl.setCheckDt(
                        GetCellData(r, "盘点日期") == null ? "" : GetCellData(r, "盘点日期"));// 盘点日期
                checkSngl.setBookDt(
                        GetCellData(r, "账面日期") == null ? "" : GetCellData(r, "账面日期"));// 账面日期q
                checkSngl.setWhsEncd(GetCellData(r, "仓库编码"));// 仓库编码
                checkSngl.setCheckBat(GetCellData(r, "盘点批号"));// 盘点批号
                checkSngl.setSetupPers(GetCellData(r, "创建人")); // 创建人q
                checkSngl.setSetupTm(
                        GetCellData(r, "创建时间") == null ? "" : GetCellData(r, "创建时间")); // 创建时间q
                checkSngl.setMdfr(GetCellData(r, "修改人")); // 修改人q
                checkSngl.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间")); // 修改时间q
                checkSngl.setChkr(GetCellData(r, "审核人")); // 审核人q
                checkSngl.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间")); // 审核时间q
                checkSngl.setMemo(GetCellData(r, "备注"));// 备注

                checkSngl.setBookEntryPers(GetCellData(r, "记账人"));// 记账人

                checkSngl.setBookEntryTm(
                        GetCellData(r, "记账时间") == null ? "" : GetCellData(r, "记账时间"));// 记账时间
//                checkSngl.setOperator(GetCellData(r, "操作员"));// 操作员
//                checkSngl.setOperatorId(GetCellData(r, "操作员编码"));// 操作员编码
                checkSngl.setCheckStatus(GetCellData(r, "盘点状态"));// 盘点状态

                checkSngl
                        .setIsNtChk(new Double(GetCellData(r, "是否审核") == null || GetCellData(r, "是否审核").equals("") ? "0"
                                : GetCellData(r, "是否审核")).intValue());// 是否审核
//				// System.out.println("checkSngl"+isNtChk);
                checkSngl.setIsNtBookEntry(
                        new Double(GetCellData(r, "是否记账") == null || GetCellData(r, "是否记账").equals("") ? "0"
                                : GetCellData(r, "是否记账")).intValue());// 是否记账
//				// System.out.println("isNtBookEntry"+isNtBookEntry);
//                checkSngl.setIsNtCmplt(
//                        new Double(GetCellData(r, "是否完成") == null || GetCellData(r, "是否完成").equals("") ? "0"
//                                : GetCellData(r, "是否完成")).intValue());// 是否完成
//				// System.out.println("isNtCmplt"+isNtCmplt);
//                checkSngl.setIsNtClos(
//                        new Double(GetCellData(r, "是否关闭") == null || GetCellData(r, "是否关闭").equals("") ? "0"
//                                : GetCellData(r, "是否关闭")).intValue());// 是否关闭
//				// System.out.println("isNtClos"+isNtClos);
//                checkSngl.setPrintCnt(
//                        new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
//                                : GetCellData(r, "打印次数")).intValue());// 打印次数q
//				checkSngl.setIsNtWms(new Double(GetCellData(r, "是否向WMS上传")).intValue());// 是否向WMS上传q
//				// System.out.println("isNtWms"+isNtWms);
                List<CheckSnglSubTab> checkSnglSubList = checkSngl.getcSubTabList();// 订单子表
                if (checkSnglSubList == null) {
                    checkSnglSubList = new ArrayList<>();
                }

                CheckSnglSubTab checkSnglSubTab = new CheckSnglSubTab();

                checkSnglSubTab.setCheckFormNum(orderNo);// 盘点单号
                checkSnglSubTab.setInvtyEncd(GetCellData(r, "存货编码")); // 存货编码1
                checkSnglSubTab.setBatNum(GetCellData(r, "批号")); // 批号1

                checkSnglSubTab.setInvtyBigClsEncd(GetCellData(r, "存货大类编码")); // 存货大类编码
                checkSnglSubTab.setBarCd(GetCellData(r, "条形码")); // 条形码1
                checkSnglSubTab.setPrdcDt(
                        GetCellData(r, "生产日期") == null ? "" : GetCellData(r, "生产日期")); // 生产日期1

//				Integer baoZhiQi = (new Double(r.getCell(29).getNumericCellValue())).intValue();
//				// System.out.println("baoZhiQi" + baoZhiQi);
                checkSnglSubTab.setBaoZhiQi(GetCellData(r, "保质期")); // 保质期1
//				// System.out.println("getCell" + r.getCell(29)); // 保质期1
//				// System.out.println("getNumericCellValue" + r.getCell(29).getNumericCellValue());
//				// System.out.println("getCellType" + r.getCell(29).getCellType()); // 保质期1

                checkSnglSubTab.setInvldtnDt(
                        GetCellData(r, "失效日期") == null ? "" : GetCellData(r, "失效日期")); // 失效日期1
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
                checkSnglSubTab.setBookAdjustQty(
                        new BigDecimal(GetCellData(r, "账面调节数量") == null || GetCellData(r, "账面调节数量").equals("") ? "0"
                                : GetCellData(r, "账面调节数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 账面调节数量?
                checkSnglSubTab.setCheckQty(
                        new BigDecimal(GetCellData(r, "盘点数量") == null || GetCellData(r, "盘点数量").equals("") ? "0"
                                : GetCellData(r, "盘点数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盘点数量1
                checkSnglSubTab.setPrftLossQty(
                        new BigDecimal(GetCellData(r, "盈亏数量") == null || GetCellData(r, "盈亏数量").equals("") ? "0"
                                : GetCellData(r, "盈亏数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盈亏数量1
                checkSnglSubTab.setPrftLossRatio(
                        new BigDecimal(GetCellData(r, "盈亏比例") == null || GetCellData(r, "盈亏比例").equals("") ? "0"
                                : GetCellData(r, "盈亏比例")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盈亏比例(%)1
                checkSnglSubTab.setReasn(GetCellData(r, "原因")); // 原因1

                checkSnglSubTab
                        .setGdsBitEncd((GetCellData(r, "货位编码") == null || GetCellData(r, "货位编码").equals("")) ? null
                                : GetCellData(r, "货位编码")); // 货位编码
                checkSnglSubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                checkSnglSubList.add(checkSnglSubTab);

                checkSngl.setcSubTabList(checkSnglSubList);
                temp.put(orderNo, checkSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());

        }
        return temp;
    }

    @Override
    public String selectCheckSnglGdsBitList(Map map) {
        String resp = "";

        // 账面数为零
        List<InvtyTab> cList = checkSnglMapper.selectCheckSnglGdsBitList(map);
        if (cList.size() > 0) {
            try {
                resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglGdsBitList", true, "成功", cList);
            } catch (IOException e) {


            }

        } else {
            try {
                resp = BaseJson.returnRespObj("whs/check_sngl/selectCheckSnglGdsBitList", false, "失败", null);
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getCheckFormNum() + " 请检查");
            }
            try {
                checkSnglMapper.exInsertCheckSngl(entry.getValue());
                checkSnglMapper.exInsertCheckSnglSubTab(entry.getValue().getcSubTabList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "盘点单新增成功！";
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
                CheckSngl checkSngl = new CheckSngl();
                if (temp.containsKey(orderNo)) {
                    checkSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//				// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                checkSngl.setCheckFormNum(orderNo);// 盘点单号
                checkSngl.setFormTypEncd("028");

                checkSngl.setCheckDt(
                        GetCellData(r, "盘点日期") == null ? "" : GetCellData(r, "盘点日期").replaceAll("[^0-9:-]", " "));// 盘点日期
                checkSngl.setBookDt(
                        GetCellData(r, "盘点日期") == null ? "" : GetCellData(r, "盘点日期").replaceAll("[^0-9:-]", " "));// 账面日期q
                checkSngl.setWhsEncd(GetCellData(r, "盘点仓库编码"));// 仓库编码
                checkSngl.setCheckBat(GetCellData(r, "盘点批号"));// 盘点批号
                checkSngl.setSetupPers(GetCellData(r, "制单人")); // 创建人q
                checkSngl.setSetupTm(
                        GetCellData(r, "制单时间") == null ? "" : GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " ")); // 创建时间q
                checkSngl.setMdfr(GetCellData(r, "修改人")); // 修改人q
                checkSngl.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ")); // 修改时间q
                checkSngl.setChkr(GetCellData(r, "审核人")); // 审核人q
                checkSngl.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " ")); // 审核时间q
                checkSngl.setMemo(GetCellData(r, "备注"));// 备注

                checkSngl.setBookEntryPers(null);// 记账人

                checkSngl.setBookEntryTm(null);// 记账时间
                checkSngl.setOperator(null);// 操作员
                checkSngl.setOperatorId(null);// 操作员编码
                checkSngl.setCheckStatus("盘点中");// 盘点状态
                checkSngl.setIsNtChk(0);// 是否审核
                checkSngl.setIsNtBookEntry(0);// 是否记账
                checkSngl.setIsNtCmplt(0);// 是否完成
                checkSngl.setIsNtClos(0);// 是否关闭
                checkSngl.setPrintCnt(
                        new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
                                : GetCellData(r, "打印次数")).intValue());// 打印次数q
                checkSngl.setIsNtWms(0);// 是否向WMS上传q
//				// System.out.println("isNtWms"+isNtWms);
                List<CheckSnglSubTab> checkSnglSubList = checkSngl.getcSubTabList();// 订单子表
                if (checkSnglSubList == null) {
                    checkSnglSubList = new ArrayList<>();
                }

                CheckSnglSubTab checkSnglSubTab = new CheckSnglSubTab();

                checkSnglSubTab.setCheckFormNum(orderNo);// 盘点单号
                checkSnglSubTab.setInvtyEncd(GetCellData(r, "存货编码")); // 存货编码
                checkSnglSubTab.setBatNum(GetCellData(r, "批号")); // 批号

                checkSnglSubTab.setInvtyBigClsEncd(null); // 存货大类编码
                checkSnglSubTab.setBarCd(GetCellData(r, "条形码")); // 条形码1
                checkSnglSubTab.setPrdcDt(
                        GetCellData(r, "生产日期") == null ? "" : GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " ")); // 生产日期1
                String BaoZhiQi = GetCellData(r, "保质期");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                }
                checkSnglSubTab.setBaoZhiQi(BaoZhiQi); // 保质期1

                checkSnglSubTab.setInvldtnDt(
                        GetCellData(r, "失效日期") == null ? "" : GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " ")); // 失效日期1
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
                checkSnglSubTab.setPrftLossRatio(
                        new BigDecimal(GetCellData(r, "盈亏比例%") == null || GetCellData(r, "盈亏比例%").equals("") ? "0"
                                : GetCellData(r, "盈亏比例%")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 盈亏比例(%)1
                checkSnglSubTab.setReasn(GetCellData(r, "原因")); // 原因1

                checkSnglSubTab.setGdsBitEncd(null); // 货位编码
                checkSnglSubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                checkSnglSubList.add(checkSnglSubTab);

                checkSngl.setcSubTabList(checkSnglSubList);
                temp.put(orderNo, checkSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());

        }
        return temp;
    }

}