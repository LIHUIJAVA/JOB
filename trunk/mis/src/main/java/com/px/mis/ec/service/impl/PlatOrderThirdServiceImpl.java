package com.px.mis.ec.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.px.mis.account.dao.FormBookEntryDao;
import com.px.mis.account.dao.FormBookEntrySubDao;
import com.px.mis.account.dao.InvtySendMthTermBgnTabDao;
import com.px.mis.account.dao.SellComnInvDao;
import com.px.mis.account.entity.FormBookEntry;
import com.px.mis.account.entity.FormBookEntrySub;
import com.px.mis.account.entity.InvtyMthTermBgnTab;
import com.px.mis.account.entity.SellComnInvSub;
import com.px.mis.account.service.impl.FormBookEntryUtil;
import com.px.mis.account.service.impl.FormBookServiceImpl;
import com.px.mis.ec.dao.CouponDetailDao;
import com.px.mis.ec.dao.InvoiceDao;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrderPaymethodDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.PlatWhsMappDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.entity.InvoiceInfo;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrderPaymethod;
import com.px.mis.ec.entity.PlatOrderThird;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.PlatOrdersThird;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.ec.service.OrderDealSettingsService;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.ec.service.PlatOrderThirdService;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.service.SellOutWhsService;
import com.px.mis.purc.service.SellSnglService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;



@Service
public class PlatOrderThirdServiceImpl implements PlatOrderThirdService {
	
