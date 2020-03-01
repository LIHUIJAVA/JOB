package com.px.mis.ec.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Ref;
import java.text.DecimalFormat;
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

import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddOrderInformationGetRequest;
import com.pdd.pop.sdk.http.api.request.PddOrderListGetRequest;
import com.pdd.pop.sdk.http.api.request.PddRefundListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.response.PddOrderInformationGetResponse;
import com.pdd.pop.sdk.http.api.response.PddOrderInformationGetResponse.OrderInfoGetResponseOrderInfo;
import com.pdd.pop.sdk.http.api.response.PddOrderInformationGetResponse.OrderInfoGetResponseOrderInfoItemListItem;
import com.pdd.pop.sdk.http.api.response.PddOrderListGetResponse;
import com.pdd.pop.sdk.http.api.response.PddOrderListGetResponse.OrderListGetResponseOrderListItem;
import com.pdd.pop.sdk.http.api.response.PddRefundListIncrementGetResponse.RefundIncrementGetResponseRefundListItem;
import com.pdd.pop.sdk.http.api.response.PddRefundListIncrementGetResponse;
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
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
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
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
@Component
public class PlatOrderPdd {
	
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
	private AuditStrategyService auditStrategyService;
	@Autowired
	private AssociatedSearchService associatedSearchService;
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private MisUserDao misUserDao;
	@Autowired
	private PlatOrderService platOrderService;
	private Logger logger = LoggerFactory.getLogger(PlatOrderPdd.class);
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 拼多多-订单下载
	 * @throws Exception 
	 */
	@Transactional
	public String pddDownload(String userId, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception{
		    String message ="";
			String resp = "";
			boolean isSucess = true;
			int downloadCount = 0;
			// 设置
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			// 信息
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			
			
			Date date = sf.parse(startDate);
			long start = date.getTime();
			date = sf.parse(endDate);
			long end = date.getTime();
			long cha = end-start;
			
			List<OrderListGetResponseOrderListItem> order_list = new ArrayList<>();  
			 
		  // 总共的天数
		   double result = cha * 1.0 / (1000 * 60 * 60 * 24);
		   for (int i = 0; i < result ; i++) {
			   if(i+1>=result) {
				   order_list.addAll(pddDownLoadOrder(storeSettings,storeRecord,pageNo,pageSize,start+(1000 * 60 * 60 * 24 * i),end,order_list));
			   }else {
				   order_list.addAll(pddDownLoadOrder(storeSettings,storeRecord,pageNo,pageSize,start+(1000 * 60 * 60 * 24 * i),start+(1000 * 60 * 60 * 24 * (i+1)),order_list));
			   }
		    
		   }
			
			//List<OrderListGetResponseOrderListItem> order_list = new ArrayList<>();		
			//order_list = pddDownLoadOrder(storeSettings,storeRecord,pageNo,pageSize,start,end,order_list);
			
			for (int i = 0; i < order_list.size(); i++) {
				OrderListGetResponseOrderListItem order = order_list.get(i);
				
				//查询对应店铺是否存在此订单
				if (platOrderDao.checkExsits(order.getOrderSn(), storeId) == 0) {
					//订单不存在
					Map<String,Object> map = pddDownloadByOrderCode(order.getOrderSn(), userId, storeId,downloadCount);
					isSucess = (Boolean)map.get("isSuccess");
					message = (String)map.get("message");
					downloadCount = (int)map.get("downloadCount");
					
				} else {
					message = "订单已存在,订单号:"+order.getOrderSn()+",店铺编号:"+storeId;
					isSucess = false;
					logger.error("订单下载:拼多多平台订单已存在,订单号:{},店铺编号:{}",order.getOrderSn(),storeId);	
				}

			}	
			if(isSucess) {
				message = "本次自动下载店铺： "+ storeSettings.getStoreName() + "订单" + downloadCount + "条";
			}
			resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

		return resp;
	}
	
	String message = "";
	
	/**
	 * post-pdd下载订单
	 * @throws Exception 
	 */
	public List<OrderListGetResponseOrderListItem> pddDownLoadOrder(StoreSettings storeSettings,StoreRecord storeRecord,
			int pageNo,int pageSize,long start,long end,List<OrderListGetResponseOrderListItem> order_list) throws Exception {
		
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddOrderListGetRequest request = new PddOrderListGetRequest();
		request.setOrderStatus(5);// 发货状态，1：待发货，2：已发货待签收，3：已签收 5：全部
		request.setRefundStatus(5);// 售后状态 1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功 5：全部

		request.setStartConfirmAt(start / 1000);// 必填，成交时间开始时间的时间戳，指格林威治时间 1970 年 01 月 01 日 00 时 00 分 00 秒起至现在的总秒数
		request.setEndConfirmAt(end / 1000);
		request.setPage(pageNo);
		request.setPageSize(pageSize);
		
		request.setTradeType(0);// 订单类型 0-普通订单 ，1- 定金订单
		 	
		//拼多多查询订单
		PddOrderListGetResponse response = client.syncInvoke(request, accessToken);
		//异常返回
		if (response.getErrorResponse() != null) {
			
			message = "订单下载:拼多多平台下载订单异常,异常信息:"+	response.getErrorResponse().getErrorMsg();
			logger.error("订单下载:拼多多平台下载订单异常,异常信息:{}",response.getErrorResponse().getErrorMsg());		
		}
		//正常返回
		if(response.getOrderListGetResponse() != null) {
			 order_list.addAll(0, response.getOrderListGetResponse().getOrderList());
		}
		
		if (response.getOrderListGetResponse() != null && response.getOrderListGetResponse().getTotalCount() > pageNo * pageSize) {
			
			pddDownLoadOrder(storeSettings,storeRecord,pageNo + 1, pageSize, start, end, order_list);
		}
		
		return order_list;
	}
	/**
	 * 拼多多-退货
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public String pddRefund(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception {
		int successCount=0;
		boolean isSuccess = true;
		String resp = "";
		List<RefundOrder> reList = new ArrayList<>();
		List<RefundIncrementGetResponseRefundListItem> refundList = new ArrayList<>();
		
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		// 信息
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		
		List<String> list = ECHelper.getIntervalTimeList(startDate,endDate,30);
		
		if(list.size()>1) {
			for (int i = 0; i < list.size(); i++) {
				refundList = postPddRefundOrder(storeSettings, storeRecord, pageNo, pageSize, list.get(i), list.get(i+1),
						refundList);
				if(i+2==list.size()) {
					break;
				}
			}
		}
		
		refundList = postPddRefundOrder(storeSettings,storeRecord,pageNo,pageSize,startDate,endDate,refundList);
		//System.out.println(refundList.size());
        if(refundList.size() > 0) {
        	
        	
    		//RefundOrder 退款单
    		for(RefundIncrementGetResponseRefundListItem refund : refundList) {
    			
    			String orderCode = refund.getOrderSn();//平台订单号		
    			String ecRefId = String.valueOf(refund.getId()); //电商退款单号
    			
    			//查询平台订单
    			List<PlatOrder> orders = platOrderDao.selectPlatOrdersByEcOrderId(orderCode);
				
				if (orders.size()>0) {
					
					RefundOrder refundOrder = refundOrderDao.selectEcRefId(ecRefId);
					if(refundOrder == null) {
						
						refundOrder = pddGetRundOrder(refund,storeRecord,isSuccess,accNum);		
						if (refundOrder.getRefundOrders().size()>0) {
							//返回的list大于0保存
							refundOrderDao.insert(refundOrder);
							refundOrdersDao.insertList(refundOrder.getRefundOrders());
							successCount++;
						}
    					
					}
				}	
        	}
    		message="本次成功下载拼多多店铺："+storeRecord.getStoreName()+"退款"+successCount+"条";
        }  else {
    		message = "本次成功下载拼多多店铺："+storeRecord.getStoreName()+"退款0条";
    		isSuccess = false; 		
    	}
        
        // 日志记录
        MisUser misUser = misUserDao.select(accNum);
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(accNum);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent(message);
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(12);// 12退款下载
		logRecord.setTypeName("退款单下载");
		logRecord.setOperatOrder(null);
		logRecordDao.insert(logRecord);
        
        if(isSuccess) {
        	logger.info("下载退款单成功,本次下载退款单共计:{}条",refundList.size());
        }
        resp = BaseJson.returnRespObj("ec/refundOrder/download", isSuccess, message, null);		
		return resp;
	}
	/**
	 * post-拼多多批量下载退款单
	 */
	private List<RefundIncrementGetResponseRefundListItem> postPddRefundOrder(StoreSettings storeSettings,StoreRecord storeRecord,
			int pageNo,int pageSize, String startDate, String endDate, List<RefundIncrementGetResponseRefundListItem> refundList) throws Exception {
		
		Date date = sf.parse(startDate);
		long start = date.getTime();
		date = sf.parse(endDate);
		long end = date.getTime();
		
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		
		PopClient client = new PopHttpClient(clientId, clientSecret);

        PddRefundListIncrementGetRequest request = new PddRefundListIncrementGetRequest();
        request.setAfterSalesStatus(1);
        request.setAfterSalesType(1);
        request.setEndUpdatedAt(end/1000);
        request.setPage(pageNo);
        request.setPageSize(pageSize);
        request.setStartUpdatedAt(start/1000);
        PddRefundListIncrementGetResponse response = client.syncInvoke(request, accessToken);
        
        if (response.getErrorResponse() != null) {
			
			logger.error("下载退款单:拼多多平台查询退款单异常,异常信息:{}",response.getErrorResponse().getErrorMsg());
			message = "下载退款单:拼多多平台查询退款异常,异常信息:"+response.getErrorResponse().getErrorMsg();
				
		}
        if(response.getRefundIncrementGetResponse() != null) {
        	//售后列表对象
        	refundList.addAll(0,response.getRefundIncrementGetResponse().getRefundList()); 
        	
        }
        if(response.getRefundIncrementGetResponse() != null && response.getRefundIncrementGetResponse().getTotalCount() != null) {
        	response.getRefundIncrementGetResponse().getTotalCount();
        	if(Integer.valueOf(response.getRefundIncrementGetResponse().getTotalCount()) > pageNo * pageSize) {
        		postPddRefundOrder(storeSettings,storeRecord,pageNo+1,pageSize,startDate,endDate,refundList);
			}
        }
		return refundList;
	}

	/**
	 * 拼多多-获取退货单数据集合
	 */
	public RefundOrder pddGetRundOrder(RefundIncrementGetResponseRefundListItem refund,StoreRecord storeRecord,Boolean isSuccess,String accNum) throws IllegalAccessException, InvocationTargetException {
		
			
			//不存在
			RefundOrder order = new RefundOrder();
			String refId = getOrderNo.getSeqNo("tk", accNum);
			order.setRefId(refId);
			//Integer.valueOf(refund.getSpeedRefundStatus()); //极速退款状态，"1"：有极速退款资格，"2"：极速退款失败, "3" 表示极速退款成功，其他表示非极速退款
			order.setRefReason(refund.getAfterSaleReason()); //售后原因
			order.setRefStatus(refund.getAfterSalesStatus());//售后状态
			refund.getAfterSalesType();//售后类型
			//refund.getConfirmTime(); //成团时间
			order.setApplyDate(refund.getCreatedTime()); //创建时间 -申请日期

			refund.getDiscountAmount(); //订单折扣金额
			order.setEcOrderId(refund.getOrderSn());//订单编号 -电商
			//refund.getGoodImage(); //商品图片
			order.setEcGoodId(String.valueOf(refund.getGoodsId()));//商品编码 -平台
			order.setEcId("PDD");
			order.setExpressCode(refund.getTrackingNumber()); // 快递单号
			order.setEcRefId(String.valueOf(refund.getId())); //售后编号
			//order.setEcGoodId(refund.getOrderSn()); //订单编号 -电商
			order.setAllRefMoney(new BigDecimal(refund.getRefundAmount())); //退款金额
			order.setAllRefNum(Integer.valueOf(refund.getGoodsNumber()));  //商品数量 -退货数量
			
			order.setStoreId(storeRecord.getStoreId()); //店铺编号
			order.setStoreName(storeRecord.getStoreName());//店铺名称
			
			if(Integer.valueOf(refund.getGoodsNumber()) == 0) {
				order.setIsRefund(0);//否
			} else {
				order.setIsRefund(1);//是
			}
			order.setDownTime(sf.format(new Date())); //下载时间
			order.setIsAudit(0); //是否审核
			
			
			//refund.getSpeedRefundStatus();// 极速退款标志位 1：极速退款，0：非极速退款
			if(StringUtils.isNotEmpty(refund.getSpeedRefundStatus())) {
				
				if(refund.getSpeedRefundStatus().contains("1")) {
					order.setMemo("极速退款"); 
				}
			}
			
			order.setSource(0); //退款单来源 0：接口下载
			
			List<RefundOrders> ordersList = new ArrayList<>();
			
			RefundOrders orders = new RefundOrders();

			orders.setRefId(refId);
			orders.setEcGoodId(String.valueOf(refund.getGoodsId())); //拼多多商品编码
			orders.setGoodId(refund.getOuterId()); 
			orders.setGoodName(refund.getGoodsName());
			orders.setGoodSku(refund.getSkuId());
			orders.setRefNum(Integer.valueOf(refund.getGoodsNumber()));
			orders.setRefMoney(new BigDecimal(refund.getRefundAmount())); //实际退款金额
			//System.out.println("处理时间："+refund.getUpdatedTime());
			order.setTreDate(refund.getUpdatedTime());//处理时间用更新时间
			ordersList.add(orders);
			order.setRefundOrders(ordersList);
			order = platOrderSN.checkRefundIsPto(order,accNum);
			//order = getRefundOrders(platOrder,order,isSuccess,refund);
			
			return order;	
			
	}
	/**
	 * 拼多多-获取退货单子表信息
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public RefundOrder getRefundOrders(PlatOrder platOrder,
										RefundOrder order,
										Boolean isSuccess,
										RefundIncrementGetResponseRefundListItem refund) throws IllegalAccessException, InvocationTargetException {
		
			
		for(PlatOrders platOrders : platOrder.getPlatOrdersList()) {
			
			String ecgoodid = String.valueOf(refund.getGoodsId());// 平台商品编号
			String goodsku = platOrders.getGoodSku();
			GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
			if(goodRecord == null) {
				message = "在店铺商品档案中未匹配到对应商品档案";
				isSuccess = false;
				return order;
			}
			if(platOrders.getGoodId().equals(goodRecord.getGoodId())) {
				if(new BigDecimal(refund.getRefundAmount()).compareTo(platOrder.getPayMoney()) == 1 ) {
					// 退款金额大于支付金额
					message = "订单退款金额大于支付金额,平台订单号:"+order.getEcGoodId();
					isSuccess = false;
				} else {
					order.setAllRefMoney(platOrders.getPayMoney());
					order.setAllRefNum(platOrders.getGoodNum());
					
					RefundOrders orders = new RefundOrders();
					orders.setRefId(order.getRefId());
					orders.setGoodName(refund.getGoodsName()); //商品名称
					orders.setRefNum(Integer.valueOf(refund.getGoodsNumber())); //商品数量
					//refund.getGoodsPrice(); //商品单价		
					//refund.getOrderAmount(); //订单金额
					orders.setGoodId(refund.getOuterGoodsId());//商家外部商品编码
					orders.setGoodSku(refund.getOuterId()); //商家外部编码(sku)	
					orders.setCanRefMoney(platOrders.getPayMoney());
					List<RefundOrders> ordersList = new ArrayList<>();
					ordersList.add(orders);
					order.setRefundOrders(ordersList);
					order = checkIsPto(order,orders,isSuccess);			
					refund.getSkuId(); //商品规格id
					refund.getTrackingNumber();//快递运单号
					refund.getUpdatedTime();// 更新时间
					
				}
			}else {
				//没有匹配到商品
				message = "订单未匹配到商品明细,平台订单号:"+order.getEcGoodId();
				isSuccess = false;
			}	
			
		}
		
		return order;
		
	}
	
	/**
	 * 商品-组装拆件
	 */
	public RefundOrder checkIsPto(RefundOrder order,RefundOrders orders,Boolean flag) throws IllegalAccessException, InvocationTargetException {
		
		String ecgoodid = orders.getEcGoodId();// 平台商品编号
		String goodsku = orders.getGoodSku();
		List<RefundOrders> ordersList = order.getRefundOrders();
		
		GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
		if (goodRecord == null) {
			order.setAuditHint("在店铺商品档案中未匹配到对应商品档案");	
			flag = false;
		
		} else if (goodRecord.getGoodId() == null) {
			order.setAuditHint("请完善店铺商品档案中存货编码的对应关系");	
			flag = false;
				
		} else {
			String invtyId = goodRecord.getGoodId(); //商品编号goodId
			
			//按照商品编号查询存货档案(invty_doc),判断产品结构pto
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyId);
			if(invtyDoc == null) {
				message = "未获取到商品编号：["+invtyId+"]对应存货档案";
				flag = false;
			} else {			
				if (invtyDoc.getPto() != null) {
					if (invtyDoc.getPto() == 1) {
						// 属于PTO类型存货
						ProdStru prodStru = prodStruDao.selectMomEncd(invtyId); //查询母件产品
						
						if (prodStru == null) {
							
							order.setAuditHint("在产品结构中未匹配到对应产品结构信息");								
							flag = false;
							
						} else {
							if (prodStru.getIsNtChk() == 0) {
								// 对应产品结构尚未审核
								order.setAuditHint("对应产品结构尚未审核");
								flag = false;
								
							} else {
								List<ProdStruSubTab> strucksublist = prodStru.getStruSubList();
								// 循环产品结构子表信息
								if (strucksublist.size() == 0) {
									order.setAuditHint("对应产品结构没有设置子件明细，请先设置。");
									flag = false;
									
								} else if (strucksublist.size() == 1) {
									
									// 当产品结构明细里面只有一个子件时，直接替换存货编码
									// 仅退款
									if(order.getAllRefNum() == 0) {
										// 设置退货数量
										orders.setRefNum(0);
										// 设置可退数量
										orders.setCanRefNum(0);
									} else {
										// 设置退货数量
										orders.setRefNum(new BigDecimal(orders.getRefNum())
												.multiply(new BigDecimal(strucksublist.get(0).getSubNm())).intValue());
									}
									// 设置可退数量
									orders.setCanRefNum(orders.getRefNum());
									orders.setGoodId(invtyDoc.getInvtyEncd());// 设置对应母件编码
									orders.setGoodName(invtyDoc.getInvtyNm());// 设置对应母件名称
									
									// 设置可退金额,与实付金额一致
									//orders.setRefMoney(orders.getRefMoney());
									ordersList.add(orders);
									flag = true;
									
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
													.multiply(strucksublist.get(j).getSubQty())); //应付
										}
									}
									
									DecimalFormat dFormat = new DecimalFormat("0.00");
									
									BigDecimal temp = new BigDecimal(dFormat.format(total));
									
									for (int j = 0; j < strucksublist.size(); j++) {
										
										RefundOrders orders1 = new RefundOrders();
										
										BeanUtils.copyProperties(orders1, orders);
										
										// 计算每条子件占总成本的比例 保留8位小数
										InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
												strucksublist.get(j).getSubEncd());
										BigDecimal rate = invtyDoc2.getRefCost()
												.multiply(strucksublist.get(j).getSubQty())
												.divide(total, 8, BigDecimal.ROUND_HALF_UP);
										orders1.setRefNum((new BigDecimal(orders.getRefNum()))
												.multiply(strucksublist.get(j).getSubQty()).intValue());
										
										// 退货金额 保留两位小数
										if(j == strucksublist.size() - 1) {
											
											orders1.setRefMoney(temp);
											
											
										} else {
											
											orders1.setRefMoney(order.getAllRefMoney().multiply(rate).divide(total, 2, BigDecimal.ROUND_HALF_UP));
											//TODO 可退货金额
											//TODO 可退数量
											//orders1.setCanRefMoney(orders1.getRefMoney()); //可退货金额
											
											temp = temp.subtract(orders1.getRefMoney());
												
										}
										if(order.getAllRefNum() == 0) {
											// 设置退货数量
											orders1.setRefNum(0);
											// 设置可退数量
											orders1.setCanRefNum(0);
										} else {
											// 设置退货数量
											orders1.setRefNum(new BigDecimal(orders.getRefNum())
													.multiply(new BigDecimal(strucksublist.get(0).getSubNm())).intValue());
										}
										orders1.setCanRefNum(orders.getRefNum());
										
										orders1.setRefMoney(new BigDecimal(dFormat.format(orders.getRefMoney().multiply(rate))));
																												
										orders1.setGoodId(invtyDoc2.getInvtyEncd());// 设置对应母件编码
										orders1.setGoodName(invtyDoc2.getInvtyNm());// 设置对应母件名称	
										
										ordersList.add(orders1);
										
										flag = true;
										
									}
								}

							}
						}
					} else {
						//单件产品
						ordersList.add(orders);
					}
						
				} else {
					// 存货档案pto属性为空
					order.setAuditHint("存货档案中PTO属性为空");			
					flag = false;
				}
			}
		}
		if(!flag) {
			ordersList.add(orders);
		}
		order.setRefundOrders(ordersList);
		
		return order;
	
	}
	/**
	 * 拼多多-根据订单号下载订单
	 */
	@Transactional
	public Map<String,Object> pddDownloadByOrderCode(String orderCode, String userId, String storeId,int downloadCount) throws Exception {
		
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		// 设置
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		// 信息
		StoreRecord storeRecord = storeRecordDao.select(storeId);

		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddOrderInformationGetRequest request = new PddOrderInformationGetRequest();
		request.setOrderSn(orderCode);
		PddOrderInformationGetResponse response = client.syncInvoke(request, accessToken);
		
		//JSONObject object = JSONObject.parseObject(JsonUtil.transferToJson(response));
		if (response.getErrorResponse() != null) {
					
			logger.error("订单下载:拼多多平台查询订单异常,异常信息:{}",response.getErrorResponse().getErrorMsg());
			message = "订单下载:拼多多平台查询订单异常,异常信息:"+response.getErrorResponse().getErrorMsg();
			isSuccess = false;	
		}
		if(response.getOrderInfoGetResponse() != null) {
			OrderInfoGetResponseOrderInfo order = response.getOrderInfoGetResponse().getOrderInfo();
			
			if(order != null) {
				PlatOrder platOrder = new PlatOrder();
				String orderId = getOrderNo.getSeqNo("ec", userId); //生成品星内部订单号
				//仓库信息
				platOrder.setPayTime(order.getPayTime()); //支付时间
				platOrder.setIsInvoice(order.getInvoiceStatus()); // 是否开票 1代表有 0代表无
				
				//TODO 获取发票信息api pdd.invoice.application.query
				platOrder.setBuyerNote(order.getBuyerMemo());// 买家留言
				platOrder.setSellerNote(order.getRemark());// 备注 卖家
				
				platOrder.setProvince(order.getProvince()); // 省
				platOrder.setProvinceId(String.valueOf(order.getProvinceId())); // 省id
				platOrder.setCity(order.getCity()); // 市 
				platOrder.setCityId(String.valueOf(order.getCityId())); //
				platOrder.setCounty(order.getTown()); // 县
				platOrder.setCountyId(String.valueOf(order.getTownId())); //
				platOrder.setRecAddress(order.getAddress()); //收货人详细地址
				platOrder.setRecName(order.getReceiverName()); // 收货人姓名
				platOrder.setRecMobile(order.getReceiverPhone()); // 收货人手机号
				
				//确认收货时间	
					
				platOrder.setEcOrderId(order.getOrderSn());// 电商订单号
				platOrder.setOrderId(orderId); // 订单编号
				platOrder.setStoreId(storeId); // 店铺编号 
				platOrder.setStoreName(storeRecord.getStoreName()); // 店铺名称
				
				platOrder.setTradeDt(order.getCreatedTime()); //交易时间 创建时间
				
				platOrder.setIsAudit(0); // 是否客审

				platOrder.setGoodMoney(new BigDecimal(order.getGoodsAmount()));  // 商品金额=商品销售价格*商品数量-改价金额(接口暂无该字段)


				platOrder.setIsClose(0); // 是否关闭
				platOrder.setIsShip(order.getOrderStatus() == 1 ? 0 : 1); // 是否发货 发货状态，枚举值：1：待发货，2：已发货待签收，3：已签收


				platOrder.setOrderStatus(0); //订单状态
				platOrder.setReturnStatus(0); //退货状态
				platOrder.setHasGift(0); //是否含赠品
				
				
				platOrder.setSellTypId("1");// 设置销售类型普通销售
				platOrder.setBizTypId("2");// 设置业务类型2c		
				
				platOrder.setFreightPrice(new BigDecimal(order.getPostage())); // 运费
				platOrder.setVenderId("PDD"); // '商家id'
				platOrder.setDownloadTime(sf.format(new Date()));
				
				platOrder.setDeliveryType(String.valueOf(3)); //送货（日期）类型（1-只工作日送货(双休日、假日不用送;2-只双休日、假日送货(工作日不用送;3-工作日、双休日与假日均可送货;其他值-返回“任意时间”
				if(StringUtils.isNotEmpty(order.getShippingTime())) {
					platOrder.setShipTime(order.getShippingTime()); //发货时间	
				}	
				platOrder.setPayMoney(new BigDecimal(order.getPayAmount()));  //实付金额

				platOrder.setOrderSellerPrice(new BigDecimal(order.getPayAmount())); // 结算金额（订单实际收入金额）支付金额（元）支付金额=商品金额-折扣金额+邮费
				//discount_amount 折扣金额（元）折扣金额=平台优惠+商家优惠+团长免单优惠金额
				//capital_free_discount   团长免单优惠金额，只在团长免单活动中才会返回优惠金额
				
				// 卖家调整金额  店铺优惠金额+团长免单优惠金额
				platOrder.setAdjustMoney(new BigDecimal(order.getSellerDiscount()).add(new BigDecimal(order.getCapitalFreeDiscount()))); 
						
				platOrder.setDiscountMoney(new BigDecimal(order.getPlatformDiscount())); // 系统优惠金额  平台优惠金额
				
				//优惠list
				List<CouponDetail> couponList = new ArrayList<>();
		
				//店铺优惠
				if(new BigDecimal(order.getSellerDiscount()).compareTo(BigDecimal.ZERO) != 0) {
					CouponDetail storeCoup = getCouponDetail(storeId,order.getOrderSn(),"店铺优惠",new BigDecimal(order.getSellerDiscount()));
					couponList.add(storeCoup);
				}
				
				//计算支付金额 实际支付金额 = 商品金额-店铺优惠金额 zz
				BigDecimal payMoney = platOrder.getGoodMoney().subtract(platOrder.getAdjustMoney());
				
				
				//支付方式list
				List<PlatOrderPaymethod> payMentList = new ArrayList<>();
				PlatOrderPaymethod payMent = getPlatOrderPaymethod(orderId,order,storeId,payMoney);
				payMentList.add(payMent);
					
				//商品list
				List<PlatOrders> ordersList = getPlatOrders(order,platOrder,orderId);
				
				//计算支付金额,商品金额,支付金额,平均单价,分摊金额 未拆装组套产品	
				
				ordersList = countAveragePrice(ordersList,payMoney); 
				
				//检查商品是否为组套产品
				ordersList = checkPlatOrdersList(platOrder,ordersList,payMoney);
				
				platOrder.setGoodNum(ordersList.size()); //商品数量
				int c = 0;
				if(couponList.size() > 0) {
					c = couponDetailDao.insert(couponList);
				}
				int p = 0;
				if(payMentList.size() > 0) {
					p = paymethodDao.insert(payMentList);
					
				}
				int po = 0;
				if(platOrder != null) {
					downloadCount ++ ;
					po = platOrderDao.insert(platOrder);
				}
				int  pos = 0;
				if(ordersList.size() > 0) {
					pos = platOrdersDao.insert(ordersList);
				}
				if(po > 0 && pos > 0) {
					/*
					 * // 订单插入完成后，判断是否自动免审 PlatOrder platorder =
					 * platOrderDao.selectByEcOrderId(orderCode); List<PlatOrders> orderslist =
					 * platOrdersDao.select(platorder.getOrderId()); if
					 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
					 * 返回true时，此订单通过免审，直接进入审核
					 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
					 * // 设置默认操作员sys } isSuccess = true; message = "下载成功";
					 */
					//执行自动匹配
					platOrderService.autoMatch(orderId, userId);
					isSuccess = true;
				}	
			} else {
				
				//未获取到订单信息
				logger.error("订单下载:拼多多平台未获取到订单明细");
				message = "订单下载:拼多多平台未获取到订单明细";
				isSuccess = false;
			}
					
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
					
		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess,message, null);
	
		Map<String,Object> map = new HashMap<>();
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		map.put("downloadCount", downloadCount);
		return map;
		
	}
	/**
	 * 检查商品是否为组套产品
	 * @throws CloneNotSupportedException 
	 */
	public List<PlatOrders> checkPlatOrdersList(PlatOrder platOrder,List<PlatOrders> orderslist, BigDecimal payMoney) throws CloneNotSupportedException {
		
		boolean flag = true;
		List<PlatOrders> orderslistNew = new ArrayList<>();
		if(orderslist.size() > 0) {
			
			for(int i=0;i<orderslist.size();i++) {
				DecimalFormat dFormat = new DecimalFormat("0.00");
				PlatOrders platOrders = orderslist.get(i);
				// 存货编码 平台商品表good_record中的平台商品编码
				String ecgoodid = platOrders.getGoodId();// 平台商品编号
				String goodsku = platOrders.getGoodSku();
				GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
				if (goodRecord == null) {
					platOrder.setAuditHint("在店铺商品档案中未匹配到对应商品档案");
					
					flag = false;
					orderslistNew.add(platOrders);
					continue;
				} else if (goodRecord.getGoodId() == null) {
					platOrder.setAuditHint("请完善店铺商品档案中存货编码的对应关系");
					
					flag = false;
					orderslistNew.add(platOrders);
					continue;
				} else {
					String invId = goodRecord.getGoodId();
					
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// 去存货档案查询存货基本信息
					if (invtyDoc == null) {
						platOrder.setAuditHint("在存货档案中未匹配到对应存货档案,需匹配的存货编码：" + invId);
						
						flag = false;
						orderslistNew.add(platOrders);
						continue;
					} else {

						if (invtyDoc.getPto() != null) {
							if (invtyDoc.getPto() == 1) {
								// 属于PTO类型存货
								ProdStru prodStru = prodStruDao.selectMomEncd(invId); //查询母件产品
								
								if (prodStru == null) {
									
									platOrder.setAuditHint("在产品结构中未匹配到对应产品结构信息");								
									flag = false;
									orderslistNew.add(platOrders);
									continue;
								} else {
									if (prodStru.getIsNtChk() == 0) {
										// 对应产品结构尚未审核
										platOrder.setAuditHint("对应产品结构尚未审核");
										flag = false;
										orderslistNew.add(platOrders);
										continue;
									} else {
										List<ProdStruSubTab> strucksublist = prodStru.getStruSubList();
										// 循环产品结构子表信息
										if (strucksublist.size() == 0) {
											platOrder.setAuditHint("对应产品结构没有设置子件明细，请先设置。");
											// platOrderDao.update(platOrder);//更新订单的审核提示；
											flag = false;
											orderslistNew.add(platOrders);
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

											orderslistNew.add(platOrders);
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

											for (int j = 0; j < strucksublist.size(); j++) {
												//TODO ---
												PlatOrders order = platOrders;
												BigDecimal money123 = new BigDecimal("0");// 设置已分实付金额 最后一条用减法时用到
												if (j + 1 < strucksublist.size()) {
													// 计算每条子件占总成本的比例 保留8位小数
													InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
															strucksublist.get(j).getSubEncd());
													BigDecimal rate = invtyDoc2.getRefCost()
															.multiply(strucksublist.get(j).getSubQty())
															.divide(total, 8, BigDecimal.ROUND_HALF_UP);
													order.setInvNum((new BigDecimal(order.getGoodNum()))
															.multiply(strucksublist.get(j).getSubQty()).intValue());
													order.setPayMoney(new BigDecimal(dFormat.format(order.getPayMoney().multiply(rate))));// 设置实付金额
																													// 保留两位小数
													// 计算实付单价 保留8位小数
													order.setPayPrice(
															order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																	8, BigDecimal.ROUND_HALF_UP));
													order.setSellerPrice(order.getPayPrice());
													// 存货编码
													order.setInvId(strucksublist.get(0).getSubEncd());
													// 设置可退数量
													order.setCanRefNum(order.getInvNum());
													// 设置可退金额
													order.setCanRefMoney(order.getPayMoney());
													order.setPtoCode(invtyDoc2.getInvtyEncd());// 设置对应母件编码
													order.setPtoName(invtyDoc2.getInvtyNm());// 设置对应母件名称
													if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
														order.setIsGift(1);
														platOrder.setHasGift(1);
													} else {// 设置是否赠品
														order.setIsGift(0);
													}
													orderslistNew.add(order);
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
													order.setInvId(strucksublist.get(0).getSubEncd());
													// 设置可退数量
													order.setCanRefNum(order.getInvNum());
													// 设置可退金额
													order.setCanRefMoney(order.getPayMoney());
													order.setPtoCode(invtyDoc.getInvtyEncd());// 设置对应母件编码
													order.setPtoName(invtyDoc.getInvtyNm());// 设置对应母件名称
													if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
														order.setIsGift(1);
														platOrder.setHasGift(1);
													} else {// 设置是否赠品
														order.setIsGift(0);
													}
													orderslistNew.add(order);
												}

											}
										}

									}
								}
							} else {
								//单件产品
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
								if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
									platOrders.setIsGift(1);
									platOrder.setHasGift(1);
								} else {// 根据实付单价是否为0判断是否为赠品
									platOrders.setIsGift(0);
								}
								// 设置可退金额,与实付金额一致
								platOrders.setCanRefMoney(platOrders.getPayMoney());

								orderslistNew.add(platOrders);
							}
								
						} else {
							// 存货档案pto属性为空
							platOrder.setAuditHint("存货档案中PTO属性为空");					
							flag = false;
							orderslistNew.add(platOrders);
							continue;
						}	
					}
				}
			}
		} else {
			return orderslist;
		}
		return orderslistNew;
	}

	/**
	 * 支付明细
	 */
	public PlatOrderPaymethod getPlatOrderPaymethod(String orderId,OrderInfoGetResponseOrderInfo order,String storeId, BigDecimal payMoney) {
		PlatOrderPaymethod payMent = new PlatOrderPaymethod();
		payMent.setPlatId("PDD");
		payMent.setStoreId(storeId);
		payMent.setOrderId(orderId); //电商平台编号
		payMent.setPayMoney(payMoney);  //支付金额
		payMent.setPaymoneyTime(order.getPayTime()); //支付时间
		payMent.setPaymentNumber(order.getPayNo()); //交易单号
		payMent.setPayWay(order.getPayType()); //支付方式
		payMent.setPayStatus("支付成功"); //支付状态 默认为支付成功
		return payMent;
	}

	/**
	 * 优惠明细
	 */
	private CouponDetail getCouponDetail(String storeId,String orderSn,String couponType,BigDecimal price) {
		CouponDetail coupon = new CouponDetail();
		coupon.setPlatId("PDD"); //平台编码
		coupon.setStoreId(storeId);//店铺编码
		coupon.setOrderId(orderSn); //店铺订单编号							
		coupon.setSkuId(null);
		coupon.setCouponCode(null);// 优惠类型编码
		coupon.setCouponType(couponType);// 优惠名称
		coupon.setCouponPrice(price);// 优惠金额
		return coupon;
	}
	
	/**
	 * 计算平均价
	 */
	public List<PlatOrders> countAveragePrice(List<PlatOrders> list,BigDecimal payMoney) {
		
		boolean isSuccess = true;
					
		BigDecimal payTotal = new BigDecimal(0.00);
		
		for(PlatOrders orders: list) {
			//应付总金额
			 payTotal = payTotal.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));
		}
		
		BigDecimal payTemp = payMoney;
		
		List<PlatOrders> orderList = new ArrayList<>();
		
		for(int i=0;i<list.size();i++) {
			PlatOrders orders = list.get(i);
			
			//支付金额为0
			if(payMoney.compareTo(BigDecimal.ZERO) == 0) {
				
				BigDecimal itemTotal = orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())); //商品应付		
				orders.setPayMoney(new BigDecimal(0.00)); //实付金额
				orders.setGoodMoney(itemTotal); //商品金额				
				orders.setPayPrice(new BigDecimal(0.00));//实付单价
				
			} else {
				//判断是否是最后一个
				if(i == list.size()-1) {
					
					//商品应付占比
					BigDecimal itemTotal = orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())); //商品应付
					BigDecimal itemFact = payTemp; //商品支付
					orders.setPayMoney(itemFact); //实付金额
					orders.setGoodMoney(itemTotal); //商品金额
					orders.setCanRefMoney(itemFact); //可退金额
					orders.setCanRefNum(orders.getGoodNum()); //可退数量
					
					orders.setPayPrice(itemFact.divide(new BigDecimal(orders.getGoodNum()),6,BigDecimal.ROUND_HALF_DOWN));//实付单价
					
				} else {
							
					//商品应付占比
					BigDecimal itemTotal = orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())); //商品应付
					BigDecimal itemFact = itemTotal.multiply(payMoney).divide(payTotal,2,BigDecimal.ROUND_HALF_DOWN); //商品支付
					orders.setPayMoney(itemFact); //实付金额
					orders.setGoodMoney(itemTotal); //商品金额
					orders.setCanRefMoney(itemFact); //可退金额
					orders.setCanRefNum(orders.getGoodNum()); //可退数量
					
					orders.setPayPrice(itemFact.divide(new BigDecimal(orders.getGoodNum()),6,BigDecimal.ROUND_HALF_DOWN));//实付单价
					payTemp = payTemp.subtract(itemFact);
					
				}
				orders.setDiscountMoney(orders.getGoodMoney().subtract(payMoney));//优惠金额
			}
			orderList.add(orders);
			isSuccess = true;
		}		
		
		logger.info("订单下载:平台订单下载计算平均单价执行结果:{}",isSuccess);
		return orderList;
	}
	
	private List<PlatOrders> getPlatOrders(OrderInfoGetResponseOrderInfo order,PlatOrder platOrder,String orderId) {
		List<PlatOrders> list = new ArrayList<PlatOrders>();
		
		List<OrderInfoGetResponseOrderInfoItemListItem> item_list = order.getItemList();
		
		for (int i = 0; i < item_list.size(); i++) {
			OrderInfoGetResponseOrderInfoItemListItem item = item_list.get(i);
			PlatOrders platOrders = new PlatOrders();
			platOrders.setGoodId(String.valueOf(item.getGoodsId())); // 平台商品编号
			platOrders.setGoodSku(String.valueOf(item.getSkuId())); //商品规格编码
			platOrders.setGoodPrice(new BigDecimal(item.getGoodsPrice())); //商品销售价格 pdd
			
				
			platOrders.setGoodName(item.getGoodsName()); // 平台商品名称
			platOrders.setGoodNum(item.getGoodsCount()); //商品数量
//			platOrders.setGoodMoney(new BigDecimal(item.getDouble("goods_price"))); // 商品金额 
		
			platOrders.setOrderId(orderId); // '订单编号' 1 	
			platOrders.setExpressCom(String.valueOf(order.getLogisticsId())); // 快递公司

			platOrders.setGoodPrice(new BigDecimal(item.getGoodsPrice())); //商品单价 
																								
			platOrders.setEcOrderId(order.getOrderSn()); // '平台订单号' t oid

			platOrder.setDeliverSelf(1); // '订单是否自发货，0平台仓发货，1自发货' t

			platOrders.setInvId(item.getOuterGoodsId()); // 存货编码商家外部编码

			list.add(platOrders);
		}
		return list;
	}
}
