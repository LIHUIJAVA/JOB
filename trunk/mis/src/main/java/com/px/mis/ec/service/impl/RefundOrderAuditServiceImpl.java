package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.RefundOrderDao;
import com.px.mis.ec.dao.RefundOrdersDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.RefundOrder;
import com.px.mis.ec.entity.RefundOrders;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.service.RefundOrderAuditService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.InvtyTabDao;
import com.px.mis.purc.dao.RtnGoodsDao;
import com.px.mis.purc.dao.RtnGoodsSubDao;
import com.px.mis.purc.dao.SellOutWhsDao;
import com.px.mis.purc.dao.SellOutWhsSubDao;
import com.px.mis.purc.dao.SellSnglDao;
import com.px.mis.purc.entity.InvtyDoc;
import com.px.mis.purc.entity.RtnGoods;
import com.px.mis.purc.entity.RtnGoodsSub;
import com.px.mis.purc.entity.SellOutWhs;
import com.px.mis.purc.entity.SellOutWhsSub;
import com.px.mis.purc.entity.SellSngl;
import com.px.mis.purc.util.CalcAmt;
import com.px.mis.system.dao.MisUserDao;
import com.px.mis.system.entity.MisUser;
import com.px.mis.util.BaseJson;
import com.px.mis.util.GetOrderNo;
import com.px.mis.whs.dao.InvtyTabMapper;
import com.px.mis.whs.dao.WhsDocMapper;
import com.px.mis.whs.entity.InvtyTab;
import com.px.mis.whs.entity.WhsDoc;
@Service
@Transactional
public class RefundOrderAuditServiceImpl implements RefundOrderAuditService {
	@Autowired
	private RefundOrderDao refundOrderDao;//退款单
	@Autowired
	private RefundOrdersDao refundOrdersDao;//退款单子表
	@Autowired
	private RtnGoodsDao rtnGoodsDao;//退货单
	@Autowired
	private RtnGoodsSubDao rtnGoodsSubDao;//退货单子表
	@Autowired
	private SellOutWhsDao sellOutWhsDao;//销售出库单主表
	@Autowired
	private SellOutWhsSubDao sellOutWhsSubDao;//销售出库单主表
	@Autowired
	private MisUserDao misUserDao;//用户表
	@Autowired
	private WhsDocMapper whsDocDao;//仓库档案；
	@Autowired
	private GoodRecordDao goodRecordDao;//平台商品档案；
	@Autowired
	private InvtyTabMapper invtyTabDao;//库存表
	@Autowired
	private InvtyDocDao invtyDocDao;//存货档案；
	@Autowired
	private StoreRecordDao storeRecordDao;//店铺档案；
	@Autowired
	private PlatOrderDao platOrderDao;//订单
	@Autowired
	private SellSnglDao sellSnglDao;//销售单
	@Autowired
	private GetOrderNo getOrderNo;//生成各种单号
	@Autowired
	private InvtyTabDao itd;//库存表
	//审核退款单；
	@Override
	public String refundOrderAudit(String refId,String userId,String loginDate) {//退款单审核
		String resp="";
		String message="";
		Boolean isSuccess = true;
		RefundOrder refundOrder = refundOrderDao.select(refId);
		List<RefundOrders> list = refundOrdersDao.selectList(refId);
		if(refundOrder==null || list==null || list.size()==0) {
			message="退款单编号"+refId+"不存在或者该退款单子表不存在，退款单审核失败！";
			isSuccess=false;
		}else if(refundOrder.getIsAudit()==1){
			message="退款单编号"+refId+"下的退款单已经审核，退款单审核失败！";
			isSuccess=false;
		}else{
			RtnGoods rtnGoods=new RtnGoods();
			String rtnGoodsId=null;//退货单号
			try {
				rtnGoodsId = getOrderNo.getSeqNo("th", userId);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			rtnGoods.setRtnGoodsId(rtnGoodsId);//退货单编号
			rtnGoods.setRtnGoodsDt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//订单日期；
			PlatOrder selectPlatOrder = platOrderDao.select(refundOrder.getOrderId());//查询原订单
			if(selectPlatOrder!=null) {
				rtnGoods.setSellTypId(selectPlatOrder.getSellTypId());//销售类型编号 ；
				rtnGoods.setBizTypId(selectPlatOrder.getBizTypId());//业务类型编号；
				rtnGoods.setRecvSendCateId(selectPlatOrder.getRecvSendCateId());//收发类别编号；
			}
			StoreRecord storeRecord = storeRecordDao.select(refundOrder.getStoreId());
			if(storeRecord!=null) {
				rtnGoods.setCustId(storeRecord.getEcId());//客户编号    电商平台编号(店铺档案)
			}
			rtnGoods.setAccNum(userId);//业务员编号
			MisUser misUser = misUserDao.select(userId);
			if(misUser!=null) {
				rtnGoods.setUserName(misUser.getUserName());//用户名称
			}
			rtnGoods.setSetupPers(userId);//创建人  用户编号
			rtnGoods.setSetupTm(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//创建时间；
			//收货地址名称
			//判断仓库编码是否一致，如果不一致，审核失败
			String whsEncd=list.get(0).getRefWhs();
			for(RefundOrders refundOrders:list) {
				if(whsEncd==null ||!whsEncd.equals(refundOrders.getRefWhs()) ) {
					try {
						message="退货仓库编码为null,或者"+whsEncd+"不一致，退款单审核失败！";
						isSuccess=false;
						resp = BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit", isSuccess, message, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return resp;
				}
			}
			WhsDoc whsDoc = whsDocDao.selectWhsDoc(whsEncd);
			if(whsDoc!=null) {
				rtnGoods.setDelvAddrNm(whsDoc.getWhsNm());//发货地址（仓库地址，根据退款单子表中的退货仓库编码获取）
			}
			//销售订单号 根据订单编号查询销售单；
			SellSngl sellSngl = sellSnglDao.selectSellSnglByOrderId(refundOrder.getOrderId());
			if(sellSngl!=null) {
				rtnGoods.setSellOrdrId(sellSngl.getSellSnglId());//销售订单号销售单号
			}
			rtnGoods.setMemo(refundOrder.getMemo());//备注；
			rtnGoods.setIsNtBllg(0);//是否需要开票；
			List<RtnGoodsSub> rtnGoodsSubs=new ArrayList<RtnGoodsSub>();
			for(RefundOrders refundOrders:list) {
				RtnGoodsSub rtnGoodsSub=new RtnGoodsSub();
				rtnGoodsSub.setWhsEncd(refundOrders.getRefWhs());//仓库编号；
				//存货编码 平台商品表good_record中的平台商品编码；
				String goodId = refundOrders.getGoodId();//商品编号
				GoodRecord goodRecord = goodRecordDao.select(goodId);
				String ecGoodId=null;
				if(goodRecord!=null){
					ecGoodId = goodRecord.getEcGoodId();
					rtnGoodsSub.setInvtyEncd(ecGoodId);//存货编码		平台商品编码
				}
				rtnGoodsSub.setRtnGoodsId(rtnGoods.getRtnGoodsId());//退货单主表编号；
				Integer refNum2 = refundOrders.getRefNum();//退货数量
				BigDecimal refMoney = refundOrders.getRefMoney();//退款金额；
				Integer canRefNum2 = refundOrders.getCanRefNum();//可退货数量
				if(canRefNum2<refNum2) {//退货数量不能大于可退货数量
					try {
						message="退货数量"+refNum2+"大于可退货数量"+canRefNum2+",退款单审核失败！";
						isSuccess=false;
						resp = BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit", isSuccess, message, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return resp;
				}
				BigDecimal refNum = new BigDecimal(refNum2);
				//求Decimal的相反数，只需要调用negate方法，
				rtnGoodsSub.setQty(refNum.negate());//数量 负数
				rtnGoodsSub.setBatNum(refundOrders.getBatchNo());//批号
				//用实付单价金额和销项税率等计算一系列数据；
				//退款金额除以退款数量就是退货金额，保留8位小数，舍位的时候四舍五入；
				BigDecimal refPrice = refMoney.divide(refNum,8,BigDecimal.ROUND_HALF_UP);//退货单价 	无税单价
//				InvtyTab invtyTab = new InvtyTab();
//				String whsEncd2 = rtnGoodsSub.getWhsEncd();
//				invtyTab.setWhsEncd(whsEncd2);
				String invtyEncd = rtnGoodsSub.getInvtyEncd();
//				invtyTab.setInvtyEncd(invtyEncd);
//				String batNum = rtnGoodsSub.getBatNum();
//				invtyTab.setBatNum(batNum);
//				invtyTab = invtyTabDao.selectByReverse(invtyTab);
				InvtyDoc invtyDocOne = invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyEncd);
				BigDecimal taxRate=null;
				if(invtyDocOne!=null) {
					taxRate = invtyDocOne.getOptaxRate();//税率  存货档案中的销项税率
				}
				if(refPrice!=null && refNum!=null && taxRate!=null) {
					BigDecimal noTaxAmt = CalcAmt.noTaxAmt(refPrice, refNum);//无税金额
					BigDecimal taxAmt = CalcAmt.taxAmt(refPrice, refNum,taxRate);//税额
					BigDecimal cntnTaxUprc = CalcAmt.cntnTaxUprc(refPrice, refNum,taxRate);//含税单价
					BigDecimal prcTaxSum = CalcAmt.prcTaxSum(refPrice, refNum,taxRate);//价税合计
					
					rtnGoodsSub.setCntnTaxUprc(cntnTaxUprc);//含税单价；
					rtnGoodsSub.setPrcTaxSum(prcTaxSum);//价税合计；
					rtnGoodsSub.setNoTaxAmt(noTaxAmt);//无税金额；
					rtnGoodsSub.setNoTaxUprc(refPrice);//无税单价
					rtnGoodsSub.setTaxAmt(taxAmt);//税额
					rtnGoodsSub.setTaxRate(taxRate);//税率
				}
//				rtnGoodsSub.setPrdcDt(invtyTab.getPrdcDt());//生产日期；
//				rtnGoodsSub.setBaoZhiQi(Integer.valueOf(invtyTab.getBaoZhiQi()));//保质期
//				rtnGoodsSub.setInvldtnDt(invtyTab.getInvldtnDt());//失效日期；
				
				InvtyDoc invtyDoc = invtyDocDao.selectInvtyDocByInvtyDocEncd(ecGoodId);
				String invtyClsEncd=null;
				if(invtyDoc!=null) {
					invtyClsEncd = invtyDoc.getInvtyClsEncd();//存货分类编码；
				}
				if(invtyClsEncd==null) {
					
				}else if(invtyClsEncd.startsWith("100")) {
					rtnGoodsSub.setIsComplimentary(1);//是赠品；
				}else {
					rtnGoodsSub.setIsComplimentary(0);//不是赠品；
				}
				if(refNum2 >0) {//判断退货单子表的是否退货的字段数据；
					rtnGoodsSub.setIsNtRtnGoods(1);//是否退货    0：不退货；1：退货
				}else {
					rtnGoodsSub.setIsNtRtnGoods(0);//是否退货    0：不退货；1：退货
				}
				rtnGoodsSubs.add(rtnGoodsSub);//添加到退货单子表中
			}
			rtnGoodsDao.insertRtnGoods(rtnGoods);//插入退货单主表
			rtnGoodsSubDao.insertRtnGoodsSub(rtnGoodsSubs);//插入退货单子表
			//以上是生成退货单；
			
			
			//=======================生成红字销售出库单===================================
			Integer isRefund = refundOrder.getIsRefund();
			//销售单主表实体
			SellOutWhs sellOutWhs = new SellOutWhs();
			//销售单子表实体
			List<SellOutWhsSub> sellOutWhsSub = new ArrayList<>();
			//库存实体
			InvtyTab invtyTab = new InvtyTab();
			if(isRefund==1) {//是否退货	当退货的时候生成红字销售出库单；
				//生成红字销售出库单；
				//获取订单号
			    String number=getOrderNo.getSeqNo("CK", userId);
			    if (sellOutWhsDao.selectSellOutWhsByOutWhsId(number)!=null){//会重复吗?????
					message="编号"+number+"已存在，请重新输入！";
					isSuccess=false;
				}else {
					sellOutWhs.setOutWhsId(number);
					//出库日期应为审核或实际出库日期？？
					sellOutWhs.setOutWhsDt(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
					sellOutWhs.setAccNum(rtnGoods.getAccNum());//用户编号
					sellOutWhs.setUserName(rtnGoods.getUserName());
					sellOutWhs.setCustId(rtnGoods.getCustId());
					sellOutWhs.setBizTypId(rtnGoods.getBizTypId());
					sellOutWhs.setSellTypId(rtnGoods.getSellTypId());
					sellOutWhs.setRecvSendCateId(rtnGoods.getRecvSendCateId());
					sellOutWhs.setOutIntoWhsTypId("销售出库");
					sellOutWhs.setSellOrdrInd(rtnGoods.getSellOrdrId());//销售单标识
					sellOutWhs.setSetupPers(rtnGoods.getSetupPers());//创建人  
					/*sellOutWhs.setSetupTm(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));//创建时间 */	
					sellOutWhs.setMemo(rtnGoods.getMemo());//备注信息
					sellOutWhs.setRtnGoodsId(rtnGoods.getRtnGoodsId());
					/*String sellOrdrId = sellOutWhs.getSellOrdrInd();*/
					for(RtnGoodsSub rGSub:rtnGoodsSubs) {
						invtyTab.setWhsEncd(rGSub.getWhsEncd());
						invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
						invtyTab.setBatNum(rGSub.getBatNum());
						invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
						invtyTab.setAvalQty(rGSub.getQty());
						//在库存表中将对应的可用量减少（由于退货单中的数量是负数，所以库存表中要减可用量）
						itd.updateInvtyTabAvalQtyJian(invtyTab);
						SellOutWhsSub sOutWhsSub = new SellOutWhsSub();
						sOutWhsSub.setInvtyEncd(rGSub.getInvtyEncd());//存货档案
						sOutWhsSub.setWhsEncd(rGSub.getWhsEncd());//仓库档案
						sOutWhsSub.setBatNum(rGSub.getBatNum());//批号
						sOutWhsSub.setOutWhsId(sellOutWhs.getOutWhsId());//销售出库单子表中主表编号
						sOutWhsSub.setQty(rGSub.getQty());//数量   获取数量 sowSub.getQty();
						sOutWhsSub.setTaxRate((rGSub.getTaxRate()).divide(new BigDecimal(100)));//税率   
//						sOutWhsSub.setPrdcDt(rGSub.getPrdcDt());//生产日期
//						sOutWhsSub.setBaoZhiQi(rGSub.getBaoZhiQi());//保质期
//						sOutWhsSub.setInvldtnDt(CalcAmt.getDate(sOutWhsSub.getPrdcDt(), sOutWhsSub.getBaoZhiQi()));
//						BigDecimal noTaxUprc = sellOutWhsDao.selectSellOutWhsSubByInWhBn(sOutWhsSub);
//						if(noTaxUprc!=null) {
//							//获取无税单价
//							sOutWhsSub.setNoTaxUprc(noTaxUprc);  
//						}
						//计算未税金额  金额=未税数量*未税单价
						sOutWhsSub.setNoTaxAmt(CalcAmt.noTaxAmt(sOutWhsSub.getNoTaxUprc(), sOutWhsSub.getQty()));
						//计算税额  税额=未税金额*税率
						sOutWhsSub.setTaxAmt(CalcAmt.taxAmt(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
						//计算含税单价  含税单价=无税单价*税率+无税单价
						sOutWhsSub.setCntnTaxUprc(CalcAmt.cntnTaxUprc(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
						//计算价税合计  价税合计=无税金额*税率+无税金额=税额+无税金额
						sOutWhsSub.setPrcTaxSum(CalcAmt.prcTaxSum(sOutWhsSub.getNoTaxUprc(),sOutWhsSub.getQty(), sOutWhsSub.getTaxRate()));
						sOutWhsSub.setIsNtRtnGoods(1);//是否退货
						sOutWhsSub.setIsComplimentary(rGSub.getIsComplimentary());//是否赠品
						sOutWhsSub.setMemo(rGSub.getMemo());//备注
						sellOutWhsSub.add(sOutWhsSub);
					}
					sellOutWhsDao.insertSellOutWhs(sellOutWhs);
					sellOutWhsSubDao.insertSellOutWhsSub(sellOutWhsSub);
				    isSuccess=true;
				    message = "退货单单审核成功！";
				}
			}else {//当仅退款的时候不生成红字销售出库单；
				
			}
			//====================================================================
			//是否审核 审核人审核日期怎么处理
			rtnGoods.setIsNtChk(1);//退货单已审核
			rtnGoods.setAccNum(userId);//审核人
			rtnGoods.setChkTm(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));//审核日期
			rtnGoodsDao.updateRtnGoodsByRtnGoodsId(rtnGoods);//更新退货单的审核状态；
			refundOrder.setIsAudit(1);
			refundOrderDao.update(refundOrder);//更新退款单的审核状态；
			message="退货单已生成，退款单审核成功！";
			isSuccess=true;
		}
//		refundOrder.setRefundOrders(selectList);
		try {
			 resp=BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAudit",isSuccess,message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
	//弃审退款单；
	@Override
	public String refundOrderAbandonAudit(String refId, String userId) {
		String resp="";
		String message="";
		Boolean isSuccess = true;
		//销售出库单主表实体
		SellOutWhs sellOutWhs = new SellOutWhs();
//		//销售出库单子表实体
//		List<SellOutWhsSub> sellOutWhsSub = new ArrayList<>();
		//库存实体
		InvtyTab invtyTab = new InvtyTab();
		RefundOrder refundOrder = refundOrderDao.select(refId);//查询退款单
		if(refundOrder == null) {
			message="退款单不存在，无法弃审！！！";
			isSuccess = false;
		}else {
			//根据退款单编号去查退货单
			RtnGoods rtnGoods=new RtnGoods();
			rtnGoods = rtnGoodsDao.selectRtnGoodsByRefId(refId);//查询退货单
			//sellOutWhs = sellOutWhsDao.selectSellOutWhsByRtnGoodsId(rtnGoods.getRtnGoodsId());
			//通过退货单号取退货单子表信息
			List<RtnGoodsSub> rtnGoodsSubs = rtnGoodsSubDao.selectRtnGoodsSubByRtnGoodsId(rtnGoods.getRtnGoodsId());
			for(RtnGoodsSub rGSub:rtnGoodsSubs) {
				invtyTab.setWhsEncd(rGSub.getWhsEncd());
				invtyTab.setInvtyEncd(rGSub.getInvtyEncd());
				invtyTab.setBatNum(rGSub.getBatNum());
				invtyTab = itd.selectInvtyTabsByTerm(invtyTab);
				if(invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == 1
						|| invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == 0) {
					if(sellOutWhs.getIsNtChk()==0) {
						//求Decimal的相反数，只需要调用negate方法，
						invtyTab.setAvalQty(rGSub.getQty().negate());//退款单的数量为负；
						itd.updateInvtyTabAvalQtyJia(invtyTab);//修改库存中的可用量；
						sellOutWhsDao.deleteSellOutWhsByOutWhsId(sellOutWhs.getOutWhsId());//删除红字销售出库单；
						rtnGoodsDao.deleteRtnGoodsByRtnGoodsId(rtnGoods.getRtnGoodsId());//删除退货单；
						
						refundOrder.setIsAudit(0);//退款单的审核状态改为未审核；
						refundOrderDao.update(refundOrder);//修改退款单得审核状态；
						isSuccess=true;
						message = "退款单弃审成功！";
					}else {
						message = "退款单对应的红字销售出库单已审核，无法弃审退款单";
					}
				}else if(invtyTab.getAvalQty().compareTo(rGSub.getQty().abs()) == -1) {
					isSuccess=false;
					message = "库存表中数量不足，无法弃审退款单";
				}
			}
		}
		try {
			 resp=BaseJson.returnRespObj("ec/refundOrderAudit/refundOrderAbandonAudit",isSuccess,message, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
}
