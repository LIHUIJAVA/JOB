package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.px.mis.util.poiTool;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddGoodsListGetRequest;
import com.pdd.pop.sdk.http.api.response.PddGoodsListGetResponse;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.GoodRecordService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.taobao.api.ApiException;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.ItemsSellerListGetRequest;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.ItemsSellerListGetResponse;

@Transactional
@Service
public class GoodRecordServiceImpl extends poiTool implements GoodRecordService {

	private Logger logger = LoggerFactory.getLogger(GoodRecordServiceImpl.class);

	@Autowired
	private GoodRecordDao goodRecordDao;

	@Autowired
	private StoreSettingsDao storeSettingsDao;

	@Autowired
	private StoreRecordDao storeRecordDao;

	@Autowired
	private InvtyDocDao invtyDocDao;

	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	PlatOrderXHS platOrderXHS;
	@Autowired
	PlatOrderXMYP platOrderXMYP;
//	private List<GoodRecord> goodRecordsRecordList = new ArrayList();

	@Override
	public String add(GoodRecord goodRecord) {
		String message = "";
		Boolean isSuccess = false;
		String resp = "";
		if (goodRecordDao.selectByEcGoodIdAndSku(goodRecord) != null) {
			message = "sku:" + goodRecord.getGoodSku() + "��spu:" + goodRecord.getEcGoodId() + "�Ѵ��ڣ����������룡";
			isSuccess = false;

		} else {
			goodRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			goodRecordDao.insert(goodRecord);
			message = "�����ɹ���";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/goodRecord/add", isSuccess, message, goodRecord);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String edit(GoodRecord goodRecord) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (goodRecordDao.selectById(goodRecord.getId()) == null) {
				message = "id" + goodRecord.getId() + "�����ڣ��������޸ģ�";
				isSuccess = false;
			} else {
				// System.err.println("\t\t:" +
				// goodRecordDao.selectById(goodRecord.getId()).getStoreName());
				goodRecordDao.update(goodRecord);
				message = "�޸ĳɹ���";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/goodRecord/edit", isSuccess, message, goodRecord);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/add �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String delete(String id) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<String> idStrings = Arrays.asList(id.split(","));
			goodRecordDao.delete(idStrings);
			resp = BaseJson.returnRespObj("ec/goodRecord/delete", isSuccess, "ɾ���ɹ�", null);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/delete �쳣˵����", e);
			try {
				resp = BaseJson.returnRespObj("ec/goodRecord/delete", isSuccess, "ɾ���쳣��������", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	@Override
	public String query(Integer id) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		GoodRecord ecPlatform = goodRecordDao.selectById(id);

		try {
			if (ecPlatform == null) {
				message = "���" + id + "�����ڣ����������룡";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/goodRecord/query", isSuccess, message, null);
			} else {
				message = "��ѯ�ɹ�";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/goodRecord/query", isSuccess, message, ecPlatform);
			}
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/query �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<GoodRecord> gList = goodRecordDao.selectList(map);
			int count = goodRecordDao.selectCount(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = gList.size();
			int pages = count / pageSize + 1;
			resp = BaseJson.returnRespList("ec/goodRecord/queryList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, gList);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/queryList �쳣˵����", e);
		}
		return resp;
	}

	@Override
	public String download(String accNum, String storeId) {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		boolean flag = true;
		List<GoodRecord> goodRecordList = new ArrayList<>();

		try {
			String userName = misUserDao.select(accNum).getUserName();
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

			StoreRecord storeRecord = storeRecordDao.select(storeId);
			StoreSettings storeSettings = storeSettingsDao.select(storeRecord.getStoreId());
			if (storeRecord.getEcId().equals("JD")) {
				getJDSku(storeSettings, 1, 20, userName, date, goodRecordList);
				// jdDownload(storeSettings, 1, 20, userName, date,goodRecordList);
			} else if (storeRecord.getEcId().equals("TM")) {
				tmDownloadSdk(storeSettings, 1, 20, userName, date, goodRecordList);
			} else if (storeRecord.getEcId().equals("PDD")) {
				pddDownload(storeSettings, 1, 20, userName, date, goodRecordList);
				
			} else if (storeRecord.getEcId().equals("SN")) {	
				
				goodRecordList = platOrderSN.getSNSku(userName, 1, 20,date,storeSettings);
				
			} else if(storeRecord.getEcId().equals("KaoLa")){
				kaolaDownload(storeSettings, 1, 5, "accNum", date, goodRecordList);
				
			} else if (storeRecord.getEcId().equals("XHS")) {
				platOrderXHS.xhsGoodRecord(storeSettings, 1, 20, userName, date, goodRecordList);

			}
			else if (storeRecord.getEcId().equals("XMYP")) {
				platOrderXMYP.getXMYPGoodSku(userName,date,storeSettings,goodRecordList);

			}
			else {
				flag = false;
				message = "��ѡ��ĵ��̲�֧������";
			}
			if (goodRecordList.size() == 0) {
				if (flag) {
					isSuccess = false;
					message = "������ɣ�û����Ҫ���ص���Ʒ��";
				}
			} else {
				goodRecordDao.download(goodRecordList);
				isSuccess = true;
				message = "���³ɹ���������" + goodRecordList.size() + "����Ʒ��Ϣ��";
			}

			resp = BaseJson.returnRespObj("ec/goodRecord/download", isSuccess, message, null);
		} catch (Exception e) {
			logger.error("URL:ec/goodRecord/download �쳣˵����", e);
			try {
				resp = BaseJson.returnRespObj("ec/goodRecord/download", isSuccess, "���س����쳣������", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	private void tmDownloadSdk(StoreSettings storeSettings, int pageNo, int pageSize, String userName, String date,
			List<GoodRecord> goodRecordList) throws NoSuchAlgorithmException, IOException, ApiException {
//		String url = "http://47.101.179.124/HttpTrans/TBTrans";
//		String url = "http://gw.api.taobao.com/router/rest";

		ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setFields("num_iid");
		req.setPageNo(Long.valueOf(pageNo));
		req.setPageSize(Long.valueOf(pageSize));

		// ת��
		Map<String, String> map = new HashMap<String, String>();
		map.put("path", ItemsOnsaleGetRequest.class.getName());
		map.put("taobaoObject", JSON.toJSONString(req));
		String taobao = ECHelper.getTB("", storeSettings, map);
		ItemsOnsaleGetResponse rsp = JSONObject.parseObject(taobao, ItemsOnsaleGetResponse.class);

		logger.info("��èbody:" + rsp.getBody());
		if (!rsp.isSuccess()) {
			logger.error("��èerr:" + rsp.getSubMsg());
			return;
		}
		if (rsp.getTotalResults() == 0) {
			logger.error("��è��Ʒ��0");
			return;
		}

		StringBuffer sb = new StringBuffer();

		for (com.taobao.api.domain.Item item : rsp.getItems()) {
			sb.append(item.getNumIid()).append(",");
		}
		// �ӿ� ���� 20 ���
		String keywordStr = sb.deleteCharAt(sb.length() - 1).toString();

		ItemsSellerListGetRequest preq = new ItemsSellerListGetRequest();
		preq.setFields("outer_id,num_iid,title,nick,price,approve_status,sku");
		preq.setNumIids(keywordStr);

		Map<String, String> maps = new HashMap<String, String>();
		maps.put("path", ItemsSellerListGetRequest.class.getName());
		maps.put("taobaoObject", JSON.toJSONString(preq));
		String taobaos = ECHelper.getTB("", storeSettings, maps);

		ItemsSellerListGetResponse prsp = JSONObject.parseObject(taobaos, ItemsSellerListGetResponse.class);
		System.out.println(prsp.getBody());
		logger.info("��èbody:" + prsp.getBody());
		if (!prsp.isSuccess()) {
			logger.error("��èerr:" + prsp.getSubMsg());
			return;
		}

		for (com.taobao.api.domain.Item item : prsp.getItems()) {
			GoodRecord goodRecord = new GoodRecord();
			goodRecord.setEcGoodId(item.getNumIid().toString());// 'ƽ̨��Ʒ���',
			goodRecord.setEcGoodName(item.getTitle());// 'ƽ̨��Ʒ����',
			goodRecord.setGoodId(item.getOuterId());// '��Ʒ���',
			goodRecord.setUpsetPrice(new BigDecimal(item.getPrice()==null?"0":item.getPrice()));// '����ۼ�',

			goodRecord.setEcId("TM");// '����ƽ̨',
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(item.getOuterId());
			if (invtyDoc != null) {
//				`goodname`1	varchar(100		
				goodRecord.setGoodName(invtyDoc.getInvtyNm());// '�����Ʒ����',
				goodRecord.setGoodMode(invtyDoc.getSpcModel());// '����ͺ�',
			}
			goodRecord.setSafeInv("0");// '��ȫ���',
			goodRecord.setOnlineStatus("����");// '����״̬',
			goodRecord.setIsSecSale(0);// '�Ƿ����',
			goodRecord.setOperator(userName);// '������',
			goodRecord.setOperatTime(date);// '����ʱ��',

			if (item.getSkus() != null && item.getSkus().size() > 0) {
				for (com.taobao.api.domain.Sku sku : item.getSkus()) {
					GoodRecord goodRecords = new GoodRecord();
					goodRecord.setSkuProp(sku.getProperties());// sku����
					goodRecord.setGoodSku(sku.getSkuId().toString());// ������Ʒsku
					Integer id = goodRecordDao.selectByEcGoodIdAnd(goodRecord);
					goodRecord.setId(id);
					logger.info("sku����" + sku.getProperties());
					goodRecord.setStoreId(storeSettings.getStoreId());// '���̱��',
					BeanUtils.copyProperties(goodRecord, goodRecords);
					goodRecordList.add(goodRecords);
				}
			} else {
				Integer id = goodRecordDao.selectByEcGoodIdAnd(goodRecord);
				goodRecord.setId(id);
				goodRecord.setStoreId(storeSettings.getStoreId());// '���̱��',
				goodRecordList.add(goodRecord);

			}

		}
		if (pageSize * pageNo < rsp.getTotalResults()) {
			tmDownloadSdk(storeSettings, pageNo + 1, pageSize, userName, date, goodRecordList);
		}

//		Map<String, String> map = new HashMap<String, String>();
//		map.put("page_no", pageNo + "");
//		map.put("page_size", pageSize + "");
//		map.put("fields", "num_iid");
////				"approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id,sold_quantity");
//
//		String tmRespStr = ECHelper.getTB("taobao.items.onsale.get", storeSettings, map);
////				("jingdong.ware.read.searchWare4Valid", storeSettings, jdReq.toString());
////		JSONArray jsonObject = JSON.parseObject(tmRespStr).getJSONObject("items_onsale_get_response").getJSONObject("items").getJSONArray("list");
//		JsonNode tmRespJson = JacksonUtil.getObjectNode(tmRespStr);
//		if (tmRespJson.has("error_response")) {
//			return;
////		BaseJson.returnRespObj("ec/goodRecord/download", false, tmRespJson.get("error_response").get("sub_msg").asText(), null);
//		}
//		int totalItem = tmRespJson.get("items_onsale_get_response").get("total_results").asInt();
//		if (totalItem == 0) {
//			return;
//		}
//		ArrayNode dataList = (ArrayNode) tmRespJson.get("items_onsale_get_response").get("items").get("item");
//		StringBuffer sb = new StringBuffer();
//		for (Iterator<JsonNode> dataIterator = dataList.iterator(); dataIterator.hasNext();) {
//			JsonNode data = dataIterator.next();
//			sb.append(data.get("num_iid")).append(",");
//		}
//		// �ӿ� ���� 20 ���
//		String keywordStr = sb.deleteCharAt(sb.length() - 1).toString();
//		System.err.println("keywordStr\t" + keywordStr);
//		Map<String, String> maps = new HashMap<String, String>();
//
//		maps.put("fields", "outer_id,num_iid,title,nick,price,approve_status,sku.sku_id");
//		maps.put("num_iids", keywordStr);
//
//		String tmResp = ECHelper.getTB("taobao.items.seller.list.get", storeSettings, maps);
//		JsonNode tmResps = JacksonUtil.getObjectNode(tmResp);
//		if (tmResps.has("error_response")) {
//			return;
////		BaseJson.returnRespObj("ec/goodRecord/download", false, tmRespJson.get("error_response").get("sub_msg").asText(), null);
//		}
//		ArrayNode dataLists = (ArrayNode) tmResps.get("items_seller_list_get_response").get("items").get("item");
//		for (Iterator<JsonNode> dataIterator = dataLists.iterator(); dataIterator.hasNext();) {
//			JsonNode data = dataIterator.next();
//
//	
//
////			`id`1	int(11	'��Ʒ����id',
////			`goodsku`0	varchar(50			'������Ʒsku',
////			`skuprop`0	varchar(50			'sku����',
////			`memo` 	varchar(500			'��ע',
//
//		}
	}

	private void jdDownload(StoreSettings storeSettings, int pageNo, int pageSize, String userName, String date,
			List<GoodRecord> goodRecordList) throws NoSuchAlgorithmException, IOException {
		/*
		 * ObjectNode jdReq = JacksonUtil.getObjectNode(""); jdReq.put("pageNo", pageNo
		 * + ""); jdReq.put("pageSize", pageSize + ""); jdReq.put("wareStatusValue",
		 * "8"); jdReq.put("field",
		 * "wareId,title,categoryId,brandId,templateId,transportId,wareStatus,outerId,itemNum,barCode,wareLocation,modified,created,offlineTime,onlineTime,colType,delivery,adWords,wrap,packListing,weight,width,height,length,props,features,images,shopCategorys,mobileDesc,introduction,zhuangBaIntroduction,zhuangBaId,introductionUseFlag,afterSales,logo,marketPrice,costPrice,jdPrice,brandName,stockNum,categorySecId,shopId,promiseId,multiCategoryId,multiCateProps,sellPoint,wareTax,afterSaleDesc,zhuangBaMobileDesc,mobileZhuangBaId,mobileDescUseFlag,fitCaseHtmlPc,fitCaseHtmlApp,specialServices,parentId,wareGroupId,businessType,designConcept,isArchival,templateIds"
		 * ); String jdRespStr = ECHelper.getJD("jingdong.ware.read.searchWare4Valid",
		 * storeSettings, jdReq.toString()); ObjectNode jdRespJson =
		 * JacksonUtil.getObjectNode(jdRespStr); JsonNode resultJson =
		 * jdRespJson.get("jingdong_ware_read_searchWare4Valid_responce"); if
		 * (resultJson.get("code").asText().equals("0")) {
		 */
		/*
		 * List<GoodRecord> goodRecords = getJDSku(storeSettings, pageNo,
		 * pageSize,userName,date,goodRecords); for (int i = 0; i < goodRecords.size();
		 * i++) { goodRecordList.add(goodRecords.get(i)); }
		 */
		// JsonNode page = resultJson.get("page");

		// ArrayNode dataList = (ArrayNode) page.get("data");

		/*
		 * for (Iterator<JsonNode> dataIterator = dataList.iterator();
		 * dataIterator.hasNext();) { JsonNode data = dataIterator.next();
		 * 
		 * GoodRecord goodRecord = new GoodRecord(); // private String goodSku;//��Ʒsku
		 * // private String ecGoodId;//ƽ̨��Ʒ���
		 * goodRecord.setStoreId(storeSettings.getStoreId()); goodRecord.setEcId("JD");
		 * goodRecord.setEcGoodId(data.get("wareId").asText());
		 * goodRecord.setEcGoodName(data.get("title").asText()); if
		 * (data.has("itemNum")) { goodRecord.setGoodId(data.get("itemNum").asText());
		 * InvtyDoc invtyDoc =
		 * invtyDocDao.selectInvtyDocByInvtyDocEncd(data.get("itemNum").asText()); if
		 * (invtyDoc != null) { goodRecord.setGoodName(invtyDoc.getInvtyNm());
		 * goodRecord.setGoodMode(invtyDoc.getSpcModel()); } }
		 */
		/*
		 * for (Map<String, String> map : mapList) {
		 * 
		 * if (map.get("wareId").equals(data.get("wareId").asText())) {
		 * goodRecord.setGoodSku(map.get("skuId"));
		 * System.out.println(map.get("skuId")); // goodSku ecGoodId Integer id =
		 * goodRecordDao.selectByEcGoodIdAndSku(goodRecord);
		 * System.err.println("id\t"+id); if(id!=null) goodRecord.setId(id); } }
		 */

		/*
		 * goodRecord.setOperator(userName); goodRecord.setOperatTime(date);
		 * goodRecord.setIsSecSale(0);//'�Ƿ����',
		 */
//				    private BigDecimal upsetPrice;//����ۼ�
//				    private String safeInv;//��ȫ���
//				    private String skuProp;//sku����
//				    private String onlineStatus;//����״̬

		// goodRecordList.add(goodRecord);
		/*
		 * }
		 * 
		 * int totalItem = page.get("totalItem").asInt(); if (pageSize * pageNo <
		 * totalItem) { jdDownload(storeSettings, pageNo + 1, pageSize, userName, date,
		 * goodRecordList); } }
		 */
	}
	/**
	 * ����������Ʒ��������
	 * @param storeSettings
	 * @param pageNo
	 * @param pageSize
	 * @param oprator
	 * @param date
	 * @param gRecords1
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private void kaolaDownload(StoreSettings storeSettings, int pageNo, int pageSize, String oprator, String date,
			List<GoodRecord> gRecords1)throws IOException, NoSuchAlgorithmException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("page_no", pageNo + "");
		jdReq.put("page_size", pageSize + "");
		jdReq.put("item_edit_status", "5");//��Ʒ״̬����
		
		String jdRespStr = ECHelper.getKaola("kaola.item.batch.status.get", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		ArrayNode dataList = (ArrayNode) jdRespJson.get("kaola_item_batch_status_get_response").get("item_edit_list");
		for (Iterator<JsonNode> dataIterator = dataList.iterator(); dataIterator.hasNext();) {
			JsonNode data = dataIterator.next();
			//System.out.println("��Ʒ���ƣ�"+data.toString());
			GoodRecord gRecord = new GoodRecord();
			gRecord.setEcGoodId(data.get("sku_list").get(0).get("raw_sku").get("bar_code").asText());
			gRecord.setGoodSku("");//sku
			gRecord.setEcGoodName(data.get("raw_item_edit").get("name").asText());
			//System.out.println(data.get("sku_list").get(0).get("raw_sku").get("sale_price").asText());
			gRecord.setUpsetPrice(new BigDecimal(String.valueOf(data.get("sku_list").get(0).get("raw_sku").get("sale_price").asText())));
			
			gRecord.setEcId("KaoLa");
			gRecord.setStoreId(storeSettings.getStoreId());
			gRecord.setEcName("���׿���");
			gRecord.setIsSecSale(0);
			gRecord.setOnlineStatus("����");
			gRecord.setSafeInv("0");
			gRecord.setOperatTime(date);
			gRecord.setOperator(oprator);
			if (data.get("raw_item_edit").has("outerId")) {
				if (StringUtils.isNotEmpty(data.get("raw_item_edit").get("outerId").asText())) {
					InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(data.get("outerId").asText());
					if (invtyDoc != null) {
						gRecord.setGoodId(data.get("raw_item_edit").get("outerId").asText());
						gRecord.setGoodName(invtyDoc.getInvtyNm());
						gRecord.setGoodMode(invtyDoc.getSpcModel());
					}
				}
			}
			Integer id = goodRecordDao.selectByEcGoodIdAndSku(gRecord);
			gRecord.setId(id);
			gRecords1.add(gRecord);
			
		}
		
		int totalItem = jdRespJson.get("kaola_item_batch_status_get_response").get("total_count").asInt();
		if (pageSize * pageNo < totalItem) {
			// jdDownload(storeSettings, pageNo + 1, pageSize, userName, date,
			// goodRecordList);
			kaolaDownload(storeSettings, pageNo + 1, pageSize, oprator, date, gRecords1);
		}
		//System.out.println("���ؽ����"+jdRespStr);
		
		
	}
	
	

	private void getJDSku(StoreSettings storeSettings, int pageNo, int pageSize, String oprator, String date,
			List<GoodRecord> gRecords1) throws IOException, NoSuchAlgorithmException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageNo", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		// jdReq.put("skuStatuValue", "1");//�����ϼܵ�sku
		jdReq.put("field", "wareTitle,skuName");
		// List<GoodRecord> records = new ArrayList<GoodRecord>();
		String jdRespStr = ECHelper.getJD("jingdong.sku.read.searchSkuList", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		//System.err.println("AAAA:" + jdRespJson);
		JsonNode resultJson = jdRespJson.get("jingdong_sku_read_searchSkuList_responce");
		// List<Map<String, String>> mapList = new ArrayList<>();
		if (resultJson.get("code").asText().equals("0")) {
			ArrayNode dataList = (ArrayNode) resultJson.get("page").get("data");
			for (Iterator<JsonNode> dataIterator = dataList.iterator(); dataIterator.hasNext();) {
				JsonNode data = dataIterator.next();
				GoodRecord gRecord = new GoodRecord();
				gRecord.setEcGoodId(data.get("wareId").asText());
				gRecord.setGoodSku(data.get("skuId").asText());
				gRecord.setEcGoodName(data.get("skuName").asText());
				gRecord.setEcId("JD");
				gRecord.setStoreId(storeSettings.getStoreId());
				gRecord.setEcName("����");
				gRecord.setUpsetPrice(new BigDecimal(String.valueOf(data.get("jdPrice").asText())));
				gRecord.setIsSecSale(0);
				if (data.get("status").asInt() == 1) {
					gRecord.setOnlineStatus("����");
				} else {
					gRecord.setOnlineStatus("�¼�");
				}
				gRecord.setSafeInv("0");
				gRecord.setOperatTime(date);
				gRecord.setOperator(oprator);
				if (data.has("outerId")) {
					if (data.get("outerId").asText().length() > 0) {
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(data.get("outerId").asText());
						if (invtyDoc != null) {
							gRecord.setGoodId(data.get("outerId").asText());
							gRecord.setGoodName(invtyDoc.getInvtyNm());
							gRecord.setGoodMode(invtyDoc.getSpcModel());
						}
					}
				}
				Integer id = goodRecordDao.selectByEcGoodIdAndSku(gRecord);
				gRecord.setId(id);
				gRecords1.add(gRecord);
			}
			int totalItem = resultJson.get("page").get("totalItem").asInt();
			if (pageSize * pageNo < totalItem) {
				// jdDownload(storeSettings, pageNo + 1, pageSize, userName, date,
				// goodRecordList);
				getJDSku(storeSettings, pageNo + 1, pageSize, oprator, date, gRecords1);
			}
		}
	}

	@Override
	public String uploadFile(MultipartFile file, String userID) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<GoodRecord> list = parseFile(file, userID);
			if (list.size()>0) {
				goodRecordDao.insertList(list);
			}
			message = "���������Ʒ�����ɹ������γɹ�����" + list.size() + "����";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "���������Ʒ�����쳣�������³��ԡ�";
			isSuccess = false;
		}
		try {
			resp = BaseJson.returnRespObj("ec/goodRecord/uploadFile", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	public List<GoodRecord> parseFile(MultipartFile file, String userID) {
        List<GoodRecord> goodrecordlist = new ArrayList<GoodRecord>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            InputStream fileIn = file.getInputStream();
            // ����ָ�����ļ�����������Excel�Ӷ�����Workbook����
            Workbook wb0 = new HSSFWorkbook(fileIn);

            // ��ȡExcel�ĵ��еĵ�һ����
            Sheet sht0 = wb0.getSheetAt(0);
            // ��õ�ǰsheet�Ŀ�ʼ��
            int firstRowNum = sht0.getFirstRowNum();
            // ��ȡ�ļ������һ��
            int lastRowNum = sht0.getLastRowNum();
            // ���������ֶκ��±�ӳ��
            SetColIndex(sht0.getRow(firstRowNum));
            // ��Sheet�е�ÿһ�н��е���
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                Row r = sht0.getRow(i);
                // �����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
                if (r.getRowNum() < 1) {
                    continue;
                }

                GoodRecord gRecord = new GoodRecord();
                gRecord.setEcGoodId(GetCellData(r, "������Ʒ����").trim());
                gRecord.setGoodSku((GetCellData(r, "������ƷSKU") == null ||
                        GetCellData(r, "������ƷSKU").equals("")) ?
                        null : GetCellData(r, "������ƷSKU").trim());

				if ( StringUtils.isEmpty(gRecord.getEcGoodId()) ){
					continue;
				}

                if (goodRecordDao.selectBySkuAndEcGoodId(gRecord.getGoodSku(), gRecord.getEcGoodId()) == null) {
                    gRecord.setStoreId(GetCellData(r, "���̱��"));
                    gRecord.setEcId(GetCellData(r, "ƽ̨ID"));
                    gRecord.setEcGoodName(GetCellData(r, "������Ʒ����"));
                    gRecord.setGoodId((GetCellData(r, "��Ӧ��Ʒ����") == null ||
                            GetCellData(r, "��Ӧ��Ʒ����").equals("")) ?
                            null : GetCellData(r, "��Ӧ��Ʒ����"));

//                    gRecord.setGoodName((GetCellData(r, "�����Ʒ����") == null ||
//                            GetCellData(r, "�����Ʒ����").equals("") ?
//                            null : GetCellData(r, "�����Ʒ����")));

                    gRecord.setGoodMode(GetCellData(r, "����ͺ�") == null ||
                            GetCellData(r, "����ͺ�").equals("") ? null :
                            GetCellData(r, "����ͺ�"));

                    gRecord.setUpsetPrice((new BigDecimal(GetCellData(r, "����ۼ�") == null || GetCellData(r, "����ۼ�").equals("") ? "0"
                            : GetCellData(r, "����ۼ�")).setScale(8, BigDecimal.ROUND_HALF_UP)));

                    gRecord.setSafeInv(GetCellData(r, "��ȫ���") == null ||
                            GetCellData(r, "��ȫ���").equals("") ? null :
                            GetCellData(r, "��ȫ���"));

                    gRecord.setSkuProp(GetCellData(r, "SKU����") == null ||
                            GetCellData(r, "SKU����").equals("") ? null :
                            GetCellData(r, "SKU����"));
                    gRecord.setOnlineStatus(GetCellData(r, "����״̬") == null ||
                            GetCellData(r, "����״̬").equals("") ? null :
                            GetCellData(r, "����״̬"));
                    gRecord.setIsSecSale((int) Double.parseDouble(GetCellData(r, "�Ƿ��������") == null ||
                            GetCellData(r, "�Ƿ��������").equals("") ? null :
                            GetCellData(r, "�Ƿ��������")));
                    gRecord.setMemo(GetCellData(r, "��ע") == null ||
                            GetCellData(r, "��ע").equals("") ? null :
                            GetCellData(r, "��ע"));
                    gRecord.setOperatTime(simpleDateFormat.format(new Date()));
                    gRecord.setOperator(userID);
                    goodrecordlist.add(gRecord);
                }
            }
            fileIn.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("����excel�����쳣��", e);
            goodrecordlist.clear();
        }
        return goodrecordlist;
    }

	private void pddDownload(StoreSettings storeSettings, int pageNo, int pageSize, String userName, String date,
			List<GoodRecord> goodRecordList) throws NoSuchAlgorithmException, IOException {

		// �̼�����Ƶ�Σ�500��/60��
//				"approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id,sold_quantity");
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);
		PddGoodsListGetRequest request = new PddGoodsListGetRequest();
		request.setPageSize(pageSize);// ��������
		request.setPage(pageNo);// ����ҳ��
		PddGoodsListGetResponse response = null;
		try {
			response = client.syncInvoke(request, accessToken);
//			System.out.println(JsonUtil.transferToJson(response));
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String pddRespStr = ECHelper.getPDD("pdd.goods.list.get", storeSettings, map);

		JSONObject pddObject = JSON.parseObject(JsonUtil.transferToJson(response));
		if (pddObject.containsKey("error_response")) {
			pddObject.getJSONObject("error_response").getString("error_msg");
			System.err.println("error_msg");
			return;
		}
		JSONObject pddtotal = pddObject.getJSONObject("goods_list_get_response");
		int totalItem = pddtotal.getIntValue("total_count");// total_count INTEGER ������Ʒ����
		JSONArray jsonArray = pddtotal.getJSONArray("goods_list");

		for (int i = 0; i < jsonArray.size(); i++) {
			// ���� jsonarray ���飬��ÿһ������ת�� json ����
			JSONObject job = jsonArray.getJSONObject(i);
			JSONArray jobArray = job.getJSONArray("sku_list");
			for (int j = 0; j < jobArray.size(); j++) {
				// ���� jsonarray ���飬��ÿһ������ת�� json ����
				JSONObject skuJob = jobArray.getJSONObject(j);

				GoodRecord goodRecord = new GoodRecord();
				goodRecord.setEcGoodId(job.getString("goods_id")); // (50) DEFAULT NULL 'ƽ̨��Ʒ���',
				goodRecord.setStoreId(storeSettings.getStoreId()); // (50) DEFAULT NULL '���̱��',
				goodRecord.setEcId("PDD"); // (50) DEFAULT NULL '����ƽ̨',
				goodRecord.setEcGoodName(job.getString("goods_name")); // (100) DEFAULT NULL 'ƽ̨��Ʒ����',
				goodRecord.setGoodSku(skuJob.getString("sku_id")); // (50) DEFAULT NULL '������Ʒsku',
				if(skuJob.getString("outer_id").length()>0) {
					System.out.println(skuJob.getString("outer_id"));
				}
				goodRecord.setGoodId(skuJob.getString("outer_id")); // (50) DEFAULT NULL '��Ʒ���',
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(skuJob.getString("outer_id"));
				if (invtyDoc != null) {
					goodRecord.setGoodName(invtyDoc.getInvtyNm());// '�����Ʒ����',
					goodRecord.setGoodMode(invtyDoc.getSpcModel());// '����ͺ�',
				}
//   ��	  goodRecord.setUpsetPrice(upsetPrice); 	//	 '����ۼ�',	
				goodRecord.setSafeInv("0"); // (50) DEFAULT NULL '��ȫ���',
				// sku_list �� spec STRING �������
				goodRecord.setSkuProp(skuJob.getString("spec")); // (50) DEFAULT NULL 'sku����',
				goodRecord.setOnlineStatus(skuJob.getIntValue("is_sku_onsale") == 0 ? "�¼���" : "����"); // (50) DEFAULT
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

		}

		if (pageSize * pageNo < totalItem) {
			pddDownload(storeSettings, pageNo + 1, pageSize, userName, date, goodRecordList);
		}

	}

	@Override
	public String exportList(Map map) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<Map> gList = goodRecordDao.exportList(map);
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/goodRecord/exportList", isSuccess, message, 1, 1, 1,
					1, 1, gList);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/queryList �쳣˵����", e);
		}
		return resp;
	}
}
