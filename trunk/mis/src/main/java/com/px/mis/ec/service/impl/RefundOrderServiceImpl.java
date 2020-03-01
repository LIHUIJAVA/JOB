package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.px.mis.util.CommonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddRefundListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.response.PddRefundListIncrementGetResponse;
import com.pdd.pop.sdk.http.api.response.PddRefundListIncrementGetResponse.RefundIncrementGetResponseRefundListItem;
import com.px.mis.account.service.FormBookService;
import com.px.mis.ec.dao.AftermarketDao;
import com.px.mis.ec.dao.CancelOrderDao;
import com.px.mis.ec.dao.CompensateDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.Aftermarket;
import com.px.mis.ec.entity.CancelOrder;
import com.px.mis.ec.entity.Compensate;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrderExport;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.entity.UnionQuery;
import com.px.mis.ec.service.RefundOrderService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.InvtyCls;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.purc.service.InvtyDocService;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.px.mis.whs.entity.WhsDoc;
import com.taobao.api.domain.Refund;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.RefundGetRequest;
import com.taobao.api.request.RefundsReceiveGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.RefundGetResponse;
import com.taobao.api.response.RefundsReceiveGetResponse;
import com.px.mis.ec.service.impl.PlatOrderPdd;

@Transactional
@Service
public class RefundOrderServiceImpl implements RefundOrderService {

    private Logger logger = LoggerFactory.getLogger(RefundOrderServiceImpl.class);

    @Autowired
    private PlatOrderPdd PlatOrderPdd;
    @Autowired
    private GetOrderNo getOrderNo;

    @Autowired
    private RefundOrderDao refundOrderDao;

    @Autowired
    private RefundOrdersDao refundOrdersDao;
    @Autowired
    private MisUserDao misUserDao;
    @Autowired
    private PlatOrderDao platOrderDao;

    @Autowired
    private SellSnglDao sellSnglDao;
    @Autowired
    private SellOutWhsDao sellOutWhsDao;
    @Autowired
    private SellSnglSubDao sellSnglSubDao;
    @Autowired
    private StoreRecordDao storeRecordDao;
    @Autowired
    private WhsDocMapper whsdocDao;
    @Autowired
    private InvtyDocDao invtyDocDao;
    @Autowired
    private RtnGoodsDao rtnGoodsDao;
    @Autowired
    private RtnGoodsSubDao rtnGoodsSubDao;
    @Autowired
    private CancelOrderServiceImpl cancelOrderServiceImpl;
    @Autowired
    private CompensateServiceImpl compensateServiceImpl;
    @Autowired
    private AftermarketServiceImpl aftermarketServiceImpl;
    @Autowired
    private AftermarketDao aftermarketDao;
    @Autowired
    private CancelOrderDao cancelOrderDao;
    @Autowired
    private CompensateDao compensateDao;
    @Autowired
    private PlatOrderSN platOrderSN;
    @Autowired
    private PlatOrderMaiDu platOrderMaiDu;
    @Autowired
    private LogRecordDao logRecordDao;
    @Autowired
    private FormBookService formBookService;
    private Integer downloadCount = 0;//记录下载条数
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private PlatOrderXHS platOrderXHS;
    @Autowired
    private InvtyClsDao invtyClsDao;
    @Autowired
    PlatOrderXMYP platOrderXMYP;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Override
    public String add(String jsonBody) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            MisUser misUser = misUserDao.select(userId);
            RefundOrder refundOrder = BaseJson.getPOJO(jsonBody, RefundOrder.class);
            String refId = getOrderNo.getSeqNo("tk", userId);
            refundOrder.setRefId(refId);
            List<Map> mapList = BaseJson.getList(jsonBody);
            List<RefundOrders> refundOrdersList = new ArrayList();

