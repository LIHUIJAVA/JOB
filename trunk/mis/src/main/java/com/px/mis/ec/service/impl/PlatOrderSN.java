package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
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
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.ExpressCorpMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.suning.api.DefaultSuningClient;
import com.suning.api.entity.custom.DeliverynewAddRequest;
import com.suning.api.entity.custom.orderQueryQueryRequest;
import com.suning.api.entity.custom.orderQueryQueryResponse;
import com.suning.api.entity.custom.DeliverynewAddRequest.OrderLineNumbers;
import com.suning.api.entity.custom.DeliverynewAddResponse;
import com.suning.api.entity.custom.DeliverynewAddResponse.SendDetail;
import com.suning.api.entity.custom.orderGetGetRequest;
import com.suning.api.entity.custom.orderGetGetResponse;
import com.suning.api.entity.custom.orderGetGetResponse.OrderGet;
import com.suning.api.entity.custom.orderQueryQueryResponse.OrderQuery;
import com.suning.api.entity.custom.orderQueryQueryResponse.OrderDetail;
import com.suning.api.entity.custom.orderQueryQueryResponse.CouponList;
import com.suning.api.entity.custom.orderQueryQueryResponse.PaymentList;
import com.suning.api.entity.custom.orderSelfDistAddRequest.ProductCodes;
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
 * 订单-苏宁
 * @author lxya0
 *
 */
