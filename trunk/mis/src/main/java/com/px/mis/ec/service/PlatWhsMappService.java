package com.px.mis.ec.service;

import java.util.Map;

public interface PlatWhsMappService {
	
	public String select(Map map);
	
	public String insert(String jsonbody);
	public String update(String jsonbody);
	public String delete(String jsonbody);
	public String selectList(String jsonbody);

}
