package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.account.utils.TransformJson;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.service.LogisticsAdjustService;
import com.px.mis.util.BaseJson;
@Service
@Transactional
public class LogisticsAdjustServiceImpl implements LogisticsAdjustService {
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private LogisticsTabDao logisticsTabDao;
	@Override
	public String showSplitLogistics(Integer ordrNum) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		LogisticsTab logisticsTab = logisticsTabDao.select(ordrNum);
		if(logisticsTab==null) {
			isSuccess=false;
			message="查询失败,"+ordrNum+",该物流编号下没有物流单！";
		}else {
			String orderId = logisticsTab.getOrderId();
			List<PlatOrders> list = platOrdersDao.select(orderId);
			if(list!=null && list.size()>0) {
				message="查询成功！";
				logisticsTab.setPlatOrdersList(list);
			}else {
				isSuccess=false;
				message="查询失败,"+ordrNum+",该物流表的订单下没有商品项！";
			}
		}
		try {
			 resp=BaseJson.returnRespObj("ec/logisticsAdjust/showSplitLogistics",isSuccess,message, logisticsTab);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String editSplitLogistics(LogisticsTab logisticsTab) {
		BigDecimal goodMoneyAll = new BigDecimal(0);//新订单中的商品金额
		BigDecimal payMoneyAll = new BigDecimal(0);//新订单中的实付金额
		Integer goodNumber=0;//新订单中的数量
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		String orderId2 = logisticsTab.getOrderId();
		if(platOrderDao.select(orderId2) == null) {
			message = "物流表中的订单编号" +orderId2 + "不存在！手工拆物流表失败！";
			isSuccess = false;
		} else {
			Integer adjustStatus = logisticsTab.getAdjustStatus();
			if(adjustStatus==1 || adjustStatus==2 || adjustStatus==3 || adjustStatus==4){
				message = "该物流单已经拆分或者合并过！不能再进行手工拆单操作！";
				isSuccess = false;
			} else {
				LogisticsTab newLogisticsTab = new LogisticsTab();
				try {
					//把logisticsTab里面的值都赋给newLogisticsTab；
					BeanUtils.copyProperties(newLogisticsTab, logisticsTab);
				} catch (IllegalAccessException | InvocationTargetException e2) {
					e2.printStackTrace();
				}
				List<PlatOrders> newPlatOrdersList = new ArrayList<PlatOrders>();
				
				List<PlatOrders> platOrdersList = logisticsTab.getPlatOrdersList();
				for(int i=0;i<platOrdersList.size();i++) {
					PlatOrders platOrders=platOrdersList.get(i);
					Integer splitNum = platOrders.getSplitNum();//该订单项中的拆分数量
					Integer goodNum = platOrders.getGoodNum();//该订单项中的商品数量
					if(splitNum==null || splitNum<=0 || goodNum==null || goodNum<=0 || splitNum>goodNum) {
						continue;
					}else {
						PlatOrders newPlatOrders = new PlatOrders();
						BigDecimal goodMoney = platOrders.getGoodMoney();//商品金额
						BigDecimal payMoney = platOrders.getPayMoney();//实付金额
						try {
							BeanUtils.copyProperties(newPlatOrders, platOrders);//把后面一个参数的属性值都赋值给前面的参数
						} catch (IllegalAccessException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
						newPlatOrders.setGoodNum(splitNum);
						goodNumber = goodNumber+splitNum;
						//除法的时候保留8位小数，舍位的时候四舍五入；
						BigDecimal scale = new BigDecimal(splitNum).divide(new BigDecimal(goodNum),8,BigDecimal.ROUND_HALF_UP);
						BigDecimal multiply = goodMoney.multiply(scale);
						BigDecimal multiply2 = payMoney.multiply(scale);
						newPlatOrders.setGoodMoney(multiply);//新订单项中的商品金额
						goodMoneyAll = goodMoneyAll.add(multiply);
						newPlatOrders.setPayMoney(multiply2);//新订单项中的实付金额
						payMoneyAll = payMoneyAll.add(multiply2);
						newPlatOrdersList.add(newPlatOrders);//添加到对应新商品项的list中
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
					newLogisticsTab.setOrderId(orderId);//新订单中的订单编号
					for(int i=0;i<newPlatOrdersList.size();i++) {
						newPlatOrdersList.get(i).setOrderId(orderId);
					}
					newLogisticsTab.setPlatOrdersList(newPlatOrdersList);
					newLogisticsTab.setPayMoney(payMoneyAll);
					newLogisticsTab.setGoodMoney(goodMoneyAll);
					newLogisticsTab.setGoodNum(new BigDecimal(goodNumber));
					newLogisticsTab.setAdjustStatus(1);//手工拆物流表之后新物流表标记为1；
					splitNewOrder(newLogisticsTab);
					
					logisticsTab.setPlatOrdersList(platOrdersList);
					logisticsTab.setPayMoney(logisticsTab.getPayMoney().subtract(payMoneyAll));
					logisticsTab.setGoodMoney(logisticsTab.getGoodMoney().subtract(goodMoneyAll));
					logisticsTab.setGoodNum(logisticsTab.getGoodNum().subtract(new BigDecimal(goodNumber)));
					logisticsTab.setAdjustStatus(2);//被拆分的物流表被手工拆之后标记为2；（以后不要再拆了，否则拆后新增订单的主键可能会有冲突）；
					splitOldOrder(logisticsTab);
					
//				LogisticsTab logisticsTab=new LogisticsTab();
//				LogisticsTab logisticsTab1=new LogisticsTab();
//				try {
//					BeanUtils.copyProperties(logisticsTab, newPlatOrder);
//					BeanUtils.copyProperties(logisticsTab1, platOrder);
//				} catch (IllegalAccessException | InvocationTargetException e) {
//					e.printStackTrace();
//				}
//				logisticsTabs.add(logisticsTab);
//				logisticsTabs.add(logisticsTab1);
////				logisticsTabDao.deleteByOrderId(platOrder.getOrderId());//新增物流表之前，先删除被拆分订单(原订单)在物流表中的数据；
//				logisticsTabDao.insertList(logisticsTabs);
					message = "手工拆物流表成功！";
					isSuccess = true;
				}else {
					message = "手工拆物流表失败！拆分数量不对";
					isSuccess = true;
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsAdjust/editSplitLogistics", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String showMergeLogistics(String ordrNums) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		if(ordrNums==null || ordrNums=="null") {
			message="没有解析到要展示的物流单号！";
			isSuccess=false;
			try {
				resp = TransformJson.returnRespList("ec/logisticsAdjust/showSplitLogistics",isSuccess,message, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resp;
		}
		String[] split = ordrNums.split(",");
		List<LogisticsTab> list=new ArrayList<LogisticsTab>();
		for(String ordrNum:split) {
			if(ordrNum==null || "".equals(ordrNum)) {
				continue;
			}else {
				LogisticsTab logisticsTab = logisticsTabDao.select(Integer.valueOf(ordrNum));
				list.add(logisticsTab);
			}
		}
		if(list.size()>0) {
			message="获取数据成功！";
			isSuccess=true;
		}else {
			message="没有解析到要展示的物流单号！";
			isSuccess=false;
		}
		try {
			resp = TransformJson.returnRespList("ec/logisticsAdjust/showSplitLogistics",isSuccess,message, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//手工合并物流表
	@Override
	public String editMergeLogistics(List<Map> list, Integer ordrNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<LogisticsTab> logisticsTabs = new ArrayList<LogisticsTab>();
		if(list==null || ordrNum==null || list.size()==0){
			message = "没有主物流单编号" +ordrNum + "或者合并的物流单数据不存在！手工合并失败！";
			isSuccess = false;
		}else{
			LogisticsTab mainlogisticsTab = logisticsTabDao.select(ordrNum);
			mainlogisticsTab.setAdjustStatus(3);//合并的主物流单标志状态为3
			String ecOrderId = mainlogisticsTab.getEcOrderId();
			String orderId = mainlogisticsTab.getOrderId();
			BigDecimal goodNum = mainlogisticsTab.getGoodNum();//合并的主物流单整单商品数量
			BigDecimal goodMoney = mainlogisticsTab.getGoodMoney();//合并的主物流单整单金额
			BigDecimal payMoney = mainlogisticsTab.getPayMoney();//合并的主物流单整单实付金额
			for(Map map:list) {
				Integer adjustStatus = (Integer)map.get("adjustStatus");
				if(map==null || adjustStatus==1|| adjustStatus==2|| adjustStatus==3|| adjustStatus==4) {
					message = "物流单不能为null,或者物流单以前被拆分过，合并过！手工合并失败！";
					isSuccess = false;
					try {
						resp = BaseJson.returnRespObj("ec/logisticsAdjust/editMergeLogistics", isSuccess, message, null);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return resp;
				}
				LogisticsTab logisticsTab = new LogisticsTab();
				try {
					BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
					BeanUtils.populate(logisticsTab, map);
					logisticsTab.setAdjustStatus(4);//被合并的物流单标志状态为4
					logisticsTabs.add(logisticsTab);
					if(ordrNum!=logisticsTab.getOrdrNum()) {//不是主物流单的时候把其他的物流单的电商订单号给拼接上；
						ecOrderId=ecOrderId+","+logisticsTab.getEcOrderId();//拼接电商订单号；
						orderId=orderId+","+logisticsTab.getOrderId();//拼接订单编号；
						goodNum = goodNum.add(logisticsTab.getGoodNum());
						goodMoney = goodMoney.add(logisticsTab.getGoodMoney());
						payMoney = payMoney.add(logisticsTab.getPayMoney());
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			mainlogisticsTab.setEcOrderId(ecOrderId);//电商订单号；
			mainlogisticsTab.setOrderId(orderId);//订单编号
			mainlogisticsTab.setGoodNum(goodNum);
			mainlogisticsTab.setGoodMoney(goodMoney);
			mainlogisticsTab.setPayMoney(payMoney);
			for(LogisticsTab platOrder:logisticsTabs) {
				logisticsTabDao.update(platOrder);
			}
			//往物流表中添加数据之前先根据订单编号（order_id）删除这条数据；(已经合并过的，就不能再合并)
//			for(LogisticsTab logisticsTab:logisticsTabs) {
//				logisticsTabDao.deleteByOrderId(orderId);
//			}
			logisticsTabDao.insert(mainlogisticsTab);
			message = "手工合并物流表成功！";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsAdjust/editMergeLogistics", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	
	
	
	
	//手工拆分物流表保存时，原物流表往数据库中要更新物流信息，并更新子订单表(先删后添加)
	public void splitOldOrder(LogisticsTab logisticsTab) {
//		platOrderDao.delete(platOrder.getOrderId());
		logisticsTabDao.update(logisticsTab);
		platOrdersDao.delete(logisticsTab.getOrderId());
		List<PlatOrders> list=new ArrayList<PlatOrders>();
		for(PlatOrders platOrders:logisticsTab.getPlatOrdersList()) {//删除拆分的物流表中商品项中的空商品项
			if(platOrders!=null) {
				list.add(platOrders);
			}
		}
		platOrdersDao.insert(list);
	}
	//手工拆分物流表保存时，新生成的物流表直接往物流表中添加一条物流信息，并且更新子订单表(先删后添加)
	public void splitNewOrder(LogisticsTab logisticsTab) {
//		platOrderDao.delete(platOrder.getOrderId());
		logisticsTabDao.insert(logisticsTab);
		platOrdersDao.delete(logisticsTab.getOrderId());
		List<PlatOrders> list=new ArrayList<PlatOrders>();
		for(PlatOrders platOrders:logisticsTab.getPlatOrdersList()) {
			if(platOrders!=null) {
				list.add(platOrders);
			}
		}
		platOrdersDao.insert(list);
	}
}
