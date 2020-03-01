package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.CancelOrderDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.CancelOrder;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.CancelOrderService;
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

@Transactional
@Service
public class CancelOrderServiceImpl implements CancelOrderService {
	
	private Logger logger = LoggerFactory.getLogger(CancelOrderServiceImpl.class);
	
	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private CancelOrderDao cancelOrderDao;

	@Autowired
	private PlatOrdersDao platOrdersDao;
	
	@Autowired
	private StoreSettingsDao storeSettingsDao;
	
	@Autowired
	private StoreRecordDao storeRecordDao;
	
	@Autowired
	private RefundOrderDao refundOrderDao;
	
	@Autowired
	private RefundOrdersDao refundOrdersDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private GetOrderNo getOrderNo;
    @Autowired
    private SellSnglDao sellSnglDao;
    @Autowired
    private SellSnglSubDao sellSnglSubDao;
    @Autowired
    private LogRecordDao logRecordDao;
    @Autowired
    private InvtyDocDao invtyDocDao;
	private List<CancelOrder> cancelOrderList;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public int audit(List<CancelOrder> cancelOrders,String accNum,String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int count = 0;
		List<String> okids = new ArrayList<String>();
		try {
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			MisUser misUser = misUserDao.select(accNum);
			String date=sdf.format(new Date());
			if (cancelOrders.size() > 0) {
				List<RefundOrder> refundOrderList = new ArrayList();
				List<RefundOrders> refundOrdersList = new ArrayList();
				for (CancelOrder cancelOrder : cancelOrders) {
					if(platOrderDao.checkExsits(cancelOrder.getOrderId(), cancelOrder.getStoreId())==0) {
						//根据当前取消订单中的订单号查询原订单
						//如果返回值为0时，说明没有找到原订单对应订单号
						/*cancelOrder.setAuditHint("未找到对应原订单，不能审核。");
						cancelOrderDao.update(cancelOrder);*/
						cancelOrderDao.delete(cancelOrder);
						continue;
					}
					/*if(platOrderDao.selectNoAuditCountByOrderIdAndStoreId(cancelOrder.getStoreId(), cancelOrder.getOrderId())!=0){
						//当对应原订单存在未审核的订单时
						//不能审核此取消订单
						cancelOrder.setAuditHint("对应原订单尚未审核，此单不能审核。");
						cancelOrderDao.update(cancelOrder);
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载失败，店铺："+storeRecord.getStoreName()+",原因说明："+cancelOrder.getOrderId()+"订单未审核");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款下载");
						logRecord.setOperatOrder(cancelOrder.getOrderId());
						logRecordDao.insert(logRecord);
						cancelOrderDao.delete(cancelOrder);
						continue;
					}*/
					
					List<PlatOrders> platOrdersList=platOrdersDao.selectByEcOrderId(cancelOrder.getOrderId());
							//platOrdersDao.select(cancelOrder.getOrderId());
					if (platOrdersList.size()==0) {
						/*cancelOrder.setAuditHint("对应原订单没有订单明细，此单不能审核。");
						cancelOrderDao.update(cancelOrder);*/
						// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载失败，店铺："+storeRecord.getStoreName()+",原因说明："+cancelOrder.getOrderId()+"订单没有订单明细");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款下载");
						logRecord.setOperatOrder(cancelOrder.getOrderId());
						logRecordDao.insert(logRecord);
						cancelOrderDao.delete(cancelOrder);
						continue;
					}
					boolean flag = true;
					for (int i = 0; i < platOrdersList.size(); i++) {
						//循环判断原订单是否存在未匹配到存货档案的记录
						if(!StringUtils.isNotEmpty(platOrdersList.get(i).getInvId())) {
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
						logRecord.setOperatContent("退款单下载，订单："+cancelOrder.getOrderId()+"存在未匹配到存货档案的记录，本次下载取消订单失败");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(cancelOrder.getOrderId());
						logRecordDao.insert(logRecord);
						//删除自主售后单，方便下次下载时能正确下载下来
						cancelOrderDao.delete(cancelOrder);
						continue;
					}
					RefundOrder refundOrder = new RefundOrder();
					String refId=getOrderNo.getSeqNo("tk", accNum);
					refundOrder.setRefId(refId);
					
					refundOrder.setStoreId(cancelOrder.getStoreId());
//					private String storeName;//店铺名称
					refundOrder.setEcRefId(cancelOrder.getId());
					refundOrder.setApplyDate(cancelOrder.getApplyTime());
					refundOrder.setBuyerId(cancelOrder.getBuyerId());
					refundOrder.setIsRefund(1);
					refundOrder.setRefReason(cancelOrder.getReason());
//				    private String refExplain;//退款说明
					refundOrder.setRefStatus(1);
					refundOrder.setTreDate(date);
					refundOrder.setOperator(misUser.getUserName());
					refundOrder.setOperatorId(misUser.getAccNum());
					refundOrder.setOperatorTime(date);
					refundOrder.setOrderId(cancelOrder.getOrderId());//电商平台订单号
					refundOrder.setEcOrderId(cancelOrder.getOrderId());//电商平台订单号
					refundOrder.setSource(2);//设置退款单来源为京东自主售后
					refundOrder.setSourceNo(""+cancelOrder.getId());//设置来源单据号为取消订单单号
					refundOrder.setStoreName(storeRecord.getStoreName());//店铺名称
					refundOrder.setDownTime(date);//设置下载时间为当前时间
					refundOrder.setIsAudit(0);
//				    
					
					int allRefNum=0;
					BigDecimal allRefMoney=new BigDecimal(0);
					for (PlatOrders platOrders : platOrdersList) {
						RefundOrders refundOrders = new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodId(platOrders.getInvId());
						refundOrders.setIsGift(platOrders.getIsGift());//退款单是否赠品取原单属性
//					    private String goodName;//商品名称
						//refundOrders.setGoodSku(platOrders.getGoodSku());
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrders.getInvId());
						refundOrders.setGoodName(invtyDoc.getInvtyNm());
						refundOrders.setCanRefNum(platOrders.getCanRefNum());//设置可退数量
						refundOrders.setRefNum(platOrders.getInvNum());//设置退货数量
						refundOrders.setCanRefMoney(platOrders.getCanRefMoney());//设置可退金额
						if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));//设置保质期
						}
						refundOrders.setPrdcDt(platOrders.getPrdcDt());//设置生产日期
						refundOrders.setInvldtnDt(platOrders.getInvldtnDt());//设置失效日期
						refundOrders.setGoodSku(platOrders.getGoodSku());//sku
						refundOrders.setEcGoodId(platOrders.getGoodId());//spu
						refundOrders.setRefMoney(platOrders.getPayMoney());//设置退货金额
//					    private String batchNo;//批号
//					    private String refWhs;//退货仓库
//					    private String memo;//备注
						refundOrders.setBatchNo(platOrders.getBatchNo());//设置批号
						refundOrders.setRefWhs(platOrders.getDeliverWhs());//选择对应店铺设置的默认退货仓
						allRefNum+=refundOrders.getRefNum();//累加整单退货数量
						allRefMoney=allRefMoney.add(refundOrders.getRefMoney());//累加整单退货金额
						refundOrdersList.add(refundOrders);
					}
					refundOrder.setTreDate(cancelOrder.getCheckTime());//处理时间用平台的审核时间
				    refundOrder.setAllRefNum(allRefNum);
				    refundOrder.setAllRefMoney(allRefMoney);
				    refundOrderList.add(refundOrder);
				    okids.add(cancelOrder.getId());//将处理成功的取消订单id加入到成功列表中，后续更新状态
				}
				if (refundOrderList.size()>0) {
					refundOrderDao.insertList(refundOrderList);
					refundOrdersDao.insertList(refundOrdersList);
					count = cancelOrderDao.audit(okids, misUser.getAccNum(), misUser.getUserName(), date);
				}
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("退款单下载，本次成功下载京东店铺："+storeRecord.getStoreName()+",取消订单"+refundOrderList.size()+"条");
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12退款下载
				logRecord.setTypeName("退款下载");
				logRecordDao.insert(logRecord);
			}
		/*	message = "成功审核" + count + "条取消订单！失败+"+ids.length+"条";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/cancelOrder/audit", isSuccess, message, null);*/
		}catch(Exception e){
			logger.error("URL:ec/cancelOrder/audit 异常说明：",e);
			throw new RuntimeException();
		}
		return okids.size();
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
			List<CancelOrder> cancelOrderList = cancelOrderDao.selectList(map);
			int count = cancelOrderDao.selectCount(map);
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/cancelOrder/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, cancelOrderList);
		} catch (IOException e) {
			logger.error("URL:ec/cancelOrder/queryList 异常说明：",e);
		}
		return resp;
	}

	@Override
	public int download(String storeId,String ecorderId,String accNum,String startDate,String endDate) {
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		int pageNo=1;
		int pageSize=50;
		cancelOrderList = new ArrayList();
		int count=0;
		StoreRecord storeRecord = storeRecordDao.select(storeId);
	    StoreSettings storeSettings = storeSettingsDao.select(storeId);
		try {
			List<Map<String, String>> mapList = storeSettingsDao.selectRelevantEC();
			if ("JD".equals(storeRecord.getEcId())) {
				JDdownload(storeSettings, storeRecord,ecorderId, pageNo, pageSize,startDate,endDate);
			
				if (cancelOrderList.size() == 0) {
					/*isSuccess = true;
					message = "下载完成，没有需要下载的取消订单！";*/
				} else {
					cancelOrderDao.insert(cancelOrderList);
					count=audit(cancelOrderList, accNum, storeId);
					/*isSuccess = true;
					message = "下载成功！本次共下载"+cancelOrderList.size()+"条取消订单。";*/
				}
			}else {
				/*isSuccess = false;
				message = "请选择京东平台的店铺下载取消订单";*/
			}
			/*resp = BaseJson.returnRespObj("ec/cancelOrder/download", isSuccess, message, null);*/
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/cancelOrder/download 异常说明：",e);
		}
		return count;
	}

	private void JDdownload(StoreSettings storeSettings,StoreRecord storeRecord,String ecorderId, int pageNo, int pageSize,String startDate,String endDate)
			throws NoSuchAlgorithmException, IOException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageIndex", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		if (ecorderId.length()>0) {
			//当平台订单号长度大于0时，说明传入值
			//需要将订单号加入查询条件中
			jdReq.put("orderId", ecorderId);
		}
		if (startDate.length()==0||endDate.length()==0) {
			Date date = new Date();
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			cale.add(Calendar.DAY_OF_YEAR, -6);
			jdReq.put("applyTimeStart", sdf.format(cale.getTime()));
			jdReq.put("applyTimeEnd", sdf.format(date));
		}else {
			jdReq.put("applyTimeStart",startDate );
			jdReq.put("applyTimeEnd", endDate);
		}
		//下载的时候不传取消订单的状态编号，插入时判断,
		//非处理状态的时候不插入数据库
		//处理取消订单审核结果编号1,3,5,6,7,9
		String jdRespStr = ECHelper.getJD("jingdong.pop.afs.soa.refundapply.queryPageList", storeSettings,
				jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		JsonNode resultJson = jdRespJson.get("jingdong_pop_afs_soa_refundapply_queryPageList_responce");
		if (resultJson.get("code").asText().equals("0")) {
			ArrayNode resultList = (ArrayNode) resultJson.get("queryResult").get("result");
			if (resultList.size() > 0) {
				for (Iterator<JsonNode> resultIterator = resultList.iterator(); resultIterator.hasNext();) {
					JsonNode result = resultIterator.next();
					if (result.get("status").asInt()==1||result.get("status").asInt()==3||result.get("status").asInt()==5
							||result.get("status").asInt()==6||result.get("status").asInt()==7||result.get("status").asInt()==9) {
						//跟据当前条取消订单id查询是否已存在数据库，不存在时处理
						if (cancelOrderDao.selectById(result.get("id").asText())==null) {
							CancelOrder cancelOrder = new CancelOrder();
							if (result.get("id") != null) {
								cancelOrder.setId(result.get("id").asText());// 退款单编号
							}
							if (result.get("buyerId") != null) {
								cancelOrder.setBuyerId(result.get("buyerId").asText());// 客户账号
							}
							if (result.get("buyerName") != null) {
								cancelOrder.setBuyerName(result.get("buyerName").asText());// 客户姓名
							}
							if (result.get("checkTime") != null) {
								cancelOrder.setCheckTime(result.get("checkTime").asText());// 审核时间
							}
							if (result.get("applyTime") != null) {
								cancelOrder.setApplyTime(result.get("applyTime").asText());// 申请时间
							}
							if (result.get("applyRefundSum") != null) {
								cancelOrder.setApplyRefundSum(result.get("applyRefundSum").decimalValue());// 退款金额
							}
							if (result.get("status") != null) {
								cancelOrder.setStatus(result.get("status").asInt());// 审核状态
							}
							if (result.get("checkUserName") != null) {
								cancelOrder.setCheckUserName(result.get("checkUserName").asText());// 审核人
							}
							if (result.get("orderId") != null) {
								cancelOrder.setOrderId(result.get("orderId").asText());// 订单号
							}
							if (result.get("checkRemark") != null) {
								cancelOrder.setCheckRemark(result.get("checkRemark").asText());// 审核备注
							}
							if (result.get("reason") != null) {
								cancelOrder.setReason(result.get("reason").asText());// 退款原因
							}
							if (result.get("systemId") != null) {
								cancelOrder.setSystemId(result.get("systemId").asInt());// 退款来源
							}
							cancelOrder.setIsAudit(0);// 本地库审核状态
							cancelOrder.setStoreId(storeRecord.getStoreId());// 店铺编号
							cancelOrder.setEcId("JD");// 电商平台编号
							cancelOrder.setDownloadTime(sdf.format(new Date()));
							cancelOrderList.add(cancelOrder);
						}
					}
				}
				int totalcount = resultJson.get("queryResult").get("totalCount").asInt();
				if(totalcount-pageNo*pageSize>0) {
					JDdownload(storeSettings, storeRecord, ecorderId, pageNo+1, pageSize, startDate, endDate);
				}
			}
		}
	}

}
