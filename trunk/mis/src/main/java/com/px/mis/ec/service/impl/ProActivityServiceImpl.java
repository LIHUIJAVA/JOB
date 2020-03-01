package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.px.mis.ec.dao.ProActivityDao;
import com.px.mis.ec.dao.ProActivitysDao;
import com.px.mis.ec.entity.ProActivity;
import com.px.mis.ec.entity.ProActivitys;
import com.px.mis.ec.service.ProActivityService;
import com.px.mis.util.BaseJson;

@Transactional
@Service
public class ProActivityServiceImpl implements ProActivityService {

	private Logger logger = LoggerFactory.getLogger(ProActivityServiceImpl.class);

	@Autowired
	private ProActivityDao proActDao;

	@Autowired
	private ProActivitysDao proActsDao;

	@Override
	public String add(ProActivity proAct, List<ProActivitys> proActsList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			if (proActDao.select(proAct.getProActId()) != null) {
				message = "编号" + proAct.getProActId() + "已存在，请重新输入！";
				isSuccess = false;
			} else {
				if (proActsDao.select(proAct.getProActId()) != null) {
					proActsDao.delete(proAct.getProActId());
				}
				proActDao.insert(proAct);
				List<ProActivitys> proActsLists = new ArrayList<ProActivitys>();
				for (ProActivitys proActivitys : proActsList) {
					if (proActivitys.getAllGoods() == 1) {
						proActsLists.add(proActivitys);
					} else {
						String goodsRange = proActivitys.getGoodsRange();
						String[] split = goodsRange.split(",");// 参加活动的商品范围是商品编码用逗号隔开的字符串；
						for (String invtyEncd : split) {
							ProActivitys activitys = new ProActivitys();
							BeanUtils.copyProperties(proActivitys, activitys);
							System.err.println("1" + proActivitys);
							System.err.println("2" + activitys);
							activitys.setGoodsRange(invtyEncd); // 商品范围
							proActsLists.add(activitys);
						}
					}
				}
				proActsDao.insert(proActsLists);

				// ============zds 往商品活动中间表中添加数据；
//				for(ProActivitys proActivitys:proActsList) {
//					Integer no = proActivitys.getNo();
//					Integer allGoods = proActivitys.getAllGoods();//是否全部商品参加活动，1：是；0：否
//					if(allGoods==1) {
//						GoodsActivityMiddle goodsActivityMiddle=new GoodsActivityMiddle();
//						goodsActivityMiddle.setAllGoods(1);//是否全部商品
//						goodsActivityMiddle.setInvtyEncd("all");//存货编码 写成固定的，表示本店铺全部的商品都参加促销活动
//						goodsActivityMiddle.setNo(proActivitys.getNo());//促销活动子表序号
//						goodsActivityMiddle.setStoreId(proAct.getTakeStore());//参加活动的店铺编码
//						goodsActivityMiddle.setStartTime(proAct.getStartDate());//活动起始日期
//						goodsActivityMiddle.setEndTime(proAct.getEndDate());//活动结束日期；
//						goodsActivityMiddleDao.insertGoodsActivityMiddle(goodsActivityMiddle);
//					}else if(allGoods==0){
//						String goodsRange = proActivitys.getGoodsRange();
//						String[] split = goodsRange.split(",");//参加活动的商品范围是商品编码用逗号隔开的字符串；
//						for(String invtyEncd:split) {
//							GoodsActivityMiddle goodsActivityMiddle=new GoodsActivityMiddle();
//							goodsActivityMiddle.setAllGoods(0);//是否全部商品
//							goodsActivityMiddle.setInvtyEncd(invtyEncd);//存货编码
//							goodsActivityMiddle.setNo(proActivitys.getNo());//促销活动子表序号
//							goodsActivityMiddle.setStoreId(proAct.getTakeStore());//参加活动的店铺编码
//							goodsActivityMiddle.setStartTime(proAct.getStartDate());//活动起始日期
//							goodsActivityMiddle.setEndTime(proAct.getEndDate());//活动结束日期；
//							goodsActivityMiddleDao.insertGoodsActivityMiddle(goodsActivityMiddle);
//						}
//					}
//				}
//				//====================zds===========================

				message = "新增成功！";
				isSuccess = true;
			}
			resp = BaseJson.returnRespObj("ec/proActivity/add", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/proActivity/add 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String edit(ProActivity proAct, List<ProActivitys> proActsList) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ProActivity proActs = proActDao.select(proAct.getProActId());
			if (proActs == null || proActs.getAuditResult().equals("1")) {
				resp = BaseJson.returnRespObj("ec/proActivity/edit", false, "单据已审核", null);
				return resp;
			}
			proActsDao.delete(proAct.getProActId());
			proActDao.update(proAct);
//            proActsDao.insert(proActsList);
			List<ProActivitys> proActsLists = new ArrayList<ProActivitys>();
			for (ProActivitys proActivitys : proActsList) {
				if (proActivitys.getAllGoods() == 1) {
					proActsLists.add(proActivitys);
				} else {
					String goodsRange = proActivitys.getGoodsRange();
					String[] split = goodsRange.split(",");// 参加活动的商品范围是商品编码用逗号隔开的字符串；
					for (String invtyEncd : split) {
						ProActivitys activitys = new ProActivitys();
						BeanUtils.copyProperties(proActivitys, activitys);
						activitys.setGoodsRange(invtyEncd); // 商品范围
						proActsLists.add(activitys);
					}
				}
			}
			proActsDao.insert(proActsLists);
			message = "更新成功！";
			resp = BaseJson.returnRespObj("ec/proActivity/edit", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/proActivity/edit 异常说明：", e);
			throw new RuntimeException();
		}
		return resp;
	}

	@Override
	public String delete(String proActId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			ProActivity proActs = proActDao.select(proActId);
			if (proActs == null || proActs.getAuditResult().equals("1")) {
				resp = BaseJson.returnRespObj("ec/proActivity/delete", false, "促销活动已审核", null);
				return resp;
			}
			if (proActDao.select(proActId) != null) {
				proActDao.delete(proActId);
				isSuccess = true;
				message = "删除成功！";
			} else {
				isSuccess = false;
				message = "编号" + proActId + "促销活动不存在！";
			}
			resp = BaseJson.returnRespObj("ec/proActivity/delete", isSuccess, message, null);
		} catch (IOException e) {
			logger.error("URL：ec/proActivity/delete 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String query(String proActId) {
		String message = "";
		Boolean isSuccess = true;
		String resp = "";
		try {
			List<ProActivitys> proActsList = new ArrayList<>();
			ProActivity proAct = proActDao.select(proActId);
			if (proAct != null) {
				proActsList = proActsDao.select(proActId);
				message = "查询成功！";
			} else {
				isSuccess = false;
				message = "编号" + proActId + "促销活动不存在！";
			}
			resp = BaseJson.returnRespObjList("ec/proActivity/query", isSuccess, message, proAct, proActsList);
		} catch (IOException e) {
			logger.error("URL：ec/proActivity/query 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String queryList(Map map) {
		String resp = "";
		try {
			List<ProActivity> proList = proActDao.selectList(map);
			int count = proActDao.selectCount(map);
			int pageNo = Integer.parseInt(map.get("pageNo").toString());
			int pageSize = Integer.parseInt(map.get("pageSize").toString());
			resp = BaseJson.returnRespList("ec/proActivity/queryList", true, "查询成功！", count, pageNo, pageSize, 0, 0,
					proList);
		} catch (IOException e) {
			logger.error("URL：ec/proActivity/queryList 异常说明：", e);
		}
		return resp;
	}

	@Override
	public String updateAudit(List<ProActivity> activities) {
		String resp = "";
		try {
			String userName = null;
			String auditResult = null;
			String auditDate = null;
			for (ProActivity proActId : activities) {
				userName = proActId.getAuditor();
				auditDate = proActId.getAuditDate();
				auditResult = proActId.getAuditResult();
			}
			List<ProActivity> activities2 = new ArrayList<ProActivity>();
			for (ProActivity proActId : activities) {
				ProActivity proAct = proActDao.select(proActId.getProActId());
				if (proAct != null && proAct.getAuditResult().equals("0")) {
					proAct.setAuditDate(auditDate);
					proAct.setAuditor(userName);
					proAct.setAuditResult(auditResult);

					activities2.add(proAct);
				}
			}
			if (activities2.size() > 0) {
				int i = proActDao.updateAudit(activities2);
				if (i == 0) {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", false, "审核失败", null);
				} else {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", true, "审核成功！", null);
				}
			} else {
				resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", false, "单据已审核，审核失败", null);
			}

		} catch (IOException e) {
			logger.error("URL：ec/proActivity/updateAuditResult 异常说明：", e);
		}
		return resp;
		// TODO Auto-generated method stub
	}

	@Override
	public String updateAuditNo(List<ProActivity> activities) {
		String resp = "";
		try {
			String userName = null;
			String auditResult = null;
			String auditDate = null;
			for (ProActivity proActId : activities) {
				userName = proActId.getAuditor();
				auditDate = proActId.getAuditDate();
				auditResult = proActId.getAuditResult();
			}

			List<ProActivity> activities2 = new ArrayList<ProActivity>();
			for (ProActivity proActId : activities) {
				ProActivity proAct = proActDao.select(proActId.getProActId());
				if (proAct != null && proAct.getAuditResult().equals("1")) {
					proAct.setAuditDate(auditDate);
					proAct.setAuditor(userName);
					proAct.setAuditResult(auditResult);

					activities2.add(proAct);
				}
			}
			if (activities2.size() > 0) {
				int i = proActDao.updateAudit(activities);
				if (i == 0) {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResultNo", false, "弃审失败", null);
				} else {
					resp = BaseJson.returnResp("ec/proActivity/updateAuditResultNo", true, "弃审成功！", null);
				}
			} else {
				resp = BaseJson.returnResp("ec/proActivity/updateAuditResult", false, "单据已弃审，弃审失败", null);
			}
		} catch (IOException e) {
			logger.error("URL：ec/proActivity/updateAuditResultNo 异常说明：", e);
		}
		return resp;
	}

}
