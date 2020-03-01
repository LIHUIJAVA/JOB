package com.px.mis.ec.service.impl;

import com.px.mis.ec.dao.*;
import com.px.mis.ec.entity.*;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.SalesPromotionActivityUtil;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.px.mis.whs.service.InvtyTabService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PlatOrderDownloadServiceImpl {

    @Autowired
    private PlatOrderDao platOrderDao;
    @Autowired
    private PlatOrdersDao platOrdersDao;
    @Autowired
    private CouponDetailDao couponDetailDao;
    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private StoreRecordDao storeRecordDao;
    @Autowired
    private AuditStrategyService auditStrategyService;
    @Autowired
    private AssociatedSearchService associatedSearchService;
    @Autowired
    private GoodRecordDao goodRecordDao;
    @Autowired
    private InvtyDocDao invtyDocDao;
    @Autowired
    private ProdStruMapper prodStruDao;// 产品结构
    @Autowired
    private LogRecordDao logRecordDao;
    @Autowired
    private MisUserDao misUserDao;
    @Autowired
    private LogisticsTabServiceImpl logisticsTabServiceImpl;
    @Autowired
    private OrderDealSettingsServiceImpl orderDealSettingsServiceImpl;
    @Autowired
    private InvtyTabService invtyTabService;
    @Autowired
    private InvtyNumMapper invtyNumMapper;
    @Autowired
    private SalesPromotionActivityUtil salesPromotionActivityUtil;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private PlatOrderPaymethodDao paymethodDao; //支付明细
    @Autowired
    private GetOrderNo getOrderNo;

    /**
     * 批量插入数据库 and 自动匹配
     * 目前只做了  JD  TM
     *
     * @param platOrderList
     * @param invoiceList
     * @param couponList
     * @param payMentList
     * @param downloadCount
     * @param userId
     * @return
     */
    public int DownliadOrder(List<PlatOrder> platOrderList,
                             List<InvoiceInfo> invoiceList,
                             List<CouponDetail> couponList,
                             List<PlatOrderPaymethod> payMentList,
                             int downloadCount,
                             String userId,
                             List<String> seqNoList
    ) {
        //主表非空
        if (platOrderList == null || platOrderList.size() == 0) {
        } else {
            int i = 0;
            for (PlatOrder platOrder : platOrderList) {
                //生成订单号
//                String orderId = getOrderNo.getSeqNo("ec", userId);
                String orderId = seqNoList.get(i);
                //赋值主表订单号
                platOrder.setOrderId(orderId);
                //拿到主表中子表集合
                for (PlatOrders platOrders : platOrder.getPlatOrdersList()) {
                    //赋值子表订单号
                    platOrders.setOrderId(orderId);
                }
                //数据库插入子表
                platOrdersDao.insert(platOrder.getPlatOrdersList());//plat_orders
                i++;
            }
        }


        if (platOrderList == null || platOrderList.size() == 0) {
        } else {
            //数据库插入主表
            platOrderDao.insertList(platOrderList);//plat_order
//            platOrdersDao.insert(platOrdersList);//plat_orders
        }

        if (invoiceList == null || invoiceList.size() == 0) {
        } else {
            invoiceDao.insert(invoiceList);//invoice_info
        }

        if (couponList == null || couponList.size() == 0) {
        } else {
            couponDetailDao.insert(couponList);//coupon_detail
        }

        if (payMentList == null || payMentList.size() == 0) {
        } else {
            paymethodDao.insert(payMentList);//plat_order_paymethod
        }
        //计数
        downloadCount += platOrderList.size();

        // 订单插入完成后，判断是否自动免审
        for (int i = 0; i < platOrderList.size(); i++) {
            //执行自动匹配
            autoMatch(platOrderList.get(i).getOrderId(), userId);
        }


        return downloadCount;
    }

    /**
     * 自动匹配
     *
     * @param orderId
     * @param accNum
     * @return
     */
    public String autoMatch(String orderId, String accNum) {
        // TODO Auto-generated method stub
        boolean isSuccess = true;
        String resp = "";
        String[] orderIds = orderId.split(",");
        int successCount = 0;
        for (int i = 0; i < orderIds.length; i++) {

            PlatOrder platOrder = new PlatOrder();
            platOrder = platOrderDao.select(orderIds[i]);
            java.util.List<PlatOrders> platOrdersList = new ArrayList<PlatOrders>();
            platOrdersList = platOrdersDao.select(orderIds[i]);
            java.util.List<PlatOrders> orderslist = new ArrayList<PlatOrders>();
            boolean flag = true;
            if (platOrder.getIsAudit() == 0) {//订单未审核才可以执行自动匹配
                int jishu = 0;//声明一个计数项，当循环订单明细失败时，用于记录当前循环到第几个明细了

                // 循环匹配商品档案  完成
                for (int j = 0; j < platOrdersList.size(); j++) {
                    jishu = j;//给计数项赋值当前循环数
                    DecimalFormat dFormat = new DecimalFormat("0.00");

                    PlatOrders platOrders = platOrdersList.get(j);

                    if (StringUtils.isNotEmpty(platOrders.getInvId())) {
                        // 若当前条订单明细中存货编码不为空则说明匹配到商品档案了，不需要执行自动匹配
                        orderslist.add(platOrders);
                        continue;
                    }
                    // 存货编码 平台商品表good_record中的平台商品编码；
                    String ecgoodid = platOrders.getGoodId();// 平台商品编号
                    String goodsku = platOrders.getGoodSku();
                    GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
                    flag = record(goodRecord, platOrder, platOrders, orderslist, dFormat, flag);
                }

                // 匹配促销  完成
                if (flag && orderslist.size() > 0 && platOrder.getCanMatchActive() == null) {
                    platOrder.setPlatOrdersList(orderslist);
                    int size1 = orderslist.size();
                    platOrder = salesPromotionActivityUtil.matchSalesPromotions(platOrder); //匹配促销方案
                    orderslist = platOrder.getPlatOrdersList();
                    int size2 = orderslist.size();
                    platOrderDao.update(platOrder);//更新主表
                    if (size1 < size2) {
                        // 删除原来订单明细子表添加新的
                        insertOrders(platOrder, orderslist);

                    }

                }

                // 匹配仓库 = 自发货 + 货仓为空
                if (flag) {
                    if (platOrder.getDeliverSelf() == 1 && StringUtils.isEmpty(platOrder.getDeliverWhs())) {
                        // 全部商品匹配完成后匹配仓库 若订单为自发货订单且发货仓库为空时则匹配
                        StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
                        if (orderslist.size() == 0) {
                            orderslist = platOrdersDao.select(platOrder.getOrderId());
                        }
                        platOrder.setPlatOrdersList(orderslist);
                        Map map2 = orderDealSettingsServiceImpl.matchWareHouse(platOrder, storeRecord.getEcId());
                        if (map2.get("isSuccess").toString().equals("true")) {
                            platOrder.setDeliverWhs(map2.get("whsCode").toString());
                            for (int j = 0; j < orderslist.size(); j++) {
                                orderslist.get(i).setDeliverWhs(map2.get("whsCode").toString());
                            }
                        } else {
                            platOrder.setAuditHint(map2.get("message").toString());
                            // 日志记录

                            flag = false;

                        }

                    }
                }

                // 匹配快递公司
                if (flag) {
                    // 匹配仓库成功后匹配快递公司
                    // 跟据订单中的仓库编码和平台id以及存货档案明细来匹配快递公司
                    if (platOrder.getDeliverSelf() == 1 && StringUtils.isEmpty(platOrder.getExpressCode())) {
                        Map map3 = logisticsTabServiceImpl.findExpressCodeByOrderId(platOrder, platOrdersList);
                        if (map3.get("flag").toString().equals("true")) {
                            try {
                                platOrder.setExpressCode(map3.get("expressCode").toString());
                                platOrder.setExpressTemplate(map3.get("templateId").toString());
                            } catch (Exception e) {
                                StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
                                if (storeRecord.getEcId().equals("PDD") || storeRecord.getEcId().equals("TM")) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();

                                } else {
                                    platOrder.setAuditHint("请检查仓库平台快递公司设置对应模板");
                                    flag = false;
                                }
                            }
                        } else {
                            flag = false;
                            platOrder.setAuditHint(map3.get("message").toString());
                        }

                    }
                }

                // 虚拟商品移除占库存（带走插入DB后）
                if (flag) {
                    for (int j = 0; j < orderslist.size(); j++) {
                        InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(orderslist.get(j).getInvId());
                        if (invtyDoc.getShdTaxLabour() != null) {
                            if (invtyDoc.getShdTaxLabour() == 1) {//为1的是虚拟商品
                                orderslist.remove(j);
                                j--;
                                if (orderslist.size() == 0) {
                                    break;
                                }
                            } else {
                                //存储分配批次成功后返回来的list以及可用量不足的list
                                java.util.List<PlatOrders> platOrders123 = new ArrayList<PlatOrders>();
                                for (int z = 0; z < orderslist.size(); z++) {
                                    if (StringUtils.isEmpty(orderslist.get(z).getBatchNo())) {//如果当前条明细批次字段为空
                                        //同步锁防止超卖
                                        synchronized (this) {
                                            // 验证可用量及分配批次
                                            Map map4 = checkCanUseNumAndBatch(orderslist.get(z));   //   占库存   可后移
                                            if (map4.get("flag").toString().equals("true")) {
                                                // 分配批次成功
                                                java.util.List<PlatOrders> platOrders456 = (List<PlatOrders>) map4.get("platOrders");
                                                for (int k = 0; k < platOrders456.size(); k++) {
                                                    platOrders123.add(platOrders456.get(k));
                                                }

                                            } else {
                                                flag = false;//没分配成功时，将当前条明细复制到新list
                                                platOrder.setAuditHint(map4.get("message").toString());
                                                platOrders123.add(orderslist.get(z));
                                            }
                                        }
                                    } else {
                                        platOrders123.add(orderslist.get(z));
                                    }
                                }
                                if (platOrders123.size() > 0) {
                                    orderslist.clear();
                                    orderslist.addAll(platOrders123);
                                }
                            }
                        } else {
                            platOrder.setAuditHint("请检查存货：" + invtyDoc.getInvtyEncd() + "是否应税劳务属性");
                            flag = false;
                            break;
                        }
                    }
                }

                // 判断正常商品是否全部正常匹配完成
                if (orderslist.size() > 0) {
                    if (platOrdersList.size() == jishu + 1) {
                        //当jishu+1等于原订单明细的size时,说明正常匹配完商品
                    } else {
                        //非正常匹配商品时，需要把剩余部分加到列表中待下一次进行匹配
                        for (int j = jishu; j < platOrdersList.size(); j++) {
                            orderslist.add(platOrdersList.get(j));
                        }
                    }
                    // 删除原来订单明细子表添加新的
                    insertOrders(platOrder, orderslist);
                }

                // 匹配完成 --> 记录日志 --> 走自动免审（带走插入DB后） --> 响应成功条数
                if (flag) {

                    successCount++;
                    int goodCount = 0;
                    if (orderslist.size() > 0) {
                        for (int j = 0; j < orderslist.size(); j++) {
                            goodCount += orderslist.get(j).getInvNum();
                        }
                        platOrder.setGoodNum(goodCount);// 更新订单商品数量
                    }
                    platOrder.setAuditHint("");// 清空审核提示
                    platOrderDao.update(platOrder);// 更新订单的审核提示；
                    insertLog2(accNum, platOrder);//记录日志

                    // 订单插入完成后，判断是否自动免审  //独立  带到插入DB操作之后
                    isTrial(platOrder, orderslist);

                } else {
                    //记录日志
                    InsertLog(accNum, platOrder);
                }

            }

        }

        //响应resp
        try {
            resp = BaseJson.returnRespObj("ec/platOrder/autoMatch", isSuccess,
                    "本次自动匹配成功" + successCount + "条订单，失败" + (orderIds.length - successCount) + "条订单", null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return resp;
    }


    //记录日志
    private void insertLog2(String accNum, PlatOrder platOrder) {
        // 日志记录
        LogRecord logRecord = new LogRecord();
        logRecord.setOperatId(accNum);
        MisUser misUser = misUserDao.select(accNum);
        if (misUser != null) {
            logRecord.setOperatName(misUser.getUserName());
        }
        logRecord.setOperatContent("自动匹配成功");
        logRecord.setOperatTime(sdf.format(new Date()));
        logRecord.setOperatType(11);// 11自动匹配
        logRecord.setTypeName("自动匹配");
        logRecord.setOperatOrder(platOrder.getEcOrderId());
        logRecordDao.insert(logRecord);
    }

    //记录日志
    private void InsertLog(String accNum, PlatOrder platOrder) {
        // 日志记录
        LogRecord logRecord = new LogRecord();
        logRecord.setOperatId(accNum);
        MisUser misUser = misUserDao.select(accNum);
        if (misUser != null) {
            logRecord.setOperatName(misUser.getUserName());
        }
        logRecord.setOperatContent("自动匹配失败：" + platOrder.getAuditHint());
        logRecord.setOperatTime(sdf.format(new Date()));
        logRecord.setOperatType(11);// 11自动匹配
        logRecord.setTypeName("自动匹配");
        logRecord.setOperatOrder(platOrder.getEcOrderId());
        logRecordDao.insert(logRecord);
        platOrderDao.update(platOrder);// 更新订单的审核提示；
    }

    //判断是否自动免审
    private void isTrial(PlatOrder platOrder, List<PlatOrders> orderslist) {
        if (auditStrategyService.autoAuditCheck(platOrder, orderslist)) {
            // 返回true时，此订单通过免审，直接进入审核
            associatedSearchService.orderAuditByOrderId(platOrder.getOrderId(), "sys", sdf.format(new Date()));
            // 设置默认操作员sys
        }
    }

    //// 删除原来订单明细子表添加新的
    private void insertOrders(PlatOrder platOrder, List<PlatOrders> orderslist) {
        // 删除原来订单明细子表
        platOrdersDao.delete(platOrder.getOrderId());
        // 新增自动匹配拆解过后的订单明细子表
        platOrdersDao.insert(orderslist);
    }

    /**
     * 库存可用量
     *
     * @param platOrders
     * @return
     */
    @Transactional
    public Map checkCanUseNumAndBatch(PlatOrders platOrders) {
        List<PlatOrders> platOrdersNew = new ArrayList<PlatOrders>();
        String message = "";
        // 验证可用量及分配批次信息
        boolean flag = true;

        InvtyTab invtyTab = new InvtyTab();
        invtyTab.setAvalQty(new BigDecimal(platOrders.getInvNum()));// 子表数量
        invtyTab.setWhsEncd(platOrders.getDeliverWhs());// 设置仓库编码
        invtyTab.setInvtyEncd(platOrders.getInvId());// 存货编码
        List<InvtyTab> tablist = invtyTabService.selectInvtyTabByBatNum(invtyTab);// 获取批次列表
        if (tablist.size() == 0) {
            // 对应存货可用量不足
            flag = false;
            message = platOrders.getInvId() + "可用量不足";
        } else {
            if (tablist.size() == 1) {
                // 当返回的批次信息只有一条时，直接更新本条的批次及效期信息
                invtyTab = tablist.get(0);
                platOrders.setBatchNo(invtyTab.getBatNum());// 批号
                platOrders.setPrdcDt(invtyTab.getPrdcDt() == null ? null : invtyTab.getPrdcDt());// 生产日期；
                /*
                 * String baoZhiQi = invtyTab.getBaoZhiQi();
                 * if(baoZhiQi!=null||!baoZhiQi.equals("")) {
                 * platOrders.get(i).(Integer.valueOf(baoZhiQi));//保质期 }
                 */
                platOrders.setInvldtnDt(invtyTab.getInvldtnDt() == null ? null : invtyTab.getInvldtnDt());// 失效日期；
                platOrdersNew.add(platOrders);
            } else {
                // 当返回的批次信息不止一条时，循环生成明细数据
                int numCount = platOrders.getInvNum();// 当前条明细存货总数量
                int hasNum = 0;// 已分配数
                BigDecimal hasMoney = BigDecimal.ZERO;// 已分配批次的金额
                for (int j = 0; j < tablist.size(); j++) {
                    // 创建新的子表
                    PlatOrders platOrders2 = new PlatOrders();
                    try {
                        platOrders2 = (PlatOrders) platOrders.clone();
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        flag = false;
                        message = "匹配可用量时系统异常，请联系管理员";
                    }
                    if (j + 1 < tablist.size()) {
                        platOrders2.setBatchNo(tablist.get(j).getBatNum());// 批次
                        platOrders2.setPrdcDt(invtyTab.getPrdcDt() == null ? null : invtyTab.getPrdcDt());// 生产日期
                        /*
                         * if(tablist.get(j).getBaoZhiQi()!=null||!tablist.get(j).getBaoZhiQi().equals(
                         * "")) { sub2.setBaoZhiQi(Integer.valueOf(tablist.get(j).getBaoZhiQi()));//保质期
                         * }
                         */
                        platOrders2.setInvldtnDt(
                                tablist.get(j).getInvldtnDt() == null ? null : tablist.get(j).getInvldtnDt());// 失效日期
                        platOrders2.setInvNum(tablist.get(j).getAvalQty().intValue());// 设置当前条明细的数量
                        platOrders2.setPayMoney(
                                platOrders2.getPayPrice().multiply(new BigDecimal(platOrders2.getInvNum())));// 设置实付金额
                        platOrders2.setCanRefMoney(platOrders2.getPayMoney());// 设置可退金额
                        platOrders2.setCanRefNum(platOrders2.getInvNum());// 设置可退数量
                        hasMoney = hasMoney.add(platOrders2.getPayMoney());// 累加计算金额 最后用减法
                        hasNum = hasNum + platOrders2.getInvNum();// 累加已分配数量
                        platOrdersNew.add(platOrders2);
                    } else {
                        // 最后一个批次时
                        platOrders2.setBatchNo(tablist.get(j).getBatNum());// 批次
                        platOrders2.setPrdcDt(invtyTab.getPrdcDt() == null ? null : invtyTab.getPrdcDt());// 生产日期
                        /*
                         * if(tablist.get(j).getBaoZhiQi()!=null||!tablist.get(j).getBaoZhiQi().equals(
                         * "")) { sub2.setBaoZhiQi(Integer.valueOf(tablist.get(j).getBaoZhiQi()));//保质期
                         * }
                         */
                        platOrders2.setInvldtnDt(
                                tablist.get(j).getInvldtnDt() == null ? null : tablist.get(j).getInvldtnDt());// 失效日期
                        platOrders2.setInvNum(platOrders.getInvNum() - hasNum);// 设置当前条明细的数量
                        platOrders2.setPayMoney(platOrders.getPayMoney().subtract(hasMoney));// 设置实付金额
                        platOrders2.setCanRefMoney(platOrders2.getPayMoney());// 设置可退金额
                        platOrders2.setCanRefNum(platOrders2.getInvNum());// 设置可退数量
                        platOrdersNew.add(platOrders2);
                    }
                }
            }

        }
        Map map = new HashMap<>();
        map.put("flag", flag);
        if (flag) {
            // 明细分配完批次后，扣减对应批次的可用量 public int updateAInvtyTab(List<InvtyTab>
            // invtyTab);//可用量减少(减法)
            List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
            for (int k = 0; k < platOrdersNew.size(); k++) {
                InvtyTab invtyTab1 = new InvtyTab();
                invtyTab1.setAvalQty(new BigDecimal(platOrdersNew.get(k).getInvNum()));
                invtyTab1.setWhsEncd(platOrdersNew.get(k).getDeliverWhs());
                invtyTab1.setBatNum(platOrdersNew.get(k).getBatchNo());
                invtyTab1.setInvtyEncd(platOrdersNew.get(k).getInvId());
                invtyTabs.add(invtyTab1);
            }
            invtyNumMapper.updateAInvtyTab(invtyTabs);
            map.put("platOrders", platOrdersNew);
        } else {
            map.put("message", message);
        }

        return map;

    }


    //档案
    public Boolean record(GoodRecord goodRecord, PlatOrder platOrder, PlatOrders platOrders, List<PlatOrders> orderslist, DecimalFormat dFormat, Boolean flag) {
        String name = Thread.currentThread().getName();
        System.out.println(name);
        if (goodRecord == null) {
            platOrder.setAuditHint("在店铺商品档案中未匹配到对应商品档案");
            platOrderDao.update(platOrder);// 更新订单的审核提示；
            orderslist.add(platOrders);
            flag = false;
            return flag;
        } else if (StringUtils.isEmpty(goodRecord.getGoodId())) {
            platOrder.setAuditHint("请完善店铺商品档案中存货编码的对应关系");
            platOrderDao.update(platOrder);// 更新订单的审核提示；
            orderslist.add(platOrders);
            flag = false;
            return flag;
        } else {
            String invId = goodRecord.getGoodId();
            InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// 去存货档案查询存货基本信息
            if (invtyDoc == null) {
                platOrder.setAuditHint("在存货档案中未匹配到对应存货档案,需匹配的存货编码：" + invId);
                platOrderDao.update(platOrder);// 更新订单的审核提示；
                orderslist.add(platOrders);
                flag = false;
                return flag;
            } else {
                if (invtyDoc.getPto() != null) {
                    if (invtyDoc.getPto() == 1) {
                        // 属于PTO类型存货
                        ProdStru prodtru = prodStruDao.selectMomEncd(invId);
                        if (prodtru == null) {
                            // 未在产品结构中查询到对应存货的产品结构信息
                            platOrder.setAuditHint("在产品结构：" + invId + "中未匹配到对应产品结构信息");
                            platOrderDao.update(platOrder);// 更新订单的审核提示；
                            orderslist.add(platOrders);
                            flag = false;
                            return flag;
                        } else {
                            if (prodtru.getIsNtChk() == 0) {
                                // 对应产品结构尚未审核
                                platOrder.setAuditHint("对应产品结构：" + prodtru.getMomEncd() + "尚未审核");
                                platOrderDao.update(platOrder);// 更新订单的审核提示；
                                orderslist.add(platOrders);
                                flag = false;
                                return flag;
                            } else {
                                java.util.List<ProdStruSubTab> strucksublist = prodtru.getStruSubList();
                                // 循环产品结构子表信息
                                if (strucksublist.size() == 0) {
                                    platOrder.setAuditHint("对应产品结构：" + prodtru.getMomEncd() + "没有设置子件明细，请先设置。");
                                    platOrderDao.update(platOrder);// 更新订单的审核提示；
                                    orderslist.add(platOrders);
                                    flag = false;
                                    return flag;
                                } else if (strucksublist.size() == 1) {
                                    // 当产品结构明细里面只有一个子件时，直接替换存货编码
                                    // 存货编码
                                    platOrders.setInvId(strucksublist.get(0).getSubEncd());
                                    // 设置存货数量
                                    platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum()).multiply(strucksublist.get(0).getSubQty()).intValue());
                                    // 设置可退数量
                                    platOrders.setCanRefNum(platOrders.getInvNum());
                                    platOrders.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
                                    platOrders.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
                                    // 设置实付单价
                                    platOrders.setPayPrice(platOrders.getPayMoney().divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                                    platOrders.setSellerPrice(platOrders.getPayPrice());// 结算单价与实付单价一样
                                    if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
                                        platOrders.setIsGift(1);
                                        platOrder.setHasGift(1);
                                    } else {// 根据实付单价是否为0判断是否为赠品
                                        platOrders.setIsGift(0);
                                    }
                                    // 设置可退金额,与实付金额一致
                                    platOrders.setCanRefMoney(platOrders.getPayMoney());

                                    orderslist.add(platOrders);
                                } else {
                                    // 当产品结构中子件的数量大于1时，需要生成多条明细

                                    // 计算子件参考成本与子件数量的总成本
                                    // 声明旗标，如果false说明对应子件的参考成本未设置，系统无法拆分比例
                                    BigDecimal total = new BigDecimal("0");
                                    for (int x = 0; x < strucksublist.size(); x++) {
                                        InvtyDoc invtyDoc1 = invtyDocDao.selectInvtyDocByInvtyDocEncd(strucksublist.get(x).getSubEncd());
                                        if (invtyDoc1 == null) {
                                            flag = false;
                                            return flag;
                                        } else if (invtyDoc1.getRefCost() == null) {
                                            flag = false;
                                            return flag;
                                        } else {
                                            total = total.add(invtyDoc1.getRefCost().multiply(strucksublist.get(x).getSubQty()));
                                        }
                                    }
                                    BigDecimal money123 = new BigDecimal("0");// 设置已分实付金额 最后一条用减法时用到
                                    BigDecimal money456 = new BigDecimal("0");// 设置拆分后的商品金额，最后一条减法
                                    for (int x = 0; x < strucksublist.size(); x++) {
                                        PlatOrders order = new PlatOrders();
                                        try {
                                            order = (PlatOrders) platOrders.clone();
                                        } catch (CloneNotSupportedException e) {
                                            // TODO Auto-generated catch block
                                            //e.printStackTrace();
                                        }

                                        if (x + 1 < strucksublist.size()) {
                                            // 计算每条子件占总成本的比例 保留8位小数
                                            InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(strucksublist.get(x).getSubEncd());
                                            BigDecimal rate = invtyDoc2.getRefCost().multiply(strucksublist.get(x).getSubQty()).divide(total, 8, BigDecimal.ROUND_HALF_UP);
                                            order.setInvNum((new BigDecimal(order.getGoodNum())).multiply(strucksublist.get(x).getSubQty()).intValue());
                                            order.setPayMoney(new BigDecimal(dFormat.format(order.getPayMoney().multiply(rate))));// 设置实付金额
                                            // 保留两位小数
                                            // 计算实付单价 保留8位小数
                                            order.setPayPrice(order.getPayMoney().divide((new BigDecimal(order.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                                            order.setSellerPrice(order.getPayPrice());
                                            // 存货编码
                                            order.setInvId(strucksublist.get(x).getSubEncd());
                                            // 设置可退数量
                                            order.setCanRefNum(order.getInvNum());
                                            // 设置可退金额
                                            order.setCanRefMoney(order.getPayMoney());
                                            // 设置商品金额
                                            order.setGoodMoney(new BigDecimal(dFormat.format(platOrders.getGoodMoney().multiply(rate))));
                                            money456 = money456.add(order.getGoodMoney());
                                            // 设置优惠金额
                                            order.setDiscountMoney(order.getGoodMoney().subtract(order.getPayMoney()));
                                            order.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
                                            order.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
                                            if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
                                                order.setIsGift(1);
                                                platOrder.setHasGift(1);
                                            } else {// 设置是否赠品
                                                order.setIsGift(0);
                                            }
                                            orderslist.add(order);
                                            money123 = money123.add(order.getPayMoney());
                                        } else {
                                            order.setInvNum((new BigDecimal(order.getGoodNum())).multiply(strucksublist.get(x).getSubQty()).intValue());
                                            order.setPayMoney(order.getPayMoney().subtract(money123));// 设置实付金额
                                            // 保留两位小数
                                            // 计算实付单价 保留8位小数
                                            order.setPayPrice(order.getPayMoney().divide((new BigDecimal(order.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                                            order.setSellerPrice(order.getPayPrice());
                                            // 存货编码
                                            order.setInvId(strucksublist.get(x).getSubEncd());
                                            // 设置可退数量
                                            order.setCanRefNum(order.getInvNum());
                                            // 设置可退金额
                                            order.setCanRefMoney(order.getPayMoney());
                                            // 设置商品金额
                                            order.setGoodMoney(platOrders.getGoodMoney().subtract(money456));
                                            // 设置优惠金额
                                            order.setDiscountMoney(order.getGoodMoney().subtract(order.getPayMoney()));
                                            order.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
                                            order.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
                                            if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
                                                order.setIsGift(1);
                                                platOrder.setHasGift(1);
                                            } else {// 设置是否赠品
                                                order.setIsGift(0);
                                            }
                                            orderslist.add(order);
                                        }

                                    }
                                }

                            }
                        }
                    } else {
                        // 不属于PTO类型存货
                        // 存货编码
                        platOrders.setInvId(goodRecord.getGoodId());
                        // 设置存货数量
                        platOrders.setInvNum(platOrders.getGoodNum());
                        // 设置可退数量
                        platOrders.setCanRefNum(platOrders.getInvNum());
                        platOrders.setPtoCode("");// 设置对应母件编码
                        platOrders.setPtoName("");// 设置对应母件名称
                        // 设置实付单价
                        platOrders.setPayPrice(platOrders.getPayMoney().divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                        platOrders.setSellerPrice(platOrders.getPayPrice());// 结算单价与实付单价一样
                        // 设置优惠金额
                        platOrders.setDiscountMoney(platOrders.getGoodMoney().subtract(platOrders.getPayMoney()));
                        if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
                            platOrders.setIsGift(1);
                            platOrder.setHasGift(1);
                        } else {// 根据实付单价是否为0判断是否为赠品
                            platOrders.setIsGift(0);
                        }
                        // 设置可退金额,与实付金额一致
                        platOrders.setCanRefMoney(platOrders.getPayMoney());
                        orderslist.add(platOrders);
                    }
                } else {
                    // 存货档案pto属性为空
                    platOrder.setAuditHint("存货编码：" + invtyDoc.getInvtyEncd() + "在存货档案中PTO属性为空");
                    platOrderDao.update(platOrder);// 更新订单的审核提示；
                    flag = false;
                    return flag;
                }
            }

        }
        return flag;
    }

}
