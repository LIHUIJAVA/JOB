package com.px.mis.ec.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.*;
import com.px.mis.ec.entity.*;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.*;
import com.px.mis.whs.dao.ExpressCorpMapper;
import com.px.mis.whs.dao.InvtyNumMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.*;
import com.px.mis.whs.service.InvtyTabService;
import com.taobao.api.domain.PromotionDetail;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TradesSoldGetResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


//@Transactional
@Service
public class PlatOrderServiceImpl extends poiTool implements PlatOrderService {

	private Logger logger = LoggerFactory.getLogger(PlatOrderServiceImpl.class);

	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private PlatWhsMappDao PlatWhsMappDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private CouponDetailDao couponDetailDao;
	@Autowired
	private InvoiceDao invoiceDao;
	@Autowired
	private StoreSettingsDao storeSettingsDao;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private GetOrderNo getOrderNo;
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
	private ExpressCorpMapper expressCorpDao;
	@Autowired
	private OrderDealSettingsServiceImpl orderDealSettingsServiceImpl;
	@Autowired
	private InvtyTabService invtyTabService;
	@Autowired
	private InvtyNumMapper invtyNumMapper;
	@Autowired
	private PlatOrderPdd platOrderPdd;
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	private PlatOrderMaiDu platOrderMaiDu;
	@Autowired
	private SalesPromotionActivityUtil salesPromotionActivityUtil;
	@Autowired
	private WhsDocMapper whsDocMapper;
	private List<PlatOrder> platOrderList;
	private List<PlatOrders> platOrdersList;
	@Autowired
	private PlatOrderXMYP platOrderXMYP;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private PlatOrderXHS platOrderXHS;
    @Autowired
    private PlatOrderDownloadServiceImpl platOrderDownloadServiceImpl;

    @Autowired
    private GetOrderNoList getOrderNoList;

