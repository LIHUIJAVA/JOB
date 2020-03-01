package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.px.mis.ec.dao.CouponDetailDao;
import com.px.mis.ec.dao.EcExpressSettingsDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.CouponDetail;
import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;

@Component
@Transactional
public class PlatOrderXHS {

	@Autowired
	private StoreSettingsDao storeSettingsDao; // ��������
	@Autowired
	private PlatOrderDao platOrderDao; // ��������
	@Autowired
	private StoreRecordDao storeRecordDao; // ���̵���
	@Autowired
	private PlatOrdersDao platOrdersDao; // �����ӱ�
	@Autowired
	private GetOrderNo getOrderNo; // ���ɶ�����
	@Autowired
	private GoodRecordDao goodRecordDao; // ��Ʒ����
	@Autowired
	private InvtyDocDao invtyDocDao; // �������
	@Autowired
	private EcExpressSettingsDao ecExpressSettingsDao; // xhsС����
	@Autowired
	private CouponDetailDao couponDetailDao; // �Ż���ϸ
	@Autowired
	private RefundOrderDao refundOrderDao; // �˿����
	@Autowired
	private RefundOrdersDao refundOrdersDao; // �˿�ӱ�
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private MisUserDao misUserDao;
	@Autowired
	private PlatOrderServiceImpl orderServiceImpl;

	private Logger logger = LoggerFactory.getLogger(PlatOrderXHS.class);
	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * С����-��Ʒ����
	 * 
	 * @throws Exception
	 */
	public void xhsGoodRecord(StoreSettings storeSettings, int pageNo, int pageSize, String userName, String date,
			List<GoodRecord> goodRecordList) throws NoSuchAlgorithmException, IOException, URISyntaxException {
		String method = "/ark/open_api/v1/items";

		Map<String, String> map = new HashMap<String, String>();
//		��Ʒ״̬(0Ϊ�༭�У�1Ϊ����ˣ�2Ϊ���ͨ��)
		map.put("status", "2");
//		��Ʒҳ��, �ӵ�һҳ��ʼ,Ĭ��Ϊ1
		map.put("page_no", pageNo + "");
//		��Ʒ�б�ÿҳ������Ĭ��Ϊ50������Ϊ50
		map.put("page_size", pageSize + "");
		map.put("buyable", "true");
		/*
		 * create_time_to Int �� ��Ʒ����ʱ�����ʱ�䣬Unix-Timeʱ��� update_time_from Int ��
		 * ��Ʒ����ʱ�俪ʼʱ�䣬Unix-Timeʱ��� update_time_to Int �� ��Ʒ����ʱ�����ʱ�䣬Unix-Timeʱ���
		 * stock_gte Int �� �����ڵ���ĳ�� stock_lte Int �� ���С�ڵ���ĳ��
		 */
//		��Ʒ�Ƿ��������trueΪ�ڼ��Ͽ�������falseΪ���¼ܲ�������
//		map.put("buyable", "true");

		JSONObject response = ECHelper.getXHS(method, storeSettings, map);
		if (response == null) {
			logger.error("���ش���" + null);

			return;
		}
		Boolean xhsSuccess = response.getBoolean("success");
		if (!xhsSuccess) {
			response.getString("error_msg");
			logger.error("���ش���" + response.getString("error_msg"));
			return;
		}
		JSONObject pddtotal = response.getJSONObject("data");
		int totalItem = pddtotal.getIntValue("total");// total INTEGER ������Ʒ����
		JSONArray jsonArray = pddtotal.getJSONArray("hits");
		logger.info("��" + pageNo + "ҳ,��" + totalItem + "��");

		for (int i = 0; i < jsonArray.size(); i++) {
			// ���� jsonarray ���飬��ÿһ������ת�� json ����
			JSONObject job = jsonArray.getJSONObject(i);

			GoodRecord goodRecord = new GoodRecord();
			goodRecord.setEcGoodId(job.getJSONObject("item").getString("id")); // (50) DEFAULT NULL 'ƽ̨��Ʒ���',
			goodRecord.setStoreId(storeSettings.getStoreId()); // (50) DEFAULT NULL '���̱��',
			goodRecord.setEcId("XHS"); // (50) DEFAULT NULL '����ƽ̨',
			goodRecord.setEcGoodName(job.getJSONObject("item").getString("name")); // (100) DEFAULT NULL 'ƽ̨��Ʒ����',
			goodRecord.setGoodSku(null); // (50) DEFAULT NULL '������Ʒsku',
			goodRecord.setGoodId(job.getJSONObject("item").getString("barcode")); // (50) DEFAULT NULL '��Ʒ���',
			InvtyDoc invtyDoc = invtyDocDao
					.selectInvtyDocByInvtyDocEncd(job.getJSONObject("item").getString("barcode"));
			if (invtyDoc != null) {
				goodRecord.setGoodName(invtyDoc.getInvtyNm());// '�����Ʒ����',
				goodRecord.setGoodMode(invtyDoc.getSpcModel());// '����ͺ�',
			}
//   ��	  goodRecord.setUpsetPrice(upsetPrice); 	//	 '����ۼ�',	
			goodRecord.setSafeInv("0"); // (50) DEFAULT NULL '��ȫ���',
			// sku_list �� spec STRING �������
//			goodRecord.setSkuProp(skuJob.getString("spec")); // (50) DEFAULT NULL 'sku����',
			goodRecord.setOnlineStatus(job.getJSONObject("item").getBoolean("buyable") == false ? "�¼���" : "����"); // (50)
																													// DEFAULT
			// NULL
			// '����״̬',sku�Ƿ��ڼ��ϣ�0-�¼��У�1-����
			goodRecord.setOperator(userName); // (50) DEFAULT NULL '������',
			goodRecord.setOperatTime(date); // '����ʱ��',
			goodRecord.setIsSecSale(0);// '�Ƿ����',
//   ��	  goodRecord.setMemo(memo);	//	(500) DEFAULT NULL 	 '��ע',
			Integer id = goodRecordDao.selectByEcGoodIdAndSku(goodRecord);
			goodRecord.setId(id); // '��Ʒ����id',

			goodRecordList.add(goodRecord);

		}

		if (pageSize * pageNo < totalItem) {
			xhsGoodRecord(storeSettings, pageNo + 1, pageSize, userName, date, goodRecordList);
		}

	}

