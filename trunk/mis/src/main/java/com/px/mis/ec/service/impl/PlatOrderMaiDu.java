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
 * 订单-脉度
 * 
 * @author lxya0
 *
 */
@Component
public class PlatOrderMaiDu {

	@Autowired
	private StoreSettingsDao storeSettingsDao; // 店铺设置
	@Autowired
	private PlatOrderDao platOrderDao; // 订单主表
	@Autowired
	private StoreRecordDao storeRecordDao; // 店铺档案
	@Autowired
	private PlatOrdersDao platOrdersDao; // 订单子表
	@Autowired
	private GetOrderNo getOrderNo; // 生成订单号
	@Autowired
	private GoodRecordDao goodRecordDao; // 商品档案
	@Autowired
	private InvtyDocDao invtyDocDao; // 存货档案
	@Autowired
	private ProdStruMapper prodStruDao; // 产品结构
	@Autowired
	private CouponDetailDao couponDetailDao; // 优惠明细
	@Autowired
	private PlatOrderPaymethodDao paymethodDao; // 支付明细
	@Autowired
	private RefundOrderDao refundOrderDao; // 退款单主表
	@Autowired
	private RefundOrdersDao refundOrdersDao; // 退款单子表
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
	 * 生成脉度sign
	 */
	public String getSign(String appKey, String appSecret) throws IOException, NoSuchAlgorithmException {

		Map<String, String> params = new LinkedHashMap<>(); // 签名字典
		params.put("AppKey", appKey);
		params.put("TimeStamp", sf.format(new Date()));
		params.put("ModuleKeyLogo", "");
		params.put("ActionKeyLogo", "");

		String signMethod = "md5";

		return MaiDuSignUtil.signTopRequest(params, appSecret, signMethod);
	}

