package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.ProActivity;
import com.px.mis.ec.entity.ProPlan;

public interface ProPlanDao {

	public void insert(ProPlan proPlan);
	
	public void update(ProPlan proPlan);
	
	public void delete(List<String> proPlanIdList);
	
	public ProPlan select(String proPlanId);
	
	public List<ProPlan> selectList(Map map);
	
	public int selectCount(Map map);
	
}
