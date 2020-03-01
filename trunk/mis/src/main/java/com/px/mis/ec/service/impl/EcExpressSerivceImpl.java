package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pdd.pop.sdk.http.api.request.PddCloudprintCustomaresGetRequest;
import com.pdd.pop.sdk.http.api.response.PddCloudprintCustomaresGetResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddCloudprintStdtemplatesGetRequest;
import com.pdd.pop.sdk.http.api.request.PddLogisticsCompaniesGetRequest;
import com.pdd.pop.sdk.http.api.request.PddWaybillSearchRequest;
import com.pdd.pop.sdk.http.api.response.PddCloudprintStdtemplatesGetResponse;
import com.pdd.pop.sdk.http.api.response.PddCloudprintStdtemplatesGetResponse.InnerPddCloudprintStdtemplatesGetResponseResultDatasItem;
import com.pdd.pop.sdk.http.api.response.PddCloudprintStdtemplatesGetResponse.InnerPddCloudprintStdtemplatesGetResponseResultDatasItemStandardTemplatesItem;
import com.pdd.pop.sdk.http.api.response.PddLogisticsCompaniesGetResponse;
import com.pdd.pop.sdk.http.api.response.PddLogisticsCompaniesGetResponse.LogisticsCompaniesGetResponseLogisticsCompaniesItem;
import com.pdd.pop.sdk.http.api.response.PddWaybillSearchResponse;
import com.px.mis.ec.dao.EcExpressDao;
import com.px.mis.ec.dao.EcExpressSettingsDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.dao.WhsPlatExpressMappDao;
import com.px.mis.ec.entity.EcExpress;
import com.px.mis.ec.entity.EcExpressSettings;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.entity.WhsPlatExpressMapp;
import com.px.mis.ec.service.EcExpressService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.taobao.api.ApiException;
import com.taobao.api.request.CainiaoCloudprintCustomaresGetRequest;
import com.taobao.api.request.CainiaoCloudprintMystdtemplatesGetRequest;
import com.taobao.api.request.CainiaoWaybillIiSearchRequest;
import com.taobao.api.response.CainiaoCloudprintCustomaresGetResponse;
import com.taobao.api.response.CainiaoCloudprintCustomaresGetResponse.CustomAreaResult;
import com.taobao.api.response.CainiaoCloudprintMystdtemplatesGetResponse;
import com.taobao.api.response.CainiaoCloudprintMystdtemplatesGetResponse.UserTemplateDo;
import com.taobao.api.response.CainiaoCloudprintMystdtemplatesGetResponse.UserTemplateResult;
import com.taobao.api.response.CainiaoWaybillIiSearchResponse;
import com.taobao.api.response.CainiaoWaybillIiSearchResponse.AddressDto;
import com.taobao.api.response.CainiaoWaybillIiSearchResponse.WaybillApplySubscriptionInfo;
import com.taobao.api.response.CainiaoWaybillIiSearchResponse.WaybillBranchAccount;

@Service
@Transactional
public class EcExpressSerivceImpl implements EcExpressService {
	@Autowired
	private EcExpressDao ecExpressDao;
	@Autowired
	private StoreSettingsDao storeSettingsDao;
	@Autowired
	private EcExpressSettingsDao ecExpressSettingsDao;

