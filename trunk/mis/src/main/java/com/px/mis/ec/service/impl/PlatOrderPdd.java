package com.px.mis.ec.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Ref;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddOrderInformationGetRequest;
import com.pdd.pop.sdk.http.api.request.PddOrderListGetRequest;
import com.pdd.pop.sdk.http.api.request.PddRefundListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.response.PddOrderInformationGetResponse;
import com.pdd.pop.sdk.http.api.response.PddOrderInformationGetResponse.OrderInfoGetResponseOrderInfo;
import com.pdd.pop.sdk.http.api.response.PddOrderInformationGetResponse.OrderInfoGetResponseOrderInfoItemListItem;
import com.pdd.pop.sdk.http.api.response.PddOrderListGetResponse;
import com.pdd.pop.sdk.http.api.response.PddOrderListGetResponse.OrderListGetResponseOrderListItem;
import com.pdd.pop.sdk.http.api.response.PddRefundListIncrementGetResponse.RefundIncrementGetResponseRefundListItem;
import com.pdd.pop.sdk.http.api.response.PddRefundListIncrementGetResponse;
import com.px.mis.ec.dao.CouponDetailDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrderPaymethodDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrderPaymethod;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.AssociatedSearchService;
import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.ec.service.PlatOrderService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
@Component
public class PlatOrderPdd {
	
