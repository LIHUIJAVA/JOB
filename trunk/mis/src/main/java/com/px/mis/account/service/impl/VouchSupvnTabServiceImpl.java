package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.VouchSupvnTabDao;
import com.px.mis.account.entity.VouchSupvnTab;
import com.px.mis.account.service.VouchSupvnTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class VouchSupvnTabServiceImpl implements VouchSupvnTabService {
	@Autowired
	private VouchSupvnTabDao vouchSupvnTabDao;
	public ObjectNode insertVouchSupvnTab(VouchSupvnTab vouchSupvnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int insertResult = vouchSupvnTabDao.insertVouchSupvnTab(vouchSupvnTab);
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
	public ObjectNode updateVouchSupvnTabByOrdrNum(VouchSupvnTab vouchSupvnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = vouchSupvnTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ��,��������Ϊ��");
		}else if (vouchSupvnTabDao.selectVouchSupvnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "����ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int updateResult = vouchSupvnTabDao.updateVouchSupvnTabByOrdrNum(vouchSupvnTab);
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
	public ObjectNode deleteVouchSupvnTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (vouchSupvnTabDao.selectVouchSupvnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "ɾ��ʧ�ܣ����"+ordrNum+"�����ڣ�");
		}else {
			int deleteResult = vouchSupvnTabDao.deleteVouchSupvnTabByOrdrNum(ordrNum);
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
	public VouchSupvnTab selectVouchSupvnTabByOrdrNum(Integer ordrNum) {
		VouchSupvnTab vouchSupvnTab = vouchSupvnTabDao.selectVouchSupvnTabByOrdrNum(ordrNum);
		return vouchSupvnTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectVouchSupvnTabList(Map map) {
		String resp="";
		List<VouchSupvnTab> list = vouchSupvnTabDao.selectVouchSupvnTabList(map);
		int count = vouchSupvnTabDao.selectVouchSupvnTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", true, "��ѯ�ɹ���", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
