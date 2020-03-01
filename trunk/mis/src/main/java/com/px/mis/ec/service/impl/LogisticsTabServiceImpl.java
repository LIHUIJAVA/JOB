package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.px.mis.util.CommonUtil;
import com.px.mis.util.poiTool;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maidu.api.entity.custom.XS_OrdersGetListResponse;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;
import com.pdd.pop.sdk.common.util.JsonUtil;
import com.pdd.pop.sdk.http.PopAccessTokenClient;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddLogisticsOnlineSendRequest;
import com.pdd.pop.sdk.http.api.request.PddWaybillCancelRequest;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequest;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestSender;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestSenderAddress;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemOrderInfo;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfo;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfoItemsItem;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemRecipient;
import com.pdd.pop.sdk.http.api.request.PddWaybillGetRequest.ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemRecipientAddress;
import com.pdd.pop.sdk.http.api.response.PddLogisticsOnlineSendResponse;
import com.pdd.pop.sdk.http.api.response.PddWaybillCancelResponse;
import com.pdd.pop.sdk.http.api.response.PddWaybillGetResponse;
import com.pdd.pop.sdk.http.api.response.PddWaybillGetResponse.InnerPddWaybillGetResponseModulesItem;
import com.pdd.pop.sdk.http.token.AccessTokenResponse;
import com.px.mis.ec.dao.EcExpressDao;
import com.px.mis.ec.dao.EcExpressSettingsDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.LogisticsTabDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.dao.WhsPlatExpressMappDao;
import com.px.mis.ec.entity.EcExpress;
import com.px.mis.ec.entity.EcExpressSettings;
import com.px.mis.ec.entity.ExpressCodeAndName;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.LogisticsTab;
import com.px.mis.ec.entity.LogisticsTabExport;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.entity.WhsPlatExpressMapp;
import com.px.mis.ec.service.LogisticsTabService;
import com.px.mis.ec.util.ECHelper;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.SellSnglSubDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.ExpressCorpMapper;
import com.px.mis.whs.entity.ExpressCorp;
import com.suning.api.entity.custom.DeliverynewAddResponse;
import com.suning.api.exception.SuningApiException;
import com.taobao.api.ApiException;
import com.taobao.api.request.CainiaoWaybillIiCancelRequest;
import com.taobao.api.request.CainiaoWaybillIiGetRequest;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.AddressDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.Item;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.OrderInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.PackageInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.TradeOrderInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.UserInfoDto;
import com.taobao.api.request.CainiaoWaybillIiGetRequest.WaybillCloudPrintApplyNewRequest;
import com.taobao.api.request.LogisticsOnlineSendRequest;
import com.taobao.api.response.CainiaoWaybillIiCancelResponse;
import com.taobao.api.response.CainiaoWaybillIiGetResponse;
import com.taobao.api.response.LogisticsOnlineSendResponse;

@Service
@Transactional
public class LogisticsTabServiceImpl extends poiTool implements LogisticsTabService {
	@Autowired
	private LogisticsTabDao logisticsTabDao;
	@Autowired
	private StoreSettingsDao storeSettingsDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private EcExpressSettingsDao ecExpressSettingsDao;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private ExpressCorpMapper expressCorpDao;
	@Autowired
	private EcExpressDao ecExpressDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private WhsPlatExpressMappDao whsPlatExpressMappDao;
	@Autowired
	private InvtyDocDao invtyDocDao;

