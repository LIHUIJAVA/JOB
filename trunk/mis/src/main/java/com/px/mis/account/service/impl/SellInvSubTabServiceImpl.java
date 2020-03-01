package com.px.mis.account.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.SellInvSubTabDao;
import com.px.mis.account.entity.SellInvSubTab;
import com.px.mis.account.service.SellInvSubTabService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.purc.dao.MeasrCorpDocDao;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//���۷�Ʊ�ӱ�
@Service
@Transactional
public class SellInvSubTabServiceImpl implements SellInvSubTabService {
	@Autowired
	private SellInvSubTabDao sellInvSubTabDao;
	@Autowired
	private WhsDocMapper whsDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private MeasrCorpDocDao measrCorpDocDao;
	@Override
	public ObjectNode insertSellInvSubTab(SellInvSubTab sellInvSubTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(whsDocDao.selectWhsDoc(sellInvSubTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ��вֿ���"+sellInvSubTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(sellInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ��д������"+sellInvSubTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}/*else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(sellInvSubTab.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ��м�����λ���"+sellInvSubTab.getMeasrCorpId()+"�����ڣ�����ʧ�ܣ�");
		}*/else {
			sellInvSubTabDao.insertSellInvSubTab(sellInvSubTab);
			on.put("isSuccess", true);
			on.put("message", "���۷�Ʊ��������ɹ�");
		}
		return on;
	}

	@Override
	public ObjectNode updateSellInvSubTabById(SellInvSubTab sellInvSubTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = sellInvSubTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ����ʧ��,��������Ϊ��");
		}else if(sellInvSubTabDao.selectSellInvSubTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ������"+ordrNum+"�����ڣ�����ʧ�ܣ�");
		}else if(whsDocDao.selectWhsDoc(sellInvSubTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ��вֿ���"+sellInvSubTab.getWhsEncd()+"�����ڣ�����ʧ�ܣ�");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(sellInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ��д������"+sellInvSubTab.getInvtyEncd()+"�����ڣ�����ʧ�ܣ�");
		}/*else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(sellInvSubTab.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "���۷�Ʊ�ӱ��м�����λ���"+sellInvSubTab.getMeasrCorpId()+"�����ڣ�����ʧ�ܣ�");
		}*/else {
			sellInvSubTabDao.updateSellInvSubTab(sellInvSubTab);
			on.put("isSuccess", true);
			on.put("message", "���۷�Ʊ���´���ɹ�");
		}
		return on;
	}

	@Override
	public Integer deleteSellInvSubTabBySellInvNumv(String sellInvNum) {
		int deleteResult = sellInvSubTabDao.deleteSellInvSubTabBySellInvNum(sellInvNum);		
		return deleteResult;
	}

	@Override
	public SellInvSubTab selectSellInvSubTabById(Integer ordrNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SellInvSubTab> selectSellInvSubTabList() {
		// TODO Auto-generated method stub
		return null;
	}

}
