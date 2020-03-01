package com.px.mis.whs.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.fasterxml.jackson.databind.JsonNode;
import com.px.mis.util.JacksonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.service.FormBookService;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.system.dao.MisLogDAO;
import com.px.mis.system.entity.MisLog;
import com.px.mis.util.BaseJson;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.service.IntoWhsService;

//ＰＤＡ的采购入　　销售出
@Controller
@RequestMapping("/whs/pda_into_whs")
public class IntoWhsController {

    private Logger logger = LoggerFactory.getLogger(IntoWhsController.class);

    @Autowired
    IntoWhsService intoWhsService;
    // 日志
    @Autowired
    MisLogDAO misLogDAO;
    @Autowired
    HttpServletRequest request;

    @Autowired
    InvtyNumMapper invtyNumMapper;
    @Autowired
    WhsDocMapper whsDocMapper;
    //	记账
    @Autowired
    FormBookService formBookService;

    // ------------------采购入--------------------
    // 查询所有到货单 采购入的查询
    @RequestMapping(value = "selectToGdsSnglList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectToGdsSnglList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectToGdsSnglList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectToGdsSnglList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectToGdsSnglList", false, "用户无业务仓库", null);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectToGdsSnglList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 新增到货拒收单信息 拒收接口(回传)
    @RequestMapping(value = "addToGdsSngl", method = RequestMethod.POST)
    @ResponseBody
    private String addPursOrdr(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/addToGdsSngl");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode aString = BaseJson.getReqHead(jsonBody);
            String userId = aString.get("accNum").asText();

            ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);
            List<Map> mList = BaseJson.getList(jsonBody);
            List<ToGdsSnglSub> toGdsSnglSubList = new ArrayList<>();
            for (Map map : mList) {
                ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
                toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
                BeanUtils.populate(toGdsSnglSub, map);
                toGdsSnglSubList.add(toGdsSnglSub);
            }

            // 添加到货单
            resp = intoWhsService.insertToGdsSngl(userId, toGdsSngl, toGdsSnglSubList);
            misLogDAO.insertSelective(new MisLog("PDA新增到货拒收单信息", "仓库", null, jsonBody, request));

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("PDA新增到货拒收单信息", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/addToGdsSngl", true, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 新增采购入库单信息 采购入接口(回传)
    @RequestMapping(value = "addIntoWhs", method = RequestMethod.POST)
    @ResponseBody
    private String addIntoWhs(@RequestBody String jsonBody) throws Exception {
        logger.info("url:whs/pda_into_whs/addIntoWhs");
        logger.info("请求参数：" + jsonBody);
        String resp = "";

        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/addIntoWhs", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            ObjectNode aString = BaseJson.getReqHead(jsonBody);

            String userId = aString.get("accNum").asText();
            String userName = aString.get("userName").asText();

            List<ToGdsSnglSub> tLists = new ArrayList<>();// 到货拒收单子表
            Map<String, List<MovBitTab>> movMap = new HashMap<String, List<MovBitTab>>();
            List<MovBitTab> list = new ArrayList<MovBitTab>();

//            JSONArray array = JSON.parseArray(JSON.parseObject(jsonBody).getJSONObject("reqBody").getString("list"));
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");

//                ToGdsSnglSub gdsSnglSub = JSON.parseObject(str.toString(), ToGdsSnglSub.class);

                ToGdsSnglSub gdsSnglSub = JacksonUtil.getPOJO(str, ToGdsSnglSub.class);

                gdsSnglSub.setCrspdBarCd(str.get("ordrNum").asText());// 对应条形码 设置为序号
                gdsSnglSub.setQty(BigDecimal.ZERO);// 实际到货数量


                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("toGdsSnglId").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());

                if (oSubTab.getGdsBitEncd() != null && !oSubTab.getGdsBitEncd().equals("")) {
                    String[] strs = oSubTab.getGdsBitEncd().split(",");
                    String[] qtys = qty.split(",");
                    for (int j = 0; j < strs.length; j++) {
                        MovBitTab oSubTabs = new MovBitTab();
                        org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);

                        String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), strs[j]);
                        if (regnEncd == null) {
                            logger.info("请检查货位的区域是否存在");
                            throw new RuntimeException("请检查仓库货位对应关系是否存在");
                        }
                        oSubTabs.setRegnEncd(regnEncd);
                        oSubTabs.setQty(new BigDecimal(qtys[j]));
                        gdsSnglSub.setQty(gdsSnglSub.getQty().add(oSubTabs.getQty()));// 实际到货数量
                        oSubTabs.setGdsBitEncd(strs[j]);
                        list.add(oSubTabs);
                    }

                }
                tLists.add(gdsSnglSub);

            }
            ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);