	@Autowired
	private SellSnglSubDao sellSnglSubDao;
	@Autowired
	private MisUserDao misUserDao;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private PlatOrderMaiDu platOrderMaiDu;
	@Autowired
	private PlatOrderSN platOrderSN;
	@Autowired
	PlatOrderXMYP platOrderXMYP;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String insert(LogisticsTab logisticsTab) {
		String resp = "";
		logisticsTabDao.insert(logisticsTab);
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/insert", true, "新增成功！", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String update(LogisticsTab logisticsTab) {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		if (logisticsTabDao.select(logisticsTab.getOrdrNum()) == null) {
			isSuccess = false;
			message = "序号" + logisticsTab.getOrdrNum() + "不存在，更新失败！";
		} else {
			logisticsTabDao.update(logisticsTab);
			message = "更新成功！";
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/update", isSuccess, message, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String delete(String ordrNum,String accNum) {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		int successCount=0;
		int fallCount=0;
		String[] ordrNums = ordrNum.split(",");
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < ordrNums.length; i++) {
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(ordrNums[i]));
			if (ordrNum == null || logisticsTab == null) {
				fallCount++;
			} else {
				if (logisticsTab.getIsShip() == 1) {
					message = "物流单已发货，不能删除";
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(20);// 20物流表删除
					logRecord.setTypeName("物流表删除");
					logRecord.setOperatOrder(logisticsTab.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallCount++;
				} else {
					logisticsTabDao.delete(Integer.parseInt(ordrNums[i]));
					message = "删除成功";
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(20);// 20物流表删除
					logRecord.setTypeName("物流表删除");
					logRecord.setOperatOrder(logisticsTab.getEcOrderId());
					logRecordDao.insert(logRecord);
					//message = "删除成功！";
					successCount++;
				}

			} 
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/delete", true,
					"删除成功，本次成功删除" + successCount + "单，失败" + fallCount + "单", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String select(Integer ordrNum) {
		String message = "";
		boolean isSuccess = true;
		String resp = "";
		LogisticsTab logisticsTab = logisticsTabDao.select(ordrNum);
		if (logisticsTab == null) {
			message = "编号为" + ordrNum + "的物流表不存在，查询单个失败。";
			isSuccess = false;
		} else {
			message = "查询单个成功！！！";
			isSuccess = true;
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/select", isSuccess, message, logisticsTab);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String selectList(Map map) {
		String resp = "";
		map.put("whsId", CommonUtil.strToList((String)map.get("whsId")));
		List<Map> selectList = logisticsTabDao.selectList(map);
		int count = logisticsTabDao.selectCount(map);
		int pageNo = Integer.parseInt(map.get("pageNo").toString());
		int pageSize = Integer.parseInt(map.get("pageSize").toString());
		int listNum = selectList.size();
		int pages = count / pageSize + 1;
		try {
			resp = BaseJson.returnRespList("ec/logisticsTab/selectList", true, "分页查询成功！", count, pageNo, pageSize,
					listNum, pages, selectList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String insertList(List<LogisticsTab> logisticsTabList) {
		String resp = "";
		logisticsTabDao.insertList(logisticsTabList);
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/insertList", true, "批量成功！", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String achieveECExpressOrder(String logisticsNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		int successcount = 0;
		int fallcount = 0;
		String[] nums = logisticsNum.split(",");
		String message = "";
		List<String> list = new ArrayList<>();
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以获取电子面单
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsShip()==1) {
				//已发货不能取电子面单
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("物流表已发货，不能取电子面单");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(StringUtils.isNotEmpty(logisticsTab.getExpressCode())) {
				//已有面单号不能取电子面单
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("物流表已分配单号，不能重复取电子面单");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			String orderId = logisticsTab.getOrderId();
			// 根据物流表的电商订单号获取电商订单信息
			PlatOrder platOrder = platOrderDao.select(orderId);
			// 根据订单的店铺ID获取店铺档案
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// 根据店铺档案获取订单平台ID
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// 获取对应平台所开通电子面单的店铺设置
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：请设置当前平台开通电子面单的店铺");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			// 获取快递公司档案信息
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			// 获取平台设置的电子面单快递公司信息
			EcExpress ecExpress=null;
			try {
				ecExpress = ecExpressDao.select(storeRecord.getEcId(), expressCorp.getCompanyCode(),
						expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),expressCorp.getDetailedAddress());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递公司档案对应电子面单账户信息重复，请检查");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(ecExpress==null) {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递公司档案设置错误，请检查");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if (storeRecord.getEcId().equals("JD")) {
				// 调用京东电子面单接口获取电子面单
				String result = JDexpressOrder(storeSettings, ecExpress, platOrder, expressCorp,
						ecExpressSettings.getVenderId());
				// 解析返回结果
				try {
					ObjectNode jdRespJson = JacksonUtil.getObjectNode(result);
					if (jdRespJson.toString().length() != 0) {
						if (jdRespJson.get("statusCode").asText().equals("-1")) {
							// 返回错误时输出错误提示
							//System.out.println("获取面单错误提示：" + jdRespJson.get("statusMessage").asText());
							message = jdRespJson.get("statusMessage").asText();
							fallcount++;
						} else {
							JsonNode resultJson = jdRespJson.get("data");
							// 获取电子面单成功
							ArrayNode codes = (ArrayNode) resultJson.get("waybillCodeList");
							String expressCode = "";
							for (Iterator<JsonNode> itemInfoInfoIterator = codes.iterator(); itemInfoInfoIterator
									.hasNext();) {
								JsonNode itemInfo = itemInfoInfoIterator.next();
								expressCode = itemInfo.asText();
							}
							// 保存获取到的电子面单号
							logisticsTab.setExpressCode(expressCode);
							// 获取大头笔等信息
							String bigShotInfomation = JDBigShot(storeSettings, expressCode, ecExpress.getProviderId(),
									ecExpress.getProviderCode());
							ObjectNode infomation = JacksonUtil.getObjectNode(bigShotInfomation);
							if (infomation.toString().length() != 0) {
								if (!infomation.get("jingdong_ldop_alpha_vendor_bigshot_query_responce").get("code")
										.asText().equals("0")) {
									// 返回错误时输出错误提示
									System.out.println("获取大头笔错误+++++++");
									fallcount++;
								} else {
									// 解析返回的大头笔数据
									JsonNode infomationJson = infomation
											.get("jingdong_ldop_alpha_vendor_bigshot_query_responce").get("resultInfo");
									if (infomationJson.get("statusCode").asText().equals("0")) {
										JsonNode node = infomationJson.get("data");
										logisticsTab.setBigShotCode(node.get("bigShotCode").asText());
										logisticsTab.setBigShotName(node.get("bigShotName").asText());
										// logisticsTab.setBranchCode(node.get("branchCode").asText());
										// logisticsTab.setBranchName(node.get("branchName").asText());
										logisticsTab.setGatherCenterCode(node.get("gatherCenterCode").asText());
										logisticsTab.setGatherCenterName(node.get("gatherCenterName").asText());
										logisticsTab.setSecondSectionCode(node.get("secondSectionCode").asText());
										logisticsTab.setThirdSectionCode(node.get("thirdSectionCode").asText());
										logisticsTabDao.update(logisticsTab);
										successcount++;
										message = "快递公司：" + ecExpress.getProviderName() + "，快递单号："
												+ logisticsTab.getExpressCode();
									} else {
										message = "接口返回数据状态值不为0";
										fallcount++;
									}
								}
							} else {
								message = "接口返回数据无明细";
								fallcount++;
							}
						}
					} else {
						fallcount++;
						message = "接口无数据返回";
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message = "解析返回数据异常";
				}

				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);

			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：当前平台订单不支持此操作");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressOrder", true,
					"获取电子面单成功，本次成功获取面单" + successcount + "条，失败" + fallcount + "条", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() > 0) {
			try {
				resp = BaseJson.returnRespObjList("ec/logisticsTab/achieveECExpressOrder", true, "云打印消息", null, list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(resp);
		return resp;
	}

	private JSONObject PDDECExpressOrderYun(EcExpress ecExpress, ExpressCorp expressCorp, StoreSettings storeSettings,
			EcExpressSettings ecExpressSettings, LogisticsTab logisticsTab, PlatOrder platOrder,
			StoreRecord storeRecord,MisUser misUser) throws Exception {
		JSONObject asda = new JSONObject();

		JSONObject jsonObjectdate = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ItemName", "");
		jsonObject.put("ItemNum", "");
		jsonObjectdate.put("data", jsonObject);

		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddWaybillGetRequest request = new PddWaybillGetRequest();

		ParamWaybillCloudPrintApplyNewRequest paramWaybillCloudPrintApplyNewRequest = new ParamWaybillCloudPrintApplyNewRequest();
		// 发货人信息
		ParamWaybillCloudPrintApplyNewRequestSender sender = new ParamWaybillCloudPrintApplyNewRequestSender();
		ParamWaybillCloudPrintApplyNewRequestSenderAddress address = new ParamWaybillCloudPrintApplyNewRequestSenderAddress();
		address.setCity(ecExpress.getCityName());
		address.setDetail(ecExpress.getAddress());
		address.setDistrict(ecExpress.getCountryName());
		address.setProvince(ecExpress.getProvinceName());
		address.setTown(ecExpress.getCountrysideName());
		sender.setAddress(address);
		sender.setMobile(expressCorp.getDeliverPhone());
		sender.setName(expressCorp.getDeliver());
		paramWaybillCloudPrintApplyNewRequest.setSender(sender);
//		请求面单信息，
		List<ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem> tradeOrderInfoDtos = new ArrayList<ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem>();

		ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem item = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem();
		item.setObjectId("1");// 请求id
		ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemOrderInfo applyNewRequestTradeOrderInfoDtosItemOrderInfo = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemOrderInfo();
		applyNewRequestTradeOrderInfoDtosItemOrderInfo.setOrderChannelsType(ecExpressSettings.getPlatId());
		List<String> tradeOrderList = new ArrayList<String>();
		tradeOrderList.add(platOrder.getEcOrderId());
		applyNewRequestTradeOrderInfoDtosItemOrderInfo.setTradeOrderList(tradeOrderList);
		item.setOrderInfo(applyNewRequestTradeOrderInfoDtosItemOrderInfo);
		ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfo packageInfo = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfo();
		packageInfo.setId(logisticsTab.getOrdrNum()+"");
		List<ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfoItemsItem> items = new ArrayList<ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfoItemsItem>();

		for (PlatOrders platOrders : platOrdersDao.select(platOrder.getOrderId())) {
			ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfoItemsItem item1 = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemPackageInfoItemsItem();
			item1.setCount(platOrders.getGoodNum());
			item1.setName(platOrders.getGoodName());
			items.add(item1);
			String ItemNum = "";
			String ItemName = "";
			InvtyDoc inv = invtyDocDao.selectAllByInvtyEncd(platOrders.getInvId());
			if (inv.getInvtyNm().length() > 24) {
				String str = inv.getInvtyNm().substring(0, 24) + "\n";
				String str1 = inv.getInvtyNm().substring(24,
						inv.getInvtyNm().length() > 48 ? 48 : inv.getInvtyNm().length()) + "\n";
				ItemName = jsonObject.get("ItemName").toString() + str + str1;

				ItemNum = "\n" + platOrders.getInvNum() + "\n";
				ItemNum = jsonObject.get("ItemNum").toString() + ItemNum;
			} else {
				ItemName = jsonObject.get("ItemName").toString() + inv.getInvtyNm() + "\n";
				ItemNum = jsonObject.get("ItemNum").toString() + platOrders.getInvNum() + "\n";

			}

			jsonObject.put("ItemName", ItemName);
			jsonObject.put("ItemNum", ItemNum);
		}

		packageInfo.setItems(items);
//        packageInfo.setVolume(0L);
//        packageInfo.setWeight(0L);
		item.setPackageInfo(packageInfo);
		// 收件人信息
		ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemRecipient recipient = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemRecipient();

		ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemRecipientAddress address1 = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItemRecipientAddress();
		address1.setCity(platOrder.getCity());
		address1.setDetail(platOrder.getRecAddress());
		address1.setDistrict(platOrder.getCounty());
		address1.setProvince(platOrder.getProvince());
		address1.setTown(platOrder.getTown());
		recipient.setAddress(address1);
		recipient.setMobile(platOrder.getRecMobile());
		recipient.setName(platOrder.getRecName());

		item.setRecipient(recipient);
		String ddd =  ecExpress.getProviderCode();
		if(ddd.equals("JD")) {
			ddd = "JDWL";
		}
		List<WhsPlatExpressMapp> list = whsPlatExpressMappDao.selectCloudPrint("PDD", ddd);
		if (list.size() > 0) {
			item.setTemplateUrl(list.get(0).getCloudPrint());
			jsonObjectdate.put("templateURL", list.get(0).getCloudPrintCustom());
		} else {
			asda.put("k", "无云打印标准模版");

			return asda;
		}
		item.setUserId(Long.valueOf(storeSettings.getVenderId()));
//        item.setLogisticsServices("str");//物流服务内容链接

		tradeOrderInfoDtos.add(item);
		paramWaybillCloudPrintApplyNewRequest.setTradeOrderInfoDtos(tradeOrderInfoDtos);
		paramWaybillCloudPrintApplyNewRequest.setWpCode(ecExpress.getProviderCode());
		paramWaybillCloudPrintApplyNewRequest.setNeedEncrypt(true);
		request.setParamWaybillCloudPrintApplyNewRequest(paramWaybillCloudPrintApplyNewRequest);
		System.out.println(JsonUtil.transferToJson(paramWaybillCloudPrintApplyNewRequest));
		PddWaybillGetResponse response = client.syncInvoke(request, accessToken);
		System.out.println(JsonUtil.transferToJson(response));

		if (response.getErrorResponse() != null) {
			asda.put("k", response.getErrorResponse().getErrorMsg());
			// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(misUser.getAccNum());

						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("失败："+response.getErrorResponse().getErrorMsg());
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(14);// 14取电子面单
						logRecord.setTypeName("取电子面单");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
			return asda;
		}

		String string = "";
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		asda.put("list", jsonObjects);
//		asda.put("TM", jsonObjects);
		for (InnerPddWaybillGetResponseModulesItem wcpr : response.getPddWaybillGetResponse().getModules()) {

			logisticsTab.setExpressCode(wcpr.getWaybillCode());// 返回的面单号
			string = wcpr.getPrintData();
			JSONObject dsfadsf = JSON.parseObject(string);
			jsonObjects.add(dsfadsf);
			jsonObjects.add(jsonObjectdate);
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("快递公司："+expressCorp.getExpressNm()+"，快递单号："+wcpr.getWaybillCode());
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(14);// 14取电子面单
			logRecord.setTypeName("取电子面单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			
			logisticsTabDao.update(logisticsTab);
		}

		return asda;
	}

	// 京东电子面单获取 返回京东传回来的json
	public String JDexpressOrder(StoreSettings storeSettings, EcExpress ecExpress, PlatOrder platOrder,
			ExpressCorp expressCorp, String venderId) {
		String message = "";
		try {
			DecimalFormat dFormat = new DecimalFormat("0.00");
			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("waybillType", 1);// 运单类型1普通运单
			objectNode.put("waybillCount", 1);// 运单数量
			objectNode.put("providerId", ecExpress.getProviderId());// 承运商id
			objectNode.put("branchCode", ecExpress.getBranchCode());// 发货网点编码
			objectNode.put("salePlatform", "0010001");// 销售平台0010001代表京东平台
			objectNode.put("platformOrderNo", platOrder.getEcOrderId());// 平台订单号
			objectNode.put("vendorCode", venderId);// 商家ID
			objectNode.put("vendorName", storeSettings.getStoreName());// 商家名称
			objectNode.put("vendorOrderCode", platOrder.getOrderId());// 商家自有订单号
			objectNode.put("weight", 0);// 重量，单位千克，两位小数， 没有填0
			objectNode.put("volume", 0);// 体积，单位为统一为立方厘米 两位小数 没有填0
			objectNode.put("promiseTimeType", 0);// 承诺时效类型，无时效默认传0
			objectNode.put("payType", 0);// 付款方式0-在线支付，目前暂时不支持货到付款业务
			objectNode.put("goodsMoney", dFormat.format(platOrder.getOrderSellerPrice()));// 商品金额 两位小数
			objectNode.put("shouldPayMoney", 0.00);// 代收金额 两位小数
			objectNode.put("needGuarantee", false);// 是否要保价（系统暂不开放报价业务）
			objectNode.put("guaranteeMoney", 0.00);// 保价金额 两位小数,非保价默认传0.0
			objectNode.put("receiveTimeType", 0);// 收货时间类型，0任何时间，1工作日2节假日
			objectNode.put("needGuarantee", false);// 是否要保价（系统暂不开放报价业务）
			objectNode.put("needGuarantee", false);// 是否要保价（系统暂不开放报价业务）
			// 收货地址
			ObjectNode toaddress = JacksonUtil.getObjectNode("");
			toaddress.put("provinceName", platOrder.getProvince());// 收货地址省
			toaddress.put("provinceId", platOrder.getProvinceId());// 收货地址省ID
			toaddress.put("cityName", platOrder.getCity());// 收货地址市
			toaddress.put("cityId", platOrder.getCityId());// 收货地址市ID
			toaddress.put("countryName", platOrder.getCounty());// 收货地址区
			toaddress.put("countryId", platOrder.getCountyId());// 收货地址区ID
			toaddress.put("address", platOrder.getRecAddress());// 收货全地址
			toaddress.put("contact", platOrder.getRecName());// 收货人姓名
			toaddress.put("phone", platOrder.getRecMobile());// 收货人手机
			toaddress.put("mobile", platOrder.getRecMobile());// 座机
			objectNode.put("toAddress", toaddress);
			// 发货地址
			ObjectNode fromaddress = JacksonUtil.getObjectNode("");
			fromaddress.put("provinceName", ecExpress.getProvinceName());// 发货地址省
			fromaddress.put("provinceId", ecExpress.getProvinceId());// 发货地址省ID
			fromaddress.put("cityName", ecExpress.getCityName());// 发货地址市
			fromaddress.put("cityId", ecExpress.getCityId());// 发货地址市ID
			fromaddress.put("countryName", ecExpress.getCountryName());// 发货地址区
			fromaddress.put("countryId", ecExpress.getCountryId());// 发货地址区ID
			fromaddress.put("address", ecExpress.getAddress());// 发货详细地址
			fromaddress.put("contact", expressCorp.getDeliver());// 发件人姓名
			fromaddress.put("phone", expressCorp.getDeliverPhone());// 发件人手机
			fromaddress.put("mobile", expressCorp.getDeliverMobile());// 座机
			objectNode.put("fromAddress", fromaddress);
			ObjectNode jsObjectNode = JacksonUtil.getObjectNode("");
			jsObjectNode.put("content", objectNode);
			// message=objectNode.toString();
			//System.err.println("请求数据：" + objectNode.toString());
			String jdRespStr = ECHelper.getJD("jingdong.ldop.alpha.waybill.receive", storeSettings,
					jsObjectNode.toString());
			//System.out.println(jdRespStr);
			// String jdRespStr = ECHelper.getJD("jingdong.pop.order.search",
			// "0b88dcc2ac604d4fb55fe317f9f5bd94ogrj", json);
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			message = jdRespJson.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	
	// 京东快递下单
		public String JDWLexpress(StoreSettings storeSettings, EcExpress ecExpress, PlatOrder platOrder,
				ExpressCorp expressCorp, String venderId) {
			String message = "";
			try {
				DecimalFormat dFormat = new DecimalFormat("0.00");
				ObjectNode objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("waybillType", 1);// 运单类型1普通运单
				objectNode.put("waybillCount", 1);// 运单数量
				objectNode.put("providerId", ecExpress.getProviderId());// 承运商id
				objectNode.put("branchCode", ecExpress.getBranchCode());// 发货网点编码
				objectNode.put("salePlatform", "0010001");// 销售平台0010001代表京东平台
				objectNode.put("platformOrderNo", platOrder.getEcOrderId());// 平台订单号
				objectNode.put("vendorCode", venderId);// 商家ID
				objectNode.put("vendorName", storeSettings.getStoreName());// 商家名称
				objectNode.put("vendorOrderCode", platOrder.getOrderId());// 商家自有订单号
				objectNode.put("weight", 0);// 重量，单位千克，两位小数， 没有填0
				objectNode.put("volume", 0);// 体积，单位为统一为立方厘米 两位小数 没有填0
				objectNode.put("promiseTimeType", 0);// 承诺时效类型，无时效默认传0
				objectNode.put("payType", 0);// 付款方式0-在线支付，目前暂时不支持货到付款业务
				objectNode.put("goodsMoney", dFormat.format(platOrder.getOrderSellerPrice()));// 商品金额 两位小数
				objectNode.put("shouldPayMoney", 0.00);// 代收金额 两位小数
				objectNode.put("needGuarantee", false);// 是否要保价（系统暂不开放报价业务）
				objectNode.put("guaranteeMoney", 0.00);// 保价金额 两位小数,非保价默认传0.0
				objectNode.put("receiveTimeType", 0);// 收货时间类型，0任何时间，1工作日2节假日
				objectNode.put("needGuarantee", false);// 是否要保价（系统暂不开放报价业务）
				objectNode.put("needGuarantee", false);// 是否要保价（系统暂不开放报价业务）
				// 收货地址
				ObjectNode toaddress = JacksonUtil.getObjectNode("");
				toaddress.put("provinceName", platOrder.getProvince());// 收货地址省
				toaddress.put("provinceId", platOrder.getProvinceId());// 收货地址省ID
				toaddress.put("cityName", platOrder.getCity());// 收货地址市
				toaddress.put("cityId", platOrder.getCityId());// 收货地址市ID
				toaddress.put("countryName", platOrder.getCounty());// 收货地址区
				toaddress.put("countryId", platOrder.getCountyId());// 收货地址区ID
				toaddress.put("address", platOrder.getRecAddress());// 收货全地址
				toaddress.put("contact", platOrder.getRecName());// 收货人姓名
				toaddress.put("phone", platOrder.getRecMobile());// 收货人手机
				toaddress.put("mobile", platOrder.getRecMobile());// 座机
				objectNode.put("toAddress", toaddress);
				// 发货地址
				ObjectNode fromaddress = JacksonUtil.getObjectNode("");
				fromaddress.put("provinceName", ecExpress.getProvinceName());// 发货地址省
				fromaddress.put("provinceId", ecExpress.getProvinceId());// 发货地址省ID
				fromaddress.put("cityName", ecExpress.getCityName());// 发货地址市
				fromaddress.put("cityId", ecExpress.getCityId());// 发货地址市ID
				fromaddress.put("countryName", ecExpress.getCountryName());// 发货地址区
				fromaddress.put("countryId", ecExpress.getCountryId());// 发货地址区ID
				fromaddress.put("address", ecExpress.getAddress());// 发货详细地址
				fromaddress.put("contact", expressCorp.getDeliver());// 发件人姓名
				fromaddress.put("phone", expressCorp.getDeliverPhone());// 发件人手机
				fromaddress.put("mobile", expressCorp.getDeliverMobile());// 座机
				objectNode.put("fromAddress", fromaddress);
				ObjectNode jsObjectNode = JacksonUtil.getObjectNode("");
				jsObjectNode.put("content", objectNode);
				// message=objectNode.toString();
				//System.err.println("请求数据：" + objectNode.toString());
				String jdRespStr = ECHelper.getJD("jingdong.ldop.alpha.waybill.receive", storeSettings,
						jsObjectNode.toString());
				//System.out.println(jdRespStr);
				// String jdRespStr = ECHelper.getJD("jingdong.pop.order.search",
				// "0b88dcc2ac604d4fb55fe317f9f5bd94ogrj", json);
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
				message = jdRespJson.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return message;
		}

		/**
		 * 获取京东快递运单号
		 * @param storeSettings 京东快递店铺设置
		 * @param count  获取运单数量
		 * @return  
		 */
		
		public String loadJdwlExpressCode(StoreSettings storeSettings, int count) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("customerCode", storeSettings.getVenderId());// 商家编码 有京东销售提供 010K345716
				objectNode.put("preNum", count);// 获取运单号数量，最大值100
				objectNode.put("orderType",0);// 运单类型。(普通外单：0，O2O 外单：1)默认为 0
				String jdRespStr = ECHelper.getJD("jingdong.etms.waybillcode.get", storeSettings,
						objectNode.toString());
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
				result = jdRespJson.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}
		
		/**
		 * 判断寄件地址,收件地址是否在京东的揽收范围 : jingdong.etms.range.check
		 * @param storeSettings 京东快递店铺设置
		 * @param logisticsTab 对应物流表
		 * @param goodsType 配送业务类型（ 1:普通，3:填仓，4:特配，5:鲜活，6:控温，7:冷藏，8:冷冻，9:深冷）
		 * @param platOrder 对应订单信息
		 * @return  
		 */
		
		public String checkIsJdwl(StoreSettings storeSettings, LogisticsTab logisticsTab,PlatOrder platOrder, int goodsType,ExpressCorp expressCorp,StoreRecord storeRecord) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("customerCode", storeSettings.getVenderId());// 商家编码 有京东销售提供 010K345716
				//objectNode.put("preNum", count);// 获取运单号数量，最大值100
				objectNode.put("orderId", storeRecord.getStoreName()+logisticsTab.getOrderId());//订单号
				objectNode.put("goodsType",goodsType);// 配送业务类型（ 1:普通，3:填仓，4:特配，5:鲜活，6:控温，7:冷藏，8:冷冻，9:深冷）默认是1
				objectNode.put("receiveAddress",platOrder.getProvince()+platOrder.getCity()+platOrder.getCounty());//收件人地址
				objectNode.put("senderAddress",expressCorp.getProvince()+expressCorp.getCity()+expressCorp.getCountry());//发货地址
				
				String jdRespStr = ECHelper.getJD("jingdong.etms.range.check", storeSettings,
						objectNode.toString());
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
				result = jdRespJson.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}
		/**
		 *  青龙接单 只有判断是否京配的结果=100 可以京配才能调此接口
		 * @param storeSettings 京东快递店铺设置
		 * @param logisticsTab 对应物流表
		 * @param goodsType 配送业务类型（ 1:普通，3:填仓，4:特配，5:鲜活，6:控温，7:冷藏，8:冷冻，9:深冷）
		 * @param platOrder 对应订单信息
		 * @return  
		 */
		
		public String JdwlOrder(StoreSettings storeSettings, LogisticsTab logisticsTab,PlatOrder platOrder, int goodsType,StoreRecord storeRecord,ExpressCorp expressCorp) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("customerCode", storeSettings.getVenderId());// 商家编码 有京东销售提供 010K345716
				objectNode.put("deliveryId", logisticsTab.getExpressCode());//运单号
				/*
				 * 0010001 代表京东平台 0010002 代表天猫 0010003 代表苏宁 0030001 代表其他平台
				 */
				if(storeRecord.getEcId().equals("JD")) {
					 objectNode.put("salePlat","0010001");
				 }else if(storeRecord.getEcId().equals("TM")) {
					 objectNode.put("salePlat","0010002");
				 }else if(storeRecord.getEcId().equals("SN")) {
					 objectNode.put("salePlat","0010003");
				 }else {
					 objectNode.put("salePlat","0030001");
				 }
			 	
				//objectNode.put("preNum", count);// 获取运单号数量，最大值100
				objectNode.put("orderId", storeRecord.getStoreName()+logisticsTab.getOrderId());//青龙订单号，用店铺名称+订单号拼接，方便月底导出数据做报表
				objectNode.put("senderName",expressCorp.getDeliver());//寄件人
				objectNode.put("senderAddress",expressCorp.getDetailedAddress());//寄件人地址
				objectNode.put("senderMobile",expressCorp.getDeliverPhone());//寄件人手机
				objectNode.put("receiveName",logisticsTab.getRecName());//收件人
				objectNode.put("receiveAddress",platOrder.getProvince()+platOrder.getCity()+platOrder.getCounty()+platOrder.getRecAddress());//收件人地址
				objectNode.put("receiveName",logisticsTab.getRecName());//收件人
				objectNode.put("receiveMobile",logisticsTab.getRecMobile());// 配送业务类型（ 1:普通，3:填仓，4:特配，5:鲜活，6:控温，7:冷藏，8:冷冻，9:深冷）默认是1
				objectNode.put("packageCount",1);//包裹数
				if (logisticsTab.getWeight()!=null) {
					//重量，物流单中有重量时取物流单的重量，没有重量时默认1
					if (logisticsTab.getWeight().compareTo(BigDecimal.ZERO) > 0) {
						objectNode.put("weight", logisticsTab.getWeight());
					} else {
						objectNode.put("weight", "1.00");
					} 
				} else {
					objectNode.put("weight", "1.00");
				}
				if (logisticsTab.getVolume()!=null) {
					//体积，物流单中有重量时取物流单的重量，没有重量时默认1000立方厘米 
					if (logisticsTab.getVolume().compareTo(BigDecimal.ZERO) > 0) {
						objectNode.put("vloumn", logisticsTab.getWeight());
					} else {
						objectNode.put("vloumn", "1000.00");
					} 
				}else {
					objectNode.put("vloumn", "1000.00");
				}
				objectNode.put("goods","母婴");//
				String jdRespStr = ECHelper.getJD("jingdong.etms.waybill.send", storeSettings,
						objectNode.toString());
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
				result = jdRespJson.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}
		
		/**
		 * 	取消下单/拦截运单 api
		 * 	调用此 api，如果未取件完成,则是发出取消指令； 如果揽收完成,则是发出拦截指令
			异步返回,建议轮询,判断状态码 stateCode
			stateCode=100,表示取消成功
			stateCode=108,表示运单已经被取消成功过了
		 * 	接口名：jingdong.ldop.receive.order.intercept
		 * @param storeSettings
		 * @param logisticsTab
		 * @param cancelReason 取消原因
		 * @return
		 */
		public String cancelJdwl(StoreSettings storeSettings, LogisticsTab logisticsTab,String cancelReason) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("vendorCode", storeSettings.getVenderId());// 商家编码 有京东销售提供 010K345716
				objectNode.put("deliveryId", logisticsTab.getExpressCode());//运单号
				objectNode.put("interceptReason",cancelReason);//取消原因
				String jdRespStr = ECHelper.getJD("jingdong.ldop.receive.order.intercept", storeSettings,
						objectNode.toString());
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
				result = jdRespJson.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;

		}
	

	/**
	 * 获取京东无界电子面单大头笔编码信息
	 * 
	 * @param storeSettings 店铺设置
	 * @param expressCode   快递单号
	 * @param providerId    承运商ID
	 * @param providerCode  承运商编码
	 * @return 接口返回结果
	 */
	public String JDBigShot(StoreSettings storeSettings, String expressCode, int providerId, String providerCode) {
		String result = "";
		ObjectNode objectNode;
		try {
			objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("waybillCode", expressCode);// 运单号
			objectNode.put("providerId", providerId);// 承运商ID
			objectNode.put("providerCode", providerCode);// 承运商编码
			String jdRespStr = ECHelper.getJD("jingdong.ldop.alpha.vendor.bigshot.query", storeSettings,
					objectNode.toString());
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			result = jdRespJson.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}
	
	//京东订单发货
	public String JDOrderShip(LogisticsTab logistics, EcExpress express, StoreSettings storeSettings) {
		String result = "";
		try {
			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("orderId", logistics.getEcOrderId());// 平台订单号
			objectNode.put("logiCoprId", express.getProviderId());// 快递公司id
			objectNode.put("logiNo", logistics.getExpressCode());// 快递单号
			result = ECHelper.getJD("jingdong.pop.order.shipment", storeSettings, objectNode.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String platOrderShip(String logisticsNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		int successcount = 0;
		int fallcount = 0;
		String[] nums = logisticsNum.split(",");
		String message = "";
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {

			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以进行发货操作
				fallcount++;
				continue;
			}
			if(StringUtils.isEmpty(logisticsTab.getExpressCode())) {
				//如果物流表快递单号为空时不允许发货
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递单号为空，发货失败");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsPick()==0) {
				//如果物流表状态为未拣货，不允许执行发货
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：物流表未开始拣货，请先执行拣货标记");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsShip()==1) {
				//如果物流表已发货不允许重复发货
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：物流表已发货，不允许重复发货");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			// 根据物流表的订单号获取电商订单信息
			PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
			// 根据订单的店铺ID获取店铺档案
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// 根据店铺档案获取订单平台ID
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// 获取对应订单的店铺设置
			StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
			// 获取快递公司档案信息
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			// 获取平台设置的电子面单快递公司信息
			EcExpress ecExpress=null;
			StoreRecord storeRecord2 = storeRecordDao.select(storeSettings.getStoreId());
			if (!storeRecord2.getEcId().equals("JDWL")&&!expressCorp.getCompanyCode().equals("SF")) {
				try {
					ecExpress = ecExpressDao.select(storeRecord2.getEcId(), expressCorp.getCompanyCode(),
							expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),
							expressCorp.getDetailedAddress());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("失败：快递公司档案对应电子面单账户信息重复，请检查");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8发货
					logRecord.setTypeName("发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				if (ecExpress == null) {
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("失败：快递公司档案设置错误，请检查");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8发货
					logRecord.setTypeName("发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				} 
			}
			if (storeRecord.getEcId().equals("JD")) {
				 storeSettings = storeSettingsDao.select(platOrder.getStoreId());
				// 京东平台的订单
				try {
					String result = JDOrderShip(logisticsTab, ecExpress, storeSettings);
					//System.err.println(result);
					if (result.equals("")) {
						message = "接口没有返回信息";
						// 返回为空时，说明调用订单发货接口时没有返回信息
						fallcount++;
					} else {
						// System.err.println("调用京东接口发货后返回json：" + result);
						if (JacksonUtil.getObjectNode(result).has("error_response")) {
							// 接口返回信息包含error_response
							message = JacksonUtil.getObjectNode(result).get("error_response").asText();
							fallcount++;
						} else {
							JsonNode node = JacksonUtil.getObjectNode(result).get("jingdong_pop_order_shipment_responce").get("sopjosshipment_result");
							//System.out.println(node.get("success").asBoolean());
							if (node.get("success").asBoolean()) {
								// 发货成功
								logisticsTab.setIsShip(1);// 设置物流表状态为已发货
								logisticsTab.setIsBackPlatform(1);// 设置已回传平台
								logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
								logisticsTabDao.update(logisticsTab);// 更新物流表信息

								platOrder.setIsShip(1);
								platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
								platOrder.setShipTime(logisticsTab.getShipDate());
								platOrderDao.update(platOrder);

								message = "发货回传平台成功";
								successcount++;
							} else {
								// 发货失败
								message = node.get("chineseErrCode").asText();
								fallcount++;
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message = "发货回传平台异常";
				}

				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);

			} else if (storeRecord.getEcId().equals("TM")) {
				try {
				//System.err.println(platOrder.getEcOrderId());
				//System.err.println(logisticsTab.getExpressCode());
			    storeSettings = storeSettingsDao.select(platOrder.getStoreId());
				// 天猫（淘宝）
				if ((platOrder.getEcOrderId() == null || platOrder.getEcOrderId().equals(""))
						|| (logisticsTab.getExpressCode() == null || logisticsTab.getExpressCode().equals(""))) {
					fallcount++;
					continue;
				}

				LogisticsOnlineSendRequest req = new LogisticsOnlineSendRequest();
				req.setTid(Long.valueOf(platOrder.getEcOrderId()));
				req.setOutSid(logisticsTab.getExpressCode());
				

				if(expressCorp.getCompanyCode().equals("SF")) {
					req.setCompanyCode("SF");
				}else{
					req.setCompanyCode(ecExpress.getProviderCode());
				}
				// 转发
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("path", LogisticsOnlineSendRequest.class.getName());
				maps.put("taobaoObject", JSON.toJSONString(req));
				String taobao = null;
				
				 
					taobao = ECHelper.getTB("", storeSettings, maps);
				

				LogisticsOnlineSendResponse rsp = JSONObject.parseObject(taobao, LogisticsOnlineSendResponse.class);
				if (!rsp.isSuccess()) {
					fallcount++;
					// 发货失败
					message = rsp.getSubMsg();
				}else {
					if (rsp.getShipping().getIsSuccess()) {
						// 发货成功
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						
						

						platOrder.setIsShip(1);
						platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
						platOrder.setShipTime(logisticsTab.getShipDate());
						platOrderDao.update(platOrder);
						successcount++;
						message = "发货回传平台成功";

					} else {
						fallcount++;
						// 发货失败
						message = "发货回传平台失败";
						
					}
				}
				} catch ( Exception e) {
					// TODO Auto-generated catch block
					fallcount++;

					e.printStackTrace();
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8发货
					logRecord.setTypeName("发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					message = "发货回传平台异常";

				}
				// taobao.logistics.online.send( 在线订单发货处理（支持货到付款） )
				// taobao.logistics.online.confirm( 确认发货通知接口 )不是这个
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
			} else if (storeRecord.getEcId().equals("PDD")) {
				PddLogisticsOnlineSendRequest request = new PddLogisticsOnlineSendRequest();

				//System.err.println(platOrder.getEcOrderId());
				//System.err.println(logisticsTab.getExpressCode());
//				if("9999".equals(ecExpressSettings.getStoreId())){
////				{"logistics_companies_get_response":{"logistics_companies":[{"code":"STO","available":1,"logistics_company":"申通快递","id":1},{"code":"SHHT","available":0,"logistics_company":"上海汇通","id":2},{"code":"HT","available":1,"logistics_company":"百世快递","id":3},{"code":"SF","available":1,"logistics_company":"顺丰快递","id":44},{"code":"YTO","available":1,"logistics_company":"圆通快递","id":85},{"code":"","available":0,"logistics_company":"内部员工自提","id":86},{"code":"BBSD","available":0,"logistics_company":"奔奔速达","id":88},{"code":"SAD","available":0,"logistics_company":"赛澳递","id":89},{"code":"CHENGBANG","available":0,"logistics_company":"晟邦物流","id":90},{"code":"ZTO","available":1,"logistics_company":"中通快递","id":115},{"code":"QF","available":0,"logistics_company":"全峰快递","id":116},{"code":"YS","available":1,"logistics_company":"优速快递","id":117},{"code":"EMS","available":1,"logistics_company":"邮政EMS","id":118},{"code":"TT","available":1,"logistics_company":"天天快递","id":119},{"code":"JD","available":1,"logistics_company":"京东配送","id":120},{"code":"YUNDA","available":1,"logistics_company":"韵达快递","id":121},{"code":"KJ","available":0,"logistics_company":"快捷快递","id":122},{"code":"GTO","available":1,"logistics_company":"国通快递","id":124},{"code":"DDCBPS","available":0,"logistics_company":"当当出版配送","id":128},{"code":"ZJS","available":1,"logistics_company":"宅急送快递","id":129},{"code":"RFD","available":1,"logistics_company":"如风达","id":130},{"code":"DB","available":1,"logistics_company":"德邦快递","id":131},{"code":"YZXB","available":1,"logistics_company":"邮政快递包裹","id":132},{"code":"LBEX","available":1,"logistics_company":"龙邦快递","id":133},{"code":"FEDEX","available":1,"logistics_company":"联邦快递","id":135},{"code":"JIUYE","available":1,"logistics_company":"九曳供应链","id":136},{"code":"","available":0,"logistics_company":"百城当日达快递","id":137},{"code":"","available":0,"logistics_company":"达达快递","id":138},{"code":"","available":0,"logistics_company":"冻到家物流","id":139},{"code":"NJCHENGBANG","available":0,"logistics_company":"南京晟邦","id":140},{"code":"SXHONGMAJIA","available":0,"logistics_company":"山西红马甲","id":141},{"code":"WXWL","available":0,"logistics_company":"万象物流","id":142},{"code":"LIJISONG","available":0,"logistics_company":"立即送","id":143},{"code":"MENDUIMEN","available":0,"logistics_company":"门对门","id":144},{"code":"SAD2","available":0,"logistics_company":"赛澳递","id":145},{"code":"","available":0,"logistics_company":"丰程","id":147},{"code":"ADX","available":1,"logistics_company":"安达信","id":148},{"code":"HWKD","available":0,"logistics_company":"海外快递","id":149},{"code":"GZLT","available":0,"logistics_company":"飞远物流","id":150},{"code":"","available":0,"logistics_company":"南都快递","id":151},{"code":"HUIWEN","available":0,"logistics_company":"汇文快递","id":152},{"code":"","available":0,"logistics_company":"南都快递","id":153},{"code":"HUANGMAJIA","available":0,"logistics_company":"黄马甲","id":154},{"code":"SURE","available":1,"logistics_company":"速尔快递","id":155},{"code":"YAMAXUNWULIU","available":1,"logistics_company":"亚马逊物流","id":156},{"code":"YCT","available":1,"logistics_company":"黑猫宅急便","id":157},{"code":"","available":0,"logistics_company":"顺丰航运","id":158},{"code":"","available":0,"logistics_company":"圆通航运","id":159},{"code":"","available":0,"logistics_company":"拼好货","id":160},{"code":"SHSAD","available":0,"logistics_company":"上海赛澳递","id":161},{"code":"BJCS","available":0,"logistics_company":"城市100","id":162},{"code":"ZMKM","available":0,"logistics_company":"芝麻开门","id":163},{"code":"SHUNJIEFENGDA","available":0,"logistics_company":"顺捷丰达","id":164},{"code":"HTXMJ","available":1,"logistics_company":"汇通小红马","id":165},{"code":"","available":0,"logistics_company":"辽宁小红马","id":166},{"code":"LNHUANGMAJIA","available":0,"logistics_company":"辽宁黄马甲","id":167},{"code":"","available":0,"logistics_company":"江苏赛澳递","id":168},{"code":"","available":0,"logistics_company":"三人行","id":169},{"code":"","available":0,"logistics_company":"通和佳递","id":170},{"code":"SUJIEVIP","available":0,"logistics_company":"速捷","id":171},{"code":"","available":0,"logistics_company":"信诺迅达","id":172},{"code":"","available":0,"logistics_company":"风先生","id":173},{"code":"","available":0,"logistics_company":"宽容","id":174},{"code":"","available":0,"logistics_company":"广州途客","id":175},{"code":"","available":0,"logistics_company":"小红帽","id":176},{"code":"","available":0,"logistics_company":"鹏达","id":177},{"code":"FJGZLT","available":0,"logistics_company":"福建飞远","id":178},{"code":"","available":0,"logistics_company":"E特快","id":179},{"code":"SELF","available":1,"logistics_company":"其他","id":180},{"code":"","available":0,"logistics_company":"云鸟","id":181},{"code":"","available":0,"logistics_company":"保达","id":182},{"code":"KYE","available":1,"logistics_company":"跨越速运","id":183},{"code":"","available":0,"logistics_company":"吉林黄马甲","id":184},{"code":"CHENGJI","available":0,"logistics_company":"城际速递","id":185},{"code":"USPS","available":1,"logistics_company":"usps","id":186},{"code":"ANJELEX","available":1,"logistics_company":"青岛安捷","id":187},{"code":"","available":0,"logistics_company":"大韩通运","id":188},{"code":"","available":0,"logistics_company":"棒棒糖","id":189},{"code":"TUXIAN","available":0,"logistics_company":"途鲜","id":190},{"code":"CNKD","available":0,"logistics_company":"菜鸟快递","id":191},{"code":"EMSKD","available":0,"logistics_company":"EMS经济快递","id":192},{"code":"","available":0,"logistics_company":"汇站众享","id":193},{"code":"","available":0,"logistics_company":"派客","id":194},{"code":"XLOBO","available":1,"logistics_company":"贝海国际速递","id":195},{"code":"","available":0,"logistics_company":"丰泰国际快递","id":196},{"code":"HUANQIU","available":1,"logistics_company":"环球速运","id":197},{"code":"","available":0,"logistics_company":"168顺发速递","id":198},{"code":"","available":0,"logistics_company":"全球快递","id":199},{"code":"CG","available":1,"logistics_company":"程光物流","id":200},{"code":"UAPEX","available":1,"logistics_company":"全一快递","id":201},{"code":"","available":0,"logistics_company":"环球速运","id":202},{"code":"DJKJ","available":0,"logistics_company":"东骏快捷","id":203},{"code":"BSKD","available":0,"logistics_company":"百世快递","id":204},{"code":"YCGWL","available":1,"logistics_company":"远成快运","id":205},{"code":"","available":0,"logistics_company":"风腾国际速递","id":206},{"code":"","available":0,"logistics_company":"笨鸟转运","id":207},{"code":"ANNENG","available":1,"logistics_company":"安能快递","id":208},{"code":"EPS","available":0,"logistics_company":"联众国际快运","id":209},{"code":"HOAU","available":1,"logistics_company":"天地华宇","id":210},{"code":"ZHONGYOUWULIU","available":1,"logistics_company":"中邮速递","id":211},{"code":"","available":0,"logistics_company":"hi淘易","id":212},{"code":"INTEREMS","available":1,"logistics_company":"EMS-国际件","id":213},{"code":"ZTKY","available":1,"logistics_company":"中铁物流","id":214},{"code":"","available":0,"logistics_company":"楚源物流","id":215},{"code":"XBWL","available":1,"logistics_company":"新邦物流","id":216},{"code":"FLASH","available":1,"logistics_company":"Flash Express","id":217},{"code":"NSF","available":0,"logistics_company":"新顺丰NSF","id":218},{"code":"","available":0,"logistics_company":"锐朗快递","id":219},{"code":"","available":0,"logistics_company":"王道国际物流","id":220},{"code":"DCS","available":0,"logistics_company":"DCS GLOBAL","id":221},{"code":"","available":0,"logistics_company":"迅速快递","id":222},{"code":"FTD","available":1,"logistics_company":"富腾达","id":223},{"code":"QFWL","available":0,"logistics_company":"琦峰物流","id":224},{"code":"","available":0,"logistics_company":"金运通物流","id":225},{"code":"EWE","available":1,"logistics_company":"EWE全球快递","id":226},{"code":"RRS","available":1,"logistics_company":"日日顺物流","id":227},{"code":"SNWL","available":1,"logistics_company":"苏宁快递","id":228},{"code":"BESTQJT","available":1,"logistics_company":"百世快运","id":229},{"code":"DEBANGWULIU","available":1,"logistics_company":"德邦物流","id":230},{"code":"WEITEPAI","available":0,"logistics_company":"微特派","id":231},{"code":"MYAAE","available":1,"logistics_company":"AAE全球专递","id":232},{"code":"ARAMEX","available":1,"logistics_company":"Aramex","id":233},{"code":"ASENDIA","available":0,"logistics_company":"Asendia USA","id":234},{"code":"CITYLINK","available":0,"logistics_company":"City-Link","id":235},{"code":"COE","available":1,"logistics_company":"COE东方快递","id":236},{"code":"DHLDE","available":0,"logistics_company":"DHL德国","id":237},{"code":"DHL","available":0,"logistics_company":"DHL全球","id":238},{"code":"DHLCN","available":1,"logistics_company":"DHL中国","id":239},{"code":"EMSGJ","available":0,"logistics_company":"EMS国际","id":240},{"code":"FEDEXUS","available":0,"logistics_company":"FedEx美国","id":241},{"code":"FEDEXCN","available":0,"logistics_company":"FedEx中国","id":242},{"code":"OCS","available":0,"logistics_company":"OCS","id":243},{"code":"ONTRAC","available":0,"logistics_company":"OnTrac","id":244},{"code":"TNT","available":0,"logistics_company":"TNT","id":245},{"code":"UPS","available":1,"logistics_company":"UPS","id":246},{"code":"POSTAL","available":0,"logistics_company":"阿尔巴尼亚邮政","id":247},{"code":"POSTAR","available":0,"logistics_company":"阿根廷邮政","id":248},{"code":"POSTAE","available":0,"logistics_company":"阿联酋邮政","id":249},{"code":"POSTEE","available":0,"logistics_company":"爱沙尼亚邮政","id":250},{"code":"POSTAT","available":0,"logistics_company":"奥地利邮政","id":252},{"code":"POSTAU","available":0,"logistics_company":"澳大利亚邮政","id":253},{"code":"POSTPK","available":0,"logistics_company":"巴基斯坦邮政","id":254},{"code":"POSTBR","available":0,"logistics_company":"巴西邮政","id":255},{"code":"POSTBY","available":0,"logistics_company":"白俄罗斯邮政","id":256},{"code":"EES","available":0,"logistics_company":"百福东方","id":257},{"code":"POSTB","available":0,"logistics_company":"包裹信件","id":258},{"code":"POSTBG","available":0,"logistics_company":"保加利亚邮政","id":259},{"code":"BLSYZ","available":0,"logistics_company":"比利时邮政","id":260},{"code":"BLYZ","available":0,"logistics_company":"波兰邮政","id":261},{"code":"CXCOD","available":1,"logistics_company":"传喜物流","id":262},{"code":"DTW","available":1,"logistics_company":"大田物流","id":263},{"code":"4PX","available":1,"logistics_company":"递四方","id":264},{"code":"RUSTON","available":0,"logistics_company":"俄速通","id":265},{"code":"FGYZ","available":0,"logistics_company":"法国邮政","id":266},{"code":"GZFY","available":0,"logistics_company":"凡宇快递","id":267},{"code":"ZTKY","available":1,"logistics_company":"飞豹快递","id":268},{"code":"HZABC","available":0,"logistics_company":"飞远(爱彼西)配送","id":269},{"code":"POSTFI","available":0,"logistics_company":"芬兰邮政","id":270},{"code":"POSTCO","available":0,"logistics_company":"哥伦比亚邮政","id":271},{"code":"EPOST","available":0,"logistics_company":"韩国邮政","id":272},{"code":"HLWL","available":1,"logistics_company":"恒路物流","id":273},{"code":"HQKY","available":0,"logistics_company":"华企快运","id":274},{"code":"TMS56","available":1,"logistics_company":"加运美","id":275},{"code":"CNEX","available":1,"logistics_company":"佳吉快运","id":276},{"code":"JIAYI","available":1,"logistics_company":"佳怡物流","id":277},{"code":"KERRYEAS","available":0,"logistics_company":"嘉里大通","id":278},{"code":"JKYZ","available":0,"logistics_company":"捷克邮政","id":279},{"code":"JDYWL","available":0,"logistics_company":"筋斗云物流","id":280},{"code":"SZKKE","available":1,"logistics_company":"京广速递","id":281},{"code":"POSTHR","available":0,"logistics_company":"克罗地亚邮政","id":282},{"code":"POSTLV","available":0,"logistics_company":"拉脱维亚邮政","id":283},{"code":"POSTLB","available":0,"logistics_company":"黎巴嫩邮政","id":284},{"code":"LTS","available":1,"logistics_company":"联昊通","id":285},{"code":"POSTMT","available":0,"logistics_company":"马耳他邮政","id":286},{"code":"POSTMK","available":0,"logistics_company":"马其顿邮政","id":287},{"code":"POSTMU","available":0,"logistics_company":"毛里求斯邮政","id":288},{"code":"SERPOST","available":0,"logistics_company":"秘鲁邮政","id":289},{"code":"MBEX","available":0,"logistics_company":"民邦快递","id":290},{"code":"CAE","available":1,"logistics_company":"民航快递","id":291},{"code":"SZML56","available":0,"logistics_company":"明亮物流","id":292},{"code":"POSTMD","available":0,"logistics_company":"摩尔多瓦邮政","id":293},{"code":"POSTZA","available":0,"logistics_company":"南非邮政","id":294},{"code":"POSTNO","available":0,"logistics_company":"挪威邮政","id":295},{"code":"POSTPT","available":0,"logistics_company":"葡萄牙邮政","id":296},{"code":"QRT","available":0,"logistics_company":"全日通","id":297},{"code":"RBYZEMS","available":0,"logistics_company":"日本邮政","id":298},{"code":"POSTSE","available":0,"logistics_company":"瑞典邮政","id":299},{"code":"POSTCH","available":0,"logistics_company":"瑞士邮政","id":300},{"code":"POSTSRB","available":0,"logistics_company":"塞尔维亚邮政","id":301},{"code":"SANTAI","available":0,"logistics_company":"三态速递","id":302},{"code":"POSTSA","available":0,"logistics_company":"沙特邮政","id":303},{"code":"SZSA56","available":0,"logistics_company":"圣安物流","id":304},{"code":"FJSFWLJTYXGS","available":1,"logistics_company":"盛丰物流","id":305},{"code":"SHENGHUI","available":1,"logistics_company":"盛辉物流","id":306},{"code":"POSTSK","available":0,"logistics_company":"斯洛伐克邮政","id":307},{"code":"POSTSI","available":0,"logistics_company":"斯洛文尼亚邮政","id":308},{"code":"SUIJIAWL","available":0,"logistics_company":"穗佳物流","id":309},{"code":"POSTTH","available":0,"logistics_company":"泰国邮政","id":310},{"code":"POSTTR","available":0,"logistics_company":"土耳其邮政","id":311},{"code":"MANCOWL","available":1,"logistics_company":"万家物流","id":312},{"code":"POSTUA","available":0,"logistics_company":"乌克兰邮政","id":313},{"code":"POSTES","available":0,"logistics_company":"西班牙邮政","id":314},{"code":"XFWL","available":1,"logistics_company":"信丰物流","id":315},{"code":"POSTHU","available":0,"logistics_company":"匈牙利邮政","id":316},{"code":"AIR","available":1,"logistics_company":"亚风速递","id":317},{"code":"POSTAM","available":0,"logistics_company":"亚美尼亚邮政","id":318},{"code":"YWWL","available":1,"logistics_company":"燕文物流","id":319},{"code":"POSTIT","available":0,"logistics_company":"意大利邮政","id":320},{"code":"FEC","available":0,"logistics_company":"银捷速递","id":321},{"code":"POSTIN","available":0,"logistics_company":"印度邮政","id":322},{"code":"ROYALMAIL","available":0,"logistics_company":"英国皇家邮政","id":323},{"code":"POSTBBZ","available":1,"logistics_company":"邮政标准快递","id":324},{"code":"CNPOSTGJ","available":1,"logistics_company":"邮政国际包裹","id":325},{"code":"YFEXPRESS","available":0,"logistics_company":"越丰物流","id":326},{"code":"YTZG","available":0,"logistics_company":"运通中港快递","id":327},{"code":"ZENY","available":0,"logistics_company":"增益速递","id":328},{"code":"POSTCL","available":0,"logistics_company":"智利邮政","id":329},{"code":"SPSR","available":0,"logistics_company":"中俄快递","id":330},{"code":"CRE","available":1,"logistics_company":"中铁快运","id":332},{"code":"KFW","available":0,"logistics_company":"快服务快递","id":333},{"code":"KDN","available":0,"logistics_company":"快递鸟","id":334},{"code":"YOUBANG","available":1,"logistics_company":"优邦国际速运","id":335},{"code":"TJ","available":1,"logistics_company":"天际快递","id":336},{"code":"FY","available":1,"logistics_company":"飞洋快递","id":337},{"code":"BM","available":1,"logistics_company":"斑马物联网","id":338},{"code":"EKM","available":1,"logistics_company":"易客满","id":339},{"code":"JDKD","available":1,"logistics_company":"京东大件物流","id":340},{"code":"SUBIDA","available":1,"logistics_company":"速必达","id":341},{"code":"DJKJWL","available":0,"logistics_company":"东骏快捷","id":342},{"code":"ZTOKY","available":1,"logistics_company":"中通快运","id":343},{"code":"YDKY","available":1,"logistics_company":"韵达快运","id":344},{"code":"ANKY","available":1,"logistics_company":"安能快运","id":345},{"code":"ANDE","available":1,"logistics_company":"安得物流","id":346},{"code":"WM","available":1,"logistics_company":"中粮我买网","id":347},{"code":"YMDD","available":1,"logistics_company":"壹米滴答","id":348},{"code":"DD","available":1,"logistics_company":"当当网","id":349},{"code":"PJ","available":1,"logistics_company":"品骏","id":350},{"code":"OTP","available":1,"logistics_company":"承诺达特快","id":351},{"code":"AXWL","available":1,"logistics_company":"安迅物流","id":352},{"code":"YJ","available":0,"logistics_company":"友家速递","id":353},{"code":"SDSD","available":1,"logistics_company":"D速物流","id":354},{"code":"STOINTER","available":1,"logistics_company":"申通国际","id":355},{"code":"YZT","available":1,"logistics_company":"一智通","id":356},{"code":"JGSD","available":0,"logistics_company":"京广速递","id":357},{"code":"SXJD","available":1,"logistics_company":"顺心捷达","id":358},{"code":"QH","available":1,"logistics_company":"群航国际货运","id":359},{"code":"ZWYSD","available":1,"logistics_company":"中外运速递","id":360},{"code":"ZZSY","available":1,"logistics_company":"卓志速运","id":361},{"code":"JZMSD","available":1,"logistics_company":"加州猫速递","id":362},{"code":"GJ","available":1,"logistics_company":"高捷物流","id":363},{"code":"SQWL","available":1,"logistics_company":"商桥物流","id":364},{"code":"FR","available":1,"logistics_company":"复融供应链","id":365},{"code":"ZY","available":1,"logistics_company":"中远","id":366},{"code":"YDGJ","available":1,"logistics_company":"韵达国际","id":367},{"code":"MKGJ","available":1,"logistics_company":"美快国际","id":368},{"code":"NFCM","available":0,"logistics_company":"南方传媒","id":369},{"code":"WSPY","available":1,"logistics_company":"威时沛运","id":370},{"code":"ZTOINTER","available":1,"logistics_company":"中通国际","id":371},{"code":"SFKY","available":1,"logistics_company":"顺丰快运","id":372},{"code":"MGWL","available":1,"logistics_company":"亚马逊综合物流","id":373},{"code":"HKE","available":1,"logistics_company":"HKE国际速递","id":374},{"code":"EFSPOST","available":1,"logistics_company":"新西兰平安物流","id":375},{"code":"HTINTER","available":1,"logistics_company":"百世国际","id":376},{"code":"BSE","available":1,"logistics_company":"蓝天国际快递","id":377},{"code":"YLJY","available":0,"logistics_company":"优联吉运","id":378},{"code":"ZYSFWL","available":0,"logistics_company":"转运四方物流","id":379},{"code":"WSKD","available":1,"logistics_company":"威盛快递","id":380},{"code":"YTGJ","available":0,"logistics_company":"圆通国际","id":381},{"code":"HXWL","available":0,"logistics_company":"海信物流","id":382},{"code":"HYWL","available":0,"logistics_company":"宏远物流","id":383},{"code":"JTKD","available":0,"logistics_company":"极兔快递","id":384}],"request_id":"15807151639054916"}}
//					request.setLogisticsId(120L);//京东快递
//					storeSettings = storeSettingsDao.select(platOrder.getStoreId());
//					System.err.println(120L);
//
//				}else{
				if (storeRecord2.getEcId().equals("JDWL")){
					ecExpress = ecExpressDao.select(storeRecord2.getEcId(), "JD",
					      expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),
					      expressCorp.getDetailedAddress());
				}
				
					request.setLogisticsId((long)ecExpress.getProviderId());
//					System.err.println("!999"+ecExpress.getProviderId());

//				}

				// pdd
				if ((platOrder.getEcOrderId() == null || platOrder.getEcOrderId().equals(""))
						|| (logisticsTab.getExpressCode() == null || logisticsTab.getExpressCode().equals(""))) {
					fallcount++;
					continue;
				}
				StoreSettings storeSettings22 = storeSettingsDao.select(ecExpressSettings.getStoreId());
				String clientId = storeSettings22.getAppKey();
				String clientSecret = storeSettings22.getAppSecret();
				String accessToken = storeSettings22.getAccessToken();
				PopClient client = new PopHttpClient(clientId, clientSecret);

				request.setOrderSn(platOrder.getEcOrderId());
				request.setTrackingNumber(logisticsTab.getExpressCode());
				//request.setRefundAddressId(logisticsTab.getExpressCode());
				//System.err.println(platOrder.getEcOrderId());
				//System.err.println(logisticsTab.getExpressCode());
				PddLogisticsOnlineSendResponse response = null;
				try {
					response = client.syncInvoke(request, accessToken);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(JsonUtil.transferToJson(response));

				if (response.getErrorResponse() != null) {
					System.out.println(response.getErrorResponse().getErrorMsg());
					fallcount++;
					message = response.getErrorResponse().getErrorMsg();
				}else {
					if (response.getLogisticsOnlineSendResponse().getIsSuccess()) {
						//发货成功
						
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						// 发货成功
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						successcount++;
						message = "发货回传平台成功";

					} else {
						fallcount++;
						message = "发货回传平台失败";

					}
				}
				
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
			} else if (storeRecord.getEcId().equals("MaiDu")) {
				try {
					 storeSettings = storeSettingsDao.select(platOrder.getStoreId());
					
					XS_OrdersGetListResponse result = platOrderMaiDu.MaiDuOrderShip1(expressCorp,logisticsTab, ecExpress,
							storeSettings);
					if (result.getDone()) {
						// 发货成功
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						

						platOrder.setIsShip(1);
						platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
						platOrder.setShipTime(logisticsTab.getShipDate());
						platOrderDao.update(platOrder);
						
						message = result.getMsg();
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent(message);
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8发货
						logRecord.setTypeName("发货");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
						
						successcount++;
					} else {
						if(result.getMsg().equals("订单已发货或已完成，系统默认发货回传成功")) {
							
							// 发货成功
							logisticsTab.setIsShip(1);// 设置物流表状态为已发货
							logisticsTab.setIsBackPlatform(1);// 设置已回传平台
							logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
							logisticsTabDao.update(logisticsTab);// 更新物流表信息
							

							platOrder.setIsShip(1);
							platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
							platOrder.setShipTime(logisticsTab.getShipDate());
							platOrderDao.update(platOrder);
							
							
							message = result.getMsg();
							// 日志记录
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							if (misUser != null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent(message);
							logRecord.setOperatTime(sdf.format(new Date()));
							logRecord.setOperatType(8);// 8发货
							logRecord.setTypeName("发货");
							logRecord.setOperatOrder(platOrder.getEcOrderId());
							logRecordDao.insert(logRecord);
							
							
							successcount++;
							
						}else {
						
						message = result.getMsg();
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent(message);
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8发货
						logRecord.setTypeName("发货");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						fallcount++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					message = "发货异常，请重试";
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8发货
					logRecord.setTypeName("发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					
				}
			} else if (storeRecord.getEcId().equals("SN")) {
				try {
					StoreSettings storeSettings2 = storeSettingsDao.select(logisticsTab.getStoreId());
					DeliverynewAddResponse result = platOrderSN.SNOrderShip(logisticsTab, ecExpress,
							storeSettings2);
					if(result.getSnbody() != null && result.getSnbody().getAddDeliverynew().getSendDetail().get(0).getSendresult().equals("Y")) {
						//发货成功
						
						platOrder.setIsShip(1);
						platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
						platOrder.setShipTime(logisticsTab.getShipDate());
						platOrderDao.update(platOrder);
						
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						
						successcount++;

						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("发货上传成功");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8发货
						logRecord.setTypeName("发货");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
						
					} 
					if(result.getSnerror()!= null) {
						//发货失败
						message = result.getSnerror().getErrorCode();
						//System.out.println("AAAAAAAAAAAAAAAAAA"+message);
						
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("发货失败："+message);
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8发货
						logRecord.setTypeName("发货");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						fallcount++;
					}
					
				} catch (SuningApiException e) {

					e.printStackTrace();
				}
			}else if(storeRecord.getEcId().equals("KaoLa")) {
				try {
					ObjectNode objectNode = JacksonUtil.getObjectNode("");
					objectNode.put("order_id", platOrder.getEcOrderId());
					objectNode.put("express_company_code", ecExpress.getProviderCode());//标准快递公司编码
					objectNode.put("express_no", logisticsTab.getExpressCode());//快递单号
					String json = objectNode.toString();
					StoreSettings storeSettings1 = storeSettingsDao.select(logisticsTab.getStoreId());
					String jdRespStr = ECHelper.getKaola("kaola.logistics.deliver", storeSettings1, json);
					ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
					//System.out.println("返回结果："+jdRespJson);
					if(jdRespJson.has("error_response")) {
						//发货失败
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("发货失败："+jdRespJson.get("error_response").get("subErrors").get(0).get("msg").asText());
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8发货
						logRecord.setTypeName("发货");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						fallcount++;
					}else {
						//发货成功
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						successcount++;

						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("发货上传成功");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8发货
						logRecord.setTypeName("发货");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
					}
				} catch (Exception e) {
					// TODO: handle exception
					fallcount++;
				}
			}
			
			else if (storeRecord.getEcId().equals("XHS")) {
				// System.err.println(platOrder.getEcOrderId());
				// System.err.println(logisticsTab.getExpressCode());
				try {
					storeSettings = storeSettingsDao.select(platOrder.getStoreId());
					if ((platOrder.getEcOrderId() == null || platOrder.getEcOrderId().equals(""))
							|| (logisticsTab.getExpressCode() == null || logisticsTab.getExpressCode().equals(""))
							|| (logisticsTab.getExpressEncd() == null || logisticsTab.getExpressEncd().equals(""))) {
						fallcount++;
						continue;
					}
					ExpressCodeAndName name = ecExpressSettingsDao.selectExpressCode(expressCorp.getCompanyCode());

					if (name == null) {
						//System.err.println("关系链接错误");

					
						fallcount++;
						continue;
					}

					String method = "/ark/open_api/v0/packages/";

					Map<String, String> map = new HashMap<String, String>();
//				订单ID
					map.put("status", "shipped");
					map.put("express_no", logisticsTab.getExpressCode());
					map.put("express_company_code", name.getExpressCode());

					method = method + platOrder.getEcOrderId();

					JSONObject response = ECHelper.getXHSPUT(method, storeSettings, map);

					Boolean xhsSuccess = response.getBoolean("success");
					if (!xhsSuccess) {
						fallcount++;
						// 发货失败
						message = response.getString("error_msg");

					} else {
						// 发货成功
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						successcount++;
						message = "发货回传平台成功";

					}

					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8发货
					logRecord.setTypeName("发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
				} catch (Exception e) {
					// TODO: handle exception
					fallcount++;
				}
			}
			else if (storeRecord.getEcId().equals("XMYP")) {
				String method="setdelivery";
				try {
					storeSettings = storeSettingsDao.select(platOrder.getStoreId());
					if ((platOrder.getEcOrderId() == null || platOrder.getEcOrderId().equals(""))
							|| (logisticsTab.getExpressCode() == null || logisticsTab.getExpressCode().equals(""))
							|| (logisticsTab.getExpressEncd() == null || logisticsTab.getExpressEncd().equals(""))) {
						fallcount++;
						continue;
					}
					ExpressCodeAndName codeName = ecExpressSettingsDao.selectExpressCodeXMYP(expressCorp.getCompanyCode());
					if (codeName == null) {
						System.err.println("关系链接错误");
						fallcount++;
						continue;
					}



					Map<String, String> map = new HashMap<String, String>();
//				订单ID
//				"order_id":34564333, // 订单ID
//				"express_sn":1345454334,// 快递单号
//				"express_name":"顺丰速运", // 快递公司名称, BizName
//				"bizcode":"shunfeng", // 快递公司编号, BizCode
//				"userId":1345434245, // 可以登录商户后台的米聊账号
//				"descr": "", // "发货单备注"
//				"pid":"bigint(商品型号ID)",
//				"donkey_id": 0, // 非小驴发货填0
//				"count":2, // 发货数量
//				"ext": "pid1:count1-pid2:count2" // 发货数量备注
					map.put("order_id",platOrder.getEcOrderId() );// 订单ID
					map.put("express_sn", logisticsTab.getExpressCode());// 快递单号
					map.put("express_name", codeName.getExpressName());// 快递公司名称
					map.put("bizcode", codeName.getExpressCode());// 快递公司编号
					map.put("userId", storeRecord.getSellerId());// 可以登录商户后台的米聊账号
//					map.put("descr", "");// 发货单备注
//					map.put("pid", "0");// bigint(商品型号ID)
					map.put("donkey_id", "0"); // 非小驴发货填0
					XMYPLogisticsTabBean xmypLogisticsTabBean=platOrderXMYP.XMYPapi(method,map,storeSettings,XMYPLogisticsTabBean.class);
//String ssssss="{\"code\":0,\"message\":\"OK\",\"result\":{\"delivery_id\":419122054900568801,\"oid\":4191220549005688,\"uid\":2314717465,\"pid\":0,\"stime\":1576841428,\"atime\":0,\"express_sn\":\"544871878946\",\"bizcode\":\"中通快递\",\"express_name\":\"zhongtong\",\"status\":6,\"descr\":\"\",\"otime\":1576841428,\"ttl\":0,\"count\":1,\"ext\":\"\",\"donkey_id\":0}}";
					if (xmypLogisticsTabBean.getCode() !=0) {
						fallcount++;
						// 发货失败
						message = xmypLogisticsTabBean.getMessage();

					} else {
						// 发货成功
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						logisticsTab.setIsShip(1);// 设置物流表状态为已发货
						logisticsTab.setIsBackPlatform(1);// 设置已回传平台
						logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
						logisticsTabDao.update(logisticsTab);// 更新物流表信息
						successcount++;
						message = "发货回传平台成功";

					}

					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8发货
					logRecord.setTypeName("发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
				} catch (Exception e) {
					// TODO: handle exception
					fallcount++;
				}
			}
			else {
				//不支持的平台
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("发货失败：对应平台暂不支持发货回传，请使用强制发货功能");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8发货
				logRecord.setTypeName("发货");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/platOrderShip", true,
					"平台订单发货成功，本次成功发货并上传" + successcount + "单，失败" + fallcount + "单", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// 根据物流表id获取物流表信息
	// 根据物流表的电商订单号获取电商订单信息
	// 根据订单的店铺ID获取店铺档案
	// 根据店铺档案获取订单平台ID
	// 获取对应平台所开通电子面单的店铺设置
	// 获取快递公司档案信息
	// 获取平台设置的电子面单快递公司信息
	public String tmECExpressOrder(EcExpress ecExpress, ExpressCorp expressCorp, StoreSettings storeSettings,
			EcExpressSettings ecExpressSettings, LogisticsTab logisticsTab, PlatOrder platOrder,
			StoreRecord storeRecord) throws Exception {

		List<Map<String, Object>> PackageItems = new ArrayList<Map<String, Object>>();// package_items PackageItem[]
		Map<String, Object> PackageItem = new HashMap<String, Object>();// package_items PackageItem[] true 包裹里面的商品名称
		for (PlatOrders platOrders : platOrdersDao.select(platOrder.getOrderId())) {
			System.err.println("\t" + platOrders.getGoodName());
			PackageItem.put("item_name", platOrders.getGoodName());// item_name String true 衣服 商品名称
			PackageItem.put("count", platOrders.getGoodNum());// count Number true 123 商品数量
			PackageItems.add(PackageItem);
		}

		Map<String, Object> WaybillAddresss = new HashMap<String, Object>(); // consignee_address WaybillAddress true
																				// 收\发货地址

		WaybillAddresss.put("area", platOrder.getCounty());// area String false 朝阳区 区名称（三级地址）
		WaybillAddresss.put("province", platOrder.getProvince());// province String true 北京 省名称（一级地址）
		WaybillAddresss.put("town", platOrder.getTown());// town String false 八里庄 街道\镇名称（四级地址）
		WaybillAddresss.put("address_detail", platOrder.getRecAddress());// address_detail String true
																			// 朝阳路高井，财满街，财经中心9号楼21单元6013 详细地址
		WaybillAddresss.put("city", platOrder.getCity());// city String false 北京市 市名称（二级地址）

		Map<String, Object> TradeOrderInfo = new HashMap<String, Object>();
		TradeOrderInfo.put("consignee_name", platOrder.getRecName());// consignee_name String true 张三 收货人
		TradeOrderInfo.put("order_channels_type", ecExpressSettings.getPlatId());// 平台id String true TB 订单渠道
		TradeOrderInfo.put("consignee_phone", platOrder.getRecMobile());// consignee_phone String true 13242422352
																		// 收货人联系方式
		TradeOrderInfo.put("consignee_address", WaybillAddresss);// consignee_address WaybillAddress true 收\发货地址
		TradeOrderInfo.put("send_phone", expressCorp.getDeliverPhone());// send_phone String false 13242422352 发货人联系方式

//		TradeOrderInfo.put("weight", 0);// weight Number false 123 包裹重量（克）
		TradeOrderInfo.put("send_name", expressCorp.getDeliver());// send_name String false 李四 发货人姓名
		System.err.println("\t快递服务产品类型编码\t\"没定怎么写\"写死啦、\t STANDARD_EXPRESS");
		TradeOrderInfo.put("product_type", "STANDARD_EXPRESS");// product_type String true STANDARD_EXPRESS 快递服务产品类型编码
		TradeOrderInfo.put("real_user_id", storeRecord.getSellerId());// 店铺设置的会员号 Number true 13123 使用者ID
//		TradeOrderInfo.put("volume", 0);// volume Number false 123 包裹体积（立方厘米）
//			TradeOrderInfo.put("package_id", "包裹号");// package_id String false E12321321-1234567 包裹号(或者ERP订单号)

//			TradeOrderInfo.put("logistics_service_list", LogisticsService);// logistics_service_list LogisticsService[]
//																			// false 物流服务能力集合
		TradeOrderInfo.put("package_items", PackageItems);// package_items PackageItem[] true 包裹里面的商品名称
//			ArrayList<String> vale=new ArrayList<String>();
//			vale.add("adfadf");

		TradeOrderInfo.put("trade_order_list", platOrder.getEcOrderId());// trade_order_list String[] true
																			// 12321321,12321321 交易订单列表

		List<Map<String, Object>> TradeOrderInfos = new ArrayList<Map<String, Object>>();// 订单数据
		TradeOrderInfos.add(TradeOrderInfo);
		Map<String, Object> WaybillAddress = new HashMap<String, Object>();// 发货地址
		WaybillAddress.put("area", ecExpress.getCountryName());// area String false 朝阳区 区名称（三级地址）
		WaybillAddress.put("province", ecExpress.getProvinceName());// province String true 北京 省名称（一级地址）
		WaybillAddress.put("town", ecExpress.getCountrysideName());// town String false 八里庄 街道\镇名称（四级地址）
		WaybillAddress.put("address_detail", ecExpress.getAddress());// address_detail String true
																		// 朝阳路高井，财满街，财经中心9号楼21单元6013 详细地址
		WaybillAddress.put("city", ecExpress.getCityName());// city String false 北京市 市名称（二级地址）

		Map<String, Object> WaybillApplyNewRequest = new HashMap<String, Object>();
		WaybillApplyNewRequest.put("cp_code", ecExpress.getProviderCode());// cp_code String true ZTO 物流服务商编码
		WaybillApplyNewRequest.put("shipping_address", WaybillAddress);// shipping_address
																		// WaybillAddress true
																		// 收\发货地址
		WaybillApplyNewRequest.put("trade_order_info_cols", TradeOrderInfos);// trade_order_info_cols
																				// TradeOrderInfo[]

		Map<String, String> waybill_apply_new_request = new HashMap<String, String>();
		waybill_apply_new_request.put("waybill_apply_new_request", JSON.toJSONString(WaybillApplyNewRequest));

		System.err.println(waybill_apply_new_request);

		String tmOrder = ECHelper.getTB("taobao.wlb.waybill.i.get", storeSettings, waybill_apply_new_request);
		ObjectNode tmobjectNode = JacksonUtil.getObjectNode(tmOrder);
		if (tmobjectNode.has("error_response")) {
			System.err.println("sub_msg" + tmobjectNode.get("error_response").get("sub_msg"));
			return "sub_msg" + tmobjectNode.get("error_response").get("sub_msg");
		}
		ArrayNode codes = (ArrayNode) tmobjectNode.get("wlb_waybill_i_get_response").get("waybill_apply_new_cols")
				.get("waybill_apply_new_info");
		// 成功
		String expressCode = "";
		for (Iterator<JsonNode> itemInfoInfoIterator = codes.iterator(); itemInfoInfoIterator.hasNext();) {
			JsonNode itemInfo = itemInfoInfoIterator.next();

//				short_address	String	hello world	根据收货地址返回大头笔信息
//				trade_order_info	TradeOrderInfo		面单对应的订单列
//				waybill_code	String	hello world	返回的面单号
//				package_center_code	String	123321	集包地代码
//				package_center_name	String	杭州余杭	集包地名称
//				print_config	String	SDFASFAFSAFSADF	打印配置项，传给ali-print组件
//				shipping_branch_code	String	123132	面单号对应的物流服务商网点（分支机构）代码
//				consignee_branch_name	String	余杭一部	包裹对应的派件（收件）物流服务商网点（分支机构）名称
//				shipping_branch_name	String	西湖二部	面单号对于的物流服务商网点（分支机构）名称
//				consignee_branch_code	String	123132	包裹对应的派件（收件）物流服务商网点（分支机构）代码
			itemInfo.get("short_address");
			// 保存获取到的电子面单号
			logisticsTab.setExpressCode(itemInfo.get("waybill_code").asText());// 返回的面单号
//				logisticsTab.setBigShotCode(node.get("bigShotCode").asText());//null
			logisticsTab.setBigShotName(itemInfo.get("short_address").asText());// 根据收货地址返回大头笔信息
			logisticsTab.setGatherCenterCode(itemInfo.get("package_center_code").asText());// 集包地代码
			logisticsTab.setGatherCenterName(itemInfo.get("package_center_name").asText());// 集包地名称
//				logisticsTab.setSecondSectionCode(node.get("secondSectionCode").asText());//二段码
//				logisticsTab.setThirdSectionCode(node.get("thirdSectionCode").asText());//三段码
			logisticsTabDao.update(logisticsTab);
		}

		return null;
	}

	@Override
	public String cancelECExpressOrder(String logisticsNum,String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		Integer successcount = 0;
		Integer fallcount = 0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("successcount", successcount);
		map.put("fallcount", fallcount);
		String[] nums = logisticsNum.split(",");
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以进行操作
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			// 根据物流表的电商订单号获取电商订单信息
			PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
			// 根据订单的店铺ID获取店铺档案
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// 获取对应平台所开通电子面单的店铺设置
			//StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：请设置当前平台开通电子面单的店铺");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消面单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			// 获取快递公司档案信息
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			if(expressCorp.getCompanyCode().equals("ZTO")||expressCorp.getCompanyCode().equals("YTO")||
					expressCorp.getCompanyCode().equals("STO")||expressCorp.getCompanyCode().equals("YUNDA")) {
				
			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：当前订单对应快递公司非加盟型，请使用[取消拦截]功能按钮执行取消操作");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消面单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			// 获取平台设置的电子面单快递公司信息
			EcExpress ecExpress=null;
			try {
				ecExpress = ecExpressDao.select(storeRecord.getEcId(), expressCorp.getCompanyCode(),
						expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),expressCorp.getDetailedAddress());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递公司档案对应电子面单账户信息重复，请检查");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消面单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			if(ecExpress==null) {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递公司档案设置错误，请检查");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消面单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			switch (storeRecord.getEcId()) {
			case "JD":
				JDcancelExpress(logisticsTab, ecExpress, storeSettings, map, misUser);
				break;
			case "TM":
				try {
					TMcancelECExpressOrderYun(logisticsTab, storeSettings, map, ecExpress, misUser);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case "PDD":
				try {
					PDDcancelECExpressOrderYun(logisticsTab, storeSettings, map, ecExpress, misUser);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			default:
				break;
			}
			

		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/cancelECExpressOrder", true,
					"取消电子面单成功，本次成功取消面单" + map.get("successcount") + "条，失败" + map.get("fallcount") + "条", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	private String PDDcancelECExpressOrderYun(LogisticsTab logisticsTab, StoreSettings storeSettings,
			Map<String, Integer> map, EcExpress ecExpress,MisUser misUser) throws Exception {

//		取消电子面单 
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddWaybillCancelRequest request = new PddWaybillCancelRequest();
		request.setWaybillCode(logisticsTab.getExpressCode());// 电子面单号码
		request.setWpCode(ecExpress.getProviderCode());// CP快递公司编码
		PddWaybillCancelResponse response = client.syncInvoke(request, accessToken);
		System.out.println(JsonUtil.transferToJson(response));

		if (response.getErrorResponse() != null) {
			map.put("fallcount", map.get("fallcount") + 1);
			System.err.println(map.get("fallcount"));
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("取消面单成功");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消免单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
			
			return response.getErrorResponse().getErrorMsg();
		}

		if (response.getPddWaybillCancelResponse().getCancelResult()) {
			logisticsTab.setExpressCode(null);
			logisticsTab.setBigShotCode(null);
			logisticsTab.setBigShotName(null);
			logisticsTab.setGatherCenterCode(null);
			logisticsTab.setGatherCenterName(null);
			logisticsTab.setSecondSectionCode(null);
			logisticsTab.setThirdSectionCode(null);
			logisticsTabDao.update(logisticsTab);
			map.put("successcount", map.get("successcount") + 1);
			// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(misUser.getAccNum());

						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("取消面单成功");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(17);// 17取消免单
						logRecord.setTypeName("取消面单");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
		} else {
			map.put("fallcount", map.get("fallcount") + 1);
			//解绑失败
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("取消面单失败");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消免单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
		}
		return "成功取消";

	}

	/**
	 * 取消电子面单
	 * 
	 * @param logisticsTab  物流表
	 * @param storeSettings 店铺信息设置
	 * @param successcount  成功条数
	 * @param fallcount     失败条数
	 */
	private String TMcancelECExpressOrder(LogisticsTab logisticsTab, StoreSettings storeSettings,
			Map<String, Integer> map, EcExpress ecExpress) throws Exception, IOException {

//		取消电子面单 taobao.wlb.waybill.i.cancel
		Map<String, Object> waybill = new HashMap<String, Object>();
//		waybill.put("real_user_id", value);//	Number	false	2134234234	面单使用者编号
		waybill.put("trade_order_list", logisticsTab.getEcOrderId());// String[] true 交易订单列表
		waybill.put("cp_code", ecExpress.getProviderCode());// String true EMS CP快递公司编码
		waybill.put("waybill_code", logisticsTab.getExpressCode());// String true 1100222969702 电子面单号码
//		waybill.put("package_id", value);//String	false	E12321321-1234567	ERP订单号或包裹号
		Map<String, String> waybill_apply_cancel_request = new HashMap<String, String>();
		waybill_apply_cancel_request.put("waybill_apply_cancel_request", JSON.toJSONString(waybill));
		System.err.println(JSON.toJSONString(waybill_apply_cancel_request));

		String tmOrder = ECHelper.getTB("taobao.wlb.waybill.i.cancel", storeSettings, waybill_apply_cancel_request);
		ObjectNode tmobjectNode = JacksonUtil.getObjectNode(tmOrder);

		if (tmobjectNode.has("error_response")) {
			map.put("fallcount", map.get("fallcount") + 1);
			System.err.println(map.get("fallcount"));
			System.err.println("sub_msg" + tmobjectNode.get("error_response").get("msg"));
			return "sub_msg" + tmobjectNode.get("error_response").get("msg");
		}

		if (tmobjectNode.get("wlb_waybill_i_cancel_response").get("cancel_result").asBoolean()) {
			logisticsTab.setExpressCode(null);
			logisticsTab.setBigShotCode(null);
			logisticsTab.setBigShotName(null);
			logisticsTab.setGatherCenterCode(null);
			logisticsTab.setGatherCenterName(null);
			logisticsTab.setSecondSectionCode(null);
			logisticsTab.setThirdSectionCode(null);
			logisticsTabDao.update(logisticsTab);
			map.put("successcount", map.get("successcount") + 1);

		} else {
			map.put("fallcount", map.get("fallcount") + 1);
		}
		return "成功取消";

	}

	@Override
	public Map findExpressCodeByOrderId(PlatOrder platOrder, List<PlatOrders> platOrders) {
		// TODO Auto-generated method stub
		Map map = new HashMap<>();
		StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
		if (!StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
			map.put("message", "当前订单发货仓库为空，不能自动匹配对应快递公司");
			map.put("flag", "false");
			map.put("expressCode", "");
			map.put("templateId", "");
		}
		List<WhsPlatExpressMapp> whsPlatExpressMapps = whsPlatExpressMappDao
				.selectListByPlatIdAndWhsCode(storeRecord.getEcId(), platOrder.getDeliverWhs());

		if (whsPlatExpressMapps.size() == 0) {
			map.put("message", "当前订单发货仓库未设置对应快递公司");
			map.put("flag", "false");
			map.put("expressCode", "");
			map.put("templateId", "");
		} else if (whsPlatExpressMapps.size() == 1) {
			// 当所设置的快递公司只有一个时，直接返回快递公司编码
			map.put("message", "");
			map.put("flag", "true");
			map.put("expressCode", whsPlatExpressMapps.get(0).getExpressId());
			map.put("templateId", whsPlatExpressMapps.get(0).getTemplateId());
		} else {
			// 先计算销售单明细中所有商品的重量
			BigDecimal allWeight = BigDecimal.ZERO;
			for (int i = 0; i < platOrders.size(); i++) {
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
				if (invtyDoc.getWeight() != null) {
					allWeight = allWeight
							.add(invtyDoc.getWeight().multiply(new BigDecimal(platOrders.get(i).getInvNum())));
				}
			}
			// 当同一仓库对同一平台设置的快递公司超过1个时
			// 根据设置中的最大最小重量顺序循环匹配
			for (int i = 0; i < whsPlatExpressMapps.size(); i++) {
				// 判断销售单明细重量综合是否介于所设置的重量之间
				if ((allWeight.compareTo(whsPlatExpressMapps.get(i).getWeightMin()) > 0
						|| allWeight.compareTo(whsPlatExpressMapps.get(i).getWeightMin()) == 0)
						&& allWeight.compareTo(whsPlatExpressMapps.get(i).getWeightMax()) < 0) {
					map.put("message", "");
					map.put("flag", "true");
					map.put("expressCode", whsPlatExpressMapps.get(i).getExpressId());
					map.put("templateId", whsPlatExpressMapps.get(i).getTemplateId());
					break;
				}
				if (i + 1 == whsPlatExpressMapps.size()) {
					// 当目前循环为最后一次循环时，走到这一步说明匹配完了所有的快递公司都没有符合重量的
					map.put("message", "订单总重量" + allWeight.setScale(3) + "kg不符合仓库平台快递公司映射档案中所设置的重量规则");
					map.put("flag", "false");
					map.put("expressCode", "");
					map.put("templateId", "");
				}
			}
		}

		return map;
	}

	/**
	 * 取消电子面单
	 * 
	 * @param logisticsTab  物流表
	 * @param storeSettings 店铺信息设置
	 * @param successcount  成功条数
	 * @param fallcount     失败条数
	 */
	private String PDDcancelECExpressOrder(LogisticsTab logisticsTab, StoreSettings storeSettings,
			Map<String, Integer> map, EcExpress ecExpress) throws Exception, IOException {
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);
		PddWaybillCancelRequest request = new PddWaybillCancelRequest();
		request.setWaybillCode(logisticsTab.getExpressCode());
		request.setWpCode(ecExpress.getProviderCode());
		PddWaybillCancelResponse response = client.syncInvoke(request, accessToken);
		System.out.println(JsonUtil.transferToJson(response));

		if (response.getErrorResponse() != null) {
			map.put("fallcount", map.get("fallcount") + 1);
			System.err.println(map.get("fallcount"));
			System.err.println("sub_msg" + response.getErrorResponse().getErrorMsg());
			return "sub_msg" + response.getErrorResponse().getErrorMsg();
		}

		if (response.getPddWaybillCancelResponse().getCancelResult()) {
			logisticsTab.setExpressCode(null);
			logisticsTab.setBigShotCode(null);
			logisticsTab.setBigShotName(null);
			logisticsTab.setGatherCenterCode(null);
			logisticsTab.setGatherCenterName(null);
			logisticsTab.setSecondSectionCode(null);
			logisticsTab.setThirdSectionCode(null);
			logisticsTabDao.update(logisticsTab);
			map.put("successcount", map.get("successcount") + 1);

		} else {
			map.put("fallcount", map.get("fallcount") + 1);
		}
		return "成功取消";

	}

	void PDD() {
		String clientId = "your app clientId";

		String clientSecret = "your app clientSecret";

		String refreshToken = "your app refreshToken";

		String code = "your code";
		PopAccessTokenClient accessTokenClient = new PopAccessTokenClient(clientId, clientSecret);
		// 生成AccessToken
		try {
			AccessTokenResponse response = accessTokenClient.generate(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 刷新AccessToken
		try {

			AccessTokenResponse response = accessTokenClient.refresh(refreshToken);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	// 电子面单云打印接口
	public JSONObject tmECExpressOrderYun(EcExpress ecExpress, ExpressCorp expressCorp, StoreSettings storeSettings,
			EcExpressSettings ecExpressSettings, LogisticsTab logisticsTab, PlatOrder platOrder,
			StoreRecord storeRecord,MisUser misUser) throws Exception {
		JSONObject asda = new JSONObject();

		CainiaoWaybillIiGetRequest req = new CainiaoWaybillIiGetRequest();
		WaybillCloudPrintApplyNewRequest waybillCloudPrintApplyNewRequest = new WaybillCloudPrintApplyNewRequest();
		waybillCloudPrintApplyNewRequest.setCpCode(ecExpress.getProviderCode());// 物流公司Code，长度小于20
//		obj1.setProductCode("目前已经不推荐使用此字段，请不要使用");
		UserInfoDto sender = new UserInfoDto();// 发货人信息
		AddressDto obj3 = new AddressDto();// 发货地址需要通过search接口

		obj3.setCity(ecExpress.getCityName());
		obj3.setDetail(ecExpress.getAddress());
		obj3.setDistrict(ecExpress.getCountryName());
		obj3.setProvince(ecExpress.getProvinceName());
		obj3.setTown(ecExpress.getCountrysideName());
		sender.setAddress(obj3);
		sender.setMobile(expressCorp.getDeliverPhone());
		sender.setName(expressCorp.getDeliver());// 姓名，长度小于40
		List<TradeOrderInfoDto> tradeOrderInfoDto = new ArrayList<TradeOrderInfoDto>();
		TradeOrderInfoDto obj6 = new TradeOrderInfoDto();

		// obj6.setLogisticsServices("如不需要特殊服务，该值为空");
		obj6.setObjectId("1");// 请求ID
//		venderId
		obj6.setUserId(Long.valueOf(storeSettings.getVenderId()));// 使用者ID（使用电子面单账号的实际商家ID，如存在一个电子面单账号多个店铺使用时，请传入店铺的商家ID）
		// 天猫平台云打印模板

		JSONObject jsonObjectdate = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		List<WhsPlatExpressMapp> list = whsPlatExpressMappDao.selectCloudPrint("TM", ecExpress.getProviderCode());
		if (list.size() > 0) {
//			obj6.setTemplateUrl("http://cloudprint.cainiao.com/template/standard/" + list.get(0).getCloudPrint());// 云打印标准模板URL（组装云打印结果使用，值格式http://cloudprint.cainiao.com/template/standard/${模板ID}）
			obj6.setTemplateUrl(list.get(0).getCloudPrint());// 云打印标准模板URL（组装云打印结果使用，值格式http://cloudprint.cainiao.com/template/standard/${模板ID}）

			jsonObjectdate.put("templateURL", list.get(0).getCloudPrintCustom());
		} else {
			asda.put("k", "无云打印标准模版");
			return asda;
		}

		UserInfoDto recipient = new UserInfoDto();// 收件人信息
		AddressDto obj16 = new AddressDto();
		obj16.setCity(platOrder.getCity());
		obj16.setDetail(platOrder.getRecAddress());
		obj16.setDistrict(platOrder.getCounty());
		obj16.setProvince(platOrder.getProvince());
		obj16.setTown(platOrder.getTown());
		recipient.setAddress(obj16);
		recipient.setMobile(platOrder.getRecMobile());
		recipient.setName(platOrder.getRecName());
		obj6.setRecipient(recipient);

		OrderInfoDto orderInfo = new OrderInfoDto();
		orderInfo.setOrderChannelsType(ecExpressSettings.getPlatId());
		List<String> tradeOrderList = new ArrayList<String>();
		tradeOrderList.add(platOrder.getEcOrderId());
		orderInfo.setTradeOrderList(tradeOrderList);

		obj6.setOrderInfo(orderInfo);// 订单信息

		tradeOrderInfoDto.add(obj6);

		PackageInfoDto obj10 = new PackageInfoDto();// 包裹信息
		obj10.setId(logisticsTab.getOrderId());//包裹id，用于拆合单场景（只能传入数字、字母和下划线；批量请求时值不得重复，大小写敏感，即123A,123a 不可当做不同ID，否则存在一定可能取号失败）
		List<Item> list12 = new ArrayList<Item>();

		jsonObjectdate.put("data", jsonObject);

		jsonObject.put("ItemName", "");
		jsonObject.put("ItemNum", "");

		for (PlatOrders platOrders : platOrdersDao.select(platOrder.getOrderId())) {
			Item obj13 = new Item();
			obj13.setCount(platOrders.getGoodNum().longValue());// count Number true 123 商品数量
			obj13.setName(platOrders.getGoodName());// item_name String true 衣服 商品名称
			list12.add(obj13);
			String ItemNum = "";
			String ItemName = "";
			InvtyDoc inv = invtyDocDao.selectAllByInvtyEncd(platOrders.getInvId());
			if (inv.getInvtyNm().length() > 24) {
				String str = inv.getInvtyNm().substring(0, 24) + "\n";
				String str1 = inv.getInvtyNm().substring(24,
						inv.getInvtyNm().length() > 48 ? 48 : inv.getInvtyNm().length()) + "\n";
				ItemName = jsonObject.get("ItemName").toString() + str + str1;

				ItemNum = "\n" + platOrders.getInvNum() + "\n";
				ItemNum = jsonObject.get("ItemNum").toString() + ItemNum;
			} else {
				ItemName = jsonObject.get("ItemName").toString() + inv.getInvtyNm() + "\n";
				ItemNum = jsonObject.get("ItemNum").toString() + platOrders.getInvNum() + "\n";

			}

			jsonObject.put("ItemName", ItemName);
			jsonObject.put("ItemNum", ItemNum);
		}
		obj10.setItems(list12);
		obj6.setPackageInfo(obj10);

		waybillCloudPrintApplyNewRequest.setSender(sender);// 发货人信息
		waybillCloudPrintApplyNewRequest.setTradeOrderInfoDtos(tradeOrderInfoDto);// 请求面单信息，数量限制为10
		waybillCloudPrintApplyNewRequest.setNeedEncrypt(false);
		req.setParamWaybillCloudPrintApplyNewRequest(waybillCloudPrintApplyNewRequest);

		// 转发
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("path", CainiaoWaybillIiGetRequest.class.getName());
		maps.put("taobaoObject", JSON.toJSONString(req));

//		CainiaoWaybillIiGetResponse rsp = client.execute(req, sessionKey);
		String taobao = ECHelper.getTB("", storeSettings, maps);
//		String  rsps=JSONObject.parseObject(taobao,Feature.IgnoreNotMatch).getString("body");
		CainiaoWaybillIiGetResponse rsp = JSONObject.parseObject(taobao, CainiaoWaybillIiGetResponse.class);

		//System.out.println(rsp.getBody());
		if (!rsp.isSuccess()) {
			asda.put("k", rsp.getSubMsg());
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("失败："+rsp.getSubMsg());
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(14);// 14取电子面单
			logRecord.setTypeName("取电子面单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			return asda;
		}
//		{"body":"{\"cainiao_waybill_ii_get_response\":{\"modules\":{\"waybill_cloud_print_response\":[{\"object_id\":\"1\",\"print_data\":\"{\\\"data\\\":{\\\"_dataFrom\\\":\\\"waybill\\\",\\\"adsInfo\\\":{\\\"bannerUrl\\\":\\\"http:\\/\\/cdn-cloudprint.cainiao.com\\/waybill-print\\/cloudprint-imgs\\/c6fc411b400f4471a4d28e0a4a18c40b.jpg\\\"},\\\"cpCode\\\":\\\"ZTO\\\",\\\"needEncrypt\\\":false,\\\"packageInfo\\\":{\\\"items\\\":[{\\\"count\\\":1,\\\"name\\\":\\\"【安满旗舰店】安满孕妇奶粉正品 怀孕期800g罐装 孕早期妈妈奶粉\\\"}],\\\"volume\\\":0,\\\"weight\\\":0},\\\"parent\\\":false,\\\"recipient\\\":{\\\"address\\\":{\\\"city\\\":\\\"北京市\\\",\\\"detail\\\":\\\"广安门内街道广华轩小区3号楼208\\\",\\\"district\\\":\\\"西城区\\\",\\\"province\\\":\\\"北京\\\"},\\\"mobile\\\":\\\"15810104492\\\",\\\"name\\\":\\\"卢天顺\\\"},\\\"routingInfo\\\":{\\\"consolidation\\\":{\\\"name\\\":\\\"北京\\\"},\\\"origin\\\":{\\\"code\\\":\\\"02293\\\",\\\"name\\\":\\\"天津市场部\\\"},\\\"receiveBranch\\\":{\\\"code\\\":\\\"01090\\\"},\\\"routeCode\\\":\\\"22-01 13\\\",\\\"sortation\\\":{\\\"name\\\":\\\"800-\\\"},\\\"startCenter\\\":{},\\\"terminalCenter\\\":{}},\\\"sender\\\":{\\\"address\\\":{\\\"city\\\":\\\"天津\\\",\\\"detail\\\":\\\"新世纪产业园世纪西路雍德科技院内\\\",\\\"district\\\":\\\"武清区\\\",\\\"province\\\":\\\"天津市\\\"},\\\"mobile\\\":\\\"18616950914\\\",\\\"name\\\":\\\"姜海洋\\\"},\\\"shippingOption\\\":{\\\"code\\\":\\\"STANDARD_EXPRESS\\\",\\\"title\\\":\\\"标准快递\\\"},\\\"waybillCode\\\":\\\"75163074465093\\\"},\\\"signature\\\":\\\"MD:8wHe5ZI7tZ+YWNDEBFIqBg==\\\",\\\"templateURL\\\":\\\"http:\\/\\/cloudprint.cainiao.com\\/template\\/standard\\/4802048\\\"}\",\"waybill_code\":\"75163074465093\"}]},\"request_id\":\"45mfxf7g8oy7\"}}","errorCode":"","headerContent":{"Transfer-Encoding":"chunked","":"HTTP/1.1 200 OK","Location-Host":"top011010242240.na61","Server":"Tengine","Content-Encoding":"gzip","Connection":"close","top-bodylength":"1331","Vary":"Accept-Encoding","Application-Host":"11.10.242.240","Date":"Sat, 20 Jul 2019 07:24:57 GMT","Content-Type":"text/javascript;charset=UTF-8"},"modules":[{"objectId":"1","printData":"{\"data\":{\"_dataFrom\":\"waybill\",\"adsInfo\":{\"bannerUrl\":\"http://cdn-cloudprint.cainiao.com/waybill-print/cloudprint-imgs/c6fc411b400f4471a4d28e0a4a18c40b.jpg\"},\"cpCode\":\"ZTO\",\"needEncrypt\":false,\"packageInfo\":{\"items\":[{\"count\":1,\"name\":\"【安满旗舰店】安满孕妇奶粉正品 怀孕期800g罐装 孕早期妈妈奶粉\"}],\"volume\":0,\"weight\":0},\"parent\":false,\"recipient\":{\"address\":{\"city\":\"北京市\",\"detail\":\"广安门内街道广华轩小区3号楼208\",\"district\":\"西城区\",\"province\":\"北京\"},\"mobile\":\"15810104492\",\"name\":\"卢天顺\"},\"routingInfo\":{\"consolidation\":{\"name\":\"北京\"},\"origin\":{\"code\":\"02293\",\"name\":\"天津市场部\"},\"receiveBranch\":{\"code\":\"01090\"},\"routeCode\":\"22-01 13\",\"sortation\":{\"name\":\"800-\"},\"startCenter\":{},\"terminalCenter\":{}},\"sender\":{\"address\":{\"city\":\"天津\",\"detail\":\"新世纪产业园世纪西路雍德科技院内\",\"district\":\"武清区\",\"province\":\"天津市\"},\"mobile\":\"18616950914\",\"name\":\"姜海洋\"},\"shippingOption\":{\"code\":\"STANDARD_EXPRESS\",\"title\":\"标准快递\"},\"waybillCode\":\"75163074465093\"},\"signature\":\"MD:8wHe5ZI7tZ+YWNDEBFIqBg==\",\"templateURL\":\"http://cloudprint.cainiao.com/template/standard/4802048\"}","waybillCode":"75163074465093"}],"msg":"","params":{"param_waybill_cloud_print_apply_new_request":"{\"cp_code\":\"ZTO\",\"sender\":{\"address\":{\"city\":\"天津\",\"detail\":\"新世纪产业园世纪西路雍德科技院内\",\"district\":\"武清区\",\"province\":\"天津市\"},\"mobile\":\"18616950914\",\"name\":\"姜海洋\"},\"trade_order_info_dtos\":[{\"object_id\":\"1\",\"order_info\":{\"order_channels_type\":\"TM\",\"trade_order_list\":[\"545400098176560866\"]},\"package_info\":{\"items\":[{\"count\":1,\"name\":\"【安满旗舰店】安满孕妇奶粉正品 怀孕期800g罐装 孕早期妈妈奶粉\"}]},\"recipient\":{\"address\":{\"city\":\"北京市\",\"detail\":\"广安门内街道广华轩小区3号楼208\",\"district\":\"西城区\",\"province\":\"北京\"},\"mobile\":\"15810104492\",\"name\":\"卢天顺\"},\"template_url\":\"http:\\/\\/cloudprint.cainiao.com\\/template\\/standard\\/${4802048}\",\"user_id\":72356451}]}"},"requestId":"45mfxf7g8oy7","requestUrl":"http://gw.api.taobao.com/router/rest?param_waybill_cloud_print_apply_new_request={"cp_code":"ZTO","sender":{"address":{"city":"天津","detail":"新世纪产业园世纪西路雍德科技院内","district":"武清区","province":"天津市"},"mobile":"18616950914","name":"姜海洋"},"trade_order_info_dtos":[{"object_id":"1","order_info":{"order_channels_type":"TM","trade_order_list":["545400098176560866"]},"package_info":{"items":[{"count":1,"name":"【安满旗舰店】安满孕妇奶粉正品+怀孕期800g罐装+孕早期妈妈奶粉"}]},"recipient":{"address":{"city":"北京市","detail":"广安门内街道广华轩小区3号楼208","district":"西城区","province":"北京"},"mobile":"15810104492","name":"卢天顺"},"template_url":"http:\/\/cloudprint.cainiao.com\/template\/standard\/${4802048}","user_id":72356451}]}&app_key=27536575&method=cainiao.waybill.ii.get&v=2.0&sign=736A450E5D76CDB7C42F912FB6D14CF48700EC280777F7BEBD346429562C8A58&timestamp=2019-07-20+15:24:57&partner_id=top-sdk-java-20190613&session=6101027bff53a0fc1df75f0df20659f96501c785a851da42843305610&format=json&sign_method=hmac-sha256","subCode":"","subMsg":"","success":true}

//		JSONObject  rsp=JSONObject.parseObject(taobao);
//		System.out.println(rsp.containsKey("error_response"));
//		if (rsp.containsKey("error_response")) {
//			System.err.println(rsp.getJSONObject("error_response").getString("sub_msg"));
//			return rsp.getJSONObject("error_response").getString("sub_msg");
//		}

		for (com.taobao.api.response.CainiaoWaybillIiGetResponse.WaybillCloudPrintResponse wcpr : rsp.getModules()) {

			JSONObject printData = JSON.parseObject(wcpr.getPrintData());

			logisticsTab.setExpressCode(printData.getJSONObject("data").getString("waybillCode"));// 返回的面单号

//			logisticsTab.setBigShotCode(node.get("bigShotCode").asText());//null
			logisticsTab.setBigShotName(printData.getJSONObject("data").getJSONObject("routingInfo")
					.getJSONObject("sortation").getString("name"));// 根据收货地址返回大头笔信息
			logisticsTab.setGatherCenterCode(printData.getJSONObject("data").getJSONObject("routingInfo")
					.getJSONObject("consolidation").getString("name"));// 集包地代码
			logisticsTab.setGatherCenterName(printData.getJSONObject("data").getJSONObject("routingInfo")
					.getJSONObject("consolidation").getString("code"));// 集包地名称
//			logisticsTab.setSecondSectionCode(node.get("secondSectionCode").asText());//二段码
			logisticsTab.setThirdSectionCode(
					printData.getJSONObject("data").getJSONObject("routingInfo").getString("routeCode"));// 三段码
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("快递公司："+expressCorp.getExpressNm()+"，快递单号："+printData.getJSONObject("data").getString("waybillCode"));
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(14);// 14取电子面单
			logRecord.setTypeName("取电子面单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			logisticsTabDao.update(logisticsTab);
		}
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		asda.put("list", jsonObjects);
		for (com.taobao.api.response.CainiaoWaybillIiGetResponse.WaybillCloudPrintResponse waybillCloudPrintResponse : rsp
				.getModules()) {
			String string = waybillCloudPrintResponse.getPrintData();
			JSONObject printData = JSON.parseObject(string);

			jsonObjects.add(printData);
			jsonObjects.add(jsonObjectdate);

		}

		return asda;
	}

	private String TMcancelECExpressOrderYun(LogisticsTab logisticsTab, StoreSettings storeSettings,
			Map<String, Integer> map, EcExpress ecExpress, MisUser misUser) throws ApiException, NoSuchAlgorithmException, IOException {

//		取消电子面单 cainiao.waybill.ii.cancel
		CainiaoWaybillIiCancelRequest req = new CainiaoWaybillIiCancelRequest();
		req.setCpCode(ecExpress.getProviderCode());// String true EMS CP快递公司编码
		req.setWaybillCode(logisticsTab.getExpressCode());// String true 1100222969702 电子面单号码

		// 转发
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("path", CainiaoWaybillIiCancelRequest.class.getName());
		maps.put("taobaoObject", JSON.toJSONString(req));

		String taobao = ECHelper.getTB("", storeSettings, maps);
		CainiaoWaybillIiCancelResponse rsp = JSONObject.parseObject(taobao, CainiaoWaybillIiCancelResponse.class);

		System.out.println(rsp.getBody());

		if (!rsp.isSuccess()) {
			map.put("fallcount", map.get("fallcount") + 1);
			System.err.println(map.get("fallcount"));
			//解绑失败
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("取消面单失败,"+rsp.getSubMsg());
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消免单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
			return rsp.getSubMsg();
		}

		if (rsp.getCancelResult()) {
			logisticsTab.setExpressCode(null);
			logisticsTab.setBigShotCode(null);
			logisticsTab.setBigShotName(null);
			logisticsTab.setGatherCenterCode(null);
			logisticsTab.setGatherCenterName(null);
			logisticsTab.setSecondSectionCode(null);
			logisticsTab.setThirdSectionCode(null);
			logisticsTabDao.update(logisticsTab);
			map.put("successcount", map.get("successcount") + 1);
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("取消面单成功");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消免单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
		} else {
			//解绑失败
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("取消面单失败");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消免单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
			map.put("fallcount", map.get("fallcount") + 1);
		}
		return "成功取消";

	}

	/**
	 * id放入list
	 * 
	 * @param id id(多个已逗号分隔)
	 * @return List集合
	 */
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			//System.out.println(str[i] + "=====impl 的 str[" + i + "]");
			list.add(str[i]);
		}
		return list;
	}

	@Override
	public String selectPrint(String ordrNum) {
		String resp = "";
		List<String> list = getList(ordrNum);
		List<LogisticsTab> selectList = logisticsTabDao.selectListPrint(list);

		try {
			resp = BaseJson.returnRespList("ec/logisticsTab/selectList", true, "分页查询成功！", selectList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String print(String orderNum, String accNum) {
		// TODO Auto-generated method stub
		// 更新物流表的打印状态
		String resp = "";
		String[] nums = orderNum.split(",");
		// List<String> resultList= new ArrayList<>(Arrays.asList(nums));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			logisticsTab.setIsPrint(1);// 设置已打印
			logisticsTab.setPrintTime(sdf.format(new Date()));
			logisticsTabDao.update(logisticsTab);

			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(15);// 15打印快递单
			logRecord.setTypeName("打印快递单");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/print", true, "物流表更新打印状态完成", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String achieveECExpressCloudPrint(String logisticsNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "云打印消息";
		String[] nums = logisticsNum.split(",");
		List<JSONObject> list = new ArrayList<>();
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以获取电子面单
				continue;
			}
			// 根据物流表的电商订单号获取电商订单信息
			//PlatOrder platOrder = platOrderDao.selectByEcOrderId(logisticsTab.getEcOrderId());
			
			PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
			// 根据订单的店铺ID获取店铺档案
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// 根据店铺档案获取订单平台ID
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// 获取对应平台所开通电子面单的店铺设置
			StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
			// 获取快递公司档案信息
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			// 获取平台设置的电子面单快递公司信息
			EcExpress ecExpress=null;
			StoreRecord storeRecord2 = storeRecordDao.select(ecExpressSettings.getStoreId());
			try {
				
				if (expressCorp.getCompanyCode().equals("JDWL")) {
					ecExpress = ecExpressDao.select(storeRecord2.getEcId(), "JD",
							expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),
							expressCorp.getDetailedAddress());
				}else {
					ecExpress = ecExpressDao.select(storeRecord2.getEcId(), expressCorp.getCompanyCode(),
							expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),
							expressCorp.getDetailedAddress());
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递公司档案对应电子面单账户信息重复，请检查");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
				continue;
			}
			if(ecExpress==null) {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：快递公司档案设置错误，请检查");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
				continue;
			}
			//MisUser misUser = misUserDao.select(accNum);

			if (storeRecord2.getEcId().equals("TM")) {
				try {
					JSONObject string = tmECExpressOrderYun(ecExpress, expressCorp, storeSettings, ecExpressSettings,
							logisticsTab, platOrder, storeRecord2,misUser);

					list.add(string);
				} catch (Exception e) {
					e.printStackTrace();
					message = "解析返回数据异常";
				}

			} else if (storeRecord2.getEcId().equals("PDD")) {
				try {
					JSONObject string = PDDECExpressOrderYun(ecExpress, expressCorp, storeSettings, ecExpressSettings,
							logisticsTab, platOrder, storeRecord2,misUser);
					list.add(string);
				} catch (Exception e) {
					e.printStackTrace();
					message = "解析返回数据异常";
				}
			} else {
				message = "有不属于云打印的单据";

			}
		}

		if (list.size() > 0) {
			try {
				resp = BaseJson.returnRespObjList("ec/logisticsTab/achieveECExpressCloudPrint", true, message, null,
						list);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressCloudPrint", false, "无打印信息", null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resp;

	}
	/**
	 * 京东解绑电子面单
	 * @return
	 */
	public String JDcancelExpress(LogisticsTab logisticsTab,EcExpress ecExpress,StoreSettings storeSettings,Map<String, Integer> map,MisUser misUser) {
		
		String result = "";
		ObjectNode objectNode;
		try {
			objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("waybillCode", logisticsTab.getExpressCode());// 运单号
			objectNode.put("providerId", ecExpress.getProviderId());// 承运商ID
			objectNode.put("providerCode", ecExpress.getProviderCode());// 承运商编码
			String jdRespStr = ECHelper.getJD("jingdong.ldop.alpha.waybill.api.unbind", storeSettings,
					objectNode.toString());
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			System.out.println("解绑电子面单返回结果："+jdRespStr);
			if(jdRespJson.get("jingdong_ldop_alpha_waybill_api_unbind_responce").get("resultInfo").get("statusCode").toString().equals("0")) {
				//解绑电子面单成功
				logisticsTab.setExpressCode(null);//清空面单号
				logisticsTab.setBigShotCode(null);
				logisticsTab.setBigShotName(null);
				logisticsTab.setGatherCenterCode(null);
				logisticsTab.setGatherCenterName(null);
				logisticsTab.setSecondSectionCode(null);
				logisticsTab.setThirdSectionCode(null);
				logisticsTabDao.update(logisticsTab);
				
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getAccNum());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("取消面单成功");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消免单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				
				map.put("successcount", map.get("successcount") + 1);
				
			}else {
				//解绑失败
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getAccNum());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("取消面单失败,"+jdRespJson.get("jingdong_ldop_alpha_waybill_api_unbind_responce").get("resultInfo").get("statusMessage").toString());
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消免单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				
				map.put("fallcount", map.get("fallcount")+1);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	/**
	 * 强制发货
	 */
	@Override
	public String forceShip(String ordrNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		int successcount = 0;
		int fallcount = 0;
		String[] nums = ordrNum.split(",");
		String message = "";
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {

			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以进行发货操作
				fallcount++;
				continue;
			}
			
			if(logisticsTab.getIsPick()==0) {
				//如果物流表状态为未拣货，不允许执行发货
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("强制发货失败：物流表未开始拣货，请先执行拣货标记");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(18);// 8发货
				logRecord.setTypeName("强制发货");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(StringUtils.isEmpty(logisticsTab.getExpressCode())) {
				//如果快递单号为空，不允许执行发货
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("强制发货失败：快递单号为空");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(18);// 8发货
				logRecord.setTypeName("强制发货");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			if (logisticsTab.getIsShip()==0) {//状态为已发货的不能再次发货
				if(StringUtils.isNotEmpty(logisticsTab.getExpressCode())) {
					
				
				try {
					logisticsTab.setIsShip(1);// 设置物流表状态为已发货
					logisticsTab.setIsBackPlatform(0);// 设置未回传平台
					logisticsTab.setShipDate(sdf.format(new Date()));// 设置发货时间
					
					logisticsTabDao.update(logisticsTab);// 更新物流表信息
					PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
					platOrder.setIsShip(1);
					platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
					platOrder.setShipTime(logisticsTab.getShipDate());
					platOrderDao.update(platOrder);

					message = "强制发货成功";
					
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(18);// 18强制发货
					logRecord.setTypeName("强制发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					successcount++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					message = "强制发货异常";
					fallcount++;
					e.printStackTrace();
				} 
				
				}else {
					fallcount++;
				}
				
			}else {
				fallcount++;
			}
			

		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/forceShip", true,
					"强制发货成功，本次成功发货" + successcount + "单，失败" + fallcount + "单", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String cancelShip(String ordrNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		int successcount = 0;
		int fallcount = 0;
		String[] nums = ordrNum.split(",");
		String message = "";
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {

			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以进行取消发货操作
				fallcount++;
				continue;
			}
			
			if (logisticsTab.getIsShip()==1) {//状态为待发货的不能取消发货
				PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
				try {
					logisticsTab.setIsShip(0);// 设置物流表状态为已发货
					logisticsTab.setIsBackPlatform(0);// 设置未回传平台
					logisticsTab.setShipDate(null);// 设置发货时间
					logisticsTab.setExpressCode(null);
					logisticsTabDao.update(logisticsTab);// 更新物流表信息
					
					platOrder.setIsShip(0);
					platOrder.setExpressNo(logisticsTab.getExpressCode());//更新订单发货的快递单号
					platOrder.setShipTime(null);
					platOrderDao.update(platOrder);

					message = "取消发货成功";
					
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(19);// 19取消发货
					logRecord.setTypeName("取消发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					successcount++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					message = "取消发货异常";
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(19);// 19取消发货
					logRecord.setTypeName("取消发货");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					e.printStackTrace();
				} 
				
			}else {
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/cancelShip", true,
					"取消发货成功，本次成功取消" + successcount + "单，失败" + fallcount + "单", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String exportList(Map map) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		try {
			map.put("whsId", CommonUtil.strToList((String)map.get("whsId")));
			List<String> orderIds = logisticsTabDao.selectOrderIdByExportCondition(map);
			List<Map> LogisticsTabExports = null;
			if (orderIds.size()>0) {
				LogisticsTabExports = logisticsTabDao.exportList(orderIds);
			}
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespObjList("ec/logisticsTab/exportList", true, "", null,
					LogisticsTabExports);
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
		return resp;
	}

	//批量修改快递公司编码
	@Override
	public String batchUpdate(String accNum, String ordrNum, String expressEncd) {
		// TODO Auto-generated method stub
		String resp ="";
		try {
			List<String> ordrNums = Arrays.asList(ordrNum.split(","));
			logisticsTabDao.updateExpress(ordrNums, expressEncd);
			platOrderDao.updateExpress(ordrNums, expressEncd);
			resp = BaseJson.returnRespObj("ec/logisticsTab/batchUpdate", true, "批量修改成功", null);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				resp = BaseJson.returnRespObj("ec/logisticsTab/batchUpdate", true, "批量修改异常，请重试", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	//修改快递单号
	@Override
	public String updateExpressCode(String ordrNum, String expressCode) {
		// TODO Auto-generated method stub
		String resp ="";
		//String message = "";
		int fallCount =0;
		int successCount=0;
		String[] ordrNums = ordrNum.split(",");
		String[] expressCodes = expressCode.split(",");
		for (int i = 0; i < ordrNums.length; i++) {
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(ordrNums[i]));
			if (logisticsTab == null) {
				//message = "物流单不存在";
				fallCount++;
			} else {
				if (logisticsTab.getIsShip() == 1) {
					//message = "物流表已发货，不能修改快递单号";
					fallCount++;
				} else {
					logisticsTabDao.updateExpressCode(ordrNums[i], expressCodes[i]);
					//message = "修改成功";
					successCount++;
				}
			} 
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/updateExpressCode", true, "修改快递单号完成，本次成功修改"+successCount+"条，失败"+fallCount+"条", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
	//修改拣货状态
	@Override
	public String updatePick(String accNum, String ordrNums,String type) {
		// TODO Auto-generated method stub
		String resp ="";
		//String message = "";
		int fallCount =0;
		int successCount=0;
		String[] ordrNumsss = ordrNums.split(",");
		MisUser misUser = misUserDao.select(accNum);
		if(type.equals("1")) {
			//更新为拣货完成状态
			for (int i = 0; i < ordrNumsss.length; i++) {
				LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(ordrNumsss[i]));
				if (logisticsTab==null){
					
				} else{
					int a = logisticsTabDao.updatePickOK(Integer.parseInt(ordrNumsss[i]));
					if (a > 0) {
						logisticsTabDao.updateSellSnglPickOK(logisticsTab.getSaleEncd());
						successCount++;
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("开始拣货，标记成功");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(23);// 
						logRecord.setTypeName("开始拣货");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} else {
						fallCount++;
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("开始拣货，标记失败");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(23);//
						logRecord.setTypeName("开始拣货");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} 
				}
			
			}
			
		}else {
			//更新为未拣货状态
			for (int i = 0; i < ordrNumsss.length; i++) {
				LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(ordrNumsss[i]));
				if (logisticsTab==null){
					
				} else{
					int a = logisticsTabDao.updatePickOff(Integer.parseInt(ordrNumsss[i]));
					if (a > 0) {
						logisticsTabDao.updateSellSnglPickOff(logisticsTab.getSaleEncd());
						successCount++;
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("放弃拣货，标记撤回成功");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(24);// 
						logRecord.setTypeName("放弃拣货");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} else {
						fallCount++;
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("放弃拣货，标记撤回失败");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(24);//
						logRecord.setTypeName("放弃拣货");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} 
				}
			
			}
		}
		
		
		try {
			if (type.equals("1")) {
				resp = BaseJson.returnRespObj("ec/logisticsTab/updatePick", true,
						"标记完成，本次成功标记" + successCount + "条，失败" + fallCount + "条", null);
			}else {
				resp = BaseJson.returnRespObj("ec/logisticsTab/updatePick", true, 
						"取消标记完成，本次成功取消标记"+successCount+"条，失败"+fallCount+"条", null);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	//导入快递单号
	@Override
	public String importExpressCode(MultipartFile file, String userId) {
		int j=0;
		String resp ="";
		String message = "";
		int fallCount =0;
		int successCount=0;
		try {
			// TODO Auto-generated method stub
			InputStream fileIn = file.getInputStream();
			//根据指定的文件输入流导入Excel从而产生Workbook对象
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//获取Excel文档中的第一个表单
			Sheet sht0 = wb0.getSheetAt(0);
			//获得当前sheet的开始行
			int firstRowNum = sht0.getFirstRowNum();
			//获取文件的最后一行
			int lastRowNum = sht0.getLastRowNum();
			//设置中文字段和下标映射
			SetColIndex(sht0.getRow(firstRowNum));
			//对Sheet中的每一行进行迭代
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				//如果当前行的行号（从0开始）未达到2（第三行）则从新循环
				if(r.getRowNum()<1){
				continue;
				}
				
				LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(GetCellData(r,"物流号")));
				if (logisticsTab == null) {
					//message = "物流单不存在";
					fallCount++;
				} else {
					if (logisticsTab.getIsShip() == 1) {
						//message = "物流表已发货，不能修改快递单号";
						fallCount++;
					} else {
						logisticsTabDao.updateExpressCode(GetCellData(r,"物流号"), GetCellData(r,"快递单号"));
						//message = "修改成功";
						successCount++;
					}
				} 
			} 
		} catch (Exception e) {
			// TODO: handle exception
			message="导入快递单号异常，请检查。导入成功"+successCount+"条，失败"+fallCount+"条";
		}
		if(message.length()==0) {
			message="导入快递单号完成，导入成功"+successCount+"条，失败"+fallCount+"条";
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/updateExpressCode", true, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * 考拉获取快递公司编码 测试
	 * @param storeSettings
	 * @param companyName
	 */
	public void loadExpressCode() {
		try {
		StoreSettings storeSettings = storeSettingsDao.select("0042");
		String jdRespStr = ECHelper.getKaola("kaola.logistics.companies.get", storeSettings, "");
		System.out.println("返回信息："+jdRespStr);
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//京东快递下单
	@Override
	public String achieveJDWLOrder(String logisticsNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		int successcount = 0;
		int fallcount = 0;
		String[] nums = logisticsNum.split(",");
		String message = "";
		List<String> list = new ArrayList<>();
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以获取电子面单
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsShip()==1) {
				//已发货不能取电子面单
				fallcount++;
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("物流表已发货，不能取电子面单");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(StringUtils.isNotEmpty(logisticsTab.getExpressCode())) {
				//已有快递单号不能取电子面单
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("物流表已有快递单号，不能重复取电子面单");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			// 获取快递公司档案信息
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			if(expressCorp==null) {
				//快递公司不存在
				fallcount++;
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("对应快递公司档案不存在");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(!expressCorp.getCompanyCode().equals("JDWL")||StringUtils.isEmpty(expressCorp.getCompanyCode())) {
				//快递公司不是京东快递
				fallcount++;
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("对应快递公司非京东快递");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			String orderId = logisticsTab.getOrderId();
			// 根据物流表的电商订单号获取电商订单信息
			PlatOrder platOrder = platOrderDao.select(orderId);
			// 根据订单的店铺ID获取店铺档案
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// 将快递公司标准代码作为平台编码查询对应平台的电子面单店铺编号
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(expressCorp.getCompanyCode());
			// 获取对应平台所开通电子面单的店铺设置
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				//storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：请设置当前快递公司："+expressCorp.getCompanyCode()+"对应的店铺设置");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			
			if (expressCorp.getCompanyCode().equals("JDWL")) {
				try {
				//京东快递
				
				// 调用京东快递接口获取运单号
				//目前一次获取一个快递单号
				String respJson = loadJdwlExpressCode(storeSettings, 1);
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(respJson);
				if(jdRespJson.has("error_response")) {
					//存在异常时
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单失败："+jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
					/*{
					"jingdong_etms_waybillcode_get_responce": {
					"code": "0",
					"resultInfo": {
					"message": "成功",
					"code": 100,
					"deliveryIdList": [
					"JDVA01172582655",
					"JDVA01172582664"
					]
					}
					}
					}*/
				
				
				if (jdRespJson.get("jingdong_etms_waybillcode_get_responce").get("resultInfo").get("code").asText().equals("100")) {
					logisticsTab.setExpressCode(jdRespJson.get("jingdong_etms_waybillcode_get_responce").get("resultInfo").get("deliveryIdList").get(0).asText());
				}else {
					//取京东快递单号失败
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单失败："+jdRespJson.get("jingdong_etms_waybillcode_get_responce").get("resultInfo").get("message").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//判断收货地址是否满足京东快递配送区域
				respJson = checkIsJdwl(storeSettings, logisticsTab, platOrder, 1,expressCorp,storeRecord);//配送业务类型选1 普通
				jdRespJson = JacksonUtil.getObjectNode(respJson);
				if(jdRespJson.has("error_response")) {
					//存在异常时
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单失败："+jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				if(jdRespJson.get("jingdong_etms_range_check_responce").get("resultInfo").get("rcode").asText().equals("100")) {
					//"isHideName": 0, 	是否隐藏姓名
					//"agingName": "无时效",  	时效名称
					//"targetSortCenterId": 364605, 目的分拣中心编码
					//"promiseTimeTypeDownGrade": true, 时效产品是否降级
					//"sourcetSortCenterName": "华东外单分拣中心", 始发分拣中心名称
					//"rmessage": "可以京配",  	结果描述
					//"rcode": 100,  	结果编码
					//"promiseTimeType": 1,  产品类型 1-特惠送，2-特快送，4-特瞬送城际，5-同城当日达，6-特快次晨，7-微小件，8-生鲜专送，16-生鲜速达，17-生鲜惠达，21-特惠小包
					//"sourcetSortCenterId": 151678,   	始发分拣中心编码
					//"road": "0",  	路区
					//"originalTabletrolleyCode": "110-10",   始发笼车号
					//"transType": 0,   运输类型（0：陆运，1：航空，）
					//"destinationCrossCode": "7",   目的笼车号
					//"siteId": 289191,  目的站点编码
					//"originalCrossCode": "30", 	始发道口号
					//"destinationTabletrolleyCode": "M19",   	目的笼车号
					//"isHideContractNumbers": 1,   	是否隐藏联系方式
					//"targetSortCenterName": "北京通州分拣中心",   	目的分拣中心名称
					//"orderId": "测试京东快递0001",    	订单号
					//"siteName": "*北京团结湖营业部",    		目的站点名称
					//"aging": 0  		时效
						/*
						 * private String bigShotCode;//大头笔编码     sourcetSortCenterName
						 *  private String bigShotName;//大头笔名称   originalCrossCode-originalTabletrolleyCode
						 *  privateString gatherCenterName;//集包地名称 targetSortCenterName
						 *  private String gatherCenterCode;//集包地代码 destinationCrossCode-destinationTabletrolleyCode
						 * private String branchName;//目的地网点名称  siteName
						 *  private String branchCode;//目的地网点编码 road
						 * private String secondSectionCode;//二段码 agingName
						 * private String thirdSectionCode;//三段码 orderId
						 */	
						ObjectNode jdRespJson1 = JacksonUtil.getObjectNode(jdRespJson.get("jingdong_etms_range_check_responce").get("resultInfo"));
						logisticsTab.setBigShotCode(jdRespJson1.get("sourcetSortCenterName").asText());
						logisticsTab.setBigShotName(jdRespJson1.get("originalCrossCode").asText()+"-"+jdRespJson1.get("originalTabletrolleyCode").asText());
						logisticsTab.setGatherCenterName(jdRespJson1.get("targetSortCenterName").asText());
						logisticsTab.setGatherCenterCode(jdRespJson1.get("destinationCrossCode").asText()+"-"+jdRespJson1.get("destinationTabletrolleyCode").asText());
						logisticsTab.setBranchName(jdRespJson1.get("siteName").asText());
						logisticsTab.setBranchCode(jdRespJson1.get("road").asText());
						logisticsTab.setSecondSectionCode(jdRespJson1.get("agingName").asText());
						logisticsTab.setThirdSectionCode(jdRespJson1.get("orderId").asText());
					}else {
					//存在异常时
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单失败："+jdRespJson.get("jingdong_etms_range_check_responce").get("resultInfo").get("rmessage").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//京东快递青龙接单 只有判断是否京配的结果=100 可以京配才能调此接口
				respJson = JdwlOrder(storeSettings, logisticsTab, platOrder, 1, storeRecord, expressCorp);
				jdRespJson = JacksonUtil.getObjectNode(respJson);
				if(jdRespJson.has("error_response")) {
					//存在异常时
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单失败："+jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//System.out.println(jdRespJson);
				if(jdRespJson.get("jingdong_etms_waybill_send_responce").get("resultInfo").get("code").asText().equals("100")) {
					//下单成功
					logisticsTabDao.update(logisticsTab);
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单成功，快递单号："+logisticsTab.getExpressCode());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					successcount++;
				}else {
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("下单失败，"+jdRespJson.get("jingdong_etms_waybill_send_response").get("resultInfo").get("message"));
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14取电子面单
					logRecord.setTypeName("取电子面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					successcount++;
				}
				

				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				fallcount++;
			}

			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：当前快递公司不支持使用此功能");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14取电子面单
				logRecord.setTypeName("取电子面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/achieveJDWLOrder", true,
					"获取快递单号成功，本次成功获取" + successcount + "条，失败" + fallcount + "条", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
		
		
	}
	/**
	 * 取消快递单号 限直营型快递公司
	 * @param ordrNum 逗号分隔的物流单主键
	 * @param accNum  用户账号
	 * @return
	 */
	@Override
	public String cancelExpressOrder(String ordrNum, String accNum) {
		// TODO Auto-generated method stub
		String resp = "";
		Integer successcount = 0;
		Integer fallcount = 0;
		//Map<String, Integer> map = new HashMap<String, Integer>();
		//map.put("successcount", successcount);
		//map.put("fallcount", fallcount);
		String[] nums = ordrNum.split(",");
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			// 根据物流表id获取物流表信息
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// 如果此物流表不是自发货的不可以进行操作
				fallcount++;
				continue;
			}
			// 根据物流表的电商订单号获取电商订单信息
			PlatOrder platOrder = platOrderDao.selectByEcOrderId(logisticsTab.getEcOrderId());
			// 获取快递公司档案信息
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			//直营型快递公司
			switch (expressCorp.getCompanyCode()) {
			case "JDWL":
				//京东快递
				
				if(cancelJDWLOrder(platOrder, misUser, logisticsTab, expressCorp)) {
					successcount++;//成功
				}else {
					fallcount++;//失败
				}
				break;

			default:
				fallcount++;
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：当前快递公司暂不支持使用此功能");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消面单
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				break;
			}

		}
		
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/cancelExpressOrder", true,
					"获取快递单成功，本次成功取消" + successcount + "条，失败" + fallcount + "条", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * 京东快递取消拦截运单
	 * @param platOrder 电商订单
	 * @param misUser 用户
	 * @param logisticsTab  物流表
	 * @param expressCorp  快递公司
	 * @return true 成功  false 失败
	 */
	public boolean cancelJDWLOrder(PlatOrder platOrder,MisUser misUser,LogisticsTab logisticsTab,ExpressCorp expressCorp) {
		
		
		// 根据订单的店铺ID获取店铺档案
		//StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
		//EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
		// 获取对应平台所开通电子面单的店铺设置
		//StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
		//StoreSettings storeSettings = new StoreSettings();
		
		if(StringUtils.isEmpty(logisticsTab.getExpressCode())) {
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("失败：该物流单尚未分配快递单号，不能执行取消操作");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消面单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			return false;
		}else if (logisticsTab.getIsShip()==1) {
			// 日志记录
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("失败：该物流单已发货，请先执行取消发货操作");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17取消面单
			logRecord.setTypeName("取消面单");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			return false;
		}else {
			// 将快递公司标准代码作为平台编码查询对应平台的电子面单店铺编号
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(expressCorp.getCompanyCode());
			// 获取对应平台所开通电子面单的店铺设置
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				//storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getUserName());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("失败：请设置当前快递公司："+expressCorp.getCompanyCode()+"对应的店铺设置");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				return false;
			}
			//调用京东快递取消接口
			
			try {
				String resp = cancelJdwl(storeSettings, logisticsTab, "订单取消");
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(resp);
				if (jdRespJson.has("error_response")) {
					//存在异常时
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(misUser.getUserName());

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("取消失败：" + jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(17);// 17取消
					logRecord.setTypeName("取消面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					return false;
				}
				if(jdRespJson.get("jingdong_ldop_receive_order_intercept_responce").get("resultInfo").get("stateCode").asText().equals("100")) {
					//取消成功
					logisticsTab.setExpressCode("");//清空对应快递单号
					logisticsTab.setBigShotCode(null);
					logisticsTab.setBigShotName(null);
					logisticsTab.setGatherCenterName(null);
					logisticsTab.setGatherCenterCode(null);
					logisticsTab.setBranchName(null);
					logisticsTab.setBranchCode(null);
					logisticsTab.setSecondSectionCode(null);
					logisticsTab.setThirdSectionCode(null);
					logisticsTabDao.update(logisticsTab);
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(misUser.getUserName());

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("取消成功：原快递单号："+logisticsTab.getExpressCode());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(17);// 17取消
					logRecord.setTypeName("取消面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					return true;
				}else {
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(misUser.getUserName());

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("取消失败："+jdRespJson.get("jingdong_ldop_receive_order_intercept_responce").get("resultInfo").get("stateMessage").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(17);// 17取消
					logRecord.setTypeName("取消面单");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					return false;
				}
				
			
			} catch (Exception e) {
				// TODO: handle exception
				//存在异常时
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getUserName());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("取消失败：调用京东快递接口异常，请联系管理员");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17取消
				logRecord.setTypeName("取消面单");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				return false;
			}
		}
		
	}

}
class XMYPLogisticsTabBean {

	private int code;
	private String message;
	private Result result;
	static  class Result {
	public 	Result(){

		}
		private String delivery_id;
		private String oid;
		private String uid;
		private String stime;
		private String atime;
		private String express_sn;
		private String bizcode;
		private String express_name;
		private String status;
		private String descr;
		private String otime;
		private String ttl;
		private String donkey_id;
		public void setDelivery_id(String delivery_id) {
			this.delivery_id = delivery_id;
		}
		public String getDelivery_id() {
			return delivery_id;
		}

		public void setOid(String oid) {
			this.oid = oid;
		}
		public String getOid() {
			return oid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getUid() {
			return uid;
		}

		public void setStime(String stime) {
			this.stime = stime;
		}
		public String getStime() {
			return stime;
		}

		public void setAtime(String atime) {
			this.atime = atime;
		}
		public String getAtime() {
			return atime;
		}

		public void setExpress_sn(String express_sn) {
			this.express_sn = express_sn;
		}
		public String getExpress_sn() {
			return express_sn;
		}

		public void setBizcode(String bizcode) {
			this.bizcode = bizcode;
		}
		public String getBizcode() {
			return bizcode;
		}

		public void setExpress_name(String express_name) {
			this.express_name = express_name;
		}
		public String getExpress_name() {
			return express_name;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		public String getStatus() {
			return status;
		}

		public void setDescr(String descr) {
			this.descr = descr;
		}
		public String getDescr() {
			return descr;
		}

		public void setOtime(String otime) {
			this.otime = otime;
		}
		public String getOtime() {
			return otime;
		}

		public void setTtl(String ttl) {
			this.ttl = ttl;
		}
		public String getTtl() {
			return ttl;
		}

		public void setDonkey_id(String donkey_id) {
			this.donkey_id = donkey_id;
		}
		public String getDonkey_id() {
			return donkey_id;
		}

	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	public Result getResult() {
		return result;
	}

}