	/**
	 * С����-ӆ����������
	 * 
	 * @throws Exception
	 */
	@Transactional
	public String xhsDownloadPlatOrderLsit(String userId, int pageNo, int pageSize, String startDate, String endDate,
			String storeId, String status, Map<String, Integer> maps) throws Exception {

		boolean isSucess = true;

		String message = "";
		String resp = "";
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
//		// ��Ϣ
//		StoreRecord storeRecord = storeRecordDao.select(storeId);
		try {
			String method = "/ark/open_api/v0/packages";
			logger.info(startDate);
			Date date = sf.parse(startDate);
			long start = date.getTime() / 1000;
			date = sf.parse(endDate);
			long end = date.getTime() / 1000;

			Map<String, String> map = new HashMap<String, String>();
//		��Ʒҳ��, �ӵ�һҳ��ʼ,Ĭ��Ϊ1
			map.put("page_no", pageNo + "");
//		��ҳ��С, Ĭ��50�����ֵ������100
			map.put("page_size", pageSize + "");
			map.put("start_time", start + "");
			map.put("end_time", end + "");
			map.put("status", status);
			map.put("time_type", "confirmed_at");

			/*
			 * logistics String �� ����ģʽcode���������ֶ��򷵻ص���Ĭ������ģʽ�µĶ���, ������������᷵�ؿ�ֵ:
			 * 1.������ֵ�ҵ���Ĭ�ϵ�����ģʽ���Ǹýӿ�֧�ֵ��򷵻ؿ�ֵ 2.���������ģʽ���Ǹýӿ�֧�ֵ��򷵻ؿ�ֵ status String �� ����״̬,
			 * waiting������ʹ����(APP����ʾΪ������������),shipped�ѷ���,received�ռ�����ǩ�ա�
			 * ����������ֶ���Ĭ�Ϸ���waiting״̬�¶��� page_no Int �� ��ѯ��ǰ��ҳ����1��ʼ���� page_size Int �� ��ҳ��С,
			 * Ĭ��50�����ֵ������100 start_time Int ��
			 * ����ȷ�Ͽ�ʼʱ�䣬Unix-Timeʱ�����Ӧ��end_timeͬʱ��ֵ�����ݴ���time_type��ѯָ��ʱ�䷶Χ�ڵĶ���������Ĭ�ϲ�ѯ����ȷ��ʱ�䣨
			 * confirm_time�� end_time Int ��
			 * ����ȷ�Ͻ���ʱ�䣬Unix-Timeʱ�����Ӧ��start_timeͬʱ��ֵ�����ݴ���time_type��ѯָ��ʱ�䷶Χ�ڵĶ���������Ĭ�ϲ�ѯ����ȷ��ʱ��
			 * ��confirm_time�� time_type Int ��
			 * ������ѯʱ�����ͣ���������ʱ�䣺created_at������ȷ��ʱ�䣺confirmed_at����������ʱ�䣺updated_at
			 */
//		��Ʒ�Ƿ��������trueΪ�ڼ��Ͽ�������falseΪ���¼ܲ�������
//		map.put("buyable", "true");

			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "���ش���";
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("���ش���" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

				return resp;
			}

			JSONObject pddtotal = response.getJSONObject("data");
			int totalItem = pddtotal.getIntValue("total_page");// ��ҳ��
			JSONArray jsonArray = pddtotal.getJSONArray("package_list");// �����б�

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject job = jsonArray.getJSONObject(i);

				// ��ѯ��Ӧ�����Ƿ���ڴ˶���
				if (platOrderDao.checkExsits(job.getString("package_id"), storeId) == 0) {
					// ����������

					// logger.info(job.toString());
					maps.put("downloadCount", maps.get("downloadCount") + 1);
					//
					xhsDownloadByPlatOrder(job.getString("package_id"), userId, storeId, false);
				} else {
					message = "�����Ѵ���,������:" + job.getString("package_id") + ",���̱��:" + storeId;
					isSucess = false;
					logger.error("��������:С����ƽ̨�����Ѵ���,������:{},���̱��:{}", job.getString("package_id"), storeId);
				}

			}

			if (pageNo < totalItem) {
				logger.info(status + " ״̬,��" + pageNo + "ҳ,��" + totalItem + "��");
				resp = xhsDownloadPlatOrderLsit(userId, pageNo + 1, pageSize, startDate, endDate, storeId, status,
						maps);
			} else if (status.equals("waiting")) {
				resp = xhsDownloadPlatOrderLsit(userId, 1, pageSize, startDate, endDate, storeId, "shipped", maps);
			} else if (status.equals("shipped")) {
				resp = xhsDownloadPlatOrderLsit(userId, 1, pageSize, startDate, endDate, storeId, "received", maps);
			} else {

				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(userId);
				MisUser misUser = misUserDao.select(userId);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}

				logRecord.setOperatContent(
						"�����Զ����ص��̣�" + storeSettings.getStoreName() + "����" + maps.get("downloadCount") + "��");
				logRecord.setOperatTime(sf.format(new Date()));
				logRecord.setOperatType(10);// 9�ֹ����� 10 �Զ�����
				logRecord.setTypeName("�Զ�����");
				logRecordDao.insert(logRecord);
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess,
						"���γɹ�����" + maps.get("downloadCount") + "������", null);
				return resp;

			}
		} catch (Exception e) {
			logger.error("URL��ec/platOrder/download �쳣˵����", e);
			try {

				isSucess = false;
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, e.getMessage(), null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		return resp;

	}

	/**
	 * С����-���ݶ��������ض���
	 */
	@Transactional
	public String xhsDownloadByPlatOrder(String orderCode, String userId, String storeId, boolean isOrder)
			throws Exception {
		String method = "/ark/open_api/v0/packages/";
		String message = "";
		boolean isSucess = true;
		String resp = "";
		// ����
		try {

			if (isOrder) {
				// ��ѯ��Ӧ�����Ƿ���ڴ˶���
				if (platOrderDao.checkExsits(orderCode, storeId) != 0) {
					// ����������

					message = "�����Ѵ���,������:" + orderCode + ",���̱��:" + storeId;
					isSucess = false;
					logger.error("��������:С����ƽ̨�����Ѵ���,������:{},���̱��:{}", orderCode, storeId);
					resp = BaseJson.returnResp("ec/platOrder/download ", false, message, null);
					return resp;
				}

			}

			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			// ��Ϣ
			StoreRecord storeRecord = storeRecordDao.select(storeId);

			Map<String, String> map = new HashMap<String, String>();
//			����ID
			method = method + orderCode;
			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "���ش���";
				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("���ش���" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess, message, null);

				return resp;
			}

			JSONObject xhstotal = response.getJSONObject("data");

			String orderId = getOrderNo.getSeqNo("ec", userId);

			PlatOrder platOrder = new PlatOrder();

			platOrder.setEcOrderId(xhstotal.getString("package_id"));// ���̶�����
			platOrder.setOrderId(orderId); // �������
			platOrder.setStoreId(storeId); // ���̱��
			platOrder.setStoreName(storeRecord.getStoreName()); // '��������' 1
			platOrder.setPayTime(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(xhstotal.getLong("pay_time") * 1000))); // ����ʱ��'
			// t
			// paytime
//				platOrder.setWaif(waif);		//	'���' 			
			platOrder.setIsAudit(0); // '�Ƿ����'
//				platOrder.setAuditHint(auditHint); // '�����ʾ'
			// totalfee
			platOrder.setPayMoney(xhstotal.getBigDecimal("pay_amount")); // 'ʵ�����' t
																			// payment
//		platOrder.setBuyerNote(trade.getBuyerMessage()); // �������' t buyermessage
//		platOrder.setSellerNote(trade.getSellerMemo()); // ���ұ�ע'
			platOrder.setRecAddress(xhstotal.getString("receiver_address")); // �ջ�����ϸ��ַ' t receiveraddress
//		platOrder.setBuyerId(buyerId); // '��һ�Ա��'
			platOrder.setRecName(xhstotal.getString("receiver_name")); // �ջ�������' t receivername
			platOrder.setRecMobile(xhstotal.getString("receiver_phone")); // �ջ����ֻ���' t receivermobile
			platOrder.setIsInvoice(0); // '�Ƿ�Ʊ'
//		platOrder.setInvoiceTitle(resultJsons.getString("invoice_name")); // '��Ʊ̧ͷ'
//		platOrder.setNoteFlag(trade.getSellerFlag().intValue()); // ���ұ�ע����' t sellerflag
			platOrder.setIsClose(0); // '�Ƿ�ر�'
			platOrder.setAdjustMoney(new BigDecimal(0)); // ���ҵ������'
			// discountfee
			platOrder.setOrderStatus(0); // '����״̬'
			platOrder.setReturnStatus(0); // '�˻�״̬'
			// platOrder.setHasGift(0); // '�Ƿ���Ʒ'
			// platOrder.setMemo(memo); // '��ע'
			// platOrder.setAdjustStatus(adjustStatus); // '������ʶ'
			platOrder.setTradeDt(
					new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(xhstotal.getLong("time") * 1000))); // '����ʱ��'
			// t
			// created
			platOrder.setSellTypId("1");// ��������������ͨ����
			platOrder.setBizTypId("2");// ����ҵ������2c
			platOrder.setRecvSendCateId("6");// �����շ����
