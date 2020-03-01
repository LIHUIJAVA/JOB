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
			on.put("message", "采购发票子表id"+curmthIntoWhsCostTab.getPursInvSubTabId()+"不存在，新增失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(curmthIntoWhsCostTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+curmthIntoWhsCostTab.getInvtyEncd()+"不存在，新增失败！");
		}else {
			int insertResult = curmthIntoWhsCostTabDao.insertCurmthIntoWhsCostTab(curmthIntoWhsCostTab);
			if(insertResult==1) {
				on.put("isSuccess", true);
				on.put("message", "新增成功");
			}else {
				on.put("isSuccess", false);
				on.put("message", "新增失败");
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
			on.put("message", "更新失败，主键不能为null！！！");
		}else if (curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+ordrNum+"不存在！");
		}else if(pursInvSubTabDao.selectPursInvSubTabByOrdrNum(curmthIntoWhsCostTab.getPursInvSubTabId())==null){
			on.put("isSuccess", false);
			on.put("message", "采购发票子表id"+curmthIntoWhsCostTab.getPursInvSubTabId()+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(curmthIntoWhsCostTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+curmthIntoWhsCostTab.getInvtyEncd()+"不存在，更新失败！");
		}else {
			int updateResult = curmthIntoWhsCostTabDao.updateCurmthIntoWhsCostTabById(curmthIntoWhsCostTab);
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
	public ObjectNode deleteCurmthIntoWhsCostTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (curmthIntoWhsCostTabDao.selectCurmthIntoWhsCostTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = curmthIntoWhsCostTabDao.deleteCurmthIntoWhsCostTabByOrdrNum(ordrNum);
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
			resp=BaseJson.returnRespList("/account/curmthIntoWhsCostTab/selectCurmthIntoWhsCostTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
