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
//出入库调整单子表
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
			on.put("message", "出入库调整单中仓库编码"+outIntoWhsAdjSnglSubTab.getWhsEncd()+"不存在，新增失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(outIntoWhsAdjSnglSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "出入库调整单中存货编码"+outIntoWhsAdjSnglSubTab.getInvtyEncd()+"不存在，新增失败！");
		}else {
			outIntoWhsAdjSnglSubTabDao.insertOutIntoWhsAdjSnglSubTab(outIntoWhsAdjSnglSubTab);
			on.put("isSuccess", true);
			on.put("message", "新增成功");
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
			on.put("message", "出入库调整单子表新增失败,主键不能为空");
		}else if(whsDocDao.selectWhsDoc(outIntoWhsAdjSnglSubTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "出入库调整单子表中仓库编码"+outIntoWhsAdjSnglSubTab.getWhsEncd()+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(outIntoWhsAdjSnglSubTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "出入库调整单子表中存货编码"+outIntoWhsAdjSnglSubTab.getInvtyEncd()+"不存在，更新失败！");
		}/*else {
			outIntoWhsAdjSnglSubTabDao.updateOutIntoWhsAdjSnglSubTabByOrdrNum(outIntoWhsAdjSnglSubTab);
			on.put("isSuccess", true);
			on.put("message", "更新处理成功");
		}*/
		return on;
	}

	@Override
	public Integer deleteOutIntoWhsAdjSnglSubTabByFormNum(String formNum) {
		int deleteResult = outIntoWhsAdjSnglSubTabDao.deleteOutIntoWhsAdjSnglSubTabByFormNum(formNum);		
		return deleteResult;
	}

}
