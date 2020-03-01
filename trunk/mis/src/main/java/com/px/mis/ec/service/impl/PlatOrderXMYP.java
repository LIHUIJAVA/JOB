package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.px.mis.ec.util.ECHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.CouponDetailDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrderPaymethodDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.entity.EcExpress;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.InvoiceInfo;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrderPaymethod;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.ec.util.OpenApiUtils;
import com.px.mis.ec.util.PartnerInfo;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.ExpressCorpMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ExpressCorp;
import com.suning.api.DefaultSuningClient;
import com.suning.api.entity.custom.DeliverynewAddRequest;
import com.suning.api.entity.custom.DeliverynewAddRequest.OrderLineNumbers;
import com.suning.api.entity.custom.DeliverynewAddResponse;
import com.suning.api.entity.custom.DeliverynewAddResponse.SendDetail;
import com.suning.api.entity.custom.orderGetGetRequest;
import com.suning.api.entity.custom.orderGetGetResponse;
import com.suning.api.entity.custom.orderGetGetResponse.OrderGet;
import com.suning.api.entity.custom.orderQueryQueryRequest;
import com.suning.api.entity.custom.orderQueryQueryResponse;
import com.suning.api.entity.custom.orderQueryQueryResponse.CouponList;
import com.suning.api.entity.custom.orderQueryQueryResponse.OrderDetail;
import com.suning.api.entity.custom.orderQueryQueryResponse.OrderQuery;
import com.suning.api.entity.custom.orderQueryQueryResponse.PaymentList;
import com.suning.api.entity.item.ItemQueryRequest;
import com.suning.api.entity.item.ItemQueryResponse;
import com.suning.api.entity.item.ItemQueryResponse.ItemQuery;
import com.suning.api.entity.rejected.BatchrejectedQueryRequest;
import com.suning.api.entity.rejected.BatchrejectedQueryResponse;
import com.suning.api.entity.rejected.Rejected;
import com.suning.api.entity.transaction.LogisticcompanyGetRequest;
import com.suning.api.entity.transaction.LogisticcompanyGetResponse;
import com.suning.api.exception.SuningApiException;

/**
 * 订单-小米有品
 */
@Component
@Transactional
public class PlatOrderXMYP {

    @Autowired
    private StoreSettingsDao storeSettingsDao; //店铺设置
    @Autowired
    private PlatOrderDao platOrderDao; //订单主表
    @Autowired
    private StoreRecordDao storeRecordDao; //店铺档案
    @Autowired
    private PlatOrdersDao platOrdersDao; //订单子表
    @Autowired
    private GetOrderNo getOrderNo; //生成订单号
    @Autowired
    private GoodRecordDao goodRecordDao; //商品档案
    @Autowired
    private InvtyDocDao invtyDocDao; //存货档案
    @Autowired
    private ProdStruMapper prodStruDao; // 产品结构
    @Autowired
    private CouponDetailDao couponDetailDao; //优惠明细
    @Autowired
    private PlatOrderPaymethodDao paymethodDao; //支付明细
    @Autowired
    private RefundOrderDao refundOrderDao; //退款单主表
    @Autowired
    private RefundOrdersDao refundOrdersDao; //退款单子表
    @Autowired
    private ExpressCorpMapper expressCorpDao;//快递公司
    @Autowired
    private PlatOrderPdd platOrderPdd;
    @Autowired
    private AuditStrategyService auditStrategyService;
    @Autowired
    private AssociatedSearchService associatedSearchService;
    @Autowired
    private LogRecordDao logRecordDao;
    @Autowired
    private MisUserDao misUserDao;
    @Autowired
    private PlatOrderService platOrderService;
    @Autowired
    private PlatOrderServiceImpl orderServiceImpl;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Logger logger = LoggerFactory.getLogger(PlatOrderSN.class);


    /**
     * 批量订单下载-小米有品
     */
    public String XMYPDownload(String accNum, int pageNo, int pageSize, String startDate, String endDate,
                               String storeId, Map<String, Integer> map) throws Exception {
        String resp = "";
        String message = "";
        boolean isSuccess = true;
        String method = "orderlist";
        try {


            // 设置
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            StoreRecord storeRecord = storeRecordDao.select(storeId);

            Map<String, String> data = new HashMap<>();
            data.put("status", "4,5,6");
            Date date = simpleDateFormat.parse(startDate);
            long start = date.getTime() / 1000;
            date = simpleDateFormat.parse(endDate);
            long end = date.getTime() / 1000;
            data.put("beginTime", start + "");
            data.put("endTime", end + "");
            int pageStart = (pageNo - 1) * pageSize;
            int pageEnd = pageNo * pageSize;
            data.put("start", pageStart + "");
            data.put("end", pageEnd + "");

            JsonOrderBean jsonOrderBean = XMYPapi(method, data, storeSettings, JsonOrderBean.class);
            if (jsonOrderBean.getResult() == null) {
                message = "查询时间无数据或其他原因";
                return resp = BaseJson.returnRespObj("ec/platOrder/download", false, message, null);

            }
            resp = XMYPOrdr(jsonOrderBean, storeId, accNum, storeRecord, map);
            if (jsonOrderBean.getResult().getTotal() > pageEnd) {
                resp = XMYPDownload(accNum, pageNo + 1, pageSize, startDate, endDate, storeId, map);
            }
            message = "本次自动下载店铺：" + storeRecord.getStoreName() + "订单" + map.get("keys") + "条";


            resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);


        } catch (Exception e) {
            logger.error("下载错误" + e.getMessage(), e);
            message = "下载错误";
            resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
        }

        return resp;

    }

    /**
     * 单条订单下载-小米有品
     *
     * @throws Exception
     */
    public String XMYPDownloadByOrderId(String accNum, String orderId, String storeId) throws Exception {
        String resp = "";
        String message = "";
        boolean isSuccess = true;
        String method = "orderstatus";
        try {

            Map<String, Integer> mapS = new HashMap<String, Integer>();
            mapS.put("keys", 0);
            Map<String, String> data = new HashMap<>();
            data.put("order_ids", Arrays.asList(orderId).toString());
            // 设置
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            StoreRecord storeRecord = storeRecordDao.select(storeId);

//            StoreSettings storeSettings = new StoreSettings();
//            StoreRecord storeRecord = new StoreRecord();

            JsonOrderBean jsonOrderBean = XMYPapi(method, data, storeSettings, JsonOrderBean.class);
            resp = XMYPOrdr(jsonOrderBean, storeId, accNum, storeRecord, mapS);
        } catch (Exception e) {


            resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
        }
        return resp;

    }

    /**
     * 订单下载-小米有品
     *
     * @throws Exception
     */
    public String XMYPOrdr(JsonOrderBean jsonOrderBean, String storeId, String userId, StoreRecord storeRecord, Map<String, Integer> map) throws Exception {
        String resp = "";
        String message = "";
        boolean isSucess = true;
        if (jsonOrderBean.getCode() != 0 || jsonOrderBean.getResult() == null) {
            message = "下载失败";
            return BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);
        }

        List<JsonOrderBean.Result.Data> datas = jsonOrderBean.getResult().getData();
        for (JsonOrderBean.Result.Data data : datas) {
// 查询对应店铺是否存在此订单

            if (platOrderDao.checkExsits(data.getOrder_id(), storeId) == 0) {
                map.put("keys", map.get("keys") + 1);
                ObjectMapper mapper = new ObjectMapper();
                address address = mapper.readValue(data.getAddress(), address.class);
                // 订单不存在
//                maps.put("downloadCount", maps.get("downloadCount") + 1);


                String orderId = getOrderNo.getSeqNo("ec", userId);

                PlatOrder platOrder = new PlatOrder();

                platOrder.setEcOrderId(data.getOrder_id());// 电商订单号
                platOrder.setOrderId(orderId); // 订单编号
                platOrder.setStoreId(storeId); // 店铺编号
                platOrder.setStoreName(storeRecord.getStoreName()); // '店铺名称' 1
                platOrder.setPayTime(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.getFtime() * 1000))); // 付款时间'
                // t
                // paytime
//				platOrder.setWaif(waif);		//	'旗标'
                platOrder.setIsAudit(0); // '是否客审'
//				platOrder.setAuditHint(auditHint); // '审核提示'
                // totalfee
                // payment
                platOrder.setBuyerNote(data.getDescription().length() == 0 ? null : data.getDescription()); // 买家留言' t buyermessage
//		platOrder.setSellerNote(trade.getSellerMemo()); // 卖家备注'
                platOrder.setRecAddress(address.getProvince().getName() +
                        address.getCity().getName() + address.getDistrict().getName()
                        + address.getArea().getName() + address.getAddress()); // 收货人详细地址' t receiveraddress
//		platOrder.setBuyerId(buyerId); // '买家会员号'
                platOrder.setRecName(address.getConsignee()); // 收货人姓名' t receivername
                platOrder.setRecMobile(address.getTel()); // 收货人手机号' t receivermobile
                platOrder.setIsInvoice(0); // '是否开票'
                platOrder.setInvoiceTitle(data.getInvoice_title()); // '发票抬头'