//			for (MovBitTab movBitTab : list) {
//				String key = movBitTab.getWhsEncd();
//				List<MovBitTab> bitTabs;
//				if (movMap.containsKey(key)) {
//					bitTabs = movMap.get(key);
//				} else {
//					bitTabs = new ArrayList<MovBitTab>();
//				}
//				bitTabs.add(movBitTab);
//				movMap.put(key, bitTabs);
//			}
//
//			List<List<MovBitTab>> lists = new ArrayList<List<MovBitTab>>(movMap.values());

            resp = intoWhsService.addIntoWhs(userId, userName, null, null, null, list, toGdsSngl, tLists);
            if (true) {
                logger.info("返回参数：" + resp);

                return resp;
            }

            // 入库单和到货拒收单主表
            IntoWhs intoWhs = BaseJson.getPOJO(jsonBody, IntoWhs.class);
//			ToGdsSngl toGdsSngl = BaseJson.getPOJO(jsonBody, ToGdsSngl.class);
            List<Map> mList = BaseJson.getList(jsonBody);

            List<IntoWhsSub> intoWhsSubList = new ArrayList<>();// 入库单子表
            List<InvtyTab> iList = new ArrayList<>();// 库存表
            List<MovBitTab> mBList = new ArrayList<>();// 移位表
            List<ToGdsSnglSub> tList = new ArrayList<>();// 到货拒收单子表

            for (Map map : mList) {
                // System.out.println(map.toString());

                String qty = map.get("qty").toString();
                String whsEncd = map.get("whsEncd").toString();
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
                String gdsBitEncd = map.get("gdsBitEncd").toString();
                String prdcDt = map.get("prdcDt").toString();

                // 移位表 (入库的时候如果存货本身有增在所在货位上增加，如果没有则进行添加)
                String[] gBitEncd = gdsBitEncd.split(",");
                String[] Qty = qty.split(",");// 10,20,30
                MovBitTab movBitTab;
                for (int m = 0; m < gBitEncd.length; m++) {
                    // map.put("qty", Qty[m]);
                    movBitTab = new MovBitTab();
                    movBitTab.setInvtyEncd(invtyEncd);// 存货编码
                    movBitTab.setWhsEncd(whsEncd);// 仓库编码
                    movBitTab.setBatNum(batNum);// 批次号
                    movBitTab.setGdsBitEncd(gBitEncd[m]);// 货位编码
                    movBitTab.setPrdcDt(prdcDt);
                    String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                    movBitTab.setIntoDt(dateStr);
                    String qString = Qty[m].toString();

                    // System.out.println("Qty[m].toString()" + Qty[m].toString());
                    // System.out.println("for" + map.get("qty"));

                    movBitTab.setQty(new BigDecimal(qString));// 数量
                    mBList.add(movBitTab);
                }

            }

            IntoWhsSub intoWhsSub;
            for (Map map : mList) {
                // System.out.println(map);

                map.remove("gdsBitEncd");

                String qty = map.get("qty").toString();
                String[] Qty = qty.split(",");// 10,20,30
                double dQty = 0;
                for (int m = 0; m < Qty.length; m++) {
                    dQty += new BigDecimal(Qty[m]).doubleValue();
                    // System.out.println(dQty);
                }
                map.put("qty", dQty);
                // 入库单子表
                intoWhsSub = new IntoWhsSub();
//				intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());
//				intoWhsSub.setQty(new BigDecimal(dQty));
                BeanUtils.populate(intoWhsSub, map);
                intoWhsSubList.add(intoWhsSub);

                // 到货拒收单
                ToGdsSnglSub toGdsSnglSub = new ToGdsSnglSub();
//				toGdsSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
//				toGdsSnglSub.setQty(new BigDecimal(dQty));
                BeanUtils.populate(toGdsSnglSub, map);
                tList.add(toGdsSnglSub);

            }

            // 新增入库单
