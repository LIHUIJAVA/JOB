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
//销售发票子表
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
			on.put("message", "销售发票子表中仓库编号"+sellInvSubTab.getWhsEncd()+"不存在，新增失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(sellInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "销售发票子表中存货编码"+sellInvSubTab.getInvtyEncd()+"不存在，新增失败！");
		}/*else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(sellInvSubTab.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "销售发票子表中计量单位编号"+sellInvSubTab.getMeasrCorpId()+"不存在，新增失败！");
		}*/else {
			sellInvSubTabDao.insertSellInvSubTab(sellInvSubTab);
			on.put("isSuccess", true);
			on.put("message", "销售发票新增处理成功");
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
			on.put("message", "销售发票子表更新失败,主键不能为空");
		}else if(sellInvSubTabDao.selectSellInvSubTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "销售发票子表中序号"+ordrNum+"不存在，更新失败！");
		}else if(whsDocDao.selectWhsDoc(sellInvSubTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "销售发票子表中仓库编号"+sellInvSubTab.getWhsEncd()+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(sellInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "销售发票子表中存货编码"+sellInvSubTab.getInvtyEncd()+"不存在，更新失败！");
		}/*else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(sellInvSubTab.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "销售发票子表中计量单位编号"+sellInvSubTab.getMeasrCorpId()+"不存在，更新失败！");
		}*/else {
			sellInvSubTabDao.updateSellInvSubTab(sellInvSubTab);
			on.put("isSuccess", true);
			on.put("message", "销售发票更新处理成功");
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
