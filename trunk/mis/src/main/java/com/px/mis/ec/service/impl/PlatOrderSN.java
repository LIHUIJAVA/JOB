package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import com.px.mis.ec.entity.EcExpress;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.InvoiceInfo;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogisticsTab;
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
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.ExpressCorpMapper;
import com.px.mis.whs.dao.ProdStruMapper;
import com.px.mis.whs.entity.ExpressCorp;
import com.px.mis.whs.entity.ProdStru;
import com.px.mis.whs.entity.ProdStruSubTab;
import com.suning.api.DefaultSuningClient;
import com.suning.api.entity.custom.DeliverynewAddRequest;
import com.suning.api.entity.custom.orderQueryQueryRequest;
import com.suning.api.entity.custom.orderQueryQueryResponse;
import com.suning.api.entity.custom.DeliverynewAddRequest.OrderLineNumbers;
import com.suning.api.entity.custom.DeliverynewAddResponse;
import com.suning.api.entity.custom.DeliverynewAddResponse.SendDetail;
import com.suning.api.entity.custom.orderGetGetRequest;
import com.suning.api.entity.custom.orderGetGetResponse;
import com.suning.api.entity.custom.orderGetGetResponse.OrderGet;
import com.suning.api.entity.custom.orderQueryQueryResponse.OrderQuery;
import com.suning.api.entity.custom.orderQueryQueryResponse.OrderDetail;
import com.suning.api.entity.custom.orderQueryQueryResponse.CouponList;
import com.suning.api.entity.custom.orderQueryQueryResponse.PaymentList;
import com.suning.api.entity.custom.orderSelfDistAddRequest.ProductCodes;
import com.suning.api.entity.item.ItemQueryRequest;
import com.suning.api.entity.item.ItemQueryResponse;
import com.suning.api.entity.item.ItemQueryResponse.ItemQuery;
import com.suning.api.entity.rejected.BatchrejectedQueryRequest;
import com.suning.api.entity.rejected.BatchrejectedQueryResponse;
import com.suning.api.entity.rejected.Rejected;
import com.suning.api.entity.transaction.LogisticcompanyGetRequest;
import com.suning.api.entity.transaction.LogisticcompanyGetResponse;
import com.suning.api.exception.SuningApiException;

/**
 * ����-����
 * @author lxya0
 *
 */
@Component
public class PlatOrderSN {
	
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
	private ExpressCorpMapper expressCorpDao;//��ݹ�˾
	@Autowired
	private PlatOrderPdd platOrderPdd; 
	@Autowired
	private AuditStrategyService auditStrategyService;
	@Autowired
	private AssociatedSearchService associatedSearchService;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private MisUserDao misUserDao;
	@Autowired
	private PlatOrderService platOrderService;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private Logger logger = LoggerFactory.getLogger(PlatOrderSN.class);
	
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String message = "";
	boolean isSuccess = true;
	/**
	 * ������������-����	
	 * @throws Exception 
	 */
	@Transactional
	public String snDownload(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception {
		
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		List<OrderQuery> orderQueryList = new ArrayList<>();
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		orderQueryList = postSnOrder(storeSettings,storeRecord,pageNo,pageSize,startDate,endDate,orderQueryList);
		if(orderQueryList.size() > 0) {
			map = getPlatOrder(orderQueryList,storeId,accNum,storeRecord);	
			message = (String)map.get("message");
			isSuccess = (boolean)map.get("isSuccess");
		}else {
			message="�����Զ����ص��̣�"+storeRecord.getStoreName()+"����0��";
		}
		
		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
				
		return resp;
		
	}
	
	/**
	 * ������������-����	
	 * @throws Exception 
	 */
	@Transactional
	public String snDownloadByOrderId(String accNum,String orderId, String startDate, String endDate,
			String storeId) throws Exception {
		
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		OrderGet orderGet = getSnOrder(storeSettings, storeRecord, orderId);
		
		
		map = getPlatOrder(orderGet,storeId,accNum,storeRecord);	
		message = (String)map.get("message");
		isSuccess = (boolean)map.get("isSuccess");
	
		
		resp = BaseJson.returnRespObj("ec/platOrder/download", isSuccess, message, null);
				
		return resp;
		
	}
	

	/**
	 * ��Ʒ������-����
	 * @return
	 */
	@Transactional
	public List<GoodRecord> getSNSku(String accNum, int pageNo, int pageSize, String date,
			StoreSettings storeSettings) throws Exception  {
	
		String storeId = storeSettings.getStoreId();
		List<GoodRecord> gRecordList = new ArrayList<>();
		List<ItemQuery> itemQueryList = new ArrayList<>();
		
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		itemQueryList = postSnProductCode(storeSettings,storeRecord,pageNo,pageSize,itemQueryList);
		
		if(itemQueryList.size() > 0) {
			gRecordList = getSNGoodRecord(itemQueryList,storeId,accNum,date,storeRecord,gRecordList);		
		} else {
			message = "����������Ʒ��ʧ��";
			isSuccess = false;
		}	
		return gRecordList;
		
	}
	
	
	/**
	 * �����˻�-����
	 */
	@Transactional
	public String snRefund(String accNum, int pageNo, int pageSize, String startDate, String endDate,
			String storeId) throws Exception  {
		
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		Map<String,Object> map = new HashMap<>();
		int successCount = 0;
		List<Rejected> orderQueryList = new ArrayList<>();
		
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		StoreRecord storeRecord = storeRecordDao.select(storeId);
		//post sn
		orderQueryList = postSnRefundOrder(storeSettings,storeRecord,pageNo,pageSize,startDate,endDate,orderQueryList);
		
		
		if(orderQueryList.size() > 0) {
				
			map = getRefundOrder(orderQueryList,storeId,accNum,storeRecord);
			/*message = (String)map.get("message");
			isSuccess = (boolean)map.get("isSuccess");*/
			logger.info("�˿����:��������ƽ̨�����˿��������:{}��",orderQueryList.size());
			successCount=Integer.parseInt(map.get("successCount").toString());
		}
		MisUser misUser = misUserDao.select(accNum);
		// ��־��¼
		LogRecord logRecord = new LogRecord();
		logRecord.setOperatId(accNum);
		if (misUser != null) {
			logRecord.setOperatName(misUser.getUserName());
		}
		logRecord.setOperatContent("���γɹ����ص��̣�"+storeRecord.getStoreName()+",�˿��"+successCount+"����");
		logRecord.setOperatTime(sf.format(new Date()));
		logRecord.setOperatType(12);// 12�˿�����
		logRecord.setTypeName("�˿����");
		logRecord.setOperatOrder(null);
		logRecordDao.insert(logRecord);
		
		resp = BaseJson.returnRespObj("ec/refundOrder/download", isSuccess, "���γɹ������˿"+successCount+"��", null);
		return resp;
		
	}
	/**
	 * post����-��������
	 * @param logistics ��������Ϣ
	 * @param express ��ݹ�˾��Ϣ
	 * @param storeSettings ��������
	 * @throws SuningApiException 
	 */

	@Transactional(rollbackForClassName={"Exception"})
	public DeliverynewAddResponse SNOrderShip(LogisticsTab logistics, EcExpress express,StoreSettings storeSettings) throws SuningApiException {
		//���ݿ�ݹ�˾�����ѯ��ݹ�˾����	
		ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logistics.getExpressEncd());
		String companyCode = "";
		if(expressCorp != null) {
			List<ExpressCorp> corpList = expressCorpDao.selectExpressEncdName(expressCorp.getExpressEncd());
			if(corpList.size()  > 0) {
				
				companyCode = SnGetCommpayCode(corpList.get(0).getExpressNm(),storeSettings);
			}
		}
		
		
		DeliverynewAddRequest request = new DeliverynewAddRequest();
		request.setDeliveryTime(sf.format(new Date())); //����ʱ��	
		request.setExpressCompanyCode(companyCode); //������˾����----����
		request.setExpressNo(logistics.getExpressCode()); //�˵���
		request.setOrderCode(logistics.getEcOrderId()); //������
		//request.setOrderCode("37108911108");
		List<OrderLineNumbers> numbers = new ArrayList<>();
		
		/*
		 * OrderLineNumbers order = new OrderLineNumbers();
		 * order.setOrderLineNumber("");//��������Ŀ�� order.setProductCode(""); //������Ʒ����
		 */
		  request.setOrderLineNumbers(numbers); 
		 //List<SendDetail> sendDetails = new ArrayList<SendDetail>(); 
		  List<PlatOrders> platOrders =platOrdersDao.selectListByOrderIdAndGroupByEcGoodId(logistics.getOrderId());
		  for (int i = 0; i < platOrders.size(); i++) { 
			  OrderLineNumbers orderLineNumbers = new OrderLineNumbers();
			  orderLineNumbers.setProductCode(platOrders.get(i).getGoodId());
			  numbers.add(orderLineNumbers); 
		  } 
		  request.setOrderLineNumbers(numbers);
		 
		//api���У���߼����أ��������ȶ�֮��������Ϊ false ����ɾ������
		//request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		DeliverynewAddResponse result = client.excute(request);
	
		return result;
		 
		
	}
	/**
	 * get- ��ȡ������˾����
	 * @param companyName ��ݹ�˾����
	 * @param storeSettings ��������
	 * @return
	 */
	private String SnGetCommpayCode(String companyName,StoreSettings storeSettings) {
		String companyCode = "";
		
	    LogisticcompanyGetRequest request = new LogisticcompanyGetRequest();
	   if(companyName.equals("Բͨ���")) {
		   companyName="Բͨ�ٵ�";
	   }
		request.setCompanyName(companyName);
		//api���У���߼����أ��������ȶ�֮��������Ϊ false ����ɾ������
		request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		try {
		 LogisticcompanyGetResponse response = client.excute(request);
		 if(response.getSnbody() != null) {
			 if(response.getSnbody().getLogisticCompany().size() > 0) {
				 companyCode = response.getSnbody().getLogisticCompany().get(0).getExpressCompanyCode();
				// System.out.println(companyCode);
			 }
		 }
		} catch (SuningApiException e) {
		 e.printStackTrace();
		}
		return companyCode;
	}