	@Override
	public String add(String userId, PlatOrder platOrder, List<PlatOrders> platOrdersList) {
		String resp = "";
		String orderId = "";
		try {
			
			if(platOrderDao.checkExsits1(platOrder.getEcOrderId())==0) {
				orderId = getOrderNo.getSeqNo("ec", userId);
				platOrder.setOrderId(orderId);
				platOrder.setDownloadTime(sdf.format(new Date()));//��������ʱ���������ʱ��
				MisUser misUser = misUserDao.select(userId);
				platOrderDao.insert(platOrder);
				for (PlatOrders platOrders : platOrdersList) {
					platOrders.setOrderId(orderId);
					platOrders.setEcOrderId(platOrder.getEcOrderId());
					platOrders.setDeliverWhs(platOrder.getDeliverWhs());
					platOrders.setExpressCom(platOrder.getExpressCode());
				}
				platOrdersDao.insert(platOrdersList);
				
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(userId);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�����ɹ�");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(1);// 1�ֹ�����
				logRecord.setTypeName("�ֹ���������");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				//�����궩���Զ�ƥ��
				autoMatch(orderId, userId);
			resp = BaseJson.returnRespObjList("ec/platOrder/add", true, "�����ɹ���", platOrder, platOrdersList);
			}else {
				resp = BaseJson.returnRespObjList("ec/platOrder/add", true, "����ʧ�ܣ������Ѵ��ڣ�", platOrder, platOrdersList);
			}

		} catch (Exception e) {
			logger.error("URL��ec/platOrder/add �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}
	@Override
	public String edit(PlatOrder platOrder, List<PlatOrders> platOrdersList,String userId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			PlatOrder platOrder2 = platOrderDao.select(platOrder.getOrderId());
			if(platOrder2.getIsAudit()==0) {
				platOrderDao.update(platOrder);
				List<PlatOrders> platOrders = platOrdersDao.select(platOrder.getOrderId());
				List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>(); 
				for (int j = 0; j < platOrders.size(); j++) {
					if (StringUtils.isNotEmpty(platOrders.get(j).getBatchNo())) {
							InvtyTab invtyTab = new InvtyTab();
							invtyTab.setAvalQty(new BigDecimal(platOrders.get(j).getInvNum()));
							invtyTab.setWhsEncd(platOrders.get(j).getDeliverWhs());
							invtyTab.setBatNum(platOrders.get(j).getBatchNo());
							invtyTab.setInvtyEncd(platOrders.get(j).getInvId());
							invtyTabs.add(invtyTab);
					}
						
				}
				if (invtyTabs.size()>0) {
					for (int j = 0; j < invtyTabs.size(); j++) {
						List<InvtyTab> invtyTabs11 = new ArrayList<InvtyTab>();
						invtyTabs11.add(invtyTabs.get(j));
						invtyNumMapper.updateAInvtyTabJia(invtyTabs11);// ����������(�ӷ�)
					}
				}
				platOrdersDao.delete(platOrder.getOrderId());
				for (int i = 0; i < platOrdersList.size(); i++) {
					platOrdersList.get(i).setBatchNo("");
					platOrdersList.get(i).setInvldtnDt(null);
					platOrdersList.get(i).setPrdcDt(null);//���ԭ������ϸ��������ź�Ч��
					platOrdersList.get(i).setEcOrderId(platOrder.getEcOrderId());
					platOrdersList.get(i).setOrderId(platOrder.getOrderId());
					if(StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
						platOrdersList.get(i).setDeliverWhs(platOrder.getDeliverWhs());
					}
					if(StringUtils.isNotEmpty(platOrder.getExpressCode())) {
						platOrdersList.get(i).setExpressCom(platOrder.getExpressCode());
					}
				}
				platOrdersDao.insert(platOrdersList);
				
				message = "�޸ĳɹ���";
			}else {
				isSuccess = false;
				message = "��������ˣ������޸�";
			}
			
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(userId);
			MisUser misUser = misUserDao.select(userId);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("�����޸ģ�"+message);
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(5);// 5�޸�
			logRecord.setTypeName("�����޸�");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			
			if (isSuccess) {
				//������Զ�ƥ��
				autoMatch(platOrder.getOrderId(), userId);
			}
			resp = BaseJson.returnRespObj("ec/platOrder/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/edit �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String delete(String orderId, String accNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int successcount = 0;
		int fallcount = 0;
		try {
			String orderIds[] = orderId.split(",");
			for (int i = 0; i < orderIds.length; i++) {

				if (platOrderDao.checkExsits1(orderIds[i]) > 0) {
					List<PlatOrder> platOrderList = platOrderDao.selectPlatOrdersByEcOrderId(orderIds[i]);
					boolean flag = true;
					for (int j = 0; j < platOrderList.size(); j++) {
						if (platOrderList.get(j).getIsAudit() == 1) {
							// �����Ѿ���ˣ�����ֱ��ɾ��
							platOrderList.get(j).setAuditHint("����Ϊ���״̬������ֱ��ɾ��");
							platOrderDao.update(platOrderList.get(j));// ���¶����������ʾ��
							fallcount++;
							flag = false;
							break;
						}
					}
					if (flag) {
						List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
						// ���Ӷ�Ӧ������
						for (int j = 0; j < platOrderList.size(); j++) {
							List<PlatOrders> platOrders = platOrdersDao.select(platOrderList.get(j).getOrderId());
							
							for (int k = 0; k < platOrders.size(); k++) {
								if (StringUtils.isNotEmpty(platOrders.get(k).getBatchNo())) {
									InvtyTab invtyTab = new InvtyTab();
									invtyTab.setAvalQty(new BigDecimal(platOrders.get(k).getInvNum()));
									invtyTab.setWhsEncd(platOrders.get(k).getDeliverWhs());
									invtyTab.setBatNum(platOrders.get(k).getBatchNo());
									invtyTab.setInvtyEncd(platOrders.get(k).getInvId());
									invtyTabs.add(invtyTab);
								}

							}
							if (invtyTabs.size()>0) {
								for (int k = 0; k < invtyTabs.size(); k++) {
									List<InvtyTab> invtyTabs123 = new ArrayList<InvtyTab>();
									invtyTabs123.add(invtyTabs.get(k));
									invtyNumMapper.updateAInvtyTabJia(invtyTabs123);// ����������(�ӷ�)
								}
							}
							
						}
					}
					if (flag) {
						platOrderDao.delete(orderIds[i]);// ɾ������
						couponDetailDao.delete(orderIds[i]);// ɾ���Ż���ϸ
						invoiceDao.delete(orderIds[i]);// ɾ����Ʊ��Ϣ
						successcount++;
					}

					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					MisUser misUser = misUserDao.select(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					if (flag) {
						logRecord.setOperatContent("ɾ���ɹ�");
					} else {
						logRecord.setOperatContent("ɾ��ʧ�ܣ�������������ˣ�����ֱ��ɾ��");
					}
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(13);// 13ɾ��
					logRecord.setTypeName("ɾ��");
					logRecord.setOperatOrder(orderIds[i]);
					logRecordDao.insert(logRecord);
				}

			}
			message = "ɾ���ɹ������γɹ�ɾ��" + successcount + "��������ʧ��" + fallcount + "������";
			resp = BaseJson.returnRespObj("ec/platOrder/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/delete �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String query(String orderId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<PlatOrders> proActsList = new ArrayList<>();
			PlatOrder proAct = platOrderDao.select(orderId);
			if (proAct != null) {
				proActsList = platOrdersDao.select(orderId);
				message = "��ѯ�ɹ���";
			} else {
				isSuccess = false;
				message = "���" + orderId + "�����ڣ�";
			}
			resp = BaseJson.returnRespObjList("ec/platOrder/query", isSuccess, message, proAct, proActsList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String resp = "";
		try {
			List<PlatOrder> proList = platOrderDao.selectList(map);
			int count = platOrderDao.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/platOrder/queryList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					proList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/queryList �쳣˵����", e);
		}
		return resp;
	}
	
	@Override
	public String batchSelect(List<String> list,String isAudit) {
		String resp = "";
		try {
			List<PlatOrder> proList = platOrderDao.batchList(list,isAudit);
			
			resp = BaseJson.returnRespList("ec/platOrder/batchSelect", true, "��ѯ�ɹ���", proList.size(), 1, proList.size()+1, 0, 0,
					proList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/batchSelect �쳣˵����", e);
		}
		return resp;
	}

	private static int downloadCount = 0;// ��Ӧ���û������ض�����

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Override
	public String download(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId ) {
		String resp = "";
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		// �жϵ�ǰ����id�����ĸ�ƽ̨
		Map<String, Integer> map =new HashMap<String, Integer>();
		map.put("keys", 0);
		switch (storeRecord.getEcId()) {
		case "JD":
			resp = jdDownload(userId, startDate, endDate, pageNo, pageSize, storeId,downloadCount);
			break;
		case "TM":
			try {
				resp = tmDownloadSdk(map, userId, pageNo, pageSize, startDate, endDate, storeId);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		case "PDD":
			try {
				resp = platOrderPdd.pddDownload(userId,pageNo,pageSize,startDate,endDate,storeId);
				
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			break;
		case "SN":
			try {
				
				resp = platOrderSN.snDownload(userId,pageNo, pageSize, startDate, endDate, storeId);
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			break;
		case "MaiDu":
			try {
				
				resp = platOrderMaiDu.maiDuDownload(userId, pageNo, pageSize, startDate, endDate, storeId,null);
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			break;
		case "KaoLa":
			
			resp = KaoLaDownload(userId, startDate, endDate, pageNo, pageSize, storeId,"1");
			
			break;
		case "XMYP":
			try {
				resp = platOrderXMYP.XMYPDownload(userId, pageNo, pageSize, startDate, endDate, storeId, map);
				} catch (Exception e1) {
				try {
					resp = BaseJson.returnRespObj("ec/platOrder/download", false, "���������쳣��������", null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		case "XHS":
			Map<String, Integer> maps =new HashMap<String, Integer>();
			maps.put("downloadCount",  0);
			try {
				resp = platOrderXHS.xhsDownloadPlatOrderLsit(userId, pageNo, pageSize, startDate, endDate, storeId, "waiting", maps);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				try {
					resp=BaseJson.returnRespObj("ec/platOrder/download", true, "���������쳣��������", null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			break;
		
		default:
			try {
				resp = BaseJson.returnRespObj("ec/platOrder/download", true, "��ǰ���̲�֧�����ض���", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

		return resp;
	}

	/**
	 * ����������������
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	@Override
	public String jdDownloadByOrderId(String userId, Map map) {
		String resp = "";
		String message = "�������!";
		boolean success = true;
		try {
			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("order_id", map.get("orderId").toString());
			// objectNode.put("start_date", map.get("startDate").toString());
			// objectNode.put("end_date", map.get("endDate").toString());
			objectNode.put("order_state", "WAIT_SELLER_STOCK_OUT,WAIT_GOODS_RECEIVE_CONFIRM,FINISHED_L");
			objectNode.put("optional_fields",
					"itemInfoList,couponDetailList,paymentConfirmTime,orderTotalPrice,orderPayment,orderRemark,venderRemark,pin,consigneeInfo,invoiceInfo,invoiceEasyInfo,invoiceConsigneeEmail,invoiceConsigneePhone,balanceUsed,sellerDiscount,orderSellerPrice,freightPrice,orderStartTime,deliveryType,venderId,waybill,orderState");

			String json = objectNode.toString();
			String storeId = map.get("storeId").toString();
//			resp = platOrderService.download(storeId, json, userId);

			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			String jdRespStr = ECHelper.getJD("jingdong.pop.order.get", storeSettings, json);
			//System.out.println(jdRespStr);
			// String jdRespStr = ECHelper.getJD("jingdong.pop.order.search",
			// "0b88dcc2ac604d4fb55fe317f9f5bd94ogrj", json);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			// �ж������Ƿ���ִ����д���ʱֱ�ӷ��ء�
			if (jdRespJson.has("error_response")) {
				message = jdRespJson.get("error_response").get("zh_desc").asText();
				success = false;
				resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
				return resp;
			}
			JsonNode resultJson = jdRespJson.get("jingdong_pop_order_get_responce").get("orderDetailInfo");
			if (resultJson.get("apiResult").get("success").asBoolean()) {
				if (resultJson.has("orderInfo")) {
					List<PlatOrder> platOrderList = new ArrayList();
					List<PlatOrders> platOrdersList = new ArrayList();
					List<InvoiceInfo> invoiceList = new ArrayList();
					List<CouponDetail> couponList = new ArrayList();
					// System.out.println(resultJson);
					// ArrayNode orderInfoList = (ArrayNode) resultJson.get("orderInfo");
					// System.out.println(resultJson);
					// for (Iterator<JsonNode> orderInfoIterator = orderInfoList.iterator();
					// orderInfoIterator.hasNext();) {
					JsonNode orderInfo = resultJson.get("orderInfo");
					String ecOrderId = orderInfo.get("orderId").asText();
					// ���ж϶������Ƿ��Ѿ�����
					// //System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
					if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
						PlatOrder platOrder = new PlatOrder();
						int goodNum = 0;
						String orderId = getOrderNo.getSeqNo("ec", userId);

						// ������ϸlist
						ArrayNode itemInfoList = (ArrayNode) orderInfo.get("itemInfoList");
						// �����ܽ��
						/*
						 * BigDecimal moneySum = new BigDecimal(0); for (Iterator<JsonNode>
						 * itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
						 * .hasNext();) { JsonNode itemInfo = itemInfoInfoIterator.next();
						 * //��������������������ܽ�� BigDecimal goodPrice = new
						 * BigDecimal(itemInfo.get("jdPrice").asDouble()*itemInfo.get("itemTotal").asInt
						 * ()); moneySum.add(goodPrice); }
						 */
						// �����Ż���ϸ
						List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
						if (orderInfo.get("sellerDiscount").asDouble() != 0) {
							ArrayNode couponIterator = (ArrayNode) orderInfo.get("couponDetailList");
							if (couponIterator != null) {
								for (Iterator<JsonNode> it = couponIterator.iterator(); it.hasNext();) {
									JsonNode coupons = it.next();
									// //System.err.println("_________�Ż���ϸ��" + coupons.toString());
									CouponDetail coupon = JacksonUtil.getPOJO((ObjectNode) coupons, CouponDetail.class);
									coupon.setStoreId(storeId);
									coupon.setCouponCode(
											coupon.getCouponType().substring(0, coupon.getCouponType().indexOf('-')));
									coupon.setPlatId("JD");
									coupon.setOrderId(ecOrderId);
									couponList.add(coupon);
									couponDetails.add(coupon);
								}
							}
						}

						List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
						platOrder.setEcOrderId(orderInfo.get("orderId").asText());// ���̶�����
						for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
								.hasNext();) {
							JsonNode itemInfo = itemInfoInfoIterator.next();
							PlatOrders platOrders = new PlatOrders();
							platOrders.setGoodId(itemInfo.get("wareId").asText());// ������Ʒ����
							platOrders.setGoodNum(itemInfo.get("itemTotal").asInt());// ��Ʒ����
							platOrders.setGoodName(itemInfo.get("skuName").asText());// ƽ̨��Ʒ����
							platOrders.setGoodPrice(new BigDecimal(itemInfo.get("jdPrice").asDouble()));// ��Ʒ���� ������
							// platOrders.setPayMoney(new
							// BigDecimal(itemInfo.get("productNo").asDouble()));// ʵ�����
							platOrders.setGoodSku(itemInfo.get("skuId").asText());// ��Ʒsku
							platOrders.setOrderId(orderId);
							platOrders.setEcOrderId(platOrder.getEcOrderId());
							platOrders.setGoodMoney(
									platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// ��Ʒ���
							platOrders.setDeliverWhs(itemInfo.get("newStoreId").asText());// ������ϸ�еķ�����id
							platOrders.setPayMoney(platOrders.getGoodMoney());
							if (platOrders.getDeliverWhs().equals("0")) {
								platOrders.setDeliverWhs("");
							} else {
								Map map1 = new HashMap();
								map1.put("type", "JD");
								map1.put("online", platOrders.getDeliverWhs());
								//System.out.println("��ϸ�ֿ��Ӧonline��" + platOrder.getStoreId());
								String newWhs = PlatWhsMappDao.select(map1);
								//System.out.println("��ϸ�ֿ��Ӧ��" + newWhs);
								if (newWhs != null) {
									platOrders.setDeliverWhs(newWhs);
								}else {
									platOrder.setAuditHint("ƽ̨�ַ�������ȱ��ƽ̨��ӳ��,����ƽ�ֱ��룺"+platOrders.getDeliverWhs());
								}
							}
							platOrdersList1.add(platOrders);

							platOrder.setHasGift(0);// ���Ƿ���Ʒ��0������1��
							goodNum += itemInfo.get("itemTotal").asInt();
						}
						StoreRecord storeRecord = storeRecordDao.select(storeId);
						////System.err.println("storeID::" + storeId);
						//�ж϶���״̬�Ƿ񷢻�
						if(orderInfo.get("orderState").asText().equals("WAIT_SELLER_STOCK_OUT")) {
							//�ȴ�����
							platOrder.setIsShip(0);// ���Ƿ񷢻�
						}else {
							platOrder.setIsShip(1);// ���Ƿ񷢻�
							if(orderInfo.has("waybill")){
								platOrder.setExpressNo(orderInfo.get("waybill").asText());
							}
							
						}
						platOrder.setOrderId(orderId);// �������
						platOrder.setStoreId(storeId);// ���̱��
						platOrder.setStoreName(storeRecord.getStoreName());// ��������
						if (orderInfo.has("paymentConfirmTime")) {
							//�ж϶����Ƿ���ڸ���ʱ�䣬���������û�и���ʱ��
							platOrder.setPayTime(orderInfo.get("paymentConfirmTime").asText());// ����ʱ��
						}
						platOrder.setTradeDt(orderInfo.get("orderStartTime").asText());// �µ�ʱ��
						platOrder.setIsAudit(0);// ���Ƿ���ˣ�0δ��ˣ�1���
						platOrder.setGoodNum(goodNum);// ��Ʒ������
						platOrder.setGoodMoney(new BigDecimal(orderInfo.get("orderTotalPrice").asDouble()));// �����ܽ��
						platOrder.setPayMoney(new BigDecimal(orderInfo.get("orderPayment").asDouble()));// ���ʵ�ʸ�����
						platOrder.setBuyerNote(orderInfo.get("orderRemark").asText());// ��ұ�ע
						platOrder.setSellerNote(orderInfo.get("venderRemark").asText());// �̼ұ�ע
						platOrder.setDeliveryType(orderInfo.get("deliveryType").asText());// �����ͻ�����
						platOrder.setVenderId(orderInfo.get("venderId").asText());// �����̼�ID�̼ұ���
						platOrder.setBuyerId(orderInfo.get("pin").asText());// ��һ�Ա��
						platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("orderSellerPrice").asDouble()));// ������
						JsonNode consigneeInfo = orderInfo.get("consigneeInfo");
						platOrder.setRecAddress(consigneeInfo.get("fullAddress").asText());// ���ջ�����ϸ��ַ
						platOrder.setRecName(consigneeInfo.get("fullname").asText());// ���ջ�������
						platOrder.setRecMobile(consigneeInfo.get("mobile").asText());// ���ջ����ֻ���
						platOrder.setProvince(consigneeInfo.get("province").asText());// �ջ���ʡ
						platOrder.setProvinceId(consigneeInfo.get("provinceId").asText());// �ջ���ʡid
						platOrder.setCity(consigneeInfo.get("city").asText());// �ջ�����
						platOrder.setCityId(consigneeInfo.get("cityId").asText());// �ջ�����id
						platOrder.setCounty(consigneeInfo.get("county").asText());// �ջ�������
						platOrder.setCountyId(consigneeInfo.get("countyId").asText());// �ջ�������id
						platOrder.setDeliverWhs(orderInfo.get("storeId").asText());
						platOrder.setSellTypId("1");// ��������������ͨ����
						platOrder.setBizTypId("2");// ����ҵ������2c
						platOrder.setRecvSendCateId("6");// �����շ����
						if (platOrder.getDeliverWhs().equals("0")) {
							// �����ֿ����Ϊ0��˵���˶������Է�������
							platOrder.setDeliverWhs("");
							platOrder.setDeliverSelf(1);//���ô˶���Ϊ�Է�������
						} else {
							Map map2 = new HashMap();
							map2.put("type", "JD");
							map2.put("online", platOrder.getDeliverWhs());
							String newWhs1 = PlatWhsMappDao.select(map2);
							// System.out.println("����ֿ��Ӧ��" + newWhs1);
							if (newWhs1 != null) {
								platOrder.setDeliverWhs(newWhs1);
							} else {
								platOrder.setDeliverWhs("");
							}
							platOrder.setDeliverSelf(0);//���ô˶���Ϊƽ̨�ַ�������
						}
						if (consigneeInfo.has("town")) {
							platOrder.setTown(consigneeInfo.get("town").asText());
							platOrder.setTownId(consigneeInfo.get("townId").asText());
						}

						if (orderInfo.get("invoiceInfo").asText().equals("����Ҫ���߷�Ʊ")) {
							platOrder.setIsInvoice(0);// ���Ƿ�Ʊ
						} else {
							platOrder.setIsInvoice(1);// ���Ƿ�Ʊ
							InvoiceInfo invoiceInfo = new InvoiceInfo();
							JsonNode invoice = orderInfo.get("invoiceEasyInfo");
							// //System.err.println("__________��Ʊ��Ϣ��" + invoice.toString());
							invoiceInfo = JacksonUtil.getPOJO((ObjectNode) invoice, InvoiceInfo.class);
							invoiceInfo.setOrderId(platOrder.getEcOrderId());
							invoiceInfo.setPlatId("JD");
							invoiceInfo.setShopId(storeId);
							platOrder.setInvoiceTitle(orderInfo.get("invoiceEasyInfo").get("invoiceTitle").asText());// ��Ʊ̧ͷ
							invoiceList.add(invoiceInfo);
						}
						/* ͨ��storeOrder�Ƿ���ھ��ֶ����ж��Ƿ��Է�������ȷ��һ�̻��������ھ��������������ֶ�ֵ�����ھ��ֶ���2019��12��9��
						 * if (orderInfo.get("storeOrder").asText().equals("���ֶ���")) {
						 * 
						 * platOrder.setDeliverSelf(0);// ���ö���Ϊƽ̨�ַ������� } else {
						 * platOrder.setDeliverSelf(1);// ���ö���Ϊ�Է������� }
						 */

						platOrder.setFreightPrice(new BigDecimal(orderInfo.get("freightPrice").asDouble()));// �˷�
						platOrder.setNoteFlag(0);// ���̼ұ�ע���ģ�Ĭ��ֵ����ʾ������
						platOrder.setIsClose(0);// ���Ƿ�ر�
						// platOrder.setIsShip(0);// ���Ƿ񷢻�
						// platOrder.setAdjustMoney(new
						// BigDecimal(orderInfo.get("balanceUsed").asDouble()));// �̼ҵ������
						platOrder.setAdjustMoney(new BigDecimal(0));
						platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("sellerDiscount").asDouble()));// �Żݽ��
						// platOrder.setGoodPrice(new BigDecimal(0L));// ��Ʒ����
						// platOrder.setPayPrice(new BigDecimal(0L));// ʵ������

						platOrder.setOrderStatus(0);// ������״̬
						platOrder.setReturnStatus(0);// ���˻�״̬ ���ö���δ�����˻�
						platOrder.setDownloadTime(sdf.format(new Date()));// ��������ʱ��
						platOrdersList.addAll(checkInvty(platOrder, platOrdersList1, couponDetails));

						/*platOrder.setPlatOrdersList(platOrdersList);
						platOrder=activityUtil.matchSalesPromotions(platOrder);
						platOrdersList=platOrder.getPlatOrdersList();*/
						
						/*if (StringUtils.isEmpty(platOrder.getAuditHint())) {
							if (platOrder.getDeliverSelf() == 1) {
								platOrder.setPlatOrdersList(platOrdersList);
								Map map2 = orderDealSettingsServiceImpl.matchWareHouse(platOrder,
										storeRecord.getEcId());
								if (map2.get("isSuccess").toString().equals("true")) {
									platOrder.setDeliverWhs(map2.get("whsCode").toString());
									for (int i = 0; i < platOrdersList.size(); i++) {
										platOrdersList.get(i).setDeliverWhs(map2.get("whsCode").toString());
									}
									// ƥ��ֿ�ɹ���ƥ���ݹ�˾
									Map map3 = logisticsTabServiceImpl.findExpressCodeByOrderId(platOrder,
											platOrdersList);
									if (map3.get("flag").toString().equals("true")) {
										platOrder.setExpressCode(map3.get("expressCode").toString());
										platOrder.setExpressTemplate(map3.get("templateId").toString());
									} else {
										platOrder.setAuditHint(map3.get("message").toString());
									}

								} else {
									platOrder.setAuditHint(map2.get("message").toString());
								}
							} 
						}*/
						/*if (StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
							// ƥ��ֿ�ɹ���������μ��۳�������
							int aa = 0;
							int bb = 0;
							for (int j = 0; j < platOrdersList.size(); j++) {
								if (StringUtils.isNotEmpty(platOrdersList.get(j).getBatchNo())) {
									aa++;
								}
								if (StringUtils.isEmpty(platOrdersList.get(j).getInvId())) {
									bb++;
								}
							}
							if (aa != platOrdersList.size() && bb == 0) {
								// aa��С��������ϸ�б�sizeʱ��˵����ϸ����δ�ۼ���������������
								Map map4 = checkCanUseNumAndBatch(platOrdersList);
								if (map4.get("flag").toString().equals("true")) {
									// �������γɹ�
									platOrdersList.clear();
									platOrdersList.addAll((List<PlatOrders>) map4.get("platOrders"));
								} else {
									platOrder.setAuditHint(map4.get("message").toString());
									// ��־��¼
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(userId);
									MisUser misUser = misUserDao.select(userId);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("�Զ�ƥ��ʧ�ܣ�" + platOrder.getAuditHint());
									logRecord.setOperatTime(sdf.format(new Date()));
									logRecord.setOperatType(11);// 11�Զ�ƥ��
									logRecord.setTypeName("�Զ�ƥ��");
									logRecord.setOperatOrder(platOrder.getEcOrderId());
									logRecordDao.insert(logRecord);
								}
							}
						}*/
						/*boolean ff = true;// ������꣬�ж϶�����ϸ���Ƿ����δƥ�䵽�����������ݡ�
						BigDecimal allCount = new BigDecimal("0");// ������Ʒ����
						for (int j = 0; j < platOrdersList.size(); j++) {
							if (StringUtils.isEmpty(platOrdersList.get(j).getInvId())) {
								ff = false;
								break;
							} else {
								allCount = allCount.add(new BigDecimal(platOrdersList.get(j).getInvNum()));
							}
						}
						if (ff) {
							platOrder.setGoodNum(allCount.intValue());
						}*/
						platOrderList.add(platOrder);
					}
					// }
					if (platOrderList.size() > 0) {
						platOrderDao.insertList(platOrderList);
						platOrdersDao.insert(platOrdersList);
					}
					if (invoiceList.size() > 0) {
						invoiceDao.insert(invoiceList);
					}
					if (couponList.size() > 0) {
						couponDetailDao.insert(couponList);
					}
					
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					MisUser misUser = misUserDao.select(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(
							"�������ض���" + platOrderList.size() + "��");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(9);// 9�ֹ����� 10 �Զ�����
					logRecord.setTypeName("�ֹ�����");
					logRecord.setOperatOrder(map.get("orderId").toString());
					logRecordDao.insert(logRecord);
					
					
					for (int i = 0; i < platOrderList.size(); i++) {
						//ִ���Զ�ƥ��
						autoMatch(platOrderList.get(i).getOrderId(), userId);
					}
					
					
				} else {
					success = false;
					message = "�����ض��������ڵ�ǰ���̻򳬳�ϵͳ��������ʱ�䷶Χ";
				}
			} else {
				success = false;
				message = "������ʾ: " + resultJson.get("apiResult").get("chineseErrCode").asText();
			}
			resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
			// resp=resultJson.toString();
		} catch (Exception e) {
			logger.error("URL��ec/platOrder/download �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}
	//matchSalesPromotions
	@Autowired
	SalesPromotionActivityUtil activityUtil;
	// ���̱�� storeId
	private String tmdownloadByOrderId(String tid, String userId, String storeId)
			throws NoSuchAlgorithmException, IOException, CloneNotSupportedException {

		String resp = "";
		String message = "";
		try {
			// ����
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			// ��Ϣ
			StoreRecord storeRecord = storeRecordDao.select(storeId);

			Map<String, String> maps = new HashMap<String, String>();
			maps.put("fields",
					"logistics_company,invoice_name,tid,type,status,payment,est_con_time,orders,promotion_details,pay_time,total_fee,buyer_message,seller_memo,receiver_address,receiver_name,receiver_mobile,seller_flag,adjust_fee,discount_fee,created,received_payment,receiver_state,post_fee,receiver_city,receiver_district,num");
			maps.put("tid", tid);
			String tmResps = ECHelper.getTB("taobao.trade.fullinfo.get.customization", storeSettings, maps);

			JSONObject tmRespJson = JSONObject.parseObject(tmResps);
			if (tmRespJson.containsKey("error_response")) {
				return BaseJson.returnRespObj("ec/platOrder/download", false,
						tmRespJson.getJSONObject("error_response").getString("msg"), null);
			}
			JSONObject resultJsons = tmRespJson.getJSONObject("trade_fullinfo_get_customization_response")
					.getJSONObject("trade");

			PlatOrder platOrder = new PlatOrder();

			System.err.println(resultJsons);

			String orderId = getOrderNo.getSeqNo("ec", userId);

			platOrder.setEcOrderId(resultJsons.getString("tid"));// ���̶�����
			platOrder.setOrderId(orderId); // �������
			platOrder.setStoreId(storeId); // ���̱��
			platOrder.setStoreName(storeRecord.getStoreName()); // '��������' 1
			platOrder.setPayTime(resultJsons.getString("pay_time")); // ����ʱ��' t paytime
//					platOrder.setWaif(waif);		//	'���' 			
			platOrder.setIsAudit(0); // '�Ƿ����'
//					platOrder.setAuditHint(auditHint); // '�����ʾ'

			platOrder.setGoodMoney(resultJsons.getBigDecimal("total_fee")); // ��Ʒ���' t totalfee
			platOrder.setPayMoney(resultJsons.getBigDecimal("payment")); // 'ʵ�����' t payment
			platOrder.setBuyerNote(resultJsons.getString("buyer_message")); // �������' t buyermessage
			platOrder.setSellerNote(resultJsons.getString("seller_memo")); // ���ұ�ע'

			platOrder.setRecAddress(resultJsons.getString("receiver_address")); // �ջ�����ϸ��ַ' t receiveraddress
//					platOrder.setBuyerId(buyerId); // '��һ�Ա��'
			platOrder.setRecName(resultJsons.getString("receiver_name")); // �ջ�������' t receivername
			platOrder.setRecMobile(resultJsons.getString("receiver_mobile")); // �ջ����ֻ���' t receivermobile
			if (resultJsons.containsKey("invoice_name")) {
				platOrder.setIsInvoice(1); // '�Ƿ�Ʊ'
				platOrder.setInvoiceTitle(resultJsons.getString("invoice_name")); // '��Ʊ̧ͷ'
			}
			platOrder.setNoteFlag(resultJsons.getInteger("seller_flag")); // ���ұ�ע����' t sellerflag
			platOrder.setIsClose(0); // '�Ƿ�ر�'
			if (resultJsons.getString("status").equals("WAIT_SELLER_SEND_GOODS")) {
				platOrder.setIsShip(0); // '�Ƿ񷢻�'
			} else {
				platOrder.setIsShip(1); // '�Ƿ񷢻�'
			}
			platOrder.setAdjustMoney(resultJsons.getBigDecimal("adjust_fee")); // ���ҵ������' t
																				// adjustfee
			platOrder.setDiscountMoney(resultJsons.getBigDecimal("discount_fee")); // ϵͳ�Żݽ��' t
																					// discountfee
			platOrder.setOrderStatus(0); // '����״̬'
			platOrder.setReturnStatus(0); // '�˻�״̬'
//					platOrder.setHasGift(0); // '�Ƿ���Ʒ'
//					platOrder.setMemo(memo); // '��ע'
//					platOrder.setAdjustStatus(adjustStatus); // '������ʶ'
			platOrder.setTradeDt(resultJsons.getString("created")); // '����ʱ��' t created
			platOrder.setSellTypId("1");// ��������������ͨ����
			platOrder.setBizTypId("2");// ����ҵ������2c
			platOrder.setRecvSendCateId("6");// �����շ����

//					platOrder.setRecvSendCateId(recvSendCateId); // '�շ������'
			platOrder.setOrderSellerPrice(resultJsons.getBigDecimal("received_payment")); // ���������ʵ�������'
																							// t
																							// receivedpayment
			platOrder.setProvince(resultJsons.getString("receiver_state")); // ʡ' t receiverstate
//					platOrder.setProvinceId(provinceId); // 'ʡid'
			platOrder.setCity(resultJsons.getString("receiver_city")); // ��' t receivercity
//					platOrder.setCityId(cityId); //
			platOrder.setCounty(resultJsons.getString("receiver_district")); // ��' t receiverdistrict
//					platOrder.setCountyId(countyId); //
//					platOrder.setTown(town); // '��'
//					platOrder.setTownId(townId); // '��id'
			platOrder.setFreightPrice(resultJsons.getBigDecimal("post_fee")); // �˷�' t postfee
			platOrder.setVenderId("TM"); // '�̼�id'
//					orders	Order[]	
			platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					 platOrder.setDeliveryType("����ʱ��"); //'�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����;2-ֻ˫���ա������ͻ�(�����ղ�����;3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱  ��'

			List<PlatOrders> list = new ArrayList<PlatOrders>();

			JSONArray orderInfoList = resultJsons.getJSONObject("orders").getJSONArray("order");
			for (int i = 0; i < orderInfoList.size(); i++) {
				JSONObject itemInfo = orderInfoList.getJSONObject(i); // ���� jsonarray ���飬��ÿһ������ת�� json ����
				////System.err.println("orderInfo\t" + itemInfo);
				PlatOrders platOrders = new PlatOrders();
				platOrders.setGoodId(itemInfo.getString("num_iid")); // ƽ̨��Ʒ��� ��Ʒ����ID
				platOrders.setGoodName(itemInfo.getString("title")); // 'ƽ̨��Ʒ����' t title String ɽկ����Ի��� ��Ʒ����
				platOrders.setGoodNum(itemInfo.getInteger("num")); // '��Ʒ����' t num Number 1 ����������ȡֵ��Χ:�����������

				platOrders.setGoodMoney(itemInfo.getBigDecimal("num").multiply(itemInfo.getBigDecimal("price"))); // ��Ʒ���'
																													// t
				if (orderInfoList.size() == 1) {
					platOrders.setPayMoney(
							itemInfo.getBigDecimal("divide_order_fee").subtract(resultJsons.getBigDecimal("post_fee"))); // 'ʵ�����'��̯֮���ʵ�����
																															// 3.
																															// ��ȡ����Order�е�payment�ֶ��ڵ����Ӷ���ʱ�����������ã�����Ӷ���ʱ��������������
				} else {
					platOrders.setPayMoney(itemInfo.getBigDecimal("divide_order_fee")); // 'ʵ�����'��̯֮���ʵ�����
				}
				platOrders.setGoodSku(itemInfo.getString("sku_id")); // '��Ʒsku'
				platOrders.setOrderId(orderId); // '�������' 1
//						platOrders.setBatchNo(batchNo);	//	'����' 	
				platOrders.setExpressCom(itemInfo.getString("logistics_company")); // ��ݹ�˾' t�Ӷ��������Ŀ�ݹ�˾����
//						promotion_details		platOrders.setProActId(proActId);	//	��������' t promotionid	String	mjs	�Ż�id
//						platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("discount_fee").asDouble())); // ϵͳ�Żݽ��'
				platOrders.setAdjustMoney(itemInfo.getBigDecimal("adjust_fee")); // ��ҵ������' t
				platOrders.setMemo(itemInfo.getString("oid")); // '��ע' �� �ӵ�id oid
				platOrders.setGoodPrice(itemInfo.getBigDecimal("price")); // '��Ʒ����' t price
				platOrders.setDeliverWhs(itemInfo.getString("store_code")); // �����ֿ����'
				platOrders.setEcOrderId(resultJsons.getString("tid")); // 'ƽ̨������' t oid

				if (itemInfo.getString("shipper") == null) {
					platOrder.setDeliverSelf(1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t
				} else if (itemInfo.getString("shipper").equals("cn") || itemInfo.getString("shipper").equals("hold")) {
					platOrder.setDeliverSelf(0); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t
				} else {
					platOrder.setDeliverSelf(1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t
				}
				// �ֶ� shipper ��cn:����ַ�����seller:�̼Ҳַ�����hold:��ȷ�Ϸ����ֿ⣬null(��ֵ�����̼Ҳַ�����
				////System.err.println("itemInfo.get(\"shipper\")" + itemInfo.getShortValue("shipper"));
//						platOrders.setInvId(itemInfo.get("outer_iid") == null ? null : itemInfo.get("outer_iid").asText()); // �������'// �̼��ⲿ����

				// ѭ��ƥ����Ʒ����
				DecimalFormat dFormat = new DecimalFormat("0.00");
				boolean flag = true;

				// ������� ƽ̨��Ʒ��good_record�е�ƽ̨��Ʒ���룻
				String ecgoodid = platOrders.getGoodId();// ƽ̨��Ʒ���
				String goodsku = platOrders.getGoodSku();
				GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
				if (goodRecord == null) {
					platOrder.setAuditHint("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");
					////System.err.println("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");

					// platOrderDao.update(platOrder);//���¶����������ʾ��
					flag = false;
					list.add(platOrders);
				} else if (goodRecord.getGoodId() == null) {
					platOrder.setAuditHint("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");
					////System.err.println("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");

					// platOrderDao.update(platOrder);//���¶����������ʾ��
					flag = false;
					list.add(platOrders);
				} else {
					String invId = goodRecord.getGoodId();
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// ȥ���������ѯ���������Ϣ
					if (invtyDoc == null) {
						platOrder.setAuditHint("�ڴ��������δƥ�䵽��Ӧ�������,��ƥ��Ĵ�����룺" + invId);
						// platOrderDao.update(platOrder);//���¶����������ʾ��
						flag = false;
						////System.err.println("�ڴ��������δƥ�䵽��Ӧ�������,��ƥ��Ĵ�����룺" + invId);

						list.add(platOrders);
					} else {
						////System.err.println("�Ƿ���Ʒ\t" + platOrders.getPayMoney());

						if (platOrders.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {// ��Ʒ
							// �����Ƿ���Ʒ
							platOrders.setIsGift(1);
							platOrder.setHasGift(1);
							if (invtyDoc.getPto() != null) {
								if (invtyDoc.getPto() == 1) {
									// ����PTO���ʹ��
									ProdStru struck = prodStruDao.selectMomEncd(invId);
									if (struck == null) {
										// δ�ڲ�Ʒ�ṹ�в�ѯ����Ӧ����Ĳ�Ʒ�ṹ��Ϣ
										platOrder.setAuditHint("�ڲ�Ʒ�ṹ��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");
										// platOrderDao.update(platOrder);//���¶����������ʾ��
										////System.err.println("�ڲ�Ʒ�ṹ��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");

										flag = false;
										list.add(platOrders);
									} else {
										if (struck.getIsNtChk() == 0) {
											// ��Ӧ��Ʒ�ṹ��δ���
											////System.err.println("��Ӧ��Ʒ�ṹ��δ���");
											platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��δ���");
											// platOrderDao.update(platOrder);//���¶����������ʾ��
											flag = false;
											list.add(platOrders);
										} else {
											////System.err.println("ѭ����Ʒ�ṹ�ӱ���Ϣ");

											List<ProdStruSubTab> strucksublist = struck.getStruSubList();
											// ѭ����Ʒ�ṹ�ӱ���Ϣ
											if (strucksublist.size() == 0) {
												////System.err.println("��Ӧ��Ʒ�ṹû�������Ӽ���ϸ���������á�");

												platOrder.setAuditHint("��Ӧ��Ʒ�ṹû�������Ӽ���ϸ���������á�");
												// platOrderDao.update(platOrder);//���¶����������ʾ��
												flag = false;
												list.add(platOrders);
											} else if (strucksublist.size() == 1) {
												////System.err.println("����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������");

												// ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
												// �������
												platOrders.setInvId(strucksublist.get(0).getSubEncd());
												// ���ô������
												platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
														.multiply(strucksublist.get(0).getSubQty()).intValue());
												// ���ÿ�������
												platOrders.setCanRefNum(platOrders.getInvNum());
												platOrders.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
												platOrders.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
												// ����ʵ������
												platOrders.setPayPrice(BigDecimal.ZERO);
												platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
												// ���ÿ��˽��,��ʵ�����һ��
												platOrders.setCanRefMoney(platOrders.getPayMoney());
												list.add(platOrders);
											} else {
												// ��Ʒ ѭ��
												////System.err.println(" ��Ʒ ѭ��");

												for (int j = 0; j < strucksublist.size(); j++) {
													PlatOrders order = (PlatOrders) platOrders.clone();

													InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
															strucksublist.get(j).getSubEncd());

													order.setInvNum((new BigDecimal(order.getGoodNum()))
															.multiply(strucksublist.get(j).getSubQty()).intValue());
													order.setPayMoney(BigDecimal.ZERO);// ����ʵ����� ������λС��
													// ����ʵ������ ����8λС��
													order.setPayPrice(BigDecimal.ZERO);
													order.setSellerPrice(order.getPayPrice());
													// �������
													order.setInvId(strucksublist.get(j).getSubEncd());
													// ���ÿ�������
													order.setCanRefNum(order.getInvNum());
													// ���ÿ��˽��
													order.setCanRefMoney(order.getPayMoney());
													order.setPtoCode(invtyDoc2.getInvtyEncd());// ���ö�Ӧĸ������
													order.setPtoName(invtyDoc2.getInvtyNm());// ���ö�Ӧĸ������

													list.add(order);

												}
											}

										}
									}
								} else {
									////System.err.println("������PTO���ʹ��");

									// ������PTO���ʹ��
									// �������
									platOrders.setInvId(goodRecord.getGoodId());
									// ���ô������
									platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum()).intValue());
									// ���ÿ�������
									platOrders.setCanRefNum(platOrders.getInvNum());
									platOrders.setPtoCode("");// ���ö�Ӧĸ������
									platOrders.setPtoName("");// ���ö�Ӧĸ������
									// ����ʵ������
									platOrders.setPayPrice(BigDecimal.ZERO);
									platOrders.setSellerPrice(BigDecimal.ZERO);// ���㵥����ʵ������һ��

									// ���ÿ��˽��,��ʵ�����һ��
									platOrders.setCanRefMoney(platOrders.getPayMoney());

									list.add(platOrders);
								}
							} else {
								////System.err.println("�������pto����Ϊ��");

								// �������pto����Ϊ��
								platOrder.setAuditHint("���������PTO����Ϊ��");
								// platOrderDao.update(platOrder);//���¶����������ʾ��
								flag = false;
								list.add(platOrders);
							}

						} else {// ����Ʒ
							platOrders.setIsGift(0);
							////System.err.println("����Ʒ");

							if (invtyDoc.getPto() != null) {
								if (invtyDoc.getPto() == 1) {
									//////System.err.println("����PTO���ʹ��");

									// ����PTO���ʹ��
									ProdStru struck = prodStruDao.selectMomEncd(invId);
									if (struck == null) {
										////System.err.println("�ڲ�Ʒ�ṹ��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");

										// δ�ڲ�Ʒ�ṹ�в�ѯ����Ӧ����Ĳ�Ʒ�ṹ��Ϣ
										platOrder.setAuditHint("�ڲ�Ʒ�ṹ��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");
										// platOrderDao.update(platOrder);//���¶����������ʾ��
										flag = false;
										list.add(platOrders);
									} else {
										if (struck.getIsNtChk() == 0) {
											// ��Ӧ��Ʒ�ṹ��δ���
											////System.err.println("��Ӧ��Ʒ�ṹ��δ���");

											platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��δ���");
											// platOrderDao.update(platOrder);//���¶����������ʾ��
											flag = false;
											list.add(platOrders);
										} else {
											List<ProdStruSubTab> strucksublist = struck.getStruSubList();
											// ѭ����Ʒ�ṹ�ӱ���Ϣ
											////System.err.println("ѭ����Ʒ�ṹ�ӱ���Ϣ");

											if (strucksublist.size() == 0) {
												////System.err.println("��Ӧ��Ʒ�ṹû�������Ӽ���ϸ���������á�");

												platOrder.setAuditHint("��Ӧ��Ʒ�ṹû�������Ӽ���ϸ���������á�");
												// platOrderDao.update(platOrder);//���¶����������ʾ��
												flag = false;
												list.add(platOrders);
											} else if (strucksublist.size() == 1) {
												////System.err.println("����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������");

												// ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
												// �������
												platOrders.setInvId(strucksublist.get(0).getSubEncd());
												// ���ô������
												platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
														.multiply(strucksublist.get(0).getSubQty()).intValue());
												// ���ÿ�������
												platOrders.setCanRefNum(platOrders.getInvNum());
												platOrders.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
												platOrders.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
												// ����ʵ������
												platOrders.setPayPrice(platOrders.getPayMoney().divide(
														(new BigDecimal(platOrders.getInvNum())), 8,
														BigDecimal.ROUND_HALF_UP));
												platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��

												// ���ÿ��˽��,��ʵ�����һ��
												platOrders.setCanRefMoney(platOrders.getPayMoney());

												list.add(platOrders);
											} else {
												// ����Ʒ�ṹ���Ӽ�����������1ʱ����Ҫ���ɶ�����ϸ
												////System.err.println("����Ʒ�ṹ���Ӽ�����������1ʱ����Ҫ���ɶ�����ϸ");

												// �����Ӽ��ο��ɱ����Ӽ��������ܳɱ�
												// ������꣬���false˵����Ӧ�Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���
												BigDecimal total = new BigDecimal("0");
												for (int j = 0; j < strucksublist.size(); j++) {
													InvtyDoc invtyDoc1 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
															strucksublist.get(j).getSubEncd());
													if (invtyDoc1.getRefCost() == null) {
														flag = false;
													} else {
														total = total.add(invtyDoc1.getRefCost()
																.multiply(strucksublist.get(j).getSubQty()));
													}
												}
												if (!flag) {
													// �Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���
													platOrder.setAuditHint("��Ӧ�Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���");
													list.add(platOrders);
												} else {

													// ����Ʒ ѭ��
													BigDecimal money123 = new BigDecimal("0");// �����ѷ�ʵ�����
																								// ���һ���ü���ʱ�õ�
													for (int j = 0; j < strucksublist.size(); j++) {
														PlatOrders order = (PlatOrders) platOrders.clone();

														if (j + 1 < strucksublist.size()) {
															// ����ÿ���Ӽ�ռ�ܳɱ��ı��� ����8λС��
															InvtyDoc invtyDoc2 = invtyDocDao
																	.selectInvtyDocByInvtyDocEncd(
																			strucksublist.get(j).getSubEncd());
															BigDecimal rate = invtyDoc2.getRefCost()
																	.multiply(strucksublist.get(j).getSubQty())
																	.divide(total, 8, BigDecimal.ROUND_HALF_UP);
															order.setInvNum((new BigDecimal(order.getGoodNum()))
																	.multiply(strucksublist.get(j).getSubQty())
																	.intValue());
															order.setPayMoney(new BigDecimal(dFormat
																	.format(order.getPayMoney().multiply(rate))));// ����ʵ�����
																													// ������λС��
															// ����ʵ������ ����8λС��
															order.setPayPrice(order.getPayMoney().divide(
																	(new BigDecimal(order.getInvNum())), 8,
																	BigDecimal.ROUND_HALF_UP));
															order.setSellerPrice(order.getPayPrice());
															// �������
															order.setInvId(strucksublist.get(j).getSubEncd());
															// ���ÿ�������
															order.setCanRefNum(order.getInvNum());
															// ���ÿ��˽��
															order.setCanRefMoney(order.getPayMoney());
															order.setPtoCode(invtyDoc2.getInvtyEncd());// ���ö�Ӧĸ������
															order.setPtoName(invtyDoc2.getInvtyNm());// ���ö�Ӧĸ������
															// �����Ƿ���Ʒ

															list.add(order);
															money123 = money123.add(order.getPayMoney());
														} else {
															order.setInvNum((new BigDecimal(order.getGoodNum()))
																	.multiply(strucksublist.get(j).getSubQty())
																	.intValue());
															order.setPayMoney(order.getPayMoney().subtract(money123));// ����ʵ�����
																														// ������λС��
															// ����ʵ������ ����8λС��
															order.setPayPrice(order.getPayMoney().divide(
																	(new BigDecimal(order.getInvNum())), 8,
																	BigDecimal.ROUND_HALF_UP));
															order.setSellerPrice(order.getPayPrice());
															// �������
															order.setInvId(strucksublist.get(j).getSubEncd());
															// ���ÿ�������
															order.setCanRefNum(order.getInvNum());
															// ���ÿ��˽��
															order.setCanRefMoney(order.getPayMoney());
															order.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
															order.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
															// �����Ƿ���Ʒ
															order.setIsGift(0);

															list.add(order);
														}

													}
												}
											}

										}
									}
								} else {
									////System.err.println("������PTO���ʹ��");

									// ������PTO���ʹ��
									// �������
									platOrders.setInvId(goodRecord.getGoodId());
									// ���ô������
									platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum()).intValue());
									// ���ÿ�������
									platOrders.setCanRefNum(platOrders.getInvNum());
									platOrders.setPtoCode("");// ���ö�Ӧĸ������
									platOrders.setPtoName("");// ���ö�Ӧĸ������
									// ����ʵ������
									platOrders.setPayPrice(platOrders.getPayMoney().divide(
											(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
									platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
									// ����ʵ�������Ƿ�Ϊ0�ж��Ƿ�Ϊ��Ʒ
									platOrders.setIsGift(0);

									// ���ÿ��˽��,��ʵ�����һ��
									platOrders.setCanRefMoney(platOrders.getPayMoney());

									list.add(platOrders);
								}
							} else {
								////System.err.println(" �������pto����Ϊ��");

								// �������pto����Ϊ��
								platOrder.setAuditHint("���������PTO����Ϊ��");
								// platOrderDao.update(platOrder);//���¶����������ʾ��
								flag = false;
								list.add(platOrders);
								continue;
							}
						}
					}
				}

			}
			if (resultJsons.containsKey("promotion_details")) {
				List<CouponDetail> couponList = new ArrayList<CouponDetail>();

				JSONArray promotionDetails = resultJsons.getJSONObject("promotion_details")
						.getJSONArray("promotion_detail");
				for (int i = 0; i < promotionDetails.size(); i++) {
					JSONObject promotion = promotionDetails.getJSONObject(i); // ���� jsonarray ���飬��ÿһ������ת�� json ����
					////System.err.println("promotion\t" + promotion);
					CouponDetail coupon1 = new CouponDetail();
					coupon1.setPlatId("TM");// ƽ̨id������JD����èJD
					coupon1.setStoreId(storeId);// ����id
					coupon1.setOrderId(resultJsons.getString("tid"));// ������
					coupon1.setSkuId(null);
					coupon1.setCouponCode(null);// �Ż����ͱ���
					coupon1.setCouponType(promotion.getString("promotion_name"));// �Ż�����
					coupon1.setCouponPrice(promotion.getBigDecimal("discount_fee"));// �Żݽ��
					couponList.add(coupon1);
				}
				couponDetailDao.insert(couponList);
			}
			
			platOrder.setGoodNum(list.size()); // ��ƷƷ����
			////System.err.println("platOrder.setGoodNum(list.size())��Ʒ����\t" + list.size());

			platOrderDao.insert(platOrder);
			platOrdersDao.insert(list);
//					invoiceDao.insert(invoiceList);					
			autoMatch(platOrder.getOrderId(), userId);
			// ����������ɺ�ִ���Զ�ƥ��
			//PlatOrder platorder = platOrderDao.selectByEcOrderId(resultJsons.getString("tid"));
			//List<PlatOrders> orderslist = platOrdersDao.select(platorder.getOrderId());
			/*if (auditStrategyService.autoAuditCheck(platorder, orderslist)) {
				// ����trueʱ���˶���ͨ������ֱ�ӽ������
				associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
				// ����Ĭ�ϲ���Աsys
			}*/
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "�������سɹ�" + tid, null);

		} catch (Exception e) {
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "��������ʧ��" + tid, null);
		}
		return resp;
	}
	private String jdDownload(String userId, String startDate, String endDate, int pageNo, int pageSize,
			String storeId,int downloadCount) {
		String resp = "";
		String message = "";
		boolean success = true;
		if (pageNo == 1) {
			// �����ص�һҳʱ����downloadCount��0
			downloadCount = 0;
		}
		try {
			// String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			// Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("start_date", startDate);
			objectNode.put("end_date", endDate);
			objectNode.put("dateType", 1);// ����ʱ����� 1 ��������ʱ�� 2��������ʱ��
			objectNode.put("order_state", "WAIT_SELLER_STOCK_OUT,WAIT_GOODS_RECEIVE_CONFIRM,FINISHED_L");
			objectNode.put("optional_fields",
					"itemInfoList,couponDetailList,paymentConfirmTime,orderTotalPrice,orderPayment,orderRemark,venderRemark,pin,consigneeInfo,invoiceInfo,invoiceEasyInfo,invoiceConsigneeEmail,invoiceConsigneePhone,balanceUsed,sellerDiscount,orderSellerPrice,freightPrice,orderStartTime,deliveryType,venderId,orderState,waybill");
			objectNode.put("page", pageNo);
			objectNode.put("page_size", pageSize);
//			objectNode.put("sortType", map.get("sortType").toString());
//			objectNode.put("dateType", map.get("dateType").toString());
			objectNode.put("sortType", "");

			String json = objectNode.toString();
			// String storeId = map.get("storeId").toString();
//			resp = platOrderService.download(storeId, json, userId);

			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			String jdRespStr = ECHelper.getJD("jingdong.pop.order.search", storeSettings, json);
			////System.err.println("���������" + json);
			//System.out.println(jdRespStr);
			// String jdRespStr = ECHelper.getJD("jingdong.pop.order.search",
			// "0b88dcc2ac604d4fb55fe317f9f5bd94ogrj", json);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			// �ж������Ƿ���ִ����д���ʱֱ�ӷ��ء�
			if (jdRespJson.has("error_response")) {
				message = jdRespJson.get("error_response").get("zh_desc").asText();
				success = false;
				resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
				return resp;
			}
			JsonNode resultJson = jdRespJson.get("jingdong_pop_order_search_responce").get("searchorderinfo_result");
			if (resultJson.get("apiResult").get("success").asBoolean()) {
				//������list
				List<PlatOrder> platOrderList = new ArrayList();
				List<InvoiceInfo> invoiceList = new ArrayList();
				List<CouponDetail> couponList = new ArrayList();
				// System.out.println(resultJson);
				ArrayNode orderInfoList = (ArrayNode) resultJson.get("orderInfoList");
				// System.out.println(resultJson);
				for (Iterator<JsonNode> orderInfoIterator = orderInfoList.iterator(); orderInfoIterator.hasNext();) {
					//�Ӷ���list
					List<PlatOrders> platOrdersList = new ArrayList();

					JsonNode orderInfo = orderInfoIterator.next();
					String ecOrderId = orderInfo.get("orderId").asText();
					// ���ж϶������Ƿ��Ѿ�����
					////System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
					if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
						PlatOrder platOrder = new PlatOrder();
						int goodNum = 0;

						//��ȡ������
//						String orderId = getOrderNo.getSeqNo("ec", userId);

						// ������ϸlist
						ArrayNode itemInfoList = (ArrayNode) orderInfo.get("itemInfoList");
						// �����ܽ��
						/*
						 * BigDecimal moneySum = new BigDecimal(0); for (Iterator<JsonNode>
						 * itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
						 * .hasNext();) { JsonNode itemInfo = itemInfoInfoIterator.next();
						 * //��������������������ܽ�� BigDecimal goodPrice = new
						 * BigDecimal(itemInfo.get("jdPrice").asDouble()*itemInfo.get("itemTotal").asInt
						 * ()); moneySum.add(goodPrice); }
						 */
						// �����Ż���ϸ
						List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
						if (orderInfo.get("sellerDiscount").asDouble() != 0) {
							ArrayNode couponIterator = (ArrayNode) orderInfo.get("couponDetailList");
							if (couponIterator != null) {
								for (Iterator<JsonNode> it = couponIterator.iterator(); it.hasNext();) {
									JsonNode coupons = it.next();
									// //System.err.println("_________�Ż���ϸ��" + coupons.toString());
									CouponDetail coupon = JacksonUtil.getPOJO((ObjectNode) coupons, CouponDetail.class);
									coupon.setStoreId(storeId);
									coupon.setCouponCode(
											coupon.getCouponType().substring(0, coupon.getCouponType().indexOf('-')));
									coupon.setPlatId("JD");
									coupon.setOrderId(ecOrderId);
									couponList.add(coupon);
									couponDetails.add(coupon);
								}
							}
						}
						platOrder.setEcOrderId(orderInfo.get("orderId").asText());// ���̶�����
						List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
						for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
								.hasNext();) {
							JsonNode itemInfo = itemInfoInfoIterator.next();
							PlatOrders platOrders = new PlatOrders();
							platOrders.setGoodId(itemInfo.get("wareId").asText());// ������Ʒ����
							platOrders.setGoodNum(itemInfo.get("itemTotal").asInt());// ��Ʒ����
							platOrders.setGoodName(itemInfo.get("skuName").asText());// ƽ̨��Ʒ����
							platOrders.setGoodPrice(new BigDecimal(itemInfo.get("jdPrice").asDouble()));// ��Ʒ���� ������
							// platOrders.setPayMoney(new
							// BigDecimal(itemInfo.get("productNo").asDouble()));// ʵ�����
							platOrders.setGoodSku(itemInfo.get("skuId").asText());// ��Ʒsku
							//�ֱ�����
//							platOrders.setOrderId(orderId);
							platOrders.setEcOrderId(platOrder.getEcOrderId());

							platOrders.setGoodMoney(
									platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// ��Ʒ���
							platOrders.setDeliverWhs(itemInfo.get("newStoreId").asText());// ������ϸ�еķ�����id
							platOrders.setPayMoney(platOrders.getGoodMoney());
							if (platOrders.getDeliverWhs().equals("0")) {
								platOrders.setDeliverWhs("");
							
							} else {
								Map map = new HashMap();
								map.put("type", "JD");
								map.put("online", platOrders.getDeliverWhs());
								String newWhs = PlatWhsMappDao.select(map);
								if (newWhs != null) {
									platOrders.setDeliverWhs(newWhs);
								} else {
									platOrder.setDeliverWhs("");
									platOrder.setAuditHint("ƽ̨�ַ�������ȱ��ƽ̨��ӳ��,����ƽ�ֱ��룺"+platOrders.getDeliverWhs());
								}
							}
							// BigDecimal num = new
							// BigDecimal(orderInfo.get("orderSellerPrice").asDouble()*itemInfo.get("jdPrice").asDouble());
							// platOrders.setSellerPrice(num.divide(moneySum));
							// private String batchNo;//����
							// private String expressCom;//��ݹ�˾
							/*
							 * if (orderInfo.get("storeOrder").asText().equals("���ֶ���")) { //
							 * platOrders.setDeliverWhs("�������ܲ�");// �������ֿ� }
							 */
							// private String proActId;//��������
							// private Long discountMoney;//ϵͳ�Żݽ��
							// private String adjustMoney;//���ҵ������

							// itemInfo.get("skuName").asText();
							platOrdersList1.add(platOrders);

							platOrder.setHasGift(0);// ���Ƿ���Ʒ��0������1��
							goodNum += itemInfo.get("itemTotal").asInt();
						}
						StoreRecord storeRecord = storeRecordDao.select(storeId);
						//�ж϶���״̬�Ƿ񷢻�
						if(orderInfo.get("orderState").asText().equals("WAIT_SELLER_STOCK_OUT")) {
							//�ȴ�����
							platOrder.setIsShip(0);// ���Ƿ񷢻�
						}else {
							platOrder.setIsShip(1);// ���Ƿ񷢻�
							if(orderInfo.has("waybill")){
								platOrder.setExpressNo(orderInfo.get("waybill").asText());
							}
							
						}
						//��������
//						platOrder.setOrderId(orderId);// �������
						platOrder.setStoreId(storeId);// ���̱��
						platOrder.setStoreName(storeRecord.getStoreName());// ��������
						if (orderInfo.has("paymentConfirmTime")) {
							//�ж϶����Ƿ���ڸ���ʱ�䣬���������û�и���ʱ��
							platOrder.setPayTime(orderInfo.get("paymentConfirmTime").asText());// ����ʱ��
						}
						platOrder.setTradeDt(orderInfo.get("orderStartTime").asText());// �µ�ʱ��
						platOrder.setIsAudit(0);// ���Ƿ���ˣ�0δ��ˣ�1���
						platOrder.setGoodNum(goodNum);// ��Ʒ������
						platOrder.setDeliveryType(orderInfo.get("deliveryType").asText());// �����ͻ�����
						platOrder.setVenderId(orderInfo.get("venderId").asText());// �����̼�ID�̼ұ���
						platOrder.setGoodMoney(new BigDecimal(orderInfo.get("orderTotalPrice").asDouble()));// �����ܽ��
						platOrder.setPayMoney(new BigDecimal(orderInfo.get("orderPayment").asDouble()));// ���ʵ�ʸ�����
						platOrder.setBuyerNote(orderInfo.get("orderRemark").asText());// ��ұ�ע
						platOrder.setSellerNote(orderInfo.get("venderRemark").asText());// �̼ұ�ע
						platOrder.setBuyerId(orderInfo.get("pin").asText());// ��һ�Ա��
						platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("orderSellerPrice").asDouble()));// ������
						JsonNode consigneeInfo = orderInfo.get("consigneeInfo");
						platOrder.setRecAddress(consigneeInfo.get("fullAddress").asText());// ���ջ�����ϸ��ַ
						platOrder.setRecName(consigneeInfo.get("fullname").asText());// ���ջ�������
						platOrder.setRecMobile(consigneeInfo.get("mobile").asText());// ���ջ����ֻ���
						platOrder.setProvince(consigneeInfo.get("province").asText());// �ջ���ʡ
						platOrder.setProvinceId(consigneeInfo.get("provinceId").asText());// �ջ���ʡid
						platOrder.setCity(consigneeInfo.get("city").asText());// �ջ�����
						platOrder.setCityId(consigneeInfo.get("cityId").asText());// �ջ�����id
						if (consigneeInfo.has("county")) {
							platOrder.setCounty(consigneeInfo.get("county").asText());// �ջ�������
							platOrder.setCountyId(consigneeInfo.get("countyId").asText());// �ջ�������id
						}
						platOrder.setDeliverWhs(orderInfo.get("storeId").asText());
						platOrder.setSellTypId("1");// ��������������ͨ����
						platOrder.setBizTypId("2");// ����ҵ������2c
						platOrder.setRecvSendCateId("6");// �����շ����
						// �ö����еķ����ֿ�ȥƽ̨��ӳ���жһ�ϵͳ�ֿ����
						if (platOrder.getDeliverWhs().equals("0")) {
							platOrder.setDeliverWhs("");
							platOrder.setDeliverSelf(1);
						} else {
							Map map = new HashMap();
							map.put("type", "JD");
							map.put("online", platOrder.getDeliverWhs());
							String newWhs1 = PlatWhsMappDao.select(map);
							if (newWhs1 != null) {
								platOrder.setDeliverWhs(newWhs1);
							} else {
								platOrder.setDeliverWhs("");
								platOrder.setAuditHint("ƽ̨�ַ�������ȱ��ƽ̨��ӳ��");
							}
							platOrder.setDeliverSelf(0);
						}
						if (consigneeInfo.has("town")) {
							platOrder.setTown(consigneeInfo.get("town").asText());
							platOrder.setTownId(consigneeInfo.get("townId").asText());
						}

						if (orderInfo.get("invoiceInfo").asText().equals("����Ҫ���߷�Ʊ")) {
							platOrder.setIsInvoice(0);// ���Ƿ�Ʊ
						} else {
							platOrder.setIsInvoice(1);// ���Ƿ�Ʊ
							InvoiceInfo invoiceInfo = new InvoiceInfo();
							JsonNode invoice = orderInfo.get("invoiceEasyInfo");
							////System.err.println("__________��Ʊ��Ϣ��" + invoice.toString());
							/*
							 * invoiceInfo.setInvoiceCode(invoice.get("invoiceCode").asText());
							 * invoiceInfo.setInvoiceConsigneeEmail(invoice.get("invoiceConsigneeEmail").
							 * asText()); invoiceInfo.setInvoiceConsigneePhone(invoice.get(""));
							 */
							invoiceInfo = JacksonUtil.getPOJO((ObjectNode) invoice, InvoiceInfo.class);
							invoiceInfo.setOrderId(platOrder.getEcOrderId());
							invoiceInfo.setPlatId("JD");
							invoiceInfo.setShopId(storeId);
							platOrder.setInvoiceTitle(orderInfo.get("invoiceEasyInfo").get("invoiceTitle").asText());// ��Ʊ̧ͷ
							invoiceList.add(invoiceInfo);
						}
						/*ͨ��storeOrder�Ƿ���ھ��ֶ����ж��Ƿ��Է�������ȷ��һ�̻��������ھ��������������ֶ�ֵ�����ھ��ֶ���2019��12��9��
						 * if (orderInfo.get("storeOrder").asText().equals("���ֶ���")) {
						 * platOrder.setDeliverSelf(0);// ���ö���Ϊ } else { platOrder.setDeliverSelf(1);//
						 * ���ö���Ϊ�Է������� }
						 */
						platOrder.setFreightPrice(new BigDecimal(orderInfo.get("freightPrice").asDouble()));// �˷�
						platOrder.setNoteFlag(0);// ���̼ұ�ע���ģ�Ĭ��ֵ����ʾ������
						platOrder.setIsClose(0);// ���Ƿ�ر�
						// platOrder.setIsShip(0);// ���Ƿ񷢻�
						// platOrder.setAdjustMoney(new
						// BigDecimal(orderInfo.get("balanceUsed").asDouble()));// �̼ҵ������
						platOrder.setAdjustMoney(new BigDecimal(0));
						platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("sellerDiscount").asDouble()));// �Żݽ��
						// platOrder.setGoodPrice(new BigDecimal(0L));// ��Ʒ����
						// platOrder.setPayPrice(new BigDecimal(0L));// ʵ������

						platOrder.setOrderStatus(0);// ������״̬
						platOrder.setReturnStatus(0);// ���˻�״̬
						platOrder.setDownloadTime(sdf.format(new Date()));// ��������ʱ��
						//�ֱ�list
						platOrdersList.addAll(checkInvty(platOrder, platOrdersList1, couponDetails));

						/*if (platOrder.getDeliverSelf() == 1) {
							platOrder.setPlatOrdersList(platOrdersList);
							Map map2 = orderDealSettingsServiceImpl.matchWareHouse(platOrder, storeRecord.getEcId());
							if (map2.get("isSuccess").toString().equals("true")) {
								platOrder.setDeliverWhs(map2.get("whsCode").toString());
								for (int i = 0; i < platOrdersList.size(); i++) {
									platOrdersList.get(i).setDeliverWhs(map2.get("whsCode").toString());
								}
								// ƥ��ֿ�ɹ���ƥ���ݹ�˾
								Map map3 = logisticsTabServiceImpl.findExpressCodeByOrderId(platOrder, platOrdersList);
								if (map3.get("flag").toString().equals("true")) {
									platOrder.setExpressCode(map3.get("expressCode").toString());
									platOrder.setExpressTemplate(map3.get("templateId").toString());
								} else {
									platOrder.setAuditHint(map3.get("message").toString());
								}

							} else {
								platOrder.setAuditHint(map2.get("message").toString());
							}
						}*/

						/*if (StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
							// ƥ��ֿ�ɹ���������μ��۳�������
							int aa = 0;
							int bb = 0;
							for (int j = 0; j < platOrdersList.size(); j++) {
								if (StringUtils.isNotEmpty(platOrdersList.get(j).getBatchNo())) {
									aa++;
								}
								if (StringUtils.isEmpty(platOrdersList.get(j).getInvId())) {
									bb++;
								}
							}
							if (aa != platOrdersList.size() && bb == 0) {
								// aa��С��������ϸ�б�sizeʱ��˵����ϸ����δ�ۼ���������������
								Map map4 = checkCanUseNumAndBatch(platOrdersList);
								if (map4.get("flag").toString().equals("true")) {
									// �������γɹ�
									platOrdersList.clear();
									platOrdersList.addAll((List<PlatOrders>) map4.get("platOrders"));
								} else {
									platOrder.setAuditHint(map4.get("message").toString());
									// ��־��¼
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(userId);
									MisUser misUser = misUserDao.select(userId);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("�Զ�ƥ��ʧ�ܣ�" + platOrder.getAuditHint());
									logRecord.setOperatTime(sdf.format(new Date()));
									logRecord.setOperatType(11);// 11�Զ�ƥ��
									logRecord.setTypeName("�Զ�ƥ��");
									logRecord.setOperatOrder(platOrder.getEcOrderId());
									logRecordDao.insert(logRecord);
								}
							}
						}*/

						/*boolean ff = true;// ������꣬�ж϶�����ϸ���Ƿ����δƥ�䵽�����������ݡ�
						BigDecimal allCount = new BigDecimal("0");// ������Ʒ����
						for (int j = 0; j < platOrdersList.size(); j++) {
							if (platOrdersList.get(j).getInvId() == null
									|| platOrdersList.get(j).getInvId().equals("")) {
								ff = false;
							} else {
								allCount = allCount.add(new BigDecimal(platOrdersList.get(j).getInvNum()));
							}
						}
						if (ff) {
							platOrder.setGoodNum(allCount.intValue());
						}*/

                        //�ֱ�List����set����  ����������
                        platOrder.setPlatOrdersList(platOrdersList);
                        //����
                        platOrderList.add(platOrder);
                    }
                }

                //���ɶ����� + �������ݿ�
                List<String> seqNoList = getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId);
                //���붩�� + �Զ�ƥ��
                downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, invoiceList, couponList, null, downloadCount, userId, seqNoList);

/*				if (platOrderList.size() > 0) {
					platOrderDao.insertList(platOrderList);
					platOrdersDao.insert(platOrdersList);
				}
				if (invoiceList.size() > 0) {
					invoiceDao.insert(invoiceList);
				}
				if (couponList.size() > 0) {
					couponDetailDao.insert(couponList);
				}
				downloadCount += platOrderList.size();

				// ����������ɺ��ж��Ƿ��Զ�����
				for (int i = 0; i < platOrderList.size(); i++) {
					//ִ���Զ�ƥ��
					autoMatch(platOrderList.get(i).getOrderId(), userId);
					*//*
					PlatOrder platorder = platOrderDao.selectByEcOrderId(platOrderList.get(i).getEcOrderId());
					List<PlatOrders> orderslist = platOrdersDao.select(platorder.getOrderId());
					boolean ff = true;// ������꣬�ж϶�����ϸ���Ƿ����δƥ�䵽�����������ݡ�
					for (int j = 0; j < orderslist.size(); j++) {
						if (orderslist.get(j).getInvId() == null || orderslist.get(j).getInvId().equals("")) {
							ff = false;
						}
					}
					if (!ff) {
						continue;
					}
					if (auditStrategyService.autoAuditCheck(platorder, orderslist)) {
						// ����trueʱ���˶���ͨ������ֱ�ӽ������
						associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
						// ����Ĭ�ϲ���Աsys
					}*//*
				}*/

                if (resultJson.get("orderTotal").asInt() - pageNo * pageSize > 0) {
                    resp = jdDownload(userId, startDate, endDate, pageNo + 1, pageSize, storeId, downloadCount);
                } else {
                    // ��־��¼
                    LogRecord logRecord = new LogRecord();
                    logRecord.setOperatId(userId);
                    MisUser misUser = misUserDao.select(userId);
                    if (misUser != null) {
                        logRecord.setOperatName(misUser.getUserName());
                    }
                    logRecord.setOperatContent("�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + downloadCount + "��");
                    logRecord.setOperatTime(sdf.format(new Date()));
                    logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
                    logRecord.setTypeName("�Զ�����");
                    logRecordDao.insert(logRecord);
                    resp = BaseJson.returnRespObj("ec/platOrder/download", success, "���γɹ�����" + downloadCount + "������", null);
                    return resp;
                }
            } else {
                success = false;
                message = "JD errInfo : " + resultJson.get("apiResult").get("chineseErrCode").asText();
                // ��־��¼
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                MisUser misUser = misUserDao.select(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent("�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + downloadCount + "��������쳣��" + message);
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
                logRecord.setTypeName("�Զ�����");
                logRecordDao.insert(logRecord);
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
            }

            // resp=resultJson.toString();
        } catch (Exception e) {
            logger.error("URL��ec/platOrder/download �쳣˵����", e);

            success = false;
            StackTraceElement s = e.getStackTrace()[0];
            message = "�쳣��Ϊ��" + s.getClassName() + "  �쳣������" + s.getMethodName() + "()" + "  �쳣������" + s.getLineNumber() + "  �쳣���̣�" + storeId + "  ������Ա��" + userId + "  ����ʱ��Σ�" + startDate + " --- " + endDate;
            // ��־��¼
            LogRecord logRecord = new LogRecord();
            logRecord.setOperatId(userId);
            MisUser misUser = misUserDao.select(userId);
            if (misUser != null) {
                logRecord.setOperatName(misUser.getUserName());
            }
            logRecord.setOperatContent("�����Զ����ص��̣�" + storeId + "�ŵ��̣�������" + downloadCount + "��������쳣��" + message);
            logRecord.setOperatTime(sdf.format(new Date()));
            logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
            logRecord.setTypeName("�Զ�����");
            logRecordDao.insert(logRecord);

            try {
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
//				e1.printStackTrace();
            }
            //throw new RuntimeException();

        }

        return resp;
    }

    /**
     * ������������
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @param storeId
     * @param orderState ����״̬1(�Ѹ���)��2(�ѷ���)��3(��ǩ��)
     * @return
     */
    private String KaoLaDownload(String userId, String startDate, String endDate, int pageNo, int pageSize,
                                 String storeId, String orderState) {
        String resp = "";
        String message = "";
        boolean success = true;
        if (pageNo == 1 && orderState.equals("1")) {
            // �����ص�һҳʱ����downloadCount��0
            downloadCount = 0;
        }
        try {
            // String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            // Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            ObjectNode objectNode = JacksonUtil.getObjectNode("");
            objectNode.put("start_time", startDate);
            objectNode.put("end_time", endDate);
            objectNode.put("order_status", orderState);//����״̬1(�Ѹ���)��2(�ѷ���)��3(��ǩ��)��5(ȡ����ȷ��)��6(��ȡ��)
            objectNode.put("date_type", "1001");// ������������1(֧��ʱ��)��2(����ʱ��)��3(ǩ��ʱ��)��1001��������ʱ�䣩��1005(ȡ��������ʱ��)
            objectNode.put("page_no", "" + pageNo);
            objectNode.put("page_size", "" + pageSize);

            String json = objectNode.toString();
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            String jdRespStr = ECHelper.getKaola("kaola.order.search", storeSettings, json);
            ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
            //////System.err.println("��ѯ�����"+jdRespJson);

            // �ж������Ƿ���ִ����д���ʱֱ�ӷ��ء�
            if (jdRespJson.has("error_response")) {
                message = jdRespJson.get("error_response").get("msg").asText();
                success = false;
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
                return resp;
            }
            List<PlatOrder> platOrderList = new ArrayList();
            List<PlatOrders> platOrdersList = new ArrayList();
            List<InvoiceInfo> invoiceList = new ArrayList();
            List<CouponDetail> couponList = new ArrayList();
            // System.out.println(resultJson);
            ArrayNode orderInfoList = (ArrayNode) jdRespJson.get("kaola_order_search_response").get("orders");
            // System.out.println(resultJson);
            for (Iterator<JsonNode> orderInfoIterator = orderInfoList.iterator(); orderInfoIterator.hasNext(); ) {
                JsonNode orderInfo = orderInfoIterator.next();
                String ecOrderId = orderInfo.get("order_id").asText();
                // ���ж϶������Ƿ��Ѿ�����
                //////System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
                if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
                    PlatOrder platOrder = new PlatOrder();
                    int goodNum = 0;
                    String orderId = getOrderNo.getSeqNo("ec", userId);

                    // ������ϸlist
                    ArrayNode itemInfoList = (ArrayNode) orderInfo.get("order_skus");
                    // �����Ż���ϸ
                    List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
                    platOrder.setEcOrderId(orderInfo.get("order_id").asText());// ���̶�����
                    List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
                    for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
                            .hasNext(); ) {
                        JsonNode itemInfo = itemInfoInfoIterator.next();
                        PlatOrders platOrders = new PlatOrders();
                        platOrders.setGoodId(itemInfo.get("barcode").asText());// ������Ʒ����
                        platOrders.setGoodNum(itemInfo.get("count").asInt());// ��Ʒ����
                        platOrders.setGoodName(itemInfo.get("product_name").asText());// ƽ̨��Ʒ����
                        platOrders.setGoodPrice(new BigDecimal(itemInfo.get("origin_price").asDouble()));// ��Ʒ���� ������

                        platOrders.setGoodSku("");// ��Ʒsku
                        platOrders.setOrderId(orderId);
                        platOrders.setEcOrderId(platOrder.getEcOrderId());

                        platOrders.setGoodMoney(platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// ��Ʒ���
                        platOrders.setDeliverWhs("");// ������ϸ�еķ�����id
                        platOrders.setPayMoney(new BigDecimal(itemInfo.get("real_totle_price").asDouble()));
                        platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("activity_totle_amount").asDouble()));

                        platOrdersList.add(platOrders);

                        //platOrder.setHasGift(0);// ���Ƿ���Ʒ��0������1��
                        goodNum += platOrders.getGoodNum();
                    }
                    StoreRecord storeRecord = storeRecordDao.select(storeId);
                    platOrder.setOrderId(orderId);// �������
                    platOrder.setStoreId(storeId);// ���̱��
                    platOrder.setStoreName(storeRecord.getStoreName());// ��������
                    platOrder.setPayTime(orderInfo.get("pay_success_time").asText());// ����ʱ��
                    platOrder.setTradeDt(orderInfo.get("order_time").asText());// �µ�ʱ��
                    platOrder.setIsAudit(0);// ���Ƿ���ˣ�0δ��ˣ�1���
                    platOrder.setGoodNum(goodNum);// ��Ʒ������
                    platOrder.setDeliveryType("");// �����ͻ�����
                    platOrder.setVenderId("");// �����̼�ID�̼ұ���
                    platOrder.setGoodMoney(new BigDecimal(orderInfo.get("order_origin_price").asDouble()));// �����ܽ��
                    platOrder.setPayMoney(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// ���ʵ�ʸ�����
                    ////System.err.println("aaaaa:"+orderInfo.get("order_buyer_remark").asText());
                    platOrder.setBuyerNote(StringUtils.isEmpty(orderInfo.get("order_buyer_remark").asText()) || orderInfo.get("order_buyer_remark").asText().equals("null") ? "" : orderInfo.get("order_buyer_remark").asText());// ��ұ�ע
                    platOrder.setSellerNote(StringUtils.isEmpty(orderInfo.get("order_business_remark").asText()) || orderInfo.get("order_business_remark").asText().equals("null") ? "" : orderInfo.get("order_business_remark").asText());// �̼ұ�ע
                    platOrder.setBuyerId(orderInfo.get("buyer_account").asText());// ��һ�Ա��
                    platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// ������
                    //JsonNode consigneeInfo = orderInfo.get("consigneeInfo");

                    platOrder.setRecName(orderInfo.get("receiver_name").asText());// ���ջ�������
                    platOrder.setRecMobile(orderInfo.get("receiver_phone").asText());// ���ջ����ֻ���
                    platOrder.setProvince(orderInfo.get("receiver_province_name").asText());// �ջ���ʡ
                    platOrder.setProvinceId("");// �ջ���ʡid
                    platOrder.setCity(orderInfo.get("receiver_city_name").asText());// �ջ�����
                    platOrder.setCityId("");// �ջ�����id
                    platOrder.setCounty(orderInfo.get("receiver_district_name").asText());// �ջ�������
                    platOrder.setCountyId("");// �ջ�������id
                    platOrder.setRecAddress(platOrder.getProvince() + platOrder.getCity() + platOrder.getCounty() + orderInfo.get("receiver_address_detail").asText());// ���ջ�����ϸ��ַ
                    platOrder.setDeliverWhs("");//�����ֿ�
                    platOrder.setSellTypId("1");// ��������������ͨ����
                    platOrder.setBizTypId("2");// ����ҵ������2c
                    platOrder.setRecvSendCateId("6");// �����շ����

                    platOrder.setIsInvoice(orderInfo.get("need_invoice").asInt());// ���Ƿ�Ʊ
                    platOrder.setInvoiceTitle(orderInfo.get("invoice_title").asText());// ��Ʊ̧ͷ
                    if (orderInfo.get("order_status").asText().equals("1")) {
                        //����״̬Ϊ1δ����
                        platOrder.setIsShip(0);// ���Ƿ񷢻�
                    } else {
                        platOrder.setIsShip(1);// ���Ƿ񷢻�
                        try {
                            //�ѷ�����Ҫ��ȡ��ݵ���
                            platOrder.setExpressNo(
                                    orderInfo.get("order_expresses").get(0).get("express_no").asText());
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                    platOrder.setDeliverSelf(1);// ���ö���Ϊ�Է�������
                    platOrder.setAdjustMoney(BigDecimal.ZERO);//���ҵ������

                    platOrder.setFreightPrice(new BigDecimal(orderInfo.get("express_fee").asDouble()));// �˷�
                    platOrder.setNoteFlag(0);// ���̼ұ�ע���ģ�Ĭ��ֵ����ʾ������
                    platOrder.setIsClose(0);// ���Ƿ�ر�
                    platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("coupon_amount").asDouble()));
                    platOrder.setOrderStatus(0);// ������״̬
                    platOrder.setReturnStatus(0);// ���˻�״̬
                    platOrder.setDownloadTime(sdf.format(new Date()));// ��������ʱ��
                    //platOrdersList.addAll(checkInvty(platOrder, platOrdersList1, couponDetails));

                    platOrderList.add(platOrder);
                }
            }
            if (platOrderList.size() > 0) {
                platOrderDao.insertList(platOrderList);
                platOrdersDao.insert(platOrdersList);
            }
            if (invoiceList.size() > 0) {
                invoiceDao.insert(invoiceList);
            }
            if (couponList.size() > 0) {
                couponDetailDao.insert(couponList);
            }
            downloadCount += platOrderList.size();

            // ����������ɺ��ж��Ƿ��Զ�����
            for (int i = 0; i < platOrderList.size(); i++) {
                //ִ���Զ�ƥ��
                autoMatch(platOrderList.get(i).getOrderId(), userId);
            }

            if (jdRespJson.get("kaola_order_search_response").get("total_count").asInt() - pageNo * pageSize > 0) {
                resp = KaoLaDownload(userId, startDate, endDate, pageNo + 1, pageSize, storeId, orderState);
            } else if (orderState.equals("1")) {
                resp = KaoLaDownload(userId, startDate, endDate, 1, pageSize, storeId, "2");
            } else if (orderState.equals("2")) {
                resp = KaoLaDownload(userId, startDate, endDate, 1, pageSize, storeId, "3");
            } else {
                // ��־��¼
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                MisUser misUser = misUserDao.select(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent("�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + downloadCount + "��");
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
                logRecord.setTypeName("�Զ�����");
                logRecordDao.insert(logRecord);
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, "���γɹ�����" + downloadCount + "������", null);
                return resp;
            }

            // resp=resultJson.toString();
        } catch (Exception e) {
            logger.error("URL��ec/platOrder/download �쳣˵����", e);
            try {
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //throw new RuntimeException();

        }

        return resp;
    }

    /**
     * ����������������
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param storeId
     * @return
     */
    private String KaoLaDownloadByOrderId(String userId, String startDate, String endDate, String storeId, String ecOrderId) {
        String resp = "";
        String message = "";
        boolean success = true;
        String downloadTime = sdf.format(new Date());
        try {
            // String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            // Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            ObjectNode objectNode = JacksonUtil.getObjectNode("");
            objectNode.put("order_id", "" + ecOrderId);

            String json = objectNode.toString();
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            String jdRespStr = ECHelper.getKaola("kaola.order.get", storeSettings, json);
            ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
            ////System.err.println("��ѯ�����"+jdRespJson);

            // �ж������Ƿ���ִ����д���ʱֱ�ӷ��ء�
            if (jdRespJson.has("error_response")) {
                message = jdRespJson.get("error_response").get("subErrors").get(0).get("msg").asText();
                success = false;
                resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", success, "����ʧ�ܣ�" + message, null);
                return resp;
            }
            //����״̬1(�Ѹ���)��2(�ѷ���)��3(��ǩ��)
            if (jdRespJson.get("kaola_order_get_response").get("order").get("order_status").asText().equals("1") ||
                    jdRespJson.get("kaola_order_get_response").get("order").get("order_status").asText().equals("2") ||
                    jdRespJson.get("kaola_order_get_response").get("order").get("order_status").asText().equals("3")) {


                List<PlatOrder> platOrderList = new ArrayList();
                List<PlatOrders> platOrdersList = new ArrayList();
                List<InvoiceInfo> invoiceList = new ArrayList();
                List<CouponDetail> couponList = new ArrayList();
                // System.out.println(resultJson);
                JsonNode orderInfo = jdRespJson.get("kaola_order_get_response").get("order");
                // ���ж϶������Ƿ��Ѿ�����
                ////System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
                if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
                    PlatOrder platOrder = new PlatOrder();
                    int goodNum = 0;
                    String orderId = getOrderNo.getSeqNo("ec", userId);

                    // ������ϸlist
                    ArrayNode itemInfoList = (ArrayNode) orderInfo.get("order_skus");
                    // �����Ż���ϸ
                    List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
                    platOrder.setEcOrderId(orderInfo.get("order_id").asText());// ���̶�����
                    List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
                    for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
                            .hasNext(); ) {
                        JsonNode itemInfo = itemInfoInfoIterator.next();
                        PlatOrders platOrders = new PlatOrders();
                        platOrders.setGoodId(itemInfo.get("barcode").asText());// ������Ʒ����
                        platOrders.setGoodNum(itemInfo.get("count").asInt());// ��Ʒ����
                        platOrders.setGoodName(itemInfo.get("product_name").asText());// ƽ̨��Ʒ����
                        platOrders.setGoodPrice(new BigDecimal(itemInfo.get("origin_price").asDouble()));// ��Ʒ���� ������

                        platOrders.setGoodSku("");// ��Ʒsku
                        platOrders.setOrderId(orderId);
                        platOrders.setEcOrderId(platOrder.getEcOrderId());

                        platOrders.setGoodMoney(platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// ��Ʒ���
                        platOrders.setDeliverWhs("");// ������ϸ�еķ�����id
                        platOrders.setPayMoney(new BigDecimal(itemInfo.get("real_totle_price").asDouble()));
                        platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("activity_totle_amount").asDouble()));

                        platOrdersList.add(platOrders);

                        //platOrder.setHasGift(0);// ���Ƿ���Ʒ��0������1��
                        goodNum += platOrders.getGoodNum();
                    }
                    StoreRecord storeRecord = storeRecordDao.select(storeId);
                    platOrder.setOrderId(orderId);// �������
                    platOrder.setStoreId(storeId);// ���̱��
                    platOrder.setStoreName(storeRecord.getStoreName());// ��������
                    platOrder.setPayTime(orderInfo.get("pay_success_time").asText());// ����ʱ��
                    platOrder.setTradeDt(orderInfo.get("order_time").asText());// �µ�ʱ��
                    platOrder.setIsAudit(0);// ���Ƿ���ˣ�0δ��ˣ�1���
                    platOrder.setGoodNum(goodNum);// ��Ʒ������
                    platOrder.setDeliveryType("");// �����ͻ�����
                    platOrder.setVenderId("");// �����̼�ID�̼ұ���
                    platOrder.setGoodMoney(new BigDecimal(orderInfo.get("order_origin_price").asDouble()));// �����ܽ��
                    platOrder.setPayMoney(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// ���ʵ�ʸ�����
                    ////System.err.println("aaaaa:"+orderInfo.get("order_buyer_remark").asText());
                    platOrder.setBuyerNote(StringUtils.isEmpty(orderInfo.get("order_buyer_remark").asText()) || orderInfo.get("order_buyer_remark").asText().equals("null") ? "" : orderInfo.get("order_buyer_remark").asText());// ��ұ�ע
                    platOrder.setSellerNote(StringUtils.isEmpty(orderInfo.get("order_business_remark").asText()) || orderInfo.get("order_business_remark").asText().equals("null") ? "" : orderInfo.get("order_business_remark").asText());// �̼ұ�ע
                    platOrder.setBuyerId(orderInfo.get("buyer_account").asText());// ��һ�Ա��
                    platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// ������
                    //JsonNode consigneeInfo = orderInfo.get("consigneeInfo");
                    platOrder.setRecAddress(orderInfo.get("receiver_address_detail").asText());// ���ջ�����ϸ��ַ
                    platOrder.setRecName(orderInfo.get("receiver_name").asText());// ���ջ�������
                    platOrder.setRecMobile(orderInfo.get("receiver_phone").asText());// ���ջ����ֻ���
                    platOrder.setProvince(orderInfo.get("receiver_province_name").asText());// �ջ���ʡ
                    platOrder.setProvinceId("");// �ջ���ʡid
                    platOrder.setCity(orderInfo.get("receiver_city_name").asText());// �ջ�����
                    platOrder.setCityId("");// �ջ�����id
                    platOrder.setCounty(orderInfo.get("receiver_district_name").asText());// �ջ�������
                    platOrder.setCountyId("");// �ջ�������id
                    platOrder.setDeliverWhs("");//�����ֿ�
                    platOrder.setSellTypId("1");// ��������������ͨ����
                    platOrder.setBizTypId("2");// ����ҵ������2c
                    platOrder.setRecvSendCateId("6");// �����շ����

                    platOrder.setIsInvoice(orderInfo.get("need_invoice").asInt());// ���Ƿ�Ʊ
                    platOrder.setInvoiceTitle(orderInfo.get("invoice_title").asText());// ��Ʊ̧ͷ
                    /*
                     * System.out.println(orderInfo.get("order_status").asText());
                     * System.out.println(orderInfo.get("order_expresses").get(0).get("express_no").
                     * asText());
                     */
                    if (orderInfo.get("order_status").asText().equals("1")) {
                        //����״̬Ϊ1δ����
                        platOrder.setIsShip(0);// ���Ƿ񷢻�
                    } else {
                        platOrder.setIsShip(1);// ���Ƿ񷢻�
                        try {
                            //�ѷ�����Ҫ��ȡ��ݵ���
                            platOrder.setExpressNo(
                                    orderInfo.get("order_expresses").get(0).get("express_no").asText());
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                    platOrder.setDeliverSelf(1);// ���ö���Ϊ�Է�������
                    platOrder.setAdjustMoney(BigDecimal.ZERO);//���ҵ������

                    platOrder.setFreightPrice(new BigDecimal(orderInfo.get("express_fee").asDouble()));// �˷�
                    platOrder.setNoteFlag(0);// ���̼ұ�ע���ģ�Ĭ��ֵ����ʾ������
                    platOrder.setIsClose(0);// ���Ƿ�ر�
                    platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("coupon_amount").asDouble()));
                    platOrder.setOrderStatus(0);// ������״̬
                    platOrder.setReturnStatus(0);// ���˻�״̬
                    platOrder.setDownloadTime(downloadTime);// ��������ʱ��
                    //platOrdersList.addAll(checkInvty(platOrder, platOrdersList1, couponDetails));

                    platOrderList.add(platOrder);
                }
                if (platOrderList.size() > 0) {
                    platOrderDao.insertList(platOrderList);
                    platOrdersDao.insert(platOrdersList);
                }
                if (invoiceList.size() > 0) {
                    invoiceDao.insert(invoiceList);
                }
                if (couponList.size() > 0) {
                    couponDetailDao.insert(couponList);
                }
                downloadCount += platOrderList.size();
                // ����������ɺ��ж��Ƿ��Զ�����
                for (int i = 0; i < platOrderList.size(); i++) {
                    //ִ���Զ�ƥ��
                    autoMatch(platOrderList.get(i).getOrderId(), userId);
                }

            }

            // ��־��¼
            LogRecord logRecord = new LogRecord();
            logRecord.setOperatId(userId);
            MisUser misUser = misUserDao.select(userId);
            if (misUser != null) {
                logRecord.setOperatName(misUser.getUserName());
            }
            logRecord.setOperatContent("�����ֹ����ص��̣�" + storeSettings.getStoreName() + "����" + downloadCount + "��");
            logRecord.setOperatTime(downloadTime);
            logRecord.setOperatType(9);// 9�ֹ����� 10 �Զ�����
            logRecord.setTypeName("�ֹ�����");
            logRecord.setOperatOrder(ecOrderId);//�����ĵ���
            logRecordDao.insert(logRecord);
            resp = BaseJson.returnRespObj("ec/platOrder/download", success, "���γɹ�����" + downloadCount + "������", null);
            return resp;

            // resp=resultJson.toString();
        } catch (Exception e) {
            logger.error("URL��ec/platOrder/download �쳣˵����", e);
            try {
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //throw new RuntimeException();

        }

        return resp;
    }

    private String tmDownload(Integer ss, String userId, int pageNo, int pageSize, String startDate, String endDate,
                              String storeId) throws Exception {
        String resp = "";
        try {
            // ����
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
//			// ��Ϣ
//			StoreRecord storeRecord = storeRecordDao.select(storeId);

            Map<String, String> map = new HashMap<>();
            map.put("fields", "tid,status");
            map.put("page_no", pageNo + "");
            map.put("page_size", pageSize + "");
//		map.put("use_has_next", "true");
            map.put("start_created", startDate);
            map.put("end_created", endDate);
            String tmRespStr = ECHelper.getTB("taobao.trades.sold.get", storeSettings, map);

            JSONObject tmRespJson = JSONObject.parseObject(tmRespStr);
            if (tmRespJson.containsKey("error_response")) {
                return BaseJson.returnRespObj("ec/platOrder/download", false,
                        tmRespJson.getJSONObject("error_response").getString("msg"), null);
            }
            JSONObject resultJson = tmRespJson.getJSONObject("trades_sold_get_response");

            JSONArray orderInfoList = resultJson.getJSONObject("trades").getJSONArray("trade");
            for (int i = 0; i < orderInfoList.size(); i++) {
                JSONObject orderInfo = orderInfoList.getJSONObject(i); // ���� jsonarray ���飬��ÿһ������ת�� json ����
                System.err.println(orderInfo.getString("status"));
                if (orderInfo.getString("status").equals("WAIT_SELLER_SEND_GOODS")
                        || orderInfo.getString("status").equals("WAIT_BUYER_CONFIRM_GOODS")
                        || orderInfo.getString("status").equals("TRADE_FINISHED")) {
                    // WAIT_SELLER_SEND_GOODS���ȴ����ҷ���
                    // WAIT_BUYER_CONFIRM_GOODS���ȴ����ȷ���ջ�
                    // TRADE_FINISHED�����׳ɹ�
                    if (platOrderDao.checkExsits(orderInfo.getString("tid"), storeId) == 0) {
                        tmdownloadByOrderId(orderInfo.getString("tid"), userId, storeId);
                        ss++;
                    }

                }

            }

            if (resultJson.getLong("total_results") > pageNo * pageSize) {
                tmDownload(ss, userId, pageNo + 1, pageSize, startDate, endDate, storeId);
            }
            resp = BaseJson.returnRespObj("ec/platOrder/download", true, "���³ɹ�" + ss + "��", null);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp = BaseJson.returnRespObj("ec/platOrder/download", false, "����ʧ��", null);
        }
        return resp;
    }

    private String tmDownloadSdk(Map<String, Integer> ss, String userId, int pageNo, int pageSize, String startDate, String endDate,
                                 String storeId) throws Exception {
        String resp = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // ����
            StoreSettings storeSettings = storeSettingsDao.select(storeId);

            TradesSoldGetRequest req = new TradesSoldGetRequest();
            req.setFields("tid,status");
            req.setPageNo(Long.valueOf(pageNo));
            req.setPageSize(Long.valueOf(pageSize));
            req.setStartCreated(dateFormat.parse(startDate));
            req.setEndCreated(dateFormat.parse(endDate));
            // ת��
            Map<String, String> map = new HashMap<String, String>();
            map.put("path", TradesSoldGetRequest.class.getName());
            map.put("taobaoObject", JSON.toJSONString(req));
            String taobao = ECHelper.getTB("", storeSettings, map);
            TradesSoldGetResponse rsp = JSONObject.parseObject(taobao, TradesSoldGetResponse.class);

            logger.info("��èbody:" + rsp.getBody());
            if (!rsp.isSuccess()) {
                logger.error("��èerr:" + rsp.getMsg());
                return BaseJson.returnRespObj("ec/platOrder/download", false, rsp.getMsg(), null);
            }

            List<CouponDetail> couponList = new ArrayList<CouponDetail>();
            List<PlatOrder> platOrderList = new ArrayList<>();

            for (Trade trade : rsp.getTrades()) {
                if (trade.getStatus().equals("WAIT_SELLER_SEND_GOODS")
                        || trade.getStatus().equals("WAIT_BUYER_CONFIRM_GOODS")
                        || trade.getStatus().equals("TRADE_FINISHED")) {
                    // WAIT_SELLER_SEND_GOODS���ȴ����ҷ���
                    // WAIT_BUYER_CONFIRM_GOODS���ȴ����ȷ���ջ�
                    // TRADE_FINISHED�����׳ɹ�
                    if (platOrderDao.checkExsits(trade.getTid().toString(), storeId) == 0) {

                        tmdownloadByOrderIdSdk(trade.getTid().toString(), userId, storeId, platOrderList, couponList);

                        ss.put("keys", ss.get("keys") + 1);

                    }
                }
            }


            //��֧�����䶩����
            List<String> seqNoList = getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId);
            downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId, seqNoList);


            resp = BaseJson.returnRespObj("ec/platOrder/download", true, "���³ɹ�" + ss.get("keys") + "��", null);
            if (rsp.getTotalResults() > pageNo * pageSize) {
                resp = tmDownloadSdk(ss, userId, pageNo + 1, pageSize, startDate, endDate, storeId);
            } else {
                // ��־��¼
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                MisUser misUser = misUserDao.select(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent("�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + ss.get("keys") + "��");
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
                logRecord.setTypeName("�Զ�����");
                logRecordDao.insert(logRecord);
                return resp;
            }

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp = BaseJson.returnRespObj("ec/platOrder/download", false, "����ʧ��", null);
        }
        return resp;
    }

    private String tmdownloadByOrderIdSdk(String tid, String userId, String storeId, List<PlatOrder> platOrderList, List<CouponDetail> couponList) throws IOException {
        String resp = "";
        String message = "";
        if (platOrderDao.checkExsits(tid, storeId) != 0) {
            return resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, tid + "�����Ѵ���", null);
        }
        try {
            // ����
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            // ��Ϣ
            StoreRecord storeRecord = storeRecordDao.select(storeId);

            TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
            req.setTid(Long.valueOf(tid));
            req.setFields(
                    "logistics_company,invoice_name,tid,type,status,payment,est_con_time,orders,promotion_details,pay_time,total_fee,buyer_message,seller_memo,receiver_address,receiver_name,receiver_mobile,seller_flag,adjust_fee,discount_fee,created,received_payment,receiver_state,post_fee,receiver_city,receiver_district,num");

            // ת��
            Map<String, String> map = new HashMap<String, String>();
            map.put("path", TradeFullinfoGetRequest.class.getName());
            map.put("taobaoObject", JSON.toJSONString(req));
            String taobao = ECHelper.getTB("", storeSettings, map);
            TradeFullinfoGetResponse rsp = JSONObject.parseObject(taobao, TradeFullinfoGetResponse.class);
            logger.info("��èbody:" + rsp.getBody());
            if (!rsp.isSuccess()) {
                logger.error("��èerr:" + rsp.getMsg());
                return BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", false, rsp.getMsg(), null);
            }
            Trade trade = rsp.getTrade();
//			System.out.println(trade.getStatus());
            if (!trade.getStatus().equals("WAIT_SELLER_SEND_GOODS")
                    && !trade.getStatus().equals("WAIT_BUYER_CONFIRM_GOODS")
                    && !trade.getStatus().equals("TRADE_FINISHED")) {
                return BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", false, "�����ض���״̬������Ҫ��", null);
            }

            //��ȡ������
//			String orderId = getOrderNo.getSeqNo("ec", userId);

            PlatOrder platOrder = new PlatOrder();

            platOrder.setEcOrderId(trade.getTid().toString());// ���̶�����
//			platOrder.setOrderId(orderId); // �������
            platOrder.setStoreId(storeId); // ���̱��
            platOrder.setStoreName(storeRecord.getStoreName()); // '��������' 1
            if (trade.getPayTime() == null) {

            } else {
                platOrder.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(trade.getPayTime())); // ����ʱ��' t
            }
            // paytime
//					platOrder.setWaif(waif);		//	'���'
            platOrder.setIsAudit(0); // '�Ƿ����'
//					platOrder.setAuditHint(auditHint); // '�����ʾ'
            platOrder.setGoodMoney(new BigDecimal(trade.getTotalFee() == null ? "0" : trade.getTotalFee())); // ��Ʒ���' t
            // totalfee
            platOrder.setPayMoney(new BigDecimal(trade.getPayment() == null ? "0" : trade.getPayment())); // 'ʵ�����' t
            // payment
            platOrder.setBuyerNote(trade.getBuyerMessage()); // �������' t buyermessage
            platOrder.setSellerNote(trade.getSellerMemo()); // ���ұ�ע'
            platOrder.setRecAddress(trade.getReceiverAddress()); // �ջ�����ϸ��ַ' t receiveraddress
//			platOrder.setBuyerId(buyerId); // '��һ�Ա��'
            platOrder.setRecName(trade.getReceiverName()); // �ջ�������' t receivername
            platOrder.setRecMobile(trade.getReceiverMobile()); // �ջ����ֻ���' t receivermobile
            platOrder.setIsInvoice(0); // '�Ƿ�Ʊ'
//			platOrder.setInvoiceTitle(resultJsons.getString("invoice_name")); // '��Ʊ̧ͷ'
            platOrder.setNoteFlag(trade.getSellerFlag().intValue()); // ���ұ�ע����' t sellerflag
            platOrder.setIsClose(0); // '�Ƿ�ر�'
            platOrder.setAdjustMoney(new BigDecimal(trade.getAdjustFee() == null ? "0" : trade.getAdjustFee())); // ���ҵ������'
            // t
            // adjustfee
            platOrder.setDiscountMoney(new BigDecimal(trade.getDiscountFee() == null ? "0" : trade.getDiscountFee())); // ϵͳ�Żݽ��'
            // t
            // discountfee
            platOrder.setOrderStatus(0); // '����״̬'
            platOrder.setReturnStatus(0); // '�˻�״̬'
            // platOrder.setHasGift(0); // '�Ƿ���Ʒ'
            // platOrder.setMemo(memo); // '��ע'
            // platOrder.setAdjustStatus(adjustStatus); // '������ʶ'
            platOrder.setTradeDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(trade.getCreated())); // '����ʱ��' t
            // created
            platOrder.setSellTypId("1");// ��������������ͨ����
            platOrder.setBizTypId("2");// ����ҵ������2c
            platOrder.setRecvSendCateId("6");// �����շ����
            if (trade.getStatus().equals("WAIT_SELLER_SEND_GOODS")) {
                platOrder.setIsShip(0); // '�Ƿ񷢻�'
            } else {
                platOrder.setIsShip(1); // '�Ƿ񷢻�'
                platOrder.setExpressNo(trade.getOrders().get(0).getInvoiceNo());
            }
//			platOrder.setRecvSendCateId(recvSendCateId); // '�շ������'
            platOrder.setOrderSellerPrice(
                    new BigDecimal(trade.getReceivedPayment() == null ? "0" : trade.getReceivedPayment())); // ���������ʵ�������'
            // t
            // receivedpayment
            platOrder.setProvince(trade.getReceiverState()); // ʡ' t receiverstate
            // platOrder.setProvinceId(provinceId); // 'ʡid'
            platOrder.setCity(trade.getReceiverCity()); // ��' t receivercity
            // platOrder.setCityId(cityId); //
            platOrder.setCounty(trade.getReceiverDistrict()); // ��' t receiverdistrict
            // platOrder.setCountyId(countyId); //
            // platOrder.setTown(town); // '��'
            // platOrder.setTownId(townId); // '��id'
            platOrder.setFreightPrice(new BigDecimal(trade.getPostFee() == null ? "0" : trade.getPostFee())); // �˷�' t
            // postfee
            platOrder.setVenderId("TM"); // '�̼�id'

            // orders Order[]
            platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // platOrder.setDeliveryType("����ʱ��");
            // //'�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����;2-ֻ˫���ա������ͻ�(�����ղ�����;3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱
            // ��'

			List<PlatOrders> list = new ArrayList<PlatOrders>();

			for (com.taobao.api.domain.Order order : trade.getOrders()) {
				PlatOrders platOrders = new PlatOrders();
				platOrders.setGoodId(order.getNumIid().toString()); // ƽ̨��Ʒ��� ��Ʒ����ID
				platOrders.setGoodName(order.getTitle()); // 'ƽ̨��Ʒ����' t title String ɽկ����Ի��� ��Ʒ����
				platOrders.setGoodNum(order.getNum().intValue()); // '��Ʒ����' t num Number 1 ����������ȡֵ��Χ:�����������

				platOrders.setGoodMoney(new BigDecimal(order.getNum()).multiply(new BigDecimal(order.getPrice()))); // ��Ʒ���'
				if (trade.getOrders().size() == 1) {
					platOrders.setPayMoney(
							new BigDecimal(order.getDivideOrderFee()== null ? "0" : order.getDivideOrderFee()).subtract(new BigDecimal(trade.getPostFee()))); // 'ʵ�����'��̯֮���ʵ�����
					// ��ȡ����Order�е�payment�ֶ��ڵ����Ӷ���ʱ�����������ã�����Ӷ���ʱ��������������
				} else {
					platOrders.setPayMoney(new BigDecimal(order.getDivideOrderFee())); // 'ʵ�����'��̯֮���ʵ�����
				}
				platOrders.setGoodSku(order.getSkuId()); // '��Ʒsku'
//				platOrders.setOrderId(orderId); //  x'd'�������' 1
//						platOrders.setBatchNo(batchNo);	//	'����' 	
				platOrders.setExpressCom(order.getLogisticsCompany()); // ��ݹ�˾' t�Ӷ��������Ŀ�ݹ�˾����
//						promotion_details		platOrders.setProActId(proActId);	//	��������' t promotionid	String	mjs	�Ż�id
//						platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("discount_fee").asDouble())); // ϵͳ�Żݽ��'
				platOrders.setAdjustMoney(new BigDecimal(order.getAdjustFee() == null ? "0" : order.getAdjustFee())); // ��ҵ������'
																														// t
				platOrders.setMemo(order.getOid().toString()); // '��ע' �� �ӵ�id oid
				platOrders.setGoodPrice(new BigDecimal(order.getPrice() == null ? "0" : order.getPrice())); // '��Ʒ����' t
																											// price
				platOrders.setDeliverWhs(order.getStoreCode()); // �����ֿ����'
				platOrders.setEcOrderId(trade.getTid().toString()); // 'ƽ̨������' t oid

				if (order.getShipper() == null) {
					platOrder.setDeliverSelf(1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t
				} else if (order.getShipper().equals("cn") || order.getShipper().equals("hold")) {
					platOrder.setDeliverSelf(0); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t
					platOrder.setDeliverWhs(order.getStoreCode());//�����Ĳֿ����
					Map map11 = new HashMap();
					map11.put("type", "TM");
					map11.put("online", platOrder.getDeliverWhs());
					String newWhs1 = PlatWhsMappDao.select(map11);
					if (newWhs1 != null) {
						platOrder.setDeliverWhs(newWhs1);
					} else {
						platOrder.setDeliverWhs("");
						platOrder.setAuditHint("ƽ̨�ַ�������ȱ��ƽ̨��ӳ��,����ƽ�ֱ��룺"+order.getStoreCode());
					}
					

				} else {
					platOrder.setDeliverSelf(1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t
				}
				// �ֶ� shipper ��cn:����ַ�����seller:�̼Ҳַ�����hold:��ȷ�Ϸ����ֿ⣬null(��ֵ�����̼Ҳַ�����
				////System.err.println("itemInfo.get(\"shipper\")" + order.getShipper());
//						platOrders.setInvId(itemInfo.get("outer_iid") == null ? null : itemInfo.get("outer_iid").asText()); // �������'// �̼��ⲿ����

				list.add(platOrders);
			}


            if (trade.getPromotionDetails() != null) {

//				JSONArray promotionDetails = resultJsons.getJSONObject("promotion_details")
//						.getJSONArray("promotion_detail");
				for (PromotionDetail promotionDetail : trade.getPromotionDetails()) {
					CouponDetail coupon1 = new CouponDetail();
					coupon1.setPlatId("TM");// ƽ̨id������JD����èJD
					coupon1.setStoreId(storeId);// ����id
					coupon1.setOrderId(trade.getTid().toString());// ������
					coupon1.setSkuId(null);
					coupon1.setCouponCode(null);// �Ż����ͱ���
					coupon1.setCouponType(promotionDetail.getPromotionName());// �Ż�����
					coupon1.setCouponPrice(new BigDecimal(
							promotionDetail.getDiscountFee() == null ? "0" : promotionDetail.getDiscountFee()));// �Żݽ��
					couponList.add(coupon1);
				}
			}
            platOrder.setGoodNum(list.size()); // ��ƷƷ����
            logger.info("list" + list.size());
			//��װ�ӱ�
			platOrder.setPlatOrdersList(list);
			//��װ����list����
            platOrderList.add(platOrder);

			//��֧�����䶩����
			//downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId);

			/*
			 * //���ɶ����� + �������ݿ� List<String> seqNoList =
			 * getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId); //���붩�� +
			 * �Զ�ƥ�� downloadCount =
			 * platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList,
			 * null, downloadCount, userId, seqNoList);
			 */

/*            couponDetailDao.insert(couponList);
			platOrderDao.insert(platOrder);
			platOrdersDao.insert(list);
			//ִ���Զ�ƥ��
			autoMatch(platOrder.getOrderId(), userId);*/
			
			
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "�������سɹ�" + tid, null);

		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "��������ʧ��" + tid, null);
		}
		return resp;

	}

	@Override
	public List<PlatOrders> jisuanPayPrice(String orderid) {
		// TODO Auto-generated method stub
		PlatOrder platOrder = new PlatOrder();
		List<PlatOrders> orderslist1 = new ArrayList<>();
		// �ȸ���orderid��ȡ�������ӱ���Ϣ
		platOrder = platOrderDao.select(orderid);
		List<PlatOrders> orderslist = platOrdersDao.select(orderid);
		BigDecimal sellerprice = platOrder.getOrderSellerPrice();// ������
		StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
		BigDecimal money1 = platOrder.getOrderSellerPrice().subtract(platOrder.getFreightPrice());// ������-�˷ѣ�����Ϊ��ϸ��ʵ�����ĺ�
		if (orderslist.size() == 1) {
			// ����ϸ������ֻ��һ����¼ʱ�������Ľ�����-�˷Ѿ��Ǹ�����¼��ʵ�����
			PlatOrders orders = orderslist.get(0);
			orders.setPayMoney(money1);
			orderslist1.add(orders);
		} else {
			DecimalFormat df1 = new DecimalFormat("0.00");
			// ��ѯ�����Ż�
			BigDecimal couponprice100 = new BigDecimal(
					couponDetailDao.selectByOrderId(storeRecord.getEcId(), orderid, "", 2));
			BigDecimal money2 = new BigDecimal("0");// �ѷ����ȥ�Ľ����һ��ʱ�����õ�
			for (int i = 0; i < orderslist.size(); i++) {
				// �����ӱ��skuȥ�Żݱ������ѯ�Ƿ��е�Ʒ�Ż�
				// ���Ż���Ϣʱ
				PlatOrders orders = orderslist.get(i);
				// ��ѯ��Ʒ����
				BigDecimal couponprice1 = new BigDecimal(
						couponDetailDao.selectByOrderId(storeRecord.getEcId(), orderid, orders.getGoodSku(), 1));

				if (couponprice100.compareTo(BigDecimal.ZERO) == 0) {
					// ��������Ż�Ϊ0ʱ��
					// ��ϸ��ʵ�����͵�����Ʒ���-��Ʒ�����Ľ��
					orders.setPayMoney(orders.getGoodMoney().subtract(couponprice1));
					orderslist1.add(orders);
				} else {
					// �������Żݽ�Ϊ0ʱ
					// ��Ҫ����Ʒ����������ÿ����ϸ�Ľ����һ����ϸ��ʵ������ü���
					if (i + 1 == orderslist.size()) {
						// �����һ����ϸʱ
						orders.setPayMoney(money1.subtract(money2));
					} else {
						// ���㵱ǰ����ϸ����Ʒ���-��Ʒ�Żݺ�Ľ��
						BigDecimal currentMoney = orders.getGoodMoney().subtract(couponprice1);
						// ���������̯������
						BigDecimal current = new BigDecimal(df1.format(money1.multiply(currentMoney.divide(money1))));// ��ǰ��ϸ���䵽��ʵ�����
						money2.add(current);
						orders.setPayMoney(current);
					}
					orderslist1.add(orders);
				}
			}
		}
		return orderslist1;
	}

	@Override
	public List<PlatOrders> checkInvty(PlatOrder platOrder, List<PlatOrders> platOrdersList,
									   List<CouponDetail> couponDetails) {
		// TODO Auto-generated method stub
		// ��������ϸ�е�sku��spu�һ���ϵͳ�Ĵ�����룬�����PTO���͵���Ҫ�����б��� �����������Լ�ÿ����ϸ��ʵ�����,����
		List<PlatOrders> platOrders123 = platOrdersList;// ����listװ�����ԭʼ�б������������쳣ʱ��ֱ�ӷ��ش�list��

		List<PlatOrders> orderslist = new ArrayList<PlatOrders>();
		// ���ptoǰӦ�ȼ���ÿ����ϸ��ʵ�����
		// ��һ��ѭ���۳���Ʒ�����Żݽ��
		List<CouponDetail> coupons = new ArrayList<CouponDetail>();//������ʱ��ŵ�Ʒsku�Żݽ��ƥ���Ϻ�ʹ��Ż��б����Ƴ������ⶩ���к������ͬsku��ϸ
		for (int i = 0; i < platOrdersList.size(); i++) {
			// BigDecimal sellerprice = platOrder.getOrderSellerPrice();// ������
			// BigDecimal money1 =
			// platOrder.getOrderSellerPrice().subtract(platOrder.getFreightPrice());//
			// ���������Ϊ��ϸ��ʵ�����ĺ�
			// ��ֹһ����ϸʱ����Ҫ����ÿ����ϸ��ʵ�����
			// ����Ż���ϸû�����ݣ�ֱ�ӵ��۳�����
			if (couponDetails.size() == 0) {
				platOrdersList.get(i).setPayMoney(platOrdersList.get(i).getGoodMoney());// û���Ż�ʱ����Ʒ���͵���ʵ�����

			} else {
				// ����Ե�Ʒ�Żݿۼ�������ڿۼ���Ļ����ϰ�������̯�����Żݽ��
				for (int j = 0; j < couponDetails.size(); j++) {
					// �ж��Ż���ϸ��sku�Ƿ��붩����ϸ��ǰ����һ�£���һ�¾��޸�
					if (couponDetails.get(j).getSkuId().equals(platOrdersList.get(i).getGoodSku())) {
						if (platOrdersList.get(i).getGoodMoney().compareTo(BigDecimal.ZERO) > 0) {
							
							// ��Ʒʵ�������ڻ�Ʒ����ȥ��Ʒ�Żݽ��
							platOrdersList.get(i).setPayMoney(platOrdersList.get(i).getPayMoney()
									.subtract(couponDetails.get(j).getCouponPrice()));
							platOrdersList.get(i).setDiscountMoney(
									(platOrdersList.get(i).getDiscountMoney() == null ? BigDecimal.ZERO
											: platOrdersList.get(i).getDiscountMoney())
											.add(couponDetails.get(j).getCouponPrice()));
							coupons.add(couponDetails.get(j));
							couponDetails.remove(j);
							break;
						}
					}
				}
			}

		}
		if(coupons.size()>0) {
			couponDetails.addAll(coupons);
		}
		// ���������Ż��ܽ��
		BigDecimal discountall = new BigDecimal("0");
		for (int i = 0; i < couponDetails.size(); i++) {

			// 20-��װ�Ż� 1
			// 28-�����Ż� 1
			// 29-�Ź��Ż� 1
			// 30-��Ʒ�����Ż� 1
			// 34-�ֻ����
			// 35-��������(����) 1
			// 39-�����Ż�
			// 41-����ȯ�Ż�
			// 52-��Ʒ���Ż�
			// 100-�����Ż� 1
			// ���Ż����Ͳ�������������ʱ�������ۼ������Żݽ��
			if (!couponDetails.get(i).getCouponCode().equals("20") &&!couponDetails.get(i).getCouponCode().equals("30") && !couponDetails.get(i).getCouponCode().equals("34")
					&& !couponDetails.get(i).getCouponCode().equals("39")
					&& !couponDetails.get(i).getCouponCode().equals("41")
					&& !couponDetails.get(i).getCouponCode().equals("52")) {
				discountall = discountall.add(couponDetails.get(i).getCouponPrice());
			}
		}
		// �۳�ÿ����ϸռ�����Żݵı���
		// �����з�̯ʱ��Ҫ�ȹ��˵�����ʵ�����Ϊ0����ϸ��¼����ֹ�����䲻׼ȷ�����

		List<PlatOrders> platOrders1 = new ArrayList<PlatOrders>();
		for (int i = 0; i < platOrdersList.size(); i++) {
			// System.out.println(platOrdersList.get(i).getPayMoney());
			if (platOrdersList.get(i).getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
				// ʵ�����Ϊ0ʱ�ų�
				platOrdersList.get(i).setDiscountMoney(platOrdersList.get(i).getGoodMoney());// ʵ�����Ϊ0ʱ�������Żݽ��Ϊ��Ʒ���
				platOrders1.add(platOrdersList.get(i));
				platOrdersList.remove(i);// ��ʱ�Ƴ�ʵ�����Ϊ0��
				i--;
			}
		}
		if (platOrder.getOrderSellerPrice().compareTo(BigDecimal.ZERO) > 0) {
			int ii = platOrdersList.size();
			BigDecimal hasdiscount = new BigDecimal("0");
			for (int i = 0; i < platOrdersList.size(); i++) {
				if (i + 1 < ii) {
					// ���㵱ǰ��ϸ��Ҫ�ۼ����������Żݽ��
					BigDecimal discountmoney = platOrdersList.get(i).getPayMoney()
							.divide(platOrder.getOrderSellerPrice(), 2, BigDecimal.ROUND_HALF_UP).multiply(discountall);
					// �ۼ��Żݽ��
					platOrdersList.get(i).setPayMoney(platOrdersList.get(i).getPayMoney().subtract(discountmoney));
					// �ۼ��Żݽ��
					platOrdersList.get(i)
							.setDiscountMoney((platOrdersList.get(i).getDiscountMoney() == null ? BigDecimal.ZERO
									: platOrdersList.get(i).getDiscountMoney()).add(discountmoney));
					hasdiscount = hasdiscount.add(discountmoney);
				} else {
					// ���һ����ϸʱ
					// �ۼ��Żݽ��
					// ��ǰ����ϸ��ʵ�����-�������Ż��ܽ��-������ϸ���Żݽ�
					platOrdersList.get(i).setPayMoney(
							platOrdersList.get(i).getPayMoney().subtract(discountall.subtract(hasdiscount)));
					// �ۼ��Żݽ��
					platOrdersList.get(i)
							.setDiscountMoney((platOrdersList.get(i).getDiscountMoney() == null ? BigDecimal.ZERO
									: platOrdersList.get(i).getDiscountMoney()).add(discountall.subtract(hasdiscount)));
				}
			}
		}else {
			//����������Ϊ0ʱ����ϸ�����е���Ʒ
			//Ϊ�˱�����Ʒ�Ķ����Ż���ϸ���ǵ�Ʒ�Ż��ǵ��������Ż�
			//������������Ϊ0ʱ,��������ϸ��ʵ��������ó�0
			//
			for (int i = 0; i < platOrdersList.size(); i++) {
				platOrdersList.get(i).setPayMoney(BigDecimal.ZERO);//ʵ�����Ϊ0��
				platOrdersList.get(i).setPayPrice(BigDecimal.ZERO);//ʵ������Ϊ0
				platOrdersList.get(i).setSellerPrice(BigDecimal.ZERO);//���㵥��Ϊ0
				platOrdersList.get(i).setDiscountMoney(platOrdersList.get(i).getGoodMoney());//�Żݽ�������Ʒ���
				platOrdersList.get(i).setAdjustMoney(BigDecimal.ZERO);//�������Ϊ0
			}

		}
		BigDecimal payMoney = BigDecimal.ZERO;
		for (int i = 0; i < platOrdersList.size(); i++) {
			payMoney = payMoney.add(platOrdersList.get(i).getPayMoney());
		}
		platOrder.setPayMoney(payMoney);
		// ���ո���ʱ�Ƴ�����ӽ�ȥ
		platOrdersList.addAll(platOrders1);
		// ѭ��ƥ����Ʒ����
		/*for (int i = 0; i < platOrdersList.size(); i++) {
			DecimalFormat dFormat = new DecimalFormat("0.00");
			boolean flag = true;
			PlatOrders platOrders = platOrdersList.get(i);
			// ������� ƽ̨��Ʒ��good_record�е�ƽ̨��Ʒ���룻
			String ecgoodid = platOrders.getGoodId();// ƽ̨��Ʒ���
			String goodsku = platOrders.getGoodSku();
			GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
			if (goodRecord == null) {
				platOrder.setAuditHint("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");
				// platOrderDao.update(platOrder);//���¶����������ʾ��
				flag = false;
				orderslist.add(platOrders);
				continue;
			} else if (goodRecord.getGoodId() == null) {
				platOrder.setAuditHint("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");
				// platOrderDao.update(platOrder);//���¶����������ʾ��
				flag = false;
				orderslist.add(platOrders);
				continue;
			} else {
				String invId = goodRecord.getGoodId();
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// ȥ���������ѯ���������Ϣ
				if (invtyDoc == null) {
					platOrder.setAuditHint("�ڴ��������δƥ�䵽��Ӧ�������,��ƥ��Ĵ�����룺" + invId);
					// platOrderDao.update(platOrder);//���¶����������ʾ��
					flag = false;
					orderslist.add(platOrders);
					continue;
				} else {
					if (invtyDoc.getPto() != null) {
						if (invtyDoc.getPto() == 1) {
							// ����PTO���ʹ��
							ProdStru prodtru = prodStruDao.selectMomEncd(invId);
							if (prodtru == null) {
								// δ�ڲ�Ʒ�ṹ�в�ѯ����Ӧ����Ĳ�Ʒ�ṹ��Ϣ
								platOrder.setAuditHint("�ڲ�Ʒ�ṹ��" + invId + "��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");
								// platOrderDao.update(platOrder);//���¶����������ʾ��
								flag = false;
								orderslist.add(platOrders);
								continue;
							} else {
								if (prodtru.getIsNtChk() == 0) {
									// ��Ӧ��Ʒ�ṹ��δ���
									platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��" + prodtru.getMomEncd() + "��δ���");
									// platOrderDao.update(platOrder);//���¶����������ʾ��
									flag = false;
									orderslist.add(platOrders);
									continue;
								} else {
									List<ProdStruSubTab> strucksublist = prodtru.getStruSubList();
									// ѭ����Ʒ�ṹ�ӱ���Ϣ
									if (strucksublist.size() == 0) {
										platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��" + prodtru.getMomEncd() + "û�������Ӽ���ϸ���������á�");
										// platOrderDao.update(platOrder);//���¶����������ʾ��
										flag = false;
										orderslist.add(platOrders);
										continue;
									} else if (strucksublist.size() == 1) {
										// ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
										// �������
										platOrders.setInvId(strucksublist.get(0).getSubEncd());
										// ���ô������
										platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
												.multiply(strucksublist.get(0).getSubQty()).intValue());
										// ���ÿ�������
										platOrders.setCanRefNum(platOrders.getInvNum());
										platOrders.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
										platOrders.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
										// ����ʵ������
										platOrders.setPayPrice(platOrders.getPayMoney().divide(
												(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
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
										for (int j = 0; j < strucksublist.size(); j++) {
											InvtyDoc invtyDoc1 = invtyDocDao
													.selectInvtyDocByInvtyDocEncd(strucksublist.get(j).getSubEncd());
											if (invtyDoc1.getRefCost() == null) {
												flag = false;
												break;
											} else {
												total = total.add(invtyDoc1.getRefCost()
														.multiply(strucksublist.get(j).getSubQty()));
											}
										}
										BigDecimal money123 = new BigDecimal("0");// �����ѷ�ʵ����� ���һ���ü���ʱ�õ�
										BigDecimal money456 = new BigDecimal("0");// ���ò�ֺ����Ʒ�����һ������
										for (int j = 0; j < strucksublist.size(); j++) {
											PlatOrders order = new PlatOrders();
											try {
												order = (PlatOrders) platOrders.clone();
											} catch (CloneNotSupportedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											if (j + 1 < strucksublist.size()) {
												// ����ÿ���Ӽ�ռ�ܳɱ��ı��� ����8λС��
												InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
														strucksublist.get(j).getSubEncd());
												BigDecimal rate = invtyDoc2.getRefCost()
														.multiply(strucksublist.get(j).getSubQty())
														.divide(total, 8, BigDecimal.ROUND_HALF_UP);
												order.setInvNum((new BigDecimal(order.getGoodNum()))
														.multiply(strucksublist.get(j).getSubQty()).intValue());
												order.setPayMoney(new BigDecimal(
														dFormat.format(order.getPayMoney().multiply(rate))));// ����ʵ�����
																												// ������λС��
												// ����ʵ������ ����8λС��
												order.setPayPrice(
														order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																8, BigDecimal.ROUND_HALF_UP));
												order.setSellerPrice(order.getPayPrice());
												// �������
												order.setInvId(strucksublist.get(j).getSubEncd());
												// ���ÿ�������
												order.setCanRefNum(order.getInvNum());
												// ���ÿ��˽��
												order.setCanRefMoney(order.getPayMoney());
												// ������Ʒ���
												order.setGoodMoney(new BigDecimal(
														dFormat.format(platOrders.getGoodMoney().multiply(rate))));
												money456 = money456.add(order.getGoodMoney());
												// �����Żݽ��
												order.setDiscountMoney(
														order.getGoodMoney().subtract(order.getPayMoney()));
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
												order.setInvNum((new BigDecimal(order.getGoodNum()))
														.multiply(strucksublist.get(j).getSubQty()).intValue());
												order.setPayMoney(order.getPayMoney().subtract(money123));// ����ʵ�����
																											// ������λС��
												// ����ʵ������ ����8λС��
												order.setPayPrice(
														order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																8, BigDecimal.ROUND_HALF_UP));
												order.setSellerPrice(order.getPayPrice());
												// �������
												order.setInvId(strucksublist.get(j).getSubEncd());
												// ���ÿ�������
												order.setCanRefNum(order.getInvNum());
												// ���ÿ��˽��
												order.setCanRefMoney(order.getPayMoney());
												// ������Ʒ���
												order.setGoodMoney(platOrders.getGoodMoney().subtract(money456));
												// �����Żݽ��
												order.setDiscountMoney(
														order.getGoodMoney().subtract(order.getPayMoney()));
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
							platOrders.setPayPrice(platOrders.getPayMoney()
									.divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
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
						platOrder.setAuditHint("���������PTO����Ϊ��");
						// platOrderDao.update(platOrder);//���¶����������ʾ��
						flag = false;
						orderslist.add(platOrders);
						continue;
					}
				}
			}
		}*/
		return platOrdersList;
	}
	// ��������

	@Override
	public String unionQuery(String orderId) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "��ѯ�ɹ�";
		boolean success = true;
		PlatOrder platOrder = platOrderDao.select(orderId);
		List<UnionQuery> unionQueries = new ArrayList<>();
		if (platOrder == null) {
			message = "����ѯ����������";
		} else if (platOrder.getIsAudit() == 0) {
			// ����ѯ������δ��ˣ�û�й�������
			message = "��ǰ������δ��ˣ�û�й�������";
		} else {
			// ��ѯ������Ӧ���۵���
			// ��ѯ������Ӧ�˿
			// ��ѯ��Ӧ���۳��ⵥ
			SellSngl sellSngl = platOrderDao.selectSellSnglByOrderId(orderId);

			if (sellSngl!=null) {
				UnionQuery unionQuery = new UnionQuery();
				unionQuery.setAudit("" + sellSngl.getIsNtChk());
				unionQuery.setOrderId(sellSngl.getSellSnglId());
				unionQuery.setType(1);// ���õ�������Ϊ���۵�
				unionQuery.setOrderName("���۵�");
				unionQueries.add(unionQuery);
				if (sellSngl.getIsNtChk() == 1) {
					// ���۵�����ˣ��������۳��ⵥ
					List<SellOutWhs> sellOutWhs = platOrderDao.selectSellOutWhsByOrderId(sellSngl.getSellSnglId());// ��sellsnglid
					for (int i = 0; i < sellOutWhs.size(); i++) {
						UnionQuery unionQuery1 = new UnionQuery();
						unionQuery1.setAudit("" + sellOutWhs.get(i).getIsNtChk());
						unionQuery1.setOrderId(sellOutWhs.get(i).getOutWhsId());
						unionQuery1.setType(2);// ���õ�������Ϊ���۳��ⵥ
						unionQuery1.setOrderName("���۳��ⵥ");
						unionQueries.add(unionQuery1);
					}
					List<LogisticsTab> logisticsTabs = platOrderDao.selectLogisticsTabByOrderId(orderId);
					for (int i = 0; i < logisticsTabs.size(); i++) {
						UnionQuery unionQuery1 = new UnionQuery();
						unionQuery1.setAudit("1");//�����������״̬��ȫ��Ĭ�������
						unionQuery1.setOrderId(logisticsTabs.get(i).getOrdrNum().toString());
						unionQuery1.setType(4);// ���õ�������Ϊ������
						unionQuery1.setOrderName("������");
						unionQueries.add(unionQuery1);
					}
				}
			}

			List<RefundOrder> refundOrders = platOrderDao.selectRefundOrderByEcOrderId(platOrder.getEcOrderId());// ��ecorderid
			for (int i = 0; i < refundOrders.size(); i++) {
				UnionQuery unionQuery2 = new UnionQuery();
				unionQuery2.setAudit("" + refundOrders.get(i).getIsAudit());
				unionQuery2.setOrderId(refundOrders.get(i).getRefId());
				unionQuery2.setType(3);// ���õ�������Ϊ�˿
				unionQuery2.setOrderName("�����˿");
				unionQueries.add(unionQuery2);
			}

		}
		try {
			resp = BaseJson.returnRespObjList("ec/platOrder/unionQuery", success, message, null, unionQueries);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(resp);
		return resp;
	}

	@Override
	public String exportPlatOrder(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		List<Map> platOrders = platOrderDao.exportList1(map);
		try {
			resp = BaseJson.returnRespObjList("ec/platOrder/exportPlatOrder", true, "", null, platOrders);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String autoMatch(String orderId, String accNum) {
		// TODO Auto-generated method stub
		boolean isSuccess = true;
		String resp = "";
		String[] orderIds = orderId.split(",");
		int successCount = 0;
		for (int i = 0; i < orderIds.length; i++) {

			PlatOrder platOrder = new PlatOrder();
			platOrder = platOrderDao.select(orderIds[i]);
			List<PlatOrders> platOrdersList = new ArrayList<PlatOrders>();
			platOrdersList = platOrdersDao.select(orderIds[i]);
			List<PlatOrders> orderslist = new ArrayList<PlatOrders>();
			boolean flag = true;
			if (platOrder.getIsAudit()==0) {//����δ��˲ſ���ִ���Զ�ƥ��
				int jishu =0;//����һ���������ѭ��������ϸʧ��ʱ�����ڼ�¼��ǰѭ�����ڼ�����ϸ��
				// ѭ��ƥ����Ʒ����
				for (int j = 0; j < platOrdersList.size(); j++) {
					jishu=j;//�������ֵ��ǰѭ����
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
					if (goodRecord == null) {
						platOrder.setAuditHint("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");
						platOrderDao.update(platOrder);// ���¶����������ʾ��
						orderslist.add(platOrders);
						flag = false;
						break;
					} else if (StringUtils.isEmpty(goodRecord.getGoodId())) {
						platOrder.setAuditHint("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");
						platOrderDao.update(platOrder);// ���¶����������ʾ��
						orderslist.add(platOrders);
						flag = false;
						break;
					} else {
						String invId = goodRecord.getGoodId();
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// ȥ���������ѯ���������Ϣ
						if (invtyDoc == null) {
							platOrder.setAuditHint("�ڴ��������δƥ�䵽��Ӧ�������,��ƥ��Ĵ�����룺" + invId);
							platOrderDao.update(platOrder);// ���¶����������ʾ��
							orderslist.add(platOrders);
							flag = false;
							break;
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
										break;
									} else {
										if (prodtru.getIsNtChk() == 0) {
											// ��Ӧ��Ʒ�ṹ��δ���
											platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��" + prodtru.getMomEncd() + "��δ���");
											platOrderDao.update(platOrder);// ���¶����������ʾ��
											orderslist.add(platOrders);
											flag = false;
											break;
										} else {
											List<ProdStruSubTab> strucksublist = prodtru.getStruSubList();
											// ѭ����Ʒ�ṹ�ӱ���Ϣ
											if (strucksublist.size() == 0) {
												platOrder.setAuditHint(
														"��Ӧ��Ʒ�ṹ��" + prodtru.getMomEncd() + "û�������Ӽ���ϸ���������á�");
												platOrderDao.update(platOrder);// ���¶����������ʾ��
												orderslist.add(platOrders);
												flag = false;
												break;
											} else if (strucksublist.size() == 1) {
												// ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
												// �������
												platOrders.setInvId(strucksublist.get(0).getSubEncd());
												// ���ô������
												platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
														.multiply(strucksublist.get(0).getSubQty()).intValue());
												// ���ÿ�������
												platOrders.setCanRefNum(platOrders.getInvNum());
												platOrders.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
												platOrders.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
												// ����ʵ������
												platOrders.setPayPrice(platOrders.getPayMoney().divide(
														(new BigDecimal(platOrders.getInvNum())), 8,
														BigDecimal.ROUND_HALF_UP));
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
													InvtyDoc invtyDoc1 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
															strucksublist.get(x).getSubEncd());
													if (invtyDoc1.getRefCost() == null) {
														flag = false;
														break;
													} else {
														total = total.add(invtyDoc1.getRefCost()
																.multiply(strucksublist.get(x).getSubQty()));
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
														e.printStackTrace();
													}

													if (x + 1 < strucksublist.size()) {
														// ����ÿ���Ӽ�ռ�ܳɱ��ı��� ����8λС��
														InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
																strucksublist.get(x).getSubEncd());
														BigDecimal rate = invtyDoc2.getRefCost()
																.multiply(strucksublist.get(x).getSubQty())
																.divide(total, 8, BigDecimal.ROUND_HALF_UP);
														order.setInvNum((new BigDecimal(order.getGoodNum()))
																.multiply(strucksublist.get(x).getSubQty()).intValue());
														order.setPayMoney(new BigDecimal(
																dFormat.format(order.getPayMoney().multiply(rate))));// ����ʵ�����
																																						// ������λС��
																																						// ����ʵ������ ����8λС��
														order.setPayPrice(order.getPayMoney().divide(
																(new BigDecimal(order.getInvNum())), 8,
																BigDecimal.ROUND_HALF_UP));
														order.setSellerPrice(order.getPayPrice());
														// �������
														order.setInvId(strucksublist.get(x).getSubEncd());
														// ���ÿ�������
														order.setCanRefNum(order.getInvNum());
														// ���ÿ��˽��
														order.setCanRefMoney(order.getPayMoney());
														// ������Ʒ���
														order.setGoodMoney(new BigDecimal(dFormat
																.format(platOrders.getGoodMoney().multiply(rate))));
														money456 = money456.add(order.getGoodMoney());
														// �����Żݽ��
														order.setDiscountMoney(
																order.getGoodMoney().subtract(order.getPayMoney()));
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
														order.setInvNum((new BigDecimal(order.getGoodNum()))
																.multiply(strucksublist.get(x).getSubQty()).intValue());
														order.setPayMoney(order.getPayMoney().subtract(money123));// ����ʵ�����
																													// ������λС��
																													// ����ʵ������ ����8λС��
														order.setPayPrice(order.getPayMoney().divide(
																(new BigDecimal(order.getInvNum())), 8,
																BigDecimal.ROUND_HALF_UP));
														order.setSellerPrice(order.getPayPrice());
														// �������
														order.setInvId(strucksublist.get(x).getSubEncd());
														// ���ÿ�������
														order.setCanRefNum(order.getInvNum());
														// ���ÿ��˽��
														order.setCanRefMoney(order.getPayMoney());
														// ������Ʒ���
														order.setGoodMoney(
																platOrders.getGoodMoney().subtract(money456));
														// �����Żݽ��
														order.setDiscountMoney(
																order.getGoodMoney().subtract(order.getPayMoney()));
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
									platOrders.setPayPrice(platOrders.getPayMoney().divide(
											(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
									platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
									// �����Żݽ��
									platOrders.setDiscountMoney(
											platOrders.getGoodMoney().subtract(platOrders.getPayMoney()));
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
								platOrder.setAuditHint("������룺"+invtyDoc.getInvtyEncd()+"�ڴ��������PTO����Ϊ��");
								platOrderDao.update(platOrder);// ���¶����������ʾ��
								flag = false;
								break;
							}
						}
					}
				}
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
									if (storeRecord.getEcId().equals("PDD")||storeRecord.getEcId().equals("TM")) {
										// TODO Auto-generated catch block
										//e.printStackTrace();
										
									}else {
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
					//�洢������Ʒ���Ƴ���list
					List<PlatOrders> platOrders111 = new ArrayList<PlatOrders>();
					if (flag) {
						for (int j = 0; j < orderslist.size(); j++) {
							InvtyDoc invtyDoc = invtyDocDao
									.selectInvtyDocByInvtyDocEncd(orderslist.get(j).getInvId());
							if (invtyDoc.getShdTaxLabour() != null) {
								if (invtyDoc.getShdTaxLabour() == 1) {
									platOrders111.add(orderslist.get(j));
									orderslist.remove(j);
									j--;
									if (orderslist.size() == 0) {
										break;
									}
								}
							} else {
								platOrder.setAuditHint("��������" + invtyDoc.getInvtyEncd() + "�Ƿ�Ӧ˰��������");
								flag = false;
								break;
							}
						} 
					}
					if (flag) {
						//�洢�������γɹ��󷵻�����list�Լ������������list
						List<PlatOrders> platOrders123 = new ArrayList<PlatOrders>();
						for (int j = 0; j < orderslist.size(); j++) {
							if (StringUtils.isEmpty(orderslist.get(j).getBatchNo())) {//�����ǰ����ϸ�����ֶ�Ϊ��
								// ��֤����������������
								Map map4 = checkCanUseNumAndBatch(orderslist.get(j));
								if (map4.get("flag").toString().equals("true")) {
									// �������γɹ�
									List<PlatOrders> platOrders456 = (List<PlatOrders>) map4.get("platOrders");
									for (int k = 0; k < platOrders456.size(); k++) {
										platOrders123.add(platOrders456.get(k));
									}

								} else {
									flag = false;//û����ɹ�ʱ������ǰ����ϸ���Ƶ���list
									platOrder.setAuditHint(map4.get("message").toString());
									platOrders123.add(orderslist.get(j));
								} 
							} else {
								platOrders123.add(orderslist.get(j));
							}
						}
						if (platOrders123.size() > 0) {
							orderslist.clear();
							orderslist.addAll(platOrders123);
						} 
					}
					//���ո��Ƴ���������Ʒ��list���ӽ�ȥ
					for (int j = 0; j < platOrders111.size(); j++) {
						orderslist.add(platOrders111.get(j));
					}
					//System.out.println("��ƥ�����:"+platOrder.getCanMatchActive());
					if(flag&&orderslist.size()>0&&platOrder.getCanMatchActive()==null) {
						platOrder.setPlatOrdersList(orderslist);
						int size1 = orderslist.size();
						platOrder = salesPromotionActivityUtil.matchSalesPromotions(platOrder);
						orderslist = platOrder.getPlatOrdersList();
						int size2= orderslist.size();
						platOrderDao.update(platOrder);//��������
						if(size1<size2) {
							// ɾ��ԭ��������ϸ�ӱ�
							platOrdersDao.delete(platOrder.getOrderId());
							// �����Զ�ƥ�������Ķ�����ϸ�ӱ�
							platOrdersDao.insert(orderslist);
							
							autoMatch(platOrder.getOrderId(), accNum);
							try {
								resp = BaseJson.returnRespObj("ec/platOrder/autoMatch", isSuccess,
										"�����Զ�ƥ��ɹ�" + successCount + "��������ʧ��" + (orderIds.length - successCount) + "������", null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return resp;
						}
						
					}
					if (orderslist.size()>0) {
						if(platOrdersList.size()==jishu+1) {
							//��jishu+1����ԭ������ϸ��sizeʱ,˵������ƥ������Ʒ
						}else {
							//������ƥ����Ʒʱ����Ҫ��ʣ�ಿ�ּӵ��б��д���һ�ν���ƥ��
							for (int j = jishu+1; j < platOrdersList.size(); j++) {
								orderslist.add(platOrdersList.get(j));
							}
						}
						// ɾ��ԭ��������ϸ�ӱ�
						platOrdersDao.delete(platOrder.getOrderId());
						// �����Զ�ƥ�������Ķ�����ϸ�ӱ�
						platOrdersDao.insert(orderslist);
					}
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

						// ����������ɺ��ж��Ƿ��Զ�����
						if (auditStrategyService.autoAuditCheck(platOrder, orderslist)) {
							// ����trueʱ���˶���ͨ������ֱ�ӽ������
							associatedSearchService.orderAuditByOrderId(platOrder.getOrderId(), "sys",sdf.format(new Date()));
							// ����Ĭ�ϲ���Աsys
						}
					}else {
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

				
			}

		}
		try {
			resp = BaseJson.returnRespObj("ec/platOrder/autoMatch", isSuccess,
					"�����Զ�ƥ��ɹ�" + successCount + "��������ʧ��" + (orderIds.length - successCount) + "������", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String splitOrder(String orderId, String platOrdersIds, String splitNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		DecimalFormat dFormat = new DecimalFormat("0.00");
		String[] platordersids = platOrdersIds.split(",");
		String[] splitnum = splitNum.split(",");
		PlatOrder platOrder = platOrderDao.select(orderId);
		if(platOrder.getIsAudit()==1) {
			//���������
			
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			MisUser misUser = misUserDao.select(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("��ʧ�ܣ����������");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(4);// 4��
			logRecord.setTypeName("��");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			message = "��ʧ�ܣ����������";
			try {
				resp = BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resp;
		}
		List<PlatOrders> platOrdersOld = new ArrayList<PlatOrders>();
		List<PlatOrders> platOrdersNew = new ArrayList<PlatOrders>();
		if (platOrdersDao.checkInvIdHasNull(orderId).size() > 0) {
			// �������ֶ�����ϸ�к���δƥ�䵽���������Ʒʱ���������

			platOrder.setAuditHint("��ʧ�ܣ�����δƥ�䵽��������Ķ�����ϸ");
			platOrderDao.update(platOrder);
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			MisUser misUser = misUserDao.select(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("��ʧ�ܣ�����δƥ�䵽��������Ķ�����ϸ");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(4);// 4��
			logRecord.setTypeName("��");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			message = "��ʧ�ܣ�����δƥ�䵽��������Ķ�����ϸ";
			try {
				resp = BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resp;
		}
		int splitCount = 0;
		for (int i = 0; i < splitnum.length; i++) {
			splitCount += Integer.parseInt(splitnum[i]);
		}
		int ccount = platOrderDao.selectInvNumCountByOrderId(orderId);

		if(ccount<=splitCount) {
			platOrder.setAuditHint("��ʧ�ܣ����������ӦС��ԭ��������");
			platOrderDao.update(platOrder);
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			MisUser misUser = misUserDao.select(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("��ʧ�ܣ����������ӦС��ԭ��������");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(4);// 4��
			logRecord.setTypeName("��");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			message = "��ʧ�ܣ����������ӦС��ԭ��������";
			try {
				resp = BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resp;
		}
		/*
		 * if (platOrder.getGoodNum() <= splitCount) { // ���������Ʒ����С�ڵ���Ҫ��ֵ������ܺͣ��������
		 * platOrder.setAuditHint("��ʧ�ܣ����������ڶ�������Ʒ����"); platOrderDao.update(platOrder);
		 * // ��־��¼ LogRecord logRecord = new LogRecord(); logRecord.setOperatId(accNum);
		 * MisUser misUser = misUserDao.select(accNum); if (misUser != null) {
		 * logRecord.setOperatName(misUser.getUserName()); }
		 * logRecord.setOperatContent("��ʧ�ܣ����������ڶ�������Ʒ����");
		 * logRecord.setOperatTime(sdf.format(new Date()));
		 * logRecord.setOperatType(4);// 4�� logRecord.setTypeName("��");
		 * logRecord.setOperatOrder(platOrder.getEcOrderId());
		 * logRecordDao.insert(logRecord); message = "��ʧ�ܣ����������ڶ�������Ʒ����"; try { resp =
		 * BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * return resp; }
		 */
		try {
			// �������������µ�������������
			PlatOrder platOrder2 = (PlatOrder) platOrder.clone();
			String newOrderId = getOrderNo.getSeqNo("ec", accNum);
			platOrder2.setOrderId(newOrderId);
			boolean flag = true;
			for (int i = 0; i < platordersids.length; i++) {
				// ��ѯ������ϸ
				PlatOrders platOrders = platOrdersDao.selectByNo(Long.parseLong(platordersids[i]));
				if (platOrders.getInvNum() < Integer.parseInt(splitnum[i])) {
					// �����������С��Ҫ��ֵ�����ʱ�����ɲ��
					platOrder.setAuditHint("��ʧ�ܣ��������������ԭ���ɲ�����");
					platOrderDao.update(platOrder);
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					MisUser misUser = misUserDao.select(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("��ʧ�ܣ��������������ԭ���ɲ�����");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(4);// 4��
					logRecord.setTypeName("��");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					flag = false;
					break;
				} else {
					PlatOrders platOrders2 = (PlatOrders) platOrders.clone();
					platOrders2.setOrderId(newOrderId);
					platOrders2.setInvNum(Integer.parseInt(splitnum[i]));// ��������Ϊ�������
					platOrders2.setCanRefNum(platOrders2.getInvNum());
					platOrders.setSplitNum(Integer.parseInt(splitnum[i]));
					if (platOrders.getInvNum() == Integer.parseInt(splitnum[i])) {
						// ������������ԭ������һ��
						platOrders2.setPayMoney(platOrders.getPayMoney());
						platOrders2.setCanRefMoney(platOrders.getCanRefMoney());
						platOrders.setInvNum(0);
						platOrders.setPayMoney(BigDecimal.ZERO);
						// �ɲ����ÿ�����������������ɾ��������ϸ
					} else {
						BigDecimal price = platOrders.getGoodMoney().divide(new BigDecimal(platOrders.getInvNum()),2,BigDecimal.ROUND_HALF_UP);

						platOrders2.setPayMoney(new BigDecimal(dFormat
								.format(platOrders.getPayPrice().multiply(new BigDecimal(platOrders2.getInvNum())))));
						platOrders2.setGoodMoney(new BigDecimal(dFormat
								.format(price.multiply(new BigDecimal(platOrders2.getInvNum())))));
						platOrders2.setDiscountMoney(platOrders2.getGoodMoney().subtract(platOrders2.getPayMoney()));
						platOrders2.setCanRefMoney(platOrders2.getPayMoney());
						platOrders.setInvNum(platOrders.getInvNum() - platOrders2.getInvNum());
						platOrders.setCanRefNum(platOrders.getInvNum());
						platOrders.setPayMoney(platOrders.getPayMoney().subtract(platOrders2.getPayMoney()));
						platOrders.setCanRefMoney(platOrders.getPayMoney());
					}
					platOrdersOld.add(platOrders);
					platOrdersNew.add(platOrders2);
				}
			}
			if (flag) {

				platOrder.setGoodNum(platOrder.getGoodNum() - splitCount);
				platOrder2.setGoodNum(splitCount);
				BigDecimal xinGoodMoney = BigDecimal.ZERO;
				BigDecimal xinPayMoney = BigDecimal.ZERO;
				for (int j = 0; j < platOrdersNew.size(); j++) {
					//�����µ�����Ʒ���ʵ�����ϵͳ�Żݽ��
					xinGoodMoney=xinGoodMoney.add(platOrdersNew.get(j).getGoodMoney());
					xinPayMoney=xinPayMoney.add(platOrdersNew.get(j).getPayMoney());
				}

				platOrder2.setGoodMoney(xinGoodMoney);
				platOrder2.setPayMoney(xinPayMoney);
				platOrder2.setDiscountMoney(xinGoodMoney.subtract(xinPayMoney));
				platOrder.setGoodMoney(platOrder.getGoodMoney().subtract(platOrder2.getGoodMoney()));
				platOrder.setPayMoney(platOrder.getPayMoney().subtract(platOrder2.getPayMoney()));
				platOrder.setDiscountMoney(platOrder.getDiscountMoney().subtract(platOrder2.getDiscountMoney()));
				// �����µĶ�������
				platOrderDao.insert(platOrder2);
				// ѭ���жϵ�ǰ��������ӵ������Ƿ�Ϊ0 �����Ϊ0��ɾ����ǰ������Ϊ0��update
				for (int i = 0; i < platOrdersOld.size(); i++) {
					if (platOrdersOld.get(i).getInvNum() == 0) {
						platOrdersDao.deleteByOrdersNo(platOrdersOld.get(i).getNo());
					} else {
						platOrdersDao.updateNumAndMoney(platOrdersOld.get(i));
					}
				}
				platOrderDao.update(platOrder);
				platOrdersDao.insert(platOrdersNew);
				message = "������ֳɹ�";
			} else {
				message = "�������ʧ��";
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "�쳣������ϵ����Ա�鿴";
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "�쳣������ϵ����Ա�鿴";
		}
		try {
			resp = BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

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

	/**
	 * ���������������
	 */
	@Override
	public String downloadByOrderId(String json) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			String userId = BaseJson.getReqHead(json).get("accNum").asText();
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(json).toString());
			String storeId = map.get("storeId").toString();
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			//����
			List<PlatOrder> platOrderList = new ArrayList<>();
			//�Ż�
			List<CouponDetail> couponList = new ArrayList<CouponDetail>();

			if (storeRecord.getEcId().equals("JD")) {
				resp = jdDownloadByOrderId(userId, map);
			} else if (storeRecord.getEcId().equals("TM")) {
				String tid = map.get("orderId").toString();
				resp = tmdownloadByOrderIdSdk(tid, userId, storeId,platOrderList,couponList);
				 //��֧�����䶩����
	            List<String> seqNoList = getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId);
	            downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId, seqNoList);
	            if (downloadCount>0) {
					resp = BaseJson.returnRespObj("ec/platOrder/download", true, "�������سɹ�" + tid, null);
				}else {
					resp = BaseJson.returnRespObj("ec/platOrder/download", true, "��������ʧ��", null);
				}

				//��֧�����䶩����
				//downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId);


			}
			else if (storeRecord.getEcId().equals("XMYP")) {
				try {
					String orderId = map.get("orderId").toString();
					resp = platOrderXMYP.XMYPDownloadByOrderId(userId, orderId, storeId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "����ʧ�ܣ������������س����쳣������", null);
				}
			}else if (storeRecord.getEcId().equals("PDD")) {
				try {
					Map map2 = platOrderPdd.pddDownloadByOrderCode(map.get("orderId").toString(), userId, storeId, 0);
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "������ɣ����γɹ�����"+map2.get("downloadCount").toString()+"������", null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "����ʧ�ܣ�ƴ��൥���������س����쳣������", null);
				}
			} else if(storeRecord.getEcId().equals("KaoLa")){
				resp = KaoLaDownloadByOrderId(userId, "", "", storeId, map.get("orderId").toString());
			}else if(storeRecord.getEcId().equals("XHS")){
				try {
					resp = platOrderXHS.xhsDownloadByPlatOrder(map.get("orderId").toString(), userId, storeId, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						resp=BaseJson.returnRespObj("ec/platOrder/download", true, "���������쳣��������", null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}else if(storeRecord.getEcId().equals("MaiDu")){
				try {
					
					resp = platOrderMaiDu.maiDuDownload(userId, 1, 20, "", "", storeId,map.get("orderId").toString());
				} catch (Exception e1) {
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "�����쳣", null);
					//e1.printStackTrace();
				}
				
			}else {
				resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "����ʧ�ܣ���ѡ������ݲ�֧�����ض���", null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}


	//����excle
	private Map<String,PlatOrder> uploadPlatOrder(MultipartFile  file,String userId){
		Map<String,PlatOrder> temp = new HashMap<>();
		int j=0;
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				if( StringUtils.isEmpty(GetCellData(r,"ƽ̨������"))){
					continue;
				}
				//����ʵ����
				PlatOrder platOrder=new PlatOrder();
				String ecOrderid = GetCellData(r,"ƽ̨������");
				if(temp.containsKey(ecOrderid)) {
					platOrder = temp.get(ecOrderid);
				}else {
					platOrder.setOrderId(getOrderNo.getSeqNo("ec", userId));
				}
				
				platOrder.setStoreId(GetCellData(r,"���̱��"));
				platOrder.setStoreName(GetCellData(r,"��������"));
				platOrder.setPayTime(GetCellData(r,"����ʱ��"));
				platOrder.setGoodNum(GetBigDecimal(GetCellData(r,"������Ʒ����"),0).intValue());
				platOrder.setGoodMoney(GetBigDecimal(GetCellData(r,"��Ʒ���"),2));
				platOrder.setPayMoney(GetBigDecimal(GetCellData(r,"ʵ�����"),2));
				platOrder.setBuyerNote(GetCellData(r,"��ұ�ע"));
				platOrder.setSellerNote(GetCellData(r,"���ұ�ע"));
				platOrder.setRecAddress(GetCellData(r,"�ջ���ַ"));
				platOrder.setBuyerId(GetCellData(r,"��һ�Ա��"));
				platOrder.setRecName(GetCellData(r,"�ջ�������"));
				platOrder.setRecMobile(GetCellData(r,"�ֻ���"));
				platOrder.setEcOrderId(GetCellData(r,"ƽ̨������"));
				platOrder.setNoteFlag(0);
				platOrder.setIsClose(0);
				
				platOrder.setIsShip(Integer.parseInt(GetCellData(r,"�Ƿ񷢻�")));
				platOrder.setExpressNo(GetCellData(r,"��ݵ���"));
				platOrder.setExpressCode(GetCellData(r,"��ݹ�˾����"));
				platOrder.setIsAudit(0);
				platOrder.setIsInvoice(0);
				platOrder.setAdjustMoney(GetBigDecimal(GetCellData(r,"�������"), 2));
				platOrder.setDiscountMoney(GetBigDecimal(GetCellData(r,"�Żݽ��"),2));
				platOrder.setOrderStatus(0);
				platOrder.setReturnStatus(0);
				platOrder.setHasGift((GetBigDecimal(GetCellData(r,"������Ʒ"),0).intValue()));
				platOrder.setMemo(GetCellData(r,"������ע"));
				platOrder.setAdjustStatus(0);
				platOrder.setTradeDt(GetCellData(r,"�µ�ʱ��"));
				platOrder.setBizTypId("2");
				platOrder.setSellTypId("1");
				platOrder.setRecvSendCateId("6");
				platOrder.setOrderSellerPrice(GetBigDecimal(GetCellData(r,"������"),2));
				platOrder.setProvince(GetCellData(r,"ʡ"));
				platOrder.setCity(GetCellData(r,"��"));
				platOrder.setCounty(GetCellData(r,"��"));
				platOrder.setFreightPrice(GetBigDecimal(GetCellData(r,"�˷�"),2));
				platOrder.setDeliverWhs(GetCellData(r,"�����ֿ����"));
				platOrder.setDeliverSelf((GetBigDecimal(GetCellData(r,"�Ƿ��Է���"),0).intValue()));
				platOrder.setWeight(GetBigDecimal(GetCellData(r,"����"), 6));
				platOrder.setDownloadTime(sdf.format(new Date()));
				
				
				
				List<PlatOrders> PlatOrderslist = platOrder.getPlatOrdersList();//�ӱ�
				if(PlatOrderslist == null) {
					PlatOrderslist = new ArrayList<>();
				}
				PlatOrders platOrders= new PlatOrders();
				platOrders.setGoodId(GetCellData(r,"spu"));
				platOrders.setGoodName(GetCellData(r,"��Ʒ����"));
				platOrders.setGoodNum((GetBigDecimal(GetCellData(r,"sku����"),0).intValue()));
				platOrders.setGoodMoney(GetBigDecimal(GetCellData(r,"��ϸ��Ʒ���"),2));
				platOrders.setPayMoney(GetBigDecimal(GetCellData(r,"��ϸʵ�����"),2));
				platOrders.setGoodSku(GetCellData(r,"��Ʒsku"));
				platOrders.setDiscountMoney(GetBigDecimal(GetCellData(r,"��ϸ�Żݽ��"),2));
				platOrders.setAdjustMoney(GetBigDecimal(GetCellData(r,"��ϸ�������"), 2));
				platOrders.setMemo(GetCellData(r,"��ϸ��ע"));
				platOrders.setGoodPrice(GetBigDecimal(GetCellData(r,"��ϸ����"), 8));
				platOrders.setPayPrice(GetBigDecimal(GetCellData(r,"��ϸʵ������"), 8));
				platOrders.setSellerPrice(GetBigDecimal(GetCellData(r,"���㵥��"), 8));
				platOrders.setInvId(GetCellData(r,"�������"));
				platOrders.setInvNum((GetBigDecimal(GetCellData(r,"�������"),0).intValue()));
				platOrders.setPtoCode(GetCellData(r,"ĸ������"));
				platOrders.setPtoName(GetCellData(r,"ĸ������"));
				platOrders.setIsGift((GetBigDecimal(GetCellData(r,"�Ƿ���Ʒ"),0).intValue()));
				platOrders.setCanRefNum(platOrders.getInvNum());
				platOrders.setCanRefMoney(platOrders.getPayMoney());
				platOrders.setOrderId(platOrder.getOrderId());
				platOrders.setEcOrderId(platOrder.getEcOrderId());
				platOrders.setDeliverWhs(platOrder.getDeliverWhs());
				platOrders.setExpressCom(platOrder.getExpressCode());
				PlatOrderslist.add(platOrders);
				platOrder.setPlatOrdersList(PlatOrderslist);
				temp.put(ecOrderid,platOrder);
			
			}
		    fileIn.close();
		} catch (Exception e) {
			e.printStackTrace();
			PlatOrder platOrder = new PlatOrder();
			platOrder.setAuditHint("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!");
			temp.put("errMessage", platOrder);
			//throw new RuntimeException("�ļ��ĵ�"+j+"�е����ʽ�����޷�����!"+e.getMessage());
		}
	   return temp;    
	}

	@Override
	public String importPlatOrder(MultipartFile file, String userId) {
		// TODO Auto-generated method stub
		String message="";
		Boolean isSuccess=true;
		int successCount=0;
		String resp="";
		Map<String, PlatOrder> map = uploadPlatOrder(file, userId);
		if (map.containsKey("errMessage")){
			message=map.get("errMessage").getAuditHint();
		}else{
			MisUser misUser = misUserDao.select(userId);
			for (Map.Entry<String, PlatOrder> entry : map.entrySet()) {
				if (platOrderDao.checkExsits1(entry.getValue().getEcOrderId()) == 0) {
					platOrderDao.insert(entry.getValue());
					platOrdersDao.insert(entry.getValue().getPlatOrdersList());
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("����ɹ�");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(16);// 4���붩��
					logRecord.setTypeName("���붩��");
					logRecord.setOperatOrder(entry.getValue().getEcOrderId());
					logRecordDao.insert(logRecord);
					//��������Զ�ƥ��
					autoMatch(entry.getValue().getOrderId(), userId);
					successCount++;
				}
			}
			message="������ɣ����γɹ����붩��"+successCount+"��";
		}
		try {
			resp = BaseJson.returnRespObj("ec/platOrder/importPlatOrder", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String Near15DaysOrder() {
		// TODO Auto-generated method stub
		String resp="";
		try {
			List<Near15DaysOrder> list = platOrderDao.Near15DaysOrder();
			resp = BaseJson.returnRespObjList("ec/platOrder/Near15DaysOrder", true, "��ѯ�ɹ���", null, list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObjList("ec/platOrder/Near15DaysOrder", true, "��ѯ�쳣��", null, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	@Override
	public String orderssssList(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<Orderssss> OrderssssList = platOrdersDao.orderssssList(map);
			int count = platOrdersDao.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/platOrder/orderssssList", true, "��ѯ�ɹ���", count, pageNo, pageSize, 0, 0,
					OrderssssList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/orderssssList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String exportOrderssssList(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<exportOrders> exportOrdersList = platOrdersDao.exportOrderssssList(map);
			if (exportOrdersList == null || exportOrdersList.size() == 0){
				exportOrdersList.add(new exportOrders());
			}
//			resp = BaseJson.returnRespList("ec/platOrder/exportOrderssssList", true, "��ѯ�ɹ���", OrderssssList.size(), 1, OrderssssList.size(), 0, 0,
//					OrderssssList);
			resp = BaseJson.returnRespObjListAnno("ec/platOrder/exportOrderssssList", true, "��ѯ�ɹ���", null, exportOrdersList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/exportOrderssssList �쳣˵����", e);
		}
		return resp;
	}

	/**
	 * ������ѯ������ϸ�б�
	 */
	@Override
	public String batchList(List<String> list) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<Orderssss> OrderssssList = platOrdersDao.batchList(list);
			
			resp = BaseJson.returnRespList("ec/platOrder/batchList", true, "��ѯ�ɹ���", OrderssssList.size(), 1, OrderssssList.size()+1, 0, 0,
					OrderssssList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/batchList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String batchEditWhs(String orderIds, String userId,String whsEncd) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int successCount =0 ;
		try {
			String[] orderIdsss = orderIds.split(",");
			WhsDoc whsDoc = whsDocMapper.selectWhsDoc(whsEncd);
			for (int i = 0; i < orderIdsss.length; i++) {
				PlatOrder platOrder2 = platOrderDao.select(orderIdsss[i]);
				if(platOrder2.getIsAudit()==0) {
					platOrder2.setDeliverWhs(whsEncd);//���ö����ķ����ֿ�
					platOrderDao.update(platOrder2);
					List<PlatOrders> platOrders = platOrdersDao.select(platOrder2.getOrderId());
					List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
					//ѭ���ж�ԭ������ϸ�������Ʒ�Ƿ���ռ�ÿ���������ռ�ÿ���������Ҫ�ͷ�ԭ�ֵĿ�����
					for (int j = 0; j < platOrders.size(); j++) {
						if (StringUtils.isNotEmpty(platOrders.get(j).getBatchNo())) {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setAvalQty(new BigDecimal(platOrders.get(j).getInvNum()));
								invtyTab.setWhsEncd(platOrders.get(j).getDeliverWhs());
								invtyTab.setBatNum(platOrders.get(j).getBatchNo());
								invtyTab.setInvtyEncd(platOrders.get(j).getInvId());
								invtyTabs.add(invtyTab);
						}
							
					}
					if (invtyTabs.size()>0) {
						for (int j = 0; j < invtyTabs.size(); j++) {
							
							List<InvtyTab> invtyTabs11 = new ArrayList<InvtyTab>();
							invtyTabs11.add(invtyTabs.get(j));
							invtyNumMapper.updateAInvtyTabJia(invtyTabs11);// ����������(�ӷ�)
						}
					}
					//�ͷ�ԭ�ֿ���������Ҫ���ԭ������ϸ�����Σ�Ч�ڵ���Ϣ
					for (int j = 0; j < platOrders.size(); j++) {
						platOrders.get(j).setBatchNo("");
						platOrders.get(j).setDeliverWhs(whsEncd);
						platOrders.get(j).setInvldtnDt(null);
						platOrders.get(j).setPrdcDt(null);
						
					}
					platOrdersDao.delete(platOrder2.getOrderId());
					platOrdersDao.insert(platOrders);
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					MisUser misUser = misUserDao.select(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�����޸ģ������޸ķ����ֿ�Ϊ��"+whsDoc.getWhsNm());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(5);// 5�޸�
					logRecord.setTypeName("�����޸�");
					logRecord.setOperatOrder(platOrder2.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					if (isSuccess) {
						//������Զ�ƥ��
						autoMatch(platOrder2.getOrderId(), userId);
					}
					successCount++;
				}
				
			}
			message="�����޸ĳɹ�"+successCount+"��������ʧ��"+(orderIdsss.length-successCount)+"������";
			resp = BaseJson.returnRespObj("ec/platOrder/batchEditWhs", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/batchEditWhs �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}
	@Override
	public String batchEditExpress(String orderIds, String userId,String expressCode) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int successCount =0 ;
		try {
			String[] orderIdsss = orderIds.split(",");
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(expressCode);
			for (int i = 0; i < orderIdsss.length; i++) {
				PlatOrder platOrder2 = platOrderDao.select(orderIdsss[i]);
				if(platOrder2.getIsAudit()==0) {
					platOrder2.setExpressTemplate(expressCorp.getCompanyCode());
					platOrder2.setExpressCode(expressCode);//���ö����Ŀ�ݹ�˾
					platOrderDao.update(platOrder2);
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					MisUser misUser = misUserDao.select(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�����޸ģ������޸Ŀ�ݹ�˾Ϊ��"+expressCorp.getExpressNm());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(5);// 5�޸�
					logRecord.setTypeName("�����޸�");
					logRecord.setOperatOrder(platOrder2.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					if (isSuccess) {
						//������Զ�ƥ��
						autoMatch(platOrder2.getOrderId(), userId);
					}
					successCount++;
				}
				
			}
			message="�����޸ĳɹ�"+successCount+"��������ʧ��"+(orderIdsss.length-successCount)+"������";
			resp = BaseJson.returnRespObj("ec/platOrder/batchEditExpress", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/batchEditExpress �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}
	
 
	@Override
	public String updateGooId(PlatOrder platOrder,String invId,String invIdLast,String multiple,List<PlatOrders> platOrders) {
		int result=0;
		String resp = "";
		for(int i=0;i<platOrders.size();i++) {

			String inv_Id= platOrders.get(i).getInvId();
			if(invId.equals(inv_Id)) {
				//�ж�ԭ������ϸ�������Ʒ�Ƿ���ռ�ÿ���������ռ�ÿ���������Ҫ�ͷ�ԭ�ֵĿ�����
				List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
				if (StringUtils.isNotEmpty(platOrders.get(i).getBatchNo())) {
						InvtyTab invtyTab = new InvtyTab();
						invtyTab.setAvalQty(new BigDecimal(platOrders.get(i).getInvNum()));
						invtyTab.setWhsEncd(platOrders.get(i).getDeliverWhs());
						invtyTab.setBatNum(platOrders.get(i).getBatchNo());
						invtyTab.setInvtyEncd(platOrders.get(i).getInvId());
						invtyTabs.add(invtyTab);
				}
				if (invtyTabs.size()>0) {
					for (int j = 0; j < invtyTabs.size(); j++) {
						List<InvtyTab> invtyTabs11 = new ArrayList<InvtyTab>();
						invtyTabs11.add(invtyTabs.get(j));
						invtyNumMapper.updateAInvtyTabJia(invtyTabs11);// ����������(�ӷ�)
					}
				}
				//�ͷ�ԭ�ֿ���������Ҫ���ԭ������ϸ�����Σ�Ч�ڵ���Ϣ
				platOrders.get(i).setBatchNo("");
				platOrders.get(i).setInvldtnDt(null);
				platOrders.get(i).setPrdcDt(null);
				

                platOrders.get(i).setInvId(invIdLast);
                result = result - platOrders.get(i).getInvNum();

                if (platOrders.get(i).getInvNum() != null || ("").equals(platOrders.get(i).getInvNum())) {
                    int newinvNum = platOrders.get(i).getInvNum() * Integer.parseInt(multiple);
                    platOrders.get(i).setInvNum(newinvNum);
                    BigDecimal payMoney = platOrders.get(i).getPayMoney();//ʵ��Ǯ
                    BigDecimal number = new BigDecimal(0);
                    number = BigDecimal.valueOf(newinvNum);
                    BigDecimal newpayPrice = payMoney.divide(number, 8, BigDecimal.ROUND_HALF_DOWN);
                    platOrders.get(i).setPayPrice(newpayPrice);
                    platOrdersDao.updateInvIdGooIdsGoodMoney(platOrders.get(i));
                }
                //�޸Ķ�������
                if (0 != platOrders.get(i).getInvNum()) {
                    result += platOrders.get(i).getInvNum();
                }
            }
            if (0 != result) {
                platOrder.setGoodNum(platOrder.getGoodNum() + result);
            }
        }
        platOrderDao.updateGoodNum(platOrder);

        try {
            resp = BaseJson.returnRespObj("ec/platOrder/updateecGooId", true, "���³ɹ�", null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    public String selectecGooId(List<String> list) {

        String resp = "";
        try {
            String result = "";
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    result = "'" + list.get(i) + "'";
                } else {
                    result += ",'" + list.get(i) + "'";
                }
            }
            PlatOrder platorder = null;
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", result);

			List<PlatOrders> platOrdersList = platOrdersDao.selectOrderByinvId(map);

			resp = BaseJson.returnRespObjList("ec/platOrder/selectecGooId", true, "��ѯ�ɹ���",platorder ,platOrdersList);
		} catch (IOException e) {
			logger.error("URL��ec/platOrder/selectecGooId �쳣˵����", e);
		}
		return resp;
		
	}

	@Override
	public PlatOrder selectecByorderId(String orderId) {
		
		PlatOrder platOrder2 =null;
		try {
			 platOrder2 = platOrderDao.selectecByorderId(orderId);
			
		} catch (Exception e) {
			logger.error("URL��ec/platOrder/selectecGooId �쳣˵����", e);
		}
		return platOrder2;
		
	}

	@Override
	public List<PlatOrders> selectecByorderIds(String orderId) {
		
		List<PlatOrders> platOrder2 =null;
		try {
			platOrder2 = platOrderDao.selectecByorderIds(orderId);
			
		} catch (Exception e) {
			logger.error("URL��ec/platOrder/selectecGooId �쳣˵����", e);
		}
		return platOrder2;
		
		
	}
	//�����ر�
	@Transactional
	@Override
	public String closeOrder(String orderId, String accNum) {
		// TODO Auto-generated method stub
		int successCount = 0 ;
		String resp ="";
		MisUser misUser = misUserDao.select(accNum);
		String[] orderIds = orderId.split(",");
		for (int i = 0; i < orderIds.length; i++) {
			PlatOrder platOrder = platOrderDao.select(orderIds[i]);
			List<PlatOrders> platOrders = platOrdersDao.select(orderIds[i]);
			if(platOrder.getIsAudit()==1) {
				//����˲��ܹر�
				// ��־��¼
				LogRecord logRecord = new LogRecord();

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�����رգ�ʧ�ܣ���������˲���ֱ�ӹر�");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(7);// 7�����ر�
				logRecord.setTypeName("�����ر�");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
			}else if(platOrder.getIsClose()==1) {
				//�����ѹر�
				// ��־��¼
				LogRecord logRecord = new LogRecord();

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�����رգ�ʧ��,�����ѹرգ������ظ��ر�");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(7);// 7�����ر�
				logRecord.setTypeName("�����ر�");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
			}else {
				//������ʼ�ر�
				//�ж϶�����ϸ���Ƿ���ռ�ÿ���������ϸ
				//��ռ�ÿ���������ϸ��Ҫ�ͷſ�����
				List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
				for (int k = 0; k < platOrders.size(); k++) {
					if (StringUtils.isNotEmpty(platOrders.get(k).getBatchNo())) {
						InvtyTab invtyTab1 = new InvtyTab();
						invtyTab1.setAvalQty(new BigDecimal(platOrders.get(k).getInvNum()));
						invtyTab1.setWhsEncd(platOrders.get(k).getDeliverWhs());
						invtyTab1.setBatNum(platOrders.get(k).getBatchNo());
						invtyTab1.setInvtyEncd(platOrders.get(k).getInvId());
						invtyTabs.add(invtyTab1);
					}
				}

				//���¶����ر�״̬
				int re = platOrderDao.closeOrder(orderIds[i]);

				if(re>0) {
					//��¼�ر���Ϣ
					successCount++;
					// ��־��¼
					LogRecord logRecord = new LogRecord();

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�����رգ��ɹ�");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(7);// 7�����ر�
					logRecord.setTypeName("�����ر�");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					//���³ɹ������ӿ�����
					for (int j = 0; j < invtyTabs.size(); j++) {
						List<InvtyTab> invtyTabs11 = new ArrayList<InvtyTab>();
						invtyTabs11.add(invtyTabs.get(j));
						invtyNumMapper.updateAInvtyTab(invtyTabs11);

					}
					//�ͷ�ԭ�ֿ���������Ҫ���ԭ������ϸ�����Σ�Ч�ڵ���Ϣ
					for (int j = 0; j < platOrders.size(); j++) {
						if (StringUtils.isNotEmpty(platOrders.get(j).getBatchNo())) {

							platOrders.get(j).setBatchNo("");
							platOrders.get(j).setInvldtnDt(null);
							platOrders.get(j).setPrdcDt(null);
							platOrdersDao.updateInvIdGooIdsGoodMoney(platOrders.get(j));
						}
					}
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/platOrder/closeOrder", true, "�رճɹ������γɹ��رն���"+successCount+"����ʧ��"+(orderIds.length-successCount)+"��", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	//������
	@Override
	public String openOrder(String orderId, String accNum) {
		// TODO Auto-generated method stub
		int successCount = 0 ;
		String resp ="";
		MisUser misUser = misUserDao.select(accNum);
		String[] orderIds = orderId.split(",");
		for (int i = 0; i < orderIds.length; i++) {
			PlatOrder platOrder = platOrderDao.select(orderIds[i]);
			List<PlatOrders> platOrders = platOrdersDao.select(orderIds[i]);
			if(platOrder.getIsClose()==0) {
				//����δ�ر�
				// ��־��¼
				LogRecord logRecord = new LogRecord();

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�����򿪣�ʧ��,����δ�ر�");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(21);// 21������
				logRecord.setTypeName("������");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
			}else {
				//������ʼ��
				//�����򿪺���Ҫִ���Զ�ƥ��
				//���¶���״̬
				int re = platOrderDao.openOrder(orderIds[i]);

				if(re>0) {

					autoMatch(orderIds[i], accNum);
					//��¼����Ϣ
					successCount++;
					// ��־��¼
					LogRecord logRecord = new LogRecord();

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�����򿪣��ɹ�");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(21);// 21������
					logRecord.setTypeName("������");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/platOrder/openOrder", true, "�򿪳ɹ������γɹ��򿪶���"+successCount+"����ʧ��"+(orderIds.length-successCount)+"��", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

}
