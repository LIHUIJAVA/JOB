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
	private ProdStruMapper prodStruDao;// 产品结构
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
				platOrder.setDownloadTime(sdf.format(new Date()));//设置下载时间等于新增时间
				MisUser misUser = misUserDao.select(userId);
				platOrderDao.insert(platOrder);
				for (PlatOrders platOrders : platOrdersList) {
					platOrders.setOrderId(orderId);
					platOrders.setEcOrderId(platOrder.getEcOrderId());
					platOrders.setDeliverWhs(platOrder.getDeliverWhs());
					platOrders.setExpressCom(platOrder.getExpressCode());
				}
				platOrdersDao.insert(platOrdersList);
				
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(userId);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("新增成功");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(1);// 1手工新增
				logRecord.setTypeName("手工新增订单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				//新增完订单自动匹配
				autoMatch(orderId, userId);
			resp = BaseJson.returnRespObjList("ec/platOrder/add", true, "新增成功！", platOrder, platOrdersList);
			}else {
				resp = BaseJson.returnRespObjList("ec/platOrder/add", true, "新增失败，订单已存在！", platOrder, platOrdersList);
			}

		} catch (Exception e) {
			logger.error("URL：ec/platOrder/add 异常说明：", e);
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
						invtyNumMapper.updateAInvtyTabJia(invtyTabs11);// 可用量增加(加法)
					}
				}
				platOrdersDao.delete(platOrder.getOrderId());
				for (int i = 0; i < platOrdersList.size(); i++) {
					platOrdersList.get(i).setBatchNo("");
					platOrdersList.get(i).setInvldtnDt(null);
					platOrdersList.get(i).setPrdcDt(null);//清空原订单明细里面的批号和效期
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
				
				message = "修改成功！";
			}else {
				isSuccess = false;
				message = "订单已审核，不可修改";
			}
			
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(userId);
			MisUser misUser = misUserDao.select(userId);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("订单修改，"+message);
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(5);// 5修改
			logRecord.setTypeName("订单修改");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			
			if (isSuccess) {
				//插入后自动匹配
				autoMatch(platOrder.getOrderId(), userId);
			}
			resp = BaseJson.returnRespObj("ec/platOrder/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/edit 异常说明：", e);
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
							// 订单已经审核，不能直接删除
							platOrderList.get(j).setAuditHint("订单为审核状态，不可直接删除");
							platOrderDao.update(platOrderList.get(j));// 更新订单的审核提示；
							fallcount++;
							flag = false;
							break;
						}
					}
					if (flag) {
						List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
						// 增加对应可用量
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
									invtyNumMapper.updateAInvtyTabJia(invtyTabs123);// 可用量增加(加法)
								}
							}
							
						}
					}
					if (flag) {
						platOrderDao.delete(orderIds[i]);// 删除订单
						couponDetailDao.delete(orderIds[i]);// 删除优惠明细
						invoiceDao.delete(orderIds[i]);// 删除发票信息
						successcount++;
					}

					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					MisUser misUser = misUserDao.select(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					if (flag) {
						logRecord.setOperatContent("删除成功");
					} else {
						logRecord.setOperatContent("删除失败，订单存在已审核，不能直接删除");
					}
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(13);// 13删除
					logRecord.setTypeName("删除");
					logRecord.setOperatOrder(orderIds[i]);
					logRecordDao.insert(logRecord);
				}

			}
			message = "删除成功！本次成功删除" + successcount + "条订单，失败" + fallcount + "条订单";
			resp = BaseJson.returnRespObj("ec/platOrder/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/delete 异常说明：", e);
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
				message = "查询成功！";
			} else {
				isSuccess = false;
				message = "编号" + orderId + "不存在！";
			}
			resp = BaseJson.returnRespObjList("ec/platOrder/query", isSuccess, message, proAct, proActsList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/query 异常说明：", e);
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
			resp = BaseJson.returnRespList("ec/platOrder/queryList", true, "查询成功！", count, pageNo, pageSize, 0, 0,
					proList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/queryList 异常说明：", e);
		}
		return resp;
	}
	
	@Override
	public String batchSelect(List<String> list,String isAudit) {
		String resp = "";
		try {
			List<PlatOrder> proList = platOrderDao.batchList(list,isAudit);
			
			resp = BaseJson.returnRespList("ec/platOrder/batchSelect", true, "查询成功！", proList.size(), 1, proList.size()+1, 0, 0,
					proList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/batchSelect 异常说明：", e);
		}
		return resp;
	}

	private static int downloadCount = 0;// 响应给用户共下载多少条

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Override
	public String download(String userId, String startDate, String endDate, int pageNo, int pageSize, String storeId ) {
		String resp = "";
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		// 判断当前店铺id属于哪个平台
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
					resp = BaseJson.returnRespObj("ec/platOrder/download", false, "订单下载异常，请重试", null);
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
					resp=BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载异常，请重试", null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			break;
		
		default:
			try {
				resp = BaseJson.returnRespObj("ec/platOrder/download", true, "当前店铺不支持下载订单", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

		return resp;
	}

	/**
	 * 京东单条订单下载
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	@Override
	public String jdDownloadByOrderId(String userId, Map map) {
		String resp = "";
		String message = "下载完成!";
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
			// 判断下载是否出现错误，有错误时直接返回。
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
					// 先判断订单号是否已经存在
					// //System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
					if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
						PlatOrder platOrder = new PlatOrder();
						int goodNum = 0;
						String orderId = getOrderNo.getSeqNo("ec", userId);

						// 订单明细list
						ArrayNode itemInfoList = (ArrayNode) orderInfo.get("itemInfoList");
						// 整单总金额
						/*
						 * BigDecimal moneySum = new BigDecimal(0); for (Iterator<JsonNode>
						 * itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
						 * .hasNext();) { JsonNode itemInfo = itemInfoInfoIterator.next();
						 * //计算整单按京东价算的总金额 BigDecimal goodPrice = new
						 * BigDecimal(itemInfo.get("jdPrice").asDouble()*itemInfo.get("itemTotal").asInt
						 * ()); moneySum.add(goodPrice); }
						 */
						// 订单优惠明细
						List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
						if (orderInfo.get("sellerDiscount").asDouble() != 0) {
							ArrayNode couponIterator = (ArrayNode) orderInfo.get("couponDetailList");
							if (couponIterator != null) {
								for (Iterator<JsonNode> it = couponIterator.iterator(); it.hasNext();) {
									JsonNode coupons = it.next();
									// //System.err.println("_________优惠明细：" + coupons.toString());
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
						platOrder.setEcOrderId(orderInfo.get("orderId").asText());// 电商订单号
						for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
								.hasNext();) {
							JsonNode itemInfo = itemInfoInfoIterator.next();
							PlatOrders platOrders = new PlatOrders();
							platOrders.setGoodId(itemInfo.get("wareId").asText());// 店铺商品编码
							platOrders.setGoodNum(itemInfo.get("itemTotal").asInt());// 商品数量
							platOrders.setGoodName(itemInfo.get("skuName").asText());// 平台商品名称
							platOrders.setGoodPrice(new BigDecimal(itemInfo.get("jdPrice").asDouble()));// 商品单价 京东价
							// platOrders.setPayMoney(new
							// BigDecimal(itemInfo.get("productNo").asDouble()));// 实付金额
							platOrders.setGoodSku(itemInfo.get("skuId").asText());// 商品sku
							platOrders.setOrderId(orderId);
							platOrders.setEcOrderId(platOrder.getEcOrderId());
							platOrders.setGoodMoney(
									platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// 商品金额
							platOrders.setDeliverWhs(itemInfo.get("newStoreId").asText());// 订单明细中的发货仓id
							platOrders.setPayMoney(platOrders.getGoodMoney());
							if (platOrders.getDeliverWhs().equals("0")) {
								platOrders.setDeliverWhs("");
							} else {
								Map map1 = new HashMap();
								map1.put("type", "JD");
								map1.put("online", platOrders.getDeliverWhs());
								//System.out.println("明细仓库对应online：" + platOrder.getStoreId());
								String newWhs = PlatWhsMappDao.select(map1);
								//System.out.println("明细仓库对应：" + newWhs);
								if (newWhs != null) {
									platOrders.setDeliverWhs(newWhs);
								}else {
									platOrder.setAuditHint("平台仓发货订单缺少平台仓映射,所需平仓编码："+platOrders.getDeliverWhs());
								}
							}
							platOrdersList1.add(platOrders);

							platOrder.setHasGift(0);// ‘是否含赠品，0不含，1含
							goodNum += itemInfo.get("itemTotal").asInt();
						}
						StoreRecord storeRecord = storeRecordDao.select(storeId);
						////System.err.println("storeID::" + storeId);
						//判断订单状态是否发货
						if(orderInfo.get("orderState").asText().equals("WAIT_SELLER_STOCK_OUT")) {
							//等待发货
							platOrder.setIsShip(0);// ‘是否发货
						}else {
							platOrder.setIsShip(1);// ‘是否发货
							if(orderInfo.has("waybill")){
								platOrder.setExpressNo(orderInfo.get("waybill").asText());
							}
							
						}
						platOrder.setOrderId(orderId);// 订单编号
						platOrder.setStoreId(storeId);// 店铺编号
						platOrder.setStoreName(storeRecord.getStoreName());// 店铺名称
						if (orderInfo.has("paymentConfirmTime")) {
							//判断订单是否存在付款时间，货到付款订单没有付款时间
							platOrder.setPayTime(orderInfo.get("paymentConfirmTime").asText());// 付款时间
						}
						platOrder.setTradeDt(orderInfo.get("orderStartTime").asText());// 下单时间
						platOrder.setIsAudit(0);// ’是否审核，0未审核，1审核
						platOrder.setGoodNum(goodNum);// 商品总数量
						platOrder.setGoodMoney(new BigDecimal(orderInfo.get("orderTotalPrice").asDouble()));// 订单总金额
						platOrder.setPayMoney(new BigDecimal(orderInfo.get("orderPayment").asDouble()));// 买家实际付款金额
						platOrder.setBuyerNote(orderInfo.get("orderRemark").asText());// 买家备注
						platOrder.setSellerNote(orderInfo.get("venderRemark").asText());// 商家备注
						platOrder.setDeliveryType(orderInfo.get("deliveryType").asText());// 设置送货类型
						platOrder.setVenderId(orderInfo.get("venderId").asText());// 设置商家ID商家编码
						platOrder.setBuyerId(orderInfo.get("pin").asText());// 买家会员号
						platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("orderSellerPrice").asDouble()));// 结算金额
						JsonNode consigneeInfo = orderInfo.get("consigneeInfo");
						platOrder.setRecAddress(consigneeInfo.get("fullAddress").asText());// ’收货人详细地址
						platOrder.setRecName(consigneeInfo.get("fullname").asText());// ‘收货人姓名
						platOrder.setRecMobile(consigneeInfo.get("mobile").asText());// ‘收货人手机号
						platOrder.setProvince(consigneeInfo.get("province").asText());// 收货人省
						platOrder.setProvinceId(consigneeInfo.get("provinceId").asText());// 收货人省id
						platOrder.setCity(consigneeInfo.get("city").asText());// 收货人市
						platOrder.setCityId(consigneeInfo.get("cityId").asText());// 收货人市id
						platOrder.setCounty(consigneeInfo.get("county").asText());// 收货人区县
						platOrder.setCountyId(consigneeInfo.get("countyId").asText());// 收货人区县id
						platOrder.setDeliverWhs(orderInfo.get("storeId").asText());
						platOrder.setSellTypId("1");// 设置销售类型普通销售
						platOrder.setBizTypId("2");// 设置业务类型2c
						platOrder.setRecvSendCateId("6");// 设置收发类别
						if (platOrder.getDeliverWhs().equals("0")) {
							// 发货仓库编码为0，说明此订单是自发货订单
							platOrder.setDeliverWhs("");
							platOrder.setDeliverSelf(1);//设置此订单为自发货订单
						} else {
							Map map2 = new HashMap();
							map2.put("type", "JD");
							map2.put("online", platOrder.getDeliverWhs());
							String newWhs1 = PlatWhsMappDao.select(map2);
							// System.out.println("主表仓库对应：" + newWhs1);
							if (newWhs1 != null) {
								platOrder.setDeliverWhs(newWhs1);
							} else {
								platOrder.setDeliverWhs("");
							}
							platOrder.setDeliverSelf(0);//设置此订单为平台仓发货订单
						}
						if (consigneeInfo.has("town")) {
							platOrder.setTown(consigneeInfo.get("town").asText());
							platOrder.setTownId(consigneeInfo.get("townId").asText());
						}

						if (orderInfo.get("invoiceInfo").asText().equals("不需要开具发票")) {
							platOrder.setIsInvoice(0);// ‘是否开票
						} else {
							platOrder.setIsInvoice(1);// ’是否开票
							InvoiceInfo invoiceInfo = new InvoiceInfo();
							JsonNode invoice = orderInfo.get("invoiceEasyInfo");
							// //System.err.println("__________发票信息：" + invoice.toString());
							invoiceInfo = JacksonUtil.getPOJO((ObjectNode) invoice, InvoiceInfo.class);
							invoiceInfo.setOrderId(platOrder.getEcOrderId());
							invoiceInfo.setPlatId("JD");
							invoiceInfo.setShopId(storeId);
							platOrder.setInvoiceTitle(orderInfo.get("invoiceEasyInfo").get("invoiceTitle").asText());// 发票抬头
							invoiceList.add(invoiceInfo);
						}
						/* 通过storeOrder是否等于京仓订单判断是否自发货不正确，一盘货订单属于京东发货，但此字段值不等于京仓订单2019年12月9日
						 * if (orderInfo.get("storeOrder").asText().equals("京仓订单")) {
						 * 
						 * platOrder.setDeliverSelf(0);// 设置订单为平台仓发货订单 } else {
						 * platOrder.setDeliverSelf(1);// 设置订单为自发货订单 }
						 */

						platOrder.setFreightPrice(new BigDecimal(orderInfo.get("freightPrice").asDouble()));// 运费
						platOrder.setNoteFlag(0);// ‘商家备注旗帜，默认值，表示无旗帜
						platOrder.setIsClose(0);// ‘是否关闭
						// platOrder.setIsShip(0);// ‘是否发货
						// platOrder.setAdjustMoney(new
						// BigDecimal(orderInfo.get("balanceUsed").asDouble()));// 商家调整金额
						platOrder.setAdjustMoney(new BigDecimal(0));
						platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("sellerDiscount").asDouble()));// 优惠金额
						// platOrder.setGoodPrice(new BigDecimal(0L));// 商品单价
						// platOrder.setPayPrice(new BigDecimal(0L));// 实付单价

						platOrder.setOrderStatus(0);// ‘订单状态
						platOrder.setReturnStatus(0);// ‘退货状态 设置订单未发生退货
						platOrder.setDownloadTime(sdf.format(new Date()));// 设置下载时间
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
									// 匹配仓库成功后匹配快递公司
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
							// 匹配仓库成功后分配批次及扣除可用量
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
								// aa大小不等于明细列表size时，说明明细内容未扣减过可用量及批次
								Map map4 = checkCanUseNumAndBatch(platOrdersList);
								if (map4.get("flag").toString().equals("true")) {
									// 分配批次成功
									platOrdersList.clear();
									platOrdersList.addAll((List<PlatOrders>) map4.get("platOrders"));
								} else {
									platOrder.setAuditHint(map4.get("message").toString());
									// 日志记录
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(userId);
									MisUser misUser = misUserDao.select(userId);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("自动匹配失败：" + platOrder.getAuditHint());
									logRecord.setOperatTime(sdf.format(new Date()));
									logRecord.setOperatType(11);// 11自动匹配
									logRecord.setTypeName("自动匹配");
									logRecord.setOperatOrder(platOrder.getEcOrderId());
									logRecordDao.insert(logRecord);
								}
							}
						}*/
						/*boolean ff = true;// 声明旗标，判断订单明细中是否存在未匹配到存货编码的内容。
						BigDecimal allCount = new BigDecimal("0");// 整单商品数量
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
					
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					MisUser misUser = misUserDao.select(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(
							"本次下载订单" + platOrderList.size() + "条");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(9);// 9手工下载 10 自动下载
					logRecord.setTypeName("手工下载");
					logRecord.setOperatOrder(map.get("orderId").toString());
					logRecordDao.insert(logRecord);
					
					
					for (int i = 0; i < platOrderList.size(); i++) {
						//执行自动匹配
						autoMatch(platOrderList.get(i).getOrderId(), userId);
					}
					
					
				} else {
					success = false;
					message = "所下载订单不属于当前店铺或超出系统允许下载时间范围";
				}
			} else {
				success = false;
				message = "错误提示: " + resultJson.get("apiResult").get("chineseErrCode").asText();
			}
			resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
			// resp=resultJson.toString();
		} catch (Exception e) {
			logger.error("URL：ec/platOrder/download 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}
	//matchSalesPromotions
	@Autowired
	SalesPromotionActivityUtil activityUtil;
	// 店铺编号 storeId
	private String tmdownloadByOrderId(String tid, String userId, String storeId)
			throws NoSuchAlgorithmException, IOException, CloneNotSupportedException {

		String resp = "";
		String message = "";
		try {
			// 设置
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			// 信息
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

			platOrder.setEcOrderId(resultJsons.getString("tid"));// 电商订单号
			platOrder.setOrderId(orderId); // 订单编号
			platOrder.setStoreId(storeId); // 店铺编号
			platOrder.setStoreName(storeRecord.getStoreName()); // '店铺名称' 1
			platOrder.setPayTime(resultJsons.getString("pay_time")); // 付款时间' t paytime
//					platOrder.setWaif(waif);		//	'旗标' 			
			platOrder.setIsAudit(0); // '是否客审'
//					platOrder.setAuditHint(auditHint); // '审核提示'

			platOrder.setGoodMoney(resultJsons.getBigDecimal("total_fee")); // 商品金额' t totalfee
			platOrder.setPayMoney(resultJsons.getBigDecimal("payment")); // '实付金额' t payment
			platOrder.setBuyerNote(resultJsons.getString("buyer_message")); // 买家留言' t buyermessage
			platOrder.setSellerNote(resultJsons.getString("seller_memo")); // 卖家备注'

			platOrder.setRecAddress(resultJsons.getString("receiver_address")); // 收货人详细地址' t receiveraddress
//					platOrder.setBuyerId(buyerId); // '买家会员号'
			platOrder.setRecName(resultJsons.getString("receiver_name")); // 收货人姓名' t receivername
			platOrder.setRecMobile(resultJsons.getString("receiver_mobile")); // 收货人手机号' t receivermobile
			if (resultJsons.containsKey("invoice_name")) {
				platOrder.setIsInvoice(1); // '是否开票'
				platOrder.setInvoiceTitle(resultJsons.getString("invoice_name")); // '发票抬头'
			}
			platOrder.setNoteFlag(resultJsons.getInteger("seller_flag")); // 卖家备注旗帜' t sellerflag
			platOrder.setIsClose(0); // '是否关闭'
			if (resultJsons.getString("status").equals("WAIT_SELLER_SEND_GOODS")) {
				platOrder.setIsShip(0); // '是否发货'
			} else {
				platOrder.setIsShip(1); // '是否发货'
			}
			platOrder.setAdjustMoney(resultJsons.getBigDecimal("adjust_fee")); // 卖家调整金额' t
																				// adjustfee
			platOrder.setDiscountMoney(resultJsons.getBigDecimal("discount_fee")); // 系统优惠金额' t
																					// discountfee
			platOrder.setOrderStatus(0); // '订单状态'
			platOrder.setReturnStatus(0); // '退货状态'
//					platOrder.setHasGift(0); // '是否含赠品'
//					platOrder.setMemo(memo); // '备注'
//					platOrder.setAdjustStatus(adjustStatus); // '调整标识'
			platOrder.setTradeDt(resultJsons.getString("created")); // '交易时间' t created
			platOrder.setSellTypId("1");// 设置销售类型普通销售
			platOrder.setBizTypId("2");// 设置业务类型2c
			platOrder.setRecvSendCateId("6");// 设置收发类别

//					platOrder.setRecvSendCateId(recvSendCateId); // '收发类别编号'
			platOrder.setOrderSellerPrice(resultJsons.getBigDecimal("received_payment")); // 结算金额（订单实际收入金额）'
																							// t
																							// receivedpayment
			platOrder.setProvince(resultJsons.getString("receiver_state")); // 省' t receiverstate
//					platOrder.setProvinceId(provinceId); // '省id'
			platOrder.setCity(resultJsons.getString("receiver_city")); // 市' t receivercity
//					platOrder.setCityId(cityId); //
			platOrder.setCounty(resultJsons.getString("receiver_district")); // 区' t receiverdistrict
//					platOrder.setCountyId(countyId); //
//					platOrder.setTown(town); // '镇'
//					platOrder.setTownId(townId); // '镇id'
			platOrder.setFreightPrice(resultJsons.getBigDecimal("post_fee")); // 运费' t postfee
			platOrder.setVenderId("TM"); // '商家id'
//					orders	Order[]	
			platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//					 platOrder.setDeliveryType("任意时间"); //'送货（日期）类型（1-只工作日送货(双休日、假日不用送;2-只双休日、假日送货(工作日不用送;3-工作日、双休日与假日均可送货;其他值-返回“任意时间”  ）'

			List<PlatOrders> list = new ArrayList<PlatOrders>();

			JSONArray orderInfoList = resultJsons.getJSONObject("orders").getJSONArray("order");
			for (int i = 0; i < orderInfoList.size(); i++) {
				JSONObject itemInfo = orderInfoList.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
				////System.err.println("orderInfo\t" + itemInfo);
				PlatOrders platOrders = new PlatOrders();
				platOrders.setGoodId(itemInfo.getString("num_iid")); // 平台商品编号 商品数字ID
				platOrders.setGoodName(itemInfo.getString("title")); // '平台商品名称' t title String 山寨版测试机器 商品标题
				platOrders.setGoodNum(itemInfo.getInteger("num")); // '商品数量' t num Number 1 购买数量。取值范围:大于零的整数

				platOrders.setGoodMoney(itemInfo.getBigDecimal("num").multiply(itemInfo.getBigDecimal("price"))); // 商品金额'
																													// t
				if (orderInfoList.size() == 1) {
					platOrders.setPayMoney(
							itemInfo.getBigDecimal("divide_order_fee").subtract(resultJsons.getBigDecimal("post_fee"))); // '实付金额'分摊之后的实付金额
																															// 3.
																															// 获取到的Order中的payment字段在单笔子订单时包含物流费用，多笔子订单时不包含物流费用
				} else {
					platOrders.setPayMoney(itemInfo.getBigDecimal("divide_order_fee")); // '实付金额'分摊之后的实付金额
				}
				platOrders.setGoodSku(itemInfo.getString("sku_id")); // '商品sku'
				platOrders.setOrderId(orderId); // '订单编号' 1
//						platOrders.setBatchNo(batchNo);	//	'批号' 	
				platOrders.setExpressCom(itemInfo.getString("logistics_company")); // 快递公司' t子订单发货的快递公司名称
//						promotion_details		platOrders.setProActId(proActId);	//	促销活动编号' t promotionid	String	mjs	优惠id
//						platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("discount_fee").asDouble())); // 系统优惠金额'
				platOrders.setAdjustMoney(itemInfo.getBigDecimal("adjust_fee")); // 买家调整金额' t
				platOrders.setMemo(itemInfo.getString("oid")); // '备注' 该 子单id oid
				platOrders.setGoodPrice(itemInfo.getBigDecimal("price")); // '商品单价' t price
				platOrders.setDeliverWhs(itemInfo.getString("store_code")); // 发货仓库编码'
				platOrders.setEcOrderId(resultJsons.getString("tid")); // '平台订单号' t oid

				if (itemInfo.getString("shipper") == null) {
					platOrder.setDeliverSelf(1); // '订单是否自发货，0平台仓发货，1自发货' t
				} else if (itemInfo.getString("shipper").equals("cn") || itemInfo.getString("shipper").equals("hold")) {
					platOrder.setDeliverSelf(0); // '订单是否自发货，0平台仓发货，1自发货' t
				} else {
					platOrder.setDeliverSelf(1); // '订单是否自发货，0平台仓发货，1自发货' t
				}
				// 字段 shipper （cn:菜鸟仓发货，seller:商家仓发货，hold:待确认发货仓库，null(空值）：商家仓发货）
				////System.err.println("itemInfo.get(\"shipper\")" + itemInfo.getShortValue("shipper"));
//						platOrders.setInvId(itemInfo.get("outer_iid") == null ? null : itemInfo.get("outer_iid").asText()); // 存货编码'// 商家外部编码

				// 循环匹配商品档案
				DecimalFormat dFormat = new DecimalFormat("0.00");
				boolean flag = true;

				// 存货编码 平台商品表good_record中的平台商品编码；
				String ecgoodid = platOrders.getGoodId();// 平台商品编号
				String goodsku = platOrders.getGoodSku();
				GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
				if (goodRecord == null) {
					platOrder.setAuditHint("在店铺商品档案中未匹配到对应商品档案");
					////System.err.println("在店铺商品档案中未匹配到对应商品档案");

					// platOrderDao.update(platOrder);//更新订单的审核提示；
					flag = false;
					list.add(platOrders);
				} else if (goodRecord.getGoodId() == null) {
					platOrder.setAuditHint("请完善店铺商品档案中存货编码的对应关系");
					////System.err.println("请完善店铺商品档案中存货编码的对应关系");

					// platOrderDao.update(platOrder);//更新订单的审核提示；
					flag = false;
					list.add(platOrders);
				} else {
					String invId = goodRecord.getGoodId();
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// 去存货档案查询存货基本信息
					if (invtyDoc == null) {
						platOrder.setAuditHint("在存货档案中未匹配到对应存货档案,需匹配的存货编码：" + invId);
						// platOrderDao.update(platOrder);//更新订单的审核提示；
						flag = false;
						////System.err.println("在存货档案中未匹配到对应存货档案,需匹配的存货编码：" + invId);

						list.add(platOrders);
					} else {
						////System.err.println("是否赠品\t" + platOrders.getPayMoney());

						if (platOrders.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {// 赠品
							// 设置是否赠品
							platOrders.setIsGift(1);
							platOrder.setHasGift(1);
							if (invtyDoc.getPto() != null) {
								if (invtyDoc.getPto() == 1) {
									// 属于PTO类型存货
									ProdStru struck = prodStruDao.selectMomEncd(invId);
									if (struck == null) {
										// 未在产品结构中查询到对应存货的产品结构信息
										platOrder.setAuditHint("在产品结构中未匹配到对应产品结构信息");
										// platOrderDao.update(platOrder);//更新订单的审核提示；
										////System.err.println("在产品结构中未匹配到对应产品结构信息");

										flag = false;
										list.add(platOrders);
									} else {
										if (struck.getIsNtChk() == 0) {
											// 对应产品结构尚未审核
											////System.err.println("对应产品结构尚未审核");
											platOrder.setAuditHint("对应产品结构尚未审核");
											// platOrderDao.update(platOrder);//更新订单的审核提示；
											flag = false;
											list.add(platOrders);
										} else {
											////System.err.println("循环产品结构子表信息");

											List<ProdStruSubTab> strucksublist = struck.getStruSubList();
											// 循环产品结构子表信息
											if (strucksublist.size() == 0) {
												////System.err.println("对应产品结构没有设置子件明细，请先设置。");

												platOrder.setAuditHint("对应产品结构没有设置子件明细，请先设置。");
												// platOrderDao.update(platOrder);//更新订单的审核提示；
												flag = false;
												list.add(platOrders);
											} else if (strucksublist.size() == 1) {
												////System.err.println("当产品结构明细里面只有一个子件时，直接替换存货编码");

												// 当产品结构明细里面只有一个子件时，直接替换存货编码
												// 存货编码
												platOrders.setInvId(strucksublist.get(0).getSubEncd());
												// 设置存货数量
												platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
														.multiply(strucksublist.get(0).getSubQty()).intValue());
												// 设置可退数量
												platOrders.setCanRefNum(platOrders.getInvNum());
												platOrders.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
												platOrders.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
												// 设置实付单价
												platOrders.setPayPrice(BigDecimal.ZERO);
												platOrders.setSellerPrice(platOrders.getPayPrice());// 结算单价与实付单价一样
												// 设置可退金额,与实付金额一致
												platOrders.setCanRefMoney(platOrders.getPayMoney());
												list.add(platOrders);
											} else {
												// 赠品 循环
												////System.err.println(" 赠品 循环");

												for (int j = 0; j < strucksublist.size(); j++) {
													PlatOrders order = (PlatOrders) platOrders.clone();

													InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
															strucksublist.get(j).getSubEncd());

													order.setInvNum((new BigDecimal(order.getGoodNum()))
															.multiply(strucksublist.get(j).getSubQty()).intValue());
													order.setPayMoney(BigDecimal.ZERO);// 设置实付金额 保留两位小数
													// 计算实付单价 保留8位小数
													order.setPayPrice(BigDecimal.ZERO);
													order.setSellerPrice(order.getPayPrice());
													// 存货编码
													order.setInvId(strucksublist.get(j).getSubEncd());
													// 设置可退数量
													order.setCanRefNum(order.getInvNum());
													// 设置可退金额
													order.setCanRefMoney(order.getPayMoney());
													order.setPtoCode(invtyDoc2.getInvtyEncd());// 设置对应母件编码
													order.setPtoName(invtyDoc2.getInvtyNm());// 设置对应母件名称

													list.add(order);

												}
											}

										}
									}
								} else {
									////System.err.println("不属于PTO类型存货");

									// 不属于PTO类型存货
									// 存货编码
									platOrders.setInvId(goodRecord.getGoodId());
									// 设置存货数量
									platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum()).intValue());
									// 设置可退数量
									platOrders.setCanRefNum(platOrders.getInvNum());
									platOrders.setPtoCode("");// 设置对应母件编码
									platOrders.setPtoName("");// 设置对应母件名称
									// 设置实付单价
									platOrders.setPayPrice(BigDecimal.ZERO);
									platOrders.setSellerPrice(BigDecimal.ZERO);// 结算单价与实付单价一样

									// 设置可退金额,与实付金额一致
									platOrders.setCanRefMoney(platOrders.getPayMoney());

									list.add(platOrders);
								}
							} else {
								////System.err.println("存货档案pto属性为空");

								// 存货档案pto属性为空
								platOrder.setAuditHint("存货档案中PTO属性为空");
								// platOrderDao.update(platOrder);//更新订单的审核提示；
								flag = false;
								list.add(platOrders);
							}

						} else {// 非赠品
							platOrders.setIsGift(0);
							////System.err.println("非赠品");

							if (invtyDoc.getPto() != null) {
								if (invtyDoc.getPto() == 1) {
									//////System.err.println("属于PTO类型存货");

									// 属于PTO类型存货
									ProdStru struck = prodStruDao.selectMomEncd(invId);
									if (struck == null) {
										////System.err.println("在产品结构中未匹配到对应产品结构信息");

										// 未在产品结构中查询到对应存货的产品结构信息
										platOrder.setAuditHint("在产品结构中未匹配到对应产品结构信息");
										// platOrderDao.update(platOrder);//更新订单的审核提示；
										flag = false;
										list.add(platOrders);
									} else {
										if (struck.getIsNtChk() == 0) {
											// 对应产品结构尚未审核
											////System.err.println("对应产品结构尚未审核");

											platOrder.setAuditHint("对应产品结构尚未审核");
											// platOrderDao.update(platOrder);//更新订单的审核提示；
											flag = false;
											list.add(platOrders);
										} else {
											List<ProdStruSubTab> strucksublist = struck.getStruSubList();
											// 循环产品结构子表信息
											////System.err.println("循环产品结构子表信息");

											if (strucksublist.size() == 0) {
												////System.err.println("对应产品结构没有设置子件明细，请先设置。");

												platOrder.setAuditHint("对应产品结构没有设置子件明细，请先设置。");
												// platOrderDao.update(platOrder);//更新订单的审核提示；
												flag = false;
												list.add(platOrders);
											} else if (strucksublist.size() == 1) {
												////System.err.println("当产品结构明细里面只有一个子件时，直接替换存货编码");

												// 当产品结构明细里面只有一个子件时，直接替换存货编码
												// 存货编码
												platOrders.setInvId(strucksublist.get(0).getSubEncd());
												// 设置存货数量
												platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
														.multiply(strucksublist.get(0).getSubQty()).intValue());
												// 设置可退数量
												platOrders.setCanRefNum(platOrders.getInvNum());
												platOrders.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
												platOrders.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
												// 设置实付单价
												platOrders.setPayPrice(platOrders.getPayMoney().divide(
														(new BigDecimal(platOrders.getInvNum())), 8,
														BigDecimal.ROUND_HALF_UP));
												platOrders.setSellerPrice(platOrders.getPayPrice());// 结算单价与实付单价一样

												// 设置可退金额,与实付金额一致
												platOrders.setCanRefMoney(platOrders.getPayMoney());

												list.add(platOrders);
											} else {
												// 当产品结构中子件的数量大于1时，需要生成多条明细
												////System.err.println("当产品结构中子件的数量大于1时，需要生成多条明细");

												// 计算子件参考成本与子件数量的总成本
												// 声明旗标，如果false说明对应子件的参考成本未设置，系统无法拆分比例
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
													// 子件的参考成本未设置，系统无法拆分比例
													platOrder.setAuditHint("对应子件的参考成本未设置，系统无法拆分比例");
													list.add(platOrders);
												} else {

													// 非赠品 循环
													BigDecimal money123 = new BigDecimal("0");// 设置已分实付金额
																								// 最后一条用减法时用到
													for (int j = 0; j < strucksublist.size(); j++) {
														PlatOrders order = (PlatOrders) platOrders.clone();

														if (j + 1 < strucksublist.size()) {
															// 计算每条子件占总成本的比例 保留8位小数
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
																	.format(order.getPayMoney().multiply(rate))));// 设置实付金额
																													// 保留两位小数
															// 计算实付单价 保留8位小数
															order.setPayPrice(order.getPayMoney().divide(
																	(new BigDecimal(order.getInvNum())), 8,
																	BigDecimal.ROUND_HALF_UP));
															order.setSellerPrice(order.getPayPrice());
															// 存货编码
															order.setInvId(strucksublist.get(j).getSubEncd());
															// 设置可退数量
															order.setCanRefNum(order.getInvNum());
															// 设置可退金额
															order.setCanRefMoney(order.getPayMoney());
															order.setPtoCode(invtyDoc2.getInvtyEncd());// 设置对应母件编码
															order.setPtoName(invtyDoc2.getInvtyNm());// 设置对应母件名称
															// 设置是否赠品

															list.add(order);
															money123 = money123.add(order.getPayMoney());
														} else {
															order.setInvNum((new BigDecimal(order.getGoodNum()))
																	.multiply(strucksublist.get(j).getSubQty())
																	.intValue());
															order.setPayMoney(order.getPayMoney().subtract(money123));// 设置实付金额
																														// 保留两位小数
															// 计算实付单价 保留8位小数
															order.setPayPrice(order.getPayMoney().divide(
																	(new BigDecimal(order.getInvNum())), 8,
																	BigDecimal.ROUND_HALF_UP));
															order.setSellerPrice(order.getPayPrice());
															// 存货编码
															order.setInvId(strucksublist.get(j).getSubEncd());
															// 设置可退数量
															order.setCanRefNum(order.getInvNum());
															// 设置可退金额
															order.setCanRefMoney(order.getPayMoney());
															order.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
															order.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
															// 设置是否赠品
															order.setIsGift(0);

															list.add(order);
														}

													}
												}
											}

										}
									}
								} else {
									////System.err.println("不属于PTO类型存货");

									// 不属于PTO类型存货
									// 存货编码
									platOrders.setInvId(goodRecord.getGoodId());
									// 设置存货数量
									platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum()).intValue());
									// 设置可退数量
									platOrders.setCanRefNum(platOrders.getInvNum());
									platOrders.setPtoCode("");// 设置对应母件编码
									platOrders.setPtoName("");// 设置对应母件名称
									// 设置实付单价
									platOrders.setPayPrice(platOrders.getPayMoney().divide(
											(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
									platOrders.setSellerPrice(platOrders.getPayPrice());// 结算单价与实付单价一样
									// 根据实付单价是否为0判断是否为赠品
									platOrders.setIsGift(0);

									// 设置可退金额,与实付金额一致
									platOrders.setCanRefMoney(platOrders.getPayMoney());

									list.add(platOrders);
								}
							} else {
								////System.err.println(" 存货档案pto属性为空");

								// 存货档案pto属性为空
								platOrder.setAuditHint("存货档案中PTO属性为空");
								// platOrderDao.update(platOrder);//更新订单的审核提示；
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
					JSONObject promotion = promotionDetails.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					////System.err.println("promotion\t" + promotion);
					CouponDetail coupon1 = new CouponDetail();
					coupon1.setPlatId("TM");// 平台id，京东JD，天猫JD
					coupon1.setStoreId(storeId);// 店铺id
					coupon1.setOrderId(resultJsons.getString("tid"));// 订单号
					coupon1.setSkuId(null);
					coupon1.setCouponCode(null);// 优惠类型编码
					coupon1.setCouponType(promotion.getString("promotion_name"));// 优惠名称
					coupon1.setCouponPrice(promotion.getBigDecimal("discount_fee"));// 优惠金额
					couponList.add(coupon1);
				}
				couponDetailDao.insert(couponList);
			}
			
			platOrder.setGoodNum(list.size()); // 商品品类数
			////System.err.println("platOrder.setGoodNum(list.size())商品数量\t" + list.size());

			platOrderDao.insert(platOrder);
			platOrdersDao.insert(list);
//					invoiceDao.insert(invoiceList);					
			autoMatch(platOrder.getOrderId(), userId);
			// 订单插入完成后，执行自动匹配
			//PlatOrder platorder = platOrderDao.selectByEcOrderId(resultJsons.getString("tid"));
			//List<PlatOrders> orderslist = platOrdersDao.select(platorder.getOrderId());
			/*if (auditStrategyService.autoAuditCheck(platorder, orderslist)) {
				// 返回true时，此订单通过免审，直接进入审核
				associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
				// 设置默认操作员sys
			}*/
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载成功" + tid, null);

		} catch (Exception e) {
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载失败" + tid, null);
		}
		return resp;
	}
	private String jdDownload(String userId, String startDate, String endDate, int pageNo, int pageSize,
			String storeId,int downloadCount) {
		String resp = "";
		String message = "";
		boolean success = true;
		if (pageNo == 1) {
			// 当下载第一页时，将downloadCount置0
			downloadCount = 0;
		}
		try {
			// String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
			// Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("start_date", startDate);
			objectNode.put("end_date", endDate);
			objectNode.put("dateType", 1);// 下载时间根据 1 订单创建时间 2订单更新时间
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
			////System.err.println("请求参数：" + json);
			//System.out.println(jdRespStr);
			// String jdRespStr = ECHelper.getJD("jingdong.pop.order.search",
			// "0b88dcc2ac604d4fb55fe317f9f5bd94ogrj", json);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			// 判断下载是否出现错误，有错误时直接返回。
			if (jdRespJson.has("error_response")) {
				message = jdRespJson.get("error_response").get("zh_desc").asText();
				success = false;
				resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
				return resp;
			}
			JsonNode resultJson = jdRespJson.get("jingdong_pop_order_search_responce").get("searchorderinfo_result");
			if (resultJson.get("apiResult").get("success").asBoolean()) {
				//主订单list
				List<PlatOrder> platOrderList = new ArrayList();
				List<InvoiceInfo> invoiceList = new ArrayList();
				List<CouponDetail> couponList = new ArrayList();
				// System.out.println(resultJson);
				ArrayNode orderInfoList = (ArrayNode) resultJson.get("orderInfoList");
				// System.out.println(resultJson);
				for (Iterator<JsonNode> orderInfoIterator = orderInfoList.iterator(); orderInfoIterator.hasNext();) {
					//子订单list
					List<PlatOrders> platOrdersList = new ArrayList();

					JsonNode orderInfo = orderInfoIterator.next();
					String ecOrderId = orderInfo.get("orderId").asText();
					// 先判断订单号是否已经存在
					////System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
					if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
						PlatOrder platOrder = new PlatOrder();
						int goodNum = 0;

						//获取订单号
//						String orderId = getOrderNo.getSeqNo("ec", userId);

						// 订单明细list
						ArrayNode itemInfoList = (ArrayNode) orderInfo.get("itemInfoList");
						// 整单总金额
						/*
						 * BigDecimal moneySum = new BigDecimal(0); for (Iterator<JsonNode>
						 * itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
						 * .hasNext();) { JsonNode itemInfo = itemInfoInfoIterator.next();
						 * //计算整单按京东价算的总金额 BigDecimal goodPrice = new
						 * BigDecimal(itemInfo.get("jdPrice").asDouble()*itemInfo.get("itemTotal").asInt
						 * ()); moneySum.add(goodPrice); }
						 */
						// 订单优惠明细
						List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
						if (orderInfo.get("sellerDiscount").asDouble() != 0) {
							ArrayNode couponIterator = (ArrayNode) orderInfo.get("couponDetailList");
							if (couponIterator != null) {
								for (Iterator<JsonNode> it = couponIterator.iterator(); it.hasNext();) {
									JsonNode coupons = it.next();
									// //System.err.println("_________优惠明细：" + coupons.toString());
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
						platOrder.setEcOrderId(orderInfo.get("orderId").asText());// 电商订单号
						List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
						for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
								.hasNext();) {
							JsonNode itemInfo = itemInfoInfoIterator.next();
							PlatOrders platOrders = new PlatOrders();
							platOrders.setGoodId(itemInfo.get("wareId").asText());// 店铺商品编码
							platOrders.setGoodNum(itemInfo.get("itemTotal").asInt());// 商品数量
							platOrders.setGoodName(itemInfo.get("skuName").asText());// 平台商品名称
							platOrders.setGoodPrice(new BigDecimal(itemInfo.get("jdPrice").asDouble()));// 商品单价 京东价
							// platOrders.setPayMoney(new
							// BigDecimal(itemInfo.get("productNo").asDouble()));// 实付金额
							platOrders.setGoodSku(itemInfo.get("skuId").asText());// 商品sku
							//字表订单号
//							platOrders.setOrderId(orderId);
							platOrders.setEcOrderId(platOrder.getEcOrderId());

							platOrders.setGoodMoney(
									platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// 商品金额
							platOrders.setDeliverWhs(itemInfo.get("newStoreId").asText());// 订单明细中的发货仓id
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
									platOrder.setAuditHint("平台仓发货订单缺少平台仓映射,所需平仓编码："+platOrders.getDeliverWhs());
								}
							}
							// BigDecimal num = new
							// BigDecimal(orderInfo.get("orderSellerPrice").asDouble()*itemInfo.get("jdPrice").asDouble());
							// platOrders.setSellerPrice(num.divide(moneySum));
							// private String batchNo;//批号
							// private String expressCom;//快递公司
							/*
							 * if (orderInfo.get("storeOrder").asText().equals("京仓订单")) { //
							 * platOrders.setDeliverWhs("京东仓总仓");// ’发货仓库 }
							 */
							// private String proActId;//促销活动编号
							// private Long discountMoney;//系统优惠金额
							// private String adjustMoney;//卖家调整金额

							// itemInfo.get("skuName").asText();
							platOrdersList1.add(platOrders);

							platOrder.setHasGift(0);// ‘是否含赠品，0不含，1含
							goodNum += itemInfo.get("itemTotal").asInt();
						}
						StoreRecord storeRecord = storeRecordDao.select(storeId);
						//判断订单状态是否发货
						if(orderInfo.get("orderState").asText().equals("WAIT_SELLER_STOCK_OUT")) {
							//等待发货
							platOrder.setIsShip(0);// ‘是否发货
						}else {
							platOrder.setIsShip(1);// ‘是否发货
							if(orderInfo.has("waybill")){
								platOrder.setExpressNo(orderInfo.get("waybill").asText());
							}
							
						}
						//主表订单号
//						platOrder.setOrderId(orderId);// 订单编号
						platOrder.setStoreId(storeId);// 店铺编号
						platOrder.setStoreName(storeRecord.getStoreName());// 店铺名称
						if (orderInfo.has("paymentConfirmTime")) {
							//判断订单是否存在付款时间，货到付款订单没有付款时间
							platOrder.setPayTime(orderInfo.get("paymentConfirmTime").asText());// 付款时间
						}
						platOrder.setTradeDt(orderInfo.get("orderStartTime").asText());// 下单时间
						platOrder.setIsAudit(0);// ’是否审核，0未审核，1审核
						platOrder.setGoodNum(goodNum);// 商品总数量
						platOrder.setDeliveryType(orderInfo.get("deliveryType").asText());// 设置送货类型
						platOrder.setVenderId(orderInfo.get("venderId").asText());// 设置商家ID商家编码
						platOrder.setGoodMoney(new BigDecimal(orderInfo.get("orderTotalPrice").asDouble()));// 订单总金额
						platOrder.setPayMoney(new BigDecimal(orderInfo.get("orderPayment").asDouble()));// 买家实际付款金额
						platOrder.setBuyerNote(orderInfo.get("orderRemark").asText());// 买家备注
						platOrder.setSellerNote(orderInfo.get("venderRemark").asText());// 商家备注
						platOrder.setBuyerId(orderInfo.get("pin").asText());// 买家会员号
						platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("orderSellerPrice").asDouble()));// 结算金额
						JsonNode consigneeInfo = orderInfo.get("consigneeInfo");
						platOrder.setRecAddress(consigneeInfo.get("fullAddress").asText());// ’收货人详细地址
						platOrder.setRecName(consigneeInfo.get("fullname").asText());// ‘收货人姓名
						platOrder.setRecMobile(consigneeInfo.get("mobile").asText());// ‘收货人手机号
						platOrder.setProvince(consigneeInfo.get("province").asText());// 收货人省
						platOrder.setProvinceId(consigneeInfo.get("provinceId").asText());// 收货人省id
						platOrder.setCity(consigneeInfo.get("city").asText());// 收货人市
						platOrder.setCityId(consigneeInfo.get("cityId").asText());// 收货人市id
						if (consigneeInfo.has("county")) {
							platOrder.setCounty(consigneeInfo.get("county").asText());// 收货人区县
							platOrder.setCountyId(consigneeInfo.get("countyId").asText());// 收货人区县id
						}
						platOrder.setDeliverWhs(orderInfo.get("storeId").asText());
						platOrder.setSellTypId("1");// 设置销售类型普通销售
						platOrder.setBizTypId("2");// 设置业务类型2c
						platOrder.setRecvSendCateId("6");// 设置收发类别
						// 拿订单中的发货仓库去平台仓映射中兑换系统仓库编码
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
								platOrder.setAuditHint("平台仓发货订单缺少平台仓映射");
							}
							platOrder.setDeliverSelf(0);
						}
						if (consigneeInfo.has("town")) {
							platOrder.setTown(consigneeInfo.get("town").asText());
							platOrder.setTownId(consigneeInfo.get("townId").asText());
						}

						if (orderInfo.get("invoiceInfo").asText().equals("不需要开具发票")) {
							platOrder.setIsInvoice(0);// ‘是否开票
						} else {
							platOrder.setIsInvoice(1);// ’是否开票
							InvoiceInfo invoiceInfo = new InvoiceInfo();
							JsonNode invoice = orderInfo.get("invoiceEasyInfo");
							////System.err.println("__________发票信息：" + invoice.toString());
							/*
							 * invoiceInfo.setInvoiceCode(invoice.get("invoiceCode").asText());
							 * invoiceInfo.setInvoiceConsigneeEmail(invoice.get("invoiceConsigneeEmail").
							 * asText()); invoiceInfo.setInvoiceConsigneePhone(invoice.get(""));
							 */
							invoiceInfo = JacksonUtil.getPOJO((ObjectNode) invoice, InvoiceInfo.class);
							invoiceInfo.setOrderId(platOrder.getEcOrderId());
							invoiceInfo.setPlatId("JD");
							invoiceInfo.setShopId(storeId);
							platOrder.setInvoiceTitle(orderInfo.get("invoiceEasyInfo").get("invoiceTitle").asText());// 发票抬头
							invoiceList.add(invoiceInfo);
						}
						/*通过storeOrder是否等于京仓订单判断是否自发货不正确，一盘货订单属于京东发货，但此字段值不等于京仓订单2019年12月9日
						 * if (orderInfo.get("storeOrder").asText().equals("京仓订单")) {
						 * platOrder.setDeliverSelf(0);// 设置订单为 } else { platOrder.setDeliverSelf(1);//
						 * 设置订单为自发货订单 }
						 */
						platOrder.setFreightPrice(new BigDecimal(orderInfo.get("freightPrice").asDouble()));// 运费
						platOrder.setNoteFlag(0);// ‘商家备注旗帜，默认值，表示无旗帜
						platOrder.setIsClose(0);// ‘是否关闭
						// platOrder.setIsShip(0);// ‘是否发货
						// platOrder.setAdjustMoney(new
						// BigDecimal(orderInfo.get("balanceUsed").asDouble()));// 商家调整金额
						platOrder.setAdjustMoney(new BigDecimal(0));
						platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("sellerDiscount").asDouble()));// 优惠金额
						// platOrder.setGoodPrice(new BigDecimal(0L));// 商品单价
						// platOrder.setPayPrice(new BigDecimal(0L));// 实付单价

						platOrder.setOrderStatus(0);// ‘订单状态
						platOrder.setReturnStatus(0);// ‘退货状态
						platOrder.setDownloadTime(sdf.format(new Date()));// 设置下载时间
						//字表list
						platOrdersList.addAll(checkInvty(platOrder, platOrdersList1, couponDetails));

						/*if (platOrder.getDeliverSelf() == 1) {
							platOrder.setPlatOrdersList(platOrdersList);
							Map map2 = orderDealSettingsServiceImpl.matchWareHouse(platOrder, storeRecord.getEcId());
							if (map2.get("isSuccess").toString().equals("true")) {
								platOrder.setDeliverWhs(map2.get("whsCode").toString());
								for (int i = 0; i < platOrdersList.size(); i++) {
									platOrdersList.get(i).setDeliverWhs(map2.get("whsCode").toString());
								}
								// 匹配仓库成功后匹配快递公司
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
							// 匹配仓库成功后分配批次及扣除可用量
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
								// aa大小不等于明细列表size时，说明明细内容未扣减过可用量及批次
								Map map4 = checkCanUseNumAndBatch(platOrdersList);
								if (map4.get("flag").toString().equals("true")) {
									// 分配批次成功
									platOrdersList.clear();
									platOrdersList.addAll((List<PlatOrders>) map4.get("platOrders"));
								} else {
									platOrder.setAuditHint(map4.get("message").toString());
									// 日志记录
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(userId);
									MisUser misUser = misUserDao.select(userId);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("自动匹配失败：" + platOrder.getAuditHint());
									logRecord.setOperatTime(sdf.format(new Date()));
									logRecord.setOperatType(11);// 11自动匹配
									logRecord.setTypeName("自动匹配");
									logRecord.setOperatOrder(platOrder.getEcOrderId());
									logRecordDao.insert(logRecord);
								}
							}
						}*/

						/*boolean ff = true;// 声明旗标，判断订单明细中是否存在未匹配到存货编码的内容。
						BigDecimal allCount = new BigDecimal("0");// 整单商品数量
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

                        //字表List数据set主表  关联订单号
                        platOrder.setPlatOrdersList(platOrdersList);
                        //主表
                        platOrderList.add(platOrder);
                    }
                }

                //生成订单号 + 插入数据库
                List<String> seqNoList = getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId);
                //插入订单 + 自动匹配
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

				// 订单插入完成后，判断是否自动免审
				for (int i = 0; i < platOrderList.size(); i++) {
					//执行自动匹配
					autoMatch(platOrderList.get(i).getOrderId(), userId);
					*//*
					PlatOrder platorder = platOrderDao.selectByEcOrderId(platOrderList.get(i).getEcOrderId());
					List<PlatOrders> orderslist = platOrdersDao.select(platorder.getOrderId());
					boolean ff = true;// 声明旗标，判断订单明细中是否存在未匹配到存货编码的内容。
					for (int j = 0; j < orderslist.size(); j++) {
						if (orderslist.get(j).getInvId() == null || orderslist.get(j).getInvId().equals("")) {
							ff = false;
						}
					}
					if (!ff) {
						continue;
					}
					if (auditStrategyService.autoAuditCheck(platorder, orderslist)) {
						// 返回true时，此订单通过免审，直接进入审核
						associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
						// 设置默认操作员sys
					}*//*
				}*/

                if (resultJson.get("orderTotal").asInt() - pageNo * pageSize > 0) {
                    resp = jdDownload(userId, startDate, endDate, pageNo + 1, pageSize, storeId, downloadCount);
                } else {
                    // 日志记录
                    LogRecord logRecord = new LogRecord();
                    logRecord.setOperatId(userId);
                    MisUser misUser = misUserDao.select(userId);
                    if (misUser != null) {
                        logRecord.setOperatName(misUser.getUserName());
                    }
                    logRecord.setOperatContent("本次自动下载店铺：" + storeSettings.getStoreName() + "订单" + downloadCount + "条");
                    logRecord.setOperatTime(sdf.format(new Date()));
                    logRecord.setOperatType(10);// 9手工下载 10 自动下载
                    logRecord.setTypeName("自动下载");
                    logRecordDao.insert(logRecord);
                    resp = BaseJson.returnRespObj("ec/platOrder/download", success, "本次成功下载" + downloadCount + "条订单", null);
                    return resp;
                }
            } else {
                success = false;
                message = "JD errInfo : " + resultJson.get("apiResult").get("chineseErrCode").asText();
                // 日志记录
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                MisUser misUser = misUserDao.select(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent("本次自动下载店铺：" + storeSettings.getStoreName() + "订单" + downloadCount + "条后出现异常：" + message);
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(10);// 9手工下载 10 自动下载
                logRecord.setTypeName("自动下载");
                logRecordDao.insert(logRecord);
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, message, null);
            }

            // resp=resultJson.toString();
        } catch (Exception e) {
            logger.error("URL：ec/platOrder/download 异常说明：", e);

            success = false;
            StackTraceElement s = e.getStackTrace()[0];
            message = "异常类为：" + s.getClassName() + "  异常方法：" + s.getMethodName() + "()" + "  异常行数：" + s.getLineNumber() + "  异常店铺：" + storeId + "  操作人员：" + userId + "  操作时间段：" + startDate + " --- " + endDate;
            // 日志记录
            LogRecord logRecord = new LogRecord();
            logRecord.setOperatId(userId);
            MisUser misUser = misUserDao.select(userId);
            if (misUser != null) {
                logRecord.setOperatName(misUser.getUserName());
            }
            logRecord.setOperatContent("本次自动下载店铺：" + storeId + "号店铺，订单在" + downloadCount + "条后出现异常：" + message);
            logRecord.setOperatTime(sdf.format(new Date()));
            logRecord.setOperatType(10);// 9手工下载 10 自动下载
            logRecord.setTypeName("自动下载");
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
     * 考拉订单下载
     *
     * @param userId
     * @param startDate
     * @param endDate
     * @param pageNo
     * @param pageSize
     * @param storeId
     * @param orderState 订单状态1(已付款)、2(已发货)、3(已签收)
     * @return
     */
    private String KaoLaDownload(String userId, String startDate, String endDate, int pageNo, int pageSize,
                                 String storeId, String orderState) {
        String resp = "";
        String message = "";
        boolean success = true;
        if (pageNo == 1 && orderState.equals("1")) {
            // 当下载第一页时，将downloadCount置0
            downloadCount = 0;
        }
        try {
            // String userId = BaseJson.getReqHead(jsonBody).get("accNum").asText();
            // Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());

            ObjectNode objectNode = JacksonUtil.getObjectNode("");
            objectNode.put("start_time", startDate);
            objectNode.put("end_time", endDate);
            objectNode.put("order_status", orderState);//订单状态1(已付款)、2(已发货)、3(已签收)、5(取消待确认)、6(已取消)
            objectNode.put("date_type", "1001");// 搜索日期类型1(支付时间)、2(发货时间)、3(签收时间)、1001（待发货时间）、1005(取消待处理时间)
            objectNode.put("page_no", "" + pageNo);
            objectNode.put("page_size", "" + pageSize);

            String json = objectNode.toString();
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            String jdRespStr = ECHelper.getKaola("kaola.order.search", storeSettings, json);
            ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
            //////System.err.println("查询结果："+jdRespJson);

            // 判断下载是否出现错误，有错误时直接返回。
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
                // 先判断订单号是否已经存在
                //////System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
                if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
                    PlatOrder platOrder = new PlatOrder();
                    int goodNum = 0;
                    String orderId = getOrderNo.getSeqNo("ec", userId);

                    // 订单明细list
                    ArrayNode itemInfoList = (ArrayNode) orderInfo.get("order_skus");
                    // 订单优惠明细
                    List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
                    platOrder.setEcOrderId(orderInfo.get("order_id").asText());// 电商订单号
                    List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
                    for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
                            .hasNext(); ) {
                        JsonNode itemInfo = itemInfoInfoIterator.next();
                        PlatOrders platOrders = new PlatOrders();
                        platOrders.setGoodId(itemInfo.get("barcode").asText());// 店铺商品编码
                        platOrders.setGoodNum(itemInfo.get("count").asInt());// 商品数量
                        platOrders.setGoodName(itemInfo.get("product_name").asText());// 平台商品名称
                        platOrders.setGoodPrice(new BigDecimal(itemInfo.get("origin_price").asDouble()));// 商品单价 京东价

                        platOrders.setGoodSku("");// 商品sku
                        platOrders.setOrderId(orderId);
                        platOrders.setEcOrderId(platOrder.getEcOrderId());

                        platOrders.setGoodMoney(platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// 商品金额
                        platOrders.setDeliverWhs("");// 订单明细中的发货仓id
                        platOrders.setPayMoney(new BigDecimal(itemInfo.get("real_totle_price").asDouble()));
                        platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("activity_totle_amount").asDouble()));

                        platOrdersList.add(platOrders);

                        //platOrder.setHasGift(0);// ‘是否含赠品，0不含，1含
                        goodNum += platOrders.getGoodNum();
                    }
                    StoreRecord storeRecord = storeRecordDao.select(storeId);
                    platOrder.setOrderId(orderId);// 订单编号
                    platOrder.setStoreId(storeId);// 店铺编号
                    platOrder.setStoreName(storeRecord.getStoreName());// 店铺名称
                    platOrder.setPayTime(orderInfo.get("pay_success_time").asText());// 付款时间
                    platOrder.setTradeDt(orderInfo.get("order_time").asText());// 下单时间
                    platOrder.setIsAudit(0);// ’是否审核，0未审核，1审核
                    platOrder.setGoodNum(goodNum);// 商品总数量
                    platOrder.setDeliveryType("");// 设置送货类型
                    platOrder.setVenderId("");// 设置商家ID商家编码
                    platOrder.setGoodMoney(new BigDecimal(orderInfo.get("order_origin_price").asDouble()));// 订单总金额
                    platOrder.setPayMoney(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// 买家实际付款金额
                    ////System.err.println("aaaaa:"+orderInfo.get("order_buyer_remark").asText());
                    platOrder.setBuyerNote(StringUtils.isEmpty(orderInfo.get("order_buyer_remark").asText()) || orderInfo.get("order_buyer_remark").asText().equals("null") ? "" : orderInfo.get("order_buyer_remark").asText());// 买家备注
                    platOrder.setSellerNote(StringUtils.isEmpty(orderInfo.get("order_business_remark").asText()) || orderInfo.get("order_business_remark").asText().equals("null") ? "" : orderInfo.get("order_business_remark").asText());// 商家备注
                    platOrder.setBuyerId(orderInfo.get("buyer_account").asText());// 买家会员号
                    platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// 结算金额
                    //JsonNode consigneeInfo = orderInfo.get("consigneeInfo");

                    platOrder.setRecName(orderInfo.get("receiver_name").asText());// ‘收货人姓名
                    platOrder.setRecMobile(orderInfo.get("receiver_phone").asText());// ‘收货人手机号
                    platOrder.setProvince(orderInfo.get("receiver_province_name").asText());// 收货人省
                    platOrder.setProvinceId("");// 收货人省id
                    platOrder.setCity(orderInfo.get("receiver_city_name").asText());// 收货人市
                    platOrder.setCityId("");// 收货人市id
                    platOrder.setCounty(orderInfo.get("receiver_district_name").asText());// 收货人区县
                    platOrder.setCountyId("");// 收货人区县id
                    platOrder.setRecAddress(platOrder.getProvince() + platOrder.getCity() + platOrder.getCounty() + orderInfo.get("receiver_address_detail").asText());// ’收货人详细地址
                    platOrder.setDeliverWhs("");//发货仓库
                    platOrder.setSellTypId("1");// 设置销售类型普通销售
                    platOrder.setBizTypId("2");// 设置业务类型2c
                    platOrder.setRecvSendCateId("6");// 设置收发类别

                    platOrder.setIsInvoice(orderInfo.get("need_invoice").asInt());// ‘是否开票
                    platOrder.setInvoiceTitle(orderInfo.get("invoice_title").asText());// 发票抬头
                    if (orderInfo.get("order_status").asText().equals("1")) {
                        //发货状态为1未发货
                        platOrder.setIsShip(0);// ‘是否发货
                    } else {
                        platOrder.setIsShip(1);// ‘是否发货
                        try {
                            //已发货需要获取快递单号
                            platOrder.setExpressNo(
                                    orderInfo.get("order_expresses").get(0).get("express_no").asText());
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                    platOrder.setDeliverSelf(1);// 设置订单为自发货订单
                    platOrder.setAdjustMoney(BigDecimal.ZERO);//卖家调整金额

                    platOrder.setFreightPrice(new BigDecimal(orderInfo.get("express_fee").asDouble()));// 运费
                    platOrder.setNoteFlag(0);// ‘商家备注旗帜，默认值，表示无旗帜
                    platOrder.setIsClose(0);// ‘是否关闭
                    platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("coupon_amount").asDouble()));
                    platOrder.setOrderStatus(0);// ‘订单状态
                    platOrder.setReturnStatus(0);// ‘退货状态
                    platOrder.setDownloadTime(sdf.format(new Date()));// 设置下载时间
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

            // 订单插入完成后，判断是否自动免审
            for (int i = 0; i < platOrderList.size(); i++) {
                //执行自动匹配
                autoMatch(platOrderList.get(i).getOrderId(), userId);
            }

            if (jdRespJson.get("kaola_order_search_response").get("total_count").asInt() - pageNo * pageSize > 0) {
                resp = KaoLaDownload(userId, startDate, endDate, pageNo + 1, pageSize, storeId, orderState);
            } else if (orderState.equals("1")) {
                resp = KaoLaDownload(userId, startDate, endDate, 1, pageSize, storeId, "2");
            } else if (orderState.equals("2")) {
                resp = KaoLaDownload(userId, startDate, endDate, 1, pageSize, storeId, "3");
            } else {
                // 日志记录
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                MisUser misUser = misUserDao.select(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent("本次自动下载店铺：" + storeSettings.getStoreName() + "订单" + downloadCount + "条");
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(10);// 9手工下载 10 自动下载
                logRecord.setTypeName("自动下载");
                logRecordDao.insert(logRecord);
                resp = BaseJson.returnRespObj("ec/platOrder/download", success, "本次成功下载" + downloadCount + "条订单", null);
                return resp;
            }

            // resp=resultJson.toString();
        } catch (Exception e) {
            logger.error("URL：ec/platOrder/download 异常说明：", e);
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
     * 考拉单条订单下载
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
            ////System.err.println("查询结果："+jdRespJson);

            // 判断下载是否出现错误，有错误时直接返回。
            if (jdRespJson.has("error_response")) {
                message = jdRespJson.get("error_response").get("subErrors").get(0).get("msg").asText();
                success = false;
                resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", success, "下载失败：" + message, null);
                return resp;
            }
            //订单状态1(已付款)、2(已发货)、3(已签收)
            if (jdRespJson.get("kaola_order_get_response").get("order").get("order_status").asText().equals("1") ||
                    jdRespJson.get("kaola_order_get_response").get("order").get("order_status").asText().equals("2") ||
                    jdRespJson.get("kaola_order_get_response").get("order").get("order_status").asText().equals("3")) {


                List<PlatOrder> platOrderList = new ArrayList();
                List<PlatOrders> platOrdersList = new ArrayList();
                List<InvoiceInfo> invoiceList = new ArrayList();
                List<CouponDetail> couponList = new ArrayList();
                // System.out.println(resultJson);
                JsonNode orderInfo = jdRespJson.get("kaola_order_get_response").get("order");
                // 先判断订单号是否已经存在
                ////System.err.println("ecorderId:" + ecOrderId + "____storeId:" + storeId);
                if (platOrderDao.checkExsits(ecOrderId, storeId) == 0) {
                    PlatOrder platOrder = new PlatOrder();
                    int goodNum = 0;
                    String orderId = getOrderNo.getSeqNo("ec", userId);

                    // 订单明细list
                    ArrayNode itemInfoList = (ArrayNode) orderInfo.get("order_skus");
                    // 订单优惠明细
                    List<CouponDetail> couponDetails = new ArrayList<CouponDetail>();
                    platOrder.setEcOrderId(orderInfo.get("order_id").asText());// 电商订单号
                    List<PlatOrders> platOrdersList1 = new ArrayList<PlatOrders>();
                    for (Iterator<JsonNode> itemInfoInfoIterator = itemInfoList.iterator(); itemInfoInfoIterator
                            .hasNext(); ) {
                        JsonNode itemInfo = itemInfoInfoIterator.next();
                        PlatOrders platOrders = new PlatOrders();
                        platOrders.setGoodId(itemInfo.get("barcode").asText());// 店铺商品编码
                        platOrders.setGoodNum(itemInfo.get("count").asInt());// 商品数量
                        platOrders.setGoodName(itemInfo.get("product_name").asText());// 平台商品名称
                        platOrders.setGoodPrice(new BigDecimal(itemInfo.get("origin_price").asDouble()));// 商品单价 京东价

                        platOrders.setGoodSku("");// 商品sku
                        platOrders.setOrderId(orderId);
                        platOrders.setEcOrderId(platOrder.getEcOrderId());

                        platOrders.setGoodMoney(platOrders.getGoodPrice().multiply(new BigDecimal(platOrders.getGoodNum())));// 商品金额
                        platOrders.setDeliverWhs("");// 订单明细中的发货仓id
                        platOrders.setPayMoney(new BigDecimal(itemInfo.get("real_totle_price").asDouble()));
                        platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("activity_totle_amount").asDouble()));

                        platOrdersList.add(platOrders);

                        //platOrder.setHasGift(0);// ‘是否含赠品，0不含，1含
                        goodNum += platOrders.getGoodNum();
                    }
                    StoreRecord storeRecord = storeRecordDao.select(storeId);
                    platOrder.setOrderId(orderId);// 订单编号
                    platOrder.setStoreId(storeId);// 店铺编号
                    platOrder.setStoreName(storeRecord.getStoreName());// 店铺名称
                    platOrder.setPayTime(orderInfo.get("pay_success_time").asText());// 付款时间
                    platOrder.setTradeDt(orderInfo.get("order_time").asText());// 下单时间
                    platOrder.setIsAudit(0);// ’是否审核，0未审核，1审核
                    platOrder.setGoodNum(goodNum);// 商品总数量
                    platOrder.setDeliveryType("");// 设置送货类型
                    platOrder.setVenderId("");// 设置商家ID商家编码
                    platOrder.setGoodMoney(new BigDecimal(orderInfo.get("order_origin_price").asDouble()));// 订单总金额
                    platOrder.setPayMoney(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// 买家实际付款金额
                    ////System.err.println("aaaaa:"+orderInfo.get("order_buyer_remark").asText());
                    platOrder.setBuyerNote(StringUtils.isEmpty(orderInfo.get("order_buyer_remark").asText()) || orderInfo.get("order_buyer_remark").asText().equals("null") ? "" : orderInfo.get("order_buyer_remark").asText());// 买家备注
                    platOrder.setSellerNote(StringUtils.isEmpty(orderInfo.get("order_business_remark").asText()) || orderInfo.get("order_business_remark").asText().equals("null") ? "" : orderInfo.get("order_business_remark").asText());// 商家备注
                    platOrder.setBuyerId(orderInfo.get("buyer_account").asText());// 买家会员号
                    platOrder.setOrderSellerPrice(new BigDecimal(orderInfo.get("order_real_price").asDouble()));// 结算金额
                    //JsonNode consigneeInfo = orderInfo.get("consigneeInfo");
                    platOrder.setRecAddress(orderInfo.get("receiver_address_detail").asText());// ’收货人详细地址
                    platOrder.setRecName(orderInfo.get("receiver_name").asText());// ‘收货人姓名
                    platOrder.setRecMobile(orderInfo.get("receiver_phone").asText());// ‘收货人手机号
                    platOrder.setProvince(orderInfo.get("receiver_province_name").asText());// 收货人省
                    platOrder.setProvinceId("");// 收货人省id
                    platOrder.setCity(orderInfo.get("receiver_city_name").asText());// 收货人市
                    platOrder.setCityId("");// 收货人市id
                    platOrder.setCounty(orderInfo.get("receiver_district_name").asText());// 收货人区县
                    platOrder.setCountyId("");// 收货人区县id
                    platOrder.setDeliverWhs("");//发货仓库
                    platOrder.setSellTypId("1");// 设置销售类型普通销售
                    platOrder.setBizTypId("2");// 设置业务类型2c
                    platOrder.setRecvSendCateId("6");// 设置收发类别

                    platOrder.setIsInvoice(orderInfo.get("need_invoice").asInt());// ‘是否开票
                    platOrder.setInvoiceTitle(orderInfo.get("invoice_title").asText());// 发票抬头
                    /*
                     * System.out.println(orderInfo.get("order_status").asText());
                     * System.out.println(orderInfo.get("order_expresses").get(0).get("express_no").
                     * asText());
                     */
                    if (orderInfo.get("order_status").asText().equals("1")) {
                        //发货状态为1未发货
                        platOrder.setIsShip(0);// ‘是否发货
                    } else {
                        platOrder.setIsShip(1);// ‘是否发货
                        try {
                            //已发货需要获取快递单号
                            platOrder.setExpressNo(
                                    orderInfo.get("order_expresses").get(0).get("express_no").asText());
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                    platOrder.setDeliverSelf(1);// 设置订单为自发货订单
                    platOrder.setAdjustMoney(BigDecimal.ZERO);//卖家调整金额

                    platOrder.setFreightPrice(new BigDecimal(orderInfo.get("express_fee").asDouble()));// 运费
                    platOrder.setNoteFlag(0);// ‘商家备注旗帜，默认值，表示无旗帜
                    platOrder.setIsClose(0);// ‘是否关闭
                    platOrder.setDiscountMoney(new BigDecimal(orderInfo.get("coupon_amount").asDouble()));
                    platOrder.setOrderStatus(0);// ‘订单状态
                    platOrder.setReturnStatus(0);// ‘退货状态
                    platOrder.setDownloadTime(downloadTime);// 设置下载时间
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
                // 订单插入完成后，判断是否自动免审
                for (int i = 0; i < platOrderList.size(); i++) {
                    //执行自动匹配
                    autoMatch(platOrderList.get(i).getOrderId(), userId);
                }

            }

            // 日志记录
            LogRecord logRecord = new LogRecord();
            logRecord.setOperatId(userId);
            MisUser misUser = misUserDao.select(userId);
            if (misUser != null) {
                logRecord.setOperatName(misUser.getUserName());
            }
            logRecord.setOperatContent("本次手工下载店铺：" + storeSettings.getStoreName() + "订单" + downloadCount + "条");
            logRecord.setOperatTime(downloadTime);
            logRecord.setOperatType(9);// 9手工下载 10 自动下载
            logRecord.setTypeName("手工下载");
            logRecord.setOperatOrder(ecOrderId);//操作的单号
            logRecordDao.insert(logRecord);
            resp = BaseJson.returnRespObj("ec/platOrder/download", success, "本次成功下载" + downloadCount + "条订单", null);
            return resp;

            // resp=resultJson.toString();
        } catch (Exception e) {
            logger.error("URL：ec/platOrder/download 异常说明：", e);
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
            // 设置
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
//			// 信息
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
                JSONObject orderInfo = orderInfoList.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                System.err.println(orderInfo.getString("status"));
                if (orderInfo.getString("status").equals("WAIT_SELLER_SEND_GOODS")
                        || orderInfo.getString("status").equals("WAIT_BUYER_CONFIRM_GOODS")
                        || orderInfo.getString("status").equals("TRADE_FINISHED")) {
                    // WAIT_SELLER_SEND_GOODS：等待卖家发货
                    // WAIT_BUYER_CONFIRM_GOODS：等待买家确认收货
                    // TRADE_FINISHED：交易成功
                    if (platOrderDao.checkExsits(orderInfo.getString("tid"), storeId) == 0) {
                        tmdownloadByOrderId(orderInfo.getString("tid"), userId, storeId);
                        ss++;
                    }

                }

            }

            if (resultJson.getLong("total_results") > pageNo * pageSize) {
                tmDownload(ss, userId, pageNo + 1, pageSize, startDate, endDate, storeId);
            }
            resp = BaseJson.returnRespObj("ec/platOrder/download", true, "更新成功" + ss + "条", null);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp = BaseJson.returnRespObj("ec/platOrder/download", false, "更新失败", null);
        }
        return resp;
    }

    private String tmDownloadSdk(Map<String, Integer> ss, String userId, int pageNo, int pageSize, String startDate, String endDate,
                                 String storeId) throws Exception {
        String resp = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 设置
            StoreSettings storeSettings = storeSettingsDao.select(storeId);

            TradesSoldGetRequest req = new TradesSoldGetRequest();
            req.setFields("tid,status");
            req.setPageNo(Long.valueOf(pageNo));
            req.setPageSize(Long.valueOf(pageSize));
            req.setStartCreated(dateFormat.parse(startDate));
            req.setEndCreated(dateFormat.parse(endDate));
            // 转发
            Map<String, String> map = new HashMap<String, String>();
            map.put("path", TradesSoldGetRequest.class.getName());
            map.put("taobaoObject", JSON.toJSONString(req));
            String taobao = ECHelper.getTB("", storeSettings, map);
            TradesSoldGetResponse rsp = JSONObject.parseObject(taobao, TradesSoldGetResponse.class);

            logger.info("天猫body:" + rsp.getBody());
            if (!rsp.isSuccess()) {
                logger.error("天猫err:" + rsp.getMsg());
                return BaseJson.returnRespObj("ec/platOrder/download", false, rsp.getMsg(), null);
            }

            List<CouponDetail> couponList = new ArrayList<CouponDetail>();
            List<PlatOrder> platOrderList = new ArrayList<>();

            for (Trade trade : rsp.getTrades()) {
                if (trade.getStatus().equals("WAIT_SELLER_SEND_GOODS")
                        || trade.getStatus().equals("WAIT_BUYER_CONFIRM_GOODS")
                        || trade.getStatus().equals("TRADE_FINISHED")) {
                    // WAIT_SELLER_SEND_GOODS：等待卖家发货
                    // WAIT_BUYER_CONFIRM_GOODS：等待买家确认收货
                    // TRADE_FINISHED：交易成功
                    if (platOrderDao.checkExsits(trade.getTid().toString(), storeId) == 0) {

                        tmdownloadByOrderIdSdk(trade.getTid().toString(), userId, storeId, platOrderList, couponList);

                        ss.put("keys", ss.get("keys") + 1);

                    }
                }
            }


            //分支插库分配订单号
            List<String> seqNoList = getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId);
            downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId, seqNoList);


            resp = BaseJson.returnRespObj("ec/platOrder/download", true, "更新成功" + ss.get("keys") + "条", null);
            if (rsp.getTotalResults() > pageNo * pageSize) {
                resp = tmDownloadSdk(ss, userId, pageNo + 1, pageSize, startDate, endDate, storeId);
            } else {
                // 日志记录
                LogRecord logRecord = new LogRecord();
                logRecord.setOperatId(userId);
                MisUser misUser = misUserDao.select(userId);
                if (misUser != null) {
                    logRecord.setOperatName(misUser.getUserName());
                }
                logRecord.setOperatContent("本次自动下载店铺：" + storeSettings.getStoreName() + "订单" + ss.get("keys") + "条");
                logRecord.setOperatTime(sdf.format(new Date()));
                logRecord.setOperatType(10);// 9手工下载 10 自动下载
                logRecord.setTypeName("自动下载");
                logRecordDao.insert(logRecord);
                return resp;
            }

            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            resp = BaseJson.returnRespObj("ec/platOrder/download", false, "更新失败", null);
        }
        return resp;
    }

    private String tmdownloadByOrderIdSdk(String tid, String userId, String storeId, List<PlatOrder> platOrderList, List<CouponDetail> couponList) throws IOException {
        String resp = "";
        String message = "";
        if (platOrderDao.checkExsits(tid, storeId) != 0) {
            return resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, tid + "订单已存在", null);
        }
        try {
            // 设置
            StoreSettings storeSettings = storeSettingsDao.select(storeId);
            // 信息
            StoreRecord storeRecord = storeRecordDao.select(storeId);

            TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
            req.setTid(Long.valueOf(tid));
            req.setFields(
                    "logistics_company,invoice_name,tid,type,status,payment,est_con_time,orders,promotion_details,pay_time,total_fee,buyer_message,seller_memo,receiver_address,receiver_name,receiver_mobile,seller_flag,adjust_fee,discount_fee,created,received_payment,receiver_state,post_fee,receiver_city,receiver_district,num");

            // 转发
            Map<String, String> map = new HashMap<String, String>();
            map.put("path", TradeFullinfoGetRequest.class.getName());
            map.put("taobaoObject", JSON.toJSONString(req));
            String taobao = ECHelper.getTB("", storeSettings, map);
            TradeFullinfoGetResponse rsp = JSONObject.parseObject(taobao, TradeFullinfoGetResponse.class);
            logger.info("天猫body:" + rsp.getBody());
            if (!rsp.isSuccess()) {
                logger.error("天猫err:" + rsp.getMsg());
                return BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", false, rsp.getMsg(), null);
            }
            Trade trade = rsp.getTrade();
//			System.out.println(trade.getStatus());
            if (!trade.getStatus().equals("WAIT_SELLER_SEND_GOODS")
                    && !trade.getStatus().equals("WAIT_BUYER_CONFIRM_GOODS")
                    && !trade.getStatus().equals("TRADE_FINISHED")) {
                return BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", false, "所下载订单状态不符合要求", null);
            }

            //获取订单号
//			String orderId = getOrderNo.getSeqNo("ec", userId);

            PlatOrder platOrder = new PlatOrder();

            platOrder.setEcOrderId(trade.getTid().toString());// 电商订单号
//			platOrder.setOrderId(orderId); // 订单编号
            platOrder.setStoreId(storeId); // 店铺编号
            platOrder.setStoreName(storeRecord.getStoreName()); // '店铺名称' 1
            if (trade.getPayTime() == null) {

            } else {
                platOrder.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(trade.getPayTime())); // 付款时间' t
            }
            // paytime
