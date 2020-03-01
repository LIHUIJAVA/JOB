package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyCapOcupLaytTabDao;
import com.px.mis.account.entity.InvtyCapOcupLaytTab;
import com.px.mis.account.service.InvtyCapOcupLaytTabService;
import com.px.mis.purc.dao.DeptDocDao;
import com.px.mis.purc.dao.InvtyClsDao;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//存货资金占用规划表
@Service
@Transactional
public class InvtyCapOcupLaytTabServiceImpl implements InvtyCapOcupLaytTabService {
	@Autowired
	private InvtyCapOcupLaytTabDao invtyCapOcupLaytTabDao;
	@Autowired
	private DeptDocDao deptDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Autowired
	private InvtyClsDao invtyClsDao;
	@Autowired
	private WhsDocMapper WhsDocDao;
	@Override
	public ObjectNode insertInvtyCapOcupLaytTab(InvtyCapOcupLaytTab invtyCapOcupLaytTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		/*if(deptDocDao.selectDeptDocByDeptEncd(invtyCapOcupLaytTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "部门编号"+invtyCapOcupLaytTab.getDeptId()+"不存在，新增失败！");
		}else*/ if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyCapOcupLaytTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+invtyCapOcupLaytTab.getInvtyEncd()+"不存在，新增失败！");
		}else if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCapOcupLaytTab.getInvtyBigClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货大类编码"+invtyCapOcupLaytTab.getInvtyBigClsEncd()+"不存在，新增失败！");
		}else if(WhsDocDao.selectWhsDoc(invtyCapOcupLaytTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编码"+invtyCapOcupLaytTab.getWhsEncd()+"不存在，新增失败！");
		}else {
			int insertResult = invtyCapOcupLaytTabDao.insertInvtyCapOcupLaytTab(invtyCapOcupLaytTab);
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
	public ObjectNode updateInvtyCapOcupLaytTabByOrdrNum(InvtyCapOcupLaytTab invtyCapOcupLaytTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyCapOcupLaytTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if(invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "编号"+ordrNum+"不存在，更新失败！");
		}else if(invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(invtyCapOcupLaytTab.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+invtyCapOcupLaytTab.getOrdrNum()+"不存在！");
		}else if(deptDocDao.selectDeptDocByDeptEncd(invtyCapOcupLaytTab.getDeptId())==null){
			on.put("isSuccess", false);
			on.put("message", "部门编号"+invtyCapOcupLaytTab.getDeptId()+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(invtyCapOcupLaytTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+invtyCapOcupLaytTab.getInvtyEncd()+"不存在，更新失败！");
		}else if(invtyClsDao.selectInvtyClsByInvtyClsEncd(invtyCapOcupLaytTab.getInvtyBigClsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货大类编码"+invtyCapOcupLaytTab.getInvtyBigClsEncd()+"不存在，更新失败！");
		}else if(WhsDocDao.selectWhsDoc(invtyCapOcupLaytTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编码"+invtyCapOcupLaytTab.getWhsEncd()+"不存在，更新失败！");
		}else {
			int updateResult = invtyCapOcupLaytTabDao.updateInvtyCapOcupLaytTabByOrdrNum(invtyCapOcupLaytTab);
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
	public ObjectNode deleteInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = invtyCapOcupLaytTabDao.deleteInvtyCapOcupLaytTabByOrdrNum(ordrNum);
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
	public InvtyCapOcupLaytTab selectInvtyCapOcupLaytTabByOrdrNum(Integer ordrNum) {
		InvtyCapOcupLaytTab invtyCapOcupLaytTab = invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabByOrdrNum(ordrNum);
		return invtyCapOcupLaytTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyCapOcupLaytTabList(Map map) {
		String resp="";
		List<InvtyCapOcupLaytTab> list = invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabList(map);
		int count = invtyCapOcupLaytTabDao.selectInvtyCapOcupLaytTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyCapOcupLaytTab/selectInvtyCapOcupLaytTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