	@Autowired
	private StoreSettingsDao storeSettingsDao; //��������
	@Autowired
	private PlatOrderDao platOrderDao; //��������
	@Autowired
	private StoreRecordDao storeRecordDao; //���̵���
	@Autowired
	private PlatOrdersDao platOrdersDao; //�����ӱ�
	@Autowired
	private GetOrderNo getOrderNo; //���ɶ�����
	@Autowired
	private GoodRecordDao goodRecordDao; //��Ʒ����
	@Autowired
	private InvtyDocDao invtyDocDao; //�������
	@Autowired
	private ProdStruMapper prodStruDao; // ��Ʒ�ṹ
	@Autowired
	private CouponDetailDao couponDetailDao; //�Ż���ϸ
	@Autowired
	private PlatOrderPaymethodDao paymethodDao; //֧����ϸ
	@Autowired
	private RefundOrderDao refundOrderDao; //�˿����
	@Autowired
	private RefundOrdersDao refundOrdersDao; //�˿�ӱ�
	@Autowired
	private AuditStrategyService auditStrategyService;
	@Autowired
	private AssociatedSearchService associatedSearchService;
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private MisUserDao misUserDao;
	@Autowired
	private PlatOrderService platOrderService;
	private Logger logger = LoggerFactory.getLogger(PlatOrderPdd.class);
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * ƴ���-��������
	 * @throws Exception 
	 */
	@Transactional
	public String pddDownload(String userId, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception{
		    String message ="";
			String resp = "";
			boolean isSucess = true;
			int downloadCount = 0;
			// ����
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			// ��Ϣ
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			
			
			Date date = sf.parse(startDate);
			long start = date.getTime();
			date = sf.parse(endDate);
			long end = date.getTime();
			long cha = end-start;
			
			List<OrderListGetResponseOrderListItem> order_list = new ArrayList<>();  
			 
		  // �ܹ�������
		   double result = cha * 1.0 / (1000 * 60 * 60 * 24);
		   for (int i = 0; i < result ; i++) {
			   if(i+1>=result) {
				   order_list.addAll(pddDownLoadOrder(storeSettings,storeRecord,pageNo,pageSize,start+(1000 * 60 * 60 * 24 * i),end,order_list));
			   }else {
				   order_list.addAll(pddDownLoadOrder(storeSettings,storeRecord,pageNo,pageSize,start+(1000 * 60 * 60 * 24 * i),start+(1000 * 60 * 60 * 24 * (i+1)),order_list));
			   }
		    
		   }
			
			//List<OrderListGetResponseOrderListItem> order_list = new ArrayList<>();		
			//order_list = pddDownLoadOrder(storeSettings,storeRecord,pageNo,pageSize,start,end,order_list);
			
			for (int i = 0; i < order_list.size(); i++) {
				OrderListGetResponseOrderListItem order = order_list.get(i);
				
				//��ѯ��Ӧ�����Ƿ���ڴ˶���
				if (platOrderDao.checkExsits(order.getOrderSn(), storeId) == 0) {
					//����������
					Map<String,Object> map = pddDownloadByOrderCode(order.getOrderSn(), userId, storeId,downloadCount);
					isSucess = (Boolean)map.get("isSuccess");
					message = (String)map.get("message");
					downloadCount = (int)map.get("downloadCount");
					
				} else {
					message = "�����Ѵ���,������:"+order.getOrderSn()+",���̱��:"+storeId;
					isSucess = false;
					logger.error("��������:ƴ���ƽ̨�����Ѵ���,������:{},���̱��:{}",order.getOrderSn(),storeId);	
				}

			}	
			if(isSucess) {
				message = "�����Զ����ص��̣� "+ storeSettings.getStoreName() + "����" + downloadCount + "��";
			}
			resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

		return resp;
	}
	
	String message = "";
	
	/**
	 * post-pdd���ض���
	 * @throws Exception 
	 */
	public List<OrderListGetResponseOrderListItem> pddDownLoadOrder(StoreSettings storeSettings,StoreRecord storeRecord,
			int pageNo,int pageSize,long start,long end,List<OrderListGetResponseOrderListItem> order_list) throws Exception {
		
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddOrderListGetRequest request = new PddOrderListGetRequest();
		request.setOrderStatus(5);// ����״̬��1����������2���ѷ�����ǩ�գ�3����ǩ�� 5��ȫ��
		request.setRefundStatus(5);// �ۺ�״̬ 1�����ۺ���ۺ�رգ�2���ۺ����У�3���˿��У�4�� �˿�ɹ� 5��ȫ��

		request.setStartConfirmAt(start / 1000);// ����ɽ�ʱ�俪ʼʱ���ʱ�����ָ��������ʱ�� 1970 �� 01 �� 01 �� 00 ʱ 00 �� 00 ���������ڵ�������
		request.setEndConfirmAt(end / 1000);
		request.setPage(pageNo);
		request.setPageSize(pageSize);
		
		request.setTradeType(0);// �������� 0-��ͨ���� ��1- ���𶩵�
		 	
		//ƴ����ѯ����
		PddOrderListGetResponse response = client.syncInvoke(request, accessToken);
		//�쳣����
		if (response.getErrorResponse() != null) {
			
			message = "��������:ƴ���ƽ̨���ض����쳣,�쳣��Ϣ:"+	response.getErrorResponse().getErrorMsg();
			logger.error("��������:ƴ���ƽ̨���ض����쳣,�쳣��Ϣ:{}",response.getErrorResponse().getErrorMsg());		
		}
		//��������
		if(response.getOrderListGetResponse() != null) {
			 order_list.addAll(0, response.getOrderListGetResponse().getOrderList());
		}
		
		if (response.getOrderListGetResponse() != null && response.getOrderListGetResponse().getTotalCount() > pageNo * pageSize) {
			
			pddDownLoadOrder(storeSettings,storeRecord,pageNo + 1, pageSize, start, end, order_list);
		}
		
		return order_list;
	}
	/**
	 * ƴ���-�˻�
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public String pddRefund(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception {
		int successCount=0;
		boolean isSuccess = true;
		String resp = "";
		List<RefundOrder> reList = new ArrayList<>();
		List<RefundIncrementGetResponseRefundListItem> refundList = new ArrayList<>();
		
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		// ��Ϣ
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		
		List<String> list = ECHelper.getIntervalTimeList(startDate,endDate,30);
		
		if(list.size()>1) {
			for (int i = 0; i < list.size(); i++) {
				refundList = postPddRefundOrder(storeSettings, storeRecord, pageNo, pageSize, list.get(i), list.get(i+1),
						refundList);
				if(i+2==list.size()) {
					break;
				}
			}
		}
		
		refundList = postPddRefundOrder(storeSettings,storeRecord,pageNo,pageSize,startDate,endDate,refundList);
		//System.out.println(refundList.size());
        if(refundList.size() > 0) {
        	
        	
    		//RefundOrder �˿
    		for(RefundIncrementGetResponseRefundListItem refund : refundList) {
    			
    			String orderCode = refund.getOrderSn();//ƽ̨������		
    			String ecRefId = String.valueOf(refund.getId()); //�����˿��
    			
    			//��ѯƽ̨����
    			List<PlatOrder> orders = platOrderDao.selectPlatOrdersByEcOrderId(orderCode);
				
				if (orders.size()>0) {
					
					RefundOrder refundOrder = refundOrderDao.selectEcRefId(ecRefId);
					if(refundOrder == null) {
						
						refundOrder = pddGetRundOrder(refund,storeRecord,isSuccess,accNum);		
						if (refundOrder.getRefundOrders().size()>0) {
							//���ص�list����0����
							refundOrderDao.insert(refundOrder);
							refundOrdersDao.insertList(refundOrder.getRefundOrders());
							successCount++;
						}
    					
					}
				}	
        	}
    		message="���γɹ�����ƴ�����̣�"+storeRecord.getStoreName()+"�˿�"+successCount+"��";
        }  else {
    		message = "���γɹ�����ƴ�����̣�"+storeRecord.getStoreName()+"�˿�0��";
    		isSuccess = false; 		
    	}
        
        // ��־��¼
        MisUser misUser = misUserDao.select(accNum);
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(accNum);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent(message);
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(12);// 12�˿�����
		logRecord.setTypeName("�˿����");
		logRecord.setOperatOrder(null);
		logRecordDao.insert(logRecord);
        
        if(isSuccess) {
        	logger.info("�����˿�ɹ�,���������˿����:{}��",refundList.size());
        }
        resp = BaseJson.returnRespObj("ec/refundOrder/download", isSuccess, message, null);		
		return resp;
	}
	/**
	 * post-ƴ������������˿
	 */
	private List<RefundIncrementGetResponseRefundListItem> postPddRefundOrder(StoreSettings storeSettings,StoreRecord storeRecord,
			int pageNo,int pageSize, String startDate, String endDate, List<RefundIncrementGetResponseRefundListItem> refundList) throws Exception {
		
		Date date = sf.parse(startDate);
		long start = date.getTime();
		date = sf.parse(endDate);
		long end = date.getTime();
		
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		
		PopClient client = new PopHttpClient(clientId, clientSecret);

        PddRefundListIncrementGetRequest request = new PddRefundListIncrementGetRequest();
        request.setAfterSalesStatus(1);
        request.setAfterSalesType(1);
        request.setEndUpdatedAt(end/1000);
        request.setPage(pageNo);
        request.setPageSize(pageSize);
        request.setStartUpdatedAt(start/1000);
        PddRefundListIncrementGetResponse response = client.syncInvoke(request, accessToken);
        
        if (response.getErrorResponse() != null) {
			
			logger.error("�����˿:ƴ���ƽ̨��ѯ�˿�쳣,�쳣��Ϣ:{}",response.getErrorResponse().getErrorMsg());
			message = "�����˿:ƴ���ƽ̨��ѯ�˿��쳣,�쳣��Ϣ:"+response.getErrorResponse().getErrorMsg();
				
		}
        if(response.getRefundIncrementGetResponse() != null) {
        	//�ۺ��б����
        	refundList.addAll(0,response.getRefundIncrementGetResponse().getRefundList()); 
        	
        }
        if(response.getRefundIncrementGetResponse() != null && response.getRefundIncrementGetResponse().getTotalCount() != null) {
        	response.getRefundIncrementGetResponse().getTotalCount();
        	if(Integer.valueOf(response.getRefundIncrementGetResponse().getTotalCount()) > pageNo * pageSize) {
        		postPddRefundOrder(storeSettings,storeRecord,pageNo+1,pageSize,startDate,endDate,refundList);
			}
        }
		return refundList;
	}

	/**
	 * ƴ���-��ȡ�˻������ݼ���
	 */
	public RefundOrder pddGetRundOrder(RefundIncrementGetResponseRefundListItem refund,StoreRecord storeRecord,Boolean isSuccess,String accNum) throws IllegalAccessException, InvocationTargetException {
		
			
			//������
			RefundOrder order = new RefundOrder();
			String refId = getOrderNo.getSeqNo("tk", accNum);
			order.setRefId(refId);
			//Integer.valueOf(refund.getSpeedRefundStatus()); //�����˿�״̬��"1"���м����˿��ʸ�"2"�������˿�ʧ��, "3" ��ʾ�����˿�ɹ���������ʾ�Ǽ����˿�
			order.setRefReason(refund.getAfterSaleReason()); //�ۺ�ԭ��
			order.setRefStatus(refund.getAfterSalesStatus());//�ۺ�״̬
			refund.getAfterSalesType();//�ۺ�����
			//refund.getConfirmTime(); //����ʱ��
			order.setApplyDate(refund.getCreatedTime()); //����ʱ�� -��������

			refund.getDiscountAmount(); //�����ۿ۽��
			order.setEcOrderId(refund.getOrderSn());//������� -����
			//refund.getGoodImage(); //��ƷͼƬ
			order.setEcGoodId(String.valueOf(refund.getGoodsId()));//��Ʒ���� -ƽ̨
			order.setEcId("PDD");
			order.setExpressCode(refund.getTrackingNumber()); // ��ݵ���
			order.setEcRefId(String.valueOf(refund.getId())); //�ۺ���
			//order.setEcGoodId(refund.getOrderSn()); //������� -����
			order.setAllRefMoney(new BigDecimal(refund.getRefundAmount())); //�˿���
			order.setAllRefNum(Integer.valueOf(refund.getGoodsNumber()));  //��Ʒ���� -�˻�����
			
			order.setStoreId(storeRecord.getStoreId()); //���̱��
			order.setStoreName(storeRecord.getStoreName());//��������
			
			if(Integer.valueOf(refund.getGoodsNumber()) == 0) {
				order.setIsRefund(0);//��
			} else {
				order.setIsRefund(1);//��
			}
			order.setDownTime(sf.format(new Date())); //����ʱ��
			order.setIsAudit(0); //�Ƿ����
			
			
			//refund.getSpeedRefundStatus();// �����˿��־λ 1�������˿0���Ǽ����˿�
			if(StringUtils.isNotEmpty(refund.getSpeedRefundStatus())) {
				
				if(refund.getSpeedRefundStatus().contains("1")) {
					order.setMemo("�����˿�"); 
				}
			}
			
			order.setSource(0); //�˿��Դ 0���ӿ�����
			
			List<RefundOrders> ordersList = new ArrayList<>();
			
			RefundOrders orders = new RefundOrders();

			orders.setRefId(refId);
			orders.setEcGoodId(String.valueOf(refund.getGoodsId())); //ƴ�����Ʒ����
			orders.setGoodId(refund.getOuterId()); 
			orders.setGoodName(refund.getGoodsName());
			orders.setGoodSku(refund.getSkuId());
			orders.setRefNum(Integer.valueOf(refund.getGoodsNumber()));
			orders.setRefMoney(new BigDecimal(refund.getRefundAmount())); //ʵ���˿���
			//System.out.println("����ʱ�䣺"+refund.getUpdatedTime());
			order.setTreDate(refund.getUpdatedTime());//����ʱ���ø���ʱ��
			ordersList.add(orders);
			order.setRefundOrders(ordersList);
			order = platOrderSN.checkRefundIsPto(order,accNum);
			//order = getRefundOrders(platOrder,order,isSuccess,refund);
			
			return order;	
			
	}
	/**
	 * ƴ���-��ȡ�˻����ӱ���Ϣ
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public RefundOrder getRefundOrders(PlatOrder platOrder,
										RefundOrder order,
										Boolean isSuccess,
										RefundIncrementGetResponseRefundListItem refund) throws IllegalAccessException, InvocationTargetException {
		
			
		for(PlatOrders platOrders : platOrder.getPlatOrdersList()) {
			
			String ecgoodid = String.valueOf(refund.getGoodsId());// ƽ̨��Ʒ���
			String goodsku = platOrders.getGoodSku();
			GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
			if(goodRecord == null) {
				message = "�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����";
				isSuccess = false;
				return order;
			}
			if(platOrders.getGoodId().equals(goodRecord.getGoodId())) {
				if(new BigDecimal(refund.getRefundAmount()).compareTo(platOrder.getPayMoney()) == 1 ) {
					// �˿������֧�����
					message = "�����˿������֧�����,ƽ̨������:"+order.getEcGoodId();
					isSuccess = false;
				} else {
					order.setAllRefMoney(platOrders.getPayMoney());
					order.setAllRefNum(platOrders.getGoodNum());
					
					RefundOrders orders = new RefundOrders();
					orders.setRefId(order.getRefId());
					orders.setGoodName(refund.getGoodsName()); //��Ʒ����
					orders.setRefNum(Integer.valueOf(refund.getGoodsNumber())); //��Ʒ����
					//refund.getGoodsPrice(); //��Ʒ����		
					//refund.getOrderAmount(); //�������
					orders.setGoodId(refund.getOuterGoodsId());//�̼��ⲿ��Ʒ����
					orders.setGoodSku(refund.getOuterId()); //�̼��ⲿ����(sku)	
					orders.setCanRefMoney(platOrders.getPayMoney());
					List<RefundOrders> ordersList = new ArrayList<>();
					ordersList.add(orders);
					order.setRefundOrders(ordersList);
					order = checkIsPto(order,orders,isSuccess);			
					refund.getSkuId(); //��Ʒ���id
					refund.getTrackingNumber();//����˵���
					refund.getUpdatedTime();// ����ʱ��
					
				}
			}else {
				//û��ƥ�䵽��Ʒ
				message = "����δƥ�䵽��Ʒ��ϸ,ƽ̨������:"+order.getEcGoodId();
				isSuccess = false;
			}	
			
		}
		
		return order;
		
	}
	
	/**
	 * ��Ʒ-��װ���
	 */
	public RefundOrder checkIsPto(RefundOrder order,RefundOrders orders,Boolean flag) throws IllegalAccessException, InvocationTargetException {
		
		String ecgoodid = orders.getEcGoodId();// ƽ̨��Ʒ���
		String goodsku = orders.getGoodSku();
		List<RefundOrders> ordersList = order.getRefundOrders();
		
		GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
		if (goodRecord == null) {
			order.setAuditHint("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");	
			flag = false;
		
		} else if (goodRecord.getGoodId() == null) {
			order.setAuditHint("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");	
			flag = false;
				
		} else {
			String invtyId = goodRecord.getGoodId(); //��Ʒ���goodId
			
			//������Ʒ��Ų�ѯ�������(invty_doc),�жϲ�Ʒ�ṹpto
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyId);
			if(invtyDoc == null) {
				message = "δ��ȡ����Ʒ��ţ�["+invtyId+"]��Ӧ�������";
				flag = false;
			} else {			
				if (invtyDoc.getPto() != null) {
					if (invtyDoc.getPto() == 1) {
						// ����PTO���ʹ��
						ProdStru prodStru = prodStruDao.selectMomEncd(invtyId); //��ѯĸ����Ʒ
						
						if (prodStru == null) {
							
							order.setAuditHint("�ڲ�Ʒ�ṹ��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");								
							flag = false;
							
						} else {
							if (prodStru.getIsNtChk() == 0) {
								// ��Ӧ��Ʒ�ṹ��δ���
								order.setAuditHint("��Ӧ��Ʒ�ṹ��δ���");
								flag = false;
								
							} else {
								List<ProdStruSubTab> strucksublist = prodStru.getStruSubList();
								// ѭ����Ʒ�ṹ�ӱ���Ϣ
								if (strucksublist.size() == 0) {
									order.setAuditHint("��Ӧ��Ʒ�ṹû�������Ӽ���ϸ���������á�");
									flag = false;
									
								} else if (strucksublist.size() == 1) {
									
									// ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
									// ���˿�
									if(order.getAllRefNum() == 0) {
										// �����˻�����
										orders.setRefNum(0);
										// ���ÿ�������
										orders.setCanRefNum(0);
									} else {
										// �����˻�����
										orders.setRefNum(new BigDecimal(orders.getRefNum())
												.multiply(new BigDecimal(strucksublist.get(0).getSubNm())).intValue());
									}
									// ���ÿ�������
									orders.setCanRefNum(orders.getRefNum());
									orders.setGoodId(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
									orders.setGoodName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
									
									// ���ÿ��˽��,��ʵ�����һ��
									//orders.setRefMoney(orders.getRefMoney());
									ordersList.add(orders);
									flag = true;
									
								} else {
									// ����Ʒ�ṹ���Ӽ�����������1ʱ����Ҫ���ɶ�����ϸ

									// �����Ӽ��ο��ɱ����Ӽ��������ܳɱ�
									// ������꣬���false˵����Ӧ�Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���
									BigDecimal total = new BigDecimal("0");
									for (int j = 0; j < strucksublist.size(); j++) {
										InvtyDoc invtyDoc1 = invtyDocDao
												.selectInvtyDocByInvtyDocEncd(strucksublist.get(j).getSubEncd());
										if (invtyDoc1.getRefCost() == null) {
											flag = false;
											break;
										} else {
											total = total.add(invtyDoc1.getRefCost()
													.multiply(strucksublist.get(j).getSubQty())); //Ӧ��
										}
									}
									
									DecimalFormat dFormat = new DecimalFormat("0.00");
									
									BigDecimal temp = new BigDecimal(dFormat.format(total));
									
									for (int j = 0; j < strucksublist.size(); j++) {
										
										RefundOrders orders1 = new RefundOrders();
										
										BeanUtils.copyProperties(orders1, orders);
										
										// ����ÿ���Ӽ�ռ�ܳɱ��ı��� ����8λС��
										InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
												strucksublist.get(j).getSubEncd());
										BigDecimal rate = invtyDoc2.getRefCost()
												.multiply(strucksublist.get(j).getSubQty())
												.divide(total, 8, BigDecimal.ROUND_HALF_UP);
										orders1.setRefNum((new BigDecimal(orders.getRefNum()))
												.multiply(strucksublist.get(j).getSubQty()).intValue());
										
										// �˻���� ������λС��
										if(j == strucksublist.size() - 1) {
											
											orders1.setRefMoney(temp);
											
											
										} else {
											
											orders1.setRefMoney(order.getAllRefMoney().multiply(rate).divide(total, 2, BigDecimal.ROUND_HALF_UP));
											//TODO ���˻����
											//TODO ��������
											//orders1.setCanRefMoney(orders1.getRefMoney()); //���˻����
											
											temp = temp.subtract(orders1.getRefMoney());
												
										}
										if(order.getAllRefNum() == 0) {
											// �����˻�����
											orders1.setRefNum(0);
											// ���ÿ�������
											orders1.setCanRefNum(0);
										} else {
											// �����˻�����
											orders1.setRefNum(new BigDecimal(orders.getRefNum())
													.multiply(new BigDecimal(strucksublist.get(0).getSubNm())).intValue());
										}
										orders1.setCanRefNum(orders.getRefNum());
										
										orders1.setRefMoney(new BigDecimal(dFormat.format(orders.getRefMoney().multiply(rate))));
																												
										orders1.setGoodId(invtyDoc2.getInvtyEncd());// ���ö�Ӧĸ������
										orders1.setGoodName(invtyDoc2.getInvtyNm());// ���ö�Ӧĸ������	
										
										ordersList.add(orders1);
										
										flag = true;
										
									}
								}

							}
						}
					} else {
						//������Ʒ
						ordersList.add(orders);
					}
						
				} else {
					// �������pto����Ϊ��
					order.setAuditHint("���������PTO����Ϊ��");			
					flag = false;
				}
			}
		}
		if(!flag) {
			ordersList.add(orders);
		}
		order.setRefundOrders(ordersList);
		
		return order;
	
	}
	/**
	 * ƴ���-���ݶ��������ض���
	 */
	@Transactional
	public Map<String,Object> pddDownloadByOrderCode(String orderCode, String userId, String storeId,int downloadCount) throws Exception {
		
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		// ��Ϣ
		StoreRecord storeRecord = storeRecordDao.select(storeId);

		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddOrderInformationGetRequest request = new PddOrderInformationGetRequest();
		request.setOrderSn(orderCode);
		PddOrderInformationGetResponse response = client.syncInvoke(request, accessToken);
		
		//JSONObject object = JSONObject.parseObject(JsonUtil.transferToJson(response));
		if (response.getErrorResponse() != null) {
					
			logger.error("��������:ƴ���ƽ̨��ѯ�����쳣,�쳣��Ϣ:{}",response.getErrorResponse().getErrorMsg());
			message = "��������:ƴ���ƽ̨��ѯ�����쳣,�쳣��Ϣ:"+response.getErrorResponse().getErrorMsg();
			isSuccess = false;	
		}
		if(response.getOrderInfoGetResponse() != null) {
			OrderInfoGetResponseOrderInfo order = response.getOrderInfoGetResponse().getOrderInfo();
			
			if(order != null) {
				PlatOrder platOrder = new PlatOrder();
				String orderId = getOrderNo.getSeqNo("ec", userId); //����Ʒ���ڲ�������
				//�ֿ���Ϣ
				platOrder.setPayTime(order.getPayTime()); //֧��ʱ��
				platOrder.setIsInvoice(order.getInvoiceStatus()); // �Ƿ�Ʊ 1������ 0������
				
				//TODO ��ȡ��Ʊ��Ϣapi pdd.invoice.application.query
				platOrder.setBuyerNote(order.getBuyerMemo());// �������
				platOrder.setSellerNote(order.getRemark());// ��ע ����
				
				platOrder.setProvince(order.getProvince()); // ʡ
				platOrder.setProvinceId(String.valueOf(order.getProvinceId())); // ʡid
				platOrder.setCity(order.getCity()); // �� 
				platOrder.setCityId(String.valueOf(order.getCityId())); //
				platOrder.setCounty(order.getTown()); // ��
				platOrder.setCountyId(String.valueOf(order.getTownId())); //
				platOrder.setRecAddress(order.getAddress()); //�ջ�����ϸ��ַ
				platOrder.setRecName(order.getReceiverName()); // �ջ�������
				platOrder.setRecMobile(order.getReceiverPhone()); // �ջ����ֻ���
				
				//ȷ���ջ�ʱ��	
					
				platOrder.setEcOrderId(order.getOrderSn());// ���̶�����
				platOrder.setOrderId(orderId); // �������
				platOrder.setStoreId(storeId); // ���̱�� 
				platOrder.setStoreName(storeRecord.getStoreName()); // ��������
				
				platOrder.setTradeDt(order.getCreatedTime()); //����ʱ�� ����ʱ��
				
				platOrder.setIsAudit(0); // �Ƿ����

				platOrder.setGoodMoney(new BigDecimal(order.getGoodsAmount()));  // ��Ʒ���=��Ʒ���ۼ۸�*��Ʒ����-�ļ۽��(�ӿ����޸��ֶ�)


				platOrder.setIsClose(0); // �Ƿ�ر�
				platOrder.setIsShip(order.getOrderStatus() == 1 ? 0 : 1); // �Ƿ񷢻� ����״̬��ö��ֵ��1����������2���ѷ�����ǩ�գ�3����ǩ��


				platOrder.setOrderStatus(0); //����״̬
				platOrder.setReturnStatus(0); //�˻�״̬
				platOrder.setHasGift(0); //�Ƿ���Ʒ
				
				
				platOrder.setSellTypId("1");// ��������������ͨ����
				platOrder.setBizTypId("2");// ����ҵ������2c		
				
				platOrder.setFreightPrice(new BigDecimal(order.getPostage())); // �˷�
				platOrder.setVenderId("PDD"); // '�̼�id'
				platOrder.setDownloadTime(sf.format(new Date()));
				
				platOrder.setDeliveryType(String.valueOf(3)); //�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����;2-ֻ˫���ա������ͻ�(�����ղ�����;3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱
				if(StringUtils.isNotEmpty(order.getShippingTime())) {
					platOrder.setShipTime(order.getShippingTime()); //����ʱ��	
				}	
				platOrder.setPayMoney(new BigDecimal(order.getPayAmount()));  //ʵ�����

				platOrder.setOrderSellerPrice(new BigDecimal(order.getPayAmount())); // ���������ʵ�������֧����Ԫ��֧�����=��Ʒ���-�ۿ۽��+�ʷ�
				//discount_amount �ۿ۽�Ԫ���ۿ۽��=ƽ̨�Ż�+�̼��Ż�+�ų��ⵥ�Żݽ��
				//capital_free_discount   �ų��ⵥ�Żݽ�ֻ���ų��ⵥ��вŻ᷵���Żݽ��
				
				// ���ҵ������  �����Żݽ��+�ų��ⵥ�Żݽ��
				platOrder.setAdjustMoney(new BigDecimal(order.getSellerDiscount()).add(new BigDecimal(order.getCapitalFreeDiscount()))); 
						
				platOrder.setDiscountMoney(new BigDecimal(order.getPlatformDiscount())); // ϵͳ�Żݽ��  ƽ̨�Żݽ��
				
				//�Ż�list
				List<CouponDetail> couponList = new ArrayList<>();
		
				//�����Ż�
				if(new BigDecimal(order.getSellerDiscount()).compareTo(BigDecimal.ZERO) != 0) {
					CouponDetail storeCoup = getCouponDetail(storeId,order.getOrderSn(),"�����Ż�",new BigDecimal(order.getSellerDiscount()));
					couponList.add(storeCoup);
				}
				
				//����֧����� ʵ��֧����� = ��Ʒ���-�����Żݽ�� zz
				BigDecimal payMoney = platOrder.getGoodMoney().subtract(platOrder.getAdjustMoney());
				
				
				//֧����ʽlist
				List<PlatOrderPaymethod> payMentList = new ArrayList<>();
				PlatOrderPaymethod payMent = getPlatOrderPaymethod(orderId,order,storeId,payMoney);
				payMentList.add(payMent);
					
				//��Ʒlist
				List<PlatOrders> ordersList = getPlatOrders(order,platOrder,orderId);
				
				//����֧�����,��Ʒ���,֧�����,ƽ������,��̯��� δ��װ���ײ�Ʒ	
				
				ordersList = countAveragePrice(ordersList,payMoney); 
				
				//�����Ʒ�Ƿ�Ϊ���ײ�Ʒ
				ordersList = checkPlatOrdersList(platOrder,ordersList,payMoney);
				
				platOrder.setGoodNum(ordersList.size()); //��Ʒ����
				int c = 0;
				if(couponList.size() > 0) {
					c = couponDetailDao.insert(couponList);
				}
				int p = 0;
				if(payMentList.size() > 0) {
					p = paymethodDao.insert(payMentList);
					
				}
				int po = 0;
				if(platOrder != null) {
					downloadCount ++ ;
					po = platOrderDao.insert(platOrder);
				}
				int  pos = 0;
				if(ordersList.size() > 0) {
					pos = platOrdersDao.insert(ordersList);
				}
				if(po > 0 && pos > 0) {
					/*
					 * // ����������ɺ��ж��Ƿ��Զ����� PlatOrder platorder =
					 * platOrderDao.selectByEcOrderId(orderCode); List<PlatOrders> orderslist =
					 * platOrdersDao.select(platorder.getOrderId()); if
					 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
					 * ����trueʱ���˶���ͨ������ֱ�ӽ������
					 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
					 * // ����Ĭ�ϲ���Աsys } isSuccess = true; message = "���سɹ�";
					 */
					//ִ���Զ�ƥ��
					platOrderService.autoMatch(orderId, userId);
					isSuccess = true;
				}	
			} else {
				
				//δ��ȡ��������Ϣ
				logger.error("��������:ƴ���ƽ̨δ��ȡ��������ϸ");
				message = "��������:ƴ���ƽ̨δ��ȡ��������ϸ";
				isSuccess = false;
			}
					
		}
		// ��־��¼
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(userId);
		MisUser misUser = misUserDao.select(userId);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent("�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + downloadCount + "��");
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
		logRecord.setTypeName("�Զ�����");
		logRecordDao.insert(logRecord);
					
		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess,message, null);
	
		Map<String,Object> map = new HashMap<>();
		map.put("isSuccess", isSuccess);
		map.put("message", message);
		map.put("downloadCount", downloadCount);
		return map;
		
	}
	/**
	 * �����Ʒ�Ƿ�Ϊ���ײ�Ʒ
	 * @throws CloneNotSupportedException 
	 */
	public List<PlatOrders> checkPlatOrdersList(PlatOrder platOrder,List<PlatOrders> orderslist, BigDecimal payMoney) throws CloneNotSupportedException {
		
		boolean flag = true;
		List<PlatOrders> orderslistNew = new ArrayList<>();
		if(orderslist.size() > 0) {
			
			for(int i=0;i<orderslist.size();i++) {
				DecimalFormat dFormat = new DecimalFormat("0.00");
				PlatOrders platOrders = orderslist.get(i);
				// ������� ƽ̨��Ʒ��good_record�е�ƽ̨��Ʒ����
				String ecgoodid = platOrders.getGoodId();// ƽ̨��Ʒ���
				String goodsku = platOrders.getGoodSku();
				GoodRecord goodRecord = goodRecordDao.selectBySkuAndEcGoodId(goodsku, ecgoodid);
				if (goodRecord == null) {
					platOrder.setAuditHint("�ڵ�����Ʒ������δƥ�䵽��Ӧ��Ʒ����");
					
					flag = false;
					orderslistNew.add(platOrders);
					continue;
				} else if (goodRecord.getGoodId() == null) {
					platOrder.setAuditHint("�����Ƶ�����Ʒ�����д������Ķ�Ӧ��ϵ");
					
					flag = false;
					orderslistNew.add(platOrders);
					continue;
				} else {
					String invId = goodRecord.getGoodId();
					
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(invId);// ȥ���������ѯ���������Ϣ
					if (invtyDoc == null) {
						platOrder.setAuditHint("�ڴ��������δƥ�䵽��Ӧ�������,��ƥ��Ĵ�����룺" + invId);
						
						flag = false;
						orderslistNew.add(platOrders);
						continue;
					} else {

						if (invtyDoc.getPto() != null) {
							if (invtyDoc.getPto() == 1) {
								// ����PTO���ʹ��
								ProdStru prodStru = prodStruDao.selectMomEncd(invId); //��ѯĸ����Ʒ
								
								if (prodStru == null) {
									
									platOrder.setAuditHint("�ڲ�Ʒ�ṹ��δƥ�䵽��Ӧ��Ʒ�ṹ��Ϣ");								
									flag = false;
									orderslistNew.add(platOrders);
									continue;
								} else {
									if (prodStru.getIsNtChk() == 0) {
										// ��Ӧ��Ʒ�ṹ��δ���
										platOrder.setAuditHint("��Ӧ��Ʒ�ṹ��δ���");
										flag = false;
										orderslistNew.add(platOrders);
										continue;
									} else {
										List<ProdStruSubTab> strucksublist = prodStru.getStruSubList();
										// ѭ����Ʒ�ṹ�ӱ���Ϣ
										if (strucksublist.size() == 0) {
											platOrder.setAuditHint("��Ӧ��Ʒ�ṹû�������Ӽ���ϸ���������á�");
											// platOrderDao.update(platOrder);//���¶����������ʾ��
											flag = false;
											orderslistNew.add(platOrders);
											continue;
										} else if (strucksublist.size() == 1) {
											// ����Ʒ�ṹ��ϸ����ֻ��һ���Ӽ�ʱ��ֱ���滻�������
											// �������
											platOrders.setInvId(strucksublist.get(0).getSubEncd());
											// ���ô������
											platOrders.setInvNum(new BigDecimal(platOrders.getGoodNum())
													.multiply(strucksublist.get(0).getSubQty()).intValue());
											// ���ÿ�������
											platOrders.setCanRefNum(platOrders.getInvNum());
											platOrders.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
											platOrders.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
											// ����ʵ������
											platOrders.setPayPrice(platOrders.getPayMoney().divide(
													(new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
											platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
											if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
												platOrders.setIsGift(1);
												platOrder.setHasGift(1);
											} else {// ����ʵ�������Ƿ�Ϊ0�ж��Ƿ�Ϊ��Ʒ
												platOrders.setIsGift(0);
											}
											// ���ÿ��˽��,��ʵ�����һ��
											platOrders.setCanRefMoney(platOrders.getPayMoney());

											orderslistNew.add(platOrders);
										} else {
											// ����Ʒ�ṹ���Ӽ�����������1ʱ����Ҫ���ɶ�����ϸ

											// �����Ӽ��ο��ɱ����Ӽ��������ܳɱ�
											// ������꣬���false˵����Ӧ�Ӽ��Ĳο��ɱ�δ���ã�ϵͳ�޷���ֱ���
											BigDecimal total = new BigDecimal("0");
											for (int j = 0; j < strucksublist.size(); j++) {
												InvtyDoc invtyDoc1 = invtyDocDao
														.selectInvtyDocByInvtyDocEncd(strucksublist.get(j).getSubEncd());
												if (invtyDoc1.getRefCost() == null) {
													flag = false;
													break;
												} else {
													total = total.add(invtyDoc1.getRefCost()
															.multiply(strucksublist.get(j).getSubQty()));
												}
											}

											for (int j = 0; j < strucksublist.size(); j++) {
												//TODO ---
												PlatOrders order = platOrders;
												BigDecimal money123 = new BigDecimal("0");// �����ѷ�ʵ����� ���һ���ü���ʱ�õ�
												if (j + 1 < strucksublist.size()) {
													// ����ÿ���Ӽ�ռ�ܳɱ��ı��� ����8λС��
													InvtyDoc invtyDoc2 = invtyDocDao.selectInvtyDocByInvtyDocEncd(
															strucksublist.get(j).getSubEncd());
													BigDecimal rate = invtyDoc2.getRefCost()
															.multiply(strucksublist.get(j).getSubQty())
															.divide(total, 8, BigDecimal.ROUND_HALF_UP);
													order.setInvNum((new BigDecimal(order.getGoodNum()))
															.multiply(strucksublist.get(j).getSubQty()).intValue());
													order.setPayMoney(new BigDecimal(dFormat.format(order.getPayMoney().multiply(rate))));// ����ʵ�����
																													// ������λС��
													// ����ʵ������ ����8λС��
													order.setPayPrice(
															order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																	8, BigDecimal.ROUND_HALF_UP));
													order.setSellerPrice(order.getPayPrice());
													// �������
													order.setInvId(strucksublist.get(0).getSubEncd());
													// ���ÿ�������
													order.setCanRefNum(order.getInvNum());
													// ���ÿ��˽��
													order.setCanRefMoney(order.getPayMoney());
													order.setPtoCode(invtyDoc2.getInvtyEncd());// ���ö�Ӧĸ������
													order.setPtoName(invtyDoc2.getInvtyNm());// ���ö�Ӧĸ������
													if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
														order.setIsGift(1);
														platOrder.setHasGift(1);
													} else {// �����Ƿ���Ʒ
														order.setIsGift(0);
													}
													orderslistNew.add(order);
													money123 = money123.add(order.getPayMoney());
												} else {
													order.setInvNum((new BigDecimal(order.getGoodNum()))
															.multiply(strucksublist.get(j).getSubQty()).intValue());
													order.setPayMoney(order.getPayMoney().subtract(money123));// ����ʵ�����
																												// ������λС��
													// ����ʵ������ ����8λС��
													order.setPayPrice(
															order.getPayMoney().divide((new BigDecimal(order.getInvNum())),
																	8, BigDecimal.ROUND_HALF_UP));
													order.setSellerPrice(order.getPayPrice());
													// �������
													order.setInvId(strucksublist.get(0).getSubEncd());
													// ���ÿ�������
													order.setCanRefNum(order.getInvNum());
													// ���ÿ��˽��
													order.setCanRefMoney(order.getPayMoney());
													order.setPtoCode(invtyDoc.getInvtyEncd());// ���ö�Ӧĸ������
													order.setPtoName(invtyDoc.getInvtyNm());// ���ö�Ӧĸ������
													if (order.getPayMoney().compareTo(BigDecimal.ZERO) == 0) {
														order.setIsGift(1);
														platOrder.setHasGift(1);
													} else {// �����Ƿ���Ʒ
														order.setIsGift(0);
													}
													orderslistNew.add(order);
												}

											}
										}

									}
								}
							} else {
								//������Ʒ
								// ������PTO���ʹ��
								// �������
								platOrders.setInvId(goodRecord.getGoodId());
								// ���ô������
								platOrders.setInvNum(platOrders.getGoodNum());
								// ���ÿ�������
								platOrders.setCanRefNum(platOrders.getInvNum());
								platOrders.setPtoCode("");// ���ö�Ӧĸ������
								platOrders.setPtoName("");// ���ö�Ӧĸ������
								// ����ʵ������
								platOrders.setPayPrice(platOrders.getPayMoney()
										.divide((new BigDecimal(platOrders.getInvNum())), 8, BigDecimal.ROUND_HALF_UP));
								platOrders.setSellerPrice(platOrders.getPayPrice());// ���㵥����ʵ������һ��
								if (platOrders.getPayPrice().compareTo(BigDecimal.ZERO) == 0) {
									platOrders.setIsGift(1);
									platOrder.setHasGift(1);
								} else {// ����ʵ�������Ƿ�Ϊ0�ж��Ƿ�Ϊ��Ʒ
									platOrders.setIsGift(0);
								}
								// ���ÿ��˽��,��ʵ�����һ��
								platOrders.setCanRefMoney(platOrders.getPayMoney());

								orderslistNew.add(platOrders);
							}
								
						} else {
							// �������pto����Ϊ��
							platOrder.setAuditHint("���������PTO����Ϊ��");					
							flag = false;
							orderslistNew.add(platOrders);
							continue;
						}	
					}
				}
			}
		} else {
			return orderslist;
		}
		return orderslistNew;
	}

