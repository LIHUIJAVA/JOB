package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.service.RefundOrderAuditService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.InvtyTabMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.WhsDoc;
@Service
@Transactional
public class RefundOrderAuditServiceImpl implements RefundOrderAuditService {
	@Autowired
	private RefundOrderDao refundOrderDao;//�˿
	@Autowired
	private RefundOrdersDao refundOrdersDao;//�˿�ӱ�
	@Autowired
	private RtnGoodsDao rtnGoodsDao;//�˻���
	@Autowired
	private RtnGoodsSubDao rtnGoodsSubDao;//�˻����ӱ�
	@Autowired
	private SellOutWhsDao sellOutWhsDao;//���۳��ⵥ����
	@Autowired
	private SellOutWhsSubDao sellOutWhsSubDao;//���۳��ⵥ����
	@Autowired
	private MisUserDao misUserDao;//�û���
	@Autowired
	private WhsDocMapper whsDocDao;//�ֿ⵵����
	@Autowired
	private GoodRecordDao goodRecordDao;//ƽ̨��Ʒ������
	@Autowired
	private InvtyTabMapper invtyTabDao;//����
	@Autowired
	private InvtyDocDao invtyDocDao;//���������
	@Autowired
	private StoreRecordDao storeRecordDao;//���̵�����
	@Autowired
	private PlatOrderDao platOrderDao;//����
	@Autowired
	private SellSnglDao sellSnglDao;//���۵�
	@Autowired
	private GetOrderNo getOrderNo;//���ɸ��ֵ���
	@Autowired
	private InvtyTabDao itd;//����
	//����˿��
	@Override
	public String refundOrderAudit(String refId,String userId,String loginDate) {//�˿���
		String resp="";
		String message="";
		Boolean isSuccess = true;
		RefundOrder refundOrder = refundOrderDao.select(refId);
		List<RefundOrders> list = refundOrdersDao.selectList(refId);
		if(refundOrder==null || list==null || list.size()==0) {
			message="�˿���"+refId+"�����ڻ��߸��˿�ӱ����ڣ��˿���ʧ�ܣ�";
			isSuccess=false;
		}else if(refundOrder.getIsAudit()==1){
			message="�˿���"+refId+"�µ��˿�Ѿ���ˣ��˿���ʧ�ܣ�";
			isSuccess=false;
		}else{
			RtnGoods rtnGoods=new RtnGoods();
			String rtnGoodsId=null;//�˻�����
			try {
				rtnGoodsId = getOrderNo.getSeqNo("th", userId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			rtnGoods.setRtnGoodsId(rtnGoodsId);//�˻������
			rtnGoods.setRtnGoodsDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//�������ڣ�
			PlatOrder selectPlatOrder = platOrderDao.select(refundOrder.getOrderId());//��ѯԭ����
			if(selectPlatOrder!=null) {
				rtnGoods.setSellTypId(selectPlatOrder.getSellTypId());//�������ͱ�� ��
				rtnGoods.setBizTypId(selectPlatOrder.getBizTypId());//ҵ�����ͱ�ţ�
				rtnGoods.setRecvSendCateId(selectPlatOrder.getRecvSendCateId());//�շ�����ţ�
			}
			StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
			if(storeRecord!=null) {
				rtnGoods.setCustId(storeRecord.getEcId());//�ͻ����    ����ƽ̨���(���̵���)
			}
			rtnGoods.setAccNum(userId);//ҵ��Ա���
			MisUser misUser = misUserDao.select(userId);
			if(misUser!=null) {
				rtnGoods.setUserName(misUser.getUserName());//�û�����
			}
			rtnGoods.setSetupPers(userId);//������  �û����
			rtnGoods.setSetupTm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//����ʱ�䣻
			//�ջ���ַ����
			//�жϲֿ�����Ƿ�һ�£������һ�£����ʧ��
			String whsEncd=list.get(0).getRefWhs();
			for(RefundOrders refundOrders:list) {
				if(whsEncd==null ||!whsEncd.equals(refundOrders.getRefWhs()) ) {
					try {
						message="�˻��ֿ����Ϊnull,����"+whsEncd+"��һ�£��˿���ʧ�ܣ�";
						isSuccess=false;
						resp = BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit", isSuccess, message, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return resp;
				}
			}
			WhsDoc whsDoc = whsDocDao.selectWhsDoc(whsEncd);
			if(whsDoc!=null) {
				rtnGoods.setDelvAddrNm(whsDoc.getWhsNm());//������ַ���ֿ��ַ�������˿�ӱ��е��˻��ֿ�����ȡ��
			}
			//���۶����� ���ݶ�����Ų�ѯ���۵���
			SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(refundOrder.getOrderId());
			if(sellSngl!=null) {
				rtnGoods.setSellOrdrId(sellSngl.getSellSnglId());//���۶��������۵���
			}
			rtnGoods.setMemo(refundOrder.getMemo());//��ע��
			rtnGoods.setIsNtBllg(0);//�Ƿ���Ҫ��Ʊ��
			List<RtnGoodsSub> rtnGoodsSubs=new ArrayList<RtnGoodsSub>();
			for(RefundOrders refundOrders:list) {
				RtnGoodsSub rtnGoodsSub=new RtnGoodsSub();
				rtnGoodsSub.setWhsEncd(refundOrders.getRefWhs());//�ֿ��ţ�
				//������� ƽ̨��Ʒ��good_record�е�ƽ̨��Ʒ���룻
				String goodId = refundOrders.getGoodId();//��Ʒ���
				GoodRecord goodRecord = goodRecordDao.select(goodId);
				String ecGoodId=null;
				if(goodRecord!=null){
					ecGoodId = goodRecord.getEcGoodId();
					rtnGoodsSub.setInvtyEncd(ecGoodId);//�������		ƽ̨��Ʒ����
				}
				rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());//�˻��������ţ�
				Integer refNum2 = refundOrders.getRefNum();//�˻�����
				BigDecimal refMoney = refundOrders.getRefMoney();//�˿��
				Integer canRefNum2 = refundOrders.getCanRefNum();//���˻�����
				if(canRefNum2<refNum2) {//�˻��������ܴ��ڿ��˻�����
					try {
						message="�˻�����"+refNum2+"���ڿ��˻�����"+canRefNum2+",�˿���ʧ�ܣ�";
						isSuccess=false;
						resp = BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit", isSuccess, message, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return resp;
				}
				BigDecimal refNum = new BigDecimal(refNum2);
				//��Decimal���෴����ֻ��Ҫ����negate������
				rtnGoodsSub.setQty(refNum.negate());//���� ����
				rtnGoodsSub.setBatNum(refundOrders.getBatchNo());//����
				//��ʵ�����۽�������˰�ʵȼ���һϵ�����ݣ�
				//�˿�������˿����������˻�������8λС������λ��ʱ���������룻
				BigDecimal refPrice = refMoney.divide(refNum,8,BigDecimal.ROUND_HALF_UP);//�˻����� 	��˰����
//				InvtyTab invtyTab = new InvtyTab();
//				String whsEncd2 = rtnGoodsSub.getWhsEncd();
//				invtyTab.setWhsEncd(whsEncd2);
				String invtyEncd = rtnGoodsSub.getInvtyEncd();
//				invtyTab.setInvtyEncd(invtyEncd);
//				String batNum = rtnGoodsSub.getBatNum();
//				invtyTab.setBatNum(batNum);
//				invtyTab = invtyTabDao.selectByReverse(invtyTab);
				InvtyDoc invtyDocOne = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyEncd);
				BigDecimal taxRate=null;
				if(invtyDocOne!=null) {
					taxRate = invtyDocOne.getOptaxRate();//˰��  ��������е�����˰��
				}
				if(refPrice!=null && refNum!=null && taxRate!=null) {
					BigDecimal noTaxAmt = CalcAmt.noTaxAmt(refPrice, refNum);//��˰���
					BigDecimal taxAmt = CalcAmt.taxAmt(refPrice, refNum,taxRate);//˰��
					BigDecimal cntnTaxUprc = CalcAmt.cntnTaxUprc(refPrice, refNum,taxRate);//��˰����
					BigDecimal prcTaxSum = CalcAmt.prcTaxSum(refPrice, refNum,taxRate);//��˰�ϼ�
					
					rtnGoodsSub.setCntnTaxUprc(cntnTaxUprc);//��˰���ۣ�
					rtnGoodsSub.setPrcTaxSum(prcTaxSum);//��˰�ϼƣ�
					rtnGoodsSub.setNoTaxAmt(noTaxAmt);//��˰��
					rtnGoodsSub.setNoTaxUprc(refPrice);//��˰����
					rtnGoodsSub.setTaxAmt(taxAmt);//˰��
					rtnGoodsSub.setTaxRate(taxRate);//˰��
				}
//				rtnGoodsSub.setPrdcDt(invtyTab.getPrdcDt());//�������ڣ�
//				rtnGoodsSub.setBaoZhiQi(Integer.valueOf(invtyTab.getBaoZhiQi()));//������
//				rtnGoodsSub.setInvldtnDt(invtyTab.getInvldtnDt());//ʧЧ���ڣ�
				
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(ecGoodId);
				String invtyClsEncd=null;
				if(invtyDoc!=null) {
					invtyClsEncd = invtyDoc.getInvtyClsEncd();//���������룻
				}
				if(invtyClsEncd==null) {
					
				}else if(invtyClsEncd.startsWith("100")) {
					rtnGoodsSub.setIsComplimentary(1);//����Ʒ��
				}else {
					rtnGoodsSub.setIsComplimentary(0);//������Ʒ��
				}
				if(refNum2 >0) {//�ж��˻����ӱ���Ƿ��˻����ֶ����ݣ�
					rtnGoodsSub.setIsNtRtnGoods(1);//�Ƿ��˻�    0�����˻���1���˻�
				}else {
					rtnGoodsSub.setIsNtRtnGoods(0);//�Ƿ��˻�    0�����˻���1���˻�
				}
				rtnGoodsSubs.add(rtnGoodsSub);//��ӵ��˻����ӱ���
			}
			rtnGoodsDao.insertRtnGoods(rtnGoods);//�����˻�������
			rtnGoodsSubDao.insertRtnGoodsSub(rtnGoodsSubs);//�����˻����ӱ�
			//�����������˻�����
			
			
			//=======================���ɺ������۳��ⵥ===================================
			Integer isRefund = refundOrder.getIsRefund();
			//���۵�����ʵ��
			SellOutWhs sellOutWhs = new SellOutWhs();
			//���۵��ӱ�ʵ��
			List<SellOutWhsSub> sellOutWhsSub = new ArrayList<>();
			//���ʵ��
			InvtyTab invtyTab = new InvtyTab();
			if(isRefund==1) {//�Ƿ��˻�	���˻���ʱ�����ɺ������۳��ⵥ��
				//���ɺ������۳��ⵥ��
				//��ȡ������
			    String number=getOrderNo.getSeqNo("CK", userId);
			    if (sellOutWhsDao.selectSellOutWhsByOutWhsId(number)!=null){//���ظ���?????
					message="���"+number+"�Ѵ��ڣ����������룡";
					isSuccess=false;
				}else {
					sellOutWhs.setOutWhsId(number);
					//��������ӦΪ��˻�ʵ�ʳ������ڣ���
					sellOutWhs.setOutWhsDt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
					sellOutWhs.setAccNum(rtnGoods.getAccNum());//�û����
					sellOutWhs.setUserName(rtnGoods.getUserName());
					sellOutWhs.setCustId(rtnGoods.getCustId());
					sellOutWhs.setBizTypId(rtnGoods.getBizTypId());
					sellOutWhs.setSellTypId(rtnGoods.getSellTypId());
					sellOutWhs.setRecvSendCateId(rtnGoods.getRecvSendCateId());
					sellOutWhs.setOutIntoWhsTypId("���۳���");
					sellOutWhs.setSellOrdrInd(rtnGoods.getSellOrdrId());//���۵���ʶ
					sellOutWhs.setSetupPers(rtnGoods.getSetupPers());//������  
					/*sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));//����ʱ�� */	
					sellOutWhs.setMemo(rtnGoods.getMemo());//��ע��Ϣ
					sellOutWhs.setRtnGoodsId(rtnGoods.getRtnGoodsId());
					/*String sellOrdrId = sellOutWhs.getSellOrdrInd();*/
					for(RtnGoodsSub rGSub:rtnGoodsSubs) {
						invtyTab.setWhsEncd(rGSub.getWhsEncd());
						invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
						invtyTab.setBatNum(rGSub.getBatNum());
						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						invtyTab.setAvalQty(rGSub.getQty());
						//�ڿ����н���Ӧ�Ŀ��������٣������˻����е������Ǹ��������Կ�����Ҫ����������
						itd.updateInvtyTabAvalQtyJian(invtyTab);
						SellOutWhsSub sOutWhsSub = new SellOutWhsSub();
						sOutWhsSub.setInvtyEncd(rGSub.getInvtyEncd());//�������
						sOutWhsSub.setWhsEncd(rGSub.getWhsEncd());//�ֿ⵵��
						sOutWhsSub.setBatNum(rGSub.getBatNum());//����
						sOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//���۳��ⵥ�ӱ���������
						sOutWhsSub.setQty(rGSub.getQty());//����   ��ȡ���� sowSub.getQty();
						sOutWhsSub.setTaxRate((rGSub.getTaxRate()).divide(new BigDecimal(100)));//˰��   
//						sOutWhsSub.setPrdcDt(rGSub.getPrdcDt());//��������
//						sOutWhsSub.setBaoZhiQi(rGSub.getBaoZhiQi());//������
//						sOutWhsSub.setInvldtnDt(CalcAmt.getDate(sOutWhsSub.getPrdcDt(), sOutWhsSub.getBaoZhiQi()));
//						BigDecimal noTaxUprc = sellOutWhsDao.selectSellOutWhsSubByInWhBn(sOutWhsSub);
//						if(noTaxUprc!=null) {
//							//��ȡ��˰����
//							sOutWhsSub.setNoTaxUprc(noTaxUprc);  
//						}
						//����δ˰���  ���=δ˰����*δ˰����
						sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
						//����˰��  ˰��=δ˰���*˰��
						sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
						//���㺬˰����  ��˰����=��˰����*˰��+��˰����
						sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
						//�����˰�ϼ�  ��˰�ϼ�=��˰���*˰��+��˰���=˰��+��˰���
						sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
						sOutWhsSub.setIsNtRtnGoods(1);//�Ƿ��˻�
						sOutWhsSub.setIsComplimentary(rGSub.getIsComplimentary());//�Ƿ���Ʒ
						sOutWhsSub.setMemo(rGSub.getMemo());//��ע
						sellOutWhsSub.add(sOutWhsSub);
					}
					sellOutWhsDao.insertSellOutWhs(sellOutWhs);
					sellOutWhsSubDao.insertSellOutWhsSub(sellOutWhsSub);
				    isSuccess=true;
				    message = "�˻�������˳ɹ���";
				}
			}else {//�����˿��ʱ�����ɺ������۳��ⵥ��
				
			}
			//====================================================================
			//�Ƿ���� ��������������ô����
			rtnGoods.setIsNtChk(1);//�˻��������
			rtnGoods.setAccNum(userId);//�����
			rtnGoods.setChkTm(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));//�������
			rtnGoodsDao.updateRtnGoodsByRtnGoodsId(rtnGoods);//�����˻��������״̬��
			refundOrder.setIsAudit(1);
			refundOrderDao.update(refundOrder);//�����˿�����״̬��
			message="�˻��������ɣ��˿��˳ɹ���";
			isSuccess=true;
		}
//		refundOrder.setRefundOrders(selectList);
		try {
			 resp=BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit",isSuccess,message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//�����˿��
	@Override
	public String refundOrderAbandonAudit(String refId, String userId) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		//���۳��ⵥ����ʵ��
		SellOutWhs sellOutWhs = new SellOutWhs();
//		//���۳��ⵥ�ӱ�ʵ��
//		List<SellOutWhsSub> sellOutWhsSub = new ArrayList<>();
		//���ʵ��
		InvtyTab invtyTab = new InvtyTab();
		RefundOrder refundOrder = refundOrderDao.select(refId);//��ѯ�˿
		if(refundOrder == null) {
			message="�˿�����ڣ��޷����󣡣���";
			isSuccess = false;
		}else {
			//�����˿���ȥ���˻���
			RtnGoods rtnGoods=new RtnGoods();
			rtnGoods = rtnGoodsDao.selectRtnGoodsByRefId(refId);//��ѯ�˻���
			//sellOutWhs = sellOutWhsDao.selectSellOutWhsByRtnGoodsId(rtnGoods.getRtnGoodsId());
			//ͨ���˻�����ȡ�˻����ӱ���Ϣ
			List<RtnGoodsSub> rtnGoodsSubs = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(rtnGoods.getRtnGoodsId());
			for(RtnGoodsSub rGSub:rtnGoodsSubs) {
				invtyTab.setWhsEncd(rGSub.getWhsEncd());
				invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
				invtyTab.setBatNum(rGSub.getBatNum());
				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
				if(invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == 1
						|| invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == 0) {
					if(sellOutWhs.getIsNtChk()==0) {
						//��Decimal���෴����ֻ��Ҫ����negate������
						invtyTab.setAvalQty(rGSub.getQty().negate());//�˿������Ϊ����
						itd.updateInvtyTabAvalQtyJia(invtyTab);//�޸Ŀ���еĿ�������
						sellOutWhsDao.deleteSellOutWhsByOutWhsId(sellOutWhs.getOutWhsId());//ɾ���������۳��ⵥ��
						rtnGoodsDao.deleteRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());//ɾ���˻�����
						
						refundOrder.setIsAudit(0);//�˿�����״̬��Ϊδ��ˣ�
						refundOrderDao.update(refundOrder);//�޸��˿�����״̬��
						isSuccess=true;
						message = "�˿����ɹ���";
					}else {
						message = "�˿��Ӧ�ĺ������۳��ⵥ����ˣ��޷������˿";
					}
				}else if(invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == -1) {
					isSuccess=false;
					message = "�������������㣬�޷������˿";
				}
			}
		}
		try {
			 resp=BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAbandonAudit",isSuccess,message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
}
