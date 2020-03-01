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
					goodRecord.setNo(no);//��plat_orders���е�no�ֶ���Ӧ��ǰ�ˣ�
					goodRecords.add(i,goodRecord);
				}else {//û���ҵ����ƽ̨��Ʒ��
					message="û���ҵ��ö����µ�ƽ̨��Ʒ��";
					isSuccess=false;
					break;
				}
			}
		}else {
			isSuccess=false;
			message="��ѯʧ��,"+orderId+",�ö�����û����Ʒ!";
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
					message = "��������" +orderItemId + "�����ڣ���Ʒ����ʧ�ܣ�";
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
				message = "��Ʒ�����ɹ���";
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
			message="��ѯ�ɹ���";
			platOrder.setPlatOrdersList(list);
		}else {
			isSuccess=false;
			message="��ѯʧ��,"+orderId+",�ö�����û����Ʒ��";
		}
		try {
			 resp=BaseJson.returnRespObj("ec/orderAdjust/showSplitOrder",isSuccess,message, platOrder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//�ֹ���ֶ����߼�
	//�ֹ���ֲ�ֵ��Ƕ����еĶ����Ȼ����ݸ����������еĲ����������һ���µĶ�����Ȼ��ԭ�������¶������䶩�����е���Ʒ��������Ʒ��
	//ʵ���������䶩����adjust_status�����б仯�����Ҫ��������������������ݣ�
	@Override
	public String editSplitOrder(PlatOrder platOrder) {
		
		BigDecimal goodMoneyAll = new BigDecimal(0);//�¶����е���Ʒ���
		BigDecimal payMoneyAll = new BigDecimal(0);//�¶����е�ʵ�����
		Integer goodNumber=0;//�¶����е�����
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		String orderId2 = platOrder.getOrderId();
		if(platOrderDao.select(orderId2) == null) {
			message = "�������" +orderId2 + "�����ڣ��ֹ���ʧ�ܣ�";
			isSuccess = false;
		}else {
			PlatOrder newPlatOrder = new PlatOrder();
			try {
				BeanUtils.copyProperties(newPlatOrder, platOrder);
			} catch (IllegalAccessException | InvocationTargetException e2) {
				e2.printStackTrace();
			}
			//��platOrder�����ֵ������newPlatOrder��
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
						BeanUtils.copyProperties(newPlatOrders, platOrders);//�Ѻ���һ������������ֵ����ֵ��ǰ��Ĳ���
					} catch (IllegalAccessException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
					newPlatOrders.setGoodNum(splitNum);
					goodNumber = goodNumber+splitNum;
					//������ʱ����4λС������λ��ʱ���������룻
					BigDecimal scale = new BigDecimal(splitNum).divide(new BigDecimal(goodNum),4,BigDecimal.ROUND_HALF_UP);
					BigDecimal multiply = goodMoney.multiply(scale);
					BigDecimal multiply2 = payMoney.multiply(scale);
					newPlatOrders.setGoodMoney(multiply);//�¶������е���Ʒ���
					goodMoneyAll = goodMoneyAll.add(multiply);
					newPlatOrders.setPayMoney(multiply2);//�¶������е�ʵ�����
					payMoneyAll = payMoneyAll.add(multiply2);
					newPlatOrdersList.add(newPlatOrders);//��ӵ���Ӧlist��
					if(splitNum == goodNum) {//������������ԭ������һ�������Ƴ�ԭ�ж����е���Ʒ��
						platOrdersList.remove(i);
						platOrdersList.add(i, null);
					}else {
						PlatOrders oldPlatOrders = new PlatOrders();//ԭ�������в��֮��ʣ�µ���Ʒ��
						try {
							BeanUtils.copyProperties(oldPlatOrders, platOrders);
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}
						oldPlatOrders.setGoodNum(goodNum-splitNum);
						oldPlatOrders.setGoodMoney(goodMoney.subtract(multiply));//ԭ��������ʣ�µ���Ʒ���
						oldPlatOrders.setPayMoney(payMoney.subtract(multiply2));//ԭ�������е�ʣ��ʵ�����
						platOrdersList.remove(i);
						platOrdersList.add(i, oldPlatOrders);
					}
				}
			}
			List<LogisticsTab> logisticsTabs = new ArrayList<LogisticsTab>();
			if(newPlatOrdersList.size()>0) {
				String orderId=null;
				char charAt = orderId2.charAt(orderId2.length()-2);
				//�����¶���order_id�Ĺ����������ֵĶ�����order_id��β�������ơ�_1�����������ô�ͼ���'_1';
				//�������ֵĶ�����order_id��β�����ơ�_1�������,��ô�����һλ�ַ������־��ۼӣ�
				if(charAt=='_') {
					String substring = orderId2.substring(orderId2.length()-1);
					Integer end = Integer.valueOf(substring);end++;
					orderId = orderId2.substring(0, orderId2.length()-1)+end;
				}else {
					orderId = orderId2+"_1";
				}
				newPlatOrder.setOrderId(orderId);//�¶����еĶ������
				for(int i=0;i<newPlatOrdersList.size();i++) {
					newPlatOrdersList.get(i).setOrderId(orderId);
				}
				newPlatOrder.setPlatOrdersList(newPlatOrdersList);
				newPlatOrder.setPayMoney(payMoneyAll);
				newPlatOrder.setGoodMoney(goodMoneyAll);
				newPlatOrder.setGoodNum(goodNumber);
				newPlatOrder.setAdjustStatus(1);//�ֹ���֮���¶������Ϊ1��
				splitNewOrder(newPlatOrder);
				
				platOrder.setPlatOrdersList(platOrdersList);
				platOrder.setPayMoney(platOrder.getPayMoney().subtract(payMoneyAll));
				platOrder.setGoodMoney(platOrder.getGoodMoney().subtract(goodMoneyAll));
				platOrder.setGoodNum(platOrder.getGoodNum() - goodNumber);
				platOrder.setAdjustStatus(3);//����ֵĶ������ֹ���֮����Ϊ3�����Ժ�Ҫ�ٲ��ˣ������������������������ܻ��г�ͻ����
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
//				logisticsTabDao.deleteByOrderId(platOrder.getOrderId());//����������֮ǰ����ɾ������ֶ���(ԭ����)���������е����ݣ�
				logisticsTabDao.insertList(logisticsTabs);
				message = "�ֹ��𵥳ɹ���";
				isSuccess = true;
			}else {
				message = "�ֹ���ʧ�ܣ������������";
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
			message="û�н�����Ҫչʾ�Ķ����ţ�";
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
			message="��ȡ���ݳɹ���";
			isSuccess=true;
		}else {
			message="û�н�����Ҫչʾ�Ķ����ţ�";
			isSuccess=false;
		}
		try {
			resp = TransformJson.returnRespList("ec/orderAdjust/showMergeOrder",isSuccess,message, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//�ֹ��ϲ��߼�
	@Override
	public String editMergeOrder(List<Map> list,String orderId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<PlatOrder> platOrders = new ArrayList<PlatOrder>();
		List<LogisticsTab> logisticsTabs = new ArrayList<LogisticsTab>();
		if(list==null || orderId==null || "".equals(orderId) || list.size()==0){
			message = "û�����������" +orderId + "���ߺϲ��Ķ������ݲ����ڣ��ֹ��ϲ�ʧ�ܣ�";
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
					platOrder.setAdjustStatus(2);//�ϲ����ı�־״̬Ϊ2
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
			//�����������������֮ǰ�ȸ��ݶ�����ţ�order_id��ɾ���������ݣ�(�Ѿ��ϲ����ģ��Ͳ����ٺϲ�)
			for(LogisticsTab logisticsTab:logisticsTabs) {
//				logisticsTabDao.deleteByOrderId(orderId);
			}
			logisticsTabDao.insertList(logisticsTabs);
			message = "�ֹ��ϲ��ɹ���";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("ec/orderAdjust/editMergeOrder", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	
	//�ֹ���ֶ�������ʱ��ԭ���������ݿ���ҩ���¶������������Ӷ�����(��ɾ�����)
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
		//�ֹ���ֶ�������ʱ�������ɵĶ���ֱ��������������Ӷ��������Ҹ����Ӷ�����(��ɾ�����)
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
