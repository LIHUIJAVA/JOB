package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyDtlAcctDao;
import com.px.mis.account.entity.InvtyDtlAcct;
import com.px.mis.account.service.InvtyDtlAcctService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
//�����ϸ��
@Service
@Transactional
public class InvtyDtlAcctServiceImpl implements InvtyDtlAcctService {
	@Autowired
	private InvtyDtlAcctDao invtyDtlAcctDao;
	@Override
	public ObjectNode insertInvtyDtlAcct(InvtyDtlAcct invtyDtlAcct) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
			int insertResult = invtyDtlAcctDao.insertInvtyDtlAcct(invtyDtlAcct);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "�����ɹ�");
			}else {
				on.put("isSuccess", false);
				on.put("message", "����ʧ��");
			}
		return on;
	}

	@Override
	public ObjectNode updateInvtyDtlAcctByOrdrNum(InvtyDtlAcct invtyDtlAcct) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyDtlAcct.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,��������Ϊ��");
		}else if(invtyDtlAcctDao.selectInvtyDtlAcctByOrdrNum(invtyDtlAcct.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+invtyDtlAcct.getOrdrNum()+"�����ڣ�");
		}else {
			int updateResult = invtyDtlAcctDao.updateInvtyDtlAcctByOrdrNum(invtyDtlAcct);
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
	public ObjectNode deleteInvtyDtlAcctByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyDtlAcctDao.selectInvtyDtlAcctByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = invtyDtlAcctDao.deleteInvtyDtlAcctByOrdrNum(ordrNum);
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
	public InvtyDtlAcct selectInvtyDtlAcctByOrdrNum(Integer ordrNum) {
		InvtyDtlAcct invtyDtlAcct = invtyDtlAcctDao.selectInvtyDtlAcctByOrdrNum(ordrNum);
		return invtyDtlAcct;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyDtlAcctList(Map map) {
		String resp="";
		List<InvtyDtlAcct> list = invtyDtlAcctDao.selectInvtyDtlAcctList(map);
		int count = invtyDtlAcctDao.selectInvtyDtlAcctCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyDtlAcct/selectInvtyDtlAcct", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