//		platOrder.setNoteFlag(trade.getSellerFlag().intValue()); // 卖家备注旗帜' t sellerflag
                platOrder.setIsClose(0); // '是否关闭'
                // discountfee
                platOrder.setOrderStatus(0); // '订单状态'
                platOrder.setReturnStatus(0); // '退货状态'
                platOrder.setPayMoney(BigDecimal.ZERO); // '实付金额
                platOrder.setGoodMoney(BigDecimal.ZERO); // 商品金额
                platOrder.setDiscountMoney(BigDecimal.ZERO); // 系统优惠金额
                platOrder.setAdjustMoney(BigDecimal.ZERO); // 卖家调整金额'
                // platOrder.setMemo(memo); // '备注'
                // platOrder.setAdjustStatus(adjustStatus); // '调整标识'
                platOrder.setTradeDt(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.getCtime() * 1000))); // '交易时间'
                // t
                // created
                platOrder.setSellTypId("1");// 设置销售类型普通销售
                platOrder.setBizTypId("2");// 设置业务类型2c
                platOrder.setRecvSendCateId("6");// 设置收发类别
// 订单中商户的商品是否全部发货[1-是,0-否]
                platOrder.setIsShip(data.getAlldelivery()); // '是否发货'

//		platOrder.setRecvSendCateId(recvSendCateId); // '收发类别编号'
                platOrder.setOrderSellerPrice(data.getTotal_price()); // 结算金额（订单实际收入金额）'订单支付金额(包含运费)

                // receivedpayment
                platOrder.setProvince(address.getProvince().getName()); // 省' t receiverstate
                // platOrder.setProvinceId(provinceId); // '省id'
                platOrder.setCity(address.getCity().getName()); // 市' t receivercity
                // platOrder.setCityId(cityId); //
                platOrder.setCounty(address.getDistrict().getName()); // 区' t receiverdistrict
                // platOrder.setCountyId(countyId); //
                platOrder.setTown(address.getArea().getName()); // '镇'
                // platOrder.setTownId(townId); // '镇id'
                platOrder.setFreightPrice(data.getShip_fee()); // 运费' t

                platOrder.setVenderId("XMYP"); // '商家id'

                // orders Order[]
                platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                // "delivery_time": 1, // 配送时间 '1-不限送货时间,2-工作日送货,3-双休日/假日送货',
                // //'送货（日期）类型（1-只工作日送货(双休日、假日不用送;2-只双休日、假日送货(工作日不用送;3-工作日、双休日与假日均可送货;其他值-返回“任意时间”

                if (data.getDelivery_time() == 1) {
                    platOrder.setDeliveryType("任意时间");

                } else if (data.getDelivery_time() == 2) {
                    platOrder.setDeliveryType("1");

                } else if (data.getDelivery_time() == 3) {
                    platOrder.setDeliveryType("2");

                } else {
                    platOrder.setDeliveryType(data.getDelivery_time() + "");

                }
                // 发货主体 (65为有品配送，非65为商家自发)
                platOrder.setDeliverSelf(data.getConsignor() == 65 ? 0 : 1); // '订单是否自发货，0平台仓发货，1自发货' t


                List<PlatOrders> list = new ArrayList<PlatOrders>();
                List<CouponDetail> couponList = new ArrayList<>();
                List<JsonOrderBean.Result.Data.Products> products = data.getProducts();
                for (JsonOrderBean.Result.Data.Products product : products) {
                    platOrder.setExpressNo(product.getProduct_express_sn());

                    PlatOrders platOrders = new PlatOrders();
                    platOrders.setGoodId(product.getPid()); // 平台商品编号 商品数字ID
                    platOrders.setGoodName(product.getName()); //  平台商品名称
                    platOrders.setGoodNum(product.getCount()); //  商品数量
                    BigDecimal goodMoney = product.getProduct_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(product.getCount()));
                    platOrders.setGoodMoney(goodMoney); // 商品金额'
                    platOrder.setGoodMoney(platOrder.getGoodMoney().add(goodMoney)); // 商品金额

                    platOrders.setGoodPrice(product.getProduct_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));// 商品单价
                    platOrders.setPayPrice(product.getSale_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));// 实付单价
                    BigDecimal payMoney = product.getSale_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(product.getCount()));
                    platOrders.setPayMoney(payMoney); // 实付金额
                    platOrder.setPayMoney(platOrder.getPayMoney().add(payMoney)); // 实付金额

                    platOrders.setGoodSku(product.getGid()); // '商品sku'
                    platOrders.setOrderId(orderId); // '订单编号' 1
//				platOrders.setExpressCom(xhstotal.getString("express_company_code")); // 快递公司' t子订单发货的快递公司名称
//			platOrders.setBatchNo(batchNo);	//	'批号'
//			promotion_details		platOrders.setProActId(proActId);	//	促销活动编号' t promotionid	String	mjs	优惠id
                    BigDecimal discountMoney = goodMoney.subtract(payMoney);
                    platOrders.setDiscountMoney(discountMoney); // 系统优惠金额
                    platOrder.setDiscountMoney(platOrder.getDiscountMoney().add(discountMoney)); // 系统优惠金额
                    platOrders.setAdjustMoney(BigDecimal.ZERO); // 买家调整金额'
//			platOrders.setMemo(order.getOid().toString()); // '备注' 该 子单id oid
//			platOrders.setDeliverWhs(order.getStoreCode()); // 发货仓库编码'
                    platOrders.setEcOrderId(product.getOrder_id()); // '平台订单号' t oid


//                    platOrder.setDeliverWhs("");
                    //是否赠品
                    if (payMoney != null && payMoney.compareTo(BigDecimal.ZERO) == 0) {

                        platOrders.setIsGift(1);
                        platOrder.setHasGift(1); // '是否含赠品'

                    } else {
                        platOrders.setIsGift(0);
                        platOrder.setHasGift(0); // '是否含赠品'

                    }
                    list.add(platOrders);
//			优惠信息
                    CouponDetail coupon1 = new CouponDetail();
                    coupon1.setPlatId("XMYP");// 平台id，京东JD，天猫JD
                    coupon1.setStoreId(storeId);// 店铺id
                    coupon1.setOrderId(product.getOrder_id());// 订单号
                    coupon1.setSkuId(null);
                    coupon1.setCouponCode(null);// 优惠类型编码
                    coupon1.setCouponType("XMYP店铺优惠");// 优惠名称
                    coupon1.setCouponPrice(discountMoney);//  优惠金额

                    if (coupon1.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
                        couponList.add(coupon1);
                    }
                }
                if (couponList.size() > 0) {
                    couponDetailDao.insert(couponList);
                }

                platOrder.setGoodNum(list.size()); // 商品品类数

                platOrderDao.insert(platOrder);
                platOrdersDao.insert(list);

