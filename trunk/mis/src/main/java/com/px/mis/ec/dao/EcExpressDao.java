package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.EcExpress;

public interface EcExpressDao {
	public void insert(List<EcExpress> expresslist);
	
	public void delete(String platId);
	
	public EcExpress select(String platId,String companyCode,String province,String city,String country,String address);

	//express_code_and_name
	public Map exSelect(String jsonBody);
	
	public List<EcExpress> selectList(Map map);
	
	public int selectCount(Map map);
	
}
