package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.px.mis.ec.dao.CouponDetailDao;
import com.px.mis.ec.dao.EcExpressSettingsDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;

@Component
@Transactional
public class PlatOrderXHS {

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
	private EcExpressSettingsDao ecExpressSettingsDao; // xhs小红书
	@Autowired
	private CouponDetailDao couponDetailDao; // 优惠明细
	@Autowired
	private RefundOrderDao refundOrderDao; // 退款单主表
	@Autowired
	private RefundOrdersDao refundOrdersDao; // 退款单子表
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private MisUserDao misUserDao;
	@Autowired
	private PlatOrderServiceImpl orderServiceImpl;

	private Logger logger = LoggerFactory.getLogger(PlatOrderXHS.class);
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 小红书-商品下载
	 * 
	 * @throws Exception
	 */
	public void xhsGoodRecord(StoreSettings storeSettings, int pageNo, int pageSize, String userName, String date,
			List<GoodRecord> goodRecordList) throws NoSuchAlgorithmException, IOException, URISyntaxException {
		String method = "/ark/open_api/v1/items";

		Map<String, String> map = new HashMap<String, String>();
//		商品状态(0为编辑中，1为待审核，2为审核通过)
		map.put("status", "2");
//		商品页数, 从第一页开始,默认为1
		map.put("page_no", pageNo + "");
//		商品列表每页数量，默认为50，上限为50
		map.put("page_size", pageSize + "");
		map.put("buyable", "true");
		/*
		 * create_time_to Int 否 商品创建时间结束时间，Unix-Time时间戳 update_time_from Int 否
		 * 商品更新时间开始时间，Unix-Time时间戳 update_time_to Int 否 商品更新时间结束时间，Unix-Time时间戳
		 * stock_gte Int 否 库存大于等于某数 stock_lte Int 否 库存小于等于某数
		 */
//		商品是否可售卖，true为在架上可售卖，false为已下架不可售卖
//		map.put("buyable", "true");

		JSONObject response = ECHelper.getXHS(method, storeSettings, map);
		if (response == null) {
			logger.error("下载错误：" + null);

			return;
		}
		Boolean xhsSuccess = response.getBoolean("success");
		if (!xhsSuccess) {
			response.getString("error_msg");
			logger.error("下载错误：" + response.getString("error_msg"));
			return;
		}
		JSONObject pddtotal = response.getJSONObject("data");
		int totalItem = pddtotal.getIntValue("total");// total INTEGER 返回商品总数
		JSONArray jsonArray = pddtotal.getJSONArray("hits");
		logger.info("第" + pageNo + "页,共" + totalItem + "条");

		for (int i = 0; i < jsonArray.size(); i++) {
			// 遍历 jsonarray 数组，把每一个对象转成 json 对象
			JSONObject job = jsonArray.getJSONObject(i);

			GoodRecord goodRecord = new GoodRecord();
			goodRecord.setEcGoodId(job.getJSONObject("item").getString("id")); // (50) DEFAULT NULL '平台商品编号',
			goodRecord.setStoreId(storeSettings.getStoreId()); // (50) DEFAULT NULL '店铺编号',
			goodRecord.setEcId("XHS"); // (50) DEFAULT NULL '电商平台',
			goodRecord.setEcGoodName(job.getJSONObject("item").getString("name")); // (100) DEFAULT NULL '平台商品名称',
			goodRecord.setGoodSku(null); // (50) DEFAULT NULL '店铺商品sku',
			goodRecord.setGoodId(job.getJSONObject("item").getString("barcode")); // (50) DEFAULT NULL '商品编号',
			InvtyDoc invtyDoc = invtyDocDao
					.selectInvtyDocByInvtyDocEncd(job.getJSONObject("item").getString("barcode"));
			if (invtyDoc != null) {
				goodRecord.setGoodName(invtyDoc.getInvtyNm());// '存货商品名称',
				goodRecord.setGoodMode(invtyDoc.getSpcModel());// '规格型号',
			}
//   无	  goodRecord.setUpsetPrice(upsetPrice); 	//	 '最低售价',	
			goodRecord.setSafeInv("0"); // (50) DEFAULT NULL '安全库存',
			// sku_list 下 spec STRING 规格名称
//			goodRecord.setSkuProp(skuJob.getString("spec")); // (50) DEFAULT NULL 'sku属性',
			goodRecord.setOnlineStatus(job.getJSONObject("item").getBoolean("buyable") == false ? "下架中" : "在售"); // (50)
																													// DEFAULT
			// NULL
			// '线上状态',sku是否在架上，0-下架中，1-架上
			goodRecord.setOperator(userName); // (50) DEFAULT NULL '操作人',
			goodRecord.setOperatTime(date); // '操作时间',
			goodRecord.setIsSecSale(0);// '是否二销',
//   无	  goodRecord.setMemo(memo);	//	(500) DEFAULT NULL 	 '备注',
			Integer id = goodRecordDao.selectByEcGoodIdAndSku(goodRecord);
			goodRecord.setId(id); // '商品主键id',

			goodRecordList.add(goodRecord);

		}

		if (pageSize * pageNo < totalItem) {
			xhsGoodRecord(storeSettings, pageNo + 1, pageSize, userName, date, goodRecordList);
		}

	}