            refundOrder.setOperator(misUser.getUserName());// 设置创建人
            refundOrder.setOperatorId(misUser.getAccNum());// 设置创建人id
            refundOrder.setSource(0);// 设置退款单来源手工新增
            refundOrder.setSourceNo("");// 手工新增的退款单没有来源单据
            refundOrder.setDownTime(sdf.format(new Date()));// 设置下载时间为当前时间
            refundOrder.setIsAudit(0);// 设置待审核
            refundOrder.setOperatorTime(sdf.format(new Date()));
            if (mapList.size() > 0) {
                for (Map map : mapList) {
                    RefundOrders refundOrders = new RefundOrders();
                    refundOrders.setRefId(refId);
                    refundOrders.setGoodId(map.get("goodId").toString());
                    refundOrders.setGoodName(map.get("goodName").toString());
                    // refundOrders.setGoodSku(map.get("goodSku").toString());
                    // refundOrders.setEcGoodId(map.get("ecGoodId").toString());
                    refundOrders.setCanRefNum(Integer.parseInt(map.get("canRefNum").toString()));
                    refundOrders.setCanRefMoney(new BigDecimal(map.get("canRefMoney").toString()));
                    refundOrders.setRefNum(Integer.parseInt(map.get("refNum").toString()));
                    refundOrders.setRefMoney(new BigDecimal(map.get("refMoney").toString()));
                    refundOrders.setBatchNo(map.get("batchNo").toString());
                    refundOrders.setRefWhs(map.get("refWhs").toString());
                    refundOrders.setMemo(map.get("memo").toString());
//                    refundOrders.setBaozhiqi(Integer.parseInt(map.get("baozhiqi").toString()));
                    refundOrders.setPrdcDt(map.get("prdcDt").toString());
                    refundOrders.setInvldtnDt(map.get("invldtnDt").toString());
                    refundOrders.setIsGift(Integer.parseInt(String.valueOf(map.get("isGift").toString())));
                    refundOrders.setPrdcDt(map.get("prdcDt").toString());
                    refundOrders.setInvldtnDt(map.get("invldtnDt").toString());
                    InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(refundOrders.getGoodId());
                    if (invtyDoc == null) {
                        message = "没有该商品档案新增失败！";
                        isSuccess = false;
                        resp = BaseJson.returnRespObjList("ec/refundOrder/add", isSuccess, message, refundOrder,
                                refundOrdersList);
                        return resp;
                    }
                    if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
                        refundOrders.setBaozhiqi(Integer.parseInt(String.valueOf(invtyDoc.getBaoZhiQiDt())));
                    }
                    refundOrdersList.add(refundOrders);
                }
            }
            refundOrderDao.insert(refundOrder);
            refundOrdersDao.insertList(refundOrdersList);
            message = "新增成功!";
            isSuccess = true;
            resp = BaseJson.returnRespObjList("ec/refundOrder/add", isSuccess, message, refundOrder, refundOrdersList);

        } catch (Exception e) {
            logger.error("URL：ec/refundOrder/add 异常说明：", e);
            throw new RuntimeException(e.getMessage());
        }
        return resp;
    }

    @Override
    public String edit(String jsonBody) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            RefundOrder refundOrder = BaseJson.getPOJO(jsonBody, RefundOrder.class);
            List<Map> mapList = BaseJson.getList(jsonBody);
            List<RefundOrders> refundOrdersList = new ArrayList();
            for (Map map : mapList) {
                RefundOrders refundOrders = new RefundOrders();
                BeanUtils.populate(refundOrders, map);
                refundOrdersList.add(refundOrders);
            }
            if (refundOrder.getRefId() == null || "".equals(refundOrder.getRefId())) {
                message = "编号" + refundOrder.getRefId() + "不存在！";
                isSuccess = false;
            } else {
                refundOrderDao.update(refundOrder);
                refundOrdersDao.delete(refundOrder.getRefId());
                refundOrdersDao.insertList(refundOrdersList);
                message = "修改成功!";
                isSuccess = true;
            }
            resp = BaseJson.returnRespObj("ec/refundOrder/edit", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL：ec/refundOrder/edit 异常说明：", e);
            throw new RuntimeException();
        }
        return resp;
    }

    @Override
    public String delete(String jsonBody) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        List<String> okids = new ArrayList<String>();
        try {
            String[] refIds = BaseJson.getReqBody(jsonBody).get("refId").asText().split(",");
            if (refIds.length > 0) {
                List<String> refIdList = Arrays.asList(refIds);
                for (int i = 0; i < refIdList.size(); i++) {
                    RefundOrder refundOrder = refundOrderDao.select(refIdList.get(i));
                    if (refundOrder.getIsAudit() == 1) {
                        // 退款单已审核，不能直接删除
                        refundOrder.setAuditHint("退款单已审核，不能直接删除");
                        refundOrderDao.update(refundOrder);
                    } else {
                        if (refundOrder.getSource() == 1) {
                            // 京东自主售后
                            Aftermarket aftermarket = aftermarketDao
                                    .selectById(Integer.parseInt(refundOrder.getSourceNo()));
                            aftermarketDao.delete(aftermarket);
                        } else if (refundOrder.getSource() == 2) {
                            CancelOrder cancelOrder = cancelOrderDao.selectById(refundOrder.getSourceNo());
                            cancelOrderDao.delete(cancelOrder);
                            // 京东取消订单
                        } else if (refundOrder.getSource() == 3) {
                            // 京东赔付
                            Compensate compensate = compensateDao.selectById(refundOrder.getSourceNo());
                            compensateDao.delete(compensate);
                        }
                        okids.add(refIdList.get(i));
                    }
                }
                if (okids.size() > 0) {
                    refundOrderDao.delete(okids);
                }
                message = "删除完成!本次成功删除退款单" + okids.size() + "条";
                isSuccess = true;
            } else {
                message = "请选择要删除的退款单！";
                isSuccess = false;
            }
            resp = BaseJson.returnRespObj("ec/refundOrder/delete", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL：ec/refundOrder/delete 异常说明：", e);
        }
        return resp;
    }

    @Override
    public String query(String refId) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            RefundOrder refundOrder = refundOrderDao.select(refId);
            List<RefundOrders> refundOrdersList = refundOrdersDao.selectList(refId);
            if (refundOrder == null) {
                message = "编号" + refId + "不存在!";
                isSuccess = false;
            } else {
                message = "查询成功!";
                isSuccess = true;
            }
            resp = BaseJson.returnRespObjList("ec/refundOrder/query", isSuccess, message, refundOrder,
                    refundOrdersList);
        } catch (Exception e) {
            logger.error("URL：ec/refundOrder/query 异常说明：", e);
        }
        return resp;
    }

    @Override
    public String queryList(String jsonBody) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
            map.put("index", (pageNo - 1) * pageSize);
            map.put("num", pageSize);

            String applyDateStart = (String) map.get("applyDateStart");
            String applyDateEnd = (String) map.get("applyDateEnd");
            if (StringUtils.isNotEmpty(applyDateStart)) {
                applyDateStart = applyDateStart + " 00:00:00";
                map.put("applyDateStart", applyDateStart);
            }
            if (StringUtils.isNotEmpty(applyDateEnd)) {
                applyDateEnd = applyDateEnd + " 23:59:59";
                map.put("applyDateEnd", applyDateEnd);
            }
            String treDateStart = (String) map.get("treDateStart");
            String treDateEnd = (String) map.get("treDateEnd");
            if (StringUtils.isNotEmpty(treDateStart)) {
                treDateStart = treDateStart + " 00:00:00";
                map.put("treDateStart", treDateStart);
            }
            if (StringUtils.isNotEmpty(treDateEnd)) {
                treDateEnd = treDateEnd + " 23:59:59";
                map.put("treDateEnd", treDateEnd);
            }

            List<RefundOrder> refundOrderList = refundOrderDao.selectList(map);
            int count = refundOrderDao.selectCount(map);
            message = "查询成功";
            isSuccess = true;
            resp = BaseJson.returnRespList("ec/refundOrder/queryList", isSuccess, message, count, pageNo, pageSize, 0,
                    0, refundOrderList);
        } catch (Exception e) {
            logger.error("URL：ec/refundOrder/queryList 异常说明：", e);
        }
        return resp;
    }

    /**
     * 退款参照原始电商订单
     */
    @Override
    public String refundReference(String ecOrderId, String orderId) {
        // TODO Auto-generated method stub
        String message = "";
        Boolean isSuccess = true;
        String resp = "查询成功";
        // 先根据平台电商订单号查找订单是否存在已审核的电商订单
        List<PlatOrder> platOrders = refundOrderDao.selectPlatOrderListByEcOrderId(ecOrderId);
        // List<SellSnglSub> sellSnglSubsList = new ArrayList<>();
        if (platOrders.size() == 0) {
            isSuccess = false;
            message = "所参照订单未审核或不存在";
        } else {
            if (orderId.length() > 0) {
                platOrders = platOrderDao.selectByOrderId(orderId);
            }

            /*
             * if (sellSngls.size() == 0) { isSuccess = false; message =
             * "所参照订单没有对应销售单或对应销售单未审核"; } else { for (int i = 0; i < sellSngls.size(); i++)
             * { if (sellSngId.length() != 0) { if
             * (sellSngls.get(i).getSellSnglId().equals(sellSngId)) {
             * sellSngls1.add(sellSngls.get(i)); } } else {
             * sellSngls1.add(sellSngls.get(i)); } } }
             */
        }
        try {
            resp = BaseJson.returnRespList("ec/refundOrder/refundReference", isSuccess, message, 10, 10, 10, 10, 10,
                    platOrders);
            // resp = BaseJson.returnRespObjList("ec/refundOrder/refundReference",
            // isSuccess, message,sellSngl , sellSnglSubsList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    public String audit(String jsonBody) {
        // TODO Auto-generated method stub
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        int count = 0;
        try {
            String refId = BaseJson.getReqBody(jsonBody).get("refId").asText();
            String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String loginDate = BaseJson.getReqHead(jsonBody).get("loginTime").asText();
            MisUser misUser = misUserDao.select(accNum);
            String[] refIds = refId.split(",");
            String date = sdf.format(new Date());
            if (refIds.length > 0) {
                List<String> serviceIdList = Arrays.asList(refIds);
                List<RefundOrder> refundOrders = refundOrderDao.selectListByRefIds(serviceIdList);
                for (int i = 0; i < refundOrders.size(); i++) {
                    RefundOrder refundOrder = refundOrders.get(i);
                    if (refundOrder.getIsAudit() == 1) {
                        // 退款单已经审核
                        refundOrder.setAuditHint("退款单已审核，不能重复审核。");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }

                    if (StringUtils.isEmpty(refundOrder.getApplyDate())) {

                        refundOrder.setAuditHint("退款单申请日期为空，不能审核");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    String time = refundOrder.getTreDate();
                    if (StringUtils.isEmpty(time)) {
                        refundOrder.setAuditHint("退款单处理日期为空，不能审核");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    if (formBookService.isMthSeal(loginDate)) {
                        refundOrder.setAuditHint("登录日期所在月份已封账，不能审核");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    List<PlatOrder> platOrderlist = platOrderDao
                            .selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId());
                    if (platOrderlist.size() == 0) {
                        // 退款单对应平台订单已删除
                        refundOrder.setAuditHint("退款单对应订单已删除，不能审核。");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    if (platOrderDao.selectNoAuditOrderByEcOrderId(refundOrder.getEcOrderId()) > 0) {
                        // 退款单对应原订单尚未审核
                        refundOrder.setAuditHint("退款单对应原订单尚未审核，不能审核退款单");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    List<RefundOrders> refundOrderss = refundOrdersDao.selectListGroupByInvidAndBatch(refundOrder.getRefId());
                    RtnGoods rtnGoods = new RtnGoods();
                    String rtnGoodsId = "";
                    try {
                        rtnGoodsId = getOrderNo.getSeqNo("TH", accNum, loginDate);
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        //e1.printStackTrace();

                        refundOrder.setAuditHint("审核失败，生成退货单单号失败");
                        refundOrderDao.update(refundOrder);

                        continue;

                    }
                    rtnGoods.setRtnGoodsId(rtnGoodsId);// 退货单号
                    //rtnGoods.setRtnGoodsDt(loginDate);// 订单日期；退货单日期选用审核时的登录日期
                    rtnGoods.setRtnGoodsDt(loginDate.split(" ")[0] + sdf.format(new Date()).substring(10));//退货日期，用登录日期加系统时分秒
                    // PlatOrder selectPlatOrder =
                    // platOrderDao.select(refundOrder.getOrderId());//查询原订单
                    rtnGoods.setSellTypId(platOrderlist.get(0).getSellTypId());// 销售类型编号 ；
                    rtnGoods.setBizTypId(platOrderlist.get(0).getBizTypId());// 业务类型编号；
                    rtnGoods.setRecvSendCateId(platOrderlist.get(0).getRecvSendCateId());// 收发类别编号；
                    rtnGoods.setRefId(refundOrder.getRefId());// 在退货单中设置退款单号，方便后续弃审
                    rtnGoods.setTxId(platOrderlist.get(0).getOrderId());// 设置订单号
                    rtnGoods.setCustOrdrNum(refundOrder.getEcOrderId());// 平台订单
                    rtnGoods.setFormTypEncd("008");
                    StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
                    if (storeRecord != null) {
                        rtnGoods.setCustId(storeRecord.getCustomerId());// 客户编号
                    } else {
                        // 退款单对应店铺不存在
                        refundOrder.setAuditHint("退款单对应店铺档案不存在，不能审核退款单");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    rtnGoods.setAccNum(storeRecord.getRespPerson());// 业务员编号 取店铺负责人id
                    rtnGoods.setUserName(misUser.getUserName());// 用户名称
                    rtnGoods.setSetupPers(misUser.getAccNum());// 创建人 用户编号
                    rtnGoods.setSetupTm(sdf.format(new Date()));// 创建时间；
                    // 收货地址名称
                    // 判断仓库编码是否一致，如果不一致，审核失败
                    String whsEncd = refundOrderss.get(0).getRefWhs();
                    boolean flag = true;
                    for (RefundOrders refundOrders1 : refundOrderss) {
                        if (whsEncd == null || !whsEncd.equals(refundOrders1.getRefWhs())) {
                            refundOrder.setAuditHint("退货仓库编码为空,或者不一致，退款单审核失败！");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        continue;
                    }
                    WhsDoc whsDoc = whsdocDao.selectWhsDoc(whsEncd);
                    if (whsDoc != null) {
                        rtnGoods.setDelvAddrNm(whsDoc.getWhsAddr());// 发货地址（仓库地址，根据退款单子表中的退货仓库编码获取）
                    }
                    // 销售订单号 根据订单编号查询销售单；
                    SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(refundOrder.getOrderId());
                    if (sellSngl != null) {
                        rtnGoods.setSellOrdrId(sellSngl.getSellSnglId());// 销售订单号销售单号
                    }
                    rtnGoods.setMemo(refundOrder.getMemo());// 备注；
                    rtnGoods.setIsNtBllg(1);// 是否需要开票；

                    List<RtnGoodsSub> rtnGoodsSubs = new ArrayList<RtnGoodsSub>();

                    for (RefundOrders refundOrders1 : refundOrderss) {
                        RtnGoodsSub rtnGoodsSub = new RtnGoodsSub();
                        rtnGoodsSub.setWhsEncd(refundOrders1.getRefWhs());// 仓库编号；
                        // 存货编码 平台商品表good_record中的平台商品编码；
                        rtnGoodsSub.setInvtyEncd(refundOrders1.getGoodId());// 存货编码
                        rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());// 退货单主表编号；
                        rtnGoodsSub.setPrdcDt(refundOrders1.getPrdcDt());// 生产日期
                        rtnGoodsSub.setInvldtnDt(refundOrders1.getInvldtnDt());// 失效日期
                        rtnGoodsSub.setMemo(refundOrders1.getMemo());// 子表明细备注
                        Integer refNum2 = refundOrders1.getRefNum();// 退货数量
                        BigDecimal refMoney = refundOrders1.getRefMoney();// 退款金额；
                        Integer canRefNum2 = refundOrders1.getCanRefNum();// 可退货数量
                        if (refNum2 == 0 && refMoney.compareTo(BigDecimal.ZERO) == 0) {
                            refundOrder.setAuditHint("退货数量、退货金额不能同时为0");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }
                        if (canRefNum2 < refNum2) {// 退货数量不能大于可退货数量
                            refundOrder.setAuditHint("退货数量不能大于可退货数量");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }

                        BigDecimal refNum = new BigDecimal(refNum2);


                        // 求Decimal的相反数，只需要调用negate方法，
                        rtnGoodsSub.setQty(refNum.negate());// 数量 负数
                        rtnGoodsSub.setUnBllgQty(refNum);//未开票数量
                        rtnGoodsSub.setBatNum(refundOrders1.getBatchNo());// 批号
                        // 用实付单价金额和销项税率等计算一系列数据；
                        // 退款金额除以退款数量就是退货金额，保留8位小数，舍位的时候四舍五入；
                       /* BigDecimal refPrice;// 退货单价 无税单价
                        if (refNum.compareTo(BigDecimal.ZERO) > 0) {
                            refPrice = refMoney.divide(refNum, 8, BigDecimal.ROUND_HALF_UP);
                        } else {
                            refPrice = BigDecimal.ZERO;
                        }*/
//					InvtyTab invtyTab = new InvtyTab();
//					String whsEncd2 = rtnGoodsSub.getWhsEncd();
//					invtyTab.setWhsEncd(whsEncd2);
                        String invtyEncd = rtnGoodsSub.getInvtyEncd();
//					invtyTab.setInvtyEncd(invtyEncd);
//					String batNum = rtnGoodsSub.getBatNum();
//					invtyTab.setBatNum(batNum);
//					invtyTab = invtyTabDao.selectByReverse(invtyTab);
                        InvtyDoc invtyDocOne = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyEncd);

                        InvtyCls ic = invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyDocOne.getInvtyClsEncd());
                        if (StringUtils.isEmpty(ic.getProjEncd())) {
                            refundOrder.setAuditHint("商品：" + refundOrders1.getGoodId() + "对应分类项目编码为空，请先设置");
                            refundOrderDao.update(refundOrder);//更新订单的审核提示；

                            //日志记录
                            LogRecord logRecord = new LogRecord();
                            logRecord.setOperatId(accNum);
                            //MisUser misUser = misUserDao.select(accNum);
                            if (misUser != null) {
                                logRecord.setOperatName(misUser.getUserName());
                            }
                            logRecord.setOperatContent("审核失败，商品：" + refundOrders1.getGoodId() + "对应分类项目编码为空，请先设置");
                            logRecord.setOperatTime(
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            logRecord.setOperatType(2);//2审核
                            logRecord.setTypeName("审核");
                            logRecord.setOperatOrder(refundOrders.get(i).getEcOrderId());//操作单号
                            logRecordDao.insert(logRecord);

                            flag = false;
                            break;

                        } else {
                            rtnGoodsSub.setProjEncd(ic.getProjEncd());//设置项目编码
                        }


                        BigDecimal taxRate = null;
                        if (invtyDocOne == null) {
                        	refundOrder.setAuditHint("审核失败，未找到对应存货档案：" + refundOrders1.getGoodId());
                            refundOrderDao.update(refundOrder);//更新订单的审核提示；

                            //日志记录
                            LogRecord logRecord = new LogRecord();
                            logRecord.setOperatId(accNum);
                            //MisUser misUser = misUserDao.select(accNum);
                            if (misUser != null) {
                                logRecord.setOperatName(misUser.getUserName());
                            }
                            logRecord.setOperatContent("审核失败，未找到对应存货档案：" + refundOrders1.getGoodId());
                            logRecord.setOperatTime(
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            logRecord.setOperatType(2);//2审核
                            logRecord.setTypeName("审核");
                            logRecord.setOperatOrder(refundOrders.get(i).getEcOrderId());//操作单号
                            logRecordDao.insert(logRecord);

                            flag = false;
                            break;
                            
                        }else {
                        	taxRate = invtyDocOne.getOptaxRate();// 税率 存货档案中的销项税率
                        }
                        try {
                            rtnGoodsSub.setBaoZhiQi(Integer.parseInt(invtyDocOne.getBaoZhiQiDt()));// 保质期天数
                        } catch (Exception e) {
                            // TODO: handle exception
                            logger.error("URL：ec/refundOrder/add 异常说明：存货" + invtyDocOne.getInvtyEncd() + "保质期转换异常");
                        }
                        if (rtnGoodsSub.getQty().compareTo(BigDecimal.ZERO) > 0) {
                            rtnGoodsSub.setIsNtRtnGoods(1);// 是否退货
                        } else {
                            rtnGoodsSub.setIsNtRtnGoods(0);
                        }
                        if (refNum != null && taxRate != null) {
                        	taxRate = taxRate.divide(new BigDecimal(100));
                            BigDecimal noTaxAmt = BigDecimal.ZERO;// 无税金额
                            BigDecimal taxAmt = BigDecimal.ZERO;// 税额
                            BigDecimal cntnTaxUprc = BigDecimal.ZERO;// 含税单价
                            BigDecimal prcTaxSum = refundOrders1.getRefMoney();// 价税合计
                            BigDecimal noTaxUprc = BigDecimal.ZERO;//无税单价
                            if(refNum.compareTo(BigDecimal.ZERO)>0) {
                            	noTaxAmt = prcTaxSum.divide((new BigDecimal(1).add(taxRate)),2,BigDecimal.ROUND_HALF_UP); // 无税金额
                            	taxAmt = prcTaxSum.subtract(noTaxAmt);// 税额  含税金额-无税金额
                            	noTaxUprc = noTaxAmt.divide(refNum,8,BigDecimal.ROUND_HALF_UP);//无税单价
                            	cntnTaxUprc = noTaxUprc.multiply((new BigDecimal(1).add(taxRate)));// 含税单价
                            }else {
                            	noTaxAmt = prcTaxSum.divide((new BigDecimal(1).add(taxRate)),2,BigDecimal.ROUND_HALF_UP); // 无税金额
                            	taxAmt = prcTaxSum.subtract(noTaxAmt);// 税额  含税金额-无税金额
                            }
                            rtnGoodsSub.setCntnTaxUprc(cntnTaxUprc);// 含税单价；
                            rtnGoodsSub.setPrcTaxSum(prcTaxSum.negate());// 价税合计；
                            rtnGoodsSub.setNoTaxAmt(noTaxAmt.negate());// 无税金额；
                            rtnGoodsSub.setNoTaxUprc(noTaxUprc);// 无税单价
                            rtnGoodsSub.setTaxAmt(taxAmt.negate());// 税额
                            rtnGoodsSub.setTaxRate(taxRate.multiply(new BigDecimal(100)));// 税率	
                        
                        
                        } else {
                            refundOrder.setAuditHint(invtyDocOne.getInvtyEncd() + "：退货金额、退货数量、税率不能为空，请检查");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }
//					rtnGoodsSub.setPrdcDt(invtyTab.getPrdcDt());//生产日期；
//					rtnGoodsSub.setBaoZhiQi(Integer.valueOf(invtyTab.getBaoZhiQi()));//保质期
//					rtnGoodsSub.setInvldtnDt(invtyTab.getInvldtnDt());//失效日期；

                        // InvtyDoc invtyDoc =
                        // invtyDocDao.selectInvtyDocByInvtyDocEncd(refundOrders1.getGoodId());
                        /*
                         * String invtyClsEncd = null; if (invtyDocOne != null) { invtyClsEncd =
                         * invtyDocOne.getInvtyClsEncd();// 存货分类编码； }
                         */
                        /*
                         * if (invtyClsEncd == null) {
                         *
                         * } else if (invtyClsEncd.startsWith("100")) {
                         * rtnGoodsSub.setIsComplimentary(1);// 是赠品； } else {
                         * rtnGoodsSub.setIsComplimentary(0);// 不是赠品； }
                         */
                        rtnGoodsSub.setIsComplimentary(refundOrders1.getIsGift());// 设置是否赠品
                        if (refNum.compareTo(BigDecimal.ZERO) > 0) {
                            rtnGoodsSub.setIsNtRtnGoods(1);// 是否退货 0：不退货；1：退货
                        } else {
                            rtnGoodsSub.setIsNtRtnGoods(0);// 是否退货 0：不退货；1：退货
                        }
                        rtnGoodsSubs.add(rtnGoodsSub);// 添加到退货单子表中
                    }
                    if (!flag) {
                        continue;
                    }
                    rtnGoods.setFormTypEncd("008");// 单据类型
                    rtnGoods.setIsNtChk(0);// 退货单待审核

                    rtnGoods.setDeptId(misUser.getDepId());

                    refundOrder.setIsAudit(1);// 设置退款单已审核
                    refundOrder.setAuditTime(date);// 设置审核时间
                    refundOrder.setAuditUserId(misUser.getAccNum());
                    refundOrder.setAuditUserName(misUser.getUserName());
                    refundOrder.setAuditHint("");
                    int re = refundOrderDao.updateAudit(refundOrder);
                    if (re == 1) {
                        //更新成功时
                        rtnGoodsDao.insertRtnGoods(rtnGoods);// 插入退货单主表
                        rtnGoodsSubDao.insertRtnGoodsSub(rtnGoodsSubs);// 插入退货单子表
                        if (refundOrder.getSource() != 3) {// 退款单来源不等于3时需要修改原单的可退金额和可退数量
                            // 退款单审核完成后修改原单的可退数量及可退金额
                            refundOrderss = refundOrdersDao.selectList(refundOrder.getRefId());
                            for (RefundOrders refundOrders1 : refundOrderss) {
                                int needRefundCount = refundOrders1.getRefNum();// 还需退货数量
                                BigDecimal needRefundMoney = refundOrders1.getRefMoney();// 还需退货金额
                                List<PlatOrders> platOrderslist = platOrdersDao.selectByEcOrderIdAndInvIdAndBatNum(
                                        refundOrder.getEcOrderId(), refundOrders1.getGoodId(), refundOrders1.getBatchNo());
                                for (int j = 0; j < platOrderslist.size(); j++) {
                                    // 更新每条明细可退金额及可退数量
                                    // 当当前条明细不够数量时，将剩余数量扣减到下一次
                                    if (needRefundCount > 0) {
                                        if (platOrderslist.get(j).getCanRefNum() >= needRefundCount) {
                                            // 当前条明细可退数量大于退货数量时
                                            // 设置原订单可退数量
                                            platOrderslist.get(j).setCanRefNum(
                                                    platOrderslist.get(j).getCanRefNum() - refundOrders1.getRefNum());
                                            needRefundCount = 0;
                                        } else {
                                            needRefundCount = needRefundCount - platOrderslist.get(j).getCanRefNum();
                                            platOrderslist.get(j).setCanRefNum(0);

                                        }
                                    }
                                    if (needRefundMoney.compareTo(BigDecimal.ZERO) > 0) {
                                        // 还需退货金额大于0
                                        if (platOrderslist.get(j).getCanRefMoney().compareTo(needRefundMoney) >= 0) {
                                            platOrderslist.get(j).setCanRefMoney(
                                                    platOrderslist.get(j).getCanRefMoney().subtract(needRefundMoney));
                                            needRefundMoney = BigDecimal.ZERO;
                                        } else {
                                            needRefundMoney = needRefundMoney
                                                    .subtract(platOrderslist.get(j).getCanRefMoney());
                                            platOrderslist.get(j).setCanRefMoney(BigDecimal.ZERO);
                                        }
                                    }
                                    // 更新原单可退金额及可退数量
                                    platOrdersDao.updateCanRefMoneyAndNum(platOrderslist.get(j));
                                }
                            }
                        }
                        count++;
                    }


                    // 以上是生成退货单；

                    /*
                     * //=======================生成红字销售出库单=================================== Integer
                     * isRefund = refundOrder.getIsRefund(); //销售单主表实体 SellOutWhs sellOutWhs = new
                     * SellOutWhs(); //销售单子表实体 List<SellOutWhsSub> sellOutWhsSub = new
                     * ArrayList<>(); //库存实体 InvtyTab invtyTab = new InvtyTab(); if(isRefund==1)
                     * {//是否退货 当退货的时候生成红字销售出库单； //生成红字销售出库单； //获取订单号 String
                     * number=getOrderNo.getSeqNo("CK", userId); if
                     * (sellOutWhsDao.selectSellOutWhsByOutWhsId(number)!=null){//会重复吗?????
                     * message="编号"+number+"已存在，请重新输入！"; isSuccess=false; }else {
                     * sellOutWhs.setOutWhsId(number); //出库日期应为审核或实际出库日期？？
                     * sellOutWhs.setOutWhsDt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new
                     * Date())); sellOutWhs.setAccNum(rtnGoods.getAccNum());//用户编号
                     * sellOutWhs.setUserName(rtnGoods.getUserName());
                     * sellOutWhs.setCustId(rtnGoods.getCustId());
                     * sellOutWhs.setBizTypId(rtnGoods.getBizTypId());
                     * sellOutWhs.setSellTypId(rtnGoods.getSellTypId());
                     * sellOutWhs.setRecvSendCateId(rtnGoods.getRecvSendCateId());
                     * sellOutWhs.setOutIntoWhsTypId("销售出库");
                     * sellOutWhs.setSellOrdrInd(rtnGoods.getSellOrdrId());//销售单标识
                     * sellOutWhs.setSetupPers(rtnGoods.getSetupPers());//创建人
                     * sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new
                     * Date()));//创建时间 sellOutWhs.setMemo(rtnGoods.getMemo());//备注信息
                     * sellOutWhs.setRtnGoodsId(rtnGoods.getRtnGoodsId()); String sellOrdrId =
                     * sellOutWhs.getSellOrdrInd(); for(RtnGoodsSub rGSub:rtnGoodsSubs) {
                     * invtyTab.setWhsEncd(rGSub.getWhsEncd());
                     * invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
                     * invtyTab.setBatNum(rGSub.getBatNum()); invtyTab =
                     * itd.selectInvtyTabsByTerm(invtyTab); invtyTab.setAvalQty(rGSub.getQty());
                     * //在库存表中将对应的可用量减少（由于退货单中的数量是负数，所以库存表中要减可用量）
                     * itd.updateInvtyTabAvalQtyJian(invtyTab); SellOutWhsSub sOutWhsSub = new
                     * SellOutWhsSub(); sOutWhsSub.setInvtyEncd(rGSub.getInvtyEncd());//存货档案
                     * sOutWhsSub.setWhsEncd(rGSub.getWhsEncd());//仓库档案
                     * sOutWhsSub.setBatNum(rGSub.getBatNum());//批号
                     * sOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//销售出库单子表中主表编号
                     * sOutWhsSub.setQty(rGSub.getQty());//数量 获取数量 sowSub.getQty();
                     * sOutWhsSub.setTaxRate((rGSub.getTaxRate()).divide(new BigDecimal(100)));//税率
                     * // sOutWhsSub.setPrdcDt(rGSub.getPrdcDt());//生产日期 //
                     * sOutWhsSub.setBaoZhiQi(rGSub.getBaoZhiQi());//保质期 //
                     * sOutWhsSub.setInvldtnDt(CalcAmt.getDate(sOutWhsSub.getPrdcDt(),
                     * sOutWhsSub.getBaoZhiQi())); BigDecimal noTaxUprc =
                     * sellOutWhsDao.selectSellOutWhsSubByInWhBn(sOutWhsSub); if(noTaxUprc!=null) {
                     * //获取无税单价 sOutWhsSub.setNoTaxUprc(noTaxUprc); } //计算未税金额 金额=未税数量*未税单价
                     * sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(),
                     * sOutWhsSub.getQty())); //计算税额 税额=未税金额*税率
                     * sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.
                     * getQty(), sOutWhsSub.getTaxRate())); //计算含税单价 含税单价=无税单价*税率+无税单价
                     * sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(),
                     * sOutWhsSub.getQty(), sOutWhsSub.getTaxRate())); //计算价税合计
                     * 价税合计=无税金额*税率+无税金额=税额+无税金额
                     * sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(),
                     * sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
                     * sOutWhsSub.setIsNtRtnGoods(1);//是否退货
                     * sOutWhsSub.setIsComplimentary(rGSub.getIsComplimentary());//是否赠品
                     * sOutWhsSub.setMemo(rGSub.getMemo());//备注 sellOutWhsSub.add(sOutWhsSub); }
                     * sellOutWhsDao.insertSellOutWhs(sellOutWhs);
                     * sellOutWhsSubDao.insertSellOutWhsSub(sellOutWhsSub); isSuccess=true; message
                     * = "退货单单审核成功！"; } }else {//当仅退款的时候不生成红字销售出库单；
                     *
                     * }
                     */
                    // ====================================================================
                    // 是否审核 审核人审核日期怎么处理

                    /*
                     * refundOrder.setIsAudit(1); refundOrderDao.update(refundOrder);//更新退款单的审核状态；
                     */

                }
                if (count > 0) {
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
                message = "审核完成，本次成功审核退款单" + count + "条，失败" + (refIds.length - count) + "条。";
            } else {
                isSuccess = false;
                message = "请选择要审核的退款单。";
            }
            resp = BaseJson.returnRespObj("ec/aftermarket/audit", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    public String noAudit(String jsonBody) {
        // TODO Auto-generated method stub
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        int count = 0;
        try {
            String refId = BaseJson.getReqBody(jsonBody).get("refId").asText();
            String accNum = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            String[] refIds = refId.split(",");
            if (refIds.length > 0) {
                List<String> refids = Arrays.asList(refIds);
                List<RefundOrder> refundOrderlist = refundOrderDao.selectListByRefIds(refids);
                for (int i = 0; i < refundOrderlist.size(); i++) {
                    RefundOrder refundOrder = refundOrderlist.get(i);
                    if (refundOrder.getIsAudit() == 0) {
                        // 退款单尚未审核
                        refundOrder.setAuditHint("当前退款单尚未审核，不能弃审");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    RtnGoods rtnGoods = rtnGoodsDao.selectRtnGoodsByRefId(refundOrder.getRefId());
                    if (rtnGoods.getIsNtChk() == 1) {
                        // 对应退货单已经审核
                        refundOrder.setAuditHint("对应退货单已经审核，需先弃审退货单");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }

                    // 更新退款单状态
                    refundOrder.setIsAudit(0);// 设置状态为待审核
                    refundOrder.setAuditHint("");
                    refundOrder.setAuditTime(null);
                    ;
                    refundOrder.setAuditUserId("");
                    refundOrder.setAuditUserName("");

                    // 退款单弃审增加原单可退金额及可退数量

                    if (refundOrder.getSource() != 3) {// 退款单来源不等于3时需要修改原单的可退金额和可退数量
                        // 退款单审核完成后修改原单的可退数量及可退金额
                        List<RefundOrders> refundOrderss = refundOrdersDao.selectList(refundOrder.getRefId());
                        for (RefundOrders refundOrders1 : refundOrderss) {

                            int needRefundCount = refundOrders1.getRefNum();// 需退货数量
                            BigDecimal needRefundMoney = refundOrders1.getRefMoney();// 需退货金额
                            List<PlatOrders> platOrderslist = platOrdersDao.selectByEcOrderIdAndInvIdAndBatNum(
                                    refundOrder.getEcOrderId(), refundOrders1.getGoodId(), refundOrders1.getBatchNo());
                            for (int j = 0; j < platOrderslist.size(); j++) {
                                if (needRefundCount > 0) {
                                    // 更新每条明细可退金额及可退数量
                                    // 当当前条明细可退数量小于商品数量时，需要增加可退数量
                                    if (platOrderslist.get(j).getInvNum() > platOrderslist.get(j).getCanRefNum()) {
                                        if (platOrderslist.get(j).getCanRefNum() + needRefundCount > platOrderslist
                                                .get(j).getInvNum()) {
                                            needRefundCount = needRefundCount + platOrderslist.get(j).getCanRefNum()
                                                    - platOrderslist.get(j).getInvNum();
                                            // 设置原订单可退数量
                                            platOrderslist.get(j).setCanRefNum(platOrderslist.get(j).getInvNum());// 设置可退数量为订单明细商品数量

                                        } else {
                                            platOrderslist.get(j).setCanRefNum(
                                                    platOrderslist.get(j).getCanRefNum() + needRefundCount);
                                            needRefundCount = 0;
                                        }
                                    }
                                }
                                if (needRefundMoney.compareTo(BigDecimal.ZERO) > 0) {
                                    if (platOrderslist.get(j).getCanRefMoney()
                                            .compareTo(platOrderslist.get(j).getPayMoney()) < 0) {
                                        // 可退金额小于实付金额时才需要增加可退金额
                                        // 当前条明细可退金额加上需要增加的金额大于原单实付金额时，增加部分
                                        if ((platOrderslist.get(j).getCanRefMoney().add(needRefundMoney))
                                                .compareTo(platOrderslist.get(j).getPayMoney()) >= 0) {
                                            needRefundMoney = needRefundMoney
                                                    .add(platOrderslist.get(j).getCanRefMoney())
                                                    .subtract(platOrderslist.get(j).getPayMoney());

                                            platOrderslist.get(j).setCanRefMoney(platOrderslist.get(j).getPayMoney());

                                        } else {

                                            platOrderslist.get(j).setCanRefMoney(
                                                    platOrderslist.get(j).getCanRefMoney().add(needRefundMoney));
                                            needRefundMoney = BigDecimal.ZERO;

                                        }
                                    }
                                }
                                // 更新原单可退金额及可退数量
                                platOrdersDao.updateCanRefMoneyAndNum(platOrderslist.get(j));
                            }
                        }
                    }

                    // 删除对应退货单
                    rtnGoodsDao.deleteRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());
                    // 更新退款单状态
                    refundOrderDao.update(refundOrder);
                    count++;
                }
                message = "弃审完成，本次成功弃审" + count + "条退款单，失败" + (refIds.length - count) + "条";
            } else {
                message = "请选择需要弃审的退款单";
            }
            if (count > 0) {
                isSuccess = true;
            } else {
                isSuccess = false;
            }
            resp = BaseJson.returnRespObj("ec/aftermarket/noAudit", isSuccess, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                resp = BaseJson.returnRespObj("ec/aftermarket/noAudit", false, "弃审时发生异常，请重试", null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return resp;
    }

    @Autowired
    private StoreSettingsDao storeSettingsDao;

    @Override
    public String download(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId,
                           String ecOrderId) {
        // TODO Auto-generated method stub

        String resp = "";
        StoreRecord storeRecord = storeRecordDao.select(storeId);
        // 设置
        StoreSettings storeSettings = storeSettingsDao.select(storeId);

        // 判断当前店铺id属于哪个平台
        switch (storeRecord.getEcId()) {
            case "JD":
                if (StringUtils.isEmpty(storeSettings.getVenderId())) {
                    //京东店铺商家ID不能为空，为空时返回错误提示消息
                    try {
                        resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "退款下载失败，京东平台店铺商家ID不能为空，请在店铺设置中维护对应店铺商家ID", null);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    resp = JDrefundDownload(storeId, userId, ecOrderId, startDate, endDate);
                }

                break;
            case "TM":
                try {
                    List<String> dataList = new ArrayList<String>();
                    if (ecOrderId != null && ecOrderId.length() > 0) {
                        dataList.add(ecOrderId);
                    } else {
                        TMrefundList(storeSettings, pageNo, pageSize, startDate, endDate, dataList);
                    }
                    resp = TMrefundDownload(dataList, storeSettings, userId);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "PDD":
                try {
                    resp = PlatOrderPdd.pddRefund(userId, pageNo, pageSize, startDate, endDate, storeId);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
            case "SN":
                try {
                    resp = platOrderSN.snRefund(userId, pageNo, pageSize, startDate, endDate, storeId);
                    //resp = platOrderSN.snDownload(userId, pageNo, pageSize, startDate, endDate, storeSettings.getStoreId());
                } catch (Exception e1) {

                    e1.printStackTrace();
                }
                break;
		/*
		 * case "MaiDu": try { resp = platOrderMaiDu.maiDuDownload(userId, pageNo,
		 * pageSize, startDate, endDate, storeSettings.getStoreId()); } catch (Exception
		 * e1) {
		 * 
		 * e1.printStackTrace(); } break;
		 */
            case "KaoLa":
                resp = KaoLaDownload(userId, startDate, endDate, pageNo, pageSize, storeId, ecOrderId, "1", 0);
                break;
            case "XHS":
                resp = platOrderXHS.xhsRefundPlatOrder(userId, pageNo, pageSize, startDate, endDate, storeId, 0);
                break;
            case "XMYP":
                try {
                    resp = platOrderXMYP.XMYPRefundPlatOrderV2(userId, startDate, endDate, storeId, "", 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                try {
                    resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "当前店铺不支持退款单下载", null);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                break;
        }

        return resp;

    }

    private String JDrefundDownload(String storeId, String accNum, String ecOrderId, String startDate, String endDate) {
        String resp = "";
        int count = 0;
        try {
            count += aftermarketServiceImpl.download(storeId, accNum, ecOrderId, startDate, endDate);
            count += cancelOrderServiceImpl.download(storeId, ecOrderId, accNum, startDate, endDate);
            count += compensateServiceImpl.download(storeId, ecOrderId, accNum, startDate, endDate);
            resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "本次成功下载退款单" + count + "条", null);
        } catch (Exception e) {
            // TODO: handle exception
            try {
                resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "下载京东退款单出错，请检查", null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return resp;
    }

    private String PDDrefundDownload(StoreSettings storeSettings, String userId, String startDate, String endDate,
                                     int pageNo, int pageSize) {
        String resp = "";
        // TODO Auto-generated method stub
        String clientId = storeSettings.getAppKey();
        String clientSecret = storeSettings.getAppSecret();
        String accessToken = storeSettings.getAccessToken();

        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddRefundListIncrementGetRequest request = new PddRefundListIncrementGetRequest();
        request.setAfterSalesStatus(1);
        request.setAfterSalesType(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(startDate);
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            return "时间格式错误";
        }
        long start = date.getTime();
        try {
            date = simpleDateFormat.parse(endDate);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "时间格式错误";
        }
        long end = date.getTime();
        request.setEndUpdatedAt(start / 1000);
        request.setPage(pageNo);
        request.setPageSize(pageSize);
        request.setStartUpdatedAt(end / 1000);
        PddRefundListIncrementGetResponse response = null;
        try {
            response = client.syncInvoke(request, accessToken);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "请求出错";

        }
        System.out.println(JsonUtil.transferToJson(response));

        if (response.getErrorResponse() != null) {
            System.err.println(response.getErrorResponse().getErrorMsg());
            return response.getErrorResponse().getErrorMsg();
        }
        if (response.getRefundIncrementGetResponse().getTotalCount() == 0) {
            return "没有售后单";
        }
        List<RefundIncrementGetResponseRefundListItem> allRefNum = response.getRefundIncrementGetResponse()
                .getRefundList();
        int m = 0;

        for (RefundIncrementGetResponseRefundListItem item : allRefNum) {
            try {
                if (refundOrderDao.selectEcRefId(item.getId().toString()) != null) {
                    continue;
                }

                RefundOrder refundOrder = new RefundOrder();
                String ecRefId = item.getOrderSn();

                List<RefundOrders> RefundOrdersList = new ArrayList<>();
                System.err.println("ecRefId\n" + ecRefId);
//		 platOrderDao.selectByEcOrderId("11111111");
                // 查退货的订单主信息
                PlatOrder platOrder = platOrderDao.selectByEcOrderId(ecRefId);
                if (platOrder == null) {
                    throw new RuntimeException("没有对应订单:" + ecRefId);
                }
//		查对应的销售单 主
                SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(platOrder.getOrderId());
                if (sellSngl == null) {
                    throw new RuntimeException("没有销售单:" + platOrder.getOrderId());
                }
                // 店铺设置
                StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
                String refId = getOrderNo.getSeqNo("tk", userId);

                refundOrder.setRefId(refId); // 退款单编号
                refundOrder.setOrderId(platOrder.getOrderId()); // 订单编号
                refundOrder.setStoreId(platOrder.getStoreId()); // 店铺编号
                refundOrder.setStoreName(platOrder.getStoreName()); // 店铺名称
                refundOrder.setEcRefId(item.getId().toString()); // 电商退款单号
//			refundOrder.setApplyDate(jsonNode.get("created").asText()); // 申请日期
//			refundOrder.setBuyerId(jsonNode.get("buyer_open_uid").asText()); // 买家会员号
                refundOrder.setIsRefund(item.getAfterSalesType() == 2 ? 1 : 0); // 是否退货
                refundOrder.setAllRefNum(Integer.parseInt(item.getGoodsNumber())); // 整单退货数量
                refundOrder.setAllRefMoney(new BigDecimal(item.getRefundAmount())); // 整单退款金额
                refundOrder.setRefReason(item.getAfterSaleReason()); // 退款原因
//			refundOrder.setRefExplain(jsonNode.get("desc").asText()); // 退款说明
                refundOrder.setRefStatus(1); // 退款状态
                refundOrder.setDownTime(simpleDateFormat.format(new Date())); // 下载时间
//			refundOrder.setTreDate(treDate); // 处理日期
                refundOrder.setOperator(userId); // 操作员
                refundOrder.setIsAudit(0); // 是否审核
//			refundOrder.setMemo(memo); // 备注
//		查对应的销售单 子
                List<SellSnglSub> sellSnglSubs = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSngl.getSellSnglId());
//			// 根据sku和平台商品编码查询店铺商品档案
//			GoodRecord goodRecord = goodRecordDao.selectGoodRecordBySkuAndEcGoodId(jsonNode.get("sku").asText(),
//					jsonNode.get("num_iid").asText());
                // 根据商品id 查是否有对应的产品结构
                ProdStru prodStru = prodStruMapper.selectMomEncd(item.getOuterGoodsId());
                if (prodStru == null) {
                    for (SellSnglSub sub : sellSnglSubs) {
                        if (sub.getInvtyEncd().equals(item.getOuterGoodsId())) {
                            RefundOrders refundOrders = new RefundOrders();

                            refundOrders.setGoodId(sub.getInvtyEncd()); // 商品编号
                            refundOrders.setGoodName(sub.getInvtyNm()); // 商品名称
                            // refundOrders.setNo(no); // 序号
                            refundOrders.setRefId(refId); // 退款单编号
                            refundOrders.setEcGoodId(item.getGoodsId().toString()); // 平台商品
                            refundOrders.setGoodSku(item.getSkuId()); // 商品sku
//								refundOrders.setCanRefNum(canRefNum); // 可退货商品数量
//								refundOrders.setCanRefMoney(canRefMoney); // 可退货金额
                            refundOrders.setRefNum(Integer.parseInt(item.getGoodsNumber())); // 退货数量
                            refundOrders.setRefMoney(new BigDecimal(item.getRefundAmount())); // 退货金额
                            refundOrders.setBatchNo(sub.getBatNum()); // 批号
                            refundOrders.setRefWhs(storeRecord.getDefaultRefWhs()); // 退货仓库
//								refundOrders.setMemo(memo); // 备注
                            RefundOrdersList.add(refundOrders);

                        }
                    }
                } else {
                    // 有 将销售单子 与 产品结构 循环 对比数量乘母子比例

                    List<ProdStruSubTab> prodStruSubTabs = prodStru.getStruSubList();
                    for (SellSnglSub sub : sellSnglSubs) {
                        if (sub.getInvtyEncd().equals(item.getOuterGoodsId())) {

                            for (ProdStruSubTab batchNo : prodStruSubTabs) {

                                RefundOrders refundOrders = new RefundOrders();

                                refundOrders.setGoodId(batchNo.getSubEncd()); // 商品编号
                                refundOrders.setGoodName(batchNo.getSubNm()); // 商品名称
                                // refundOrders.setNo(no); // 序号
                                refundOrders.setRefId(refId); // 退款单编号

                                refundOrders.setEcGoodId(item.getGoodsId().toString()); // 平台商品
                                refundOrders.setGoodSku(item.getSkuId()); // 商品sku
//						refundOrders.setCanRefNum(canRefNum); // 可退货商品数量
//						refundOrders.setCanRefMoney(canRefMoney); // 可退货金额
                                Double bigDecimal = batchNo.getMomQty().doubleValue();
                                int i = bigDecimal.intValue();
                                refundOrders.setRefNum(Integer.parseInt(item.getGoodsNumber()) * i); // 退货数量
                                refundOrders.setRefMoney(new BigDecimal(item.getRefundAmount())); // 退货金额
                                refundOrders.setBatchNo(sub.getBatNum()); // 批号
                                refundOrders.setRefWhs(storeRecord.getDefaultRefWhs()); // 退货仓库
//						refundOrders.setMemo(memo); // 备注
                                RefundOrdersList.add(refundOrders);

                            }
                        }

                    }

                }

//			refundOrder.setRefundOrders(RefundOrdersList);
                if (RefundOrdersList.size() > 0) {
                    refundOrderDao.insert(refundOrder);
                    refundOrdersDao.insertList(RefundOrdersList);
                }

                m++;
            } catch (Exception e) {
                // TODO: handle exception
                resp += e.getMessage();
            }
        }

        return "PDD新增条数" + m + "条" + resp;
    }

    /**
     * storeSettings 店铺登陆设置 pageNo 开始页 pageSize 一页大小 startDate 开始时间 endDate 结束时间
     * dataList 传入的退货id集合
     */
    public void TMrefundList(StoreSettings storeSettings, int pageNo, int pageSize, String startDate, String endDate,
                             List<String> dataList) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        RefundsReceiveGetRequest req = new RefundsReceiveGetRequest();
        req.setFields(
                "refund_id, tid, title, buyer_nick, seller_nick, total_fee, status, created, refund_fee, oid, good_status, company_name, sid, payment, reason, desc, has_good_return, modified, order_status,refund_phase");
        req.setPageNo(Long.valueOf(pageNo));
        req.setPageSize(Long.valueOf(pageSize));
        req.setUseHasNext(true);
        req.setStartModified(simpleDateFormat.parse(startDate));// 查询修改时间开始。格式: yyyy-MM-dd HH:mm:ss
        req.setEndModified(simpleDateFormat.parse(endDate));//
        // 转发
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", RefundsReceiveGetRequest.class.getName());
        map.put("taobaoObject", JSON.toJSONString(req));
        String taobao = ECHelper.getTB("", storeSettings, map);
        RefundsReceiveGetResponse rsp = JSONObject.parseObject(taobao, RefundsReceiveGetResponse.class);
        logger.info("天猫body:" + rsp.getBody());
        if (!rsp.isSuccess()) {
            logger.error("天猫err:" + rsp.getSubMsg());
            return;
        }
        for (Refund refund : rsp.getRefunds()) {
            String status = refund.getStatus();
            if (status.equals("WAIT_BUYER_RETURN_GOODS") || status.equals("WAIT_SELLER_CONFIRM_GOODS")
                    || status.equals("SUCCESS")) {
                // WAIT_BUYER_RETURN_GOODS(卖家已经同意退款 等待买家退货)
                // WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
                // SUCCESS(退款成功)
                if (refundOrderDao.selectEcRefId(refund.getRefundId()) == null) {
                    dataList.add(refund.getRefundId());
                }
            }
        }
        if (rsp.getHasNext()) {
            TMrefundList(storeSettings, pageNo + 1, pageSize, startDate, endDate, dataList);

        }
        System.out.println(dataList.toString());
        return;
    }

    //@Autowired
//StoreRecordDao storeRecordDao;
    @Autowired
    GoodRecordDao goodRecordDao;
    @Autowired
    InvtyDocService invtyDocService;
    @Autowired
    ProdStruMapper prodStruMapper;
    @Autowired
    PlatOrdersDao platOrdersDao; // 订单子表

    /**
     * dataList 平台退货单id集合
     */
    public String TMrefundDownload(List<String> dataList, StoreSettings storeSettings, String userId) {
        SimpleDateFormat allRefNum = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String resp = "";
        RefundGetRequest req = new RefundGetRequest();
        req.setFields(
                "order_status,refund_id,alipay_no,tid,oid,buyer_nick,seller_nick,total_fee,status,created,refund_fee,good_status,has_good_return,payment,reason,desc,num_iid,title,price,num,good_return_time,company_name,sid,address,shipping_type,refund_remind_timeout,refund_phase,refund_version,operation_contraint,attribute,outer_id,sku");
        String message = "";
        MisUser misUser = misUserDao.select(userId);
        int m = 0;
        // 循环平台退货单id集合
        for (String list : dataList) {
            try {
                req.setRefundId(Long.valueOf(list));
                // 转发
                Map<String, String> map = new HashMap<String, String>();
                map.put("path", RefundGetRequest.class.getName());
                map.put("taobaoObject", JSON.toJSONString(req));
                String taobao = ECHelper.getTB("", storeSettings, map);
                RefundGetResponse rsp = JSONObject.parseObject(taobao, RefundGetResponse.class);

                logger.info("天猫body:" + rsp.getBody());
                if (!rsp.isSuccess()) {
                    logger.error("天猫err:" + rsp.getSubMsg());
                    break;
                }

                Refund refund = rsp.getRefund();
                RefundOrder refundOrder = new RefundOrder();
                String ecRefId = refund.getTid().toString();

                List<RefundOrders> RefundOrdersList = new ArrayList<>();
                System.err.println("ecRefId\n" + ecRefId);

                // TODO 订单
                List<PlatOrder> platOrderlist = platOrderDao.selectPlatOrdersByEcOrderId(ecRefId);

                if (platOrderlist.size() == 0) {
                    message = "退货单对应销售单不存在,平台订单号:" + ecRefId;
                    continue;
                }
                PlatOrder platOrder = platOrderlist.get(0);
				/*if (platOrder.getIsAudit() == null || platOrder.getIsAudit() == 0) {
					message = "退货单对应销售单未审核,平台订单号:" + ecRefId;
					continue;
				}*/

                // 销售单子表
                List<PlatOrders> platOrdersList = platOrdersDao.selectByEcOrderAndGood(ecRefId, refund.getNumIid().toString(),
                        refund.getSku() == null ? null : refund.getSku().substring(0, refund.getSku().indexOf("|")));
                platOrder.setPlatOrdersList(platOrdersList);

                // 店铺设置
                StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
                String refId = getOrderNo.getSeqNo("tk", userId);
                refundOrder.setExpressCode(refund.getSid()); // 快递单号
                refundOrder.setRefId(refId); // 退款单编号
                refundOrder.setEcId("TM");
                refundOrder.setOrderId(platOrder.getOrderId()); // 订单编号
                refundOrder.setStoreId(platOrder.getStoreId()); // 店铺编号
                refundOrder.setEcOrderId(platOrder.getEcOrderId());
                refundOrder.setStoreName(platOrder.getStoreName()); // 店铺名称
                refundOrder.setEcRefId(refund.getRefundId().toString()); // 电商退款单号
                refundOrder.setApplyDate(allRefNum.format(refund.getCreated())); // 申请日期
                refundOrder.setBuyerId(refund.getBuyerOpenUid()); // 买家会员号
                refundOrder.setIsRefund(refund.getHasGoodReturn() == true ? 1 : 0); // 是否退货
                refundOrder.setAllRefNum(refund.getNum().intValue()); // 整单退货数量
                refundOrder.setAllRefMoney(new BigDecimal(refund.getRefundFee())); // 整单退款金额
                refundOrder.setRefReason(refund.getReason()); // 退款原因
                refundOrder.setRefExplain(refund.getDesc()); // 退款说明
                refundOrder.setRefStatus(1); // 退款状态
                refundOrder.setDownTime(allRefNum.format(new Date())); // 下载时间
                refundOrder.setTreDate(sdf.format(new Date())); // 处理日期
                refundOrder.setOperator(userId); // 操作员
                refundOrder.setIsAudit(0); // 是否审核
//			refundOrder.setMemo(memo); // 备注
                refundOrder.setSource(0);//退款单来源设置0
                refundOrder.setOperatorId(misUser.getAccNum());
                refundOrder.setOperatorTime(sdf.format(new Date()));
                List<RefundOrders> ordersList = new ArrayList<>();
                BigDecimal total = new BigDecimal("0");
                for (PlatOrders orders : platOrdersList) {
                    total.add(orders.getPayMoney());
                }
                int ii = 0;
                for (PlatOrders orders : platOrdersList) {
                    ii++;
                    RefundOrders refundOrders = new RefundOrders();

                    if (refund.getNum().intValue() == orders.getGoodNum()) {
                        refundOrders.setRefNum(orders.getInvNum()); // 退货数量

                        refundOrders.setRefMoney(orders.getPayMoney()); // 退货金额

                    } else {

                        if (ii == platOrdersList.size()) {
                            Integer integers = orders.getInvNum() * orders.getGoodNum() / refund.getNum().intValue();
                            refundOrders.setRefNum(integers); // 退货数量
                            refundOrders.setRefMoney(total); // 退货金额
                        } else {
                            Integer integer = orders.getInvNum() * orders.getGoodNum() / refund.getNum().intValue();
                            refundOrders.setRefNum(integer); // 退货数量
                            refundOrders.setRefMoney(orders.getPayPrice().multiply(new BigDecimal(integer))); // 退货金额
                            total.subtract(refundOrders.getRefMoney());
                        }

                    }
                    refundOrders.setPrdcDt(orders.getPrdcDt());// 生产日期
                    refundOrders.setInvldtnDt(orders.getInvldtnDt());// 失效日期
                    refundOrders.setGoodId(orders.getInvId());
                    InvtyDoc doc = invtyDocDao.selectAllByInvtyEncd(orders.getInvId());
                    if (doc == null) {
                        refundOrders.setBaozhiqi(Integer.valueOf(doc.getBaoZhiQiDt()));// 保质期天数
                    } else {
                        refundOrders.setBaozhiqi(null);// 保质期天数
                    }
                    refundOrders.setIsGift(orders.getIsGift());// 是否赠品
                    refundOrders.setBatchNo(orders.getBatchNo());// 批号
                    refundOrders.setCanRefMoney(orders.getCanRefMoney());// 可退货金额
                    refundOrders.setCanRefNum(orders.getCanRefNum());// 可退货数量
                    refundOrders.setGoodName(orders.getGoodName());
                    refundOrders.setRefId(refId); // 退款单编号
                    if (new BigDecimal(refund.getRefundFee()).compareTo(BigDecimal.ZERO) == 0) {
                        refundOrders.setIsGift(1); // 是否赠品
                    } else {
                        refundOrders.setIsGift(0); // 是否赠品
                    }
                    refundOrders.setGoodSku(orders.getGoodSku()); // 商品sku
                    refundOrders.setEcGoodId(refund.getNumIid().toString()); // 平台商品

                    if (refund.getOrderStatus().equals("WAIT_SELLER_SEND_GOODS")) {
                        // 等待卖家发货,即:买家已付款
                        refundOrders.setRefWhs(orders.getDeliverWhs()); // 退货仓库
                    } else {
                        refundOrders.setRefWhs(storeRecord.getDefaultRefWhs()); // 退货仓库
                    }

                    ordersList.add(refundOrders);

                }

                refundOrder.setRefundOrders(ordersList);
                // 苏宁的退货
//				refundOrder = platOrderSN.checkRefundIsPto(refundOrder);

                // insert
                refundOrderDao.insert(refundOrder);
                refundOrdersDao.insertList(ordersList);

//                BigDecimal he = new BigDecimal(0);
//                List<RefundOrders> refundOrderslist = new ArrayList<RefundOrders>();
//                for (PlatOrders orders : platOrdersList) {
//                    if (refundOrders.getGoodSku().equals(orders.getGoodSku())
//                            && refundOrders.getEcGoodId().equals(orders.getGoodId())) {
//                        // 计算和
//                        he.add(orders.getPayMoney());
//
//                        RefundOrders order = new RefundOrders();
//                        refundOrders.setGoodId(orders.getInvId());// 商品编号
//                        refundOrders.setGoodName(orders.getPtoName());// 商品名称
//                        refundOrders.setCanRefNum(orders.getCanRefNum());// 可退货数量
//                        refundOrders.setCanRefMoney(orders.getCanRefMoney());// 可退货金额
//                        refundOrders.setBatchNo(orders.getBatchNo());// 批号
//                        refundOrders.setPrdcDt(orders.getPrdcDt());// 生产日期
//                        refundOrders.setInvldtnDt(orders.getInvldtnDt());// 失效日期
//                        BeanUtils.copyProperties(order, refundOrders);
//                        refundOrderslist.add(order);
//                    }

                m++;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                logger.error("URL：ec/refundOrder/download 异常说明：", e);
//                resp += e.getMessage() + "\n";
            }
        }
        message = "本次成功下载天猫退款单：" + m + "条";
        try {
            resp = BaseJson.returnResp("ec/refundOrder/download ", true, message, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    public String exportList(String jsonBody) {
        String message = "";
        Boolean isSuccess = true;
        String resp = "";
        try {
            Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            String applyDateStart = (String) map.get("applyDateStart");
            String applyDateEnd = (String) map.get("applyDateEnd");
            if (StringUtils.isNotEmpty(applyDateStart)) {
                applyDateStart = applyDateStart + " 00:00:00";
                map.put("applyDateStart", applyDateStart);
            }
            if (StringUtils.isNotEmpty(applyDateEnd)) {
                applyDateEnd = applyDateEnd + " 23:59:59";
                map.put("applyDateEnd", applyDateEnd);
            }
            String treDateStart = (String) map.get("treDateStart");
            String treDateEnd = (String) map.get("treDateEnd");
            if (StringUtils.isNotEmpty(treDateStart)) {
                treDateStart = treDateStart + " 00:00:00";
                map.put("treDateStart", treDateStart);
            }
            if (StringUtils.isNotEmpty(treDateEnd)) {
                treDateEnd = treDateEnd + " 23:59:59";
                map.put("treDateEnd", treDateEnd);
            }

            List<Map> refundOrderList = refundOrderDao.exportList(map);
            message = "查询成功";
            isSuccess = true;
            resp = BaseJson.returnRespList("ec/refundOrder/exportList", isSuccess, message, refundOrderList);
        } catch (Exception e) {
            logger.error("URL：ec/refundOrder/exportList 异常说明：", e);
        }
        return resp;
    }

    /**
     * 考拉退款下载
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @param storeId
     * @param ecOrderId
     * @param state     1.待处理2.待退款3.待用户修改4.待考拉仲裁5.已完成
     * @return
     */
    private String KaoLaDownload(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId,
                                 String ecOrderId, String state, int downloadCount) {
        String resp = "";

        if (state.equals("1") && pageSize == 1) {
            //当下载第一种情况和第一页时将数量置0
            downloadCount = 0;
        }
        StoreRecord storeRecord = storeRecordDao.select(storeId);
        try {
            MisUser misUser = misUserDao.select(userId);
            String date = sdf.format(new Date());

            ObjectNode objectNode = JacksonUtil.getObjectNode("");
            objectNode.put("refund_status", state);//1.待处理2.待退款3.待用户修改4.待考拉仲裁5.已完成
            objectNode.put("start_time", startDate);
            objectNode.put("end_time", endDate);
            objectNode.put("page_no", "" + pageNo);
            objectNode.put("page_size", "" + pageSize);
            String json = objectNode.toString();
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            String jdRespStr = ECHelper.getKaola("kaola.refund.search", storeSettings, json);
            ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
            if (jdRespJson.has("error_response")) {
                return resp = BaseJson.returnResp("ec/refundOrder/download ", true,
                        jdRespJson.get("error_response").get("subErrors").get(0).get("msg").asText(), null);
            }
            //System.err.println("查询结果："+jdRespJson);
            List<RefundOrder> refundOrderList = new ArrayList();
            List<RefundOrders> refundOrdersList = new ArrayList();
            ArrayNode orderInfoList = (ArrayNode) jdRespJson.get("kaola_refund_search_response").get("refunds");
            // System.out.println(resultJson);
            for (Iterator<JsonNode> orderInfoIterator = orderInfoList.iterator(); orderInfoIterator.hasNext(); ) {
                JsonNode orderInfo = orderInfoIterator.next();
                //售后订单详细状态（1.申请中2.同意退款3. 拒绝退款,等待用户修改4. 同意退货,等待用户发货5. 用户已退货，等待验货
                //6. 已确认收货7. 售后完成：发起退款8. 系统超时关闭9. 用户已撤销10. 售后完成：退款完成
                //11. 等待商家确认12. 商家拒绝退货退款-待审核13. 商家拒绝仅退款-待审核14. 客服关闭售后15. 客服挂起售后）
                if (orderInfo.get("refund_status_detail").asInt() == 1 || orderInfo.get("refund_status_detail").asInt() == 3
                        || orderInfo.get("refund_status_detail").asInt() == 8 || orderInfo.get("refund_status_detail").asInt() == 9
                        || orderInfo.get("refund_status_detail").asInt() == 12 || orderInfo.get("refund_status_detail").asInt() == 13
                        || orderInfo.get("refund_status_detail").asInt() == 14 || orderInfo.get("refund_status_detail").asInt() == 15) {
                    //退款单状态不符合生成退款单的条件
                    continue;
                }
                ecOrderId = orderInfo.get("order_id").asText();
                if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
                    //原订单已删除,不存在
                    continue;
                }
                //判断退款单是否已存在
                RefundOrder refundOrder11 = refundOrderDao.selectEcRefId(orderInfo.get("refund_id").asText());
                if (refundOrder11 != null) {
                    continue;
                }
                //用订单号去查询原单的购买数量以及实付金额
                List<PlatOrders> platOrders = platOrdersDao.selectByEcOrderId(ecOrderId);
                if (platOrders.size() == 0) {
                    // 日志记录
                    LogRecord logRecord = new LogRecord();
                    logRecord.setOperatId(userId);
                    if (misUser != null) {
                        logRecord.setOperatName(misUser.getUserName());
                    }
                    logRecord.setOperatContent("退款单下载，订单：" + ecOrderId + "未找到订单明细，本次下载失败。");
                    logRecord.setOperatTime(sdf.format(new Date()));
                    logRecord.setOperatType(12);// 12退款下载
                    logRecord.setTypeName("退款单下载");
                    logRecord.setOperatOrder(ecOrderId);
                    logRecordDao.insert(logRecord);
                } else {
                    boolean flag = true;
                    for (int i = 0; i < platOrders.size(); i++) {
                        //循环判断原订单是否存在未匹配到存货档案的记录
                        if (!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        // 日志记录
                        LogRecord logRecord = new LogRecord();
                        logRecord.setOperatId(userId);
                        if (misUser != null) {
                            logRecord.setOperatName(misUser.getUserName());
                        }
                        logRecord.setOperatContent("退款单下载，订单：" + ecOrderId + "存在未匹配到存货档案的记录，本次下载失败。");
                        logRecord.setOperatTime(sdf.format(new Date()));
                        logRecord.setOperatType(12);// 12退款下载
                        logRecord.setTypeName("退款单下载");
                        logRecord.setOperatOrder(ecOrderId);
                        logRecordDao.insert(logRecord);
                        continue;
                    }
                }


                RefundOrder refundOrder = new RefundOrder();

                String refId = getOrderNo.getSeqNo("tk", userId);
                refundOrder.setRefId(refId);// 退款单编号
                refundOrder.setStoreId(storeId);// 店铺编号
                refundOrder.setEcRefId(orderInfo.get("refund_id").asText());//电商退款单号
                refundOrder.setOrderId(ecOrderId);//电商订单号
                refundOrder.setEcOrderId(ecOrderId);//电商平台订单号
                refundOrder.setSource(0);//设置退款单来源为京东自主售后
                refundOrder.setSourceNo("");//设置来源单据号为自主售后的服务单号
                refundOrder.setOperatorId(misUser.getAccNum());
                refundOrder.setOperatorTime(date);
                refundOrder.setExpressCode(orderInfo.get("express_no").asText());//用户寄回快递单号2019年12月31日19:13:36
                refundOrder.setRefReason(orderInfo.get("refund_reason").asText());//退款原因
                refundOrder.setRefExplain(orderInfo.get("refund_reason").asText());//退款说明
                refundOrder.setStoreName(storeRecord.getStoreName());//店铺名称
//				    private String storeName;//店铺名称
                refundOrder.setApplyDate(orderInfo.get("refund_create_time").asText());// 申请日期
                refundOrder.setBuyerId(orderInfo.get("user_account").asText().equals("null") ? "" : orderInfo.get("user_account").asText());//买家会员号
                refundOrder.setDownTime(date);//设置下载时间为当前时间
                refundOrder.setOperator(misUser.getUserName());
                refundOrder.setIsAudit(0);
                refundOrder.setTreDate(date);//退款单处理时间用平台的审核时间
                int allReturnCount = 0;
                BigDecimal allReturnMoney = BigDecimal.ZERO;


                ObjectNode objectNode1 = JacksonUtil.getObjectNode("");
                objectNode1.put("refund_id", orderInfo.get("refund_id").asText());
                String json1 = objectNode1.toString();

                String jdRespStr1 = ECHelper.getKaola("kaola.refund.get", storeSettings, json1);
                ObjectNode jdRespJson1 = JacksonUtil.getObjectNode(jdRespStr1);

                ///System.err.println(jdRespJson1);
                ArrayNode orderDetailList = (ArrayNode) jdRespJson1.get("kaola_refund_get_response").get("refundDetailInfoDTO").get("refund_skus");
                refundOrder.setBuyerId(jdRespJson1.get("kaola_refund_get_response").get("refundDetailInfoDTO").get("user_account").asText());

                for (Iterator<JsonNode> orderDetail = orderDetailList.iterator(); orderDetail.hasNext(); ) {
                    JsonNode node = orderDetail.next();

                    List<PlatOrders> platOrders2 = platOrdersDao.selectByEcOrderAndGood(ecOrderId, node.get("barcode").asText(), "");
                    if (platOrders2.size() > 0) {
                        for (int i = 0; i < platOrders2.size(); i++) {
                            //直接取当前条明细生成退款单
                            RefundOrders refundOrders = new RefundOrders();
                            refundOrders.setRefId(refId);
                            refundOrders.setGoodSku(StringUtils.isEmpty(platOrders2.get(i).getGoodSku()) ? ""
                                    : platOrders2.get(i).getGoodSku());//商品sku
                            refundOrders.setEcGoodId(platOrders2.get(i).getGoodId());//设置店铺商品编码
                            InvtyDoc invtyDoc = invtyDocDao
                                    .selectInvtyDocByInvtyDocEncd(platOrders2.get(i).getInvId());

                            if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
                                refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//设置保质期
                            }
                            refundOrders.setIsGift(platOrders2.get(i).getIsGift());//是否赠品用原订单属性
                            refundOrders.setPrdcDt(platOrders2.get(i).getPrdcDt());//设置生产日期
                            refundOrders.setInvldtnDt(platOrders2.get(i).getInvldtnDt());//设置失效日期
                            refundOrders.setCanRefNum(platOrders2.get(i).getCanRefNum());//设置可退数量
                            refundOrders.setCanRefMoney(platOrders2.get(i).getCanRefMoney());//可退金额
                            refundOrders.setRefMoney(new BigDecimal(orderInfo.get("refund_fee").asText()));//退货金额
                            if (orderInfo.get("refund_type").asInt() == 0) {//0退货退款
                                refundOrders.setRefNum(platOrders2.get(i).getInvNum() * node.get("refund_count").asInt() / platOrders2.get(i).getGoodNum());//设置退货数量,退货数量*存货数量/购买数量
                            } else {
                                //仅退款
                                refundOrders.setRefNum(0);//设置退货数量
                            }
                            allReturnCount += refundOrders.getRefNum();
                            allReturnMoney = allReturnMoney.add(refundOrders.getRefMoney());
                            refundOrders.setGoodId(platOrders2.get(i).getInvId());//设置存货编码

                            refundOrders.setGoodName(invtyDoc.getInvtyNm());//设置存货名称
                            refundOrders.setBatchNo(platOrders2.get(i).getBatchNo());//设置退货批次
                            refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//取对应店铺的默认退货仓
                            refundOrdersList.add(refundOrders);//装入list
                        }
                    }
                }
                refundOrder.setRefStatus(0);//退款状态暂时先设置成0,不知道有啥用
                refundOrder.setAllRefNum(allReturnCount);
                refundOrder.setAllRefMoney(allReturnMoney);
                if (refundOrder.getAllRefNum() > 0) {
                    refundOrder.setIsRefund(1);//退货数量大于0时是否退货1
                } else {
                    refundOrder.setIsRefund(0);
                }
                refundOrderList.add(refundOrder);


            }
            if (refundOrderList.size() > 0) {
                refundOrderDao.insertList(refundOrderList);
                refundOrdersDao.insertList(refundOrdersList);
                downloadCount += refundOrderList.size();
            }
            if (pageNo * pageSize < jdRespJson.get("kaola_refund_search_response").get("total_count").asInt()) {
                resp = KaoLaDownload(userId, startDate, endDate, pageNo + 1, pageSize, storeId, ecOrderId, state, downloadCount);
            } else if (state.equals("1")) {
                resp = KaoLaDownload(userId, startDate, endDate, 1, pageSize, storeId, ecOrderId, "2", downloadCount);
            } else if (state.equals("2")) {
                resp = KaoLaDownload(userId, startDate, endDate, 1, pageSize, storeId, ecOrderId, "5", downloadCount);
            } else {
                // 日志记录
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent(
                        "退款单下载，本次成功下载考拉店铺：" + storeRecord.getStoreName() + ",退款单" + downloadCount + "条");
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(12);// 12退款下载
                logRecord.setTypeName("退款下载");
                logRecordDao.insert(logRecord);
                resp = BaseJson.returnResp("ec/refundOrder/download ", true,
                        "退款单下载，本次成功下载考拉店铺：" + storeRecord.getStoreName() + ",退款单" + downloadCount + "条", null);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            try {
                resp = BaseJson.returnResp("ec/refundOrder/download ", false, "退款单下载，下载" + downloadCount + "条后出现异常", null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

        return resp;
    }

    @Override
    public String unionQuery(String refId) {

        // TODO Auto-generated method stub
        String resp = "";
        String message = "查询成功";
        boolean success = true;
        RefundOrder refundOrder = refundOrderDao.select(refId);
        List<UnionQuery> unionQueries = new ArrayList<>();
        if (refundOrder == null) {
            message = "所查询退款单不存在";
        } else if (refundOrder.getIsAudit() == 0) {
            // 所查询订单尚未审核，没有关联单据
            message = "当前退款单未审核，没有关联单据";
        } else {
            // 查询退款单对应退货单
            // 查询退货单对应销售出库单
            // 查询对应销售出库单
            RtnGoods rtnGoods = refundOrderDao.selectRtnGoodsByRefId(refundOrder.getRefId());

            if (rtnGoods != null) {
                UnionQuery unionQuery = new UnionQuery();
                unionQuery.setAudit("" + rtnGoods.getIsNtChk());
                unionQuery.setOrderId(rtnGoods.getRtnGoodsId());
                unionQuery.setType(1);// 设置单据类型为销售单
                unionQuery.setOrderName("退货单");
                unionQueries.add(unionQuery);
                if (rtnGoods.getIsNtChk() == 1) {
                    // 退货单已审核，才有销售出库单
                    SellOutWhs sellOutWhs = refundOrderDao.selectSellOutWhsByRtnId(rtnGoods.getRtnGoodsId());
                    if (sellOutWhs != null) {
                        UnionQuery unionQuery1 = new UnionQuery();
                        unionQuery1.setAudit("" + sellOutWhs.getIsNtChk());
                        unionQuery1.setOrderId(sellOutWhs.getOutWhsId());
                        unionQuery1.setType(2);// 设置单据类型为销售出库单
                        unionQuery1.setOrderName("销售出库单");
                        unionQueries.add(unionQuery1);
                    }
                }
            }

        }
        try {
            resp = BaseJson.returnRespObjList("ec/refundOrder/unionQuery", success, message, null, unionQueries);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(resp);
        return resp;

    }
}
