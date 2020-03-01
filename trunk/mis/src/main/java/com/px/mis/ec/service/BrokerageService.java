package com.px.mis.ec.service;

import java.util.Map;

import com.px.mis.ec.entity.Brokerage;

public interface BrokerageService {

	public String add(Brokerage brokerage);
	
	public String edit(Brokerage brokerage);
	
	public String delete(String brokId);
	
	public String query(String brokId);
	
	public String queryList(Map map);
}