	private Logger logger = LoggerFactory.getLogger(PlatOrderThirdServiceImpl.class);
	
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private InvoiceDao invoiceDao;
	@Autowired
	private CouponDetailDao couponDetailDao;
	@Autowired
	private PlatOrderPaymethodDao paymethodDao;
	@Autowired
	private SellSnglDao sellSnglDao;
	@Autowired
	private SellOutWhsDao sellOutWhsDao;
	@Autowired
	private SellOutWhsService sellOutWhsService; //销售出库单
	@Autowired
	private SellSnglService sellSnglService; //销售单
	@Autowired
	private LogisticsTabDao logisticsTabDao;//物流表
	@Autowired
	private AssociatedSearchService associatedService; //订单
	@Autowired
	private PlatOrderService platOrderService; //平台订单
	@Autowired
	private PlatOrderPdd platOrderPdd;
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	private OrderDealSettingsService orderDealSettingsService;
	@Autowired
	private FormBookServiceImpl formBookServiceImpl;
	@Autowired
	private FormBookEntryDao formBookEntryDao; //记账主表
	@Autowired
	private FormBookEntrySubDao formBookEntrySubDao; //记账zi表
	@Autowired
	private PlatOrderMaiDu platOrderMaiDu;
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; //发出商品各月期初
	@Autowired
	private SellSnglSubDao sellSnglSubDao; //销售子单
	@Autowired
	private SellComnInvDao sellComnInvDao; //发票
	@Autowired
	private FormBookEntryUtil formBookEntryUtil;
	@Override
	public String testPddOrder() {
		String resp = "";
		try {
			//resp = platOrderPdd.pddDownload("guoguo",1,2,"2019-05-12 00:00:00","2019-05-16 23:59:59","008");
			 //resp = platOrderPdd.pddRefund("guoguo",1,2,"2019-08-14 18:10:00","2019-08-14 18:30:59","008");
			//resp = platOrderSN.snDownload("guoguo", 1, 3, "2019-05-31 17:43:11", "2019-06-11 13:59:59", "021179");
			//resp = platOrderSN.snRefund("guoguo", 1, 1, "2019-05-31 17:43:11", "2019-06-02 13:59:59", "021179");
			//resp = platOrderSN.getSNSku("guoguo", 1, 1, "2019-05-31 17:43:11", "2019-06-10 13:59:59", "021179");
			//PlatOrder platOrder = platOrderDao.select("ecroot201906200000992");
			//platOrder.setPlatOrdersList(platOrdersDao.select("ecroot201906200000992"));
			/*Map<String,Object> map = orderDealSettingsService.matchWareHouse(platOrder, "TM");
			for(String key:map.keySet()){
				
				String value = map.get(key).toString();//
				System.out.println("key:"+key+" vlaue:"+value);
			}
			*/
			//platOrderMaiDu.maiDuDownload("guoguo", 1, 10, startDate, endDate, storeId)
			//List<String> formCodeList = new ArrayList<>();
			//formCodeList.add("10");
			
			
			
			//resp = formBookEntryUtil.formBookEntryAss2(formList,"root",loginTime);
			
			//formBookServiceImpl.backFormBook("", "root", loginTime);
			//Boolean is=formBookServiceImpl.dealSendProductMth(formBookEntryDao.selectMap(paramMap),loginTime,"2007");
			
			//dealSend(loginTime,paramMap);
			//System.out.println("---->");
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			List<FormBookEntrySub> listA = formBookEntryDao.selectA();
//			List<FormBookEntrySub> listB = formBookEntryDao.selectB();
//			List<FormBookEntrySub> li = new ArrayList<>();
//			for(FormBookEntrySub f :listB) {
//				
//				f.setQty(Optional.ofNullable(f.getQty()).orElse(BigDecimal.ZERO));
//				if(!paramMap.containsKey(f.getWhsEncd()+f.getInvtyEncd()+f.getBatNum())) {	
//					paramMap.put(f.getWhsEncd()+f.getInvtyEncd()+f.getBatNum(),f.getQty());
//				}
//			}
//			for(FormBookEntrySub f :listA) {
//				if(paramMap.containsKey(f.getWhsEncd()+f.getInvtyEncd()+f.getBatNum())) {
//					BigDecimal num = (BigDecimal)paramMap.get(f.getWhsEncd()+f.getInvtyEncd()+f.getBatNum());
//					if(num.compareTo(f.getQty()) != 0) {
//						System.out.println(f.getWhsEncd()+"-->"+f.getInvtyEncd()+"-->"+f.getBatNum()+"-->"+f.getQty()+"--单据数量->"+num);
//						li.add(f);
//					}
//				}
//			}
//			if(li.size() > 0) {
//				formBookEntrySubDao.updateSubj(li);
//			}
			//String loginTime = "2019-01-31 00:00:00";
			//List<FormBookEntry> formList = new ArrayList<>();
			//dealFormBook1(formList,loginTime,"root");
			formBookEntryUtil.dealInvtyTab("2020-01-31 00:00:00");
			 
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return resp;
	}
	
	
	
	private void dealFormBook1(List<FormBookEntry> formList,String loginTime,String str) throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isNtBookOk", "0");
		paramMap.put("loginTime", loginTime);
		paramMap.put("index", 0);
		paramMap.put("num", 2000);
		paramMap.put("isPage", "1");
		formList = formBookEntryDao.selectStreamALLList(paramMap);
		
		if(formList.size() > 0) {
			List<List<FormBookEntry>> list = Lists.partition(formList, 1000);
			for (List<FormBookEntry> list2 : list) {
				formBookServiceImpl.formBook(list2, "root", loginTime); 
			}
			formList = formBookEntryDao.selectStreamALLList(paramMap);
			if(formList.size() > 0) {
				dealFormBook1(formList,loginTime,"root");
			}
			
		}
			
		
	}
	public void dealSend(String loginTime,Map<String, Object> paramMap) throws Exception {
		paramMap.put("year", loginTime.substring(0, 4));
		paramMap.put("month", loginTime.substring(5, 7));
		//paramMap.put("isMthEndStl",0);
		List<InvtyMthTermBgnTab> sendMthList = invtySendMthTermBgnTabDao.selectSendMthByMthTerm(paramMap);
		List<InvtyMthTermBgnTab> sendNextMthList = new ArrayList<>();
		
		if(sendMthList.size() > 0) {
			//处理开在本月的发票数量和金额
			paramMap.put("loginTime", loginTime);
			paramMap.put("bookDt",loginTime);
			List<SellComnInvSub> sellList = sellComnInvDao.selectUnBllgQtyAndAmt(paramMap); 
			Map<String,SellComnInvSub> sellMap = new HashMap<>();
			if(sellList.size() > 0) {
				for(SellComnInvSub sub : sellList) {
					String key = sub.getOutWhsId()+"-"+sub.getInvtyEncd()+"-"+sub.getWhsEncd()+"-"+sub.getBatNum();
					
					if(!sellMap.containsKey(key)) {
						sellMap.put(key, sub);
					}
				}
			}
			
			
			for(InvtyMthTermBgnTab tab : sendMthList) {
							
				String key = tab.getCustId()+"-"+tab.getInvtyEncd()+"-"+tab.getWhsEncd()+"-"+tab.getBatNum();
				
				if(sellMap.containsKey(key)) {
					
					SellComnInvSub sell = sellMap.get(key);
					
					if(sell != null) {
						tab.setSendMoeny(tab.getSendMoeny().add(sell.getNoTaxAmt()));
						tab.setSendQty(tab.getSendQty().add(sell.getQty()));
						if(tab.getSendQty().compareTo(BigDecimal.ZERO) == 0) {
							tab.setSendUnitPrice(new BigDecimal(0.00000000));
						 } else {
							 tab.setSendUnitPrice(tab.getSendMoeny().divide(tab.getSendQty(),8,BigDecimal.ROUND_HALF_UP).abs());
						}
						BigDecimal othSendQty = tab.getQty().add(tab.getInQty()).subtract(tab.getSendQty());						
						BigDecimal othSendMoeny = tab.getAmt().add(tab.getInMoeny()).subtract(tab.getSendMoeny());
						BigDecimal uprc = new BigDecimal(0);
						if(othSendQty.compareTo(BigDecimal.ZERO) == 0) {
							uprc = new BigDecimal(0.00000000);
						} else {
							uprc = othSendMoeny.divide(othSendQty,8,BigDecimal.ROUND_HALF_UP).abs();
						}
						tab.setOthMoeny(othSendMoeny);
						tab.setOthQty(othSendQty);
						tab.setOthUnitPrice(uprc);
					}
				}
			
				//生成下月的发出商品
				if(BigDecimal.ZERO.compareTo(tab.getOthQty()) != 0 || BigDecimal.ZERO.compareTo(tab.getOthMoeny()) != 0) {
					InvtyMthTermBgnTab next = new InvtyMthTermBgnTab();
					next = formBookServiceImpl.generateNextMth(next,tab);
					sendNextMthList.add(next);
				}
					
				tab.setIsMthEndStl(1);
			}
			invtySendMthTermBgnTabDao.updateSendMthList(sendMthList);
			if(sendNextMthList.size()> 0) {
				invtySendMthTermBgnTabDao.insertMthList(sendNextMthList);
			}
			
		}
	}
	
