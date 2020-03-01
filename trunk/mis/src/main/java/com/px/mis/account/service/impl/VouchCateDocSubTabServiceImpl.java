package com.px.mis.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.VouchCateDocSubTabDao;
import com.px.mis.account.entity.VouchCateDocSubTab;
import com.px.mis.account.service.VouchCateDocSubTabService;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class VouchCateDocSubTabServiceImpl implements VouchCateDocSubTabService {
	
	@Autowired
	private VouchCateDocSubTabDao vouchCateDocSubTabDao;
	@Override
	public ObjectNode insertVouchCateSubTabDoc(VouchCateDocSubTab vouchCateSubTabDoc) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		vouchCateDocSubTabDao.insertVouchCateSubTabDoc(vouchCateSubTabDoc);
		on.put("isSuccess", true);
		on.put("message", "��������ɹ�");
		return on;
	}
	@Override
	public ObjectNode updateVouchCateDocSubTabById(VouchCateDocSubTab vouchCateSubTabDoc) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = vouchCateSubTabDoc.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "�ӱ����ʧ��,��������Ϊ��");
		}else if(vouchCateDocSubTabDao.selectVouchCateDocSubTabById(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "�ӱ�������"+ordrNum+"�����ڣ�����ʧ�ܣ�");
		}else {
			vouchCateDocSubTabDao.updateVouchCateDocSubTabById(vouchCateSubTabDoc);
			on.put("isSuccess", true);
			on.put("message", "���´���ɹ�");
		}
		return on;
	}

	@Override
	public Integer deleteVouchCateDocSubTabByVouchCateWor(String vouchCateWor) {
		int deleteResult = vouchCateDocSubTabDao.deleteVouchCateDocSubTabByVouchCateWor(vouchCateWor);		
		return deleteResult;
	}

	@Override
	public VouchCateDocSubTab selectVouchCateDocSubTabById(String vouchCateSubTabId) {
		return null;
	}

	@Override
	public List<VouchCateDocSubTab> selectVouchCateDocSubTabList() {
		return null;
	}

}