//		����״̬��waiting������ʹ����(APP����ʾΪ������������),shipped�ѷ���,received�ռ�����ǩ��
			if (xhstotal.getString("status").equals("waiting")) {
				platOrder.setIsShip(0); // '�Ƿ񷢻�'
			} else {
				platOrder.setIsShip(1); // '�Ƿ񷢻�'
				platOrder.setExpressNo(xhstotal.getString("express_no"));
			}
//		platOrder.setRecvSendCateId(recvSendCateId); // '�շ������'
			platOrder.setOrderSellerPrice(xhstotal.getBigDecimal("pay_amount")); // ���������ʵ�������'����֧�����(�����˷�)

			// receivedpayment
			platOrder.setProvince(xhstotal.getString("province")); // ʡ' t receiverstate
			// platOrder.setProvinceId(provinceId); // 'ʡid'
			platOrder.setCity(xhstotal.getString("city")); // ��' t receivercity
			// platOrder.setCityId(cityId); //
			platOrder.setCounty(xhstotal.getString("district")); // ��' t receiverdistrict
			// platOrder.setCountyId(countyId); //
			// platOrder.setTown(town); // '��'
			// platOrder.setTownId(townId); // '��id'
			platOrder.setFreightPrice(xhstotal.getBigDecimal("shipping_fee")); // �˷�' t

			platOrder.setVenderId("XHS"); // '�̼�id'

			// orders Order[]
			platOrder.setDownloadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			// platOrder.setDeliveryType("����ʱ��");
			// //'�ͻ������ڣ����ͣ�1-ֻ�������ͻ�(˫���ա����ղ�����;2-ֻ˫���ա������ͻ�(�����ղ�����;3-�����ա�˫��������վ����ͻ�;����ֵ-���ء�����ʱ�䡱
			// ��'

			List<PlatOrders> list = new ArrayList<PlatOrders>();
			List<CouponDetail> couponList = new ArrayList<>();
			JSONArray jsonArray = xhstotal.getJSONArray("item_list");
			BigDecimal goodMoney = BigDecimal.ZERO;
			BigDecimal discountMoney = BigDecimal.ZERO;
			BigDecimal payMoney = BigDecimal.ZERO;
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject order = jsonArray.getJSONObject(i);

				PlatOrders platOrders = new PlatOrders();
				platOrders.setGoodId(order.getString("item_id")); // ƽ̨��Ʒ��� ��Ʒ����ID
				platOrders.setGoodName(order.getString("item_name")); // 'ƽ̨��Ʒ����' t title String ɽկ����Ի��� ��Ʒ����
				platOrders.setGoodNum(order.getInteger("qty")); // '��Ʒ����' t num Number 1 ����������ȡֵ��Χ:�����������
				platOrders.setGoodMoney(order.getBigDecimal("price").multiply(order.getBigDecimal("qty"))); // ��Ʒ���'

				platOrders.setGoodPrice(order.getBigDecimal("price"));
				;// ��Ʒ����
				platOrders.setPayPrice(order.getBigDecimal("pay_price").add(order.getBigDecimal("red_discount")));
				;// ʵ������
				goodMoney = goodMoney.add(platOrders.getGoodMoney());
				platOrders.setPayMoney((order.getBigDecimal("pay_price").add(order.getBigDecimal("red_discount")))
						.multiply(order.getBigDecimal("qty"))); // 'ʵ�����'��̯֮���ʵ�����
				payMoney = payMoney.add(platOrders.getPayMoney());
				platOrders.setGoodSku(null); // '��Ʒsku'
				platOrders.setOrderId(orderId); // '�������' 1
