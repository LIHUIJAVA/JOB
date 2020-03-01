package com.px.mis.whs.service.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.px.mis.purc.dao.IntoWhsDao;
import com.px.mis.purc.dao.IntoWhsSubDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.ToGdsSnglDao;
import com.px.mis.purc.dao.ToGdsSnglSubDao;
import com.px.mis.purc.entity.IntoWhs;
import com.px.mis.purc.entity.IntoWhsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.ToGdsSngl;
import com.px.mis.purc.entity.ToGdsSnglSub;
import com.px.mis.purc.service.impl.PurcIntoWhsServiceImpl;
import com.px.mis.purc.service.impl.SellOutWhsServiceImpl;
import com.px.mis.purc.service.impl.ToGdsSnglServiceImpl;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.IntoWhsMapper;
import com.px.mis.whs.dao.InvtyGdsBitListMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.OthOutIntoWhsMapper;
import com.px.mis.whs.entity.GdsBit;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.MovBitTab;
import com.px.mis.whs.entity.RefuseReason;
import com.px.mis.whs.service.IntoWhsService;

@Service
@Transactional
public class IntoWhsServiceImpl implements IntoWhsService {

    @Autowired
    IntoWhsMapper intoWhsMapper;

    @Autowired
    OthOutIntoWhsMapper othOutIntoWhsMapper;

    @Autowired
    InvtyNumMapper invtyNumMapper;

    // 到货单
    @Autowired
    ToGdsSnglDao toGdsSnglDao;
    @Autowired
    ToGdsSnglSubDao toGdsSnglSubDao;
    // 入库单
    @Autowired
    IntoWhsDao intoWhsDao;
    @Autowired
    IntoWhsSubDao intoWhsSubDao;
    // 订单号
    @Autowired
    GetOrderNo getOrderNo;
    // 销售出库单
    @Autowired
    SellOutWhsDao sellOutWhsDao;
    @Autowired
    SellOutWhsSubDao sellOutWhsSubDao;
    @Autowired
    InvtyGdsBitListMapper bitListMapper;
    @Autowired
    ToGdsSnglServiceImpl toGdsSnglServiceImpl;

    @Autowired
    PurcIntoWhsServiceImpl purcIntoWhsServiceImpl;
    @Autowired
    SellOutWhsServiceImpl sellOutWhsServiceImpl;

    // 查询所有到货单
    @Override
    public String selectToGdsSnglList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<ToGdsSngl> tGdsSngl = intoWhsMapper.selectToGdsSnglList(list);

        // **************推荐入库货位*****************

