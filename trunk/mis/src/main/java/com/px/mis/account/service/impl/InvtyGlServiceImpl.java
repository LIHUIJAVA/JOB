package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyGlDao;
import com.px.mis.account.entity.InvtyGl;
import com.px.mis.account.service.InvtyGlService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//存货总账实体类
@Service
@Transactional
public class InvtyGlServiceImpl implements InvtyGlService {
	@Autowired
	private InvtyGlDao invtyGlDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private WhsDocMapper whsDocDao;
	@Autowired
	private DeptDocDao deptDocDao;
//	private BizMemDoc bizMemDoc;
	@Override
	public ObjectNode insertInvtyGl(InvtyGl invtyGl) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyGl.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+invtyGl.getInvtyEncd()+"不存在，新增失败！");
		}else if(whsDocDao.selectWhsDoc(invtyGl.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编号"+invtyGl.getWhsEncd()+"不存在，新增失败！");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyGl.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "部门编号"+invtyGl.getDeptId()+"不存在，新增失败！");
		}else {
			int insertResult = invtyGlDao.insertInvtyGl(invtyGl);
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
	public ObjectNode updateInvtyGlByOrdrNum(InvtyGl invtyGl) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyGl.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if(invtyGlDao.selectInvtyGlByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "编号"+ordrNum+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyGl.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+invtyGl.getInvtyEncd()+"不存在，更新失败！");
		}else if(whsDocDao.selectWhsDoc(invtyGl.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编号"+invtyGl.getWhsEncd()+"不存在，更新失败！");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyGl.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "部门编号"+invtyGl.getDeptId()+"不存在，更新失败！");
		}else {
			int updateResult = invtyGlDao.updateInvtyGlByOrdrNum(invtyGl);
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
	public ObjectNode deleteInvtyGlByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyGlDao.selectInvtyGlByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = invtyGlDao.deleteInvtyGlByOrdrNum(ordrNum);
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
	public InvtyGl selectInvtyGlByOrdrNum(Integer ordrNum) {
		InvtyGl invtyGl = invtyGlDao.selectInvtyGlByOrdrNum(ordrNum);
		return invtyGl;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyGlList(Map map) {
		String resp="";
		List<InvtyGl> list = invtyGlDao.selectInvtyGlList(map);
		int count = invtyGlDao.selectInvtyGlCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyGl/selectInvtyGl", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}
}