//					platOrder.setWaif(waif);		//	'旗标'
            platOrder.setIsAudit(0); // '是否客审'
//					platOrder.setAuditHint(auditHint); // '审核提示'
            platOrder.setGoodMoney(new BigDecimal(trade.getTotalFee() == null ? "0" : trade.getTotalFee())); // 商品金额' t
            // totalfee
            platOrder.setPayMoney(new BigDecimal(trade.getPayment() == null ? "0" : trade.getPayment())); // '实付金额' t
            // payment
            platOrder.setBuyerNote(trade.getBuyerMessage()); // 买家留言' t buyermessage
            platOrder.setSellerNote(trade.getSellerMemo()); // 卖家备注'
            platOrder.setRecAddress(trade.getReceiverAddress()); // 收货人详细地址' t receiveraddress
//			platOrder.setBuyerId(buyerId); // '买家会员号'
            platOrder.setRecName(trade.getReceiverName()); // 收货人姓名' t receivername
            platOrder.setRecMobile(trade.getReceiverMobile()); // 收货人手机号' t receivermobile
            platOrder.setIsInvoice(0); // '是否开票'
//			platOrder.setInvoiceTitle(resultJsons.getString("invoice_name")); // '发票抬头'
            platOrder.setNoteFlag(trade.getSellerFlag().intValue()); // 卖家备注旗帜' t sellerflag
            platOrder.setIsClose(0); // '是否关闭'
            platOrder.setAdjustMoney(new BigDecimal(trade.getAdjustFee() == null ? "0" : trade.getAdjustFee())); // 卖家调整金额'
            // t
            // adjustfee
            platOrder.setDiscountMoney(new BigDecimal(trade.getDiscountFee() == null ? "0" : trade.getDiscountFee())); // 系统优惠金额'
            // t
            // discountfee
            platOrder.setOrderStatus(0); // '订单状态'
            platOrder.setReturnStatus(0); // '退货状态'
            // platOrder.setHasGift(0); // '是否含赠品'
            // platOrder.setMemo(memo); // '备注'
            // platOrder.setAdjustStatus(adjustStatus); // '调整标识'
            platOrder.setTradeDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(trade.getCreated())); // '交易时间' t
            // created
            platOrder.setSellTypId("1");// 设置销售类型普通销售
            platOrder.setBizTypId("2");// 设置业务类型2c
            platOrder.setRecvSendCateId("6");// 设置收发类别
            if (trade.getStatus().equals("WAIT_SELLER_SEND_GOODS")) {
                platOrder.setIsShip(0); // '是否发货'
            } else {
                platOrder.setIsShip(1); // '是否发货'
                platOrder.setExpressNo(trade.getOrders().get(0).getInvoiceNo());
            }
