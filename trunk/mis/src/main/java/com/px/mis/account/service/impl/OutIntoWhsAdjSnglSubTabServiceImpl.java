package com.px.mis.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.OutIntoWhsAdjSnglSubTabDao;
import com.px.mis.account.entity.OutIntoWhsAdjSnglSubTab;
import com.px.mis.account.service.OutIntoWhsAdjSnglSubTabService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//�����������ӱ�
@Service
@Transactional
public class OutIntoWhsAdjSnglSubTabServiceImpl implements OutIntoWhsAdjSnglSubTabService {
	@Autowired
	private OutIntoWhsAdjSnglSubTabDao outIntoWhsAdjSnglSubTabDao;
	@Autowired
	private WhsDocMapper whsDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Override
	public ObjectNode insertOutIntoWhsAdjSnglSubTab(OutIntoWhsAdjSnglSubTab outIntoWhsAdjSnglSubTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(whsDocDao.selectWhsDoc(outIntoWhsAdjSnglSubTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�����������вֿ����"+outIntoWhsAdjSnglSubTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(outIntoWhsAdjSnglSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�����������д������"+outIntoWhsAdjSnglSubTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(outIntoWhsAdjSnglSubTab);
			on.put("isSuccess", true);
			on.put("message", "�����ɹ�");
		}
		return on;
	}

	@Override
	public ObjectNode updateOutIntoWhsAdjSnglSubTabById(OutIntoWhsAdjSnglSubTab outIntoWhsAdjSnglSubTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = outIntoWhsAdjSnglSubTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "�����������ӱ�����ʧ��,��������Ϊ��");
		}else if(whsDocDao.selectWhsDoc(outIntoWhsAdjSnglSubTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�����������ӱ��вֿ����"+outIntoWhsAdjSnglSubTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(outIntoWhsAdjSnglSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�����������ӱ��д������"+outIntoWhsAdjSnglSubTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}/*else {
			outIntoWhsAdjSnglSubTabDao.updateOutIntoWhsAdjSnglSubTabByOrdrNum(outIntoWhsAdjSnglSubTab);
			on.put("isSuccess", true);
			on.put("message", "���´���ɹ�");
		}*/
		return on;
	}

	@Override
	public Integer deleteOutIntoWhsAdjSnglSubTabByFormNum(String formNum) {
		int deleteResult = outIntoWhsAdjSnglSubTabDao.deleteOutIntoWhsAdjSnglSubTabByFormNum(formNum);		
		return deleteResult;
	}

}