//				platOrders.setExpressCom(xhstotal.getString("express_company_code")); // ��ݹ�˾' t�Ӷ��������Ŀ�ݹ�˾����
//			platOrders.setBatchNo(batchNo);	//	'����' 	
//			promotion_details		platOrders.setProActId(proActId);	//	��������' t promotionid	String	mjs	�Ż�id
				platOrders.setDiscountMoney(
						order.getBigDecimal("merchant_discount").multiply(order.getBigDecimal("qty"))); // ϵͳ�Żݽ��
																										// ������Ʒ�̼ҳе����Ż�
				discountMoney = discountMoney
						.add(order.getBigDecimal("merchant_discount").multiply(order.getBigDecimal("qty")));
				platOrders.setAdjustMoney(BigDecimal.ZERO); // ��ҵ������'
//			platOrders.setMemo(order.getOid().toString()); // '��ע' �� �ӵ�id oid
				platOrders.setGoodPrice(order.getBigDecimal("price")); // '��Ʒ����'
//			platOrders.setDeliverWhs(order.getStoreCode()); // �����ֿ����'
				platOrders.setEcOrderId(xhstotal.getString("package_id")); // 'ƽ̨������' t oid
//			 ��������
				if (xhstotal.getString("logistics").equals("red_auto")) {
					platOrder.setDeliverSelf(1); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t

				} else {
					platOrder.setDeliverSelf(0); // '�����Ƿ��Է�����0ƽ̨�ַ�����1�Է���' t

				}
				platOrder.setDeliverWhs("");
				if (xhstotal.getString("type") != null && xhstotal.getString("type").equals("gift")) {

					platOrders.setIsGift(1);

				} else {
					platOrders.setIsGift(0);
				}
				list.add(platOrders);