//			intoWhsService.addIntoWhs(userId, userName, intoWhs, intoWhsSubList, iList, mBList, toGdsSngl, tList);
            misLogDAO.insertSelective(new MisLog("PDA新增采购入库单信息", "仓库", null, jsonBody, request));

            try {
                resp = BaseJson.returnRespObj("/whs/pda_into_whs/addIntoWhs", true, "新增成功！", null);
            } catch (IOException e) {

            }

            logger.info("返回参数：" + resp);

        } catch (Exception e) {
            misLogDAO.insertSelective(new MisLog("PDA新增采购入库单信息", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/addIntoWhs", false, e.getMessage(), null);

        }

        return resp;

    }

    // -----------销售出-------------------------------
    // 查询所有出库单
    @RequestMapping(value = "selectSellOutWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectSellOutWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectSellOutWhsList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectSellOutWhsList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectSellOutWhsList", false, "用户无业务仓库", null);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectSellOutWhsList", false, e.getMessage(), null);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    private String realWhsString(String whsEncd) {
        // TODO Auto-generated method stub
        List<String> aList = whsDocMapper.selectRealWhsList(getList(whsEncd));

        String s = "";
        for (int i = 0; i < aList.size(); i++) {
            if (i == aList.size() - 1) {
                s += aList.get(i);
            } else {
                s += aList.get(i) + ",";
            }
        }
        if (s.equals("")) {
            s = null;
        }
        return s;
    }

    // 销售出库（回传）
    @RequestMapping(value = "updatesOutWhs", method = RequestMethod.POST)
    @ResponseBody
    public String selectCheckSnglStauts(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/updatesOutWhs");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");

                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);

                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());
                String[] strs = oSubTab.getGdsBitEncd().split(",");
                String[] qtys = qty.split(",");
                for (int j = 0; j < strs.length; j++) {
                    MovBitTab oSubTabs = new MovBitTab();
                    org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);

                    String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), strs[j]);
                    if (regnEncd == null) {
                        logger.info("请检查货位的区域是否存在");
                        throw new RuntimeException("请检查仓库货位对应关系是否存在");
                    }
                    oSubTabs.setRegnEncd(regnEncd);

                    oSubTabs.setQty(new BigDecimal(qtys[j]));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            // 主表

            SellOutWhs sOutWhs = new SellOutWhs();
            sOutWhs.setChkr(BaseJson.getReqHead(jsonBody).get("userName").asText());
            sOutWhs.setOutWhsId(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
            sOutWhs.setIsNtChk(1);

            resp = intoWhsService.updatesOutWhs(sOutWhs, null, null, list);

            if (true) {
                logger.info("返回参数：" + resp);
                return resp;
            }

            // 子表
            List<Map> oTabMap = BaseJson.getList(jsonBody);
            List<SellOutWhsSub> sList = new ArrayList<>();// 出入库子表
            List<InvtyTab> iList = new ArrayList<>();// 库存表
            List<MovBitTab> mList = new ArrayList<>();// 移位表

            for (Map map : oTabMap) {
                String qty = map.get("qty").toString();
                String whsEncd = map.get("whsEncd").toString();
                String invtyEncd = map.get("invtyEncd").toString();
                String batNum = map.get("batNum").toString();
                String gdsBitEncd = map.get("gdsBitEncd").toString();

                String[] Qty = qty.split(",");// 10,20,30
                double dQty = 0;
                for (int m = 0; m < Qty.length; m++) {
                    dQty += new BigDecimal(Qty[m]).doubleValue();
                }
                // 出入库子 取到出入库数量
                SellOutWhsSub sub = new SellOutWhsSub();// 出入库子表
                sub.setQty(new BigDecimal(dQty));// 数量
                // System.out.println(dQty);
                sList.add(sub);

                // 库存表 【】
                InvtyTab invtyTab = new InvtyTab();
                invtyTab.setWhsEncd(whsEncd);// 仓库编码(子表里面有)
                invtyTab.setInvtyEncd(invtyEncd);// 存货编码
                invtyTab.setBatNum(batNum);// 批号
                invtyTab.setNowStok(new BigDecimal(dQty));
                iList.add(invtyTab);

                // 移位表
                String[] gBitEncd = gdsBitEncd.split(",");
                MovBitTab movBitTab = null;
                for (int m = 0; m < gBitEncd.length; m++) {
                    map.put("qty", Qty[m]);
                    movBitTab = new MovBitTab();
                    movBitTab.setInvtyEncd(invtyEncd);
                    ;// 存货编码
                    movBitTab.setWhsEncd(whsEncd);
                    ;// 仓库编码
                    movBitTab.setBatNum(batNum);// 批次号
                    movBitTab.setGdsBitEncd(gBitEncd[m]);// 货位编码
                    String qString = map.get("qty").toString();
                    movBitTab.setQty(new BigDecimal(qString));// 数量
                    mList.add(movBitTab);
                }

            }
            resp = intoWhsService.updatesOutWhs(sOutWhs, sList, iList, mList);
            misLogDAO.insertSelective(new MisLog("PDA销售出库（回传）", "仓库", null, jsonBody, request));

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("PDA销售出库（回传）", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);

        return resp;
    }

    // 拒收原因
    @RequestMapping(value = "selectReason", method = RequestMethod.POST)
    @ResponseBody
    public String selectReason(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectReason");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            resp = intoWhsService.selectReason();
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectReason", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // -----------红字销售出-------------------------------
    // 查询所有红字出库单
    @RequestMapping(value = "selectRedSellOutWhsList", method = RequestMethod.POST)
    @ResponseBody
    public Object selectRedSellOutWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectRedSellOutWhsList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectRedSellOutWhsList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectRedSellOutWhsList", false, "用户无业务仓库", null);
            }

        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectRedSellOutWhsList", false, e.getMessage(), null);
        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 红字销售出库（回传）
    @RequestMapping(value = "updatesRedOutWhsPda", method = RequestMethod.POST)
    @ResponseBody
    public String updatesRedOutWhsPda(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/updatesRedOutWhsPda");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());
                String[] strs = oSubTab.getGdsBitEncd().split(",");
                String[] qtys = qty.split(",");
                for (int j = 0; j < strs.length; j++) {
                    MovBitTab oSubTabs = new MovBitTab();
                    org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);

                    String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), strs[j]);
                    if (regnEncd == null) {
                        logger.info("请检查货位的区域是否存在");
                        throw new RuntimeException("请检查仓库货位对应关系是否存在");
                    }
                    oSubTabs.setRegnEncd(regnEncd);

                    oSubTabs.setQty(BigDecimal.ZERO.subtract(new BigDecimal(qtys[j])));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            // 主表

            SellOutWhs sOutWhs = new SellOutWhs();
            sOutWhs.setChkr(BaseJson.getReqHead(jsonBody).get("userName").asText());
            sOutWhs.setOutWhsId(BaseJson.getReqBody(jsonBody).get("outWhsId").asText());
            sOutWhs.setIsNtChk(1);

            resp = intoWhsService.updatesRedOutWhs(sOutWhs, list);

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("PDA红字销售出库（回传）", "仓库", null, jsonBody, request, e));
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);

        return resp;
    }

    // 采购退货的查询
    @RequestMapping(value = "selectIntoWhsList", method = RequestMethod.POST)
    @ResponseBody
    public String selectIntoWhsList(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/selectIntoWhsList");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {

            ObjectNode reqBody = BaseJson.getReqBody(jsonBody);
            String whsEncd = reqBody.get("whsEncd").asText();
//			whsEncd = realWhsString(whsEncd);

            if (whsEncd != null) {
                resp = intoWhsService.selectIntoWhsList(whsEncd);
            } else {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/selectIntoWhsList", false, "用户无业务仓库", null);
            }
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("whs/pda_into_whs/selectIntoWhsList", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);
        return resp;
    }

    // 采购退货（回传）
    @RequestMapping(value = "updatesRedIntoWhsPda", method = RequestMethod.POST)
    @ResponseBody
    public String updatesRedIntoWhsPda(@RequestBody String jsonBody) throws IOException {
        logger.info("url:whs/pda_into_whs/updatesRedIntoWhsPda");
        logger.info("请求参数：" + jsonBody);
        String resp = "";
        try {
            if (formBookService.isMthSeal(LocalDateTime.now().toString())) {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedIntoWhsPda", false, "当月已封账！", null);
                logger.info("返回参数：" + resp);
                return resp;
            }
            List<MovBitTab> list = new ArrayList<MovBitTab>();
            JsonNode array = BaseJson.getReqBody(jsonBody).get("list");
            for (int i = 0; i < array.size(); i++) {
                ObjectNode str = (ObjectNode) array.path(i);
                String qty = str.get("qty").asText();
                str.remove("qty");
                MovBitTab oSubTab = JacksonUtil.getPOJO(str, MovBitTab.class);
                oSubTab.setOrderNum(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
                oSubTab.setSerialNum(str.get("ordrNum").asText());
                String[] strs = oSubTab.getGdsBitEncd().split(",");
                String[] qtys = qty.split(",");
                for (int j = 0; j < strs.length; j++) {
                    MovBitTab oSubTabs = new MovBitTab();
                    org.springframework.beans.BeanUtils.copyProperties(oSubTab, oSubTabs);

                    String regnEncd = whsDocMapper.selectWhsGdsReal(oSubTab.getWhsEncd(), strs[j]);
                    if (regnEncd == null) {
                        logger.info("请检查货位的区域是否存在");
                        throw new RuntimeException("请检查仓库货位对应关系是否存在");
                    }
                    oSubTabs.setRegnEncd(regnEncd);

                    oSubTabs.setQty(BigDecimal.ZERO.subtract(new BigDecimal(qtys[j])));
                    oSubTabs.setGdsBitEncd(strs[j]);
                    list.add(oSubTabs);
                }
            }
            // 主表

            IntoWhs intoWhs = new IntoWhs();
            intoWhs.setChkr(BaseJson.getReqBody(jsonBody).get("userName").asText());
            intoWhs.setIntoWhsSnglId(BaseJson.getReqBody(jsonBody).get("intoWhsSnglId").asText());
            intoWhs.setIsNtChk(1);

            resp = intoWhsService.updatesRedIntoWhs(intoWhs, list);

        } catch (Exception e) {

            misLogDAO.insertSelective(new MisLog("采购退货（回传）", "仓库", null, jsonBody, request, e));

            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedIntoWhsPda", false, e.getMessage(), null);

        }
        logger.info("返回参数：" + resp);

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
}