	/**
	 * 订单下载-脉度
	 * 
	 * @param accNum    当前用户
	 * @param pageNo    当前页
	 * @param pageSize  当前页数
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param storeId   店铺编码
	 * @return 请求结果
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
		// 设置
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
	 * 简单处理脉度订单
	 * 非null非空非AAAAA非BBBBB,存在list为空情况
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
			Map<String, Object> params = new HashMap<>(); // 脉度发货回传参数
			params.put("AppKey", storeSettings.getAppKey());
			params.put("TimeStamp", sf.format(new Date()));
			params.put("ModuleKeyLogo", "000");
			params.put("ActionKeyLogo", "000");
			params.put("Sign", getSign(storeSettings.getAppKey(), storeSettings.getAppSecret())); // 签名
			if (expressCorp.getCompanyCode().equals("JDWL")) {
				params.put("TransferCode", "003"); // 物流编号 jd
			}else {
				params.put("TransferCode", "sf"); // 物流编号 sf顺丰快递
			}
			params.put("ExpressCode", expressCode); // wuliu 快递单号
			params.put("OrderSubs", subs); // 子订单发货信息
			params.put("FlagMemo", ""); // 订单备注
			params.put("ExecutorId", 1);
			params.put("OrderId", subs.get(0).getOrderId()); // 脉度订单ID
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
			rep.setMsg("查询对应平台订单明细为空，不回传");
		}
		return rep;
	}
	
	public XS_OrdersGetListResponse MaiDuOrderShip1(ExpressCorp expressCorp,LogisticsTab logistics, EcExpress express,
			StoreSettings storeSettings) throws Exception {

		XS_OrdersGetListResponse rep = new XS_OrdersGetListResponse();
		List<XS_Orders> orderQueryList = new ArrayList<>();
		
		orderQueryList = postMaiDuOrder(storeSettings, 1, 999, orderQueryList, logistics.getEcOrderId(), "", "", "");
		
		// 1待付款 2待发货 3已发货4 已完成5 已取消6 退款中7 已关闭8 待评价20 付款成功
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
					//查询物流单号
					//TODO 
					List<LogisticsTab> tabList = new ArrayList<>();//需要查询多个物流单号
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
					rep.setMsg("查询对应平台订单明细为空，不回传");
				} 
			}else if(orderQueryList.get(0).getOrderStatus()==3||
					orderQueryList.get(0).getOrderStatus()==4){
					rep.setDone(false);
					rep.setMsg("订单已发货或已完成，系统默认发货回传成功");
				
			}else {
				if (orderQueryList.get(0).getOrderStatus()==5) {
						//5 已取消6 退款中7 已关闭
						rep.setDone(false);
						rep.setMsg("订单已取消，发货回传失败");
				}else if(orderQueryList.get(0).getOrderStatus()==6) {
					rep.setDone(false);
					rep.setMsg("订单退款中，发货回传失败");
				}else if(orderQueryList.get(0).getOrderStatus()==7) {
					rep.setDone(false);
					rep.setMsg("订单已关闭，发货回传失败");
				}else {
					rep.setDone(false);
					rep.setMsg("订单状态不属于发货状态，发货回传失败");
				}
			}
				
			
			
		} else {		
			rep.setDone(false);
			rep.setMsg("未查询到平台对应订单，请使用强制发货");
			
		}
		return rep;
	}
	
	
	/**
	 * 脉度平台发货接口
	 * 
	 * @param logistics     物流表信息
	 * @param express       快递公司信息
	 * @param storeSettings 店铺设置
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public XS_OrdersGetListResponse MaiDuOrderShip(LogisticsTab logistics, EcExpress express,
			StoreSettings storeSettings) throws NoSuchAlgorithmException, IOException {

		Map<String, Object> params = new HashMap<>(); // 脉度发货回传参数
		params.put("AppKey", storeSettings.getAppKey());
		params.put("TimeStamp", sf.format(new Date()));
		params.put("ModuleKeyLogo", "000");
		params.put("ActionKeyLogo", "000");
		params.put("Sign", getSign(storeSettings.getAppKey(), storeSettings.getAppSecret())); // 签名
		// logistics.getEcOrderId();
		params.put("TransferCode", "003"); // 物流编号 jd
		params.put("ExpressCode", logistics.getExpressCode()); // wuliu 快递单号
		
		List<XS_Orders> orderQueryList = new ArrayList<>();
		orderQueryList = postMaiDuOrder(storeSettings, 1, 999, orderQueryList, logistics.getEcOrderId(), "", "", "");
		List<XS_OrderSub> subs = new ArrayList<>();
		//System.out.println("--ord--->"+orderQueryList.size());
		
		for (XS_Orders xs : orderQueryList) {
            //subs = new ArrayList<>();
			params.put("OrderId", xs.getOrderId()); // 脉度订单ID
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
               // this.logger.error("非品星商品不推送:{}", sub.getMerchantCode());
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
		params.put("OrderSubs", subs); // 子订单发货信息
		params.put("FlagMemo", ""); // 订单备注
		params.put("ExecutorId", 1);
		
System.out.println(JSON.toJSONString(params));
		return JSON.parseObject(
				SendUtil.sendPost("https://qinwe-api.rfc-china.com/api/XS_Orders/SendGood", 
						JSON.toJSONString(params)),
				XS_OrdersGetListResponse.class);

	}

	/**
	 * post脉度-批量获取订单
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
		String sign = getSign(appKey, appSecret); // 生成脉度sign

		// 发送获取订单请求
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("AppKey", appKey);
		params.put("TimeStamp", sf.format(new Date()));
		params.put("Sign", sign); // 签名
		params.put("ModuleKeyLogo", "000");
		params.put("ActionKeyLogo", "000");
		if (orderCode != null) {
			params.put("OrderCode", orderCode);
		} else {
			params.put("OrderCode", ""); // 9569434542 //927057517117410
		}
		params.put("OrderStutas", orderState); // 1待付款 2待发货 3已发货4 已完成5 已取消6 退款中7 已关闭8 待评价20 付款成功
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

			message = "订单下载:脉度平台下载订单异常,异常信息:" + result.getData().getErrMsg();
			isSuccess = false;
			logger.error("订单下载:脉度平台下载订单异常,异常信息:{}", result.getData().getErrMsg());
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
			logger.info("订单下载：脉度平台查询订单成功,本次查询共计:{}条", result.getData().getTotalCounts());
		}

		return orderQueryList;
	}

	/**
	 * 订单详情获取-苏宁
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

					List<PlatOrders> ordersList = new ArrayList<>(); // 商品
					List<InvoiceInfo> invoiceList = new ArrayList<>(); // 发票
					List<CouponDetail> couponsList = new ArrayList<>(); // 优惠
					List<PlatOrderPaymethod> payMentList = new ArrayList<>(); // 支付

					BigDecimal goodMoney = new BigDecimal(0.00);
					BigDecimal adjustMoney = new BigDecimal(0.00);

					String orderId = getOrderNo.getSeqNo("ec", userId);

					order.setOrderId(orderId); // px-订单编号
					order.setStoreId(storeId);
					order.setStoreName(storeRecord.getStoreName());
					order.setTradeDt(query.getCreateTime()); // 拍单时间
					order.setBuyerNote(query.getRemark());// 订单备注 -买家

					XS_OrderAddress address = query.getxS_OrderAddress();

					if (address != null) {
						order.setRecName(address.getAccepter()); // 收货人
						// map.put("receiver_phone",address.getTelePhone()); //固定电话
						order.setRecMobile(address.getMobile()); // 手机号码
						// map.put("receiver_zip",address.getPostCode()); //邮政编码
						order.setProvince(address.getProvinceName()); // 省名称
						order.setCity(address.getCityName()); // 市名称
						order.setCounty(address.getDistrictName()); // 区名称
						
						order.setRecAddress(address.getProvinceName()+address.getCityName()+address.getDistrictName()+address.getAddress()); // 收货地址
					}

					// 获取发票信息
					if (query.getxS_Invoice() != null) {

						invoiceList = getInvoice(query, storeId, orderId, invoiceList, order);
						order.setIsInvoice(1);
					} else {
						order.setIsInvoice(0);
					}

					order.setEcOrderId(String.valueOf(query.getOrderCode())); // B2C订单号

					order.setPayTime(query.getPayTime()); // 订单支付时间,格式：yyyy-MM-dd HH:mm:ss
					order.setOrderStatus(query.getOrderStatus()); // 订单总状态
					order.setCanMatchActive(0);
					order.setBuyerId(String.valueOf(query.getCusCode())); // 买家帐号
					order.setSellTypId("1");// 设置销售类型普通销售
					order.setBizTypId("2");// 设置业务类型2c
					order.setRecvSendCateId("6");//设置收发类别
					order.setOrderStatus(0); // 订单状态
					order.setDownloadTime(sf.format(new Date()));
					order.setReturnStatus(0); // 退货状态
					order.setIsClose(0); // 是否关闭
					order.setIsAudit(0); // 是否客审
					order.setVenderId("MaiDu"); // '商家id'
					order.setFreightPrice(query.getPostage()); // 运费。订单行项目对应的运费
					order.setDeliverSelf(1); // 自发货
					if(query.getOrderStatus()==2) {
						order.setIsShip(0);//是否发货
					}else {
						order.setIsShip(1);//是否发货
					}

					// 折扣金额 优惠
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
								pay.setPayStatus("待支付");
							} else if (payment.getPayStatus() == 1) {
								pay.setPayStatus("支付成功");
							} else if (payment.getPayStatus() == 2) {
								pay.setPayStatus("非正常支付");
							} else if (payment.getPayStatus() == 3) {
								pay.setPayStatus("支付关闭");
							}

							pay.setOrderId(orderId);
							pay.setPlatId("MaiDu");
							pay.setStoreId(storeId);
							pay.setBanktypecode(String.valueOf(payment.getPayConCode()));

							if (payment.getPayConCode() == 50) {

								pay.setPayWay("支付宝支付");
							} else if (payment.getPayConCode() == 100) {
								pay.setPayWay("微信支付");
							} else if (payment.getPayConCode() == 200) {
								pay.setPayWay("现金支付");
							} else if (payment.getPayConCode() == 300) {
								pay.setPayWay("礼券");
							} else if (payment.getPayConCode() == 400) {
								pay.setPayWay("积分");
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

							orders.setDeliverWhs(query.getWarehouseCode()); // 仓库编码

							orders.setGoodId(String.valueOf(detail.getItemId())); // 平台商品编号

							orders.setGoodMoney(new BigDecimal(detail.getToalAmt())); // 商品金额

							orders.setGoodName(detail.getTitle()); // 商品中文名称
							//orders.setInvId(detail.getMerchantCode());

							orders.setGoodPrice(new BigDecimal(detail.getPrice())); // 商品单价
							orders.setPayPrice(new BigDecimal(detail.getRealPrice())); // 实付单价
							orders.setGoodNum(detail.getQty()); // 数量
							goodNum=goodNum+orders.getGoodNum();
							goodMoney = goodMoney
									.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));

							adjustMoney = adjustMoney.add(pAdjustMoney);

							orders.setDiscountMoney(pDiscountMoney); // 系统优惠金额
							orders.setAdjustMoney(pAdjustMoney); // 优惠劵金额
							ordersList.add(orders);
							
						}
					}
					
					order.setGoodMoney(goodMoney); // 应付金额
					order.setAdjustStatus(0);
					order.setAdjustMoney(adjustMoney);
					order.setPayMoney(goodMoney.subtract(adjustMoney)); // 支付金额，支付方式的支付金额
					order.setGoodNum(goodNum);
					order.setOrderSellerPrice(order.getPayMoney());
					order.setDiscountMoney(order.getGoodMoney().subtract(order.getPayMoney()));
					order.setPlatOrdersList(ordersList);
					// 计算按照比例平均单价
					ordersList = platOrderPdd.countAveragePrice(ordersList, order.getPayMoney());
					// 检查是否组套
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
						 * // 订单插入完成后，判断是否自动免审 PlatOrder platorder =
						 * platOrderDao.selectByEcOrderId(order.getEcOrderId()); List<PlatOrders>
						 * orderslist = platOrdersDao.select(platorder.getOrderId()); if
						 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
						 * 返回true时，此订单通过免审，直接进入审核
						 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
						 * // 设置默认操作员sys } isSuccess = true; message = "下载成功";
						 * logger.info("订单下载:脉度平台订单下载成功");
						 */
						// 执行自动匹配
						platOrderService.autoMatch(orderId, userId);
						isSuccess = true;
					}

				} else {
					message = "订单下载:脉度平台订单已存在,订单号:" + query.getOrderCode();
					isSuccess = false;
					// 订单已存在
					logger.error("订单下载:脉度平台订单已存在,订单号:{}", query.getOrderCode());

				}
			}

		} else {

			message = "订单下载:脉度平台下载订单数据为空";
			isSuccess = false;
		}
		// 日志记录
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(userId);
		MisUser misUser = misUserDao.select(userId);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent("本次自动下载店铺：" + storeSettings.getStoreName() + "订单" + downloadCount + "条");
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(10);// 9手工下载 10 自动下载
		logRecord.setTypeName("自动下载");
		logRecordDao.insert(logRecord);
		if (isSuccess) {
			message = "本次自动下载店铺： " + storeSettings.getStoreName() + "订单" + downloadCount + "条";
		}
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		return map;
	}

	/**
	 * 获取发票信息
	 */
	private List<InvoiceInfo> getInvoice(XS_Orders query, String storeId, String orderId, List<InvoiceInfo> invoiceList,
			PlatOrder order) {
		// 发票
		XS_Invoice xs_invoice = query.getxS_Invoice(); // 发票

		InvoiceInfo info = new InvoiceInfo();
		info.setPlatId("MaiDu");
		info.setShopId(storeId);
		info.setOrderId(orderId);
		info.setInvoiceType(String.valueOf(xs_invoice.getInvType()));// 发票类型 -- 增值还是普通（01增值 02普通 04 电子发票）
		info.setInvoiceTitle(xs_invoice.getInvUserName()); // 抬头
		info.setInvoiceContentId(xs_invoice.getInvContent());// 发票内容
		XS_InvoiceSub xs_invoiceSub = xs_invoice.getXS_InvoiceSub(); // 发票明细
		if (xs_invoiceSub != null) {
			info.setInvoiceConsigneePhone(xs_invoiceSub.getMobile()); // 发票收件人手机
			info.setInvoiceCode(xs_invoiceSub.getTaxpayerCode()); // 纳税人识别号
		}

		order.setIsInvoice(1);
		order.setInvoiceTitle(xs_invoice.getInvUserName()); // 抬头
		invoiceList.add(info);
		return invoiceList;
	}

	/**
	 * 脉度-根据订单获取支付信息
	 */
	private List<PM_PayApp> selectPmPayAppByOrderId(String appKey, String appSecret, long orderId)
			throws IOException, NoSuchAlgorithmException {
		String sign = getSign(appKey, appSecret); // 生成脉度sign

		List<PM_PayApp> pmPayAppList = new ArrayList<>();
		// 发送获取订单请求
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("AppKey", appKey);
		params.put("TimeStamp", sf.format(new Date()));
		params.put("Sign", sign); // 签名
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

			logger.info("订单下载：脉度平台获取支付信息成功,本次查询共计:{}条", pmPayAppList.size());
		} else {
			logger.info("订单下载：脉度平台查询订单支付信息异常:{}", result);
		}

		return pmPayAppList;
	}
}