	/**
	 * post����-������ȡ����
	 * @throws SuningApiException 
	 */
	private List<OrderQuery> postSnOrder(StoreSettings storeSettings, StoreRecord storeRecord, int pageNo, int pageSize,
			String startDate, String endDate, List<OrderQuery> orderQueryList) throws SuningApiException {
		
	
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();	
		orderQueryQueryRequest request = new orderQueryQueryRequest();
		
		request.setEndTime(endDate);
		//request.setOrderStatus("10"); //����ͷ״̬��10������Ѹ��20�������ѷ�����21�����ַ�����30�����׳ɹ���40�����׹رգ�
		request.setPageNo(pageNo);
		request.setPageSize(pageSize);
		request.setStartTime(startDate);//��ѯ���״�����ʼʱ��
		//request.setUserID("7017925352"); //��Ա����
		//request.setUserName("123@qq.com"); //�������
		
		//api���У���߼����أ��������ȶ�֮��������Ϊ false ����ɾ������
		//request.setCheckParam(true);
		
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		
		orderQueryQueryResponse response = client.excute(request);
		
		if(response.getSnerror() != null) {
			
			message = "��������:����ƽ̨���ض����쳣,�쳣��Ϣ:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("��������:����ƽ̨���ض����쳣,�쳣��Ϣ:{}",response.getSnerror().getErrorMsg());
		}
		
		
		if(response.getSnbody() != null) {
			orderQueryList.addAll(0,response.getSnbody().getOrderQuery());
					
		}
		if(response.getSnhead() != null && response.getSnhead().getTotalSize() != null) {
			if(Integer.valueOf(response.getSnhead().getTotalSize()) > pageNo * pageSize) {
				 postSnOrder(storeSettings,storeRecord,pageNo+1,pageSize,startDate,endDate,orderQueryList);
			}
		}
		return orderQueryList;
	}
	
	
	/**
	 * get����-������ȡ����
	 * @throws SuningApiException 
	 */
	private OrderGet getSnOrder(StoreSettings storeSettings, StoreRecord storeRecord,String ecOrderId) throws SuningApiException {
		
	
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();	
		//orderQueryQueryRequest request = new orderQueryQueryRequest();
		orderGetGetRequest request = new orderGetGetRequest();
		request.setOrderCode(ecOrderId);//������
		//request.setEndTime(endDate);
		//request.setOrderStatus("10"); //����ͷ״̬��10������Ѹ��20�������ѷ�����21�����ַ�����30�����׳ɹ���40�����׹رգ�
		//request.setPageNo(pageNo);
		//request.setPageSize(pageSize);
		//request.setStartTime(startDate);//��ѯ���״�����ʼʱ��
		//request.setUserID("7017925352"); //��Ա����
		//request.setUserName("123@qq.com"); //�������
		
		//api���У���߼����أ��������ȶ�֮��������Ϊ false ����ɾ������
		request.setCheckParam(true);
		
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		
		orderGetGetResponse response = client.excute(request);
		
		if(response.getSnerror() != null) {
			
			message = "��������:����ƽ̨���ض����쳣,�쳣��Ϣ:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("��������:����ƽ̨���ض����쳣,�쳣��Ϣ:{}",response.getSnerror().getErrorMsg());
		}
		
		OrderGet orderGet = new OrderGet();
		if(response.getSnbody() != null) {
			//orderQueryList.addAll(0,response.getSnbody().getOrderGet());
			orderGet = response.getSnbody().getOrderGet();	
		}
		return orderGet;
	}
	
	/**
	 * post����-������ȡ�˿
	 */
	private List<Rejected> postSnRefundOrder(StoreSettings storeSettings,StoreRecord storeRecord,
			int pageNo, int pageSize, String startDate, String endDate,List<Rejected> reList) throws SuningApiException {
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		
		//�˻���
		BatchrejectedQueryRequest request = new BatchrejectedQueryRequest();
		request.setStartTime(startDate);
		request.setEndTime(endDate);
		request.setPageNo(pageNo);
		request.setPageSize(pageSize);
		
		//api���У���߼����أ��������ȶ�֮��������Ϊ false ����ɾ������
		request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");
		
		BatchrejectedQueryResponse response = client.excute(request);
		if(response.getSnerror() != null) {
			
			message = "�˿����:����ƽ̨�����˿�쳣,�쳣��Ϣ:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("�˿����:����ƽ̨�����˿�쳣,�쳣��Ϣ:{}",response.getSnerror().getErrorMsg());
		}
		if(response.getSnbody() != null) {
			
			reList.addAll(0,response.getSnbody().getRejects());	
					
		}
		
		if(response.getSnhead() != null && response.getSnhead().getTotalSize() != null) {
			if(Integer.valueOf(response.getSnhead().getTotalSize()) > pageNo * pageSize) {
				
				postSnRefundOrder(storeSettings,storeRecord,pageNo+1,pageSize,startDate,endDate,reList);
			}
		}
		return reList;
	}
	/**
	 * post����-������ȡ��Ʒ��
	 * @throws SuningApiException 
	 */
	private List<ItemQuery> postSnProductCode(StoreSettings storeSettings, StoreRecord storeRecord, int pageNo,
			int pageSize,List<ItemQuery> itemQueryList) throws SuningApiException {
		String appKey = storeSettings.getAppKey();
		String appSecret = storeSettings.getAppSecret();
		
		ItemQueryRequest request = new ItemQueryRequest();
		request.setCategoryCode("");
		request.setBrandCode("");
		request.setStatus("2");
		//request.setStartTime(startDate);
		//request.setEndTime(endDate);
		request.setPageNo(pageNo);
		request.setPageSize(pageSize);	
		//api���У���߼����أ��������ȶ�֮��������Ϊ false ����ɾ������
		request.setCheckParam(true);
		String serverUrl = "https://open.suning.com/api/http/sopRequest";
		
		DefaultSuningClient client = new DefaultSuningClient(serverUrl, appKey,appSecret, "json");	
		ItemQueryResponse response = client.excute(request);
		if(response.getSnerror() != null) {
			
			message = "��Ʒ������:����ƽ̨������Ʒ���쳣,�쳣��Ϣ:"+	response.getSnerror().getErrorMsg();
			isSuccess = false;
			logger.error("��Ʒ������:����ƽ̨������Ʒ���쳣,�쳣��Ϣ:{}",response.getSnerror().getErrorMsg());
		}
		if(response.getSnbody() != null) {
			
			itemQueryList.addAll(0,response.getSnbody().getItemQueries());	
					
		}
		
		if(response.getSnhead() != null && response.getSnhead().getTotalSize() != null) {
			if(Integer.valueOf(response.getSnhead().getTotalSize()) > pageNo * pageSize) {
				
				postSnProductCode(storeSettings,storeRecord,pageNo+1,pageSize,itemQueryList);
			}
		}
		return itemQueryList;
	}

	/**
	 * ��ȡ��Ʒ����Ϣ
	 */
	private List<GoodRecord> getSNGoodRecord(List<ItemQuery> itemQueryList, String storeId, String accNum,String date,
			StoreRecord storeRecord,List<GoodRecord> gRecordList) {
		
		
		if(itemQueryList.size() > 0) {
			
			for(ItemQuery itemQuery : itemQueryList) {
				GoodRecord gRecord = new GoodRecord();
				
				gRecord.setEcGoodName(itemQuery.getProductName()); //������Ʒ����
				gRecord.setEcGoodId(itemQuery.getProductCode()); //������Ʒ����
				gRecord.setGoodId(itemQuery.getItemCode()); //�̼�����Ʒ����
				gRecord.setStoreId(storeId);
				gRecord.setEcId("SN");
				gRecord.setStoreId(storeRecord.getStoreId());
				gRecord.setEcName("�����׹�");
				
				//gRecord.setOnlineStatus(skuJob.getIntValue("is_sku_onsale") == 0 ? "�¼���" : "����");
				
				Integer id = goodRecordDao.selectByEcGoodIdAndSku(gRecord);
				gRecord.setId(id);
				
				//itemQuery.getCategoryName(); //������Ʒ�ɹ�Ŀ¼����
				//itemQuery.getCategoryCode(); //������Ʒ�ɹ�Ŀ¼����
				//itemQuery.getBrandName(); //������ƷƷ�����ơ�
				//itemQuery.getBrandCode(); //������ƷƷ�Ʊ��롣
				//itemQuery.getStatus(); //����״̬��1�����ڴ���2������ɹ���3������ʧ�ܣ�4����˲�ͨ����
				//itemQuery.getPublished(); //������Ʒ��һ���ύʱ��
				//itemQuery.getImg1Url(); //�̼���ƷͼƬ1��ַurl
				//itemQuery.getEditTime(); //����޸�ʱ��
				//itemQuery.getProductName(); //����Ʒ����Ʒ����
				//itemQuery.getProductCode(); //����Ʒ����Ʒ����
				//itemQuery.getItemCode(); //����Ʒ���̼���Ʒ���롣��������Ʒ����չʾ���ֶΡ�
				//itemQuery.getCmTitle(); //��Ʒ����
				
					
				gRecord.setIsSecSale(0);
				
				gRecord.setSafeInv("0");
				gRecord.setOperatTime(date);
				gRecord.setOperator(accNum);
				
				gRecordList.add(gRecord);
			}
		} else {
			//����Ϊ��
			message = "������Ʒ��:δ��ȡ��������Ʒ����Ϣ";
			logger.error("������Ʒ��:δ��ȡ��������Ʒ����Ϣ");
		}
		return gRecordList;
	}
	
