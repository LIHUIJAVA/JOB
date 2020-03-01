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
 * ����-С����Ʒ
 */
@Component
@Transactional
public class PlatOrderXMYP {

    @Autowired
    private StoreSettingsDao storeSettingsDao; //��������
    @Autowired
    private PlatOrderDao platOrderDao; //��������
    @Autowired
    private StoreRecordDao storeRecordDao; //���̵���
    @Autowired
    private PlatOrdersDao platOrdersDao; //�����ӱ�
    @Autowired
    private GetOrderNo getOrderNo; //���ɶ�����
    @Autowired
    private GoodRecordDao goodRecordDao; //��Ʒ����
    @Autowired
    private InvtyDocDao invtyDocDao; //�������
    @Autowired
    private ProdStruMapper prodStruDao; // ��Ʒ�ṹ
    @Autowired
    private CouponDetailDao couponDetailDao; //�Ż���ϸ
    @Autowired
    private PlatOrderPaymethodDao paymethodDao; //֧����ϸ
    @Autowired
    private RefundOrderDao refundOrderDao; //�˿����
    @Autowired
    private RefundOrdersDao refundOrdersDao; //�˿�ӱ�
    @Autowired
    private ExpressCorpMapper expressCorpDao;//��ݹ�˾
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
     * ������������-С����Ʒ
     */
    public String XMYPDownload(String accNum, int pageNo, int pageSize, String startDate, String endDate,
                               String storeId, Map<String, Integer> map) throws Exception {
        String resp = "";
        String message = "";
        boolean isSuccess = true;
        String method = "orderlist";
        try {


            // ����
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
                message = "��ѯʱ�������ݻ�����ԭ��";
                return resp = BaseJson.returnRespObj("ec/platOrder/download", false, message, null);

            }
            resp = XMYPOrdr(jsonOrderBean, storeId, accNum, storeRecord, map);
            if (jsonOrderBean.getResult().getTotal() > pageEnd) {
                resp = XMYPDownload(accNum, pageNo + 1, pageSize, startDate, endDate, storeId, map);
            }
            message = "�����Զ����ص��̣�" + storeRecord.getStoreName() + "����" + map.get("keys") + "��";


            resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);


        } catch (Exception e) {
            logger.error("���ش���" + e.getMessage(), e);
            message = "���ش���";
            resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
        }

        return resp;

    }

    /**
     * ������������-С����Ʒ
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
            // ����
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
     * ��������-С����Ʒ
     *
     * @throws Exception
     */
    public String XMYPOrdr(JsonOrderBean jsonOrderBean, String storeId, String userId, StoreRecord storeRecord, Map<String, Integer> map) throws Exception {
        String resp = "";
        String message = "";
        boolean isSucess = true;
        if (jsonOrderBean.getCode() != 0 || jsonOrderBean.getResult() == null) {
            message = "����ʧ��";
            return BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);
        }

        List<JsonOrderBean.Result.Data> datas = jsonOrderBean.getResult().getData();
        for (JsonOrderBean.Result.Data data : datas) {
// ��ѯ��Ӧ�����Ƿ���ڴ˶���

            if (platOrderDao.checkExsits(data.getOrder_id(), storeId) == 0) {
                map.put("keys", map.get("keys") + 1);
                ObjectMapper mapper = new ObjectMapper();
                address address = mapper.readValue(data.getAddress(), address.class);
                // ����������
//                maps.put("downloadCount", maps.get("downloadCount") + 1);


                String orderId = getOrderNo.getSeqNo("ec", userId);

                PlatOrder platOrder = new PlatOrder();

                platOrder.setEcOrderId(data.getOrder_id());// ���̶�����
                platOrder.setOrderId(orderId); // �������
                platOrder.setStoreId(storeId); // ���̱��
                platOrder.setStoreName(storeRecord.getStoreName()); // '��������' 1
                platOrder.setPayTime(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.getFtime() * 1000))); // ����ʱ��'
                // t
                // paytime
//				platOrder.setWaif(waif);		//	'���'
                platOrder.setIsAudit(0); // '�Ƿ����'
//				platOrder.setAuditHint(auditHint); // '�����ʾ'
                // totalfee
                // payment
                platOrder.setBuyerNote(data.getDescription().length() == 0 ? null : data.getDescription()); // �������' t buyermessage
//		platOrder.setSellerNote(trade.getSellerMemo()); // ���ұ�ע'
                platOrder.setRecAddress(address.getProvince().getName() +
                        address.getCity().getName() + address.getDistrict().getName()
                        + address.getArea().getName() + address.getAddress()); // �ջ�����ϸ��ַ' t receiveraddress
//		platOrder.setBuyerId(buyerId); // '��һ�Ա��'
                platOrder.setRecName(address.getConsignee()); // �ջ�������' t receivername
                platOrder.setRecMobile(address.getTel()); // �ջ����ֻ���' t receivermobile
                platOrder.setIsInvoice(0); // '�Ƿ�Ʊ'
                platOrder.setInvoiceTitle(data.getInvoice_title()); // '��Ʊ̧ͷ'
//		platOrder.setNoteFlag(trade.getSellerFlag().intValue()); // ���ұ�ע����' t sellerflag
                platOrder.setIsClose(0); // '�Ƿ�ر�'
                // discountfee
                platOrder.setOrderStatus(0); // '����״̬'
                platOrder.setReturnStatus(0); // '�˻�״̬'
                platOrder.setPayMoney(BigDecimal.ZERO); // 'ʵ�����
                platOrder.setGoodMoney(BigDecimal.ZERO); // ��Ʒ���
                platOrder.setDiscountMoney(BigDecimal.ZERO); // ϵͳ�Żݽ��
                platOrder.setAdjustMoney(BigDecimal.ZERO); // ���ҵ������'
                // platOrder.setMemo(memo); // '��ע'
                // platOrder.setAdjustStatus(adjustStatus); // '������ʶ'
                platOrder.setTradeDt(
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(data.getCtime() * 1000))); // '����ʱ��'
                // t
                // created
                platOrder.setSellTypId("1");// ��������������ͨ����
                platOrder.setBizTypId("2");// ����ҵ������2c
                platOrder.setRecvSendCateId("6");// �����շ����
