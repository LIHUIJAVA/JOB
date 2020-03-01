package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.px.mis.ec.dao.AuditStrategyDao;
import com.px.mis.ec.dao.GoodRecordDao;
import com.px.mis.ec.dao.PlatOrderDao;
import com.px.mis.ec.dao.StoreRecordDao;
import com.px.mis.ec.dao.StrategyGoodsDao;
import com.px.mis.ec.entity.AuditStrategy;
import com.px.mis.ec.entity.BusinessType;
import com.px.mis.ec.entity.GoodRecord;
import com.px.mis.ec.entity.PlatOrder;
import com.px.mis.ec.entity.PlatOrders;
import com.px.mis.ec.entity.ProActivitys;
import com.px.mis.ec.entity.StoreRecord;
import com.px.mis.ec.entity.StrategyGoods;
import com.px.mis.ec.service.AuditStrategyService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;

//@Transactional
@Service
public class AuditStrategyServiceImpl implements AuditStrategyService {

	private Logger logger = LoggerFactory.getLogger(AuditStrategyServiceImpl.class);
	
	@Autowired
	private AuditStrategyDao auditStrategyDao;
	@Autowired
	private StrategyGoodsDao strategyGoodsDao;
	@Autowired
	private StoreRecordDao storeRecordDao;
	@Autowired
	private GoodRecordDao goodRecordDao;
	@Autowired
	private PlatOrderDao platOrderDao;
	@Override
	@Transactional
	public String insert(String requestString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		List<StrategyGoods> strategyGoodslist=new ArrayList<>();
		AuditStrategy strategy=new AuditStrategy();
		try {
			//先增加主表的记录
			strategy = BaseJson.getPOJO(requestString, AuditStrategy.class);
			auditStrategyDao.insert(strategy);
			//将主表的id插入子表
			ArrayNode strategyGoodsIterator = (ArrayNode) BaseJson.getReqBody(requestString).get("list");
			if (strategyGoodsIterator.size()>0) {
				for (Iterator<JsonNode> it = strategyGoodsIterator.iterator(); it.hasNext();) {
					JsonNode strategys = it.next();
					StrategyGoods strategyGoods = JacksonUtil.getPOJO((ObjectNode) strategys, StrategyGoods.class);
					strategyGoods.setStrategyId(strategy.getId());
					strategyGoodslist.add(strategyGoods);
				}
				strategyGoodsDao.insert(strategyGoodslist);
			}
			message="保存成功";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/addStrategy 异常说明：转换实体时或保存时异常",e);
			isSuccess=false;
			message="保存失败，请重试";
		}
		try {
			resp=BaseJson.returnRespObj("ec/auditStrategy/addStrategy", isSuccess, message, strategy);
			//resp = BaseJson.returnRespObj("ec/auditStrategy/addStrategy", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/addStrategy 异常说明：封装resp时异常",e);
		}
		return resp;
	}
	@Override
	@Transactional
	public String delete(String reString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		try {
			AuditStrategy strategy = BaseJson.getPOJO(reString, AuditStrategy.class);
			int used = auditStrategyDao.checkUsedByStore(strategy.getId());
			if(used==0) {
				auditStrategyDao.deletezi(strategy);
				auditStrategyDao.deletezhu(strategy);
				message="删除成功";
				isSuccess=true;
			}else {
				message="要删除的免审策略已在店铺中使用，删除失败";
				isSuccess=false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess=false;
			logger.error("URL:ec/auditStrategy/delete 异常说明：转换实体时或保存时异常",e);
			message="删除失败";
		}
		try {
			resp = BaseJson.returnRespObj("ec/auditStrategy/delete", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/delete 异常说明：封装resp时异常",e);
		}
		
		return resp;
	}
	@Override
	@Transactional
	public String update(String requestString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		List<StrategyGoods> strategyGoodslist=new ArrayList<>();
		try {
			AuditStrategy strategy = BaseJson.getPOJO(requestString, AuditStrategy.class);
			//更新主表
			auditStrategyDao.update(strategy);
			//删除子表
			auditStrategyDao.deletezi(strategy);
			//插入子表
			ArrayNode strategyGoodsIterator = (ArrayNode) BaseJson.getReqBody(requestString).get("list");
			if (strategyGoodsIterator.size()>0) {
				for (Iterator<JsonNode> it = strategyGoodsIterator.iterator(); it.hasNext();) {
					JsonNode strategys = it.next();
					StrategyGoods strategyGoods = JacksonUtil.getPOJO((ObjectNode) strategys, StrategyGoods.class);
					strategyGoods.setStrategyId(strategy.getId());
					strategyGoodslist.add(strategyGoods);
				}
				if (strategyGoodslist.size()>0) {
					strategyGoodsDao.insert(strategyGoodslist);
				}
			}
			message="修改成功";
			isSuccess=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccess=false;
			logger.error("URL:ec/auditStrategy/delete 异常说明：转换实体时或保存时异常",e);
			message="修改失败";
		}
		try {
			resp = BaseJson.returnRespObj("ec/auditStrategy/delete", isSuccess, message, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/delete 异常说明：封装resp时异常",e);
		}
		
		return resp;
	}
	@Override
	@Transactional
	public String selectList(Map map) {
		// TODO Auto-generated method stub
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		List<AuditStrategy> strategylist;
		int count;
		int pageNo;
		int pageSize;
		int listNum;
		int pages;
		try {
			count = auditStrategyDao.selectCount(map);
			strategylist = auditStrategyDao.selectList(map);
			message = "查询成功";
			isSuccess = true;
			pageNo = Integer.parseInt(map.get("pageNo").toString());
			pageSize = Integer.parseInt(map.get("pageSize").toString());
			listNum = strategylist.size();
			pages = count / pageSize + 1;
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			isSuccess=false;
			message="查询失败，请重试。";
			count=0;
			pageNo = Integer.parseInt(map.get("pageNo").toString());
			pageSize = Integer.parseInt(map.get("pageSize").toString());
			listNum=0;
			pages=0;
			strategylist=null;
		}
		try {
			resp = BaseJson.returnRespList("ec/auditStrategy/selectList", isSuccess, message, count, pageNo, pageSize,
					listNum, pages, strategylist);
		} catch (IOException e) {
			logger.error("URL:ec/auditStrategy/selectList 异常说明：拼接resp异常", e);
		}
		return resp;
	}
	@Override
	@Transactional
	public String findById(String requestString) {
		// TODO Auto-generated method stub
		String resp = "";
		String message = "";
		boolean isSuccess = true;
		AuditStrategy strategy=new AuditStrategy();
		try {
			strategy=auditStrategyDao.findById(Integer.parseInt(BaseJson.getReqBody(requestString).get("id").asText()));
			message="查询成功";
			isSuccess=true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message="查询失败";
			isSuccess=false;
			logger.error("URL:ec/auditStrategy/findById 异常说明：获取参数或查询时失败", e);
		}
		try {
			resp=BaseJson.returnRespObj("ec/auditStrategy/findById", isSuccess, message, strategy);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("URL:ec/auditStrategy/findById 异常说明：拼接resp异常", e);
		}
		return resp;
	}
	/**
	 * 根据传入订单的主子表信息，
	 * 获取店铺档案中的审核策略id,
	 * 获取订单明细中的sku和spu在店铺商品档案中判断是否2次销售品
	 * 获取审核策略id并判断此订单是否满足对应店铺的免审设置，
	 * 满足免审设置时返回true，不满足免审设置时返回false
	 */
	@Override
	@Transactional
	public boolean autoAuditCheck(PlatOrder order, List<PlatOrders> orders) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i < orders.size(); i++) {
			if(StringUtils.isEmpty(orders.get(i).getGoodSku())&&StringUtils.isEmpty(orders.get(i).getGoodId())) {
				
			}else {
				//先循环明细是否含有二次销售的sku
				GoodRecord goodrecord = goodRecordDao.selectBySkuAndEcGoodId(orders.get(i).getGoodSku(), orders.get(i).getGoodId());
				if(goodrecord!=null) {
					if(goodrecord.getIsSecSale()==1) {
						order.setAuditHint("订单含有二次销售SKU，无法自动审核");
						platOrderDao.update(order);//更新订单的审核提示；
						return false;
					}
				}else {
					order.setAuditHint("sku:"+orders.get(i).getGoodSku()+"或spu:"+orders.get(i).getGoodId()+"未匹配到对应商品档案,无法自动审核");
					platOrderDao.update(order);//更新订单的审核提示；
					return false;
				}
			}
		}
		StoreRecord storeRecord = storeRecordDao.select(order.getStoreId());
		AuditStrategy auditStrategy = auditStrategyDao.findById(Integer.parseInt(storeRecord.getNoAuditId()));
		//判断审核策略是否免审,审核方式(0.免审；1.有条件免审；2.必审)
		if(auditStrategy.getAuditWay()==0) {
			return true;
		}else if(auditStrategy.getAuditWay()==1) {//有条件免审
			boolean flag = true;
			if(auditStrategy.getBuyerNoteNull()==1) {//买家留言非空
				if(order.getBuyerNote()!=null&&order.getBuyerNote().length()>0) {
					//有买家留言
					order.setAuditHint("订单有买家留言，无法自动审核");
					platOrderDao.update(order);//更新订单的审核提示；
					return false;
				}
			}else if(auditStrategy.getBuyerNote()==1) {
				//买家留言包含
				if(order.getBuyerNote()!=null&&order.getBuyerNote().length()>0) {
					List<StrategyGoods> strategyGoodsList = auditStrategy.getStrategyGoodsList();
					//条件方式，0：卖家留言包含字段，1：买家留言包含字段，2：包含sku
					for (int i = 0; i < strategyGoodsList.size(); i++) {
						if(strategyGoodsList.get(i).getVaType()==1) {
							if(order.getBuyerNote().indexOf(strategyGoodsList.get(i).getVa())>0) {
								flag=false;
								break;
							}
						}
					}
					//当flag=false时，返回false;
					if(!flag) {
						order.setAuditHint("买家留言包含所设置的关键字，无法自动审核");
						platOrderDao.update(order);//更新订单的审核提示；
						return false;
					}
				}
			}
			//卖家留言非空
			if(auditStrategy.getSellerNoteNull()==1) {
				//买家留言包含关键字
				if (StringUtils.isNotEmpty(order.getSellerNote())) {
					order.setAuditHint("订单有卖家留言，无法自动审核");
					platOrderDao.update(order);//更新订单的审核提示；
					return false;
				}
			}else if(auditStrategy.getSellerNote()==1) {
				//卖家留言包含关键字
				if(StringUtils.isNotEmpty(order.getSellerNote())) {
					List<StrategyGoods> strategyGoodsList = auditStrategy.getStrategyGoodsList();
					//条件方式，0：卖家留言包含字段，1：买家留言包含字段，2：包含sku
					for (int i = 0; i < strategyGoodsList.size(); i++) {
						if(strategyGoodsList.get(i).getVaType()==0) {
							if(order.getBuyerNote().indexOf(strategyGoodsList.get(i).getVa())>0) {
								flag=false;
								break;
							}
						}
					}
					//当flag=false时，返回false;
					if(!flag) {
						order.setAuditHint("卖家留言包含所设置的关键字，无法自动审核");
						platOrderDao.update(order);//更新订单的审核提示；
						return false;
					}
				}
			}
			if(auditStrategy.getIncludeSku()==1) {
				//订单包含特殊sku
				List<StrategyGoods> strategyGoodsList = auditStrategy.getStrategyGoodsList();
				//先将订单明细中的sku和spu拼接成String,用逗号隔开
				String skuspus = "";
				for (int i = 0; i < orders.size(); i++) {
					skuspus+=orders.get(i).getGoodSku()+","+orders.get(i).getGoodId()+",";
				}
				//条件方式，0：卖家留言包含字段，1：买家留言包含字段，2：包含sku
				for (int i = 0; i < strategyGoodsList.size(); i++) {
					if(strategyGoodsList.get(i).getVaType()==2) {
						if(skuspus.indexOf(strategyGoodsList.get(i).getVa())>0) {
							flag=false;
							break;
						}
					}
				}
				//当flag=false时，返回false;
				if(!flag) {
					order.setAuditHint("订单包含特殊sku或spu，无法自动审核");
					platOrderDao.update(order);//更新订单的审核提示；
					return false;
				}
			}
			return true;
		}else {
			//必审
			return false;
		}
	}

}