        for (ToGdsSngl toGdsSngl : tGdsSngl) {
            List<ToGdsSnglSub> tSnglSubsList = toGdsSngl.getToGdsSnglSub();
            for (ToGdsSnglSub tSnglSub : tSnglSubsList) {

                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(tSnglSub.getWhsEncd());
                movBitTab.setInvtyEncd(tSnglSub.getInvtyEncd());
                movBitTab.setBatNum(tSnglSub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                tSnglSub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (tGdsSngl.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectToGdsSnglList", true, "查询成功！", null,
                        tGdsSngl);
            } else {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectToGdsSnglList", false, "暂无数据", null,
                        tGdsSngl);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    // 新增到货拒收单信息
    @Override
    public String insertToGdsSngl(String userId, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> toGdsSnglSubList) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        String number = null;
        try {
            number = getOrderNo.getSeqNo("DHD", userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e1) {

            throw new RuntimeException("单号获取失败");
        } // 获取订单号

        if (intoWhsMapper.selectToGdsSnglByToGdsSnglId(number) != null) {
            message = "编号" + number + "已存在，请重新输入！";
            isSuccess = false;
        } else {

            toGdsSngl.setToGdsSnglId(number);// 订单号
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String toGdsSnglDt = sdf.format(date);
            toGdsSngl.setToGdsSnglDt(toGdsSnglDt);// 拒收单日期

            toGdsSnglDao.insertToGdsSngl(toGdsSngl);
            toGdsSnglSubDao.insertToGdsSnglSub(toGdsSnglSubList);

            message = "新增成功！";
            isSuccess = true;
        }

        try {
            resp = BaseJson.returnRespObj("/whs/pda_into_whs/addToGdsSngl", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    /*
     * //新增入库单
     *
     * @Override public String addIntoWhs(String userId,String userName, IntoWhs
     * intoWhs, List<IntoWhsSub> intoWhsSubList, List<InvtyTab>
     * invtyTab,List<MovBitTab> movBitTab, ToGdsSngl toGdsSngl,List<ToGdsSnglSub>
     * tSnglSubList) throws Exception{
     *
     * String message=""; Boolean isSuccess=true; String resp="";
     *
     * String number = null; try { number = getOrderNo.getSeqNo("RK", userId); }
     * catch (Exception e) { //此处需要跳出，抛出异常，带回错误结果； throw new Exception("无法获取到入库单号！"
     * + e.getMessage()); }//获取订单号
     *
     * for(IntoWhsSub intoWhsSub:intoWhsSubList) {
     *
     * if(intoWhsSub.getReturnQty().compareTo(BigDecimal.ZERO) >0) {
     * //则有入库单和拒收单--------------------- //1.入库单 //新增入库单
     * intoWhs.setIntoWhsSnglId(number);//单据号 Date date=new Date(); SimpleDateFormat
     * sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String intoWhsDt =
     * sdf.format(date); intoWhs.setIntoWhsDt(intoWhsDt);//入库日期
     * intoWhs.setRecvSendCateId("1");//收发类别编码 采购入库
     * intoWhs.setOutIntoWhsTypId("9");//其他出入库类别编码 intoWhs.setAccNum(userId);//业务员编码
     * intoWhs.setUserName(userName);//操作人 intoWhs.setSetupPers(userName);//创建人
     *
     * //新增主、子表信息 intoWhsDao.insertIntoWhs(intoWhs);
     * intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());//入库单据号
     * intoWhsSub.setIsNtRtnGoods(1); BigDecimal dQty=BigDecimal.ZERO; for(MovBitTab
     * mTab:movBitTab) { dQty = dQty.add(mTab.getQty()); } intoWhsSub.setQty(dQty);
     * intoWhsSubDao.insertIntoWhsSub(intoWhsSubList);
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
     *
     * //2.拒收单 String number1 = null; try { number1 = getOrderNo.getSeqNo("DHD",
     * userId); } catch (Exception e1) { }//获取订单号
     *
     *
     * //到货拒收单主 toGdsSngl.setToGdsSnglId(number1);//订单号 Date date1=new Date();
     * SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
     * toGdsSnglDt = sdf1.format(date1);
     * toGdsSngl.setToGdsSnglDt(toGdsSnglDt);//拒收单日期
     * toGdsSngl.setAccNum(userId);//业务员编号 toGdsSngl.setUserName(userName);//业务员名称
     * toGdsSngl.setSetupPers(userName);//创建人
     *
     * //到货拒收单子表 for(ToGdsSnglSub tSnglSub:tSnglSubList) {
     * tSnglSub.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
     * tSnglSub.setMemo(tSnglSub.getReturnMemo());//备注 BigDecimal
     * rQty=tSnglSub.getReturnQty(); BigDecimal rFu=new BigDecimal("-1"); double
     * cheng=rQty.multiply(rFu).doubleValue(); BigDecimal returnQty=new
     * BigDecimal(cheng); tSnglSub.setQty(returnQty);//拒收数量 //未税金额
     * tSnglSub.setCntnTaxUprc(CalcAmt.noTaxAmt(tSnglSub.getNoTaxUprc(),
     * tSnglSub.getReturnQty())); //含税单价
     * tSnglSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(tSnglSub.getNoTaxUprc(),
     * tSnglSub.getReturnQty(), tSnglSub.getTaxRate())); //含税金额(价税合计)
     * tSnglSub.setPrcTaxSum(CalcAmt.prcTaxSum(tSnglSub.getNoTaxUprc(),
     * tSnglSub.getReturnQty(), tSnglSub.getTaxRate()));
     * tSnglSub.setIsNtRtnGoods(0); }
     *
     * toGdsSnglDao.insertToGdsSngl(toGdsSngl);
     * toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubList);
     *
     *
     * message="新增成功！"; isSuccess=true;
     *
     * try { resp=BaseJson.returnRespObj("/whs/pda_into_whs/addIntoWhs", isSuccess,
     * message, null); } catch (IOException e) { throw new RuntimeException(); }
     *
     * }else { //入库单-------------
     *
     * //入库单主表 intoWhs.setIntoWhsSnglId(number);//单据号 Date date=new Date();
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
     * intoWhsDt = sdf.format(date); intoWhs.setIntoWhsDt(intoWhsDt);//入库日期
     * intoWhs.setRecvSendCateId("1");//收发类别编码 采购入库
     * intoWhs.setOutIntoWhsTypId("9");//其他出入库类别编码
     * intoWhs.setUserName(userName);//操作人 intoWhs.setSetupPers(userName);//创建人
     *
     * //入库单子表 intoWhsSub.setIntoWhsSnglId(intoWhs.getIntoWhsSnglId());//入库单据号
     * intoWhsSub.setIsNtRtnGoods(1); double dQty=0; for(MovBitTab mTab:movBitTab) {
     * dQty += mTab.getQty().doubleValue(); } intoWhsSub.setQty(new
     * BigDecimal(dQty));
     *
     * //新增主、子表信息 intoWhsDao.insertIntoWhs(intoWhs);
     * intoWhsSubDao.insertIntoWhsSub(intoWhsSubList);
     *
     * message="新增成功！"; isSuccess=true;
     *
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
     *
     * try { resp=BaseJson.returnRespObj("/whs/pda_into_whs/addIntoWhs", isSuccess,
     * message, null); } catch (IOException e) { }
     *
     *
     * } }
     *
     *
     * return resp; }
     */

    /**
     * 新增入库单 未修改库存
     */
    @Override
    public String addIntoWhs(String userId, String userName, IntoWhs intoWhs, List<IntoWhsSub> intoWhsSubList,
                             List<InvtyTab> invtyTab, List<MovBitTab> movBitTab, ToGdsSngl toGdsSngl, List<ToGdsSnglSub> tSnglSubList)
            throws Exception {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            // 拒收主表
            ToGdsSngl toGdsSngls = null;
            // 拒收单号
            String number = null;
            // 拒收子表
            List<ToGdsSnglSub> tSnglSubLists = new ArrayList<ToGdsSnglSub>();
            // 入库子
            List<IntoWhsSub> intoWhsSubs = new ArrayList<IntoWhsSub>();
            // 入库主
//			intoWhs=new IntoWhs();

            String numbers = null;
            toGdsSngl = toGdsSnglDao.selectToGdsSnglById(toGdsSngl.getToGdsSnglId());

            for (ToGdsSnglSub gdsSnglSubs : tSnglSubList) {
                ToGdsSnglSub snglSub = intoWhsMapper.selectToGdsByOrdrNumKey(gdsSnglSubs.getOrdrNum());
                if (snglSub != null) {
                    snglSub.setQty(gdsSnglSubs.getQty());//实际到货量

                    IntoWhsSub intoWhsSub = new IntoWhsSub();
                    BeanUtils.copyProperties(snglSub, intoWhsSub);
                    intoWhsSub.setOrdrNum(null);
                    intoWhsSub.setToOrdrNum(gdsSnglSubs.getOrdrNum());
                    intoWhsSub.setCrspdBarCd(gdsSnglSubs.getCrspdBarCd());
                    intoWhsSub.setPursOrdrNum(snglSub.getToOrdrNum());
                    if (gdsSnglSubs.getIntlBat() != null && !gdsSnglSubs.getIntlBat().equals("")) {
                        intoWhsSub.setIntlBat(gdsSnglSubs.getIntlBat());
                    }
                    intoWhsSub.setPursToGdsSnglSubTabInd(snglSub.getOrdrNum().toString());
                    intoWhsSub.setReturnMemo(null);
//                    if (gdsSnglSubs.getReturnQty() != null
//                            && gdsSnglSubs.getReturnQty().abs().compareTo(snglSub.getQty()) == 0) {
//                        if (number == null) {
//                            toGdsSngls = new ToGdsSngl();
//                            number = aInstToGds(toGdsSngls, userId, userName, toGdsSngl);
//                        }
//                        snglSub.setToGdsSnglId(number);
//                        snglSub.setQty(BigDecimal.ZERO.subtract(gdsSnglSubs.getReturnQty().abs()));// 拒收数量
//                        snglSub.setMemo(gdsSnglSubs.getMemo());
//                        snglSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(snglSub.getQty()));// 无税金额
//                        snglSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(snglSub.getQty()));// 价税合计
//                        snglSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// 税额
//                        snglSub.setBxQty(BigDecimal.ZERO.subtract(snglSub.getBxQty()));// 箱数
//                        snglSub.setIsNtRtnGoods(0);
//                        snglSub.setOrdrNum(null);
//
//                        snglSub.setToOrdrNum(gdsSnglSubs.getOrdrNum());
//                        snglSub.setUnIntoWhsQty(BigDecimal.ZERO);
//                        snglSub.setReturnQty(BigDecimal.ZERO);
//
//                        tSnglSubLists.add(snglSub);
//                    } else

                        if (gdsSnglSubs.getReturnQty() != null
                            && gdsSnglSubs.getReturnQty().abs().compareTo(BigDecimal.ZERO) != 0) {
                        if (number == null) {
                            toGdsSngls = new ToGdsSngl();
                            number = aInstToGds(toGdsSngls, userId, userName, toGdsSngl);
                        }

                        // 有拒收明细
                        intoWhsSub.setQty(snglSub.getQty());
                        intoWhsSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(intoWhsSub.getQty()));// 无税金额
                        intoWhsSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(intoWhsSub.getQty()));// 价税合计
                        intoWhsSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// 税额
                        intoWhsSub.setBxQty(intoWhsSub.getQty().divide(
                                (snglSub.getBxRule() == null || snglSub.getBxRule().compareTo(BigDecimal.ZERO) == 0)
                                        ? new BigDecimal(1)
                                        : snglSub.getBxRule(),
                                2, BigDecimal.ROUND_HALF_UP));// 箱数

                        intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum());
                        intoWhsSub.setUnBllgQty(intoWhsSub.getQty());
                        intoWhsSub.setUnBllgUprc(intoWhsSub.getCntnTaxUprc());
                        intoWhsSub.setReturnQty(intoWhsSub.getQty());
                        intoWhsSubs.add(intoWhsSub);

                        // 拒收子
                        snglSub.setToGdsSnglId(number);
                        snglSub.setQty(BigDecimal.ZERO.subtract(gdsSnglSubs.getReturnQty().abs()));// 拒收数量
                        snglSub.setMemo(gdsSnglSubs.getMemo());
                        snglSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(snglSub.getQty()));// 无税金额
                        snglSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(snglSub.getQty()));// 价税合计
                        snglSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// 税额
                        snglSub.setBxQty(snglSub.getQty().divide(
                                (snglSub.getBxRule() == null || snglSub.getBxRule().compareTo(BigDecimal.ZERO) == 0)
                                        ? new BigDecimal(1)
                                        : snglSub.getBxRule(),
                                2, BigDecimal.ROUND_HALF_UP));// 箱数
                        snglSub.setIsNtRtnGoods(0);
                        snglSub.setOrdrNum(null);
                        snglSub.setToOrdrNum(gdsSnglSubs.getOrdrNum());
                        snglSub.setUnIntoWhsQty(BigDecimal.ZERO);
                        snglSub.setReturnQty(BigDecimal.ZERO);

                        tSnglSubLists.add(snglSub);

                    } else {

                        // 实际到货量
                        intoWhsSub.setQty(snglSub.getQty());
                        intoWhsSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(intoWhsSub.getQty()));// 无税金额
                        intoWhsSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(intoWhsSub.getQty()));// 价税合计
                        intoWhsSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// 税额
                        intoWhsSub.setBxQty(intoWhsSub.getQty().divide(
                                (snglSub.getBxRule() == null || snglSub.getBxRule().compareTo(BigDecimal.ZERO) == 0)
                                        ? new BigDecimal(1)
                                        : snglSub.getBxRule(),
                                2, BigDecimal.ROUND_HALF_UP));// 箱数

                        intoWhsSub.setUnBllgAmt(intoWhsSub.getPrcTaxSum());
                        intoWhsSub.setUnBllgQty(intoWhsSub.getQty());
                        intoWhsSub.setUnBllgUprc(intoWhsSub.getCntnTaxUprc());
                        intoWhsSub.setReturnQty(intoWhsSub.getQty());
                        intoWhsSubs.add(intoWhsSub);
                    }
                } else {
                    throw new RuntimeException("到货单单据明细序号错误");
                }

            }

            // 入库主
            List<String> lists = new ArrayList<String>();
            // 分仓库
            Map<String, List<IntoWhsSub>> intoWhsMap = new HashMap<String, List<IntoWhsSub>>();

            for (IntoWhsSub into : intoWhsSubs) {
                String key = into.getWhsEncd();
                List<IntoWhsSub> intoWhslist;
                if (intoWhsMap.containsKey(key)) {
                    intoWhslist = intoWhsMap.get(key);
                } else {
                    intoWhslist = new ArrayList<IntoWhsSub>();
                }
                intoWhslist.add(into);
                intoWhsMap.put(key, intoWhslist);
            }

            List<List<IntoWhsSub>> intoLists = new ArrayList<List<IntoWhsSub>>(intoWhsMap.values());

            List<IntoWhs> IntoWhslist = new ArrayList<IntoWhs>();

            for (List<IntoWhsSub> intoList : intoLists) {
                if (intoList.size() > 0) {
                    // System.out.println("\t\t\t仓库" + intoList.get(0).getWhsEncd());

                    // 入库单号
                    intoWhs = new IntoWhs();
                    numbers = aInstIntoWhs(intoWhs, userId, userName, toGdsSngl);
                    IntoWhslist.add(intoWhs);

                    for (IntoWhsSub intoWhsSub : intoList) {
                        intoWhsSub.setIntoWhsSnglId(numbers);
                    }
                    // System.out.println("\t 插入前" + intoList.size());

                    intoWhsSubDao.insertIntoWhsSub(intoList);
                    // System.out.println("\t 插入后" + intoList.size());

                    for (IntoWhsSub intoWhsSub : intoList) {

                        // System.out.println("\t 序号" + intoWhsSub.getOrdrNum());

                        for (MovBitTab mov : movBitTab) {
                            // System.out.println(mov.getSerialNum() + "" + intoWhsSub.getCrspdBarCd());
                            if (mov.getSerialNum().equals(intoWhsSub.getCrspdBarCd())) {
                                mov.setOrderNum(numbers);
                                mov.setSerialNum(intoWhsSub.getOrdrNum().toString());
                                lists.add(mov.getOrderNum());
                            }

                        }
                    }
                }

            }

            // 到货拒收单子
            if (tSnglSubLists.size() > 0) {
                toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubLists);
            }
            if (toGdsSngls != null) {
                toGdsSnglServiceImpl.updateReturnToGdsSnglIsNtChkOK(toGdsSngls);
            }

            if (movBitTab.size() > 0) {
                bitListMapper.deleteInvtyGdsBitList(lists);
                bitListMapper.insertInvtyGdsBitLists(movBitTab);
            }
            message = "成功";

            // 处理中-处理完成
