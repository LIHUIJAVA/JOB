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
 * @version 创建时间：2018年11月15日 下午2:21:54
 * @ClassName 类名称
 * @Description 类描述
 */
@Service
@Transactional
public class CannibSnglServiceImpl extends poiTool implements CannibSnglService {

    @Autowired
    CannibSnglMapper cannibSnglMapper;
    // 订单号
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
    // 货位
    @Autowired
    InvtyGdsBitListMapper bitListMapper;

    // 添加调拨单
    @Override
    public String insertCSngl(String userId, CannibSngl cSngl, List<CannibSnglSubTab> cList, String loginTime) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        String number = null;
        try {
            number = getOrderNo.getSeqNo("DB", userId, loginTime);// 获取订单号
        } catch (Exception e) {

            throw new RuntimeException("获取订单号出错");
        }

        if (cannibSnglMapper.selectCSngl(number) != null) {
            message = "编号" + number + "已存在，请重新输入！";
            isSuccess = false;

            throw new RuntimeException(message);

        } else {
            if (cannibSnglMapper.selectCSnglSubTabList(number).size() > 0) {
                cannibSnglMapper.deleteCSnglSubTab(number);
            }
            cSngl.setFormTypEncd("011");
            cSngl.setFormNum(number);// 订单号
            cSngl.setCannibDt(cSngl.getFormDt());

            cannibSnglMapper.insertCSngl(cSngl);// 新增主表
            for (CannibSnglSubTab cSubTab : cList) {
                cSubTab.setFormNum(cSngl.getFormNum());
                InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                        .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd()+"该存货不存在"));
                Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
                if(sisQuaGuaPer == 1){
                    if(StringUtils.isBlank(cSubTab.getBaoZhiQi())|| StringUtils.isBlank(cSubTab.getPrdcDt())
                            || StringUtils.isBlank(cSubTab.getInvldtnDt()) ){
                        throw new RuntimeException(cSubTab.getInvtyEncd()+"保质期管理存货日期设置错误");
                    }
                }else{
                    cSubTab.setBaoZhiQi(null);
                    cSubTab.setPrdcDt(null);
                    cSubTab.setInvldtnDt(null);
                }
            }
            cannibSnglMapper.insertCSnglSubTab(cList);// 新增子表

            isSuccess = true;
            message = "新增成功！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/insertCSngl", isSuccess, message, cSngl);
        } catch (IOException e) {

        }

        return resp;
    }

    // 修改调拨单
    @Override
    public String updateCSngl(CannibSngl cSngl, List<CannibSnglSubTab> cList) {

        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(cSngl.getFormNum());
        if (cannibSngl.getIsNtChk() == null) {
            throw new RuntimeException("单号审核状态异常");
        } else if (cannibSngl.getIsNtChk() == 1) {
            throw new RuntimeException("单号已审核不允许修改");
        }

        int i = cannibSnglMapper.deleteCSnglSubTab(cSngl.getFormNum());

        cSngl.setFormDt(cSngl.getCannibDt());

        for (CannibSnglSubTab cSubTab : cList) {
            cSubTab.setFormNum(cSngl.getFormNum());
            InvtyDoc sinvty = Optional.ofNullable(invtyDocDao.selectInvtyDocByInvtyDocEncd(cSubTab.getInvtyEncd()))
                    .orElseThrow(() -> new RuntimeException(cSubTab.getInvtyEncd()+"该存货不存在"));
            Integer sisQuaGuaPer = Optional.ofNullable(sinvty.getIsQuaGuaPer()).orElseThrow(() -> new RuntimeException("是否保质期管理属于为空"));
            if(sisQuaGuaPer == 1){
                if(StringUtils.isBlank(cSubTab.getBaoZhiQi())|| StringUtils.isBlank(cSubTab.getPrdcDt())
                        || StringUtils.isBlank(cSubTab.getInvldtnDt()) ){
                    throw new RuntimeException(cSubTab.getInvtyEncd()+"保质期管理存货日期设置错误");
                }
            }else{
                cSubTab.setBaoZhiQi(null);
                cSubTab.setPrdcDt(null);
                cSubTab.setInvldtnDt(null);
            }
        }

        cannibSnglMapper.updateCSngl(cSngl);
        cannibSnglMapper.insertCSnglSubTab(cList);
        message = "更新成功！";

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngl", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 批量删除
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
                message = "删除成功！" + lists.toString();
                if (lists2.size() > 0) {
                    message = message + "\r编号已审：" + lists2;
                }
            } else {
                message = "编号已审：" + lists2;

            }
        } else {
            isSuccess = false;
            message = "编号" + formNum + "不存在！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/deleteAllCSngl", isSuccess, message, null);
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
    public String query(String formNum) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<CannibSnglSubTab> cSubTabList = null;
        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(formNum);
        if (cannibSngl != null) {
            cSubTabList = cannibSnglMapper.selectCSnglSubTabList(formNum);
            message = "查询成功！";
        } else {
            isSuccess = false;
            message = "编号" + formNum + "不存在！";
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
            resp = BaseJson.returnRespList("whs/cannib_sngl/queryList", true, "查询成功！", count, pageNo, pageSize, listNum,
                    pages, aList);

        } catch (IOException e) {

        }
        return resp;
    }

    // PDA 查询
    @Override
    public String selectCSnglChkr() {
        String resp = "";
        List<CannibSngl> csChkrList = cannibSnglMapper.selectCSnglChkr();
        try {
            resp = BaseJson.returnRespObjList("whs/cannib_sngl/selectCSnglChkr", true, "查询成功！", null, csChkrList);
        } catch (IOException e) {

        }
        return resp;
    }

    // 修改子表
    @Override
    public String updateCSnglSubTab(CannibSnglSubTab cSubTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = cannibSnglMapper.updateCSnglSubTab(cSubTab);
        if (i >= 1) {
            message = "修改成功！";
            isSuccess = true;
        } else {
            message = "修改失败！";
            isSuccess = true;
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSnglSubTab", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 修改主表
    @Override
    public String updateCSngl(CannibSngl cSngl) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        int i = cannibSnglMapper.updateCSngl(cSngl);
        if (i >= 1) {
            message = "修改成功！";
            isSuccess = true;
        } else {
            message = "修改失败！";
            isSuccess = true;
        }

        try {
            resp = BaseJson.returnRespObj("whs/cannib_sngl/updateCSngl", isSuccess, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 调拨单审核
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

        List<CannibSngl> cannibSngls = new ArrayList<>();// 调拨主表list
        List<CannibSnglSubTab> cSnglSubTabs = new ArrayList<>();// 调拨子表list
        List<InvtyTab> invtyTabs = new ArrayList<>();// 库存表list
        OthOutIntoWhs othOutWhs = new OthOutIntoWhs();// 其他出库主表
        OthOutIntoWhs othIntoWhs = new OthOutIntoWhs();// 其他入库主表
        List<OthOutIntoWhsSubTab> othOutWhsSubTabs = new ArrayList<>();// 其他出库子
        List<OthOutIntoWhsSubTab> othIntoWhsSubTabs = new ArrayList<>();// 其他入库子

        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(danHao);

        if (cannibSngl == null) {
            throw new RuntimeException("单据:" + danHao + "不存在 ");
        }
        if (cannibSngl.getIsNtChk() == 1) {
            throw new RuntimeException("单号：" + danHao + "已审核，不需要再次审核");
        }

        cannibSngl.setChkr(userName);
        cannibSngl.setChkTm(loginTime);
        cannibSngls.add(cannibSngl);

        int a = cannibSnglMapper.updateCSnglChk(cannibSngls);// 修改审核状态

        if (a >= 1) {
            cSnglSubTabs = cannibSnglMapper.selectCSnglSubTabList(danHao);
            if (cSnglSubTabs.size() == 0) {
                throw new RuntimeException("单号：" + danHao + "没有表体信息");
            }
            // 出库主=======
            String number = null;
            try {
                number = getOrderNo.getSeqNo("OTCR", userId, loginTime);
            } catch (Exception e) {

                throw new RuntimeException("获取单号失败");
            } // 获取订单号
            othOutWhs.setFormNum(number);// 订单号
            othOutWhs.setFormDt(formDate);
            othOutWhs.setOutIntoWhsTypId("2");// 调拨出库类型
            othOutWhs.setOutStatus("处理中");// 出库状态
            othOutWhs.setSetupPers(userName);// 创建人
            othOutWhs.setFormTypEncd("015");
            othOutWhs.setWhsEncd(cannibSngl.getTranOutWhsEncd());// 转出仓库编码
            othOutWhs.setSrcFormNum(cannibSngl.getFormNum());// 来源单据号
            othOutWhs.setMemo(cannibSngl.getMemo());// 备注

            // 入库主=======
            String number2 = null;
            try {
                number2 = getOrderNo.getSeqNo("OTCR", userId, loginTime);
            } catch (Exception e) {

                throw new RuntimeException("获取单号失败");

            } // 获取订单号
            othIntoWhs.setFormNum(number2);// 订单号
            othIntoWhs.setFormDt(formDate);
            othIntoWhs.setOutIntoWhsTypId("1");// 调拨入库类型
            othIntoWhs.setInStatus("处理中");// 入库状态
            othIntoWhs.setSetupPers(userName);// 创建人
            othIntoWhs.setFormTypEncd("014");
            othIntoWhs.setWhsEncd(cannibSngl.getTranInWhsEncd());// 转入仓库编码
            othIntoWhs.setSrcFormNum(cannibSngl.getFormNum());// 来源单据号
            othIntoWhs.setMemo(cannibSngl.getMemo());// 备注

            for (CannibSnglSubTab tabs : cSnglSubTabs) {
                // 库存表
                InvtyTab invtyTab = new InvtyTab();
                invtyTab.setWhsEncd(cannibSngl.getTranOutWhsEncd());// 仓库编码
                invtyTab.setInvtyEncd(tabs.getInvtyId());// 存货编码
                invtyTab.setBatNum(tabs.getBatNum());// 批号
                invtyTab.setAvalQty(tabs.getCannibQty());// 可用量

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

                // 入库子
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

                Optional.ofNullable(inTab).orElseThrow(() -> new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd()
                        + ",存货：" + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存不存在,审核失败！"));

                if (inTab.getAvalQty().compareTo(tabs.getCannibQty()) > -1) {
                    invtyNumMapper.updateAInvtyTab(Arrays.asList(invtyTab));// 修改库存表调拨出
                    message = "单据:" + danHao + "审核成功！";
                } else {
                    throw new RuntimeException("单据：" + danHao + "中,仓库：" + invtyTab.getWhsEncd() + ",存货："
                            + invtyTab.getInvtyEncd() + ",批号：" + invtyTab.getBatNum() + "对应的库存数量不足,审核失败！");
                }

            }
            othOutIntoWhsMapper.insertOthOutIntoWhs(othIntoWhs);// 其他出入库主
            othOutIntoWhsMapper.insertOthOutIntoWhs(othOutWhs);

            othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othIntoWhsSubTabs);// 其他出入入库子
            othOutIntoWhsMapper.insertOthOutIntoWhsSubTab(othOutWhsSubTabs);

        } else {
            throw new RuntimeException("单号：" + danHao + "审核失败！");
        }

        return message;
    }

    // 调拨单 弃审
    @Override
    public String updateCSnglNoChk(String userId, String jsonBody, String userName) {
        String message = "";

        ObjectNode dbzhu = null;

        try {
            dbzhu = JacksonUtil.getObjectNode(jsonBody);
        } catch (IOException e1) {

        }
        String danHao = dbzhu.get("formNum").asText();
        String chkr = userName;// 审核人
        List<CannibSngl> cannibSngls = new ArrayList<>();// 调拨主表list
        List<CannibSnglSubTab> cannibSnglSubTab = new ArrayList<>();// 调拨子

        CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(danHao);
        if (cannibSngl == null) {
            throw new RuntimeException("单据:" + danHao + "不存在 ");
        }
        // 调拨主
        cannibSngl.setChkr(chkr);// 审核人
        cannibSngls.add(cannibSngl);


        if (cannibSngl.getIsNtChk() == 0) {
            return message = "单据:" + danHao + "已弃审,不需要重复弃审 ";
        } else {
            // 查询是否有其他出入库
            OthOutIntoWhs intoWhs1 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(danHao, "1");
            OthOutIntoWhs intoWhs2 = othOutIntoWhsMapper.selectOthOutIntoWhsSrc(danHao, "2");
            if (intoWhs1 == null || intoWhs2 == null) {
                throw new RuntimeException("单号：" + danHao + "对应的其他出入库存在异常");
            }
            // 删除其他出入库的数据，进行弃审操作
            if (intoWhs1.getIsNtChk() == 1 || intoWhs2.getIsNtChk() == 1) {
                throw new RuntimeException("单号：" + danHao + "对应的其他出入库已审核");
            } else {
                List<String> lists = Arrays.asList(intoWhs1.getFormNum(), intoWhs2.getFormNum());
                othOutIntoWhsMapper.insertOthOutIntoWhsDl(lists);
                othOutIntoWhsMapper.insertOthOutIntoWhsSubTabDl(lists);
                othOutIntoWhsMapper.deleteAllOthOutIntoWhs(lists);
                bitListMapper.deleteInvtyGdsBitList(lists);
            }

            int a = cannibSnglMapper.updateCSnglNoChk(cannibSngls);// 修改弃审状态
            if (a >= 1) {

                cannibSnglSubTab = cannibSnglMapper.selectCSnglSubTabList(danHao);// 调拨子
                if (cannibSnglSubTab.size() == 0) {
                    throw new RuntimeException("单号：" + danHao + "没有表体信息");
                }
                for (CannibSnglSubTab dbzi : cannibSnglSubTab) {
                    // 库存表
                    InvtyTab invtyTab = new InvtyTab();
                    invtyTab.setWhsEncd(cannibSngl.getTranOutWhsEncd());// 仓库编码
                    invtyTab.setInvtyEncd(dbzi.getInvtyId());// 存货编码
                    invtyTab.setBatNum(dbzi.getBatNum());// 批号
                    invtyTab.setAvalQty(dbzi.getCannibQty());// 可用量
// 					invtyTab.setNowStok(BigDecimal.ZERO);// 现存量
                    InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(invtyTab);// 转入仓库

                    if (invtyNumMapper.selectInvtyTabByTerm(invtyTab) != null) {
                        invtyNumMapper.updateAInvtyTabJia(Arrays.asList(invtyTab));// 修改库存表可用量 调拨出 增加
                    } else {
                        invtyTab.setPrdcDt(dbzi.getPrdcDt());
                        invtyTab.setBaoZhiQi(dbzi.getBaoZhiQi());
                        invtyTab.setInvldtnDt(dbzi.getInvldtnDt());
//							金额 注释不传
//							invtyTab.setCntnTaxUprc(BigDecimal.ZERO);
//							invtyTab.setUnTaxUprc(BigDecimal.ZERO);
//							invtyTab.setTaxRate(BigDecimal.ZERO);
//							invtyTab.setCntnTaxAmt(BigDecimal.ZERO);
//							invtyTab.setUnTaxAmt(BigDecimal.ZERO);
                        // 新增库存表 入库
                        invtyNumMapper.insertInvtyTabList(Arrays.asList(invtyTab));
                    }
                }
                message = "单据：" + danHao + "单据弃审成功！";
            } else {
                throw new RuntimeException("单据：" + danHao + "单据弃审失败！");
            }

        }

        return message;
    }

    // 打印
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
            resp = BaseJson.returnRespObjListAnno("whs/cannib_sngl/queryListDaYin", true, "查询成功！", null, aList);
//			resp = BaseJson.returnRespObjList("whs/cannib_sngl/queryListDaYin", true, "查询成功！", null, aList);

        } catch (IOException e) {

        }
        return resp;
    }

    // 查询库存表的现存量 可用量
    @Override
    public String selectInvty(Map map) {
        String resp = "";
        List<InvtyTab> invtyTabList = cannibSnglMapper.selectInvty(map);
        try {
            resp = BaseJson.returnRespObjList("whs/cannib_sngl/selectInvty", true, "查询成功！", null, invtyTabList);
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
                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");

            }
            try {
                cannibSnglMapper.exInsertCSngl(entry.getValue());

                cannibSnglMapper.exInsertCSnglSubTab(entry.getValue().getCheckSnglList());

            } catch (Exception e) {


                throw new RuntimeException("插入sql问题");
            }
        }

        isSuccess = true;
        message = "调拨单新增成功！";
        try {
            resp = BaseJson.returnResp("whs/cannib_sngl/uploadFileAddDb", isSuccess, message, null);
        } catch (IOException e) {


        }

        return resp;

    }

    // 导入excle
    private Map<String, CannibSngl> uploadScoreInfo(MultipartFile file) {
        Map<String, CannibSngl> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "单据号");
                // 创建实体类
                // System.out.println(orderNo);
                CannibSngl cannibSngl = new CannibSngl();
                if (temp.containsKey(orderNo)) {
                    cannibSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                cannibSngl.setFormNum(orderNo);// 单据号
                cannibSngl.setFormDt(
                        GetCellData(r, "单据日期") == null ? "" : GetCellData(r, "单据日期")); // 单据日期,q
                // datetime
                cannibSngl.setCannibDt(cannibSngl.getFormDt());
//                        GetCellData(r, "调拨日期") == null ? "" : GetCellData(r, "调拨日期")); // 调拨日期,a
                // datetime
                cannibSngl.setTranOutWhsEncd(GetCellData(r, "转出仓库编码")); // 转出仓库编码,q varchar(200
                cannibSngl.setTranInWhsEncd(GetCellData(r, "转入仓库编码")); // 转入仓库编码,q varchar(200
//                cannibSngl.setTranOutDeptEncd(GetCellData(r, "转出部门编码")); // 转出部门编码,q varchar(200
//                cannibSngl.setTranInDeptEncd(GetCellData(r, "转入部门编码")); // 转入部门编码,q varchar(200
//                cannibSngl.setCannibStatus(GetCellData(r, "调拨状态")); // 调拨状态, a varchar(200
                cannibSngl.setMemo(GetCellData(r, "备注")); // 备注,q varchar(2000
                cannibSngl.setFormTypEncd("011");// 单据类型编码

//				cannibSngl.setIsNtWms(new Double(GetCellData(r, "是否向WMS上传")).intValue()); // 是否向WMS上传,a int(11
                cannibSngl.setIsNtChk(
                        (new Double(GetCellData(r, "是否审核") == null || GetCellData(r, "是否审核").equals("") ? "0"
                                : GetCellData(r, "是否审核"))).intValue()); // 是否审核(0.未审核1.已审核,a int(11
//                cannibSngl.setIsNtCmplt(
//                        (new Double(GetCellData(r, "是否完成") == null || GetCellData(r, "是否完成").equals("") ? "0"
//                                : GetCellData(r, "是否完成"))).intValue()); // 是否完成,a int(11
//                cannibSngl.setIsNtClos(
//                        (new Double(GetCellData(r, "是否关闭") == null || GetCellData(r, "是否关闭").equals("") ? "0"
//                                : GetCellData(r, "是否关闭"))).intValue()); // 是否关闭,a int(11
//                cannibSngl.setPrintCnt(
//                        (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
//                                : GetCellData(r, "打印次数"))).intValue()); // 打印次数,q int(11
                cannibSngl.setSetupPers(GetCellData(r, "创建人")); // 创建人,q varchar(200
                cannibSngl.setSetupTm(
                        GetCellData(r, "创建时间") == null ? "" : GetCellData(r, "创建时间")); // 创建时间,q
                // datetime
                cannibSngl.setMdfr(GetCellData(r, "修改人")); // 修改人,q varchar(200
                cannibSngl.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间")); // 修改时间,q
                // datetime
                cannibSngl.setChkr(GetCellData(r, "审核人")); // 审核人,q varchar(200
                cannibSngl.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间")); // 审核时间,q
                // datetime

                List<CannibSnglSubTab> cannibSnglSubList = cannibSngl.getCheckSnglList();// 订单子表
                if (cannibSnglSubList == null) {
                    cannibSnglSubList = new ArrayList<>();
                }

                CannibSnglSubTab cannibSnglSubTab = new CannibSnglSubTab();

                cannibSnglSubTab.setFormNum(orderNo); // 单据号 varchar(200
                cannibSnglSubTab.setInvtyId(GetCellData(r, "存货编码")); // 存货编号 1 varchar(200

                cannibSnglSubTab.setCannibQty(
                        new BigDecimal(GetCellData(r, "调拨数量") == null || GetCellData(r, "调拨数量").equals("") ? "0"
                                : GetCellData(r, "调拨数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 调拨数量 1 decimal(208

                cannibSnglSubTab.setRecvQty(cannibSnglSubTab.getCannibQty());
//                        new BigDecimal(GetCellData(r, "实收数量") == null || GetCellData(r, "实收数量").equals("") ? "0"
//                                : GetCellData(r, "实收数量")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 实收数量 a decimal(208
                cannibSnglSubTab.setBatNum(GetCellData(r, "批号")); // 批号 1 varchar(200
                cannibSnglSubTab.setInvldtnDt(
                        GetCellData(r, "失效日期") == null ? "" : GetCellData(r, "失效日期")); // 失效日期
                // 1
                // datetime
                cannibSnglSubTab.setBaoZhiQi(GetCellData(r, "保质期")); // 保质期 1 varchar(200
                cannibSnglSubTab.setPrdcDt(
                        GetCellData(r, "生产日期 ") == null ? "" : GetCellData(r, "生产日期")); // 生产日期
                // 1
                // datetime
                cannibSnglSubTab.setTaxRate(new BigDecimal(
                        GetCellData(r, "税率") == null || GetCellData(r, "税率").equals("") ? "0" : GetCellData(r, "税率"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 税率 a decimal(208
                cannibSnglSubTab.setCntnTaxUprc(
                        new BigDecimal(GetCellData(r, "含税单价") == null || GetCellData(r, "含税单价").equals("") ? "0"
                                : GetCellData(r, "含税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 含税单价 1 decimal(208
                cannibSnglSubTab.setUnTaxUprc(
                        new BigDecimal(GetCellData(r, "未税单价") == null || GetCellData(r, "未税单价").equals("") ? "0"
                                : GetCellData(r, "未税单价")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税单价 1 decimal(208
                cannibSnglSubTab.setCntnTaxAmt(
                        new BigDecimal(GetCellData(r, "含税金额") == null || GetCellData(r, "含税金额").equals("") ? "0"
                                : GetCellData(r, "含税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 含税金额 a decimal(208
                cannibSnglSubTab.setUnTaxAmt(
                        new BigDecimal(GetCellData(r, "未税金额") == null || GetCellData(r, "未税金额").equals("") ? "0"
                                : GetCellData(r, "未税金额")).setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税金额 a decimal(208
                cannibSnglSubTab.setBxQty(new BigDecimal(
                        GetCellData(r, "箱数") == null || GetCellData(r, "箱数").equals("") ? "0" : GetCellData(r, "箱数"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 箱数 a decimal(208
                cannibSnglSubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                cannibSnglSubList.add(cannibSnglSubTab);

                cannibSngl.setCheckSnglList(cannibSnglSubList);
                temp.put(orderNo, cannibSngl);

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
        Map<String, CannibSngl> pusOrderMap = uploadScoreInfoU8(file);

        for (Map.Entry<String, CannibSngl> entry : pusOrderMap.entrySet()) {

            CannibSngl cannibSngl = cannibSnglMapper.selectCSngl(entry.getValue().getFormNum());

            if (cannibSngl != null) {
                throw new RuntimeException("插入重复单号 " + entry.getValue().getFormNum() + " 请检查");

            }
            try {
                cannibSnglMapper.exInsertCSngl(entry.getValue());

                cannibSnglMapper.exInsertCSnglSubTab(entry.getValue().getCheckSnglList());

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

    private Map<String, CannibSngl> uploadScoreInfoU8(MultipartFile file) {
        Map<String, CannibSngl> temp = new HashMap<>();
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
                String orderNo = GetCellData(r, "单据号");
                // 创建实体类
                // System.out.println(orderNo);
                CannibSngl cannibSngl = new CannibSngl();
                if (temp.containsKey(orderNo)) {
                    cannibSngl = temp.get(orderNo);
                }
                // r.getCell(0).setCellType(Cell.CELL_TYPE_NUMERIC);
//					// System.out.println(r.getCell(0));
                // 取出当前行第1个单元格数据，并封装在pursOrdr实体的各个属性上
                cannibSngl.setFormNum(orderNo);// 单据号
                cannibSngl.setFormDt(
                        GetCellData(r, "日期") == null ? "" : GetCellData(r, "日期").replaceAll("[^0-9:-]", " ")); // 单据日期,q
                // datetime
                cannibSngl.setCannibDt(
                        GetCellData(r, "日期") == null ? "" : GetCellData(r, "日期").replaceAll("[^0-9:-]", " ")); // 调拨日期,a
                // datetime
                cannibSngl.setTranOutWhsEncd(GetCellData(r, "转出仓库编码")); // 转出仓库编码
                cannibSngl.setTranInWhsEncd(GetCellData(r, "转入仓库编码")); // 转入仓库编码
                cannibSngl.setTranOutDeptEncd(GetCellData(r, "转出部门编码")); // 转出部门编码
                cannibSngl.setTranInDeptEncd(GetCellData(r, "转入部门编码")); // 转入部门编码
                cannibSngl.setCannibStatus("处理中"); // 调拨状态, a varchar(200
                cannibSngl.setMemo(GetCellData(r, "备注")); // 备注,q varchar(2000
                cannibSngl.setFormTypEncd("011");// 单据类型编码

                cannibSngl.setIsNtWms(0); // 是否向WMS上传,a int(11
                cannibSngl.setIsNtChk(0); // 是否审核(0.未审核1.已审核,a int(11
                cannibSngl.setIsNtCmplt(0); // 是否完成,a int(11
                cannibSngl.setIsNtClos(0); // 是否关闭,a int(11
                cannibSngl.setPrintCnt(
                        (new Double(GetCellData(r, "打印次数") == null || GetCellData(r, "打印次数").equals("") ? "0"
                                : GetCellData(r, "打印次数"))).intValue()); // 打印次数
                cannibSngl.setSetupPers(GetCellData(r, "制单人")); // 创建人
                cannibSngl.setSetupTm(
                        GetCellData(r, "制单时间") == null ? "" : GetCellData(r, "制单时间").replaceAll("[^0-9:-]", " ")); // 创建时间,q
                // datetime
                cannibSngl.setMdfr(GetCellData(r, "修改人")); // 修改人
                cannibSngl.setModiTm(
                        GetCellData(r, "修改时间") == null ? "" : GetCellData(r, "修改时间").replaceAll("[^0-9:-]", " ")); // 修改时间
                cannibSngl.setChkr(GetCellData(r, "审核人")); // 审核人
                cannibSngl.setChkTm(
                        GetCellData(r, "审核时间") == null ? "" : GetCellData(r, "审核时间").replaceAll("[^0-9:-]", " ")); // 审核时间
                List<CannibSnglSubTab> cannibSnglSubList = cannibSngl.getCheckSnglList();// 订单子表
                if (cannibSnglSubList == null) {
                    cannibSnglSubList = new ArrayList<>();
                }

                CannibSnglSubTab cannibSnglSubTab = new CannibSnglSubTab();

                cannibSnglSubTab.setFormNum(orderNo); // 单据号 varchar(200
                cannibSnglSubTab.setInvtyId(GetCellData(r, "存货编码")); // 存货编号 1 varchar(200

                cannibSnglSubTab.setCannibQty(new BigDecimal(
                        GetCellData(r, "数量") == null || GetCellData(r, "数量").equals("") ? "0" : GetCellData(r, "数量"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 调拨数量 1 decimal(208

                cannibSnglSubTab.setRecvQty(new BigDecimal(
                        GetCellData(r, "数量") == null || GetCellData(r, "数量").equals("") ? "0" : GetCellData(r, "数量"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 实收数量
                cannibSnglSubTab.setBatNum(GetCellData(r, "批号")); // 批号
                cannibSnglSubTab.setInvldtnDt(
                        GetCellData(r, "失效日期") == null ? "" : GetCellData(r, "失效日期").replaceAll("[^0-9:-]", " ")); // 失效日期
                // 1
                String BaoZhiQi = GetCellData(r, "保质期");
                if (BaoZhiQi != null && !BaoZhiQi.equals("")) {
                    BaoZhiQi = Double.valueOf(BaoZhiQi).intValue() + "";
                } else {
                    BaoZhiQi = null;
                } // datetime
                cannibSnglSubTab.setBaoZhiQi(BaoZhiQi); // 保质期 1 varchar(200
                cannibSnglSubTab.setPrdcDt(
                        GetCellData(r, "生产日期 ") == null ? "" : GetCellData(r, "生产日期").replaceAll("[^0-9:-]", " ")); // 生产日期
                // 1
                // datetime
                cannibSnglSubTab.setTaxRate(new BigDecimal(0).setScale(8, BigDecimal.ROUND_HALF_UP)); // 税率 a
                // decimal(208
                cannibSnglSubTab.setCntnTaxUprc(BigDecimal.ZERO); // 含税单价 1 decimal(208
                cannibSnglSubTab.setUnTaxUprc(new BigDecimal(
                        GetCellData(r, "单价") == null || GetCellData(r, "单价").equals("") ? "0" : GetCellData(r, "单价"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税单价
                cannibSnglSubTab.setCntnTaxAmt(BigDecimal.ZERO); // 含税金额
                cannibSnglSubTab.setUnTaxAmt(new BigDecimal(
                        GetCellData(r, "金额") == null || GetCellData(r, "金额").equals("") ? "0" : GetCellData(r, "金额"))
                        .setScale(8, BigDecimal.ROUND_HALF_UP)); // 未税金额 a decimal(208

                BigDecimal bxRule = GetCellData(r, "箱规") == null || GetCellData(r, "箱规").equals("") ? null
                        : new BigDecimal(GetCellData(r, "箱规"));
                if (bxRule != null && bxRule.compareTo(BigDecimal.ZERO) != 0) {
                    cannibSnglSubTab
                            .setBxQty(cannibSnglSubTab.getCannibQty().divide(bxRule, 2, BigDecimal.ROUND_HALF_UP)); // 箱数
                } else {
                    cannibSnglSubTab.setBxQty(BigDecimal.ZERO);// 箱数
                }
                cannibSnglSubTab.setProjEncd(GetCellData(r, "项目编码")); //项目编码

                cannibSnglSubList.add(cannibSnglSubTab);

                cannibSngl.setCheckSnglList(cannibSnglSubList);
                temp.put(orderNo, cannibSngl);

            }
            fileIn.close();
        } catch (Exception e) {

            throw new RuntimeException("解析格式问题第" + j + "行" + e.getMessage());

        }
        return temp;
    }

}
