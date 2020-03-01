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
						//此自主售后单已审核，不能重复审核
						aftermarket.setAuditHint("此自主售后单已审核，不能重复审核");
						aftermarketDao.update(aftermarket);
						fallcount++;
						continue;
					}*/
					if(aftermarket.getServiceStatusName().equals("取消")||aftermarket.getServiceStatusName().equals("审核关闭")
							||aftermarket.getServiceStatusName().equals("待审核")) {
						//当自助售后单状态为已取消/待审核/审核关闭时，不能审核生成退款单
						/*aftermarket.setAuditHint("此自主售后单已取消或待审核或审核关闭，不能生成退款单");
						aftermarketDao.update(aftermarket);*/
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}
					if(platOrderDao.checkExsits(aftermarket.getOrderId(), aftermarket.getStoreId())==0) {
						/*aftermarket.setAuditHint("原订单已被删除，无法审核");
						aftermarketDao.update(aftermarket);*/
						//删除自主售后单，方便下次下载时能正确下载下来
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}
					/*if(platOrderDao.selectNoAuditCountByOrderIdAndStoreId(aftermarket.getStoreId(), aftermarket.getOrderId())!=0) {
						aftermarket.setAuditHint("原订单未审核，请先审核原订单");
						aftermarketDao.update(aftermarket);
						
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载，订单："+aftermarket.getOrderId()+"原订单未审核，本次下载失败。");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(aftermarket.getOrderId());
						logRecordDao.insert(logRecord);
						//删除自主售后单，方便下次下载时能正确下载下来
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}*/
					/*if(!aftermarket.getProcessResultName().equals("退货")||!aftermarket.getProcessResultName().equals("换新")) {
						//当处理结果不是退货或者换新时，不处理，不能生成退款单
						aftermarket.setAuditHint("处理结果不是退货或者换新，不能审核");
						aftermarketDao.update(aftermarket);
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载，订单："+aftermarket.getOrderId()+"处理结果不是退货或者换新，本次下载失败。");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(aftermarket.getOrderId());
						logRecordDao.insert(logRecord);
						//删除自主售后单，方便下次下载时能正确下载下来
						aftermarketDao.delete(aftermarket);
						fallcount++;
						continue;
					}*/
					
					Map<String,String> map=new HashMap<>();
					//System.out.println(aftermarket.getOrderId());
					map.put("ecOrderId", aftermarket.getOrderId());
					map.put("goodSku", aftermarket.getSkuId());
					//用自主售后单中的订单号和skuid去查询原单的购买数量以及实付金额
					List<PlatOrders> platOrders = platOrderDao.selectNumAndPayMoneyByOrderAndSKu(map);
					if (platOrders.size()==0) {
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载，订单："+aftermarket.getOrderId()+"未找到订单明细，本次下载失败。");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(aftermarket.getOrderId());
						logRecordDao.insert(logRecord);
						//删除自主售后单，方便下次下载时能正确下载下来
						aftermarketDao.delete(aftermarket);
						continue;
					}else {
						boolean flag = true;
						for (int i = 0; i < platOrders.size(); i++) {
							//循环判断原订单是否存在未匹配到存货档案的记录
							if(!StringUtils.isNotEmpty(platOrders.get(i).getInvId())) {
								flag=false;
								break;
							}
						}
						if(!flag) {
							// 日志记录
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							if (misUser != null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("退款单下载，订单："+aftermarket.getOrderId()+"存在未匹配到存货档案的记录，本次下载失败。");
							logRecord.setOperatTime(sdf.format(new Date()));
							logRecord.setOperatType(12);// 12退款下载
							logRecord.setTypeName("退款单下载");
							logRecord.setOperatOrder(aftermarket.getOrderId());
							logRecordDao.insert(logRecord);
							//删除自主售后单，方便下次下载时能正确下载下来
							aftermarketDao.delete(aftermarket);
							continue;
						}
					}
					
					
					RefundOrder refundOrder = new RefundOrder();
					
					String refId = getOrderNo.getSeqNo("tk", accNum);
					refundOrder.setRefId(refId);// 退款单编号
					refundOrder.setStoreId(aftermarket.getStoreId());// 店铺编号
					refundOrder.setEcRefId(String.valueOf(aftermarket.getServiceId()));//电商退款单号
					refundOrder.setOrderId(aftermarket.getOrderId());//电商平台订单号
					refundOrder.setEcOrderId(aftermarket.getOrderId());//电商平台订单号
					refundOrder.setSource(1);//设置退款单来源为京东自主售后
					refundOrder.setSourceNo(""+aftermarket.getServiceId());//设置来源单据号为自主售后的服务单号
					refundOrder.setOperatorId(misUser.getAccNum());
					refundOrder.setOperatorTime(date);
					
					refundOrder.setStoreName(storeRecord.getStoreName());//店铺名称
//				    private String storeName;//店铺名称
					refundOrder.setApplyDate(aftermarket.getApplyTime());// 申请日期
					refundOrder.setBuyerId(aftermarket.getCustomerPin());//买家会员号
					refundOrder.setDownTime(date);//设置下载时间为当前时间
					refundOrder.setOperator(misUser.getUserName());
					refundOrder.setIsAudit(0);
					refundOrder.setTreDate(aftermarket.getApproveTime());//退款单处理时间用平台的审核时间
					//refundOrder.setRefReason(aftermarket.get);
					//只处理处理结果为退货和换货的自主售后单
					//每次申请数量为1，指sku购买数量
					//退货数量需要用存货数量除sku购买数量
					int allReturnCount=0;
					BigDecimal allReturnMoney=aftermarket.getActualPayPrice();//总共需要退款的金额
					BigDecimal hasReturnMoney = BigDecimal.ZERO;//已退金额
					for (int i = 0; i < platOrders.size(); i++) {
						//直接取当前条明细生成退款单
						RefundOrders refundOrders = new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodSku(aftermarket.getSkuId());//商品sku
						refundOrders.setEcGoodId(platOrders.get(i).getGoodId());//设置店铺商品编码
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.get(i).getInvId());
						refundOrders.setCanRefNum(platOrders.get(i).getCanRefNum());//设置可退数量
						//refundOrders.setRefNum(platOrders.get(i).getCanRefNum());//设置退货数量
						refundOrders.setRefNum(platOrders.get(i).getInvNum()/platOrders.get(i).getGoodNum());//设置退货数量
						if(StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//设置保质期
						}
						refundOrders.setIsGift(platOrders.get(i).getIsGift());//是否赠品用原订单属性
						refundOrders.setPrdcDt(platOrders.get(i).getPrdcDt());//设置生产日期
						refundOrders.setInvldtnDt(platOrders.get(i).getInvldtnDt());//设置失效日期
						//设置可退金额processResult
						if (aftermarket.getProcessResult()==40) {//40是退货
							refundOrders.setCanRefMoney(platOrders.get(i).getCanRefMoney());
							if (i+1<platOrders.size()) {
								refundOrders.setRefMoney(platOrders.get(i).getPayMoney().multiply(new BigDecimal(1).divide(new BigDecimal(platOrders.get(i).getGoodNum()))));
								hasReturnMoney = hasReturnMoney.add(refundOrders.getRefMoney());
							}else {
								refundOrders.setRefMoney(allReturnMoney.subtract(hasReturnMoney));
							}
						}else {//23换货
							//换新时不需要退款
							refundOrders.setCanRefMoney(platOrders.get(i).getCanRefMoney());
							refundOrders.setRefMoney(new BigDecimal(0));
						}
						allReturnCount+=refundOrders.getRefNum();
						//allReturnMoney=allReturnMoney.add(refundOrders.getRefMoney());
						refundOrders.setGoodId(platOrders.get(i).getInvId());//设置存货编码
						
						refundOrders.setGoodName(invtyDoc.getInvtyNm());//设置存货名称
						refundOrders.setBatchNo(platOrders.get(i).getBatchNo());//设置退货批次
						refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//取对应店铺的默认退货仓
						refundOrdersList.add(refundOrders);//装入list
					}
					refundOrder.setRefStatus(0);//退款状态暂时先设置成0,不知道有啥用
					refundOrder.setAllRefNum(allReturnCount);
					refundOrder.setAllRefMoney(allReturnMoney);
					if(refundOrder.getAllRefNum()>0) {
						refundOrder.setIsRefund(1);//退货数量大于0时是否退货1
					}else {
						refundOrder.setIsRefund(0);
					}
					refundOrderList.add(refundOrder);
