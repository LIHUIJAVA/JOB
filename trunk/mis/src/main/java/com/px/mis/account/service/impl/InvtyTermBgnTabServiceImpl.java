package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyTermBgnTabDao;
import com.px.mis.account.entity.InvtyTermBgnTab;
import com.px.mis.account.service.InvtyTermBgnTabService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//����ڳ���ʵ����
@Service
@Transactional
public class InvtyTermBgnTabServiceImpl implements InvtyTermBgnTabService {
	@Autowired
	private InvtyTermBgnTabDao invtyTermBgnTabDao;
	@Autowired
	private WhsDocMapper whsDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private DeptDocDao deptDocDao;
	@Override
	public ObjectNode insertInvtyTermBgnTab(InvtyTermBgnTab invtyTermBgnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(whsDocDao.selectWhsDoc(invtyTermBgnTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ֿ���"+invtyTermBgnTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyTermBgnTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+invtyTermBgnTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyTermBgnTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "���ű���"+invtyTermBgnTab.getDeptId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = invtyTermBgnTabDao.insertInvtyTermBgnTab(invtyTermBgnTab);
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
	public ObjectNode updateInvtyTermBgnTabByOrdrNum(InvtyTermBgnTab invtyTermBgnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = invtyTermBgnTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,��������Ϊ��");
		}else if(invtyTermBgnTabDao.selectInvtyTermBgnTabByOrdrNum(invtyTermBgnTab.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+invtyTermBgnTab.getOrdrNum()+"�����ڣ�");
		}else if(whsDocDao.selectWhsDoc(invtyTermBgnTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ֿ���"+invtyTermBgnTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyTermBgnTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+invtyTermBgnTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyTermBgnTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "���ű���"+invtyTermBgnTab.getDeptId()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = invtyTermBgnTabDao.updateInvtyTermBgnTabByOrdrNum(invtyTermBgnTab);
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
	public ObjectNode deleteInvtyTermBgnTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (invtyTermBgnTabDao.selectInvtyTermBgnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = invtyTermBgnTabDao.deleteInvtyTermBgnTabByOrdrNum(ordrNum);
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
	public InvtyTermBgnTab selectInvtyTermBgnTabByOrdrNum(Integer ordrNum) {
		InvtyTermBgnTab invtyTermBgnTab = invtyTermBgnTabDao.selectInvtyTermBgnTabByOrdrNum(ordrNum);
		return invtyTermBgnTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyTermBgnTabList(Map map) {
		String resp="";
		List<InvtyTermBgnTab> list = invtyTermBgnTabDao.selectInvtyTermBgnTabList(map);
		int count = invtyTermBgnTabDao.selectInvtyTermBgnTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyTermBgnTab/selectInvtyTermBgnTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
