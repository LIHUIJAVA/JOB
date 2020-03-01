package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyAcctiMakDocInvtySubjDao;
import com.px.mis.account.entity.InvtyAcctiMakDocInvtySubj;
import com.px.mis.account.service.InvtyAcctiMakDocInvtySubjService;
import com.px.mis.purc.dao.MeasrCorpDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class InvtyAcctiMakDocInvtySubjServiceImpl implements InvtyAcctiMakDocInvtySubjService {
	@Autowired
	private InvtyAcctiMakDocInvtySubjDao invtyAcctiMakDocInvtySubjDao;
	@Autowired
	private MeasrCorpDocDao measrCorpDocDao;
	@Override
	public ObjectNode insertInvtyAcctiMakDocInvtySubj(InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(invtyAcctiMakDocInvtySubj.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "������λ���"+invtyAcctiMakDocInvtySubj.getMeasrCorpId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = invtyAcctiMakDocInvtySubjDao.insertInvtyAcctiMakDocInvtySubj(invtyAcctiMakDocInvtySubj);
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
	public ObjectNode updateInvtyAcctiMakDocInvtySubjByOrdrNum(InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyAcctiMakDocInvtySubj.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,��������Ϊ��");
		}else if(invtyAcctiMakDocInvtySubjDao.selectInvtyAcctiMakDocInvtySubjByOrdrNum(invtyAcctiMakDocInvtySubj.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+invtyAcctiMakDocInvtySubj.getOrdrNum()+"�����ڣ�");
		}else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(invtyAcctiMakDocInvtySubj.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "������λ���"+invtyAcctiMakDocInvtySubj.getMeasrCorpId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = invtyAcctiMakDocInvtySubjDao.updateInvtyAcctiMakDocInvtySubjByOrdrNum(invtyAcctiMakDocInvtySubj);
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
	public ObjectNode deleteInvtyAcctiMakDocInvtySubjByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyAcctiMakDocInvtySubjDao.selectInvtyAcctiMakDocInvtySubjByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = invtyAcctiMakDocInvtySubjDao.deleteInvtyAcctiMakDocInvtySubjByOrdrNum(ordrNum);
			if(deleteResult==1) {
				on.put("isSuccess", true);
				on.put("message", "ɾ���ɹ�");
			}else if(deleteResult==0){
				on.put("isSuccess", true);
				on.put("message", "û��Ҫɾ��������");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "ɾ��ʧ��");
			}
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public InvtyAcctiMakDocInvtySubj selectInvtyAcctiMakDocInvtySubjByOrdrNum(Integer ordrNum) {
		InvtyAcctiMakDocInvtySubj invtyAcctiMakDocInvtySubj = invtyAcctiMakDocInvtySubjDao.selectInvtyAcctiMakDocInvtySubjByOrdrNum(ordrNum);
		return invtyAcctiMakDocInvtySubj;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyAcctiMakDocInvtySubjList(Map map) {
		String resp="";
		List<InvtyAcctiMakDocInvtySubj> list = invtyAcctiMakDocInvtySubjDao.selectInvtyAcctiMakDocInvtySubjList(map);
		int count = invtyAcctiMakDocInvtySubjDao.selectInvtyAcctiMakDocInvtySubjCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyAcctiMakDocInvtySubj/selectInvtyAcctiMakDocInvtySubj", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