//                 执行自动匹配
                orderServiceImpl.autoMatch(platOrder.getOrderId(), userId);

                logger.info("订单下载成功" + data.getOrder_id());
                resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, "订单下载成功" + data.getOrder_id(), null);

            } else {
                message = "订单已存在,订单号:" + (data.getOrder_id() + ",店铺编号:" + storeId);
                isSucess = false;
                logger.error("订单下载:小米有品平台订单已存在,订单号:{},店铺编号:{}", data.getOrder_id(), storeId);
                resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

            }


        }

        return resp;
    }

    /**
     * 小米有品-退货列表
     * @param orderIds  订单号
     * @param startDate 创建开始时间
     * @param endDate   创建结束时间
     * @param storeId   店铺id
     */
    @Transactional
    public String XMYPRefundPlatOrderV2(String userId, String startDate, String endDate, String storeId, String orderIds, int downloadCount) throws Exception {

        String message = "";
        boolean isSucess = true;
        String resp = "";
        String method = "aftersaleorders";

        try {
            // 设置
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            Date date = simpleDateFormat.parse(startDate);
            long start = date.getTime() / 1000;
            date = simpleDateFormat.parse(endDate);
            long end = date.getTime() / 1000;
            Map<String, Object> data = new HashMap<>();
//            data.put("ids", Arrays.asList(ids).toString());
            data.put("orderIds", Arrays.asList(orderIds).toString());
            data.put("createTimeBegin", start);
            data.put("createTimeEnd", end);

            XMYPRefundPlatOrderBean refundPlatOrderBean = XMYPapiV2(method, data, storeSettings, XMYPRefundPlatOrderBean.class);
            if (refundPlatOrderBean.getCode() != 0) {
                message = refundPlatOrderBean.getMessage();
                return resp = BaseJson.returnRespObj("ec/refundOrder/download", false, message, null);
            }
            MisUser misUser = misUserDao.select(userId);
            List<RefundOrder> refundOrderList = new ArrayList<>();
            List<RefundOrders> refundOrdersList = new ArrayList<>();
            StoreRecord storeRecord = storeRecordDao.select(storeId);
            if (refundPlatOrderBean.getData() == null) {
                message = "暂无退款单数据";
                return resp = BaseJson.returnRespObj("ec/refundOrder/download", false, message, null);
            }

            List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas> jsonArray = refundPlatOrderBean.getData().getData();
            if (refundPlatOrderBean.getData().getData() == null && refundPlatOrderBean.getData().getData().size() == 0) {
                message = "暂无退款单数据";
                return resp = BaseJson.returnRespObj("ec/refundOrder/download", false, message, null);
            }

            for(int i = 0; i < jsonArray.size(); i++) {
                com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas job = jsonArray.get(i);

                if (platOrderDao.checkExsits(job.getId(), storeId) == 0) {
                    // 原订单已删除,不存在
                    logger.info("原订单已删除,不存在：" + job.getId());
                    continue;
                }
                if (refundOrderDao.selectEcRefId(job.getId()) == null) {
                    // 判断退款单是否已存在
                    logger.info("判断退款单已存在：" + job.getId());
                    continue;
                }
                List<PlatOrder> platOrderlist = platOrderDao.selectPlatOrdersByEcOrderId(job.getOrderId());
                if (platOrderlist.size() == 0) {
                    message = "退货单对应销售单不存在,平台订单号:" + job.getOrderId();
                    continue;
                }
                PlatOrder platOrder = platOrderlist.get(0);


                RefundOrder refundOrder = new RefundOrder();
                refundOrder.setExpressCode(job.getUserExpressSn()); // 快递单号
                // 店铺设置
                String refId = getOrderNo.getSeqNo("tk", userId);
                String ecOrderId = job.getOrderId();
                refundOrder.setRefId(refId); // 退款单编号
                refundOrder.setEcId("XMYP");
                refundOrder.setOrderId(platOrder.getOrderId()); // 订单编号
                refundOrder.setStoreId(storeId); // 店铺编号
                refundOrder.setEcOrderId(job.getOrderId());// 平台订单号
                refundOrder.setStoreName(storeRecord.getStoreName()); // 店铺名称
                refundOrder.setEcRefId(job.getId()); // 电商退款单号
                refundOrder.setApplyDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(job.getCreatePerson() * 1000))); // 申请日期
//				refundOrder.setBuyerId(refund.getBuyerOpenUid()); // 买家会员号
                if (job.getType().equalsIgnoreCase("TK")) {
                    refundOrder.setIsRefund(0); // 是否退货
                } else {
                    refundOrder.setIsRefund(1); // 是否退货
                }

//				refundOrder.setAllRefNum(refund.getNum().intValue()); // 整单退货数量
//				refundOrder.setAllRefMoney(new BigDecimal(refund.getRefundFee())); // 整单退款金额
                refundOrder.setRefReason(job.getApplyReason()); // 退款原因
//				refundOrder.setRefExplain(refund.getDesc()); // 退款说明
                refundOrder.setRefStatus(1); // 退款状态
                refundOrder.setDownTime(simpleDateFormat.format(new Date())); // 下载时间
                refundOrder.setTreDate(simpleDateFormat.format(new Date())); // 处理日期
                refundOrder.setOperator(userId); // 操作员
                refundOrder.setIsAudit(0); // 是否审核
//			refundOrder.setMemo(memo); // 备注
                refundOrder.setSource(0);// 退款单来源设置0
                refundOrder.setOperatorId(misUser.getAccNum());
                refundOrder.setOperatorTime(simpleDateFormat.format(new Date()));

                // 用订单号去查询原单的购买数量以及实付金额
                List<PlatOrders> platOrders = platOrdersDao.selectByEcOrderId(ecOrderId);
                if (platOrders.size() == 0) {
                    // 日志记录
                    LogRecord logRecord = new LogRecord();
                    logRecord.setOperatId(userId);
                    if (misUser != null) {
                        logRecord.setOperatName(misUser.getUserName());
                    }
                    logRecord.setOperatContent("退款单下载，订单：" + ecOrderId + "未找到订单明细，本次下载失败。");
                    logRecord.setOperatTime(simpleDateFormat.format(new Date()));
                    logRecord.setOperatType(12);// 12退款下载
                    logRecord.setTypeName("退款单下载");
                    logRecord.setOperatOrder(ecOrderId);
                    logRecordDao.insert(logRecord);
                } else {
                    boolean flag = true;
                    for (int i1 = 0; i1 < platOrders.size(); i1++) {
                        // 循环判断原订单是否存在未匹配到存货档案的记录
                        if (!StringUtils.isNotEmpty(platOrders.get(i1).getInvId())) {
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
                        logRecord.setOperatTime(simpleDateFormat.format(new Date()));
                        logRecord.setOperatType(12);// 12退款下载
                        logRecord.setTypeName("退款单下载");
                        logRecord.setOperatOrder(ecOrderId);
                        logRecordDao.insert(logRecord);
                        continue;
                    }
                }
                List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList> refundList = job.getRefundList();
                for (com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList refund : refundList) {
                    // 销售单子表
                    List<PlatOrders> platOrdersList = platOrdersDao.selectByEcOrderAndGood(ecOrderId, refund.getPid(), null);
                    int ii = 0;
                    BigDecimal total = refund.getRefundFee().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);

                    for (PlatOrders plat : platOrdersList) {
                        ii++;
                        RefundOrders refundOrders = new RefundOrders();
                        refundOrders.setRefId(refId);
                        refundOrders.setGoodSku(null);// 商品sku
                        refundOrders.setEcGoodId(plat.getGoodId());// 设置店铺商品编码
                        InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(plat.getInvId());
                        if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
                            refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));// 设置保质期
                        }
                        refundOrders.setIsGift(plat.getIsGift());// 是否赠品用原订单属性
                        refundOrders.setPrdcDt(plat.getPrdcDt());// 设置生产日期
                        refundOrders.setInvldtnDt(plat.getInvldtnDt());// 设置失效日期
                        refundOrders.setGoodId(plat.getInvId());// 设置存货编码
                        refundOrders.setGoodName(invtyDoc.getInvtyNm());// 设置存货名称
                        refundOrders.setBatchNo(plat.getBatchNo());// 设置退货批次
                        refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());// 取对应店铺的默认退货仓

                        if (refund.getCount() == plat.getGoodNum()) {
                            refundOrders.setRefNum(plat.getInvNum()); // 退货数量
                            refundOrders.setRefMoney(total); // 退货金额

                        } else {

                            if (ii == platOrdersList.size()) {
                                Integer integers = plat.getInvNum() * plat.getGoodNum() / refund.getCount();
                                refundOrders.setRefNum(integers); // 退货数量
                                refundOrders.setRefMoney(total); // 退货金额
                            } else {
                                Integer integer = plat.getInvNum() * plat.getGoodNum() / refund.getCount();
                                refundOrders.setRefNum(integer); // 退货数量
                                refundOrders.setRefMoney(plat.getPayPrice().multiply(new BigDecimal(integer))); // 退货金额
                                total.subtract(refundOrders.getRefMoney());
                            }

                        }
                        refundOrders.setCanRefNum(plat.getCanRefNum());// 设置可退数量
                        refundOrders.setCanRefMoney(plat.getCanRefMoney());// 可退金额