// �������̻�����Ʒ�Ƿ�ȫ������[1-��,0-��]
                platOrder.setIsShip(data.getAlldelivery()); // '�Ƿ񷢻�'

//		platOrder.setRecvSendCateId(recvSendCateId); // '�շ������'
                platOrder.setOrderSellerPrice(data.getTotal_price()); // ���������ʵ�������'����֧�����(�����˷�)

                // receivedpayment
                platOrder.setProvince(address.getProvince().getName()); // ʡ' t receiverstate
                // platOrder.setProvinceId(provinceId); // 'ʡid'
                platOrder.setCity(address.getCity().getName()); // ��' t receivercity
                // platOrder.setCityId(cityId); //
                platOrder.setCounty(address.getDistrict().getName()); // ��' t receiverdistrict
                // platOrder.setCountyId(countyId); //
                platOrder.setTown(address.getArea().getName()); // '��'
                // platOrder.setTownId(townId); // '��id'
                platOrder.setFreightPrice(data.getShip_fee()); // �˷�' t

                platOrder.setVenderId("XMYP"); // '�̼�id'

                // orders Order[]
                platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                // "delivery_time": 1, // ����ʱ�� '1-�����ͻ�ʱ��,2-�������ͻ�,3-˫����/�����ͻ�',
                // //'�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����;2-ֻ˫���ա������ͻ�(�����ղ�����;3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱

                if (data.getDelivery_time() == 1) {
                    platOrder.setDeliveryType("����ʱ��");

                } else if (data.getDelivery_time() == 2) {
                    platOrder.setDeliveryType("1");

                } else if (data.getDelivery_time() == 3) {
                    platOrder.setDeliveryType("2");

                } else {
                    platOrder.setDeliveryType(data.getDelivery_time() + "");

                }
                // �������� (65Ϊ��Ʒ���ͣ���65Ϊ�̼��Է�)
                platOrder.setDeliverSelf(data.getConsignor() == 65 ? 0 : 1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t


                List<PlatOrders> list = new ArrayList<PlatOrders>();
                List<CouponDetail> couponList = new ArrayList<>();
                List<JsonOrderBean.Result.Data.Products> products = data.getProducts();
                for (JsonOrderBean.Result.Data.Products product : products) {
                    platOrder.setExpressNo(product.getProduct_express_sn());

                    PlatOrders platOrders = new PlatOrders();
                    platOrders.setGoodId(product.getPid()); // ƽ̨��Ʒ��� ��Ʒ����ID
                    platOrders.setGoodName(product.getName()); //  ƽ̨��Ʒ����
                    platOrders.setGoodNum(product.getCount()); //  ��Ʒ����
                    BigDecimal goodMoney = product.getProduct_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(product.getCount()));
                    platOrders.setGoodMoney(goodMoney); // ��Ʒ���'
                    platOrder.setGoodMoney(platOrder.getGoodMoney().add(goodMoney)); // ��Ʒ���

                    platOrders.setGoodPrice(product.getProduct_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));// ��Ʒ����
                    platOrders.setPayPrice(product.getSale_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP));// ʵ������
                    BigDecimal payMoney = product.getSale_price().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(product.getCount()));
                    platOrders.setPayMoney(payMoney); // ʵ�����
                    platOrder.setPayMoney(platOrder.getPayMoney().add(payMoney)); // ʵ�����

                    platOrders.setGoodSku(product.getGid()); // '��Ʒsku'
                    platOrders.setOrderId(orderId); // '�������' 1
//				platOrders.setExpressCom(xhstotal.getString("express_company_code")); // ��ݹ�˾' t�Ӷ��������Ŀ�ݹ�˾����
//			platOrders.setBatchNo(batchNo);	//	'����'
//			promotion_details		platOrders.setProActId(proActId);	//	��������' t promotionid	String	mjs	�Ż�id
                    BigDecimal discountMoney = goodMoney.subtract(payMoney);
                    platOrders.setDiscountMoney(discountMoney); // ϵͳ�Żݽ��
                    platOrder.setDiscountMoney(platOrder.getDiscountMoney().add(discountMoney)); // ϵͳ�Żݽ��
                    platOrders.setAdjustMoney(BigDecimal.ZERO); // ��ҵ������'
//			platOrders.setMemo(order.getOid().toString()); // '��ע' �� �ӵ�id oid
//			platOrders.setDeliverWhs(order.getStoreCode()); // �����ֿ����'
                    platOrders.setEcOrderId(product.getOrder_id()); // 'ƽ̨������' t oid


//                    platOrder.setDeliverWhs("");
                    //�Ƿ���Ʒ
                    if (payMoney != null && payMoney.compareTo(BigDecimal.ZERO) == 0) {

                        platOrders.setIsGift(1);
                        platOrder.setHasGift(1); // '�Ƿ���Ʒ'

                    } else {
                        platOrders.setIsGift(0);
                        platOrder.setHasGift(0); // '�Ƿ���Ʒ'

                    }
                    list.add(platOrders);
