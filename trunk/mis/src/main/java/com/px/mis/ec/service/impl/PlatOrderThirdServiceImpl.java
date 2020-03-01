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
	private SellOutWhsService sellOutWhsService; //���۳��ⵥ
	@Autowired
	private SellSnglService sellSnglService; //���۵�
	@Autowired
	private LogisticsTabDao logisticsTabDao;//������
	@Autowired
	private AssociatedSearchService associatedService; //����
	@Autowired
	private PlatOrderService platOrderService; //ƽ̨����
	@Autowired
	private PlatOrderPdd platOrderPdd;
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	private OrderDealSettingsService orderDealSettingsService;
	@Autowired
	private FormBookServiceImpl formBookServiceImpl;
	@Autowired
	private FormBookEntryDao formBookEntryDao; //��������
	@Autowired
	private FormBookEntrySubDao formBookEntrySubDao; //����zi��
	@Autowired
	private PlatOrderMaiDu platOrderMaiDu;
	@Autowired
	private InvtySendMthTermBgnTabDao invtySendMthTermBgnTabDao; //������Ʒ�����ڳ�
	@Autowired
	private SellSnglSubDao sellSnglSubDao; //�����ӵ�
	@Autowired
	private SellComnInvDao sellComnInvDao; //��Ʊ
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
//						System.out.println(f.getWhsEncd()+"-->"+f.getInvtyEncd()+"-->"+f.getBatNum()+"-->"+f.getQty()+"--��������->"+num);
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
			//�����ڱ��µķ�Ʊ�����ͽ��
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
			
				//�������µķ�����Ʒ
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
	
		String message = "�������!";
		boolean success = true;
		try {
			if(paltOrderThirdList.size() == 0) {
				message = "���붩������Ϊ��";
				success = false;
			} else {
				List<PlatOrder> platOrderList = new ArrayList<>();
				List<PlatOrders> platOrdersList = new ArrayList<>();
				List<InvoiceInfo> invoiceList = new ArrayList<>();
				List<CouponDetail> couponList = new ArrayList<>();
				List<PlatOrderPaymethod> payMentList = new ArrayList<>();
				//������ϸ��
				for(PlatOrderThird plat : paltOrderThirdList) {
					//�ж϶������Ƿ����
					String ecOrderId = plat.getOrderCode(); //���̶�����
					if(platOrderDao.checkExsits(ecOrderId,storeId) > 0) {
						message = "ƽ̨��Ӧ���������ظ�";
						success = false;
						break;
					} else {
						
						int goodNum = 0;				
						
						PlatOrder platOrder = new PlatOrder();
						String orderId = getOrderNo.getSeqNo("ec", userId,""); //����ƽ̨�ڵĶ������

						if(plat.getPlatOrdersList().size() == 0) {
							message = "������Ʒ���ݲ���Ϊ��";
							success = false;
							break;
						} else {
							//��Ʒ��ϸ��
							for(PlatOrdersThird ordersThird : plat.getPlatOrdersList()) {
								
								PlatOrders platOrders = new PlatOrders();
								platOrders.setGoodId(ordersThird.getGoodId());// ������Ʒ����
								platOrders.setGoodNum(ordersThird.getItemTotal());// ��Ʒ����
								platOrders.setGoodName(ordersThird.getSkuName());//ƽ̨��Ʒ����
								
								platOrders.setGoodPrice(ordersThird.getUnitPrice());// ��Ʒ���� 
								platOrders.setPayPrice(ordersThird.getPayPrice());// ��Ʒ�ɽ��� 
								platOrders.setDiscountMoney(ordersThird.getDiscountMoney()); //�Żݼ۸�
								platOrders.setGoodMoney(ordersThird.getGoodMoney());// ��ƷӦ�����
								platOrders.setPayMoney(ordersThird.getPayMoney());// ��Ʒʵ�����
								
								platOrders.setGoodSku(ordersThird.getSkuId());// ��Ʒsku
								platOrders.setOrderId(orderId); //ƽ̨�������
								platOrders.setEcOrderId(ecOrderId); //���̶�����
								
								platOrdersList.add(platOrders);
								
								goodNum += ordersThird.getItemTotal();
								
							}
													
							//�����Ż���ϸ
							if(plat.getSellerDiscount() > 0) {
								if(plat.getCouponDetailList().size() == 0) {
									message = "�����Ż���ϸ����Ϊ��";
									success = false;
									break;
								} else {
									
									for(CouponDetail coupons : plat.getCouponDetailList()) {
										System.out.println("_________�Ż���ϸ��"+coupons.getCouponType());
										CouponDetail coupon = new CouponDetail();
										coupon.setPlatId(platId); //ƽ̨����
										coupon.setStoreId(storeId);//���̱���
										coupon.setOrderId(ecOrderId); //���̶������
										/*
										 * if(StringUtils.isNotEmpty(coupons.getSkuId())) {
										 * coupon.setSkuId(coupons.getSkuId());//SKU��� }
										 */
										coupon.setCouponType(coupons.getCouponType());//�Ż�����
										coupon.setCouponCode(coupons.getCouponCode()); //�Ż����ͱ���
										coupon.setCouponPrice(coupons.getCouponPrice());//�Żݽ��							
									
										couponList.add(coupon);
									}
								}
									
							}	
							//���ʽ
							//TODO ���ʽ
							if(plat.getPayMethodList().size() == 0) {
								message = "���ʽ��ϸΪ��";
								success = false;
								break;
							} else {
								for(PlatOrderPaymethod pays : plat.getPayMethodList()) {
									PlatOrderPaymethod payMent = new PlatOrderPaymethod();
									payMent.setPlatId(platId);
									payMent.setStoreId(storeId);
									payMent.setOrderId(orderId); //ƽ̨���
									payMent.setPayMoney(pays.getPayMoney());  //֧�����
									payMent.setPaymoneyTime(pays.getPaymoneyTime()); //֧��ʱ��
									payMent.setPaymentNumber(pays.getPaymentNumber()); //���׵���
									payMent.setPayWay(pays.getPayWay()); //֧����ʽ
									payMent.setPayStatus("֧���ɹ�"); //֧��״̬ Ĭ��Ϊ֧���ɹ�
									payMentList.add(payMent);
								}
							}
							// �Ƿ���Ʒ 
							platOrder.setHasGift(0);// �Ƿ���Ʒ��0������1��
							
							StoreRecord storeRecord = storeRecordDao.select(storeId);
							platOrder.setOrderId(orderId);// �������
							platOrder.setStoreId(storeId);// ���̱��
							platOrder.setEcOrderId(ecOrderId);// ���̱��
							platOrder.setStoreName(storeRecord.getStoreName());// ��������
							platOrder.setPayTime(plat.getPaymentConfirmTime());// ����ʱ��
							platOrder.setTradeDt(plat.getOrderStartTime());//�µ�ʱ��
							platOrder.setIsAudit(0);// �Ƿ���ˣ�0δ��ˣ�1���
							platOrder.setGoodNum(goodNum);// ��Ʒ������
							//platOrder.setDeliveryType(plat.getDeliveryType());//�����ͻ�����
							platOrder.setVenderId(plat.getVenderCode());//�����̼�ID�̼ұ���									
							platOrder.setSellerNote(plat.getVenderRemark());// �̼ұ�ע
									
							platOrder.setGoodMoney(plat.getOrderTotalPrice());// �����ܽ��
							platOrder.setPayMoney(plat.getOrderPayment());// ���ʵ�ʸ�����
							// �Ƿ���� ĿǰĬ��Ϊ�Ƿ�������
							//platOrder.setOrderSellerPrice(plat.getOrderSellerPrice());//������
							platOrder.setFreightPrice(plat.getFreightPrice()); //�˷ѽ��
							
							platOrder.setBuyerId(plat.getPin());// ��һ�Ա��
							platOrder.setBuyerNote(plat.getOrderRemark());// ��ұ�ע
							platOrder.setRecAddress(plat.getFullAddress());// �ջ�����ϸ��ַ
							platOrder.setRecName(plat.getFullname());// �ջ�������
							platOrder.setRecMobile(plat.getMobile());// �ջ����ֻ���
							platOrder.setProvince(plat.getProvince());//�ջ���ʡ
							platOrder.setProvinceId(plat.getProvinceId());//�ջ���ʡid
							platOrder.setCity(plat.getCity());//�ջ�����
							platOrder.setCityId(plat.getCityId());//�ջ�����id
							platOrder.setCounty(plat.getCounty());//�ջ�������
							platOrder.setCountyId(plat.getCountyId());//�ջ�������id
							//platOrder.setDeliverWhs(); //�����ֿ�
							platOrder.setSellTypId("1");//��������������ͨ����
							platOrder.setBizTypId("2");//����ҵ������2c
							
							
							platOrder.setIsShip(1);// �Ƿ񷢻� 
							platOrder.setDeliverSelf(0);//���ö���Ϊƽ̨��������
							platOrder.setNoteFlag(0);// �̼ұ�ע���ģ�Ĭ��ֵ����ʾ������
							platOrder.setIsClose(0);// �Ƿ�ر�
							platOrder.setIsInvoice(0);// �Ƿ񿪷�Ʊ
							//platOrder.setIsShip(0);// �Ƿ񷢻�
							//platOrder.setAdjustMoney(new BigDecimal(orderInfo.get("balanceUsed").asDouble()));// �̼ҵ������
							platOrder.setAdjustMoney(new BigDecimal(0));
							platOrder.setDiscountMoney(new BigDecimal(plat.getSellerDiscount()));// �Żݽ��
											
							platOrder.setOrderStatus(0);// ����״̬
							platOrder.setReturnStatus(0);// �˻�״̬
							
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
			logger.error("URL��ec/platOrder/other/export �쳣˵����", e);
			throw new RuntimeException();
		}
		return message;
		
	}

	@Override
	public String recallPlatOrderThird(String ecorderId, String storeId, String userId) {
		
		//��������
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		int flag = 0; //1.����ɾ�� 2.�������� 3.���۵����� 4.���۳��ⵥ���� 5.�޷�����
		
		List<SellSngl> sellSnglList = new ArrayList<>(); //�������۵�����
		List<SellOutWhs> sellOutWhsList = new ArrayList<>(); //�������۳��ⵥ����
	
		try {
			PlatOrder platOrder = platOrderDao.selectByEcOrderId(ecorderId);
			String orderId = "";
			if(platOrder == null) {
				
				message = "�������"+ecorderId+"������,����ʧ��";
				isSuccess = false;
				
			} else {
				orderId = platOrder.getOrderId(); //ƽ̨�������
				
				//���������
				if(platOrder.getIsAudit() == 0) {
					//0δ��� ɾ������
					flag = 1; //����ɾ��
				} else {
					//���۵�
					SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderId);
					if(sellSngl == null) {
						
						flag = 2; //��������	
						
					} else if(sellSngl.getIsNtChk() == 0){
						
						//�������۵�
						sellSnglList.add(sellSngl); //�������۵�����	
						flag = 3; //����+���۵�����
						
					} else {
						
						//���۳��ⵥ
						String sellSnglId = sellSngl.getSellSnglId(); //���۵����
						SellOutWhs sellOutWhs = sellOutWhsDao.selectSellOutWhsBySellSnglId(sellSnglId);
						if(sellOutWhs == null) {
							
							//�������۵�
							sellSngl.setIsNtChk(0); //���۵�״̬��Ϊ 0δ���
							sellSnglList.add(sellSngl); //�������۵�����	
							flag = 3;
							
						} else if(sellOutWhs.getIsNtChk() == 0) {
							
							//���۳��ⵥδ���
							sellSnglList.add(sellSngl); //�������۵�����	
							sellOutWhsList.add(sellOutWhs); //�������۳��ⵥ����	
							flag = 4;
							
						} else {
							//������
							LogisticsTab logisticsTab =  logisticsTabDao.selectByOrderId(orderId);
							if(logisticsTab == null || StringUtils.isEmpty(String.valueOf(logisticsTab.getOrdrNum()))) {
								
								//������Ϊ�ջ���������Ϊ��
								sellSnglList.add(sellSngl); //�������۵�����	
								sellOutWhsList.add(sellOutWhs); //�������۳��ⵥ����	
								flag = 4;			
							} else {
								//�ѷ���
								//�޷�����
								flag = 5;
							}
							
						} 
					}
				}
				String delMess = "";
				if(flag == 5){
						
					message = "�������"+ecorderId+"�ѷ���,����ʧ��";
					isSuccess = false;
					
				} else if(flag == 4) {
					//����	
					//���۳��ⵥ
					//delMess = sellOutWhsService.updateSellOutWhsIsNtChk(sellOutWhsList);
					if(delMess.contains("true")) {
						//���۵�
						//delMess = sellSnglService.updateSellSnglIsNtChkList(userId, sellSnglList);
						if(delMess.contains("true")) {
							//����
							delMess = associatedService.orderAbandonAuditByOrderId(orderId,userId);
							if(delMess.contains("true")) {
								//����ɾ��
								//TODO ���ʽɾ��
								//delMess = platOrderService.delete(orderId);
								if(delMess.contains("true")) {
									message = "�������"+ecorderId+"��ȡ��";
									isSuccess = true;
								}
							}
						}
					} else {
						message = "�������"+ecorderId+"����ʧ��";
						isSuccess = false;
					}
							
				} else if(flag == 3){
					
					//���۵�
					//delMess = sellSnglService.updateSellSnglIsNtChkList(userId, sellSnglList);
					if(delMess.contains("true")) {
						//����
						delMess = associatedService.orderAbandonAuditByOrderId(orderId,userId);
						if(delMess.contains("true")) {
							//����ɾ��
							//delMess = platOrderService.delete(orderId);
							if(delMess.contains("true")) {
								resp = delMess;
								isSuccess = true;
							}
						}
					} else {
						message = "�������"+ecorderId+"����ʧ��";
						isSuccess = false;
					}
				} else if(flag == 2) {
					
					//����
					delMess = associatedService.orderAbandonAuditByOrderId(orderId,userId);
					if(delMess.contains("true")) {
						//����ɾ��
						//delMess = platOrderService.delete(orderId);
						if(delMess.contains("true")) {
							message = "�������"+ecorderId+"��ȡ��";
							isSuccess = true;
						}
					} else {
						message = "�������"+ecorderId+"����ʧ��";
						isSuccess = false;
					}
					
				} else if(flag == 1) {
					
					//����ɾ��
					//delMess = platOrderService.delete(ecorderId);
					if(delMess.contains("true")) {
									
						message = "�������"+ecorderId+"��ȡ��";
						isSuccess = true;
						
					} else {
						message = "�������"+ecorderId+"����ʧ��";
						isSuccess = false;
					}
				} else {
					message = "�������"+ecorderId+"����ʧ��";
					isSuccess = false;
				}
				
				
			}
			System.out.println(message);
			resp = BaseJson.returnRespObj("mis/ec/platOrder/other/recall", isSuccess, message, null);
		} catch(Exception e) {
			logger.error("URL��mis/ec/platOrder/other/recall �쳣˵����", e);
			throw new RuntimeException();
		}
		return resp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