	@Override
	public String exportPlatOrderThird(String userId,String platId,String storeId, List<PlatOrderThird> paltOrderThirdList) {
	
		String message = "导入完成!";
		boolean success = true;
		try {
			if(paltOrderThirdList.size() == 0) {
				message = "导入订单数据为空";
				success = false;
			} else {
				List<PlatOrder> platOrderList = new ArrayList<>();
				List<PlatOrders> platOrdersList = new ArrayList<>();
				List<InvoiceInfo> invoiceList = new ArrayList<>();
				List<CouponDetail> couponList = new ArrayList<>();
				List<PlatOrderPaymethod> payMentList = new ArrayList<>();
				//订单明细表
				for(PlatOrderThird plat : paltOrderThirdList) {
					//判断订单号是否存在
					String ecOrderId = plat.getOrderCode(); //电商订单号
					if(platOrderDao.checkExsits(ecOrderId,storeId) > 0) {
						message = "平台对应订单不可重复";
						success = false;
						break;
					} else {
						
						int goodNum = 0;				
						
						PlatOrder platOrder = new PlatOrder();
						String orderId = getOrderNo.getSeqNo("ec", userId,""); //生成平台内的订单编号

						if(plat.getPlatOrdersList().size() == 0) {
							message = "导入商品数据不能为空";
							success = false;
							break;
						} else {
							//商品明细表
							for(PlatOrdersThird ordersThird : plat.getPlatOrdersList()) {
								
								PlatOrders platOrders = new PlatOrders();
								platOrders.setGoodId(ordersThird.getGoodId());// 店铺商品编码
								platOrders.setGoodNum(ordersThird.getItemTotal());// 商品数量
								platOrders.setGoodName(ordersThird.getSkuName());//平台商品名称
								
								platOrders.setGoodPrice(ordersThird.getUnitPrice());// 商品单价 
								platOrders.setPayPrice(ordersThird.getPayPrice());// 商品成交价 
								platOrders.setDiscountMoney(ordersThird.getDiscountMoney()); //优惠价格
								platOrders.setGoodMoney(ordersThird.getGoodMoney());// 商品应付金额
								platOrders.setPayMoney(ordersThird.getPayMoney());// 商品实付金额
								
								platOrders.setGoodSku(ordersThird.getSkuId());// 商品sku
								platOrders.setOrderId(orderId); //平台订单编号
								platOrders.setEcOrderId(ecOrderId); //电商订单号
								
								platOrdersList.add(platOrders);
								
								goodNum += ordersThird.getItemTotal();
								
							}
													
							//订单优惠明细
							if(plat.getSellerDiscount() > 0) {
								if(plat.getCouponDetailList().size() == 0) {
									message = "订单优惠明细不能为空";
									success = false;
									break;
								} else {
									
									for(CouponDetail coupons : plat.getCouponDetailList()) {
										System.out.println("_________优惠明细："+coupons.getCouponType());
										CouponDetail coupon = new CouponDetail();
										coupon.setPlatId(platId); //平台编码
										coupon.setStoreId(storeId);//店铺编码
										coupon.setOrderId(ecOrderId); //店铺订单编号
										/*
										 * if(StringUtils.isNotEmpty(coupons.getSkuId())) {
										 * coupon.setSkuId(coupons.getSkuId());//SKU编号 }
										 */
										coupon.setCouponType(coupons.getCouponType());//优惠类型
										coupon.setCouponCode(coupons.getCouponCode()); //优惠类型编码
										coupon.setCouponPrice(coupons.getCouponPrice());//优惠金额							
									
										couponList.add(coupon);
									}
								}
									
							}	
							//付款方式
							//TODO 付款方式
							if(plat.getPayMethodList().size() == 0) {
								message = "付款方式明细为空";
								success = false;
								break;
							} else {
								for(PlatOrderPaymethod pays : plat.getPayMethodList()) {
									PlatOrderPaymethod payMent = new PlatOrderPaymethod();
									payMent.setPlatId(platId);
									payMent.setStoreId(storeId);
									payMent.setOrderId(orderId); //平台编号
									payMent.setPayMoney(pays.getPayMoney());  //支付金额
									payMent.setPaymoneyTime(pays.getPaymoneyTime()); //支付时间
									payMent.setPaymentNumber(pays.getPaymentNumber()); //交易单号
									payMent.setPayWay(pays.getPayWay()); //支付方式
									payMent.setPayStatus("支付成功"); //支付状态 默认为支付成功
									payMentList.add(payMent);
								}
							}
							// 是否赠品 
							platOrder.setHasGift(0);// 是否含赠品，0不含，1含
							
							StoreRecord storeRecord = storeRecordDao.select(storeId);
							platOrder.setOrderId(orderId);// 订单编号
							platOrder.setStoreId(storeId);// 店铺编号
							platOrder.setEcOrderId(ecOrderId);// 店铺编号
							platOrder.setStoreName(storeRecord.getStoreName());// 店铺名称
							platOrder.setPayTime(plat.getPaymentConfirmTime());// 付款时间
							platOrder.setTradeDt(plat.getOrderStartTime());//下单时间
							platOrder.setIsAudit(0);// 是否审核，0未审核，1审核
							platOrder.setGoodNum(goodNum);// 商品总数量
							//platOrder.setDeliveryType(plat.getDeliveryType());//设置送货类型
							platOrder.setVenderId(plat.getVenderCode());//设置商家ID商家编码									
							platOrder.setSellerNote(plat.getVenderRemark());// 商家备注
									
							platOrder.setGoodMoney(plat.getOrderTotalPrice());// 订单总金额
							platOrder.setPayMoney(plat.getOrderPayment());// 买家实际付款金额
							// 是否分销 目前默认为非分销订单
							//platOrder.setOrderSellerPrice(plat.getOrderSellerPrice());//结算金额
							platOrder.setFreightPrice(plat.getFreightPrice()); //运费金额
							
							platOrder.setBuyerId(plat.getPin());// 买家会员号
							platOrder.setBuyerNote(plat.getOrderRemark());// 买家备注
							platOrder.setRecAddress(plat.getFullAddress());// 收货人详细地址
							platOrder.setRecName(plat.getFullname());// 收货人姓名
							platOrder.setRecMobile(plat.getMobile());// 收货人手机号
							platOrder.setProvince(plat.getProvince());//收货人省
							platOrder.setProvinceId(plat.getProvinceId());//收货人省id
							platOrder.setCity(plat.getCity());//收货人市
							platOrder.setCityId(plat.getCityId());//收货人市id
							platOrder.setCounty(plat.getCounty());//收货人区县
							platOrder.setCountyId(plat.getCountyId());//收货人区县id
							//platOrder.setDeliverWhs(); //发货仓库
							platOrder.setSellTypId("1");//设置销售类型普通销售
							platOrder.setBizTypId("2");//设置业务类型2c
							
							
							platOrder.setIsShip(1);// 是否发货 
							platOrder.setDeliverSelf(0);//设置订单为平台发货订单
							platOrder.setNoteFlag(0);// 商家备注旗帜，默认值，表示无旗帜
							platOrder.setIsClose(0);// 是否关闭
							platOrder.setIsInvoice(0);// 是否开发票
							//platOrder.setIsShip(0);// 是否发货
							//platOrder.setAdjustMoney(new BigDecimal(orderInfo.get("balanceUsed").asDouble()));// 商家调整金额
							platOrder.setAdjustMoney(new BigDecimal(0));
							platOrder.setDiscountMoney(new BigDecimal(plat.getSellerDiscount()));// 优惠金额
											
							platOrder.setOrderStatus(0);// 订单状态
							platOrder.setReturnStatus(0);// 退货状态
							
						}				
						platOrderList.add(platOrder);
					}	
					
				}
				if(platOrderList.size() > 0) {
					platOrderDao.insertList(platOrderList);
					platOrdersDao.insert(platOrdersList);
				}
				if(invoiceList.size() > 0) {
					invoiceDao.insert(invoiceList);
				}
				if(couponList.size() > 0) {
					couponDetailDao.insert(couponList);
				}
				if(payMentList.size() > 0) {
					
					paymethodDao.insert(payMentList);
				}
			}
			message = BaseJson.returnRespObj("ec/platOrder/other/export", success, message, null);
			
		} catch (Exception e) {
			logger.error("URL：ec/platOrder/other/export 异常说明：", e);
			throw new RuntimeException();
		}
		return message;
		
	}

