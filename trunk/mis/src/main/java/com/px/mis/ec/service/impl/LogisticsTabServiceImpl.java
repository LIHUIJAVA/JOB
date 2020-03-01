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
			resp = BaseJson.returnRespObj("ec/logisticsTab/insert", true, "�����ɹ���", null);
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
			message = "���" + logisticsTab.getOrdrNum() + "�����ڣ�����ʧ�ܣ�";
		} else {
			logisticsTabDao.update(logisticsTab);
			message = "���³ɹ���";
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
					message = "�������ѷ���������ɾ��";
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(20);// 20������ɾ��
					logRecord.setTypeName("������ɾ��");
					logRecord.setOperatOrder(logisticsTab.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallCount++;
				} else {
					logisticsTabDao.delete(Integer.parseInt(ordrNums[i]));
					message = "ɾ���ɹ�";
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(20);// 20������ɾ��
					logRecord.setTypeName("������ɾ��");
					logRecord.setOperatOrder(logisticsTab.getEcOrderId());
					logRecordDao.insert(logRecord);
					//message = "ɾ���ɹ���";
					successCount++;
				}

			} 
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/delete", true,
					"ɾ���ɹ������γɹ�ɾ��" + successCount + "����ʧ��" + fallCount + "��", null);
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
			message = "���Ϊ" + ordrNum + "�����������ڣ���ѯ����ʧ�ܡ�";
			isSuccess = false;
		} else {
			message = "��ѯ�����ɹ�������";
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
			resp = BaseJson.returnRespList("ec/logisticsTab/selectList", true, "��ҳ��ѯ�ɹ���", count, pageNo, pageSize,
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
			resp = BaseJson.returnRespObj("ec/logisticsTab/insertList", true, "�����ɹ���", null);
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
			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Ի�ȡ�����浥
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsShip()==1) {
				//�ѷ�������ȡ�����浥
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�������ѷ���������ȡ�����浥");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(StringUtils.isNotEmpty(logisticsTab.getExpressCode())) {
				//�����浥�Ų���ȡ�����浥
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�������ѷ��䵥�ţ������ظ�ȡ�����浥");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			String orderId = logisticsTab.getOrderId();
			// ����������ĵ��̶����Ż�ȡ���̶�����Ϣ
			PlatOrder platOrder = platOrderDao.select(orderId);
			// ���ݶ����ĵ���ID��ȡ���̵���
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// ���ݵ��̵�����ȡ����ƽ̨ID
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ������õ�ǰƽ̨��ͨ�����浥�ĵ���");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			// ��ȡ��ݹ�˾������Ϣ
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			// ��ȡƽ̨���õĵ����浥��ݹ�˾��Ϣ
			EcExpress ecExpress=null;
			try {
				ecExpress = ecExpressDao.select(storeRecord.getEcId(), expressCorp.getCompanyCode(),
						expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),expressCorp.getDetailedAddress());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݹ�˾������Ӧ�����浥�˻���Ϣ�ظ�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(ecExpress==null) {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݹ�˾�������ô�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if (storeRecord.getEcId().equals("JD")) {
				// ���þ��������浥�ӿڻ�ȡ�����浥
				String result = JDexpressOrder(storeSettings, ecExpress, platOrder, expressCorp,
						ecExpressSettings.getVenderId());
				// �������ؽ��
				try {
					ObjectNode jdRespJson = JacksonUtil.getObjectNode(result);
					if (jdRespJson.toString().length() != 0) {
						if (jdRespJson.get("statusCode").asText().equals("-1")) {
							// ���ش���ʱ���������ʾ
							//System.out.println("��ȡ�浥������ʾ��" + jdRespJson.get("statusMessage").asText());
							message = jdRespJson.get("statusMessage").asText();
							fallcount++;
						} else {
							JsonNode resultJson = jdRespJson.get("data");
							// ��ȡ�����浥�ɹ�
							ArrayNode codes = (ArrayNode) resultJson.get("waybillCodeList");
							String expressCode = "";
							for (Iterator<JsonNode> itemInfoInfoIterator = codes.iterator(); itemInfoInfoIterator
									.hasNext();) {
								JsonNode itemInfo = itemInfoInfoIterator.next();
								expressCode = itemInfo.asText();
							}
							// �����ȡ���ĵ����浥��
							logisticsTab.setExpressCode(expressCode);
							// ��ȡ��ͷ�ʵ���Ϣ
							String bigShotInfomation = JDBigShot(storeSettings, expressCode, ecExpress.getProviderId(),
									ecExpress.getProviderCode());
							ObjectNode infomation = JacksonUtil.getObjectNode(bigShotInfomation);
							if (infomation.toString().length() != 0) {
								if (!infomation.get("jingdong_ldop_alpha_vendor_bigshot_query_responce").get("code")
										.asText().equals("0")) {
									// ���ش���ʱ���������ʾ
									System.out.println("��ȡ��ͷ�ʴ���+++++++");
									fallcount++;
								} else {
									// �������صĴ�ͷ������
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
										message = "��ݹ�˾��" + ecExpress.getProviderName() + "����ݵ��ţ�"
												+ logisticsTab.getExpressCode();
									} else {
										message = "�ӿڷ�������״ֵ̬��Ϊ0";
										fallcount++;
									}
								}
							} else {
								message = "�ӿڷ�����������ϸ";
								fallcount++;
							}
						}
					} else {
						fallcount++;
						message = "�ӿ������ݷ���";
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message = "�������������쳣";
				}

				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);

			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ǰƽ̨������֧�ִ˲���");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressOrder", true,
					"��ȡ�����浥�ɹ������γɹ���ȡ�浥" + successcount + "����ʧ��" + fallcount + "��", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (list.size() > 0) {
			try {
				resp = BaseJson.returnRespObjList("ec/logisticsTab/achieveECExpressOrder", true, "�ƴ�ӡ��Ϣ", null, list);
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
		// ��������Ϣ
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
//		�����浥��Ϣ��
		List<ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem> tradeOrderInfoDtos = new ArrayList<ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem>();

		ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem item = new ParamWaybillCloudPrintApplyNewRequestTradeOrderInfoDtosItem();
		item.setObjectId("1");// ����id
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
		// �ռ�����Ϣ
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
			asda.put("k", "���ƴ�ӡ��׼ģ��");

			return asda;
		}
		item.setUserId(Long.valueOf(storeSettings.getVenderId()));
//        item.setLogisticsServices("str");//����������������

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
			// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(misUser.getAccNum());

						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("ʧ�ܣ�"+response.getErrorResponse().getErrorMsg());
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(14);// 14ȡ�����浥
						logRecord.setTypeName("ȡ�����浥");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
			return asda;
		}

		String string = "";
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		asda.put("list", jsonObjects);
//		asda.put("TM", jsonObjects);
		for (InnerPddWaybillGetResponseModulesItem wcpr : response.getPddWaybillGetResponse().getModules()) {

			logisticsTab.setExpressCode(wcpr.getWaybillCode());// ���ص��浥��
			string = wcpr.getPrintData();
			JSONObject dsfadsf = JSON.parseObject(string);
			jsonObjects.add(dsfadsf);
			jsonObjects.add(jsonObjectdate);
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("��ݹ�˾��"+expressCorp.getExpressNm()+"����ݵ��ţ�"+wcpr.getWaybillCode());
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(14);// 14ȡ�����浥
			logRecord.setTypeName("ȡ�����浥");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			
			logisticsTabDao.update(logisticsTab);
		}

		return asda;
	}

	// ���������浥��ȡ ���ؾ�����������json
	public String JDexpressOrder(StoreSettings storeSettings, EcExpress ecExpress, PlatOrder platOrder,
			ExpressCorp expressCorp, String venderId) {
		String message = "";
		try {
			DecimalFormat dFormat = new DecimalFormat("0.00");
			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("waybillType", 1);// �˵�����1��ͨ�˵�
			objectNode.put("waybillCount", 1);// �˵�����
			objectNode.put("providerId", ecExpress.getProviderId());// ������id
			objectNode.put("branchCode", ecExpress.getBranchCode());// �����������
			objectNode.put("salePlatform", "0010001");// ����ƽ̨0010001������ƽ̨
			objectNode.put("platformOrderNo", platOrder.getEcOrderId());// ƽ̨������
			objectNode.put("vendorCode", venderId);// �̼�ID
			objectNode.put("vendorName", storeSettings.getStoreName());// �̼�����
			objectNode.put("vendorOrderCode", platOrder.getOrderId());// �̼����ж�����
			objectNode.put("weight", 0);// ��������λǧ�ˣ���λС���� û����0
			objectNode.put("volume", 0);// �������λΪͳһΪ�������� ��λС�� û����0
			objectNode.put("promiseTimeType", 0);// ��ŵʱЧ���ͣ���ʱЧĬ�ϴ�0
			objectNode.put("payType", 0);// ���ʽ0-����֧����Ŀǰ��ʱ��֧�ֻ�������ҵ��
			objectNode.put("goodsMoney", dFormat.format(platOrder.getOrderSellerPrice()));// ��Ʒ��� ��λС��
			objectNode.put("shouldPayMoney", 0.00);// ���ս�� ��λС��
			objectNode.put("needGuarantee", false);// �Ƿ�Ҫ���ۣ�ϵͳ�ݲ����ű���ҵ��
			objectNode.put("guaranteeMoney", 0.00);// ���۽�� ��λС��,�Ǳ���Ĭ�ϴ�0.0
			objectNode.put("receiveTimeType", 0);// �ջ�ʱ�����ͣ�0�κ�ʱ�䣬1������2�ڼ���
			objectNode.put("needGuarantee", false);// �Ƿ�Ҫ���ۣ�ϵͳ�ݲ����ű���ҵ��
			objectNode.put("needGuarantee", false);// �Ƿ�Ҫ���ۣ�ϵͳ�ݲ����ű���ҵ��
			// �ջ���ַ
			ObjectNode toaddress = JacksonUtil.getObjectNode("");
			toaddress.put("provinceName", platOrder.getProvince());// �ջ���ַʡ
			toaddress.put("provinceId", platOrder.getProvinceId());// �ջ���ַʡID
			toaddress.put("cityName", platOrder.getCity());// �ջ���ַ��
			toaddress.put("cityId", platOrder.getCityId());// �ջ���ַ��ID
			toaddress.put("countryName", platOrder.getCounty());// �ջ���ַ��
			toaddress.put("countryId", platOrder.getCountyId());// �ջ���ַ��ID
			toaddress.put("address", platOrder.getRecAddress());// �ջ�ȫ��ַ
			toaddress.put("contact", platOrder.getRecName());// �ջ�������
			toaddress.put("phone", platOrder.getRecMobile());// �ջ����ֻ�
			toaddress.put("mobile", platOrder.getRecMobile());// ����
			objectNode.put("toAddress", toaddress);
			// ������ַ
			ObjectNode fromaddress = JacksonUtil.getObjectNode("");
			fromaddress.put("provinceName", ecExpress.getProvinceName());// ������ַʡ
			fromaddress.put("provinceId", ecExpress.getProvinceId());// ������ַʡID
			fromaddress.put("cityName", ecExpress.getCityName());// ������ַ��
			fromaddress.put("cityId", ecExpress.getCityId());// ������ַ��ID
			fromaddress.put("countryName", ecExpress.getCountryName());// ������ַ��
			fromaddress.put("countryId", ecExpress.getCountryId());// ������ַ��ID
			fromaddress.put("address", ecExpress.getAddress());// ������ϸ��ַ
			fromaddress.put("contact", expressCorp.getDeliver());// ����������
			fromaddress.put("phone", expressCorp.getDeliverPhone());// �������ֻ�
			fromaddress.put("mobile", expressCorp.getDeliverMobile());// ����
			objectNode.put("fromAddress", fromaddress);
			ObjectNode jsObjectNode = JacksonUtil.getObjectNode("");
			jsObjectNode.put("content", objectNode);
			// message=objectNode.toString();
			//System.err.println("�������ݣ�" + objectNode.toString());
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
	
	
	// ��������µ�
		public String JDWLexpress(StoreSettings storeSettings, EcExpress ecExpress, PlatOrder platOrder,
				ExpressCorp expressCorp, String venderId) {
			String message = "";
			try {
				DecimalFormat dFormat = new DecimalFormat("0.00");
				ObjectNode objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("waybillType", 1);// �˵�����1��ͨ�˵�
				objectNode.put("waybillCount", 1);// �˵�����
				objectNode.put("providerId", ecExpress.getProviderId());// ������id
				objectNode.put("branchCode", ecExpress.getBranchCode());// �����������
				objectNode.put("salePlatform", "0010001");// ����ƽ̨0010001������ƽ̨
				objectNode.put("platformOrderNo", platOrder.getEcOrderId());// ƽ̨������
				objectNode.put("vendorCode", venderId);// �̼�ID
				objectNode.put("vendorName", storeSettings.getStoreName());// �̼�����
				objectNode.put("vendorOrderCode", platOrder.getOrderId());// �̼����ж�����
				objectNode.put("weight", 0);// ��������λǧ�ˣ���λС���� û����0
				objectNode.put("volume", 0);// �������λΪͳһΪ�������� ��λС�� û����0
				objectNode.put("promiseTimeType", 0);// ��ŵʱЧ���ͣ���ʱЧĬ�ϴ�0
				objectNode.put("payType", 0);// ���ʽ0-����֧����Ŀǰ��ʱ��֧�ֻ�������ҵ��
				objectNode.put("goodsMoney", dFormat.format(platOrder.getOrderSellerPrice()));// ��Ʒ��� ��λС��
				objectNode.put("shouldPayMoney", 0.00);// ���ս�� ��λС��
				objectNode.put("needGuarantee", false);// �Ƿ�Ҫ���ۣ�ϵͳ�ݲ����ű���ҵ��
				objectNode.put("guaranteeMoney", 0.00);// ���۽�� ��λС��,�Ǳ���Ĭ�ϴ�0.0
				objectNode.put("receiveTimeType", 0);// �ջ�ʱ�����ͣ�0�κ�ʱ�䣬1������2�ڼ���
				objectNode.put("needGuarantee", false);// �Ƿ�Ҫ���ۣ�ϵͳ�ݲ����ű���ҵ��
				objectNode.put("needGuarantee", false);// �Ƿ�Ҫ���ۣ�ϵͳ�ݲ����ű���ҵ��
				// �ջ���ַ
				ObjectNode toaddress = JacksonUtil.getObjectNode("");
				toaddress.put("provinceName", platOrder.getProvince());// �ջ���ַʡ
				toaddress.put("provinceId", platOrder.getProvinceId());// �ջ���ַʡID
				toaddress.put("cityName", platOrder.getCity());// �ջ���ַ��
				toaddress.put("cityId", platOrder.getCityId());// �ջ���ַ��ID
				toaddress.put("countryName", platOrder.getCounty());// �ջ���ַ��
				toaddress.put("countryId", platOrder.getCountyId());// �ջ���ַ��ID
				toaddress.put("address", platOrder.getRecAddress());// �ջ�ȫ��ַ
				toaddress.put("contact", platOrder.getRecName());// �ջ�������
				toaddress.put("phone", platOrder.getRecMobile());// �ջ����ֻ�
				toaddress.put("mobile", platOrder.getRecMobile());// ����
				objectNode.put("toAddress", toaddress);
				// ������ַ
				ObjectNode fromaddress = JacksonUtil.getObjectNode("");
				fromaddress.put("provinceName", ecExpress.getProvinceName());// ������ַʡ
				fromaddress.put("provinceId", ecExpress.getProvinceId());// ������ַʡID
				fromaddress.put("cityName", ecExpress.getCityName());// ������ַ��
				fromaddress.put("cityId", ecExpress.getCityId());// ������ַ��ID
				fromaddress.put("countryName", ecExpress.getCountryName());// ������ַ��
				fromaddress.put("countryId", ecExpress.getCountryId());// ������ַ��ID
				fromaddress.put("address", ecExpress.getAddress());// ������ϸ��ַ
				fromaddress.put("contact", expressCorp.getDeliver());// ����������
				fromaddress.put("phone", expressCorp.getDeliverPhone());// �������ֻ�
				fromaddress.put("mobile", expressCorp.getDeliverMobile());// ����
				objectNode.put("fromAddress", fromaddress);
				ObjectNode jsObjectNode = JacksonUtil.getObjectNode("");
				jsObjectNode.put("content", objectNode);
				// message=objectNode.toString();
				//System.err.println("�������ݣ�" + objectNode.toString());
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
		 * ��ȡ��������˵���
		 * @param storeSettings ������ݵ�������
		 * @param count  ��ȡ�˵�����
		 * @return  
		 */
		
		public String loadJdwlExpressCode(StoreSettings storeSettings, int count) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("customerCode", storeSettings.getVenderId());// �̼ұ��� �о��������ṩ 010K345716
				objectNode.put("preNum", count);// ��ȡ�˵������������ֵ100
				objectNode.put("orderType",0);// �˵����͡�(��ͨ�ⵥ��0��O2O �ⵥ��1)Ĭ��Ϊ 0
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
		 * �жϼļ���ַ,�ռ���ַ�Ƿ��ھ��������շ�Χ : jingdong.etms.range.check
		 * @param storeSettings ������ݵ�������
		 * @param logisticsTab ��Ӧ������
		 * @param goodsType ����ҵ�����ͣ� 1:��ͨ��3:��֣�4:���䣬5:�ʻ6:���£�7:��أ�8:�䶳��9:���䣩
		 * @param platOrder ��Ӧ������Ϣ
		 * @return  
		 */
		
		public String checkIsJdwl(StoreSettings storeSettings, LogisticsTab logisticsTab,PlatOrder platOrder, int goodsType,ExpressCorp expressCorp,StoreRecord storeRecord) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("customerCode", storeSettings.getVenderId());// �̼ұ��� �о��������ṩ 010K345716
				//objectNode.put("preNum", count);// ��ȡ�˵������������ֵ100
				objectNode.put("orderId", storeRecord.getStoreName()+logisticsTab.getOrderId());//������
				objectNode.put("goodsType",goodsType);// ����ҵ�����ͣ� 1:��ͨ��3:��֣�4:���䣬5:�ʻ6:���£�7:��أ�8:�䶳��9:���䣩Ĭ����1
				objectNode.put("receiveAddress",platOrder.getProvince()+platOrder.getCity()+platOrder.getCounty());//�ռ��˵�ַ
				objectNode.put("senderAddress",expressCorp.getProvince()+expressCorp.getCity()+expressCorp.getCountry());//������ַ
				
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
		 *  �����ӵ� ֻ���ж��Ƿ���Ľ��=100 ���Ծ�����ܵ��˽ӿ�
		 * @param storeSettings ������ݵ�������
		 * @param logisticsTab ��Ӧ������
		 * @param goodsType ����ҵ�����ͣ� 1:��ͨ��3:��֣�4:���䣬5:�ʻ6:���£�7:��أ�8:�䶳��9:���䣩
		 * @param platOrder ��Ӧ������Ϣ
		 * @return  
		 */
		
		public String JdwlOrder(StoreSettings storeSettings, LogisticsTab logisticsTab,PlatOrder platOrder, int goodsType,StoreRecord storeRecord,ExpressCorp expressCorp) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("customerCode", storeSettings.getVenderId());// �̼ұ��� �о��������ṩ 010K345716
				objectNode.put("deliveryId", logisticsTab.getExpressCode());//�˵���
				/*
				 * 0010001 ������ƽ̨ 0010002 ������è 0010003 �������� 0030001 ��������ƽ̨
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
			 	
				//objectNode.put("preNum", count);// ��ȡ�˵������������ֵ100
				objectNode.put("orderId", storeRecord.getStoreName()+logisticsTab.getOrderId());//���������ţ��õ�������+������ƴ�ӣ������µ׵�������������
				objectNode.put("senderName",expressCorp.getDeliver());//�ļ���
				objectNode.put("senderAddress",expressCorp.getDetailedAddress());//�ļ��˵�ַ
				objectNode.put("senderMobile",expressCorp.getDeliverPhone());//�ļ����ֻ�
				objectNode.put("receiveName",logisticsTab.getRecName());//�ռ���
				objectNode.put("receiveAddress",platOrder.getProvince()+platOrder.getCity()+platOrder.getCounty()+platOrder.getRecAddress());//�ռ��˵�ַ
				objectNode.put("receiveName",logisticsTab.getRecName());//�ռ���
				objectNode.put("receiveMobile",logisticsTab.getRecMobile());// ����ҵ�����ͣ� 1:��ͨ��3:��֣�4:���䣬5:�ʻ6:���£�7:��أ�8:�䶳��9:���䣩Ĭ����1
				objectNode.put("packageCount",1);//������
				if (logisticsTab.getWeight()!=null) {
					//��������������������ʱȡ��������������û������ʱĬ��1
					if (logisticsTab.getWeight().compareTo(BigDecimal.ZERO) > 0) {
						objectNode.put("weight", logisticsTab.getWeight());
					} else {
						objectNode.put("weight", "1.00");
					} 
				} else {
					objectNode.put("weight", "1.00");
				}
				if (logisticsTab.getVolume()!=null) {
					//�������������������ʱȡ��������������û������ʱĬ��1000�������� 
					if (logisticsTab.getVolume().compareTo(BigDecimal.ZERO) > 0) {
						objectNode.put("vloumn", logisticsTab.getWeight());
					} else {
						objectNode.put("vloumn", "1000.00");
					} 
				}else {
					objectNode.put("vloumn", "1000.00");
				}
				objectNode.put("goods","ĸӤ");//
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
		 * 	ȡ���µ�/�����˵� api
		 * 	���ô� api�����δȡ�����,���Ƿ���ȡ��ָ� ����������,���Ƿ�������ָ��
			�첽����,������ѯ,�ж�״̬�� stateCode
			stateCode=100,��ʾȡ���ɹ�
			stateCode=108,��ʾ�˵��Ѿ���ȡ���ɹ�����
		 * 	�ӿ�����jingdong.ldop.receive.order.intercept
		 * @param storeSettings
		 * @param logisticsTab
		 * @param cancelReason ȡ��ԭ��
		 * @return
		 */
		public String cancelJdwl(StoreSettings storeSettings, LogisticsTab logisticsTab,String cancelReason) {
			String result = "";
			ObjectNode objectNode;
			try {
				objectNode = JacksonUtil.getObjectNode("");
				objectNode.put("vendorCode", storeSettings.getVenderId());// �̼ұ��� �о��������ṩ 010K345716
				objectNode.put("deliveryId", logisticsTab.getExpressCode());//�˵���
				objectNode.put("interceptReason",cancelReason);//ȡ��ԭ��
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
	 * ��ȡ�����޽�����浥��ͷ�ʱ�����Ϣ
	 * 
	 * @param storeSettings ��������
	 * @param expressCode   ��ݵ���
	 * @param providerId    ������ID
	 * @param providerCode  �����̱���
	 * @return �ӿڷ��ؽ��
	 */
	public String JDBigShot(StoreSettings storeSettings, String expressCode, int providerId, String providerCode) {
		String result = "";
		ObjectNode objectNode;
		try {
			objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("waybillCode", expressCode);// �˵���
			objectNode.put("providerId", providerId);// ������ID
			objectNode.put("providerCode", providerCode);// �����̱���
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
	
	//������������
	public String JDOrderShip(LogisticsTab logistics, EcExpress express, StoreSettings storeSettings) {
		String result = "";
		try {
			ObjectNode objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("orderId", logistics.getEcOrderId());// ƽ̨������
			objectNode.put("logiCoprId", express.getProviderId());// ��ݹ�˾id
			objectNode.put("logiNo", logistics.getExpressCode());// ��ݵ���
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

			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Խ��з�������
				fallcount++;
				continue;
			}
			if(StringUtils.isEmpty(logisticsTab.getExpressCode())) {
				//����������ݵ���Ϊ��ʱ��������
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݵ���Ϊ�գ�����ʧ��");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsPick()==0) {
				//���������״̬Ϊδ�����������ִ�з���
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ�������δ��ʼ���������ִ�м�����");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsShip()==1) {
				//����������ѷ����������ظ�����
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ��������ѷ������������ظ�����");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			// ����������Ķ����Ż�ȡ���̶�����Ϣ
			PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
			// ���ݶ����ĵ���ID��ȡ���̵���
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// ���ݵ��̵�����ȡ����ƽ̨ID
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// ��ȡ��Ӧ�����ĵ�������
			StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
			// ��ȡ��ݹ�˾������Ϣ
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			// ��ȡƽ̨���õĵ����浥��ݹ�˾��Ϣ
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
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("ʧ�ܣ���ݹ�˾������Ӧ�����浥�˻���Ϣ�ظ�������");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8����
					logRecord.setTypeName("����");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				if (ecExpress == null) {
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("ʧ�ܣ���ݹ�˾�������ô�������");
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8����
					logRecord.setTypeName("����");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				} 
			}
			if (storeRecord.getEcId().equals("JD")) {
				 storeSettings = storeSettingsDao.select(platOrder.getStoreId());
				// ����ƽ̨�Ķ���
				try {
					String result = JDOrderShip(logisticsTab, ecExpress, storeSettings);
					//System.err.println(result);
					if (result.equals("")) {
						message = "�ӿ�û�з�����Ϣ";
						// ����Ϊ��ʱ��˵�����ö��������ӿ�ʱû�з�����Ϣ
						fallcount++;
					} else {
						// System.err.println("���þ����ӿڷ����󷵻�json��" + result);
						if (JacksonUtil.getObjectNode(result).has("error_response")) {
							// �ӿڷ�����Ϣ����error_response
							message = JacksonUtil.getObjectNode(result).get("error_response").asText();
							fallcount++;
						} else {
							JsonNode node = JacksonUtil.getObjectNode(result).get("jingdong_pop_order_shipment_responce").get("sopjosshipment_result");
							//System.out.println(node.get("success").asBoolean());
							if (node.get("success").asBoolean()) {
								// �����ɹ�
								logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
								logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
								logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
								logisticsTabDao.update(logisticsTab);// ������������Ϣ

								platOrder.setIsShip(1);
								platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
								platOrder.setShipTime(logisticsTab.getShipDate());
								platOrderDao.update(platOrder);

								message = "�����ش�ƽ̨�ɹ�";
								successcount++;
							} else {
								// ����ʧ��
								message = node.get("chineseErrCode").asText();
								fallcount++;
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message = "�����ش�ƽ̨�쳣";
				}

				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);

			} else if (storeRecord.getEcId().equals("TM")) {
				try {
				//System.err.println(platOrder.getEcOrderId());
				//System.err.println(logisticsTab.getExpressCode());
			    storeSettings = storeSettingsDao.select(platOrder.getStoreId());
				// ��è���Ա���
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
				// ת��
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("path", LogisticsOnlineSendRequest.class.getName());
				maps.put("taobaoObject", JSON.toJSONString(req));
				String taobao = null;
				
				 
					taobao = ECHelper.getTB("", storeSettings, maps);
				

				LogisticsOnlineSendResponse rsp = JSONObject.parseObject(taobao, LogisticsOnlineSendResponse.class);
				if (!rsp.isSuccess()) {
					fallcount++;
					// ����ʧ��
					message = rsp.getSubMsg();
				}else {
					if (rsp.getShipping().getIsSuccess()) {
						// �����ɹ�
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						
						

						platOrder.setIsShip(1);
						platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
						platOrder.setShipTime(logisticsTab.getShipDate());
						platOrderDao.update(platOrder);
						successcount++;
						message = "�����ش�ƽ̨�ɹ�";

					} else {
						fallcount++;
						// ����ʧ��
						message = "�����ش�ƽ̨ʧ��";
						
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
					logRecord.setOperatType(8);// 8����
					logRecord.setTypeName("����");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					message = "�����ش�ƽ̨�쳣";

				}
				// taobao.logistics.online.send( ���߶�����������֧�ֻ������ )
				// taobao.logistics.online.confirm( ȷ�Ϸ���֪ͨ�ӿ� )�������
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
			} else if (storeRecord.getEcId().equals("PDD")) {
				PddLogisticsOnlineSendRequest request = new PddLogisticsOnlineSendRequest();

				//System.err.println(platOrder.getEcOrderId());
				//System.err.println(logisticsTab.getExpressCode());
//				if("9999".equals(ecExpressSettings.getStoreId())){
////				{"logistics_companies_get_response":{"logistics_companies":[{"code":"STO","available":1,"logistics_company":"��ͨ���","id":1},{"code":"SHHT","available":0,"logistics_company":"�Ϻ���ͨ","id":2},{"code":"HT","available":1,"logistics_company":"�������","id":3},{"code":"SF","available":1,"logistics_company":"˳����","id":44},{"code":"YTO","available":1,"logistics_company":"Բͨ���","id":85},{"code":"","available":0,"logistics_company":"�ڲ�Ա������","id":86},{"code":"BBSD","available":0,"logistics_company":"�����ٴ�","id":88},{"code":"SAD","available":0,"logistics_company":"���ĵ�","id":89},{"code":"CHENGBANG","available":0,"logistics_company":"�ɰ�����","id":90},{"code":"ZTO","available":1,"logistics_company":"��ͨ���","id":115},{"code":"QF","available":0,"logistics_company":"ȫ����","id":116},{"code":"YS","available":1,"logistics_company":"���ٿ��","id":117},{"code":"EMS","available":1,"logistics_company":"����EMS","id":118},{"code":"TT","available":1,"logistics_company":"������","id":119},{"code":"JD","available":1,"logistics_company":"��������","id":120},{"code":"YUNDA","available":1,"logistics_company":"�ϴ���","id":121},{"code":"KJ","available":0,"logistics_company":"��ݿ��","id":122},{"code":"GTO","available":1,"logistics_company":"��ͨ���","id":124},{"code":"DDCBPS","available":0,"logistics_company":"������������","id":128},{"code":"ZJS","available":1,"logistics_company":"լ���Ϳ��","id":129},{"code":"RFD","available":1,"logistics_company":"����","id":130},{"code":"DB","available":1,"logistics_company":"�°���","id":131},{"code":"YZXB","available":1,"logistics_company":"������ݰ���","id":132},{"code":"LBEX","available":1,"logistics_company":"������","id":133},{"code":"FEDEX","available":1,"logistics_company":"������","id":135},{"code":"JIUYE","available":1,"logistics_company":"��ҷ��Ӧ��","id":136},{"code":"","available":0,"logistics_company":"�ٳǵ��մ���","id":137},{"code":"","available":0,"logistics_company":"�����","id":138},{"code":"","available":0,"logistics_company":"����������","id":139},{"code":"NJCHENGBANG","available":0,"logistics_company":"�Ͼ��ɰ�","id":140},{"code":"SXHONGMAJIA","available":0,"logistics_company":"ɽ�������","id":141},{"code":"WXWL","available":0,"logistics_company":"��������","id":142},{"code":"LIJISONG","available":0,"logistics_company":"������","id":143},{"code":"MENDUIMEN","available":0,"logistics_company":"�Ŷ���","id":144},{"code":"SAD2","available":0,"logistics_company":"���ĵ�","id":145},{"code":"","available":0,"logistics_company":"���","id":147},{"code":"ADX","available":1,"logistics_company":"������","id":148},{"code":"HWKD","available":0,"logistics_company":"������","id":149},{"code":"GZLT","available":0,"logistics_company":"��Զ����","id":150},{"code":"","available":0,"logistics_company":"�϶����","id":151},{"code":"HUIWEN","available":0,"logistics_company":"���Ŀ��","id":152},{"code":"","available":0,"logistics_company":"�϶����","id":153},{"code":"HUANGMAJIA","available":0,"logistics_company":"�����","id":154},{"code":"SURE","available":1,"logistics_company":"�ٶ����","id":155},{"code":"YAMAXUNWULIU","available":1,"logistics_company":"����ѷ����","id":156},{"code":"YCT","available":1,"logistics_company":"��èլ����","id":157},{"code":"","available":0,"logistics_company":"˳�ẽ��","id":158},{"code":"","available":0,"logistics_company":"Բͨ����","id":159},{"code":"","available":0,"logistics_company":"ƴ�û�","id":160},{"code":"SHSAD","available":0,"logistics_company":"�Ϻ����ĵ�","id":161},{"code":"BJCS","available":0,"logistics_company":"����100","id":162},{"code":"ZMKM","available":0,"logistics_company":"֥�鿪��","id":163},{"code":"SHUNJIEFENGDA","available":0,"logistics_company":"˳�ݷ��","id":164},{"code":"HTXMJ","available":1,"logistics_company":"��ͨС����","id":165},{"code":"","available":0,"logistics_company":"����С����","id":166},{"code":"LNHUANGMAJIA","available":0,"logistics_company":"���������","id":167},{"code":"","available":0,"logistics_company":"�������ĵ�","id":168},{"code":"","available":0,"logistics_company":"������","id":169},{"code":"","available":0,"logistics_company":"ͨ�ͼѵ�","id":170},{"code":"SUJIEVIP","available":0,"logistics_company":"�ٽ�","id":171},{"code":"","available":0,"logistics_company":"��ŵѸ��","id":172},{"code":"","available":0,"logistics_company":"������","id":173},{"code":"","available":0,"logistics_company":"����","id":174},{"code":"","available":0,"logistics_company":"����;��","id":175},{"code":"","available":0,"logistics_company":"С��ñ","id":176},{"code":"","available":0,"logistics_company":"����","id":177},{"code":"FJGZLT","available":0,"logistics_company":"������Զ","id":178},{"code":"","available":0,"logistics_company":"E�ؿ�","id":179},{"code":"SELF","available":1,"logistics_company":"����","id":180},{"code":"","available":0,"logistics_company":"����","id":181},{"code":"","available":0,"logistics_company":"����","id":182},{"code":"KYE","available":1,"logistics_company":"��Խ����","id":183},{"code":"","available":0,"logistics_company":"���ֻ����","id":184},{"code":"CHENGJI","available":0,"logistics_company":"�Ǽ��ٵ�","id":185},{"code":"USPS","available":1,"logistics_company":"usps","id":186},{"code":"ANJELEX","available":1,"logistics_company":"�ൺ����","id":187},{"code":"","available":0,"logistics_company":"��ͨ��","id":188},{"code":"","available":0,"logistics_company":"������","id":189},{"code":"TUXIAN","available":0,"logistics_company":";��","id":190},{"code":"CNKD","available":0,"logistics_company":"������","id":191},{"code":"EMSKD","available":0,"logistics_company":"EMS���ÿ��","id":192},{"code":"","available":0,"logistics_company":"��վ����","id":193},{"code":"","available":0,"logistics_company":"�ɿ�","id":194},{"code":"XLOBO","available":1,"logistics_company":"���������ٵ�","id":195},{"code":"","available":0,"logistics_company":"��̩���ʿ��","id":196},{"code":"HUANQIU","available":1,"logistics_company":"��������","id":197},{"code":"","available":0,"logistics_company":"168˳���ٵ�","id":198},{"code":"","available":0,"logistics_company":"ȫ����","id":199},{"code":"CG","available":1,"logistics_company":"�̹�����","id":200},{"code":"UAPEX","available":1,"logistics_company":"ȫһ���","id":201},{"code":"","available":0,"logistics_company":"��������","id":202},{"code":"DJKJ","available":0,"logistics_company":"�������","id":203},{"code":"BSKD","available":0,"logistics_company":"�������","id":204},{"code":"YCGWL","available":1,"logistics_company":"Զ�ɿ���","id":205},{"code":"","available":0,"logistics_company":"���ڹ����ٵ�","id":206},{"code":"","available":0,"logistics_company":"����ת��","id":207},{"code":"ANNENG","available":1,"logistics_company":"���ܿ��","id":208},{"code":"EPS","available":0,"logistics_company":"���ڹ��ʿ���","id":209},{"code":"HOAU","available":1,"logistics_company":"��ػ���","id":210},{"code":"ZHONGYOUWULIU","available":1,"logistics_company":"�����ٵ�","id":211},{"code":"","available":0,"logistics_company":"hi����","id":212},{"code":"INTEREMS","available":1,"logistics_company":"EMS-���ʼ�","id":213},{"code":"ZTKY","available":1,"logistics_company":"��������","id":214},{"code":"","available":0,"logistics_company":"��Դ����","id":215},{"code":"XBWL","available":1,"logistics_company":"�°�����","id":216},{"code":"FLASH","available":1,"logistics_company":"Flash Express","id":217},{"code":"NSF","available":0,"logistics_company":"��˳��NSF","id":218},{"code":"","available":0,"logistics_company":"���ʿ��","id":219},{"code":"","available":0,"logistics_company":"������������","id":220},{"code":"DCS","available":0,"logistics_company":"DCS GLOBAL","id":221},{"code":"","available":0,"logistics_company":"Ѹ�ٿ��","id":222},{"code":"FTD","available":1,"logistics_company":"���ڴ�","id":223},{"code":"QFWL","available":0,"logistics_company":"��������","id":224},{"code":"","available":0,"logistics_company":"����ͨ����","id":225},{"code":"EWE","available":1,"logistics_company":"EWEȫ����","id":226},{"code":"RRS","available":1,"logistics_company":"����˳����","id":227},{"code":"SNWL","available":1,"logistics_company":"�������","id":228},{"code":"BESTQJT","available":1,"logistics_company":"��������","id":229},{"code":"DEBANGWULIU","available":1,"logistics_company":"�°�����","id":230},{"code":"WEITEPAI","available":0,"logistics_company":"΢����","id":231},{"code":"MYAAE","available":1,"logistics_company":"AAEȫ��ר��","id":232},{"code":"ARAMEX","available":1,"logistics_company":"Aramex","id":233},{"code":"ASENDIA","available":0,"logistics_company":"Asendia USA","id":234},{"code":"CITYLINK","available":0,"logistics_company":"City-Link","id":235},{"code":"COE","available":1,"logistics_company":"COE�������","id":236},{"code":"DHLDE","available":0,"logistics_company":"DHL�¹�","id":237},{"code":"DHL","available":0,"logistics_company":"DHLȫ��","id":238},{"code":"DHLCN","available":1,"logistics_company":"DHL�й�","id":239},{"code":"EMSGJ","available":0,"logistics_company":"EMS����","id":240},{"code":"FEDEXUS","available":0,"logistics_company":"FedEx����","id":241},{"code":"FEDEXCN","available":0,"logistics_company":"FedEx�й�","id":242},{"code":"OCS","available":0,"logistics_company":"OCS","id":243},{"code":"ONTRAC","available":0,"logistics_company":"OnTrac","id":244},{"code":"TNT","available":0,"logistics_company":"TNT","id":245},{"code":"UPS","available":1,"logistics_company":"UPS","id":246},{"code":"POSTAL","available":0,"logistics_company":"��������������","id":247},{"code":"POSTAR","available":0,"logistics_company":"����͢����","id":248},{"code":"POSTAE","available":0,"logistics_company":"����������","id":249},{"code":"POSTEE","available":0,"logistics_company":"��ɳ��������","id":250},{"code":"POSTAT","available":0,"logistics_company":"�µ�������","id":252},{"code":"POSTAU","available":0,"logistics_company":"�Ĵ���������","id":253},{"code":"POSTPK","available":0,"logistics_company":"�ͻ�˹̹����","id":254},{"code":"POSTBR","available":0,"logistics_company":"��������","id":255},{"code":"POSTBY","available":0,"logistics_company":"�׶���˹����","id":256},{"code":"EES","available":0,"logistics_company":"�ٸ�����","id":257},{"code":"POSTB","available":0,"logistics_company":"�����ż�","id":258},{"code":"POSTBG","available":0,"logistics_company":"������������","id":259},{"code":"BLSYZ","available":0,"logistics_company":"����ʱ����","id":260},{"code":"BLYZ","available":0,"logistics_company":"��������","id":261},{"code":"CXCOD","available":1,"logistics_company":"��ϲ����","id":262},{"code":"DTW","available":1,"logistics_company":"��������","id":263},{"code":"4PX","available":1,"logistics_company":"���ķ�","id":264},{"code":"RUSTON","available":0,"logistics_company":"����ͨ","id":265},{"code":"FGYZ","available":0,"logistics_company":"��������","id":266},{"code":"GZFY","available":0,"logistics_company":"������","id":267},{"code":"ZTKY","available":1,"logistics_company":"�ɱ����","id":268},{"code":"HZABC","available":0,"logistics_company":"��Զ(������)����","id":269},{"code":"POSTFI","available":0,"logistics_company":"��������","id":270},{"code":"POSTCO","available":0,"logistics_company":"���ױ�������","id":271},{"code":"EPOST","available":0,"logistics_company":"��������","id":272},{"code":"HLWL","available":1,"logistics_company":"��·����","id":273},{"code":"HQKY","available":0,"logistics_company":"�������","id":274},{"code":"TMS56","available":1,"logistics_company":"������","id":275},{"code":"CNEX","available":1,"logistics_company":"�Ѽ�����","id":276},{"code":"JIAYI","available":1,"logistics_company":"��������","id":277},{"code":"KERRYEAS","available":0,"logistics_company":"�����ͨ","id":278},{"code":"JKYZ","available":0,"logistics_company":"�ݿ�����","id":279},{"code":"JDYWL","available":0,"logistics_company":"�������","id":280},{"code":"SZKKE","available":1,"logistics_company":"�����ٵ�","id":281},{"code":"POSTHR","available":0,"logistics_company":"���޵�������","id":282},{"code":"POSTLV","available":0,"logistics_company":"����ά������","id":283},{"code":"POSTLB","available":0,"logistics_company":"���������","id":284},{"code":"LTS","available":1,"logistics_company":"���ͨ","id":285},{"code":"POSTMT","available":0,"logistics_company":"���������","id":286},{"code":"POSTMK","available":0,"logistics_company":"���������","id":287},{"code":"POSTMU","available":0,"logistics_company":"ë����˹����","id":288},{"code":"SERPOST","available":0,"logistics_company":"��³����","id":289},{"code":"MBEX","available":0,"logistics_company":"�����","id":290},{"code":"CAE","available":1,"logistics_company":"�񺽿��","id":291},{"code":"SZML56","available":0,"logistics_company":"��������","id":292},{"code":"POSTMD","available":0,"logistics_company":"Ħ����������","id":293},{"code":"POSTZA","available":0,"logistics_company":"�Ϸ�����","id":294},{"code":"POSTNO","available":0,"logistics_company":"Ų������","id":295},{"code":"POSTPT","available":0,"logistics_company":"����������","id":296},{"code":"QRT","available":0,"logistics_company":"ȫ��ͨ","id":297},{"code":"RBYZEMS","available":0,"logistics_company":"�ձ�����","id":298},{"code":"POSTSE","available":0,"logistics_company":"�������","id":299},{"code":"POSTCH","available":0,"logistics_company":"��ʿ����","id":300},{"code":"POSTSRB","available":0,"logistics_company":"����ά������","id":301},{"code":"SANTAI","available":0,"logistics_company":"��̬�ٵ�","id":302},{"code":"POSTSA","available":0,"logistics_company":"ɳ������","id":303},{"code":"SZSA56","available":0,"logistics_company":"ʥ������","id":304},{"code":"FJSFWLJTYXGS","available":1,"logistics_company":"ʢ������","id":305},{"code":"SHENGHUI","available":1,"logistics_company":"ʢ������","id":306},{"code":"POSTSK","available":0,"logistics_company":"˹�工������","id":307},{"code":"POSTSI","available":0,"logistics_company":"˹������������","id":308},{"code":"SUIJIAWL","available":0,"logistics_company":"�������","id":309},{"code":"POSTTH","available":0,"logistics_company":"̩������","id":310},{"code":"POSTTR","available":0,"logistics_company":"����������","id":311},{"code":"MANCOWL","available":1,"logistics_company":"�������","id":312},{"code":"POSTUA","available":0,"logistics_company":"�ڿ�������","id":313},{"code":"POSTES","available":0,"logistics_company":"����������","id":314},{"code":"XFWL","available":1,"logistics_company":"�ŷ�����","id":315},{"code":"POSTHU","available":0,"logistics_company":"����������","id":316},{"code":"AIR","available":1,"logistics_company":"�Ƿ��ٵ�","id":317},{"code":"POSTAM","available":0,"logistics_company":"������������","id":318},{"code":"YWWL","available":1,"logistics_company":"��������","id":319},{"code":"POSTIT","available":0,"logistics_company":"���������","id":320},{"code":"FEC","available":0,"logistics_company":"�����ٵ�","id":321},{"code":"POSTIN","available":0,"logistics_company":"ӡ������","id":322},{"code":"ROYALMAIL","available":0,"logistics_company":"Ӣ���ʼ�����","id":323},{"code":"POSTBBZ","available":1,"logistics_company":"������׼���","id":324},{"code":"CNPOSTGJ","available":1,"logistics_company":"�������ʰ���","id":325},{"code":"YFEXPRESS","available":0,"logistics_company":"Խ������","id":326},{"code":"YTZG","available":0,"logistics_company":"��ͨ�иۿ��","id":327},{"code":"ZENY","available":0,"logistics_company":"�����ٵ�","id":328},{"code":"POSTCL","available":0,"logistics_company":"��������","id":329},{"code":"SPSR","available":0,"logistics_company":"�ж���","id":330},{"code":"CRE","available":1,"logistics_company":"��������","id":332},{"code":"KFW","available":0,"logistics_company":"�������","id":333},{"code":"KDN","available":0,"logistics_company":"�����","id":334},{"code":"YOUBANG","available":1,"logistics_company":"�Ű��������","id":335},{"code":"TJ","available":1,"logistics_company":"��ʿ��","id":336},{"code":"FY","available":1,"logistics_company":"������","id":337},{"code":"BM","available":1,"logistics_company":"����������","id":338},{"code":"EKM","available":1,"logistics_company":"�׿���","id":339},{"code":"JDKD","available":1,"logistics_company":"�����������","id":340},{"code":"SUBIDA","available":1,"logistics_company":"�ٱش�","id":341},{"code":"DJKJWL","available":0,"logistics_company":"�������","id":342},{"code":"ZTOKY","available":1,"logistics_company":"��ͨ����","id":343},{"code":"YDKY","available":1,"logistics_company":"�ϴ����","id":344},{"code":"ANKY","available":1,"logistics_company":"���ܿ���","id":345},{"code":"ANDE","available":1,"logistics_company":"��������","id":346},{"code":"WM","available":1,"logistics_company":"����������","id":347},{"code":"YMDD","available":1,"logistics_company":"Ҽ�׵δ�","id":348},{"code":"DD","available":1,"logistics_company":"������","id":349},{"code":"PJ","available":1,"logistics_company":"Ʒ��","id":350},{"code":"OTP","available":1,"logistics_company":"��ŵ���ؿ�","id":351},{"code":"AXWL","available":1,"logistics_company":"��Ѹ����","id":352},{"code":"YJ","available":0,"logistics_company":"�Ѽ��ٵ�","id":353},{"code":"SDSD","available":1,"logistics_company":"D������","id":354},{"code":"STOINTER","available":1,"logistics_company":"��ͨ����","id":355},{"code":"YZT","available":1,"logistics_company":"һ��ͨ","id":356},{"code":"JGSD","available":0,"logistics_company":"�����ٵ�","id":357},{"code":"SXJD","available":1,"logistics_company":"˳�Ľݴ�","id":358},{"code":"QH","available":1,"logistics_company":"Ⱥ�����ʻ���","id":359},{"code":"ZWYSD","available":1,"logistics_company":"�������ٵ�","id":360},{"code":"ZZSY","available":1,"logistics_company":"׿־����","id":361},{"code":"JZMSD","available":1,"logistics_company":"����è�ٵ�","id":362},{"code":"GJ","available":1,"logistics_company":"�߽�����","id":363},{"code":"SQWL","available":1,"logistics_company":"��������","id":364},{"code":"FR","available":1,"logistics_company":"���ڹ�Ӧ��","id":365},{"code":"ZY","available":1,"logistics_company":"��Զ","id":366},{"code":"YDGJ","available":1,"logistics_company":"�ϴ����","id":367},{"code":"MKGJ","available":1,"logistics_company":"�������","id":368},{"code":"NFCM","available":0,"logistics_company":"�Ϸ���ý","id":369},{"code":"WSPY","available":1,"logistics_company":"��ʱ����","id":370},{"code":"ZTOINTER","available":1,"logistics_company":"��ͨ����","id":371},{"code":"SFKY","available":1,"logistics_company":"˳�����","id":372},{"code":"MGWL","available":1,"logistics_company":"����ѷ�ۺ�����","id":373},{"code":"HKE","available":1,"logistics_company":"HKE�����ٵ�","id":374},{"code":"EFSPOST","available":1,"logistics_company":"������ƽ������","id":375},{"code":"HTINTER","available":1,"logistics_company":"��������","id":376},{"code":"BSE","available":1,"logistics_company":"������ʿ��","id":377},{"code":"YLJY","available":0,"logistics_company":"��������","id":378},{"code":"ZYSFWL","available":0,"logistics_company":"ת���ķ�����","id":379},{"code":"WSKD","available":1,"logistics_company":"��ʢ���","id":380},{"code":"YTGJ","available":0,"logistics_company":"Բͨ����","id":381},{"code":"HXWL","available":0,"logistics_company":"��������","id":382},{"code":"HYWL","available":0,"logistics_company":"��Զ����","id":383},{"code":"JTKD","available":0,"logistics_company":"���ÿ��","id":384}],"request_id":"15807151639054916"}}
//					request.setLogisticsId(120L);//�������
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
						//�����ɹ�
						
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						// �����ɹ�
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						successcount++;
						message = "�����ش�ƽ̨�ɹ�";

					} else {
						fallcount++;
						message = "�����ش�ƽ̨ʧ��";

					}
				}
				
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent(message);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
			} else if (storeRecord.getEcId().equals("MaiDu")) {
				try {
					 storeSettings = storeSettingsDao.select(platOrder.getStoreId());
					
					XS_OrdersGetListResponse result = platOrderMaiDu.MaiDuOrderShip1(expressCorp,logisticsTab, ecExpress,
							storeSettings);
					if (result.getDone()) {
						// �����ɹ�
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						

						platOrder.setIsShip(1);
						platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
						platOrder.setShipTime(logisticsTab.getShipDate());
						platOrderDao.update(platOrder);
						
						message = result.getMsg();
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent(message);
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8����
						logRecord.setTypeName("����");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
						
						successcount++;
					} else {
						if(result.getMsg().equals("�����ѷ���������ɣ�ϵͳĬ�Ϸ����ش��ɹ�")) {
							
							// �����ɹ�
							logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
							logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
							logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
							logisticsTabDao.update(logisticsTab);// ������������Ϣ
							

							platOrder.setIsShip(1);
							platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
							platOrder.setShipTime(logisticsTab.getShipDate());
							platOrderDao.update(platOrder);
							
							
							message = result.getMsg();
							// ��־��¼
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							if (misUser != null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent(message);
							logRecord.setOperatTime(sdf.format(new Date()));
							logRecord.setOperatType(8);// 8����
							logRecord.setTypeName("����");
							logRecord.setOperatOrder(platOrder.getEcOrderId());
							logRecordDao.insert(logRecord);
							
							
							successcount++;
							
						}else {
						
						message = result.getMsg();
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent(message);
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8����
						logRecord.setTypeName("����");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						fallcount++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					message = "�����쳣��������";
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8����
					logRecord.setTypeName("����");
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
						//�����ɹ�
						
						platOrder.setIsShip(1);
						platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
						platOrder.setShipTime(logisticsTab.getShipDate());
						platOrderDao.update(platOrder);
						
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						
						successcount++;

						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�����ϴ��ɹ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8����
						logRecord.setTypeName("����");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						
						
					} 
					if(result.getSnerror()!= null) {
						//����ʧ��
						message = result.getSnerror().getErrorCode();
						//System.out.println("AAAAAAAAAAAAAAAAAA"+message);
						
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("����ʧ�ܣ�"+message);
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8����
						logRecord.setTypeName("����");
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
					objectNode.put("express_company_code", ecExpress.getProviderCode());//��׼��ݹ�˾����
					objectNode.put("express_no", logisticsTab.getExpressCode());//��ݵ���
					String json = objectNode.toString();
					StoreSettings storeSettings1 = storeSettingsDao.select(logisticsTab.getStoreId());
					String jdRespStr = ECHelper.getKaola("kaola.logistics.deliver", storeSettings1, json);
					ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
					//System.out.println("���ؽ����"+jdRespJson);
					if(jdRespJson.has("error_response")) {
						//����ʧ��
						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("����ʧ�ܣ�"+jdRespJson.get("error_response").get("subErrors").get(0).get("msg").asText());
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8����
						logRecord.setTypeName("����");
						logRecord.setOperatOrder(platOrder.getEcOrderId());
						logRecordDao.insert(logRecord);
						fallcount++;
					}else {
						//�����ɹ�
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						successcount++;

						// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�����ϴ��ɹ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(8);// 8����
						logRecord.setTypeName("����");
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
						//System.err.println("��ϵ���Ӵ���");

					
						fallcount++;
						continue;
					}

					String method = "/ark/open_api/v0/packages/";

					Map<String, String> map = new HashMap<String, String>();
//				����ID
					map.put("status", "shipped");
					map.put("express_no", logisticsTab.getExpressCode());
					map.put("express_company_code", name.getExpressCode());

					method = method + platOrder.getEcOrderId();

					JSONObject response = ECHelper.getXHSPUT(method, storeSettings, map);

					Boolean xhsSuccess = response.getBoolean("success");
					if (!xhsSuccess) {
						fallcount++;
						// ����ʧ��
						message = response.getString("error_msg");

					} else {
						// �����ɹ�
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						successcount++;
						message = "�����ش�ƽ̨�ɹ�";

					}

					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8����
					logRecord.setTypeName("����");
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
						System.err.println("��ϵ���Ӵ���");
						fallcount++;
						continue;
					}



					Map<String, String> map = new HashMap<String, String>();
//				����ID
//				"order_id":34564333, // ����ID
//				"express_sn":1345454334,// ��ݵ���
//				"express_name":"˳������", // ��ݹ�˾����, BizName
//				"bizcode":"shunfeng", // ��ݹ�˾���, BizCode
//				"userId":1345434245, // ���Ե�¼�̻���̨�������˺�
//				"descr": "", // "��������ע"
//				"pid":"bigint(��Ʒ�ͺ�ID)",
//				"donkey_id": 0, // ��С¿������0
//				"count":2, // ��������
//				"ext": "pid1:count1-pid2:count2" // ����������ע
					map.put("order_id",platOrder.getEcOrderId() );// ����ID
					map.put("express_sn", logisticsTab.getExpressCode());// ��ݵ���
					map.put("express_name", codeName.getExpressName());// ��ݹ�˾����
					map.put("bizcode", codeName.getExpressCode());// ��ݹ�˾���
					map.put("userId", storeRecord.getSellerId());// ���Ե�¼�̻���̨�������˺�
//					map.put("descr", "");// ��������ע
//					map.put("pid", "0");// bigint(��Ʒ�ͺ�ID)
					map.put("donkey_id", "0"); // ��С¿������0
					XMYPLogisticsTabBean xmypLogisticsTabBean=platOrderXMYP.XMYPapi(method,map,storeSettings,XMYPLogisticsTabBean.class);
//String ssssss="{\"code\":0,\"message\":\"OK\",\"result\":{\"delivery_id\":419122054900568801,\"oid\":4191220549005688,\"uid\":2314717465,\"pid\":0,\"stime\":1576841428,\"atime\":0,\"express_sn\":\"544871878946\",\"bizcode\":\"��ͨ���\",\"express_name\":\"zhongtong\",\"status\":6,\"descr\":\"\",\"otime\":1576841428,\"ttl\":0,\"count\":1,\"ext\":\"\",\"donkey_id\":0}}";
					if (xmypLogisticsTabBean.getCode() !=0) {
						fallcount++;
						// ����ʧ��
						message = xmypLogisticsTabBean.getMessage();

					} else {
						// �����ɹ�
						platOrder.setExpressNo(logisticsTab.getExpressCode());
						platOrder.setIsShip(1);
						platOrder.setShipTime(sdf.format(new Date()));
						platOrderDao.update(platOrder);
						logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
						logisticsTab.setIsBackPlatform(1);// �����ѻش�ƽ̨
						logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
						logisticsTabDao.update(logisticsTab);// ������������Ϣ
						successcount++;
						message = "�����ش�ƽ̨�ɹ�";

					}

					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(8);// 8����
					logRecord.setTypeName("����");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
				} catch (Exception e) {
					// TODO: handle exception
					fallcount++;
				}
			}
			else {
				//��֧�ֵ�ƽ̨
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("����ʧ�ܣ���Ӧƽ̨�ݲ�֧�ַ����ش�����ʹ��ǿ�Ʒ�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(8);// 8����
				logRecord.setTypeName("����");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/platOrderShip", true,
					"ƽ̨���������ɹ������γɹ��������ϴ�" + successcount + "����ʧ��" + fallcount + "��", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}

	// ����������id��ȡ��������Ϣ
	// ����������ĵ��̶����Ż�ȡ���̶�����Ϣ
	// ���ݶ����ĵ���ID��ȡ���̵���
	// ���ݵ��̵�����ȡ����ƽ̨ID
	// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
	// ��ȡ��ݹ�˾������Ϣ
	// ��ȡƽ̨���õĵ����浥��ݹ�˾��Ϣ
	public String tmECExpressOrder(EcExpress ecExpress, ExpressCorp expressCorp, StoreSettings storeSettings,
			EcExpressSettings ecExpressSettings, LogisticsTab logisticsTab, PlatOrder platOrder,
			StoreRecord storeRecord) throws Exception {

		List<Map<String, Object>> PackageItems = new ArrayList<Map<String, Object>>();// package_items PackageItem[]
		Map<String, Object> PackageItem = new HashMap<String, Object>();// package_items PackageItem[] true �����������Ʒ����
		for (PlatOrders platOrders : platOrdersDao.select(platOrder.getOrderId())) {
			System.err.println("\t" + platOrders.getGoodName());
			PackageItem.put("item_name", platOrders.getGoodName());// item_name String true �·� ��Ʒ����
			PackageItem.put("count", platOrders.getGoodNum());// count Number true 123 ��Ʒ����
			PackageItems.add(PackageItem);
		}

		Map<String, Object> WaybillAddresss = new HashMap<String, Object>(); // consignee_address WaybillAddress true
																				// ��\������ַ

		WaybillAddresss.put("area", platOrder.getCounty());// area String false ������ �����ƣ�������ַ��
		WaybillAddresss.put("province", platOrder.getProvince());// province String true ���� ʡ���ƣ�һ����ַ��
		WaybillAddresss.put("town", platOrder.getTown());// town String false ����ׯ �ֵ�\�����ƣ��ļ���ַ��
		WaybillAddresss.put("address_detail", platOrder.getRecAddress());// address_detail String true
																			// ����·�߾��������֣��ƾ�����9��¥21��Ԫ6013 ��ϸ��ַ
		WaybillAddresss.put("city", platOrder.getCity());// city String false ������ �����ƣ�������ַ��

		Map<String, Object> TradeOrderInfo = new HashMap<String, Object>();
		TradeOrderInfo.put("consignee_name", platOrder.getRecName());// consignee_name String true ���� �ջ���
		TradeOrderInfo.put("order_channels_type", ecExpressSettings.getPlatId());// ƽ̨id String true TB ��������
		TradeOrderInfo.put("consignee_phone", platOrder.getRecMobile());// consignee_phone String true 13242422352
																		// �ջ�����ϵ��ʽ
		TradeOrderInfo.put("consignee_address", WaybillAddresss);// consignee_address WaybillAddress true ��\������ַ
		TradeOrderInfo.put("send_phone", expressCorp.getDeliverPhone());// send_phone String false 13242422352 ��������ϵ��ʽ

//		TradeOrderInfo.put("weight", 0);// weight Number false 123 �����������ˣ�
		TradeOrderInfo.put("send_name", expressCorp.getDeliver());// send_name String false ���� ����������
		System.err.println("\t��ݷ����Ʒ���ͱ���\t\"û����ôд\"д������\t STANDARD_EXPRESS");
		TradeOrderInfo.put("product_type", "STANDARD_EXPRESS");// product_type String true STANDARD_EXPRESS ��ݷ����Ʒ���ͱ���
		TradeOrderInfo.put("real_user_id", storeRecord.getSellerId());// �������õĻ�Ա�� Number true 13123 ʹ����ID
//		TradeOrderInfo.put("volume", 0);// volume Number false 123 ����������������ף�
//			TradeOrderInfo.put("package_id", "������");// package_id String false E12321321-1234567 ������(����ERP������)

//			TradeOrderInfo.put("logistics_service_list", LogisticsService);// logistics_service_list LogisticsService[]
//																			// false ����������������
		TradeOrderInfo.put("package_items", PackageItems);// package_items PackageItem[] true �����������Ʒ����
//			ArrayList<String> vale=new ArrayList<String>();
//			vale.add("adfadf");

		TradeOrderInfo.put("trade_order_list", platOrder.getEcOrderId());// trade_order_list String[] true
																			// 12321321,12321321 ���׶����б�

		List<Map<String, Object>> TradeOrderInfos = new ArrayList<Map<String, Object>>();// ��������
		TradeOrderInfos.add(TradeOrderInfo);
		Map<String, Object> WaybillAddress = new HashMap<String, Object>();// ������ַ
		WaybillAddress.put("area", ecExpress.getCountryName());// area String false ������ �����ƣ�������ַ��
		WaybillAddress.put("province", ecExpress.getProvinceName());// province String true ���� ʡ���ƣ�һ����ַ��
		WaybillAddress.put("town", ecExpress.getCountrysideName());// town String false ����ׯ �ֵ�\�����ƣ��ļ���ַ��
		WaybillAddress.put("address_detail", ecExpress.getAddress());// address_detail String true
																		// ����·�߾��������֣��ƾ�����9��¥21��Ԫ6013 ��ϸ��ַ
		WaybillAddress.put("city", ecExpress.getCityName());// city String false ������ �����ƣ�������ַ��

		Map<String, Object> WaybillApplyNewRequest = new HashMap<String, Object>();
		WaybillApplyNewRequest.put("cp_code", ecExpress.getProviderCode());// cp_code String true ZTO ���������̱���
		WaybillApplyNewRequest.put("shipping_address", WaybillAddress);// shipping_address
																		// WaybillAddress true
																		// ��\������ַ
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
		// �ɹ�
		String expressCode = "";
		for (Iterator<JsonNode> itemInfoInfoIterator = codes.iterator(); itemInfoInfoIterator.hasNext();) {
			JsonNode itemInfo = itemInfoInfoIterator.next();

//				short_address	String	hello world	�����ջ���ַ���ش�ͷ����Ϣ
//				trade_order_info	TradeOrderInfo		�浥��Ӧ�Ķ�����
//				waybill_code	String	hello world	���ص��浥��
//				package_center_code	String	123321	�����ش���
//				package_center_name	String	�����ຼ	����������
//				print_config	String	SDFASFAFSAFSADF	��ӡ���������ali-print���
//				shipping_branch_code	String	123132	�浥�Ŷ�Ӧ���������������㣨��֧����������
//				consignee_branch_name	String	�ຼһ��	������Ӧ���ɼ����ռ����������������㣨��֧����������
//				shipping_branch_name	String	��������	�浥�Ŷ��ڵ��������������㣨��֧����������
//				consignee_branch_code	String	123132	������Ӧ���ɼ����ռ����������������㣨��֧����������
			itemInfo.get("short_address");
			// �����ȡ���ĵ����浥��
			logisticsTab.setExpressCode(itemInfo.get("waybill_code").asText());// ���ص��浥��
//				logisticsTab.setBigShotCode(node.get("bigShotCode").asText());//null
			logisticsTab.setBigShotName(itemInfo.get("short_address").asText());// �����ջ���ַ���ش�ͷ����Ϣ
			logisticsTab.setGatherCenterCode(itemInfo.get("package_center_code").asText());// �����ش���
			logisticsTab.setGatherCenterName(itemInfo.get("package_center_name").asText());// ����������
//				logisticsTab.setSecondSectionCode(node.get("secondSectionCode").asText());//������
//				logisticsTab.setThirdSectionCode(node.get("thirdSectionCode").asText());//������
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
			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Խ��в���
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			// ����������ĵ��̶����Ż�ȡ���̶�����Ϣ
			PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
			// ���ݶ����ĵ���ID��ȡ���̵���
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
			//StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ������õ�ǰƽ̨��ͨ�����浥�ĵ���");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���浥
				logRecord.setTypeName("ȡ���浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			// ��ȡ��ݹ�˾������Ϣ
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			if(expressCorp.getCompanyCode().equals("ZTO")||expressCorp.getCompanyCode().equals("YTO")||
					expressCorp.getCompanyCode().equals("STO")||expressCorp.getCompanyCode().equals("YUNDA")) {
				
			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ǰ������Ӧ��ݹ�˾�Ǽ����ͣ���ʹ��[ȡ������]���ܰ�ťִ��ȡ������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���浥
				logRecord.setTypeName("ȡ���浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			// ��ȡƽ̨���õĵ����浥��ݹ�˾��Ϣ
			EcExpress ecExpress=null;
			try {
				ecExpress = ecExpressDao.select(storeRecord.getEcId(), expressCorp.getCompanyCode(),
						expressCorp.getProvince(), expressCorp.getCity(), expressCorp.getCountry(),expressCorp.getDetailedAddress());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݹ�˾������Ӧ�����浥�˻���Ϣ�ظ�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���浥
				logRecord.setTypeName("ȡ���浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				map.put("fallcount",fallcount);
				continue;
			}
			if(ecExpress==null) {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݹ�˾�������ô�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���浥
				logRecord.setTypeName("ȡ���浥");
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
					"ȡ�������浥�ɹ������γɹ�ȡ���浥" + map.get("successcount") + "����ʧ��" + map.get("fallcount") + "��", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	private String PDDcancelECExpressOrderYun(LogisticsTab logisticsTab, StoreSettings storeSettings,
			Map<String, Integer> map, EcExpress ecExpress,MisUser misUser) throws Exception {

//		ȡ�������浥 
		String clientId = storeSettings.getAppKey();
		String clientSecret = storeSettings.getAppSecret();
		String accessToken = storeSettings.getAccessToken();
		PopClient client = new PopHttpClient(clientId, clientSecret);

		PddWaybillCancelRequest request = new PddWaybillCancelRequest();
		request.setWaybillCode(logisticsTab.getExpressCode());// �����浥����
		request.setWpCode(ecExpress.getProviderCode());// CP��ݹ�˾����
		PddWaybillCancelResponse response = client.syncInvoke(request, accessToken);
		System.out.println(JsonUtil.transferToJson(response));

		if (response.getErrorResponse() != null) {
			map.put("fallcount", map.get("fallcount") + 1);
			System.err.println(map.get("fallcount"));
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ȡ���浥�ɹ�");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���ⵥ
			logRecord.setTypeName("ȡ���浥");
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
			// ��־��¼
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(misUser.getAccNum());

						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("ȡ���浥�ɹ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(17);// 17ȡ���ⵥ
						logRecord.setTypeName("ȡ���浥");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
		} else {
			map.put("fallcount", map.get("fallcount") + 1);
			//���ʧ��
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ȡ���浥ʧ��");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���ⵥ
			logRecord.setTypeName("ȡ���浥");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
		}
		return "�ɹ�ȡ��";

	}

	/**
	 * ȡ�������浥
	 * 
	 * @param logisticsTab  ������
	 * @param storeSettings ������Ϣ����
	 * @param successcount  �ɹ�����
	 * @param fallcount     ʧ������
	 */
	private String TMcancelECExpressOrder(LogisticsTab logisticsTab, StoreSettings storeSettings,
			Map<String, Integer> map, EcExpress ecExpress) throws Exception, IOException {

//		ȡ�������浥 taobao.wlb.waybill.i.cancel
		Map<String, Object> waybill = new HashMap<String, Object>();
//		waybill.put("real_user_id", value);//	Number	false	2134234234	�浥ʹ���߱��
		waybill.put("trade_order_list", logisticsTab.getEcOrderId());// String[] true ���׶����б�
		waybill.put("cp_code", ecExpress.getProviderCode());// String true EMS CP��ݹ�˾����
		waybill.put("waybill_code", logisticsTab.getExpressCode());// String true 1100222969702 �����浥����
//		waybill.put("package_id", value);//String	false	E12321321-1234567	ERP�����Ż������
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
		return "�ɹ�ȡ��";

	}

	@Override
	public Map findExpressCodeByOrderId(PlatOrder platOrder, List<PlatOrders> platOrders) {
		// TODO Auto-generated method stub
		Map map = new HashMap<>();
		StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
		if (!StringUtils.isNotEmpty(platOrder.getDeliverWhs())) {
			map.put("message", "��ǰ���������ֿ�Ϊ�գ������Զ�ƥ���Ӧ��ݹ�˾");
			map.put("flag", "false");
			map.put("expressCode", "");
			map.put("templateId", "");
		}
		List<WhsPlatExpressMapp> whsPlatExpressMapps = whsPlatExpressMappDao
				.selectListByPlatIdAndWhsCode(storeRecord.getEcId(), platOrder.getDeliverWhs());

		if (whsPlatExpressMapps.size() == 0) {
			map.put("message", "��ǰ���������ֿ�δ���ö�Ӧ��ݹ�˾");
			map.put("flag", "false");
			map.put("expressCode", "");
			map.put("templateId", "");
		} else if (whsPlatExpressMapps.size() == 1) {
			// �������õĿ�ݹ�˾ֻ��һ��ʱ��ֱ�ӷ��ؿ�ݹ�˾����
			map.put("message", "");
			map.put("flag", "true");
			map.put("expressCode", whsPlatExpressMapps.get(0).getExpressId());
			map.put("templateId", whsPlatExpressMapps.get(0).getTemplateId());
		} else {
			// �ȼ������۵���ϸ��������Ʒ������
			BigDecimal allWeight = BigDecimal.ZERO;
			for (int i = 0; i < platOrders.size(); i++) {
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
				if (invtyDoc.getWeight() != null) {
					allWeight = allWeight
							.add(invtyDoc.getWeight().multiply(new BigDecimal(platOrders.get(i).getInvNum())));
				}
			}
			// ��ͬһ�ֿ��ͬһƽ̨���õĿ�ݹ�˾����1��ʱ
			// ���������е������С����˳��ѭ��ƥ��
			for (int i = 0; i < whsPlatExpressMapps.size(); i++) {
				// �ж����۵���ϸ�����ۺ��Ƿ���������õ�����֮��
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
					// ��Ŀǰѭ��Ϊ���һ��ѭ��ʱ���ߵ���һ��˵��ƥ���������еĿ�ݹ�˾��û�з���������
					map.put("message", "����������" + allWeight.setScale(3) + "kg�����ϲֿ�ƽ̨��ݹ�˾ӳ�䵵���������õ���������");
					map.put("flag", "false");
					map.put("expressCode", "");
					map.put("templateId", "");
				}
			}
		}

		return map;
	}

	/**
	 * ȡ�������浥
	 * 
	 * @param logisticsTab  ������
	 * @param storeSettings ������Ϣ����
	 * @param successcount  �ɹ�����
	 * @param fallcount     ʧ������
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
		return "�ɹ�ȡ��";

	}

	void PDD() {
		String clientId = "your app clientId";

		String clientSecret = "your app clientSecret";

		String refreshToken = "your app refreshToken";

		String code = "your code";
		PopAccessTokenClient accessTokenClient = new PopAccessTokenClient(clientId, clientSecret);
		// ����AccessToken
		try {
			AccessTokenResponse response = accessTokenClient.generate(code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ˢ��AccessToken
		try {

			AccessTokenResponse response = accessTokenClient.refresh(refreshToken);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	// �����浥�ƴ�ӡ�ӿ�
	public JSONObject tmECExpressOrderYun(EcExpress ecExpress, ExpressCorp expressCorp, StoreSettings storeSettings,
			EcExpressSettings ecExpressSettings, LogisticsTab logisticsTab, PlatOrder platOrder,
			StoreRecord storeRecord,MisUser misUser) throws Exception {
		JSONObject asda = new JSONObject();

		CainiaoWaybillIiGetRequest req = new CainiaoWaybillIiGetRequest();
		WaybillCloudPrintApplyNewRequest waybillCloudPrintApplyNewRequest = new WaybillCloudPrintApplyNewRequest();
		waybillCloudPrintApplyNewRequest.setCpCode(ecExpress.getProviderCode());// ������˾Code������С��20
//		obj1.setProductCode("Ŀǰ�Ѿ����Ƽ�ʹ�ô��ֶΣ��벻Ҫʹ��");
		UserInfoDto sender = new UserInfoDto();// ��������Ϣ
		AddressDto obj3 = new AddressDto();// ������ַ��Ҫͨ��search�ӿ�

		obj3.setCity(ecExpress.getCityName());
		obj3.setDetail(ecExpress.getAddress());
		obj3.setDistrict(ecExpress.getCountryName());
		obj3.setProvince(ecExpress.getProvinceName());
		obj3.setTown(ecExpress.getCountrysideName());
		sender.setAddress(obj3);
		sender.setMobile(expressCorp.getDeliverPhone());
		sender.setName(expressCorp.getDeliver());// ����������С��40
		List<TradeOrderInfoDto> tradeOrderInfoDto = new ArrayList<TradeOrderInfoDto>();
		TradeOrderInfoDto obj6 = new TradeOrderInfoDto();

		// obj6.setLogisticsServices("�粻��Ҫ������񣬸�ֵΪ��");
		obj6.setObjectId("1");// ����ID
//		venderId
		obj6.setUserId(Long.valueOf(storeSettings.getVenderId()));// ʹ����ID��ʹ�õ����浥�˺ŵ�ʵ���̼�ID�������һ�������浥�˺Ŷ������ʹ��ʱ���봫����̵��̼�ID��
		// ��èƽ̨�ƴ�ӡģ��

		JSONObject jsonObjectdate = new JSONObject();
		JSONObject jsonObject = new JSONObject();
		List<WhsPlatExpressMapp> list = whsPlatExpressMappDao.selectCloudPrint("TM", ecExpress.getProviderCode());
		if (list.size() > 0) {
//			obj6.setTemplateUrl("http://cloudprint.cainiao.com/template/standard/" + list.get(0).getCloudPrint());// �ƴ�ӡ��׼ģ��URL����װ�ƴ�ӡ���ʹ�ã�ֵ��ʽhttp://cloudprint.cainiao.com/template/standard/${ģ��ID}��
			obj6.setTemplateUrl(list.get(0).getCloudPrint());// �ƴ�ӡ��׼ģ��URL����װ�ƴ�ӡ���ʹ�ã�ֵ��ʽhttp://cloudprint.cainiao.com/template/standard/${ģ��ID}��

			jsonObjectdate.put("templateURL", list.get(0).getCloudPrintCustom());
		} else {
			asda.put("k", "���ƴ�ӡ��׼ģ��");
			return asda;
		}

		UserInfoDto recipient = new UserInfoDto();// �ռ�����Ϣ
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

		obj6.setOrderInfo(orderInfo);// ������Ϣ

		tradeOrderInfoDto.add(obj6);

		PackageInfoDto obj10 = new PackageInfoDto();// ������Ϣ
		obj10.setId(logisticsTab.getOrderId());//����id�����ڲ�ϵ�������ֻ�ܴ������֡���ĸ���»��ߣ���������ʱֵ�����ظ�����Сд���У���123A,123a ���ɵ�����ͬID���������һ������ȡ��ʧ�ܣ�
		List<Item> list12 = new ArrayList<Item>();

		jsonObjectdate.put("data", jsonObject);

		jsonObject.put("ItemName", "");
		jsonObject.put("ItemNum", "");

		for (PlatOrders platOrders : platOrdersDao.select(platOrder.getOrderId())) {
			Item obj13 = new Item();
			obj13.setCount(platOrders.getGoodNum().longValue());// count Number true 123 ��Ʒ����
			obj13.setName(platOrders.getGoodName());// item_name String true �·� ��Ʒ����
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

		waybillCloudPrintApplyNewRequest.setSender(sender);// ��������Ϣ
		waybillCloudPrintApplyNewRequest.setTradeOrderInfoDtos(tradeOrderInfoDto);// �����浥��Ϣ����������Ϊ10
		waybillCloudPrintApplyNewRequest.setNeedEncrypt(false);
		req.setParamWaybillCloudPrintApplyNewRequest(waybillCloudPrintApplyNewRequest);

		// ת��
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
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ʧ�ܣ�"+rsp.getSubMsg());
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(14);// 14ȡ�����浥
			logRecord.setTypeName("ȡ�����浥");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			return asda;
		}
//		{"body":"{\"cainiao_waybill_ii_get_response\":{\"modules\":{\"waybill_cloud_print_response\":[{\"object_id\":\"1\",\"print_data\":\"{\\\"data\\\":{\\\"_dataFrom\\\":\\\"waybill\\\",\\\"adsInfo\\\":{\\\"bannerUrl\\\":\\\"http:\\/\\/cdn-cloudprint.cainiao.com\\/waybill-print\\/cloudprint-imgs\\/c6fc411b400f4471a4d28e0a4a18c40b.jpg\\\"},\\\"cpCode\\\":\\\"ZTO\\\",\\\"needEncrypt\\\":false,\\\"packageInfo\\\":{\\\"items\\\":[{\\\"count\\\":1,\\\"name\\\":\\\"�������콢�꡿�����и��̷���Ʒ ������800g��װ �����������̷�\\\"}],\\\"volume\\\":0,\\\"weight\\\":0},\\\"parent\\\":false,\\\"recipient\\\":{\\\"address\\\":{\\\"city\\\":\\\"������\\\",\\\"detail\\\":\\\"�㰲���ڽֵ��㻪��С��3��¥208\\\",\\\"district\\\":\\\"������\\\",\\\"province\\\":\\\"����\\\"},\\\"mobile\\\":\\\"15810104492\\\",\\\"name\\\":\\\"¬��˳\\\"},\\\"routingInfo\\\":{\\\"consolidation\\\":{\\\"name\\\":\\\"����\\\"},\\\"origin\\\":{\\\"code\\\":\\\"02293\\\",\\\"name\\\":\\\"����г���\\\"},\\\"receiveBranch\\\":{\\\"code\\\":\\\"01090\\\"},\\\"routeCode\\\":\\\"22-01 13\\\",\\\"sortation\\\":{\\\"name\\\":\\\"800-\\\"},\\\"startCenter\\\":{},\\\"terminalCenter\\\":{}},\\\"sender\\\":{\\\"address\\\":{\\\"city\\\":\\\"���\\\",\\\"detail\\\":\\\"�����Ͳ�ҵ԰������·Ӻ�¿Ƽ�Ժ��\\\",\\\"district\\\":\\\"������\\\",\\\"province\\\":\\\"�����\\\"},\\\"mobile\\\":\\\"18616950914\\\",\\\"name\\\":\\\"������\\\"},\\\"shippingOption\\\":{\\\"code\\\":\\\"STANDARD_EXPRESS\\\",\\\"title\\\":\\\"��׼���\\\"},\\\"waybillCode\\\":\\\"75163074465093\\\"},\\\"signature\\\":\\\"MD:8wHe5ZI7tZ+YWNDEBFIqBg==\\\",\\\"templateURL\\\":\\\"http:\\/\\/cloudprint.cainiao.com\\/template\\/standard\\/4802048\\\"}\",\"waybill_code\":\"75163074465093\"}]},\"request_id\":\"45mfxf7g8oy7\"}}","errorCode":"","headerContent":{"Transfer-Encoding":"chunked","":"HTTP/1.1 200 OK","Location-Host":"top011010242240.na61","Server":"Tengine","Content-Encoding":"gzip","Connection":"close","top-bodylength":"1331","Vary":"Accept-Encoding","Application-Host":"11.10.242.240","Date":"Sat, 20 Jul 2019 07:24:57 GMT","Content-Type":"text/javascript;charset=UTF-8"},"modules":[{"objectId":"1","printData":"{\"data\":{\"_dataFrom\":\"waybill\",\"adsInfo\":{\"bannerUrl\":\"http://cdn-cloudprint.cainiao.com/waybill-print/cloudprint-imgs/c6fc411b400f4471a4d28e0a4a18c40b.jpg\"},\"cpCode\":\"ZTO\",\"needEncrypt\":false,\"packageInfo\":{\"items\":[{\"count\":1,\"name\":\"�������콢�꡿�����и��̷���Ʒ ������800g��װ �����������̷�\"}],\"volume\":0,\"weight\":0},\"parent\":false,\"recipient\":{\"address\":{\"city\":\"������\",\"detail\":\"�㰲���ڽֵ��㻪��С��3��¥208\",\"district\":\"������\",\"province\":\"����\"},\"mobile\":\"15810104492\",\"name\":\"¬��˳\"},\"routingInfo\":{\"consolidation\":{\"name\":\"����\"},\"origin\":{\"code\":\"02293\",\"name\":\"����г���\"},\"receiveBranch\":{\"code\":\"01090\"},\"routeCode\":\"22-01 13\",\"sortation\":{\"name\":\"800-\"},\"startCenter\":{},\"terminalCenter\":{}},\"sender\":{\"address\":{\"city\":\"���\",\"detail\":\"�����Ͳ�ҵ԰������·Ӻ�¿Ƽ�Ժ��\",\"district\":\"������\",\"province\":\"�����\"},\"mobile\":\"18616950914\",\"name\":\"������\"},\"shippingOption\":{\"code\":\"STANDARD_EXPRESS\",\"title\":\"��׼���\"},\"waybillCode\":\"75163074465093\"},\"signature\":\"MD:8wHe5ZI7tZ+YWNDEBFIqBg==\",\"templateURL\":\"http://cloudprint.cainiao.com/template/standard/4802048\"}","waybillCode":"75163074465093"}],"msg":"","params":{"param_waybill_cloud_print_apply_new_request":"{\"cp_code\":\"ZTO\",\"sender\":{\"address\":{\"city\":\"���\",\"detail\":\"�����Ͳ�ҵ԰������·Ӻ�¿Ƽ�Ժ��\",\"district\":\"������\",\"province\":\"�����\"},\"mobile\":\"18616950914\",\"name\":\"������\"},\"trade_order_info_dtos\":[{\"object_id\":\"1\",\"order_info\":{\"order_channels_type\":\"TM\",\"trade_order_list\":[\"545400098176560866\"]},\"package_info\":{\"items\":[{\"count\":1,\"name\":\"�������콢�꡿�����и��̷���Ʒ ������800g��װ �����������̷�\"}]},\"recipient\":{\"address\":{\"city\":\"������\",\"detail\":\"�㰲���ڽֵ��㻪��С��3��¥208\",\"district\":\"������\",\"province\":\"����\"},\"mobile\":\"15810104492\",\"name\":\"¬��˳\"},\"template_url\":\"http:\\/\\/cloudprint.cainiao.com\\/template\\/standard\\/${4802048}\",\"user_id\":72356451}]}"},"requestId":"45mfxf7g8oy7","requestUrl":"http://gw.api.taobao.com/router/rest?param_waybill_cloud_print_apply_new_request={"cp_code":"ZTO","sender":{"address":{"city":"���","detail":"�����Ͳ�ҵ԰������·Ӻ�¿Ƽ�Ժ��","district":"������","province":"�����"},"mobile":"18616950914","name":"������"},"trade_order_info_dtos":[{"object_id":"1","order_info":{"order_channels_type":"TM","trade_order_list":["545400098176560866"]},"package_info":{"items":[{"count":1,"name":"�������콢�꡿�����и��̷���Ʒ+������800g��װ+�����������̷�"}]},"recipient":{"address":{"city":"������","detail":"�㰲���ڽֵ��㻪��С��3��¥208","district":"������","province":"����"},"mobile":"15810104492","name":"¬��˳"},"template_url":"http:\/\/cloudprint.cainiao.com\/template\/standard\/${4802048}","user_id":72356451}]}&app_key=27536575&method=cainiao.waybill.ii.get&v=2.0&sign=736A450E5D76CDB7C42F912FB6D14CF48700EC280777F7BEBD346429562C8A58&timestamp=2019-07-20+15:24:57&partner_id=top-sdk-java-20190613&session=6101027bff53a0fc1df75f0df20659f96501c785a851da42843305610&format=json&sign_method=hmac-sha256","subCode":"","subMsg":"","success":true}

//		JSONObject  rsp=JSONObject.parseObject(taobao);
//		System.out.println(rsp.containsKey("error_response"));
//		if (rsp.containsKey("error_response")) {
//			System.err.println(rsp.getJSONObject("error_response").getString("sub_msg"));
//			return rsp.getJSONObject("error_response").getString("sub_msg");
//		}

		for (com.taobao.api.response.CainiaoWaybillIiGetResponse.WaybillCloudPrintResponse wcpr : rsp.getModules()) {

			JSONObject printData = JSON.parseObject(wcpr.getPrintData());

			logisticsTab.setExpressCode(printData.getJSONObject("data").getString("waybillCode"));// ���ص��浥��

//			logisticsTab.setBigShotCode(node.get("bigShotCode").asText());//null
			logisticsTab.setBigShotName(printData.getJSONObject("data").getJSONObject("routingInfo")
					.getJSONObject("sortation").getString("name"));// �����ջ���ַ���ش�ͷ����Ϣ
			logisticsTab.setGatherCenterCode(printData.getJSONObject("data").getJSONObject("routingInfo")
					.getJSONObject("consolidation").getString("name"));// �����ش���
			logisticsTab.setGatherCenterName(printData.getJSONObject("data").getJSONObject("routingInfo")
					.getJSONObject("consolidation").getString("code"));// ����������
//			logisticsTab.setSecondSectionCode(node.get("secondSectionCode").asText());//������
			logisticsTab.setThirdSectionCode(
					printData.getJSONObject("data").getJSONObject("routingInfo").getString("routeCode"));// ������
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("��ݹ�˾��"+expressCorp.getExpressNm()+"����ݵ��ţ�"+printData.getJSONObject("data").getString("waybillCode"));
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(14);// 14ȡ�����浥
			logRecord.setTypeName("ȡ�����浥");
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

//		ȡ�������浥 cainiao.waybill.ii.cancel
		CainiaoWaybillIiCancelRequest req = new CainiaoWaybillIiCancelRequest();
		req.setCpCode(ecExpress.getProviderCode());// String true EMS CP��ݹ�˾����
		req.setWaybillCode(logisticsTab.getExpressCode());// String true 1100222969702 �����浥����

		// ת��
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("path", CainiaoWaybillIiCancelRequest.class.getName());
		maps.put("taobaoObject", JSON.toJSONString(req));

		String taobao = ECHelper.getTB("", storeSettings, maps);
		CainiaoWaybillIiCancelResponse rsp = JSONObject.parseObject(taobao, CainiaoWaybillIiCancelResponse.class);

		System.out.println(rsp.getBody());

		if (!rsp.isSuccess()) {
			map.put("fallcount", map.get("fallcount") + 1);
			System.err.println(map.get("fallcount"));
			//���ʧ��
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ȡ���浥ʧ��,"+rsp.getSubMsg());
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���ⵥ
			logRecord.setTypeName("ȡ���浥");
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
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ȡ���浥�ɹ�");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���ⵥ
			logRecord.setTypeName("ȡ���浥");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
		} else {
			//���ʧ��
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ȡ���浥ʧ��");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���ⵥ
			logRecord.setTypeName("ȡ���浥");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
			map.put("fallcount", map.get("fallcount") + 1);
		}
		return "�ɹ�ȡ��";

	}

	/**
	 * id����list
	 * 
	 * @param id id(����Ѷ��ŷָ�)
	 * @return List����
	 */
	public List<String> getList(String id) {
		List<String> list = new ArrayList<String>();
		String[] str = id.split(",");
		for (int i = 0; i < str.length; i++) {
			//System.out.println(str[i] + "=====impl �� str[" + i + "]");
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
			resp = BaseJson.returnRespList("ec/logisticsTab/selectList", true, "��ҳ��ѯ�ɹ���", selectList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public String print(String orderNum, String accNum) {
		// TODO Auto-generated method stub
		// ����������Ĵ�ӡ״̬
		String resp = "";
		String[] nums = orderNum.split(",");
		// List<String> resultList= new ArrayList<>(Arrays.asList(nums));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			logisticsTab.setIsPrint(1);// �����Ѵ�ӡ
			logisticsTab.setPrintTime(sdf.format(new Date()));
			logisticsTabDao.update(logisticsTab);

			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(accNum);
			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(15);// 15��ӡ��ݵ�
			logRecord.setTypeName("��ӡ��ݵ�");
			logRecord.setOperatOrder(logisticsTab.getEcOrderId());
			logRecordDao.insert(logRecord);
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/print", true, "��������´�ӡ״̬���", null);
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
		String message = "�ƴ�ӡ��Ϣ";
		String[] nums = logisticsNum.split(",");
		List<JSONObject> list = new ArrayList<>();
		MisUser misUser = misUserDao.select(accNum);
		for (int i = 0; i < nums.length; i++) {
			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Ի�ȡ�����浥
				continue;
			}
			// ����������ĵ��̶����Ż�ȡ���̶�����Ϣ
			//PlatOrder platOrder = platOrderDao.selectByEcOrderId(logisticsTab.getEcOrderId());
			
			PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
			// ���ݶ����ĵ���ID��ȡ���̵���
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// ���ݵ��̵�����ȡ����ƽ̨ID
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
			// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
			StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
			// ��ȡ��ݹ�˾������Ϣ
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			// ��ȡƽ̨���õĵ����浥��ݹ�˾��Ϣ
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
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݹ�˾������Ӧ�����浥�˻���Ϣ�ظ�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				
				continue;
			}
			if(ecExpress==null) {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ݹ�˾�������ô�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
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
					message = "�������������쳣";
				}

			} else if (storeRecord2.getEcId().equals("PDD")) {
				try {
					JSONObject string = PDDECExpressOrderYun(ecExpress, expressCorp, storeSettings, ecExpressSettings,
							logisticsTab, platOrder, storeRecord2,misUser);
					list.add(string);
				} catch (Exception e) {
					e.printStackTrace();
					message = "�������������쳣";
				}
			} else {
				message = "�в������ƴ�ӡ�ĵ���";

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
				resp = BaseJson.returnRespObj("ec/logisticsTab/achieveECExpressCloudPrint", false, "�޴�ӡ��Ϣ", null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resp;

	}
	/**
	 * �����������浥
	 * @return
	 */
	public String JDcancelExpress(LogisticsTab logisticsTab,EcExpress ecExpress,StoreSettings storeSettings,Map<String, Integer> map,MisUser misUser) {
		
		String result = "";
		ObjectNode objectNode;
		try {
			objectNode = JacksonUtil.getObjectNode("");
			objectNode.put("waybillCode", logisticsTab.getExpressCode());// �˵���
			objectNode.put("providerId", ecExpress.getProviderId());// ������ID
			objectNode.put("providerCode", ecExpress.getProviderCode());// �����̱���
			String jdRespStr = ECHelper.getJD("jingdong.ldop.alpha.waybill.api.unbind", storeSettings,
					objectNode.toString());
			ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
			System.out.println("�������浥���ؽ����"+jdRespStr);
			if(jdRespJson.get("jingdong_ldop_alpha_waybill_api_unbind_responce").get("resultInfo").get("statusCode").toString().equals("0")) {
				//�������浥�ɹ�
				logisticsTab.setExpressCode(null);//����浥��
				logisticsTab.setBigShotCode(null);
				logisticsTab.setBigShotName(null);
				logisticsTab.setGatherCenterCode(null);
				logisticsTab.setGatherCenterName(null);
				logisticsTab.setSecondSectionCode(null);
				logisticsTab.setThirdSectionCode(null);
				logisticsTabDao.update(logisticsTab);
				
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getAccNum());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ȡ���浥�ɹ�");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���ⵥ
				logRecord.setTypeName("ȡ���浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				
				map.put("successcount", map.get("successcount") + 1);
				
			}else {
				//���ʧ��
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getAccNum());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ȡ���浥ʧ��,"+jdRespJson.get("jingdong_ldop_alpha_waybill_api_unbind_responce").get("resultInfo").get("statusMessage").toString());
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���ⵥ
				logRecord.setTypeName("ȡ���浥");
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
	 * ǿ�Ʒ���
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

			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Խ��з�������
				fallcount++;
				continue;
			}
			
			if(logisticsTab.getIsPick()==0) {
				//���������״̬Ϊδ�����������ִ�з���
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ǿ�Ʒ���ʧ�ܣ�������δ��ʼ���������ִ�м�����");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(18);// 8����
				logRecord.setTypeName("ǿ�Ʒ���");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(StringUtils.isEmpty(logisticsTab.getExpressCode())) {
				//�����ݵ���Ϊ�գ�������ִ�з���
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ǿ�Ʒ���ʧ�ܣ���ݵ���Ϊ��");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(18);// 8����
				logRecord.setTypeName("ǿ�Ʒ���");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			if (logisticsTab.getIsShip()==0) {//״̬Ϊ�ѷ����Ĳ����ٴη���
				if(StringUtils.isNotEmpty(logisticsTab.getExpressCode())) {
					
				
				try {
					logisticsTab.setIsShip(1);// ����������״̬Ϊ�ѷ���
					logisticsTab.setIsBackPlatform(0);// ����δ�ش�ƽ̨
					logisticsTab.setShipDate(sdf.format(new Date()));// ���÷���ʱ��
					
					logisticsTabDao.update(logisticsTab);// ������������Ϣ
					PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
					platOrder.setIsShip(1);
					platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
					platOrder.setShipTime(logisticsTab.getShipDate());
					platOrderDao.update(platOrder);

					message = "ǿ�Ʒ����ɹ�";
					
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(18);// 18ǿ�Ʒ���
					logRecord.setTypeName("ǿ�Ʒ���");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					successcount++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					message = "ǿ�Ʒ����쳣";
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
					"ǿ�Ʒ����ɹ������γɹ�����" + successcount + "����ʧ��" + fallcount + "��", null);
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

			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Խ���ȡ����������
				fallcount++;
				continue;
			}
			
			if (logisticsTab.getIsShip()==1) {//״̬Ϊ�������Ĳ���ȡ������
				PlatOrder platOrder = platOrderDao.select(logisticsTab.getOrderId());
				try {
					logisticsTab.setIsShip(0);// ����������״̬Ϊ�ѷ���
					logisticsTab.setIsBackPlatform(0);// ����δ�ش�ƽ̨
					logisticsTab.setShipDate(null);// ���÷���ʱ��
					logisticsTab.setExpressCode(null);
					logisticsTabDao.update(logisticsTab);// ������������Ϣ
					
					platOrder.setIsShip(0);
					platOrder.setExpressNo(logisticsTab.getExpressCode());//���¶��������Ŀ�ݵ���
					platOrder.setShipTime(null);
					platOrderDao.update(platOrder);

					message = "ȡ�������ɹ�";
					
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(19);// 19ȡ������
					logRecord.setTypeName("ȡ������");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					
					successcount++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					message = "ȡ�������쳣";
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent(message);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(19);// 19ȡ������
					logRecord.setTypeName("ȡ������");
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
					"ȡ�������ɹ������γɹ�ȡ��" + successcount + "����ʧ��" + fallcount + "��", null);
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
			message = "��ѯ�ɹ�";
			isSuccess = true;
			resp = BaseJson.returnRespObjList("ec/logisticsTab/exportList", true, "", null,
					LogisticsTabExports);
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
		return resp;
	}

	//�����޸Ŀ�ݹ�˾����
	@Override
	public String batchUpdate(String accNum, String ordrNum, String expressEncd) {
		// TODO Auto-generated method stub
		String resp ="";
		try {
			List<String> ordrNums = Arrays.asList(ordrNum.split(","));
			logisticsTabDao.updateExpress(ordrNums, expressEncd);
			platOrderDao.updateExpress(ordrNums, expressEncd);
			resp = BaseJson.returnRespObj("ec/logisticsTab/batchUpdate", true, "�����޸ĳɹ�", null);
		} catch (Exception e) {
			// TODO: handle exception
			try {
				resp = BaseJson.returnRespObj("ec/logisticsTab/batchUpdate", true, "�����޸��쳣��������", null);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return resp;
	}

	//�޸Ŀ�ݵ���
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
				//message = "������������";
				fallCount++;
			} else {
				if (logisticsTab.getIsShip() == 1) {
					//message = "�������ѷ����������޸Ŀ�ݵ���";
					fallCount++;
				} else {
					logisticsTabDao.updateExpressCode(ordrNums[i], expressCodes[i]);
					//message = "�޸ĳɹ�";
					successCount++;
				}
			} 
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/updateExpressCode", true, "�޸Ŀ�ݵ�����ɣ����γɹ��޸�"+successCount+"����ʧ��"+fallCount+"��", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
	
	//�޸ļ��״̬
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
			//����Ϊ������״̬
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
						logRecord.setOperatContent("��ʼ�������ǳɹ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(23);// 
						logRecord.setTypeName("��ʼ���");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} else {
						fallCount++;
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("��ʼ��������ʧ��");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(23);//
						logRecord.setTypeName("��ʼ���");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} 
				}
			
			}
			
		}else {
			//����Ϊδ���״̬
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
						logRecord.setOperatContent("�����������ǳ��سɹ�");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(24);// 
						logRecord.setTypeName("�������");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} else {
						fallCount++;
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("�����������ǳ���ʧ��");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(24);//
						logRecord.setTypeName("�������");
						logRecord.setOperatOrder(logisticsTab.getEcOrderId());
						logRecordDao.insert(logRecord);
					} 
				}
			
			}
		}
		
		
		try {
			if (type.equals("1")) {
				resp = BaseJson.returnRespObj("ec/logisticsTab/updatePick", true,
						"�����ɣ����γɹ����" + successCount + "����ʧ��" + fallCount + "��", null);
			}else {
				resp = BaseJson.returnRespObj("ec/logisticsTab/updatePick", true, 
						"ȡ�������ɣ����γɹ�ȡ�����"+successCount+"����ʧ��"+fallCount+"��", null);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return resp;
	}
	//�����ݵ���
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
			//����ָ�����ļ�����������Excel�Ӷ�����Workbook����
			Workbook wb0 = new HSSFWorkbook(fileIn);
			//��ȡExcel�ĵ��еĵ�һ����
			Sheet sht0 = wb0.getSheetAt(0);
			//��õ�ǰsheet�Ŀ�ʼ��
			int firstRowNum = sht0.getFirstRowNum();
			//��ȡ�ļ������һ��
			int lastRowNum = sht0.getLastRowNum();
			//���������ֶκ��±�ӳ��
			SetColIndex(sht0.getRow(firstRowNum));
			//��Sheet�е�ÿһ�н��е���
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				j++;
				Row r = sht0.getRow(i);
				//�����ǰ�е��кţ���0��ʼ��δ�ﵽ2�������У������ѭ��
				if(r.getRowNum()<1){
				continue;
				}
				
				LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(GetCellData(r,"������")));
				if (logisticsTab == null) {
					//message = "������������";
					fallCount++;
				} else {
					if (logisticsTab.getIsShip() == 1) {
						//message = "�������ѷ����������޸Ŀ�ݵ���";
						fallCount++;
					} else {
						logisticsTabDao.updateExpressCode(GetCellData(r,"������"), GetCellData(r,"��ݵ���"));
						//message = "�޸ĳɹ�";
						successCount++;
					}
				} 
			} 
		} catch (Exception e) {
			// TODO: handle exception
			message="�����ݵ����쳣�����顣����ɹ�"+successCount+"����ʧ��"+fallCount+"��";
		}
		if(message.length()==0) {
			message="�����ݵ�����ɣ�����ɹ�"+successCount+"����ʧ��"+fallCount+"��";
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
	 * ������ȡ��ݹ�˾���� ����
	 * @param storeSettings
	 * @param companyName
	 */
	public void loadExpressCode() {
		try {
		StoreSettings storeSettings = storeSettingsDao.select("0042");
		String jdRespStr = ECHelper.getKaola("kaola.logistics.companies.get", storeSettings, "");
		System.out.println("������Ϣ��"+jdRespStr);
		
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//��������µ�
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
			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Ի�ȡ�����浥
				fallcount++;
				continue;
			}
			if(logisticsTab.getIsShip()==1) {
				//�ѷ�������ȡ�����浥
				fallcount++;
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("�������ѷ���������ȡ�����浥");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(StringUtils.isNotEmpty(logisticsTab.getExpressCode())) {
				//���п�ݵ��Ų���ȡ�����浥
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("���������п�ݵ��ţ������ظ�ȡ�����浥");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			// ��ȡ��ݹ�˾������Ϣ
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			if(expressCorp==null) {
				//��ݹ�˾������
				fallcount++;
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("��Ӧ��ݹ�˾����������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			if(!expressCorp.getCompanyCode().equals("JDWL")||StringUtils.isEmpty(expressCorp.getCompanyCode())) {
				//��ݹ�˾���Ǿ������
				fallcount++;
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("��Ӧ��ݹ�˾�Ǿ������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(logisticsTab.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			String orderId = logisticsTab.getOrderId();
			// ����������ĵ��̶����Ż�ȡ���̶�����Ϣ
			PlatOrder platOrder = platOrderDao.select(orderId);
			// ���ݶ����ĵ���ID��ȡ���̵���
			StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
			// ����ݹ�˾��׼������Ϊƽ̨�����ѯ��Ӧƽ̨�ĵ����浥���̱��
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(expressCorp.getCompanyCode());
			// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				//storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ������õ�ǰ��ݹ�˾��"+expressCorp.getCompanyCode()+"��Ӧ�ĵ�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
				continue;
			}
			
			
			if (expressCorp.getCompanyCode().equals("JDWL")) {
				try {
				//�������
				
				// ���þ�����ݽӿڻ�ȡ�˵���
				//Ŀǰһ�λ�ȡһ����ݵ���
				String respJson = loadJdwlExpressCode(storeSettings, 1);
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(respJson);
				if(jdRespJson.has("error_response")) {
					//�����쳣ʱ
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ�ʧ�ܣ�"+jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
					/*{
					"jingdong_etms_waybillcode_get_responce": {
					"code": "0",
					"resultInfo": {
					"message": "�ɹ�",
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
					//ȡ������ݵ���ʧ��
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ�ʧ�ܣ�"+jdRespJson.get("jingdong_etms_waybillcode_get_responce").get("resultInfo").get("message").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//�ж��ջ���ַ�Ƿ����㾩�������������
				respJson = checkIsJdwl(storeSettings, logisticsTab, platOrder, 1,expressCorp,storeRecord);//����ҵ������ѡ1 ��ͨ
				jdRespJson = JacksonUtil.getObjectNode(respJson);
				if(jdRespJson.has("error_response")) {
					//�����쳣ʱ
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ�ʧ�ܣ�"+jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				if(jdRespJson.get("jingdong_etms_range_check_responce").get("resultInfo").get("rcode").asText().equals("100")) {
					//"isHideName": 0, 	�Ƿ���������
					//"agingName": "��ʱЧ",  	ʱЧ����
					//"targetSortCenterId": 364605, Ŀ�ķּ����ı���
					//"promiseTimeTypeDownGrade": true, ʱЧ��Ʒ�Ƿ񽵼�
					//"sourcetSortCenterName": "�����ⵥ�ּ�����", ʼ���ּ���������
					//"rmessage": "���Ծ���",  	�������
					//"rcode": 100,  	�������
					//"promiseTimeType": 1,  ��Ʒ���� 1-�ػ��ͣ�2-�ؿ��ͣ�4-��˲�ͳǼʣ�5-ͬ�ǵ��մ6-�ؿ�γ���7-΢С����8-����ר�ͣ�16-�����ٴ17-���ʻݴ21-�ػ�С��
					//"sourcetSortCenterId": 151678,   	ʼ���ּ����ı���
					//"road": "0",  	·��
					//"originalTabletrolleyCode": "110-10",   ʼ��������
					//"transType": 0,   �������ͣ�0��½�ˣ�1�����գ���
					//"destinationCrossCode": "7",   Ŀ��������
					//"siteId": 289191,  Ŀ��վ�����
					//"originalCrossCode": "30", 	ʼ�����ں�
					//"destinationTabletrolleyCode": "M19",   	Ŀ��������
					//"isHideContractNumbers": 1,   	�Ƿ�������ϵ��ʽ
					//"targetSortCenterName": "����ͨ�ݷּ�����",   	Ŀ�ķּ���������
					//"orderId": "���Ծ������0001",    	������
					//"siteName": "*�����Ž��Ӫҵ��",    		Ŀ��վ������
					//"aging": 0  		ʱЧ
						/*
						 * private String bigShotCode;//��ͷ�ʱ���     sourcetSortCenterName
						 *  private String bigShotName;//��ͷ������   originalCrossCode-originalTabletrolleyCode
						 *  privateString gatherCenterName;//���������� targetSortCenterName
						 *  private String gatherCenterCode;//�����ش��� destinationCrossCode-destinationTabletrolleyCode
						 * private String branchName;//Ŀ�ĵ���������  siteName
						 *  private String branchCode;//Ŀ�ĵ�������� road
						 * private String secondSectionCode;//������ agingName
						 * private String thirdSectionCode;//������ orderId
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
					//�����쳣ʱ
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ�ʧ�ܣ�"+jdRespJson.get("jingdong_etms_range_check_responce").get("resultInfo").get("rmessage").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//������������ӵ� ֻ���ж��Ƿ���Ľ��=100 ���Ծ�����ܵ��˽ӿ�
				respJson = JdwlOrder(storeSettings, logisticsTab, platOrder, 1, storeRecord, expressCorp);
				jdRespJson = JacksonUtil.getObjectNode(respJson);
				if(jdRespJson.has("error_response")) {
					//�����쳣ʱ
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ�ʧ�ܣ�"+jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					fallcount++;
					continue;
				}
				//System.out.println(jdRespJson);
				if(jdRespJson.get("jingdong_etms_waybill_send_responce").get("resultInfo").get("code").asText().equals("100")) {
					//�µ��ɹ�
					logisticsTabDao.update(logisticsTab);
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ��ɹ�����ݵ��ţ�"+logisticsTab.getExpressCode());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					successcount++;
				}else {
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("�µ�ʧ�ܣ�"+jdRespJson.get("jingdong_etms_waybill_send_response").get("resultInfo").get("message"));
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(14);// 14ȡ�����浥
					logRecord.setTypeName("ȡ�����浥");
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
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ǰ��ݹ�˾��֧��ʹ�ô˹���");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(14);// 14ȡ�����浥
				logRecord.setTypeName("ȡ�����浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				fallcount++;
			}
		}
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/achieveJDWLOrder", true,
					"��ȡ��ݵ��ųɹ������γɹ���ȡ" + successcount + "����ʧ��" + fallcount + "��", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
		
		
	}
	/**
	 * ȡ����ݵ��� ��ֱӪ�Ϳ�ݹ�˾
	 * @param ordrNum ���ŷָ�������������
	 * @param accNum  �û��˺�
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
			// ����������id��ȡ��������Ϣ
			LogisticsTab logisticsTab = logisticsTabDao.select(Integer.parseInt(nums[i]));
			if (logisticsTab.getDeliverSelf() == 0) {
				// ��������������Է����Ĳ����Խ��в���
				fallcount++;
				continue;
			}
			// ����������ĵ��̶����Ż�ȡ���̶�����Ϣ
			PlatOrder platOrder = platOrderDao.selectByEcOrderId(logisticsTab.getEcOrderId());
			// ��ȡ��ݹ�˾������Ϣ
			ExpressCorp expressCorp = expressCorpDao.selectExpressCorp(logisticsTab.getExpressEncd());
			//ֱӪ�Ϳ�ݹ�˾
			switch (expressCorp.getCompanyCode()) {
			case "JDWL":
				//�������
				
				if(cancelJDWLOrder(platOrder, misUser, logisticsTab, expressCorp)) {
					successcount++;//�ɹ�
				}else {
					fallcount++;//ʧ��
				}
				break;

			default:
				fallcount++;
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ���ǰ��ݹ�˾�ݲ�֧��ʹ�ô˹���");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ���浥
				logRecord.setTypeName("ȡ���浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				break;
			}

		}
		
		try {
			resp = BaseJson.returnRespObj("ec/logisticsTab/cancelExpressOrder", true,
					"��ȡ��ݵ��ɹ������γɹ�ȡ��" + successcount + "����ʧ��" + fallcount + "��", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
	/**
	 * �������ȡ�������˵�
	 * @param platOrder ���̶���
	 * @param misUser �û�
	 * @param logisticsTab  ������
	 * @param expressCorp  ��ݹ�˾
	 * @return true �ɹ�  false ʧ��
	 */
	public boolean cancelJDWLOrder(PlatOrder platOrder,MisUser misUser,LogisticsTab logisticsTab,ExpressCorp expressCorp) {
		
		
		// ���ݶ����ĵ���ID��ȡ���̵���
		//StoreRecord storeRecord = storeRecordDao.select(platOrder.getStoreId());
		//EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(storeRecord.getEcId());
		// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
		//StoreSettings storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
		//StoreSettings storeSettings = new StoreSettings();
		
		if(StringUtils.isEmpty(logisticsTab.getExpressCode())) {
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ʧ�ܣ�����������δ�����ݵ��ţ�����ִ��ȡ������");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���浥
			logRecord.setTypeName("ȡ���浥");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			return false;
		}else if (logisticsTab.getIsShip()==1) {
			// ��־��¼
			LogRecord logRecord = new LogRecord();
			logRecord.setOperatId(misUser.getAccNum());

			if (misUser != null) {
				logRecord.setOperatName(misUser.getUserName());
			}
			logRecord.setOperatContent("ʧ�ܣ����������ѷ���������ִ��ȡ����������");
			logRecord.setOperatTime(sdf.format(new Date()));
			logRecord.setOperatType(17);// 17ȡ���浥
			logRecord.setTypeName("ȡ���浥");
			logRecord.setOperatOrder(platOrder.getEcOrderId());
			logRecordDao.insert(logRecord);
			return false;
		}else {
			// ����ݹ�˾��׼������Ϊƽ̨�����ѯ��Ӧƽ̨�ĵ����浥���̱��
			EcExpressSettings ecExpressSettings = ecExpressSettingsDao.selectByPlatId(expressCorp.getCompanyCode());
			// ��ȡ��Ӧƽ̨����ͨ�����浥�ĵ�������
			StoreSettings storeSettings = new StoreSettings();
			if (ecExpressSettings!=null) {
				storeSettings = storeSettingsDao.select(ecExpressSettings.getStoreId());
				//storeRecord=storeRecordDao.select(ecExpressSettings.getStoreId());
			}else {
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getUserName());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ʧ�ܣ������õ�ǰ��ݹ�˾��"+expressCorp.getCompanyCode()+"��Ӧ�ĵ�������");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ��
				logRecord.setTypeName("ȡ���浥");
				logRecord.setOperatOrder(platOrder.getEcOrderId());
				logRecordDao.insert(logRecord);
				return false;
			}
			//���þ������ȡ���ӿ�
			
			try {
				String resp = cancelJdwl(storeSettings, logisticsTab, "����ȡ��");
				ObjectNode jdRespJson = JacksonUtil.getObjectNode(resp);
				if (jdRespJson.has("error_response")) {
					//�����쳣ʱ
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(misUser.getUserName());

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("ȡ��ʧ�ܣ�" + jdRespJson.get("error_response").get("zh_desc").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(17);// 17ȡ��
					logRecord.setTypeName("ȡ���浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					return false;
				}
				if(jdRespJson.get("jingdong_ldop_receive_order_intercept_responce").get("resultInfo").get("stateCode").asText().equals("100")) {
					//ȡ���ɹ�
					logisticsTab.setExpressCode("");//��ն�Ӧ��ݵ���
					logisticsTab.setBigShotCode(null);
					logisticsTab.setBigShotName(null);
					logisticsTab.setGatherCenterName(null);
					logisticsTab.setGatherCenterCode(null);
					logisticsTab.setBranchName(null);
					logisticsTab.setBranchCode(null);
					logisticsTab.setSecondSectionCode(null);
					logisticsTab.setThirdSectionCode(null);
					logisticsTabDao.update(logisticsTab);
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(misUser.getUserName());

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("ȡ���ɹ���ԭ��ݵ��ţ�"+logisticsTab.getExpressCode());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(17);// 17ȡ��
					logRecord.setTypeName("ȡ���浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					return true;
				}else {
					// ��־��¼
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(misUser.getUserName());

					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("ȡ��ʧ�ܣ�"+jdRespJson.get("jingdong_ldop_receive_order_intercept_responce").get("resultInfo").get("stateMessage").asText());
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(17);// 17ȡ��
					logRecord.setTypeName("ȡ���浥");
					logRecord.setOperatOrder(platOrder.getEcOrderId());
					logRecordDao.insert(logRecord);
					return false;
				}
				
			
			} catch (Exception e) {
				// TODO: handle exception
				//�����쳣ʱ
				// ��־��¼
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(misUser.getUserName());

				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("ȡ��ʧ�ܣ����þ�����ݽӿ��쳣������ϵ����Ա");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(17);// 17ȡ��
				logRecord.setTypeName("ȡ���浥");
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