	/**
	 * ֧����ϸ
	 */
	public PlatOrderPaymethod getPlatOrderPaymethod(String orderId,OrderInfoGetResponseOrderInfo order,String storeId, BigDecimal payMoney) {
		PlatOrderPaymethod payMent = new PlatOrderPaymethod();
		payMent.setPlatId("PDD");
		payMent.setStoreId(storeId);
		payMent.setOrderId(orderId); //����ƽ̨���
		payMent.setPayMoney(payMoney);  //֧�����
		payMent.setPaymoneyTime(order.getPayTime()); //֧��ʱ��
		payMent.setPaymentNumber(order.getPayNo()); //���׵���
		payMent.setPayWay(order.getPayType()); //֧����ʽ
		payMent.setPayStatus("֧���ɹ�"); //֧��״̬ Ĭ��Ϊ֧���ɹ�
		return payMent;
	}

	/**
	 * �Ż���ϸ
	 */
	private CouponDetail getCouponDetail(String storeId,String orderSn,String couponType,BigDecimal price) {
		CouponDetail coupon = new CouponDetail();
		coupon.setPlatId("PDD"); //ƽ̨����
		coupon.setStoreId(storeId);//���̱���
		coupon.setOrderId(orderSn); //���̶������							
		coupon.setSkuId(null);
		coupon.setCouponCode(null);// �Ż����ͱ���
		coupon.setCouponType(couponType);// �Ż�����
		coupon.setCouponPrice(price);// �Żݽ��
		return coupon;
	}
	
