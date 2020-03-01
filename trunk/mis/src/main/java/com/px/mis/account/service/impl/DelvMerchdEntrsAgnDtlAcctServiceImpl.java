package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.DelvMerchdEntrsAgnDtlAcctDao;
import com.px.mis.account.entity.DelvMerchdEntrsAgnDtlAcct;
import com.px.mis.account.service.DelvMerchdEntrsAgnDtlAcctService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class DelvMerchdEntrsAgnDtlAcctServiceImpl implements DelvMerchdEntrsAgnDtlAcctService {
	@Autowired
	private DelvMerchdEntrsAgnDtlAcctDao delvMerchdEntrsAgnDtlAcctDao;
	@Override
	public ObjectNode insertDelvMerchdEntrsAgnDtlAcct(DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
			int insertResult = delvMerchdEntrsAgnDtlAcctDao.insertDelvMerchdEntrsAgnDtlAcct(delvMerchdEntrsAgnDtlAcct);
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
	public ObjectNode updateDelvMerchdEntrsAgnDtlAcctByOrdrNum(DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		Integer ordrNum = delvMerchdEntrsAgnDtlAcct.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if (delvMerchdEntrsAgnDtlAcctDao.selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(delvMerchdEntrsAgnDtlAcct.getOrdrNum())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+delvMerchdEntrsAgnDtlAcct.getOrdrNum()+"不存在！");
		}else {
			int updateResult = delvMerchdEntrsAgnDtlAcctDao.updateDelvMerchdEntrsAgnDtlAcctByOrdrNum(delvMerchdEntrsAgnDtlAcct);
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
	public ObjectNode deleteDelvMerchdEntrsAgnDtlAcctByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		if (delvMerchdEntrsAgnDtlAcctDao.selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = delvMerchdEntrsAgnDtlAcctDao.deleteDelvMerchdEntrsAgnDtlAcctByOrdrNum(ordrNum);
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
	public DelvMerchdEntrsAgnDtlAcct selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(Integer ordrNum) {
		DelvMerchdEntrsAgnDtlAcct delvMerchdEntrsAgnDtlAcct = delvMerchdEntrsAgnDtlAcctDao.selectDelvMerchdEntrsAgnDtlAcctByOrdrNum(ordrNum);
		return delvMerchdEntrsAgnDtlAcct;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectDelvMerchdEntrsAgnDtlAcctList(Map map) {
		String resp="";
		List<DelvMerchdEntrsAgnDtlAcct> list = delvMerchdEntrsAgnDtlAcctDao.selectDelvMerchdEntrsAgnDtlAcctList(map);
		int count = delvMerchdEntrsAgnDtlAcctDao.selectDelvMerchdEntrsAgnDtlAcctCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/delvMerchdEntrsAgnDtlAcct/selectDelvMerchdEntrsAgnDtlAcct", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
