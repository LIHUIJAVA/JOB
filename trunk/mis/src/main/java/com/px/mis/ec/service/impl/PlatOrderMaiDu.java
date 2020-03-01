package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.maidu.api.entity.custom.PM_PayAppGetListResponse;
import com.maidu.api.entity.custom.XS_OrdersGetListResponse;
import com.maidu.api.entity.custom.orders.Data;
import com.maidu.api.entity.custom.orders.XS_Invoice;
import com.maidu.api.entity.custom.orders.XS_InvoiceSub;
import com.maidu.api.entity.custom.orders.XS_OrderAddress;
import com.maidu.api.entity.custom.orders.XS_OrderSub;
import com.maidu.api.entity.custom.orders.XS_Orders;
import com.maidu.api.entity.custom.payApp.PM_PayApp;
import com.maidu.api.entity.custom.payApp.PmData;
import com.px.mis.ec.util.SendUtil;
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
import com.px.mis.ec.util.MaiDuSignUtil;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.suning.api.entity.rejected.Rejected;

/**
 * ����-����
 * 
 * @author lxya0
 *
 */
@Component
public class PlatOrderMaiDu {

	@Autowired
	private StoreSettingsDao storeSettingsDao; // ��������
	@Autowired
	private PlatOrderDao platOrderDao; // ��������
	@Autowired
	private StoreRecordDao storeRecordDao; // ���̵���
	@Autowired
	private PlatOrdersDao platOrdersDao; // �����ӱ�
	@Autowired
	private GetOrderNo getOrderNo; // ���ɶ�����
	@Autowired
	private GoodRecordDao goodRecordDao; // ��Ʒ����
	@Autowired
	private InvtyDocDao invtyDocDao; // �������
	@Autowired
	private ProdStruMapper prodStruDao; // ��Ʒ�ṹ
	@Autowired
	private CouponDetailDao couponDetailDao; // �Ż���ϸ
	@Autowired
	private PlatOrderPaymethodDao paymethodDao; // ֧����ϸ
	@Autowired
	private RefundOrderDao refundOrderDao; // �˿����
	@Autowired
	private RefundOrdersDao refundOrdersDao; // �˿�ӱ�
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

	private Logger logger = LoggerFactory.getLogger(PlatOrderSN.class);

	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String message = "";
	boolean isSuccess = true;

	/**
	 * ��������sign
	 */
	public String getSign(String appKey, String appSecret) throws IOException, NoSuchAlgorithmException {

		Map<String, String> params = new LinkedHashMap<>(); // ǩ���ֵ�
		params.put("AppKey", appKey);
		params.put("TimeStamp", sf.format(new Date()));
		params.put("ModuleKeyLogo", "");
		params.put("ActionKeyLogo", "");

		String signMethod = "md5";

		return MaiDuSignUtil.signTopRequest(params, appSecret, signMethod);
	}

	/**
	 * ��������-����
	 * 
	 * @param accNum    ��ǰ�û�
	 * @param pageNo    ��ǰҳ
	 * @param pageSize  ��ǰҳ��
	 * @param startDate ��ʼʱ��
	 * @param endDate   ����ʱ��
	 * @param storeId   ���̱���
	 * @return ������
	 * @throws Exception
	 */
	@Transactional
	public String maiDuDownload(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId,String ecOrderId) throws Exception {

		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		List<XS_Orders> orderQueryList = new ArrayList<>();
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);

