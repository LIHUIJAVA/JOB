package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.GoodsActivityMiddleDao;
import com.px.mis.ec.dao.ProActivitysDao;
import com.px.mis.ec.entity.GoodsActivityMiddle;
import com.px.mis.ec.service.GoodsActivityMiddleService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class GoodsActivityMiddleServiceImpl implements GoodsActivityMiddleService {
	@Autowired
	private GoodsActivityMiddleDao goodsActivityMiddleDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private ProActivitysDao proActivitysDao;
	@Override
	public ObjectNode insertGoodsActivityMiddle(GoodsActivityMiddle goodsActivityMiddle) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(invtyDocDao.selectInvtyDocByInvtyDocEncd(goodsActivityMiddle.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货档案中存货编码"+goodsActivityMiddle.getInvtyEncd()+"不存在，新增失败！");
		}else if(proActivitysDao.selectById(goodsActivityMiddle.getSublistNo())==null){
			on.put("isSuccess", false);
			on.put("message", "促销活动子表序号"+goodsActivityMiddle.getSublistNo()+"不存在，新增失败！");
		}else if(goodsActivityMiddleDao.selectGoodsActivityMiddleByPriority(goodsActivityMiddle.getPriority())!=null){
			on.put("isSuccess", false);
			on.put("message", "商品活动中间表的优先级"+goodsActivityMiddle.getPriority()+"已存在，优先级不能重复，新增失败！");
		}else {
			int insertResult = goodsActivityMiddleDao.insertGoodsActivityMiddle(goodsActivityMiddle);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "新增失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updateGoodsActivityMiddleById(GoodsActivityMiddle goodsActivityMiddle) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		GoodsActivityMiddle goodsActivityMiddleOne = goodsActivityMiddleDao.selectGoodsActivityMiddleByPriority(goodsActivityMiddle.getPriority());
		if (goodsActivityMiddleDao.selectGoodsActivityMiddleById(goodsActivityMiddle.getNo())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+goodsActivityMiddle.getNo()+"不存在！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(goodsActivityMiddle.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货档案中存货编码"+goodsActivityMiddle.getInvtyEncd()+"不存在，新增失败！");
		}else if(proActivitysDao.selectById(goodsActivityMiddle.getSublistNo())==null){
			on.put("isSuccess", false);
			on.put("message", "促销活动子表序号"+goodsActivityMiddle.getSublistNo()+"不存在，新增失败！");
		}else if(goodsActivityMiddleOne!=null && goodsActivityMiddleOne.getNo()!=goodsActivityMiddle.getNo()){
			on.put("isSuccess", false);
			on.put("message", "商品活动中间表的优先级"+goodsActivityMiddle.getPriority()+"已存在，优先级不能重复，新增失败！");
		}else {
			int updateResult = goodsActivityMiddleDao.updateGoodsActivityMiddleById(goodsActivityMiddle);
			if(updateResult==1) {
				on.put("isSuccess", true);
				on.put("message", "更新成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "更新失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteGoodsActivityMiddleById(Integer no) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (goodsActivityMiddleDao.selectGoodsActivityMiddleById(no)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，序号"+no+"不存在！");
		}else {
			int deleteResult = goodsActivityMiddleDao.deleteGoodsActivityMiddleById(no);
			if(deleteResult==1) {
				on.put("isSuccess", true);
				on.put("message", "处理成功");
			}else if(deleteResult==0){
				on.put("isSuccess", true);
				on.put("message", "没有要删除的数据");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "处理失败");
			}
		}
			
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public GoodsActivityMiddle selectGoodsActivityMiddleById(Integer no) {
		GoodsActivityMiddle goodsActivityMiddle = goodsActivityMiddleDao.selectGoodsActivityMiddleById(no);
		return goodsActivityMiddle;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectGoodsActivityMiddleList(Map map) {
		String resp="";
		List<GoodsActivityMiddle> list = goodsActivityMiddleDao.selectGoodsActivityMiddleList(map);
		int count = goodsActivityMiddleDao.selectGoodsActivityMiddleCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
