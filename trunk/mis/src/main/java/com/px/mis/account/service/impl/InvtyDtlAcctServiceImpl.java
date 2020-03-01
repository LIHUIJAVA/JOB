package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.InvtyDtlAcctDao;
import com.px.mis.account.entity.InvtyDtlAcct;
import com.px.mis.account.service.InvtyDtlAcctService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
//存货明细账
@Service
@Transactional
public class InvtyDtlAcctServiceImpl implements InvtyDtlAcctService {
	@Autowired
	private InvtyDtlAcctDao invtyDtlAcctDao;
	@Override
	public ObjectNode insertInvtyDtlAcct(InvtyDtlAcct invtyDtlAcct) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
			int insertResult = invtyDtlAcctDao.insertInvtyDtlAcct(invtyDtlAcct);
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
	public ObjectNode updateInvtyDtlAcctByOrdrNum(InvtyDtlAcct invtyDtlAcct) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = invtyDtlAcct.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if(invtyDtlAcctDao.selectInvtyDtlAcctByOrdrNum(invtyDtlAcct.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+invtyDtlAcct.getOrdrNum()+"不存在！");
		}else {
			int updateResult = invtyDtlAcctDao.updateInvtyDtlAcctByOrdrNum(invtyDtlAcct);
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
	public ObjectNode deleteInvtyDtlAcctByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (invtyDtlAcctDao.selectInvtyDtlAcctByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = invtyDtlAcctDao.deleteInvtyDtlAcctByOrdrNum(ordrNum);
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
	public InvtyDtlAcct selectInvtyDtlAcctByOrdrNum(Integer ordrNum) {
		InvtyDtlAcct invtyDtlAcct = invtyDtlAcctDao.selectInvtyDtlAcctByOrdrNum(ordrNum);
		return invtyDtlAcct;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectInvtyDtlAcctList(Map map) {
		String resp="";
		List<InvtyDtlAcct> list = invtyDtlAcctDao.selectInvtyDtlAcctList(map);
		int count = invtyDtlAcctDao.selectInvtyDtlAcctCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/invtyDtlAcct/selectInvtyDtlAcct", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
