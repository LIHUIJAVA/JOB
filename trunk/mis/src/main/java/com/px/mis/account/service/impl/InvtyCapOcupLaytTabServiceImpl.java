package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyCapOcupLaytTabDao;
import com.px.mis.account.entity.InvtyCapOcupLaytTab;
import com.px.mis.account.service.InvtyCapOcupLaytTabService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//����ʽ�ռ�ù滮��
@Service
@Transactional
public class InvtyCapOcupLaytTabServiceImpl implements InvtyCapOcupLaytTabService {
	@Autowired
	private InvtyCapOcupLaytTabDao invtyCapOcupLaytTabDao;
	@Autowired
	private DeptDocDao deptDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private InvtyClsDao invtyClsDao;
	@Autowired
	private WhsDocMapper WhsDocDao;
	@Override
	public ObjectNode insertInvtyCapOcupLaytTab(InvtyCapOcupLaytTab invtyCapOcupLaytTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		/*if(deptDocDao.selectDeptDocByDeptEncd(invtyCapOcupLaytTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "���ű��"+invtyCapOcupLaytTab.getDeptId()+"�����ڣ�����ʧ�ܣ�");
		}else*/ if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyCapOcupLaytTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+invtyCapOcupLaytTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCapOcupLaytTab.getInvtyBigClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "����������"+invtyCapOcupLaytTab.getInvtyBigClsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(WhsDocDao.selectWhsDoc(invtyCapOcupLaytTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ֿ����"+invtyCapOcupLaytTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = invtyCapOcupLaytTabDao.insertInvtyCapOcupLaytTab(invtyCapOcupLaytTab);
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
	public ObjectNode updateInvtyCapOcupLaytTabByOrdrNum(InvtyCapOcupLaytTab invtyCapOcupLaytTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyCapOcupLaytTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,��������Ϊ��");
		}else if(invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "���"+ordrNum+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(invtyCapOcupLaytTab.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+invtyCapOcupLaytTab.getOrdrNum()+"�����ڣ�");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyCapOcupLaytTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "���ű��"+invtyCapOcupLaytTab.getDeptId()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyCapOcupLaytTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+invtyCapOcupLaytTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCapOcupLaytTab.getInvtyBigClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "����������"+invtyCapOcupLaytTab.getInvtyBigClsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(WhsDocDao.selectWhsDoc(invtyCapOcupLaytTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�ֿ����"+invtyCapOcupLaytTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = invtyCapOcupLaytTabDao.updateInvtyCapOcupLaytTabByOrdrNum(invtyCapOcupLaytTab);
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
	public ObjectNode deleteInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = invtyCapOcupLaytTabDao.deleteInvtyCapOcupLaytTabByOrdrNum(ordrNum);
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
	public InvtyCapOcupLaytTab selectInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum) {
		InvtyCapOcupLaytTab invtyCapOcupLaytTab = invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum);
		return invtyCapOcupLaytTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyCapOcupLaytTabList(Map map) {
		String resp="";
		List<InvtyCapOcupLaytTab> list = invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabList(map);
		int count = invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