//			�Ż���Ϣ
				CouponDetail coupon1 = new CouponDetail();
				coupon1.setPlatId("XHS");// ƽ̨id������JD����èJD
				coupon1.setStoreId(storeId);// ����id
				coupon1.setOrderId(xhstotal.getString("package_id"));// ������
				coupon1.setSkuId(null);
				coupon1.setCouponCode(null);// �Ż����ͱ���
				coupon1.setCouponType("С����е�");// �Ż�����
				coupon1.setCouponPrice(order.getBigDecimal("red_discount").multiply(order.getBigDecimal("qty")));// ������ƷС����е����Ż�
				CouponDetail coupon = new CouponDetail();
				coupon.setPlatId("XHS");// ƽ̨id������JD����èJD
				coupon.setStoreId(storeId);// ����id
				coupon.setOrderId(xhstotal.getString("package_id"));// ������
				coupon.setSkuId(null);
				coupon.setCouponCode(null);// �Ż����ͱ���
				coupon.setCouponType("�̼ҳе�");// �Ż�����
				coupon.setCouponPrice(order.getBigDecimal("merchant_discount").multiply(order.getBigDecimal("qty")));// ������ƷС����е����Ż�
				if (coupon1.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
					couponList.add(coupon1);
				}
				if (coupon.getCouponPrice().compareTo(BigDecimal.ZERO) != 0) {
					couponList.add(coupon);
				}
			}
			if (couponList.size() > 0) {
				couponDetailDao.insert(couponList);
			}

			platOrder.setGoodNum(list.size()); // ��ƷƷ����
			platOrder.setGoodMoney(goodMoney); // ��Ʒ���' t
			platOrder.setDiscountMoney(discountMoney); // ϵͳ�Żݽ��' ������Ʒ�̼ҳе����Ż�
			platOrder.setPayMoney(payMoney); // 'ʵ�����' t

			platOrderDao.insert(platOrder);
			platOrdersDao.insert(list);

			// ִ���Զ�ƥ��
			orderServiceImpl.autoMatch(platOrder.getOrderId(), userId);

			logger.info("�������سɹ�" + xhstotal.getString("package_id"));
			resp = BaseJson.returnRespObj("ec/platOrder/download", isSucess,
					"�������سɹ�" + xhstotal.getString("package_id"), null);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnResp("ec/platOrder/download ", false, "�������أ�����ʧ��", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return resp;

	}

	/**
	 * С����-��ݹ�˾�б�
	 * 
	 * @throws Exception
	 */
	@Transactional
	public String xhsExpressSettingsDao(String storeId) throws Exception {

		boolean isSucess = true;

		String message = "";
		String resp = "";
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
//		// ��Ϣ
//		StoreRecord storeRecord = storeRecordDao.select(storeId);
		try {

			String method = "/ark/open_api/v0/express_companies";

			Map<String, String> map = new HashMap<String, String>();

			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "���ش���";
				resp = BaseJson.returnRespObj("ec/platOrder/xhsExpressSettingsDao", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("���ش���" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/platOrder/xhsExpressSettingsDao", isSucess, message, null);

				return resp;
			}
			ecExpressSettingsDao.deleteExpressCodeAndName();

			JSONArray jsonArray = response.getJSONArray("data");
			List<ExpressCodeAndName> list = new ArrayList<ExpressCodeAndName>();

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject job = jsonArray.getJSONObject(i);
				ExpressCodeAndName expressCodeAndName = new ExpressCodeAndName();

				logger.info(job.toString());
				expressCodeAndName.setExpressCode(job.getString("express_company_code"));
				expressCodeAndName.setExpressName(job.getString("express_company_name"));
				list.add(expressCodeAndName);
			}
			ecExpressSettingsDao.intotExpressCodeAndName(list);

		} catch (Exception e) {
			logger.error("URL��ec/platOrder/xhsExpressSettingsDao �쳣˵����", e);
			try {
				isSucess = false;
				resp = BaseJson.returnRespObj("ec/platOrder/xhsExpressSettingsDao", isSucess, message, null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		return resp;

	}

	/**
	 * С����-�˻��б�
	 */
	@Transactional
	public String xhsRefundPlatOrder(String userId, int pageNo, int pageSize, String startDate, String endDate,
			String storeId, int downloadCount) {
		String method = "/ark/open_api/v0/packages/canceling/list";
//		String method = "/ark/open_api/v0/packages";

		String message = "";
		boolean isSucess = true;
		String resp = "";
		// ����
		StoreSettings storeSettings = storeSettingsDao.select(storeId);
		try {
			Date date = sf.parse(startDate);
			long start = date.getTime() / 1000;
			date = sf.parse(endDate);
			long end = date.getTime() / 1000;

			Map<String, String> map = new HashMap<String, String>();
//		����ID
			map.put("status", "audited");
			map.put("page_no", pageNo + "");
			map.put("page_size", pageSize + "");
			map.put("start_time", start + "");
			map.put("end_time", end + "");

			JSONObject response = ECHelper.getXHS(method, storeSettings, map);
			if (response == null) {
				message = "���ش���";
				resp = BaseJson.returnRespObj("ec/refundOrder/download", isSucess, message, null);
				return resp;
			}
			Boolean xhsSuccess = response.getBoolean("success");
			if (!xhsSuccess) {
				message = response.getString("error_msg");
				logger.error("���ش���" + response.getString("error_msg"));
				isSucess = false;

				resp = BaseJson.returnRespObj("ec/refundOrder/download", isSucess, message, null);
				return resp;
			}

			JSONObject pddtotal = response.getJSONObject("data");
			int totalItem = pddtotal.getIntValue("total_page");// ��ҳ��
			JSONArray jsonArray = pddtotal.getJSONArray("package_list");// �����б�
			MisUser misUser = misUserDao.select(userId);
			List<RefundOrder> refundOrderList = new ArrayList<>();
			List<RefundOrders> refundOrdersList = new ArrayList<>();
			StoreRecord storeRecord = storeRecordDao.select(storeId);

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject job = jsonArray.getJSONObject(i);
				if (job.getString("audit_result").equals("canceled") && job.getString("status").equals("audited")) {
//				if (true) {
//				package_id

					if (platOrderDao.checkExsits(job.getString("package_id"), storeId) == 0) {
						// ԭ������ɾ��,������
						logger.info("ԭ������ɾ��,�����ڣ�" + job.getString("package_id"));

						continue;
					}
					if (refundOrderDao.selectEcRefId(job.getString("package_id")) == null) {
						// �ж��˿�Ƿ��Ѵ���
						logger.info("�ж��˿�Ѵ��ڣ�" + job.getString("package_id"));

						continue;
					}
					RefundOrder refundOrder = new RefundOrder();

					// ��������
					String refId = getOrderNo.getSeqNo("tk", userId);
					String ecOrderId = job.getString("package_id");
					refundOrder.setRefId(refId); // �˿���
					refundOrder.setEcId("XHS");
					refundOrder.setOrderId(job.getString("package_id")); // �������
					refundOrder.setStoreId(storeId); // ���̱��
					refundOrder.setEcOrderId(job.getString("package_id"));
					refundOrder.setStoreName(storeRecord.getStoreName()); // ��������
					refundOrder.setEcRefId(job.getString("package_id")); // �����˿��
					refundOrder.setApplyDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date(job.getLong("cancel_time") * 1000))); // ��������
//				refundOrder.setBuyerId(refund.getBuyerOpenUid()); // ��һ�Ա��
					refundOrder.setIsRefund(1); // �Ƿ��˻�
//				refundOrder.setAllRefNum(refund.getNum().intValue()); // �����˻�����
//				refundOrder.setAllRefMoney(new BigDecimal(refund.getRefundFee())); // �����˿���
					refundOrder.setRefReason(job.getString("cancel_reason")); // �˿�ԭ��
//				refundOrder.setRefExplain(refund.getDesc()); // �˿�˵��
					refundOrder.setRefStatus(1); // �˿�״̬
					refundOrder.setDownTime(sf.format(new Date())); // ����ʱ��
					refundOrder.setTreDate(sf.format(new Date())); // ��������
					refundOrder.setOperator(userId); // ����Ա
					refundOrder.setIsAudit(0); // �Ƿ����
//			refundOrder.setMemo(memo); // ��ע
					refundOrder.setSource(0);// �˿��Դ����0
					refundOrder.setOperatorId(misUser.getAccNum());
					refundOrder.setOperatorTime(sf.format(new Date()));

					// �ö�����ȥ��ѯԭ���Ĺ��������Լ�ʵ�����
					List<PlatOrders> platOrders = platOrdersDao.selectByEcOrderId(ecOrderId);
					if (platOrders.size() == 0) {
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(userId);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�˿���أ�������" + ecOrderId + "δ�ҵ�������ϸ����������ʧ�ܡ�");
						logRecord.setOperatTime(sf.format(new Date()));
						logRecord.setOperatType(12);// 12�˿�����
						logRecord.setTypeName("�˿����");
						logRecord.setOperatOrder(ecOrderId);
						logRecordDao.insert(logRecord);
					} else {
						boolean flag = true;
						for (int i1 = 0; i1 < platOrders.size(); i1++) {
							// ѭ���ж�ԭ�����Ƿ����δƥ�䵽��������ļ�¼
							if (!StringUtils.isNotEmpty(platOrders.get(i1).getInvId())) {
								flag = false;
								break;
							}
						}
						if (!flag) {
							// ��־��¼
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(userId);
							if (misUser != null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("�˿���أ�������" + ecOrderId + "����δƥ�䵽��������ļ�¼����������ʧ�ܡ�");
							logRecord.setOperatTime(sf.format(new Date()));
							logRecord.setOperatType(12);// 12�˿�����
							logRecord.setTypeName("�˿����");
							logRecord.setOperatOrder(ecOrderId);
							logRecordDao.insert(logRecord);
							continue;
						}
					}
					for (int i1 = 0; i1 < platOrders.size(); i1++) {
						// ֱ��ȡ��ǰ����ϸ�����˿
						RefundOrders refundOrders = new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodSku(StringUtils.isEmpty(platOrders.get(i1).getGoodSku()) ? ""
								: platOrders.get(i1).getGoodSku());// ��Ʒsku
						refundOrders.setEcGoodId(platOrders.get(i1).getGoodId());// ���õ�����Ʒ����
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i1).getInvId());

						if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));// ���ñ�����
						}
						refundOrders.setIsGift(platOrders.get(i1).getIsGift());// �Ƿ���Ʒ��ԭ��������
						refundOrders.setPrdcDt(platOrders.get(i1).getPrdcDt());// ������������
						refundOrders.setInvldtnDt(platOrders.get(i1).getInvldtnDt());// ����ʧЧ����
						refundOrders.setCanRefNum(platOrders.get(i1).getCanRefNum());// ���ÿ�������
						refundOrders.setCanRefMoney(platOrders.get(i1).getCanRefMoney());// ���˽��
						refundOrders.setRefMoney(platOrders.get(i1).getPayMoney());// �˻����

						refundOrders.setGoodId(platOrders.get(i1).getInvId());// ���ô������

						refundOrders.setGoodName(invtyDoc.getInvtyNm());// ���ô������
						refundOrders.setBatchNo(platOrders.get(i1).getBatchNo());// �����˻�����
						refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());// ȡ��Ӧ���̵�Ĭ���˻���
						refundOrdersList.add(refundOrders);// װ��list
					}

				}

			}
			logger.info("����������" + refundOrderList.size());

			if (refundOrderList.size() > 0) {
				refundOrderDao.insertList(refundOrderList);
				refundOrdersDao.insertList(refundOrdersList);
				downloadCount += refundOrderList.size();
			}
			if (pageNo < totalItem) {
				logger.info("��" + pageNo + "ҳ,��" + totalItem + "��");
				xhsRefundPlatOrder(userId, pageNo + 1, pageSize, startDate, endDate, storeId, downloadCount);
			} else {

				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(userId);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(
						"�˿���أ����γɹ�����С������̣�" + storeRecord.getStoreName() + ",�˿" + downloadCount + "��");
				logRecord.setOperatTime(sf.format(new Date()));
				logRecord.setOperatType(12);// 12�˿�����
				logRecord.setTypeName("�˿�����");
				logRecordDao.insert(logRecord);
				resp = BaseJson.returnResp("ec/refundOrder/download ", true,
						"�˿���أ����γɹ�����С������̣�" + storeRecord.getStoreName() + ",�˿" + downloadCount + "��", null);

			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				resp = BaseJson.returnResp("ec/refundOrder/download ", false, "�˿���أ�����" + downloadCount + "��������쳣",
						null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return resp;
	}
}
