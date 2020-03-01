package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyTermBgnTabDao;
import com.px.mis.account.entity.InvtyTermBgnTab;
import com.px.mis.account.service.InvtyTermBgnTabService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//库存期初表实体类
@Service
@Transactional
public class InvtyTermBgnTabServiceImpl implements InvtyTermBgnTabService {
	@Autowired
	private InvtyTermBgnTabDao invtyTermBgnTabDao;
	@Autowired
	private WhsDocMapper whsDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private DeptDocDao deptDocDao;
	@Override
	public ObjectNode insertInvtyTermBgnTab(InvtyTermBgnTab invtyTermBgnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(whsDocDao.selectWhsDoc(invtyTermBgnTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编号"+invtyTermBgnTab.getWhsEncd()+"不存在，新增失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyTermBgnTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+invtyTermBgnTab.getInvtyEncd()+"不存在，新增失败！");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyTermBgnTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "部门编码"+invtyTermBgnTab.getDeptId()+"不存在，新增失败！");
		}else {
			int insertResult = invtyTermBgnTabDao.insertInvtyTermBgnTab(invtyTermBgnTab);
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
	public ObjectNode updateInvtyTermBgnTabByOrdrNum(InvtyTermBgnTab invtyTermBgnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = invtyTermBgnTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if(invtyTermBgnTabDao.selectInvtyTermBgnTabByOrdrNum(invtyTermBgnTab.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+invtyTermBgnTab.getOrdrNum()+"不存在！");
		}else if(whsDocDao.selectWhsDoc(invtyTermBgnTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编号"+invtyTermBgnTab.getWhsEncd()+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyTermBgnTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+invtyTermBgnTab.getInvtyEncd()+"不存在，更新失败！");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyTermBgnTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "部门编码"+invtyTermBgnTab.getDeptId()+"不存在，更新失败！");
		}else {
			int updateResult = invtyTermBgnTabDao.updateInvtyTermBgnTabByOrdrNum(invtyTermBgnTab);
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
	public ObjectNode deleteInvtyTermBgnTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (invtyTermBgnTabDao.selectInvtyTermBgnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = invtyTermBgnTabDao.deleteInvtyTermBgnTabByOrdrNum(ordrNum);
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
	public InvtyTermBgnTab selectInvtyTermBgnTabByOrdrNum(Integer ordrNum) {
		InvtyTermBgnTab invtyTermBgnTab = invtyTermBgnTabDao.selectInvtyTermBgnTabByOrdrNum(ordrNum);
		return invtyTermBgnTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyTermBgnTabList(Map map) {
		String resp="";
		List<InvtyTermBgnTab> list = invtyTermBgnTabDao.selectInvtyTermBgnTabList(map);
		int count = invtyTermBgnTabDao.selectInvtyTermBgnTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyTermBgnTab/selectInvtyTermBgnTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
