package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.VouchSupvnTabDao;
import com.px.mis.account.entity.VouchSupvnTab;
import com.px.mis.account.service.VouchSupvnTabService;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
@Service
@Transactional
public class VouchSupvnTabServiceImpl implements VouchSupvnTabService {
	@Autowired
	private VouchSupvnTabDao vouchSupvnTabDao;
	public ObjectNode insertVouchSupvnTab(VouchSupvnTab vouchSupvnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int insertResult = vouchSupvnTabDao.insertVouchSupvnTab(vouchSupvnTab);
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
	public ObjectNode updateVouchSupvnTabByOrdrNum(VouchSupvnTab vouchSupvnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = vouchSupvnTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if (vouchSupvnTabDao.selectVouchSupvnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败，序号"+ordrNum+"不存在！");
		}else {
			int updateResult = vouchSupvnTabDao.updateVouchSupvnTabByOrdrNum(vouchSupvnTab);
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
	public ObjectNode deleteVouchSupvnTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (vouchSupvnTabDao.selectVouchSupvnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = vouchSupvnTabDao.deleteVouchSupvnTabByOrdrNum(ordrNum);
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
	public VouchSupvnTab selectVouchSupvnTabByOrdrNum(Integer ordrNum) {
		VouchSupvnTab vouchSupvnTab = vouchSupvnTabDao.selectVouchSupvnTabByOrdrNum(ordrNum);
		return vouchSupvnTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectVouchSupvnTabList(Map map) {
		String resp="";
		List<VouchSupvnTab> list = vouchSupvnTabDao.selectVouchSupvnTabList(map);
		int count = vouchSupvnTabDao.selectVouchSupvnTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/taxSubj/selectTaxSubj", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
