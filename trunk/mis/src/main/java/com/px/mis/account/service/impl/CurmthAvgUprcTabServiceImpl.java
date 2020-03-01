package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.CurmthAvgUprcTabDao;
import com.px.mis.account.entity.CurmthAvgUprcTab;
import com.px.mis.account.service.CurmthAvgUprcTabService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.service.InvtyDocService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class CurmthAvgUprcTabServiceImpl implements CurmthAvgUprcTabService {
	@Autowired
	private CurmthAvgUprcTabDao curmthAvgUprcTabDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Override
	public ObjectNode insertCurmthAvgUprcTab(CurmthAvgUprcTab curmthAvgUprcTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(invtyDocDao.selectInvtyDocByInvtyDocEncd(curmthAvgUprcTab.getInvtyEncd()) ==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+curmthAvgUprcTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int insertResult = curmthAvgUprcTabDao.insertCurmthAvgUprcTab(curmthAvgUprcTab);
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
	public ObjectNode updateCurmthAvgUprcTabByOrdrNum(CurmthAvgUprcTab curmthAvgUprcTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = curmthAvgUprcTab.getOrdrNum();
		if(ordrNum == null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ���������Ϊnull��");
		}else if (curmthAvgUprcTabDao.selectCurmthAvgUprcTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(curmthAvgUprcTab.getInvtyEncd()) ==null){
			on.put("isSuccess", false);
			on.put("message", "�������"+curmthAvgUprcTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}else {
			int updateResult = curmthAvgUprcTabDao.updateCurmthAvgUprcTabByOrdrNum(curmthAvgUprcTab);
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
	public ObjectNode deleteCurmthAvgUprcTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (curmthAvgUprcTabDao.selectCurmthAvgUprcTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = curmthAvgUprcTabDao.deleteCurmthAvgUprcTabByOrdrNum(ordrNum);
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
	public CurmthAvgUprcTab selectCurmthAvgUprcTabByOrdrNum(Integer ordrNum) {
		CurmthAvgUprcTab curmthAvgUprcTab = curmthAvgUprcTabDao.selectCurmthAvgUprcTabByOrdrNum(ordrNum);
		return curmthAvgUprcTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectCurmthAvgUprcTabList(Map map) {
		String resp="";
		List<CurmthAvgUprcTab> list = curmthAvgUprcTabDao.selectCurmthAvgUprcTabList(map);
		int count = curmthAvgUprcTabDao.selectCurmthAvgUprcTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/curmthAvgUprcTab/selectCurmthAvgUprcTab", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

	@Override
	public List<CurmthAvgUprcTab> getCurmthAvgUprcList(Map<String, String> parameters) {
		return curmthAvgUprcTabDao.getCurmthAvgUprcList(parameters);
	}

	
}
