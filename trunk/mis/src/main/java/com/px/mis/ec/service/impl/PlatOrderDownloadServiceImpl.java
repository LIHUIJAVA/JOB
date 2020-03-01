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
    private ProdStruMapper prodStruDao;// ��Ʒ�ṹ
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
    private PlatOrderPaymethodDao paymethodDao; //֧����ϸ
    @Autowired
    private GetOrderNo getOrderNo;

    /**
     * �����������ݿ� and �Զ�ƥ��
     * Ŀǰֻ����  JD  TM
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
        //����ǿ�
        if (platOrderList == null || platOrderList.size() == 0) {
        } else {
            int i = 0;
            for (PlatOrder platOrder : platOrderList) {
                //���ɶ�����
//                String orderId = getOrderNo.getSeqNo("ec", userId);
                String orderId = seqNoList.get(i);
                //��ֵ��������
                platOrder.setOrderId(orderId);
                //�õ��������ӱ���
                for (PlatOrders platOrders : platOrder.getPlatOrdersList()) {
                    //��ֵ�ӱ�����
                    platOrders.setOrderId(orderId);
                }
                //���ݿ�����ӱ�
                platOrdersDao.insert(platOrder.getPlatOrdersList());//plat_orders
                i++;
            }
        }


        if (platOrderList == null || platOrderList.size() == 0) {
        } else {
            //���ݿ��������
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
        //����
        downloadCount += platOrderList.size();

        // ����������ɺ��ж��Ƿ��Զ�����
        for (int i = 0; i < platOrderList.size(); i++) {
            //ִ���Զ�ƥ��
            autoMatch(platOrderList.get(i).getOrderId(), userId);
        }


        return downloadCount;
    }

    /**
     * �Զ�ƥ��
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
            if (platOrder.getIsAudit() == 0) {//����δ��˲ſ���ִ���Զ�ƥ��
                int jishu = 0;//����һ���������ѭ��������ϸʧ��ʱ�����ڼ�¼��ǰѭ�����ڼ�����ϸ��

                // ѭ��ƥ����Ʒ����  ���
                for (int j = 0; j < platOrdersList.size(); j++) {
                    jishu = j;//�������ֵ��ǰѭ����
                    DecimalFormat dFormat = new DecimalFormat("0.00");

                    PlatOrders platOrders = platOrdersList.get(j);

                    if (StringUtils.isNotEmpty(platOrders.getInvId())) {
                        // ����ǰ��������ϸ�д�����벻Ϊ����˵��ƥ�䵽��Ʒ�����ˣ�����Ҫִ���Զ�ƥ��
                        orderslist.add(platOrders);
                        continue;
                    }
                    // ������� ƽ̨��Ʒ��good_record�е�ƽ̨��Ʒ���룻
                    String ecgoodid = platOrders.getGoodId();// ƽ̨��Ʒ���
                    String goodsku = platOrders.getGoodSku();
                    GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
                    flag = record(goodRecord, platOrder, platOrders, orderslist, dFormat, flag);
                }

                // ƥ�����  ���
                if (flag && orderslist.size() > 0 && platOrder.getCanMatchActive() == null) {
                    platOrder.setPlatOrdersList(orderslist);
                    int size1 = orderslist.size();
                    platOrder = salesPromotionActivityUtil.matchSalesPromotions(platOrder); //ƥ���������
                    orderslist = platOrder.getPlatOrdersList();
                    int size2 = orderslist.size();
                    platOrderDao.update(platOrder);//��������
                    if (size1 < size2) {
                        // ɾ��ԭ��������ϸ�ӱ�����µ�
                        insertOrders(platOrder, orderslist);

                    }

                }

                // ƥ��ֿ� = �Է��� + ����Ϊ��
                if (flag) {
                    if (platOrder.getDeliverSelf() == 1 && StringUtils.isEmpty(platOrder.getDeliverWhs())) {
                        // ȫ����Ʒƥ����ɺ�ƥ��ֿ� ������Ϊ�Է��������ҷ����ֿ�Ϊ��ʱ��ƥ��
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
                            // ��־��¼

                            flag = false;

                        }

                    }
                }

                // ƥ���ݹ�˾
                if (flag) {
                    // ƥ��ֿ�ɹ���ƥ���ݹ�˾
                    // ���ݶ����еĲֿ�����ƽ̨id�Լ����������ϸ��ƥ���ݹ�˾
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
                                    platOrder.setAuditHint("����ֿ�ƽ̨��ݹ�˾���ö�Ӧģ��");
                                    flag = false;
                                }
                            }
                        } else {
                            flag = false;
                            platOrder.setAuditHint(map3.get("message").toString());
                        }

                    }
                }

                // ������Ʒ�Ƴ�ռ��棨���߲���DB��
                if (flag) {
                    for (int j = 0; j < orderslist.size(); j++) {
                        InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(orderslist.get(j).getInvId());
                        if (invtyDoc.getShdTaxLabour() != null) {
                            if (invtyDoc.getShdTaxLabour() == 1) {//Ϊ1����������Ʒ
                                orderslist.remove(j);
                                j--;
                                if (orderslist.size() == 0) {
                                    break;
                                }
                            } else {
                                //�洢�������γɹ��󷵻�����list�Լ������������list
                                java.util.List<PlatOrders> platOrders123 = new ArrayList<PlatOrders>();
                                for (int z = 0; z < orderslist.size(); z++) {
                                    if (StringUtils.isEmpty(orderslist.get(z).getBatchNo())) {//�����ǰ����ϸ�����ֶ�Ϊ��
                                        //ͬ������ֹ����
                                        synchronized (this) {
                                            // ��֤����������������
                                            Map map4 = checkCanUseNumAndBatch(orderslist.get(z));   //   ռ���   �ɺ���
                                            if (map4.get("flag").toString().equals("true")) {
                                                // �������γɹ�
                                                java.util.List<PlatOrders> platOrders456 = (List<PlatOrders>) map4.get("platOrders");
                                                for (int k = 0; k < platOrders456.size(); k++) {
                                                    platOrders123.add(platOrders456.get(k));
                                                }

                                            } else {
                                                flag = false;//û����ɹ�ʱ������ǰ����ϸ���Ƶ���list
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
                            platOrder.setAuditHint("��������" + invtyDoc.getInvtyEncd() + "�Ƿ�Ӧ˰��������");
                            flag = false;
                            break;
                        }
                    }
                }

                // �ж�������Ʒ�Ƿ�ȫ������ƥ�����
                if (orderslist.size() > 0) {
                    if (platOrdersList.size() == jishu + 1) {
                        //��jishu+1����ԭ������ϸ��sizeʱ,˵������ƥ������Ʒ
                    } else {
                        //������ƥ����Ʒʱ����Ҫ��ʣ�ಿ�ּӵ��б��д���һ�ν���ƥ��
                        for (int j = jishu; j < platOrdersList.size(); j++) {
                            orderslist.add(platOrdersList.get(j));
                        }
                    }
                    // ɾ��ԭ��������ϸ�ӱ�����µ�
                    insertOrders(platOrder, orderslist);
                }

                // ƥ����� --> ��¼��־ --> ���Զ����󣨴��߲���DB�� --> ��Ӧ�ɹ�����
                if (flag) {

                    successCount++;
                    int goodCount = 0;
                    if (orderslist.size() > 0) {
                        for (int j = 0; j < orderslist.size(); j++) {
                            goodCount += orderslist.get(j).getInvNum();
                        }
                        platOrder.setGoodNum(goodCount);// ���¶�����Ʒ����
                    }
                    platOrder.setAuditHint("");// ��������ʾ
                    platOrderDao.update(platOrder);// ���¶����������ʾ��
                    insertLog2(accNum, platOrder);//��¼��־

                    // ����������ɺ��ж��Ƿ��Զ�����  //����  ��������DB����֮��
                    isTrial(platOrder, orderslist);

                } else {
                    //��¼��־
                    InsertLog(accNum, platOrder);
                }

            }

        }

        //��Ӧresp
        try {
            resp = BaseJson.returnRespObj("ec/platOrder/autoMatch", isSuccess,
                    "�����Զ�ƥ��ɹ�" + successCount + "��������ʧ��" + (orderIds.length - successCount) + "������", null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return resp;
    }


    //��¼��־
    private void insertLog2(String accNum, PlatOrder platOrder) {
        // ��־��¼
        LogRecord logRecord = new LogRecord();
        logRecord.setOperatId(accNum);
        MisUser misUser = misUserDao.select(accNum);
        if (misUser != null) {
            logRecord.setOperatName(misUser.getUserName());
        }
        logRecord.setOperatContent("�Զ�ƥ��ɹ�");
        logRecord.setOperatTime(sdf.format(new Date()));
        logRecord.setOperatType(11);// 11�Զ�ƥ��
        logRecord.setTypeName("�Զ�ƥ��");
        logRecord.setOperatOrder(platOrder.getEcOrderId());
        logRecordDao.insert(logRecord);
    }

    //��¼��־
    private void InsertLog(String accNum, PlatOrder platOrder) {
        // ��־��¼
        LogRecord logRecord = new LogRecord();
        logRecord.setOperatId(accNum);
        MisUser misUser = misUserDao.select(accNum);
        if (misUser != null) {
            logRecord.setOperatName(misUser.getUserName());
        }
        logRecord.setOperatContent("�Զ�ƥ��ʧ�ܣ�" + platOrder.getAuditHint());
        logRecord.setOperatTime(sdf.format(new Date()));
        logRecord.setOperatType(11);// 11�Զ�ƥ��
        logRecord.setTypeName("�Զ�ƥ��");
        logRecord.setOperatOrder(platOrder.getEcOrderId());
        logRecordDao.insert(logRecord);
        platOrderDao.update(platOrder);// ���¶����������ʾ��
    }

    //�ж��Ƿ��Զ�����
    private void isTrial(PlatOrder platOrder, List<PlatOrders> orderslist) {
        if (auditStrategyService.autoAuditCheck(platOrder, orderslist)) {
            // ����trueʱ���˶���ͨ������ֱ�ӽ������
            associatedSearchService.orderAuditByOrderId(platOrder.getOrderId(), "sys", sdf.format(new Date()));
            // ����Ĭ�ϲ���Աsys
        }
    }

    //// ɾ��ԭ��������ϸ�ӱ�����µ�
    private void insertOrders(PlatOrder platOrder, List<PlatOrders> orderslist) {
        // ɾ��ԭ��������ϸ�ӱ�
        platOrdersDao.delete(platOrder.getOrderId());
        // �����Զ�ƥ�������Ķ�����ϸ�ӱ�
        platOrdersDao.insert(orderslist);
    }

    /**
     * ��������
     *
     * @param platOrders
     * @return
     */
    @Transactional
    public Map checkCanUseNumAndBatch(PlatOrders platOrders) {
        List<PlatOrders> platOrdersNew = new ArrayList<PlatOrders>();
        String message = "";
        // ��֤������������������Ϣ
        boolean flag = true;

        InvtyTab invtyTab = new InvtyTab();
        invtyTab.setAvalQty(new BigDecimal(platOrders.getInvNum()));// �ӱ�����
        invtyTab.setWhsEncd(platOrders.getDeliverWhs());// ���òֿ����
        invtyTab.setInvtyEncd(platOrders.getInvId());// �������
        List<InvtyTab> tablist = invtyTabService.selectInvtyTabByBatNum(invtyTab);// ��ȡ�����б�
        if (tablist.size() == 0) {
            // ��Ӧ�������������
            flag = false;
            message = platOrders.getInvId() + "����������";
        } else {
            if (tablist.size() == 1) {
                // �����ص�������Ϣֻ��һ��ʱ��ֱ�Ӹ��±��������μ�Ч����Ϣ
                invtyTab = tablist.get(0);
                platOrders.setBatchNo(invtyTab.getBatNum());// ����
                platOrders.setPrdcDt(invtyTab.getPrdcDt() == null ? null : invtyTab.getPrdcDt());// �������ڣ�
                /*
                 * String baoZhiQi = invtyTab.getBaoZhiQi();
                 * if(baoZhiQi!=null||!baoZhiQi.equals("")) {
                 * platOrders.get(i).(Integer.valueOf(baoZhiQi));//������ }
                 */
                platOrders.setInvldtnDt(invtyTab.getInvldtnDt() == null ? null : invtyTab.getInvldtnDt());// ʧЧ���ڣ�
                platOrdersNew.add(platOrders);
            } else {
                // �����ص�������Ϣ��ֹһ��ʱ��ѭ��������ϸ����
                int numCount = platOrders.getInvNum();// ��ǰ����ϸ���������
                int hasNum = 0;// �ѷ�����
                BigDecimal hasMoney = BigDecimal.ZERO;// �ѷ������εĽ��
                for (int j = 0; j < tablist.size(); j++) {
                    // �����µ��ӱ�
                    PlatOrders platOrders2 = new PlatOrders();
                    try {
                        platOrders2 = (PlatOrders) platOrders.clone();
                    } catch (CloneNotSupportedException e) {
                        // TODO Auto-generated catch block
                        flag = false;
                        message = "ƥ�������ʱϵͳ�쳣������ϵ����Ա";
                    }
                    if (j + 1 < tablist.size()) {
                        platOrders2.setBatchNo(tablist.get(j).getBatNum());// ����
                        platOrders2.setPrdcDt(invtyTab.getPrdcDt() == null ? null : invtyTab.getPrdcDt());// ��������
                        /*
                         * if(tablist.get(j).getBaoZhiQi()!=null||!tablist.get(j).getBaoZhiQi().equals(
                         * "")) { sub2.setBaoZhiQi(Integer.valueOf(tablist.get(j).getBaoZhiQi()));//������
                         * }
                         */
                        platOrders2.setInvldtnDt(
                                tablist.get(j).getInvldtnDt() == null ? null : tablist.get(j).getInvldtnDt());// ʧЧ����
                        platOrders2.setInvNum(tablist.get(j).getAvalQty().intValue());// ���õ�ǰ����ϸ������
                        platOrders2.setPayMoney(
                                platOrders2.getPayPrice().multiply(new BigDecimal(platOrders2.getInvNum())));// ����ʵ�����
                        platOrders2.setCanRefMoney(platOrders2.getPayMoney());// ���ÿ��˽��
                        platOrders2.setCanRefNum(platOrders2.getInvNum());// ���ÿ�������
                        hasMoney = hasMoney.add(platOrders2.getPayMoney());// �ۼӼ����� ����ü���
                        hasNum = hasNum + platOrders2.getInvNum();// �ۼ��ѷ�������
                        platOrdersNew.add(platOrders2);
                    } else {
                        // ���һ������ʱ
                        platOrders2.setBatchNo(tablist.get(j).getBatNum());// ����
                        platOrders2.setPrdcDt(invtyTab.getPrdcDt() == null ? null : invtyTab.getPrdcDt());// ��������
                        /*
                         * if(tablist.get(j).getBaoZhiQi()!=null||!tablist.get(j).getBaoZhiQi().equals(
                         * "")) { sub2.setBaoZhiQi(Integer.valueOf(tablist.get(j).getBaoZhiQi()));//������
                         * }
                         */
                        platOrders2.setInvldtnDt(
                                tablist.get(j).getInvldtnDt() == null ? null : tablist.get(j).getInvldtnDt());// ʧЧ����
                        platOrders2.setInvNum(platOrders.getInvNum() - hasNum);// ���õ�ǰ����ϸ������
                        platOrders2.setPayMoney(platOrders.getPayMoney().subtract(hasMoney));// ����ʵ�����
                        platOrders2.setCanRefMoney(platOrders2.getPayMoney());// ���ÿ��˽��
                        platOrders2.setCanRefNum(platOrders2.getInvNum());// ���ÿ�������
                        platOrdersNew.add(platOrders2);
                    }
                }
            }

        }
        Map map = new HashMap<>();
        map.put("flag", flag);
        if (flag) {
            // ��ϸ���������κ󣬿ۼ���Ӧ���εĿ����� public int updateAInvtyTab(List<InvtyTab>
            // invtyTab);//����������(����)
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


    //����
    public Boolean record(GoodRecord goodRecord, PlatOrder platOrder, PlatOrders platOrders, List<PlatOrders> orderslist, DecimalFormat dFormat, Boolean flag) {
        String name = Thread.currentThread().getName();
        System.out.println(name);
        if (goodRecord == null) {
            platOrder.setAuditHint("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");
            platOrderDao.update(platOrder);// ���¶����������ʾ��
            orderslist.add(platOrders);
            flag = false;
            return flag;
        } else if (StringUtils.isEmpty(goodRecord.getGoodId())) {
            platOrder.setAuditHint("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");
            platOrderDao.update(platOrder);// ���¶����������ʾ��
            orderslist.add(platOrders);
            flag = false;
            return flag;
        } else {
            String invId = goodRecord.getGoodId();
            InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// ȥ���������ѯ���������Ϣ
            if (invtyDoc == null) {
                platOrder.setAuditHint("�ڴ��������δƥ�䵽��Ӧ�������,��ƥ��Ĵ�����룺" + invId);
                platOrderDao.update(platOrder);// ���¶����������ʾ��
                orderslist.add(platOrders);
                flag = false;
                return flag;
            } else {
                if (invtyDoc.getPto() != null) {
                    if (invtyDoc.getPto() == 1) {
                        // ����PTO���ʹ��
                        ProdStru prodtru = prodStruDao.selectMomEncd(invId);
                        if (prodtru == null) {
                            // δ�ڲ�Ʒ�ṹ�в�ѯ����Ӧ����Ĳ�Ʒ�ṹ��Ϣ
                            platOrder.setAuditHint("�ڲ�Ʒ�ṹ��" + invId + "��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");
                            platOrderDao.update(platOrder);// ���¶����������ʾ��
                            orderslist.add(platOrders);
                            flag = false;
                            return flag;
                        } else {
                            if (prodtru.getIsNtChk() == 0) {
                                // ��Ӧ��Ʒ�ṹ��δ���
                                platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��" + prodtru.getMomEncd() + "��δ���");
                                platOrderDao.update(platOrder);// ���¶����������ʾ��
                                orderslist.add(platOrders);
                                flag = false;
                                return flag;
                            } else {
                                java.util.List<ProdStruSubTab> strucksublist = prodtru.getStruSubList();
                                // ѭ����Ʒ�ṹ�ӱ���Ϣ
                                if (strucksublist.size() == 0) {
                                    platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��" + prodtru.getMomEncd() + "û�������Ӽ���ϸ���������á�");
                                    platOrderDao.update(platOrder);// ���¶����������ʾ��
                                    orderslist.add(platOrders);
                                    flag = false;
                                    return flag;
                                } else if (strucksublist.size() == 1) {
                                    // ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
                                    // �������
                                    platOrders.setInvId(strucksublist.get(0).getSubEncd());
                                    // ���ô������
                                    platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum()).multiply(strucksublist.get(0).getSubQty()).intValue());
                                    // ���ÿ�������
                                    platOrders.setCanRefNum(platOrders.getInvNum());
                                    platOrders.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
                                    platOrders.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
                                    // ����ʵ������
                                    platOrders.setPayPrice(platOrders.getPayMoney().divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                                    platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
                                    if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
                                        platOrders.setIsGift(1);
                                        platOrder.setHasGift(1);
                                    } else {// ����ʵ�������Ƿ�Ϊ0�ж��Ƿ�Ϊ��Ʒ
                                        platOrders.setIsGift(0);
                                    }
                                    // ���ÿ��˽��,��ʵ�����һ��
                                    platOrders.setCanRefMoney(platOrders.getPayMoney());

                                    orderslist.add(platOrders);
                                } else {
                                    // ����Ʒ�ṹ���Ӽ�����������1ʱ����Ҫ���ɶ�����ϸ

                                    // �����Ӽ��ο��ɱ����Ӽ��������ܳɱ�
                                    // ������꣬���false˵����Ӧ�Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���
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
                                    BigDecimal money123 = new BigDecimal("0");// �����ѷ�ʵ����� ���һ���ü���ʱ�õ�
                                    BigDecimal money456 = new BigDecimal("0");// ���ò�ֺ����Ʒ�����һ������
                                    for (int x = 0; x < strucksublist.size(); x++) {
                                        PlatOrders order = new PlatOrders();
                                        try {
                                            order = (PlatOrders) platOrders.clone();
                                        } catch (CloneNotSupportedException e) {
                                            // TODO Auto-generated catch block
                                            //e.printStackTrace();
                                        }

                                        if (x + 1 < strucksublist.size()) {
                                            // ����ÿ���Ӽ�ռ�ܳɱ��ı��� ����8λС��
                                            InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(strucksublist.get(x).getSubEncd());
                                            BigDecimal rate = invtyDoc2.getRefCost().multiply(strucksublist.get(x).getSubQty()).divide(total, 8, BigDecimal.ROUND_HALF_UP);
                                            order.setInvNum((new BigDecimal(order.getGoodNum())).multiply(strucksublist.get(x).getSubQty()).intValue());
                                            order.setPayMoney(new BigDecimal(dFormat.format(order.getPayMoney().multiply(rate))));// ����ʵ�����
                                            // ������λС��
                                            // ����ʵ������ ����8λС��
                                            order.setPayPrice(order.getPayMoney().divide((new BigDecimal(order.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                                            order.setSellerPrice(order.getPayPrice());
                                            // �������
                                            order.setInvId(strucksublist.get(x).getSubEncd());
                                            // ���ÿ�������
                                            order.setCanRefNum(order.getInvNum());
                                            // ���ÿ��˽��
                                            order.setCanRefMoney(order.getPayMoney());
                                            // ������Ʒ���
                                            order.setGoodMoney(new BigDecimal(dFormat.format(platOrders.getGoodMoney().multiply(rate))));
                                            money456 = money456.add(order.getGoodMoney());
                                            // �����Żݽ��
                                            order.setDiscountMoney(order.getGoodMoney().subtract(order.getPayMoney()));
                                            order.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
                                            order.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
                                            if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
                                                order.setIsGift(1);
                                                platOrder.setHasGift(1);
                                            } else {// �����Ƿ���Ʒ
                                                order.setIsGift(0);
                                            }
                                            orderslist.add(order);
                                            money123 = money123.add(order.getPayMoney());
                                        } else {
                                            order.setInvNum((new BigDecimal(order.getGoodNum())).multiply(strucksublist.get(x).getSubQty()).intValue());
                                            order.setPayMoney(order.getPayMoney().subtract(money123));// ����ʵ�����
                                            // ������λС��
                                            // ����ʵ������ ����8λС��
                                            order.setPayPrice(order.getPayMoney().divide((new BigDecimal(order.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                                            order.setSellerPrice(order.getPayPrice());
                                            // �������
                                            order.setInvId(strucksublist.get(x).getSubEncd());
                                            // ���ÿ�������
                                            order.setCanRefNum(order.getInvNum());
                                            // ���ÿ��˽��
                                            order.setCanRefMoney(order.getPayMoney());
                                            // ������Ʒ���
                                            order.setGoodMoney(platOrders.getGoodMoney().subtract(money456));
                                            // �����Żݽ��
                                            order.setDiscountMoney(order.getGoodMoney().subtract(order.getPayMoney()));
                                            order.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
                                            order.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
                                            if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
                                                order.setIsGift(1);
                                                platOrder.setHasGift(1);
                                            } else {// �����Ƿ���Ʒ
                                                order.setIsGift(0);
                                            }
                                            orderslist.add(order);
                                        }

                                    }
                                }

                            }
                        }
                    } else {
                        // ������PTO���ʹ��
                        // �������
                        platOrders.setInvId(goodRecord.getGoodId());
                        // ���ô������
                        platOrders.setInvNum(platOrders.getGoodNum());
                        // ���ÿ�������
                        platOrders.setCanRefNum(platOrders.getInvNum());
                        platOrders.setPtoCode("");// ���ö�Ӧĸ������
                        platOrders.setPtoName("");// ���ö�Ӧĸ������
                        // ����ʵ������
                        platOrders.setPayPrice(platOrders.getPayMoney().divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
                        platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
                        // �����Żݽ��
                        platOrders.setDiscountMoney(platOrders.getGoodMoney().subtract(platOrders.getPayMoney()));
                        if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
                            platOrders.setIsGift(1);
                            platOrder.setHasGift(1);
                        } else {// ����ʵ�������Ƿ�Ϊ0�ж��Ƿ�Ϊ��Ʒ
                            platOrders.setIsGift(0);
                        }
                        // ���ÿ��˽��,��ʵ�����һ��
                        platOrders.setCanRefMoney(platOrders.getPayMoney());
                        orderslist.add(platOrders);
                    }
                } else {
                    // �������pto����Ϊ��
                    platOrder.setAuditHint("������룺" + invtyDoc.getInvtyEncd() + "�ڴ��������PTO����Ϊ��");
                    platOrderDao.update(platOrder);// ���¶����������ʾ��
                    flag = false;
                    return flag;
                }
            }

        }
        return flag;
    }

}