	/**
	 * ����ƽ����
	 */
	public List<PlatOrders> countAveragePrice(List<PlatOrders> list,BigDecimal payMoney) {
		
		boolean isSuccess = true;
					
		BigDecimal payTotal = new BigDecimal(0.00);
		
		for(PlatOrders orders: list) {
			//Ӧ���ܽ��
			 payTotal = payTotal.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));
		}
		
		BigDecimal payTemp = payMoney;
		
		List<PlatOrders> orderList = new ArrayList<>();
		
		for(int i=0;i<list.size();i++) {
			PlatOrders orders = list.get(i);
			
			//֧�����Ϊ0
			if(payMoney.compareTo(BigDecimal.ZERO) == 0) {
				
				BigDecimal itemTotal = orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())); //��ƷӦ��		
				orders.setPayMoney(new BigDecimal(0.00)); //ʵ�����
				orders.setGoodMoney(itemTotal); //��Ʒ���				
				orders.setPayPrice(new BigDecimal(0.00));//ʵ������
				
			} else {
				//�ж��Ƿ������һ��
				if(i == list.size()-1) {
					
					//��ƷӦ��ռ��
					BigDecimal itemTotal = orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())); //��ƷӦ��
					BigDecimal itemFact = payTemp; //��Ʒ֧��
					orders.setPayMoney(itemFact); //ʵ�����
					orders.setGoodMoney(itemTotal); //��Ʒ���
					orders.setCanRefMoney(itemFact); //���˽��
					orders.setCanRefNum(orders.getGoodNum()); //��������
					
					orders.setPayPrice(itemFact.divide(new BigDecimal(orders.getGoodNum()),6,BigDecimal.ROUND_HALF_DOWN));//ʵ������
					
				} else {
							
					//��ƷӦ��ռ��
					BigDecimal itemTotal = orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())); //��ƷӦ��
					BigDecimal itemFact = itemTotal.multiply(payMoney).divide(payTotal,2,BigDecimal.ROUND_HALF_DOWN); //��Ʒ֧��
					orders.setPayMoney(itemFact); //ʵ�����
					orders.setGoodMoney(itemTotal); //��Ʒ���
					orders.setCanRefMoney(itemFact); //���˽��
					orders.setCanRefNum(orders.getGoodNum()); //��������
					
					orders.setPayPrice(itemFact.divide(new BigDecimal(orders.getGoodNum()),6,BigDecimal.ROUND_HALF_DOWN));//ʵ������
					payTemp = payTemp.subtract(itemFact);
					
				}
				orders.setDiscountMoney(orders.getGoodMoney().subtract(payMoney));//�Żݽ��
			}
			orderList.add(orders);
			isSuccess = true;
		}		
		
		logger.info("��������:ƽ̨�������ؼ���ƽ������ִ�н��:{}",isSuccess);
		return orderList;
	}
	
	private List<PlatOrders> getPlatOrders(OrderInfoGetResponseOrderInfo order,PlatOrder platOrder,String orderId) {
		List<PlatOrders> list = new ArrayList<PlatOrders>();
		
		List<OrderInfoGetResponseOrderInfoItemListItem> item_list = order.getItemList();
		
		for (int i = 0; i < item_list.size(); i++) {
			OrderInfoGetResponseOrderInfoItemListItem item = item_list.get(i);
			PlatOrders platOrders = new PlatOrders();
			platOrders.setGoodId(String.valueOf(item.getGoodsId())); // ƽ̨��Ʒ���
			platOrders.setGoodSku(String.valueOf(item.getSkuId())); //��Ʒ������
			platOrders.setGoodPrice(new BigDecimal(item.getGoodsPrice())); //��Ʒ���ۼ۸� pdd
			
				
			platOrders.setGoodName(item.getGoodsName()); // ƽ̨��Ʒ����
			platOrders.setGoodNum(item.getGoodsCount()); //��Ʒ����
//			platOrders.setGoodMoney(new BigDecimal(item.getDouble("goods_price"))); // ��Ʒ��� 
		
			platOrders.setOrderId(orderId); // '�������' 1 	
			platOrders.setExpressCom(String.valueOf(order.getLogisticsId())); // ��ݹ�˾

			platOrders.setGoodPrice(new BigDecimal(item.getGoodsPrice())); //��Ʒ���� 
																								
			platOrders.setEcOrderId(order.getOrderSn()); // 'ƽ̨������' t oid

			platOrder.setDeliverSelf(1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t

			platOrders.setInvId(item.getOuterGoodsId()); // ��������̼��ⲿ����

			list.add(platOrders);
		}
		return list;
	}
}