		orderQueryList = postMaiDuOrder(storeSettings, pageNo, pageSize, orderQueryList, ecOrderId, startDate, endDate, "2");
		orderQueryList = postMaiDuOrder(storeSettings, pageNo, pageSize, orderQueryList, ecOrderId, startDate, endDate, "3");
		orderQueryList = postMaiDuOrder(storeSettings, pageNo, pageSize, orderQueryList, ecOrderId, startDate, endDate, "4");
		orderQueryList = dealMaiduOrderList(orderQueryList);
		if (orderQueryList.size() > 0) {
			map = getPlatOrder(orderQueryList, storeId, accNum, storeSettings, storeRecord);
			message = (String) map.get("message");
			isSuccess = (boolean) map.get("isSuccess");
		}

		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);

		return resp;

	}
	/**
	 * �򵥴������ȶ���
	 * ��null�ǿշ�AAAAA��BBBBB,����listΪ�����
	 * @param orderQueryList
	 * @return
	 */
	private List<XS_Orders> dealMaiduOrderList(List<XS_Orders> orderQueryList) {
		
		List<XS_Orders> dataList = new ArrayList<>();
		for (XS_Orders xs : orderQueryList) {
			List<XS_OrderSub> subs = new ArrayList<>();
			for (XS_OrderSub sub : xs.getxS_OrderSubs()) {

				if (sub.getMerchantCode() != null && !String.valueOf(sub.getMerchantCode()).equals("")
						&& !String.valueOf(sub.getMerchantCode()).equals("AAAAA")
						&& !String.valueOf(sub.getMerchantCode()).equals("BBBBB")) {

					subs.add(sub);
				}

			}
			if (subs.size() > 0) {
				xs.setxS_OrderSubs(subs);
				dataList.add(xs);
			}
		}
		return dataList;
	}
	
	private XS_OrdersGetListResponse postTransferCodeToMaidu(ExpressCorp expressCorp,String expressCode,List<XS_OrderSub> subs,
			StoreSettings storeSettings) throws Exception {
		XS_OrdersGetListResponse rep = new XS_OrdersGetListResponse();
		if(subs.size() > 0) {
			Map<String, Object> params = new HashMap<>(); // ���ȷ����ش�����
			params.put("AppKey", storeSettings.getAppKey());
			params.put("TimeStamp", sf.format(new Date()));
			params.put("ModuleKeyLogo", "000");
			params.put("ActionKeyLogo", "000");
			params.put("Sign", getSign(storeSettings.getAppKey(), storeSettings.getAppSecret())); // ǩ��
			if (expressCorp.getCompanyCode().equals("JDWL")) {
				params.put("TransferCode", "003"); // ������� jd
			}else {
				params.put("TransferCode", "sf"); // ������� sf˳����
			}
			params.put("ExpressCode", expressCode); // wuliu ��ݵ���
			params.put("OrderSubs", subs); // �Ӷ���������Ϣ
			params.put("FlagMemo", ""); // ������ע
			params.put("ExecutorId", 1);
			params.put("OrderId", subs.get(0).getOrderId()); // ���ȶ���ID
			String fff="";
				fff = SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/SendGood",
						JSON.toJSONString(params));
				System.out.println(fff);
				
			
			/*
			 * try { Thread.currentThread().sleep(2000); } catch (InterruptedException e) {
			 * // TODO Auto-generated catch block //e.printStackTrace(); }
			 */
					rep = JSON.parseObject(fff,XS_OrdersGetListResponse.class);
			/*rep = JSON.parseObject(
					SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/SendGood", 
							JSON.toJSONString(params)),
					XS_OrdersGetListResponse.class);*/
		} else {
			rep.setDone(false);
			rep.setMsg("��ѯ��Ӧƽ̨������ϸΪ�գ����ش�");
		}
		return rep;
	}
	
	public XS_OrdersGetListResponse MaiDuOrderShip1(ExpressCorp expressCorp,LogisticsTab logistics, EcExpress express,
			StoreSettings storeSettings) throws Exception {

		XS_OrdersGetListResponse rep = new XS_OrdersGetListResponse();
		List<XS_Orders> orderQueryList = new ArrayList<>();
		
		orderQueryList = postMaiDuOrder(storeSettings, 1, 999, orderQueryList, logistics.getEcOrderId(), "", "", "");
		
		// 1������ 2������ 3�ѷ���4 �����5 ��ȡ��6 �˿���7 �ѹر�8 ������20 ����ɹ�
		if(orderQueryList.size() > 0) {
			if (orderQueryList.get(0).getOrderStatus()==2) {
				List<XS_OrderSub> subs = new ArrayList<>();
				for (XS_Orders xs : orderQueryList) {

					for (XS_OrderSub sub : xs.getxS_OrderSubs()) {

						if (!String.valueOf(sub.getMerchantCode()).equals("AAAAA")
								&& !String.valueOf(sub.getMerchantCode()).equals("BBBBB")) {
							XS_OrderSub sub1 = new XS_OrderSub();
							sub1.setOrderSubId(sub.getOrderSubId());
							sub1.setQty(sub.getQty());
							sub1.setOrderId(xs.getOrderId());
							subs.add(sub1);
						}
					}
				}
				if (subs.size() > 0) {
					//��ѯ��������
					//TODO 
					List<LogisticsTab> tabList = new ArrayList<>();//��Ҫ��ѯ�����������
					tabList.add(logistics);
					int deliSize = tabList.size();
					if (tabList.size() > 0) {
						if (deliSize == 1) {
							String expressCode = tabList.get(0).getExpressCode();
							rep = postTransferCodeToMaidu(expressCorp,expressCode, subs, storeSettings);
						} else {
							List<XS_OrderSub> subList = new ArrayList<>();
							for (int i = 0; i < deliSize; i++) {
								LogisticsTab delivery = tabList.get(i);
								String expressCode = delivery.getExpressCode();
								if (i <= subs.size()) {
									if (i + 1 == deliSize) {
										if (subs.size() >= i + 1) {
											subList = subs.subList(i, subs.size());
											XS_OrdersGetListResponse reps = postTransferCodeToMaidu(expressCorp,expressCode,
													subList, storeSettings);
											if (!reps.getDone()) {

												rep.setMsg(rep.getMsg() + "," + reps.getMsg());
											}
										}
									} else {
										subList = subs.subList(i, i + 1);
										XS_OrdersGetListResponse reps = postTransferCodeToMaidu(expressCorp,expressCode, subList,
												storeSettings);
										if (!reps.getDone()) {

											rep.setMsg(rep.getMsg() + "," + reps.getMsg());
										}
									}
								}
							}
						}

					}

				} else {
					rep.setDone(false);
					rep.setMsg("��ѯ��Ӧƽ̨������ϸΪ�գ����ش�");
				} 
			}else if(orderQueryList.get(0).getOrderStatus()==3||
					orderQueryList.get(0).getOrderStatus()==4){
					rep.setDone(false);
					rep.setMsg("�����ѷ���������ɣ�ϵͳĬ�Ϸ����ش��ɹ�");
				
			}else {
				if (orderQueryList.get(0).getOrderStatus()==5) {
						//5 ��ȡ��6 �˿���7 �ѹر�
						rep.setDone(false);
						rep.setMsg("������ȡ���������ش�ʧ��");
				}else if(orderQueryList.get(0).getOrderStatus()==6) {
					rep.setDone(false);
					rep.setMsg("�����˿��У������ش�ʧ��");
				}else if(orderQueryList.get(0).getOrderStatus()==7) {
					rep.setDone(false);
					rep.setMsg("�����ѹرգ������ش�ʧ��");
				}else {
					rep.setDone(false);
					rep.setMsg("����״̬�����ڷ���״̬�������ش�ʧ��");
				}
			}
				
			
			
		} else {		
			rep.setDone(false);
			rep.setMsg("δ��ѯ��ƽ̨��Ӧ��������ʹ��ǿ�Ʒ���");
			
		}
		return rep;
	}
	
	
	/**
	 * ����ƽ̨�����ӿ�
	 * 
	 * @param logistics     ��������Ϣ
	 * @param express       ��ݹ�˾��Ϣ
	 * @param storeSettings ��������
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public XS_OrdersGetListResponse MaiDuOrderShip(LogisticsTab logistics, EcExpress express,
			StoreSettings storeSettings) throws NoSuchAlgorithmException, IOException {

		Map<String, Object> params = new HashMap<>(); // ���ȷ����ش�����
		params.put("AppKey", storeSettings.getAppKey());
		params.put("TimeStamp", sf.format(new Date()));
		params.put("ModuleKeyLogo", "000");
		params.put("ActionKeyLogo", "000");
		params.put("Sign", getSign(storeSettings.getAppKey(), storeSettings.getAppSecret())); // ǩ��
		// logistics.getEcOrderId();
		params.put("TransferCode", "003"); // ������� jd
		params.put("ExpressCode", logistics.getExpressCode()); // wuliu ��ݵ���
		
		List<XS_Orders> orderQueryList = new ArrayList<>();
		orderQueryList = postMaiDuOrder(storeSettings, 1, 999, orderQueryList, logistics.getEcOrderId(), "", "", "");
		List<XS_OrderSub> subs = new ArrayList<>();
		//System.out.println("--ord--->"+orderQueryList.size());
		
		for (XS_Orders xs : orderQueryList) {
            //subs = new ArrayList<>();
			params.put("OrderId", xs.getOrderId()); // ���ȶ���ID
			//System.out.println("---subs-->"+xs.getxS_OrderSubs().size());
            for (XS_OrderSub sub : xs.getxS_OrderSubs()) {
            	//System.out.println("--1111-->"+sub.getMerchantCode());
                if (!String.valueOf(sub.getMerchantCode()).equals("AAAAA") &&
                        !String.valueOf(sub.getMerchantCode()).equals("BBBBB")) {
                    XS_OrderSub sub1 = new XS_OrderSub();
                    sub1.setOrderSubId(sub.getOrderSubId());
                    sub1.setQty(sub.getQty());
                    //System.out.println("--1111-->"+sub.getOrderSubId());
                    subs.add(sub1);
                   
                }
               // this.logger.error("��Ʒ����Ʒ������:{}", sub.getMerchantCode());
            }
        }
		
		
		//List<XS_OrderSub> subs = new ArrayList<>();
		//if (orderQueryList.size() > 0) {
			//for (XS_Orders xs : orderQueryList) {
				//subs = xs.getxS_OrderSubs();
			//}
	//	}
		 //List<XS_OrderSub> subList = new ArrayList<>();
		//subList = subs.subList(i, subs.size());
		//System.out.println(subs.get(index));
		params.put("OrderSubs", subs); // �Ӷ���������Ϣ
		params.put("FlagMemo", ""); // ������ע
		params.put("ExecutorId", 1);
		
System.out.println(JSON.toJSONString(params));
		return JSON.parseObject(
				SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/SendGood", 
						JSON.toJSONString(params)),
				XS_OrdersGetListResponse.class);

	}

	/**
	 * post����-������ȡ����
	 * 
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws SuningApiException
	 */
	public List<XS_Orders> postMaiDuOrder(StoreSettings storeSettings, int pageNo, int pageSize,
			List<XS_Orders> orderQueryList, String orderCode, String startDate, String endDate, String orderState)
			throws NoSuchAlgorithmException, IOException {

		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		String sign = getSign(appKey, appSecret); // ��������sign

		// ���ͻ�ȡ��������
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("AppKey", appKey);
		params.put("TimeStamp", sf.format(new Date()));
		params.put("Sign", sign); // ǩ��
		params.put("ModuleKeyLogo", "000");
		params.put("ActionKeyLogo", "000");
		if (orderCode != null) {
			params.put("OrderCode", orderCode);
		} else {
			params.put("OrderCode", ""); // 9569434542 //927057517117410
		}
		params.put("OrderStutas", orderState); // 1������ 2������ 3�ѷ���4 �����5 ��ȡ��6 �˿���7 �ѹر�8 ������20 ����ɹ�
		params.put("BusiId", "10000");
		params.put("CusCode", "");
		params.put("ReviewStatus", "");
		if (StringUtils.isNotEmpty(startDate)) {

			JSONArray array = new JSONArray();
			array.add(startDate);
			array.add(endDate);
			params.put("OrderDates", array);
			// params.put("OrderDates", "[\"" + startDate + "\",\"" + endDate + "\"]");
		} else {
			params.put("OrderDates", "");
		}

		params.put("OrderPayTimes", "");
		params.put("UpdateTimes", "");
		params.put("OutTimes", "");
		params.put("IsSettlement", "");
		params.put("IsJoinAuth", "");
		params.put("IsJoin", "");
		params.put("IsJoinSucess", "");
		params.put("PrmCode", "");
		params.put("PGId", "");
		params.put("Title", "");
		params.put("SkuProperties", "");
		params.put("PageNo", pageNo);
		params.put("PageSize", pageSize);
		params.put("ExecutorId", "1");

		/*
		 * String sss = JSON.toJSONString(params);
		 * System.out.println(JSON.toJSONString(params));
		 * System.out.println(StringEscapeUtils.unescapeJava(JSON.toJSONString(params)))
		 * ; int aa= 0; if(aa==0) { return orderQueryList; }
		 */
		//XS_OrdersGetListResponse result = JSON.parseObject(
				//SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/GetList", JSON.toJSONString(params)),
				//XS_OrdersGetListResponse.class);
		XS_OrdersGetListResponse result = JSON.parseObject(
				SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/GetList", JSON.toJSONString(params)),
				XS_OrdersGetListResponse.class);
		/*
		 * try { Thread.currentThread().sleep(2000); } catch (InterruptedException e) {
		 * // TODO Auto-generated catch block //e.printStackTrace(); }
		 */
		
		if (result.getData().getErrMsg() != null) {

			message = "��������:����ƽ̨���ض����쳣,�쳣��Ϣ:" + result.getData().getErrMsg();
			isSuccess = false;
			logger.error("��������:����ƽ̨���ض����쳣,�쳣��Ϣ:{}", result.getData().getErrMsg());
		}

		if (result.getDone() == true && result.getData().getModels() != null) {

			orderQueryList.addAll(0, result.getData().getModels());

		}
		if (result.getData().getModels() != null && result.getData().getTotalCounts() != 0) {
			if (Integer.valueOf(result.getData().getTotalCounts()) > pageNo * pageSize) {
				postMaiDuOrder(storeSettings, pageNo + 1, pageSize, orderQueryList, null, startDate, endDate,
						orderState);
			}
		} else {
			logger.info("�������أ�����ƽ̨��ѯ�����ɹ�,���β�ѯ����:{}��", result.getData().getTotalCounts());
		}

		return orderQueryList;
	}

	/**
	 * ���������ȡ-����
	 * 
	 * @return
	 * @throws CloneNotSupportedException
	 * @throws IOException
	 * @throws ParseException
	 * @throws NoSuchAlgorithmException
	 */
	@Transactional
	private Map<String, Object> getPlatOrder(List<XS_Orders> orderQueryList, String storeId, String userId,
			StoreSettings storeSettings, StoreRecord storeRecord)
			throws CloneNotSupportedException, IOException, ParseException, NoSuchAlgorithmException {

		boolean isSuccess = true;
		Map<String, Object> map = new HashMap<>();
		int downloadCount = 0;

		if (orderQueryList.size() > 0) {
			for (XS_Orders query : orderQueryList) {

				if (platOrderDao.checkExsits(String.valueOf(query.getOrderCode()), storeId) == 0) {

					PlatOrder order = new PlatOrder();

					List<PlatOrders> ordersList = new ArrayList<>(); // ��Ʒ
					List<InvoiceInfo> invoiceList = new ArrayList<>(); // ��Ʊ
					List<CouponDetail> couponsList = new ArrayList<>(); // �Ż�
					List<PlatOrderPaymethod> payMentList = new ArrayList<>(); // ֧��

					BigDecimal goodMoney = new BigDecimal(0.00);
					BigDecimal adjustMoney = new BigDecimal(0.00);

					String orderId = getOrderNo.getSeqNo("ec", userId);

					order.setOrderId(orderId); // px-�������
					order.setStoreId(storeId);
					order.setStoreName(storeRecord.getStoreName());
					order.setTradeDt(query.getCreateTime()); // �ĵ�ʱ��
					order.setBuyerNote(query.getRemark());// ������ע -���

					XS_OrderAddress address = query.getxS_OrderAddress();

					if (address != null) {
						order.setRecName(address.getAccepter()); // �ջ���
						// map.put("receiver_phone",address.getTelePhone()); //�̶��绰
						order.setRecMobile(address.getMobile()); // �ֻ�����
						// map.put("receiver_zip",address.getPostCode()); //��������
						order.setProvince(address.getProvinceName()); // ʡ����
						order.setCity(address.getCityName()); // ������
						order.setCounty(address.getDistrictName()); // ������
						
						order.setRecAddress(address.getProvinceName()+address.getCityName()+address.getDistrictName()+address.getAddress()); // �ջ���ַ
					}

					// ��ȡ��Ʊ��Ϣ
					if (query.getxS_Invoice() != null) {

						invoiceList = getInvoice(query, storeId, orderId, invoiceList, order);
						order.setIsInvoice(1);
					} else {
						order.setIsInvoice(0);
					}

					order.setEcOrderId(String.valueOf(query.getOrderCode())); // B2C������

					order.setPayTime(query.getPayTime()); // ����֧��ʱ��,��ʽ��yyyy-MM-dd HH:mm:ss
					order.setOrderStatus(query.getOrderStatus()); // ������״̬
					order.setCanMatchActive(0);
					order.setBuyerId(String.valueOf(query.getCusCode())); // ����ʺ�
					order.setSellTypId("1");// ��������������ͨ����
					order.setBizTypId("2");// ����ҵ������2c
					order.setRecvSendCateId("6");//�����շ����
					order.setOrderStatus(0); // ����״̬
					order.setDownloadTime(sf.format(new Date()));
					order.setReturnStatus(0); // �˻�״̬
					order.setIsClose(0); // �Ƿ�ر�
					order.setIsAudit(0); // �Ƿ����
					order.setVenderId("MaiDu"); // '�̼�id'
					order.setFreightPrice(query.getPostage()); // �˷ѡ���������Ŀ��Ӧ���˷�
					order.setDeliverSelf(1); // �Է���
					if(query.getOrderStatus()==2) {
						order.setIsShip(0);//�Ƿ񷢻�
					}else {
						order.setIsShip(1);//�Ƿ񷢻�
					}

					// �ۿ۽�� �Ż�
					if (query.getDisAmt() != null && query.getDisAmt().compareTo(BigDecimal.ZERO) != 0) {
						CouponDetail coup = new CouponDetail();
						coup.setPlatId("MaiDu");
						coup.setStoreId(storeId);
						coup.setOrderId(orderId);
						coup.setCouponPrice(query.getDisAmt());
						couponsList.add(coup);
					}
					List<PM_PayApp> paymentList = selectPmPayAppByOrderId(storeSettings.getAppKey(),
							storeSettings.getAppSecret(), query.getOrderCode());
					if (paymentList.size() > 0) {
						for (PM_PayApp payment : paymentList) {

							PlatOrderPaymethod pay = new PlatOrderPaymethod();

							if (payment.getPayStatus() == 0) {
								pay.setPayStatus("��֧��");
							} else if (payment.getPayStatus() == 1) {
								pay.setPayStatus("֧���ɹ�");
							} else if (payment.getPayStatus() == 2) {
								pay.setPayStatus("������֧��");
							} else if (payment.getPayStatus() == 3) {
								pay.setPayStatus("֧���ر�");
							}

							pay.setOrderId(orderId);
							pay.setPlatId("MaiDu");
							pay.setStoreId(storeId);
							pay.setBanktypecode(String.valueOf(payment.getPayConCode()));

							if (payment.getPayConCode() == 50) {

								pay.setPayWay("֧����֧��");
							} else if (payment.getPayConCode() == 100) {
								pay.setPayWay("΢��֧��");
							} else if (payment.getPayConCode() == 200) {
								pay.setPayWay("�ֽ�֧��");
							} else if (payment.getPayConCode() == 300) {
								pay.setPayWay("��ȯ");
							} else if (payment.getPayConCode() == 400) {
								pay.setPayWay("����");
							}

							pay.setPaymoneyTime(order.getPayTime());
							pay.setPayMoney(new BigDecimal(payment.getRealPayAmt()));

							payMentList.add(pay);

						}
					}

					List<XS_OrderSub> orderDetailList = query.getxS_OrderSubs();
					int goodNum=0;
					if (orderDetailList.size() > 0) {
						for (XS_OrderSub detail : orderDetailList) {
							PlatOrders orders = new PlatOrders();
							orders.setOrderId(orderId);
							orders.setEcOrderId(String.valueOf(query.getOrderCode()));
							BigDecimal pDiscountMoney = new BigDecimal(0.00);
							BigDecimal pAdjustMoney = new BigDecimal(0.00);

							orders.setDeliverWhs(query.getWarehouseCode()); // �ֿ����

							orders.setGoodId(String.valueOf(detail.getItemId())); // ƽ̨��Ʒ���

							orders.setGoodMoney(new BigDecimal(detail.getToalAmt())); // ��Ʒ���

							orders.setGoodName(detail.getTitle()); // ��Ʒ��������
							//orders.setInvId(detail.getMerchantCode());

							orders.setGoodPrice(new BigDecimal(detail.getPrice())); // ��Ʒ����
							orders.setPayPrice(new BigDecimal(detail.getRealPrice())); // ʵ������
							orders.setGoodNum(detail.getQty()); // ����
							goodNum=goodNum+orders.getGoodNum();
							goodMoney = goodMoney
									.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));

							adjustMoney = adjustMoney.add(pAdjustMoney);

							orders.setDiscountMoney(pDiscountMoney); // ϵͳ�Żݽ��
							orders.setAdjustMoney(pAdjustMoney); // �Ż݄����
							ordersList.add(orders);
							
						}
					}
					
					order.setGoodMoney(goodMoney); // Ӧ�����
					order.setAdjustStatus(0);
					order.setAdjustMoney(adjustMoney);
					order.setPayMoney(goodMoney.subtract(adjustMoney)); // ֧����֧����ʽ��֧�����
					order.setGoodNum(goodNum);
					order.setOrderSellerPrice(order.getPayMoney());
					order.setDiscountMoney(order.getGoodMoney().subtract(order.getPayMoney()));
					order.setPlatOrdersList(ordersList);
					// ���㰴�ձ���ƽ������
					ordersList = platOrderPdd.countAveragePrice(ordersList, order.getPayMoney());
					// ����Ƿ�����
					ordersList = platOrderPdd.checkPlatOrdersList(order, ordersList, order.getPayMoney());

					int c = 0;
					if (couponsList.size() > 0) {
						c = couponDetailDao.insert(couponsList);
					}
					int p = 0;
					if (payMentList.size() > 0) {
						p = paymethodDao.insert(payMentList);

					}
					int po = 0;
					if (order != null) {
						downloadCount++;
						platOrderDao.insert(order);
					}
					int pos = 0;
					if (ordersList.size() > 0) {
						pos = platOrdersDao.insert(ordersList);
					}
					if (po > 0 && pos > 0) {
						/*
						 * // ����������ɺ��ж��Ƿ��Զ����� PlatOrder platorder =
						 * platOrderDao.selectByEcOrderId(order.getEcOrderId()); List<PlatOrders>
						 * orderslist = platOrdersDao.select(platorder.getOrderId()); if
						 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
						 * ����trueʱ���˶���ͨ������ֱ�ӽ������
						 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
						 * // ����Ĭ�ϲ���Աsys } isSuccess = true; message = "���سɹ�";
						 * logger.info("��������:����ƽ̨�������سɹ�");
						 */
						// ִ���Զ�ƥ��
						platOrderService.autoMatch(orderId, userId);
						isSuccess = true;
					}

				} else {
					message = "��������:����ƽ̨�����Ѵ���,������:" + query.getOrderCode();
					isSuccess = false;
					// �����Ѵ���
					logger.error("��������:����ƽ̨�����Ѵ���,������:{}", query.getOrderCode());

				}
			}

		} else {

			message = "��������:����ƽ̨���ض�������Ϊ��";
			isSuccess = false;
		}
		// ��־��¼
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(userId);
		MisUser misUser = misUserDao.select(userId);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent("�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + downloadCount + "��");
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
		logRecord.setTypeName("�Զ�����");
		logRecordDao.insert(logRecord);
		if (isSuccess) {
			message = "�����Զ����ص��̣� " + storeSettings.getStoreName() + "����" + downloadCount + "��";
		}
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		return map;
	}

	/**
	 * ��ȡ��Ʊ��Ϣ
	 */
	private List<InvoiceInfo> getInvoice(XS_Orders query, String storeId, String orderId, List<InvoiceInfo> invoiceList,
			PlatOrder order) {
		// ��Ʊ
		XS_Invoice xs_invoice = query.getxS_Invoice(); // ��Ʊ

		InvoiceInfo info = new InvoiceInfo();
		info.setPlatId("MaiDu");
		info.setShopId(storeId);
		info.setOrderId(orderId);
		info.setInvoiceType(String.valueOf(xs_invoice.getInvType()));// ��Ʊ���� -- ��ֵ������ͨ��01��ֵ 02��ͨ 04 ���ӷ�Ʊ��
		info.setInvoiceTitle(xs_invoice.getInvUserName()); // ̧ͷ
		info.setInvoiceContentId(xs_invoice.getInvContent());// ��Ʊ����
		XS_InvoiceSub xs_invoiceSub = xs_invoice.getXS_InvoiceSub(); // ��Ʊ��ϸ
		if (xs_invoiceSub != null) {
			info.setInvoiceConsigneePhone(xs_invoiceSub.getMobile()); // ��Ʊ�ռ����ֻ�
			info.setInvoiceCode(xs_invoiceSub.getTaxpayerCode()); // ��˰��ʶ���
		}

		order.setIsInvoice(1);
		order.setInvoiceTitle(xs_invoice.getInvUserName()); // ̧ͷ
		invoiceList.add(info);
		return invoiceList;
	}

	/**
	 * ����-���ݶ�����ȡ֧����Ϣ
	 */
	private List<PM_PayApp> selectPmPayAppByOrderId(String appKey, String appSecret, long orderId)
			throws IOException, NoSuchAlgorithmException {
		String sign = getSign(appKey, appSecret); // ��������sign

		List<PM_PayApp> pmPayAppList = new ArrayList<>();
		// ���ͻ�ȡ��������
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("AppKey", appKey);
		params.put("TimeStamp", sf.format(new Date()));
		params.put("Sign", sign); // ǩ��
		params.put("ModuleKeyLogo", "000");
		params.put("ActionKeyLogo", "000");

		params.put("OrderId", orderId);
		params.put("Status", "");
		params.put("PayStatus", "");
		params.put("CusCode", "");
		params.put("PageNo", "");
		params.put("PageSize", "");
		params.put("ExecutorId", "1");
		
		PM_PayAppGetListResponse result = JSON.parseObject(
				SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/PM_PayApp/GetList", JSON.toJSONString(params)),
				PM_PayAppGetListResponse.class);
		if (result.getDone()) {
			PmData data = result.getData();
			pmPayAppList = data.getModels();

			logger.info("�������أ�����ƽ̨��ȡ֧����Ϣ�ɹ�,���β�ѯ����:{}��", pmPayAppList.size());
		} else {
			logger.info("�������أ�����ƽ̨��ѯ����֧����Ϣ�쳣:{}", result);
		}

		return pmPayAppList;
	}
}
