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
			on.put("message", "��������д������"+goodsActivityMiddle.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(proActivitysDao.selectById(goodsActivityMiddle.getSublistNo())==null){
			on.put("isSuccess", false);
			on.put("message", "������ӱ����"+goodsActivityMiddle.getSublistNo()+"�����ڣ�����ʧ�ܣ�");
		}else if(goodsActivityMiddleDao.selectGoodsActivityMiddleByPriority(goodsActivityMiddle.getPriority())!=null){
			on.put("isSuccess", false);
			on.put("message", "��Ʒ��м������ȼ�"+goodsActivityMiddle.getPriority()+"�Ѵ��ڣ����ȼ������ظ�������ʧ�ܣ�");
		}else {
			int insertResult = goodsActivityMiddleDao.insertGoodsActivityMiddle(goodsActivityMiddle);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
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
			on.put("message", "����ʧ�ܣ����"+goodsActivityMiddle.getNo()+"�����ڣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(goodsActivityMiddle.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "��������д������"+goodsActivityMiddle.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(proActivitysDao.selectById(goodsActivityMiddle.getSublistNo())==null){
			on.put("isSuccess", false);
			on.put("message", "������ӱ����"+goodsActivityMiddle.getSublistNo()+"�����ڣ�����ʧ�ܣ�");
		}else if(goodsActivityMiddleOne!=null && goodsActivityMiddleOne.getNo()!=goodsActivityMiddle.getNo()){
			on.put("isSuccess", false);
			on.put("message", "��Ʒ��м������ȼ�"+goodsActivityMiddle.getPriority()+"�Ѵ��ڣ����ȼ������ظ�������ʧ�ܣ�");
		}else {
			int updateResult = goodsActivityMiddleDao.updateGoodsActivityMiddleById(goodsActivityMiddle);
			if(updateResult==1) {
				on.put("isSuccess", true);
				on.put("message", "���³ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
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
			on.put("message", "ɾ��ʧ�ܣ����"+no+"�����ڣ�");
		}else {
			int deleteResult = goodsActivityMiddleDao.deleteGoodsActivityMiddleById(no);
			if(deleteResult==1) {
				on.put("isSuccess", true);
				on.put("message", "����ɹ�");
			}else if(deleteResult==0){
				on.put("isSuccess", true);
				on.put("message", "û��Ҫɾ��������");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
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
			resp=BaseJson.returnRespList("/", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
