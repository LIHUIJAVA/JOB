package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.ec.dao.ProConditionDao;
import com.px.mis.ec.entity.ProCondition;
import com.px.mis.ec.service.ProConditionService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class ProConditionServiceImpl implements ProConditionService {
	@Autowired
	private ProConditionDao proConditionDao;
	@Override
	public ObjectNode insertProCondition(ProCondition proCondition) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(proConditionDao.selectProConditionByProConditionEncd(proCondition.getProConditionEncd())!=null){
			on.put("isSuccess", false);
			on.put("message", "��Ʒ��ʽ����"+proCondition.getProConditionEncd()+"�Ѿ����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = proConditionDao.insertProCondition(proCondition);
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
	public ObjectNode updateProConditionById(ProCondition proCondition) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		ProCondition proConditionOne = proConditionDao.selectProConditionByProConditionEncd(proCondition.getProConditionEncd());
		if (proConditionDao.selectProConditionById(proCondition.getNo())==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+proCondition.getNo()+"�����ڣ�");
		}else if(proConditionOne!=null && proConditionOne.getNo()!=proCondition.getNo()){
			on.put("isSuccess", false);
			on.put("message", "��Ʒ��ʽ����"+proCondition.getProConditionEncd()+"�Ѿ����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = proConditionDao.updateProConditionById(proCondition);
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
	public ObjectNode deleteProConditionById(Integer no) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (proConditionDao.selectProConditionById(no)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+no+"�����ڣ�");
		}else {
			int deleteResult = proConditionDao.deleteProConditionById(no);
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
	public ProCondition selectProConditionById(Integer no) {
		ProCondition proCondition = proConditionDao.selectProConditionById(no);
		return proCondition;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectProConditionList(Map map) {
		String resp="";
		List<ProCondition> list = proConditionDao.selectProConditionList(map);
		int count = proConditionDao.selectProConditionCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/ec/proCondition/selectProCondition", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
