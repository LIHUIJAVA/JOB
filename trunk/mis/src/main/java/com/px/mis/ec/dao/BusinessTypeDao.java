package com.px.mis.ec.dao;

import java.util.List;
import java.util.Map;

import com.px.mis.ec.entity.BusinessType;

public interface BusinessTypeDao {

	public void insert(BusinessType businessType);
	
	public void update(BusinessType businessType);
	
	public void delete(String busTypeId);
	
	public BusinessType select(String busTypeId);
	
	public List<BusinessType> selectList(Map map);
	
	public int selectCount(Map map);
	
}
