package com.px.mis.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.PursInvSubTabDao;
import com.px.mis.account.entity.PursInvSubTab;
import com.px.mis.account.service.PursInvSubTabService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.JacksonUtil;
//�ɹ���Ʊ�ӱ�
@Service
@Transactional
public class PursInvSubTabServiceImpl implements PursInvSubTabService {
	@Autowired
	private PursInvSubTabDao pursInvSubTabDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Override
	public ObjectNode insertPursInvSubTab(PursInvSubTab pursInvSubTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(invtyDocDao.selectInvtyDocByInvtyDocEncd(pursInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ô������"+pursInvSubTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}/*else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(pursInvSubTab.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "�ü�����λ"+pursInvSubTab.getMeasrCorpId()+"�����ڣ�����ʧ�ܣ�");
		}*/else {
			int a = pursInvSubTabDao.insertPursInvSubTab(pursInvSubTab);
			if(a>=0) {
				on.put("isSuccess", true);
				on.put("message", "�ɹ���Ʊ�����ɹ�");
			}else {
				on.put("isSuccess", true);
				on.put("message", "�ɹ���Ʊ����ʧ��");
			}
		}
		return on;
	}

	@Override
	public ObjectNode updatePursInvSubTabById(PursInvSubTab pursInvSubTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = pursInvSubTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "�ɹ���Ʊ�ӱ����ʧ��,��������Ϊ��");
		}else if(pursInvSubTabDao.selectPursInvSubTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "�ɹ���Ʊ�ӱ������"+ordrNum+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(pursInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ô������"+pursInvSubTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int a = pursInvSubTabDao.updatePursInvSubTabByOrdrNum(pursInvSubTab);
			if(a>=0) {
				on.put("isSuccess", true);
				on.put("message", "�ɹ���Ʊ���´���ɹ�");
			}else {
				on.put("isSuccess", true);
				on.put("message", "�ɹ���Ʊ���´���ʧ��");
			}
			
		}
		return on;
	}

	@Override
	public Integer deletePursInvSubTabByPursInvNum(String pursInvNum) {
		int deleteResult = pursInvSubTabDao.deletePursInvSubTabByOrdrNum(pursInvNum);		
		return deleteResult;
	}

	@Override
	public PursInvSubTab selectPursInvSubTabById(String vouchCateSubTabId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PursInvSubTab> selectPursInvSubTabList() {
		// TODO Auto-generated method stub
		return null;
	}

}