@Component
public class PlatOrderSN {
	
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
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Logger logger = LoggerFactory.getLogger(PlatOrderSN.class);
	
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String message = "";
	boolean isSuccess = true;
	/**
	 * 批量订单下载-苏宁	
	 * @throws Exception 
	 */
	@Transactional
	public String snDownload(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception {
		
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		List<OrderQuery> orderQueryList = new ArrayList<>();
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		orderQueryList = postSnOrder(storeSettings,storeRecord,pageNo,pageSize,startDate,endDate,orderQueryList);
		if(orderQueryList.size() > 0) {
			map = getPlatOrder(orderQueryList,storeId,accNum,storeRecord);	
			message = (String)map.get("message");
			isSuccess = (boolean)map.get("isSuccess");
		}else {
			message="本次自动下载店铺："+storeRecord.getStoreName()+"订单0条";
		}
		
		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
				
		return resp;
		
	}
	
	/**
	 * 单条订单下载-苏宁	
	 * @throws Exception 
	 */
	@Transactional
	public String snDownloadByOrderId(String accNum,String orderId, String startDate, String endDate,
			String storeId) throws Exception {
		
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		OrderGet orderGet = getSnOrder(storeSettings, storeRecord, orderId);
		
		
		map = getPlatOrder(orderGet,storeId,accNum,storeRecord);	
		message = (String)map.get("message");
		isSuccess = (boolean)map.get("isSuccess");
	
		
		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
				
		return resp;
		
	}
	

	/**
	 * 商品库下载-苏宁
	 * @return
	 */
	@Transactional
	public List<GoodRecord> getSNSku(String accNum, int pageNo, int pageSize, String date,
			StoreSettings storeSettings) throws Exception  {
	
		String storeId = storeSettings.getStoreId();
		List<GoodRecord> gRecordList = new ArrayList<>();
		List<ItemQuery> itemQueryList = new ArrayList<>();
		
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		itemQueryList = postSnProductCode(storeSettings,storeRecord,pageNo,pageSize,itemQueryList);
		
		if(itemQueryList.size() > 0) {
			gRecordList = getSNGoodRecord(itemQueryList,storeId,accNum,date,storeRecord,gRecordList);		
		} else {
			message = "下载苏宁商品库失败";
			isSuccess = false;
		}	
		return gRecordList;
		
	}
	
	
	/**
	 * 订单退货-苏宁
	 */
	@Transactional
	public String snRefund(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception  {
		
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		int successCount = 0;
		List<Rejected> orderQueryList = new ArrayList<>();
		
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		//post sn
		orderQueryList = postSnRefundOrder(storeSettings,storeRecord,pageNo,pageSize,startDate,endDate,orderQueryList);
		
		
		if(orderQueryList.size() > 0) {
				
			map = getRefundOrder(orderQueryList,storeId,accNum,storeRecord);
			/*message = (String)map.get("message");
			isSuccess = (boolean)map.get("isSuccess");*/
			logger.info("退款单下载:本次苏宁平台下载退款单共计下载:{}条",orderQueryList.size());
			successCount=Integer.parseInt(map.get("successCount").toString());
		}
		MisUser misUser = misUserDao.select(accNum);
		// 日志记录
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(accNum);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent("本次成功下载店铺："+storeRecord.getStoreName()+",退款单："+successCount+"条。");
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(12);// 12退款下载
		logRecord.setTypeName("退款单下载");
		logRecord.setOperatOrder(null);
		logRecordDao.insert(logRecord);
		
		resp = BaseJson.returnRespObj("ec/refundOrder/download", isSuccess, "本次成功下载退款单"+successCount+"条", null);
		return resp;
		
	}
	/**
	 * post苏宁-订单发货
	 * @param logistics 物流表信息
	 * @param express 快递公司信息
	 * @param storeSettings 店铺设置
	 * @throws SuningApiException 
	 */

	@Transactional(rollbackForClassName={"Exception"})
	public DeliverynewAddResponse SNOrderShip(LogisticsTab logistics, EcExpress express,StoreSettings storeSettings) throws SuningApiException {
		//根据快递公司编码查询快递公司代码	
		ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logistics.getExpressEncd());
		String companyCode = "";
		if(expressCorp != null) {
			List<ExpressCorp> corpList = expressCorpDao.selectExpressEncdName(expressCorp.getExpressEncd());
			if(corpList.size()  > 0) {
				
				companyCode = SnGetCommpayCode(corpList.get(0).getExpressNm(),storeSettings);
			}
		}
		
		
		DeliverynewAddRequest request = new DeliverynewAddRequest();
		request.setDeliveryTime(sf.format(new Date())); //发货时间	
		request.setExpressCompanyCode(companyCode); //物流公司编码----苏宁
		request.setExpressNo(logistics.getExpressCode()); //运单号
		request.setOrderCode(logistics.getEcOrderId()); //订单号
		//request.setOrderCode("37108911108");
		List<OrderLineNumbers> numbers = new ArrayList<>();
		
		/*
		 * OrderLineNumbers order = new OrderLineNumbers();
		 * order.setOrderLineNumber("");//订单行项目号 order.setProductCode(""); //苏宁商品编码
		 */
		  request.setOrderLineNumbers(numbers); 
		 //List<SendDetail> sendDetails = new ArrayList<SendDetail>(); 
		  List<PlatOrders> platOrders =platOrdersDao.selectListByOrderIdAndGroupByEcGoodId(logistics.getOrderId());
		  for (int i = 0; i < platOrders.size(); i++) { 
			  OrderLineNumbers orderLineNumbers = new OrderLineNumbers();
			  orderLineNumbers.setProductCode(platOrders.get(i).getGoodId());
			  numbers.add(orderLineNumbers); 
		  } 
		  request.setOrderLineNumbers(numbers);
		 
		//api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
		//request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		DeliverynewAddResponse result = client.excute(request);
	
		return result;
		 
		
	}
	/**
	 * get- 获取物流公司代码
	 * @param companyName 快递公司名称
	 * @param storeSettings 店铺设置
	 * @return
	 */
	private String SnGetCommpayCode(String companyName,StoreSettings storeSettings) {
		String companyCode = "";
		
	    LogisticcompanyGetRequest request = new LogisticcompanyGetRequest();
	   if(companyName.equals("圆通快递")) {
		   companyName="圆通速递";
	   }
		request.setCompanyName(companyName);
		//api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
		request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		try {
		 LogisticcompanyGetResponse response = client.excute(request);
		 if(response.getSnbody() != null) {
			 if(response.getSnbody().getLogisticCompany().size() > 0) {
				 companyCode = response.getSnbody().getLogisticCompany().get(0).getExpressCompanyCode();
				// System.out.println(companyCode);
			 }
		 }
		} catch (SuningApiException e) {
		 e.printStackTrace();
		}
		return companyCode;
	}


	/**
	 * post苏宁-批量获取订单
	 * @throws SuningApiException 
	 */
	private List<OrderQuery> postSnOrder(StoreSettings storeSettings, StoreRecord storeRecord, int pageNo, int pageSize,
			String startDate, String endDate, List<OrderQuery> orderQueryList) throws SuningApiException {
		
	
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();	
		orderQueryQueryRequest request = new orderQueryQueryRequest();
		
		request.setEndTime(endDate);
		//request.setOrderStatus("10"); //订单头状态（10：买家已付款，20：卖家已发货，21：部分发货，30：交易成功，40：交易关闭）
		request.setPageNo(pageNo);
		request.setPageSize(pageSize);
		request.setStartTime(startDate);//查询交易创建开始时间
		//request.setUserID("7017925352"); //会员编码
		//request.setUserName("123@qq.com"); //买家名称
		
		//api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
		//request.setCheckParam(true);
		
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		
		orderQueryQueryResponse response = client.excute(request);
		
		if(response.getSnerror() != null) {
			
			message = "订单下载:苏宁平台下载订单异常,异常信息:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("订单下载:苏宁平台下载订单异常,异常信息:{}",response.getSnerror().getErrorMsg());
		}
		
		
		if(response.getSnbody() != null) {
			orderQueryList.addAll(0,response.getSnbody().getOrderQuery());
					
		}
		if(response.getSnhead() != null && response.getSnhead().getTotalSize() != null) {
			if(Integer.valueOf(response.getSnhead().getTotalSize()) > pageNo * pageSize) {
				 postSnOrder(storeSettings,storeRecord,pageNo+1,pageSize,startDate,endDate,orderQueryList);
			}
		}
		return orderQueryList;
	}
	
	
	/**
	 * get苏宁-单条获取订单
	 * @throws SuningApiException 
	 */
	private OrderGet getSnOrder(StoreSettings storeSettings, StoreRecord storeRecord,String ecOrderId) throws SuningApiException {
		
	
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();	
		//orderQueryQueryRequest request = new orderQueryQueryRequest();
		orderGetGetRequest request = new orderGetGetRequest();
		request.setOrderCode(ecOrderId);//订单号
		//request.setEndTime(endDate);
		//request.setOrderStatus("10"); //订单头状态（10：买家已付款，20：卖家已发货，21：部分发货，30：交易成功，40：交易关闭）
		//request.setPageNo(pageNo);
		//request.setPageSize(pageSize);
		//request.setStartTime(startDate);//查询交易创建开始时间
		//request.setUserID("7017925352"); //会员编码
		//request.setUserName("123@qq.com"); //买家名称
		
		//api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
		request.setCheckParam(true);
		
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		
		orderGetGetResponse response = client.excute(request);
		
		if(response.getSnerror() != null) {
			
			message = "订单下载:苏宁平台下载订单异常,异常信息:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("订单下载:苏宁平台下载订单异常,异常信息:{}",response.getSnerror().getErrorMsg());
		}
		
		OrderGet orderGet = new OrderGet();
		if(response.getSnbody() != null) {
			//orderQueryList.addAll(0,response.getSnbody().getOrderGet());
			orderGet = response.getSnbody().getOrderGet();	
		}
		return orderGet;
	}
	
	/**
	 * post苏宁-批量获取退款单
	 */
	private List<Rejected> postSnRefundOrder(StoreSettings storeSettings,StoreRecord storeRecord,
			int pageNo, int pageSize, String startDate, String endDate,List<Rejected> reList) throws SuningApiException {
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		
		//退货单
		BatchrejectedQueryRequest request = new BatchrejectedQueryRequest();
		request.setStartTime(startDate);
		request.setEndTime(endDate);
		request.setPageNo(pageNo);
		request.setPageSize(pageSize);
		
		//api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
		request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		
		BatchrejectedQueryResponse response = client.excute(request);
		if(response.getSnerror() != null) {
			
			message = "退款单下载:苏宁平台下载退款单异常,异常信息:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("退款单下载:苏宁平台下载退款单异常,异常信息:{}",response.getSnerror().getErrorMsg());
		}
		if(response.getSnbody() != null) {
			
			reList.addAll(0,response.getSnbody().getRejects());	
					
		}
		
		if(response.getSnhead() != null && response.getSnhead().getTotalSize() != null) {
			if(Integer.valueOf(response.getSnhead().getTotalSize()) > pageNo * pageSize) {
				
				postSnRefundOrder(storeSettings,storeRecord,pageNo+1,pageSize,startDate,endDate,reList);
			}
		}
		return reList;
	}
	/**
	 * post苏宁-批量获取商品库
	 * @throws SuningApiException 
	 */
	private List<ItemQuery> postSnProductCode(StoreSettings storeSettings, StoreRecord storeRecord, int pageNo,
			int pageSize,List<ItemQuery> itemQueryList) throws SuningApiException {
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		
		ItemQueryRequest request = new ItemQueryRequest();
		request.setCategoryCode("");
		request.setBrandCode("");
		request.setStatus("2");
		//request.setStartTime(startDate);
		//request.setEndTime(endDate);
		request.setPageNo(pageNo);
		request.setPageSize(pageSize);	
		//api入参校验逻辑开关，当测试稳定之后建议设置为 false 或者删除该行
		request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");	
		ItemQueryResponse response = client.excute(request);
		if(response.getSnerror() != null) {
			
			message = "商品库下载:苏宁平台下载商品库异常,异常信息:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("商品库下载:苏宁平台下载商品库异常,异常信息:{}",response.getSnerror().getErrorMsg());
		}
		if(response.getSnbody() != null) {
			
			itemQueryList.addAll(0,response.getSnbody().getItemQueries());	
					
		}
		
		if(response.getSnhead() != null && response.getSnhead().getTotalSize() != null) {
			if(Integer.valueOf(response.getSnhead().getTotalSize()) > pageNo * pageSize) {
				
				postSnProductCode(storeSettings,storeRecord,pageNo+1,pageSize,itemQueryList);
			}
		}
		return itemQueryList;
	}

	/**
	 * 获取商品库信息
	 */
	private List<GoodRecord> getSNGoodRecord(List<ItemQuery> itemQueryList, String storeId, String accNum,String date,
			StoreRecord storeRecord,List<GoodRecord> gRecordList) {
		
		
		if(itemQueryList.size() > 0) {
			
			for(ItemQuery itemQuery : itemQueryList) {
				GoodRecord gRecord = new GoodRecord();
				
				gRecord.setEcGoodName(itemQuery.getProductName()); //苏宁商品名称
				gRecord.setEcGoodId(itemQuery.getProductCode()); //苏宁商品编码
				gRecord.setGoodId(itemQuery.getItemCode()); //商家主商品编码
				gRecord.setStoreId(storeId);
				gRecord.setEcId("SN");
				gRecord.setStoreId(storeRecord.getStoreId());
				gRecord.setEcName("苏宁易购");
				
				//gRecord.setOnlineStatus(skuJob.getIntValue("is_sku_onsale") == 0 ? "下架中" : "在售");
				
				Integer id = goodRecordDao.selectByEcGoodIdAndSku(gRecord);
				gRecord.setId(id);
				
				//itemQuery.getCategoryName(); //苏宁商品采购目录名称
				//itemQuery.getCategoryCode(); //苏宁商品采购目录编码
				//itemQuery.getBrandName(); //苏宁商品品牌名称。
				//itemQuery.getBrandCode(); //苏宁商品品牌编码。
				//itemQuery.getStatus(); //处理状态。1：正在处理；2：处理成功；3：处理失败；4：审核不通过。
				//itemQuery.getPublished(); //卖家商品第一次提交时间
				//itemQuery.getImg1Url(); //商家商品图片1地址url
				//itemQuery.getEditTime(); //最后修改时间
				//itemQuery.getProductName(); //子商品的商品名称
				//itemQuery.getProductCode(); //子商品的商品编码
				//itemQuery.getItemCode(); //子商品的商家商品编码。如无子商品，则不展示此字段。
				//itemQuery.getCmTitle(); //商品标题
				
					
				gRecord.setIsSecSale(0);
				
				gRecord.setSafeInv("0");
				gRecord.setOperatTime(date);
				gRecord.setOperator(accNum);
				
				gRecordList.add(gRecord);
			}
		} else {
			//数据为空
			message = "下载商品库:未获取到苏宁商品库信息";
			logger.error("下载商品库:未获取到苏宁商品库信息");
		}
		return gRecordList;
	}
	
	/**
	 * 退款单-订单
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	private Map<String,Object> getRefundOrder(List<Rejected> rejectedList,
											  String storeId, String accNum,
											  StoreRecord storeRecord) throws IllegalAccessException, InvocationTargetException, ParseException {
		
	
		Map<String,Object> map = new HashMap<>();
		int successCount=0;
		if(rejectedList.size() > 0) {
			
			for(Rejected rejected : rejectedList) {
				//System.out.println("AAAAAAA状态"+rejected.getStatusDesc());
				//refundtype 订单状态。 1-退款待处理； 2-已拒绝退款 ； 3-待买家发货 ； 4-待商家收货 ； 5-退款失败； 6-退款处理中 ； 7-退款关闭 ； 8-退款成功；C030-申请退货
				if(rejected.getStatusDesc().equals("退款待处理")||rejected.getStatusDesc().equals("已拒绝退款")||rejected.getStatusDesc().equals("退款失败")||
						rejected.getStatusDesc().equals("退款关闭")||rejected.getStatusDesc().equals("申请退货")) {
					//这几类情况下不下载对应退款单
					continue;
				}
				
				//查询平台订单
				List<PlatOrder> order = platOrderDao.selectPlatOrdersByEcOrderId(rejected.getOrderCode());
				if (order.size()>0) {
					String ecRefId = rejected.getOrderCode()+"-"+rejected.getProductCode()+"-"+rejected.getReturntype();//电商退款单号
					RefundOrder refundOrder = refundOrderDao.selectEcRefId(ecRefId);
					if(refundOrder == null) {//returntype 2表示货未发、3表示货已发未收、4表示货已收
						
						refundOrder = new RefundOrder();
						String refId = getOrderNo.getSeqNo("tk", accNum);
						refundOrder.setRefId(refId);
						refundOrder.setExpressCode(rejected.getExpressNo()); // 快递单号
						refundOrder.setEcOrderId(rejected.getOrderCode()); //B2C订单号
						refundOrder.setOrderId(order.get(0).getOrderId());
						refundOrder.setEcId("SN");
						refundOrder.setEcRefId(ecRefId);//电商退款单号
						refundOrder.setTreDate(rejected.getStatusPassTime());//处理时间
						refundOrder.setStoreId(storeRecord.getStoreId());
						refundOrder.setStoreName(storeRecord.getStoreName());
						//refundOrder.setEcRefId(rejected.getItemNo()); //订单行号
						refundOrder.setApplyDate(rejected.getApplyTime()); //申请时间
						refundOrder.setRefExplain(rejected.getStatusDesc()); //退款说明   订单状态。 1-退款待处理； 2-已拒绝退款 ； 3-待买家发货 ； 4-待商家收货 ； 5-退款失败； 6-退款处理中 ； 7-退款关闭 ； 8-退款成功；C030-申请退货
						refundOrder.setDownTime(sf.format(new Date()));
						refundOrder.setIsAudit(0); //是否审核
						refundOrder.setSource(0);				
						refundOrder.setRefReason(rejected.getReturnReason()); //退货原因
						refundOrder.setAllRefMoney(new BigDecimal(rejected.getReturnMoney()));//实际退款金额
						refundOrder.setSource(0);
						if(StringUtils.isEmpty(rejected.getRefundtype())) {
							refundOrder.setIsRefund(1);
							int count =0;
							for (int i = 0; i < order.size(); i++) {
								count+=order.get(i).getGoodNum();
							}
							refundOrder.setAllRefNum(count);
						} else {
							refundOrder.setIsRefund(0);
							refundOrder.setAllRefNum(0);//仅退款情况下
						}
						List<RefundOrders> ordersList = new ArrayList<>();
						
						RefundOrders orders = new RefundOrders();
						orders.setRefId(refId);
						orders.setEcGoodId(rejected.getProductCode()); //苏宁商品编码
						
						
						//rejected.getCustomer(); //买家帐号
						
						//rejected.getDealMoney(); //交易金额
						
						
						orders.setRefMoney(new BigDecimal(rejected.getDealMoney())); //退款金额 取平台交易金额
						
						//rejected.getStatusPassTime(); //倒计时时间,格式：yyyy-MM-dd HH:mm：ss
										
						rejected.getExpressCompanyCode(); //物流公司代码
						rejected.getExpressNo(); //物流公司运单号
						rejected.getReturntype(); //2表示货未发、3表示货已发未收、4表示货已收
						
						ordersList.add(orders);
						refundOrder.setRefundOrders(ordersList);
						refundOrder = checkRefundIsPtoSN(refundOrder,accNum,rejected.getReturntype());
										
						if (refundOrder.getRefundOrders().size()>0) {
							//insert
							refundOrderDao.insert(refundOrder);
							refundOrdersDao.insertList(refundOrder.getRefundOrders());
							successCount++;
						}
						
						
					} 	
					
				} 
							
			}
		} 
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		map.put("successCount", successCount);
		return map;
	}
	/**
	 * 拼多多退货单-拆单
	 */
	public RefundOrder checkRefundIsPto(RefundOrder refundOrder,String accNum) throws IllegalAccessException, InvocationTargetException {
		
		List<RefundOrders> refundOrdersList = refundOrder.getRefundOrders();
		List<RefundOrders> refundsList = new ArrayList<>();
		boolean flag = true;
		MisUser misUser = misUserDao.select(accNum);
		refundOrder.setOperator(misUser.getUserName());
		refundOrder.setOperatorId(accNum);
		refundOrder.setOperatorTime(sf.format(new Date()));
		if(refundOrdersList.size() > 0) {
			for(RefundOrders refundOrders : refundOrdersList) {
			
				Map<String,String> map=new HashMap<>();
				BigDecimal allPayMoney = BigDecimal.ZERO;//明细的实付金额总和
				//System.out.println(aftermarket.getOrderId());
				map.put("ecOrderId", refundOrder.getEcOrderId());
				map.put("goodSku", refundOrders.getGoodSku());
				//用单中的订单号和skuid去查询原单的购买数量以及实付金额
				List<PlatOrders> platOrders = platOrderDao.selectNumAndPayMoneyByOrderAndSKu(map);
				
				//根据本地订单是否发货状态判断退款单的退货仓库
				String reWhsCode="";
				PlatOrder platOrder = platOrderDao.selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId()).get(0);
				if(platOrder.getIsShip()==0) {
					//未发货
					reWhsCode=platOrder.getDeliverWhs();
				}else {
					//已发货
					StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
					reWhsCode=storeRecord.getDefaultRefWhs();//取对应店铺的默认退货仓
				}
				
				if (platOrders.size()==0) {
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("退款单下载，订单："+refundOrder.getEcOrderId()+"未找到订单明细，本次下载失败。");
					logRecord.setOperatTime(sf.format(new Date()));
					logRecord.setOperatType(12);// 12退款下载
					logRecord.setTypeName("退款单下载");
					logRecord.setOperatOrder(refundOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
				}else {
					
					for (int i = 0; i < platOrders.size(); i++) {
						//循环判断原订单是否存在未匹配到存货档案的记录
						if(!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
							flag=false;
							break;
						}
						allPayMoney=allPayMoney.add(platOrders.get(i).getPayMoney());
						
					}
					if(!flag) {
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载，订单："+refundOrder.getEcOrderId()+"存在未匹配到存货档案的记录，本次下载失败。");
						logRecord.setOperatTime(sf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(refundOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
					}
				}
				
			
				int allReturnCount=0;
				BigDecimal allReturnMoney=BigDecimal.ZERO;
				for (int i = 0; i < platOrders.size(); i++) {
					//直接取当前条明细生成退款单
					RefundOrders refundOrders1 = new RefundOrders();
					refundOrders1.setRefId(refundOrders.getRefId());
					refundOrders1.setGoodSku(platOrders.get(i).getGoodSku());//商品sku
					refundOrders1.setEcGoodId(platOrders.get(i).getGoodId());//设置店铺商品编码
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
					refundOrders1.setCanRefNum(platOrders.get(i).getCanRefNum());//设置可退数量
					//退货数量
					if(refundOrders.getRefNum()>0) {
						//设置退货数量
						int refNum = platOrders.get(i).getInvNum()*refundOrders.getRefNum()/platOrders.get(i).getGoodNum();
						refundOrders1.setRefNum(refNum);
						allReturnCount=allReturnCount+refNum;
					}else {
						refundOrders1.setRefNum(0);
					}
					if(StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
						refundOrders1.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//设置保质期
					}
					refundOrders1.setIsGift(platOrders.get(i).getIsGift());//是否赠品用原订单属性
					refundOrders1.setPrdcDt(platOrders.get(i).getPrdcDt());//设置生产日期
					refundOrders1.setInvldtnDt(platOrders.get(i).getInvldtnDt());//设置失效日期
					if (i+1<platOrders.size()) {
						//设置金额
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						if (platOrders.get(i).getPayMoney().compareTo(BigDecimal.ZERO)>0) {
							BigDecimal refMoney = (refundOrders.getRefMoney().multiply(platOrders.get(i).getPayMoney()))
									.divide(allPayMoney,2,RoundingMode.HALF_DOWN);//需要退的金额*对应实付÷所有实付
							refundOrders1.setRefMoney(refMoney);
							allReturnMoney=allReturnMoney.add(refMoney);
						}else {
							//实付金额等于0
							refundOrders1.setCanRefMoney(BigDecimal.ZERO);
							refundOrders1.setRefMoney(BigDecimal.ZERO);
						}
					}else {
						//最后一条用减法
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						refundOrders1.setRefMoney(refundOrders.getRefMoney().subtract(allReturnMoney));
					}
					System.out.println("批次:"+platOrders.get(i).getBatchNo()+"生产日期："+platOrders.get(i).getPrdcDt()+"失效日期："+platOrders.get(i).getInvldtnDt()+"保质期天数："+invtyDoc.getBaoZhiQiDt());
					allReturnMoney=allReturnMoney.add(refundOrders.getRefMoney());
					refundOrders1.setGoodId(platOrders.get(i).getInvId());//设置存货编码
					
					refundOrders1.setGoodName(invtyDoc.getInvtyNm());//设置存货名称
					refundOrders1.setBatchNo(platOrders.get(i).getBatchNo());//设置退货批次
					refundOrders1.setRefWhs(reWhsCode);//设置退货仓库编码
					refundsList.add(refundOrders1);//装入list

					refundOrder.setOrderId(platOrders.get(i).getOrderId());
				}
				refundOrder.setRefStatus(0);//退款状态暂时先设置成0,不知道有啥用
				refundOrder.setAllRefNum(allReturnCount);
				refundOrder.setAllRefMoney(allReturnMoney);
				if(refundOrder.getAllRefNum()>0) {
					refundOrder.setIsRefund(1);//退货数量大于0时是否退货1
				}else {
					refundOrder.setIsRefund(0);
				}
				
			}
		
		}
		refundOrder.setRefundOrders(refundsList);//返回list。size等于0则说明下载失败，不需要保存
		return refundOrder;
	}
	
	/**
	 * 苏宁退货单-拆单
	 */
	public RefundOrder checkRefundIsPtoSN(RefundOrder refundOrder,String accNum,String returnType) throws IllegalAccessException, InvocationTargetException {
		
		List<RefundOrders> refundOrdersList = refundOrder.getRefundOrders();
		List<RefundOrders> refundsList = new ArrayList<>();
		boolean flag = true;
		MisUser misUser = misUserDao.select(accNum);
		refundOrder.setOperator(misUser.getUserName());
		refundOrder.setOperatorId(accNum);
		refundOrder.setOperatorTime(sf.format(new Date()));
		if(refundOrdersList.size() > 0) {
			for(RefundOrders refundOrders : refundOrdersList) {
			
				Map<String,String> map=new HashMap<>();
				BigDecimal allPayMoney = BigDecimal.ZERO;//明细的实付金额总和
				//System.out.println(aftermarket.getOrderId());
				map.put("ecOrderId", refundOrder.getEcOrderId());
				map.put("goodId", refundOrders.getEcGoodId());
				//用单中的订单号和goodId去查询原单的购买数量以及实付金额
				List<PlatOrders> platOrders = platOrderDao.selectNumAndPayMoneyByOrderAndSKuSN(map);
				
				//根据退款单状态判断退款单的退货仓库
				String reWhsCode="";
				if(refundOrder.getIsRefund()==1) {
					//当退款单是否退款为1时，需要退货
					//当订单为未签收情况下的退货时，退回仓库为原订单发货仓库，货已签收后退回仓库为店铺设置的默认退货仓
					if (returnType.equals("4")) {//货已收
						StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
						reWhsCode = storeRecord.getDefaultRefWhs();//店铺默认退货仓
					}else {
						PlatOrder platOrder = platOrderDao.selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId()).get(0);
						reWhsCode=platOrder.getDeliverWhs();//原单发货仓库
					}
				}else {
					//不需要退货（仅退款）时，退货仓库为原单发货仓库
					PlatOrder platOrder = platOrderDao.selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId()).get(0);
					reWhsCode=platOrder.getDeliverWhs();//原单发货仓库
				}
				
				
				
				if (platOrders.size()==0) {
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("退款单下载，订单："+refundOrder.getEcOrderId()+"未找到订单明细，本次下载失败。");
					logRecord.setOperatTime(sf.format(new Date()));
					logRecord.setOperatType(12);// 12退款下载
					logRecord.setTypeName("退款单下载");
					logRecord.setOperatOrder(refundOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
				}else {
					
					for (int i = 0; i < platOrders.size(); i++) {
						//循环判断原订单是否存在未匹配到存货档案的记录
						if(!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
							flag=false;
							break;
						}
						allPayMoney=allPayMoney.add(platOrders.get(i).getPayMoney());
						
					}
					if(!flag) {
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载，订单："+refundOrder.getEcOrderId()+"存在未匹配到存货档案的记录，本次下载失败。");
						logRecord.setOperatTime(sf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(refundOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
					}
				}
				
			
				int allReturnCount=0;
				BigDecimal allReturnMoney=BigDecimal.ZERO;
				for (int i = 0; i < platOrders.size(); i++) {
					//直接取当前条明细生成退款单
					RefundOrders refundOrders1 = new RefundOrders();
					refundOrders1.setRefId(refundOrders.getRefId());
					refundOrders1.setGoodSku(platOrders.get(i).getGoodSku());//商品sku
					refundOrders1.setEcGoodId(platOrders.get(i).getGoodId());//设置店铺商品编码
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
					refundOrders1.setCanRefNum(platOrders.get(i).getCanRefNum());//设置可退数量
					//退货数量
					if(refundOrder.getIsRefund()==1) {//当退款单需要退货时
						//设置退货数量
						int refNum = platOrders.get(i).getInvNum();
						refundOrders1.setRefNum(refNum);
						allReturnCount=allReturnCount+refNum;
					}else {
						refundOrders1.setRefNum(0);
					}
					if(StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
						refundOrders1.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//设置保质期
					}
					refundOrders1.setIsGift(platOrders.get(i).getIsGift());//是否赠品用原订单属性
					refundOrders1.setPrdcDt(platOrders.get(i).getPrdcDt());//设置生产日期
					refundOrders1.setInvldtnDt(platOrders.get(i).getInvldtnDt());//设置失效日期
					if (i+1<platOrders.size()) {
						//设置金额
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						if (platOrders.get(i).getPayMoney().compareTo(BigDecimal.ZERO)>0) {
							BigDecimal refMoney = (refundOrders.getRefMoney().multiply(platOrders.get(i).getPayMoney()))
									.divide(allPayMoney,2,RoundingMode.HALF_DOWN);//需要退的金额*对应实付÷所有实付
							refundOrders1.setRefMoney(refMoney);
							allReturnMoney=allReturnMoney.add(refMoney);
						}else {
							//实付金额等于0
							refundOrders1.setCanRefMoney(BigDecimal.ZERO);
							refundOrders1.setRefMoney(BigDecimal.ZERO);
						}
					}else {
						//最后一条用减法
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						refundOrders1.setRefMoney(refundOrders.getRefMoney().subtract(allReturnMoney));
					}
					//System.out.println("批次:"+platOrders.get(i).getBatchNo()+"生产日期："+platOrders.get(i).getPrdcDt()+"失效日期："+platOrders.get(i).getInvldtnDt()+"保质期天数："+invtyDoc.getBaoZhiQiDt());
					allReturnMoney=allReturnMoney.add(refundOrders.getRefMoney());
					refundOrders1.setGoodId(platOrders.get(i).getInvId());//设置存货编码
					
					refundOrders1.setGoodName(invtyDoc.getInvtyNm());//设置存货名称
					refundOrders1.setBatchNo(platOrders.get(i).getBatchNo());//设置退货批次
					refundOrders1.setRefWhs(reWhsCode);//设置退货仓库编码
					refundsList.add(refundOrders1);//装入list

					refundOrder.setOrderId(platOrders.get(i).getOrderId());
				}
				refundOrder.setRefStatus(0);//退款状态暂时先设置成0,不知道有啥用
				refundOrder.setAllRefNum(allReturnCount);
				refundOrder.setAllRefMoney(allReturnMoney);
				if(refundOrder.getAllRefNum()>0) {
					refundOrder.setIsRefund(1);//退货数量大于0时是否退货1
				}else {
					refundOrder.setIsRefund(0);
				}
				
			}
		
		}
		refundOrder.setRefundOrders(refundsList);//返回list。size等于0则说明下载失败，不需要保存
		return refundOrder;
	}
	/**
	 * 设置退货单可退金额,数量
	 * @param refundOrder
	 * @return
	 */
	private RefundOrder setCanRefValue(RefundOrder refundOrder) {
		List<RefundOrders> refundOrdersList = refundOrder.getRefundOrders();
		List<RefundOrders> refundsList = new ArrayList<>();
		if(refundOrdersList.size() > 0) {
			//查询原单
			List<PlatOrders> platOrdersList = platOrdersDao.selectByEcOrderId(refundOrder.getEcGoodId());
			if(platOrdersList.size() > 0) {
				Map<String,Object> maps = new HashMap<>();
				for(PlatOrders platOrders : platOrdersList) {
					maps.put(platOrders.getInvId(),platOrders);
				}
				for(RefundOrders refundOrders : refundOrdersList) {
					if(maps.containsKey(refundOrders.getGoodId())) {
						PlatOrders platOrders = (PlatOrders)maps.get(refundOrders.getGoodId());
						refundOrders.setCanRefMoney(platOrders.getPayMoney());
						refundOrders.setCanRefNum(platOrders.getGoodNum());
						refundsList.add(refundOrders);
					}
				}
				refundOrder.setRefundOrders(refundsList);
			} 
		}
		return refundOrder;
	}


	/**
	 * 订单详情获取-苏宁
	 * @return
	 * @throws CloneNotSupportedException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Transactional
	private Map<String,Object> getPlatOrder(List<OrderQuery> orderQueryList,
										 String storeId,String userId,
										 StoreRecord storeRecord) throws CloneNotSupportedException, IOException, ParseException {

		boolean isSuccess = true;
		String resp = "";
		Map<String,Object> map = new HashMap<>();
		DecimalFormat dFormat = new DecimalFormat("0.00");
		int downloadCount = 0;
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		if(orderQueryList.size() > 0) {
			for(OrderQuery query : orderQueryList) {
				System.out.println(query.getOrderTotalStatus());
				if(query.getOrderTotalStatus().equals("10")||query.getOrderTotalStatus().equals("15")||query.getOrderTotalStatus().equals("20")||
						query.getOrderTotalStatus().equals("30")||query.getOrderTotalStatus().contentEquals("21")) {
					//订单总状态，0 订单取消；5 未付款；10 买家已付款；15 发货中；20 已发货；21 部分发货；30 交易成功；40 交易关闭
				
				
				
				if (platOrderDao.checkExsits(query.getOrderCode(), storeId) == 0) {
					
					PlatOrder order = new PlatOrder();
					List<PlatOrders> ordersList = new ArrayList<>(); //商品				
					List<InvoiceInfo> invoiceList = new ArrayList<>(); //发票
					List<CouponDetail> couponsList = new ArrayList<>(); //优惠			
					List<PlatOrderPaymethod> payMentList = new ArrayList<>(); //支付
					BigDecimal goodMoney = new BigDecimal(0.00);
					BigDecimal discountMoney = new BigDecimal(0.00);
					BigDecimal adjustMoney = new BigDecimal(0.00);
					
					String orderId  = getOrderNo.getSeqNo("ec", userId);
					order.setDownloadTime(simpleDateFormat.format(new Date()));
					order.setOrderId(orderId); //px-订单编号
					order.setStoreId(storeId);
					order.setStoreName(storeRecord.getStoreName());
					
					//query.getAccount(); //开户账号
					//query.getBank();//开户行
					order.setBuyerNote(query.getBuyerOrdRemark());//订单备注 -买家
					order.setCityId(query.getCityCode());//城市代码
					order.setCity(query.getCityName());//市
					order.setRecAddress(query.getCustomerAddress());//详细地址
					
					//query.getCustomerName();//顾客姓名
					//order.setIsShip(0);//是否发货为0
					order.setHasGift(0);//是否含赠品
					order.setAdjustStatus(0);//调整状态设置为0
					order.setTradeDt(query.getOrderSaleTime());//下单时间 下单时间用支付时间 接口没找到下单时间
					order.setBizTypId("2");
					order.setSellTypId("1");
					order.setRecvSendCateId("6");
					order.setCountyId(query.getDistrictCode());//区域代码
					order.setCounty(query.getDistrictName());//区
					
					//query.getEvaluationMark();//评价状态 0未评价 1已评价
					
					//获取发票信息					
					if(query.getNeedinvoiceflag().equals("Y")) {
						
						invoiceList = getInvoice(query,storeId,orderId,invoiceList,order);
		
					} else {
						order.setIsInvoice(0);
					}
					
				
					//query.getInvoiceRecipientAddress();//发票收件人地址					
					//query.getInvoiceRecipientPhone(); //发票收件人电话
					//query.getRegisterAddress(); //注册地址
					//query.getRegisterPhone(); //注册电话
					
					//query.getMobNum(); //买家联系电话
				
					order.setEcOrderId(query.getOrderCode()); //B2C订单号
					
					order.setPayTime(query.getOrderSaleTime()); //订单支付时间,格式：yyyy-MM-dd HH:mm:ss
					//order.setOrderStatus(Integer.valueOf(query.getOrderTotalStatus())); //订单总状态
					//order.setOrderStatus(0);//订单状态
					
					//query.getPayType(); //支付方式，如礼品卡支付
					
					order.setProvinceId(query.getProvinceCode()); //省份代码
					order.setProvince(query.getProvinceName()); //收件人地址（省）中文
					
					order.setSellerNote(query.getSellerOrdRemark()); //订单备注（卖家）--商家对订单的备注信息
					order.setBuyerId(query.getUserName()); //买家帐号
					order.setSellTypId("1");// 设置销售类型普通销售
					order.setBizTypId("2");// 设置业务类型2c	
					order.setRecName(query.getCustomerName());//收货人姓名
					order.setOrderStatus(0); //订单状态
					if (query.getOrderTotalStatus().equals("10")) {//未发货
						order.setIsShip(0);
					}else {
						order.setIsShip(1);
						String expressNo = "";
						for (int i = 0; i < query.getOrderDetail().size(); i++) {
							if(query.getOrderDetail().get(i).getExpressno().length()>0) {
								expressNo=query.getOrderDetail().get(i).getExpressno();
								break;
							}
							
						}
						order.setExpressNo(expressNo);//快递单号
					}
					order.setReturnStatus(0); //退货状态
					order.setIsClose(0); // 是否关闭
					order.setIsAudit(0); // 是否客审
					order.setVenderId("SN"); // '商家id'
					order.setRecMobile(query.getMobNum());//电话
					List<OrderDetail> orderDetailList = query.getOrderDetail();
					int goodNum = 0;
					if(orderDetailList.size() > 0) {
						for(OrderDetail detail : orderDetailList) {
							PlatOrders orders = new PlatOrders();
							orders.setOrderId(orderId);
							orders.setEcOrderId(order.getEcOrderId());
		
							
							//detail.getActivitytype(); //活动类型（02：渠道专享订单，03：免费试用订单，04：付邮试用订单，05,06：预定订单，07：秒杀订单，08：S码活动订单，09：赠品）
							//detail.getbLineNumber(); //b2c行项目号
							//detail.getCarShopAddr(); //	汽车服务门店地址
							//detail.getCarShopCode(); //汽车服务门店编码
							//detail.getCarShopName(); //汽车服务门店名称
							//detail.getCarShopSerWay(); //汽车服务方式（1-上门服务，2-到店服务）
							//detail.getCarShopTel(); //汽车服务门店电话
							
							//detail.getDeclareGoodsAmount(); //申报订单行货款
							//detail.getDeclareItemPrice(); //申报单价
							//detail.getDeclareItemTaxfare(); //申报税费
							
							//detail.getDisType(); //发货方式。01 代表海外直邮发货；02代表商家保税区发货；03代表苏宁保税区发货;空代表国内海外购
			
							
							//detail.getExpresscompanycode(); //物流公司编码
							//detail.getExpressno(); //运单号
							
							//4PS发货标示,1代表4PS发货、0代表C店发货
							if(Integer.valueOf(detail.getFpsdeliveryflag()) == 0) {
								order.setDeliverSelf(1); //自发货
							}else{
								order.setDeliverSelf(0);
							}; 
							
							//detail.getHwgFlag(); //海外购标识。01表示海外购订单，其他值为非海外购订单
							
							orders.setDeliverWhs(detail.getInvCode()); //仓库编码
							
							//detail.getIsProsupplierDelivery(); //是否供应商发货标识，1代表是
							
							//detail.getItemCode(); //商家商品编码
							orders.setGoodId(detail.getProductCode()); // 平台商品编号 苏宁平台编码
							
							//detail.getItemTaxFare(); //税费
							//detail.getMode(); //通关模式(01:保税备货,02:海外直邮,03:个人申报,04:邮关)
							//detail.getOrderchannel(); //订单渠道。PC，手机MOBILE，WAP， 电话PHONE
							//detail.getOrderLineNumber(); //订单行项目号
							//detail.getOrderLineStatus(); //订单行项目状态。10=待发货；20=已发货；30=交易成功
							//detail.getPackageorderid(); //包裹号
							
							orders.setPayMoney(new BigDecimal(detail.getPayAmount())); //付款金额。订单行项目金额
							
							//detail.getPayerCustomerName(); //支付人姓名
							//detail.getPayerIdNumber(); //支付人身份证号码
							
							//detail.getPayorderid(); //支付单号
							
							//detail.getPhoneIdentifyCode(); //手机串码
							//detail.getPrmtcode(); //推广方式。取值为01时，表示网盟
							
							//detail.getProductCode(); //苏宁商品编码
							orders.setGoodName(detail.getProductName()); //商品中文名称
							
							//detail.getReceivezipCode(); //收货人邮政编码
							//detail.getReservebalanceamount(); //	预定尾款金额
							//detail.getReservedepositamount();//预定定金金额
							//detail.getReservestatus(); //预定状态（M：定金已支付，P：定金已罚没，R：定金已退还）
							//detail.getReturnOrderFlag(); //退货订单标示位 0表示正常订单, 1表示退货订单
							
							orders.setGoodNum(dFormat.parse(detail.getSaleNum()).intValue()); //数量
							goodNum=goodNum+orders.getGoodNum();
							order.setFreightPrice(new BigDecimal(detail.getTransportFee())); //运费。订单行项目对应的运费
							
							orders.setGoodPrice(new BigDecimal(detail.getUnitPrice())); //商品单价
							
							
							
							//orders.setDiscountMoney(new BigDecimal(detail.getVouchertotalMoney())); //优惠单金额
							
							//detail.getServiceItemFlag(); //0非服务行 ,1服务行
							//detail.getOtoOrderType(); //O2O订单类型
							//detail.getDepositmoney();//押金
							//detail.getRentperiod();//租期
							//detail.getRenttype(); //租期类型
							
							order.setBizTypId(detail.getOrderServiceTypeMulti()); //组合的订单业务类型逗号隔开
							
							List<CouponList> couponList = detail.getCouponList(); //
							BigDecimal pDiscountMoney = new BigDecimal(0.00);
							//BigDecimal pAdjustMoney = new BigDecimal(0.00);
							
							if(couponList.size() > 0) {
								
								for(CouponList coupon : couponList) {
									//coupon.getCoupontype(); //券类型。券类型表示返券返积分的类型：5998-店铺优惠券、6998-联合0元购券、7998-0元购券、8012-积分抵现、9994-优惠券、9995-优惠券。
									//coupon.getSharelimit(); //销售单据分摊金额。订单返券返积分总金额需每个行项目的销售单据分摊额度累计。
									CouponDetail coup = new CouponDetail();
									coup.setPlatId("SN");
									coup.setStoreId(storeId);
									coup.setOrderId(query.getOrderCode()); //平台订单号
									coup.setCouponCode(coupon.getCoupontype());
									if(Integer.valueOf(coupon.getCoupontype()) == 5998) {
										coup.setCouponType("店铺优惠券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 6998) {
										coup.setCouponType("联合0元购券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 7998) {
										coup.setCouponType("0元购券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 8012) {
										coup.setCouponType("积分抵现");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9994) {
										coup.setCouponType("优惠券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9995) {
										coup.setCouponType("优惠券");
									} else {
										coup.setCouponType("未知优惠券类型");
									}
									
									coup.setCouponPrice(new BigDecimal(coupon.getSharelimit()));
									
									couponsList.add(coup);
								} 
							}
							
							List<PaymentList> paymentList = detail.getPaymentList(); //支付明细
							
							if(paymentList.size() > 0) {
								for(PaymentList payment : paymentList) {
									if(StringUtils.isNotEmpty(payment.getBanktypecode())) {
										if(Integer.valueOf(payment.getBanktypecode()) == 10006 || Integer.valueOf(payment.getBanktypecode()) == 10003) {
											PlatOrderPaymethod pay = new PlatOrderPaymethod();
											pay.setPayStatus("支付成功");
											pay.setOrderId(orderId); 
											pay.setPlatId("SN");
											pay.setStoreId(storeId);
											
											pay.setPayWay(payment.getPaycode());
											pay.setPaymoneyTime(order.getPayTime());
											pay.setPayMoney(new BigDecimal(payment.getPayamount()));
											
											//支付方式编码。表示对订单使用的券用积分类型：5998-店铺优惠券、6998-联合0元购券、7998-0元购券、8012-积分抵现、9994-优惠券、9995-优惠券、10001-云券、10002-限品类云券、10003-店铺云券、10004-易券、10005-限品类易券、10006-店铺易券、10009-无敌券。
											pay.setBanktypecode(payment.getBanktypecode()); //10006,10003
											 //线下支付：1；非线下支付：空值
											pay.setOffLinePayFlag(payment.getOffLinePayFlag());
											
											//支付方式分摊金额。订单使用劵和积分总金额需每个行项目的支付方式分摊金额累计。
											//payment.getPayamount(); 
											//付款方式。4148-易付宝支付、5002-礼品卡支付、9003-门店付款、6901-券或云钻支付、6903-易付宝优惠券支付、2037-光大MIS银行卡（新）、1001-现金 (CASH)、2044-建行MIS银行卡（新）、6906-赠品支付、4237-支付宝支付、4245-微信支付、6915-铜板支付。
											//payment.getPaycode();
											//商家的承担比例
											pay.setMerchantPercent(payment.getMerchantPercent());
											//平台的承担比例
											pay.setPlatformPercent(payment.getPlatformPercent());
											
											pDiscountMoney = pDiscountMoney.add(new BigDecimal(payment.getPayamount()));
											
											payMentList.add(pay);
										} 
									} 
											
								}
							}
							goodMoney = goodMoney.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));
							discountMoney = discountMoney.add(pDiscountMoney);
							
							
							orders.setDiscountMoney(pDiscountMoney); //系统优惠金额 --系统
							orders.setAdjustMoney(BigDecimal.ZERO); //优惠劵金额 ---店铺
							ordersList.add(orders);
							
						}
						order.setGoodMoney(goodMoney); //应付金额
						order.setDiscountMoney(discountMoney);
						order.setAdjustMoney(BigDecimal.ZERO);			
						order.setPayMoney(goodMoney.subtract(discountMoney)); //支付金额，支付方式的支付金额
						order.setOrderSellerPrice(order.getPayMoney());//结算金额
						order.setGoodNum(goodNum);//sku订购数量
						order.setPlatOrdersList(ordersList);
						//计算按照比例平均单价
						ordersList = platOrderPdd.countAveragePrice(ordersList, order.getPayMoney());
						//检查是否组套
						//ordersList = platOrderPdd.checkPlatOrdersList(order,ordersList,order.getPayMoney());
						
						
						int c = 0;
						if(couponsList.size() > 0) {
							c = couponDetailDao.insert(couponsList);
						}
						int p = 0;
						if(payMentList.size() > 0) {
							p = paymethodDao.insert(payMentList);
							
						}
						int po = 0;
						if(order != null) {
							downloadCount++;
							po++;
							platOrderDao.insert(order);
							
						}
						int  pos = 0;
						if(ordersList.size() > 0) {
							pos = platOrdersDao.insert(ordersList);
						}			
						if(po > 0 && pos > 0) {
							/*
							 * // 订单插入完成后，判断是否自动免审 PlatOrder platorder =
							 * platOrderDao.selectByEcOrderId(order.getEcOrderId()); List<PlatOrders>
							 * orderslist = platOrdersDao.select(platorder.getOrderId()); if
							 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
							 * 返回true时，此订单通过免审，直接进入审核
							 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
							 * // 设置默认操作员sys }
							 * 
							 * message = "下载成功"; logger.info("订单下载:苏宁平台订单下载成功");
							 */
							//执行自动匹配
							platOrderService.autoMatch(orderId, userId);
							isSuccess = true;
						}
					}
				}
				
				/*else {
					message = "订单下载:苏宁平台订单已存在,订单号:"+query.getOrderCode();
					isSuccess = false;
					//订单已存在
					logger.error("订单下载:苏宁平台订单已存在,订单号:{}",query.getOrderCode());
					
				}*/
				}
			}
			
		}  else {
			
			message = "订单下载:苏宁平台下载订单数据为空";
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
		if(isSuccess) {
			message = "本次自动下载店铺： "+ storeSettings.getStoreName() + "订单" + downloadCount + "条";
		} 
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		return map;
	}
	
	
	/**
	 * 订单详情获取-苏宁
	 * @return
	 * @throws CloneNotSupportedException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Transactional
	private Map<String,Object> getPlatOrder(OrderGet query,
										 String storeId,String userId,
										 StoreRecord storeRecord) throws CloneNotSupportedException, IOException, ParseException {

		boolean isSuccess = true;
		String resp = "";
		Map<String,Object> map = new HashMap<>();
		DecimalFormat dFormat = new DecimalFormat("0.00");
		int downloadCount = 0;
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		if(query!=null) {
				System.out.println(query.getOrderTotalStatus());
				if(query.getOrderTotalStatus().equals("10")||query.getOrderTotalStatus().equals("15")||query.getOrderTotalStatus().equals("20")||
						query.getOrderTotalStatus().equals("30")||query.getOrderTotalStatus().contentEquals("21")) {
					//订单总状态，0 订单取消；5 未付款；10 买家已付款；15 发货中；20 已发货；21 部分发货；30 交易成功；40 交易关闭
				
				
				
				if (platOrderDao.checkExsits(query.getOrderCode(), storeId) == 0) {
					
					PlatOrder order = new PlatOrder();
					List<PlatOrders> ordersList = new ArrayList<>(); //商品				
					List<InvoiceInfo> invoiceList = new ArrayList<>(); //发票
					List<CouponDetail> couponsList = new ArrayList<>(); //优惠			
					List<PlatOrderPaymethod> payMentList = new ArrayList<>(); //支付
					BigDecimal goodMoney = new BigDecimal(0.00);
					BigDecimal discountMoney = new BigDecimal(0.00);
					BigDecimal adjustMoney = new BigDecimal(0.00);
					
					String orderId  = getOrderNo.getSeqNo("ec", userId);
					order.setDownloadTime(simpleDateFormat.format(new Date()));
					order.setOrderId(orderId); //px-订单编号
					order.setStoreId(storeId);
					order.setStoreName(storeRecord.getStoreName());
					
					//query.getAccount(); //开户账号
					//query.getBank();//开户行
					order.setBuyerNote(query.getBuyerOrdRemark());//订单备注 -买家
					order.setCityId(query.getCityCode());//城市代码
					order.setCity(query.getCityName());//市
					order.setRecAddress(query.getCustomerAddress());//详细地址
					
					//query.getCustomerName();//顾客姓名
					//order.setIsShip(0);//是否发货为0
					order.setHasGift(0);//是否含赠品
					order.setAdjustStatus(0);//调整状态设置为0
					order.setTradeDt(query.getOrderSaleTime());//下单时间 下单时间用支付时间 接口没找到下单时间
					order.setBizTypId("2");
					order.setSellTypId("1");
					order.setRecvSendCateId("6");
					order.setCountyId(query.getDistrictCode());//区域代码
					order.setCounty(query.getDistrictName());//区
					
					//query.getEvaluationMark();//评价状态 0未评价 1已评价
					
					//获取发票信息					
					if(query.getNeedinvoiceflag().equals("Y")) {
						
						invoiceList = getInvoice(query,storeId,orderId,invoiceList,order);
		
					} else {
						order.setIsInvoice(0);
					}
					
				
					//query.getInvoiceRecipientAddress();//发票收件人地址					
					//query.getInvoiceRecipientPhone(); //发票收件人电话
					//query.getRegisterAddress(); //注册地址
					//query.getRegisterPhone(); //注册电话
					
					//query.getMobNum(); //买家联系电话
				
					order.setEcOrderId(query.getOrderCode()); //B2C订单号
					
					order.setPayTime(query.getOrderSaleTime()); //订单支付时间,格式：yyyy-MM-dd HH:mm:ss
					//order.setOrderStatus(Integer.valueOf(query.getOrderTotalStatus())); //订单总状态
					//order.setOrderStatus(0);//订单状态
					
					//query.getPayType(); //支付方式，如礼品卡支付
					
					order.setProvinceId(query.getProvinceCode()); //省份代码
					order.setProvince(query.getProvinceName()); //收件人地址（省）中文
					
					order.setSellerNote(query.getSellerOrdRemark()); //订单备注（卖家）--商家对订单的备注信息
					order.setBuyerId(query.getUserName()); //买家帐号
					order.setSellTypId("1");// 设置销售类型普通销售
					order.setBizTypId("2");// 设置业务类型2c	
					order.setRecName(query.getCustomerName());//收货人姓名
					order.setOrderStatus(0); //订单状态
					if (query.getOrderTotalStatus().equals("10")) {//未发货
						order.setIsShip(0);
					}else {
						order.setIsShip(1);
						String expressNo = "";
						for (int i = 0; i < query.getOrderDetail().size(); i++) {
							if(query.getOrderDetail().get(i).getExpressno().length()>0) {
								expressNo=query.getOrderDetail().get(i).getExpressno();
								break;
							}
							
						}
						order.setExpressNo(expressNo);//快递单号
					}
					order.setReturnStatus(0); //退货状态
					order.setIsClose(0); // 是否关闭
					order.setIsAudit(0); // 是否客审
					order.setVenderId("SN"); // '商家id'
					order.setRecMobile(query.getMobNum());//电话
					List<com.suning.api.entity.custom.orderGetGetResponse.OrderDetail> orderDetailList = query.getOrderDetail();
					
					if(orderDetailList.size() > 0) {
						for(com.suning.api.entity.custom.orderGetGetResponse.OrderDetail detail : orderDetailList) {
							PlatOrders orders = new PlatOrders();
							orders.setOrderId(orderId);
							orders.setEcOrderId(order.getEcOrderId());
		
							
							//detail.getActivitytype(); //活动类型（02：渠道专享订单，03：免费试用订单，04：付邮试用订单，05,06：预定订单，07：秒杀订单，08：S码活动订单，09：赠品）
							//detail.getbLineNumber(); //b2c行项目号
							//detail.getCarShopAddr(); //	汽车服务门店地址
							//detail.getCarShopCode(); //汽车服务门店编码
							//detail.getCarShopName(); //汽车服务门店名称
							//detail.getCarShopSerWay(); //汽车服务方式（1-上门服务，2-到店服务）
							//detail.getCarShopTel(); //汽车服务门店电话
							
							//detail.getDeclareGoodsAmount(); //申报订单行货款
							//detail.getDeclareItemPrice(); //申报单价
							//detail.getDeclareItemTaxfare(); //申报税费
							
							//detail.getDisType(); //发货方式。01 代表海外直邮发货；02代表商家保税区发货；03代表苏宁保税区发货;空代表国内海外购
			
							
							//detail.getExpresscompanycode(); //物流公司编码
							//detail.getExpressno(); //运单号
							
							//4PS发货标示,1代表4PS发货、0代表C店发货
							if(Integer.valueOf(detail.getFpsdeliveryflag()) == 0) {
								order.setDeliverSelf(1); //自发货
							}else{
								order.setDeliverSelf(0);
							}; 
							
							//detail.getHwgFlag(); //海外购标识。01表示海外购订单，其他值为非海外购订单
							
							orders.setDeliverWhs(detail.getInvCode()); //仓库编码
							
							//detail.getIsProsupplierDelivery(); //是否供应商发货标识，1代表是
							
							//detail.getItemCode(); //商家商品编码
							orders.setGoodId(detail.getProductCode()); // 平台商品编号 苏宁平台编码
							
							//detail.getItemTaxFare(); //税费
							//detail.getMode(); //通关模式(01:保税备货,02:海外直邮,03:个人申报,04:邮关)
							//detail.getOrderchannel(); //订单渠道。PC，手机MOBILE，WAP， 电话PHONE
							//detail.getOrderLineNumber(); //订单行项目号
							//detail.getOrderLineStatus(); //订单行项目状态。10=待发货；20=已发货；30=交易成功
							//detail.getPackageorderid(); //包裹号
							
							orders.setPayMoney(new BigDecimal(detail.getPayAmount())); //付款金额。订单行项目金额
							
							//detail.getPayerCustomerName(); //支付人姓名
							//detail.getPayerIdNumber(); //支付人身份证号码
							
							//detail.getPayorderid(); //支付单号
							
							//detail.getPhoneIdentifyCode(); //手机串码
							//detail.getPrmtcode(); //推广方式。取值为01时，表示网盟
							
							//detail.getProductCode(); //苏宁商品编码
							orders.setGoodName(detail.getProductName()); //商品中文名称
							
							//detail.getReceivezipCode(); //收货人邮政编码
							//detail.getReservebalanceamount(); //	预定尾款金额
							//detail.getReservedepositamount();//预定定金金额
							//detail.getReservestatus(); //预定状态（M：定金已支付，P：定金已罚没，R：定金已退还）
							//detail.getReturnOrderFlag(); //退货订单标示位 0表示正常订单, 1表示退货订单
							
							orders.setGoodNum(dFormat.parse(detail.getSaleNum()).intValue()); //数量
							
							order.setFreightPrice(new BigDecimal(detail.getTransportFee())); //运费。订单行项目对应的运费
							
							orders.setGoodPrice(new BigDecimal(detail.getUnitPrice())); //商品单价
							
							
							
							//orders.setDiscountMoney(new BigDecimal(detail.getVouchertotalMoney())); //优惠单金额
							
							//detail.getServiceItemFlag(); //0非服务行 ,1服务行
							//detail.getOtoOrderType(); //O2O订单类型
							//detail.getDepositmoney();//押金
							//detail.getRentperiod();//租期
							//detail.getRenttype(); //租期类型
							
							order.setBizTypId(detail.getOrderServiceTypeMulti()); //组合的订单业务类型逗号隔开
							
							List<com.suning.api.entity.custom.orderGetGetResponse.CouponList> couponList = detail.getCouponList(); //
							BigDecimal pDiscountMoney = new BigDecimal(0.00);
							//BigDecimal pAdjustMoney = new BigDecimal(0.00);
							
							if(couponList.size() > 0) {
								
								for(com.suning.api.entity.custom.orderGetGetResponse.CouponList coupon : couponList) {
									//coupon.getCoupontype(); //券类型。券类型表示返券返积分的类型：5998-店铺优惠券、6998-联合0元购券、7998-0元购券、8012-积分抵现、9994-优惠券、9995-优惠券。
									//coupon.getSharelimit(); //销售单据分摊金额。订单返券返积分总金额需每个行项目的销售单据分摊额度累计。
									CouponDetail coup = new CouponDetail();
									coup.setPlatId("SN");
									coup.setStoreId(storeId);
									coup.setOrderId(query.getOrderCode()); //平台订单号
									coup.setCouponCode(coupon.getCoupontype());
									if(Integer.valueOf(coupon.getCoupontype()) == 5998) {
										coup.setCouponType("店铺优惠券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 6998) {
										coup.setCouponType("联合0元购券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 7998) {
										coup.setCouponType("0元购券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 8012) {
										coup.setCouponType("积分抵现");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9994) {
										coup.setCouponType("优惠券");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9995) {
										coup.setCouponType("优惠券");
									} else {
										coup.setCouponType("未知优惠券类型");
									}
									
									coup.setCouponPrice(new BigDecimal(coupon.getSharelimit()));
									
									couponsList.add(coup);
								} 
							}
							
							List<com.suning.api.entity.custom.orderGetGetResponse.PaymentList> paymentList = detail.getPaymentList(); //支付明细
							
							if(paymentList.size() > 0) {
								for(com.suning.api.entity.custom.orderGetGetResponse.PaymentList payment : paymentList) {
									if(StringUtils.isNotEmpty(payment.getBanktypecode())) {
										if(Integer.valueOf(payment.getBanktypecode()) == 10006 || Integer.valueOf(payment.getBanktypecode()) == 10003) {
											PlatOrderPaymethod pay = new PlatOrderPaymethod();
											pay.setPayStatus("支付成功");
											pay.setOrderId(orderId); 
											pay.setPlatId("SN");
											pay.setStoreId(storeId);
											
											pay.setPayWay(payment.getPaycode());
											pay.setPaymoneyTime(order.getPayTime());
											pay.setPayMoney(new BigDecimal(payment.getPayamount()));
											
											//支付方式编码。表示对订单使用的券用积分类型：5998-店铺优惠券、6998-联合0元购券、7998-0元购券、8012-积分抵现、9994-优惠券、9995-优惠券、10001-云券、10002-限品类云券、10003-店铺云券、10004-易券、10005-限品类易券、10006-店铺易券、10009-无敌券。
											pay.setBanktypecode(payment.getBanktypecode()); //10006,10003
											 //线下支付：1；非线下支付：空值
											pay.setOffLinePayFlag(payment.getOffLinePayFlag());
											
											//支付方式分摊金额。订单使用劵和积分总金额需每个行项目的支付方式分摊金额累计。
											//payment.getPayamount(); 
											//付款方式。4148-易付宝支付、5002-礼品卡支付、9003-门店付款、6901-券或云钻支付、6903-易付宝优惠券支付、2037-光大MIS银行卡（新）、1001-现金 (CASH)、2044-建行MIS银行卡（新）、6906-赠品支付、4237-支付宝支付、4245-微信支付、6915-铜板支付。
											//payment.getPaycode();
											//商家的承担比例
											pay.setMerchantPercent(payment.getMerchantPercent());
											//平台的承担比例
											pay.setPlatformPercent(payment.getPlatformPercent());
											
											pDiscountMoney = pDiscountMoney.add(new BigDecimal(payment.getPayamount()));
											
											payMentList.add(pay);
										} 
									} 
											
								}
							}
							goodMoney = goodMoney.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));
							discountMoney = discountMoney.add(pDiscountMoney);
							
							
							orders.setDiscountMoney(pDiscountMoney); //系统优惠金额 --系统
							orders.setAdjustMoney(BigDecimal.ZERO); //优惠劵金额 ---店铺
							ordersList.add(orders);
							
						}
						order.setGoodMoney(goodMoney); //应付金额
						order.setDiscountMoney(discountMoney);
						order.setAdjustMoney(BigDecimal.ZERO);			
						order.setPayMoney(goodMoney.subtract(discountMoney)); //支付金额，支付方式的支付金额
						
						order.setPlatOrdersList(ordersList);
						//计算按照比例平均单价
						ordersList = platOrderPdd.countAveragePrice(ordersList, order.getPayMoney());
						//检查是否组套
						//ordersList = platOrderPdd.checkPlatOrdersList(order,ordersList,order.getPayMoney());
						
						
						int c = 0;
						if(couponsList.size() > 0) {
							c = couponDetailDao.insert(couponsList);
						}
						int p = 0;
						if(payMentList.size() > 0) {
							p = paymethodDao.insert(payMentList);
							
						}
						int po = 0;
						if(order != null) {
							downloadCount++;
							po++;
							platOrderDao.insert(order);
							
						}
						int  pos = 0;
						if(ordersList.size() > 0) {
							pos = platOrdersDao.insert(ordersList);
						}			
						if(po > 0 && pos > 0) {
							/*
							 * // 订单插入完成后，判断是否自动免审 PlatOrder platorder =
							 * platOrderDao.selectByEcOrderId(order.getEcOrderId()); List<PlatOrders>
							 * orderslist = platOrdersDao.select(platorder.getOrderId()); if
							 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
							 * 返回true时，此订单通过免审，直接进入审核
							 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
							 * // 设置默认操作员sys }
							 * 
							 * message = "下载成功"; logger.info("订单下载:苏宁平台订单下载成功");
							 */
							//执行自动匹配
							platOrderService.autoMatch(orderId, userId);
							isSuccess = true;
						}
					}
				}
				
				/*else {
					message = "订单下载:苏宁平台订单已存在,订单号:"+query.getOrderCode();
					isSuccess = false;
					//订单已存在
					logger.error("订单下载:苏宁平台订单已存在,订单号:{}",query.getOrderCode());
					
				}*/
				}
			
		}  else {
			
			message = "订单下载:苏宁平台下载订单数据为空";
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
		if(isSuccess) {
			message = "本次自动下载店铺： "+ storeSettings.getStoreName() + "订单" + downloadCount + "条";
		} 
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		return map;
	}
	
	/**
	 * post获取发票信息
	 */
	private List<InvoiceInfo> getInvoice(OrderQuery query,String storeId,
										 String orderId,List<InvoiceInfo> invoiceList,PlatOrder order) {
		InvoiceInfo info = new InvoiceInfo();
		info.setPlatId("SN");
		info.setShopId(storeId);
		info.setOrderId(orderId);
		info.setInvoiceType(query.getInvoiceType());//发票类型 -- 增值还是普通（01增值 02普通 04 电子发票）
		info.setInvoiceTitle(query.getInvoiceHead());  //抬头
		info.setInvoiceContentId(query.getInvoice());//发票内容
		info.setInvoiceConsigneePhone(query.getInvoiceRecipientHandPhone()); //发票收件人手机
		info.setInvoiceCode(query.getVatTaxpayerNumber()); //纳税人识别号
		order.setIsInvoice(1);
		order.setInvoiceTitle(query.getInvoiceHead()); //抬头
		invoiceList.add(info);
		return invoiceList;
	}
	
	/**
	 * get获取发票信息
	 */
	private List<InvoiceInfo> getInvoice(OrderGet query,String storeId,
										 String orderId,List<InvoiceInfo> invoiceList,PlatOrder order) {
		InvoiceInfo info = new InvoiceInfo();
		info.setPlatId("SN");
		info.setShopId(storeId);
		info.setOrderId(orderId);
		info.setInvoiceType(query.getInvoiceType());//发票类型 -- 增值还是普通（01增值 02普通 04 电子发票）
		info.setInvoiceTitle(query.getInvoiceHead());  //抬头
		info.setInvoiceContentId(query.getInvoice());//发票内容
		info.setInvoiceConsigneePhone(query.getInvoiceRecipientHandPhone()); //发票收件人手机
		info.setInvoiceCode(query.getVatTaxpayerNumber()); //纳税人识别号
		order.setIsInvoice(1);
		order.setInvoiceTitle(query.getInvoiceHead()); //抬头
		invoiceList.add(info);
		return invoiceList;
	}
	
}
