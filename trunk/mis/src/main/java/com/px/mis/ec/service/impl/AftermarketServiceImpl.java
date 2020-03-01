package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElementDecl.GLOBAL;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.AftermarketDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.Aftermarket;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.AftermarketService;
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
import com.px.mis.whs.entity.ProdStruSubTab;

@Transactional
@Service
public class AftermarketServiceImpl implements AftermarketService {

	private Logger logger = LoggerFactory.getLogger(AftermarketServiceImpl.class);
	
	@Autowired
	private AftermarketDao aftermarketDao;

	@Autowired
	private StoreRecordDao storeRecordDao;

	@Autowired
	private StoreSettingsDao storeSettingsDao;

	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private RefundOrderDao refundOrderDao;
	
	@Autowired
	private RefundOrdersDao refundOrdersDao;
	
	@Autowired
	private PlatOrderDao platOrderDao;

	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private GoodRecordDao goodRecordDao;
	
	@Autowired
	private SellSnglSubDao sellSnglSubDao;
	
	@Autowired
	private SellSnglDao sellSnglDao;
	
	@Autowired
	private LogRecordDao logRecordDao;
	private List<Aftermarket> aftermarketList;
	private String venderId;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String errMessage="";
	@Override
	public int audit(List<Aftermarket> aftermarkets,String accNum,String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int count = 0;
		int successcount=0;
		int fallcount=0;
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		try {
			MisUser misUser = misUserDao.select(accNum);
			String date=sdf.format(new Date());
			List<String> serviceIdListSuccess = new ArrayList<>();
			if (aftermarkets.size() > 0) {
				List<RefundOrder> refundOrderList = new ArrayList();
				List<RefundOrders> refundOrdersList = new ArrayList();
				for (Aftermarket aftermarket : aftermarkets) {
					//System.out.println(aftermarket.getStoreId());
					//System.out.println(aftermarket.getOrderId());
					/*if(aftermarket.getIsAudit()==1) {
						//�������ۺ�����ˣ������ظ����
						aftermarket.setAuditHint("�������ۺ�����ˣ������ظ����");
						aftermarketDao.update(aftermarket);
						fallcount++;
						continue;
					}*/
					if(aftermarket.getServiceStatusName().equals("ȡ��")||aftermarket.getServiceStatusName().equals("��˹ر�")
							||aftermarket.getServiceStatusName().equals("�����")) {
						//�������ۺ�״̬Ϊ��ȡ��/�����/��˹ر�ʱ��������������˿
						/*aftermarket.setAuditHint("�������ۺ���ȡ�������˻���˹رգ����������˿");
						aftermarketDao.update(aftermarket);*/
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}
					if(platOrderDao.checkExsits(aftermarket.getOrderId(), aftermarket.getStoreId())==0) {
						/*aftermarket.setAuditHint("ԭ�����ѱ�ɾ�����޷����");
						aftermarketDao.update(aftermarket);*/
						//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}
					/*if(platOrderDao.selectNoAuditCountByOrderIdAndStoreId(aftermarket.getStoreId(), aftermarket.getOrderId())!=0) {
						aftermarket.setAuditHint("ԭ����δ��ˣ��������ԭ����");
						aftermarketDao.update(aftermarket);
						
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������"+aftermarket.getOrderId()+"ԭ����δ��ˣ���������ʧ�ܡ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(aftermarket.getOrderId());
						logRecordDao.insert(logRecord);
						//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}*/
					/*if(!aftermarket.getProcessResultName().equals("�˻�")||!aftermarket.getProcessResultName().equals("����")) {
						//�������������˻����߻���ʱ�����������������˿
						aftermarket.setAuditHint("�����������˻����߻��£��������");
						aftermarketDao.update(aftermarket);
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������"+aftermarket.getOrderId()+"�����������˻����߻��£���������ʧ�ܡ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(aftermarket.getOrderId());
						logRecordDao.insert(logRecord);
						//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}*/
					
					Map<String,String> map=new HashMap<>();
					//System.out.println(aftermarket.getOrderId());
					map.put("ecOrderId", aftermarket.getOrderId());
					map.put("goodSku", aftermarket.getSkuId());
					//�������ۺ��еĶ����ź�skuidȥ��ѯԭ���Ĺ��������Լ�ʵ�����
					List<PlatOrders> platOrders = platOrderDao.selectNumAndPayMoneyByOrderAndSKu(map);
					if (platOrders.size()==0) {
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������"+aftermarket.getOrderId()+"δ�ҵ�������ϸ����������ʧ�ܡ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(aftermarket.getOrderId());
						logRecordDao.insert(logRecord);
						//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
						aftermarketDao.delete(aftermarket);
						continue;
					}else {
						boolean flag = true;
						for (int i = 0; i < platOrders.size(); i++) {
							//ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
							if(!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
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
							logRecord.setOperatContent("�˿���أ�������"+aftermarket.getOrderId()+"����δƥ�䵽��������ļ�¼����������ʧ�ܡ�");
							logRecord.setOperatTime(sdf.format(new Date()));
							logRecord.setOperatType(12);// 12�˿�����
							logRecord.setTypeName("�˿����");
							logRecord.setOperatOrder(aftermarket.getOrderId());
							logRecordDao.insert(logRecord);
							//ɾ�������ۺ󵥣������´�����ʱ����ȷ��������
							aftermarketDao.delete(aftermarket);
							continue;
						}
					}
					
					
					RefundOrder refundOrder = new RefundOrder();
					
					String refId = getOrderNo.getSeqNo("tk", accNum);
					refundOrder.setRefId(refId);// �˿���
					refundOrder.setStoreId(aftermarket.getStoreId());// ���̱��
					refundOrder.setEcRefId(String.valueOf(aftermarket.getServiceId()));//�����˿��
					refundOrder.setOrderId(aftermarket.getOrderId());//����ƽ̨������
					refundOrder.setEcOrderId(aftermarket.getOrderId());//����ƽ̨������
					refundOrder.setSource(1);//�����˿��ԴΪ���������ۺ�
					refundOrder.setSourceNo(""+aftermarket.getServiceId());//������Դ���ݺ�Ϊ�����ۺ�ķ��񵥺�
					refundOrder.setOperatorId(misUser.getAccNum());
					refundOrder.setOperatorTime(date);
					
					refundOrder.setStoreName(storeRecord.getStoreName());//��������
//				    private String storeName;//��������
					refundOrder.setApplyDate(aftermarket.getApplyTime());// ��������
					refundOrder.setBuyerId(aftermarket.getCustomerPin());//��һ�Ա��
					refundOrder.setDownTime(date);//��������ʱ��Ϊ��ǰʱ��
					refundOrder.setOperator(misUser.getUserName());
					refundOrder.setIsAudit(0);
					refundOrder.setTreDate(aftermarket.getApproveTime());//�˿����ʱ����ƽ̨�����ʱ��
					//refundOrder.setRefReason(aftermarket.get);
					//ֻ��������Ϊ�˻��ͻ����������ۺ�
					//ÿ����������Ϊ1��ָsku��������
					//�˻�������Ҫ�ô��������sku��������
					int allReturnCount=0;
					BigDecimal allReturnMoney=aftermarket.getActualPayPrice();//�ܹ���Ҫ�˿�Ľ��
					BigDecimal hasReturnMoney = BigDecimal.ZERO;//���˽��
					for (int i = 0; i < platOrders.size(); i++) {
						//ֱ��ȡ��ǰ����ϸ�����˿
						RefundOrders refundOrders = new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodSku(aftermarket.getSkuId());//��Ʒsku
						refundOrders.setEcGoodId(platOrders.get(i).getGoodId());//���õ�����Ʒ����
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
						refundOrders.setCanRefNum(platOrders.get(i).getCanRefNum());//���ÿ�������
						//refundOrders.setRefNum(platOrders.get(i).getCanRefNum());//�����˻�����
						refundOrders.setRefNum(platOrders.get(i).getInvNum()/platOrders.get(i).getGoodNum());//�����˻�����
						if(StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//���ñ�����
						}
						refundOrders.setIsGift(platOrders.get(i).getIsGift());//�Ƿ���Ʒ��ԭ��������
						refundOrders.setPrdcDt(platOrders.get(i).getPrdcDt());//������������
						refundOrders.setInvldtnDt(platOrders.get(i).getInvldtnDt());//����ʧЧ����
						//���ÿ��˽��processResult
						if (aftermarket.getProcessResult()==40) {//40���˻�
							refundOrders.setCanRefMoney(platOrders.get(i).getCanRefMoney());
							if (i+1<platOrders.size()) {
								refundOrders.setRefMoney(platOrders.get(i).getPayMoney().multiply(new BigDecimal(1).divide(new BigDecimal(platOrders.get(i).getGoodNum()))));
								hasReturnMoney = hasReturnMoney.add(refundOrders.getRefMoney());
							}else {
								refundOrders.setRefMoney(allReturnMoney.subtract(hasReturnMoney));
							}
						}else {//23����
							//����ʱ����Ҫ�˿�
							refundOrders.setCanRefMoney(platOrders.get(i).getCanRefMoney());
							refundOrders.setRefMoney(new BigDecimal(0));
						}
						allReturnCount+=refundOrders.getRefNum();
						//allReturnMoney=allReturnMoney.add(refundOrders.getRefMoney());
						refundOrders.setGoodId(platOrders.get(i).getInvId());//���ô������
						
						refundOrders.setGoodName(invtyDoc.getInvtyNm());//���ô������
						refundOrders.setBatchNo(platOrders.get(i).getBatchNo());//�����˻�����
						refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//ȡ��Ӧ���̵�Ĭ���˻���
						refundOrdersList.add(refundOrders);//װ��list
					}
					refundOrder.setRefStatus(0);//�˿�״̬��ʱ�����ó�0,��֪����ɶ��
					refundOrder.setAllRefNum(allReturnCount);
					refundOrder.setAllRefMoney(allReturnMoney);
					if(refundOrder.getAllRefNum()>0) {
						refundOrder.setIsRefund(1);//�˻���������0ʱ�Ƿ��˻�1
					}else {
						refundOrder.setIsRefund(0);
					}
					refundOrderList.add(refundOrder);
//				    private Integer isRefund;//�Ƿ��˻�
//				    private Integer allRefNum;//�����˻�����
//				    private BigDecimal allRefMoney;//�����˿���
//				    private String refReason;//�˿�ԭ��
//				    private String refExplain;//�˿�˵��
//				    private Integer refStatus;//�˿�״̬
//				    private String treDate;//��������
//				    private String operator;//����Ա
//				    private String memo;//��ע
					
				}
				if (refundOrderList.size()>0) {
					refundOrderDao.insertList(refundOrderList);
					refundOrdersDao.insertList(refundOrdersList);
					count = refundOrderList.size();
				}
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�˿���أ����γɹ����ؾ������̣�"+storeRecord.getStoreName()+",�����ۺ�"+refundOrderList.size()+"��");
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
				logRecord.setOperatContent("�˿���أ����γɹ����ؾ������̣�"+storeRecord.getStoreName()+",�����ۺ�0��");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12�˿�����
				logRecord.setTypeName("�˿�����");
				logRecordDao.insert(logRecord);
			}
			
			/*message = "�ɹ����" + count + "�������ۺ�,ʧ��"+fallcount+"����";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/aftermarket/audit", isSuccess, message, null);*/
		} catch (Exception e) {
			logger.error("URL:ec/aftermarket/audit �쳣˵����",e);
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
			List<Aftermarket> aftermarkets = aftermarketDao.selectList(map);
			int count = aftermarketDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/aftermarket/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, aftermarkets);
		} catch (IOException e) {
			logger.error("URL:ec/aftermarket/queryList �쳣˵����",e);
		}
		return resp;
	}

	@Override
	public int download(String storeId,String accNum,String orderId,String startDate,String endDate) {
		aftermarketList = new ArrayList();
		int count=0;
		try {
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			StoreSettings storeSettings = storeSettingsDao.select(storeRecord.getStoreId());
			MisUser misUser = misUserDao.select(accNum);
			if (storeRecord.getEcId().equals("JD")) {
				
				download(storeSettings, storeRecord, 23, 1, 50,orderId,startDate,endDate);//���ش�����Ϊ���µ�
				//download(storeSettings, storeRecord, 40, 1, 50,orderId);//���ش�����Ϊ�˻���
				if (errMessage.length()==0) {
					if (aftermarketList.size() > 0) {
						aftermarketDao.insert(aftermarketList);
						count=audit(aftermarketList, accNum,storeId);
					} 
					errMessage="";
				}else {
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�˿���أ����ִ��󣬴�����̣�"+storeRecord.getStoreName()+",����������"+errMessage);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(12);// 12�˿�����
					logRecord.setTypeName("�˿����");
					logRecordDao.insert(logRecord);
				}
			}
			
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/aftermarket/download �쳣˵����",e);
		}
		return count;
	}

	
	@Override
	public String downloadByStoreId(String jsonbody) {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		aftermarketList = new ArrayList();
		try {
			String storeId = BaseJson.getReqBody(jsonbody).get("storeId").asText();
			String orderId = BaseJson.getReqBody(jsonbody).get("orderId").asText();
			String startDate = BaseJson.getReqBody(jsonbody).get("startDate").asText();
			String endDate = BaseJson.getReqBody(jsonbody).get("endDate").asText();
			StoreRecord store = storeRecordDao.select(storeId);
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			if (store.getEcId().equals("JD")) {
					download(storeSettings,store,23, 1, 50,orderId,startDate,endDate);
					//download(storeSettings,store,40, 1, 50,orderId);
			
			if (errMessage.length()==0) {
				if (aftermarketList.size() == 0) {
					isSuccess = false;
					message = "������ɣ����γɹ�����" + aftermarketList.size() + "�������ۺ󵥣�";
				} else {
					aftermarketDao.insert(aftermarketList);
					isSuccess = true;
					message = "������ɣ����γɹ�����" + aftermarketList.size() + "�������ۺ󵥣�";
				} 
				errMessage="";
			}else {
				isSuccess=false;
				message=errMessage;
			}
			}else {
				isSuccess=false;
				message="��ѡ�񾩶�ƽ̨�ĵ�������";
			}
			resp = BaseJson.returnRespObj("ec/aftermarket/download", isSuccess, message, null);
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/aftermarket/download �쳣˵����",e);
		}
		return resp;
	}
	private void download(StoreSettings storeSettings,StoreRecord storeRecord,int processResult, int pageNo, int pageSize,String orderId,String startDate,String endDate)
			throws IOException, NoSuchAlgorithmException {
		
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageNumber", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		jdReq.put("buId", storeSettings.getVenderId());
		jdReq.put("operatePin", storeRecord.getSellerId());//�������˺�
		jdReq.put("operateNick", storeRecord.getSellerId());//����������
		if (startDate.length()==0||endDate.length()==0) {
			Date date = new Date();
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			cale.add(Calendar.DAY_OF_YEAR, -6);
			jdReq.put("applyTimeBegin", sdf.format(cale.getTime()));
			jdReq.put("applyTimeEnd", sdf.format(date));
		}else {
			
			jdReq.put("applyTimeBegin", startDate);
			jdReq.put("applyTimeEnd", endDate);
		}
		//jdReq.put("processResult",processResult);
		if(orderId.length()>0) {
			jdReq.put("orderId", orderId);
		}
		//System.out.println("�������ݣ�"+jdReq.toString());
		String jdRespStr = ECHelper.getJD("jingdong.asc.query.list", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		//System.out.println("�����"+jdRespJson);
		JsonNode resultJson = jdRespJson.get("jingdong_asc_query_list_responce");
		if (jdRespJson.has("error_response")) {
			errMessage=jdRespJson.get("error_response").get("zh_desc").asText();
		}else {

			if (resultJson.get("code").asText().equals("0")) {
				if (resultJson.get("pageResult").get("totalCount").asInt() != 0) {
					ArrayNode dataList = (ArrayNode) resultJson.get("pageResult").get("data");
					for (Iterator<JsonNode> dataIterator = dataList.iterator(); dataIterator.hasNext();) {
						JsonNode data = dataIterator.next();
						//System.err.println(data);
						if (data.has("processResult")) {
							if (data.get("processResult").asInt() == 40 || data.get("processResult").asInt() == 23) {
								//processResult==40ʱ��˽��Ϊ�˻�    23Ϊ����
								
								Aftermarket aftermarke1t = aftermarketDao.selectById(data.get("serviceId").asInt());
								if (aftermarketDao.selectById(data.get("serviceId").asInt()) == null) {//����˷��񵥺Ų�����ʱ�������ر���
									Aftermarket aftermarket = new Aftermarket();
									aftermarket.setApplyId(data.get("applyId").asInt());// ���뵥��
									if (data.get("serviceId") != null) {
										aftermarket.setServiceId(data.get("serviceId").asInt());// ���񵥺�
									}
									//System.out.println(sdf.format(new Date(new Long(data.get("applyTime").asText()))));
									if (data.get("applyTime") != null) {
										aftermarket.setApplyTime(
												sdf.format(new Date(new Long(data.get("applyTime").asText()))));// ����ʱ��
									}
									if (data.get("customerExpect") != null) {
										aftermarket.setCustomerExpect(data.get("customerExpect").asInt());// �ͻ�����
									}
									if (data.get("customerExpectName") != null) {
										aftermarket.setCustomerExpectName(data.get("customerExpectName").asText());// �ͻ���������
									}
									if (data.get("serviceStatus") != null) {
										aftermarket.setServiceStatus(data.get("serviceStatus").asInt());// ����״̬
									}
									if (data.get("serviceStatusName") != null) {
										aftermarket.setServiceStatusName(data.get("serviceStatusName").asText());// ����״̬����
									}
									if (data.get("customerPin") != null) {
										aftermarket.setCustomerPin(data.get("customerPin").asText());// �ͻ��˺�
									}
									if (data.get("customerName") != null) {
										aftermarket.setCustomerName(data.get("customerName").asText());// �ͻ�����
									}
									if (data.get("customerGrade") != null) {
										aftermarket.setCustomerGrade(data.get("customerGrade").asInt());// �û�����
									}
									if (data.get("customerTel") != null) {
										aftermarket.setCustomerTel(data.get("customerTel").asText());// �û��绰
									}
									if (data.get("pickwareAddress") != null) {
										aftermarket.setPickwareAddress(data.get("pickwareAddress").asText());// ȡ����ַ
									}
									if (data.get("orderId") != null) {
										aftermarket.setOrderId(data.get("orderId").asText());// ������
									}
									if (data.get("orderType") != null) {
										aftermarket.setOrderType(data.get("orderType").asInt());// ��������
									}
									if (data.get("orderTypeName") != null) {
										aftermarket.setOrderTypeName(data.get("orderTypeName").asText());// ������������
									}
									if (data.get("actualPayPrice") != null) {
										aftermarket.setActualPayPrice(
												new BigDecimal(data.get("actualPayPrice").asDouble()));// ʵ�����
									}
									if (data.get("skuId") != null) {
										aftermarket.setSkuId(data.get("skuId").asText());// ��Ʒ���
									}
									if (data.get("wareType") != null) {
										aftermarket.setWareType(data.get("wareType").asInt());// ��Ʒ����
									}
									if (data.get("wareTypeName") != null) {
										aftermarket.setWareTypeName(data.get("wareTypeName").asText());// ��Ʒ��������
									}
									if (data.get("wareName") != null) {
										aftermarket.setWareName(data.get("wareName").asText());// ��Ʒ����
									}
									if (data.get("approvePin") != null) {
										aftermarket.setApprovePin(data.get("approvePin").asText());// ������˺�
									}
									if (data.get("approveName") != null) {
										aftermarket.setApproveName(data.get("approveName").asText());// ���������
									}
									if (data.get("approveTime") != null) {
										aftermarket.setApproveTime(
												sdf.format(new Date(new Long(data.get("approveTime").asText()))));// ���ʱ��
									}
									if (data.get("approveResult") != null) {
										aftermarket.setApproveResult(data.get("approveResult").asInt());// ��˽��
									}
									if (data.get("approveResultName") != null) {
										aftermarket.setApproveResultName(data.get("approveResultName").asText());// ��˽������
									}
									if (data.get("processPin") != null) {
										aftermarket.setProcessPin(data.get("processPin").asText());// �������˺�
									}
									if (data.get("processName") != null) {
										aftermarket.setProcessName(data.get("processName").asText());// ����������
									}
									if (data.get("processTime") != null) {
										aftermarket.setProcessTime(
												sdf.format(new Date(new Long(data.get("processTime").asText()))));// ����ʱ��
									}
									if (data.get("processResult") != null) {
										aftermarket.setProcessResult(data.get("processResult").asInt());// ������
									}
									if (data.get("processResultName") != null) {
										aftermarket.setProcessResultName(data.get("processResultName").asText());// ����������
									}
									aftermarket.setIsAudit(0);
									aftermarket.setEcId("JD");
									aftermarket.setStoreId(storeSettings.getStoreId());
									aftermarketList.add(aftermarket);

								}
							} 
						}
					}
					int totalCount = resultJson.get("pageResult").get("totalCount").asInt();
					if (pageSize * pageNo < totalCount) {
						download(storeSettings, storeRecord, processResult, pageNo + 1, pageSize,orderId,startDate,endDate);
					}
				}
			} 
		
		}
	}

	private String getVenderid(StoreSettings storeSettings) throws NoSuchAlgorithmException, IOException {
		String jdRespStr = ECHelper.getJD("jingdong.seller.vender.info.get", storeSettings, "{}");
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		JsonNode resultJson = jdRespJson.get("jingdong_seller_vender_info_get_responce");
		if (resultJson.get("code").asText().equals("0")) {
			return resultJson.get("vender_info_result").get("vender_id").asText();
		} else {
			return "flase";
		}
	}

}