	/**
	 * 小红书-訂單批量下载
	 * 
	 * @throws Exception
	 */
	@Transactional
	public String xhsDownloadPlatOrderLsit(String userId, int pageNo, int pageSize, String startDate, String endDate,
			String storeId, String status, Map<String, Integer> maps) throws Exception {

		boolean isSucess = true;

		String message = "";
		String resp = "";
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
//		// 信息
//		StoreRecord storeRecord = storeRecordDao.select(storeId);
		try {
			String method = "/ark/open_api/v0/packages";
			logger.info(startDate);
			Date date = sf.parse(startDate);
			long start = date.getTime() / 1000;
			date = sf.parse(endDate);
			long end = date.getTime() / 1000;

			Map<String, String> map = new HashMap<String, String>();
//		商品页数, 从第一页开始,默认为1
			map.put("page_no", pageNo + "");
//		分页大小, 默认50，最大值不超过100
			map.put("page_size", pageSize + "");
			map.put("start_time", start + "");
			map.put("end_time", end + "");
			map.put("status", status);
			map.put("time_type", "confirmed_at");

			/*
			 * logistics String 否 物流模式code，不传该字段则返回店铺默认物流模式下的订单, 以下两种情况会返回空值:
			 * 1.不传该值且店铺默认的物流模式不是该接口支持的则返回空值 2.传入的物流模式不是该接口支持的则返回空值 status String 否 订单状态,
			 * waiting待打包和打包中(APP端显示为待配货和配货中),shipped已发货,received收件人已签收。
			 * 若不传入该字段则默认返回waiting状态下订单 page_no Int 否 查询当前分页，从1开始计数 page_size Int 否 分页大小,
			 * 默认50，最大值不超过100 start_time Int 否
			 * 订单确认开始时间，Unix-Time时间戳，应与end_time同时赋值，根据传入time_type查询指定时间范围内的订单包裹，默认查询订单确认时间（
			 * confirm_time） end_time Int 否
			 * 订单确认结束时间，Unix-Time时间戳，应与start_time同时赋值，根据传入time_type查询指定时间范围内的订单包裹，默认查询订单确认时间
			 * （confirm_time） time_type Int 否
			 * 订单查询时间类型，订单创建时间：created_at，订单确认时间：confirmed_at，订单更新时间：updated_at
			 */
//		商品是否可售卖，true为在架上可售卖，false为已下架不可售卖
//		map.put("buyable", "true");

			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "下载错误";
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("下载错误：" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

				return resp;
			}

			JSONObject pddtotal = response.getJSONObject("data");
			int totalItem = pddtotal.getIntValue("total_page");// 总页数
			JSONArray jsonArray = pddtotal.getJSONArray("package_list");// 订单列表

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject job = jsonArray.getJSONObject(i);

				// 查询对应店铺是否存在此订单
				if (platOrderDao.checkExsits(job.getString("package_id"), storeId) == 0) {
					// 订单不存在

					// logger.info(job.toString());
					maps.put("downloadCount", maps.get("downloadCount") + 1);
					//
					xhsDownloadByPlatOrder(job.getString("package_id"), userId, storeId, false);
				} else {
					message = "订单已存在,订单号:" + job.getString("package_id") + ",店铺编号:" + storeId;
					isSucess = false;
					logger.error("订单下载:小红书平台订单已存在,订单号:{},店铺编号:{}", job.getString("package_id"), storeId);
				}

			}

			if (pageNo < totalItem) {
				logger.info(status + " 状态,第" + pageNo + "页,共" + totalItem + "条");
				resp = xhsDownloadPlatOrderLsit(userId, pageNo + 1, pageSize, startDate, endDate, storeId, status,
						maps);
			} else if (status.equals("waiting")) {
				resp = xhsDownloadPlatOrderLsit(userId, 1, pageSize, startDate, endDate, storeId, "shipped", maps);
			} else if (status.equals("shipped")) {
				resp = xhsDownloadPlatOrderLsit(userId, 1, pageSize, startDate, endDate, storeId, "received", maps);
			} else {

				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(userId);
				MisUser misUser = misUserDao.select(userId);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}

				logRecord.setOperatContent(
						"本次自动下载店铺：" + storeSettings.getStoreName() + "订单" + maps.get("downloadCount") + "条");
				logRecord.setOperatTime(sf.format(new Date()));
				logRecord.setOperatType(10);// 9手工下载 10 自动下载
				logRecord.setTypeName("自动下载");
				logRecordDao.insert(logRecord);
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess,
						"本次成功下载" + maps.get("downloadCount") + "条订单", null);
				return resp;

			}
		} catch (Exception e) {
			logger.error("URL：ec/platOrder/download 异常说明：", e);
			try {

				isSucess = false;
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		return resp;

	}

	/**
	 * 小红书-根据订单号下载订单
	 */
	@Transactional
	public String xhsDownloadByPlatOrder(String orderCode, String userId, String storeId, boolean isOrder)
			throws Exception {
		String method = "/ark/open_api/v0/packages/";
		String message = "";
		boolean isSucess = true;
		String resp = "";
		// 设置
		try {

			if (isOrder) {
				// 查询对应店铺是否存在此订单
				if (platOrderDao.checkExsits(orderCode, storeId) != 0) {
					// 订单不存在

					message = "订单已存在,订单号:" + orderCode + ",店铺编号:" + storeId;
					isSucess = false;
					logger.error("订单下载:小红书平台订单已存在,订单号:{},店铺编号:{}", orderCode, storeId);
					resp = BaseJson.returnResp("ec/platOrder/download ", false, message, null);
					return resp;
				}

			}

			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			// 信息
			StoreRecord storeRecord = storeRecordDao.select(storeId);

			Map<String, String> map = new HashMap<String, String>();
//			订单ID
			method = method + orderCode;
			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "下载错误";
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("下载错误：" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

				return resp;
			}

			JSONObject xhstotal = response.getJSONObject("data");

			String orderId = getOrderNo.getSeqNo("ec", userId);

			PlatOrder platOrder = new PlatOrder();

			platOrder.setEcOrderId(xhstotal.getString("package_id"));// 电商订单号
			platOrder.setOrderId(orderId); // 订单编号
			platOrder.setStoreId(storeId); // 店铺编号
			platOrder.setStoreName(storeRecord.getStoreName()); // '店铺名称' 1
			platOrder.setPayTime(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(xhstotal.getLong("pay_time") * 1000))); // 付款时间'
			// t
			// paytime
//				platOrder.setWaif(waif);		//	'旗标' 			
			platOrder.setIsAudit(0); // '是否客审'
//				platOrder.setAuditHint(auditHint); // '审核提示'
			// totalfee
			platOrder.setPayMoney(xhstotal.getBigDecimal("pay_amount")); // '实付金额' t
																			// payment
//		platOrder.setBuyerNote(trade.getBuyerMessage()); // 买家留言' t buyermessage
//		platOrder.setSellerNote(trade.getSellerMemo()); // 卖家备注'
			platOrder.setRecAddress(xhstotal.getString("receiver_address")); // 收货人详细地址' t receiveraddress
//		platOrder.setBuyerId(buyerId); // '买家会员号'
			platOrder.setRecName(xhstotal.getString("receiver_name")); // 收货人姓名' t receivername
			platOrder.setRecMobile(xhstotal.getString("receiver_phone")); // 收货人手机号' t receivermobile
			platOrder.setIsInvoice(0); // '是否开票'
//		platOrder.setInvoiceTitle(resultJsons.getString("invoice_name")); // '发票抬头'
//		platOrder.setNoteFlag(trade.getSellerFlag().intValue()); // 卖家备注旗帜' t sellerflag
			platOrder.setIsClose(0); // '是否关闭'
			platOrder.setAdjustMoney(new BigDecimal(0)); // 卖家调整金额'
			// discountfee
			platOrder.setOrderStatus(0); // '订单状态'
			platOrder.setReturnStatus(0); // '退货状态'
			// platOrder.setHasGift(0); // '是否含赠品'
			// platOrder.setMemo(memo); // '备注'
			// platOrder.setAdjustStatus(adjustStatus); // '调整标识'
			platOrder.setTradeDt(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(xhstotal.getLong("time") * 1000))); // '交易时间'
			// t
			// created
			platOrder.setSellTypId("1");// 设置销售类型普通销售
			platOrder.setBizTypId("2");// 设置业务类型2c
			platOrder.setRecvSendCateId("6");// 设置收发类别
//		订单状态，waiting待打包和打包中(APP端显示为待配货和配货中),shipped已发货,received收件人已签收
			if (xhstotal.getString("status").equals("waiting")) {
				platOrder.setIsShip(0); // '是否发货'
			} else {
				platOrder.setIsShip(1); // '是否发货'
				platOrder.setExpressNo(xhstotal.getString("express_no"));
			}
//		platOrder.setRecvSendCateId(recvSendCateId); // '收发类别编号'
			platOrder.setOrderSellerPrice(xhstotal.getBigDecimal("pay_amount")); // 结算金额（订单实际收入金额）'订单支付金额(包含运费)

			// receivedpayment
			platOrder.setProvince(xhstotal.getString("province")); // 省' t receiverstate
			// platOrder.setProvinceId(provinceId); // '省id'
			platOrder.setCity(xhstotal.getString("city")); // 市' t receivercity
			// platOrder.setCityId(cityId); //
			platOrder.setCounty(xhstotal.getString("district")); // 区' t receiverdistrict
			// platOrder.setCountyId(countyId); //
			// platOrder.setTown(town); // '镇'
			// platOrder.setTownId(townId); // '镇id'
			platOrder.setFreightPrice(xhstotal.getBigDecimal("shipping_fee")); // 运费' t

			platOrder.setVenderId("XHS"); // '商家id'

			// orders Order[]
			platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			// platOrder.setDeliveryType("任意时间");
			// //'送货（日期）类型（1-只工作日送货(双休日、假日不用送;2-只双休日、假日送货(工作日不用送;3-工作日、双休日与假日均可送货;其他值-返回“任意时间”
			// ）'

			List<PlatOrders> list = new ArrayList<PlatOrders>();
			List<CouponDetail> couponList = new ArrayList<>();
			JSONArray jsonArray = xhstotal.getJSONArray("item_list");
			BigDecimal goodMoney = BigDecimal.ZERO;
			BigDecimal discountMoney = BigDecimal.ZERO;
			BigDecimal payMoney = BigDecimal.ZERO;
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject order = jsonArray.getJSONObject(i);

				PlatOrders platOrders = new PlatOrders();
				platOrders.setGoodId(order.getString("item_id")); // 平台商品编号 商品数字ID
				platOrders.setGoodName(order.getString("item_name")); // '平台商品名称' t title String 山寨版测试机器 商品标题
				platOrders.setGoodNum(order.getInteger("qty")); // '商品数量' t num Number 1 购买数量。取值范围:大于零的整数
				platOrders.setGoodMoney(order.getBigDecimal("price").multiply(order.getBigDecimal("qty"))); // 商品金额'

				platOrders.setGoodPrice(order.getBigDecimal("price"));
				;// 商品单价
				platOrders.setPayPrice(order.getBigDecimal("pay_price").add(order.getBigDecimal("red_discount")));
				;// 实付单价
				goodMoney = goodMoney.add(platOrders.getGoodMoney());
				platOrders.setPayMoney((order.getBigDecimal("pay_price").add(order.getBigDecimal("red_discount")))
						.multiply(order.getBigDecimal("qty"))); // '实付金额'分摊之后的实付金额
				payMoney = payMoney.add(platOrders.getPayMoney());
				platOrders.setGoodSku(null); // '商品sku'
				platOrders.setOrderId(orderId); // '订单编号' 1
//				platOrders.setExpressCom(xhstotal.getString("express_company_code")); // 快递公司' t子订单发货的快递公司名称
//			platOrders.setBatchNo(batchNo);	//	'批号' 	
//			promotion_details		platOrders.setProActId(proActId);	//	促销活动编号' t promotionid	String	mjs	优惠id
				platOrders.setDiscountMoney(
						order.getBigDecimal("merchant_discount").multiply(order.getBigDecimal("qty"))); // 系统优惠金额
																										// 单件商品商家承担的优惠
				discountMoney = discountMoney
						.add(order.getBigDecimal("merchant_discount").multiply(order.getBigDecimal("qty")));
				platOrders.setAdjustMoney(BigDecimal.ZERO); // 买家调整金额'
//			platOrders.setMemo(order.getOid().toString()); // '备注' 该 子单id oid
				platOrders.setGoodPrice(order.getBigDecimal("price")); // '商品单价'
//			platOrders.setDeliverWhs(order.getStoreCode()); // 发货仓库编码'
				platOrders.setEcOrderId(xhstotal.getString("package_id")); // '平台订单号' t oid
//			 自主发货
				if (xhstotal.getString("logistics").equals("red_auto")) {
					platOrder.setDeliverSelf(1); // '订单是否自发货，0平台仓发货，1自发货' t

				} else {
					platOrder.setDeliverSelf(0); // '订单是否自发货，0平台仓发货，1自发货' t

				}
				platOrder.setDeliverWhs("");
				if (xhstotal.getString("type") != null && xhstotal.getString("type").equals("gift")) {

					platOrders.setIsGift(1);

				} else {
					platOrders.setIsGift(0);
				}
				list.add(platOrders);
//			优惠信息
				CouponDetail coupon1 = new CouponDetail();
				coupon1.setPlatId("XHS");// 平台id，京东JD，天猫JD
				coupon1.setStoreId(storeId);// 店铺id
				coupon1.setOrderId(xhstotal.getString("package_id"));// 订单号
				coupon1.setSkuId(null);
				coupon1.setCouponCode(null);// 优惠类型编码
				coupon1.setCouponType("小红书承担");// 优惠名称
				coupon1.setCouponPrice(order.getBigDecimal("red_discount").multiply(order.getBigDecimal("qty")));// 单件商品小红书承担的优惠
				CouponDetail coupon = new CouponDetail();
				coupon.setPlatId("XHS");// 平台id，京东JD，天猫JD
				coupon.setStoreId(storeId);// 店铺id
				coupon.setOrderId(xhstotal.getString("package_id"));// 订单号
				coupon.setSkuId(null);
				coupon.setCouponCode(null);// 优惠类型编码
				coupon.setCouponType("商家承担");// 优惠名称
				coupon.setCouponPrice(order.getBigDecimal("merchant_discount").multiply(order.getBigDecimal("qty")));// 单件商品小红书承担的优惠
				if (coupon1.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
					couponList.add(coupon1);
				}
				if (coupon.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
					couponList.add(coupon);
				}
			}
			if (couponList.size() > 0) {
				couponDetailDao.insert(couponList);
			}

			platOrder.setGoodNum(list.size()); // 商品品类数
			platOrder.setGoodMoney(goodMoney); // 商品金额' t
			platOrder.setDiscountMoney(discountMoney); // 系统优惠金额' 单件商品商家承担的优惠
			platOrder.setPayMoney(payMoney); // '实付金额' t

			platOrderDao.insert(platOrder);
			platOrdersDao.insert(list);

			// 执行自动匹配
			orderServiceImpl.autoMatch(platOrder.getOrderId(), userId);

			logger.info("订单下载成功" + xhstotal.getString("package_id"));
			resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess,
					"订单下载成功" + xhstotal.getString("package_id"), null);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnResp("ec/platOrder/download ", false, "订单下载，下载失败", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return resp;

	}

	/**
	 * 小红书-快递公司列表
	 * 
	 * @throws Exception
	 */
	@Transactional
	public String xhsExpressSettingsDao(String storeId) throws Exception {

		boolean isSucess = true;

		String message = "";
		String resp = "";
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
//		// 信息
//		StoreRecord storeRecord = storeRecordDao.select(storeId);
		try {

			String method = "/ark/open_api/v0/express_companies";

			Map<String, String> map = new HashMap<String, String>();

			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "下载错误";
				resp = BaseJson.returnRespObj("ec/platOrder/xhsExpressSettingsDao", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("下载错误：" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/platOrder/xhsExpressSettingsDao", isSucess, message, null);

				return resp;
			}
			ecExpressSettingsDao.deleteExpressCodeAndName();

			JSONArray jsonArray = response.getJSONArray("data");
			List<ExpressCodeAndName> list = new ArrayList<ExpressCodeAndName>();

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject job = jsonArray.getJSONObject(i);
				ExpressCodeAndName expressCodeAndName = new ExpressCodeAndName();

				logger.info(job.toString());
				expressCodeAndName.setExpressCode(job.getString("express_company_code"));
				expressCodeAndName.setExpressName(job.getString("express_company_name"));
				list.add(expressCodeAndName);
			}
			ecExpressSettingsDao.intotExpressCodeAndName(list);

		} catch (Exception e) {
			logger.error("URL：ec/platOrder/xhsExpressSettingsDao 异常说明：", e);
			try {
				isSucess = false;
				resp = BaseJson.returnRespObj("ec/platOrder/xhsExpressSettingsDao", isSucess, message, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		return resp;

	}

	/**
	 * 小红书-退货列表
	 */
	@Transactional
	public String xhsRefundPlatOrder(String userId, int pageNo, int pageSize, String startDate, String endDate,
			String storeId, int downloadCount) {
		String method = "/ark/open_api/v0/packages/canceling/list";
//		String method = "/ark/open_api/v0/packages";

		String message = "";
		boolean isSucess = true;
		String resp = "";
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		try {
			Date date = sf.parse(startDate);
			long start = date.getTime() / 1000;
			date = sf.parse(endDate);
			long end = date.getTime() / 1000;

			Map<String, String> map = new HashMap<String, String>();
//		订单ID
			map.put("status", "audited");
			map.put("page_no", pageNo + "");
			map.put("page_size", pageSize + "");
			map.put("start_time", start + "");
			map.put("end_time", end + "");

			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "下载错误";
				resp = BaseJson.returnRespObj("ec/refundOrder/download", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("下载错误：" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/refundOrder/download", isSucess, message, null);
				return resp;
			}

			JSONObject pddtotal = response.getJSONObject("data");
			int totalItem = pddtotal.getIntValue("total_page");// 总页数
			JSONArray jsonArray = pddtotal.getJSONArray("package_list");// 订单列表
			MisUser misUser = misUserDao.select(userId);
			List<RefundOrder> refundOrderList = new ArrayList<>();
			List<RefundOrders> refundOrdersList = new ArrayList<>();
			StoreRecord storeRecord = storeRecordDao.select(storeId);

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject job = jsonArray.getJSONObject(i);
				if (job.getString("audit_result").equals("canceled") && job.getString("status").equals("audited")) {
//				if (true) {
//				package_id

					if (platOrderDao.checkExsits(job.getString("package_id"), storeId) == 0) {
						// 原订单已删除,不存在
						logger.info("原订单已删除,不存在：" + job.getString("package_id"));

						continue;
					}
					if (refundOrderDao.selectEcRefId(job.getString("package_id")) == null) {
						// 判断退款单是否已存在
						logger.info("判断退款单已存在：" + job.getString("package_id"));

						continue;
					}
					RefundOrder refundOrder = new RefundOrder();

					// 店铺设置
					String refId = getOrderNo.getSeqNo("tk", userId);
					String ecOrderId = job.getString("package_id");
					refundOrder.setRefId(refId); // 退款单编号
					refundOrder.setEcId("XHS");
					refundOrder.setOrderId(job.getString("package_id")); // 订单编号
					refundOrder.setStoreId(storeId); // 店铺编号
					refundOrder.setEcOrderId(job.getString("package_id"));
					refundOrder.setStoreName(storeRecord.getStoreName()); // 店铺名称
					refundOrder.setEcRefId(job.getString("package_id")); // 电商退款单号
					refundOrder.setApplyDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date(job.getLong("cancel_time") * 1000))); // 申请日期
//				refundOrder.setBuyerId(refund.getBuyerOpenUid()); // 买家会员号
					refundOrder.setIsRefund(1); // 是否退货
//				refundOrder.setAllRefNum(refund.getNum().intValue()); // 整单退货数量
//				refundOrder.setAllRefMoney(new BigDecimal(refund.getRefundFee())); // 整单退款金额
					refundOrder.setRefReason(job.getString("cancel_reason")); // 退款原因
//				refundOrder.setRefExplain(refund.getDesc()); // 退款说明
					refundOrder.setRefStatus(1); // 退款状态
					refundOrder.setDownTime(sf.format(new Date())); // 下载时间
					refundOrder.setTreDate(sf.format(new Date())); // 处理日期
					refundOrder.setOperator(userId); // 操作员
					refundOrder.setIsAudit(0); // 是否审核
//			refundOrder.setMemo(memo); // 备注
					refundOrder.setSource(0);// 退款单来源设置0
					refundOrder.setOperatorId(misUser.getAccNum());
					refundOrder.setOperatorTime(sf.format(new Date()));

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
						logRecord.setOperatTime(sf.format(new Date()));
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
							logRecord.setOperatTime(sf.format(new Date()));
							logRecord.setOperatType(12);// 12退款下载
							logRecord.setTypeName("退款单下载");
							logRecord.setOperatOrder(ecOrderId);
							logRecordDao.insert(logRecord);
							continue;
						}
					}
					for (int i1 = 0; i1 < platOrders.size(); i1++) {
						// 直接取当前条明细生成退款单
						RefundOrders refundOrders = new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodSku(StringUtils.isEmpty(platOrders.get(i1).getGoodSku()) ? ""
								: platOrders.get(i1).getGoodSku());// 商品sku
						refundOrders.setEcGoodId(platOrders.get(i1).getGoodId());// 设置店铺商品编码
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i1).getInvId());

						if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));// 设置保质期
						}
						refundOrders.setIsGift(platOrders.get(i1).getIsGift());// 是否赠品用原订单属性
						refundOrders.setPrdcDt(platOrders.get(i1).getPrdcDt());// 设置生产日期
						refundOrders.setInvldtnDt(platOrders.get(i1).getInvldtnDt());// 设置失效日期
						refundOrders.setCanRefNum(platOrders.get(i1).getCanRefNum());// 设置可退数量
						refundOrders.setCanRefMoney(platOrders.get(i1).getCanRefMoney());// 可退金额
						refundOrders.setRefMoney(platOrders.get(i1).getPayMoney());// 退货金额

						refundOrders.setGoodId(platOrders.get(i1).getInvId());// 设置存货编码

						refundOrders.setGoodName(invtyDoc.getInvtyNm());// 设置存货名称
						refundOrders.setBatchNo(platOrders.get(i1).getBatchNo());// 设置退货批次
						refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());// 取对应店铺的默认退货仓
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
			if (pageNo < totalItem) {
				logger.info("第" + pageNo + "页,共" + totalItem + "条");
				xhsRefundPlatOrder(userId, pageNo + 1, pageSize, startDate, endDate, storeId, downloadCount);
			} else {

				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(userId);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(
						"退款单下载，本次成功下载小红书店铺：" + storeRecord.getStoreName() + ",退款单" + downloadCount + "条");
				logRecord.setOperatTime(sf.format(new Date()));
				logRecord.setOperatType(12);// 12退款下载
				logRecord.setTypeName("退款下载");
				logRecordDao.insert(logRecord);
				resp = BaseJson.returnResp("ec/refundOrder/download ", true,
						"退款单下载，本次成功下载小红书店铺：" + storeRecord.getStoreName() + ",退款单" + downloadCount + "条", null);

			}

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
}
