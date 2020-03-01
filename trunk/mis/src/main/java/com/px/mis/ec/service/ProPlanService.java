package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.ProPlan;
import com.px.mis.ec.entity.ProPlans;

public interface ProPlanService {

	public String add(ProPlan proPlan,List<ProPlans> proPlansList);
	
	public String edit(ProPlan proPlan,List<ProPlans> proPlansList);
	
	public String delete(String proPlanId);
	
	public String query(String proPlanId);
	
	public String queryList(Map map);
	
}