//			ToGdsSngl toGdsSngla = new ToGdsSngl();
//			toGdsSngla.setToGdsSnglId(toGdsSngl.getToGdsSnglId());
//			toGdsSngla.setDealStat("处理完成");
            intoWhsMapper.updateTGdsGngl(tSnglSubList);

            for (IntoWhs whs : IntoWhslist) {
                whs.setChkr(userId);
                whs.setIsNtChk(1);
                purcIntoWhsServiceImpl.updateIntoWhsIsNtChk(whs);
            }

            if (true) {
                try {
                    resp = BaseJson.returnRespObj("whs/pda_into_whs/addIntoWhs", isSuccess, message, null);
                    return resp;
                } catch (IOException e) {


                }
            }

            // 入库子
            List<ToGdsSnglSub> toGdsSnglSubs = toGdsSnglSubDao
                    .selectToGdsSnglSubByToGdsSnglId(toGdsSngl.getToGdsSnglId());
            for (ToGdsSnglSub gdsSnglSub : toGdsSnglSubs) {
                IntoWhsSub intoWhsSub = new IntoWhsSub();
                BeanUtils.copyProperties(gdsSnglSub, intoWhsSub);
                intoWhsSub.setIntoWhsSnglId(numbers);
                intoWhsSub.setOrdrNum(null);

                for (ToGdsSnglSub gdsSnglSubs : tSnglSubList) {

                    ToGdsSnglSub snglSub = intoWhsMapper.selectToGdsByOrdrNumKey(gdsSnglSubs.getOrdrNum());
                    if (snglSub != null) {
                        if (number == null) {
                            toGdsSngls = new ToGdsSngl();
                            number = aInstToGds(toGdsSngls, userId, userName, toGdsSngl);
                        }

                        intoWhsSub.setQty(snglSub.getQty().subtract(gdsSnglSubs.getQty().abs()));
                        intoWhsSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(intoWhsSub.getQty()));// 无税金额
                        intoWhsSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(intoWhsSub.getQty()));// 价税合计
                        intoWhsSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// 税额

                        snglSub.setToGdsSnglId(number);
                        snglSub.setQty(BigDecimal.ZERO.subtract(gdsSnglSubs.getQty().abs()));// 拒收数量
                        snglSub.setMemo(gdsSnglSubs.getMemo());
                        snglSub.setNoTaxAmt(snglSub.getNoTaxUprc().multiply(snglSub.getQty()));// 无税金额
                        snglSub.setPrcTaxSum(snglSub.getCntnTaxUprc().multiply(snglSub.getQty()));// 价税合计
                        snglSub.setTaxAmt(snglSub.getNoTaxAmt().multiply(snglSub.getTaxRate()));// 税额

                        snglSub.setIsNtRtnGoods(0);
                        snglSub.setOrdrNum(null);
                        tSnglSubLists.add(snglSub);
                        intoWhsSubs.add(intoWhsSub);
                    } else {
                        // System.out.println("单据号序号不存在");
                    }
                }
            }

            if (intoWhsSubs.size() > 0) {
                intoWhsSubDao.insertIntoWhsSub(intoWhsSubs);
            }
            // 到货拒收单子
            if (tSnglSubLists.size() > 0) {
                toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubLists);
            }
            if (toGdsSngls != null) {
                toGdsSnglServiceImpl.updateReturnToGdsSnglIsNtChkOK(toGdsSngls);
            }

            String number21 = null;

            for (IntoWhsSub intoWhsSub : intoWhsSubList) {
                // 入库单子表
                InstIntoWhs(number, intoWhsSub);
                if ((intoWhsSub.getReturnQty() == null ? BigDecimal.ZERO : intoWhsSub.getReturnQty())
                        .compareTo(BigDecimal.ZERO) > 0) {
                    // 3.拒收单
                    if (number21 == null) {
                        number21 = aInstToGds(toGdsSngl, userId, userName, toGdsSngl);
                    }
                }

            }
            InstMovBitTab(movBitTab);
            // System.out.println(number21);
            if (number21 != null) {
                InstToGds(toGdsSngl, number21, tSnglSubList);
            }