//			�Ż���Ϣ
                    CouponDetail coupon1 = new CouponDetail();
                    coupon1.setPlatId("XMYP");// ƽ̨id������JD����èJD
                    coupon1.setStoreId(storeId);// ����id
                    coupon1.setOrderId(product.getOrder_id());// ������
                    coupon1.setSkuId(null);
                    coupon1.setCouponCode(null);// �Ż����ͱ���
                    coupon1.setCouponType("XMYP�����Ż�");// �Ż�����
                    coupon1.setCouponPrice(discountMoney);//  �Żݽ��

                    if (coupon1.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
                        couponList.add(coupon1);
                    }
                }
                if (couponList.size() > 0) {
                    couponDetailDao.insert(couponList);
                }

                platOrder.setGoodNum(list.size()); // ��ƷƷ����

                platOrderDao.insert(platOrder);
                platOrdersDao.insert(list);

//                 ִ���Զ�ƥ��
                orderServiceImpl.autoMatch(platOrder.getOrderId(), userId);

                logger.info("�������سɹ�" + data.getOrder_id());
                resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, "�������سɹ�" + data.getOrder_id(), null);

            } else {
                message = "�����Ѵ���,������:" + (data.getOrder_id() + ",���̱��:" + storeId);
                isSucess = false;
                logger.error("��������:С����Ʒƽ̨�����Ѵ���,������:{},���̱��:{}", data.getOrder_id(), storeId);
                resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

            }


        }

        return resp;
    }

    /**
     * С����Ʒ-�˻��б�
     * @param orderIds  ������
     * @param startDate ������ʼʱ��
     * @param endDate   ��������ʱ��
     * @param storeId   ����id
     */
    @Transactional
    public String XMYPRefundPlatOrderV2(String userId, String startDate, String endDate, String storeId, String orderIds, int downloadCount) throws Exception {

        String message = "";
        boolean isSucess = true;
        String resp = "";
        String method = "aftersaleorders";

        try {
            // ����
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
                message = "�����˿����";
                return resp = BaseJson.returnRespObj("ec/refundOrder/download", false, message, null);
            }

            List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas> jsonArray = refundPlatOrderBean.getData().getData();
            if (refundPlatOrderBean.getData().getData() == null && refundPlatOrderBean.getData().getData().size() == 0) {
                message = "�����˿����";
                return resp = BaseJson.returnRespObj("ec/refundOrder/download", false, message, null);
            }

            for(int i = 0; i < jsonArray.size(); i++) {
                com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas job = jsonArray.get(i);

                if (platOrderDao.checkExsits(job.getId(), storeId) == 0) {
                    // ԭ������ɾ��,������
                    logger.info("ԭ������ɾ��,�����ڣ�" + job.getId());
                    continue;
                }
                if (refundOrderDao.selectEcRefId(job.getId()) == null) {
                    // �ж��˿�Ƿ��Ѵ���
                    logger.info("�ж��˿�Ѵ��ڣ�" + job.getId());
                    continue;
                }
                List<PlatOrder> platOrderlist = platOrderDao.selectPlatOrdersByEcOrderId(job.getOrderId());
                if (platOrderlist.size() == 0) {
                    message = "�˻�����Ӧ���۵�������,ƽ̨������:" + job.getOrderId();
                    continue;
                }
                PlatOrder platOrder = platOrderlist.get(0);


                RefundOrder refundOrder = new RefundOrder();
                refundOrder.setExpressCode(job.getUserExpressSn()); // ��ݵ���
                // ��������
                String refId = getOrderNo.getSeqNo("tk", userId);
                String ecOrderId = job.getOrderId();
                refundOrder.setRefId(refId); // �˿���
                refundOrder.setEcId("XMYP");
                refundOrder.setOrderId(platOrder.getOrderId()); // �������
                refundOrder.setStoreId(storeId); // ���̱��
                refundOrder.setEcOrderId(job.getOrderId());// ƽ̨������
                refundOrder.setStoreName(storeRecord.getStoreName()); // ��������
                refundOrder.setEcRefId(job.getId()); // �����˿��
                refundOrder.setApplyDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(job.getCreatePerson() * 1000))); // ��������
//				refundOrder.setBuyerId(refund.getBuyerOpenUid()); // ��һ�Ա��
                if (job.getType().equalsIgnoreCase("TK")) {
                    refundOrder.setIsRefund(0); // �Ƿ��˻�
                } else {
                    refundOrder.setIsRefund(1); // �Ƿ��˻�
                }

//				refundOrder.setAllRefNum(refund.getNum().intValue()); // �����˻�����
//				refundOrder.setAllRefMoney(new BigDecimal(refund.getRefundFee())); // �����˿���
                refundOrder.setRefReason(job.getApplyReason()); // �˿�ԭ��
//				refundOrder.setRefExplain(refund.getDesc()); // �˿�˵��
                refundOrder.setRefStatus(1); // �˿�״̬
                refundOrder.setDownTime(simpleDateFormat.format(new Date())); // ����ʱ��
                refundOrder.setTreDate(simpleDateFormat.format(new Date())); // ��������
                refundOrder.setOperator(userId); // ����Ա
                refundOrder.setIsAudit(0); // �Ƿ����
