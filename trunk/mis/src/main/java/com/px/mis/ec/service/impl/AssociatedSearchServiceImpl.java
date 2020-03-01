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
	private SellSnglDao sellSnglDao;//���۵� (����û�����۶��������۷�������,�����ŵ��ϳ����۵��ˡ�)
	@Autowired
	private SellSnglSubDao sellSnglSubDao;//���۵��ӵ� (����û�����۶��������۷�������,�����ŵ��ϳ����۵��ˡ�)
	@Autowired
	private SellOutWhsDao sellOutWhsDao;//���۳��ⵥ
	@Autowired
	private SellOutWhsSubDao sellOutWhsSubDao;//���۳��ⵥ�ֱ�
	@Autowired
	private RtnGoodsDao rtnGoodsDao;//�˻���
	@Autowired
	private PlatOrderDao platOrderDao;//����
	@Autowired
	private PlatOrdersDao platOrdersDao;//�����ӵ�
	@Autowired
	private LogisticsTabDao logisticsTabDao;//������
	@Autowired
	private PickSnglMapper pickSnglDao;//�����
	@Autowired
	private InvtyTabMapper invtyTabDao;//����
	@Autowired
	private MisUserDao misUserDao;//����
	@Autowired
	private WhsDocMapper whsDocDao;//�ֿ⵵����
	@Autowired
	private StoreRecordDao storeRecordDao;//���̵�����
	@Autowired
	private GoodRecordDao goodRecordDao;//ƽ̨��Ʒ������
	@Autowired
	private InvtyDocDao invtyDocDao;//���������
	@Autowired
	private ProdStruMapper prodStruDao;//��Ʒ�ṹ
	@Autowired
	private InvtyTabService invtyTabService;//����
	@Autowired
	private PlatOrderService platOrderService;//����
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
	//���ϲ�ѯ
	@Override
	public String quickSearchByOrderId(String orderId) {
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		List<Associated> list=new ArrayList<Associated>();
		if(orderId == null || platOrderDao.select(orderId)==null) {
			isSuccess=false;
			message="�������"+orderId+"������,����ʧ�ܣ�";
		}else {
			SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderId);
			if(sellSngl==null) {
				isSuccess=false;
				message="���۵�������������"+orderId+"�����۵�������,����ʧ�ܣ�";
			}else {
				String sellSnglId = sellSngl.getSellSnglId();
				Associated associated=new Associated();
				associated.setName("���۵�");
				associated.setOrderCode(sellSnglId);
				associated.setSetupPers(sellSngl.getSetupPers());
				associated.setSetupTm(sellSngl.getSetupTm());
				associated.setChkr(sellSngl.getChkr());
				associated.setChkTm(sellSngl.getChkTm());
				list.add(associated);
				
				SellOutWhs sellOutWhs = sellOutWhsDao.selectSellOutWhsBySellSnglId(sellSnglId);
				if(sellOutWhs == null) {
					isSuccess=false;
					message="���۳��ⵥ��������۶�����"+sellSnglId+"�����۳��ⵥ������,����ʧ�ܣ�";
				}else {
					Associated associatedOne=new Associated();
					associatedOne.setName("���۳��ⵥ");
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
					message="�˻�����������۶�����"+sellSnglId+"���˻���������,����ʧ�ܣ�";
				}else {
					Associated associatedTwo=new Associated();
					associatedTwo.setName("�˻���");
					associatedTwo.setOrderCode(rtnGoods.getRtnGoodsId());
					associatedTwo.setSetupPers(rtnGoods.getSetupPers());
					associatedTwo.setSetupTm(rtnGoods.getSetupTm());
					associatedTwo.setChkr(rtnGoods.getChkr());
					associatedTwo.setChkTm(rtnGoods.getChkTm());
					list.add(associatedTwo);
				}
				isSuccess=true;
				message="����ɹ���";
			}
		}
		
		try {
			resp = TransformJson.returnRespList("ec/associatedSearch/quickSearchByOrderId", isSuccess, message, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//һ���������
	@Override
	public String reverseOperationByOrderId(String orderId) {
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		PlatOrder platOrder = platOrderDao.select(orderId);
		if(platOrder == null || platOrder.getIsAudit()==0) {//������Ų����ڣ��޷����������
			isSuccess=false;
			message="�������"+orderId+"�����ڻ��߸ö���״̬Ϊδ���,������ѡ�񶩵����������ʧ�ܣ�";
		}else {
			SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderId);
			if(sellSngl==null) {//������Ŷ�Ӧ�����۵������ڣ�˵��������û����ˣ��ǿ϶���û�м���������۳��ⵥ��
				isSuccess=false;
				message="�������"+orderId+"��Ӧ�����۵�������,�������ʧ�ܣ�";
			}else {//���������֮��Ż��������۵������û���������۵���������һ��û����ˣ�
				String sellSnglId = sellSngl.getSellSnglId();
				SellOutWhs sellOutWhs = sellOutWhsDao.selectSellOutWhsBySellSnglId(sellSnglId);
				if(sellOutWhs!=null && sellOutWhs.getIsNtChk()==1) {//���۳��ⵥ�����,�޷����������
					isSuccess=false;
					message="���۵���"+sellSnglId+"��Ӧ�����۳��ⵥ�����,�޷����������";
				}else{
					if(sellOutWhs==null) {
						isSuccess=false;
						message="���۵���"+sellSnglId+"��Ӧ�����۳��ⵥ�����ڣ�";
					}else {
						String outWhsId = sellOutWhs.getOutWhsId();
						sellOutWhsDao.deleteSellOutWhsByOutWhsId(outWhsId);
						sellOutWhsSubDao.deleteSellOutWhsSubByOutWhsId(outWhsId);
					}
					if(sellSngl.getIsNtChk()==1) {//���۵�����ˣ���Ҫ�޸Ŀ��Ŀ�������
						//�޸Ŀ��Ŀ����������ı京˰�����δ˰�������ݣ�
						List<SellSnglSub> sellSnglSubs = sellSnglSubDao.selectSellSnglSubBySellSnglId(sellSnglId);
						if(sellSnglSubs != null && sellSnglSubs.size()>0) {
							for(SellSnglSub sellSnglSub:sellSnglSubs) {
								InvtyTab invtyTab = new InvtyTab();
								invtyTab.setWhsEncd(sellSnglSub.getWhsEncd());
								invtyTab.setInvtyEncd(sellSnglSub.getInvtyEncd());
								invtyTab.setBatNum(sellSnglSub.getBatNum());
								invtyTab = invtyTabDao.selectByReverse(invtyTab);
								if(invtyTab==null) {//�������۳��ⵥ�ֱ��еĲֿ���룬������룬����û��ƥ���Ͽ����е����ݣ�
									continue;
								}else {
									BigDecimal qty = sellSnglSub.getQty();
									invtyTab.setAvalQty(qty);//���ⵥ�ӱ��еĿ���������Ҫ���ϵ�������
									invtyTabDao.updateByReverse(invtyTab);//���¿����еĿ�������
								}
							}
						}
					}
					//ɾ�����۵�
					sellSnglDao.deleteSellSnglBySellSnglId(sellSnglId);
					sellSnglSubDao.deleteSellSnglSubBySellSnglId(sellSnglId);
					//ɾ�������
					pickSnglDao.deletePickSnglTabBySellSnglId(sellSnglId);
					//ɾ��������
					logisticsTabDao.deleteBySellSnglId(sellSnglId);
					Integer isAudit = platOrder.getIsAudit();//�����Ƿ����
					if(isAudit==1) {//��������ˣ����¶��������״̬
						platOrder.setIsAudit(0);//�Ƿ����	0��δ��ˣ�1������ˡ�
						platOrderDao.update(platOrder);
					}
					message="��������ɹ���";
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
	//�������
	@Override
	public String orderAuditByOrderId(String orderIds,String accNum,String loginDate) {
		//System.out.println("������ˣ����������orderIds+++"+orderIds);
		String resp = "";
		String message = "";
		boolean isSuccess=true;
		int successcount = 0;//��˳ɹ���������
		int fallcount = 0;//���ʧ������
		String[] split = orderIds.split(",");
		MisUser misUser = misUserDao.select(accNum);
		for(String orderId:split) {
			PlatOrder platOrder = platOrderDao.select(orderId);
			/*
			 * String time = platOrder.getPayTime(); if(StringUtils.isEmpty(time)) { time =
			 * platOrder.getTradeDt(); }
			 */
			if(formBookService.isMthSeal(loginDate)) {
				//���������Ѳ����������۵�
				platOrder.setAuditHint("�������ʧ�ܣ���¼������Ӧ�·��ѷ���");
				platOrderDao.update(platOrder);//���¶����������ʾ��
				//��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				//MisUser misUser = misUserDao.select(accNum);
				if(misUser!=null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�������ʧ�ܣ���¼������Ӧ�·��ѷ���");
				logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logRecord.setOperatType(2);//2���
				logRecord.setTypeName("���");
				logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			List<PlatOrders> list = platOrdersDao.select(orderId);
			if(platOrder==null || list==null || list.size()==0) {
				//message="�������"+orderId+"�����ڻ��߸ö����µ���Ʒ�����ڣ��������ʧ�ܣ�";
				fallcount++;
				continue;
			}else if(platOrder.getIsAudit()==1){
				//message="�������"+orderId+"�µĶ����Ѿ���ˣ��������ʧ�ܣ�";
				fallcount++;
				continue;
			}else{
				boolean flag = true;//�жϹ��������Ƿ���ֹ����󣬳��ִ���ʱ��������ѭ����ֱ�������һ�Ŷ���
				String invid="";
				try {
				for (int i = 0; i < list.size(); i++) {
					if(StringUtils.isEmpty(list.get(i).getInvId())) {
						//��������ϸ�к���δƥ�䵽����������ϸʱ��������ˣ�������ǰ���������һ��
						platOrder.setAuditHint("�����д���δƥ�䵽��������ļ�¼");
						platOrderDao.update(platOrder);//���¶����������ʾ��
						//��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�������ʧ�ܣ������д���δƥ�䵽��������ļ�¼");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2���
						logRecord.setTypeName("���");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
						logRecordDao.insert(logRecord);
						fallcount++;
						flag=false;
						break;
					}else if(StringUtils.isEmpty(list.get(i).getBatchNo())) {
							
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(list.get(i).getInvId());
							if(invtyDoc==null) {
								platOrder.setAuditHint("�ڴ��������δƥ�䵽������룺"+list.get(i).getInvId());
								platOrderDao.update(platOrder);//���¶����������ʾ��
								//��־��¼
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if (misUser != null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("�������ʧ�ܣ�" + "�ڴ��������δƥ�䵽������룺"+list.get(i).getInvId());
								logRecord.setOperatTime(
										new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2���
								logRecord.setTypeName("���");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
								logRecordDao.insert(logRecord);
								fallcount++;
								flag=false;
								break;
							}
							invid=invtyDoc.getInvtyEncd();
							
								if (invtyDoc.getShdTaxLabour() == 0) {
									//��������ϸ��δƥ�䵽���εĴ������������ʱ
									//��������ϸ�к���δƥ�䵽�������ʱ��������ˣ�������ǰ���������һ��
									platOrder.setAuditHint(list.get(i).getInvId() + ":δƥ�䵽����");
									platOrderDao.update(platOrder);//���¶����������ʾ��
									//��־��¼
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(accNum);
									//MisUser misUser = misUserDao.select(accNum);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("�������ʧ�ܣ�" + list.get(i).getInvId() + ":δƥ�䵽����");
									logRecord.setOperatTime(
											new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									logRecord.setOperatType(2);//2���
									logRecord.setTypeName("���");
									logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
									logRecordDao.insert(logRecord);
									fallcount++;
									flag = false;
									break;
								} 
							
						}else if(StringUtils.isEmpty(list.get(i).getDeliverWhs())) {
							//�����ֿⲻ��Ϊ��
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(list.get(i).getInvId());
							invid=invtyDoc.getInvtyEncd();
							if (invtyDoc.getShdTaxLabour()==0) {
								//����Ӧ˰����
								//�������ֿ�Ϊ��ʱ��������ˣ�������ǰ���������һ��
								platOrder.setAuditHint("��������Ʒ�����ֿⲻ��Ϊ��");
								platOrderDao.update(platOrder);//���¶����������ʾ��
								//��־��¼
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if (misUser != null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("�������ʧ�ܣ���������Ʒ�����ֿⲻ��Ϊ��");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2���
								logRecord.setTypeName("���");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
								logRecordDao.insert(logRecord);
								fallcount++;
								flag = false;
								break;
							}
						}
				}
				} catch (Exception e) {
					// TODO: handle exception
				
					platOrder.setAuditHint("���������룺"+invid + "�Ƿ�������Ʒ��������");
					platOrderDao.update(platOrder);//���¶����������ʾ��
					//��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					//MisUser misUser = misUserDao.select(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�������ʧ�ܣ����������룺"+invid + "�Ƿ�������Ʒ��������");
					logRecord.setOperatTime(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(2);//2���
					logRecord.setTypeName("���");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
					logRecordDao.insert(logRecord);
					fallcount++;
					flag = false;
					break;
				}
				if(!flag) {
					continue;
				}
				
				//��������Ϊ0
				if(platOrder.getGoodNum()==0) {
					//��������Ϊ0��������ˣ�������ǰ���������һ��
					platOrder.setAuditHint("�������ʧ�ܣ���Ʒ����Ϊ0");
					platOrderDao.update(platOrder);//���¶����������ʾ��
					//��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					//MisUser misUser = misUserDao.select(accNum);
					if(misUser!=null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�������ʧ�ܣ���Ʒ����Ϊ0");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(2);//2���
					logRecord.setTypeName("���");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				int refundCount = refundOrderDao.selectCountByEcOrderId(platOrder.getEcOrderId());
				if(refundCount>0) {
					//���������˿���������
					platOrder.setAuditHint("���������˿���������");
					platOrderDao.update(platOrder);//���¶����������ʾ��
					//��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					//MisUser misUser = misUserDao.select(accNum);
					if(misUser!=null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�������ʧ�ܣ����������˿���������");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(2);//2���
					logRecord.setTypeName("���");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//�Է���������ݹ�˾���벻��Ϊ��
				if(platOrder.getDeliverSelf()==1&&StringUtils.isEmpty(platOrder.getExpressCode())) {
					int aa = 0;//������������Ʒ����
					for (int i = 0; i < list.size(); i++) {
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(list.get(i).getInvId());
						if(invtyDoc.getShdTaxLabour()==0) {
							aa++;
						}
					}
					if(aa<list.size()) {
						//��������Ʒ������С�ڶ�����ϸ����ʱ���˵������п�ݹ�˾����
						//���Է���������ݹ�˾Ϊ��ʱ��������ˣ�������ǰ���������һ��
						platOrder.setAuditHint("�Է���������ݹ�˾����Ϊ��");
						platOrderDao.update(platOrder);//���¶����������ʾ��
						//��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�������ʧ�ܣ��Է���������ݹ�˾Ϊ��");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2���
						logRecord.setTypeName("���");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
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
						platOrder.setAuditHint("�������ʧ�ܣ��������۵���ʧ��");
						platOrderDao.update(platOrder);//���¶����������ʾ��
						//��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�������ʧ�ܣ��������۵���ʧ��");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2���
						logRecord.setTypeName("���");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
						logRecordDao.insert(logRecord);
						fallcount++;
						continue;
					}
					sellSngl.setSellSnglId(sellSnglId);//���۵����
					sellSngl.setDeliverSelf(platOrder.getDeliverSelf());//�������۵��Ƿ��Է���
				/*
				 * if(StringUtils.isEmpty(platOrder.getPayTime())) {
				 * sellSngl.setSellSnglDt(platOrder.getTradeDt());//���۵����� ������ʱ��Ϊ��ʱ���µ�ʱ�� }else {
				 * sellSngl.setSellSnglDt(platOrder.getPayTime());//���۵����� ����ʱ����Ϊ���۵������� }
				 */
				/*
				 * String aaa = loginDate.split(" ")[0]+sdf.format(new Date()).substring(10);
				 * System.out.println(aaa);
				 */
					sellSngl.setSellSnglDt(loginDate.split(" ")[0]+sdf.format(new Date()).substring(10));//���۵���������˵�¼���ڼ�ϵͳʱ����
					sellSngl.setAccNum(accNum);//�û����
					
					
					StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
					if (StringUtils.isEmpty(storeRecord.getRespPerson())) {
						platOrder.setAuditHint("�������ʧ�ܣ���Ӧ���̵���������Ϊ�գ���������");
						platOrderDao.update(platOrder);//���¶����������ʾ��
						//��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						//MisUser misUser = misUserDao.select(accNum);
						if(misUser!=null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�������ʧ�ܣ���Ӧ���̵���������Ϊ�գ���������");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(2);//2���
						logRecord.setTypeName("���");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
						logRecordDao.insert(logRecord);
						fallcount++;
						continue;
					}
					if(storeRecord!=null) {
						if(StringUtils.isNotEmpty(storeRecord.getCustomerId())) {
							sellSngl.setCustId(storeRecord.getCustomerId());//�ͻ����    
						}else {
							
							platOrder.setAuditHint("�������ʧ�ܣ����̶�Ӧ�ͻ���ϢΪ�գ���������");
							platOrderDao.update(platOrder);//���¶����������ʾ��
							//��־��¼
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							//MisUser misUser = misUserDao.select(accNum);
							if(misUser!=null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("�������ʧ�ܣ����̶�Ӧ�ͻ���ϢΪ�գ���������");
							logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							logRecord.setOperatType(2);//2���
							logRecord.setTypeName("���");
							logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
							logRecordDao.insert(logRecord);
							fallcount++;
							continue;
						}
					}
					
					
					
					
					
					sellSngl.setBizTypId(platOrder.getBizTypId());//ҵ�����ͱ��
					sellSngl.setSellTypId(platOrder.getSellTypId());//�������ͱ��
					sellSngl.setRecvSendCateId(platOrder.getRecvSendCateId());//�շ������
					
					for(PlatOrders platOrders:list) {
						//System.err.println("whsEncd\t"+whsEncd);
						//System.err.println("platOrders.getDeliverWhs()\t"+platOrders.getDeliverWhs());
						if(StringUtils.isEmpty(platOrders.getDeliverWhs())) {
							//�������ֿ����Ϊ��ʱ�жϴ���Ƿ���������Ʒ������������Ʒʱ��ʾ�����ֿⲻ��Ϊ��
							
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.getInvId());
							if (invtyDoc.getShdTaxLabour()==0) {
								try {
									//message="�ֿ����Ϊnull,����"+whsEncd+"��һ�£��������ʧ�ܣ�";
									platOrder.setAuditHint("��������Ʒ�����ֿⲻ��Ϊ��");
									platOrderDao.update(platOrder);//���¶����������ʾ��

									//��־��¼
									LogRecord logRecord = new LogRecord();
									logRecord.setOperatId(accNum);
									//MisUser misUser = misUserDao.select(accNum);
									if (misUser != null) {
										logRecord.setOperatName(misUser.getUserName());
									}
									logRecord.setOperatContent("�������ʧ�ܣ���������Ʒ�����ֿⲻ��Ϊ�գ��������ʧ�ܣ�");
									logRecord.setOperatTime(
											new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
									logRecord.setOperatType(2);//2���
									logRecord.setTypeName("���");
									logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
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
					//�жϲֿ�����Ƿ�һ�£������һ�£����ʧ��
					/*String whsEncd=list.get(0).getDeliverWhs();
					
					if(StringUtils.isEmpty(platOrder.getDeliverWhs())) {
						platOrder.setAuditHint("������Ϊ��");
						platOrderDao.update(platOrder);//���¶����������ʾ��
						fallcount++;
						flag=false;
					}*/
					if(!flag) {
						continue;
					}
					
					List<SellSnglSub> subs=new ArrayList<SellSnglSub>();//�ӱ�list��Ϊ�˴��pto�������ʱһ��������ϸ���������ӱ�
					if(flag) {
						
						if (StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
							WhsDoc whsDoc = whsDocDao.selectWhsDoc(platOrder.getDeliverWhs());
							sellSngl.setDelvAddr(whsDoc.getWhsAddr());//������ַ���ֿ��ַ�����ݶ����ӱ��еĲֿ�����ȡ��
						}
						sellSngl.setTxId(platOrder.getOrderId());//�������
						sellSngl.setEcOrderId(platOrder.getEcOrderId());//���̶�����
						sellSngl.setIsNtBllg(platOrder.getIsInvoice());//�Ƿ�Ʊ
						sellSngl.setIsNtChk(0);//�Ƿ���� ���Ϊ0:δ��ˣ�
						sellSngl.setSetupPers(accNum);//������  �û����
						
						
						MisUser mis = misUserDao.select(storeRecord.getRespPerson());
						if(mis==null) {
							//��ѯ�����û�����
							platOrder.setAuditHint("�������ʧ�ܣ����̶�Ӧ���������û�������Ϊ��");
							platOrderDao.update(platOrder);//���¶����������ʾ��
							//��־��¼
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							//MisUser misUser = misUserDao.select(accNum);
							if(misUser!=null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("�������ʧ�ܣ����̶�Ӧ���������û�������Ϊ��");
							logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							logRecord.setOperatType(2);//2���
							logRecord.setTypeName("���");
							logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
							logRecordDao.insert(logRecord);
							fallcount++;
							continue;
						}
						sellSngl.setDeptId(mis.getDepId());//����id
						sellSngl.setAccNum(storeRecord.getRespPerson());//ҵ��Ա
						sellSngl.setUserName(mis.getUserName());//ҵ��Ա����
						sellSngl.setFormTypEncd("007");//��������
						sellSngl.setSetupTm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//����ʱ�䣻
						sellSngl.setRecvr(platOrder.getRecName());//�ռ���������
						sellSngl.setRecvrTel(platOrder.getRecMobile());//�ռ��˵绰
						sellSngl.setRecvrAddr(platOrder.getRecAddress());//�ռ��˵�ַ��
		//		    private String recvrEml;//�ռ�������
						sellSngl.setBuyerNote(platOrder.getBuyerNote());//�������
						
						//��ʼ֮ǰ�ȼ��㶩��ÿ����ϸ��ʵ����
						//list = platOrderService.jisuanPayPrice(platOrder.getOrderId());
						/*List<SellSnglSub> subs=new ArrayList<SellSnglSub>();//�ӱ�list��Ϊ�˴��pto�������ʱһ��������ϸ���������ӱ�
						List<SellSnglSub> subs1=new ArrayList<SellSnglSub>();//�ӱ�list��Ϊ�˴��pto�������ʱ������˰�����ӱ�
						List<SellSnglSub> subs2=new ArrayList<SellSnglSub>();//�ӱ�list��Ϊ�˴��pto�������ʱ��ͬ���������ʱ����ȡ���μ�Ч�ں���ӱ�
						List<SellSnglSub> sellSnglSubs=new ArrayList<SellSnglSub>();*/
						List<PlatOrders> platOrders1 = platOrdersDao.platOrdersByInvIdAndBatch(platOrder.getOrderId());
						
						for(PlatOrders platOrders:platOrders1) {
							
							//platOrdersDao.updatePayMoney(platOrders);
							SellSnglSub sellSnglSub=new SellSnglSub();
							sellSnglSub.setWhsEncd(platOrder.getDeliverWhs());//�ֿ��ţ�
							
							sellSnglSub.setSellSnglId(sellSngl.getSellSnglId());//���۵���ţ�
							sellSnglSub.setPrcTaxSum(platOrders.getPayMoney());//����ÿ����ϸ��ʵ�����
							
							if (platOrders.getPayMoney().compareTo(BigDecimal.ZERO)>0) {
								sellSnglSub.setIsComplimentary(0);//�����Ƿ���Ʒ
							}else {
								sellSnglSub.setIsComplimentary(1);//�����Ƿ���Ʒ
							}
							sellSnglSub.setBatNum(platOrders.getBatchNo());//����
							InvtyDoc in = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.getInvId());
							InvtyCls ic = invtyClsDao.selectInvtyClsByInvtyClsEncd(in.getInvtyClsEncd());
						
						  if(StringUtils.isEmpty(ic.getProjEncd())) {
						  platOrder.setAuditHint("��Ʒ��"+platOrders.getInvId()+"��Ӧ������Ŀ����Ϊ�գ���������");
						  platOrderDao.update(platOrder);//���¶����������ʾ��
						  
						  //��־��¼ 
						  LogRecord logRecord = new LogRecord();
						  logRecord.setOperatId(accNum);
						  //MisUser misUser = misUserDao.select(accNum); 
						  if (misUser != null) {
						  logRecord.setOperatName(misUser.getUserName()); }
						  logRecord.setOperatContent("���ʧ�ܣ���Ʒ��"+platOrders.getInvId()+"��Ӧ������Ŀ����Ϊ�գ���������"
						  ); logRecord.setOperatTime( new
						  SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						  logRecord.setOperatType(2);//2��� logRecord.setTypeName("���");
						  logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
						  logRecordDao.insert(logRecord);
						  
						  fallcount++; 
						  flag = false; 
						  break;
						  
						  }else { 
							  sellSnglSub.setProjEncd(ic.getProjEncd());//������Ŀ����
						  }
						 
							if (StringUtils.isNotEmpty(platOrders.getPrdcDt())) {
								sellSnglSub.setPrdcDt(platOrders.getPrdcDt());//��������
								sellSnglSub.setInvldtnDt(platOrders.getInvldtnDt());//ʧЧ����
								
								sellSnglSub.setBaoZhiQi(Integer.parseInt(in.getBaoZhiQiDt()));//����������
							}
							sellSnglSub.setInvtyEncd(platOrders.getInvId());//���������Ʒ����
							sellSnglSub.setQty(new BigDecimal(platOrders.getInvNum()));//����
							sellSnglSub.setUnBllgQty(sellSnglSub.getQty());//δ��Ʊ����
							sellSnglSub.setRtnblQty(sellSnglSub.getQty());//���ÿ�������
							sellSnglSub.setHadrtnQty(new BigDecimal(0));//�������˻�����
							
							if(in.getBxRule()!=null&&in.getBxRule().compareTo(BigDecimal.ZERO)>0) {
								//��������
								//System.err.println(sellSnglSub.getQty()+"\t"+in.getBxRule());
								sellSnglSub.setBxQty(sellSnglSub.getQty().divide(in.getBxRule(),2,BigDecimal.ROUND_HALF_DOWN));
							}
							
							//sellSnglSub.setWhsEncd(whsEncd);//���òֿ����
							sellSnglSub=countPrice(sellSnglSub);
							if(sellSnglSub.getTaxRate()==null) {
								//�������굥�ۺ� ����˰��Ϊ��ʱ��˵����Ӧ���������˰��Ϊ��
								platOrder.setAuditHint("�������ʧ�ܣ����"+sellSnglSub.getInvtyEncd()+"δ��������˰��");
								platOrderDao.update(platOrder);//���¶����������ʾ��
								
								//��־��¼
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if(misUser!=null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("�������ʧ�ܣ����"+sellSnglSub.getInvtyEncd()+"δ��������˰��");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2���
								logRecord.setTypeName("���");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
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
					//List<SellSnglSub> subs2=new ArrayList<SellSnglSub>();//�ӱ�list��Ϊ�˴��pto�������ʱ��ͬ���������ʱ����ȡ���μ�Ч�ں���ӱ�
						
						if (flag) {
							//TODO
							//System.out.println("��ʼ�����۵���������ݡ�������������������");
							//subs2 = salesPromotionActivityUtil.matchSalesPromotion(platOrder, list, sellSngl,subs2);//ƥ������
							
							
							//���¶��������״̬��
							platOrder.setIsAudit(1);
							platOrder.setAuditHint("");//��ն����ϵ������ʾ��Ϣ
							platOrder.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
							int re = platOrderDao.updateAudit(platOrder);//���¶��������״̬��
							if (re==1) {
								//����״̬���³ɹ�
								sellSnglDao.insertSellSngl(sellSngl);//�����۵������һ�����ݣ�
								sellSnglSubDao.insertSellSnglSub(subs);//���۵��ӱ����������
								//��־��¼
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if(misUser!=null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("������˳ɹ�");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2���
								logRecord.setTypeName("���");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
								logRecordDao.insert(logRecord);
								successcount++;
							}else {
								//����״̬����ʧ��
								//��־��¼
								LogRecord logRecord = new LogRecord();
								logRecord.setOperatId(accNum);
								//MisUser misUser = misUserDao.select(accNum);
								if(misUser!=null) {
									logRecord.setOperatName(misUser.getUserName());
								}
								logRecord.setOperatContent("�������ʧ��,�ö����ѱ���һ����Ա���");
								logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
								logRecord.setOperatType(2);//2���
								logRecord.setTypeName("���");
								logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
								logRecordDao.insert(logRecord);
								fallcount++;
								
								
							}
							
						}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/associatedSearch/orderAuditByOrderId", true, "��˶�����ɣ�������˳ɹ�"+successcount+"����ʧ��"+fallcount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//��������
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
				//message="�������"+orderId+"��Ӧ�Ķ���Ϊδ���״̬����������û�����壡";
				//����δ���
				platOrder.setAuditHint("�˶�����δ��ˣ���������");
				platOrderDao.update(platOrder);//���¶����������ʾ��
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�˶�����δ��ˣ���������");
				logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				logRecord.setOperatType(3);// 3 ����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
				logRecordDao.insert(logRecord);
				fallcount++;
			}else{
				SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(orderids[i]);
				if(sellSngl==null) {
					platOrder.setIsAudit(0);//��������״̬��Ϊδ���״̬��
					platOrder.setAuditHint("");//��ն����ϵ������ʾ
					platOrder.setAuditTime(null);//������ʱ��
					platOrderDao.update(platOrder);
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("��������ɹ�");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(3);// 3 ����
					logRecord.setTypeName("����");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
					logRecordDao.insert(logRecord);
					successcount++;
				}else if(sellSngl.getIsNtChk()==1) {
					//��Ӧ�����۵��Ѿ���ˣ���������ʧ�ܣ�
					platOrder.setAuditHint("��Ӧ�����۵��Ѿ���ˣ���������ʧ�ܣ�");
					platOrderDao.update(platOrder);//���¶����������ʾ��

					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("��Ӧ�����۵��Ѿ���ˣ���������ʧ�ܣ�");
					logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					logRecord.setOperatType(3);// 3 ����
					logRecord.setTypeName("����");
					logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
					logRecordDao.insert(logRecord);
					fallcount++;
				}else {
					
				   int count = refundOrderDao.selectCountByEcOrderId(platOrder.getEcOrderId());
					if(count>0) {
						//��Ӧ�����۵��Ѿ���ˣ���������ʧ�ܣ�
						platOrder.setAuditHint("���������˿������ʧ��");
						platOrderDao.update(platOrder);//���¶����������ʾ��

						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("��������ʧ�ܣ������˿");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(3);// 3 ����
						logRecord.setTypeName("����");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
						logRecordDao.insert(logRecord);
						fallcount++;
					}else {
						sellSnglDao.deleteSellSnglBySellSnglId(sellSngl.getSellSnglId());
						platOrder.setIsAudit(0);//��������״̬��Ϊδ���״̬��
						platOrder.setAuditHint("");//��ն����ϵ������ʾ
						platOrder.setAuditTime(null);//������ʱ��
						platOrderDao.update(platOrder);
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("��������ɹ�");
						logRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
						logRecord.setOperatType(3);// 3 ����
						logRecord.setTypeName("����");
						logRecord.setOperatOrder(platOrder.getEcOrderId());//��������
						logRecordDao.insert(logRecord);
						successcount++;
					}
					
				}
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/associatedSearch/orderAbandonAuditByOrderId", isSuccess, "������ɣ����γɹ�����"+successcount+"����ʧ��"+fallcount+"��", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	//��PTO������������ɶ��������ӵ���ȥ��
	public List<SellSnglSub> checkPTO(SellSnglSub nowsub,PlatOrders orders,List<ProdStruSubTab> subs){
		List<SellSnglSub> sellSnglSubs = new ArrayList<>();
		//����PTO����Ķ����Ӽ��Ĳο��ɱ�*������������̯ÿ����ϸ��Ӧ��ʵ�����
		//�����Ӽ��ο��ɱ����Ӽ��������ܳɱ�
		boolean flag = true;//������꣬���false˵����Ӧ�Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���
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
				//ѭ��ÿ���Ӽ�
				nowsub.setInvtyEncd(subs.get(i).getSubEncd());//�Ӽ�����
				nowsub.setQty(new BigDecimal(orders.getGoodNum()).multiply(subs.get(i).getSubQty()));
				//�Ӽ�����*�ο��ɱ�
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(subs.get(i).getSubEncd());
				BigDecimal cost = subs.get(i).getSubQty().multiply(invtyDoc.getRefCost());
				if(i+1==subs.size()) {
					//���һ��ʱ���ü���
					nowsub.setPrcTaxSum(orders.getPayMoney().subtract(costed));//���ü�˰�ϼ�
				}else {
					//����ռ��
					BigDecimal thismoney = orders.getPayMoney().multiply(cost).divide(total);
					costed = costed.add(new BigDecimal(dFormat.format(thismoney)));
					nowsub.setPrcTaxSum(thismoney);//���ü�˰�ϼ�
				}
				nowsub.setWhsEncd(orders.getDeliverWhs());//���òֿ����
				if(invtyDoc.getBxRule()!=null||invtyDoc.getBxRule().compareTo(BigDecimal.ZERO)>0) {
					//��������
					nowsub.setBxQty(nowsub.getQty().divide(invtyDoc.getBxRule(),2,BigDecimal.ROUND_HALF_DOWN));
					nowsub.setRtnblQty(nowsub.getQty());
					nowsub.setHadrtnQty(new BigDecimal(0));
				}
				sellSnglSubs.add(nowsub);
			}
		}else {
			//flag  falseʱ���list ���ؿյ�list
			sellSnglSubs.clear();
		}
		return sellSnglSubs;
	}
	/*private BigDecimal cntnTaxUprc;//��˰����

	    private BigDecimal prcTaxSum;//��˰�ϼ�

	    private BigDecimal noTaxUprc;//��˰����

	    private BigDecimal noTaxAmt;//��˰���

	    private BigDecimal taxAmt;//˰��

	    private BigDecimal taxRate;//˰��
*/	
	//���������ӵ��ĺ�˰�����㺬˰���ۣ���˰���ۣ�˰��
	public SellSnglSub countPrice(SellSnglSub sub) {
		BigDecimal paymoney = sub.getPrcTaxSum();//��˰�ϼƵ����û���ʵ�����
		DecimalFormat dFormat = new DecimalFormat("0.00");
		paymoney=new BigDecimal(dFormat.format(paymoney));
		InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(sub.getInvtyEncd());
		if(invtyDoc.getOptaxRate()==null) {
			return sub;
		}
		sub.setTaxRate(invtyDoc.getOptaxRate());//����˰��
		if (paymoney.compareTo(BigDecimal.ZERO)>0) {
			//System.out.println("ʵ����"+paymoney);
			//��ȡ˰�ʳ�100
			BigDecimal rate = invtyDoc.getOptaxRate().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_DOWN);
			
			//������˰���
			BigDecimal notaxmoney = paymoney.divide((new BigDecimal("1").add(rate)), 2, BigDecimal.ROUND_HALF_DOWN);
			//����˰��
			BigDecimal taxmoney = paymoney.subtract(notaxmoney);
			//���㺬˰����
			BigDecimal taxprice = paymoney.divide(sub.getQty(), 8, BigDecimal.ROUND_HALF_DOWN);
			//������˰����
			BigDecimal notaxprice = notaxmoney.divide(sub.getQty(), 8, BigDecimal.ROUND_HALF_DOWN);
			sub.setCntnTaxUprc(taxprice);//��˰����
			sub.setNoTaxUprc(notaxprice);//��˰����
			sub.setTaxAmt(taxmoney);//˰��
			sub.setNoTaxAmt(notaxmoney);//��˰���
		}else {
			sub.setCntnTaxUprc(BigDecimal.ZERO);//��˰����
			sub.setNoTaxUprc(BigDecimal.ZERO);//��˰����
			sub.setTaxAmt(BigDecimal.ZERO);//˰��
			sub.setNoTaxAmt(BigDecimal.ZERO);//��˰���
		}
		return sub;
	}
	@Override
	public String selectList(Map map) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
