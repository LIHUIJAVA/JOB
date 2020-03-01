package com.px.mis.ec.service;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.EcExpress;

public interface EcExpressService {
	
	public String delete(String platId);
	
	public String selectList(Map map);
	
	public String insertList(List<EcExpress> expresslist);
	
	public String download(String platId);
	
	public String selectList(String jsonBody);

}
