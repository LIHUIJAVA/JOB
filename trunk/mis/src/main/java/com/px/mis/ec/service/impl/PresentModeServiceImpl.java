package com.px.mis.ec.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.TaxSubjDao;
import com.px.mis.account.entity.TaxSubj;
import com.px.mis.ec.dao.PresentModeDao;
import com.px.mis.ec.entity.PresentMode;
import com.px.mis.ec.service.PresentModeService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class PresentModeServiceImpl implements PresentModeService {
	@Autowired
	private PresentModeDao presentModeDao;
	@Override
	public ObjectNode insertPresentMode(PresentMode presentMode) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(presentModeDao.selectPresentModeByPresentModeCode(presentMode.getPresentModeCode())!=null){
			on.put("isSuccess", false);
			on.put("message", "赠品方式编码"+presentMode.getPresentModeCode()+"已经存在，新增失败！");
		}else {
			int insertResult = presentModeDao.insertPresentMode(presentMode);
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
	public ObjectNode updatePresentModeById(PresentMode presentMode) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		PresentMode presentModeOne = presentModeDao.selectPresentModeByPresentModeCode(presentMode.getPresentModeCode());
		if (presentModeDao.selectPresentModeById(presentMode.getNo())==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+presentMode.getNo()+"不存在！");
		}else if(presentModeOne!=null && presentModeOne.getNo()!=presentMode.getNo()){
			on.put("isSuccess", false);
			on.put("message", "赠品方式编码"+presentMode.getPresentModeCode()+"已经存在，更新失败！");
		}else {
			int updateResult = presentModeDao.updatePresentModeById(presentMode);
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
	public ObjectNode deletePresentModeById(Integer no) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (presentModeDao.selectPresentModeById(no)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，序号"+no+"不存在！");
		}else {
			int deleteResult = presentModeDao.deletePresentModeById(no);
			if(deleteResult==1) {
				on.put("isSuccess", true);
				on.put("message", "处理成功");
			}else if(deleteResult==0){
				on.put("isSuccess", true);
				on.put("message", "没有要删除的数据");		
			}else {
				on.put("isSuccess", false);
				on.put("message", "处理失败");
			}
		}
		return on;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public PresentMode selectPresentModeById(Integer no) {
		PresentMode presentMode = presentModeDao.selectPresentModeById(no);
		return presentMode;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectPresentModeList(Map map) {
		String resp="";
		List<PresentMode> list = presentModeDao.selectPresentModeList(map);
		int count = presentModeDao.selectPresentModeCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