//			refundOrder.setMemo(memo); // ��ע
                refundOrder.setSource(0);// �˿��Դ����0
                refundOrder.setOperatorId(misUser.getAccNum());
                refundOrder.setOperatorTime(simpleDateFormat.format(new Date()));

                // �ö�����ȥ��ѯԭ���Ĺ��������Լ�ʵ�����
                List<PlatOrders> platOrders = platOrdersDao.selectByEcOrderId(ecOrderId);
                if (platOrders.size() == 0) {
                    // ��־��¼
                    LogRecord logRecord = new LogRecord();
                    logRecord.setOperatId(userId);
                    if (misUser != null) {
                        logRecord.setOperatName(misUser.getUserName());
                    }
                    logRecord.setOperatContent("�˿���أ�������" + ecOrderId + "δ�ҵ�������ϸ����������ʧ�ܡ�");
                    logRecord.setOperatTime(simpleDateFormat.format(new Date()));
                    logRecord.setOperatType(12);// 12�˿�����
                    logRecord.setTypeName("�˿����");
                    logRecord.setOperatOrder(ecOrderId);
                    logRecordDao.insert(logRecord);
                } else {
                    boolean flag = true;
                    for (int i1 = 0; i1 < platOrders.size(); i1++) {
                        // ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
                        if (!StringUtils.isNotEmpty(platOrders.get(i1).getInvId())) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        // ��־��¼
                        LogRecord logRecord = new LogRecord();
                        logRecord.setOperatId(userId);
                        if (misUser != null) {
                            logRecord.setOperatName(misUser.getUserName());
                        }
                        logRecord.setOperatContent("�˿���أ�������" + ecOrderId + "����δƥ�䵽��������ļ�¼����������ʧ�ܡ�");
                        logRecord.setOperatTime(simpleDateFormat.format(new Date()));
                        logRecord.setOperatType(12);// 12�˿�����
                        logRecord.setTypeName("�˿����");
                        logRecord.setOperatOrder(ecOrderId);
                        logRecordDao.insert(logRecord);
                        continue;
                    }
                }
                List<com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList> refundList = job.getRefundList();
                for (com.px.mis.ec.service.impl.XMYPRefundPlatOrderBean.Data.Datas.RefundList refund : refundList) {
                    // ���۵��ӱ�
                    List<PlatOrders> platOrdersList = platOrdersDao.selectByEcOrderAndGood(ecOrderId, refund.getPid(), null);
                    int ii = 0;
                    BigDecimal total = refund.getRefundFee().divide(new BigDecimal(100), 8, BigDecimal.ROUND_HALF_UP);

                    for (PlatOrders plat : platOrdersList) {
                        ii++;
                        RefundOrders refundOrders = new RefundOrders();
                        refundOrders.setRefId(refId);
                        refundOrders.setGoodSku(null);// ��Ʒsku
                        refundOrders.setEcGoodId(plat.getGoodId());// ���õ�����Ʒ����
                        InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(plat.getInvId());
                        if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
                            refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));// ���ñ�����
                        }
                        refundOrders.setIsGift(plat.getIsGift());// �Ƿ���Ʒ��ԭ��������
                        refundOrders.setPrdcDt(plat.getPrdcDt());// ������������
                        refundOrders.setInvldtnDt(plat.getInvldtnDt());// ����ʧЧ����
                        refundOrders.setGoodId(plat.getInvId());// ���ô������
                        refundOrders.setGoodName(invtyDoc.getInvtyNm());// ���ô������
                        refundOrders.setBatchNo(plat.getBatchNo());// �����˻�����
                        refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());// ȡ��Ӧ���̵�Ĭ���˻���

                        if (refund.getCount() == plat.getGoodNum()) {
                            refundOrders.setRefNum(plat.getInvNum()); // �˻�����
                            refundOrders.setRefMoney(total); // �˻����

                        } else {

                            if (ii == platOrdersList.size()) {
                                Integer integers = plat.getInvNum() * plat.getGoodNum() / refund.getCount();
                                refundOrders.setRefNum(integers); // �˻�����
                                refundOrders.setRefMoney(total); // �˻����
                            } else {
                                Integer integer = plat.getInvNum() * plat.getGoodNum() / refund.getCount();
                                refundOrders.setRefNum(integer); // �˻�����
                                refundOrders.setRefMoney(plat.getPayPrice().multiply(new BigDecimal(integer))); // �˻����
                                total.subtract(refundOrders.getRefMoney());
                            }

                        }
                        refundOrders.setCanRefNum(plat.getCanRefNum());// ���ÿ�������
                        refundOrders.setCanRefMoney(plat.getCanRefMoney());// ���˽��
