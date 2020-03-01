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
//采购发票子表
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
			on.put("message", "该存货编码"+pursInvSubTab.getInvtyEncd()+"不存在，新增失败！");
		}/*else if(measrCorpDocDao.selectMeasrCorpDocByMeasrCorpId(pursInvSubTab.getMeasrCorpId())==null){
			on.put("isSuccess", false);
			on.put("message", "该计量单位"+pursInvSubTab.getMeasrCorpId()+"不存在，新增失败！");
		}*/else {
			int a = pursInvSubTabDao.insertPursInvSubTab(pursInvSubTab);
			if(a>=0) {
				on.put("isSuccess", true);
				on.put("message", "采购发票新增成功");
			}else {
				on.put("isSuccess", true);
				on.put("message", "采购发票新增失败");
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
			on.put("message", "采购发票子表更新失败,主键不能为空");
		}else if(pursInvSubTabDao.selectPursInvSubTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "采购发票子表中序号"+ordrNum+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(pursInvSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "该存货编码"+pursInvSubTab.getInvtyEncd()+"不存在，更新失败！");
		}else {
			int a = pursInvSubTabDao.updatePursInvSubTabByOrdrNum(pursInvSubTab);
			if(a>=0) {
				on.put("isSuccess", true);
				on.put("message", "采购发票更新处理成功");
			}else {
				on.put("isSuccess", true);
				on.put("message", "采购发票更新处理失败");
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
