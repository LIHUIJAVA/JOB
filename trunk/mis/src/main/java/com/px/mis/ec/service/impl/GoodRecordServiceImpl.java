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
			message = "sku:" + goodRecord.getGoodSku() + "或spu:" + goodRecord.getEcGoodId() + "已存在，请重新输入！";
			isSuccess = false;

		} else {
			goodRecord.setOperatTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			goodRecordDao.insert(goodRecord);
			message = "新增成功！";
			isSuccess = true;
		}

		try {
			resp = BaseJson.returnRespObj("ec/goodRecord/add", isSuccess, message, goodRecord);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/add 异常说明：", e);
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
				message = "id" + goodRecord.getId() + "不存在，请重新修改！";
				isSuccess = false;
			} else {
				// System.err.println("\t\t:" +
				// goodRecordDao.selectById(goodRecord.getId()).getStoreName());
				goodRecordDao.update(goodRecord);
				message = "修改成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/goodRecord/edit", isSuccess, message, goodRecord);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/add 异常说明：", e);
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
			resp = BaseJson.returnRespObj("ec/goodRecord/delete", isSuccess, "删除成功", null);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/delete 异常说明：", e);
			try {
				resp = BaseJson.returnRespObj("ec/goodRecord/delete", isSuccess, "删除异常，请重试", null);
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
				message = "编号" + id + "不存在，请重新输入！";
				isSuccess = false;
				resp = BaseJson.returnRespObj("ec/goodRecord/query", isSuccess, message, null);
			} else {
				message = "查询成功";
				isSuccess = true;
				resp = BaseJson.returnRespObj("ec/goodRecord/query", isSuccess, message, ecPlatform);
			}
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/query 异常说明：", e);
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
			message = "查询成功";
			isSuccess = true;
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			int listNum = gList.size();
			int pages = count / pageSize + 1;
			resp = BaseJson.returnRespList("ec/goodRecord/queryList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, gList);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/queryList 异常说明：", e);
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
				message = "所选择的店铺不支持下载";
			}
			if (goodRecordList.size() == 0) {
				if (flag) {
					isSuccess = false;
					message = "下载完成，没有需要下载的商品！";
				}
			} else {
				goodRecordDao.download(goodRecordList);
				isSuccess = true;
				message = "更新成功，共更新" + goodRecordList.size() + "条商品信息！";
			}

			resp = BaseJson.returnRespObj("ec/goodRecord/download", isSuccess, message, null);
		} catch (Exception e) {
			logger.error("URL:ec/goodRecord/download 异常说明：", e);
			try {
				resp = BaseJson.returnRespObj("ec/goodRecord/download", isSuccess, "下载出现异常，请检查", null);
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

		// 转发
		Map<String, String> map = new HashMap<String, String>();
		map.put("path", ItemsOnsaleGetRequest.class.getName());
		map.put("taobaoObject", JSON.toJSONString(req));
		String taobao = ECHelper.getTB("", storeSettings, map);
		ItemsOnsaleGetResponse rsp = JSONObject.parseObject(taobao, ItemsOnsaleGetResponse.class);

		logger.info("天猫body:" + rsp.getBody());
		if (!rsp.isSuccess()) {
			logger.error("天猫err:" + rsp.getSubMsg());
			return;
		}
		if (rsp.getTotalResults() == 0) {
			logger.error("天猫商品数0");
			return;
		}

		StringBuffer sb = new StringBuffer();

		for (com.taobao.api.domain.Item item : rsp.getItems()) {
			sb.append(item.getNumIid()).append(",");
		}
		// 接口 批量 20 最大
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
		logger.info("天猫body:" + prsp.getBody());
		if (!prsp.isSuccess()) {
			logger.error("天猫err:" + prsp.getSubMsg());
			return;
		}

		for (com.taobao.api.domain.Item item : prsp.getItems()) {
			GoodRecord goodRecord = new GoodRecord();
			goodRecord.setEcGoodId(item.getNumIid().toString());// '平台商品编号',
			goodRecord.setEcGoodName(item.getTitle());// '平台商品名称',
			goodRecord.setGoodId(item.getOuterId());// '商品编号',
			goodRecord.setUpsetPrice(new BigDecimal(item.getPrice()==null?"0":item.getPrice()));// '最低售价',

			goodRecord.setEcId("TM");// '电商平台',
			InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(item.getOuterId());
			if (invtyDoc != null) {
//				`goodname`1	varchar(100		
				goodRecord.setGoodName(invtyDoc.getInvtyNm());// '存货商品名称',
				goodRecord.setGoodMode(invtyDoc.getSpcModel());// '规格型号',
			}
			goodRecord.setSafeInv("0");// '安全库存',
			goodRecord.setOnlineStatus("在售");// '线上状态',
			goodRecord.setIsSecSale(0);// '是否二销',
			goodRecord.setOperator(userName);// '操作人',
			goodRecord.setOperatTime(date);// '操作时间',

			if (item.getSkus() != null && item.getSkus().size() > 0) {
				for (com.taobao.api.domain.Sku sku : item.getSkus()) {
					GoodRecord goodRecords = new GoodRecord();
					goodRecord.setSkuProp(sku.getProperties());// sku属性
					goodRecord.setGoodSku(sku.getSkuId().toString());// 店铺商品sku
					Integer id = goodRecordDao.selectByEcGoodIdAnd(goodRecord);
					goodRecord.setId(id);
					logger.info("sku属性" + sku.getProperties());
					goodRecord.setStoreId(storeSettings.getStoreId());// '店铺编号',
					BeanUtils.copyProperties(goodRecord, goodRecords);
					goodRecordList.add(goodRecords);
				}
			} else {
				Integer id = goodRecordDao.selectByEcGoodIdAnd(goodRecord);
				goodRecord.setId(id);
				goodRecord.setStoreId(storeSettings.getStoreId());// '店铺编号',
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
//		// 接口 批量 20 最大
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
////			`id`1	int(11	'商品主键id',
////			`goodsku`0	varchar(50			'店铺商品sku',
////			`skuprop`0	varchar(50			'sku属性',
////			`memo` 	varchar(500			'备注',
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
		 * GoodRecord goodRecord = new GoodRecord(); // private String goodSku;//商品sku
		 * // private String ecGoodId;//平台商品编号
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
		 * goodRecord.setIsSecSale(0);//'是否二销',
		 */
//				    private BigDecimal upsetPrice;//最低售价
//				    private String safeInv;//安全库存
//				    private String skuProp;//sku属性
//				    private String onlineStatus;//线上状态

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
	 * 考拉店铺商品档案下载
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
		jdReq.put("item_edit_status", "5");//商品状态在售
		
		String jdRespStr = ECHelper.getKaola("kaola.item.batch.status.get", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		ArrayNode dataList = (ArrayNode) jdRespJson.get("kaola_item_batch_status_get_response").get("item_edit_list");
		for (Iterator<JsonNode> dataIterator = dataList.iterator(); dataIterator.hasNext();) {
			JsonNode data = dataIterator.next();
			//System.out.println("商品名称："+data.toString());
			GoodRecord gRecord = new GoodRecord();
			gRecord.setEcGoodId(data.get("sku_list").get(0).get("raw_sku").get("bar_code").asText());
			gRecord.setGoodSku("");//sku
			gRecord.setEcGoodName(data.get("raw_item_edit").get("name").asText());
			//System.out.println(data.get("sku_list").get(0).get("raw_sku").get("sale_price").asText());
			gRecord.setUpsetPrice(new BigDecimal(String.valueOf(data.get("sku_list").get(0).get("raw_sku").get("sale_price").asText())));
			
			gRecord.setEcId("KaoLa");
			gRecord.setStoreId(storeSettings.getStoreId());
			gRecord.setEcName("网易考拉");
			gRecord.setIsSecSale(0);
			gRecord.setOnlineStatus("在售");
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
		//System.out.println("返回结果："+jdRespStr);
		
		
	}
	
	

	private void getJDSku(StoreSettings storeSettings, int pageNo, int pageSize, String oprator, String date,
			List<GoodRecord> gRecords1) throws IOException, NoSuchAlgorithmException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageNo", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		// jdReq.put("skuStatuValue", "1");//下载上架的sku
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
				gRecord.setEcName("京东");
				gRecord.setUpsetPrice(new BigDecimal(String.valueOf(data.get("jdPrice").asText())));
				gRecord.setIsSecSale(0);
				if (data.get("status").asInt() == 1) {
					gRecord.setOnlineStatus("在售");
				} else {
					gRecord.setOnlineStatus("下架");
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
			message = "导入店铺商品档案成功，本次成功导入" + list.size() + "条。";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = "导入店铺商品档案异常，请重新尝试。";
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
            // 根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb0 = new HSSFWorkbook(fileIn);

            // 获取Excel文档中的第一个表单
            Sheet sht0 = wb0.getSheetAt(0);
            // 获得当前sheet的开始行
            int firstRowNum = sht0.getFirstRowNum();
            // 获取文件的最后一行
            int lastRowNum = sht0.getLastRowNum();
            // 设置中文字段和下标映射
            SetColIndex(sht0.getRow(firstRowNum));
            // 对Sheet中的每一行进行迭代
            for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
                Row r = sht0.getRow(i);
                // 如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (r.getRowNum() < 1) {
                    continue;
                }

                GoodRecord gRecord = new GoodRecord();
                gRecord.setEcGoodId(GetCellData(r, "店铺商品编码").trim());
                gRecord.setGoodSku((GetCellData(r, "店铺商品SKU") == null ||
                        GetCellData(r, "店铺商品SKU").equals("")) ?
                        null : GetCellData(r, "店铺商品SKU").trim());

				if ( StringUtils.isEmpty(gRecord.getEcGoodId()) ){
					continue;
				}

                if (goodRecordDao.selectBySkuAndEcGoodId(gRecord.getGoodSku(), gRecord.getEcGoodId()) == null) {
                    gRecord.setStoreId(GetCellData(r, "店铺编号"));
                    gRecord.setEcId(GetCellData(r, "平台ID"));
                    gRecord.setEcGoodName(GetCellData(r, "店铺商品名称"));
                    gRecord.setGoodId((GetCellData(r, "对应商品编码") == null ||
                            GetCellData(r, "对应商品编码").equals("")) ?
                            null : GetCellData(r, "对应商品编码"));

//                    gRecord.setGoodName((GetCellData(r, "存货商品名称") == null ||
//                            GetCellData(r, "存货商品名称").equals("") ?
//                            null : GetCellData(r, "存货商品名称")));

                    gRecord.setGoodMode(GetCellData(r, "规格型号") == null ||
                            GetCellData(r, "规格型号").equals("") ? null :
                            GetCellData(r, "规格型号"));

                    gRecord.setUpsetPrice((new BigDecimal(GetCellData(r, "最低售价") == null || GetCellData(r, "最低售价").equals("") ? "0"
                            : GetCellData(r, "最低售价")).setScale(8, BigDecimal.ROUND_HALF_UP)));

                    gRecord.setSafeInv(GetCellData(r, "安全库存") == null ||
                            GetCellData(r, "安全库存").equals("") ? null :
                            GetCellData(r, "安全库存"));

                    gRecord.setSkuProp(GetCellData(r, "SKU属性") == null ||
                            GetCellData(r, "SKU属性").equals("") ? null :
                            GetCellData(r, "SKU属性"));
                    gRecord.setOnlineStatus(GetCellData(r, "线上状态") == null ||
                            GetCellData(r, "线上状态").equals("") ? null :
                            GetCellData(r, "线上状态"));
                    gRecord.setIsSecSale((int) Double.parseDouble(GetCellData(r, "是否二次销售") == null ||
                            GetCellData(r, "是否二次销售").equals("") ? null :
                            GetCellData(r, "是否二次销售")));
                    gRecord.setMemo(GetCellData(r, "备注") == null ||
                            GetCellData(r, "备注").equals("") ? null :
                            GetCellData(r, "备注"));
                    gRecord.setOperatTime(simpleDateFormat.format(new Date()));
                    gRecord.setOperator(userID);
                    goodrecordlist.add(gRecord);
                }
            }
            fileIn.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.error("解析excel出现异常：", e);
            goodrecordlist.clear();
        }
        return goodrecordlist;
    }

	private void pddDownload(StoreSettings storeSettings, int pageNo, int pageSize, String userName, String date,
			List<GoodRecord> goodRecordList) throws NoSuchAlgorithmException, IOException {

		// 商家限流频次：500次/60秒
//				"approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id,sold_quantity");
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);
		PddGoodsListGetRequest request = new PddGoodsListGetRequest();
		request.setPageSize(pageSize);// 返回数量
		request.setPage(pageNo);// 返回页码
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
		int totalItem = pddtotal.getIntValue("total_count");// total_count INTEGER 返回商品总数
		JSONArray jsonArray = pddtotal.getJSONArray("goods_list");

		for (int i = 0; i < jsonArray.size(); i++) {
			// 遍历 jsonarray 数组，把每一个对象转成 json 对象
			JSONObject job = jsonArray.getJSONObject(i);
			JSONArray jobArray = job.getJSONArray("sku_list");
			for (int j = 0; j < jobArray.size(); j++) {
				// 遍历 jsonarray 数组，把每一个对象转成 json 对象
				JSONObject skuJob = jobArray.getJSONObject(j);

				GoodRecord goodRecord = new GoodRecord();
				goodRecord.setEcGoodId(job.getString("goods_id")); // (50) DEFAULT NULL '平台商品编号',
				goodRecord.setStoreId(storeSettings.getStoreId()); // (50) DEFAULT NULL '店铺编号',
				goodRecord.setEcId("PDD"); // (50) DEFAULT NULL '电商平台',
				goodRecord.setEcGoodName(job.getString("goods_name")); // (100) DEFAULT NULL '平台商品名称',
				goodRecord.setGoodSku(skuJob.getString("sku_id")); // (50) DEFAULT NULL '店铺商品sku',
				if(skuJob.getString("outer_id").length()>0) {
					System.out.println(skuJob.getString("outer_id"));
				}
				goodRecord.setGoodId(skuJob.getString("outer_id")); // (50) DEFAULT NULL '商品编号',
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(skuJob.getString("outer_id"));
				if (invtyDoc != null) {
					goodRecord.setGoodName(invtyDoc.getInvtyNm());// '存货商品名称',
					goodRecord.setGoodMode(invtyDoc.getSpcModel());// '规格型号',
				}
//   无	  goodRecord.setUpsetPrice(upsetPrice); 	//	 '最低售价',	
				goodRecord.setSafeInv("0"); // (50) DEFAULT NULL '安全库存',
				// sku_list 下 spec STRING 规格名称
				goodRecord.setSkuProp(skuJob.getString("spec")); // (50) DEFAULT NULL 'sku属性',
				goodRecord.setOnlineStatus(skuJob.getIntValue("is_sku_onsale") == 0 ? "下架中" : "在售"); // (50) DEFAULT
																										// NULL
																										// '线上状态',sku是否在架上，0-下架中，1-架上
				goodRecord.setOperator(userName); // (50) DEFAULT NULL '操作人',
				goodRecord.setOperatTime(date); // '操作时间',
				goodRecord.setIsSecSale(0);// '是否二销',
//   无	  goodRecord.setMemo(memo);	//	(500) DEFAULT NULL 	 '备注',
				Integer id = goodRecordDao.selectByEcGoodIdAndSku(goodRecord);
				goodRecord.setId(id); // '商品主键id',

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
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/goodRecord/exportList", isSuccess, message, 1, 1, 1,
					1, 1, gList);
		} catch (IOException e) {
			logger.error("URL:ec/goodRecord/queryList 异常说明：", e);
		}
		return resp;
	}
}
