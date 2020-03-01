package com.px.mis.ec.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.javassist.expr.NewArray;
import org.apache.log4j.lf5.viewer.TrackingAdjustmentListener;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.account.service.FormBookService;
import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.entity.Associated;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.InvtyCls;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.SalesPromotionActivityUtil;
import com.px.mis.whs.dao.InvtyTabMapper;
import com.px.mis.whs.dao.PickSnglMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.px.mis.whs.entity.WhsDoc;
import com.px.mis.whs.service.InvtyTabService;
@Service
@Transactional
public class AssociatedSearchServiceImpl implements AssociatedSearchService {
	@Autowired
	private SellSnglDao sellSnglDao;//销售单 (现在没有销售订单和销售发货单了,这两张单合成销售单了。)
	@Autowired
	private SellSnglSubDao sellSnglSubDao;//销售单子单 (现在没有销售订单和销售发货单了,这两张单合成销售单了。)
	@Autowired
	private SellOutWhsDao sellOutWhsDao;//销售出库单
	@Autowired
	private SellOutWhsSubDao sellOutWhsSubDao;//销售出库单字表；
	@Autowired
	private RtnGoodsDao rtnGoodsDao;//退货单
	@Autowired
	private PlatOrderDao platOrderDao;//订单
	@Autowired
	private PlatOrdersDao platOrdersDao;//订单子单
	@Autowired
	private LogisticsTabDao logisticsTabDao;//物流表
	@Autowired
	private PickSnglMapper pickSnglDao;//拣货单
	@Autowired
	private InvtyTabMapper invtyTabDao;//库存表
	@Autowired
	private MisUserDao misUserDao;//库存表
	@Autowired
	private WhsDocMapper whsDocDao;//仓库档案；
	@Autowired
	private StoreRecordDao storeRecordDao;//店铺档案；
	@Autowired
	private GoodRecordDao goodRecordDao;//平台商品档案；
	@Autowired
	private InvtyDocDao invtyDocDao;//存货档案；
	@Autowired
	private ProdStruMapper prodStruDao;//产品结构
	@Autowired
	private InvtyTabService invtyTabService;//库存表
	@Autowired
	private PlatOrderService platOrderService;//订单
	@Autowired
	private InvtyClsDao invtyClsDao;
	@Autowired
	private RefundOrderDao refundOrderDao;
	@Autowired
	private GetOrderNo gon;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private SalesPromotionActivityUtil salesPromotionActivityUtil;
	@Autowired 
	private FormBookService formBookService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//联合查询
	@Override
	public String quickSearchByOrderId(String orderId) {
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		List<Associated> list=new ArrayList<Associated>();
		if(orderId == null || platOrderDao.select(orderId)==null) {
			isSuccess=false;
			message="订单编号"+orderId+"不存在,联查失败！";
		}else {
			SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderId);
			if(sellSngl==null) {
				isSuccess=false;
				message="销售单中这个订单编号"+orderId+"的销售单不存在,联查失败！";
			}else {
				String sellSnglId = sellSngl.getSellSnglId();
				Associated associated=new Associated();
				associated.setName("销售单");
				associated.setOrderCode(sellSnglId);
				associated.setSetupPers(sellSngl.getSetupPers());
				associated.setSetupTm(sellSngl.getSetupTm());
				associated.setChkr(sellSngl.getChkr());
				associated.setChkTm(sellSngl.getChkTm());
				list.add(associated);
				
				SellOutWhs sellOutWhs = sellOutWhsDao.selectSellOutWhsBySellSnglId(sellSnglId);
				if(sellOutWhs == null) {
					isSuccess=false;
					message="销售出库单中这个销售订单号"+sellSnglId+"的销售出库单不存在,联查失败！";
				}else {
					Associated associatedOne=new Associated();
					associatedOne.setName("销售出库单");
					associatedOne.setOrderCode(sellOutWhs.getOutWhsId());
					associatedOne.setSetupPers(sellOutWhs.getSetupPers());
					associatedOne.setSetupTm(sellOutWhs.getSetupTm());
					associatedOne.setChkr(sellOutWhs.getChkr());
					associatedOne.setChkTm(sellOutWhs.getChkTm());
					list.add(associatedOne);
				}
				RtnGoods rtnGoods = rtnGoodsDao.selectRtnGoodsBySellSnglId(sellSnglId);
				if(rtnGoods == null) {
					isSuccess=false;
					message="退货单中这个销售订单号"+sellSnglId+"的退货单不存在,联查失败！";
				}else {
					Associated associatedTwo=new Associated();
					associatedTwo.setName("退货单");
					associatedTwo.setOrderCode(rtnGoods.getRtnGoodsId());
					associatedTwo.setSetupPers(rtnGoods.getSetupPers());
					associatedTwo.setSetupTm(rtnGoods.getSetupTm());
					associatedTwo.setChkr(rtnGoods.getChkr());
					associatedTwo.setChkTm(rtnGoods.getChkTm());
					list.add(associatedTwo);
				}
				isSuccess=true;
				message="联查成功！";
			}
		}
		