//                        refundOrders.setRefMoney(platOrders.get(i1).getPayMoney());// 退货金额


                        refundOrdersList.add(refundOrders);// 装入list


                    }


                }
            }
            logger.info("下载条数：" + refundOrderList.size());

            if (refundOrderList.size() > 0) {
                refundOrderDao.insertList(refundOrderList);
                refundOrdersDao.insertList(refundOrdersList);
                downloadCount += refundOrderList.size();
            }


            // 日志记录
            LogRecord logRecord = new LogRecord();
            logRecord.setOperatId(userId);
            if (misUser != null) {
                logRecord.setOperatName(misUser.getUserName());
            }
            logRecord.setOperatContent(
                    "退款单下载，本次成功下载小米有品店铺：" + storeRecord.getStoreName() + ",退款单" + downloadCount + "条");
            logRecord.setOperatTime(simpleDateFormat.format(new Date()));
            logRecord.setOperatType(12);// 12退款下载
            logRecord.setTypeName("退款下载");
            logRecordDao.insert(logRecord);
            resp = BaseJson.returnResp("ec/refundOrder/download ", true,
                    "退款单下载，本次成功下载小米有品店铺：" + storeRecord.getStoreName() + ",退款单" + downloadCount + "条", null);


        } catch (Exception e) {
            e.printStackTrace();
            try {
                resp = BaseJson.returnResp("ec/refundOrder/download ", false, "退款单下载，下载" + downloadCount + "条后出现异常",
                        null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        return resp;
    }

    public static void main(String[] args) {
        String storeId = "4191111272312666";
//        StoreSettings asd = new StoreSettings();
//        String res = " {\"code\":0,\"message\":\"OK\",\"result\":{\"data\":[{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"252\\\",\\\"name\\\":\\\"东莞市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2340\\\",\\\"name\\\":\\\"企石镇\\\"},\\\"area\\\":{\\\"id\\\":\\\"2340001\\\",\\\"name\\\":\\\"企石镇\\\"},\\\"tel\\\":\\\"13728499709\\\",\\\"address\\\":\\\"振兴路东四街11号\\\",\\\"consignee\\\":\\\"李德茂\\\",\\\"zipcode\\\":\\\"523027\\\",\\\"address_id\\\":\\\"10160331992526546\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1573109405,\\\"add_time\\\":1459425552,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575526129,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575526138,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205958301114,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575526129,\"customized_info\":\"\",\"delivery_time\":1575530635,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575526138,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 900G 三段\",\"order_id\":4191205958301114,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":33000,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01262594795\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":103907175}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":33000},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"252\\\",\\\"name\\\":\\\"东莞市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2328\\\",\\\"name\\\":\\\"长安镇\\\"},\\\"area\\\":{\\\"id\\\":\\\"2328001\\\",\\\"name\\\":\\\"长安镇\\\"},\\\"tel\\\":\\\"13609670315\\\",\\\"address\\\":\\\"长青南路长安医院放射科\\\",\\\"consignee\\\":\\\"李树金\\\",\\\"zipcode\\\":\\\"523068\\\",\\\"address_id\\\":\\\"9952861\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1573297487,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575529040,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575529051,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205973202099,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":4,\"ctime\":1575529040,\"customized_info\":\"\",\"delivery_time\":1575615451,\"deliverycount\":4,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575529051,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 900G 三段\",\"order_id\":4191205973202099,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":66000,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01269766346\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":110482078}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":66000},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"235\\\",\\\"name\\\":\\\"深圳市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2222\\\",\\\"name\\\":\\\"龙岗区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2222002\\\",\\\"name\\\":\\\"布吉街道\\\"},\\\"tel\\\":\\\"13790877272\\\",\\\"address\\\":\\\"可园19栋A605\\\",\\\"consignee\\\":\\\"许小姐\\\",\\\"zipcode\\\":\\\"518116\\\",\\\"address_id\\\":\\\"10160502951601782\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1548765290,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575533046,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575533057,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205976803295,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575533046,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575533057,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 900G 三段\",\"order_id\":4191205976803295,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":33000,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270339001\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":963087220}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.102.0\",\"total_price\":33000},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"17\\\",\\\"name\\\":\\\"河南\\\"},\\\"city\\\":{\\\"id\\\":\\\"196\\\",\\\"name\\\":\\\"濮阳市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1896\\\",\\\"name\\\":\\\"华龙区\\\"},\\\"area\\\":{\\\"id\\\":\\\"1896006\\\",\\\"name\\\":\\\"开发区街道\\\"},\\\"tel\\\":\\\"18939337877\\\",\\\"address\\\":\\\"卫河路宁安街电信小区\\\",\\\"consignee\\\":\\\"郑号锡\\\",\\\"zipcode\\\":\\\"457001\\\",\\\"address_id\\\":\\\"11000000044987889\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1563713189,\\\"add_time\\\":1563713189,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575543257,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575543272,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205959311234,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575543257,\"customized_info\":\"\",\"delivery_time\":1575621319,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575543272,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 900G 三段\",\"order_id\":4191205959311234,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":16500,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270806577\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":1458816}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.101.0\",\"total_price\":16500},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"12\\\",\\\"name\\\":\\\"浙江\\\"},\\\"city\\\":{\\\"id\\\":\\\"122\\\",\\\"name\\\":\\\"杭州市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1263\\\",\\\"name\\\":\\\"江干区\\\"},\\\"area\\\":{\\\"id\\\":\\\"1263004\\\",\\\"name\\\":\\\"笕桥镇\\\"},\\\"tel\\\":\\\"13758271657\\\",\\\"address\\\":\\\"全福桥路439号锦润公寓7-1-403室（枸桔弄社区）\\\",\\\"consignee\\\":\\\"杨华军\\\",\\\"zipcode\\\":\\\"310000\\\",\\\"address_id\\\":\\\"10150824510933570\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1573482318,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575544385,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575544392,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":5,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"个人\",\"order_id\":4191205978513117,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575544385,\"customized_info\":\"\",\"delivery_time\":1575619581,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575544392,\"gid\":115007,\"invoice_status\":1,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 1200G 三段\",\"order_id\":4191205978513117,\"order_type\":0,\"partner_id\":2706,\"pid\":88281,\"price\":17200,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270402766\",\"product_price\":23200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":17200,\"status\":6,\"uid\":36825362}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":17200},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"244\\\",\\\"name\\\":\\\"梅州市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2284\\\",\\\"name\\\":\\\"五华县\\\"},\\\"area\\\":{\\\"id\\\":\\\"2284010\\\",\\\"name\\\":\\\"岐岭镇\\\"},\\\"tel\\\":\\\"13068749763\\\",\\\"address\\\":\\\"双头卫生院\\\",\\\"consignee\\\":\\\"钟苑涛\\\",\\\"zipcode\\\":\\\"514400\\\",\\\"address_id\\\":\\\"11000000105014999\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1575552311,\\\"add_time\\\":1575552311,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575553253,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575553270,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":5,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"13068749763\",\"invoice_title\":\"个人\",\"order_id\":4191205976405008,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":3,\"ctime\":1575553253,\"customized_info\":\"\",\"delivery_time\":1575619590,\"deliverycount\":3,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575553270,\"gid\":115007,\"invoice_status\":1,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 1200G 三段\",\"order_id\":4191205976405008,\"order_type\":0,\"partner_id\":2706,\"pid\":88281,\"price\":56390,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270376413\",\"product_price\":23200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":18800,\"status\":6,\"uid\":1406912475}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":56390},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"252\\\",\\\"name\\\":\\\"东莞市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2344\\\",\\\"name\\\":\\\"常平镇\\\"},\\\"area\\\":{\\\"id\\\":\\\"2344001\\\",\\\"name\\\":\\\"常平镇\\\"},\\\"tel\\\":\\\"13686092299\\\",\\\"address\\\":\\\"花和小城翠竹苑a座3a\\\",\\\"consignee\\\":\\\"陈志荣\\\",\\\"zipcode\\\":\\\"523036\\\",\\\"address_id\\\":\\\"10160323609030723\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1566625329,\\\"add_time\\\":1458717236,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575556938,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575556948,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205962005983,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575556938,\"customized_info\":\"\",\"delivery_time\":1575619590,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575556948,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 1200G 三段\",\"order_id\":4191205962005983,\"order_type\":0,\"partner_id\":2706,\"pid\":88281,\"price\":37600,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270323188\",\"product_price\":23200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":18800,\"status\":6,\"uid\":956499222}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":37600},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"18\\\",\\\"name\\\":\\\"湖北\\\"},\\\"city\\\":{\\\"id\\\":\\\"205\\\",\\\"name\\\":\\\"武汉市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1977\\\",\\\"name\\\":\\\"洪山区\\\"},\\\"area\\\":{\\\"id\\\":\\\"1977014\\\",\\\"name\\\":\\\"狮子山街道\\\"},\\\"tel\\\":\\\"15971482752\\\",\\\"address\\\":\\\"华中农业大学西苑小区40栋149门301\\\",\\\"consignee\\\":\\\"王芳\\\",\\\"zipcode\\\":\\\"430070\\\",\\\"address_id\\\":\\\"11000000096383673\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1573804539,\\\"add_time\\\":1573804539,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575537931,\"delivery_time\":0,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575537938,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205956810173,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575537931,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575537938,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 900G 四段\",\"order_id\":4191205956810173,\"order_type\":0,\"partner_id\":2706,\"pid\":88282,\"price\":32400,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270402748\",\"product_price\":19800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16200,\"status\":6,\"uid\":271883305}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.0.0\",\"total_price\":32400},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"18\\\",\\\"name\\\":\\\"湖北\\\"},\\\"city\\\":{\\\"id\\\":\\\"209\\\",\\\"name\\\":\\\"襄阳市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2013\\\",\\\"name\\\":\\\"樊城区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2013020\\\",\\\"name\\\":\\\"中原街道\\\"},\\\"tel\\\":\\\"13871719340\\\",\\\"address\\\":\\\"中原路中原市场布林轩\\\",\\\"consignee\\\":\\\"施辉\\\",\\\"zipcode\\\":\\\"441001\\\",\\\"address_id\\\":\\\"11000000101181303\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1574649594,\\\"add_time\\\":1574649594,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575531173,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575531180,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205974802821,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575531173,\"customized_info\":\"\",\"delivery_time\":1575621320,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575531180,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 1200G 四段\",\"order_id\":4191205974802821,\"order_type\":0,\"partner_id\":2706,\"pid\":88283,\"price\":16500,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270778100\",\"product_price\":20200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":2164327825}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.102.0\",\"total_price\":16500},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"22\\\",\\\"name\\\":\\\"海南\\\"},\\\"city\\\":{\\\"id\\\":\\\"268\\\",\\\"name\\\":\\\"海口市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2471\\\",\\\"name\\\":\\\"龙华区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2471002\\\",\\\"name\\\":\\\"城西镇\\\"},\\\"tel\\\":\\\"13379892816\\\",\\\"address\\\":\\\"山高村消防总队后门岗3栋802室\\\",\\\"consignee\\\":\\\"邢敏\\\",\\\"zipcode\\\":\\\"570105\\\",\\\"address_id\\\":\\\"10180507937608863\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1546318934,\\\"add_time\\\":1525686296,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575529538,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575562202,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205965501701,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575529538,\"customized_info\":\"\",\"delivery_time\":1575615451,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575562202,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装 900g*6罐 二段\",\"order_id\":4191205965501701,\"order_type\":0,\"partner_id\":2706,\"pid\":88446,\"price\":110800,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01269755761\",\"product_price\":160800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":160800,\"status\":6,\"uid\":18027061}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":110800},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"235\\\",\\\"name\\\":\\\"深圳市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2220\\\",\\\"name\\\":\\\"南山区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2220008\\\",\\\"name\\\":\\\"招商街道\\\"},\\\"tel\\\":\\\"18688780656\\\",\\\"address\\\":\\\"港城路前海湾花园7-313\\\",\\\"consignee\\\":\\\"桔子姐姐\\\",\\\"zipcode\\\":\\\"518052\\\",\\\"address_id\\\":\\\"10180516396304376\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1526440163,\\\"add_time\\\":1526440163,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575560595,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575561719,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205982313053,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575560595,\"customized_info\":\"\",\"delivery_time\":1575619581,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575561719,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装 900g*6罐 三段\",\"order_id\":4191205982313053,\"order_type\":0,\"partner_id\":2706,\"pid\":88448,\"price\":97800,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270319408\",\"product_price\":136800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":136800,\"status\":6,\"uid\":1137778750}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":97800},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"13\\\",\\\"name\\\":\\\"安徽\\\"},\\\"city\\\":{\\\"id\\\":\\\"148\\\",\\\"name\\\":\\\"池州市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1452\\\",\\\"name\\\":\\\"东至县\\\"},\\\"area\\\":{\\\"id\\\":\\\"1452001\\\",\\\"name\\\":\\\"大渡口镇\\\"},\\\"tel\\\":\\\"18056620254\\\",\\\"address\\\":\\\"大渡口幼儿园\\\",\\\"consignee\\\":\\\"欧文莲\\\",\\\"zipcode\\\":\\\"247200\\\",\\\"address_id\\\":\\\"11000000104921531\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1575430218,\\\"add_time\\\":1575430218,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575430233,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575606553,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191204929400236,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575430233,\"customized_info\":\"\",\"delivery_time\":1575705868,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575606553,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装 900g*6罐 三段\",\"order_id\":4191204929400236,\"order_type\":0,\"partner_id\":2706,\"pid\":88448,\"price\":195400,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01277668642\",\"product_price\":136800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":136800,\"status\":6,\"uid\":47156356}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":195400},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"广东\\\"},\\\"city\\\":{\\\"id\\\":\\\"235\\\",\\\"name\\\":\\\"深圳市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2220\\\",\\\"name\\\":\\\"南山区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2220008\\\",\\\"name\\\":\\\"招商街道\\\"},\\\"tel\\\":\\\"18688780656\\\",\\\"address\\\":\\\"港城路前海湾花园7-313\\\",\\\"consignee\\\":\\\"桔子姐姐\\\",\\\"zipcode\\\":\\\"518052\\\",\\\"address_id\\\":\\\"10180516396304376\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1526440163,\\\"add_time\\\":1526440163,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575560639,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575561657,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205974007112,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575560639,\"customized_info\":\"\",\"delivery_time\":1575615451,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575561657,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装 900g*4罐 四段\",\"order_id\":4191205974007112,\"order_type\":0,\"partner_id\":2706,\"pid\":88450,\"price\":63200,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01269763951\",\"product_price\":79200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":79200,\"status\":6,\"uid\":1137778750}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":63200},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"16\\\",\\\"name\\\":\\\"山东\\\"},\\\"city\\\":{\\\"id\\\":\\\"175\\\",\\\"name\\\":\\\"烟台市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1700\\\",\\\"name\\\":\\\"芝罘区\\\"},\\\"area\\\":{\\\"id\\\":\\\"1700008\\\",\\\"name\\\":\\\"向阳街道\\\"},\\\"tel\\\":\\\"18953558180\\\",\\\"address\\\":\\\"环山路静海山庄57号11楼东\\\",\\\"consignee\\\":\\\"张戈\\\",\\\"zipcode\\\":\\\"264001\\\",\\\"address_id\\\":\\\"10180417140706195\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1535609962,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575522106,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575587405,\"goods_name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205959404141,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575522106,\"customized_info\":\"\",\"delivery_time\":1575621319,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575587405,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿（Friso) 婴幼儿奶粉 金装系列罐装组合装 900g*4罐 四段\",\"order_id\":4191205959404141,\"order_type\":0,\"partner_id\":2706,\"pid\":88450,\"price\":63200,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270817314\",\"product_price\":79200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":79200,\"status\":6,\"uid\":1304077150}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":63200},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"23\\\",\\\"name\\\":\\\"重庆\\\"},\\\"city\\\":{\\\"id\\\":\\\"271\\\",\\\"name\\\":\\\"重庆市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2506\\\",\\\"name\\\":\\\"九龙坡区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2506004\\\",\\\"name\\\":\\\"二郎街道\\\"},\\\"tel\\\":\\\"15178793885\\\",\\\"address\\\":\\\"巴国丽景3栋\\\",\\\"consignee\\\":\\\"刘瑞\\\",\\\"zipcode\\\":\\\"400050\\\",\\\"address_id\\\":\\\"11000000105036429\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1575589068,\\\"add_time\\\":1575589068,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575589223,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575589236,\"goods_name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191206987936285,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575589223,\"customized_info\":\"\",\"delivery_time\":1575619590,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575589236,\"gid\":115022,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉 1罐 400G\",\"order_id\":4191206987936285,\"order_type\":0,\"partner_id\":2706,\"pid\":88451,\"price\":6900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270384683\",\"product_price\":13800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":6900,\"status\":6,\"uid\":1259504412}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.101.0\",\"total_price\":6900},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"16\\\",\\\"name\\\":\\\"山东\\\"},\\\"city\\\":{\\\"id\\\":\\\"171\\\",\\\"name\\\":\\\"青岛市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1669\\\",\\\"name\\\":\\\"市北区\\\"},\\\"area\\\":{\\\"id\\\":\\\"1669022\\\",\\\"name\\\":\\\"水清沟街道\\\"},\\\"tel\\\":\\\"18561858571\\\",\\\"address\\\":\\\"四流南路127号青岛市中心医院\\\",\\\"consignee\\\":\\\"于杰\\\",\\\"zipcode\\\":\\\"266011\\\",\\\"address_id\\\":\\\"10170407589306286\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1540526784,\\\"add_time\\\":1491568705,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575555804,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575555931,\"goods_name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205963204896,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575555804,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575555931,\"gid\":115022,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉 1罐 900G\",\"order_id\":4191205963204896,\"order_type\":0,\"partner_id\":2706,\"pid\":88452,\"price\":25900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270402730\",\"product_price\":28800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":25900,\"status\":6,\"uid\":23033163}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":25900},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"16\\\",\\\"name\\\":\\\"山东\\\"},\\\"city\\\":{\\\"id\\\":\\\"171\\\",\\\"name\\\":\\\"青岛市\\\"},\\\"district\\\":{\\\"id\\\":\\\"1673\\\",\\\"name\\\":\\\"李沧区\\\"},\\\"area\\\":{\\\"id\\\":\\\"1673001\\\",\\\"name\\\":\\\"浮山路街道\\\"},\\\"tel\\\":\\\"18561858571\\\",\\\"address\\\":\\\"青山路福临万家一期A区11号楼1单元204\\\",\\\"consignee\\\":\\\"于杰\\\",\\\"zipcode\\\":\\\"266021\\\",\\\"address_id\\\":\\\"10170411702201084\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1538527419,\\\"add_time\\\":1491864222,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575556094,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575556100,\"goods_name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205965604076,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575556094,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575556100,\"gid\":115022,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉 1罐 900G\",\"order_id\":4191205965604076,\"order_type\":0,\"partner_id\":2706,\"pid\":88452,\"price\":25900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270330082\",\"product_price\":28800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":25900,\"status\":6,\"uid\":23033163}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":25900},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"中国大陆\\\"},\\\"province\\\":{\\\"id\\\":\\\"19\\\",\\\"name\\\":\\\"湖南\\\"},\\\"city\\\":{\\\"id\\\":\\\"229\\\",\\\"name\\\":\\\"永州市\\\"},\\\"district\\\":{\\\"id\\\":\\\"2170\\\",\\\"name\\\":\\\"零陵区\\\"},\\\"area\\\":{\\\"id\\\":\\\"2170001\\\",\\\"name\\\":\\\"朝阳街道\\\"},\\\"tel\\\":\\\"13874699070\\\",\\\"address\\\":\\\"潇水西路35号新河西市场\\\",\\\"consignee\\\":\\\"蒋淑凤\\\",\\\"zipcode\\\":\\\"425000\\\",\\\"address_id\\\":\\\"21535418\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1564654014,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575559605,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575559614,\"goods_name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉\",\"goods_price\":13800,\"invoice\":5,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"个人\",\"order_id\":4191205957611854,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575559605,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575559614,\"gid\":115022,\"invoice_status\":1,\"invoice_url\":\"\",\"name\":\"美素佳儿妈妈 （Frisomum） 孕产妇配方奶粉调制乳粉 1罐 900G\",\"order_id\":4191205957611854,\"order_type\":0,\"partner_id\":2706,\"pid\":88452,\"price\":23900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270323161\",\"product_price\":28800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":25900,\"status\":6,\"uid\":85961556}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":23900}],\"total\":19}}\n";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            JsonOrderBean propLists = mapper.readValue(res, JsonOrderBean.class);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            String method = "setdelivery";

            Map<String, String> map = new HashMap<String, String>();

            map.put("express_sn", "123");// 快递单号
            map.put("express_name", "中国邮政EMS");// 快递公司名称
            map.put("bizcode", "ems");// 快递公司编号
            map.put("userId", "1");// 可以登录商户后台的米聊账号
            map.put("descr", "");// 商品型号ID
            map.put("pid", "0");// 商品型号ID
            map.put("donkey_id", "0"); // 非小驴发货填0
            map.put("order_id", "123456");// 订单ID

//            XMYPapi(method,map,new StoreSettings(),String.class);
//            getXMYPSku("", "", asd);
//            XMYPDownloadByOrderId("accNum", "orderId", "startDate", "endDate",storeId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static <T> T XMYPapi(String method, Map data, StoreSettings storeSettings, Class<T> objClass) throws IOException {

        String venderId = storeSettings.getVenderId();
        String accessToken = storeSettings.getAccessToken();
        String appKey = storeSettings.getAppKey();
//        String partnerId = "2706";
//        String aesKey = "50449ca64308d8f9fa29220ec319b7e7";
//        String key = "1599bcdc0815f4eb23d4a518630b24f2";

        OpenApiUtils openapiUtils = new OpenApiUtils();

        PartnerInfo partnerInfo = new PartnerInfo(venderId, null, accessToken, appKey);
//		PartnerInfo partnerInfo = new PartnerInfo(storeSettings.getVenderId(), null, storeSettings.getAppKey(), storeSettings.getAppSecret());
        openapiUtils.SetShop(partnerInfo);

        String res = openapiUtils.api(method, data, "online");
        logger.info(res);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T propLists = mapper.readValue(res, objClass);
        return propLists;
    }

    public static <T> T XMYPapiV2(String method, Map data, StoreSettings storeSettings, Class<T> objClass) throws IOException {

        String venderId = storeSettings.getVenderId();
        String accessToken = storeSettings.getAccessToken();
        String appKey = storeSettings.getAppKey();
//        String partnerId = "2706";
//        String aesKey = "50449ca64308d8f9fa29220ec319b7e7";
//        String key = "1599bcdc0815f4eb23d4a518630b24f2";

        OpenApiUtils openapiUtils = new OpenApiUtils();

        PartnerInfo partnerInfo = new PartnerInfo(venderId, null, accessToken, appKey);
//		PartnerInfo partnerInfo = new PartnerInfo(storeSettings.getVenderId(), null, storeSettings.getAppKey(), storeSettings.getAppSecret());
        openapiUtils.SetShop(partnerInfo);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String datas = mapper.writeValueAsString(data);
        String res = openapiUtils.apiV2(method, datas, "online");
        logger.info(res);
        T propLists = mapper.readValue(res, objClass);
        return propLists;
    }

    /**
     * 商品库下载-小米有品
     *
     * @return
     */
    public List<GoodRecord> getXMYPGoodSku(String accNum, String date, StoreSettings storeSettings, List<GoodRecord> gRecordList) throws Exception {

//        String storeId = storeSettings.getStoreId();
//        String appSecret = storeSettings.getAppSecret();
//        String appKey = storeSettings.getAppKey();
//
//        String partnerId = "2706";
//        String aesKey = "50449ca64308d8f9fa29220ec319b7e7";
//        String key = "1599bcdc0815f4eb23d4a518630b24f2";
//
//        OpenApiUtils openapiUtils = new OpenApiUtils();
//
//		PartnerInfo partnerInfo = new PartnerInfo(partnerId, null,key , aesKey);
////		PartnerInfo partnerInfo = new PartnerInfo(storeSettings.getVenderId(), null, storeSettings.getAppKey(), storeSettings.getAppSecret());
//        openapiUtils.SetShop(partnerInfo);
//
//	        商品详情
        Map<String, String> data = new HashMap<>();
        data.put("merchant_id", storeSettings.getVenderId());
        data.put("status", "1");

//        String res = openapiUtils.api("proplist", data, "online");
        String method = "proplist";
        propList propLists = XMYPapi(method, data, storeSettings, propList.class);
//        logger.info(res);
//
//        ObjectMapper mapper = new ObjectMapper();
//        propList propLists = mapper.readValue(res, );
        if (propLists.getCode() != 200) {
            logger.error("下载错误：" + propLists.getMsg());
        }
        //Json映射为对象
        List<propList.Data> datas = propLists.getData();

        for (propList.Data zdata : datas) {
            List<propList.Data.Sku_ids> indexs = zdata.getSku_ids();
            for (propList.Data.Sku_ids sku : indexs) {
                GoodRecord gRecord = new GoodRecord();
                //商品名称
                gRecord.setStoreId(storeSettings.getStoreId());// 店铺编号
                gRecord.setEcId("XMYP");// 电商平台编号
                gRecord.setEcName("小米有品");// 电商平台名称
                gRecord.setGoodSku(zdata.getGid().toString());// 商品sku
                gRecord.setEcGoodId(zdata.getPid().toString());// 平台商品编号
                gRecord.setEcGoodName(sku.getName());// 平台商品名称
                BigDecimal priceb = new BigDecimal(zdata.getPrice());
                BigDecimal num = new BigDecimal(100);
                BigDecimal Pricey = priceb.divide(num, 8, BigDecimal.ROUND_HALF_DOWN);
                gRecord.setUpsetPrice(Pricey);// 最低售价
                gRecord.setSafeInv("0");// 安全库存
                gRecord.setSkuProp("");// sku属性
                gRecord.setOnlineStatus("在售");// 线上状态
                gRecord.setOperator(accNum);//操作人
                gRecord.setOperatTime(date);//操作时间
                gRecord.setIsSecSale(0);//是否二销
                gRecord.setMemo(""); // 备注
                Integer id1 = goodRecordDao.selectByEcGoodIdAndSku(gRecord);
                gRecord.setId(id1);
                gRecordList.add(gRecord);
            }
        }
        return gRecordList;
    }


}


class propList {

    private Integer code;
    private List<Data> data;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    static class Data {
        public Data() {
        }

        private Integer pid;
        private Integer gid;
        private Integer consignor;
        private String prop_name;
        private Integer map_id;
        private Integer merchant_id;
        private String merchant_name;
        private Integer price;
        private String add_time;
        private Integer goods_type;
        private List<Sku_ids> sku_ids;

        public Integer getPid() {
            return pid;
        }

        public void setPid(Integer pid) {
            this.pid = pid;
        }

        public Integer getGid() {
            return gid;
        }

        public void setGid(Integer gid) {
            this.gid = gid;
        }

        public Integer getConsignor() {
            return consignor;
        }

        public void setConsignor(Integer consignor) {
            this.consignor = consignor;
        }

        public String getProp_name() {
            return prop_name;
        }

        public void setProp_name(String prop_name) {
            this.prop_name = prop_name;
        }

        public Integer getMap_id() {
            return map_id;
        }

        public void setMap_id(Integer map_id) {
            this.map_id = map_id;
        }

        public Integer getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(Integer merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public Integer getGoods_type() {
            return goods_type;
        }

        public void setGoods_type(Integer goods_type) {
            this.goods_type = goods_type;
        }

        public List<Sku_ids> getSku_ids() {
            return sku_ids;
        }

        public void setSku_ids(List<Sku_ids> sku_ids) {
            this.sku_ids = sku_ids;
        }

        static class Sku_ids {
            public Sku_ids() {
            }

            private Integer sku_id;
            private Integer count;
            private String code69;
            private Integer price;
            private String name;

            public Integer getSku_id() {
                return sku_id;
            }

            public void setSku_id(Integer sku_id) {
                this.sku_id = sku_id;
            }

            public Integer getCount() {
                return count;
            }

            public void setCount(Integer count) {
                this.count = count;
            }

            public String getCode69() {
                return code69;
            }

            public void setCode69(String code69) {
                this.code69 = code69;
            }

            public Integer getPrice() {
                return price;
            }

            public void setPrice(Integer price) {
                this.price = price;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

    }
}


class JsonOrderBean {

    private int code;
    private String message;
    private Result result;

    static class Result {
        public Result() {
        }

        private List<Data> data;

        static class Data {
            public Data() {
            }


            private int alldelivery;
            private String address;
            private Consignee_id_card consignee_id_card;

            static class Consignee_id_card {
                public Consignee_id_card() {
                }

                private String card_id;
                private String card_name;

                public void setCard_id(String card_id) {
                    this.card_id = card_id;
                }

                public String getCard_id() {
                    return card_id;
                }

                public void setCard_name(String card_name) {
                    this.card_name = card_name;
                }

                public String getCard_name() {
                    return card_name;
                }

            }

            private Payment_info payment_info;

            static class Payment_info {
                public Payment_info() {
                }

                private List<String> importation_type;

                public void setImportation_type(List<String> importation_type) {
                    this.importation_type = importation_type;
                }

                public List<String> getImportation_type() {
                    return importation_type;
                }

            }

            private BigDecimal coupon_amount;
            private long ctime;
            private int consignor;
            private int delivery_time;
            private String description;
            private long ftime;
            private String goods_name;
            private String goods_price;
            private String invoice;
            private String invoice_title;
            private String invoice_company_code;
            private String invoice_phone;
            private String invoice_email;
            private String order_id;
            private String partner_id;
            private String otype;
            private List<Products> products;

            static class Products {
                public Products() {
                }

                private String customized_info;
                private int count;
                private String ctime;
                private String ftime;
                private String invoice_status;
                private String invoice_url;
                private String delivery_time;
                private String deliverycount;
                private String gid;
                private String name;
                private String order_id;
                private String partner_id;
                private String order_type;
                private String pid;
                private BigDecimal price;
                private String product_bizcode;
                private String product_express_sn;
                private BigDecimal product_price;
                private BigDecimal sale_price;
                private String refund_id;
                private String refunddescription;
                private String refundprice;
                private String refundstatus;
                private String refundtype;
                private String rtime;
                private String rctime;
                private String status;
                private String uid;
                private String express_sn;
                private String bizcode;

                public void setCustomized_info(String customized_info) {
                    this.customized_info = customized_info;
                }

                public String getCustomized_info() {
                    return customized_info;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getCount() {
                    return count;
                }

                public void setCtime(String ctime) {
                    this.ctime = ctime;
                }

                public String getCtime() {
                    return ctime;
                }

                public void setFtime(String ftime) {
                    this.ftime = ftime;
                }

                public String getFtime() {
                    return ftime;
                }

                public void setInvoice_status(String invoice_status) {
                    this.invoice_status = invoice_status;
                }

                public String getInvoice_status() {
                    return invoice_status;
                }

                public void setInvoice_url(String invoice_url) {
                    this.invoice_url = invoice_url;
                }

                public String getInvoice_url() {
                    return invoice_url;
                }

                public void setDelivery_time(String delivery_time) {
                    this.delivery_time = delivery_time;
                }

                public String getDelivery_time() {
                    return delivery_time;
                }

                public void setDeliverycount(String deliverycount) {
                    this.deliverycount = deliverycount;
                }

                public String getDeliverycount() {
                    return deliverycount;
                }

                public void setGid(String gid) {
                    this.gid = gid;
                }

                public String getGid() {
                    return gid;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getName() {
                    return name;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public void setPartner_id(String partner_id) {
                    this.partner_id = partner_id;
                }

                public String getPartner_id() {
                    return partner_id;
                }

                public void setOrder_type(String order_type) {
                    this.order_type = order_type;
                }

                public String getOrder_type() {
                    return order_type;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getPid() {
                    return pid;
                }

                public void setPrice(BigDecimal price) {
                    this.price = price;
                }

                public BigDecimal getPrice() {
                    return price;
                }

                public void setProduct_bizcode(String product_bizcode) {
                    this.product_bizcode = product_bizcode;
                }

                public String getProduct_bizcode() {
                    return product_bizcode;
                }

                public void setProduct_express_sn(String product_express_sn) {
                    this.product_express_sn = product_express_sn;
                }

                public String getProduct_express_sn() {
                    return product_express_sn;
                }

                public void setProduct_price(BigDecimal product_price) {
                    this.product_price = product_price;
                }

                public BigDecimal getProduct_price() {
                    return product_price;
                }

                public void setSale_price(BigDecimal sale_price) {
                    this.sale_price = sale_price;
                }

                public BigDecimal getSale_price() {
                    return sale_price;
                }

                public void setRefund_id(String refund_id) {
                    this.refund_id = refund_id;
                }

                public String getRefund_id() {
                    return refund_id;
                }

                public void setRefunddescription(String refunddescription) {
                    this.refunddescription = refunddescription;
                }

                public String getRefunddescription() {
                    return refunddescription;
                }

                public void setRefundprice(String refundprice) {
                    this.refundprice = refundprice;
                }

                public String getRefundprice() {
                    return refundprice;
                }

                public void setRefundstatus(String refundstatus) {
                    this.refundstatus = refundstatus;
                }

                public String getRefundstatus() {
                    return refundstatus;
                }

                public void setRefundtype(String refundtype) {
                    this.refundtype = refundtype;
                }

                public String getRefundtype() {
                    return refundtype;
                }

                public void setRtime(String rtime) {
                    this.rtime = rtime;
                }

                public String getRtime() {
                    return rtime;
                }

                public void setRctime(String rctime) {
                    this.rctime = rctime;
                }

                public String getRctime() {
                    return rctime;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getStatus() {
                    return status;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getUid() {
                    return uid;
                }

                public void setExpress_sn(String express_sn) {
                    this.express_sn = express_sn;
                }

                public String getExpress_sn() {
                    return express_sn;
                }

                public void setBizcode(String bizcode) {
                    this.bizcode = bizcode;
                }

                public String getBizcode() {
                    return bizcode;
                }

            }

            private List<AftersaleOrders> aftersaleOrders;

            static class AftersaleOrders {
                public AftersaleOrders() {
                }

                private String id;
                private String refund_type;
                private String order_id;
                private String pid;
                private String count;
                private String state;
                private String op_code;
                private String target_state;
                private String op_time;
                private String self_supporting;
                private String type;
                private String service_way;
                private String org_id;
                private String org_name;
                private String apply_reason;
                private String arbitrable;
                private String evaluable;
                private String create_time;
                private String create_person;
                private String update_time;
                private String update_person;
                private String create_from;
                private String user_express_sn;
                private String user_express_name;
                private List<RefundList> refundList;

                static class RefundList {
                    public RefundList() {
                    }

                    private String refund_fee;
                    private String refund_status;
                    private String notify_time;
                    private String state;
                    private String pid;
                    private String count;
                    private String pay_type;

                    public void setRefund_fee(String refund_fee) {
                        this.refund_fee = refund_fee;
                    }

                    public String getRefund_fee() {
                        return refund_fee;
                    }

                    public void setRefund_status(String refund_status) {
                        this.refund_status = refund_status;
                    }

                    public String getRefund_status() {
                        return refund_status;
                    }

                    public void setNotify_time(String notify_time) {
                        this.notify_time = notify_time;
                    }

                    public String getNotify_time() {
                        return notify_time;
                    }

                    public void setState(String state) {
                        this.state = state;
                    }

                    public String getState() {
                        return state;
                    }

                    public void setPid(String pid) {
                        this.pid = pid;
                    }

                    public String getPid() {
                        return pid;
                    }

                    public void setCount(String count) {
                        this.count = count;
                    }

                    public String getCount() {
                        return count;
                    }

                    public void setPay_type(String pay_type) {
                        this.pay_type = pay_type;
                    }

                    public String getPay_type() {
                        return pay_type;
                    }

                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getId() {
                    return id;
                }

                public void setRefund_type(String refund_type) {
                    this.refund_type = refund_type;
                }

                public String getRefund_type() {
                    return refund_type;
                }

                public void setOrder_id(String order_id) {
                    this.order_id = order_id;
                }

                public String getOrder_id() {
                    return order_id;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getPid() {
                    return pid;
                }

                public void setCount(String count) {
                    this.count = count;
                }

                public String getCount() {
                    return count;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getState() {
                    return state;
                }

                public void setOp_code(String op_code) {
                    this.op_code = op_code;
                }

                public String getOp_code() {
                    return op_code;
                }

                public void setTarget_state(String target_state) {
                    this.target_state = target_state;
                }

                public String getTarget_state() {
                    return target_state;
                }

                public void setOp_time(String op_time) {
                    this.op_time = op_time;
                }

                public String getOp_time() {
                    return op_time;
                }

                public void setSelf_supporting(String self_supporting) {
                    this.self_supporting = self_supporting;
                }

                public String getSelf_supporting() {
                    return self_supporting;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getType() {
                    return type;
                }

                public void setService_way(String service_way) {
                    this.service_way = service_way;
                }

                public String getService_way() {
                    return service_way;
                }

                public void setOrg_id(String org_id) {
                    this.org_id = org_id;
                }

                public String getOrg_id() {
                    return org_id;
                }

                public void setOrg_name(String org_name) {
                    this.org_name = org_name;
                }

                public String getOrg_name() {
                    return org_name;
                }

                public void setApply_reason(String apply_reason) {
                    this.apply_reason = apply_reason;
                }

                public String getApply_reason() {
                    return apply_reason;
                }

                public void setArbitrable(String arbitrable) {
                    this.arbitrable = arbitrable;
                }

                public String getArbitrable() {
                    return arbitrable;
                }

                public void setEvaluable(String evaluable) {
                    this.evaluable = evaluable;
                }

                public String getEvaluable() {
                    return evaluable;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public void setCreate_person(String create_person) {
                    this.create_person = create_person;
                }

                public String getCreate_person() {
                    return create_person;
                }

                public void setUpdate_time(String update_time) {
                    this.update_time = update_time;
                }

                public String getUpdate_time() {
                    return update_time;
                }

                public void setUpdate_person(String update_person) {
                    this.update_person = update_person;
                }

                public String getUpdate_person() {
                    return update_person;
                }

                public void setCreate_from(String create_from) {
                    this.create_from = create_from;
                }

                public String getCreate_from() {
                    return create_from;
                }

                public void setUser_express_sn(String user_express_sn) {
                    this.user_express_sn = user_express_sn;
                }

                public String getUser_express_sn() {
                    return user_express_sn;
                }

                public void setUser_express_name(String user_express_name) {
                    this.user_express_name = user_express_name;
                }

                public String getUser_express_name() {
                    return user_express_name;
                }

                public void setRefundList(List<RefundList> refundList) {
                    this.refundList = refundList;
                }

                public List<RefundList> getRefundList() {
                    return refundList;
                }

            }

            private BigDecimal ship_fee;
            private String shopbrand;
            private BigDecimal total_price;

            public void setAlldelivery(int alldelivery) {
                this.alldelivery = alldelivery;
            }

            public int getAlldelivery() {
                return alldelivery;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAddress() {
                return address;
            }

            public void setConsignee_id_card(Consignee_id_card consignee_id_card) {
                this.consignee_id_card = consignee_id_card;
            }

            public Consignee_id_card getConsignee_id_card() {
                return consignee_id_card;
            }

            public void setPayment_info(Payment_info payment_info) {
                this.payment_info = payment_info;
            }

            public Payment_info getPayment_info() {
                return payment_info;
            }

            public void setCoupon_amount(BigDecimal coupon_amount) {
                this.coupon_amount = coupon_amount;
            }

            public BigDecimal getCoupon_amount() {
                return coupon_amount;
            }

            public void setCtime(long ctime) {
                this.ctime = ctime;
            }

            public long getCtime() {
                return ctime;
            }

            public void setConsignor(int consignor) {
                this.consignor = consignor;
            }

            public int getConsignor() {
                return consignor;
            }

            public void setDelivery_time(int delivery_time) {
                this.delivery_time = delivery_time;
            }

            public int getDelivery_time() {
                return delivery_time;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }

            public void setFtime(long ftime) {
                this.ftime = ftime;
            }

            public long getFtime() {
                return ftime;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_price(String goods_price) {
                this.goods_price = goods_price;
            }

            public String getGoods_price() {
                return goods_price;
            }

            public void setInvoice(String invoice) {
                this.invoice = invoice;
            }

            public String getInvoice() {
                return invoice;
            }

            public void setInvoice_title(String invoice_title) {
                this.invoice_title = invoice_title;
            }

            public String getInvoice_title() {
                return invoice_title;
            }

            public void setInvoice_company_code(String invoice_company_code) {
                this.invoice_company_code = invoice_company_code;
            }

            public String getInvoice_company_code() {
                return invoice_company_code;
            }

            public void setInvoice_phone(String invoice_phone) {
                this.invoice_phone = invoice_phone;
            }

            public String getInvoice_phone() {
                return invoice_phone;
            }

            public void setInvoice_email(String invoice_email) {
                this.invoice_email = invoice_email;
            }

            public String getInvoice_email() {
                return invoice_email;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setPartner_id(String partner_id) {
                this.partner_id = partner_id;
            }

            public String getPartner_id() {
                return partner_id;
            }

            public void setOtype(String otype) {
                this.otype = otype;
            }

            public String getOtype() {
                return otype;
            }

            public void setProducts(List<Products> products) {
                this.products = products;
            }

            public List<Products> getProducts() {
                return products;
            }

            public void setAftersaleOrders(List<AftersaleOrders> aftersaleOrders) {
                this.aftersaleOrders = aftersaleOrders;
            }

            public List<AftersaleOrders> getAftersaleOrders() {
                return aftersaleOrders;
            }

            public void setShip_fee(BigDecimal ship_fee) {
                this.ship_fee = ship_fee;
            }

            public BigDecimal getShip_fee() {
                return ship_fee;
            }

            public void setShopbrand(String shopbrand) {
                this.shopbrand = shopbrand;
            }

            public String getShopbrand() {
                return shopbrand;
            }

            public void setTotal_price(BigDecimal total_price) {
                this.total_price = total_price;
            }

            public BigDecimal getTotal_price() {
                return total_price;
            }

        }


        private int total;

        public void setData(List<Data> data) {
            this.data = data;
        }

        public List<Data> getData() {
            return data;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

}

class address {

    private Address country;
    private Address province;
    private Address city;
    private Address district;
    private Address area;
    private String tel;
    private String address;
    private String consignee;
    private String zipcode;
    private String address_id;
    private String tag_name;
    private List<String> matching;
    private String type;
    private int need_edit;
    private int is_invalid;
    private int is_default;
    private int award_default;
    private long update_time;
    private int add_time;
    private String address_name;

    public void setCountry(Address country) {
        this.country = country;
    }

    public Address getCountry() {
        return country;
    }

    public void setProvince(Address province) {
        this.province = province;
    }

    public Address getProvince() {
        return province;
    }

    public void setCity(Address city) {
        this.city = city;
    }

    public Address getCity() {
        return city;
    }

    public void setDistrict(Address district) {
        this.district = district;
    }

    public Address getDistrict() {
        return district;
    }

    public void setArea(Address area) {
        this.area = area;
    }

    public Address getArea() {
        return area;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setMatching(List<String> matching) {
        this.matching = matching;
    }

    public List<String> getMatching() {
        return matching;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setNeed_edit(int need_edit) {
        this.need_edit = need_edit;
    }

    public int getNeed_edit() {
        return need_edit;
    }

    public void setIs_invalid(int is_invalid) {
        this.is_invalid = is_invalid;
    }

    public int getIs_invalid() {
        return is_invalid;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setAward_default(int award_default) {
        this.award_default = award_default;
    }

    public int getAward_default() {
        return award_default;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_name() {
        return address_name;
    }


    static class Address {
        public Address() {
        }

        private String id;
        private String name;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }


}

class XMYPRefundPlatOrderBean {

    private int code;
    private String message;
    private com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data data;

    static class Data {
        public Data() {

        }

        private List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas> data;

        public void setData(List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas> data) {
            this.data = data;
        }

        public List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas> getData() {
            return data;
        }

        static class Datas {
            public Datas() {

            }

            private String id;
            private String refundType;
            private String orderId;
            private String pid;
            private String count;
            private String state;
            private String opCode;
            private String targetState;
            private String opTime;
            private String selfSupporting;
            private String type;
            private String serviceWay;
            private String orgId;
            private String orgName;
            private String applyReason;
            private String arbitrable;
            private String evaluable;
            private String createTime;
            private long createPerson;
            private String updateTime;
            private String updatePerson;
            private String createFrom;
            private String userExpressName;
            private String userExpressSn;
            private List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList> refundList;


            static class RefundList {
                public RefundList() {

                }

                private BigDecimal refundFee;
                private String refundStatus;
                private String notifyTime;
                private String state;
                private String pid;
                private int count;
                private String payType;

                public void setRefundFee(BigDecimal refundFee) {
                    this.refundFee = refundFee;
                }

                public BigDecimal getRefundFee() {
                    return refundFee;
                }

                public void setRefundStatus(String refundStatus) {
                    this.refundStatus = refundStatus;
                }

                public String getRefundStatus() {
                    return refundStatus;
                }

                public void setNotifyTime(String notifyTime) {
                    this.notifyTime = notifyTime;
                }

                public String getNotifyTime() {
                    return notifyTime;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getState() {
                    return state;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getPid() {
                    return pid;
                }

                public void setCount(int count) {
                    this.count = count;
                }

                public int getCount() {
                    return count;
                }

                public void setPayType(String payType) {
                    this.payType = payType;
                }

                public String getPayType() {
                    return payType;
                }

            }


            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setRefundType(String refundType) {
                this.refundType = refundType;
            }

            public String getRefundType() {
                return refundType;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getPid() {
                return pid;
            }

            public void setCount(String count) {
                this.count = count;
            }

            public String getCount() {
                return count;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getState() {
                return state;
            }

            public void setOpCode(String opCode) {
                this.opCode = opCode;
            }

            public String getOpCode() {
                return opCode;
            }

            public void setTargetState(String targetState) {
                this.targetState = targetState;
            }

            public String getTargetState() {
                return targetState;
            }

            public void setOpTime(String opTime) {
                this.opTime = opTime;
            }

            public String getOpTime() {
                return opTime;
            }

            public void setSelfSupporting(String selfSupporting) {
                this.selfSupporting = selfSupporting;
            }

            public String getSelfSupporting() {
                return selfSupporting;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }

            public void setServiceWay(String serviceWay) {
                this.serviceWay = serviceWay;
            }

            public String getServiceWay() {
                return serviceWay;
            }

            public void setOrgId(String orgId) {
                this.orgId = orgId;
            }

            public String getOrgId() {
                return orgId;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setApplyReason(String applyReason) {
                this.applyReason = applyReason;
            }

            public String getApplyReason() {
                return applyReason;
            }

            public void setArbitrable(String arbitrable) {
                this.arbitrable = arbitrable;
            }

            public String getArbitrable() {
                return arbitrable;
            }

            public void setEvaluable(String evaluable) {
                this.evaluable = evaluable;
            }

            public String getEvaluable() {
                return evaluable;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreatePerson(long createPerson) {
                this.createPerson = createPerson;
            }

            public long getCreatePerson() {
                return createPerson;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdatePerson(String updatePerson) {
                this.updatePerson = updatePerson;
            }

            public String getUpdatePerson() {
                return updatePerson;
            }

            public void setCreateFrom(String createFrom) {
                this.createFrom = createFrom;
            }

            public String getCreateFrom() {
                return createFrom;
            }

            public void setUserExpressName(String userExpressName) {
                this.userExpressName = userExpressName;
            }

            public String getUserExpressName() {
                return userExpressName;
            }

            public void setUserExpressSn(String userExpressSn) {
                this.userExpressSn = userExpressSn;
            }

            public String getUserExpressSn() {
                return userExpressSn;
            }

            public void setRefundList(List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList> refundList) {
                this.refundList = refundList;
            }

            public List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList> getRefundList() {
                return refundList;
            }

        }
    }


    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data data) {
        this.data = data;
    }

    public com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data getData() {
        return data;
    }

}