//                        refundOrders.setRefMoney(platOrders.get(i1).getPayMoney());// �˻����


                        refundOrdersList.add(refundOrders);// װ��list


                    }


                }
            }
            logger.info("����������" + refundOrderList.size());

            if (refundOrderList.size() > 0) {
                refundOrderDao.insertList(refundOrderList);
                refundOrdersDao.insertList(refundOrdersList);
                downloadCount += refundOrderList.size();
            }


            // ��־��¼
            LogRecord logRecord = new LogRecord();
            logRecord.setOperatId(userId);
            if (misUser != null) {
                logRecord.setOperatName(misUser.getUserName());
            }
            logRecord.setOperatContent(
                    "�˿���أ����γɹ�����С����Ʒ���̣�" + storeRecord.getStoreName() + ",�˿" + downloadCount + "��");
            logRecord.setOperatTime(simpleDateFormat.format(new Date()));
            logRecord.setOperatType(12);// 12�˿�����
            logRecord.setTypeName("�˿�����");
            logRecordDao.insert(logRecord);
            resp = BaseJson.returnResp("ec/refundOrder/download ", true,
                    "�˿���أ����γɹ�����С����Ʒ���̣�" + storeRecord.getStoreName() + ",�˿" + downloadCount + "��", null);


        } catch (Exception e) {
            e.printStackTrace();
            try {
                resp = BaseJson.returnResp("ec/refundOrder/download ", false, "�˿���أ�����" + downloadCount + "��������쳣",
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
//        String res = " {\"code\":0,\"message\":\"OK\",\"result\":{\"data\":[{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"252\\\",\\\"name\\\":\\\"��ݸ��\\\"},\\\"district\\\":{\\\"id\\\":\\\"2340\\\",\\\"name\\\":\\\"��ʯ��\\\"},\\\"area\\\":{\\\"id\\\":\\\"2340001\\\",\\\"name\\\":\\\"��ʯ��\\\"},\\\"tel\\\":\\\"13728499709\\\",\\\"address\\\":\\\"����·���Ľ�11��\\\",\\\"consignee\\\":\\\"���ï\\\",\\\"zipcode\\\":\\\"523027\\\",\\\"address_id\\\":\\\"10160331992526546\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1573109405,\\\"add_time\\\":1459425552,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575526129,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575526138,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205958301114,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575526129,\"customized_info\":\"\",\"delivery_time\":1575530635,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575526138,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 900G ����\",\"order_id\":4191205958301114,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":33000,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01262594795\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":103907175}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":33000},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"252\\\",\\\"name\\\":\\\"��ݸ��\\\"},\\\"district\\\":{\\\"id\\\":\\\"2328\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"2328001\\\",\\\"name\\\":\\\"������\\\"},\\\"tel\\\":\\\"13609670315\\\",\\\"address\\\":\\\"������·����ҽԺ�����\\\",\\\"consignee\\\":\\\"������\\\",\\\"zipcode\\\":\\\"523068\\\",\\\"address_id\\\":\\\"9952861\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1573297487,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575529040,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575529051,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205973202099,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":4,\"ctime\":1575529040,\"customized_info\":\"\",\"delivery_time\":1575615451,\"deliverycount\":4,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575529051,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 900G ����\",\"order_id\":4191205973202099,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":66000,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01269766346\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":110482078}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":66000},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"235\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2222\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"2222002\\\",\\\"name\\\":\\\"�����ֵ�\\\"},\\\"tel\\\":\\\"13790877272\\\",\\\"address\\\":\\\"��԰19��A605\\\",\\\"consignee\\\":\\\"��С��\\\",\\\"zipcode\\\":\\\"518116\\\",\\\"address_id\\\":\\\"10160502951601782\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1548765290,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575533046,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575533057,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205976803295,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575533046,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575533057,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 900G ����\",\"order_id\":4191205976803295,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":33000,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270339001\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":963087220}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.102.0\",\"total_price\":33000},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"17\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"196\\\",\\\"name\\\":\\\"�����\\\"},\\\"district\\\":{\\\"id\\\":\\\"1896\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"1896006\\\",\\\"name\\\":\\\"�������ֵ�\\\"},\\\"tel\\\":\\\"18939337877\\\",\\\"address\\\":\\\"����·�����ֵ���С��\\\",\\\"consignee\\\":\\\"֣����\\\",\\\"zipcode\\\":\\\"457001\\\",\\\"address_id\\\":\\\"11000000044987889\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1563713189,\\\"add_time\\\":1563713189,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575543257,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575543272,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205959311234,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575543257,\"customized_info\":\"\",\"delivery_time\":1575621319,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575543272,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 900G ����\",\"order_id\":4191205959311234,\"order_type\":0,\"partner_id\":2706,\"pid\":88280,\"price\":16500,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270806577\",\"product_price\":22800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":1458816}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.101.0\",\"total_price\":16500},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"12\\\",\\\"name\\\":\\\"�㽭\\\"},\\\"city\\\":{\\\"id\\\":\\\"122\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"1263\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"1263004\\\",\\\"name\\\":\\\"������\\\"},\\\"tel\\\":\\\"13758271657\\\",\\\"address\\\":\\\"ȫ����·439�Ž���Ԣ7-1-403�ң��۽�Ū������\\\",\\\"consignee\\\":\\\"���\\\",\\\"zipcode\\\":\\\"310000\\\",\\\"address_id\\\":\\\"10150824510933570\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1573482318,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575544385,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575544392,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":5,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"����\",\"order_id\":4191205978513117,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575544385,\"customized_info\":\"\",\"delivery_time\":1575619581,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575544392,\"gid\":115007,\"invoice_status\":1,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 1200G ����\",\"order_id\":4191205978513117,\"order_type\":0,\"partner_id\":2706,\"pid\":88281,\"price\":17200,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270402766\",\"product_price\":23200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":17200,\"status\":6,\"uid\":36825362}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":17200},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"244\\\",\\\"name\\\":\\\"÷����\\\"},\\\"district\\\":{\\\"id\\\":\\\"2284\\\",\\\"name\\\":\\\"�廪��\\\"},\\\"area\\\":{\\\"id\\\":\\\"2284010\\\",\\\"name\\\":\\\"�����\\\"},\\\"tel\\\":\\\"13068749763\\\",\\\"address\\\":\\\"˫ͷ����Ժ\\\",\\\"consignee\\\":\\\"��Է��\\\",\\\"zipcode\\\":\\\"514400\\\",\\\"address_id\\\":\\\"11000000105014999\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1575552311,\\\"add_time\\\":1575552311,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575553253,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575553270,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":5,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"13068749763\",\"invoice_title\":\"����\",\"order_id\":4191205976405008,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":3,\"ctime\":1575553253,\"customized_info\":\"\",\"delivery_time\":1575619590,\"deliverycount\":3,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575553270,\"gid\":115007,\"invoice_status\":1,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 1200G ����\",\"order_id\":4191205976405008,\"order_type\":0,\"partner_id\":2706,\"pid\":88281,\"price\":56390,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270376413\",\"product_price\":23200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":18800,\"status\":6,\"uid\":1406912475}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":56390},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"252\\\",\\\"name\\\":\\\"��ݸ��\\\"},\\\"district\\\":{\\\"id\\\":\\\"2344\\\",\\\"name\\\":\\\"��ƽ��\\\"},\\\"area\\\":{\\\"id\\\":\\\"2344001\\\",\\\"name\\\":\\\"��ƽ��\\\"},\\\"tel\\\":\\\"13686092299\\\",\\\"address\\\":\\\"����С�Ǵ���Էa��3a\\\",\\\"consignee\\\":\\\"��־��\\\",\\\"zipcode\\\":\\\"523036\\\",\\\"address_id\\\":\\\"10160323609030723\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1566625329,\\\"add_time\\\":1458717236,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575556938,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575556948,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205962005983,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575556938,\"customized_info\":\"\",\"delivery_time\":1575619590,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575556948,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 1200G ����\",\"order_id\":4191205962005983,\"order_type\":0,\"partner_id\":2706,\"pid\":88281,\"price\":37600,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270323188\",\"product_price\":23200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":18800,\"status\":6,\"uid\":956499222}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":37600},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"18\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"205\\\",\\\"name\\\":\\\"�人��\\\"},\\\"district\\\":{\\\"id\\\":\\\"1977\\\",\\\"name\\\":\\\"��ɽ��\\\"},\\\"area\\\":{\\\"id\\\":\\\"1977014\\\",\\\"name\\\":\\\"ʨ��ɽ�ֵ�\\\"},\\\"tel\\\":\\\"15971482752\\\",\\\"address\\\":\\\"����ũҵ��ѧ��ԷС��40��149��301\\\",\\\"consignee\\\":\\\"����\\\",\\\"zipcode\\\":\\\"430070\\\",\\\"address_id\\\":\\\"11000000096383673\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1573804539,\\\"add_time\\\":1573804539,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575537931,\"delivery_time\":0,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575537938,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205956810173,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575537931,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575537938,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 900G �Ķ�\",\"order_id\":4191205956810173,\"order_type\":0,\"partner_id\":2706,\"pid\":88282,\"price\":32400,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270402748\",\"product_price\":19800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16200,\"status\":6,\"uid\":271883305}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.0.0\",\"total_price\":32400},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"18\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"209\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2013\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"2013020\\\",\\\"name\\\":\\\"��ԭ�ֵ�\\\"},\\\"tel\\\":\\\"13871719340\\\",\\\"address\\\":\\\"��ԭ·��ԭ�г�������\\\",\\\"consignee\\\":\\\"ʩ��\\\",\\\"zipcode\\\":\\\"441001\\\",\\\"address_id\\\":\\\"11000000101181303\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1574649594,\\\"add_time\\\":1574649594,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575531173,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575531180,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷�\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205974802821,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575531173,\"customized_info\":\"\",\"delivery_time\":1575621320,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575531180,\"gid\":115007,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� 1200G �Ķ�\",\"order_id\":4191205974802821,\"order_type\":0,\"partner_id\":2706,\"pid\":88283,\"price\":16500,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270778100\",\"product_price\":20200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":16500,\"status\":6,\"uid\":2164327825}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.102.0\",\"total_price\":16500},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"22\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"268\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2471\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"2471002\\\",\\\"name\\\":\\\"������\\\"},\\\"tel\\\":\\\"13379892816\\\",\\\"address\\\":\\\"ɽ�ߴ������ܶӺ��Ÿ�3��802��\\\",\\\"consignee\\\":\\\"����\\\",\\\"zipcode\\\":\\\"570105\\\",\\\"address_id\\\":\\\"10180507937608863\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1546318934,\\\"add_time\\\":1525686296,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575529538,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575562202,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205965501701,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575529538,\"customized_info\":\"\",\"delivery_time\":1575615451,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575562202,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ 900g*6�� ����\",\"order_id\":4191205965501701,\"order_type\":0,\"partner_id\":2706,\"pid\":88446,\"price\":110800,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01269755761\",\"product_price\":160800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":160800,\"status\":6,\"uid\":18027061}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":110800},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"235\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2220\\\",\\\"name\\\":\\\"��ɽ��\\\"},\\\"area\\\":{\\\"id\\\":\\\"2220008\\\",\\\"name\\\":\\\"���ֵ̽�\\\"},\\\"tel\\\":\\\"18688780656\\\",\\\"address\\\":\\\"�۳�·ǰ���廨԰7-313\\\",\\\"consignee\\\":\\\"���ӽ��\\\",\\\"zipcode\\\":\\\"518052\\\",\\\"address_id\\\":\\\"10180516396304376\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1526440163,\\\"add_time\\\":1526440163,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575560595,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575561719,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205982313053,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575560595,\"customized_info\":\"\",\"delivery_time\":1575619581,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575561719,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ 900g*6�� ����\",\"order_id\":4191205982313053,\"order_type\":0,\"partner_id\":2706,\"pid\":88448,\"price\":97800,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270319408\",\"product_price\":136800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":136800,\"status\":6,\"uid\":1137778750}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":97800},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"13\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"148\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"1452\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"1452001\\\",\\\"name\\\":\\\"��ɿ���\\\"},\\\"tel\\\":\\\"18056620254\\\",\\\"address\\\":\\\"��ɿ��׶�԰\\\",\\\"consignee\\\":\\\"ŷ����\\\",\\\"zipcode\\\":\\\"247200\\\",\\\"address_id\\\":\\\"11000000104921531\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1575430218,\\\"add_time\\\":1575430218,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575430233,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575606553,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191204929400236,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":2,\"ctime\":1575430233,\"customized_info\":\"\",\"delivery_time\":1575705868,\"deliverycount\":2,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575606553,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ 900g*6�� ����\",\"order_id\":4191204929400236,\"order_type\":0,\"partner_id\":2706,\"pid\":88448,\"price\":195400,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01277668642\",\"product_price\":136800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":136800,\"status\":6,\"uid\":47156356}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":195400},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"20\\\",\\\"name\\\":\\\"�㶫\\\"},\\\"city\\\":{\\\"id\\\":\\\"235\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2220\\\",\\\"name\\\":\\\"��ɽ��\\\"},\\\"area\\\":{\\\"id\\\":\\\"2220008\\\",\\\"name\\\":\\\"���ֵ̽�\\\"},\\\"tel\\\":\\\"18688780656\\\",\\\"address\\\":\\\"�۳�·ǰ���廨԰7-313\\\",\\\"consignee\\\":\\\"���ӽ��\\\",\\\"zipcode\\\":\\\"518052\\\",\\\"address_id\\\":\\\"10180516396304376\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1526440163,\\\"add_time\\\":1526440163,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575560639,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575561657,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205974007112,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575560639,\"customized_info\":\"\",\"delivery_time\":1575615451,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575561657,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ 900g*4�� �Ķ�\",\"order_id\":4191205974007112,\"order_type\":0,\"partner_id\":2706,\"pid\":88450,\"price\":63200,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01269763951\",\"product_price\":79200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":79200,\"status\":6,\"uid\":1137778750}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":63200},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"16\\\",\\\"name\\\":\\\"ɽ��\\\"},\\\"city\\\":{\\\"id\\\":\\\"175\\\",\\\"name\\\":\\\"��̨��\\\"},\\\"district\\\":{\\\"id\\\":\\\"1700\\\",\\\"name\\\":\\\"֥���\\\"},\\\"area\\\":{\\\"id\\\":\\\"1700008\\\",\\\"name\\\":\\\"�����ֵ�\\\"},\\\"tel\\\":\\\"18953558180\\\",\\\"address\\\":\\\"��ɽ·����ɽׯ57��11¥��\\\",\\\"consignee\\\":\\\"�Ÿ�\\\",\\\"zipcode\\\":\\\"264001\\\",\\\"address_id\\\":\\\"10180417140706195\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1535609962,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575522106,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575587405,\"goods_name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ\",\"goods_price\":39600,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205959404141,\"otype\":16,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575522106,\"customized_info\":\"\",\"delivery_time\":1575621319,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575587405,\"gid\":115052,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ���Friso) Ӥ�׶��̷� ��װϵ�й�װ���װ 900g*4�� �Ķ�\",\"order_id\":4191205959404141,\"order_type\":0,\"partner_id\":2706,\"pid\":88450,\"price\":63200,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270817314\",\"product_price\":79200,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":79200,\"status\":6,\"uid\":1304077150}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":63200},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"23\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"271\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2506\\\",\\\"name\\\":\\\"��������\\\"},\\\"area\\\":{\\\"id\\\":\\\"2506004\\\",\\\"name\\\":\\\"���ɽֵ�\\\"},\\\"tel\\\":\\\"15178793885\\\",\\\"address\\\":\\\"�͹�����3��\\\",\\\"consignee\\\":\\\"����\\\",\\\"zipcode\\\":\\\"400050\\\",\\\"address_id\\\":\\\"11000000105036429\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1575589068,\\\"add_time\\\":1575589068,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575589223,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575589236,\"goods_name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191206987936285,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575589223,\"customized_info\":\"\",\"delivery_time\":1575619590,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575589236,\"gid\":115022,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������ 1�� 400G\",\"order_id\":4191206987936285,\"order_type\":0,\"partner_id\":2706,\"pid\":88451,\"price\":6900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270384683\",\"product_price\":13800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":6900,\"status\":6,\"uid\":1259504412}],\"ship_fee\":0,\"shopbrand\":\"2019.1000.101.0\",\"total_price\":6900},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"16\\\",\\\"name\\\":\\\"ɽ��\\\"},\\\"city\\\":{\\\"id\\\":\\\"171\\\",\\\"name\\\":\\\"�ൺ��\\\"},\\\"district\\\":{\\\"id\\\":\\\"1669\\\",\\\"name\\\":\\\"�б���\\\"},\\\"area\\\":{\\\"id\\\":\\\"1669022\\\",\\\"name\\\":\\\"ˮ�幵�ֵ�\\\"},\\\"tel\\\":\\\"18561858571\\\",\\\"address\\\":\\\"������·127���ൺ������ҽԺ\\\",\\\"consignee\\\":\\\"�ڽ�\\\",\\\"zipcode\\\":\\\"266011\\\",\\\"address_id\\\":\\\"10170407589306286\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":1,\\\"award_default\\\":0,\\\"update_time\\\":1540526784,\\\"add_time\\\":1491568705,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575555804,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575555931,\"goods_name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205963204896,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575555804,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575555931,\"gid\":115022,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������ 1�� 900G\",\"order_id\":4191205963204896,\"order_type\":0,\"partner_id\":2706,\"pid\":88452,\"price\":25900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270402730\",\"product_price\":28800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":25900,\"status\":6,\"uid\":23033163}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":25900},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"16\\\",\\\"name\\\":\\\"ɽ��\\\"},\\\"city\\\":{\\\"id\\\":\\\"171\\\",\\\"name\\\":\\\"�ൺ��\\\"},\\\"district\\\":{\\\"id\\\":\\\"1673\\\",\\\"name\\\":\\\"�����\\\"},\\\"area\\\":{\\\"id\\\":\\\"1673001\\\",\\\"name\\\":\\\"��ɽ·�ֵ�\\\"},\\\"tel\\\":\\\"18561858571\\\",\\\"address\\\":\\\"��ɽ·�������һ��A��11��¥1��Ԫ204\\\",\\\"consignee\\\":\\\"�ڽ�\\\",\\\"zipcode\\\":\\\"266021\\\",\\\"address_id\\\":\\\"10170411702201084\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1538527419,\\\"add_time\\\":1491864222,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575556094,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575556100,\"goods_name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������\",\"goods_price\":13800,\"invoice\":0,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"\",\"order_id\":4191205965604076,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575556094,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575556100,\"gid\":115022,\"invoice_status\":0,\"invoice_url\":\"\",\"name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������ 1�� 900G\",\"order_id\":4191205965604076,\"order_type\":0,\"partner_id\":2706,\"pid\":88452,\"price\":25900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270330082\",\"product_price\":28800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":25900,\"status\":6,\"uid\":23033163}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.102.0\",\"total_price\":25900},{\"address\":\"{\\\"country\\\":{\\\"id\\\":\\\"1\\\",\\\"name\\\":\\\"�й���½\\\"},\\\"province\\\":{\\\"id\\\":\\\"19\\\",\\\"name\\\":\\\"����\\\"},\\\"city\\\":{\\\"id\\\":\\\"229\\\",\\\"name\\\":\\\"������\\\"},\\\"district\\\":{\\\"id\\\":\\\"2170\\\",\\\"name\\\":\\\"������\\\"},\\\"area\\\":{\\\"id\\\":\\\"2170001\\\",\\\"name\\\":\\\"�����ֵ�\\\"},\\\"tel\\\":\\\"13874699070\\\",\\\"address\\\":\\\"��ˮ��·35���º����г�\\\",\\\"consignee\\\":\\\"�����\\\",\\\"zipcode\\\":\\\"425000\\\",\\\"address_id\\\":\\\"21535418\\\",\\\"tag_name\\\":\\\"\\\",\\\"matching\\\":[],\\\"type\\\":\\\"\\\",\\\"need_edit\\\":0,\\\"is_invalid\\\":0,\\\"is_default\\\":0,\\\"award_default\\\":0,\\\"update_time\\\":1564654014,\\\"add_time\\\":0,\\\"address_name\\\":\\\"\\\"}\",\"aftersaleOrders\":[],\"alldelivery\":1,\"consignee_id_card\":{\"card_id\":\"\",\"card_name\":\"\"},\"consignor\":2706,\"coupon_amount\":0,\"ctime\":1575559605,\"delivery_time\":1,\"description\":\"\",\"ext_info\":\"\",\"ftime\":1575559614,\"goods_name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������\",\"goods_price\":13800,\"invoice\":5,\"invoice_company_code\":\"\",\"invoice_email\":\"\",\"invoice_phone\":\"\",\"invoice_title\":\"����\",\"order_id\":4191205957611854,\"otype\":6,\"partner_id\":2706,\"payment_info\":{\"importation_type\":[]},\"products\":[{\"bizcode\":\"\",\"count\":1,\"ctime\":1575559605,\"customized_info\":\"\",\"delivery_time\":1575619580,\"deliverycount\":1,\"express_av_fee\":0,\"express_sn\":\"\",\"ftime\":1575559614,\"gid\":115022,\"invoice_status\":1,\"invoice_url\":\"\",\"name\":\"���ؼѶ����� ��Frisomum�� �в����䷽�̷۵������ 1�� 900G\",\"order_id\":4191205957611854,\"order_type\":0,\"partner_id\":2706,\"pid\":88452,\"price\":23900,\"product_bizcode\":\"jd\",\"product_express_sn\":\"JDVA01270323161\",\"product_price\":28800,\"refund_id\":0,\"refunddescription\":\"\",\"refundprice\":0,\"refundstatus\":0,\"refundtype\":-1,\"rtime\":0,\"sale_price\":25900,\"status\":6,\"uid\":85961556}],\"ship_fee\":0,\"shopbrand\":\"2019.2000.101.0\",\"total_price\":23900}],\"total\":19}}\n";
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

            map.put("express_sn", "123");// ��ݵ���
            map.put("express_name", "�й�����EMS");// ��ݹ�˾����
            map.put("bizcode", "ems");// ��ݹ�˾���
            map.put("userId", "1");// ���Ե�¼�̻���̨�������˺�
            map.put("descr", "");// ��Ʒ�ͺ�ID
            map.put("pid", "0");// ��Ʒ�ͺ�ID
            map.put("donkey_id", "0"); // ��С¿������0
            map.put("order_id", "123456");// ����ID

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
     * ��Ʒ������-С����Ʒ
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
//	        ��Ʒ����
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
            logger.error("���ش���" + propLists.getMsg());
        }
        //Jsonӳ��Ϊ����
        List<propList.Data> datas = propLists.getData();

        for (propList.Data zdata : datas) {
            List<propList.Data.Sku_ids> indexs = zdata.getSku_ids();
            for (propList.Data.Sku_ids sku : indexs) {
                GoodRecord gRecord = new GoodRecord();
                //��Ʒ����
                gRecord.setStoreId(storeSettings.getStoreId());// ���̱��
                gRecord.setEcId("XMYP");// ����ƽ̨���
                gRecord.setEcName("С����Ʒ");// ����ƽ̨����
                gRecord.setGoodSku(zdata.getGid().toString());// ��Ʒsku
                gRecord.setEcGoodId(zdata.getPid().toString());// ƽ̨��Ʒ���
                gRecord.setEcGoodName(sku.getName());// ƽ̨��Ʒ����
                BigDecimal priceb = new BigDecimal(zdata.getPrice());
                BigDecimal num = new BigDecimal(100);
                BigDecimal Pricey = priceb.divide(num, 8, BigDecimal.ROUND_HALF_DOWN);
                gRecord.setUpsetPrice(Pricey);// ����ۼ�
                gRecord.setSafeInv("0");// ��ȫ���
                gRecord.setSkuProp("");// sku����
                gRecord.setOnlineStatus("����");// ����״̬
                gRecord.setOperator(accNum);//������
                gRecord.setOperatTime(date);//����ʱ��
                gRecord.setIsSecSale(0);//�Ƿ����
                gRecord.setMemo(""); // ��ע
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