	@Override
	public String delete(String platId) {
		// TODO Auto-generated method stub
		String resp = "";
		try {
			ecExpressDao.delete(platId);
			resp = BaseJson.returnRespList("/ec/ecExpress/delete", true, "删除成功", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				resp = BaseJson.returnRespList("/ec/ecExpress/delete", false, "删除时异常", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	@Override
	public String selectList(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insertList(List<EcExpress> expresslist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String download(String platId) {
		// TODO Auto-generated method stub
		String resp = "";
		EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(platId);
		if(ecExpressSettings==null) {
			try {
				return resp = BaseJson.returnRespObj("ec/ecExpress/download", true, "请先设置该平台开通电子面单的店铺", null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//该平台不支持电子面单更新
		}
		StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
		try {
			if (storeSettings!=null) {
				if (StringUtils.isNotEmpty(storeSettings.getAppKey())) {
					if ("TM".equals(platId)) {
						try {
							resp = tmDownloadYun(platId, storeSettings);
						} catch (ApiException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						//				resp = tmDownload(platId, storeSettings);
					} else if ("JD".equals(platId)) {
						resp = jdDownload(platId, storeSettings);

					} else if ("PDD".equals(platId)) {
						resp = PDDDownload(platId, storeSettings);

					} else {
						resp = BaseJson.returnRespObj("ec/ecExpress/download", true, "该平台不支持电子面单更新", null);//该平台不支持电子面单更新
					} 
				}else {
					resp = BaseJson.returnRespObj("ec/ecExpress/download", true, "更新失败，对应店铺接口信息未设置", null);//未设置店铺接口
				}
			}else {
				resp = BaseJson.returnRespObj("ec/ecExpress/download", true, "更新失败，该平台未找到开通电子面单对应店铺", null);//未找到对应店铺
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	private String jdDownload(String platId, StoreSettings storeSettings) {

		ObjectNode objectNode;
		String resp = "";
		String message = "下载完成!";
		boolean success = true;
		try {
			objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("vendorCode", storeSettings.getVenderId());
			String json = objectNode.toString();

			String jdRespStr;
			jdRespStr = ECHelper.getJD("jingdong.ldop.alpha.provider.sign.success", storeSettings, json);
			//System.out.println(jdRespStr);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			// 判断下载是否出现错误，有错误时直接返回。
			if (jdRespJson.has("error_response")) {
				message = jdRespJson.get("error_response").get("zh_desc").asText();
				success = false;
				resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
				return resp;
			}
			JsonNode resultJson = jdRespJson.get("jingdong_ldop_alpha_provider_sign_success_responce")
					.get("resultInfo");
			if (resultJson.get("statusCode").asInt() == 0) {
				List<EcExpress> expresslist = new ArrayList<EcExpress>();
				ArrayNode expressinfoList = (ArrayNode) resultJson.get("data");
				for (Iterator<JsonNode> orderInfoIterator = expressinfoList.iterator(); orderInfoIterator.hasNext();) {
					JsonNode expressinfo = orderInfoIterator.next();
					EcExpress express = new EcExpress();
					/*
					 * private String platId;//平台编号 private String providerCode;//承运商编码 private int
					 * providerId;//承运商id private String providerName;//承运商名称 private String
					 * providerType;//承运商类型 private String branchCode;//网点编码 private String
					 * branchName;//网点名称
					 */
					express.setPlatId(platId);
					express.setProviderCode(expressinfo.get("providerCode").asText());
					express.setProviderId(expressinfo.get("providerId").asInt());
					express.setProviderName(expressinfo.get("providerName").asText());
					express.setProviderType(expressinfo.get("providerType").asText());
					if (expressinfo.has("amount")) {
						express.setAmount(expressinfo.get("amount").asInt());
					} else {
						express.setAmount(0);
					}
					express.setBranchCode(expressinfo.get("branchCode").asText());
					express.setBranchName(expressinfo.get("branchName").asText());
					express.setCityId(expressinfo.get("address").get("cityId").asText());
					express.setCityName(expressinfo.get("address").get("cityName").asText());
					express.setProvinceId(expressinfo.get("address").get("provinceId").asText());
					express.setProvinceName(expressinfo.get("address").get("provinceName").asText());
					express.setCountryId(expressinfo.get("address").get("countryId").asText());
					express.setCountryName(expressinfo.get("address").get("countryName").asText());
					express.setCountrysideId(expressinfo.get("address").get("countrysideId").asText());
					express.setCountrysideName(expressinfo.get("address").get("countrysideName").asText());
					express.setAddress(expressinfo.get("address").get("address").asText());
					expresslist.add(express);
				}
				ecExpressDao.delete(platId);
				ecExpressDao.insert(expresslist);
				success = true;
				message = "下载更新成功";
				resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
			} else {
				message = resultJson.get("statusMessage").asText();
				success = false;
				resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
				return resp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	private String tmDownload(String platId, StoreSettings storeSettings) throws NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub
		String message;
		boolean success;
		String resp = "";
		Map<String, String> map = new HashMap<String, String>();
		try {

		//请求参数
		map.put("waybill_apply_request", "{'cp_code':''}");
		String jdRespStr = ECHelper.getTB("taobao.wlb.waybill.i.search", storeSettings, map);
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		
//		出错
		if (jdRespJson.has("error_response")) {
			message = jdRespJson.get("error_response").get("sub_msg").asText();
			success = false;
			resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
			return resp;
		}
		ArrayNode arrayNode = (ArrayNode) jdRespJson.get("wlb_waybill_i_search_response").get("subscribtions")
				.get("waybill_apply_subscription_info");
		List<EcExpress> expresslist = new ArrayList<EcExpress>();

		for (Iterator<JsonNode> orderInfoIterator = arrayNode.iterator(); orderInfoIterator.hasNext();) {
			JsonNode node = orderInfoIterator.next();
			ArrayNode arrayNodes = (ArrayNode) node.get("branch_account_cols").get("waybill_branch_account");
			for (Iterator<JsonNode> orderInfo = arrayNodes.iterator(); orderInfo.hasNext();) {
				JsonNode nodes = orderInfo.next();
				ArrayNode arrayNodesd = (ArrayNode) nodes.get("shipp_address_cols").get("waybill_address");
				for (Iterator<JsonNode> orderInfod = arrayNodesd.iterator(); orderInfod.hasNext();) {
					JsonNode nodesd = orderInfod.next();
					EcExpress express = new EcExpress();

					express.setPlatId(platId); // (200
					express.setProviderCode(node.get("cp_code").asText()); // (255 '承运商编码',
//					express.setProviderId();	//(11			'承运商id',
					Map map1 = ecExpressDao.exSelect(node.get("cp_code").asText());
					express.setProviderName(map1.get("express_name").toString()); // (255 '承运商名称',
					express.setProviderType(node.get("cp_type").asText()); // (255 '承运商类型',
					express.setBranchCode(nodes.get("branch_code").asText()); // (255 '网点编码',
					express.setBranchName(nodes.get("branch_name").asText()); // (255 '网点名称',
					express.setAmount(nodes.get("quantity").asInt()); // (11 '剩余单号',
//					express.setProvinceId();	//(255		
					express.setProvinceName(nodesd.get("province").asText()); // (255
//					express.setCityId();	//(255		
					express.setCityName(nodesd.get("city").asText()); // (255
//					express.setCountryId();	//(255		
					express.setCountryName(nodesd.get("area").asText()); // (255
//					express.setCountrysideId();	//(255		
					express.setCountrysideName(nodesd.get("town") == null ? null : nodesd.get("town").asText()); // (255
					express.setAddress(nodesd.get("address_detail").asText()); // (255 '详细地址',
					expresslist.add(express);
				}

			}
		}
		ecExpressDao.delete(platId);
		ecExpressDao.insert(expresslist);
		success = true;
		message = "下载更新成功";
		resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
		}catch (Exception e){
			success = false;
			message = "下载更新失败";
			resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
		}
		return resp;

	}

	private String PDDDownload(String platId, StoreSettings storeSettings)
			throws NoSuchAlgorithmException, Exception {
		// TODO Auto-generated method stub
		String message;
		boolean success;
		String resp = "";

		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();

		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddWaybillSearchRequest request = new PddWaybillSearchRequest();
//        request.setWpCode("str");
		PddWaybillSearchResponse response = null;
		response = client.syncInvoke(request, accessToken);

		System.err.println(JsonUtil.transferToJson(response));
		JSONObject object = JSONObject.parseObject(JsonUtil.transferToJson(response));
		if (object.containsKey("error_response")) {
			System.err.println(object.getJSONObject("error_response").getString("error_msg"));
			resp = BaseJson.returnRespObj("ec/ecExpress/download", false, object.getJSONObject("error_response").getString("error_msg"), null);

			return resp;
		}
		JSONArray waybill_apply_subscription_cols = object.getJSONObject("pdd_waybill_search_response")
				.getJSONArray("waybill_apply_subscription_cols");
		List<EcExpress> expresslist = new ArrayList<EcExpress>();

		for (int i = 0; i < waybill_apply_subscription_cols.size(); i++) {
			JSONObject branch_account_cols = waybill_apply_subscription_cols.getJSONObject(i);
			JSONArray branch_account_list = branch_account_cols.getJSONArray("branch_account_cols");
			for (int j = 0; j < branch_account_list.size(); j++) {
				JSONObject branch_account = branch_account_list.getJSONObject(j);
				JSONArray shipp_address_list = branch_account.getJSONArray("shipp_address_cols");
				for (int l = 0; l < shipp_address_list.size(); l++) {
					JSONObject shipp_address = shipp_address_list.getJSONObject(l);
					EcExpress express = new EcExpress();

					express.setPlatId(platId); // (200
					express.setProviderCode(branch_account_cols.getString("wp_code")); // (255 '承运商编码',
					Map map = ecExpressDao.exSelect(branch_account_cols.getString("wp_code"));
					express.setProviderName(map.get("express_name").toString()); // (255 '承运商名称',
					express.setProviderType(branch_account_cols.getString("wp_type")); // (255 '承运商类型',
					express.setBranchCode(branch_account.getString("branch_code")); // (255 '网点编码',
					express.setBranchName(branch_account.getString("branch_name")); // (255 '网点名称',
					express.setAmount(branch_account.getInteger("quantity")); // (11 '剩余单号',
//					express.setProvinceId();	//(255		
					express.setProvinceName(shipp_address.getString("province")); // (255
//					express.setCityId();	//(255		
					express.setCityName(shipp_address.getString("city")); // (255
//					express.setCountryId();	//(255		
					express.setCountryName(shipp_address.getString("district")); // (255
//					express.setCountrysideId();	//(255		
//					express.setCountrysideName(); // (255
					express.setAddress(shipp_address.getString("detail")); // (255 '详细地址',
					expresslist.add(express);

				}

			}

		}


        PddLogisticsCompaniesGetRequest requesta = new PddLogisticsCompaniesGetRequest();
        PddLogisticsCompaniesGetResponse responsea = client.syncInvoke(requesta);
		if (responsea.getErrorResponse() != null) {
			resp = BaseJson.returnRespObj("ec/ecExpress/download", false, responsea.getErrorResponse().getErrorMsg(), null);

			return resp;
		}
		for (LogisticsCompaniesGetResponseLogisticsCompaniesItem companiesItem : responsea
				.getLogisticsCompaniesGetResponse().getLogisticsCompanies()) {
			for( EcExpress express:expresslist) {
				if(companiesItem.getCode().equals(express.getProviderCode())) {
					express.setProviderId(companiesItem.getId().intValue());	//(11			'承运商id',
				}

			}
		}
		ecExpressDao.delete(platId);
		ecExpressDao.insert(expresslist);
		success = true;
		message = "下载更新成功";
		resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);

		PddCloudprintStdtemplatesGetRequest request2 = new PddCloudprintStdtemplatesGetRequest();
		PddCloudprintStdtemplatesGetResponse response2;

		response2 = client.syncInvoke(request2);
		if (response2.getErrorResponse() != null) {
			System.err.println(response2.getErrorResponse().getErrorMsg());
			resp = BaseJson.returnRespObj("ec/ecExpress/download", false, response2.getErrorResponse().getErrorMsg(), null);
			return resp;
		}
		for (InnerPddCloudprintStdtemplatesGetResponseResultDatasItem innerPddCloudprintStdtemplatesGetResponseResultDatasItem : response2
				.getPddCloudprintStdtemplatesGetResponse().getResult().getDatas()) {
			String whsCode = innerPddCloudprintStdtemplatesGetResponseResultDatasItem.getWpCode();
			if(whsCode.equals("JD")) {
				whsCode="JDWL";
			}
			List<WhsPlatExpressMapp> list = whsPlatExpressMappDao.selectCloudPrint("PDD", whsCode);
			for (InnerPddCloudprintStdtemplatesGetResponseResultDatasItemStandardTemplatesItem standardTemplates : innerPddCloudprintStdtemplatesGetResponseResultDatasItem
					.getStandardTemplates()) {
				if (standardTemplates.getStandardWaybillType() != null
						&& standardTemplates.getStandardWaybillType().equals(1)) {
					String cloudPrint = standardTemplates.getStandardTemplateUrl();
					if (list.size() > 0) {
						PddCloudprintCustomaresGetRequest   cloudPrintRequest = new PddCloudprintCustomaresGetRequest();
						cloudPrintRequest.setTemplateId(standardTemplates.getStandardTemplateId()+"");
						PddCloudprintCustomaresGetResponse  cloudPrintResponse=null;
						try {
							cloudPrintResponse =client.syncInvoke(cloudPrintRequest,accessToken);

						if (cloudPrintResponse.getErrorResponse() != null) {
							resp = BaseJson.returnRespObj("ec/ecExpress/download", false, cloudPrintResponse.getErrorResponse().getErrorMsg(), null);
							return resp;
						}
							String 	cloudPrintResponseSrt=	JsonUtil.transferToJson(cloudPrintResponse);
							JSONObject cloudPrintObject = JSONObject.parseObject(cloudPrintResponseSrt);
							JSONObject  pdd_cloudprint_customares_get_response =cloudPrintObject.getJSONObject("pdd_cloudprint_customares_get_response");
							JSONObject result=  pdd_cloudprint_customares_get_response.getJSONObject("result") ;
							JSONArray cloudPrintArray 	=result.getJSONArray("datas");
							if(cloudPrintArray.size()==0){
								whsPlatExpressMappDao.updateCloudPrint(list, cloudPrint,null);
							}else{
							for(int i=0;i<cloudPrintArray.size();i++ ){
								String str= cloudPrintArray.getJSONObject(i).getString("custom_area_url");
							whsPlatExpressMappDao.updateCloudPrint(list, cloudPrint,str);
								}
							}
						}catch (Exception e){
//							throw new RuntimeException(whsCode+"没有设置自定义区域模板");
							whsPlatExpressMappDao.updateCloudPrint(list, cloudPrint,null);
							resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
						}
					}
				}

			}
		}

		return resp;

	}

	@Override
	public String selectList(String jsonBody) {
		// TODO Auto-generated method stub
		String message = "";
		boolean isSuccess;
		String resp = "";
		List<EcExpress> expresses = new ArrayList<EcExpress>();
		try {
			Map map = JacksonUtil.getMap(BaseJson.getReqBody(jsonBody).toString());
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			map.put("index", (pageNo - 1) * pageSize);
			map.put("num", pageSize);
			expresses = ecExpressDao.selectList(map);
			int count = ecExpressDao.selectCount(map);
			isSuccess = true;
			message = "查询成功";
			resp = BaseJson.returnRespList("ec/ecExpress/queryList", isSuccess, message, count, pageNo, pageSize, 0, 0,
					expresses);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess = false;
			message = "解析参数时或查询发生异常，请重试。";
			try {
				resp = BaseJson.returnRespList("ec/ecExpress/queryList", isSuccess, message, 1, 1, 1, 0, 0, expresses);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return resp;
	}

	private String tmDownloadYun(String platId, StoreSettings storeSettings)
			throws NoSuchAlgorithmException, IOException, ApiException {
		// TODO Auto-generated method stub
		String message;
		boolean success;
		String resp = "";
try {
//		req.setCpCode("a");

		CainiaoWaybillIiSearchRequest req = new CainiaoWaybillIiSearchRequest();

		// 转发
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("path", CainiaoWaybillIiSearchRequest.class.getName());
		maps.put("taobaoObject", JSON.toJSONString(req));
		String taobao = ECHelper.getTB("", storeSettings, maps);
		CainiaoWaybillIiSearchResponse rsp = JSONObject.parseObject(taobao, CainiaoWaybillIiSearchResponse.class);

		if (!rsp.isSuccess()) {
			message = rsp.getSubMsg();
			success = false;
			resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
			return resp;
		}

		List<EcExpress> expresslist = new ArrayList<EcExpress>();

		for (WaybillApplySubscriptionInfo waybillApplySubscriptionInfo : rsp.getWaybillApplySubscriptionCols()) {
			for (WaybillBranchAccount waybillBranchAccount : waybillApplySubscriptionInfo.getBranchAccountCols()) {
				for (AddressDto addressDto : waybillBranchAccount.getShippAddressCols()) {

					EcExpress express = new EcExpress();

					express.setPlatId(platId); // (200
					express.setProviderCode(waybillApplySubscriptionInfo.getCpCode()); // (255 '承运商编码',
//				express.setProviderId();	//(11			'承运商id',
					Map map = ecExpressDao.exSelect(waybillApplySubscriptionInfo.getCpCode());
					express.setProviderName(map.get("express_name").toString()); // (255 '承运商名称',
					express.setProviderType(waybillApplySubscriptionInfo.getCpType().toString()); // (255 '承运商类型',
					express.setBranchCode(waybillBranchAccount.getBranchCode()); // (255 '网点编码',
					express.setBranchName(waybillBranchAccount.getBranchName()); // (255 '网点名称',
					express.setAmount(waybillBranchAccount.getQuantity().intValue());// (11 '剩余单号',
//				express.setProvinceId();	//(255		
					express.setProvinceName(addressDto.getProvince()); // (255
//				express.setCityId();	//(255		
					express.setCityName(addressDto.getCity()); // (255
//				express.setCountryId();	//(255		
					express.setCountryName(addressDto.getDistrict()); // (255
//				express.setCountrysideId();	//(255		
					express.setCountrysideName(addressDto.getTown()); // (255
					express.setAddress(addressDto.getDetail()); // (255 '详细地址',
					expresslist.add(express);

				}
			}

		}
		ecExpressDao.delete(platId);
		ecExpressDao.insert(expresslist);
		success = true;
		message = "下载更新成功";
		resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
		CainiaoCloudprintMystdtemplatesGetRequest creq = new CainiaoCloudprintMystdtemplatesGetRequest();
		// 转发
		Map<String, String> cmaps = new HashMap<String, String>();
		cmaps.put("path", CainiaoCloudprintMystdtemplatesGetRequest.class.getName());
		cmaps.put("taobaoObject", JSON.toJSONString(creq));
		String ctaobao = ECHelper.getTB("", storeSettings, cmaps);
		CainiaoCloudprintMystdtemplatesGetResponse crsp = JSONObject.parseObject(ctaobao,
				CainiaoCloudprintMystdtemplatesGetResponse.class);
		if (!crsp.isSuccess()) {
			message = crsp.getSubMsg();
			success = false;
			resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
			return resp;
		}
		if (!crsp.getResult().getSuccess()) {
			message = crsp.getResult().getErrorMessage();
			success = false;
			resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
			return resp;

		}
		for (UserTemplateResult userTemplateResult : crsp.getResult().getDatas()) {
			String whsCode=userTemplateResult.getCpCode();
			List<WhsPlatExpressMapp> list=	whsPlatExpressMappDao.selectCloudPrint("TM", whsCode);
			for (UserTemplateDo userTemplateDo : userTemplateResult.getUserStdTemplates()) {
				String cloudPrint =userTemplateDo.getUserStdTemplateUrl();
				if (list.size() > 0) {
					CainiaoCloudprintCustomaresGetRequest reqCloudprintCustomaresGetRequest = new CainiaoCloudprintCustomaresGetRequest();
					System.err.println("sfdasf \t"+userTemplateDo.getUserStdTemplateId());
					reqCloudprintCustomaresGetRequest.setTemplateId(userTemplateDo.getUserStdTemplateId());
					// 转发
					Map<String, String> cmapsMap = new HashMap<String, String>();
					cmapsMap.put("path", CainiaoCloudprintCustomaresGetRequest.class.getName());
					cmapsMap.put("taobaoObject", JSON.toJSONString(reqCloudprintCustomaresGetRequest));
					String ctaobaoString = ECHelper.getTB("", storeSettings, cmapsMap);
					CainiaoCloudprintCustomaresGetResponse crspResponse = JSONObject.parseObject(ctaobaoString,
							CainiaoCloudprintCustomaresGetResponse.class);
					if(!crspResponse.isSuccess()) {
						message = crsp.getSubMsg();
						success = false;
						resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
						return resp;
					}
					if (!crspResponse.getResult().getSuccess()) {
						message = crsp.getResult().getErrorMessage();
						success = false;
						resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
						return resp;
					}
					for (CustomAreaResult areaResult:crspResponse.getResult().getDatas()) {
					String string=	areaResult.getCustomAreaUrl();
						whsPlatExpressMappDao.updateCloudPrint(list, cloudPrint,string);
					}
					
				}
			}
		}
		}catch (Exception e){
			message = "下载失败";
			success = false;
			resp = BaseJson.returnRespObj("ec/ecExpress/download", success, message, null);
		}
		return resp;

	}
	@Autowired
	WhsPlatExpressMappDao  whsPlatExpressMappDao;
}