//				    private Integer isRefund;//是否退货
//				    private Integer allRefNum;//整单退货数量
//				    private BigDecimal allRefMoney;//整单退款金额
//				    private String refReason;//退款原因
//				    private String refExplain;//退款说明
//				    private Integer refStatus;//退款状态
//				    private String treDate;//处理日期
//				    private String operator;//操作员
//				    private String memo;//备注
					
				}
				if (refundOrderList.size()>0) {
					refundOrderDao.insertList(refundOrderList);
					refundOrdersDao.insertList(refundOrdersList);
					count = refundOrderList.size();
				}
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("退款单下载，本次成功下载京东店铺："+storeRecord.getStoreName()+",自主售后"+refundOrderList.size()+"条");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12退款下载
				logRecord.setTypeName("退款下载");
				logRecordDao.insert(logRecord);
			}else {
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("退款单下载，本次成功下载京东店铺："+storeRecord.getStoreName()+",自主售后0条");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12退款下载
				logRecord.setTypeName("退款下载");
				logRecordDao.insert(logRecord);
			}
			
			/*message = "成功审核" + count + "条自主售后单,失败"+fallcount+"条！";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/aftermarket/audit", isSuccess, message, null);*/
		} catch (Exception e) {
			logger.error("URL:ec/aftermarket/audit 异常说明：",e);
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
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/aftermarket/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, aftermarkets);
		} catch (IOException e) {
			logger.error("URL:ec/aftermarket/queryList 异常说明：",e);
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
				
				download(storeSettings, storeRecord, 23, 1, 50,orderId,startDate,endDate);//下载处理结果为换新的
				//download(storeSettings, storeRecord, 40, 1, 50,orderId);//下载处理结果为退货的
				if (errMessage.length()==0) {
					if (aftermarketList.size() > 0) {
						aftermarketDao.insert(aftermarketList);
						count=audit(aftermarketList, accNum,storeId);
					} 
					errMessage="";
				}else {
					// 日志记录
					LogRecord logRecord = new LogRecord();
					logRecord.setOperatId(accNum);
					if (misUser != null) {
						logRecord.setOperatName(misUser.getUserName());
					}
					logRecord.setOperatContent("退款单下载，出现错误，错误店铺："+storeRecord.getStoreName()+",错误描述："+errMessage);
					logRecord.setOperatTime(sdf.format(new Date()));
					logRecord.setOperatType(12);// 12退款下载
					logRecord.setTypeName("退款单下载");
					logRecordDao.insert(logRecord);
				}
			}
			
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/aftermarket/download 异常说明：",e);
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
					message = "下载完成，本次成功下载" + aftermarketList.size() + "条自主售后单！";
				} else {
					aftermarketDao.insert(aftermarketList);
					isSuccess = true;
					message = "下载完成，本次成功下载" + aftermarketList.size() + "条自主售后单！";
				} 
				errMessage="";
			}else {
				isSuccess=false;
				message=errMessage;
			}
			}else {
				isSuccess=false;
				message="请选择京东平台的店铺下载";
			}
			resp = BaseJson.returnRespObj("ec/aftermarket/download", isSuccess, message, null);
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/aftermarket/download 异常说明：",e);
		}
		return resp;
	}
	private void download(StoreSettings storeSettings,StoreRecord storeRecord,int processResult, int pageNo, int pageSize,String orderId,String startDate,String endDate)
			throws IOException, NoSuchAlgorithmException {
		
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageNumber", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		jdReq.put("buId", storeSettings.getVenderId());
		jdReq.put("operatePin", storeRecord.getSellerId());//操作人账号
		jdReq.put("operateNick", storeRecord.getSellerId());//操作人姓名
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
		//System.out.println("请求数据："+jdReq.toString());
		String jdRespStr = ECHelper.getJD("jingdong.asc.query.list", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		//System.out.println("结果："+jdRespJson);
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
								//processResult==40时审核结果为退货    23为换新
								
								Aftermarket aftermarke1t = aftermarketDao.selectById(data.get("serviceId").asInt());
								if (aftermarketDao.selectById(data.get("serviceId").asInt()) == null) {//如果此服务单号不存在时，则下载保存
									Aftermarket aftermarket = new Aftermarket();
									aftermarket.setApplyId(data.get("applyId").asInt());// 申请单号
									if (data.get("serviceId") != null) {
										aftermarket.setServiceId(data.get("serviceId").asInt());// 服务单号
									}
									//System.out.println(sdf.format(new Date(new Long(data.get("applyTime").asText()))));
									if (data.get("applyTime") != null) {
										aftermarket.setApplyTime(
												sdf.format(new Date(new Long(data.get("applyTime").asText()))));// 申请时间
									}
									if (data.get("customerExpect") != null) {
										aftermarket.setCustomerExpect(data.get("customerExpect").asInt());// 客户期望
									}
									if (data.get("customerExpectName") != null) {
										aftermarket.setCustomerExpectName(data.get("customerExpectName").asText());// 客户期望名称
									}
									if (data.get("serviceStatus") != null) {
										aftermarket.setServiceStatus(data.get("serviceStatus").asInt());// 服务单状态
									}
									if (data.get("serviceStatusName") != null) {
										aftermarket.setServiceStatusName(data.get("serviceStatusName").asText());// 服务单状态名称
									}
									if (data.get("customerPin") != null) {
										aftermarket.setCustomerPin(data.get("customerPin").asText());// 客户账号
									}
									if (data.get("customerName") != null) {
										aftermarket.setCustomerName(data.get("customerName").asText());// 客户姓名
									}
									if (data.get("customerGrade") != null) {
										aftermarket.setCustomerGrade(data.get("customerGrade").asInt());// 用户级别
									}
									if (data.get("customerTel") != null) {
										aftermarket.setCustomerTel(data.get("customerTel").asText());// 用户电话
									}
									if (data.get("pickwareAddress") != null) {
										aftermarket.setPickwareAddress(data.get("pickwareAddress").asText());// 取件地址
									}
									if (data.get("orderId") != null) {
										aftermarket.setOrderId(data.get("orderId").asText());// 订单号
									}
									if (data.get("orderType") != null) {
										aftermarket.setOrderType(data.get("orderType").asInt());// 订单类型
									}
									if (data.get("orderTypeName") != null) {
										aftermarket.setOrderTypeName(data.get("orderTypeName").asText());// 订单类型名称
									}
									if (data.get("actualPayPrice") != null) {
										aftermarket.setActualPayPrice(
												new BigDecimal(data.get("actualPayPrice").asDouble()));// 实付金额
									}
									if (data.get("skuId") != null) {
										aftermarket.setSkuId(data.get("skuId").asText());// 商品编号
									}
									if (data.get("wareType") != null) {
										aftermarket.setWareType(data.get("wareType").asInt());// 商品类型
									}
									if (data.get("wareTypeName") != null) {
										aftermarket.setWareTypeName(data.get("wareTypeName").asText());// 商品类型名称
									}
									if (data.get("wareName") != null) {
										aftermarket.setWareName(data.get("wareName").asText());// 商品名称
									}
									if (data.get("approvePin") != null) {
										aftermarket.setApprovePin(data.get("approvePin").asText());// 审核人账号
									}
									if (data.get("approveName") != null) {
										aftermarket.setApproveName(data.get("approveName").asText());// 审核人名称
									}
									if (data.get("approveTime") != null) {
										aftermarket.setApproveTime(
												sdf.format(new Date(new Long(data.get("approveTime").asText()))));// 审核时间
									}
									if (data.get("approveResult") != null) {
										aftermarket.setApproveResult(data.get("approveResult").asInt());// 审核结果
									}
									if (data.get("approveResultName") != null) {
										aftermarket.setApproveResultName(data.get("approveResultName").asText());// 审核结果名称
									}
									if (data.get("processPin") != null) {
										aftermarket.setProcessPin(data.get("processPin").asText());// 处理人账号
									}
									if (data.get("processName") != null) {
										aftermarket.setProcessName(data.get("processName").asText());// 处理人名称
									}
									if (data.get("processTime") != null) {
										aftermarket.setProcessTime(
												sdf.format(new Date(new Long(data.get("processTime").asText()))));// 处理时间
									}
									if (data.get("processResult") != null) {
										aftermarket.setProcessResult(data.get("processResult").asInt());// 处理结果
									}
									if (data.get("processResultName") != null) {
										aftermarket.setProcessResultName(data.get("processResultName").asText());// 处理结果名称
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