	@Override
	public String recallPlatOrderThird(String ecorderId, String storeId, String userId) {
		
		//撤销订单
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		int flag = 0; //1.订单删除 2.订单弃审 3.销售单弃审 4.销售出库单弃审 5.无法撤销
		
		List<SellSngl> sellSnglList = new ArrayList<>(); //弃审销售单集合
		List<SellOutWhs> sellOutWhsList = new ArrayList<>(); //弃审销售出库单集合
	
		try {
			PlatOrder platOrder = platOrderDao.selectByEcOrderId(ecorderId);
			String orderId = "";
			if(platOrder == null) {
				
				message = "订单编号"+ecorderId+"不存在,操作失败";
				isSuccess = false;
				
			} else {
				orderId = platOrder.getOrderId(); //平台订单编号
				
				//订单已审核
				if(platOrder.getIsAudit() == 0) {
					//0未审核 删除订单
					flag = 1; //订单删除
				} else {
					//销售单
					SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderId);
					if(sellSngl == null) {
						
						flag = 2; //订单弃审	
						
					} else if(sellSngl.getIsNtChk() == 0){
						
						//弃审销售单
						sellSnglList.add(sellSngl); //弃审销售单集合	
						flag = 3; //订单+销售单弃审
						
					} else {
						
						//销售出库单
						String sellSnglId = sellSngl.getSellSnglId(); //销售单编号
						SellOutWhs sellOutWhs = sellOutWhsDao.selectSellOutWhsBySellSnglId(sellSnglId);
						if(sellOutWhs == null) {
							
							//弃审销售单
							sellSngl.setIsNtChk(0); //销售单状态改为 0未审核
							sellSnglList.add(sellSngl); //弃审销售单集合	
							flag = 3;
							
						} else if(sellOutWhs.getIsNtChk() == 0) {
							
							//销售出库单未审核
							sellSnglList.add(sellSngl); //弃审销售单集合	
							sellOutWhsList.add(sellOutWhs); //弃审销售出库单集合	
							flag = 4;
							
						} else {
							//物流表
							LogisticsTab logisticsTab =  logisticsTabDao.selectByOrderId(orderId);
							if(logisticsTab == null || StringUtils.isEmpty(String.valueOf(logisticsTab.getOrdrNum()))) {
								
								//物流表为空或物流单号为空
								sellSnglList.add(sellSngl); //弃审销售单集合	
								sellOutWhsList.add(sellOutWhs); //弃审销售出库单集合	
								flag = 4;			
							} else {
								//已发货
								//无法撤销
								flag = 5;
							}
							
						} 
					}
				}
				String delMess = "";
				if(flag == 5){
						
					message = "订单编号"+ecorderId+"已发货,操作失败";
					isSuccess = false;
					
				} else if(flag == 4) {
					//弃审	
					//销售出库单
					//delMess = sellOutWhsService.updateSellOutWhsIsNtChk(sellOutWhsList);
					if(delMess.contains("true")) {
						//销售单
						//delMess = sellSnglService.updateSellSnglIsNtChkList(userId, sellSnglList);
						if(delMess.contains("true")) {
							//订单
							delMess = associatedService.orderAbandonAuditByOrderId(orderId,userId);
							if(delMess.contains("true")) {
								//订单删除
								//TODO 付款方式删除
								//delMess = platOrderService.delete(orderId);
								if(delMess.contains("true")) {
									message = "订单编号"+ecorderId+"已取消";
									isSuccess = true;
								}
							}
						}
					} else {
						message = "订单编号"+ecorderId+"操作失败";
						isSuccess = false;
					}
							
				} else if(flag == 3){
					
					//销售单
					//delMess = sellSnglService.updateSellSnglIsNtChkList(userId, sellSnglList);
					if(delMess.contains("true")) {
						//订单
						delMess = associatedService.orderAbandonAuditByOrderId(orderId,userId);
						if(delMess.contains("true")) {
							//订单删除
							//delMess = platOrderService.delete(orderId);
							if(delMess.contains("true")) {
								resp = delMess;
								isSuccess = true;
							}
						}
					} else {
						message = "订单编号"+ecorderId+"操作失败";
						isSuccess = false;
					}
				} else if(flag == 2) {
					
					//弃审
					delMess = associatedService.orderAbandonAuditByOrderId(orderId,userId);
					if(delMess.contains("true")) {
						//订单删除
						//delMess = platOrderService.delete(orderId);
						if(delMess.contains("true")) {
							message = "订单编号"+ecorderId+"已取消";
							isSuccess = true;
						}
					} else {
						message = "订单编号"+ecorderId+"操作失败";
						isSuccess = false;
					}
					
				} else if(flag == 1) {
					
					//订单删除
					//delMess = platOrderService.delete(ecorderId);
					if(delMess.contains("true")) {
									
						message = "订单编号"+ecorderId+"已取消";
						isSuccess = true;
						
					} else {
						message = "订单编号"+ecorderId+"操作失败";
						isSuccess = false;
					}
				} else {
					message = "订单编号"+ecorderId+"操作失败";
					isSuccess = false;
				}
				
				
			}
			System.out.println(message);
			resp = BaseJson.returnRespObj("mis/ec/platOrder/other/recall", isSuccess, message, null);
		} catch(Exception e) {
			logger.error("URL：mis/ec/platOrder/other/recall 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
