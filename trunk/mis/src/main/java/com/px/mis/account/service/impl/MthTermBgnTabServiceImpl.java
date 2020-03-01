package com.px.mis.account.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.px.mis.account.dao.MthTermBgnTabDao;
import com.px.mis.account.entity.MthTermBgnTab;
import com.px.mis.account.service.MthTermBgnTabService;
import com.px.mis.purc.dao.InvtyDocDao;
import com.px.mis.util.BaseJson;
import com.px.mis.util.JacksonUtil;
import com.px.mis.whs.dao.WhsDocMapper;
//各月期初表实体类
@Service
@Transactional
public class MthTermBgnTabServiceImpl implements MthTermBgnTabService {
	@Autowired
	private MthTermBgnTabDao mthTermBgnTabDao;
	@Autowired
	private WhsDocMapper whsDocDao;
	@Autowired
	private InvtyDocDao invtyDocDao;
	@Override
	public ObjectNode insertMthTermBgnTab(MthTermBgnTab mthTermBgnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(whsDocDao.selectWhsDoc(mthTermBgnTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编码"+mthTermBgnTab.getWhsEncd()+"不存在，新增失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(mthTermBgnTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+mthTermBgnTab.getInvtyEncd()+"不存在，新增失败！");
		}else {
			int insertResult = mthTermBgnTabDao.insertMthTermBgnTab(mthTermBgnTab);
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
	public ObjectNode updateMthTermBgnTabByOrdrNum(MthTermBgnTab mthTermBgnTab) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer ordrNum = mthTermBgnTab.getOrdrNum();
		if(ordrNum==null) {
			on.put("isSuccess", false);
			on.put("message", "更新失败,主键不能为空");
		}else if(mthTermBgnTabDao.selectMthTermBgnTabByOrdrNum(ordrNum)==null){
			on.put("isSuccess", false);
			on.put("message", "编号"+ordrNum+"不存在，更新失败！");
		}else if(whsDocDao.selectWhsDoc(mthTermBgnTab.getWhsEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "仓库编码"+mthTermBgnTab.getWhsEncd()+"不存在，更新失败！");
		}else if(invtyDocDao.selectInvtyDocByInvtyDocEncd(mthTermBgnTab.getInvtyEncd())==null){
			on.put("isSuccess", false);
			on.put("message", "存货编码"+mthTermBgnTab.getInvtyEncd()+"不存在，更新失败！");
		}else {
			
			int updateResult = mthTermBgnTabDao.updateMthTermBgnTabByOrdrNum(mthTermBgnTab);
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
	public ObjectNode deleteMthTermBgnTabByOrdrNum(Integer ordrNum) {
		ObjectNode on=null;
		try {
			on = JacksonUtil.getObjectNode("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mthTermBgnTabDao.selectMthTermBgnTabByOrdrNum(ordrNum)==null) {
			on.put("isSuccess", false);
			on.put("message", "删除失败，编号"+ordrNum+"不存在！");
		}else {
			int deleteResult = mthTermBgnTabDao.deleteMthTermBgnTabByOrdrNum(ordrNum);
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
	public MthTermBgnTab selectMthTermBgnTabByOrdrNum(Integer ordrNum) {
		MthTermBgnTab mthTermBgnTab = mthTermBgnTabDao.selectMthTermBgnTabByOrdrNum(ordrNum);
		return mthTermBgnTab;
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public String selectMthTermBgnTabList(Map map) {
		String resp="";
		List<MthTermBgnTab> list = mthTermBgnTabDao.selectMthTermBgnTabList(map);
		int count = mthTermBgnTabDao.selectMthTermBgnTabCount();
		int listNum=0;
		if(list!=null) {
			listNum=list.size();
		}
		try {
			int pageNo = (int) map.get("pageNo");
			int pageSize = (int) map.get("pageSize");
			int pages = (count+pageSize-1)/pageSize;
			resp=BaseJson.returnRespList("/account/mthTermBgnTab/selectMthTermBgnTab", true, "查询成功！", count,pageNo,pageSize,listNum,pages, list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resp;
	}

}
