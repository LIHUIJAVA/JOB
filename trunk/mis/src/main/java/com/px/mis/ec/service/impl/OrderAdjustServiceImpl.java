package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.service.OrderAdjustService;
import com.px.mis.util.BaseJson;
@Service
@Transactional
public class OrderAdjustServiceImpl implements OrderAdjustService {
	@Autowired
	private GoodRecordDao goodRecordDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private LogisticsTabDao logisticsTabDao;
	@Override
	public String showAdjustGoods(String orderId) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		List<GoodRecord> goodRecords=new ArrayList<GoodRecord>();
		List<PlatOrders> list = platOrdersDao.select(orderId);
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				PlatOrders platOrders = list.get(i);
				String goodId = platOrders.getGoodId();
				GoodRecord goodRecord= new GoodRecord();
				goodRecord = goodRecordDao.select(goodId);
				Long no = platOrders.getNo();
				if(goodRecord !=null ) {
					goodRecord.setNo(no);//将plat_orders表中的no字段响应给前端；
					goodRecords.add(i,goodRecord);
				}else {//没有找到这个平台商品；
					message="没有找到该订单下的平台商品！";
					isSuccess=false;
					break;
				}
			}
		}else {
			isSuccess=false;
			message="查询失败,"+orderId+",该订单下没有商品!";
		}
		try {
			resp = TransformJson.returnRespList("ec/orderAdjust/showAdjustGoods", isSuccess,message, goodRecords);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	@Override
	public String editAdjustGoods(List<Map> list) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		if(list!=null && list.size()>0) {
			for(Map map:list) {
				Integer orderItemId = (Integer)map.get("orderItemId");
				if (platOrdersDao.selectById(orderItemId) == null) {
					message = "订单项编号" +orderItemId + "不存在！商品调整失败！";
					isSuccess = false;
					try {
						resp = BaseJson.returnRespObj("ec/orderAdjust/editAdjustGoods", isSuccess, message, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return resp;
				}
			}
			for(Map map:list) {
				platOrdersDao.update(map);
				message = "商品调整成功！";
				isSuccess = true;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/orderAdjust/editAdjustGoods", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	@Override
	public String showSplitOrder(String orderId) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		PlatOrder platOrder = platOrderDao.select(orderId);
		List<PlatOrders> list = platOrdersDao.select(orderId);
		if(list!=null && list.size()>0) {
			message="查询成功！";
			platOrder.setPlatOrdersList(list);
		}else {
			isSuccess=false;
			message="查询失败,"+orderId+",该订单下没有商品！";
		}
		try {
			 resp=BaseJson.returnRespObj("ec/orderAdjust/showSplitOrder",isSuccess,message, platOrder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//手工拆分订单逻辑
	//手工拆分拆分的是订单中的订单项；然后根据各个订单项中的拆分数量生产一个新的订单，然后原订单、新订单及其订单项中的商品数量、商品金额、
	//实付金额，还有其订单的adjust_status都会有变化；最后还要在物流表中添加两条数据；
	@Override
	public String editSplitOrder(PlatOrder platOrder) {
		
		BigDecimal goodMoneyAll = new BigDecimal(0);//新订单中的商品金额
		BigDecimal payMoneyAll = new BigDecimal(0);//新订单中的实付金额
		Integer goodNumber=0;//新订单中的数量
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		String orderId2 = platOrder.getOrderId();
		if(platOrderDao.select(orderId2) == null) {
			message = "订单编号" +orderId2 + "不存在！手工拆单失败！";
			isSuccess = false;
		}else {
			PlatOrder newPlatOrder = new PlatOrder();
			try {
				BeanUtils.copyProperties(newPlatOrder, platOrder);
			} catch (IllegalAccessException | InvocationTargetException e2) {
				e2.printStackTrace();
			}
			//把platOrder里面的值都赋给newPlatOrder；
			List<PlatOrders> newPlatOrdersList = new ArrayList<PlatOrders>();
			
			List<PlatOrders> platOrdersList = platOrder.getPlatOrdersList();
			for(int i=0;i<platOrdersList.size();i++) {
				PlatOrders platOrders=platOrdersList.get(i);
				Integer splitNum = platOrders.getSplitNum();
				Integer goodNum = Integer.valueOf(platOrders.getGoodNum().toString());
				if(splitNum==null || splitNum<=0 || goodNum<=0 || splitNum>goodNum) {
					continue;
				}else {
					PlatOrders newPlatOrders = new PlatOrders();
					BigDecimal goodMoney = platOrders.getGoodMoney();
					BigDecimal payMoney = platOrders.getPayMoney();
					try {
						BeanUtils.copyProperties(newPlatOrders, platOrders);//把后面一个参数的属性值都赋值给前面的参数
					} catch (IllegalAccessException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
					newPlatOrders.setGoodNum(splitNum);
					goodNumber = goodNumber+splitNum;
					//除法的时候保留4位小数，舍位的时候四舍五入；
					BigDecimal scale = new BigDecimal(splitNum).divide(new BigDecimal(goodNum),4,BigDecimal.ROUND_HALF_UP);
					BigDecimal multiply = goodMoney.multiply(scale);
					BigDecimal multiply2 = payMoney.multiply(scale);
					newPlatOrders.setGoodMoney(multiply);//新订单项中的商品金额
					goodMoneyAll = goodMoneyAll.add(multiply);
					newPlatOrders.setPayMoney(multiply2);//新订单项中的实付金额
					payMoneyAll = payMoneyAll.add(multiply2);
					newPlatOrdersList.add(newPlatOrders);//添加到对应list中
					if(splitNum == goodNum) {//如果拆分数量个原来数量一样；就移除原有订单中的商品项
						platOrdersList.remove(i);
						platOrdersList.add(i, null);
					}else {
						PlatOrders oldPlatOrders = new PlatOrders();//原订单项中拆分之后剩下的商品项
						try {
							BeanUtils.copyProperties(oldPlatOrders, platOrders);
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
						oldPlatOrders.setGoodNum(goodNum-splitNum);
						oldPlatOrders.setGoodMoney(goodMoney.subtract(multiply));//原订单项中剩下的商品金额
						oldPlatOrders.setPayMoney(payMoney.subtract(multiply2));//原订单项中的剩下实付金额
						platOrdersList.remove(i);
						platOrdersList.add(i, oldPlatOrders);
					}
				}
			}
			List<LogisticsTab> logisticsTabs = new ArrayList<LogisticsTab>();
			if(newPlatOrdersList.size()>0) {
				String orderId=null;
				char charAt = orderId2.charAt(orderId2.length()-2);
				//生产新订单order_id的规则；如果被拆分的订单的order_id结尾不是类似“_1”的情况；那么就加上'_1';
				//如果被拆分的订单的order_id结尾是类似“_1”的情况,那么就最后一位字符的数字就累加；
				if(charAt=='_') {
					String substring = orderId2.substring(orderId2.length()-1);
					Integer end = Integer.valueOf(substring);end++;
					orderId = orderId2.substring(0, orderId2.length()-1)+end;
				}else {
					orderId = orderId2+"_1";
				}
				newPlatOrder.setOrderId(orderId);//新订单中的订单编号
				for(int i=0;i<newPlatOrdersList.size();i++) {
					newPlatOrdersList.get(i).setOrderId(orderId);
				}
				newPlatOrder.setPlatOrdersList(newPlatOrdersList);
				newPlatOrder.setPayMoney(payMoneyAll);
				newPlatOrder.setGoodMoney(goodMoneyAll);
				newPlatOrder.setGoodNum(goodNumber);
				newPlatOrder.setAdjustStatus(1);//手工拆单之后新订单标记为1；
				splitNewOrder(newPlatOrder);
				
				platOrder.setPlatOrdersList(platOrdersList);
				platOrder.setPayMoney(platOrder.getPayMoney().subtract(payMoneyAll));
				platOrder.setGoodMoney(platOrder.getGoodMoney().subtract(goodMoneyAll));
				platOrder.setGoodNum(platOrder.getGoodNum() - goodNumber);
				platOrder.setAdjustStatus(3);//被拆分的订单被手工拆单之后标记为3；（以后不要再拆了，否则拆后新增订单的主键可能会有冲突）；
				splitOldOrder(platOrder);
				
				LogisticsTab logisticsTab=new LogisticsTab();
				LogisticsTab logisticsTab1=new LogisticsTab();
				try {
					BeanUtils.copyProperties(logisticsTab, newPlatOrder);
					BeanUtils.copyProperties(logisticsTab1, platOrder);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				logisticsTabs.add(logisticsTab);
				logisticsTabs.add(logisticsTab1);
//				logisticsTabDao.deleteByOrderId(platOrder.getOrderId());//新增物流表之前，先删除被拆分订单(原订单)在物流表中的数据；
				logisticsTabDao.insertList(logisticsTabs);
				message = "手工拆单成功！";
				isSuccess = true;
			}else {
				message = "手工拆单失败！拆分数量不对";
				isSuccess = true;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/orderAdjust/editSplitOrder", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	@Override
	public String showMergeOrder(String orderIds) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		if(orderIds==null || orderIds=="null") {
			message="没有解析到要展示的订单号！";
			isSuccess=false;
			try {
				resp = TransformJson.returnRespList("ec/orderAdjust/showMergeOrder",isSuccess,message, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resp;
		}
		String[] split = orderIds.split(",");
		List<PlatOrder> list=new ArrayList<PlatOrder>();
		for(String orderId:split) {
			if(orderId==null || "".equals(orderId)) {
				continue;
			}else {
				PlatOrder platOrder = platOrderDao.select(orderId);
				list.add(platOrder);
			}
		}
		if(list.size()>0) {
			message="获取数据成功！";
			isSuccess=true;
		}else {
			message="没有解析到要展示的订单号！";
			isSuccess=false;
		}
		try {
			resp = TransformJson.returnRespList("ec/orderAdjust/showMergeOrder",isSuccess,message, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//手工合并逻辑
	@Override
	public String editMergeOrder(List<Map> list,String orderId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<PlatOrder> platOrders = new ArrayList<PlatOrder>();
		List<LogisticsTab> logisticsTabs = new ArrayList<LogisticsTab>();
		if(list==null || orderId==null || "".equals(orderId) || list.size()==0){
			message = "没有主订单编号" +orderId + "或者合并的订单数据不存在！手工合并失败！";
			isSuccess = false;
		}else {
			for(Map map:list) {
				if(map==null) {
					continue;
				}
				PlatOrder platOrder = new PlatOrder();
				LogisticsTab logisticsTab=new LogisticsTab();
				try {
					BeanUtils.populate(platOrder, map);
					platOrder.setAdjustStatus(2);//合并过的标志状态为2
					platOrders.add(platOrder);
					
					BeanUtils.populate(logisticsTab, map);
					logisticsTab.setOrderId(orderId);
					logisticsTabs.add(logisticsTab);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			for(PlatOrder platOrder:platOrders) {
				platOrderDao.update(platOrder);
			}
			//往物流表中添加数据之前先根据订单编号（order_id）删除这条数据；(已经合并过的，就不能再合并)
			for(LogisticsTab logisticsTab:logisticsTabs) {
//				logisticsTabDao.deleteByOrderId(orderId);
			}
			logisticsTabDao.insertList(logisticsTabs);
			message = "手工合并成功！";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("ec/orderAdjust/editMergeOrder", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//手工拆分订单保存时，原订单往数据库中药更新订单，并更新子订单表(先删后添加)
		public void splitOldOrder(PlatOrder platOrder) {
//			platOrderDao.delete(platOrder.getOrderId());
			platOrderDao.update(platOrder);
			platOrdersDao.delete(platOrder.getOrderId());
			List<PlatOrders> list=new ArrayList<PlatOrders>();
			for(PlatOrders platOrders:platOrder.getPlatOrdersList()) {
				if(platOrders!=null) {
					list.add(platOrders);
				}
			}
			platOrdersDao.insert(list);
		}
		//手工拆分订单保存时，新生成的订单直接往订单表中添加订单，并且更新子订单表(先删后添加)
		public void splitNewOrder(PlatOrder platOrder) {
//			platOrderDao.delete(platOrder.getOrderId());
			platOrderDao.insert(platOrder);
			platOrdersDao.delete(platOrder.getOrderId());
			List<PlatOrders> list=new ArrayList<PlatOrders>();
			for(PlatOrders platOrders:platOrder.getPlatOrdersList()) {
				if(platOrders!=null) {
					list.add(platOrders);
				}
			}
			platOrdersDao.insert(list);
		}
}
