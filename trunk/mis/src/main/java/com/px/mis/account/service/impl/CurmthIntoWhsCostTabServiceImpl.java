package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.CurmthIntoWhsCostTabDao;
import com.px.mis.account.entity.CurmthIntoWhsCostTab;
import com.px.mis.account.service.CurmthIntoWhsCostTabService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class CurmthIntoWhsCostTabServiceImpl implements CurmthIntoWhsCostTabService {
	@Autowired
	private CurmthIntoWhsCostTabDao curmthIntoWhsCostTabDao;
	@Autowired
	private com.px.mis.account.dao.PursInvSubTabDao pursInvSubTabDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Override
	public ObjectNode insertCurmthIntoWhsCostTab(CurmthIntoWhsCostTab curmthIntoWhsCostTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(pursInvSubTabDao.selectPursInvSubTabByOrdrNum(curmthIntoWhsCostTab.getPursInvSubTabId())==null){
			on.put("isSuccess", false);
			on.put("message", "�ɹ���Ʊ�ӱ�id"+curmthIntoWhsCostTab.getPursInvSubTabId()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(curmthIntoWhsCostTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+curmthIntoWhsCostTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = curmthIntoWhsCostTabDao.insertCurmthIntoWhsCostTab(curmthIntoWhsCostTab);
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
	public ObjectNode updateCurmthIntoWhsCostTabByOrdrNum(CurmthIntoWhsCostTab curmthIntoWhsCostTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = curmthIntoWhsCostTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ���������Ϊnull������");
		}else if (curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else if(pursInvSubTabDao.selectPursInvSubTabByOrdrNum(curmthIntoWhsCostTab.getPursInvSubTabId())==null){
			on.put("isSuccess", false);
			on.put("message", "�ɹ���Ʊ�ӱ�id"+curmthIntoWhsCostTab.getPursInvSubTabId()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(curmthIntoWhsCostTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+curmthIntoWhsCostTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = curmthIntoWhsCostTabDao.updateCurmthIntoWhsCostTabById(curmthIntoWhsCostTab);
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
	public ObjectNode deleteCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = curmthIntoWhsCostTabDao.deleteCurmthIntoWhsCostTabByOrdrNum(ordrNum);
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
	public CurmthIntoWhsCostTab selectCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum) {
		CurmthIntoWhsCostTab curmthIntoWhsCostTab = curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabByOrdrNum(ordrNum);
		return curmthIntoWhsCostTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectCurmthIntoWhsCostTabList(Map map) {
		String resp="";
		List<CurmthIntoWhsCostTab> list = curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabList(map);
		int count = curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
