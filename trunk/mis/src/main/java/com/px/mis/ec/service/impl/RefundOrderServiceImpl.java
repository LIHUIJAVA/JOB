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
    private Integer downloadCount = 0;//��¼��������
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

            refundOrder.setOperator(misUser.getUserName());// ���ô�����
            refundOrder.setOperatorId(misUser.getAccNum());// ���ô�����id
            refundOrder.setSource(0);// �����˿��Դ�ֹ�����
            refundOrder.setSourceNo("");// �ֹ��������˿û����Դ����
            refundOrder.setDownTime(sdf.format(new Date()));// ��������ʱ��Ϊ��ǰʱ��
            refundOrder.setIsAudit(0);// ���ô����
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
                        message = "û�и���Ʒ��������ʧ�ܣ�";
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
            message = "�����ɹ�!";
            isSuccess = true;
            resp = BaseJson.returnRespObjList("ec/refundOrder/add", isSuccess, message, refundOrder, refundOrdersList);

        } catch (Exception e) {
            logger.error("URL��ec/refundOrder/add �쳣˵����", e);
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
                message = "���" + refundOrder.getRefId() + "�����ڣ�";
                isSuccess = false;
            } else {
                refundOrderDao.update(refundOrder);
                refundOrdersDao.delete(refundOrder.getRefId());
                refundOrdersDao.insertList(refundOrdersList);
                message = "�޸ĳɹ�!";
                isSuccess = true;
            }
            resp = BaseJson.returnRespObj("ec/refundOrder/edit", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL��ec/refundOrder/edit �쳣˵����", e);
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
                        // �˿����ˣ�����ֱ��ɾ��
                        refundOrder.setAuditHint("�˿����ˣ�����ֱ��ɾ��");
                        refundOrderDao.update(refundOrder);
                    } else {
                        if (refundOrder.getSource() == 1) {
                            // ���������ۺ�
                            Aftermarket aftermarket = aftermarketDao
                                    .selectById(Integer.parseInt(refundOrder.getSourceNo()));
                            aftermarketDao.delete(aftermarket);
                        } else if (refundOrder.getSource() == 2) {
                            CancelOrder cancelOrder = cancelOrderDao.selectById(refundOrder.getSourceNo());
                            cancelOrderDao.delete(cancelOrder);
                            // ����ȡ������
                        } else if (refundOrder.getSource() == 3) {
                            // �����⸶
                            Compensate compensate = compensateDao.selectById(refundOrder.getSourceNo());
                            compensateDao.delete(compensate);
                        }
                        okids.add(refIdList.get(i));
                    }
                }
                if (okids.size() > 0) {
                    refundOrderDao.delete(okids);
                }
                message = "ɾ�����!���γɹ�ɾ���˿" + okids.size() + "��";
                isSuccess = true;
            } else {
                message = "��ѡ��Ҫɾ�����˿��";
                isSuccess = false;
            }
            resp = BaseJson.returnRespObj("ec/refundOrder/delete", isSuccess, message, null);
        } catch (Exception e) {
            logger.error("URL��ec/refundOrder/delete �쳣˵����", e);
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
                message = "���" + refId + "������!";
                isSuccess = false;
            } else {
                message = "��ѯ�ɹ�!";
                isSuccess = true;
            }
            resp = BaseJson.returnRespObjList("ec/refundOrder/query", isSuccess, message, refundOrder,
                    refundOrdersList);
        } catch (Exception e) {
            logger.error("URL��ec/refundOrder/query �쳣˵����", e);
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
            message = "��ѯ�ɹ�";
            isSuccess = true;
            resp = BaseJson.returnRespList("ec/refundOrder/queryList", isSuccess, message, count, pageNo, pageSize, 0,
                    0, refundOrderList);
        } catch (Exception e) {
            logger.error("URL��ec/refundOrder/queryList �쳣˵����", e);
        }
        return resp;
    }

    /**
     * �˿����ԭʼ���̶���
     */
    @Override
    public String refundReference(String ecOrderId, String orderId) {
        // TODO Auto-generated method stub
        String message = "";
        Boolean isSuccess = true;
        String resp = "��ѯ�ɹ�";
        // �ȸ���ƽ̨���̶����Ų��Ҷ����Ƿ��������˵ĵ��̶���
        List<PlatOrder> platOrders = refundOrderDao.selectPlatOrderListByEcOrderId(ecOrderId);
        // List<SellSnglSub> sellSnglSubsList = new ArrayList<>();
        if (platOrders.size() == 0) {
            isSuccess = false;
            message = "�����ն���δ��˻򲻴���";
        } else {
            if (orderId.length() > 0) {
                platOrders = platOrderDao.selectByOrderId(orderId);
            }

            /*
             * if (sellSngls.size() == 0) { isSuccess = false; message =
             * "�����ն���û�ж�Ӧ���۵����Ӧ���۵�δ���"; } else { for (int i = 0; i < sellSngls.size(); i++)
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
                        // �˿�Ѿ����
                        refundOrder.setAuditHint("�˿����ˣ������ظ���ˡ�");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }

                    if (StringUtils.isEmpty(refundOrder.getApplyDate())) {

                        refundOrder.setAuditHint("�˿��������Ϊ�գ��������");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    String time = refundOrder.getTreDate();
                    if (StringUtils.isEmpty(time)) {
                        refundOrder.setAuditHint("�˿��������Ϊ�գ��������");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    if (formBookService.isMthSeal(loginDate)) {
                        refundOrder.setAuditHint("��¼���������·��ѷ��ˣ��������");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    List<PlatOrder> platOrderlist = platOrderDao
                            .selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId());
                    if (platOrderlist.size() == 0) {
                        // �˿��Ӧƽ̨������ɾ��
                        refundOrder.setAuditHint("�˿��Ӧ������ɾ����������ˡ�");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    if (platOrderDao.selectNoAuditOrderByEcOrderId(refundOrder.getEcOrderId()) > 0) {
                        // �˿��Ӧԭ������δ���
                        refundOrder.setAuditHint("�˿��Ӧԭ������δ��ˣ���������˿");
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

                        refundOrder.setAuditHint("���ʧ�ܣ������˻�������ʧ��");
                        refundOrderDao.update(refundOrder);

                        continue;

                    }
                    rtnGoods.setRtnGoodsId(rtnGoodsId);// �˻�����
                    //rtnGoods.setRtnGoodsDt(loginDate);// �������ڣ��˻�������ѡ�����ʱ�ĵ�¼����
                    rtnGoods.setRtnGoodsDt(loginDate.split(" ")[0] + sdf.format(new Date()).substring(10));//�˻����ڣ��õ�¼���ڼ�ϵͳʱ����
                    // PlatOrder selectPlatOrder =
                    // platOrderDao.select(refundOrder.getOrderId());//��ѯԭ����
                    rtnGoods.setSellTypId(platOrderlist.get(0).getSellTypId());// �������ͱ�� ��
                    rtnGoods.setBizTypId(platOrderlist.get(0).getBizTypId());// ҵ�����ͱ�ţ�
                    rtnGoods.setRecvSendCateId(platOrderlist.get(0).getRecvSendCateId());// �շ�����ţ�
                    rtnGoods.setRefId(refundOrder.getRefId());// ���˻����������˿�ţ������������
                    rtnGoods.setTxId(platOrderlist.get(0).getOrderId());// ���ö�����
                    rtnGoods.setCustOrdrNum(refundOrder.getEcOrderId());// ƽ̨����
                    rtnGoods.setFormTypEncd("008");
                    StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
                    if (storeRecord != null) {
                        rtnGoods.setCustId(storeRecord.getCustomerId());// �ͻ����
                    } else {
                        // �˿��Ӧ���̲�����
                        refundOrder.setAuditHint("�˿��Ӧ���̵��������ڣ���������˿");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    rtnGoods.setAccNum(storeRecord.getRespPerson());// ҵ��Ա��� ȡ���̸�����id
                    rtnGoods.setUserName(misUser.getUserName());// �û�����
                    rtnGoods.setSetupPers(misUser.getAccNum());// ������ �û����
                    rtnGoods.setSetupTm(sdf.format(new Date()));// ����ʱ�䣻
                    // �ջ���ַ����
                    // �жϲֿ�����Ƿ�һ�£������һ�£����ʧ��
                    String whsEncd = refundOrderss.get(0).getRefWhs();
                    boolean flag = true;
                    for (RefundOrders refundOrders1 : refundOrderss) {
                        if (whsEncd == null || !whsEncd.equals(refundOrders1.getRefWhs())) {
                            refundOrder.setAuditHint("�˻��ֿ����Ϊ��,���߲�һ�£��˿���ʧ�ܣ�");
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
                        rtnGoods.setDelvAddrNm(whsDoc.getWhsAddr());// ������ַ���ֿ��ַ�������˿�ӱ��е��˻��ֿ�����ȡ��
                    }
                    // ���۶����� ���ݶ�����Ų�ѯ���۵���
                    SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(refundOrder.getOrderId());
                    if (sellSngl != null) {
                        rtnGoods.setSellOrdrId(sellSngl.getSellSnglId());// ���۶��������۵���
                    }
                    rtnGoods.setMemo(refundOrder.getMemo());// ��ע��
                    rtnGoods.setIsNtBllg(1);// �Ƿ���Ҫ��Ʊ��

                    List<RtnGoodsSub> rtnGoodsSubs = new ArrayList<RtnGoodsSub>();

                    for (RefundOrders refundOrders1 : refundOrderss) {
                        RtnGoodsSub rtnGoodsSub = new RtnGoodsSub();
                        rtnGoodsSub.setWhsEncd(refundOrders1.getRefWhs());// �ֿ��ţ�
                        // ������� ƽ̨��Ʒ��good_record�е�ƽ̨��Ʒ���룻
                        rtnGoodsSub.setInvtyEncd(refundOrders1.getGoodId());// �������
                        rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());// �˻��������ţ�
                        rtnGoodsSub.setPrdcDt(refundOrders1.getPrdcDt());// ��������
                        rtnGoodsSub.setInvldtnDt(refundOrders1.getInvldtnDt());// ʧЧ����
                        rtnGoodsSub.setMemo(refundOrders1.getMemo());// �ӱ���ϸ��ע
                        Integer refNum2 = refundOrders1.getRefNum();// �˻�����
                        BigDecimal refMoney = refundOrders1.getRefMoney();// �˿��
                        Integer canRefNum2 = refundOrders1.getCanRefNum();// ���˻�����
                        if (refNum2 == 0 && refMoney.compareTo(BigDecimal.ZERO) == 0) {
                            refundOrder.setAuditHint("�˻��������˻�����ͬʱΪ0");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }
                        if (canRefNum2 < refNum2) {// �˻��������ܴ��ڿ��˻�����
                            refundOrder.setAuditHint("�˻��������ܴ��ڿ��˻�����");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }

                        BigDecimal refNum = new BigDecimal(refNum2);


                        // ��Decimal���෴����ֻ��Ҫ����negate������
                        rtnGoodsSub.setQty(refNum.negate());// ���� ����
                        rtnGoodsSub.setUnBllgQty(refNum);//δ��Ʊ����
                        rtnGoodsSub.setBatNum(refundOrders1.getBatchNo());// ����
                        // ��ʵ�����۽�������˰�ʵȼ���һϵ�����ݣ�
                        // �˿�������˿����������˻�������8λС������λ��ʱ���������룻
                       /* BigDecimal refPrice;// �˻����� ��˰����
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
                            refundOrder.setAuditHint("��Ʒ��" + refundOrders1.getGoodId() + "��Ӧ������Ŀ����Ϊ�գ���������");
                            refundOrderDao.update(refundOrder);//���¶����������ʾ��

                            //��־��¼
                            LogRecord logRecord = new LogRecord();
                            logRecord.setOperatId(accNum);
                            //MisUser misUser = misUserDao.select(accNum);
                            if (misUser != null) {
                                logRecord.setOperatName(misUser.getUserName());
                            }
                            logRecord.setOperatContent("���ʧ�ܣ���Ʒ��" + refundOrders1.getGoodId() + "��Ӧ������Ŀ����Ϊ�գ���������");
                            logRecord.setOperatTime(
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            logRecord.setOperatType(2);//2���
                            logRecord.setTypeName("���");
                            logRecord.setOperatOrder(refundOrders.get(i).getEcOrderId());//��������
                            logRecordDao.insert(logRecord);

                            flag = false;
                            break;

                        } else {
                            rtnGoodsSub.setProjEncd(ic.getProjEncd());//������Ŀ����
                        }


                        BigDecimal taxRate = null;
                        if (invtyDocOne == null) {
                        	refundOrder.setAuditHint("���ʧ�ܣ�δ�ҵ���Ӧ���������" + refundOrders1.getGoodId());
                            refundOrderDao.update(refundOrder);//���¶����������ʾ��

                            //��־��¼
                            LogRecord logRecord = new LogRecord();
                            logRecord.setOperatId(accNum);
                            //MisUser misUser = misUserDao.select(accNum);
                            if (misUser != null) {
                                logRecord.setOperatName(misUser.getUserName());
                            }
                            logRecord.setOperatContent("���ʧ�ܣ�δ�ҵ���Ӧ���������" + refundOrders1.getGoodId());
                            logRecord.setOperatTime(
                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            logRecord.setOperatType(2);//2���
                            logRecord.setTypeName("���");
                            logRecord.setOperatOrder(refundOrders.get(i).getEcOrderId());//��������
                            logRecordDao.insert(logRecord);

                            flag = false;
                            break;
                            
                        }else {
                        	taxRate = invtyDocOne.getOptaxRate();// ˰�� ��������е�����˰��
                        }
                        try {
                            rtnGoodsSub.setBaoZhiQi(Integer.parseInt(invtyDocOne.getBaoZhiQiDt()));// ����������
                        } catch (Exception e) {
                            // TODO: handle exception
                            logger.error("URL��ec/refundOrder/add �쳣˵�������" + invtyDocOne.getInvtyEncd() + "������ת���쳣");
                        }
                        if (rtnGoodsSub.getQty().compareTo(BigDecimal.ZERO) > 0) {
                            rtnGoodsSub.setIsNtRtnGoods(1);// �Ƿ��˻�
                        } else {
                            rtnGoodsSub.setIsNtRtnGoods(0);
                        }
                        if (refNum != null && taxRate != null) {
                        	taxRate = taxRate.divide(new BigDecimal(100));
                            BigDecimal noTaxAmt = BigDecimal.ZERO;// ��˰���
                            BigDecimal taxAmt = BigDecimal.ZERO;// ˰��
                            BigDecimal cntnTaxUprc = BigDecimal.ZERO;// ��˰����
                            BigDecimal prcTaxSum = refundOrders1.getRefMoney();// ��˰�ϼ�
                            BigDecimal noTaxUprc = BigDecimal.ZERO;//��˰����
                            if(refNum.compareTo(BigDecimal.ZERO)>0) {
                            	noTaxAmt = prcTaxSum.divide((new BigDecimal(1).add(taxRate)),2,BigDecimal.ROUND_HALF_UP); // ��˰���
                            	taxAmt = prcTaxSum.subtract(noTaxAmt);// ˰��  ��˰���-��˰���
                            	noTaxUprc = noTaxAmt.divide(refNum,8,BigDecimal.ROUND_HALF_UP);//��˰����
                            	cntnTaxUprc = noTaxUprc.multiply((new BigDecimal(1).add(taxRate)));// ��˰����
                            }else {
                            	noTaxAmt = prcTaxSum.divide((new BigDecimal(1).add(taxRate)),2,BigDecimal.ROUND_HALF_UP); // ��˰���
                            	taxAmt = prcTaxSum.subtract(noTaxAmt);// ˰��  ��˰���-��˰���
                            }
                            rtnGoodsSub.setCntnTaxUprc(cntnTaxUprc);// ��˰���ۣ�
                            rtnGoodsSub.setPrcTaxSum(prcTaxSum.negate());// ��˰�ϼƣ�
                            rtnGoodsSub.setNoTaxAmt(noTaxAmt.negate());// ��˰��
                            rtnGoodsSub.setNoTaxUprc(noTaxUprc);// ��˰����
                            rtnGoodsSub.setTaxAmt(taxAmt.negate());// ˰��
                            rtnGoodsSub.setTaxRate(taxRate.multiply(new BigDecimal(100)));// ˰��	
                        
                        
                        } else {
                            refundOrder.setAuditHint(invtyDocOne.getInvtyEncd() + "���˻����˻�������˰�ʲ���Ϊ�գ�����");
                            refundOrderDao.update(refundOrder);
                            flag = false;
                            break;
                        }
//					rtnGoodsSub.setPrdcDt(invtyTab.getPrdcDt());//�������ڣ�
//					rtnGoodsSub.setBaoZhiQi(Integer.valueOf(invtyTab.getBaoZhiQi()));//������
//					rtnGoodsSub.setInvldtnDt(invtyTab.getInvldtnDt());//ʧЧ���ڣ�

                        // InvtyDoc invtyDoc =
                        // invtyDocDao.selectInvtyDocByInvtyDocEncd(refundOrders1.getGoodId());
                        /*
                         * String invtyClsEncd = null; if (invtyDocOne != null) { invtyClsEncd =
                         * invtyDocOne.getInvtyClsEncd();// ���������룻 }
                         */
                        /*
                         * if (invtyClsEncd == null) {
                         *
                         * } else if (invtyClsEncd.startsWith("100")) {
                         * rtnGoodsSub.setIsComplimentary(1);// ����Ʒ�� } else {
                         * rtnGoodsSub.setIsComplimentary(0);// ������Ʒ�� }
                         */
                        rtnGoodsSub.setIsComplimentary(refundOrders1.getIsGift());// �����Ƿ���Ʒ
                        if (refNum.compareTo(BigDecimal.ZERO) > 0) {
                            rtnGoodsSub.setIsNtRtnGoods(1);// �Ƿ��˻� 0�����˻���1���˻�
                        } else {
                            rtnGoodsSub.setIsNtRtnGoods(0);// �Ƿ��˻� 0�����˻���1���˻�
                        }
                        rtnGoodsSubs.add(rtnGoodsSub);// ��ӵ��˻����ӱ���
                    }
                    if (!flag) {
                        continue;
                    }
                    rtnGoods.setFormTypEncd("008");// ��������
                    rtnGoods.setIsNtChk(0);// �˻��������

                    rtnGoods.setDeptId(misUser.getDepId());

                    refundOrder.setIsAudit(1);// �����˿�����
                    refundOrder.setAuditTime(date);// �������ʱ��
                    refundOrder.setAuditUserId(misUser.getAccNum());
                    refundOrder.setAuditUserName(misUser.getUserName());
                    refundOrder.setAuditHint("");
                    int re = refundOrderDao.updateAudit(refundOrder);
                    if (re == 1) {
                        //���³ɹ�ʱ
                        rtnGoodsDao.insertRtnGoods(rtnGoods);// �����˻�������
                        rtnGoodsSubDao.insertRtnGoodsSub(rtnGoodsSubs);// �����˻����ӱ�
                        if (refundOrder.getSource() != 3) {// �˿��Դ������3ʱ��Ҫ�޸�ԭ���Ŀ��˽��Ϳ�������
                            // �˿�����ɺ��޸�ԭ���Ŀ������������˽��
                            refundOrderss = refundOrdersDao.selectList(refundOrder.getRefId());
                            for (RefundOrders refundOrders1 : refundOrderss) {
                                int needRefundCount = refundOrders1.getRefNum();// �����˻�����
                                BigDecimal needRefundMoney = refundOrders1.getRefMoney();// �����˻����
                                List<PlatOrders> platOrderslist = platOrdersDao.selectByEcOrderIdAndInvIdAndBatNum(
                                        refundOrder.getEcOrderId(), refundOrders1.getGoodId(), refundOrders1.getBatchNo());
                                for (int j = 0; j < platOrderslist.size(); j++) {
                                    // ����ÿ����ϸ���˽���������
                                    // ����ǰ����ϸ��������ʱ����ʣ�������ۼ�����һ��
                                    if (needRefundCount > 0) {
                                        if (platOrderslist.get(j).getCanRefNum() >= needRefundCount) {
                                            // ��ǰ����ϸ�������������˻�����ʱ
                                            // ����ԭ������������
                                            platOrderslist.get(j).setCanRefNum(
                                                    platOrderslist.get(j).getCanRefNum() - refundOrders1.getRefNum());
                                            needRefundCount = 0;
                                        } else {
                                            needRefundCount = needRefundCount - platOrderslist.get(j).getCanRefNum();
                                            platOrderslist.get(j).setCanRefNum(0);

                                        }
                                    }
                                    if (needRefundMoney.compareTo(BigDecimal.ZERO) > 0) {
                                        // �����˻�������0
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
                                    // ����ԭ�����˽���������
                                    platOrdersDao.updateCanRefMoneyAndNum(platOrderslist.get(j));
                                }
                            }
                        }
                        count++;
                    }


                    // �����������˻�����

                    /*
                     * //=======================���ɺ������۳��ⵥ=================================== Integer
                     * isRefund = refundOrder.getIsRefund(); //���۵�����ʵ�� SellOutWhs sellOutWhs = new
                     * SellOutWhs(); //���۵��ӱ�ʵ�� List<SellOutWhsSub> sellOutWhsSub = new
                     * ArrayList<>(); //���ʵ�� InvtyTab invtyTab = new InvtyTab(); if(isRefund==1)
                     * {//�Ƿ��˻� ���˻���ʱ�����ɺ������۳��ⵥ�� //���ɺ������۳��ⵥ�� //��ȡ������ String
                     * number=getOrderNo.getSeqNo("CK", userId); if
                     * (sellOutWhsDao.selectSellOutWhsByOutWhsId(number)!=null){//���ظ���?????
                     * message="���"+number+"�Ѵ��ڣ����������룡"; isSuccess=false; }else {
                     * sellOutWhs.setOutWhsId(number); //��������ӦΪ��˻�ʵ�ʳ������ڣ���
                     * sellOutWhs.setOutWhsDt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new
                     * Date())); sellOutWhs.setAccNum(rtnGoods.getAccNum());//�û����
                     * sellOutWhs.setUserName(rtnGoods.getUserName());
                     * sellOutWhs.setCustId(rtnGoods.getCustId());
                     * sellOutWhs.setBizTypId(rtnGoods.getBizTypId());
                     * sellOutWhs.setSellTypId(rtnGoods.getSellTypId());
                     * sellOutWhs.setRecvSendCateId(rtnGoods.getRecvSendCateId());
                     * sellOutWhs.setOutIntoWhsTypId("���۳���");
                     * sellOutWhs.setSellOrdrInd(rtnGoods.getSellOrdrId());//���۵���ʶ
                     * sellOutWhs.setSetupPers(rtnGoods.getSetupPers());//������
                     * sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new
                     * Date()));//����ʱ�� sellOutWhs.setMemo(rtnGoods.getMemo());//��ע��Ϣ
                     * sellOutWhs.setRtnGoodsId(rtnGoods.getRtnGoodsId()); String sellOrdrId =
                     * sellOutWhs.getSellOrdrInd(); for(RtnGoodsSub rGSub:rtnGoodsSubs) {
                     * invtyTab.setWhsEncd(rGSub.getWhsEncd());
                     * invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
                     * invtyTab.setBatNum(rGSub.getBatNum()); invtyTab =
                     * itd.selectInvtyTabsByTerm(invtyTab); invtyTab.setAvalQty(rGSub.getQty());
                     * //�ڿ����н���Ӧ�Ŀ��������٣������˻����е������Ǹ��������Կ�����Ҫ����������
                     * itd.updateInvtyTabAvalQtyJian(invtyTab); SellOutWhsSub sOutWhsSub = new
                     * SellOutWhsSub(); sOutWhsSub.setInvtyEncd(rGSub.getInvtyEncd());//�������
                     * sOutWhsSub.setWhsEncd(rGSub.getWhsEncd());//�ֿ⵵��
                     * sOutWhsSub.setBatNum(rGSub.getBatNum());//����
                     * sOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//���۳��ⵥ�ӱ���������
                     * sOutWhsSub.setQty(rGSub.getQty());//���� ��ȡ���� sowSub.getQty();
                     * sOutWhsSub.setTaxRate((rGSub.getTaxRate()).divide(new BigDecimal(100)));//˰��
                     * // sOutWhsSub.setPrdcDt(rGSub.getPrdcDt());//�������� //
                     * sOutWhsSub.setBaoZhiQi(rGSub.getBaoZhiQi());//������ //
                     * sOutWhsSub.setInvldtnDt(CalcAmt.getDate(sOutWhsSub.getPrdcDt(),
                     * sOutWhsSub.getBaoZhiQi())); BigDecimal noTaxUprc =
                     * sellOutWhsDao.selectSellOutWhsSubByInWhBn(sOutWhsSub); if(noTaxUprc!=null) {
                     * //��ȡ��˰���� sOutWhsSub.setNoTaxUprc(noTaxUprc); } //����δ˰��� ���=δ˰����*δ˰����
                     * sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(),
                     * sOutWhsSub.getQty())); //����˰�� ˰��=δ˰���*˰��
                     * sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.
                     * getQty(), sOutWhsSub.getTaxRate())); //���㺬˰���� ��˰����=��˰����*˰��+��˰����
                     * sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(),
                     * sOutWhsSub.getQty(), sOutWhsSub.getTaxRate())); //�����˰�ϼ�
                     * ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
                     * sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(),
                     * sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
                     * sOutWhsSub.setIsNtRtnGoods(1);//�Ƿ��˻�
                     * sOutWhsSub.setIsComplimentary(rGSub.getIsComplimentary());//�Ƿ���Ʒ
                     * sOutWhsSub.setMemo(rGSub.getMemo());//��ע sellOutWhsSub.add(sOutWhsSub); }
                     * sellOutWhsDao.insertSellOutWhs(sellOutWhs);
                     * sellOutWhsSubDao.insertSellOutWhsSub(sellOutWhsSub); isSuccess=true; message
                     * = "�˻�������˳ɹ���"; } }else {//�����˿��ʱ�����ɺ������۳��ⵥ��
                     *
                     * }
                     */
                    // ====================================================================
                    // �Ƿ���� ��������������ô����

                    /*
                     * refundOrder.setIsAudit(1); refundOrderDao.update(refundOrder);//�����˿�����״̬��
                     */

                }
                if (count > 0) {
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }
                message = "�����ɣ����γɹ�����˿" + count + "����ʧ��" + (refIds.length - count) + "����";
            } else {
                isSuccess = false;
                message = "��ѡ��Ҫ��˵��˿��";
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
                        // �˿��δ���
                        refundOrder.setAuditHint("��ǰ�˿��δ��ˣ���������");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }
                    RtnGoods rtnGoods = rtnGoodsDao.selectRtnGoodsByRefId(refundOrder.getRefId());
                    if (rtnGoods.getIsNtChk() == 1) {
                        // ��Ӧ�˻����Ѿ����
                        refundOrder.setAuditHint("��Ӧ�˻����Ѿ���ˣ����������˻���");
                        refundOrderDao.update(refundOrder);
                        continue;
                    }

                    // �����˿״̬
                    refundOrder.setIsAudit(0);// ����״̬Ϊ�����
                    refundOrder.setAuditHint("");
                    refundOrder.setAuditTime(null);
                    ;
                    refundOrder.setAuditUserId("");
                    refundOrder.setAuditUserName("");

                    // �˿��������ԭ�����˽���������

                    if (refundOrder.getSource() != 3) {// �˿��Դ������3ʱ��Ҫ�޸�ԭ���Ŀ��˽��Ϳ�������
                        // �˿�����ɺ��޸�ԭ���Ŀ������������˽��
                        List<RefundOrders> refundOrderss = refundOrdersDao.selectList(refundOrder.getRefId());
                        for (RefundOrders refundOrders1 : refundOrderss) {

                            int needRefundCount = refundOrders1.getRefNum();// ���˻�����
                            BigDecimal needRefundMoney = refundOrders1.getRefMoney();// ���˻����
                            List<PlatOrders> platOrderslist = platOrdersDao.selectByEcOrderIdAndInvIdAndBatNum(
                                    refundOrder.getEcOrderId(), refundOrders1.getGoodId(), refundOrders1.getBatchNo());
                            for (int j = 0; j < platOrderslist.size(); j++) {
                                if (needRefundCount > 0) {
                                    // ����ÿ����ϸ���˽���������
                                    // ����ǰ����ϸ��������С����Ʒ����ʱ����Ҫ���ӿ�������
                                    if (platOrderslist.get(j).getInvNum() > platOrderslist.get(j).getCanRefNum()) {
                                        if (platOrderslist.get(j).getCanRefNum() + needRefundCount > platOrderslist
                                                .get(j).getInvNum()) {
                                            needRefundCount = needRefundCount + platOrderslist.get(j).getCanRefNum()
                                                    - platOrderslist.get(j).getInvNum();
                                            // ����ԭ������������
                                            platOrderslist.get(j).setCanRefNum(platOrderslist.get(j).getInvNum());// ���ÿ�������Ϊ������ϸ��Ʒ����

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
                                        // ���˽��С��ʵ�����ʱ����Ҫ���ӿ��˽��
                                        // ��ǰ����ϸ���˽�������Ҫ���ӵĽ�����ԭ��ʵ�����ʱ�����Ӳ���
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
                                // ����ԭ�����˽���������
                                platOrdersDao.updateCanRefMoneyAndNum(platOrderslist.get(j));
                            }
                        }
                    }

                    // ɾ����Ӧ�˻���
                    rtnGoodsDao.deleteRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());
                    // �����˿״̬
                    refundOrderDao.update(refundOrder);
                    count++;
                }
                message = "������ɣ����γɹ�����" + count + "���˿��ʧ��" + (refIds.length - count) + "��";
            } else {
                message = "��ѡ����Ҫ������˿";
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
                resp = BaseJson.returnRespObj("ec/aftermarket/noAudit", false, "����ʱ�����쳣��������", null);
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
        // ����
        StoreSettings storeSettings = storeSettingsDao.select(storeId);

        // �жϵ�ǰ����id�����ĸ�ƽ̨
        switch (storeRecord.getEcId()) {
            case "JD":
                if (StringUtils.isEmpty(storeSettings.getVenderId())) {
                    //���������̼�ID����Ϊ�գ�Ϊ��ʱ���ش�����ʾ��Ϣ
                    try {
                        resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "�˿�����ʧ�ܣ�����ƽ̨�����̼�ID����Ϊ�գ����ڵ���������ά����Ӧ�����̼�ID", null);
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
                    resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "��ǰ���̲�֧���˿����", null);
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
            resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "���γɹ������˿" + count + "��", null);
        } catch (Exception e) {
            // TODO: handle exception
            try {
                resp = BaseJson.returnRespObj("ec/refundOrder/download", true, "���ؾ����˿��������", null);
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
            return "ʱ���ʽ����";
        }
        long start = date.getTime();
        try {
            date = simpleDateFormat.parse(endDate);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "ʱ���ʽ����";
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
            return "�������";

        }
        System.out.println(JsonUtil.transferToJson(response));

        if (response.getErrorResponse() != null) {
            System.err.println(response.getErrorResponse().getErrorMsg());
            return response.getErrorResponse().getErrorMsg();
        }
        if (response.getRefundIncrementGetResponse().getTotalCount() == 0) {
            return "û���ۺ�";
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
                // ���˻��Ķ�������Ϣ
                PlatOrder platOrder = platOrderDao.selectByEcOrderId(ecRefId);
                if (platOrder == null) {
                    throw new RuntimeException("û�ж�Ӧ����:" + ecRefId);
                }
//		���Ӧ�����۵� ��
                SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(platOrder.getOrderId());
                if (sellSngl == null) {
                    throw new RuntimeException("û�����۵�:" + platOrder.getOrderId());
                }
                // ��������
                StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
                String refId = getOrderNo.getSeqNo("tk", userId);

                refundOrder.setRefId(refId); // �˿���
                refundOrder.setOrderId(platOrder.getOrderId()); // �������
                refundOrder.setStoreId(platOrder.getStoreId()); // ���̱��
                refundOrder.setStoreName(platOrder.getStoreName()); // ��������
                refundOrder.setEcRefId(item.getId().toString()); // �����˿��
//			refundOrder.setApplyDate(jsonNode.get("created").asText()); // ��������
//			refundOrder.setBuyerId(jsonNode.get("buyer_open_uid").asText()); // ��һ�Ա��
                refundOrder.setIsRefund(item.getAfterSalesType() == 2 ? 1 : 0); // �Ƿ��˻�
                refundOrder.setAllRefNum(Integer.parseInt(item.getGoodsNumber())); // �����˻�����
                refundOrder.setAllRefMoney(new BigDecimal(item.getRefundAmount())); // �����˿���
                refundOrder.setRefReason(item.getAfterSaleReason()); // �˿�ԭ��
//			refundOrder.setRefExplain(jsonNode.get("desc").asText()); // �˿�˵��
                refundOrder.setRefStatus(1); // �˿�״̬
                refundOrder.setDownTime(simpleDateFormat.format(new Date())); // ����ʱ��
//			refundOrder.setTreDate(treDate); // ��������
                refundOrder.setOperator(userId); // ����Ա
                refundOrder.setIsAudit(0); // �Ƿ����
//			refundOrder.setMemo(memo); // ��ע
//		���Ӧ�����۵� ��
                List<SellSnglSub> sellSnglSubs = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSngl.getSellSnglId());
//			// ����sku��ƽ̨��Ʒ�����ѯ������Ʒ����
//			GoodRecord goodRecord = goodRecordDao.selectGoodRecordBySkuAndEcGoodId(jsonNode.get("sku").asText(),
//					jsonNode.get("num_iid").asText());
                // ������Ʒid ���Ƿ��ж�Ӧ�Ĳ�Ʒ�ṹ
                ProdStru prodStru = prodStruMapper.selectMomEncd(item.getOuterGoodsId());
                if (prodStru == null) {
                    for (SellSnglSub sub : sellSnglSubs) {
                        if (sub.getInvtyEncd().equals(item.getOuterGoodsId())) {
                            RefundOrders refundOrders = new RefundOrders();

                            refundOrders.setGoodId(sub.getInvtyEncd()); // ��Ʒ���
                            refundOrders.setGoodName(sub.getInvtyNm()); // ��Ʒ����
                            // refundOrders.setNo(no); // ���
                            refundOrders.setRefId(refId); // �˿���
                            refundOrders.setEcGoodId(item.getGoodsId().toString()); // ƽ̨��Ʒ
                            refundOrders.setGoodSku(item.getSkuId()); // ��Ʒsku
//								refundOrders.setCanRefNum(canRefNum); // ���˻���Ʒ����
//								refundOrders.setCanRefMoney(canRefMoney); // ���˻����
                            refundOrders.setRefNum(Integer.parseInt(item.getGoodsNumber())); // �˻�����
                            refundOrders.setRefMoney(new BigDecimal(item.getRefundAmount())); // �˻����
                            refundOrders.setBatchNo(sub.getBatNum()); // ����
                            refundOrders.setRefWhs(storeRecord.getDefaultRefWhs()); // �˻��ֿ�
//								refundOrders.setMemo(memo); // ��ע
                            RefundOrdersList.add(refundOrders);

                        }
                    }
                } else {
                    // �� �����۵��� �� ��Ʒ�ṹ ѭ�� �Ա�������ĸ�ӱ���

                    List<ProdStruSubTab> prodStruSubTabs = prodStru.getStruSubList();
                    for (SellSnglSub sub : sellSnglSubs) {
                        if (sub.getInvtyEncd().equals(item.getOuterGoodsId())) {

                            for (ProdStruSubTab batchNo : prodStruSubTabs) {

                                RefundOrders refundOrders = new RefundOrders();

                                refundOrders.setGoodId(batchNo.getSubEncd()); // ��Ʒ���
                                refundOrders.setGoodName(batchNo.getSubNm()); // ��Ʒ����
                                // refundOrders.setNo(no); // ���
                                refundOrders.setRefId(refId); // �˿���

                                refundOrders.setEcGoodId(item.getGoodsId().toString()); // ƽ̨��Ʒ
                                refundOrders.setGoodSku(item.getSkuId()); // ��Ʒsku
//						refundOrders.setCanRefNum(canRefNum); // ���˻���Ʒ����
//						refundOrders.setCanRefMoney(canRefMoney); // ���˻����
                                Double bigDecimal = batchNo.getMomQty().doubleValue();
                                int i = bigDecimal.intValue();
                                refundOrders.setRefNum(Integer.parseInt(item.getGoodsNumber()) * i); // �˻�����
                                refundOrders.setRefMoney(new BigDecimal(item.getRefundAmount())); // �˻����
                                refundOrders.setBatchNo(sub.getBatNum()); // ����
                                refundOrders.setRefWhs(storeRecord.getDefaultRefWhs()); // �˻��ֿ�
//						refundOrders.setMemo(memo); // ��ע
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

        return "PDD��������" + m + "��" + resp;
    }

    /**
     * storeSettings ���̵�½���� pageNo ��ʼҳ pageSize һҳ��С startDate ��ʼʱ�� endDate ����ʱ��
     * dataList ������˻�id����
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
        req.setStartModified(simpleDateFormat.parse(startDate));// ��ѯ�޸�ʱ�俪ʼ����ʽ: yyyy-MM-dd HH:mm:ss
        req.setEndModified(simpleDateFormat.parse(endDate));//
        // ת��
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", RefundsReceiveGetRequest.class.getName());
        map.put("taobaoObject", JSON.toJSONString(req));
        String taobao = ECHelper.getTB("", storeSettings, map);
        RefundsReceiveGetResponse rsp = JSONObject.parseObject(taobao, RefundsReceiveGetResponse.class);
        logger.info("��èbody:" + rsp.getBody());
        if (!rsp.isSuccess()) {
            logger.error("��èerr:" + rsp.getSubMsg());
            return;
        }
        for (Refund refund : rsp.getRefunds()) {
            String status = refund.getStatus();
            if (status.equals("WAIT_BUYER_RETURN_GOODS") || status.equals("WAIT_SELLER_CONFIRM_GOODS")
                    || status.equals("SUCCESS")) {
                // WAIT_BUYER_RETURN_GOODS(�����Ѿ�ͬ���˿� �ȴ�����˻�)
                // WAIT_SELLER_CONFIRM_GOODS(����Ѿ��˻����ȴ�����ȷ���ջ�)
                // SUCCESS(�˿�ɹ�)
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
    PlatOrdersDao platOrdersDao; // �����ӱ�

    /**
     * dataList ƽ̨�˻���id����
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
        // ѭ��ƽ̨�˻���id����
        for (String list : dataList) {
            try {
                req.setRefundId(Long.valueOf(list));
                // ת��
                Map<String, String> map = new HashMap<String, String>();
                map.put("path", RefundGetRequest.class.getName());
                map.put("taobaoObject", JSON.toJSONString(req));
                String taobao = ECHelper.getTB("", storeSettings, map);
                RefundGetResponse rsp = JSONObject.parseObject(taobao, RefundGetResponse.class);

                logger.info("��èbody:" + rsp.getBody());
                if (!rsp.isSuccess()) {
                    logger.error("��èerr:" + rsp.getSubMsg());
                    break;
                }

                Refund refund = rsp.getRefund();
                RefundOrder refundOrder = new RefundOrder();
                String ecRefId = refund.getTid().toString();

                List<RefundOrders> RefundOrdersList = new ArrayList<>();
                System.err.println("ecRefId\n" + ecRefId);

                // TODO ����
                List<PlatOrder> platOrderlist = platOrderDao.selectPlatOrdersByEcOrderId(ecRefId);

                if (platOrderlist.size() == 0) {
                    message = "�˻�����Ӧ���۵�������,ƽ̨������:" + ecRefId;
                    continue;
                }
                PlatOrder platOrder = platOrderlist.get(0);
				/*if (platOrder.getIsAudit() == null || platOrder.getIsAudit() == 0) {
					message = "�˻�����Ӧ���۵�δ���,ƽ̨������:" + ecRefId;
					continue;
				}*/

                // ���۵��ӱ�
                List<PlatOrders> platOrdersList = platOrdersDao.selectByEcOrderAndGood(ecRefId, refund.getNumIid().toString(),
                        refund.getSku() == null ? null : refund.getSku().substring(0, refund.getSku().indexOf("|")));
                platOrder.setPlatOrdersList(platOrdersList);

                // ��������
                StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
                String refId = getOrderNo.getSeqNo("tk", userId);
                refundOrder.setExpressCode(refund.getSid()); // ��ݵ���
                refundOrder.setRefId(refId); // �˿���
                refundOrder.setEcId("TM");
                refundOrder.setOrderId(platOrder.getOrderId()); // �������
                refundOrder.setStoreId(platOrder.getStoreId()); // ���̱��
                refundOrder.setEcOrderId(platOrder.getEcOrderId());
                refundOrder.setStoreName(platOrder.getStoreName()); // ��������
                refundOrder.setEcRefId(refund.getRefundId().toString()); // �����˿��
                refundOrder.setApplyDate(allRefNum.format(refund.getCreated())); // ��������
                refundOrder.setBuyerId(refund.getBuyerOpenUid()); // ��һ�Ա��
                refundOrder.setIsRefund(refund.getHasGoodReturn() == true ? 1 : 0); // �Ƿ��˻�
                refundOrder.setAllRefNum(refund.getNum().intValue()); // �����˻�����
                refundOrder.setAllRefMoney(new BigDecimal(refund.getRefundFee())); // �����˿���
                refundOrder.setRefReason(refund.getReason()); // �˿�ԭ��
                refundOrder.setRefExplain(refund.getDesc()); // �˿�˵��
                refundOrder.setRefStatus(1); // �˿�״̬
                refundOrder.setDownTime(allRefNum.format(new Date())); // ����ʱ��
                refundOrder.setTreDate(sdf.format(new Date())); // ��������
                refundOrder.setOperator(userId); // ����Ա
                refundOrder.setIsAudit(0); // �Ƿ����
//			refundOrder.setMemo(memo); // ��ע
                refundOrder.setSource(0);//�˿��Դ����0
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
                        refundOrders.setRefNum(orders.getInvNum()); // �˻�����

                        refundOrders.setRefMoney(orders.getPayMoney()); // �˻����

                    } else {

                        if (ii == platOrdersList.size()) {
                            Integer integers = orders.getInvNum() * orders.getGoodNum() / refund.getNum().intValue();
                            refundOrders.setRefNum(integers); // �˻�����
                            refundOrders.setRefMoney(total); // �˻����
                        } else {
                            Integer integer = orders.getInvNum() * orders.getGoodNum() / refund.getNum().intValue();
                            refundOrders.setRefNum(integer); // �˻�����
                            refundOrders.setRefMoney(orders.getPayPrice().multiply(new BigDecimal(integer))); // �˻����
                            total.subtract(refundOrders.getRefMoney());
                        }

                    }
                    refundOrders.setPrdcDt(orders.getPrdcDt());// ��������
                    refundOrders.setInvldtnDt(orders.getInvldtnDt());// ʧЧ����
                    refundOrders.setGoodId(orders.getInvId());
                    InvtyDoc doc = invtyDocDao.selectAllByInvtyEncd(orders.getInvId());
                    if (doc == null) {
                        refundOrders.setBaozhiqi(Integer.valueOf(doc.getBaoZhiQiDt()));// ����������
                    } else {
                        refundOrders.setBaozhiqi(null);// ����������
                    }
                    refundOrders.setIsGift(orders.getIsGift());// �Ƿ���Ʒ
                    refundOrders.setBatchNo(orders.getBatchNo());// ����
                    refundOrders.setCanRefMoney(orders.getCanRefMoney());// ���˻����
                    refundOrders.setCanRefNum(orders.getCanRefNum());// ���˻�����
                    refundOrders.setGoodName(orders.getGoodName());
                    refundOrders.setRefId(refId); // �˿���
                    if (new BigDecimal(refund.getRefundFee()).compareTo(BigDecimal.ZERO) == 0) {
                        refundOrders.setIsGift(1); // �Ƿ���Ʒ
                    } else {
                        refundOrders.setIsGift(0); // �Ƿ���Ʒ
                    }
                    refundOrders.setGoodSku(orders.getGoodSku()); // ��Ʒsku
                    refundOrders.setEcGoodId(refund.getNumIid().toString()); // ƽ̨��Ʒ

                    if (refund.getOrderStatus().equals("WAIT_SELLER_SEND_GOODS")) {
                        // �ȴ����ҷ���,��:����Ѹ���
                        refundOrders.setRefWhs(orders.getDeliverWhs()); // �˻��ֿ�
                    } else {
                        refundOrders.setRefWhs(storeRecord.getDefaultRefWhs()); // �˻��ֿ�
                    }

                    ordersList.add(refundOrders);

                }

                refundOrder.setRefundOrders(ordersList);
                // �������˻�
//				refundOrder = platOrderSN.checkRefundIsPto(refundOrder);

                // insert
                refundOrderDao.insert(refundOrder);
                refundOrdersDao.insertList(ordersList);

//                BigDecimal he = new BigDecimal(0);
//                List<RefundOrders> refundOrderslist = new ArrayList<RefundOrders>();
//                for (PlatOrders orders : platOrdersList) {
//                    if (refundOrders.getGoodSku().equals(orders.getGoodSku())
//                            && refundOrders.getEcGoodId().equals(orders.getGoodId())) {
//                        // �����
//                        he.add(orders.getPayMoney());
//
//                        RefundOrders order = new RefundOrders();
//                        refundOrders.setGoodId(orders.getInvId());// ��Ʒ���
//                        refundOrders.setGoodName(orders.getPtoName());// ��Ʒ����
//                        refundOrders.setCanRefNum(orders.getCanRefNum());// ���˻�����
//                        refundOrders.setCanRefMoney(orders.getCanRefMoney());// ���˻����
//                        refundOrders.setBatchNo(orders.getBatchNo());// ����
//                        refundOrders.setPrdcDt(orders.getPrdcDt());// ��������
//                        refundOrders.setInvldtnDt(orders.getInvldtnDt());// ʧЧ����
//                        BeanUtils.copyProperties(order, refundOrders);
//                        refundOrderslist.add(order);
//                    }

                m++;
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                logger.error("URL��ec/refundOrder/download �쳣˵����", e);
//                resp += e.getMessage() + "\n";
            }
        }
        message = "���γɹ�������è�˿��" + m + "��";
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
            message = "��ѯ�ɹ�";
            isSuccess = true;
            resp = BaseJson.returnRespList("ec/refundOrder/exportList", isSuccess, message, refundOrderList);
        } catch (Exception e) {
            logger.error("URL��ec/refundOrder/exportList �쳣˵����", e);
        }
        return resp;
    }

    /**
     * �����˿�����
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @param storeId
     * @param ecOrderId
     * @param state     1.������2.���˿�3.���û��޸�4.�������ٲ�5.�����
     * @return
     */
    private String KaoLaDownload(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId,
                                 String ecOrderId, String state, int downloadCount) {
        String resp = "";

        if (state.equals("1") && pageSize == 1) {
            //�����ص�һ������͵�һҳʱ��������0
            downloadCount = 0;
        }
        StoreRecord storeRecord = storeRecordDao.select(storeId);
        try {
            MisUser misUser = misUserDao.select(userId);
            String date = sdf.format(new Date());

            ObjectNode objectNode = JacksonUtil.getObjectNode("");
            objectNode.put("refund_status", state);//1.������2.���˿�3.���û��޸�4.�������ٲ�5.�����
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
            //System.err.println("��ѯ�����"+jdRespJson);
            List<RefundOrder> refundOrderList = new ArrayList();
            List<RefundOrders> refundOrdersList = new ArrayList();
            ArrayNode orderInfoList = (ArrayNode) jdRespJson.get("kaola_refund_search_response").get("refunds");
            // System.out.println(resultJson);
            for (Iterator<JsonNode> orderInfoIterator = orderInfoList.iterator(); orderInfoIterator.hasNext(); ) {
                JsonNode orderInfo = orderInfoIterator.next();
                //�ۺ󶩵���ϸ״̬��1.������2.ͬ���˿�3. �ܾ��˿�,�ȴ��û��޸�4. ͬ���˻�,�ȴ��û�����5. �û����˻����ȴ����
                //6. ��ȷ���ջ�7. �ۺ���ɣ������˿�8. ϵͳ��ʱ�ر�9. �û��ѳ���10. �ۺ���ɣ��˿����
                //11. �ȴ��̼�ȷ��12. �̼Ҿܾ��˻��˿�-�����13. �̼Ҿܾ����˿�-�����14. �ͷ��ر��ۺ�15. �ͷ������ۺ�
                if (orderInfo.get("refund_status_detail").asInt() == 1 || orderInfo.get("refund_status_detail").asInt() == 3
                        || orderInfo.get("refund_status_detail").asInt() == 8 || orderInfo.get("refund_status_detail").asInt() == 9
                        || orderInfo.get("refund_status_detail").asInt() == 12 || orderInfo.get("refund_status_detail").asInt() == 13
                        || orderInfo.get("refund_status_detail").asInt() == 14 || orderInfo.get("refund_status_detail").asInt() == 15) {
                    //�˿״̬�����������˿������
                    continue;
                }
                ecOrderId = orderInfo.get("order_id").asText();
                if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
                    //ԭ������ɾ��,������
                    continue;
                }
                //�ж��˿�Ƿ��Ѵ���
                RefundOrder refundOrder11 = refundOrderDao.selectEcRefId(orderInfo.get("refund_id").asText());
                if (refundOrder11 != null) {
                    continue;
                }
                //�ö�����ȥ��ѯԭ���Ĺ��������Լ�ʵ�����
                List<PlatOrders> platOrders = platOrdersDao.selectByEcOrderId(ecOrderId);
                if (platOrders.size() == 0) {
                    // ��־��¼
                    LogRecord logRecord = new LogRecord();
                    logRecord.setOperatId(userId);
                    if (misUser != null) {
                        logRecord.setOperatName(misUser.getUserName());
                    }
                    logRecord.setOperatContent("�˿���أ�������" + ecOrderId + "δ�ҵ�������ϸ����������ʧ�ܡ�");
                    logRecord.setOperatTime(sdf.format(new Date()));
                    logRecord.setOperatType(12);// 12�˿�����
                    logRecord.setTypeName("�˿����");
                    logRecord.setOperatOrder(ecOrderId);
                    logRecordDao.insert(logRecord);
                } else {
                    boolean flag = true;
                    for (int i = 0; i < platOrders.size(); i++) {
                        //ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
                        if (!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
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
                        logRecord.setOperatTime(sdf.format(new Date()));
                        logRecord.setOperatType(12);// 12�˿�����
                        logRecord.setTypeName("�˿����");
                        logRecord.setOperatOrder(ecOrderId);
                        logRecordDao.insert(logRecord);
                        continue;
                    }
                }


                RefundOrder refundOrder = new RefundOrder();

                String refId = getOrderNo.getSeqNo("tk", userId);
                refundOrder.setRefId(refId);// �˿���
                refundOrder.setStoreId(storeId);// ���̱��
                refundOrder.setEcRefId(orderInfo.get("refund_id").asText());//�����˿��
                refundOrder.setOrderId(ecOrderId);//���̶�����
                refundOrder.setEcOrderId(ecOrderId);//����ƽ̨������
                refundOrder.setSource(0);//�����˿��ԴΪ���������ۺ�
                refundOrder.setSourceNo("");//������Դ���ݺ�Ϊ�����ۺ�ķ��񵥺�
                refundOrder.setOperatorId(misUser.getAccNum());
                refundOrder.setOperatorTime(date);
                refundOrder.setExpressCode(orderInfo.get("express_no").asText());//�û��Ļؿ�ݵ���2019��12��31��19:13:36
                refundOrder.setRefReason(orderInfo.get("refund_reason").asText());//�˿�ԭ��
                refundOrder.setRefExplain(orderInfo.get("refund_reason").asText());//�˿�˵��
                refundOrder.setStoreName(storeRecord.getStoreName());//��������
//				    private String storeName;//��������
                refundOrder.setApplyDate(orderInfo.get("refund_create_time").asText());// ��������
                refundOrder.setBuyerId(orderInfo.get("user_account").asText().equals("null") ? "" : orderInfo.get("user_account").asText());//��һ�Ա��
                refundOrder.setDownTime(date);//��������ʱ��Ϊ��ǰʱ��
                refundOrder.setOperator(misUser.getUserName());
                refundOrder.setIsAudit(0);
                refundOrder.setTreDate(date);//�˿����ʱ����ƽ̨�����ʱ��
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
                            //ֱ��ȡ��ǰ����ϸ�����˿
                            RefundOrders refundOrders = new RefundOrders();
                            refundOrders.setRefId(refId);
                            refundOrders.setGoodSku(StringUtils.isEmpty(platOrders2.get(i).getGoodSku()) ? ""
                                    : platOrders2.get(i).getGoodSku());//��Ʒsku
                            refundOrders.setEcGoodId(platOrders2.get(i).getGoodId());//���õ�����Ʒ����
                            InvtyDoc invtyDoc = invtyDocDao
                                    .selectInvtyDocByInvtyDocEncd(platOrders2.get(i).getInvId());

                            if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
                                refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//���ñ�����
                            }
                            refundOrders.setIsGift(platOrders2.get(i).getIsGift());//�Ƿ���Ʒ��ԭ��������
                            refundOrders.setPrdcDt(platOrders2.get(i).getPrdcDt());//������������
                            refundOrders.setInvldtnDt(platOrders2.get(i).getInvldtnDt());//����ʧЧ����
                            refundOrders.setCanRefNum(platOrders2.get(i).getCanRefNum());//���ÿ�������
                            refundOrders.setCanRefMoney(platOrders2.get(i).getCanRefMoney());//���˽��
                            refundOrders.setRefMoney(new BigDecimal(orderInfo.get("refund_fee").asText()));//�˻����
                            if (orderInfo.get("refund_type").asInt() == 0) {//0�˻��˿�
                                refundOrders.setRefNum(platOrders2.get(i).getInvNum() * node.get("refund_count").asInt() / platOrders2.get(i).getGoodNum());//�����˻�����,�˻�����*�������/��������
                            } else {
                                //���˿�
                                refundOrders.setRefNum(0);//�����˻�����
                            }
                            allReturnCount += refundOrders.getRefNum();
                            allReturnMoney = allReturnMoney.add(refundOrders.getRefMoney());
                            refundOrders.setGoodId(platOrders2.get(i).getInvId());//���ô������

                            refundOrders.setGoodName(invtyDoc.getInvtyNm());//���ô������
                            refundOrders.setBatchNo(platOrders2.get(i).getBatchNo());//�����˻�����
                            refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//ȡ��Ӧ���̵�Ĭ���˻���
                            refundOrdersList.add(refundOrders);//װ��list
                        }
                    }
                }
                refundOrder.setRefStatus(0);//�˿�״̬��ʱ�����ó�0,��֪����ɶ��
                refundOrder.setAllRefNum(allReturnCount);
                refundOrder.setAllRefMoney(allReturnMoney);
                if (refundOrder.getAllRefNum() > 0) {
                    refundOrder.setIsRefund(1);//�˻���������0ʱ�Ƿ��˻�1
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
                // ��־��¼
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent(
                        "�˿���أ����γɹ����ؿ������̣�" + storeRecord.getStoreName() + ",�˿" + downloadCount + "��");
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(12);// 12�˿�����
                logRecord.setTypeName("�˿�����");
                logRecordDao.insert(logRecord);
                resp = BaseJson.returnResp("ec/refundOrder/download ", true,
                        "�˿���أ����γɹ����ؿ������̣�" + storeRecord.getStoreName() + ",�˿" + downloadCount + "��", null);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            try {
                resp = BaseJson.returnResp("ec/refundOrder/download ", false, "�˿���أ�����" + downloadCount + "��������쳣", null);
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
        String message = "��ѯ�ɹ�";
        boolean success = true;
        RefundOrder refundOrder = refundOrderDao.select(refId);
        List<UnionQuery> unionQueries = new ArrayList<>();
        if (refundOrder == null) {
            message = "����ѯ�˿������";
        } else if (refundOrder.getIsAudit() == 0) {
            // ����ѯ������δ��ˣ�û�й�������
            message = "��ǰ�˿δ��ˣ�û�й�������";
        } else {
            // ��ѯ�˿��Ӧ�˻���
            // ��ѯ�˻�����Ӧ���۳��ⵥ
            // ��ѯ��Ӧ���۳��ⵥ
            RtnGoods rtnGoods = refundOrderDao.selectRtnGoodsByRefId(refundOrder.getRefId());

            if (rtnGoods != null) {
                UnionQuery unionQuery = new UnionQuery();
                unionQuery.setAudit("" + rtnGoods.getIsNtChk());
                unionQuery.setOrderId(rtnGoods.getRtnGoodsId());
                unionQuery.setType(1);// ���õ�������Ϊ���۵�
                unionQuery.setOrderName("�˻���");
                unionQueries.add(unionQuery);
                if (rtnGoods.getIsNtChk() == 1) {
                    // �˻�������ˣ��������۳��ⵥ
                    SellOutWhs sellOutWhs = refundOrderDao.selectSellOutWhsByRtnId(rtnGoods.getRtnGoodsId());
                    if (sellOutWhs != null) {
                        UnionQuery unionQuery1 = new UnionQuery();
                        unionQuery1.setAudit("" + sellOutWhs.getIsNtChk());
                        unionQuery1.setOrderId(sellOutWhs.getOutWhsId());
                        unionQuery1.setType(2);// ���õ�������Ϊ���۳��ⵥ
                        unionQuery1.setOrderName("���۳��ⵥ");
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
