package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.px.mis.ec.dao.CompensateDao;
import com.px.mis.ec.dao.LogRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.PlatOrdersDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StoreSettingsDao;
import com.px.mis.ec.entity.Compensate;
import com.px.mis.ec.entity.LogRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StoreSettings;
import com.px.mis.ec.service.CompensateService;
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
public class CompensateServiceImpl implements CompensateService {

	private Logger logger = LoggerFactory.getLogger(CompensateServiceImpl.class);
	
	@Autowired
	private MisUserDao misUserDao;
	
	@Autowired
	private CompensateDao compensateDao;

	@Autowired
	private StoreSettingsDao storeSettingsDao;
	
	@Autowired
	private RefundOrderDao refundOrderDao;
	
	@Autowired
	private GetOrderNo getOrderNo;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Autowired
	private PlatOrdersDao platOrdersDao;
	@Autowired
	private SellSnglDao sellSnglDao;
	@Autowired
	private RefundOrdersDao refundOrdersDao;
	@Autowired
	private LogRecordDao logRecordDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	private List<Compensate> compensateList;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	public int audit(List<Compensate> compensates,String accNum,String storeId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		int count = 0;
		List<String> okids=new ArrayList<String>();
		try {
			MisUser misUser = misUserDao.select(accNum);
			String date=sdf.format(new Date());
			if (compensates.size() > 0) {
				List<RefundOrder> refundOrderList = new ArrayList();
				List<RefundOrders> refundOrderss = new ArrayList<RefundOrders>();
				for (Compensate compensate : compensates) {
					/*if (compensate.getIsAudit()==1) {
						//当所选择的赔付单状态为已审核时，不继续往下处理
						continue;
					}*/
					List<PlatOrder> platOrders = platOrderDao.selectPlatOrdersByEcOrderId(compensate.getOrderId());
					if(platOrders.size()==0) {
						/*compensate.setAuditHint("赔付单对应原订单已删除，赔付单不能审核。");
						compensateDao.update(compensate);*/
						continue;
					}
					boolean flag = true;
					for (int i = 0; i < platOrders.size(); i++) {
						/*if (platOrders.get(i).getIsAudit() == 0) {
							compensate.setAuditHint("赔付单对应原订单未审核，赔付单不能审核。");
							compensateDao.update(compensate);
							compensateDao.delete(compensate);
							// 日志记录
							LogRecord logRecord = new LogRecord();
							logRecord.setOperatId(accNum);
							if (misUser != null) {
								logRecord.setOperatName(misUser.getUserName());
							}
							logRecord.setOperatContent("退款单下载，订单："+compensate.getOrderId()+"未审核，赔付单"+compensate.getCompensateId()+"不能下载，本次下载失败。");
							logRecord.setOperatTime(sdf.format(new Date()));
							logRecord.setOperatType(12);// 12退款下载
							logRecord.setTypeName("退款单下载");
							logRecord.setOperatOrder(compensate.getOrderId());
							logRecordDao.insert(logRecord);
							flag=false;
							break;
						}*/ 
					if(!flag) {
						continue;
					}
					okids.add(compensate.getCompensateId());
					/*SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(platOrder.getOrderId());//根据订单号查询对应销售单
					List<SellSnglSub> sellSnglSubs = compensateDao.selectSellSnglSubBySellSnglId(sellSngl.getSellSnglId());*/
					RefundOrder refundOrder = new RefundOrder();
					String refId=getOrderNo.getSeqNo("tk", accNum);
					refundOrder.setRefId(refId);// 退款单编号
					refundOrder.setStoreId(compensate.getStoreId());// 店铺编号
					refundOrder.setEcRefId(compensate.getCompensateId());//电商退款单号 赔付父单号
					refundOrder.setOrderId(platOrders.get(0).getOrderId());//电商订单号
					refundOrder.setSource(3);//设置退款单来源为京东赔付
					refundOrder.setSourceNo(""+compensate.getCompensateId());//设置来源单据号为自主售后的服务单号
					StoreRecord storeRecord = storeRecordDao.select(compensate.getStoreId());
					refundOrder.setStoreName(storeRecord.getStoreName());//店铺名称
//				    private String storeName;//店铺名称
					refundOrder.setApplyDate(compensate.getCreated());// 申请日期
					refundOrder.setBuyerId(platOrders.get(0).getBuyerId());//买家会员号 赔付接口没有买家会员号 取原订单中的买家会员号
					refundOrder.setDownTime(date);//设置下载时间为当前时间
					
					refundOrder.setIsAudit(0);//设置待审核
//					private String storeName;//店铺名称
//					private String buyerId;//店铺名称
					refundOrder.setExpressCode("");
					refundOrder.setIsRefund(0);
					refundOrder.setRefReason(compensate.getCompensateReason());
//				    private String refExplain;//退款说明
					refundOrder.setRefStatus(0);
//					private String downTime;//下载时间
					refundOrder.setTreDate(compensate.getCreated());
					refundOrder.setOperator(misUser.getUserName());
//				    private String memo;//备注
				    refundOrder.setAllRefNum(0);
				    refundOrder.setAllRefMoney(compensate.getCompensateAmount());
				    refundOrder.setOperatorId(misUser.getAccNum());
				    refundOrder.setOperatorTime(date);
				    refundOrder.setEcOrderId(compensate.getOrderId());//平台订单号
				    List<PlatOrders> platOrderslist = platOrdersDao.selectByEcOrderId(compensate.getOrderId());
				    if(platOrderslist.size()==0) {
				    	// 日志记录
						LogRecord logRecord = new LogRecord();
						logRecord.setOperatId(accNum);
						if (misUser != null) {
							logRecord.setOperatName(misUser.getUserName());
						}
						logRecord.setOperatContent("退款单下载，失败。订单："+compensate.getOrderId()+"商品明细无实付金额，不符合赔付条件");
						logRecord.setOperatTime(sdf.format(new Date()));
						logRecord.setOperatType(12);// 12退款下载
						logRecord.setTypeName("退款单下载");
						logRecord.setOperatOrder(compensate.getOrderId());
						logRecordDao.insert(logRecord);
						//删除自主售后单，方便下次下载时能正确下载下来
						compensateDao.delete(compensate);
						break;
				    }else if(platOrderslist.size()==1) {
						//当订单明细只有一条记录时，不需要循环占比拆分赔付金额
						RefundOrders refundOrders=new RefundOrders();
						refundOrders.setRefId(refId);
						refundOrders.setGoodSku(platOrderslist.get(0).getGoodSku());//商品sku
						refundOrders.setEcGoodId(platOrderslist.get(0).getGoodId());//设置店铺商品编码，暂时为空
						refundOrders.setCanRefNum(platOrderslist.get(0).getCanRefNum());//设置可退数量
						refundOrders.setRefNum(0);//设置退货数量
						//设置可退金额
						refundOrders.setCanRefMoney(platOrderslist.get(0).getCanRefMoney());
						//设置退货金额
						refundOrders.setRefMoney(compensate.getCompensateAmount());
						refundOrders.setGoodId(platOrderslist.get(0).getInvId());//设置存货编码
						InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrderslist.get(0).getInvId());
						refundOrders.setGoodName(invtyDoc.getInvtyNm());//设置存货名称
						refundOrders.setBatchNo(platOrderslist.get(0).getBatchNo());//设置退货批次
						refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//取对应店铺的默认退货仓
						if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
							refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));
						}
						refundOrders.setPrdcDt(platOrderslist.get(0).getPrdcDt());//设置生产日期
						refundOrders.setInvldtnDt(platOrderslist.get(0).getInvldtnDt());//设置失效日期
						refundOrderss.add(refundOrders);//装入list
						
					}else {
						//多条时，需要循环求占比
						DecimalFormat df1 = new DecimalFormat("0.00");
						BigDecimal total = new BigDecimal(0);
						for (int j = 0; j < platOrderslist.size(); j++) {
							if(platOrderslist.get(j).getPayMoney().compareTo(BigDecimal.ZERO)==0) {
								platOrderslist.remove(j);
								j--;
							}
						}
						for (int j = 0; j < platOrders.size(); j++) {
							//循环每条订单求和
							total=total.add(platOrders.get(j).getPayMoney());//取每条明细实付金额求和
						}
						//判断赔付金额是否大于销售单整单价税合计
						/*if(compensate.getCompensateAmount().compareTo(total)>0) {
							//当赔付金额大于
						}*/
						BigDecimal hasCompensateAmount=new BigDecimal(0);//声明已赔付金额
						for (int j = 0; j < platOrderslist.size(); j++) {
							RefundOrders refundOrders=new RefundOrders();
							refundOrders.setRefId(refId);
							refundOrders.setIsGift(0);//设置不是赠品
							refundOrders.setIsGift(platOrderslist.get(j).getIsGift());//退款单是否赠品属性取原单属性
							refundOrders.setGoodSku(platOrderslist.get(j).getGoodSku());//商品sku
							refundOrders.setEcGoodId(platOrderslist.get(j).getGoodId());//设置店铺商品编码，暂时为空
							refundOrders.setCanRefNum(platOrderslist.get(j).getCanRefNum());//设置可退数量
							refundOrders.setRefNum(0);//设置退货数量
							InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(platOrderslist.get(j).getInvId());
							if (StringUtils.isNotEmpty(invtyDoc.getBaoZhiQiDt())) {
								refundOrders.setBaozhiqi(Integer.parseInt(invtyDoc.getBaoZhiQiDt()));
							}
							refundOrders.setPrdcDt(platOrderslist.get(j).getPrdcDt());//设置生产日期
							refundOrders.setInvldtnDt(platOrderslist.get(j).getInvldtnDt());//设置失效日期
							//设置可退金额
							refundOrders.setCanRefMoney(platOrderslist.get(j).getCanRefMoney());
							//设置退货金额
							if(i+1==platOrderslist.size()) {
								//最后一条 用减法
								refundOrders.setRefMoney(compensate.getCompensateAmount().subtract(hasCompensateAmount));
							}else {
								BigDecimal bb= compensate.getCompensateAmount().divide(total,8,RoundingMode.HALF_DOWN);
								
								//BigDecimal bbBigDecimal = platOrderslist.get(j).getPayMoney().multiply(compensate.getCompensateAmount().divide(total,8));
								//计算每条明细应赔付金额保留两位小数
								BigDecimal dBigDecimal =new BigDecimal(df1.format(platOrderslist.get(j).getPayMoney().multiply(bb)));
								hasCompensateAmount=hasCompensateAmount.add(dBigDecimal);
								refundOrders.setRefMoney(dBigDecimal);
							}
							refundOrders.setGoodId(platOrderslist.get(j).getInvId());//设置存货编码
							refundOrders.setGoodName(invtyDoc.getInvtyNm());//设置存货名称
							refundOrders.setBatchNo(platOrderslist.get(j).getBatchNo());//设置退货批次
							refundOrders.setRefWhs(storeRecord.getDefaultRefWhs());//取对应店铺的默认退货仓
							refundOrderss.add(refundOrders);//装入list
						}
					}
					
				    refundOrderList.add(refundOrder);
				}
				
			}
				if (refundOrderList.size()>0) {
					refundOrderDao.insertList(refundOrderList);
					refundOrdersDao.insertList(refundOrderss);
					//count = compensateDao.audit(okids, date, misUser.getAccNum(), misUser.getUserName());
					count = refundOrderList.size();
				}
		}
		//StoreRecord storeRecord = storeRecordDao.select(storeId);
		
			
		/*	message = "成功审核" + count + "条赔付单！";
			isSuccess = true;
			resp = BaseJson.returnRespObj("ec/cancelOrder/audit", isSuccess, message, null);*/
		}catch(Exception e){
			logger.error("URL:ec/cancelOrder/audit 异常说明：",e);
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
			List<Compensate> compensateList = compensateDao.selectList(map);
			int count = compensateDao.selectCount(map);
			message = "查询成功";
			isSuccess = true;
			resp = BaseJson.returnRespList("ec/compensate/queryList", isSuccess, message, count, pageNo, pageSize, 0,
					0, compensateList);
		} catch (IOException e) {
			logger.error("URL:ec/compensate/queryList 异常说明：",e);
		}
		return resp;
	}
	String errMessage="";
	@Override
	public int download(String storeId,String ecOrderId,String accNum,String startDate,String endDate) {
		int count = 0;
		int pageNo=1;
		int pageSize=30;
		errMessage="";
		compensateList = new ArrayList();
		try {
			StoreSettings storeSettings = storeSettingsDao.select(storeId);
			StoreRecord storeRecord = storeRecordDao.select(storeId);
			MisUser misUser = misUserDao.select(accNum);
			if ("JD".equals(storeRecord.getEcId())) {
				download(storeSettings, ecOrderId, pageNo, pageSize,startDate,endDate);
			}
			if (errMessage.length()==0) {
				//当errmessage的长度等于0时，说明下载过程中没有出现过问题。
				if (compensateList.size() == 0) {
				} else {
					compensateDao.insert(compensateList);
					count = audit(compensateList, accNum, storeId);
					
				} 
				// 日志记录
				LogRecord logRecord = new LogRecord();
				logRecord.setOperatId(accNum);
				if (misUser != null) {
					logRecord.setOperatName(misUser.getUserName());
				}
				logRecord.setOperatContent("退款单下载，本次成功下载店铺："+storeRecord.getStoreName()+"赔付单"+count+"条");
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
				logRecord.setOperatContent("退款单下载失败，店铺："+storeRecord.getStoreName()+",原因说明："+errMessage);
				logRecord.setOperatTime(sdf.format(new Date()));
				logRecord.setOperatType(12);// 12退款下载
				logRecord.setTypeName("退款下载");
				logRecordDao.insert(logRecord);
			}
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("URL:ec/compensate/download 异常说明：",e);
		}
		return count;
	}

	private void download(StoreSettings storeSettings,String ecorderid, int pageNo, int pageSize,String startDate,String endDate)
			throws NoSuchAlgorithmException, IOException {
		ObjectNode jdReq = JacksonUtil.getObjectNode("");
		jdReq.put("pageIndex", pageNo + "");
		jdReq.put("pageSize", pageSize + "");
		//当传入参数没有平台订单号时，按照当前时间-7天查询范围数据
		//传入参数有平台订单号时，只根据平台订单号查询
		if(ecorderid.length()!=0) {
			jdReq.put("refId",ecorderid);
		}else {
			if (startDate.length()==0||endDate.length()==0) {
				Date date = new Date();
				Calendar cale = Calendar.getInstance();
				cale.setTime(date);
				cale.add(Calendar.DAY_OF_YEAR, -7);
				jdReq.put("refType", 10);
				jdReq.put("modifiedStartTime", sdf.format(cale.getTime()));
				jdReq.put("modifiedEndTime", sdf.format(date));
			}else {
				jdReq.put("modifiedStartTime", startDate);
				jdReq.put("modifiedEndTime", endDate);
			}
		}
		
		String jdRespStr = ECHelper.getJD("jingdong.pop.afs.soa.compensate.queryCompensateList", storeSettings, jdReq.toString());
		ObjectNode jdRespJson = JacksonUtil.getObjectNode(jdRespStr);
		if(jdRespJson.has("error_response")) {
			//如果存在error_response
			//说明查询出现错误，返回错误提示信息
			errMessage=jdRespJson.get("error_response").get("zh_desc").asText();
			compensateList.clear();
		}else {
			JsonNode resultJson = jdRespJson.get("jingdong_pop_afs_soa_compensate_queryCompensateList_responce");
			if (resultJson.get("code").asText().equals("0")) {
				 
				if (resultJson.get("queryResult").get("errorCode").asText().equals("10000")) {
					ArrayNode resultList = (ArrayNode) resultJson.get("queryResult").get("result");
					if (resultList != null) {
						for (Iterator<JsonNode> resultIterator = resultList.iterator(); resultIterator.hasNext();) {
							JsonNode result = resultIterator.next();
							Compensate compensate = new Compensate();
							if (compensateDao.selectById(result.get("compensate_id").asText())==null) {
								if(result.get("type").asInt()==30||result.get("type").asInt()==40) {
									//30  40  属于先行赔付单据，先行赔付的单子check_status=1才有效
									if (result.get("check_status").asInt()==1) {
										
										//商家审核状态 0：待审核 1：审核通过 2：审核不通过
										//审核状态为1时，保存到系统
										compensate.setCompensateId(result.get("compensate_id").asText());//赔付单号
										compensate.setCompensateKeyid(result.get("compensate_keyid").asInt());//赔付子单号
										compensate.setVenderId(result.get("vender_id").asText());//商家编号
										compensate.setType(result.get("type").asInt());//赔付类型
										compensate.setOrderId(result.get("order_id").asText());//订单号
										compensate.setOrderType(result.get("order_type").asInt());//订单类型
										compensate.setModified(sdf.format(new Date(new Long(result.get("modified").asText()))));//更新日期
										compensate.setCreated(sdf.format(new Date(new Long(result.get("created").asText()))));//创建日期
										compensate.setCompensateType(result.get("compensate_type").asInt());//赔付金额类型
										compensate.setShouldPay(result.get("shouldpay").decimalValue());//应赔付金额
										compensate.setCompensateAmount(result.get("compensateamount").decimalValue());//赔付金额
										compensate.setCompensateReason(result.get("compensate_reason").asText());//赔付原因
										compensate.setCheckStatus(result.get("check_status").asInt());//商家审核状态
										compensate.setErpCheckStatus(result.get("erp_check_status").asInt());//运营客服审核状态
										compensate.setCanSecondAppeal(result.get("can_second_appeal").asInt());//是否可以二次申诉
										compensate.setDownloadTime(sdf.format(new Date()));//设置下载时间
										compensate.setIsAudit(0);//本地库审核状态
										compensate.setEcId("JD");//电商平台编号
										compensate.setStoreId(storeSettings.getStoreId());
										compensateList.add(compensate);
									}
								}else {
									
									//商家审核状态 0：待审核 1：审核通过 2：审核不通过
									//审核状态为1时，保存到系统
									compensate.setCompensateId(result.get("compensate_id").asText());//赔付单号
									compensate.setCompensateKeyid(result.get("compensate_keyid").asInt());//赔付子单号
									compensate.setVenderId(result.get("vender_id").asText());//商家编号
									compensate.setType(result.get("type").asInt());//赔付类型
									compensate.setOrderId(result.get("order_id").asText());//订单号
									compensate.setOrderType(result.get("order_type").asInt());//订单类型
									compensate.setModified(sdf.format(new Date(new Long(result.get("modified").asText()))));//更新日期
									compensate.setCreated(sdf.format(new Date(new Long(result.get("created").asText()))));//创建日期
									compensate.setCompensateType(result.get("compensate_type").asInt());//赔付金额类型
									compensate.setShouldPay(result.get("shouldpay").decimalValue());//应赔付金额
									compensate.setCompensateAmount(result.get("compensateamount").decimalValue());//赔付金额
									compensate.setCompensateReason(result.get("compensate_reason").asText());//赔付原因
									compensate.setCheckStatus(result.get("check_status").asInt());//商家审核状态
									compensate.setErpCheckStatus(result.get("erp_check_status").asInt());//运营客服审核状态
									compensate.setCanSecondAppeal(result.get("can_second_appeal").asInt());//是否可以二次申诉
									compensate.setDownloadTime(sdf.format(new Date()));//设置下载时间
									compensate.setIsAudit(0);//本地库审核状态
									compensate.setEcId("JD");//电商平台编号
									compensate.setStoreId(storeSettings.getStoreId());
									compensateList.add(compensate);
								}
							}
						}
						download(storeSettings, ecorderid, pageNo+1, pageSize,startDate,endDate);
					} 
				}else {
					//如果存在errorCode不等于10000时，没有数据
					//当前页没有数据，不再执行递归
				}
			}else {
				errMessage=resultJson.get("error_response").get("zh_desc").asText();
				compensateList.clear();
			}
		}
		
	}

}