//			platOrder.setRecvSendCateId(recvSendCateId); // '收发类别编号'
            platOrder.setOrderSellerPrice(
                    new BigDecimal(trade.getReceivedPayment() == null ? "0" : trade.getReceivedPayment())); // 结算金额（订单实际收入金额）'
            // t
            // receivedpayment
            platOrder.setProvince(trade.getReceiverState()); // 省' t receiverstate
            // platOrder.setProvinceId(provinceId); // '省id'
            platOrder.setCity(trade.getReceiverCity()); // 市' t receivercity
            // platOrder.setCityId(cityId); //
            platOrder.setCounty(trade.getReceiverDistrict()); // 区' t receiverdistrict
            // platOrder.setCountyId(countyId); //
            // platOrder.setTown(town); // '镇'
            // platOrder.setTownId(townId); // '镇id'
            platOrder.setFreightPrice(new BigDecimal(trade.getPostFee() == null ? "0" : trade.getPostFee())); // 运费' t
            // postfee
            platOrder.setVenderId("TM"); // '商家id'

            // orders Order[]
            platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // platOrder.setDeliveryType("任意时间");
            // //'送货（日期）类型（1-只工作日送货(双休日、假日不用送;2-只双休日、假日送货(工作日不用送;3-工作日、双休日与假日均可送货;其他值-返回“任意时间”
            // ）'

			List<PlatOrders> list = new ArrayList<PlatOrders>();

			for (com.taobao.api.domain.Order order : trade.getOrders()) {
				PlatOrders platOrders = new PlatOrders();
				platOrders.setGoodId(order.getNumIid().toString()); // 平台商品编号 商品数字ID
				platOrders.setGoodName(order.getTitle()); // '平台商品名称' t title String 山寨版测试机器 商品标题
				platOrders.setGoodNum(order.getNum().intValue()); // '商品数量' t num Number 1 购买数量。取值范围:大于零的整数

				platOrders.setGoodMoney(new BigDecimal(order.getNum()).multiply(new BigDecimal(order.getPrice()))); // 商品金额'
				if (trade.getOrders().size() == 1) {
					platOrders.setPayMoney(
							new BigDecimal(order.getDivideOrderFee()== null ? "0" : order.getDivideOrderFee()).subtract(new BigDecimal(trade.getPostFee()))); // '实付金额'分摊之后的实付金额
					// 获取到的Order中的payment字段在单笔子订单时包含物流费用，多笔子订单时不包含物流费用
				} else {
					platOrders.setPayMoney(new BigDecimal(order.getDivideOrderFee())); // '实付金额'分摊之后的实付金额
				}
				platOrders.setGoodSku(order.getSkuId()); // '商品sku'
//				platOrders.setOrderId(orderId); //  x'd'订单编号' 1
//						platOrders.setBatchNo(batchNo);	//	'批号' 	
				platOrders.setExpressCom(order.getLogisticsCompany()); // 快递公司' t子订单发货的快递公司名称
//						promotion_details		platOrders.setProActId(proActId);	//	促销活动编号' t promotionid	String	mjs	优惠id
//						platOrders.setDiscountMoney(new BigDecimal(itemInfo.get("discount_fee").asDouble())); // 系统优惠金额'
				platOrders.setAdjustMoney(new BigDecimal(order.getAdjustFee() == null ? "0" : order.getAdjustFee())); // 买家调整金额'
																														// t
				platOrders.setMemo(order.getOid().toString()); // '备注' 该 子单id oid
				platOrders.setGoodPrice(new BigDecimal(order.getPrice() == null ? "0" : order.getPrice())); // '商品单价' t
																											// price
				platOrders.setDeliverWhs(order.getStoreCode()); // 发货仓库编码'
				platOrders.setEcOrderId(trade.getTid().toString()); // '平台订单号' t oid

				if (order.getShipper() == null) {
					platOrder.setDeliverSelf(1); // '订单是否自发货，0平台仓发货，1自发货' t
				} else if (order.getShipper().equals("cn") || order.getShipper().equals("hold")) {
					platOrder.setDeliverSelf(0); // '订单是否自发货，0平台仓发货，1自发货' t
					platOrder.setDeliverWhs(order.getStoreCode());//发货的仓库编码
					Map map11 = new HashMap();
					map11.put("type", "TM");
					map11.put("online", platOrder.getDeliverWhs());
					String newWhs1 = PlatWhsMappDao.select(map11);
					if (newWhs1 != null) {
						platOrder.setDeliverWhs(newWhs1);
					} else {
						platOrder.setDeliverWhs("");
						platOrder.setAuditHint("平台仓发货订单缺少平台仓映射,所需平仓编码："+order.getStoreCode());
					}
					

				} else {
					platOrder.setDeliverSelf(1); // '订单是否自发货，0平台仓发货，1自发货' t
				}
				// 字段 shipper （cn:菜鸟仓发货，seller:商家仓发货，hold:待确认发货仓库，null(空值）：商家仓发货）
				////System.err.println("itemInfo.get(\"shipper\")" + order.getShipper());
//						platOrders.setInvId(itemInfo.get("outer_iid") == null ? null : itemInfo.get("outer_iid").asText()); // 存货编码'// 商家外部编码

				list.add(platOrders);
			}


            if (trade.getPromotionDetails() != null) {

//				JSONArray promotionDetails = resultJsons.getJSONObject("promotion_details")
//						.getJSONArray("promotion_detail");
				for (PromotionDetail promotionDetail : trade.getPromotionDetails()) {
					CouponDetail coupon1 = new CouponDetail();
					coupon1.setPlatId("TM");// 平台id，京东JD，天猫JD
					coupon1.setStoreId(storeId);// 店铺id
					coupon1.setOrderId(trade.getTid().toString());// 订单号
					coupon1.setSkuId(null);
					coupon1.setCouponCode(null);// 优惠类型编码
					coupon1.setCouponType(promotionDetail.getPromotionName());// 优惠名称
					coupon1.setCouponPrice(new BigDecimal(
							promotionDetail.getDiscountFee() == null ? "0" : promotionDetail.getDiscountFee()));// 优惠金额
					couponList.add(coupon1);
				}
			}
            platOrder.setGoodNum(list.size()); // 商品品类数
            logger.info("list" + list.size());
			//封装子表
			platOrder.setPlatOrdersList(list);
			//封装主表list集合
            platOrderList.add(platOrder);

			//分支插库分配订单号
			//downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId);

			/*
			 * //生成订单号 + 插入数据库 List<String> seqNoList =
			 * getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId); //插入订单 +
			 * 自动匹配 downloadCount =
			 * platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList,
			 * null, downloadCount, userId, seqNoList);
			 */

