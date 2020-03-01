package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.ProActivity;
import com.px.mis.ec.entity.ProActivitys;

public interface ProActivityService {

	public String add(ProActivity proAct,List<ProActivitys> proActsList);
	
	public String edit(ProActivity proAct,List<ProActivitys> proActsList);
	
	public String delete(String proActId);
	
	public String query(String proActId);
	
	public String queryList(Map map);
	
	public String updateAudit(List<ProActivity> activities);
	public String updateAuditNo(List<ProActivity> activities);

}
