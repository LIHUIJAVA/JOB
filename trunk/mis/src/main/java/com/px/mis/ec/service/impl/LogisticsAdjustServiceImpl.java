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
			message="��ѯʧ��,"+ordrNum+",�����������û����������";
		}else {
			String orderId = logisticsTab.getOrderId();
			List<PlatOrders> list = platOrdersDao.select(orderId);
			if(list!=null && list.size()>0) {
				message="��ѯ�ɹ���";
				logisticsTab.setPlatOrdersList(list);
			}else {
				isSuccess=false;
				message="��ѯʧ��,"+ordrNum+",��������Ķ�����û����Ʒ�";
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
		BigDecimal goodMoneyAll = new BigDecimal(0);//�¶����е���Ʒ���
		BigDecimal payMoneyAll = new BigDecimal(0);//�¶����е�ʵ�����
		Integer goodNumber=0;//�¶����е�����
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		String orderId2 = logisticsTab.getOrderId();
		if(platOrderDao.select(orderId2) == null) {
			message = "�������еĶ������" +orderId2 + "�����ڣ��ֹ���������ʧ�ܣ�";
			isSuccess = false;
		} else {
			Integer adjustStatus = logisticsTab.getAdjustStatus();
			if(adjustStatus==1 || adjustStatus==2 || adjustStatus==3 || adjustStatus==4){
				message = "���������Ѿ���ֻ��ߺϲ����������ٽ����ֹ��𵥲�����";
				isSuccess = false;
			} else {
				LogisticsTab newLogisticsTab = new LogisticsTab();
				try {
					//��logisticsTab�����ֵ������newLogisticsTab��
					BeanUtils.copyProperties(newLogisticsTab, logisticsTab);
				} catch (IllegalAccessException | InvocationTargetException e2) {
					e2.printStackTrace();
				}
				List<PlatOrders> newPlatOrdersList = new ArrayList<PlatOrders>();
				
				List<PlatOrders> platOrdersList = logisticsTab.getPlatOrdersList();
				for(int i=0;i<platOrdersList.size();i++) {
					PlatOrders platOrders=platOrdersList.get(i);
					Integer splitNum = platOrders.getSplitNum();//�ö������еĲ������
					Integer goodNum = platOrders.getGoodNum();//�ö������е���Ʒ����
					if(splitNum==null || splitNum<=0 || goodNum==null || goodNum<=0 || splitNum>goodNum) {
						continue;
					}else {
						PlatOrders newPlatOrders = new PlatOrders();
						BigDecimal goodMoney = platOrders.getGoodMoney();//��Ʒ���
						BigDecimal payMoney = platOrders.getPayMoney();//ʵ�����
						try {
							BeanUtils.copyProperties(newPlatOrders, platOrders);//�Ѻ���һ������������ֵ����ֵ��ǰ��Ĳ���
						} catch (IllegalAccessException | InvocationTargetException e1) {
							e1.printStackTrace();
						}
						newPlatOrders.setGoodNum(splitNum);
						goodNumber = goodNumber+splitNum;
						//������ʱ����8λС������λ��ʱ���������룻
						BigDecimal scale = new BigDecimal(splitNum).divide(new BigDecimal(goodNum),8,BigDecimal.ROUND_HALF_UP);
						BigDecimal multiply = goodMoney.multiply(scale);
						BigDecimal multiply2 = payMoney.multiply(scale);
						newPlatOrders.setGoodMoney(multiply);//�¶������е���Ʒ���
						goodMoneyAll = goodMoneyAll.add(multiply);
						newPlatOrders.setPayMoney(multiply2);//�¶������е�ʵ�����
						payMoneyAll = payMoneyAll.add(multiply2);
						newPlatOrdersList.add(newPlatOrders);//��ӵ���Ӧ����Ʒ���list��
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
					newLogisticsTab.setOrderId(orderId);//�¶����еĶ������
					for(int i=0;i<newPlatOrdersList.size();i++) {
						newPlatOrdersList.get(i).setOrderId(orderId);
					}
					newLogisticsTab.setPlatOrdersList(newPlatOrdersList);
					newLogisticsTab.setPayMoney(payMoneyAll);
					newLogisticsTab.setGoodMoney(goodMoneyAll);
					newLogisticsTab.setGoodNum(new BigDecimal(goodNumber));
					newLogisticsTab.setAdjustStatus(1);//�ֹ���������֮������������Ϊ1��
					splitNewOrder(newLogisticsTab);
					
					logisticsTab.setPlatOrdersList(platOrdersList);
					logisticsTab.setPayMoney(logisticsTab.getPayMoney().subtract(payMoneyAll));
					logisticsTab.setGoodMoney(logisticsTab.getGoodMoney().subtract(goodMoneyAll));
					logisticsTab.setGoodNum(logisticsTab.getGoodNum().subtract(new BigDecimal(goodNumber)));
					logisticsTab.setAdjustStatus(2);//����ֵ��������ֹ���֮����Ϊ2�����Ժ�Ҫ�ٲ��ˣ������������������������ܻ��г�ͻ����
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
////				logisticsTabDao.deleteByOrderId(platOrder.getOrderId());//����������֮ǰ����ɾ������ֶ���(ԭ����)���������е����ݣ�
//				logisticsTabDao.insertList(logisticsTabs);
					message = "�ֹ���������ɹ���";
					isSuccess = true;
				}else {
					message = "�ֹ���������ʧ�ܣ������������";
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
			message="û�н�����Ҫչʾ���������ţ�";
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
			message="��ȡ���ݳɹ���";
			isSuccess=true;
		}else {
			message="û�н�����Ҫչʾ���������ţ�";
			isSuccess=false;
		}
		try {
			resp = TransformJson.returnRespList("ec/logisticsAdjust/showSplitLogistics",isSuccess,message, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//�ֹ��ϲ�������
	@Override
	public String editMergeLogistics(List<Map> list, Integer ordrNum) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<LogisticsTab> logisticsTabs = new ArrayList<LogisticsTab>();
		if(list==null || ordrNum==null || list.size()==0){
			message = "û�������������" +ordrNum + "���ߺϲ������������ݲ����ڣ��ֹ��ϲ�ʧ�ܣ�";
			isSuccess = false;
		}else{
			LogisticsTab mainlogisticsTab = logisticsTabDao.select(ordrNum);
			mainlogisticsTab.setAdjustStatus(3);//�ϲ�������������־״̬Ϊ3
			String ecOrderId = mainlogisticsTab.getEcOrderId();
			String orderId = mainlogisticsTab.getOrderId();
			BigDecimal goodNum = mainlogisticsTab.getGoodNum();//�ϲ�����������������Ʒ����
			BigDecimal goodMoney = mainlogisticsTab.getGoodMoney();//�ϲ������������������
			BigDecimal payMoney = mainlogisticsTab.getPayMoney();//�ϲ���������������ʵ�����
			for(Map map:list) {
				Integer adjustStatus = (Integer)map.get("adjustStatus");
				if(map==null || adjustStatus==1|| adjustStatus==2|| adjustStatus==3|| adjustStatus==4) {
					message = "����������Ϊnull,������������ǰ����ֹ����ϲ������ֹ��ϲ�ʧ�ܣ�";
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
					logisticsTab.setAdjustStatus(4);//���ϲ�����������־״̬Ϊ4
					logisticsTabs.add(logisticsTab);
					if(ordrNum!=logisticsTab.getOrdrNum()) {//��������������ʱ����������������ĵ��̶����Ÿ�ƴ���ϣ�
						ecOrderId=ecOrderId+","+logisticsTab.getEcOrderId();//ƴ�ӵ��̶����ţ�
						orderId=orderId+","+logisticsTab.getOrderId();//ƴ�Ӷ�����ţ�
						goodNum = goodNum.add(logisticsTab.getGoodNum());
						goodMoney = goodMoney.add(logisticsTab.getGoodMoney());
						payMoney = payMoney.add(logisticsTab.getPayMoney());
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			mainlogisticsTab.setEcOrderId(ecOrderId);//���̶����ţ�
			mainlogisticsTab.setOrderId(orderId);//�������
			mainlogisticsTab.setGoodNum(goodNum);
			mainlogisticsTab.setGoodMoney(goodMoney);
			mainlogisticsTab.setPayMoney(payMoney);
			for(LogisticsTab platOrder:logisticsTabs) {
				logisticsTabDao.update(platOrder);
			}
			//�����������������֮ǰ�ȸ��ݶ�����ţ�order_id��ɾ���������ݣ�(�Ѿ��ϲ����ģ��Ͳ����ٺϲ�)
//			for(LogisticsTab logisticsTab:logisticsTabs) {
//				logisticsTabDao.deleteByOrderId(orderId);
//			}
			logisticsTabDao.insert(mainlogisticsTab);
			message = "�ֹ��ϲ�������ɹ���";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsAdjust/editMergeLogistics", isSuccess, message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	
	
	
	
	//�ֹ������������ʱ��ԭ�����������ݿ���Ҫ����������Ϣ���������Ӷ�����(��ɾ�����)
	public void splitOldOrder(LogisticsTab logisticsTab) {
//		platOrderDao.delete(platOrder.getOrderId());
		logisticsTabDao.update(logisticsTab);
		platOrdersDao.delete(logisticsTab.getOrderId());
		List<PlatOrders> list=new ArrayList<PlatOrders>();
		for(PlatOrders platOrders:logisticsTab.getPlatOrdersList()) {//ɾ����ֵ�����������Ʒ���еĿ���Ʒ��
			if(platOrders!=null) {
				list.add(platOrders);
			}
		}
		platOrdersDao.insert(list);
	}
	//�ֹ������������ʱ�������ɵ�������ֱ���������������һ��������Ϣ�����Ҹ����Ӷ�����(��ɾ�����)
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