	/**
	 * �˿-����
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws ParseException 
	 */
	private Map<String,Object> getRefundOrder(List<Rejected> rejectedList,
											  String storeId, String accNum,
											  StoreRecord storeRecord) throws IllegalAccessException, InvocationTargetException, ParseException {
		
	
		Map<String,Object> map = new HashMap<>();
		int successCount=0;
		if(rejectedList.size() > 0) {
			
			for(Rejected rejected : rejectedList) {
				//System.out.println("AAAAAAA״̬"+rejected.getStatusDesc());
				//refundtype ����״̬�� 1-�˿������ 2-�Ѿܾ��˿� �� 3-����ҷ��� �� 4-���̼��ջ� �� 5-�˿�ʧ�ܣ� 6-�˿���� �� 7-�˿�ر� �� 8-�˿�ɹ���C030-�����˻�
				if(rejected.getStatusDesc().equals("�˿������")||rejected.getStatusDesc().equals("�Ѿܾ��˿�")||rejected.getStatusDesc().equals("�˿�ʧ��")||
						rejected.getStatusDesc().equals("�˿�ر�")||rejected.getStatusDesc().equals("�����˻�")) {
					//�⼸������²����ض�Ӧ�˿
					continue;
				}
				
				//��ѯƽ̨����
				List<PlatOrder> order = platOrderDao.selectPlatOrdersByEcOrderId(rejected.getOrderCode());
				if (order.size()>0) {
					String ecRefId = rejected.getOrderCode()+"-"+rejected.getProductCode()+"-"+rejected.getReturntype();//�����˿��
					RefundOrder refundOrder = refundOrderDao.selectEcRefId(ecRefId);
					if(refundOrder == null) {//returntype 2��ʾ��δ����3��ʾ���ѷ�δ�ա�4��ʾ������
						
						refundOrder = new RefundOrder();
						String refId = getOrderNo.getSeqNo("tk", accNum);
						refundOrder.setRefId(refId);
						refundOrder.setExpressCode(rejected.getExpressNo()); // ��ݵ���
						refundOrder.setEcOrderId(rejected.getOrderCode()); //B2C������
						refundOrder.setOrderId(order.get(0).getOrderId());
						refundOrder.setEcId("SN");
						refundOrder.setEcRefId(ecRefId);//�����˿��
						refundOrder.setTreDate(rejected.getStatusPassTime());//����ʱ��
						refundOrder.setStoreId(storeRecord.getStoreId());
						refundOrder.setStoreName(storeRecord.getStoreName());
						//refundOrder.setEcRefId(rejected.getItemNo()); //�����к�
						refundOrder.setApplyDate(rejected.getApplyTime()); //����ʱ��
						refundOrder.setRefExplain(rejected.getStatusDesc()); //�˿�˵��   ����״̬�� 1-�˿������ 2-�Ѿܾ��˿� �� 3-����ҷ��� �� 4-���̼��ջ� �� 5-�˿�ʧ�ܣ� 6-�˿���� �� 7-�˿�ر� �� 8-�˿�ɹ���C030-�����˻�
						refundOrder.setDownTime(sf.format(new Date()));
						refundOrder.setIsAudit(0); //�Ƿ����
						refundOrder.setSource(0);				
						refundOrder.setRefReason(rejected.getReturnReason()); //�˻�ԭ��
						refundOrder.setAllRefMoney(new BigDecimal(rejected.getReturnMoney()));//ʵ���˿���
						refundOrder.setSource(0);
						if(StringUtils.isEmpty(rejected.getRefundtype())) {
							refundOrder.setIsRefund(1);
							int count =0;
							for (int i = 0; i < order.size(); i++) {
								count+=order.get(i).getGoodNum();
							}
							refundOrder.setAllRefNum(count);
						} else {
							refundOrder.setIsRefund(0);
							refundOrder.setAllRefNum(0);//���˿������
						}
						List<RefundOrders> ordersList = new ArrayList<>();
						
						RefundOrders orders = new RefundOrders();
						orders.setRefId(refId);
						orders.setEcGoodId(rejected.getProductCode()); //������Ʒ����
						
						
						//rejected.getCustomer(); //����ʺ�
						
						//rejected.getDealMoney(); //���׽��
						
						
						orders.setRefMoney(new BigDecimal(rejected.getDealMoney())); //�˿��� ȡƽ̨���׽��
						
						//rejected.getStatusPassTime(); //����ʱʱ��,��ʽ��yyyy-MM-dd HH:mm��ss
										
						rejected.getExpressCompanyCode(); //������˾����
						rejected.getExpressNo(); //������˾�˵���
						rejected.getReturntype(); //2��ʾ��δ����3��ʾ���ѷ�δ�ա�4��ʾ������
						
						ordersList.add(orders);
						refundOrder.setRefundOrders(ordersList);
						refundOrder = checkRefundIsPtoSN(refundOrder,accNum,rejected.getReturntype());
										
						if (refundOrder.getRefundOrders().size()>0) {
							//insert
							refundOrderDao.insert(refundOrder);
							refundOrdersDao.insertList(refundOrder.getRefundOrders());
							successCount++;
						}
						
						
					} 	
					
				} 
							
			}
		} 
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		map.put("successCount", successCount);
		return map;
	}
	/**
	 * ƴ����˻���-��
	 */
	public RefundOrder checkRefundIsPto(RefundOrder refundOrder,String accNum) throws IllegalAccessException, InvocationTargetException {
		
		List<RefundOrders> refundOrdersList = refundOrder.getRefundOrders();
		List<RefundOrders> refundsList = new ArrayList<>();
		boolean flag = true;
		MisUser misUser = misUserDao.select(accNum);
		refundOrder.setOperator(misUser.getUserName());
		refundOrder.setOperatorId(accNum);
		refundOrder.setOperatorTime(sf.format(new Date()));
		if(refundOrdersList.size() > 0) {
			for(RefundOrders refundOrders : refundOrdersList) {
			
				Map<String,String> map=new HashMap<>();
				BigDecimal allPayMoney = BigDecimal.ZERO;//��ϸ��ʵ������ܺ�
				//System.out.println(aftermarket.getOrderId());
				map.put("ecOrderId", refundOrder.getEcOrderId());
				map.put("goodSku", refundOrders.getGoodSku());
				//�õ��еĶ����ź�skuidȥ��ѯԭ���Ĺ��������Լ�ʵ�����
				List<PlatOrders> platOrders = platOrderDao.selectNumAndPayMoneyByOrderAndSKu(map);
				
				//���ݱ��ض����Ƿ񷢻�״̬�ж��˿���˻��ֿ�
				String reWhsCode="";
				PlatOrder platOrder = platOrderDao.selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId()).get(0);
				if(platOrder.getIsShip()==0) {
					//δ����
					reWhsCode=platOrder.getDeliverWhs();
				}else {
					//�ѷ���
					StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
					reWhsCode=storeRecord.getDefaultRefWhs();//ȡ��Ӧ���̵�Ĭ���˻���
				}
				
				if (platOrders.size()==0) {
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�˿���أ�������"+refundOrder.getEcOrderId()+"δ�ҵ�������ϸ����������ʧ�ܡ�");
					logRecord.setOperatTime(sf.format(new Date()));
					logRecord.setOperatType(12);// 12�˿�����
					logRecord.setTypeName("�˿����");
					logRecord.setOperatOrder(refundOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
				}else {
					
					for (int i = 0; i < platOrders.size(); i++) {
						//ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
						if(!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
							flag=false;
							break;
						}
						allPayMoney=allPayMoney.add(platOrders.get(i).getPayMoney());
						
					}
					if(!flag) {
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������"+refundOrder.getEcOrderId()+"����δƥ�䵽��������ļ�¼����������ʧ�ܡ�");
						logRecord.setOperatTime(sf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(refundOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
					}
				}
				
			
				int allReturnCount=0;
				BigDecimal allReturnMoney=BigDecimal.ZERO;
				for (int i = 0; i < platOrders.size(); i++) {
					//ֱ��ȡ��ǰ����ϸ�����˿
					RefundOrders refundOrders1 = new RefundOrders();
					refundOrders1.setRefId(refundOrders.getRefId());
					refundOrders1.setGoodSku(platOrders.get(i).getGoodSku());//��Ʒsku
					refundOrders1.setEcGoodId(platOrders.get(i).getGoodId());//���õ�����Ʒ����
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
					refundOrders1.setCanRefNum(platOrders.get(i).getCanRefNum());//���ÿ�������
					//�˻�����
					if(refundOrders.getRefNum()>0) {
						//�����˻�����
						int refNum = platOrders.get(i).getInvNum()*refundOrders.getRefNum()/platOrders.get(i).getGoodNum();
						refundOrders1.setRefNum(refNum);
						allReturnCount=allReturnCount+refNum;
					}else {
						refundOrders1.setRefNum(0);
					}
					if(StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
						refundOrders1.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//���ñ�����
					}
					refundOrders1.setIsGift(platOrders.get(i).getIsGift());//�Ƿ���Ʒ��ԭ��������
					refundOrders1.setPrdcDt(platOrders.get(i).getPrdcDt());//������������
					refundOrders1.setInvldtnDt(platOrders.get(i).getInvldtnDt());//����ʧЧ����
					if (i+1<platOrders.size()) {
						//���ý��
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						if (platOrders.get(i).getPayMoney().compareTo(BigDecimal.ZERO)>0) {
							BigDecimal refMoney = (refundOrders.getRefMoney().multiply(platOrders.get(i).getPayMoney()))
									.divide(allPayMoney,2,RoundingMode.HALF_DOWN);//��Ҫ�˵Ľ��*��Ӧʵ��������ʵ��
							refundOrders1.setRefMoney(refMoney);
							allReturnMoney=allReturnMoney.add(refMoney);
						}else {
							//ʵ��������0
							refundOrders1.setCanRefMoney(BigDecimal.ZERO);
							refundOrders1.setRefMoney(BigDecimal.ZERO);
						}
					}else {
						//���һ���ü���
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						refundOrders1.setRefMoney(refundOrders.getRefMoney().subtract(allReturnMoney));
					}
					System.out.println("����:"+platOrders.get(i).getBatchNo()+"�������ڣ�"+platOrders.get(i).getPrdcDt()+"ʧЧ���ڣ�"+platOrders.get(i).getInvldtnDt()+"������������"+invtyDoc.getBaoZhiQiDt());
					allReturnMoney=allReturnMoney.add(refundOrders.getRefMoney());
					refundOrders1.setGoodId(platOrders.get(i).getInvId());//���ô������
					
					refundOrders1.setGoodName(invtyDoc.getInvtyNm());//���ô������
					refundOrders1.setBatchNo(platOrders.get(i).getBatchNo());//�����˻�����
					refundOrders1.setRefWhs(reWhsCode);//�����˻��ֿ����
					refundsList.add(refundOrders1);//װ��list

					refundOrder.setOrderId(platOrders.get(i).getOrderId());
				}
				refundOrder.setRefStatus(0);//�˿�״̬��ʱ�����ó�0,��֪����ɶ��
				refundOrder.setAllRefNum(allReturnCount);
				refundOrder.setAllRefMoney(allReturnMoney);
				if(refundOrder.getAllRefNum()>0) {
					refundOrder.setIsRefund(1);//�˻���������0ʱ�Ƿ��˻�1
				}else {
					refundOrder.setIsRefund(0);
				}
				
			}
		
		}
		refundOrder.setRefundOrders(refundsList);//����list��size����0��˵������ʧ�ܣ�����Ҫ����
		return refundOrder;
	}
	
	/**
	 * �����˻���-��
	 */
	public RefundOrder checkRefundIsPtoSN(RefundOrder refundOrder,String accNum,String returnType) throws IllegalAccessException, InvocationTargetException {
		
		List<RefundOrders> refundOrdersList = refundOrder.getRefundOrders();
		List<RefundOrders> refundsList = new ArrayList<>();
		boolean flag = true;
		MisUser misUser = misUserDao.select(accNum);
		refundOrder.setOperator(misUser.getUserName());
		refundOrder.setOperatorId(accNum);
		refundOrder.setOperatorTime(sf.format(new Date()));
		if(refundOrdersList.size() > 0) {
			for(RefundOrders refundOrders : refundOrdersList) {
			
				Map<String,String> map=new HashMap<>();
				BigDecimal allPayMoney = BigDecimal.ZERO;//��ϸ��ʵ������ܺ�
				//System.out.println(aftermarket.getOrderId());
				map.put("ecOrderId", refundOrder.getEcOrderId());
				map.put("goodId", refundOrders.getEcGoodId());
				//�õ��еĶ����ź�goodIdȥ��ѯԭ���Ĺ��������Լ�ʵ�����
				List<PlatOrders> platOrders = platOrderDao.selectNumAndPayMoneyByOrderAndSKuSN(map);
				
				//�����˿״̬�ж��˿���˻��ֿ�
				String reWhsCode="";
				if(refundOrder.getIsRefund()==1) {
					//���˿�Ƿ��˿�Ϊ1ʱ����Ҫ�˻�
					//������Ϊδǩ������µ��˻�ʱ���˻زֿ�Ϊԭ���������ֿ⣬����ǩ�պ��˻زֿ�Ϊ�������õ�Ĭ���˻���
					if (returnType.equals("4")) {//������
						StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
						reWhsCode = storeRecord.getDefaultRefWhs();//����Ĭ���˻���
					}else {
						PlatOrder platOrder = platOrderDao.selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId()).get(0);
						reWhsCode=platOrder.getDeliverWhs();//ԭ�������ֿ�
					}
				}else {
					//����Ҫ�˻������˿ʱ���˻��ֿ�Ϊԭ�������ֿ�
					PlatOrder platOrder = platOrderDao.selectPlatOrdersByEcOrderId(refundOrder.getEcOrderId()).get(0);
					reWhsCode=platOrder.getDeliverWhs();//ԭ�������ֿ�
				}
				
				
				
				if (platOrders.size()==0) {
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�˿���أ�������"+refundOrder.getEcOrderId()+"δ�ҵ�������ϸ����������ʧ�ܡ�");
					logRecord.setOperatTime(sf.format(new Date()));
					logRecord.setOperatType(12);// 12�˿�����
					logRecord.setTypeName("�˿����");
					logRecord.setOperatOrder(refundOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
				}else {
					
					for (int i = 0; i < platOrders.size(); i++) {
						//ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
						if(!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
							flag=false;
							break;
						}
						allPayMoney=allPayMoney.add(platOrders.get(i).getPayMoney());
						
					}
					if(!flag) {
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������"+refundOrder.getEcOrderId()+"����δƥ�䵽��������ļ�¼����������ʧ�ܡ�");
						logRecord.setOperatTime(sf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(refundOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
					}
				}
				
			
				int allReturnCount=0;
				BigDecimal allReturnMoney=BigDecimal.ZERO;
				for (int i = 0; i < platOrders.size(); i++) {
					//ֱ��ȡ��ǰ����ϸ�����˿
					RefundOrders refundOrders1 = new RefundOrders();
					refundOrders1.setRefId(refundOrders.getRefId());
					refundOrders1.setGoodSku(platOrders.get(i).getGoodSku());//��Ʒsku
					refundOrders1.setEcGoodId(platOrders.get(i).getGoodId());//���õ�����Ʒ����
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
					refundOrders1.setCanRefNum(platOrders.get(i).getCanRefNum());//���ÿ�������
					//�˻�����
					if(refundOrder.getIsRefund()==1) {//���˿��Ҫ�˻�ʱ
						//�����˻�����
						int refNum = platOrders.get(i).getInvNum();
						refundOrders1.setRefNum(refNum);
						allReturnCount=allReturnCount+refNum;
					}else {
						refundOrders1.setRefNum(0);
					}
					if(StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
						refundOrders1.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//���ñ�����
					}
					refundOrders1.setIsGift(platOrders.get(i).getIsGift());//�Ƿ���Ʒ��ԭ��������
					refundOrders1.setPrdcDt(platOrders.get(i).getPrdcDt());//������������
					refundOrders1.setInvldtnDt(platOrders.get(i).getInvldtnDt());//����ʧЧ����
					if (i+1<platOrders.size()) {
						//���ý��
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						if (platOrders.get(i).getPayMoney().compareTo(BigDecimal.ZERO)>0) {
							BigDecimal refMoney = (refundOrders.getRefMoney().multiply(platOrders.get(i).getPayMoney()))
									.divide(allPayMoney,2,RoundingMode.HALF_DOWN);//��Ҫ�˵Ľ��*��Ӧʵ��������ʵ��
							refundOrders1.setRefMoney(refMoney);
							allReturnMoney=allReturnMoney.add(refMoney);
						}else {
							//ʵ��������0
							refundOrders1.setCanRefMoney(BigDecimal.ZERO);
							refundOrders1.setRefMoney(BigDecimal.ZERO);
						}
					}else {
						//���һ���ü���
						refundOrders1.setCanRefMoney(platOrders.get(i).getCanRefMoney());
						refundOrders1.setRefMoney(refundOrders.getRefMoney().subtract(allReturnMoney));
					}
					//System.out.println("����:"+platOrders.get(i).getBatchNo()+"�������ڣ�"+platOrders.get(i).getPrdcDt()+"ʧЧ���ڣ�"+platOrders.get(i).getInvldtnDt()+"������������"+invtyDoc.getBaoZhiQiDt());
					allReturnMoney=allReturnMoney.add(refundOrders.getRefMoney());
					refundOrders1.setGoodId(platOrders.get(i).getInvId());//���ô������
					
					refundOrders1.setGoodName(invtyDoc.getInvtyNm());//���ô������
					refundOrders1.setBatchNo(platOrders.get(i).getBatchNo());//�����˻�����
					refundOrders1.setRefWhs(reWhsCode);//�����˻��ֿ����
					refundsList.add(refundOrders1);//װ��list

					refundOrder.setOrderId(platOrders.get(i).getOrderId());
				}
				refundOrder.setRefStatus(0);//�˿�״̬��ʱ�����ó�0,��֪����ɶ��
				refundOrder.setAllRefNum(allReturnCount);
				refundOrder.setAllRefMoney(allReturnMoney);
				if(refundOrder.getAllRefNum()>0) {
					refundOrder.setIsRefund(1);//�˻���������0ʱ�Ƿ��˻�1
				}else {
					refundOrder.setIsRefund(0);
				}
				
			}
		
		}
		refundOrder.setRefundOrders(refundsList);//����list��size����0��˵������ʧ�ܣ�����Ҫ����
		return refundOrder;
	}
	/**
	 * �����˻������˽��,����
	 * @param refundOrder
	 * @return
	 */
	private RefundOrder setCanRefValue(RefundOrder refundOrder) {
		List<RefundOrders> refundOrdersList = refundOrder.getRefundOrders();
		List<RefundOrders> refundsList = new ArrayList<>();
		if(refundOrdersList.size() > 0) {
			//��ѯԭ��
			List<PlatOrders> platOrdersList = platOrdersDao.selectByEcOrderId(refundOrder.getEcGoodId());
			if(platOrdersList.size() > 0) {
				Map<String,Object> maps = new HashMap<>();
				for(PlatOrders platOrders : platOrdersList) {
					maps.put(platOrders.getInvId(),platOrders);
				}
				for(RefundOrders refundOrders : refundOrdersList) {
					if(maps.containsKey(refundOrders.getGoodId())) {
						PlatOrders platOrders = (PlatOrders)maps.get(refundOrders.getGoodId());
						refundOrders.setCanRefMoney(platOrders.getPayMoney());
						refundOrders.setCanRefNum(platOrders.getGoodNum());
						refundsList.add(refundOrders);
					}
				}
				refundOrder.setRefundOrders(refundsList);
			} 
		}
		return refundOrder;
	}


	/**
	 * ���������ȡ-����
	 * @return
	 * @throws CloneNotSupportedException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Transactional
	private Map<String,Object> getPlatOrder(List<OrderQuery> orderQueryList,
										 String storeId,String userId,
										 StoreRecord storeRecord) throws CloneNotSupportedException, IOException, ParseException {

		boolean isSuccess = true;
		String resp = "";
		Map<String,Object> map = new HashMap<>();
		DecimalFormat dFormat = new DecimalFormat("0.00");
		int downloadCount = 0;
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		if(orderQueryList.size() > 0) {
			for(OrderQuery query : orderQueryList) {
				System.out.println(query.getOrderTotalStatus());
				if(query.getOrderTotalStatus().equals("10")||query.getOrderTotalStatus().equals("15")||query.getOrderTotalStatus().equals("20")||
						query.getOrderTotalStatus().equals("30")||query.getOrderTotalStatus().contentEquals("21")) {
					//������״̬��0 ����ȡ����5 δ���10 ����Ѹ��15 �����У�20 �ѷ�����21 ���ַ�����30 ���׳ɹ���40 ���׹ر�
				
				
				
				if (platOrderDao.checkExsits(query.getOrderCode(), storeId) == 0) {
					
					PlatOrder order = new PlatOrder();
					List<PlatOrders> ordersList = new ArrayList<>(); //��Ʒ				
					List<InvoiceInfo> invoiceList = new ArrayList<>(); //��Ʊ
					List<CouponDetail> couponsList = new ArrayList<>(); //�Ż�			
					List<PlatOrderPaymethod> payMentList = new ArrayList<>(); //֧��
					BigDecimal goodMoney = new BigDecimal(0.00);
					BigDecimal discountMoney = new BigDecimal(0.00);
					BigDecimal adjustMoney = new BigDecimal(0.00);
					
					String orderId  = getOrderNo.getSeqNo("ec", userId);
					order.setDownloadTime(simpleDateFormat.format(new Date()));
					order.setOrderId(orderId); //px-�������
					order.setStoreId(storeId);
					order.setStoreName(storeRecord.getStoreName());
					
					//query.getAccount(); //�����˺�
					//query.getBank();//������
					order.setBuyerNote(query.getBuyerOrdRemark());//������ע -���
					order.setCityId(query.getCityCode());//���д���
					order.setCity(query.getCityName());//��
					order.setRecAddress(query.getCustomerAddress());//��ϸ��ַ
					
					//query.getCustomerName();//�˿�����
					//order.setIsShip(0);//�Ƿ񷢻�Ϊ0
					order.setHasGift(0);//�Ƿ���Ʒ
					order.setAdjustStatus(0);//����״̬����Ϊ0
					order.setTradeDt(query.getOrderSaleTime());//�µ�ʱ�� �µ�ʱ����֧��ʱ�� �ӿ�û�ҵ��µ�ʱ��
					order.setBizTypId("2");
					order.setSellTypId("1");
					order.setRecvSendCateId("6");
					order.setCountyId(query.getDistrictCode());//�������
					order.setCounty(query.getDistrictName());//��
					
					//query.getEvaluationMark();//����״̬ 0δ���� 1������
					
					//��ȡ��Ʊ��Ϣ					
					if(query.getNeedinvoiceflag().equals("Y")) {
						
						invoiceList = getInvoice(query,storeId,orderId,invoiceList,order);
		
					} else {
						order.setIsInvoice(0);
					}
					
				
					//query.getInvoiceRecipientAddress();//��Ʊ�ռ��˵�ַ					
					//query.getInvoiceRecipientPhone(); //��Ʊ�ռ��˵绰
					//query.getRegisterAddress(); //ע���ַ
					//query.getRegisterPhone(); //ע��绰
					
					//query.getMobNum(); //�����ϵ�绰
				
					order.setEcOrderId(query.getOrderCode()); //B2C������
					
					order.setPayTime(query.getOrderSaleTime()); //����֧��ʱ��,��ʽ��yyyy-MM-dd HH:mm:ss
					//order.setOrderStatus(Integer.valueOf(query.getOrderTotalStatus())); //������״̬
					//order.setOrderStatus(0);//����״̬
					
					//query.getPayType(); //֧����ʽ������Ʒ��֧��
					
					order.setProvinceId(query.getProvinceCode()); //ʡ�ݴ���
					order.setProvince(query.getProvinceName()); //�ռ��˵�ַ��ʡ������
					
					order.setSellerNote(query.getSellerOrdRemark()); //������ע�����ң�--�̼ҶԶ����ı�ע��Ϣ
					order.setBuyerId(query.getUserName()); //����ʺ�
					order.setSellTypId("1");// ��������������ͨ����
					order.setBizTypId("2");// ����ҵ������2c	
					order.setRecName(query.getCustomerName());//�ջ�������
					order.setOrderStatus(0); //����״̬
					if (query.getOrderTotalStatus().equals("10")) {//δ����
						order.setIsShip(0);
					}else {
						order.setIsShip(1);
						String expressNo = "";
						for (int i = 0; i < query.getOrderDetail().size(); i++) {
							if(query.getOrderDetail().get(i).getExpressno().length()>0) {
								expressNo=query.getOrderDetail().get(i).getExpressno();
								break;
							}
							
						}
						order.setExpressNo(expressNo);//��ݵ���
					}
					order.setReturnStatus(0); //�˻�״̬
					order.setIsClose(0); // �Ƿ�ر�
					order.setIsAudit(0); // �Ƿ����
					order.setVenderId("SN"); // '�̼�id'
					order.setRecMobile(query.getMobNum());//�绰
					List<OrderDetail> orderDetailList = query.getOrderDetail();
					int goodNum = 0;
					if(orderDetailList.size() > 0) {
						for(OrderDetail detail : orderDetailList) {
							PlatOrders orders = new PlatOrders();
							orders.setOrderId(orderId);
							orders.setEcOrderId(order.getEcOrderId());
		
							
							//detail.getActivitytype(); //����ͣ�02������ר������03��������ö�����04���������ö�����05,06��Ԥ��������07����ɱ������08��S��������09����Ʒ��
							//detail.getbLineNumber(); //b2c����Ŀ��
							//detail.getCarShopAddr(); //	���������ŵ��ַ
							//detail.getCarShopCode(); //���������ŵ����
							//detail.getCarShopName(); //���������ŵ�����
							//detail.getCarShopSerWay(); //��������ʽ��1-���ŷ���2-�������
							//detail.getCarShopTel(); //���������ŵ�绰
							
							//detail.getDeclareGoodsAmount(); //�걨�����л���
							//detail.getDeclareItemPrice(); //�걨����
							//detail.getDeclareItemTaxfare(); //�걨˰��
							
							//detail.getDisType(); //������ʽ��01 ������ֱ�ʷ�����02�����̼ұ�˰��������03����������˰������;�մ�����ں��⹺
			
							
							//detail.getExpresscompanycode(); //������˾����
							//detail.getExpressno(); //�˵���
							
							//4PS������ʾ,1����4PS������0����C�귢��
							if(Integer.valueOf(detail.getFpsdeliveryflag()) == 0) {
								order.setDeliverSelf(1); //�Է���
							}else{
								order.setDeliverSelf(0);
							}; 
							
							//detail.getHwgFlag(); //���⹺��ʶ��01��ʾ���⹺����������ֵΪ�Ǻ��⹺����
							
							orders.setDeliverWhs(detail.getInvCode()); //�ֿ����
							
							//detail.getIsProsupplierDelivery(); //�Ƿ�Ӧ�̷�����ʶ��1������
							
							//detail.getItemCode(); //�̼���Ʒ����
							orders.setGoodId(detail.getProductCode()); // ƽ̨��Ʒ��� ����ƽ̨����
							
							//detail.getItemTaxFare(); //˰��
							//detail.getMode(); //ͨ��ģʽ(01:��˰����,02:����ֱ��,03:�����걨,04:�ʹ�)
							//detail.getOrderchannel(); //����������PC���ֻ�MOBILE��WAP�� �绰PHONE
							//detail.getOrderLineNumber(); //��������Ŀ��
							//detail.getOrderLineStatus(); //��������Ŀ״̬��10=��������20=�ѷ�����30=���׳ɹ�
							//detail.getPackageorderid(); //������
							
							orders.setPayMoney(new BigDecimal(detail.getPayAmount())); //�������������Ŀ���
							
							//detail.getPayerCustomerName(); //֧��������
							//detail.getPayerIdNumber(); //֧�������֤����
							
							//detail.getPayorderid(); //֧������
							
							//detail.getPhoneIdentifyCode(); //�ֻ�����
							//detail.getPrmtcode(); //�ƹ㷽ʽ��ȡֵΪ01ʱ����ʾ����
							
							//detail.getProductCode(); //������Ʒ����
							orders.setGoodName(detail.getProductName()); //��Ʒ��������
							
							//detail.getReceivezipCode(); //�ջ�����������
							//detail.getReservebalanceamount(); //	Ԥ��β����
							//detail.getReservedepositamount();//Ԥ��������
							//detail.getReservestatus(); //Ԥ��״̬��M��������֧����P�������ѷ�û��R���������˻���
							//detail.getReturnOrderFlag(); //�˻�������ʾλ 0��ʾ��������, 1��ʾ�˻�����
							
							orders.setGoodNum(dFormat.parse(detail.getSaleNum()).intValue()); //����
							goodNum=goodNum+orders.getGoodNum();
							order.setFreightPrice(new BigDecimal(detail.getTransportFee())); //�˷ѡ���������Ŀ��Ӧ���˷�
							
							orders.setGoodPrice(new BigDecimal(detail.getUnitPrice())); //��Ʒ����
							
							
							
							//orders.setDiscountMoney(new BigDecimal(detail.getVouchertotalMoney())); //�Żݵ����
							
							//detail.getServiceItemFlag(); //0�Ƿ����� ,1������
							//detail.getOtoOrderType(); //O2O��������
							//detail.getDepositmoney();//Ѻ��
							//detail.getRentperiod();//����
							//detail.getRenttype(); //��������
							
							order.setBizTypId(detail.getOrderServiceTypeMulti()); //��ϵĶ���ҵ�����Ͷ��Ÿ���
							
							List<CouponList> couponList = detail.getCouponList(); //
							BigDecimal pDiscountMoney = new BigDecimal(0.00);
							//BigDecimal pAdjustMoney = new BigDecimal(0.00);
							
							if(couponList.size() > 0) {
								
								for(CouponList coupon : couponList) {
									//coupon.getCoupontype(); //ȯ���͡�ȯ���ͱ�ʾ��ȯ�����ֵ����ͣ�5998-�����Ż�ȯ��6998-����0Ԫ��ȯ��7998-0Ԫ��ȯ��8012-���ֵ��֡�9994-�Ż�ȯ��9995-�Ż�ȯ��
									//coupon.getSharelimit(); //���۵��ݷ�̯��������ȯ�������ܽ����ÿ������Ŀ�����۵��ݷ�̯����ۼơ�
									CouponDetail coup = new CouponDetail();
									coup.setPlatId("SN");
									coup.setStoreId(storeId);
									coup.setOrderId(query.getOrderCode()); //ƽ̨������
									coup.setCouponCode(coupon.getCoupontype());
									if(Integer.valueOf(coupon.getCoupontype()) == 5998) {
										coup.setCouponType("�����Ż�ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 6998) {
										coup.setCouponType("����0Ԫ��ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 7998) {
										coup.setCouponType("0Ԫ��ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 8012) {
										coup.setCouponType("���ֵ���");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9994) {
										coup.setCouponType("�Ż�ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9995) {
										coup.setCouponType("�Ż�ȯ");
									} else {
										coup.setCouponType("δ֪�Ż�ȯ����");
									}
									
									coup.setCouponPrice(new BigDecimal(coupon.getSharelimit()));
									
									couponsList.add(coup);
								} 
							}
							
							List<PaymentList> paymentList = detail.getPaymentList(); //֧����ϸ
							
							if(paymentList.size() > 0) {
								for(PaymentList payment : paymentList) {
									if(StringUtils.isNotEmpty(payment.getBanktypecode())) {
										if(Integer.valueOf(payment.getBanktypecode()) == 10006 || Integer.valueOf(payment.getBanktypecode()) == 10003) {
											PlatOrderPaymethod pay = new PlatOrderPaymethod();
											pay.setPayStatus("֧���ɹ�");
											pay.setOrderId(orderId); 
											pay.setPlatId("SN");
											pay.setStoreId(storeId);
											
											pay.setPayWay(payment.getPaycode());
											pay.setPaymoneyTime(order.getPayTime());
											pay.setPayMoney(new BigDecimal(payment.getPayamount()));
											
											//֧����ʽ���롣��ʾ�Զ���ʹ�õ�ȯ�û������ͣ�5998-�����Ż�ȯ��6998-����0Ԫ��ȯ��7998-0Ԫ��ȯ��8012-���ֵ��֡�9994-�Ż�ȯ��9995-�Ż�ȯ��10001-��ȯ��10002-��Ʒ����ȯ��10003-������ȯ��10004-��ȯ��10005-��Ʒ����ȯ��10006-������ȯ��10009-�޵�ȯ��
											pay.setBanktypecode(payment.getBanktypecode()); //10006,10003
											 //����֧����1��������֧������ֵ
											pay.setOffLinePayFlag(payment.getOffLinePayFlag());
											
											//֧����ʽ��̯������ʹ�Ä��ͻ����ܽ����ÿ������Ŀ��֧����ʽ��̯����ۼơ�
											//payment.getPayamount(); 
											//���ʽ��4148-�׸���֧����5002-��Ʒ��֧����9003-�ŵ긶�6901-ȯ������֧����6903-�׸����Ż�ȯ֧����2037-���MIS���п����£���1001-�ֽ� (CASH)��2044-����MIS���п����£���6906-��Ʒ֧����4237-֧����֧����4245-΢��֧����6915-ͭ��֧����
											//payment.getPaycode();
											//�̼ҵĳе�����
											pay.setMerchantPercent(payment.getMerchantPercent());
											//ƽ̨�ĳе�����
											pay.setPlatformPercent(payment.getPlatformPercent());
											
											pDiscountMoney = pDiscountMoney.add(new BigDecimal(payment.getPayamount()));
											
											payMentList.add(pay);
										} 
									} 
											
								}
							}
							goodMoney = goodMoney.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));
							discountMoney = discountMoney.add(pDiscountMoney);
							
							
							orders.setDiscountMoney(pDiscountMoney); //ϵͳ�Żݽ�� --ϵͳ
							orders.setAdjustMoney(BigDecimal.ZERO); //�Ż݄���� ---����
							ordersList.add(orders);
							
						}
						order.setGoodMoney(goodMoney); //Ӧ�����
						order.setDiscountMoney(discountMoney);
						order.setAdjustMoney(BigDecimal.ZERO);			
						order.setPayMoney(goodMoney.subtract(discountMoney)); //֧����֧����ʽ��֧�����
						order.setOrderSellerPrice(order.getPayMoney());//������
						order.setGoodNum(goodNum);//sku��������
						order.setPlatOrdersList(ordersList);
						//���㰴�ձ���ƽ������
						ordersList = platOrderPdd.countAveragePrice(ordersList, order.getPayMoney());
						//����Ƿ�����
						//ordersList = platOrderPdd.checkPlatOrdersList(order,ordersList,order.getPayMoney());
						
						
						int c = 0;
						if(couponsList.size() > 0) {
							c = couponDetailDao.insert(couponsList);
						}
						int p = 0;
						if(payMentList.size() > 0) {
							p = paymethodDao.insert(payMentList);
							
						}
						int po = 0;
						if(order != null) {
							downloadCount++;
							po++;
							platOrderDao.insert(order);
							
						}
						int  pos = 0;
						if(ordersList.size() > 0) {
							pos = platOrdersDao.insert(ordersList);
						}			
						if(po > 0 && pos > 0) {
							/*
							 * // ����������ɺ��ж��Ƿ��Զ����� PlatOrder platorder =
							 * platOrderDao.selectByEcOrderId(order.getEcOrderId()); List<PlatOrders>
							 * orderslist = platOrdersDao.select(platorder.getOrderId()); if
							 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
							 * ����trueʱ���˶���ͨ������ֱ�ӽ������
							 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
							 * // ����Ĭ�ϲ���Աsys }
							 * 
							 * message = "���سɹ�"; logger.info("��������:����ƽ̨�������سɹ�");
							 */
							//ִ���Զ�ƥ��
							platOrderService.autoMatch(orderId, userId);
							isSuccess = true;
						}
					}
				}
				
				/*else {
					message = "��������:����ƽ̨�����Ѵ���,������:"+query.getOrderCode();
					isSuccess = false;
					//�����Ѵ���
					logger.error("��������:����ƽ̨�����Ѵ���,������:{}",query.getOrderCode());
					
				}*/
				}
			}
			
		}  else {
			
			message = "��������:����ƽ̨���ض�������Ϊ��";
			isSuccess = false;
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
		if(isSuccess) {
			message = "�����Զ����ص��̣� "+ storeSettings.getStoreName() + "����" + downloadCount + "��";
		} 
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		return map;
	}
	
	
	/**
	 * ���������ȡ-����
	 * @return
	 * @throws CloneNotSupportedException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@Transactional
	private Map<String,Object> getPlatOrder(OrderGet query,
										 String storeId,String userId,
										 StoreRecord storeRecord) throws CloneNotSupportedException, IOException, ParseException {

		boolean isSuccess = true;
		String resp = "";
		Map<String,Object> map = new HashMap<>();
		DecimalFormat dFormat = new DecimalFormat("0.00");
		int downloadCount = 0;
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		if(query!=null) {
				System.out.println(query.getOrderTotalStatus());
				if(query.getOrderTotalStatus().equals("10")||query.getOrderTotalStatus().equals("15")||query.getOrderTotalStatus().equals("20")||
						query.getOrderTotalStatus().equals("30")||query.getOrderTotalStatus().contentEquals("21")) {
					//������״̬��0 ����ȡ����5 δ���10 ����Ѹ��15 �����У�20 �ѷ�����21 ���ַ�����30 ���׳ɹ���40 ���׹ر�
				
				
				
				if (platOrderDao.checkExsits(query.getOrderCode(), storeId) == 0) {
					
					PlatOrder order = new PlatOrder();
					List<PlatOrders> ordersList = new ArrayList<>(); //��Ʒ				
					List<InvoiceInfo> invoiceList = new ArrayList<>(); //��Ʊ
					List<CouponDetail> couponsList = new ArrayList<>(); //�Ż�			
					List<PlatOrderPaymethod> payMentList = new ArrayList<>(); //֧��
					BigDecimal goodMoney = new BigDecimal(0.00);
					BigDecimal discountMoney = new BigDecimal(0.00);
					BigDecimal adjustMoney = new BigDecimal(0.00);
					
					String orderId  = getOrderNo.getSeqNo("ec", userId);
					order.setDownloadTime(simpleDateFormat.format(new Date()));
					order.setOrderId(orderId); //px-�������
					order.setStoreId(storeId);
					order.setStoreName(storeRecord.getStoreName());
					
					//query.getAccount(); //�����˺�
					//query.getBank();//������
					order.setBuyerNote(query.getBuyerOrdRemark());//������ע -���
					order.setCityId(query.getCityCode());//���д���
					order.setCity(query.getCityName());//��
					order.setRecAddress(query.getCustomerAddress());//��ϸ��ַ
					
					//query.getCustomerName();//�˿�����
					//order.setIsShip(0);//�Ƿ񷢻�Ϊ0
					order.setHasGift(0);//�Ƿ���Ʒ
					order.setAdjustStatus(0);//����״̬����Ϊ0
					order.setTradeDt(query.getOrderSaleTime());//�µ�ʱ�� �µ�ʱ����֧��ʱ�� �ӿ�û�ҵ��µ�ʱ��
					order.setBizTypId("2");
					order.setSellTypId("1");
					order.setRecvSendCateId("6");
					order.setCountyId(query.getDistrictCode());//�������
					order.setCounty(query.getDistrictName());//��
					
					//query.getEvaluationMark();//����״̬ 0δ���� 1������
					
					//��ȡ��Ʊ��Ϣ					
					if(query.getNeedinvoiceflag().equals("Y")) {
						
						invoiceList = getInvoice(query,storeId,orderId,invoiceList,order);
		
					} else {
						order.setIsInvoice(0);
					}
					
				
					//query.getInvoiceRecipientAddress();//��Ʊ�ռ��˵�ַ					
					//query.getInvoiceRecipientPhone(); //��Ʊ�ռ��˵绰
					//query.getRegisterAddress(); //ע���ַ
					//query.getRegisterPhone(); //ע��绰
					
					//query.getMobNum(); //�����ϵ�绰
				
					order.setEcOrderId(query.getOrderCode()); //B2C������
					
					order.setPayTime(query.getOrderSaleTime()); //����֧��ʱ��,��ʽ��yyyy-MM-dd HH:mm:ss
					//order.setOrderStatus(Integer.valueOf(query.getOrderTotalStatus())); //������״̬
					//order.setOrderStatus(0);//����״̬
					
					//query.getPayType(); //֧����ʽ������Ʒ��֧��
					
					order.setProvinceId(query.getProvinceCode()); //ʡ�ݴ���
					order.setProvince(query.getProvinceName()); //�ռ��˵�ַ��ʡ������
					
					order.setSellerNote(query.getSellerOrdRemark()); //������ע�����ң�--�̼ҶԶ����ı�ע��Ϣ
					order.setBuyerId(query.getUserName()); //����ʺ�
					order.setSellTypId("1");// ��������������ͨ����
					order.setBizTypId("2");// ����ҵ������2c	
					order.setRecName(query.getCustomerName());//�ջ�������
					order.setOrderStatus(0); //����״̬
					if (query.getOrderTotalStatus().equals("10")) {//δ����
						order.setIsShip(0);
					}else {
						order.setIsShip(1);
						String expressNo = "";
						for (int i = 0; i < query.getOrderDetail().size(); i++) {
							if(query.getOrderDetail().get(i).getExpressno().length()>0) {
								expressNo=query.getOrderDetail().get(i).getExpressno();
								break;
							}
							
						}
						order.setExpressNo(expressNo);//��ݵ���
					}
					order.setReturnStatus(0); //�˻�״̬
					order.setIsClose(0); // �Ƿ�ر�
					order.setIsAudit(0); // �Ƿ����
					order.setVenderId("SN"); // '�̼�id'
					order.setRecMobile(query.getMobNum());//�绰
					List<com.suning.api.entity.custom.orderGetGetResponse.OrderDetail> orderDetailList = query.getOrderDetail();
					
					if(orderDetailList.size() > 0) {
						for(com.suning.api.entity.custom.orderGetGetResponse.OrderDetail detail : orderDetailList) {
							PlatOrders orders = new PlatOrders();
							orders.setOrderId(orderId);
							orders.setEcOrderId(order.getEcOrderId());
		
							
							//detail.getActivitytype(); //����ͣ�02������ר������03��������ö�����04���������ö�����05,06��Ԥ��������07����ɱ������08��S��������09����Ʒ��
							//detail.getbLineNumber(); //b2c����Ŀ��
							//detail.getCarShopAddr(); //	���������ŵ��ַ
							//detail.getCarShopCode(); //���������ŵ����
							//detail.getCarShopName(); //���������ŵ�����
							//detail.getCarShopSerWay(); //��������ʽ��1-���ŷ���2-�������
							//detail.getCarShopTel(); //���������ŵ�绰
							
							//detail.getDeclareGoodsAmount(); //�걨�����л���
							//detail.getDeclareItemPrice(); //�걨����
							//detail.getDeclareItemTaxfare(); //�걨˰��
							
							//detail.getDisType(); //������ʽ��01 ������ֱ�ʷ�����02�����̼ұ�˰��������03����������˰������;�մ�����ں��⹺
			
							
							//detail.getExpresscompanycode(); //������˾����
							//detail.getExpressno(); //�˵���
							
							//4PS������ʾ,1����4PS������0����C�귢��
							if(Integer.valueOf(detail.getFpsdeliveryflag()) == 0) {
								order.setDeliverSelf(1); //�Է���
							}else{
								order.setDeliverSelf(0);
							}; 
							
							//detail.getHwgFlag(); //���⹺��ʶ��01��ʾ���⹺����������ֵΪ�Ǻ��⹺����
							
							orders.setDeliverWhs(detail.getInvCode()); //�ֿ����
							
							//detail.getIsProsupplierDelivery(); //�Ƿ�Ӧ�̷�����ʶ��1������
							
							//detail.getItemCode(); //�̼���Ʒ����
							orders.setGoodId(detail.getProductCode()); // ƽ̨��Ʒ��� ����ƽ̨����
							
							//detail.getItemTaxFare(); //˰��
							//detail.getMode(); //ͨ��ģʽ(01:��˰����,02:����ֱ��,03:�����걨,04:�ʹ�)
							//detail.getOrderchannel(); //����������PC���ֻ�MOBILE��WAP�� �绰PHONE
							//detail.getOrderLineNumber(); //��������Ŀ��
							//detail.getOrderLineStatus(); //��������Ŀ״̬��10=��������20=�ѷ�����30=���׳ɹ�
							//detail.getPackageorderid(); //������
							
							orders.setPayMoney(new BigDecimal(detail.getPayAmount())); //�������������Ŀ���
							
							//detail.getPayerCustomerName(); //֧��������
							//detail.getPayerIdNumber(); //֧�������֤����
							
							//detail.getPayorderid(); //֧������
							
							//detail.getPhoneIdentifyCode(); //�ֻ�����
							//detail.getPrmtcode(); //�ƹ㷽ʽ��ȡֵΪ01ʱ����ʾ����
							
							//detail.getProductCode(); //������Ʒ����
							orders.setGoodName(detail.getProductName()); //��Ʒ��������
							
							//detail.getReceivezipCode(); //�ջ�����������
							//detail.getReservebalanceamount(); //	Ԥ��β����
							//detail.getReservedepositamount();//Ԥ��������
							//detail.getReservestatus(); //Ԥ��״̬��M��������֧����P�������ѷ�û��R���������˻���
							//detail.getReturnOrderFlag(); //�˻�������ʾλ 0��ʾ��������, 1��ʾ�˻�����
							
							orders.setGoodNum(dFormat.parse(detail.getSaleNum()).intValue()); //����
							
							order.setFreightPrice(new BigDecimal(detail.getTransportFee())); //�˷ѡ���������Ŀ��Ӧ���˷�
							
							orders.setGoodPrice(new BigDecimal(detail.getUnitPrice())); //��Ʒ����
							
							
							
							//orders.setDiscountMoney(new BigDecimal(detail.getVouchertotalMoney())); //�Żݵ����
							
							//detail.getServiceItemFlag(); //0�Ƿ����� ,1������
							//detail.getOtoOrderType(); //O2O��������
							//detail.getDepositmoney();//Ѻ��
							//detail.getRentperiod();//����
							//detail.getRenttype(); //��������
							
							order.setBizTypId(detail.getOrderServiceTypeMulti()); //��ϵĶ���ҵ�����Ͷ��Ÿ���
							
							List<com.suning.api.entity.custom.orderGetGetResponse.CouponList> couponList = detail.getCouponList(); //
							BigDecimal pDiscountMoney = new BigDecimal(0.00);
							//BigDecimal pAdjustMoney = new BigDecimal(0.00);
							
							if(couponList.size() > 0) {
								
								for(com.suning.api.entity.custom.orderGetGetResponse.CouponList coupon : couponList) {
									//coupon.getCoupontype(); //ȯ���͡�ȯ���ͱ�ʾ��ȯ�����ֵ����ͣ�5998-�����Ż�ȯ��6998-����0Ԫ��ȯ��7998-0Ԫ��ȯ��8012-���ֵ��֡�9994-�Ż�ȯ��9995-�Ż�ȯ��
									//coupon.getSharelimit(); //���۵��ݷ�̯��������ȯ�������ܽ����ÿ������Ŀ�����۵��ݷ�̯����ۼơ�
									CouponDetail coup = new CouponDetail();
									coup.setPlatId("SN");
									coup.setStoreId(storeId);
									coup.setOrderId(query.getOrderCode()); //ƽ̨������
									coup.setCouponCode(coupon.getCoupontype());
									if(Integer.valueOf(coupon.getCoupontype()) == 5998) {
										coup.setCouponType("�����Ż�ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 6998) {
										coup.setCouponType("����0Ԫ��ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 7998) {
										coup.setCouponType("0Ԫ��ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 8012) {
										coup.setCouponType("���ֵ���");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9994) {
										coup.setCouponType("�Ż�ȯ");
									} else if(Integer.valueOf(coupon.getCoupontype()) == 9995) {
										coup.setCouponType("�Ż�ȯ");
									} else {
										coup.setCouponType("δ֪�Ż�ȯ����");
									}
									
									coup.setCouponPrice(new BigDecimal(coupon.getSharelimit()));
									
									couponsList.add(coup);
								} 
							}
							
							List<com.suning.api.entity.custom.orderGetGetResponse.PaymentList> paymentList = detail.getPaymentList(); //֧����ϸ
							
							if(paymentList.size() > 0) {
								for(com.suning.api.entity.custom.orderGetGetResponse.PaymentList payment : paymentList) {
									if(StringUtils.isNotEmpty(payment.getBanktypecode())) {
										if(Integer.valueOf(payment.getBanktypecode()) == 10006 || Integer.valueOf(payment.getBanktypecode()) == 10003) {
											PlatOrderPaymethod pay = new PlatOrderPaymethod();
											pay.setPayStatus("֧���ɹ�");
											pay.setOrderId(orderId); 
											pay.setPlatId("SN");
											pay.setStoreId(storeId);
											
											pay.setPayWay(payment.getPaycode());
											pay.setPaymoneyTime(order.getPayTime());
											pay.setPayMoney(new BigDecimal(payment.getPayamount()));
											
											//֧����ʽ���롣��ʾ�Զ���ʹ�õ�ȯ�û������ͣ�5998-�����Ż�ȯ��6998-����0Ԫ��ȯ��7998-0Ԫ��ȯ��8012-���ֵ��֡�9994-�Ż�ȯ��9995-�Ż�ȯ��10001-��ȯ��10002-��Ʒ����ȯ��10003-������ȯ��10004-��ȯ��10005-��Ʒ����ȯ��10006-������ȯ��10009-�޵�ȯ��
											pay.setBanktypecode(payment.getBanktypecode()); //10006,10003
											 //����֧����1��������֧������ֵ
											pay.setOffLinePayFlag(payment.getOffLinePayFlag());
											
											//֧����ʽ��̯������ʹ�Ä��ͻ����ܽ����ÿ������Ŀ��֧����ʽ��̯����ۼơ�
											//payment.getPayamount(); 
											//���ʽ��4148-�׸���֧����5002-��Ʒ��֧����9003-�ŵ긶�6901-ȯ������֧����6903-�׸����Ż�ȯ֧����2037-���MIS���п����£���1001-�ֽ� (CASH)��2044-����MIS���п����£���6906-��Ʒ֧����4237-֧����֧����4245-΢��֧����6915-ͭ��֧����
											//payment.getPaycode();
											//�̼ҵĳе�����
											pay.setMerchantPercent(payment.getMerchantPercent());
											//ƽ̨�ĳе�����
											pay.setPlatformPercent(payment.getPlatformPercent());
											
											pDiscountMoney = pDiscountMoney.add(new BigDecimal(payment.getPayamount()));
											
											payMentList.add(pay);
										} 
									} 
											
								}
							}
							goodMoney = goodMoney.add(orders.getGoodPrice().multiply(new BigDecimal(orders.getGoodNum())));
							discountMoney = discountMoney.add(pDiscountMoney);
							
							
							orders.setDiscountMoney(pDiscountMoney); //ϵͳ�Żݽ�� --ϵͳ
							orders.setAdjustMoney(BigDecimal.ZERO); //�Ż݄���� ---����
							ordersList.add(orders);
							
						}
						order.setGoodMoney(goodMoney); //Ӧ�����
						order.setDiscountMoney(discountMoney);
						order.setAdjustMoney(BigDecimal.ZERO);			
						order.setPayMoney(goodMoney.subtract(discountMoney)); //֧����֧����ʽ��֧�����
						
						order.setPlatOrdersList(ordersList);
						//���㰴�ձ���ƽ������
						ordersList = platOrderPdd.countAveragePrice(ordersList, order.getPayMoney());
						//����Ƿ�����
						//ordersList = platOrderPdd.checkPlatOrdersList(order,ordersList,order.getPayMoney());
						
						
						int c = 0;
						if(couponsList.size() > 0) {
							c = couponDetailDao.insert(couponsList);
						}
						int p = 0;
						if(payMentList.size() > 0) {
							p = paymethodDao.insert(payMentList);
							
						}
						int po = 0;
						if(order != null) {
							downloadCount++;
							po++;
							platOrderDao.insert(order);
							
						}
						int  pos = 0;
						if(ordersList.size() > 0) {
							pos = platOrdersDao.insert(ordersList);
						}			
						if(po > 0 && pos > 0) {
							/*
							 * // ����������ɺ��ж��Ƿ��Զ����� PlatOrder platorder =
							 * platOrderDao.selectByEcOrderId(order.getEcOrderId()); List<PlatOrders>
							 * orderslist = platOrdersDao.select(platorder.getOrderId()); if
							 * (auditStrategyService.autoAuditCheck(platorder, orderslist)) { //
							 * ����trueʱ���˶���ͨ������ֱ�ӽ������
							 * associatedSearchService.orderAuditByOrderId(platorder.getOrderId(), "sys");
							 * // ����Ĭ�ϲ���Աsys }
							 * 
							 * message = "���سɹ�"; logger.info("��������:����ƽ̨�������سɹ�");
							 */
							//ִ���Զ�ƥ��
							platOrderService.autoMatch(orderId, userId);
							isSuccess = true;
						}
					}
				}
				
				/*else {
					message = "��������:����ƽ̨�����Ѵ���,������:"+query.getOrderCode();
					isSuccess = false;
					//�����Ѵ���
					logger.error("��������:����ƽ̨�����Ѵ���,������:{}",query.getOrderCode());
					
				}*/
				}
			
		}  else {
			
			message = "��������:����ƽ̨���ض�������Ϊ��";
			isSuccess = false;
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
		if(isSuccess) {
			message = "�����Զ����ص��̣� "+ storeSettings.getStoreName() + "����" + downloadCount + "��";
		} 
		map.put("message", message);
		map.put("isSuccess", isSuccess);
		return map;
	}
	
	/**
	 * post��ȡ��Ʊ��Ϣ
	 */
	private List<InvoiceInfo> getInvoice(OrderQuery query,String storeId,
										 String orderId,List<InvoiceInfo> invoiceList,PlatOrder order) {
		InvoiceInfo info = new InvoiceInfo();
		info.setPlatId("SN");
		info.setShopId(storeId);
		info.setOrderId(orderId);
		info.setInvoiceType(query.getInvoiceType());//��Ʊ���� -- ��ֵ������ͨ��01��ֵ 02��ͨ 04 ���ӷ�Ʊ��
		info.setInvoiceTitle(query.getInvoiceHead());  //̧ͷ
		info.setInvoiceContentId(query.getInvoice());//��Ʊ����
		info.setInvoiceConsigneePhone(query.getInvoiceRecipientHandPhone()); //��Ʊ�ռ����ֻ�
		info.setInvoiceCode(query.getVatTaxpayerNumber()); //��˰��ʶ���
		order.setIsInvoice(1);
		order.setInvoiceTitle(query.getInvoiceHead()); //̧ͷ
		invoiceList.add(info);
		return invoiceList;
	}
	
	/**
	 * get��ȡ��Ʊ��Ϣ
	 */
	private List<InvoiceInfo> getInvoice(OrderGet query,String storeId,
										 String orderId,List<InvoiceInfo> invoiceList,PlatOrder order) {
		InvoiceInfo info = new InvoiceInfo();
		info.setPlatId("SN");
		info.setShopId(storeId);
		info.setOrderId(orderId);
		info.setInvoiceType(query.getInvoiceType());//��Ʊ���� -- ��ֵ������ͨ��01��ֵ 02��ͨ 04 ���ӷ�Ʊ��
		info.setInvoiceTitle(query.getInvoiceHead());  //̧ͷ
		info.setInvoiceContentId(query.getInvoice());//��Ʊ����
		info.setInvoiceConsigneePhone(query.getInvoiceRecipientHandPhone()); //��Ʊ�ռ����ֻ�
		info.setInvoiceCode(query.getVatTaxpayerNumber()); //��˰��ʶ���
		order.setIsInvoice(1);
		order.setInvoiceTitle(query.getInvoiceHead()); //̧ͷ
		invoiceList.add(info);
		return invoiceList;
	}
	
}
