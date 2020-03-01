package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.CancelOrderDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.CancelOrder;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.CancelOrderService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.entity.SellSnglSub;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.util.JacksonUtil;

@Transactional
@Service
public class CancelOrderServiceImpl implements CancelOrderService {
	
	private Logger logger = LoggerFactory.getLogger(CancelOrderServiceImpl.class);
	
	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private CancelOrderDao cancelOrderDao;

	@Autowired
	private PlatOrdersDao platOrdersDao;
	
	@Autowired
	private StoreSettingsDao storeSettingsDao;
	
	@Autowired
	private StoreRecordDao storeRecordDao;
	
	@Autowired
	private RefundOrderDao refundOrderDao;
	
	@Autowired
	private RefundOrdersDao refundOrdersDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private GetOrderNo getOrderNo;
    @Autowired
    private SellSnglDao sellSnglDao;
    @Autowired
    private SellSnglSubDao sellSnglSubDao;
    @Autowired
    private LogRecordDao logRecordDao;
    @Autowired
    private InvtyDocDao invtyDocDao;
	private List<CancelOrder> cancelOrderList;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public int audit(List<CancelOrder> cancelOrders,String accNum,String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int count = 0;
		List<String> okids = new ArrayList<String>();
		try {
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			MisUser misUser = misUserDao.select(accNum);
			String date=sdf.format(new Date());
			if (cancelOrders.size() > 0) {
				List<RefundOrder> refundOrderList = new ArrayList();
				List<RefundOrders> refundOrdersList = new ArrayList();
				for (CancelOrder cancelOrder : cancelOrders) {
					if(platOrderDao.checkExsits(cancelOrder.getOrderId(), cancelOrder.getStoreId())==0) {
						//���ݵ�ǰȡ�������еĶ����Ų�ѯԭ����
						//�������ֵΪ0ʱ��˵��û���ҵ�ԭ������Ӧ������
						/*cancelOrder.setAuditHint("δ�ҵ���Ӧԭ������������ˡ�");
						cancelOrderDao.update(cancelOrder);*/
						cancelOrderDao.delete(cancelOrder);
						continue;
					}
					/*if(platOrderDao.selectNoAuditCountByOrderIdAndStoreId(cancelOrder.getStoreId(), cancelOrder.getOrderId())!=0){
						//����Ӧԭ��������δ��˵Ķ���ʱ
						//������˴�ȡ������
						cancelOrder.setAuditHint("��Ӧԭ������δ��ˣ��˵�������ˡ�");
						cancelOrderDao.update(cancelOrder);
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿����ʧ�ܣ����̣�"+storeRecord.getStoreName()+",ԭ��˵����"+cancelOrder.getOrderId()+"����δ���");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿�����");
						logRecord.setOperatOrder(cancelOrder.getOrderId());
						logRecordDao.insert(logRecord);
						cancelOrderDao.delete(cancelOrder);
						continue;
					}*/
					
					List<PlatOrders> platOrdersList=platOrdersDao.selectByEcOrderId(cancelOrder.getOrderId());
							//platOrdersDao.select(cancelOrder.getOrderId());
					if (platOrdersList.size()==0) {
						/*cancelOrder.setAuditHint("��Ӧԭ����û�ж�����ϸ���˵�������ˡ�");
						cancelOrderDao.update(cancelOrder);*/
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿����ʧ�ܣ����̣�"+storeRecord.getStoreName()+",ԭ��˵����"+cancelOrder.getOrderId()+"����û�ж�����ϸ");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿�����");
						logRecord.setOperatOrder(cancelOrder.getOrderId());
						logRecordDao.insert(logRecord);
						cancelOrderDao.delete(cancelOrder);
						continue;
					}
					boolean flag = true;
					for (int i = 0; i < platOrdersList.size(); i++) {
						//ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
						if(!StringUtils.isNotEmpty(platOrdersList.get(i).getInvId())) {
							flag=false;
							break;
						}
					}
					if(!flag) {
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������"+cancelOrder.getOrderId()+"����δƥ�䵽��������ļ�¼����������ȡ������ʧ��");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(cancelOrder.getOrderId());
						logRecordDao.insert(logRecord);
						//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
						cancelOrderDao.delete(cancelOrder);
						continue;
					}
					RefundOrder refundOrder = new RefundOrder();
					String refId=getOrderNo.getSeqNo("tk", accNum);
					refundOrder.setRefId(refId);
					
					refundOrder.setStoreId(cancelOrder.getStoreId());
//					private String storeName;//��������
					refundOrder.setEcRefId(cancelOrder.getId());
					refundOrder.setApplyDate(cancelOrder.getApplyTime());
					refundOrder.setBuyerId(cancelOrder.getBuyerId());
					refundOrder.setIsRefund(1);
					refundOrder.setRefReason(cancelOrder.getReason());
//				    private String refExplain;//�˿�˵��
					refundOrder.setRefStatus(1);
					refundOrder.setTreDate(date);
					refundOrder.setOperator(misUser.getUserName());
					refundOrder.setOperatorId(misUser.getAccNum());
					refundOrder.setOperatorTime(date);
					refundOrder.setOrderId(cancelOrder.getOrderId());//����ƽ̨������
					refundOrder.setEcOrderId(cancelOrder.getOrderId());//����ƽ̨������
					refundOrder.setSource(2);//�����˿��ԴΪ���������ۺ�
					refundOrder.setSourceNo(""+cancelOrder.getId());//������Դ���ݺ�Ϊȡ����������
					refundOrder.setStoreName(storeRecord.getStoreName());//��������
					refundOrder.setDownTime(date);//��������ʱ��Ϊ��ǰʱ��
					refundOrder.setIsAudit(0);
//				    
					
					int allRefNum=0;
					BigDecimal allRefMoney=new BigDecimal(0);
					for (PlatOrders platOrders : platOrdersList) {
						RefundOrders refundOrders = new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodId(platOrders.getInvId());
						refundOrders.setIsGift(platOrders.getIsGift());//�˿�Ƿ���Ʒȡԭ������
//					    private String goodName;//��Ʒ����
						//refundOrders.setGoodSku(platOrders.getGoodSku());
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.getInvId());
						refundOrders.setGoodName(invtyDoc.getInvtyNm());
						refundOrders.setCanRefNum(platOrders.getCanRefNum());//���ÿ�������
						refundOrders.setRefNum(platOrders.getInvNum());//�����˻�����
						refundOrders.setCanRefMoney(platOrders.getCanRefMoney());//���ÿ��˽��
						if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//���ñ�����
						}
						refundOrders.setPrdcDt(platOrders.getPrdcDt());//������������
						refundOrders.setInvldtnDt(platOrders.getInvldtnDt());//����ʧЧ����
						refundOrders.setGoodSku(platOrders.getGoodSku());//sku
						refundOrders.setEcGoodId(platOrders.getGoodId());//spu
						refundOrders.setRefMoney(platOrders.getPayMoney());//�����˻����
//					    private String batchNo;//����
//					    private String refWhs;//�˻��ֿ�
//					    private String memo;//��ע
						refundOrders.setBatchNo(platOrders.getBatchNo());//��������
						refundOrders.setRefWhs(platOrders.getDeliverWhs());//ѡ���Ӧ�������õ�Ĭ���˻���
						allRefNum+=refundOrders.getRefNum();//�ۼ������˻�����
						allRefMoney=allRefMoney.add(refundOrders.getRefMoney());//�ۼ������˻����
						refundOrdersList.add(refundOrders);
					}
					refundOrder.setTreDate(cancelOrder.getCheckTime());//����ʱ����ƽ̨�����ʱ��
				    refundOrder.setAllRefNum(allRefNum);
				    refundOrder.setAllRefMoney(allRefMoney);
				    refundOrderList.add(refundOrder);
				    okids.add(cancelOrder.getId());//������ɹ���ȡ������id���뵽�ɹ��б��У���������״̬
				}
				if (refundOrderList.size()>0) {
					refundOrderDao.insertList(refundOrderList);
					refundOrdersDao.insertList(refundOrdersList);
					count = cancelOrderDao.audit(okids, misUser.getAccNum(), misUser.getUserName(), date);
				}
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�˿���أ����γɹ����ؾ������̣�"+storeRecord.getStoreName()+",ȡ������"+refundOrderList.size()+"��");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12�˿�����
				logRecord.setTypeName("�˿�����");
				logRecordDao.insert(logRecord);
			}
		/*	message = "�ɹ����" + count + "��ȡ��������ʧ��+"+ids.length+"��";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/cancelOrder/audit", isSuccess, message, null);*/
		}catch(Exception e){
			logger.error("URL:ec/cancelOrder/audit �쳣˵����",e);
			throw new RuntimeException();
		}
		return okids.size();
	}

	@Override
	public String queryList(String jsonBody) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			List<CancelOrder> cancelOrderList = cancelOrderDao.selectList(map);
			int count = cancelOrderDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/cancelOrder/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, cancelOrderList);
		} catch (IOException e) {
			logger.error("URL:ec/cancelOrder/queryList �쳣˵����",e);
		}
		return resp;
	}

	@Override
	public int download(String storeId,String ecorderId,String accNum,String startDate,String endDate) {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		int pageNo=1;
		int pageSize=50;
		cancelOrderList = new ArrayList();
		int count=0;
		StoreRecord storeRecord = storeRecordDao.select(storeId);
	    StoreSettings storeSettings = storeSettingsDao.select(storeId);
		try {
			List<Map<String, String>> mapList = storeSettingsDao.selectRelevantEC();
			if ("JD".equals(storeRecord.getEcId())) {
				JDdownload(storeSettings, storeRecord,ecorderId, pageNo, pageSize,startDate,endDate);
			
				if (cancelOrderList.size() == 0) {
					/*isSuccess = true;
					message = "������ɣ�û����Ҫ���ص�ȡ��������";*/
				} else {
					cancelOrderDao.insert(cancelOrderList);
					count=audit(cancelOrderList, accNum, storeId);
					/*isSuccess = true;
					message = "���سɹ������ι�����"+cancelOrderList.size()+"��ȡ��������";*/
				}
			}else {
				/*isSuccess = false;
				message = "��ѡ�񾩶�ƽ̨�ĵ�������ȡ������";*/
			}
			/*resp = BaseJson.returnRespObj("ec/cancelOrder/download", isSuccess, message, null);*/
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/cancelOrder/download �쳣˵����",e);
		}
		return count;
	}

	private void JDdownload(StoreSettings storeSettings,StoreRecord storeRecord,String ecorderId, int pageNo, int pageSize,String startDate,String endDate)
			throws NoSuchAlgorithmException, IOException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageIndex", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		if (ecorderId.length()>0) {
			//��ƽ̨�����ų��ȴ���0ʱ��˵������ֵ
			//��Ҫ�������ż����ѯ������
			jdReq.put("orderId", ecorderId);
		}
		if (startDate.length()==0||endDate.length()==0) {
			Date date = new Date();
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			cale.add(Calendar.DAY_OF_YEAR, -6);
			jdReq.put("applyTimeStart", sdf.format(cale.getTime()));
			jdReq.put("applyTimeEnd", sdf.format(date));
		}else {
			jdReq.put("applyTimeStart",startDate );
			jdReq.put("applyTimeEnd", endDate);
		}
		//���ص�ʱ�򲻴�ȡ��������״̬��ţ�����ʱ�ж�,
		//�Ǵ���״̬��ʱ�򲻲������ݿ�
		//����ȡ��������˽�����1,3,5,6,7,9
		String jdRespStr = ECHelper.getJD("jingdong.pop.afs.soa.refundapply.queryPageList", storeSettings,
				jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		JsonNode resultJson = jdRespJson.get("jingdong_pop_afs_soa_refundapply_queryPageList_responce");
		if (resultJson.get("code").asText().equals("0")) {
			ArrayNode resultList = (ArrayNode) resultJson.get("queryResult").get("result");
			if (resultList.size() > 0) {
				for (Iterator<JsonNode> resultIterator = resultList.iterator(); resultIterator.hasNext();) {
					JsonNode result = resultIterator.next();
					if (result.get("status").asInt()==1||result.get("status").asInt()==3||result.get("status").asInt()==5
							||result.get("status").asInt()==6||result.get("status").asInt()==7||result.get("status").asInt()==9) {
						//���ݵ�ǰ��ȡ������id��ѯ�Ƿ��Ѵ������ݿ⣬������ʱ����
						if (cancelOrderDao.selectById(result.get("id").asText())==null) {
							CancelOrder cancelOrder = new CancelOrder();
							if (result.get("id") != null) {
								cancelOrder.setId(result.get("id").asText());// �˿���
							}
							if (result.get("buyerId") != null) {
								cancelOrder.setBuyerId(result.get("buyerId").asText());// �ͻ��˺�
							}
							if (result.get("buyerName") != null) {
								cancelOrder.setBuyerName(result.get("buyerName").asText());// �ͻ�����
							}
							if (result.get("checkTime") != null) {
								cancelOrder.setCheckTime(result.get("checkTime").asText());// ���ʱ��
							}
							if (result.get("applyTime") != null) {
								cancelOrder.setApplyTime(result.get("applyTime").asText());// ����ʱ��
							}
							if (result.get("applyRefundSum") != null) {
								cancelOrder.setApplyRefundSum(result.get("applyRefundSum").decimalValue());// �˿���
							}
							if (result.get("status") != null) {
								cancelOrder.setStatus(result.get("status").asInt());// ���״̬
							}
							if (result.get("checkUserName") != null) {
								cancelOrder.setCheckUserName(result.get("checkUserName").asText());// �����
							}
							if (result.get("orderId") != null) {
								cancelOrder.setOrderId(result.get("orderId").asText());// ������
							}
							if (result.get("checkRemark") != null) {
								cancelOrder.setCheckRemark(result.get("checkRemark").asText());// ��˱�ע
							}
							if (result.get("reason") != null) {
								cancelOrder.setReason(result.get("reason").asText());// �˿�ԭ��
							}
							if (result.get("systemId") != null) {
								cancelOrder.setSystemId(result.get("systemId").asInt());// �˿���Դ
							}
							cancelOrder.setIsAudit(0);// ���ؿ����״̬
							cancelOrder.setStoreId(storeRecord.getStoreId());// ���̱��
							cancelOrder.setEcId("JD");// ����ƽ̨���
							cancelOrder.setDownloadTime(sdf.format(new Date()));
							cancelOrderList.add(cancelOrder);
						}
					}
				}
				int totalcount = resultJson.get("queryResult").get("totalCount").asInt();
				if(totalcount-pageNo*pageSize>0) {
					JDdownload(storeSettings, storeRecord, ecorderId, pageNo+1, pageSize, startDate, endDate);
				}
			}
		}
	}

}
