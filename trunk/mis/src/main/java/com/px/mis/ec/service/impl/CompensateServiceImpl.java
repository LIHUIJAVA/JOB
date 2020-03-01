package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.px.mis.ec.dao.CompensateDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.Compensate;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.CompensateService;
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
public class CompensateServiceImpl implements CompensateService {

	private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);
	
	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private CompensateDao compensateDao;

	@Autowired
	private StoreSettingsDao storeSettingsDao;
	
	@Autowired
	private RefundOrderDao refundOrderDao;
	
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private SellSnglDao sellSnglDao;
	@Autowired
	private RefundOrdersDao refundOrdersDao;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	private List<Compensate> compensateList;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public int audit(List<Compensate> compensates,String accNum,String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int count = 0;
		List<String> okids=new ArrayList<String>();
		try {
			MisUser misUser = misUserDao.select(accNum);
			String date=sdf.format(new Date());
			if (compensates.size() > 0) {
				List<RefundOrder> refundOrderList = new ArrayList();
				List<RefundOrders> refundOrderss = new ArrayList<RefundOrders>();
				for (Compensate compensate : compensates) {
					/*if (compensate.getIsAudit()==1) {
						//����ѡ����⸶��״̬Ϊ�����ʱ�����������´���
						continue;
					}*/
					List<PlatOrder> platOrders = platOrderDao.selectPlatOrdersByEcOrderId(compensate.getOrderId());
					if(platOrders.size()==0) {
						/*compensate.setAuditHint("�⸶����Ӧԭ������ɾ�����⸶��������ˡ�");
						compensateDao.update(compensate);*/
						continue;
					}
					boolean flag = true;
					for (int i = 0; i < platOrders.size(); i++) {
						/*if (platOrders.get(i).getIsAudit() == 0) {
							compensate.setAuditHint("�⸶����Ӧԭ����δ��ˣ��⸶��������ˡ�");
							compensateDao.update(compensate);
							compensateDao.delete(compensate);
							// ��־��¼
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							if (misUser != null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("�˿���أ�������"+compensate.getOrderId()+"δ��ˣ��⸶��"+compensate.getCompensateId()+"�������أ���������ʧ�ܡ�");
							logRecord.setOperatTime(sdf.format(new Date()));
							logRecord.setOperatType(12);// 12�˿�����
							logRecord.setTypeName("�˿����");
							logRecord.setOperatOrder(compensate.getOrderId());
							logRecordDao.insert(logRecord);
							flag=false;
							break;
						}*/ 
					if(!flag) {
						continue;
					}
					okids.add(compensate.getCompensateId());
					/*SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(platOrder.getOrderId());//���ݶ����Ų�ѯ��Ӧ���۵�
					List<SellSnglSub> sellSnglSubs = compensateDao.selectSellSnglSubBySellSnglId(sellSngl.getSellSnglId());*/
					RefundOrder refundOrder = new RefundOrder();
					String refId=getOrderNo.getSeqNo("tk", accNum);
					refundOrder.setRefId(refId);// �˿���
					refundOrder.setStoreId(compensate.getStoreId());// ���̱��
					refundOrder.setEcRefId(compensate.getCompensateId());//�����˿�� �⸶������
					refundOrder.setOrderId(platOrders.get(0).getOrderId());//���̶�����
					refundOrder.setSource(3);//�����˿��ԴΪ�����⸶
					refundOrder.setSourceNo(""+compensate.getCompensateId());//������Դ���ݺ�Ϊ�����ۺ�ķ��񵥺�
					StoreRecord storeRecord = storeRecordDao.select(compensate.getStoreId());
					refundOrder.setStoreName(storeRecord.getStoreName());//��������
//				    private String storeName;//��������
					refundOrder.setApplyDate(compensate.getCreated());// ��������
					refundOrder.setBuyerId(platOrders.get(0).getBuyerId());//��һ�Ա�� �⸶�ӿ�û����һ�Ա�� ȡԭ�����е���һ�Ա��
					refundOrder.setDownTime(date);//��������ʱ��Ϊ��ǰʱ��
					
					refundOrder.setIsAudit(0);//���ô����
//					private String storeName;//��������
//					private String buyerId;//��������
					refundOrder.setExpressCode("");
					refundOrder.setIsRefund(0);
					refundOrder.setRefReason(compensate.getCompensateReason());
//				    private String refExplain;//�˿�˵��
					refundOrder.setRefStatus(0);
//					private String downTime;//����ʱ��
					refundOrder.setTreDate(compensate.getCreated());
					refundOrder.setOperator(misUser.getUserName());
//				    private String memo;//��ע
				    refundOrder.setAllRefNum(0);
				    refundOrder.setAllRefMoney(compensate.getCompensateAmount());
				    refundOrder.setOperatorId(misUser.getAccNum());
				    refundOrder.setOperatorTime(date);
				    refundOrder.setEcOrderId(compensate.getOrderId());//ƽ̨������
				    List<PlatOrders> platOrderslist = platOrdersDao.selectByEcOrderId(compensate.getOrderId());
				    if(platOrderslist.size()==0) {
				    	// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�ʧ�ܡ�������"+compensate.getOrderId()+"��Ʒ��ϸ��ʵ�����������⸶����");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(compensate.getOrderId());
						logRecordDao.insert(logRecord);
						//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
						compensateDao.delete(compensate);
						break;
				    }else if(platOrderslist.size()==1) {
						//��������ϸֻ��һ����¼ʱ������Ҫѭ��ռ�Ȳ���⸶���
						RefundOrders refundOrders=new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodSku(platOrderslist.get(0).getGoodSku());//��Ʒsku
						refundOrders.setEcGoodId(platOrderslist.get(0).getGoodId());//���õ�����Ʒ���룬��ʱΪ��
						refundOrders.setCanRefNum(platOrderslist.get(0).getCanRefNum());//���ÿ�������
						refundOrders.setRefNum(0);//�����˻�����
						//���ÿ��˽��
						refundOrders.setCanRefMoney(platOrderslist.get(0).getCanRefMoney());
						//�����˻����
						refundOrders.setRefMoney(compensate.getCompensateAmount());
						refundOrders.setGoodId(platOrderslist.get(0).getInvId());//���ô������
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrderslist.get(0).getInvId());
						refundOrders.setGoodName(invtyDoc.getInvtyNm());//���ô������
						refundOrders.setBatchNo(platOrderslist.get(0).getBatchNo());//�����˻�����
						refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//ȡ��Ӧ���̵�Ĭ���˻���
						if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));
						}
						refundOrders.setPrdcDt(platOrderslist.get(0).getPrdcDt());//������������
						refundOrders.setInvldtnDt(platOrderslist.get(0).getInvldtnDt());//����ʧЧ����
						refundOrderss.add(refundOrders);//װ��list
						
					}else {
						//����ʱ����Ҫѭ����ռ��
						DecimalFormat df1 = new DecimalFormat("0.00");
						BigDecimal total = new BigDecimal(0);
						for (int j = 0; j < platOrderslist.size(); j++) {
							if(platOrderslist.get(j).getPayMoney().compareTo(BigDecimal.ZERO)==0) {
								platOrderslist.remove(j);
								j--;
							}
						}
						for (int j = 0; j < platOrders.size(); j++) {
							//ѭ��ÿ���������
							total=total.add(platOrders.get(j).getPayMoney());//ȡÿ����ϸʵ��������
						}
						//�ж��⸶����Ƿ�������۵�������˰�ϼ�
						/*if(compensate.getCompensateAmount().compareTo(total)>0) {
							//���⸶������
						}*/
						BigDecimal hasCompensateAmount=new BigDecimal(0);//�������⸶���
						for (int j = 0; j < platOrderslist.size(); j++) {
							RefundOrders refundOrders=new RefundOrders();
							refundOrders.setRefId(refId);
							refundOrders.setIsGift(0);//���ò�����Ʒ
							refundOrders.setIsGift(platOrderslist.get(j).getIsGift());//�˿�Ƿ���Ʒ����ȡԭ������
							refundOrders.setGoodSku(platOrderslist.get(j).getGoodSku());//��Ʒsku
							refundOrders.setEcGoodId(platOrderslist.get(j).getGoodId());//���õ�����Ʒ���룬��ʱΪ��
							refundOrders.setCanRefNum(platOrderslist.get(j).getCanRefNum());//���ÿ�������
							refundOrders.setRefNum(0);//�����˻�����
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrderslist.get(j).getInvId());
							if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
								refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));
							}
							refundOrders.setPrdcDt(platOrderslist.get(j).getPrdcDt());//������������
							refundOrders.setInvldtnDt(platOrderslist.get(j).getInvldtnDt());//����ʧЧ����
							//���ÿ��˽��
							refundOrders.setCanRefMoney(platOrderslist.get(j).getCanRefMoney());
							//�����˻����
							if(i+1==platOrderslist.size()) {
								//���һ�� �ü���
								refundOrders.setRefMoney(compensate.getCompensateAmount().subtract(hasCompensateAmount));
							}else {
								BigDecimal bb= compensate.getCompensateAmount().divide(total,8,RoundingMode.HALF_DOWN);
								
								//BigDecimal bbBigDecimal = platOrderslist.get(j).getPayMoney().multiply(compensate.getCompensateAmount().divide(total,8));
								//����ÿ����ϸӦ�⸶������λС��
								BigDecimal dBigDecimal =new BigDecimal(df1.format(platOrderslist.get(j).getPayMoney().multiply(bb)));
								hasCompensateAmount=hasCompensateAmount.add(dBigDecimal);
								refundOrders.setRefMoney(dBigDecimal);
							}
							refundOrders.setGoodId(platOrderslist.get(j).getInvId());//���ô������
							refundOrders.setGoodName(invtyDoc.getInvtyNm());//���ô������
							refundOrders.setBatchNo(platOrderslist.get(j).getBatchNo());//�����˻�����
							refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//ȡ��Ӧ���̵�Ĭ���˻���
							refundOrderss.add(refundOrders);//װ��list
						}
					}
					
				    refundOrderList.add(refundOrder);
				}
				
			}
				if (refundOrderList.size()>0) {
					refundOrderDao.insertList(refundOrderList);
					refundOrdersDao.insertList(refundOrderss);
					//count = compensateDao.audit(okids, date, misUser.getAccNum(), misUser.getUserName());
					count = refundOrderList.size();
				}
		}
		//StoreRecord storeRecord = storeRecordDao.select(storeId);
		
			
		/*	message = "�ɹ����" + count + "���⸶����";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/cancelOrder/audit", isSuccess, message, null);*/
		}catch(Exception e){
			logger.error("URL:ec/cancelOrder/audit �쳣˵����",e);
			throw new RuntimeException();
		}
		return count;
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
			List<Compensate> compensateList = compensateDao.selectList(map);
			int count = compensateDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/compensate/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, compensateList);
		} catch (IOException e) {
			logger.error("URL:ec/compensate/queryList �쳣˵����",e);
		}
		return resp;
	}
	String errMessage="";
	@Override
	public int download(String storeId,String ecOrderId,String accNum,String startDate,String endDate) {
		int count = 0;
		int pageNo=1;
		int pageSize=30;
		errMessage="";
		compensateList = new ArrayList();
		try {
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			MisUser misUser = misUserDao.select(accNum);
			if ("JD".equals(storeRecord.getEcId())) {
				download(storeSettings, ecOrderId, pageNo, pageSize,startDate,endDate);
			}
			if (errMessage.length()==0) {
				//��errmessage�ĳ��ȵ���0ʱ��˵�����ع�����û�г��ֹ����⡣
				if (compensateList.size() == 0) {
				} else {
					compensateDao.insert(compensateList);
					count = audit(compensateList, accNum, storeId);
					
				} 
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�˿���أ����γɹ����ص��̣�"+storeRecord.getStoreName()+"�⸶��"+count+"��");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12�˿�����
				logRecord.setTypeName("�˿�����");
				logRecordDao.insert(logRecord);
			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�˿����ʧ�ܣ����̣�"+storeRecord.getStoreName()+",ԭ��˵����"+errMessage);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12�˿�����
				logRecord.setTypeName("�˿�����");
				logRecordDao.insert(logRecord);
			}
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/compensate/download �쳣˵����",e);
		}
		return count;
	}

	private void download(StoreSettings storeSettings,String ecorderid, int pageNo, int pageSize,String startDate,String endDate)
			throws NoSuchAlgorithmException, IOException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageIndex", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		//���������û��ƽ̨������ʱ�����յ�ǰʱ��-7���ѯ��Χ����
		//���������ƽ̨������ʱ��ֻ����ƽ̨�����Ų�ѯ
		if(ecorderid.length()!=0) {
			jdReq.put("refId",ecorderid);
		}else {
			if (startDate.length()==0||endDate.length()==0) {
				Date date = new Date();
				Calendar cale = Calendar.getInstance();
				cale.setTime(date);
				cale.add(Calendar.DAY_OF_YEAR, -7);
				jdReq.put("refType", 10);
				jdReq.put("modifiedStartTime", sdf.format(cale.getTime()));
				jdReq.put("modifiedEndTime", sdf.format(date));
			}else {
				jdReq.put("modifiedStartTime", startDate);
				jdReq.put("modifiedEndTime", endDate);
			}
		}
		
		String jdRespStr = ECHelper.getJD("jingdong.pop.afs.soa.compensate.queryCompensateList", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		if(jdRespJson.has("error_response")) {
			//�������error_response
			//˵����ѯ���ִ��󣬷��ش�����ʾ��Ϣ
			errMessage=jdRespJson.get("error_response").get("zh_desc").asText();
			compensateList.clear();
		}else {
			JsonNode resultJson = jdRespJson.get("jingdong_pop_afs_soa_compensate_queryCompensateList_responce");
			if (resultJson.get("code").asText().equals("0")) {
				 
				if (resultJson.get("queryResult").get("errorCode").asText().equals("10000")) {
					ArrayNode resultList = (ArrayNode) resultJson.get("queryResult").get("result");
					if (resultList != null) {
						for (Iterator<JsonNode> resultIterator = resultList.iterator(); resultIterator.hasNext();) {
							JsonNode result = resultIterator.next();
							Compensate compensate = new Compensate();
							if (compensateDao.selectById(result.get("compensate_id").asText())==null) {
								if(result.get("type").asInt()==30||result.get("type").asInt()==40) {
									//30  40  ���������⸶���ݣ������⸶�ĵ���check_status=1����Ч
									if (result.get("check_status").asInt()==1) {
										
										//�̼����״̬ 0������� 1�����ͨ�� 2����˲�ͨ��
										//���״̬Ϊ1ʱ�����浽ϵͳ
										compensate.setCompensateId(result.get("compensate_id").asText());//�⸶����
										compensate.setCompensateKeyid(result.get("compensate_keyid").asInt());//�⸶�ӵ���
										compensate.setVenderId(result.get("vender_id").asText());//�̼ұ��
										compensate.setType(result.get("type").asInt());//�⸶����
										compensate.setOrderId(result.get("order_id").asText());//������
										compensate.setOrderType(result.get("order_type").asInt());//��������
										compensate.setModified(sdf.format(new Date(new Long(result.get("modified").asText()))));//��������
										compensate.setCreated(sdf.format(new Date(new Long(result.get("created").asText()))));//��������
										compensate.setCompensateType(result.get("compensate_type").asInt());//�⸶�������
										compensate.setShouldPay(result.get("shouldpay").decimalValue());//Ӧ�⸶���
										compensate.setCompensateAmount(result.get("compensateamount").decimalValue());//�⸶���
										compensate.setCompensateReason(result.get("compensate_reason").asText());//�⸶ԭ��
										compensate.setCheckStatus(result.get("check_status").asInt());//�̼����״̬
										compensate.setErpCheckStatus(result.get("erp_check_status").asInt());//��Ӫ�ͷ����״̬
										compensate.setCanSecondAppeal(result.get("can_second_appeal").asInt());//�Ƿ���Զ�������
										compensate.setDownloadTime(sdf.format(new Date()));//��������ʱ��
										compensate.setIsAudit(0);//���ؿ����״̬
										compensate.setEcId("JD");//����ƽ̨���
										compensate.setStoreId(storeSettings.getStoreId());
										compensateList.add(compensate);
									}
								}else {
									
									//�̼����״̬ 0������� 1�����ͨ�� 2����˲�ͨ��
									//���״̬Ϊ1ʱ�����浽ϵͳ
									compensate.setCompensateId(result.get("compensate_id").asText());//�⸶����
									compensate.setCompensateKeyid(result.get("compensate_keyid").asInt());//�⸶�ӵ���
									compensate.setVenderId(result.get("vender_id").asText());//�̼ұ��
									compensate.setType(result.get("type").asInt());//�⸶����
									compensate.setOrderId(result.get("order_id").asText());//������
									compensate.setOrderType(result.get("order_type").asInt());//��������
									compensate.setModified(sdf.format(new Date(new Long(result.get("modified").asText()))));//��������
									compensate.setCreated(sdf.format(new Date(new Long(result.get("created").asText()))));//��������
									compensate.setCompensateType(result.get("compensate_type").asInt());//�⸶�������
									compensate.setShouldPay(result.get("shouldpay").decimalValue());//Ӧ�⸶���
									compensate.setCompensateAmount(result.get("compensateamount").decimalValue());//�⸶���
									compensate.setCompensateReason(result.get("compensate_reason").asText());//�⸶ԭ��
									compensate.setCheckStatus(result.get("check_status").asInt());//�̼����״̬
									compensate.setErpCheckStatus(result.get("erp_check_status").asInt());//��Ӫ�ͷ����״̬
									compensate.setCanSecondAppeal(result.get("can_second_appeal").asInt());//�Ƿ���Զ�������
									compensate.setDownloadTime(sdf.format(new Date()));//��������ʱ��
									compensate.setIsAudit(0);//���ؿ����״̬
									compensate.setEcId("JD");//����ƽ̨���
									compensate.setStoreId(storeSettings.getStoreId());
									compensateList.add(compensate);
								}
							}
						}
						download(storeSettings, ecorderid, pageNo+1, pageSize,startDate,endDate);
					} 
				}else {
					//�������errorCode������10000ʱ��û������
					//��ǰҳû�����ݣ�����ִ�еݹ�
				}
			}else {
				errMessage=resultJson.get("error_response").get("zh_desc").asText();
				compensateList.clear();
			}
		}
		
	}

}