/*            couponDetailDao.insert(couponList);
			platOrderDao.insert(platOrder);
			platOrdersDao.insert(list);
			//执行自动匹配
			autoMatch(platOrder.getOrderId(), userId);*/
			
			
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载成功" + tid, null);

		} catch (Exception e) {
			e.printStackTrace();
			resp = BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载失败" + tid, null);
		}
		return resp;

	}

	@Override
	public List<PlatOrders> jisuanPayPrice(String orderid) {
		// TODO Auto-generated method stub
		PlatOrder platOrder = new PlatOrder();
		List<PlatOrders> orderslist1 = new ArrayList<>();
		// 先根据orderid获取订单主子表信息
		platOrder = platOrderDao.select(orderid);
		List<PlatOrders> orderslist = platOrdersDao.select(orderid);
		BigDecimal sellerprice = platOrder.getOrderSellerPrice();// 结算金额
		StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
		BigDecimal money1 = platOrder.getOrderSellerPrice().subtract(platOrder.getFreightPrice());// 结算金额-运费，可视为明细总实付金额的和
		if (orderslist.size() == 1) {
			// 当明细表里面只有一条记录时，订单的结算金额-运费就是该条记录的实付金额
			PlatOrders orders = orderslist.get(0);
			orders.setPayMoney(money1);
			orderslist1.add(orders);
		} else {
			DecimalFormat df1 = new DecimalFormat("0.00");
			// 查询整单优惠
			BigDecimal couponprice100 = new BigDecimal(
					couponDetailDao.selectByOrderId(storeRecord.getEcId(), orderid, "", 2));
			BigDecimal money2 = new BigDecimal("0");// 已分配出去的金额，最后一条时减法用到
			for (int i = 0; i < orderslist.size(); i++) {
				// 根据子表的sku去优惠表里面查询是否有单品优惠
				// 有优惠信息时
				PlatOrders orders = orderslist.get(i);
				// 查询单品促销
				BigDecimal couponprice1 = new BigDecimal(
						couponDetailDao.selectByOrderId(storeRecord.getEcId(), orderid, orders.getGoodSku(), 1));

				if (couponprice100.compareTo(BigDecimal.ZERO) == 0) {
					// 如果整单优惠为0时，
					// 明细的实付金额就等于商品金额-单品促销的金额
					orders.setPayMoney(orders.getGoodMoney().subtract(couponprice1));
					orderslist1.add(orders);
				} else {
					// 当整单优惠金额不为0时
					// 需要按商品金额比例分配每条明细的金额，最后一条明细的实付金额用减法
					if (i + 1 == orderslist.size()) {
						// 当最后一条明细时
						orders.setPayMoney(money1.subtract(money2));
					} else {
						// 计算当前条明细的商品金额-单品优惠后的金额
						BigDecimal currentMoney = orders.getGoodMoney().subtract(couponprice1);
						// 计算比例分摊结算金额
						BigDecimal current = new BigDecimal(df1.format(money1.multiply(currentMoney.divide(money1))));// 当前明细分配到的实付金额
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
		// 将订单明细中的sku和spu兑换成系统的存货编码，如果是PTO类型的需要拆解成列表保存 并计算数量以及每条明细的实付金额,单价
		List<PlatOrders> platOrders123 = platOrdersList;// 声明list装传入的原始列表，后续若发生异常时，直接返回此list。

		List<PlatOrders> orderslist = new ArrayList<PlatOrders>();
		// 拆解pto前应先计算每笔明细的实付金额
		// 第一次循环扣除单品促销优惠金额
		List<CouponDetail> coupons = new ArrayList<CouponDetail>();//用来临时存放单品sku优惠金额匹配上后就从优惠列表中移除，避免订单中含多个相同sku明细
		for (int i = 0; i < platOrdersList.size(); i++) {
			// BigDecimal sellerprice = platOrder.getOrderSellerPrice();// 结算金额
			// BigDecimal money1 =
			// platOrder.getOrderSellerPrice().subtract(platOrder.getFreightPrice());//
			// 结算金额可视为明细总实付金额的和
			// 不止一条明细时，需要计算每条明细的实付金额
			// 如果优惠明细没有内容，直接单价乘数量
			if (couponDetails.size() == 0) {
				platOrdersList.get(i).setPayMoney(platOrdersList.get(i).getGoodMoney());// 没有优惠时，商品金额就等于实付金额

			} else {
				// 先针对单品优惠扣减金额再在扣减后的基础上按比例分摊整单优惠金额
				for (int j = 0; j < couponDetails.size(); j++) {
					// 判断优惠明细的sku是否与订单明细当前条的一致，若一致就修改
					if (couponDetails.get(j).getSkuId().equals(platOrdersList.get(i).getGoodSku())) {
						if (platOrdersList.get(i).getGoodMoney().compareTo(BigDecimal.ZERO) > 0) {
							
							// 单品实付金额等于货品金额减去单品优惠金额
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
		// 计算整单优惠总金额
		BigDecimal discountall = new BigDecimal("0");
		for (int i = 0; i < couponDetails.size(); i++) {

			// 20-套装优惠 1
			// 28-闪团优惠 1
			// 29-团购优惠 1
			// 30-单品促销优惠 1
			// 34-手机红包
			// 35-满返满送(返现) 1
			// 39-京豆优惠
			// 41-京东券优惠
			// 52-礼品卡优惠
			// 100-店铺优惠 1
			// 当优惠类型不等于以下类型时，计算累加整单优惠金额
			if (!couponDetails.get(i).getCouponCode().equals("20") &&!couponDetails.get(i).getCouponCode().equals("30") && !couponDetails.get(i).getCouponCode().equals("34")
					&& !couponDetails.get(i).getCouponCode().equals("39")
					&& !couponDetails.get(i).getCouponCode().equals("41")
					&& !couponDetails.get(i).getCouponCode().equals("52")) {
				discountall = discountall.add(couponDetails.get(i).getCouponPrice());
			}
		}
		// 扣除每条明细占整单优惠的比例
		// 按比列分摊时需要先过滤掉其中实付金额为0的明细记录。防止金额分配不准确的情况

		List<PlatOrders> platOrders1 = new ArrayList<PlatOrders>();
		for (int i = 0; i < platOrdersList.size(); i++) {
			// System.out.println(platOrdersList.get(i).getPayMoney());
			if (platOrdersList.get(i).getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
				// 实付金额为0时排除
				platOrdersList.get(i).setDiscountMoney(platOrdersList.get(i).getGoodMoney());// 实付金额为0时，设置优惠金额为商品金额
				platOrders1.add(platOrdersList.get(i));
				platOrdersList.remove(i);// 临时移除实付金额为0的
				i--;
			}
		}
		if (platOrder.getOrderSellerPrice().compareTo(BigDecimal.ZERO) > 0) {
			int ii = platOrdersList.size();
			BigDecimal hasdiscount = new BigDecimal("0");
			for (int i = 0; i < platOrdersList.size(); i++) {
				if (i + 1 < ii) {
					// 计算当前明细需要扣减掉的整单优惠金额
					BigDecimal discountmoney = platOrdersList.get(i).getPayMoney()
							.divide(platOrder.getOrderSellerPrice(), 2, BigDecimal.ROUND_HALF_UP).multiply(discountall);
					// 扣减优惠金额
					platOrdersList.get(i).setPayMoney(platOrdersList.get(i).getPayMoney().subtract(discountmoney));
					// 累加优惠金额
					platOrdersList.get(i)
							.setDiscountMoney((platOrdersList.get(i).getDiscountMoney() == null ? BigDecimal.ZERO
									: platOrdersList.get(i).getDiscountMoney()).add(discountmoney));
					hasdiscount = hasdiscount.add(discountmoney);
				} else {
					// 最后一条明细时
					// 扣减优惠金额
					// 当前条明细的实付金额-（整单优惠总金额-其余明细已优惠金额）
					platOrdersList.get(i).setPayMoney(
							platOrdersList.get(i).getPayMoney().subtract(discountall.subtract(hasdiscount)));
					// 累加优惠金额
					platOrdersList.get(i)
							.setDiscountMoney((platOrdersList.get(i).getDiscountMoney() == null ? BigDecimal.ZERO
									: platOrdersList.get(i).getDiscountMoney()).add(discountall.subtract(hasdiscount)));
				}
			}
		}else {
			//订单结算金额为0时，明细中所有的商品
			//为了避免赠品的订单优惠明细不是单品优惠是店铺整单优惠
			//当订单结算金额为0时,将所有明细的实付金额设置成0
			//
			for (int i = 0; i < platOrdersList.size(); i++) {
				platOrdersList.get(i).setPayMoney(BigDecimal.ZERO);//实付金额为0；
				platOrdersList.get(i).setPayPrice(BigDecimal.ZERO);//实付单价为0
				platOrdersList.get(i).setSellerPrice(BigDecimal.ZERO);//结算单价为0
				platOrdersList.get(i).setDiscountMoney(platOrdersList.get(i).getGoodMoney());//优惠金额等于商品金额
				platOrdersList.get(i).setAdjustMoney(BigDecimal.ZERO);//调整金额为0
			}

		}
		BigDecimal payMoney = BigDecimal.ZERO;
		for (int i = 0; i < platOrdersList.size(); i++) {
			payMoney = payMoney.add(platOrdersList.get(i).getPayMoney());
		}
		platOrder.setPayMoney(payMoney);
		// 将刚刚临时移除的添加进去
		platOrdersList.addAll(platOrders1);
		// 循环匹配商品档案
		/*for (int i = 0; i < platOrdersList.size(); i++) {
			DecimalFormat dFormat = new DecimalFormat("0.00");
			boolean flag = true;
			PlatOrders platOrders = platOrdersList.get(i);
			// 存货编码 平台商品表good_record中的平台商品编码；
			String ecgoodid = platOrders.getGoodId();// 平台商品编号
			String goodsku = platOrders.getGoodSku();
			GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
			if (goodRecord == null) {
				platOrder.setAuditHint("在店铺商品档案中未匹配到对应商品档案");
				// platOrderDao.update(platOrder);//更新订单的审核提示；
				flag = false;
				orderslist.add(platOrders);
				continue;
			} else if (goodRecord.getGoodId() == null) {
				platOrder.setAuditHint("请完善店铺商品档案中存货编码的对应关系");
				// platOrderDao.update(platOrder);//更新订单的审核提示；
				flag = false;
				orderslist.add(platOrders);
				continue;
			} else {
				String invId = goodRecord.getGoodId();
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// 去存货档案查询存货基本信息
				if (invtyDoc == null) {
					platOrder.setAuditHint("在存货档案中未匹配到对应存货档案,需匹配的存货编码：" + invId);
					// platOrderDao.update(platOrder);//更新订单的审核提示；
					flag = false;
					orderslist.add(platOrders);
					continue;
				} else {
					if (invtyDoc.getPto() != null) {
						if (invtyDoc.getPto() == 1) {
							// 属于PTO类型存货
							ProdStru prodtru = prodStruDao.selectMomEncd(invId);
							if (prodtru == null) {
								// 未在产品结构中查询到对应存货的产品结构信息
								platOrder.setAuditHint("在产品结构：" + invId + "中未匹配到对应产品结构信息");
								// platOrderDao.update(platOrder);//更新订单的审核提示；
								flag = false;
								orderslist.add(platOrders);
								continue;
							} else {
								if (prodtru.getIsNtChk() == 0) {
									// 对应产品结构尚未审核
									platOrder.setAuditHint("对应产品结构：" + prodtru.getMomEncd() + "尚未审核");
									// platOrderDao.update(platOrder);//更新订单的审核提示；
									flag = false;
									orderslist.add(platOrders);
									continue;
								} else {
									List<ProdStruSubTab> strucksublist = prodtru.getStruSubList();
									// 循环产品结构子表信息
									if (strucksublist.size() == 0) {
										platOrder.setAuditHint("对应产品结构：" + prodtru.getMomEncd() + "没有设置子件明细，请先设置。");
										// platOrderDao.update(platOrder);//更新订单的审核提示；
										flag = false;
										orderslist.add(platOrders);
										continue;
									} else if (strucksublist.size() == 1) {
										// 当产品结构明细里面只有一个子件时，直接替换存货编码
										// 存货编码
										platOrders.setInvId(strucksublist.get(0).getSubEncd());
										// 设置存货数量
										platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
												.multiply(strucksublist.get(0).getSubQty()).intValue());
										// 设置可退数量
										platOrders.setCanRefNum(platOrders.getInvNum());
										platOrders.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
										platOrders.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
										// 设置实付单价
										platOrders.setPayPrice(platOrders.getPayMoney().divide(
												(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
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
										BigDecimal money123 = new BigDecimal("0");// 设置已分实付金额 最后一条用减法时用到
										BigDecimal money456 = new BigDecimal("0");// 设置拆分后的商品金额，最后一条减法
										for (int j = 0; j < strucksublist.size(); j++) {
											PlatOrders order = new PlatOrders();
											try {
												order = (PlatOrders) platOrders.clone();
											} catch (CloneNotSupportedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											if (j + 1 < strucksublist.size()) {
												// 计算每条子件占总成本的比例 保留8位小数
												InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
														strucksublist.get(j).getSubEncd());
												BigDecimal rate = invtyDoc2.getRefCost()
														.multiply(strucksublist.get(j).getSubQty())
														.divide(total, 8, BigDecimal.ROUND_HALF_UP);
												order.setInvNum((new BigDecimal(order.getGoodNum()))
														.multiply(strucksublist.get(j).getSubQty()).intValue());
												order.setPayMoney(new BigDecimal(
														dFormat.format(order.getPayMoney().multiply(rate))));// 设置实付金额
																												// 保留两位小数
												// 计算实付单价 保留8位小数
												order.setPayPrice(
														order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																8, BigDecimal.ROUND_HALF_UP));
												order.setSellerPrice(order.getPayPrice());
												// 存货编码
												order.setInvId(strucksublist.get(j).getSubEncd());
												// 设置可退数量
												order.setCanRefNum(order.getInvNum());
												// 设置可退金额
												order.setCanRefMoney(order.getPayMoney());
												// 设置商品金额
												order.setGoodMoney(new BigDecimal(
														dFormat.format(platOrders.getGoodMoney().multiply(rate))));
												money456 = money456.add(order.getGoodMoney());
												// 设置优惠金额
												order.setDiscountMoney(
														order.getGoodMoney().subtract(order.getPayMoney()));
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
												order.setInvNum((new BigDecimal(order.getGoodNum()))
														.multiply(strucksublist.get(j).getSubQty()).intValue());
												order.setPayMoney(order.getPayMoney().subtract(money123));// 设置实付金额
																											// 保留两位小数
												// 计算实付单价 保留8位小数
												order.setPayPrice(
														order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																8, BigDecimal.ROUND_HALF_UP));
												order.setSellerPrice(order.getPayPrice());
												// 存货编码
												order.setInvId(strucksublist.get(j).getSubEncd());
												// 设置可退数量
												order.setCanRefNum(order.getInvNum());
												// 设置可退金额
												order.setCanRefMoney(order.getPayMoney());
												// 设置商品金额
												order.setGoodMoney(platOrders.getGoodMoney().subtract(money456));
												// 设置优惠金额
												order.setDiscountMoney(
														order.getGoodMoney().subtract(order.getPayMoney()));
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
							platOrders.setPayPrice(platOrders.getPayMoney()
									.divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
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
						platOrder.setAuditHint("存货档案中PTO属性为空");
						// platOrderDao.update(platOrder);//更新订单的审核提示；
						flag = false;
						orderslist.add(platOrders);
						continue;
					}
				}
			}
		}*/
		return platOrdersList;
	}
	// 订单联查

	@Override
	public String unionQuery(String orderId) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "查询成功";
		boolean success = true;
		PlatOrder platOrder = platOrderDao.select(orderId);
		List<UnionQuery> unionQueries = new ArrayList<>();
		if (platOrder == null) {
			message = "所查询订单不存在";
		} else if (platOrder.getIsAudit() == 0) {
			// 所查询订单尚未审核，没有关联单据
			message = "当前订单尚未审核，没有关联单据";
		} else {
			// 查询订单对应销售单号
			// 查询订单对应退款单
			// 查询对应销售出库单
			SellSngl sellSngl = platOrderDao.selectSellSnglByOrderId(orderId);

			if (sellSngl!=null) {
				UnionQuery unionQuery = new UnionQuery();
				unionQuery.setAudit("" + sellSngl.getIsNtChk());
				unionQuery.setOrderId(sellSngl.getSellSnglId());
				unionQuery.setType(1);// 设置单据类型为销售单
				unionQuery.setOrderName("销售单");
				unionQueries.add(unionQuery);
				if (sellSngl.getIsNtChk() == 1) {
					// 销售单已审核，才有销售出库单
					List<SellOutWhs> sellOutWhs = platOrderDao.selectSellOutWhsByOrderId(sellSngl.getSellSnglId());// 传sellsnglid
					for (int i = 0; i < sellOutWhs.size(); i++) {
						UnionQuery unionQuery1 = new UnionQuery();
						unionQuery1.setAudit("" + sellOutWhs.get(i).getIsNtChk());
						unionQuery1.setOrderId(sellOutWhs.get(i).getOutWhsId());
						unionQuery1.setType(2);// 设置单据类型为销售出库单
						unionQuery1.setOrderName("销售出库单");
						unionQueries.add(unionQuery1);
					}
					List<LogisticsTab> logisticsTabs = platOrderDao.selectLogisticsTabByOrderId(orderId);
					for (int i = 0; i < logisticsTabs.size(); i++) {
						UnionQuery unionQuery1 = new UnionQuery();
						unionQuery1.setAudit("1");//物流单无审核状态，全部默认已审核
						unionQuery1.setOrderId(logisticsTabs.get(i).getOrdrNum().toString());
						unionQuery1.setType(4);// 设置单据类型为物流单
						unionQuery1.setOrderName("物流单");
						unionQueries.add(unionQuery1);
					}
				}
			}

			List<RefundOrder> refundOrders = platOrderDao.selectRefundOrderByEcOrderId(platOrder.getEcOrderId());// 传ecorderid
			for (int i = 0; i < refundOrders.size(); i++) {
				UnionQuery unionQuery2 = new UnionQuery();
				unionQuery2.setAudit("" + refundOrders.get(i).getIsAudit());
				unionQuery2.setOrderId(refundOrders.get(i).getRefId());
				unionQuery2.setType(3);// 设置单据类型为退款单
				unionQuery2.setOrderName("电商退款单");
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
			if (platOrder.getIsAudit()==0) {//订单未审核才可以执行自动匹配
				int jishu =0;//声明一个计数项，当循环订单明细失败时，用于记录当前循环到第几个明细了
				// 循环匹配商品档案
				for (int j = 0; j < platOrdersList.size(); j++) {
					jishu=j;//给计数项赋值当前循环数
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
					if (goodRecord == null) {
						platOrder.setAuditHint("在店铺商品档案中未匹配到对应商品档案");
						platOrderDao.update(platOrder);// 更新订单的审核提示；
						orderslist.add(platOrders);
						flag = false;
						break;
					} else if (StringUtils.isEmpty(goodRecord.getGoodId())) {
						platOrder.setAuditHint("请完善店铺商品档案中存货编码的对应关系");
						platOrderDao.update(platOrder);// 更新订单的审核提示；
						orderslist.add(platOrders);
						flag = false;
						break;
					} else {
						String invId = goodRecord.getGoodId();
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// 去存货档案查询存货基本信息
						if (invtyDoc == null) {
							platOrder.setAuditHint("在存货档案中未匹配到对应存货档案,需匹配的存货编码：" + invId);
							platOrderDao.update(platOrder);// 更新订单的审核提示；
							orderslist.add(platOrders);
							flag = false;
							break;
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
										break;
									} else {
										if (prodtru.getIsNtChk() == 0) {
											// 对应产品结构尚未审核
											platOrder.setAuditHint("对应产品结构：" + prodtru.getMomEncd() + "尚未审核");
											platOrderDao.update(platOrder);// 更新订单的审核提示；
											orderslist.add(platOrders);
											flag = false;
											break;
										} else {
											List<ProdStruSubTab> strucksublist = prodtru.getStruSubList();
											// 循环产品结构子表信息
											if (strucksublist.size() == 0) {
												platOrder.setAuditHint(
														"对应产品结构：" + prodtru.getMomEncd() + "没有设置子件明细，请先设置。");
												platOrderDao.update(platOrder);// 更新订单的审核提示；
												orderslist.add(platOrders);
												flag = false;
												break;
											} else if (strucksublist.size() == 1) {
												// 当产品结构明细里面只有一个子件时，直接替换存货编码
												// 存货编码
												platOrders.setInvId(strucksublist.get(0).getSubEncd());
												// 设置存货数量
												platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
														.multiply(strucksublist.get(0).getSubQty()).intValue());
												// 设置可退数量
												platOrders.setCanRefNum(platOrders.getInvNum());
												platOrders.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
												platOrders.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
												// 设置实付单价
												platOrders.setPayPrice(platOrders.getPayMoney().divide(
														(new BigDecimal(platOrders.getInvNum())), 8,
														BigDecimal.ROUND_HALF_UP));
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
												BigDecimal money123 = new BigDecimal("0");// 设置已分实付金额 最后一条用减法时用到
												BigDecimal money456 = new BigDecimal("0");// 设置拆分后的商品金额，最后一条减法
												for (int x = 0; x < strucksublist.size(); x++) {
													PlatOrders order = new PlatOrders();
													try {
														order = (PlatOrders) platOrders.clone();
													} catch (CloneNotSupportedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}

													if (x + 1 < strucksublist.size()) {
														// 计算每条子件占总成本的比例 保留8位小数
														InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
																strucksublist.get(x).getSubEncd());
														BigDecimal rate = invtyDoc2.getRefCost()
																.multiply(strucksublist.get(x).getSubQty())
																.divide(total, 8, BigDecimal.ROUND_HALF_UP);
														order.setInvNum((new BigDecimal(order.getGoodNum()))
																.multiply(strucksublist.get(x).getSubQty()).intValue());
														order.setPayMoney(new BigDecimal(
																dFormat.format(order.getPayMoney().multiply(rate))));// 设置实付金额
																																						// 保留两位小数
																																						// 计算实付单价 保留8位小数
														order.setPayPrice(order.getPayMoney().divide(
																(new BigDecimal(order.getInvNum())), 8,
																BigDecimal.ROUND_HALF_UP));
														order.setSellerPrice(order.getPayPrice());
														// 存货编码
														order.setInvId(strucksublist.get(x).getSubEncd());
														// 设置可退数量
														order.setCanRefNum(order.getInvNum());
														// 设置可退金额
														order.setCanRefMoney(order.getPayMoney());
														// 设置商品金额
														order.setGoodMoney(new BigDecimal(dFormat
																.format(platOrders.getGoodMoney().multiply(rate))));
														money456 = money456.add(order.getGoodMoney());
														// 设置优惠金额
														order.setDiscountMoney(
																order.getGoodMoney().subtract(order.getPayMoney()));
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
														order.setInvNum((new BigDecimal(order.getGoodNum()))
																.multiply(strucksublist.get(x).getSubQty()).intValue());
														order.setPayMoney(order.getPayMoney().subtract(money123));// 设置实付金额
																													// 保留两位小数
																													// 计算实付单价 保留8位小数
														order.setPayPrice(order.getPayMoney().divide(
																(new BigDecimal(order.getInvNum())), 8,
																BigDecimal.ROUND_HALF_UP));
														order.setSellerPrice(order.getPayPrice());
														// 存货编码
														order.setInvId(strucksublist.get(x).getSubEncd());
														// 设置可退数量
														order.setCanRefNum(order.getInvNum());
														// 设置可退金额
														order.setCanRefMoney(order.getPayMoney());
														// 设置商品金额
														order.setGoodMoney(
																platOrders.getGoodMoney().subtract(money456));
														// 设置优惠金额
														order.setDiscountMoney(
																order.getGoodMoney().subtract(order.getPayMoney()));
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
									platOrders.setPayPrice(platOrders.getPayMoney().divide(
											(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
									platOrders.setSellerPrice(platOrders.getPayPrice());// 结算单价与实付单价一样
									// 设置优惠金额
									platOrders.setDiscountMoney(
											platOrders.getGoodMoney().subtract(platOrders.getPayMoney()));
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
								platOrder.setAuditHint("存货编码："+invtyDoc.getInvtyEncd()+"在存货档案中PTO属性为空");
								platOrderDao.update(platOrder);// 更新订单的审核提示；
								flag = false;
								break;
							}
						}
					}
				}
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
									if (storeRecord.getEcId().equals("PDD")||storeRecord.getEcId().equals("TM")) {
										// TODO Auto-generated catch block
										//e.printStackTrace();
										
									}else {
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
					//存储虚拟商品被移除的list
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
								platOrder.setAuditHint("请检查存货：" + invtyDoc.getInvtyEncd() + "是否应税劳务属性");
								flag = false;
								break;
							}
						} 
					}
					if (flag) {
						//存储分配批次成功后返回来的list以及可用量不足的list
						List<PlatOrders> platOrders123 = new ArrayList<PlatOrders>();
						for (int j = 0; j < orderslist.size(); j++) {
							if (StringUtils.isEmpty(orderslist.get(j).getBatchNo())) {//如果当前条明细批次字段为空
								// 验证可用量及分配批次
								Map map4 = checkCanUseNumAndBatch(orderslist.get(j));
								if (map4.get("flag").toString().equals("true")) {
									// 分配批次成功
									List<PlatOrders> platOrders456 = (List<PlatOrders>) map4.get("platOrders");
									for (int k = 0; k < platOrders456.size(); k++) {
										platOrders123.add(platOrders456.get(k));
									}

								} else {
									flag = false;//没分配成功时，将当前条明细复制到新list
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
					//将刚刚移除的虚拟商品的list增加进去
					for (int j = 0; j < platOrders111.size(); j++) {
						orderslist.add(platOrders111.get(j));
					}
					//System.out.println("可匹配促销:"+platOrder.getCanMatchActive());
					if(flag&&orderslist.size()>0&&platOrder.getCanMatchActive()==null) {
						platOrder.setPlatOrdersList(orderslist);
						int size1 = orderslist.size();
						platOrder = salesPromotionActivityUtil.matchSalesPromotions(platOrder);
						orderslist = platOrder.getPlatOrdersList();
						int size2= orderslist.size();
						platOrderDao.update(platOrder);//更新主表
						if(size1<size2) {
							// 删除原来订单明细子表
							platOrdersDao.delete(platOrder.getOrderId());
							// 新增自动匹配拆解过后的订单明细子表
							platOrdersDao.insert(orderslist);
							
							autoMatch(platOrder.getOrderId(), accNum);
							try {
								resp = BaseJson.returnRespObj("ec/platOrder/autoMatch", isSuccess,
										"本次自动匹配成功" + successCount + "条订单，失败" + (orderIds.length - successCount) + "条订单", null);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return resp;
						}
						
					}
					if (orderslist.size()>0) {
						if(platOrdersList.size()==jishu+1) {
							//当jishu+1等于原订单明细的size时,说明正常匹配完商品
						}else {
							//非正常匹配商品时，需要把剩余部分加到列表中待下一次进行匹配
							for (int j = jishu+1; j < platOrdersList.size(); j++) {
								orderslist.add(platOrdersList.get(j));
							}
						}
						// 删除原来订单明细子表
						platOrdersDao.delete(platOrder.getOrderId());
						// 新增自动匹配拆解过后的订单明细子表
						platOrdersDao.insert(orderslist);
					}
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

						// 订单插入完成后，判断是否自动免审
						if (auditStrategyService.autoAuditCheck(platOrder, orderslist)) {
							// 返回true时，此订单通过免审，直接进入审核
							associatedSearchService.orderAuditByOrderId(platOrder.getOrderId(), "sys",sdf.format(new Date()));
							// 设置默认操作员sys
						}
					}else {
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

				
			}

		}
		try {
			resp = BaseJson.returnRespObj("ec/platOrder/autoMatch", isSuccess,
					"本次自动匹配成功" + successCount + "条订单，失败" + (orderIds.length - successCount) + "条订单", null);
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
			//订单已审核
			
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			MisUser misUser = misUserDao.select(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("拆单失败，订单已审核");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(4);// 4拆单
			logRecord.setTypeName("拆单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			message = "拆单失败，订单已审核";
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
			// 如果被拆分订单明细中含有未匹配到存货档案商品时，不允许拆单

			platOrder.setAuditHint("拆单失败，含有未匹配到存货档案的订单明细");
			platOrderDao.update(platOrder);
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			MisUser misUser = misUserDao.select(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("拆单失败，含有未匹配到存货档案的订单明细");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(4);// 4拆单
			logRecord.setTypeName("拆单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			message = "拆单失败，含有未匹配到存货档案的订单明细";
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
			platOrder.setAuditHint("拆单失败，拆分总数量应小于原单总数量");
			platOrderDao.update(platOrder);
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			MisUser misUser = misUserDao.select(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("拆单失败，拆分总数量应小于原单总数量");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(4);// 4拆单
			logRecord.setTypeName("拆单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			message = "拆单失败，拆分总数量应小于原单总数量";
			try {
				resp = BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return resp;
		}
		/*
		 * if (platOrder.getGoodNum() <= splitCount) { // 如果整单商品数量小于等于要拆分的数量总和，不允许拆单
		 * platOrder.setAuditHint("拆单失败，拆单数量大于订单内商品数量"); platOrderDao.update(platOrder);
		 * // 日志记录 LogRecord logRecord = new LogRecord(); logRecord.setOperatId(accNum);
		 * MisUser misUser = misUserDao.select(accNum); if (misUser != null) {
		 * logRecord.setOperatName(misUser.getUserName()); }
		 * logRecord.setOperatContent("拆单失败，拆单数量大于订单内商品数量");
		 * logRecord.setOperatTime(sdf.format(new Date()));
		 * logRecord.setOperatType(4);// 4拆单 logRecord.setTypeName("拆单");
		 * logRecord.setOperatOrder(platOrder.getEcOrderId());
		 * logRecordDao.insert(logRecord); message = "拆单失败，拆单数量大于订单内商品数量"; try { resp =
		 * BaseJson.returnResp("ec/platOrder/splitOrder", true, message, null); } catch
		 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 * return resp; }
		 */
		try {
			// 复制主表并生成新的主表主键单号
			PlatOrder platOrder2 = (PlatOrder) platOrder.clone();
			String newOrderId = getOrderNo.getSeqNo("ec", accNum);
			platOrder2.setOrderId(newOrderId);
			boolean flag = true;
			for (int i = 0; i < platordersids.length; i++) {
				// 查询订单明细
				PlatOrders platOrders = platOrdersDao.selectByNo(Long.parseLong(platordersids[i]));
				if (platOrders.getInvNum() < Integer.parseInt(splitnum[i])) {
					// 如果订单数量小于要拆分的数量时，不可拆分
					platOrder.setAuditHint("拆单失败，被拆分数量大于原单可拆数量");
					platOrderDao.update(platOrder);
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					MisUser misUser = misUserDao.select(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("拆单失败，被拆分数量大于原单可拆数量");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(4);// 4拆单
					logRecord.setTypeName("拆单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					flag = false;
					break;
				} else {
					PlatOrders platOrders2 = (PlatOrders) platOrders.clone();
					platOrders2.setOrderId(newOrderId);
					platOrders2.setInvNum(Integer.parseInt(splitnum[i]));// 设置数量为拆分数量
					platOrders2.setCanRefNum(platOrders2.getInvNum());
					platOrders.setSplitNum(Integer.parseInt(splitnum[i]));
					if (platOrders.getInvNum() == Integer.parseInt(splitnum[i])) {
						// 如果拆分数量跟原单数量一致
						platOrders2.setPayMoney(platOrders.getPayMoney());
						platOrders2.setCanRefMoney(platOrders.getCanRefMoney());
						platOrders.setInvNum(0);
						platOrders.setPayMoney(BigDecimal.ZERO);
						// 可不设置可退数量及金额，后续会删掉此条明细
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
					//计算新单的商品金额实付金额系统优惠金额
					xinGoodMoney=xinGoodMoney.add(platOrdersNew.get(j).getGoodMoney());
					xinPayMoney=xinPayMoney.add(platOrdersNew.get(j).getPayMoney());
				}

				platOrder2.setGoodMoney(xinGoodMoney);
				platOrder2.setPayMoney(xinPayMoney);
				platOrder2.setDiscountMoney(xinGoodMoney.subtract(xinPayMoney));
				platOrder.setGoodMoney(platOrder.getGoodMoney().subtract(platOrder2.getGoodMoney()));
				platOrder.setPayMoney(platOrder.getPayMoney().subtract(platOrder2.getPayMoney()));
				platOrder.setDiscountMoney(platOrder.getDiscountMoney().subtract(platOrder2.getDiscountMoney()));
				// 插入新的订单主表
				platOrderDao.insert(platOrder2);
				// 循环判断当前条被拆分子单数量是否为0 ，如果为0则删除当前条，不为0则update
				for (int i = 0; i < platOrdersOld.size(); i++) {
					if (platOrdersOld.get(i).getInvNum() == 0) {
						platOrdersDao.deleteByOrdersNo(platOrdersOld.get(i).getNo());
					} else {
						platOrdersDao.updateNumAndMoney(platOrdersOld.get(i));
					}
				}
				platOrderDao.update(platOrder);
				platOrdersDao.insert(platOrdersNew);
				message = "订单拆分成功";
			} else {
				message = "订单拆分失败";
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "异常，请联系管理员查看";
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "异常，请联系管理员查看";
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

	/**
	 * 单条订单下载入口
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
			//主表
			List<PlatOrder> platOrderList = new ArrayList<>();
			//优惠
			List<CouponDetail> couponList = new ArrayList<CouponDetail>();

			if (storeRecord.getEcId().equals("JD")) {
				resp = jdDownloadByOrderId(userId, map);
			} else if (storeRecord.getEcId().equals("TM")) {
				String tid = map.get("orderId").toString();
				resp = tmdownloadByOrderIdSdk(tid, userId, storeId,platOrderList,couponList);
				 //分支插库分配订单号
	            List<String> seqNoList = getOrderNoList.getSeqNoList(platOrderList.size(), "ec", userId);
	            downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId, seqNoList);
	            if (downloadCount>0) {
					resp = BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载成功" + tid, null);
				}else {
					resp = BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载失败", null);
				}

				//分支插库分配订单号
				//downloadCount = platOrderDownloadServiceImpl.DownliadOrder(platOrderList, null, couponList, null, downloadCount, userId);


			}
			else if (storeRecord.getEcId().equals("XMYP")) {
				try {
					String orderId = map.get("orderId").toString();
					resp = platOrderXMYP.XMYPDownloadByOrderId(userId, orderId, storeId);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "下载失败，单条订单下载出现异常，请检查", null);
				}
			}else if (storeRecord.getEcId().equals("PDD")) {
				try {
					Map map2 = platOrderPdd.pddDownloadByOrderCode(map.get("orderId").toString(), userId, storeId, 0);
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "下载完成，本次成功下载"+map2.get("downloadCount").toString()+"条订单", null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "下载失败，拼多多单条订单下载出现异常，请检查", null);
				}
			} else if(storeRecord.getEcId().equals("KaoLa")){
				resp = KaoLaDownloadByOrderId(userId, "", "", storeId, map.get("orderId").toString());
			}else if(storeRecord.getEcId().equals("XHS")){
				try {
					resp = platOrderXHS.xhsDownloadByPlatOrder(map.get("orderId").toString(), userId, storeId, true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					try {
						resp=BaseJson.returnRespObj("ec/platOrder/download", true, "订单下载异常，请重试", null);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}else if(storeRecord.getEcId().equals("MaiDu")){
				try {
					
					resp = platOrderMaiDu.maiDuDownload(userId, 1, 20, "", "", storeId,map.get("orderId").toString());
				} catch (Exception e1) {
					resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "下载异常", null);
					//e1.printStackTrace();
				}
				
			}else {
				resp = BaseJson.returnRespObj("ec/platOrder/downloadByOrderId", true, "下载失败，所选择店铺暂不支持下载订单", null);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}


	//导入excle
	private Map<String,PlatOrder> uploadPlatOrder(MultipartFile  file,String userId){
		Map<String,PlatOrder> temp = new HashMap<>();
		int j=0;
		try {
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum+1; i<= lastRowNum;i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				if( StringUtils.isEmpty(GetCellData(r,"平台订单号"))){
					continue;
				}
				//创建实体类
				PlatOrder platOrder=new PlatOrder();
				String ecOrderid = GetCellData(r,"平台订单号");
				if(temp.containsKey(ecOrderid)) {
					platOrder = temp.get(ecOrderid);
				}else {
					platOrder.setOrderId(getOrderNo.getSeqNo("ec", userId));
				}
				
				platOrder.setStoreId(GetCellData(r,"店铺编号"));
				platOrder.setStoreName(GetCellData(r,"店铺名称"));
				platOrder.setPayTime(GetCellData(r,"付款时间"));
				platOrder.setGoodNum(GetBigDecimal(GetCellData(r,"整单商品数量"),0).intValue());
				platOrder.setGoodMoney(GetBigDecimal(GetCellData(r,"商品金额"),2));
				platOrder.setPayMoney(GetBigDecimal(GetCellData(r,"实付金额"),2));
				platOrder.setBuyerNote(GetCellData(r,"买家备注"));
				platOrder.setSellerNote(GetCellData(r,"卖家备注"));
				platOrder.setRecAddress(GetCellData(r,"收货地址"));
				platOrder.setBuyerId(GetCellData(r,"买家会员号"));
				platOrder.setRecName(GetCellData(r,"收货人姓名"));
				platOrder.setRecMobile(GetCellData(r,"手机号"));
				platOrder.setEcOrderId(GetCellData(r,"平台订单号"));
				platOrder.setNoteFlag(0);
				platOrder.setIsClose(0);
				
				platOrder.setIsShip(Integer.parseInt(GetCellData(r,"是否发货")));
				platOrder.setExpressNo(GetCellData(r,"快递单号"));
				platOrder.setExpressCode(GetCellData(r,"快递公司编码"));
				platOrder.setIsAudit(0);
				platOrder.setIsInvoice(0);
				platOrder.setAdjustMoney(GetBigDecimal(GetCellData(r,"调整金额"), 2));
				platOrder.setDiscountMoney(GetBigDecimal(GetCellData(r,"优惠金额"),2));
				platOrder.setOrderStatus(0);
				platOrder.setReturnStatus(0);
				platOrder.setHasGift((GetBigDecimal(GetCellData(r,"包含赠品"),0).intValue()));
				platOrder.setMemo(GetCellData(r,"订单备注"));
				platOrder.setAdjustStatus(0);
				platOrder.setTradeDt(GetCellData(r,"下单时间"));
				platOrder.setBizTypId("2");
				platOrder.setSellTypId("1");
				platOrder.setRecvSendCateId("6");
				platOrder.setOrderSellerPrice(GetBigDecimal(GetCellData(r,"结算金额"),2));
				platOrder.setProvince(GetCellData(r,"省"));
				platOrder.setCity(GetCellData(r,"市"));
				platOrder.setCounty(GetCellData(r,"区"));
				platOrder.setFreightPrice(GetBigDecimal(GetCellData(r,"运费"),2));
				platOrder.setDeliverWhs(GetCellData(r,"发货仓库编码"));
				platOrder.setDeliverSelf((GetBigDecimal(GetCellData(r,"是否自发货"),0).intValue()));
				platOrder.setWeight(GetBigDecimal(GetCellData(r,"重量"), 6));
				platOrder.setDownloadTime(sdf.format(new Date()));
				
				
				
				List<PlatOrders> PlatOrderslist = platOrder.getPlatOrdersList();//子表
				if(PlatOrderslist == null) {
					PlatOrderslist = new ArrayList<>();
				}
				PlatOrders platOrders= new PlatOrders();
				platOrders.setGoodId(GetCellData(r,"spu"));
				platOrders.setGoodName(GetCellData(r,"商品名称"));
				platOrders.setGoodNum((GetBigDecimal(GetCellData(r,"sku数量"),0).intValue()));
				platOrders.setGoodMoney(GetBigDecimal(GetCellData(r,"明细商品金额"),2));
				platOrders.setPayMoney(GetBigDecimal(GetCellData(r,"明细实付金额"),2));
				platOrders.setGoodSku(GetCellData(r,"商品sku"));
				platOrders.setDiscountMoney(GetBigDecimal(GetCellData(r,"明细优惠金额"),2));
				platOrders.setAdjustMoney(GetBigDecimal(GetCellData(r,"明细调整金额"), 2));
				platOrders.setMemo(GetCellData(r,"明细备注"));
				platOrders.setGoodPrice(GetBigDecimal(GetCellData(r,"明细单价"), 8));
				platOrders.setPayPrice(GetBigDecimal(GetCellData(r,"明细实付单价"), 8));
				platOrders.setSellerPrice(GetBigDecimal(GetCellData(r,"结算单价"), 8));
				platOrders.setInvId(GetCellData(r,"存货编码"));
				platOrders.setInvNum((GetBigDecimal(GetCellData(r,"存货数量"),0).intValue()));
				platOrders.setPtoCode(GetCellData(r,"母件编码"));
				platOrders.setPtoName(GetCellData(r,"母件名称"));
				platOrders.setIsGift((GetBigDecimal(GetCellData(r,"是否赠品"),0).intValue()));
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
			platOrder.setAuditHint("文件的第"+j+"行导入格式有误，无法导入!");
			temp.put("errMessage", platOrder);
			//throw new RuntimeException("文件的第"+j+"行导入格式有误，无法导入!"+e.getMessage());
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
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("导入成功");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(16);// 4导入订单
					logRecord.setTypeName("导入订单");
					logRecord.setOperatOrder(entry.getValue().getEcOrderId());
					logRecordDao.insert(logRecord);
					//导入完成自动匹配
					autoMatch(entry.getValue().getOrderId(), userId);
					successCount++;
				}
			}
			message="导入完成，本次成功导入订单"+successCount+"条";
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
			resp = BaseJson.returnRespObjList("ec/platOrder/Near15DaysOrder", true, "查询成功！", null, list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespObjList("ec/platOrder/Near15DaysOrder", true, "查询异常！", null, null);
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
			resp = BaseJson.returnRespList("ec/platOrder/orderssssList", true, "查询成功！", count, pageNo, pageSize, 0, 0,
					OrderssssList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/orderssssList 异常说明：", e);
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
//			resp = BaseJson.returnRespList("ec/platOrder/exportOrderssssList", true, "查询成功！", OrderssssList.size(), 1, OrderssssList.size(), 0, 0,
//					OrderssssList);
			resp = BaseJson.returnRespObjListAnno("ec/platOrder/exportOrderssssList", true, "查询成功！", null, exportOrdersList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/exportOrderssssList 异常说明：", e);
		}
		return resp;
	}

	/**
	 * 批量查询订单明细列表
	 */
	@Override
	public String batchList(List<String> list) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			List<Orderssss> OrderssssList = platOrdersDao.batchList(list);
			
			resp = BaseJson.returnRespList("ec/platOrder/batchList", true, "查询成功！", OrderssssList.size(), 1, OrderssssList.size()+1, 0, 0,
					OrderssssList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/batchList 异常说明：", e);
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
					platOrder2.setDeliverWhs(whsEncd);//设置订单的发货仓库
					platOrderDao.update(platOrder2);
					List<PlatOrders> platOrders = platOrdersDao.select(platOrder2.getOrderId());
					List<InvtyTab> invtyTabs = new ArrayList<InvtyTab>();
					//循环判断原订单明细里面的商品是否已占用可用量，已占用可用量的需要释放原仓的可用量
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
							invtyNumMapper.updateAInvtyTabJia(invtyTabs11);// 可用量增加(加法)
						}
					}
					//释放原仓可用量用需要清空原订单明细的批次，效期等信息
					for (int j = 0; j < platOrders.size(); j++) {
						platOrders.get(j).setBatchNo("");
						platOrders.get(j).setDeliverWhs(whsEncd);
						platOrders.get(j).setInvldtnDt(null);
						platOrders.get(j).setPrdcDt(null);
						
					}
					platOrdersDao.delete(platOrder2.getOrderId());
					platOrdersDao.insert(platOrders);
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					MisUser misUser = misUserDao.select(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单修改，批量修改发货仓库为："+whsDoc.getWhsNm());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(5);// 5修改
					logRecord.setTypeName("订单修改");
					logRecord.setOperatOrder(platOrder2.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					if (isSuccess) {
						//插入后自动匹配
						autoMatch(platOrder2.getOrderId(), userId);
					}
					successCount++;
				}
				
			}
			message="批量修改成功"+successCount+"条订单，失败"+(orderIdsss.length-successCount)+"条订单";
			resp = BaseJson.returnRespObj("ec/platOrder/batchEditWhs", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/batchEditWhs 异常说明：", e);
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
					platOrder2.setExpressCode(expressCode);//设置订单的快递公司
					platOrderDao.update(platOrder2);
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(userId);
					MisUser misUser = misUserDao.select(userId);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单修改，批量修改快递公司为："+expressCorp.getExpressNm());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(5);// 5修改
					logRecord.setTypeName("订单修改");
					logRecord.setOperatOrder(platOrder2.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					if (isSuccess) {
						//插入后自动匹配
						autoMatch(platOrder2.getOrderId(), userId);
					}
					successCount++;
				}
				
			}
			message="批量修改成功"+successCount+"条订单，失败"+(orderIdsss.length-successCount)+"条订单";
			resp = BaseJson.returnRespObj("ec/platOrder/batchEditExpress", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/batchEditExpress 异常说明：", e);
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
				//判断原订单明细里面的商品是否已占用可用量，已占用可用量的需要释放原仓的可用量
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
						invtyNumMapper.updateAInvtyTabJia(invtyTabs11);// 可用量增加(加法)
					}
				}
				//释放原仓可用量用需要清空原订单明细的批次，效期等信息
				platOrders.get(i).setBatchNo("");
				platOrders.get(i).setInvldtnDt(null);
				platOrders.get(i).setPrdcDt(null);
				

                platOrders.get(i).setInvId(invIdLast);
                result = result - platOrders.get(i).getInvNum();

                if (platOrders.get(i).getInvNum() != null || ("").equals(platOrders.get(i).getInvNum())) {
                    int newinvNum = platOrders.get(i).getInvNum() * Integer.parseInt(multiple);
                    platOrders.get(i).setInvNum(newinvNum);
                    BigDecimal payMoney = platOrders.get(i).getPayMoney();//实付钱
                    BigDecimal number = new BigDecimal(0);
                    number = BigDecimal.valueOf(newinvNum);
                    BigDecimal newpayPrice = payMoney.divide(number, 8, BigDecimal.ROUND_HALF_DOWN);
                    platOrders.get(i).setPayPrice(newpayPrice);
                    platOrdersDao.updateInvIdGooIdsGoodMoney(platOrders.get(i));
                }
                //修改订单数量
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
            resp = BaseJson.returnRespObj("ec/platOrder/updateecGooId", true, "更新成功", null);
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

			resp = BaseJson.returnRespObjList("ec/platOrder/selectecGooId", true, "查询成功！",platorder ,platOrdersList);
		} catch (IOException e) {
			logger.error("URL：ec/platOrder/selectecGooId 异常说明：", e);
		}
		return resp;
		
	}

	@Override
	public PlatOrder selectecByorderId(String orderId) {
		
		PlatOrder platOrder2 =null;
		try {
			 platOrder2 = platOrderDao.selectecByorderId(orderId);
			
		} catch (Exception e) {
			logger.error("URL：ec/platOrder/selectecGooId 异常说明：", e);
		}
		return platOrder2;
		
	}

	@Override
	public List<PlatOrders> selectecByorderIds(String orderId) {
		
		List<PlatOrders> platOrder2 =null;
		try {
			platOrder2 = platOrderDao.selectecByorderIds(orderId);
			
		} catch (Exception e) {
			logger.error("URL：ec/platOrder/selectecGooId 异常说明：", e);
		}
		return platOrder2;
		
		
	}
	//订单关闭
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
				//已审核不能关闭
				// 日志记录
				LogRecord logRecord = new LogRecord();

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("订单关闭：失败，订单已审核不能直接关闭");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(7);// 7订单关闭
				logRecord.setTypeName("订单关闭");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
			}else if(platOrder.getIsClose()==1) {
				//订单已关闭
				// 日志记录
				LogRecord logRecord = new LogRecord();

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("订单关闭：失败,订单已关闭，不能重复关闭");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(7);// 7订单关闭
				logRecord.setTypeName("订单关闭");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
			}else {
				//订单开始关闭
				//判断订单明细中是否已占用可用量的明细
				//有占用可用量的明细需要释放可用量
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

				//更新订单关闭状态
				int re = platOrderDao.closeOrder(orderIds[i]);

				if(re>0) {
					//记录关闭信息
					successCount++;
					// 日志记录
					LogRecord logRecord = new LogRecord();

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单关闭：成功");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(7);// 7订单关闭
					logRecord.setTypeName("订单关闭");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					//更新成功后增加可用量
					for (int j = 0; j < invtyTabs.size(); j++) {
						List<InvtyTab> invtyTabs11 = new ArrayList<InvtyTab>();
						invtyTabs11.add(invtyTabs.get(j));
						invtyNumMapper.updateAInvtyTab(invtyTabs11);

					}
					//释放原仓可用量用需要清空原订单明细的批次，效期等信息
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
			resp = BaseJson.returnRespObj("ec/platOrder/closeOrder", true, "关闭成功，本次成功关闭订单"+successCount+"条，失败"+(orderIds.length-successCount)+"条", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	//订单打开
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
				//订单未关闭
				// 日志记录
				LogRecord logRecord = new LogRecord();

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("订单打开：失败,订单未关闭");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(21);// 21订单打开
				logRecord.setTypeName("订单打开");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
			}else {
				//订单开始打开
				//订单打开后需要执行自动匹配
				//更新订单状态
				int re = platOrderDao.openOrder(orderIds[i]);

				if(re>0) {

					autoMatch(orderIds[i], accNum);
					//记录打开信息
					successCount++;
					// 日志记录
					LogRecord logRecord = new LogRecord();

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单打开：成功");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(21);// 21订单打开
					logRecord.setTypeName("订单打开");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/platOrder/openOrder", true, "打开成功，本次成功打开订单"+successCount+"条，失败"+(orderIds.length-successCount)+"条", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}

}
