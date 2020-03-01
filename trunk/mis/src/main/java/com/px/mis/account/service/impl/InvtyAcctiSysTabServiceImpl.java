package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyAcctiSysTabDao;
import com.px.mis.account.entity.InvtyAcctiSysTab;
import com.px.mis.account.service.InvtyAcctiSysTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class InvtyAcctiSysTabServiceImpl implements InvtyAcctiSysTabService {
	@Autowired
	private InvtyAcctiSysTabDao invtyAcctiSysTabDao;
	@Override
	public ObjectNode insertInvtyAcctiSysTab(InvtyAcctiSysTab invtyAcctiSysTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		int insertResult = invtyAcctiSysTabDao.insertInvtyAcctiSysTab(invtyAcctiSysTab);
		if(insertResult==1) {
			on.put("isSuccess", true);
			on.put("message", "新增成功");
		}else {
			on.put("isSuccess", false);
			on.put("message", "新增失败");
		}
		return on;
	}

	@Override
	public ObjectNode updateInvtyAcctiSysTabByOrdrNum(InvtyAcctiSysTab invtyAcctiSysTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyAcctiSysTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if(invtyAcctiSysTabDao.selectInvtyAcctiSysTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "编号"+ordrNum+"不存在，更新失败！");
		}else if(invtyAcctiSysTabDao.selectInvtyAcctiSysTabByOrdrNum(invtyAcctiSysTab.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+invtyAcctiSysTab.getOrdrNum()+"不存在！");
		}else {
			int updateResult = invtyAcctiSysTabDao.updateInvtyAcctiSysTabByOrdrNum(invtyAcctiSysTab);
			if(updateResult==1) {
				on.put("isSuccess", true);
				on.put("message", "更新成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "更新失败");
			}
		}
		return on;
	}

	@Override
	public ObjectNode deleteInvtyAcctiSysTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyAcctiSysTabDao.selectInvtyAcctiSysTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			
			int deleteResult = invtyAcctiSysTabDao.deleteInvtyAcctiSysTabByOrdrNum(ordrNum);
			if(deleteResult==1) {
				on.put("isSuccess", true);
				on.put("message", "删除成功");
			}else if(deleteResult==0){
				on.put("isSuccess", true);
				on.put("message", "没有要删除的数据");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "删除失败");
			}
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public InvtyAcctiSysTab selectInvtyAcctiSysTabByOrdrNum(Integer ordrNum) {
		InvtyAcctiSysTab invtyAcctiSysTab = invtyAcctiSysTabDao.selectInvtyAcctiSysTabByOrdrNum(ordrNum);
		return invtyAcctiSysTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyAcctiSysTabList(Map map) {
		String resp="";
		List<InvtyAcctiSysTab> list = invtyAcctiSysTabDao.selectInvtyAcctiSysTabList(map);
		int count = invtyAcctiSysTabDao.selectInvtyAcctiSysTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyAcctiSysTab/selectInvtyAcctiSysTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