		try {
			resp = TransformJson.returnRespList("ec/associatedSearch/quickSearchByOrderId", isSuccess, message, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//一键逆向操作
	@Override
	public String reverseOperationByOrderId(String orderId) {
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		PlatOrder platOrder = platOrderDao.select(orderId);
		if(platOrder == null || platOrder.getIsAudit()==0) {//订单编号不存在，无法逆向操作；
			isSuccess=false;
			message="订单编号"+orderId+"不存在或者该订单状态为未审核,请重新选择订单，逆向操作失败！";
		}else {
			SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderId);
			if(sellSngl==null) {//订单编号对应的销售单不存在；说明订单还没有审核，那肯定就没有拣货单，销售出库单了
				isSuccess=false;
				message="订单编号"+orderId+"对应的销售单不存在,逆向操作失败！";
			}else {//订单审核完之后才会生成销售单，如果没有生成销售单，订单就一定没有审核；
				String sellSnglId = sellSngl.getSellSnglId();
				SellOutWhs sellOutWhs = sellOutWhsDao.selectSellOutWhsBySellSnglId(sellSnglId);
				if(sellOutWhs!=null && sellOutWhs.getIsNtChk()==1) {//销售出库单已审核,无法逆向操作；
					isSuccess=false;
					message="销售单号"+sellSnglId+"对应的销售出库单已审核,无法逆向操作！";
				}else{
					if(sellOutWhs==null) {
						isSuccess=false;
						message="销售单号"+sellSnglId+"对应的销售出库单不存在！";
					}else {
						String outWhsId = sellOutWhs.getOutWhsId();
						sellOutWhsDao.deleteSellOutWhsByOutWhsId(outWhsId);
						sellOutWhsSubDao.deleteSellOutWhsSubByOutWhsId(outWhsId);
					}
					if(sellSngl.getIsNtChk()==1) {//销售单已审核，需要修改库存的可用量；
						//修改库存的可用量，不改变含税金额与未税金额等数据；
						List<SellSnglSub> sellSnglSubs = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSnglId);
						if(sellSnglSubs != null && sellSnglSubs.size()>0) {
							for(SellSnglSub sellSnglSub:sellSnglSubs) {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(sellSnglSub.getWhsEncd());
								invtyTab.setInvtyEncd(sellSnglSub.getInvtyEncd());
								invtyTab.setBatNum(sellSnglSub.getBatNum());
								invtyTab = invtyTabDao.selectByReverse(invtyTab);
								if(invtyTab==null) {//根据销售出库单字表中的仓库编码，存货编码，批号没有匹配上库存表中的数据；
									continue;
								}else {
									BigDecimal qty = sellSnglSub.getQty();
									invtyTab.setAvalQty(qty);//出库单子表中的可用量数量要加上的数量；
									invtyTabDao.updateByReverse(invtyTab);//更新库存表中的可用量；
								}
							}
						}
					}
					//删除销售单
					sellSnglDao.deleteSellSnglBySellSnglId(sellSnglId);
					sellSnglSubDao.deleteSellSnglSubBySellSnglId(sellSnglId);
					//删除拣货单
					pickSnglDao.deletePickSnglTabBySellSnglId(sellSnglId);
					//删除物流表
					logisticsTabDao.deleteBySellSnglId(sellSnglId);
					Integer isAudit = platOrder.getIsAudit();//订单是否审核
					if(isAudit==1) {//订单已审核，更新订单的审核状态
						platOrder.setIsAudit(0);//是否审核	0：未审核；1：已审核。
						platOrderDao.update(platOrder);
					}
					message="逆向操作成功！";
					isSuccess=true;
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/associatedSearch/reverseOperationByOrderId", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//订单审核
	@Override
	public String orderAuditByOrderId(String orderIds,String accNum,String loginDate) {
		//System.out.println("订单审核，请求参数：orderIds+++"+orderIds);
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		int successcount = 0;//审核成功订单条数
		int fallcount = 0;//审核失败条数
		String[] split = orderIds.split(",");
		MisUser misUser = misUserDao.select(accNum);
		for(String orderId:split) {
			PlatOrder platOrder = platOrderDao.select(orderId);
			/*
			 * String time = platOrder.getPayTime(); if(StringUtils.isEmpty(time)) { time =
			 * platOrder.getTradeDt(); }
			 */
			if(formBookService.isMthSeal(loginDate)) {
				//单据日期已不能新增销售单
				platOrder.setAuditHint("订单审核失败，登录日期相应月份已封账");
				platOrderDao.update(platOrder);//更新订单的审核提示；
				//日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				//MisUser misUser = misUserDao.select(accNum);
				if(misUser!=null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("订单审核失败，登录日期相应月份已封账");
				logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logRecord.setOperatType(2);//2审核
				logRecord.setTypeName("审核");
				logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			List<PlatOrders> list = platOrdersDao.select(orderId);
			if(platOrder==null || list==null || list.size()==0) {
				//message="订单编号"+orderId+"不存在或者该订单下的商品不存在，订单审核失败！";
				fallcount++;
				continue;
			}else if(platOrder.getIsAudit()==1){
				//message="订单编号"+orderId+"下的订单已经审核，订单审核失败！";
				fallcount++;
				continue;
			}else{
				boolean flag = true;//判断过程中是是否出现过错误，出现错误时跳出后续循环，直接审核下一张订单
				String invid="";
				try {
				for (int i = 0; i < list.size(); i++) {
					if(StringUtils.isEmpty(list.get(i).getInvId())) {
						//当订单明细中含有未匹配到存货编码的明细时，不能审核，跳出当前条，审核下一条
						platOrder.setAuditHint("订单中存在未匹配到存货档案的记录");
						platOrderDao.update(platOrder);//更新订单的审核提示；
						//日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("订单审核失败，订单中存在未匹配到存货档案的记录");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2审核
						logRecord.setTypeName("审核");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						logRecordDao.insert(logRecord);
						fallcount++;
						flag=false;
						break;
					}else if(StringUtils.isEmpty(list.get(i).getBatchNo())) {
							
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(list.get(i).getInvId());
							if(invtyDoc==null) {
								platOrder.setAuditHint("在存货档案中未匹配到存货编码："+list.get(i).getInvId());
								platOrderDao.update(platOrder);//更新订单的审核提示；
								//日志记录
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if (misUser != null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("订单审核失败，" + "在存货档案中未匹配到存货编码："+list.get(i).getInvId());
								logRecord.setOperatTime(
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2审核
								logRecord.setTypeName("审核");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
								logRecordDao.insert(logRecord);
								fallcount++;
								flag=false;
								break;
							}
							invid=invtyDoc.getInvtyEncd();
							
								if (invtyDoc.getShdTaxLabour() == 0) {
									//当订单明细中未匹配到批次的存货不是虚拟存货时
									//当订单明细中含有未匹配到存货批次时，不能审核，跳出当前条，审核下一条
									platOrder.setAuditHint(list.get(i).getInvId() + ":未匹配到批次");
									platOrderDao.update(platOrder);//更新订单的审核提示；
									//日志记录
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(accNum);
									//MisUser misUser = misUserDao.select(accNum);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("订单审核失败，" + list.get(i).getInvId() + ":未匹配到批次");
									logRecord.setOperatTime(
											new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									logRecord.setOperatType(2);//2审核
									logRecord.setTypeName("审核");
									logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
									logRecordDao.insert(logRecord);
									fallcount++;
									flag = false;
									break;
								} 
							
						}else if(StringUtils.isEmpty(list.get(i).getDeliverWhs())) {
							//发货仓库不能为空
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(list.get(i).getInvId());
							invid=invtyDoc.getInvtyEncd();
							if (invtyDoc.getShdTaxLabour()==0) {
								//不是应税劳务
								//当发货仓库为空时，不能审核，跳出当前条，审核下一条
								platOrder.setAuditHint("非虚拟商品发货仓库不能为空");
								platOrderDao.update(platOrder);//更新订单的审核提示；
								//日志记录
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if (misUser != null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("订单审核失败，非虚拟商品发货仓库不能为空");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2审核
								logRecord.setTypeName("审核");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
								logRecordDao.insert(logRecord);
								fallcount++;
								flag = false;
								break;
							}
						}
				}
				} catch (Exception e) {
					// TODO: handle exception
				
					platOrder.setAuditHint("请检查存货编码："+invid + "是否虚拟商品属性设置");
					platOrderDao.update(platOrder);//更新订单的审核提示；
					//日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					//MisUser misUser = misUserDao.select(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单审核失败，请检查存货编码："+invid + "是否虚拟商品属性设置");
					logRecord.setOperatTime(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(2);//2审核
					logRecord.setTypeName("审核");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
					logRecordDao.insert(logRecord);
					fallcount++;
					flag = false;
					break;
				}
				if(!flag) {
					continue;
				}
				
				//数量不能为0
				if(platOrder.getGoodNum()==0) {
					//订单数量为0，不能审核，跳出当前条，审核下一条
					platOrder.setAuditHint("订单审核失败，商品数量为0");
					platOrderDao.update(platOrder);//更新订单的审核提示；
					//日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					//MisUser misUser = misUserDao.select(accNum);
					if(misUser!=null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单审核失败，商品数量为0");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(2);//2审核
					logRecord.setTypeName("审核");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				int refundCount = refundOrderDao.selectCountByEcOrderId(platOrder.getEcOrderId());
				if(refundCount>0) {
					//订单存在退款单，不能审核
					platOrder.setAuditHint("订单存在退款单，不能审核");
					platOrderDao.update(platOrder);//更新订单的审核提示；
					//日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					//MisUser misUser = misUserDao.select(accNum);
					if(misUser!=null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单审核失败，订单存在退款单，不能审核");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(2);//2审核
					logRecord.setTypeName("审核");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//自发货订单快递公司编码不能为空
				if(platOrder.getDeliverSelf()==1&&StringUtils.isEmpty(platOrder.getExpressCode())) {
					int aa = 0;//订单中虚拟商品数量
					for (int i = 0; i < list.size(); i++) {
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(list.get(i).getInvId());
						if(invtyDoc.getShdTaxLabour()==0) {
							aa++;
						}
					}
					if(aa<list.size()) {
						//当虚拟商品的数量小于订单明细数量时，此单必须有快递公司编码
						//当自发货订单快递公司为空时，不能审核，跳出当前条，审核下一条
						platOrder.setAuditHint("自发货订单快递公司不能为空");
						platOrderDao.update(platOrder);//更新订单的审核提示；
						//日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("订单审核失败，自发货订单快递公司为空");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2审核
						logRecord.setTypeName("审核");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						logRecordDao.insert(logRecord);
						fallcount++;
						continue;
					}
					
				}
					
					if(!flag) {
						continue;
					}
					SellSngl sellSngl=new SellSngl();
					String sellSnglId=null;
					try {
						sellSnglId = gon.getSeqNo("XS", accNum,loginDate);
					} catch (Exception e1) {
						//e1.printStackTrace();
						platOrder.setAuditHint("订单审核失败，生成销售单号失败");
						platOrderDao.update(platOrder);//更新订单的审核提示；
						//日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("订单审核失败，生成销售单号失败");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2审核
						logRecord.setTypeName("审核");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						logRecordDao.insert(logRecord);
						fallcount++;
						continue;
					}
					sellSngl.setSellSnglId(sellSnglId);//销售单编号
					sellSngl.setDeliverSelf(platOrder.getDeliverSelf());//设置销售单是否自发货
				/*
				 * if(StringUtils.isEmpty(platOrder.getPayTime())) {
				 * sellSngl.setSellSnglDt(platOrder.getTradeDt());//销售单日期 当付款时间为空时用下单时间 }else {
				 * sellSngl.setSellSnglDt(platOrder.getPayTime());//销售单日期 付款时间做为销售单的日期 }
				 */
				/*
				 * String aaa = loginDate.split(" ")[0]+sdf.format(new Date()).substring(10);
				 * System.out.println(aaa);
				 */
					sellSngl.setSellSnglDt(loginDate.split(" ")[0]+sdf.format(new Date()).substring(10));//销售单日期用审核登录日期加系统时分秒
					sellSngl.setAccNum(accNum);//用户编号
					
					
					StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
					if (StringUtils.isEmpty(storeRecord.getRespPerson())) {
						platOrder.setAuditHint("订单审核失败，对应店铺档案负责人为空，请先设置");
						platOrderDao.update(platOrder);//更新订单的审核提示；
						//日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("订单审核失败，对应店铺档案负责人为空，请先设置");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2审核
						logRecord.setTypeName("审核");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						logRecordDao.insert(logRecord);
						fallcount++;
						continue;
					}
					if(storeRecord!=null) {
						if(StringUtils.isNotEmpty(storeRecord.getCustomerId())) {
							sellSngl.setCustId(storeRecord.getCustomerId());//客户编号    
						}else {
							
							platOrder.setAuditHint("订单审核失败，店铺对应客户信息为空，请先设置");
							platOrderDao.update(platOrder);//更新订单的审核提示；
							//日志记录
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							//MisUser misUser = misUserDao.select(accNum);
							if(misUser!=null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("订单审核失败，店铺对应客户信息为空，请先设置");
							logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							logRecord.setOperatType(2);//2审核
							logRecord.setTypeName("审核");
							logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
							logRecordDao.insert(logRecord);
							fallcount++;
							continue;
						}
					}
					
					
					
					
					
					sellSngl.setBizTypId(platOrder.getBizTypId());//业务类型编号
					sellSngl.setSellTypId(platOrder.getSellTypId());//销售类型编号
					sellSngl.setRecvSendCateId(platOrder.getRecvSendCateId());//收发类别编号
					
					for(PlatOrders platOrders:list) {
						//System.err.println("whsEncd\t"+whsEncd);
						//System.err.println("platOrders.getDeliverWhs()\t"+platOrders.getDeliverWhs());
						if(StringUtils.isEmpty(platOrders.getDeliverWhs())) {
							//当发货仓库编码为空时判断存货是否是虚拟物品，不是虚拟物品时提示发货仓库不能为空
							
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.getInvId());
							if (invtyDoc.getShdTaxLabour()==0) {
								try {
									//message="仓库编码为null,或者"+whsEncd+"不一致，订单审核失败！";
									platOrder.setAuditHint("非虚拟商品发货仓库不能为空");
									platOrderDao.update(platOrder);//更新订单的审核提示；

									//日志记录
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(accNum);
									//MisUser misUser = misUserDao.select(accNum);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("订单审核失败，非虚拟商品发货仓库不能为空，订单审核失败！");
									logRecord.setOperatTime(
											new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									logRecord.setOperatType(2);//2审核
									logRecord.setTypeName("审核");
									logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
									logRecordDao.insert(logRecord);

									fallcount++;
									flag = false;
									break;
									//resp = BaseJson.returnRespObj("ec/associatedSearch/orderAuditByOrderId", isSuccess, message, null);
								} catch (Exception e) {
									e.printStackTrace();
								}
								//return resp;
							}
						}
					}
					if(!flag) {
						continue;
					}
					//判断仓库编码是否一致，如果不一致，审核失败
					/*String whsEncd=list.get(0).getDeliverWhs();
					
					if(StringUtils.isEmpty(platOrder.getDeliverWhs())) {
						platOrder.setAuditHint("发货仓为空");
						platOrderDao.update(platOrder);//更新订单的审核提示；
						fallcount++;
						flag=false;
					}*/
					if(!flag) {
						continue;
					}
					
					List<SellSnglSub> subs=new ArrayList<SellSnglSub>();//子表list，为了存放pto情况发生时一条订单明细会产生多个子表
					if(flag) {
						
						if (StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
							WhsDoc whsDoc = whsDocDao.selectWhsDoc(platOrder.getDeliverWhs());
							sellSngl.setDelvAddr(whsDoc.getWhsAddr());//发货地址（仓库地址，根据订单子表中的仓库编码获取）
						}
						sellSngl.setTxId(platOrder.getOrderId());//订单编号
						sellSngl.setEcOrderId(platOrder.getEcOrderId());//电商订单号
						sellSngl.setIsNtBllg(platOrder.getIsInvoice());//是否开票
						sellSngl.setIsNtChk(0);//是否审核 标记为0:未审核；
						sellSngl.setSetupPers(accNum);//创建人  用户编号
						
						
						MisUser mis = misUserDao.select(storeRecord.getRespPerson());
						if(mis==null) {
							//查询不到用户档案
							platOrder.setAuditHint("订单审核失败，店铺对应负责人在用户档案中为空");
							platOrderDao.update(platOrder);//更新订单的审核提示；
							//日志记录
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							//MisUser misUser = misUserDao.select(accNum);
							if(misUser!=null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("订单审核失败，店铺对应负责人在用户档案中为空");
							logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							logRecord.setOperatType(2);//2审核
							logRecord.setTypeName("审核");
							logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
							logRecordDao.insert(logRecord);
							fallcount++;
							continue;
						}
						sellSngl.setDeptId(mis.getDepId());//部门id
						sellSngl.setAccNum(storeRecord.getRespPerson());//业务员
						sellSngl.setUserName(mis.getUserName());//业务员姓名
						sellSngl.setFormTypEncd("007");//单据类型
						sellSngl.setSetupTm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//创建时间；
						sellSngl.setRecvr(platOrder.getRecName());//收件人姓名；
						sellSngl.setRecvrTel(platOrder.getRecMobile());//收件人电话
						sellSngl.setRecvrAddr(platOrder.getRecAddress());//收件人地址；
		//		    private String recvrEml;//收件人邮箱
						sellSngl.setBuyerNote(platOrder.getBuyerNote());//买家留言
						
						//开始之前先计算订单每条明细的实付金额。
						//list = platOrderService.jisuanPayPrice(platOrder.getOrderId());
						/*List<SellSnglSub> subs=new ArrayList<SellSnglSub>();//子表list，为了存放pto情况发生时一条订单明细会产生多个子表
						List<SellSnglSub> subs1=new ArrayList<SellSnglSub>();//子表list，为了存放pto情况发生时，计算税额后的子表
						List<SellSnglSub> subs2=new ArrayList<SellSnglSub>();//子表list，为了存放pto情况发生时，同存货多批次时，获取批次及效期后的子表
						List<SellSnglSub> sellSnglSubs=new ArrayList<SellSnglSub>();*/
						List<PlatOrders> platOrders1 = platOrdersDao.platOrdersByInvIdAndBatch(platOrder.getOrderId());
						
						for(PlatOrders platOrders:platOrders1) {
							
							//platOrdersDao.updatePayMoney(platOrders);
							SellSnglSub sellSnglSub=new SellSnglSub();
							sellSnglSub.setWhsEncd(platOrder.getDeliverWhs());//仓库编号；
							
							sellSnglSub.setSellSnglId(sellSngl.getSellSnglId());//销售单编号；
							sellSnglSub.setPrcTaxSum(platOrders.getPayMoney());//设置每条明细的实付金额
							
							if (platOrders.getPayMoney().compareTo(BigDecimal.ZERO)>0) {
								sellSnglSub.setIsComplimentary(0);//设置是否赠品
							}else {
								sellSnglSub.setIsComplimentary(1);//设置是否赠品
							}
							sellSnglSub.setBatNum(platOrders.getBatchNo());//批号
							InvtyDoc in = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.getInvId());
							InvtyCls ic = invtyClsDao.selectInvtyClsByInvtyClsEncd(in.getInvtyClsEncd());
						
						  if(StringUtils.isEmpty(ic.getProjEncd())) {
						  platOrder.setAuditHint("商品："+platOrders.getInvId()+"对应分类项目编码为空，请先设置");
						  platOrderDao.update(platOrder);//更新订单的审核提示；
						  
						  //日志记录 
						  LogRecord logRecord = new LogRecord();
						  logRecord.setOperatId(accNum);
						  //MisUser misUser = misUserDao.select(accNum); 
						  if (misUser != null) {
						  logRecord.setOperatName(misUser.getUserName()); }
						  logRecord.setOperatContent("审核失败，商品："+platOrders.getInvId()+"对应分类项目编码为空，请先设置"
						  ); logRecord.setOperatTime( new
						  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						  logRecord.setOperatType(2);//2审核 logRecord.setTypeName("审核");
						  logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						  logRecordDao.insert(logRecord);
						  
						  fallcount++; 
						  flag = false; 
						  break;
						  
						  }else { 
							  sellSnglSub.setProjEncd(ic.getProjEncd());//设置项目编码
						  }
						 
							if (StringUtils.isNotEmpty(platOrders.getPrdcDt())) {
								sellSnglSub.setPrdcDt(platOrders.getPrdcDt());//生产日期
								sellSnglSub.setInvldtnDt(platOrders.getInvldtnDt());//失效日期
								
								sellSnglSub.setBaoZhiQi(Integer.parseInt(in.getBaoZhiQiDt()));//保质期天数
							}
							sellSnglSub.setInvtyEncd(platOrders.getInvId());//存货编码商品编码
							sellSnglSub.setQty(new BigDecimal(platOrders.getInvNum()));//数量
							sellSnglSub.setUnBllgQty(sellSnglSub.getQty());//未开票数量
							sellSnglSub.setRtnblQty(sellSnglSub.getQty());//设置可退数量
							sellSnglSub.setHadrtnQty(new BigDecimal(0));//设置已退货数量
							
							if(in.getBxRule()!=null&&in.getBxRule().compareTo(BigDecimal.ZERO)>0) {
								//计算箱数
								//System.err.println(sellSnglSub.getQty()+"\t"+in.getBxRule());
								sellSnglSub.setBxQty(sellSnglSub.getQty().divide(in.getBxRule(),2,BigDecimal.ROUND_HALF_DOWN));
							}
							
							//sellSnglSub.setWhsEncd(whsEncd);//设置仓库编码
							sellSnglSub=countPrice(sellSnglSub);
							if(sellSnglSub.getTaxRate()==null) {
								//当计算完单价后 销项税率为空时，说明对应存货的销项税率为空
								platOrder.setAuditHint("订单审核失败，存货"+sellSnglSub.getInvtyEncd()+"未设置销项税率");
								platOrderDao.update(platOrder);//更新订单的审核提示；
								
								//日志记录
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if(misUser!=null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("订单审核失败，存货"+sellSnglSub.getInvtyEncd()+"未设置销项税率");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2审核
								logRecord.setTypeName("审核");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
								logRecordDao.insert(logRecord);
								fallcount++;
								flag=false;
								break;
								
							}
							subs.add(sellSnglSub);
						}
						if(!flag) {
							continue;
						}
						
					}
					//List<SellSnglSub> subs2=new ArrayList<SellSnglSub>();//子表list，为了存放pto情况发生时，同存货多批次时，获取批次及效期后的子表
						
						if (flag) {
							//TODO
							//System.out.println("开始往销售单表里插数据。。。。。。。。。。");
							//subs2 = salesPromotionActivityUtil.matchSalesPromotion(platOrder, list, sellSngl,subs2);//匹配促销活动
							
							
							//更新订单的审核状态；
							platOrder.setIsAudit(1);
							platOrder.setAuditHint("");//清空订单上的审核提示信息
							platOrder.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							int re = platOrderDao.updateAudit(platOrder);//更新订单的审核状态；
							if (re==1) {
								//订单状态更新成功
								sellSnglDao.insertSellSngl(sellSngl);//往销售单中添加一条数据；
								sellSnglSubDao.insertSellSnglSub(subs);//销售单子表中添加数据
								//日志记录
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if(misUser!=null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("订单审核成功");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2审核
								logRecord.setTypeName("审核");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
								logRecordDao.insert(logRecord);
								successcount++;
							}else {
								//订单状态更新失败
								//日志记录
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if(misUser!=null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("订单审核失败,该订单已被另一操作员审核");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2审核
								logRecord.setTypeName("审核");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
								logRecordDao.insert(logRecord);
								fallcount++;
								
								
							}
							
						}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/associatedSearch/orderAuditByOrderId", true, "审核订单完成，本次审核成功"+successcount+"条，失败"+fallcount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//订单弃审
	@Override
	public String orderAbandonAuditByOrderId(String orderId, String accNum) {
		String resp = "";
		//String message = "";
		boolean isSuccess=true;
		int successcount=0;
		int fallcount=0;
		String[] orderids = orderId.split(",");
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < orderids.length; i++) {
			PlatOrder platOrder = platOrderDao.select(orderids[i]);
			if(platOrder==null) {
				fallcount++;
			}else if(platOrder.getIsAudit()==0) {
				//message="订单编号"+orderId+"对应的订单为未审核状态，订单弃审没有意义！";
				//订单未审核
				platOrder.setAuditHint("此订单尚未审核，不能弃审");
				platOrderDao.update(platOrder);//更新订单的审核提示；
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("此订单尚未审核，不能弃审");
				logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logRecord.setOperatType(3);// 3 弃审
				logRecord.setTypeName("弃审");
				logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
				logRecordDao.insert(logRecord);
				fallcount++;
			}else{
				SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderids[i]);
				if(sellSngl==null) {
					platOrder.setIsAudit(0);//将订单的状态改为未审核状态；
					platOrder.setAuditHint("");//清空订单上的审核提示
					platOrder.setAuditTime(null);//清空审核时间
					platOrderDao.update(platOrder);
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("订单弃审成功");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(3);// 3 弃审
					logRecord.setTypeName("弃审");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
					logRecordDao.insert(logRecord);
					successcount++;
				}else if(sellSngl.getIsNtChk()==1) {
					//对应的销售单已经审核，订单弃审失败！
					platOrder.setAuditHint("对应的销售单已经审核，订单弃审失败！");
					platOrderDao.update(platOrder);//更新订单的审核提示；

					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("对应的销售单已经审核，订单弃审失败！");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(3);// 3 弃审
					logRecord.setTypeName("弃审");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
					logRecordDao.insert(logRecord);
					fallcount++;
				}else {
					
				   int count = refundOrderDao.selectCountByEcOrderId(platOrder.getEcOrderId());
					if(count>0) {
						//对应的销售单已经审核，订单弃审失败！
						platOrder.setAuditHint("订单存在退款单，弃审失败");
						platOrderDao.update(platOrder);//更新订单的审核提示；

						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("订单弃审失败，存在退款单");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(3);// 3 弃审
						logRecord.setTypeName("弃审");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						logRecordDao.insert(logRecord);
						fallcount++;
					}else {
						sellSnglDao.deleteSellSnglBySellSnglId(sellSngl.getSellSnglId());
						platOrder.setIsAudit(0);//将订单的状态改为未审核状态；
						platOrder.setAuditHint("");//清空订单上的审核提示
						platOrder.setAuditTime(null);//清空审核时间
						platOrderDao.update(platOrder);
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("订单弃审成功");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(3);// 3 弃审
						logRecord.setTypeName("弃审");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//操作单号
						logRecordDao.insert(logRecord);
						successcount++;
					}
					
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/associatedSearch/orderAbandonAuditByOrderId", isSuccess, "弃审完成，本次成功弃审"+successcount+"条，失败"+fallcount+"条", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//将PTO存货传过来生成多条销售子单回去。
	public List<SellSnglSub> checkPTO(SellSnglSub nowsub,PlatOrders orders,List<ProdStruSubTab> subs){
		List<SellSnglSub> sellSnglSubs = new ArrayList<>();
		//按照PTO存货的多条子件的参考成本*订单数量来分摊每条明细对应的实付金额
		//计算子件参考成本与子件数量的总成本
		boolean flag = true;//声明旗标，如果false说明对应子件的参考成本未设置，系统无法拆分比例
		BigDecimal total = new BigDecimal("0");
		for (int i = 0; i < subs.size(); i++) {
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(subs.get(i).getSubEncd());
			if(invtyDoc.getRefCost()==null) {
				flag=false;
				break;
			}else {
				total=total.add(invtyDoc.getRefCost().multiply(subs.get(i).getSubQty()));
			}
		}
		if(flag) {
			BigDecimal costed = new BigDecimal("0");
			DecimalFormat dFormat = new DecimalFormat("0.00");
			for (int i = 0; i < subs.size(); i++) {
				//循环每条子件
				nowsub.setInvtyEncd(subs.get(i).getSubEncd());//子件编码
				nowsub.setQty(new BigDecimal(orders.getGoodNum()).multiply(subs.get(i).getSubQty()));
				//子件数量*参考成本
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(subs.get(i).getSubEncd());
				BigDecimal cost = subs.get(i).getSubQty().multiply(invtyDoc.getRefCost());
				if(i+1==subs.size()) {
					//最后一条时，用减法
					nowsub.setPrcTaxSum(orders.getPayMoney().subtract(costed));//设置价税合计
				}else {
					//计算占比
					BigDecimal thismoney = orders.getPayMoney().multiply(cost).divide(total);
					costed = costed.add(new BigDecimal(dFormat.format(thismoney)));
					nowsub.setPrcTaxSum(thismoney);//设置价税合计
				}
				nowsub.setWhsEncd(orders.getDeliverWhs());//设置仓库编码
				if(invtyDoc.getBxRule()!=null||invtyDoc.getBxRule().compareTo(BigDecimal.ZERO)>0) {
					//计算箱数
					nowsub.setBxQty(nowsub.getQty().divide(invtyDoc.getBxRule(),2,BigDecimal.ROUND_HALF_DOWN));
					nowsub.setRtnblQty(nowsub.getQty());
					nowsub.setHadrtnQty(new BigDecimal(0));
				}
				sellSnglSubs.add(nowsub);
			}
		}else {
			//flag  false时清空list 返回空的list
			sellSnglSubs.clear();
		}
		return sellSnglSubs;
	}
	/*private BigDecimal cntnTaxUprc;//含税单价

	    private BigDecimal prcTaxSum;//价税合计

	    private BigDecimal noTaxUprc;//无税单价

	    private BigDecimal noTaxAmt;//无税金额

	    private BigDecimal taxAmt;//税额

	    private BigDecimal taxRate;//税率
*/	
	//根据销售子单的含税金额计算含税单价，无税单价，税额
	public SellSnglSub countPrice(SellSnglSub sub) {
		BigDecimal paymoney = sub.getPrcTaxSum();//价税合计等于用户的实付金额
		DecimalFormat dFormat = new DecimalFormat("0.00");
		paymoney=new BigDecimal(dFormat.format(paymoney));
		InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(sub.getInvtyEncd());
		if(invtyDoc.getOptaxRate()==null) {
			return sub;
		}
		sub.setTaxRate(invtyDoc.getOptaxRate());//设置税率
		if (paymoney.compareTo(BigDecimal.ZERO)>0) {
			//System.out.println("实付金额："+paymoney);
			//获取税率除100
			BigDecimal rate = invtyDoc.getOptaxRate().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
			
			//计算无税金额
			BigDecimal notaxmoney = paymoney.divide((new BigDecimal("1").add(rate)), 2, BigDecimal.ROUND_HALF_DOWN);
			//计算税额
			BigDecimal taxmoney = paymoney.subtract(notaxmoney);
			//计算含税单价
			BigDecimal taxprice = paymoney.divide(sub.getQty(), 8, BigDecimal.ROUND_HALF_DOWN);
			//计算无税单价
			BigDecimal notaxprice = notaxmoney.divide(sub.getQty(), 8, BigDecimal.ROUND_HALF_DOWN);
			sub.setCntnTaxUprc(taxprice);//含税单价
			sub.setNoTaxUprc(notaxprice);//无税单价
			sub.setTaxAmt(taxmoney);//税额
			sub.setNoTaxAmt(notaxmoney);//无税金额
		}else {
			sub.setCntnTaxUprc(BigDecimal.ZERO);//含税单价
			sub.setNoTaxUprc(BigDecimal.ZERO);//无税单价
			sub.setTaxAmt(BigDecimal.ZERO);//税额
			sub.setNoTaxAmt(BigDecimal.ZERO);//无税金额
		}
		return sub;
	}
	@Override
	public String selectList(Map map) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