//				for (IntoWhsSub intoWhsSub : intoWhsSubList) {
//					if (intoWhsSub.getReturnQty().compareTo(BigDecimal.ZERO) > 0) {
//						// 则有入库单和拒收单---------------------
//						// 入库单子表
//						InstIntoWhs(number, intoWhsSub);
//						// 2.移位表
//						InstMovBitTab(movBitTab);
//						// 3.拒收单
//						InstToGds(, tSnglSubList, userId, userName);
//					} else {
//						// 入库单-------------
//						// 入库单主表
//						InstIntoWhs(number, intoWhsSub);
//						// (入库的时候如果存货本身有增在所在货位上增加，如果没有则进行添加)
//						InstMovBitTab(movBitTab);
//					}
//				}
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
        return userName;
    }

    private String aInstIntoWhs(IntoWhs intoWhs, String userId, String userName, ToGdsSngl toGdsSngl) throws Exception {
        String number = null;
        try {
            number = getOrderNo.getSeqNo("RK", userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            // 此处需要跳出，抛出异常，带回错误结果；

            throw new Exception("无法获取到入库单号！" + e.getMessage());
        } // 获取订单号
        // 新增入库单
        // System.out.println(number);
        intoWhs.setIntoWhsSnglId(number);// 单据号
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String intoWhsDt = sdf.format(date);
        intoWhs.setIntoWhsDt(intoWhsDt);// 入库日期
        intoWhs.setPursTypId(toGdsSngl.getPursTypId());// 采购类型编号
        intoWhs.setProvrId(toGdsSngl.getProvrId());// 供应商编号
        intoWhs.setRecvSendCateId("1");// 收发类别编码 采购入库
        intoWhs.setOutIntoWhsTypId("9");// 其他出入库类别编码
        intoWhs.setAccNum(userId);// 业务员编码
        intoWhs.setUserName(userName);// 操作人
        intoWhs.setSetupPers(userName);// 创建人
        intoWhs.setPursOrdrId(toGdsSngl.getPursOrdrId());// 采购订单编号
        intoWhs.setToGdsSnglId(toGdsSngl.getToGdsSnglId());// 到货单编号
        intoWhs.setProvrOrdrNum(toGdsSngl.getProvrOrdrNum());// 供应商订单号
        intoWhs.setFormTypEncd("004");
        intoWhs.setDeptId(toGdsSngl.getDeptId());// 部门编号
        intoWhs.setIsNtRtnGood(0);// 是否有退货
        intoWhs.setToFormTypEncd(toGdsSngl.getFormTypEncd());
//		intoWhs.setExaRep(toGdsSngl.getexaRep);//检验检疫报告
        // 新增主表信息
        intoWhsDao.insertIntoWhs(intoWhs);

        return number;

    }

    private void InstIntoWhs(String number, IntoWhsSub intoWhsSub) throws Exception {

        intoWhsSub.setIntoWhsSnglId(number);// 入库单据号
        intoWhsSub.setIsNtRtnGoods(1);
//			BigDecimal dQty = BigDecimal.ZERO;
//			for (MovBitTab mTab : movBitTab) {
//				dQty = dQty.add(mTab.getQty());
//			}
        // System.out.println("intoWhsSub.getQty(dQty);" + intoWhsSub.getQty());
        // intoWhsSub.setQty(dQty);
//			// System.out.println("intoWhsSub.setQty(dQty);"+dQty);

        List<IntoWhsSub> lst = new ArrayList<IntoWhsSub>();
        lst.add(intoWhsSub);
        // 插入入库单子表
        intoWhsSubDao.insertIntoWhsSub(lst);
    }

    // 添加移位表数据
    private void InstMovBitTab(List<MovBitTab> movBitTab) {
        for (MovBitTab mTab : movBitTab) {
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

//			// (入库的时候如果存货本身有增在所在货位上增加，如果没有则进行添加)
//			if (invtyNumMapper.selectAllMbit(movBitTab) != null) {
//
//				invtyNumMapper.updateMovbitTabJia(movBitTab);// 修改移位表(增加)
//			} else {
//				// 新增移位表 寻找区域
//				for (MovBitTab mTab : movBitTab) {
//					List<GdsBit> gList = invtyNumMapper.selectRegn(mTab.getGdsBitEncd());
//					for (GdsBit gdsBit : gList) {
//						mTab.setRegnEncd(gdsBit.getRegnEncd());// 区域
//						movBitTab.add(mTab);
//					}
//				}
//				invtyNumMapper.insertMovBitTab(movBitTab);// 增加移位表
//			}
    }

    // 到货拒收单主
    private String aInstToGds(ToGdsSngl toGdsSngl, String userId, String userName, ToGdsSngl toGdsSngls)
            throws Exception {
        String number = null;
        try {
            number = getOrderNo.getSeqNo("DH", userId,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        } // 获取订单号
        BeanUtils.copyProperties(toGdsSngls, toGdsSngl);

        // 到货拒收单主
        toGdsSngl.setToGdsSnglId(number);// 订单号
        Date date1 = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String toGdsSnglDt = sdf1.format(date1);
        toGdsSngl.setToGdsSnglDt(toGdsSnglDt);// 拒收单日期
        toGdsSngl.setAccNum(userId);// 业务员编号
        toGdsSngl.setUserName(userName);// 业务员名称
        toGdsSngl.setSetupPers(userName);// 创建人
        toGdsSngl.setFormTypEncd("003");// 单据类型
        toGdsSngl.setMemo(toGdsSngls.getMemo());
        toGdsSngl.setIsNtRtnGood(1);
        toGdsSnglDao.insertToGdsSngl(toGdsSngl);
        // System.out.println(number);
        return number;

    }

    // 到货拒收单子
    private void InstToGds(ToGdsSngl toGdsSngl, String number1, List<ToGdsSnglSub> tSnglSubList) throws Exception {
        // 到货拒收单子表
        for (ToGdsSnglSub tSnglSub : tSnglSubList) {
            tSnglSub.setToGdsSnglId(number1);
            tSnglSub.setPursOrdrNum(toGdsSngl.getPursOrdrId());// 采购订单号
            tSnglSub.setMemo(tSnglSub.getReturnMemo());// 备注
            BigDecimal rQty = tSnglSub.getReturnQty();
//				BigDecimal rFu = new BigDecimal("-1");
//				double cheng = rQty.multiply(rFu).doubleValue();
//				BigDecimal returnQty = new BigDecimal(cheng);
//				tSnglSub.setQty(returnQty);// 拒收数量
            tSnglSub.setQty(rQty);// 拒收数量
            // 未税金额
            tSnglSub.setCntnTaxUprc(CalcAmt.noTaxAmt(tSnglSub.getNoTaxUprc(), tSnglSub.getReturnQty()));
            // 含税单价
            tSnglSub.setCntnTaxUprc(
                    CalcAmt.cntnTaxUprc(tSnglSub.getNoTaxUprc(), tSnglSub.getReturnQty(), tSnglSub.getTaxRate()));
            // 含税金额(价税合计)
            tSnglSub.setPrcTaxSum(
                    CalcAmt.prcTaxSum(tSnglSub.getNoTaxUprc(), tSnglSub.getReturnQty(), tSnglSub.getTaxRate()));
            tSnglSub.setIsNtRtnGoods(0);
        }

        toGdsSnglSubDao.insertToGdsSnglSub(tSnglSubList);
    }

    // 查询销售出库单
    @Override
    public String selectSellOutWhsList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<SellOutWhs> sellOutWhs = intoWhsMapper.selectSellOutWhsList(list);

        // **************推荐出库货位*****************

        for (SellOutWhs sOutWhs : sellOutWhs) {
            List<SellOutWhsSub> sList = sOutWhs.getsList();
            for (SellOutWhsSub sub : sList) {

                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(sub.getWhsEncd());
                movBitTab.setInvtyEncd(sub.getInvtyEncd());
                movBitTab.setBatNum(sub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                sub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (sellOutWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectSellOutWhsList", true, "查询成功！", null,
                        sellOutWhs);
            } else {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectSellOutWhsList", false, "暂无数据", null,
                        sellOutWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    /**
     * 销售出库 审核状态已审
     */
    @Override
    public String updatesOutWhs(SellOutWhs sellOutWhs, List<SellOutWhsSub> sellOutWhsSub, List<InvtyTab> invtyTab,
                                List<MovBitTab> movBitTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(sellOutWhs.getOutWhsId());
        if (bitTabs.size() > 0) {
            List<String> lists = new ArrayList<String>();
            lists.add(sellOutWhs.getOutWhsId());
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

        bitListMapper.insertInvtyGdsBitLists(movBitTab);
        message = "成功";

        try {
            sellOutWhsServiceImpl.updateSellOutWhsIsNtChk(sellOutWhs);
        } catch (Exception e2) {

            throw new RuntimeException(e2.getMessage());
        }
        if (true) {
            try {
                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", isSuccess, message, null);
                return resp;
            } catch (IOException e) {


            }
        }

        // 修改审核状态
        int a = intoWhsMapper.updateSOutWhs(sellOutWhs);// 修改状态
        if (a >= 1) {
            Boolean enough = true;
            for (SellOutWhsSub sWhsSub : sellOutWhsSub) {
                for (InvtyTab iTab : invtyTab) {
                    InvtyTab inTab = invtyNumMapper.selectInvtyTabByTerm(iTab);

                    // System.out.println("inTab.getNowStok()" + inTab.getNowStok() + "\tsWhsSub.getQty()"
//                            + sWhsSub.getQty() + "\tinTab.getNowStok()" + inTab.getNowStok() + "\tsWhsSub.getQty()"
//                            + sWhsSub.getQty());
                    if (inTab.getNowStok().compareTo(sWhsSub.getQty()) == 1
                            || inTab.getNowStok().compareTo(sWhsSub.getQty()) == 0) {
                        isSuccess = true;
                        message = "出库成功！";
//						enough=true;
                    }

                    if (inTab.getNowStok().compareTo(sWhsSub.getQty()) == -1) {
                        isSuccess = false;
                        message = "库存中数量不足";
                        enough = false;
                        try {
                            throw new RuntimeException("库存中数量不足(有误？)");
                        } catch (Exception e) {

                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

                            try {
                                resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", true, message, null);
                            } catch (IOException e1) {

                            }
                            return resp;
                        }

                    }
                }
            }

            if (enough) {
                // System.out.println("没有更改金额       sql 没写？？");
                invtyNumMapper.updateInvtyTab(invtyTab);// 现存量减少
                // 修改移位数量(减少)
                invtyNumMapper.updateMovbitTab(movBitTab);// 修改移位表(减少)
            }

        } else {
            isSuccess = false;
            message = "出库失败！";
        }

        try {
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesOutWhs", true, message, null);
        } catch (IOException e) {

        }
        return resp;
    }

    // 拒收原因
    @Override
    public String selectReason() {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<RefuseReason> reasons = intoWhsMapper.selectReason();
        if (reasons.size() > 0) {
            isSuccess = true;
            message = "查询拒收原因成功";
            try {
                resp = BaseJson.returnRespObjList("whs/pda_into_whs/selectReason", isSuccess, message, null, reasons);
            } catch (IOException e) {

            }

        } else {
            isSuccess = false;
            message = "查询拒收原因失败";
            try {
                resp = BaseJson.returnRespObjList("whs/pda_into_whs/selectReason", isSuccess, message, null, reasons);
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

    // 查询销售红字出库单
    @Override
    public String selectRedSellOutWhsList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<SellOutWhs> sellOutWhs = intoWhsMapper.selectRedSellOutWhsList(list);

        // **************推荐红出（入）库货位*****************

        for (SellOutWhs sOutWhs : sellOutWhs) {
            List<SellOutWhsSub> sList = sOutWhs.getsList();
            for (SellOutWhsSub sub : sList) {
                sub.setQty(sub.getQty().abs());
                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(sub.getWhsEncd());
                movBitTab.setInvtyEncd(sub.getInvtyEncd());
                movBitTab.setBatNum(sub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectIntogBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                sub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (sellOutWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectRedSellOutWhsList", true, "查询成功！", null,
                        sellOutWhs);
            } else {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectRedSellOutWhsList", false, "暂无数据", null,
                        sellOutWhs);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    /**
     * 红字销售出库 审核状态已审
     */
    @Override
    public String updatesRedOutWhs(SellOutWhs sellOutWhs, List<MovBitTab> movBitTab) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(sellOutWhs.getOutWhsId());
        if (bitTabs.size() > 0) {
            List<String> lists = new ArrayList<String>();
            lists.add(sellOutWhs.getOutWhsId());
            bitListMapper.deleteInvtyGdsBitList(lists);
        }

        bitListMapper.insertInvtyGdsBitLists(movBitTab);
        message = "成功";

        try {
            sellOutWhsServiceImpl.updateSellOutWhsIsNtChk(sellOutWhs);
        } catch (Exception e2) {
            throw new RuntimeException(e2.getMessage());
        }

        try {
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", isSuccess, message, null);
        } catch (IOException e1) {


        }

        return resp;
    }

    //		 查询红字入库单
    @Override
    public String selectIntoWhsList(String whsEncd) {
        String resp = "";
        List<String> list = getList(whsEncd);

        List<IntoWhs> intoWhs = intoWhsMapper.selectIntoWhsList(list);

        // **************推荐出库货位*****************

        for (IntoWhs into : intoWhs) {
            List<IntoWhsSub> sList = into.getIntoWhsSub();
            for (IntoWhsSub sub : sList) {
                sub.setQty(sub.getQty().abs());
                MovBitTab movBitTab = new MovBitTab();
                movBitTab.setWhsEncd(sub.getWhsEncd());
                movBitTab.setInvtyEncd(sub.getInvtyEncd());
                movBitTab.setBatNum(sub.getBatNum());
                List<MovBitTab> mBitTab = intoWhsMapper.selectOutgBit(movBitTab);
                String s = "";
                for (MovBitTab mTab : mBitTab) {
                    s += mTab.getGdsBitEncd() + ",";
                }
                if (s.length() > 2) {
                    s.substring(s.length() - 2);
                }
                sub.setGdsBitEncd(s);
            }
        }
        // *******************************

        try {
            if (intoWhs.size() > 0) {
                resp = BaseJson.returnRespObjList("/whs/pda_into_whs/selectIntoWhsList", true, "查询成功！", null, intoWhs);
            } else {
                resp = BaseJson.returnRespObj("/whs/pda_into_whs/selectIntoWhsList", false, "暂无数据", null);
            }

        } catch (IOException e) {

        }
        return resp;
    }

    @Override
    public String updatesRedIntoWhs(IntoWhs intoWhs, List<MovBitTab> list) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";

        List<MovBitTab> bitTabs = bitListMapper.selectInvtyGdsBitListf(intoWhs.getIntoWhsSnglId());
        if (bitTabs.size() > 0) {
//			List<String> lists = new ArrayList<String>();
//			lists.add(sellOutWhs.getOutWhsId());
            bitListMapper.deleteInvtyGdsBitList(Arrays.asList(intoWhs.getIntoWhsSnglId()));
        }

        bitListMapper.insertInvtyGdsBitLists(list);
        message = "成功";

        try {
            purcIntoWhsServiceImpl.updateIntoWhsIsNtChk(intoWhs);
        } catch (Exception e2) {

            throw new RuntimeException(e2.getMessage());
        }

        try {
            resp = BaseJson.returnRespObj("whs/pda_into_whs/updatesRedOutWhsPda", isSuccess, message, null);
        } catch (IOException e1) {

        }

        return resp;
    }
}
